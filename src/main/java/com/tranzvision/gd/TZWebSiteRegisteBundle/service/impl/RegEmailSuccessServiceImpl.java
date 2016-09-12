package com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
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
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	
	@Override
	//确认修改邮箱成功后跳转页面
	public String tzGetHtmlContent(String strParams) {
		String result = "";
		String strErrorDesc = "";
		String contextPaht = request.getContextPath();
		String imgPath = "";
		//JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			//jacksonUtil.json2Map(strParams);
			String FLAGE =request.getParameter("FLAGE");
			String strJgid = request.getParameter("strJgid");
			String strErrorFlg = request.getParameter("errorFlg");
			String siteid =  request.getParameter("siteid");
			
			// Map<String, Object> map = jdbcTemplate.queryForMap("select TZ_SITEI_ID, TZ_SKIN_ID from PS_TZ_SITEI_DEFN_T where TZ_JG_ID=? AND TZ_SITEI_ENABLE='Y' LIMIT 0,1",new Object[]{strJgid});
			Map<String, Object> map = jdbcTemplate.queryForMap("select TZ_SITEI_ID, TZ_SKIN_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=? AND TZ_SITEI_ENABLE='Y' LIMIT 0,1",new Object[]{siteid});
			String siteId = "",skinId = "";
			if(map != null){
				siteId = (String)map.get("TZ_SITEI_ID");
				skinId = (String)map.get("TZ_SKIN_ID");
			}
			if(siteId == null){
				siteId = "";
			}
			if(skinId == null){
				skinId = "";
			}
			if(strJgid == null){
				strJgid = "";
			}
			if(FLAGE == null){
				FLAGE = "";
			}
			if(strErrorFlg == null){
				strErrorFlg = "";
			}
			
			
			String loginUrl = request.getContextPath()+"/user/login/"+strJgid.toLowerCase()+"/"+siteId;
			imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
			imgPath = request.getContextPath() + imgPath + "/" + skinId;

			if("Y".equals(FLAGE)){
				result = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_EMAIL_EITE_HTML",
						contextPaht,imgPath, "邮箱已经修改成功",loginUrl);
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
				
				result = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_EMAIL_EITE_ERROR_HTML",
						imgPath+"/error_001.jpg", strErrorDesc,contextPaht);
			}
		}catch(Exception e){
			e.printStackTrace();
			try {
				result = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_EMAIL_EITE_ERROR_HTML",
						imgPath+"/error_001.jpg", strErrorDesc,contextPaht);
			} catch (TzSystemException e1){
				e1.printStackTrace();
			}
		}
		return result;
		
	}
}
