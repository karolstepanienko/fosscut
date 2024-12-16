package com.fosscut.api.controller;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.tekton.client.DefaultTektonClient;
import io.fabric8.tekton.client.TektonClient;
import io.fabric8.tekton.v1beta1.TaskRun;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class Tekton {

    @GetMapping("/tekton/run")
    @ResponseBody
    public String runTekton() {
        String namespace = "fosscut";
        String podLogs = "";
        TektonClient tkn = new DefaultTektonClient();
        KubernetesClient k8sClient = new KubernetesClientBuilder().build();

        TaskRun taskRun = tkn.v1beta1()
            .taskRuns()
            .inNamespace(namespace)
            .withName("hello-task-run")
            .get();

        String podName = taskRun.getMetadata().getName() + "-pod"; // Adjust if needed
        System.out.println(podName);
        String logs = k8sClient.pods().inNamespace(namespace).withName(podName).inContainer("step-echo").getLog();
        System.out.println(logs);

        podLogs = podName + "\n" + logs;
        tkn.close();

        return podLogs;
    }

}
