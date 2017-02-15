package com.tranzvision.gd.TZCanInTsinghuaBundle.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.TZCanInTsinghuaBundle.service.impl.TzCanInTsinghuaClsServiceImpl;
import com.tranzvision.gd.TZCanInTsinghuaBundle.service.impl.TzRegMbaKsServiceImpl;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.httpclient.CommonUtils;
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 测测是否能上清华 登录前端控制器
 * 
 * @author WRL
 * @since 2017/01/11
 */
@Controller
@RequestMapping(value = { "/exam" })
public class TzCanInTsinghuaLoginController {
	@Autowired
	private TZGDObject tzGdObject;
	
	@Autowired
	private TzFilterIllegalCharacter tzFilterIllegalCharacter;
	 
	@Autowired
	private TzCanInTsinghuaClsServiceImpl tzCanInTsinghuaClsServiceImpl;
	
	@Autowired
	private TzRegMbaKsServiceImpl tzRegMbaKsServiceImpl;
	
	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;

	@RequestMapping(value = { "/{orgid}/{siteid}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String login(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "orgid") String orgid, @PathVariable(value = "siteid") String siteid) {
	
		//是否移动设备访问
		boolean isMobile = CommonUtils.isMobile(request);
		
		/*登录页面内容*/
		String loginHtml = "";
		try {
			orgid = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(orgid).toUpperCase();
			siteid = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(siteid);
			
			if(StringUtils.isBlank(orgid) || StringUtils.isBlank(siteid)){
				loginHtml = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", "", "访问站点异常，请检查您访问的地址是否正确。",
						"Can not visit the site.Please check the url.");
			}else{
				String passwordUrl = request.getContextPath() + "/dispatcher?classid=enrollCls&siteid=" + siteid + "&orgid=" + orgid.toUpperCase() + "&lang=ZHS&sen=4";
				if(isMobile){
					loginHtml = tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_MLOGIN",request.getContextPath(),orgid,siteid,passwordUrl);
				}else{
					loginHtml = tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_LOGIN",request.getContextPath(),orgid,siteid,passwordUrl);
				}
			}
		} catch (TzSystemException e) {
			e.printStackTrace();
			loginHtml = "";
		}
		return loginHtml;
	}
	
	@RequestMapping(value = { "/count" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String count(HttpServletRequest request, HttpServletResponse response) {
		//是否移动设备访问
		boolean isMobile = CommonUtils.isMobile(request);
		
		/*登录页面内容*/
		String loginHtml = "";
		String wjid = request.getParameter("surveyid");
		String pageno = request.getParameter("pageno");
		
		if(StringUtils.isBlank(pageno)){
			pageno = "1";
		}
		
		boolean isValid = tzCanInTsinghuaClsServiceImpl.isWjValid(wjid);
		if(isValid){
			loginHtml = tzCanInTsinghuaClsServiceImpl.getCountPage(wjid,pageno,isMobile);
		}else{
			loginHtml = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", "", "参数异常，问卷编号不存在或者为null.",
					"Parameter is unusual, questionnaire number does not exist or is null. ");
		}

		return loginHtml;
	}
	
	@RequestMapping(value = { "/reg/{min}/{max}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String reg(HttpServletRequest request, HttpServletResponse response,@PathVariable(value = "max") int max, @PathVariable(value = "min")  int min) {
		if(max == min){
			max = max + 1;
		}
		String loginHtml = tzRegMbaKsServiceImpl.reg(max,min);

		return loginHtml;
	}
}
