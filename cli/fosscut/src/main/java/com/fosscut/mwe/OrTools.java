// Minimal example to call the GLOP solver.
// example inspired by: https://github.com/or-tools/java_or-tools/blob/main/src/main/java/org/or_tools/example/BasicExample.java
package com.fosscut.mwe;

import com.google.ortools.Loader;
import com.google.ortools.init.OrToolsVersion;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

public class OrTools {

    public static void main() {
        Loader.loadNativeLibraries();

        System.out.println("Google OR-Tools version: " + OrToolsVersion.getVersionString());

        // Create the linear solver with the GLOP backend.
        MPSolver solver = MPSolver.createSolver("GLOP");

        // 0.0 <= x <= 1.0
        MPVariable x = solver.makeNumVar(0.0, 1.0, "x");
        // 0.0 <= y <= 2.0
        MPVariable y = solver.makeNumVar(0.0, 2.0, "y");
        System.out.println("Number of variables = " + solver.numVariables());

        double infinity = java.lang.Double.POSITIVE_INFINITY;
        // Create a linear constraint, x + 2y <= 5.
        // https://developers.google.com/optimization/lp/lp_example#java_3
        MPConstraint constraint = solver.makeConstraint(-infinity, 5.0, "constraint");
        constraint.setCoefficient(x, 1);
        constraint.setCoefficient(y, 2);
        System.out.println("Number of constraints = " + solver.numConstraints());

        // f(x,y) = 3x + 2y
        MPObjective objective = solver.objective();
        objective.setCoefficient(x, 3);
        objective.setCoefficient(y, 2);
        objective.setMaximization();

        System.out.println("Solving with " + solver.solverVersion());
        final MPSolver.ResultStatus resultStatus = solver.solve();

        System.out.println("Status: " + resultStatus);
        if (resultStatus != MPSolver.ResultStatus.OPTIMAL) {
            System.out.println("The problem does not have an optimal solution!");
            if (resultStatus == MPSolver.ResultStatus.FEASIBLE) {
                System.out.println("A potentially suboptimal solution was found");
            } else {
                System.out.println("The solver could not solve the problem.");
                return;
            }
        }

        System.out.println("");
        System.out.println("Solution:");
        System.out.println("Objective value = " + objective.value());
        System.out.println("x = " + x.solutionValue());
        System.out.println("y = " + y.solutionValue());
        System.out.println("dual value = " + constraint.dualValue());
        System.out.println("Problem solved in " + solver.wallTime() + " milliseconds");
        System.out.println("Problem solved in " + solver.iterations() + " iterations");
    }

}
