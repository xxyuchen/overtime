package com.nancy.redis;

/**
 * Created by Administrator on 2017/12/28 0028.
 */
public interface RedisService {
    public void set(String key, Object value);

    public Object get(String key);

    public void del(String key);
}
