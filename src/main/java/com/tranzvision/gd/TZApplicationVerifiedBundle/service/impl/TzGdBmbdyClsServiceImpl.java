package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author Administrator
 * PS:TZ_GD_BMGL_BMBSH_PKG:TZ_GD_BMBDY_CLS
 *
 */
@Service("com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl.TzGdBmbdyClsServiceImpl")
public class TzGdBmbdyClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private TzDealWithXMLServiceImpl tzDealWithXMLServiceImpl;

	
	@Override
	public String tzGetHtmlContent(String strParams) {
		//返回值;
		String strReturn = "";
		
		// 若参数为空，直接返回;
		if(strParams == null || "".equals(strParams)){
			return strReturn; 
		}
		
		String[] errMsg = {"0",""};
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String return_xmlstr = "";
		if(jacksonUtil.containsKey("appInsID")){
			String appInsID = jacksonUtil.getString("appInsID");
			String OPRID = jdbcTemplate.queryForObject("SELECT OPRID FROM PS_TZ_FORM_WRK_T A WHERE A.TZ_APP_INS_ID = ?", new Object[]{Long.parseLong(appInsID)},"String");
			String TZ_APP_TPL_ID = jdbcTemplate.queryForObject("SELECT TZ_APP_TPL_ID FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID = ?", new Object[]{Long.parseLong(appInsID)},"String");
			
			
			return_xmlstr = tzDealWithXMLServiceImpl.replaceXMLPulish(appInsID, OPRID, TZ_APP_TPL_ID, false,"", errMsg);
			
			SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
		    String s_dtm = datetimeFormate.format(new Date());
			String newFileName = s_dtm + String.valueOf((int)(10*(Math.random())));
			response.setContentType("application/download");
			response.setHeader("content-disposition", "attachment;filename="+newFileName+".xml");
			try {
				
				 ServletOutputStream out = response.getOutputStream();
				 out.write(return_xmlstr.getBytes());
				 out.close();  
				 out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		return errMsg[1]; 
		
	}
}
