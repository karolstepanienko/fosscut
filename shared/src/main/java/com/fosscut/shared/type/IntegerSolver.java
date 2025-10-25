package com.fosscut.shared.type;

/**
 * Javadoc of MPSolver.createSolver() method contains a list of supported
 * solvers. Only a compatible subset of open source solvers were chosen.
 */
public enum IntegerSolver {
    CBC, // single-threaded
    SAT, // multi-threaded
    SCIP // multi-threaded, non-deterministic when using more than one thread
}
