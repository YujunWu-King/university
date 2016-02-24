package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClsBmlchfTMapper;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsBmlchfTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 高端产品-报名审核-报名流程结果公布---回复语设置
 * @author tang
 * PS:TZ_GD_LCFB_PKG:TZ_GD_HFY_CLS
 *
 */
@Service("com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl.TzGdHfyClsServiceImpl")
public class TzGdHfyClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzClsBmlchfTMapper psTzClsBmlchfTMapper;
	
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
			String str_bmlc_id = jacksonUtil.getString("bmlc_id");
		      
			//总数;
	        int total = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM PS_TZ_CLS_BMLCHF_T WHERE TZ_CLASS_ID=? AND TZ_APPPRO_ID=?",new Object[]{str_bj_id, str_bmlc_id},"Integer");
	        List<Map<String, Object>> list ;
	        
	        if(numLimit > 0){
	        	String sql = "SELECT TZ_APPPRO_HF_BH,TZ_APPPRO_COLOR,TZ_CLS_RESULT FROM PS_TZ_CLS_BMLCHF_T WHERE TZ_CLASS_ID = ? AND TZ_APPPRO_ID=? ORDER BY TZ_APPPRO_HF_BH ASC limit ?,?";
	        	list = jdbcTemplate.queryForList(sql,new Object[]{str_bj_id, str_bmlc_id,numStart,numLimit});
	        }else{
	        	String sql = "SELECT TZ_APPPRO_HF_BH,TZ_APPPRO_COLOR,TZ_CLS_RESULT FROM PS_TZ_CLS_BMLCHF_T WHERE TZ_CLASS_ID = ? AND TZ_APPPRO_ID=? ORDER BY TZ_APPPRO_HF_BH ASC";
	        	list = jdbcTemplate.queryForList(sql,new Object[]{str_bj_id, str_bmlc_id});
	        }
	        if(list !=null && list.size()>0){
	        	for(int i = 0; i < list.size(); i++){
	        		String str_dybh_id = (String)list.get(i).get("TZ_APPPRO_HF_BH") == null ?"":(String)list.get(i).get("TZ_APPPRO_HF_BH");
	        		String str_color = (String)list.get(i).get("TZ_APPPRO_COLOR")== null ?"":(String)list.get(i).get("TZ_APPPRO_COLOR");
	        		String str_result_desc = (String)list.get(i).get("TZ_CLS_RESULT")== null ?"":(String)list.get(i).get("TZ_CLS_RESULT");

	        		Map<String, Object> map = new HashMap<>();

	        		map.put("bj_id",  str_bj_id);
	        		map.put("bmlc_id", str_bmlc_id);
	        		map.put("dybh_id", str_dybh_id);
	        		map.put("colorCode", str_color);
	        		map.put("result_desc", str_result_desc);

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
	
	// 新增;
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (actData.length == 0) {
			return strRet;
		}

		try {
			for (int num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				
				String str_bj_id = jacksonUtil.getString("bj_id");
				String str_bmlc_id = jacksonUtil.getString("bmlc_id");
				String str_dybh_id = jacksonUtil.getString("dybh_id");
				if(str_bj_id != null  && !"".equals(str_bj_id)
					&& str_bmlc_id != null  && !"".equals(str_bmlc_id)
					&& str_dybh_id != null  && !"".equals(str_dybh_id)){
					PsTzClsBmlchfTKey psTzClsBmlchfTKey = new PsTzClsBmlchfTKey();
					psTzClsBmlchfTKey.setTzAppproHfBh(str_dybh_id);
					psTzClsBmlchfTKey.setTzAppproId(str_bmlc_id);
					psTzClsBmlchfTKey.setTzClassId(str_bj_id);
					psTzClsBmlchfTMapper.deleteByPrimaryKey(psTzClsBmlchfTKey);
				}
					
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
}
