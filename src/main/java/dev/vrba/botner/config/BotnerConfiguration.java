package dev.vrba.botner.config;

import dev.vrba.botner.discord.commands.RequiredCommandRole;

import java.util.HashMap;
import java.util.List;

public class BotnerConfiguration
{
    public VerificationMessage verificationMessage;

    public List<RoleAssigningMessage> roleAssigningMessages;

    public HashMap<RequiredCommandRole, Long> mappedPermissionRoles;
}
