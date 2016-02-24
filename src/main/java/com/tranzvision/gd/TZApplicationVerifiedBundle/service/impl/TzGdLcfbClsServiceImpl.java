package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 * @author tang PS:TZ_GD_LCFB_PKG:TZ_GD_LCFB_CLS
 */
@Service("com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl.TzGdLcfbClsServiceImpl")
public class TzGdLcfbClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzClsBmlcTMapper psTzClsBmlcTMapper;

	// 获得当前班级的报名流程列表
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {

			jacksonUtil.json2Map(comParams);
			String str_bj_id = jacksonUtil.getString("bj_id");

			// 总数;
			int total = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM PS_TZ_CLS_BMLC_T WHERE TZ_CLASS_ID=?",
					new Object[] { str_bj_id }, "Integer");
			List<Map<String, Object>> list;

			if (numLimit > 0) {
				String sql = "SELECT TZ_APPPRO_ID,TZ_SORT_NUM,TZ_APPPRO_NAME,TZ_IS_PUBLIC FROM PS_TZ_CLS_BMLC_T WHERE TZ_CLASS_ID = ? ORDER BY TZ_SORT_NUM ASC limit ?,?";
				list = jdbcTemplate.queryForList(sql, new Object[] { str_bj_id, numStart, numLimit });
			} else {
				String sql = "SELECT TZ_APPPRO_ID,TZ_SORT_NUM,TZ_APPPRO_NAME,TZ_IS_PUBLIC FROM PS_TZ_CLS_BMLC_T WHERE TZ_CLASS_ID = ? ORDER BY TZ_SORT_NUM ASC";
				list = jdbcTemplate.queryForList(sql, new Object[] { str_bj_id });
			}
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					String str_lc_id = (String) list.get(i).get("TZ_APPPRO_ID") == null ? ""
							: (String) list.get(i).get("TZ_APPPRO_ID");
					int str_lc_num = (int) list.get(i).get("TZ_SORT_NUM");
					String str_lc_name = (String) list.get(i).get("TZ_APPPRO_NAME") == null ? ""
							: (String) list.get(i).get("TZ_APPPRO_NAME");
					String str_lc_fb = (String) list.get(i).get("TZ_IS_PUBLIC") == null ? ""
							: (String) list.get(i).get("TZ_IS_PUBLIC");
					boolean bl = false;
					if ("true".equals(str_lc_fb)) {
						bl = true;
					} else {
						bl = false;
					}

					Map<String, Object> map = new HashMap<>();

					map.put("bj_id", str_bj_id);
					map.put("lc_id", str_lc_id);
					map.put("bmlc_xh", String.valueOf(str_lc_num));
					map.put("bmlc_name", str_lc_name);
					map.put("bmlc_fb", bl);

					listData.add(map);
				}
			}
			mapRet.replace("total", total);
			mapRet.replace("root", listData);

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(mapRet);
	}

	// 修改;
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("bj_id", "");
		returnMap.put("siteId", "1");
		returnMap.put("coluId", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (actData.length == 0) {
			return jacksonUtil.Map2json(returnMap);
		}

		try {

			for (int num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");
				if ("sffb".equals(strFlag)) {
					// 信息内容;
					Map<String, Object> infoData = jacksonUtil.getMap("data");
					String str_bj_id = this.tzUpdate_sffb(infoData, errMsg);
					returnMap.replace("bj_id", str_bj_id);
					returnMap.replace("siteId", "1");
					returnMap.replace("coluId", "");
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return jacksonUtil.Map2json(returnMap);
	}

	// 修改是否发布;
	public String tzUpdate_sffb(Map<String, Object> infoData, String[] errMsg) {
		// 返回值;
		String str_bj_id = "";
		try {
			str_bj_id = (String)infoData.get("bj_id");
			String lc_id = (String)infoData.get("lc_id");
			String bmlc_fb = (String)infoData.get("bmlc_fb");
			PsTzClsBmlcTKey psTzClsBmlcTkey = new PsTzClsBmlcTKey();
			psTzClsBmlcTkey.setTzAppproId(lc_id);
			psTzClsBmlcTkey.setTzClassId(str_bj_id);
			PsTzClsBmlcTWithBLOBs psTzClsBmlcT = psTzClsBmlcTMapper.selectByPrimaryKey(psTzClsBmlcTkey);
			if(psTzClsBmlcT != null){
				psTzClsBmlcT.setTzIsPublic(bmlc_fb);
				psTzClsBmlcTMapper.updateByPrimaryKey(psTzClsBmlcT);
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return str_bj_id;
	}

}
