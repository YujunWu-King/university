package com.tranzvision.gd.TZWeChatBundle.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZWeChatBundle.base.dispatcher.EventDispatcher;
import com.tranzvision.gd.TZWeChatBundle.base.dispatcher.MessageDispatcher;
import com.tranzvision.gd.TZWeChatBundle.base.util.MessageUtil;
import com.tranzvision.gd.TZWeChatBundle.base.util.SignUtil;

@Controller
@RequestMapping(value = "/wxServer")
public class TzWxServerController {

	private Logger logger =Logger.getLogger(TzWxServerController.class);  
    
    @RequestMapping(method =RequestMethod.GET)   
    public void get(HttpServletRequest request,HttpServletResponse response) {   
    	   logger.info("开始微信服务器验证......");  
           // 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。   
           String signature =request.getParameter("signature");   
           // 时间戳   
           String timestamp =request.getParameter("timestamp");   
           // 随机数   
           String nonce =request.getParameter("nonce");   
           // 随机字符串   
           String echostr =request.getParameter("echostr");   
      
           PrintWriter out = null;   
           try {   
               out = response.getWriter();   
               // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，否则接入失败   
               if (SignUtil.checkSignature(signature,timestamp, nonce)) {
                   out.print(echostr);   
               }  
           } catch (IOException e) {   
               e.printStackTrace();   
               logger.error("验证失败");  
           } finally {   
               out.close();   
               out = null;   
           }   
    } 
    
    
    
    @RequestMapping(method =RequestMethod.POST, produces="application/xml;charset=UTF-8")
    @ResponseBody
    public void post(HttpServletRequest request,HttpServletResponse response) {   
    	 try{
             Map<String, String> map = MessageUtil.parseXml(request);
             String msgtype = map.get("MsgType");

             String returnStr = "";
             if(MessageUtil.REQ_MESSAGE_TYPE_EVENT.equals(msgtype)){
            	 returnStr = EventDispatcher.processEvent(map); //进入事件处理
             }else{
            	 returnStr = MessageDispatcher.processMessage(map); //进入消息处理
             }
             
             if(returnStr != null && !"".equals(returnStr)){
            	 response.getWriter().write(returnStr);
             }
         }catch(Exception e){
             e.printStackTrace();
             logger.error("服务器时间处理失败，"+e.getMessage());
         }
    }

}
