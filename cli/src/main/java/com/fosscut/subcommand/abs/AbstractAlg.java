package com.fosscut.subcommand.abs;

import com.fosscut.shared.type.OptimizationCriterion;
import com.fosscut.util.Defaults;

import picocli.CommandLine.Option;

public abstract class AbstractAlg extends AbstractInputOutputFile {

    @Option(names = { "-r", "--relaxation-enabled" },
        defaultValue = "false",
        description = "Enables relaxation mechanism.")
    protected boolean relaxEnabled;

    @Option(names = { "-i", "--integer-relaxation" },
        description = "Enforces integer constraints on relaxation values."
         + " By default relaxation values can be floating point numbers.")
    protected boolean forceIntegerRelax;

    @Option(names = { "--optimization-criterion"},
        defaultValue = Defaults.DEFAULT_PARAM_OPTIMIZATION_CRITERION,
        description = "One of: (${COMPLETION-CANDIDATES})."
            + " Default: ${DEFAULT-VALUE}.")
    protected OptimizationCriterion optimizationCriterion;

    public static boolean isRelaxationEnabled(boolean relaxEnabled, Double relaxCost) {
        return relaxEnabled && relaxCost != null && relaxCost >= 0;
    }

}
