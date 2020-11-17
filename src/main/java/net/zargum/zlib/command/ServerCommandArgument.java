package net.zargum.zlib.command;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class ServerCommandArgument<T extends JavaPlugin> {

    public final T plugin;

    private final ServerCommand<?> serverCommand;
    private final String argumentName, description, usageFormat;
    private final String[] aliases;
    private final String permission;
    private final String usageMessage;
    private final List<ServerCommandArgument<T>> arguments = new ArrayList<>();
    private final boolean consoleOnly;
    private final boolean playerOnly;
    private final boolean requiredPermission = true;
    private int minArgRequired = 0;

    public ServerCommandArgument(ServerCommand<?> serverCommand, String argumentName, String description, String usageFormat, String... aliases) {
        plugin = (T) serverCommand.getPlugin();

        this.serverCommand = serverCommand;
        this.argumentName = argumentName.toLowerCase();
        this.description = description;
        this.usageFormat = usageFormat == null ? "" : usageFormat;
        this.usageMessage = ChatColor.RED + "Use: /" + serverCommand.getName() + " " + argumentName + " " + usageFormat;
        this.aliases = aliases;
        this.permission = "command." + serverCommand.getName() + "." + argumentName;

        // Inherit variables (by default)
        this.consoleOnly = serverCommand.isConsoleOnly();
        this.playerOnly = serverCommand.isPlayerOnly();

        if (!this.usageFormat.isEmpty()) {
            String[] args = usageFormat.split(" ");
            for (String arg : args) if (arg.charAt(0) == '<') minArgRequired++;
        }
    }

    public ServerCommandArgument(ServerCommand<T> serverCommand, String argumentName, String description, String usageFormat) {
        this(serverCommand, argumentName, description, usageFormat, (String) null);
    }

    public ServerCommandArgument(ServerCommand<T> serverCommand, String argumentName, String description) {
        this(serverCommand, argumentName, description, null, (String) null);
    }

    public abstract void execute(String argumentName, CommandSender sender, String[] args);

}
