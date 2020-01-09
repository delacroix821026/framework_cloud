package com.fw.common.handler;

import brave.Tracer;
import com.fw.common.entity.RestError;
import com.fw.common.exception.BaseRuntimeException;
import com.fw.common.exception.FwFeignException;
import com.fw.common.exception.SessionTimeOutException;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

@Slf4j
@RestControllerAdvice
public class BaseDefalutErrorHandler implements Ordered {
    @Override
    public int getOrder() {
        return 2;
    }

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    protected ExceptionMapping exceptionMapping;

    @Autowired
    protected Tracer tracer;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HystrixBadRequestException.class)
    public RestError handlerHystrixBadRequestException(HystrixBadRequestException ex) {
        BaseRuntimeException baseRuntimeException = (BaseRuntimeException) ex.getCause();
        RestError.Builder erb = new RestError.Builder();
        String msg = ex.getMessage();

        erb.setRestError(baseRuntimeException.getRestError());
        msg +=  "," + baseRuntimeException.getMessage();

        erb.setTracer(tracer);
        erb.setMessage(baseRuntimeException.getRestError().getMessage());
        erb.setDeveloperMessage(msg);
        erb.setApplicationName(applicationName);
        return erb.build();
    }

    @ResponseStatus(code = HttpStatus.METHOD_FAILURE)
    @ExceptionHandler(HystrixRuntimeException.class)
    public RestError handlerException(HystrixRuntimeException ex) {
        log.error("调用Feign错误", ex);
        RestError.Builder erb = new RestError.Builder();
        String msg = ex.getMessage();

        if( ex.getCause() instanceof FwFeignException) {
            erb.setRestError(((FwFeignException) ex.getCause()).getRestError());
        } else {
            msg +=  "," + ex.getCause().getMessage();
        }

        erb.setTracer(tracer);
        erb.setCode("001");
        erb.setMessage("请求失败，请关闭后重试或联系管理员。(" + applicationName + "):");
        erb.setDeveloperMessage(msg);
        erb.setApplicationName(applicationName);
        return erb.build();
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BaseRuntimeException.class)
    public RestError handlerBaseRuntimeException(BaseRuntimeException ex) {
        log.debug("handlerBaseRuntimeException", ex);
        if(ex.getRestError() != null) {
            return ex.getRestError();
        }
        RestError.Builder erb = new RestError.Builder();
        erb.setTracer(tracer);
        erb.setCode(ex.getErrorCode());
        erb.setMessage(exceptionMapping.getValue(ex.getErrorCode(), ex.getArgs()));
        erb.setApplicationName(applicationName);
        //erb.setMoreInfoUrl("/abc.htm");
        //erb.setThrowable(ex);
        return erb.build();
    }

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(SessionTimeOutException.class)
    public RestError handlerException(SessionTimeOutException ex) {
        log.error("Session Timeout", ex);
        RestError.Builder erb = new RestError.Builder();
        erb.setTracer(tracer);
        erb.setCode("001");
        erb.setMessage("Session Timeout");
        erb.setApplicationName(applicationName);
        return erb.build();
    }

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public RestError handlerException(AuthenticationException ex) {
        throw ex;
    }

    @ResponseStatus(code = HttpStatus.METHOD_FAILURE)
    @ExceptionHandler(Exception.class)
    public RestError handlerException(Exception ex) {
        log.error("未捕获的错误", ex);
        RestError.Builder erb = new RestError.Builder();
        erb.setTracer(tracer);
        erb.setCode("000");
        erb.setMessage("系统异常(" + applicationName + "):" + ex.getClass().getName());
        erb.setApplicationName(applicationName);
        return erb.build();
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServletRequestBindingException.class)
    public RestError handlerException(ServletRequestBindingException ex) {
        RestError.Builder erb = new RestError.Builder();
        erb.setTracer(tracer);
        erb.setCode("1004");
        erb.setMessage(ex.getMessage());
        erb.setApplicationName(applicationName);
        return erb.build();
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestError handlerException(MethodArgumentNotValidException ex) {
        RestError.Builder erb = new RestError.Builder();
        erb.setTracer(tracer);
        erb.setCode("1004");
        erb.setApplicationName(applicationName);
        for(ObjectError objectError : ex.getBindingResult().getAllErrors()) {
            objectError.getDefaultMessage();
            erb.setMessage(objectError.getDefaultMessage());
        }

        return erb.build();
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public RestError handlerException(ValidationException ex) {
        RestError.Builder erb = new RestError.Builder();
        erb.setTracer(tracer);
        erb.setCode("1004");
        erb.setApplicationName(applicationName);
        //erb.setMessage(ex.getMessage());
        if(ex instanceof ConstraintViolationException) {
            ConstraintViolationException exception = (ConstraintViolationException) ex;
            if(exception.getConstraintViolations() != null) {
                StringBuffer buffer = new StringBuffer();
                for(ConstraintViolation violation : exception.getConstraintViolations()) {
                    buffer.append(violation.getPropertyPath().toString())
                            .append(": ")
                            .append(violation.getMessage());
                }
                erb.setMessage(buffer.toString());
            }
        }


        return erb.build();
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public RestError handlerException(BindException ex) {
        RestError.Builder erb = new RestError.Builder();
        erb.setTracer(tracer);
        erb.setCode("1004");
        erb.setApplicationName(applicationName);
        StringBuffer buffer = new StringBuffer();
        for(ObjectError error : ex.getAllErrors()) {
            buffer.append(error.toString()).append(";  ");
        }
        erb.setMessage(buffer.toString());
        return erb.build();
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public RestError handlerException(MethodArgumentTypeMismatchException ex) {
        RestError.Builder erb = new RestError.Builder();
        erb.setTracer(tracer);
        erb.setCode("1004");
        erb.setApplicationName(applicationName);
        StringBuffer buffer = new StringBuffer();
        buffer.append("Parameter: ").append(ex.getName()).append("with value: ")
                .append(ex.getValue()).append(".   ").append(ex.getMessage());

        erb.setMessage(buffer.toString());
        return erb.build();
    }

}
