package com.fosscut.alg.cg;

import com.fosscut.type.cutting.order.Order;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPSolver.ResultStatus;


/*
 * Linear programming task. Encapsulates all necessary fields for linear
 * programming tasks in column generation algorithm.
 */
abstract class LPTask {
    private Order order;
    private MPSolver solver;
    private MPObjective objective;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

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
        System.out.println("Solved with " + getSolver().solverVersion());

        System.out.println("Status: " + resultStatus);
        if (resultStatus != ResultStatus.OPTIMAL) {
            System.err.println("The problem does not have an optimal solution!");
            if (resultStatus == ResultStatus.FEASIBLE) {
                System.err.println("A potentially suboptimal solution was found");
            } else {
                System.err.println("The solver could not solve the problem.");
                return;
            }
        }

        System.out.println("Number of variables = " + getSolver().numVariables());
        System.out.println("Number of constraints = " + getSolver().numConstraints());
        System.out.println("Solution:");
        System.out.println("Objective value = " + getObjective().value());
        System.out.println("Problem solved in " + getSolver().wallTime() + " milliseconds");
        System.out.println("Problem solved in " + getSolver().iterations() + " iterations");
    }
}
