package com.tranzvision.gd.TZSchlrBundle.service.impl;

import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.httpclient.CommonUtils;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 奖学金申请 -- 招生网站考生申请入口
 * 
 * @author WRL
 *
 */
@Service("com.tranzvision.gd.TZSchlrBundle.service.impl.TzSchlrViewClsServiceImpl")
public class TzSchlrViewClsServiceImpl extends FrameworkImpl {
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private TZGDObject tzGDObject;
	
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private SiteRepCssServiceImpl siteRepCssServiceImpl;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Override
	public String tzGetHtmlContent(String strParams) {
		//是否移动设备访问
		boolean isMobile = CommonUtils.isMobile(request);
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		String schlrViewHtml = "";
		jacksonUtil.json2Map(strParams);
		
		String strSiteId = "";
		if (jacksonUtil.containsKey("siteId")) {
			strSiteId = jacksonUtil.getString("siteId");
		}

		if (StringUtils.isBlank(strSiteId)) {
			strSiteId = request.getParameter("siteId");
		}
		
		String siteSQL = "SELECT TZ_JG_ID,TZ_SKIN_STOR,TZ_SITE_LANG FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID = ?";
		Map<String, Object> siteMap = sqlQuery.queryForMap(siteSQL, new Object[] { strSiteId });

		if (siteMap != null) {
			/*根据站点编号获取机构编号*/
			String jgId = siteMap.get("TZ_JG_ID") == null ? "" : String.valueOf(siteMap.get("TZ_JG_ID"));
			String skinstor = siteMap.get("TZ_SKIN_STOR") == null ? "" : String.valueOf(siteMap.get("TZ_SKIN_STOR"));
			/*语言*/
			String language = siteMap.get("TZ_SITE_LANG") == null ? "" : String.valueOf(siteMap.get("TZ_SITE_LANG"));
			if (StringUtils.isBlank(language)) {
				language = "ZHS";
			}
			
			String websitePath = getSysHardCodeVal.getWebsiteCssPath();
			
			String strRandom = String.valueOf(10*Math.random());
			/*样式表文件路径*/
			String cssPath = "";
			if(StringUtils.isBlank(skinstor)){
				cssPath = request.getContextPath() + websitePath + "/" + jgId.toLowerCase() + "/" + strSiteId + "/"+ "style_" + jgId.toLowerCase() + ".css?v=" + strRandom ;
			}else{
				cssPath = request.getContextPath() + websitePath + "/" + jgId.toLowerCase() + "/" + strSiteId + "/" + skinstor + "/"+ "style_" + jgId.toLowerCase() + ".css?v=" + strRandom;
			}
			
			String schlrHtml = this.schlrHtml(jgId);
			String schlredHtml = this.schlredHtml(jgId);

			try {
				if(isMobile){
					String contextPath = request.getContextPath();
					String commonUrl = contextPath + "/dispatcher";
					String JGID = sqlQuery.queryForObject("select TZ_JG_ID from PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=?",new Object[]{strSiteId},"String");
					
					if (JGID.equals("SEM")) {
						JGID="";
					} else {
						JGID.toLowerCase();
					}
					System.out.println(this.getClass().getName()+":"+JGID);
					String strHeadHtml = tzGDObject.getHTMLText("HTML.TZSchlrBundle.TZ_GD_SCHLR_HEAD_MHTML",contextPath,commonUrl,jgId,strSiteId,language);
					String strMainHtml = tzGDObject.getHTMLText("HTML.TZSchlrBundle.TZ_GD_SCHLR_VIEW_MAIN_MHTML",contextPath,schlrHtml,schlredHtml);
					schlrViewHtml = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_BASE_HTML", "申请奖学金",contextPath, strHeadHtml,strSiteId,"5", strMainHtml,JGID);
				}else{
					schlrViewHtml = tzGDObject.getHTMLText("HTML.TZSchlrBundle.TZ_GD_SCHLR_VIEW_MAIN_HTML",cssPath,request.getContextPath(), "申请奖学金", jgId, strSiteId,schlrHtml,schlredHtml);
				}
				schlrViewHtml = siteRepCssServiceImpl.repTitle(schlrViewHtml, strSiteId);
				schlrViewHtml=siteRepCssServiceImpl.repCss(schlrViewHtml, strSiteId);
				
				schlrViewHtml = siteRepCssServiceImpl.repSiteid(schlrViewHtml, strSiteId);
				schlrViewHtml = siteRepCssServiceImpl.repJgid(schlrViewHtml, jgId);
				schlrViewHtml = siteRepCssServiceImpl.repLang(schlrViewHtml, language);
			} catch (TzSystemException e) {
				e.printStackTrace();
			}
		}

		return schlrViewHtml;
	}
	
