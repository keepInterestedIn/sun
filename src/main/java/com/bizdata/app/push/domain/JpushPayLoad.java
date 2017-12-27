package com.bizdata.app.push.domain;

/**
 * 
 * @author Administrator
 *
 */
public class JpushPayLoad {
	
	public JpushPayLoad(){
		this.platform = "all";
		this.audience = "all";
		this.alert = "";
	}
	private int msg_id;
	private int sendno;
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
	private String notification;	//通知内容体,与 message 一起二者必须有其一，可以二者并存
	/**
	 * 为""时不展示
	 * 必填
	 */
		private String alert;
		private String cid;				//用于防止 api 调用端重试造成服务端的重复推送而定义的一个标识符。
		/**
		 * 键值对，附加字段
		 * "extras" : {
	                      "news_id" : 134, 
	                      "my_key" : "a value"
	                 }
		 */
		private String extras;
	private String message;			//消息内容体,与 notification 一起二者必须有其一，可以二者并存

}
