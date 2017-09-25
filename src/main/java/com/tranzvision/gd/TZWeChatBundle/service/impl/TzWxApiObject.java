package com.tranzvision.gd.TZWeChatBundle.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.wechart.TzGetAccessToken;
import com.tranzvision.gd.util.wechart.TzWxMaterialUtil;
import com.tranzvision.gd.util.wechart.TzWxMessageMassSendUtil;
import com.tranzvision.gd.util.wechart.TzWxTagsUtil;
import com.tranzvision.gd.util.wechart.TzWxTemplateMessageUtil;
import com.tranzvision.gd.util.wechart.TzWxUserUtil;

@Service
public class TzWxApiObject {

	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private TzGetAccessToken tzGetAccessToken;
	
	
	/**
	 * 获取微信appsecret
	 * @param orgId		//机构ID
	 * @param appId		//微信APPID
	 * @return
	 */
	private String getWxAppsecret(String orgId, String appId){
		String appsecret  ="";
		try{
			String sql = "select TZ_WX_SECRET from PS_TZ_WX_APPSE_TBL where TZ_JG_ID=? and TZ_WX_APPID=?";
			appsecret = sqlQuery.queryForObject(sql, new Object[]{ orgId,appId }, "String");
			appsecret = appsecret == null ? "" : appsecret;
		}catch(Exception e){
			e.printStackTrace();
		}
		return appsecret;
	}
	
	
	
	/**
	 * 获取微信用户列表
	 * 当公众号关注者数量超过10000时，可通过填写next_openid的值，从而多次拉取列表的方式来满足需求。
	 * @param orgId			机构ID
	 * @param appid			微信公众号APPID
	 * @param nextOpenid	拉取列表的最后一个用户的OPENID,第一次拉取或从头拉取传空即可
	 * @return
	 *  正确返回：
	 *  {
	 *  	"total":2,				//关注该公众账号的总用户数
	 *  	"count":2,				//拉取的OPENID个数，最大值为10000
	 *  	"data":{
	 *  		"openid":["","OPENID1","OPENID2"]		//列表数据，OPENID的列表
	 *  	},
	 *  	"next_openid":"NEXT_OPENID"					//拉取列表的最后一个用户的OPENID
	 *  }
	 *  
	 *  错误返回：
	 *  {
	 *  	"errcode":"-1",					//errcode-错误码，不一定是-1，也可能是微信返回的错误码
	 *  	"errmsg":"invalid appid"
	 *  }
	 */
	public Map<String,Object> getUserList(String orgId, String appid, String nextOpenid){
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(appid == null || "".equals(appid)){
			returnMap.put("errcode", "-1");
			returnMap.put("errmsg", "参数错误，APPID不能为空");
			return returnMap;
		}else{
			//获取机构下appid对应的appsecret
			String appsecret = this.getWxAppsecret(orgId, appid);
			
			if(!"".equals(appsecret) && appsecret != null){
				//获取access_token
				String access_token = "";
				try {
					access_token = tzGetAccessToken.getBaseAccessToken(appid, appsecret, "GZ", false);
				} catch (TzException e) {
					e.printStackTrace();
					access_token = "";
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败，"+e.getMessage());
					return returnMap;
				}
				
				if(!"".equals(access_token)){
					try {
						String result = TzWxUserUtil.getUserList(access_token, nextOpenid);
						jacksonUtil.json2Map(result);
						returnMap = jacksonUtil.getMap();
					} catch (TzException e) {
						e.printStackTrace();
						returnMap.put("errcode", "-1");
						returnMap.put("errmsg", "获取关注用户列表失败，"+e.getMessage());
						return returnMap;
					}
				}else{
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败。");
					return returnMap;
				}
			}else{
				returnMap.put("errcode", "-1");
				returnMap.put("errmsg", "获取机构下APPID["+appid+"]对应APPSECRET失败。");
				return returnMap;
			}
		}
		
		return returnMap;
	}
	
	
	
	/**
	 * 获取微信用户信息
	 * @param orgId		机构ID
	 * @param appid		微信公众号APPID
	 * @param openid	微信用户OPENID
	 * @param lang		返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
	 * @return
	 * 正确返回：
	 *  {
	 *     "subscribe": 1, 									//用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
	 *	   "openid": "o6_bmjrPTlm6_2sgVt7hMZOPfL2M", 		//用户的标识，对当前公众号唯一
	 *	   "nickname": "Band", 								//用户的昵称
	 *	   "sex": 1, 										//用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	 *	   "language": "zh_CN", 							//用户的语言，简体中文为zh_CN
	 *	   "city": "广州", 									//用户所在城市
	 *	   "province": "广东", 								//用户所在省份
	 *	   "country": "中国", 								//用户所在国家
	 *	   "headimgurl":  "http://wx.qlogo.cn/...xCfe/0",	//用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
	 *	   "subscribe_time": 1382694957,					//用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
	 *	   "unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL"		//只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
	 *	   "remark": "",									//公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注
	 *	   "groupid": 0,									//用户所在的分组ID（兼容旧的用户分组接口）
	 *	   "tagid_list":[128,2]								//用户被打上的标签ID列表
	 *  }
	 *  
	 *  错误返回：
	 *  {
	 *  	"errcode":"-1",					//errcode-错误码，不一定是-1，也可能是微信返回的错误码
	 *  	"errmsg":"invalid appid"
	 *  }
	 */
	public Map<String,Object> getUserInfo(String orgId, String appid, String openid, String lang){
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(appid == null || "".equals(appid)){
			returnMap.put("errcode", "-1");
			returnMap.put("errmsg", "参数错误，APPID不能为空");
			return returnMap;
		}else{
			//获取机构下appid对应的appsecret
			String appsecret = this.getWxAppsecret(orgId, appid);
			
			if(!"".equals(appsecret) && appsecret != null){
				//获取access_token
				String access_token = "";
				try {
					access_token = tzGetAccessToken.getBaseAccessToken(appid, appsecret, "GZ", false);
				} catch (TzException e) {
					e.printStackTrace();
					access_token = "";
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败，"+e.getMessage());
					return returnMap;
				}
				
				if(!"".equals(access_token)){
					try {
						String result = TzWxUserUtil.getUserInfo(access_token, openid, lang);
						jacksonUtil.json2Map(result);
						
						returnMap = jacksonUtil.getMap();
					} catch (TzException e) {
						e.printStackTrace();
						returnMap.put("errcode", "-1");
						returnMap.put("errmsg", "获取用户信息失败，"+e.getMessage());
						return returnMap;
					}
				}else{
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败。");
					return returnMap;
				}
			}else{
				returnMap.put("errcode", "-1");
				returnMap.put("errmsg", "获取机构下APPID["+appid+"]对应APPSECRET失败。");
				return returnMap;
			}
		}
		
		return returnMap;
	}
	
	
	/**
	 * 创建标签
	 * @param orgId		机构ID
	 * @param appid		微信APPID
	 * @param tagName	需要创建的标签名称
	 * @return
	 * 正确返回：
	 *  {
	 * 	   "tag":{
	 *		    "id":134,			//标签id
	 *		    "name":"广东"		//标签名称
	 *	   }
	 *  }
	 * 
	 * 错误返回：
	 *  {
	 *  	"errcode":"-1",					//errcode-错误码，不一定是-1，也可能是微信返回的错误码
	 *  	"errmsg":"invalid appid"
	 *  }
	 */
	public Map<String,Object> createTag(String orgId, String appid, String tagName){
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(appid == null || "".equals(appid)){
			returnMap.put("errcode", "-1");
			returnMap.put("errmsg", "参数错误，APPID不能为空");
			return returnMap;
		}else{
			//获取机构下appid对应的appsecret
			String appsecret = this.getWxAppsecret(orgId, appid);
			
			if(!"".equals(appsecret) && appsecret != null){
				//获取access_token
				String access_token = "";
				try {
					access_token = tzGetAccessToken.getBaseAccessToken(appid, appsecret, "GZ", false);
				} catch (TzException e) {
					e.printStackTrace();
					access_token = "";
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败，"+e.getMessage());
					return returnMap;
				}
				
				if(!"".equals(access_token)){
					try {
						String result = TzWxTagsUtil.createTag(access_token, tagName);
						jacksonUtil.json2Map(result);
						returnMap = jacksonUtil.getMap();
					} catch (TzException e) {
						e.printStackTrace();
						returnMap.put("errcode", "-1");
						returnMap.put("errmsg", "创建标签["+tagName+"]失败，"+e.getMessage());
						return returnMap;
					}
				}else{
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败。");
					return returnMap;
				}
			}else{
				returnMap.put("errcode", "-1");
				returnMap.put("errmsg", "获取机构下APPID["+appid+"]对应APPSECRET失败。");
				return returnMap;
			}
		}
		
		return returnMap;
	}
	
	
	
