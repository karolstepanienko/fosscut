package com.fosscut.util.save;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fosscut.alg.cg.ColumnGeneration;
import com.fosscut.alg.ffd.FirstFitDecreasing;
import com.fosscut.alg.greedy.GreedyAlg;
import com.fosscut.exception.NotIntegerLPTaskException;
import com.fosscut.type.cutting.order.Order;
import com.fosscut.type.cutting.plan.CuttingPlan;

public class YamlDumper {

    private static final Logger logger = LoggerFactory.getLogger(YamlDumper.class);

    public String dump(FirstFitDecreasing firstFitDecreasing) {
        return serialize(firstFitDecreasing.getCuttingPlan());
    }

    public String dump(GreedyAlg greedy) {
        return serialize(greedy.getCuttingPlan());
    }

    public String dump(ColumnGeneration columnGeneration) {
        CuttingPlan cuttingPlan = null;
        try {
            cuttingPlan = columnGeneration.getCuttingPlan();
        } catch (NotIntegerLPTaskException e) {
            logger.error(e.getMessage());
            System.exit(1);
        }
        return serialize(cuttingPlan);
    }

    public String dump(Order order) {
        return serialize(order);
    }

    public String dump(CuttingPlan cuttingPlan) {
        return serialize(cuttingPlan);
    }

    private String serialize(Object object) {
        ObjectMapper yamlMapper = getObjectMapper();
        yamlMapper.setSerializationInclusion(Include.NON_NULL);

        String cuttingPatternString = "";
        try {
            // Serialize object to YAML
            cuttingPatternString = yamlMapper.writeValueAsString(object);
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
