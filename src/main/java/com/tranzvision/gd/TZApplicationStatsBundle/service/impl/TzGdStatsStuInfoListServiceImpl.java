package com.tranzvision.gd.TZApplicationStatsBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author WRL 报名管理-学生报考多项目统计--学生报考多项目信息
 */
@Service("com.tranzvision.gd.TZApplicationStatsBundle.service.impl.TzGdStatsStuInfoListServiceImpl")
public class TzGdStatsStuInfoListServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	/* 学生报考多项目信息列表 */
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
			jacksonUtil.json2Map(comParams);
			// 统计编号
			String statsId = jacksonUtil.getString("statsId");
			// 报考学生姓名
			String paramUsername = jacksonUtil.getString("username");
			// 报考多项目(Y Or N)
			String paramIsMulti = jacksonUtil.getString("isMulti");

			String statsSql = "SELECT TZ_CLASS_ID FROM PS_TZ_STU_STATS_T WHERE TZ_STATS_ID = ?";
			String paramClassId = sqlQuery.queryForObject(statsSql, new Object[] { statsId }, "String");
			String sql = "SELECT * FROM (SELECT DISTINCT(OPRID) ,TZ_CLASS_ID,(SELECT TZ_REALNAME FROM PS_TZ_REG_USER_T WHERE OPRID = A.OPRID) TZ_REALNAME, (SELECT NATIONAL_ID FROM PS_TZ_REG_USER_T WHERE OPRID = A.OPRID) NATIONAL_ID, (SELECT CASE WHEN COUNT(OPRID) > 1 THEN 'Y' ELSE 'N' END FROM PS_TZ_FORM_WRK_T WHERE OPRID = A.OPRID) ISMULTI FROM PS_TZ_FORM_WRK_T A GROUP BY A.OPRID) S WHERE 1=1 ";

			/* 统计指定班级的学生 */
			if (StringUtils.isNotBlank(paramClassId)) {
				sql = sql + " AND TZ_CLASS_ID = '" + paramClassId + "'";
			}
			/* 统计班级下的指定学生 */
			if (StringUtils.isNoneBlank(paramUsername)) {
				sql = sql + " AND TZ_REALNAME LIKE '%" + paramUsername + "%'";
			}
			/* 指定班级下报考单个项目或多个项目的学生 */
			if (StringUtils.isNoneBlank(paramIsMulti)) {
				sql = sql + " AND ISMULTI = '" + paramIsMulti + "'";
			}

			List<?> resultlist = sqlQuery.queryForList(sql, new Object[] {});
			if (resultlist == null) {
				return jacksonUtil.Map2json(mapRet);
			}
			for (Object obj : resultlist) {
				Map<String, Object> result = (Map<String, Object>) obj;

				String oprid = result.get("OPRID") == null ? "" : String.valueOf(result.get("OPRID"));
				String username = result.get("TZ_REALNAME") == null ? "" : String.valueOf(result.get("TZ_REALNAME"));
				String codeId = result.get("NATIONAL_ID") == null ? "" : String.valueOf(result.get("NATIONAL_ID"));
				String isMulti = result.get("ISMULTI") == null ? "" : String.valueOf(result.get("ISMULTI"));
				if(StringUtils.equals("Y", isMulti)){
					isMulti = "是";
				}else{
					isMulti = "否";
				}
				Map<String, Object> mapRetJson = new HashMap<String, Object>();
				mapRetJson.put("username", username);
				mapRetJson.put("codeId", codeId);
				mapRetJson.put("isMulti", isMulti);

				String sqlMulti = "SELECT TZ_CLASS_ID FROM PS_TZ_STATS_PROJECT_T WHERE TZ_STATS_ID = ? ORDER BY TZ_ORDER";
				Map<String, Object> mapBaokao = this.searchExamCls(oprid, statsId);

				List<?> retClslist = sqlQuery.queryForList(sqlMulti, new Object[] { statsId });
				for (Object object : retClslist) {
					Map<String, Object> ret = (Map<String, Object>) object;
					String clsid = ret.get("TZ_CLASS_ID") == null ? "" : String.valueOf(ret.get("TZ_CLASS_ID"));
//					String isBaokao = mapBaokao.containsKey(clsid) ? "Y" : "N";
					String isBaokao = mapBaokao.containsKey(clsid) ? "是" : "否";
					mapRetJson.put("dIndex_" + clsid, isBaokao);
				}

				listData.add(mapRetJson);
			}

			mapRet.replace("total", resultlist.size());
			mapRet.replace("root", listData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);
	}

	/* 查找某个学生报考了几个班级 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> searchExamCls(String oprid, String statsId) {
		Map<String, Object> mapRetJson = new HashMap<String, Object>();
		String sql = "SELECT W.TZ_CLASS_ID FROM PS_TZ_FORM_WRK_T W LEFT JOIN PS_TZ_STATS_PROJECT_T P ON W.TZ_CLASS_ID = P.TZ_CLASS_ID WHERE W.OPRID = ? AND W.TZ_CLASS_ID IN (SELECT TZ_CLASS_ID FROM PS_TZ_STATS_PROJECT_T WHERE TZ_STATS_ID = ?)";
		List<?> retlist = sqlQuery.queryForList(sql, new Object[] { oprid, statsId });
		if (retlist != null && retlist.size() > 0) {
			for (Object object : retlist) {
				Map<String, Object> ret = (Map<String, Object>) object;
				String clsid = ret.get("TZ_CLASS_ID") == null ? "" : String.valueOf(ret.get("TZ_CLASS_ID"));
				mapRetJson.put(clsid, clsid);
			}
		}
		return mapRetJson;
	}
}
