package com.tranzvision.gd.TZAutoScoringRulerBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZAutoScoringRulerBundle.dao.PsTzZdCsDfGzTblMapper;
import com.tranzvision.gd.TZAutoScoringRulerBundle.model.PsTzZdCsDfGzTbl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZAutoScoringRulerBundle.service.impl.autoScoringRulerInfoImpl")
public class autoScoringRulerInfoImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzZdCsDfGzTblMapper PsTzZdCsDfGzTblMapper;
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

				int zhjgID = getSeqNum.getSeqNum("PS_TZ_ZDCS_DFGZ_T", "TZ_ZDCSGZ_ID");
				String zhjgName = jacksonUtil.getString("zhjgName");
				String zhjgLinkedIn = jacksonUtil.getString("zhjgLinkedIn");

				PsTzZdCsDfGzTbl PsTzZdCsDfGzTbl = new PsTzZdCsDfGzTbl();
				PsTzZdCsDfGzTbl.setTzZdcsgzId(String.valueOf(zhjgID));
				PsTzZdCsDfGzTbl.setTzZdcsgzName(zhjgName);
				PsTzZdCsDfGzTbl.setTzZdcsgz(zhjgLinkedIn);

				PsTzZdCsDfGzTblMapper.insert(PsTzZdCsDfGzTbl);

				strRet = String.valueOf(zhjgID);

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

				PsTzZdCsDfGzTbl PsTzZsJGTBL = PsTzZdCsDfGzTblMapper.selectByPrimaryKey(zhjgID);

				if (PsTzZsJGTBL != null) {
					// String zhjgID=PsTzZsJGTBL.getTzCertJgId();
					String zhjgName = PsTzZsJGTBL.getTzZdcsgzName();
					System.out.println(zhjgName);
					String zhjgLinkedIn = PsTzZsJGTBL.getTzZdcsgz();

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

				String sql = "SELECT COUNT(1) FROM PS_TZ_ZDCS_DFGZ_T WHERE TZ_ZDCSGZ_ID=?";
				int count = jdbcTemplate.queryForObject(sql, new Object[] { zhjgID }, "Integer");
				if (count > 0) {

					PsTzZdCsDfGzTbl PsTzZsJGTBL = new PsTzZdCsDfGzTbl();
					/*
					 * PsTzZsJGTBL.setTzJgId(OrgID); PsTzZsJGTBL.set
					 */
					PsTzZsJGTBL.setTzZdcsgzId(String.valueOf(zhjgID));
					PsTzZsJGTBL.setTzZdcsgzName(zhjgName);
					PsTzZsJGTBL.setTzZdcsgz(zhjgLinkedIn);

					PsTzZdCsDfGzTblMapper.updateByPrimaryKeySelective(PsTzZsJGTBL);

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
