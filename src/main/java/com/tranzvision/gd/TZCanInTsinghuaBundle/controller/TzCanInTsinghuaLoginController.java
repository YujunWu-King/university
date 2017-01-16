package com.tranzvision.gd.TZCanInTsinghuaBundle.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 测测是否能上清华 登录前端控制器
 * 
 * @author WRL
 * @since 2017/01/11
 */
@Controller
@RequestMapping(value = { "/tsinghua" })
public class TzCanInTsinghuaLoginController {
	@Autowired
	private TZGDObject tzGdObject;

	@RequestMapping(value = "login", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String login(HttpServletRequest request, HttpServletResponse response) {
		
		String sUserAgent = request.getHeader("User-Agent");
		//是否移动设备访问
		boolean isMobile = false;
		String[] mobileAgents = {"Windows CE","iPod","Symbian","iPhone","BlackBerry","Android","Windows Phone"};
		if(sUserAgent.indexOf("Android") > -1 && (sUserAgent.indexOf("ERD79) > -1 || sUserAgent.indexOf('MZ60") > -1 || sUserAgent.indexOf("GT-P7") > -1 || sUserAgent.indexOf("SCH-P7") > -1)){
		}else{
			for( int i = 0; i < mobileAgents.length; i++){
				if(sUserAgent.indexOf(mobileAgents[i])>-1){
					isMobile = true;
					break;
				}
			}
		}
		
		/*登录页面内容*/
		String loginHtml = "";
		try {
			if(isMobile){
				loginHtml = tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_MLOGIN",request.getContextPath(),"","");
			}else{
				loginHtml = tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_LOGIN",request.getContextPath(),"","");
			}
		} catch (TzSystemException e) {
			e.printStackTrace();
			loginHtml = "";
		}
		return loginHtml;
	}
}
