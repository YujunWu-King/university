package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClsBmlcTMapper;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsBmlcTKey;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsBmlcTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author tang 
 * PS:TZ_GD_LCFB_PKG:TZ_GD_BJMB_CLS
 */
@Service("com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl.TzGdBjmbClsServiceImpl")
public class TzGdBjmbClsServiceImpl extends FrameworkImpl {
	@Autowired
	private PsTzClsBmlcTMapper psTzClsBmlcTMapper;
	@Autowired
	private SqlQuery jdbcTemplate;

	// 获取班级信息
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			// 信息内容;
			Map<String, Object> infoData = jacksonUtil.getMap("data");

			String str_bj_id = (String) infoData.get("bj_id");
			String str_bmlc_id = (String) infoData.get("bmlc_id");
			String str_bjmb = "";
			if (str_bj_id != null && !"".equals(str_bj_id) && str_bmlc_id != null && !"".equals(str_bmlc_id)) {
				str_bjmb = jdbcTemplate.queryForObject(
						"SELECT TZ_TMP_CONTENT FROM PS_TZ_CLS_BMLC_T WHERE TZ_CLASS_ID=? AND TZ_APPPRO_ID=?",
						new Object[] { str_bj_id, str_bmlc_id }, "String");
				if (str_bjmb == null) {
					str_bjmb = "";
				}
				Map<String, Object> map = new HashMap<>();
				map.put("bj_id", str_bj_id);
				map.put("bmlc_id", str_bmlc_id);
				map.put("bmlc_mbnr", str_bjmb);
				returnJsonMap.replace("formData", map);
			} else {
				errMsg[0] = "1";
				errMsg[1] = "参数不正确！";
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}

	// 修改;
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		// 返回值;
		String strComContent = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (actData.length == 0) {
			return strComContent;
		}

		try {

			for (int num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				// 保存数据;
				String str_bj_id = jacksonUtil.getString("bj_id");
				String str_bmlc_id = jacksonUtil.getString("bmlc_id");
				if (str_bj_id != null && !"".equals(str_bj_id) && str_bmlc_id != null && !"".equals(str_bmlc_id)) {
					PsTzClsBmlcTKey psTzClsBmlcTKey = new PsTzClsBmlcTKey();
					psTzClsBmlcTKey.setTzAppproId(str_bmlc_id);
					psTzClsBmlcTKey.setTzClassId(str_bj_id);
					PsTzClsBmlcTWithBLOBs psTzClsBmlcT = psTzClsBmlcTMapper.selectByPrimaryKey(psTzClsBmlcTKey);
					if (psTzClsBmlcT != null) {
						String bmlc_mbnr = jacksonUtil.getString("bmlc_mbnr");
						psTzClsBmlcT.setTzTmpContent(bmlc_mbnr);
						psTzClsBmlcTMapper.updateByPrimaryKeySelective(psTzClsBmlcT);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "更新数据时发生错误！";
					}
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strComContent;
	}
}
