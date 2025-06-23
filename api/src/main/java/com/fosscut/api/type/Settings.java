package com.fosscut.api.type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fosscut.shared.type.IntegerSolver;
import com.fosscut.shared.type.LinearSolver;
import com.fosscut.shared.type.OptimizationCriterion;

public class Settings {

    private Algorithm algorithm = Algorithm.FFD;
    private LinearSolver linearSolver = LinearSolver.GLOP;
    private IntegerSolver integerSolver = IntegerSolver.SCIP;
    private double relaxCost = 0.0;
    private boolean relaxEnabled = false;
    private OptimizationCriterion optimizationCriterion = OptimizationCriterion.MIN_WASTE;

    private String identifier;

    public Settings() {}

    public Settings(String settingsString) throws JsonProcessingException, JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Settings settings = objectMapper.readValue(settingsString, Settings.class);
        this.algorithm = settings.getAlgorithm();
        this.linearSolver = settings.getLinearSolver();
        this.integerSolver = settings.getIntegerSolver();
        this.relaxCost = settings.getRelaxCost();
        this.relaxEnabled = settings.isRelaxEnabled();
        this.optimizationCriterion = settings.getOptimizationCriterion();
    }

    public static Settings getSettingsSafe(String settingsString, String identifier) {
        try {
            Settings settings = new Settings(settingsString);
            settings.setIdentifier(identifier);
            return settings;
        } catch (JsonProcessingException e) {
            // Log the error or handle it as needed
            e.printStackTrace();
        }
        return null;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public LinearSolver getLinearSolver() {
        return linearSolver;
    }

    public void setLinearSolver(LinearSolver linearSolver) {
        this.linearSolver = linearSolver;
    }

    public IntegerSolver getIntegerSolver() {
        return integerSolver;
    }

    public void setIntegerSolver(IntegerSolver integerSolver) {
        this.integerSolver = integerSolver;
    }

    public double getRelaxCost() {
        return relaxCost;
    }

    public void setRelaxCost(double relaxCost) {
        this.relaxCost = relaxCost;
    }

    public boolean isRelaxEnabled() {
        return relaxEnabled;
    }

    public void setRelaxEnabled(boolean relaxEnabled) {
        this.relaxEnabled = relaxEnabled;
    }

    public OptimizationCriterion getOptimizationCriterion() {
        return optimizationCriterion;
    }

    public void setOptimizationCriterion(OptimizationCriterion optimizationCriterion) {
        this.optimizationCriterion = optimizationCriterion;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String toJenkinsParamsString(String redisReadHost, String redisReadPort) {
        return "SUBCOMMAND=" + algorithm.toString().toLowerCase() +
               "&REDIS_URL=redis://" + redisReadHost + ":" + redisReadPort + "/" + identifier +
               "&LINEAR_SOLVER=" + linearSolver.toString() +
               "&INTEGER_SOLVER=" + integerSolver.toString() +
               "&RELAX_COST=" + relaxCost +
               "&RELAX_ENABLED=" + relaxEnabled +
               "&OPTIMIZATION_CRITERION=" + optimizationCriterion.toString();
    }

    public String toAirflowParamsString(String redisReadHost, String redisReadPort) {
        return "\"conf\": {" +
            "\"subcommand\": \"" + algorithm.toString().toLowerCase() + "\"," +
            "\"redis_url\": \"redis://" + redisReadHost + ":" + redisReadPort + "/" + identifier + "\"" +
            ",\"linear_solver\": \"" + linearSolver.toString() + "\"" +
            ",\"integer_solver\": \"" + integerSolver.toString() + "\"" +
            ",\"relax_cost\": " + relaxCost +
            ",\"relax_enabled\": " + relaxEnabled +
            ",\"optimization_criterion\": \"" + optimizationCriterion.toString() + "\"" +
        "}";
    }

    @Override
    public String toString() {
        return "Settings{" +
                "algorithm=" + algorithm +
                ", integerSolver=" + integerSolver +
                ", linearSolver=" + linearSolver +
                ", relaxCost=" + relaxCost +
                ", relaxEnabled=" + relaxEnabled +
                '}';
    }

}
