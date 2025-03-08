#!/usr/bin/env bash

# static webserver secret key ensures that Airflow components only restart when
# necessary
kubectl delete secret airflow-webserver-secret-key -n airflow
kubectl create secret generic airflow-webserver-secret-key -n airflow --from-literal="webserver-secret-key=$(python3 -c 'import secrets; print(secrets.token_hex(16))')"

# HTTPS secrets
kubectl delete secret airflow-webserver-tls-secret -n airflow
kubectl create secret generic airflow-webserver-tls-secret -n airflow --from-file=tls.crt=../../helm/secrets/server.crt --from-file=tls.key=../../helm/secrets/server.key

# fosscut CA cert
kubectl delete secret fosscut-ca -n airflow
kubectl create secret generic fosscut-ca -n airflow --from-file=tls.crt=../../helm/secrets/ca.crt

# Ingress secrets
kubectl delete secret airflow.fosscut.com-tls-secret -n airflow
kubectl create secret tls airflow.fosscut.com-tls-secret -n airflow --cert=../../helm/secrets/ingress.crt --key=../../helm/secrets/ingress.key

# airflow
helm upgrade --install airflow apache-airflow/airflow --namespace airflow --create-namespace -f local-values.yaml -f values.yaml
