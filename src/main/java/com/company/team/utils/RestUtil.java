package com.company.team.utils;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class RestUtil {

    private RestUtil() {
        throw new IllegalStateException("Utility class");
    }


    public static void restError(HttpServerResponse resp, int statusCode, String msg) {
        if (msg == null || msg.isEmpty()) {
            msg = "Something bad happened";
        }
        JsonObject obj = new JsonObject();
        obj.put("status", 0);
        obj.put("message", msg);
        obj.put("code", statusCode);
        obj.put("data", new JsonObject());
        resp.setStatusCode(200).end(obj.encodePrettily());
    }

    public static void restError(HttpServerResponse resp, int statusCode, JsonObject object) {
        JsonObject obj = new JsonObject();
        obj.put("status", 0);
        obj.put("message", "Failed");
        obj.put("code", statusCode);
        obj.put("data", object);
        resp.setStatusCode(200).end(obj.encodePrettily());
    }

    public static void restError(HttpServerResponse resp, int statusCode, String message, JsonObject object) {
        JsonObject obj = new JsonObject();
        obj.put("status", 0);
        obj.put("message", message);
        obj.put("code", statusCode);
        obj.put("data", object);
        resp.setStatusCode(statusCode).end(obj.encodePrettily());
    }

    public static void restError(HttpServerResponse resp, int statusCode, Throwable e) {
        restError(resp, statusCode, e.getMessage());
    }

    public static void restSuccess(HttpServerResponse resp, JsonObject object) {
        JsonObject obj = new JsonObject();
        obj.put("status", 1);
        obj.put("message", "Successful");
        obj.put("code", 200);
        obj.put("data", object);
        resp.setStatusCode(200).end(obj.encodePrettily());
    }

    public static void restSuccess(HttpServerResponse resp, Object object) {
        JsonObject obj = new JsonObject();
        obj.put("status", 1);
        obj.put("message", "Successful");
        obj.put("code", 200);
        obj.put("data", object);
        resp.setStatusCode(200).end(obj.encodePrettily());
    }

    public static void restSuccess(HttpServerResponse resp, JsonArray object) {
        JsonObject obj = new JsonObject();
        obj.put("status", 1);
        obj.put("message", "Successful");
        obj.put("code", 200);
        obj.put("data", object);
        resp.setStatusCode(200).end(obj.encodePrettily());
    }

}
