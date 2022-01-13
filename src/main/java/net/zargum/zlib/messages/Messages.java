package net.zargum.zlib.messages;

import net.zargum.zlib.utils.ColorUtils;

public enum Messages {

    AMOUNT_MENU_LORE(
            "&6Left click&e to %1 &c%2&e %5 the amount.",
            "&6Middle click&e to %1 &c%3&e %5 the amount.",
            "&6Right click&e to %1 &c%4&e %5 the amount."
    ),
    NO_PERMISSION("&c# &7You do not have permission."),
    ONLY_CONSOLE("&c# &7Only console can use that command."),
    ONLY_PLAYER("&c# &7Only players can use this command."),
    USAGE_MESSAGE_HEADER("%1#%2&m---------&r %1%3&r &7(%1%4&7/%1%5&7)&r %2&m---------%1#"),
    USAGE_MESSAGE_ARGUMENT("%1# &7/%2 %3 %4 %1-&f %5."),
    USAGE_MESSAGE_FOOTER("%1#%2&m-----------------------------%1#"),
    USAGE_COMMAND("&c# &7Use: &e/%1 %2"),
    INVALID_HELP_PAGE("&c# &7Invalid page number."),
    USAGE_COMMAND_ARGUMENT("&c# &7Use: &e/%1 %2 %3"),
    UNKNOWN_ARGUMENT("&c# &7Unknown argument: /%1 &c%2&7"),
    ARG_ONLY_PLAYER("&c# &7That argument can be used only by a player."),
    ARG_ONLY_CONSOLE("&c# &7That argument can be used only by the console."),
    CONNECT_OFFLINE("&c# &e%1 &cis currently offline."),
    CONNECT_WHITELISTED("&c# &e%1 &cis currently on maintenance."),
    CONNECT_INITIALIZING("&c# &e%1 &7is initializing, wait a few seconds to join!.");

    private final String value;

    Messages(String message) {
        this.value = message;
    }

    Messages(String... message) {
        StringBuilder t = new StringBuilder();
        for (String s : message) {
            t.append(s).append("\n");
        }
        this.value = t.toString();
    }

    @Override
    public String toString() {
        return ColorUtils.translate(value);
    }

    public String toString(String... args) {
        String result = this.value;
        int i = 1;
        for (String arg : args) result = result.replaceAll("%" + i++, arg);
        return ColorUtils.translate(result);
    }

    public String[] toStringList() {
        return ColorUtils.translate(this.value.split("\n"));
    }
    public String[] toStringList(String... args) {
        String t = this.value;
        int i = 1;
        for (String arg : args) t = t.replaceAll("%" + i++, arg);
        return ColorUtils.translate(t.split("\n"));
    }
}
