package com.tranzvision.gd.TZResourceCollectionMgBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZResourceCollectionMgBundle.dao.PsTzPtZyjhTblMapper;
import com.tranzvision.gd.TZResourceCollectionMgBundle.dao.PsTzPtZyxxTblMapper;
import com.tranzvision.gd.TZResourceCollectionMgBundle.model.PsTzPtZyjhTbl;
import com.tranzvision.gd.TZResourceCollectionMgBundle.model.PsTzPtZyxxTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author tang; 功能说明：资源集合信息相关类; 原PS类：TZ_GD_RESSET_PKG:TZ_GD_RESSET_INFO_CLS
 */
@Service("com.tranzvision.gd.TZResourceCollectionMgBundle.service.impl.ResSetInfoServiceImpl")
public class ResSetInfoServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzPtZyjhTblMapper psTzPtZyjhTblMapper;
	@Autowired
	private PsTzPtZyxxTblMapper psTzPtZyxxTblMapper;

	/* 获取资源集合信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("resSetID")) {
				// 资源集合编号;
				String strResSetID = jacksonUtil.getString("resSetID");
				PsTzPtZyjhTbl psTzPtZyjhTbl = psTzPtZyjhTblMapper.selectByPrimaryKey(strResSetID);
				if (psTzPtZyjhTbl != null) {
					Map<String, Object> map = new HashMap<>();
					map.put("resSetID", psTzPtZyjhTbl.getTzZyjhId());
					map.put("resSetDesc", psTzPtZyjhTbl.getTzZyjhMc());
					map.put("publicRes", psTzPtZyjhTbl.getTzZyjhIsgg());
					returnJsonMap.replace("formData", map);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该资源集合数据不存在";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "该资源集合数据不存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}

	/* 获取资源信息列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		int total = 0;
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 将字符串转换成json;
			jacksonUtil.json2Map(comParams);
			// 资源集合编号;
			String strResSetID = jacksonUtil.getString("resSetID");
			if (strResSetID != null && !"".equals(strResSetID)) {
				// 查询总条数;
				String totalSQL = "SELECT COUNT(1) FROM PS_TZ_PT_ZYXX_TBL WHERE TZ_ZYJH_ID=?";
				total = jdbcTemplate.queryForObject(totalSQL, new Object[] { strResSetID }, "Integer");

				String sql = "SELECT TZ_RES_ID,TZ_RES_MC FROM PS_TZ_PT_ZYXX_TBL WHERE TZ_ZYJH_ID=? ORDER BY TZ_RES_ID limit ?,?";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,
						new Object[] { strResSetID, numStart, numLimit });
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> mapList = new HashMap<String, Object>();
						mapList.put("resSetID", strResSetID);
						mapList.put("resourceID", (String) list.get(i).get("TZ_RES_ID"));
						mapList.put("resourceName", (String) list.get(i).get("TZ_RES_MC"));
						listData.add(mapList);
					}
					mapRet.replace("total",total);
					mapRet.replace("root", listData);
				}
			} else {
				errorMsg[0] = "1";
				errorMsg[1] = "未获得资源编号";
			}

		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(mapRet);
	}

	// 新增资源集合信息;
	@Override
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
				// 资源集合信息;
				if ("SET".equals(typeFlag)) {
					// 资源集合编号;
					String strResSetID = (String) infoData.get("resSetID");
					// 资源集合名称;
					String strResSetDesc = (String) infoData.get("resSetDesc");
					// 是否公共资源;
					String strPublicRes = (String) infoData.get("publicRes");

					String sql = "select COUNT(1) from PS_TZ_PT_ZYJH_TBL WHERE TZ_ZYJH_ID=?";
					int count = jdbcTemplate.queryForObject(sql, new Object[] { strResSetID }, "Integer");
					if (count > 0) {
						errMsg[0] = "1";
						errMsg[1] = "资源集合编号为：" + strResSetID + "的信息已经存在，请修改资源编号。";
					} else {
						PsTzPtZyjhTbl psTzPtZyjhTbl = new PsTzPtZyjhTbl();
						psTzPtZyjhTbl.setTzZyjhId(strResSetID);
						psTzPtZyjhTbl.setTzZyjhMc(strResSetDesc);
						psTzPtZyjhTbl.setTzZyjhIsgg(strPublicRes);
						psTzPtZyjhTblMapper.insert(psTzPtZyjhTbl);
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

	// 新增资源集合信息;
	@Override
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
				// 资源集合信息;
				if ("SET".equals(typeFlag)) {
					// 资源集合编号;
					String strResSetID = (String) infoData.get("resSetID");
					// 资源集合名称;
					String strResSetDesc = (String) infoData.get("resSetDesc");
					// 是否公共资源;
					String strPublicRes = (String) infoData.get("publicRes");

					String sql = "select COUNT(1) from PS_TZ_PT_ZYJH_TBL WHERE TZ_ZYJH_ID=?";
					int count = jdbcTemplate.queryForObject(sql, new Object[] { strResSetID }, "Integer");
					if (count > 0) {
						PsTzPtZyjhTbl psTzPtZyjhTbl = new PsTzPtZyjhTbl();
						psTzPtZyjhTbl.setTzZyjhId(strResSetID);
						psTzPtZyjhTbl.setTzZyjhMc(strResSetDesc);
						psTzPtZyjhTbl.setTzZyjhIsgg(strPublicRes);
						psTzPtZyjhTblMapper.updateByPrimaryKey(psTzPtZyjhTbl);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "资源集合编号为：" + strResSetID + "的信息不存在。";
						
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
	
	/* 删除资源信息 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 提交信息
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 资源集合ID;
				String sResSetID = jacksonUtil.getString("resSetID");
				//资源ID;
				String sResourceID = jacksonUtil.getString("resourceID");
				if (sResSetID != null && !"".equals(sResSetID) && sResourceID != null && !"".equals(sResourceID)) {
					PsTzPtZyxxTblKey psTzPtZyxxTblKey = new PsTzPtZyxxTblKey();
					psTzPtZyxxTblKey.setTzZyjhId(sResSetID);
					psTzPtZyxxTblKey.setTzResId(sResourceID);
					psTzPtZyxxTblMapper.deleteByPrimaryKey(psTzPtZyxxTblKey);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
}
