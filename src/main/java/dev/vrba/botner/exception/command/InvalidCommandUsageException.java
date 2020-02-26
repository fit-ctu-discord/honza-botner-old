package dev.vrba.botner.exception.command;

public class InvalidCommandUsageException extends CommandException
{
    public InvalidCommandUsageException(String message)
    {
        super(message);
    }
}
