package dev.vrba.botner.discord.commands;

import org.javacord.api.entity.message.Message;

public abstract class ModCommand extends Command
{
    @Override
    public RequiredCommandRole[] getRequiredRoles()
    {
        return new RequiredCommandRole[] { };
    }
}
