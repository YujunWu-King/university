/**
 * 
 */
package com.tranzvision.gd.TZClassSetBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClsBmlcTMapper;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsBmlcTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 报名流程默认-显示内容，原PS：TZ_GD_BJGL_CLS:TZ_GD_LCXS_CLS
 * 
 * @author SHIHUA
 * @since 2016-02-03
 */
@Service("com.tranzvision.gd.TZClassSetBundle.service.impl.TzClassBmlcxsServiceImpl")
public class TzClassBmlcxsServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private PsTzClsBmlcTMapper psTzClsBmlcTMapper;

	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);

			String bj_id = jacksonUtil.getString("bj_id");
			String bmlc_id = jacksonUtil.getString("bmlc_id");

			Map<String, Object> mapJson = new HashMap<String, Object>();

			if (null != bj_id && !"".equals(bj_id) && null != bmlc_id && !"".equals(bmlc_id)) {
				String sql = "select TZ_TMP_CONTENT from PS_TZ_CLS_BMLC_T where TZ_CLASS_ID=? AND TZ_APPPRO_ID=?";
				String str_bmlc_desc = sqlQuery.queryForObject(sql, new Object[] { bj_id, bmlc_id }, "String");

				mapJson.put("bj_id", bj_id);
				mapJson.put("bmlc_id", bmlc_id);
				mapJson.put("bmlc_desc", str_bmlc_desc);
			}

			mapRet.replace("formData", mapJson);

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "查询失败！" + e.getMessage();
		}

		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}

	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		try {

			JacksonUtil jacksonUtil = new JacksonUtil();

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				Map<String, Object> mapParam = jacksonUtil.getMap("data");

				String bj_id = String.valueOf(mapParam.getOrDefault("bj_id", ""));
				String bmlc_id = String.valueOf(mapParam.getOrDefault("bmlc_id", ""));
				String bmlc_desc = String.valueOf(mapParam.getOrDefault("bmlc_desc", ""));

				if (!"".equals(bj_id) && !"".equals(bmlc_id)) {

					PsTzClsBmlcTWithBLOBs psTzClsBmlcTWithBLOBs = new PsTzClsBmlcTWithBLOBs();
					psTzClsBmlcTWithBLOBs.setTzClassId(bj_id);
					psTzClsBmlcTWithBLOBs.setTzAppproId(bmlc_id);
					psTzClsBmlcTWithBLOBs.setTzTmpContent(bmlc_desc);

					int rst = psTzClsBmlcTMapper.updateByPrimaryKeyWithBLOBs(psTzClsBmlcTWithBLOBs);
					if (rst == 0) {
						errMsg[0] = "1";
						errMsg[1] = "报名流程【" + bmlc_desc + "】更新失败！";
						break;
					}

				} else {
					errMsg[0] = "1";
					errMsg[1] = "参数错误！";
					break;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "更新失败！" + e.getMessage();
		}

		return strRet;
	}

}
