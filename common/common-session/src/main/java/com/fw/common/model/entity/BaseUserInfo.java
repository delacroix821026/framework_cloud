package com.fw.common.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class BaseUserInfo implements Serializable {
    private static final long serialVersionUID = -1;

    private Long id;

    /** 用户名称. */
    private String username;

    /** 用户密码. */
    private String password;

    /** 性别：0 未知， 1男， 1 女. */
    private Byte gender;

    /** 生日. */
    private Date birthday;

    /** 最近一次登录时间. */
    private Date lastLoginTime;

    /** 最近一次登录IP地址. */
    private String lastLoginIp;

    /** 0 普通用户，1 VIP用户，2 高级VIP用户. */
    private Byte userLevel;

    /** 用户昵称或网络名称. */
    private String nickname;

    /** 用户手机号码. */
    private String mobile;

    /** 用户头像图片. */
    private String avatar;

    /** 微信登录openid. */
    private String weixinOpenid;

    /** 微信登录会话KEY. */
    private String sessionKey;

    /** 0 可用, 1 禁用, 2 注销. */
    private Byte status;

    /** 创建时间. */
    private Date addTime;

    /** 更新时间. */
    private Date updateTime;

    /** 逻辑删除. */
    private Boolean deleted;

}
