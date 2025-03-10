#!/usr/bin/env bash
helm template fosscut . -n fosscut -f local-values.yaml -f values.yaml > template.log
helm upgrade --install fosscut . -n fosscut -f local-values.yaml -f values.yaml --create-namespace
