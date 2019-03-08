FROM adoptopenjdk/openjdk11-openj9:alpine-slim
RUN mkdir /app && mkdir /app/shared
WORKDIR /app
COPY target/j9-demo-fat.jar ./
ENV JAVA_TOOL_OPTIONS="-XX:+IgnoreUnrecognizedVMOptions -XX:+UseContainerSupport -XX:+IdleTuningCompactOnIdle -XX:+IdleTuningGcOnIdle -Xtune:virtualized -Xshareclasses:cacheDir=/app/shared -Dio.netty.noUnsafe=true -Dvertx.disableDnsResolver=true"

CMD java -jar j9-demo-fat.jar