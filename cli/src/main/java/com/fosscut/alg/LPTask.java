package com.fosscut.alg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.exception.LPUnfeasibleException;
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

    protected void printSolution(ResultStatus resultStatus) throws LPUnfeasibleException {
        logger.info("Solved with " + getSolver().solverVersion());

        logger.info("Status: " + resultStatus);
        if (resultStatus != ResultStatus.OPTIMAL) {
            if (resultStatus == ResultStatus.FEASIBLE) {
                logger.warn("A potentially suboptimal solution was found.");
            } else {
                throw new LPUnfeasibleException("");
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
