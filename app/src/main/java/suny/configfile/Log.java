package suny.configfile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Created by suny on 17-5-25.
 */

public class Log {
    private static Logger   logger = null;

    public static synchronized void init(String loggerName) {
        if (logger == null) {
            logger = Logger.getLogger(loggerName);
            logger.setLevel(Level.FINE);

            try {
                FileHandler fileHandler = new FileHandler("./mylog%u%g.log", 10 * 1024 * 1024, 5);
                fileHandler.setLevel(Level.FINE);
                fileHandler.setFormatter(new FileFormatter());
                logger.addHandler(fileHandler);
            } catch (IOException e) {
                e.printStackTrace();
                logger = null;
            }
        }
    }

    public static void e(String TAG, String msg) {
        if (logger != null)
            logger.severe(TAG + ": " + msg);
    }

    public static void e(String TAG, String msg, Throwable e) {
        if (logger != null) {
            String err = android.util.Log.getStackTraceString(e);
            logger.severe(TAG + ": " + msg + '\n' + err);
        }
    }

    public static void w(String TAG, String msg) {
        if (logger != null)
            logger.warning(TAG + ": " + msg);
    }

    public static void i(String TAG, String msg) {
        if (logger != null) {
            logger.info(TAG + ": " + msg);
        }
    }

    public static void d(String TAG, String msg) {
        if (logger != null)
            logger.fine(TAG + ": " + msg);
    }

    public static void v(String TAG, String msg) {
        if (logger != null)
            logger.finer(TAG + ": " + msg);
    }

    public static void release() {
        if (logger != null) {
            for(Handler handler : logger.getHandlers())
                handler.close();
        }
    }

    private static class FileFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            Date date = new Date(record.getMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS", Locale.CHINA);
            return sdf.format(date) + " " + record.getThreadID() + "/" + record.getLoggerName() +
                    " " + record.getLevel() + "/" + record.getMessage() + "\n";
        }
    }
}
