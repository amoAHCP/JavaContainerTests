FROM oracle/graalvm-ce:1.0.0-rc15 AS build-aot
WORKDIR /app
ADD . /app
RUN ./mvnw package

# Create new image from alpine
FROM frolvlad/alpine-glibc:alpine-3.9
COPY --from=build-aot /app/target/graal-demo /graal-demo
COPY --from=build-aot /app/config /config
ENV LOCAL="false"
# Set the entrypoint
ENTRYPOINT [ "/graal-demo" ]