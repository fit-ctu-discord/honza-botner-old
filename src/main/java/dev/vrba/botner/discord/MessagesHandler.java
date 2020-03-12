package dev.vrba.botner.discord;

import dev.vrba.botner.discord.messages.MessageHandler;
import dev.vrba.botner.discord.messages.command.MessageDispatcher;
import dev.vrba.botner.discord.messages.emoji.EmojiCounterMessageHandler;
import org.javacord.api.event.message.MessageCreateEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessagesHandler {
    private List<MessageHandler> handlers = List.of(
            new MessageDispatcher(),
            new EmojiCounterMessageHandler()
    );

    public void handleMessageCreated(@NotNull MessageCreateEvent event) {
        for (MessageHandler handler : this.handlers) {
            handler.handleMessageCreated(event);
        }
    }
}
