package com.tranzvision.gd.TZApplicationTemplateBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZApplicationTemplateBundle.dao.PsTzAppEventsTMapper;
import com.tranzvision.gd.TZApplicationTemplateBundle.dao.PsTzAppXxJygzTMapper;
import com.tranzvision.gd.TZApplicationTemplateBundle.dao.PsTzAppXxxKxzTMapper;
import com.tranzvision.gd.TZApplicationTemplateBundle.dao.PsTzAppXxxPzTMapper;
import com.tranzvision.gd.TZApplicationTemplateBundle.dao.PsTzApptplDyTMapper;
import com.tranzvision.gd.TZApplicationTemplateBundle.dao.PsTzRqXxxPzTMapper;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzAppEventsT;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzAppEventsTKey;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzAppXxJygzT;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzAppXxJygzTKey;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzAppXxxKxzT;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzAppXxxKxzTKey;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzAppXxxPzTKey;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzAppXxxPzTWithBLOBs;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzApptplDyTWithBLOBs;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzRqXxxPzT;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzRqXxxPzTKey;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.poi.excel.ExcelHandle;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * @author WRL TZ_ONLINE_REG_PKG:TZ_ONREG_MNG_CLS 报名表模板管理
 */
@Service("com.tranzvision.gd.TZApplicationTemplateBundle.service.impl.AppFormListClsServiceImpl")
public class AppFormListClsServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private TZGDObject tzSQLObject;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private PsTzApptplDyTMapper psTzApptplDyTMapper;

	@Autowired
	private PsTzAppEventsTMapper psTzAppEventsTMapper;

	@Autowired
	private PsTzRqXxxPzTMapper psTzRqXxxPzTMapper;

	@Autowired
	private PsTzAppXxxPzTMapper psTzAppXxxPzTMapper;

	@Autowired
	private PsTzAppXxJygzTMapper psTzAppXxJygzTMapper;

	@Autowired
	private PsTzAppXxxKxzTMapper psTzAppXxxKxzTMapper;

	@Autowired
	private TemplateEngine templateEngine;

	/* 查询报名表列表 */
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
			String[][] orderByArr = new String[][] {};

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_APP_TPL_ID", "TZ_APP_TPL_MC", "TZ_EFFEXP_ZT" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();

					mapList.put("tplid", rowList[0]);
					mapList.put("tplname", rowList[1]);
					mapList.put("activestate", rowList[2]);
					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);
	}

	/**
	 * 新增報名表
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public String tzAdd(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			Date dateNow = new Date();
			JacksonUtil jacksonUtil = new JacksonUtil();

			int num = 0;
			for (num = 0; num < actData.length; num++) {
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				String id = jacksonUtil.getString("id");
				String name = jacksonUtil.getString("name");
				String language = jacksonUtil.getString("language");

				if (StringUtils.isBlank(name)) {
					errMsg[0] = "1";
					errMsg[1] = "未提供新的模板名称！";
					return strRet;
				} else {
					String sql = "SELECT 'Y' FROM PS_TZ_APPTPL_DY_T WHERE TZ_JG_ID = ? AND TZ_APP_TPL_MC = ?";
					String isSame = sqlQuery.queryForObject(sql, new Object[] { orgId, name }, "String");
					if (StringUtils.equals("Y", isSame)) {
						errMsg[0] = "1";
						errMsg[1] = "模板名称重复！";
						return strRet;
					}
				}

				if (StringUtils.isBlank(language)) {
					language = tzLoginServiceImpl.getSysLanaguageCD(request);
				}

				String newTplId = "" + getSeqNum.getSeqNum("TZ_APPTPL_DY_T", "TZ_APP_TPL_ID");
				if (StringUtils.isBlank(id)) {
					// 新增空白模板
					Map<String, Object> mapTplDef = new HashMap<String, Object>();
					mapTplDef.put("tplId", newTplId);
					mapTplDef.put("deptId", orgId);
					mapTplDef.put("deptName", orgId);
					mapTplDef.put("tplName", name);
					mapTplDef.put("state", "Y");
					mapTplDef.put("tplDesc", name);
					mapTplDef.put("lang", language);
					mapTplDef.put("tplUse", "REG");
					mapTplDef.put("tplUseType", "BMB");
					// add by caoy @2016-6-13
					mapTplDef.put("tpPdfType", "HPDF");
					// add by caoy @2017-1-21 增加推荐信开启密码 默认不开启
					mapTplDef.put("tpPwdType", "N");

					mapTplDef.put("labelPostion", "UP");
					mapTplDef.put("showType", "POP");
					mapTplDef.put("printTplName", "");
					mapTplDef.put("targetType", "TOP");
					mapTplDef.put("redirectUrl", "");
					mapTplDef.put("filename", "");
					mapTplDef.put("sysFileName", "");
					mapTplDef.put("path", "");
					mapTplDef.put("accessPath", "");
					mapTplDef.put("events", new HashMap<String, Object>());
					mapTplDef.put("items", new HashMap<String, Object>());
					String tzApptplJsonStr = jacksonUtil.Map2json(mapTplDef);

					PsTzApptplDyTWithBLOBs psTzApptplDyT = new PsTzApptplDyTWithBLOBs();
					psTzApptplDyT.setTzAppTplId(newTplId);
					psTzApptplDyT.setTzJgId(orgId);
					psTzApptplDyT.setTzAppTplMc(name);
					psTzApptplDyT.setTzAppTplMs(name);
					psTzApptplDyT.setTzAppTplYt("REG");
					psTzApptplDyT.setTzUseType("BMB");
					// add by caoy @2016-6-13
					psTzApptplDyT.setTzPdfType("HPDF");
					psTzApptplDyT.setTzPwdType("N");
					psTzApptplDyT.setTzAppLabelWz("UP");
					psTzApptplDyT.setTzAppTsxxFs("POP");
					psTzApptplDyT.setTzAppTplLan(language);
					psTzApptplDyT.setTzAppTzfs("TOP");
					psTzApptplDyT.setTzAppTzurl("");
					psTzApptplDyT.setTzEffexpZt("Y");
					psTzApptplDyT.setTzApptplJsonStr(tzApptplJsonStr);
					psTzApptplDyT.setRowAddedDttm(dateNow);
					psTzApptplDyT.setRowAddedOprid(oprid);
					psTzApptplDyT.setRowLastmantDttm(dateNow);
					psTzApptplDyT.setRowLastmantOprid(oprid);

					int i = psTzApptplDyTMapper.insert(psTzApptplDyT);
					if (i > 0) {
						Map<String, Object> mapRet = new HashMap<String, Object>();
						mapRet.put("id", newTplId);
						strRet = jacksonUtil.Map2json(mapRet);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "创建新的模板失败！";
						return strRet;
					}
				} else {
					// 从其他模板复制模板
					PsTzApptplDyTWithBLOBs psTzApptplDyT = psTzApptplDyTMapper.selectByPrimaryKey(id);
					if (psTzApptplDyT != null) {
						psTzApptplDyT.setTzAppTplId(newTplId);
						psTzApptplDyT.setTzAppTplMc(name);
						psTzApptplDyT.setTzAppTplMs(name);
						psTzApptplDyT.setTzJgId(orgId);
						psTzApptplDyT.setRowAddedDttm(dateNow);
						psTzApptplDyT.setRowAddedOprid(oprid);
						psTzApptplDyT.setRowLastmantDttm(dateNow);
						psTzApptplDyT.setRowLastmantOprid(oprid);
						String tzApptplJsonStr = psTzApptplDyT.getTzApptplJsonStr();
						jacksonUtil.json2Map(tzApptplJsonStr);
						Map<String, Object> jsonMap = jacksonUtil.getMap();

						jsonMap.put("deptName", orgId);
						jsonMap.put("tplName", name);
						jsonMap.put("tplDesc", name);
						psTzApptplDyT.setTzApptplJsonStr(jacksonUtil.Map2json(jsonMap));
						int i = psTzApptplDyTMapper.insert(psTzApptplDyT);
						if (i > 0) {
							Map<String, Object> mapRet = new HashMap<String, Object>();
							mapRet.put("id", newTplId);
							strRet = jacksonUtil.Map2json(mapRet);
						} else {
							errMsg[0] = "1";
							errMsg[1] = "创建新的模板失败！";
							return strRet;
						}

						/* 报名表保存、提交事件列表 BEGIN */
						String sql = "SELECT TZ_EVENT_ID FROM PS_TZ_APP_EVENTS_T WHERE TZ_APP_TPL_ID = ?";
						// 查询结果
						List<?> resultlist = sqlQuery.queryForList(sql, new Object[] { id });

						for (Object obj : resultlist) {
							Map<String, Object> result = (Map<String, Object>) obj;

							String eventId = result.get("TZ_EVENT_ID") == null ? ""
									: String.valueOf(result.get("TZ_EVENT_ID"));
							PsTzAppEventsTKey psTzAppEventsTKey = new PsTzAppEventsTKey();
							psTzAppEventsTKey.setTzAppTplId(id);
							psTzAppEventsTKey.setTzEventId(eventId);
							PsTzAppEventsT psTzAppEventsT = psTzAppEventsTMapper.selectByPrimaryKey(psTzAppEventsTKey);

							psTzAppEventsT.setTzAppTplId(newTplId);
							psTzAppEventsTMapper.insert(psTzAppEventsT);
						}
						/* 报名表保存、提交事件列表 END */

						/* 容器信息项配置表 BEGIN */
						String sqlRq = "SELECT TZ_D_XXX_BH,TZ_XXX_BH FROM PS_TZ_RQ_XXXPZ_T WHERE TZ_APP_TPL_ID = ?";
						// 查询结果
						List<?> resultRqlist = sqlQuery.queryForList(sqlRq, new Object[] { id });

						for (Object obj : resultRqlist) {
							Map<String, Object> result = (Map<String, Object>) obj;

							String dBh = result.get("TZ_D_XXX_BH") == null ? ""
									: String.valueOf(result.get("TZ_D_XXX_BH"));
							String bh = result.get("TZ_XXX_BH") == null ? "" : String.valueOf(result.get("TZ_XXX_BH"));
							PsTzRqXxxPzTKey psTzRqXxxPzTKey = new PsTzRqXxxPzTKey();
							psTzRqXxxPzTKey.setTzAppTplId(id);
							psTzRqXxxPzTKey.setTzDXxxBh(dBh);
							psTzRqXxxPzTKey.setTzXxxBh(bh);
							PsTzRqXxxPzT psTzRqXxxPzT = psTzRqXxxPzTMapper.selectByPrimaryKey(psTzRqXxxPzTKey);

							psTzRqXxxPzT.setTzAppTplId(newTplId);
							psTzRqXxxPzTMapper.insert(psTzRqXxxPzT);
						}
						/* 容器信息项配置表 END */

						/* 报名表模板信息项配置表 BEGIN */
						String sqlPz = "SELECT TZ_XXX_BH FROM PS_TZ_APP_XXXPZ_T WHERE TZ_APP_TPL_ID = ?";
						// 查询结果
						List<?> resultPzlist = sqlQuery.queryForList(sqlPz, new Object[] { id });

						for (Object obj : resultPzlist) {
							Map<String, Object> result = (Map<String, Object>) obj;

							String bh = result.get("TZ_XXX_BH") == null ? "" : String.valueOf(result.get("TZ_XXX_BH"));

							PsTzAppXxxPzTKey psTzAppXxxPzTKey = new PsTzAppXxxPzTKey();
							psTzAppXxxPzTKey.setTzAppTplId(id);
							psTzAppXxxPzTKey.setTzXxxBh(bh);
							PsTzAppXxxPzTWithBLOBs psTzAppXxxPz = psTzAppXxxPzTMapper
									.selectByPrimaryKey(psTzAppXxxPzTKey);

							psTzAppXxxPz.setTzAppTplId(newTplId);
							psTzAppXxxPzTMapper.insert(psTzAppXxxPz);
						}
						/* 报名表模板信息项配置表 END */

						/* 报名表模板信息项校验规则表 BEGIN */
						String sqlJygz = "SELECT TZ_XXX_BH,TZ_JYGZ_ID FROM PS_TZ_APPXX_JYGZ_T WHERE TZ_APP_TPL_ID = ?";
						// 查询结果
						List<?> resultJygzlist = sqlQuery.queryForList(sqlJygz, new Object[] { id });

						for (Object obj : resultJygzlist) {
							Map<String, Object> result = (Map<String, Object>) obj;

							String bh = result.get("TZ_XXX_BH") == null ? "" : String.valueOf(result.get("TZ_XXX_BH"));
							String gzId = result.get("TZ_JYGZ_ID") == null ? ""
									: String.valueOf(result.get("TZ_JYGZ_ID"));

							PsTzAppXxJygzTKey psTzAppXxJygzTKey = new PsTzAppXxJygzTKey();
							psTzAppXxJygzTKey.setTzAppTplId(id);
							psTzAppXxJygzTKey.setTzXxxBh(bh);
							psTzAppXxJygzTKey.setTzJygzId(gzId);

							PsTzAppXxJygzT psTzAppXxJygzT = psTzAppXxJygzTMapper.selectByPrimaryKey(psTzAppXxJygzTKey);

							psTzAppXxJygzT.setTzAppTplId(newTplId);
							psTzAppXxJygzTMapper.insert(psTzAppXxJygzT);
						}
						/* 报名表模板信息项校验规则表 END */

						/* 报名表模板信息项可选值定义表 BEGIN */
						String sqlKxz = "SELECT TZ_XXX_BH,TZ_XXXKXZ_MC FROM PS_TZ_APPXXX_KXZ_T WHERE TZ_APP_TPL_ID = ?";
						// 查询结果
						List<?> resultKxzlist = sqlQuery.queryForList(sqlKxz, new Object[] { id });

						for (Object obj : resultKxzlist) {
							Map<String, Object> result = (Map<String, Object>) obj;

							String bh = result.get("TZ_XXX_BH") == null ? "" : String.valueOf(result.get("TZ_XXX_BH"));
							String kxzId = result.get("TZ_XXXKXZ_MC") == null ? ""
									: String.valueOf(result.get("TZ_XXXKXZ_MC"));

							PsTzAppXxxKxzTKey psTzAppXxxKxzTKey = new PsTzAppXxxKxzTKey();
							psTzAppXxxKxzTKey.setTzAppTplId(id);
							psTzAppXxxKxzTKey.setTzXxxBh(bh);
							psTzAppXxxKxzTKey.setTzXxxkxzMc(kxzId);

							PsTzAppXxxKxzT psTzAppXxxKxzT = psTzAppXxxKxzTMapper.selectByPrimaryKey(psTzAppXxxKxzTKey);

							psTzAppXxxKxzT.setTzAppTplId(newTplId);
							psTzAppXxxKxzTMapper.insert(psTzAppXxxKxzT);
						}
						/* 报名表模板信息项可选值定义表 END */
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

	@SuppressWarnings("unchecked")
	@Override
	public String tzGetJsonData(String strParams) {
		String strRet = "{}";

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String oType = jacksonUtil.getString("OType");

		if (StringUtils.isBlank(oType)) {
			return strRet;
		}
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String oprId = tzLoginServiceImpl.getLoginedManagerOprid(request);

		System.out.println("strParams:" + strParams);

		/* 推荐人姓名 */
		if (StringUtils.equals(oType, "RNAME")) {
			String userName = "";
			String insid = jacksonUtil.getString("insid");
			if (StringUtils.isNotBlank(insid) && StringUtils.length(insid) > 0) {
				String sqlTjx = "SELECT TZ_APP_INS_ID FROM PS_TZ_KS_TJX_TBL WHERE TZ_TJX_APP_INS_ID = ? LIMIT 1";
				insid = sqlQuery.queryForObject(sqlTjx, new Object[] { insid }, "String");

				String sqlOprid = "SELECT OPRID FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID = ? ORDER BY OPRID LIMIT 1";
				String refereesOprid = sqlQuery.queryForObject(sqlOprid, new Object[] { insid }, "String");

				String sqlRealName = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID = ?";
				userName = sqlQuery.queryForObject(sqlRealName, new Object[] { refereesOprid }, "String");
			}
			if (userName == null) {
				userName = "";
			}
			Map<String, Object> mapRet = new HashMap<String, Object>();
			mapRet.put("refName", userName);

			return jacksonUtil.Map2json(mapRet);
		}
		/* 考生基本信息 */
		if (StringUtils.equals(oType, "KSPHOTO")) {
			// 判断当前登录人是否有个人照片信息
			String ishasSql = "SELECT 'Y' FROM PS_TZ_OPR_PHT_GL_T WHERE OPRID = ?";
			String isHas = sqlQuery.queryForObject(ishasSql, new Object[] { oprId }, "String");
			if (StringUtils.equals("Y", isHas)) {
				String sql = "SELECT PH.TZ_ATTACHSYSFILENA,PH.TZ_ATTACHFILE_NAME,PH.TZ_ATT_P_URL,PH.TZ_ATT_A_URL FROM PS_TZ_OPR_PHT_GL_T GL, PS_TZ_OPR_PHOTO_T PH WHERE GL.TZ_ATTACHSYSFILENA = PH.TZ_ATTACHSYSFILENA AND GL.OPRID = ?";
				Map<String, Object> photoMap = sqlQuery.queryForMap(sql, new Object[] { oprId });

				String sysfilename = photoMap.get("TZ_ATTACHSYSFILENA") == null ? ""
						: String.valueOf(photoMap.get("TZ_ATTACHSYSFILENA"));
				String filename = photoMap.get("TZ_ATTACHFILE_NAME") == null ? ""
						: String.valueOf(photoMap.get("TZ_ATTACHFILE_NAME"));
				String path = photoMap.get("TZ_ATT_P_URL") == null ? "" : String.valueOf(photoMap.get("TZ_ATT_P_URL"));
				String imapath = photoMap.get("TZ_ATTACHSYSFILENA") == null ? ""
						: String.valueOf(photoMap.get("TZ_ATT_A_URL"));

				String photo = imapath + sysfilename;

				Map<String, Object> mapRet = new HashMap<String, Object>();
				mapRet.put("photo", photo);
				mapRet.put("sysFileName", sysfilename);
				mapRet.put("filename", filename);
				mapRet.put("path", path);
				mapRet.put("imaPath", imapath);

				return jacksonUtil.Map2json(mapRet);
			}
			return strRet;
		}

		/* 考生班级信息 */
		if (StringUtils.equals(oType, "CLASSINFO")) {
			String classId = jacksonUtil.getString("CLASSID");

			String sqlClass = "SELECT TZ_CLASS_NAME FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID = ?";
			String className = sqlQuery.queryForObject(sqlClass, new Object[] { classId }, "String");

			Map<String, Object> mapRet = new HashMap<String, Object>();
			mapRet.put("classCode", classId);
			mapRet.put("className", className);

			return jacksonUtil.Map2json(mapRet);
		}

		/* 专业方向 */
		if (StringUtils.equals(oType, "MAJOR")) {
			String classId = jacksonUtil.getString("CLASSID");
			String sqlMajor = "SELECT TZ_MAJOR_ID, TZ_MAJOR_NAME FROM PS_TZ_CLS_MAJOR_T WHERE TZ_CLASS_ID = ? ORDER BY TZ_SORT_NUM";
			List<?> resultlist = sqlQuery.queryForList(sqlMajor, new Object[] { classId });

			Map<String, Object> mapRet = new LinkedHashMap<String, Object>();
			for (Object obj : resultlist) {
				Map<String, Object> result = (Map<String, Object>) obj;

				String optId = "A" + Math.round(Math.random() * 899999999 + 100000000);
				String majorId = result.get("TZ_MAJOR_ID") == null ? "" : String.valueOf(result.get("TZ_MAJOR_ID"));
				String majorName = result.get("TZ_MAJOR_NAME") == null ? ""
						: String.valueOf(result.get("TZ_MAJOR_NAME"));

				Map<String, Object> mapOptJson = new HashMap<String, Object>();
				mapOptJson.put("code", majorId);
				mapOptJson.put("txt", majorName);
				mapOptJson.put("orderby", 0);
				mapOptJson.put("defaultval", "N");
				mapOptJson.put("other", "N");
				mapOptJson.put("weight", 0);
				mapRet.put(optId, mapOptJson);
			}
			return jacksonUtil.Map2json(mapRet);
		}

		/* 班级批次   modity by caoy 增加批次时间的处理*/
		if (StringUtils.equals(oType, "BATCH")) {
			String classId = jacksonUtil.getString("CLASSID");
			String sqlBatch = "SELECT B.TZ_BATCH_ID,B.TZ_BATCH_NAME FROM PS_TZ_CLASS_INF_T C, PS_TZ_CLS_BATCH_T B WHERE C.TZ_CLASS_ID = B.TZ_CLASS_ID AND C.TZ_IS_SUB_BATCH = 'Y' AND B.TZ_APP_PUB_STATUS = 'Y' AND C.TZ_CLASS_ID = ? AND B.TZ_APP_END_DT >= current_date() ORDER BY B.TZ_BATCH_ID";
			List<?> resultlist = sqlQuery.queryForList(sqlBatch, new Object[] { classId });

			Map<String, Object> mapRet = new HashMap<String, Object>();
			for (Object obj : resultlist) {
				Map<String, Object> result = (Map<String, Object>) obj;

				String optId = "A" + Math.round(Math.random() * 899999999 + 100000000);
				String batchId = result.get("TZ_BATCH_ID") == null ? "" : String.valueOf(result.get("TZ_BATCH_ID"));
				String batchName = result.get("TZ_BATCH_NAME") == null ? ""
						: String.valueOf(result.get("TZ_BATCH_NAME"));

				Map<String, Object> mapOptJson = new HashMap<String, Object>();
				mapOptJson.put("code", batchId);
				mapOptJson.put("txt", batchName);
				mapOptJson.put("orderby", 0);
				mapOptJson.put("defaultval", "N");
				mapOptJson.put("other", "N");
				mapOptJson.put("weight", 0);
				mapRet.put(optId, mapOptJson);
			}
			return jacksonUtil.Map2json(mapRet);
		}

		/* 查看后台管理员上传的附件 */
		if (StringUtils.equals(oType, "VIEWATTACH")) {

			String instanceId = jacksonUtil.getString("INSTANCEID");
			String tplId = sqlQuery.queryForObject("SELECT TZ_APP_TPL_ID FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID =?",
					new Object[] { instanceId }, "String");
			try {
				List<?> resultlist = sqlQuery.queryForList(
						tzSQLObject.getSQLText("SQL.TZApplicationTemplateBundle.TzGetAdminAttachment"),
						new Object[] { instanceId, tplId, instanceId });
				ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
				int i = 0;
				for (Object obj : resultlist) {
					Map<String, Object> result = (Map<String, Object>) obj;

					String itemId = result.get("TZ_XXX_BH") == null ? "" : String.valueOf(result.get("TZ_XXX_BH"));
					String fileName = result.get("ATTACHUSERFILE") == null ? ""
							: String.valueOf(result.get("ATTACHUSERFILE"));
					String sysFileName = result.get("ATTACHSYSFILENAME") == null ? ""
							: String.valueOf(result.get("ATTACHSYSFILENAME"));
					String accessPath = result.get("TZ_ACCESS_PATH") == null ? ""
							: String.valueOf(result.get("TZ_ACCESS_PATH"));

					Map<String, Object> mapAttachJson = new HashMap<String, Object>();
					mapAttachJson.put("itemId", itemId);
					mapAttachJson.put("itemName", "附件上传");
					mapAttachJson.put("title", "附件上传");
					mapAttachJson.put("orderby", ++i);
					mapAttachJson.put("fileName", fileName);
					mapAttachJson.put("sysFileName", sysFileName);
					mapAttachJson.put("accessPath", accessPath);
					mapAttachJson.put("viewFileName", fileName);
					listData.add(mapAttachJson);
				}
				return jacksonUtil.List2json(listData);
			} catch (TzSystemException tze) {
				tze.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/* 平台预定义报名表模板 */
		if (StringUtils.equals(oType, "YDYTPL")) {
			String adminOrgId = "ADMIN";

			String sqlDef = "SELECT TZ_APP_TPL_ID, TZ_APP_TPL_MC FROM PS_TZ_APPTPL_DY_T WHERE TZ_EFFEXP_ZT = 'Y' AND UPPER(TZ_JG_ID) = ?";
			List<?> resultlist = sqlQuery.queryForList(sqlDef, new Object[] { adminOrgId });

			ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

			for (Object obj : resultlist) {
				Map<String, Object> result = (Map<String, Object>) obj;
				String tplid = result.get("TZ_APP_TPL_ID") == null ? "" : String.valueOf(result.get("TZ_APP_TPL_ID"));
				String tplname = result.get("TZ_APP_TPL_MC") == null ? "" : String.valueOf(result.get("TZ_APP_TPL_MC"));

				Map<String, Object> mapDefJson = new HashMap<String, Object>();
				mapDefJson.put("tplid", tplid);
				mapDefJson.put("tplname", tplname);
				mapDefJson.put("activestate", "");
				mapDefJson.put("activestatedesc", "");
				listData.add(mapDefJson);
			}
			return jacksonUtil.List2json(listData);
		}

		/* 机构报名表模板 */
		if (StringUtils.equals(oType, "MAINTPL")) {
			String sqlMain = "SELECT TZ_APP_TPL_ID, TZ_APP_TPL_MC FROM PS_TZ_APPTPL_DY_T WHERE TZ_EFFEXP_ZT = 'Y' AND TZ_JG_ID = ?";
			List<?> resultlist = sqlQuery.queryForList(sqlMain, new Object[] { orgId });

			ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

			for (Object obj : resultlist) {
				Map<String, Object> result = (Map<String, Object>) obj;
				String tplid = result.get("TZ_APP_TPL_ID") == null ? "" : String.valueOf(result.get("TZ_APP_TPL_ID"));
				String tplname = result.get("TZ_APP_TPL_MC") == null ? "" : String.valueOf(result.get("TZ_APP_TPL_MC"));

				Map<String, Object> mapMainJson = new HashMap<String, Object>();
				mapMainJson.put("tplid", tplid);
				mapMainJson.put("tplname", tplname);
				mapMainJson.put("activestate", "");
				mapMainJson.put("activestatedesc", "");
				listData.add(mapMainJson);
			}
			return jacksonUtil.List2json(listData);
		}

		/* 邮件模板 */
		if (StringUtils.equals(oType, "MAILTPL")) {
			String sqlMail = "SELECT TZ_TMPL_ID,TZ_TMPL_NAME FROM PS_TZ_EMALTMPL_TBL WHERE TZ_JG_ID = ?";
			List<?> resultlist = sqlQuery.queryForList(sqlMail, new Object[] { orgId });

			ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

			for (Object obj : resultlist) {
				Map<String, Object> result = (Map<String, Object>) obj;
				String tplid = result.get("TZ_TMPL_ID") == null ? "" : String.valueOf(result.get("TZ_TMPL_ID"));
				String tplname = result.get("TZ_TMPL_NAME") == null ? "" : String.valueOf(result.get("TZ_TMPL_NAME"));

				Map<String, Object> mapMailJson = new HashMap<String, Object>();
				mapMailJson.put("tplid", tplid);
				mapMailJson.put("tplname", tplname);
				mapMailJson.put("activestate", "");
				mapMailJson.put("activestatedesc", "");
				listData.add(mapMailJson);
			}
			return jacksonUtil.List2json(listData);
		}

		/* 校验推荐信是否需要验证的appclass */
		if (StringUtils.equals(oType, "REFAPPCLASS")) {
			String sqlTjxCls = "SELECT TZ_APPCLS_ID,TZ_DESCR100 FROM PS_TZ_APPCLS_TBL";
			List<?> resultlist = sqlQuery.queryForList(sqlTjxCls);
			ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

			for (Object obj : resultlist) {
				Map<String, Object> result = (Map<String, Object>) obj;
				String appclassid = result.get("TZ_APPCLS_ID") == null ? ""
						: String.valueOf(result.get("TZ_APPCLS_ID"));
				String appclassname = result.get("TZ_DESCR100") == null ? ""
						: String.valueOf(result.get("TZ_DESCR100"));

				Map<String, Object> mapTjxClsJson = new HashMap<String, Object>();
				mapTjxClsJson.put("appclassid", appclassid);
				mapTjxClsJson.put("appclassname", appclassname);
				listData.add(mapTjxClsJson);
			}
			return jacksonUtil.List2json(listData);
		}

		/* 推荐信称呼 */
		if (StringUtils.equals(oType, "TZ_APP_REF_TITLE")) {
			String lang = jacksonUtil.getString("LANG");

			String sqlBatch = "SELECT TZ_ZHZ_ID,TZ_ZHZ_DMS,TZ_ZHZ_CMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID= ? AND TZ_EFF_STATUS = 'A' ORDER BY TZ_EFF_DATE";
			List<?> resultlist = sqlQuery.queryForList(sqlBatch, new Object[] { "TZ_APP_REF_TITLE" });

			Map<String, Object> mapRet = new HashMap<String, Object>();
			int i = 0;
			for (Object obj : resultlist) {
				Map<String, Object> result = (Map<String, Object>) obj;

				String optId = result.get("TZ_ZHZ_ID") == null ? "" : String.valueOf(result.get("TZ_ZHZ_ID"));
				String shortName = result.get("TZ_ZHZ_DMS") == null ? "" : String.valueOf(result.get("TZ_ZHZ_DMS"));
				String longName = result.get("TZ_ZHZ_CMS") == null ? "" : String.valueOf(result.get("TZ_ZHZ_CMS"));
				String name = shortName;
				if (StringUtils.equals("ENG", lang)) {
					name = longName;
				}

				Map<String, Object> mapOptJson = new HashMap<String, Object>();
				mapOptJson.put("code", optId);
				mapOptJson.put("txt", name);
				mapOptJson.put("orderby", ++i);
				mapOptJson.put("defaultval", "N");
				mapOptJson.put("other", "N");
				mapOptJson.put("weight", 0);
				mapRet.put("A" + optId, mapOptJson);
			}
			return jacksonUtil.Map2json(mapRet);
		}

		/* 报名表元数据导出 */
		if (StringUtils.equals(oType, "METADATA")) {
			String code = "0";
			String msg = "";
			String url = "";

			String tplId = jacksonUtil.getString("tid");

			if (StringUtils.isBlank(tplId)) {
				code = "1";
				msg = "参数错误，未提供报名表模板编号！";
			}

			if (StringUtils.equals(code, "0")) {
				String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				String downloadPath = getSysHardCodeVal.getDownloadPath();

				SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
				String s_dtm = datetimeFormate.format(new Date());
				String fileName = s_dtm + "-" + Math.round(Math.random() * 899999999 + 100000000) + ".xlsx";

				ExcelHandle excelHandle = new ExcelHandle(request, downloadPath, orgid, "METADATA");

				List<String[]> dataCellKeys = new ArrayList<String[]>();
				List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
				Map<String, Object> mapData = new HashMap<String, Object>();

				String sql = "SELECT TZ_XXX_BH,TZ_XXX_MC FROM PS_TZ_TEMP_FIELD_V WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_CCLX IN ('D','L','S')";
				List<?> resultlist = sqlQuery.queryForList(sql, new Object[] { tplId });
				for (Object obj : resultlist) {
					Map<String, Object> result = (Map<String, Object>) obj;
					String xxxbh = result.get("TZ_XXX_BH") == null ? "" : String.valueOf(result.get("TZ_XXX_BH"));
					String xxxmc = result.get("TZ_XXX_MC") == null ? "" : String.valueOf(result.get("TZ_XXX_MC"));
					dataCellKeys.add(new String[] { xxxbh, xxxmc });
					mapData.put(xxxbh, xxxbh);
				}
				dataList.add(mapData);

				boolean rst = excelHandle.export2Excel(fileName, dataCellKeys, dataList);
				if (rst) {
					code = "0";
					msg = "导出元数据成功！";
					url = excelHandle.getExportExcelPath();
				} else {
					code = "1";
					msg = "导出元数据失败！";
					url = "";
				}
			}
			Map<String, Object> mapRet = new HashMap<String, Object>();
			mapRet.put("code", code);
			mapRet.put("msg", msg);
			mapRet.put("url", url);
			return jacksonUtil.Map2json(mapRet);
		}
		return strRet;
	}

	/**
	 * 更新报名表模板
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";

		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		if (StringUtils.isBlank(orgId)) {
			errMsg[0] = "1";
			errMsg[1] = "您当前没有机构，不能保存报名表模板！";
			return strRet;
		}

		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			int dataLength = actData.length;

			System.out.println();
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				System.out.println("strForm:" + strForm);
				// 解析json
				jacksonUtil.json2Map(strForm);
				
				//System.out.println("strForm:"+strForm);
				
				String tid = jacksonUtil.getString("tid");
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				strRet = templateEngine.saveTpl(tid, infoData);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/**
	 * 报名表模板编辑页面
	 */
	@Override
	public String tzGetHtmlContent(String strParams) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String tplId = jacksonUtil.getString("TZ_APP_TPL_ID");

		String editHtml = templateEngine.init(tplId, "");
		return editHtml;
	}
}
