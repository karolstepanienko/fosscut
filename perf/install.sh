# Secret for performance workload pods
kubectl delete secret cli-redis-connection-secrets -n performance
kubectl create secret generic cli-redis-connection-secrets \
    -n performance \
    --from-file=keystore.p12=../helm/secrets/keystore.p12 \
    --from-file=truststore.p12=../helm/secrets/truststore.p12 \
    --from-file=redis-connection-secrets.yaml=../helm/secrets/redis-connection-secrets.yaml
