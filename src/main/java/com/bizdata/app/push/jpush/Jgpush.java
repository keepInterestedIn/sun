package com.bizdata.app.push.jpush;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizdata.config.FileUploadConfig;
import com.bizdata.config.JpushConfig;

//
import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.common.resp.DefaultResult;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

public class Jgpush {
	 protected static final Logger LOG = LoggerFactory.getLogger(Jgpush.class);  
	  
     // demo App defined in resources/jpush-api.conf   
	
    public static final String TITLE = "申通快递";  
    public static final String ALERT = "祝大家新春快乐";  
    public static final String MSG_CONTENT = "申通快递祝新老客户新春快乐";
    public static final String REGISTRATION_ID = "0900e8d85ef";  
    public static final String TAG = "tag_api";
    @Autowired
    private static JpushConfig jpushConfig;
      
    public static PushResult testSendPush(JPushClient jpushClient,PushPayload load) {  
//         jpushClient = new JPushClient(masterSecret, appKey, 3);  
        // HttpProxy proxy = new HttpProxy("localhost", 3128);  
        // Can use this https proxy: https://github.com/Exa-Networks/exaproxy  
        // For push, all you need do is to build PushPayload object.  
        //PushPayload payload = buildPushObject_all_all_alert();
        //生成推送的内容，测试全部推送  
//        PushPayload payload=buildPushObject_all_alias_alert(ALERT);
        //正式内容
    	PushResult result = null;
    	PushPayload payload= load;
        try {  
            System.out.println(payload.toString());  
            result = jpushClient.sendPush(payload);
            System.out.println(result+"................................");  
            
            LOG.info("Got result - " + result);
        } catch (APIConnectionException e) {  
            LOG.error("Connection error. Should retry later. ", e);
              
        } catch (APIRequestException e) {  
            LOG.error("Error response from JPush server. Should review and fix it. ", e);  
            LOG.info("HTTP Status: " + e.getStatus());  
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());  
            LOG.info("Msg ID: " + e.getMsgId());  
        }
        return result;
    }  
    /**
     * 
     * @param jpushClient 账号密码
     * @param theTag	标签名
     * @param toAddUsers	添加id
     * @param toRemoveUsers	删除id
     */
    public static void tagChange(JPushClient jpushClient,String theTag,Set<String> toAddUsers,Set<String> toRemoveUsers) {
    		try {
                DefaultResult result = jpushClient.addRemoveDevicesFromTag(theTag, toAddUsers, toRemoveUsers);
                System.out.println(result+"................................tag_result");
                LOG.info("Got result - " + result);
            } catch (APIConnectionException e) {  
                LOG.error("Connection error. Should retry later. ", e);
                  
            } catch (APIRequestException e) {  
                LOG.error("Error response from JPush server. Should review and fix it. ", e);  
                LOG.info("HTTP Status: " + e.getStatus());
                LOG.info("Error Code: " + e.getErrorCode());  
                LOG.info("Error Message: " + e.getErrorMessage());  
                LOG.info("Msg ID: " + e.getMsgId());
            } 
    }
    
	/**
	 * 按标签设置推送内容及跳转链接
	 * @param ALERT 显示内容
	 * @param title android标题
	 * @param extras 键值对,跳转链接等其他数据
	 * @param tag	标签
	 * @return
	 */
    public static PushPayload buildPushObject_tag(String ALERT,String title,Map<String,String> extras,String...tag) {  
        return PushPayload.newBuilder()
        		.setPlatform(Platform.all())				//设置平台
                .setAudience(Audience.tag(tag))				//设置标签
                .setNotification(Notification.newBuilder()
    					.addPlatformNotification(AndroidNotification.newBuilder()
    							.setAlert(ALERT)
    							.setTitle(title)
    							.addExtras(extras).build())	//设置android内容
    					.addPlatformNotification(IosNotification.newBuilder()
    							.setAlert(ALERT)
    							.addExtras(extras).build()).build())		//设置IOS内容
    			.setOptions(Options.newBuilder().setApnsProduction(true).build())	//设置android内容
                .build();
    }
    
    /**
     * 设置推送内容及跳转链接
     * @param ALERT 显示内容
     * @param title android标题
     * @param extras 键值对,跳转链接等其他数据
     * @return
     */
    public static PushPayload buildPushObject_tag(String ALERT,String title,Map<String,String> extras) {  
    	return PushPayload.newBuilder()
    			.setPlatform(Platform.all())				//设置平台
    			.setAudience(Audience.all())				//设置标签
    			.setNotification(Notification.newBuilder()
    					.addPlatformNotification(AndroidNotification.newBuilder()
    							.setAlert(ALERT)
    							.setTitle(title)
    							.addExtras(extras).build())	//设置android内容
    					.addPlatformNotification(IosNotification.newBuilder()
    							.setAlert(ALERT)
    							.addExtras(extras).build()).build())		//设置IOS内容
    			.setOptions(Options.newBuilder().setApnsProduction(true).build())	//设置为生产环境
    			.build();
    }
    
    
    
    public static PushPayload buildPushObject_all_all_alert(String ALERT) {  
        return PushPayload.alertAll(ALERT);  
    }  
      
    public static PushPayload buildPushObject_all_alias_alert(String ALERT) {  
        return PushPayload.newBuilder()  
                .setPlatform(Platform.all())//设置接受的平台  
                .setAudience(Audience.all())//Audience设置为all，说明采用广播方式推送，所有用户都可以接收到  
                .setNotification(Notification.alert(ALERT))
                .build();  
    }  
      
    public static PushPayload buildPushObject_android_tag_alertWithTitle(String ALERT) {  
        return PushPayload.newBuilder()  
                .setPlatform(Platform.android())  
                .setAudience(Audience.all())  
                .setNotification(Notification.android(ALERT, TITLE, null))  
                .build();  
    }  
      
    public static PushPayload buildPushObject_android_and_ios(String ALERT) {  
        return PushPayload.newBuilder()  
                .setPlatform(Platform.android_ios())  
                .setAudience(Audience.tag("tag1"))  
                .setNotification(Notification.newBuilder()  
                        .setAlert("alert content")  
                        .addPlatformNotification(AndroidNotification.newBuilder()  
                                .setTitle("Android Title").build())  
                        .addPlatformNotification(IosNotification.newBuilder()  
                                .incrBadge(1)  
                                .addExtra("extra_key", "extra_value").build())  
                        .build())  
                .build();  
    }  
      
    public static PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage(String ALERT) {  
        return PushPayload.newBuilder()  
                .setPlatform(Platform.ios())  
                .setAudience(Audience.tag_and("tag1", "tag_all"))  
                .setNotification(Notification.newBuilder()  
                        .addPlatformNotification(IosNotification.newBuilder()  
                                .setAlert(ALERT)  
                                .setBadge(5)  
                                .setSound("happy")  
                                .addExtra("from", "JPush")  
                                .build())  
                        .build())  
                 .setMessage(Message.content(MSG_CONTENT))  
                 .setOptions(Options.newBuilder()  
                         .setApnsProduction(true)  
                         .build())  
                 .build();  
    }  
      
    public static PushPayload buildPushObject_ios_audienceMore_messageWithExtras(String ALERT) {  
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())  
                .setAudience(Audience.newBuilder()  
                        .addAudienceTarget(AudienceTarget.tag("tag1", "tag2"))  
                        .addAudienceTarget(AudienceTarget.alias("alias1", "alias2"))  
                        .build())  
                .setMessage(Message.newBuilder()  
                        .setMsgContent(MSG_CONTENT)  
                        .addExtra("from", "JPush")  
                        .build())  
                .build();  
    }
}
