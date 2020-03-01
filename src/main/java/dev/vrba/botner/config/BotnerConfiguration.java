package dev.vrba.botner.config;

import dev.vrba.botner.discord.commands.RequiredCommandRole;

import java.util.List;

public class BotnerConfiguration
{
    private static BotnerConfiguration globalInstance;

    public VerificationMessageConfiguration verification;

    public long server;

    public static BotnerConfiguration getGlobalInstance()
    {
        return globalInstance;
    }

    public void setGlobalInstance(BotnerConfiguration instance)
    {
        globalInstance = instance;
    }

}
