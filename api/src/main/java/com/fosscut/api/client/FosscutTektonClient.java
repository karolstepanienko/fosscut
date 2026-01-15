package com.fosscut.api.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fosscut.api.type.Settings;
import com.fosscut.api.type.TektonTaskRunLogsDTO;
import com.fosscut.shared.SharedDefaults;

import io.fabric8.knative.pkg.apis.Condition;
import io.fabric8.kubernetes.api.model.SecretVolumeSourceBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.tekton.client.TektonClient;
import io.fabric8.tekton.v1.Param;
import io.fabric8.tekton.v1.TaskRun;
import io.fabric8.tekton.v1.TaskRunBuilder;
import io.fabric8.tekton.v1.TaskRunStatus;
import io.fabric8.tekton.v1.WorkspaceBinding;
import io.fabric8.tekton.v1.WorkspaceBindingBuilder;

public class FosscutTektonClient extends AbstractClient {

    private static final String TASK_NAME = "fosscut-generate";
    private static final String TASK_RUN_NAME_PREFIX = "fosscut-generate-";
    private static final String SECRET_NAME = "tekton-cli-redis-connection-secrets";
    private static final String STEP_NAME = "step-generate";

    @Autowired
    private TektonClient tkn;

    @Autowired
    private KubernetesClient k8s;

    public boolean taskRunExists(String identifier) {
        TaskRun taskRun = tkn.v1()
            .taskRuns()
            .inNamespace(SharedDefaults.TEKTON_NAMESPACE)
            .withName(TASK_RUN_NAME_PREFIX + identifier)
            .get();
        return taskRun != null;
    }

    public void createTaskRun(String identifier, Settings settings) {
        List<WorkspaceBinding> workspaces = new ArrayList<WorkspaceBinding>();
        workspaces.add(new WorkspaceBindingBuilder()
            .withName("keystore")
            .withSecret(new SecretVolumeSourceBuilder()
                .withSecretName(SECRET_NAME)
                .build()
            )
            .withSubPath("keystore.p12")
            .build()
        );

        workspaces.add(new WorkspaceBindingBuilder()
            .withName("truststore")
            .withSecret(new SecretVolumeSourceBuilder()
                .withSecretName(SECRET_NAME)
                .build()
            )
            .withSubPath("truststore.p12")
            .build()
        );

        workspaces.add(new WorkspaceBindingBuilder()
            .withName("redis-connection-secrets")
            .withSecret(new SecretVolumeSourceBuilder()
                .withSecretName(SECRET_NAME)
                .build()
            )
            .withSubPath("redis-connection-secrets.yaml")
            .build()
        );

        List<Param> params = settings.toTektonParameters(redisReadHost, redisReadPort);

        TaskRun taskRun = new TaskRunBuilder()
            .withNewMetadata()
                .withName(TASK_RUN_NAME_PREFIX + identifier)
            .endMetadata()
            .withNewSpec()
                .withNewTaskRef().withName(TASK_NAME).endTaskRef()
                .withWorkspaces(workspaces)
                .withParams(params)
            .endSpec()
        .build();

        tkn.v1().taskRuns().inNamespace(SharedDefaults.TEKTON_NAMESPACE).resource(taskRun).create();
    }

    public void deleteTaskRun(String identifier) {
        tkn.v1().taskRuns()
            .inNamespace(SharedDefaults.TEKTON_NAMESPACE)
            .withName(TASK_RUN_NAME_PREFIX + identifier)
            .delete();
    }

    public TektonTaskRunLogsDTO getTaskRunLogs(String identifier) {
        TektonTaskRunLogsDTO taskRunLogsDTO = new TektonTaskRunLogsDTO();

        TaskRun taskRun = tkn.v1()
            .taskRuns()
            .inNamespace(SharedDefaults.TEKTON_NAMESPACE)
            .withName(TASK_RUN_NAME_PREFIX + identifier)
            .get();

        TaskRunStatus status = taskRun.getStatus();
        if (status == null) {
            taskRunLogsDTO.setStatus("Unknown");
            taskRunLogsDTO.setReason("No status available");
            taskRunLogsDTO.setLogs("No logs available");
            return taskRunLogsDTO;
        }
        Condition condition = status.getConditions().get(0);

        taskRunLogsDTO.setStatus(condition.getStatus());
        taskRunLogsDTO.setReason(condition.getReason());

        String podLogs = "";
        // Wait for pod creation before asking for logs
        if (!taskRunLogsDTO.getReason().equals("Pending")) {
            podLogs = k8s.pods()
                .inNamespace(SharedDefaults.TEKTON_NAMESPACE)
                .withName(status.getPodName())
                .inContainer(STEP_NAME)
                .getLog();
        }
        taskRunLogsDTO.setLogs(podLogs);

        return taskRunLogsDTO;
    }

    public void close() {
        tkn.close();
        k8s.close();
    }

}
