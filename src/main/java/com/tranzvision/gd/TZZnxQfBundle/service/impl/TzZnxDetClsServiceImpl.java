package com.tranzvision.gd.TZZnxQfBundle.service.impl;

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
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzYjQfFjXxTblMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzDxYjDsfsT;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzDxYjQfSjrT;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzDxyjQfDyTWithBLOBs;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzDxyjqaudTKey;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzYjQfFjXxTbl;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzYjQfFjXxTblKey;
import com.tranzvision.gd.TZEmailSmsQFBundle.service.impl.CreateQfTaskServiceImpl;
import com.tranzvision.gd.TZEmailSmsQFBundle.service.impl.SendSmsOrMalQfServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzEmlTaskAetMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzEmlTaskAet;
import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.batch.engine.base.EngineParameters;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.TZZnxQfBundle.service.impl.TzZnxDetClsServiceImpl")
public class TzZnxDetClsServiceImpl extends FrameworkImpl {
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
	private PsTzDxyjqaudTMapper psTzDxyjqaudTMapper;

	@Autowired
	private PsTzDxYjQfSjrTMapper psTzDxYjQfSjrTMapper;

	@Autowired
	private PsTzYjQfFjXxTblMapper psTzYjQfFjXxTblMapper;

	@Autowired
	private CreateQfTaskServiceImpl createQfTaskServiceImpl;

	@Autowired
	private PsTzDxYjDsfsTMapper psTzDxYjDsfsTMapper;

	@Autowired
	private PsprcsrqstMapper psprcsrqstMapper;

	@Autowired
	private PsTzEmlTaskAetMapper psTzEmlTaskAetMapper;

	@Autowired
	private SendSmsOrMalQfServiceImpl sendSmsOrMalQfServiceImpl;

	@Autowired
	private TZGDObject tZGDObject;

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

