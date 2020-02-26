package dev.vrba.botner.discord;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiParser;
import dev.vrba.botner.discord.commands.Command;
import dev.vrba.botner.discord.commands.RequiredCommandRole;
import dev.vrba.botner.discord.commands.message.SendMessageCommand;
import dev.vrba.botner.exception.command.CommandException;
import dev.vrba.botner.exception.command.CommandExecutionException;
import dev.vrba.botner.exception.command.InvalidCommandUsageException;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.event.message.MessageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MessageDispatcher
{
    private final String prefix = ".";

    private List<Command> registeredCommands = List.of(
            new SendMessageCommand()
    );

    public void handleMessage(@NotNull MessageCreateEvent event)
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
            if (!command.isPresent())
            {
                event.getMessage().addReaction(EmojiParser.parseToUnicode(":man_shrug:"));
                return;
            }

            if (!this.isUserEligibleToRunCommand(event, command.get()))
            {
                this.reply(event.getMessage(), "Pro tento příkaz nemáš dostatečná oprávnění.");
                this.react(event.getMessage(), ":splayed_hand:");
                return;
            }

            try
            {
                command.get().execute(event, event.getMessage(), parameters);
            }
            catch (InvalidCommandUsageException exception)
            {
                this.reply(event.getMessage(), "Špatné použití příkazu `" + name + "`.\n" + exception.getMessage());
                this.react(event.getMessage(), ":warning:");
                return;
            }
            catch (CommandException exception)
            {

                this.reply(event.getMessage(), "Při vykonávání příkazu `" + name + "` nastala chyba. \n" + exception.getMessage());
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

        if (!user.isPresent() || !server.isPresent())
        {
            return false;
        }

        List<Role> roles = user.get().getRoles(server.get());
        RequiredCommandRole[] requiredRoles = command.getRequiredRoles();

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
        message.getChannel().sendMessage(tag + " ," + content);
    }

    private Optional<Command> getCommandByName(@NotNull String name)
    {

        return this.registeredCommands.stream()
                .filter(command -> (this.prefix + command.getName()).equals(name))
                .findFirst();
    }

}
