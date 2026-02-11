package com.fosscut.opt;

import java.util.LinkedList;

import com.fosscut.AbstractTest;

// TODO add plots here
public class CgOptimal10itPlot extends AbstractTest {

    // Testing 150 output types to generate more diverse orders
    protected static String orderCommand = "optimalgen -iu 1000 -il 500 -it 10 -ol 0.4 -ou 0.8 -ot 150 -oc 10000 --timeout-amount 10 --timeout-unit SECONDS";
    protected static String xAxisLabel = "x150";

    // 100 orders grouped in groups of 4 seeds for easier triggering
    protected static int N_GROUP_SIZE = 4;
    protected static LinkedList<Integer> seedsA = LinkedList_of(1, 2, 4, 5); // 4 seeds, 4 in total
    protected static LinkedList<Integer> seedsB = LinkedList_of(6, 8, 9, 11); // 4 seeds, 8 in total
    protected static LinkedList<Integer> seedsC = LinkedList_of(12, 13, 14, 15); // 4 seeds, 12 in total
    protected static LinkedList<Integer> seedsD = LinkedList_of(17, 18, 20, 22); // 4 seeds, 16 in total
    protected static LinkedList<Integer> seedsE = LinkedList_of(23, 24, 25, 26); // 4 seeds, 20 in total
    protected static LinkedList<Integer> seedsF = LinkedList_of(28, 29, 30, 31); // 4 seeds, 24 in total
    protected static LinkedList<Integer> seedsG = LinkedList_of(34, 35, 36, 37); // 4 seeds, 28 in total
    protected static LinkedList<Integer> seedsH = LinkedList_of(38, 39, 40, 41); // 4 seeds, 32 in total
    protected static LinkedList<Integer> seedsI = LinkedList_of(43, 44, 45, 46); // 4 seeds, 36 in total
    protected static LinkedList<Integer> seedsJ = LinkedList_of(47, 48, 49, 50); // 4 seeds, 40 in total
    protected static LinkedList<Integer> seedsK = LinkedList_of(51, 52, 53, 54); // 4 seeds, 44 in total
    protected static LinkedList<Integer> seedsL = LinkedList_of(55, 57, 58, 59); // 4 seeds, 48 in total
    protected static LinkedList<Integer> seedsM = LinkedList_of(60, 61, 62, 63); // 4 seeds, 52 in total
    protected static LinkedList<Integer> seedsN = LinkedList_of(64, 66, 67, 70); // 4 seeds, 56 in total
    protected static LinkedList<Integer> seedsO = LinkedList_of(72, 73, 75, 76); // 4 seeds, 60 in total
    protected static LinkedList<Integer> seedsP = LinkedList_of(78, 79, 81, 82); // 4 seeds, 64 in total
    protected static LinkedList<Integer> seedsQ = LinkedList_of(83, 84, 86, 87); // 4 seeds, 68 in total
    protected static LinkedList<Integer> seedsR = LinkedList_of(88, 91, 92, 93); // 4 seeds, 72 in total
    protected static LinkedList<Integer> seedsS = LinkedList_of(94, 95, 96, 97); // 4 seeds, 76 in total
    protected static LinkedList<Integer> seedsT = LinkedList_of(98, 99, 100, 102); // 4 seeds, 80 in total
    protected static LinkedList<Integer> seedsU = LinkedList_of(104, 105, 106, 108); // 4 seeds, 84 in total
    protected static LinkedList<Integer> seedsV = LinkedList_of(109, 110, 111, 112); // 4 seeds, 88 in total
    protected static LinkedList<Integer> seedsW = LinkedList_of(113, 115, 116, 117); // 4 seeds, 92 in total
    protected static LinkedList<Integer> seedsX = LinkedList_of(118, 119, 121, 124); // 4 seeds, 96 in total
    protected static LinkedList<Integer> seedsY = LinkedList_of(125, 126, 127, 128); // 4 seeds, 100 in total

    protected static LinkedList<Integer> getAllSeeds() {
        LinkedList<Integer> allSeeds = new LinkedList<>();
        allSeeds.addAll(seedsA);
        allSeeds.addAll(seedsB);
        allSeeds.addAll(seedsC);
        allSeeds.addAll(seedsD);
        allSeeds.addAll(seedsE);
        allSeeds.addAll(seedsF);
        allSeeds.addAll(seedsG);
        allSeeds.addAll(seedsH);
        allSeeds.addAll(seedsI);
        allSeeds.addAll(seedsJ);
        allSeeds.addAll(seedsK);
        allSeeds.addAll(seedsL);
        allSeeds.addAll(seedsM);
        allSeeds.addAll(seedsN);
        allSeeds.addAll(seedsO);
        allSeeds.addAll(seedsP);
        allSeeds.addAll(seedsQ);
        allSeeds.addAll(seedsR);
        allSeeds.addAll(seedsS);
        allSeeds.addAll(seedsT);
        allSeeds.addAll(seedsU);
        allSeeds.addAll(seedsV);
        allSeeds.addAll(seedsW);
        allSeeds.addAll(seedsX);
        allSeeds.addAll(seedsY);
        return allSeeds;
    }

}
