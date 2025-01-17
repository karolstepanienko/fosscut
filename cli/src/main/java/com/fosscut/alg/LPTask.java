package com.fosscut.alg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPSolver.ResultStatus;

/*
 * Linear programming task. Encapsulates all necessary fields for linear
 * programming tasks in column generation algorithm.
 */
public abstract class LPTask {

    private static final Logger logger = LoggerFactory.getLogger(LPTask.class);

    private MPSolver solver;
    private MPObjective objective;

    public MPSolver getSolver() {
        return solver;
    }

    public void setSolver(MPSolver solver) {
        this.solver = solver;
    }

    public MPObjective getObjective() {
        return objective;
    }

    public void setObjective(MPObjective objective) {
        this.objective = objective;
    }

    protected void printSolution(ResultStatus resultStatus) {
        logger.info("Solved with " + getSolver().solverVersion());

        logger.info("Status: " + resultStatus);
        if (resultStatus != ResultStatus.OPTIMAL) {
            logger.error("The problem does not have an optimal solution!");
            if (resultStatus == ResultStatus.FEASIBLE) {
                logger.error("A potentially suboptimal solution was found");
            } else {
                logger.error("The solver could not solve the problem.");
                return;
            }
        }

        logger.info("Number of variables = " + getSolver().numVariables());
        logger.info("Number of constraints = " + getSolver().numConstraints());
        logger.info("Solution:");
        logger.info("Objective value = " + getObjective().value());
        logger.info("Problem solved in " + getSolver().wallTime() + " milliseconds");
        logger.info("Problem solved in " + getSolver().iterations() + " iterations");
    }

}
