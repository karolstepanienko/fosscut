#!/usr/bin/env bash
helm uninstall airflow -n airflow

kubectl delete secret airflow-webserver-secret-key -n airflow
kubectl delete secret airflow-webserver-tls-secret -n airflow
kubectl delete secret fosscut-ca -n airflow
kubectl delete secret airflow.fosscut.com-tls-secret -n airflow
kubectl delete secret cli-redis-connection-secrets -n airflow
