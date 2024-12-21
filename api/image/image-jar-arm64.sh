# amd64 and arm64 build with saving to tar
docker buildx build --builder buildx-builder --platform linux/amd64,linux/arm64 -t karolstepanienko/fosscut-api-jar:0.0.1 --progress plain --output type=oci,dest=/img/fosscut-api-jar.tar -f Dockerfile-jar .
scp /img/fosscut-api-jar.tar rpi-arch:/img/fosscut-api-jar.tar
ssh rpi-arch "ctr -n k8s.io image import /img/fosscut-api-jar.tar" &
scp /img/fosscut-api-jar.tar arch-beta:/img/fosscut-api-jar.tar
ssh arch-beta "ctr -n k8s.io image import /img/fosscut-api-jar.tar"
scp /img/fosscut-api-jar.tar arch-gamma:/img/fosscut-api-jar.tar
ssh arch-gamma "ctr -n k8s.io image import /img/fosscut-api-jar.tar"
