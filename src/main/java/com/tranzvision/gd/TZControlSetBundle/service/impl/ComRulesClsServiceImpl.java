package com.tranzvision.gd.TZControlSetBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZControlSetBundle.dao.PsTzComJygzpzTMapper;
import com.tranzvision.gd.TZControlSetBundle.model.PsTzComJygzpzT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * @author WRL TZ_KJ_JYGZ_PKG:TZ_KJ_JYGZ_CLS
 * 
 *         控件校验规则
 */
@Service("com.tranzvision.gd.TZControlSetBundle.service.impl.ComRulesClsServiceImpl")
public class ComRulesClsServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private PsTzComJygzpzTMapper psTzComJygzpzTMapper;

	/**
	 * 获取控件规则信息
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
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("kjID")) {
				String kjID = jacksonUtil.getString("kjID");
				String kjJygzID = jacksonUtil.getString("kjJygzID");
				if (StringUtils.isBlank(kjID) || StringUtils.isBlank(kjJygzID)) {
					errMsg[0] = "1";
					errMsg[1] = "参数错误";
				} else {
					String kjName = sqlQuery.queryForObject("SELECT TZ_COM_MC FROM PS_TZ_COM_DY_T WHERE TZ_COM_ID = ?",
							new Object[] { kjID }, "String");
					String kjJygzMx = sqlQuery.queryForObject(
							"SELECT TZ_JYGZ_MC FROM PS_TZ_JYGZ_DY_T WHERE TZ_JYGZ_ID =?", new Object[] { kjJygzID },
							"String");
					String isQy = sqlQuery.queryForObject(
							"SELECT TZ_QY_BZ FROM PS_TZ_COM_JYGZPZ_T WHERE TZ_COM_ID = ? AND TZ_JYGZ_ID = ?",
							new Object[] { kjID, kjJygzID }, "String");
					String tz_order = sqlQuery.queryForObject(
							"SELECT TZ_ORDER FROM PS_TZ_COM_JYGZPZ_T WHERE TZ_COM_ID = ? AND TZ_JYGZ_ID = ?",
							new Object[] { kjID, kjJygzID }, "String");

					Map<String, Object> mapData = new HashMap<String, Object>();

					mapData.put("kjID", kjID);
					mapData.put("kjName", kjName);
					mapData.put("kjJygzID", kjJygzID);
					mapData.put("kjJygzMx", kjJygzMx);
					mapData.put("isQy", isQy);
					mapData.put("tz_order", tz_order);

					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("formData", mapData);

					strRet = jacksonUtil.Map2json(mapRet);
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
	 * 更新控件规则信息
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		if (StringUtils.isEmpty(oprid) || StringUtils.equals("TZ_GUEST", oprid)) {
			errMsg[0] = "1";
			errMsg[1] = "请先登录再操作";
			return strRet;
		}

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String kjID = jacksonUtil.getString("kjID");
				String kjJygzID = jacksonUtil.getString("kjJygzID");
				String isQy = jacksonUtil.getString("isQy");
				String tz_order = jacksonUtil.getString("tz_order");
				if(StringUtils.isBlank(tz_order)){
					tz_order = sqlQuery.queryForObject("SELECT MAX(TZ_ORDER) + 1 FROM PS_TZ_COM_JYGZPZ_T WHERE TZ_COM_ID = ?", new Object[] { kjID }, "String");
					if(StringUtils.isBlank(tz_order)){
						tz_order = "1";
					}
				}

				String isExist = "";
				isExist = sqlQuery.queryForObject(
						"SELECT 'Y' FROM PS_TZ_COM_JYGZPZ_T WHERE TZ_COM_ID = ? AND TZ_JYGZ_ID = ?",
						new Object[] { kjID, kjJygzID }, "String");
				if (StringUtils.equals("Y", isExist)) {
					PsTzComJygzpzT psTzComJygzpzT = new PsTzComJygzpzT();
					psTzComJygzpzT.setTzComId(kjID);
					psTzComJygzpzT.setTzJygzId(kjJygzID);
					psTzComJygzpzT.setTzQyBz(isQy);
					psTzComJygzpzT.setTzOrder(Integer.parseInt(tz_order));
					
					psTzComJygzpzTMapper.updateByPrimaryKeySelective(psTzComJygzpzT);
				} else {
					PsTzComJygzpzT psTzComJygzpzT = new PsTzComJygzpzT();
					psTzComJygzpzT.setTzComId(kjID);
					psTzComJygzpzT.setTzJygzId(kjJygzID);
					psTzComJygzpzT.setTzQyBz(isQy);
					psTzComJygzpzT.setTzOrder(Integer.parseInt(tz_order));
					psTzComJygzpzTMapper.insert(psTzComJygzpzT);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
}
