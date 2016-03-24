/**
 * 
 */
package com.tranzvision.gd.TZLeaguerDataItemBundle.service.impl;

import java.util.ArrayList;
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
import com.tranzvision.gd.TZLeaguerDataItemBundle.dao.PsTzRegFieldTMapper;
import com.tranzvision.gd.TZLeaguerDataItemBundle.dao.PsTzRegfieldEngMapper;
import com.tranzvision.gd.TZLeaguerDataItemBundle.dao.PsTzUserregMbTMapper;
import com.tranzvision.gd.TZLeaguerDataItemBundle.dao.PsTzYhzcXxzEngMapper;
import com.tranzvision.gd.TZLeaguerDataItemBundle.dao.PsTzYhzcXxzTblMapper;
import com.tranzvision.gd.TZLeaguerDataItemBundle.model.PsTzRegFieldT;
import com.tranzvision.gd.TZLeaguerDataItemBundle.model.PsTzRegfieldEng;
import com.tranzvision.gd.TZLeaguerDataItemBundle.model.PsTzUserregMbT;
import com.tranzvision.gd.TZLeaguerDataItemBundle.model.PsTzYhzcXxzEng;
import com.tranzvision.gd.TZLeaguerDataItemBundle.model.PsTzYhzcXxzTbl;
import com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl.RegisteServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 用户注册项模板管理，原PS：TZ_USER_REG_PKG:TZ_USER_MNG_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-07
 */
