package com.tranzvision.gd.TZApplicationTemplateBundle.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZApplicationTemplateBundle.service.impl.TZImpAppFormServiceImpl;
import com.tranzvision.gd.TZCanInTsinghuaBundle.service.impl.TzRegMbaKsServiceImpl;

/**
 * MBA考生账号、报名表、推荐信批量导入
 * 
 * @author WRL
 * @since 2017-03-14
 */
@Controller
@RequestMapping(value = { "/mba" })
public class TZImpAppFormController {

	@Autowired
	private TZImpAppFormServiceImpl tZImpAppFormServiceImpl;
	
	@Autowired
	private TzRegMbaKsServiceImpl tzRegMbaKsServiceImpl;
	
	/*----------------------------考生数据导入相关方法---------------------------------*/
	/**
	 * 批量注册MBA历史考生
	 * 
	 * @param request
	 * @param response
	 * @param max
	 * @param min
	 * @return
	 */
	@RequestMapping(value = { "/reg/{min}/{max}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String reg(HttpServletRequest request, HttpServletResponse response,@PathVariable(value = "max") int max, @PathVariable(value = "min")  int min) {
		if(max == min){
			max = max + 1;
		}
		String loginHtml = tzRegMbaKsServiceImpl.reg(max,min);

		return loginHtml;
	}
	
	/**
	 * 添加MBA历史考生
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "/reg/add" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String reg(HttpServletRequest request, HttpServletResponse response) {
		String oprs = request.getParameter("oprs");
		String oprids[] = StringUtils.split(oprs, ",");
		String out = "";
        for (String oprid : oprids) {
        	int min = Integer.parseInt(oprid);
        	int max = min + 1;
        	String loginHtml = tzRegMbaKsServiceImpl.reg(max,min);
        	out = out + loginHtml;
        }

		return out;
	}
	
	/**
	 * 删除MBA历史注册考生
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "/reg/delete" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String delete(HttpServletRequest request, HttpServletResponse response) {
		String oprs = request.getParameter("oprs");
		String oprids[] = StringUtils.split(oprs, ",");
		String out = "";
        for (String oprid : oprids) {
        	String msg = tzRegMbaKsServiceImpl.delete(oprid);
        	out = out + msg;
        }
        
        return out;
	}
	
	/**
	 * 删除所有导入的MBA历史注册考生
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "/reg/delAll" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String delAll(HttpServletRequest request, HttpServletResponse response) {
		String msg = tzRegMbaKsServiceImpl.delAll();
        return msg;
	}
	
	/**
	 * 删除重复数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "/reg/delrepeat" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String delRepeat(HttpServletRequest request, HttpServletResponse response) {
        String msg = tzRegMbaKsServiceImpl.delRepeat();
       
        return msg;
	}
	
	/**
	 * 初始化邮箱密码
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "/reg/change" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String changePassword(HttpServletRequest request, HttpServletResponse response){
		String mail = request.getParameter("mail");
       	boolean retmsg = tZImpAppFormServiceImpl.changePassword(mail);
       
        return retmsg + "";
	}
	
	/*----------------------------报名表导入相关方法---------------------------------*/
	/**
	 * 报名表导入
	 * @param request
	 * @param response
	 * @param max	报名表实例编号
	 * @param min	报名表实例编号
	 * @return
	 */
	@RequestMapping(value = { "/impform/{clsid}/{min}/{max}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String impform(HttpServletRequest request, HttpServletResponse response,@PathVariable(value = "clsid") String clsid,@PathVariable(value = "max") int max, @PathVariable(value = "min")  int min) {
		if(max == min){
			max = max + 1;
		}
		String impMsg = tZImpAppFormServiceImpl.impAppForm(clsid,min,max);
		return impMsg;
	}
	
	@RequestMapping(value = { "/impform/delAppAll" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String delAppAll(HttpServletRequest request, HttpServletResponse response) {
		String impMsg = tZImpAppFormServiceImpl.delAppAll();
		return impMsg;
	}
	
	@RequestMapping(value = { "/impform/dInsRepeat" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String dInsRepeat(HttpServletRequest request, HttpServletResponse response) {
		String impMsg = tZImpAppFormServiceImpl.dInsRepeat();
		return impMsg;
	}
	
	/*----------------------------推荐信导入相关方法---------------------------------*/
	
	/**
	 * 推荐信导入
	 * 
	 * @param request
	 * @param response
	 * @param max
	 * @param min
	 * @return
	 */
	@RequestMapping(value = { "/impletter/{clsid}/{min}/{max}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String impLetter(HttpServletRequest request, HttpServletResponse response,@PathVariable(value = "clsid") String clsid,@PathVariable(value = "max") int max, @PathVariable(value = "min")  int min){
		if(StringUtils.isBlank(clsid)){
			clsid = "123";
		}
		String impMsg = tZImpAppFormServiceImpl.impAppLetter(clsid,min,max);

		return impMsg;
	}
	
	@RequestMapping(value = { "/impform/delLetterAll" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String delLetterAll(HttpServletRequest request, HttpServletResponse response) {
		String impMsg = tZImpAppFormServiceImpl.delLetterAll();
		return impMsg;
	}
	
	@RequestMapping(value = { "/createform/{clsid}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String createAppForm(HttpServletRequest request, HttpServletResponse response,@PathVariable(value = "clsid") String clsid) {
		String params = request.getParameter("oprids");
		String[] oprids = StringUtils.split(params,",");
		String impMsg = tZImpAppFormServiceImpl.createAppForm(clsid,oprids);
		return impMsg;
	}

}
