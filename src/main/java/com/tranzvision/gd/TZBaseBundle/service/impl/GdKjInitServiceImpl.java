package com.tranzvision.gd.TZBaseBundle.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.GdKjInitService;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service
public class GdKjInitServiceImpl extends GdObjectServiceImpl implements GdKjInitService {
	@Autowired
	private SqlQuery jdbcTemplate;
	
	/* 保存上级菜单描述信息的变量 */
	private String m_ParentMenuDesc = "";
	/* 保存上级菜单大图标的变量 */
	private String m_ReturnMenuBigICON = "";
	/* 保存上级菜单小图标的变量 */
	private String m_ReturnMenuSmallICON = "";

	@Override
	public String getKJResources(HttpServletRequest request, HttpServletResponse response, String strThemeID) {
		// 返回json报文;
		String strRet = "";
		// 主题资源内报文;
		String strResContent = "";
		// 资源文件内容报文;
		String strFilesContent = "";
		// 资源集合编号;
		String strResSetID = "";
		// 文件类型、文件名称、文件路径;
		String strFileType = "", strFileName = "", strFilePath = "";
		// js文件列表;
		String strJSContent = "";
		// css文件列表;
		String strCssContent = "";
		int num = 1;
		int numJS = 0, numCss = 0;

		String sqlList = "SELECT TZ_ZYJH_ID FROM PS_TZ_PT_ZTZY_TBL WHERE TZ_ZT_ID=? UNION SELECT TZ_ZYJH_ID FROM PS_TZ_PT_ZYJH_TBL WHERE TZ_ZYJH_ISGG='Y'";
		List<Map<String, Object>> list = null;
		list = jdbcTemplate.queryForList(sqlList, new Object[] { strThemeID });
		
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				strResSetID = (String) map.get("TZ_ZYJH_ID");
				String sqlResList = "SELECT TZ_RES_FILE_TYPE,TZ_RES_FILE_NAME,TZ_RES_FILE_PATH FROM PS_TZ_PT_ZYXX_TBL WHERE TZ_ZYJH_ID=?";
				List<Map<String, Object>> list2 = null;

				list2 = jdbcTemplate.queryForList(sqlResList, new Object[] { strResSetID });
				if (list2 != null) {
					for (int j = 0; j < list2.size(); j++) {
						strFileType = (String) list2.get(j).get("TZ_RES_FILE_TYPE");
						strFileName = (String) list2.get(j).get("TZ_RES_FILE_NAME");
						strFilePath = (String) list2.get(j).get("TZ_RES_FILE_PATH");
						if (strFileType != null && "js".equals(strFileType.toLowerCase())) {
							numJS = numJS + 1;
							if (numJS == 1) {
								strJSContent = "{\"path\": \"" + strFilePath + "\",\"name\": [\"" + strFileName
										+ "\"] }";
							} else {
								strJSContent = strJSContent + ",{\"path\": \"" + strFilePath + "\",\"name\": [\""
										+ strFileName + "\"] }";
							}
						}

						if (strFileType != null && "css".equals(strFileType.toLowerCase())) {
							numCss = numCss + 1;
							if (numCss == 1) {
								strCssContent = "{\"path\": \"" + strFilePath + "\",\"name\": [\"" + strFileName
										+ "\"] }";
							} else {
								strCssContent = strCssContent + ",{\"path\": \"" + strFilePath + "\",\"name\": [\""
										+ strFileName + "\"] }";
							}
						}
					}
				}

				// 资源文件列表信息;
				strFilesContent = "\"js\": [" + strJSContent + "],\"css\": [" + strCssContent + "]";
				if (num == 1) {
					strResContent = "{\"index\": " + num + ",\"reid\": \"" + strResSetID
							+ "\",\"required\": true,\"content\": {" + strFilesContent + "}}";
				} else {
					strResContent = strResContent + ",{\"index\": " + num + ",\"reid\": \"" + strResSetID
							+ "\",\"required\": true,\"content\": {" + strFilesContent + "}}";
				}
				num = num + 1;
			}
		}

		// 登录人姓名;
		String strName = "";
		String relNameSQL = "select TZ_REALNAME from PS_TZ_AQ_YHXX_TBL where OPRID=?";
		String oprid = this.getOPRID(request);
		strName = jdbcTemplate.queryForObject(relNameSQL, new Object[] { oprid }, "String");
		if (strName == null) {
			strName = "";
		}

		strRet = "{" + this.getFrameworkLabelResources(request, response) + ",\"resources\":{\"" + strThemeID + "\":["
				+ strResContent + "]},\"curOrgID\":\"" + this.getLoginOrgID(request, response) + "\",\"firstName\":\""
				+ strName + "\",\"languageCode\":\"" + this.getLoginLanguage(request, response)
				+ "\",\"loginUserID\":\"" + this.getLoginAccount(request, response) + "\"}";

		return strRet;
	}

	/* 获取框架标签描述资源信息集合的方法 */
	private String getFrameworkLabelResources(HttpServletRequest request, HttpServletResponse response) {
		// String tmpMsgSetID = "";
		Map<String, Object> msgMap = new HashMap<String, Object>();
		String tmpMsgID = "", tmpMsgText = "", tmpJSONString = "";

		String languageId = this.getLoginLanguage(request, response);
		String jgId = this.getLoginOrgID(request, response);
		String SuperOrgId = this.getSuperOrgId(request, response);
		String xxjhId = "TZGD_FRMWRK_MSGSET";
		// 查询登录机构下的系统消息定义;
		List<Map<String, Object>> list = null;
		String loginMsgSQL = "select A.TZ_XXJH_ID, A.TZ_MSG_ID,ifnull(B.TZ_MSG_TEXT,A.TZ_MSG_TEXT) TZ_MSG_TEXT from PS_TZ_PT_XXDY_TBL A left join PS_TZ_PT_XXDY_TBL B on A.TZ_XXJH_ID = B.TZ_XXJH_ID and A.TZ_JG_ID=B.TZ_JG_ID and A.TZ_MSG_ID=B.TZ_MSG_ID where upper(B.TZ_LANGUAGE_ID)=upper(?) and upper(A.TZ_LANGUAGE_ID)=(SELECT UPPER(TZ_HARDCODE_VAL) TZ_LANGUAGE_CD FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZGD_BASIC_LANGUAGE' ) AND A.TZ_XXJH_ID=? AND  UPPER(A.TZ_JG_ID)=UPPER(?)";
		list = jdbcTemplate.queryForList(loginMsgSQL, new Object[] { languageId, xxjhId, jgId });
		

		Map<String, Object> map = null;
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				map = list.get(i);
				// tmpMsgSetID = (String) map.get("TZ_XXJH_ID");
				tmpMsgID = (String) map.get("TZ_MSG_ID");
				tmpMsgText = (String) map.get("TZ_MSG_TEXT");
				/*
				tmpJSONString = tmpJSONString + ",\"" + tmpMsgID + "\":\""
						+ tmpMsgText + "\"";
						*/
				msgMap.put(tmpMsgID, tmpMsgText);
			}
		}

		if (jgId != null && !jgId.equals(SuperOrgId)) {
			String superMsgSQL = "select A.TZ_XXJH_ID, A.TZ_MSG_ID,ifnull(B.TZ_MSG_TEXT,A.TZ_MSG_TEXT) TZ_MSG_TEXT from PS_TZ_PT_XXDY_TBL A left join PS_TZ_PT_XXDY_TBL B on A.TZ_XXJH_ID = B.TZ_XXJH_ID and A.TZ_JG_ID=B.TZ_JG_ID and A.TZ_MSG_ID=B.TZ_MSG_ID where upper(B.TZ_LANGUAGE_ID)=upper(?) and upper(A.TZ_LANGUAGE_ID)=(SELECT UPPER(TZ_HARDCODE_VAL) TZ_LANGUAGE_CD FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZGD_BASIC_LANGUAGE' ) AND A.TZ_XXJH_ID=? AND  UPPER(A.TZ_JG_ID)=UPPER(?) AND NOT exists(SELECT 'Y' FROM PS_TZ_PT_XXDY_TBL WHERE TZ_XXJH_ID=? AND UPPER(A.TZ_JG_ID)=UPPER(?))";
			list = jdbcTemplate.queryForList(superMsgSQL,
						new Object[] { languageId, xxjhId, SuperOrgId, xxjhId, jgId });
			

			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					map = list.get(i);
					// tmpMsgSetID = (String) map.get("TZ_XXJH_ID");
					tmpMsgID = (String) map.get("TZ_MSG_ID");
					tmpMsgText = (String) map.get("TZ_MSG_TEXT");
					/*
					tmpJSONString = tmpJSONString + ",\"" + tmpMsgID + "\":\""
							+ tmpMsgText + "\"";
							*/
					msgMap.put(tmpMsgID, tmpMsgText);
				}
			}
		}
