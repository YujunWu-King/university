package com.tranzvision.gd.TZProjectSetBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZProjectSetBundle.dao.PsTzPrjInfTMapper;
import com.tranzvision.gd.TZProjectSetBundle.dao.PsTzPrjMajorTMapper;
import com.tranzvision.gd.TZProjectSetBundle.dao.PsTzProjectSiteTMapper;
import com.tranzvision.gd.TZProjectSetBundle.dao.PsTzPrjAdminTMapper;
import com.tranzvision.gd.TZProjectSetBundle.model.PsTzPrjInfT;
import com.tranzvision.gd.TZProjectSetBundle.model.PsTzPrjMajorT;
import com.tranzvision.gd.TZProjectSetBundle.model.PsTzPrjMajorTKey;
import com.tranzvision.gd.TZProjectSetBundle.model.PsTzProjectSiteTKey;
import com.tranzvision.gd.TZProjectSetBundle.model.PsTzPrjAdminTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author 张彬彬; 功能说明：招生项目管理; 原PS类：TZ_GD_PROMG_PKG:TZ_GD_PROMG_CLS
 * @author ZXW  20160117 
 * 功能说明：材料面试相关-修改面试评审模板对应字段，添加自动初筛模板、标签组、初筛比率字段 
 */
@Service("com.tranzvision.gd.TZProjectSetBundle.service.impl.tzProMgClsServiceImpl")
public class tzProMgClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzPrjInfTMapper PsTzPrjInfTMapper;
	@Autowired
	private PsTzPrjMajorTMapper PsTzPrjMajorTMapper;
	@Autowired
	private PsTzPrjAdminTMapper PsTzPrjAdminTMapper;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzProjectSiteTMapper psTzProjectSiteTMapper;

	/* 查询项目分类列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] {};

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_PRJ_ID", "TZ_PRJ_NAME", "TZ_PRJ_DESC", "TZ_PRJ_TYPE_NAME", "TZ_IS_OPEN",
				"TZ_ISOPEN_DESC" };

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("projectId", rowList[0]);
				mapList.put("projectName", rowList[1]);
				mapList.put("projectDesc", rowList[2]);
				mapList.put("projectType", rowList[3]);
				mapList.put("usedStatus", rowList[4]);
				mapList.put("statusDesc", rowList[5]);
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}

	/* 获取项目详情信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("projectId")) {
				// 项目分类编号;
				String strProjectId = jacksonUtil.getString("projectId");
				PsTzPrjInfT psTzPrjInfT = PsTzPrjInfTMapper.selectByPrimaryKey(strProjectId);
				if (psTzPrjInfT != null) {
					Map<String, Object> map = new HashMap<>();
					map.put("projectId", psTzPrjInfT.getTzPrjId());
					map.put("projectName", psTzPrjInfT.getTzPrjName());
					map.put("projectType", psTzPrjInfT.getTzPrjTypeId());
					map.put("appOnFormModel", psTzPrjInfT.getTzAppModalId());
					map.put("applyScheduModel", psTzPrjInfT.getTzAppproTmpId());
					map.put("isOpen", psTzPrjInfT.getTzIsOpen());
					map.put("projectDesc", psTzPrjInfT.getTzPrjDesc());
					map.put("appFormName", "");
					map.put("appScheduModName", "");
					map.put("smtDtTmpId", psTzPrjInfT.getTzSbminfTmpId());
					map.put("smtDtName", "");
					/*20170118*/
					map.put("clps_cj_modal", psTzPrjInfT.getTzZlpsScorMdId());
					map.put("clps_cj_modal_desc", "");
					map.put("msps_cj_modal", psTzPrjInfT.getTzMscjScorMdId());
					map.put("msps_cj_modal_desc", "");					
					//初筛模型
					map.put("cs_cj_modal", psTzPrjInfT.getTzCsScorMdId());
					map.put("cs_cj_modal_desc", "");
					//考生标签
					map.put("ksbq", psTzPrjInfT.getTzCsKsbqzId());
					map.put("ksbq_desc", "");
					//负面清单
					map.put("fmqd", psTzPrjInfT.getTzCsFmbqzId());
					map.put("fmqd_desc", "");
					//淘汰比率
					map.put("ttbl", psTzPrjInfT.getTzTtBl());
					map.put("ttbl2", "%");
					/*20170118 end*/
					
					//@added by ytt 2017-2-8
					//证书模板
					map.put("zsmbid", psTzPrjInfT.getTzCertTmplId());
					map.put("zsmbname", "%");
					
					map.put("ps_appf_modal", psTzPrjInfT.getTzPsAppfMId());
					map.put("ps_appf_modal_desc", "");
					map.put("sites", "");

					/* 在线报名模版描述 */
					String sql = "SELECT TZ_APP_TPL_MC FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID= ?";
					String strAppModelID = psTzPrjInfT.getTzAppModalId();
					String strAppModelName = sqlQuery.queryForObject(sql, new Object[] { strAppModelID }, "String");
					map.replace("appFormName", strAppModelName);

					/* 在线报名附属模版描述 */
					sql = "SELECT TZ_APP_TPL_MC FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID= ?";
					String strAppSubModelID = psTzPrjInfT.getTzPsAppfMId();
					String strAppSubModelName = sqlQuery.queryForObject(sql, new Object[] { strAppSubModelID },
							"String");
					map.replace("ps_appf_modal_desc", strAppSubModelName);

					/* 材料评审成绩模型描述 */
					sql = "SELECT TZ_MODAL_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_JG_ID=? AND TZ_SCORE_MODAL_ID=?";
					String strClpsScoreModelID = psTzPrjInfT.getTzZlpsScorMdId();
					String strClpsScoreModelName = sqlQuery.queryForObject(sql, new Object[] {orgid,strClpsScoreModelID },
							"String");
					map.replace("clps_cj_modal_desc", strClpsScoreModelName);

					/* 面试评审成绩模型描述 */
					sql = "SELECT TZ_MODAL_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_JG_ID=? AND TZ_SCORE_MODAL_ID=?";
					String strMspsScoreModelID = psTzPrjInfT.getTzMscjScorMdId();
					String strMspsScoreModelName = sqlQuery.queryForObject(sql, new Object[] {orgid,strMspsScoreModelID },
							"String");
					map.replace("msps_cj_modal_desc", strMspsScoreModelName);

					/* 报名流程模版描述 */
					sql = "SELECT TZ_APPPRO_TMP_NAME FROM PS_TZ_APPPRO_TMP_T WHERE TZ_APPPRO_TMP_ID=?";
					String strAppScheduModId = psTzPrjInfT.getTzAppproTmpId();
					String strAppScheduModName = sqlQuery.queryForObject(sql, new Object[] { strAppScheduModId },
							"String");
					map.replace("appScheduModName", strAppScheduModName);

					/* 递交资料模型描述 */
					sql = "SELECT TZ_SBMINF_TMP_NAME FROM PS_TZ_SBMINF_TMP_T WHERE TZ_SBMINF_TMP_ID=?";
					String strSubmitInfoModId = psTzPrjInfT.getTzSbminfTmpId();
					String strSubmitInfoModName = sqlQuery.queryForObject(sql, new Object[] { strSubmitInfoModId },
							"String");
					map.replace("smtDtName", strSubmitInfoModName);
				
					/* 自动初筛模型描述 20170118 TZ_CS_SCOR_MD_ID */
					sql = "SELECT TZ_MODAL_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_JG_ID=? AND TZ_SCORE_MODAL_ID=?";
					String strCsScoreModelID = psTzPrjInfT.getTzCsScorMdId();
					String strCsScoreModelName = sqlQuery.queryForObject(sql, new Object[] { orgid,strCsScoreModelID },
							"String");
					map.replace("cs_cj_modal_desc", strCsScoreModelName);

					/* 负面清单规则描述 20170118 TZ_CS_KSBQZ_ID */
					sql = "SELECT TZ_BIAOQZ_NAME FROM PS_TZ_BIAOQZ_T WHERE TZ_JG_ID=? AND TZ_BIAOQZ_ID=?";
					String strtTzCsFmbqzId = psTzPrjInfT.getTzCsFmbqzId();
					String strtTzCsFmbqzDesc = sqlQuery.queryForObject(sql, new Object[] { orgid,strtTzCsFmbqzId },
							"String");
					map.replace("fmqd_desc", strtTzCsFmbqzDesc);
					
					/* 自动标签规则描述 20170118 TZ_CS_FMBQZ_ID */
					sql = "SELECT TZ_BIAOQZ_NAME FROM PS_TZ_BIAOQZ_T WHERE TZ_JG_ID=? AND TZ_BIAOQZ_ID=?";
					String strtTzCsKsbqzId = psTzPrjInfT.getTzCsKsbqzId();
					String strtTzCsKsbqzDesc = sqlQuery.queryForObject(sql, new Object[] { orgid,strtTzCsKsbqzId },
							"String");
					map.replace("ksbq_desc", strtTzCsKsbqzDesc);
					
					//@added by ytt 2017-2-8
					//证书模板名称
					sql = "SELECT TZ_TMPL_NAME FROM PS_TZ_CERTTMPL_TBL WHERE TZ_JG_ID=? AND TZ_CERT_TMPL_ID=?";
					String strtzCertTmplId = psTzPrjInfT.getTzCertTmplId();
					String strtzTmplName = sqlQuery.queryForObject(sql, new Object[] { orgid,strtzCertTmplId },
							"String");
					map.replace("zsmbname", strtzTmplName);
					
					
					/* 项目发布的站点 */
					ArrayList<String> sites = new ArrayList<>();
					List<Map<String, Object>> siteList = sqlQuery.queryForList(
							"select TZ_SITEI_ID from PS_TZ_PROJECT_SITE_T where TZ_PRJ_ID=?",
							new Object[] { strProjectId });
					if (siteList != null) {
						for (int h = 0; h < siteList.size(); h++) {
							String siteId = (String) siteList.get(h).get("TZ_SITEI_ID");
							sites.add(siteId);
						}
					}
					map.put("sites", sites);

					returnJsonMap.replace("formData", map);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该项目不存在.";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "该项目不存在.";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}

	/**
	 * 新建项目
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> mapData = jacksonUtil.getMap("data");

				if ("PROINFO".equals(typeFlag)) {

					String strPojectId = String.valueOf(mapData.get("projectId"));
					String strProjectName = String.valueOf(mapData.get("projectName"));
					/* 项目名称是否已经存在 */
					String sql = "SELECT count(1) FROM PS_TZ_PRJ_INF_T WHERE TZ_JG_ID= ? AND TZ_PRJ_ID <> ? AND TZ_PRJ_NAME= ?";

					int isExistProjectNum = sqlQuery.queryForObject(sql,
							new Object[] { orgid, strPojectId, strProjectName }, "Integer");

					if (isExistProjectNum == 0) {

						if ("".equals(strPojectId) || "NEXT".equals(strPojectId.toUpperCase())) {
							strPojectId = "PRJ_" + String.valueOf(getSeqNum.getSeqNum("PS_TZ_PRJ_INF_T", "TZ_PRJ_ID"));
						}
						String strProjectType = String.valueOf(mapData.get("projectType"));
						String strAppModelId = String.valueOf(mapData.get("appOnFormModel"));
						String strAppSubModelId = String.valueOf(mapData.get("ps_appf_modal"));
						String strIsOpen = String.valueOf(mapData.get("isOpen"));
						String strAppScheduModId = String.valueOf(mapData.get("applyScheduModel"));
						String strSubmitInfoModId = String.valueOf(mapData.get("smtDtTmpId"));
						String strClpsScoreModelID = String.valueOf(mapData.get("clps_cj_modal"));
						String strMspsScoreModelID = String.valueOf(mapData.get("msps_cj_modal"));
						String strProjectDesc = String.valueOf(mapData.get("projectDesc"));
						/*20170118 tzAdd方法：获取*/
						String strCsScoreModelID = String.valueOf(mapData.get("cs_cj_modal"));
						String strtTzCsKsbqzId = String.valueOf(mapData.get("ksbq"));
						String strtTzCsFmbqzId = String.valueOf(mapData.get("fmqd"));
						String strTtbl = String.valueOf(mapData.get("ttbl"));
						String strCertTmplId = String.valueOf(mapData.get("zsmbid"));
						/*20170118 tzAdd方法end*/
						PsTzPrjInfT psTzPrjInfT = new PsTzPrjInfT();
						psTzPrjInfT.setTzPrjId(strPojectId);
						psTzPrjInfT.setTzPrjName(strProjectName);
						psTzPrjInfT.setTzPrjTypeId(strProjectType);
						psTzPrjInfT.setTzJgId(orgid);
						psTzPrjInfT.setTzAppModalId(strAppModelId);
						psTzPrjInfT.setTzPsAppfMId(strAppSubModelId);
						psTzPrjInfT.setTzIsOpen(strIsOpen);
						psTzPrjInfT.setTzAppproTmpId(strAppScheduModId);
						psTzPrjInfT.setTzSbminfTmpId(strSubmitInfoModId);
						psTzPrjInfT.setTzZlpsScorMdId(strClpsScoreModelID);
						psTzPrjInfT.setTzMscjScorMdId(strMspsScoreModelID);
						psTzPrjInfT.setTzPrjDesc(strProjectDesc);
						/*20170118 tzAdd方法：保存*/
						psTzPrjInfT.setTzMscjScorMdId(strCsScoreModelID) ;
						psTzPrjInfT.setTzMscjScorMdId(strtTzCsKsbqzId) ;
						psTzPrjInfT.setTzMscjScorMdId(strtTzCsFmbqzId) ;
						psTzPrjInfT.setTzMscjScorMdId(strTtbl);
						psTzPrjInfT.setTzCertTmplId(strCertTmplId);
						/*20170118 tzAdd方法end*/
						int i = PsTzPrjInfTMapper.insert(psTzPrjInfT);
						if (i > 0) {
							// 限定的项目;
							ArrayList<String> sites = new ArrayList<>();
							if (mapData.get("sites") != null && !"".equals(mapData.get("sites"))) {
								sites = (ArrayList<String>) mapData.get("sites");
							}

							// 删除发布的站点;
							sqlQuery.update("delete from PS_TZ_PROJECT_SITE_T where TZ_PRJ_ID=?",
									new Object[] { strPojectId });
							// 重新添加发布站点
							if (sites != null && sites.size() > 0) {
								for (int k = 0; k < sites.size(); k++) {
									String siteId = (String) sites.get(k);
									PsTzProjectSiteTKey psTzProjectSiteTKey = new PsTzProjectSiteTKey();
									psTzProjectSiteTKey.setTzPrjId(strPojectId);
									psTzProjectSiteTKey.setTzSiteiId(siteId);
									psTzProjectSiteTMapper.insertSelective(psTzProjectSiteTKey);
								}
							}
						}

						Map<String, Object> mapJson = new HashMap<String, Object>();
						mapJson.put("projectId", strPojectId);
						strRet = jacksonUtil.Map2json(mapJson);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "同一机构下项目名称不能重复！";
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	/**
	 * 更新项目信息
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String strPojectIdIns = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> mapData = jacksonUtil.getMap("data");

				if ("PROINFO".equals(typeFlag)) {

					String strPojectId = String.valueOf(mapData.get("projectId"));
					String strProjectName = String.valueOf(mapData.get("projectName"));
					/* 项目名称是否已经存在 */
					String sql = "SELECT count(1) FROM PS_TZ_PRJ_INF_T WHERE TZ_JG_ID= ? AND TZ_PRJ_ID <> ? AND TZ_PRJ_NAME= ?";

					int isExistProjectNameNum = sqlQuery.queryForObject(sql,
							new Object[] { orgid, strPojectId, strProjectName }, "Integer");

					if (isExistProjectNameNum == 0) {

						int isExistProjectNum = 0;

						if ("".equals(strPojectId) || "NEXT".equals(strPojectId.toUpperCase())) {
							strPojectId = "PRJ_" + String.valueOf(getSeqNum.getSeqNum("PS_TZ_PRJ_INF_T", "TZ_PRJ_ID"));
						} else {
							/* 项目名称是否已经存在 */
							String sqlProjectExists = "SELECT count(1) FROM PS_TZ_PRJ_INF_T WHERE TZ_JG_ID= ? AND TZ_PRJ_ID = ?";

							isExistProjectNum = sqlQuery.queryForObject(sqlProjectExists,
									new Object[] { orgid, strPojectId }, "Integer");
						}
						String strProjectType = String.valueOf(mapData.get("projectType"));
						String strAppModelId = String.valueOf(mapData.get("appOnFormModel"));
						String strAppSubModelId = String.valueOf(mapData.get("ps_appf_modal"));
						String strIsOpen = String.valueOf(mapData.get("isOpen"));
						String strAppScheduModId = String.valueOf(mapData.get("applyScheduModel"));
						String strSubmitInfoModId = String.valueOf(mapData.get("smtDtTmpId"));
						String strClpsScoreModelID = String.valueOf(mapData.get("clps_cj_modal"));
						String strMspsScoreModelID = String.valueOf(mapData.get("msps_cj_modal"));
						String strProjectDesc = String.valueOf(mapData.get("projectDesc"));
						/*20170118 tzUpdate方法：获取*/
						String strCsScoreModelID = String.valueOf(mapData.get("cs_cj_modal"));
						String strtTzCsKsbqzId = String.valueOf(mapData.get("ksbq"));
						String strtTzCsFmbqzId = String.valueOf(mapData.get("fmqd"));
						String strTtbl = String.valueOf(mapData.get("ttbl"));
						String strCertTmplId = String.valueOf(mapData.get("zsmbid"));
						/*20170118 tzUpdate方法end*/
						
						PsTzPrjInfT psTzPrjInfT = new PsTzPrjInfT();
						psTzPrjInfT.setTzPrjId(strPojectId);
						psTzPrjInfT.setTzPrjName(strProjectName);
						psTzPrjInfT.setTzPrjTypeId(strProjectType);
						psTzPrjInfT.setTzJgId(orgid);
						psTzPrjInfT.setTzAppModalId(strAppModelId);
						psTzPrjInfT.setTzPsAppfMId(strAppSubModelId);
						psTzPrjInfT.setTzIsOpen(strIsOpen);
						psTzPrjInfT.setTzAppproTmpId(strAppScheduModId);
						psTzPrjInfT.setTzSbminfTmpId(strSubmitInfoModId);
						psTzPrjInfT.setTzZlpsScorMdId(strClpsScoreModelID);
						psTzPrjInfT.setTzMscjScorMdId(strMspsScoreModelID);
						psTzPrjInfT.setTzPrjDesc(strProjectDesc);
						/*20170118 tzUpdate方法：保存*/
						psTzPrjInfT.setTzCsScorMdId(strCsScoreModelID) ;
						psTzPrjInfT.setTzCsKsbqzId(strtTzCsKsbqzId) ;
						psTzPrjInfT.setTzCsFmbqzId(strtTzCsFmbqzId) ;
						psTzPrjInfT.setTzTtBl(strTtbl);
						psTzPrjInfT.setTzCertTmplId(strCertTmplId);
						/*20170118 tzUpdate方法end*/
						if (isExistProjectNum > 0) {
							PsTzPrjInfTMapper.updateByPrimaryKeyWithBLOBs(psTzPrjInfT);
						} else {
							PsTzPrjInfTMapper.insert(psTzPrjInfT);
						}
						strPojectIdIns = strPojectId;

						// 限定的项目;
						ArrayList<String> sites = new ArrayList<>();
						if (mapData.get("sites") != null && !"".equals(mapData.get("sites"))) {
							sites = (ArrayList<String>) mapData.get("sites");
						}

						// 删除发布的站点;
						sqlQuery.update("delete from PS_TZ_PROJECT_SITE_T where TZ_PRJ_ID=?",
								new Object[] { strPojectId });
						// 重新添加发布站点
						if (sites != null && sites.size() > 0) {
							for (int k = 0; k < sites.size(); k++) {
								String siteId = (String) sites.get(k);
								PsTzProjectSiteTKey psTzProjectSiteTKey = new PsTzProjectSiteTKey();
								psTzProjectSiteTKey.setTzPrjId(strPojectId);
								psTzProjectSiteTKey.setTzSiteiId(siteId);
								psTzProjectSiteTMapper.insertSelective(psTzProjectSiteTKey);
							}
						}

					} else {
						errMsg[0] = "1";
						errMsg[1] = "同一机构下项目名称不能重复！";
					}
				}
				/* 保存专业方向信息 */
				if ("PROFESSION".equals(typeFlag) && "0".equals(errMsg[0])) {
					String strProfessionId = String.valueOf(mapData.get("professionId"));
					String strProfessionName = String.valueOf(mapData.get("professionName"));
					String strSortNum = String.valueOf(mapData.get("sortNum"));

					// 查看是否已经存在;
					int isExistProffessionNum = 0;
					String isExistProffessionSQL = "SELECT COUNT(1) FROM PS_TZ_PRJ_MAJOR_T WHERE TZ_PRJ_ID=? AND TZ_MAJOR_ID = ?";
					isExistProffessionNum = jdbcTemplate.queryForObject(isExistProffessionSQL,
							new Object[] { strPojectIdIns, strProfessionId }, "Integer");

					if (isExistProffessionNum > 0) {
						PsTzPrjMajorT psTzPrjMajorT = new PsTzPrjMajorT();
						psTzPrjMajorT.setTzPrjId(strPojectIdIns);
						psTzPrjMajorT.setTzMajorId(strProfessionId);
						psTzPrjMajorT.setTzMajorName(strProfessionName);
						psTzPrjMajorT.setTzSortNum(Integer.parseInt(strSortNum));
						PsTzPrjMajorTMapper.updateByPrimaryKeySelective(psTzPrjMajorT);
					} else {
						PsTzPrjMajorT psTzPrjMajorT = new PsTzPrjMajorT();
						psTzPrjMajorT.setTzPrjId(strPojectIdIns);
						psTzPrjMajorT.setTzMajorId(strProfessionId);
						psTzPrjMajorT.setTzMajorName(strProfessionName);
						psTzPrjMajorT.setTzSortNum(Integer.parseInt(strSortNum));
						PsTzPrjMajorTMapper.insert(psTzPrjMajorT);
					}
				}

				/* 保存专业方向信息 */
				if ("MANAGER".equals(typeFlag) && "0".equals(errMsg[0])) {
					String strManagerOprid = String.valueOf(mapData.get("managerOprid"));

					// 查看是否已经存在;
					int isExistManagerNum = 0;
					String isExistManagerSQL = "SELECT COUNT(1) FROM PS_TZ_PRJ_ADMIN_T WHERE TZ_PRJ_ID=? AND OPRID = ?";
					isExistManagerNum = jdbcTemplate.queryForObject(isExistManagerSQL,
							new Object[] { strPojectIdIns, strManagerOprid }, "Integer");

					if (isExistManagerNum == 0) {
						PsTzPrjAdminTKey psTzPrjAdminTKey = new PsTzPrjAdminTKey();
						psTzPrjAdminTKey.setTzPrjId(strPojectIdIns);
						psTzPrjAdminTKey.setOprid(strManagerOprid);
						PsTzPrjAdminTMapper.insert(psTzPrjAdminTKey);
					}
				}

				Map<String, Object> mapJson = new HashMap<String, Object>();
				mapJson.put("projectId", strPojectIdIns);
				strRet = jacksonUtil.Map2json(mapJson);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	/**
	 * 删除专业方向、项目管理员员信息
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzDelete(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String typeFlag = jacksonUtil.getString("typeFlag");
				String strPojectId = jacksonUtil.getString("projectId");
				Map<String, Object> mapData = jacksonUtil.getMap("data");

				/* 删除专业方向信息 */
				if ("PROFESSION".equals(typeFlag)) {
					String strProfessionId = String.valueOf(mapData.get("professionId"));

					// 查看是否已经存在;
					int isExistProffessionNum = 0;
					String isExistProffessionSQL = "SELECT COUNT(1) FROM PS_TZ_PRJ_MAJOR_T WHERE TZ_PRJ_ID=? AND TZ_MAJOR_ID = ?";
					isExistProffessionNum = jdbcTemplate.queryForObject(isExistProffessionSQL,
							new Object[] { strPojectId, strProfessionId }, "Integer");

					if (isExistProffessionNum > 0) {
						PsTzPrjMajorTKey psTzPrjMajorTKey = new PsTzPrjMajorTKey();
						psTzPrjMajorTKey.setTzPrjId(strPojectId);
						psTzPrjMajorTKey.setTzMajorId(strProfessionId);
						PsTzPrjMajorTMapper.deleteByPrimaryKey(psTzPrjMajorTKey);
					}
				}
				/* 保存专业方向信息 */
				if ("MANAGER".equals(typeFlag)) {
					String strManagerOprid = String.valueOf(mapData.get("managerOprid"));

					// 查看是否已经存在;
					int isExistManagerNum = 0;
					String isExistManagerSQL = "SELECT COUNT(1) FROM PS_TZ_PRJ_ADMIN_T WHERE TZ_PRJ_ID=? AND OPRID = ?";
					isExistManagerNum = jdbcTemplate.queryForObject(isExistManagerSQL,
							new Object[] { strPojectId, strManagerOprid }, "Integer");

					if (isExistManagerNum > 0) {
						PsTzPrjAdminTKey psTzPrjAdminTKey = new PsTzPrjAdminTKey();
						psTzPrjAdminTKey.setTzPrjId(strPojectId);
						psTzPrjAdminTKey.setOprid(strManagerOprid);
						PsTzPrjAdminTMapper.deleteByPrimaryKey(psTzPrjAdminTKey);
					}
				}

				Map<String, Object> mapJson = new HashMap<String, Object>();
				mapJson.put("projectId", strPojectId);
				strRet = jacksonUtil.Map2json(mapJson);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}
}
