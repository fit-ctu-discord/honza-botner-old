package dev.vrba.botner.discord;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.api.event.message.reaction.ReactionEvent;
import org.javacord.api.event.message.reaction.ReactionRemoveEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ReactionsHandler
{
    public void handleReactionAdded(@NotNull ReactionAddEvent event)
    {
        if (this.shouldIgnore(event)) return;
    }

    public void handleReactionRemoved(@NotNull ReactionRemoveEvent event)
    {
        if (this.shouldIgnore(event)) return;
    }

    /**
     * The bot should only react to reactions on his own messages
     *
     * @param event Event emitted from the Discord's websocket connection
     * @return Whether the bot should ignore the given event
     */
    private boolean shouldIgnore(@NotNull ReactionEvent event)
    {
        Optional<Message> message = event.getMessage();
        DiscordApi client = event.getApi();

        // Only react if bot is author of the message, that the reaction targets
        return message.filter(value -> (value.getAuthor().getId() != client.getClientId())).isPresent();
    }

}
