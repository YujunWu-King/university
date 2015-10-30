package com.tranzvision.gd.TZComRegMgBundle.service.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.PaseJsonUtil;
import com.tranzvision.gd.util.base.TZUtility;

import net.sf.json.JSONObject;

/**
 * @author tang
 * @version 1.0, 2015-10-9
 * @功能：功能页面注册相关类 原ps类：TZ_GD_COMREGMG_PKG:TZ_GD_PAGEREG_CLS
 */
@Service("com.tranzvision.gd.TZComRegMgBundle.service.impl.PageRegImpl")
public class PageRegImpl extends FrameworkImpl {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/* 新增组件页面注册信息 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		try {
			// 表单内容;
			String strForm = actData[0];
			// 将字符串转换成json;
			JSONObject CLASSJson = PaseJsonUtil.getJson(strForm);
			// 组件编号;
			String strComID = CLASSJson.getString("comID");
			// 页面编号;
			String strPageID = CLASSJson.getString("pageID");
			// 页面名称;
			String strPageName = CLASSJson.getString("pageName");

			// 序号;
			String strOrder = CLASSJson.getString("orderNum");
			if (strOrder == null || "".equals(strOrder)) {
				strOrder = "0";
			}
			// 序号;
			int numOrder = 0;
			if (StringUtils.isNumeric(strOrder)) {
				numOrder = Integer.parseInt(strOrder);
			}

			// 是否外部链接;
			String isExURL = CLASSJson.getString("isExURL");
			// 外部URL;
			String exURL = CLASSJson.getString("exURL");
			// 客户端页面处理JS类;
			String jsClass = CLASSJson.getString("jsClass");
			// 服务器端处理类;
			String appClass = CLASSJson.getString("appClass");
			// 默认首页;
			String isDefault = CLASSJson.getString("isDefault");
			// 新开窗口;
			String isNewWin = CLASSJson.getString("isNewWin");
			// 引用编号;
			String refCode = CLASSJson.getString("refCode");

			// 查找当前组件下是否已经存在该页面;
			String isExistSql = "SELECT count(1) FROM PS_TZ_AQ_PAGZC_TBL WHERE TZ_COM_ID=? AND TZ_PAGE_ID=?";
			try {
				int count = jdbcTemplate.queryForObject(isExistSql, new Object[] { strComID, strPageID },
						Integer.class);
				
				// 新增页面注册信息;
				/*
				 * System.out.println(strComID + "-->" + strPageID + "-->" +
				 * strPageName + "-->" + strOrder + "-->" + isExURL + "-->" +
				 * exURL + "-->" + jsClass + "-->" + appClass + "-->" +
				 * isDefault + "-->" + isNewWin + "-->" + refCode + "-->");
				 */
				if (count == 0) {
					String insertPageInfoSql = "INSERT INTO PS_TZ_AQ_PAGZC_TBL (TZ_COM_ID,TZ_PAGE_ID, TZ_PAGE_MC, TZ_PAGE_XH,TZ_PAGE_ISWBURL,TZ_PAGE_WBURL,TZ_PAGE_KHDJS,TZ_PAGE_FWDCLS,TZ_PAGE_MRSY,TZ_PAGE_NEWWIN,TZ_PAGE_REFCODE,ROW_ADDED_DTTM,ROW_ADDED_OPRID,ROW_LASTMANT_DTTM,ROW_LASTMANT_OPRID) VALUES(?,?,?,?,?,?,?,?,?,?,?,current_timestamp(),?,current_timestamp(),?)";
					/****** TODO, 应该是当前登录人员 %userid ****/
					int excuteNum = jdbcTemplate.update(insertPageInfoSql, strComID, strPageID, strPageName, numOrder,
							isExURL, exURL, jsClass, appClass, isDefault, isNewWin, refCode, "TZ_7", "TZ_7");
					if (excuteNum == 0) {
						/*** 添加失败 ****/
						errMsg[0] = "1";
						errMsg[1] = "添加失败";
					}
				} else {
					errMsg[0] = "1";
					errMsg[1] = "当前组件下，页面ID为：" + strPageID + "的信息已经注册,请修改页面ID。";
					return strRet;
				}
			} catch (Exception e) {
				e.printStackTrace();
				errMsg[0] = "1";
				errMsg[1] = "当前组件下，页面ID为：" + strPageID + "的信息已经注册,请修改页面ID。";
				return strRet;
			}

		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}

	/* 更新页面注册信息 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		// 表单内容;
		String strForm = actData[0];
		// 修改页面注册信息;
		strRet = this.tzEditPageInfo(strForm, errMsg);

		return strRet;
	}

	/* 获取组件页面注册信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		try {
			JSONObject CLASSJson = PaseJsonUtil.getJson(strParams);

			String strComID = CLASSJson.getString("comID");
			String strPageID = CLASSJson.getString("pageID");
			if (strComID != null && !"".equals(strComID) && strPageID != null && !"".equals(strPageID)) {

				// 页面名称，是否外部链接，外部URL，客户端JS类，服务器端AppClass，是否默认首页，是否新开窗口，引用编号;
				String strPageName = "", isExUrl = "", exUrl = "", JSClass = "", appClass = "", isDefault = "",
						isNewWin = "", refCode = "";
				// 序号;
				int numOrder = 0;
				// 获取页面注册信息;
				String sql = "SELECT TZ_PAGE_MC,TZ_PAGE_XH,TZ_PAGE_ISWBURL,TZ_PAGE_WBURL,TZ_PAGE_KHDJS,TZ_PAGE_FWDCLS,TZ_PAGE_MRSY,TZ_PAGE_NEWWIN,TZ_PAGE_REFCODE FROM PS_TZ_AQ_PAGZC_TBL WHERE TZ_COM_ID=? AND TZ_PAGE_ID=?";
				try {
					Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { strComID, strPageID });

					strPageName = TZUtility.transFormchar((String) map.get("TZ_PAGE_MC"));
					numOrder = (int) map.get("TZ_PAGE_XH");
					isExUrl = (String) map.get("TZ_PAGE_ISWBURL");
					exUrl = TZUtility.transFormchar((String) map.get("TZ_PAGE_WBURL"));
					JSClass = TZUtility.transFormchar((String) map.get("TZ_PAGE_KHDJS"));
					appClass = TZUtility.transFormchar((String) map.get("TZ_PAGE_FWDCLS"));
					isDefault = (String) map.get("TZ_PAGE_MRSY");
					isNewWin = (String) map.get("TZ_PAGE_NEWWIN");
					refCode = TZUtility.transFormchar((String) map.get("TZ_PAGE_REFCODE"));

					// 组件注册信息;
					strRet = "{\"comID\":\"" + strComID + "\",\"pageID\":\"" + strPageID + "\",\"pageName\":\""
							+ strPageName + "\",\"orderNum\":" + numOrder + ",\"isExURL\":\"" + isExUrl
							+ "\",\"exURL\":\"" + exUrl + "\",\"jsClass\":\"" + JSClass + "\",\"appClass\":\""
							+ appClass + "\",\"isDefault\":\"" + isDefault + "\",\"isNewWin\":\"" + isNewWin
							+ "\",\"refCode\":\"" + refCode + "\"}";
				} catch (Exception e) {
					errMsg[0] = "1";
					errMsg[1] = "无法获取页面信息";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "无法获取页面信息";
			}

		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	private String tzEditPageInfo(String fromData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		try {

			JSONObject CLASSJson = PaseJsonUtil.getJson(fromData);
			// 组件编号;
			String strComID = CLASSJson.getString("comID");
			// 页面编号;
			String strPageID = CLASSJson.getString("pageID");
			// 页面名称;
			String strPageName = CLASSJson.getString("pageName");
			
			// 序号;
			String strOrder = CLASSJson.getString("orderNum");
			// 序号;
			if (strOrder == null || "".equals(strOrder)) {
				strOrder = "0";
			}
			// 序号;
			int numOrder = 0;
			if (StringUtils.isNumeric(strOrder)) {
				numOrder = Integer.parseInt(strOrder);
			}

			// 是否外部链接;
			String isExURL = CLASSJson.getString("isExURL");
			// 外部URL;
			String exURL = CLASSJson.getString("exURL");
			// 客户端页面处理JS类;
			String jsClass = CLASSJson.getString("jsClass");
			// 服务器端处理类;
			String appClass = CLASSJson.getString("appClass");
			// 默认首页;
			String isDefault = CLASSJson.getString("isDefault");
			// 新开窗口;
			String isNewWin = CLASSJson.getString("isNewWin");
			// 引用编号;
			String refCode = CLASSJson.getString("refCode");
			/**
			 * System.out.println(strComID + "-->" + strPageID + "-->" +
			 * strPageName + "-->" + strOrder + "-->" + isExURL + "-->" + exURL
			 * + "-->" + jsClass + "-->" + appClass + "-->" + isDefault + "-->"
			 * + isNewWin + "-->" + refCode + "-->");
			 **/

			/* 引用编号存在则必须唯一 */
			String refCodeExist = "";
			if (refCode != null && !"".equals(refCode.trim())) {
				String refCodeUnqSQL = "SELECT 'Y' FROM PS_TZ_AQ_PAGZC_TBL WHERE (TZ_COM_ID<>? OR TZ_PAGE_ID<>?) AND TZ_PAGE_REFCODE=?";
				try {
					refCodeExist = jdbcTemplate.queryForObject(refCodeUnqSQL,
							new Object[] { strComID, strPageID, refCode }, String.class);
				} catch (DataAccessException e) {

				}
			}

			if ("Y".equals(refCodeExist)) {
				errMsg[0] = "1";
				errMsg[1] = "引用编号出现重复!";
			} else {
				// 默认首页;
				if ("Y".equals(isDefault)) {
					// 默认首页只能有一个;
					String updateDefalutNoSQL = "UPDATE PS_TZ_AQ_PAGZC_TBL SET TZ_PAGE_MRSY='N' WHERE TZ_COM_ID=?";
					try {
						jdbcTemplate.update(updateDefalutNoSQL, strComID);
					} catch (DataAccessException e) {

					}
				}

				// 是否存在;
				String isExist = "";
				String isExistSQL = "SELECT 'Y' FROM PS_TZ_AQ_PAGZC_TBL WHERE TZ_COM_ID=? AND TZ_PAGE_ID=?";
				try {
					isExist = jdbcTemplate.queryForObject(isExistSQL, new Object[] { strComID,strPageID }, String.class);
				} catch (DataAccessException e) {

				}
				if ("Y".equals(isExist)) {
					/*** 更新 ****/
					String updateSql = "UPDATE PS_TZ_AQ_PAGZC_TBL set TZ_PAGE_MC=?, TZ_PAGE_XH=?,TZ_PAGE_ISWBURL=?,TZ_PAGE_WBURL=?,TZ_PAGE_KHDJS=?,TZ_PAGE_FWDCLS=?,TZ_PAGE_MRSY=?,TZ_PAGE_NEWWIN=?,TZ_PAGE_REFCODE=?,ROW_LASTMANT_DTTM=current_timestamp(),ROW_LASTMANT_OPRID=? WHERE TZ_COM_ID=? AND TZ_PAGE_ID=?";
					try {
						/****** TODO, 应该是当前登录人员 %userid ****/
						int i = jdbcTemplate.update(updateSql, strPageName, numOrder, isExURL, exURL, jsClass, appClass,
								isDefault, isNewWin, refCode, "TZ_7", strComID, strPageID);
						if (i == 0) {
							/*** 更新失败 ****/
							errMsg[0] = "1";
							errMsg[1] = "更新失败";
						}
					} catch (DataAccessException e) {
						e.printStackTrace();
						/*** 更新失败 ****/
						errMsg[0] = "1";
						errMsg[1] = "更新失败";
					}

					
				} else {
					/*** 更新的页面不存在 ****/
					errMsg[0] = "1";
					errMsg[1] = "更新的页面不存在";
				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = "更新的页面不存在";
		}
		return strRet;
	}
}
