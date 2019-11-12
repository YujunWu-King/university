package com.tranzvision.gd.TZWeakPasswordBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWeakPasswordBundle.dao.PsTzWeakPasswordMapper;
import com.tranzvision.gd.TZWeakPasswordBundle.model.PsTzWeakPassword;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 弱密码管理类
 */
@Service("com.tranzvision.gd.TZWeakPasswordBundle.service.impl.WeakPwdInfoImpl")
public class WeakPwdInfoImpl extends FrameworkImpl {
	
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private PsTzWeakPasswordMapper psTzWeakPasswordMapper;
	
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("tzPwdId")) {
				String tzPwdId = jacksonUtil.getString("tzPwdId");
				if (tzPwdId != null && !"".equals(tzPwdId)) {

					PsTzWeakPassword psTzWeakPassword = psTzWeakPasswordMapper.selectByPrimaryKey(tzPwdId);
					if (psTzWeakPassword != null) {
						Map<String, Object> jsonMap = new HashMap<>();
						jsonMap.put("TZ_PWD_ID", tzPwdId);
						jsonMap.put("TZ_PWD_VAL", psTzWeakPassword.getTzPwdVal());
						returnJsonMap.replace("formData", jsonMap);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "无法获取弱密码信息";
					}

				} else {
					errMsg[0] = "1";
					errMsg[1] = "无法获取弱密码信息";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "无法获取弱密码信息";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				String tzPwdId = (String) infoData.get("TZ_PWD_ID");
				String tzPwdVal = (String) infoData.get("TZ_PWD_VAL");
				if(tzPwdId!=null&&!"".equals(tzPwdId)&&tzPwdVal!=null&&!"".equals(tzPwdVal)){
					// 是否已经存在;
					String existSql = "SELECT 'Y' FROM PS_TZ_WEAK_PASSWORD WHERE TZ_PWD_ID=?";
					String isExist = "";
					isExist = sqlQuery.queryForObject(existSql, new Object[] { tzPwdId }, "String");
					if ("Y".equals(isExist)) {
						errMsg[0] = "1";
						errMsg[1] = "弱密码ID已存在";
						return strRet;
					}
					PsTzWeakPassword psTzWeakPassword = new PsTzWeakPassword();
					psTzWeakPassword.setTzPwdId(tzPwdId);
					psTzWeakPassword.setTzPwdVal(tzPwdVal);
					int i = psTzWeakPasswordMapper.insert(psTzWeakPassword);
					if(i <= 0){
						errMsg[0] = "1";
						errMsg[1] = "保存失败";
					}
				}else{
					errMsg[0] = "1";
					errMsg[1] = "参数不能为空";
					return strRet;
				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
	
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				String tzPwdId = (String) infoData.get("TZ_PWD_ID");
				String tzPwdVal = (String) infoData.get("TZ_PWD_VAL");

				// 是否已经存在;
				String existSql = "SELECT 'Y' FROM PS_TZ_WEAK_PASSWORD WHERE TZ_PWD_ID=?";
				String isExist = "";
				isExist = sqlQuery.queryForObject(existSql, new Object[] { tzPwdId }, "String");

				if (!"Y".equals(isExist)) {
					errMsg[0] = "1";
					errMsg[1] = "弱密码ID为：" + tzPwdId + "的信息不存在。";
					return strRet;
				}

				PsTzWeakPassword psTzWeakPassword = new PsTzWeakPassword();
				psTzWeakPassword.setTzPwdId(tzPwdId);
				psTzWeakPassword.setTzPwdVal(tzPwdVal);
				int i = psTzWeakPasswordMapper.updateByPrimaryKey(psTzWeakPassword);
				if(i <= 0){
					errMsg[0] = "1";
					errMsg[1] = "更新失败";
				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}

}
