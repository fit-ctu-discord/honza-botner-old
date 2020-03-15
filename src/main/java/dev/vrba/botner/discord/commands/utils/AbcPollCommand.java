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
    public void execute(MessageCreateEvent event, Message message, String[] parameters) throws CommandException {
        if (parameters.length == 0)
        {
            throw new InvalidCommandUsageException();
        }

        Optional<TextChannel> _channel = event.getChannel().asTextChannel();

        if (_channel.isPresent())
        {
            String tag = "<@" + message.getAuthor().getId() + ">";
            TextChannel channel = _channel.get();

            EmbedBuilder builder = new EmbedBuilder();

            builder.setTitle(String.join(" ", parameters));

            List<String> optionsEmoji = Arrays.asList("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine");

            // Match all parameters as "options".
            // If quotes around word/s are present, mark that as a parameter, otherwise each word is parameter.
            String pattern = "\"[^\"]+\"|[\\S]+";
            Pattern regex = Pattern.compile(pattern);
            Matcher matcher = regex.matcher(String.join(" ", parameters));

            // Message without options
            if (!matcher.find())
            {
                throw new InvalidCommandUsageException();
            }

            matcher.reset();

            try {
                int optionEmojiIndex = 0;
                while (matcher.find()) {
                    builder.addField(EmojiParser.parseToUnicode(":" + optionsEmoji.get(optionEmojiIndex) + ":"), matcher.group());
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
                    sentMessage.get().addReaction(EmojiParser.parseToUnicode(":" + optionsEmoji.get(optionEmojiIndex) + ":"));
                }
            }
            catch (InterruptedException | ExecutionException exception) {
                Logger.getGlobal().log(Level.SEVERE, exception.getMessage());
                throw new CommandExecutionException();
            }
        }
    }
}
