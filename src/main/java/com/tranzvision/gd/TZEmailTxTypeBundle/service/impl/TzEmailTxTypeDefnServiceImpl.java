package com.tranzvision.gd.TZEmailTxTypeBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailTxTypeBundle.dao.PsTzTxLbtjgxTMapper;
import com.tranzvision.gd.TZEmailTxTypeBundle.dao.PsTzTxTypeTblMapper;
import com.tranzvision.gd.TZEmailTxTypeBundle.model.PsTzTxLbtjgxTKey;
import com.tranzvision.gd.TZEmailTxTypeBundle.model.PsTzTxTypeTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZEmailTxTypeBundle.service.impl.TzEmailTxTypeDefnServiceImpl")
public class TzEmailTxTypeDefnServiceImpl extends FrameworkImpl{
	
	@Autowired
	private PsTzTxTypeTblMapper psTzTxTypeTblMapper;
	
	@Autowired
	private PsTzTxLbtjgxTMapper psTzTxLbtjgxTMapper;

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
			if (jacksonUtil.containsKey("txTypeId")) {
				//txTypeId;
				String txTypeId = jacksonUtil.getString("txTypeId");
				
				PsTzTxTypeTbl psTzTxTypeTbl = psTzTxTypeTblMapper.selectByPrimaryKey(txTypeId);

				if(psTzTxTypeTbl != null){
					returnJsonMap.put("txTypeId", txTypeId);
					returnJsonMap.put("txTypeName", psTzTxTypeTbl.getTzTxTypeDesc());
					returnJsonMap.put("txType", psTzTxTypeTbl.getTzTxSslx());
					returnJsonMap.put("isValid", psTzTxTypeTbl.getTzIsUsed());
					returnJsonMap.put("txTypeDesc", psTzTxTypeTbl.getTzTxDesc());
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
		returnJsonMap.put("txTypeId", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				String type = jacksonUtil.getString("type");
				
				if("FORMINFO".equals(type)){
					Map<String,Object> dataMap = jacksonUtil.getMap("data");
					
					// 退信类别ID;
					String txTypeId = (String) dataMap.get("txTypeId");
					// 退信类别名称;
					String txTypeName = (String) dataMap.get("txTypeName");
					// 退信类型;
					String txType = (String) dataMap.get("txType");
					// 有效;
					String isValid = (String) dataMap.get("isValid");
					//描述
					String txTypeDesc = (String) dataMap.get("txTypeDesc");
					
					
					// 检查是否重复;
					int rptNum = 0;
					String rptNumSQL = "SELECT COUNT(1) FROM PS_TZ_TX_TYPE_TBL WHERE TZ_TX_TYPE_ID = ?";
					rptNum = sqlQuery.queryForObject(rptNumSQL, new Object[] { txTypeId }, "Integer");
					if (rptNum > 0) {
						errMsg[0] = "1";
						errMsg[1] = "退信类别ID：" + txTypeId + "已经存在，请勿重复定义。";
					} else {
						PsTzTxTypeTbl psTzTxTypeTbl = new PsTzTxTypeTbl();
						psTzTxTypeTbl.setTzTxTypeId(txTypeId);
						psTzTxTypeTbl.setTzTxTypeDesc(txTypeName);
						psTzTxTypeTbl.setTzTxSslx(txType);
						psTzTxTypeTbl.setTzIsUsed(isValid);
						psTzTxTypeTbl.setTzTxDesc(txTypeDesc);

						int rtn = psTzTxTypeTblMapper.insert(psTzTxTypeTbl);
						
						if (rtn > 0) {
							returnJsonMap.replace("txTypeId", txTypeId);
						} else {
							errMsg[0] = "1";
							errMsg[1] = "退信类别定义保存失败";
						}
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

	
	@SuppressWarnings("unchecked")
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("txTypeId", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				String type = jacksonUtil.getString("type");
				
				if("FORMINFO".equals(type)){
					Map<String,Object> dataMap = jacksonUtil.getMap("data");
					
					// 退信类别ID;
					String txTypeId = (String) dataMap.get("txTypeId");
					// 退信类别名称;
					String txTypeName = (String) dataMap.get("txTypeName");
					// 退信类型;
					String txType = (String) dataMap.get("txType");
					// 有效;
					String isValid = (String) dataMap.get("isValid");
					//描述
					String txTypeDesc = (String) dataMap.get("txTypeDesc");
					
					
					PsTzTxTypeTbl psTzTxTypeTbl = psTzTxTypeTblMapper.selectByPrimaryKey(txTypeId);
					if(psTzTxTypeTbl != null){
						psTzTxTypeTbl.setTzTxTypeDesc(txTypeName);
						psTzTxTypeTbl.setTzTxSslx(txType);
						psTzTxTypeTbl.setTzIsUsed(isValid);
						psTzTxTypeTbl.setTzTxDesc(txTypeDesc);

						int rtn = psTzTxTypeTblMapper.updateByPrimaryKey(psTzTxTypeTbl);
						
						if (rtn > 0) {
							returnJsonMap.replace("txTypeId", txTypeId);
						} else {
							errMsg[0] = "1";
							errMsg[1] = "退信类别定义保存失败";
						}
					}
				}
				
				if("GRIDINFO".equals(type)){
					List<Map<String,Object>> gridList = (List<Map<String, Object>>) jacksonUtil.getList("data");
					if(gridList != null){
						for(Map<String,Object> gMap: gridList){
							String txTypeId = (String) gMap.get("txTypeId");
							String txRuleId = (String) gMap.get("txRuleId");
							
							PsTzTxLbtjgxTKey psTzTxLbtjgxTKey = new PsTzTxLbtjgxTKey(); 
							psTzTxLbtjgxTKey.setTzTxTypeId(txTypeId);
							psTzTxLbtjgxTKey.setTzTxRuleId(txRuleId);
							
							psTzTxLbtjgxTMapper.insert(psTzTxLbtjgxTKey);
						}
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
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("result", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				String type = jacksonUtil.getString("type");

				if("GRIDINFO".equals(type)){
					List<Map<String,Object>> gridList = (List<Map<String, Object>>) jacksonUtil.getList("data");
					if(gridList != null){
						for(Map<String,Object> gMap: gridList){
							String txTypeId = (String) gMap.get("txTypeId");
							String txRuleId = (String) gMap.get("txRuleId");
							
							PsTzTxLbtjgxTKey psTzTxLbtjgxTKey = new PsTzTxLbtjgxTKey(); 
							psTzTxLbtjgxTKey.setTzTxTypeId(txTypeId);
							psTzTxLbtjgxTKey.setTzTxRuleId(txRuleId);
							
							psTzTxLbtjgxTMapper.deleteByPrimaryKey(psTzTxLbtjgxTKey);
						}
					}
				}
			}
			returnJsonMap.replace("result", "success");
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = "系统错误："+e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	
	
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", new ArrayList<Map<String, Object>>());
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String txTypeId = jacksonUtil.getString("txTypeId");
			
			if(null != txTypeId && !"".equals(txTypeId)){
				//查询面试安排总数
				String sql = "SELECT COUNT(1) FROM PS_TZ_TX_LBTJGX_T WHERE TZ_TX_TYPE_ID=?";
				int total = sqlQuery.queryForObject(sql, new Object[]{txTypeId}, "int");

				ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
				if (total > 0) {
					sql = "SELECT A.TZ_TX_RULE_ID,B.TZ_RULE_DESC FROM PS_TZ_TX_LBTJGX_T A left join PS_TZ_TX_RULE_TBL B on(A.TZ_TX_RULE_ID=B.TZ_TX_RULE_ID) where A.TZ_TX_TYPE_ID=?";
					List<Map<String, Object>> listData = sqlQuery.queryForList(sql, new Object[]{txTypeId});

					for(Map<String,Object> mapData : listData){
						Map<String,Object> mapJson = new HashMap<String,Object>();
						
						String txRuleId = mapData.get("TZ_TX_RULE_ID").toString();
						String txRuleName = mapData.get("TZ_RULE_DESC") == null ? "" : mapData.get("TZ_RULE_DESC").toString();

						mapJson.put("txTypeId", txTypeId);
						mapJson.put("txRuleId", txRuleId);
						mapJson.put("txRuleName", txRuleName);
						
						listJson.add(mapJson);
					}
					mapRet.replace("total", total);
					mapRet.replace("root", listJson);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jacksonUtil.Map2json(mapRet);
	}
	
}
