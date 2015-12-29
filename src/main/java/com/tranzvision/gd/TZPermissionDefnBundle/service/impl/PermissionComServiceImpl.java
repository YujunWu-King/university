package com.tranzvision.gd.TZPermissionDefnBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZPermissionDefnBundle.dao.PsTzAqComsqTblMapper;
import com.tranzvision.gd.TZPermissionDefnBundle.model.PsTzAqComsqTbl;
import com.tranzvision.gd.TZPermissionDefnBundle.model.PsTzAqComsqTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/*
 * @author tang
 * 许可权组件定义，原PS类：TZ_GD_PLST_PKG:TZ_GD_PERMCOMP_CLS
 */
@Service("com.tranzvision.gd.TZPermissionDefnBundle.service.impl.PermissionComServiceImpl")
public class PermissionComServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzAqComsqTblMapper psTzAqComsqTblMapper;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;

	/* 获取组件授权页面列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			// 将字符串转换成json;
			jacksonUtil.json2Map(comParams);
			// 许可权编号;
			String strPermID = jacksonUtil.getString("permID");
			// 组件ID;
			String strComID = jacksonUtil.getString("comID");
			// 页面ID,页面名称，只读，修改;
			String strPageID, strPageName;
			int numReadonly, numModify;

			// 授权组件页面列表sql;
			String sqlComPageList;
			String totalSQL = "";
			List<Map<String, Object>> list = null;
			if (numLimit == 0) {
				sqlComPageList = "SELECT TZ_PAGE_ID,TZ_PAGE_MC FROM PS_TZ_AQ_PAGZC_TBL WHERE TZ_COM_ID=?  ORDER BY TZ_PAGE_ID";
				list = jdbcTemplate.queryForList(sqlComPageList, new Object[] { strComID });
			} else {
				sqlComPageList = "SELECT TZ_PAGE_ID,TZ_PAGE_MC FROM PS_TZ_AQ_PAGZC_TBL WHERE TZ_COM_ID=?  ORDER BY TZ_PAGE_ID limit ?,?";
				list = jdbcTemplate.queryForList(sqlComPageList, new Object[] { strComID, numStart, numLimit });
			}

			int total = 0;
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					boolean strReadonly = false;
					boolean strModify = false;
					strPageID = (String) list.get(i).get("TZ_PAGE_ID");
					strPageName = (String) list.get(i).get("TZ_PAGE_MC");
					String sql = "SELECT DISPLAYONLY,TZ_EDIT_FLG FROM PS_TZ_AQ_COMSQ_TBL WHERE CLASSID=? AND TZ_COM_ID=? AND TZ_PAGE_ID=?";
					Map<String, Object> map = jdbcTemplate.queryForMap(sql,
							new Object[] { strPermID, strComID, strPageID });
					if (map != null) {

						numReadonly = (int) map.get("DISPLAYONLY");
						numModify = (int) map.get("TZ_EDIT_FLG");
						if (1 == numReadonly) {
							strReadonly = true;
						} else {
							strReadonly = false;
						}

						if (1 == numModify) {
							strModify = true;
						} else {
							strModify = false;
						}
					}
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("permID", strPermID);
					mapList.put("comID", strComID);
					mapList.put("pageID", strPageID);
					mapList.put("pageName", strPageName);
					mapList.put("readonly", strReadonly);
					mapList.put("modify", strModify);

					listData.add(mapList);

				}
			    totalSQL = "SELECT COUNT(1) FROM PS_TZ_AQ_PAGZC_TBL WHERE TZ_COM_ID=?";
				total = jdbcTemplate.queryForObject(totalSQL, new Object[]{strComID},"Integer");
			    mapRet.replace("total", total);
				mapRet.replace("root", listData);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(mapRet);

	}

	/* 修改许可权信息 */
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				// 许可权编号;
				String strPermID = (String) infoData.get("permID");
				// 授权组件编号;
				String strComID = (String) infoData.get("comID");
				// 页面编号，显示，修改;
				String strPageID;
				strPageID = (String) infoData.get("pageID");
				boolean readonly = (boolean) infoData.get("readonly");
				boolean modify = (boolean) infoData.get("modify");

				Short numReadonly, numModify;
				if (readonly == true) {
					numReadonly = 1;
				} else {
					numReadonly = 0;
				}

				if (modify == true) {
					numModify = 1;
				} else {
					numModify = 0;
				}

				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

				PsTzAqComsqTblKey psTzAqComsqTblKey = new PsTzAqComsqTblKey();
				psTzAqComsqTblKey.setClassid(strPermID);
				psTzAqComsqTblKey.setTzComId(strComID);
				psTzAqComsqTblKey.setTzPageId(strPageID);

				PsTzAqComsqTbl psTzAqComsqTbl = psTzAqComsqTblMapper.selectByPrimaryKey(psTzAqComsqTblKey);
				int i = 0;
				if (psTzAqComsqTbl != null) {
					psTzAqComsqTbl.setDisplayonly(numReadonly);
					psTzAqComsqTbl.setTzEditFlg(numModify);
					psTzAqComsqTbl.setRowLastmantDttm(new Date());
					psTzAqComsqTbl.setRowLastmantOprid(oprid);
					i = psTzAqComsqTblMapper.updateByPrimaryKeySelective(psTzAqComsqTbl);
				} else {
					psTzAqComsqTbl = new PsTzAqComsqTbl();
					psTzAqComsqTbl.setClassid(strPermID);
					;
					psTzAqComsqTbl.setTzComId(strComID);
					psTzAqComsqTbl.setTzPageId(strPageID);
					psTzAqComsqTbl.setDisplayonly(numReadonly);
					psTzAqComsqTbl.setTzEditFlg(numModify);
					psTzAqComsqTbl.setRowAddedDttm(new Date());
					psTzAqComsqTbl.setRowLastmantDttm(new Date());
					psTzAqComsqTbl.setRowAddedOprid(oprid);
					psTzAqComsqTbl.setRowLastmantOprid(oprid);
					i = psTzAqComsqTblMapper.insert(psTzAqComsqTbl);
				}
				if (i <= 0) {
					errMsg[0] = "1";
					errMsg[1] = "保存失败";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
}
