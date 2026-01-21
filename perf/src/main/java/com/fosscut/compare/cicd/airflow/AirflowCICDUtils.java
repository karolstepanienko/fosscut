package com.fosscut.compare.cicd.airflow;

import java.util.List;

import com.fosscut.compare.cicd.CICDUtils;

public class AirflowCICDUtils {

    CICDUtils cicdUtils;

    public AirflowCICDUtils(String RUN_ID, int NUM_PARTS) {
        cicdUtils = new CICDUtils(RUN_ID, NUM_PARTS);
    }

    public List<String> generateIdentifiers() {
        return removeLeadingDashFromIdentifiers(cicdUtils.generateIdentifiers());
    }

    private List<String> removeLeadingDashFromIdentifiers(List<String> identifiers) {
        for (int i = 0; i < identifiers.size(); i++) {
            identifiers.set(i, identifiers.get(i).substring(1));
        }
        return identifiers;
    }

}
