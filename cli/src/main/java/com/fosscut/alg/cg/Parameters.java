package com.fosscut.alg.cg;

import java.util.ArrayList;
import java.util.List;

import com.fosscut.type.cutting.order.Order;
import com.fosscut.type.cutting.order.OrderInput;
import com.fosscut.type.cutting.order.OrderOutput;

public class Parameters {
    // pattern definition, number/count of output elements for [input][pattern][output]
    private List<List<List<Integer>>> nipo;
    // relaxation value for [input][pattern][output]
    private List<List<List<Double>>> ripo;
    private int nPattern;
    private int nPatternMax;

    Parameters(Order order) {
        this.nipo = initParameterMatrix(order, Integer.class, 0);
        this.ripo = initParameterMatrix(order, Double.class, 0.0);
        this.nPattern = 0;
        this.nPatternMax = 0;
        initPatterns(order);
    }

    public List<List<List<Integer>>> getNipo() {
        return nipo;
    }

    public void setNipo(List<List<List<Integer>>> nipo) {
        this.nipo = nipo;
    }

    public List<List<List<Double>>> getRipo() {
        return ripo;
    }

    public int getNPattern() {
        return this.nPattern;
    }

    public void setNPattern(int nPattern) {
        this.nPattern = nPattern;
    }

    public void incrementNPattern() {
        this.nPattern++;
    }

    public int getNPatternMax() {
        return nPatternMax;
    }

    public void setNPatternMax(int nPatternMax) {
        this.nPatternMax = nPatternMax;
    }

    public void incrementNPatternMax() {
        this.nPatternMax++;
    }

    private <T> List<List<List<T>>> initParameterMatrix(Order order, Class<T> type, T zero) {
        // without ArrayList it will be created as a static unmodifiable List
        List<List<List<T>>> inputs = new ArrayList<>();
        for (int i = 0; i < order.getInputs().size(); i++) {
            List<List<T>> patterns = new ArrayList<>();
            for (int p = 0; p < order.getOutputs().size(); p++) {
                List<T> outputs = new ArrayList<>();
                for (int o = 0; o < order.getOutputs().size(); o++) {
                    outputs.add(zero);
                }
                patterns.add(outputs);
            }
            inputs.add(patterns);
        }
        return inputs;
    }

    public void initPatterns(Order order) {
        for (int i = 0; i < order.getInputs().size(); i++) {
            OrderInput input = order.getInputs().get(i);
            setNPattern(0);
            for (int o = 0; o < order.getOutputs().size(); o++) {
                OrderOutput output = order.getOutputs().get(o);
                getNipo().get(i).get(getNPattern()).set(o, Math.floorDiv(input.getLength(), output.getLength()));

                incrementNPattern();
                if (getNPattern() > getNPatternMax()) setNPatternMax(getNPattern());
            }
        }
    }
}
