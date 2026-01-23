#!/usr/bin/env bash

kubectl create namespace prometheus

# Ingress secrets
kubectl delete secret prometheus.fosscut.com-tls-secret -n prometheus
kubectl create secret tls prometheus.fosscut.com-tls-secret -n prometheus --cert=../../helm/secrets/ingress.crt --key=../../helm/secrets/ingress.key

# prometheus
helm template prometheus prometheus-community/prometheus -n prometheus -f local-values.yaml -f values.yaml > template.log
helm upgrade --install prometheus prometheus-community/prometheus -n prometheus --version 28.6.0 -f local-values.yaml -f values.yaml
