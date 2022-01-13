package net.zargum.zlib.command;

import lombok.Getter;
import lombok.Setter;
import net.zargum.zlib.messages.Messages;
import net.zargum.zlib.utils.JavaUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.ParameterizedType;
import java.util.*;

@Getter
public abstract class ServerCommand<T extends JavaPlugin> {

    public final T plugin;

    public final static int MAX_COMMANDS_PER_PAGE = 7;

    private final String name, description, usageFormat, permission, defaultUsageMessage;
    @Setter
    protected String usage, pagedHelpTitle;
    protected boolean pagedHelp;
    private String[] aliases = new String[]{};
    private final Map<String, ServerCommandArgument<T>> arguments = new LinkedHashMap<>();
    private final Map<Integer, List<String>> tabCompletionList = new LinkedHashMap<>();
    private final boolean requiresPermission = true;
    private boolean consoleOnly, playerOnly, async, reservedArgs;
    private int minArgRequired = 0;

    public ServerCommand(String commandName, String description, String usageFormat, String aliases) {
        plugin = JavaPlugin.getPlugin((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);

        this.name = commandName.toLowerCase();
        this.description = description;
        this.usageFormat = usageFormat;
        if (aliases != null && !aliases.equals("")) this.aliases = aliases.split(", ");
        defaultUsageMessage = Messages.USAGE_COMMAND.toString(name, usageFormat);
        usage = defaultUsageMessage;
        pagedHelpTitle = JavaUtils.capitalizeFirstLetter(commandName + " Help");
        permission = "command." + commandName;
        if (!usageFormat.isEmpty()) {
            String[] args = usageFormat.split(" ");
            for (String arg : args) if (arg.charAt(0) == '<') minArgRequired++;
        }
    }

    public String getHelpPage(String title, ChatColor primaryColor, ChatColor secondaryColor, int page) {
        StringBuilder messageBuilder = new StringBuilder();
        int maxPagesTmp = arguments.size() < MAX_COMMANDS_PER_PAGE ? 1 : arguments.size() / MAX_COMMANDS_PER_PAGE;
        int maxPages = maxPagesTmp * MAX_COMMANDS_PER_PAGE < arguments.size() ? maxPagesTmp + 1 : maxPagesTmp;
        if (page > maxPages) throw new IllegalStateException("Invalid help page for command /" + name);
        String header = Messages.USAGE_MESSAGE_HEADER.toString(primaryColor.toString(), secondaryColor.toString(), title, String.valueOf(page), String.valueOf(maxPages));
        String footer = Messages.USAGE_MESSAGE_FOOTER.toString(primaryColor.toString(), secondaryColor.toString());

        messageBuilder.append(header).append("\n");
        int argumentIndex = page * MAX_COMMANDS_PER_PAGE - (MAX_COMMANDS_PER_PAGE - 1);
        List<ServerCommandArgument<T>> arguments = new ArrayList<>(this.arguments.values()).subList(argumentIndex - 1, this.arguments.size());
        for (ServerCommandArgument<T> argument : arguments) {
            if (argumentIndex > (page * MAX_COMMANDS_PER_PAGE) || argumentIndex < (page * MAX_COMMANDS_PER_PAGE - (MAX_COMMANDS_PER_PAGE - 1)))
                continue;
            argumentIndex++;
            messageBuilder.append(Messages.USAGE_MESSAGE_ARGUMENT.toString(
                    primaryColor.toString(), argument.getCommand().getName(),
                    argument.getArgumentName(), argument.getUsageFormat(),
                    argument.getDescription()
            )).append("\n");
        }
        messageBuilder.append(footer);
        return messageBuilder.toString();
    }

    public String getHelpPage(String title, int page) {
        return getHelpPage(title, ChatColor.DARK_PURPLE, ChatColor.LIGHT_PURPLE, page);
    }

    public ServerCommand(String name, String description, String usage) {
        this(name, description, usage, "");
    }

    public ServerCommand(String name, String description) {
        this(name, description, "", "");
    }

    public abstract void execute(CommandSender sender, String label, String[] args);

    public void addArgument(ServerCommandArgument<T> argument) {
        arguments.put(argument.getArgumentName(), argument);
        addToTablistCompletion(0, argument.getArgumentName());
    }

    public void addToTablistCompletion(int pos, String arg) {
        if (tabCompletionList.containsKey(pos)) {
            tabCompletionList.get(pos).add(arg);
        } else {
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