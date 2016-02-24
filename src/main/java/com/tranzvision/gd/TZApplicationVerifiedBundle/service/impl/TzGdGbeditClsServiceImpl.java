package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzAppproRstTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzAppproRstT;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzAppproRstTKey;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author tang PS:TZ_GD_LCFB_PKG:TZ_GD_GBEDIT_CLS
 */
@Service("com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl.TzGdGbeditClsServiceImpl")
public class TzGdGbeditClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzAppproRstTMapper psTzAppproRstTMapper;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	// 查询表单
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			String str_bj_id = jacksonUtil.getString("bj_id");
			String str_bmlc_id = jacksonUtil.getString("bmlc_id");
			Long str_bmb_id = Long.parseLong(jacksonUtil.getString("bmb_id"));
			String str_color_id = "", str_fb_desc = "", str_fb_desc1 = "";
			String sql = "SELECT TZ_APPPRO_HF_BH,TZ_APPPRO_RST FROM PS_TZ_APPPRO_RST_T WHERE TZ_CLASS_ID=? AND TZ_APPPRO_ID=? AND TZ_APP_INS_ID=?";
			Map<String, Object> map = jdbcTemplate.queryForMap(sql,
					new Object[] { str_bj_id, str_bmlc_id, str_bmb_id });
			if (map != null) {
				str_color_id = (String) map.get("TZ_APPPRO_HF_BH");
				str_fb_desc = (String) map.get("TZ_APPPRO_RST");
				if (str_color_id == null || "".equals(str_color_id)) {
					sql = "SELECT TZ_CLS_RESULT,TZ_APPPRO_CONTENT FROM PS_TZ_CLS_BMLCHF_T WHERE TZ_CLASS_ID=? AND TZ_APPPRO_ID=? AND TZ_WFB_DEFALT_BZ='on' limit 0,1";
					Map<String, Object> map1 = jdbcTemplate.queryForMap(sql, new Object[] { str_bj_id, str_bmlc_id });
					if (map1 != null) {
						// str_hfy_name = (String)map1.get("TZ_CLS_RESULT");
						str_fb_desc1 = (String) map1.get("TZ_APPPRO_CONTENT");
					}
				}
				if (str_fb_desc == null || "".equals(str_fb_desc)) {
					str_fb_desc = str_fb_desc1;
				}
				
			}
			String str_oprid = jdbcTemplate.queryForObject(
					"SELECT OPRID FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID=? AND TZ_APP_INS_ID=?",
					new Object[] { str_bj_id, str_bmb_id }, "String");
			String str_first_name = jdbcTemplate.queryForObject(
					"SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?", new Object[] { str_oprid },
					"String");

			Map<String, Object> jsonMap = new HashMap<>();
			jsonMap.put("bj_id", str_bj_id);
			jsonMap.put("bmlc_id", str_bmlc_id);
			jsonMap.put("bmb_id", String.valueOf(str_bmb_id));
			jsonMap.put("ry_name", str_first_name);
			jsonMap.put("jg_id", str_color_id);
			jsonMap.put("plgb_area", str_fb_desc);
			returnJsonMap.replace("formData", jsonMap);

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}

	// 修改;
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		// 返回值;
		String strComContent = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (actData.length == 0) {
			return strComContent;
		}

		try {
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			for (int num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				// 保存数据;
				String str_bj_id = jacksonUtil.getString("bj_id");
				String str_bmlc_id = jacksonUtil.getString("bmlc_id");
				long str_bmb_id = Long.parseLong(jacksonUtil.getString("bmb_id"));
				String str_jg_id = jacksonUtil.getString("jg_id");
				String str_plgb_area = jacksonUtil.getString("plgb_area");

				PsTzAppproRstTKey psTzAppproRstTKey = new PsTzAppproRstTKey();
				psTzAppproRstTKey.setTzAppInsId(str_bmb_id);
				psTzAppproRstTKey.setTzAppproId(str_bmlc_id);
				psTzAppproRstTKey.setTzClassId(str_bj_id);
				PsTzAppproRstT psTzAppproRstT = psTzAppproRstTMapper.selectByPrimaryKey(psTzAppproRstTKey);
				if (psTzAppproRstT != null) {
					psTzAppproRstT.setTzClassId(str_bj_id);
					psTzAppproRstT.setTzAppproId(str_bmlc_id);
					psTzAppproRstT.setTzAppInsId(str_bmb_id);
					psTzAppproRstT.setTzAppproHfBh(str_jg_id);
					psTzAppproRstT.setTzAppproRst(str_plgb_area);
					psTzAppproRstT.setRowLastmantDttm(new Date());
					psTzAppproRstT.setRowLastmantOprid(oprid);
					psTzAppproRstTMapper.updateByPrimaryKeySelective(psTzAppproRstT);
				} else {
					psTzAppproRstT = new PsTzAppproRstT();
					psTzAppproRstT.setTzClassId(str_bj_id);
					psTzAppproRstT.setTzAppproId(str_bmlc_id);
					psTzAppproRstT.setTzAppInsId(str_bmb_id);
					psTzAppproRstT.setTzAppproHfBh(str_jg_id);
					psTzAppproRstT.setTzAppproRst(str_plgb_area);
					psTzAppproRstT.setRowAddedDttm(new Date());
					psTzAppproRstT.setRowAddedOprid(oprid);
					psTzAppproRstT.setRowLastmantDttm(new Date());
					psTzAppproRstT.setRowLastmantOprid(oprid);
					psTzAppproRstTMapper.insert(psTzAppproRstT);
				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strComContent;
	}
	
	// 根据不同的结果获取公布结果;
	@Override
	public String tzGetJsonData(String strParams){
		Map<String, Object> returnMap = new HashMap<>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			jacksonUtil.json2Map(strParams);
			String str_bj_id = jacksonUtil.getString("bj_id");
			String str_bmlc_id = jacksonUtil.getString("bmlc_id");
			String str_old_value = jacksonUtil.getString("old_value");
			String str_new_value = jacksonUtil.getString("new_value");
			String str_desc = "";
			String str_content = "1";
			if(! "null".equals(str_old_value)){
				String sql = "SELECT TZ_APPPRO_CONTENT FROM PS_TZ_CLS_BMLCHF_T WHERE TZ_CLASS_ID=? AND TZ_APPPRO_ID=? AND TZ_APPPRO_HF_BH=?";
				str_desc = jdbcTemplate.queryForObject(sql, new Object[]{str_bj_id,str_bmlc_id,str_new_value},"String");
			}else{
				str_content = "2";
			}
			returnMap.put("gbjg_pd", str_content);
			returnMap.put("gbjg_desc",str_desc);
		}catch(Exception e){
			e.printStackTrace();
			return e.toString();
		}
		return jacksonUtil.Map2json(returnMap);
	}

}
