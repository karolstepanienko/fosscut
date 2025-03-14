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
helm template airflow apache-airflow/airflow -n airflow -f local-values.yaml -f values.yaml > template.log
helm upgrade --install airflow apache-airflow/airflow -n airflow --create-namespace -f local-values.yaml -f values.yaml

# Secret for fosscut workload pods
kubectl delete secret cli-redis-connection-secrets -n airflow
kubectl create secret generic cli-redis-connection-secrets \
    -n airflow \
    --from-file=keystore.p12=../../helm/secrets/keystore.p12 \
    --from-file=truststore.p12=../../helm/secrets/truststore.p12 \
    --from-file=redis-connection-secrets.yaml=../../helm/secrets/redis-connection-secrets.yaml
