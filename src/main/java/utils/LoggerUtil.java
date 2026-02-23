package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerUtil {

    private LoggerUtil() {
        // Private constructor to prevent instantiation
    }

    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }

    public static void info(Class<?> clazz, String message) {
        getLogger(clazz).info(message);
    }

    public static void debug(Class<?> clazz, String message) {
        getLogger(clazz).debug(message);
    }

    public static void error(Class<?> clazz, String message) {
        getLogger(clazz).error(message);
    }

    public static void error(Class<?> clazz, String message, Throwable throwable) {
        getLogger(clazz).error(message, throwable);
    }

    public static void warn(Class<?> clazz, String message) {
        getLogger(clazz).warn(message);
    }

    public static void logTestStart(Class<?> clazz, String testName) {
        getLogger(clazz).info("========== TEST STARTED: {} ==========", testName);
    }

    public static void logTestEnd(Class<?> clazz, String testName, String status) {
        getLogger(clazz).info("========== TEST ENDED: {} - STATUS: {} ==========", testName, status);
    }

    public static void logStep(Class<?> clazz, String step) {
        getLogger(clazz).info("STEP: {}", step);
    }
}
