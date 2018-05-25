package com.tranzvision.gd.TZMyEnrollmentClueBundle.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsInfoT;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.service.impl.TZCreateLeadServiceImpl;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzXsxsInfoTMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.cookie.TzCookie;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

@Controller
@RequestMapping(value = { "/createlead/mobile" })
public class TZCreateLeadsController {
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private GetHardCodePoint GetHardCodePoint;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzXsxsInfoTMapper psTzXsxsInfoTMapper;
	@Autowired
	private TzCookie tzCookie;
	@Autowired
	private TZCreateLeadServiceImpl tzCreateLeadServiceImpl;
	
	@RequestMapping(value = { "/getLocation" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getLocation(HttpServletRequest request, HttpServletResponse response) {
		
		//当前登录人通过848传入
		String tmpPwdKey = "SEMSSOKEY";
		String tmpUserId = request.getParameter("dlzh");		
		tmpUserId = tmpUserId == null ? "" : DESUtil.decrypt(tmpUserId,tmpPwdKey);
		
		String currentUserId = sqlQuery.queryForObject("SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=?",new Object[]{tmpUserId},"String");
		
		String strResponse = "\"failure\"";
		Map<String, Object> returnMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		String currentOrgId = GetHardCodePoint.getHardCodePointVal("TZ_LEADS_DEFAULT_ORG");	
		
		
		try{
			//一般职员管理所有地区，销售人员管理负责的地区
			/*
			String salesRole = GetHardCodePoint.getHardCodePointVal("TZ_EMBA_SALES_ROLE");
			String salesFlag = sqlQuery.queryForObject("SELECT 'Y' FROM PSROLEUSER WHERE ROLEUSER=? AND ROLENAME=?", new Object[]{currentUserId,salesRole}, "String");
			String optionHtml = "";
			if("Y".equals(salesFlag)){
				String strCuPsnPosSQL = "SELECT TZ_AQDQ_LABEL FROM PS_TZ_AQ_DQ_T WHERE OPRID=?";
				List<Map<String,Object>> dqList = sqlQuery.queryForList(strCuPsnPosSQL, new Object[]{currentUserId});
				if(dqList!=null){
					for(Object sObj:dqList){
						Map<String,Object> sMap = (Map<String, Object>) sObj;
						String locationId = (String) sMap.get("TZ_AQDQ_LABEL");
						String locationSQL = "SELECT TZ_LABEL_DESC FROM PS_TZ_XSXS_DQBQ_T WHERE TZ_JG_ID=? AND TZ_LABEL_NAME=?";
						String locationDesc = sqlQuery.queryForObject(locationSQL, new Object[]{currentOrgId,locationId}, "String");
						optionHtml = optionHtml + "<option value=\""+locationId+"\">"+locationDesc+"</option>";
					}
				}
			}else{
				String listSQL = "SELECT TZ_LABEL_NAME,TZ_LABEL_DESC FROM PS_TZ_XSXS_DQBQ_T WHERE TZ_LABEL_STATUS='Y' AND TZ_JG_ID=?";
				List<Map<String,Object>> dqList = sqlQuery.queryForList(listSQL, new Object[]{currentOrgId});
				if(dqList!=null){
					for(Object sObj:dqList){
						Map<String,Object> sMap = (Map<String, Object>) sObj;
						String locationId = (String) sMap.get("TZ_LABEL_NAME");						
						String locationDesc = (String) sMap.get("TZ_LABEL_DESC");		
						optionHtml = optionHtml + "<option value=\""+locationId+"\">"+locationDesc+"</option>";						
					}
				}
			}*/
			String optionHtml = "";
			String listSQL = "SELECT TZ_LABEL_NAME,TZ_LABEL_DESC FROM PS_TZ_XSXS_DQBQ_T WHERE TZ_LABEL_STATUS='Y' AND TZ_JG_ID=?";
			List<Map<String,Object>> dqList = sqlQuery.queryForList(listSQL, new Object[]{currentOrgId});
			if(dqList!=null){
				for(Object sObj:dqList){
					Map<String,Object> sMap = (Map<String, Object>) sObj;
					String locationId = (String) sMap.get("TZ_LABEL_NAME");						
					String locationDesc = (String) sMap.get("TZ_LABEL_DESC");		
					optionHtml = optionHtml + "<option value=\""+locationId+"\">"+locationDesc+"</option>";						
				}
			}
			returnMap.put("result", optionHtml);
			strResponse = jacksonUtil.Map2json(returnMap);
		}catch(Exception e){
			e.printStackTrace();
		}		
		return "handleResponse("+strResponse+")";
	}
	@RequestMapping(value = { "/create" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String createLead(HttpServletRequest request, HttpServletResponse response) {
		
		String tmpPwdKey = "SEMSSOKEY";
		String tmpUserId = request.getParameter("dlzh");		
		tmpUserId = tmpUserId == null ? "" : DESUtil.decrypt(tmpUserId,tmpPwdKey);
		
		String oprid = sqlQuery.queryForObject("SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=?",new Object[]{tmpUserId},"String");
		
		String orgid = GetHardCodePoint.getHardCodePointVal("TZ_LEADS_DEFAULT_ORG");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		try{
			
			if (!"TZ_GUEST".equals(oprid) &&!"".equals(oprid) && oprid != null) {
						
				String lxrName = request.getParameter("lxrName");
				String mobile = request.getParameter("mobile");
				String company = request.getParameter("company");
				String positon = request.getParameter("positon");
				String lendProid = request.getParameter("lendProid");
				String descr = request.getParameter("descr");
	
				String result = tzCreateLeadServiceImpl.createLead(orgid, oprid, lxrName, mobile, company, positon, lendProid, descr);
				
				returnMap.put("result", result);
			}else{
				returnMap.put("result", "登录超时，请刷新当前页面或重新登录系统");
			}
		}catch(Exception e){
			returnMap.replace("result", e.toString());
			e.printStackTrace();
		}
		return "handleResponse(" + jacksonUtil.Map2json(returnMap) + ")"; 
	}
}
