package com.fosscut.util.save;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fosscut.alg.cg.ColumnGeneration;
import com.fosscut.alg.ffd.FirstFitDecreasing;
import com.fosscut.exception.NotIntegerLPTaskException;
import com.fosscut.type.cutting.plan.CuttingPlan;

public class YamlDumper {

    private static final Logger logger = LoggerFactory.getLogger(YamlDumper.class);

    public String dump(FirstFitDecreasing firstFitDecreasing) {
        CuttingPlan cuttingPlan = new CuttingPlan();
        cuttingPlan = firstFitDecreasing.getCuttingPlan();
        return serialiseCuttingPlan(cuttingPlan);
    }

    public String dump(ColumnGeneration columnGeneration) {
        CuttingPlan cuttingPlan = new CuttingPlan();
        try {
            cuttingPlan = columnGeneration.getCuttingPlan();
        } catch (NotIntegerLPTaskException e) {
            logger.error(e.getMessage());
            System.exit(1);
        }
        return serialiseCuttingPlan(cuttingPlan);
    }

    private String serialiseCuttingPlan(CuttingPlan cuttingPlan) {
        ObjectMapper yamlMapper = getObjectMapper();
        String cuttingPatternString = "";
        try {
            // Serialize object to YAML
            cuttingPatternString = yamlMapper.writeValueAsString(cuttingPlan);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            System.exit(1);
        }
        return cuttingPatternString;
    }

    private ObjectMapper getObjectMapper() {
        YAMLFactory f = YAMLFactory.builder()
            .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
            .build();
        return new ObjectMapper(f);
    }

}