	/**
	 * 获取公众号已创建的标签
	 * @param orgId		机构ID
	 * @param appid		微信APPID
	 * @return
	 * 正确返回：
	 * 	{
	 *	  "tags":[
	 *		  	{
	 *		      "id":1,						//标签ID
	 *		      "name":"每天一罐可乐星人",			//标签名称
	 *		      "count":20 					//此标签下粉丝数			
	 *			},{
	 *			  "id":2,
	 *			  "name":"星标组",
	 *			  "count":10
	 *			},
	 *			......
	 *	  ]
	 *	}
	 *
	 * 错误返回：
	 *  {
	 *  	"errcode":"-1",					//errcode-错误码，不一定是-1，也可能是微信返回的错误码
	 *  	"errmsg":"invalid appid"
	 *  }
	 */
	public Map<String, Object> getAllTags(String orgId, String appid){
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(appid == null || "".equals(appid)){
			returnMap.put("errcode", "-1");
			returnMap.put("errmsg", "参数错误，APPID不能为空");
			return returnMap;
		}else{
			//获取机构下appid对应的appsecret
			String appsecret = this.getWxAppsecret(orgId, appid);
			
			if(!"".equals(appsecret) && appsecret != null){
				//获取access_token
				String access_token = "";
				try {
					access_token = tzGetAccessToken.getBaseAccessToken(appid, appsecret, "GZ", false);
				} catch (TzException e) {
					e.printStackTrace();
					access_token = "";
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败，"+e.getMessage());
					return returnMap;
				}
				
				if(!"".equals(access_token)){
					try {
						String result = TzWxTagsUtil.getAllTags(access_token);
						jacksonUtil.json2Map(result);
						returnMap = jacksonUtil.getMap();
					} catch (TzException e) {
						e.printStackTrace();
						returnMap.put("errcode", "-1");
						returnMap.put("errmsg", "获取已创建标签失败，"+e.getMessage());
						return returnMap;
					}
				}else{
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败。");
					return returnMap;
				}
			}else{
				returnMap.put("errcode", "-1");
				returnMap.put("errmsg", "获取机构下APPID["+appid+"]对应APPSECRET失败。");
				return returnMap;
			}
		}
		
		return returnMap;
	}
	
	
	
	
	/**
	 * 编辑标签
	 * @param orgId			机构ID
	 * @param appid			微信APPID
	 * @param tagId			标签ID
	 * @param tagName		标签名称
	 * @return
	 *  返回说明：
	 *  {
	 *	  "errcode":0,		//返回0，修改成功，其他修改失败
	 *	  "errmsg":"ok"
	 *	}
	 */
	public Map<String,Object> editTagNameByTagID(String orgId, String appid, String tagId, String tagName){
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(appid == null || "".equals(appid)){
			returnMap.put("errcode", "-1");
			returnMap.put("errmsg", "参数错误，APPID不能为空");
			return returnMap;
		}else{
			//获取机构下appid对应的appsecret
			String appsecret = this.getWxAppsecret(orgId, appid);
			
			if(!"".equals(appsecret) && appsecret != null){
				//获取access_token
				String access_token = "";
				try {
					access_token = tzGetAccessToken.getBaseAccessToken(appid, appsecret, "GZ", false);
				} catch (TzException e) {
					e.printStackTrace();
					access_token = "";
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败，"+e.getMessage());
					return returnMap;
				}
				
				if(!"".equals(access_token)){
					try {
						String result = TzWxTagsUtil.editTagNameByTagID(access_token, tagId, tagName);
						
						jacksonUtil.json2Map(result);
						returnMap = jacksonUtil.getMap();
					} catch (TzException e) {
						e.printStackTrace();
						returnMap.put("errcode", "-1");
						returnMap.put("errmsg", "修改标签失败，"+e.getMessage());
						return returnMap;
					}
				}else{
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败。");
					return returnMap;
				}
			}else{
				returnMap.put("errcode", "-1");
				returnMap.put("errmsg", "获取机构下APPID["+appid+"]对应APPSECRET失败。");
				return returnMap;
			}
		}
		
		return returnMap;
	}
	
	
	
	
	/**
	 * 删除标签
	 * @param orgId			机构ID
	 * @param appid			微信APPID
	 * @param tagId			标签ID
	 * @return
	 *  返回说明：
	 *  {
	 *	  "errcode":0,		//返回0，删除成功，其他删除失败
	 *	  "errmsg":"ok"
	 *	}
	 */
	public Map<String,Object> deleteTagByTagID(String orgId, String appid, String tagId){
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(appid == null || "".equals(appid)){
			returnMap.put("errcode", "-1");
			returnMap.put("errmsg", "参数错误，APPID不能为空");
			return returnMap;
		}else{
			//获取机构下appid对应的appsecret
			String appsecret = this.getWxAppsecret(orgId, appid);
			
			if(!"".equals(appsecret) && appsecret != null){
				//获取access_token
				String access_token = "";
				try {
					access_token = tzGetAccessToken.getBaseAccessToken(appid, appsecret, "GZ", false);
				} catch (TzException e) {
					e.printStackTrace();
					access_token = "";
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败，"+e.getMessage());
					return returnMap;
				}
				
				if(!"".equals(access_token)){
					try {
						String result = TzWxTagsUtil.deleteTagByTagID(access_token, tagId);
						
						jacksonUtil.json2Map(result);
						returnMap = jacksonUtil.getMap();
					} catch (TzException e) {
						e.printStackTrace();
						returnMap.put("errcode", "-1");
						returnMap.put("errmsg", "删除标签失败，"+e.getMessage());
						return returnMap;
					}
				}else{
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败。");
					return returnMap;
				}
			}else{
				returnMap.put("errcode", "-1");
				returnMap.put("errmsg", "获取机构下APPID["+appid+"]对应APPSECRET失败。");
				return returnMap;
			}
		}
		
		return returnMap;
	}
	
	
	
