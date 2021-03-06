package com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 申请人在线报名；原：TZ_ONLINE_REG_PKG:TZ_ONLINE_APP_EXTEND
 * 
 * @author 张彬彬
 * @since 2016-1-29
 */

@Service("com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl.tzOnlineAppHisServiceImpl")
public class tzOnlineAppHisServiceImpl {

	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TZGDObject tzSQLObject;

	/* 根据当前模版编号和历史报名实例生成报名实例json */
	public String getHisAppInfoJson(Long numAppInsId, String strTplId) {

		String strOpridApp = "";

		String strOprNameApp = "";

		String sql = "";

		sql = "SELECT OPRID FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID = ? ORDER BY OPRID LIMIT 1";
		strOpridApp = sqlQuery.queryForObject(sql, new Object[] { numAppInsId }, "String");

		sql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID = ?";
		strOprNameApp = sqlQuery.queryForObject(sql, new Object[] { strOpridApp }, "String");

		// 报名表使用模版编号
		String strAppTplId = "";
		// 报名表模板类型
		String strAppTplType = "";

		sql = "SELECT TZ_APP_TPL_ID FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID = ?";
		strAppTplId = sqlQuery.queryForObject(sql, new Object[] { numAppInsId }, "String");

		sql = "SELECT TZ_USE_TYPE FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ?";
		strAppTplType = sqlQuery.queryForObject(sql, new Object[] { strAppTplId }, "String");

		String strGname = "";
		String strSname = "";
		if ("TJX".equals(strAppTplType)) {
			strOprNameApp = "";
			sql = "SELECT TZ_REFERRER_GNAME,TZ_REFERRER_NAME FROM PS_TZ_KS_TJX_TBL WHERE TZ_TJX_APP_INS_ID = ?";
			Map<String, Object> MapTjxInfo = sqlQuery.queryForMap(sql, new Object[] { numAppInsId });
			if (MapTjxInfo != null) {
				strGname = MapTjxInfo.get("TZ_REFERRER_GNAME") == null ? ""
						: String.valueOf(MapTjxInfo.get("TZ_REFERRER_GNAME"));
				strSname = MapTjxInfo.get("TZ_REFERRER_NAME") == null ? ""
						: String.valueOf(MapTjxInfo.get("TZ_REFERRER_NAME"));
				if ("".equals(strGname)) {
					if ("".equals(strSname)) {
						strOprNameApp = strGname + " " + strSname;
					} else {
						strOprNameApp = strGname;
					}
				} else {
					if ("".equals(strSname)) {
						strOprNameApp = strSname;
					} else {
						strOprNameApp = "";
					}
				}
			}
		}

		JacksonUtil jacksonUtil = new JacksonUtil();

		String strAppInsJson = "";

		ArrayList<Map<String, Object>> listAppXxxInsJson = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();

		// 用来记录 页面上"控件编号"
		String strXxxBh = "";

		// 不在多行容器中的字段
		String strGetXxxInfoSingleSql = "SELECT A.TZ_XXX_BH FROM PS_TZ_APP_XXXPZ_T A " + "WHERE TZ_APP_TPL_ID = ? "
				+ "AND EXISTS (SELECT * FROM PS_TZ_TEMP_FIELD_T B WHERE A.TZ_APP_TPL_ID = B.TZ_APP_TPL_ID AND A.TZ_XXX_BH = B.TZ_XXX_BH) "
				+ "AND A.TZ_COM_LMC NOT IN ('LayoutControls','DHContainer','workExperience','EduExperience','recommendletter','Page','TextExplain','Separator','EngLevl')";

		List<?> XxxInfoSingleList = sqlQuery.queryForList(strGetXxxInfoSingleSql, new Object[] { strTplId });
		for (Object XxxInfoSingle : XxxInfoSingleList) {
			Map<String, Object> mapXxxInfoSingle = (Map<String, Object>) XxxInfoSingle;
			strXxxBh = mapXxxInfoSingle.get("TZ_XXX_BH") == null ? ""
					: String.valueOf(mapXxxInfoSingle.get("TZ_XXX_BH"));

			Map<String, Object> mapAppXxxInsJson = new HashMap<String, Object>();

			mapAppXxxInsJson = this.getSingleXxxInfoJson(numAppInsId, strTplId, strXxxBh, strOprNameApp);

			if (mapAppXxxInsJson != null) {
				for (Entry<String, Object> entry : mapAppXxxInsJson.entrySet()) {
					String mapAppXxxInsJsonKey = entry.getKey();
					Map<String, Object> mapAppXxxInsJsonValue = (Map<String, Object>) entry.getValue();
					map.put(mapAppXxxInsJsonKey, mapAppXxxInsJsonValue);
				}
			}
		}

		// 在多行容器中的字段
		String strXxxSlid = "";
		String strComLmc = "";
		String strMaxLine = "";
		String strMinLine = "";
		int numMaxLine = 0;
		int numMinLine = 0;
		int numDhLine = 0;
		String strGetXxxInfoMultipleSql = "SELECT A.TZ_XXX_BH,A.TZ_XXX_SLID,A.TZ_COM_LMC,A.TZ_XXX_MIN_LINE,A.TZ_XXX_MAX_LINE FROM PS_TZ_APP_XXXPZ_T A "
				+ "WHERE TZ_APP_TPL_ID = ? "
				+ "AND EXISTS (SELECT * FROM PS_TZ_TEMP_FIELD_T B WHERE A.TZ_APP_TPL_ID = B.TZ_APP_TPL_ID AND A.TZ_XXX_BH = B.TZ_XXX_BH) "
				+ "AND A.TZ_COM_LMC IN ('LayoutControls','DHContainer','workExperience','EduExperience','EngLevl','recommendletter','FirmType','StartBusinessExp')";
		List<?> XxxInfoMultipleList = sqlQuery.queryForList(strGetXxxInfoMultipleSql, new Object[] { strTplId });
		for (Object XxxInfoMultiple : XxxInfoMultipleList) {
			Map<String, Object> mapXxxInfoMultiple = (Map<String, Object>) XxxInfoMultiple;
			strXxxBh = mapXxxInfoMultiple.get("TZ_XXX_BH") == null ? ""
					: String.valueOf(mapXxxInfoMultiple.get("TZ_XXX_BH"));
			strXxxSlid = mapXxxInfoMultiple.get("TZ_XXX_SLID") == null ? ""
					: String.valueOf(mapXxxInfoMultiple.get("TZ_XXX_SLID"));
			strComLmc = mapXxxInfoMultiple.get("TZ_COM_LMC") == null ? ""
					: String.valueOf(mapXxxInfoMultiple.get("TZ_COM_LMC"));
			strMaxLine = mapXxxInfoMultiple.get("TZ_XXX_MAX_LINE") == null ? ""
					: String.valueOf(mapXxxInfoMultiple.get("TZ_XXX_MAX_LINE"));
			strMinLine = mapXxxInfoMultiple.get("TZ_XXX_MIN_LINE") == null ? ""
					: String.valueOf(mapXxxInfoMultiple.get("TZ_XXX_MIN_LINE"));

			if (!"".equals(strMaxLine)) {
				numMaxLine = Integer.parseInt(strMaxLine);
			}
			if (!"".equals(strMinLine)) {
				numMinLine = Integer.parseInt(strMinLine);
			}

			String sqlGetXxxLine = "SELECT TZ_XXX_LINE FROM PS_TZ_APP_DHHS_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
			String strDhLine = "";
			strDhLine = sqlQuery.queryForObject(sqlGetXxxLine, new Object[] { numAppInsId, strXxxBh }, "String");

			if (strDhLine != null) {
				numDhLine = Integer.parseInt(strDhLine);
			} else {
				numDhLine = 1;
			}

			if (numDhLine > numMaxLine) {
				numDhLine = numMaxLine;
			}
			// 解析'FirmType','StartBusinessExp'
			// if("FirmType".equals(strComLmc)||"StartBusinessExp".equals(strComLmc)){
			// numDhLine=1;
			// }
			String strXxxBhChild = "";
			String strAppXxxInsChildJson = "";
			String strAppXxxInsChildList = "";
			String strAppXxxInsChild = "";
			Map<String, Object> mapChild = null;
			ArrayList<Map<String, Object>> listChild = new ArrayList<Map<String, Object>>();

			ArrayList<Integer> listLinesNo = new ArrayList<Integer>();

			for (int j = numDhLine; j < numMaxLine; j++) {
				listLinesNo.add(j);
			}
			for (int i = 1; i <= numDhLine; i++) {
				mapChild = new HashMap<String, Object>();
				strAppXxxInsChildList = "";
				boolean flagA = true;
				String sqlGetChildrenInfo = "SELECT TZ_XXX_BH FROM PS_TZ_RQ_XXXPZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ?  ORDER BY TZ_ORDER";
				List<?> childrenInfoList = sqlQuery.queryForList(sqlGetChildrenInfo,
						new Object[] { strTplId, strXxxBh });

				List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
				for (Object childrenInfo : childrenInfoList) {
					Map<String, Object> mapChildrenInfo = (Map<String, Object>) childrenInfo;
					strXxxBhChild = mapChildrenInfo.get("TZ_XXX_BH") == null ? ""
							: String.valueOf(mapChildrenInfo.get("TZ_XXX_BH"));
					Map<String, Object> mapAppXxxInsJson = new HashMap<String, Object>();
					if ("EduExperience".equals(strComLmc) || "workExperience".equals(strComLmc)
							|| "EngLevl".equals(strComLmc)) {
						mapAppXxxInsJson = this.getEduOrWorkExprXxxInfoJson(numAppInsId, strTplId, strXxxBhChild, i,
								strOprNameApp);
					}
					// 1.解析"英语水平"控件 //2.解析"职业背景"控件 //3.解析"开始创业"控件 //4.解析"推荐信"控件
					else if ("FirmType".equals(strComLmc) || "StartBusinessExp".equals(strComLmc)) {
						// System.out.println("strComLmc-xx:"+strComLmc);

						mapAppXxxInsJson = this.getGpCtrlData(strComLmc, numAppInsId, strTplId, strXxxBhChild, strXxxBh,
								strOprNameApp);
						// mapAppXxxInsJson=this.getEduOrWorkExprXxxInfoJson(numAppInsId,
						// strTplId, strXxxBhChild, i, strOprNameApp);
					} else if ("recommendletter".equals(strComLmc)) {
						mapAppXxxInsJson = getRefLetterXxxInfoJson(numAppInsId, strTplId, strXxxBhChild, i,
								strOprNameApp);
					} else {
						mapAppXxxInsJson = this.getDhXxxInfoJson(numAppInsId, strTplId, strXxxBhChild, strXxxBh, i,
								strOprNameApp, strComLmc);
						// System.out.println("mapAppXxxInsJson.size:"+mapAppXxxInsJson.size()+"
						// strComLmc:"+strComLmc+"
						// strXxxBhChild:"+strXxxBhChild);
					}

					if (mapAppXxxInsJson != null) {
						for (Entry<String, Object> entry : mapAppXxxInsJson.entrySet()) {
							String mapAppXxxInsJsonKey = entry.getKey();
							Map<String, Object> mapAppXxxInsJsonValue = (Map<String, Object>) entry.getValue();
							// 分解mapAppXxxInsJsonValue
							// for(Entry<String,
							// Object>valEntry:mapAppXxxInsJsonValue.entrySet()){
							// String valKey=valEntry.getKey();
							// if(valKey.equals("children")){
							// //將children中的值取出來作爲一個Map
							// Map<String,Object>valChildren=((List<Map<String,
							// Object>>) valEntry.getValue()).get(0);
							// tempList.add(valChildren);
							// }
							// }
							mapChild.put(mapAppXxxInsJsonKey, mapAppXxxInsJsonValue);
						}
					}

				}
				// ---------------------------------------------------------------------
				// Map<String,Object>relChildrenData=new
				// HashMap<String,Object>();
				// try{
				// if(tempList!=null&&tempList.size()>1){
				// for(int index=0;index<tempList.size();index++){
				// Map<String,Object>tempMap=tempList.get(index);
				// for (Entry<String, Object> entry:tempMap.entrySet()){
				// String dataKey=entry.getKey();
				// Map<String,Object>dataVal=(Map<String,
				// Object>)entry.getValue();
				// relChildrenData.put(dataKey, dataVal);
				// }
				// }
				// //將mapChild中的val中的children換成relChildrenData
				// for (Entry<String, Object> entry:mapChild.entrySet()){
				// String mapAppXxxInsJsonKey = entry.getKey();
				// Map<String, Object> mapAppXxxInsJsonValue = (Map<String,
				// Object>)entry.getValue();
				//
				// for(Entry<String,
				// Object>valEntry:mapAppXxxInsJsonValue.entrySet()){
				// String valKey=valEntry.getKey();
				// if(valKey.equals("children")){
				// //將children中的值取出來作爲一個Map
				// mapAppXxxInsJsonValue.replace("children", relChildrenData);
				// }
				// }
				// mapChild.replace(mapAppXxxInsJsonKey, mapAppXxxInsJsonValue);
				// }
				// }
				// }catch(Exception e){
				//
				// }
				// System.out.println("relChildrenData:"+new
				// JacksonUtil().Map2json(relChildrenData));
				// ---------------------------------------------------------------------
				listChild.add(mapChild);
			}

			if (!listChild.isEmpty()) {
				Map<String, Object> mapAppXxxInsJson = new HashMap<String, Object>();
				mapAppXxxInsJson.put("instanceId", strXxxSlid);
				mapAppXxxInsJson.put("itemId", strXxxBh);
				mapAppXxxInsJson.put("classname", strComLmc);
				mapAppXxxInsJson.put("isDoubleLine", "Y");
				mapAppXxxInsJson.put("isSingleLine", "N");
				mapAppXxxInsJson.put("value", "");
				mapAppXxxInsJson.put("wzsm", "");
				mapAppXxxInsJson.put("children", listChild);
				mapAppXxxInsJson.put("minLines", strMinLine);
				mapAppXxxInsJson.put("maxLines", strMaxLine);
				mapAppXxxInsJson.put("linesNo", listLinesNo);

				map.put(strXxxSlid, mapAppXxxInsJson);

			}
			// System.out.println("worldList"+jacksonUtil.List2json(listChild));
		}

		strAppInsJson = jacksonUtil.Map2json(map);
		// System.out.println("strAppInsJson:"+strAppInsJson);
		return strAppInsJson;
	}

