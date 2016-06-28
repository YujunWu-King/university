package com.tranzvision.gd.TZBaseBundle.controller;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tranzvision.gd.util.sql.SqlQuery;

@Controller
@RequestMapping(value = "/")
public class PdfPrintController {
	@Autowired
	private SqlQuery jdbcTemplate;
	
	//打印报名表pdf;
	@RequestMapping(value = "PrintPdfServlet")
	public String PrintPdfServlet(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> allRequestParams){
		
		String instanceID = String.valueOf(allRequestParams.get("instanceID"));
		String pdfType = jdbcTemplate.queryForObject(" select TZ_PDF_TYPE from PS_TZ_APP_INS_T a,PS_TZ_APPTPL_DY_T b where a.TZ_APP_TPL_ID=b.TZ_APP_TPL_ID and a.TZ_APP_INS_ID=?", new Object[]{Long.parseLong(instanceID)},"String");
		if("HPDF".equals(pdfType)){
			return "forward:/admission/expform/"+instanceID;
		}
		if("TPDF".equals(pdfType)){
			return "forward:/DownPdfServlet";
		}
		
		return "forward:/admission/expform/"+instanceID;
		
	}
	
}
