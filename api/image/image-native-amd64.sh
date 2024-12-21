# amd64 only build
docker build -t karolstepanienko/fosscut-api-native:0.0.1 --progress plain -f Dockerfile-native .
docker save karolstepanienko/fosscut-api-native:0.0.1 > /img/fosscut-api-native.tar
scp /img/fosscut-api-native.tar arch-beta:/img/fosscut-api-native.tar
ssh arch-beta "ctr -n k8s.io image import /img/fosscut-api-native.tar"
scp /img/fosscut-api-native.tar arch-gamma:/img/fosscut-api-native.tar
ssh arch-gamma "ctr -n k8s.io image import /img/fosscut-api-native.tar"
