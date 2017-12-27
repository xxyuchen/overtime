package com.nancy.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable{

    @Setter
    @Getter
    private Integer id;

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private String pwd;

    @Setter
    @Getter
    private Date createTime;

}