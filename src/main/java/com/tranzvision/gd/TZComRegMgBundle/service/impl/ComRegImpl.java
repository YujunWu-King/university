package com.tranzvision.gd.TZComRegMgBundle.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZComRegMgBundle.dao.PsTzAqComzcTblMapper;
import com.tranzvision.gd.TZComRegMgBundle.dao.PsTzAqPagzcTblMapper;
import com.tranzvision.gd.TZComRegMgBundle.model.PsTzAqComzcTbl;
import com.tranzvision.gd.TZComRegMgBundle.model.PsTzAqPagzcTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TZUtility;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * @author tang
 * @version 1.0, 2015-10-9
 * @功能：功能组件注册管理相关类 原ps类：TZ_GD_COMREGMG_PKG:TZ_GD_COMREG_CLS
 */
@Service("com.tranzvision.gd.TZComRegMgBundle.service.impl.ComRegImpl")
public class ComRegImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private JacksonUtil jacksonUtil;
	@Autowired
	private PsTzAqComzcTblMapper psTzAqComzcTblMapper;
	@Autowired
	private PsTzAqPagzcTblMapper psTzAqPagzcTblMapper;

	/* 新增组件注册信息 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				if ("COM".equals(strFlag)) {
					// 组件编号;
					String strComID = (String) infoData.get("comID");
					// 组件名称;
					String comName = (String) infoData.get("comName");

					// 是否已经存在;
					String comExistSql = "SELECT 'Y' FROM PS_TZ_AQ_COMZC_TBL WHERE TZ_COM_ID=?";
					String isExist = "";
					isExist = jdbcTemplate.queryForObject(comExistSql, new Object[] { strComID }, "String");

					if ("Y".equals(isExist)) {
						errMsg[0] = "1";
						errMsg[1] = "组件ID为：" + strComID + "的信息已经注册，请修改组件ID。";
						return strRet;
					}

					PsTzAqComzcTbl psTzAqComzcTbl = new PsTzAqComzcTbl();
					psTzAqComzcTbl.setTzComId(strComID);
					psTzAqComzcTbl.setTzComMc(comName);
					/****** TODO, 应该是当前登录人员 %userid ****/
					psTzAqComzcTbl.setRowAddedOprid("TZ_7");
					psTzAqComzcTbl.setRowAddedDttm(new Date());
					psTzAqComzcTbl.setRowLastmantOprid("TZ_7");
					psTzAqComzcTbl.setRowLastmantDttm(new Date());
					psTzAqComzcTblMapper.insert(psTzAqComzcTbl);
				}

				if ("PAGE".equals(strFlag)) {
					strRet = this.tzUpdatePageInfo(infoData, errMsg);
				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}

	/* 修改组件注册信息 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				if ("COM".equals(strFlag)) {
					// 组件编号;
					String strComID = (String) infoData.get("comID");
					// 组件名称;
					String comName = (String) infoData.get("comName");

					// 是否已经存在;
					String comExistSql = "SELECT 'Y' FROM PS_TZ_AQ_COMZC_TBL WHERE TZ_COM_ID=?";
					String isExist = "";
					isExist = jdbcTemplate.queryForObject(comExistSql, new Object[] { strComID }, "String");

					if (!"Y".equals(isExist)) {
						errMsg[0] = "1";
						errMsg[1] = "组件ID为：" + strComID + "的信息不存在。";
						return strRet;
					}

					PsTzAqComzcTbl psTzAqComzcTbl = new PsTzAqComzcTbl();
					psTzAqComzcTbl.setTzComId(strComID);
					psTzAqComzcTbl.setTzComMc(comName);
					/****** TODO, 应该是当前登录人员 %userid ****/
					psTzAqComzcTbl.setRowLastmantOprid("TZ_7");
					psTzAqComzcTbl.setRowLastmantDttm(new Date());
					psTzAqComzcTblMapper.updateByPrimaryKeySelective(psTzAqComzcTbl);

				}

				if ("PAGE".equals(strFlag)) {
					strRet = this.tzUpdatePageInfo(infoData, errMsg);
				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}

	/* 获取组件注册信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("comID")) {
				String strComID = jacksonUtil.getString("comID");
				if (strComID != null && !"".equals(strComID)) {

					PsTzAqComzcTbl psTzAqComzcTbl = psTzAqComzcTblMapper.selectByPrimaryKey(strComID);
					if (psTzAqComzcTbl != null) {
						// 组件注册信息;
						strRet = "{\"formData\":{\"comID\":\"" + TZUtility.transFormchar(strComID) + "\",\"comName\":\""
								+ TZUtility.transFormchar(psTzAqComzcTbl.getTzComMc()) + "\"}}";
					} else {
						errMsg[0] = "1";
						errMsg[1] = "无法获取组件信息";
					}

				} else {
					errMsg[0] = "1";
					errMsg[1] = "无法获取组件信息";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "无法获取组件信息";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/* 获取页面注册信息列表 */
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errMsg) {
		// 返回值;
		String strRet = "";

		// 页面注册信息列表内容;
		String strPageContent = "";
		// 页面注册信息总数;
		int numTotal = 0;

		try {
			jacksonUtil.json2Map(strParams);

			String strComID = jacksonUtil.getString("comID");
			if (strComID != null && !"".equals(strComID)) {

				// 页面ID，页面名称，是否默认首页;
				String strPageID = "", strPageName = "", isDefault = "";
				// 序号;
				int numOrder = 0;
				// 页面注册信息列表sql;
				String sqlPageList = "";
				/******* 查询组件下的页面列表 ****/
				Object[] obj = null;
				if (numLimit == 0) {
					sqlPageList = "SELECT TZ_PAGE_ID,TZ_PAGE_MC,TZ_PAGE_XH,TZ_PAGE_MRSY FROM PS_TZ_AQ_PAGZC_TBL WHERE TZ_COM_ID=? ORDER BY TZ_PAGE_XH";
					obj = new Object[] { strComID };
				} else {
					sqlPageList = "SELECT TZ_PAGE_ID,TZ_PAGE_MC,TZ_PAGE_XH,TZ_PAGE_MRSY FROM PS_TZ_AQ_PAGZC_TBL WHERE TZ_COM_ID=? ORDER BY TZ_PAGE_XH limit ?,?";
					obj = new Object[] { strComID, numStart, numLimit };
				}

				List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlPageList, obj);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						isDefault = (String) list.get(i).get("TZ_PAGE_MRSY");
						if (!"Y".equals(isDefault)) {
							isDefault = "N";
						} 
						strPageID = (String) list.get(i).get("TZ_PAGE_ID");
						strPageName = (String) list.get(i).get("TZ_PAGE_MC");
						numOrder = (int) list.get(i).get("TZ_PAGE_XH");
	
						strPageContent = strPageContent + ",{\"comID\":\"" + TZUtility.transFormchar(strComID)
								+ "\",\"pageID\":\"" + TZUtility.transFormchar(strPageID) + "\",\"pageName\":\""
								+ TZUtility.transFormchar(strPageName) + "\",\"orderNum\":" + numOrder + ",\"isDefault\":\""
								+ isDefault + "\"}";
					}
				}
				if (!"".equals(strPageContent)) {
					strPageContent = strPageContent.substring(1);
				}

				/******* 查询组件总的页面数 ****/
				String totalSQL = "SELECT COUNT(1) FROM PS_TZ_AQ_PAGZC_TBL WHERE TZ_COM_ID=?";
				numTotal = jdbcTemplate.queryForObject(totalSQL, new Object[] { strComID }, "Integer");

				strRet = "{\"total\":" + numTotal + ",\"root\":[" + strPageContent + "]}";
			} else {
				errMsg[0] = "1";
				errMsg[1] = "无法获取组件页面信息";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/* 删除页面注册信息列表 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 组件ID;
				String sComID = jacksonUtil.getString("comID");
				// 页面ID;
				String sPageID = jacksonUtil.getString("pageID");
				
				PsTzAqPagzcTbl psTzAqPagzcTbl = new PsTzAqPagzcTbl();
				psTzAqPagzcTbl.setTzComId(sComID);
				psTzAqPagzcTbl.setTzPageId(sPageID);
				
				psTzAqPagzcTblMapper.deleteByPrimaryKey(psTzAqPagzcTbl);
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}

	private String tzUpdatePageInfo(Map<String, Object> infoData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		try {
			// 组件编号;
			String strComID = (String) infoData.get("comID");
			// 组件名称;
			String strPageID = (String) infoData.get("pageID");
			// 序号;
			String numOrderStr = String.valueOf(infoData.get("orderNum"));
			short numOrder = Short.parseShort(numOrderStr);
			
			// 默认首页;
			boolean isDefaultBoolean = (boolean) infoData.get("isDefault");
			String isDefault = "N";
			if (isDefaultBoolean) {
				isDefault = "Y";
			} else {
				isDefault = "N";
			}

			if ("Y".equals(isDefault)) {
				// 默认首页只能有一个;
				String updateDefalutNoSQL = "UPDATE PS_TZ_AQ_PAGZC_TBL SET TZ_PAGE_MRSY='N' WHERE TZ_COM_ID=?";

				jdbcTemplate.update(updateDefalutNoSQL, new Object[] { strComID });
			}

			// 页面注册信息表;
			PsTzAqPagzcTbl psTzAqPagzcTbl = new PsTzAqPagzcTbl();
			psTzAqPagzcTbl.setTzComId(strComID);
			psTzAqPagzcTbl.setTzPageId(strPageID);
			psTzAqPagzcTbl.setTzPageXh(numOrder);
			psTzAqPagzcTbl.setTzPageMrsy(isDefault);
			psTzAqPagzcTblMapper.updateByPrimaryKeySelective(psTzAqPagzcTbl);
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

}
