package com.company.team.infrastructure.controller.verticle.router;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public abstract class RestModule {
    protected Router router;
    protected Vertx vertx;

    public RestModule(Vertx vertx) {
        router = Router.router(vertx);
        this.vertx = vertx;
        initRoute();
    }

    public abstract void initRoute();

    public void install(Router rootRouter, String location) {
        rootRouter.mountSubRouter(location, router);
    }
}
