package com.tranzvision.gd.TZClassIndividualizationBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZClassIndividualizationBundle.dao.PsTzClsAttrTMapper;
import com.tranzvision.gd.TZClassIndividualizationBundle.model.PsTzClsAttrT;
import com.tranzvision.gd.TZClassIndividualizationBundle.model.PsTzClsAttrTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;


/**
 * @author WRL TZ_GD_CLSATTR_PKG:TZ_GD_ATTRPAGE_CLS
 * 班级个性化属性定义
 *
 */
@Service("com.tranzvision.gd.TZClassIndividualizationBundle.service.impl.ClassAttrInfoClsServiceImpl")
public class ClassAttrInfoClsServiceImpl extends FrameworkImpl {
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
	 * 获取班级属性信息
	 * 
	 * @param strParams
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String language = tzLoginServiceImpl.getSysLanaguageCD(request);
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("attrValue")) {

				String attrValue = jacksonUtil.getString("attrValue");
				PsTzClsAttrTKey psTzClsAttrTKey = new PsTzClsAttrTKey();
				psTzClsAttrTKey.setTzJgId(orgId);
				psTzClsAttrTKey.setTzAttributeId(attrValue);

				PsTzClsAttrT psTzClsAttrT = psTzClsAttrTMapper.selectByPrimaryKey(psTzClsAttrTKey);

				if (psTzClsAttrT != null) {
					Map<String, Object> mapData = new HashMap<String, Object>();

					String attrSeq = psTzClsAttrT.getTzSortNum() == null ? ""
							: String.valueOf(psTzClsAttrT.getTzSortNum());
					String attrVal = psTzClsAttrT.getTzAttributeId() == null ? ""
							: String.valueOf(psTzClsAttrT.getTzAttributeId());
					String attrDesc = psTzClsAttrT.getTzAttributeName() == null ? ""
							: String.valueOf(psTzClsAttrT.getTzAttributeName());
					String attrType = psTzClsAttrT.getTzControlType() == null ? ""
							: String.valueOf(psTzClsAttrT.getTzControlType());
					String attrTypeDesc = sqlQuery.queryForObject(
							"SELECT IFNULL ((SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_LNG WHERE TZ_ZHZJH_ID = A.TZ_ZHZJH_ID AND TZ_ZHZ_ID = A.TZ_ZHZ_ID AND TZ_LANGUAGE_ID = ?), A.TZ_ZHZ_DMS) TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL A WHERE TZ_ZHZJH_ID = 'TZ_CONTROL_TYPE' AND TZ_ZHZ_ID = ?",
							new Object[] { language, attrType }, "String");
					String attrEnabled = psTzClsAttrT.getTzIsUsed() == null ? ""
							: String.valueOf(psTzClsAttrT.getTzIsUsed());
					String attrEnabledDesc = sqlQuery.queryForObject(
							"SELECT IFNULL ((SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_LNG WHERE TZ_ZHZJH_ID = A.TZ_ZHZJH_ID AND TZ_ZHZ_ID = A.TZ_ZHZ_ID AND TZ_LANGUAGE_ID = ?), A.TZ_ZHZ_DMS) TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL A WHERE TZ_ZHZJH_ID = 'TZ_ATTR_ENABLED' AND TZ_ZHZ_ID = ?",
							new Object[] { language, attrEnabled }, "String");

					mapData.put("attrSeq", attrSeq);
					mapData.put("attrValue", attrVal);
					mapData.put("attrDesc", attrDesc);
					mapData.put("attrType", attrType);
					mapData.put("attrTypeDesc", attrTypeDesc);
					mapData.put("attrEnabled", attrEnabled);
					mapData.put("attrEnabledDesc", attrEnabledDesc);

					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("formData", mapData);

					strRet = jacksonUtil.Map2json(mapRet);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该字段数据不存在";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "参数错误";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/**
	 * 修改班级属性信息
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {

		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();

		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		if (StringUtils.isBlank(orgId)) {
			errMsg[0] = "1";
			errMsg[1] = "您不属于任何机构，不能修改班级属性定义！";
			return strRet;
		}
		try {
			int dataLength = actData.length;
			Date dateNow = new Date();

			for (int num = 0; num < dataLength; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String attrValue = jacksonUtil.getString("attrValue");
				String attrSeq = jacksonUtil.getString("attrSeq");
				String attrDesc = jacksonUtil.getString("attrDesc");
				String attrType = jacksonUtil.getString("attrType");
				String attrEnabled = jacksonUtil.getString("attrEnabled");

				if (StringUtils.equals(attrValue, "NEXT") || StringUtils.isBlank(attrValue)) {
					attrValue = "" + getSeqNum.getSeqNum("TZ_CLS_ATTR_T", "TZ_ATTRIBUTE_ID");
				}
				String sql = "SELECT COUNT(*) FROM PS_TZ_CLS_ATTR_T WHERE TZ_JG_ID = ? AND TZ_ATTRIBUTE_ID = ?";
				int count = sqlQuery.queryForObject(sql, new Object[] { orgId, attrValue }, "Integer");

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
				} else {
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
				Map<String, Object> mapRet = new HashMap<String, Object>();
				mapRet.put("attrValue", attrValue);

				strRet = jacksonUtil.Map2json(mapRet);
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
}
