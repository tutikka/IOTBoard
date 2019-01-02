package com.tt.iotboard.server;

import com.tt.iotboard.server.authentication.Authentication;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerVerticle extends AbstractVerticle {

    private static final Logger L = LoggerFactory.getLogger(ServerVerticle.class);

    private HttpServer httpServer;

    private EventBus eventBus;

    /**
     *
     * @throws Exception
     */
    @Override
    public void start() throws Exception {
        L.trace("start");

        Router router = Router.router(vertx);

        // html
        router.route("/html/*").handler(StaticHandler.create().setCachingEnabled(false));

        // event
        BridgeOptions bridgeOptions = new BridgeOptions().addOutboundPermitted(new PermittedOptions().setAddress("event.to.client"));
        SockJSHandler sockJSHandler = SockJSHandler.create(vertx).bridge(bridgeOptions);
        router.route("/event/*").handler(sockJSHandler);

        // api
        router.post("/api/v1/measurement").handler(this::measurement_v1);

        httpServer = vertx.createHttpServer();
        httpServer.requestHandler(router::accept).listen(Config.getInstance().getPort());

        eventBus = vertx.eventBus();
    }

    /**
     *
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        L.trace("stop");

        httpServer.close();
    }

    /**
     *
     * @param routingContext
     */
    private void measurement_v1(RoutingContext routingContext) {
        L.trace("measurement_v1");

        HttpServerRequest request = routingContext.request();

        request.bodyHandler(bodyHandler -> {

            Authentication authentication = Config.getInstance().getAuthentication();
            if (!authentication.validateRequestHeaders(request, bodyHandler.toString())) {
                HttpServerResponse response = routingContext.response();
                response.setStatusCode(403);
                response.end();
                return;
            }

            L.info("body: " + bodyHandler.toString());
            JsonArray jsonArray = bodyHandler.toJsonArray();
            eventBus.publish("event.to.client", jsonArray);

        });

        HttpServerResponse response = routingContext.response();
        response.setStatusCode(200);
        response.end();
    }

}
