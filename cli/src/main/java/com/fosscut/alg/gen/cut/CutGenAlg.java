package com.fosscut.alg.gen.cut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fosscut.alg.gen.AbstractGenAlg;
import com.fosscut.exception.DuplicatesAreNotAllowedException;
import com.fosscut.exception.NotSupportedCutGenConfigException;
import com.fosscut.shared.type.cutting.order.Order;
import com.fosscut.shared.type.cutting.order.OrderInput;
import com.fosscut.shared.type.cutting.order.OrderOutput;
import com.fosscut.shared.type.cutting.plan.PlanInput;

/**
 * MLCSP order generator based on:
 * <a href="https://github.com/akavrt/cutgen/tree/master">github.com/akavrt/cutgen</a>.
 *
 * <p>Algorithm code inspired by an EJOR paper:</p>
 * <p>Gau, T., and Wascher, G., 1995, CUTGEN1 - a problem generator for the one-dimensional
 * cutting stock problem. European Journal of Operational Research, 84, 572-579.</p>
 */
public class CutGenAlg extends AbstractGenAlg {

    private boolean allowOutputTypeDuplicates;

    public CutGenAlg(
        Integer inputLength,
        Integer inputTypeCount,
        Integer minInputLength,
        Integer maxInputLength,
        boolean allowInputTypeDuplicates,
        int outputTypeCount,
        double outputLengthLowerBound,
        double outputLengthUpperBound,
        boolean allowOutputTypeDuplicates,
        int outputCount,
        Long seed
    ) {
        super(
            inputLength,
            inputTypeCount,
            minInputLength,
            maxInputLength,
            allowInputTypeDuplicates,
            outputTypeCount,
            outputLengthLowerBound,
            outputLengthUpperBound,
            outputCount,
            seed
        );
        this.allowOutputTypeDuplicates = allowOutputTypeDuplicates;
    }

    /**
     * <p>Generates an Order instance with predefined characteristics.</p>
     *
     * <p>As stated in Gau and Wascher (1995), the actual number of output
     * or input element types does not have to equal the desired values
     * specified in the generator parameters, because the possibility exists
     * that identical output or input lengths may be generated. By default
     * an error is thrown when that happens but it can be disabled with a flag.
     * Then demands of identical output and input lengths are summed up.</p>
     *
     * @return Order instance.
     */
    public Order nextOrder()
        throws NotSupportedCutGenConfigException, DuplicatesAreNotAllowedException
    {
        Order order = new Order();
        List<PlanInput> inputs = generateInputs();

        if (isSingleInputType()) {
            order = nextSingleInputOrder(inputs);
        } else if (isMultiInputType()) {
            order = nextMultiInputOrder(inputs);
        } else throw new NotSupportedCutGenConfigException("");

        return order;
    }

    private Order nextSingleInputOrder(List<PlanInput> inputs)
        throws DuplicatesAreNotAllowedException
    {
        int[] lengths = generateOutputLengths(
            outputTypeCount,
            outputLengthLowerBound,
            outputLengthUpperBound,
            inputs.get(0).getLength()
        );
        int[] demands = generateDemands(outputTypeCount);

        List<OrderOutput> outputs = merge(lengths, demands);
        List<OrderInput> orderInputs = new ArrayList<OrderInput>();
        orderInputs.add(new OrderInput(inputs.get(0).getLength()));

        return new Order(orderInputs, outputs);
    }

    private Order nextMultiInputOrder(List<PlanInput> inputs) throws DuplicatesAreNotAllowedException {
        Map<Integer, OrderOutput> outputCountMap = new HashMap<Integer, OrderOutput>();

        int finalInputTypeCount = inputs.size();

        if (!allowInputTypeDuplicates && finalInputTypeCount != inputTypeCount)
            throw new IllegalStateException("Number of generated inputs does not equal the desired one.");

        int[] outputCountPerInput = divideOrdersBetweenStockItems(finalInputTypeCount);

        List<OrderInput> resultInputs = new ArrayList<OrderInput>();
        for (int i = 0; i < finalInputTypeCount; i++) {
            int inputLength = inputs.get(i).getLength();

            int[] lengths = generateOutputLengths(
                outputCountPerInput[i],
                outputLengthLowerBound,
                outputLengthUpperBound,
                inputLength
            );

            int[] demands = generateDemands(outputCountPerInput[i]);
            List<OrderOutput> mergedOutputs = merge(lengths, demands);
            addOutputsToMap(outputCountMap, mergedOutputs);

            resultInputs.add(new OrderInput(inputLength));
        }

        List<OrderOutput> resultOutputs = new ArrayList<OrderOutput>(outputCountMap.values());
        return new Order(resultInputs, resultOutputs);
    }

