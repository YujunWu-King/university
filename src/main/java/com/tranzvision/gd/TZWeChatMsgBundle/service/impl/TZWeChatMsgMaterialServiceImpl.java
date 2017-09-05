package com.tranzvision.gd.TZWeChatMsgBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZWeChatMsgBundle.service.impl.TZWeChatMsgMaterialServiceImpl")
public class TZWeChatMsgMaterialServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
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
		//素材类型 TP:图片 TW:图文
		String strMediaType=jacksonUtil.getString("mediaType");
		if(strWxAppId==null||"".equals(strWxAppId)){
			return null;
		}
		//选择图片素材
		if("TP".equals(strMediaType)){
			int countPic=sqlQuery.queryForObject("select count(*) from PS_TZ_WX_MEDIA_TBL where TZ_JG_ID=? and TZ_WX_APPID=? and TZ_MEDIA_TYPE='A'", new Object[]{strOrgId,strWxAppId}, "Integer");
		    List<Map<String,Object>> list=sqlQuery.queryForList("select TZ_XH,TZ_SC_NAME,TZ_MEDIA_ID,TZ_IMAGE_PATH FROM PS_TZ_WX_MEDIA_TBL where TZ_JG_ID=? and TZ_WX_APPID=? AND TZ_MEDIA_TYPE='A' LIMIT ?,?", new Object[]{strOrgId,strWxAppId,numStart,numLimit});
			if(list!=null &&list.size()>0){
				for(int i=0;i<list.size();i++){
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("index", Integer.valueOf(list.get(i).get("TZ_XH").toString()));
					map.put("mediaId", list.get(i).get("TZ_MEDIA_ID").toString());
					map.put("src", list.get(i).get("TZ_IMAGE_PATH").toString());
					map.put("caption", list.get(i).get("TZ_SC_NAME").toString());
					listData.add(map);
				}
				mapRet.replace("total", countPic);
				mapRet.replace("root", listData);
				return jacksonUtil.Map2json(mapRet);
			}
		}
		//选择图文素材
		if("TW".equals(strMediaType)){
			int countTw=sqlQuery.queryForObject("select count(*) from PS_TZ_WX_MEDIA_TBL where TZ_JG_ID=? and TZ_WX_APPID=? and TZ_MEDIA_TYPE='B'", new Object[]{strOrgId,strWxAppId}, "Integer");
		    List<Map<String,Object>> list=sqlQuery.queryForList("select TZ_XH,TZ_MEDIA_ID FROM PS_TZ_WX_MEDIA_TBL where TZ_JG_ID=? and TZ_WX_APPID=? AND TZ_MEDIA_TYPE='B' LIMIT ?,?", new Object[]{strOrgId,strWxAppId,numStart,numLimit});
			if(list!=null &&list.size()>0){
				for(int i=0;i<list.size();i++){
					Map<String,Object> map=new HashMap<String,Object>();
					String strXh=list.get(i).get("TZ_XH").toString();
					String strMediaId=list.get(i).get("TZ_MEDIA_ID").toString();
					String imageUrl=sqlQuery.queryForObject("select TZ_HEAD_IMAGE from PS_TZ_WX_TWL_TBL where TZ_JG_ID=? and TZ_WX_APPID=? and TZ_XH=? and TZ_SHCPIC_FLG='Y'", new Object[]{strOrgId,strWxAppId,strXh}, "String");
					String caption= sqlQuery.queryForObject("select TZ_TW_TITLE from PS_TZ_WX_TWL_TBL where TZ_JG_ID=? and TZ_WX_APPID=? and TZ_XH=? and TZ_SHCPIC_FLG='Y'", new Object[]{strOrgId,strWxAppId,strXh}, "String");
					map.put("index",Integer.valueOf(strXh));
					map.put("mediaId", strMediaId);
					map.put("src",imageUrl);
					map.put("caption",caption);
					listData.add(map);
				}
				mapRet.replace("total", countTw);
				mapRet.replace("root", listData);
				return jacksonUtil.Map2json(mapRet);
			}
		}
		
		return null;
	}
}
