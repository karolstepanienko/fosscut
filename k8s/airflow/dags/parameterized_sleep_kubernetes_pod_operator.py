import os
from datetime import datetime
from airflow import DAG
from airflow.models import Param
from airflow.providers.cncf.kubernetes.operators.pod import KubernetesPodOperator

default_args = {
    'owner': 'airflow',
    'depends_on_past': False,
    'email_on_failure': False,
    'email_on_retry': False,
    'retries': 0
}

parameterized_sleep_kubernetes_pod_operator_dag = DAG(
    'parameterized_sleep_kubernetes_pod_operator_dag',
    default_args = default_args,
    description = 'A DAG that sleeps for a parameterized duration',
    schedule_interval = None,  # Only triggered manually
    start_date = datetime(2024, 1, 1),
    catchup = False,
    params = {
        "sleep_seconds": Param(
            default=10,
            type="integer",
            minimum=1,
            maximum=3600,  # Max 1 hour
            description="Number of seconds to sleep"
        )
    }
)

KubernetesPodOperator(
    name="sleep_task",
    image="karolstepanienko/fosscut-cli-native:0.0.1",
    dag = parameterized_sleep_kubernetes_pod_operator_dag,
    cmds=["bash", "-cx"],
    arguments=["/fosscut && sleep {{ params.sleep_seconds }}"],
    task_id="sleep_task_kubernetes_pod_operator"
)
