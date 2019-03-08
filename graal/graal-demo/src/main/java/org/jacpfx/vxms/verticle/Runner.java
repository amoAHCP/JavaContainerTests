package org.jacpfx.vxms.verticle;

import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.logging.SLF4JLogDelegateFactory;
import io.vertx.ext.web.Router;

import java.io.File;

public class Runner {

    public static void main(String[] args) {
        final long startTime = System.currentTimeMillis();
        initLogging();
        Logger log = LoggerFactory.getLogger(Runner.class);

        // Setup the http server
        log.info("Starting server for: http://localhost:8080/hello");
        Vertx vertx = Vertx.vertx();
        Router router = Router.router(vertx);

        router.route("/hello").handler(rc -> {
            log.info("Got hello request");
            rc.response().end("World");
        });

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080, handler -> {
                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    System.out.println("finished in: " + elapsedTime +" ms");
                });

    }

    private static void initLogging() {

        //Get file from resources folder
        File logbackFile = new File("config","logback.xml");
        System.setProperty("logback.configurationFile", logbackFile.getAbsolutePath());
        System.setProperty(LoggerFactory.LOGGER_DELEGATE_FACTORY_CLASS_NAME, SLF4JLogDelegateFactory.class.getName());
    }

}