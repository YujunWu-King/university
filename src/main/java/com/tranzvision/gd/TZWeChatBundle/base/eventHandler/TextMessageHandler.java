package com.tranzvision.gd.TZWeChatBundle.base.eventHandler;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZWeChatBundle.base.respMessage.TextMessage;
import com.tranzvision.gd.TZWeChatBundle.base.util.MessageUtil;

@Service
public class TextMessageHandler implements EventsHandlerInterface  {

	@Override
	public String execute(Map<String, String> reqMap) {
		
		String openid=reqMap.get("FromUserName"); //用户openid
        String mpid=reqMap.get("ToUserName");   //公众号原始ID
		
        //普通文本消息
        TextMessage txtmsg=new TextMessage();
        txtmsg.setToUserName(openid);
        txtmsg.setFromUserName(mpid);
        txtmsg.setCreateTime(new Date().getTime());
        txtmsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
        txtmsg.setContent("您好，这里是北京创景咨询公众号！");

        String returnStr = MessageUtil.textMessageToXml(txtmsg);
        return returnStr;
	}
}
