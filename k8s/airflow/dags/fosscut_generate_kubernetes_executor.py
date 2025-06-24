import os
from datetime import datetime
from airflow import DAG
from airflow.operators.bash import BashOperator
from airflow.models import Param
from airflow.settings import AIRFLOW_HOME

from kubernetes.client import models as k8s

default_args = {
    'owner': 'airflow',
    'depends_on_past': False,
    'email_on_failure': False,
    'email_on_retry': False,
    'retries': 0
}

fosscut_generate_kubernetes_executor = DAG(
    'fosscut_generate_kubernetes_executor',
    default_args = default_args,
    description = 'A DAG that runs fosscut cutting plan generation',
    schedule_interval = None,  # Only triggered manually
    start_date = datetime(2024, 1, 1),
    catchup = False,
    params = {
        "subcommand": Param(
            default = "cg",
            enum = ["cg", "ffd", "greedy"],
            description = "Subcommand that determines cutting plan generation algorithm."
        ),
        "redis_url": Param(
            default = "redis://redis-replicas.redis.svc.cluster.local:6379/example-order",
            type = "string"
        ),
        "linear_solver" : Param(
            default = "GLOP",
            enum = ["CLP", "GLOP", "PDLP"],
            description = "Linear solver to use for cutting plan generation."
        ),
        "integer_solver": Param(
            default = "SCIP",
            enum = ["CBC", "SAT", "SCIP"],
            description = "Integer solver to use for cutting plan generation."
        ),
        "optimization_criterion": Param(
            default = "MIN_WASTE",
            enum = ["MIN_WASTE", "MIN_COST"],
            description = "Optimization criterion for cutting plan generation."
        ),
        "relax_cost": Param(
            default = "0.0",
            description = "Relax cost for cutting plan generation."
        ),
        "relax_enabled": Param(
            default = False,
            description = "Whether to enable relaxation for cutting plan generation."
        )
    }
)

secret_volumes = [
    k8s.V1Volume(
        name = 'redis-connection-secrets',
        secret = k8s.V1SecretVolumeSource(
            secret_name = 'cli-redis-connection-secrets'
        )
    )
]

volume_mounts = [
    k8s.V1VolumeMount(
        name='redis-connection-secrets',
        mount_path='/secrets/keystore.p12',
        sub_path='keystore.p12',
        read_only=True
    ),
    k8s.V1VolumeMount(
        name='redis-connection-secrets',
        mount_path='/secrets/truststore.p12',
        sub_path='truststore.p12',
        read_only=True
    ),
    k8s.V1VolumeMount(
        name='redis-connection-secrets',
        mount_path='/secrets/redis-connection-secrets.yaml',
        sub_path='redis-connection-secrets.yaml',
        read_only=True
    )
]

pod_override = k8s.V1Pod(
    spec = k8s.V1PodSpec(
        containers = [
            k8s.V1Container(
                name = "base",
                image = "karolstepanienko/fosscut-cli-native:0.0.1",
                volume_mounts=volume_mounts,
                # entrypoint borrowed from the original airflow/airflow Dockerfile
                command = [
                    "/usr/bin/dumb-init",
                    "--",
                    "/entrypoint"
                ]
            )
        ],
        volumes = secret_volumes
    )
)

BashOperator(
    task_id = 'fosscut_generate_kubernetes_executor_task_id',
    bash_command = """
        if [ {{ params.subcommand }} = 'cg' ]; then
            COMMAND_PARAMETERS="$COMMAND_PARAMETERS --linear-solver {{ params.linear_solver }}"
        fi

        if [ {{ params.subcommand }} = 'cg' ] || [ {{ params.subcommand }} = 'greedy' ]; then
            COMMAND_PARAMETERS="$COMMAND_PARAMETERS --integer-solver {{ params.integer_solver }}"
            COMMAND_PARAMETERS="$COMMAND_PARAMETERS --relaxation-cost {{ params.relax_cost }}"
        fi

        if [ {{ params.subcommand }} = 'ffd' ] && [ {{ params.relax_enabled }} = true ]; then
            COMMAND_PARAMETERS="$COMMAND_PARAMETERS --relaxation-enabled"
        fi

        fosscut --redis-connection-secrets /secrets/redis-connection-secrets.yaml {{ params.subcommand }} {{ params.redis_url}} --optimization-criterion {{ params.optimization_criterion }} $COMMAND_PARAMETERS
    """,
    dag = fosscut_generate_kubernetes_executor,
    executor_config = { "pod_override": pod_override }
)