	/**
	 * 获取标签下粉丝列表
	 * @param orgId			机构ID
	 * @param appid			微信APPID
	 * @param tagId			标签ID
	 * @param nextOpenid	拉取列表最后一个用户的openid
	 * @return
	 *  正确返回：
	 *  {
	 *	  	"count":2,											//这次获取的粉丝数量
	 *	    "data":{											//粉丝列表
	 *			"openid":[
	 *			    "ocYxcuAEy30bX0NXmGn4ypqx3tI0",
	 *			    "ocYxcuBt0mRugKZ7tGAHPnUaOW7Y"
	 *			 ]
	 *		 },
	 *	  	"next_openid":"ocYxcuBt0mRugKZ7tGAHPnUaOW7Y"  		//拉取列表最后一个用户的openid
	 *	}
	 *
	 * 错误返回
	 *  {
	 *  	"errcode":"-1",					//errcode-错误码，不一定是-1，也可能是微信返回的错误码
	 *  	"errmsg":"invalid appid"
	 *  }
	 */
	public Map<String,Object> getTagUserList(String orgId, String appid, String tagId, String nextOpenid){
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(appid == null || "".equals(appid)){
			returnMap.put("errcode", "-1");
			returnMap.put("errmsg", "参数错误，APPID不能为空");
			return returnMap;
		}else{
			//获取机构下appid对应的appsecret
			String appsecret = this.getWxAppsecret(orgId, appid);
			
			if(!"".equals(appsecret) && appsecret != null){
				//获取access_token
				String access_token = "";
				try {
					access_token = tzGetAccessToken.getBaseAccessToken(appid, appsecret, "GZ", false);
				} catch (TzException e) {
					e.printStackTrace();
					access_token = "";
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败，"+e.getMessage());
					return returnMap;
				}
				
				if(!"".equals(access_token)){
					try {
						String result = TzWxTagsUtil.getTagUserList(access_token, tagId, nextOpenid);
						
						jacksonUtil.json2Map(result);
						returnMap = jacksonUtil.getMap();
					} catch (TzException e) {
						e.printStackTrace();
						returnMap.put("errcode", "-1");
						returnMap.put("errmsg", "获取标签下粉丝列表失败，"+e.getMessage());
						return returnMap;
					}
				}else{
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败。");
					return returnMap;
				}
			}else{
				returnMap.put("errcode", "-1");
				returnMap.put("errmsg", "获取机构下APPID["+appid+"]对应APPSECRET失败。");
				return returnMap;
			}
		}
		
		return returnMap;
	}
	
	
	
	/**
	 * 批量为用户打标签
	 * @param orgId		机构ID
	 * @param appid		微信APPID
	 * @param tagId		标签ID
	 * @param openIds	微信用户OPENID List
	 * @return
	 *  返回说明：
	 *  {
	 *	  "errcode":0,		//返回0，打标签成功，其他失败
	 *	  "errmsg":"ok"
	 *	}
	 */
	public Map<String,Object> batchTagging(String orgId, String appid, String tagId, List<String> openIds){
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(appid == null || "".equals(appid)){
			returnMap.put("errcode", "-1");
			returnMap.put("errmsg", "参数错误，APPID不能为空");
			return returnMap;
		}else{
			//获取机构下appid对应的appsecret
			String appsecret = this.getWxAppsecret(orgId, appid);
			
			if(!"".equals(appsecret) && appsecret != null){
				//获取access_token
				String access_token = "";
				try {
					access_token = tzGetAccessToken.getBaseAccessToken(appid, appsecret, "GZ", false);
				} catch (TzException e) {
					e.printStackTrace();
					access_token = "";
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败，"+e.getMessage());
					return returnMap;
				}
				
				if(!"".equals(access_token)){
					try {
						String result = TzWxTagsUtil.batchTagging(access_token, tagId, openIds);
						
						jacksonUtil.json2Map(result);
						returnMap = jacksonUtil.getMap();
					} catch (TzException e) {
						e.printStackTrace();
						returnMap.put("errcode", "-1");
						returnMap.put("errmsg", "批量为用户打标签失败，"+e.getMessage());
						return returnMap;
					}
				}else{
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败。");
					return returnMap;
				}
			}else{
				returnMap.put("errcode", "-1");
				returnMap.put("errmsg", "获取机构下APPID["+appid+"]对应APPSECRET失败。");
				return returnMap;
			}
		}
		
		return returnMap;
	}
	
	
	
	/**
	 * 批量为用户取消标签
	 * @param orgId		机构ID
	 * @param appid		微信APPID
	 * @param tagId		标签ID
	 * @param openIds	微信用户OPENID List
	 * @return
	 *  返回说明：
	 *  {
	 *	  "errcode":0,		////返回0，取消签成功，返回其他失败
	 *	  "errmsg":"ok"
	 *	}
	 */
	public Map<String,Object> batchUnTagging (String orgId, String appid, String tagId, List<String> openIds){
	
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(appid == null || "".equals(appid)){
			returnMap.put("errcode", "-1");
			returnMap.put("errmsg", "参数错误，APPID不能为空");
			return returnMap;
		}else{
			//获取机构下appid对应的appsecret
			String appsecret = this.getWxAppsecret(orgId, appid);
			
			if(!"".equals(appsecret) && appsecret != null){
				//获取access_token
				String access_token = "";
				try {
					access_token = tzGetAccessToken.getBaseAccessToken(appid, appsecret, "GZ", false);
				} catch (TzException e) {
					e.printStackTrace();
					access_token = "";
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败，"+e.getMessage());
					return returnMap;
				}
				
				if(!"".equals(access_token)){
					try {
						String result = TzWxTagsUtil.batchUnTagging(access_token, tagId, openIds);
						
						jacksonUtil.json2Map(result);
						returnMap = jacksonUtil.getMap();
					} catch (TzException e) {
						e.printStackTrace();
						returnMap.put("errcode", "-1");
						returnMap.put("errmsg", "批量为用户取消标签失败，"+e.getMessage());
						return returnMap;
					}
				}else{
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败。");
					return returnMap;
				}
			}else{
				returnMap.put("errcode", "-1");
				returnMap.put("errmsg", "获取机构下APPID["+appid+"]对应APPSECRET失败。");
				return returnMap;
			}
		}
		
		return returnMap;
	}
	
	
	
