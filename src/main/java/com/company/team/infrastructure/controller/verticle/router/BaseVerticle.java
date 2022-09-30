package com.company.team.infrastructure.controller.verticle.router;

import com.company.team.config.ConfigInfo;
import com.company.team.enumcustom.ResponseStatus;
import com.company.team.infrastructure.database.MySqlConnector;
import com.company.team.utils.RestUtil;
import com.company.team.infrastructure.controller.verticle.subrouter.UserRouter;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.LoggerFormat;
import io.vertx.ext.web.handler.LoggerHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * một verticle giống như 1 eventloop để xử lí các event
 */
public class BaseVerticle extends AbstractVerticle{

    public static final Logger LOG_SERVICE = LogManager.getLogger("log-service");

    private static final String ROOT_API = "/api/v1";
    private static final String USER_API = "/api/v1/user";

    @Override
    public void start() throws Exception {
        super.start();
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        new ProxyHandler(router, ROOT_API);
        router.route().handler(LoggerHandler.create(LoggerFormat.TINY));

        MySqlConnector.init(vertx);  // TODO: nên thêm một cái verticle khác để handle sql, và dùng event bus để giao tiếp.

        // BrandSafety Service
        RestModule toolVerticle = new UserRouter(vertx);
        toolVerticle.install(router, USER_API);


        router.route(ROOT_API + "/*").handler(ctx -> RestUtil.restError(ctx.response(), ResponseStatus.BAD_REQUEST, "API endpoint not found"));

        HttpServer server = vertx.createHttpServer().requestHandler(router);
        server.listen(ConfigInfo.PORT)
                .onFailure(Throwable::printStackTrace)
                .onSuccess(httpServer -> System.out.println("Server start listening at port " + httpServer.actualPort()));
    }
}
