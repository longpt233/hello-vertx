package com.company.team.adapter.repo;

import com.company.team.data.entity.User;
import io.vertx.core.Future;

public interface UserRepository {
    Future<User> getAllUser();
}
