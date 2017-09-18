package com.tranzvision.gd.TZEmailSmsQFBundle.service.impl;

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

import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsprcsrqstMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.Psprcsrqst;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzDxYjDsfsTMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzDxYjQfSjrTMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzDxyjQfDyTMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzDxyjqaudTMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzExcSetTblMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzMlsmDrnrTMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzDxYjDsfsT;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzDxYjQfSjrT;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzDxyjQfDyTWithBLOBs;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzDxyjqaudTKey;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzExcSetTbl;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzMlsmDrnrT;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzEmlTaskAetMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzEmlTaskAet;
import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.batch.engine.base.EngineParameters;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 
 * @author tang TZ_GD_SMSQF_PKG:TZ_SMSQF_DEF_CLS
 */
@Service("com.tranzvision.gd.TZEmailSmsQFBundle.service.impl.TzSmsQfDefClsServiceImpl")
public class TzSmsQfDefClsServiceImpl extends FrameworkImpl {
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
	private PsTzDxYjQfSjrTMapper psTzDxYjQfSjrTMapper;

	@Autowired
	private PsTzDxYjDsfsTMapper psTzDxYjDsfsTMapper;

	@Autowired
	private CreateQfTaskServiceImpl createQfTaskServiceImpl;

	@Autowired
	private PsprcsrqstMapper psprcsrqstMapper;

	@Autowired
	private PsTzEmlTaskAetMapper psTzEmlTaskAetMapper;

	@Autowired
	private PsTzMlsmDrnrTMapper psTzMlsmDrnrTMapper;

	@Autowired
	private PsTzExcSetTblMapper psTzExcSetTblMapper;

	@Autowired
	private TZGDObject tZGDObject;

	@Autowired
	private SendSmsOrMalQfServiceImpl sendSmsOrMalQfServiceImpl;
	
	@Autowired
	private PsTzDxyjqaudTMapper psTzDxyjqaudTMapper;

	// 获取群发任务信息;
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回;
		Map<String, Object> map = new HashMap<>();

		map.put("smsQfId", "");
		map.put("smsQfDesc", "");
		map.put("crePer", "");
		map.put("sendModel", "");
		// 收件人s;
		ArrayList<String> arrrecever = new ArrayList<>();
		map.put("recever", arrrecever);
		map.put("tsfsFlag", "");
		map.put("tsfsPhone", "");
		map.put("smsTmpId", "");
		map.put("smsQm", "");
		map.put("smsCont", "");
		map.put("dsfsFlag", "");
		map.put("dsfsDate", "");
		map.put("dsfsTime", "");
		map.put("creDt", "");
		map.put("receverOrigin", "");
		map.put("dsfsInfo", "");
		map.put("rwzxZt", "");

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		// 任务编号，任务名称，发件人，发送模式;
		String strsmsQfId = "", strsmsQfDesc = "", strsendModel = "";
		// 模版id，邮件主题，邮件内容，抄送人，同时发送标志，同时发送Email，收件人;
		String strsmsTmpId = "", strsmsQm = "", strsmsCont = "", strtsfsFlag = "", strtsfsPhone = "";
		// 启用edm，取消订阅，启用频发，每小时发送量，定时发送标志，发送日期，发送时间，强制发送;
		String strdsfsFlag = "", strdsfsDate = "", strdsfsTime = "";
		// 创建人，创建时间,所属部门desc;
		String strCreName = "", strCreDt = "", strCreOprid = "";
		// 任务执行状态;
		String strRwzxZt = "";

		// 收件人tmp;
		String strReceverTmp = "";

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		if (jacksonUtil.containsKey("smsQfId")) {
			strsmsQfId = jacksonUtil.getString("smsQfId");
			if (strsmsQfId == null) {
				strsmsQfId = "";
			}
		}

		String sql = " SELECT TZ_MLSM_QFPC_DESC,TZ_SEND_MODEL,TZ_TMPL_ID,TZ_DXQM,TZ_SMS_CONTENT,TZ_TSFS_FLAG,TZ_TSFS_ADDR, TZ_DSFS_FLAG,date_format(TZ_DSFS_DATE,'%Y-%m-%d') TZ_DSFS_DATE ,date_format(TZ_DSFS_TIME,'%H:%i') TZ_DSFS_TIME, OPRID,date_format(ROW_ADDED_DTTM,'%Y-%m-%d %H:%i') ROW_ADDED_DTTM FROM PS_TZ_DXYJQF_DY_T WHERE TZ_MLSM_QFPC_ID=?";
		Map<String, Object> dyMap = jdbcTemplate.queryForMap(sql, new Object[] { strsmsQfId });
		if (dyMap != null) {
			strsmsQfDesc = (String) dyMap.get("TZ_MLSM_QFPC_DESC") == null ? ""
					: (String) dyMap.get("TZ_MLSM_QFPC_DESC");
			strsendModel = (String) dyMap.get("TZ_SEND_MODEL") == null ? "" : (String) dyMap.get("TZ_SEND_MODEL");
			strsmsTmpId = (String) dyMap.get("TZ_TMPL_ID") == null ? "" : (String) dyMap.get("TZ_TMPL_ID");
			strsmsQm = (String) dyMap.get("TZ_DXQM") == null ? "" : (String) dyMap.get("TZ_DXQM");
			strsmsCont = (String) dyMap.get("TZ_SMS_CONTENT") == null ? "" : (String) dyMap.get("TZ_SMS_CONTENT");
			strtsfsFlag = (String) dyMap.get("TZ_TSFS_FLAG") == null ? "" : (String) dyMap.get("TZ_TSFS_FLAG");
			strtsfsPhone = (String) dyMap.get("TZ_TSFS_ADDR") == null ? "" : (String) dyMap.get("TZ_TSFS_ADDR");

			strdsfsFlag = (String) dyMap.get("TZ_DSFS_FLAG") == null ? "" : (String) dyMap.get("TZ_DSFS_FLAG");
			strdsfsDate = (String) dyMap.get("TZ_DSFS_DATE") == null ? "" : (String) dyMap.get("TZ_DSFS_DATE");
			strdsfsTime = (String) dyMap.get("TZ_DSFS_TIME") == null ? "" : (String) dyMap.get("TZ_DSFS_TIME");
			strCreOprid = (String) dyMap.get("OPRID") == null ? "" : (String) dyMap.get("OPRID");
			strCreDt = (String) dyMap.get("ROW_ADDED_DTTM") == null ? "" : (String) dyMap.get("ROW_ADDED_DTTM");
			// (String)dyMap.get("OPRID");
			strCreName = jdbcTemplate.queryForObject("SELECT TZ_REALNAME FROM  PS_TZ_AQ_YHXX_TBL WHERE OPRID = ?",
					new Object[] { strCreOprid }, "String");

		} else {
			strCreName = jdbcTemplate.queryForObject("SELECT TZ_REALNAME FROM  PS_TZ_AQ_YHXX_TBL WHERE OPRID = ?",
					new Object[] { oprid }, "String");
		}

