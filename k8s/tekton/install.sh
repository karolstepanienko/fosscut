#!/usr/bin/env bash

helm template tekton . -n tekton -f values.yaml > template.log
helm upgrade --install tekton . -n tekton --create-namespace -f values.yaml
