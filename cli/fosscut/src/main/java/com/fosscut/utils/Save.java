package com.fosscut.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class Save {
    private String cuttingPlan;
    private boolean quietModeRequested;

    public Save(String cuttingPlan, boolean quietModeRequested) {
        this.cuttingPlan = cuttingPlan;
        this.quietModeRequested = quietModeRequested;
    }

    public void save(File outputFile) {
        if (outputFile == null) {
            if (!quietModeRequested) printIntro();
            printCuttingPlan();
        } else saveCuttingPlanToFile(outputFile);
    }

    private void printIntro() {
        System.out.println("");
        System.out.println("Generated cutting plan:");
    }

    private void printCuttingPlan() {
        System.out.println(cuttingPlan);
    }

    private void saveCuttingPlanToFile(File outputFile) {
        try {
            outputFile.createNewFile();  // will do nothing if file exists
            PrintWriter out = new PrintWriter(outputFile);
            out.print(cuttingPlan);
            out.close();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println("Unable to save file.");
            System.err.println(e.getMessage());
        }
    }
}
