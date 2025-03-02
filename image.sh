#!/usr/bin/env bash

# Fail on first failed command and print commands before executing them
set -e -x

# Check if an argument is provided
if [ $# -eq 0 ]; then
    echo "Usage: $0 {api|cli|gui}"
    exit 1
fi

# Execute commands based on the argument
case "$1" in
    api)
        echo "Building API docker image..."
        # amd64 only build
        docker build -t karolstepanienko/fosscut-api-jar:0.0.1 --progress plain -f api/Dockerfile-jar .
        docker save karolstepanienko/fosscut-api-jar:0.0.1 > /img/fosscut-api-jar.tar
        # Upload the image to cluster nodes
        scp /img/fosscut-api-jar.tar arch-gamma:/img/fosscut-api-jar.tar
        ssh arch-gamma "ctr -n k8s.io image import /img/fosscut-api-jar.tar"
        scp /img/fosscut-api-jar.tar arch-beta:/img/fosscut-api-jar.tar
        ssh arch-beta "ctr -n k8s.io image import /img/fosscut-api-jar.tar"
        ;;
    cli)
        echo "Building CLI docker image..."
        # amd64 only build
        docker build -t karolstepanienko/fosscut-cli-native:0.0.1 --progress plain -f cli/Dockerfile-native .
        docker save karolstepanienko/fosscut-cli-native:0.0.1 > /img/fosscut-cli-native.tar
        # Upload the image to cluster nodes
        scp /img/fosscut-cli-native.tar arch-gamma:/img/fosscut-cli-native.tar
        ssh arch-gamma "ctr -n k8s.io image import /img/fosscut-cli-native.tar"
        scp /img/fosscut-cli-native.tar arch-beta:/img/fosscut-cli-native.tar
        ssh arch-beta "ctr -n k8s.io image import /img/fosscut-cli-native.tar"
        ;;
    gui)
        echo "Building GUI docker image..."
        # amd64 only build
        docker build -t karolstepanienko/fosscut-gui-native:0.0.1 --progress plain -f gui/Dockerfile-native .
        docker save karolstepanienko/fosscut-gui-native:0.0.1 > /img/fosscut-gui-native.tar
        # Upload the image to cluster nodes
        scp /img/fosscut-gui-native.tar arch-gamma:/img/fosscut-gui-native.tar
        ssh arch-gamma "ctr -n k8s.io image import /img/fosscut-gui-native.tar"
        scp /img/fosscut-gui-native.tar arch-beta:/img/fosscut-gui-native.tar
        ssh arch-beta "ctr -n k8s.io image import /img/fosscut-gui-native.tar"
        ;;
    *)
        echo "Invalid argument: $1"
        echo "Usage: $0 {api|cli|gui}"
        exit 1
        ;;
esac
