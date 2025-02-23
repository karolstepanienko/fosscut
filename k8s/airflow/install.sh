#!/usr/bin/env bash

# airflow
helm upgrade --install airflow apache-airflow/airflow --namespace airflow --create-namespace -f local-values.yaml -f values.yaml

# Forward Airflow UI to local machine
kubectl port-forward svc/airflow-webserver 6080:8080 --namespace airflow
