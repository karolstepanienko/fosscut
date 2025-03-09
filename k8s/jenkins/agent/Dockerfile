FROM karolstepanienko/fosscut-cli-native:0.0.1 AS cli

FROM jenkins/inbound-agent:3283.v92c105e0f819-9 AS final

# Adding fosscut-ca for HTTPS connectivity between jenkins inbound agent and
# jenkins controller
COPY ./secrets/ca.crt /home/jenkins
USER root
RUN keytool -importcert -noprompt -trustcacerts -alias fosscut-ca \
    -file /home/jenkins/ca.crt \
    -keystore /opt/java/openjdk/lib/security/cacerts -storepass changeit
USER jenkins

COPY --from=cli /fosscut ./bin/fosscut
ENV PATH="/home/jenkins/bin/:${PATH}"
