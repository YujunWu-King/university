package com.tranzvision.gd.TZRecommendBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FileManageServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteInfoBundle.service.impl.ArtContentHtml;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzLmNrGlTMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
/**
 * 推荐人邮件发送史
 * 
 * @author 
 * @since 
 */
@Service("com.tranzvision.gd.TZRecommendBundle.service.impl.TzRecommendEmailImpl")
public class TzRecommendEmailImpl extends FrameworkImpl {
	
	@Autowired
	private SqlQuery jdbcTemplate;


	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		String strRet = "";

		JacksonUtil jacksonUtil = new JacksonUtil();

		try {

			jacksonUtil.json2Map(strParams);
			String email = jacksonUtil.getString("email");
			
			
			if (null != email && !"".equals(email)) {
					String sql = "SELECT COUNT(*) FROM PS_TZ_YJFSLSHI_TBL WHERE TZ_JS_EMAIL=?";
					int total = jdbcTemplate.queryForObject(sql, new Object[] { email }, "int");
					
					ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();

					if (total > 0) {

						sql = "SELECT TZ_RWSL_ID,TZ_EM_ZHUTI,TZ_JS_EMAIL,TZ_FS_ZT,date_format(TZ_FS_DT,'%Y-%m-%d') as TZ_FS_DT FROM PS_TZ_YJFSLSHI_TBL A WHERE TZ_JS_EMAIL=? ORDER BY TZ_FS_DT ASC";

						List<Map<String, Object>> listData = jdbcTemplate.queryForList(sql, new Object[] { email });

						for (Map<String, Object> mapData : listData) {
							Map<String, Object> mapJson = new HashMap<String, Object>();

							mapJson.put("rwslID", mapData.get("TZ_RWSL_ID") == null ? "" : String.valueOf(mapData.get("TZ_RWSL_ID")));

							mapJson.put("theme", mapData.get("TZ_EM_ZHUTI") == null ? "" : String.valueOf(mapData.get("TZ_EM_ZHUTI")));

							mapJson.put("email", email);
							
							mapJson.put("sendState", mapData.get("TZ_FS_ZT").equals("SUC") ? "成功" : "失败");
							
							mapJson.put("sendDate", mapData.get("TZ_FS_DT") == null ? "" : String.valueOf(mapData.get("TZ_FS_DT")));

							listJson.add(mapJson);
						}
						
						Map<String, Object> mapRet = new HashMap<String, Object>();
						mapRet.put("total", total);
						mapRet.put("root", listJson);

						strRet = jacksonUtil.Map2json(mapRet);
					}
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "参数不正确！";
		}

		return strRet;
	}

}
