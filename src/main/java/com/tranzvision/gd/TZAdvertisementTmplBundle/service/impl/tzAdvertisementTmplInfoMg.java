package com.tranzvision.gd.TZAdvertisementTmplBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZAdvertisementTmplBundle.dao.PsTzADTMPLTBLMapper;
import com.tranzvision.gd.TZAdvertisementTmplBundle.model.PsTzADTMPLTBL;
import com.tranzvision.gd.TZAdvertisementTmplBundle.model.PsTzADTMPLTBLKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZAdvertisementTmplBundle.service.impl.tzAdvertisementTmplInfoMg")
public class tzAdvertisementTmplInfoMg extends FrameworkImpl{
	@Autowired
	private  SqlQuery jdbcTemplate;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private  PsTzADTMPLTBLMapper PsTzADTMPLTBLMapper;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Override
	public String tzAdd(String[] actData, String[] errMsg){
		String strRet = "";
	Map<String, Object> returnJsonMap = new HashMap<String, Object>();
	
	
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			Date nowdate=new Date();
			System.out.println(actData);
			String OrgID=tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String Opid=tzLoginServiceImpl.getLoginedManagerOprid(request);
			System.out.println(OrgID+Opid);
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析 json
				jacksonUtil.json2Map(strForm);

				//int adcertTmpl=getSeqNum.getSeqNum("TZ_ADTMPL_TBL", "TZ_AD_TMPL_ID");
				String adcertTmpl=jacksonUtil.getString("adcertTmpl");
				System.out.println(adcertTmpl);
				String adtmplName=jacksonUtil.getString("adtmplName");
				//String useFlag=jacksonUtil.getString("useFlag");
				String adcertMergHtml=jacksonUtil.getString("adcertMergHtml");

				 
				PsTzADTMPLTBL PsTzADTMPLTBL=new PsTzADTMPLTBL();
				PsTzADTMPLTBL.setTzJgId(OrgID);
				PsTzADTMPLTBL.setTzAdTmplId(adcertTmpl);
				PsTzADTMPLTBL.setTzTmplName(adtmplName);
				PsTzADTMPLTBL.setTzAdHtml(adcertMergHtml);
				PsTzADTMPLTBL.setTzUseFlag("Y");
				PsTzADTMPLTBL.setRowAddedDttm(nowdate);
				PsTzADTMPLTBL.setRowAddedOprid(Opid);
				PsTzADTMPLTBL.setRowLastmantDttm(nowdate);
				PsTzADTMPLTBL.setRowLastmantOprid(Opid);

	
				PsTzADTMPLTBLMapper.insert(PsTzADTMPLTBL);
				

				
				}	
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	/* 获取广告位模板定义信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			String OrgID=tzLoginServiceImpl.getLoginedManagerOrgid(request);
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("adcertTmpl")) {
				// 机构ID;
				String adcertTmpl = jacksonUtil.getString("adcertTmpl");
				
			     System.out.println(adcertTmpl);
				
			     PsTzADTMPLTBLKey PsTzADTMPLTBLKey = new PsTzADTMPLTBLKey();
			     PsTzADTMPLTBLKey.setTzAdTmplId(adcertTmpl);
			     PsTzADTMPLTBLKey.setTzJgId(OrgID);

			
				PsTzADTMPLTBL PsTzADTMPLTBL = PsTzADTMPLTBLMapper.selectByPrimaryKey(PsTzADTMPLTBLKey);
				
				if (PsTzADTMPLTBL != null) {
					//String zhjgID=PsTzZsJGTBL.getTzCertJgId();
					String adtmplName=PsTzADTMPLTBL.getTzTmplName();
					String useFlag=PsTzADTMPLTBL.getTzUseFlag();
					String adcertMergHtml=PsTzADTMPLTBL.getTzAdHtml();
					
					
				
			        returnJsonMap.put("adcertTmpl", adcertTmpl);
					returnJsonMap.put("adtmplName", adtmplName);
					returnJsonMap.put("useFlag", useFlag);
					returnJsonMap.put("adcertMergHtml",adcertMergHtml);

				} else {
					errMsg[0] = "1";
					errMsg[1] = "该模板数据不存在";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "该模板数据不存在";
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
	/* 更新广告位模板 */
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			Date nowdate=new Date();
			System.out.println(actData);
			String OrgID=tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String Opid=tzLoginServiceImpl.getLoginedManagerOprid(request);
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				String adcertTmpl=jacksonUtil.getString("adcertTmpl");
				String adtmplName=jacksonUtil.getString("adtmplName");
				//String useFlag=jacksonUtil.getString("useFlag");
				String adcertMergHtml=jacksonUtil.getString("adcertMergHtml");

				

				String sql = "select COUNT(1) from PS_TZ_ADTMPL_TBL WHERE TZ_AD_TMPL_ID=?";
				int count = jdbcTemplate.queryForObject(sql, new Object[] { adcertTmpl }, "Integer");
				if (count > 0) {
					 
					PsTzADTMPLTBL PsTzADTMPLTBL=new PsTzADTMPLTBL();
					/*PsTzZsJGTBL.setTzJgId(OrgID);
					PsTzZsJGTBL.set*/
					PsTzADTMPLTBL.setTzAdTmplId(String.valueOf(adcertTmpl));
					PsTzADTMPLTBL.setTzTmplName(adtmplName);
					PsTzADTMPLTBL.setTzUseFlag("Y");
					PsTzADTMPLTBL.setTzAdHtml(adcertMergHtml);
					PsTzADTMPLTBL.setRowLastmantDttm(nowdate);
					PsTzADTMPLTBL.setRowLastmantOprid(Opid);
					PsTzADTMPLTBL.setTzJgId(OrgID);
					PsTzADTMPLTBLMapper.updateByPrimaryKeySelective(PsTzADTMPLTBL);

					//strRet=String.valueOf(adcertTmpl);
					

				} else {
					errMsg[0] = "1";
					errMsg[1] = "机构ID为：" + adcertTmpl + "的证书颁发机构不存在";

				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
