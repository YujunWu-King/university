package com.tranzvision.gd.TZAdvertisementTmplBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAdvertisementTmplBundle.dao.PsTzADTMPLTBLMapper;
import com.tranzvision.gd.TZAdvertisementTmplBundle.model.PsTzADTMPLTBL;
import com.tranzvision.gd.TZAdvertisementTmplBundle.model.PsTzADTMPLTBLKey;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZAdvertisementTmplBundle.service.impl.tzAdvertisementTmplInfoMg")
public class tzAdvertisementTmplInfoMg extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private PsTzADTMPLTBLMapper PsTzADTMPLTBLMapper;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
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

				int adcertTmpl = getSeqNum.getSeqNum("TZ_ADTMPL_TBL", "TZ_AD_TMPL_ID");
				// String adcertTmpl = jacksonUtil.getString("adcertTmpl");
				System.out.println(adcertTmpl);
				String adtmplName = jacksonUtil.getString("adtmplName");
				// String useFlag=jacksonUtil.getString("useFlag");
				String adcertMergHtml = jacksonUtil.getString("adcertMergHtml");

				PsTzADTMPLTBL PsTzADTMPLTBL = new PsTzADTMPLTBL();
				PsTzADTMPLTBL.setTzJgId(OrgID);
				PsTzADTMPLTBL.setTzAdTmplId("AD_" + String.valueOf(adcertTmpl));
				PsTzADTMPLTBL.setTzTmplName(adtmplName);
				PsTzADTMPLTBL.setTzAdHtml(adcertMergHtml);
				PsTzADTMPLTBL.setTzUseFlag("Y");
				PsTzADTMPLTBL.setRowAddedDttm(nowdate);
				PsTzADTMPLTBL.setRowAddedOprid(Opid);
				PsTzADTMPLTBL.setRowLastmantDttm(nowdate);
				PsTzADTMPLTBL.setRowLastmantOprid(Opid);

				PsTzADTMPLTBLMapper.insert(PsTzADTMPLTBL);
				// strRet=String.valueOf(adcertTmpl);
				strRet = "AD_" + String.valueOf(adcertTmpl);
				returnJsonMap.put("adcertTmpl", strRet);

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return jacksonUtil.Map2json(returnJsonMap);
	}

	/* 获取广告位模板定义信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			String OrgID = tzLoginServiceImpl.getLoginedManagerOrgid(request);
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
					// String zhjgID=PsTzZsJGTBL.getTzCertJgId();
					String adtmplName = PsTzADTMPLTBL.getTzTmplName();
					String useFlag = PsTzADTMPLTBL.getTzUseFlag();
					String adcertMergHtml = PsTzADTMPLTBL.getTzAdHtml();

					returnJsonMap.put("adcertTmpl", adcertTmpl);
					returnJsonMap.put("adtmplName", adtmplName);
					returnJsonMap.put("useFlag", useFlag);
					returnJsonMap.put("adcertMergHtml", adcertMergHtml);

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
		// 返回值
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
				String adcertTmpl = jacksonUtil.getString("adcertTmpl");
				String adtmplName = jacksonUtil.getString("adtmplName");
				// String useFlag=jacksonUtil.getString("useFlag");
				String adcertMergHtml = jacksonUtil.getString("adcertMergHtml");

				String sql = "select COUNT(1) from PS_TZ_ADTMPL_TBL WHERE TZ_AD_TMPL_ID=?";
				int count = jdbcTemplate.queryForObject(sql, new Object[] { adcertTmpl }, "Integer");
				if (count > 0) {

					PsTzADTMPLTBL PsTzADTMPLTBL = new PsTzADTMPLTBL();
					/*
					 * PsTzZsJGTBL.setTzJgId(OrgID); PsTzZsJGTBL.set
					 */
					PsTzADTMPLTBL.setTzAdTmplId(String.valueOf(adcertTmpl));
					PsTzADTMPLTBL.setTzTmplName(adtmplName);
					PsTzADTMPLTBL.setTzUseFlag("Y");
					PsTzADTMPLTBL.setTzAdHtml(adcertMergHtml);
					PsTzADTMPLTBL.setRowLastmantDttm(nowdate);
					PsTzADTMPLTBL.setRowLastmantOprid(Opid);
					PsTzADTMPLTBL.setTzJgId(OrgID);
					PsTzADTMPLTBLMapper.updateByPrimaryKeySelective(PsTzADTMPLTBL);
					returnJsonMap.put("adcertTmpl", adcertTmpl);

					// strRet=String.valueOf(adcertTmpl);

				} else {
					errMsg[0] = "1";
					errMsg[1] = "机构ID为：" + adcertTmpl + "的证书颁发机构不存在";

				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}

	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		// 获取当前登陆人机构ID
		String Orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		int num = 0;
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			// 排序字段如果没有不要赋值

			// String[][] orderByArr = new String[][] { { "TZ_AD_TMPL_ID", "ASC"
			// }
			// };
			String[][] orderByArr = new String[][] {};

			// json数据要的结果字段;

			String[] resultFldArray = { "TZ_JG_ID", "TZ_PRJ_ID", "TZ_PRJ_NAME" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					String JgID = rowList[0];
					System.out.println(JgID);
					System.out.println(rowList[1]);
					Map<String, Object> mapList = new HashMap<String, Object>();
					if (JgID.equals(Orgid)) {

						mapList.put("xmid", rowList[1]);

						mapList.put("xmName", rowList[2]);
						listData.add(mapList);

					} else {
						num = Integer.valueOf(obj[0].toString()) - 1;

					}

				}
				mapRet.replace("total", num);
				mapRet.replace("root", listData);
			}

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

		return jacksonUtil.Map2json(mapRet);
	}

}
