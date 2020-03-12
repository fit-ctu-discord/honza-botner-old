package dev.vrba.botner.discord.commands;

import dev.vrba.botner.config.BotnerConfiguration;

import java.util.Optional;

public abstract class AuthenticatedCommand extends Command {
    @Override
    public RequiredCommandRole[] getRequiredRoles() {
        BotnerConfiguration configuration = BotnerConfiguration.getGlobalInstance();
        Optional<RequiredCommandRole> role = configuration.roles.stream().filter(_role -> _role.name.equals("AUTHENTICATED")).findFirst();

        if (role.isEmpty()) {
            throw new RuntimeException("Cannot find AUTHENTICATED role. Better safe than sorry.");
        }

        return new RequiredCommandRole[]{role.get()};
    }
}
