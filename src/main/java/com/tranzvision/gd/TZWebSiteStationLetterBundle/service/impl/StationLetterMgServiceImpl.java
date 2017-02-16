package com.tranzvision.gd.TZWebSiteStationLetterBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
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
	private MessageTextServiceImpl messageTextServiceImpl;
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
	@Autowired
	private FliterForm fliterForm;

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

			// 项目跟目录;
			String rootPath = request.getContextPath();

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
			String ZSGL_URL = request.getContextPath() + "/dispatcher";

			// 1.站内信;
			String stationLetter = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_WEB_ZNX_MESSAGE", "1",
					language, "站内信", "站内信");
			// 获取数据失败，请联系管理员;
			znxCenterHtml = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_WEB_ZNX_MESSAGE", "2",
					language, "获取数据失败，请联系管理员", "获取数据失败，请联系管理员");
			// 2.有;
			String have = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_WEB_ZNX_MESSAGE", "3",
					language, "有", "有");
			// 3.条数未读;
			String unread = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_WEB_ZNX_MESSAGE", "4",
					language, "条未读", "条未读");
			//4.删除;
			String delete = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_WEB_ZNX_MESSAGE", "5",
					language, "删除", "删除");
			//5.发件人;
			String sendName = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_WEB_ZNX_MESSAGE", "6",
					language, "发件人", "发件人");
			//6.主题;
			String theme = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_WEB_ZNX_MESSAGE", "7",
					language, "主题", "主题");
			//7.时间;
			String time = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_WEB_ZNX_MESSAGE", "8",
					language, "时间", "时间");
			String znxCenterList = "";
			//未读和已读状态显示样式;
			String unreadStyle = "erow";
			String readStyle = "erow read_on";
			String sql = "select a.TZ_ZNX_MSGID,(select TZ_REALNAME from PS_TZ_AQ_YHXX_TBL c where c.OPRID = a.TZ_ZNX_SENDID) TZ_ZNX_SENDNAME,"
					+ "a.TZ_MSG_SUBJECT,a.ROW_ADDED_DTTM from PS_TZ_ZNX_MSG_T a,PS_TZ_ZNX_REC_T b where a.TZ_ZNX_MSGID = b.TZ_ZNX_MSGID and b.TZ_ZNX_RECID = ? and b.TZ_REC_DELSTATUS = 'N' limit 0,15";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,new Object[]{oprid});
			if(list != null && list.size() > 0){
				for(int i = 0; i < list.size(); i++){
					String msgId = (String)list.get(i).get("TZ_ZNX_MSGID");
					String sendFirstName = (String)list.get(i).get("TZ_ZNX_SENDNAME");
					String subJect = (String)list.get(i).get("TZ_MSG_SUBJECT");
					String sendTime = (String)list.get(i).get("ROW_ADDED_DTTM").toString();
					//站内信列表;
					String viewStyle = "select TZ_ZNX_STATUS from PS_TZ_ZNX_REC_T WHERE TZ_ZNX_MSGID = ?";
					String znxStatus = jdbcTemplate.queryForObject(viewStyle,new Object[]{msgId},"String");
					
					String strTr = "";
					try{
						if (znxStatus.equals("N")){
							strTr = tzGDObject.getHTMLText("HTML.TZWebStationLetterMgBundle.TZ_WEB_ZNX_MG_LIST",unreadStyle,msgId,sendFirstName,subJect,sendTime);
						}else{
							strTr = tzGDObject.getHTMLText("HTML.TZWebStationLetterMgBundle.TZ_WEB_ZNX_MG_LIST",readStyle,msgId,sendFirstName,subJect,sendTime);
						}
					}catch(TzSystemException e){
						e.printStackTrace();
					}
					znxCenterList += strTr;
				}
			}else{
				//没有站内信;
				znxCenterList = tzGDObject.getHTMLText("HTML.TZWebStationLetterMgBundle.TZ_WEB_ZNX_MG_LIST","erow","123123","系统管理员","飓风今天测试","2017/02/13 20:00");
			}
			//未读站内信数量;
			int unreadCount =0;
			String unreadCountSql = "select count(*) from PS_TZ_ZNX_REC_T where TZ_ZNX_RECID = ? and TZ_REC_DELSTATUS = 'N' and TZ_ZNX_STATUS <>'Y'";
			unreadCount = jdbcTemplate.queryForObject(unreadCountSql,new Object[]{oprid},"int");
			//我的站内信总数;
			int znxCount = 0;
			String znxCountSql= "Select count(*) from PS_TZ_ZNX_REC_T where TZ_ZNX_RECID = ? and TZ_REC_DELSTATUS = 'N'";
			znxCount = jdbcTemplate.queryForObject(znxCountSql,new Object[]{oprid},"int");
			int pageSize = 15;
			int countPage = 0;
			String strPageNav = "";
			if (znxCount > 0){
				if (znxCount%pageSize == 0){
					countPage = znxCount/pageSize;
				}else
					countPage = znxCount/pageSize + 1;
			}
			for (int j = 1;j<countPage;j++){
				strPageNav = tzGDObject.getHTMLText("HTML.TZWebStationLetterMgBundle.TZ_ZNX_TABS_NAV",String.valueOf(j));
				strPageNav += strPageNav;
			}
			System.out.println(countPage+","+strPageNav);
			String strShowPageNav = "";
			if (countPage < 1){
				strShowPageNav = "display:none";
			}
			// 站内信信息;
			znxCenterHtml = tzGDObject.getHTMLText("HTML.TZWebStationLetterMgBundle.TZ_WEB_ZNX_MG_CONTENT",
					request.getContextPath(),stationLetter,have,String.valueOf(unreadCount),unread,delete,sendName,theme,time,znxCenterList,
					strShowPageNav,strPageNav,String.valueOf(countPage));
			// 展示页面;
			znxCenterHtml = tzGDObject.getHTMLText("HTML.TZWebStationLetterMgBundle.TZ_WEB_ZNX_MG_HTML",
					request.getContextPath(), ZSGL_URL, strCssDir, znxCenterHtml, str_jg_id, strSiteId);

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
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { { "ROW_ADDED_DTTM", "ASC" } };

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_ZNX_MSGID", "TZ_ZNX_SENDNAME","TZ_MSG_SUBJECT","ROW_ADDED_DTTM"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);
			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("stationMailId", rowList[0]);
					mapList.put("sendName", rowList[1]);
					mapList.put("stationMailTitle", rowList[2]);
					mapList.put("stationMailReceived", rowList[3]);
					
					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return jacksonUtil.Map2json(mapRet);
	}
	
	/* 删除站内信 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 组件ID;
				
				String strStationMailId = jacksonUtil.getString("stationMailId");
				
				String comPageSql = "UPDATE PS_TZ_ZNX_REC_T SET TZ_REC_DELSTATUS = 'Y' WHERE TZ_ZNX_MSGID = ? AND TZ_ZNX_RECID = ?";
				jdbcTemplate.update(comPageSql,new Object[]{strStationMailId,oprid});		
				
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
}
