package com.fw.common.exception;

import com.fw.common.entity.RestError;
import feign.FeignException;
import feign.Response;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FwFeignException extends FeignException {
    private RestError restError;

    protected FwFeignException(String message, Throwable cause) {
        super(HttpStatus.METHOD_FAILURE.value(), message, cause);
    }

    protected FwFeignException(String message) {
        super(HttpStatus.METHOD_FAILURE.value(), message);
    }

    protected FwFeignException(int status, String message) {
        super(status, message);
    }

    protected FwFeignException(int status, String message, RestError restError) {
        this(status, message);
        this.restError = restError;
    }

    public static FeignException errorStatus(String methodKey, Response response, RestError error) {
        String message = String.format("status %s reading %s", response.status(), methodKey);
        return new FwFeignException(response.status(), message, error);
    }
}
