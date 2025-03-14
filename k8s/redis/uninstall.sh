#!/usr/bin/env bash
helm uninstall redis -n redis

kubectl delete secret redis-password-secret -n redis
kubectl delete secret redis-tls-secret -n redis
