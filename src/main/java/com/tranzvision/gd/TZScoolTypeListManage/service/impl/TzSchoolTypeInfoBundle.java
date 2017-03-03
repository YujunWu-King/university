package com.tranzvision.gd.TZScoolTypeListManage.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZScoolTypeListManage.dao.PsTzShoolLogTMapper;
import com.tranzvision.gd.TZScoolTypeListManage.model.PsTzShoolLogT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZScoolTypeListManage.service.impl.TzSchoolTypeInfoBundle")
public class TzSchoolTypeInfoBundle extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzShoolLogTMapper PsTzShoolLogTMapper;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		int typeID;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			Date nowdate = new Date();
			System.out.println(actData);
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析 json
				jacksonUtil.json2Map(strForm);

				typeID = getSeqNum.getSeqNum("TZ_SCHOOL_TYPE_TBL", "TZ_SCHOOL_TYPEID");
				String typeName = jacksonUtil.getString("typeName");
				String typedec = jacksonUtil.getString("typedec");
				String typeFlag = jacksonUtil.getString("typeFlag");

				System.out.println(typeName + typedec);

				PsTzShoolLogT PsTzShoolLogT = new PsTzShoolLogT();
				PsTzShoolLogT.setTzSchoolTypeid(String.valueOf(typeID));
				PsTzShoolLogT.setTzSchoolTypename(typeName);
				PsTzShoolLogT.setTzSchoolDec(typedec);
				PsTzShoolLogT.setTzSchoolFlag(typeFlag);
				PsTzShoolLogT.setTzSchoolAddtime(nowdate);

				PsTzShoolLogTMapper.insert(PsTzShoolLogT);

				strRet = String.valueOf(typeID);
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	/* 获取院校库定义信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("typeID")) {
				// 院校库机构ID;
				String typeID = jacksonUtil.getString("typeID");

				PsTzShoolLogT PsTzShoolLogT = PsTzShoolLogTMapper.selectByPrimaryKey(typeID);
				if (PsTzShoolLogT != null) {

					returnJsonMap.put("typeID", PsTzShoolLogT.getTzSchoolTypeid());
					returnJsonMap.put("typeName", PsTzShoolLogT.getTzSchoolTypename());
					returnJsonMap.put("typedec", PsTzShoolLogT.getTzSchoolDec());
					returnJsonMap.put("typeFlag", PsTzShoolLogT.getTzSchoolFlag());
					returnJsonMap.put("typeCreatime", PsTzShoolLogT.getTzSchoolAddtime());

				} else {
					errMsg[0] = "1";
					errMsg[1] = "该院校数据不存在";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "该院校数据不存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		System.out.println(jacksonUtil.Map2json(returnJsonMap));
		return jacksonUtil.Map2json(returnJsonMap);
	}

	@Override
	/* 更新院校库 */
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);

				String typeID = jacksonUtil.getString("typeID");

				String typeName = jacksonUtil.getString("typeName");
				String typedec = jacksonUtil.getString("typedec");
				String typeFlag = jacksonUtil.getString("typeFlag");

				System.out.println(typeName + typedec);

				String sql = "select COUNT(1) from PS_TZ_SCHOOL_TYPE_TBL WHERE TZ_SCHOOL_TYPEID=?";
				int count = jdbcTemplate.queryForObject(sql, new Object[] { typeID }, "Integer");
				if (count > 0) {

					PsTzShoolLogT PsTzShoolLogT = new PsTzShoolLogT();
					PsTzShoolLogT.setTzSchoolTypeid(typeID);
					PsTzShoolLogT.setTzSchoolTypename(typeName);
					PsTzShoolLogT.setTzSchoolDec(typedec);
					PsTzShoolLogT.setTzSchoolFlag(typeFlag);

					PsTzShoolLogTMapper.updateByPrimaryKeySelective(PsTzShoolLogT);

					strRet = String.valueOf(typeID);

				} else {
					errMsg[0] = "1";
					errMsg[1] = "机构ID为：" + typeID + "的院校不存在";

				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

}
