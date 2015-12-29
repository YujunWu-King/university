package com.tranzvision.gd.TZThemeMgBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZThemeMgBundle.dao.PsTzPtZtxxTblMapper;
import com.tranzvision.gd.TZThemeMgBundle.dao.PsTzPtZtzyTblMapper;
import com.tranzvision.gd.TZThemeMgBundle.model.PsTzPtZtxxTbl;
import com.tranzvision.gd.TZThemeMgBundle.model.PsTzPtZtzyTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author tang 功能说明：高端产品-主题资源集合信息列表 原PS类：TZ_GD_THEMEGL_PKG：TZ_GD_THEMERES_CLS
 */
@Service("com.tranzvision.gd.TZThemeMgBundle.service.impl.ThemeResServiceImpl")
public class ThemeResServiceImpl extends FrameworkImpl {
	@Autowired
	private PsTzPtZtxxTblMapper psTzPtZtxxTblMapper;
	@Autowired
	private PsTzPtZtzyTblMapper psTzPtZtzyTblMapper;
	@Autowired
	private SqlQuery jdbcTemplate;

	/* 获取主题定义信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("themeID")) {
				// themeID;
				String str_zt_id = jacksonUtil.getString("themeID");
				PsTzPtZtxxTbl psTzPtZtxxTbl = psTzPtZtxxTblMapper.selectByPrimaryKey(str_zt_id);
				if (psTzPtZtxxTbl != null) {
					Map<String, Object> map = new HashMap<>();
					map.put("themeID", psTzPtZtxxTbl.getTzZtId());
					map.put("themeName", psTzPtZtxxTbl.getTzZtMc());
					map.put("themeDesc", psTzPtZtxxTbl.getTzZtMs());
					map.put("themeState", psTzPtZtxxTbl.getTzYxx());
					returnJsonMap.replace("formData", map);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该主题数据不存在";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "该主题数据不存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}

	/* 加载主题资源集合列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		int total = 0;
		try {
			// 将字符串转换成json;
			jacksonUtil.json2Map(comParams);
			String strThemeId = jacksonUtil.getString("themeID");
			if (strThemeId != null && !"".equals(strThemeId)) {
				// 查询总条数;
				String totalSQL = "SELECT count(1) FROM PS_TZ_PT_ZTZY_TBL A, PS_TZ_PT_ZYJH_TBL B where A.TZ_ZYJH_ID=B.TZ_ZYJH_ID and TZ_ZT_ID=?";
				total = jdbcTemplate.queryForObject(totalSQL, new Object[] { strThemeId }, "Integer");

				String sql = "SELECT A.TZ_ZYJH_ID,B.TZ_ZYJH_MC FROM PS_TZ_PT_ZTZY_TBL A, PS_TZ_PT_ZYJH_TBL B where A.TZ_ZYJH_ID=B.TZ_ZYJH_ID and TZ_ZT_ID=? ORDER BY A.TZ_ZYJH_ID limit ?,?";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,
						new Object[] { strThemeId, numStart, numLimit });
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> mapList = new HashMap<String, Object>();
						mapList.put("themeID", strThemeId);
						mapList.put("resSetID", (String) list.get(i).get("TZ_ZYJH_ID"));
						mapList.put("resSetDesc", (String) list.get(i).get("TZ_ZYJH_MC"));
						listData.add(mapList);
					}
				}
				
				mapRet.replace("total",total);
				mapRet.replace("root", listData);

			} else {
				errorMsg[0] = "1";
				errorMsg[1] = "未获得主题编号";
			}

		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(mapRet);
	}
	
	@Override
	/* 新增主题信息 */
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String typeFlag = jacksonUtil.getString("typeFlag");
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");

				if ("THEME".equals(typeFlag)) {
					String str_zt_id = (String) infoData.get("themeID");
					String str_zt_mc = (String) infoData.get("themeName");
					String str_zt_ms = (String) infoData.get("themeDesc");
					String str_yxx = (String) infoData.get("themeState");
					
					String sql = "select COUNT(1) from PS_TZ_PT_ZTXX_TBL WHERE TZ_ZT_ID=?";
					int count = jdbcTemplate.queryForObject(sql, new Object[]{str_zt_id},"Integer");
					if(count > 0){
						errMsg[0] = "1";
						errMsg[1] = "主题编号为：" + str_zt_id + "的信息已经存在，请修改主题编号。";
					}else{
						PsTzPtZtxxTbl psTzPtZtxxTbl = new PsTzPtZtxxTbl();
						psTzPtZtxxTbl.setTzZtId(str_zt_id);
						psTzPtZtxxTbl.setTzZtMc(str_zt_mc);
						psTzPtZtxxTbl.setTzZtMs(str_zt_ms);
						psTzPtZtxxTbl.setTzYxx(str_yxx);
						psTzPtZtxxTblMapper.insert(psTzPtZtxxTbl);
					}
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	@Override
	/* 修改主题信息 */
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String typeFlag = jacksonUtil.getString("typeFlag");
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");

				if ("THEME".equals(typeFlag)) {
					String str_zt_id = (String) infoData.get("themeID");
					String str_zt_mc = (String) infoData.get("themeName");
					String str_zt_ms = (String) infoData.get("themeDesc");
					String str_yxx = (String) infoData.get("themeState");
					PsTzPtZtxxTbl psTzPtZtxxTbl = new PsTzPtZtxxTbl();
					psTzPtZtxxTbl.setTzZtId(str_zt_id);
					psTzPtZtxxTbl.setTzZtMc(str_zt_mc);
					psTzPtZtxxTbl.setTzZtMs(str_zt_ms);
					psTzPtZtxxTbl.setTzYxx(str_yxx);
					psTzPtZtxxTblMapper.updateByPrimaryKey(psTzPtZtxxTbl);
				}

				if ("RESSET".equals(typeFlag)) {
					// 主题编号;
					String strThemeId = (String) infoData.get("themeID");
					// 资源集合编号;
					String strResSetID = (String) infoData.get("resSetID");
					String sql = "SELECT count(1) FROM PS_TZ_PT_ZTZY_TBL WHERE TZ_ZT_ID=? and TZ_ZYJH_ID=?";
					int count = jdbcTemplate.queryForObject(sql, new Object[] { strThemeId, strResSetID }, "Integer");
					if (count > 0) {

					} else {
						PsTzPtZtzyTblKey psTzPtZtzyTblKey = new PsTzPtZtzyTblKey();
						psTzPtZtzyTblKey.setTzZtId(strThemeId);
						psTzPtZtzyTblKey.setTzZyjhId(strResSetID);
						psTzPtZtzyTblMapper.insert(psTzPtZtzyTblKey);
					}
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	@Override
	/* 删除主题资源信息 */
	public String tzDelete(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);

				// 主题编号;
				String strThemeId = jacksonUtil.getString("themeID");
				// 资源集合编号;
				String strResSetID = jacksonUtil.getString("resSetID");

				PsTzPtZtzyTblKey psTzPtZtzyTblKey = new PsTzPtZtzyTblKey();
				psTzPtZtzyTblKey.setTzZtId(strThemeId);
				psTzPtZtzyTblKey.setTzZyjhId(strResSetID);
				psTzPtZtzyTblMapper.deleteByPrimaryKey(psTzPtZtzyTblKey);

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
}
