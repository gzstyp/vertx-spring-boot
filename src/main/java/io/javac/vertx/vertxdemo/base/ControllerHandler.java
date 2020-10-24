package io.javac.vertx.vertxdemo.base;

import io.javac.vertx.vertxdemo.vertx.VertxRequest;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public interface ControllerHandler extends Handler<RoutingContext> {

    @Override
    default void handle(RoutingContext event) {
        handle(VertxRequest.build(event));
    }
    void handle(VertxRequest vertxRequest);
}