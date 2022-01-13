package net.zargum.zlib.utils;

public class TextUtils {

    public static String getSpacedText(String text, int endLength) {
        if (text.length() > endLength) return text;
        StringBuilder stringBuilder = new StringBuilder(text);
        for (int i = 0; i < endLength - text.length(); i++) {
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    public static String getOrdinalIndicator(int i) {
        return (i == 1) ? "st" : ((i == 2) ? "nd" : ((i == 3) ? "rd" : "th"));
    }
}
