import os
import shutil
from airflow import DAG
from airflow.operators.python import PythonOperator
from airflow.utils.dates import days_ago
from airflow.models import DagRun
from airflow.settings import Session
from airflow.configuration import conf

LOG_BASE = conf.get("logging", "base_log_folder")

def cleanup_orphaned_logs(dry_run=True):
    session = Session()

    # 1. Build set of valid (dag_id, dag_run_id)
    valid_runs = {
        (dr.dag_id, dr.dag_run_id)
        for dr in session.query(DagRun.dag_id, DagRun.dag_run_id).all()
    }
    session.close()

    removed = []

    # 2. Walk log directory
    for dag_id in os.listdir(LOG_BASE):
        dag_path = os.path.join(LOG_BASE, dag_id)
        if not os.path.isdir(dag_path):
            continue

        for dag_run_id in os.listdir(dag_path):
            run_path = os.path.join(dag_path, dag_run_id)

            if not os.path.isdir(run_path):
                continue

            if (dag_id, dag_run_id) not in valid_runs:
                if dry_run:
                    print(f"[DRY-RUN] Would remove {run_path}")
                else:
                    shutil.rmtree(run_path)
                    print(f"[REMOVED] {run_path}")
                    removed.append(run_path)

    return removed

with DAG(
    dag_id="maintenance.cleanup_orphaned_logs",
    start_date=days_ago(1),
    schedule_interval="@daily",
    catchup=False,
    max_active_runs=1,
) as dag:
    cleanup = PythonOperator(
        task_id="cleanup_logs",
        python_callable=cleanup_orphaned_logs,
        op_kwargs={"dry_run": True},
    )
