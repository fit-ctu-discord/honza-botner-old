package dev.vrba.botner.discord.reactions.verification;

import dev.vrba.botner.discord.reactions.ReactionHandler;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.api.event.message.reaction.ReactionRemoveEvent;
import org.jetbrains.annotations.NotNull;

public class VerificationMessageReactionHandler extends ReactionHandler
{
    @Override
    public void handleReactionAdded(@NotNull ReactionAddEvent event)
    {
    }

    @Override
    public void handleReactionRemoved(@NotNull ReactionRemoveEvent event)
    {

    }
}
