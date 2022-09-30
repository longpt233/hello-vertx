package com.company.team.infrastructure.database;

import com.company.team.config.ConfigInfo;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;

public class MySqlConnector {

    // volatile -> thread sync
    private static volatile JDBCClient dbClientInstance;

    // singleton -> chú ý phải khởi tạo trước tại base verticle
    public static void init(Vertx vertx) {

        if (dbClientInstance ==null) {
            synchronized (MySqlConnector.class){
                if(dbClientInstance == null){
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
        System.out.println("init pool successful");
    }

    private static Future<SQLConnection> getConnection() {
        Promise<SQLConnection> promise = Promise.promise();
        dbClientInstance.getConnection(promise);     // Promise<T> extends Handler<AsyncResult<T>>
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
                }).onFailure(e -> {

                }).onComplete(sqlConnectionAsyncResult -> {
                    System.out.println("return connection to pool");
                    sqlConnectionAsyncResult.result().close();
                });

        return promise.future();
    }


}
