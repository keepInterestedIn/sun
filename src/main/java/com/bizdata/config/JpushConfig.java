package com.bizdata.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "jpush")
public class JpushConfig {
//	public JpushConfig() {
//		this.appKey = "13cda0e8a5b74305f393d007";
//		this.masterSecret = "f3d0fc52ebdc6818539b4f7d";
//	}
	/**
	 * 极光推送应用appkey
	 */
	private String appKey;
	/**
	 * 极光推送应用密码
	 */
	private String masterSecret;
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getMasterSecret() {
		return masterSecret;
	}
	public void setMasterSecret(String masterSecret) {
		this.masterSecret = masterSecret;
	}
	
	
}
