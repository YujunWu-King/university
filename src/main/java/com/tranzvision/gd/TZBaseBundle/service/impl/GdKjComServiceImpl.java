package com.tranzvision.gd.TZBaseBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.Framework;
import com.tranzvision.gd.TZBaseBundle.service.GdKjComService;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.OperateType;
import com.tranzvision.gd.util.base.TZUtility;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author tang 2015-10-23 功能说明：高端产品-招生系统框架通用类
 */

@Service
public class GdKjComServiceImpl extends GdObjectServiceImpl implements GdKjComService {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private JacksonUtil jacksonUtil;
	@Autowired
	private ApplicationContext ctx;

	/**
	 * 获取框架级错误、警告、提示等描述信息的方法
	 * 该方法使得框架级错误、警告、提示等描述信息可以通过可配置的方法实现，而不是在mainapp.js程序中写死
	 *****/
	@Override
	public String getFrameworkDescriptionResources(HttpServletRequest request, HttpServletResponse response) {
		String tmpJSONString = "";
		/*** 读取客户化的系统名称; **/
		String jgId = this.getLoginOrgID(request, response);
		String SuperOrgId = this.getSuperOrgId(request, response);
		String tmpKhhSystemNameSQL = "SELECT TZ_JG_LOGIN_INFO FROM PS_TZ_JG_BASE_T WHERE TZ_JG_ID=?";
		String tmpKhhSystemName = "";
		tmpKhhSystemName = TZUtility
				.transFormchar(jdbcTemplate.queryForObject(tmpKhhSystemNameSQL, new Object[] { jgId }, "String"));

		String languageId = this.getLoginLanguage(request, response);
		String xxjhId = "TZGD_FWINIT_MSGSET";
		// 查询登录机构下的系统消息定义;
		List<Map<String, Object>> list = null;
		String loginMsgSQL = "select A.TZ_XXJH_ID, A.TZ_MSG_ID,ifnull(B.TZ_MSG_TEXT,A.TZ_MSG_TEXT) TZ_MSG_TEXT from PS_TZ_PT_XXDY_TBL A left join PS_TZ_PT_XXDY_TBL B on A.TZ_XXJH_ID = B.TZ_XXJH_ID and A.TZ_JG_ID=B.TZ_JG_ID and A.TZ_MSG_ID=B.TZ_MSG_ID where upper(B.TZ_LANGUAGE_ID)=upper(?) and upper(A.TZ_LANGUAGE_ID)=(SELECT UPPER(TZ_HARDCODE_VAL) TZ_LANGUAGE_CD FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZGD_BASIC_LANGUAGE' ) AND A.TZ_XXJH_ID=? AND  UPPER(A.TZ_JG_ID)=UPPER(?)";
		list = jdbcTemplate.queryForList(loginMsgSQL, new Object[] { languageId, xxjhId, jgId });

		String tmpMsgSetID = "";
		String tmpMsgID = "";
		String tmpMsgText = "";
		Map<String, Object> map = null;
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				map = list.get(i);
				tmpMsgSetID = (String) map.get("TZ_XXJH_ID");
				tmpMsgID = (String) map.get("TZ_MSG_ID");
				tmpMsgText = (String) map.get("TZ_MSG_TEXT");
				// 如果用户指定了客户化的系统名称，则使用客户化的系统名称;
				if ("TZGD_FWINIT_MSGSET".equals(tmpMsgSetID) && "TZGD_FWINIT_00003".equals(tmpMsgID)
						&& (tmpKhhSystemName != null || !"".equals(tmpKhhSystemName))) {
					tmpMsgText = tmpKhhSystemName;
				}
				tmpJSONString = tmpJSONString + ",\"" + TZUtility.transFormchar(tmpMsgID) + "\":\""
						+ TZUtility.transFormchar(tmpMsgText) + "\"";
			}
		}

		if (jgId != null && !jgId.equals(SuperOrgId)) {
			String superMsgSQL = "select A.TZ_XXJH_ID, A.TZ_MSG_ID,ifnull(B.TZ_MSG_TEXT,A.TZ_MSG_TEXT) TZ_MSG_TEXT from PS_TZ_PT_XXDY_TBL A left join PS_TZ_PT_XXDY_TBL B on A.TZ_XXJH_ID = B.TZ_XXJH_ID and A.TZ_JG_ID=B.TZ_JG_ID and A.TZ_MSG_ID=B.TZ_MSG_ID where upper(B.TZ_LANGUAGE_ID)=upper(?) and upper(A.TZ_LANGUAGE_ID)=(SELECT UPPER(TZ_HARDCODE_VAL) TZ_LANGUAGE_CD FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZGD_BASIC_LANGUAGE' ) AND A.TZ_XXJH_ID=? AND  UPPER(A.TZ_JG_ID)=UPPER(?) AND NOT exists(SELECT 'Y' FROM PS_TZ_PT_XXDY_TBL WHERE TZ_XXJH_ID=? AND UPPER(A.TZ_JG_ID)=UPPER(?))";
			list = jdbcTemplate.queryForList(superMsgSQL,
					new Object[] { languageId, xxjhId, SuperOrgId, xxjhId, jgId });

			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					map = list.get(i);
					tmpMsgSetID = (String) map.get("TZ_XXJH_ID");
					tmpMsgID = (String) map.get("TZ_MSG_ID");
					tmpMsgText = (String) map.get("TZ_MSG_TEXT");
					// 如果用户指定了客户化的系统名称，则使用客户化的系统名称;
					if ("TZGD_FWINIT_MSGSET".equals(tmpMsgSetID) && "TZGD_FWINIT_00003".equals(tmpMsgID)
							&& (tmpKhhSystemName != null || !"".equals(tmpKhhSystemName))) {
						tmpMsgText = tmpKhhSystemName;
					}
					tmpJSONString = tmpJSONString + ",\"" + TZUtility.transFormchar(tmpMsgID) + "\":\""
							+ TZUtility.transFormchar(tmpMsgText) + "\"";
				}
			}
		}

		if (tmpJSONString != null && !"".equals(tmpJSONString)) {
			tmpJSONString = tmpJSONString.substring(1);
		}
		tmpJSONString = "{ " + languageId + ":{" + tmpJSONString + "}}";

		return tmpJSONString;

	}

	@Override
	/******************************************************************
	 *** 开发时间：2015-09-30 开发人：tang 控件标签 功能说明：根据标签ID获取对应的标签描述，若未找到对应值，则添加
	 * 一条数据到数据库，标签描述为传入的默认标签描述。 参数说明：sCID 标签ID sDefaultText 默认标签内容
	 *******************************************************************/
	public String getFieldTag(HttpServletRequest request, HttpServletResponse response, String sCID,
			String sDefaultText, String[] errMsgArr) {
		// 返回值;
		String strRet = "{}";
		// 语言编号;
		String strLangID = this.getLoginLanguage(request, response);
		// 标签内容;
		String strTagContent = "";
		// 按.分割;
		String[] ary = sCID.split("\\.");
		// 当前登录人归属机构编号;
		String tmpLoginOrgId = this.getLoginOrgID(request, response);

		if (ary.length != 3) {
			errMsgArr[0] = "1";
			errMsgArr[1] = "传入参数的格式不对。";
			return strRet;
		}

		try {

			// 读取客户化的系统名称;
			String selectSql1 = "SELECT TZ_MSG_TEXT FROM PS_TZ_PT_XXDY_TBL WHERE TZ_JG_ID=? AND TZ_MSG_BQID=? AND TZ_LANGUAGE_ID=?";
			strTagContent = jdbcTemplate.queryForObject(selectSql1, new Object[] { tmpLoginOrgId, sCID, strLangID },
					"String");

			if ((strTagContent == null || "".equals(strTagContent))
					&& (tmpLoginOrgId != null && !"".equals(tmpLoginOrgId))) {
				strTagContent = sDefaultText;
				/* 添加一条数据到数据库，标签描述为传入的默认标签描述 */
				int total = 0;
				String xxjhSQL = "SELECT count(1) FROM PS_TZ_PT_XXJH_TBL WHERE TZ_XXJH_ID=?";
				total = jdbcTemplate.queryForObject(xxjhSQL, new Object[] { ary[0] }, "Integer");

				if (total <= 0) {
					String insertXxjhSQL = "insert into PS_TZ_PT_XXJH_TBL(TZ_XXJH_ID, TZ_XXJH_MC) values(?,?)";
					jdbcTemplate.update(insertXxjhSQL, new Object[] { ary[0], "" });

				}

				int numCount = 0;
				String tmpMsgId = "";
				String maxMsgIdSQL = "SELECT MAX(TZ_MSG_ID) FROM PS_TZ_PT_XXDY_TBL WHERE TZ_JG_ID=? AND TZ_MSG_BQID=? AND TZ_LANGUAGE_ID<>?";
				tmpMsgId = jdbcTemplate.queryForObject(maxMsgIdSQL, new Object[] { tmpLoginOrgId, sCID, strLangID },
						"String");

				if (tmpMsgId == null || "".equals(tmpMsgId)) {
					SimpleDateFormat form = new SimpleDateFormat("yyyyMMddHHmmss");
					Date date = new Date();
					tmpMsgId = form.format(date);
					String countSQL = "SELECT COUNT(1) FROM PS_TZ_PT_XXDY_TBL WHERE TZ_XXJH_ID=? AND TZ_JG_ID=? AND TZ_MSG_ID LIKE ? AND TZ_LANGUAGE_ID=?";
					numCount = jdbcTemplate.queryForObject(countSQL,
							new Object[] { ary[0], tmpLoginOrgId, tmpMsgId + "%", strLangID }, "Integer");

					String numCountString = "0000" + (numCount);
					tmpMsgId = tmpMsgId
							+ numCountString.substring(numCountString.length() - 4, numCountString.length());
				}

				String insertXxdySQL = "INSERT INTO PS_TZ_PT_XXDY_TBL(TZ_XXJH_ID,TZ_MSG_ID,TZ_LANGUAGE_ID,TZ_MSG_TEXT,TZ_MSG_BQID,TZ_MSG_DESC,TZ_JG_ID) VALUES(?,?,?,?,?,?,?)";
				jdbcTemplate.update(insertXxdySQL, new Object[] { ary[0], tmpMsgId, strLangID, strTagContent, sCID,
						strTagContent, tmpLoginOrgId });

			}

			if (strTagContent == null || "".equals(strTagContent)) {
				strTagContent = sDefaultText;
			}

			String[] tmpPrefixArray = sCID.split("\\.");
			String retBiaoQian = "";
			if (tmpPrefixArray.length >= 3) {
				String tmpPrefixCID = tmpPrefixArray[0] + "." + tmpPrefixArray[1] + ".%";
				String tmpSQLText = "SELECT TZ_MSG_BQID,TZ_MSG_TEXT FROM PS_TZ_PT_XXDY_TBL WHERE TZ_JG_ID=? AND TZ_MSG_BQID LIKE ? AND TZ_LANGUAGE_ID=?";
				List<Map<String, Object>> list = null;
				list = jdbcTemplate.queryForList(tmpSQLText, new Object[] { tmpLoginOrgId, tmpPrefixCID, strLangID });

				String tmpBQID = "";
				String tmpBQValue = "";
				if (list != null) {
					for (int k = 0; k < list.size(); k++) {
						tmpBQID = (String) list.get(k).get("TZ_MSG_BQID");
						tmpBQValue = (String) list.get(k).get("TZ_MSG_TEXT");
						if ("".equals(retBiaoQian)) {
							retBiaoQian = "\"" + TZUtility.transFormchar(tmpBQID) + "\":\""
									+ TZUtility.transFormchar(tmpBQValue) + "\"";
						} else {
							retBiaoQian = retBiaoQian + ",\"" + TZUtility.transFormchar(tmpBQID) + "\":\""
									+ TZUtility.transFormchar(tmpBQValue) + "\"";
						}
					}
				}

			} else {
				retBiaoQian = "\"" + TZUtility.transFormchar(sCID) + "\":\"" + TZUtility.transFormchar(strTagContent)
						+ "\"";
			}
			strRet = "{\"" + strLangID + "\":{\"languagePackage\":{" + retBiaoQian + "}}}";
		} catch (Exception e) {
			e.printStackTrace();
			errMsgArr[0] = "1";
			errMsgArr[1] = "发生错误。";
			return strRet;

		}

		return strRet;
	}

	@Override
	/* 根据字段名称获取TransValue值 */
	public String getTransValue(HttpServletRequest request, HttpServletResponse response, String sFieldName) {
		// 返回报文;
		String strRet = "";
		// 转化报文内容;
		String strTransContent = "";
		// 转换值，转换值短描述，转换值长描述;
		String strTransID = "", strTransSDesc = "", strTransLDesc = "", strLanguageId = "";

		// 根据当前语言环境和有效性获取TranslateValue值;
		if ("ENG".equals(this.getLoginLanguage(request, response))) {
			strLanguageId = "ENG";
		} else {
			strLanguageId = "ZHS";
		}

		try {
			List<Map<String, Object>> list = null;
			String sqlList = "SELECT B.TZ_ZHZ_ID ,ifnull((SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_LNG WHERE TZ_ZHZJH_ID=B.TZ_ZHZJH_ID AND TZ_ZHZ_ID=B.TZ_ZHZ_ID AND TZ_LANGUAGE_ID=?),B.TZ_ZHZ_DMS) TZ_ZHZ_DMS ,ifnull((SELECT TZ_ZHZ_CMS FROM PS_TZ_PT_ZHZXX_LNG WHERE TZ_ZHZJH_ID=B.TZ_ZHZJH_ID AND TZ_ZHZ_ID=B.TZ_ZHZ_ID AND TZ_LANGUAGE_ID=?),B.TZ_ZHZ_CMS) TZ_ZHZ_CMS FROM PS_TZ_PT_ZHZJH_TBL A,PS_TZ_PT_ZHZXX_TBL B WHERE B.TZ_EFF_STATUS='A' AND  A.TZ_ZHZJH_ID=B.TZ_ZHZJH_ID AND A.TZ_ZHZJH_ID=? AND curdate()>=B.TZ_EFF_DATE";
			list = jdbcTemplate.queryForList(sqlList, new Object[] { strLanguageId, strLanguageId, sFieldName });

			if (list != null) {
				for (int num = 0; num < list.size(); num++) {
					strTransID = (String) list.get(num).get("TZ_ZHZ_ID");
					strTransSDesc = (String) list.get(num).get("TZ_ZHZ_DMS");
					strTransLDesc = (String) list.get(num).get("TZ_ZHZ_CMS");
					if (num == 0) {
						strTransContent = "{\"TValue\": \"" + strTransID + "\",\"TSDesc\": \"" + strTransSDesc
								+ "\",\"TLDesc\": \"" + strTransLDesc + "\"}";
					} else {
						strTransContent = strTransContent + ",{\"TValue\": \"" + strTransID + "\",\"TSDesc\": \""
								+ strTransSDesc + "\",\"TLDesc\": \"" + strTransLDesc + "\"}";
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return strRet;
		}
		strRet = "{\"" + sFieldName + "\":[" + strTransContent + "]}";
		return strRet;
	}

	/* 功能说明：根据组件ID获取该组件下所有页面使用到的资源 */
	public String getComResourses(HttpServletRequest request, HttpServletResponse response, String sComID)
			throws Exception {

		// 返回值;
		String strRet = "";
		// 语言编号;
		String strLangID = "";
		strLangID = this.getLoginLanguage(request, response);
		// 页面资源内容;
		String strPageContent = "";
		// 页面标签内容;
		String strPageTagCont = "";
		// 登录人ID;
		String strUserID = "";
		// 页面id，是否外部链接，外部URL，是否新开窗口，客户端处理JS类，是否默认首页;
		String sPageID = "", isExternalURL = "", externalURL = "", isNewWin = "", jsClassName = "", isDefault = "";
		// 默认首页页面ID;
		String sDefaultPageID = "";
		// 标签ID，标签内容;
		String strTagID = "", strTagContent = "";
		// 组件页面标签;
		String sCID = "";
		int numTag = 0;

		strUserID = this.getOPRID(request);
		boolean rsExistsFlag = false;

		try {
			String sqlComList = "SELECT DISTINCT C.TZ_PAGE_ID FROM PSROLEUSER A,PSROLECLASS B,PS_TZ_AQ_COMSQ_TBL C WHERE A.ROLEUSER=? AND A.DYNAMIC_SW='N' AND A.ROLENAME = B.ROLENAME AND B.CLASSID=C.CLASSID AND (C.TZ_COM_ID= ? OR C.TZ_COM_ID LIKE ? ) AND (C.DISPLAYONLY=1 OR C.TZ_EDIT_FLG=1)";
			List<Map<String, Object>> list = null;
			list = jdbcTemplate.queryForList(sqlComList, new Object[] { strUserID, sComID, sComID + "$%" });

			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					sPageID = (String) list.get(i).get("TZ_PAGE_ID");
					rsExistsFlag = true;
					String pageInfoSQL = "SELECT TZ_PAGE_ISWBURL,TZ_PAGE_WBURL,TZ_PAGE_NEWWIN,TZ_PAGE_KHDJS,TZ_PAGE_MRSY FROM PS_TZ_AQ_PAGZC_TBL WHERE (TZ_COM_ID=? OR TZ_COM_ID LIKE ?) AND TZ_PAGE_ID=? limit 0,1";
					Map<String, Object> map = null;
					map = jdbcTemplate.queryForMap(pageInfoSQL, new Object[] { sComID, sComID + "$%", sPageID });

					isExternalURL = (String) map.get("TZ_PAGE_ISWBURL");
					externalURL = (String) map.get("TZ_PAGE_WBURL");
					isNewWin = (String) map.get("TZ_PAGE_NEWWIN");
					jsClassName = (String) map.get("TZ_PAGE_KHDJS");
					isDefault = (String) map.get("TZ_PAGE_MRSY");

					// 获取该页面下的标签资源;
					sCID = sComID + "." + sPageID + "%";
					List<Map<String, Object>> BqList = null;
					String BqListSQL = "select ifnull(B.TZ_MSG_BQID,A.TZ_MSG_BQID) TZ_MSG_BQID,ifnull(B.TZ_MSG_TEXT,A.TZ_MSG_TEXT) TZ_MSG_TEXT from PS_TZ_PT_XXDY_TBL A left join PS_TZ_PT_XXDY_TBL B on A.TZ_XXJH_ID = B.TZ_XXJH_ID and A.TZ_JG_ID=B.TZ_JG_ID and A.TZ_MSG_ID=B.TZ_MSG_ID where upper(B.TZ_LANGUAGE_ID)=upper(?) and upper(A.TZ_LANGUAGE_ID)=(SELECT UPPER(TZ_HARDCODE_VAL) TZ_LANGUAGE_CD FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZGD_BASIC_LANGUAGE' ) AND A.TZ_XXJH_ID=? AND  UPPER(A.TZ_JG_ID)=UPPER(?) AND (A.TZ_MSG_BQID LIKE ? or B.TZ_MSG_BQID LIKE ?)";
					BqList = jdbcTemplate.queryForList(BqListSQL,
							new Object[] { strLangID, sComID, this.getLoginOrgID(request, response), sCID, sCID });

					if (BqList != null) {
						for (int j = 0; j < BqList.size(); j++) {
							strTagID = (String) BqList.get(j).get("TZ_MSG_BQID");
							strTagContent = (String) BqList.get(j).get("TZ_MSG_TEXT");
							numTag = numTag + 1;
							if (numTag == 1) {
								strPageTagCont = "{\"tagID\":\"" + strTagID + "\", \"tagName\":\"" + strTagContent
										+ "\" }";
							} else {
								strPageTagCont = strPageTagCont + ",{\"tagID\":\"" + strTagID + "\", \"tagName\":\""
										+ strTagContent + "\" }";
							}
						}
					}

					if (i == 0) {
						strPageContent = "{\"pageID\":\"" + sPageID + "\", \"isexternalURL\":\"" + isExternalURL
								+ "\",  \"externalURL\":\"" + externalURL + "\",  \"isNewWin\":\"" + isNewWin
								+ "\", \"jsClassName\":\"" + jsClassName + "\", \"tagResources\":[" + strPageTagCont
								+ "]}";
					} else {
						strPageContent = strPageContent + ",{\"pageID\":\"" + sPageID + "\", \"isexternalURL\":\""
								+ isExternalURL + "\",  \"externalURL\":\"" + externalURL + "\",  \"isNewWin\":\""
								+ isNewWin + "\", \"jsClassName\":\"" + jsClassName + "\", \"tagResources\":["
								+ strPageTagCont + "]}";
					}

					// 默认首页;
					if ("Y".equals(isDefault)) {
						sDefaultPageID = sPageID;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return strRet;
		}

		// 没有获得指定的组件相关资源;
		if (rsExistsFlag == false) {
			throw new Exception("无法获取指定组件['" + sComID + "']对应的相关代码资源。");
		}

		strRet = "{\"defaultPageID\":\"" + sDefaultPageID + "\", \"comResources\":[" + strPageContent + "]}";

		return strRet;
	}

	/* 用户请求操作分发器逻辑 */
	@SuppressWarnings("unchecked")
	public String userRequestDispatcher(HttpServletRequest request, HttpServletResponse response, String sComID,
			String sPageID, String strOprType, String comParams, String[] errMsgArr) {
		// 返回值;
		String strRet = "{}";
		// 组件ID是否注册，页面ID是否注册;
		String isExistCom = "", isExistPage = "";
		// 服务端AppClass;
		String strAppClass = "";
		// 当前登录人;
		String strUserID = this.getOPRID(request);
		// 更新权限,查看权限;
		int update = 0, view = 0;
		// 客户端js类;
		// String strJS;

		if (sComID == null || "".equals(sComID) || sPageID == null || "".equals(sPageID)) {
			errMsgArr[0] = "1";
			errMsgArr[1] = "非法访问，未指定组件ID或者页面ID。";
			return strRet;
		}

		try {
			String isZcSQL = "SELECT 'Y' FROM PS_TZ_AQ_COMZC_TBL WHERE TZ_COM_ID=?";
			isExistCom = jdbcTemplate.queryForObject(isZcSQL, new Object[] { sComID }, "String");

			if (!"Y".equals(isExistCom)) {
				errMsgArr[0] = "1";
				errMsgArr[1] = "非法访问，该组件[" + sComID + "]未注册。";
				return strRet;
			}

			String isPageSQL = "SELECT 'Y' FROM PS_TZ_AQ_PAGZC_TBL WHERE TZ_COM_ID=? AND TZ_PAGE_ID=?";
			isExistPage = jdbcTemplate.queryForObject(isPageSQL, new Object[] { sComID, sPageID }, "String");

			if (!"Y".equals(isExistPage)) {
				errMsgArr[0] = "1";
				errMsgArr[1] = "非法访问，组件页面[" + sComID + "][" + sPageID + "]未注册。";
				return strRet;
			}

			/* 是否存在组件配置的权限 */
			String existSQL = "SELECT COUNT(1) FROM PSROLEUSER A,PSROLECLASS B,PS_TZ_AQ_COMSQ_TBL C WHERE A.ROLEUSER=? AND A.DYNAMIC_SW='N' AND A.ROLENAME = B.ROLENAME AND B.CLASSID=C.CLASSID AND (C.TZ_COM_ID=? OR C.TZ_COM_ID LIKE ?) AND C.TZ_PAGE_ID=?";
			int existComQx = 0;
			try {
				existComQx = jdbcTemplate.queryForObject(existSQL,
						new Object[] { strUserID, sComID, sComID + "$%", sPageID }, "Integer");
			} catch (Exception e) {
				existComQx = 0;
			}
			if (existComQx == 0) {
				errMsgArr[0] = "1";
				errMsgArr[1] = "非法访问，您对组件页面[" + sComID + "][" + sPageID + "]的访问未获得授权。";
				return strRet;
			}

			/***** 是否有访问权限 ******/
			/*
			boolean debug = false;
			try {
				Resource resource = new ClassPathResource("conf/cookieSession.properties");
				Properties properties = PropertiesLoaderUtils.loadProperties(resource);
				debug = Boolean.parseBoolean(properties.getProperty("debug"));
				System.out.println("========debug================>" + properties.getProperty("debug"));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("========================>error");
			}
		    */
			// 更新权限;
			String haveUpdateSQL = "SELECT C.TZ_EDIT_FLG FROM PSROLEUSER A,PSROLECLASS B,PS_TZ_AQ_COMSQ_TBL C WHERE A.ROLEUSER=? AND A.DYNAMIC_SW='N' AND A.ROLENAME = B.ROLENAME AND B.CLASSID=C.CLASSID AND (C.TZ_COM_ID=? OR C.TZ_COM_ID LIKE ?) AND C.TZ_PAGE_ID=? ORDER BY C.TZ_EDIT_FLG DESC limit 0,1";
			try {
				update = jdbcTemplate.queryForObject(haveUpdateSQL,
						new Object[] { strUserID, sComID, sComID + "$%", sPageID }, "Integer");
			} catch (Exception e) {
				update = 0;
			}

			if (update != 1) {
				// 查看权限;
				String haveReadSQL = "SELECT  C.DISPLAYONLY FROM PSROLEUSER A,PSROLECLASS B,PS_TZ_AQ_COMSQ_TBL C WHERE A.ROLEUSER=? AND A.DYNAMIC_SW='N' AND A.ROLENAME = B.ROLENAME AND B.CLASSID=C.CLASSID AND (C.TZ_COM_ID=? OR C.TZ_COM_ID LIKE ?) AND C.TZ_PAGE_ID=? ORDER BY  C.DISPLAYONLY limit 0,1";
				try {
					view = jdbcTemplate.queryForObject(haveReadSQL,
							new Object[] { strUserID, sComID, sComID + "$%", sPageID }, "Integer");
				} catch (Exception e) {
					view = 0;
				}
				if (view != 1) {
					errMsgArr[0] = "1";
					errMsgArr[1] = "非法访问，您对组件页面[" + sComID + "][" + sPageID + "]的访问未获得授权。";
					return strRet;
				}
			}

			// 获取服务端AppClass;
			String appClassSQL = "SELECT TZ_PAGE_FWDCLS FROM PS_TZ_AQ_PAGZC_TBL WHERE TZ_COM_ID=? AND TZ_PAGE_ID=?";
			strAppClass = jdbcTemplate.queryForObject(appClassSQL, new Object[] { sComID, sPageID }, "String");

			if (strAppClass == null || "".equals(strAppClass)) {
				errMsgArr[0] = "1";
				errMsgArr[1] = "配置错误，未配置组件页面[" + sComID + "][" + sPageID + "]对应的服务器端处理程序。";
				return strRet;
			}

			Framework obj = (Framework) ctx.getBean(strAppClass);
			switch (OperateType.getOperateType(strOprType)) {
			// 查询表单;
			case QF:

				strRet = obj.tzQuery(comParams, errMsgArr);
				break;
			// 查询列表;
			case QG:

				// parameterTypes = new String[] { "String", "int", "int",
				// "String[]" };
				int numLimit = 0, numStart = 0;
				String limit = request.getParameter("limit");
				String strStart = request.getParameter("start");

				if (limit != null && !"".equals(limit) && StringUtils.isNumeric(limit)) {
					numLimit = Integer.parseInt(limit);
				}

				if (strStart != null && !"".equals(strStart) && StringUtils.isNumeric(strStart)) {
					numStart = Integer.parseInt(strStart);
				}

				strRet = obj.tzQueryList(comParams, numLimit, numStart, errMsgArr);

				break;
			case U:
				// 将字符串转换成json;
				jacksonUtil.json2Map(comParams);

				// 操作数据;
				String[] strActData = null;
				// JSONArray jsonArray = null;
				List<Map<String, Object>> jsonArray = null;
				int num = 0;

				if (jacksonUtil.containsKey("add")) {
					jsonArray = (List<Map<String, Object>>) jacksonUtil.getList("add");
					if (jsonArray != null && jsonArray.size() > 0) {
						strActData = new String[jsonArray.size()];
						for (num = 0; num < jsonArray.size(); num++) {
							strActData[num] = jacksonUtil.Map2json(jsonArray.get(num));
						}

						strRet = obj.tzAdd(strActData, errMsgArr);
					}
				}

				jacksonUtil.json2Map(comParams);
				if (jacksonUtil.containsKey("update")) {
					jsonArray = (List<Map<String, Object>>) jacksonUtil.getList("update");
					if (jsonArray != null && jsonArray.size() > 0) {
						strActData = new String[jsonArray.size()];
						for (num = 0; num < jsonArray.size(); num++) {
							strActData[num] = jacksonUtil.Map2json(jsonArray.get(num));
						}

						strRet = obj.tzUpdate(strActData, errMsgArr);
					}

				}

				jacksonUtil.json2Map(comParams);
				if (jacksonUtil.containsKey("delete")) {
					jsonArray = (List<Map<String, Object>>) jacksonUtil.getList("delete");
					if (jsonArray != null && jsonArray.size() > 0) {
						strActData = new String[jsonArray.size()];
						for (num = 0; num < jsonArray.size(); num++) {
							strActData[num] = jacksonUtil.Map2json(jsonArray.get(num));
						}

						strRet = obj.tzDelete(strActData, errMsgArr);
					}
				}
				break;
			// 获取html内容;
			case HTML:

				strRet = obj.tzGetHtmlContent(comParams);
				break;
			// 获取json格式的html内容;
			case JHTML:
				strRet = obj.tzGetHtmlData(comParams);
				break;
			// 获取json格式的数据;
			case EJSON:
				strRet = obj.tzGetJsonData(comParams);
				break;
			// tzOther;
			default:
				strRet = obj.tzOther(strOprType, comParams, errMsgArr);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsgArr[0] = "1";
			errMsgArr[1] = e.toString();
			return strRet;
		}

		return strRet;
	}

	@Override
	/*
	 * 功能说明：根据搜索条件获取满足条件中的数据，供放大镜使用 参数说明：&recname 搜索表或视图名称 &condition 搜索条件
	 * &result 搜索结果字段 &maxRow 搜索最大行
	 */
	public String getPromptSearchList(String recname, String condition, String result, String maxRow, int numLimit,
			int numStart, String[] errMsgArr) {
		// 返回值;
		String strRet = "";
		// 数据内容;
		String strContent = "";
		// 总记录数;
		int total = 0;
		// 搜索最大行;
		int maxNum;
		if (maxRow != null && StringUtils.isNumeric(maxRow)) {
			maxNum = Integer.parseInt(maxRow);
		} else {
			maxNum = 0;
		}

		String resultSelectFlds = "";

		try {

			// 将字符串转换成json;
			// JSONObject conJson = PaseJsonUtil.getJson(condition);
			jacksonUtil.json2Map(condition);
			int i, j;
			// 搜索字段名称;
			String key = "";

			// 操作符;
			String operate = "";
			// 搜索字段值;
			String value = "";
			// 搜索字段类型：01-字符串，02-数字，03-日期，04-时间，05-日期时间;
			String type;
			// 将搜索字段内容转成json;
			// JSONObject fieldJson;
			// 结果字段按逗号分隔;
			String[] aryResult = result.split(",");
			int resultSelectFldsLen = aryResult.length;
			if (resultSelectFldsLen == 0) {
				errMsgArr[0] = "1";
				errMsgArr[1] = "未设置搜索结果字段";
				return strRet;
			}

			int tableNameCount = 0;
			String tableName = recname;
			String tableNameSql = "select COUNT(1) from information_schema.tables where TABLE_NAME=?";
			tableNameCount = jdbcTemplate.queryForObject(tableNameSql, new Object[] { recname }, "Integer");
			if (tableNameCount <= 0) {
				tableName = "PS_" + recname;
			}

			// 类型为number的值;
			String intTypeString = "TINYINT,SMALLINT,MEDIUMINT,INT,INTEGER,BIGINT,FLOAT,DOUBLE,DECIMAL";

			String resultFldTypeSQL = "SELECT  COLUMN_NAME, DATA_TYPE from information_schema.COLUMNS WHERE TABLE_NAME=?";
			List<Map<String, Object>> list = null;
			try {
				list = jdbcTemplate.queryForList(resultFldTypeSQL, new Object[] { tableName });
			} catch (Exception e) {

			}
			/** 字段类型 **/
			String columnNanme = "";
			String dateType = "";
			if (list != null) {
				for (i = 0; i < list.size(); i++) {
					columnNanme = (String) list.get(i).get("COLUMN_NAME");
					if (result.contains(columnNanme)) {
						dateType = ((String) list.get(i).get("DATA_TYPE")).toUpperCase();

						if (intTypeString.contains(dateType)) {
							// 数字;
							resultSelectFlds = resultSelectFlds + ", CONCAT(ifnull(" + columnNanme + ",'0'),'')";
						} else if ("DATE".equals(dateType)) {
							// 是否是日期;
							resultSelectFlds = resultSelectFlds + ", ifnull(date_format(" + columnNanme
									+ ",'%Y-%m-%d'),'')";

						} else if ("TIME".equals(dateType)) {
							// 是否是时间;
							resultSelectFlds = resultSelectFlds + ", ifnull(date_format(" + columnNanme
									+ ",'%H:%i'),'')";

						} else if ("DATETIME".equals(dateType) || "TIMESTAMP".equals(dateType)) {
							// 是否日期时间;
							resultSelectFlds = resultSelectFlds + ", ifnull(date_format(" + columnNanme
									+ ",'%Y-%m-%d %H:%i'),'')";
						} else {
							// 字符串;
							resultSelectFlds = resultSelectFlds + ", ifnull(" + columnNanme + ",'')";
						}
					}
				}
			}

			if ("".equals(resultSelectFlds)) {
				errMsgArr[0] = "1";
				errMsgArr[1] = "未设置搜索结果字段";
				return strRet;
			} else {
				resultSelectFlds = resultSelectFlds.substring(1);
			}

			// 搜索条件;
			String sqlWhere = "";
			if (jacksonUtil.containsKey("presetFields")) {
				try {
					// JSONObject presetJson =
					// PaseJsonUtil.getJson(presetFields);
					Map<String, Object> presetJson = jacksonUtil.getMap("presetFields");
					for (Map.Entry<String, Object> entry : presetJson.entrySet()) {
						// 搜索字段名称;
						key = entry.getKey();
						// 搜索字段内容;
						// 搜索字段内容;
						@SuppressWarnings("unchecked")
						Map<String, Object> keyContent = (Map<String, Object>) entry.getValue();

						if (keyContent.containsKey("value")) {
							value = (String) keyContent.get("value");
						} else {
							value = "";
						}

						if (keyContent.containsKey("type")) {
							type = (String) keyContent.get("type");
						} else {
							type = "";
						}

						if ("".equals(value) || "".equals(type)) {
							continue;
						}

						if ("".equals(sqlWhere)) {
							sqlWhere = " WHERE ";
						} else {
							sqlWhere = sqlWhere + " AND ";
						}

						// 搜索字段类型;
						if ("0".equals(type.substring(0, 1))) {
							type = type.substring(1);
						}

						switch (Integer.parseInt(type)) {
						case 1:
							value = "'" + value + "'";
							break;
						case 3:
							value = " str_to_date('" + value + "','%Y-%m-%d')";
							break;
						case 4:
							value = " str_to_date('" + value + "','%H:%i')";
							break;
						case 5:
							value = " str_to_date('" + value + "','%Y-%m-%d %H:%i')";
							break;
						default:
							break;
						}

						sqlWhere = sqlWhere + key + "=" + value;
					}
				} catch (Exception e) {

				}
			}

			if (jacksonUtil.containsKey("srhConFields")) {
				try {
					// JSONObject srhConJson =
					// PaseJsonUtil.getJson(srhConFields);
					Map<String, Object> srhConJson = jacksonUtil.getMap("srhConFields");
					for (Map.Entry<String, Object> entry : srhConJson.entrySet()) {
						// 搜索字段名称;
						key = entry.getKey();
						// 搜索字段内容;
						@SuppressWarnings("unchecked")
						Map<String, Object> keyContent = (Map<String, Object>) entry.getValue();
						// 操作符;
						operate = (String) keyContent.get("operator");

						if (keyContent.containsKey("value")) {
							value = (String) keyContent.get("value");
						} else {
							value = "";
						}

						if (keyContent.containsKey("type")) {
							type = (String) keyContent.get("type");
						} else {
							type = "";
						}

						if ("".equals(value) || "".equals(type)) {
							continue;
						}

						if ("".equals(sqlWhere)) {
							sqlWhere = " WHERE ";
						} else {
							sqlWhere = sqlWhere + " AND ";
						}

						// 搜索字段类型;
						if ("0".equals(type.substring(0, 1))) {
							type = type.substring(1);
						}

						String originValue = value;
						switch (Integer.parseInt(type)) {
						case 1:
							value = "'" + value + "'";
							break;
						case 3:
							value = " str_to_date('" + value + "','%Y-%m-%d')";
							break;
						case 4:
							value = " str_to_date('" + value + "','%H:%i')";
							break;
						case 5:
							value = " str_to_date('" + value + "','%Y-%m-%d %H:%i')";
							break;
						default:
							break;
						}

						// 操作符;
						if ("0".equals(operate.substring(0, 1))) {
							operate = operate.substring(1);
						}

						switch (Integer.parseInt(operate)) {
						case 1:
							// 等于;
							operate = "=";
							sqlWhere = sqlWhere + key + operate + value;
							break;
						case 2:
							// 不等于;
							operate = "<>";
							sqlWhere = sqlWhere + key + operate + value;
							break;
						case 3:
							// 大于;
							operate = ">";
							sqlWhere = sqlWhere + key + operate + value;
							break;
						case 4:
							// 大于等于;
							operate = ">=";
							sqlWhere = sqlWhere + key + operate + value;
							break;
						case 5:
							// 小于;
							operate = "<";
							sqlWhere = sqlWhere + key + operate + value;
							break;
						case 6:
							// 小于等于;
							operate = "<=";
							sqlWhere = sqlWhere + key + operate + value;
							break;
						case 7:
							// 包含;
							value = "'%" + originValue + "%'";
							sqlWhere = sqlWhere + key + " LIKE " + value;
							break;
						case 8:
							// 开始于…;
							value = "'" + originValue + "%'";
							sqlWhere = sqlWhere + key + " LIKE " + value;
							break;
						case 9:
							// 结束于…;
							value = "'%" + originValue + "'";
							sqlWhere = sqlWhere + key + " LIKE " + value;
							break;
						case 10:
							originValue = originValue.replaceAll(" ", "");
							originValue = originValue.trim();
							String[] inArr = originValue.split(",");

							int inArrLen = inArr.length;
							if (inArrLen > 0) {
								value = "";
								if ("1".equals(type)) {
									for (int ii = 0; ii < inArrLen; ii++) {
										originValue = originValue + ",'" + inArr[ii] + "'";
									}

								} else {
									for (int ii = 0; ii < inArrLen; ii++) {
										originValue = originValue + "," + inArr[ii];
									}
								}
								originValue = value.substring(1);
								originValue = "(" + originValue + ")";
							}

							sqlWhere = sqlWhere + key + " IN " + originValue;
							break;
						case 11:
							// 为空;
							sqlWhere = sqlWhere + key + " IS NULL";
							break;
						case 12:
							// 不为空;
							sqlWhere = sqlWhere + key + " IS NOT NULL";
							break;

						default:
							sqlWhere = sqlWhere + key + "=" + value;
							break;
						}

					}
				} catch (Exception e) {

				}
			}

			String totalSQL = "SELECT COUNT(1) FROM " + tableName + sqlWhere;
			try {
				total = (int) jdbcTemplate.queryForObject(totalSQL, "Integer");
			} catch (Exception e) {

			}

			// 总数;
			if (maxNum > 0 && total > maxNum) {
				total = maxNum;
			}

			if (total == 0 || numStart >= total) {
				numStart = 0;
			}

			// 查看开始行数+限制的行数是否大于最大显示行数;
			if (maxNum > 0 && (numStart + numLimit) > maxNum) {
				numLimit = maxNum - numStart;
			}

			// 查询结果;
			String sqlList = "";
			if (numLimit == 0 && numStart == 0) {
				sqlList = "SELECT " + resultSelectFlds + " FROM " + tableName + sqlWhere;
			} else {
				sqlList = "SELECT " + resultSelectFlds + " FROM " + tableName + sqlWhere + " limit ?,?";
			}

			List<Map<String, Object>> resultlist = null;
			if (numLimit != 0) {
				resultlist = jdbcTemplate.queryForList(sqlList, new Object[] { numStart, numLimit });
			} else if (numLimit == 0 && numStart > 0) {
				resultlist = jdbcTemplate.queryForList(sqlList, new Object[] { numStart, total - numStart });
			} else {
				resultlist = jdbcTemplate.queryForList(sqlList);
			}

			for (int resultlist_i = 0; resultlist_i < resultlist.size(); resultlist_i++) {
				strContent = "";
				Map<String, Object> resultMap = resultlist.get(resultlist_i);
				j = 0;
				for (Object vl : resultMap.values()) {
					strContent = strContent + ",\"" + aryResult[j] + "\":\"" + vl + "\"";
					j++;
				}
				strContent = strContent.substring(1);
				strContent = "{" + strContent + "}";

				strRet = strRet + "," + strContent;

			}

			strRet = strRet.substring(1);

		} catch (Exception e) {
			e.printStackTrace();
			errMsgArr[0] = "1";
			errMsgArr[1] = e.toString();
		}
		strRet = "{\"total\":" + total + ",\"root\":[" + strRet + "]}";
		return strRet;
	}

	@SuppressWarnings("unchecked")
	@Override
	/* 下拉框数据 */
	public String getComboxValue(String recname, String condition, String result, String[] errMsgArr) {
		String strRet = "";
		// 数据内容;
		String strContent = "";
		try {
			// 将字符串转换成json;
			// JSONObject conJson = PaseJsonUtil.getJson(condition);

			int i, j;
			// 搜索字段名称;
			String key = "";
			// 操作符;
			String operate = "";
			// 搜索字段值;
			String value = "";
			// 搜索字段类型：01-字符串，02-数字，03-日期，04-时间，05-日期时间;
			String type = "";
			// 结果字段按逗号分隔;
			String[] aryResult = result.split(",");
			int resultSelectFldsLen = aryResult.length;
			if (resultSelectFldsLen == 0) {
				errMsgArr[0] = "1";
				errMsgArr[1] = "未设置搜索结果字段";
				return strRet;
			}

			// 类型为number的值;
			// String intTypeString =
			// "TINYINT,SMALLINT,MEDIUMINT,INT,INTEGER,BIGINT,FLOAT,DOUBLE,DECIMAL";

			String strSql = "SELECT " + result + " FROM " + recname;

			int tableNameCount = 0;
			String tableNameSql = "select COUNT(1) from information_schema.tables where TABLE_NAME=?";
			tableNameCount = jdbcTemplate.queryForObject(tableNameSql, new Object[] { recname }, "Integer");
			if (tableNameCount <= 0) {
				strSql = "SELECT " + result + " FROM PS_" + recname;
			}

			jacksonUtil.json2Map(condition);
			Map<String, Object> conJson = jacksonUtil.getMap();
			i = 0;

			for (Map.Entry<String, Object> entry : conJson.entrySet()) {
				// 搜索字段名称;
				key = entry.getKey();
				// 搜索字段内容;
				Map<String, Object> keyContent = (Map<String, Object>) entry.getValue();

				// 操作符;
				operate = (String) keyContent.get("operator");
				if (keyContent.containsKey("value") && keyContent.containsKey("type")) {
					value = (String) keyContent.get("value");
					type = (String) keyContent.get("type");
				} else {
					continue;
				}

				if (i == 0) {
					strSql = strSql + " WHERE ";
				} else {
					strSql = strSql + " AND ";
				}
				i++;

				// 搜索字段类型;
				if ("0".equals(type.substring(0, 1))) {
					type = type.substring(1);
				}

				String originValue = value;
				switch (Integer.parseInt(type)) {
				case 1:
					value = "'" + value + "'";
					break;
				case 3:
					value = " str_to_date('" + value + "','%Y-%m-%d')";
					break;
				case 4:
					value = " str_to_date('" + value + "','%H:%i')";
					break;
				case 5:
					value = " str_to_date('" + value + "','%Y-%m-%d %H:%i')";
					break;
				default:
					break;
				}

				// 操作符;
				if ("0".equals(operate.substring(0, 1))) {
					operate = operate.substring(1);
				}

				switch (Integer.parseInt(operate)) {
				case 1:
					// 等于;
					operate = "=";
					strSql = strSql + key + operate + value;
					break;
				case 2:
					// 不等于;
					operate = "<>";
					strSql = strSql + key + operate + value;
					break;
				case 3:
					// 大于;
					operate = ">";
					strSql = strSql + key + operate + value;
					break;
				case 4:
					// 大于等于;
					operate = ">=";
					strSql = strSql + key + operate + value;
					break;
				case 5:
					// 小于;
					operate = "<";
					strSql = strSql + key + operate + value;
					break;
				case 6:
					// 小于等于;
					operate = "<=";
					strSql = strSql + key + operate + value;
					break;
				case 7:
					// 包含;
					value = "'%" + originValue + "%'";
					strSql = strSql + key + " LIKE " + value;
					break;
				case 8:
					// 开始于…;
					value = "'" + originValue + "%'";
					strSql = strSql + key + " LIKE " + value;
					break;
				case 9:
					// 结束于…;
					value = "'%" + originValue + "'";
					strSql = strSql + key + " LIKE " + value;
					break;
				case 10:
					// 在...之内;
					originValue = originValue.replaceAll(" ", "");
					originValue = originValue.trim();
					String[] inArr = originValue.split(",");

					int inArrLen = inArr.length;
					if (inArrLen > 0) {
						value = "";
						if ("1".equals(type)) {
							for (int ii = 0; ii < inArrLen; ii++) {
								originValue = originValue + ",'" + inArr[ii] + "'";
							}

						} else {
							for (int ii = 0; ii < inArrLen; ii++) {
								originValue = originValue + "," + inArr[ii];
							}
						}
						originValue = value.substring(1);
						originValue = "(" + originValue + ")";
					}

					strSql = strSql + key + " IN " + originValue;
					break;
				case 11:
					// 为空;
					strSql = strSql + key + " IS NULL";
					break;
				case 12:
					// 不为空;
					strSql = strSql + key + " IS NOT NULL";
					break;

				default:
					strSql = strSql + key + "=" + value;
					break;
				}

			}

			List<Map<String, Object>> list = null;
			list = jdbcTemplate.queryForList(strSql);

			if (list != null) {
				for (int k = 0; k < list.size(); k++) {
					strContent = "";
					Map<String, Object> map = list.get(k);
					j = 0;
					for (Object vl : map.values()) {
						strContent = strContent + ",\"" + aryResult[j] + "\":\"" + TZUtility.transFormchar((String) vl)
								+ "\"";
						j++;
					}
					strContent = strContent.substring(1);
					strContent = "{" + strContent + "}";

					strRet = strRet + "," + strContent;
				}
			}
			strRet = strRet.substring(1);
			strRet = "{\"" + recname + "\":[" + strRet + "]}";

		} catch (Exception e) {
			e.printStackTrace();
			errMsgArr[0] = "1";
			errMsgArr[1] = e.toString();
		}
		return strRet;
	}

	/* 获取指定组件页面的访问授权信息 */
	@Override
	public String getComAuthorizedInfo(String sUserId, String sComID) {
		String sql = "SELECT C.TZ_COM_ID , C.TZ_PAGE_ID , if(SUM(if(C.DISPLAYONLY = 1 ,1 ,0)) =0 ,0,1) DISPLAYONLY, if(SUM(if(C.TZ_EDIT_FLG =1 ,1 ,0)) = 0 ,0 ,1) TZ_EDIT_FLG FROM PSROLEUSER A, PSROLECLASS B, PS_TZ_AQ_COMSQ_TBL C WHERE A.ROLEUSER=? AND A.DYNAMIC_SW='N' AND A.ROLENAME = B.ROLENAME AND B.CLASSID=C.CLASSID AND C.TZ_COM_ID=? GROUP BY C.TZ_COM_ID,C.TZ_PAGE_ID";
		String authJSON = "";
		try {
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { sUserId, sComID });
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {

					String tmpPageID = (String) list.get(i).get("TZ_PAGE_ID");
					long tmpNumDisplayOnly = (long) list.get(i).get("DISPLAYONLY");
					long tmpNumModifiable = (long) list.get(i).get("TZ_EDIT_FLG");
					String tmpModifiable = "";
					String tmpDisplayOnly = "";
					if (tmpNumModifiable >= 1) {
						tmpModifiable = "true";
						tmpDisplayOnly = "false";
					} else {
						if (tmpNumDisplayOnly >= 1) {
							tmpModifiable = "false";
							tmpDisplayOnly = "true";
						} else {
							tmpModifiable = "false";
							tmpDisplayOnly = "false";
						}
					}
					if ("".equals(authJSON)) {
						authJSON = "{\"pageID\":\"" + tmpPageID + "\",\"displayOnly\":" + tmpDisplayOnly
								+ ",\"modifiable\":" + tmpModifiable + "}";
					} else {
						authJSON = authJSON + ",{\"pageID\":\"" + tmpPageID + "\",\"displayOnly\":" + tmpDisplayOnly
								+ ",\"modifiable\":" + tmpModifiable + "}";
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ("".equals(authJSON)) {
			authJSON = "\"comID\":\"" + sComID + "\",\"pageAuthInfo\":[]";
		} else {
			authJSON = "\"comID\":\"" + sComID + "\",\"pageAuthInfo\":[" + authJSON + "]";
		}
		return authJSON;
	}
}
