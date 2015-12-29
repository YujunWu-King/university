package com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 
 * @author tang
 * 确认邮箱成功后跳转页面
 * 原： Record.WEBLIB_GD_USER, Field.TZ_GD_USER, "FieldFormula", "Iscript_RegEmailSuccess"
 */
@Service("com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl.RegEmailSuccessServiceImpl")
public class RegEmailSuccessServiceImpl extends FrameworkImpl {
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private HttpServletRequest request;
	
	@Override
	//确认修改邮箱成功后跳转页面
	public String tzGetHtmlContent(String strParams) {
		String result = "";
		String strErrorDesc = "";
		String contextPaht = request.getContextPath();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			jacksonUtil.json2Map(strParams);
			String FLAGE =request.getParameter("FLAGE").trim();
			//String strJgid = request.getParameter("strJgid").trim();
			String strErrorFlg = request.getParameter("errorFlg").trim();
			
			if("Y".equals(FLAGE)){
				result = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_EMAIL_EITE_HTML", true,
						contextPaht,contextPaht+"/statics/images/website/right_green.png", "邮箱已经修改成功");
			}else{
				if("overtime".equals(strErrorFlg)){
					 strErrorDesc = "邮箱修改失败，验证已超时";
				}
				
				if("repeat".equals(strErrorFlg)){
					strErrorDesc = "邮箱修改失败，该邮箱已被占用";
				}
				
				if("dataerror".equals(strErrorFlg)){
					strErrorDesc = "邮箱修改失败，数据错误";
				}
				
				if("codeerror".equals(strErrorFlg)){
					strErrorDesc = "邮箱修改失败，验证码无效";
				}
				
				if("".equals(strErrorDesc)){
					strErrorDesc = "邮箱修改失败";
				}
				
				result = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_EMAIL_EITE_ERROR_HTML", true,
						contextPaht+"/statics/images/website/error_001.jpg", strErrorDesc,contextPaht);
			}
		}catch(Exception e){
			try {
				result = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_EMAIL_EITE_ERROR_HTML", true,
						contextPaht+"/statics/images/website/error_001.jpg", strErrorDesc,contextPaht);
			} catch (TzSystemException e1){
				e1.printStackTrace();
			}
		}
		return result;
		
	}
}
