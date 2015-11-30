/**
 * Copyright (c) 2011-2014, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.jfinal.weixin.demo;

import java.util.List;
import java.util.Map;







import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PropKit;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.UserApi;
import com.jfinal.weixin.sdk.jfinal.MsgController;
import com.jfinal.weixin.sdk.msg.in.InImageMsg;
import com.jfinal.weixin.sdk.msg.in.InLinkMsg;
import com.jfinal.weixin.sdk.msg.in.InLocationMsg;
import com.jfinal.weixin.sdk.msg.in.InShortVideoMsg;
import com.jfinal.weixin.sdk.msg.in.InTextMsg;
import com.jfinal.weixin.sdk.msg.in.InVideoMsg;
import com.jfinal.weixin.sdk.msg.in.InVoiceMsg;
import com.jfinal.weixin.sdk.msg.in.event.InCustomEvent;
import com.jfinal.weixin.sdk.msg.in.event.InFollowEvent;
import com.jfinal.weixin.sdk.msg.in.event.InLocationEvent;
import com.jfinal.weixin.sdk.msg.in.event.InMassEvent;
import com.jfinal.weixin.sdk.msg.in.event.InMenuEvent;
import com.jfinal.weixin.sdk.msg.in.event.InQrCodeEvent;
import com.jfinal.weixin.sdk.msg.in.event.InShakearoundUserShakeEvent;
import com.jfinal.weixin.sdk.msg.in.event.InTemplateMsgEvent;
import com.jfinal.weixin.sdk.msg.in.event.InVerifyFailEvent;
import com.jfinal.weixin.sdk.msg.in.event.InVerifySuccessEvent;
import com.jfinal.weixin.sdk.msg.in.speech_recognition.InSpeechRecognitionResults;
import com.jfinal.weixin.sdk.msg.out.OutImageMsg;
import com.jfinal.weixin.sdk.msg.out.OutMusicMsg;
import com.jfinal.weixin.sdk.msg.out.OutNewsMsg;
import com.jfinal.weixin.sdk.msg.out.OutTextMsg;
import com.jfinal.weixin.sdk.msg.out.OutVoiceMsg;

/**
 * 将此 DemoController 在YourJFinalConfig 中注册路由，
 * 并设置好weixin开发者中心的 URL 与 token ，使 URL 指向该
 * DemoController 继承自父类 WeixinController 的 index
 * 方法即可直接运行看效果，在此基础之上修改相关的方法即可进行实际项目开发
 */
public class WeixinMsgController extends MsgController {
	
	private static final String helpStr = "\t机器人优化了一下：现在可以查询，新闻、天气、快递单号、食谱、星座、百科、故事、笑话、火车、成语接龙、飞机、图片。如输入：猫的图片、新闻、深圳的天气、鱼香肉丝、深圳到襄阳的火车、顺丰快递、百科周杰伦、笑话、故事等文字。更多好玩的自己慢慢玩哈";
//	private static final String helpStr ="发送 \"美女\"试试";
	
	/**
	 * 如果要支持多公众账号，只需要在此返回各个公众号对应的  ApiConfig 对象即可
	 * 可以通过在请求 url 中挂参数来动态从数据库中获取 ApiConfig 属性值
	 */
	public ApiConfig getApiConfig() {
		ApiConfig ac = new ApiConfig();
		
		// 配置微信 API 相关常量
		ac.setToken(PropKit.get("token"));
		ac.setAppId(PropKit.get("appId"));
		ac.setAppSecret(PropKit.get("appSecret"));
		
		/**
		 *  是否对消息进行加密，对应于微信平台的消息加解密方式：
		 *  1：true进行加密且必须配置 encodingAesKey
		 *  2：false采用明文模式，同时也支持混合模式
		 */
		ac.setEncryptMessage(PropKit.getBoolean("encryptMessage", false));
		ac.setEncodingAesKey(PropKit.get("encodingAesKey", "setting it in config file"));
		return ac;
	}
	
