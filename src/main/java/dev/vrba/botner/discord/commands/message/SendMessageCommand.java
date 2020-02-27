package dev.vrba.botner.discord.commands.message;

import dev.vrba.botner.discord.commands.ModCommand;
import dev.vrba.botner.exception.command.CommandException;
import dev.vrba.botner.exception.command.InvalidCommandUsageException;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.Arrays;
import java.util.List;

public class SendMessageCommand extends ModCommand
{

    @Override
    public String getName()
    {
        return "send";
    }

    @Override
    public void execute(MessageCreateEvent event, Message message, String[] parameters) throws CommandException
    {
        List<ServerTextChannel> channels = event.getMessage().getMentionedChannels();

        if (parameters.length < 2 || channels.isEmpty())
        {
            throw new InvalidCommandUsageException();
        }

        ServerTextChannel channel = channels.get(0);
        String content = String.join(" ", Arrays.copyOfRange(parameters, 1, parameters.length));

        channel.sendMessage(content);
    }

    @Override
    public String getUsage()
    {
        return "`." + this.getName() + " <channel_mention> <message>`";
    }
}