	/**
	 * 获取用户身上的标签列表
	 * @param orgId		机构ID
	 * @param appid		微信APPID
	 * @param openId	微信用户OPENID
	 * @return
	 *  正确返回：
	 *  {
	 *	   "tagid_list":[134,2]   //被置上的标签列表
	 *	}
	 *	
	 *	错误返回
	 *  {
	 *  	"errcode":"-1",					//errcode-错误码，不一定是-1，也可能是微信返回的错误码
	 *  	"errmsg":"invalid appid"
	 *  }
	 */
	public Map<String, Object> getUserTags(String orgId, String appid, String openId){
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(appid == null || "".equals(appid)){
			returnMap.put("errcode", "-1");
			returnMap.put("errmsg", "参数错误，APPID不能为空");
			return returnMap;
		}else{
			//获取机构下appid对应的appsecret
			String appsecret = this.getWxAppsecret(orgId, appid);
			
			if(!"".equals(appsecret) && appsecret != null){
				//获取access_token
				String access_token = "";
				try {
					access_token = tzGetAccessToken.getBaseAccessToken(appid, appsecret, "GZ", false);
				} catch (TzException e) {
					e.printStackTrace();
					access_token = "";
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败，"+e.getMessage());
					return returnMap;
				}
				
				if(!"".equals(access_token)){
					try {
						String result = TzWxTagsUtil.getUserTags(access_token, openId);
						
						jacksonUtil.json2Map(result);
						returnMap = jacksonUtil.getMap();
					} catch (TzException e) {
						e.printStackTrace();
						returnMap.put("errcode", "-1");
						returnMap.put("errmsg", "获取用户标签失败，"+e.getMessage());
						return returnMap;
					}
				}else{
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败。");
					return returnMap;
				}
			}else{
				returnMap.put("errcode", "-1");
				returnMap.put("errmsg", "获取机构下APPID["+appid+"]对应APPSECRET失败。");
				return returnMap;
			}
		}
		
		return returnMap;
	}
	
	
	
	
	/**
	 * 新增其他类型永久素材
	 * @param orgId				机构ID
	 * @param appid				微信APPID
	 * @param file				素材文件
	 * @param type				媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
	 * @param title				上传类型为video的参数，视频素材的标题。其他类型传空即可
	 * @param introduction		上传类型为video的参数，视频素材的描述。其他类型传空即可
	 * @return
	 * 	正确返回：
	 * 	{
	 *		 "media_id":MEDIA_ID,		//新增的永久素材的media_id
	 *		 "url":URL					//新增的图片素材的图片URL（仅新增图片素材时会返回该字段）
	 *	}
	 *	
	 *	错误返回
	 *	{
	 *  	"errcode":"-1",					//errcode-错误码，不一定是-1，也可能是微信返回的错误码
	 *  	"errmsg":"invalid appid"
	 *  }
	 */
	public Map<String,Object> addOtherMaterial(String orgId, String appid, File file, String type, String title,String introduction){
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(appid == null || "".equals(appid)){
			returnMap.put("errcode", "-1");
			returnMap.put("errmsg", "参数错误，APPID不能为空");
			return returnMap;
		}else{
			//获取机构下appid对应的appsecret
			String appsecret = this.getWxAppsecret(orgId, appid);
			
			if(!"".equals(appsecret) && appsecret != null){
				//获取access_token
				String access_token = "";
				try {
					access_token = tzGetAccessToken.getBaseAccessToken(appid, appsecret, "GZ", false);
				} catch (TzException e) {
					e.printStackTrace();
					access_token = "";
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败，"+e.getMessage());
					return returnMap;
				}
				
				if(!"".equals(access_token)){
					try {
						String result = TzWxMaterialUtil.addOtherMaterial(access_token, file, type, title, introduction);
						
						jacksonUtil.json2Map(result);
						returnMap = jacksonUtil.getMap();
					} catch (TzException e) {
						e.printStackTrace();
						returnMap.put("errcode", "-1");
						returnMap.put("errmsg", "新增"+type+"类型素材失败，"+e.getMessage());
						return returnMap;
					}
				}else{
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败。");
					return returnMap;
				}
			}else{
				returnMap.put("errcode", "-1");
				returnMap.put("errmsg", "获取机构下APPID["+appid+"]对应APPSECRET失败。");
				return returnMap;
			}
		}
		
		return returnMap;
	}
	
	
	
	/**
	 * 上传图文消息内的图片获取URL 
	 * 本接口所上传的图片不占用公众号的素材库中图片数量的5000个的限制。图片仅支持jpg/png格式，大小必须在1MB以下。
	 * @param orgId		机构ID
	 * @param appid		微信服务号的APPID
	 * @param file		图片文件
	 * @return
	 * 正确返回：
	 * 	{
	 *	    "url":  "http://mmbiz.qpic.cn/mmbiz/gLO1...wWCBRQ/0"      //上传图片的URL，可放置图文消息中使用
	 *	}
	 *	
	 * 错误返回：
	 *	{
	 *  	"errcode":"-1",				//errcode-错误码，不一定是-1，也可能是微信返回的错误码
	 *  	"errmsg":"invalid appid"
	 *  }
	 */
	public Map<String,Object> uploadMaterialContentImages(String orgId, String appid, File file){
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(appid == null || "".equals(appid)){
			returnMap.put("errcode", "-1");
			returnMap.put("errmsg", "参数错误，APPID不能为空");
			return returnMap;
		}else{
			//获取机构下appid对应的appsecret
			String appsecret = this.getWxAppsecret(orgId, appid);
			
			if(!"".equals(appsecret) && appsecret != null){
				//获取access_token
				String access_token = "";
				try {
					access_token = tzGetAccessToken.getBaseAccessToken(appid, appsecret, "GZ", false);
				} catch (TzException e) {
					e.printStackTrace();
					access_token = "";
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败，"+e.getMessage());
					return returnMap;
				}
				
				if(!"".equals(access_token)){
					try {
						String result = TzWxMaterialUtil.uploadMaterialContentImages(access_token, file);
						
						jacksonUtil.json2Map(result);
						returnMap = jacksonUtil.getMap();
					} catch (TzException e) {
						e.printStackTrace();
						returnMap.put("errcode", "-1");
						returnMap.put("errmsg", "上传图文消息内图片并获取URL失败，"+e.getMessage());
						return returnMap;
					}
				}else{
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败。");
					return returnMap;
				}
			}else{
				returnMap.put("errcode", "-1");
				returnMap.put("errmsg", "获取机构下APPID["+appid+"]对应APPSECRET失败。");
				return returnMap;
			}
		}
		
		return returnMap;
	}
	
	
	
	
	/**
	 * 新增永久图文素材
	 * @param orgId
	 * @param appid
	 * @param articleList
	 * [
	 *	   {
	 *	       "title": TITLE,								//标题
	 *	       "thumb_media_id": THUMB_MEDIA_ID,			//图文消息的封面图片素材id（必须是永久mediaID）
	 *	       "author": AUTHOR,							//作者
	 *	       "digest": DIGEST,							//图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空。如果本字段为没有填写，则默认抓取正文前64个字。
	 *	       "show_cover_pic": SHOW_COVER_PIC(0 / 1),		//是否显示封面，0为false，即不显示，1为true，即显示
	 *	       "content": CONTENT,							//图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS,涉及图片url必须来源"上传图文消息内的图片获取URL"接口获取。外部图片url将被过滤。
	 *	       "content_source_url": CONTENT_SOURCE_URL		//图文消息的原文地址，即点击“阅读原文”后的URL
	 *	    },
	 *	    ......
	 * ]
	 * @return
	 * 正确返回：
	 * {
	 *	  "media_id":MEDIA_ID			//新增的图文消息素材的media_id
	 * }
	 * 
	 * 错误返回：
	 *  {
	 *  	"errcode":"-1",				//errcode-错误码，不一定是-1，也可能是微信返回的错误码
	 *  	"errmsg":"invalid appid"
	 *  }
	 */
	public Map<String,Object> addMaterialNews(String orgId, String appid, List<Map<String,Object>> articleList){
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(appid == null || "".equals(appid)){
			returnMap.put("errcode", "-1");
			returnMap.put("errmsg", "参数错误，APPID不能为空");
			return returnMap;
		}else{
			//获取机构下appid对应的appsecret
			String appsecret = this.getWxAppsecret(orgId, appid);
			
			if(!"".equals(appsecret) && appsecret != null){
				//获取access_token
				String access_token = "";
				try {
					access_token = tzGetAccessToken.getBaseAccessToken(appid, appsecret, "GZ", false);
				} catch (TzException e) {
					e.printStackTrace();
					access_token = "";
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败，"+e.getMessage());
					return returnMap;
				}
				
				if(!"".equals(access_token)){
					try {
						String result = TzWxMaterialUtil.addMaterialNews(access_token, articleList);
						
						jacksonUtil.json2Map(result);
						returnMap = jacksonUtil.getMap();
					} catch (TzException e) {
						e.printStackTrace();
						returnMap.put("errcode", "-1");
						returnMap.put("errmsg", "新增图文素材失败，"+e.getMessage());
						return returnMap;
					}
				}else{
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败。");
					return returnMap;
				}
			}else{
				returnMap.put("errcode", "-1");
				returnMap.put("errmsg", "获取机构下APPID["+appid+"]对应APPSECRET失败。");
				return returnMap;
			}
		}
		
		return returnMap;
	}
	
	
	
