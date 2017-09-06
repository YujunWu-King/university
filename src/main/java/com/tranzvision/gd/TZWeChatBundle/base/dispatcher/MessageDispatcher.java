package com.tranzvision.gd.TZWeChatBundle.base.dispatcher;

import java.util.Map;

import com.tranzvision.gd.TZWeChatBundle.base.eventHandler.TextMessageHandler;
import com.tranzvision.gd.TZWeChatBundle.base.util.MessageUtil;


/**
 * 消息业务处理分发器
 * @author zhanglang
 *
 */
public class MessageDispatcher {
	
	/**
	 * 返回XML，如果返回信息给用户，直接返回null
	 * @param map
	 * @return
	 */
	public static String processMessage(Map<String, String> map) {
		try{
			//System.out.println("请求："+map.toString());
			/**
			 * 文本消息
			 */
	        if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) { // 文本消息
	        	TextMessageHandler textMessageHandler =  new TextMessageHandler();
	        	String respXml = textMessageHandler.execute(map);
	        	return respXml;
	        }
	         
	        /**
			 * 图片消息
			 */
	        if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) { // 图片消息
	            //System.out.println("==============这是图片消息！");
	        }
	         
	        /**
			 * 链接消息
			 */
	        if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) { // 链接消息
	            //System.out.println("==============这是链接消息！");
	        }
	         
	        /**
			 * 位置消息
			 */ 
	        if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) { // 位置消息
	            //System.out.println("==============这是位置消息！");
	        }
	         
	        /**
			 * 视频消息
			 */
	        if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_VIDEO)) { // 视频消息
	            //System.out.println("==============这是视频消息！");
	        }
	         
	        
	        /**
			 * 语音消息
			 */
	        if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) { // 语音消息
	            //System.out.println("==============这是语音消息！");
	        }
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("异常:"+e.getMessage());
		}
        return null;
    }
}