			if ("znxtmpl".equals(queryID)) {
				strComContent = this.queryZnxTmplList(comParams, errorMsg);
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

	// 获取群发任务信息;
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回;
		Map<String, Object> map = new HashMap<>();
		map.put("znxQfId", "");
		map.put("znxQfDesc", "");
		map.put("crePer", "");
		// 收件人s;
		ArrayList<String> arrrecever = new ArrayList<>();
		map.put("recever", arrrecever);

		map.put("znxTmpId", "");
		map.put("znxSubj", "");
		map.put("znxCont", "");
		map.put("creDt", "");
		map.put("rwzxZt", "");

		String strznxQfId = "", strznxQfDesc = "", strznxQfTmpId = "", strznxQfSubj = "", strznxQfCont = "",
				strCreOprid = "", strCreDt = "", strCreName = "";

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		if (jacksonUtil.containsKey("znxQfId")) {
			strznxQfId = jacksonUtil.getString("znxQfId");
			if (strznxQfId == null) {
				strznxQfId = "";
			}
		}

		String sql = "SELECT TZ_MLSM_QFPC_DESC,TZ_EMAIL_SENDER,TZ_SEND_MODEL,TZ_TMPL_ID,TZ_MAL_SUBJUECT,TZ_MAL_CONTENT,TZ_MAIL_CC,TZ_TSFS_FLAG,TZ_TSFS_ADDR, TZ_EDM_FLAG,TZ_QXDY_FLAG,TZ_QYPFCL_FLAG,TZ_XSFSSL,TZ_DSFS_FLAG, date_format(TZ_DSFS_DATE,'%Y-%m-%d') TZ_DSFS_DATE,  date_format(TZ_DSFS_TIME,'%H:%i') TZ_DSFS_TIME, TZ_QZTS_FLAG,OPRID, date_format(ROW_ADDED_DTTM,'%Y-%m-%d %H:%i:%s') ROW_ADDED_DTTM FROM PS_TZ_DXYJQF_DY_T WHERE TZ_MLSM_QFPC_ID=?";
		Map<String, Object> dyMap = jdbcTemplate.queryForMap(sql, new Object[] { strznxQfId });
		if (dyMap != null) {
			strznxQfDesc = (String) dyMap.get("TZ_MLSM_QFPC_DESC") == null ? ""
					: (String) dyMap.get("TZ_MLSM_QFPC_DESC");
			strznxQfTmpId = (String) dyMap.get("TZ_TMPL_ID") == null ? "" : (String) dyMap.get("TZ_TMPL_ID");
			strznxQfSubj = (String) dyMap.get("TZ_MAL_SUBJUECT") == null ? "" : (String) dyMap.get("TZ_MAL_SUBJUECT");
			strznxQfCont = (String) dyMap.get("TZ_MAL_CONTENT") == null ? "" : (String) dyMap.get("TZ_MAL_CONTENT");
			strCreOprid = (String) dyMap.get("OPRID") == null ? "" : (String) dyMap.get("OPRID");
			strCreDt = (String) dyMap.get("ROW_ADDED_DTTM") == null ? "" : (String) dyMap.get("ROW_ADDED_DTTM");

			strCreName = jdbcTemplate.queryForObject("SELECT TZ_REALNAME FROM  PS_TZ_AQ_YHXX_TBL WHERE OPRID = ?",
					new Object[] { strCreOprid }, "String");

		} else {
			strCreName = jdbcTemplate.queryForObject("SELECT TZ_REALNAME FROM  PS_TZ_AQ_YHXX_TBL WHERE OPRID = ?",
					new Object[] { oprid }, "String");

		}

		// 收件人;
		String strReceverTmp = "";
		// 邮件群发听众;
		String strSql = "SELECT TZ_AUDIENCE_ID FROM PS_TZ_DXYJQAUD_T WHERE TZ_MLSM_QFPC_ID=?";
		List<Map<String, Object>> tzList = jdbcTemplate.queryForList(strSql, new Object[] { strznxQfId });
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
		List<Map<String, Object>> sjrList = jdbcTemplate.queryForList(strSql, new Object[] { strznxQfId });
		if (sjrList != null && sjrList.size() > 0) {
			for (int i = 0; i < sjrList.size(); i++) {
				strReceverTmp = (String) sjrList.get(i).get("TZ_EMAIL");
				if (strReceverTmp != null && !"".equals(strReceverTmp)) {
					arrrecever.add(strReceverTmp);
				}
			}
		}

		String strRwzxZt = jdbcTemplate.queryForObject(
				"SELECT TZ_RWZX_ZT FROM PS_TZ_DXYJFSRW_TBL WHERE TZ_MLSM_QFPC_ID=? ORDER BY  CAST(TZ_EML_SMS_TASK_ID as SIGNED) DESC limit 0,1",
				new Object[] { strznxQfId }, "String");
		if (strRwzxZt == null) {
			strRwzxZt = "";
		}

		map.replace("znxQfId", strznxQfId);
		map.replace("znxQfDesc", strznxQfDesc);
		map.replace("crePer", strCreName);
		map.replace("recever", arrrecever);
		map.replace("znxTmpId", strznxQfTmpId);
		map.replace("znxSubj", strznxQfSubj);
		map.replace("znxCont", strznxQfCont);
		map.replace("creDt", strCreDt);
		map.replace("rwzxZt", strRwzxZt);

		return jacksonUtil.Map2json(map);
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
		// 得到站内信模板信息
		if ("getZnxTmpInfo".equals(OperateType)) {
			bl = true;
			strResultConten = this.getZnxTmplInfo(comParams, errorMsg);
		}
		// 得到站内信模板信息项
		if ("getZnxTmpItem".equals(OperateType)) {
			strResultConten = this.getZnxTmplItem(comParams, errorMsg);
			bl = true;
		}

		// 保存
		if ("save".equals(OperateType)) {
			strResultConten = this.saveZnxInfo(comParams, errorMsg);
			bl = true;
		}

		// 发送
		if ("sendZnx".equals(OperateType)) {
			strResultConten = this.sendZnx(comParams, errorMsg);
			bl = true;
		}

		if ("revoke".equals(OperateType)) {
			strResultConten = this.revoke(comParams, errorMsg);
			bl = true;
		}

		// 发送状态;
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

	/* 站内信收件人 */
	private String queryReceverList(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);

		JacksonUtil jacksonUtil = new JacksonUtil();
		
		jacksonUtil.json2Map(comParams);
		 
		String znxQfId = ""; 
		if (jacksonUtil.containsKey("znxQfId")) {
			znxQfId = jacksonUtil.getString("znxQfId"); 
		} else { 
			return jacksonUtil.Map2json(mapRet); 
		}
		

		// 考生;
		String strOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		//String ksSQL = "select OPRID,IF(TZ_REALNAME is NULL,OPRID,TZ_REALNAME) TZ_REALNAME FROM PS_TZ_QFKSXX_VW WHERE TZ_JG_ID=?";
		String ksSQL = " select OPRID,IF(TZ_REALNAME is NULL,OPRID,TZ_REALNAME) TZ_REALNAME FROM PS_TZ_QFKSXX_VW A ,PS_TZ_DXYJQFSJR_T B WHERE A.TZ_JG_ID=? AND B.TZ_MLSM_QFPC_ID=? AND A.OPRID=B.TZ_EMAIL";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(ksSQL, new Object[] { strOrgId,znxQfId });
		if (list != null && list.size() > 0) {
			int i = 0;
			String strID, strDesc;
			for (i = 0; i < list.size(); i++) {
				strID = (String) list.get(i).get("OPRID");
				strDesc = (String) list.get(i).get("TZ_REALNAME");
				if (strID != null && !"".equals(strID) && strDesc != null && !"".equals(strDesc)) {
					Map<String, Object> map = new HashMap<>();
					map.put("id", strID);
					map.put("desc", strDesc);
					listData.add(map);
				}
			}
		}

		// 听众;
		//String strSql = "SELECT TZ_AUD_ID,TZ_AUD_NAM FROM PS_TZ_AUDIENCE_VW WHERE TZ_JG_ID=?";
		String strSql = "SELECT A.TZ_AUD_ID,A.TZ_AUD_NAM FROM PS_TZ_AUDIENCE_VW A,PS_TZ_DXYJQAUD_T B WHERE A.TZ_JG_ID=? AND B.TZ_MLSM_QFPC_ID = ? AND A.TZ_AUD_ID=B.TZ_AUDIENCE_ID";
		List<Map<String, Object>> audlist = jdbcTemplate.queryForList(strSql, new Object[] { strOrgId,znxQfId });
		if (audlist != null && audlist.size() > 0) {
			int i = 0;
			String strID, strDesc;
			for (i = 0; i < audlist.size(); i++) {
				strID = (String) audlist.get(i).get("TZ_AUD_ID");
				strDesc = (String) audlist.get(i).get("TZ_AUD_NAM");
				if (strID != null && !"".equals(strID) && strDesc != null && !"".equals(strDesc)) {
					Map<String, Object> map = new HashMap<>();
					map.put("id", strID);
					map.put("desc", strDesc);
					listData.add(map);
				}
			}

		}

		mapRet.replace("root", listData);

		return jacksonUtil.Map2json(mapRet);

	}

