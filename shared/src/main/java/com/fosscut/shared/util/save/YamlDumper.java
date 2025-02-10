package com.fosscut.shared.util.save;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;

public class YamlDumper {

    private static final Logger logger = LoggerFactory.getLogger(YamlDumper.class);

    public String dump(Object object) {
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
