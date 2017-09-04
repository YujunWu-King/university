package com.tranzvision.gd.TZWeChatBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.wechart.TzGetAccessToken;
import com.tranzvision.gd.util.wechart.TzWxUserUtil;

@Service
public class TzWxApiObject {

	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private TzGetAccessToken tzGetAccessToken;
	
	
	/**
	 * 获取微信appsecret
	 * @param orgId
	 * @param appId
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
	 */
	public Map<String,Object> getUserList(String orgId, String appid, String nextOpenid){
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
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
		}
		
		return returnMap;
	}
	
	
	
	/**
	 * 获取微信用户信息
	 * @param orgId
	 * @param appid
	 * @param openid
	 * @param lang
	 * @return
	 */
	public Map<String,Object> getUserInfo(String orgId, String appid, String openid, String lang){
		Map<String,Object> returnMap = new HashMap<String,Object>();
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
				returnMap.put("errcode", "1");
				returnMap.put("errmsg", e.getMessage());
			}
			
			if(!"".equals(access_token)){
				try {
					String result = TzWxUserUtil.getUserInfo(access_token, openid, lang);
					
					
				} catch (TzException e) {
					e.printStackTrace();
					returnMap.put("errcode", "1");
					returnMap.put("errmsg", e.getMessage());
				}
			}else{
				returnMap.put("errcode", "1");
				returnMap.put("errmsg", "access_token");
			}
		}
		
		return returnMap;
	}
}
