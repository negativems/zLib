package net.zargum.plugin.zlib.utils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.mockito.internal.util.StringJoiner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This is used for generic Java utilities.
 */
public final class JavaUtils {

    public static boolean isInt(final String sInt) {
        try {
            Integer.parseInt(sInt);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Regex pattern to validate a UUID.
     */
    private static final Pattern UUID_PATTERN = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}");

    /**
     * The default amount of decimal places to format a number to.
     */
    private static final int DEFAULT_NUMBER_FORMAT_DECIMAL_PLACES = 5;

    private JavaUtils() {
    }

    public static Integer tryParseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static Float tryParseFloat(String string) {
        try {
            return Float.parseFloat(string);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static Double tryParseDouble(String string) {
        try {
            return Double.parseDouble(string);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    /**
     * Checks if a given String is a UUID.
     *
     * @param string a string reference to check
     * @return {@code true} if the given String is a UUID
     */
    public static boolean isUUID(String string) {
        return UUID_PATTERN.matcher(string).find();
    }

    /**
     * Checks if a given string is alphanumeric.
     *
     * @param string a string reference to check
     * @return {@code true} if the given String is alphanumeric
     */
    public static boolean isAlphanumeric(String string) {
        return string.matches("[a-zA-Z0-9]+");
    }

    /**
     * Checks if an Iterable contains a search character, handling {@code null}.
     *
     * @param elements the {@code Iterable} to check, entries may be null
     * @param string   the string to find
     * @return true if the iterable contains string
     */
    public static boolean containsIgnoreCase(Iterable<? extends String> elements, String string) {
        for (String element : elements) {
            if (StringUtils.containsIgnoreCase(element, string)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Formats a Number with {@link #DEFAULT_NUMBER_FORMAT_DECIMAL_PLACES} amount of decimal
     * places using {@link RoundingMode#HALF_DOWN} for calculating.
     *
     * @param number the {@link Number} to format
     * @return a {@code string} that has been formatted
     */
    public static String format(Number number) {
        return format(number, DEFAULT_NUMBER_FORMAT_DECIMAL_PLACES);
    }

    /**
     * Formats a Number with a given amount of decimal places using {@link RoundingMode#HALF_DOWN}
     * for calculating.
     *
     * @param number        the {@link Number} to format
     * @param decimalPlaces the decimal places to format to
     * @return a {@code string} that has been formatted
     */
    public static String format(Number number, int decimalPlaces) {
        return format(number, decimalPlaces, RoundingMode.HALF_DOWN);
    }

    /**
     * Formats a Number with a given amount of decimal places and a RoundingMode
     * to use for calculating.
     *
     * @param number        the {@link Number} to format
     * @param decimalPlaces the decimal places to format to
     * @param roundingMode  the {@link RoundingMode} for calculating
     * @return a {@code string} that has been formatted
     */
    public static String format(Number number, int decimalPlaces, RoundingMode roundingMode) {
        Preconditions.checkNotNull(number, "The number cannot be null");
        return new BigDecimal(number.toString()).setScale(decimalPlaces, roundingMode).stripTrailingZeros().toPlainString();
    }

    //TODO: The following below needs to be cleaned up and/or rewritten.

    /**
     * Joins a profilesCollection of strings together using {@link Joiner#join(Iterable)} as a base
     * with the last object using 'and' just before instead of the selected delimiter as a comma.
     *
     * @param collection         the profilesCollection to join
     * @param delimiterBeforeAnd if the delimiterBeforeAnd should be shown before the 'and' text
     * @return the returned list or empty string is profilesCollection is null or empty
     */
    public static String join(Collection<String> collection, boolean delimiterBeforeAnd) {
        return StringJoiner.join(collection, delimiterBeforeAnd, ", ");
    }

    /**
     * Joins a profilesCollection of strings together using {@link Joiner#join(Iterable)} as a base
     * with the last object using 'and' just before instead of the selected delimiter.
     *
     * @param collection         the profilesCollection to join
     * @param delimiterBeforeAnd if the delimiterBeforeAnd should be shown before the 'and' text
     * @param delimiter          the delimiter to join with
     * @return the returned list or empty string is profilesCollection is null or empty
     */
    public static String join(Collection<String> collection, boolean delimiterBeforeAnd, String delimiter) {
        if (collection == null || collection.isEmpty()) return "";

        List<String> contents = new ArrayList<>(collection);
        String last = contents.remove(contents.size() - 1);

        StringBuilder builder = new StringBuilder(Joiner.on(delimiter).join(contents));
        if (delimiterBeforeAnd) builder.append(delimiter);

        if (contents.size() > 0) builder.append(" and ");
        return builder.append(last).toString();
    }


    public static String secondsFormated(long s) {
        StringBuilder string = new StringBuilder();

        long seconds = s % 60;
        long minutes = s % 3600 / 60;
        long hours = s % 86400 / 3600;
        long days = s / 86400;

        if (days > 0) string.append(days).append(" days, ");
        if (hours > 0) string.append(hours).append(" hours, ");
        if (minutes > 0) string.append(minutes).append(" minutes, ");
        string.append(seconds).append(" seconds");

        return string.toString();
    }
}