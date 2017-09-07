package com.tranzvision.gd.TZWeChatBundle.base.eventHandler;

import java.util.Map;

/**
 * 模板消息推送事件处理
 * @author tranzvision
 *
 */
public class TmplSendMessageEventHandler implements EventsHandlerInterface  {
	

	@Override
	public String execute(Map<String, String> reqMap) {
		try{
			String MsgID = reqMap.get("MsgID"); //消息id
			
	        if(MsgID != null && !"".equals(MsgID)){
	        	
	        	//do something
	        }
		}catch(Exception e){
			e.printStackTrace();
		}
        return null;
	}
}
