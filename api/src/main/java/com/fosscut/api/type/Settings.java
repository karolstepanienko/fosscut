package com.fosscut.api.type;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fosscut.shared.type.IntegerSolver;
import com.fosscut.shared.type.LinearSolver;
import com.fosscut.shared.type.OptimizationCriterion;

import io.fabric8.tekton.v1.Param;
import io.fabric8.tekton.v1.ParamBuilder;

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

    public List<Param> toTektonParameters(String redisReadHost, String redisReadPort) {
        List<Param> params = new ArrayList<>();
        params.add(new ParamBuilder().withName("subcommand").withNewValue(algorithm.toString().toLowerCase()).build());
        params.add(new ParamBuilder().withName("redisUrl").withNewValue("redis://" + redisReadHost + ":" + redisReadPort + "/" + identifier).build());
        params.add(new ParamBuilder().withName("linearSolver").withNewValue(linearSolver.toString()).build());
        params.add(new ParamBuilder().withName("integerSolver").withNewValue(integerSolver.toString()).build());
        params.add(new ParamBuilder().withName("relaxCost").withNewValue(String.valueOf(relaxCost)).build());
        params.add(new ParamBuilder().withName("relaxEnabled").withNewValue(String.valueOf(relaxEnabled)).build());
        params.add(new ParamBuilder().withName("optimizationCriterion").withNewValue(optimizationCriterion.toString()).build());
        return params;
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
