package at.technikum.wien.mse.swe.logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Simple logger used for logging information
 */
public final class SimpleLogger {
    private static final boolean DEBUG_ENABLED = false;
    private static Calendar cal = Calendar.getInstance();
    private static SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");

    public static void debug(String s) {
        if (!DEBUG_ENABLED) {
            return;
        }
        System.out.println(sdf.format(cal.getTime()) + " [DEBUG] " + s);
    }

    public static void info(String s) {
        System.out.println(sdf.format(cal.getTime()) + " [INFO] " + s);
    }

    public static void warn(String s) {
        System.out.println(sdf.format(cal.getTime()) + " [WARNING] " + s);
    }

    public static void error(String s) {
        System.out.println(sdf.format(cal.getTime()) + " [ERROR] " + s);
    }
}