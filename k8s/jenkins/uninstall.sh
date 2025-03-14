#!/usr/bin/env bash
helm uninstall jenkins -n jenkins

kubectl delete secret jenkins-keystore-secret -n jenkins
kubectl delete secret fosscut-ca -n jenkins
kubectl delete secret jenkins.fosscut.com-tls-secret -n jenkins
kubectl delete secret jenkins-admin-user-secret -n jenkins
kubectl delete secret cli-redis-connection-secrets -n jenkins
