package com.jfinal.weixin.demo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.jfinal.kit.PropKit;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.GroupsApi;
import com.jfinal.weixin.sdk.api.MediaApi;
import com.jfinal.weixin.sdk.api.MediaApi.MediaType;
import com.jfinal.weixin.sdk.api.MediaArticles;
import com.jfinal.weixin.sdk.api.MenuApi;
import com.jfinal.weixin.sdk.api.UserApi;
import com.jfinal.weixin.sdk.jfinal.ApiController;

public class WeixinApiController extends ApiController {
	
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
	
	/*
	public void index() {
		render("/api/index.html");
	}
	*/
	
	/**
	 * 获取公众号菜单
	 */
	public void getMenu() {
		ApiResult apiResult = MenuApi.getMenu();
		if (apiResult.isSucceed())
			renderText(apiResult.getJson());
		else
			renderText(apiResult.getErrorMsg());
	}
	/**
	 * 创建公众号菜单
	 */
	public void createMenu() {
		String menu = PropKit.get("menu");
		System.out.println(menu);
		ApiResult apiResult = MenuApi.createMenu(menu);
		if (apiResult.isSucceed())
			renderText(apiResult.getJson());
		else
			renderText(apiResult.getErrorMsg());
	}
	/**
	 * 获取公众号关注用户
	 */
	public void getFollowers() {
		ApiResult apiResult = UserApi.getFollows();
		// TODO 用 jackson 解析结果出来
		renderText(apiResult.getJson());
	}
	/**
	 * 群发接口
	 */
	public void sendAll(){
		String message = PropKit.get("messgae2");
		ApiResult apiResult = MessageApi.sendAll(message);
		renderText(apiResult.getJson());
	}
	/**
	 * 群发信息状态
	 */
	public void infoStatus(){
		String message_id = getPara("messId");
		ApiResult apiResult = MessageApi.get(message_id);
		renderText(apiResult.getJson());
	}
	/**
	 * 分组接口
	 */
	public void gourp(){
		ApiResult apiResult = GroupsApi.get();
		renderText(apiResult.getJson());
	}
	/**
	 * 获取用户信息
	 */
	public void getUserInfomation(){
		String openId = getPara("openId");
		ApiResult apiResult = UserApi.getUserInfo(openId);
		renderText(apiResult.getJson());
	}
	/**
	 * 新增其他类型永久素材
	 */
	public void addSucai(){
	File file = new File("D:\\sucai\\QQ图片20151127093548.jpg");
	ApiResult apiResult = MediaApi.addMaterial(file);
	renderText(apiResult.getJson());
	}
	/**
	 * 新增图文永久素材
	 */
	public void addTuwenSucai(){
	MediaArticles media = new MediaArticles();
	media.setTitle("Happy Day");
	media.setThumb_media_id("Lfmxj0RJO4G9WnVQ5CP04TmlQxnmvi9WQRskmZ9UaX0");
	media.setDigest("快乐星期五");
	media.setShow_cover_pic(true);
	media.setContent("快乐每一天");
	media.setAuthor("胡威");
	media.setContent_source_url("www.xxtv.online");
	List<MediaArticles> list = new ArrayList<MediaArticles>();
	list.add(media);
	ApiResult apiResult = MediaApi.addNews(list);
	renderText(apiResult.getJson());
	}
	/**
	 * 获取音频永久素材列表
	 */
	public void getVoiceSucai(){
	ApiResult apiResult = MediaApi.batchGetMaterial(MediaType.VOICE,0,10);
	renderText(apiResult.getJson());
	}
	/**
	 * 获取图片永久素材列表
	 */
	public void getImageSucai(){
	ApiResult apiResult = MediaApi.batchGetMaterial(MediaType.IMAGE,0,10);
	renderText(apiResult.getJson());
	}
	/**
	 * 获取图文永久素材列表
	 */
	public void getNewsSucai(){
	ApiResult apiResult = MediaApi.batchGetMaterial(MediaType.NEWS,0,10);
	renderText(apiResult.getJson());
	}
	
}