package net.zargum.zlib.messages;

import net.zargum.zlib.utils.ColorUtils;

public enum Messages {

    NO_PERMISSION("&c# &7You do not have permission."),
    ONLY_CONSOLE("&c# &7Only console can use that command."),
    ONLY_PLAYER("&c# &7Only players can use this command."),
    COMMAND_USAGE("&c# &7Use: &e/%1 %2");

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
