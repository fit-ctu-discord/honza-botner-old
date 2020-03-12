package dev.vrba.botner.discord.messages.emoji;

import dev.vrba.botner.discord.messages.MessageHandler;
import dev.vrba.botner.service.verification.EmojiCounter;
import org.javacord.api.entity.emoji.CustomEmoji;
import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.event.message.MessageCreateEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmojiCounterMessageHandler extends MessageHandler {

    private EmojiCounter counter;

    @Override
    public void handleMessageCreated(@NotNull MessageCreateEvent event) {

        // Do not count emojis posted by bots
        if (event.getMessageAuthor().isBotUser()) return;

        try {
            if (this.counter == null) {
                this.counter = new EmojiCounter(event.getApi());
            }

            List<CustomEmoji> _emojis = event.getMessage().getCustomEmojis();

            // Remove all duplicated emojis
            List<CustomEmoji> emojis = new ArrayList<>(new HashSet<>(_emojis));

            for (CustomEmoji emoji : emojis) {
                Optional<KnownCustomEmoji> castedEmoji = emoji.asKnownCustomEmoji();

                if (castedEmoji.isPresent())
                {
                    this.counter.increment(castedEmoji.get());
                }
            }

        } catch (SQLException exception) {
            Logger.getGlobal().log(Level.SEVERE, exception.getMessage());
        }
    }
}
