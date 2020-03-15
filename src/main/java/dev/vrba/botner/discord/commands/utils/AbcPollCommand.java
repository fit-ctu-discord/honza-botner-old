package dev.vrba.botner.discord.commands.utils;

import com.vdurmont.emoji.EmojiParser;
import dev.vrba.botner.discord.commands.AuthenticatedCommand;
import dev.vrba.botner.exception.command.CommandException;
import dev.vrba.botner.exception.command.CommandExecutionException;
import dev.vrba.botner.exception.command.InvalidCommandUsageException;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AbcPollCommand extends AuthenticatedCommand {
    @Override
    public String getName() {
        return "abc";
    }

    @Override
    public String getUsage()
    {
        return "`." + this.getName() + " question option1 option2 ... option20`";
    }

    @Override
    public void execute(MessageCreateEvent event, Message message, String[] parameters) throws CommandException {
        if (parameters.length == 0) {
            throw new InvalidCommandUsageException();
        }

        Optional<TextChannel> _channel = event.getChannel().asTextChannel();

        if (_channel.isPresent()) {
            String tag = "<@" + message.getAuthor().getId() + ">";
            TextChannel channel = _channel.get();

            EmbedBuilder builder = new EmbedBuilder();

            List<String> optionsEmoji = Arrays.asList(
                "zero", "regional_indicator_b", "regional_indicator_symbol_letter_c", "regional_indicator_d",
                "regional_indicator_e", "regional_indicator_f", "regional_indicator_g", "regional_indicator_h",
                "regional_indicator_i", "regional_indicator_j", "regional_indicator_k", "regional_indicator_l",
                "regional_indicator_m", "regional_indicator_n", "regional_indicator_o", "regional_indicator_p",
                "regional_indicator_q", "regional_indicator_r", "regional_indicator_s", "regional_indicator_t"
            );

            // Match all parameters as "options".
            // If quotes around word/s are present, mark that as a parameter, otherwise each word is parameter.
            String pattern = "\"([^\"]+)\"|[\\S]+";
            Pattern regex = Pattern.compile(pattern);
            Matcher matcher = regex.matcher(String.join(" ", parameters));

            // Message without options
            if (!matcher.find()) {
                throw new InvalidCommandUsageException();
            }

            builder.setTitle(matcher.group(1) != null ? matcher.group(1) : matcher.group());

            int optionEmojiIndex = 0;
            try {
                while (matcher.find()) {
                    builder.addInlineField(EmojiParser.parseToUnicode(":" + optionsEmoji.get(optionEmojiIndex) + ":"), matcher.group(1) != null ? matcher.group(1) : matcher.group());
                    optionEmojiIndex++;
                }
            } catch (IndexOutOfBoundsException exception) {
                Logger.getGlobal().log(Level.SEVERE, exception.getMessage());
                throw new InvalidCommandUsageException();
            }

            builder.addField("Created by", tag);

            CompletableFuture<Message> sentMessage = channel.sendMessage("", builder);

            try {
                for (int i = 0; i < optionEmojiIndex; i++) {
                    sentMessage.get().addReaction(EmojiParser.parseToUnicode(":" + optionsEmoji.get(i) + ":"));
                }
            } catch (InterruptedException | ExecutionException exception) {
                Logger.getGlobal().log(Level.SEVERE, exception.getMessage());
                throw new CommandExecutionException();
            }
        }
    }
}
