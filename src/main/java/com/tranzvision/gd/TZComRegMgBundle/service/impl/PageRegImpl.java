package com.tranzvision.gd.TZComRegMgBundle.service.impl;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZComRegMgBundle.dao.PsTzAqPagzcTblMapper;
import com.tranzvision.gd.TZComRegMgBundle.model.PsTzAqPagzcTbl;
import com.tranzvision.gd.TZComRegMgBundle.model.PsTzAqPagzcTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TZUtility;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * @author tang
 * @version 1.0, 2015-10-9
 * @功能：功能页面注册相关类 原ps类：TZ_GD_COMREGMG_PKG:TZ_GD_PAGEREG_CLS
 */
@Service("com.tranzvision.gd.TZComRegMgBundle.service.impl.PageRegImpl")
public class PageRegImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private JacksonUtil jacksonUtil;
	@Autowired
	private PsTzAqPagzcTblMapper psTzAqPagzcTblMapper;

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
			jacksonUtil.json2Map(strForm);
			// 组件编号;
			String strComID = jacksonUtil.getString("comID");
			// 页面编号;
			String strPageID = jacksonUtil.getString("pageID");
			// 页面名称;
			String strPageName = jacksonUtil.getString("pageName");

			// 序号;
			String strOrder = jacksonUtil.getString("orderNum");
			if (strOrder == null || "".equals(strOrder)) {
				strOrder = "0";
			}
			// 序号;
			short numOrder = 0;
			if (!"".equals(strOrder) && StringUtils.isNumeric(strOrder)) {
				numOrder = Short.parseShort(strOrder);
			}

			// 是否外部链接;
			String isExURL = jacksonUtil.getString("isExURL");
			// 外部URL;
			String exURL = jacksonUtil.getString("exURL");
			// 客户端页面处理JS类;
			String jsClass = jacksonUtil.getString("jsClass");
			// 服务器端处理类;
			String appClass = jacksonUtil.getString("appClass");
			// 默认首页;
			String isDefault = jacksonUtil.getString("isDefault");
			// 新开窗口;
			String isNewWin = jacksonUtil.getString("isNewWin");
			// 引用编号;
			String refCode = jacksonUtil.getString("refCode");

			// 查找当前组件下是否已经存在该页面;
			String isExistSql = "SELECT count(1) FROM PS_TZ_AQ_PAGZC_TBL WHERE TZ_COM_ID=? AND TZ_PAGE_ID=?";

			int count = jdbcTemplate.queryForObject(isExistSql, new Object[] { strComID, strPageID }, "Integer");

			if (count == 0) {

				/* 引用编号存在则必须唯一 */
				String refCodeExist = "";
				if (refCode != null && !"".equals(refCode.trim())) {
					String refCodeUnqSQL = "SELECT 'Y' FROM PS_TZ_AQ_PAGZC_TBL WHERE (TZ_COM_ID<>? OR TZ_PAGE_ID<>?) AND TZ_PAGE_REFCODE=?";

					refCodeExist = jdbcTemplate.queryForObject(refCodeUnqSQL,
							new Object[] { strComID, strPageID, refCode }, "String");

				}

				if ("Y".equals(refCodeExist)) {
					errMsg[0] = "1";
					errMsg[1] = "引用编号出现重复!";
				} else {
					// 默认首页;
					if ("Y".equals(isDefault)) {
						// 默认首页只能有一个;
						String updateDefalutNoSQL = "UPDATE PS_TZ_AQ_PAGZC_TBL SET TZ_PAGE_MRSY='N' WHERE TZ_COM_ID=?";
						jdbcTemplate.update(updateDefalutNoSQL, new Object[] { strComID });
					}

					PsTzAqPagzcTbl psTzAqPagzcTbl = new PsTzAqPagzcTbl();
					psTzAqPagzcTbl.setTzComId(strComID);
					psTzAqPagzcTbl.setTzPageId(strPageID);
					psTzAqPagzcTbl.setTzPageMc(strPageName);
					psTzAqPagzcTbl.setTzPageXh(numOrder);
					psTzAqPagzcTbl.setTzPageIswburl(isExURL);
					psTzAqPagzcTbl.setTzPageWburl(exURL);
					psTzAqPagzcTbl.setTzPageKhdjs(jsClass);
					psTzAqPagzcTbl.setTzPageFwdcls(appClass);
					psTzAqPagzcTbl.setTzPageMrsy(isDefault);
					psTzAqPagzcTbl.setTzPageNewwin(isNewWin);
					psTzAqPagzcTbl.setTzPageRefcode(refCode);
					/****** TODO, 应该是当前登录人员 %userid ****/
					psTzAqPagzcTbl.setRowAddedDttm(new Date());
					psTzAqPagzcTbl.setRowAddedOprid("TZ_7");
					psTzAqPagzcTbl.setRowLastmantDttm(new Date());
					psTzAqPagzcTbl.setRowLastmantOprid("TZ_7");
					psTzAqPagzcTblMapper.insert(psTzAqPagzcTbl);

				}

			} else {
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
		String strRet = "{}";
		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		try {
			// 表单内容;
			String strForm = actData[0];
			// 将字符串转换成json;
			jacksonUtil.json2Map(strForm);
			// 组件编号;
			String strComID = jacksonUtil.getString("comID");
			// 页面编号;
			String strPageID = jacksonUtil.getString("pageID");
			// 页面名称;
			String strPageName = jacksonUtil.getString("pageName");

			// 序号;
			String strOrder = jacksonUtil.getString("orderNum");
			if (strOrder == null || "".equals(strOrder)) {
				strOrder = "0";
			}
			// 序号;
			short numOrder = 0;
			if (!"".equals(strOrder) && StringUtils.isNumeric(strOrder)) {
				numOrder = Short.parseShort(strOrder);
			}

			// 是否外部链接;
			String isExURL = jacksonUtil.getString("isExURL");
			// 外部URL;
			String exURL = jacksonUtil.getString("exURL");
			// 客户端页面处理JS类;
			String jsClass = jacksonUtil.getString("jsClass");
			// 服务器端处理类;
			String appClass = jacksonUtil.getString("appClass");
			// 默认首页;
			String isDefault = jacksonUtil.getString("isDefault");
			// 新开窗口;
			String isNewWin = jacksonUtil.getString("isNewWin");
			// 引用编号;
			String refCode = jacksonUtil.getString("refCode");

			// 查找当前组件下是否已经存在该页面;
			String isExistSql = "SELECT count(1) FROM PS_TZ_AQ_PAGZC_TBL WHERE TZ_COM_ID=? AND TZ_PAGE_ID=?";

			int count = jdbcTemplate.queryForObject(isExistSql, new Object[] { strComID, strPageID }, "Integer");

			if (count > 0) {

				/* 引用编号存在则必须唯一 */
				String refCodeExist = "";
				if (refCode != null && !"".equals(refCode.trim())) {
					String refCodeUnqSQL = "SELECT 'Y' FROM PS_TZ_AQ_PAGZC_TBL WHERE (TZ_COM_ID<>? OR TZ_PAGE_ID<>?) AND TZ_PAGE_REFCODE=?";

					refCodeExist = jdbcTemplate.queryForObject(refCodeUnqSQL,
							new Object[] { strComID, strPageID, refCode }, "String");

				}

				if ("Y".equals(refCodeExist)) {
					errMsg[0] = "1";
					errMsg[1] = "引用编号出现重复!";
				} else {
					// 默认首页;
					if ("Y".equals(isDefault)) {
						// 默认首页只能有一个;
						String updateDefalutNoSQL = "UPDATE PS_TZ_AQ_PAGZC_TBL SET TZ_PAGE_MRSY='N' WHERE TZ_COM_ID=?";
						jdbcTemplate.update(updateDefalutNoSQL, new Object[] { strComID });
					}

					PsTzAqPagzcTbl psTzAqPagzcTbl = new PsTzAqPagzcTbl();
					psTzAqPagzcTbl.setTzComId(strComID);
					psTzAqPagzcTbl.setTzPageId(strPageID);
					psTzAqPagzcTbl.setTzPageMc(strPageName);
					psTzAqPagzcTbl.setTzPageXh(numOrder);
					psTzAqPagzcTbl.setTzPageIswburl(isExURL);
					psTzAqPagzcTbl.setTzPageWburl(exURL);
					psTzAqPagzcTbl.setTzPageKhdjs(jsClass);
					psTzAqPagzcTbl.setTzPageFwdcls(appClass);
					psTzAqPagzcTbl.setTzPageMrsy(isDefault);
					psTzAqPagzcTbl.setTzPageNewwin(isNewWin);
					psTzAqPagzcTbl.setTzPageRefcode(refCode);
					/****** TODO, 应该是当前登录人员 %userid ****/
					psTzAqPagzcTbl.setRowLastmantDttm(new Date());
					psTzAqPagzcTbl.setRowLastmantOprid("TZ_7");
					psTzAqPagzcTblMapper.updateByPrimaryKeySelective(psTzAqPagzcTbl);

				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "当前组件下，页面ID为：" + strPageID + "的信息不存在";
				return strRet;
			}

		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}

	/* 获取组件页面注册信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		try {
			jacksonUtil.json2Map(strParams);

			String strComID = jacksonUtil.getString("comID");
			String strPageID = jacksonUtil.getString("pageID");
			if (strComID != null && !"".equals(strComID) && strPageID != null && !"".equals(strPageID)) {

				// 页面名称，是否外部链接，外部URL，客户端JS类，服务器端AppClass，是否默认首页，是否新开窗口，引用编号;
				String strPageName = "", isExUrl = "", exUrl = "", JSClass = "", appClass = "", isDefault = "",
						isNewWin = "", refCode = "";
				// 序号;
				short numOrder = 0;

				PsTzAqPagzcTblKey psTzAqPagzcTblKey = new PsTzAqPagzcTblKey();
				psTzAqPagzcTblKey.setTzComId(strComID);
				psTzAqPagzcTblKey.setTzPageId(strPageID);
				// 获取页面注册信息;
				PsTzAqPagzcTbl psTzAqPagzcTbl = psTzAqPagzcTblMapper.selectByPrimaryKey(psTzAqPagzcTblKey);
				strPageName = TZUtility.transFormchar(psTzAqPagzcTbl.getTzPageMc());
				numOrder = psTzAqPagzcTbl.getTzPageXh();
				isExUrl = TZUtility.transFormchar(psTzAqPagzcTbl.getTzPageIswburl());
				exUrl = TZUtility.transFormchar(psTzAqPagzcTbl.getTzPageWburl());
				JSClass = TZUtility.transFormchar(psTzAqPagzcTbl.getTzPageKhdjs());
				appClass = TZUtility.transFormchar(psTzAqPagzcTbl.getTzPageFwdcls());
				isDefault = TZUtility.transFormchar(psTzAqPagzcTbl.getTzPageMrsy());
				isNewWin = TZUtility.transFormchar(psTzAqPagzcTbl.getTzPageNewwin());
				refCode = TZUtility.transFormchar(psTzAqPagzcTbl.getTzPageRefcode());

				// 组件注册信息;
				strRet = "{\"comID\":\"" + strComID + "\",\"pageID\":\"" + strPageID + "\",\"pageName\":\""
						+ strPageName + "\",\"orderNum\":" + numOrder + ",\"isExURL\":\"" + isExUrl + "\",\"exURL\":\""
						+ exUrl + "\",\"jsClass\":\"" + JSClass + "\",\"appClass\":\"" + appClass
						+ "\",\"isDefault\":\"" + isDefault + "\",\"isNewWin\":\"" + isNewWin + "\",\"refCode\":\""
						+ refCode + "\"}";
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
}
