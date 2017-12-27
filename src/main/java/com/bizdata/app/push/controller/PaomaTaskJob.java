package com.bizdata.app.push.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bizdata.app.activity.paoma.service.ActivityPaoMaService;
import com.bizdata.app.activity.paoma.service.PaoMaMapPersonService;
import com.bizdata.app.push.jpush.Jgpush;
import com.bizdata.config.JpushConfig;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.PushPayload;

/**
 * 定时推送跑马圈奖排名
 * @author Administrator
 *
 */
@Component("paomataskJob")
public class PaomaTaskJob {
	
	private final Logger logger = LoggerFactory.getLogger(PaomaTaskJob.class);
	private static boolean isRunning = false;
//	private static String appKey = "13cda0e8a5b74305f393d007";			//极光推送appkey
//	private static String masterSecret = "f3d0fc52ebdc6818539b4f7d";	//极光推送密码
	private static String content = "您的运动排名已更新!";
	@Autowired
	private ActivityPaoMaService activityPaoMaService;
	@Autowired
	private PaoMaMapPersonService paoMaMapPersonService;
	@Autowired
    private JpushConfig jpushConfig;
	
	@Scheduled(cron= "0 0 22 * * *")//推送时间
	public void job1() {
        if (!isRunning) {
            isRunning = true;
            //极光推送账号密码
            
            logger.info("开始执行任务。");
            //=====================最近两次的活动id
            List<String> activityids = activityPaoMaService.getLastids(2);
            String n="";//现在的id
            String y="";//前一个id
            if(activityids.size()==0) {
            	n = "";
            	y = "";
            }else if(activityids.size()==1) {
            	n = activityids.get(0);
            	y = "";
            }else {
            	n = activityids.get(0);//现在的id
            	y = activityids.get(1);//前一个id
            }
            //=====================最近两次用户的变化
            List<String> nowids = new ArrayList<String>();
            List<String> laseids = new ArrayList<String>();
            List<String> result = new ArrayList<String>();
//            Set<String> remove = new HashSet<String>();
            //最近的一次的参加用户
            if(null != n) {
            	nowids.addAll(paoMaMapPersonService.getJpushid(n));
            	nowids.removeAll(Collections.singleton(null));
            }
            //上次的参加用户
            if(null!=y) {
            	laseids.addAll(paoMaMapPersonService.getJpushid(y));
            	laseids.removeAll(Collections.singleton(null));
            }
            result.addAll(nowids);
            result.retainAll(laseids);//取交集
            nowids.removeAll(result);//新添加tag
            laseids.removeAll(result);//删除tag
            String theTag = "Paoma";
            int size = nowids.size()>=laseids.size()? nowids.size():laseids.size();
            try{
	            //==============================修改用户标签,由于标签单次修改数量为0-1000,多次修改
	            JPushClient jpushClient= new JPushClient(jpushConfig.getMasterSecret(),jpushConfig.getAppKey(), 3);
	            for(int k = 0;k<Math.ceil((float)size/ (float)1000);k++) {
	            	Set<String> ss = new HashSet<String>(nowids.subList(k*1000, k*1000+1000));
	            	Set<String> kk = new HashSet<String>(laseids.subList(k*1000, k*1000+1000));
	            	Jgpush.tagChange(jpushClient, "paoma", ss, kk);
	            }
	            //==============================配置内容,发送
	            Map<String,String> map = new HashMap<String,String>();
	            map.put("addAdress", "paoma");													//设置跳转地址
	            PushPayload payload = Jgpush.buildPushObject_tag(content,"跑马圈奖",map,"paoma");//配置发送内容
	            Jgpush.testSendPush(jpushClient,payload);//发送给用户
	            isRunning = false;
            }catch (Exception e){
            	e.printStackTrace();
            	logger.error("极光推送过程出现错误,请检查推送过程!");
            	isRunning = false;
            }
            logger.info("任务执行结束。");
        } else {  
            logger.info("上一次任务执行还未结束，本次任务不能执行。");
        }
    }
}
