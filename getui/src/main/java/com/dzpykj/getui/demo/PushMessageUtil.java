package com.dzpykj.getui.demo;

import java.util.ArrayList;
import java.util.List;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.gexin.rp.sdk.template.style.Style1;

/**   
 * 个推消息推送服务工具类
 */
public class PushMessageUtil {
	private static final String GE_XIN_URL =  "http://sdk.open.api.igexin.com/apiex.htm";
	private static final String GE_XIN_APP_ID =  "2OVTXYJhXw8nsIz87x1sx7";
	private static final String GE_XIN_APP_KEY =  "pozfE8LXAa5u0RppS6Uyr9";
	private static final String GE_XIN_MASTER_SECRET =  "QSXxUBzuoE8URwSyHwOfi9";
	
	public static void main(String[] args) {
		String title = "qwer", content = "asdf", messageContent = "zxcv", logoUrl = "https://pjl.obs.myhwclouds.com/static/sharePage/pjl_app/img/pjl_app_logo.png";
		pushStationNotice(title,content,messageContent,logoUrl);
	}
	
	/**
	 * 推送全站消息
	 */
	public static boolean pushStationNotice(String title, String content, String messageContent, String logoUrl){
		
		IGtPush push = new IGtPush(GE_XIN_URL, GE_XIN_APP_KEY, GE_XIN_MASTER_SECRET);
		
		NotificationTemplate template = new NotificationTemplate();
        template.setAppId(GE_XIN_APP_ID);
        template.setAppkey(GE_XIN_APP_KEY);
        template.setTransmissionContent(messageContent);
        template.setTransmissionType(1);
        /*APNPayload payload = new APNPayload();
        payload.setContentAvailable(1);
        payload.setSound("default");
        // payload.setCategory("$由客户端定义");
        */
        
        Style1 style = new Style1();
        style.setTitle(title);
        style.setText(content);
        style.setLogoUrl(logoUrl);
        style.setRing(true);
        style.setVibrate(true);
        style.setClearable(true);
        template.setStyle(style);
        
        APNPayload payload = new APNPayload();
        payload.setAutoBadge("+1");
        payload.setContentAvailable(1);
        payload.setSound("default");
        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
        alertMsg.setBody(content);
        alertMsg.setActionLocKey("ActionLockey");
        alertMsg.setLocKey("LocKey");
        alertMsg.addLocArg("loc-args");
        alertMsg.setLaunchImage("launch-image");
        alertMsg.setTitle(title);
        payload.setAlertMsg(alertMsg);
        
        template.setAPNInfo(payload);
        List<String> appIds = new ArrayList<String>();
        appIds.add(GE_XIN_APP_ID);
        
        AppMessage message = new AppMessage();
        message.setData(template);
        message.setAppIdList(appIds);
        message.setOffline(true);
        message.setOfflineExpireTime(1000 * 3600 * 72);
        
        IPushResult ret = push.pushMessageToApp(message);
        String result = ret.getResponse().get("result").toString();
        System.out.println(ret.getResponse().toString());
		return result.equals("ok") ? true : false;
	}
	
	/**
     * 定向推送用户消息
     * @param  tilte 通知栏标题
     * @param  Content 通知栏内容
     * @param  messageContent 透传消息主题
     * @param  alias 用户名
     * @param  logoUrl 通知栏网络图标
     * @return boolean 推送结果
     */
    public static boolean pushNotificationsAndPassThroughMessages(String tilte, String Content, String messageContent, String alias, String logoUrl){
    	
    	 IGtPush push = new IGtPush(GE_XIN_APP_KEY, GE_XIN_MASTER_SECRET);
    	 
         NotificationTemplate template = new NotificationTemplate(); 
         template.setAppId(GE_XIN_APP_ID);
         template.setAppkey(GE_XIN_APP_KEY);
         template.setTransmissionContent(messageContent);
         template.setTransmissionType(1);
         
         Style1 style = new Style1();
         style.setTitle(tilte);
         style.setText(Content);
         style.setLogoUrl(logoUrl);
         style.setRing(true);
         style.setVibrate(true);
         style.setClearable(true);
         template.setStyle(style);
         
         APNPayload payload = new APNPayload();
         payload.setAutoBadge("+1");
         payload.setContentAvailable(1);
         payload.setSound("default");
         APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
         alertMsg.setBody(Content);
         alertMsg.setActionLocKey("ActionLockey");
         alertMsg.setLocKey("LocKey");
         alertMsg.addLocArg("loc-args");
         alertMsg.setLaunchImage("launch-image");
         alertMsg.setTitle(tilte);
         payload.setAlertMsg(alertMsg);
         template.setAPNInfo(payload);
         
         SingleMessage message = new SingleMessage();
         
         message.setData(template);
         message.setOffline(true);
         message.setOfflineExpireTime(1000 * 3600 * 24);
         message.setPushNetWorkType(0);
         
         Target target = new Target();
         target.setAppId(GE_XIN_APP_ID);
         target.setAlias(alias);
         
         IPushResult ret = null;
         try {
             ret = push.pushMessageToSingle(message, target);
             
         } catch (RequestException e) {
             e.printStackTrace();
             ret = push.pushMessageToSingle(message, target, e.getRequestId());
         }
         if (ret != null) {
             System.out.println(ret.getResponse().toString());
         } else {
             System.out.println("服务器响应异常");
         }
         String result = ret.getResponse().get("result").toString();
 		 return result.equals("ok") ? true : false;
    }
	
    /**
     * 定向推送用户_透传消息
     * @param  content 消息简介(苹果专用)
     * @param  messageContent 透传消息主题
     * @param  alias 用户名
     * @param  logoUrl 通知栏网络图标
     * @return boolean 推送结果
     */
    public static boolean pushPassThroughMessages(String content, String messageContent, String alias, String logoUrl){
    	
    	 IGtPush push = new IGtPush(GE_XIN_APP_KEY, GE_XIN_MASTER_SECRET);
    	 TransmissionTemplate template = new TransmissionTemplate();
	     template.setAppId(GE_XIN_APP_ID);
	     template.setAppkey(GE_XIN_APP_KEY);
	     template.setTransmissionContent(messageContent);
	     template.setTransmissionType(1);
	     
	     APNPayload payload = new APNPayload();
	     payload.setContentAvailable(1);
	     payload.setSound("default");
	     payload.setAlertMsg(new APNPayload.SimpleAlertMsg(content));
	     template.setAPNInfo(payload);
	     
         SingleMessage message = new SingleMessage();
         message.setData(template);
         message.setOffline(true);
         message.setOfflineExpireTime(1000 * 3600 * 72);
         message.setPushNetWorkType(0);
         Target target = new Target();
         target.setAppId(GE_XIN_APP_ID);
         target.setAlias(alias);
         
         IPushResult ret = null;
         try {
             ret = push.pushMessageToSingle(message, target);
             
         } catch (RequestException e) {
             e.printStackTrace();
             ret = push.pushMessageToSingle(message, target, e.getRequestId());
         }
         if (ret != null) {
             System.out.println(ret.getResponse().toString());
         } else {
             System.out.println("服务器响应异常");
         }
         String result = ret.getResponse().get("result").toString();
 		 return result.equals("ok") ? true : false;
    }
}