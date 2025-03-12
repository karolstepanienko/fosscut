#!/usr/bin/env bash

# Fail on first failed command and print commands before executing them
set -e -x

# Check if an argument is provided
if [ $# -eq 0 ]; then
    echo "Usage: $0 {api|cli|gui|controller-jenkins}"
    exit 1
fi

function upload_image() {
    local IMAGE_NAME=$1
    local NODE=$2
    scp /img/$IMAGE_NAME.tar $NODE:/img/$IMAGE_NAME.tar
    ssh $NODE "ctr -n k8s.io image import /img/$IMAGE_NAME.tar"
}

# Execute commands based on the argument
case "$1" in
    api)
        echo "Building API docker image..."
        # amd64 only build
        docker build -t karolstepanienko/fosscut-api-jar:0.0.1 --progress plain -f api/Dockerfile-jar .
        docker save karolstepanienko/fosscut-api-jar:0.0.1 > /img/fosscut-api-jar.tar
        # Upload the image to cluster nodes
        upload_image fosscut-api-jar arch-gamma &
        upload_image fosscut-api-jar arch-beta &
        wait
        ;;
    cli)
        echo "Building CLI docker image..."
        # amd64 only build
        docker build -t karolstepanienko/fosscut-cli-native:0.0.1 --progress plain -f cli/Dockerfile-native .
        docker save karolstepanienko/fosscut-cli-native:0.0.1 > /img/fosscut-cli-native.tar
        # Upload the image to cluster nodes
        upload_image fosscut-cli-native arch-gamma &
        upload_image fosscut-cli-native arch-beta &
        wait
        ;;
    gui)
        echo "Building GUI docker image..."
        # amd64 only build
        docker build -t karolstepanienko/fosscut-gui-native:0.0.1 --progress plain -f gui/Dockerfile-native .
        docker save karolstepanienko/fosscut-gui-native:0.0.1 > /img/fosscut-gui-native.tar
        # Upload the image to cluster nodes
        upload_image fosscut-gui-native arch-gamma &
        upload_image fosscut-gui-native arch-beta &
        wait
        ;;
    controller-jenkins)
        echo "Building Jenkins controller docker image..."
        docker build --no-cache -t karolstepanienko/fosscut-jenkins-controller:2.492.2-jdk17 -f k8s/jenkins/controller/Dockerfile k8s/jenkins/controller/
        docker save karolstepanienko/fosscut-jenkins-controller:2.492.2-jdk17 > /img/fosscut-jenkins-controller.tar
        upload_image fosscut-jenkins-controller arch-gamma &
        upload_image fosscut-jenkins-controller arch-beta &
        ;;
    *)
        echo "Invalid argument: $1"
        echo "Usage: $0 {api|cli|gui}"
        exit 1
        ;;
esac
