package com.newland.financial.p2p.common.handler;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.newland.financial.p2p.common.exception.BaseRuntimeException;
import feign.FeignException;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j
@RestControllerAdvice
public class DefalutErrorHandler {
    @Autowired
    protected ExceptionMapping exceptionMapping;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BaseRuntimeException.class)
    public RestError handlerBaseRuntimeException(BaseRuntimeException ex) {
        if(ex.getRestError() != null) {
            return ex.getRestError();
        }
        RestError.Builder erb = new RestError.Builder();
        erb.setCode(ex.getErrorCode());
        erb.setMessage(exceptionMapping.getValue(ex.getErrorCode(), ex.getArgs()));
        //erb.setMoreInfoUrl("/abc.htm");
        //erb.setThrowable(ex);
        return erb.build();
    }


    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HystrixBadRequestException.class)
    public RestError handlerHystrixBadRequestException(HystrixBadRequestException ex) {
        BaseRuntimeException baseRuntimeException = (BaseRuntimeException) ex.getCause();
        return baseRuntimeException.getRestError();
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public RestError handlerException(Exception ex) {
        log.error("未捕获的错误", ex);
        RestError.Builder erb = new RestError.Builder();
        erb.setCode("000");
        erb.setMessage("系统异常");
        return erb.build();
    }
}
