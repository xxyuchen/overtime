package com.nancy.security.config;

import com.nancy.model.User;
import com.nancy.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static java.util.Collections.emptyList;

/**
 * Created by Administrator on 2017/12/23 0023.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserService userService;

    // 通过构造器注入UserService
    /*public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }*/

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User myUser = doLogin(name);
        return new org.springframework.security.core.userdetails.User(myUser.getName(), myUser.getPwd(), emptyList());
    }
    public User doLogin(String loginName) throws AuthenticationException {
        if (StringUtils.isBlank(loginName)) {
            throw new BadCredentialsException("账号不能为空!");
        }
        User user = new User();
        user.setName(loginName);
        user = loadUser(user);
        if (user == null) {
            throw new UsernameNotFoundException("账号或密码不正确!");
        }
        return user;
    }
    private User loadUser(User user) {
        try {
            return userService.findByAttr(user);
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException("DCRM用户接口调用失败", e.getCause());
        }
    }
}
