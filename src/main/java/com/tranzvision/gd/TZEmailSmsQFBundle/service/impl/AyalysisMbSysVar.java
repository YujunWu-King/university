package com.tranzvision.gd.TZEmailSmsQFBundle.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.base.AnalysisSysVar;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZEmailSmsQFBundle.service.impl.AyalysisMbSysVar")
public class AyalysisMbSysVar {
	@Autowired
	private SqlQuery jdbcTemplate;
	
	// 解析邮件中的系统变量
	public ArrayList<String[]> ayalyMbVar(String strJgId, String strYmbId, String audId, String audCyId) {
			ArrayList<String[]> arrayList = new ArrayList<String[]>();
			String sql = "select a.TZ_YMB_CSLBM,b.TZ_PARA_ID,b.TZ_PARA_ALIAS,b.TZ_SYSVARID from PS_TZ_TMP_DEFN_TBL a,PS_TZ_TMP_PARA_TBL b where a.TZ_JG_ID=? and a.TZ_YMB_ID=? and a.TZ_JG_ID=b.TZ_JG_ID and a.TZ_YMB_ID=b.TZ_YMB_ID";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { strJgId, strYmbId });
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					String ymbCslbm = map.get("TZ_YMB_CSLBM") == null ? "" : (String) map.get("TZ_YMB_CSLBM");
					String ymbParaId = map.get("TZ_PARA_ID")  == null ? "" : (String) map.get("TZ_PARA_ID");
					String ymbParaAlias = map.get("TZ_PARA_ALIAS")  == null ? "" : (String) map.get("TZ_PARA_ALIAS");
					String sysvarId = map.get("TZ_SYSVARID")  == null ? "" : (String) map.get("TZ_SYSVARID");
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
					String value = obj == null ? "" : (String) obj;
					String[] returnString = { name, value };
					arrayList.add(returnString);
				}
			}
			return arrayList;
		}
}
