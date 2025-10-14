package com.fosscut.utils;

import org.slf4j.Logger;
import java.io.OutputStream;

public class LoggerOutputStream extends OutputStream {

    private final Logger logger;
    private final StringBuilder buffer = new StringBuilder();

    public LoggerOutputStream(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void write(int b) {
        if (b == '\n') {
            flush();
        } else {
            buffer.append((char) b);
        }
    }

    @Override
    public void flush() {
        if (buffer.length() > 0) {
            logger.info(buffer.toString());
            buffer.setLength(0);
        }
    }

    @Override
    public void close() {
        flush();
    }

}
