package com.fosscut.subcommand.abs;

import com.fosscut.shared.type.OptimizationCriterion;
import com.fosscut.type.RelaxationSpreadStrategy;
import com.fosscut.util.Defaults;
import com.fosscut.util.Messages;

import picocli.CommandLine.Option;

public abstract class AbstractAlg extends AbstractInputOutputFile {

    @Option(names = { "-r", "--relaxation-enabled" },
        defaultValue = "false",
        description = "Enables relaxation mechanism.")
    protected boolean relaxEnabled;

    @Option(names = { "--relaxation-spread-strategy" },
        defaultValue = Defaults.DEFAULT_PARAM_RELAX_SPREAD_STRATEGY,
        description = Messages.RELAXATION_SPREAD_STRAT_DESCRIPTION)
    protected RelaxationSpreadStrategy relaxationSpreadStrategy;

    @Option(names = { "--optimization-criterion"},
        defaultValue = Defaults.DEFAULT_PARAM_OPTIMIZATION_CRITERION,
        description = "One of: (${COMPLETION-CANDIDATES})."
            + " Default: ${DEFAULT-VALUE}.")
    protected OptimizationCriterion optimizationCriterion;

    @Option(names = { "-d", "--disable-time-measurement-metadata" },
        defaultValue = "false",
        description = "Disables adding time measurement metadata to the"
            + " generated cutting plan.")
    protected boolean disableTimeMeasurementMetadata;

    public static boolean isRelaxationEnabled(boolean relaxEnabled, Double relaxCost) {
        return relaxEnabled && relaxCost != null && relaxCost >= 0;
    }

}
