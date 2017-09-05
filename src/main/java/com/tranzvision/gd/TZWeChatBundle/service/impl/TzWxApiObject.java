package com.tranzvision.gd.TZWeChatBundle.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.wechart.TzGetAccessToken;
import com.tranzvision.gd.util.wechart.TzWxTagsUtil;
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
	
	
	
	
	
	
}
