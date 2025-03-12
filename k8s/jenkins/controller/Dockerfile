FROM docker.io/jenkins/jenkins:2.492.2-jdk17

# Preinstall plugins, otherwise they would be installed at startup
COPY --chown=jenkins:jenkins plugins.yaml /usr/share/jenkins/plugins.yaml
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/plugins.yaml