	/**
	 * 修改永久图文素材
	 * @param orgId				机构ID	
	 * @param appid				微信APPID
	 * @param media_id			要修改的图文消息的id
	 * @param index				要更新的文章在图文消息中的位置（多图文消息时，此字段才有意义），第一篇为0
	 * @param articlesMap
	 * {
	 *     "title": TITLE,							 	//标题
	 *     "thumb_media_id": THUMB_MEDIA_ID,			//图文消息的封面图片素材id（必须是永久mediaID）
	 *     "author": AUTHOR,							//作者
	 *     "digest": DIGEST,							//图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空
	 *     "show_cover_pic": SHOW_COVER_PIC(0 / 1),		//是否显示封面，0为false，即不显示，1为true，即显示
	 *     "content": CONTENT,							//图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS
	 *     "content_source_url": CONTENT_SOURCE_URL		//图文消息的原文地址，即点击“阅读原文”后的URL
	 * }
	 * @return
	 *  {
	 *  	"errcode":0,				//返回0，表示修改成功，其他返回修改失败
	 *  	"errmsg":"invalid appid"
	 *  }
	 */
	public Map<String,Object> updateMaterialNews(String orgId, String appid, String media_id, int index, Map<String,Object> articlesMap){
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(appid == null || "".equals(appid)){
			returnMap.put("errcode", "-1");
			returnMap.put("errmsg", "参数错误，APPID不能为空");
			return returnMap;
		}else{
			//获取机构下appid对应的appsecret
			String appsecret = this.getWxAppsecret(orgId, appid);
			
			if(!"".equals(appsecret) && appsecret != null){
				//获取access_token
				String access_token = "";
				try {
					access_token = tzGetAccessToken.getBaseAccessToken(appid, appsecret, "GZ", false);
				} catch (TzException e) {
					e.printStackTrace();
					access_token = "";
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败，"+e.getMessage());
					return returnMap;
				}
				
				if(!"".equals(access_token)){
					try {
						String result = TzWxMaterialUtil.updateMaterialNews(access_token, media_id, index, articlesMap);
						
						jacksonUtil.json2Map(result);
						returnMap = jacksonUtil.getMap();
					} catch (TzException e) {
						e.printStackTrace();
						returnMap.put("errcode", "-1");
						returnMap.put("errmsg", "修改图文素材失败，"+e.getMessage());
						return returnMap;
					}
				}else{
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败。");
					return returnMap;
				}
			}else{
				returnMap.put("errcode", "-1");
				returnMap.put("errmsg", "获取机构下APPID["+appid+"]对应APPSECRET失败。");
				return returnMap;
			}
		}
		
		return returnMap;
	}
	
	
	
	
	/**
	 * 删除永久素材
	 * @param orgId		机构ID
	 * @param appid		微信APPID
	 * @param mediaId	要删除素材的media_id
	 * @return
	 *  {
	 *  	"errcode":0,				//返回0，表示修改成功，其他返回修改失败
	 *  	"errmsg":"invalid appid"
	 *  }
	 */
	public Map<String,Object> deleteMaterial(String orgId, String appid, String mediaId){
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(appid == null || "".equals(appid)){
			returnMap.put("errcode", "-1");
			returnMap.put("errmsg", "参数错误，APPID不能为空");
			return returnMap;
		}else{
			//获取机构下appid对应的appsecret
			String appsecret = this.getWxAppsecret(orgId, appid);
			
			if(!"".equals(appsecret) && appsecret != null){
				//获取access_token
				String access_token = "";
				try {
					access_token = tzGetAccessToken.getBaseAccessToken(appid, appsecret, "GZ", false);
				} catch (TzException e) {
					e.printStackTrace();
					access_token = "";
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败，"+e.getMessage());
					return returnMap;
				}
				
				if(!"".equals(access_token)){
					try {
						String result = TzWxMaterialUtil.deleteMaterial(access_token, mediaId);
						
						jacksonUtil.json2Map(result);
						returnMap = jacksonUtil.getMap();
					} catch (TzException e) {
						e.printStackTrace();
						returnMap.put("errcode", "-1");
						returnMap.put("errmsg", "删除素材失败，"+e.getMessage());
						return returnMap;
					}
				}else{
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败。");
					return returnMap;
				}
			}else{
				returnMap.put("errcode", "-1");
				returnMap.put("errmsg", "获取机构下APPID["+appid+"]对应APPSECRET失败。");
				return returnMap;
			}
		}
		
		return returnMap;
	}
	
	
	
	/**
	 * 获取素材列表
	 * @param orgId			机构ID
	 * @param appid			微信APPID
	 * @param type			素材的类型，图片（image）、视频（video）、语音 （voice）、图文（news）
	 * @param offset		从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
	 * @param count			返回素材的数量，取值在1到20之间
	 * @return
	 * 永久图文消息素材列表的响应如下：
	 *	{
	 *	  "total_count": TOTAL_COUNT,				//该类型的素材的总数
	 *	  "item_count": ITEM_COUNT,					//本次调用获取的素材的数量
	 *	  "item": [{
	 *	      "media_id": MEDIA_ID,
	 *	      "content": {
	 *	          "news_item": [{
	 *	              "title": TITLE,							//图文消息的标题
	 *	              "thumb_media_id": THUMB_MEDIA_ID,			//图文消息的封面图片素材id（必须是永久mediaID）
	 *	              "show_cover_pic": SHOW_COVER_PIC(0 / 1),	//是否显示封面，0为false，即不显示，1为true，即显示
	 *	              "author": AUTHOR,							//作者
	 *	              "digest": DIGEST,							//图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空
	 *	              "content": CONTENT,						//图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS
	 *	              "url": URL,								//图文页的URL，或者，当获取的列表是图片素材列表时，该字段是图片的URL
	 *	              "content_source_url": CONTETN_SOURCE_URL	//图文消息的原文地址，即点击“阅读原文”后的URL
	 *	          },
	 *	          //多图文消息会在此处有多篇文章
	 *	          ]
	 *	       },
	 *	       "update_time": UPDATE_TIME						//这篇图文消息素材的最后更新时间
	 *	   },
	 *	   //可能有多个图文消息item结构
	 *	 ]
	 *	}
	 *	其他类型（图片、语音、视频）的返回如下：
	 *	{
	 *	  "total_count": TOTAL_COUNT,		//该类型的素材的总数
	 *	  "item_count": ITEM_COUNT,			//本次调用获取的素材的数量
	 *	  "item": [{
	 *	      "media_id": MEDIA_ID,			
	 *	      "name": NAME,					//文件名称
	 *	      "update_time": UPDATE_TIME,	//这篇图文消息素材的最后更新时间
	 *	      "url":URL						//当获取的列表是图片素材列表时，该字段是图片的URL
	 *	  },
	 *	  //可能会有多个素材
	 *	  ]
	 *	}
	 *
	 * 错误返回：
	 *	{
	 *  	"errcode":40007,
	 *  	"errmsg":"invalid appid"
	 *  }
	 */
	public Map<String,Object> batchGetMaterial(String orgId, String appid, String type, int offset, int count){
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(appid == null || "".equals(appid)){
			returnMap.put("errcode", "-1");
			returnMap.put("errmsg", "参数错误，APPID不能为空");
			return returnMap;
		}else{
			//获取机构下appid对应的appsecret
			String appsecret = this.getWxAppsecret(orgId, appid);
			
			if(!"".equals(appsecret) && appsecret != null){
				//获取access_token
				String access_token = "";
				try {
					access_token = tzGetAccessToken.getBaseAccessToken(appid, appsecret, "GZ", false);
				} catch (TzException e) {
					e.printStackTrace();
					access_token = "";
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败，"+e.getMessage());
					return returnMap;
				}
				
				if(!"".equals(access_token)){
					try {
						String result = TzWxMaterialUtil.batchGetMaterial(access_token, type, offset, count);
						
						jacksonUtil.json2Map(result);
						returnMap = jacksonUtil.getMap();
					} catch (TzException e) {
						e.printStackTrace();
						returnMap.put("errcode", "-1");
						returnMap.put("errmsg", "获取素材列表失败，"+e.getMessage());
						return returnMap;
					}
				}else{
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败。");
					return returnMap;
				}
			}else{
				returnMap.put("errcode", "-1");
				returnMap.put("errmsg", "获取机构下APPID["+appid+"]对应APPSECRET失败。");
				return returnMap;
			}
		}
		
		return returnMap;
	}
	
	
	
