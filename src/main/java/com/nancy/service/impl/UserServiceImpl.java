package com.nancy.service.impl;

import com.nancy.service.UserService;
import com.nancy.mapper.UserMapper;
import com.nancy.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/12/22 0022.
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Override
    public User selectById(Integer id) {
        return this.userMapper.selectByPrimaryKey(id);
    }

    @Override
    public User findByAttr(User user) {
        return this.userMapper.findByAttr(user);
    }

    @Override
    public int insert(User user) {
        return this.userMapper.insert(user);
    }
}
