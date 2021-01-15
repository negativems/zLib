package net.zargum.zlib.utils;

import org.apache.commons.lang.time.FastDateFormat;

import java.text.DecimalFormat;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeFormats {

    public static final TimeZone SERVER_TIME_ZONE = TimeZone.getTimeZone("EST");
    public static final ZoneId SERVER_ZONE_ID = SERVER_TIME_ZONE.toZoneId();

    public static final FastDateFormat E_MMM_DAY_HR_MIN_SECS_YR = FastDateFormat.getInstance("E MMM dd h:mm:ssa z yyyy", SERVER_TIME_ZONE);
    public static final FastDateFormat DAY_MTH_HR_MIN_SECS = FastDateFormat.getInstance("dd/MM HH:mm:ss", SERVER_TIME_ZONE, Locale.ENGLISH);
    public static final FastDateFormat DAY_MTH_YR_HR_MIN_AMPM = FastDateFormat.getInstance("dd/MM/yy hh:mma", SERVER_TIME_ZONE, Locale.ENGLISH);
    public static final FastDateFormat DAY_MTH_HR_MIN_AMPM = FastDateFormat.getInstance("dd/MM hh:mma", SERVER_TIME_ZONE, Locale.ENGLISH);
    public static final FastDateFormat DAY_MTH = FastDateFormat.getInstance("dd/MM", SERVER_TIME_ZONE, Locale.ENGLISH);
    public static final FastDateFormat DAY_MTH_YR = FastDateFormat.getInstance("dd/MM/yy", SERVER_TIME_ZONE, Locale.ENGLISH);
    public static final FastDateFormat HR_MIN_AMPM = FastDateFormat.getInstance("hh:mma", SERVER_TIME_ZONE, Locale.ENGLISH);
    public static final FastDateFormat HR_MIN = FastDateFormat.getInstance("hh:mm", SERVER_TIME_ZONE, Locale.ENGLISH);
    public static final FastDateFormat MIN_SECS = FastDateFormat.getInstance("mm:ss", SERVER_TIME_ZONE, Locale.ENGLISH);

    public static final FastDateFormat KOTH_FORMAT = FastDateFormat.getInstance("m:ss", SERVER_TIME_ZONE, Locale.ENGLISH);

    public static final ThreadLocal<DecimalFormat> REMAINING_SECONDS = ThreadLocal.withInitial(() -> new DecimalFormat("0.#"));
    public static final ThreadLocal<DecimalFormat> REMAINING_SECONDS_TRAILING = ThreadLocal.withInitial(() -> new DecimalFormat("0.0"));


}
