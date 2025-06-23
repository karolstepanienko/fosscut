package com.fosscut.api;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestDefaults {

    public static String getDefaultSettingsJson() throws JsonProcessingException {
        Map<String, Object> settings = new HashMap<>();
        settings.put("algorithm", "FFD");
        settings.put("linearSolver", "GLOP");
        settings.put("integerSolver", "SCIP");
        settings.put("optimizationCriterion", "MinWaste");
        settings.put("relaxCost", 1);
        settings.put("relaxEnabled", false);

        ObjectMapper mapper = new ObjectMapper();
        String settingsJson = mapper.writeValueAsString(settings);
        return settingsJson;
    }

}
