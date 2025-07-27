#!/usr/bin/env bash
kubectl create namespace fosscut
helm template fosscut . -n fosscut -f local-values.yaml -f values.yaml > template.log
helm upgrade --install fosscut . -n fosscut -f local-values.yaml -f values.yaml --create-namespace
