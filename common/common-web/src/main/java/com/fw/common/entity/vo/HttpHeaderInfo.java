package com.fw.common.entity.vo;


public class HttpHeaderInfo {

	private String terminalType; // 终端类型（注：pc、pad、mobile）
	private String deviceNumber; // 设备序列号
	private String deviceMac; // 设备mac地址
	private String deviceImei; // 设备国际身份码
	private String deviceModel; // 设备型号
	private String osName; // 操作系统名称（注：ios、android）
	private String osVersion; // 操作系统版本号
	private String appName; // 应用名称
	private String appId; // 应用唯一标识
	private String appVersionCode; // 应用版本号
	private String appVersionName; // 应用版本名
	private Integer apiVersion; //接口版本
	private String personSid; //用户标识

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	public String getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	public String getDeviceMac() {
		return deviceMac;
	}

	public void setDeviceMac(String deviceMac) {
		this.deviceMac = deviceMac;
	}

	public String getDeviceImei() {
		return deviceImei;
	}

	public void setDeviceImei(String deviceImei) {
		this.deviceImei = deviceImei;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppVersionCode() {
		return appVersionCode;
	}

	public void setAppVersionCode(String appVersionCode) {
		this.appVersionCode = appVersionCode;
	}

	public String getAppVersionName() {
		return appVersionName;
	}

	public void setAppVersionName(String appVersionName) {
		this.appVersionName = appVersionName;
	}

	public Integer getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(Integer apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getPersonSid() {
		return personSid;
	}

	public void setPersonSid(String personSid) {
		this.personSid = personSid;
	}


}
