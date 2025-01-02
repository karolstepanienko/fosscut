# amd64 only build
docker build -t karolstepanienko/fosscut-cli-native:0.0.1 --progress plain -f Dockerfile-native .
docker save karolstepanienko/fosscut-cli-native:0.0.1 > /img/fosscut-cli-native.tar
scp /img/fosscut-cli-native.tar arch-beta:/img/fosscut-cli-native.tar
ssh arch-beta "ctr -n k8s.io image import /img/fosscut-cli-native.tar"
scp /img/fosscut-cli-native.tar arch-gamma:/img/fosscut-cli-native.tar
ssh arch-gamma "ctr -n k8s.io image import /img/fosscut-cli-native.tar"
