/**
 * 
 */
package com.tranzvision.gd.TZClassSetBundle.service.impl;

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
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClsBmlcTMapper;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClsBmlchfTMapper;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClsDjzlTMapper;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClsHfdyTMapper;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClsMajorTMapper;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClassInfT;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsAdminTKey;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsBmlcTWithBLOBs;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsBmlchfT;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsDjzlT;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsHfdyT;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsMajorT;
import com.tranzvision.gd.TZProjectSetBundle.dao.PsTzPrjInfTMapper;
import com.tranzvision.gd.TZProjectSetBundle.model.PsTzPrjInfT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 新增班级，原PS：TZ_GD_BJGL_CLS:TZ_GD_ADD_CLS
 * 
 * @author SHIHUA
 * @since 2016-02-03
 */
@Service("com.tranzvision.gd.TZClassSetBundle.service.impl.TzClassAddServiceImpl")
public class TzClassAddServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private GetHardCodePoint getHardCodePoint;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private PsTzPrjInfTMapper psTzPrjInfTMapper;

	@Autowired
	private PsTzClassInfTMapper psTzClassInfTMapper;

	@Autowired
	private PsTzClsDjzlTMapper psTzClsDjzlTMapper;

	@Autowired
	private PsTzClsHfdyTMapper psTzClsHfdyTMapper;

	@Autowired
	private PsTzClsBmlcTMapper psTzClsBmlcTMapper;

	@Autowired
	private PsTzClsBmlchfTMapper psTzClsBmlchfTMapper;

	@Autowired
	private PsTzClsAdminTMapper psTzClsAdminTMapper;

	@Autowired
	private PsTzClsMajorTMapper psTzClsMajorTMapper;

	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);

			String bj_id = jacksonUtil.getString("bj_id");
			String bmlc_id = jacksonUtil.getString("bmlc_id");
			String hfy_id = jacksonUtil.getString("hfy_id");

			Map<String, Object> mapJson = new HashMap<String, Object>();

			if (null != bj_id && !"".equals(bj_id) && null != bmlc_id && !"".equals(bmlc_id) && null != hfy_id
					&& !"".equals(hfy_id)) {
				String sql = "select TZ_APPPRO_COLOR,TZ_CLS_RESULT,TZ_APPPRO_CONTENT,TZ_WFB_DEFALT_BZ from PS_TZ_CLS_BMLCHF_T where TZ_CLASS_ID=? and TZ_APPPRO_ID=? and TZ_APPPRO_HF_BH=?";
				Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { bj_id, bmlc_id, hfy_id });

				if (mapData != null) {

					String str_xs_name = mapData.get("TZ_APPPRO_CONTENT") == null ? ""
							: String.valueOf(mapData.get("TZ_APPPRO_CONTENT"));
					String str_mrfb = mapData.get("TZ_WFB_DEFALT_BZ") == null ? ""
							: String.valueOf(mapData.get("TZ_APPPRO_COLOR"));

					str_xs_name = str_xs_name.replace("<p>", "");
					str_xs_name = str_xs_name.replace("</p>", "");

					if ("on".equals(str_mrfb)) {
						str_mrfb = "true";
					} else {
						str_mrfb = "false";
					}

					mapJson.put("bj_id", bj_id);
					mapJson.put("bmlc_id", bmlc_id);
					mapJson.put("dybh_id", hfy_id);
					mapJson.put("colorCode", mapData.get("TZ_APPPRO_COLOR") == null ? ""
							: String.valueOf(mapData.get("TZ_APPPRO_COLOR")));
					mapJson.put("hf_jg",
							mapData.get("TZ_CLS_RESULT") == null ? "" : String.valueOf(mapData.get("TZ_CLS_RESULT")));
					mapJson.put("ms_zg", str_xs_name);
					mapJson.put("hf_mrz", str_mrfb);
				}

			}

			mapRet.replace("formData", mapJson);

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "查询失败！" + e.getMessage();
		}

		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}

	@Override
	@Transactional
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		try {
			JacksonUtil jacksonUtil = new JacksonUtil();

			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {

				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String str_bj_id = jacksonUtil.getString("bj_id");
				String str_prj_id = jacksonUtil.getString("xm_id");
				String str_bj_name = jacksonUtil.getString("bj_name");
				String str_guest_apply = jacksonUtil.getString("guest_apply");

				String sql = "select 'Y' from PS_TZ_CLASS_INF_T where TZ_JG_ID=? and TZ_CLASS_NAME=?";
				String recExists = sqlQuery.queryForObject(sql, new Object[] { orgid, str_bj_name }, "String");

				if (!"Y".equals(recExists)) {

					if ("".equals(str_bj_id) || "NEXT".equals(str_bj_id)) {
						str_bj_id = String.valueOf(getSeqNum.getSeqNum("TZ_CLASS_INF_T", "TZ_CLASS_ID"));
					}

					if ("trye".equals(str_guest_apply)) {
						str_guest_apply = "Y";
					} else {
						str_guest_apply = "N";
					}

					PsTzPrjInfT psTzPrjInfT = psTzPrjInfTMapper.selectByPrimaryKey(str_prj_id);
					String str_modal_id = "";
					String str_djzl_id = "";
					String str_bmlc_id = "";
					String str_bj_desc = "";
					if (psTzPrjInfT != null) {
						str_modal_id = psTzPrjInfT.getTzAppModalId();
						str_djzl_id = psTzPrjInfT.getTzSbminfTmpId();
						str_bmlc_id = psTzPrjInfT.getTzAppproTmpId();
						str_bj_desc = psTzPrjInfT.getTzPrjDesc();
					}

					sql = "select TZ_APP_TPL_MC from PS_TZ_APPTPL_DY_T where TZ_APP_TPL_ID=?";
					String str_bmb_desc = sqlQuery.queryForObject(sql, new Object[] { str_modal_id }, "String");

					PsTzClassInfT psTzClassInfT = new PsTzClassInfT();
					psTzClassInfT.setTzClassId(str_bj_id);
					psTzClassInfT.setTzJgId(orgid);
					psTzClassInfT.setTzPrjId(str_prj_id);
					psTzClassInfT.setTzClassName(str_bj_name);
					psTzClassInfT.setTzGuestApply(str_guest_apply);
					psTzClassInfT.setTzAppModalId(str_modal_id);
					psTzClassInfT.setTzClassDesc(str_bj_desc);

					sql = "select 'Y' from PS_TZ_CLASS_INF_T where TZ_CLASS_ID=?";
					String recKeyExists = sqlQuery.queryForObject(sql, new Object[] { str_bj_id }, "String");

					if (!"Y".equals(recKeyExists)) {
						int rst = psTzClassInfTMapper.insertSelective(psTzClassInfT);
						if (rst == 0) {
							errMsg[0] = "1";
							errMsg[1] = "添加失败！";
							return strRet;
						}
					} else {
						int rst = psTzClassInfTMapper.updateByPrimaryKeySelective(psTzClassInfT);
						if (rst == 0) {
							errMsg[0] = "1";
							errMsg[1] = "更新失败！";
							return strRet;
						}
					}

					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("bj_id", str_bj_id);
					mapRet.put("bmb_id", str_modal_id);
					mapRet.put("bmb_desc", str_bmb_desc);
					mapRet.put("class_desc", str_bj_desc);
					strRet = jacksonUtil.Map2json(mapRet);

					// 将递交资料详情表中的数据克隆到班级递交资料模型实例表中
					sql = "delete from PS_TZ_CLS_DJZL_T where TZ_CLASS_ID=?";
					sqlQuery.update(sql, new Object[] { str_bj_id });

					sql = "select TZ_SBMINF_ID,TZ_SORT_NUM,TZ_CONT_INTRO,TZ_REMARK from PS_TZ_SBMINF_STP_T where TZ_SBMINF_TMP_ID=?";
					List<Map<String, Object>> listSbminfStpData = sqlQuery.queryForList(sql,
							new Object[] { str_djzl_id });
					for (Map<String, Object> mapSbminfStpData : listSbminfStpData) {

						String str_zl_id = mapSbminfStpData.get("TZ_SBMINF_ID") == null ? ""
								: String.valueOf(mapSbminfStpData.get("TZ_SBMINF_ID"));

						PsTzClsDjzlT psTzClsDjzlT = new PsTzClsDjzlT();
						psTzClsDjzlT.setTzClassId(str_bj_id);
						psTzClsDjzlT.setTzSbminfId(str_zl_id);
						psTzClsDjzlT.setTzSortNum(mapSbminfStpData.get("TZ_SORT_NUM") == null ? 0
								: Integer.parseInt(String.valueOf(mapSbminfStpData.get("TZ_SORT_NUM"))));
						psTzClsDjzlT.setTzContIntro(mapSbminfStpData.get("TZ_CONT_INTRO") == null ? ""
								: String.valueOf(mapSbminfStpData.get("TZ_CONT_INTRO")));
						psTzClsDjzlT.setTzRemark(mapSbminfStpData.get("TZ_REMARK") == null ? ""
								: String.valueOf(mapSbminfStpData.get("TZ_REMARK")));

						psTzClsDjzlTMapper.insert(psTzClsDjzlT);

						// 将递交资料模板常用回复短语表克隆到班级递交资料常用回复短语表
						sql = "delete from PS_TZ_CLS_HFDY_T where TZ_CLASS_ID=? and TZ_SBMINF_ID=?";
						sqlQuery.update(sql, new Object[] { str_bj_id, str_zl_id });

						sql = "select TZ_SBMINF_REP_ID,TZ_SBMINF_REP from PS_TZ_SBMINF_REP_T where TZ_SBMINF_TMP_ID=? and TZ_SBMINF_ID=?";
						List<Map<String, Object>> listSbminfRepData = sqlQuery.queryForList(sql,
								new Object[] { str_djzl_id, str_zl_id });
						for (Map<String, Object> mapSbminfRepData : listSbminfRepData) {
							PsTzClsHfdyT psTzClsHfdyT = new PsTzClsHfdyT();
							psTzClsHfdyT.setTzClassId(str_bj_id);
							psTzClsHfdyT.setTzSbminfId(str_zl_id);
							psTzClsHfdyT.setTzSbminfRepId(mapSbminfRepData.get("TZ_SBMINF_REP_ID") == null ? ""
									: String.valueOf(mapSbminfRepData.get("TZ_SBMINF_REP_ID")));
							psTzClsHfdyT.setTzSbminfRep(mapSbminfRepData.get("TZ_SBMINF_REP") == null ? ""
									: String.valueOf(mapSbminfRepData.get("TZ_SBMINF_REP")));
							psTzClsHfdyTMapper.insert(psTzClsHfdyT);
						}

					}

					// 将报名流程步骤详情表中的数据克隆到班级报名流程实例表中
					sql = "delete from PS_TZ_CLS_BMLC_T where TZ_CLASS_ID=?";
					sqlQuery.update(sql, new Object[] { str_bj_id });

					sql = "select TZ_APPPRO_ID,TZ_SORT_NUM,TZ_APPPRO_NAME from PS_TZ_APPPRO_STP_T where TZ_APPPRO_TMP_ID=?";
					List<Map<String, Object>> listAppproStpData = sqlQuery.queryForList(sql,
							new Object[] { str_bmlc_id });
					for (Map<String, Object> mapAppproStpData : listAppproStpData) {
						String str_lc_id = mapAppproStpData.get("TZ_APPPRO_ID") == null ? ""
								: String.valueOf(mapAppproStpData.get("TZ_APPPRO_ID"));

						PsTzClsBmlcTWithBLOBs psTzClsBmlcTWithBLOBs = new PsTzClsBmlcTWithBLOBs();
						psTzClsBmlcTWithBLOBs.setTzClassId(str_bj_id);
						psTzClsBmlcTWithBLOBs.setTzAppproId(str_lc_id);
						psTzClsBmlcTWithBLOBs.setTzSortNum(mapAppproStpData.get("TZ_SORT_NUM") == null ? 0
								: Integer.parseInt(String.valueOf(mapAppproStpData.get("TZ_SORT_NUM"))));
						psTzClsBmlcTWithBLOBs.setTzAppproName(mapAppproStpData.get("TZ_APPPRO_NAME") == null ? ""
								: String.valueOf(mapAppproStpData.get("TZ_APPPRO_NAME")));
						psTzClsBmlcTWithBLOBs.setTzIsPublic("");
						psTzClsBmlcTWithBLOBs.setTzDefContent("");
						psTzClsBmlcTWithBLOBs.setTzTmpContent("");
						psTzClsBmlcTMapper.insert(psTzClsBmlcTWithBLOBs);

						// 将报名流程步骤回复短语表克隆到班级报名流程步骤回复短语表
						sql = "delete from PS_TZ_CLS_BMLCHF_T where TZ_CLASS_ID=? and TZ_APPPRO_ID=?";
						sqlQuery.update(sql, new Object[] { str_bj_id, str_lc_id });

						sql = "select TZ_APPPRO_HF_BH,TZ_APPPRO_COLOR,TZ_CLS_RESULT,TZ_APPPRO_CONTENT,TZ_WFB_DEFALT_BZ from PS_TZ_APPPRO_HF_T where TZ_APPPRO_TMP_ID=? and TZ_APPPRO_ID=?";
						List<Map<String, Object>> listAppproHfData = sqlQuery.queryForList(sql,
								new Object[] { str_bmlc_id, str_lc_id });
						for (Map<String, Object> mapAppproHfData : listAppproHfData) {
							PsTzClsBmlchfT psTzClsBmlchfT = new PsTzClsBmlchfT();
							psTzClsBmlchfT.setTzClassId(str_bj_id);
							psTzClsBmlchfT.setTzAppproId(str_lc_id);
							psTzClsBmlchfT.setTzAppproHfBh(mapAppproHfData.get("TZ_APPPRO_HF_BH") == null ? ""
									: String.valueOf(mapAppproHfData.get("TZ_APPPRO_HF_BH")));
							psTzClsBmlchfT.setTzAppproColor(mapAppproHfData.get("TZ_APPPRO_COLOR") == null ? ""
									: String.valueOf(mapAppproHfData.get("TZ_APPPRO_COLOR")));
							psTzClsBmlchfT.setTzClsResult(mapAppproHfData.get("TZ_CLS_RESULT") == null ? ""
									: String.valueOf(mapAppproHfData.get("TZ_CLS_RESULT")));
							psTzClsBmlchfT.setTzAppproContent(mapAppproHfData.get("TZ_APPPRO_CONTENT") == null ? ""
									: String.valueOf(mapAppproHfData.get("TZ_APPPRO_CONTENT")));
							psTzClsBmlchfT.setTzWfbDefaltBz(mapAppproHfData.get("TZ_WFB_DEFALT_BZ") == null ? ""
									: String.valueOf(mapAppproHfData.get("TZ_WFB_DEFALT_BZ")));
							psTzClsBmlchfTMapper.insert(psTzClsBmlchfT);
						}
					}

					// 将项目管理人员关系表克隆到班级管理人员关系表
					sql = "delete from PS_TZ_CLS_ADMIN_T where TZ_CLASS_ID=?";
					sqlQuery.update(sql, new Object[] { str_bj_id });

					sql = "select OPRID from PS_TZ_PRJ_ADMIN_T where TZ_PRJ_ID=?";
					List<Map<String, Object>> listPrjAdminData = sqlQuery.queryForList(sql,
							new Object[] { str_prj_id });
					for (Map<String, Object> mapPrjAdminData : listPrjAdminData) {
						PsTzClsAdminTKey psTzClsAdminTKey = new PsTzClsAdminTKey();
						psTzClsAdminTKey.setTzClassId(str_bj_id);
						psTzClsAdminTKey.setOprid(mapPrjAdminData.get("OPRID") == null ? ""
								: String.valueOf(mapPrjAdminData.get("OPRID")));
						psTzClsAdminTMapper.insert(psTzClsAdminTKey);
					}

					// 将项目与专业方向关系表克隆到班级与专业方向关系表
					sql = "delete from PS_TZ_CLS_MAJOR_T where TZ_CLASS_ID=?";
					sqlQuery.update(sql, new Object[] { str_bj_id });

					sql = "select TZ_MAJOR_ID,TZ_MAJOR_NAME,TZ_SORT_NUM from PS_TZ_PRJ_MAJOR_T where TZ_PRJ_ID=?";
					List<Map<String, Object>> listPrjMajorData = sqlQuery.queryForList(sql,
							new Object[] { str_prj_id });
					for (Map<String, Object> mapPrjMajorData : listPrjMajorData) {
						PsTzClsMajorT psTzClsMajorT = new PsTzClsMajorT();
						psTzClsMajorT.setTzClassId(str_bj_id);
						psTzClsMajorT.setTzMajorId(mapPrjMajorData.get("TZ_MAJOR_ID") == null ? ""
								: String.valueOf(mapPrjMajorData.get("TZ_MAJOR_ID")));
						psTzClsMajorT.setTzMajorName(mapPrjMajorData.get("TZ_MAJOR_NAME") == null ? ""
								: String.valueOf(mapPrjMajorData.get("TZ_MAJOR_NAME")));
						psTzClsMajorT.setTzSortNum(mapPrjMajorData.get("TZ_SORT_NUM") == null ? 0
								: Integer.parseInt(String.valueOf(mapPrjMajorData.get("TZ_SORT_NUM"))));
						psTzClsMajorT.setRowAddedDttm(dateNow);
						psTzClsMajorT.setRowAddedOprid(oprid);
						psTzClsMajorT.setRowLastmantDttm(dateNow);
						psTzClsMajorT.setRowLastmantOprid(oprid);
						psTzClsMajorTMapper.insertSelective(psTzClsMajorT);
					}

				} else {
					errMsg[0] = "1";
					errMsg[1] = "班级名称【" + str_bj_name + "】在当前机构已经存在！请重新输入。";
					break;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "添加失败！" + e.getMessage();
		}

		return strRet;
	}

	/**
	 * 班级基本页面，勾选时发送请求
	 */
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {

		String strRet = "";

		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strParams);

			String str_bj_id = jacksonUtil.getString("bj_id");
			String str_guest_apply = jacksonUtil.getString("guest_apply");

			if ("true".equals(str_guest_apply)) {

				// hardCode 获取注册组件ID和页面ID
				String str_com_page_id = getHardCodePoint.getHardCodePointVal("TZ_GD_GUEST_APPLY");
				String[] aryComPage = str_com_page_id.split("\\+");
				String str_com_id = "";
				String str_page_id = "";
				if (aryComPage.length == 2) {
					str_com_id = aryComPage[0];
					str_page_id = aryComPage[1];
				}
				
				String sqlGetSiteId = "select TZ_SITEI_ID from PS_TZ_CLASS_INF_T A,PS_TZ_PROJECT_SITE_T B where A.TZ_CLASS_ID=? AND A.TZ_PRJ_ID = B.TZ_PRJ_ID LIMIT 1";
				String strSiteId = sqlQuery.queryForObject(sqlGetSiteId, new Object[] { str_bj_id }, "String");
				
				String sql = "select TZ_PAGE_REFCODE from PS_TZ_AQ_PAGZC_TBL where TZ_COM_ID=? and TZ_PAGE_ID=?";
				String classId = sqlQuery.queryForObject(sql, new Object[] { str_com_id, str_page_id }, "String");
				
				String strUrlSuffix;
				strUrlSuffix = request.getServerName();
				if(!"80".equals(request.getServerPort()))
				{
					strUrlSuffix = strUrlSuffix  + ":" + request.getServerPort();
				}
				
				String guest_apply_url = strUrlSuffix + request.getContextPath() + "/dispatcher?classid=" + classId + "&TZ_CLASS_ID="
						+ str_bj_id + "&SITE_ID=" + strSiteId;
				
				guest_apply_url = "&nbsp;<span style=\"font-size:12px;color:#FF0000;\">" + guest_apply_url + "</span>";

				Map<String, Object> mapRet = new HashMap<String, Object>();
				mapRet.put("guest_apply_url", guest_apply_url);

				strRet = jacksonUtil.Map2json(mapRet);

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作失败！" + e.getMessage();
		}

		return strRet;
	}

}
