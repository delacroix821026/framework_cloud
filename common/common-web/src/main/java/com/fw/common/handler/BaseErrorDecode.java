package com.fw.common.handler;

import com.fw.common.entity.RestError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.fw.common.exception.BaseRuntimeException;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.io.IOException;

@Slf4j
@Configuration
public class BaseErrorDecode implements ErrorDecoder {
    public Exception decode(String methodKey, Response response) {
        if (response.status() == HttpStatus.BAD_REQUEST.value()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                RestError restError = mapper.readValue(response.body().asReader(), RestError.class);
                return new HystrixBadRequestException(restError.getMessage(), new BaseRuntimeException(restError));

            } catch (IOException e) {
                log.error("IOException",e);
            }
            //log.info("reason" + response.reason());

        }
        return FeignException.errorStatus(methodKey, response);
    }
}
