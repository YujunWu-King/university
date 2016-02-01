package com.tranzvision.gd.TZAdditionalFieldBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAdditionalFieldBundle.dao.PsTzFormAttrTMapper;
import com.tranzvision.gd.TZAdditionalFieldBundle.model.PsTzFormAttrT;
import com.tranzvision.gd.TZAdditionalFieldBundle.model.PsTzFormAttrTKey;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * @author WRL TZ_GD_F_ATTR_PKG:TZ_GD_ATTRPAGE_CLS 附加字段定义
 *
 */
@Service("com.tranzvision.gd.TZAdditionalFieldBundle.service.impl.AFieldInfoClsServiceImpl")
public class AFieldInfoClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private PsTzFormAttrTMapper psTzFormAttrTMapper;

	/**
	 * 获取附加字段信息
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
				PsTzFormAttrTKey psTzFormAttrTKey = new PsTzFormAttrTKey();
				psTzFormAttrTKey.setTzJgId(orgId);
				psTzFormAttrTKey.setTzAttributeId(attrValue);

				PsTzFormAttrT psTzFormAttrT = psTzFormAttrTMapper.selectByPrimaryKey(psTzFormAttrTKey);

				if (psTzFormAttrT != null) {
					Map<String, Object> mapData = new HashMap<String, Object>();

					String attrSeq = psTzFormAttrT.getTzSortNum() == null ? ""
							: String.valueOf(psTzFormAttrT.getTzSortNum());
					String attrVal = psTzFormAttrT.getTzAttributeId() == null ? ""
							: String.valueOf(psTzFormAttrT.getTzAttributeId());
					String attrDesc = psTzFormAttrT.getTzAttributeName() == null ? ""
							: String.valueOf(psTzFormAttrT.getTzAttributeName());
					String attrType = psTzFormAttrT.getTzControlType() == null ? ""
							: String.valueOf(psTzFormAttrT.getTzControlType());
					String attrTypeDesc = sqlQuery.queryForObject(
							"SELECT IFNULL ((SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_LNG WHERE TZ_ZHZJH_ID = A.TZ_ZHZJH_ID AND TZ_ZHZ_ID = A.TZ_ZHZ_ID AND TZ_LANGUAGE_ID = ?), A.TZ_ZHZ_DMS) TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL A WHERE TZ_ZHZJH_ID = 'TZ_CONTROL_TYPE' AND TZ_ZHZ_ID = ?",
							new Object[] { language, attrType }, "String");
					String attrEnabled = psTzFormAttrT.getTzIsUsed() == null ? ""
							: String.valueOf(psTzFormAttrT.getTzIsUsed());
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
	 * 修改附加字段信息
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
			errMsg[1] = "您不属于任何机构，不能修改附加字段定义！";
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
					attrValue = "" + getSeqNum.getSeqNum("PS_TZ_FORM_ATTR_T", "TZ_ATTRIBUTE_ID");
				}
				String sql = "SELECT COUNT(*) FROM PS_TZ_FORM_ATTR_T WHERE TZ_JG_ID = ? AND TZ_ATTRIBUTE_ID = ?";
				int count = sqlQuery.queryForObject(sql, new Object[] { orgId, attrValue }, "Integer");

				if (count > 0) {
					PsTzFormAttrT psTzFormAttrT = new PsTzFormAttrT();

					psTzFormAttrT.setTzJgId(orgId);
					psTzFormAttrT.setTzAttributeId(attrValue);
					psTzFormAttrT.setTzSortNum(Integer.parseInt(attrSeq));
					psTzFormAttrT.setTzAttributeName(attrDesc);
					psTzFormAttrT.setTzControlType(attrType);
					psTzFormAttrT.setTzIsUsed(attrEnabled);
					psTzFormAttrT.setRowLastmantDttm(dateNow);
					psTzFormAttrT.setRowLastmantOprid(oprid);

					psTzFormAttrTMapper.updateByPrimaryKeySelective(psTzFormAttrT);
				} else {
					PsTzFormAttrT psTzFormAttrT = new PsTzFormAttrT();

					psTzFormAttrT.setTzJgId(orgId);
					psTzFormAttrT.setTzAttributeId(attrValue);
					psTzFormAttrT.setTzSortNum(Integer.parseInt(attrSeq));
					psTzFormAttrT.setTzAttributeName(attrDesc);
					psTzFormAttrT.setTzControlType(attrType);
					psTzFormAttrT.setTzIsUsed(attrEnabled);
					psTzFormAttrT.setRowAddedDttm(dateNow);
					psTzFormAttrT.setRowAddedOprid(oprid);
					psTzFormAttrT.setRowLastmantDttm(dateNow);
					psTzFormAttrT.setRowLastmantOprid(oprid);

					psTzFormAttrTMapper.insert(psTzFormAttrT);
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
