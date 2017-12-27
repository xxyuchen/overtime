package com.nancy.security.config;

import com.nancy.model.User;
import com.nancy.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

/**
 * Created by Administrator on 2017/12/23 0023.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserService userService;

    // 通过构造器注入UserService
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = new User();
        user.setName(name);
        User myUser = userService.findByAttr(user);
        if(myUser == null){
            throw new UsernameNotFoundException("找不到对应用户!!!");
        }
        return new org.springframework.security.core.userdetails.User(myUser.getName(), myUser.getPwd(), emptyList());
    }
    public User doLogin(String loginName) throws AuthenticationException {
        if (StringUtils.isBlank(loginName)) {
            throw new BadCredentialsException("账号不能为空!");
        }
        User user = new User();
        user.setName(loginName);
        user = userService.findByAttr(user);
        if (user == null) {
            throw new UsernameNotFoundException("账号或密码不正确!");
        }
        return user;
    }
}