    private void addOutputsToMap(
        Map<Integer, OrderOutput> outputCountMap, List<OrderOutput> mergedOutputs
    ) throws DuplicatesAreNotAllowedException {
        for (OrderOutput output : mergedOutputs) {
            if (!outputCountMap.containsKey(output.getLength())) {
                outputCountMap.put(output.getLength(), output);
            } else if (!allowOutputTypeDuplicates) {
                throw new DuplicatesAreNotAllowedException("output");
            } else {
                OrderOutput mapOutput = outputCountMap.get(output.getLength());
                mapOutput.setCount(mapOutput.getCount() + output.getCount());
            }
        }
    }

    private int[] generateOutputLengths(int typeCount, double lb, double ub, int il) {
        int[] result = new int[typeCount];

        for (int i = 0; i < result.length; i++) {
            result[i] = generateNewLength(lb, ub, il);
        }

        descendingSort(result);

        return result;
    }

    private int[] generateDemands(int typeCount) {
        int[] result = new int[typeCount];

        double sum = 0;
        double[] rands = new double[typeCount];
        for (int i = 0; i < result.length; i++) {
            rands[i] = randomGenerator.nextDouble();
            sum += rands[i];
        }

        // round up to ensure total generated demand >= outputCount
        int averageDemand = Math.ceilDiv(outputCount, outputTypeCount);
        int localTotalDemand = averageDemand * typeCount;
        int rest = localTotalDemand;
        for (int i = 0; i < result.length - 1; i++) {
            double demand = localTotalDemand * rands[i] / sum + 0.5;
            result[i] = Math.max(1, (int) demand);

            rest -= result[i];
        }

        result[result.length - 1] = Math.max(1, rest);

        return result;
    }

    /**
     * <p>Used to identify orders with identical lengths and throw an error
     * if they are generated or sum up the corresponding demands if enabled.</p>
     */
    private List<OrderOutput> merge(int[] lengths, int[] demands)
    throws DuplicatesAreNotAllowedException {
        List<OrderOutput> outputs = new ArrayList<OrderOutput>();

        for (int i = 0; i < lengths.length; i++) {
            if (i == lengths.length - 1 || lengths[i] != lengths[i + 1]) {
                outputs.add(new OrderOutput(lengths[i], demands[i]));
            } else if (!allowOutputTypeDuplicates) {
                throw new DuplicatesAreNotAllowedException("output");
            } else {
                demands[i + 1] += demands[i];
            }

        }

        return outputs;
    }

    /**
     * <p>Sorts the specified array of ints into descending numerical order.</p>
     *
     * @param a The array to be sorted.
     */
    public static void descendingSort(int[] a) {
        // sort array in ascending order
        Arrays.sort(a);

        // reverse order of elements in sorted array
        int left = 0;
        int right = a.length - 1;

        while (left < right) {
            // exchange the left and right elements
            int temp = a[left];
            a[left] = a[right];
            a[right] = temp;

            // move the bounds toward the center
            left++;
            right--;
        }
    }


    private int[] divideOrdersBetweenStockItems(int finalInputTypeCount) {
        int[] result = new int[finalInputTypeCount];

        int avrCount = outputTypeCount / finalInputTypeCount;
        Arrays.fill(result, avrCount);

        for(int i = 0; i < outputTypeCount % finalInputTypeCount; ++i)
        {
            int randomIndex = randomGenerator.nextInt(result.length);
            ++result[randomIndex];
        }

        return result;
    }

}
