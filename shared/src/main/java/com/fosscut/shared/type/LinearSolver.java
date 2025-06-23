package com.fosscut.shared.type;

/**
 * Javadoc of MPSolver.createSolver() method contains a list of supported
 * solvers. Only a compatible subset of open source solvers were chosen.
 *
 * Linear solvers are used only by column generation algorithm and need
 * to be able to provide dual variables.
 */
public enum LinearSolver {
    CLP,
    GLOP,
    PDLP
}