/*
		if (tmpJSONString != null && !"".equals(tmpJSONString)) {
			tmpJSONString = tmpJSONString.substring(1);
		}

		tmpJSONString = "\"" + this.getLoginLanguage(request, response) + "\":{\"languagePackage\":{" + tmpJSONString
				+ "}}";
				*/
		JacksonUtil jacksonUtil = new JacksonUtil();
		tmpJSONString = "\"" + this.getLoginLanguage(request, response) + "\":{\"languagePackage\":" + jacksonUtil.Map2json(msgMap)
				+ "}";
		return tmpJSONString;
	}

	@Override
	public String getMenuList(HttpServletRequest request, HttpServletResponse response) {
		// 返回json报文;
		String strRet = "";
		// 机构编号;
		String strOrgID = "";
		// 主菜单内容;
		String strContent = "";

		// loginlanguage;
		String loginLanguage = this.getLoginLanguage(request, response);
		// 当前机构编号;
		strOrgID = this.getLoginOrgID(request, response);

		// 获取上级菜单描述信息;
		String tmpParentMenuDesc = "";
		String sql = "SELECT TZ_MSG_TEXT FROM PS_TZ_PT_XXDY_TBL WHERE TZ_XXJH_ID=? AND TZ_MSG_ID=? AND TZ_LANGUAGE_ID=?";
		tmpParentMenuDesc = jdbcTemplate.queryForObject(sql,
				new Object[] { "TZGD_FRMWRK_MSGSET", "tz-frmwrk-lang-00012", loginLanguage }, "String");

		if (tmpParentMenuDesc == null || "".equals(tmpParentMenuDesc)) {
			if ("ZHS".equals(loginLanguage)) {
				this.m_ParentMenuDesc = "返回上级菜单";
			} else {
				this.m_ParentMenuDesc = "Return to the upper menu";
			}
		} else {
			this.m_ParentMenuDesc = tmpParentMenuDesc;
		}

		// 获取上级菜单大图标ID;
		String tmpReturnMenuBigICON = this.getHardCodeValue("TZGD_RETURN_MENU_BICON");
		this.m_ReturnMenuBigICON = tmpReturnMenuBigICON;

		// 获取上级菜单小图标ID;
		String tmpReturnMenuSmallICON = this.getHardCodeValue("TZGD_RETURN_MENU_SICON");
		this.m_ReturnMenuSmallICON = tmpReturnMenuSmallICON;

		strContent = this.getSubMenuList(request, response, strOrgID);
		// 合并生成主菜单JSON报文;
		strRet = "{\"" + loginLanguage + "\":{\"mainMenu\":[" + strContent + "]}}";

		return strRet;
	}

	@Override
	public String getSubMenuList(HttpServletRequest request, HttpServletResponse response, String strMenuID) {
		// 返回json报文;
		String strRet = "";
		// 登录人ID;
		String strUserID = "";
		// 菜单树编号;
		String strTreeName = "";
		// 下级菜单编号，下级菜单名称，组件ID，下级菜单大图标，下级菜单小图标，下级菜单说明信息内容编号;
		String strSubMenuID, strSubMenuName, strComID, strSubMenuLImg, strSubMenuSImg, strSubMenuContID;
		int num = 1;

		strUserID = this.getOPRID(request);
		strTreeName = "TZ_GD_GNCD_MENU";
		String languageId = this.getLoginLanguage(request, response);
		strUserID = this.getOPRID(request);
		strTreeName = "TZ_GD_GNCD_MENU";
		// 获取下级菜单;
		String subSQL = "SELECT E1.TZ_MENU_NUM , CASE WHEN  E1.TZ_MENU_MC_M IS NULL THEN E1.TZ_MENU_MC WHEN E1.TZ_MENU_MC_M=' ' THEN E1.TZ_MENU_MC ELSE E1.TZ_MENU_MC_M END TZ_MENU_MC,E1.TZ_COM_ID ,E1.TZ_MENU_LIMG ,E1.TZ_MENU_SIMG ,E1.TZ_MENU_NRID FROM (SELECT E.TREE_NAME,E.TZ_MENU_NUM, E.TZ_MENU_MC TZ_MENU_MC,M.TZ_MENU_MC TZ_MENU_MC_M,E.TZ_COM_ID ,E.TZ_MENU_LIMG ,E.TZ_MENU_SIMG ,E.TZ_MENU_NRID FROM PS_TZ_AQ_CDJD_TBL E left join PS_TZ_AQ_CDJD_LNG M on E.TREE_NAME=M.TREE_NAME AND E.TZ_MENU_NUM=M.TZ_MENU_NUM  AND M.TZ_LANGUAGE_ID=? where  E.TZ_YXX='Y' AND E.TREE_NAME = ?) E1, PSTREENODE F WHERE E1.TREE_NAME=F.TREE_NAME AND E1.TZ_MENU_NUM=F.TREE_NODE AND F.PARENT_NODE_NUM >= 1 AND EXISTS(SELECT 'X' FROM PSTREENODE A WHERE A.TREE_NAME=? AND A.PARENT_NODE_NAME=? AND E1.TZ_MENU_NUM=A.TREE_NODE AND EXISTS(SELECT 'X' FROM PSROLEUSER S1 ,PSROLECLASS S2 ,PS_TZ_AQ_COMSQ_TBL S3 ,PS_TZ_AQ_CDJD_TBL S4 ,PSTREENODE S5 WHERE S1.ROLEUSER = ? AND S1.DYNAMIC_SW='N' AND S1.ROLENAME=S2.ROLENAME AND S2.CLASSID=S3.CLASSID AND S4.TZ_COM_ID=S3.TZ_COM_ID AND (S3.DISPLAYONLY+S3.TZ_EDIT_FLG) >= 1 AND S3.DISPLAYONLY >= 0 AND S3.TZ_EDIT_FLG >= 0 AND S4.TREE_NAME = ? AND S5.TREE_NAME=S4.TREE_NAME AND S5.TREE_NODE=S4.TZ_MENU_NUM AND A.TREE_NODE_NUM <= S5.TREE_NODE_NUM AND A.TREE_NODE_NUM_END >= S5.TREE_NODE_NUM AND NOT EXISTS( SELECT 'X' FROM PSTREENODE S6 ,PS_TZ_AQ_CDJD_TBL S7 WHERE S6.TREE_NAME=S5.TREE_NAME AND S6.TREE_NODE_NUM<=S5.TREE_NODE_NUM AND S6.TREE_NODE_NUM_END>=S5.TREE_NODE_NUM AND S7.TREE_NAME=S6.TREE_NAME AND S7.TZ_MENU_NUM=S6.TREE_NODE AND S7.TZ_YXX<>'Y' ))) ORDER BY F.TREE_NODE_NUM ASC";
		List<Map<String, Object>> list = null;
		list = jdbcTemplate.queryForList(subSQL,
					new Object[] { languageId, strTreeName, strTreeName, strMenuID, strUserID, strTreeName });

		

		// 结果;
		String jsClassName = "";
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				strSubMenuID = (String) list.get(i).get("TZ_MENU_NUM");
				strSubMenuName = (String) list.get(i).get("TZ_MENU_MC");
				strComID = (String) list.get(i).get("TZ_COM_ID");
				strSubMenuLImg = (String) list.get(i).get("TZ_MENU_LIMG");
				strSubMenuSImg = (String) list.get(i).get("TZ_MENU_SIMG");
				strSubMenuContID = (String) list.get(i).get("TZ_MENU_NRID");

				// 判断该菜单是否是叶子菜单;
				String isleaf = "Y";
				int subMenuNum = 0;
				String isLeafSQL = "SELECT COUNT(1) FROM PSTREENODE WHERE TREE_NAME = ? AND PARENT_NODE_NAME=?";
				subMenuNum = jdbcTemplate.queryForObject(isLeafSQL, new Object[] { strTreeName, strSubMenuID },
						"Integer");

				if (subMenuNum > 0) {
					isleaf = "N";
				} else {
					isleaf = "Y";
				}
				if ("Y".equals(isleaf)) {
					jsClassName = "";
					String pageKjdjsSQL = "SELECT TZ_PAGE_KHDJS FROM PS_TZ_AQ_PAGZC_TBL WHERE TZ_COM_ID=? AND TZ_PAGE_MRSY='Y' ORDER BY TZ_PAGE_XH ASC";
					jsClassName = jdbcTemplate.queryForObject(pageKjdjsSQL, new Object[] { strComID }, "String");

					if (jsClassName == null) {
						jsClassName = "";
					}
					if (num == 1) {
						strRet = "{\"id\": \"" + strSubMenuID + "\",\"text\": \"" + strSubMenuName
								+ "\",\"descriptionID\": \"" + strSubMenuContID + "\",\"comID\":\"" + strComID
								+ "\",\"leaf\": true,\"className\":\"" + jsClassName + "\",\"largeIcon\":\""
								+ strSubMenuLImg + "\",\"smallIcon\":\"" + strSubMenuSImg + "\",\"returnMenu\": false}";
					} else {
						strRet = strRet + ",{\"id\": \"" + strSubMenuID + "\",\"text\": \"" + strSubMenuName
								+ "\",\"descriptionID\": \"" + strSubMenuContID + "\",\"comID\":\"" + strComID
								+ "\",\"leaf\": true,\"className\":\"" + jsClassName + "\",\"largeIcon\":\""
								+ strSubMenuLImg + "\",\"smallIcon\":\"" + strSubMenuSImg + "\",\"returnMenu\": false}";
					}
				} else {
					if (num == 1) {
						strRet = "{\"id\": \"" + strSubMenuID + "\",\"expanded\": true,\"text\": \"" + strSubMenuName
								+ "\",\"leaf\": false,\"descriptionID\": \"" + strSubMenuContID + "\",\"comID\":\""
								+ strComID + "\",\"children\": [" + this.getSubMenuList(request, response, strSubMenuID)
								+ "],\"largeIcon\":\"" + strSubMenuLImg + "\",\"smallIcon\":\"" + strSubMenuSImg
								+ "\",\"returnMenu\": false}";
					} else {
						strRet = strRet + ",{\"id\": \"" + strSubMenuID + "\",\"expanded\": true,\"text\": \""
								+ strSubMenuName + "\",\"leaf\": false,\"descriptionID\": \"" + strSubMenuContID
								+ "\",\"comID\":\"" + strComID + "\",\"children\": ["
								+ this.getSubMenuList(request, response, strSubMenuID) + "],\"largeIcon\":\""
								+ strSubMenuLImg + "\",\"smallIcon\":\"" + strSubMenuSImg + "\",\"returnMenu\": false}";
					}
				}
				num = num + 1;
			}
		}

		/* 添加返回上级菜单节点 */
		String loginOrgId = this.getLoginOrgID(request, response);
		if ((strRet != null && !"".equals(strRet)) && (loginOrgId != null && !loginOrgId.equals(strMenuID))) {
			String tmpReturnMenuId = strMenuID + "_RTUM";
			String tmpReturnMenuMS = m_ParentMenuDesc;
			String tmpReturnMenuNode = "{\"id\": \"" + tmpReturnMenuId + "\",\"text\": \"" + tmpReturnMenuMS
					+ "\",\"descriptionID\": \"\",\"comID\":\"\",\"leaf\": true,\"className\":\"\",\"largeIcon\":\""
					+ m_ReturnMenuBigICON + "\",\"smallIcon\":\"" + m_ReturnMenuSmallICON + "\",\"returnMenu\":true}";
			strRet = tmpReturnMenuNode + "," + strRet;
		}
		return strRet;
	}

	@Override
	/* 获取框架操作列表信息 */
	public String getKJVersionInfo() {
		// 返回json报文;
		String strRet = "";
		// 主题内容;
		String strThemeCont = "";
		// 语言内容;
		String strLangCont = "";
		// 主题编号，主题名称;
		String strThemeID = "", strThemeName = "";

		// 语言转换值集合编号;
		String strLangSetID = "";
		int num = 0;

		// 语言转换值集合编号;
		strLangSetID = this.getHardCodeValue("TZ_GD_LANGUAGE");

		try {
			// 获取有效状态的主题 ;
			String sqlThemeList = "SELECT TZ_ZT_ID,TZ_ZT_MC FROM PS_TZ_PT_ZTXX_TBL WHERE TZ_YXX='Y'";
			List<Map<String, Object>> list = null;
			list = jdbcTemplate.queryForList(sqlThemeList);
			

			if (list != null) {
				for (num = 0; num < list.size(); num++) {
					strThemeID = (String) list.get(num).get("TZ_ZT_ID");
					strThemeName = (String) list.get(num).get("TZ_ZT_MC");
					if (num == 0) {
						strThemeCont = "{\"themeid\":\"" + strThemeID + "\", \"themeName\":\"" + strThemeName + "\"}";
					} else {
						strThemeCont = strThemeCont + ",{\"themeid\":\"" + strThemeID + "\", \"themeName\":\""
								+ strThemeName + "\"}";
					}
				}
			}

			// 获取有效的语言信息列表;
			num = 0;
			list = null;
			String sqlLangList =  "SELECT A.TZ_ZHZ_ID,A.TZ_ZHZ_CMS FROM PS_TZ_PT_ZHZXX_TBL A,PS_TZ_PT_ZHZJH_TBL B WHERE A.TZ_ZHZJH_ID=B.TZ_ZHZJH_ID AND A.TZ_EFF_STATUS='A' AND B.TZ_ZHZJH_ID=? AND curdate()>=A.TZ_EFF_DATE";
			list = jdbcTemplate.queryForList(sqlLangList, new Object[]{strLangSetID});
			

			if (list != null) {
				for (num = 0; num < list.size(); num++) {
					// 语言编号，语言名称;
					String strLangID = (String) list.get(num).get("TZ_ZHZ_ID");
					String strLangName = (String) list.get(num).get("TZ_ZHZ_CMS");
					if (num == 0) {
						strLangCont = "{\"languageid\":\"" + strLangID + "\", \"languageName\":\"" + strLangName
								+ "\"}";
					} else {
						strLangCont = strLangCont + ",{\"languageid\":\"" + strLangID + "\", \"languageName\":\""
								+ strLangName + "\"}";
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return strRet;
		}
		strRet = "{\"themes\":[" + strThemeCont + "],\"languages\":[" + strLangCont + "]}";
		return strRet;
	}

	/* 获取菜单说明信息 */
	@Override
	public String getMenuDescription(HttpServletRequest request, HttpServletResponse response, String strDescID) {
		// 返回json报文;
		String strRet;
		// 说明信息内容;
		String strDescContent = "";

		// 菜单说明信息内容;
		strRet = "{\"" + this.getLoginLanguage(request, response) + "\":{\"descriptionID\":\"" + strDescID
				+ "\",\"description\":\"" + strDescContent + "\"}}";

		return strRet;
	}
}