	/**
	 * 获取已申请的奖学金
	 * 
	 * @param jgId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String schlredHtml(String jgId) {
		//是否移动设备访问
		boolean isMobile = CommonUtils.isMobile(request);
		
		String schlredHtml = "";
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String sql = "SELECT SCH.TZ_SCHLR_ID,WJ.TZ_DC_WJBT TZ_SCHLR_NAME,SCH.TZ_DC_WJ_ID FROM PS_TZ_SCHLR_TBL SCH,PS_TZ_DC_WJ_DY_T WJ,PS_TZ_DC_INS_T INS WHERE WJ.TZ_DC_WJ_ID = SCH.TZ_DC_WJ_ID AND SCH.TZ_DC_WJ_ID = INS.TZ_DC_WJ_ID AND SCH.TZ_JG_ID = ? AND SCH.TZ_STATE = 'Y' AND INS.ROW_ADDED_OPRID = ? ORDER BY WJ.TZ_DC_WJ_KSRQ DESC";
		String wjSql = "SELECT CONCAT(WJ.TZ_DC_WJ_KSRQ,' ',WJ.TZ_DC_WJ_KSSJ) AS TZ_DC_WJ_KRQ,CONCAT(WJ.TZ_DC_WJ_JSRQ,' ',WJ.TZ_DC_WJ_JSSJ) AS TZ_DC_WJ_JRQ,TZ_DC_WJ_URL FROM PS_TZ_DC_WJ_DY_T WJ WHERE TZ_DC_WJ_ID = ?";
		String applySql = "SELECT TZ_IS_APPLY,TZ_NOTE FROM PS_TZ_SCHLR_RSLT_TBL WHERE TZ_SCHLR_ID = ? AND OPRID = ?";
		List<?> schlrList = sqlQuery.queryForList(sql, new Object[]{jgId,oprid});
		String attrKrq = "";
		String attrJrq = "";
		String attrWjUrl = "";
		String attrIsApply = "";
		String attrNote = "";
		try {
			if(schlrList == null || schlrList.size() < 1){
				/*没有开放的奖学金申请*/
				if(isMobile){
					//移动端内容展示
					schlredHtml = tzGDObject.getHTMLText("HTML.TZSchlrBundle.TZ_GD_SCHLR_VIEW_NO_APPLY_MHTML");
				}else{
					schlredHtml = tzGDObject.getHTMLText("HTML.TZSchlrBundle.TZ_GD_SCHLR_VIEW_NO_APPLY_HTML");
					schlredHtml = tzGDObject.getHTMLText("HTML.TZSchlrBundle.TZ_GD_SCHLR_VIEW_TABLE_HTML","",schlredHtml);
				}
			}else{
				for (Object obj : schlrList) {
					Map<String, Object> result = (Map<String, Object>) obj;
					String attrSchlrId = result.get("TZ_SCHLR_ID") == null ? "" : String.valueOf(result.get("TZ_SCHLR_ID"));
					Map<String, Object> schreMap = sqlQuery.queryForMap(applySql, new Object[]{attrSchlrId,oprid});
					if(schreMap != null){
						attrIsApply = schreMap.get("TZ_IS_APPLY") == null ? "" : String.valueOf(schreMap.get("TZ_IS_APPLY"));
						
						attrNote = schreMap.get("TZ_NOTE") == null ? "" : String.valueOf(schreMap.get("TZ_NOTE"));
						if(StringUtils.equals("Y", attrIsApply)){
							attrIsApply = "通过申请";
						}else if(StringUtils.equals("N", attrIsApply)){
							attrIsApply = "建议申请其他";
						}else if(StringUtils.equals("W", attrIsApply)){
							attrIsApply = "待审核";
						}else{
							attrIsApply = "待审核";
						}
					}else{
						attrIsApply = "待审核";
						attrNote = "";
					}

					
					String attrSchlrName = result.get("TZ_SCHLR_NAME") == null ? "" : String.valueOf(result.get("TZ_SCHLR_NAME"));
					String attrWjId = result.get("TZ_DC_WJ_ID") == null ? "" : String.valueOf(result.get("TZ_DC_WJ_ID"));
					
					Map<String, Object> wjMap = sqlQuery.queryForMap(wjSql, new Object[] { attrWjId });
	
					if (wjMap != null) {
						attrKrq = wjMap.get("TZ_DC_WJ_KRQ") == null ? "" : String.valueOf(wjMap.get("TZ_DC_WJ_KRQ"));
						attrJrq = wjMap.get("TZ_DC_WJ_JRQ") == null ? "" : String.valueOf(wjMap.get("TZ_DC_WJ_JRQ"));
						attrWjUrl = wjMap.get("TZ_DC_WJ_URL") == null ? "" : String.valueOf(wjMap.get("TZ_DC_WJ_URL"));
					}
					attrKrq=attrKrq.substring(0,attrKrq.length()-3);
					attrJrq=attrJrq.substring(0,attrJrq.length()-3);
					
					String wjInsSql = "SELECT TZ_APP_INS_ID FROM PS_TZ_DC_INS_T WHERE ROW_ADDED_OPRID = ? ORDER BY ROW_LASTMANT_DTTM DESC limit 0,1";
					String insId = sqlQuery.queryForObject(wjInsSql, new Object[] { oprid },"String");
					
					if(StringUtils.isNotBlank(attrWjUrl)){
						attrWjUrl = attrWjUrl + "&SURVEY_INS_ID=" + insId;
					}
					if(isMobile){
						//移动端展示内容
						schlredHtml += tzGDObject.getHTMLText("HTML.TZSchlrBundle.TZ_GD_SCHLR_VIEW_SCHLREDTR_MHTML",attrSchlrName,attrIsApply,attrNote,attrWjUrl);
					}else{
						schlredHtml += tzGDObject.getHTMLText("HTML.TZSchlrBundle.TZ_GD_SCHLR_VIEW_SCHLREDTR_HTML",attrSchlrName,attrKrq,attrJrq,attrIsApply,attrNote,attrWjUrl);
					}
				}
				if(!isMobile){
					String theadHtml = tzGDObject.getHTMLText("HTML.TZSchlrBundle.TZ_GD_SCHLR_VIEW_APPLY_THEAD_HTML");
					schlredHtml = tzGDObject.getHTMLText("HTML.TZSchlrBundle.TZ_GD_SCHLR_VIEW_TABLE_HTML",theadHtml,schlredHtml);

				}
			}
		} catch (TzSystemException e) {
			e.printStackTrace();
		}
		return schlredHtml;
	}
	
	/**
	 * 获取可申请未申请的奖学金列表
	 * 
	 * @param jgId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String schlrHtml(String jgId) {
		//是否移动设备访问
		boolean isMobile = CommonUtils.isMobile(request);
		
		String schlrHtml = "";
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			String sql = tzGDObject.getSQLText("SQL.TZSchlrBundle.TzViewOpenSchlr");
			List<?> schlrList = sqlQuery.queryForList(sql, new Object[]{oprid,jgId,oprid});
			if(schlrList == null ||  schlrList.size() < 1){
				/*没有开放的奖学金申请*/
				if(isMobile){
					//移动设备展示内容
					schlrHtml = tzGDObject.getHTMLText("HTML.TZSchlrBundle.TZ_GD_SCHLR_VIEW_NO_OPEN_MHTML");
				}else{
					schlrHtml = tzGDObject.getHTMLText("HTML.TZSchlrBundle.TZ_GD_SCHLR_VIEW_NO_OPEN_HTML");
					schlrHtml = tzGDObject.getHTMLText("HTML.TZSchlrBundle.TZ_GD_SCHLR_VIEW_TABLE_HTML","",schlrHtml);
				}
			}else{
				for (Object obj : schlrList) {
					Map<String, Object> result = (Map<String, Object>) obj;

					String attrSchlrName = result.get("TZ_SCHLR_NAME") == null ? "" : String.valueOf(result.get("TZ_SCHLR_NAME"));
					String attrKrq = result.get("TZ_DC_WJ_KRQ") == null ? "" : String.valueOf(result.get("TZ_DC_WJ_KRQ"));
					String attrJrq = result.get("TZ_DC_WJ_JRQ") == null ? "" : String.valueOf(result.get("TZ_DC_WJ_JRQ"));
					String attrWjUrl = result.get("TZ_DC_WJ_URL") == null ? "" : String.valueOf(result.get("TZ_DC_WJ_URL"));
					attrKrq=attrKrq.substring(0,attrKrq.length()-3);
					attrJrq=attrJrq.substring(0,attrJrq.length()-3);
					if(isMobile){
						//移动端展示
						schlrHtml += tzGDObject.getHTMLText("HTML.TZSchlrBundle.TZ_GD_SCHLR_VIEW_SCHLRTR_MHTML",attrSchlrName,attrWjUrl);
					}else{
						schlrHtml += tzGDObject.getHTMLText("HTML.TZSchlrBundle.TZ_GD_SCHLR_VIEW_SCHLRTR_HTML",attrSchlrName,attrKrq,attrJrq,attrWjUrl);
					}
				}
				if(!isMobile){
					String theadHtml = tzGDObject.getHTMLText("HTML.TZSchlrBundle.TZ_GD_SCHLR_VIEW_OPEN_THEAD_HTML");
					schlrHtml = tzGDObject.getHTMLText("HTML.TZSchlrBundle.TZ_GD_SCHLR_VIEW_TABLE_HTML",theadHtml,schlrHtml);
				}
			}
						
		} catch (TzSystemException e1) {
			// Auto-generated catch block
			e1.printStackTrace();
		}
		
		return schlrHtml;
	}
}
