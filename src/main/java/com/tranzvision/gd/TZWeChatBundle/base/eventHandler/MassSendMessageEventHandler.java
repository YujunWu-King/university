package com.tranzvision.gd.TZWeChatBundle.base.eventHandler;

import java.util.List;
import java.util.Map;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 群发推送事件处理
 * @author tranzvision
 *
 */
public class MassSendMessageEventHandler implements EventsHandlerInterface  {
	

	@Override
	public String execute(Map<String, String> reqMap) {
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
    	SqlQuery sqlQuery = (SqlQuery) getSpringBeanUtil.getSpringBeanByID("sqlQuery");
    	
		try{
			String MsgID = reqMap.get("MsgID"); //消息id
			int TotalCount = Integer.parseInt(reqMap.get("TotalCount")); 
			int FilterCount = Integer.parseInt(reqMap.get("FilterCount")); 
			int SentCount = Integer.parseInt(reqMap.get("SentCount")); 
			int ErrorCount = Integer.parseInt(reqMap.get("ErrorCount")); 
			
	        if(MsgID != null && !"".equals(MsgID)){
	            String sql = "select TZ_JG_ID,TZ_WX_APPID,TZ_XH from PS_TZ_WXMSG_LOG_T where TZ_MSG_ID=?";
	            List<Map<String,Object>> UserList = sqlQuery.queryForList(sql, new Object[]{ MsgID });
	            
	            if(UserList != null && UserList.size() > 0){
	            	for(Map<String,Object> userMap : UserList){
	            		String orgId = userMap.get("TZ_JG_ID").toString();
	            		String appId = userMap.get("TZ_WX_APPID").toString();
	            		String orderNum = userMap.get("TZ_XH").toString();
	            		
	            		String updateSql = "update PS_TZ_WXMSG_LOG_T set TZ_S_TOTAL=?,TZ_S_FILTER=?,TZ_S_SUCESS=?,TZ_S_FAIL=? where TZ_JG_ID=? and TZ_WX_APPID=? and TZ_XH=?";
	            		sqlQuery.update(updateSql, new Object[]{ TotalCount, FilterCount, SentCount, ErrorCount, orgId, appId, orderNum });
	            	}
	            }
	        }
		}catch(Exception e){
			e.printStackTrace();
		}
        return null;
	}
}
