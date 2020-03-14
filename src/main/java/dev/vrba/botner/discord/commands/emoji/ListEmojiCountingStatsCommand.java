package dev.vrba.botner.discord.commands.emoji;

import dev.vrba.botner.database.entities.CountedEmoji;
import dev.vrba.botner.discord.commands.ModCommand;
import dev.vrba.botner.exception.command.CommandException;
import dev.vrba.botner.exception.command.CommandExecutionException;
import dev.vrba.botner.service.verification.EmojiCounter;
import org.apache.commons.lang3.StringUtils;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageCreateEvent;

import java.sql.SQLException;
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

            boolean total = false;
            List<CountedEmoji> results = this.counter.all();
            Comparator<CountedEmoji> comparator = Comparator.comparing(CountedEmoji::getUsagePerDay);

            if (Arrays.asList(parameters).contains("total")) {
                total = true;
                comparator = Comparator.comparing(CountedEmoji::getTotalUsage);
            }

            if (Arrays.asList(parameters).contains("reverse")) {
                results.sort(comparator);
            } else {
                results.sort(Collections.reverseOrder(comparator));
            }

            Optional<TextChannel> channel = event.getChannel().asTextChannel();

            if (channel.isEmpty()) {
                throw new CommandExecutionException();
            }

            channel.get().sendMessage("**Statistika používání custom emotes**");

            StringBuilder builder = new StringBuilder();
            builder.append("\n");

            int emojisAppended = 0;
            final int chunkSize = 30;

            for (CountedEmoji result : results) {

                Optional<KnownCustomEmoji> _wrapper = event.getApi().getCustomEmojiById(result.id);

                if (_wrapper.isPresent()) {
                    KnownCustomEmoji emoji = _wrapper.get();

                    String label = total ? "×" : "×/day";

                    builder.append(emoji.getMentionTag())
                            .append("`")
                            .append(StringUtils.leftPad(String.valueOf(total ? result.getTotalUsage() : String.format("%.2f", result.getUsagePerDay())), 10, " "))
                            .append(label)
                            .append("`")
                            .append(emojisAppended % 3 == 2 ? "\n" : "\t");

                    emojisAppended++;
                }

                if (emojisAppended == chunkSize) {
                    channel.get().sendMessage(builder.toString());
                    builder.setLength(0);
                    builder.append("\n");
                    emojisAppended = 0;
                }
            }

            channel.get().sendMessage(builder.toString());
            builder.setLength(0);

        } catch (SQLException exception) {
            Logger.getGlobal().log(Level.SEVERE, exception.getMessage());
            throw new CommandExecutionException();
        }
    }
}

