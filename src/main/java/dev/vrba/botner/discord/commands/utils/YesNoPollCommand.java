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

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class YesNoPollCommand extends AuthenticatedCommand {
    @Override
    public String getName() {
        return "yesno";
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
            builder.addField("Created by", tag);

            CompletableFuture<Message> sentMessage = channel.sendMessage("", builder);

            try {
                sentMessage.get().addReaction(EmojiParser.parseToUnicode(":thumbsup:"));
                sentMessage.get().addReaction(EmojiParser.parseToUnicode(":thumbsdown:"));
            }
            catch (InterruptedException | ExecutionException exception) {
                Logger.getGlobal().log(Level.SEVERE, exception.getMessage());
                throw new CommandExecutionException();
            }
        }
    }
}
