# amd64 and arm64 build with saving to tar
docker buildx build --builder buildx-builder --platform linux/amd64,linux/arm64 -t karolstepanienko/fosscut-api-native:0.0.1 --progress plain --output type=oci,dest=/img/fosscut-api-native.tar -f Dockerfile-native .
scp /img/fosscut-api-native.tar rpi-arch:/img/fosscut-api-native.tar
ssh rpi-arch "ctr -n k8s.io image import /img/fosscut-api-native.tar" &
scp /img/fosscut-api-native.tar arch-beta:/img/fosscut-api-native.tar
ssh arch-beta "ctr -n k8s.io image import /img/fosscut-api-native.tar"
