package com.fosscut.compare.gen;

import com.fosscut.utils.CloudCommand;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.junit.jupiter.api.Assertions.assertTrue;


@Execution(ExecutionMode.CONCURRENT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OptimalgenCompare extends GenComparePlot {

    private static String testName = "optimalgenCompare";
    private static String orderCommand = "optimalgen -il 100 -iu 1000 -it 5 -ol 0.4 -ou 0.8 -oc 1000 -ot 50 --timeout-amount 10 --timeout-unit SECONDS";

    /********************************* Tests **********************************/

    @Test @Order(1) public void optimalgenComparex1() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, "x1", orderCommand, planCommand);
        assertTrue(cmd.run(LinkedList_of(seeds.get(0)), N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void optimalgenComparex2() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, "x2", orderCommand, planCommand);
        assertTrue(cmd.run(LinkedList_of(seeds.get(1)), N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void optimalgenComparex3() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, "x3", orderCommand, planCommand);
        assertTrue(cmd.run(LinkedList_of(seeds.get(2)), N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void optimalgenComparex4() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, "x4", orderCommand, planCommand);
        assertTrue(cmd.run(LinkedList_of(seeds.get(3)), N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void optimalgenComparex5() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, "x5", orderCommand, planCommand);
        assertTrue(cmd.run(LinkedList_of(seeds.get(4)), N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void optimalgenComparex6() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, "x6", orderCommand, planCommand);
        assertTrue(cmd.run(LinkedList_of(seeds.get(5)), N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void optimalgenComparex7() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, "x7", orderCommand, planCommand);
        assertTrue(cmd.run(LinkedList_of(seeds.get(6)), N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void optimalgenComparex8() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, "x8", orderCommand, planCommand);
        assertTrue(cmd.run(LinkedList_of(seeds.get(7)), N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void optimalgenComparex9() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, "x9", orderCommand, planCommand);
        assertTrue(cmd.run(LinkedList_of(seeds.get(8)), N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void optimalgenComparex10() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, "x10", orderCommand, planCommand);
        assertTrue(cmd.run(LinkedList_of(seeds.get(9)), N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

}
