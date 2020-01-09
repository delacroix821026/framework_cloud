package com.fw.common.exception;

public class DfsException extends BaseRuntimeException {
    public DfsException(String key) {
        super(key);
    }

    public DfsException(String key, String... args) {
        super(key, args);
    }

}
