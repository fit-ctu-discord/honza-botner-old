package dev.vrba.botner.discord.commands.emoji;

import dev.vrba.botner.database.entities.CountedEmoji;
import dev.vrba.botner.discord.commands.ModCommand;
import dev.vrba.botner.exception.command.CommandException;
import dev.vrba.botner.service.verification.EmojiCounter;
import org.apache.commons.lang3.StringUtils;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListEmojiCountingStatsCommand extends ModCommand {

    private EmojiCounter counter;

    @Override
    public String getName() {
        return "emotes";
    }

    @Override
    public void execute(MessageCreateEvent event, Message message, String[] parameters) throws CommandException {
        try {
            if (this.counter == null) {
                this.counter = new EmojiCounter(event.getApi());
            }

            List<CountedEmoji> results = this.counter.all();
            Comparator<CountedEmoji> sortByUsage = new Comparator<CountedEmoji>() {
                @Override
                public int compare(CountedEmoji first, CountedEmoji second) {
                    return (int) (first.times - second.times);
                }
            };

            if (parameters.length > 0 && parameters[0].equals("reverse")) {
                results.sort(sortByUsage);
            } else {
                results.sort(Collections.reverseOrder(sortByUsage));
            }

            StringBuilder builder = new StringBuilder();

            builder.append("**Statistika používání custom emotes**\n");

            for (CountedEmoji result : results) {

                Optional<KnownCustomEmoji> _wrapper = event.getApi().getCustomEmojiById(result.id);

                _wrapper.ifPresent(knownCustomEmoji -> {
                    long daysInUsage = ChronoUnit.DAYS.between(new Date().toInstant(), result.firstUsedAt.toInstant()) + 1;
                    double coefficient = (double) result.times / daysInUsage;

                    builder.append(knownCustomEmoji.getMentionTag())
                            .append("\t`")
                            .append(StringUtils.leftPad(String.valueOf(result.times), 6, " "))
                            .append("× ~")
                            .append(StringUtils.leftPad(String.valueOf(Math.round(coefficient)), 6, " "))
                            .append("×/day`\n");
                });
            }

            Optional<TextChannel> channel = event.getChannel().asTextChannel();
            channel.ifPresent(textChannel -> textChannel.sendMessage(builder.toString()));
        } catch (SQLException exception) {
            Logger.getGlobal().log(Level.SEVERE, exception.getMessage());

            Optional<TextChannel> channel = event.getChannel().asTextChannel();
            channel.ifPresent(textChannel -> textChannel.sendMessage("Při získávání statistiky došlo k chybě"));
        }
    }
}

