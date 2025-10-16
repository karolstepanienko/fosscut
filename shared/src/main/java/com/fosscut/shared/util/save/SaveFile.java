package com.fosscut.shared.util.save;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveFile {

    private static final Logger logger = LoggerFactory.getLogger(SaveFile.class);

    public static void saveContentToFile(String content, String outputFilePath) {
        if (outputFilePath == null) return;
        File outputFile = new File(outputFilePath);
        saveContentToFile(content, outputFile);
    }

    public static void saveContentToFile(String content, File outputFile) {
        try {
            // it cannot create directories automatically and will throw IOException
            outputFile.createNewFile();  // will do nothing if file exists
            PrintWriter out = new PrintWriter(outputFile);
            out.print(content);
            out.close();
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error("Unable to save file.");
            logger.error(e.getMessage());
        }
    }

}
