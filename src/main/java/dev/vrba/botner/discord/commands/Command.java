package dev.vrba.botner.discord.commands;

import org.javacord.api.entity.message.Message;

public abstract class Command
{
    public abstract String getName();

    public abstract RequiredCommandRole[] getRequiredRoles();

    /**
     *
     * @param message
     * @return whether the command has failed
     */
    public abstract boolean execute(Message message);
}
