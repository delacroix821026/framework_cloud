package com.fw.common.entity;

import brave.Span;
import brave.Tracer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

@Getter
@Setter
public class RestError {
    private HttpStatus status;
    private String code;
    private String applicationName;
    private String message;
    private String developerMessage;
    private String moreInfoUrl;
    private Throwable throwable;
    private String spanId;
    private String traceId;
    private RestError restError;

    public RestError() {
    }

    public RestError(HttpStatus status, String code, String applicationName, String message, String developerMessage, String moreInfoUrl,
                     Throwable throwable, String traceId, String spanId, RestError restError) {
        if (status == null) {
            throw new NullPointerException("HttpStatus argument cannot be null.");
        }
        this.status = status;
        this.code = code;
        this.applicationName = applicationName;
        this.message = message;
        this.developerMessage = developerMessage;
        this.moreInfoUrl = moreInfoUrl;
        this.throwable = throwable;
        this.traceId = traceId;
        this.spanId = spanId;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof RestError) {
            RestError re = (RestError) o;
            return ObjectUtils.nullSafeEquals(getStatus(), re.getStatus()) &&
                    getCode() == re.getCode() &&
                    ObjectUtils.nullSafeEquals(getMessage(), re.getMessage()) &&
                    ObjectUtils.nullSafeEquals(getDeveloperMessage(), re.getDeveloperMessage()) &&
                    ObjectUtils.nullSafeEquals(getMoreInfoUrl(), re.getMoreInfoUrl()) &&
                    ObjectUtils.nullSafeEquals(getThrowable(), re.getThrowable()) &&
                    ObjectUtils.nullSafeEquals(getTraceId(), re.getTraceId()) &&
                    ObjectUtils.nullSafeEquals(getSpanId(), re.getSpanId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        //noinspection ThrowableResultOfMethodCallIgnored
        return ObjectUtils.nullSafeHashCode(new Object[]{
                getStatus(), getCode(), getMessage(), getDeveloperMessage(), getMoreInfoUrl(), getThrowable(),
                getSpanId(), getTraceId()
        });
    }

    @Override
    public String toString() {
        //noinspection StringBufferReplaceableByString
        return new StringBuilder().append(getStatus().value())
                .append(" (").append(getStatus().getReasonPhrase()).append(" )")
                .toString();
    }

    public static class Builder {

        private HttpStatus status;
        private String code;
        private String applicationName;
        private StringBuffer message = new StringBuffer();
        private String developerMessage;
        private String moreInfoUrl;
        private Throwable throwable;
        private Tracer tracer;
        private RestError restError;

        public Builder() {
        }

        public Builder setStatus(int statusCode) {
            this.status = HttpStatus.valueOf(statusCode);
            return this;
        }

        public Builder setStatus(HttpStatus status) {
            this.status = status;
            return this;
        }

        public Builder setCode(String code) {
            this.code = code;
            return this;
        }

        public Builder setApplicationName(String applicationName) {
            this.applicationName = applicationName;
            return this;
        }

        public Builder setMessage(String message) {
            this.message.append(message);
            return this;
        }

        public Builder setDeveloperMessage(String developerMessage) {
            this.developerMessage = developerMessage;
            return this;
        }

        public Builder setMoreInfoUrl(String moreInfoUrl) {
            this.moreInfoUrl = moreInfoUrl;
            return this;
        }

        public Builder setThrowable(Throwable throwable) {
            this.throwable = throwable;
            return this;
        }

        public Builder setTracer(Tracer tracer) {
            this.tracer = tracer;
            return this;
        }

        public Builder setRestError(RestError restError) {
            this.restError = restError;
            return this;
        }

        public RestError build() {
            if (this.status == null) {
                this.status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            Span span = tracer.currentSpan();
            return new RestError(this.status, this.code, this.applicationName, this.message.toString(), this.developerMessage, this.moreInfoUrl,
                    this.throwable, span.context().traceIdString(), span.context().spanIdString(), this.restError);
        }
    }
}
