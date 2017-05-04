package com.tranzvision.gd.TZEmailSmsSendBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.AnalysisSysVar;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * PS: TZ_GD_COM_EMLSMS_APP:emlPreview
 * 
 * @author tang 预览邮件内容
 */
@Service("com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.emlPreviewServiceImpl")
public class emlPreviewServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;

	/* 邮件预览 */
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
					&& jacksonUtil.containsKey("emailTheme") && jacksonUtil.containsKey("emailContent")) {

				String jgId = jacksonUtil.getString("jgId");
				String tmpId = jacksonUtil.getString("tmpId");
				String audienceId = jacksonUtil.getString("audienceId");
				int showNum = jacksonUtil.getInt("showNum");
				// 邮件主题;
				String emailTheme = jacksonUtil.getString("emailTheme");
				// 邮件内容;
				String emailContent = jacksonUtil.getString("emailContent");
				// 发件人;
				String fjrSQL = "SELECT B.TZ_EML_ADDR100 FROM PS_TZ_EMALTMPL_TBL A left join PS_TZ_EMLS_DEF_TBL B ON A.TZ_EMLSERV_ID = B.TZ_EMLSERV_ID WHERE A.TZ_JG_ID=? AND A.TZ_TMPL_ID=?";
				String senderEmail = jdbcTemplate.queryForObject(fjrSQL, new Object[] { jgId, tmpId }, "String");

				// 收件人id;
				String sjrSQL = "select TZ_AUDCY_ID from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID=? order by cast(TZ_AUDCY_ID as UNSIGNED INTEGER) limit ?,1";
				String audCyId = jdbcTemplate.queryForObject(sjrSQL, new Object[] { audienceId, showNum - 1 },
						"String");
				// 收件人;
				String AddresseeEmailSQL = "select TZ_ZY_EMAIL from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID=? and TZ_AUDCY_ID=?";
				String AddresseeEmail = jdbcTemplate.queryForObject(AddresseeEmailSQL,
						new Object[] { audienceId, audCyId }, "String");

				// 一共有多少人;
				String totalSQL = "select count(1) from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID=?";
				int audCyrTotal = jdbcTemplate.queryForObject(totalSQL, new Object[] { audienceId }, "Integer");

				// emailContent;
				String ymbSQL = "select TZ_YMB_ID from PS_TZ_EMALTMPL_TBL where TZ_JG_ID=? and TZ_TMPL_ID=?";
				String ymbId = jdbcTemplate.queryForObject(ymbSQL, new Object[] { jgId, tmpId }, "String");
				ArrayList<String[]> arrayList = this.ayalyMbVar(jgId, ymbId, audienceId, audCyId);
				if (arrayList != null && arrayList.size() > 0) {
					for (int i = 0; i < arrayList.size(); i++) {
						String[] str = arrayList.get(i);
						String name = str[0];
						String value = str[1];
						
						emailContent = emailContent.replace(name, value);
					}
				}

				Map<String, Object> map = new HashMap<>();
				map.put("senderEmail", senderEmail);
				map.put("AddresseeEmail", AddresseeEmail);
				map.put("emailTheme", emailTheme);
				map.put("emailContent", emailContent);
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

	// 解析邮件中的系统变量
	public ArrayList<String[]> ayalyMbVar(String jgId, String ymbId, String audId, String audCyId) {
		
		ArrayList<String[]> arrayList = new ArrayList<String[]>();
		try{
			String sql = "select a.TZ_YMB_CSLBM,b.TZ_PARA_ID,b.TZ_PARA_ALIAS,b.TZ_SYSVARID from PS_TZ_TMP_DEFN_TBL a,PS_TZ_TMP_PARA_TBL b where a.TZ_JG_ID=? and a.TZ_YMB_ID=? and a.TZ_JG_ID=b.TZ_JG_ID and a.TZ_YMB_ID=b.TZ_YMB_ID";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { jgId, ymbId });
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					String ymbCslbm =  map.get("TZ_YMB_CSLBM") == null ? "" : (String)map.get("TZ_YMB_CSLBM");
					String ymbParaId = map.get("TZ_PARA_ID") == null ? "" : (String) map.get("TZ_PARA_ID");
					String ymbParaAlias = map.get("TZ_PARA_ALIAS") == null ? "" : (String) map.get("TZ_PARA_ALIAS");
					String sysvarId = map.get("TZ_SYSVARID") == null ? "" : (String) map.get("TZ_SYSVARID");
					String[] sysVarParam = { audId, audCyId };
					AnalysisSysVar analysisSysVar = new AnalysisSysVar();
					analysisSysVar.setM_SysVarID(sysvarId);
					analysisSysVar.setM_SysVarParam(sysVarParam);
					Object obj = analysisSysVar.GetVarValue();
					
					/*
					ymbCslbm = ymbCslbm.replaceAll("\\(", "\\\\("); 
				   	ymbCslbm = ymbCslbm.replaceAll("\\)", "\\\\)"); 
				   	
				   	ymbParaId = ymbParaId.replaceAll("\\(", "\\\\("); 
				   	ymbParaId = ymbParaId.replaceAll("\\)", "\\\\)"); 
				   	   
				   	ymbParaAlias = ymbParaAlias.replaceAll("\\(", "\\\\("); 
				   	ymbParaAlias = ymbParaAlias.replaceAll("\\)", "\\\\)"); 
				   	
					String name = "\\[" + ymbCslbm + "\\." + ymbParaId + "\\." + ymbParaAlias + "\\]";
					*/
					String name = "[" + ymbCslbm + "." + ymbParaId + "." + ymbParaAlias + "]";
					String value = obj == null ? "" : (String)obj;
					
					String[] returnString = { name, value };
					arrayList.add(returnString);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return arrayList;
	}
}
