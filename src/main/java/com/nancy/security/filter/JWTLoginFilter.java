package com.nancy.security.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nancy.dto.ResultDto;
import org.apache.commons.io.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nancy.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 验证用户名密码正确后，生成一个token，并将token返回给客户端
 * 该类继承自UsernamePasswordAuthenticationFilter，重写了其中的2个方法
 * attemptAuthentication ：接收并解析用户凭证。
 * successfulAuthentication ：用户成功登录后，这个方法会被调用，我们在这个方法里生成token。
 * Created by Administrator on 2017/12/23 0023.
 */
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    private CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();

    public JWTLoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    // 接收并解析用户凭证
    @Override
    @ResponseBody
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            /*User user = new ObjectMapper().readValue(req.getInputStream(), User.class);
            String username = user.getName();
            String password = user.getPwd();*/
            if (!req.getMethod().equals("POST")) {
                throw new AuthenticationServiceException("POST method support only current: " + req.getMethod());
            }
            if (null != SecurityContextHolder.getContext().getAuthentication()) {
                return SecurityContextHolder.getContext().getAuthentication();
            }
            Map<String, String> paramMap = extraParameterMap(req);
            String username = paramMap.get("name");
            String password = paramMap.get("pwd");

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            password,
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 用户成功登录后，这个方法会被调用，我们在这个方法里生成token
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        String token = Jwts.builder()
                .setSubject(auth.getName())
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000))
                .signWith(SignatureAlgorithm.HS512, "MyJwtSecret") //采用什么算法是可以自己选择的，不一定非要采用HS512
                .compact();
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(ResultDto.CODE_FAIL);
        resultDto.setMsg("登录成功！");
        res.setContentType("application/json;charset=UTF-8");
        res.addHeader("token", "Bearer" + token);
        res.getWriter().write(JSONObject.toJSONString(resultDto));
    }

    private Map<String, String> extraParameterMap(HttpServletRequest request) throws IOException {
        String contentType = StringUtils.lowerCase(request.getContentType());
        Map<String, String> paramMap = new HashMap<>();
        if (StringUtils.startsWith(contentType, MediaType.APPLICATION_JSON_VALUE)) {
            String content = IOUtils.toString(request.getInputStream(), "utf-8");
            JSONObject data = JSON.parseObject(content);
            for (String key : data.keySet()) {
                String value = data.getString(key);
                if (StringUtils.isNotBlank(value)) {
                    paramMap.put(key, value);
                }
            }
        } else {
            HttpServletRequest parameterRequest = StringUtils.startsWith(contentType, MediaType.MULTIPART_FORM_DATA_VALUE) ? multipartResolver.resolveMultipart(request) : request;
            Enumeration parameterNames = parameterRequest.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String parameterName = (String) parameterNames.nextElement();
                String parameterValue = parameterRequest.getParameter(parameterName);
                if (StringUtils.isNotBlank(parameterValue)) {
                    paramMap.put(parameterName, parameterValue);
                }
            }
        }
        return paramMap;
    }

}
