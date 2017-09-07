package com.tranzvision.gd.TZWeChatMaterialBundle.service.impl;

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
@Service("com.tranzvision.gd.TZWeChatMaterialBundle.service.impl.TzWeChatMaterialServiceImpl")
public class TzWeChatMaterialServiceImpl extends FrameworkImpl {
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
		/*String strWxAppId=jacksonUtil.getString("wxAppId");
		//素材类型 TP:图片 TW:图文
		String strMediaType=jacksonUtil.getString("mediaType");
		if(strWxAppId==null||"".equals(strWxAppId)){
			return null;
		}*/
		int count=sqlQuery.queryForObject("select count(*) from PS_TZ_WX_MEDIA_TBL where TZ_JG_ID=? ", new Object[]{strOrgId}, "Integer");
		List<Map<String,Object>> list=sqlQuery.queryForList("select TZ_WX_APPID,TZ_XH,TZ_SC_NAME,TZ_MEDIA_ID,TZ_IMAGE_PATH,TZ_MEDIA_TYPE FROM PS_TZ_WX_MEDIA_TBL where TZ_JG_ID=?  LIMIT ?,?", new Object[]{strOrgId,numStart,numLimit});
		if(list!=null &&list.size()>0){
			for(int i=0;i<list.size();i++){
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("appId",list.get(i).get("TZ_WX_APPID").toString());
				map.put("index", Integer.valueOf(list.get(i).get("TZ_XH").toString()));
				map.put("mediaId", list.get(i).get("TZ_MEDIA_ID").toString());
				map.put("caption", "图片"+list.get(i).get("TZ_SC_NAME").toString());
				String strMediaType=list.get(i).get("TZ_MEDIA_TYPE").toString();
				//图片素材
				if("A".equals(strMediaType)){
					map.put("src", list.get(i).get("TZ_IMAGE_PATH").toString());
				}
				//图文素材
				if("B".equals(strMediaType)){
					String imageUrl=sqlQuery.queryForObject("select TZ_HEAD_IMAGE from PS_TZ_WX_TWL_TBL where TZ_JG_ID=?  and TZ_XH=? and TZ_SHCPIC_FLG='Y'", new Object[]{strOrgId,list.get(i).get("TZ_XH").toString()}, "String");
					map.put("src", imageUrl);
				}
				listData.add(map);
			}
			mapRet.replace("total", count);
			mapRet.replace("root", listData);
			return jacksonUtil.Map2json(mapRet);
		}
		return null;
	}
}
