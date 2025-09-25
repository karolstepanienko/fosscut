package com.fosscut.alg.cg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fosscut.shared.type.cutting.order.Order;
import com.fosscut.shared.type.cutting.order.OrderInput;
import com.fosscut.shared.type.cutting.order.OrderOutput;

public class Parameters {
    // pattern definition, number/count of output elements for [input][pattern][output]
    private List<List<List<Integer>>> nipo;
    // relaxation value for [input][pattern][output]
    private List<List<List<Integer>>> ripo;
    private Map<Integer, Integer> numberOfPatternsPerInput;

    Parameters(Order order) {
        this.nipo = initParameterMatrix(order, Integer.class, 0);
        this.ripo = initParameterMatrix(order, Integer.class, 0);
        this.numberOfPatternsPerInput = new HashMap<>();
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

    public Integer getNumberOfPatternsPerInput(int inputId) {
        return this.numberOfPatternsPerInput.get(inputId);
    }

    public void incrementNumberOfPatternsPerInput(int inputId) {
        this.numberOfPatternsPerInput.put(inputId, this.numberOfPatternsPerInput.get(inputId) + 1);
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
            numberOfPatternsPerInput.put(i, 0);
            for (int o = 0; o < order.getOutputs().size(); o++) {
                OrderOutput output = order.getOutputs().get(o);
                // initial patterns should contain as many outputs as possible
                // but not more than required by the order since that could
                // result in plans with too much waste for small orders
                getNipo().get(i).get(getNumberOfPatternsPerInput(i))
                    .set(o,
                        Math.min(
                            output.getCount(),
                            Math.floorDiv(input.getLength(), output.getLength())
                        )
                    );

                incrementNumberOfPatternsPerInput(i);
            }
        }
    }
}
