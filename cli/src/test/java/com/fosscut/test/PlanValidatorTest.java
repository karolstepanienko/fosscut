package com.fosscut.test;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fosscut.exception.PlanValidationException;
import com.fosscut.shared.type.cutting.order.OrderOutput;
import com.fosscut.type.cutting.plan.CuttingPlan;
import com.fosscut.type.cutting.plan.Pattern;
import com.fosscut.type.cutting.plan.PlanInput;
import com.fosscut.type.cutting.plan.PlanOutputDouble;
import com.fosscut.util.Messages;
import com.fosscut.util.PlanValidator;
import com.fosscut.util.TestDefaults;

public class PlanValidatorTest {

    private static CuttingPlan loadPlanFromFile(String path) throws IOException {
        File planFile = new File(path);
        String planString = Files.readString(Paths.get(planFile.getPath()));
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        return yamlMapper.readValue(planString, CuttingPlan.class);
    }

    @Test
    public void testValidatePlan() throws IOException {
        CuttingPlan plan = loadPlanFromFile(TestDefaults.PLAN_FAIL_PATTERN_TO_LONG);

        PlanValidator validator = new PlanValidator();

        // Act
        PlanValidationException exception = assertThrows(PlanValidationException.class, () -> {
            validator.validatePlan(plan);
        });
        assertTrue(exception.getMessage().contains(Messages.PLAN_PATTERN_DOES_NOT_FIT_IN_INPUT));
        assertTrue(exception.getMessage().contains(
            "inputLength: 100\n"
            + "pattern:\n"
            + "  count: 4\n"
            + "  patternDefinition:\n"
            + "  - id: 0\n"
            + "    count: 6\n"
        ));
    }

    @Test
    public void testValidatePlanRelax() {
        PlanOutputDouble planOutputDouble = new PlanOutputDouble(0, 5, 0.95);
        Pattern pattern = new Pattern();
        pattern.setCount(1);
        pattern.setPatternDefinition(List.of(planOutputDouble));
        PlanInput input = new PlanInput();
        input.setLength(100);
        input.setPatterns(List.of(pattern));
        List<PlanInput> inputs = List.of(input);
        OrderOutput output = new OrderOutput();
        output.setLength(21);
        output.setCount(5);
        output.setMaxRelax(1);
        List<OrderOutput> outputs = List.of(output);
        CuttingPlan plan = new CuttingPlan(inputs, outputs);

        PlanValidator validator = new PlanValidator();

        // Act
        PlanValidationException exception = assertThrows(PlanValidationException.class, () -> {
            validator.validatePlan(plan);
        });
        assertTrue(exception.getMessage().contains(Messages.PLAN_PATTERN_DOES_NOT_FIT_IN_INPUT));
        assertTrue(exception.getMessage().contains(
            "inputLength: 100\n"
            + "pattern:\n"
            + "  count: 1\n"
            + "  patternDefinition:\n"
            + "  - id: 0\n"
            + "    count: 5\n"
            + "    relax: 0.95\n"
        ));
    }

    @Test
    public void testValidatePlanDemand() throws IOException {
        CuttingPlan plan = loadPlanFromFile(TestDefaults.PLAN_FAIL_DEMAND_NOT_SATISFIED);

        PlanValidator validator = new PlanValidator();

        // Act
        PlanValidationException exception = assertThrows(PlanValidationException.class, () -> {
            validator.validatePlan(plan);
        });
        assertTrue(exception.getMessage().contains(Messages.PLAN_DEMAND_NOT_SATISFIED));
        assertTrue(exception.getMessage().contains(
            "Expected output count: 21, actual output count: 20.\n"
            + "outputId: 0\n"
            + "output:\n"
            + "  length: 20\n"
            + "  count: 21\n"
            + "  maxRelax: 0\n"
        ));
    }

}