		// 收件人;
		if ("NOR".equals(strsendModel)) {
			 // 邮件群发听众;
			String strSql = "SELECT TZ_AUDIENCE_ID FROM PS_TZ_DXYJQAUD_T WHERE TZ_MLSM_QFPC_ID=?";
			List<Map<String, Object>> tzList = jdbcTemplate.queryForList(strSql, new Object[] { strsmsQfId });
			if (tzList != null && tzList.size() > 0) {
				for (int i = 0; i < tzList.size(); i++) {
					strReceverTmp = (String) tzList.get(i).get("TZ_AUDIENCE_ID");
					if (strReceverTmp != null && !"".equals(strReceverTmp)) {
						arrrecever.add(strReceverTmp);
					}
				}
			}
			
			// 邮件群发收件人;
			strSql = "SELECT TZ_EMAIL FROM PS_TZ_DXYJQFSJR_T WHERE TZ_MLSM_QFPC_ID=?";
			List<Map<String, Object>> sjrList = jdbcTemplate.queryForList(strSql, new Object[] { strsmsQfId });
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
						new Object[] { strsmsQfId }, "String");
				if (strExcEmlField != null && !"".equals(strExcEmlField)) {
					String strSql = "SELECT " + strExcEmlField
							+ " FROM PS_TZ_MLSM_DRNR_T WHERE TZ_MLSM_QFPC_ID=?";
					List<Map<String, Object>> excList = jdbcTemplate.queryForList(strSql, new Object[] { strsmsQfId });
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

		strRwzxZt = jdbcTemplate.queryForObject(
				"SELECT TZ_RWZX_ZT FROM PS_TZ_DXYJFSRW_TBL WHERE TZ_MLSM_QFPC_ID=? ORDER BY  CAST(TZ_EML_SMS_TASK_ID as SIGNED) DESC limit 0,1",
				new Object[] { strsmsQfId }, "String");
		if (strRwzxZt == null) {
			strRwzxZt = "";
		}
		String strdsfsInfo = "";
		if ("Y".equals(strdsfsFlag) && ("A".equals(strRwzxZt) || "".equals(strRwzxZt))) {
			strdsfsInfo = "<span style='color:red;'>短信将会于" + strdsfsDate + "  " + strdsfsTime + "发送.</span>";
		}

		map.replace("smsQfId", strsmsQfId);
		map.replace("smsQfDesc", strsmsQfDesc);
		map.replace("crePer", strCreName);
		map.replace("sendModel", strsendModel);
		// 收件人s;
		map.replace("recever", arrrecever);
		map.replace("tsfsFlag", strtsfsFlag);
		map.replace("tsfsPhone", strtsfsPhone);
		map.replace("smsTmpId", strsmsTmpId);
		map.replace("smsQm", strsmsQm);
		map.replace("smsCont", strsmsCont);
		map.replace("dsfsFlag", strdsfsFlag);
		map.replace("dsfsDate", strdsfsDate);
		map.replace("dsfsTime", strdsfsTime);
		map.replace("creDt", strCreDt);
		map.replace("receverOrigin", "");
		map.replace("dsfsInfo", strdsfsInfo);
		map.replace("rwzxZt", strRwzxZt);

		return jacksonUtil.Map2json(map);
	}

