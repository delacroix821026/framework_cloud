package com.fw.common.entity;

import javax.servlet.http.HttpServletRequest;

import com.fw.common.entity.vo.HttpHeaderInfo;

public class RequestHeader {

	
	public static HttpHeaderInfo getHttpHeaderInfo(HttpServletRequest request) {
		HttpHeaderInfo hhi = new HttpHeaderInfo();
		if (null != request) {
			hhi.setTerminalType(request.getHeader("terminal-type") != null ? request.getHeader("terminal-type") : "unknown");
			hhi.setDeviceNumber(request.getHeader("device-number") != null ? request.getHeader("device-number") : "unknown");
			hhi.setDeviceMac(request.getHeader("device-mac") != null ? request.getHeader("device-mac") : "unknown");
			hhi.setDeviceImei(request.getHeader("device-imei") != null ? request.getHeader("device-imei") : "unknown");
			hhi.setDeviceModel(request.getHeader("device-model") != null ? request.getHeader("device-model") : "unknown");
			hhi.setOsName(request.getHeader("os-name") != null ? request.getHeader("os-name") : "unknown");
			hhi.setOsVersion(request.getHeader("os-version") != null ? request.getHeader("os-version") : "unknown");
			hhi.setAppName(request.getHeader("app-name") != null ? request.getHeader("app-name") : "unknown");
			hhi.setAppId(request.getHeader("app-id") != null ? request.getHeader("app-id") : "unknown");
			hhi.setAppVersionCode(request.getHeader("app-version-code") != null ? request.getHeader("app-version-code") : "unknown");
			hhi.setAppVersionName(request.getHeader("app-version-name") != null ? request.getHeader("app-version-name") : "unknown");
			hhi.setPersonSid(request.getHeader("person-id") != null ? request.getHeader("person-id") : "unknown");
		} else {
			hhi.setTerminalType("unknown");
			hhi.setDeviceNumber("unknown");
			hhi.setDeviceMac("unknown");
			hhi.setDeviceImei("unknown");
			hhi.setDeviceModel("unknown");
			hhi.setOsName("unknown");
			hhi.setOsVersion("unknown");
			hhi.setAppName("unknown");
			hhi.setAppId("unknown");
			hhi.setAppVersionCode("unknown");
			hhi.setAppVersionName("unknown");
			hhi.setPersonSid("unknown");
		}
		return hhi;
	}
}
