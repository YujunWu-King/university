package com.tranzvision.gd.TZWeChatBundle.base.eventHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.tranzvision.gd.TZWeChatBundle.base.respMessage.TextMessage;
import com.tranzvision.gd.TZWeChatBundle.base.util.MessageUtil;
import com.tranzvision.gd.TZWeChatBundle.service.impl.TzWxApiObject;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 关注事件处理
 * @author tranzvision
 *
 */
public class SubscribeEventHandler implements EventsHandlerInterface  {
	

	@Override
	public String execute(Map<String, String> reqMap) {
		try{
			String openid=reqMap.get("FromUserName"); //用户openid
	        String mpid=reqMap.get("ToUserName");   //公众号原始ID
			
	        String appID = "wx5bdecf7575803a8d";
	        String orgId = "SEM";
	        
	        System.out.println("-----原始ID:"+mpid+"---->OPNEID:"+openid);
	        
	        if(orgId != null && !"".equals(orgId) 
	        		&& appID != null && !"".equals(appID)){
	        	
	        	GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
	        	TzWxApiObject tzWxApiObject = (TzWxApiObject) getSpringBeanUtil.getSpringBeanByID("tzWxApiObject");
	        	
	        	SqlQuery sqlQuery = (SqlQuery) getSpringBeanUtil.getSpringBeanByID("sqlQuery");
	        	
	        	//TZGDObject tzGDObject = (TZGDObject) getSpringBeanUtil.getSpringBeanByID("tzGDObject");
	        	
	        	//获取用户信息
	        	Map<String,Object> userInfoMap = tzWxApiObject.getUserInfo(orgId, appID, openid, "zh_CN");
	        	
	        	System.out.println("用户信息---> "+userInfoMap.toString());
	        	if(userInfoMap != null && !userInfoMap.containsKey("errcode")){
	        		String subscribe = userInfoMap.get("subscribe").toString();
	        		String nickname = userInfoMap.get("nickname").toString();
	        		String sex = userInfoMap.get("sex").toString();
	        		String language = userInfoMap.get("language").toString();
	        		String city = userInfoMap.get("city").toString();
	        		String province = userInfoMap.get("province").toString();
	        		String country = userInfoMap.get("country").toString();
	        		String headimgurl = userInfoMap.get("headimgurl").toString();
	        		String subscribe_time = userInfoMap.get("subscribe_time").toString();
	        		String remark = userInfoMap.get("remark").toString();
	        		
	        		System.out.println("DATE---------- " + subscribe_time);
	        		
	        		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	                long l_subscribe_time = new Long(subscribe_time);
	                Date subscribe_Date = MessageUtil.long2Date(l_subscribe_time);
	                String subscribe_Datetime = simpleDateFormat.format(subscribe_Date);
	                
	                System.out.println("DATE---------- " + subscribe_Datetime);
	                
	                String sql = "select 'Y' from PS_TZ_WX_USER_TBL where TZ_JG_ID=? and TZ_WX_APPID=? and TZ_OPEN_ID=?";
	                String existsFlg = sqlQuery.queryForObject(sql, new Object[]{ orgId, appID, openid }, "String");
	                
	                if("Y".equals(existsFlg)){
	                	String updateSql = "update PS_TZ_WX_USER_TBL set TZ_SUBSCRIBE='"+ subscribe 
	                			+"',TZ_NICKNAME='"+ nickname 
	                			+"',TZ_SEX='"+ sex 
	                			+"',TZ_LANGUAGE='"+ language 
	                			+"',TZ_CITY='"+ city 
	                			+"',TZ_PROVINCE='"+ province 
	                			+"',TZ_COUNTRY='"+ country 
	                			+"',TZ_IMAGE_URL='"+ headimgurl 
	                			+"',TZ_SUBSRIBE_DT='"+ subscribe_Datetime 
	                			+"',TZ_REMARK='"+ remark +"' where TZ_JG_ID=? and TZ_WX_APPID=? and TZ_OPEN_ID=?";
	                	
	                	sqlQuery.update(updateSql, new Object[]{ orgId, appID, openid });
	                }else{
	                	String insertSql = "insert into PS_TZ_WX_USER_TBL (TZ_JG_ID,TZ_WX_APPID,TZ_OPEN_ID,TZ_SUBSCRIBE,TZ_NICKNAME,TZ_SEX,TZ_LANGUAGE,TZ_CITY,TZ_PROVINCE,TZ_COUNTRY,TZ_IMAGE_URL,TZ_SUBSRIBE_DT,TZ_REMARK) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	                	
	                	sqlQuery.update(insertSql, new Object[]{ orgId, appID, openid, subscribe, nickname, sex, language, city, province, country, headimgurl, subscribe_Datetime, remark });
	                }
	                
	        		
	               //普通文本消息
	                TextMessage txtmsg=new TextMessage();
	                txtmsg.setToUserName(openid);
	                txtmsg.setFromUserName(mpid);
	                txtmsg.setCreateTime(new Date().getTime());
	                txtmsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
	                txtmsg.setContent(nickname + "您好，欢迎您关注！");

	                String returnStr = MessageUtil.textMessageToXml(txtmsg);
	                return returnStr;
	        	}
	        }
		}catch(Exception e){
			e.printStackTrace();
		}
        return null;
	}
}
