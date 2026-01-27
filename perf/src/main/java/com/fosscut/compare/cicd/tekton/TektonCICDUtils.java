package com.fosscut.compare.cicd.tekton;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fosscut.compare.cicd.CICDReportLine;
import com.fosscut.shared.SharedDefaults;
import com.fosscut.utils.PerformanceDefaults;

import io.fabric8.kubernetes.api.model.TolerationBuilder;
import io.fabric8.tekton.client.DefaultTektonClient;
import io.fabric8.tekton.client.TektonClient;
import io.fabric8.tekton.v1.ParamBuilder;
import io.fabric8.tekton.v1.TaskRun;
import io.fabric8.tekton.v1.TaskRunBuilder;
import io.fabric8.tekton.v1.TaskRunStatus;

public class TektonCICDUtils {
    private TektonClient tkn;

    public TektonCICDUtils(String RUN_ID, int NUM_PARTS) {
        this.tkn = new DefaultTektonClient();
    }

    public TektonClient getTektonClient() {
        return tkn;
    }

    public List<CICDReportLine> prepareReportLines(List<String> identifiers) {
        List<CICDReportLine> reportLines = new ArrayList<>();

        for (String identifier : identifiers.reversed()) {
            String name = PerformanceDefaults.CICD_PERFORMANCE_TEKTON_TASK_RUN_NAME_PREFIX + identifier;
            TaskRun taskRun = tkn.v1().taskRuns()
                .inNamespace(SharedDefaults.TEKTON_NAMESPACE)
                .withName(name)
                .get();

            String creationTimestamp = null;
            String completionTimestamp = null;

            if (taskRun.getMetadata() != null) {
                creationTimestamp = taskRun.getMetadata().getCreationTimestamp();
            }

            if (taskRun.getStatus() != null) {
                TaskRunStatus status = taskRun.getStatus();
                completionTimestamp = status.getCompletionTime();
            }

            reportLines.add(new CICDReportLine(
                name,
                creationTimestamp == null ? null : Instant.parse(creationTimestamp),
                completionTimestamp == null ? null : Instant.parse(completionTimestamp)));
        }
        return reportLines;
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
                .withParams(
                    new ParamBuilder()
                    .withName("IDENTIFIER")
                    .withNewValue(identifier)
                    .build())
                // Toleration cannot be defined in Task definition, only in TaskRun
                .withNewPodTemplate()
                    .addToTolerations(
                        new TolerationBuilder()
                            .withKey("node-role.kubernetes.io/control-plane")
                            .withOperator("Exists")
                            .withEffect("NoSchedule")
                        .build()
                    )
                .endPodTemplate()
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
