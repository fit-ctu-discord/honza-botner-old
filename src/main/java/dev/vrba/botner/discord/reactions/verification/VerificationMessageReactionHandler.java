package dev.vrba.botner.discord.reactions.verification;

import dev.vrba.botner.config.BotnerConfiguration;
import dev.vrba.botner.config.VerificationMessageConfiguration;
import dev.vrba.botner.database.entities.UserVerification;
import dev.vrba.botner.discord.reactions.ReactionHandler;
import dev.vrba.botner.service.verification.VerificationService;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.api.event.message.reaction.ReactionRemoveEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VerificationMessageReactionHandler extends ReactionHandler
{
    private VerificationService service;

    @Override
    public void handleReactionAdded(@NotNull ReactionAddEvent event)
    {
        final VerificationMessageConfiguration configuration = BotnerConfiguration.getGlobalInstance().verification;

        final long verificationMessageId = configuration.id;
        final String reactionEmoji = configuration.emoji;

        // Only for registered message.
        if (event.getMessageId() != verificationMessageId || !event.getEmoji().equalsEmoji(reactionEmoji)) return;

        if (this.service == null)
        {
            try
            {
                this.service = new VerificationService(event.getApi());
            }
            catch (SQLException exception)
            {
                Logger.getGlobal().log(Level.SEVERE, exception.getMessage());
                event.getUser().sendMessage("Při vytváření instance verifikační služby došlo k chybě. Pracujeme na opravě.");
                return;
            }
        }

        Optional<UserVerification> verification = this.service.getVerification(event.getUser().getId());

        if (verification.isPresent() && verification.get().isVerified())
        {
            event.getUser().sendMessage(
                    "Už jsi verifikován. V případě potíží kontaktuj moderátory.\n" +
                    "Tvoje referenční ID: **" + event.getUser().getIdAsString() + "**"
            );
            return;
        }

        event.getUser().sendMessage(
                "Ahoj, zažádal jsi o verifikaci na FIT ČVUT serveru.\n\n" +
                        "Pro ověření navštiv následující unikátní odkaz:\n" +
                        "<" + this.service.getVerificationLink(event.getUser().getId()) + ">\n\n" +
                        "Po autentizaci ti bude zpřístupněn server. V případě dotazů neváhej kontaktovat moderátory."
        );
    }

    @Override
    public void handleReactionRemoved(@NotNull ReactionRemoveEvent event)
    {
    }
}
