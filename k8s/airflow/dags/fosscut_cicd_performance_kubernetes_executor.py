import os
from datetime import datetime
from airflow import DAG
from airflow.models import Param
from airflow.operators.bash import BashOperator

from kubernetes.client import models as k8s

default_args = {
    'owner': 'airflow',
    'depends_on_past': False,
    'email_on_failure': False,
    'email_on_retry': False,
    'retries': 0
}

fosscut_cicd_performance_kubernetes_executor = DAG(
    'fosscut_cicd_performance_kubernetes_executor',
    default_args = default_args,
    description = 'A DAG used in CICD Performance testing. Creates only one pod.',
    schedule_interval = None,  # Only triggered manually
    start_date = datetime(2024, 1, 1),
    catchup = False,
    params = {
        "IDENTIFIER": Param(
            default = "",
            type = "string",
            description = "Identifier of the CICD performance test."
        )
    }
)


pod_override = k8s.V1Pod(
    spec = k8s.V1PodSpec(
        containers = [
            k8s.V1Container(
                name = "base",
                image = "karolstepanienko/fosscut-cli-native:0.0.1",
                # entrypoint borrowed from the original airflow/airflow Dockerfile
                command = [
                    "/usr/bin/dumb-init",
                    "--",
                    "/entrypoint"
                ],
                # disable migration check to speed up pod startup
                # and decrease CPU usage on k8s nodes where pods are scheduled
                env = [
                    k8s.V1EnvVar(
                        name = "AIRFLOW__DATABASE__CHECK_MIGRATIONS",
                        value = "False"
                    )
                ]
            )
        ],
        tolerations = [
            k8s.V1Toleration(
                key="node-role.kubernetes.io/control-plane",
                operator="Exists",
                effect="NoSchedule"
            )
        ]
    )
)

BashOperator(
    task_id = 'fosscut_cicd_performance_kubernetes_executor_task_id',
    bash_command = """
        #!/bin/sh
        echo "CICD performance test. {{ params.IDENTIFIER }}"
    """,
    dag = fosscut_cicd_performance_kubernetes_executor,
    executor_config = { "pod_override": pod_override }
)
