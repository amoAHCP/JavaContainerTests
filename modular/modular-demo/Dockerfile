# First stage: Copy the project and run mvn clean install in Docker to build the project against Java 9
FROM maven:3.5.3-jdk-9 AS mavenbuilder
MAINTAINER Andy Moncsek <amo.ahcp@gmail.com>
RUN apt-get update && apt-get install -y --no-install-recommends git && rm -rf /var/lib/apt/lists/*
WORKDIR /app
ADD . /app
RUN mvn clean install
RUN ls -la target/modules/


# Second stage: Copies the builded application and creates a custom JRE
FROM openjdk:13-ea-9-jdk-alpine3.9 as builder
MAINTAINER Andy Moncsek <amo.ahcp@gmail.com>

WORKDIR /app
COPY --from=mavenbuilder /app/ ./
COPY --from=mavenbuilder /app/target/modular-demo-1.2-SNAPSHOT.jar ./target/modules
RUN jlink --module-path target/modular-demo-1.2-SNAPSHOT.jar:target/modules:$JAVA_HOME/jmods \
          --add-modules vxms.core.demo \
          --limit-modules vxms.core.demo \
          --launcher run=vxms.core.demo/org.jacpfx.vxms.verticle.Runner \
          --output dist \
          --compress 2 \
          --strip-debug \
          --no-header-files \
          --no-man-pages \
          --vm server


# Third stage: Copies the custom JRE into our image and runs it
FROM alpine:3.9
MAINTAINER Andy Moncsek <amo.ahcp@gmail.com>
WORKDIR /app
COPY --from=builder /app/dist/ ./
# activate CDS
RUN bin/java -Xshare:dump
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -Xshare:on -Djava.net.preferIPv4Stack=true -Dio.netty.noUnsafe=true -Dvertx.disableDnsResolver=true -Djava.security.egd=file:/dev/./urandom"
CMD bin/run