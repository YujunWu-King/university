package com.tranzvision.gd.TZWeChatMsgBundle.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWeChatBundle.service.impl.TzWxApiObject;
import com.tranzvision.gd.TZWeChatMsgBundle.dao.PsTzWxmsgLogTMapper;
import com.tranzvision.gd.TZWeChatMsgBundle.dao.PsTzWxmsgUserTMapper;
import com.tranzvision.gd.TZWeChatMsgBundle.model.PsTzWxmsgLogT;
import com.tranzvision.gd.TZWeChatMsgBundle.model.PsTzWxmsgUserT;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
@Service("com.tranzvision.gd.TZWeChatMsgBundle.service.impl.TZWeChatMsgServiceImpl")
public class TZWeChatMsgServiceImpl extends FrameworkImpl {
	
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsTzWxmsgLogTMapper psTzWxmsgLogTMapper;
	@Autowired
	private PsTzWxmsgUserTMapper psTzWxmsgUserTMapper;

	
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg)  {
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

		String strOrgId=tzLoginServiceImpl.getLoginedManagerOrgid(request);
		JacksonUtil jacksonUtil=new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String strWxAppId=jacksonUtil.getString("wxAppId");
		if(strWxAppId==null||strWxAppId.equals("")){
			return null;
		}
		int count=sqlQuery.queryForObject("select count(*) from PS_TZ_WX_TAG_TBL where TZ_JG_ID=? AND TZ_WX_APPID=?", new Object[]{strOrgId,strWxAppId}, "Integer");	
		String sqlTag="select TZ_WX_TAG_ID,TZ_WX_TAG_NAME from PS_TZ_WX_TAG_TBL where TZ_JG_ID=? AND TZ_WX_APPID=?";
	    List<Map<String, Object>>tagList=new ArrayList<Map<String, Object>>();
	    tagList=sqlQuery.queryForList(sqlTag, new Object[]{strOrgId,strWxAppId});
		if (tagList != null && tagList.size() > 0) {
			for (int i = 0; i < tagList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("tagId", tagList.get(i).get("TZ_WX_TAG_ID").toString());
				map.put("tagName", tagList.get(i).get("TZ_WX_TAG_NAME").toString());
				listData.add(map);
			}
			mapRet.replace("total", count);
			mapRet.replace("root", listData);
		}
		
		return jacksonUtil.Map2json(mapRet);
	}

	/*
	 * 发送微信消息
	 * LDD
	 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		//参数为空直接跳出方法
		if(actData!=null)
		{
			//获取UserId 如何用户为“TZ_GUEST”->"请先登录再操作";
			String userId= tzLoginServiceImpl.getLoginedManagerOprid(request);
			if(userId.equals("TZ_GUEST"))
			{
				errMsg[0]="1";
				errMsg[1]="请先登录再操作";
				return null;
			}
	
			jacksonUtil.json2Map(actData[0]);
			String strSendType = jacksonUtil.getString("sendType");
			Map<String, Object> dataMap = jacksonUtil.getMap("data");
			String strAppId=dataMap.get("appId").toString();
			String strOrgId=tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String strSendMode=dataMap.get("sendMode").toString();
			String strWxTag=dataMap.get("wechatTag").toString();
			String strOpenIds=dataMap.get("openIds").toString();
			String strWordMsg=dataMap.get("wordMessage").toString();
			String strTpMediaId=dataMap.get("tpMediaId").toString();
			String strTwMediaId=dataMap.get("twMediaId").toString();
			String strTwTitle=dataMap.get("twTitle").toString();
			
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowTimeStr = simpleDateFormat.format(new Date());
			Date nowTime=null;
			try {
				nowTime = simpleDateFormat.parse(nowTimeStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			PsTzWxmsgLogT PsTzWxmsgLogT=new PsTzWxmsgLogT();
			//strSendType-A:图片消息,B:图文消息,C:文字消息,D:模板消息
			 String mediaIdOrContent="";
			 String msgtype="";
		    if("A".equals(strSendType)){
		    	PsTzWxmsgLogT.setTzMediaId(strTpMediaId);
		    	mediaIdOrContent=strTpMediaId;
		    	msgtype="image";
		    }
		    if("B".equals(strSendType)){
		    	PsTzWxmsgLogT.setTzMediaId(strTwMediaId);
		    	PsTzWxmsgLogT.setTzContent(strTwTitle);
		    	strTpMediaId=strTwMediaId;
		    	msgtype="mpnews";
		    }
		    if("C".equals(strSendType)){
		    	PsTzWxmsgLogT.setTzContent(strWordMsg);
		    	strTwMediaId=strWordMsg;
		    	msgtype="text";
		    }
		    
		    //TzWxApiObject TzWxApiObject=new TzWxApiObject();
		    GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
        	TzWxApiObject tzWxApiObject = (TzWxApiObject) getSpringBeanUtil.getSpringBeanByID("tzWxApiObject");
		    String strMsgId="";
		    String strSendStatus="N";
			//strSendMode-A:指定用户,B:按照标签
			if("A".equals(strSendMode)){
				String  strOpenArray[]=strOpenIds.split(",");
				Map<String,Object> map=tzWxApiObject.messageMassSendByOpenid(strOrgId, strAppId, mediaIdOrContent, msgtype, strOpenArray, "", "");
				if("0".equals(map.get("errcode").toString())){
					strMsgId=map.get("msg_id").toString();
					Map<String,Object> statusMap=tzWxApiObject.getMassSendStatus(strOrgId, strAppId, strMsgId);
					strSendStatus=statusMap.get("msg_status").toString();
					if("SEND_SUCCESS".equals(strSendStatus)){
						strSendStatus="Y";
					}
				}else{
					returnMap.put("errcode", "-1");
					returnMap.put("errmsg", "指定用户消息发送失败！");
					return jacksonUtil.Map2json(map);
				}
				for(int i=0;i<strOpenArray.length;i++){
					String openId=strOpenArray[i];
					if(!"".equals(openId)){
					String strUserXh=getSeqNum.getSeqNum("TZ_WXMSG_USER_T", "TZ_XH")+""; 
					PsTzWxmsgUserT PsTzWxmsgUserT=new PsTzWxmsgUserT();
					PsTzWxmsgUserT.setTzWxAppid(strAppId);
					PsTzWxmsgUserT.setTzJgId(strOrgId);
					PsTzWxmsgUserT.setTzXh(strUserXh);
					PsTzWxmsgUserT.setTzXhId(openId);
					psTzWxmsgUserTMapper.insert(PsTzWxmsgUserT);
				  }
				}
				
			}
			//按照标签
			if("B".equals(strSendMode)){
					if(!"".equals(strWxTag)){
						Map<String,Object> map=tzWxApiObject.messageMassSendByTag(strOrgId, strAppId, mediaIdOrContent, msgtype, strWxTag, "", "");
						if("0".equals(map.get("errcode").toString())){
							strMsgId=map.get("msg_id").toString();
							Map<String,Object> statusMap=tzWxApiObject.getMassSendStatus(strOrgId, strAppId, strMsgId);
							strSendStatus=statusMap.get("msg_status").toString();
							if("SEND_SUCCESS".equals(strSendStatus)){
								strSendStatus="Y";
							}
						}else{
							returnMap.put("errcode", "-1");
							returnMap.put("errmsg", "指定用户消息发送失败！");
							return jacksonUtil.Map2json(map);
						}
						String strUserXh=getSeqNum.getSeqNum("TZ_WXMSG_USER_T", "TZ_XH")+""; 
						PsTzWxmsgUserT PsTzWxmsgUserT=new PsTzWxmsgUserT();
						PsTzWxmsgUserT.setTzWxAppid(strAppId);
						PsTzWxmsgUserT.setTzJgId(strOrgId);
						PsTzWxmsgUserT.setTzXh(strUserXh);
						PsTzWxmsgUserT.setTzXhId(strWxTag);
						psTzWxmsgUserTMapper.insert(PsTzWxmsgUserT);
					}
				
			}
			//微信消息发送日志
			String StrXh=getSeqNum.getSeqNum("TZ_WXMSG_LOG_T", "TZ_XH")+"";
			PsTzWxmsgLogT.setTzWxAppid(strAppId);
			PsTzWxmsgLogT.setTzJgId(strOrgId);
			PsTzWxmsgLogT.setTzXh(StrXh);
			PsTzWxmsgLogT.setTzSendPsn(userId);
			PsTzWxmsgLogT.setTzSendDtime(nowTime);
			PsTzWxmsgLogT.setTzSendMode(strSendMode);
			PsTzWxmsgLogT.setTzSendType(strSendType);
			PsTzWxmsgLogT.setTzSendState(strSendStatus);
			PsTzWxmsgLogT.setTzMsgId(strMsgId);
			psTzWxmsgLogTMapper.insert(PsTzWxmsgLogT);
		}
		returnMap.put("errcode", "0");
		returnMap.put("errmsg", "微信消息发送成功");
	    return jacksonUtil.Map2json(returnMap);
	}
}
