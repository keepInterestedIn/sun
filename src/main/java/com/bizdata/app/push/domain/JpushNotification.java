package com.bizdata.app.push.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.bizdata.app.push.constant.PushEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.sdevil507.base.JpaUUIDBaseEntity;

@Entity
@Table(name = "app_jpush_notification")
@Data
@EqualsAndHashCode(callSuper = true)
public class JpushNotification extends JpaUUIDBaseEntity {
	public JpushNotification(){
		this.platform = "all";
		this.audience = "all";
		this.alert = "";
	}
	//===========推送对象内容================//
	/**
	 * 必填，推送平台设置
	 * 其关键字分别为："android", "ios", "winphone"
	 * 实例："platform" : ["android", "ios"]
	 * 实例："platform" : "all"
	 */
	private String platform;
	/**
	 * 必填，推送设备指定
	 * 默认为"all"
	 */
	private String audience;
	private String notification;//通知内容体,与 message 一起二者必须有其一，可以二者并存
	/**
	 * 推送内容
	 * 为""时不展示
	 * 必填
	 */
	private String alert;
	private String cid;				//用于防止 api 调用端重试造成服务端的重复推送而定义的一个标识符。
	/**
	 * 可选参数,键值对
	 * "options": {
        "time_to_live": 60,
        "apns_production": false,
        "apns_collapse_id":"jiguang_test_201706011100"
    }
	 */
	private String options;
	//==========推送成功返回值==========//
	/**
	 * 推送对象标识
	 */
	private int msg_id;
	/**
	 * 用来作为 API 调用标识
	 */
	private int sendno;

}
