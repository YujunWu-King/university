package com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdKjComServiceImpl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppInsTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppInsT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;


/**
 * 申请人在线报名；原：TZ_ONLINE_REG_PKG:TZ_ONLINE_APP_CLS
 * 
 * @author 张彬彬
 * @since 2016-1-29
 */
@Service("com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl.tzOnlineAppServiceImpl")
public class tzOnlineAppServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private GdKjComServiceImpl GdKjComServiceImpl;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private PsTzAppInsTMapper PsTzAppInsTMapper;
	
	/* 报名表展示 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzGetHtmlContent(String comParams) {
		// 返回值;
		String str_appform_main_html = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		//组件注册引用编号
		String strReferenceId = request.getParameter("classid");

	    //当前登陆人
		String oprid = "";
		oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		//报名表机构编号
		String strAppOrgId = "";
		//班级编号
		String strClassId = "";
		//报名表实例编号
		String strAppInsId = "";
		//被推荐人报名表实例编号
		String strAppInsIdRefer = "";
		//报名表实例编号
		Long numAppInsId = 0L;
		//班级编号
		String strRefLetterId = "";
		//管理员查看
		String strManagerView = "";
		//从历史报名表复制
		String strCopyFrom = "";
		//历史报名表使用模版编号
		String strAttachedTplId = "";
		
		//是否后台管理员人员查看
		String strIsAdmin = "";
		//当前人员填写的报名表的版本号
		String strAppInsVersion = "";
		//报名表是否只读标记
		String strAppFormReadOnly = "";
		//报名人Oprid
		String strAppOprId = "";
		//报名表状态
		String strAppInsState = "";
		//报名表使用模版编号
		String strTplId = "";
		//报名表模板类型
		String strTplType = "";
		//报名表模版语言
		String strLanguage = "";
		//报名表模版Json数据
		String strTplData = "";
		//报名表实例数据
		String strInsData = "";
		//是否匿名报名
		String strIsGuest = "N";
		//报名表提交后Url
		String strAfterSubmitUrl = "";

		//错误提示信息
		String strMessageError = "";

		if("appId".equals(strReferenceId)){
			strClassId = request.getParameter("TZ_CLASS_ID");
		    strAppInsId = request.getParameter("TZ_APP_INS_ID");
		    strRefLetterId = request.getParameter("TZ_REF_LETTER_ID");
		    strManagerView = request.getParameter("TZ_MANAGER");
		    strCopyFrom = request.getParameter("APPCOPY");
		    strAttachedTplId = request.getParameter("TZ_APP_TPL_ID");
		}else{
			strClassId = String.valueOf(jacksonUtil.getString("TZ_CLASS_ID"));
			strAppInsId = String.valueOf(jacksonUtil.getString("TZ_APP_INS_ID"));
			strRefLetterId = String.valueOf(jacksonUtil.getString("TZ_REF_LETTER_ID"));
			strManagerView = String.valueOf(jacksonUtil.getString("TZ_MANAGER"));
			strCopyFrom = String.valueOf(jacksonUtil.getString("APPCOPY"));
			strAttachedTplId = String.valueOf(jacksonUtil.getString("TZ_APP_TPL_ID"));
		}
		
		numAppInsId = Long.parseLong(strAppInsId);
		
		if(numAppInsId > 0){
			//如果存在报名表实例
			PsTzAppInsT PsTzAppInsT = PsTzAppInsTMapper.selectByPrimaryKey(numAppInsId);
			strTplId = PsTzAppInsT.getTzAppTplId();
			strAppInsState = PsTzAppInsT.getTzAppFormSta();
			strAppInsVersion = PsTzAppInsT.getTzAppInsVersion();
			strInsData = PsTzAppInsT.getTzAppinsJsonStr();
			
			if(!"".equals(strTplId) && strTplId !=null){
				//查看是否是查看附属模版 Start
				if(!"".equals(strAttachedTplId) && strAttachedTplId !=null){
					String sqlExistsZfFlag = "SELECT 'Y' FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ? AND TZ_APP_M_TPL_ID = ?";
					String strExistsZfFlag = sqlQuery.queryForObject(sqlExistsZfFlag, new Object[] { strTplId,strAttachedTplId }, "String");
					if("Y".equals(strExistsZfFlag)){
						strTplId = strAttachedTplId;
						//根据报名表实例和附属模版编号去获得报名表Json数据(先保留，待开发完善)
						strIsAdmin = "Y";
						strAppFormReadOnly = "Y";
					}
				}
				//查看是否是查看附属模版 end
				
				//如果报名表已提交，则只读显示
				if("U".equals(strAppInsState)){
					strAppFormReadOnly = "Y";
				}
				
				String sqlGetAppTplInfo = "SELECT TZ_APP_TPL_LAN,TZ_USE_TYPE FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ?";
				Map<String, Object> MapAppTplInfo = sqlQuery.queryForMap(sqlGetAppTplInfo, new Object[] { strTplId });
				strLanguage = String.valueOf(MapAppTplInfo.get("TZ_APP_TPL_LAN"));
				strTplType = String.valueOf(MapAppTplInfo.get("TZ_USE_TYPE"));
				
				//如果报名表模版类型为报名表
				if("BMB".equals(strTplType)){
					String sqlGetFormWorkInfo = "SELECT OPRID,TZ_CLASS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID = ? ORDER BY OPRID";
					Map<String, Object> MapFormWorkInfo = sqlQuery.queryForMap(sqlGetFormWorkInfo, new Object[] { strAppInsId });
					if(MapFormWorkInfo!=null){
						strAppOprId = String.valueOf(MapFormWorkInfo.get("OPRID"));
						strClassId = String.valueOf(MapFormWorkInfo.get("TZ_CLASS_ID"));
						if("TZ_GUEST".equals(strAppOprId)){
							//如果是匿名报名
							String sqlGetIsGuest = "SELECT TZ_GUEST_APPLY FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID = ?";
							strIsGuest = sqlQuery.queryForObject(sqlGetIsGuest, new Object[] { strClassId }, "String");
							if(!"Y".equals(strIsGuest)){
								strMessageError = GdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET", "SESSION_INVAILD", strLanguage,"当前会话已失效，请重新登陆。", "The current session is timeout or the current access is invalid,Please relogin.");
							}
						}else{
							if(oprid.equals(strAppOprId)){
								//自己操作自己的报名表
							}else{
								//看是否管理员查看报名表(待完善)
								strIsAdmin = "";
								if("Y".equals(strIsAdmin)){
									//管理员只读查看
									strAppFormReadOnly = "Y";
								}else{
									//非法访问
									strMessageError = GdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET", "ILLEGAL_OPERATION", strLanguage,"非法操作", "Illegal operation.");
								}
							}
						}
					}else{
						strMessageError = GdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET", "ILLEGAL_OPERATION", strLanguage,"非法操作", "Illegal operation.");
					}
					
				}else if("TJX".equals(strTplType)){
					//是否有推荐信编号
					if(!"".equals(strRefLetterId) && strRefLetterId != null){
						if("Y".equals(strManagerView)){
							strIsAdmin = "Y";
							strAppFormReadOnly = "Y";
						}
						//查询推荐信报名表编号
						String sqlGetKsTjxInfo = "SELECT TZ_APP_INS_ID,OPRID FROM PS_TZ_KS_TJX_TBL WHERE TZ_REF_LETTER_ID = ? AND TZ_TJX_APP_INS_ID = ?";
						Map<String, Object> MapKsTjxInfo = sqlQuery.queryForMap(sqlGetKsTjxInfo, new Object[] { strRefLetterId,strAppInsId });
						strAppInsIdRefer = String.valueOf(MapKsTjxInfo.get("TZ_APP_INS_ID"));
						strAppOprId = String.valueOf(MapKsTjxInfo.get("OPRID"));
						Long numAppInsIdRefer = Long.parseLong(strAppInsIdRefer);
						if(numAppInsIdRefer > 0){
							//找到有效的被推荐人
						}else{
							strMessageError = GdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET", "PARAERROR", strLanguage,"参数错误", "Parameter error.");
						}
					}else{
						strMessageError = GdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET", "PARAERROR", strLanguage,"参数错误", "Parameter error.");
					}
				}else{
					strMessageError = GdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET", "TEMPLATEERROR", strLanguage,"没有找到对应的模版", "Could not find the corresponding template");
				}
				//str_appform_main_html = "报名表语言"+ strLanguage + "报名表类型"+strTplType + "";
			}else{
				//没有找到对应的模版
				strMessageError = GdKjComServiceImpl.getMessageText(request, response, "TZGD_APPONLINE_MSGSET", "TEMPLATEERROR", "没有找到对应的模版", "Could not find the corresponding template");
			}
		}else{
			//如果没有报名表实例编号，看是否有班级编号
			if(!"".equals(strClassId) && strClassId != null){
				//班级是否开放报名
				String sql = "SELECT TZ_IS_APP_OPEN FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID = ?";
				String strClassIsOpen = sqlQuery.queryForObject(sql, new Object[] { strClassId }, "String");
				if("Y".equals(strClassIsOpen)){
					//班级开放报名
					if("TZ_GUEST".equals(oprid)){
						//如果是Guest用户，看班级是否允许匿名报名
						sql = "SELECT TZ_GUEST_APPLY FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID = ?";
						String strGuestApply = sqlQuery.queryForObject(sql, new Object[] { strClassId }, "String");
						if("Y".equals(strGuestApply)){
							sql = "SELECT TZ_APP_MODAL_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_IS_APP_OPEN = 'Y' AND TZ_CLASS_ID = ?";
							strTplId = sqlQuery.queryForObject(sql, new Object[] { strClassId }, "String");
							if("".equals(strTplId) || strTplId == null){
								strMessageError = GdKjComServiceImpl.getMessageText(request, response, "TZGD_APPONLINE_MSGSET", "PARAERROR", "参数错误", "Parameter error");
							}
							strIsGuest = "Y";
						}else{
							strMessageError = GdKjComServiceImpl.getMessageText(request,response, "TZGD_APPONLINE_MSGSET", "SESSION_INVAILD", "当前会话已失效，请重新登陆。", "The current session is timeout or the current access is invalid,Please relogin.");
						}
					}else{
						//是注册用户在线报名
						sql = "SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID = ? AND TZ_CLASS_ID = ?";
						strAppInsId = sqlQuery.queryForObject(sql, new Object[] { oprid,strClassId }, "String");
						strAppOprId = oprid;
						if(!"".equals(strAppInsId) && strAppInsId != null){
							numAppInsId = Long.parseLong(strAppInsId);
						}else{
							numAppInsId = 0L;
						}
						
						if(numAppInsId > 0){
							//传入了报名表实例
							PsTzAppInsT PsTzAppInsT = PsTzAppInsTMapper.selectByPrimaryKey(numAppInsId);
							strTplId = PsTzAppInsT.getTzAppTplId();
							strAppInsState = PsTzAppInsT.getTzAppFormSta();
							strAppInsVersion = PsTzAppInsT.getTzAppInsVersion();
							strInsData = PsTzAppInsT.getTzAppinsJsonStr();
							if("".equals(strTplId) || strTplId == null){
								strMessageError = GdKjComServiceImpl.getMessageText(request, response, "TZGD_APPONLINE_MSGSET", "PARAERROR", "参数错误", "Parameter error");
							}
							//如果报名表已提交，则只读显示
							if("U".equals(strAppInsState)){
								strAppFormReadOnly = "Y";
							}
						}else{
							sql = "SELECT TZ_APP_MODAL_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_IS_APP_OPEN = 'Y' AND TZ_CLASS_ID = ?";
							strTplId = sqlQuery.queryForObject(sql, new Object[] { strClassId }, "String");
							if("".equals(strTplId) || strTplId == null){
								strMessageError = GdKjComServiceImpl.getMessageText(request, response, "TZGD_APPONLINE_MSGSET", "PARAERROR", "参数错误", "Parameter error");
							}
						}
					}	
				}else{
					strMessageError = GdKjComServiceImpl.getMessageText(request, response, "TZGD_APPONLINE_MSGSET", "NOT_OPEN", "当前班级未开通在线报名", "Not open the online registration.");
				}
			}else{
				//没有找到对应的模版
				strMessageError = GdKjComServiceImpl.getMessageText(request, response, "TZGD_APPONLINE_MSGSET", "PARAERROR", "参数错误", "Parameter error");
			}
		}
		
		//如果存在报名表模版
		String sql = "";
		sql = "SELECT TZ_APPTPL_JSON_STR,TZ_USE_TYPE,TZ_JG_ID,TZ_APP_TPL_LAN,TZ_APP_TZURL FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ?";
		Map<String, Object> MapAppTplInfo = sqlQuery.queryForMap(sql, new Object[] { strTplId });
		strAppOrgId = String.valueOf(MapAppTplInfo.get("TZ_JG_ID"));
		strTplType = String.valueOf(MapAppTplInfo.get("TZ_USE_TYPE"));
		strTplData = String.valueOf(MapAppTplInfo.get("TZ_APPTPL_JSON_STR"));
		strLanguage = String.valueOf(MapAppTplInfo.get("TZ_APP_TPL_LAN"));
		strAfterSubmitUrl = String.valueOf(MapAppTplInfo.get("TZ_APP_TZURL"));
		if("null".equals(strTplData) || "".equals(strTplData) || strTplData == null){
			strTplData = "''";
		}
		//获得站点信息
		sql = "SELECT TZ_SITEI_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_JG_ID = ? AND TZ_SITEI_ENABLE = 'Y'";
		//站点编号
		String strSiteId = "";
		strSiteId = sqlQuery.queryForObject(sql, new Object[] { strAppOrgId }, "String");
		
		String strMenuType = "";
		String strMenuId = "";
		
		sql = "SELECT CMBC_HARDCODE_VAL FROM PS_CMBC_HARDCD_PNT WHERE CMBC_HARDCODE_PNT = ?";
		strMenuType = sqlQuery.queryForObject(sql, new Object[] { "TZ_ACCOUNT_MANAGEMENT" }, "String");
		sql = "SELECT TZ_MENU_ID FROM PS_TZ_SITEI_MENU_T WHERE TZ_SITEI_ID=? AND TZ_MENU_TYPE_ID=?";
		strMenuId = sqlQuery.queryForObject(sql, new Object[] { strSiteId,strMenuType }, "String");
		
		
		if("".equals(strMessageError)){
			if(numAppInsId>0 && "BMB".equals(strTplType)){
				//检查推荐信的完成状态 %This.checkRefletter(&strAppInsId, &strTplId);
			}
			//执行页面加载事件-模版级事件（待完成）
			//报名表Tab页签展示
			int numIndex = 0;
			String strXxxBh = "";
			//String strXxxMc = "";
			String strXxxTitle = "";
			String strTapStyle = "";
			String strDivClass = "";
			String strTabs = "";
			String strTab = "";
			String sqlGetTapInfo = "SELECT TZ_XXX_BH,TZ_XXX_MC,TZ_TITLE,TZ_TAPSTYLE FROM PS_TZ_APP_XXXPZ_T WHERE TZ_COM_LMC = 'Page' AND TZ_APP_TPL_ID = ? ORDER BY TZ_ORDER ASC";	
			List<?> listDataTap = sqlQuery.queryForList(sqlGetTapInfo, new Object[] { strTplId });
			for (Object objDataTap : listDataTap) {
				Map<String, Object> mapDataTap = (Map<String, Object>) objDataTap;
				strXxxBh = String.valueOf(mapDataTap.get("TZ_XXX_BH"));
				//strXxxMc = String.valueOf(mapDataTap.get("TZ_XXX_MC"));
				strXxxTitle = String.valueOf(mapDataTap.get("TZ_TITLE"));
				strTapStyle = String.valueOf(mapDataTap.get("TZ_TAPSTYLE"));
				numIndex = numIndex + 1;
				if(numIndex == 1){
					strDivClass = "tabNav_c";
				}else{
					strDivClass = "tabNav";
				}
				if("Y".equals(strIsAdmin)){
					if(numIndex == 1){
						strDivClass = strDivClass + " tabNav_bg_c2";
					}else{
						strDivClass = strDivClass + " tabNav_bg2";
					}
				}else{
					String strPageComplete = "";
					if(numAppInsId>0){
						sql = "SELECT TZ_HAS_COMPLETE FROM PS_TZ_APP_COMP_TBL WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
						strPageComplete = sqlQuery.queryForObject(sql, new Object[] { numAppInsId,strXxxBh }, "String");
						if("Y".equals(strPageComplete)){
							if(numIndex == 1){
								strDivClass = strDivClass + " tabNav_bg_c";
							}else{
								strDivClass = strDivClass + " tabNav_bg";
							}
						}else{
							if(numIndex == 1){
								strDivClass = strDivClass + " tabNav_bg_c2";
							}else{
								strDivClass = strDivClass + " tabNav_bg2";
							}
						}
					}else{
						if(numIndex == 1){
							strDivClass = strDivClass + " tabNav_bg_c2";
						}else{
							strDivClass = strDivClass + " tabNav_bg2";
						}
					}
				}
				try {
					strTab = strTab + tzGdObject.getHTMLText("HTML.TZWebsiteApplicationBundle.TZ_TABS_DIV", strDivClass, strXxxTitle, strTapStyle, strXxxBh);
				} catch (TzSystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					strTab = "";
				}
			}
			try {
				strTabs = tzGdObject.getHTMLText("HTML.TZWebsiteApplicationBundle.TZ_STATIC_TABS", strTab);
			} catch (TzSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				strTabs = "";
			}
			//控件信息（待开发）
			String strComRegInfo = "";
			//双语化消息集合Json字符串
			//msgSet 用于双语（待开发）
			String strMsgSet= "{}";
			//获取个人基本信息
			String strUserInfoSet = "";
			
			String strSave = GdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET", "SAVE", strLanguage,"保存", "Save");
			String strSubmit = GdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET", "SUBMIT", strLanguage,"提交", "Submit");
			String strNext = GdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET", "NEXT", strLanguage,"下一步", "Next");
			String strPrev = GdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET", "Prev", strLanguage,"上一步", "Previous");
			String strLoading = GdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET", "LOADING", strLanguage,"上传中", "Loading");
			String strProcessing = GdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET", "PROCESS", strLanguage,"正在处理", "Processing");
			
			String strTzGeneralURL = "";
			
			if("N".equals(strIsGuest)){
				sql = "SELECT TZ_IS_GUEST FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID = ? AND OPRID = ?";
				strIsGuest = sqlQuery.queryForObject(sql, new Object[] { strClassId,strAppOprId }, "String");
			}
			
			//报名表头部信息
			String strOnlineHead = "";
			//报名表底部信息
			String strOnlineFoot = "";
			//报名表左侧
			String strOnlineLeft = "";
			
			String strMainInnerStyle = "";
			
			String strMainStyle = "";
			
			if("Y".equals(strIsAdmin)){
				strMainInnerStyle = "margin: 0 auto;float:none";
				strMainStyle = "width:788px;";
			}else{
				try {
					strOnlineHead = tzGdObject.getHTMLText("HTML.TZWebsiteApplicationBundle.TZ_ONLINE_HEAD_HTML");
					strOnlineFoot = tzGdObject.getHTMLText("HTML.TZWebsiteApplicationBundle.TZ_ONLINE_FOOT_HTML");
					if("BMB".equals(strTplType)){
						if("Y".equals(strIsGuest)){
							strMainInnerStyle = "margin: 0 auto;float:none";
						}else{
							strOnlineLeft = tzGdObject.getHTMLText("HTML.TZWebsiteApplicationBundle.TZ_ONLINE_LEFTMENU_HTML");
						}
					}else{
						strMainInnerStyle = "margin: 0 auto;float:none";
					}
				} catch (TzSystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if("".equals(strAppInsId) || strAppInsId == null){
				strAppInsId = "0";
			}
			
			try {
				str_appform_main_html = tzGdObject.getHTMLText("HTML.TZWebsiteApplicationBundle.TZ_ONLINE_PAGE_HTML",strTzGeneralURL,"[" + strComRegInfo + "]",strTplId,strAppInsId,strClassId,strRefLetterId,strTplData,strInsData,strTabs,strSiteId,strAppOrgId,strMenuId,strAppFormReadOnly,strMsgSet,strLanguage,strSave,strNext,strSubmit,strTplType,strLoading,strProcessing,strAfterSubmitUrl,strOnlineHead,strOnlineFoot,strOnlineLeft,strIsAdmin,strMainInnerStyle,strUserInfoSet,strMainStyle,strPrev,strAppInsVersion);
			} catch (TzSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			str_appform_main_html = strMessageError;
		}
		return str_appform_main_html;
	}
}
