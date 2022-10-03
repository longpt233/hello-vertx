package com.company.team.infrastructure.repo;

import com.company.team.adapter.repo.UserRepository;
import com.company.team.data.entity.User;
import com.company.team.infrastructure.database.MySqlConnector;
import io.vertx.core.Future;
import io.vertx.core.Promise;

public class UserDao implements UserRepository {

    public UserDao() {
    }

    @Override
    public Future<User> getAllUser() {

        Promise<User> promise = Promise.promise();
        MySqlConnector.query("select * from ecom_be.user_tb")
            .onSuccess(resultSet -> {
                System.out.println(resultSet.toJson());
//                String mail = resultSet.getNext();
                User u = new User();
                promise.complete(u);
            }).onFailure(throwable->{
                promise.fail(throwable.getCause());
            });

        return promise.future();
    }
}
