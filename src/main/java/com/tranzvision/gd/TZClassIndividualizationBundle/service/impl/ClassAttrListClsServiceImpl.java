package com.tranzvision.gd.TZClassIndividualizationBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZClassIndividualizationBundle.dao.PsTzClsAttrTMapper;
import com.tranzvision.gd.TZClassIndividualizationBundle.model.PsTzClsAttrT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * @author WRL TZ_GD_CLSATTR_PKG:TZ_GD_CLSATTR_CLS
 * 班级个性化属性管理
 *
 */
@Service("com.tranzvision.gd.TZClassIndividualizationBundle.service.impl.ClassAttrListClsServiceImpl")
public class ClassAttrListClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private PsTzClsAttrTMapper psTzClsAttrTMapper;
	/**
	 * 班级属性列表显示
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
	
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String language = tzLoginServiceImpl.getSysLanaguageCD(request);

		JacksonUtil jacksonUtil = new JacksonUtil();
		
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		try {
			
			//列表SQL
			String sql = "";
			//查询结果
			List<?> resultlist = null;
			
			if(numLimit == 0){
				sql = "SELECT TZ_ATTRIBUTE_ID,TZ_ATTRIBUTE_NAME,TZ_SORT_NUM,TZ_CONTROL_TYPE,TZ_IS_USED FROM PS_TZ_CLS_ATTR_T WHERE TZ_JG_ID = ? ORDER BY TZ_SORT_NUM";
				resultlist = sqlQuery.queryForList(sql, new Object[] { orgId });
			}else{
				sql = "SELECT TZ_ATTRIBUTE_ID,TZ_ATTRIBUTE_NAME,TZ_SORT_NUM,TZ_CONTROL_TYPE,TZ_IS_USED FROM PS_TZ_CLS_ATTR_T WHERE TZ_JG_ID = ? ORDER BY TZ_SORT_NUM LIMIT ?,?";
				resultlist = sqlQuery.queryForList(sql, new Object[] { orgId, numStart, numStart + numLimit });
			}
			
			for (Object obj : resultlist) {
				Map<String, Object> result = (Map<String, Object>) obj;

				String attrSeq = result.get("TZ_SORT_NUM") == null ? "" : String.valueOf(result.get("TZ_SORT_NUM"));
				String attrValue = result.get("TZ_ATTRIBUTE_ID") == null ? "" : String.valueOf(result.get("TZ_ATTRIBUTE_ID"));
				String attrDesc = result.get("TZ_ATTRIBUTE_NAME") == null ? "" : String.valueOf(result.get("TZ_ATTRIBUTE_NAME"));
				
				String attrType = result.get("TZ_CONTROL_TYPE") == null ? "" : String.valueOf(result.get("TZ_CONTROL_TYPE"));
				String attrTypeDesc = sqlQuery.queryForObject("SELECT IFNULL ((SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_LNG WHERE TZ_ZHZJH_ID = A.TZ_ZHZJH_ID AND TZ_ZHZ_ID = A.TZ_ZHZ_ID AND TZ_LANGUAGE_ID = ?), A.TZ_ZHZ_DMS) TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL A WHERE TZ_ZHZJH_ID = 'TZ_CONTROL_TYPE' AND TZ_ZHZ_ID = ?", new Object[] { language,attrType },"String");

				String attrEnabled = result.get("TZ_IS_USED") == null ? "" : String.valueOf(result.get("TZ_IS_USED"));
				String attrEnabledDesc = sqlQuery.queryForObject("SELECT IFNULL ((SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_LNG WHERE TZ_ZHZJH_ID = A.TZ_ZHZJH_ID AND TZ_ZHZ_ID = A.TZ_ZHZ_ID AND TZ_LANGUAGE_ID = ?), A.TZ_ZHZ_DMS) TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL A WHERE TZ_ZHZJH_ID = 'TZ_ATTR_ENABLED' AND TZ_ZHZ_ID = ?", new Object[] { language,attrEnabled },"String");

				Map<String, Object> mapRetJson = new HashMap<String, Object>();

				mapRetJson.put("attrSeq",attrSeq);
				mapRetJson.put("attrValue",attrValue);
				mapRetJson.put("attrDesc",attrDesc);
				mapRetJson.put("attrType",attrType);
				mapRetJson.put("attrTypeDesc",attrTypeDesc);
				mapRetJson.put("attrEnabled",attrEnabled);
				mapRetJson.put("attrEnabledDesc",attrEnabledDesc);
				
				listData.add(mapRetJson);
			}
		
			mapRet.replace("total", resultlist.size());
			mapRet.replace("root", listData);
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "数据异常，请重试！";
		}

		return jacksonUtil.Map2json(mapRet);

	}
	
	
	/**
	 * 新增或修改班级属性信息
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		
		if(StringUtils.isBlank(orgId)){
			errMsg[0] = "1";
			errMsg[1] = "您当前没有机构，不能添加班级属性！";
			return strRet;
		}
		
		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			Date dateNow = new Date();
			int dataLength = actData.length;

			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				Map<String, Object> infoData = jacksonUtil.getMap("data");
				
				String attrValue = (String)infoData.get("attrValue");
				String attrSeq = "" + infoData.get("attrSeq");
				if(StringUtils.isBlank(attrSeq)){
					attrSeq = "1";
				}
				String attrDesc = (String)infoData.get("attrDesc");
				String attrType = (String)infoData.get("attrType");
				String attrEnabled = (String)infoData.get("attrEnabled");
		
				if(StringUtils.equals(attrValue, "NEXT")){
					attrValue = "" + getSeqNum.getSeqNum("TZ_CLS_ATTR_T", "TZ_ATTRIBUTE_ID");
				}
				
				String sql = "SELECT COUNT(*) FROM PS_TZ_CLS_ATTR_T WHERE TZ_JG_ID = ? AND TZ_ATTRIBUTE_ID = ?";
				int count = sqlQuery.queryForObject(sql, new Object[] { orgId,attrValue }, "Integer");
				
				if (count > 0) {
					PsTzClsAttrT psTzClsAttrT = new PsTzClsAttrT();
					
					psTzClsAttrT.setTzJgId(orgId);
					psTzClsAttrT.setTzAttributeId(attrValue);
					psTzClsAttrT.setTzSortNum(Integer.parseInt(attrSeq));
					psTzClsAttrT.setTzAttributeName(attrDesc);
					psTzClsAttrT.setTzControlType(attrType);
					psTzClsAttrT.setTzIsUsed(attrEnabled);
					psTzClsAttrT.setRowLastmantDttm(dateNow);
					psTzClsAttrT.setRowLastmantOprid(oprid);
					
					psTzClsAttrTMapper.updateByPrimaryKeySelective(psTzClsAttrT);
				}else{
					PsTzClsAttrT psTzClsAttrT = new PsTzClsAttrT();
					
					psTzClsAttrT.setTzJgId(orgId);
					psTzClsAttrT.setTzAttributeId(attrValue);
					psTzClsAttrT.setTzSortNum(Integer.parseInt(attrSeq));
					psTzClsAttrT.setTzAttributeName(attrDesc);
					psTzClsAttrT.setTzControlType(attrType);
					psTzClsAttrT.setTzIsUsed(attrEnabled);
					psTzClsAttrT.setRowAddedDttm(dateNow);
					psTzClsAttrT.setRowAddedOprid(oprid);
					psTzClsAttrT.setRowLastmantDttm(dateNow);
					psTzClsAttrT.setRowLastmantOprid(oprid);
					
					psTzClsAttrTMapper.insert(psTzClsAttrT);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	/***
	 * 删除班级属性信息
	 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 提交信息
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 班级属性id
				String attrValue = jacksonUtil.getString("attrValue");
				
				if(StringUtils.isNotBlank(attrValue) && !StringUtils.equals(attrValue, "NEXT")){
					sqlQuery.update("DELETE FROM PS_TZ_CLS_ATTR_T WHERE TZ_JG_ID = ? AND TZ_ATTRIBUTE_ID = ?", new Object[] {orgId,attrValue});
					sqlQuery.update("DELETE FROM PS_TZ_C_ATTR_OPT_T WHERE TZ_JG_ID = ? AND TZ_ATTRIBUTE_ID = ?", new Object[] {orgId,attrValue});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
}
