package com.fw.common.model;

import com.fw.common.model.entity.BaseUserInfo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Data
public class UserInfo extends BaseUserInfo implements Serializable {
    private static final long serialVersionUID = -1;

    protected List<String> roles = new ArrayList<>();

    protected List<String> promissions = new ArrayList<>();

}
