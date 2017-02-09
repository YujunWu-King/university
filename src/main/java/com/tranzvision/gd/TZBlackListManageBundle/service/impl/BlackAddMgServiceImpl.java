package com.tranzvision.gd.TZBlackListManageBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZBlackListManageBundle.service.impl.BlackAddMgServiceImpl")
public class BlackAddMgServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery jdbcTemplate;
	
	/* 新增黑名单用户 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("OPRID", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				if(jacksonUtil.containsKey("OPRID")){
					// 用户账号;
				    String strOprId = jacksonUtil.getString("OPRID");
				    String updatelSFSSql = "UPDATE PS_TZ_REG_USER_T SET TZ_BLACK_NAME='Y' WHERE OPRID=?";
					jdbcTemplate.update(updatelSFSSql, new Object[]{strOprId});
					
				}else{
					errMsg[0] = "1";
					errMsg[1] = "参数错误";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
//		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
}

