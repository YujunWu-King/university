package com.tranzvision.gd.TZCopyBundle.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZCopyBundle.service.impl.TZCopyServiceImpl;
import com.tranzvision.gd.util.sql.SqlQuery;
/**
 * 复制账号、报名表
 * @author WRL
 *
 */
@Controller
@RequestMapping(value = { "/copy" })
public class TZCopyController {
	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private TZCopyServiceImpl tZCopyServiceImpl;
	
	/**
	 * 复制账号
	 * 
	 * @param request
	 * @param response
	 * @param oprid		原始账号
	 * @param count		复制次数
	 * @return
	 */
	@RequestMapping(value = { "/account/{oprid}/{prefix}/{count}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String createAccount(HttpServletRequest request, HttpServletResponse response,@PathVariable(value = "oprid") String oprid,@PathVariable(value = "prefix") String prefix, @PathVariable(value = "count")  int count) {
		String msg = "";
		if(count < 1){
			msg = "复制次数必须大于1";
		}else{
			String ishasSql = "SELECT 'Y' FROM PSOPRDEFN WHERE OPRID = ? LIMIT 0,1";
			String isHas = sqlQuery.queryForObject(ishasSql, new Object[] { oprid }, "String");
			
			if (!StringUtils.equals("Y", isHas)) {
				msg = "原始用户" + oprid + "不存在！";
			}else{
				msg = tZCopyServiceImpl.createAccount(oprid,StringUtils.upperCase(prefix),count);
			}
		}
		return msg;
	}
	
	@RequestMapping(value = { "/account/delAccount" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String delAccount(HttpServletRequest request, HttpServletResponse response) {
		String prefix = request.getParameter("prefix");
		prefix = StringUtils.upperCase(prefix);
		
		if(StringUtils.isBlank(prefix)){
			return "请指定要删除的账户前缀！";
		}
		if(StringUtils.startsWith(prefix, "MBA") || StringUtils.startsWith(prefix, "TZ")){
			return "以MBA或TZ开头的账户禁止删除！";
		}
		
		String msg = tZCopyServiceImpl.delAccount(prefix);
		return msg;
	}
	
	@RequestMapping(value = { "/appform/{oprid}/{prefix}/{clsid}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String createAppForm(HttpServletRequest request, HttpServletResponse response,@PathVariable(value = "oprid") String oprid,@PathVariable(value = "prefix") String prefix,@PathVariable(value = "clsid") String clsid) {
		
		/*种子账户是否存在*/
		String ishasSql = "SELECT 'Y' FROM PSOPRDEFN WHERE OPRID = ? LIMIT 0,1";
		String isHas = sqlQuery.queryForObject(ishasSql, new Object[] { oprid }, "String");
		
		if (!StringUtils.equals("Y", isHas)) {
			return "原始用户" + oprid + "不存在！";
		}
		
		/*班级不存在*/
		String isHasCls = "SELECT 'Y' FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID = ? LIMIT 0,1";
		String isHasC = sqlQuery.queryForObject(isHasCls, new Object[] { clsid }, "String");
		
		if (!StringUtils.equals("Y", isHasC)) {
			return "班级" + clsid + "不存在！";
		}
		/*种子账户是否已报名*/
		String isHasAppSql = "SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID = ? AND OPRID = ? limit 0,1";
		String isHasApp = sqlQuery.queryForObject(isHasAppSql, new Object[] { clsid,oprid }, "String");
		
		if (StringUtils.isBlank(isHasApp)) {
			return "原始用户" + oprid + "未报名！";
		}

		String msg = tZCopyServiceImpl.createAppForm(oprid,StringUtils.upperCase(prefix),clsid);

		return msg;
	}
	
	@RequestMapping(value = { "/appform/delAppAll" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String delAppAll(HttpServletRequest request, HttpServletResponse response) {
		String prefix = request.getParameter("prefix");
		prefix = StringUtils.upperCase(prefix);
		
		if(StringUtils.isBlank(prefix)){
			return "请指定要删除的账户前缀！";
		}
		if(StringUtils.startsWith(prefix, "MBA") || StringUtils.startsWith(prefix, "TZ")){
			return "以MBA或TZ开头的账户禁止删除！";
		}
		
		String msg = tZCopyServiceImpl.delAppAll(prefix);
		return msg;
	}
}
