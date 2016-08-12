package com.tranzvision.gd.TZEmailSmsQFBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzDxYjDsfsTMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzDxYjQfSjrTMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzDxyjQfDyTMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzYjQfFjXxTblMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzDxYjDsfsT;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzDxYjQfSjrT;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzDxyjQfDyTWithBLOBs;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzYjQfFjXxTbl;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzYjQfFjXxTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/*
 * 原PS类：TZ_GD_BULKES_PKG:TZ_EMAILBULK_DET_CLS 
 */
@Service("com.tranzvision.gd.TZEmailSmsQFBundle.service.impl.TzEmailBulkDetClsServiceImpl")
public class TzEmailBulkDetClsServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private PsTzDxyjQfDyTMapper psTzDxyjQfDyTMapper;

	@Autowired
	private PsTzDxYjDsfsTMapper psTzDxYjDsfsTMapper;

	@Autowired
	private PsTzDxYjQfSjrTMapper psTzDxYjQfSjrTMapper;
	
	@Autowired
	private PsTzYjQfFjXxTblMapper psTzYjQfFjXxTblMapper;
	
	@Autowired
	private CreateQfTaskServiceImpl createQfTaskServiceImpl;
	
	@Autowired
	private SendSmsOrMalQfServiceImpl sendSmsOrMalQfServiceImpl;

	/* 获取页面信息 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strComContent = "{}";

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		if (jacksonUtil.containsKey("queryID")) {
			String queryID = jacksonUtil.getString("queryID");
			if ("sender".equals(queryID)) {
				strComContent = this.querySenderList(comParams, errorMsg);
			}

			if ("recever".equals(queryID)) {
				strComContent = this.queryReceverList(comParams, errorMsg);
			}

			if ("CC".equals(queryID)) {
				strComContent = this.queryCCList(comParams, errorMsg);
			}

			if ("emltmpl".equals(queryID)) {
				strComContent = this.queryEmlTmplList(comParams, errorMsg);
			}

			if ("atta".equals(queryID)) {
				strComContent = this.queryAttaList(comParams, errorMsg);
			}
		} else {
			errorMsg[0] = "1";
			errorMsg[1] = "参数不正确！";
		}

		return strComContent;
	}

	/* 获取页面信息 */
	@Override
	public String tzOther(String OperateType, String comParams, String[] errorMsg) {
		// 返回值;
		String strResultConten = "{}";
		boolean bl = false;

		if ("getCreInfo".equals(OperateType)) {
			bl = true;
			strResultConten = this.getCreInfo(comParams, errorMsg);
		}

		if ("getEmlTmpInfo".equals(OperateType)) {
			bl = true;
			strResultConten = this.getEmlTmplInfo(comParams, errorMsg);
		}

		if ("getEmlTmpItem".equals(OperateType)) {
			strResultConten = this.getEmlTmplItem(comParams, errorMsg);
			bl = true;
		}

		if ("getEmlMetaTmpInfo".equals(OperateType)) {
			strResultConten = this.getEmlMetaTmpInfo(comParams, errorMsg);
			bl = true;
		}

		if ("save".equals(OperateType)) {
			strResultConten = this.saveEmlBulkInfo(comParams, errorMsg);
			bl = true;
		}

		if ("sendEml".equals(OperateType)) {
			strResultConten = this.sendEml(comParams, errorMsg);
			bl = true;
		}

		if ("revoke".equals(OperateType)) {
			strResultConten = this.revoke(comParams, errorMsg);
			bl = true;
		}

		if ("clearAll".equals(OperateType)) {
			strResultConten = this.clearAll(comParams, errorMsg);
			bl = true;
		}

		if ("getRwzxZt".equals(OperateType)) {
			bl = true;
			strResultConten = this.getRwzxZt(comParams, errorMsg);
		}

		if (bl == false) {
			errorMsg[0] = "1";
			errorMsg[1] = "未找到[" + OperateType + "]对应处理方法.";
		}

		return strResultConten;
	}

	// 获取群发任务信息;
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回;
		Map<String, Object> map = new HashMap<>();
		map.put("emlQfId", "");
		map.put("emlQfDesc", "");
		map.put("crePer", "");
		map.put("sender", "");
		map.put("sendModel", "");
		// 收件人s;
		ArrayList<String> arrrecever = new ArrayList<>();
		map.put("recever", arrrecever);
		map.put("tsfsFlag", "");
		map.put("tsfsEmail", "");
		ArrayList<String> mailCCList = new ArrayList<>();
		map.put("mailCC", mailCCList);
		map.put("emlTmpId", "");
		map.put("emlSubj", "");
		map.put("emlCont", "");
		map.put("edmFlag", "");
		map.put("qxdyFlag", "");
		map.put("qypfFlag", "");
		map.put("fsslXs", "");
		map.put("dsfsFlag", "");
		map.put("dsfsDate", "");
		map.put("dsfsTime", "");
		map.put("qzfsFlag", "");
		map.put("creDt", "");
		map.put("receverOrigin", "");
		map.put("dsfsInfo", "");
		map.put("rwzxZt", "");

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		// 任务编号，任务名称，发件人，发送模式;
		String stremlQfId = "", stremlQfDesc = "", strsender = "", strsendModel = "";
		// 模版id，邮件主题，邮件内容，抄送人，同时发送标志，同时发送Email，收件人;
		String stremlTmpId = "", stremlSubj = "", stremlCont = "", strmailCC = "", strtsfsFlag = "", strtsfsEmail = "",
				strrecever = "";
		// 启用edm，取消订阅，启用频发，每小时发送量，定时发送标志，发送日期，发送时间，强制发送;
		String stredmFlag = "", strqxdyFlag = "", strqypfFlag = "", strfsslXs = "", strdsfsFlag = "", strdsfsDate = "",
				strdsfsTime = "", strqzfsFlag = "";
		// 创建人，创建时间,所属部门desc;
		String strCreName = "", strCreDt = "";
		// 任务执行状态;
		String strRwzxZt = "";

		// 收件人tmp;
		String strReceverTmp = "";

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		if (jacksonUtil.containsKey("emlQfId")) {
			stremlQfId = jacksonUtil.getString("emlQfId");
			if (stremlQfId == null) {
				stremlQfId = "";
			}
		}

		String sql = "SELECT TZ_MLSM_QFPC_DESC,TZ_EMAIL_SENDER,TZ_SEND_MODEL,TZ_TMPL_ID,TZ_MAL_SUBJUECT,TZ_MAL_CONTENT,TZ_MAIL_CC,TZ_TSFS_FLAG,TZ_TSFS_ADDR, TZ_EDM_FLAG,TZ_QXDY_FLAG,TZ_QYPFCL_FLAG,TZ_XSFSSL,TZ_DSFS_FLAG, date_format(TZ_DSFS_DATE,'%Y-%m-%d') TZ_DSFS_DATE,  date_format(TZ_DSFS_TIME,'%H:%i') TZ_DSFS_TIME, TZ_QZTS_FLAG,OPRID, date_format(ROW_ADDED_DTTM,'%Y-%m-%d %H:%i:%s') ROW_ADDED_DTTM FROM PS_TZ_DXYJQF_DY_T WHERE TZ_MLSM_QFPC_ID=?";
		Map<String, Object> dyMap = jdbcTemplate.queryForMap(sql, new Object[] { stremlQfId });
		if (dyMap != null) {
			stremlQfDesc = (String) dyMap.get("TZ_MLSM_QFPC_DESC") == null ? ""
					: (String) dyMap.get("TZ_MLSM_QFPC_DESC");
			strsender = (String) dyMap.get("TZ_EMAIL_SENDER") == null ? "" : (String) dyMap.get("TZ_EMAIL_SENDER");
			strsendModel = (String) dyMap.get("TZ_SEND_MODEL") == null ? "" : (String) dyMap.get("TZ_SEND_MODEL");
			stremlTmpId = (String) dyMap.get("TZ_TMPL_ID") == null ? "" : (String) dyMap.get("TZ_TMPL_ID");
			stremlSubj = (String) dyMap.get("TZ_MAL_SUBJUECT") == null ? "" : (String) dyMap.get("TZ_MAL_SUBJUECT");
			stremlCont = (String) dyMap.get("TZ_MAL_CONTENT") == null ? "" : (String) dyMap.get("TZ_MAL_CONTENT");
			strmailCC = (String) dyMap.get("TZ_MAIL_CC") == null ? "" : (String) dyMap.get("TZ_MAIL_CC");
			strtsfsFlag = (String) dyMap.get("TZ_TSFS_FLAG") == null ? "" : (String) dyMap.get("TZ_TSFS_FLAG");
			strtsfsEmail = (String) dyMap.get("TZ_TSFS_ADDR") == null ? "" : (String) dyMap.get("TZ_TSFS_ADDR");
			stredmFlag = (String) dyMap.get("TZ_EDM_FLAG") == null ? "" : (String) dyMap.get("TZ_EDM_FLAG");
			strqxdyFlag = (String) dyMap.get("TZ_QXDY_FLAG") == null ? "" : (String) dyMap.get("TZ_QXDY_FLAG");
			strqypfFlag = (String) dyMap.get("TZ_QYPFCL_FLAG") == null ? "" : (String) dyMap.get("TZ_QYPFCL_FLAG");
			strfsslXs = String.valueOf(dyMap.get("TZ_XSFSSL"));
			strdsfsFlag = (String) dyMap.get("TZ_DSFS_FLAG") == null ? "" : (String) dyMap.get("TZ_DSFS_FLAG");
			strdsfsDate = (String) dyMap.get("TZ_DSFS_DATE") == null ? "" : (String) dyMap.get("TZ_DSFS_DATE");
			strdsfsTime = (String) dyMap.get("TZ_DSFS_TIME") == null ? "" : (String) dyMap.get("TZ_DSFS_TIME");
			strqzfsFlag = (String) dyMap.get("TZ_QZTS_FLAG") == null ? "" : (String) dyMap.get("TZ_QZTS_FLAG");
			// strCreOprid = (String)dyMap.get("OPRID") == null ? "" :
			// (String)dyMap.get("OPRID");
			strCreDt = (String) dyMap.get("ROW_ADDED_DTTM") == null ? "" : (String) dyMap.get("ROW_ADDED_DTTM");
		}

		strCreName = jdbcTemplate.queryForObject("SELECT TZ_REALNAME FROM  PS_TZ_AQ_YHXX_TBL WHERE OPRID = ?",
				new Object[] { oprid }, "String");

		// 收件人;
		if ("NOR".equals(strsendModel)) {
			// 邮件群发收件人;
			String strSql = "SELECT TZ_EMAIL FROM PS_TZ_DXYJQFSJR_T WHERE TZ_MLSM_QFPC_ID=?";
			List<Map<String, Object>> sjrList = jdbcTemplate.queryForList(strSql, new Object[] { stremlQfId });
			if (sjrList != null && sjrList.size() > 0) {
				for (int i = 0; i < sjrList.size(); i++) {
					strReceverTmp = (String) sjrList.get(i).get("TZ_EMAIL");
					if (strReceverTmp != null && !"".equals(strReceverTmp)) {
						arrrecever.add(strReceverTmp);
					}
				}
			}
		} else {
			if ("EXC".equals(strsendModel)) {
				String strExcEmlField = jdbcTemplate.queryForObject(
						"select TZ_FIELD_NAME from PS_TZ_EXC_SET_TBL where TZ_MLSM_QFPC_ID=? and TZ_XXX_TYPE='B' limit 0,1",
						new Object[] { stremlQfId }, "String");
				if (strExcEmlField != null && !"".equals(strExcEmlField)) {
					String strSql = "SELECT " + strExcEmlField
							+ " FROM PS_TZ_MLSM_DRNR_T WHERE TZ_MLSM_QFPC_ID=? limit 0,20";
					List<Map<String, Object>> excList = jdbcTemplate.queryForList(strSql, new Object[] { stremlQfId });
					if (excList != null && excList.size() > 0) {
						for (int i = 0; i < excList.size(); i++) {
							strReceverTmp = (String) excList.get(i).get(strExcEmlField);
							if (strReceverTmp != null && !"".equals(strReceverTmp)) {
								arrrecever.add(strReceverTmp);
							}
						}
					}
				}
			}
		}

		if ("Y".equals(strtsfsFlag)) {
			strtsfsFlag = "on";
		}

		if (strmailCC != null && !"".equals(strmailCC)) {
			String[] cc = strmailCC.split(";");
			for (int i = 0; i < cc.length; i++) {
				mailCCList.add(cc[i]);
			}
		}

		strRwzxZt = jdbcTemplate.queryForObject(
				"SELECT TZ_RWZX_ZT FROM PS_TZ_DXYJFSRW_TBL WHERE TZ_MLSM_QFPC_ID=? ORDER BY  CAST(TZ_EML_SMS_TASK_ID as SIGNED) DESC limit 0,1",
				new Object[] { stremlQfId }, "String");
		if (strRwzxZt == null) {
			strRwzxZt = "";
		}
		String strdsfsInfo = "";
		if ("Y".equals(strdsfsFlag) && ("A".equals(strRwzxZt) || "".equals(strRwzxZt))) {
			strdsfsInfo = "<span style='color:red;'>邮件将会于" + strdsfsDate + "  " + strdsfsTime + "发送.</span>";
		}

		map.replace("emlQfId", stremlQfId);
		map.replace("emlQfDesc", stremlQfDesc);
		map.replace("crePer", strCreName);
		map.replace("sender", strsender);
		map.replace("sendModel", strsendModel);
		map.replace("recever", arrrecever);
		map.replace("tsfsFlag", strtsfsFlag);
		map.replace("tsfsEmail", strtsfsEmail);
		map.replace("mailCC", mailCCList);
		map.replace("emlTmpId", stremlTmpId);
		map.replace("emlSubj", stremlSubj);
		map.replace("emlCont", stremlCont);
		map.replace("edmFlag", stredmFlag);
		map.replace("qxdyFlag", strqxdyFlag);
		map.replace("qypfFlag", strqypfFlag);
		map.replace("fsslXs", strfsslXs);
		map.replace("dsfsFlag", strdsfsFlag);
		map.replace("dsfsDate", strdsfsDate);
		map.replace("dsfsTime", strdsfsTime);
		map.replace("qzfsFlag", strqzfsFlag);
		map.replace("creDt", strCreDt);
		if (arrrecever != null && arrrecever.size() > 0) {
			for (int i = 0; i < arrrecever.size(); i++) {
				if ("".equals(strrecever)) {
					strrecever = arrrecever.get(i);
				} else {
					strrecever = strrecever + "," + arrrecever.get(i);
				}
			}
		}
		map.replace("receverOrigin", strrecever);
		map.replace("dsfsInfo", strdsfsInfo);
		map.replace("rwzxZt", strRwzxZt);

		return jacksonUtil.Map2json(map);
	}

	/* 查询发件人 */
	private String querySenderList(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);

		JacksonUtil jacksonUtil = new JacksonUtil();

		// 当前登录人的机构;
		String strOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"SELECT TZ_EMLSERV_ID,TZ_EML_ADDR100,TZ_CHS_SNAME FROM PS_TZ_EMLS_DEF_TBL WHERE TZ_JG_ID=?",
				new Object[] { strOrgId });
		if (list != null && list.size() > 0) {
			int i = 0;
			String emlServId, strEmail, strDesc;
			for (i = 0; i < list.size(); i++) {
				emlServId = (String) list.get(i).get("TZ_EMLSERV_ID");
				strEmail = (String) list.get(i).get("TZ_EML_ADDR100") == null ? ""
						: (String) list.get(i).get("TZ_EML_ADDR100");
				strDesc = (String) list.get(i).get("TZ_CHS_SNAME") == null ? ""
						: (String) list.get(i).get("TZ_CHS_SNAME");

				strDesc = strDesc + "    " + strEmail;
				Map<String, Object> map = new HashMap<>();
				map.put("email", emlServId);
				map.put("desc", strDesc);
				listData.add(map);
			}
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);

	}

	/* 查询收件人 */
	private String queryReceverList(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);

		String strEmlQfId = ""; /* 群发任务id */
		if (jacksonUtil.containsKey("emlQfId")) {
			strEmlQfId = jacksonUtil.getString("emlQfId");
		} else {
			return jacksonUtil.Map2json(mapRet);
		}

		// 邮件群发收件人;
		String strSql = "SELECT TZ_AUDCY_ID,TZ_EMAIL FROM PS_TZ_DXYJQFSJR_T WHERE TZ_MLSM_QFPC_ID=?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(strSql, new Object[] { strEmlQfId });
		if (list != null && list.size() > 0) {
			int i = 0;
			String strID, strDesc;
			for (i = 0; i < list.size(); i++) {
				strID = (String) list.get(i).get("TZ_AUDCY_ID");
				strDesc = (String) list.get(i).get("TZ_EMAIL");
				if (strID != null && !"".equals(strID) && strDesc != null && !"".equals(strDesc)) {
					Map<String, Object> map = new HashMap<>();
					map.put("id", strID);
					map.put("desc", strDesc);
					listData.add(map);
				}
			}
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);

	}

	/* 查询抄送 */
	private String queryCCList(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);

		String strEmlQfId = ""; /* 群发任务id */
		if (jacksonUtil.containsKey("emlQfId")) {
			strEmlQfId = jacksonUtil.getString("emlQfId");
		} else {
			return jacksonUtil.Map2json(mapRet);
		}

		// 查询抄送;
		String strSql = "SELECT TZ_MAIL_CC FROM PS_TZ_DXYJQF_DY_T WHERE TZ_MLSM_QFPC_ID=?";
		String strLongEmail = jdbcTemplate.queryForObject(strSql, new Object[] { strEmlQfId }, "String");
		if (strLongEmail == null || "".equals(strLongEmail)) {
			return jacksonUtil.Map2json(mapRet);
		}

		String[] arrEmails = strLongEmail.split(";");

		if (arrEmails != null && arrEmails.length > 0) {
			int i = 0;
			for (i = 0; i < arrEmails.length; i++) {
				Map<String, Object> map = new HashMap<>();
				map.put("email", arrEmails[i]);
				map.put("desc", arrEmails[i]);
				listData.add(map);
			}
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);

	}

	/* 查询邮件模版 */
	private String queryEmlTmplList(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);

		JacksonUtil jacksonUtil = new JacksonUtil();

		// 当前登录人的机构;
		String strOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"SELECT TZ_TMPL_ID,TZ_TMPL_NAME FROM PS_TZ_EMALTMPL_TBL WHERE TZ_JG_ID=? AND TZ_USE_FLAG='Y'",
				new Object[] { strOrgId });
		if (list != null && list.size() > 0) {
			int i = 0;
			String strEmailTmpl, strDesc;
			for (i = 0; i < list.size(); i++) {
				strEmailTmpl = (String) list.get(i).get("TZ_TMPL_ID");
				strDesc = (String) list.get(i).get("TZ_TMPL_NAME") == null ? ""
						: (String) list.get(i).get("TZ_TMPL_NAME");

				Map<String, Object> map = new HashMap<>();
				map.put("emailtmpl", strEmailTmpl);
				map.put("desc", strDesc);
				listData.add(map);
			}
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);

	}

	/* 查询附件 */
	private String queryAttaList(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);

		String strEmlQfId = ""; /* 群发任务id */
		if (jacksonUtil.containsKey("emlQfId")) {
			strEmlQfId = jacksonUtil.getString("emlQfId");
		} else {
			return jacksonUtil.Map2json(mapRet);
		}

		// 查询附件;
		String strSql = "SELECT TZ_FJIAN_ID,TZ_FJIAN_MC,TZ_FILE_PATH FROM PS_TZ_YJQFFJXX_TBL WHERE TZ_MLSM_QFPC_ID=? ORDER BY CAST(TZ_FJIAN_ID as SIGNED) DESC";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(strSql, new Object[] { strEmlQfId });
		if (list != null && list.size() > 0) {
			int i = 0;
			String strAttaID, strAttaName, strAttaURl;
			for (i = 0; i < list.size(); i++) {
				strAttaID = (String) list.get(i).get("TZ_FJIAN_ID");
				strAttaName = (String) list.get(i).get("TZ_FJIAN_MC") == null ? ""
						: (String) list.get(i).get("TZ_FJIAN_MC");
				strAttaURl = (String) list.get(i).get("TZ_FILE_PATH") == null ? ""
						: (String) list.get(i).get("TZ_FILE_PATH");
				if(!"".equals(strAttaURl)){
					strAttaURl = request.getContextPath() + strAttaURl;
				}

				Map<String, Object> map = new HashMap<>();
				map.put("emlQfId", strEmlQfId);
				map.put("attaID", strAttaID);
				map.put("attaName", strAttaName);
				map.put("attaUrl", strAttaURl);
				listData.add(map);
			}
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);

	}

	// 查询创建人;
	private String getCreInfo(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> map = new HashMap<>();
		String emlQfId = String.valueOf(getSeqNum.getSeqNum("TZ_DXYJQF_DY_T", "TZ_MLSM_QFPC_ID"));

		map.put("emlQfId", emlQfId);
		map.put("crePer", "");
		map.put("tsfsEmail", "");

		JacksonUtil jacksonUtil = new JacksonUtil();
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		Map<String, Object> ryMap = jdbcTemplate.queryForMap(
				"select a.TZ_REALNAME,b.TZ_ZY_EMAIL from (select * from  PS_TZ_AQ_YHXX_TBL  where OPRID=?) a left join PS_TZ_LXFSINFO_TBL b ON  a.TZ_RYLX  = b.TZ_LXFS_LY AND a.OPRID=b.TZ_LYDX_ID limit 0,1",
				new Object[] { oprid });
		if (ryMap != null) {
			String crePer = ryMap.get("TZ_REALNAME") == null ? "" : (String) ryMap.get("TZ_REALNAME");
			String tsfsEmail = ryMap.get("TZ_ZY_EMAIL") == null ? "" : (String) ryMap.get("TZ_ZY_EMAIL");
			map.replace("crePer", crePer);
			map.replace("tsfsEmail", tsfsEmail);
		}

		return jacksonUtil.Map2json(map);
	}

	// 查询邮件模版信息
	private String getEmlTmplInfo(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> map = new HashMap<>();
		map.put("emlSubj", "");
		map.put("emlCont", "");
		map.put("sender", "");

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		if (jacksonUtil.containsKey("emlTmpId")) {
			String tmpId = jacksonUtil.getString("emlTmpId");
			String strOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			Map<String, Object> emailMap = jdbcTemplate.queryForMap(
					"SELECT B.TZ_EML_ADDR100, A.TZ_MAL_SUBJUECT,A.TZ_MAL_CONTENT FROM (SELECT * FROM PS_TZ_EMALTMPL_TBL  WHERE TZ_JG_ID=? AND TZ_TMPL_ID=?) A LEFT JOIN PS_TZ_EMLS_DEF_TBL B ON A.TZ_EMLSERV_ID=B.TZ_EMLSERV_ID",
					new Object[] { strOrgId, tmpId });
			if (emailMap != null) {
				String sender = emailMap.get("TZ_EML_ADDR100") == null ? "" : (String) emailMap.get("TZ_EML_ADDR100");
				String emlSubj = emailMap.get("TZ_MAL_SUBJUECT") == null ? ""
						: (String) emailMap.get("TZ_MAL_SUBJUECT");
				String emlCont = emailMap.get("TZ_MAL_CONTENT") == null ? "" : (String) emailMap.get("TZ_MAL_CONTENT");
				map.replace("emlSubj", emlSubj);
				map.replace("emlCont", emlCont);
				map.replace("sender", sender);
			}

		}
		return jacksonUtil.Map2json(map);
	}

	// 功能说明：查询邮件模版信息项
	private String getEmlTmplItem(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> map = new HashMap<>();
		int numItem = 0;
		map.put("total", numItem);
		ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
		map.put("root", arrayList);

		String strOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		if (jacksonUtil.containsKey("emlTmpId")) {
			String tmpId = jacksonUtil.getString("emlTmpId");
			if (tmpId != null && !"".equals(tmpId)) {
				Map<String, Object> map1 = jdbcTemplate.queryForMap(
						"SELECT A.TZ_YMB_ID,B.TZ_YMB_CSLBM FROM PS_TZ_EMALTMPL_TBL A,PS_TZ_TMP_DEFN_TBL B WHERE A.TZ_JG_ID=? AND A.TZ_TMPL_ID=? AND A.TZ_YMB_ID=B.TZ_YMB_ID",
						new Object[] { strOrgId, tmpId });
				if (map1 != null) {
					String strRestEmlId = map1.get("TZ_YMB_ID") == null ? "" : (String) map1.get("TZ_YMB_ID");
					String str_ymb_clsbm = map1.get("TZ_YMB_CSLBM") == null ? "" : (String) map1.get("TZ_YMB_CSLBM");
					List<Map<String, Object>> list1 = jdbcTemplate.queryForList(
							"SELECT TZ_PARA_ID,TZ_PARA_ALIAS FROM PS_TZ_TMP_PARA_TBL WHERE TZ_YMB_ID=?",
							new Object[] { strRestEmlId });
					if (list1 != null && list1.size() > 0) {
						for (int i = 0; i < list1.size(); i++) {
							String str_ParaId = list1.get(i).get("TZ_PARA_ID") == null ? ""
									: (String) list1.get(i).get("TZ_PARA_ID");
							String str_paraAlias = list1.get(i).get("TZ_PARA_ALIAS") == null ? ""
									: (String) list1.get(i).get("TZ_PARA_ALIAS");

							numItem++;
							String str_ParaItem = "[" + str_ymb_clsbm + "." + str_ParaId + "." + str_paraAlias + "]";

							Map<String, Object> returnMap1 = new HashMap<>();
							returnMap1.put("parainfoitem", str_ParaItem);
							arrayList.add(returnMap1);
						}
					}
				}
			}
		}

		if (jacksonUtil.containsKey("emlQfId")) {
			String emlQfId = jacksonUtil.getString("emlQfId");
			if (emlQfId != null && !"".equals(emlQfId)) {
				List<Map<String, Object>> list2 = jdbcTemplate.queryForList(
						"SELECT TZ_XXX_NAME FROM PS_TZ_EXC_SET_TBL WHERE TZ_MLSM_QFPC_ID=?", new Object[] { emlQfId });
				if (list2 != null && list2.size() > 0) {
					for (int i = 0; i < list2.size(); i++) {
						String str_paraAlias = (String) list2.get(i).get("TZ_XXX_NAME");
						if (str_paraAlias != null && !"".equals(str_paraAlias)) {

							numItem++;
							String str_ParaItem = "[" + str_paraAlias + "]";

							Map<String, Object> returnMap2 = new HashMap<>();
							returnMap2.put("parainfoitem", str_ParaItem);
							arrayList.add(returnMap2);
						}
					}
				}
			}
		}
		map.replace("total", numItem);
		map.replace("root", arrayList);
		return jacksonUtil.Map2json(map);
	}

	// 功能说明：查询邮件元模版信息
	private String getEmlMetaTmpInfo(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> map = new HashMap<>();
		map.put("emltemporg", "");
		map.put("emltempid", "");
		map.put("metaempid", "");

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		if (jacksonUtil.containsKey("emlTmpId")) {
			String tmpId = jacksonUtil.getString("emlTmpId");
			if (tmpId != null && !"".equals(tmpId)) {
				String strOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				String strMetaEmlId = jdbcTemplate.queryForObject(
						"SELECT TZ_YMB_ID FROM PS_TZ_EMALTMPL_TBL WHERE TZ_JG_ID=? AND TZ_TMPL_ID=?",
						new Object[] { strOrgId, tmpId }, "String");
				if (strMetaEmlId == null) {
					strMetaEmlId = "";
				}

				map.replace("emltemporg", strOrgId);
				map.replace("emltempid", tmpId);
				map.replace("metaempid", strMetaEmlId);
			}
		}

		return jacksonUtil.Map2json(map);
	}

	// 功能说明：保存
	private String saveEmlBulkInfo(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> map = new HashMap<>();
		map.put("emlQfId", "");
		map.put("creDt", "");
		map.put("receverOrigin", "");

		// 任务编号，任务名称，发件人，发送模式;
		String stremlQfId = "", stremlQfDesc = "", strsender = "", strsendModel = "";
		// 模版id，邮件主题，邮件内容，抄送人，同时发送标志，同时发送Email，收件人;
		String stremlTmpId = "", stremlSubj = "", stremlCont = "", strmailCC = "", strtsfsFlag = "", strtsfsEmail = "",
				strrecever = "";
		// 启用edm，取消订阅，启用频发，每小时发送量，定时发送标志，发送日期，发送时间，强制发送;
		String stredmFlag = "", strqxdyFlag = "", strqypfFlag = "", strfsslXs = "", strdsfsFlag = "", strdsfsDate = "",
				strdsfsTime = "", strqzfsFlag = "";
		// 收件人s;
		String[] arrrecever;
		// 附件id，附件名称，存放地址，访问路径;
		String strattaID = "", strattaName = "",  strattaAccUrl = "", path = "";
		// 收件人Origin;
		//String strreceverOrigin = "";
		// 收件人Origin s;
		//String[] arrreceverOrigin;

		String strOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String strOprId = tzLoginServiceImpl.getLoginedManagerOprid(request);

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		Map<String, Object> formDataJson = new HashMap<>(), othInfoJson = new HashMap<>();
		if (jacksonUtil.containsKey("formdata")) {
			formDataJson = jacksonUtil.getMap("formdata");
		}
		if (jacksonUtil.containsKey("othInfo")) {
			othInfoJson = jacksonUtil.getMap("othInfo");
		}
		// 删除的附件;
		List<?> attaDelLit = new ArrayList<>();
		if (jacksonUtil.containsKey("attaDelLit")) {
			attaDelLit = jacksonUtil.getList("attaDelLit");
		}
		// 添加的附件;
		List<?> attaNewList = new ArrayList<>();
		if (jacksonUtil.containsKey("attaNewList")) {
			attaNewList = jacksonUtil.getList("attaNewList");
		}

		if (formDataJson != null) {
			stremlQfId = (String) formDataJson.get("emlQfId");
			stremlQfDesc = (String) formDataJson.get("emlQfDesc");
			strsender = (String) formDataJson.get("sender");
			strsendModel = (String) formDataJson.get("sendModel");
			if (formDataJson.containsKey("tsfsFlag")) {
				strtsfsFlag = (String) formDataJson.get("tsfsFlag");
			}
			if (formDataJson.containsKey("tsfsEmail")) {
				strtsfsEmail = (String) formDataJson.get("tsfsEmail");
			}
			if (formDataJson.containsKey("emlTmpId")) {
				stremlTmpId = (String) formDataJson.get("emlTmpId");
			}

			stremlSubj = (String) formDataJson.get("emlSubj");
			stremlCont = (String) formDataJson.get("emlCont");
			stredmFlag = (String) formDataJson.get("edmFlag");
			strqxdyFlag = (String) formDataJson.get("qxdyFlag");
			strqypfFlag = (String) formDataJson.get("qypfFlag");
			strfsslXs = (String) formDataJson.get("fsslXs");
			int fsslXs = 0;
			if (strfsslXs != null && !"".equals(strfsslXs) && StringUtils.isNumeric(strfsslXs)) {
				fsslXs = Integer.parseInt(strfsslXs);
			}
			strdsfsFlag = (String) formDataJson.get("dsfsFlag");
			strdsfsDate = (String) formDataJson.get("dsfsDate");
			if (formDataJson.containsKey("dsfsTime")) {
				strdsfsTime = (String) formDataJson.get("dsfsTime");
			}
			if (formDataJson.containsKey("qzfsFlag")) {
				strqzfsFlag = (String) formDataJson.get("qzfsFlag");
			} else {
				strqzfsFlag = "N";
			}
			//strreceverOrigin = (String) formDataJson.get("receverOrigin");

			if (othInfoJson != null) {
				strrecever = (String) othInfoJson.get("recever");
				strmailCC = (String) othInfoJson.get("mailCC");
			}

			if ("on".equals(strtsfsFlag)) {
				strtsfsFlag = "Y";
			}
			if (strmailCC != null && !"".equals(strmailCC)) {
				strmailCC.replaceAll(",", ";");
			}

			arrrecever = strrecever.split(",");

			//arrreceverOrigin = strreceverOrigin.split(",");

			PsTzDxyjQfDyTWithBLOBs psTzDxyjQfDyT = psTzDxyjQfDyTMapper.selectByPrimaryKey(stremlQfId);
			if (psTzDxyjQfDyT != null) {
				// 邮件群发任务;
				psTzDxyjQfDyT.setTzMlsmQfpcDesc(stremlQfDesc);
				psTzDxyjQfDyT.setTzEmailSender(strsender);
				psTzDxyjQfDyT.setTzSendModel(strsendModel);
				psTzDxyjQfDyT.setTzTmplId(stremlTmpId);
				psTzDxyjQfDyT.setTzMalSubjuect(stremlSubj);
				psTzDxyjQfDyT.setTzMalContent(stremlCont);
				psTzDxyjQfDyT.setTzMailCc(strmailCC);
				psTzDxyjQfDyT.setTzTsfsFlag(strtsfsFlag);
				psTzDxyjQfDyT.setTzTsfsAddr(strtsfsEmail);
				psTzDxyjQfDyT.setTzEdmFlag(stredmFlag);
				psTzDxyjQfDyT.setTzQxdyFlag(strqxdyFlag);
				psTzDxyjQfDyT.setTzQypfclFlag(strqypfFlag);
				psTzDxyjQfDyT.setTzXsfssl(fsslXs);
				psTzDxyjQfDyT.setTzDsfsFlag(strdsfsFlag);

				if (strdsfsDate != null && !"".equals(strdsfsDate)) {
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Date dsfsDate = sdf.parse(strdsfsDate);
						psTzDxyjQfDyT.setTzDsfsDate(dsfsDate);
					} catch (Exception e) {

					}
				}

				if (strdsfsTime != null && !"".equals(strdsfsTime)) {
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
						Date dsfsTime = sdf.parse(strdsfsTime);
						psTzDxyjQfDyT.setTzDsfsTime(dsfsTime);
					} catch (Exception e) {

					}
				}
				psTzDxyjQfDyT.setTzQztsFlag(strqzfsFlag);
				psTzDxyjQfDyT.setTzOffSend("N");
				psTzDxyjQfDyT.setRowLastmantDttm(new Date());
				psTzDxyjQfDyT.setRowLastmantOprid(strOprId);
				psTzDxyjQfDyTMapper.updateByPrimaryKeyWithBLOBs(psTzDxyjQfDyT);
			} else {
				// 邮件群发任务;
				psTzDxyjQfDyT = new PsTzDxyjQfDyTWithBLOBs();
				psTzDxyjQfDyT.setTzMlsmQfpcId(stremlQfId);
				psTzDxyjQfDyT.setTzMlsmQfpcDesc(stremlQfDesc);
				psTzDxyjQfDyT.setTzQfType("MAL");
				psTzDxyjQfDyT.setOprid(strOprId);
				psTzDxyjQfDyT.setTzJgId(strOrgId);
				psTzDxyjQfDyT.setTzEmailSender(strsender);
				psTzDxyjQfDyT.setTzSendModel(strsendModel);
				psTzDxyjQfDyT.setTzTmplId(stremlTmpId);
				psTzDxyjQfDyT.setTzMalSubjuect(stremlSubj);
				psTzDxyjQfDyT.setTzMalContent(stremlCont);
				psTzDxyjQfDyT.setTzMailCc(strmailCC);
				psTzDxyjQfDyT.setTzTsfsFlag(strtsfsFlag);
				psTzDxyjQfDyT.setTzTsfsAddr(strtsfsEmail);
				psTzDxyjQfDyT.setTzEdmFlag(stredmFlag);
				psTzDxyjQfDyT.setTzQxdyFlag(strqxdyFlag);
				psTzDxyjQfDyT.setTzQypfclFlag(strqypfFlag);
				psTzDxyjQfDyT.setTzXsfssl(fsslXs);
				psTzDxyjQfDyT.setTzDsfsFlag(strdsfsFlag);

				if (strdsfsDate != null && !"".equals(strdsfsDate)) {
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Date dsfsDate = sdf.parse(strdsfsDate);
						psTzDxyjQfDyT.setTzDsfsDate(dsfsDate);
					} catch (Exception e) {

					}
				}

				if (strdsfsTime != null && !"".equals(strdsfsTime)) {
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("hh24:mm");
						Date dsfsTime = sdf.parse(strdsfsTime);
						psTzDxyjQfDyT.setTzDsfsTime(dsfsTime);
					} catch (Exception e) {

					}
				}
				psTzDxyjQfDyT.setTzQztsFlag(strqzfsFlag);
				psTzDxyjQfDyT.setTzOffSend("N");
				psTzDxyjQfDyT.setTzAutoCreate("N");
				psTzDxyjQfDyT.setRowAddedDttm(new Date());
				psTzDxyjQfDyT.setRowAddedOprid(strOprId);
				psTzDxyjQfDyT.setRowLastmantDttm(new Date());
				psTzDxyjQfDyT.setRowLastmantOprid(strOprId);
				psTzDxyjQfDyTMapper.insert(psTzDxyjQfDyT);
			}

			// 收件人;
			if ("NOR".equals(strsendModel)) {
				// 删除excel导入的数据;
				jdbcTemplate.update("DELETE from PS_TZ_EXC_SET_TBL WHERE TZ_MLSM_QFPC_ID=?",
						new Object[] { stremlQfId });

				jdbcTemplate.update("DELETE from PS_TZ_MLSM_DRNR_T WHERE TZ_MLSM_QFPC_ID=?",
						new Object[] { stremlQfId });

				// 删除收件人数据;
				jdbcTemplate.update("DELETE from PS_TZ_DXYJQFSJR_T WHERE TZ_MLSM_QFPC_ID=?",
						new Object[] { stremlQfId });
				// 添加收件人;
				for (int i = 0; i < arrrecever.length; i++) {
					PsTzDxYjQfSjrT psTzDxYjQfSjrT = new PsTzDxYjQfSjrT();
					psTzDxYjQfSjrT.setTzMlsmQfpcId(stremlQfId);
					psTzDxYjQfSjrT.setTzAudcyId(String.valueOf(getSeqNum.getSeqNum("TZ_AUDCYUAN_T", "TZ_AUDCY_ID")));
					psTzDxYjQfSjrT.setTzEmail(arrrecever[i]);
					psTzDxYjQfSjrTMapper.insert(psTzDxYjQfSjrT);
				}

			} else {
				if ("EXC".equals(strsendModel)) {
					jdbcTemplate.update("DELETE FROM PS_TZ_DXYJQFSJR_T WHERE TZ_MLSM_QFPC_ID=?",
							new Object[] { stremlQfId });
				}
			}

			// 定时发送;
			if ("Y".equals(strdsfsFlag)) {
				PsTzDxYjDsfsT psTzDxYjDsfsT = psTzDxYjDsfsTMapper.selectByPrimaryKey(stremlQfId);
				if (psTzDxYjDsfsT != null) {
					psTzDxYjDsfsT.setTzSendZt("N");
					psTzDxYjDsfsTMapper.updateByPrimaryKey(psTzDxYjDsfsT);
				} else {
					psTzDxYjDsfsT = new PsTzDxYjDsfsT();
					psTzDxYjDsfsT.setTzMlsmQfpcId(stremlQfId);
					psTzDxYjDsfsT.setTzSendZt("N");
					psTzDxYjDsfsTMapper.insert(psTzDxYjDsfsT);
				}
			} else {
				PsTzDxYjDsfsT psTzDxYjDsfsT = psTzDxYjDsfsTMapper.selectByPrimaryKey(stremlQfId);
				if (psTzDxYjDsfsT != null) {
					psTzDxYjDsfsTMapper.deleteByPrimaryKey(stremlQfId);
				}
			}

			// 处理删除的附件;
			if (attaDelLit != null && attaDelLit.size()>0) {
				for (int i = 0; i < attaDelLit.size(); i++) {
					@SuppressWarnings("unchecked")
					Map<String, Object> delMap = (Map<String, Object>)attaDelLit.get(i);
					strattaID = (String)delMap.get("attaID");
					if(strattaID != null && !"".equals(strattaID)){
						PsTzYjQfFjXxTblKey key = new PsTzYjQfFjXxTblKey();
						key.setTzMlsmQfpcId(stremlQfId);
						key.setTzFjianId(strattaID);
						psTzYjQfFjXxTblMapper.deleteByPrimaryKey(key);
					}
				}
			}

			// 处理添加的附件;
			if (attaNewList != null && attaNewList.size()>0) {
				for (int i = 0; i < attaNewList.size(); i++) {
					@SuppressWarnings("unchecked")
					Map<String, Object> delMap = (Map<String, Object>)attaNewList.get(i);
					strattaName = (String)delMap.get("attaName");
					strattaAccUrl = (String)delMap.get("path");
					path = request.getServletContext().getRealPath(strattaAccUrl);
					PsTzYjQfFjXxTbl psTzYjQfFjXxTbl = new PsTzYjQfFjXxTbl();
					strattaID = String.valueOf(getSeqNum.getSeqNum("TZ_YJQFFJXX_TBL", "TZ_FJIAN_ID"));
					psTzYjQfFjXxTbl.setTzMlsmQfpcId(stremlQfId);
					psTzYjQfFjXxTbl.setTzFjianId(strattaID);
					psTzYjQfFjXxTbl.setTzFjianMc(strattaName);
					psTzYjQfFjXxTbl.setTzFjianLj(path);
					psTzYjQfFjXxTbl.setTzFilePath(strattaAccUrl);
					psTzYjQfFjXxTblMapper.insert(psTzYjQfFjXxTbl);
				}
			}
		}

		map.replace("emlQfId", stremlQfId);
		SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		map.replace("creDt", datetimeFormate.format(new Date()));
		map.replace("receverOrigin", strrecever);

		return jacksonUtil.Map2json(map);
	}
	
	
	private String sendEml(String comParams, String[] errorMsg){
		String strComContent = this.saveEmlBulkInfo(comParams, errorMsg);
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		Map<String, Object> formDataJson = jacksonUtil.getMap("formdata");
		
		// 是否定时发送;
	    String strdsfsFlag = (String)formDataJson.get("dsfsFlag");
	    // 同时发送;
	    String strTsfsFlag = "", strtsfsEmail = "";
	    if(formDataJson.containsKey("tsfsFlag")){
	    	strTsfsFlag = (String)formDataJson.get("tsfsFlag");
	    }
	    //同时发送邮箱
	    if(formDataJson.containsKey("tsfsEmail")){
	    	strtsfsEmail = (String)formDataJson.get("tsfsEmail");
	    }
	    //TaskId;
	    String strTaskId = "";
	    
	    //不是定时发送的;
	    if(!"Y".equals(strdsfsFlag)){
	    	//邮件群发编号;
	    	String stremlQfId = (String)formDataJson.get("emlQfId");
	        // 发送模式;
	    	String strsendModel = (String)formDataJson.get("sendModel");
	    	
	    	String strOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
	         
	    	//创建任务;
	    	strTaskId = createQfTaskServiceImpl.createTaskIns(strOrgId, stremlQfId, "MAL", "A");
	    	// 创建听众;
	        String strAudienceDesc = "短信邮件群发_MAL_" + stremlQfId;
	        String strAudID = createQfTaskServiceImpl.createAudience(strTaskId, strOrgId, strAudienceDesc, "YJQF");
	        
	        // 添加听众成员;
	        String strSql = "";
	        String strAudIDTemp = "",strEmlTmp = "";
	        if("NOR".equals(strsendModel)){
	        	// 收件人;
	            strSql = "SELECT TZ_AUDCY_ID,TZ_EMAIL FROM PS_TZ_DXYJQFSJR_T WHERE TZ_MLSM_QFPC_ID= ?";
	            List<Map<String, Object>> audList = jdbcTemplate.queryForList(strSql,new Object[]{stremlQfId}); 
	            if(audList != null && audList.size() > 0){
	            	for(int i = 0; i<audList.size(); i++){
	            		strAudIDTemp = (String)audList.get(i).get("TZ_AUDCY_ID");
	            		strEmlTmp = (String)audList.get(i).get("TZ_EMAIL");
	            		createQfTaskServiceImpl.addAudCy(strAudID,strAudIDTemp, "", "", "", "", strEmlTmp, "", "", "", "", "", "");
	            	}
	            	
	            }else{
	            	if("EXC".equals(strsendModel)){
	            		String strXXXField = "", strXXXFieldTemp = "";
	            		String strNameTemp = "";
	                    int numFieldCount = 0;
	                    String typeAFieldName = "";
	                    String typeBFieldName = "";
	                    strSql = "SELECT TZ_FIELD_NAME FROM PS_TZ_EXC_SET_TBL WHERE TZ_MLSM_QFPC_ID=? AND TZ_XXX_TYPE IN('A','B') order by TZ_XXX_TYPE";
	                    List<Map<String, Object>> excList = jdbcTemplate.queryForList(strSql,new Object[]{stremlQfId});
	                    if(excList != null && excList.size()>0){
	                    	for(int i = 0; i < excList.size(); i++){
	                    		strXXXFieldTemp = (String)excList.get(i).get("TZ_FIELD_NAME");
	                    		if("".equals(strXXXField)){
	                    			strXXXField = strXXXFieldTemp;;
	                    		}else{
	                    			strXXXField = strXXXField + "," + strXXXFieldTemp;
	                    		}
	                    		if(i == 0){
	                    			typeAFieldName = strXXXFieldTemp;
	                    		}
	                    		if(i == 1){
	                    			typeBFieldName = strXXXFieldTemp;
	                    		}
	                    		
	                            numFieldCount = numFieldCount + 1;
	                    	}
	                    }
	                    
	                    if("".equals(strXXXField)){
	                    	strXXXField = "TZ_AUDCY_ID" ;
	                    }else{
	                    	strXXXField = "TZ_AUDCY_ID," + strXXXField;
	                    }
	                    
	                    
	                    strSql = "SELECT " + strXXXField + " FROM PS_TZ_MLSM_DRNR_T WHERE TZ_MLSM_QFPC_ID=?";
	                    List<Map<String, Object>> list = jdbcTemplate.queryForList(strSql,new Object[]{stremlQfId});
	                    if(list != null && list.size() > 0){
	                    	for(int i = 0; i < list.size(); i++){
	                    		if(numFieldCount == 1){
	                    			strAudIDTemp = (String)list.get(i).get("TZ_AUDCY_ID");
	                    			strEmlTmp = (String)list.get(i).get(typeAFieldName);
	                    		}
	                    		
	                    		if(numFieldCount == 2){
	                    			strAudIDTemp = (String)list.get(i).get("TZ_AUDCY_ID");
	                    			strNameTemp = (String)list.get(i).get(typeAFieldName);
	                    			strEmlTmp = (String)list.get(i).get(typeBFieldName);
	                    		}
	                    		
	                    		createQfTaskServiceImpl.addAudCy(strAudID,strAudIDTemp, strNameTemp, "", "", "", strEmlTmp, "", "", "", "", "", "");
	                    	}
	                    }
	            	}
	            }
	        }
	        
	        // 同时发送;
	        if("on".equals(strTsfsFlag)){
	        	createQfTaskServiceImpl.addAudCy(strAudID,"", "", "", "", "", strtsfsEmail, "", "", "", "", "", "");
	        }
	        
	        PsTzDxYjDsfsT psTzDxYjDsfsT = psTzDxYjDsfsTMapper.selectByPrimaryKey(stremlQfId);
	        if(psTzDxYjDsfsT != null){
	        	psTzDxYjDsfsT.setTzEmlSmsTaskId(strTaskId);
	        	psTzDxYjDsfsTMapper.updateByPrimaryKey(psTzDxYjDsfsT);
	        }
	        
	        sendSmsOrMalQfServiceImpl.send(strTaskId, "");
	    }
	    
	    String strRwzxZt = "";
	    strRwzxZt = jdbcTemplate.queryForObject("SELECT TZ_RWZX_ZT FROM PS_TZ_DXYJFSRW_TBL WHERE TZ_EML_SMS_TASK_ID=?", new Object[]{strTaskId},"String");
	    strComContent = strComContent.substring(0, strComContent.length()-1);
	    strComContent = strComContent + ",\"rwzxZt\":\"" + strRwzxZt + "\"}";
		return strComContent;
	}

	// 功能说明：中断发送
	private String revoke(String comParams, String[] errorMsg) {
		// 返回值;
		String strComContent = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		if (jacksonUtil.containsKey("emlQfId")) {
			String stremlQfId = jacksonUtil.getString("emlQfId");
			if (stremlQfId != null && !"".equals(stremlQfId)) {
				// 短信邮件群发任务批次定义表;
				PsTzDxyjQfDyTWithBLOBs psTzDxyjQfDyT = psTzDxyjQfDyTMapper.selectByPrimaryKey(stremlQfId);
				if (psTzDxyjQfDyT != null) {
					psTzDxyjQfDyT.setTzOffSend("Y");
					psTzDxyjQfDyTMapper.updateByPrimaryKeySelective(psTzDxyjQfDyT);
				}

				// 短信邮件定时发送表;
				PsTzDxYjDsfsT psTzDxYjDsfsT = psTzDxYjDsfsTMapper.selectByPrimaryKey(stremlQfId);
				if (psTzDxYjDsfsT != null) {
					psTzDxYjDsfsT.setTzSendZt("Y");
					psTzDxYjDsfsTMapper.updateByPrimaryKeySelective(psTzDxYjDsfsT);
				}
			}

		}

		return strComContent;
	}

	// 功能说明：清除所有
	private String clearAll(String comParams, String[] errorMsg) {
		// 返回值;
		String strComContent = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		if (jacksonUtil.containsKey("emlQfId")) {
			String stremlQfId = jacksonUtil.getString("emlQfId");
			if (stremlQfId != null && !"".equals(stremlQfId)) {
				jdbcTemplate.update("delete from PS_TZ_EXC_SET_TBL where TZ_MLSM_QFPC_ID=?",
						new Object[] { stremlQfId });

				jdbcTemplate.update("delete from PS_TZ_MLSM_DRNR_T where TZ_MLSM_QFPC_ID=?",
						new Object[] { stremlQfId });
			}
		}

		return strComContent;
	}

	// 获取任务执行状态;
	private String getRwzxZt(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> map = new HashMap<>();
		map.put("emlQfId", "");
		map.put("rwzxZt", "");

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		if (jacksonUtil.containsKey("emlQfId")) {
			String stremlQfId = jacksonUtil.getString("emlQfId");

			String sql = "SELECT TZ_RWZX_ZT FROM PS_TZ_DXYJFSRW_TBL WHERE TZ_MLSM_QFPC_ID=? ORDER BY CAST(TZ_EML_SMS_TASK_ID as SIGNED) DESC limit 0,1";
			String strRwzxZt = jdbcTemplate.queryForObject(sql, new Object[] { stremlQfId }, "String");
			if (strRwzxZt == null) {
				strRwzxZt = "";
			}
			map.replace("emlQfId", stremlQfId);
			map.replace("rwzxZt", strRwzxZt);
		}

		return jacksonUtil.Map2json(map);
	}
}
