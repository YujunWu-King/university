package com.tranzvision.gd.TZWebSiteStationLetterBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 招生网站站内信管理
 *
 */
@Service("com.tranzvision.gd.TZWebSiteStationLetterBundle.service.impl.StationLetterMgServiceImpl")
public class StationLetterMgServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private SiteRepCssServiceImpl siteRepCssServiceImpl;
//	@Autowired
//	private FliterForm fliterForm;

	/****** 站内信 ********/
	@Override
	public String tzGetHtmlContent(String strParams) {
		//返回值
		String znxCenterHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			jacksonUtil.json2Map(strParams);
			String strSiteId = "";
			if (jacksonUtil.containsKey("siteId")) {
				strSiteId = jacksonUtil.getString("siteId");
			}

			if (strSiteId == null || "".equals(strSiteId)) {
				strSiteId = request.getParameter("siteId");
			}

			String page = request.getParameter("page");
			String searchText = request.getParameter("searchText");
			
			// 根据siteid得到机构id;
			String str_jg_id = "";
			// language;
			String language = "";
			String strCssDir = "";
			String siteSQL = "select TZ_JG_ID,TZ_SKIN_STOR,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> siteMap = jdbcTemplate.queryForMap(siteSQL, new Object[] { strSiteId });
			if (siteMap != null) {
				str_jg_id = (String) siteMap.get("TZ_JG_ID");
				String skinstor = (String) siteMap.get("TZ_SKIN_STOR");
				language = (String) siteMap.get("TZ_SITE_LANG");
				String websitePath = getSysHardCodeVal.getWebsiteCssPath();

				String strRandom = String.valueOf(10 * Math.random());
				if ("".equals(skinstor) || skinstor == null) {
					strCssDir = request.getContextPath() + websitePath + "/" + str_jg_id.toLowerCase() + "/" + strSiteId
							+ "/" + "style_" + str_jg_id.toLowerCase() + ".css?v=" + strRandom;
				} else {
					strCssDir = request.getContextPath() + websitePath + "/" + str_jg_id.toLowerCase() + "/" + strSiteId
							+ "/" + skinstor + "/" + "style_" + str_jg_id.toLowerCase() + ".css?v=" + strRandom;
				}
			}

			if (language == null || "".equals(language)) {   
				language = "ZHS";
			}
			// 通用链接;
			String dispatcher = request.getContextPath() + "/dispatcher";
			//未读站内信数量;
			int unreadCount =0;
			String unreadCountSql = "select count(*) from PS_TZ_ZNX_REC_T where TZ_ZNX_RECID = ? and TZ_REC_DELSTATUS = 'N' and TZ_ZNX_STATUS <>'Y'";
			unreadCount = jdbcTemplate.queryForObject(unreadCountSql,new Object[]{oprid},"int");
			
			// 展示页面;
			znxCenterHtml = tzGDObject.getHTMLText("HTML.TZWebStationLetterMgBundle.TZ_WEB_ZNX_MG_HTML",
					true,request.getContextPath(),dispatcher,page,searchText,strCssDir,String.valueOf(unreadCount), str_jg_id, strSiteId);

			znxCenterHtml = siteRepCssServiceImpl.repTitle(znxCenterHtml, strSiteId);
			znxCenterHtml = siteRepCssServiceImpl.repCss(znxCenterHtml, strSiteId);
			
			return znxCenterHtml;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	//站内信列表
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", new ArrayList<Map<String, Object>>());
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			/* 
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { { "ROW_ADDED_DTTM", "DESC" } };

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_ZNX_MSGID","TZ_ZNX_STATUS", "TZ_ZNX_SENDNAME","TZ_MSG_SUBJECT","ROW_ADDED_DTTM"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);
			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("stationMailId", rowList[0]);
					mapList.put("znxStatus", rowList[1]);
					mapList.put("sendName", rowList[2]);
					mapList.put("stationMailTitle", rowList[3]);
					mapList.put("stationMailReceived", rowList[4]);
					
					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}
			*/
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String tmpPwdKey = "TZGD_@_!_*_StationLetter_20170821_Tranzvision";
			jacksonUtil.json2Map(comParams);
			
			String searchText = jacksonUtil.getString("searchText");
			searchText = "%"+ searchText +"%";

			//查询总数
			String totalSql = "SELECT count(B.TZ_ZNX_MSGID) FROM PS_TZ_ZNX_MSG_T A JOIN PS_TZ_ZNX_REC_T B ON (A.TZ_ZNX_MSGID = B.TZ_ZNX_MSGID) WHERE B.TZ_REC_DELSTATUS <> 'Y' and TZ_ZNX_RECID=? AND TZ_MSG_SUBJECT LIKE ?";
			int total = jdbcTemplate.queryForObject(totalSql, new Object[]{ oprid, searchText }, "int");
			
			ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
			if (total > 0) {
				String sql = "SELECT A.TZ_ZNX_MSGID,B.TZ_ZNX_STATUS,A.TZ_MSG_SUBJECT,DATE_FORMAT(A.ROW_ADDED_DTTM, '%Y-%m-%d %H:%i:%s') AS ROW_ADDED_DTTM FROM PS_TZ_ZNX_MSG_T A JOIN PS_TZ_ZNX_REC_T B ON (A.TZ_ZNX_MSGID = B.TZ_ZNX_MSGID) WHERE B.TZ_REC_DELSTATUS <> 'Y' AND TZ_ZNX_RECID=? AND TZ_MSG_SUBJECT LIKE ? ORDER BY A.ROW_ADDED_DTTM DESC , CONVERT( A.TZ_ZNX_MSGID , SIGNED) DESC LIMIT ? , ?";
				List<Map<String,Object>> znxList = jdbcTemplate.queryForList(sql, new Object[]{ oprid, searchText, numStart, numLimit });
				if(znxList != null && znxList.size()>0){
					for(Map<String,Object> znxMap : znxList){
						Map<String,Object> mapJson = new HashMap<String,Object>();
						
						String stationMailId = znxMap.get("TZ_ZNX_MSGID").toString();
						stationMailId = DESUtil.encrypt(stationMailId + "==" + stationMailId, tmpPwdKey);
						
						mapJson.put("stationMailId", stationMailId);
						mapJson.put("znxStatus", znxMap.get("TZ_ZNX_STATUS"));
						mapJson.put("sendName", "");
						mapJson.put("stationMailTitle", znxMap.get("TZ_MSG_SUBJECT"));
						mapJson.put("stationMailReceived", znxMap.get("ROW_ADDED_DTTM"));
						
						listJson.add(mapJson);
					}
				}
			}
			
			mapRet.replace("total", total);
			mapRet.replace("root", listJson);
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return jacksonUtil.Map2json(mapRet);
	}
	
	/* 删除站内信 */
	@Override
	public String tzUpdate(String[] znxData, String[] errMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("result", "success");
		mapRet.put("message", "删除成功");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		String strStationMailId = "";
		String tmpPwdKey = "TZGD_@_!_*_StationLetter_20170821_Tranzvision";
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		// 若参数为空，直接返回;
		if (znxData == null || znxData.length == 0) {
			mapRet.replace("result", "error");
			mapRet.replace("message", "删除失败，参数为空");
			
			return jacksonUtil.Map2json(mapRet);
		}

		try {
			int num = 0;
			int notDelCount = 0;
			for (num = 0; num < znxData.length; num++) {
				jacksonUtil.json2Map(znxData[num]);				
				String encryptMailId = jacksonUtil.getString("mailId");
				
				try{
					encryptMailId = DESUtil.decrypt(encryptMailId, tmpPwdKey);
					if(encryptMailId == null){
						throw new Exception("站内信不存在");
					}
					
					String mailIdArr[] = encryptMailId.split("==");
					if(mailIdArr.length != 2 && mailIdArr[0] != mailIdArr[1]){
						throw new Exception("站内信不存在");
					}else{
						strStationMailId = mailIdArr[0];
					}
				}catch(Exception ee){
					notDelCount ++;
					continue;
				}

				String comPageSql = "UPDATE PS_TZ_ZNX_REC_T SET TZ_REC_DELSTATUS = 'Y' WHERE TZ_ZNX_MSGID = ? AND TZ_ZNX_RECID = ?";
				int rtn = jdbcTemplate.update(comPageSql,new Object[]{strStationMailId,oprid});
				if(rtn <= 0){
					notDelCount ++;
				}
			}
			
			if(notDelCount > 0){
				mapRet.replace("result", "error");
				mapRet.replace("message", notDelCount + "条站内信删除失败。");
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return jacksonUtil.Map2json(mapRet);
	}
}
