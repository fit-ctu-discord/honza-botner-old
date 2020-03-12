package dev.vrba.botner.discord.reactions.emoji;

import dev.vrba.botner.discord.reactions.ReactionHandler;
import dev.vrba.botner.service.verification.EmojiCounter;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.emoji.CustomEmoji;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.entity.message.Reaction;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.api.event.message.reaction.ReactionRemoveEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmojiCounterReactionsHandler extends ReactionHandler {

    private EmojiCounter counter;

    @Override
    public void handleReactionAdded(@NotNull ReactionAddEvent event) {
        try {
            if (this.counter == null) {
                this.counter = new EmojiCounter(event.getApi());
            }

            Emoji emoji = event.getEmoji();

            if (emoji.isKnownCustomEmoji()) {
                @SuppressWarnings("OptionalGetWithoutIsPresent") // The isPresent() call is made internally in the if condition
                KnownCustomEmoji castedEmoji = emoji.asKnownCustomEmoji().get();

                System.out.println(castedEmoji);
                this.counter.increment(castedEmoji);
            }
        }
        catch (SQLException exception) {
            Logger.getGlobal().log(Level.SEVERE, exception.getMessage());
        }
    }

    @Override
    public void handleReactionRemoved(@NotNull ReactionRemoveEvent event) {
        try {
            if (this.counter == null) {
                this.counter = new EmojiCounter(event.getApi());
            }
            Emoji emoji = event.getEmoji();

            // This emoji is known to the bot -> it is from the given server
            if (emoji.isKnownCustomEmoji()) {
                @SuppressWarnings("OptionalGetWithoutIsPresent") // The isPresent() call is made internally in the if condition
                        KnownCustomEmoji castedEmoji = emoji.asKnownCustomEmoji().get();

                this.counter.decrement(castedEmoji);
            }
        }
        catch (SQLException exception) {
            Logger.getGlobal().log(Level.SEVERE, exception.getMessage());
        }
    }
}
