package com.grgbanking.alicloud.user.controller;

import com.grgbanking.alicloud.common.utils.GrgbankingEnums;
import com.grgbanking.alicloud.common.utils.ResultMap;
import com.grgbanking.alicloud.common.utils.ResultMapUtil;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author machao
 * 全局异常处理类
 */
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResultMap errorHandler(Exception e){
        return ResultMapUtil.error(e);
    }
}
