package com.tranzvision.gd.TZScoolLiblistManage.service.impl;
/**
 * @author tzhjl
 *   院校库管理
 */

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.TZScoolLiblistManage.dao.PsTzPayLogTMapper;
import com.tranzvision.gd.TZScoolLiblistManage.model.PsTzPayLogT;


@Service("com.tranzvision.gd.TZScoolLiblistManage.service.impl.TzSchoolListMgBundle")

public class TzSchoolListMgBundle extends FrameworkImpl {
	@Autowired
	private  SqlQuery jdbcTemplate;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private  PsTzPayLogTMapper PsTzPayLogTMapper;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	

	@Override
	public String tzAdd(String[] actData, String[] errMsg){
		String strRet = "{}";
		int tzJgId;
	//Map<String, Object> returnJsonMap = new HashMap<String, Object>();
	
	
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			Date nowdate=new Date();
			System.out.println(actData);
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析 json
				jacksonUtil.json2Map(strForm);
				
				tzJgId=getSeqNum.getSeqNum("PS_TZ_SCH_LIB_TBL", "TZ_JG_ID");
				String tzSchoolName=jacksonUtil.getString("chinaName");
				String tzSchoolNameeng=jacksonUtil.getString("engName");
				String country=jacksonUtil.getString("country");
				String tzZgbm=jacksonUtil.getString("mainDeart");
				String city=jacksonUtil.getString("city");
				String tzSchoolLevel=jacksonUtil.getString("level");
				String tzSchoolNatrue=jacksonUtil.getString("attriBute");
				String tzSchoolType=jacksonUtil.getString("type");
				String dec=jacksonUtil.getString("adddec");
				String tzState=jacksonUtil.getString("state");
				String tzHemisphere=jacksonUtil.getString("hemiHere");
				System.out.println(tzSchoolName+tzSchoolNameeng);
			
			
				
				PsTzPayLogT PsTzPayLogT=new PsTzPayLogT();
				PsTzPayLogT.setTzJgId(String.valueOf(tzJgId));
				PsTzPayLogT.setTzSchoolName(tzSchoolName);
				PsTzPayLogT.setTzSchoolNameeng(tzSchoolNameeng);
				PsTzPayLogT.setCountry(country);
				PsTzPayLogT.setTzZgbm(tzZgbm);
				PsTzPayLogT.setCity(city);
				PsTzPayLogT.setTzSchoolLevel(tzSchoolLevel);
				PsTzPayLogT.setTzSchoolNatrue(tzSchoolNatrue);
				PsTzPayLogT.setTzSchoolType(tzSchoolType);
				PsTzPayLogT.setTzState(tzState);
				PsTzPayLogT.setDecstring(dec);
			    PsTzPayLogT.setTzHemisphere(tzHemisphere);
				PsTzPayLogT.setTzAddtime(nowdate);
				
	
				PsTzPayLogTMapper.insert(PsTzPayLogT);
			
				strRet=String.valueOf(tzJgId);
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

			if (jacksonUtil.containsKey("orgaID")) {
				// 院校库机构ID;
				String orgaID = jacksonUtil.getString("orgaID");
			
				PsTzPayLogT PsTzPayLogT = PsTzPayLogTMapper.selectByPrimaryKey(orgaID);
				if (PsTzPayLogT != null) {
					String countryID=PsTzPayLogT.getCountry();
					String selectSQLEng = "select descr FROM PS_COUNTRYTBL_LANG WHERE country=?";
					String selectSQL = "select descr FROM PS_COUNTRY_TBL WHERE country=?";
					String country=jdbcTemplate.queryForObject(selectSQL, new Object[]{countryID},"String");
					
					returnJsonMap.put("orgaID", orgaID);
					returnJsonMap.put("city", PsTzPayLogT.getCity());
					returnJsonMap.put("country",country);
					returnJsonMap.put("adddec", PsTzPayLogT.getDecstring());
					returnJsonMap.put("hemiHere", PsTzPayLogT.getTzHemisphere());
					returnJsonMap.put("level", PsTzPayLogT.getTzSchoolLevel());
					returnJsonMap.put("chinaName", PsTzPayLogT.getTzSchoolName());
					returnJsonMap.put("engName", PsTzPayLogT.getTzSchoolNameeng());
					returnJsonMap.put("attriBute", PsTzPayLogT.getTzSchoolNatrue());
					returnJsonMap.put("type", PsTzPayLogT.getTzSchoolType());
					returnJsonMap.put("state", PsTzPayLogT.getTzState());
					returnJsonMap.put("mainDeart", PsTzPayLogT.getTzZgbm());
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
				String orgaID=jacksonUtil.getString("orgaID");
				String tzSchoolName=jacksonUtil.getString("chinaName");
				String tzSchoolNameeng=jacksonUtil.getString("engName");
				String country=jacksonUtil.getString("country");
				String tzZgbm=jacksonUtil.getString("mainDeart");
				String city=jacksonUtil.getString("city");
				String tzSchoolLevel=jacksonUtil.getString("level");
				String tzSchoolNatrue=jacksonUtil.getString("attriBute");
				String tzSchoolType=jacksonUtil.getString("type");
				String dec=jacksonUtil.getString("adddec");
				String tzState=jacksonUtil.getString("state");
				String tzHemisphere=jacksonUtil.getString("hemiHere");
				

				String sql = "select COUNT(1) from PS_TZ_SCH_LIB_TBL WHERE TZ_JG_ID=?";
				int count = jdbcTemplate.queryForObject(sql, new Object[] { orgaID }, "Integer");
				if (count > 0) {

					PsTzPayLogT PsTzPayLogT=new PsTzPayLogT();
					PsTzPayLogT.setTzJgId(orgaID);
					PsTzPayLogT.setTzSchoolName(tzSchoolName);
					PsTzPayLogT.setTzSchoolNameeng(tzSchoolNameeng);
					PsTzPayLogT.setCountry(country);
					PsTzPayLogT.setTzZgbm(tzZgbm);
					PsTzPayLogT.setCity(city);
					PsTzPayLogT.setTzSchoolLevel(tzSchoolLevel);
					PsTzPayLogT.setTzSchoolNatrue(tzSchoolNatrue);
					PsTzPayLogT.setTzSchoolType(tzSchoolType);
					PsTzPayLogT.setTzState(tzState);
					PsTzPayLogT.setDecstring(dec);
				    PsTzPayLogT.setTzHemisphere(tzHemisphere);
				    
				    PsTzPayLogTMapper.updateByPrimaryKeySelective(PsTzPayLogT);

				    strRet=String.valueOf(orgaID);

				} else {
					errMsg[0] = "1";
					errMsg[1] = "机构ID为：" + orgaID + "的院校不存在";

				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
}
