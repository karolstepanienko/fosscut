package com.fosscut.subcommand.abs;

import com.fosscut.shared.type.OptimizationCriterion;
import com.fosscut.util.Defaults;

import picocli.CommandLine.Option;

public abstract class AbstractAlg extends AbstractInputOutputFile {

    @Option(names = { "-i", "--integer-relaxation" },
        description = "Enforces integer constraints on relaxation values."
         + " By default relaxation values can be floating point numbers.")
    protected boolean forceIntegerRelax;

    @Option(names = { "--optimization-criterion"},
        defaultValue = Defaults.DEFAULT_PARAM_OPTIMIZATION_CRITERION,
        description = "One of: (${COMPLETION-CANDIDATES}).")
    protected OptimizationCriterion optimizationCriterion;

}
