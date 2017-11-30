FROM azul/zulu-openjdk-alpine:8u152-8.25.0.1

ENV JAVA_HOME="/usr/lib/jvm/zulu8.25.0.1-jdk8.0.152-linux_x64"

WORKDIR '/u00'
COPY *jar /u00/lib/

RUN wget https://github.com/jmxtrans/jmxtrans-agent/releases/download/jmxtrans-agent-1.2.7-beta-1/jmxtrans-agent-1.2.7-beta-1.jar -O /u00/jmxtrans-agent.jar

ENTRYPOINT exec java -cp "/u00/lib/*" \
                     -javaagent:/u00/jmxtrans-agent.jar=classpath:metrics.xml \
                     org.foo.ApplicationMain
