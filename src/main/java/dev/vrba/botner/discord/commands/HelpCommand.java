package dev.vrba.botner.discord.commands;

import dev.vrba.botner.exception.command.CommandException;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.List;

public class HelpCommand extends AuthenticatedCommand {
    private final List<Command> registeredCommands;

    public HelpCommand(List<Command> registeredCommands) {
        this.registeredCommands = registeredCommands;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void execute(MessageCreateEvent event, Message message, String[] parameters) throws CommandException {
        for (Command command : this.registeredCommands) {
            EmbedBuilder info = new EmbedBuilder();

            // Do not include mod commands to the listing
            if (command instanceof ModCommand) {
                continue;
            }

            info.setDescription(command.getHelp());
            info.setTitle("Command ." + command.getName());
            info.addField("Usage", command.getUsage());

            if (command instanceof AuthenticatedCommand) {
                info.addField("Required roles", "@Authenticated");
            }

            message.getChannel().sendMessage(info);
        }
    }

    @Override
    public String getHelp() {
        return "View this list of commands with their respective help messages";
    }

    @Override
    public String getUsage() {
        return "`.help`";
    }
}