	/**
	 * 获取永久素材
	 * @param orgId			机构ID
	 * @param appid			微信APPID
	 * @param media_id		素材media_id
	 * @return
	 * 图文素材:
	 *	{
	 *	"news_item":
	 *		[
	 *		    {
	 *		    "title":TITLE,							//图文消息的标题
	 *		    "thumb_media_id"::THUMB_MEDIA_ID,		//图文消息的封面图片素材id（必须是永久mediaID）
	 *		    "show_cover_pic":SHOW_COVER_PIC(0/1),	//是否显示封面，0为false，即不显示，1为true，即显示
	 *		    "author":AUTHOR,						//作者
	 *		    "digest":DIGEST,						//图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空
	 *		    "content":CONTENT,						//图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS
	 *		    "url":URL,								//图文页的URL
	 *		    "content_source_url":CONTENT_SOURCE_URL	//图文消息的原文地址，即点击“阅读原文”后的URL
	 *		    },
	 *		    //多图文消息有多篇文章
	 *		 ]
	 *	}
	 *	视频消息素材：
	 *	{
	 *		 "title":TITLE,					//视频标题
	 *		 "description":DESCRIPTION,		//视频描述
	 *		 "down_url":DOWN_URL,			//视频下载URL
	 *	}
	 *	其他类型的素材消息，则响应的直接为素材的内容，开发者可以自行保存为文件
	 *
	 * 错误返回：
	 * 	{
	 *  	"errcode":40007,
	 *  	"errmsg":"invalid media_id"
	 *  }
	 */
	public Map<String,Object> getMaterial(String orgId, String appid, String media_id){
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(appid == null || "".equals(appid)){
			returnMap.put("errcode", "-1");
			returnMap.put("errmsg", "参数错误，APPID不能为空");
			return returnMap;
		}else{
			//获取机构下appid对应的appsecret
			String appsecret = this.getWxAppsecret(orgId, appid);
			
			if(!"".equals(appsecret) && appsecret != null){
				//获取access_token
				String access_token = "";
				try {
					access_token = tzGetAccessToken.getBaseAccessToken(appid, appsecret, "GZ", false);
				} catch (TzException e) {
					e.printStackTrace();
					access_token = "";
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败，"+e.getMessage());
					return returnMap;
				}
				
				if(!"".equals(access_token)){
					try {
						String result = TzWxMaterialUtil.getMaterial(access_token, media_id);
						
						jacksonUtil.json2Map(result);
						returnMap = jacksonUtil.getMap();
					} catch (TzException e) {
						e.printStackTrace();
						returnMap.put("errcode", "-1");
						returnMap.put("errmsg", "获取素材失败，"+e.getMessage());
						return returnMap;
					}
				}else{
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败。");
					return returnMap;
				}
			}else{
				returnMap.put("errcode", "-1");
				returnMap.put("errmsg", "获取机构下APPID["+appid+"]对应APPSECRET失败。");
				return returnMap;
			}
		}
		
		return returnMap;
	}
	
	
	
	
	/**
	 * 上传图文消息素材
	 * @param orgId			机构ID
	 * @param appid			微信APPID
	 * @param articleList	图文素材内容，支持多图文素材上传
	 * [
	 *	   {
	 *	       "title": TITLE,								//标题
	 *	       "thumb_media_id": THUMB_MEDIA_ID,			//图文消息的封面图片素材id（必须是永久mediaID）
	 *	       "author": AUTHOR,							//作者
	 *	       "digest": DIGEST,							//图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空。如果本字段为没有填写，则默认抓取正文前64个字。
	 *	       "show_cover_pic": SHOW_COVER_PIC(0 / 1),		//是否显示封面，0为false，即不显示，1为true，即显示
	 *	       "content": CONTENT,							//图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS,涉及图片url必须来源"上传图文消息内的图片获取URL"接口获取。外部图片url将被过滤。
	 *	       "content_source_url": CONTENT_SOURCE_URL		//图文消息的原文地址，即点击“阅读原文”后的URL
	 *	    },
	 *	    ......
	 *    ]
	 * @return
	 * 正确返回：
	 *  {
	 *	   "type":"news",									//媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb），图文消息（news）	
	 *	   "media_id":"CsEf3ldqkAYJAUfUJ54vq6-aYyUiQ",		//媒体文件/图文消息上传后获取的唯一标识
	 *	   "created_at":1391857799							//媒体文件上传时间
	 *	}
	 * 
	 * 错误返回：
	 *  {
	 *  	"errcode":"-1",				//errcode-错误码，不一定是-1，也可能是微信返回的错误码
	 *  	"errmsg":"invalid appid"
	 *  }
	 */
	public Map<String,Object> uploadNews(String orgId, String appid, List<Map<String, Object>> articleList){
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(appid == null || "".equals(appid)){
			returnMap.put("errcode", "-1");
			returnMap.put("errmsg", "参数错误，APPID不能为空");
			return returnMap;
		}else{
			//获取机构下appid对应的appsecret
			String appsecret = this.getWxAppsecret(orgId, appid);
			
			if(!"".equals(appsecret) && appsecret != null){
				//获取access_token
				String access_token = "";
				try {
					access_token = tzGetAccessToken.getBaseAccessToken(appid, appsecret, "GZ", false);
				} catch (TzException e) {
					e.printStackTrace();
					access_token = "";
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败，"+e.getMessage());
					return returnMap;
				}
				
				if(!"".equals(access_token)){
					try {
						String result = TzWxMessageMassSendUtil.uploadNews(access_token, articleList);
						
						jacksonUtil.json2Map(result);
						returnMap = jacksonUtil.getMap();
					} catch (TzException e) {
						e.printStackTrace();
						returnMap.put("errcode", "-1");
						returnMap.put("errmsg", "上传图文素材失败，"+e.getMessage());
						return returnMap;
					}
				}else{
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败。");
					return returnMap;
				}
			}else{
				returnMap.put("errcode", "-1");
				returnMap.put("errmsg", "获取机构下APPID["+appid+"]对应APPSECRET失败。");
				return returnMap;
			}
		}
		
		return returnMap;
	}
	
	
	
	
	/**
	 * 根据标签进行群发
	 * @param orgId					机构ID
	 * @param appid					微信APPID
	 * @param mediaIdOrContent		用于群发的消息的media_id或文本消息
	 * @param msgtype				群发的消息类型，图文消息为mpnews，文本消息为text，语音为voice，音乐为music，图片为image，视频为video，卡券为wxcard
	 * @param tagId					群发到的标签的tag_id，参加用户管理中用户分组接口，若is_to_all值为true，可不填写tag_id
	 * @param title					消息的标题（仅视频）,其他类型传空值
	 * @param description			消息的描述（仅视频）,其他类型传空值
	 * @return
	 *  {
	 *	   "errcode":0,								//返回0，表示群发任务提交成功，其他值失败
	 *	   "errmsg":"send job submission success",	//错误信息
	 *	   "msg_id":34182, 							//消息发送任务的ID
	 *	   "msg_data_id": 206227730					//消息的数据ID，该字段只有在群发图文消息时，才会出现。可以用于在图文分析数据接口中，获取到对应的图文消息的数据，是图文分析数据接口中的msgid字段中的前半部分，详见图文分析数据接口中的msgid字段的介绍。
	 *	}
	 */
	public Map<String,Object> messageMassSendByTag (String orgId, String appid, String mediaIdOrContent, String msgtype, String tagId, String title, String description){
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(appid == null || "".equals(appid)){
			returnMap.put("errcode", "-1");
			returnMap.put("errmsg", "参数错误，APPID不能为空");
			return returnMap;
		}else{
			//获取机构下appid对应的appsecret
			String appsecret = this.getWxAppsecret(orgId, appid);
			
			if(!"".equals(appsecret) && appsecret != null){
				//获取access_token
				String access_token = "";
				try {
					access_token = tzGetAccessToken.getBaseAccessToken(appid, appsecret, "GZ", false);
				} catch (TzException e) {
					e.printStackTrace();
					access_token = "";
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败，"+e.getMessage());
					return returnMap;
				}
				
				if(!"".equals(access_token)){
					try {
						String result = TzWxMessageMassSendUtil.messageMassSendByTag(access_token, mediaIdOrContent, msgtype, tagId, title, description);
						
						jacksonUtil.json2Map(result);
						returnMap = jacksonUtil.getMap();
					} catch (TzException e) {
						e.printStackTrace();
						returnMap.put("errcode", "-1");
						returnMap.put("errmsg", "按标签群发消息失败，"+e.getMessage());
						return returnMap;
					}
				}else{
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败。");
					return returnMap;
				}
			}else{
				returnMap.put("errcode", "-1");
				returnMap.put("errmsg", "获取机构下APPID["+appid+"]对应APPSECRET失败。");
				return returnMap;
			}
		}
		
		return returnMap;
	}
	
	
	
