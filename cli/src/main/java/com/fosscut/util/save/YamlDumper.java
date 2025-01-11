package com.fosscut.util.save;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fosscut.alg.cg.ColumnGeneration;
import com.fosscut.exception.NotIntegerLPTaskException;
import com.fosscut.type.cutting.plan.CuttingPlan;

public class YamlDumper {

    private static final Logger logger = LoggerFactory.getLogger(YamlDumper.class);

    public String dump(ColumnGeneration columnGeneration) {
        YAMLFactory f = YAMLFactory.builder()
            .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
            .build();
        ObjectMapper yamlMapper = new ObjectMapper(f);

        CuttingPlan cuttingPlan = new CuttingPlan();
        try {
            cuttingPlan = columnGeneration.getCuttingPlan();
        } catch (NotIntegerLPTaskException e) {
            logger.error(e.getMessage());
            System.exit(1);
        }

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

}
