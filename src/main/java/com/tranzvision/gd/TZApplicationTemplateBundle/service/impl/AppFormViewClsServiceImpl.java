package com.tranzvision.gd.TZApplicationTemplateBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
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
			String siteId = jacksonUtil.getString("SiteID");
			//String oprId = jacksonUtil.getString("OPRID");
			
			// 父分隔符号的id
			String strTZ_FPAGE_BH = "";
			/*--- TAB页签  BEGIN ---*/
			String sql = "SELECT TZ_XXX_BH,TZ_XXX_MC,TZ_TITLE,TZ_TAPSTYLE,TZ_FPAGE_BH FROM PS_TZ_APP_XXXPZ_T WHERE TZ_COM_LMC = 'Page' AND TZ_APP_TPL_ID = ? ORDER BY TZ_ORDER ASC";
			List<?> resultlist = sqlQuery.queryForList(sql, new Object[] { tplId });
			String tabHtml = "";
			//int i = 0;
			int numChild = 0;
			int numIndex = 0;
			
			String strDivClass = "";
			for (Object obj : resultlist) {
				Map<String, Object> result = (Map<String, Object>) obj;

				String xxxBh = result.get("TZ_XXX_BH") == null ? "" : String.valueOf(result.get("TZ_XXX_BH"));
				String xxxTitle = result.get("TZ_TITLE") == null ? "" : String.valueOf(result.get("TZ_TITLE"));
				String tapStyle = result.get("TZ_TAPSTYLE") == null ? "" : String.valueOf(result.get("TZ_TAPSTYLE"));
				strTZ_FPAGE_BH = result.get("TZ_FPAGE_BH") == null ? ""
						: String.valueOf(result.get("TZ_FPAGE_BH"));
				
				//String divClass = "";
				numIndex = numIndex + 1;
				
				// 默认第一级菜单高亮
				if (strTZ_FPAGE_BH == null || strTZ_FPAGE_BH.trim().equals("")) {
					strDivClass = "menu-active-top";
				} else {
					numChild = numChild + 1;
					// 默认第一页高亮
					if (numChild == 1) {
						strDivClass = "menu-active";
					} else {
						strDivClass = "";
					}
				}

				try {
					tabHtml = tabHtml +  tzGdObject.getHTMLText("HTML.TZApplicationTemplateBundle.TZ_TABS_DIV", strDivClass,xxxTitle,"",xxxBh);
				} catch (TzSystemException e) {
					e.printStackTrace();
				}
				//i++;
			}

		   /*--- TAB页签  END ---*/
			
		   /*--- 控件信息  BEGIN ---*/
			ArrayList<Map<String, Object>> comDfn = templateEngine.getComDfn(tplId);
			String comRegInfo = jacksonUtil.List2json(comDfn);
		   /*--- 控件信息  END ---*/
			
			/*
			if(StringUtils.isBlank(oprId)){
				oprId = tzLoginServiceImpl.getLoginedManagerOprid(request);
			}
			*/
			
			/*模板名称、模板报文JSON、实例数据JSON、*/
			PsTzApptplDyTWithBLOBs psTzApptplDyT = psTzApptplDyTMapper.selectByPrimaryKey(tplId);
			String tplData = psTzApptplDyT.getTzApptplJsonStr();
			tplData = tplData.replace("\\", "\\\\");
			String orgId = psTzApptplDyT.getTzJgId();
			String language = psTzApptplDyT.getTzAppTplLan();
			//版式（横版、竖版）
			String strDisplayType = psTzApptplDyT.getTzDisplayType();
			String contextUrl = request.getContextPath();
			   
			//String siteId = sqlQuery.queryForObject("SELECT TZ_SITEI_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_JG_ID = ? AND TZ_SITEI_ENABLE = 'Y'", new Object[] { orgId }, "String");
			siteId = (siteId == null ? "" : siteId);
			if(StringUtils.isBlank(strDisplayType) || StringUtils.equals("V", strDisplayType)){
				strDisplayType = "";
			}
			String onlineHead = "";
			String onlineFoot = "";
			try {
				onlineHead = tzGdObject.getHTMLText("HTML.TZWebsiteApplicationBundle.TZ_ONLINE_HEAD_HTML",contextUrl);
				onlineFoot = tzGdObject.getHTMLText("HTML.TZWebsiteApplicationBundle.TZ_ONLINE_FOOT_HTML");
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
			
			String leftWidth = "";
			if(psTzApptplDyT.getTzLeftWidth() != null && psTzApptplDyT.getTzLeftWidth() > 0){
				leftWidth = String.valueOf(psTzApptplDyT.getTzLeftWidth()) + "px";
			}
			String rightWidth = "";
			if(psTzApptplDyT.getTzRightWidth() != null && psTzApptplDyT.getTzRightWidth() > 0){
				rightWidth = String.valueOf(psTzApptplDyT.getTzRightWidth()) + "px";
			}
			
			String viewHtml = "";
			try {
				tplData = tplData.replaceAll("\\$", "~");
				viewHtml = tzGdObject.getHTMLText("HTML.TZApplicationTemplateBundle.TZ_ONLINE_VIEW_HTML",contextUrl,comRegInfo,tplId,"0",tplData,tabHtml,siteId,orgId,menuId,msgSet, onlineHead, onlineFoot, save, submit, next, loading, processing, language,leftWidth,rightWidth,strDisplayType);
				viewHtml = viewHtml.replaceAll("\\~", "\\$");
			} catch (TzSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			viewHtml = siteRepCssServiceImpl.repTitle(viewHtml, siteId);
			viewHtml = siteRepCssServiceImpl.repCss(viewHtml, siteId);
			return viewHtml;
	}
	
	/**
	 * 其他相关函数 找到外网站点
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzOther(String oprType, String strParams, String[] errMsg) {
		// 返回值;
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");
		// mapRet.put("result", "T");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 加载报名表模板字段
			System.out.println("type:" + oprType);
			if ("loadZSSite".equals(oprType)) {
				jacksonUtil.json2Map(strParams);
				// 机构ID
				String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				System.out.println("orgId:" + orgId);
				ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
				String sql = "SELECT TZ_SITEI_ID,TZ_SITEI_NAME FROM PS_TZ_SITEI_DEFN_T WHERE TZ_JG_ID = ? AND TZ_SITEI_ENABLE = 'Y' AND TZ_SITEI_TYPE='C'";
				System.out.println("sql:" + sql);
				List<?> listData = sqlQuery.queryForList(sql, new Object[] { orgId });

				if (listData == null || listData.size() == 0) {
					mapRet.replace("root", "");
				} else {
					String siteId = "";
					String siteName = "";
					for (Object objData : listData) {
						Map<String, Object> mapData = (Map<String, Object>) objData;
						siteId = String.valueOf(mapData.get("TZ_SITEI_ID"));
						siteName = String.valueOf(mapData.get("TZ_SITEI_NAME"));
						Map<String, Object> mapJson = new HashMap<String, Object>();
						mapJson.put("siteId", siteId);
						mapJson.put("siteName", siteName);
						listJson.add(mapJson);
					}
					mapRet.replace("root", listJson);
					// mapRet.replace("result", "T");
				}
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		System.out.println("return:" + jacksonUtil.Map2json(mapRet));
		return jacksonUtil.Map2json(mapRet);
	}
}
