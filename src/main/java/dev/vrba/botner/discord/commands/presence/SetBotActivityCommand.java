package dev.vrba.botner.discord.commands.presence;

import dev.vrba.botner.discord.commands.ModCommand;
import dev.vrba.botner.exception.command.CommandException;
import dev.vrba.botner.exception.command.InvalidCommandUsageException;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.Arrays;

public class SetBotActivityCommand extends ModCommand {
    @Override
    public String getName() {
        return "activity";
    }

    @Override
    public void execute(MessageCreateEvent event, Message message, String[] parameters) throws CommandException {
        if (parameters.length < 2)
        {
            throw new InvalidCommandUsageException();
        }

        ActivityType type;
        String name = String.join(" ", Arrays.copyOfRange(parameters, 1, parameters.length));

        switch(parameters[0])
        {
            case "play":
                type = ActivityType.PLAYING;
                break;
            case "watch":
                type = ActivityType.WATCHING;
                break;
            case "listen":
                type = ActivityType.LISTENING;
                break;
            default:
                throw new InvalidCommandUsageException();
        }

        event.getApi().updateActivity(type, name);
    }

    @Override
    public String getUsage() {
        return "`.activity {play, watch, listen} [name]`";
    }
}
