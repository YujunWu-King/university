package com.tranzvision.gd.TZEmailSmsSendBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/***
 * 短信发送预览    	原PS：TZ_GD_COM_EMLSMS_APP:smsPreview    
 * @author zhanglang
 * 2017-02-08
 */
@Service("com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SmsPreviewServiceImpl")
public class SmsPreviewServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private emlPreviewServiceImpl emlPreviewServiceImpl;

	
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("jgId") && jacksonUtil.containsKey("tmpId")
					&& jacksonUtil.containsKey("audienceId") && jacksonUtil.containsKey("showNum")
					&& jacksonUtil.containsKey("smsContent")) {

				String jgId = jacksonUtil.getString("jgId");
				String tmpId = jacksonUtil.getString("tmpId");
				String audienceId = jacksonUtil.getString("audienceId");
				int showNum = jacksonUtil.getInt("showNum");
				// 短信内容;
				String smsContent = jacksonUtil.getString("smsContent");
				
				// 收件人id;
				String sjrSQL = "select TZ_AUDCY_ID from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID=? order by cast(TZ_AUDCY_ID as UNSIGNED INTEGER) limit ?,1";
				String audCyId = jdbcTemplate.queryForObject(sjrSQL, new Object[] { audienceId, showNum - 1 },
						"String");
				// 收件人;
				String AddresseeSmsSQL = "select TZ_ZY_SJ from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID=? and TZ_AUDCY_ID=?";
				String AddresseeSms = jdbcTemplate.queryForObject(AddresseeSmsSQL,new Object[] { audienceId, audCyId }, "String");

				// 一共有多少人;
				String totalSQL = "select count(1) from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID=?";
				int audCyrTotal = jdbcTemplate.queryForObject(totalSQL, new Object[] { audienceId }, "Integer");

				// smsContent;
				String ymbSQL = "select TZ_YMB_ID from PS_TZ_EMALTMPL_TBL where TZ_JG_ID=? and TZ_TMPL_ID=?";
				String ymbId = jdbcTemplate.queryForObject(ymbSQL, new Object[] { jgId, tmpId }, "String");
				
				ArrayList<String[]> arrayList = emlPreviewServiceImpl.ayalyMbVar(jgId, ymbId, audienceId, audCyId);
				if (arrayList != null && arrayList.size() > 0) {
					for (int i = 0; i < arrayList.size(); i++) {
						String[] str = arrayList.get(i);
						String name = str[0];
						String value = str[1];
						
						smsContent = smsContent.replace(name, value);
					}
				}

				Map<String, Object> map = new HashMap<>();
				map.put("AddresseeSms", AddresseeSms);
				map.put("smsContent", smsContent);
				map.put("audCyrTotal", audCyrTotal);
				map.put("currentPageNum", showNum);

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

		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
}
