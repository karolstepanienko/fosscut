package com.fosscut.util;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintResult {

    private static final Logger logger = LoggerFactory.getLogger(PrintResult.class);

    private String name;
    private File outputFile;
    private String outputPath;

    public PrintResult(String name, File outputFile) {
        this.name = name;
        this.outputFile = outputFile;
        this.outputPath = null;
    }

    public PrintResult(String name, String outputPath) {
        this.name = name;
        this.outputFile = null;
        this.outputPath = outputPath;
    }

    public void print(String result) {
        if (shouldPrint()) {
            logger.info("");
            logger.info("Generated " + this.name + ":");
            logger.info(result);
        }
    }

    private boolean shouldPrint() {
        if (outputFile == null && outputPath == null) return true;
        else return false;
    }

}
