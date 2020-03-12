package dev.vrba.botner.discord.messages;

import org.javacord.api.event.message.MessageCreateEvent;
import org.jetbrains.annotations.NotNull;

public abstract class MessageHandler {
    abstract public void handleMessageCreated(@NotNull MessageCreateEvent event);
}
