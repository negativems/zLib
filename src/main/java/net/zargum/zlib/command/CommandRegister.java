package net.zargum.zlib.command;

import lombok.Getter;
import net.zargum.zlib.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class CommandRegister<T extends JavaPlugin> implements CommandExecutor, TabExecutor {

    private final T plugin;
    @Getter private final Map<String, ServerCommand<T>> serverCommands = new HashMap<>();
    private CommandMap commandMap;

    public CommandRegister(T plugin) {
        this.plugin = plugin;
        commandMap = loadCommandMap();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String commandName = cmd.getName().toLowerCase();
        final ServerCommand<T> command = serverCommands.get(commandName);

        if (command.getMinArgRequired() > args.length) {
            sender.sendMessage(command.getUsage());
            return true;
        }

        List<String> argumentsName = new ArrayList<>();
        for (ServerCommandArgument<T> argument : command.getArguments().values()) {
            argumentsName.add(argument.getArgumentName());
            argumentsName.addAll(Arrays.asList(argument.getAliases()));
        }

        if (!command.isReservedArgs() || argumentsName.contains(args[0])) {
            if (args.length > 0 && command.getArguments().size() > 0) {
                if (!argumentsName.contains(args[0])) {
                    sender.sendMessage(Messages.UNKNOWN_ARGUMENT.toString(commandName, args[0]));
                    return true;
                }
                for (ServerCommandArgument<T> argument : command.getArguments().values()) {
                    String argumentName = argument.getArgumentName();
                    if (argumentName.equalsIgnoreCase(args[0]) || Arrays.asList(argument.getAliases()).contains(args[0].toLowerCase())) {
                        String[] argumentArgs = Arrays.copyOfRange(args, 1, args.length);
                        if (argument.isRequiresPermission() && !sender.hasPermission("command." + commandName + "." + argumentName)) {
                            sender.sendMessage(Messages.NO_PERMISSION.toString());
                            return true;
                        }
                        if (argument.isConsoleOnly() && sender instanceof Player) {
                            sender.sendMessage(Messages.ARG_ONLY_CONSOLE.toString());
                            return true;
                        }
                        if (argument.isPlayerOnly() && sender instanceof ConsoleCommandSender) {
                            sender.sendMessage(Messages.ARG_ONLY_PLAYER.toString());
                            return true;
                        }
                        if (argument.getMinArgRequired() > argumentArgs.length) {
                            sender.sendMessage(argument.getUsage());
                            return true;
                        }
                        argument.execute(sender, argumentName, argumentArgs);
                        return false;
                    }
                }
            }
        }

        if (command.isConsoleOnly() && sender instanceof Player) {
            sender.sendMessage(Messages.ONLY_CONSOLE.toString());
            return true;
        }
        if (command.isPlayerOnly() && sender instanceof ConsoleCommandSender) {
            sender.sendMessage(Messages.ONLY_PLAYER.toString());
            return true;
        }
        if (command.isAsync()) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> command.execute(sender, label, args));
            return false;
        }
        command.execute(sender, label, args);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        String commandName = cmd.getName().toLowerCase();
        if (!serverCommands.containsKey(commandName)) return null;
        if (!sender.hasPermission("command." + label)) return null;
        final ServerCommand<T> command = serverCommands.get(commandName);

        Map<Integer, List<String>> completionList = command.getTabCompletionList();
        if (!completionList.containsKey(args.length - 1)) return null;

        return completionList.get(args.length - 1).stream().filter(
                s -> s.startsWith(args[args.length - 1])
        ).collect(Collectors.toList());
    }

    public void registerCommand(ServerCommand<T> serverCommand) {
        String commandName = serverCommand.getName();
        String[] aliases = serverCommand.getAliases();
        boolean requiresPermission = serverCommand.isRequiresPermission();
        String description = serverCommand.getDescription();

        PluginCommand command = getCommand(commandName, plugin);
        command.setPermissionMessage(Messages.NO_PERMISSION.toString());
        if (aliases.length > 0) command.setAliases(Arrays.asList(aliases));
        command.setExecutor(this);
        command.setTabCompleter(this);
        if (requiresPermission) command.setPermission((serverCommand.getPermission()).toLowerCase());
        if (description != null) command.setDescription(description);

        if (!commandMap.register(serverCommand.getName(), command)) {
            command.unregister(commandMap);
            commandMap.register(serverCommand.getName(), command);
        }

        serverCommands.put(commandName, serverCommand);
    }

    public PluginCommand getCommand(String name, Plugin plugin) {
        PluginCommand command = null;

        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);

            command = constructor.newInstance(name, plugin);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return command;
    }

    public CommandMap loadCommandMap() {
        if (commandMap != null) return commandMap;
        try {
            Field commandMapFiled = SimplePluginManager.class.getDeclaredField("commandMap");
            commandMapFiled.setAccessible(true);
            commandMap = (CommandMap) commandMapFiled.get(Bukkit.getPluginManager());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return commandMap;
    }
}