	/**
	 * 实现父类抽方法，处理文本消息
	 * 本例子中根据消息中的不同文本内容分别做出了不同的响应，同时也是为了测试 jfinal weixin sdk的基本功能：
	 *     本方法仅测试了 OutTextMsg、OutNewsMsg、OutMusicMsg 三种类型的OutMsg，
	 *     其它类型的消息会在随后的方法中进行测试
	 */
	protected void processInTextMsg(InTextMsg inTextMsg) {
		String msgContent = inTextMsg.getContent().trim();
		// 帮助提示
		if ("help".equalsIgnoreCase(msgContent) || "帮助".equals(msgContent)) {
			OutTextMsg outMsg = new OutTextMsg(inTextMsg);
			outMsg.setContent(helpStr);
			render(outMsg);
		}
		// 图文消息测试
		else if ("news".equalsIgnoreCase(msgContent)) {
			OutNewsMsg outMsg = new OutNewsMsg(inTextMsg);
			outMsg.addNews(
					"看美女",
					"看美女，就上熊熊TV啦！！！！！",
					"http://img5.duitang.com/uploads/item/201412/05/20141205174557_wFUHC.thumb.700_0.jpeg",
					"http://mp.weixin.qq.com/s?__biz=MzA4NDU4MTY0OQ==&mid=400964917&idx=1&sn=c78c5a50d02e6a2c1d2860fc63d011be#rd"
					);
			outMsg.addNews(
					"看美女",
					"看美女，就上熊熊TV啦！！！！！",
					"http://img5.duitang.com/uploads/item/201412/05/20141205174557_wFUHC.thumb.700_0.jpeg",
					"http://mp.weixin.qq.com/s?__biz=MzA4NDU4MTY0OQ==&mid=400964917&idx=1&sn=c78c5a50d02e6a2c1d2860fc63d011be#rd"
					);
			outMsg.addNews(
					"看美女",
					"看美女，就上熊熊TV啦！！！！！",
					"http://img5.duitang.com/uploads/item/201412/05/20141205174557_wFUHC.thumb.700_0.jpeg",
					"http://mp.weixin.qq.com/s?__biz=MzA4NDU4MTY0OQ==&mid=400964917&idx=1&sn=c78c5a50d02e6a2c1d2860fc63d011be#rd"
					);
			render(outMsg);
		}
		// 音乐消息测试
		else if ("music".equalsIgnoreCase(msgContent) || "音乐".equals(msgContent)) {
			OutMusicMsg outMsg = new OutMusicMsg(inTextMsg);
			outMsg.setTitle("Day By Day");
			outMsg.setDescription("建议在 WIFI 环境下流畅欣赏此音乐");
			outMsg.setMusicUrl("http://www.jfinal.com/DayByDay-T-ara.mp3");
			outMsg.setHqMusicUrl("http://www.jfinal.com/DayByDay-T-ara.mp3");
			outMsg.setFuncFlag(true);
			render(outMsg);
		}
		else if ("美女".equalsIgnoreCase(msgContent)) {
			OutNewsMsg outMsg = new OutNewsMsg(inTextMsg);
			outMsg.addNews(
					"看美女",
					"看美女，就上熊熊TV啦！！！！！",
					"http://img5.duitang.com/uploads/item/201412/05/20141205174557_wFUHC.thumb.700_0.jpeg",
					"http://mp.weixin.qq.com/s?__biz=MzA4NDU4MTY0OQ==&mid=400964917&idx=1&sn=c78c5a50d02e6a2c1d2860fc63d011be#rd"
					);
			render(outMsg);
		}
		else if ("视频教程".equalsIgnoreCase(msgContent) || "视频".equalsIgnoreCase(msgContent)) {
			renderOutTextMsg("\thttp://pan.baidu.com/s/1nt2zAT7  \t密码:824r");
		}
		else if(msgContent.contains("天气")){
			String json = HttpKit.get("http://www.tuling123.com/openapi/api?key=71370c63f4cd4db53c35771428279aab&info="+inTextMsg.getContent());
			Map map = (Map) JSON.parse(json);
			String text = map.get("text").toString();
			if( text.split(":").length > 1){
			String location = text.split(":")[0];
			String [] days = text.split(":")[1].split(";");
			OutNewsMsg outMsg = new OutNewsMsg(inTextMsg);		
			outMsg.addNews(
					location,
					"看天气",
					"http://unidust.cn/images/wechat_weather.jpg",
					"http://m.sm.cn/s?q="+msgContent
					);
			outMsg.addNews(
					days[0],
					"看天气",
					image(days[0]),
					"http://m.sm.cn/s?q="+msgContent
					);
			outMsg.addNews(
					days[1],
					"看天气",
					image(days[1]),
					"http://m.sm.cn/s?q="+msgContent
					);
			outMsg.addNews(
					days[2],
					"看天气",
					image(days[2]),
					"http://m.sm.cn/s?q="+msgContent
					);
			render(outMsg);
			}else{
			render(text);
			}
		}
		// 其它文本消息直接返回原值 + 帮助提示
		else {
			String openId = inTextMsg.getFromUserName();
			ApiResult info = UserApi.getUserInfo(openId);
			Map user = (Map) JSON.parse(info.toString());
			System.out.println("---------------------"+user.get("nickname")+"-发送了:"+inTextMsg.getContent()+"---------------------");
			String json = HttpKit.get("http://www.tuling123.com/openapi/api?key=71370c63f4cd4db53c35771428279aab&info="+inTextMsg.getContent());
			Map map = (Map) JSON.parse(json);
			int code = Integer.parseInt(map.get("code").toString());
			switch (code) {
			//文字类
			case  100000:
				renderOutTextMsg(map.get("text") .toString());
				break;
			//链接类
			case 200000:
				renderOutTextMsg(map.get("text") .toString()+":"+map.get("url") .toString());
				break;
			//新闻
			case 302000:
				OutNewsMsg outMsg = new OutNewsMsg(inTextMsg);
				List newsList = (List) JSON.parse(map.get("list").toString());
				for(int i = 0; i < 4; i++){
					Map news = (Map) JSON.parse(newsList.get(i).toString());
					outMsg.addNews(
							news.get("article") .toString(),
							news.get("source") .toString(),
							"http://unidust.cn/images/weixin-xinwen.png",
							news.get("detailurl") .toString()
							);
				}			
				render(outMsg);
				break;
			//菜谱
			case 308000:
				OutNewsMsg outMsg2 = new OutNewsMsg(inTextMsg);
				List newsList2 = (List) JSON.parse(map.get("list").toString());
				for(int i = 0; i < 4; i++){
					Map news = (Map) JSON.parse(newsList2.get(i).toString());
					outMsg2.addNews(
							news.get("info") .toString(),
							news.get("name") .toString(),
							news.get("icon") .toString(),
							news.get("detailurl") .toString()
							);
				}			
				render(outMsg2);
				break;
			default:
				renderOutTextMsg("R U Kidding Me？");
				break;	
			}
			
		}
	}
	//获取天气图标
   protected static String image(String weather){
	   String w= "";
	   if(weather.contains("晴")){
		   w = "http://s1.rr.itc.cn/weather/0828/m/Sunny_d.png";
	   }
	   if(weather.contains("云")){
		   w = "http://s1.rr.itc.cn/weather/0828/m/Cloudy_d_s.png";
	   }
	   if(weather.contains("云") && weather.contains("晴")){
		   w = "http://s1.rr.itc.cn/weather/0828/m/SunnyCloudy_d.png";
	   }
		if(weather.contains("雪")){
			w = "http://s1.rr.itc.cn/weather/0828/m/Snow_d.png";
		}
		if(weather.contains("阴")){
			w = "http://s1.rr.itc.cn/weather/0828/m/Overcast_d_s.png";
		}
		if(weather.contains("雨")){
			w = "http://s1.rr.itc.cn/weather/0828/m/LightRain_d_s.png";
		}
	   return w;
   }
	/**
	 * 实现父类抽方法，处理图片消息
	 */
	protected void processInImageMsg(InImageMsg inImageMsg) {
		OutImageMsg outMsg = new OutImageMsg(inImageMsg);
		// 将刚发过来的图片再发回去
		outMsg.setMediaId(inImageMsg.getMediaId());
		render(outMsg);
	}
	