	/* 查询站内信模版 */
	private String queryZnxTmplList(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);

		JacksonUtil jacksonUtil = new JacksonUtil();

		// 当前登录人的机构;
		String strOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"SELECT TZ_TMPL_ID,TZ_TMPL_NAME FROM PS_TZ_ZNXTMPL_TBL WHERE TZ_JG_ID=? AND TZ_USE_FLAG='Y'",
				new Object[] { strOrgId });
		if (list != null && list.size() > 0) {
			int i = 0;
			String strZnxTmpl, strDesc;
			for (i = 0; i < list.size(); i++) {
				strZnxTmpl = (String) list.get(i).get("TZ_TMPL_ID");
				strDesc = (String) list.get(i).get("TZ_TMPL_NAME") == null ? ""
						: (String) list.get(i).get("TZ_TMPL_NAME");

				Map<String, Object> map = new HashMap<>();
				map.put("znxtmpl", strZnxTmpl);
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

		String strznxQfId = ""; /* 群发任务id */
		if (jacksonUtil.containsKey("znxQfId")) {
			strznxQfId = jacksonUtil.getString("znxQfId");
		} else {
			return jacksonUtil.Map2json(mapRet);
		}

		// 查询附件;
		String strSql = "SELECT TZ_FJIAN_ID,TZ_FJIAN_MC,TZ_FILE_PATH FROM PS_TZ_YJQFFJXX_TBL WHERE TZ_MLSM_QFPC_ID=? ORDER BY CAST(TZ_FJIAN_ID as SIGNED) DESC";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(strSql, new Object[] { strznxQfId });
		if (list != null && list.size() > 0) {
			int i = 0;
			String strAttaID, strAttaName, strAttaURl;
			for (i = 0; i < list.size(); i++) {
				strAttaID = (String) list.get(i).get("TZ_FJIAN_ID");
				strAttaName = (String) list.get(i).get("TZ_FJIAN_MC") == null ? ""
						: (String) list.get(i).get("TZ_FJIAN_MC");
				strAttaURl = (String) list.get(i).get("TZ_FILE_PATH") == null ? ""
						: (String) list.get(i).get("TZ_FILE_PATH");
				if (!"".equals(strAttaURl)) {
					strAttaURl = request.getContextPath() + strAttaURl;
				}

				Map<String, Object> map = new HashMap<>();
				map.put("znxQfId", strznxQfId);
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
		String znxQfId = String.valueOf(getSeqNum.getSeqNum("TZ_DXYJQF_DY_T", "TZ_MLSM_QFPC_ID"));

		map.put("znxQfId", znxQfId);
		map.put("crePer", "");

		JacksonUtil jacksonUtil = new JacksonUtil();
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String crePer = jdbcTemplate.queryForObject(
				"select a.TZ_REALNAME from (select * from  PS_TZ_AQ_YHXX_TBL  where OPRID=?) a left join PS_TZ_LXFSINFO_TBL b ON  a.TZ_RYLX  = b.TZ_LXFS_LY AND a.OPRID=b.TZ_LYDX_ID limit 0,1",
				new Object[] { oprid }, "String");
		if (crePer == null) {
			crePer = "";
		}

		map.replace("crePer", crePer);

		return jacksonUtil.Map2json(map);
	}

	// 查询邮件模版信息
	private String getZnxTmplInfo(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> map = new HashMap<>();
		map.put("znxSubj", "");
		map.put("znxCont", "");

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		if (jacksonUtil.containsKey("znxTmpId")) {
			String tmpId = jacksonUtil.getString("znxTmpId");
			String strOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			Map<String, Object> znxMap = jdbcTemplate.queryForMap(
					"select TZ_ZNX_SUBJUECT,TZ_ZNX_CONTENT from PS_TZ_ZNXTMPL_TBL where TZ_JG_ID=? and TZ_TMPL_ID=?",
					new Object[] { strOrgId, tmpId });
			if (znxMap != null) {
				String znxSubj = znxMap.get("TZ_ZNX_SUBJUECT") == null ? "" : (String) znxMap.get("TZ_ZNX_SUBJUECT");
				String znxCont = znxMap.get("TZ_ZNX_CONTENT") == null ? "" : (String) znxMap.get("TZ_ZNX_CONTENT");
				map.replace("znxSubj", znxSubj);
				map.replace("znxCont", znxCont);
			}

		}
		return jacksonUtil.Map2json(map);
	}

	// 功能说明：查询邮件模版信息项
	private String getZnxTmplItem(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> map = new HashMap<>();
		int numItem = 0;
		map.put("total", numItem);
		ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
		map.put("root", arrayList);

		String strOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		if (jacksonUtil.containsKey("znxTmpId")) {
			String tmpId = jacksonUtil.getString("znxTmpId");
			if (tmpId != null && !"".equals(tmpId)) {
				Map<String, Object> map1 = jdbcTemplate.queryForMap(
						"SELECT A.TZ_YMB_ID,B.TZ_YMB_CSLBM FROM PS_TZ_ZNXTMPL_TBL A,PS_TZ_TMP_DEFN_TBL B WHERE A.TZ_JG_ID=? AND A.TZ_TMPL_ID=? AND A.TZ_YMB_ID=B.TZ_YMB_ID",
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

		map.replace("total", numItem);
		map.replace("root", arrayList);
		return jacksonUtil.Map2json(map);
	}

	// 功能说明：保存
	private String saveZnxInfo(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> map = new HashMap<>();
		map.put("znxQfId", "");
		map.put("creDt", "");

		// 收件人s;
		String[] arrrecever;
		// 附件id，附件名称，存放地址，访问路径;
		String strattaID = "", strattaName = "", strattaAccUrl = "", path = "";

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

		String strznxQfId = "", strznxQfDesc = "", strznxQfTmpId = "", strznxQfSubj = "", strznxQfCont = "",
				strrecever = "";
		if (formDataJson != null) {
			strznxQfId = (String) formDataJson.get("znxQfId");
			strznxQfDesc = (String) formDataJson.get("znxQfDesc");
			if (formDataJson.containsKey("znxTmpId")) {
				strznxQfTmpId = (String) formDataJson.get("znxTmpId");
			}

			strznxQfSubj = (String) formDataJson.get("znxSubj");
			strznxQfCont = (String) formDataJson.get("znxCont");

			// strreceverOrigin = (String) formDataJson.get("receverOrigin");

			if (othInfoJson != null) {
				strrecever = (String) othInfoJson.get("recever");
			}

			if (strrecever != null && !"".equals(strrecever)) {
				strrecever = strrecever.replace(";", ",");
				strrecever = strrecever.replace("；", ",");
				strrecever = strrecever.replace("，", ",");
			}
			arrrecever = strrecever.split(",");

			PsTzDxyjQfDyTWithBLOBs psTzDxyjQfDyT = psTzDxyjQfDyTMapper.selectByPrimaryKey(strznxQfId);
			if (psTzDxyjQfDyT != null) {
				// 邮件群发任务;
				psTzDxyjQfDyT.setTzMlsmQfpcDesc(strznxQfDesc);
				psTzDxyjQfDyT.setTzTmplId(strznxQfTmpId);
				psTzDxyjQfDyT.setTzMalSubjuect(strznxQfSubj);
				psTzDxyjQfDyT.setTzMalContent(strznxQfCont);

				psTzDxyjQfDyT.setTzOffSend("N");
				psTzDxyjQfDyT.setRowLastmantDttm(new Date());
				psTzDxyjQfDyT.setRowLastmantOprid(strOprId);
				psTzDxyjQfDyTMapper.updateByPrimaryKeyWithBLOBs(psTzDxyjQfDyT);
			} else {
				// 邮件群发任务;
				psTzDxyjQfDyT = new PsTzDxyjQfDyTWithBLOBs();
				psTzDxyjQfDyT.setTzMlsmQfpcId(strznxQfId);
				psTzDxyjQfDyT.setTzMlsmQfpcDesc(strznxQfDesc);
				psTzDxyjQfDyT.setTzQfType("ZNX");
				psTzDxyjQfDyT.setOprid(strOprId);
				psTzDxyjQfDyT.setTzJgId(strOrgId);
				psTzDxyjQfDyT.setTzTmplId(strznxQfTmpId);
				psTzDxyjQfDyT.setTzMalSubjuect(strznxQfSubj);
				psTzDxyjQfDyT.setTzMalContent(strznxQfCont);
				psTzDxyjQfDyT.setTzOffSend("N");
				psTzDxyjQfDyT.setTzAutoCreate("N");
				psTzDxyjQfDyT.setRowAddedDttm(new Date());
				psTzDxyjQfDyT.setRowAddedOprid(strOprId);
				psTzDxyjQfDyT.setRowLastmantDttm(new Date());
				psTzDxyjQfDyT.setRowLastmantOprid(strOprId);
				psTzDxyjQfDyTMapper.insert(psTzDxyjQfDyT);
			}

			// 收件人;
			// 删除收件人数据;
			jdbcTemplate.update("DELETE from PS_TZ_DXYJQFSJR_T WHERE TZ_MLSM_QFPC_ID=?", new Object[] { strznxQfId });

			// 删除听众;
			jdbcTemplate.update("DELETE from PS_TZ_DXYJQAUD_T WHERE TZ_MLSM_QFPC_ID=?", new Object[] { strznxQfId });

			// 添加收件人;
			for (int i = 0; i < arrrecever.length; i++) {
				String sjrStr = arrrecever[i].trim();
				if (sjrStr != null && !"".equals(sjrStr)) {

					// 查看是不是听众，是听众则插入听众表;
					int count = jdbcTemplate.queryForObject("select COUNT(1) FROM PS_TZ_AUD_DEFN_T WHERE TZ_AUD_ID=?",
							new Object[] { sjrStr }, "Integer");
					if (count == 1) {
						PsTzDxyjqaudTKey psTzDxyjqaudTKey = new PsTzDxyjqaudTKey();
						psTzDxyjqaudTKey.setTzMlsmQfpcId(strznxQfId);
						psTzDxyjqaudTKey.setTzAudienceId(sjrStr);
						psTzDxyjqaudTMapper.insert(psTzDxyjqaudTKey);
					} else {
						PsTzDxYjQfSjrT psTzDxYjQfSjrT = new PsTzDxYjQfSjrT();
						psTzDxYjQfSjrT.setTzMlsmQfpcId(strznxQfId);
						psTzDxYjQfSjrT
								.setTzAudcyId(String.valueOf(getSeqNum.getSeqNum("TZ_AUDCYUAN_T", "TZ_AUDCY_ID")));
						psTzDxYjQfSjrT.setTzEmail(sjrStr);
						psTzDxYjQfSjrTMapper.insert(psTzDxYjQfSjrT);
					}

				}

			}

			// 处理删除的附件;
			if (attaDelLit != null && attaDelLit.size() > 0) {
				for (int i = 0; i < attaDelLit.size(); i++) {
					@SuppressWarnings("unchecked")
					Map<String, Object> delMap = (Map<String, Object>) attaDelLit.get(i);
					strattaID = (String) delMap.get("attaID");
					if (strattaID != null && !"".equals(strattaID)) {
						PsTzYjQfFjXxTblKey key = new PsTzYjQfFjXxTblKey();
						key.setTzMlsmQfpcId(strznxQfId);
						key.setTzFjianId(strattaID);
						psTzYjQfFjXxTblMapper.deleteByPrimaryKey(key);
					}
				}
			}

			// 处理添加的附件;
			if (attaNewList != null && attaNewList.size() > 0) {
				for (int i = 0; i < attaNewList.size(); i++) {
					@SuppressWarnings("unchecked")
					Map<String, Object> delMap = (Map<String, Object>) attaNewList.get(i);
					strattaName = (String) delMap.get("attaName");
					strattaAccUrl = (String) delMap.get("path");
					path = request.getServletContext().getRealPath(strattaAccUrl);
					PsTzYjQfFjXxTbl psTzYjQfFjXxTbl = new PsTzYjQfFjXxTbl();
					strattaID = String.valueOf(getSeqNum.getSeqNum("TZ_YJQFFJXX_TBL", "TZ_FJIAN_ID"));
					psTzYjQfFjXxTbl.setTzMlsmQfpcId(strznxQfId);
					psTzYjQfFjXxTbl.setTzFjianId(strattaID);
					psTzYjQfFjXxTbl.setTzFjianMc(strattaName);
					psTzYjQfFjXxTbl.setTzFjianLj(path);
					psTzYjQfFjXxTbl.setTzFilePath(strattaAccUrl);
					psTzYjQfFjXxTblMapper.insert(psTzYjQfFjXxTbl);
				}
			}
		}

		map.replace("znxQfId", strznxQfId);
		SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		map.replace("creDt", datetimeFormate.format(new Date()));

		return jacksonUtil.Map2json(map);
	}

	private String sendZnx(String comParams, String[] errorMsg) {
		String strComContent = this.saveZnxInfo(comParams, errorMsg);

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		Map<String, Object> formDataJson = jacksonUtil.getMap("formdata");
		// TaskId;
		String strTaskId = "";

		// 邮件群发编号;
		String strznxQfId = (String) formDataJson.get("znxQfId");

		String strOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);

		// 创建任务;
		strTaskId = createQfTaskServiceImpl.createTaskIns(strOrgId, strznxQfId, "ZNX", "A");
		// 创建听众;
		String strAudienceDesc = "站内信群发_" + strznxQfId;
		String strAudID = createQfTaskServiceImpl.createAudience(strTaskId, strOrgId, strAudienceDesc, "ZNX");

		// 添加听众成员;
		ArrayList<String> audPersonidArr = new ArrayList<>();
		// 收件人;
		String oprid = "", realName = "";
		String strSql = "SELECT TZ_AUDCY_ID,TZ_EMAIL FROM PS_TZ_DXYJQFSJR_T WHERE TZ_MLSM_QFPC_ID= ?";
		List<Map<String, Object>> sjrList = jdbcTemplate.queryForList(strSql, new Object[] { strznxQfId });
		if (sjrList != null && sjrList.size() > 0) {
			for (int i = 0; i < sjrList.size(); i++) {
				String strAudIDTemp = (String) sjrList.get(i).get("TZ_AUDCY_ID");
				oprid = (String) sjrList.get(i).get("TZ_EMAIL");
				if (oprid != null && !"".equals(oprid)) {
					if (audPersonidArr.contains(oprid)) {

					} else {
						realName = jdbcTemplate.queryForObject(
								"select TZ_REALNAME from PS_TZ_AQ_YHXX_TBL where OPRID=?", new Object[] { oprid },
								"String");
						createQfTaskServiceImpl.addAudCy(strAudID, strAudIDTemp, realName, "", "", "", "", "", "",
								oprid, "", "", "");
					}
				}

			}
		}

		// 添加听众 ;
		strSql = "SELECT TZ_AUDIENCE_ID FROM PS_TZ_DXYJQAUD_T WHERE TZ_MLSM_QFPC_ID = ?";
		List<Map<String, Object>> audList = jdbcTemplate.queryForList(strSql, new Object[] { strznxQfId });
		if (audList != null && audList.size() > 0) {
			for (int i = 0; i < audList.size(); i++) {
				String audiendeId = (String) audList.get(i).get("TZ_AUDIENCE_ID");
				String sql = "select a.OPRID,c.TZ_REALNAME FROM PS_TZ_AUD_LIST_T a, PS_TZ_AQ_YHXX_TBL c where a.OPRID=c.OPRID and a.TZ_AUD_ID=? and a.TZ_DXZT<>'N'";
				List<Map<String, Object>> oprList = jdbcTemplate.queryForList(sql, new Object[] { audiendeId });

				if (oprList != null && oprList.size() > 0) {
					for (int j = 0; j < oprList.size(); j++) {
						oprid = (String) oprList.get(j).get("OPRID");
						realName = (String) oprList.get(j).get("TZ_REALNAME");
						if (audPersonidArr.contains(oprid)) {

						} else {
							audPersonidArr.add(oprid);
							createQfTaskServiceImpl.addAudCy(strAudID, "", realName, "", "", "", "", "", "", oprid, "",
									"", "");
						}
					}
				}
			}
		}

		PsTzDxYjDsfsT psTzDxYjDsfsT = psTzDxYjDsfsTMapper.selectByPrimaryKey(strznxQfId);
		if (psTzDxYjDsfsT != null) {
			psTzDxYjDsfsT.setTzEmlSmsTaskId(strTaskId);
			psTzDxYjDsfsTMapper.updateByPrimaryKey(psTzDxYjDsfsT);
		}

		// sendSmsOrMalQfServiceImpl.send(strTaskId, "");

		int processInstance = getSeqNum.getSeqNum("PSPRCSRQST", "PROCESSINSTANCE");
		// 当前用户;
		String currentOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		// 生成运行控制ID;
		SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
		String s_dtm = datetimeFormate.format(new Date());
		String runCntlId = "ZNX" + s_dtm + "_" + getSeqNum.getSeqNum("PSPRCSRQST", "RUN_ID");

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

			schdProcessParameters.setPlanExcuteDateTime(new Date());
			schdProcessParameters.setRunControlId(runCntlId);

			// 调度作业
			tmpEngine.schedule(schdProcessParameters);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String strRwzxZt = "";
		strRwzxZt = jdbcTemplate.queryForObject("SELECT TZ_RWZX_ZT FROM PS_TZ_DXYJFSRW_TBL WHERE TZ_EML_SMS_TASK_ID=?",
				new Object[] { strTaskId }, "String");
		strComContent = strComContent.substring(0, strComContent.length() - 1);
		strComContent = strComContent + ",\"rwzxZt\":\"" + strRwzxZt + "\"}";
		return strComContent;
	}

	// 获取任务执行状态;
	private String getRwzxZt(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> map = new HashMap<>();
		map.put("znxQfId", "");
		map.put("rwzxZt", "");

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		if (jacksonUtil.containsKey("znxQfId")) {
			String strznxQfId = jacksonUtil.getString("znxQfId");

			String sql = "SELECT TZ_RWZX_ZT FROM PS_TZ_DXYJFSRW_TBL WHERE TZ_MLSM_QFPC_ID=? ORDER BY CAST(TZ_EML_SMS_TASK_ID as SIGNED) DESC limit 0,1";
			String strRwzxZt = jdbcTemplate.queryForObject(sql, new Object[] { strznxQfId }, "String");
			if (strRwzxZt == null) {
				strRwzxZt = "";
			}
			map.replace("znxQfId", strznxQfId);
			map.replace("rwzxZt", strRwzxZt);
		}

		return jacksonUtil.Map2json(map);
	}

	// 功能说明：中断发送
	private String revoke(String comParams, String[] errorMsg) {
		// 返回值;
		String strComContent = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		if (jacksonUtil.containsKey("znxQfId")) {
			String strznxQfId = jacksonUtil.getString("znxQfId");
			if (strznxQfId != null && !"".equals(strznxQfId)) {
				// 短信邮件群发任务批次定义表;
				PsTzDxyjQfDyTWithBLOBs psTzDxyjQfDyT = psTzDxyjQfDyTMapper.selectByPrimaryKey(strznxQfId);
				if (psTzDxyjQfDyT != null) {
					psTzDxyjQfDyT.setTzOffSend("Y");
					psTzDxyjQfDyTMapper.updateByPrimaryKeySelective(psTzDxyjQfDyT);
				}

				// 短信邮件定时发送表;
				PsTzDxYjDsfsT psTzDxYjDsfsT = psTzDxYjDsfsTMapper.selectByPrimaryKey(strznxQfId);
				if (psTzDxYjDsfsT != null) {
					psTzDxYjDsfsT.setTzSendZt("Y");
					psTzDxYjDsfsTMapper.updateByPrimaryKeySelective(psTzDxYjDsfsT);
				}
			}

		}

		return strComContent;
	}
}
