package com.tranzvision.gd.TZWeChatBundle.base.eventHandler;

import java.util.List;
import java.util.Map;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 关注事件处理
 * @author tranzvision
 *
 */
public class UnSubscribeEventHandler implements EventsHandlerInterface  {
	

	@Override
	public String execute(Map<String, String> reqMap) {
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
    	SqlQuery sqlQuery = (SqlQuery) getSpringBeanUtil.getSpringBeanByID("sqlQuery");
    	
		try{
			String openid=reqMap.get("FromUserName"); //用户openid
	        //String mpid=reqMap.get("ToUserName");   //公众号原始ID
			
	        if(openid != null && !"".equals(openid)){
	            String sql = "select TZ_JG_ID,TZ_WX_APPID from PS_TZ_WX_USER_TBL where TZ_OPEN_ID=?";
	            List<Map<String,Object>> UserList = sqlQuery.queryForList(sql, new Object[]{ openid });
	            
	            if(UserList != null && UserList.size() > 0){
	            	for(Map<String,Object> userMap : UserList){
	            		String orgId = userMap.get("TZ_JG_ID").toString();
	            		String appId = userMap.get("TZ_WX_APPID").toString();
	            		
	            		String updateSql = "update PS_TZ_WX_USER_TBL set TZ_SUBSCRIBE='0' where TZ_JG_ID=? and TZ_WX_APPID=? and TZ_OPEN_ID=?";
	            		sqlQuery.update(updateSql, new Object[]{ orgId, appId, openid });
	            	}
	            }
	        }
		}catch(Exception e){
			e.printStackTrace();
		}
        return null;
	}
}
