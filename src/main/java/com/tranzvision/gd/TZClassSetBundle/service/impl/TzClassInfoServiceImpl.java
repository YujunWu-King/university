/**
 * 
 */
package com.tranzvision.gd.TZClassSetBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClassInfTMapper;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClsAdminTMapper;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClsBatchTMapper;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClsBmlcTMapper;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClsDjzlTMapper;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClsMajorTMapper;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClsMorinfTMapper;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClassInfT;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsAdminTKey;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsBatchTKey;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsBmlcTKey;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsBmlcTWithBLOBs;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsDjzlT;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsDjzlTKey;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsMajorT;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsMajorTKey;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsMorinfT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 高端产品-班级管理-班级详情页面，原PS：TZ_GD_BJGL_CLS:TZ_GD_BJXQ_CLS
 * 
 * @author SHIHUA
 * @since 2016-01-28
 */
@Service("com.tranzvision.gd.TZClassSetBundle.service.impl.TzClassInfoServiceImpl")
public class TzClassInfoServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private GetHardCodePoint getHardCodePoint;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private TzClassListServiceImpl tzClassListServiceImpl;

	@Autowired
	private PsTzClassInfTMapper psTzClassInfTMapper;

	@Autowired
	private PsTzClsMajorTMapper psTzClsMajorTMapper;

	@Autowired
	private PsTzClsAdminTMapper psTzClsAdminTMapper;

	@Autowired
	private PsTzClsMorinfTMapper psTzClsMorinfTMapper;

	@Autowired
	private PsTzClsBmlcTMapper psTzClsBmlcTMapper;

	@Autowired
	private PsTzClsDjzlTMapper psTzClsDjzlTMapper;

	@Autowired
	private PsTzClsBatchTMapper psTzClsBatchTMapper;

	/**
	 * 获得班级列表（班级编号、班级名称、所属项目、项目类别）
	 */
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		String strRet = "{}";

		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strParams);
			String queryID = jacksonUtil.getString("queryID");

			if (null != queryID && !"".equals(queryID)) {
				switch (queryID) {
				case "2":
					// 专业方向
					strRet = tzClassListServiceImpl.queryFxList(strParams, errorMsg);
					break;
				case "3":
					// 批次管理
					strRet = tzClassListServiceImpl.queryPcList(strParams, errorMsg);
					break;
				case "4":
					// 管理人员
					strRet = tzClassListServiceImpl.queryGlList(strParams, errorMsg);
					break;
				case "5":
					// 报名流程
					strRet = tzClassListServiceImpl.queryBmlcList(strParams, errorMsg);
					break;
				case "6":
					// 递交资料
					strRet = tzClassListServiceImpl.queryZlList(strParams, errorMsg);
					break;
				case "7":
					// 互斥班级
					strRet = tzClassListServiceImpl.queryHCCls(strParams, errorMsg);
					break;
				}
			} else {
				errorMsg[0] = "1";
				errorMsg[1] = "参数不正确！";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return strRet;

	}

	@Override
	@Transactional
	public String tzQuery(String strParams, String[] errMsg) {
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("formData", "{}");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			String str_bj_id = jacksonUtil.getString("bj_id");
			String sql = "";

			if (null != str_bj_id && !"".equals(str_bj_id)) {

				PsTzClassInfT psTzClassInfT = psTzClassInfTMapper.selectByPrimaryKey(str_bj_id);

				String str_xm_id = psTzClassInfT.getTzPrjId();
				String str_xm_desc = "";
				if (null != str_xm_id && !"".equals(str_xm_id)) {
					sql = "select TZ_PRJ_NAME from PS_TZ_PRJ_INF_T where TZ_PRJ_ID=?";
					str_xm_desc = sqlQuery.queryForObject(sql, new Object[] { str_xm_id }, "String");
				}

				String str_bmb_id = psTzClassInfT.getTzAppModalId();
				String str_bmb_desc = "";
				if (null != str_bmb_id && !"".equals(str_bmb_id)) {
					sql = "select TZ_APP_TPL_MC from PS_TZ_APPTPL_DY_T where TZ_APP_TPL_ID=?";
					str_bmb_desc = sqlQuery.queryForObject(sql, new Object[] { str_bmb_id }, "String");
				}

				String str_clps_cj_modal = psTzClassInfT.getTzZlpsScorMdId();
				String str_msps_cj_modal = psTzClassInfT.getTzMscjScorMdId();
				String str_clps_cj_modal_desc = "";
				String str_msps_cj_modal_desc = "";
				sql = "select TZ_MODAL_NAME from PS_TZ_RS_MODAL_TBL where TZ_SCORE_MODAL_ID=?";
				if (null != str_clps_cj_modal && !"".equals(str_clps_cj_modal)) {
					str_clps_cj_modal_desc = sqlQuery.queryForObject(sql, new Object[] { str_clps_cj_modal }, "String");
				}
				if (null != str_msps_cj_modal && !"".equals(str_msps_cj_modal)) {
					str_msps_cj_modal_desc = sqlQuery.queryForObject(sql, new Object[] { str_msps_cj_modal }, "String");
				}

				String str_psbmb_id = psTzClassInfT.getTzPsAppModalId();
				String str_psbmb_desc = "";
				if (null != str_psbmb_id && !"".equals(str_psbmb_id)) {
					sql = "select TZ_APP_TPL_MC from PS_TZ_APPTPL_DY_T where TZ_APP_TPL_ID=?";
					str_psbmb_desc = sqlQuery.queryForObject(sql, new Object[] { str_psbmb_id }, "String");
				}

				String str_bj_xs = psTzClassInfT.getTzIsSubBatch();
				if ("Y".equals(str_bj_xs)) {
					str_bj_xs = "true";
				} else {
					str_bj_xs = "false";
				}

				/* 允许匿名访问-Begin */
				String str_guest_apply = psTzClassInfT.getTzGuestApply();
				String guest_apply_url = "";
				if ("Y".equals(str_guest_apply)) {
					str_guest_apply = "true";
					// 生成链接
					String str_com_page_id = getHardCodePoint.getHardCodePointVal("TZ_GD_GUEST_APPLY");
					String[] aryComPage = str_com_page_id.split("\\+");
					if (null != aryComPage && aryComPage.length == 2) {
						String str_com_id = aryComPage[0].trim();
						String str_page_id = aryComPage[1].trim();

						sql = "select TZ_PAGE_REFCODE from PS_TZ_AQ_PAGZC_TBL where TZ_COM_ID=? and TZ_PAGE_ID=?";
						String classId = sqlQuery.queryForObject(sql, new Object[] { str_com_id, str_page_id },
								"String");

						guest_apply_url = request.getContextPath() + "/dispacher?classid=" + classId + "&TZ_CLASS_ID="
								+ str_bj_id;
						guest_apply_url = "&nbsp;<span style=\"font-size:12px;color:#FF0000\">" + guest_apply_url
								+ "</sapn>";
					}
				} else {
					str_guest_apply = "false";
				}
				/* 允许匿名访问-End */

				String str_bj_name = psTzClassInfT.getTzClassName();
				String str_zxbm = psTzClassInfT.getTzIsAppOpen();
				String str_bj_desc = psTzClassInfT.getTzClassDesc();

				String strDateFormat = getSysHardCodeVal.getDateFormat();
				String strTimeFormat = getSysHardCodeVal.getTimeHMFormat();
				SimpleDateFormat dateFormat = new SimpleDateFormat(strDateFormat);
				SimpleDateFormat timeFormat = new SimpleDateFormat(strTimeFormat);

				String str_st_dt = "";
				if (psTzClassInfT.getTzStartDt() != null) {
					str_st_dt = dateFormat.format(psTzClassInfT.getTzStartDt());
				}

				String str_end_dt = "";
				if (psTzClassInfT.getTzEndDt() != null) {
					str_end_dt = dateFormat.format(psTzClassInfT.getTzEndDt());
				}

				String str_bmst_dt = "";
				if (psTzClassInfT.getTzAppStartDt() != null) {
					str_bmst_dt = dateFormat.format(psTzClassInfT.getTzAppStartDt());
				}
				
				String str_bmst_tm = null;
				if(psTzClassInfT.getTzAppStartTm() != null){
					str_bmst_tm = timeFormat.format(psTzClassInfT.getTzAppStartTm());
				}

				String str_bmend_dt = "";
				if (psTzClassInfT.getTzAppEndDt() != null) {
					str_bmend_dt = dateFormat.format(psTzClassInfT.getTzAppEndDt());
				}
				
				String str_bmend_tm = null;
				if(psTzClassInfT.getTzAppEndTm() != null){
					str_bmend_tm = timeFormat.format(psTzClassInfT.getTzAppEndTm());
				}

				Map<String, String> mapJson = new HashMap<String, String>();
				mapJson.put("bj_id", str_bj_id);
				mapJson.put("bj_name", str_bj_name);
				mapJson.put("xm_id", str_xm_id);
				mapJson.put("bm_kt", str_zxbm);
				mapJson.put("begin_time", str_st_dt);
				mapJson.put("end_time", str_end_dt);
				mapJson.put("beginBm_time", str_bmst_dt);
				mapJson.put("beginBm_tm", str_bmst_tm);
				mapJson.put("endBm_time", str_bmend_dt);
				mapJson.put("endBm_tm", str_bmend_tm);
				mapJson.put("bmb_mb", str_bmb_id);
				mapJson.put("bj_desc", str_bj_desc);
				mapJson.put("bj_xs", str_bj_xs);
				mapJson.put("xm_id_desc", str_xm_desc);
				mapJson.put("bmb_mb_desc", str_bmb_desc);
				mapJson.put("clps_cj_modal", str_clps_cj_modal);
				mapJson.put("clps_cj_modal_desc", str_clps_cj_modal_desc);
				mapJson.put("msps_cj_modal", str_msps_cj_modal);
				mapJson.put("msps_cj_modal_desc", str_msps_cj_modal_desc);
				mapJson.put("psbmb_mb", str_psbmb_id);
				mapJson.put("psbmb_mb_desc", str_psbmb_desc);
				mapJson.put("guest_apply", str_guest_apply);
				mapJson.put("guest_apply_url", guest_apply_url);

				mapRet.replace("formData", mapJson);

			} else {
				errMsg[0] = "1";
				errMsg[1] = "参数错误！";
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "";
		}

		return jacksonUtil.Map2json(mapRet);
	}

	/**
	 * 新增班级的基本信息
	 */
	@Override
	@Transactional
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		String conflictKeys = "";
		String comma = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String tzJgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String str_bj_name = jacksonUtil.getString("bj_name");

				String sql = "select 'Y' from PS_TZ_CLASS_INF_T where TZ_JG_ID=? and TZ_CLASS_NAME=upper(?)";
				String recExists = sqlQuery.queryForObject(sql, new Object[] { tzJgId, str_bj_name }, "String");
				if ("Y".equals(recExists)) {
					conflictKeys += comma + str_bj_name;
					comma = ",";
				} else {

					// 班级编号
					String tzClassId = String.valueOf(getSeqNum.getSeqNum("TZ_CLASS_INF_T", "TZ_CLASS_ID"));

					String strDateFormat = getSysHardCodeVal.getDateFormat();
					String strTimeFormat = getSysHardCodeVal.getTimeHMFormat();
					SimpleDateFormat dateFormat = new SimpleDateFormat(strDateFormat);
					SimpleDateFormat timeFormat = new SimpleDateFormat(strTimeFormat);

					PsTzClassInfT psTzClassInfT = new PsTzClassInfT();

					psTzClassInfT.setTzClassId(tzClassId);
					psTzClassInfT.setTzJgId(tzJgId);
					psTzClassInfT.setTzPrjId(jacksonUtil.getString("xm_id"));
					psTzClassInfT.setTzClassName(str_bj_name);

					String begin_time = jacksonUtil.getString("begin_time");
					if (null != begin_time && !"".equals(begin_time)) {
						Date tzStartDt = dateFormat.parse(begin_time);
						psTzClassInfT.setTzStartDt(tzStartDt);
					}

					String end_time = jacksonUtil.getString("end_time");
					if (null != end_time && !"".equals(end_time)) {
						Date tzEndDt = dateFormat.parse(end_time);
						psTzClassInfT.setTzEndDt(tzEndDt);
					}

					String beginBm_time = jacksonUtil.getString("beginBm_time");
					if (null != beginBm_time && !"".equals(beginBm_time)) {
						Date tzAppStartDt = dateFormat.parse(beginBm_time);
						psTzClassInfT.setTzAppStartDt(tzAppStartDt);
					}
					String beginBm_tm = jacksonUtil.getString("beginBm_tm");
					if (null != beginBm_tm && !"".equals(beginBm_tm)) {
						Date tzAppStartTm = timeFormat.parse(beginBm_tm);
						psTzClassInfT.setTzAppStartTm(tzAppStartTm);
					}

					String endBm_time = jacksonUtil.getString("endBm_time");
					if (null != endBm_time && !"".equals(endBm_time)) {
						Date tzAppEndDt = dateFormat.parse(endBm_time);
						psTzClassInfT.setTzAppEndDt(tzAppEndDt);
					}
					String endBm_tm = jacksonUtil.getString("endBm_tm");
					if (null != endBm_tm && !"".equals(endBm_tm)) {
						Date tzAppEndTm = timeFormat.parse(endBm_tm);
						psTzClassInfT.setTzAppEndTm(tzAppEndTm);
					}

					String bj_xs = jacksonUtil.getString("bj_xs");
					if ("false".equals(bj_xs)) {
						bj_xs = "N";
					} else {
						bj_xs = "Y";
					}
					psTzClassInfT.setTzIsSubBatch(bj_xs);

					psTzClassInfT.setTzIsAppOpen(jacksonUtil.getString("bm_kt"));
					psTzClassInfT.setTzAppModalId(jacksonUtil.getString("bmb_mb"));
					psTzClassInfT.setTzZlpsScorMdId(jacksonUtil.getString("clps_cj_modal"));
					psTzClassInfT.setTzMscjScorMdId(jacksonUtil.getString("msps_cj_modal"));
					psTzClassInfT.setTzClassDesc(jacksonUtil.getString("bj_desc"));

					psTzClassInfT.setRowAddedDttm(dateNow);
					psTzClassInfT.setRowAddedOprid(oprid);
					psTzClassInfT.setRowLastmantDttm(dateNow);
					psTzClassInfT.setRowLastmantOprid(oprid);

					int rst = psTzClassInfTMapper.insert(psTzClassInfT);

					if (rst == 1) {
						Map<String, Object> mapJson = new HashMap<String, Object>();
						mapJson.put("bj_id", tzClassId);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "保存数据发生错误。";
					}
				}

			}
			if (!"".equals(conflictKeys)) {
				errMsg[0] = "1";
				errMsg[1] = "班级名称：" + conflictKeys + "在当前机构已经存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		String str_bj_id = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			String str_y = "";

			String sql = "";

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {

				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String typeFlag = jacksonUtil.getString("typeFlag");

				if ("bj_jbxx".equals(typeFlag)) {

					Map<String, Object> mapData = jacksonUtil.getMap("data");
					String str_bjid = mapData.get("bj_id") == null ? "" : String.valueOf(mapData.get("bj_id"));
					String str_bjname = mapData.get("bj_name") == null ? "" : String.valueOf(mapData.get("bj_name"));

					sql = "select 'Y' from PS_TZ_CLASS_INF_T where TZ_JG_ID=? and TZ_CLASS_NAME=UPPER(?) and TZ_CLASS_ID<>?";

					str_y = sqlQuery.queryForObject(sql, new Object[] { orgid, str_bjname, str_bjid }, "String");

					break;
				}

			}

			if (!"Y".equals(str_y)) {

				for (int num = 0; num < dataLength; num++) {

					// 表单内容
					String strForm = actData[num];
					// 解析json
					jacksonUtil.json2Map(strForm);

					String typeFlag = jacksonUtil.getString("typeFlag");
					String bj = jacksonUtil.getString("bj_id");
					str_bj_id = "";

					Map<String, Object> mapData = jacksonUtil.getMap("data");

					switch (typeFlag) {
					case "bj_jbxx":
						// 班级基本信息
						str_bj_id = this.tzUpdate_bjxx(mapData);
						break;

					case "glry":
						// 管理人员
						str_bj_id = this.tzUpdate_glry(mapData);
						break;

					case "zyfx":
						// 专业方向
						str_bj_id = this.tzUpdate_zyfx(mapData);
						break;

					case "bmlc":
						// 报名流程
						str_bj_id = this.tzUpdate_bmlc(mapData);
						break;

					case "djzl":
						// 递交资料
						str_bj_id = this.tzUpdate_djzl(mapData);
						break;

					case "gdxx":
						// 更多信息
						str_bj_id = this.tzUpdate_gdxx(mapData, bj);
						break;
					}

					if ("".equals(str_bj_id)) {
						errMsg[0] = "1";
						errMsg[1] = "更新班级信息失败！请检查后重试。";
					}

				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "班级名称在当前机构已经存在！";
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "系统异常：" + e.getMessage();
		}

		mapRet.put("bj_id", str_bj_id);
		mapRet.put("siteId", "1");
		mapRet.put("coluId", "");

		return strRet;
	}

	// 更新班级的基本信息
	public String tzUpdate_bjxx(Map<String, Object> mapData) {

		String str_bj_id = "";

		Date dateNow = new Date();
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		try {
			str_bj_id = mapData.get("bj_id") == null ? "" : String.valueOf(mapData.get("bj_id"));

			if (!"".equals(str_bj_id)) {

				String sql = "select 'Y' from PS_TZ_CLASS_INF_T where TZ_CLASS_ID=?";
				String recExists = sqlQuery.queryForObject(sql, new Object[] { str_bj_id }, "String");

				if ("Y".equals(recExists)) {
					String strDateFormat = getSysHardCodeVal.getDateFormat();
					String strTimeFormat = getSysHardCodeVal.getTimeHMFormat();
					
					SimpleDateFormat dateFormat = new SimpleDateFormat(strDateFormat);
					SimpleDateFormat timeFormat = new SimpleDateFormat(strTimeFormat);

					PsTzClassInfT psTzClassInfT = psTzClassInfTMapper.selectByPrimaryKey(str_bj_id);
					//psTzClassInfT.setTzClassId(str_bj_id);
					psTzClassInfT.setTzClassName(
							mapData.get("bj_name") == null ? "" : String.valueOf(mapData.get("bj_name")));
					psTzClassInfT.setTzPrjId(mapData.get("xm_id") == null ? "" : String.valueOf(mapData.get("xm_id")));

					String strStartDt = String.valueOf(mapData.get("begin_time"));
					if (!"".equals(strStartDt) && strStartDt != null) {
						Date tzStartDt = dateFormat.parse(strStartDt);
						psTzClassInfT.setTzStartDt(tzStartDt);
					}else{
						psTzClassInfT.setTzStartDt(null);
					}

					String strEndDt = String.valueOf(mapData.get("end_time"));
					if (!"".equals(strEndDt) && strEndDt != null) {
						Date tzEndDt = dateFormat.parse(strEndDt);
						psTzClassInfT.setTzEndDt(tzEndDt);
					}else{
						psTzClassInfT.setTzEndDt(null);
					}
					
					String strAppStartDt = String.valueOf(mapData.get("beginBm_time"));
					if (!"".equals(strAppStartDt) && strAppStartDt != null) {
						Date tzAppStartDt = dateFormat.parse(strAppStartDt);
						psTzClassInfT.setTzAppStartDt(tzAppStartDt);
					}else{
						psTzClassInfT.setTzAppStartDt(null);
					}
					
					String strAppStartTm = String.valueOf(mapData.get("beginBm_tm"));
					if (!"".equals(strAppStartTm) && mapData.get("beginBm_tm") != null) {
						Date tzAppStartTm = timeFormat.parse(strAppStartTm);
						psTzClassInfT.setTzAppStartTm(tzAppStartTm);
					}else{
						psTzClassInfT.setTzAppStartTm(null);
					}

					String strAppEndDt = String.valueOf(mapData.get("endBm_time"));
					if (!"".equals(strAppEndDt) && strAppEndDt != null) {
						Date tzAppEndDt = dateFormat.parse(strAppEndDt);
						psTzClassInfT.setTzAppEndDt(tzAppEndDt);
					}else{
						psTzClassInfT.setTzAppEndDt(null);
					}
					
					String strAppEndTm = String.valueOf(mapData.get("endBm_tm"));
					if (!"".equals(strAppEndTm) && mapData.get("endBm_tm") != null) {
						Date tzAppEndTm = timeFormat.parse(strAppEndTm);
						psTzClassInfT.setTzAppEndTm(tzAppEndTm);
					}else{
						psTzClassInfT.setTzAppEndTm(null);
					}

					psTzClassInfT
							.setTzIsAppOpen(mapData.get("bm_kt") == null ? "" : String.valueOf(mapData.get("bm_kt")));
					psTzClassInfT.setTzAppModalId(
							mapData.get("bmb_mb") == null ? "" : String.valueOf(mapData.get("bmb_mb")));
					psTzClassInfT.setTzZlpsScorMdId(
							mapData.get("clps_cj_modal") == null ? "" : String.valueOf(mapData.get("clps_cj_modal")));
					psTzClassInfT.setTzMscjScorMdId(
							mapData.get("msps_cj_modal") == null ? "" : String.valueOf(mapData.get("msps_cj_modal")));
					psTzClassInfT.setTzPsAppModalId(
							mapData.get("psbmb_mb") == null ? "" : String.valueOf(mapData.get("psbmb_mb")));

					String str_xs = mapData.get("bj_xs") == null ? "" : String.valueOf(mapData.get("bj_xs"));
					if ("true".equals(str_xs)) {
						str_xs = "Y";
					} else {
						str_xs = "N";
					}
					psTzClassInfT.setTzIsSubBatch(str_xs);

					psTzClassInfT.setTzClassDesc(
							mapData.get("bj_desc") == null ? "" : String.valueOf(mapData.get("bj_desc")));

					String str_guest_apply = mapData.get("guest_apply") == null ? ""
							: String.valueOf(mapData.get("guest_apply"));
					if ("true".equals(str_guest_apply) || "on".equals(str_guest_apply)) {
						str_guest_apply = "Y";
					} else {
						str_guest_apply = "N";
					}
					psTzClassInfT.setTzGuestApply(str_guest_apply);

					psTzClassInfT.setRowLastmantDttm(dateNow);
					psTzClassInfT.setRowLastmantOprid(oprid);

					psTzClassInfTMapper.updateByPrimaryKeyWithBLOBs(psTzClassInfT);

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			str_bj_id = "";
		}

		return str_bj_id;
	}

	// 更新专业方向
	public String tzUpdate_zyfx(Map<String, Object> mapData) {

		String str_bj_id = "";

		Date dateNow = new Date();
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		try {
			str_bj_id = mapData.get("bj_id") == null ? "" : String.valueOf(mapData.get("bj_id"));
			String str_fx_id = mapData.get("fx_id") == null ? "" : String.valueOf(mapData.get("fx_id"));

			if ("".equals(str_fx_id)) {
				str_fx_id = String.valueOf(getSeqNum.getSeqNum("TZ_CLS_MAJOR_T", "TZ_MAJOR_ID"));
			}

			String sql = "select 'Y' from PS_TZ_CLS_MAJOR_T where TZ_CLASS_ID=? and TZ_MAJOR_ID=?";
			String recExists = sqlQuery.queryForObject(sql, new Object[] { str_bj_id, str_fx_id }, "String");

			PsTzClsMajorT psTzClsMajorT = new PsTzClsMajorT();
			psTzClsMajorT.setTzClassId(str_bj_id);
			psTzClsMajorT.setTzMajorId(str_fx_id);
			psTzClsMajorT.setTzSortNum(
					Integer.parseInt(mapData.get("fx_xh") == null ? "0" : String.valueOf(mapData.get("fx_xh"))));
			psTzClsMajorT.setTzMajorName(mapData.get("fx_name") == null ? "" : String.valueOf(mapData.get("fx_name")));
			psTzClsMajorT.setRowLastmantDttm(dateNow);
			psTzClsMajorT.setRowLastmantOprid(oprid);

			if ("Y".equals(recExists)) {
				psTzClsMajorTMapper.updateByPrimaryKeySelective(psTzClsMajorT);
			} else {
				psTzClsMajorT.setRowAddedDttm(dateNow);
				psTzClsMajorT.setRowAddedOprid(oprid);
				psTzClsMajorTMapper.insertSelective(psTzClsMajorT);
			}

		} catch (Exception e) {
			e.printStackTrace();
			str_bj_id = "";
		}

		return str_bj_id;
	}

	// 更新管理人员
	public String tzUpdate_glry(Map<String, Object> mapData) {
		String str_bj_id = "";

		try {

			str_bj_id = mapData.get("bj_id") == null ? "" : String.valueOf(mapData.get("bj_id"));
			String oprid = mapData.get("ry_id") == null ? "" : String.valueOf(mapData.get("ry_id"));

			String sql = "delete from PS_TZ_CLS_ADMIN_T where TZ_CLASS_ID=? and OPRID=?";
			sqlQuery.update(sql, new Object[] { str_bj_id, oprid });

			PsTzClsAdminTKey psTzClsAdminTKey = new PsTzClsAdminTKey();
			psTzClsAdminTKey.setTzClassId(str_bj_id);
			psTzClsAdminTKey.setOprid(oprid);

			psTzClsAdminTMapper.insert(psTzClsAdminTKey);

		} catch (Exception e) {
			e.printStackTrace();
			str_bj_id = "";
		}

		return str_bj_id;
	}

	// 更新更多信息
	public String tzUpdate_gdxx(Map<String, Object> mapData, String str_bj_id) {

		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

		try {

			String sql = "select TZ_ATTRIBUTE_ID from PS_TZ_CLS_ATTR_T where TZ_JG_ID=? and TZ_IS_USED='Y'";

			List<Map<String, Object>> listData = sqlQuery.queryForList(sql, new Object[] { orgid });

			for (Map<String, Object> mapInfo : listData) {
				String str_zd_id = mapInfo.get("TZ_ATTRIBUTE_ID") == null ? ""
						: String.valueOf(mapInfo.get("TZ_ATTRIBUTE_ID"));
				String strZd = mapData.get(str_zd_id) == null ? "" : String.valueOf(mapData.get(str_zd_id));
				if (!"".equals(strZd)) {
					PsTzClsMorinfT psTzClsMorinfT = new PsTzClsMorinfT();
					psTzClsMorinfT.setTzClassId(str_bj_id);
					psTzClsMorinfT.setTzAttributeId(str_zd_id);
					psTzClsMorinfT.setTzAttributeValue(strZd);

					sql = "select 'Y' from PS_TZ_CLS_MORINF_T where TZ_CLASS_ID=? and TZ_ATTRIBUTE_ID=?";
					String recExists = sqlQuery.queryForObject(sql, new Object[] { str_bj_id, str_zd_id }, "String");

					if ("Y".equals(recExists)) {
						psTzClsMorinfTMapper.updateByPrimaryKey(psTzClsMorinfT);
					} else {
						psTzClsMorinfTMapper.insert(psTzClsMorinfT);
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			str_bj_id = "";
		}

		return str_bj_id;
	}

	// 更新报名流程
	public String tzUpdate_bmlc(Map<String, Object> mapData) {
		String str_bj_id = "";

		try {

			str_bj_id = mapData.get("bj_id") == null ? "" : String.valueOf(mapData.get("bj_id"));
			String str_lc_id = mapData.get("bmlc_id") == null ? "" : String.valueOf(mapData.get("bmlc_id"));

			if ("".equals(str_lc_id)) {
				str_lc_id = String.valueOf(getSeqNum.getSeqNum("TZ_APPPRO_STP_T", "TZ_APPPRO_ID"));
			}

			String sql = "select 'Y' from PS_TZ_CLS_BMLC_T where TZ_CLASS_ID=? and TZ_APPPRO_ID=?";
			String recExists = sqlQuery.queryForObject(sql, new Object[] { str_bj_id, str_lc_id }, "String");

			PsTzClsBmlcTWithBLOBs psTzClsBmlcTWithBLOBs = new PsTzClsBmlcTWithBLOBs();
			psTzClsBmlcTWithBLOBs.setTzClassId(str_bj_id);
			psTzClsBmlcTWithBLOBs.setTzAppproId(str_lc_id);
			psTzClsBmlcTWithBLOBs.setTzSortNum(
					Integer.parseInt(mapData.get("bmlc_xh") == null ? "0" : String.valueOf(mapData.get("bmlc_xh"))));
			psTzClsBmlcTWithBLOBs
					.setTzAppproName(mapData.get("bmlc_name") == null ? "" : String.valueOf(mapData.get("bmlc_name")));
			psTzClsBmlcTWithBLOBs
					.setTzTmpContent(mapData.get("bmlc_desc") == null ? "" : String.valueOf(mapData.get("bmlc_desc")));
			psTzClsBmlcTWithBLOBs.setTzDefContent("");

			if ("Y".equals(recExists)) {
				psTzClsBmlcTMapper.updateByPrimaryKeyWithBLOBs(psTzClsBmlcTWithBLOBs);
			} else {
				psTzClsBmlcTMapper.insert(psTzClsBmlcTWithBLOBs);
			}

		} catch (Exception e) {
			e.printStackTrace();
			str_bj_id = "";
		}

		return str_bj_id;
	}

	// 更新递交资料
	public String tzUpdate_djzl(Map<String, Object> mapData) {
		String str_bj_id = "";

		try {

			str_bj_id = mapData.get("bj_id") == null ? "" : String.valueOf(mapData.get("bj_id"));
			String str_dj_id = mapData.get("djzl_id") == null ? "" : String.valueOf(mapData.get("djzl_id"));
			String str_djzl_name = mapData.get("djzl_name") == null ? "" : String.valueOf(mapData.get("djzl_name"));

			String sql = "select 'Y' from PS_TZ_CLS_DJZL_T where TZ_CLASS_ID=? and TZ_SBMINF_ID<>? and TZ_CONT_INTRO=?";
			String strCkName = sqlQuery.queryForObject(sql, new Object[] { str_bj_id, str_dj_id, str_djzl_name },
					"String");

			if (!"Y".equals(strCkName)) {
				if ("".equals(str_dj_id)) {
					str_dj_id = String.valueOf(getSeqNum.getSeqNum("TZ_SBMINF_STP_T", "TZ_SBMINF_ID"));
				}

				sql = "select 'Y' from PS_TZ_CLS_DJZL_T where TZ_CLASS_ID=? and TZ_SBMINF_ID=?";
				String recExists = sqlQuery.queryForObject(sql, new Object[] { str_bj_id, str_dj_id }, "String");

				PsTzClsDjzlT psTzClsDjzlT = new PsTzClsDjzlT();
				psTzClsDjzlT.setTzClassId(str_bj_id);
				psTzClsDjzlT.setTzSbminfId(str_dj_id);
				psTzClsDjzlT.setTzSortNum(Integer
						.parseInt(mapData.get("djzl_xh") == null ? "0" : String.valueOf(mapData.get("djzl_xh"))));
				psTzClsDjzlT.setTzContIntro(
						mapData.get("djzl_name") == null ? "" : String.valueOf(mapData.get("djzl_name")));
				psTzClsDjzlT.setTzRemark(mapData.get("djzl_bz") == null ? "" : String.valueOf(mapData.get("djzl_bz")));

				if ("Y".equals(recExists)) {
					psTzClsDjzlTMapper.updateByPrimaryKeyWithBLOBs(psTzClsDjzlT);
				} else {
					psTzClsDjzlTMapper.insert(psTzClsDjzlT);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			str_bj_id = "";
		}

		return str_bj_id;
	}

	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {

				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> mapInfo = jacksonUtil.getMap("data");

				int delNum = 0;

				switch (typeFlag) {
				case "zyfx":
					// 专业方向
					delNum = this.delete_Zyfx(mapInfo, errMsg);
					break;
				case "pcgl":
					// 批次管理
					delNum = this.delete_Pcgl(mapInfo, errMsg);
					break;
				case "glry":
					// 管理人员
					delNum = this.delete_Glry(mapInfo, errMsg);
					break;
				case "bmlc":
					// 报名流程
					delNum = this.delete_Bmlc(mapInfo, errMsg);
					break;
				case "djzl":
					// 递交资料
					delNum = this.delete_Djzl(mapInfo, errMsg);
					break;
				}

				if (delNum == 0 && errMsg.length == 0) {
					errMsg[0] = "1";
					errMsg[1] = "删除失败！";
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "删除失败！" + e.getMessage();
		}

		return strRet;
	}

	// 删除专业方向
	public int delete_Zyfx(Map<String, Object> mapInfo, String[] errMsg) {

		String tzClassId = mapInfo.get("bj_id") == null ? "" : String.valueOf(mapInfo.get("bj_id"));
		String tzMajorId = mapInfo.get("fx_id") == null ? "" : String.valueOf(mapInfo.get("fx_id"));

		if ("".equals(tzClassId) || "".equals(tzMajorId)) {
			errMsg[0] = "1";
			errMsg[1] = "删除失败！参数错误！";
			return 0;
		}

		PsTzClsMajorTKey psTzClsMajorTKey = new PsTzClsMajorTKey();
		psTzClsMajorTKey.setTzClassId(tzClassId);
		psTzClsMajorTKey.setTzMajorId(tzMajorId);

		return psTzClsMajorTMapper.deleteByPrimaryKey(psTzClsMajorTKey);
	}

	// 删除批次
	public int delete_Pcgl(Map<String, Object> mapInfo, String[] errMsg) {

		String tzClassId = mapInfo.get("bj_id") == null ? "" : String.valueOf(mapInfo.get("bj_id"));
		String tzBatchId = mapInfo.get("pc_id") == null ? "" : String.valueOf(mapInfo.get("pc_id"));

		if ("".equals(tzClassId) || "".equals(tzBatchId)) {
			errMsg[0] = "1";
			errMsg[1] = "删除失败！参数错误！";
			return 0;
		}

		PsTzClsBatchTKey psTzClsBatchTKey = new PsTzClsBatchTKey();
		psTzClsBatchTKey.setTzClassId(tzClassId);
		psTzClsBatchTKey.setTzBatchId(tzBatchId);

		return psTzClsBatchTMapper.deleteByPrimaryKey(psTzClsBatchTKey);
	}

	// 删除管理人员
	public int delete_Glry(Map<String, Object> mapInfo, String[] errMsg) {

		String tzClassId = mapInfo.get("bj_id") == null ? "" : String.valueOf(mapInfo.get("bj_id"));
		String oprid = mapInfo.get("ry_id") == null ? "" : String.valueOf(mapInfo.get("ry_id"));

		if ("".equals(tzClassId) || "".equals(oprid)) {
			errMsg[0] = "1";
			errMsg[1] = "删除失败！参数错误！";
			return 0;
		}

		PsTzClsAdminTKey psTzClsAdminTKey = new PsTzClsAdminTKey();
		psTzClsAdminTKey.setTzClassId(tzClassId);
		psTzClsAdminTKey.setOprid(oprid);

		return psTzClsAdminTMapper.deleteByPrimaryKey(psTzClsAdminTKey);
	}

	// 删除报名流程
	public int delete_Bmlc(Map<String, Object> mapInfo, String[] errMsg) {

		String tzClassId = mapInfo.get("bj_id") == null ? "" : String.valueOf(mapInfo.get("bj_id"));
		String tzAppproId = mapInfo.get("bmlc_id") == null ? "" : String.valueOf(mapInfo.get("bmlc_id"));

		if ("".equals(tzClassId) || "".equals(tzAppproId)) {
			errMsg[0] = "1";
			errMsg[1] = "删除失败！参数错误！";
			return 0;
		}

		PsTzClsBmlcTKey psTzClsBmlcTKey = new PsTzClsBmlcTKey();
		psTzClsBmlcTKey.setTzClassId(tzClassId);
		psTzClsBmlcTKey.setTzAppproId(tzAppproId);

		return psTzClsBmlcTMapper.deleteByPrimaryKey(psTzClsBmlcTKey);
	}

	// 删除递交资料
	public int delete_Djzl(Map<String, Object> mapInfo, String[] errMsg) {

		String tzClassId = mapInfo.get("bj_id") == null ? "" : String.valueOf(mapInfo.get("bj_id"));
		String tzSbminfId = mapInfo.get("djzl_id") == null ? "" : String.valueOf(mapInfo.get("djzl_id"));

		if ("".equals(tzClassId) || "".equals(tzSbminfId)) {
			errMsg[0] = "1";
			errMsg[1] = "删除失败！参数错误！";
			return 0;
		}

		PsTzClsDjzlTKey psTzClsDjzlTKey = new PsTzClsDjzlTKey();
		psTzClsDjzlTKey.setTzClassId(tzClassId);
		psTzClsDjzlTKey.setTzSbminfId(tzSbminfId);

		return psTzClsDjzlTMapper.deleteByPrimaryKey(psTzClsDjzlTKey);
	}

}
