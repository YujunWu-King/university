package com.tranzvision.gd.TZEmailTxTypeBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailTxTypeBundle.dao.PsTzTxRuleTblMapper;
import com.tranzvision.gd.TZEmailTxTypeBundle.model.PsTzTxRuleTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZEmailTxTypeBundle.service.impl.TzEmailTxRuleDefnServiceImpl")
public class TzEmailTxRuleDefnServiceImpl extends FrameworkImpl{

	@Autowired
	private PsTzTxRuleTblMapper psTzTxRuleTblMapper;
	
	@Autowired
	private SqlQuery sqlQuery;
	
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("ruleId")) {
				//ruleId;
				String ruleId = jacksonUtil.getString("ruleId");
				
				PsTzTxRuleTbl psTzTxRuleTbl = psTzTxRuleTblMapper.selectByPrimaryKey(ruleId);

				if(psTzTxRuleTbl != null){
					returnJsonMap.put("ruleId", ruleId);
					returnJsonMap.put("ruleName", psTzTxRuleTbl.getTzRuleDesc());
					returnJsonMap.put("compareType", psTzTxRuleTbl.getTzTxPplx());
					returnJsonMap.put("status", psTzTxRuleTbl.getTzIsUsed());
					returnJsonMap.put("keyword", psTzTxRuleTbl.getTzTxitemKey());
				}
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
	
	
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("ruleId", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				// 退信条件ID;
				String ruleId = jacksonUtil.getString("ruleId");
				// 退信条件名称;
				String ruleName = jacksonUtil.getString("ruleName");
				// 匹配类型;
				String compareType = jacksonUtil.getString("compareType");
				// 有效状态;
				String status = jacksonUtil.getString("status");
				// 匹配关键字;
				String keyword = jacksonUtil.getString("keyword");

				// 检查邮箱定义是否重复;
				int rptNum = 0;
				String rptNumSQL = "SELECT COUNT(1) FROM PS_TZ_TX_RULE_TBL WHERE TZ_TX_RULE_ID = ?";
				rptNum = sqlQuery.queryForObject(rptNumSQL, new Object[] { ruleId }, "Integer");
				if (rptNum > 0) {
					errMsg[0] = "1";
					errMsg[1] = "退信条件ID：" + ruleId + "已经存在，请勿重复定义。";
				} else {
					PsTzTxRuleTbl psTzTxRuleTbl = new PsTzTxRuleTbl();
					psTzTxRuleTbl.setTzTxRuleId(ruleId);
					psTzTxRuleTbl.setTzRuleDesc(ruleName);
					psTzTxRuleTbl.setTzTxPplx(compareType);
					psTzTxRuleTbl.setTzIsUsed(status);
					psTzTxRuleTbl.setTzTxitemKey(keyword);
					int rtn = psTzTxRuleTblMapper.insert(psTzTxRuleTbl);
					
					if (rtn > 0) {
						returnJsonMap.replace("ruleId", ruleId);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "退信条件定义保存失败";
					}
				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = "系统错误："+e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("ruleId", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				// 退信条件ID;
				String ruleId = jacksonUtil.getString("ruleId");
				// 退信条件名称;
				String ruleName = jacksonUtil.getString("ruleName");
				// 匹配类型;
				String compareType = jacksonUtil.getString("compareType");
				// 有效状态;
				String status = jacksonUtil.getString("status");
				// 匹配关键字;
				String keyword = jacksonUtil.getString("keyword");

				PsTzTxRuleTbl psTzTxRuleTbl = psTzTxRuleTblMapper.selectByPrimaryKey(ruleId);
				if(psTzTxRuleTbl != null){
					psTzTxRuleTbl.setTzRuleDesc(ruleName);
					psTzTxRuleTbl.setTzTxPplx(compareType);
					psTzTxRuleTbl.setTzIsUsed(status);
					psTzTxRuleTbl.setTzTxitemKey(keyword);
					int rtn = psTzTxRuleTblMapper.updateByPrimaryKey(psTzTxRuleTbl);
					
					if (rtn > 0) {
						returnJsonMap.replace("ruleId", ruleId);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "退信条件定义保存失败";
					}
				}else{
					errMsg[0] = "1";
					errMsg[1] = "退信定义不存在";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = "系统错误："+e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
}
