#!/usr/bin/env bash
helm upgrade --install fosscut . -n fosscut -f local-values.yaml -f values.yaml --create-namespace
