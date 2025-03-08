FROM jenkins/inbound-agent:3283.v92c105e0f819-9

# Adding fosscut-ca for HTTPS connectivity between jenkins inbound agent and
# jenkins controller
COPY ./secrets/ca.crt /home/jenkins
USER root
RUN keytool -importcert -noprompt -trustcacerts -alias fosscut-ca \
    -file /home/jenkins/ca.crt \
    -keystore /opt/java/openjdk/lib/security/cacerts -storepass changeit
USER jenkins
