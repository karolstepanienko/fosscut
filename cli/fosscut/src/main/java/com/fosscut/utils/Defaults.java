package com.fosscut.utils;

public final class Defaults {
    public static final String LINEAR_SOLVER = "GLOP";
    public static final String INTEGER_SOLVER = "SCIP";

    public static final double ENABLED_RELAXATION_COST = 0;
    // cannot be infinity because solvers get unstable
    public static final double DISABLED_RELAXATION_COST = 1000000000;
}
