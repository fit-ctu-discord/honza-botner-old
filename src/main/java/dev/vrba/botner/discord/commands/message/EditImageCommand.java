package dev.vrba.botner.discord.commands.message;

import dev.vrba.botner.exception.command.CommandException;
import dev.vrba.botner.exception.command.CommandExecutionException;
import dev.vrba.botner.exception.command.InvalidCommandUsageException;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.Arrays;
import java.util.Optional;

public class EditImageCommand extends MessageCommand
{
    @Override
    public String getName()
    {
        return "editImage";
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
            String imageUrl = parameters[1];
            String text = String.join(" ", Arrays.copyOfRange(parameters, 2, parameters.length));

            if (message.get().getAuthor().getId() == event.getApi().getClientId())
            {
                message.get().edit(text, new EmbedBuilder().setImage(imageUrl));
                return;
            }
        }

        throw new CommandExecutionException();
    }

    @Override
    public String getUsage()
    {
        return "`." + this.getName() + " <message_link> <new_image_url> <new_content>`";
    }

}
