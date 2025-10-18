package com.fosscut.cg;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fosscut.utils.CloudCommand;

public class CgCPUTest {

    @Test public void cgCPU2() throws InterruptedException {
        CloudCommand cc = new CloudCommand("CgCPUTest", "cgCPU2",
            "cutgen -i 1000 -ol 0.3 -ou 0.5 -ot 10 -d 100",
            "cg --linear-solver GLOP --integer-solver SCIP",
            "1", "5Gi",
            true
        );
        cc.run(Map.of(1, 3, 2, 5));
    }

}
