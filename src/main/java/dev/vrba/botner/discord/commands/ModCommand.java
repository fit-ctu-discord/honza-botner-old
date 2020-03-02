package dev.vrba.botner.discord.commands;

import dev.vrba.botner.config.BotnerConfiguration;
import org.javacord.api.entity.message.Message;

import java.util.Optional;

public abstract class ModCommand extends Command
{
    @Override
    public RequiredCommandRole[] getRequiredRoles()
    {
        BotnerConfiguration configuration = BotnerConfiguration.getGlobalInstance();
        Optional<RequiredCommandRole> role = configuration.roles.stream().filter(_role -> _role.name.equals("MOD")).findFirst();

        if (role.isEmpty())
        {
            throw new RuntimeException("Cannot find MOD role, shutting down. Better safe than sorry.");
        }

        return new RequiredCommandRole[] {
                role.get()
        };
    }
}
