import os
from datetime import datetime
from airflow import DAG
from airflow.operators.bash import BashOperator
from airflow.models import Param
from airflow.settings import AIRFLOW_HOME

default_args = {
    'owner': 'airflow',
    'depends_on_past': False,
    'email_on_failure': False,
    'email_on_retry': False,
    'retries': 0
}

parameterized_sleep_dag = DAG(
    'parameterized_sleep',
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

BashOperator(
    task_id = 'sleep_task',
    bash_command = 'echo "Going to sleep for {{ params.sleep_seconds }} seconds" && sleep {{ params.sleep_seconds }}',
    dag = parameterized_sleep_dag
)
