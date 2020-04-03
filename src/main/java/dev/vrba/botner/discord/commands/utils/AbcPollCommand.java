package dev.vrba.botner.discord.commands.utils;

import com.vdurmont.emoji.EmojiParser;
import dev.vrba.botner.discord.commands.AuthenticatedCommand;
import dev.vrba.botner.exception.command.CommandException;
import dev.vrba.botner.exception.command.CommandExecutionException;
import dev.vrba.botner.exception.command.InvalidCommandUsageException;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.message.MessageAuthor;
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
    public String getUsage() {
        return "`." + this.getName() + " question option1 option2 ... option20`";
    }

    @Override
    public void execute(MessageCreateEvent event, Message message, String[] parameters) throws CommandException {
        if (parameters.length == 0) {
            throw new InvalidCommandUsageException();
        }

        Optional<TextChannel> _channel = event.getChannel().asTextChannel();

        if (_channel.isPresent() && message.getAuthor().asUser().isPresent()) {
            TextChannel channel = _channel.get();
            MessageAuthor author = message.getAuthor();

            EmbedBuilder builder = new EmbedBuilder();

            List<String> optionsEmoji = Arrays.asList(
                    "regional_indicator_symbol_a", "regional_indicator_symbol_b", "regional_indicator_symbol_c",
                    "regional_indicator_symbol_d", "regional_indicator_symbol_e", "regional_indicator_symbol_f",
                    "regional_indicator_symbol_g", "regional_indicator_symbol_h", "regional_indicator_symbol_i",
                    "regional_indicator_symbol_j", "regional_indicator_symbol_k", "regional_indicator_symbol_l",
                    "regional_indicator_symbol_m", "regional_indicator_symbol_n", "regional_indicator_symbol_o",
                    "regional_indicator_symbol_p", "regional_indicator_symbol_q", "regional_indicator_symbol_r",
                    "regional_indicator_symbol_s", "regional_indicator_symbol_t"
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

            builder.setAuthor(author);
            builder.setTitle(matcher.group(1) != null ? matcher.group(1) : matcher.group());

            // If the post contains an image, embed it.
            if (!message.getAttachments().isEmpty())
            {
                builder.setImage(message.getAttachments().get(0).getUrl().toString());
            }

            int optionEmojiIndex = 0;
            try {
                while (matcher.find()) {
                    builder.addInlineField(
                            EmojiParser.parseToUnicode(":" + optionsEmoji.get(optionEmojiIndex) + ":"),
                            matcher.group(1) != null ? matcher.group(1) : matcher.group()
                    );
                    optionEmojiIndex++;
                }
            } catch (IndexOutOfBoundsException exception) {
                Logger.getGlobal().log(Level.SEVERE, exception.getMessage());
                throw new InvalidCommandUsageException();
            }

            CompletableFuture<Message> sentMessage = channel.sendMessage("", builder);

            try {
                for (int i = 0; i < optionEmojiIndex; i++) {
                    sentMessage.get().addReaction(EmojiParser.parseToUnicode(":" + optionsEmoji.get(i) + ":"));
                }

                message.delete();
            } catch (InterruptedException | ExecutionException exception) {
                Logger.getGlobal().log(Level.SEVERE, exception.getMessage());
                throw new CommandExecutionException();
            }
        }
    }
}