	/**
	 * 实现父类抽方法，处理语音消息
	 */
	protected void processInVoiceMsg(InVoiceMsg inVoiceMsg) {
		OutVoiceMsg outMsg = new OutVoiceMsg(inVoiceMsg);
		// 将刚发过来的语音再发回去
		outMsg.setMediaId(inVoiceMsg.getMediaId());
		render(outMsg);
	}
	
	/**
	 * 实现父类抽方法，处理视频消息
	 */
	protected void processInVideoMsg(InVideoMsg inVideoMsg) {
		/* 腾讯 api 有 bug，无法回复视频消息，暂时回复文本消息代码测试
		OutVideoMsg outMsg = new OutVideoMsg(inVideoMsg);
		outMsg.setTitle("OutVideoMsg 发送");
		outMsg.setDescription("刚刚发来的视频再发回去");
		// 将刚发过来的视频再发回去，经测试证明是腾讯官方的 api 有 bug，待 api bug 却除后再试
		outMsg.setMediaId(inVideoMsg.getMediaId());
		render(outMsg);
		*/
		OutTextMsg outMsg = new OutTextMsg(inVideoMsg);
		outMsg.setContent("\t视频消息已成功接收，该视频的 mediaId 为: " + inVideoMsg.getMediaId());
		render(outMsg);
	}
	
