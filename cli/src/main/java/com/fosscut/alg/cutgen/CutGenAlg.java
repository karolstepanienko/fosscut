package com.fosscut.alg.cutgen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.fosscut.exception.DuplicatesAreNotAllowedException;
import com.fosscut.exception.NotSupportedCutGenConfigException;
import com.fosscut.shared.type.cutting.order.Order;
import com.fosscut.shared.type.cutting.order.OrderInput;
import com.fosscut.shared.type.cutting.order.OrderOutput;

/**
 * MLCSP order generator based on:
 * <a href="https://github.com/akavrt/cutgen/tree/master">github.com/akavrt/cutgen</a>.
 *
 * <p>Algorithm code inspired by an EJOR paper:</p>
 * <p>Gau, T., and Wascher, G., 1995, CUTGEN1 - a problem generator for the one-dimensional
 * cutting stock problem. European Journal of Operational Research, 84, 572-579.</p>
 */
public class CutGenAlg {

    private Long seed;
    private Random randomGenerator;

    private int outputTypeCount;
    private double outputLengthLowerBound;
    private double outputLengthUpperBound;
    private int averageOutputDemand;

    private Integer inputLength;
    private Integer inputTypeCount;
    private Integer inputLengthLowerBound;
    private Integer inputLengthUpperBound;

    private boolean allowOutputTypeDuplicates;
    private boolean allowInputTypeDuplicates;

    public CutGenAlg(
        int outputTypeCount,
        double outputLengthLowerBound,
        double outputLengthUpperBound,
        int averageOutputDemand,
        Integer inputLength,
        Integer inputTypeCount,
        Integer inputLengthLowerBound,
        Integer inputLengthUpperBound,
        Long seed,
        boolean allowOutputTypeDuplicates,
        boolean allowInputTypeDuplicates
    ) {
        this.outputTypeCount = outputTypeCount;
        this.outputLengthLowerBound = outputLengthLowerBound;
        this.outputLengthUpperBound = outputLengthUpperBound;
        this.averageOutputDemand = averageOutputDemand;
        this.inputLength = inputLength;
        this.inputTypeCount = inputTypeCount;
        this.inputLengthLowerBound = inputLengthLowerBound;
        this.inputLengthUpperBound = inputLengthUpperBound;
        this.seed = seed;
        this.allowOutputTypeDuplicates = allowOutputTypeDuplicates;
        this.allowInputTypeDuplicates = allowInputTypeDuplicates;

        if (this.seed == null) this.randomGenerator = new Random();
        else this.randomGenerator = new Random(this.seed);
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

        if (inputTypeCount == null && inputLength != null)
            order = nextSingleInputOrder();
        else if (inputTypeCount != null && inputLength == null
        && inputLengthLowerBound != null && inputLengthUpperBound != null)
            order = nextMultiInputOrder();
        else throw new NotSupportedCutGenConfigException("");

        return order;
    }

    private Order nextSingleInputOrder() throws DuplicatesAreNotAllowedException {
        int[] lengths = generateLengths(
            outputTypeCount,
            outputLengthLowerBound,
            outputLengthUpperBound,
            inputLength
        );
        int[] demands = generateDemands(outputTypeCount);

        List<OrderOutput> outputs = merge(lengths, demands);
        List<OrderInput> inputs = new ArrayList<OrderInput>();
        inputs.add(new OrderInput(inputLength));

        return new Order(inputs, outputs);
    }

    private Order nextMultiInputOrder() throws DuplicatesAreNotAllowedException {
        List<Integer> inputLengths = new ArrayList<Integer>();
        Map<Integer, OrderOutput> outputCountMap = new HashMap<Integer, OrderOutput>();

        int[] outputCountPerInput = divideOrdersBetweenStockItems();

        for (int i = 0; i < inputTypeCount; i++) {
            int inputLength = randomGenerator.nextInt(
                inputLengthLowerBound,
                inputLengthUpperBound + 1
            );

            if (inputLengths.contains(inputLength)) {
                if (!allowInputTypeDuplicates)
                    throw new DuplicatesAreNotAllowedException("input");
            } else inputLengths.add(inputLength);

            int[] lengths = generateLengths(
                outputCountPerInput[i],
                outputLengthLowerBound,
                outputLengthUpperBound,
                inputLength
            );

            int[] demands = generateDemands(outputCountPerInput[i]);
            List<OrderOutput> mergedOutputs = merge(lengths, demands);
            addOutputsToMap(outputCountMap, mergedOutputs);
        }

        List<OrderInput> resultInputs = new ArrayList<OrderInput>();
        for (Integer inputLength : inputLengths) {
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

    private int[] generateLengths(int typeCount, double lb, double ub, int il) {
        int[] result = new int[typeCount];

        for (int i = 0; i < result.length; i++) {
            double rValue = randomGenerator.nextDouble();
            double length = (lb + (ub - lb) * rValue) * il + rValue;

            result[i] = (int) length;
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

        int totalDemand = averageOutputDemand * typeCount;
        int rest = totalDemand;
        for (int i = 0; i < result.length - 1; i++) {
            double demand = totalDemand * rands[i] / sum + 0.5;
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


    private int[] divideOrdersBetweenStockItems() {
        int[] result = new int[inputTypeCount];

        int avrCount = outputTypeCount / inputTypeCount;
        Arrays.fill(result, avrCount);

        for(int i = 0; i < outputTypeCount % inputTypeCount; ++i)
        {
            int randomIndex = randomGenerator.nextInt(result.length);
            ++result[randomIndex];
        }

        return result;
    }

}
