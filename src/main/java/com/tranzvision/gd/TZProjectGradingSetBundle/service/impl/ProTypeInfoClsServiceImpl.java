package com.tranzvision.gd.TZProjectGradingSetBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZProjectGradingSetBundle.dao.PsTzPrjTypeTMapper;
import com.tranzvision.gd.TZProjectGradingSetBundle.model.PsTzPrjTypeT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author 张彬彬; 
 * 功能说明：项目分类定义;
 * 原PS类：TZ_GD_PROTYPE_PKG:TZ_PROTYPEINF0_CLS
 */
@Service("com.tranzvision.gd.TZProjectGradingSetBundle.service.impl.ProTypeInfoClsServiceImpl")
public class ProTypeInfoClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzPrjTypeTMapper PsTzPrjTypeTMapper;
	
	/* 获取资源集合信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("proTypeId")) {
				// 项目分类编号;
				String strProTypeId = jacksonUtil.getString("proTypeId");
				PsTzPrjTypeT psTzPrjTypeT = PsTzPrjTypeTMapper.selectByPrimaryKey(strProTypeId);
				if (psTzPrjTypeT != null) {
					Map<String, Object> map = new HashMap<>();
					map.put("proTypeId", psTzPrjTypeT.getTzPrjTypeId());
					map.put("proTypeName", psTzPrjTypeT.getTzPrjTypeName());
					map.put("proTypeDesc", psTzPrjTypeT.getTzPrjTypeDesc());
					map.put("proTypeStatus", psTzPrjTypeT.getTzPrjTypeStatus());
					
					returnJsonMap.replace("formData", map);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该项目分类数据不存在";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "该项目分类数据不存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}
	
}
