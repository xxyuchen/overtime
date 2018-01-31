package com.nancy.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
* @Author TangZhen
* @Date 2018/1/31 0031 16:15
* @Description  自定义异常
*/
public class UsernameIsExitedException extends AuthenticationException {

    public UsernameIsExitedException(String msg) {
        super(msg);
    }

    public UsernameIsExitedException(String msg, Throwable t) {
        super(msg, t);
    }
}