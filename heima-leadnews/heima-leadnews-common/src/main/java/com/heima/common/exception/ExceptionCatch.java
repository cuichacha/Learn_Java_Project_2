package com.heima.common.exception;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author cuichacha
 * @date 1/24/21 21:20
 */
@ControllerAdvice
@Log4j2
public class ExceptionCatch {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult catchException(Exception exception) {
        exception.printStackTrace();
        log.error("catch exception:{}", exception.getMessage());
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }
}
