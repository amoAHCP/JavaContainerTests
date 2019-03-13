FROM adoptopenjdk/openjdk11-openj9:alpine-slim
RUN mkdir /app && mkdir /app/shared
WORKDIR /app
RUN ls -la
COPY target/j9-demo-fat.jar ./
ENV JAVA_TOOL_OPTIONS="-XX:+IgnoreUnrecognizedVMOptions -XX:+UseContainerSupport -XX:+IdleTuningCompactOnIdle -XX:+IdleTuningGcOnIdle -Xtune:virtualized -Xshareclasses:cacheDir=/app/shared -Dio.netty.noUnsafe=true -Dvertx.disableDnsResolver=true -Djava.security.egd=file:/dev/./urandom"

CMD ["java", "-jar", "j9-demo-fat.jar"]