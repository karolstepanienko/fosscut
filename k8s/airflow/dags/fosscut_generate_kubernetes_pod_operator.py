import os
from datetime import datetime
from airflow import DAG
from airflow.models import Param
from kubernetes.client import models as k8s
from airflow.providers.cncf.kubernetes.operators.pod import KubernetesPodOperator

default_args = {
    'owner': 'airflow',
    'depends_on_past': False,
    'email_on_failure': False,
    'email_on_retry': False,
    'retries': 0
}

fosscut_generate_kubernetes_pod_operator_dag = DAG(
    'fosscut_generate_kubernetes_pod_operator_dag',
    default_args = default_args,
    description = 'A DAG that runs fosscut cutting plan generation',
    schedule_interval = None,  # Only triggered manually
    start_date = datetime(2025, 1, 1),
    catchup = False,
    params = {
        "subcommand": Param(
            default = "cg",
            enum = ["cg", "ffd", "greedy"],
            description = "Subcommand that determines cutting plan generation algorithm."
        ),
        "redis_url": Param(
            default = "",
            type = "string"
        )
    }
)

secret_volumes = [
    k8s.V1Volume(
        name = 'keystore',
        secret = k8s.V1SecretVolumeSource(
            secret_name = 'fosscut-keystore'
        )
    ),
    k8s.V1Volume(
        name = 'truststore',
        secret = k8s.V1SecretVolumeSource(
            secret_name = 'fosscut-truststore'
        )
    ),
    k8s.V1Volume(
        name = 'redis-connection-secrets',
        secret = k8s.V1SecretVolumeSource(
            secret_name = 'fosscut-cli-redis-connection-secrets'
        )
    )
]

volume_mounts = [
    k8s.V1VolumeMount(
        name='keystore',
        mount_path='/keystore.p12',
        sub_path='keystore.p12',
        read_only=True
    ),
    k8s.V1VolumeMount(
        name='truststore',
        mount_path='/truststore.p12',
        sub_path='truststore.p12',
        read_only=True
    ),
    k8s.V1VolumeMount(
        name='redis-connection-secrets',
        mount_path='/redis-connection-secrets.yaml',
        sub_path='redis-connection-secrets.yaml',
        read_only=True
    )
]

KubernetesPodOperator(
    name = "generate_task",
    image = "karolstepanienko/fosscut-cli-native:0.0.1",
    dag = fosscut_generate_kubernetes_pod_operator_dag,
    cmds = ["bash", "-cx"],
    arguments = ["fosscut --redis-connection-secrets /redis-connection-secrets.yaml {{ params.subcommand }} {{ params.redis_url}}"],
    task_id = "fosscut_generate_kubernetes_pod_operator_task_id",
    namespace = "fosscut-workloads",
    volumes = secret_volumes,
    volume_mounts = volume_mounts,
)
