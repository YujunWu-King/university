package com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.TZAccountMgBundle.dao.PsTzAqYhxxTblMapper;
import com.tranzvision.gd.TZAccountMgBundle.dao.PsoprdefnMapper;
import com.tranzvision.gd.TZAccountMgBundle.dao.PsroleuserMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTbl;
import com.tranzvision.gd.TZAccountMgBundle.model.Psoprdefn;
import com.tranzvision.gd.TZAccountMgBundle.model.Psroleuser;

import com.tranzvision.gd.TZApplicationTemplateBundle.service.impl.TemplateEngine;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdKjComServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppInsTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppInsT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzFormWrkTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzKsTjxTblMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormWrkT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzKsTjxTbl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppDhhsTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppDhhsT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppCcTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppCcT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppDhccTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppDhccT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppHiddenTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppHiddenT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzFormAttTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormAttT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzFormPhotoTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormPhotoT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppCompTblMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppCompTbl;

import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl.tzOnlineAppViewServiceImpl;
import com.tranzvision.gd.TZRecommendationBundle.service.impl.TzTjxThanksServiceImpl;

import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzLxfsInfoTblMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzOprPhotoTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzRegUserTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzLxfsInfoTbl;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzOprPhotoT;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzRegUserT;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteEnrollClsServiceImpl;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.encrypt.Sha3DesMD5;
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
public class tzOnlineAppServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private GdKjComServiceImpl gdKjComServiceImpl;
	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;
	@Autowired
	private SiteRepCssServiceImpl siteRepCssServiceImpl;
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;
	@Autowired
	private tzOnlineAppViewServiceImpl tzOnlineAppViewServiceImpl;
	@Autowired
	private TemplateEngine templateEngine;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private PsTzAppInsTMapper psTzAppInsTMapper;
	@Autowired
	private PsTzFormWrkTMapper psTzFormWrkTMapper;
	@Autowired
	private PsTzAppDhhsTMapper psTzAppDhhsTMapper;
	@Autowired
	private PsTzAppDhccTMapper psTzAppDhccTMapper;
	@Autowired
	private PsTzAppCcTMapper psTzAppCcTMapper;
	@Autowired
	private PsTzAppHiddenTMapper psTzAppHiddenTMapper;
	@Autowired
	private PsTzFormAttTMapper psTzFormAttTMapper;
	@Autowired
	private PsTzFormPhotoTMapper psTzFormPhotoTMapper;
	@Autowired
	private PsTzAppCompTblMapper psTzAppCompTblMapper;
	@Autowired
	private PsTzLxfsInfoTblMapper psTzLxfsInfoTblMapper;
	@Autowired
	private TZGDObject tzSQLObject;
	@Autowired
	private ApplicationContext ctx;
	@Autowired
	private SiteEnrollClsServiceImpl SiteEnrollClsServiceImpl;
	@Autowired
	private PsoprdefnMapper psoprdefnMapper;
	@Autowired
	private PsTzAqYhxxTblMapper psTzAqYhxxTblMapper;
	@Autowired
	private PsTzRegUserTMapper psTzRegUserTMapper;
	@Autowired
	private PsroleuserMapper psroleuserMapper;
	@Autowired
	private TzTjxThanksServiceImpl tzTjxThanksServiceImpl;
	@Autowired
	private tzOnlineAppHisServiceImpl tzOnlineAppHisServiceImpl;
	@Autowired
	private PsTzOprPhotoTMapper psTzOprPhotoTMapper;
	@Autowired
	private PsTzKsTjxTblMapper psTzKsTjxTblMapper;

	@Autowired
	private tzOnlineAppRulesImpl tzOnlineAppRulesImpl;

	/* 报名表展示 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzGetHtmlContent(String comParams) {

		// 返回值;
		String str_appform_main_html = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		// 组件注册引用编号
		String strReferenceId = request.getParameter("classid");

		String contextUrl = request.getContextPath();
		String strTzGeneralURL = contextUrl + "/dispatcher";

		// 当前登陆人
		String oprid = "";
		oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
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

		if ("appId".equals(strReferenceId)) {
			strClassId = request.getParameter("TZ_CLASS_ID");
			strSiteId = request.getParameter("SITE_ID");
			strAppInsId = request.getParameter("TZ_APP_INS_ID");
			strRefLetterId = request.getParameter("TZ_REF_LETTER_ID");
			strManagerView = request.getParameter("TZ_MANAGER");
			strCopyFrom = request.getParameter("APPCOPY");
			strAttachedTplId = request.getParameter("TZ_APP_TPL_ID");
			strIsEdit = request.getParameter("isEdit");
			if (strClassId == null) {
				strClassId = "";
			}

		} else {
			strClassId = String.valueOf(jacksonUtil.getString("TZ_CLASS_ID"));
			strSiteId = String.valueOf(jacksonUtil.getString("SITE_ID"));
			strAppInsId = String.valueOf(jacksonUtil.getString("TZ_APP_INS_ID"));
			strRefLetterId = String.valueOf(jacksonUtil.getString("TZ_REF_LETTER_ID"));
			strManagerView = String.valueOf(jacksonUtil.getString("TZ_MANAGER"));
			strCopyFrom = String.valueOf(jacksonUtil.getString("APPCOPY"));
			strAttachedTplId = String.valueOf(jacksonUtil.getString("TZ_APP_TPL_ID"));
			strIsEdit = String.valueOf(jacksonUtil.getString("isEdit"));
			if (strClassId == null) {
				strClassId = "";
			}
		}

		if (strRefLetterId == null)
			strRefLetterId = "";
		if (strManagerView == null)
			strManagerView = "";
		if (strCopyFrom == null)
			strCopyFrom = "N";

		if ("".equals(strAppInsId) || strAppInsId == null) {
			numAppInsId = 0L;
		} else {
			numAppInsId = Long.parseLong(strAppInsId);
		}

		if (numAppInsId > 0) {
			// 如果存在报名表实例
			PsTzAppInsT PsTzAppInsT = psTzAppInsTMapper.selectByPrimaryKey(numAppInsId);
			if (PsTzAppInsT != null) {
				strTplId = PsTzAppInsT.getTzAppTplId();
				strAppInsState = PsTzAppInsT.getTzAppFormSta();
				strAppInsVersion = PsTzAppInsT.getTzAppInsVersion();
				strInsData = PsTzAppInsT.getTzAppinsJsonStr();
				strTJXPwd = PsTzAppInsT.getTzPwd();
				if (strTplId == null)
					strTplId = "";
				if (strAppInsState == null)
					strAppInsState = "N";
				if (strAppInsVersion == null)
					strAppInsVersion = "";
			}
			if (!"".equals(strTplId) && strTplId != null) {
				// 查看是否是查看附属模版 Start
				if (!"".equals(strAttachedTplId) && strAttachedTplId != null) {
					String sqlExistsZfFlag = "SELECT 'Y' FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ? AND TZ_APP_M_TPL_ID = ? LIMIT 1";
					String strExistsZfFlag = sqlQuery.queryForObject(sqlExistsZfFlag,
							new Object[] { strTplId, strAttachedTplId }, "String");
					if ("Y".equals(strExistsZfFlag)) {
						strTplId = strAttachedTplId;
						// 根据报名表实例和附属模版编号去获得报名表Json数据
						strInsData = tzOnlineAppViewServiceImpl.getHisAppInfoJson(numAppInsId, strTplId);
						strIsAdmin = "Y";
						strAppFormReadOnly = "Y";
					}
				}

				// 查看是否是查看附属模版 end

				// 如果报名表已提交，则只读显示
				if ("U".equals(strAppInsState)) {
					strAppFormReadOnly = "Y";
				}

				String sqlGetAppTplInfo = "SELECT TZ_APP_TPL_LAN,TZ_USE_TYPE,TZ_JG_ID,TZ_PWD_TYPE FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ?";
				Map<String, Object> MapAppTplInfo = sqlQuery.queryForMap(sqlGetAppTplInfo, new Object[] { strTplId });
				strLanguage = MapAppTplInfo.get("TZ_APP_TPL_LAN") == null ? ""
						: String.valueOf(MapAppTplInfo.get("TZ_APP_TPL_LAN"));
				strTplType = MapAppTplInfo.get("TZ_USE_TYPE") == null ? ""
						: String.valueOf(MapAppTplInfo.get("TZ_USE_TYPE"));
				strAppOrgId = MapAppTplInfo.get("TZ_JG_ID") == null ? ""
						: String.valueOf(MapAppTplInfo.get("TZ_JG_ID"));
				strTJXIsPwd = MapAppTplInfo.get("TZ_PWD_TYPE") == null ? "N"
						: String.valueOf(MapAppTplInfo.get("TZ_PWD_TYPE"));

				// 如果报名表模版类型为报名表
				if ("BMB".equals(strTplType)) {
					String sqlGetFormWorkInfo = "SELECT OPRID,TZ_CLASS_ID,TZ_BATCH_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID = ? ORDER BY OPRID";
					Map<String, Object> MapFormWorkInfo = sqlQuery.queryForMap(sqlGetFormWorkInfo,
							new Object[] { strAppInsId });
					if (MapFormWorkInfo != null) {
						strAppOprId = String.valueOf(MapFormWorkInfo.get("OPRID"));
						strClassId = String.valueOf(MapFormWorkInfo.get("TZ_CLASS_ID"));
						strBatchId = String.valueOf(MapFormWorkInfo.get("TZ_BATCH_ID"));
						if ("".equals(strSiteId) || strSiteId == null) {
							// 如果没有传入siteId，则取班级对应的站点
							String sqlGetSiteId = "select TZ_SITEI_ID from PS_TZ_CLASS_INF_T A,PS_TZ_PROJECT_SITE_T B where A.TZ_CLASS_ID=? AND A.TZ_PRJ_ID = B.TZ_PRJ_ID LIMIT 1";
							strSiteId = sqlQuery.queryForObject(sqlGetSiteId, new Object[] { strClassId }, "String");
						}

						if ("TZ_GUEST".equals(strAppOprId) || "".equals(strAppOprId)) {
							// 如果是匿名报名
							String sqlGetIsGuest = "SELECT TZ_GUEST_APPLY FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID = ?";
							strIsGuest = sqlQuery.queryForObject(sqlGetIsGuest, new Object[] { strClassId }, "String");
							if (!"Y".equals(strIsGuest)) {
								strMessageError = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
										"TZGD_APPONLINE_MSGSET", "SESSION_INVAILD", strLanguage, "当前会话已失效，请重新登陆。",
										"The current session is timeout or the current access is invalid,Please relogin.");
							}
						} else {
							if (oprid.equals(strAppOprId)) {
								// 自己操作自己的报名表
							} else {
								// 看是否管理员查看报名表
								strIsAdmin = "";
								strIsAdmin = this.checkAppViewQx(strTplId, oprid, strAppOrgId, strClassId);
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
					} else {
						strMessageError = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
								"TZGD_APPONLINE_MSGSET", "ILLEGAL_OPERATION", strLanguage, "非法操作",
								"Illegal operation.");
					}

				} else if ("TJX".equals(strTplType)) {
					// 是否有推荐信编号
					// 如果是推荐信，1.检查 是否推荐信模版里面 是否设置了密码，如果设置了密码，检查 推荐性实例，看看密码是否已经填写
					// 如果填写 ，那么推荐人进入的时候首先填写密码，如果没有填写，进入报名表的时候 可以填写密码(需要填写2次)
					if (!"".equals(strRefLetterId) && strRefLetterId != null) {

						// strAppInsId 该推荐信实例ID
						// strRefLetterId 该推荐信唯一ID

						if ("Y".equals(strManagerView)) {
							strIsAdmin = "Y";
							strAppFormReadOnly = "Y";
						}
						// 查询推荐信报名表编号
						String sqlGetKsTjxInfo = "SELECT TZ_APP_INS_ID,OPRID FROM PS_TZ_KS_TJX_TBL WHERE TZ_REF_LETTER_ID = ? AND TZ_TJX_APP_INS_ID = ?";
						Map<String, Object> MapKsTjxInfo = sqlQuery.queryForMap(sqlGetKsTjxInfo,
								new Object[] { strRefLetterId, strAppInsId });
						strAppInsIdRefer = String.valueOf(MapKsTjxInfo.get("TZ_APP_INS_ID"));
						strAppOprId = String.valueOf(MapKsTjxInfo.get("OPRID"));
						Long numAppInsIdRefer = Long.parseLong(strAppInsIdRefer);
						if (numAppInsIdRefer > 0) {
							// 找到有效的被推荐人
							// 获取推荐信对应的报名表
							if ("".equals(strSiteId) || strSiteId == null) {
								// 如果没有传入siteId，则取班级对应的站点
								String sqlGetSiteId = "select TZ_SITEI_ID from PS_TZ_CLASS_INF_T A,PS_TZ_PROJECT_SITE_T B,PS_TZ_FORM_WRK_T C"
										+ " where A.TZ_CLASS_ID=C.TZ_CLASS_ID AND A.TZ_PRJ_ID = B.TZ_PRJ_ID AND C.TZ_APP_INS_ID = ? ORDER BY C.OPRID LIMIT 1";
								strSiteId = sqlQuery.queryForObject(sqlGetSiteId, new Object[] { strAppInsIdRefer },
										"String");
							}
						} else {
							strMessageError = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
									"TZGD_APPONLINE_MSGSET", "PARAERROR", strLanguage, "参数错误", "Parameter error.");
						}
					} else {
						strMessageError = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
								"TZGD_APPONLINE_MSGSET", "PARAERROR", strLanguage, "参数错误", "Parameter error.");
					}
				} else {
					strMessageError = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
							"TEMPLATEERROR", strLanguage, "没有找到对应的模版", "Could not find the corresponding template");
				}
				// str_appform_main_html = "报名表语言"+ strLanguage +
				// "报名表类型"+strTplType + "";
			} else {
				// 没有找到对应的模版
				strMessageError = gdKjComServiceImpl.getMessageText(request, response, "TZGD_APPONLINE_MSGSET",
						"TEMPLATEERROR", "没有找到对应的模版", "Could not find the corresponding template");
			}
		} else {
			// 如果没有报名表实例编号，看是否有班级编号
			if (!"".equals(strClassId) && strClassId != null) {
				// 班级是否开放报名
				String sql = "SELECT TZ_IS_APP_OPEN FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID = ?";
				String strClassIsOpen = sqlQuery.queryForObject(sql, new Object[] { strClassId }, "String");
				if ("Y".equals(strClassIsOpen)) {
					// 班级开放报名
					if ("TZ_GUEST".equals(oprid) || "".equals(oprid)) {
						// 如果是Guest用户，看班级是否允许匿名报名
						sql = "SELECT TZ_GUEST_APPLY FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID = ?";
						String strGuestApply = sqlQuery.queryForObject(sql, new Object[] { strClassId }, "String");
						if ("Y".equals(strGuestApply)) {
							sql = "SELECT TZ_APP_MODAL_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_IS_APP_OPEN = 'Y' AND TZ_CLASS_ID = ?";
							strTplId = sqlQuery.queryForObject(sql, new Object[] { strClassId }, "String");
							if ("".equals(strTplId) || strTplId == null) {
								strMessageError = gdKjComServiceImpl.getMessageText(request, response,
										"TZGD_APPONLINE_MSGSET", "PARAERROR", "参数错误", "Parameter error");
							}
							strIsGuest = "Y";
						} else {
							sql = "SELECT TZ_APP_MODAL_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_IS_APP_OPEN = 'Y' AND TZ_CLASS_ID = ?";
							strTplId = sqlQuery.queryForObject(sql, new Object[] { strClassId }, "String");
							if ("".equals(strTplId) || strTplId == null) {
								strMessageError = gdKjComServiceImpl.getMessageText(request, response,
										"TZGD_APPONLINE_MSGSET", "PARAERROR", "参数错误", "Parameter error");
							}
							strMessageError = gdKjComServiceImpl.getMessageText(request, response,
									"TZGD_APPONLINE_MSGSET", "SESSION_INVAILD", "当前会话已失效，请重新登陆。",
									"The current session is timeout or the current access is invalid,Please relogin.");
						}
					} else {
						// 是注册用户在线报名
						sql = "SELECT TZ_APP_INS_ID,TZ_BATCH_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID = ? AND TZ_CLASS_ID = ?";
						Map<String, Object> MapFormWorkInfo = sqlQuery.queryForMap(sql,
								new Object[] { oprid, strClassId });
						if (MapFormWorkInfo != null) {
							strAppInsId = String.valueOf(MapFormWorkInfo.get("TZ_APP_INS_ID"));
							strBatchId = String.valueOf(MapFormWorkInfo.get("TZ_BATCH_ID"));
						}
						// strAppInsId = sqlQuery.queryForObject(sql, new
						// Object[] { oprid, strClassId }, "String");

						strAppOprId = oprid;
						if (!"".equals(strAppInsId) && strAppInsId != null) {
							numAppInsId = Long.parseLong(strAppInsId);
						} else {
							numAppInsId = 0L;
						}

						if (numAppInsId > 0) {
							// 传入了报名表实例
							PsTzAppInsT psTzAppInsT = psTzAppInsTMapper.selectByPrimaryKey(numAppInsId);
							if (psTzAppInsT != null) {
								strTplId = psTzAppInsT.getTzAppTplId();
								strAppInsState = psTzAppInsT.getTzAppFormSta();
								strAppInsVersion = psTzAppInsT.getTzAppInsVersion();
								strInsData = psTzAppInsT.getTzAppinsJsonStr();
								if (strAppInsVersion == null) {
									strAppInsVersion = "";
								}

								if ("".equals(strTplId) || strTplId == null) {
									strMessageError = gdKjComServiceImpl.getMessageText(request, response,
											"TZGD_APPONLINE_MSGSET", "PARAERROR", "参数错误", "Parameter error");
								}
								// 如果报名表已提交，则只读显示
								if ("U".equals(strAppInsState)) {
									strAppFormReadOnly = "Y";
								}
							} else {
								strMessageError = gdKjComServiceImpl.getMessageText(request, response,
										"TZGD_APPONLINE_MSGSET", "PARAERROR", "参数错误", "Parameter error");
							}

						} else {
							sql = "SELECT TZ_APP_MODAL_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_IS_APP_OPEN = 'Y' AND TZ_CLASS_ID = ?";
							strTplId = sqlQuery.queryForObject(sql, new Object[] { strClassId }, "String");
							if ("".equals(strTplId) || strTplId == null) {
								strMessageError = gdKjComServiceImpl.getMessageText(request, response,
										"TZGD_APPONLINE_MSGSET", "PARAERROR", "参数错误", "Parameter error");
							}
						}
					}
				} else {
					strMessageError = gdKjComServiceImpl.getMessageText(request, response, "TZGD_APPONLINE_MSGSET",
							"NOT_OPEN", "当前班级未开通在线报名", "Not open the online registration.");
				}
			} else {
				// 没有找到对应的模版
				strMessageError = gdKjComServiceImpl.getMessageText(request, response, "TZGD_APPONLINE_MSGSET",
						"PARAERROR", "参数错误", "Parameter error");
			}
		}

		// 如果存在报名表模版
		String sql = "";
		sql = "SELECT TZ_APPTPL_JSON_STR,TZ_USE_TYPE,TZ_JG_ID,TZ_APP_TPL_LAN,TZ_APP_TZURL,TZ_LEFT_WIDTH,TZ_RIGHT_WIDTH,TZ_ONLY_SUBMIT_BTN FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ?";
		Map<String, Object> MapAppTplInfo = sqlQuery.queryForMap(sql, new Object[] { strTplId });
		strAppOrgId = MapAppTplInfo.get("TZ_JG_ID") == null ? "" : String.valueOf(MapAppTplInfo.get("TZ_JG_ID"));
		strTplType = MapAppTplInfo.get("TZ_USE_TYPE") == null ? "" : String.valueOf(MapAppTplInfo.get("TZ_USE_TYPE"));
		strTplData = MapAppTplInfo.get("TZ_APPTPL_JSON_STR") == null ? ""
				: String.valueOf(MapAppTplInfo.get("TZ_APPTPL_JSON_STR"));
		strLanguage = MapAppTplInfo.get("TZ_APP_TPL_LAN") == null ? ""
				: String.valueOf(MapAppTplInfo.get("TZ_APP_TPL_LAN"));
		strAfterSubmitUrl = MapAppTplInfo.get("TZ_APP_TZURL") == null ? ""
				: String.valueOf(MapAppTplInfo.get("TZ_APP_TZURL"));
		String showSubmitBtnOnly = MapAppTplInfo.get("TZ_ONLY_SUBMIT_BTN") == null ? ""
				: String.valueOf(MapAppTplInfo.get("TZ_ONLY_SUBMIT_BTN"));

		// 信息项Lebal左侧宽度
		String leftWidth = "";
		leftWidth = MapAppTplInfo.get("TZ_LEFT_WIDTH") == null ? ""
				: String.valueOf(MapAppTplInfo.get("TZ_LEFT_WIDTH"));
		// 信息项输入框宽度
		String rightWidth = "";
		rightWidth = MapAppTplInfo.get("TZ_RIGHT_WIDTH") == null ? ""
				: String.valueOf(MapAppTplInfo.get("TZ_RIGHT_WIDTH"));

		String leftWidthStyle = "";
		if (leftWidth != null && !"".equals(leftWidth) && !"0".equals(leftWidth)) {
			leftWidthStyle = "width:" + leftWidth + "%";
		}
		String rightWidthStyle = "";
		if (rightWidth != null && !"".equals(rightWidth) && !"0".equals(rightWidth)) {
			rightWidthStyle = "width:" + rightWidth + "%";
		}

		// 获得站点信息
		// sql = "SELECT TZ_SITEI_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_JG_ID = ?
		// AND TZ_SITEI_ENABLE = 'Y' LIMIT 1";
		// 站点编号
		// String strSiteId = "";
		// strSiteId = sqlQuery.queryForObject(sql, new Object[] { strAppOrgId
		// }, "String");

		String strMenuId = "";

		sql = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT = ? LIMIT 1";
		strMenuId = sqlQuery.queryForObject(sql, new Object[] { "TZ_ACCOUNT_MANAGEMENT_" + strAppOrgId }, "String");
		if (strMenuId == null)
			strMenuId = "";

		String strTest = "";
		if ("".equals(strMessageError)) {
			if (numAppInsId > 0 && "BMB".equals(strTplType)) {
				// 检查推荐信的完成状态
				this.checkRefletter(numAppInsId, strTplId);
			}
			// 执行页面加载事件-模版级事件开始
			// 模版级事件
			String sqlGetModalEvents = "SELECT CMBC_APPCLS_PATH,CMBC_APPCLS_NAME,CMBC_APPCLS_METHOD FROM PS_TZ_APP_EVENTS_T WHERE TZ_APP_TPL_ID = ? AND TZ_EVENT_TYPE = 'LO_A'";
			List<?> listGetModalEvents = sqlQuery.queryForList(sqlGetModalEvents, new Object[] { strTplId });
			for (Object objDataGetModalEvents : listGetModalEvents) {
				Map<String, Object> MapGetModalEvents = (Map<String, Object>) objDataGetModalEvents;
				String strAppClassPath = "";
				String strAppClassName = "";
				String strAppClassMethod = "";
				String strEventReturn = "";
				strAppClassPath = MapGetModalEvents.get("CMBC_APPCLS_PATH") == null ? ""
						: String.valueOf(MapGetModalEvents.get("CMBC_APPCLS_PATH"));
				strAppClassName = MapGetModalEvents.get("CMBC_APPCLS_NAME") == null ? ""
						: String.valueOf(MapGetModalEvents.get("CMBC_APPCLS_NAME"));
				strAppClassMethod = MapGetModalEvents.get("CMBC_APPCLS_METHOD") == null ? ""
						: String.valueOf(MapGetModalEvents.get("CMBC_APPCLS_METHOD"));
				if (!"".equals(strAppClassPath) && !"".equals(strAppClassName) && !"".equals(strAppClassMethod)) {
					/*
					 * String[] parameterTypes = new String[] {"String[]" };
					 * Object[] arglist = new Object[] { numAppInsId ,strClassId
					 * , strAppOprId}; Object objs =
					 * ObjectDoMethod.Load(strAppClassPath + "." +
					 * strAppClassName, strAppClassMethod, parameterTypes,
					 * arglist); strEventReturn = String.valueOf(objs);
					 */
					// 根据配置需要去调用对应的程序
					tzOnlineAppEventServiceImpl tzOnlineAppEventServiceImpl = (tzOnlineAppEventServiceImpl) ctx
							.getBean(strAppClassPath + "." + strAppClassName);
					switch (strAppClassMethod) {
					// 根据报名表配置的方法名称去调用不同的方法
					}
				}
			}

			// 执行页面加载事件-模版级事件结束

			// 报名表Tab页签展示，modity by caoy
			int numIndex = 0;
			String strXxxBh = "";
			// String strXxxMc = "";
			String strXxxTitle = "";
			String strTapStyle = "";
			String strDivClass = "";
			String strTabs = "";
			// 父分隔符号的id
			String strTZ_FPAGE_BH = "";

			int numChild = 0;

			String sqlGetTapInfo = "SELECT TZ_XXX_BH,TZ_XXX_MC,TZ_TITLE,TZ_TAPSTYLE,TZ_FPAGE_BH FROM PS_TZ_APP_XXXPZ_T WHERE TZ_COM_LMC = 'Page' AND TZ_APP_TPL_ID = ? ORDER BY TZ_ORDER ASC";
			List<?> listDataTap = sqlQuery.queryForList(sqlGetTapInfo, new Object[] { strTplId });
			for (Object objDataTap : listDataTap) {
				Map<String, Object> mapDataTap = (Map<String, Object>) objDataTap;
				strXxxBh = mapDataTap.get("TZ_XXX_BH") == null ? "" : String.valueOf(mapDataTap.get("TZ_XXX_BH"));
				strXxxTitle = mapDataTap.get("TZ_TITLE") == null ? "" : String.valueOf(mapDataTap.get("TZ_TITLE"));
				strTapStyle = mapDataTap.get("TZ_TAPSTYLE") == null ? ""
						: String.valueOf(mapDataTap.get("TZ_TAPSTYLE"));
				strTZ_FPAGE_BH = mapDataTap.get("TZ_FPAGE_BH") == null ? ""
						: String.valueOf(mapDataTap.get("TZ_FPAGE_BH"));

				String strComplete = contextUrl + "/statics/images/appeditor/new/check.png"; // 对号
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

				System.out.println(strXxxBh + ":" + strDivClass);

				if ("Y".equals(strIsAdmin)) {
					strComplete = "";
					// 如果是管理员查看，不需要显示对号
				} else {
					String strPageComplete = "";
					if (numAppInsId > 0) {
						sql = "SELECT TZ_HAS_COMPLETE FROM PS_TZ_APP_COMP_TBL WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
						strPageComplete = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strXxxBh },
								"String");
						if ("Y".equals(strPageComplete)) {
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
					System.out.println("strTabs:" + strTabs);
					// if(StringUtils.isNotBlank(strComplete)){}
					strComplete = tzGdObject.getHTMLText("HTML.TZApplicationTemplateBundle.TZ_TABS_IMG", strComplete);
					System.out.println("strComplete:" + strComplete);
					strTabs = strTabs + tzGdObject.getHTMLText("HTML.TZApplicationTemplateBundle.TZ_TABS_DIV",
							strDivClass, strXxxTitle, strComplete, strXxxBh);
					System.out.println("strTabs:" + strTabs);
				} catch (TzSystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					strTabs = "";
				}
			}

			/* 如果页码大于1 则显示左侧，否则不显示左侧 */
			String strLeftStyle = "";
			String strRightStyle = "";
			if (numIndex <= 1) {
				strLeftStyle = "display:none";
				strRightStyle = "margin: 0 auto;float:none";
			}

			// 控件信息
			String strComRegInfo = "";
			ArrayList<Map<String, Object>> comDfn = templateEngine.getComDfn(strTplId);
			strComRegInfo = jacksonUtil.List2json(comDfn);
			strComRegInfo = strComRegInfo.replace("\\", "\\\\");

			/* 最新历史报名表实例编号 */
			String strHisAppInsId = "";

			Long numHisAppInsId = 0l;

			if ("".equals(strAppInsId) || strAppInsId == null) {
				if ("Y".equals(strCopyFrom)) {
					String sqlGetHisAppInsId = "SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T A ,PS_TZ_CLASS_INF_T B "
							+ "WHERE A.TZ_CLASS_ID = B.TZ_CLASS_ID AND A.OPRID = ? AND B.TZ_JG_ID = ? ORDER BY A.ROW_ADDED_DTTM DESC limit 0,1";
					strHisAppInsId = sqlQuery.queryForObject(sqlGetHisAppInsId,
							new Object[] { strAppOprId, strAppOrgId }, "String");
					if (!"".equals(strHisAppInsId) && strHisAppInsId != null) {
						numHisAppInsId = Long.parseLong(strHisAppInsId);
						strInsData = tzOnlineAppHisServiceImpl.getHisAppInfoJson(numHisAppInsId, strTplId);
						// strInsData =
						// tzOnlineAppViewServiceImpl.getHisAppInfoJson(numHisAppInsId,
						// strTplId);
						// -------------复制推荐信

						// ---0.去年的推荐信不为空，则进行“复制推荐信”操作
						sql = "SELECT TZ_APP_TPL_ID FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID = ?";
						String tempId = sqlQuery.queryForObject(sql, new Object[] { numAppInsId }, "String");

						sql = "SELECT TZ_USE_TYPE FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ?";
						String useType = sqlQuery.queryForObject(sql, new Object[] { tempId }, "String");
						if ("TJX".equals(useType)) {
							final String LAST_LETTER_SQL = "SELECT * FROM PS_TZ_KS_TJX_TBL WHERE TZ_TJX_APP_INS_ID = ?";
							List<Map<String, Object>> LetterList = new ArrayList<Map<String, Object>>();
							LetterList = sqlQuery.queryForList(LAST_LETTER_SQL);
							// 将所有的推荐信 信息复制过去 考生推荐信表
							if (LetterList != null) {
								// ---1.向"报名表实例表"，加入报名表实例信息PS_TZ_APP_INS_T
								Long tzAppInsId = Long.valueOf(getSeqNum.getSeqNum("PS_TZ_APP_INS_T", "TZ_APP_INS_ID"));
								PsTzAppInsT psTzAppInsT = new PsTzAppInsT();
								psTzAppInsT.setTzAppInsId(tzAppInsId);// 报名表实例ID
																		// 新生成的
								psTzAppInsT.setRowAddedOprid(oprid); // 当前用户ID
								psTzAppInsT.setTzAppTplId(strTplId);// 当前报名表模板ID
								psTzAppInsT.setTzAppinsJsonStr(strInsData);// 从历史表中即将推送到前台的json，存入报名表实例表中
								psTzAppInsT.setTzAppFormSta("S");
								psTzAppInsTMapper.insertSelective(psTzAppInsT);

								// ---2.将"班级"和"报名表"关系数据 存入 "PS_TZ_FORM_WRK_T"
								PsTzFormWrkT psTzFormWrkT = new PsTzFormWrkT();
								psTzFormWrkT.setTzClassId(strClassId);// 班级ID
								psTzFormWrkT.setOprid(oprid);// 当前用户
								psTzFormWrkT.setTzAppInsId(tzAppInsId);// 报名表实例ID

								// ---3.向推荐信相关表中，加入”推荐信“信息 "PS_TZ_KS_TJX_TBL"
								for (int i = 0; i < LetterList.size(); i++) {
									Map<String, Object> letterMap = LetterList.get(i);
									String tzRefLetterId = String
											.valueOf(getSeqNum.getSeqNum("PS_TZ_KS_TJX_TBL", "TZ_REF_LETTER_ID"));
									PsTzKsTjxTbl psTzKsTjxTbl = new PsTzKsTjxTbl();
									psTzKsTjxTbl.setTzRefLetterId(tzRefLetterId);// 主键
																					// 推荐信ID
									psTzKsTjxTbl.setTzAppInsId(tzAppInsId);// 关联报名表
																			// 实例ID
									Long tzTjxAppInsId = Long
											.valueOf(getSeqNum.getSeqNum("PS_TZ_KS_TJX_TBL", "TZ_REF_LETTER_ID"));
									psTzKsTjxTbl.setTzTjxAppInsId(tzTjxAppInsId); // 推荐信
																					// 报名表编号
									psTzKsTjxTbl.setOprid(oprid);
									if (letterMap.get("TZ_TJX_TYPE") != null) {
										psTzKsTjxTbl.setTzTjxType(String.valueOf(letterMap.get("TZ_TJX_TYPE")));
									}
									if (letterMap.get("TZ_REFLETTERTYPE") != null) {
										psTzKsTjxTbl
												.setTzReflettertype(String.valueOf(letterMap.get("TZ_REFLETTERTYPE")));
									}
									if (letterMap.get("TZ_TJR_ID") != null) {
										psTzKsTjxTbl.setTzTjrId(String.valueOf(letterMap.get("TZ_TJR_ID")));
									}
									if (letterMap.get("TZ_MBA_TJX_YX") != null) {
										psTzKsTjxTbl.setTzMbaTjxYx(String.valueOf(letterMap.get("TZ_MBA_TJX_YX")));
									}
									if (letterMap.get("TZ_REFERRER_NAME") != null) {
										psTzKsTjxTbl
												.setTzReferrerName(String.valueOf(letterMap.get("TZ_REFERRER_NAME")));
									}
									if (letterMap.get("TZ_REFERRER_GNAME") != null) {
										psTzKsTjxTbl
												.setTzReferrerGname(String.valueOf(letterMap.get("TZ_REFERRER_GNAME")));
									}
									if (letterMap.get("TZ_COMP_CNAME") != null) {
										psTzKsTjxTbl.setTzCompCname(String.valueOf(letterMap.get("TZ_COMP_CNAME")));
									}
									if (letterMap.get("TZ_POSITION") != null) {
										psTzKsTjxTbl.setTzPosition(String.valueOf(letterMap.get("TZ_POSITION")));
									}
									if (letterMap.get("TZ_TJX_TITLE") != null) {
										psTzKsTjxTbl.setTzTjxTitle(String.valueOf(letterMap.get("TZ_TJX_TITLE")));
									}
									if (letterMap.get("TZ_TJR_GX") != null) {
										psTzKsTjxTbl.setTzTjrGx(String.valueOf(letterMap.get("TZ_TJR_GX")));
									}
									if (letterMap.get("TZ_EMAIL") != null) {
										psTzKsTjxTbl.setTzEmail(String.valueOf(letterMap.get("TZ_EMAIL")));
									}
									if (letterMap.get("TZ_PHONE_AREA") != null) {
										psTzKsTjxTbl.setTzPhoneArea(String.valueOf(letterMap.get("TZ_PHONE_AREA")));
									}
									if (letterMap.get("TZ_PHONE") != null) {
										psTzKsTjxTbl.setTzPhone(String.valueOf(letterMap.get("TZ_PHONE")));
									}
									if (letterMap.get("TZ_GENDER") != null) {
										psTzKsTjxTbl.setTzGender(String.valueOf(letterMap.get("TZ_GENDER")));
									}
									if (letterMap.get("TZ_TJX_YL_1") != null) {
										psTzKsTjxTbl.setTzTjxYl1(String.valueOf(letterMap.get("TZ_TJX_YL_1")));
									}
									if (letterMap.get("TZ_TJX_YL_2") != null) {
										psTzKsTjxTbl.setTzTjxYl2(String.valueOf(letterMap.get("TZ_TJX_YL_2")));
									}
									if (letterMap.get("TZ_TJX_YL_3") != null) {
										psTzKsTjxTbl.setTzTjxYl3(String.valueOf(letterMap.get("TZ_TJX_YL_3")));
									}
									if (letterMap.get("TZ_TJX_YL_4") != null) {
										psTzKsTjxTbl.setTzTjxYl4(String.valueOf(letterMap.get("TZ_TJX_YL_4")));
									}
									if (letterMap.get("TZ_TJX_YL_5") != null) {
										psTzKsTjxTbl.setTzTjxYl5(String.valueOf(letterMap.get("TZ_TJX_YL_5")));
									}
									if (letterMap.get("TZ_TJX_YL_6") != null) {
										psTzKsTjxTbl.setTzTjxYl6(String.valueOf(letterMap.get("TZ_TJX_YL_6")));
									}
									if (letterMap.get("TZ_TJX_YL_7") != null) {
										psTzKsTjxTbl.setTzTjxYl7(String.valueOf(letterMap.get("TZ_TJX_YL_7")));
									}
									if (letterMap.get("TZ_TJX_YL_8") != null) {
										psTzKsTjxTbl.setTzTjxYl8(String.valueOf(letterMap.get("TZ_TJX_YL_8")));
									}
									if (letterMap.get("TZ_TJX_YL_9") != null) {
										psTzKsTjxTbl.setTzTjxYl9(String.valueOf(letterMap.get("TZ_TJX_YL_9")));
									}
									if (letterMap.get("TZ_TJX_YL_10") != null) {
										psTzKsTjxTbl.setTzTjxYl10(String.valueOf(letterMap.get("TZ_TJX_YL_10")));
									}
									if (letterMap.get("TZ_ATT_A_URL") != null) {
										psTzKsTjxTbl.setTzAttAUrl(String.valueOf(letterMap.get("TZ_ATT_A_URL")));
									}
									if (letterMap.get("ATTACHSYSFILENAME") != null) {
										psTzKsTjxTbl.setAttachsysfilename(
												String.valueOf(letterMap.get("ATTACHSYSFILENAME")));
									}
									if (letterMap.get("ATTACHUSERFILE") != null) {
										psTzKsTjxTbl.setAttachuserfile(String.valueOf(letterMap.get("ATTACHUSERFILE")));
									}
									psTzKsTjxTbl.setRowAddedDttm(new Date());
									psTzKsTjxTbl.setRowAddedOprid(oprid);
									psTzKsTjxTbl.setRowLastmantDttm(new Date());
									psTzKsTjxTbl.setRowLastmantOprid(oprid);
									if (letterMap.get("SYNCID") != null && letterMap.get("SYNCID") != "") {
										psTzKsTjxTbl
												.setSyncid(Integer.valueOf(String.valueOf(letterMap.get("SYNCID"))));
									}
									psTzKsTjxTbl.setSyncdttm(new Date());
									if (letterMap.get("TZ_ACCESS_PATH") != null) {
										psTzKsTjxTbl.setTzAccessPath(String.valueOf(letterMap.get("TZ_ACCESS_PATH")));
									}
									psTzKsTjxTblMapper.insertSelective(psTzKsTjxTbl);
								}
								// ---4.将新产生的"报名表实例ID"放入 前端html,即：strAppInsId
								strAppInsId = String.valueOf(tzAppInsId);
								System.out.println("strAppInsId:" + strAppInsId);
							}
						}

						// -----------------复制推荐信 代码结束
					}
				}
			}

			if (strTplData == null || "".equals(strTplData)) {
				strTplData = "''";
			}

			if (strInsData == null || "".equals(strInsData)) {
				strInsData = "''";
			}

			// 双语化消息集合Json字符串
			// msgSet 用于双语
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
			strUserInfoSet = this.getUserInfo();

			String strSave = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET", "SAVE",
					strLanguage, "保存", "Save");
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
			String sqlGetSiteYt = "";
			String strSiteYtHtml = "";
			String strLogoImg = "";
			try {
				sqlGetSiteYt = "SELECT TZ_AREA_PUBCODE FROM PS_TZ_SITEI_AREA_T WHERE TZ_SITEI_ID = ? AND TZ_AREA_TYPE = 'YT'";
				strSiteYtHtml = sqlQuery.queryForObject(sqlGetSiteYt, new Object[] { strSiteId }, "String");

				int numCharstart = strSiteYtHtml.indexOf("src=\"");
				int numCharend = strSiteYtHtml.indexOf("\"", numCharstart + 5);
				// System.out.println("开始位置:"+numCharstart +
				// "结束位置:"+numCharend);
				if (numCharend > numCharstart) {
					strLogoImg = strSiteYtHtml.substring(numCharstart + 5, numCharend);
					// System.out.println("logo:"+strLogoImg);
				} else {
					strLogoImg = contextUrl + "/statics/images/appeditor/new/logo.png";
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				strLogoImg = contextUrl + "/statics/images/appeditor/new/logo.png";
				e1.printStackTrace();
			}

			if ("Y".equals(strIsAdmin)) {
				strMainInnerStyle = "margin: 0 auto;float:none";
				strMainStyle = "width:788px;";
			} else {
				try {

					strOnlineHead = tzGdObject.getHTMLText("HTML.TZWebsiteApplicationBundle.TZ_ONLINE_HEAD_HTML", false,
							strLogoImg);
					strOnlineFoot = tzGdObject.getHTMLText("HTML.TZWebsiteApplicationBundle.TZ_ONLINE_FOOT_HTML", false);
					if ("BMB".equals(strTplType)) {
						if ("Y".equals(strIsGuest)) {
							strMainInnerStyle = "margin: 0 auto;float:none";
						} else {
							strOnlineLeft = tzGdObject
									.getHTMLText("HTML.TZWebsiteApplicationBundle.TZ_ONLINE_LEFTMENU_HTML", false);
						}
					} else {
						strMainInnerStyle = "margin: 0 auto;float:none";
					}
				} catch (TzSystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if ("".equals(strAppInsId) || strAppInsId == null) {
				strAppInsId = "0";
			}

			// 非匿名报名时，如果当前登录人为管理员、并且可编辑，同时报名表只读参数为Y时，将只读参数改为N
			if (!StringUtils.equals("Y", strIsGuest) && StringUtils.equals("Y", strIsAdmin)
					&& StringUtils.equals("Y", strIsEdit) && StringUtils.equals("Y", strAppFormReadOnly)) {
				strAppFormReadOnly = "N";
			}

			try {
				String passWordHtml = "";

				String setPwdId = "setPwd";
				String setPwd2Id = "setPwd2";
				String pwdTitleDivId = "PwdTitleDiv";
				String pwdDivId = "setPwdDiv";
				String pwdDivId2 = "setPwdDiv2";
				// 推荐信 密码设置控制 add by caoy 2017-1-22
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
				strTplData = strTplData.replace("$", "\\$");
				// strInsData =
				// tzOnlineAppViewServiceImpl.getHisAppInfoJson(numAppInsId,
				// strTplId);
				strInsData = strInsData.replace("\\", "\\\\");
				strInsData = strInsData.replace("$", "\\$");
				// 处理HTML换行符号，是替换的\u2028;
				strInsData = strInsData.replace(" ", "");
				System.out.println("strTabs:" + strTabs);
				String pwdError = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
						"TJXSETPWDError", strLanguage, "请填写密码", "Please fill in the password");

				String pwdError2 = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
						"TJXSETPWDError", strLanguage, "密码和确认密码不一致", "Password and confirm password inconsistent");
				
				
				
				String Pwdname = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
						"TJXSETPWD", strLanguage, "访问密码", "Access password");
				String strSubmit2 = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
						"SUBMIT", strLanguage, "提交", "Submit");
				
				//构建密码输入框
				String PWDHTML = tzGdObject.getHTMLText("HTML.TZWebsiteApplicationBundle.TZ_ONLINE_PWD_HTML", false,
						Pwdname, strSubmit2, contextUrl);
				
				
				str_appform_main_html = tzGdObject.getHTMLText("HTML.TZWebsiteApplicationBundle.TZ_ONLINE_PAGE_HTML",
						false, strTzGeneralURL, strComRegInfo, strTplId, strAppInsId, strClassId, strRefLetterId,
						strTplData, strInsData, strTabs, strSiteId, strAppOrgId, strMenuId, strAppFormReadOnly,
						strMsgSet, strLanguage, strSave, strNext, strSubmit, strTplType, strLoading, strProcessing,
						strAfterSubmitUrl, strOnlineHead, strOnlineFoot, strOnlineLeft, strIsAdmin, strMainInnerStyle,
						strUserInfoSet, strMainStyle, strPrev, strAppInsVersion, contextUrl, leftWidthStyle,
						rightWidthStyle, strLeftStyle, strRightStyle, showSubmitBtnOnly, strSubmitConfirmMsg, strIsEdit,
						strBatchId, strTJXIsPwd, passWordHtml, setPwdId, setPwd2Id, pwdTitleDivId, pwdDivId, pwdDivId2,
						pwdError, pwdError2,PWDHTML);
			

				str_appform_main_html = siteRepCssServiceImpl.repTitle(str_appform_main_html, strSiteId);
				str_appform_main_html = siteRepCssServiceImpl.repCss(str_appform_main_html, strSiteId);
			} catch (TzSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			str_appform_main_html = strMessageError;
		}

		// str_appform_main_html =
		// tzOnlineAppViewServiceImpl.getHisAppInfoJson(numAppInsId, strTplId);

		return str_appform_main_html;
	}

	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";

		String successFlag = "0";

		String strMsg = "";

		// 当前登陆人
		String oprid = "";
		oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		// 报名表机构编号
		String strAppOrgId = "";
		// 班级编号
		String strClassId = "";
		String tempClassId = "";
		// 新的班级编号
		String strNewClassId = "";

		boolean chageClass = false;
		// 报名表实例编号
		String strAppInsId = "";
		// 被推荐人报名表实例编号
		String strAppInsIdRefer = "";
		// 报名表实例编号
		Long numAppInsId = 0L;
		// 班级编号
		String strRefLetterId = "";
		// 是否允许匿名报名
		String strGuestApply = "N";
		// 报名表审核，是否管理员可修改报名表
		String strIsEdit = "N";
		// 是否后台管理员人员查看
		String strIsAdmin = "";
		// 当前人员填写的报名表的版本号
		String strAppInsVersion = "";
		// 当前人员数据库版本
		String strAppInsVersionDb = "";
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
		// 页面pageId
		String strPageId = "";
		// 会话过期
		String strSessionInvalidTips = "";
		// 非法操作
		String strIllegalOperation = "";
		// 参数操作
		String strParaError = "";
		// 修改的班级 报名表已经提交
		String strClassError = "";
		// 版本不一直
		String strVersionError = "";

		// 批次
		String strBatchId = "";
		// 密码
		String strPwd = "";

		JacksonUtil jacksonUtil = new JacksonUtil();
		// 表单内容
		int dataLength = actData.length;
		for (int num = 0; num < dataLength; num++) {
			String strForm = actData[num];
			// 解析json
			jacksonUtil.json2Map(strForm);
			strClassId = String.valueOf(jacksonUtil.getString("TZ_CLASS_ID"));
			tempClassId = strClassId;
			if (jacksonUtil.containsKey("TZ_BATCH_ID")) {
				strBatchId = String.valueOf(jacksonUtil.getString("TZ_BATCH_ID"));
			}
			if (jacksonUtil.containsKey("TZ_NEW_CLASS_ID")) {
				strNewClassId = String.valueOf(jacksonUtil.getString("TZ_NEW_CLASS_ID"));
			}
			if (jacksonUtil.containsKey("PASSWORD")) {
				strPwd = String.valueOf(jacksonUtil.getString("PASSWORD"));
			}

			// 密码用MD5加密存储
			if (strPwd != null && !strPwd.equals("")) {
				strPwd = Sha3DesMD5.md5(strPwd);
			}

			strAppInsId = String.valueOf(jacksonUtil.getString("TZ_APP_INS_ID"));
			strRefLetterId = String.valueOf(jacksonUtil.getString("TZ_REF_LETTER_ID"));
			strLanguage = String.valueOf(jacksonUtil.getString("TZ_LANGUAGE"));
			strIsEdit = String.valueOf(jacksonUtil.getString("isEdit"));
			strAppInsVersion = String.valueOf(jacksonUtil.getString("TZ_APP_INS_VERSION"));
			strPageId = String.valueOf(jacksonUtil.getString("TZ_PAGE_ID"));
			if (strAppInsVersion == null) {
				strAppInsVersion = "";
			}

			numAppInsId = Long.parseLong(strAppInsId);

			strSessionInvalidTips = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
					"SESSION_INVAILD", strLanguage, "当前会话已失效，请重新登陆。",
					"The current session is timeout or the current access is invalid,Please relogin.");
			strIllegalOperation = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
					"ILLEGAL_OPERATION", strLanguage, "非法操作", "Illegal operation");
			strParaError = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
					"PARAERROR", strLanguage, "参数错误", "Parameter error.");
			strVersionError = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
					"PAGE_INVALID", strLanguage, "当前页面已失效，请重新进入页面或刷新页面再试。",
					"The current page has expired, please re-enter the page and try again.");

			strClassError = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
					"CLASSERROR", strLanguage, "该班级已经填写报名表，不允许重复。",
					"The class has filled in the application form, not allowed to repeat.");

			String sql = "";
			if (!"".equals(strClassId) && strClassId != null) {
				// 如果跟换 class那么检查 新的classId 是否已经存在不允许保存，报错
				// 整体逻辑变更，如果跟换班级，那么 修改 TZ_FORM_WRK_T 里面的内容 by caoy 20170118
				//
				if (!"".equals(strNewClassId) && strNewClassId != null) {
					if (!strClassId.equals(strNewClassId)) {
						if (!"TZ_GUEST".equals(oprid) && !"".equals(oprid)) {
							sql = " SELECT COUNT(1) from PS_TZ_FORM_WRK_T  where  TZ_CLASS_ID=? AND OPRID=?";
							int have = sqlQuery.queryForObject(sql, new Object[] { strNewClassId, oprid }, "Integer");
							if (have > 0) {
								errMsg[0] = "1";
								errMsg[1] = strClassError;
								strMsg = strClassError;
							}
						}
						chageClass = true;
						strClassId = strNewClassId;
					}
				}

				if (!errMsg[0].equals("1")) {

					sql = "SELECT TZ_GUEST_APPLY FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID = ?";
					strGuestApply = sqlQuery.queryForObject(sql, new Object[] { strClassId }, "String");
					sql = "SELECT TZ_JG_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID = ?";
					strAppOrgId = sqlQuery.queryForObject(sql, new Object[] { strClassId }, "String");
					if (!"Y".equals(strGuestApply) && ("TZ_GUEST".equals(oprid) || "".equals(oprid))) {
						// 该班级未开发匿名报名
						errMsg[0] = "1";
						errMsg[1] = strSessionInvalidTips;
						strMsg = strSessionInvalidTips;
					} else {
						if (numAppInsId > 0) {
							// 有报名表实例编号
							PsTzAppInsT psTzAppInsT = psTzAppInsTMapper.selectByPrimaryKey(numAppInsId);
							strTplId = psTzAppInsT.getTzAppTplId();
							strAppInsState = psTzAppInsT.getTzAppFormSta();
							strAppInsVersionDb = psTzAppInsT.getTzAppInsVersion();

							sql = "SELECT OPRID FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID = ? AND TZ_CLASS_ID = ? ORDER BY OPRID";
							strAppOprId = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, tempClassId },
									"String");

							if (!"".equals(strTplId) && strTplId != null && !"".equals(strAppOprId)
									&& strAppOprId != null) {
								if (strAppOprId.equals(oprid) || "Y".equals(strGuestApply)) {
									// 自己操作自己的报名表或者允许匿名报名
								} else {
									/*
									 * 判断当前登录人是不是班级管理员的逻辑与PS版保持一致 By
									 * WRL@20161130 BEGIN
									 */
									sql = "SELECT 'Y' FROM PS_TZ_CLS_ADMIN_T WHERE TZ_CLASS_ID = ? AND OPRID = ?";
									strIsAdmin = sqlQuery.queryForObject(sql, new Object[] { strClassId, oprid },
											"String");

									// sql = "SELECT 'Y' FROM PS_TZ_APPTPL_R_T
									// A,PSROLEUSER B WHERE A.ROLENAME =
									// B.ROLENAME
									// AND A.TZ_JG_ID = ? AND A.TZ_APP_TPL_ID =
									// ?
									// AND B.ROLEUSER = ?";
									// strIsAdmin = sqlQuery.queryForObject(sql,
									// new
									// Object[] { strAppOrgId,strTplId,oprid },
									// "String");
									/*
									 * 判断当前登录人是不是班级管理员的逻辑与PS版保持一致 By
									 * WRL@20161130 END
									 */
									if (!"Y".equals(strIsAdmin)) {
										// 非法操作
										errMsg[0] = "1";
										errMsg[1] = strIllegalOperation;
										strMsg = strIllegalOperation;
									}
								}
							} else {
								// 非法操作
								errMsg[0] = "1";
								errMsg[1] = strParaError;
								strMsg = strParaError;
								// System.out.println("1111111");
							}
						} else {
							// 没报名表实例编号
							strAppOprId = oprid;
							if (!"TZ_GUEST".equals(oprid) && !"".equals(oprid)) {
								sql = "SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID = ? AND TZ_CLASS_ID = ?";
								strAppInsId = sqlQuery.queryForObject(sql, new Object[] { oprid, tempClassId },
										"String");
								if (strAppInsId == null || "".equals(strAppInsId)) {
									numAppInsId = 0L;
								} else {
									numAppInsId = Long.parseLong(strAppInsId);
								}
							}
							if (numAppInsId > 0) {
								// 如果已报名有实例编号
								PsTzAppInsT psTzAppInsT = psTzAppInsTMapper.selectByPrimaryKey(numAppInsId);
								strTplId = psTzAppInsT.getTzAppTplId();
								strAppInsState = psTzAppInsT.getTzAppFormSta();
							} else {
								sql = "SELECT TZ_APP_MODAL_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_IS_APP_OPEN = 'Y' AND TZ_CLASS_ID = ?";
								strTplId = sqlQuery.queryForObject(sql, new Object[] { strClassId }, "String");
								strAppInsId = String.valueOf(getSeqNum.getSeqNum("TZ_APP_INS_T", "TZ_APP_INS_ID"));
								numAppInsId = Long.parseLong(strAppInsId);
							}
						}
					}
				}
			} else {
				if (!"".equals(strRefLetterId) && strRefLetterId != null) {
					if (numAppInsId > 0) {
						// 查询推荐信报名表编号
						String sqlGetKsTjxInfo = "SELECT TZ_APP_INS_ID,OPRID FROM PS_TZ_KS_TJX_TBL WHERE TZ_REF_LETTER_ID = ? AND TZ_TJX_APP_INS_ID = ?";
						Map<String, Object> MapKsTjxInfo = sqlQuery.queryForMap(sqlGetKsTjxInfo,
								new Object[] { strRefLetterId, strAppInsId });
						strAppInsIdRefer = MapKsTjxInfo.get("TZ_APP_INS_ID") == null ? ""
								: String.valueOf(MapKsTjxInfo.get("TZ_APP_INS_ID"));
						strAppOprId = MapKsTjxInfo.get("OPRID") == null ? ""
								: String.valueOf(MapKsTjxInfo.get("OPRID"));
						// 有报名表实例编号
						PsTzAppInsT psTzAppInsT = psTzAppInsTMapper.selectByPrimaryKey(numAppInsId);
						if (psTzAppInsT != null) {
							strTplId = psTzAppInsT.getTzAppTplId();
							strAppInsState = psTzAppInsT.getTzAppFormSta();
							strAppInsVersionDb = psTzAppInsT.getTzAppInsVersion();
						}

						if ("".equals(strAppInsIdRefer)) {
							errMsg[0] = "1";
							errMsg[1] = strParaError;
							strMsg = strParaError;
							System.out.println("222222");
						}
					} else {
						errMsg[0] = "1";
						errMsg[1] = strParaError;
						strMsg = strParaError;
						System.out.println("33333");
					}
				} else {
					// 非法操作
					errMsg[0] = "1";
					errMsg[1] = strParaError;
					strMsg = strParaError;
					System.out.println("444444");
				}
			}
			// 当前报名表实例版本是否和数据库一致
			if (strAppInsVersionDb == null) {
				strAppInsVersionDb = "";
			}

			if (strAppInsVersion.equals(strAppInsVersionDb)) {
				DateFormat formatDate = new SimpleDateFormat("yyyyMMddhhmmss");
				String simpleDate = formatDate.format(new Date());

				String allChar = "0123456789";
				StringBuffer sb = new StringBuffer();
				Random random = new Random();
				for (int i = 0; i < 10; i++) {
					sb.append(allChar.charAt(random.nextInt(allChar.length())));
				}
				strAppInsVersionDb = String.valueOf(simpleDate) + sb.toString();
			} else {
				// 变更CLass 不需要校验这块
				if (chageClass) {
					DateFormat formatDate = new SimpleDateFormat("yyyyMMddhhmmss");
					String simpleDate = formatDate.format(new Date());

					String allChar = "0123456789";
					StringBuffer sb = new StringBuffer();
					Random random = new Random();
					for (int i = 0; i < 10; i++) {
						sb.append(allChar.charAt(random.nextInt(allChar.length())));
					}
					strAppInsVersionDb = String.valueOf(simpleDate) + sb.toString();
				} else {
					errMsg[0] = "1";
					errMsg[1] = strVersionError;
					strMsg = strVersionError;
				}
			}

			if ("U".equals(strAppInsState)) {
				if (StringUtils.equals("Y", strIsAdmin) && StringUtils.equals("Y", strIsEdit)) {
					// 如果是管理员并且可编辑的话继续 By WRL@20161027
				} else {
					errMsg[0] = "1";
					errMsg[1] = strIllegalOperation;
					strMsg = strIllegalOperation;
				}
			}

			if ("0".equals(errMsg[0]) && "".equals(strMsg)) {
				sql = "SELECT TZ_USE_TYPE FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ?";
				strTplType = sqlQuery.queryForObject(sql, new Object[] { strTplId }, "String");
				String strOtype = "";
				strOtype = String.valueOf(jacksonUtil.getString("TZ_APP_C_TYPE"));

				// String strData = jacksonUtil.getString("data");

				Map<String, Object> mapData = jacksonUtil.getMap("data");

				String strData = jacksonUtil.Map2json(mapData);

				if (!"U".equals(strAppInsState)) {
					strAppInsState = "S";
				}
				// 如果是匿名报名，报名表保存，需要为匿名用户自动注册非激活账号，并创建用户信息
				String strDefVal = "";
				String strNAME = "";
				String strFirstName = "";
				String strLastName = "";
				String strGuestOprId = "";
				if ("Y".equals(strGuestApply) && "".equals(oprid) && "".equals(strAppOprId)) {
					strIsGuest = "Y";
					for (Entry<String, Object> entry : mapData.entrySet()) {
						Map<String, Object> mapJsonItems = (Map<String, Object>) entry.getValue();
						String strIsDoubleLine = "";
						if (mapJsonItems.containsKey("isDoubleLine")) {
							strIsDoubleLine = String.valueOf(mapJsonItems.get("isDoubleLine"));
						}
						String strIsSingleLine = "";
						if (mapJsonItems.containsKey("isSingleLine")) {
							strIsSingleLine = String.valueOf(mapJsonItems.get("isSingleLine"));
						}
						String strIsFixedContainer = "";
						if (mapJsonItems.containsKey("fixedContainer")) {
							strIsSingleLine = String.valueOf(mapJsonItems.get("fixedContainer"));
						}
						if ("Y".equals(strIsDoubleLine)) {
							// 多行容器
							if ("Y".equals(strIsFixedContainer)) {
								// 固定多行容器
							} else {
								List<?> mapChildrens1 = (ArrayList<?>) mapJsonItems.get("children");
								for (Object children1 : mapChildrens1) {
									Map<String, Object> mapChildren1 = (Map<String, Object>) children1;
									for (Entry<String, Object> entryChildren : mapChildren1.entrySet()) {
										Map<String, Object> mapJsonChildrenItems = (Map<String, Object>) entryChildren
												.getValue();
										if (mapJsonChildrenItems.containsKey("children")) {
											// donothing
										} else {
											if (mapJsonChildrenItems.containsKey("defaultval")) {
												strDefVal = String.valueOf(mapJsonChildrenItems.get("defaultval"));
												if (!"".equals(strDefVal) && strDefVal != null) {
													// 取TZ_REALNAME
													if ("".equals(strNAME) && strDefVal.contains("TZ_REALNAME")) {
														strNAME = String.valueOf(mapJsonChildrenItems.get("value"));
													}
													// 取TZ_LAST_NAME
													if ("".equals(strLastName) && strDefVal.contains("TZ_LAST_NAME")) {
														strLastName = String.valueOf(mapJsonChildrenItems.get("value"));
													}
													// 取TZ_FIRST_NAME
													if ("".equals(strFirstName)
															&& strDefVal.contains("TZ_FIRST_NAME")) {
														strFirstName = String
																.valueOf(mapJsonChildrenItems.get("value"));
													}
												}
											}
										}
									}
								}
							}
						} else {
							if ("Y".equals(strIsSingleLine)) {
								// 当行控件 donothing
							} else {
								if (mapJsonItems.containsKey("defaultval")) {
									strDefVal = String.valueOf(mapJsonItems.get("defaultval"));
									if (!"".equals(strDefVal) && strDefVal != null) {
										// 取TZ_REALNAME
										if ("".equals(strNAME) && strDefVal.contains("TZ_REALNAME")) {
											strNAME = String.valueOf(mapJsonItems.get("value"));
										}
										// 取TZ_LAST_NAME
										if ("".equals(strLastName) && strDefVal.contains("TZ_LAST_NAME")) {
											strLastName = String.valueOf(mapJsonItems.get("value"));
										}
										// 取TZ_FIRST_NAME
										if ("".equals(strFirstName) && strDefVal.contains("TZ_FIRST_NAME")) {
											strFirstName = String.valueOf(mapJsonItems.get("value"));
										}
									}
								}
							}
						}
					}

					if (strLanguage != null && "ZHS".equals(strLanguage)) {
						if ("".equals(strNAME) || strNAME == null) {
							strNAME = "GUEST";
						}
					} else {
						if ("".equals(strLastName) || strLastName == null) {
							if ("".equals(strFirstName) || strFirstName == null) {
								strNAME = "GUEST";
							} else {
								strNAME = strFirstName;
							}
						} else {
							if ("".equals(strFirstName) || strFirstName == null) {
								strNAME = strLastName;
							} else {
								strNAME = strFirstName + " " + strFirstName;
							}
						}
					}
					if (strAppOrgId == null || "".equals(strAppOrgId)) {
						String sqlGetOrgId = "SELECT TZ_JG_ID FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = :1 AND TZ_EFFEXP_ZT = 'Y' LIMIT 1";
						strAppOrgId = sqlQuery.queryForObject(sqlGetOrgId, new Object[] { strTplId }, "String");
					}
					// 创建用户
					strGuestOprId = createGuestUser(strAppOrgId, strNAME);
					strAppOprId = strGuestOprId;
				}

				if ("SAVE".equals(strOtype)) {
					strMsg = this.saveAppForm(strTplId, numAppInsId, tempClassId, strAppOprId, strData, strTplType,
							strIsGuest, strAppInsVersionDb, strAppInsState, strBatchId, strClassId, strPwd);
					if ("".equals(strMsg)) {
						strMsg = this.checkFiledValid(numAppInsId, strTplId, strPageId, "save");
						if ("".equals(strMsg)) {
							this.savePageCompleteState(numAppInsId, strPageId, "Y");
						} else {
							this.savePageCompleteState(numAppInsId, strPageId, "N");
						}
					}
					// 模版级事件
					String sqlGetModalEvents = "SELECT CMBC_APPCLS_PATH,CMBC_APPCLS_NAME,CMBC_APPCLS_METHOD FROM PS_TZ_APP_EVENTS_T WHERE TZ_APP_TPL_ID = ? AND TZ_EVENT_TYPE = 'SA_A'";
					List<?> listGetModalEvents = sqlQuery.queryForList(sqlGetModalEvents, new Object[] { strTplId });
					for (Object objDataGetModalEvents : listGetModalEvents) {
						Map<String, Object> MapGetModalEvents = (Map<String, Object>) objDataGetModalEvents;
						String strAppClassPath = "";
						String strAppClassName = "";
						String strAppClassMethod = "";
						String strEventReturn = "";
						strAppClassPath = MapGetModalEvents.get("CMBC_APPCLS_PATH") == null ? ""
								: String.valueOf(MapGetModalEvents.get("CMBC_APPCLS_PATH"));
						strAppClassName = MapGetModalEvents.get("CMBC_APPCLS_NAME") == null ? ""
								: String.valueOf(MapGetModalEvents.get("CMBC_APPCLS_NAME"));
						strAppClassMethod = MapGetModalEvents.get("CMBC_APPCLS_METHOD") == null ? ""
								: String.valueOf(MapGetModalEvents.get("CMBC_APPCLS_METHOD"));
						if (!"".equals(strAppClassPath) && !"".equals(strAppClassName)
								&& !"".equals(strAppClassMethod)) {
							/*
							 * String[] parameterTypes = new String[]
							 * {"String[]" }; Object[] arglist = new Object[] {
							 * numAppInsId ,strClassId , strAppOprId}; Object
							 * objs = ObjectDoMethod.Load(strAppClassPath + "."
							 * + strAppClassName, strAppClassMethod,
							 * parameterTypes, arglist); strEventReturn =
							 * String.valueOf(objs);
							 */
							// 根据配置需要去调用对应的程序
							tzOnlineAppEventServiceImpl tzOnlineAppEventServiceImpl = (tzOnlineAppEventServiceImpl) ctx
									.getBean(strAppClassPath + "." + strAppClassName);
							switch (strAppClassMethod) {
							// 根据报名表配置的方法名称去调用不同的方法
							}

						}
					}

				} else if ("SUBMIT".equals(strOtype)) {
					// 先保存数据
					strMsg = this.saveAppForm(strTplId, numAppInsId, tempClassId, strAppOprId, strData, strTplType,
							strIsGuest, strAppInsVersionDb, strAppInsState, strBatchId, strClassId, strPwd);
					// 模版级事件
					String sqlGetModalEvents = "SELECT CMBC_APPCLS_PATH,CMBC_APPCLS_NAME,CMBC_APPCLS_METHOD FROM PS_TZ_APP_EVENTS_T WHERE TZ_APP_TPL_ID = ? AND TZ_EVENT_TYPE = 'SU_A'";
					List<?> listGetModalEvents = sqlQuery.queryForList(sqlGetModalEvents, new Object[] { strTplId });
					for (Object objDataGetModalEvents : listGetModalEvents) {
						Map<String, Object> MapGetModalEvents = (Map<String, Object>) objDataGetModalEvents;
						String strAppClassPath = "";
						String strAppClassName = "";
						String strAppClassMethod = "";
						String strEventReturn = "";
						strAppClassPath = MapGetModalEvents.get("CMBC_APPCLS_PATH") == null ? ""
								: String.valueOf(MapGetModalEvents.get("CMBC_APPCLS_PATH"));
						strAppClassName = MapGetModalEvents.get("CMBC_APPCLS_NAME") == null ? ""
								: String.valueOf(MapGetModalEvents.get("CMBC_APPCLS_NAME"));
						strAppClassMethod = MapGetModalEvents.get("CMBC_APPCLS_METHOD") == null ? ""
								: String.valueOf(MapGetModalEvents.get("CMBC_APPCLS_METHOD"));
						if (!"".equals(strAppClassPath) && !"".equals(strAppClassName)
								&& !"".equals(strAppClassMethod)) {
							/*
							 * String[] parameterTypes = new String[]
							 * {"String[]" }; Object[] arglist = new Object[] {
							 * numAppInsId ,strClassId , strAppOprId}; Object
							 * objs = ObjectDoMethod.Load(strAppClassPath + "."
							 * + strAppClassName, strAppClassMethod,
							 * parameterTypes, arglist); strEventReturn =
							 * String.valueOf(objs);
							 * if(!"".equals(strEventReturn)){ strMsg = strMsg +
							 * strEventReturn + "\n"; }
							 */
							tzOnlineAppEventServiceImpl tzOnlineAppEventServiceImpl = (tzOnlineAppEventServiceImpl) ctx
									.getBean(strAppClassPath + "." + strAppClassName);
							switch (strAppClassMethod) {
							// 根据报名表配置的方法名称去调用不同的方法
							}
						}
					}

					// 提交数据
					String strMsgAlter = "";
					if ("".equals(strMsg)) {
						strMsg = this.checkFiledValid(numAppInsId, strTplId, "", "submit");
					}
					if ("".equals(strMsg)) {
						if (StringUtils.equals("Y", strIsAdmin) && StringUtils.equals("Y", strIsEdit)) {
							// 如果是管理员并且可编辑的话继续 By WRL@20161027
						} else {
							// 如果是推荐信，则提交后发送邮件
							if ("TJX".equals(strTplType)) {
								strMsg = this.submitAppForm(numAppInsId, strClassId, strAppOprId, strTplType,
										strBatchId, strPwd);
								String strSubmitTjxSendEmail = tzTjxThanksServiceImpl.sendTJX_Thanks(numAppInsId);
							}
							if ("BMB".equals(strTplType)) {
								/*
								 * 修改为在确认的时候再提交 //同步报名人联系方式
								 * this.savaContactInfo(numAppInsId, strTplId,
								 * strAppOprId); //发送邮件 String
								 * strSubmitSendEmail =
								 * this.sendSubmitEmail(numAppInsId, strTplId,
								 * strAppOprId, strAppOrgId, strTplType);
								 */
							}
						}
					}
				} else if ("CONFIRMSUBMIT".equals(strOtype)) {
					/* 确认提交报名表 */
					if (StringUtils.equals("Y", strIsAdmin) && StringUtils.equals("Y", strIsEdit)) {
						// 如果是管理员并且可编辑的话继续 By WRL@20161027
						this.savaContactInfo(numAppInsId, strTplId, strAppOprId);
					} else {
						strMsg = this.submitAppForm(numAppInsId, strClassId, strAppOprId, strTplType, strBatchId,
								strPwd);
						if ("BMB".equals(strTplType)) {
							// 同步报名人联系方式
							this.savaContactInfo(numAppInsId, strTplId, strAppOprId);
							// 发送邮件
							String strSubmitSendEmail = this.sendSubmitEmail(numAppInsId, strTplId, strAppOprId,
									strAppOrgId, strTplType);
						}
					}
				}
			}

			if (!"".equals(strMsg)) {
				successFlag = "1";
			}
			// 页面完成状态Json
			String strPageXxxBh = "";
			String strPageCompleteState = "";

			ArrayList<Map<String, Object>> listJsonCompleteStateJson = new ArrayList<Map<String, Object>>();
			String sqlGetPageCompleteState = "SELECT TZ_XXX_BH FROM PS_TZ_APP_XXXPZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_COM_LMC = 'Page'";
			List<?> listGetPageCompleteState = sqlQuery.queryForList(sqlGetPageCompleteState,
					new Object[] { strTplId });
			for (Object objDataGetPageCompleteState : listGetPageCompleteState) {
				Map<String, Object> MapGetPageCompleteState = (Map<String, Object>) objDataGetPageCompleteState;
				strPageXxxBh = MapGetPageCompleteState.get("TZ_XXX_BH") == null ? ""
						: String.valueOf(MapGetPageCompleteState.get("TZ_XXX_BH"));
				String sqlPageHasCompleteFlag = "SELECT TZ_HAS_COMPLETE FROM PS_TZ_APP_COMP_TBL WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
				strPageCompleteState = sqlQuery.queryForObject(sqlPageHasCompleteFlag,
						new Object[] { numAppInsId, strPageXxxBh }, "String");
				if (!"Y".equals(strPageCompleteState)) {
					strPageCompleteState = "N";
				}
				Map<String, Object> strPageCompleteStateJson = new HashMap<String, Object>();
				strPageCompleteStateJson.put("pageId", strPageXxxBh);
				strPageCompleteStateJson.put("completeState", strPageCompleteState);
				listJsonCompleteStateJson.add(strPageCompleteStateJson);
			}
			Map<String, Object> mapJsonRet = new HashMap<String, Object>();
			mapJsonRet.put("code", successFlag);
			mapJsonRet.put("msg", strMsg);
			mapJsonRet.put("insid", strAppInsId);
			mapJsonRet.put("pageCompleteState", listJsonCompleteStateJson);
			mapJsonRet.put("appInsVersionId", strAppInsVersionDb);
			strRet = jacksonUtil.Map2json(mapJsonRet);
		} // end-for

		return strRet;

	}

	/**
	 * 推荐信校验密码
	 */
	@Override
	public String tzGetJsonData(String strParams) {

		String result = "{}";
		String successFlag = "0";
		String strMsg = "";
		JacksonUtil jsonUtil = new JacksonUtil();

		jsonUtil.json2Map(strParams);
		// 取JSON数据的MAP
		Map<String, Object> formDataMap = jsonUtil.getMap();

		if (formDataMap.containsKey("EType") && formDataMap.get("EType") != null) {
			String strEType = formDataMap.get("EType").toString();
			// 实例ID，模版ID
			String appInsId = null, appTplId = null, rtnMsg = null;

			String dataPwd = null;

			if (formDataMap.containsKey("TZ_APP_INS_ID") && formDataMap.get("TZ_APP_INS_ID") != null) {
				appInsId = formDataMap.get("TZ_APP_INS_ID").toString();
			}

			// 获取实例的一些信息
			String sql = "SELECT TZ_APP_TPL_ID,TZ_PWD FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID = ?";
			Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { appInsId });
			if (mapData != null) {
				appTplId = mapData.get("TZ_APP_TPL_ID") == null ? "" : String.valueOf(mapData.get("TZ_APP_TPL_ID"));
				dataPwd = mapData.get("TZ_PWD") == null ? "" : String.valueOf(mapData.get("TZ_PWD"));
			}
			/* 语言 */
			String language = sqlQuery.queryForObject(
					"SELECT TZ_APP_TPL_LAN FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID=?", new Object[] { appTplId },
					"String");

			if (language == null || language.equals("")) {
				language = "ZHS";
			}

			StringBuffer url = request.getRequestURL();
			String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length())
					.append("/").toString();

			/* 问卷密码是否正确 BEGIN */
			if (strEType.equals("PASSWORD")) {
				boolean boolRtn = false;
				String password = null;
				if (formDataMap.containsKey("PASSWORD") && formDataMap.get("PASSWORD") != null) {
					password = formDataMap.get("PASSWORD").toString();
				}
				boolRtn = tzOnlineAppRulesImpl.checkTJXPwd(dataPwd, language, password);
				System.out.println("boolRtn:"+boolRtn);
				if (!boolRtn) {
					successFlag = "1";
					strMsg = tzOnlineAppRulesImpl.msg;
				} else {
					Cookie cookie = new Cookie("SURVEY_TJX_IS_PASSWORD", "");
					// logger.info("Domain=" + tempContextUrl);
					cookie.setDomain(tempContextUrl);
					cookie.setPath("/");
					cookie.setMaxAge(-1);
					cookie.setValue(appInsId + "_" + appTplId + "_Y");
					response.addCookie(cookie);
					System.out.println("ADD cookie");
				}
				return "{\"code\": \"" + successFlag + "\",\"msg\": \"" + strMsg + "\"}";
			} else if (strEType.equals("PWDHTML")) {
				//System.out.println("11111111");
				try {
					String Pwdname = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
							"TJXSETPWD", language, "访问密码", "Access password");
					String strSubmit = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
							"SUBMIT", language, "提交", "Submit");
					
					String contextUrl = request.getContextPath();
					
					String pwdError = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
							"TJXSETPWDError", language, "请填写密码", "Please fill in the password");
					
					String PWDHTML = tzGdObject.getHTMLText("HTML.TZWebsiteApplicationBundle.TZ_ONLINE_PWD_HTML", false,
							Pwdname, strSubmit, pwdError, contextUrl);
					return PWDHTML;
				} catch (TzSystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "";
				}

			}
			/* 问卷密码是否正确 END */

			return null;

		} else {
			return result;
		}
	}

	@SuppressWarnings("unchecked")
	private String getUserInfo() {

		// 当前登陆人
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String strField = "";
		String strFieldValue = "";
		String strUserInfo = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String, Object> map = new HashMap<String, Object>();

		String sqlGetField = "SELECT TZ_REG_FIELD_ID FROM PS_TZ_REG_FIELD_T WHERE TZ_JG_ID = ? ORDER BY TZ_ORDER";
		List<?> listData = sqlQuery.queryForList(sqlGetField, new Object[] { orgid });
		for (Object objData : listData) {
			strFieldValue = "";
			Map<String, Object> mapData = (Map<String, Object>) objData;
			strField = mapData.get("TZ_REG_FIELD_ID") == null ? "" : String.valueOf(mapData.get("TZ_REG_FIELD_ID"));
			if ("TZ_PASSWORD".equals(strField) || "TZ_REPASSWORD".equals(strField)) {
				continue;
			}
			;
			try {
				if ("TZ_SKYPE".equals(strField) || "TZ_MOBILE".equals(strField) || "TZ_EMAIL".equals(strField)) {
					if ("TZ_MOBILE".equals(strField)) {
						String sql = "SELECT TZ_ZY_SJ FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY = 'ZCYH' AND TZ_LYDX_ID = ?";
						strFieldValue = sqlQuery.queryForObject(sql, new Object[] { oprid }, "String");
					} else if ("TZ_EMAIL".equals(strField)) {
						String sql = "SELECT TZ_ZY_EMAIL FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY = 'ZCYH' AND TZ_LYDX_ID = ?";
						strFieldValue = sqlQuery.queryForObject(sql, new Object[] { oprid }, "String");
					} else {
						String sql = "SELECT TZ_SKYPE FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY = 'ZCYH' AND TZ_LYDX_ID = ?";
						strFieldValue = sqlQuery.queryForObject(sql, new Object[] { oprid }, "String");
					}
				} else {
					if ("TZ_REALNAME".equals(strField)) {
						String sql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_JG_ID =? AND OPRID = ?";
						strFieldValue = sqlQuery.queryForObject(sql, new Object[] { orgid, oprid }, "String");
					} else {
						// 项目字段没对应;
						if ("TZ_PROJECT".equals(strField)) {
							String sql = "SELECT TZ_PRJ_ID FROM PS_TZ_REG_USER_T WHERE OPRID = '" + oprid + "'";
							strFieldValue = sqlQuery.queryForObject(sql, "String");
						} else {
							String sql = "SELECT " + strField + " FROM PS_TZ_REG_USER_T WHERE OPRID = '" + oprid + "'";
							strFieldValue = sqlQuery.queryForObject(sql, "String");
						}
					}
				}
				if (strFieldValue == null)
					strFieldValue = "";

				map.put(strField, strFieldValue);

			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		strUserInfo = jacksonUtil.Map2json(map);

		return strUserInfo;
	}

	private String checkAppViewQx(String strTplId, String oprid, String orgid, String strClassId) {

		String strHasPermission = "";
		String sql = "";
		sql = "SELECT 'Y' FROM PS_TZ_APPTPL_R_T A,PSROLEUSER B WHERE A.ROLENAME = B.ROLENAME AND A.TZ_JG_ID = ? AND A.TZ_APP_TPL_ID = ? AND B.ROLEUSER = ?";
		strHasPermission = sqlQuery.queryForObject(sql, new Object[] { orgid, strTplId, oprid }, "String");
		// 对当前模版是否有访问权限
		if ("".equals(strHasPermission) || strHasPermission == null) {
			// 是否材料评审评委
			sql = "SELECT 'Y' FROM PS_TZ_JUSR_REL_TBL WHERE OPRID = ?";
			strHasPermission = sqlQuery.queryForObject(sql, new Object[] { oprid }, "String");

			if ("".equals(strHasPermission) || strHasPermission == null) {
				// 是否是班级管理人员
				sql = "SELECT 'Y' FROM PS_TZ_CLS_ADMIN_T WHERE TZ_CLASS_ID = ? AND OPRID = ?";
				strHasPermission = sqlQuery.queryForObject(sql, new Object[] { strClassId, oprid }, "String");
			}
		}

		if (strHasPermission == null)
			strHasPermission = "";

		return strHasPermission;

	}

	// 报名表保存
	@SuppressWarnings("unchecked")
	private String saveAppForm(String strTplId, Long numAppInsId, String strClassId, String strAppOprId,
			String strJsonData, String strTplType, String strIsGuest, String strAppInsVersion, String strAppInsState,
			String strBathId, String newClassId, String pwd) {

		String returnMsg = "";

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		// Long numAppInsId = Long.parseLong(strAppInsId);
		try {
			String sql = "";
			int count = 0;
			boolean chageClass = false;

			if (newClassId != null && !newClassId.equals(strClassId)) {
				chageClass = true;
			}

			sql = "SELECT COUNT(1) FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID = ?";
			count = sqlQuery.queryForObject(sql, new Object[] { numAppInsId }, "Integer");
			if (count > 0) {
				PsTzAppInsT psTzAppInsT = new PsTzAppInsT();
				psTzAppInsT.setTzAppInsId(numAppInsId);
				psTzAppInsT.setTzAppTplId(strTplId);
				psTzAppInsT.setTzAppInsVersion(strAppInsVersion);
				psTzAppInsT.setTzAppFormSta(strAppInsState);
				psTzAppInsT.setTzAppinsJsonStr(strJsonData);
				psTzAppInsT.setRowLastmantOprid(oprid);
				psTzAppInsT.setRowLastmantDttm(new Date());
				psTzAppInsT.setTzPwd(pwd);
				psTzAppInsTMapper.updateByPrimaryKeySelective(psTzAppInsT);
			} else {
				PsTzAppInsT psTzAppInsT = new PsTzAppInsT();
				psTzAppInsT.setTzAppInsId(numAppInsId);
				psTzAppInsT.setTzAppTplId(strTplId);
				psTzAppInsT.setTzAppInsVersion(strAppInsVersion);
				psTzAppInsT.setTzAppFormSta(strAppInsState);
				psTzAppInsT.setTzAppinsJsonStr(strJsonData);
				psTzAppInsT.setRowAddedOprid(oprid);
				psTzAppInsT.setRowAddedDttm(new Date());
				psTzAppInsT.setRowLastmantOprid(oprid);
				psTzAppInsT.setRowLastmantDttm(new Date());
				psTzAppInsT.setTzPwd(pwd);
				psTzAppInsTMapper.insert(psTzAppInsT);
			}

			if ("BMB".equals(strTplType)) {
				// 如果是 变更班级，那么
				if (chageClass) {
					count = 0;
					sql = "SELECT COUNT(1) FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID = ? AND OPRID = ?";
					count = sqlQuery.queryForObject(sql, new Object[] { strClassId, strAppOprId }, "Integer");
					if (count > 0) {
						StringBuffer sb = new StringBuffer();
						sb.append("UPDATE PS_TZ_FORM_WRK_T SET TZ_CLASS_ID=?,OPRID=?,TZ_APP_INS_ID=?,");
						sb.append("TZ_BATCH_ID=?,ROW_ADDED_DTTM=?,ROW_ADDED_OPRID=?,ROW_LASTMANT_DTTM=?,");
						sb.append("ROW_LASTMANT_OPRID=? where TZ_CLASS_ID = ? AND OPRID = ?");
						sqlQuery.update(sb.toString(), new Object[] { newClassId, strAppOprId, numAppInsId, strBathId,
								new Date(), oprid, new Date(), oprid, strClassId, strAppOprId });
					} else {
						PsTzFormWrkT psTzFormWrkT = new PsTzFormWrkT();
						psTzFormWrkT.setTzClassId(newClassId);
						psTzFormWrkT.setOprid(strAppOprId);
						psTzFormWrkT.setTzAppInsId(numAppInsId);
						psTzFormWrkT.setRowAddedOprid(oprid);
						psTzFormWrkT.setRowAddedDttm(new Date());
						psTzFormWrkT.setRowLastmantOprid(oprid);
						psTzFormWrkT.setRowLastmantDttm(new Date());
						psTzFormWrkT.setTzBatchId(strBathId);
						psTzFormWrkTMapper.insert(psTzFormWrkT);
					}
				} else {
					count = 0;
					sql = "SELECT COUNT(1) FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID = ? AND OPRID = ?";
					count = sqlQuery.queryForObject(sql, new Object[] { strClassId, strAppOprId }, "Integer");
					if (count > 0) {
						PsTzFormWrkT psTzFormWrkT = new PsTzFormWrkT();
						psTzFormWrkT.setTzClassId(strClassId);
						psTzFormWrkT.setOprid(strAppOprId);
						psTzFormWrkT.setTzAppInsId(numAppInsId);
						psTzFormWrkT.setRowLastmantOprid(oprid);
						psTzFormWrkT.setRowLastmantDttm(new Date());
						psTzFormWrkT.setTzBatchId(strBathId);
						psTzFormWrkTMapper.updateByPrimaryKeySelective(psTzFormWrkT);
					} else {
						PsTzFormWrkT psTzFormWrkT = new PsTzFormWrkT();
						psTzFormWrkT.setTzClassId(strClassId);
						psTzFormWrkT.setOprid(strAppOprId);
						psTzFormWrkT.setTzAppInsId(numAppInsId);
						psTzFormWrkT.setRowAddedOprid(oprid);
						psTzFormWrkT.setRowAddedDttm(new Date());
						psTzFormWrkT.setRowLastmantOprid(oprid);
						psTzFormWrkT.setRowLastmantDttm(new Date());
						psTzFormWrkT.setTzBatchId(strBathId);
						psTzFormWrkTMapper.insert(psTzFormWrkT);
					}
				}
			}
			// 保存数据到结构化表
			// 解析json
			JacksonUtil jacksonUtil = new JacksonUtil();
			// jacksonUtil.json2Map(strJsonData);

			Map<String, Object> mapAppData = jacksonUtil.parseJson2Map(strJsonData);

			if (mapAppData != null) {
				this.delAppIns(numAppInsId);
				for (Entry<String, Object> entry : mapAppData.entrySet()) {
					Map<String, Object> mapJsonItems = (Map<String, Object>) entry.getValue();
					String strClassName = "";
					if (mapJsonItems.containsKey("classname")) {
						strClassName = String.valueOf(mapJsonItems.get("classname"));
					}
					String strIsDoubleLine = "";
					if (mapJsonItems.containsKey("isDoubleLine")) {
						strIsDoubleLine = String.valueOf(mapJsonItems.get("isDoubleLine"));
					}
					String strIsSingleLine = "";
					if (mapJsonItems.containsKey("isSingleLine")) {
						strIsSingleLine = String.valueOf(mapJsonItems.get("isSingleLine"));
					}
					String strOthervalue = "";
					if (mapJsonItems.containsKey("othervalue")) {
						strOthervalue = String.valueOf(mapJsonItems.get("othervalue"));
					}
					String strItemIdLevel0 = "";
					if (mapJsonItems.containsKey("itemId")) {
						strItemIdLevel0 = String.valueOf(mapJsonItems.get("itemId"));
					}
					if (mapJsonItems.containsKey("children")) {

						List<?> mapChildrens1 = (ArrayList<?>) mapJsonItems.get("children");
						if ("Y".equals(strIsDoubleLine)) {
							this.saveDhLineNum(strItemIdLevel0, numAppInsId, (short) mapChildrens1.size());
							for (Object children1 : mapChildrens1) {
								// 多行容器
								Map<String, Object> mapChildren1 = (Map<String, Object>) children1;
								for (Entry<String, Object> entryChildren : mapChildren1.entrySet()) {
									Map<String, Object> mapJsonChildrenItems = (Map<String, Object>) entryChildren
											.getValue();
									String strItemIdLevel1 = "";
									if (mapJsonChildrenItems.containsKey("itemId")) {
										strItemIdLevel1 = String.valueOf(mapJsonChildrenItems.get("itemId"));
									}
									if (mapJsonChildrenItems.containsKey("children")) {
										// 多行容器下的子容器
										List<?> mapChildrens2 = (ArrayList<?>) mapJsonChildrenItems.get("children");
										String strIsSingleLine2 = "";
										if (mapJsonChildrenItems.containsKey("isSingleLine")) {
											strIsSingleLine2 = String.valueOf(mapJsonChildrenItems.get("isSingleLine"));
										}
										if ("Y".equals(strIsSingleLine2)) {
											// 多行容器中的单行容器
											for (Object children2 : mapChildrens2) {
												Map<String, Object> mapChildren2 = (Map<String, Object>) children2;
												this.savePerXxxIns(strItemIdLevel0 + strItemIdLevel1, mapChildren2,
														numAppInsId);
											}
										} else {
											// 多行容器中的附件
											String strStorageType = "";
											if (mapJsonChildrenItems.containsKey("StorageType")) {
												strStorageType = mapJsonChildrenItems.get("StorageType") == null ? ""
														: String.valueOf(mapJsonChildrenItems.get("StorageType"));
												if ("F".equals(strStorageType)) {
													for (Object children2 : mapChildrens2) {
														Map<String, Object> mapChildren2 = (Map<String, Object>) children2;
														this.savePerAttrInfo(strItemIdLevel0 + strItemIdLevel1,
																mapChildren2, numAppInsId);
														String strIsHidden = "";
														if (mapJsonChildrenItems.containsKey("isHidden")) {
															strIsHidden = mapJsonChildrenItems.get("isHidden") == null
																	? ""
																	: String.valueOf(
																			mapJsonChildrenItems.get("isHidden"));
														}
														this.saveXxxHidden(numAppInsId,
																strItemIdLevel0 + strItemIdLevel1, strIsHidden);
													}
												}
											}
										}
									} else {
										// 多行容器中的单选框.复选框、一般字段
										String strStorageType = "";
										strStorageType = mapJsonChildrenItems.get("StorageType") == null ? ""
												: String.valueOf(mapJsonChildrenItems.get("StorageType"));
										if ("S".equals(strStorageType) || "L".equals(strStorageType)) {
											// 多行容器中的普通字段
											this.savePerXxxIns(strItemIdLevel0, mapJsonChildrenItems, numAppInsId);
										} else if ("D".equals(strStorageType)) {
											// 单选框或者复选框
											if (mapJsonChildrenItems.containsKey("option")) {
												Map<String, Object> mapOptions = (Map<String, Object>) mapJsonChildrenItems
														.get("option");
												for (Entry<String, Object> entryOption : mapOptions.entrySet()) {
													Map<String, Object> mapOption = (Map<String, Object>) entryOption
															.getValue();
													this.savePerXxxIns2(strItemIdLevel0 + strItemIdLevel1, "",
															mapOption, numAppInsId);
												}
											}
										} else if ("F".equals(strStorageType)) {
											// 推荐信附件信息和其他固定容器附件
											if (!"recommendletter".equals(strClassName)) {
												this.savePerAttrInfo(strItemIdLevel0 + strItemIdLevel1,
														mapJsonChildrenItems, numAppInsId);
											}
										}
									}
								}
							}
						} else if ("Y".equals(strIsSingleLine)) {
							// 如果是单行容器
							for (Object children1 : mapChildrens1) {
								Map<String, Object> mapChildren1 = (Map<String, Object>) children1;
								String strStorageType = "";
								if (mapChildren1.containsKey("StorageType")) {
									strStorageType = mapChildren1.get("StorageType") == null ? ""
											: String.valueOf(mapChildren1.get("StorageType"));
								}
								if ("S".equals(strStorageType) || "L".equals(strStorageType)) {
									this.savePerXxxIns(strItemIdLevel0, mapChildren1, numAppInsId);
								}
							}
						} else {
							// 如果是附件信息
							String strStorageType = "";
							if (mapJsonItems.containsKey("StorageType")) {
								strStorageType = mapJsonItems.get("StorageType") == null ? ""
										: String.valueOf(mapJsonItems.get("StorageType"));
							}
							if ("F".equals(strStorageType)) {
								for (Object children1 : mapChildrens1) {
									Map<String, Object> mapChildren1 = (Map<String, Object>) children1;
									this.savePerAttrInfo(strItemIdLevel0, mapChildren1, numAppInsId);
									String strIsHidden = "";
									if (mapJsonItems.containsKey("isHidden")) {
										strIsHidden = mapJsonItems.get("isHidden") == null ? ""
												: String.valueOf(mapJsonItems.get("isHidden"));
									}
									this.saveXxxHidden(numAppInsId, strItemIdLevel0, strIsHidden);
								}
							}
						}
					} else {
						// 没有Children节点
						String strStorageType = "";
						if (mapJsonItems.containsKey("StorageType")) {
							strStorageType = mapJsonItems.get("StorageType") == null ? ""
									: String.valueOf(mapJsonItems.get("StorageType"));
						}
						if ("D".equals(strStorageType)) {
							// 如果是多项框或者单选框
							if (mapJsonItems.containsKey("option")) {
								Map<String, Object> mapOptions = (Map<String, Object>) mapJsonItems.get("option");
								for (Entry<String, Object> entryOption : mapOptions.entrySet()) {
									Map<String, Object> mapOption = (Map<String, Object>) entryOption.getValue();
									this.savePerXxxIns2(strItemIdLevel0, "", mapOption, numAppInsId);
								}
							}
						} else if ("S".equals(strStorageType) || "L".equals(strStorageType)) {
							this.savePerXxxIns("", mapJsonItems, numAppInsId);
							if ("bmrPhoto".equals(strClassName)) {
								// this.saveBmrPhoto("", mapJsonItems,
								// numAppInsId);
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			returnMsg = e.toString();
		}
		return returnMsg;
	}

	// 报名表提交
	private String submitAppForm(Long numAppInsId, String strClassId, String strAppOprId, String strTplType,
			String strBathId, String pwd) {

		String returnMsg = "";

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		try {
			String sql = "";
			int count = 0;
			sql = "SELECT COUNT(1) FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID = ?";
			count = sqlQuery.queryForObject(sql, new Object[] { numAppInsId }, "Integer");
			if (count > 0) {
				PsTzAppInsT psTzAppInsT = new PsTzAppInsT();
				psTzAppInsT.setTzAppInsId(numAppInsId);
				psTzAppInsT.setTzAppFormSta("U");
				psTzAppInsT.setTzAppSubDttm(new Date());
				psTzAppInsT.setRowLastmantOprid(oprid);
				psTzAppInsT.setRowLastmantDttm(new Date());
				psTzAppInsT.setTzPwd(pwd);
				psTzAppInsTMapper.updateByPrimaryKeySelective(psTzAppInsT);
			} else {
				returnMsg = "failed";
			}

			if ("BMB".equals(strTplType)) {
				count = 0;
				sql = "SELECT COUNT(1) FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID = ? AND OPRID = ?";
				count = sqlQuery.queryForObject(sql, new Object[] { strClassId, strAppOprId }, "Integer");
				if (count > 0) {
					PsTzFormWrkT psTzFormWrkT = new PsTzFormWrkT();
					psTzFormWrkT.setTzClassId(strClassId);
					psTzFormWrkT.setTzBatchId(strBathId);
					psTzFormWrkT.setOprid(strAppOprId);
					psTzFormWrkT.setTzAppInsId(numAppInsId);
					psTzFormWrkT.setTzFormSpSta("N");
					psTzFormWrkT.setRowLastmantOprid(oprid);
					psTzFormWrkT.setRowLastmantDttm(new Date());
					psTzFormWrkTMapper.updateByPrimaryKeySelective(psTzFormWrkT);
				} else {
					returnMsg = "failed";

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnMsg = e.toString();
		}
		return returnMsg;
	}

	/* 保存多行容器的行数信息 */
	private void saveDhLineNum(String strItemId, Long numAppInsId, short numLineDh) {
		String sql = "SELECT COUNT(1) FROM PS_TZ_APP_DHHS_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
		int count = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strItemId }, "Integer");
		if (count > 0) {
			PsTzAppDhhsT psTzAppDhhsT = new PsTzAppDhhsT();
			psTzAppDhhsT.setTzAppInsId(numAppInsId);
			psTzAppDhhsT.setTzXxxBh(strItemId);
			psTzAppDhhsT.setTzXxxLine(numLineDh);
			psTzAppDhhsTMapper.updateByPrimaryKeySelective(psTzAppDhhsT);
		} else {
			PsTzAppDhhsT psTzAppDhhsT = new PsTzAppDhhsT();
			psTzAppDhhsT.setTzAppInsId(numAppInsId);
			psTzAppDhhsT.setTzXxxBh(strItemId);
			psTzAppDhhsT.setTzXxxLine(numLineDh);
			psTzAppDhhsTMapper.insert(psTzAppDhhsT);
		}
	}

	// 删除报名表存储表信息
	private void delAppIns(Long numAppInsId) {

		Object[] args = new Object[] { numAppInsId };
		sqlQuery.update("DELETE FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID = ?", args);
		sqlQuery.update("DELETE FROM PS_TZ_APP_DHCC_T WHERE TZ_APP_INS_ID = ?", args);
		sqlQuery.update("DELETE FROM PS_TZ_FORM_ATT_T WHERE TZ_APP_INS_ID = ?", args);
		sqlQuery.update("DELETE FROM PS_TZ_APP_DHCC_T WHERE TZ_APP_INS_ID = ?", args);
		sqlQuery.update("DELETE FROM PS_TZ_APP_DHHS_T WHERE TZ_APP_INS_ID = ?", args);
		sqlQuery.update("DELETE FROM PS_TZ_APP_HIDDEN_T WHERE TZ_APP_INS_ID = ?", args);
	}

	// 将json数据解析保存到报名表存储表
	private void savePerXxxIns(String strParentItemId, Map<String, Object> xxxObject, Long numAppInsId) {

		String strItemId = "";
		if (xxxObject.containsKey("itemId")) {
			strItemId = String.valueOf(xxxObject.get("itemId"));
		}
		if (!"".equals(strParentItemId) && strParentItemId != null) {
			strItemId = strParentItemId + strItemId;
		}
		// 数据存储类型
		String strStorageType = "";
		// 存储值
		String strValueL = "";
		String strValueS = "";
		String strValue = "";
		// 控件类名称
		String strClassName = "";

		if (xxxObject.containsKey("StorageType")) {
			strStorageType = xxxObject.get("StorageType") == null ? "" : String.valueOf(xxxObject.get("StorageType"));
			if ("L".equals(strStorageType)) {
				strValueL = xxxObject.get("value") == null ? "" : String.valueOf(xxxObject.get("value"));
			} else {
				strValueS = xxxObject.get("value") == null ? "" : String.valueOf(xxxObject.get("value"));
				strValueL = xxxObject.get("wzsm") == null ? "" : String.valueOf(xxxObject.get("wzsm"));
			}
		}
		// 如果是推荐信title Start
		String sql = "";
		if (xxxObject.containsKey("classname")) {
			strClassName = xxxObject.get("classname") == null ? "" : String.valueOf(xxxObject.get("classname"));
			if ("RefferTitle".equals(strClassName)) {
				sql = "SELECT B.TZ_APP_TPL_LAN FROM PS_TZ_APP_INS_T A,PS_TZ_APPTPL_DY_T B WHERE A.TZ_APP_INS_ID = ? AND A.TZ_APP_TPL_ID = B.TZ_APP_TPL_ID";
				String strTplLang = sqlQuery.queryForObject(sql, new Object[] { numAppInsId }, "String");
				strValue = xxxObject.get("value") == null ? "" : String.valueOf(xxxObject.get("value"));
				sql = "SELECT TZ_ZHZ_DMS,TZ_ZHZ_CMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID = 'TZ_APP_REF_TITLE' AND TZ_ZHZ_ID = ?";
				Map<String, Object> Map = sqlQuery.queryForMap(sql, new Object[] { strValue });
				String strDms = "";
				String strZms = "";
				if (Map != null) {
					strDms = Map.get("TZ_ZHZ_DMS") == null ? "" : String.valueOf(Map.get("TZ_ZHZ_DMS"));
					strZms = Map.get("TZ_ZHZ_CMS") == null ? "" : String.valueOf(Map.get("TZ_ZHZ_CMS"));
				}

				if ("ENG".equals(strTplLang)) {
					if (!"".equals(strZms)) {
						strValueS = strZms;
					}
				} else {
					if (!"".equals(strDms)) {
						strValueS = strDms;
					}
				}
			}
		}

		if (strValueS.length() > 254) {
			strValueS = strValueS.substring(0, 254);
		}

		// 如果是推荐信title End
		PsTzAppCcT psTzAppCcT = new PsTzAppCcT();
		psTzAppCcT.setTzAppInsId(numAppInsId);
		psTzAppCcT.setTzXxxBh(strItemId);
		psTzAppCcT.setTzAppSText(strValueS);
		psTzAppCcT.setTzAppLText(strValueL);
		psTzAppCcTMapper.insert(psTzAppCcT);

		// 是否隐藏
		String strIsHidden = "";
		if (xxxObject.containsKey("isHidden")) {
			strIsHidden = xxxObject.get("isHidden") == null ? "" : String.valueOf(xxxObject.get("isHidden"));
			if ("".equals(strIsHidden)) {
				strIsHidden = "N";
			}
		} else {
			strIsHidden = "N";
		}
		this.saveXxxHidden(numAppInsId, strItemId, strIsHidden);
	}

	// 将json数据解析保存到报名表存储表
	private void savePerXxxIns2(String strParentItemId, String strOtherValue, Map<String, Object> xxxObject,
			Long numAppInsId) {

		String strIsChecked = "";
		if (xxxObject.containsKey("checked")) {
			strIsChecked = xxxObject.get("checked") == null ? "" : String.valueOf(xxxObject.get("checked"));
		}
		if (!"Y".equals(strIsChecked)) {
			strIsChecked = "N";
		}
		String strCode = "";
		if (xxxObject.containsKey("code")) {
			strCode = xxxObject.get("code") == null ? "" : String.valueOf(xxxObject.get("code"));
		}
		String strTxt = "";
		if (xxxObject.containsKey("txt")) {
			strTxt = xxxObject.get("txt") == null ? "" : String.valueOf(xxxObject.get("txt"));
		}
		if (xxxObject.containsKey("othervalue")) {
			strOtherValue = xxxObject.get("othervalue") == null ? "" : String.valueOf(xxxObject.get("othervalue"));
		}

		PsTzAppDhccT psTzAppDhccT = new PsTzAppDhccT();
		psTzAppDhccT.setTzAppInsId(numAppInsId);
		psTzAppDhccT.setTzXxxBh(strParentItemId);
		psTzAppDhccT.setTzIsChecked(strIsChecked);
		psTzAppDhccT.setTzXxxkxzMc(strCode);
		psTzAppDhccT.setTzAppSText(strTxt);
		psTzAppDhccT.setTzKxxQtz(strOtherValue);
		psTzAppDhccTMapper.insert(psTzAppDhccT);

	}

	// 将json数据解析保存到报名表附件存储表
	private void savePerAttrInfo(String strParentItemId, Map<String, Object> xxxObject, Long numAppInsId) {

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String strSysFileName = "";
		if (xxxObject.containsKey("sysFileName")) {
			strSysFileName = xxxObject.get("sysFileName") == null ? "" : String.valueOf(xxxObject.get("sysFileName"));
		}
		String strUseFileName = "";
		if (xxxObject.containsKey("fileName")) {
			strUseFileName = xxxObject.get("fileName") == null ? "" : String.valueOf(xxxObject.get("fileName"));
		} else {
			if (xxxObject.containsKey("filename")) {
				strUseFileName = xxxObject.get("filename") == null ? "" : String.valueOf(xxxObject.get("filename"));
			}
		}
		String strOrderBy = "";
		if (xxxObject.containsKey("orderby")) {
			strOrderBy = xxxObject.get("orderby") == null ? "" : String.valueOf(xxxObject.get("orderby"));
		}
		int numOrderBy = 0;
		if ("".equals(strOrderBy) || strOrderBy == null) {
			numOrderBy = 0;
		} else {
			numOrderBy = Integer.parseInt(strOrderBy);
		}

		String strPath = "";
		if (xxxObject.containsKey("accessPath")) {
			strPath = xxxObject.get("accessPath") == null ? "" : String.valueOf(xxxObject.get("accessPath"));
		}

		if (!"".equals(strSysFileName) && strSysFileName != null && !"".equals(strUseFileName)
				&& strUseFileName != null) {
			PsTzFormAttT psTzFormAttT = new PsTzFormAttT();
			psTzFormAttT.setTzAppInsId(numAppInsId);
			psTzFormAttT.setTzXxxBh(strParentItemId);
			psTzFormAttT.setTzIndex(numOrderBy);
			psTzFormAttT.setTzAccessPath(strPath);
			psTzFormAttT.setAttachsysfilename(strSysFileName);
			psTzFormAttT.setAttachuserfile(strUseFileName);
			psTzFormAttT.setRowAddedOprid(oprid);
			psTzFormAttT.setRowAddedDttm(new Date());
			psTzFormAttT.setRowLastmantOprid(oprid);
			psTzFormAttT.setRowLastmantDttm(new Date());
			psTzFormAttTMapper.insert(psTzFormAttT);
		}
	}

	/* 设置字段是否隐藏 */
	private void saveXxxHidden(Long numAppInsId, String strItemId, String strIsHidden) {
		/**/
		String sql = "SELECT COUNT(1) FROM PS_TZ_APP_HIDDEN_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
		int count = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strItemId }, "Integer");
		if (count > 0) {
			PsTzAppHiddenT psTzAppHiddenT = new PsTzAppHiddenT();
			psTzAppHiddenT.setTzAppInsId(numAppInsId);
			psTzAppHiddenT.setTzXxxBh(strItemId);
			psTzAppHiddenT.setTzIsHidden(strIsHidden);
			psTzAppHiddenTMapper.updateByPrimaryKeySelective(psTzAppHiddenT);
		} else {
			PsTzAppHiddenT psTzAppHiddenT = new PsTzAppHiddenT();
			psTzAppHiddenT.setTzAppInsId(numAppInsId);
			psTzAppHiddenT.setTzXxxBh(strItemId);
			psTzAppHiddenT.setTzIsHidden(strIsHidden);
			psTzAppHiddenTMapper.insert(psTzAppHiddenT);
		}
	}

	// 保存报名人信息
	private void saveBmrPhoto(String strParentItemId, Map<String, Object> xxxObject, Long numAppInsId) {

		String strSysFileName = "";
		if (xxxObject.containsKey("sysFileName")) {
			strSysFileName = xxxObject.get("sysFileName") == null ? "" : String.valueOf(xxxObject.get("sysFileName"));
		}
		String strUseFileName = "";
		if (xxxObject.containsKey("filename")) {
			strUseFileName = xxxObject.get("filename") == null ? "" : String.valueOf(xxxObject.get("filename"));
		}
		String strImaPath = "";
		if (xxxObject.containsKey("imaPath")) {
			strImaPath = xxxObject.get("imaPath") == null ? "" : String.valueOf(xxxObject.get("imaPath"));
		}
		String strPath = "";
		if (xxxObject.containsKey("path")) {
			strPath = xxxObject.get("path") == null ? "" : String.valueOf(xxxObject.get("path"));
		}

		if (!"".equals(strSysFileName) && !"".equals(strUseFileName) && !"".equals(strImaPath) && !"".equals(strPath)) {

			String strAttPurl = "";
			String strTzAttachSysfile = "";
			String sql = "SELECT TZ_ATT_P_URL,A.TZ_ATTACHSYSFILENA FROM PS_TZ_FORM_PHOTO_T A,PS_TZ_OPR_PHOTO_T B WHERE A.TZ_ATTACHSYSFILENA=B.TZ_ATTACHSYSFILENA AND A.TZ_APP_INS_ID=?";
			Map<String, Object> Map = sqlQuery.queryForMap(sql, new Object[] { numAppInsId });
			strAttPurl = Map.get("sysFileName") == null ? "" : String.valueOf(Map.get("TZ_ATT_P_URL"));
			strTzAttachSysfile = Map.get("TZ_ATTACHSYSFILENA") == null ? ""
					: String.valueOf(Map.get("TZ_ATTACHSYSFILENA"));
			sql = "SELECT COUNT(1) FROM PS_TZ_FORM_PHOTO_T WHERE TZ_APP_INS_ID = ?";
			int count = sqlQuery.queryForObject(sql, new Object[] { numAppInsId }, "Integer");
			if (count > 0) {
				PsTzFormPhotoT psTzFormPhotoT = new PsTzFormPhotoT();
				psTzFormPhotoT.setTzAppInsId(numAppInsId);
				psTzFormPhotoT.setTzAttachsysfilena(strSysFileName);
				psTzFormPhotoTMapper.updateByPrimaryKeySelective(psTzFormPhotoT);
			} else {
				PsTzFormPhotoT psTzFormPhotoT = new PsTzFormPhotoT();
				psTzFormPhotoT.setTzAppInsId(numAppInsId);
				psTzFormPhotoT.setTzAttachsysfilena(strSysFileName);
				psTzFormPhotoTMapper.insert(psTzFormPhotoT);
			}
			// 更新TZ_OPR_PHOTO_T//
			sql = "SELECT COUNT(1) FROM PS_TZ_OPR_PHOTO_T WHERE TZ_ATTACHSYSFILENA = ?";
			int counFile = sqlQuery.queryForObject(sql, new Object[] { strSysFileName }, "Integer");
			if (counFile > 0) {
				PsTzOprPhotoT psTzOprPhotoT = new PsTzOprPhotoT();
				psTzOprPhotoT.setTzAttachsysfilena(strSysFileName);
				psTzOprPhotoT.setTzAttachfileName(strUseFileName);
				psTzOprPhotoT.setTzAttAUrl(strImaPath);
				psTzOprPhotoT.setTzAttPUrl(strPath);
				psTzOprPhotoTMapper.updateByPrimaryKeySelective(psTzOprPhotoT);
			} else {
				PsTzOprPhotoT psTzOprPhotoT = new PsTzOprPhotoT();
				psTzOprPhotoT.setTzAttachsysfilena(strSysFileName);
				psTzOprPhotoT.setTzAttachfileName(strUseFileName);
				psTzOprPhotoT.setTzAttAUrl(strImaPath);
				psTzOprPhotoT.setTzAttPUrl(strPath);
				psTzOprPhotoTMapper.insert(psTzOprPhotoT);
			}
		}
	}

	// 检查是否填写完成
	private String checkFiledValid(Long numAppInsId, String strTplId, String strPageId, String strOtype) {
		String returnMsg = "";

		/* 信息项编号 */
		String strXxxBh = "";

		/* 信息项名称 */
		String strXxxMc = "";

		/* 控件类名称 */
		String strComMc = "";

		/* 分页号 */
		int numPageNo;

		/* 信息项日期格式 */
		String strXxxRqgs = "";

		/* 信息项日期年份最小值 */
		String strXxxXfmin = "";

		/* 信息项日期年份最大值 */
		String strXxxXfmax = "";

		/* 信息项多选最少选择数量 */
		String strXxxZsxzgs = "";

		/* 信息项多选最多选择数量 */
		String strXxxZdxzgs = "";

		/* 信息项文件允许上传类型 */
		String strXxxYxsclx = "";

		/* 信息项文件允许上传大小 */
		String strXxxYxscdx = "";

		/* 信息项是否必填 */
		String strXxxBtBz = "";

		/* 信息项是否启用字数范围 */
		String strXxxCharBz = "";

		/* 信息项字数最小长度 */
		int numXxxMinlen;

		/* 信息项字数最大长度 */
		int numXxxMaxlen;

		/* 信息项是否启用数字范围 */
		String strXxxNumBz = "";

		/* 信息项字数最小长度 */
		int numXxxMin;

		/* 信息项字数最大长度 */
		int numXxxMax;

		/* 信息项字段小数位数 */
		String strXxxXsws = "";

		/* 信息项字段固定格式校验 */
		String strXxxGdgsjy = "";

		/* 信息项字段是否多容器 */
		String strXxxDrqBz = "";

		/* 信息项最小行记录数 */
		int numXxxMinLine;

		/* 信息项最大行记录数 */
		int numXxxMaxLine;

		/* 推荐信收集齐前是否允许提交报名表 */
		String strTjxSub = "";

		/* 信息项校验规则 */
		String strJygzId;

		String strJygzTsxx;

		/* 信息项校验程序 */
		String strPath, strName, strMethod;

		int numCurrentPageNo = 0;

		ArrayList<Integer> listPageNo = new ArrayList<Integer>();

		try {
			if (!"".equals(strPageId)) {
				String sqlGetPageNo = "SELECT TZ_PAGE_NO FROM PS_TZ_APP_XXXPZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_BH = ?";
				numCurrentPageNo = sqlQuery.queryForObject(sqlGetPageNo, new Object[] { strTplId, strPageId },
						"Integer");
			}

			String sql = tzSQLObject.getSQLText("SQL.TZWebsiteApplicationBundle.TZ_APP_ONLINE_CHECK_SQL");

			List<?> listData = sqlQuery.queryForList(sql, new Object[] { strTplId });
			for (Object objData : listData) {
				Map<String, Object> MapData = (Map<String, Object>) objData;
				strXxxBh = MapData.get("TZ_XXX_BH") == null ? "" : String.valueOf(MapData.get("TZ_XXX_BH"));
				strXxxMc = MapData.get("TZ_XXX_MC") == null ? "" : String.valueOf(MapData.get("TZ_XXX_MC"));
				strComMc = MapData.get("TZ_COM_LMC") == null ? "" : String.valueOf(MapData.get("TZ_COM_LMC"));
				numPageNo = MapData.get("TZ_PAGE_NO") == null ? 0
						: Integer.valueOf(String.valueOf(MapData.get("TZ_PAGE_NO")));
				strXxxRqgs = MapData.get("TZ_XXX_RQGS") == null ? "" : String.valueOf(MapData.get("TZ_XXX_RQGS"));
				strXxxXfmin = MapData.get("TZ_XXX_NFMIN") == null ? "" : String.valueOf(MapData.get("TZ_XXX_NFMIN"));
				strXxxXfmax = MapData.get("TZ_XXX_NFMAX") == null ? "" : String.valueOf(MapData.get("TZ_XXX_NFMAX"));
				strXxxZsxzgs = MapData.get("TZ_XXX_ZSXZGS") == null ? "" : String.valueOf(MapData.get("TZ_XXX_ZSXZGS"));
				strXxxZdxzgs = MapData.get("TZ_XXX_ZDXZGS") == null ? "" : String.valueOf(MapData.get("TZ_XXX_ZDXZGS"));
				strXxxYxsclx = MapData.get("TZ_XXX_YXSCLX") == null ? "" : String.valueOf(MapData.get("TZ_XXX_YXSCLX"));
				strXxxYxscdx = MapData.get("TZ_XXX_YXSCDX") == null ? "" : String.valueOf(MapData.get("TZ_XXX_YXSCDX"));
				strXxxBtBz = MapData.get("TZ_XXX_BT_BZ") == null ? "" : String.valueOf(MapData.get("TZ_XXX_BT_BZ"));
				strXxxCharBz = MapData.get("TZ_XXX_CHAR_BZ") == null ? ""
						: String.valueOf(MapData.get("TZ_XXX_CHAR_BZ"));
				numXxxMinlen = MapData.get("TZ_XXX_MINLEN") == null ? 0
						: Integer.parseInt(String.valueOf(MapData.get("TZ_XXX_MINLEN")));
				numXxxMaxlen = MapData.get("TZ_XXX_MAXLEN") == null ? 0
						: Integer.parseInt(String.valueOf(MapData.get("TZ_XXX_MAXLEN")));
				strXxxNumBz = MapData.get("TZ_XXX_NUM_BZ") == null ? "" : String.valueOf(MapData.get("TZ_XXX_NUM_BZ"));
				numXxxMin = MapData.get("TZ_XXX_MIN") == null ? 0
						: Integer.parseInt(String.valueOf(MapData.get("TZ_XXX_MIN")));
				numXxxMax = MapData.get("TZ_XXX_MAX") == null ? 0
						: Integer.parseInt(String.valueOf(MapData.get("TZ_XXX_MAX")));
				strXxxXsws = MapData.get("TZ_XXX_XSWS") == null ? "" : String.valueOf(MapData.get("TZ_XXX_XSWS"));
				strXxxGdgsjy = MapData.get("TZ_XXX_GDGSJY") == null ? "" : String.valueOf(MapData.get("TZ_XXX_GDGSJY"));
				strXxxDrqBz = MapData.get("TZ_XXX_DRQ_BZ") == null ? "" : String.valueOf(MapData.get("TZ_XXX_DRQ_BZ"));
				numXxxMinLine = MapData.get("TZ_XXX_MIN_LINE") == null ? 0
						: Integer.parseInt(String.valueOf(MapData.get("TZ_XXX_MIN_LINE")));
				numXxxMaxLine = MapData.get("TZ_XXX_MAX_LINE") == null ? 0
						: Integer.parseInt(String.valueOf(MapData.get("TZ_XXX_MAX_LINE")));
				strTjxSub = MapData.get("TZ_TJX_SUB") == null ? "" : String.valueOf(MapData.get("TZ_TJX_SUB"));
				strPath = MapData.get("TZ_APPCLS_PATH") == null ? "" : String.valueOf(MapData.get("TZ_APPCLS_PATH"));
				strName = MapData.get("TZ_APPCLS_NAME") == null ? "" : String.valueOf(MapData.get("TZ_APPCLS_NAME"));
				strMethod = MapData.get("TZ_APPCLS_METHOD") == null ? ""
						: String.valueOf(MapData.get("TZ_APPCLS_METHOD"));
				strJygzTsxx = MapData.get("TZ_JYGZ_TSXX") == null ? "" : String.valueOf(MapData.get("TZ_JYGZ_TSXX"));

				if ("save".equals(strOtype)) {
					if (numCurrentPageNo == numPageNo) {
						/*
						 * String[] parameterTypes = new String[]
						 * {"Long","String","String","String","String",
						 * "int","String","String","String","String","String",
						 * "String","String","String","String","int","int",
						 * "String","int","int","String",
						 * "String","String","int","String","String"}; Object[]
						 * arglist = new
						 * Object[]{numAppInsId,strTplId,strXxxBh,strXxxMc,
						 * strComMc,
						 * numPageNo,strXxxRqgs,strXxxXfmin,strXxxXfmax,
						 * strXxxZsxzgs,strXxxZdxzgs,
						 * strXxxYxsclx,strXxxYxscdx,strXxxBtBz,strXxxCharBz,
						 * numXxxMinlen,numXxxMaxlen,
						 * strXxxNumBz,numXxxMin,numXxxMax,strXxxXsws,
						 * strXxxGdgsjy,strXxxDrqBz,numXxxMinLine,strTjxSub,
						 * strJygzTsxx}; Object objs =
						 * ObjectDoMethod.Load(strPath + "." + strName,
						 * strMethod, parameterTypes, arglist); String strReturn
						 * = String.valueOf(objs);
						 */
						tzOnlineAppUtility tzOnlineAppUtility = (tzOnlineAppUtility) ctx
								.getBean(strPath + "." + strName);
						String strReturn = "";
						switch (strMethod) {
						case "requireValidator":
							strReturn = tzOnlineAppUtility.requireValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, strTjxSub, strJygzTsxx);
							break;
						case "ahphValidator":
							strReturn = tzOnlineAppUtility.ahphValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, strTjxSub, strJygzTsxx);
							break;
						case "charLenValidator":
							strReturn = tzOnlineAppUtility.charLenValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, strTjxSub, strJygzTsxx);
							break;
						case "valueValidator":
							strReturn = tzOnlineAppUtility.valueValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, strTjxSub, strJygzTsxx);
							break;
						case "regularValidator":
							strReturn = tzOnlineAppUtility.regularValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, strTjxSub, strJygzTsxx);
							break;
						case "dHLineValidator":
							strReturn = tzOnlineAppUtility.dHLineValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, strTjxSub, strJygzTsxx);
							break;
						case "refLetterValidator":
							strReturn = tzOnlineAppUtility.refLetterValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, strTjxSub, strJygzTsxx);
							break;
						}

						if (!"".equals(strReturn)) {
							returnMsg = strReturn;
							break;
						}
					}
				} else {
					/*
					 * String[] parameterTypes = new String[] {"String[]" };
					 * Object[] arglist = new
					 * Object[]{numAppInsId,strTplId,strXxxBh,strXxxMc,strComMc,
					 * numPageNo,strXxxRqgs,strXxxXfmin,strXxxXfmax,strXxxZsxzgs
					 * ,strXxxZdxzgs,
					 * strXxxYxsclx,strXxxYxscdx,strXxxBtBz,strXxxCharBz,
					 * numXxxMinlen,numXxxMaxlen,strXxxNumBz,numXxxMin,numXxxMax
					 * ,strXxxXsws,
					 * strXxxGdgsjy,strXxxDrqBz,numXxxMinLine,strTjxSub,
					 * strJygzTsxx}; Object objs = ObjectDoMethod.Load(strPath +
					 * "." + strName, strMethod, parameterTypes, arglist);
					 * String strReturn = String.valueOf(objs);
					 */
					tzOnlineAppUtility tzOnlineAppUtility = (tzOnlineAppUtility) ctx.getBean(strPath + "." + strName);
					String strReturn = "";
					switch (strMethod) {
					case "requireValidator":
						strReturn = tzOnlineAppUtility.requireValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
								strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs, strXxxZdxzgs,
								strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen, numXxxMaxlen,
								strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy, strXxxDrqBz, numXxxMinLine,
								strTjxSub, strJygzTsxx);
						break;
					case "ahphValidator":
						strReturn = tzOnlineAppUtility.ahphValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
								strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs, strXxxZdxzgs,
								strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen, numXxxMaxlen,
								strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy, strXxxDrqBz, numXxxMinLine,
								strTjxSub, strJygzTsxx);
						break;
					case "charLenValidator":
						strReturn = tzOnlineAppUtility.charLenValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
								strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs, strXxxZdxzgs,
								strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen, numXxxMaxlen,
								strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy, strXxxDrqBz, numXxxMinLine,
								strTjxSub, strJygzTsxx);
						break;
					case "valueValidator":
						strReturn = tzOnlineAppUtility.valueValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
								strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs, strXxxZdxzgs,
								strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen, numXxxMaxlen,
								strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy, strXxxDrqBz, numXxxMinLine,
								strTjxSub, strJygzTsxx);
						break;
					case "regularValidator":
						strReturn = tzOnlineAppUtility.regularValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
								strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs, strXxxZdxzgs,
								strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen, numXxxMaxlen,
								strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy, strXxxDrqBz, numXxxMinLine,
								strTjxSub, strJygzTsxx);
						break;
					case "dHLineValidator":
						strReturn = tzOnlineAppUtility.dHLineValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
								strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs, strXxxZdxzgs,
								strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen, numXxxMaxlen,
								strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy, strXxxDrqBz, numXxxMinLine,
								strTjxSub, strJygzTsxx);
						break;
					case "refLetterValidator":
						strReturn = tzOnlineAppUtility.refLetterValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
								strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs, strXxxZdxzgs,
								strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen, numXxxMaxlen,
								strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy, strXxxDrqBz, numXxxMinLine,
								strTjxSub, strJygzTsxx);
						break;
					case "VerificationCodeValidator":
						strReturn = tzOnlineAppUtility.VerificationCodeValidator(numAppInsId, strTplId, strXxxBh,
								strXxxMc, strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
								strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
								numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy, strXxxDrqBz,
								numXxxMinLine, strTjxSub, strJygzTsxx);
						break;
					}
					if (!"".equals(strReturn)) {
						if (!listPageNo.contains(numPageNo)) {
							listPageNo.add(numPageNo);
						}
						returnMsg = returnMsg + strReturn + "<br/>";
					}
				}
			}

			if ("submit".equals(strOtype)) {
				// 页面全部设置成完成

				String sqlGetPageXxxBh = "SELECT TZ_XXX_BH FROM PS_TZ_APP_XXXPZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_COM_LMC = ?";
				List<?> ListPageXxxBh = sqlQuery.queryForList(sqlGetPageXxxBh, new Object[] { strTplId, "Page" });
				for (Object ObjValue : ListPageXxxBh) {
					Map<String, Object> MapXxxBh = (Map<String, Object>) ObjValue;
					String strPageXxxBh = MapXxxBh.get("TZ_XXX_BH") == null ? ""
							: String.valueOf(MapXxxBh.get("TZ_XXX_BH"));
					if (strPageXxxBh != null && !"".equals(strPageXxxBh)) {
						this.savePageCompleteState(numAppInsId, strPageXxxBh, "Y");
					}
				}

				for (Integer numPageNo2 : listPageNo) {
					String sqlGetXxxBh = "SELECT TZ_XXX_BH FROM PS_TZ_APP_XXXPZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_COM_LMC = ? AND TZ_PAGE_NO = ?";

					String strXxxBh2 = sqlQuery.queryForObject(sqlGetXxxBh,
							new Object[] { strTplId, "Page", numPageNo2 }, "String");
					if (strXxxBh2 != null && !"".equals(strXxxBh2)) {
						this.savePageCompleteState(numAppInsId, strXxxBh2, "N");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnMsg;
	}

	// 更新页面完成状态
	private void savePageCompleteState(Long numAppInsId, String strXxxBh, String strPageCompleteState) {

		String sql = "SELECT COUNT(1) FROM PS_TZ_APP_COMP_TBL WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
		int count = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strXxxBh }, "Integer");
		if (count > 0) {
			PsTzAppCompTbl psTzAppCompTbl = new PsTzAppCompTbl();
			psTzAppCompTbl.setTzAppInsId(numAppInsId);
			psTzAppCompTbl.setTzXxxBh(strXxxBh);
			psTzAppCompTbl.setTzHasComplete(strPageCompleteState);
			psTzAppCompTblMapper.updateByPrimaryKeySelective(psTzAppCompTbl);
		} else {
			PsTzAppCompTbl psTzAppCompTbl = new PsTzAppCompTbl();
			psTzAppCompTbl.setTzAppInsId(numAppInsId);
			psTzAppCompTbl.setTzXxxBh(strXxxBh);
			psTzAppCompTbl.setTzHasComplete(strPageCompleteState);
			psTzAppCompTblMapper.insert(psTzAppCompTbl);
		}
	}

	/* 报名表提交后发送邮件 */
	private String sendSubmitEmail(Long numAppInsId, String strTplId, String strAppOprId, String strAppOrgId,
			String strTplType) {

		String returnMsg = "true";

		// 收件人Email
		String strEmail = "";
		// 收件人姓名
		String strName = "";
		// 邮件模版
		String strEmlTmpId = "";
		String sql = "SELECT TZ_EML_MODAL_ID FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ? AND TZ_ISSENDMAIL = 'Y'";
		strEmlTmpId = sqlQuery.queryForObject(sql, new Object[] { strTplId }, "String");
		if (!"".equals(strEmlTmpId) && strEmlTmpId != null) {
			if ("BMB".equals(strTplType)) {
				sql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
				strName = sqlQuery.queryForObject(sql, new Object[] { strAppOprId }, "String");
				sql = "SELECT TZ_ZY_EMAIL FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY='ZSBM' AND TZ_LYDX_ID=?";
				strEmail = sqlQuery.queryForObject(sql, new Object[] { numAppInsId }, "String");
				// 创建邮件短信发送任务
				String strTaskId = createTaskServiceImpl.createTaskIns(strAppOrgId, strEmlTmpId, "MAL", "A");
				if (strTaskId == null || "".equals(strTaskId)) {
					return "false";
				}
				// 创建短信、邮件发送的听众;
				String createAudience = createTaskServiceImpl.createAudience(strTaskId, strAppOrgId, "报名表提交发送邮件",
						"BMB");
				if ("".equals(createAudience) || createAudience == null) {
					return "false";
				}
				// 为听众添加听众成员
				boolean addAudCy = createTaskServiceImpl.addAudCy(createAudience, strName, strName, "", "", strEmail,
						"", "", strAppOprId, "", "", String.valueOf(numAppInsId));
				if (!addAudCy) {
					return "false";
				}
				// 得到创建的任务ID
				if ("".equals(strTaskId) || strTaskId == null) {
					return "false";
				} else {
					// 发送邮件
					sendSmsOrMalServiceImpl.send(strTaskId, "");
				}
			} else {
				return "true";
			}
		} else {
			return "true";
		}
		return returnMsg;
	}

	// 同步报名人联系方式
	private void savaContactInfo(Long numAppInsId, String strTplId, String strAppOprId) {
		// 注册信息

		/* 主要手机 */
		String strZysj = "";
		String strZysjHb = "";
		/* 备用手机 */
		String strBysj = "";
		String strBysjHb = "";
		/* 主要电话 */
		String strZydh = "";
		String strZydhHb = "";
		/* 备用电话 */
		String strBydh = "";
		String strBydhHb = "";
		/* 主要邮箱 */
		String strZyyx = "";
		String strZyyxHb = "";
		/* 备用邮箱 */
		String strByyx = "";
		String strByyxHb = "";
		/* 主要地址 */
		String strZydz = "";
		String strZydzHb = "";
		/* 主要邮编 */
		String strZyyb = "";
		String strZyybHb = "";
		/* 备要地址 */
		String strBydz = "";
		String strBydzHb = "";
		/* 备要邮编 */
		String strByyb = "";
		String strByybHb = "";
		/* 微信 */
		String strWx = "";
		String strWxHb = "";
		/* skype帐号 */
		String strSkype = "";
		String strSkypeHb = "";

		/* idcard */

		String strIdCard = "";

		String strDxxxBh = "";
		String strXxxBhLike = "";

		String strComLmc;
		String strXxxBh;
		String strSyncType = "";
		String strSyncSep = "";
		String sqlGetSyncXxx = "";
		sqlGetSyncXxx = "SELECT A.TZ_COM_LMC,A.TZ_XXX_BH,B.TZ_SYNC_TYPE,B.TZ_SYNC_SEP FROM PS_TZ_APP_XXXPZ_T A,PS_TZ_APPXX_SYNC_T B WHERE A.TZ_APP_TPL_ID = ? AND A.TZ_APP_TPL_ID = B.TZ_APP_TPL_ID AND A.TZ_XXX_BH = B.TZ_XXX_BH AND B.TZ_QY_BZ = 'Y' AND B.TZ_SYNC_TYPE <> ' ' ORDER BY B.TZ_SYNC_ORDER";
		List<?> listData = sqlQuery.queryForList(sqlGetSyncXxx, new Object[] { strTplId });
		for (Object objData : listData) {
			Map<String, Object> MapData = (Map<String, Object>) objData;
			strComLmc = MapData.get("TZ_COM_LMC") == null ? "" : String.valueOf(MapData.get("TZ_COM_LMC"));
			strXxxBh = MapData.get("TZ_XXX_BH") == null ? "" : String.valueOf(MapData.get("TZ_XXX_BH"));
			strSyncType = MapData.get("TZ_SYNC_TYPE") == null ? "" : String.valueOf(MapData.get("TZ_SYNC_TYPE"));
			strSyncSep = MapData.get("TZ_SYNC_SEP") == null ? "" : String.valueOf(MapData.get("TZ_SYNC_SEP"));
			// 查看是否在容器中
			String sql = "SELECT TZ_D_XXX_BH FROM PS_TZ_TEMP_FIELD_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ? LIMIT 0,1";
			strDxxxBh = sqlQuery.queryForObject(sql, new Object[] { strTplId, strXxxBh }, "String");
			if (!"".equals(strDxxxBh) && strDxxxBh != null) {
				strXxxBhLike = strDxxxBh + strXxxBh;
			} else {
				strDxxxBh = strXxxBh;
				strXxxBhLike = strXxxBh;
			}

			String strPhoneArea = "";
			String strPhoneNo = "";

			String strProvince = "";
			String strAddress = "";

			switch (strSyncType) {
			case "ZYSJ":
				if ("mobilePhone".equals(strComLmc)) {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = 'mobile_area'";
					strPhoneArea = sqlQuery.queryForObject(sql,
							new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" }, "String");
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = 'mobile_no'";
					strPhoneNo = sqlQuery.queryForObject(sql,
							new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" }, "String");
					if (!"".equals(strPhoneNo) && strPhoneNo != null) {
						if (!"".equals(strPhoneArea) && strPhoneArea != null) {
							strZysj = strPhoneArea + "-" + strPhoneNo;
						} else {
							strZysj = strPhoneNo;
						}
					}
				} else {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
					strZysj = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh }, "String");
				}
				// 主要手机合并
				if (!"".equals(strZysjHb) && strZysjHb != null) {
					if (!"".equals(strZysj) && strZysj != null) {
						strZysjHb = strZysjHb + strSyncSep + strZysj;
					}
				} else {
					if (!"".equals(strZysj) && strZysj != null) {
						strZysjHb = strZysj;
					}
				}
				break;
			case "BYSJ":
				if ("mobilePhone".equals(strComLmc)) {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = 'mobile_area'";
					strPhoneArea = sqlQuery.queryForObject(sql,
							new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" }, "String");
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = 'mobile_no'";
					strPhoneNo = sqlQuery.queryForObject(sql,
							new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" }, "String");
					if (!"".equals(strPhoneNo) && strPhoneNo != null) {
						if (!"".equals(strPhoneArea) && strPhoneArea != null) {
							strBysj = strPhoneArea + "-" + strPhoneNo;
						} else {
							strBysj = strPhoneNo;
						}
					}
				} else {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
					strBysj = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh }, "String");
				}
				// 备用手机合并
				if (!"".equals(strBysjHb) && strBysjHb != null) {
					if (!"".equals(strBysj) && strBysj != null) {
						strBysjHb = strBysjHb + strSyncSep + strBysj;
					}
				} else {
					if (!"".equals(strBysj) && strBysj != null) {
						strBysjHb = strBysj;
					}
				}
				break;
			case "ZYDH":
				if ("mobilePhone".equals(strComLmc)) {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = 'mobile_area'";
					strPhoneArea = sqlQuery.queryForObject(sql,
							new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" }, "String");
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = 'mobile_no'";
					strPhoneNo = sqlQuery.queryForObject(sql,
							new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" }, "String");
					if (!"".equals(strPhoneNo) && strPhoneNo != null) {
						if (!"".equals(strPhoneArea) && strPhoneArea != null) {
							strZydh = strPhoneArea + "-" + strPhoneNo;
						} else {
							strZydh = strPhoneNo;
						}
					}
				} else {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
					strZydh = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh }, "String");
				}
				// 主要电话合并
				if (!"".equals(strZydhHb) && strZydhHb != null) {
					if (!"".equals(strZydh) && strZydh != null) {
						strZydhHb = strBysjHb + strSyncSep + strZydh;
					}
				} else {
					if (!"".equals(strZydh) && strZydh != null) {
						strZydhHb = strZydh;
					}
				}
				break;
			case "BYDH":
				if ("mobilePhone".equals(strComLmc)) {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = 'mobile_area'";
					strPhoneArea = sqlQuery.queryForObject(sql,
							new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" }, "String");
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = 'mobile_no'";
					strPhoneNo = sqlQuery.queryForObject(sql,
							new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" }, "String");
					if (!"".equals(strPhoneNo) && strPhoneNo != null) {
						if (!"".equals(strPhoneArea) && strPhoneArea != null) {
							strBydh = strPhoneArea + "-" + strPhoneNo;
						} else {
							strBydh = strPhoneNo;
						}
					}
				} else {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
					strBysj = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh }, "String");
				}
				// 备用电话合并
				if (!"".equals(strBydhHb) && strBydhHb != null) {
					if (!"".equals(strBydh) && strBydh != null) {
						strBydhHb = strBydhHb + strSyncSep + strBydh;
					}
				} else {
					if (!"".equals(strBydh) && strBydh != null) {
						strBydhHb = strBydh;
					}
				}
				break;
			case "ZYYX":
				sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
				strZyyx = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh }, "String");
				// 主要邮箱合并
				if (!"".equals(strZyyxHb) && strZyyxHb != null) {
					if (!"".equals(strZyyx) && strZyyx != null) {
						strZyyxHb = strZyyxHb + strSyncSep + strZyyx;
					}
				} else {
					if (!"".equals(strZyyx) && strZyyx != null) {
						strZyyxHb = strZyyx;
					}
				}
				break;
			case "BYYX":
				sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
				strByyx = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh }, "String");
				// 备用邮箱合并
				if (!"".equals(strByyxHb) && strByyxHb != null) {
					if (!"".equals(strZyyx) && strZyyx != null) {
						strByyxHb = strByyxHb + strSyncSep + strByyx;
					}
				} else {
					if (!"".equals(strByyx) && strByyx != null) {
						strByyxHb = strByyx;
					}
				}
				break;
			case "ZYDZ":
				if ("MailingAddress".equals(strComLmc)) {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = 'province'";
					strProvince = sqlQuery.queryForObject(sql,
							new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" }, "String");
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = 'address'";
					strAddress = sqlQuery.queryForObject(sql,
							new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" }, "String");
					if (!"".equals(strProvince) && strProvince != null && !"".equals(strAddress)
							&& strAddress != null) {
						strZydz = strProvince + strAddress;
					}
				} else {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
					strZydz = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh }, "String");
				}
				// 主要地址合并
				if (!"".equals(strZydzHb) && strZydzHb != null) {
					if (!"".equals(strZydz) && strZydz != null) {
						strZydzHb = strZydzHb + strSyncSep + strZydz;
					}
				} else {
					if (!"".equals(strZydz) && strZydz != null) {
						strZydzHb = strZydz;
					}
				}
				break;
			case "ZYYB":
				sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
				strZyyb = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh }, "String");
				// 主要邮编合并
				if (!"".equals(strZyybHb) && strZyybHb != null) {
					if (!"".equals(strZyyb) && strZyyb != null) {
						strByyxHb = strByyxHb + strSyncSep + strZyyb;
					}
				} else {
					if (!"".equals(strZyyb) && strZyyb != null) {
						strZyybHb = strZyyb;
					}
				}
				break;
			case "BYDZ":
				if ("MailingAddress".equals(strComLmc)) {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = 'province'";
					strProvince = sqlQuery.queryForObject(sql,
							new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" }, "String");
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = 'address'";
					strAddress = sqlQuery.queryForObject(sql,
							new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" }, "String");
					if (!"".equals(strProvince) && strProvince != null && !"".equals(strAddress)
							&& strAddress != null) {
						strBydz = strProvince + strAddress;
					}
				} else {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
					strBydz = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh }, "String");
				}
				// 次要地址合并
				if (!"".equals(strBydzHb) && strBydzHb != null) {
					if (!"".equals(strBydz) && strBydz != null) {
						strBydzHb = strZydzHb + strSyncSep + strBydz;
					}
				} else {
					if (!"".equals(strBydz) && strBydz != null) {
						strBydzHb = strBydz;
					}
				}
				break;
			case "BYYB":
				sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
				strByyb = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh }, "String");
				// 备用邮编合并
				if (!"".equals(strByybHb) && strByybHb != null) {
					if (!"".equals(strByyb) && strByyb != null) {
						strByybHb = strByybHb + strSyncSep + strByyb;
					}
				} else {
					if (!"".equals(strByyb) && strByyb != null) {
						strByybHb = strByyb;
					}
				}
				break;
			case "WX":
				sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
				strWx = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh }, "String");
				// 微信合并
				if (!"".equals(strWxHb) && strWxHb != null) {
					if (!"".equals(strWx) && strWx != null) {
						strWxHb = strByybHb + strSyncSep + strWx;
					}
				} else {
					if (!"".equals(strWx) && strWx != null) {
						strWxHb = strWx;
					}
				}
				break;
			case "SKY":
				sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
				strSkype = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh }, "String");
				// Skype合并
				if (!"".equals(strSkypeHb) && strSkypeHb != null) {
					if (!"".equals(strSkype) && strSkype != null) {
						strSkypeHb = strByybHb + strSyncSep + strSkype;
					}
				} else {
					if (!"".equals(strSkype) && strSkype != null) {
						strSkypeHb = strSkype;
					}
				}
				break;
			case "IDCARD":
				/* 证件号码同步 */
				if ("CertificateNum".equals(strComLmc)) {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = 'com_CerNum'";
					strIdCard = sqlQuery.queryForObject(sql,
							new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" }, "String");

				} else {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
					strIdCard = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh },
							"String");
				}
				break;
			}
		}

		// 查询注册信息中的数据
		/* 主要手机 */
		String strZysjZc = "";
		/* 备用手机 */
		String strBysjZc = "";
		/* 主要电话 */
		String strZydhZc = "";
		/* 备用电话 */
		String strBydhZc = "";
		/* 主要邮箱 */
		String strZyyxZc = "";
		/* 备用邮箱 */
		String strByyxZc = "";
		/* 主要地址 */
		String strZydzZc = "";
		/* 主要邮编 */
		String strZyybZc = "";
		/* 备要地址 */
		String strBydzZc = "";
		/* 备要邮编 */
		String strByybZc = "";
		/* 微信 */
		String strWxZc = "";
		/* skype帐号 */
		String strSkypeZc = "";

		String sqlGetZcInfo = "SELECT TZ_ZY_SJ,TZ_CY_SJ,TZ_ZY_DH,TZ_CY_DH,TZ_ZY_EMAIL,TZ_CY_EMAIL,TZ_ZY_TXDZ,TZ_ZY_TXYB,TZ_CY_TXDZ,TZ_CY_TXYB,TZ_WEIXIN,TZ_SKYPE FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY = ? AND TZ_LYDX_ID = ?";
		Map<String, Object> MapGetZcInfo = sqlQuery.queryForMap(sqlGetZcInfo, new Object[] { "ZCYH", strAppOprId });
		if (MapGetZcInfo != null) {
			strZysjZc = MapGetZcInfo.get("TZ_ZY_SJ") == null ? "" : String.valueOf(MapGetZcInfo.get("TZ_ZY_SJ"));
			strBysjZc = MapGetZcInfo.get("TZ_CY_SJ") == null ? "" : String.valueOf(MapGetZcInfo.get("TZ_CY_SJ"));
			strZydhZc = MapGetZcInfo.get("TZ_ZY_DH") == null ? "" : String.valueOf(MapGetZcInfo.get("TZ_ZY_DH"));
			strBydhZc = MapGetZcInfo.get("TZ_CY_DH") == null ? "" : String.valueOf(MapGetZcInfo.get("TZ_CY_DH"));
			strZyyxZc = MapGetZcInfo.get("TZ_ZY_EMAIL") == null ? "" : String.valueOf(MapGetZcInfo.get("TZ_ZY_EMAIL"));
			strByyxZc = MapGetZcInfo.get("TZ_CY_EMAIL") == null ? "" : String.valueOf(MapGetZcInfo.get("TZ_CY_EMAIL"));
			strZydzZc = MapGetZcInfo.get("TZ_ZY_TXDZ") == null ? "" : String.valueOf(MapGetZcInfo.get("TZ_ZY_TXDZ"));
			strZyybZc = MapGetZcInfo.get("TZ_ZY_TXYB") == null ? "" : String.valueOf(MapGetZcInfo.get("TZ_ZY_TXYB"));
			strBydzZc = MapGetZcInfo.get("TZ_CY_TXDZ") == null ? "" : String.valueOf(MapGetZcInfo.get("TZ_CY_TXDZ"));
			strByybZc = MapGetZcInfo.get("TZ_CY_TXYB") == null ? "" : String.valueOf(MapGetZcInfo.get("TZ_CY_TXYB"));
			strWxZc = MapGetZcInfo.get("TZ_WEIXIN") == null ? "" : String.valueOf(MapGetZcInfo.get("TZ_WEIXIN"));
			strSkypeZc = MapGetZcInfo.get("TZ_SKYPE") == null ? "" : String.valueOf(MapGetZcInfo.get("TZ_SKYPE"));
			if ("".equals(strZysjHb) || strZysjHb == null) {
				strZysjHb = strZysjZc;
			}
			if ("".equals(strBysjHb) || strBysjHb == null) {
				strBysjHb = strBysjZc;
			}
			if ("".equals(strZydhHb) || strZydhHb == null) {
				strZydhHb = strZydhZc;
			}
			if ("".equals(strBydhHb) || strBydhHb == null) {
				strBydhHb = strBydhZc;
			}
			if ("".equals(strZyyxHb) || strZyyxHb == null) {
				strZyyxHb = strZyyxZc;
			}
			if ("".equals(strByyxHb) || strByyxHb == null) {
				strByyxHb = strByyxZc;
			}
			if ("".equals(strZydzHb) || strZydzHb == null) {
				strZydzHb = strZydzZc;
			}
			if ("".equals(strZyybHb) || strZyybHb == null) {
				strZyybHb = strZyybZc;
			}
			if ("".equals(strBydzHb) || strBydzHb == null) {
				strBydzHb = strBydzZc;
			}
			if ("".equals(strByybHb) || strByybHb == null) {
				strByybHb = strByybZc;
			}
			if ("".equals(strWxHb) || strWxHb == null) {
				strWxHb = strWxZc;
			}
			if ("".equals(strSkypeHb) || strSkypeHb == null) {
				strSkypeHb = strSkypeZc;
			}
		}

		// mysql如果字符过长会报错，需要截取长度
		if (strZysjHb.length() > 20) {
			strZysjHb = strZysjHb.substring(0, 20);
		}
		if (strBysjHb.length() > 20) {
			strBysjHb = strBysjHb.substring(0, 20);
		}
		if (strZydhHb.length() > 20) {
			strZydhHb = strZydhHb.substring(0, 20);
		}
		if (strBydhHb.length() > 20) {
			strBydhHb = strBydhHb.substring(0, 20);
		}
		if (strZyyxHb.length() > 100) {
			strZyyxHb = strZyyxHb.substring(0, 100);
		}
		if (strByyxHb.length() > 100) {
			strByyxHb = strByyxHb.substring(0, 100);
		}
		if (strZydzHb.length() > 254) {
			strZydzHb = strZydzHb.substring(0, 254);
		}
		if (strZyybHb.length() > 10) {
			strZyybHb = strZyybHb.substring(0, 10);
		}
		if (strBydzHb.length() > 254) {
			strBydzHb = strBydzHb.substring(0, 254);
		}
		if (strByybHb.length() > 10) {
			strByybHb = strByybHb.substring(0, 10);
		}
		if (strWxHb.length() > 20) {
			strWxHb = strWxHb.substring(0, 20);
		}
		if (strSkypeHb.length() > 70) {
			strSkypeHb = strSkypeHb.substring(0, 70);
		}
		if (strIdCard.length() > 20) {
			strIdCard = strIdCard.substring(0, 20);
		}

		String sqlCount = "SELECT COUNT(1) FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY = 'ZSBM' AND TZ_LYDX_ID = ?";
		String strAppInsId = String.valueOf(numAppInsId);
		int count = sqlQuery.queryForObject(sqlCount, new Object[] { strAppInsId }, "Integer");
		if (count > 0) {
			PsTzLxfsInfoTbl psTzLxfsInfoTbl = new PsTzLxfsInfoTbl();
			psTzLxfsInfoTbl.setTzLxfsLy("ZSBM");
			psTzLxfsInfoTbl.setTzLydxId(strAppInsId);
			psTzLxfsInfoTbl.setTzZySj(strZysjHb);
			psTzLxfsInfoTbl.setTzCySj(strBysjHb);
			psTzLxfsInfoTbl.setTzZyDh(strZydhHb);
			psTzLxfsInfoTbl.setTzCyDh(strBydhHb);
			psTzLxfsInfoTbl.setTzZyEmail(strZyyxHb);
			psTzLxfsInfoTbl.setTzCyEmail(strByyxHb);
			psTzLxfsInfoTbl.setTzZyTxdz(strZydzHb);
			psTzLxfsInfoTbl.setTzZyTxyb(strZyybHb);
			psTzLxfsInfoTbl.setTzCyTxdz(strBydzHb);
			psTzLxfsInfoTbl.setTzCyTxyb(strByybHb);
			psTzLxfsInfoTbl.setTzWeixin(strWxHb);
			psTzLxfsInfoTbl.setTzSkype(strSkypeHb);
			psTzLxfsInfoTblMapper.updateByPrimaryKeySelective(psTzLxfsInfoTbl);
		} else {
			PsTzLxfsInfoTbl psTzLxfsInfoTbl = new PsTzLxfsInfoTbl();
			psTzLxfsInfoTbl.setTzLxfsLy("ZSBM");
			psTzLxfsInfoTbl.setTzLydxId(strAppInsId);
			psTzLxfsInfoTbl.setTzZySj(strZysjHb);
			psTzLxfsInfoTbl.setTzCySj(strBysjHb);
			psTzLxfsInfoTbl.setTzZyDh(strZydhHb);
			psTzLxfsInfoTbl.setTzCyDh(strBydhHb);
			psTzLxfsInfoTbl.setTzZyEmail(strZyyxHb);
			psTzLxfsInfoTbl.setTzCyEmail(strByyxHb);
			psTzLxfsInfoTbl.setTzZyTxdz(strZydzHb);
			psTzLxfsInfoTbl.setTzZyTxyb(strZyybHb);
			psTzLxfsInfoTbl.setTzCyTxdz(strBydzHb);
			psTzLxfsInfoTbl.setTzCyTxyb(strByybHb);
			psTzLxfsInfoTbl.setTzWeixin(strWxHb);
			psTzLxfsInfoTbl.setTzSkype(strSkypeHb);
			psTzLxfsInfoTblMapper.insert(psTzLxfsInfoTbl);
		}

		/* 同步身份证信息 */
		if (!"".equals(strIdCard) && strIdCard != null) {
			String sqlRegInfoCount = "SELECT COUNT(1) FROM PS_TZ_REG_USER_T WHERE OPRID = ?";

			int regInfocount = sqlQuery.queryForObject(sqlRegInfoCount, new Object[] { strAppOprId }, "Integer");
			if (regInfocount > 0) {
				PsTzRegUserT psTzRegUserT = new PsTzRegUserT();
				psTzRegUserT.setOprid(strAppOprId);
				psTzRegUserT.setNationalId(strIdCard);
				psTzRegUserTMapper.updateByPrimaryKeySelective(psTzRegUserT);
			}
		}
	}

	private String createGuestUser(String strOrgId, String strName) {

		String allChar = "0123456789";
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < 6; i++) {
			sb.append(allChar.charAt(random.nextInt(allChar.length())));
		}
		String strTZ_REPASSWORD = sb.toString();

		// 开始创建账号;
		String oprId = "TZ_" + getSeqNum.getSeqNum("PSOPRDEFN", "OPRID");
		// 生成登录账号;
		String dlzh = SiteEnrollClsServiceImpl.tzGenerateAcount(strOrgId, oprId);

		// 保存用户信息;
		Psoprdefn psoprdefn = new Psoprdefn();
		psoprdefn.setOprid(oprId);
		String password = DESUtil.encrypt(strTZ_REPASSWORD, "TZGD_Tranzvision");
		psoprdefn.setOperpswd(password);
		psoprdefn.setAcctlock((short) 0);
		psoprdefn.setLastupdoprid(oprId);
		psoprdefn.setLastupddttm(new Date());
		psoprdefnMapper.insert(psoprdefn);

		PsTzAqYhxxTbl psTzAqYhxxTbl = new PsTzAqYhxxTbl();
		psTzAqYhxxTbl.setTzDlzhId(dlzh);
		psTzAqYhxxTbl.setTzJgId(strOrgId);
		psTzAqYhxxTbl.setOprid(oprId);
		psTzAqYhxxTbl.setTzRealname(strName);
		psTzAqYhxxTbl.setTzRylx("ZCYH");
		psTzAqYhxxTbl.setTzZhceDt(new Date());
		psTzAqYhxxTbl.setTzBjsEml("N");
		psTzAqYhxxTbl.setTzBjsSms("N");
		psTzAqYhxxTbl.setRowAddedDttm(new Date());
		psTzAqYhxxTbl.setRowAddedOprid(oprId);
		psTzAqYhxxTbl.setRowLastmantDttm(new Date());
		psTzAqYhxxTbl.setRowLastmantOprid(oprId);
		psTzAqYhxxTblMapper.insert(psTzAqYhxxTbl);

		// 保存用户注册信息
		PsTzRegUserT psTzRegUserT = new PsTzRegUserT();
		psTzRegUserT.setOprid(oprId);
		psTzRegUserT.setTzRealname(strName);
		psTzRegUserT.setRowAddedDttm(new Date());
		psTzRegUserT.setRowAddedOprid(oprId);
		psTzRegUserT.setRowLastmantDttm(new Date());
		psTzRegUserT.setRowLastmantOprid(oprId);
		psTzRegUserTMapper.insert(psTzRegUserT);

		// 添加角色;
		String roleSQL = " SELECT ROLENAME FROM PS_TZ_JG_ROLE_T WHERE TZ_JG_ID=? AND TZ_ROLE_TYPE='C'";
		List<Map<String, Object>> roleList = sqlQuery.queryForList(roleSQL, new Object[] { strOrgId });
		if (roleList != null && roleList.size() > 0) {
			for (int j = 0; j < roleList.size(); j++) {
				String rolename = (String) roleList.get(j).get("ROLENAME");
				Psroleuser psroleuser = new Psroleuser();
				psroleuser.setRoleuser(oprId);
				psroleuser.setRolename(rolename);
				psroleuser.setDynamicSw("N");
				psroleuserMapper.insert(psroleuser);
			}
		}

		return oprId;
	}

	// 检查推荐信对应页面完成状态
	private void checkRefletter(Long numAppInsId, String strTplId) {

		String strMsg = "";
		/* 信息项编号 */
		String strXxxBh = "";

		/* 信息项名称 */
		String strXxxMc = "";

		/* 控件类名称 */
		String strComMc = "";

		/* 分页号 */
		int numPageNo;

		/* 信息项日期格式 */
		String strXxxRqgs = "";

		/* 信息项日期年份最小值 */
		String strXxxXfmin = "";

		/* 信息项日期年份最大值 */
		String strXxxXfmax = "";

		/* 信息项多选最少选择数量 */
		String strXxxZsxzgs = "";

		/* 信息项多选最多选择数量 */
		String strXxxZdxzgs = "";

		/* 信息项文件允许上传类型 */
		String strXxxYxsclx = "";

		/* 信息项文件允许上传大小 */
		String strXxxYxscdx = "";

		/* 信息项是否必填 */
		String strXxxBtBz = "";

		/* 信息项是否启用字数范围 */
		String strXxxCharBz = "";

		/* 信息项字数最小长度 */
		int numXxxMinlen;

		/* 信息项字数最大长度 */
		int numXxxMaxlen;

		/* 信息项是否启用数字范围 */
		String strXxxNumBz = "";

		/* 信息项字数最小长度 */
		int numXxxMin;

		/* 信息项字数最大长度 */
		int numXxxMax;

		/* 信息项字段小数位数 */
		String strXxxXsws = "";

		/* 信息项字段固定格式校验 */
		String strXxxGdgsjy = "";

		/* 信息项字段是否多容器 */
		String strXxxDrqBz = "";

		/* 信息项最小行记录数 */
		int numXxxMinLine;

		/* 信息项最大行记录数 */
		int numXxxMaxLine;

		/* 推荐信收集齐前是否允许提交报名表 */
		String strTjxSub = "";

		/* 信息项校验规则 */
		String strJygzId;

		String strJygzTsxx;

		/* 信息项校验程序 */
		String strPath, strName, strMethod;

		String strPageXxxBh = "";

		String strPageNo = "";

		try {

			String sqlGetPageNo = "SELECT TZ_PAGE_NO FROM PS_TZ_APP_XXXPZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_COM_LMC = 'recommendletter' LIMIT 1";

			strPageNo = sqlQuery.queryForObject(sqlGetPageNo, new Object[] { strTplId }, "String");

			if (strPageNo != null) {
				numPageNo = Integer.parseInt(strPageNo);
				String sqlGetPageXxxBh = "SELECT TZ_XXX_BH FROM PS_TZ_APP_XXXPZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_PAGE_NO = ? AND TZ_COM_LMC = 'Page' LIMIT 1";
				strPageXxxBh = sqlQuery.queryForObject(sqlGetPageXxxBh, new Object[] { strTplId, numPageNo }, "String");

				String sql = tzSQLObject.getSQLText("SQL.TZWebsiteApplicationBundle.TZ_APP_ONLINE_CHECK_BYPAGE_SQL");
				List<?> listData = sqlQuery.queryForList(sql, new Object[] { strTplId, numPageNo });
				for (Object objData : listData) {
					Map<String, Object> MapData = (Map<String, Object>) objData;
					strXxxBh = MapData.get("TZ_XXX_BH") == null ? "" : String.valueOf(MapData.get("TZ_XXX_BH"));
					strXxxMc = MapData.get("TZ_XXX_MC") == null ? "" : String.valueOf(MapData.get("TZ_XXX_MC"));
					strComMc = MapData.get("TZ_COM_LMC") == null ? "" : String.valueOf(MapData.get("TZ_COM_LMC"));
					strXxxRqgs = MapData.get("TZ_XXX_RQGS") == null ? "" : String.valueOf(MapData.get("TZ_XXX_RQGS"));
					strXxxXfmin = MapData.get("TZ_XXX_NFMIN") == null ? ""
							: String.valueOf(MapData.get("TZ_XXX_NFMIN"));
					strXxxXfmax = MapData.get("TZ_XXX_NFMAX") == null ? ""
							: String.valueOf(MapData.get("TZ_XXX_NFMAX"));
					strXxxZsxzgs = MapData.get("TZ_XXX_ZSXZGS") == null ? ""
							: String.valueOf(MapData.get("TZ_XXX_ZSXZGS"));
					strXxxZdxzgs = MapData.get("TZ_XXX_ZDXZGS") == null ? ""
							: String.valueOf(MapData.get("TZ_XXX_ZDXZGS"));
					strXxxYxsclx = MapData.get("TZ_XXX_YXSCLX") == null ? ""
							: String.valueOf(MapData.get("TZ_XXX_YXSCLX"));
					strXxxYxscdx = MapData.get("TZ_XXX_YXSCDX") == null ? ""
							: String.valueOf(MapData.get("TZ_XXX_YXSCDX"));
					strXxxBtBz = MapData.get("TZ_XXX_BT_BZ") == null ? "" : String.valueOf(MapData.get("TZ_XXX_BT_BZ"));
					strXxxCharBz = MapData.get("TZ_XXX_CHAR_BZ") == null ? ""
							: String.valueOf(MapData.get("TZ_XXX_CHAR_BZ"));
					numXxxMinlen = MapData.get("TZ_XXX_MINLEN") == null ? 0
							: Integer.parseInt(String.valueOf(MapData.get("TZ_XXX_MINLEN")));
					numXxxMaxlen = MapData.get("TZ_XXX_MAXLEN") == null ? 0
							: Integer.parseInt(String.valueOf(MapData.get("TZ_XXX_MAXLEN")));
					strXxxNumBz = MapData.get("TZ_XXX_NUM_BZ") == null ? ""
							: String.valueOf(MapData.get("TZ_XXX_NUM_BZ"));
					numXxxMin = MapData.get("TZ_XXX_MIN") == null ? 0
							: Integer.parseInt(String.valueOf(MapData.get("TZ_XXX_MIN")));
					numXxxMax = MapData.get("TZ_XXX_MAX") == null ? 0
							: Integer.parseInt(String.valueOf(MapData.get("TZ_XXX_MAX")));
					strXxxXsws = MapData.get("TZ_XXX_XSWS") == null ? "" : String.valueOf(MapData.get("TZ_XXX_XSWS"));
					strXxxGdgsjy = MapData.get("TZ_XXX_GDGSJY") == null ? ""
							: String.valueOf(MapData.get("TZ_XXX_GDGSJY"));
					strXxxDrqBz = MapData.get("TZ_XXX_DRQ_BZ") == null ? ""
							: String.valueOf(MapData.get("TZ_XXX_DRQ_BZ"));
					numXxxMinLine = MapData.get("TZ_XXX_MIN_LINE") == null ? 0
							: Integer.parseInt(String.valueOf(MapData.get("TZ_XXX_MIN_LINE")));
					numXxxMaxLine = MapData.get("TZ_XXX_MAX_LINE") == null ? 0
							: Integer.parseInt(String.valueOf(MapData.get("TZ_XXX_MAX_LINE")));
					strTjxSub = MapData.get("TZ_TJX_SUB") == null ? "" : String.valueOf(MapData.get("TZ_TJX_SUB"));
					strPath = MapData.get("TZ_APPCLS_PATH") == null ? ""
							: String.valueOf(MapData.get("TZ_APPCLS_PATH"));
					strName = MapData.get("TZ_APPCLS_NAME") == null ? ""
							: String.valueOf(MapData.get("TZ_APPCLS_NAME"));
					strMethod = MapData.get("TZ_APPCLS_METHOD") == null ? ""
							: String.valueOf(MapData.get("TZ_APPCLS_METHOD"));
					strJygzTsxx = MapData.get("TZ_JYGZ_TSXX") == null ? ""
							: String.valueOf(MapData.get("TZ_JYGZ_TSXX"));

					if (!"".equals(strPath) && !"".equals(strName) && !"".equals(strMethod)) {
						tzOnlineAppUtility tzOnlineAppUtility = (tzOnlineAppUtility) ctx
								.getBean(strPath + "." + strName);
						String strReturn = "";
						switch (strMethod) {
						case "requireValidator":
							strReturn = tzOnlineAppUtility.requireValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, strTjxSub, strJygzTsxx);
							break;
						case "ahphValidator":
							strReturn = tzOnlineAppUtility.ahphValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, strTjxSub, strJygzTsxx);
							break;
						case "charLenValidator":
							strReturn = tzOnlineAppUtility.charLenValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, strTjxSub, strJygzTsxx);
							break;
						case "valueValidator":
							strReturn = tzOnlineAppUtility.valueValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, strTjxSub, strJygzTsxx);
							break;
						case "regularValidator":
							strReturn = tzOnlineAppUtility.regularValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, strTjxSub, strJygzTsxx);
							break;
						case "dHLineValidator":
							strReturn = tzOnlineAppUtility.dHLineValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, strTjxSub, strJygzTsxx);
							break;
						case "refLetterValidator":
							strReturn = tzOnlineAppUtility.refLetterValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, strTjxSub, strJygzTsxx);
							break;
						}
						if (!"".equals(strReturn)) {
							strMsg = strReturn;
							break;
						}
					}
				}
				if (!"".equals(strPageXxxBh)) {
					if ("".equals(strMsg)) {
						this.savePageCompleteState(numAppInsId, strPageXxxBh, "Y");
					} else {
						this.savePageCompleteState(numAppInsId, strPageXxxBh, "N");
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
