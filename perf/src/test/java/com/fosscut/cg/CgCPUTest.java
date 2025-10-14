package com.fosscut.cg;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.fosscut.utils.CloudCommand;

public class CgCPUTest {

    @Test public void cgTest() throws InterruptedException {
        CloudCommand cc = new CloudCommand("cgTest", 
            "echo 'Starting CG test'; sleep 5; echo 'Order command done.'",
            "echo 'Starting generator'; sleep 5; echo 'Generator done.'",
            "2", "5Gi",
            false
        );
        cc.run(List.of("1", "2", "3", "4", "5", "6"));
    }

}
