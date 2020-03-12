package dev.vrba.botner.discord;

import dev.vrba.botner.discord.reactions.ReactionHandler;
import dev.vrba.botner.discord.reactions.emoji.EmojiCounterReactionsHandler;
import dev.vrba.botner.discord.reactions.verification.VerificationMessageReactionHandler;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.api.event.message.reaction.ReactionEvent;
import org.javacord.api.event.message.reaction.ReactionRemoveEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ReactionsHandler
{
    public final List<ReactionHandler> registeredHandlers = List.of(
            new VerificationMessageReactionHandler(),
            new EmojiCounterReactionsHandler()
    );

    public ReactionsHandler() throws SQLException { }

    public void handleReactionAdded(@NotNull ReactionAddEvent event)
    {
        this.registeredHandlers.forEach(handler -> handler.handleReactionAdded(event));
    }

    public void handleReactionRemoved(@NotNull ReactionRemoveEvent event)
    {
        this.registeredHandlers.forEach(handler -> handler.handleReactionRemoved(event));
    }
}
