package com.nancy.security.exception;

import com.nancy.dto.ResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2018/1/31 0031.
 */
@ControllerAdvice
@Slf4j
public class UnifiedExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultDto defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        log.error("错误信息：", e);
        ResultDto resultDto = new ResultDto();
        resultDto.setMsg(e.getMessage());
        if (e instanceof org.springframework.web.servlet.NoHandlerFoundException) {
            resultDto.setCode(404);
        }else {
            resultDto.setCode(500);
        }
        return resultDto;
    }
}
