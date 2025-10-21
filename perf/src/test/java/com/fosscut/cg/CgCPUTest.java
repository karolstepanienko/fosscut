package com.fosscut.cg;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.fosscut.AbstractTest;
import com.fosscut.utils.CloudCommand;

public class CgCPUTest extends AbstractTest {

    @Test public void cgCPUx2() throws InterruptedException {
        CloudCommand cc = new CloudCommand("CgCPUTest", "cgCPUx2",
            "cutgen -i 1000 -ol 0.3 -ou 0.5 -ot 10 -d 100",
            "cg --linear-solver GLOP --integer-solver SCIP",
            "1", "5Gi",
            true
        );
        assertTrue(cc.run(LinkedHashMap_of(1, 3, 2, 5)));
    }

}
