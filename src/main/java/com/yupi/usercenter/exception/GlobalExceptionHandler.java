package com.yupi.usercenter.exception;

import com.yupi.usercenter.common.Result;
import com.yupi.usercenter.common.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author dkhaohao
 * @Title:
 * @Package
 * @Description: 全局异常处理
 * @date 2024/3/50:14
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获businessException
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public Result<?> businessExceptionHandler(BusinessException e) {
        log.error("businessException: " + e.getMessage(), e);
        return Result.fail(null,e.getCode(), e.getMessage(), e.getDescription());
    }

    //https://github.com/liyupi

    @ExceptionHandler(RuntimeException.class)
    public Result<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return Result.fail(null,ResultCodeEnum.SYSTEM_ERROR);
    }


}