	/**
	 * 实现父类抽方法，处理地址位置消息
	 */
	protected void processInLocationMsg(InLocationMsg inLocationMsg) {
		OutTextMsg outMsg = new OutTextMsg(inLocationMsg);
		outMsg.setContent("已收到地理位置消息:" +
							"\nlocation_X = " + inLocationMsg.getLocation_X() +
							"\nlocation_Y = " + inLocationMsg.getLocation_Y() + 
							"\nscale = " + inLocationMsg.getScale() +
							"\nlabel = " + inLocationMsg.getLabel());
		render(outMsg);
	}
	
	/**
	 * 实现父类抽方法，处理链接消息
	 * 特别注意：测试时需要发送我的收藏中的曾经收藏过的图文消息，直接发送链接地址会当做文本消息来发送
	 */
	protected void processInLinkMsg(InLinkMsg inLinkMsg) {
		OutNewsMsg outMsg = new OutNewsMsg(inLinkMsg);
		outMsg.addNews("链接消息已成功接收", "链接使用图文消息的方式发回给你，还可以使用文本方式发回。点击图文消息可跳转到链接地址页面，是不是很好玩 :)" , "http://mmbiz.qpic.cn/mmbiz/zz3Q6WSrzq1ibBkhSA1BibMuMxLuHIvUfiaGsK7CC4kIzeh178IYSHbYQ5eg9tVxgEcbegAu22Qhwgl5IhZFWWXUw/0", inLinkMsg.getUrl());
		render(outMsg);
	}
	
	/**
	 * 实现父类抽方法，处理关注/取消关注消息
	 */
	protected void processInFollowEvent(InFollowEvent inFollowEvent) {
		OutTextMsg outMsg = new OutTextMsg(inFollowEvent);
		outMsg.setContent("感谢关注 huv微信开发服务号，为您奉献更多的精彩 :)  " + helpStr);
		// 如果为取消关注事件，将无法接收到传回的信息
		render(outMsg);
	}
	
