package org.jacpfx.vxms.verticle;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class LoadUsers  {
    byte[] loadAll() {
        Vertx vertx = Vertx.vertx();
        MongoClient mongoClient = InitMongoDB.initMongoData(vertx, vertx.getOrCreateContext().config());
        //Future<String> future = Future.future();
        CompletableFuture<String> future = new CompletableFuture<>();
        mongoClient.find("users", new JsonObject(), lookup -> {
            // error handling
            if (lookup.failed()) {
                System.out.println("Mongo: failed");
                future.complete(lookup.cause().getMessage());
            } else {
                System.out.println("Mongo: OK");
                future.complete(new JsonArray(lookup.
                        result().
                        stream().
                        collect(Collectors.toList())).
                        encode());
            }

        });
        String result = "";
        try {
            result = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("result----------------: "+result);
        return result.getBytes(Charset.forName("UTF-8"));

    }

}
