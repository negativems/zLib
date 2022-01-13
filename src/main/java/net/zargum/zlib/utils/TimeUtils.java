package net.zargum.zlib.utils;

import java.text.DecimalFormat;
import java.time.*;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public final class TimeUtils {
    public static final long YEAR_MILLIS;
    public static final long MONTH_MILLIS;
    public static final long DAY_MILLIS;
    public static final long HOUR_MILLIS;
    public static final long MINUTE_MILLIS;
    public static final long SECOND_MILLIS;

    private TimeUtils() {
    }

    public static double getSecondsBetweenMillisAndNow(double lastMillis) {
        double currentTime = System.currentTimeMillis();
        return Double.parseDouble(new DecimalFormat("###.##").format((currentTime - lastMillis) / 1000));
    }

    public static long getTomorrowMillis(ZoneId id) {
        return ZonedDateTime.of(LocalDateTime.of(LocalDate.now(), LocalTime.MIN), id).plusDays(1L).toInstant().toEpochMilli();
    }

    public static long getTomorrowMillis(TimeZone zone) {
        return getTomorrowMillis(zone.toZoneId());
    }

    public static long getTomorrowMillis() {
        return getTomorrowMillis(DateTimeFormats.SERVER_ZONE_ID);
    }

    public static String formatDuration(long millis) {
        double seconds = (double)Math.max(0L, millis) / 1000.0D;
        int minutes = (int)(seconds / 60.0D);
        int hours = minutes / 60;
        int days = hours / 24;
        int months = days / 31;
        int years = months / 12;
        if (years >= 1) {
            return years + " year" + (years != 1 ? "s" : "");
        } else if (months >= 1) {
            return months + " month" + (months != 1 ? "s" : "");
        } else if (days >= 1) {
            return days + " day" + (days != 1 ? "s" : "");
        } else if (hours >= 1) {
            return hours + " hour" + (hours != 1 ? "s" : "");
        } else if (minutes >= 1) {
            return minutes + " minute" + (minutes != 1 ? "s" : "");
        } else {
            return seconds == 0.0D ? "1 second" : (int) seconds + " second" + (seconds != 1.0D ? "s" : "");
        }
    }

    static {
        YEAR_MILLIS = TimeUnit.DAYS.toMillis(365L);
        MONTH_MILLIS = TimeUnit.DAYS.toMillis(30L);
        DAY_MILLIS = TimeUnit.DAYS.toMillis(1L);
        HOUR_MILLIS = TimeUnit.HOURS.toMillis(1L);
        MINUTE_MILLIS = TimeUnit.MINUTES.toMillis(1L);
        SECOND_MILLIS = TimeUnit.SECONDS.toMillis(1L);
    }
}