package com.nancy.controller;

import com.nancy.annotation.Submit;
import com.nancy.dto.ResultDto;
import com.nancy.model.User;
import com.nancy.redis.RedisService;
import com.nancy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/12/22 0022.
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisService redisService;

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Submit(name = "有标识")
    @ResponseBody
    @RequestMapping(value = "/selectById", produces = {"application/json;charset=UTF-8"})
    //(value = "users",key = "'id_'.concat(#root.args[0])")
    public ResultDto selectById(Integer id){
        //String key = "userId_"+id;
        /*ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            ResultDto resultDto = (ResultDto) operations.get(key);
            log.info("从缓存中获取用户{}的信息",id);
            return resultDto;
        }*/
        /*ResultDto db = (ResultDto) redisService.get(key);
        if(null!=db){
            return db;
        }*/
        ResultDto resultDto = new ResultDto();
        resultDto.setMsg(ResultDto.MESS_SUCC);
        resultDto.setCode(ResultDto.CODE_SUCC);
        User user = userService.selectById(id);
        Map<String,Object> map = new HashMap<>();
        map.put("data",user);
        resultDto.setData(map);
        // 插入缓存
        //operations.set(key, resultDto, 120, TimeUnit.SECONDS);
        //redisService.set(key,resultDto);
        //log.info("将用户{}信息存入缓存",id);
        return resultDto;
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/signup", produces = {"application/json;charset=UTF-8"},method = RequestMethod.POST)
    public ResultDto signup(User user){
        ResultDto resultDto = new ResultDto();
        resultDto.setMsg(ResultDto.MESS_SUCC);
        resultDto.setCode(ResultDto.CODE_SUCC);
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

        return resultDto;
    }
    /**
     * 编辑
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/update", produces = {"application/json;charset=UTF-8"})
    public ResultDto update(User user){
        ResultDto resultDto = new ResultDto();
        resultDto.setMsg(ResultDto.MESS_SUCC);
        resultDto.setCode(ResultDto.CODE_SUCC);
        if(userService.update(user)<=0){
            resultDto.setMsg(ResultDto.MESS_FAIL);
            resultDto.setCode(ResultDto.CODE_FAIL);
            return resultDto;
        }
        redisService.del("userId_"+user.getId());
        log.info("删除用户{}缓存",user.getId());

        return resultDto;
    }
    /**
     * 测试----
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/demo", produces = {"application/json;charset=UTF-8"})
    public ResultDto demo(){
        ResultDto resultDto = new ResultDto();
        resultDto.setMsg(ResultDto.MESS_SUCC);
        resultDto.setCode(ResultDto.CODE_SUCC);
        Map<String,Object> map = new HashMap<>(1);
        map.put("data","测试接口");
        resultDto.setData(map);
        return resultDto;
    }

}
