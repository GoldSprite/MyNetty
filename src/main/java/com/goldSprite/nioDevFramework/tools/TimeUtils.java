package com.goldSprite.nioDevFramework.tools;
import java.text.*;
import java.time.*;
import java.util.TimeZone;

public class TimeUtils {

    public static final long Milli2Nanos = 1_000_000L;
    public static final long Second2Nanos = 1_000_000_000L;
    public static final long Minute2Nanos = Second2Nanos * 60;
    public static final long Hour2Nanos = Minute2Nanos * 60;
    public static final long Day2Nanos = Hour2Nanos * 24;

    public static final String Format_Date = "yyyy-MM-dd";
    public static final String Format_Time = "HH:mm:ss";
    public static final String Format_DateTime = "yyyy-MM-dd HH:mm:ss:SSS";
    static SimpleDateFormat simpleFormat = new SimpleDateFormat(Format_Date);
    static {
        simpleFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
    }

    public static String formatDate() { return formatTime(Format_Date); }
    public static String formatAddDate(long addNanos) { return formatAddTime(addNanos, Format_Date); }
    public static String formatDate(long totalNanos) { return formatTime(totalNanos, Format_Date); }

    public static String formatTime() { return formatTime(Format_Time); }
    public static String formatAddTime(long addNanos) { return formatAddTime(addNanos, Format_Time); }
    public static String formatTime(long totalNanos) { return formatTime(totalNanos, Format_Time); }

    public static String formatDateTime() { return formatTime(Format_DateTime); }
    public static String formatAddDateTime(long addNanos) { return formatAddTime(addNanos, Format_DateTime); }
    public static String formatDateTime(long totalNanos) { return formatTime(totalNanos, Format_DateTime); }

    public static String formatTime(String format) { return formatTime(nowTimeNanos(), format); }
    public static String formatAddTime(long addNanos, String format) { return formatTime(nowTimeNanos() + addNanos, format); }
    public static String formatTime(long totalNanos, String format) {
        simpleFormat.applyPattern(format);
        return simpleFormat.format((double)totalNanos / Milli2Nanos);
    }

	/**
     * 格式化时间（纳秒为单位）并根据精度返回字符串
     * @param nanos 传入的总纳秒数
     * @param precision 精度：0显示到ms，1显示到时分秒，2显示到分钟，3显示到小时
     * @return 格式化后的时间字符串
     */
    public static String formatSpendTime(long nanos, int precision) {
        // 计算小时、分钟、秒、毫秒
        long hours = nanos / Hour2Nanos;
        long minutes = (nanos % Hour2Nanos) / Minute2Nanos;
        long seconds = (nanos % Minute2Nanos) / Second2Nanos;
        long milliseconds = (nanos % Second2Nanos) / Milli2Nanos;

        StringBuilder timeString = new StringBuilder();

        if (precision == 0) {
            // 精度为0，显示到毫秒
            if (hours > 0) timeString.append(hours).append("h:");
            if (minutes > 0) timeString.append(minutes).append("min:");
            if (seconds > 0) timeString.append(seconds).append("s:");
            if (milliseconds > 0 || hours == 0 && minutes == 0 && seconds == 0) 
                timeString.append(milliseconds).append("ms");
        }
		else if (precision == 1) {
            // 精度为1，显示到时分秒
            if (hours > 0) timeString.append(hours).append("h:");
            if (minutes > 0) timeString.append(minutes).append("min:");
            if (seconds > 0) timeString.append(seconds).append("s");
        }
		else if (precision == 2) {
            // 精度为2，显示到分钟
            if (minutes > 0) timeString.append(minutes).append("min");
        }
		else if (precision == 3) {
            // 精度为3，显示到小时
            if (hours > 0) timeString.append(hours).append("h");
        }

        return timeString.toString();
    }

    public static long nowTimeMillis() {
        Instant instant = Instant.now();
        long epochMillis = instant.toEpochMilli();
		return epochMillis;
	}
    public static long nowTimeNanos() {
        Instant instant = Instant.now();
		long epochSeconds = instant.getEpochSecond();
        int nanoAdjustment = instant.getNano();
        long epochNanos = epochSeconds * Second2Nanos + nanoAdjustment;
        return epochNanos;
    }

    public static void main(String[] args) {
		System.out.println("Formatted DateTime (0 in nanos): " + formatDateTime(0));
        // 1. formatDate()
        String formattedDate = formatDate();
        System.out.println("Formatted Date (Default): " + formattedDate);

        // 2. formatAddDate(long addNanos)
        long addNanos = Day2Nanos * 5; // Add 5 days in nanoseconds
        String formattedAddDate = formatAddDate(addNanos);
        System.out.println("Formatted Date (Add 5 days): " + formattedAddDate);

        // 3. formatDate(long totalNanos)
        long totalNanos = Day2Nanos * 10; // 10 days in nanoseconds
        String formattedDateWithNanos = formatDate(totalNanos);
        System.out.println("Formatted Date (10 days in nanos): " + formattedDateWithNanos);

        // 4. formatTime()
        String formattedTime = formatTime();
        System.out.println("Formatted Time (Default): " + formattedTime);

        // 5. formatAddTime(long addNanos)
        addNanos = Minute2Nanos * 2; // Add 2 minutes in nanoseconds
        String formattedAddTime = formatAddTime(addNanos);
        System.out.println("Formatted Time (Add 2 minutes): " + formattedAddTime);

        // 6. formatTime(long totalNanos)
        totalNanos = Hour2Nanos; // 1 hour in nanoseconds
        String formattedTimeWithNanos = formatTime(totalNanos);
        System.out.println("Formatted Time (1 hour in nanos): " + formattedTimeWithNanos);

        // 7. formatDateTime()
        String formattedDateTime = formatDateTime();
        System.out.println("Formatted DateTime (Default): " + formattedDateTime);

        // 8. formatAddDateTime(long addNanos)
        addNanos = Hour2Nanos * 3; // Add 3 hours in nanoseconds
        String formattedAddDateTime = formatAddDateTime(addNanos);
        System.out.println("Formatted DateTime (Add 3 hours): " + formattedAddDateTime);

        // 9. formatDateTime(long totalNanos)
        totalNanos = Day2Nanos * 2; // 2 days in nanoseconds
        String formattedDateTimeWithNanos = formatDateTime(totalNanos);
        System.out.println("Formatted DateTime (2 days in nanos): " + formattedDateTimeWithNanos);

        // 10. nowTimeMillis()
        long nowMillis = nowTimeMillis();
        System.out.println("Current Time in Millis: " + nowMillis);

        // 11. nowTimeNanos()
        long nowNanos = nowTimeNanos();
        System.out.println("Current Time in Nanos: " + nowNanos);

        // 12. formatTime(String format)
		String format = "yyyy/MM/dd HH:mm:ss";
        String formattedTimeWithCustomFormat = formatTime(format);
        System.out.println("Formatted Time (Custom Format[" + format + "]): " + formattedTimeWithCustomFormat);

        // 13. formatAddTime(long addNanos, String format)
        addNanos = Minute2Nanos * 5; // Add 5 minutes in nanoseconds
		format = "HH:mm:ss";
        String formattedAddTimeWithCustomFormat = formatAddTime(addNanos, format);
        System.out.println("Formatted Time (Add 5 minutes, Custom Format[" + format + "]): " + formattedAddTimeWithCustomFormat);
    }
}
