package com.company.team.infrastructure.controller.verticle.subrouter;

import com.company.team.enumcustom.ResponseStatus;
import com.company.team.service.UserService;
import com.company.team.utils.RestUtil;
import com.company.team.infrastructure.controller.verticle.router.RestModule;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;


public class UserRouter extends RestModule {

    private final UserService userService;

    public UserRouter(Vertx vertx) {
        super(vertx);
        this.userService = new UserService();
    }

    @Override
    public void initRoute() {

        router.get("/ping").handler(this::ping);
        router.get("/get-all").handler((this::getAll));
    }

    private void ping(RoutingContext ctx) {
        try {
            // do something
            RestUtil.restSuccess(ctx.response(),"pong");
        } catch (Exception e) {
            RestUtil.restError(ctx.response(), ResponseStatus.SERVER_ERROR, e);
        }
    }

    private void getAll(RoutingContext ctx) {
        try {
            // do something
            userService.getAllUser()
                    .onSuccess(user ->{
                        RestUtil.restSuccess(ctx.response(),user);
                    })
                    .onFailure(r->{
                        RestUtil.restError(ctx.response(), ResponseStatus.SERVER_ERROR, r.getMessage());

                    });
        } catch (Exception e) {
            RestUtil.restError(ctx.response(), ResponseStatus.SERVER_ERROR, e);
        }
    }

}
