package com.nancy.annotation;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2018/1/31 0031.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Submit {
    String name() default "无提交";
}