	// 不在容器中的字段
	private Map<String, Object> getSingleXxxInfoJson(Long numAppInsId, String strTplId, String strXxxBh,
			String strOprNameApp) {

		String strAppXxxInsJson = "";

		// 报名表使用模版编号
		String strAppTplIdHis = "";
		// 报名表信息项存储类型
		String strXxxCclx = "";
		String strComLmc = "";
		String strXxxSlid = "";

		// 是否带入
		boolean flag = true;

		String strAppXxxValueS = "";
		String strAppXxxValueL = "";

		String sql = "";

		Map<String, Object> mapAppXxxOptionJson = null;

		ArrayList<Map<String, Object>> arrAppFileJson = null;

		ArrayList<Map<String, Object>> arrAppChildrenJson = null;

		sql = "SELECT TZ_APP_TPL_ID FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID = ?";
		strAppTplIdHis = sqlQuery.queryForObject(sql, new Object[] { numAppInsId }, "String");

		sql = "SELECT TZ_XXX_SLID,TZ_COM_LMC,TZ_XXX_CCLX FROM PS_TZ_APP_XXXPZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_BH = ?";
		Map<String, Object> MapXxxPz = sqlQuery.queryForMap(sql, new Object[] { strTplId, strXxxBh });
		if (MapXxxPz != null) {
			strXxxCclx = MapXxxPz.get("TZ_XXX_CCLX") == null ? "" : String.valueOf(MapXxxPz.get("TZ_XXX_CCLX"));
			strXxxSlid = MapXxxPz.get("TZ_XXX_SLID") == null ? "" : String.valueOf(MapXxxPz.get("TZ_XXX_SLID"));
			strComLmc = MapXxxPz.get("TZ_COM_LMC") == null ? "" : String.valueOf(MapXxxPz.get("TZ_COM_LMC"));
			String sqlGetValue = "";
			switch (strXxxCclx) {
			case "S":
				sqlGetValue = "SELECT TZ_APP_S_TEXT,TZ_APP_L_TEXT FROM PS_TZ_APP_CC_VW2 WHERE TZ_APP_INS_ID = ? AND TZ_XXX_NO = ? AND TZ_COM_LMC = ?";
				Map<String, Object> MapXxxValue = sqlQuery.queryForMap(sqlGetValue,
						new Object[] { numAppInsId, strXxxBh, strComLmc });
				if (MapXxxValue != null) {
					strAppXxxValueS = MapXxxValue.get("TZ_APP_S_TEXT") == null ? ""
							: String.valueOf(MapXxxValue.get("TZ_APP_S_TEXT"));
					strAppXxxValueL = MapXxxValue.get("TZ_APP_L_TEXT") == null ? ""
							: String.valueOf(MapXxxValue.get("TZ_APP_L_TEXT"));
				}
				if ("Select".equals(strComLmc)) {
					// 如果是下拉列表
					String strExistsOption = "";
					String sqlOption = "SELECT 'Y' FROM PS_TZ_APPXXX_KXZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_BH = ? AND TZ_XXXKXZ_MC = ?";
					strExistsOption = sqlQuery.queryForObject(sqlOption,
							new Object[] { strTplId, strXxxBh, strAppXxxValueS }, "String");
					if ("".equals(strExistsOption) || strExistsOption == null) {
						strAppXxxValueS = "";
						strAppXxxValueL = "";
						flag = false;
					}
				}
				break;
			case "L":
				sqlGetValue = "SELECT TZ_APP_L_TEXT FROM PS_TZ_APP_CC_VW2 WHERE TZ_APP_INS_ID = ? AND TZ_XXX_NO = ? AND TZ_COM_LMC = ?";
				strAppXxxValueL = sqlQuery.queryForObject(sqlGetValue,
						new Object[] { numAppInsId, strXxxBh, strComLmc }, "String");
				if (strAppXxxValueL == null) {
					strAppXxxValueS = "";
					strAppXxxValueL = "";
				} else {
					strAppXxxValueS = strAppXxxValueL;
				}
				break;
			case "D":
				mapAppXxxOptionJson = new HashMap<String, Object>();
				String sqlGetAppXxxOption = "SELECT TZ_XXXKXZ_MC,TZ_XXXKXZ_MS FROM PS_TZ_APPXXX_KXZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_BH = ? ORDER BY TZ_ORDER";
				String strXxxkxzMc = "";
				String strXxxkxzMs = "";
				String strIsChecked = "";
				int numOption = 0;
				List<?> appXxxOptionList = sqlQuery.queryForList(sqlGetAppXxxOption,
						new Object[] { strTplId, strXxxBh });
				for (Object appXxxOptionObj : appXxxOptionList) {
					Map<String, Object> mapAppXxxOption = (Map<String, Object>) appXxxOptionObj;
					strXxxkxzMc = mapAppXxxOption.get("TZ_XXXKXZ_MC") == null ? ""
							: String.valueOf(mapAppXxxOption.get("TZ_XXXKXZ_MC"));
					strXxxkxzMs = mapAppXxxOption.get("TZ_XXXKXZ_MS") == null ? ""
							: String.valueOf(mapAppXxxOption.get("TZ_XXXKXZ_MS"));
					numOption++;
					String sqlIsChecked = "SELECT TZ_IS_CHECKED FROM PS_TZ_APP_DHCC_VW2 "
							+ "WHERE TZ_APP_INS_ID = ? AND TZ_XXX_NO = ? AND TZ_XXXKXZ_MC = ? AND TZ_APP_S_TEXT = ?";
					strIsChecked = sqlQuery.queryForObject(sqlIsChecked,
							new Object[] { numAppInsId, strXxxBh, strXxxkxzMc, strXxxkxzMs }, "String");
					if (strIsChecked == null || "".equals(strIsChecked)) {
						strIsChecked = "N";
					}
					Map<String, Object> mapAppXxxOptionJson1 = new HashMap<String, Object>();
					mapAppXxxOptionJson1.put("code", strXxxkxzMc);
					mapAppXxxOptionJson1.put("txt", strXxxkxzMs);
					mapAppXxxOptionJson1.put("other", "N");
					mapAppXxxOptionJson1.put("checked", strIsChecked);

					mapAppXxxOptionJson.put(strXxxSlid + String.valueOf(numOption), mapAppXxxOptionJson1);
				}

				break;
			case "F":

				arrAppFileJson = new ArrayList<Map<String, Object>>();
				String strXxxMc = "";
				String strSysFileName = "";
				String strUseFileName = "";
				String strViewFileName = "";
				String strFileIndex = "";
				String accPath = "";
				// 修改视图:`A`.`TZ_ACCESS_PATH` AS `TZ_ACCESS_PATH`,
				String sqlGetFile = "SELECT TZ_ACCESS_PATH,TZ_XXX_MC,TZ_INDEX,ATTACHSYSFILENAME,ATTACHUSERFILE FROM PS_TZ_FORM_ATT_VW2 "
						+ "WHERE TZ_APP_INS_ID = ? AND TZ_XXX_NO = ? AND TZ_COM_LMC = ?";
				List<?> appFileList = sqlQuery.queryForList(sqlGetFile, new Object[] { strTplId, strXxxBh, strComLmc });
				for (Object appFileObj : appFileList) {
					Map<String, Object> mapAppFileObj = (Map<String, Object>) appFileObj;
					strXxxMc = mapAppFileObj.get("TZ_XXX_MC") == null ? ""
							: String.valueOf(mapAppFileObj.get("TZ_XXX_MC"));
					strFileIndex = mapAppFileObj.get("TZ_INDEX") == null ? ""
							: String.valueOf(mapAppFileObj.get("TZ_INDEX"));
					// 绝对路径:TZ_ACCESS_PATH
					accPath = mapAppFileObj.get("TZ_ACCESS_PATH") == null ? ""
							: mapAppFileObj.get("TZ_ACCESS_PATH").toString();
					strSysFileName = mapAppFileObj.get("ATTACHSYSFILENAME") == null ? ""
							: String.valueOf(mapAppFileObj.get("ATTACHSYSFILENAME"));
					// strSysFileName=accPath+strSysFileName;
					strUseFileName = mapAppFileObj.get("ATTACHUSERFILE") == null ? ""
							: String.valueOf(mapAppFileObj.get("ATTACHUSERFILE"));
					if (strXxxMc.length() > 17) {
						String suffix = strSysFileName.substring(strSysFileName.lastIndexOf(".") + 1);
						if ("".equals(suffix) || suffix == null) {
							strViewFileName = strUseFileName;
						} else {
							strViewFileName = strOprNameApp + "_" + strXxxMc.substring(1, 15) + "..._" + strFileIndex
									+ "." + suffix;
						}
					} else {
						strViewFileName = strUseFileName;
					}

					Map<String, Object> mapAppFileJson = new HashMap<String, Object>();
					mapAppFileJson.put("itemId", "attachment_Upload");
					mapAppFileJson.put("orderby", strFileIndex);
					// accessPath
					mapAppFileJson.put("accessPath", accPath);
					mapAppFileJson.put("fileName", strUseFileName);
					mapAppFileJson.put("sysFileName", strSysFileName);
					mapAppFileJson.put("viewFileName", strViewFileName);

					arrAppFileJson.add(mapAppFileJson);

				}
				if (arrAppFileJson.isEmpty())
					flag = false;

				// flag = false;
				break;
			default:
				arrAppChildrenJson = new ArrayList<Map<String, Object>>();
				if ("BirthdayAndAge".equals(strComLmc) || "DateComboBox".equals(strComLmc)
						|| "mobilePhone".equals(strComLmc) || "CertificateNum".equals(strComLmc)
						|| "MailingAddress".equals(strComLmc) || "FirmType".equals(strComLmc)
						|| "StartBusinessExp".equals(strComLmc) || "YearsAndMonth".equals(strComLmc)) {

					// 查看是否在容器中
					String strDxxxBh = "";
					String strXxxBhLike = "";
					String sqlGetDxxxBh = "SELECT TZ_D_XXX_BH FROM PS_TZ_TEMP_FIELD_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
					strDxxxBh = sqlQuery.queryForObject(sqlGetDxxxBh, new Object[] { strAppTplIdHis, strXxxBh },
							"String");
					if (!"".equals(strDxxxBh) && strDxxxBh != null) {
						strXxxBhLike = strDxxxBh + strXxxBh;
					} else {
						strDxxxBh = strXxxBh;
						strXxxBhLike = strXxxBh;
					}

					String strAppxxxChildrenBh = "";
					String strAppxxxChildrenValue = "";
					String strAppxxxChildrenComLmc = "";
					String sqlGetXxxChildren = "SELECT TZ_XXX_BH FROM PS_TZ_RQ_XXXPZ_T "
							+ "WHERE TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? ORDER BY TZ_ORDER";
					List<?> appChildList = sqlQuery.queryForList(sqlGetXxxChildren,
							new Object[] { strTplId, strXxxBh });
					for (Object appChildObj : appChildList) {
						Map<String, Object> mapAppChildObj = (Map<String, Object>) appChildObj;
						strAppxxxChildrenBh = mapAppChildObj.get("TZ_XXX_BH") == null ? ""
								: String.valueOf(mapAppChildObj.get("TZ_XXX_BH"));

						String sqlGetChildrenValue = "SELECT TZ_APP_S_TEXT,TZ_COM_LMC FROM PS_TZ_APP_CC_VW2 "
								+ "WHERE TZ_APP_INS_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH = ? AND TZ_XXX_NO = ?";
						Map<String, Object> mapGetChildrenValue = sqlQuery.queryForMap(sqlGetChildrenValue,
								new Object[] { numAppInsId, strDxxxBh, strXxxBhLike + strAppxxxChildrenBh,
										strAppxxxChildrenBh });
						if (mapGetChildrenValue != null) {
							strAppxxxChildrenValue = mapGetChildrenValue.get("TZ_APP_S_TEXT") == null ? ""
									: String.valueOf(mapGetChildrenValue.get("TZ_APP_S_TEXT"));
							strAppxxxChildrenComLmc = mapGetChildrenValue.get("TZ_COM_LMC") == null ? ""
									: String.valueOf(mapGetChildrenValue.get("TZ_COM_LMC"));

							Map<String, Object> mapAppChildJson = new HashMap<String, Object>();
							mapAppChildJson.put("itemId", strAppxxxChildrenBh);
							mapAppChildJson.put("classname", strAppxxxChildrenComLmc);
							mapAppChildJson.put("isDoubleLine", "N");
							mapAppChildJson.put("isSingleLine", "Y");
							mapAppChildJson.put("value", strAppxxxChildrenValue);
							arrAppChildrenJson.add(mapAppChildJson);
						}
					}
				}
				if (arrAppChildrenJson.isEmpty())
					flag = false;
			}

			if (flag) {
				Map<String, Object> mapXxxInfo = new HashMap<String, Object>();
				mapXxxInfo.put("instanceId", strXxxSlid);
				mapXxxInfo.put("itemId", strXxxBh);
				mapXxxInfo.put("classname", strComLmc);
				mapXxxInfo.put("isDoubleLine", "N");
				if ("BirthdayAndAge".equals(strComLmc) || "DateComboBox".equals(strComLmc)
						|| "mobilePhone".equals(strComLmc) || "CertificateNum".equals(strComLmc)
						|| "FirmType".equals(strComLmc) || "StartBusinessExp".equals(strComLmc)
						|| "MailingAddress".equals(strComLmc) || "YearsAndMonth".equals(strComLmc)) {
					mapXxxInfo.put("isSingleLine", "Y");
				} else {
					mapXxxInfo.put("isSingleLine", "N");
				}
				mapXxxInfo.put("value", strAppXxxValueS);
				mapXxxInfo.put("wzsm", strAppXxxValueL);

				if (mapAppXxxOptionJson != null) {
					mapXxxInfo.put("option", mapAppXxxOptionJson);
				}

				if (arrAppFileJson != null) {
					mapXxxInfo.put("children", arrAppFileJson);
				}
				if (arrAppChildrenJson != null) {
					mapXxxInfo.put("children", arrAppChildrenJson);
				}

				// 返回Map对象
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(strXxxSlid, mapXxxInfo);
				return map;
			}
		}
		return null;
	}