	/* 获取页面信息 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strComContent = "{}";

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		if (jacksonUtil.containsKey("queryID")) {
			String queryID = jacksonUtil.getString("queryID");

			if ("recever".equals(queryID)) {
				strComContent = this.queryReceverList(comParams, errorMsg);
			}

			if ("smstmpl".equals(queryID)) {
				strComContent = this.queryEmlTmplList(comParams, errorMsg);
			}

			/*
			 * if ("transmit".equals(queryID)) { strComContent =
			 * this.queryTransmitList(comParams, errorMsg); }
			 */
			// 复制历史
			if ("myHistoryRw".equals(queryID)) {
				strComContent = this.myHistoryRwList(comParams, errorMsg);
			}
		} else {
			errorMsg[0] = "1";
			errorMsg[1] = "参数不正确！";
		}

		return strComContent;
	}

	/* 我创建的历史任务列表 */
	private String myHistoryRwList(String comParams, String[] errorMsg) {
		
		int start = Integer.parseInt(request.getParameter("start"));
		int limit = Integer.parseInt(request.getParameter("limit"));	
				
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		int total = 0;
		mapRet.put("total", 0);
		mapRet.put("root", listData);

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);

		String taskType = "";
		String searchText = "";
		if (jacksonUtil.containsKey("taskType")) {
			taskType = jacksonUtil.getString("taskType");
		}

		if (jacksonUtil.containsKey("searchText")) {
			searchText = jacksonUtil.getString("searchText");
		}

		if (searchText == null) {
			searchText = "";
		}

		// 当前登录人员;
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		total = jdbcTemplate.queryForObject("SELECT count(1) FROM PS_TZ_DXYJQF_DY_T WHERE OPRID=? AND TZ_QF_TYPE=? AND UPPER(TZ_MLSM_QFPC_DESC) LIKE ?", new Object[] { oprid, taskType, '%' + searchText.toUpperCase() + '%' },"Integer");
		String strSql = "SELECT TZ_MLSM_QFPC_ID,TZ_MLSM_QFPC_DESC,TZ_SEND_MODEL,DATE_FORMAT(ROW_ADDED_DTTM,'%Y-%m-%d %H:%i') ROW_ADDED_DTTM,TZ_MAL_SUBJUECT FROM PS_TZ_DXYJQF_DY_T WHERE OPRID=? AND TZ_QF_TYPE=? AND UPPER(TZ_MLSM_QFPC_DESC) LIKE ? order by ROW_ADDED_DTTM desc limit ?,?";
		List<Map<String, Object>> lsList = jdbcTemplate.queryForList(strSql,
				new Object[] { oprid, taskType, '%' + searchText.toUpperCase() + '%' ,start,limit});
		if (lsList != null && lsList.size() > 0) {
			for (int i = 0; i < lsList.size(); i++) {
				Map<String, Object> map = new HashMap<>();
				map.put("qfRwId", lsList.get(i).get("TZ_MLSM_QFPC_ID"));
				map.put("qfRwName", lsList.get(i).get("TZ_MLSM_QFPC_DESC"));
				map.put("sendModal", lsList.get(i).get("TZ_SEND_MODEL"));
				map.put("createDttm", lsList.get(i).get("ROW_ADDED_DTTM"));
				map.put("emlTheme", lsList.get(i).get("TZ_MAL_SUBJUECT"));
				listData.add(map);
			}
		}
		
		mapRet.replace("total", total);
		mapRet.replace("root", listData);
		return jacksonUtil.Map2json(mapRet);
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

		if ("getSmsTmpInfo".equals(OperateType)) {
			bl = true;
			strResultConten = this.getSmsTmpInfo(comParams, errorMsg);
		}

		if ("getSmsTmpItem".equals(OperateType)) {
			strResultConten = this.getSmsTmplItem(comParams, errorMsg);
			bl = true;
		}

		if ("getSmsMetaTmpInfo".equals(OperateType)) {
			strResultConten = this.getSmsMetaTmpInfo(comParams, errorMsg);
			bl = true;
		}

		if ("save".equals(OperateType)) {
			strResultConten = this.saveSmsBulkInfo(comParams, errorMsg);
			bl = true;
		}

		if ("sendSms".equals(OperateType)) {
			strResultConten = this.sendSms(comParams, errorMsg);
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

		if ("getHistoryRwInfo".equals(OperateType)) {
			bl = true;
			strResultConten = this.getHistoryRwInfo(comParams, errorMsg);
		}

		if (bl == false) {
			errorMsg[0] = "1";
			errorMsg[1] = "未找到[" + OperateType + "]对应处理方法.";
		}

		return strResultConten;
	}

	// 查询创建人;
	private String getCreInfo(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> map = new HashMap<>();
		String strTaskId = String.valueOf(getSeqNum.getSeqNum("TZ_DXYJQF_DY_T", "TZ_MLSM_QFPC_ID"));

		map.put("smsQfId", strTaskId);
		map.put("crePer", "");

		JacksonUtil jacksonUtil = new JacksonUtil();
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String crePer = jdbcTemplate.queryForObject(
				"select TZ_REALNAME from  PS_TZ_AQ_YHXX_TBL  where OPRID=? limit 0,1", new Object[] { oprid },
				"String");

		map.replace("crePer", crePer);

		return jacksonUtil.Map2json(map);
	}

	// 查询邮件模版信息
	private String getSmsTmpInfo(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> map = new HashMap<>();

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		if (jacksonUtil.containsKey("SmsTmpId") && jacksonUtil.containsKey("smsQfId")) {
			String tmpId = jacksonUtil.getString("SmsTmpId");
			String smsQfId = jacksonUtil.getString("smsQfId");
			//如果没变则不返回数据;
			if(smsQfId != null && !"".equals(smsQfId) && tmpId != null && !"".equals(tmpId)){
				int existNum = jdbcTemplate.queryForObject("select COUNT(1) from PS_TZ_DXYJQF_DY_T WHERE TZ_MLSM_QFPC_ID=? AND TZ_TMPL_ID=?", new Object[]{smsQfId,tmpId},"Integer");
				if(existNum > 0){
					return jacksonUtil.Map2json(map);
				}
			}
			
			map.put("smsCont", "");
			String strOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			String smsContent = jdbcTemplate.queryForObject(
					" SELECT TZ_SMS_CONTENT FROM PS_TZ_SMSTMPL_TBL WHERE TZ_JG_ID=? AND TZ_TMPL_ID=?",
					new Object[] { strOrgId, tmpId }, "String");
			if(smsContent!=null && !"".equals(smsContent)){
				map.replace("smsCont", smsContent);
			}
			
		}
		return jacksonUtil.Map2json(map);
	}

	// 功能说明：查询邮件模版信息项
	private String getSmsTmplItem(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> map = new HashMap<>();
		int numItem = 0;
		map.put("total", numItem);
		ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
		map.put("root", arrayList);

		String strOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		if (jacksonUtil.containsKey("SmsTmpId")) {
			String tmpId = jacksonUtil.getString("SmsTmpId");
			if (tmpId != null && !"".equals(tmpId)) {
				Map<String, Object> map1 = jdbcTemplate.queryForMap(
						"SELECT A.TZ_YMB_ID,B.TZ_YMB_CSLBM FROM PS_TZ_SMSTMPL_TBL A,PS_TZ_TMP_DEFN_TBL B WHERE A.TZ_JG_ID=? AND A.TZ_TMPL_ID=? AND A.TZ_YMB_ID=B.TZ_YMB_ID",
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

		if (jacksonUtil.containsKey("smsQfId")) {
			String smsQfId = jacksonUtil.getString("smsQfId");
			if (smsQfId != null && !"".equals(smsQfId)) {
				List<Map<String, Object>> list2 = jdbcTemplate.queryForList(
						"SELECT TZ_XXX_NAME FROM PS_TZ_EXC_SET_TBL WHERE TZ_MLSM_QFPC_ID=?", new Object[] { smsQfId });
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

	// 功能说明：查询短信元模版信息
	private String getSmsMetaTmpInfo(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> map = new HashMap<>();
		map.put("smstemporg", "");
		map.put("smstempid", "");
		map.put("metaempid", "");

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		if (jacksonUtil.containsKey("smsTmpId")) {
			String tmpId = jacksonUtil.getString("smsTmpId");
			if (tmpId != null && !"".equals(tmpId)) {
				String strOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				String strMetaEmlId = jdbcTemplate.queryForObject(
						"SELECT TZ_YMB_ID FROM PS_TZ_EMALTMPL_TBL WHERE TZ_JG_ID=? AND TZ_TMPL_ID=?",
						new Object[] { strOrgId, tmpId }, "String");
				if (strMetaEmlId == null) {
					strMetaEmlId = "";
				}

				map.replace("smstemporg", strOrgId);
				map.replace("smstempid", tmpId);
				map.replace("metaempid", strMetaEmlId);
			}
		}

		return jacksonUtil.Map2json(map);
	}

	// 功能说明：保存
	private String saveSmsBulkInfo(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> map = new HashMap<>();
		map.put("smsQfId", "");
		map.put("creDt", "");
		map.put("receverOrigin", "");

		// 任务编号，任务名称，发件人，发送模式;
		String strsmsQfId = "", strsmsQfDesc = "", strsendModel = "";
		// 模版id，邮件主题，邮件内容，抄送人，同时发送标志，同时发送Email，收件人;
		String strsmsTmpId = "", strsmsCont = "", strtsfsFlag = "", strtsfsPhone = "", strrecever = "";
		// 定时发送标志，发送日期，发送时间，强制发送;
		String strdsfsFlag = "", strdsfsDate = "", strdsfsTime = "";
		// 收件人s;
		String[] arrrecever;

		// 收件人Origin;
		// String strreceverOrigin = "";
		// 收件人Origin s;
		// String[] arrreceverOrigin;

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

		if (formDataJson != null) {
			strsmsQfId = (String) formDataJson.get("smsQfId");
			strsmsQfDesc = (String) formDataJson.get("smsQfDesc");
			strsendModel = (String) formDataJson.get("sendModel");
			if (formDataJson.containsKey("tsfsFlag")) {
				strtsfsFlag = (String) formDataJson.get("tsfsFlag");
			}
			if (formDataJson.containsKey("tsfsPhone")) {
				strtsfsPhone = (String) formDataJson.get("tsfsPhone");
			}
			if (formDataJson.containsKey("smsTmpId")) {
				strsmsTmpId = (String) formDataJson.get("smsTmpId");
			}

			String strQm = (String) formDataJson.get("smsQm");

			strsmsCont = (String) formDataJson.get("smsCont");

			strdsfsFlag = (String) formDataJson.get("dsfsFlag");
			strdsfsDate = (String) formDataJson.get("dsfsDate");
			if (formDataJson.containsKey("dsfsTime")) {
				strdsfsTime = (String) formDataJson.get("dsfsTime");
			}

			String transFlag = (String) formDataJson.get("transmitFlag");

			// strreceverOrigin = (String) formDataJson.get("receverOrigin");
			if (othInfoJson != null) {
				strrecever = (String) othInfoJson.get("recever");
			}

			if ("on".equals(strtsfsFlag)) {
				strtsfsFlag = "Y";
			}

			if (strrecever != null && !"".equals(strrecever)) {
				strrecever = strrecever.replace(";", ",");
				strrecever = strrecever.replace("；", ",");
				strrecever = strrecever.replace("，", ",");
			}
			arrrecever = strrecever.split(",");

			// arrreceverOrigin = strreceverOrigin.split(",");
			PsTzDxyjQfDyTWithBLOBs psTzDxyjQfDyT = psTzDxyjQfDyTMapper.selectByPrimaryKey(strsmsQfId);
			if (psTzDxyjQfDyT != null) {
				// 邮件群发任务;
				psTzDxyjQfDyT.setTzMlsmQfpcDesc(strsmsQfDesc);
				psTzDxyjQfDyT.setTzSendModel(strsendModel);
				psTzDxyjQfDyT.setTzTmplId(strsmsTmpId);
				psTzDxyjQfDyT.setTzTsfsFlag(strtsfsFlag);
				psTzDxyjQfDyT.setTzTsfsAddr(strtsfsPhone);
				psTzDxyjQfDyT.setTzDxqm(strQm);
				psTzDxyjQfDyT.setTzSmsContent(strsmsCont);
				psTzDxyjQfDyT.setTzDsfsFlag(strdsfsFlag);
				psTzDxyjQfDyT.setTzZfFlag(transFlag);
				psTzDxyjQfDyT.setTzOffSend("N");

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
				psTzDxyjQfDyT.setRowLastmantDttm(new Date());
				psTzDxyjQfDyT.setRowLastmantOprid(strOprId);
				psTzDxyjQfDyTMapper.updateByPrimaryKeyWithBLOBs(psTzDxyjQfDyT);
			} else {
				// 邮件群发任务;
				psTzDxyjQfDyT = new PsTzDxyjQfDyTWithBLOBs();
				// 邮件群发任务;
				psTzDxyjQfDyT.setTzMlsmQfpcId(strsmsQfId);
				psTzDxyjQfDyT.setTzMlsmQfpcDesc(strsmsQfDesc);
				psTzDxyjQfDyT.setTzQfType("SMS");
				psTzDxyjQfDyT.setOprid(strOprId);
				psTzDxyjQfDyT.setTzJgId(strOrgId);
				psTzDxyjQfDyT.setTzSendModel(strsendModel);
				psTzDxyjQfDyT.setTzTmplId(strsmsTmpId);
				psTzDxyjQfDyT.setTzTsfsFlag(strtsfsFlag);
				psTzDxyjQfDyT.setTzTsfsAddr(strtsfsPhone);
				psTzDxyjQfDyT.setTzDsfsFlag(strdsfsFlag);
				psTzDxyjQfDyT.setTzDxqm(strQm);
				psTzDxyjQfDyT.setTzSmsContent(strsmsCont);

				psTzDxyjQfDyT.setTzZfFlag(transFlag);
				psTzDxyjQfDyT.setTzOffSend("N");
				psTzDxyjQfDyT.setTzAutoCreate("N");

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
						new Object[] { strsmsQfId });

				jdbcTemplate.update("DELETE from PS_TZ_MLSM_DRNR_T WHERE TZ_MLSM_QFPC_ID=?",
						new Object[] { strsmsQfId });

				// 删除收件人数据;
				jdbcTemplate.update("DELETE from PS_TZ_DXYJQFSJR_T WHERE TZ_MLSM_QFPC_ID=?",
						new Object[] { strsmsQfId });
				
				// 删除听众;
				jdbcTemplate.update("DELETE from PS_TZ_DXYJQAUD_T WHERE TZ_MLSM_QFPC_ID=?",
						new Object[] { strsmsQfId });
				
				// 添加收件人;
				for (int i = 0; i < arrrecever.length; i++) {
					String sjrStr = arrrecever[i];
					//查看是不是听众，是听众则插入听众表;
					int count = jdbcTemplate.queryForObject("select COUNT(1) FROM PS_TZ_AUD_DEFN_T WHERE TZ_AUD_ID=?", new Object[]{sjrStr},"Integer");
					if(count == 1){
						PsTzDxyjqaudTKey psTzDxyjqaudTKey = new PsTzDxyjqaudTKey();
						psTzDxyjqaudTKey.setTzMlsmQfpcId(strsmsQfId);
						psTzDxyjqaudTKey.setTzAudienceId(sjrStr);
						psTzDxyjqaudTMapper.insert(psTzDxyjqaudTKey);
					}else{
						PsTzDxYjQfSjrT psTzDxYjQfSjrT = new PsTzDxYjQfSjrT();
						psTzDxYjQfSjrT.setTzMlsmQfpcId(strsmsQfId);
						psTzDxYjQfSjrT.setTzAudcyId(String.valueOf(getSeqNum.getSeqNum("TZ_AUDCYUAN_T", "TZ_AUDCY_ID")));
						psTzDxYjQfSjrT.setTzEmail(sjrStr);
						psTzDxYjQfSjrTMapper.insert(psTzDxYjQfSjrT);
					}
					
				}

			} else {
				if ("EXC".equals(strsendModel)) {
					jdbcTemplate.update("DELETE FROM PS_TZ_DXYJQFSJR_T WHERE TZ_MLSM_QFPC_ID=?",
							new Object[] { strsmsQfId });
					
					// 删除听众;
					jdbcTemplate.update("DELETE from PS_TZ_DXYJQAUD_T WHERE TZ_MLSM_QFPC_ID=?",
							new Object[] { strsmsQfId });
				}
			}

			// 定时发送;
			if ("Y".equals(strdsfsFlag)) {
				PsTzDxYjDsfsT psTzDxYjDsfsT = psTzDxYjDsfsTMapper.selectByPrimaryKey(strsmsQfId);
				if (psTzDxYjDsfsT != null) {
					psTzDxYjDsfsT.setTzSendZt("N");
					psTzDxYjDsfsTMapper.updateByPrimaryKey(psTzDxYjDsfsT);
				} else {
					psTzDxYjDsfsT = new PsTzDxYjDsfsT();
					psTzDxYjDsfsT.setTzMlsmQfpcId(strsmsQfId);
					psTzDxYjDsfsT.setTzSendZt("N");
					psTzDxYjDsfsTMapper.insert(psTzDxYjDsfsT);
				}
			} else {
				PsTzDxYjDsfsT psTzDxYjDsfsT = psTzDxYjDsfsTMapper.selectByPrimaryKey(strsmsQfId);
				if (psTzDxYjDsfsT != null) {
					psTzDxYjDsfsTMapper.deleteByPrimaryKey(strsmsQfId);
				}
			}

		}

		map.replace("smsQfId", strsmsQfId);
		SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		map.replace("creDt", datetimeFormate.format(new Date()));
		map.replace("receverOrigin", strrecever);

		return jacksonUtil.Map2json(map);
	}

	private String sendSms(String comParams, String[] errorMsg) {
		String strComContent = this.saveSmsBulkInfo(comParams, errorMsg);

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		Map<String, Object> formDataJson = jacksonUtil.getMap("formdata");

		// 是否定时发送;
		String strdsfsFlag = (String) formDataJson.get("dsfsFlag");
		Date dsfsDateTime = null;
		if ("Y".equals(strdsfsFlag)) {
			String strdsfsDate = (String) formDataJson.get("dsfsDate");
			String strdsfsTime = "00:00";
			if (formDataJson.containsKey("dsfsTime")) {
				strdsfsTime = (String) formDataJson.get("dsfsTime");
			}

			String fsDateStr = strdsfsDate + " " + strdsfsTime;

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			try {
				dsfsDateTime = sdf.parse(fsDateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		// 同时发送;
		String strTsfsFlag = "", strtsfsPhone = "";
		if (formDataJson.containsKey("tsfsFlag")) {
			strTsfsFlag = (String) formDataJson.get("tsfsFlag");
		}
		// 同时发送
		if (formDataJson.containsKey("tsfsPhone")) {
			strtsfsPhone = (String) formDataJson.get("tsfsPhone");
		}
		// TaskId;
		String strTaskId = "";

		// 邮件群发编号;
		String strsmsQfId = (String) formDataJson.get("smsQfId");
		// 发送模式;
		String strsendModel = (String) formDataJson.get("sendModel");

		String strOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);

		// 创建任务;
		strTaskId = createQfTaskServiceImpl.createTaskIns(strOrgId, strsmsQfId, "SMS", "A");
		// 创建听众;
		String strAudienceDesc = "短信邮件群发_MAL_" + strsmsQfId;
		String strAudID = createQfTaskServiceImpl.createAudience(strTaskId, strOrgId, strAudienceDesc, "DXQF");

		// 添加听众成员;
		String strSql = "";
		String strAudIDTemp = "", strSmsTmp = "";
		if ("NOR".equals(strsendModel)) {
			// 收件人;
			strSql = "SELECT TZ_AUDCY_ID,TZ_EMAIL FROM PS_TZ_DXYJQFSJR_T WHERE TZ_MLSM_QFPC_ID= ?";
			List<Map<String, Object>> sjrList = jdbcTemplate.queryForList(strSql, new Object[] { strsmsQfId });
			if (sjrList != null && sjrList.size() > 0) {
				for (int i = 0; i < sjrList.size(); i++) {

					strAudIDTemp = (String) sjrList.get(i).get("TZ_AUDCY_ID");
					strSmsTmp = (String) sjrList.get(i).get("TZ_EMAIL");

					createQfTaskServiceImpl.addAudCy(strAudID, strAudIDTemp, "", "", strSmsTmp, "", "", "", "", "", "",
							"", "");
				}

			}
			
			// 添加听众 ;
			strSql = "SELECT TZ_AUDIENCE_ID FROM PS_TZ_DXYJQAUD_T WHERE TZ_MLSM_QFPC_ID = ?";
			List<Map<String, Object>> audList = jdbcTemplate.queryForList(strSql, new Object[] { strsmsQfId });
			if(audList != null && audList.size() > 0){
				ArrayList<String> audPersonidArr = new ArrayList<>();
				for(int i = 0; i < audList.size(); i++){
					String audiendeId = (String)audList.get(i).get("TZ_AUDIENCE_ID");
					String sql = "select a.OPRID,b.TZ_ZY_SJ,c.TZ_REALNAME FROM PS_TZ_AUD_LIST_T a, PS_TZ_LXFSINFO_TBL b,PS_TZ_AQ_YHXX_TBL c where a.TZ_LXFS_LY=b.TZ_LXFS_LY and a.TZ_LYDX_ID=b.TZ_LYDX_ID and a.OPRID=c.OPRID and a.TZ_AUD_ID=? and a.TZ_DXZT<>'N'";
					List<Map<String, Object>> oprList = jdbcTemplate.queryForList(sql,new Object[]{audiendeId});
								
					if(oprList != null && oprList.size()>0){
						for(int j = 0; j<oprList.size(); j++){
							String oprid = (String)oprList.get(j).get("OPRID");
							String phone = (String)oprList.get(j).get("TZ_ZY_SJ");
							String realName = (String)oprList.get(j).get("TZ_REALNAME");
							if(audPersonidArr.contains(oprid)){
											
							}else{
								audPersonidArr.add(oprid);
								createQfTaskServiceImpl.addAudCy(strAudID, "", realName, "", phone, "", "", "", "", oprid, "",
													"", "");
							}
						}
					}
				}
			}
		} else {
			if ("EXC".equals(strsendModel)) {
				String strXXXField = "", strXXXFieldTemp = "";
				String strNameTemp = "";
				int numFieldCount = 0;
				String typeAFieldName = "";
				String typeBFieldName = "";
				strSql = "SELECT TZ_FIELD_NAME FROM PS_TZ_EXC_SET_TBL WHERE TZ_MLSM_QFPC_ID=? AND TZ_XXX_TYPE IN('A','B') order by TZ_XXX_TYPE";
				List<Map<String, Object>> excList = jdbcTemplate.queryForList(strSql, new Object[] { strsmsQfId });
				if (excList != null && excList.size() > 0) {
					for (int i = 0; i < excList.size(); i++) {
						strXXXFieldTemp = (String) excList.get(i).get("TZ_FIELD_NAME");
						if ("".equals(strXXXField)) {
							strXXXField = strXXXFieldTemp;
						} else {
							strXXXField = strXXXField + "," + strXXXFieldTemp;
						}
						if (i == 0) {
							typeAFieldName = strXXXFieldTemp;
						}
						if (i == 1) {
							typeBFieldName = strXXXFieldTemp;
						}

						numFieldCount = numFieldCount + 1;
					}
				}

				if ("".equals(strXXXField)) {
					strXXXField = "TZ_AUDCY_ID";
				} else {
					strXXXField = "TZ_AUDCY_ID," + strXXXField;
				}

				strSql = "SELECT " + strXXXField + " FROM PS_TZ_MLSM_DRNR_T WHERE TZ_MLSM_QFPC_ID=?";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(strSql, new Object[] { strsmsQfId });
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						if (numFieldCount == 1) {
							strAudIDTemp = (String) list.get(i).get("TZ_AUDCY_ID");
							strSmsTmp = (String) list.get(i).get(typeAFieldName);
						}

						if (numFieldCount == 2) {
							strAudIDTemp = (String) list.get(i).get("TZ_AUDCY_ID");
							strNameTemp = (String) list.get(i).get(typeAFieldName);
							strSmsTmp = (String) list.get(i).get(typeBFieldName);
						}

						createQfTaskServiceImpl.addAudCy(strAudID, strAudIDTemp, strNameTemp, "", strSmsTmp, "", "", "",
								"", "", "", "", "");
					}
				}
			}
		}

		// 同时发送;
		if ("on".equals(strTsfsFlag)) {
			createQfTaskServiceImpl.addAudCy(strAudID, "", "", "", strtsfsPhone, "", "", "", "", "", "", "", "");
		}

		PsTzDxYjDsfsT psTzDxYjDsfsT = psTzDxYjDsfsTMapper.selectByPrimaryKey(strsmsQfId);
		if (psTzDxYjDsfsT != null) {
			psTzDxYjDsfsT.setTzEmlSmsTaskId(strTaskId);
			psTzDxYjDsfsTMapper.updateByPrimaryKey(psTzDxYjDsfsT);
		}

		//sendSmsOrMalQfServiceImpl.send(strTaskId, "");
		
		int processInstance = getSeqNum.getSeqNum("PSPRCSRQST", "PROCESSINSTANCE");
		// 当前用户;
		String currentOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		// 生成运行控制ID
		SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
		String s_dtm = datetimeFormate.format(new Date());
		String runCntlId = "SMS" + s_dtm + "_" + getSeqNum.getSeqNum("PSPRCSRQST", "RUN_ID");

		Psprcsrqst psprcsrqst = new Psprcsrqst();
		psprcsrqst.setPrcsinstance(processInstance);
		psprcsrqst.setRunId(runCntlId);
		psprcsrqst.setOprid(currentOprid);
		psprcsrqst.setRundttm(new Date());
		psprcsrqst.setRunstatus("5");
		psprcsrqstMapper.insert(psprcsrqst);

		PsTzEmlTaskAet psTzEmlTaskAet = new PsTzEmlTaskAet();
		psTzEmlTaskAet.setRunId(runCntlId);
		psTzEmlTaskAet.setTzEmlSmsTaskId(strTaskId);
		psTzEmlTaskAetMapper.insert(psTzEmlTaskAet);

		try {
			String currentAccountId = tzLoginServiceImpl.getLoginedManagerDlzhid(request);
			String currentOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			BaseEngine tmpEngine = tZGDObject.createEngineProcess(currentOrgId, "TZGD_QF_MS_AE");
			// 指定调度作业的相关参数
			EngineParameters schdProcessParameters = new EngineParameters();

			schdProcessParameters.setBatchServer("");
			schdProcessParameters.setCycleExpression("");
			schdProcessParameters.setLoginUserAccount(currentAccountId);
			// 不是定时发送的;
			if (!"Y".equals(strdsfsFlag)) {
				schdProcessParameters.setPlanExcuteDateTime(new Date());
			} else {
				// 定时发送;
				schdProcessParameters.setPlanExcuteDateTime(dsfsDateTime);
			}
			schdProcessParameters.setRunControlId(runCntlId);

			// 调度作业
			tmpEngine.schedule(schdProcessParameters);
		} catch (Exception e) {

		}

		String strRwzxZt = "";
		strRwzxZt = jdbcTemplate.queryForObject("SELECT TZ_RWZX_ZT FROM PS_TZ_DXYJFSRW_TBL WHERE TZ_EML_SMS_TASK_ID=?",
				new Object[] { strTaskId }, "String");
		strComContent = strComContent.substring(0, strComContent.length() - 1);
		strComContent = strComContent + ",\"rwzxZt\":\"" + strRwzxZt + "\"}";
		return strComContent;
	}

	// 功能说明：中断发送
	private String revoke(String comParams, String[] errorMsg) {
		// 返回值;
		String strComContent = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		if (jacksonUtil.containsKey("smsQfId")) {
			String strsmsQfId = jacksonUtil.getString("smsQfId");
			if (strsmsQfId != null && !"".equals(strsmsQfId)) {
				// 短信邮件群发任务批次定义表;
				PsTzDxyjQfDyTWithBLOBs psTzDxyjQfDyT = psTzDxyjQfDyTMapper.selectByPrimaryKey(strsmsQfId);
				if (psTzDxyjQfDyT != null) {
					psTzDxyjQfDyT.setTzOffSend("Y");
					psTzDxyjQfDyTMapper.updateByPrimaryKeySelective(psTzDxyjQfDyT);
				}

				// 短信邮件定时发送表;
				PsTzDxYjDsfsT psTzDxYjDsfsT = psTzDxYjDsfsTMapper.selectByPrimaryKey(strsmsQfId);
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
		if (jacksonUtil.containsKey("smsQfId")) {
			String strsmsQfId = jacksonUtil.getString("smsQfId");
			if (strsmsQfId != null && !"".equals(strsmsQfId)) {
				jdbcTemplate.update("delete from PS_TZ_EXC_SET_TBL where TZ_MLSM_QFPC_ID=?",
						new Object[] { strsmsQfId });

				jdbcTemplate.update("delete from PS_TZ_MLSM_DRNR_T where TZ_MLSM_QFPC_ID=?",
						new Object[] { strsmsQfId });
			}
		}

		return strComContent;
	}

	// 获取任务执行状态;
	private String getRwzxZt(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> map = new HashMap<>();
		map.put("smsQfId", "");
		map.put("rwzxZt", "");

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		if (jacksonUtil.containsKey("smsQfId")) {
			String strsmsQfId = jacksonUtil.getString("smsQfId");

			String sql = "SELECT TZ_RWZX_ZT FROM PS_TZ_DXYJFSRW_TBL WHERE TZ_MLSM_QFPC_ID=? ORDER BY CAST(TZ_EML_SMS_TASK_ID as SIGNED) DESC limit 0,1";
			String strRwzxZt = jdbcTemplate.queryForObject(sql, new Object[] { strsmsQfId }, "String");
			if (strRwzxZt == null) {
				strRwzxZt = "";
			}
			map.replace("smsQfId", strsmsQfId);
			map.replace("rwzxZt", strRwzxZt);
		}

		return jacksonUtil.Map2json(map);
	}

	/* 历史群发任务信息 */
	private String getHistoryRwInfo(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> map = new HashMap<>();

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);

		// 任务编号，任务名称，发件人，发送模式;
		String strsmsQfId, strsmsQfDesc, strsender, strsendModel;
		// 模版id，邮件主题，邮件内容，抄送人，同时发送标志，同时发送Email，收件人;
		String strsmsTmpId, strsmsQm, strsmsCont, strtsfsPhone, strtsfsFlag;
		// 启用edm，取消订阅，启用频发，每小时发送量，定时发送标志，发送日期，发送时间，强制发送;
		String stredmFlag, strqxdyFlag, strqypfFlag, strfsslXs, strdsfsFlag, strdsfsDate, strdsfsTime, strqzfsFlag;

		ArrayList<String> arrrecever = new ArrayList<String>();
		// ArrayList<String> arrccmail = new ArrayList<String>();

		strsmsQfId = jacksonUtil.getString("smsQfId");

		/* 当前群发任务ID */
		String strcurrSmsQfId = jacksonUtil.getString("currSmsQfId");

		String qflsSql = "SELECT TZ_MLSM_QFPC_DESC,TZ_SEND_MODEL,TZ_TMPL_ID,TZ_DXQM,TZ_SMS_CONTENT,TZ_TSFS_FLAG,TZ_TSFS_ADDR,TZ_DSFS_FLAG,TZ_ZF_FLAG FROM PS_TZ_DXYJQF_DY_T where TZ_MLSM_QFPC_ID=?";
		Map<String, Object> lsQfMap = jdbcTemplate.queryForMap(qflsSql, new Object[] { strsmsQfId });

		strsmsQfDesc = String.valueOf(lsQfMap.get("TZ_MLSM_QFPC_DESC"));

		strsendModel = String.valueOf(lsQfMap.get("TZ_SEND_MODEL"));

		strsmsTmpId = String.valueOf(lsQfMap.get("TZ_TMPL_ID"));
		strsmsQm = String.valueOf(lsQfMap.get("TZ_DXQM"));
		strsmsCont = String.valueOf(lsQfMap.get("TZ_SMS_CONTENT"));
		strtsfsFlag = String.valueOf(lsQfMap.get("TZ_TSFS_FLAG"));

		strtsfsPhone = String.valueOf(lsQfMap.get("TZ_TSFS_ADDR"));
		strdsfsFlag = String.valueOf(lsQfMap.get("TZ_DSFS_FLAG"));
		// transFlag = String.valueOf(lsQfMap.get("TZ_ZF_FLAG"));

		String strSql = "";
		int i = 0;

		// 收件人;
		if ("NOR".equals(strsendModel)) {
			// 邮件群发听众;
			 strSql = "SELECT TZ_AUDIENCE_ID FROM PS_TZ_DXYJQAUD_T WHERE TZ_MLSM_QFPC_ID=?";
			 List<Map<String, Object>> autlist = jdbcTemplate.queryForList(strSql, new Object[] { strsmsQfId });
			 if (autlist != null && autlist.size() > 0) {
				for (i = 0; i < autlist.size(); i++) {
					arrrecever.add(String.valueOf(autlist.get(i).get("TZ_AUDIENCE_ID")));
				}
			}
			 
			// 邮件群发收件人;
			strSql = "SELECT TZ_EMAIL FROM PS_TZ_DXYJQFSJR_T WHERE TZ_MLSM_QFPC_ID=?";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(strSql, new Object[] { strsmsQfId });
			if (list != null && list.size() > 0) {
				for (i = 0; i < list.size(); i++) {
					arrrecever.add(String.valueOf(list.get(i).get("TZ_EMAIL")));
				}
			}
		} else {
			if ("EXC".equals(strsendModel)) {
				String strExcEmlField = "";
				strExcEmlField = jdbcTemplate.queryForObject(
						"select TZ_FIELD_NAME from PS_TZ_EXC_SET_TBL where TZ_MLSM_QFPC_ID=? and TZ_XXX_TYPE='B'",
						new Object[] { strsmsQfId }, "String");
				if (strExcEmlField != null && !"".equals(strExcEmlField)) {
					strSql = "SELECT " + strExcEmlField + " FROM PS_TZ_MLSM_DRNR_T WHERE TZ_MLSM_QFPC_ID=? limit 0,20";
					List<Map<String, Object>> list = jdbcTemplate.queryForList(strSql, new Object[] { strsmsQfId });
					if (list != null && list.size() > 0) {
						for (i = 0; i < list.size(); i++) {
							arrrecever.add(String.valueOf(list.get(i).get(strExcEmlField)));
						}

					}
				}

				jdbcTemplate.update("DELETE FROM PS_TZ_EXC_SET_TBL WHERE TZ_MLSM_QFPC_ID=?",
						new Object[] { strsmsQfId });
				jdbcTemplate.update("DELETE FROM PS_TZ_MLSM_DRNR_T WHERE TZ_MLSM_QFPC_ID=?",
						new Object[] { strsmsQfId });

				strSql = "select * from PS_TZ_MLSM_DRNR_T where TZ_MLSM_QFPC_ID = ?";
				List<Map<String, Object>> list2 = jdbcTemplate.queryForList(strSql, new Object[] { strsmsQfId });
				if (list2 != null && list2.size() > 0) {
					for (i = 0; i < list2.size(); i++) {
						PsTzMlsmDrnrT psTzMlsmDrnrT = new PsTzMlsmDrnrT();
						psTzMlsmDrnrT.setTzMlsmQfpcId(strsmsQfId);
						String audCyId = String.valueOf(getSeqNum.getSeqNum("TZ_AUDCYUAN_T", "TZ_AUDCY_ID"));
						psTzMlsmDrnrT.setTzAudcyId(audCyId);
						psTzMlsmDrnrT.setTzMlsmContent(String.valueOf(list2.get(i).get("TZ_MLSM_CONTENT")));
						psTzMlsmDrnrT.setTzXxxNr1(String.valueOf(list2.get(i).get("TZ_XXX_NR1")));
						psTzMlsmDrnrT.setTzXxxNr2(String.valueOf(list2.get(i).get("TZ_XXX_NR2")));
						psTzMlsmDrnrT.setTzXxxNr3(String.valueOf(list2.get(i).get("TZ_XXX_NR3")));
						psTzMlsmDrnrT.setTzXxxNr4(String.valueOf(list2.get(i).get("TZ_XXX_NR4")));
						psTzMlsmDrnrT.setTzXxxNr5(String.valueOf(list2.get(i).get("TZ_XXX_NR5")));
						psTzMlsmDrnrT.setTzXxxNr6(String.valueOf(list2.get(i).get("TZ_XXX_NR6")));
						psTzMlsmDrnrT.setTzXxxNr7(String.valueOf(list2.get(i).get("TZ_XXX_NR7")));
						psTzMlsmDrnrT.setTzXxxNr8(String.valueOf(list2.get(i).get("TZ_XXX_NR8")));
						psTzMlsmDrnrT.setTzXxxNr9(String.valueOf(list2.get(i).get("TZ_XXX_NR9")));
						psTzMlsmDrnrT.setTzXxxNr10(String.valueOf(list2.get(i).get("TZ_XXX_NR10")));
						psTzMlsmDrnrT.setTzXxxNr11(String.valueOf(list2.get(i).get("TZ_XXX_NR11")));
						psTzMlsmDrnrT.setTzXxxNr12(String.valueOf(list2.get(i).get("TZ_XXX_NR12")));
						psTzMlsmDrnrT.setTzXxxNr13(String.valueOf(list2.get(i).get("TZ_XXX_NR13")));
						psTzMlsmDrnrT.setTzXxxNr14(String.valueOf(list2.get(i).get("TZ_XXX_NR14")));
						psTzMlsmDrnrT.setTzXxxNr15(String.valueOf(list2.get(i).get("TZ_XXX_NR15")));
						psTzMlsmDrnrT.setTzXxxNr16(String.valueOf(list2.get(i).get("TZ_XXX_NR16")));
						psTzMlsmDrnrT.setTzXxxNr17(String.valueOf(list2.get(i).get("TZ_XXX_NR17")));
						psTzMlsmDrnrT.setTzXxxNr18(String.valueOf(list2.get(i).get("TZ_XXX_NR18")));
						psTzMlsmDrnrT.setTzXxxNr19(String.valueOf(list2.get(i).get("TZ_XXX_NR19")));
						psTzMlsmDrnrTMapper.insert(psTzMlsmDrnrT);

					}

					strSql = "select * from PS_TZ_EXC_SET_TBL where TZ_MLSM_QFPC_ID = ?";
					List<Map<String, Object>> list3 = jdbcTemplate.queryForList(strSql, new Object[] { strsmsQfId });
					if (list3 != null && list3.size() > 0) {
						for (i = 0; i < list3.size(); i++) {
							PsTzExcSetTbl psTzExcSetTbl = new PsTzExcSetTbl();
							psTzExcSetTbl.setTzMlsmQfpcId(strcurrSmsQfId);
							psTzExcSetTbl.setTzIndex(Integer.valueOf(String.valueOf((list3.get(i).get("TZ_INDEX")))));
							psTzExcSetTbl.setTzXxxName(String.valueOf(list3.get(i).get("TZ_XXX_NAME")));
							psTzExcSetTbl.setTzXxxType(String.valueOf(list3.get(i).get("TZ_XXX_TYPE")));
							psTzExcSetTbl.setTzFieldName(String.valueOf(list3.get(i).get("TZ_FIELD_NAME")));
							psTzExcSetTblMapper.insert(psTzExcSetTbl);
						}
					}

				}

			}
		}

		if ("Y".equals(strtsfsFlag)) {
			strtsfsFlag = "on";
		}

		map.put("smsQfDesc", strsmsQfDesc);
		map.put("sendModel", strsendModel);
		map.put("recever", arrrecever);
		map.put("tsfsFlag", strtsfsFlag);
		map.put("tsfsPhone", strtsfsPhone);
		map.put("smsTmpId", strsmsTmpId);
		map.put("smsQm", strsmsQm);
		map.put("smsCont", strsmsCont);
		map.put("dsfsFlag", strdsfsFlag);
		map.put("receverOrigin", "");

		return jacksonUtil.Map2json(map);

	}

	/* 查询收件人 */
	private String queryReceverList(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);

		String strSmsQfId = ""; /* 群发任务id */
		if (jacksonUtil.containsKey("smsQfId")) {
			strSmsQfId = jacksonUtil.getString("smsQfId");
			if(strSmsQfId == null || "".equals(strSmsQfId)){
				return jacksonUtil.Map2json(mapRet);
			}
		} else {
			return jacksonUtil.Map2json(mapRet);
		}
		
		// 邮件群发听众;
		String strSql = "select A.TZ_AUDIENCE_ID,B.TZ_AUD_NAM from PS_TZ_DXYJQAUD_T A,PS_TZ_AUD_DEFN_T B WHERE A.TZ_AUDIENCE_ID=B.TZ_AUD_ID AND A.TZ_MLSM_QFPC_ID=?";
		List<Map<String, Object>> audlist = jdbcTemplate.queryForList(strSql, new Object[] { strSmsQfId });
		if (audlist != null && audlist.size() > 0) {
			int i = 0;
			String strID, strDesc;
			for (i = 0; i < audlist.size(); i++) {
				strID = (String) audlist.get(i).get("TZ_AUDIENCE_ID");
				strDesc = (String) audlist.get(i).get("TZ_AUD_NAM");
				if (strID != null && !"".equals(strID) && strDesc != null && !"".equals(strDesc)) {
					Map<String, Object> map = new HashMap<>();
					map.put("id", strID);
					map.put("desc", strDesc);
					listData.add(map);
				}
			}
			mapRet.replace("root", listData);
		}

		// 短信群发收件人;
		strSql = "SELECT TZ_AUDCY_ID,TZ_EMAIL FROM PS_TZ_DXYJQFSJR_T WHERE TZ_MLSM_QFPC_ID=?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(strSql, new Object[] { strSmsQfId });
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

	/* 查询短信模版 */
	private String queryEmlTmplList(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);

		JacksonUtil jacksonUtil = new JacksonUtil();

		// 当前登录人的机构;
		String strOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"SELECT TZ_TMPL_ID,TZ_TMPL_NAME FROM PS_TZ_SMSTMPL_TBL WHERE TZ_JG_ID=? AND TZ_USE_FLAG='Y'",
				new Object[] { strOrgId });
		if (list != null && list.size() > 0) {
			int i = 0;
			String strEmailTmpl, strDesc;
			for (i = 0; i < list.size(); i++) {
				strEmailTmpl = (String) list.get(i).get("TZ_TMPL_ID");
				strDesc = (String) list.get(i).get("TZ_TMPL_NAME") == null ? ""
						: (String) list.get(i).get("TZ_TMPL_NAME");

				Map<String, Object> map = new HashMap<>();
				map.put("smstmpl", strEmailTmpl);
				map.put("desc", strDesc);
				listData.add(map);
			}
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);

	}

}
