# amd64 only build
docker build -t karolstepanienko/fosscut-api-jar:0.0.1 --progress plain -f Dockerfile-jar .
docker save karolstepanienko/fosscut-api-jar:0.0.1 > /img/fosscut-api-jar.tar
scp /img/fosscut-api-jar.tar arch-beta:/img/fosscut-api-jar.tar
ssh arch-beta "ctr -n k8s.io image import /img/fosscut-api-jar.tar"
