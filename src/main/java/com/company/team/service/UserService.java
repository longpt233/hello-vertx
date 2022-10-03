package com.company.team.service;

import com.company.team.adapter.repo.UserRepository;
import com.company.team.data.dto.UserPresenter;
import com.company.team.data.entity.User;
import com.company.team.infrastructure.repo.UserDao;
import io.vertx.core.Future;
import io.vertx.core.Promise;

public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        userRepository = new UserDao();
    }

    public Future<UserPresenter> getAllUser() {

        Promise<UserPresenter> userPresenterFuture = Promise.promise();

        userRepository.getAllUser()
                .onSuccess(user -> {
                    UserPresenter userPresenter = new UserPresenter();
                    userPresenterFuture.complete(userPresenter);
                })
                .onFailure(e->{
                    userPresenterFuture.fail(e);
                });
        return userPresenterFuture.future();
    }
}
