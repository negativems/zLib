package net.zargum.zlib.command;

import lombok.Getter;
import lombok.Setter;
import net.zargum.zlib.messages.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public abstract class ServerCommandArgument<T extends JavaPlugin> {

    public final T plugin;

    private final ServerCommand<?> command;
    private final String argumentName, description, usageFormat,permission, usage, defaultUsageMessage;
    private final String[] aliases;
    private final List<ServerCommandArgument<T>> arguments = new ArrayList<>();
    @Setter private boolean requiresPermission, consoleOnly, playerOnly, async;
    @Setter private int minArgRequired = 0;

    public ServerCommandArgument(ServerCommand<?> serverCommand, String argumentName, String description, String usageFormat, String... aliases) {
        plugin = (T) serverCommand.getPlugin();

        this.command = serverCommand;
        this.argumentName = argumentName.toLowerCase();
        this.description = description;
        this.usageFormat = usageFormat == null ? "" : usageFormat;
        this.defaultUsageMessage = Messages.USAGE_COMMAND_ARGUMENT.toString(serverCommand.getName(), argumentName, this.usageFormat);
        this.usage = defaultUsageMessage;
        this.aliases = aliases;
        this.permission = "command." + serverCommand.getName() + "." + argumentName;
        this.async = false;

        // Inherit variables (by default)
        this.requiresPermission = serverCommand.isRequiresPermission();
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

    public abstract void execute(CommandSender sender, String argumentName, String[] args);

    public void addToTablistCompletion(int pos, String arg) {
        pos++;
        if (command.getTabCompletionList().containsKey(pos)) {
            command.getTabCompletionList().get(pos).add(arg);
        } else {
            List<String> arguments = new ArrayList<>();
            arguments.add(arg);
            command.getTabCompletionList().put(pos, arguments);
        }
    }

    public void addToTablistCompletion(int pos, String... args) {
        pos++;
        command.getTabCompletionList().put(pos, Arrays.asList(args));
    }

    public void removeFromTablistCompletion(int pos, String arg) {
        pos++;
        if (command.getTabCompletionList().containsKey(pos)) return;
        int i = 0;
        for (String tabArg : command.getTabCompletionList().get(pos)) {
            if (tabArg.equals(arg)) command.getTabCompletionList().get(pos).remove(i);
            i++;
        }
    }

    public void removeFromTablistCompletion(int pos, String... args) {
        pos++;
        if (command.getTabCompletionList().containsKey(pos)) return;
        int i = 0;
        for (String tabArg : command.getTabCompletionList().get(pos)) {
            for (String removeArg : args) {
                if (tabArg.equals(removeArg)) command.getTabCompletionList().get(pos).remove(i);
            }
            i++;
        }
        command.getTabCompletionList().get(pos).removeIf(tabArg -> Arrays.asList(args).contains(tabArg));
    }

}
