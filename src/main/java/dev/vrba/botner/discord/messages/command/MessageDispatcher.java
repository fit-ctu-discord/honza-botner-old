package dev.vrba.botner.discord.messages.command;

import com.vdurmont.emoji.EmojiParser;
import dev.vrba.botner.discord.commands.Command;
import dev.vrba.botner.discord.commands.RequiredCommandRole;
import dev.vrba.botner.discord.commands.emoji.ListEmojiCountingStatsCommand;
import dev.vrba.botner.discord.commands.message.EditMessageCommand;
import dev.vrba.botner.discord.commands.message.ReactToMessageCommand;
import dev.vrba.botner.discord.commands.message.SendImageCommand;
import dev.vrba.botner.discord.commands.message.SendMessageCommand;
import dev.vrba.botner.discord.commands.presence.SetBotActivityCommand;
import dev.vrba.botner.discord.commands.utils.CreateSimplePollCommand;
import dev.vrba.botner.discord.messages.MessageHandler;
import dev.vrba.botner.exception.command.CommandException;
import dev.vrba.botner.exception.command.InvalidCommandUsageException;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageDispatcher extends MessageHandler
{
    private final String prefix = ".";

    private List<Command> registeredCommands = List.of(
            new SendMessageCommand(),
            new SendImageCommand(),
            new EditMessageCommand(),
            new ReactToMessageCommand(),
            new SetBotActivityCommand(),
            new ListEmojiCountingStatsCommand(),
            new CreateSimplePollCommand()
    );

    public void handleMessageCreated(@NotNull MessageCreateEvent event)
    {
        if (this.shouldIgnore(event))
        {
            return;
        }
        {
            String content = event.getMessageContent();
            String[] parts = content.split(" ");

            String name = parts[0];
            String[] parameters = Arrays.copyOfRange(parts, 1, parts.length);

            Optional<Command> command = this.getCommandByName(name);

            // Unknown command
            if (command.isEmpty())
            {
                event.getMessage().addReaction(EmojiParser.parseToUnicode(":man_shrug:"));
                return;
            }

            if (!this.isUserEligibleToRunCommand(event, command.get()))
            {
                this.reply(event.getMessage(), "Pro tento příkaz nemáš dostatečná oprávnění.");
                this.react(event.getMessage(), ":skull_crossbones:");
                return;
            }

            try
            {
                command.get().execute(event, event.getMessage(), parameters);
            }
            catch (InvalidCommandUsageException exception)
            {
                this.reply(event.getMessage(), "Špatné použití příkazu `" + name + "`.\n" + command.get().getUsage());
                this.react(event.getMessage(), ":warning:");
                return;
            }
            catch (CommandException exception)
            {
                Logger.getGlobal().log(Level.SEVERE, exception.getMessage());

                this.reply(event.getMessage(), "Při vykonávání příkazu `" + name + "` nastala chyba.");
                this.react(event.getMessage(), ":x:");

                return;
            }

            this.react(event.getMessage(), ":white_check_mark:");
        }
    }

    private boolean isUserEligibleToRunCommand(MessageCreateEvent event, Command command)
    {
        Optional<User> user = event.getMessageAuthor().asUser();
        Optional<Server> server = event.getServer();

        if (user.isEmpty() || server.isEmpty())
        {
            return false;
        }

        List<Role> roles = user.get().getRoles(server.get());

        RequiredCommandRole[] requiredRoles;

        try
        {
           requiredRoles = command.getRequiredRoles();
        }
        catch (RuntimeException exception)
        {
            return false;
        }

        for (RequiredCommandRole requiredRole : requiredRoles)
        {
            if (roles.stream().noneMatch(role -> role.getId() == requiredRole.id))
            {
                return false;
            }
        }

        return true;
    }

    private boolean shouldIgnore(MessageCreateEvent event)
    {
        return !event.getMessageContent().startsWith(this.prefix);
    }

    private void react(Message message, String emoji)
    {
        message.addReaction(EmojiParser.parseToUnicode(emoji));
    }

    private void reply(Message message, String content)
    {
        String tag = "<@" + message.getAuthor().getId() + ">";
        message.getChannel().sendMessage(tag + "\n" + content);
    }

    private Optional<Command> getCommandByName(@NotNull String name)
    {

        return this.registeredCommands.stream()
                .filter(command -> (this.prefix + command.getName()).equals(name))
                .findFirst();
    }

}
