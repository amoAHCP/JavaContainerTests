FROM panga/graalvm-ce:latest AS build-aot
WORKDIR /app
ADD . /app
# Build image
RUN native-image \
    --no-server \
    -Djava.net.preferIPv4Stack=true \
    -Dio.netty.noUnsafe=true \
    -Dvertx.disableDnsResolver=true \
    -H:+ReportUnsupportedElementsAtRuntime \
    -H:ReflectionConfigurationFiles=./reflectconfigs/netty.json \
    -jar "target/graal-demo-fat.jar"

# Create new image from alpine
FROM frolvlad/alpine-glibc:alpine-3.9
#RUN apk add --no-cache ca-certificates
# Copy generated native executable from build-aot
COPY --from=build-aot /app/graal-demo-fat /graal-demo-fat
COPY --from=build-aot /app/config /config
# Set the entrypoint
ENTRYPOINT [ "/graal-demo-fat" ]