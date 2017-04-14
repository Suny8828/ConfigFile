package suny.configfile;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by suny on 17-4-11.
 */

public class ConfigFile {
    private static final String CONFIGVERSION = "0.0.1";
    public static final String TAG = "ConfigFile";
    public static final String CONFIGNAME = "my.properties";
    public static String stringName = "My_String";
    public static Boolean boolName = true;
    public static int intName = 1;
    public static long longName = 1000000000L;

    public static boolean initConfig(Context context) {
        Log.i(TAG, "initConfig: init config file");
        FileInputStream stream;
        try {
            stream = context.openFileInput(CONFIGNAME);
            int ret = loadConfig(stream);
            if (ret < 0) {
                return false;
            } else if (ret > 0) {
                try {
                    stream.close();
                } catch (IOException e) {
                    Log.e(TAG, "initConfig: " + e);
                    return false;
                }

                context.deleteFile(CONFIGNAME);
                return createConfig(context, CONFIGNAME);
            } else {
                return true;
            }
        } catch (FileNotFoundException e) {
            Log.w(TAG, "initConfig: " + e);
            return createConfig(context, CONFIGNAME);
        }
    }

    //load configure file
    private static int loadConfig(FileInputStream stream) {
        Log.i(TAG, "initConfig: load config file");
        Properties properties = new Properties();
        try {
            properties.load(stream);

            String tmpStr = properties.getProperty("CONFIG_VERSION");
            if (tmpStr != null && CONFIGVERSION.compareTo(tmpStr) == 0) {
                tmpStr = properties.getProperty("STRING_NAME");
                if (tmpStr != null)
                    stringName = tmpStr;

                tmpStr = properties.getProperty("BOOL_NAME");
                if (tmpStr != null)
                    boolName = tmpStr.equals("true");

                tmpStr = properties.getProperty("INT_NAME");
                if (tmpStr != null && Integer.parseInt(tmpStr) > 0)
                    intName = Integer.parseInt(tmpStr);

                tmpStr = properties.getProperty("LONG_NAME");
                if (tmpStr != null && Long.parseLong(tmpStr) > 0)
                    longName = Integer.parseInt(tmpStr);

                return 0;
            } else {
                return 1;
            }
        } catch (IOException e) {
            Log.e(TAG, "loadConfig: " + e);
            return -1;
        }
    }

    //create configure file
    private static boolean createConfig(Context context, String fileName) {
        Log.i(TAG, "initConfig: create config file");
        boolean result = false;
        FileOutputStream stream;
        try {
            stream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "createConfig: " + e);
            return false;
        }

        Properties properties = new Properties();
        properties.put("CONFIG_VERSION", CONFIGVERSION);
        properties.put("STRING_NAME", stringName);
        properties.put("BOOL_NAME", boolName ? "true" : "false");
        properties.put("INT_NAME", String.valueOf(intName));        // Integer.toString(intName)
        properties.put("LONG_NAME", String.valueOf(longName));      // Long.toString(longName)

        try {
            properties.store(stream, "My configure file");
            result = true;
        } catch (Exception e) {
            Log.e(TAG, "createConfig: " + e);
        }

        try {
            stream.close();
        } catch (IOException e) {
            Log.e(TAG, "createConfig: " + e);
            result = false;
        }
        return result;
    }

    // test
    public static void display() {
        Log.i(StaticString.TAG, "MainActivity: STRING_NAME=" + stringName);
        Log.i(StaticString.TAG, "MainActivity: BOOL_NAME=" + boolName);
        Log.i(StaticString.TAG, "MainActivity: INT_NAME=" + intName);
        Log.i(StaticString.TAG, "MainActivity: LONG_NAME=" + longName);
    }
}
