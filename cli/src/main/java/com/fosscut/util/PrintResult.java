package com.fosscut.util;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintResult {

    private static final Logger logger = LoggerFactory.getLogger(PrintResult.class);

    private String name;
    private File outputFile;

    public PrintResult(String name, File outputFile) {
        this.name = name;
        this.outputFile = outputFile;
    }

    public void print(String result) {
        if (outputFile == null) {
            logger.info("");
            logger.info("Generated " + this.name + ":");
            logger.info(result);
        }
    }

}
