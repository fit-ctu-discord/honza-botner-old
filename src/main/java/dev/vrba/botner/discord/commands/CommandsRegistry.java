package dev.vrba.botner.discord.commands;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class CommandsRegistry
{
    private List<Command> registeredCommands = List.of();

    public Optional<Command> getCommandByName(@NotNull String name)
    {
        return this.registeredCommands.stream()
                .filter(command -> command.getName().equals(name))
                .findFirst();
    }
}
