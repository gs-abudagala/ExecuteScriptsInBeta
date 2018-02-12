package utils;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;

/**
 * Created by athakur on 2/11/2018.
 */
public class Log {

    private static Logger log = Logger.getLogger(Log.class);

    static {
        PropertyConfigurator.configure("C:\\Users\\athakur\\Desktop\\Automation\\executeScriptsInBeta\\resources\\log4j.properties");
    }

    public static void info(String message) {
        log.log(Level.INFO, message);
    }

    public static void error(String message) {
        log.log(Level.ERROR, message);
    }

    public static void error(String message, Exception exception) {
        log.log(Level.ERROR, message, exception);
    }
}
