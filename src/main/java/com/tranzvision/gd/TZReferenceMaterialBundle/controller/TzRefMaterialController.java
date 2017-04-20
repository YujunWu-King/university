package com.tranzvision.gd.TZReferenceMaterialBundle.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZReferenceMaterialBundle.service.TzRefMaterialBase;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 显示参考资料
 * @author zhanglang
 *
 */

@Controller
@RequestMapping(value = { "/refMaterial" })
public class TzRefMaterialController {
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private TZGDObject tzGdObject;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private ApplicationContext ctx;
	
	
	@RequestMapping(value = "onload", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String loadRefMaterial(HttpServletRequest request, HttpServletResponse response) {
		String refMaterialHtml = "";

		String classId  = request.getParameter("classId");	//班级ID
		String batchId   = request.getParameter("batchId");	//批次ID
		String appInsId   = request.getParameter("appInsId");	//报名表实例ID
		String model  = request.getParameter("model");	//成绩模型
		String cjxId  = request.getParameter("cjxId");	//成绩项ID   
		
		try {
			//当前登录机构
			String currentOgrId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			//查询对应参考资料ID
			String refDataSql = tzGdObject.getSQLText("SQL.TZReferenceMaterialBundle.TzRefMateralDefn");
			String refDataId = sqlQuery.queryForObject(refDataSql, new Object[]{ cjxId, currentOgrId, model }, "String");
			
			if(refDataId == null || "".equals(refDataId)){
				/*没有配置参考资料*/
			}else{
				String ckzlSql = "select TZ_APP_JAVA from PS_TZ_CKZL_T where TZ_JG_ID=? and TZ_CKZL_ID=?";
				String javaClass = sqlQuery.queryForObject(ckzlSql, new Object[]{ currentOgrId, refDataId }, "String");
				
				if(!"".equals(javaClass) && javaClass != null){
					Map<String,String> dataMap = new HashMap<String,String>();
					dataMap.put("classId", classId);
					dataMap.put("batchId", batchId);
					dataMap.put("appInsId", appInsId);
					dataMap.put("cjxId", cjxId);
					
					TzRefMaterialBase refObj = (TzRefMaterialBase) ctx.getBean(javaClass);
					refMaterialHtml = refObj.genRefDataPage(dataMap);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return refMaterialHtml;
	}
	
}