	/**
	 * 根据OPENID进行群发
	 * @param orgId					机构ID
	 * @param appid					微信APPID
	 * @param mediaIdOrContent		用于群发的消息的media_id或文本消息
	 * @param msgtype				群发的消息类型，图文消息为mpnews，文本消息为text，语音为voice，音乐为music，图片为image，视频为video，卡券为wxcard
	 * @param openIds				群发用户OPENID数组
	 * @param title					消息的标题（仅视频）,其他类型传空值
	 * @param description			消息的描述（仅视频）,其他类型传空值
	 * @return
	 * {
	 *	   "errcode":0,								//返回0，表示群发任务提交成功，其他值失败
	 *	   "errmsg":"send job submission success",	//错误信息
	 *	   "msg_id":34182, 							//消息发送任务的ID
	 *	   "msg_data_id": 206227730					//消息的数据ID，该字段只有在群发图文消息时，才会出现。可以用于在图文分析数据接口中，获取到对应的图文消息的数据，是图文分析数据接口中的msgid字段中的前半部分，详见图文分析数据接口中的msgid字段的介绍。
	 *	}
	 */
	public Map<String,Object> messageMassSendByOpenid (String orgId, String appid, String mediaIdOrContent, String msgtype, String[] openIds , String title, String description){
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(appid == null || "".equals(appid)){
			returnMap.put("errcode", "-1");
			returnMap.put("errmsg", "参数错误，APPID不能为空");
			return returnMap;
		}else{
			//获取机构下appid对应的appsecret
			String appsecret = this.getWxAppsecret(orgId, appid);
			
			if(!"".equals(appsecret) && appsecret != null){
				//获取access_token
				String access_token = "";
				try {
					access_token = tzGetAccessToken.getBaseAccessToken(appid, appsecret, "GZ", false);
				} catch (TzException e) {
					e.printStackTrace();
					access_token = "";
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败，"+e.getMessage());
					return returnMap;
				}
				
				if(!"".equals(access_token)){
					try {
						String result = TzWxMessageMassSendUtil.messageMassSendByOpenid(access_token, mediaIdOrContent, msgtype, openIds, title, description);
						
						jacksonUtil.json2Map(result);
						returnMap = jacksonUtil.getMap();
					} catch (TzException e) {
						e.printStackTrace();
						returnMap.put("errcode", "-1");
						returnMap.put("errmsg", "按OPENID群发消息失败，"+e.getMessage());
						return returnMap;
					}
				}else{
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败。");
					return returnMap;
				}
			}else{
				returnMap.put("errcode", "-1");
				returnMap.put("errmsg", "获取机构下APPID["+appid+"]对应APPSECRET失败。");
				return returnMap;
			}
		}
		
		return returnMap;
	}
	
	
	
	
	/**
	 * 
	 * @param orgId		机构ID
	 * @param appid		微信APPID
	 * @param msg_id	群发消息后返回的消息id
	 * @return
	 * 正确返回：
	 *  {
	 *	     "msg_id":201053012,			//消息id
	 *	     "msg_status":"SEND_SUCCESS"	//消息发送后的状态，SEND_SUCCESS表示发送成功
	 *	}
	 *	
	 *	错误返回：
	 *	{
	 *  	"errcode":"-1",				//errcode-错误码，不一定是-1，也可能是微信返回的错误码
	 *  	"errmsg":"invalid appid"
	 *  }
	 */
	public Map<String,Object> getMassSendStatus(String orgId, String appid, String msg_id){
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(appid == null || "".equals(appid)){
			returnMap.put("errcode", "-1");
			returnMap.put("errmsg", "参数错误，APPID不能为空");
			return returnMap;
		}else{
			//获取机构下appid对应的appsecret
			String appsecret = this.getWxAppsecret(orgId, appid);
			
			if(!"".equals(appsecret) && appsecret != null){
				//获取access_token
				String access_token = "";
				try {
					access_token = tzGetAccessToken.getBaseAccessToken(appid, appsecret, "GZ", false);
				} catch (TzException e) {
					e.printStackTrace();
					access_token = "";
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败，"+e.getMessage());
					return returnMap;
				}
				
				if(!"".equals(access_token)){
					try {
						String result = TzWxMessageMassSendUtil.getMassSendStatus(access_token, msg_id);
						
						jacksonUtil.json2Map(result);
						returnMap = jacksonUtil.getMap();
					} catch (TzException e) {
						e.printStackTrace();
						returnMap.put("errcode", "-1");
						returnMap.put("errmsg", "查询群发消息发送状态失败，"+e.getMessage());
						return returnMap;
					}
				}else{
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败。");
					return returnMap;
				}
			}else{
				returnMap.put("errcode", "-1");
				returnMap.put("errmsg", "获取机构下APPID["+appid+"]对应APPSECRET失败。");
				return returnMap;
			}
		}
		
		return returnMap;
	}
	
	
	
	
	/**
	 * 发送普通模板消息
	 * @param orgId				机构ID
	 * @param appid				微信APPID
	 * @param opendId		 	接收者openid
	 * @param template_id		模板ID
	 * @param dataList			模板数据 [{"key":"keynote1","value":"恭喜你购买成功！","color":"#173177"},......],color模板内容字体颜色，不填默认为黑色
	 * @return
	 * {
     *      "errcode":0,		//返回0，表示发送成功，其他返回值发送失败
     *      "errmsg":"ok",
     *      "msgid":200228332	//消息ID
     *  }
	 */
	public Map<String,Object> sendTemplateMessage(String orgId, String appid, String openId, String template_id, List<Map<String,String>> dataList){
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(appid == null || "".equals(appid)){
			returnMap.put("errcode", "-1");
			returnMap.put("errmsg", "参数错误，APPID不能为空");
			return returnMap;
		}else{
			//获取机构下appid对应的appsecret
			String appsecret = this.getWxAppsecret(orgId, appid);
			
			if(!"".equals(appsecret) && appsecret != null){
				//获取access_token
				String access_token = "";
				try {
					access_token = tzGetAccessToken.getBaseAccessToken(appid, appsecret, "GZ", false);
				} catch (TzException e) {
					e.printStackTrace();
					access_token = "";
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败，"+e.getMessage());
					return returnMap;
				}
				
				if(!"".equals(access_token)){
					try {
						String result = TzWxTemplateMessageUtil.sendTemplateMessage(access_token, openId, template_id, dataList);
						
						jacksonUtil.json2Map(result);
						returnMap = jacksonUtil.getMap();
					} catch (TzException e) {
						e.printStackTrace();
						returnMap.put("errcode", "-1");
						returnMap.put("errmsg", "模板消息发送失败，"+e.getMessage());
						return returnMap;
					}
				}else{
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败。");
					return returnMap;
				}
			}else{
				returnMap.put("errcode", "-1");
				returnMap.put("errmsg", "获取机构下APPID["+appid+"]对应APPSECRET失败。");
				return returnMap;
			}
		}
		
		return returnMap;
	}
	
	
	
	
	/**
	 * 发送跳转URL模板消息
	 * @param orgId				机构ID
	 * @param appid				微信APPID
	 * @param opendId		 	接收者openid
	 * @param template_id		模板ID
	 * @param dataList			模板数据 [{"key":"keynote1","value":"恭喜你购买成功！","color":"#173177"},......],color模板内容字体颜色，不填默认为黑色
	 * @param url				模板跳转链接
	 * @return
	 * {
     *      "errcode":0,		//返回0，表示发送成功，其他返回值发送失败
     *      "errmsg":"ok",
     *      "msgid":200228332	//消息ID
     *  }
	 */
	public Map<String,Object> sendTemplateMessage(String orgId, String appid, String openId, String template_id, List<Map<String,String>> dataList, String url){
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(appid == null || "".equals(appid)){
			returnMap.put("errcode", "-1");
			returnMap.put("errmsg", "参数错误，APPID不能为空");
			return returnMap;
		}else{
			//获取机构下appid对应的appsecret
			String appsecret = this.getWxAppsecret(orgId, appid);
			
			if(!"".equals(appsecret) && appsecret != null){
				//获取access_token
				String access_token = "";
				try {
					access_token = tzGetAccessToken.getBaseAccessToken(appid, appsecret, "GZ", false);
				} catch (TzException e) {
					e.printStackTrace();
					access_token = "";
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败，"+e.getMessage());
					return returnMap;
				}
				
				if(!"".equals(access_token)){
					try {
						String result = TzWxTemplateMessageUtil.sendTemplateMessage(access_token, openId, template_id, dataList, url);
						
						jacksonUtil.json2Map(result);
						returnMap = jacksonUtil.getMap();
					} catch (TzException e) {
						e.printStackTrace();
						returnMap.put("errcode", "-1");
						returnMap.put("errmsg", "模板消息发送失败，"+e.getMessage());
						return returnMap;
					}
				}else{
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败。");
					return returnMap;
				}
			}else{
				returnMap.put("errcode", "-1");
				returnMap.put("errmsg", "获取机构下APPID["+appid+"]对应APPSECRET失败。");
				return returnMap;
			}
		}
		
		return returnMap;
	}
	
	
	
