package dev.vrba.botner.discord.commands;

import dev.vrba.botner.exception.command.CommandException;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageCreateEvent;

public abstract class Command
{
    public abstract String getName();

    public abstract RequiredCommandRole[] getRequiredRoles();

    public abstract void execute(MessageCreateEvent event, Message message, String[] parameters) throws CommandException;

    public String getUsage()
    {
        return "No usage provided by the command `" + this.getName() + "`";
    }
}
