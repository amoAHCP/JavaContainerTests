package org.jacpfx.vxms.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.logging.SLF4JLogDelegateFactory;
import io.vertx.ext.mongo.MongoClient;
import org.jacpfx.vxms.common.ServiceEndpoint;
import org.jacpfx.vxms.rest.base.RouteBuilder;
import org.jacpfx.vxms.rest.base.VxmsRESTRoutes;
import org.jacpfx.vxms.rest.base.response.RestHandler;
import org.jacpfx.vxms.services.VxmsEndpoint;

import java.io.File;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServiceEndpoint
public class VxmsIsolateRunner extends AbstractVerticle {
    private MongoClient mongo;
    Logger log = Logger.getLogger(VxmsIsolateRunner.class.getName());

    @Override
    public void start(Future<Void> startFuture) {
        final long startTime = System.currentTimeMillis();
        VxmsRESTRoutes routes =
                VxmsRESTRoutes.init()
                        .route(RouteBuilder.get("/hello/:name", this::hello))
                        .route(RouteBuilder.post("/user", this::inertUser))
                        .route(RouteBuilder.get("/users", this::getAllUsers));

        mongo = InitMongoDB.initMongoData(vertx, config());
        Future<String> future = Future.future();

        future.setHandler(v -> {
            System.out.println("User created: " + v);
            VxmsEndpoint.init(startFuture, this, routes);
            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            System.out.println("finished in: " + elapsedTime + " ms");
        });
        handleInsert(DefaultResponses.testUser(), future);


    }

    public void hello(RestHandler handler) {
        String name = handler.request().param("name");
        System.out.println("hello: " + name);
        handler.response().stringResponse((future) -> future.complete("hello: " + name)).execute();
    }

    public void getAllUsers(RestHandler reply) {
        reply.
                response().
                blocking().
                stringResponse(() -> UserService.getAllUsers()).
                timeout(10000).
                onError(error -> log.log(Level.WARNING, "ERROR: " + error.getMessage())).
                onFailureRespond((failure) ->
                        DefaultResponses.
                                defaultErrorResponse(failure.getMessage()).
                                encodePrettily()
                ).
                execute();
    }


    public void inertUser(RestHandler handler) {
        final JsonObject body = handler.request().body().toJsonObject();
        if (body == null || body.isEmpty()) {
            handler.response().stringResponse(f -> f.fail(DefaultResponses.defaultErrorResponse("no content").encode())).execute();
            return;
        }
        handler.
                response().
                stringResponse(future -> handleInsert(body, future)).
                retry(2).
                timeout(1000).
                onError(t -> log.log(Level.WARNING, "ERROR: " + t.getMessage())).
                onFailureRespond((onError, future) ->
                        future.complete(DefaultResponses.
                                defaultErrorResponse(onError.getMessage()).
                                encode())
                ).
                execute();
    }


    private void handleInsert(final JsonObject newUser, Future<String> future) {
        mongo.findOne("users", new JsonObject().put("username", newUser.getString("username")), null, lookup -> {
            // error handling
            if (lookup.failed()) {
                future.fail(lookup.cause());
                return;
            }

            JsonObject user = lookup.result();
            if (user != null) {
                // already exists
                future.fail("user already exists");
            } else {
                mongo.insert("users", newUser, insert -> {
                    // error handling
                    if (insert.failed()) {
                        future.fail("lookup failed");
                        return;
                    }
                    // add the generated id to the user object
                    newUser.put("_id", insert.result());
                    future.complete(newUser.encode());
                });
            }
        });
    }

    public static void main(String[] args) {
        Boolean local = Boolean.valueOf(Optional.ofNullable(System.getenv("LOCAL")).orElse("true"));
        DeploymentOptions options = new DeploymentOptions().setInstances(1).setConfig(new JsonObject().put("local", local));

        Vertx.vertx().deployVerticle(new VxmsIsolateRunner(), options);

    }

    private static void initLogging() {

        //Get file from resources folder
        File logbackFile = new File("config", "logback.xml");
        System.setProperty("logback.configurationFile", logbackFile.getAbsolutePath());
        System.setProperty(LoggerFactory.LOGGER_DELEGATE_FACTORY_CLASS_NAME, SLF4JLogDelegateFactory.class.getName());
    }
}
