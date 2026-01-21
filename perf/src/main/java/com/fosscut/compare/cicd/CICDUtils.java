package com.fosscut.compare.cicd;

import java.util.ArrayList;
import java.util.List;

public class CICDUtils {

    private String RUN_ID;
    private int NUM_PARTS;

    public CICDUtils(String RUN_ID, int NUM_PARTS) {
        this.RUN_ID = RUN_ID;
        this.NUM_PARTS = NUM_PARTS;
    }

    public List<String> generateIdentifiers() {
        List<String> identifiers = new ArrayList<>();
        for (int part = 0; part < NUM_PARTS; part++) {
            identifiers.add("-run-" + RUN_ID + "-part-" + part);
        }
        return identifiers;
    }

}
