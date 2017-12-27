package com.nancy.controller;

import com.nancy.dto.ResultDto;
import com.nancy.model.User;
import com.nancy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/22 0022.
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectById", produces = {"application/json;charset=UTF-8"})
    public ResultDto selectById(Integer id){
        ResultDto resultDto = new ResultDto();
        resultDto.setMsg(ResultDto.MESS_SUCC);
        resultDto.setCode(ResultDto.CODE_SUCC);
        try {
            User user = userService.selectById(id);
            Map<String,Object> map = new HashMap<>();
            map.put("data",user);
            resultDto.setData(map);
        }catch (Exception e){
            e.printStackTrace();
            resultDto.setMsg(ResultDto.MESS_FAIL);
            resultDto.setCode(ResultDto.CODE_FAIL);
            log.error("UserController.selectById is fial!");
        }
        return resultDto;
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/signup", produces = {"application/json;charset=UTF-8"})
    public ResultDto signup(User user){
        ResultDto resultDto = new ResultDto();
        resultDto.setMsg(ResultDto.MESS_SUCC);
        resultDto.setCode(ResultDto.CODE_SUCC);
        try {
            User userN = new User();
            userN.setName(user.getName());
            User userH = userService.findByAttr(userN);
            if(null != userH){
                resultDto.setMsg("用户已存在");
                resultDto.setCode(ResultDto.CODE_FAIL);
                return resultDto;
            }
            //user.setPwd(DigestUtils.md5DigestAsHex((user.getPassword()).getBytes()));
            if(userService.insert(user)<=0){
                resultDto.setMsg(ResultDto.MESS_FAIL);
                resultDto.setCode(ResultDto.CODE_FAIL);
                return resultDto;
            }
        }catch (Exception e){
            e.printStackTrace();
            resultDto.setMsg(ResultDto.MESS_FAIL);
            resultDto.setCode(ResultDto.CODE_FAIL);
            log.error("UserController.signup is fial!");
        }
        return resultDto;
    }
    /**
     * 登录
     * @param user
     * @return
     *//*
    @ResponseBody
    @RequestMapping(value = "/login", produces = {"application/json;charset=UTF-8"})
    public ResultDto login(User user){
        ResultDto resultDto = new ResultDto();
        resultDto.setMsg(ResultDto.MESS_SUCC);
        resultDto.setCode(ResultDto.CODE_SUCC);
        try {
            User userH = userService.findByAttr(user);
            if(null == userH){
                resultDto.setMsg("密码错误");
                resultDto.setCode(ResultDto.CODE_FAIL);
                return resultDto;
            }
        }catch (Exception e){
            e.printStackTrace();
            resultDto.setMsg(ResultDto.MESS_FAIL);
            resultDto.setCode(ResultDto.CODE_FAIL);
            log.error("UserController.login is fial!");
        }
        return resultDto;
    }*/

}
