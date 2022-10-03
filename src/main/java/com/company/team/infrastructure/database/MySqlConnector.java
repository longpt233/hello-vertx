package com.company.team.infrastructure.database;

import com.company.team.config.ConfigInfo;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;

public class MySqlConnector {

    // volatile -> thread sync
    private static volatile SQLClient dbClientInstance;

    // singleton -> chú ý phải khởi tạo trước tại base verticle
    public static void init(Vertx vertx) {

        if (dbClientInstance == null) {
            synchronized (MySqlConnector.class) {
                if (dbClientInstance == null) {
                    // Share -> nhiều verticle có thể share data cùng 1 cái instance này
                    dbClientInstance = JDBCClient.createShared(vertx,
                            new JsonObject()
                                    .put("url", ConfigInfo.MYSQL_URL)
                                    .put("driver_class", "com.mysql.cj.jdbc.Driver")
                                    .put("initial_pool_size", 5)
                                    .put("max_idle_time", 20)
                                    .put("max_pool_size", 10));
                }
            }
        }
        System.out.println("init pool successful: "+dbClientInstance.toString());  // same instance mặc dù là 2 cái verticle được deploy
    }

    private static Future<SQLConnection> getConnection() {
        Promise<SQLConnection> promise = Promise.promise();
        dbClientInstance.getConnection(promise);     // Promise<T> extends Handler<AsyncResult<T>>
//        promise.fail("");
        return promise.future();
    }

    public static Future<ResultSet> query(String sql) {

        Promise<ResultSet> promise = Promise.promise();

        getConnection()
                .onSuccess(sqlConnection -> {
                    System.out.println("get a connection successful");
                    sqlConnection.query(sql, r -> {
                        if (r.succeeded()) {
                            promise.complete(r.result());
                        } else {
                            promise.fail(r.cause());
                        }
                    });
                })
                .onFailure(e->{
                    System.out.println("cant execute query");
                    promise.fail(e);
                })
                .onComplete(sqlConnectionAsyncResult -> {
                    System.out.println("return connection to pool");
                    sqlConnectionAsyncResult.result().close();
                });

        return promise.future();
    }

    /**
     * queryWithParams -> prepare
     * String query = "SELECT ID, FNAME, LNAME, SHOE_SIZE from PEOPLE WHERE LNAME=? AND SHOE_SIZE > ?";
     * JsonArray params = new JsonArray().add("Fox").add(9);
     */
    public static Future<ResultSet> prepareQuery(String sql, JsonArray param) {

        Promise<ResultSet> promise = Promise.promise();

        getConnection()
                .onSuccess(sqlConnection -> {
                    System.out.println("get a connection successful");
                    sqlConnection.queryWithParams(sql,param, r -> {
                        if (r.succeeded()) {
                            promise.complete(r.result());
                        } else {
                            promise.fail(r.cause());
                        }
                    });
                })
                .onFailure(e->{
                    System.out.println("cant execute query");
                    promise.fail(e);
                })
                .onComplete(sqlConnectionAsyncResult -> {
                    System.out.println("return connection to pool");
                    sqlConnectionAsyncResult.result().close();
                });

        return promise.future();
    }


}