	/**
	 * 发送跳转小程序模板消息
	 * @param orgId				机构ID
	 * @param appid				微信APPID
	 * @param opendId		 	接收者openid
	 * @param template_id		模板ID
	 * @param dataList			模板数据 [{"key":"keynote1","value":"恭喜你购买成功！","color":"#173177"},......],color模板内容字体颜色，不填默认为黑色
	 * @param miniAppid			所需跳转到的小程序appid（该小程序appid必须与发模板消息的公众号是绑定关联关系）
	 * @param pagepath			所需跳转到小程序的具体页面路径，支持带参数,（示例index?foo=bar）
	 * @return
	 * {
     *      "errcode":0,		//返回0，表示发送成功，其他返回值发送失败
     *      "errmsg":"ok",
     *      "msgid":200228332	//消息ID
     *  }
	 */
	public Map<String,Object> sendTemplateMessage(String orgId, String appid, String openId, String template_id, List<Map<String,String>> dataList, String miniAppid, String pagepath){
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(appid == null || "".equals(appid)){
			returnMap.put("errcode", "-1");
			returnMap.put("errmsg", "参数错误，APPID不能为空");
			return returnMap;
		}else{
			//获取机构下appid对应的appsecret
			String appsecret = this.getWxAppsecret(orgId, appid);
			
			if(!"".equals(appsecret) && appsecret != null){
				//获取access_token
				String access_token = "";
				try {
					access_token = tzGetAccessToken.getBaseAccessToken(appid, appsecret, "GZ", false);
				} catch (TzException e) {
					e.printStackTrace();
					access_token = "";
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败，"+e.getMessage());
					return returnMap;
				}
				
				if(!"".equals(access_token)){
					try {
						String result = TzWxTemplateMessageUtil.sendTemplateMessage(access_token, openId, template_id, dataList, miniAppid, pagepath);
						
						jacksonUtil.json2Map(result);
						returnMap = jacksonUtil.getMap();
					} catch (TzException e) {
						e.printStackTrace();
						returnMap.put("errcode", "-1");
						returnMap.put("errmsg", "模板消息发送失败，"+e.getMessage());
						return returnMap;
					}
				}else{
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "获取access_token失败。");
					return returnMap;
				}
			}else{
				returnMap.put("errcode", "-1");
				returnMap.put("errmsg", "获取机构下APPID["+appid+"]对应APPSECRET失败。");
				return returnMap;
			}
		}
		
		return returnMap;
	}
}
