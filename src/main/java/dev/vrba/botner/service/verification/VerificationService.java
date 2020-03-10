package dev.vrba.botner.service.verification;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import dev.vrba.botner.config.BotnerConfiguration;
import dev.vrba.botner.database.DatabaseConnection;
import dev.vrba.botner.database.entities.UserVerification;
import io.github.cdimascio.dotenv.Dotenv;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VerificationService
{
    private Logger logger = Logger.getGlobal();

    private BotnerConfiguration configuration;

    private Dotenv env;

    private Dao<UserVerification, Long> dao;

    private DiscordApi api;


    public VerificationService(DiscordApi api) throws SQLException
    {
        ConnectionSource source = DatabaseConnection.getGlobalInstance().getSource();

        this.configuration = BotnerConfiguration.getGlobalInstance();
        this.dao = DaoManager.createDao(source, UserVerification.class);
        this.env = Dotenv.load();
        this.api = api;

        TableUtils.createTableIfNotExists(source, UserVerification.class);
    }

    public Optional<UserVerification> getVerification(long userId)
    {
        List<UserVerification> verifications;

        try
        {
            verifications = this.dao.queryForEq("id", userId);
        }
        catch (SQLException exception)
        {
            this.logger.log(Level.SEVERE, exception.getMessage());
            return Optional.empty();
        }

        if (verifications.isEmpty())
        {
            return Optional.empty();
        }

        return Optional.of(verifications.get(0));
    }

    public Optional<UserVerification> getVerification(String verificationCode)
    {
        List<UserVerification> verifications;

        try
        {
            verifications = this.dao.queryForEq("verificationCode", verificationCode);
        }
        catch (SQLException exception)
        {
            this.logger.log(Level.SEVERE, exception.getMessage());
            return Optional.empty();
        }

        if (verifications.isEmpty())
        {
            return Optional.empty();
        }

        return Optional.of(verifications.get(0));
    }

    public String getVerificationLink(long userId)
    {
        UserVerification verification = null;

        try
        {
            verification = this.dao.queryForId(userId);

        }
        catch (SQLException exception)
        {
            this.logger.log(Level.SEVERE, exception.getMessage());
        }


        if (verification != null && !verification.isVerified())
        {
            return this.env.get("SERVER_URL") + "/authenticate/" + verification.getVerificationCode();
        }

        // User has not requested a verification yet
        verification = new UserVerification(userId);

        try
        {
            this.dao.createOrUpdate(verification);
        }
        catch (SQLException exception)
        {
            return "**Při vytváření odkazu nastala chyba.**";
        }

        return this.env.get("SERVER_URL") + "/authenticate/" + verification.getVerificationCode();
    }

    public String getRedirectLink()
    {

        String id = env.get("CVUT_AUTH_ID");
        String redirect = env.get("CVUT_AUTH_REDIRECT");

        return "https://auth.fit.cvut.cz/oauth/authorize?response_type=code&client_id=" + id + "&redirect_uri=" + redirect;
    }

    public boolean verifyCode(String code)
    {
        String authorizationSource = this.env.get("CVUT_AUTH_ID") + ":" + this.env.get("CVUT_AUTH_SECRET");
        String authorization = "Basic " + new String(Base64.getEncoder().encode(authorizationSource.getBytes()));

        HttpResponse<JsonNode> response = Unirest.post("https://auth.fit.cvut.cz/oauth/token")
                .field("grant_type", "authorization_code")
                .field("code", code)
                .field("redirect_uri", this.env.get("CVUT_AUTH_REDIRECT"))
                .header("Authorization", authorization)
                .asJson();

        return response.isSuccess();
    }

    public void verify(String verificationCode)
    {
        this.logger.log(Level.INFO, verificationCode);
        try
        {
            Optional<UserVerification> _verification = this.getVerification(verificationCode);

            if (_verification.isEmpty())
            {
                return;
            }

            UserVerification verification = _verification.get();
            verification.setVerified(true);

            this.dao.update(verification);

            // Assign role
            Optional<Server> _server = api.getServerById(this.configuration.server);

            if (_server.isPresent())
            {
                Server server = _server.get();
                User user = api.getUserById(verification.getId()).get();

                this.logger.log(Level.INFO, user.getNicknameMentionTag());

                Optional<Role> role = server.getRoles()
                        .stream()
                        .filter(item -> item.getId() == this.configuration.verification.role)
                        .findFirst();

                if (role.isEmpty())
                {
                    this.logger.log(Level.SEVERE, "Cannot find role with id " + this.configuration.verification.role);
                    return;
                }

                this.logger.log(Level.INFO, role.get().getMentionTag());

                server.addRoleToUser(user, role.get());
            }
            else
            {
                this.logger.log(Level.SEVERE, "Cannot find server with id " + this.configuration.server);
            }

        }
        catch (Exception exception)
        {
            this.logger.log(Level.SEVERE, exception.getMessage());
        }
    }
}
