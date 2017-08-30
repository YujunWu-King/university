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
import com.tranzvision.gd.TZWeChatMsgBundle.dao.PsTzWxmsgLogTMapper;
import com.tranzvision.gd.TZWeChatMsgBundle.dao.PsTzWxmsgUserTMapper;
import com.tranzvision.gd.TZWeChatMsgBundle.model.PsTzWxmsgLogT;
import com.tranzvision.gd.TZWeChatMsgBundle.model.PsTzWxmsgUserT;
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
		int count=sqlQuery.queryForObject("select count(*) from PS_TZ_WX_TAG_TBL where TZ_JG_ID=? AND TZ_WX_APPID=?", new Object[]{strOrgId,"1"}, "Integer");	
		String sqlTag="select TZ_WX_TAG_ID,TZ_WX_TAG_NAME from PS_TZ_WX_TAG_TBL where TZ_JG_ID=? AND TZ_WX_APPID=?";
	    List<Map<String, Object>>tagList=new ArrayList<Map<String, Object>>();
	    tagList=sqlQuery.queryForList(sqlTag, new Object[]{strOrgId,"1"});
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

	/*@Override
	public String tzQuery(String strParams, String[] errMsg) {
		String strComContent="{}";
		JacksonUtil jacksonUtil=new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String strWxAppId=jacksonUtil.getString("wxAppId");
		String strWxTags=jacksonUtil.getString("weChatTagIDs");
		if(strWxAppId==null||strWxTags==null||"".equals(strWxAppId)||"".equals(strWxTags)){
			return strComContent;
		}
		String strOrgId=tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String arr[]=strWxTags.split(",");
		List<String>  tagNameList=new ArrayList<String>();
	    List<String>  tagIDList=new ArrayList<String>();
	    Map<String,Object> map=new HashMap<String,Object>();
		for(int i=0;i<arr.length;i++){
			String tagId=arr[i];
			if(!"".equals(tagId)){
				tagIDList.add(arr[i]);
				String tagName=sqlQuery.queryForObject("select TZ_WX_TAG_NAME from PS_TZ_WX_TAG_TBL where TZ_JG_ID=? AND TZ_WX_APPID=? AND TZ_WX_TAG_ID=?", new Object[]{strOrgId,strWxAppId,tagId}, "String");
				tagNameList.add(tagName);
			}
		}
		map.put("TagID", tagIDList);
		map.put("TagName", tagNameList);
		return jacksonUtil.Map2json(map);
	}*/
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
	
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
			//将String数据转换成Map
			JacksonUtil jacksonUtil=new JacksonUtil();
			jacksonUtil.json2Map(actData[0]);
			String strSendType = jacksonUtil.getString("sendType");
			Map<String, Object> dataMap = jacksonUtil.getMap("data");
			String strAppId=dataMap.get("appId").toString();
			String strOrgId=tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String strSendMode=dataMap.get("sendMode").toString();
			String strWxTags=dataMap.get("wechatTag").toString();
			String strOpenIds=dataMap.get("openIds").toString();
			String strWordMsg=dataMap.get("wordMessage").toString();
			String strTpMediaId=dataMap.get("tpMediaId").toString();
			String strTwMediaId=dataMap.get("twMediaId").toString();
			String strTwTitle=dataMap.get("twTitle").toString();
			String StrXh=getSeqNum.getSeqNum("TZ_WXMSG_LOG_T", "TZ_XH")+"";
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowTimeStr = simpleDateFormat.format(new Date());
			Date nowTime=null;
			try {
				nowTime = simpleDateFormat.parse(nowTimeStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			PsTzWxmsgLogT PsTzWxmsgLogT=new PsTzWxmsgLogT();
			PsTzWxmsgLogT.setTzWxAppid(strAppId);
			PsTzWxmsgLogT.setTzJgId(strOrgId);
			PsTzWxmsgLogT.setTzXh(StrXh);
			PsTzWxmsgLogT.setTzSendPsn(userId);
			PsTzWxmsgLogT.setTzSendDtime(nowTime);
			PsTzWxmsgLogT.setTzSendMode(strSendMode);
			PsTzWxmsgLogT.setTzSendType(strSendType);
			PsTzWxmsgLogT.setTzSendState("Y");
			//strSendType-A:图片消息,B:图文消息,C:文字消息,D:模板消息
		    if("A".equals(strSendType)){
		    	PsTzWxmsgLogT.setTzMediaId(strTpMediaId);
		    }
		    if("B".equals(strSendType)){
		    	PsTzWxmsgLogT.setTzMediaId(strTwMediaId);
		    	PsTzWxmsgLogT.setTzContent(strTwTitle);
		    }
		    if("C".equals(strSendType)){
		    	PsTzWxmsgLogT.setTzContent(strWordMsg);
		    }
		    psTzWxmsgLogTMapper.insert(PsTzWxmsgLogT);
		    
			//strSendMode-A:指定用户,B:按照标签
			if("A".equals(strSendMode)){
				String  strOpenArray[]=strOpenIds.split(",");
				for(int i=0;i<strOpenArray.length;i++){
					String openId=strOpenArray[i];
					if(!"".equals(openId)&openId!=null){
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
				String  strWxTagArray[]=strWxTags.substring(1,strWxTags.length()-1).split(",");
				for(int i=0;i<strWxTagArray.length;i++){
					String strWxTagId=strWxTagArray[i];
					//strWxTagId=sqlQuery.queryForObject("select TZ_WX_TAG_ID from PS_TZ_WX_TAG_TBL where TZ_JG_ID=? and TZ_WX_APPID=? and TZ_WX_TAG_NAME=?", new Object[]{strOrgId,strAppId,strWxTagName}, "String");
					if(!"".equals(strWxTagId)&strWxTagId!=null){
						String strUserXh=getSeqNum.getSeqNum("TZ_WXMSG_USER_T", "TZ_XH")+""; 
						PsTzWxmsgUserT PsTzWxmsgUserT=new PsTzWxmsgUserT();
						PsTzWxmsgUserT.setTzWxAppid(strAppId);
						PsTzWxmsgUserT.setTzJgId(strOrgId);
						PsTzWxmsgUserT.setTzXh(strUserXh);
						PsTzWxmsgUserT.setTzXhId(strWxTagId);
						psTzWxmsgUserTMapper.insert(PsTzWxmsgUserT);
					}
				}
			}
		}
		return "{\"insert\":\"success\"}";
	}
}
