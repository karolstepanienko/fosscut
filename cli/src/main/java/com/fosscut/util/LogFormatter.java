package com.fosscut.util;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.core.spi.FilterReply;

public class LogFormatter {

    private boolean quietModeRequested;

    public LogFormatter(boolean quietModeRequested) {
        this.quietModeRequested = quietModeRequested;
    }

    public void configure() {
        if (this.quietModeRequested) disableLogLevel(Level.INFO);
    }

    private void disableLogLevel(Level logLevel) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        // Create a LevelFilter to deny logLevel logs
        LevelFilter infoFilter = new LevelFilter();
        infoFilter.setLevel(logLevel);
        infoFilter.setOnMatch(FilterReply.DENY);
        infoFilter.setOnMismatch(FilterReply.NEUTRAL);
        infoFilter.start();

        Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.iteratorForAppenders().forEachRemaining(appender -> {
            appender.addFilter(infoFilter);
        });
    }

}
