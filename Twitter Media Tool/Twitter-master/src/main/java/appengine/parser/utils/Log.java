package appengine.parser.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by anand.kurapati on 03/12/17.
 */
public class Log {

    public static void print(String string) {
        try {
            System.out.println(string);
        } catch (Exception e) {

        }
    }

    public static void logIfWarning(Logger logger, String msg, int size, int errorSize) {
        Level level = Level.INFO;
        if (size == errorSize) {
            level = Level.WARNING;
        }
        logger.log(level, msg + " " + size);
    }

    public static final void logIfSevere(Logger logger, String msg, int size, int errorSize) {
        Level level = Level.INFO;
        if (size == errorSize) {
            level = Level.SEVERE;
        }
        logger.log(level, msg + " " + size);
    }

    public static final void logIfWarning(Logger logger, String msg, Object object) {
        Level level = Level.INFO;
        if (object == null) {
            level = Level.WARNING;
        }
        logger.log(level, msg);
    }

    public static final void logIfSevere(Logger logger, String msg, Object object) {
        Level level = Level.INFO;
        if (object == null) {
            level = Level.SEVERE;
        }
        logger.log(level, msg);
    }


}