@Service("com.tranzvision.gd.TZLeaguerDataItemBundle.service.impl.TzLeaguerDataItemMgServiceImpl")
public class TzLeaguerDataItemMgServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TZGDObject tzGdObject;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private PsTzUserregMbTMapper psTzUserregMbTMapper;

	@Autowired
	private PsTzRegFieldTMapper psTzRegFieldTMapper;

	@Autowired
	private PsTzRegfieldEngMapper psTzRegfieldEngMapper;

	@Autowired
	private PsTzYhzcXxzTblMapper psTzYhzcXxzTblMapper;

	@Autowired
	private PsTzYhzcXxzEngMapper psTzYhzcXxzEngMapper;

	@Autowired
	private RegisteServiceImpl registeServiceImpl;

	/**
	 * 修改组件注册信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			Date dateNow = new Date();
			int dataLength = actData.length;
			String languageCd = "ENG";
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String operateType = jacksonUtil.getString("OperateType");
				String sql = "";
				if ("saveForm".equals(operateType)) {

					List<?> listFormData = jacksonUtil.getList("data");

					for (Object obj : listFormData) {

						Map<String, Object> mapFormData = (Map<String, Object>) obj;

						String updActivate = "";
						String comma = "";
						if (mapFormData.containsKey("activate")) {

							Object objActivate = mapFormData.get("activate");

							String strActivate = String.valueOf(objActivate);

							if (strActivate.contains(",")) {
								List<String> listActivate = (List<String>) objActivate;
								for (String activateType : listActivate) {
									updActivate = updActivate + comma + activateType;
									comma = ",";
								}
							} else {
								updActivate = strActivate;
							}

						}

						sql = "select 'Y' from PS_TZ_USERREG_MB_T where TZ_JG_ID=?";
						String recExists = sqlQuery.queryForObject(sql, new Object[] { orgid }, "String");

						if ("Y".equals(recExists)) {
							PsTzUserregMbT psTzUserregMbT = new PsTzUserregMbT();
							psTzUserregMbT.setTzJgId(orgid);
							psTzUserregMbT.setTzActivateType(updActivate);
							psTzUserregMbT.setRowLastmantDttm(dateNow);
							psTzUserregMbT.setRowLastmantOprid(oprid);
							psTzUserregMbTMapper.updateByPrimaryKeySelective(psTzUserregMbT);
						}

					}

				} else if ("saveGrid".equals(operateType)) {

					List<?> listGridData = jacksonUtil.getList("data");

					for (Object objGrid : listGridData) {

						Map<String, Object> mapGridRow = (Map<String, Object>) objGrid;

						String regId = "";
						if (mapGridRow.containsKey("regId")) {
							regId = String.valueOf(mapGridRow.get("regId"));
						}

						if ("".equals(regId)) {
							errMsg[0] = "1";
							errMsg[1] = "参数regId无值。";
							return "{\"success\":false}";
						}

						sql = "select 'Y' from PS_TZ_REG_FIELD_T where TZ_JG_ID=? and TZ_REG_FIELD_ID=?";
						String recExists = sqlQuery.queryForObject(sql, new Object[] { orgid, regId }, "String");
						if ("Y".equals(recExists)) {
							PsTzRegFieldT psTzRegFieldT = new PsTzRegFieldT();
							psTzRegFieldT.setTzJgId(orgid);
							psTzRegFieldT.setTzRegFieldId(regId);

							if (mapGridRow.containsKey("regName")) {
								String regName = String.valueOf(mapGridRow.get("regName"));
								psTzRegFieldT.setTzRegFieldName(regName);
								psTzRegFieldT.setTzRedFldYsmc(regName);
							}

							if (mapGridRow.containsKey("defaultValue")) {
								psTzRegFieldT.setTzDefVal(String.valueOf(mapGridRow.get("defaultValue")));
							}

							if (mapGridRow.containsKey("order")) {
								psTzRegFieldT.setTzOrder(Integer.parseInt(String.valueOf(mapGridRow.get("order"))));
							}

							if (mapGridRow.containsKey("isEnable")) {
								if (Boolean.parseBoolean(String.valueOf(mapGridRow.get("isEnable")))) {
									psTzRegFieldT.setTzEnable("Y");
								} else {
									psTzRegFieldT.setTzEnable("N");
								}
							}

							if (mapGridRow.containsKey("isRequired")) {
								if (Boolean.parseBoolean(String.valueOf(mapGridRow.get("isRequired")))) {
									psTzRegFieldT.setTzIsRequired("Y");
								} else {
									psTzRegFieldT.setTzIsRequired("N");
								}
							}

							if (mapGridRow.containsKey("regFieldType")) {
								psTzRegFieldT.setTzFieldType(String.valueOf(mapGridRow.get("regFieldType")));
							}

							if (mapGridRow.containsKey("isChange")) {
								psTzRegFieldT.setTzYxbgSrklx(String.valueOf(mapGridRow.get("isChange")));
							}

							if (mapGridRow.containsKey("isShowWzsy")) {
								if (Boolean.parseBoolean(String.valueOf(mapGridRow.get("isShowWzsy")))) {
									psTzRegFieldT.setTzIsShowwzsy("Y");
								} else {
									psTzRegFieldT.setTzIsShowwzsy("N");
								}
							}

							psTzRegFieldTMapper.updateByPrimaryKeySelective(psTzRegFieldT);

							// 处理双语 - 开始
							if (mapGridRow.containsKey("regEnName")) {

								String regEnName = mapGridRow.get("regEnName") == null ? ""
										: String.valueOf(mapGridRow.get("regEnName"));

								if (!"".equals(regId)) {
									sql = "select 'Y' from PS_TZ_REGFIELD_ENG where TZ_JG_ID=? and TZ_REG_FIELD_ID=? and LANGUAGE_CD=?";
									recExists = sqlQuery.queryForObject(sql, new Object[] { orgid, regId, languageCd },
											"String");

									PsTzRegfieldEng psTzRegfieldEng = new PsTzRegfieldEng();

									psTzRegfieldEng.setTzJgId(orgid);
									psTzRegfieldEng.setTzRegFieldId(regId);
									psTzRegfieldEng.setLanguageCd(languageCd);
									psTzRegfieldEng.setTzRegFieldName(regEnName);

									if (!"Y".equals(recExists)) {
										if (!"".equals(regEnName)) {
											psTzRegfieldEngMapper.insert(psTzRegfieldEng);
										}
									} else {
										psTzRegfieldEngMapper.updateByPrimaryKey(psTzRegfieldEng);
									}
								}

							}
							// 处理双语 - 结束

						}

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
	 * 表单查询
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		try {
			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String platformOrgID = getSysHardCodeVal.getPlatformOrgID();
			String languageCd = "ENG";

			String sql = "select count(1) from PS_TZ_USERREG_MB_T where TZ_JG_ID = ?";

			int numCount = sqlQuery.queryForObject(sql, new Object[] { orgid }, "int");

			if (numCount < 1) {
				// 初始化 用户注册信息模板表
				PsTzUserregMbT psTzUserregMbT = new PsTzUserregMbT();
				psTzUserregMbT.setTzJgId(orgid);
				psTzUserregMbT.setTzFabuState("N");
				psTzUserregMbT.setTzRegMbHtml("");
				psTzUserregMbT.setRowAddedDttm(dateNow);
				psTzUserregMbT.setRowAddedOprid(oprid);
				psTzUserregMbT.setRowLastmantDttm(dateNow);
				psTzUserregMbT.setRowLastmantOprid(oprid);
				psTzUserregMbTMapper.insert(psTzUserregMbT);

				// 初始化 用户注册项信息字段存储表
				sql = "select TZ_REG_FIELD_ID,TZ_RED_FLD_YSMC,TZ_REG_FIELD_NAME,TZ_ORDER,TZ_ENABLE,TZ_IS_REQUIRED,TZ_SYSFIELD_FLAG,TZ_FIELD_TYPE,TZ_DEF_VAL,TZ_YXBG_SRKLX,TZ_IS_SHOWWZSY from PS_TZ_REG_FIELD_T where TZ_JG_ID = ? order by TZ_ORDER";

				List<?> listData = sqlQuery.queryForList(sql, new Object[] { platformOrgID });

				for (Object obj : listData) {

					Map<String, Object> mapData = (Map<String, Object>) obj;

					String fieldId = mapData.get("TZ_REG_FIELD_ID") == null ? ""
							: String.valueOf(mapData.get("TZ_REG_FIELD_ID"));

					PsTzRegFieldT psTzRegFieldT = new PsTzRegFieldT();
					psTzRegFieldT.setTzJgId(orgid);
					psTzRegFieldT.setTzRegFieldId(fieldId);
					psTzRegFieldT.setTzRedFldYsmc(mapData.get("TZ_RED_FLD_YSMC") == null ? ""
							: String.valueOf(mapData.get("TZ_RED_FLD_YSMC")));
					psTzRegFieldT.setTzRegFieldName(mapData.get("TZ_REG_FIELD_NAME") == null ? ""
							: String.valueOf(mapData.get("TZ_REG_FIELD_NAME")));
					psTzRegFieldT.setTzOrder(mapData.get("TZ_ORDER") == null ? 0
							: Integer.parseInt(String.valueOf(mapData.get("TZ_ORDER"))));
					psTzRegFieldT.setTzEnable(
							mapData.get("TZ_ENABLE") == null ? "" : String.valueOf(mapData.get("TZ_ENABLE")));
					psTzRegFieldT.setTzIsRequired(
							mapData.get("TZ_IS_REQUIRED") == null ? "" : String.valueOf(mapData.get("TZ_IS_REQUIRED")));
					psTzRegFieldT.setTzSysfieldFlag(mapData.get("TZ_SYSFIELD_FLAG") == null ? ""
							: String.valueOf(mapData.get("TZ_SYSFIELD_FLAG")));
					psTzRegFieldT.setTzFieldType(
							mapData.get("TZ_FIELD_TYPE") == null ? "" : String.valueOf(mapData.get("TZ_FIELD_TYPE")));
					psTzRegFieldT.setTzDefVal(
							mapData.get("TZ_DEF_VAL") == null ? "" : String.valueOf(mapData.get("TZ_DEF_VAL")));
					psTzRegFieldT.setTzYxbgSrklx(
							mapData.get("TZ_YXBG_SRKLX") == null ? "" : String.valueOf(mapData.get("TZ_YXBG_SRKLX")));
					psTzRegFieldT.setTzIsShowwzsy(
							mapData.get("TZ_IS_SHOWWZSY") == null ? "" : String.valueOf(mapData.get("TZ_IS_SHOWWZSY")));

					psTzRegFieldTMapper.insert(psTzRegFieldT);

					// 处理英文名称部分 - 开始
					sql = "select TZ_REG_FIELD_NAME from PS_TZ_REGFIELD_ENG where TZ_JG_ID = ? and LANGUAGE_CD = ?  and TZ_REG_FIELD_ID = ?";
					String fieldEnName = sqlQuery.queryForObject(sql,
							new Object[] { platformOrgID, languageCd, fieldId }, "String");

					if (null != fieldEnName && !"".equals(fieldEnName)) {
						PsTzRegfieldEng psTzRegfieldEng = new PsTzRegfieldEng();
						psTzRegfieldEng.setTzJgId(orgid);
						psTzRegfieldEng.setTzRegFieldId(fieldId);
						psTzRegfieldEng.setLanguageCd(languageCd);
						psTzRegfieldEng.setTzRegFieldName(fieldEnName);
						psTzRegfieldEngMapper.insert(psTzRegfieldEng);
					}
					// 处理英文名称部分 - 结束
				}

				// 初始化 用户注册信息字段选项值表
				sql = "select TZ_REG_FIELD_ID,TZ_OPT_ID,TZ_OPT_VALUE,TZ_SELECT_FLG,TZ_ORDER from PS_TZ_YHZC_XXZ_TBL where TZ_JG_ID = ? order by TZ_ORDER";
				List<?> listDataLng = sqlQuery.queryForList(sql, new Object[] { platformOrgID });
				for (Object obj : listDataLng) {

					Map<String, Object> mapData = (Map<String, Object>) obj;

					String fieldId = mapData.get("TZ_REG_FIELD_ID") == null ? ""
							: String.valueOf(mapData.get("TZ_REG_FIELD_ID"));
					String optId = mapData.get("TZ_OPT_ID") == null ? "" : String.valueOf(mapData.get("TZ_OPT_ID"));

					PsTzYhzcXxzTbl psTzYhzcXxzTbl = new PsTzYhzcXxzTbl();
					psTzYhzcXxzTbl.setTzJgId(orgid);
					psTzYhzcXxzTbl.setTzRegFieldId(fieldId);
					psTzYhzcXxzTbl.setTzOptId(optId);
					psTzYhzcXxzTbl.setTzOptValue(
							mapData.get("TZ_OPT_VALUE") == null ? "" : String.valueOf(mapData.get("TZ_OPT_VALUE")));
					psTzYhzcXxzTbl.setTzSelectFlg(
							mapData.get("TZ_SELECT_FLG") == null ? "" : String.valueOf(mapData.get("TZ_SELECT_FLG")));
					psTzYhzcXxzTbl.setTzOrder(mapData.get("TZ_ORDER") == null ? 0
							: Integer.parseInt(String.valueOf(mapData.get("TZ_ORDER"))));

					psTzYhzcXxzTblMapper.insert(psTzYhzcXxzTbl);

					// 处理英文描述部分 - 开始

					sql = "select TZ_OPT_VALUE from PS_TZ_YHZC_XXZ_ENG where TZ_JG_ID = ? and LANGUAGE_CD = ? and TZ_REG_FIELD_ID = ? and TZ_OPT_ID = ?";
					String optEnValue = sqlQuery.queryForObject(sql,
							new Object[] { platformOrgID, languageCd, fieldId, optId }, "String");
					if (null != optEnValue && !"".equals(optEnValue)) {

						PsTzYhzcXxzEng psTzYhzcXxzEng = new PsTzYhzcXxzEng();

						psTzYhzcXxzEng.setTzJgId(orgid);
						psTzYhzcXxzEng.setTzRegFieldId(fieldId);
						psTzYhzcXxzEng.setTzOptId(optId);
						psTzYhzcXxzEng.setLanguageCd(languageCd);
						psTzYhzcXxzEng.setTzOptValue(optEnValue);

						psTzYhzcXxzEngMapper.insert(psTzYhzcXxzEng);
					}
					// 处理英文描述部分 - 结束

				}

			}

			sql = "select TZ_FABU_STATE,TZ_ACTIVATE_TYPE from PS_TZ_USERREG_MB_T where TZ_JG_ID = ?";
			Map<String, Object> mapUserRegMb = sqlQuery.queryForMap(sql, new Object[] { orgid });
			String strState = String.valueOf(mapUserRegMb.get("TZ_FABU_STATE"));
			if ("Y".equals(strState)) {
				strState = "已发布";
			} else {
				strState = "未发布";
			}

			String activateType = mapUserRegMb.get("TZ_ACTIVATE_TYPE") == null ? ""
					: String.valueOf(mapUserRegMb.get("TZ_ACTIVATE_TYPE"));

			String[] listActivateType = activateType.split(",");

			Map<String, Object> mapRetFormData = new HashMap<String, Object>();
			mapRetFormData.put("id", orgid);
			mapRetFormData.put("name", orgid);
			mapRetFormData.put("state", strState);
			mapRetFormData.put("activate", listActivateType);

			sql = "select TZ_REG_FIELD_ID,TZ_REG_FIELD_NAME,TZ_ORDER,TZ_ENABLE,TZ_IS_REQUIRED,TZ_SYSFIELD_FLAG,TZ_FIELD_TYPE,TZ_DEF_VAL,TZ_YXBG_SRKLX,TZ_IS_SHOWWZSY from PS_TZ_REG_FIELD_T where TZ_JG_ID = ? order by TZ_ORDER";

			List<?> listRegField = sqlQuery.queryForList(sql, new Object[] { orgid });

			ArrayList<Map<String, Object>> listRetJson = new ArrayList<Map<String, Object>>();

			for (Object obj : listRegField) {
				Map<String, Object> mapRegField = (Map<String, Object>) obj;

				String regId = mapRegField.get("TZ_REG_FIELD_ID") == null ? ""
						: String.valueOf(mapRegField.get("TZ_REG_FIELD_ID"));
				String regName = mapRegField.get("TZ_REG_FIELD_NAME") == null ? ""
						: String.valueOf(mapRegField.get("TZ_REG_FIELD_NAME"));
				int order = Integer.parseInt(String.valueOf(mapRegField.get("TZ_ORDER")));
				String isEnable = mapRegField.get("TZ_ENABLE") == null ? ""
						: String.valueOf(mapRegField.get("TZ_ENABLE"));
				String isRequired = mapRegField.get("TZ_IS_REQUIRED") == null ? ""
						: String.valueOf(mapRegField.get("TZ_IS_REQUIRED"));
				String isSysField = mapRegField.get("TZ_SYSFIELD_FLAG") == null ? ""
						: String.valueOf(mapRegField.get("TZ_SYSFIELD_FLAG"));
				String regFieldType = mapRegField.get("TZ_FIELD_TYPE") == null ? ""
						: String.valueOf(mapRegField.get("TZ_FIELD_TYPE"));
				String defaultValue = mapRegField.get("TZ_DEF_VAL") == null ? ""
						: String.valueOf(mapRegField.get("TZ_DEF_VAL"));
				String isChange = mapRegField.get("TZ_YXBG_SRKLX") == null ? ""
						: String.valueOf(mapRegField.get("TZ_YXBG_SRKLX"));
				String isShowWzsy = mapRegField.get("TZ_IS_SHOWWZSY") == null ? ""
						: String.valueOf(mapRegField.get("TZ_IS_SHOWWZSY"));

				boolean enable = false;
				if ("Y".equals(isEnable)) {
					enable = true;
				}

				boolean required = false;
				if ("Y".equals(isRequired)) {
					required = true;
				}

				boolean showWzsy = false;
				if ("Y".equals(isShowWzsy)) {
					showWzsy = true;
				}

				sql = "select TZ_REG_FIELD_NAME from PS_TZ_REGFIELD_ENG where TZ_JG_ID = ? and TZ_REG_FIELD_ID = ? and LANGUAGE_CD = ?";
				String regEnName = sqlQuery.queryForObject(sql, new Object[] { orgid, regId, languageCd }, "String");

				Map<String, Object> mapRetJson = new HashMap<String, Object>();
				mapRetJson.put("regId", regId);
				mapRetJson.put("regName", regName);
				mapRetJson.put("order", order);
				mapRetJson.put("isEnable", enable);
				mapRetJson.put("isRequired", required);
				mapRetJson.put("isSysField", isSysField);
				mapRetJson.put("regFieldType", regFieldType);
				mapRetJson.put("defaultValue", defaultValue);
				mapRetJson.put("isChange", isChange);
				mapRetJson.put("isShowWzsy", showWzsy);
				mapRetJson.put("regEnName", regEnName);

				listRetJson.add(mapRetJson);

			}

			Map<String, Object> mapRet = new HashMap<String, Object>();

			mapRet.put("formData", mapRetFormData);
			mapRet.put("listData", listRetJson);

			JacksonUtil jacksonUtil = new JacksonUtil();
			strRet = jacksonUtil.Map2json(mapRet);

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		try {

			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String languageCd = "ENG";

			String sql = "select TZ_REG_FIELD_ID,TZ_REG_FIELD_NAME,TZ_ORDER,TZ_ENABLE,TZ_IS_REQUIRED,TZ_SYSFIELD_FLAG,TZ_FIELD_TYPE,TZ_DEF_VAL,TZ_YXBG_SRKLX,TZ_IS_SHOWWZSY from PS_TZ_REG_FIELD_T where TZ_JG_ID = ? order by TZ_ORDER";

			List<?> listRegField = sqlQuery.queryForList(sql, new Object[] { orgid });

			ArrayList<Map<String, Object>> listRetJson = new ArrayList<Map<String, Object>>();

			for (Object obj : listRegField) {
				Map<String, Object> mapRegField = (Map<String, Object>) obj;

				String regId = mapRegField.get("TZ_REG_FIELD_ID") == null ? ""
						: String.valueOf(mapRegField.get("TZ_REG_FIELD_ID"));
				String regName = mapRegField.get("TZ_REG_FIELD_NAME") == null ? ""
						: String.valueOf(mapRegField.get("TZ_REG_FIELD_NAME"));
				int order = Integer.parseInt(String.valueOf(mapRegField.get("TZ_ORDER")));
				String isEnable = mapRegField.get("TZ_ENABLE") == null ? ""
						: String.valueOf(mapRegField.get("TZ_ENABLE"));
				String isRequired = mapRegField.get("TZ_IS_REQUIRED") == null ? ""
						: String.valueOf(mapRegField.get("TZ_IS_REQUIRED"));
				String isSysField = mapRegField.get("TZ_SYSFIELD_FLAG") == null ? ""
						: String.valueOf(mapRegField.get("TZ_SYSFIELD_FLAG"));
				String regFieldType = mapRegField.get("TZ_FIELD_TYPE") == null ? ""
						: String.valueOf(mapRegField.get("TZ_FIELD_TYPE"));
				String defaultValue = mapRegField.get("TZ_DEF_VAL") == null ? ""
						: String.valueOf(mapRegField.get("TZ_DEF_VAL"));
				String isChange = mapRegField.get("TZ_YXBG_SRKLX") == null ? ""
						: String.valueOf(mapRegField.get("TZ_YXBG_SRKLX"));
				String isShowWzsy = mapRegField.get("TZ_IS_SHOWWZSY") == null ? ""
						: String.valueOf(mapRegField.get("TZ_IS_SHOWWZSY"));

				boolean enable = false;
				if ("Y".equals(isEnable)) {
					enable = true;
				}

				boolean required = false;
				if ("Y".equals(isRequired)) {
					required = true;
				}

				boolean ShowWzsy = false;
				if ("Y".equals(isShowWzsy)) {
					ShowWzsy = true;
				}

				sql = "select TZ_REG_FIELD_NAME from PS_TZ_REGFIELD_ENG where TZ_JG_ID = ? and TZ_REG_FIELD_ID = ? and LANGUAGE_CD = ?";
				String regEnName = sqlQuery.queryForObject(sql, new Object[] { orgid, regId, languageCd }, "String");

				Map<String, Object> mapRetJson = new HashMap<String, Object>();
				mapRetJson.put("regId", regId);
				mapRetJson.put("regName", regName);
				mapRetJson.put("order", order);
				mapRetJson.put("isEnable", enable);
				mapRetJson.put("isRequired", required);
				mapRetJson.put("isSysField", isSysField);
				mapRetJson.put("regFieldType", regFieldType);
				mapRetJson.put("defaultValue", defaultValue);
				mapRetJson.put("isChange", isChange);
				mapRetJson.put("isShowWzsy", ShowWzsy);
				mapRetJson.put("regEnName", regEnName);

				listRetJson.add(mapRetJson);

			}

			mapRet.replace("total", listRegField.size());
			mapRet.replace("root", listRetJson);

		} catch (Exception e) {
			e.printStackTrace();
			mapRet.clear();
			mapRet.put("success", false);
			errorMsg[0] = "1";
			errorMsg[1] = "数据异常，请重试！";
		}

		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(mapRet);

	}

	/**
	 * 预览
	 */
	@Override
	public String tzGetHtmlContent(String comParams) {

		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

		String sql = "select TZ_SITEI_ID from PS_TZ_SITEI_DEFN_T where TZ_JG_ID=? and TZ_SITEI_ENABLE='Y'";

		String strSiteId = sqlQuery.queryForObject(sql, new Object[] { orgid }, "String");

		String viewHtml = registeServiceImpl.userRegister(orgid, strSiteId);
		try {
			viewHtml = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_USER_REG_PREVIEW_HTML",viewHtml,
					request.getContextPath(), strSiteId);
		} catch (TzSystemException e) {
			viewHtml = e.toString();
			e.printStackTrace();
		}

		return viewHtml;

	}

}
