package com.fosscut.api.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fosscut.api.type.TektonTaskRunLogsDTO;

import io.fabric8.kubernetes.api.model.SecretVolumeSourceBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.tekton.client.TektonClient;
import io.fabric8.tekton.v1.Param;
import io.fabric8.tekton.v1.ParamBuilder;
import io.fabric8.tekton.v1.TaskRun;
import io.fabric8.tekton.v1.TaskRunBuilder;
import io.fabric8.tekton.v1.WorkspaceBinding;
import io.fabric8.tekton.v1.WorkspaceBindingBuilder;

public class FosscutTektonClient {

    private static final String NAMESPACE = "fosscut";
    private static final String TASK_NAME = "fosscut-generate";
    private static final String TASK_RUN_NAME_PREFIX = "fosscut-generate-";
    private static final String STEP_NAME = "step-generate";
    private static final String REDIS_READ_URL = "redis://redis-replicas.redis.svc.cluster.local:6379/";

    @Autowired
    private TektonClient tkn;

    @Autowired
    private KubernetesClient k8s;

    public boolean taskRunExists(String identifier) {
        TaskRun taskRun = tkn.v1()
            .taskRuns()
            .inNamespace(NAMESPACE)
            .withName(TASK_RUN_NAME_PREFIX + identifier)
            .get();
        return taskRun != null;
    }

    public void createTaskRun(String identifier) {
        List<WorkspaceBinding> workspaces = new ArrayList<WorkspaceBinding>();
        workspaces.add(new WorkspaceBindingBuilder()
            .withName("keystore")
            .withSecret(new SecretVolumeSourceBuilder()
                .withSecretName("fosscut-keystore")
                .build()
            )
            .withSubPath("keystore.p12")
            .build()
        );

        workspaces.add(new WorkspaceBindingBuilder()
            .withName("truststore")
            .withSecret(new SecretVolumeSourceBuilder()
                .withSecretName("fosscut-truststore")
                .build()
            )
            .withSubPath("truststore.p12")
            .build()
        );

        workspaces.add(new WorkspaceBindingBuilder()
            .withName("redis-connection-secrets")
            .withSecret(new SecretVolumeSourceBuilder()
                .withSecretName("fosscut-cli-redis-connection-secrets")
                .build()
            )
            .withSubPath("redis-connection-secrets.yaml")
            .build()
        );

        List<Param> params = new ArrayList<Param>();
        params.add(new ParamBuilder()
            .withName("redisUrl")
            .withNewValue(REDIS_READ_URL + identifier)
            .build()
        );

        params.add(new ParamBuilder()
            .withName("subcommand")
            .withNewValue("cg")
            .build()
        );

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

        tkn.v1().taskRuns().inNamespace(NAMESPACE).resource(taskRun).create();
    }

    public void deleteTaskRun(String identifier) {
        tkn.v1().taskRuns()
            .inNamespace(NAMESPACE)
            .withName(TASK_RUN_NAME_PREFIX + identifier)
            .delete();
    }

    public TektonTaskRunLogsDTO getTaskRunLogs(String identifier) {
        TektonTaskRunLogsDTO taskRunLogsDTO = new TektonTaskRunLogsDTO();

        TaskRun taskRun = tkn.v1()
            .taskRuns()
            .inNamespace(NAMESPACE)
            .withName(TASK_RUN_NAME_PREFIX + identifier)
            .get();

        taskRunLogsDTO.setStatus(taskRun.getStatus().getConditions().get(0).getStatus());
        taskRunLogsDTO.setReason(taskRun.getStatus().getConditions().get(0).getReason());

        String podLogs = "";
        // Wait for pod creation before asking for logs
        if (!taskRunLogsDTO.getReason().equals("Pending")) {
            podLogs = k8s.pods()
                .inNamespace(NAMESPACE)
                .withName(taskRun.getStatus().getPodName())
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
