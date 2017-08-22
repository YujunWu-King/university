package com.tranzvision.gd.TZBaseBundle.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.util.sql.SqlQuery;

@Controller
@RequestMapping(value = "/")
public class PdfPrintController {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	//打印报名表pdf;
	@RequestMapping(value = "PrintPdfServlet")
	public String PrintPdfServlet(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> allRequestParams){
		// 当前登陆人
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String instanceID = String.valueOf(allRequestParams.get("instanceID"));
		Long numAppInsId = Long.parseLong(instanceID);
		/*报名表对应的班级*/
		Map<String, Object> mapFormWrk = null;
		String strClassId = "";
		String oprIdApp = "";
		boolean b_flag = false;
				
		mapFormWrk = jdbcTemplate.queryForMap("SELECT TZ_CLASS_ID,OPRID FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID = ?", 
						new Object[] { numAppInsId });
		if(mapFormWrk!=null){
					
			strClassId = mapFormWrk.get("TZ_CLASS_ID")==null?"":String.valueOf(mapFormWrk.get("TZ_CLASS_ID"));
			oprIdApp = mapFormWrk.get("OPRID")==null?"":String.valueOf(mapFormWrk.get("OPRID"));
						
			if(oprid.equals(oprIdApp)){
				/*如果是查看自己的报名表，则允许下载*/
				b_flag = true;
			}else{
				/*如果不是查看自己的报名表，则只允许班级管理员下载*/
				String strIsAdmin = jdbcTemplate.queryForObject("SELECT 'Y' FROM PS_TZ_CLS_ADMIN_T WHERE TZ_CLASS_ID = ? AND OPRID = ? LIMIT 0,1", new Object[] { strClassId, oprid },"String");
				if("Y".equals(strIsAdmin)){
					b_flag = true;
				}
			}
		}
				
		if(b_flag){
			String pdfType = jdbcTemplate.queryForObject(" select TZ_PDF_TYPE from PS_TZ_APP_INS_T a,PS_TZ_APPTPL_DY_T b where a.TZ_APP_TPL_ID=b.TZ_APP_TPL_ID and a.TZ_APP_INS_ID=?", new Object[]{Long.parseLong(instanceID)},"String");
			if("HPDF".equals(pdfType)){
				return "forward:/admission/expform/"+instanceID;
			}
			if("TPDF".equals(pdfType)){
				return "forward:/DownPdfServlet";
			}
			return "forward:/admission/expform/"+instanceID;
		}else{
			return "您没有权限访问。";
		}
		
	}
	
}
