docker run --name mongo -p 27017:27017 -d mongo mongod
docker build -t native-test . && docker run --rm -p 9090:8080 --link mongo:mongo --name native-test native-test



https://vertx-howtos.github.io/graal-native-image-howto/


https://github.com/oracle/graal/blob/master/substratevm/REFLECTION.md


docker push amoahcp/native-test

