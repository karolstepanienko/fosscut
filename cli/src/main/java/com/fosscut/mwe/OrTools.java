// Minimal example to call the GLOP solver.
// example inspired by: https://github.com/or-tools/java_or-tools/blob/main/src/main/java/org/or_tools/example/BasicExample.java
package com.fosscut.mwe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.ortools.Loader;
import com.google.ortools.init.OrToolsVersion;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

public class OrTools {

    private static final Logger logger = LoggerFactory.getLogger(OrTools.class);

    public static void main() {
        Loader.loadNativeLibraries();

        logger.info("Google OR-Tools version: " + OrToolsVersion.getVersionString());

        // Create the linear solver with the GLOP backend.
        MPSolver solver = MPSolver.createSolver("GLOP");

        // 0.0 <= x <= 10.0
        MPVariable x = solver.makeIntVar(0.0, 10.0, "x");
        // 0.0 <= y <= 20.0
        MPVariable y = solver.makeIntVar(0.0, 20.0, "y");
        logger.info("Number of variables = " + solver.numVariables());

        double infinity = java.lang.Double.POSITIVE_INFINITY;
        // Create a linear constraint, x + 2y <= 5.
        // https://developers.google.com/optimization/lp/lp_example#java_3
        MPConstraint constraint = solver.makeConstraint(-infinity, 5.0, "constraint");
        constraint.setCoefficient(x, 1);
        constraint.setCoefficient(y, 2);
        logger.info("Number of constraints = " + solver.numConstraints());

        // f(x,y) = 3x + 2y
        MPObjective objective = solver.objective();
        objective.setCoefficient(x, 3);
        objective.setCoefficient(y, 10.5);
        objective.setMaximization();

        logger.info("Solving with " + solver.solverVersion());
        final MPSolver.ResultStatus resultStatus = solver.solve();

        logger.info("Status: " + resultStatus);
        if (resultStatus != MPSolver.ResultStatus.OPTIMAL) {
            logger.info("The problem does not have an optimal solution!");
            if (resultStatus == MPSolver.ResultStatus.FEASIBLE) {
                logger.info("A potentially suboptimal solution was found");
            } else {
                logger.info("The solver could not solve the problem.");
                return;
            }
        }

        logger.info("");
        logger.info("Solution:");
        logger.info("Objective value = " + objective.value());
        logger.info("x = " + x.solutionValue());
        logger.info("y = " + y.solutionValue());
        logger.info("dual value = " + constraint.dualValue());
        logger.info("Problem solved in " + solver.wallTime() + " milliseconds");
        logger.info("Problem solved in " + solver.iterations() + " iterations");
    }

}
