package com.fosscut.subcommand;

import com.fosscut.alg.gen.optimal.OptimalGenAlg;
import com.fosscut.shared.exception.FosscutException;
import com.fosscut.shared.util.save.YamlDumper;
import com.fosscut.subcommand.abs.AbstractGen;
import com.fosscut.type.OutputFormat;
import com.fosscut.type.cutting.plan.CuttingPlan;
import com.fosscut.util.PlanValidator;
import com.fosscut.util.PrintResult;
import com.fosscut.util.PropertiesVersionProvider;
import com.fosscut.util.save.Save;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "optimalgen", versionProvider = PropertiesVersionProvider.class)
public class OptimalGen extends AbstractGen {

    @Option(names = { "-oc", "--output-count" },
        description = "Minimal number of outputs in the order."
            + " Actual number might be higher due to patterns"
            + " generating multiple outputs.",
        required = true)
    private int outputCount;

    @Override
    protected void runWithExceptions() throws FosscutException {
        OptimalGenAlg optimalGenAlg = new OptimalGenAlg(
            inputLength,
            inputTypeCount,
            minInputLength,
            maxInputLength,
            allowInputTypeDuplicates,
            outputCount,
            outputTypeCount,
            outputLengthLowerBound,
            outputLengthUpperBound,
            seed);
        CuttingPlan orderWithCuttingPlan = optimalGenAlg.nextOrder();

        String orderString = null;
        if (outputFormat == OutputFormat.yaml) {
            YamlDumper yamlDumper = new YamlDumper();
            orderString = yamlDumper.dump(orderWithCuttingPlan);
        }

        Save save = new Save(orderString);
        save.save(outputFile);

        PrintResult printResult = new PrintResult("order", outputFile);
        printResult.print(orderString);

        PlanValidator planValidator = new PlanValidator();
        planValidator.validatePlan(orderWithCuttingPlan);
    }

}
