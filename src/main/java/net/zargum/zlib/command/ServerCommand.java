package net.zargum.zlib.command;

import lombok.Getter;
import net.zargum.zlib.messages.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.ParameterizedType;
import java.util.*;

@Getter
public abstract class ServerCommand<T extends JavaPlugin> {

    public final T plugin;

    private final String name;
    private final String label;
    private final String description;
    private String[] aliases = new String[]{};
    private final String usageMessage;
    private int minArgRequired = 0;
    private final String permission;
    private final boolean requiredPermission = true;
    private final boolean consoleOnly = false;
    private final boolean playerOnly = false;
    private final List<ServerCommandArgument<T>> arguments = new ArrayList<>();
    private final Map<Integer, List<String>> tabCompletionList = new HashMap<>();

    public ServerCommand(String name, String description, String usage, String aliases) {
        plugin = JavaPlugin.getPlugin((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);

        this.name = name.toLowerCase();
        this.description = description;
        this.label = name;
        usageMessage = Messages.COMMAND_USAGE.toString(name, usage);
        if (aliases != null && !aliases.equals("")) this.aliases = aliases.split(", ");
        permission = "command." + name;
        if (!usage.isEmpty()) {
            String[] args = usage.split(" ");
            for (String arg : args) if (arg.charAt(0) == '<') minArgRequired++;
        }
    }

    public ServerCommand(String name, String description, String usage) {
        this(name, description, usage, "");
    }

    public ServerCommand(String name, String description) {
        this(name, description, "", "");
    }

    public abstract void execute(String label, CommandSender sender, String[] args);

    public void addArgument(ServerCommandArgument<T> argument) {
        arguments.add(argument);
        addToTablistCompletion(1, argument.getArgumentName());
    }

    public void addToTablistCompletion(int pos, String arg) {
        if (tabCompletionList.containsKey(pos)) {
            tabCompletionList.get(pos).add(arg);
        }
        else {
            List<String> arguments = new ArrayList<>();
            arguments.add(arg);
            tabCompletionList.put(pos, arguments);
        }
    }

    public void addToTablistCompletion(int pos, String... args) {
        tabCompletionList.put(pos, Arrays.asList(args));
    }

    public void removeFromTablistCompletion(int pos, String arg) {
        tabCompletionList.get(pos).remove(arg);
    }

    public void removeFromTablistCompletion(int pos, String... args) {
        tabCompletionList.get(pos).removeIf(tabArg -> Arrays.asList(args).contains(tabArg));
    }
}