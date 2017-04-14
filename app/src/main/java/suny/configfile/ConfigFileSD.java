package suny.configfile;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by suny on 17-4-11.
 */

public class ConfigFileSD {
    private static final String TAG = "ConfigFileSD";
    private static final String CONFIGVERSION = "0.0.1";
    private static final String CONFIGNAME = "my.properties";
    public static String stringName = "My_String";
    public static Boolean boolName = true;
    public static int intName = 1;
    public static long longName = 1000000000L;

    public static boolean initConfig(String parentPath) {
        Log.i(TAG, "initConfig: init config file");
        String name = parentPath + CONFIGNAME;
        File file = new File(name);
        if (file.exists()) {
            int ret = loadConfig(name);
            if (ret < 0) {
                return false;
            } else if (ret > 0) {
                if (file.delete()) {
                    return createConfig(name);
                } else {
                    Log.e(TAG, "initConfig: delete error: " + name);
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return createConfig(name);
        }
    }

    //load configure file
    private static int loadConfig(String fileName) {
        Log.i(TAG, "initConfig: load config file");
        int result = -1;
        FileInputStream stream;
        try {
            stream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "loadConfig: " + e);
            return result;
        }

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

                result = 0;
            } else {
                result = 1;
            }
            try {
                stream.close();
            } catch (IOException e) {
                Log.e(TAG, "loadConfig: " + e);
                result = -1;
            }
        } catch (IOException e) {
            Log.e(TAG, "loadConfig: " + e);
        }
        return result;
    }

    //create configure file
    private static boolean createConfig(String fileName) {
        Log.i(TAG, "initConfig: create config file");

        boolean result = false;
        FileOutputStream stream;
        try {
            stream = new FileOutputStream(fileName);
        } catch (Exception e) {
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
        Log.i(TAG, "display: STRING_NAME=" + stringName);
        Log.i(TAG, "display: BOOL_NAME=" + boolName);
        Log.i(TAG, "display: INT_NAME=" + intName);
        Log.i(TAG, "display: LONG_NAME=" + longName);
    }
}
