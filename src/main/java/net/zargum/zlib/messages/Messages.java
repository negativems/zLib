package net.zargum.zlib.messages;

import net.zargum.zlib.utils.ColorUtils;

public enum Messages {

    COMMAND_USAGE("&c# &7Use: &e/%1 %2");

    private String value;

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
        String t = this.value;
        return ColorUtils.translate(t.split("\n"));
    }
    public String[] toStringList(String... args) {
        String t = this.value;
        int i = 1;
        for (String arg : args) t = t.replaceAll("%" + i++, arg);
        return ColorUtils.translate(t.split("\n"));
    }
}
