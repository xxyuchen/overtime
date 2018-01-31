package com.nancy.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.nancy.annotation.Submit;
import com.nancy.dto.ResultDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
* @Author TangZhen
* @Date 2018/1/31 0031 14:08
* @Description
*/
@Slf4j
public class DemoInterceptor implements HandlerInterceptor {
    /**
     * 请求重复的标记，key是用户标识和url，value是标记
     */
    private Map<String, Boolean> duplicateRecordMap = new HashMap<>();

    private ScheduledExecutorService scheduledExecutorService =
            new ScheduledThreadPoolExecutor(100, r -> {
                Thread t = new Thread(r);
                t.setDaemon(true);
                t.setName("duplicate-record");
                return t;
            });

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        System.out.println(request.getParameterMap());
        log.info(String.format("请求参数, url: %s, method: %s, uri: %s, params: %s", url, method, uri, queryString));
        if(StringUtils.isBlank(url)){
            return true;
        }

        String key = url;
        synchronized (key){
            Boolean submit = duplicateRecordMap.get(key);
            if(BooleanUtils.isTrue(submit)){
                log.debug("拦截key={}", key);
                ResultDto resultDto = new ResultDto();
                resultDto.setCode(ResultDto.CODE_FAIL);
                resultDto.setMsg("您的操作太快了！");
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(JSONObject.toJSONString(resultDto));
                return false;
            }else {
                duplicateRecordMap.put(key, Boolean.TRUE);
                log.debug("记录key={}", key);
                scheduledExecutorService.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        synchronized (key) {
                            duplicateRecordMap.remove(key);
                            log.info("超时移除key={}", key);
                        }
                    }
                }, 3, TimeUnit.SECONDS);
                return true;
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
        String url = request.getRequestURL().toString();
        if(StringUtils.isBlank(url)){
            return;
        }
        scheduledExecutorService.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized(url){
                    duplicateRecordMap.remove(url);
                    log.info("移除key={}", url);
                }
            }
        },1,TimeUnit.SECONDS);
        HandlerMethod handlerMethod = (HandlerMethod) o;
        Method method = handlerMethod.getMethod();
        final Submit submit = method.getAnnotation(Submit.class);
        log.info("-------------{}",submit);
    }
}
