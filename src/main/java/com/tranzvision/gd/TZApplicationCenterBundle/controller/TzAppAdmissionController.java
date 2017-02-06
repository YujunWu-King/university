package com.tranzvision.gd.TZApplicationCenterBundle.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZSitePageBundle.service.impl.TzWebsiteServiceImpl;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 录取通知书展示
 * 
 * @author YTT
 * @since 2017-01-22
 */
@Controller
@RequestMapping(value = { "/admission" })
public class TzAppAdmissionController {

	@Autowired
	private TzWebsiteLoginServiceImpl tzWebsiteLoginServiceImpl;

	@Autowired
	private TzWebsiteServiceImpl tzWebsiteServiceImpl;

	@Autowired
	private TZGDObject tzGDObject;
	
	@Autowired
	private SqlQuery sqlQuery1;	

	//生成录取通知书html
	@RequestMapping(value = { "/{orgid}/{siteid}/{oprid}/{tzAppInsID}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String viewQrcodeAdmission(HttpServletRequest request, HttpServletResponse response,@PathVariable(value = "orgid") String orgid, @PathVariable(value = "siteid") String siteid,@PathVariable(value = "oprid") String oprid,@PathVariable(value = "tzAppInsID") String tzAppInsID) {
		orgid = orgid.toLowerCase();
		String strRet = "";

		
			try {
				orgid = orgid.toLowerCase();
				
				/*【1】查询个人信息照片：有则取；无则取默认*/
				String ctxPath = request.getContextPath();
				
				String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetUserHeadImg");
				Map<String, Object> mapUserHeadImg = sqlQuery1.queryForMap(sql, new Object[] { oprid });
				
				String strPhoto = "";
				
				//如果有个人信息照片
				if (null != mapUserHeadImg) {
					String strPhotoDir = mapUserHeadImg.get("TZ_ATT_A_URL") == null ? ""
							: String.valueOf(mapUserHeadImg.get("TZ_ATT_A_URL"));
					String strPhotoName = mapUserHeadImg.get("TZ_ATTACHSYSFILENA") == null ? ""
							: String.valueOf(mapUserHeadImg.get("TZ_ATTACHSYSFILENA"));

					if (!"".equals(strPhotoDir) && !"".equals(strPhotoName)) {
						strPhoto = ctxPath + strPhotoDir + strPhotoName;
					}

				}
				
				//如果没有个人信息照片
				if ("".equals(strPhoto)) {
					strPhoto = "/statics/images/website/skins/21/photo_reg.png";
				}

				/*【2】查询姓名*/
				String nameSql = "SELECT B.TZ_REALNAME FROM TZGDQHJG.PS_TZ_OPR_PHT_GL_T A,TZGDQHJG.PS_TZ_REG_USER_T B WHERE A.OPRID=? AND A.OPRID=B.OPRID";
				String perName = sqlQuery1.queryForObject(nameSql, new Object[] {oprid}, "String");
				
				/*【3】查询班级*/
				String classNameSql = "SELECT A.TZ_CLASS_NAME FROM TZGDQHJG.PS_TZ_CLASS_INF_T A ,TZGDQHJG.PS_TZ_APP_INS_T B WHERE B.TZ_APP_INS_ID=? AND A.TZ_APP_MODAL_ID=TZ_APP_TPL_ID";
				String className = sqlQuery1.queryForObject(classNameSql, new Object[] {tzAppInsID}, "String");
				
				//strRet=tzGDObject.getHTMLText("HTML.TZApplicationAdmissionBundle.TZ_GD_APP_AD_HTML",false, strPhoto, perName,className);
				strRet=tzGDObject.getHTMLText("HTML.TZApplicationAdmissionBundle.TZ_APP_ADMISSION_HTML",false, strPhoto, perName,className);
					
			} catch (TzSystemException e) {
				e.printStackTrace();
			}
			return strRet;

	}

}

