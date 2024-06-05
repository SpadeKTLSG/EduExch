package com.shop.common.handler;


import com.shop.common.constant.MessageConstants;
import com.shop.common.exception.BaseException;
import com.shop.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 *
 * @author SK
 * @date 2024/06/05
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //TODO test
    /**
     * 捕获业务异常
     *
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex) {
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }


    /**
     * 处理SQL异常
     *
     */
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        //Duplicate entry 'zhangsan' for key 'employee.idx_username'
        String message = ex.getMessage();
        if (message.contains("Duplicate entry")) { //判断是否是重复插入数据
            String[] split = message.split(" "); //按照空格分割
            String username = split[2]; //获取用户名
            String msg = username + MessageConstants.ACCOUNT_ALIVED;
            return Result.error(msg);
        } else {
            return Result.error(MessageConstants.UNKNOWN_ERROR);
        }
    }
}
