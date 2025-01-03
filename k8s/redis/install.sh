# password secret
kubectl delete secret redis-password-secret -n redis
kubectl create secret generic redis-password-secret -n redis --from-file=redis-password=../../helm/secrets/redis-password

# mTLS secrets
kubectl delete secret redis-tls-secret -n redis
kubectl create secret generic redis-tls-secret -n redis --from-file=tls.crt=../../helm/secrets/server.crt --from-file=tls.key=../../helm/secrets/server.key --from-file=ca.crt=../../helm/secrets/ca.crt

# redis
helm upgrade --install redis bitnami/redis -n redis --version 20.6.1 -f values.yaml
