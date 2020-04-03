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
    public String getUsage() {
        return "`." + this.getName() + " question`";
    }

    @Override
    public void execute(MessageCreateEvent event, Message message, String[] parameters) throws CommandException {

        if (parameters.length == 0) {
            throw new InvalidCommandUsageException();
        }

        Optional<TextChannel> _channel = event.getChannel().asTextChannel();

        if (_channel.isPresent() && message.getAuthor().asUser().isPresent()) {
            TextChannel channel = _channel.get();
            User author = message.getAuthor();

            EmbedBuilder builder = new EmbedBuilder();

            builder.setAuthor(author);
            builder.setTitle(String.join(" ", parameters));

            // If the post contains an image, embed it.
            if (!message.getAttachments().isEmpty())
            {
                builder.setImage(message.getAttachments().get(0).getUrl().toString());
            }

            CompletableFuture<Message> sentMessage = channel.sendMessage("", builder);

            try {
                sentMessage.get().addReactions(
                    EmojiParser.parseToUnicode(":thumbsup:"),
                    EmojiParser.parseToUnicode(":thumbsdown:")
                );

                message.delete();
            } catch (InterruptedException | ExecutionException exception) {
                Logger.getGlobal().log(Level.SEVERE, exception.getMessage());
                throw new CommandExecutionException();
            }
        }
    }
}
