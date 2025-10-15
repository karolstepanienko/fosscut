package com.fosscut.cg;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fosscut.utils.CloudCommand;

public class CgCPUTest {

    @Test public void cgCPU2() throws InterruptedException {
        CloudCommand cc = new CloudCommand("cgCPU2",
            "cutgen -i 500000 -ol 0.2 -ou 0.6 -ot 100 -d 100",
            "cg --linear-solver GLOP --integer-solver SCIP",
            "1", "5Gi",
            true
        );
        cc.run(Map.of(1, 1, 2, 2));
    }

}
