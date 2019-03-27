FROM oracle/graalvm-ce:1.0.0-rc14 AS build-aot
WORKDIR /app
ADD . /app
RUN ./mvnw package

# Create new image from alpine
FROM frolvlad/alpine-glibc:alpine-3.9
#RUN apk add --no-cache ca-certificates
# Copy generated native executable from build-aot
COPY --from=build-aot /app/target/graal-demo /graal-demo
COPY --from=build-aot /app/config /config
# Set the entrypoint
ENTRYPOINT [ "/graal-demo" ]