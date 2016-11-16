package com.tranzvision.gd.TZInternalSiteSearch.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

@Controller
@RequestMapping(value = "/")
public class InternalSiteInfoSearch {
	@Autowired
	private SqlQuery sqlQuery;
	final String filterPattern = "[<>{}\\[\\];\\&]";
	
	@ResponseBody
	@RequestMapping(value = "search" ,produces = "text/html;charset=UTF-8")
	public String findactivityByName(String siteId,String keyWords) {
		//System.out.println(keyWords);
		
		if(keyWords!=null&&!keyWords.equals("")){
			
			
			siteId = siteId.replaceAll(filterPattern,"");
			keyWords = keyWords.replaceAll(filterPattern,"");
			List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
			//根据关键字查询活动标题，名称和ID,链接url
			String sql = "SELECT A.TZ_ART_ID  activityId,A.TZ_ART_TITLE activityTitle,A.TZ_ART_NAME activityName,B.TZ_STATIC_ART_URL activityUrl  FROM PS_TZ_ART_REC_TBL A,PS_TZ_LM_NR_GL_T B WHERE  A.TZ_ART_ID = B.TZ_ART_ID AND B.TZ_SITE_ID=? AND B.TZ_ART_HTML !='<!DOCTYPE html>' AND B.TZ_ART_HTML !='未找到对应的模版' AND B.TZ_STATIC_ART_URL IS NOT NULL AND B.TZ_STATIC_ART_URL!='' AND  B.TZ_ART_PUB_STATE='Y' AND(UPPER(A.TZ_ART_NAME) LIKE '%"+keyWords+"%'"+" OR UPPER(A.TZ_ART_CONENT) LIKE '%"+keyWords+"%') ";
			//System.out.println(sql);
			list=sqlQuery.queryForList(sql,new Object[]{siteId});
			JacksonUtil jacksonUtil=new JacksonUtil();
			String jsonStr="";
			if(list!=null){
				jsonStr=jacksonUtil.List2json((ArrayList<Map<String,Object>>) list);
				//System.out.println(jsonStr);
				jsonStr="var result={\"list\":"+jsonStr+"}";
				//System.out.println("-----\n"+jsonStr);
				return jsonStr;
				
			}
		}
		return null;
	}

	
}
