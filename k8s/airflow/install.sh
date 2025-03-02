#!/usr/bin/env bash

# static webserver secret key ensures that Airflow components only restart when
# necessary
kubectl delete secret airflow-webserver-secret-key -n airflow
kubectl create secret generic airflow-webserver-secret-key -n airflow --from-literal="webserver-secret-key=$(python3 -c 'import secrets; print(secrets.token_hex(16))')"

# HTTPS secrets
kubectl delete secret airflow-webserver-tls-secret -n airflow
kubectl create secret generic airflow-webserver-tls-secret -n airflow --from-file=tls.crt=../../helm/secrets/server.crt --from-file=tls.key=../../helm/secrets/server.key

# airflow
helm upgrade --install airflow apache-airflow/airflow --namespace airflow --create-namespace -f local-values.yaml -f values.yaml

# Forward Airflow UI to local machine
kubectl port-forward svc/airflow-webserver 6080:8080 --namespace airflow
