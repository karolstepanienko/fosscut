# amd64 only build
docker build -t karolstepanienko/fosscut-gui-native:0.0.1 --progress plain -f Dockerfile-native .
docker save karolstepanienko/fosscut-gui-native:0.0.1 > /img/fosscut-gui-native.tar
scp /img/fosscut-gui-native.tar arch-beta:/img/fosscut-gui-native.tar
ssh arch-beta "ctr -n k8s.io image import /img/fosscut-gui-native.tar"
