package com.company.team.infrastructure.controller.verticle.router;

import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;

import java.util.HashSet;
import java.util.Set;

public class ProxyHandler {

    public ProxyHandler(Router router, String path){

        enableCorsSupport(router);
        router.route(path + "/*").handler(x -> {
            x.response().putHeader("content-type", "application/json");
            x.next();
        });

    }

    protected void enableCorsSupport(Router router) {

        Set<HttpMethod> allowMethods = new HashSet<>();
        allowMethods.add(HttpMethod.GET);
        allowMethods.add(HttpMethod.PUT);
        allowMethods.add(HttpMethod.OPTIONS);
        allowMethods.add(HttpMethod.POST);
        allowMethods.add(HttpMethod.DELETE);
        allowMethods.add(HttpMethod.PATCH);
        router.route().handler(CorsHandler.create("*").allowedMethods(allowMethods));
    }

}
