### Generate final k8s config
helm template --debug . -n fosscut -f values.yaml > template.log
