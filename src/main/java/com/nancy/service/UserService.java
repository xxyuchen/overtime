package com.nancy.service;

import com.nancy.model.User;

/**
 * Created by Administrator on 2017/12/22 0022.
 */
public interface UserService {
    User selectById(Integer id);

    int insert(User user);

    User findByAttr(User user);

    int update(User user);
}
