package com.tranzvision.gd.TZWeChatBundle.base.dispatcher;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZWeChatBundle.base.eventHandler.MassSendMessageEventHandler;
import com.tranzvision.gd.TZWeChatBundle.base.eventHandler.SubscribeEventHandler;
import com.tranzvision.gd.TZWeChatBundle.base.eventHandler.TmplSendMessageEventHandler;
import com.tranzvision.gd.TZWeChatBundle.base.eventHandler.UnSubscribeEventHandler;
import com.tranzvision.gd.TZWeChatBundle.base.util.MessageUtil;


/**
 * 事件消息业务分发器
 * @author zhanglang
 *
 */
@Service
public class EventDispatcher {

	public static String processEvent(Map<String, String> map) {
		try{
			//System.out.println("请求："+map.toString());
			/**
	         * 关注推送事件
	         */
	        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) { //关注事件
	            //System.out.println("==============这是关注事件！");
	            SubscribeEventHandler subscribeEventHandler = new SubscribeEventHandler();
	            String respXml = subscribeEventHandler.execute(map);
	            return respXml;
	        }
	         
	        
	        /**
	         * 取消关注推送事件
	         */
	        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) { //取消关注事件
	            //System.out.println("==============这是取消关注事件！");
	            UnSubscribeEventHandler unSubscribeEventHandler = new UnSubscribeEventHandler();
	            String respXml = unSubscribeEventHandler.execute(map);
	            return respXml;
	        }
	        
	        
	        /**
	         * 群发消息发送完成推送事件
	         */
	        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_MASSSENDJOBFINISH)) { //群发消息发送完成
	            //System.out.println("==============群发消息发送任务完成！");
	            MassSendMessageEventHandler massSendMessageEventHandler = new MassSendMessageEventHandler();
	            String respXml = massSendMessageEventHandler.execute(map);
	            return respXml;
	        }
	        
	        
	        /**
	         * 模板消息发送完成推送事件
	         */
	        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_TEMPLATESENDJOBFINISH)) { //模板消息发送完成
	            //System.out.println("==============模板消息发送任务完成！");
	        	TmplSendMessageEventHandler tmplSendMessageEventHandler = new TmplSendMessageEventHandler();
	            String respXml = tmplSendMessageEventHandler.execute(map);
	            return respXml;
	        }
	        
	         
	        /**
	         * 扫描二维码推送事件
	         */
	        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_SCAN)) { //扫描二维码事件
	            //System.out.println("==============这是扫描二维码事件！");
	        }
	         
	        
	        /**
	         * 位置上报推送事件
	         */
	        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_LOCATION)) { //位置上报事件
	            //System.out.println("==============这是位置上报事件！");
	        }
	         
	        /**
	         * 自定义菜单点击事件
	         */
	        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_CLICK)) { //自定义菜单点击事件
	            //System.out.println("==============这是自定义菜单点击事件！");
	        }
	         
	        
	        /**
	         * 自定义菜单view事件
	         */
	        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_VIEW)) { //自定义菜单View事件
	            //System.out.println("==============这是自定义菜单View事件！");
	        }
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("异常:"+e.getMessage());
		}
        return null;
    }
}
