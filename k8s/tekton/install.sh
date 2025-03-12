#!/usr/bin/env bash

helm template tekton . -n fosscut-workloads -f values.yaml > template.log
helm upgrade --install tekton . -n fosscut-workloads --create-namespace -f values.yaml
