package com.company.team.service;

import com.company.team.adapter.repo.UserRepository;
import com.company.team.data.entity.User;
import com.company.team.infrastructure.database.MySqlConnector;
import com.company.team.infrastructure.repo.UserDao;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.sql.SQLConnection;

public class UserService {

    private UserRepository userRepository;

    public UserService(){
        userRepository = new UserDao();
    }

    public Future<User> getAllUser() {

        return userRepository.getAllUser().compose(user ->{
            Promise<User> userFuture = Promise.promise();
            if(getAllUser().succeeded())
                userFuture.complete(user);

            return userFuture.future();
        });
    }
}
