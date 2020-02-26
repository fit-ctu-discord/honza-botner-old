package dev.vrba.botner.discord.reactions;

import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.api.event.message.reaction.ReactionRemoveEvent;
import org.jetbrains.annotations.NotNull;

public abstract class ReactionHandler
{
    public abstract void handleReactionAdded(@NotNull ReactionAddEvent event);
    public abstract void handleReactionRemoved(@NotNull ReactionRemoveEvent event);
}
