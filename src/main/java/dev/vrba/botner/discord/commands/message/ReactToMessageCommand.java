package dev.vrba.botner.discord.commands.message;

import com.vdurmont.emoji.EmojiParser;
import dev.vrba.botner.exception.command.CommandException;
import dev.vrba.botner.exception.command.CommandExecutionException;
import dev.vrba.botner.exception.command.InvalidCommandUsageException;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.Optional;

public class ReactToMessageCommand extends MessageCommand
{
    @Override
    public String getName()
    {
        return "react";
    }

    @Override
    public void execute(MessageCreateEvent event, Message _message, String[] parameters) throws CommandException
    {
        if (parameters.length != 2)
        {
            throw new InvalidCommandUsageException();
        }

        Optional<Message> message = this.findMessageFromLink(event, parameters[0]);

        if (message.isPresent())
        {
            String emoji = EmojiParser.parseToUnicode(parameters[1]);
            message.get().addReaction(emoji);
        }
        else
        {
            throw new CommandExecutionException();
        }
    }

    @Override
    public String getUsage()
    {
        return "`" + this.getName() + " <message_link> <emote>`";
    }
}
