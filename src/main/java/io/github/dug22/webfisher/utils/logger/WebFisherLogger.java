package io.github.dug22.webfisher.utils.logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebFisherLogger {

    private static final Logger logger = Logger.getLogger(WebFisherLogger.class.getName());
    private static final ConsoleHandler consoleHandler = new ConsoleHandler();

    static {
        logger.setLevel(Level.ALL);
        consoleHandler.setLevel(Level.ALL);
        logger.addHandler(consoleHandler);
    }

    private static String formatLogMessage(String message, LogLevelColor levelColor) {
        return levelColor.getColorCode() + message + LogLevelColor.reset();
    }

    public static void logInfo(String message) {
        logger.info(formatLogMessage(message, LogLevelColor.INFO));
    }

    public static void logWarning(String message) {
        logger.warning(formatLogMessage(message, LogLevelColor.WARNING));
    }

    public static void logSevere(String message) {
        logger.severe(formatLogMessage(message, LogLevelColor.SEVERE));
    }

    public static void logFine(String message) {
        logger.fine(formatLogMessage(message, LogLevelColor.FINE));
    }

    public static void logFiner(String message) {
        logger.finer(formatLogMessage(message, LogLevelColor.FINER));
    }

    public static void logFinest(String message) {
        logger.finest(formatLogMessage(message, LogLevelColor.FINEST));
    }

    public static void logConfig(String message) {
        logger.config(formatLogMessage(message, LogLevelColor.CONFIG));
    }
}