	// 推荐信信息字段
	private Map<String, Object> getRefLetterXxxInfoJson(Long numAppInsId, String strTplId, String strXxxBh,
			int numDhSeq, String strOprNameApp) {

		// 报名表使用模版编号
		String strAppTplIdHis = "";
		// 报名表信息项存储类型
		String strXxxCclx = "";
		String strComLmc = "";
		String strXxxSlid = "";

		// 是否带入
		boolean flag = true;

		String strAppXxxValueS = "";
		String strAppXxxValueL = "";

		String sql = "";

		Map<String, Object> mapAppXxxOptionJson = null;

		try {
			ArrayList<Map<String, Object>> arrAppFileJson = null;

			String strSysFileName = "";
			String strUseFileName = "";
			String strViewFileName = "";

			sql = "SELECT TZ_APP_TPL_ID FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID = ?";
			strAppTplIdHis = sqlQuery.queryForObject(sql, new Object[] { numAppInsId }, "String");

			// 查看是否在容器中
			String strDxxxBh = "";
			String strXxxBhLike = "";
			String sqlGetDxxxBh = "SELECT TZ_D_XXX_BH FROM PS_TZ_TEMP_FIELD_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ? LIMIT 1";

			strDxxxBh = sqlQuery.queryForObject(sqlGetDxxxBh, new Object[] { strAppTplIdHis, strXxxBh }, "String");
			if (!"".equals(strDxxxBh) && strDxxxBh != null) {
				strXxxBhLike = strDxxxBh + strXxxBh;
			} else {
				strDxxxBh = strXxxBh;
				strXxxBhLike = strXxxBh;
			}

			String strItemId = "";

			sql = "SELECT TZ_XXX_SLID,TZ_COM_LMC,TZ_XXX_CCLX FROM PS_TZ_APP_XXXPZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_BH = ?";

			Map<String, Object> MapXxxPz = sqlQuery.queryForMap(sql, new Object[] { strTplId, strXxxBh });
			if (MapXxxPz != null) {
				strXxxCclx = MapXxxPz.get("TZ_XXX_CCLX") == null ? "" : String.valueOf(MapXxxPz.get("TZ_XXX_CCLX"));
				strXxxSlid = MapXxxPz.get("TZ_XXX_SLID") == null ? "" : String.valueOf(MapXxxPz.get("TZ_XXX_SLID"));
				strComLmc = MapXxxPz.get("TZ_COM_LMC") == null ? "" : String.valueOf(MapXxxPz.get("TZ_COM_LMC"));
				String sqlGetValue = "";
				switch (strXxxCclx) {
				case "S":
					sqlGetValue = tzSQLObject.getSQLText("SQL.TZWebsiteApplicationBundle.TZ_GETDHXXXCHILD_VALUE");

					Map<String, Object> MapXxxValue = sqlQuery.queryForMap(sqlGetValue, new Object[] { numAppInsId,
							strDxxxBh, strXxxBhLike + "%", strXxxBh, strComLmc, numDhSeq - 1 });

					if (MapXxxValue != null) {
						strAppXxxValueS = MapXxxValue.get("TZ_APP_S_TEXT") == null ? ""
								: String.valueOf(MapXxxValue.get("TZ_APP_S_TEXT"));
						strAppXxxValueL = MapXxxValue.get("TZ_APP_L_TEXT") == null ? ""
								: String.valueOf(MapXxxValue.get("TZ_APP_L_TEXT"));
					}

					if (numDhSeq > 1) {
						strItemId = strXxxBh + "_" + String.valueOf((numDhSeq - 1));
					} else {
						strItemId = strXxxBh;
					}

					if ("Select".equals(strComLmc)) {
						// 如果是下拉列表
						String strExistsOption = "";
						String sqlOption = "SELECT 'Y' FROM PS_TZ_APPXXX_KXZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_BH = ? AND TZ_XXXKXZ_MC = ?";
						strExistsOption = sqlQuery.queryForObject(sqlOption,
								new Object[] { strTplId, strXxxBh, strAppXxxValueS }, "String");
						if ("".equals(strExistsOption) || strExistsOption == null) {
							strAppXxxValueS = "";
							strAppXxxValueL = "";
							flag = false;
						}
					}
					break;
				case "L":
					if (numDhSeq > 1) {
						strItemId = strXxxBh + "_" + String.valueOf((numDhSeq - 1));
					} else {
						strItemId = strXxxBh;
					}
					sqlGetValue = tzSQLObject.getSQLText("SQL.TZWebsiteApplicationBundle.TZ_GETDHXXXCHILD_VALUE");
					Map<String, Object> MapXxxValueL = sqlQuery.queryForMap(sqlGetValue, new Object[] { numAppInsId,
							strDxxxBh, strXxxBhLike + "%", strXxxBh, strComLmc, numDhSeq - 1 });
					if (MapXxxValueL != null) {
						strAppXxxValueS = MapXxxValueL.get("TZ_APP_S_TEXT") == null ? ""
								: String.valueOf(MapXxxValueL.get("TZ_APP_S_TEXT"));
						strAppXxxValueL = MapXxxValueL.get("TZ_APP_L_TEXT") == null ? ""
								: String.valueOf(MapXxxValueL.get("TZ_APP_L_TEXT"));
					}
					if (strAppXxxValueL == null) {
						strAppXxxValueS = "";
						strAppXxxValueL = "";
					} else {
						strAppXxxValueS = strAppXxxValueL;
					}
					break;
				case "D":
					if (numDhSeq > 1) {
						strItemId = strXxxBh + "_" + String.valueOf((numDhSeq - 1));
					} else {
						strItemId = strXxxBh;
					}
					mapAppXxxOptionJson = new HashMap<String, Object>();
					String sqlGetAppXxxOption = "SELECT TZ_XXXKXZ_MC,TZ_XXXKXZ_MS FROM PS_TZ_APPXXX_KXZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_BH = ? ORDER BY TZ_ORDER";
					String strXxxkxzMc = "";
					String strXxxkxzMs = "";
					String strIsChecked = "";
					int numOption = 0;
					List<?> appXxxOptionList = sqlQuery.queryForList(sqlGetAppXxxOption,
							new Object[] { strTplId, strXxxBh });
					for (Object appXxxOptionObj : appXxxOptionList) {
						Map<String, Object> mapAppXxxOption = (Map<String, Object>) appXxxOptionObj;
						strXxxkxzMc = mapAppXxxOption.get("TZ_XXXKXZ_MC") == null ? ""
								: String.valueOf(mapAppXxxOption.get("TZ_XXXKXZ_MC"));
						strXxxkxzMs = mapAppXxxOption.get("TZ_XXXKXZ_MS") == null ? ""
								: String.valueOf(mapAppXxxOption.get("TZ_XXXKXZ_MS"));

						String strOptionSlid = "";
						switch (strXxxSlid) {
						case "rec_sex":
							if ("M".equals(strXxxkxzMc)) {
								strOptionSlid = "MAN";
							} else {
								strOptionSlid = "WOMAN";
							}
							break;
						case "rec_way":
							if ("S".equals(strXxxkxzMc)) {
								strOptionSlid = "SEND";
							} else {
								strOptionSlid = "UPLOAD";
							}
							break;
						case "rec_language":
							if ("C".equals(strXxxkxzMc)) {
								strOptionSlid = "ZHS";
							} else {
								strOptionSlid = "ENG";
							}
							break;
						}
						numOption++;
						String sqlIsChecked = tzSQLObject
								.getSQLText("SQL.TZWebsiteApplicationBundle.TZ_GETDHXXXCHILD_VALUE5");
						strIsChecked = sqlQuery.queryForObject(sqlIsChecked, new Object[] { numAppInsId, strDxxxBh,
								strXxxBhLike + "%", strXxxBh, strXxxkxzMc, numDhSeq - 1 }, "String");
						if (strIsChecked.equals("Y")) {
							strAppXxxValueS = strXxxkxzMc;
						} else {
							strIsChecked = "N";
						}
						Map<String, Object> mapAppXxxOptionJson1 = new HashMap<String, Object>();
						mapAppXxxOptionJson1.put("code", strXxxkxzMc);
						mapAppXxxOptionJson1.put("txt", strXxxkxzMs);
						mapAppXxxOptionJson1.put("other", "N");
						mapAppXxxOptionJson1.put("checked", strIsChecked);

						mapAppXxxOptionJson.put(strOptionSlid, mapAppXxxOptionJson1);
					}

					break;
				case "F":
					if (numDhSeq > 1) {
						strItemId = strXxxBh + "_" + String.valueOf((numDhSeq - 1));
					} else {
						strItemId = strXxxBh;
					}

					String strAppxxxChildrenFbh = "";

					String sqlGetAppxxxChildrenFbh = "";
					sqlGetAppxxxChildrenFbh = tzSQLObject
							.getSQLText("SQL.TZWebsiteApplicationBundle.TZ_GETDHXXXCHILD_VALUE4");
					strAppxxxChildrenFbh = sqlQuery.queryForObject(sqlGetAppxxxChildrenFbh, new Object[] { numAppInsId,
							strDxxxBh, strXxxBhLike + "%", strXxxBh, strComLmc, numDhSeq - 1 }, "String");
					String strXxxMc = "";

					String strFileIndex = "";
					if (strAppxxxChildrenFbh != null) {

						String sqlGetFile = "SELECT TZ_XXX_MC,TZ_INDEX,ATTACHSYSFILENAME,ATTACHUSERFILE FROM PS_TZ_FORM_ATT_VW2 "
								+ "WHERE TZ_APP_INS_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH = ? AND TZ_XXX_NO = ? AND TZ_COM_LMC = ?";

						Map<String, Object> mapAppFileObj = sqlQuery.queryForMap(sqlGetFile,
								new Object[] { numAppInsId, strDxxxBh, strAppxxxChildrenFbh, strXxxBh, strComLmc });

						if (mapAppFileObj != null) {
							strXxxMc = mapAppFileObj.get("TZ_XXX_MC") == null ? ""
									: String.valueOf(mapAppFileObj.get("TZ_XXX_MC"));
							strFileIndex = mapAppFileObj.get("TZ_INDEX") == null ? ""
									: String.valueOf(mapAppFileObj.get("TZ_INDEX"));
							strSysFileName = mapAppFileObj.get("ATTACHSYSFILENAME") == null ? ""
									: String.valueOf(mapAppFileObj.get("ATTACHSYSFILENAME"));
							strUseFileName = mapAppFileObj.get("ATTACHUSERFILE") == null ? ""
									: String.valueOf(mapAppFileObj.get("ATTACHUSERFILE"));
							if (strXxxMc.length() > 17) {
								String suffix = strSysFileName.substring(strSysFileName.lastIndexOf(".") + 1);
								if ("".equals(suffix) || suffix == null) {
									strViewFileName = strUseFileName;
								} else {
									strViewFileName = strOprNameApp + "_" + strXxxMc.substring(1, 15) + "..._"
											+ strFileIndex + "." + suffix;
								}
							} else {
								strViewFileName = strUseFileName;
							}
						}
					}
					break;
				}
			}
			if (flag) {
				Map<String, Object> mapXxxInfo = new HashMap<String, Object>();
				mapXxxInfo.put("instanceId", strXxxSlid);
				mapXxxInfo.put("itemId", strItemId);
				mapXxxInfo.put("classname", strComLmc);
				mapXxxInfo.put("isDoubleLine", "N");
				mapXxxInfo.put("isSingleLine", "N");
				mapXxxInfo.put("value", strAppXxxValueS);
				mapXxxInfo.put("wzsm", strAppXxxValueL);
				if ("F".equals(strXxxCclx)) {
					mapXxxInfo.put("filename", strUseFileName);
					mapXxxInfo.put("sysFileName", strSysFileName);
					mapXxxInfo.put("viewFileName", strViewFileName);
				}

				if (mapAppXxxOptionJson != null) {
					mapXxxInfo.put("option", mapAppXxxOptionJson);
				}

				String strRefLetterXxxSlid = "";
				switch (strXxxSlid) {
				case "rec_name":
					strRefLetterXxxSlid = "recommend_1";
					break;
				case "rec_company":
					strRefLetterXxxSlid = "recommend_2";
					break;
				case "rec_post":
					strRefLetterXxxSlid = "recommend_3";
					break;
				case "rec_phone_no":
					strRefLetterXxxSlid = "recommend_4";
					break;
				case "rec_email":
					strRefLetterXxxSlid = "recommend_5";
					break;
				case "rec_relation":
					strRefLetterXxxSlid = "recommend_6";
					break;
				case "rec_language":
					strRefLetterXxxSlid = "recommend_7";
					break;
				case "rec_way":
					strRefLetterXxxSlid = "recommend_8";
					break;
				case "rec_attach":
					strRefLetterXxxSlid = "recommend_9";
					break;
				case "rec_by1":
					strRefLetterXxxSlid = "recommend_10";
					break;
				case "rec_by2":
					strRefLetterXxxSlid = "recommend_11";
					break;
				case "rec_by3":
					strRefLetterXxxSlid = "recommend_12";
					break;
				case "rec_by4":
					strRefLetterXxxSlid = "recommend_13";
					break;
				case "rec_by5":
					strRefLetterXxxSlid = "recommend_14";
					break;
				case "rec_sex":
					strRefLetterXxxSlid = "recommend_15";
					break;
				case "rec_phone_area":
					strRefLetterXxxSlid = "recommend_16";
					break;
				case "rec_gname":
					strRefLetterXxxSlid = "recommend_17";
					break;
				case "rec_title":
					strRefLetterXxxSlid = "recommend_18";
					break;
				case "rec_xh":
					strRefLetterXxxSlid = "recommend_99";
					break;
				}
				// 返回Map对象
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(strRefLetterXxxSlid, mapXxxInfo);
				// JacksonUtil jacksonUtil = new JacksonUtil();
				// System.out.println("World:"+ jacksonUtil.Map2json(map));
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Hello");
		}
		return null;
	}

	// 工作经历和教育背景信息字段
	private Map<String, Object> getEduOrWorkExprXxxInfoJson(Long numAppInsId, String strTplId, String strXxxBh,
			int numDhSeq, String strOprNameApp) {

		// 报名表使用模版编号
		String strAppTplIdHis = "";
		// 报名表信息项存储类型
		String strXxxCclx = "";
		String strComLmc = "";
		String strXxxSlid = "";

		// 是否带入
		boolean flag = true;

		String strAppXxxValueS = "";
		String strAppXxxValueL = "";

		String sql = "";

		Map<String, Object> mapAppXxxOptionJson = null;

		try {
			ArrayList<Map<String, Object>> arrAppFileJson = null;

			String strSysFileName = "";
			String strUseFileName = "";
			String strViewFileName = "";

			sql = "SELECT TZ_APP_TPL_ID FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID = ?";
			strAppTplIdHis = sqlQuery.queryForObject(sql, new Object[] { numAppInsId }, "String");

			// 查看是否在容器中
			String strDxxxBh = "";
			String strXxxBhLike = "";
			String sqlGetDxxxBh = "SELECT TZ_D_XXX_BH FROM PS_TZ_TEMP_FIELD_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ? LIMIT 1";

			strDxxxBh = sqlQuery.queryForObject(sqlGetDxxxBh, new Object[] { strAppTplIdHis, strXxxBh }, "String");
			if (!"".equals(strDxxxBh) && strDxxxBh != null) {
				strXxxBhLike = strDxxxBh + strXxxBh;
			} else {
				strDxxxBh = strXxxBh;
				strXxxBhLike = strXxxBh;
			}

			String strItemId = "";

			sql = "SELECT TZ_XXX_SLID,TZ_COM_LMC,TZ_XXX_CCLX FROM PS_TZ_APP_XXXPZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_BH = ?";

			Map<String, Object> MapXxxPz = sqlQuery.queryForMap(sql, new Object[] { strTplId, strXxxBh });
			if (MapXxxPz != null) {
				strXxxCclx = MapXxxPz.get("TZ_XXX_CCLX") == null ? "" : String.valueOf(MapXxxPz.get("TZ_XXX_CCLX"));
				strXxxSlid = MapXxxPz.get("TZ_XXX_SLID") == null ? "" : String.valueOf(MapXxxPz.get("TZ_XXX_SLID"));
				strComLmc = MapXxxPz.get("TZ_COM_LMC") == null ? "" : String.valueOf(MapXxxPz.get("TZ_COM_LMC"));
				String sqlGetValue = "";
				switch (strXxxCclx) {
				case "S":
					sqlGetValue = tzSQLObject.getSQLText("SQL.TZWebsiteApplicationBundle.TZ_GETDHXXXCHILD_VALUE");

					Map<String, Object> MapXxxValue = sqlQuery.queryForMap(sqlGetValue, new Object[] { numAppInsId,
							strDxxxBh, strXxxBhLike + "%", strXxxBh, strComLmc, numDhSeq - 1 });

					if (MapXxxValue != null) {
						strAppXxxValueS = MapXxxValue.get("TZ_APP_S_TEXT") == null ? ""
								: String.valueOf(MapXxxValue.get("TZ_APP_S_TEXT"));
						strAppXxxValueL = MapXxxValue.get("TZ_APP_L_TEXT") == null ? ""
								: String.valueOf(MapXxxValue.get("TZ_APP_L_TEXT"));
					}

					if (numDhSeq > 1) {
						strItemId = strXxxBh + "_" + String.valueOf((numDhSeq - 1));
					} else {
						strItemId = strXxxBh;
					}
					/*
					 * if("Select".equals(strComLmc)){ //如果是下拉列表 String
					 * strExistsOption = ""; String sqlOption =
					 * "SELECT 'Y' FROM PS_TZ_APPXXX_KXZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_BH = ? AND TZ_XXXKXZ_MC = ?"
					 * ; strExistsOption = sqlQuery.queryForObject(sqlOption,
					 * new Object[] { strTplId, strXxxBh, strAppXxxValueS },
					 * "String"); if("".equals(strExistsOption) ||
					 * strExistsOption == null){ strAppXxxValueS = "";
					 * strAppXxxValueL = ""; } }
					 */
					break;
				case "L":
					if (numDhSeq > 1) {
						strItemId = strXxxBh + "_" + String.valueOf((numDhSeq - 1));
					} else {
						strItemId = strXxxBh;
					}
					sqlGetValue = tzSQLObject.getSQLText("SQL.TZWebsiteApplicationBundle.TZ_GETDHXXXCHILD_VALUE");
					Map<String, Object> MapXxxValueL = sqlQuery.queryForMap(sqlGetValue, new Object[] { numAppInsId,
							strDxxxBh, strXxxBhLike + "%", strXxxBh, strComLmc, numDhSeq - 1 });
					if (MapXxxValueL != null) {
						strAppXxxValueS = MapXxxValueL.get("TZ_APP_S_TEXT") == null ? ""
								: String.valueOf(MapXxxValueL.get("TZ_APP_S_TEXT"));
						strAppXxxValueL = MapXxxValueL.get("TZ_APP_L_TEXT") == null ? ""
								: String.valueOf(MapXxxValueL.get("TZ_APP_L_TEXT"));
					}
					if (strAppXxxValueL == null) {
						strAppXxxValueS = "";
						strAppXxxValueL = "";
					} else {
						strAppXxxValueS = strAppXxxValueL;
					}
					break;
				case "D":
					if (numDhSeq > 1) {
						strItemId = strXxxBh + "_" + String.valueOf((numDhSeq - 1));
					} else {
						strItemId = strXxxBh;
					}
					/*
					 * mapAppXxxOptionJson = new HashMap<String, Object>();
					 * String sqlGetAppXxxOption =
					 * "SELECT TZ_XXXKXZ_MC,TZ_XXXKXZ_MS FROM PS_TZ_APPXXX_KXZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_BH = ? ORDER BY TZ_ORDER"
					 * ; String strXxxkxzMc = ""; String strXxxkxzMs = "";
					 * String strIsChecked = ""; int numOption = 0; List<?>
					 * appXxxOptionList =
					 * sqlQuery.queryForList(sqlGetAppXxxOption, new Object[] {
					 * strTplId,strXxxBh }); for (Object appXxxOptionObj :
					 * appXxxOptionList) { Map<String, Object> mapAppXxxOption =
					 * (Map<String, Object>) appXxxOptionObj; strXxxkxzMc =
					 * mapAppXxxOption.get("TZ_XXXKXZ_MC") == null ? "" :
					 * String.valueOf(mapAppXxxOption.get("TZ_XXXKXZ_MC"));
					 * strXxxkxzMs = mapAppXxxOption.get("TZ_XXXKXZ_MS") == null
					 * ? "" :
					 * String.valueOf(mapAppXxxOption.get("TZ_XXXKXZ_MS"));
					 * 
					 * String strOptionSlid = ""; switch(strXxxSlid){ case
					 * "rec_sex": if("M".equals(strXxxkxzMc)){ strOptionSlid =
					 * "MAN"; }else{ strOptionSlid = "WOMAN"; } break; case
					 * "rec_way": if("S".equals(strXxxkxzMc)){ strOptionSlid =
					 * "SEND"; }else{ strOptionSlid = "UPLOAD"; } break; case
					 * "rec_language": if("C".equals(strXxxkxzMc)){
					 * strOptionSlid = "ZHS"; }else{ strOptionSlid = "ENG"; }
					 * break; } numOption++; String sqlIsChecked =
					 * tzSQLObject.getSQLText(
					 * "SQL.TZWebsiteApplicationBundle.TZ_GETDHXXXCHILD_VALUE5")
					 * ; strIsChecked = sqlQuery.queryForObject(sqlIsChecked,
					 * new Object[] { numAppInsId, strDxxxBh,strXxxBhLike +
					 * "%",strXxxBh, strXxxkxzMc,numDhSeq - 1}, "String");
					 * if(strIsChecked == "Y"){ strAppXxxValueS = strXxxkxzMc;
					 * }else{ strIsChecked = "N"; } Map<String, Object>
					 * mapAppXxxOptionJson1 = new HashMap<String, Object>();
					 * mapAppXxxOptionJson1.put("code", strXxxkxzMc);
					 * mapAppXxxOptionJson1.put("txt", strXxxkxzMs);
					 * mapAppXxxOptionJson1.put("other", "N");
					 * mapAppXxxOptionJson1.put("checked", strIsChecked);
					 * 
					 * mapAppXxxOptionJson.put(strOptionSlid,
					 * mapAppXxxOptionJson1); }
					 */

					break;
				case "F":

					if (numDhSeq > 1) {
						strItemId = strXxxBh + "_" + String.valueOf((numDhSeq - 1));
					} else {
						strItemId = strXxxBh;
					}

					String strAppxxxChildrenFbh = "";

					String sqlGetAppxxxChildrenFbh = "";
					sqlGetAppxxxChildrenFbh = tzSQLObject
							.getSQLText("SQL.TZWebsiteApplicationBundle.TZ_GETDHXXXCHILD_VALUE4");
					strAppxxxChildrenFbh = sqlQuery.queryForObject(sqlGetAppxxxChildrenFbh, new Object[] { numAppInsId,
							strDxxxBh, strXxxBhLike + "%", strXxxBh, strComLmc, numDhSeq - 1 }, "String");
					String strXxxMc = "";
					System.out.println("EDU attr:itemID->" + strAppxxxChildrenFbh);
					String strFileIndex = "";
					// if(strAppxxxChildrenFbh != null){
					//
					// String sqlGetFile = "SELECT
					// TZ_XXX_MC,TZ_INDEX,ATTACHSYSFILENAME,ATTACHUSERFILE FROM
					// PS_TZ_FORM_ATT_VW2 "
					// + "WHERE TZ_APP_INS_ID = ? AND TZ_D_XXX_BH = ? AND
					// TZ_XXX_BH = ? AND TZ_XXX_NO = ? AND TZ_COM_LMC = ?";
					//
					// Map<String, Object> mapAppFileObj =
					// sqlQuery.queryForMap(sqlGetFile,
					// new Object[] {
					// numAppInsId,strDxxxBh,strAppxxxChildrenFbh,strXxxBh,strComLmc
					// });
					// System.out.println("EDU attr:mapAppFileObj->"+new
					// JacksonUtil().Map2json(mapAppFileObj));
					// if(mapAppFileObj != null) {
					// strXxxMc = mapAppFileObj.get("TZ_XXX_MC") == null ? "" :
					// String.valueOf(mapAppFileObj.get("TZ_XXX_MC"));
					// strFileIndex = mapAppFileObj.get("TZ_INDEX") == null ? ""
					// : String.valueOf(mapAppFileObj.get("TZ_INDEX"));
					// strSysFileName = mapAppFileObj.get("ATTACHSYSFILENAME")
					// == null ? "" :
					// String.valueOf(mapAppFileObj.get("ATTACHSYSFILENAME"));
					// strUseFileName = mapAppFileObj.get("ATTACHUSERFILE") ==
					// null ? "" :
					// String.valueOf(mapAppFileObj.get("ATTACHUSERFILE"));
					// if(strXxxMc.length()>17){
					// String suffix =
					// strSysFileName.substring(strSysFileName.lastIndexOf(".")
					// + 1);
					// if("".equals(suffix) || suffix == null){
					// strViewFileName = strUseFileName;
					// }else{
					// strViewFileName = strOprNameApp + "_" +
					// strXxxMc.substring(1, 15) + "..._" + strFileIndex + "." +
					// suffix;
					// }
					// }else{
					// strViewFileName = strUseFileName;
					// }
					// }
					// }
					arrAppFileJson = new ArrayList<Map<String, Object>>();
					if (strAppxxxChildrenFbh != null) {
						strXxxMc = "";
						strSysFileName = "";
						strUseFileName = "";
						strViewFileName = "";
						strFileIndex = "";
						String accPath = "";
						String sqlGetFile = "SELECT TZ_ACCESS_PATH,TZ_XXX_MC,TZ_INDEX,ATTACHSYSFILENAME,ATTACHUSERFILE FROM PS_TZ_FORM_ATT_VW2 "
								+ "WHERE TZ_APP_INS_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH = ? AND TZ_XXX_NO = ? AND TZ_COM_LMC = ?";

						List<?> appFileList = sqlQuery.queryForList(sqlGetFile,
								new Object[] { numAppInsId, strDxxxBh, strAppxxxChildrenFbh, strXxxBh, strComLmc });
						// System.out.println("attr:itemID"+strAppxxxChildrenFbh+"
						// itemMc:"+strComLmc);
						// System.out.println("attrData:"+new
						// JacksonUtil().List2json((ArrayList<?>) appFileList));
						for (Object appFileObj : appFileList) {
							Map<String, Object> mapAppFileObj = (Map<String, Object>) appFileObj;
							strXxxMc = mapAppFileObj.get("TZ_XXX_MC") == null ? ""
									: String.valueOf(mapAppFileObj.get("TZ_XXX_MC"));
							strFileIndex = mapAppFileObj.get("TZ_INDEX") == null ? ""
									: String.valueOf(mapAppFileObj.get("TZ_INDEX"));
							// 绝对路径:TZ_ACCESS_PATH
							accPath = mapAppFileObj.get("TZ_ACCESS_PATH") == null ? ""
									: mapAppFileObj.get("TZ_ACCESS_PATH").toString();
							strSysFileName = mapAppFileObj.get("ATTACHSYSFILENAME") == null ? ""
									: String.valueOf(mapAppFileObj.get("ATTACHSYSFILENAME"));
							// strSysFileName=accPath+strSysFileName;
							strUseFileName = mapAppFileObj.get("ATTACHUSERFILE") == null ? ""
									: String.valueOf(mapAppFileObj.get("ATTACHUSERFILE"));
							if (strXxxMc.length() > 17) {
								String suffix = strSysFileName.substring(strSysFileName.lastIndexOf(".") + 1);
								if ("".equals(suffix) || suffix == null) {
									strViewFileName = strUseFileName;
								} else {
									strViewFileName = strOprNameApp + "_" + strXxxMc.substring(1, 15) + "..._"
											+ strFileIndex + "." + suffix;
								}
							} else {
								strViewFileName = strUseFileName;
							}

							Map<String, Object> mapAppFileJson = new HashMap<String, Object>();
							mapAppFileJson.put("itemId", "attachment_Upload");
							mapAppFileJson.put("orderby", strFileIndex);
							mapAppFileJson.put("fileName", strUseFileName);
							mapAppFileJson.put("sysFileName", strSysFileName);
							// accessPath
							mapAppFileJson.put("accessPath", accPath);
							mapAppFileJson.put("viewFileName", strViewFileName);

							arrAppFileJson.add(mapAppFileJson);
						}
					} else {
						System.out.println("read Data fail!");
						Map<String, Object> mapAppFileJson = new HashMap<String, Object>();
						mapAppFileJson.put("itemId", "attachment_Upload");
						mapAppFileJson.put("orderby", "");
						mapAppFileJson.put("fileName", "");
						mapAppFileJson.put("sysFileName", "");
						mapAppFileJson.put("viewFileName", "");

						arrAppFileJson.add(mapAppFileJson);
					}
					break;
				default:
					;
				}
			}
			if (flag) {
				Map<String, Object> mapXxxInfo = new HashMap<String, Object>();

				mapXxxInfo.put("instanceId", strXxxSlid);
				mapXxxInfo.put("itemId", strItemId);
				mapXxxInfo.put("classname", strComLmc);
				mapXxxInfo.put("isDoubleLine", "N");
				mapXxxInfo.put("isSingleLine", "N");
				mapXxxInfo.put("value", strAppXxxValueS);
				mapXxxInfo.put("wzsm", strAppXxxValueL);
				// 什么意思:
				// if("F".equals(strXxxCclx)){
				// mapXxxInfo.put("filename", "");
				// mapXxxInfo.put("sysFileName", "");
				// mapXxxInfo.put("path", "");
				// mapXxxInfo.put("accessPath", "");
				// }
				// 放入附件信息:
				if (arrAppFileJson != null) {
					mapXxxInfo.put("children", arrAppFileJson);
				}
				// 返回Map对象
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(strXxxSlid, mapXxxInfo);
				// JacksonUtil jacksonUtil = new JacksonUtil();
				// System.out.println("World:"+ jacksonUtil.Map2json(map));
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 多行容器
	private Map<String, Object> getDhXxxInfoJson(Long numAppInsId, String strTplId, String strXxxBh, String itemId,
			int numDhSeq, String strOprNameApp, String pComMc) {
		// strXxxBh=“TZ_TZ_5_1” ”TZ_TZ_5_2“
		// 报名表使用模版编号
		String strAppTplIdHis = "";
		// 报名表信息项存储类型
		String strXxxCclx = "";
		String strComLmc = "";
		String strXxxSlid = "";

		// 是否带入
		boolean flag = true;

		String strAppXxxValueS = "";
		String strAppXxxValueL = "";

		String sql = "";

		Map<String, Object> mapAppXxxOptionJson = null;

		try {
			ArrayList<Map<String, Object>> arrAppFileJson = null;

			List<Map<String, Object>> arrAppChildrenJson = null;

			sql = "SELECT TZ_APP_TPL_ID FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID = ?";
			strAppTplIdHis = sqlQuery.queryForObject(sql, new Object[] { numAppInsId }, "String");

			// 查看是否在容器中
			String strDxxxBh = "";
			String strXxxBhLike = "";

			String sqlGetDxxxBh = "SELECT TZ_D_XXX_BH FROM PS_TZ_TEMP_FIELD_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ? LIMIT 1";

			strDxxxBh = sqlQuery.queryForObject(sqlGetDxxxBh, new Object[] { strAppTplIdHis, strXxxBh }, "String");
			if (!"".equals(strDxxxBh) && strDxxxBh != null) {
				strXxxBhLike = strDxxxBh + strXxxBh;
			} else {
				strDxxxBh = strXxxBh;
				strXxxBhLike = strXxxBh;
			}

			String strItemId = "";

			sql = "SELECT TZ_XXX_SLID,TZ_COM_LMC,TZ_XXX_CCLX FROM PS_TZ_APP_XXXPZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_BH = ?";

			Map<String, Object> MapXxxPz = sqlQuery.queryForMap(sql, new Object[] { strTplId, strXxxBh });
			if (MapXxxPz != null) {
				strXxxCclx = MapXxxPz.get("TZ_XXX_CCLX") == null ? "" : String.valueOf(MapXxxPz.get("TZ_XXX_CCLX"));
				strXxxSlid = MapXxxPz.get("TZ_XXX_SLID") == null ? "" : String.valueOf(MapXxxPz.get("TZ_XXX_SLID"));
				strComLmc = MapXxxPz.get("TZ_COM_LMC") == null ? "" : String.valueOf(MapXxxPz.get("TZ_COM_LMC"));
				String sqlGetValue = "";
				switch (strXxxCclx) {
				case "S":
					sqlGetValue = tzSQLObject.getSQLText("SQL.TZWebsiteApplicationBundle.TZ_GETDHXXXCHILD_VALUE");

					Map<String, Object> MapXxxValue = sqlQuery.queryForMap(sqlGetValue, new Object[] { numAppInsId,
							strDxxxBh, strXxxBhLike + "%", strXxxBh, strComLmc, numDhSeq - 1 });

					if (MapXxxValue != null) {
						strAppXxxValueS = MapXxxValue.get("TZ_APP_S_TEXT") == null ? ""
								: String.valueOf(MapXxxValue.get("TZ_APP_S_TEXT"));
						strAppXxxValueL = MapXxxValue.get("TZ_APP_L_TEXT") == null ? ""
								: String.valueOf(MapXxxValue.get("TZ_APP_L_TEXT"));

					}

					if (numDhSeq > 1) {
						strItemId = strXxxBh + "_" + String.valueOf((numDhSeq - 1));
					} else {
						strItemId = strXxxBh;
					}

					if ("Select".equals(strComLmc)) {
						// 如果是下拉列表
						String strExistsOption = "";
						String sqlOption = "SELECT 'Y' FROM PS_TZ_APPXXX_KXZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_BH = ? AND TZ_XXXKXZ_MC = ?";
						strExistsOption = sqlQuery.queryForObject(sqlOption,
								new Object[] { strTplId, strXxxBh, strAppXxxValueS }, "String");
						if ("".equals(strExistsOption) || strExistsOption == null) {
							strAppXxxValueS = "";
							strAppXxxValueL = "";
							flag = false;
						}
					}
					break;
				case "L":
					if (numDhSeq > 1) {
						strItemId = strXxxBh + "_" + String.valueOf((numDhSeq - 1));
					} else {
						strItemId = strXxxBh;
					}
					sqlGetValue = tzSQLObject.getSQLText("SQL.TZWebsiteApplicationBundle.TZ_GETDHXXXCHILD_VALUE");
					Map<String, Object> MapXxxValueL = sqlQuery.queryForMap(sqlGetValue, new Object[] { numAppInsId,
							strDxxxBh, strXxxBhLike + "%", strXxxBh, strComLmc, numDhSeq - 1 });
					if (MapXxxValueL != null) {
						strAppXxxValueS = MapXxxValueL.get("TZ_APP_S_TEXT") == null ? ""
								: String.valueOf(MapXxxValueL.get("TZ_APP_S_TEXT"));
						strAppXxxValueL = MapXxxValueL.get("TZ_APP_L_TEXT") == null ? ""
								: String.valueOf(MapXxxValueL.get("TZ_APP_L_TEXT"));
					}
					if (strAppXxxValueL == null) {
						strAppXxxValueS = "";
						strAppXxxValueL = "";
					} else {
						strAppXxxValueS = strAppXxxValueL;
					}
					break;
				case "D":
					if (numDhSeq > 1) {
						strItemId = strXxxBh + "_" + String.valueOf((numDhSeq - 1));
					} else {
						strItemId = strXxxBh;
					}
					mapAppXxxOptionJson = new HashMap<String, Object>();
					String sqlGetAppXxxOption = "SELECT TZ_XXXKXZ_MC,TZ_XXXKXZ_MS FROM PS_TZ_APPXXX_KXZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_BH = ? ORDER BY TZ_ORDER";
					String strXxxkxzMc = "";
					String strXxxkxzMs = "";
					String strIsChecked = "";
					int numOption = 0;
					List<?> appXxxOptionList = sqlQuery.queryForList(sqlGetAppXxxOption,
							new Object[] { strTplId, strXxxBh });
					for (Object appXxxOptionObj : appXxxOptionList) {
						Map<String, Object> mapAppXxxOption = (Map<String, Object>) appXxxOptionObj;
						strXxxkxzMc = mapAppXxxOption.get("TZ_XXXKXZ_MC") == null ? ""
								: String.valueOf(mapAppXxxOption.get("TZ_XXXKXZ_MC"));
						strXxxkxzMs = mapAppXxxOption.get("TZ_XXXKXZ_MS") == null ? ""
								: String.valueOf(mapAppXxxOption.get("TZ_XXXKXZ_MS"));
						numOption++;
						String sqlIsChecked = tzSQLObject
								.getSQLText("SQL.TZWebsiteApplicationBundle.TZ_GETDHXXXCHILD_VALUE3");
						strIsChecked = sqlQuery.queryForObject(sqlIsChecked, new Object[] { numAppInsId, strDxxxBh,
								strXxxBhLike + "%", strXxxBh, strXxxkxzMs, strXxxkxzMc, numDhSeq - 1 }, "String");
						if (strIsChecked == null || "".equals(strIsChecked)) {
							strIsChecked = "N";
						}
						Map<String, Object> mapAppXxxOptionJson1 = new HashMap<String, Object>();
						mapAppXxxOptionJson1.put("code", strXxxkxzMc);
						mapAppXxxOptionJson1.put("txt", strXxxkxzMs);
						mapAppXxxOptionJson1.put("other", "N");
						mapAppXxxOptionJson1.put("checked", strIsChecked);

						mapAppXxxOptionJson.put(strXxxSlid + String.valueOf(numOption), mapAppXxxOptionJson1);
					}

					break;
				case "F":
					/**/
					if (numDhSeq > 1) {
						strItemId = strXxxBh + "_" + String.valueOf((numDhSeq - 1));
					} else {
						strItemId = strXxxBh;
					}

					String strAppxxxChildrenFbh = "";

					String sqlGetAppxxxChildrenFbh = "";
					sqlGetAppxxxChildrenFbh = tzSQLObject
							.getSQLText("SQL.TZWebsiteApplicationBundle.TZ_GETDHXXXCHILD_VALUE4");
					strAppxxxChildrenFbh = sqlQuery.queryForObject(sqlGetAppxxxChildrenFbh, new Object[] { numAppInsId,
							strDxxxBh, strXxxBhLike + "%", strXxxBh, strComLmc, numDhSeq - 1 }, "String");
					arrAppFileJson = new ArrayList<Map<String, Object>>();
					if (strAppxxxChildrenFbh != null) {
						String strXxxMc = "";
						String strSysFileName = "";
						String strUseFileName = "";
						String strViewFileName = "";
						String strFileIndex = "";
						String accPath = "";
						String sqlGetFile = "SELECT TZ_ACCESS_PATH,TZ_XXX_MC,TZ_INDEX,ATTACHSYSFILENAME,ATTACHUSERFILE FROM PS_TZ_FORM_ATT_VW2 "
								+ "WHERE TZ_APP_INS_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH = ? AND TZ_XXX_NO = ? AND TZ_COM_LMC = ?";

						List<?> appFileList = sqlQuery.queryForList(sqlGetFile,
								new Object[] { numAppInsId, strDxxxBh, strAppxxxChildrenFbh, strXxxBh, strComLmc });
						// System.out.println("attr:itemID"+strAppxxxChildrenFbh);
						// System.out.println("attrData:"+new
						// JacksonUtil().List2json((ArrayList<?>) appFileList));
						for (Object appFileObj : appFileList) {
							Map<String, Object> mapAppFileObj = (Map<String, Object>) appFileObj;
							strXxxMc = mapAppFileObj.get("TZ_XXX_MC") == null ? ""
									: String.valueOf(mapAppFileObj.get("TZ_XXX_MC"));
							strFileIndex = mapAppFileObj.get("TZ_INDEX") == null ? ""
									: String.valueOf(mapAppFileObj.get("TZ_INDEX"));
							// 绝对路径:TZ_ACCESS_PATH
							accPath = mapAppFileObj.get("TZ_ACCESS_PATH") == null ? ""
									: mapAppFileObj.get("TZ_ACCESS_PATH").toString();
							strSysFileName = mapAppFileObj.get("ATTACHSYSFILENAME") == null ? ""
									: String.valueOf(mapAppFileObj.get("ATTACHSYSFILENAME"));
							// strSysFileName=accPath+strSysFileName;
							strUseFileName = mapAppFileObj.get("ATTACHUSERFILE") == null ? ""
									: String.valueOf(mapAppFileObj.get("ATTACHUSERFILE"));
							if (strXxxMc.length() > 17) {
								String suffix = strSysFileName.substring(strSysFileName.lastIndexOf(".") + 1);
								if ("".equals(suffix) || suffix == null) {
									strViewFileName = strUseFileName;
								} else {
									strViewFileName = strOprNameApp + "_" + strXxxMc.substring(1, 15) + "..._"
											+ strFileIndex + "." + suffix;
								}
							} else {
								strViewFileName = strUseFileName;
							}

							Map<String, Object> mapAppFileJson = new HashMap<String, Object>();
							mapAppFileJson.put("itemId", "attachment_Upload");
							mapAppFileJson.put("orderby", strFileIndex);
							mapAppFileJson.put("fileName", strUseFileName);
							mapAppFileJson.put("sysFileName", strSysFileName);
							// accessPath
							mapAppFileJson.put("accessPath", accPath);
							mapAppFileJson.put("viewFileName", strViewFileName);

							arrAppFileJson.add(mapAppFileJson);
						}
					} else {
						System.out.println("read attr Data fail!");
						Map<String, Object> mapAppFileJson = new HashMap<String, Object>();
						mapAppFileJson.put("itemId", "attachment_Upload");
						mapAppFileJson.put("orderby", "");
						mapAppFileJson.put("fileName", "");
						mapAppFileJson.put("sysFileName", "");
						mapAppFileJson.put("viewFileName", "");

						arrAppFileJson.add(mapAppFileJson);
					} /**/
					// flag = false;
					break;
				default:
					arrAppChildrenJson = new ArrayList<Map<String, Object>>();
					if ("BirthdayAndAge".equals(strComLmc) || "DateComboBox".equals(strComLmc)
							|| "mobilePhone".equals(strComLmc) || "CertificateNum".equals(strComLmc)
							|| "StartBusinessExp".equals(strComLmc) || "FirmType".equals(strComLmc)
							|| "University".equals(strComLmc) || "MailingAddress".equals(strComLmc)
							|| "YearsAndMonth".equals(strComLmc)) {

						String strAppxxxChildrenBh = "";
						String strAppxxxChildrenValue = "";
						String strAppxxxChildrenComLmc = "";
						if ("StartBusinessExp".equals(strComLmc) || "FirmType".equals(strComLmc)) {

							arrAppChildrenJson.add(this.getGpCtrlsData(strComLmc, numAppInsId, strTplId, strXxxBh,
									itemId, strOprNameApp));
							// 查询item通用数据 是唯一的
							final String NORMAL_DATA_SQL = "SELECT TZ_XXX_MC,TZ_COM_LMC,TZ_ORDER,TZ_XXX_CCLX,TZ_XXX_SLID FROM PS_TZ_APP_XXXPZ_T WHERE TZ_APP_TPL_ID=? AND TZ_XXX_BH=?";
							Map<String, Object> normalDataMap = sqlQuery.queryForMap(NORMAL_DATA_SQL,
									new Object[] { strTplId, strXxxBh });
							// System.out.println("normalDataMap"+normalDataMap);
							//
							String topInsId = normalDataMap.get("TZ_XXX_SLID").toString();

							strItemId = strXxxBh;
							// topInsId:
							strXxxSlid = topInsId;

						} else {
							String sqlGetXxxChildren = "SELECT TZ_XXX_BH FROM PS_TZ_RQ_XXXPZ_T "
									+ "WHERE TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? ORDER BY TZ_ORDER";

							List<?> appChildList = sqlQuery.queryForList(sqlGetXxxChildren,
									new Object[] { strTplId, strXxxBh });
							for (Object appChildObj : appChildList) {
								Map<String, Object> mapAppChildObj = (Map<String, Object>) appChildObj;
								strAppxxxChildrenBh = mapAppChildObj.get("TZ_XXX_BH") == null ? ""
										: String.valueOf(mapAppChildObj.get("TZ_XXX_BH"));

								String sqlGetChildrenValue = tzSQLObject
										.getSQLText("SQL.TZWebsiteApplicationBundle.TZ_GETDHXXXCHILD_VALUE2");
								Map<String, Object> mapGetChildrenValue = sqlQuery
										.queryForMap(sqlGetChildrenValue,
												new Object[] { numAppInsId, strDxxxBh,
														strXxxBhLike + strAppxxxChildrenBh + "%", strAppxxxChildrenBh,
														numDhSeq - 1 });
								if (mapGetChildrenValue != null) {
									strAppxxxChildrenValue = mapGetChildrenValue.get("TZ_APP_S_TEXT") == null ? ""
											: String.valueOf(mapGetChildrenValue.get("TZ_APP_S_TEXT"));
									strAppxxxChildrenComLmc = mapGetChildrenValue.get("TZ_COM_LMC") == null ? ""
											: String.valueOf(mapGetChildrenValue.get("TZ_COM_LMC"));
									Map<String, Object> mapAppChildJson = new HashMap<String, Object>();
									mapAppChildJson.put("itemId", strAppxxxChildrenBh);
									mapAppChildJson.put("classname", strAppxxxChildrenComLmc);
									mapAppChildJson.put("value", strAppxxxChildrenValue);
									arrAppChildrenJson.add(mapAppChildJson);
								}
							}
						}
					}
					if (arrAppChildrenJson.isEmpty())
						flag = false;
				}
			}
			if (flag) {

				Map<String, Object> mapXxxInfo = new HashMap<String, Object>();
				mapXxxInfo.put("instanceId", strXxxSlid);
				mapXxxInfo.put("itemId", strItemId);
				mapXxxInfo.put("isDoubleLine", "N");

				if ("BirthdayAndAge".equals(strComLmc) || "DateComboBox".equals(strComLmc)
						|| "University".equals(strComLmc) || "mobilePhone".equals(strComLmc)
						|| "CertificateNum".equals(strComLmc) || "StartBusinessExp".equals(strComLmc)
						|| "FirmType".equals(strComLmc) || "MailingAddress".equals(strComLmc)
						|| "YearsAndMonth".equals(strComLmc)) {
					mapXxxInfo.put("isSingleLine", "Y");
				} else {
					mapXxxInfo.put("isSingleLine", "N");
				}
				mapXxxInfo.put("value", strAppXxxValueS);
				mapXxxInfo.put("wzsm", strAppXxxValueL);
				mapXxxInfo.put("classname", strComLmc);
				if (mapAppXxxOptionJson != null) {
					mapXxxInfo.put("option", mapAppXxxOptionJson);
				}

				if (arrAppFileJson != null) {
					mapXxxInfo.put("children", arrAppFileJson);
				}

				if (arrAppChildrenJson != null) {
					mapXxxInfo.put("children", arrAppChildrenJson);
				}
				// 返回Map对象
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(strXxxSlid, mapXxxInfo);
				JacksonUtil jacksonUtil = new JacksonUtil();
				// System.out.println("mapXxxInfo:"+jacksonUtil.Map2json(mapXxxInfo));
				// System.out.println("World:"+ jacksonUtil.Map2json(map));
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//// 解析分组框中的:"职业背景","创业经历"控件数据 //'FirmType','StartBusinessExp'
	public Map<String, Object> getGpCtrlsData(String strComLmc, Long numAppInsId, String strTplId, String strXxxBhChild,
			String itemId, String strOprNameApp) {
		Map<String, Object> childMap = new HashMap<String, Object>();

		ArrayList<String> keyGp = null;
		ArrayList<String> bgGp = null;
		String comFlag = "";
		if (strComLmc.equals("FirmType")) {
			// WorkExper+keyGp[index]
			keyGp = new ArrayList<String>();
			bgGp = new ArrayList<String>();
			String str[] = { "1", "2" };
			String bgStr[] = { "firm_type", "position_type" };
			for (int n = 0; n < str.length; n++) {
				keyGp.add(str[n]);
				bgGp.add(bgStr[n]);
			}
		} else if (strComLmc.equals("StartBusinessExp")) {
			keyGp = new ArrayList<String>();
			bgGp = new ArrayList<String>();

			String str[] = { "1", "2", "2_1", "2_2", "2_3", "3", "4", "5", "6", "7", "8", "9" };
			String bgStr[] = { "business_type", "financing_type", "financing_binput", "financing_ainput",
					"financing_anginput", "income_y", "user_num", "own_money", "family_money", "income_o",
					"year_profit", "firm_scale" };

			for (int n = 0; n < str.length; n++) {
				keyGp.add(str[n]);
				bgGp.add(bgStr[n]);
			}
		}
		for (int index = 0; index < bgGp.size(); index++) {
			// Map<String,Object>map=list.get(index);
			// String insId=map.get("TZ_XXX_NO").toString();
			// String childId=map.get("TZ_XXX_BH").toString();

			// 模糊查询结果 是个LIST 可以通过确定"TZ_XXX_BH"查询唯一结果:
			// comFlag=insId;
			String insId = bgGp.get(index);
			comFlag = bgGp.get(index);
			String LIKE_SQL = "SELECT TZ_XXX_BH,TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID=? AND TZ_XXX_BH LIKE '%"
					+ itemId + strXxxBhChild + comFlag + "%'";
			// System.out.println("LIKE_SQL:"+LIKE_SQL);
			Map<String, Object> valMap = sqlQuery.queryForMap(LIKE_SQL, new Object[] { numAppInsId });
			if ("FirmType".equals(strComLmc))
				;

			String tempStr = "WorkExper" + keyGp.get(index);
			// System.out.println("valMap:"+valMap);

			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("instanceId", tempStr);
			tempMap.put("itemId", insId);
			String val = "";
			if (valMap != null && valMap.get("TZ_APP_S_TEXT") != null)
				val = valMap.get("TZ_APP_S_TEXT").toString();
			if (val.equals("") && valMap != null && valMap.get("TZ_APP_L_TEXT") != null) {
				val = valMap.get("TZ_APP_L_TEXT").toString();
			}
			tempMap.put("value", val);
			// tempMap.put("StorageType",normalDataMap.get("TZ_XXX_CCLX"));
			// tempMap.put("orderby", normalDataMap.get("TZ_ORDER"));
			// tempMap.put("classname","Select");
			childMap.put(tempStr, tempMap);
		}
		return childMap;

	}

	// 解析"职业背景","创业经历"控件数据 //'FirmType','StartBusinessExp'
	public Map<String, Object> getGpCtrlData(String strComLmc, Long numAppInsId, String strTplId, String strXxxBhChild,
			String itemId, String strOprNameApp) {
		// Map<String,Object>returnMap=new HashMap<String,Object>();
		Map<String, Object> childMap = new HashMap<String, Object>();
		// System.out.println("strComLmc:"+strComLmc);
		// 查询所有实例ID为“numAppInsId”的页面数据
		// final String SQL1="SELECT TZ_XXX_BH,TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T
		// WHERE TZ_APP_INS_ID=? ";

		String LIKE_SQL = "SELECT TZ_XXX_BH,TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID=? AND TZ_XXX_BH LIKE '%"
				+ itemId + strXxxBhChild + "%'";
		// 查询item通用数据
		final String NORMAL_DATA_SQL = "SELECT TZ_XXX_MC,TZ_COM_LMC,TZ_ORDER,TZ_XXX_CCLX,TZ_XXX_SLID FROM PS_TZ_APP_XXXPZ_T WHERE TZ_APP_TPL_ID=? AND TZ_XXX_BH=?";

		// System.out.println("LIKE_SQL:"+LIKE_SQL);
		// System.out.println("NORMAL_DATA_SQL:"+NORMAL_DATA_SQL);
		// "strXxxBhChild"在"FirmType"中的值为:firm_type和position_type
		Map<String, Object> valMap = sqlQuery.queryForMap(LIKE_SQL, new Object[] { numAppInsId });
		Map<String, Object> normalDataMap = sqlQuery.queryForMap(NORMAL_DATA_SQL,
				new Object[] { strTplId, strXxxBhChild });
		// System.out.println("valMap:"+valMap);
		// System.out.println("normalDataMap"+normalDataMap);

		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("instanceId", normalDataMap.get("TZ_XXX_SLID"));
		tempMap.put("itemId", strXxxBhChild);
		tempMap.put("itemName", normalDataMap.get("TZ_XXX_MC"));
		tempMap.put("title", normalDataMap.get("TZ_XXX_MC"));
		String val = "";
		if (valMap != null && valMap.get("TZ_APP_S_TEXT") != null)
			val = valMap.get("TZ_APP_S_TEXT").toString();
		if (val.equals("") && valMap != null && valMap.get("TZ_APP_L_TEXT") != null) {
			val = valMap.get("TZ_APP_L_TEXT").toString();
		}
		tempMap.put("value", val);
		tempMap.put("StorageType", normalDataMap.get("TZ_XXX_CCLX"));
		tempMap.put("orderby", normalDataMap.get("TZ_ORDER"));
		tempMap.put("classname", normalDataMap.get("TZ_COM_LMC"));

		childMap.put(normalDataMap.get("TZ_XXX_SLID").toString(), tempMap);

		// "children"这一层数据 原代码已经做过解析
		// returnMap.put("children", childMap);

		// System.out.println("childMap:"+childMap);
		return childMap;
	}
}
