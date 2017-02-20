package com.tranzvision.gd.TZZsCreOrorganizationBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZZsCreOrorganizationBundle.dao.PsTzZsJGTBLMapper;
import com.tranzvision.gd.TZZsCreOrorganizationBundle.model.PsTzZsJGTBL;
import com.tranzvision.gd.TZZsCreOrorganizationBundle.model.PsTzZsJGTBLKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZZsCreOrorganizationBundle.service.impl.tzZsOrganizationInfoMg")
public class tzZsOrganizationInfoMg extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzZsJGTBLMapper PsTzZsJGTBLMapper;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = " ";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			Date nowdate = new Date();
			System.out.println(actData);
			String OrgID = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String Opid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			System.out.println(OrgID + Opid);
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析 json
				jacksonUtil.json2Map(strForm);

				int zhjgID = getSeqNum.getSeqNum("TZ_JG_LINKEDIN_TBL", "TZ_CERT_JG_ID");
				String zhjgName = jacksonUtil.getString("zhjgName");
				String zhjgLinkedIn = jacksonUtil.getString("zhjgLinkedIn");

				PsTzZsJGTBL PsTzZsJGTBL = new PsTzZsJGTBL();
				PsTzZsJGTBL.setTzJgId(OrgID);
				PsTzZsJGTBL.setTzCertJgId("ED_" + String.valueOf(zhjgID));
				PsTzZsJGTBL.setTzCertJgName(zhjgName);
				PsTzZsJGTBL.setTzLinkedinEduId(zhjgLinkedIn);
				PsTzZsJGTBL.setRowAddedDttm(nowdate);
				PsTzZsJGTBL.setRowAddedOprid(Opid);
				PsTzZsJGTBL.setTzUseFlag("Y");
				PsTzZsJGTBL.setRowLastmantDttm(nowdate);
				PsTzZsJGTBL.setRowLastmantOprid(Opid);

				PsTzZsJGTBLMapper.insert(PsTzZsJGTBL);

				strRet = "ED_" + String.valueOf(zhjgID);

				returnJsonMap.put("zhjgID", strRet);

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return jacksonUtil.Map2json(returnJsonMap);
	}

	/* 获取院校库定义信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			String OrgID = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("zhjgID")) {
				// 院校库机构ID;
				String zhjgID = jacksonUtil.getString("zhjgID");

				System.out.println(zhjgID);

				PsTzZsJGTBLKey PsTzZsJGTBLKey = new PsTzZsJGTBLKey();
				PsTzZsJGTBLKey.setTzCertJgId(zhjgID);
				PsTzZsJGTBLKey.setTzJgId(OrgID);

				PsTzZsJGTBL PsTzZsJGTBL = PsTzZsJGTBLMapper.selectByPrimaryKey(PsTzZsJGTBLKey);

				if (PsTzZsJGTBL != null && PsTzZsJGTBL.getTzUseFlag().equals("Y")) {
					// String zhjgID=PsTzZsJGTBL.getTzCertJgId();
					String zhjgName = PsTzZsJGTBL.getTzCertJgName();
					System.out.println(zhjgName);
					String zhjgLinkedIn = PsTzZsJGTBL.getTzLinkedinEduId();

					returnJsonMap.put("zhjgID", zhjgID);
					returnJsonMap.put("zhjgName", zhjgName);
					returnJsonMap.put("zhjgLinkedIn", zhjgLinkedIn);

				} else {
					errMsg[0] = "1";
					errMsg[1] = "该校数据不存在";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "该校数据不存在";
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
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			Date nowdate = new Date();
			System.out.println(actData);
			String OrgID = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String Opid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				String zhjgID = jacksonUtil.getString("zhjgID");
				String zhjgName = jacksonUtil.getString("zhjgName");
				String zhjgLinkedIn = jacksonUtil.getString("zhjgLinkedIn");

				String sql = "select COUNT(1) from PS_TZ_JG_LINKEDIN_TBL WHERE TZ_CERT_JG_ID=?";
				int count = jdbcTemplate.queryForObject(sql, new Object[] { zhjgID }, "Integer");
				if (count > 0) {

					PsTzZsJGTBL PsTzZsJGTBL = new PsTzZsJGTBL();
					/*
					 * PsTzZsJGTBL.setTzJgId(OrgID); PsTzZsJGTBL.set
					 */
					PsTzZsJGTBL.setTzCertJgId(String.valueOf(zhjgID));
					PsTzZsJGTBL.setTzCertJgName(zhjgName);
					PsTzZsJGTBL.setTzLinkedinEduId(zhjgLinkedIn);
					PsTzZsJGTBL.setRowLastmantDttm(nowdate);
					PsTzZsJGTBL.setRowLastmantOprid(Opid);
					PsTzZsJGTBL.setTzJgId(OrgID);
					PsTzZsJGTBLMapper.updateByPrimaryKeySelective(PsTzZsJGTBL);

					strRet = String.valueOf(zhjgID);
					returnJsonMap.put("zhjgID", strRet);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "机构ID为：" + zhjgID + "的证书颁发机构不存在";

				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}

}
