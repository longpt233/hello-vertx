package com.company.team.app;

import com.company.team.infrastructure.controller.verticle.router.BaseVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class App {
    public static void main(String[] args) {

        int maxInstances = 2 * Runtime.getRuntime().availableProcessors();
        System.out.println("total instances:"+ maxInstances);
        maxInstances = 2;

        VertxOptions vertxOptions = new VertxOptions();
        vertxOptions.setWorkerPoolSize(maxInstances);
        Vertx vertx = Vertx.vertx(vertxOptions);

        DeploymentOptions options = new DeploymentOptions().setWorker(true).setWorkerPoolName("proxy-verticle");
        options.setInstances(maxInstances);
        options.setMaxWorkerExecuteTime(10000000000L);
        vertx.deployVerticle(BaseVerticle.class, options);
    }
}
