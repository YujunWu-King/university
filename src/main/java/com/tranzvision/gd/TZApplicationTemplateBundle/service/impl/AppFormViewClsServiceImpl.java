package com.tranzvision.gd.TZApplicationTemplateBundle.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationTemplateBundle.dao.PsTzApptplDyTMapper;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzApptplDyTWithBLOBs;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
/**
 * @author WRL TZ_ONLINE_REG_PKG:TZ_ONLINE_FORM_CLS 
 * 报名表预览、管理员查看
 */
@Service("com.tranzvision.gd.TZApplicationTemplateBundle.service.impl.AppFormViewClsServiceImpl")
public class AppFormViewClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private TZGDObject tzGdObject;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private HttpServletResponse response;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private PsTzApptplDyTMapper psTzApptplDyTMapper;
	
	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;
	
	@Autowired
	private SiteRepCssServiceImpl siteRepCssServiceImpl;
	
	/**
	 * 报名表模板预览、管理员查看
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzGetHtmlContent(String strParams) {
			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strParams);
			String tplId = jacksonUtil.getString("TZ_APP_TPL_ID");
			String oprId = jacksonUtil.getString("OPRID");
			
			
			/*--- TAB页签  BEGIN ---*/
			String sql = "SELECT TZ_XXX_BH,TZ_XXX_MC,TZ_TITLE,TZ_TAPSTYLE FROM PS_TZ_APP_XXXPZ_T WHERE TZ_COM_LMC = 'Page' AND TZ_APP_TPL_ID = ? ORDER BY TZ_ORDER ASC";
			List<?> resultlist = sqlQuery.queryForList(sql, new Object[] { tplId });
			String tabHtml = "";
			int i = 1;
			for (Object obj : resultlist) {
				Map<String, Object> result = (Map<String, Object>) obj;

				String xxxBh = result.get("TZ_XXX_BH") == null ? "" : String.valueOf(result.get("TZ_XXX_BH"));
				String xxxTitle = result.get("TZ_TITLE") == null ? "" : String.valueOf(result.get("TZ_TITLE"));
				String tapStyle = result.get("TZ_TAPSTYLE") == null ? "" : String.valueOf(result.get("TZ_TAPSTYLE"));
				String divClass = "";
				if(i == 1){
					divClass = "tabNav_c tabNav_bg_c2";
				}else{
					divClass = "tabNav tabNav_bg2";
				}

				try {
					tabHtml = tabHtml +  tzGdObject.getHTMLText("HTML.TZApplicationTemplateBundle.TZ_TABS_DIV", divClass, xxxTitle, tapStyle, xxxBh);
				} catch (TzSystemException e) {
					e.printStackTrace();
				}
				i++;
			}
			try {
				tabHtml = tzGdObject.getHTMLText("HTML.TZApplicationTemplateBundle.TZ_STATIC_TABS", tabHtml);
			} catch (TzSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   /*--- TAB页签  END ---*/
			
		   /*--- 控件信息  BEGIN ---*/
			ArrayList<Map<String, Object>> comDfn = templateEngine.getComDfn(tplId);
			String comRegInfo = jacksonUtil.List2json(comDfn);
		   /*--- 控件信息  END ---*/
			
			if(StringUtils.isBlank(oprId)){
				oprId = tzLoginServiceImpl.getLoginedManagerOprid(request);
			}
			
			/*模板名称、模板报文JSON、实例数据JSON、*/
			PsTzApptplDyTWithBLOBs psTzApptplDyT = psTzApptplDyTMapper.selectByPrimaryKey(tplId);
			String tplData = psTzApptplDyT.getTzApptplJsonStr();
			tplData = tplData.replace("\\", "\\\\");
			String orgId = psTzApptplDyT.getTzJgId();
			String language = psTzApptplDyT.getTzAppTplLan();
			
			String contextUrl = request.getContextPath();
			   
			String siteId = sqlQuery.queryForObject("SELECT TZ_SITEI_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_JG_ID = ? AND TZ_SITEI_ENABLE = 'Y'", new Object[] { orgId }, "String");

			String onlineHead = "";
			String onlineFoot = "";
			try {
				onlineHead = tzGdObject.getHTMLText("HTML.TZApplicationTemplateBundle.TZ_ONLINE_HEAD_HTML");
				onlineFoot = tzGdObject.getHTMLText("HTML.TZApplicationTemplateBundle.TZ_ONLINE_FOOT_HTML");
			} catch (TzSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/*msgSet 用于双语化*/
			String msgSet = gdObjectServiceImpl.getMessageSetByLanguageCd(request, response, "TZGD_APPONLINE_MSGSET",language);
			jacksonUtil.json2Map(msgSet);
			if (jacksonUtil.containsKey(language)) {
				Map<String, Object> msgLang = jacksonUtil.getMap(language);
				msgSet = jacksonUtil.Map2json(msgLang);
			}
			
			
			String save = gdObjectServiceImpl.getMessageTextWithLanguageCd(request,"TZGD_APPONLINE_MSGSET", "SAVE", language, "保存", "Save");
			   
			String submit = gdObjectServiceImpl.getMessageTextWithLanguageCd(request,"TZGD_APPONLINE_MSGSET", "SUBMIT", language, "提交", "Submit");
			   
			String next = gdObjectServiceImpl.getMessageTextWithLanguageCd(request,"TZGD_APPONLINE_MSGSET", "NEXT", language, "下一步", "NEXT");
			
			/*上传进度条描述*/
			String loading = gdObjectServiceImpl.getMessageTextWithLanguageCd(request,"TZGD_APPONLINE_MSGSET", "LOADING", language, "上传中", "Loading");
			   
			/*上传进度条描述*/
			String processing = gdObjectServiceImpl.getMessageTextWithLanguageCd(request,"TZGD_APPONLINE_MSGSET", "PROCESS", language, "正在处理", "Processing");
			
			sql = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT = ? LIMIT 1";
			String menuId = sqlQuery.queryForObject(sql, new Object[] { "TZ_ACCOUNT_MANAGEMENT_" + orgId }, "String");
			if(StringUtils.isBlank(menuId)){
				menuId = "";
			}
			String viewHtml = "";
			try {
				tplData = tplData.replaceAll("\\$", "~");
				viewHtml = tzGdObject.getHTMLText("HTML.TZApplicationTemplateBundle.TZ_ONLINE_VIEW_HTML",contextUrl,comRegInfo,tplId,"0",tplData,tabHtml,siteId,orgId,menuId,msgSet, onlineHead, onlineFoot, save, submit, next, loading, processing, language);
				viewHtml = viewHtml.replaceAll("\\~", "\\$");
			} catch (TzSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			viewHtml = siteRepCssServiceImpl.repTitle(viewHtml, siteId);
			viewHtml = siteRepCssServiceImpl.repCss(viewHtml, siteId);
			return viewHtml;
	}
}
