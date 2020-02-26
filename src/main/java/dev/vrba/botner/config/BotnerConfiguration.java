package dev.vrba.botner.config;

import dev.vrba.botner.discord.commands.RequiredCommandRole;

import java.util.List;

public class BotnerConfiguration
{
    private static BotnerConfiguration globalInstance;

    public VerificationMessageConfiguration verificationMessage;

    public List<RoleAssigningMessage> roleAssigningMessages;

    public List<RequiredCommandRole> mappedPermissionRoles;

    public void setGlobalInstance(BotnerConfiguration instance)
    {
        globalInstance = instance;
    }

    public static BotnerConfiguration getGlobalInstance()
    {
        return globalInstance;
    }
}
