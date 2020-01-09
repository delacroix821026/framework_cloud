package com.fw.gateway.authority;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class RequestMapProperties {
    private Map<String, String> requestMaps = new HashMap<>();
}