	/**
	 * 实现父类抽方法，处理扫描带参数二维码事件
	 */
	protected void processInQrCodeEvent(InQrCodeEvent inQrCodeEvent) {
		OutTextMsg outMsg = new OutTextMsg(inQrCodeEvent);
		outMsg.setContent("processInQrCodeEvent() 方法测试成功");
		render(outMsg);
	}
	
	/**
	 * 实现父类抽方法，处理上报地理位置事件
	 */
	protected void processInLocationEvent(InLocationEvent inLocationEvent) {
		OutTextMsg outMsg = new OutTextMsg(inLocationEvent);
		String jingdu = inLocationEvent.getLatitude();
		String weidu = inLocationEvent.getLongitude();
		String api = "http://api.map.baidu.com/geocoder/v2/?ak=rmrRmQs58L06WVGxzBK3d4Ty&location="+jingdu+","+weidu+"&output=json&pois=1";
		System.out.println(HttpKit.get(api));
		JSONObject obj = (JSONObject) JSONObject.parse(HttpKit.get(api));
		String location =obj.getJSONObject("result").get("formatted_address").toString();
		outMsg.setContent("huv的微信欢迎你,我可以和你聊天，随便发消息给我吧！\n 你的位置大致为："+location +"\n\n"+helpStr);
		render(outMsg);
	}
	
	/**
	 * 实现父类抽方法，处理自定义菜单事件
	 */
	protected void processInMenuEvent(InMenuEvent inMenuEvent) {
		String key = inMenuEvent.getEventKey();
		if(key.equals("V1001_HELLO_WORLD")){
			renderOutTextMsg("Hello,baby!");
		}else if(key.equals("V1001_GOOD")){
			renderOutTextMsg("谢谢，我收到了你的赞!记得常请我吃饭");
		}
		
	}
	
	/**
	 * 实现父类抽方法，处理接收语音识别结果
	 */
	protected void processInSpeechRecognitionResults(InSpeechRecognitionResults inSpeechRecognitionResults) {
		renderOutTextMsg("语音识别结果： " + inSpeechRecognitionResults.getRecognition());
	}
	
	// 处理接收到的模板消息是否送达成功通知事件
	protected void processInTemplateMsgEvent(InTemplateMsgEvent inTemplateMsgEvent) {
		String status = inTemplateMsgEvent.getStatus();
		renderOutTextMsg("模板消息是否接收成功：" + status);
	}
	
	// 实现父类抽方法，处理小视频消息
	protected void processInShortVideoMsg(InShortVideoMsg inShortVideoMsg) {
		renderOutTextMsg("收到小视频消息，mediaId 为： " + inShortVideoMsg.getMediaId());
	}
	
	// 实现父类抽方法，处理客服消息事件：接入会话（kf_create_session）、关闭会话（kf_close_session）、转接会话（kf_switch_session）
	protected void processInCustomEvent(InCustomEvent inCustomEvent) {
		renderOutTextMsg("收到客服务消息事件： " + inCustomEvent.getEvent());
	}
	
	// 实现父类抽方法，处理群发任务结束后的回调事件
	protected void processInMassEvent(InMassEvent inMassEvent) {
		renderOutTextMsg("群发消息任务结束");
	}
	
	protected void processInShakearoundUserShakeEvent(InShakearoundUserShakeEvent inShakearoundUserShakeEvent) {
		renderOutTextMsg("摇一摇：\n" +
				"uuid = " + inShakearoundUserShakeEvent.getUuid() +
				"distance = " + inShakearoundUserShakeEvent.getDistance() +
				"" + inShakearoundUserShakeEvent.getAroundBeaconList()
				);
	}
	
	protected void processInVerifySuccessEvent(InVerifySuccessEvent inVerifySuccessEvent) {
		renderOutTextMsg(inVerifySuccessEvent.toString());
	}
	
	protected void processInVerifyFailEvent(InVerifyFailEvent inVerifyFailEvent) {
		renderOutTextMsg(inVerifyFailEvent.toString());
	}
}






