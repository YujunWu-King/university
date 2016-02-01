/**
 * 
 */
package com.tranzvision.gd.TZClassSetBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClassInfTMapper;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClassInfT;
import com.tranzvision.gd.TZOrganizationMgBundle.model.PsTzJgBaseT;
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
	private PsTzClassInfTMapper psTzClassInfTMapper;

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

			boolean todoComplete;
			if (null != queryID && !"".equals(queryID)) {
				switch (queryID) {
				case "2":
					strRet = "";
					break;
				case "3":
					strRet = "";
					break;
				case "4":
					strRet = "";
					break;
				case "5":
					strRet = "";
					break;
				case "6":
					strRet = "";
					break;
				case "7":
					strRet = "";
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
					String[] aryComPage = str_com_page_id.split("+");
					if (null != aryComPage && aryComPage.length == 2) {
						String str_com_id = aryComPage[0].trim();
						String str_page_id = aryComPage[1].trim();

						sql = "select TZ_PAGE_REFCODE from PS_TZ_AQ_PAGZC_TBL where TZ_COM_ID=? and TZ_PAGE_ID=?";
						String classId = sqlQuery.queryForObject(sql, new Object[] { str_com_id, str_page_id },
								"String");

						guest_apply_url = request.getContextPath() + "/dispacher?classid=" + classId + "&TZ_CLASS_ID="
								+ str_bj_id;
						guest_apply_url = "<span style=\"font-size:10px;color:#FF0000\">" + guest_apply_url + "</sapn>";
					}
				} else {
					str_guest_apply = "false";
				}
				/* 允许匿名访问-End */

				String str_bj_name = psTzClassInfT.getTzClassName();
				String str_zxbm = psTzClassInfT.getTzIsAppOpen();
				String str_bj_desc = psTzClassInfT.getTzClassDesc();

				String strDateFormat = getSysHardCodeVal.getDateFormat();
				SimpleDateFormat dateFormat = new SimpleDateFormat(strDateFormat);
				String str_st_dt = dateFormat.format(psTzClassInfT.getTzStartDt());
				String str_end_dt = dateFormat.format(psTzClassInfT.getTzEndDt());
				String str_bmst_dt = dateFormat.format(psTzClassInfT.getTzAppStartDt());
				String str_bmend_dt = dateFormat.format(psTzClassInfT.getTzAppEndDt());

				Map<String, String> mapJson = new HashMap<String, String>();
				mapJson.put("bj_id", str_bj_id);
				mapJson.put("bj_name", str_bj_name);
				mapJson.put("xm_id", str_xm_id);
				mapJson.put("bm_kt", str_zxbm);
				mapJson.put("begin_time", str_st_dt);
				mapJson.put("end_time", str_end_dt);
				mapJson.put("beginBm_time", str_bmst_dt);
				mapJson.put("endBm_time", str_bmend_dt);
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
					SimpleDateFormat dateFormat = new SimpleDateFormat(strDateFormat);

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

					String endBm_time = jacksonUtil.getString("endBm_time");
					if (null != endBm_time && !"".equals(endBm_time)) {
						Date tzAppEndDt = dateFormat.parse(endBm_time);
						psTzClassInfT.setTzAppEndDt(tzAppEndDt);
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
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {

				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);
				
				String typeFlag = jacksonUtil.getString("typeFlag");
				String bjid = jacksonUtil.getString("bj_id");
				
				String str_lc = "";
				   String str_zl = "";
				   String str_y = "";
				   
				if("bj_jbxx".equals(typeFlag)){
					
				}else if("".equals(typeFlag)){
					
				}else if("".equals(typeFlag)){
					
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "";
		}

		return strRet;
	}

}
