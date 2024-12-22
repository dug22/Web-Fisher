package io.github.dug22.webfisher.utils.logger;

public enum LogLevelColor {

    INFO("\u001B[34m"),        // Blue
    WARNING("\u001B[33m"),     // Yellow
    SEVERE("\u001B[31m"),      // Red
    FINE("\u001B[36m"),        // Cyan
    FINER("\u001B[36m"),       // Cyan
    FINEST("\u001B[36m"),      // Cyan
    CONFIG("\u001B[32m");      // Green

    private final String colorCode;

    LogLevelColor(String colorCode){
        this.colorCode = colorCode;
    }

    public String getColorCode() {
        return colorCode;
    }

    public static String reset() {
        return "\u001B[0m";
    }
}
