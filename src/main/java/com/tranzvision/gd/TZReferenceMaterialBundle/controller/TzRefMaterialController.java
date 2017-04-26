package com.tranzvision.gd.TZReferenceMaterialBundle.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZApplicationTemplateBundle.dao.PsTzApptplDyTMapper;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzApptplDyTWithBLOBs;
import com.tranzvision.gd.TZApplicationTemplateBundle.service.impl.TemplateEngine;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdKjComServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClassInfTMapper;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClassInfT;
import com.tranzvision.gd.TZReferenceMaterialBundle.service.TzRefMaterialBase;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppInsTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppInsT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl.tzOnlineAppEngineImpl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl.tzOnlineAppViewServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
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
	

	
	@Autowired
	private GdKjComServiceImpl gdKjComServiceImpl;
	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;
	@Autowired
	private SiteRepCssServiceImpl siteRepCssServiceImpl;

	@Autowired
	private tzOnlineAppViewServiceImpl tzOnlineAppViewServiceImpl;
	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private PsTzAppInsTMapper psTzAppInsTMapper;

	@Autowired
	private PsTzApptplDyTMapper psTzApptplDyTMapper;

	@Autowired
	private tzOnlineAppEngineImpl tzOnlineAppEngineImpl;

	@Autowired
	private PsTzClassInfTMapper psTzClassInfTMapper;
	
	
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
	
	
	
	/***
	 * 查看附属报名表
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "attachApp", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String viewAttachApplyForm(HttpServletRequest request, HttpServletResponse response) {
		// 返回值;
		String str_appform_main_html = "";
		JacksonUtil jacksonUtil = new JacksonUtil();

		String contextUrl = request.getContextPath();
		String strTzGeneralURL = contextUrl + "/dispatcher";

		// 报名表机构编号
		String strAppOrgId = "";
		// 班级编号
		String strClassId = "";
		// 报名表实例编号
		String strAppInsId = "";
		// 被推荐人报名表实例编号
		String strAppInsIdRefer = "";
		// 报名表实例编号
		Long numAppInsId = 0L;
		// 班级编号
		String strRefLetterId = "";
		// 管理员查看
		String strManagerView = "";
		// 从历史报名表复制
		String strCopyFrom = "";
		// 历史报名表使用模版编号
		String strAttachedTplId = "";

		// 是否后台管理员人员查看
		String strIsAdmin = "";
		// 当前人员填写的报名表的版本号
		String strAppInsVersion = "";
		// 报名表是否只读标记
		String strAppFormReadOnly = "";
		// 报名人Oprid
		String strAppOprId = "";
		// 报名表状态
		String strAppInsState = "";
		// 报名表使用模版编号
		String strTplId = "";
		// 报名表模板类型
		String strTplType = "";
		// 报名表模版语言
		String strLanguage = "";
		// 报名表模版Json数据
		String strTplData = "";
		// 报名表实例数据
		String strInsData = "";
		// 是否匿名报名
		String strIsGuest = "N";
		// 报名表提交后Url
		String strAfterSubmitUrl = "";
		// 站点编号
		String strSiteId = "";
		// 批次
		String strBatchId = "";

		// 推荐信是否开启密码 默认没有密码
		String strTJXIsPwd = "N";
		// 推荐信密码
		String strTJXPwd = "";

		// 错误提示信息
		String strMessageError = "";
		// 是否可编辑(管理员审核查看报名表时传递的参数)
		String strIsEdit = "N";

		// 页面跳转ID
		String strPageID = "";

		// 班级项目ID
		String classProjectID = "";


		strAppInsId = request.getParameter("TZ_APP_INS_ID");
		strAttachedTplId = request.getParameter("TZ_APP_TPL_ID");
		strIsEdit = request.getParameter("isEdit");
		
		if (strSiteId == null || strSiteId.equals("null")) {
			strSiteId = "";
		}
		if (strIsEdit == null || strIsEdit.equals("null")) {
			strIsEdit = "N";
		}
		if (strPageID == null || strPageID.equals("null")) {
			strPageID = "";
		}

		if (strRefLetterId == null || strRefLetterId.equals("null"))
			strRefLetterId = "";
		if (strManagerView == null || strManagerView.equals("null"))
			strManagerView = "";
		if (strCopyFrom == null || strCopyFrom.equals("null"))
			strCopyFrom = "N";

		if ("".equals(strAppInsId) || strAppInsId == null) {
			numAppInsId = 0L;
		} else {
			numAppInsId = Long.parseLong(strAppInsId);
		}

		// 报名表实例
		PsTzAppInsT psTzAppInsT = null;

		// 报名表模版
		PsTzApptplDyTWithBLOBs psTzApptplDyTWithBLOBs = null;

		// 报名表实例和班级对应关系表
		// PsTzFormWrkT psTzFormWrkT = null;

		// 班级表
		PsTzClassInfT psTzClassInfT = null;

		// 执行的SQL
		String sql = "";

		// 取数据的map
		Map<String, Object> mapData = null;

		// 取数据的list
		List<Map<String, Object>> listData = null;

		// 如果报名表传过来了
		if (numAppInsId > 0) {
			psTzAppInsT = psTzAppInsTMapper.selectByPrimaryKey(numAppInsId);
			if (psTzAppInsT != null) {
				strTplId = psTzAppInsT.getTzAppTplId();
				strAppInsState = psTzAppInsT.getTzAppFormSta();

				// 如果报名表已提交，则只读显示
				if ("U".equals(strAppInsState)) {
					strAppFormReadOnly = "Y";
				}
				strAppInsVersion = psTzAppInsT.getTzAppInsVersion();
				strInsData = psTzAppInsT.getTzAppinsJsonStr();
				strTJXPwd = psTzAppInsT.getTzPwd();
				if (strTplId == null) {
					strTplId = "";
				}
				if (strAppInsState == null) {
					strAppInsState = "N";
				}
				if (strAppInsVersion == null) {
					strAppInsVersion = "";
				}
				if (!"".equals(strTplId) && strTplId != null) {

					/*----查看是否是查看附属模版 Start  ----*/
					// 主模版的实例ID+附属模版的模版ID
					if (StringUtils.isNotBlank(strAttachedTplId) && !StringUtils.equals(strAttachedTplId, "null")) {
						// &&
						// strAttachedTplId.equals(psTzApptplDyTWithBLOBs.getTzAppMTplId()))
						// {
						// 附属模板
						sql = "SELECT TZ_APP_M_TPL_ID FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID=?";
						// PsTzApptplDyTWithBLOBs attachedPsTzApptplDyTWithBLOBs
						// =
						// psTzApptplDyTMapper.selectByPrimaryKey(strAttachedTplId);
						String TZ_APP_M_TPL_ID = sqlQuery.queryForObject(sql, new Object[] { strAttachedTplId }, "String");
						if (strTplId.equals(TZ_APP_M_TPL_ID)) {
							strTplId = strAttachedTplId;
							// 根据报名表实例和附属模版编号去获得报名表Json数据
							strInsData = tzOnlineAppViewServiceImpl.getHisAppInfoJson(numAppInsId, strTplId);
							strIsAdmin = "Y";
							strAppFormReadOnly = "Y";
						}
					}
					/*----查看是否是查看附属模版 end  ----*/

					// 获取模版信息
					psTzApptplDyTWithBLOBs = psTzApptplDyTMapper.selectByPrimaryKey(strTplId);

					if (psTzApptplDyTWithBLOBs != null) {

						strLanguage = psTzApptplDyTWithBLOBs.getTzAppTplLan();// TZ_APP_TPL_LAN
						strTplType = psTzApptplDyTWithBLOBs.getTzUseType(); // TZ_USE_TYPE
						strAppOrgId = psTzApptplDyTWithBLOBs.getTzJgId(); // TZ_JG_ID
						strTJXIsPwd = psTzApptplDyTWithBLOBs.getTzPwdType();// TZ_PWD_TYPE

						if ("BMB".equals(strTplType)) {
							/*---如果报名表模版类型为报名表 Begin   ----*/

							// 获取 班级和实体关联关系表
							sql = "SELECT OPRID,TZ_CLASS_ID,TZ_BATCH_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID = ? ORDER BY OPRID";
							mapData = null;
							mapData = sqlQuery.queryForMap(sql, new Object[] { strAppInsId });
							if (mapData != null) {
								strAppOprId = String.valueOf(mapData.get("OPRID"));
								strClassId = String.valueOf(mapData.get("TZ_CLASS_ID"));
								strBatchId = String.valueOf(mapData.get("TZ_BATCH_ID"));
								psTzClassInfT = psTzClassInfTMapper.selectByPrimaryKey(strClassId);

								classProjectID = psTzClassInfT.getTzPrjId();
								if ("".equals(strSiteId) || strSiteId == null) {
									// 如果没有传入siteId，则取班级对应的站点
									sql = "select TZ_SITEI_ID from PS_TZ_CLASS_INF_T A,PS_TZ_PROJECT_SITE_T B where A.TZ_CLASS_ID=? AND A.TZ_PRJ_ID = B.TZ_PRJ_ID LIMIT 1";
									strSiteId = sqlQuery.queryForObject(sql, new Object[] { strClassId }, "String");
								}
								/*
								if ("TZ_GUEST".equals(strAppOprId) || "".equals(strAppOprId)) {
									// 如果是匿名报名
									sql = "SELECT TZ_GUEST_APPLY FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID = ?";
									strIsGuest = sqlQuery.queryForObject(sql, new Object[] { strClassId }, "String");
									if (!"Y".equals(strIsGuest)) {
										strMessageError = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
												"TZGD_APPONLINE_MSGSET", "SESSION_INVAILD", strLanguage,
												"当前会话已失效，请重新登陆。",
												"The current session is timeout or the current access is invalid,Please relogin.");
									}
								} else {
									if (oprid.equals(strAppOprId)) {
										// 自己操作自己的报名表
									} else {
										// 看是否管理员查看报名表
										strIsAdmin = "";
										strIsAdmin = tzOnlineAppEngineImpl.checkAppViewQx(strTplId, oprid, strAppOrgId,
												strClassId);
										if ("Y".equals(strIsAdmin)) {
											// 管理员只读查看
											strAppFormReadOnly = "Y";
										} else {
											// 非法访问
											strMessageError = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
													"TZGD_APPONLINE_MSGSET", "ILLEGAL_OPERATION", strLanguage, "非法操作",
													"Illegal operation.");
										}
									}
								}
								*/
							} else {
								strMessageError = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
										"TZGD_APPONLINE_MSGSET", "ILLEGAL_OPERATION", strLanguage, "非法操作",
										"Illegal operation.");
							}
							/*---如果报名表模版类型为报名表 End   ----*/
						} else if ("TJX".equals(strTplType)) {
							/*---如果报名表模版类型为推荐信Begin   ----*/
							// 如果是推荐信，1.检查 是否推荐信模版里面 是否设置了密码，如果设置了密码，检查
							// 推荐性实例，看看密码是否已经填写
							// 如果填写 ，那么推荐人进入的时候首先填写密码，如果没有填写，进入报名表的时候
							// 可以填写密码(需要填写2次)
							if (!"".equals(strRefLetterId) && strRefLetterId != null) {

								// strAppInsId 该推荐信实例ID
								// strRefLetterId 该推荐信唯一ID

								if ("Y".equals(strManagerView)) {
									strIsAdmin = "Y";
									strAppFormReadOnly = "Y";
								}
								// 查询推荐信报名表编号
								sql = "SELECT TZ_APP_INS_ID,OPRID FROM PS_TZ_KS_TJX_TBL WHERE TZ_REF_LETTER_ID = ? AND TZ_TJX_APP_INS_ID = ?";
								mapData = null;
								mapData = sqlQuery.queryForMap(sql, new Object[] { strRefLetterId, strAppInsId });
								strAppInsIdRefer = String.valueOf(mapData.get("TZ_APP_INS_ID"));
								strAppOprId = String.valueOf(mapData.get("OPRID"));
								Long numAppInsIdRefer = Long.parseLong(strAppInsIdRefer);
								if (numAppInsIdRefer > 0) {
									// 找到有效的被推荐人
									// 获取推荐信对应的报名表
									if ("".equals(strSiteId) || strSiteId == null) {
										// 如果没有传入siteId，则取班级对应的站点
										sql = "select TZ_SITEI_ID from PS_TZ_CLASS_INF_T A,PS_TZ_PROJECT_SITE_T B,PS_TZ_FORM_WRK_T C"
												+ " where A.TZ_CLASS_ID=C.TZ_CLASS_ID AND A.TZ_PRJ_ID = B.TZ_PRJ_ID AND C.TZ_APP_INS_ID = ? ORDER BY C.OPRID LIMIT 1";
										strSiteId = sqlQuery.queryForObject(sql, new Object[] { strAppInsIdRefer },
												"String");
									}
								} else {
									strMessageError = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
											"TZGD_APPONLINE_MSGSET", "PARAERROR", strLanguage, "参数错误",
											"Parameter error.");
								}
							} else {
								strMessageError = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
										"TZGD_APPONLINE_MSGSET", "PARAERROR", strLanguage, "参数错误", "Parameter error.");
							}
							/*---如果报名表模版类型为推荐信End   ----*/
						} else {
							strMessageError = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
									"TZGD_APPONLINE_MSGSET", "TEMPLATEERROR", strLanguage, "没有找到对应的模版",
									"Could not find the corresponding template");
						}
					} else {
						// 没有找到对应的模版
						strMessageError = gdKjComServiceImpl.getMessageText(request, response, "TZGD_APPONLINE_MSGSET",
								"TEMPLATEERROR", "没有找到对应的模版", "Could not find the corresponding template");
					}

				} else {
					// 没有找到对应的模版
					strMessageError = gdKjComServiceImpl.getMessageText(request, response, "TZGD_APPONLINE_MSGSET",
							"TEMPLATEERROR", "没有找到对应的模版", "Could not find the corresponding template");
				}
			} else {
				// 没有对应的实例
				strMessageError = gdKjComServiceImpl.getMessageText(request, response, "TZGD_APPONLINE_MSGSET",
						"INSEERROR", "没有找到对应的实例", "Could not find the corresponding instance");
			}

		} 

		if ("".equals(strMessageError)) {
			//System.out.println("报名表展现数据处理Begin");
			strAppOrgId = psTzApptplDyTWithBLOBs.getTzJgId();
			strTplType = psTzApptplDyTWithBLOBs.getTzUseType();
			strTplData = psTzApptplDyTWithBLOBs.getTzApptplJsonStr();
			strLanguage = psTzApptplDyTWithBLOBs.getTzAppTplLan();
			strAfterSubmitUrl = psTzApptplDyTWithBLOBs.getTzAppTzurl();
			String showSubmitBtnOnly = psTzApptplDyTWithBLOBs.getTzOnlySubmitBtn();

			// 信息项Lebal左侧宽度
			String leftWidth = "";
			leftWidth = psTzApptplDyTWithBLOBs.getTzLeftWidth() == null ? ""
					: psTzApptplDyTWithBLOBs.getTzLeftWidth().toString();
			// 信息项输入框宽度
			String rightWidth = "";
			rightWidth = psTzApptplDyTWithBLOBs.getTzRightWidth() == null ? ""
					: psTzApptplDyTWithBLOBs.getTzRightWidth().toString();
			String leftWidthStyle = "";
			if (leftWidth != null && !"".equals(leftWidth) && !"0".equals(leftWidth)) {
				leftWidthStyle = "width:" + leftWidth + "%";
			}
			String rightWidthStyle = "";
			if (rightWidth != null && !"".equals(rightWidth) && !"0".equals(rightWidth)) {
				rightWidthStyle = "width:" + rightWidth + "%";
			}

			String strMenuId = "";

			sql = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT = ? LIMIT 1";
			strMenuId = sqlQuery.queryForObject(sql, new Object[] { "TZ_ACCOUNT_MANAGEMENT_" + strAppOrgId }, "String");
			if (strMenuId == null)
				strMenuId = "";

			// System.out.println("numAppInsId:"+numAppInsId);
			// System.out.println("strTplType:"+strTplType);
			long time2 = System.currentTimeMillis();
			System.out.println("报名表展现检查推荐信的完成状态Begin");
			if (numAppInsId > 0 && "BMB".equals(strTplType)) {
				// 检查推荐信的完成状态
				tzOnlineAppEngineImpl.checkRefletter(numAppInsId, strTplId);
			}
			System.out.println("报名表展现检查推荐信的完成状态End,Time=" + (System.currentTimeMillis() - time2));

			/*---执行页面加载事件-模版级事件开始 ----*/
			// 目前没有做处理，源代码请看tzOnlineAppServiceImplOld
			/*---执行页面加载事件-模版级事件结束 ----*/

			/*-----报名表菜单生成Begin--------------*/
			time2 = System.currentTimeMillis();
			System.out.println("报名表展现左侧菜单处理Begin");
			int numIndex = 0;
			String strXxxBh = "";
			// String strXxxMc = "";
			String strXxxTitle = "";
			String strDivClass = "";
			String strTabs = "";
			// 父分隔符号的id
			String strTZ_FPAGE_BH = "";

			int numChild = 0;

			sql = "SELECT A.TZ_XXX_BH,A.TZ_XXX_MC,A.TZ_TITLE,A.TZ_TAPSTYLE,A.TZ_FPAGE_BH,B.TZ_HAS_COMPLETE ";
			sql = sql
					+ "FROM PS_TZ_APP_XXXPZ_T A LEFT JOIN PS_TZ_APP_COMP_TBL B ON B.TZ_APP_INS_ID=? AND A.TZ_XXX_BH=B.TZ_XXX_BH ";
			sql = sql + "WHERE TZ_COM_LMC = 'Page' AND TZ_APP_TPL_ID = ? ORDER BY TZ_ORDER ASC";
			listData = sqlQuery.queryForList(sql, new Object[] { numAppInsId, strTplId });
			mapData = null;
			for (Object objDataTap : listData) {
				mapData = (Map<String, Object>) objDataTap;
				strXxxBh = mapData.get("TZ_XXX_BH") == null ? "" : String.valueOf(mapData.get("TZ_XXX_BH"));
				strXxxTitle = mapData.get("TZ_TITLE") == null ? "" : String.valueOf(mapData.get("TZ_TITLE"));
				strTZ_FPAGE_BH = mapData.get("TZ_FPAGE_BH") == null ? "" : String.valueOf(mapData.get("TZ_FPAGE_BH"));

				String strComplete = contextUrl + "/statics/images/appeditor/new/check.png"; // 对号
				numIndex = numIndex + 1;

				// 默认第一级菜单高亮
				if (strTZ_FPAGE_BH == null || strTZ_FPAGE_BH.trim().equals("")) {
					strDivClass = "menu-active-top";
				} else {

					if (strPageID == null || strPageID.equals("")) {
						numChild = numChild + 1;
						// 默认第一页高亮
						if (numChild == 1) {
							strDivClass = "menu-active";
						} else {
							strDivClass = "";
						}
					} else {
						if (strXxxBh.equals(strPageID)) {
							strDivClass = "menu-active";
						} else {
							strDivClass = "";
						}
					}
				}

				if ("Y".equals(strIsAdmin)) {
					strComplete = "";
					// 如果是管理员查看，不需要显示对号
				} else {
					String strPageComplete = "";
					if (numAppInsId > 0) {
						strPageComplete = mapData.get("TZ_HAS_COMPLETE") == null ? ""
								: String.valueOf(mapData.get("TZ_HAS_COMPLETE"));
						if (strPageComplete != null && "Y".equals(strPageComplete)) {
							// 已经完成的显示对号
						} else {
							// 未完成时,不显示对号
							strComplete = "";
						}
					} else {
						// 实例不存在时,不显示对号
						strComplete = "";
					}
				}

				try {
					strComplete = tzGdObject.getHTMLText("HTML.TZApplicationTemplateBundle.TZ_TABS_IMG", strComplete);
					strTabs = strTabs + tzGdObject.getHTMLText("HTML.TZApplicationTemplateBundle.TZ_TABS_DIV",
							strDivClass, strXxxTitle, strComplete, strXxxBh);
				} catch (TzSystemException e) {
					e.printStackTrace();
					strTabs = "";
				}
			}
			/*-----报名表菜单生成End--------------*/

			/* 如果页码大于1 则显示左侧，否则不显示左侧 */
			String strLeftStyle = "";
			String strRightStyle = "";
			if (numIndex <= 1) {
				strLeftStyle = "display:none";
				strRightStyle = "margin: 0 auto;float:none";
			}
			//System.out.println("报名表展现左侧菜单处理End,Time=" + (System.currentTimeMillis() - time2));

			//System.out.println("报名表展现获取控件信息处理Begin");
			time2 = System.currentTimeMillis();
			// 控件信息
			String strComRegInfo = "";
			ArrayList<Map<String, Object>> comDfn = templateEngine.getComDfn(strTplId);
			strComRegInfo = jacksonUtil.List2json(comDfn);
			strComRegInfo = strComRegInfo.replace("\\", "\\\\");
			//System.out.println("报名表展现获取控件信息处理End,Time=" + (System.currentTimeMillis() - time2));

			//System.out.println("报名表展现历史报名表处理Begin");
			time2 = System.currentTimeMillis();
			/*-----------最新历史报名表Begin------------- */
			Map<String, String> m = tzOnlineAppEngineImpl.getHistoryOnlineApp(strAppInsId, strCopyFrom, strAppOprId,
					strAppOrgId, strTplId, strAppOprId, strClassId, strRefLetterId, strInsData);
			;
			strAppInsId = m.get("strAppInsId");
			strInsData = m.get("strInsData");
			strRefLetterId = m.get("strRefLetterId");

			if (strRefLetterId == null || strRefLetterId.equals("null")) {
				strRefLetterId = "";
			}
			//System.out.println("strAppInsId:" + strAppInsId);
			//System.out.println("strRefLetterId:" + strRefLetterId);
			//System.out.println("报名表展现历史报名表处理End,Time=" + (System.currentTimeMillis() - time2));
			/*-----------最新历史报名表End------------- */

			if (strTplData == null || "".equals(strTplData)) {
				strTplData = "''";
			}

			if (strInsData == null || "".equals(strInsData)) {
				strInsData = "''";
			}

			// 双语化消息集合Json字符串
			// msgSet 用于双语
			//System.out.println("报名表展现双语化处理Begin");
			time2 = System.currentTimeMillis();
			String strMsgSet = "{}";
			strMsgSet = gdObjectServiceImpl.getMessageSetByLanguageCd(request, response, "TZGD_APPONLINE_MSGSET",
					strLanguage);
			jacksonUtil.json2Map(strMsgSet);
			if (jacksonUtil.containsKey(strLanguage)) {
				Map<String, Object> msgLang = jacksonUtil.getMap(strLanguage);
				strMsgSet = jacksonUtil.Map2json(msgLang);
			}
			// 获取个人基本信息
			String strUserInfoSet = "";
			strUserInfoSet = tzOnlineAppEngineImpl.getUserInfo(strAppInsId, strTplType);

			String strSave = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET", "SAVE",
					strLanguage, "保存", "Save");

			// 莫名其妙错误，做特殊处理
			if (strLanguage.equals("ENG")) {
				strSave = "Save";
			}

			String strSubmit = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
					"SUBMIT", strLanguage, "提交", "Submit");
			String strNext = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET", "NEXT",
					strLanguage, "下一步", "Next");
			String strPrev = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET", "PREV",
					strLanguage, "上一步", "Previous");
			String strLoading = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
					"LOADING", strLanguage, "上传中", "Loading");
			String strProcessing = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
					"PROCESS", strLanguage, "正在处理", "Processing");
			String strSubmitConfirmMsg = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
					"TZGD_APPONLINE_MSGSET", "SUBMITCONFIRMMSG", strLanguage, "我已阅读声明，确认提交报名表。",
					"I have read the statement to confirm the submission of the registration form.");

			String strDownLoadPDFMsg = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
					"DOWN", strLanguage, "下载报名表", "Download");

			String strDownErrorMsg = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
					"DOWNERR", strLanguage, "请先保存报名表", "Please save the application form。");

			//System.out.println("报名表展现双语化处理End,Time=" + (System.currentTimeMillis() - time2));

			if ("N".equals(strIsGuest)) {
				sql = "SELECT TZ_IS_GUEST FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID = ? AND OPRID = ?";
				try {
					strIsGuest = sqlQuery.queryForObject(sql, new Object[] { strClassId, strAppOprId }, "String");
				} catch (Exception e) {
					strIsGuest = "N";
				}
			}
			// 报名表头部信息
			String strOnlineHead = "";
			// 报名表底部信息
			String strOnlineFoot = "";
			// 报名表左侧
			String strOnlineLeft = "";

			String strMainInnerStyle = "";

			String strMainStyle = "";

			/* 根据站点查找页头区域 */
			// String sqlGetSiteYt = "";
			// String strSiteYtHtml = "";
			// String strLogoImg = "";

			if ("Y".equals(strIsAdmin)) {
				strMainInnerStyle = "margin: 0 auto;float:none";
				strMainStyle = "width:788px;";
			} else {
			}

			if ("".equals(strAppInsId) || strAppInsId == null) {
				strAppInsId = "0";
			}

			//System.out.println("strAppInsId:" + strAppInsId);

			// 非匿名报名时，如果当前登录人为管理员、并且可编辑，同时报名表只读参数为Y时，将只读参数改为N
			if (!StringUtils.equals("Y", strIsGuest) && StringUtils.equals("Y", strIsAdmin)
					&& StringUtils.equals("Y", strIsEdit) && StringUtils.equals("Y", strAppFormReadOnly)) {
				strAppFormReadOnly = "N";
			}

			try {

				//System.out.println("报名表展现密码处理Begin");
				time2 = System.currentTimeMillis();
				String passWordHtml = "";

				String setPwdId = "setPwd";
				String setPwd2Id = "setPwd2";
				String pwdTitleDivId = "PwdTitleDiv";
				String pwdDivId = "setPwdDiv";
				String pwdDivId2 = "setPwdDiv2";
				// 推荐信 密码设置控制 add by caoy 2017-1-22 strIsAdmin
				if ("TJX".equals(strTplType)) {

					if (strTJXIsPwd.equals("Y")) {
						// 密码如果不存在 需要设置 密码
						if (strTJXPwd == null || strTJXPwd.equals("")) {
							String pwdTitle = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
									"TZGD_APPONLINE_MSGSET", "TJXPWDTITLE", strLanguage,
									"设置访问密码(必须设置密码才能正式提交,设置密码后此页面将只能通过密码访问)",
									"Set the access password (password must be set to be formally submitted, the password will only be accessed by password)");
							String setPwd = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
									"TZGD_APPONLINE_MSGSET", "TJXSETPWD", strLanguage, "设置密码", "Set Password");
							String setPwd2 = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
									"TZGD_APPONLINE_MSGSET", "TJXSETPWD2", strLanguage, "重新输入密码", "Re Password");

							passWordHtml = tzGdObject.getHTMLText(
									"HTML.TZWebsiteApplicationBundle.TZ_ONLINE_PAGE_PWD_HTML", false, pwdTitle, setPwd,
									setPwd2, setPwdId, setPwd2Id, pwdDivId, pwdDivId2);
						}
					}

					if (strTJXPwd != null && !strTJXPwd.equals("") && strTJXIsPwd.equals("Y")) {
						strTJXIsPwd = "Y";
					} else {
						strTJXIsPwd = "N";
					}
				} else {
					strTJXIsPwd = "N";
				}

				strTplData = strTplData.replace("\\", "\\\\");
				//strTplData = strTplData.replace("$", "\\$");

				Pattern CRLF = Pattern.compile("(\r\n|\r|\n|\n\r)");
				Matcher mc = CRLF.matcher(strInsData);
				if (mc.find()) {
					strInsData = mc.replaceAll("\\\\n");
				}
				strInsData = strInsData.replace("\\", "\\\\");
				
				//strInsData = strInsData.replace("$", "\\$");
				// 处理HTML换行符号，是替换的\u2028;
				strInsData = strInsData.replace(" ", "");
				String pwdError = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
						"TJXSETPWDError", strLanguage, "请填写密码", "Please fill in the password");

				String pwdError2 = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
						"TJXSETPWDError", strLanguage, "密码和确认密码不一致", "Password and confirm password inconsistent");

				String Pwdname = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
						"TJXSETPWD", strLanguage, "访问密码", "Access password");
				String strSubmit2 = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
						"CONFIRM", strLanguage, "确认", "Confirm");

				// 构建密码输入框
				String PWDHTML = tzGdObject.getHTMLText("HTML.TZWebsiteApplicationBundle.TZ_ONLINE_PWD_HTML", false,
						Pwdname, strSubmit2, contextUrl);

				//System.out.println("报名表展现密码处理End,Time=" + (System.currentTimeMillis() - time2));

				//System.out.println("报名表展现构造HTML页面Begin");
				time2 = System.currentTimeMillis();
				str_appform_main_html = tzGdObject.getHTMLText("HTML.TZReferenceMaterialBundle.TZ_REF_APP_FORM_HTML",
						false, strTzGeneralURL, strComRegInfo, strTplId, strAppInsId, strClassId, strRefLetterId,
						strTplData, strInsData, strTabs, strSiteId, strAppOrgId, strMenuId, strAppFormReadOnly,
						strMsgSet, strLanguage, strSave, strNext, strSubmit, strTplType, strLoading, strProcessing,
						strAfterSubmitUrl, strOnlineHead, strOnlineFoot, strOnlineLeft, strIsAdmin, strMainInnerStyle,
						strUserInfoSet, strMainStyle, strPrev, strAppInsVersion, contextUrl, leftWidthStyle,
						rightWidthStyle, strLeftStyle, strRightStyle, showSubmitBtnOnly, strSubmitConfirmMsg, strIsEdit,
						strBatchId, strTJXIsPwd, passWordHtml, setPwdId, setPwd2Id, pwdTitleDivId, pwdDivId, pwdDivId2,
						pwdError, pwdError2, PWDHTML, strDownLoadPDFMsg, strDownErrorMsg, classProjectID);
				//System.out.println("报名表展现构造HTML页面End,Time=" + (System.currentTimeMillis() - time2));
				time2 = System.currentTimeMillis();
				//System.out.println("报名表展现替换HTML页面Begin");
				str_appform_main_html = siteRepCssServiceImpl.repTitle(str_appform_main_html, strSiteId);
				str_appform_main_html = siteRepCssServiceImpl.repCss(str_appform_main_html, strSiteId);
				//System.out.println("报名表展现替换HTML页面End,Time=" + (System.currentTimeMillis() - time2));
			} catch (TzSystemException e) {
				e.printStackTrace();
			}

			//System.out.println("报名表展现数据处理End,Time=" + (System.currentTimeMillis() - time));

		} else {
			str_appform_main_html = strMessageError;
		}

		return str_appform_main_html;
	}
	
}
