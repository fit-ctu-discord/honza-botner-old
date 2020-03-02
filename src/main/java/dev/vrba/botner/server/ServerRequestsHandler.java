package dev.vrba.botner.server;

import dev.vrba.botner.database.entities.UserVerification;
import dev.vrba.botner.service.verification.VerificationService;
import express.DynExpress;
import express.http.Cookie;
import express.http.request.Request;
import express.http.response.Response;
import org.javacord.api.DiscordApi;

import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Optional;

public class ServerRequestsHandler
{
    private VerificationService service;

    public ServerRequestsHandler(DiscordApi api) throws SQLException
    {
        this.service = new VerificationService(api);
    }

    @DynExpress(context = "/")
    public void index(Request request, Response response)
    {
        response.send("It' works!");
    }

    @DynExpress(context = "/callback")
    public void callback(Request request, Response response)
    {
        String code = request.getQuery("code");
        String authId = request.getCookie("Auth-ID").getValue();
        response.setContentType("text/html; charset=utf-8");

        if (code == null || authId == null)
        {
            response.send("Missing code.");
            return;
        }

        if (this.service.verifyCode(code))
        {
            response.send("Autentizace proběhla úspěšně. Během chvíle ti bude na Discordu přidělena role.");
            this.service.verify(authId);
            return;
        }

        response.send("Bohužel tě nebylo možné autentizovat. V případě potíží napiš na Discordu @Mod.");
    }

    @DynExpress(context = "/authorize/:id")
    public void redirect(Request request, Response response)
    {
        String id = request.getParam("id");

        Optional<UserVerification> verification = service.getVerification(id);

        if (verification.isPresent())
        {
            response.setCookie(new Cookie("Auth-ID", verification.get().getVerificationCode()));
            response.redirect(service.getRedirectLink());
            return;
        }

        response.send("Invalid verification id.");
    }

    @DynExpress(context = "/authorized")
    public void success(Request request, Response response)
    {

    }
}
