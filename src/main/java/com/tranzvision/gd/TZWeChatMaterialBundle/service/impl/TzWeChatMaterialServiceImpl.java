package com.tranzvision.gd.TZWeChatMaterialBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWeChatBundle.service.impl.TzWxApiObject;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
@Service("com.tranzvision.gd.TZWeChatMaterialBundle.service.impl.TzWeChatMaterialServiceImpl")
public class TzWeChatMaterialServiceImpl extends FrameworkImpl {
	
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private HttpServletRequest request;
	
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg)  {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_JG_ID", "TZ_WX_APPID", "TZ_XH", "TZ_SC_NAME", "TZ_IMAGE_PATH",
					"TZ_MEDIA_ID", "TZ_MEDIA_TYPE", "TZ_PUB_STATE" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);
            String cxtPath=request.getContextPath();
			if (obj != null && obj.length > 0) {
				@SuppressWarnings("unchecked")
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();

					String strPicName = ""; 
					String flag="";
					if("A".equals(rowList[6])){
						flag="图片:";
					}else{
						flag="图文:";
					}
					if ("Y".equals(rowList[7])) {
						strPicName =  "[已发布]"+flag + rowList[3];
					} else {
						strPicName =  "[未发布]"+flag + rowList[3];
					}
					mapList.put("jgId", rowList[0]);
					mapList.put("wxAppId", rowList[1]);
					mapList.put("index", rowList[2]);
					mapList.put("caption", strPicName);
					mapList.put("src", rowList[4]);
					mapList.put("mediaId", rowList[5]);
					mapList.put("mediaType", rowList[6]);
					mapList.put("state", rowList[7]);
					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);

		/*
		 * ArrayList<Map<String, Object>> listData = new ArrayList<Map<String,
		 * Object>>();
		 * 
		 * //String strOrgId=tzLoginServiceImpl.getLoginedManagerOrgid(request);
		 * JacksonUtil jacksonUtil=new JacksonUtil();
		 * jacksonUtil.json2Map(strParams); String
		 * strWxAppId=jacksonUtil.getString("wxAppId"); String
		 * strOrgId=jacksonUtil.getString("jgId");
		 * if("".equals(strWxAppId)||"".equals(strOrgId)){ return null; } int
		 * count=sqlQuery.queryForObject(
		 * "select count(*) from PS_TZ_WX_MEDIA_TBL where TZ_JG_ID=? and TZ_WX_APPID=?"
		 * , new Object[]{strOrgId,strWxAppId}, "Integer");
		 * List<Map<String,Object>> list=sqlQuery.queryForList(
		 * "select TZ_XH,TZ_SC_NAME,TZ_MEDIA_ID,TZ_IMAGE_PATH,TZ_MEDIA_TYPE FROM PS_TZ_WX_MEDIA_TBL where TZ_JG_ID=? and TZ_WX_APPID=? LIMIT ?,?"
		 * , new Object[]{strOrgId,strWxAppId,numStart,numLimit}); if(list!=null
		 * &&list.size()>0){ for(int i=0;i<list.size();i++){ Map<String,Object>
		 * map=new HashMap<String,Object>(); String mediaId="";
		 * if(list.get(i).get("TZ_MEDIA_ID")!=null){
		 * mediaId=list.get(i).get("TZ_MEDIA_ID").toString(); }; map.put("jgId",
		 * strOrgId); map.put("wxAppId", strWxAppId); map.put("index",
		 * Integer.valueOf(list.get(i).get("TZ_XH").toString()));
		 * map.put("mediaId", mediaId); map.put("caption",
		 * "图片:"+list.get(i).get("TZ_SC_NAME").toString());
		 * 
		 * String strMediaType=list.get(i).get("TZ_MEDIA_TYPE").toString();
		 * map.put("mediaType", strMediaType); //图片素材
		 * if("A".equals(strMediaType)){ map.put("src",
		 * list.get(i).get("TZ_IMAGE_PATH").toString()); } //图文素材
		 * if("B".equals(strMediaType)){ String
		 * imageUrl=sqlQuery.queryForObject(
		 * "select TZ_HEAD_IMAGE from PS_TZ_WX_TWL_TBL where TZ_JG_ID=?  and TZ_XH=? and TZ_SHCPIC_FLG='Y'"
		 * , new Object[]{strOrgId,list.get(i).get("TZ_XH").toString()},
		 * "String"); map.put("src", imageUrl); } listData.add(map); }
		 * mapRet.replace("total", count); mapRet.replace("root", listData);
		 * return jacksonUtil.Map2json(mapRet); }
		 */
		
	}
	
	/* 删除 */
	@Override
	@Transactional
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		if (actData.length == 0) {
			return strRet;
		}

		int dataLength = actData.length;
		for (int num = 0; num < dataLength; num++) {
			// 表单内容
			String strForm = actData[num];
			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strForm);
			String jgId = jacksonUtil.getString("jgId");
			String wxAppId=jacksonUtil.getString("wxAppId");
			String tzSeq=jacksonUtil.getString("index");
            String mediaType=jacksonUtil.getString("mediaType");
			if (!StringUtils.isBlank(jgId)&&!StringUtils.isBlank(wxAppId)&&!StringUtils.isBlank(tzSeq)) {
				this.deleteScInfo(jgId,wxAppId,tzSeq,mediaType, errMsg);
			}
		}
		return strRet;
	}

	private void deleteScInfo(String jgId, String wxAppId, String tzSeq,String mediaType, String[] errMsg) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
	        TzWxApiObject tzWxApiObject = (TzWxApiObject) getSpringBeanUtil.getSpringBeanByID("tzWxApiObject");
	        
			// 删除指定控件id的控件信息
			Object[] args = new Object[] { jgId,wxAppId,tzSeq };
			String tpSql = "DELETE FROM  PS_TZ_WX_MEDIA_TBL WHERE TZ_JG_ID=? AND TZ_WX_APPID=? AND TZ_XH=?";
			String twSql="DELETE FROM PS_TZ_WX_TWL_TBL WHERE TZ_JG_ID=? AND TZ_WX_APPID=? AND TZ_XH=?";
			int i=0,j=0 ;
			String mediaId=sqlQuery.queryForObject("select TZ_MEDIA_ID from PS_TZ_WX_MEDIA_TBL where TZ_JG_ID=? AND TZ_WX_APPID=? AND TZ_XH=?", new Object[]{jgId,wxAppId,tzSeq}, "String");
			if(mediaId!=null&&!"".equals(mediaId)){
				//从微信短删除永久素材
				Map<String,Object> map=tzWxApiObject.deleteMaterial(jgId, wxAppId, mediaId);
				if(!"0".equals(map.get("errcode").toString())){
					errMsg[0] = "1";
					errMsg[1] = "删除素材失败！";
				}
				
			}
			//图片素材
			if("A".equals(mediaType)){
				i=sqlQuery.update(tpSql, args);
			}
			//图文素材
			if("B".equals(mediaType)){
				i=sqlQuery.update(tpSql, args);
				j=sqlQuery.update(twSql,args);
			}
		
			if (i <= 0) {
				errMsg[0] = "1";
				errMsg[1] = "删除图片素材失败！";
			} else {
				if("B".equals(mediaType)&&j<=0){
					errMsg[0] = "1";
					errMsg[1] = "删除图文素材失败！";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
	}
}
