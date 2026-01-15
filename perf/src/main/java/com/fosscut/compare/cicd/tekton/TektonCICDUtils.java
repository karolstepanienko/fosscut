package com.fosscut.compare.cicd.tekton;

import java.util.ArrayList;
import java.util.List;

import com.fosscut.shared.SharedDefaults;
import com.fosscut.utils.PerformanceDefaults;

import io.fabric8.tekton.client.DefaultTektonClient;
import io.fabric8.tekton.client.TektonClient;
import io.fabric8.tekton.v1.TaskRun;
import io.fabric8.tekton.v1.TaskRunBuilder;

public class TektonCICDUtils {
    private TektonClient tkn;
    private String RUN_ID;
    private int NUM_PARTS;

    public TektonCICDUtils(String RUN_ID, int NUM_PARTS) {
        this.tkn = new DefaultTektonClient();
        this.RUN_ID = RUN_ID;
        this.NUM_PARTS = NUM_PARTS;
    }

    public TektonClient getTektonClient() {
        return tkn;
    }

    public List<String> generateIdentifiers() {
        List<String> identifiers = new ArrayList<>();
        for (int part = 0; part < NUM_PARTS; part++) {
            identifiers.add("-run-" + RUN_ID + "-part-" + part);
        }
        return identifiers;
    }

    public void createTaskRun(String identifier) {
        TaskRun taskRun = new TaskRunBuilder()
            .withNewMetadata()
                .withName(PerformanceDefaults.CICD_PERFORMANCE_TEKTON_TASK_RUN_NAME_PREFIX + identifier)
            .endMetadata()
            .withNewSpec()
                .withNewTaskRef()
                    .withName(PerformanceDefaults.CICD_PERFORMANCE_TEKTON_TASK_RUN_NAME)
                .endTaskRef()
            .endSpec()
            .build();

        tkn.v1().taskRuns()
            .inNamespace(SharedDefaults.TEKTON_NAMESPACE)
            .resource(taskRun).create();
    }

    public void deleteTaskRun(String identifier) {
        tkn.v1().taskRuns()
            .inNamespace(SharedDefaults.TEKTON_NAMESPACE)
            .withName(PerformanceDefaults.CICD_PERFORMANCE_TEKTON_TASK_RUN_NAME_PREFIX + identifier)
            .delete();
    }

    public void close() {
        tkn.close();
    }

}
