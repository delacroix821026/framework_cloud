package com.fw.common.sso.extractor;

import com.alibaba.fastjson.JSONObject;
import com.fw.common.model.LoginUserInfo;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;

import java.util.Map;

public class FwPrincipalExtractor implements PrincipalExtractor {
    @Override
    public Object extractPrincipal(Map<String, Object> map) {
        LoginUserInfo user = JSONObject.parseObject(JSONObject.toJSONString(map), LoginUserInfo.class);
        return user;
    }
}
