package net.zargum.zlib.utils;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.mockito.internal.util.StringJoiner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public final class JavaUtils {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{1,16}$");
    private static final Pattern UUID_PATTERN = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}");
    private static final int DEFAULT_NUMBER_FORMAT_DECIMAL_PLACES = 5;
    private static final CharMatcher CHAR_MATCHER_ASCII = CharMatcher.inRange('0', '9').
            or(CharMatcher.inRange('a', 'z')).
            or(CharMatcher.inRange('A', 'Z')).
            or(CharMatcher.WHITESPACE).
            precomputed();

    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    public static boolean isHexNumber(String n) {
        try {
            Long.parseLong(n, 16);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean isBoolean(String value) {
        return value != null && Arrays.stream(new String[]{"true", "false", "1", "0"}).anyMatch(b -> b.equalsIgnoreCase(value));
    }

    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isFloat(String s) {
        try {
            Float.parseFloat(s);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static boolean isUniqueId(String s) {
        return UUID_PATTERN.matcher(s).find();
    }

    public static boolean isAlphanumeric(String s) {
        return s.matches("[a-zA-Z0-9]+");
    }

    public static boolean isValidUsername(String s) {
        return USERNAME_PATTERN.matcher(s).matches();
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
}