package com.shop.common.config;


import com.shop.common.exception.BaseException;
import com.shop.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局自定义异常处理
 *
 * @author SK
 * @date 2024/06/05
 */
@Slf4j
@RestControllerAdvice
public class WebExceptionAdvice {


    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e) {

        if (e instanceof BaseException) {
            log.error("自定义异常 -> ", e);
            return Result.error(e.getMessage());
        }

        log.error(e.toString(), e);
        return Result.error("未预见之服务器异常");
    }

}
