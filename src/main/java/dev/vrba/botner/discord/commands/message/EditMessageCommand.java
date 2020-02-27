package dev.vrba.botner.discord.commands.message;

import dev.vrba.botner.discord.commands.ModCommand;
import dev.vrba.botner.exception.command.CommandException;
import dev.vrba.botner.exception.command.CommandExecutionException;
import dev.vrba.botner.exception.command.InvalidCommandUsageException;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditMessageCommand extends MessageCommand
{
    @Override
    public String getName()
    {
        return "edit";
    }

    @Override
    public void execute(MessageCreateEvent event, Message _message, String[] parameters) throws CommandException
    {
        if (parameters.length < 2)
        {
            throw new InvalidCommandUsageException();
        }

        Optional<Message> message = this.findMessageFromLink(event, parameters[0]);

        if (message.isPresent())
        {
            String content = String.join(" ", Arrays.copyOfRange(parameters, 1, parameters.length));

            if (message.get().getAuthor().getId() == event.getApi().getClientId())
            {
                message.get().edit(content);
                return;
            }
            // The message was not sent by used
            throw new CommandExecutionException();
        }
        else
        {
            throw new CommandExecutionException();
        }
    }

    @Override
    public String getUsage()
    {
        return "`." + this.getName() + " <message_link> <new_content>`";
    }

}
