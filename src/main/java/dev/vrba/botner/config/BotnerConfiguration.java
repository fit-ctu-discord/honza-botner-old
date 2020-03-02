package dev.vrba.botner.config;

import dev.vrba.botner.discord.commands.RequiredCommandRole;

import java.util.List;

public class BotnerConfiguration
{
    // Configuration of a verification message
    public VerificationMessageConfiguration verification;

    // Server id
    public long server;

    // Mapped roles
    public List<RequiredCommandRole> roles;

    private static BotnerConfiguration globalInstance;
    public static BotnerConfiguration getGlobalInstance()
    {
        return globalInstance;
    }
    public void setGlobalInstance(BotnerConfiguration instance)
    {
        globalInstance = instance;
    }
}
