package com.fosscut.alg.cg;

import java.util.ArrayList;
import java.util.List;

import com.fosscut.type.Input;
import com.fosscut.type.Order;
import com.fosscut.type.Output;

class Parameters {
    // pattern definition, number of output elements for [input][pattern][output]
    private List<List<List<Integer>>> nipo;
    // relaxation value for [input][pattern][output]
    private List<List<List<Integer>>> ripo;
    private int nPattern;
    private int nPatternMax;

    Parameters(Order order) {
        this.nipo = initParameterMatrix(order);
        this.ripo = initParameterMatrix(order);
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

    public List<List<List<Integer>>> getRipo() {
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

    List<List<List<Integer>>> initParameterMatrix(Order order) {
        // without ArrayList it will be created as a static unmodifiable List
        List<List<List<Integer>>> inputs = new ArrayList<>();
        for (int i = 0; i < order.getInputs().size(); i++) {
            List<List<Integer>> patterns = new ArrayList<>();
            for (int p = 0; p < order.getOutputs().size(); p++) {
                List<Integer> outputs = new ArrayList<>();
                for (int o = 0; o < order.getOutputs().size(); o++) {
                    outputs.add(0);
                }
                patterns.add(outputs);
            }
            inputs.add(patterns);
        }
        return inputs;
    }

    public void initPatterns(Order order) {
        for (int ni = 0; ni < order.getInputs().size(); ni++) {
            Input input = order.getInputs().get(ni);
            setNPattern(0);
            for (int no = 0; no < order.getOutputs().size(); no++) {
                Output output = order.getOutputs().get(no);
                getNipo().get(ni).get(getNPattern()).set(no, Math.floorDiv(input.getLength(), output.getLength()));

                incrementNPattern();
                if (getNPattern() > getNPatternMax()) setNPatternMax(getNPattern());
            }
        }
    }
}
