docker run --name mongo -p 27017:27017 -d mongo mongod
docker build -t native-demo . && docker run --rm -p 9090:8080 --link mongo:mongo --name native-demo --rm native-demo



https://vertx-howtos.github.io/graal-native-image-howto/


https://github.com/oracle/graal/blob/master/substratevm/REFLECTION.md


docker push amoahcp/native-test

