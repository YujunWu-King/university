package com.tranzvision.gd.TZCreateClueBundle.service.impl;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.poi.word.WordUtil;
import com.tranzvision.gd.util.session.TzSession;
import com.tranzvision.gd.util.sql.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商机处理类
 * @author zhongcg
 * 2019年7月17日
 *
 */
@Service("com.tranzvision.gd.TZCreateClueBundle.service.impl.TZOpportunityServiceImpl")
public class TZOpportunityServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		//导出拜访报告
		if("importMeeting".equals(oprType)) {
			return importMeeting(strParams, errorMsg);
		}
		if("sendMessage".equals(oprType)) {
			sendMessage(strParams, errorMsg);
		}
		return super.tzOther(oprType, strParams, errorMsg);
	}

	private void sendMessage(String strParams, String[] errorMsg) {

		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			jacksonUtil.json2Map(strParams);

			String EmailOrSmsTmpID = "", bmrGuid = "", psnType = "", tmpMenuId = "";

			// 要跳转的菜单ID
			tmpMenuId = jacksonUtil.getString("tmpMenuId");
			// 人员的Guid
			String opportunityId = jacksonUtil.getString("opportunityId");
			// 要使用的邮件或短信模板
			EmailOrSmsTmpID = jacksonUtil.getString("EmailOrSmsTmpID");
			// 人员类型：A-学生，B-教师
			psnType = jacksonUtil.getString("psnType");

			String TZ_MENUID = sqlQuery.queryForObject(
					"select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?", new Object[] { tmpMenuId },
					"String");

			TzSession tzSession = new TzSession(request);

			tzSession.addSession("opportunityId", opportunityId);
			tzSession.addSession("EmailOrSmsTmpID", EmailOrSmsTmpID);
			tzSession.addSession("psnType", psnType);

			String mode = "no-inquire", model = "content";

			String headIndex = request.getContextPath() + "/index";

			headIndex = headIndex + "?mode=" + mode;
			headIndex = headIndex + "&model=" + model;

			String tmpUrl = headIndex + TZ_MENUID;

			response.sendRedirect(tmpUrl);

		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
	}

	/**
	 * 导出拜访报告
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String importMeeting(String strParams, String[] errorMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			//商机ID
			String opportunityId = jacksonUtil.getString("opportunityId");
			String sql = "select tzms_meeting_name,tzms_meeting_time,tzms_meeting_place,tzms_meeting_per,tzms_hyjy,tzms_hhsy from tzms_meeting_t where tzms_opportunityId = ?";
			List<Map<String, Object>> meetingList = sqlQuery.queryForList(sql, new Object[] {opportunityId});
			String tmpid = sqlQuery.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",  new Object[]{"TZ_MEETING_ID"}, "String");
			if(meetingList != null && meetingList.size() > 0) {
				return jacksonUtil.Map2json(getInputUrlAndOutUrl(tmpid, meetingList));
			}else {
				errorMsg[0] = "1";
				errorMsg[1] = "导出失败,没有拜访报告";
			}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = "导出失败：" + e.getMessage();
		}
		return null;
	}
	
	/**
	 * 根据模板id合并word
	 * @param tmpid 模板id
	 * @param dataList 数据
	 * @return
	 */
	public Map<String, Object> getInputUrlAndOutUrl(String tmpid, List<Map<String, Object>> dataList) {
		Map<String, Object> returnMap = new HashMap<>();
		try {
			String url = sqlQuery.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",  new Object[]{"TZ_DOWNLOAD_FILE_ADD"}, "String");
			//根据主键查询模板Id
			String tblIdSql = "SELECT tzms_word_tmp_id FROM tzms_word_tmp_tBase WHERE tzms_word_tmp_tId =?";
			String tblId = sqlQuery.queryForObject(tblIdSql, new Object[] { tmpid }, "String");
			//根据模板Id查询模板名称、后缀名称及存储方式Id
			String tblInfoSql = "SELECT tzms_file_systemname,tzms_file_extname,tzms_storagetype_id FROM tzms_attachments_tBase WHERE tzms_attachments_tId =?";
			Map<String, Object> tblInfoMap = sqlQuery.queryForMap(tblInfoSql, new Object[] { tblId });
			String storageId = tblInfoMap.get("tzms_storagetype_id").toString();
			//根据存储方式Id获取存储路径
			String getPathSql = "SELECT tzms_storage_path FROM tzms_att_storagetype_tBase WHERE tzms_att_storagetype_tId =?";
			String Path = sqlQuery.queryForObject(getPathSql, new Object[] { storageId }, "String");
			String inputUrl = Path + "\\" + tblInfoMap.get("tzms_file_systemname").toString() + ".docx";
			String filename = "";
			filename = System.currentTimeMillis() + ".docx";
			
			// 存储路径
			String expDirPath = "/statics/DynamicFiles";
			String absexpDirPath = request.getServletContext().getRealPath(expDirPath);
			String outUrl =  absexpDirPath + "\\" + filename;
			//调用静态word工具类
			String msg = WordUtil.createWord(inputUrl, outUrl, dataList, false);
			
			returnMap.put("flag", "succ");
			returnMap.put("absexpDirPath", expDirPath + "\\" + filename);
			returnMap.put("url", url + "\\statics\\DynamicFiles\\" + filename);
		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put("flag", "fail");
			returnMap.put("msg", e.getMessage());
		}
		return returnMap;
	}
}
