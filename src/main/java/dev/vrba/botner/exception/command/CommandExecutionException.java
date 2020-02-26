package dev.vrba.botner.exception.command;

import dev.vrba.botner.discord.commands.Command;

public class CommandExecutionException extends CommandException
{
    public CommandExecutionException(String message)
    {
        super(message);
    }
}
