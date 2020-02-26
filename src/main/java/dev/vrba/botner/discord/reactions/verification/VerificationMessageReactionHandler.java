package dev.vrba.botner.discord.reactions.verification;

import dev.vrba.botner.config.BotnerConfiguration;
import dev.vrba.botner.config.VerificationMessageConfiguration;
import dev.vrba.botner.discord.reactions.ReactionHandler;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.api.event.message.reaction.ReactionRemoveEvent;
import org.jetbrains.annotations.NotNull;

public class VerificationMessageReactionHandler extends ReactionHandler
{
    @Override
    public void handleReactionAdded(@NotNull ReactionAddEvent event)
    {
        final VerificationMessageConfiguration configuration = BotnerConfiguration.getGlobalInstance().verificationMessage;

        final long verificationMessageId = configuration.id;
        final String reactionEmoji = configuration.emoji;


        if (
            event.getMessageId() == verificationMessageId &&
            event.getEmoji().equalsEmoji(reactionEmoji)
        )
        {
            // TODO
            event.getChannel().sendMessage("Let the verification ritual begin.");
        }
    }

    @Override
    public void handleReactionRemoved(@NotNull ReactionRemoveEvent event) {}
}
