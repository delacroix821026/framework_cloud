package com.fw.common.sso.Security;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class RequestMapProperties {
    private Map<String, Collection<String>> requestMaps = new HashMap<>();
}
