#!/usr/bin/env bash

# PVC
kubectl apply -f persistence.yaml

# Jenkins HTTPS secrets
kubectl delete secret jenkins-keystore-secret -n jenkins
kubectl create secret generic jenkins-keystore-secret -n jenkins --from-file=jenkins-jks-file=../../helm/secrets/server.p12 --from-file=https-jks-password=../../helm/secrets/cert-key-password.txt

# fosscut CA cert
kubectl delete secret fosscut-ca -n jenkins
kubectl create secret generic fosscut-ca -n jenkins --from-file=tls.crt=../../helm/secrets/ca.crt

# Ingress secrets
kubectl delete secret jenkins.fosscut.com-tls-secret -n jenkins
kubectl create secret tls jenkins.fosscut.com-tls-secret -n jenkins --cert=../../helm/secrets/ingress.crt --key=../../helm/secrets/ingress.key

# Admin user secrets
kubectl delete secret jenkins-admin-user-secret -n jenkins
kubectl create secret generic jenkins-admin-user-secret -n jenkins --from-file=username=../../helm/secrets/jenkins-username --from-file=password=../../helm/secrets/jenkins-password

# jenkins
helm template jenkins/jenkins -n jenkins -f local-values.yaml -f values.yaml > template.log
helm upgrade --install jenkins jenkins/jenkins -n jenkins --create-namespace -f local-values.yaml -f values.yaml
