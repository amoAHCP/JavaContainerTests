package org.jacpfx.vxms.verticle;

import java.io.File;

import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.logging.SLF4JLogDelegateFactory;
import io.vertx.ext.web.Router;

public class Runner {

    public static void main(String[] args) {
        final long startTime = System.currentTimeMillis();

        // Setup the http server
        System.out.printf("Starting server for: http://localhost:8080/hello");
        Vertx vertx = Vertx.vertx();
        Router router = Router.router(vertx);

        router.route("/hello").handler(rc -> {
            System.out.printf("Got hello request");
            rc.response().end("World");
        });

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8080, handler -> {
                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    System.out.println("finished in: " + elapsedTime +" ms");
                });

    }



}