package com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsTzAqYhxxTblMapper;
import com.tranzvision.gd.TZAccountMgBundle.dao.PsoprdefnMapper;
import com.tranzvision.gd.TZAccountMgBundle.dao.PsroleuserMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTbl;
import com.tranzvision.gd.TZAccountMgBundle.model.Psoprdefn;
import com.tranzvision.gd.TZAccountMgBundle.model.Psroleuser;
import com.tranzvision.gd.TZApplicationTemplateBundle.dao.PsTzApptplDyTMapper;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzApptplDyTWithBLOBs;
import com.tranzvision.gd.TZApplicationTemplateBundle.service.impl.TemplateEngine;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdKjComServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClassInfTMapper;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClassInfT;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzOprPhotoTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzRegUserTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzOprPhotoT;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzRegUserT;
import com.tranzvision.gd.TZRecommendationBundle.service.impl.TzTjxThanksServiceImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteEnrollClsServiceImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppInsTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzFormPhotoTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppInsT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormPhotoT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.encrypt.Sha3DesMD5;
import com.tranzvision.gd.util.httpclient.CommonUtils;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 申请人在线报名，原张彬彬从PS迁移过来，曹阳重写以更加适应JAVA版本
 * 
 * @author 曹阳
 * @since 2017-2-24
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
	private tzOnlineAppHisServiceImpl tzOnlineAppHisServiceImpl;
	@Autowired
	private TemplateEngine templateEngine;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private PsTzAppInsTMapper psTzAppInsTMapper;

	@Autowired
	private PsTzFormPhotoTMapper psTzFormPhotoTMapper;

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
	private PsTzOprPhotoTMapper psTzOprPhotoTMapper;

	@Autowired
	private PsTzApptplDyTMapper psTzApptplDyTMapper;

	@Autowired
	private tzOnlineAppEngineImpl tzOnlineAppEngineImpl;

	@Autowired
	private tzOnlineAppRulesImpl tzOnlineAppRulesImpl;

	@Autowired
	private PsTzClassInfTMapper psTzClassInfTMapper;

	@Autowired
	private TzOnlineAppSyncImpl tzOnlineAppSync;

	// 张浪添加，2017-09-25
	// 用于控制每台服务器访问量的信号变量，避免考生同时保存、提交操作过量对服务器造成过大压力,每台服务器允许5(默认5，hardcode[TZ_APPONL_XHL_COUNT]定义)个人进行排队执行保存、提交操作，其他人阻塞等待
	private static Semaphore onlineAppViewLockCounter = new Semaphore(5, true);
	private static Semaphore onlineAppSaveLockCounter = new Semaphore(5, true);
	private static int semaphoreCount = 5;

	public static void setOnlineAppLockCounter(int count) {
		tzOnlineAppServiceImpl.semaphoreCount = count;
		tzOnlineAppServiceImpl.onlineAppViewLockCounter = new Semaphore(count, true);
		tzOnlineAppServiceImpl.onlineAppSaveLockCounter = new Semaphore(count, true);
	}

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
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
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
		// 版式（横版、竖版）
		String strDisplayType = "";
		// 是否多层菜单
		boolean isMultilayerMenu = false;
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
		// 显示错误信息页面
		String strErrorPage = "N";
		// 错误页面显示返回首页
		String backIndexPage = "N";

		// 错误提示信息
		String strMessageError = "";
		// 是否可编辑(管理员审核查看报名表时传递的参数)
		String strIsEdit = "N";

		// 页面跳转ID
		String strPageID = "";

		// 班级项目ID
		String classProjectID = "";

		// 是否评审
		String strIsReview = "N";

		// 微信浏览器下访问根据openid自动登录
		Boolean isWeChart = CommonUtils.isWeChartBrowser(request);

		if ("appId".equals(strReferenceId)) {
			strClassId = request.getParameter("TZ_CLASS_ID");
			strSiteId = request.getParameter("SITE_ID");
			strAppInsId = request.getParameter("TZ_APP_INS_ID");
			strRefLetterId = request.getParameter("TZ_REF_LETTER_ID");
			strManagerView = request.getParameter("TZ_MANAGER");
			strCopyFrom = request.getParameter("APPCOPY");
			strAttachedTplId = request.getParameter("TZ_APP_TPL_ID");
			strIsEdit = request.getParameter("isEdit");
			strIsReview = request.getParameter("isReview");
			strPageID = request.getParameter("TZ_PAGE_ID");
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
			strIsReview = String.valueOf(jacksonUtil.getString("isReview"));
			strPageID = String.valueOf(jacksonUtil.getString("TZ_PAGE_ID"));

			if (strClassId == null) {
				strClassId = "";
			}
		}
		if (strSiteId == null || strSiteId.equals("null")) {
			strSiteId = "";
		}

		if (strIsEdit == null || strIsEdit.equals("null")) {
			strIsEdit = "";
		}

		if (strIsReview == null || strIsReview.equals("null")) {
			strIsReview = "";
		}

		if (StringUtils.equals("Y", strIsReview)) {
			// 评审时隐藏翻页提交按钮
			strIsReview = "none";
		} else {
			strIsReview = "";
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

		// 客户端是否移动设备访问
		boolean isMobile = CommonUtils.isMobile(request);

		// 推荐信，只有PC模式
		if (strRefLetterId != null && !strRefLetterId.trim().equals("")) {
			isMobile = false;
		}
		System.out.println("是否手机模式:" + isMobile);

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

		long time = System.currentTimeMillis();

		System.out.println("报名表展现数据预处理Begin:");

		/*********************************************************************************************
		 * 根据当前session获取信号量，防止此时频发刷新 张浪添加，20170926【修改开始】
		 *********************************************************************************************/
		// 频繁刷新提示
		String strRefreshFast = gdKjComServiceImpl.getMessageText(request, response, "TZGD_APPONLINE_MSGSET",
				"REFRESH_FAST", "您的请求正在处理中，请勿频繁刷新或反复操作，请稍后刷新重试。",
				"Your request is being processed, do not frequently refresh or repeated operation, please refresh the retry later.");
		String strSystemBusy = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
				"VIEW_SYSTEM_BUSY", strLanguage, "系统繁忙，请点击返回首页或刷新重试。",
				"The system is busy, please click to return to the home page or refresh the retry.");

		/* 只有为学生查看报名表时才进行访问信号量控制 */
		boolean isStudent = false;
		String isStu = sqlQuery.queryForObject("select 'Y' from PS_TZ_REG_USER_T where OPRID=?", new Object[] { oprid },
				"String");
		if (isStu != null && "Y".equals(isStu)) {
			isStudent = true;
		}

		Semaphore sessionSemaphore = null;
		boolean hasGetSessionSemaphore = false;
		boolean hasGetViewOnlineAppSemaphore = false;

		try {
			if (isStudent) {
				// 限制每台服务器上只能5人同时访问报名表
				if (onlineAppViewLockCounter.getQueueLength() >= tzOnlineAppServiceImpl.semaphoreCount
						|| onlineAppViewLockCounter.tryAcquire(5000, TimeUnit.MILLISECONDS) == false) {
					// 同时进入报名表的人数过多，直接返回首页
					strErrorPage = "Y";
					backIndexPage = "Y";
					throw new TzException(strSystemBusy);
				}
				hasGetViewOnlineAppSemaphore = true;

				// 当前会话sessionid
				String sessionId = request.getSession().getId();
				Map.Entry<String, Semaphore> sessionSemaphoreObject = tzGdObject.getSemaphore(sessionId);
				if (sessionSemaphoreObject == null || sessionSemaphoreObject.getKey() == null
						|| sessionSemaphoreObject.getValue() == null) {
					// 未获取到当前信号量map
				} else {
					sessionSemaphore = sessionSemaphoreObject.getValue();
					// 先判断当前报名表对应信号量大于等于1，说明当前报名表有请求尚在执行，否则获取信号灯
					if (sessionSemaphore.getQueueLength() >= 1 || sessionSemaphore.tryAcquire() == false) {
						strMessageError = strRefreshFast;
						strErrorPage = "Y";
						throw new TzException(strRefreshFast);
					}

					// 已获取信号灯，执行请求完成后需要释放
					hasGetSessionSemaphore = true;
				}
			}
			/*********************************************************************************************
			 * 根据当前session获取信号量，防止此时频发刷新 张浪添加，20170926【修改结束】
			 *********************************************************************************************/

			// 如果报名表传过来了
			if (numAppInsId > 0) {
				psTzAppInsT = psTzAppInsTMapper.selectByPrimaryKey(numAppInsId);
				if (psTzAppInsT != null) {
					strTplId = psTzAppInsT.getTzAppTplId();
					strAppInsState = psTzAppInsT.getTzAppFormSta();

					System.out.println("strAppInsState:" + strAppInsState);
					// 如果报名表已提交或者撤销，则只读显示
					if ("U".equals(strAppInsState) || "OUT".equals(strAppInsState)) {
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
							// PsTzApptplDyTWithBLOBs
							// attachedPsTzApptplDyTWithBLOBs
							// =
							// psTzApptplDyTMapper.selectByPrimaryKey(strAttachedTplId);
							String TZ_APP_M_TPL_ID = sqlQuery.queryForObject(sql, new Object[] { strAttachedTplId },
									"String");
							if (strTplId.equals(TZ_APP_M_TPL_ID)) {
								strTplId = strAttachedTplId;
								// 根据报名表实例和附属模版编号去获得报名表Json数据
								strInsData = tzOnlineAppHisServiceImpl.getHisAppInfoJson(numAppInsId, strTplId);
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

									if ("TZ_GUEST".equals(strAppOprId) || "".equals(strAppOprId)) {
										// 如果是匿名报名
										sql = "SELECT TZ_GUEST_APPLY FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID = ?";
										strIsGuest = sqlQuery.queryForObject(sql, new Object[] { strClassId },
												"String");
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
											strIsAdmin = tzOnlineAppEngineImpl.checkAppViewQx(strTplId, oprid,
													strAppOrgId, strClassId);
											if ("Y".equals(strIsAdmin)) {
												// 管理员只读查看
												strAppFormReadOnly = "Y";
											} else {
												// 非法访问
												strMessageError = gdKjComServiceImpl.getMessageTextWithLanguageCd(
														request, "TZGD_APPONLINE_MSGSET", "ILLEGAL_OPERATION",
														strLanguage, "非法操作", "Illegal operation.");
											}
										}
									}

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
											"TZGD_APPONLINE_MSGSET", "PARAERROR", strLanguage, "参数错误",
											"Parameter error.");
								}
								/*---如果报名表模版类型为推荐信End   ----*/
							} else {
								strMessageError = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
										"TZGD_APPONLINE_MSGSET", "TEMPLATEERROR", strLanguage, "没有找到对应的模版",
										"Could not find the corresponding template");
							}
						} else {
							// 没有找到对应的模版
							strMessageError = gdKjComServiceImpl.getMessageText(request, response,
									"TZGD_APPONLINE_MSGSET", "TEMPLATEERROR", "没有找到对应的模版",
									"Could not find the corresponding template");
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

			} else {
				// 模版ID没有传过来
				if (!"".equals(strClassId) && strClassId != null) {
					psTzClassInfT = psTzClassInfTMapper.selectByPrimaryKey(strClassId); // TZ_IS_APP_OPEN

					if (psTzClassInfT != null && psTzClassInfT.getTzIsAppOpen().equals("Y")) {
						classProjectID = psTzClassInfT.getTzPrjId();
						if ("TZ_GUEST".equals(oprid) || "".equals(oprid)) {
							/*--------匿名报名判断Begin---------*/
							strTplId = psTzClassInfT.getTzAppModalId();
							if ("".equals(strTplId) || strTplId == null) {
								strMessageError = gdKjComServiceImpl.getMessageText(request, response,
										"TZGD_APPONLINE_MSGSET", "PARAERROR", "参数错误", "Parameter error");
							} else {
								// 如果是Guest用户，看班级是否允许匿名报名
								if ("Y".equals(psTzClassInfT.getTzGuestApply())) {
									strIsGuest = "Y";
								} else {
									strMessageError = gdKjComServiceImpl.getMessageText(request, response,
											"TZGD_APPONLINE_MSGSET", "SESSION_INVAILD", "当前会话已失效，请重新登陆。",
											"The current session is timeout or the current access is invalid,Please relogin.");
								}
							}
							/*--------匿名报名判断End---------*/
						} else {
							// 是注册用户在线报名
							sql = "SELECT TZ_APP_INS_ID,TZ_BATCH_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID = ? AND TZ_CLASS_ID = ?";
							mapData = null;
							mapData = sqlQuery.queryForMap(sql, new Object[] { oprid, strClassId });
							if (mapData != null) {
								strAppInsId = String.valueOf(mapData.get("TZ_APP_INS_ID"));
								strBatchId = String.valueOf(mapData.get("TZ_BATCH_ID"));
							}

							strAppOprId = oprid;
							if (!"".equals(strAppInsId) && strAppInsId != null) {
								numAppInsId = Long.parseLong(strAppInsId);
							} else {
								numAppInsId = 0L;
							}

							if (numAppInsId > 0) {
								// 传入了报名表实例
								psTzAppInsT = psTzAppInsTMapper.selectByPrimaryKey(numAppInsId);
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
									
									String sql2 = "SELECT TZ_ALLOW_APPLY FROM PS_TZ_REG_USER_T WHERE OPRID = ?";
									String TZ_ALLOW_APPLY = sqlQuery.queryForObject(sql2, new Object[] {strAppOprId}, "String");
									// 如果报名表已提交或者已撤销并且不允许考生继续申请，则只读显示
									if (("U".equals(strAppInsState) || "OUT".equals(strAppInsState)) && "N".equals(TZ_ALLOW_APPLY)) {
										strAppFormReadOnly = "Y";
									}
								} else {
									strMessageError = gdKjComServiceImpl.getMessageText(request, response,
											"TZGD_APPONLINE_MSGSET", "PARAERROR", "参数错误", "Parameter error");
								}

							} else {
								strTplId = psTzClassInfT.getTzAppModalId();
								if ("".equals(strTplId) || strTplId == null) {
									strMessageError = gdKjComServiceImpl.getMessageText(request, response,
											"TZGD_APPONLINE_MSGSET", "PARAERROR", "参数错误", "Parameter error");
								}
							}
						}
						psTzApptplDyTWithBLOBs = psTzApptplDyTMapper.selectByPrimaryKey(strTplId);
						if (psTzApptplDyTWithBLOBs == null) {
							strMessageError = gdKjComServiceImpl.getMessageText(request, response,
									"TZGD_APPONLINE_MSGSET", "INSEERROR", "没有找到对应的实例",
									"Could not find the corresponding instance");
						}

					} else {
						strMessageError = gdKjComServiceImpl.getMessageText(request, response, "TZGD_APPONLINE_MSGSET",
								"NOT_OPEN", "当前班级未开通在线报名", "Not open the online registration.");
					}
				} else {
					// 没有找到对应的班级
					strMessageError = gdKjComServiceImpl.getMessageText(request, response, "TZGD_APPONLINE_MSGSET",
							"PARAERROR", "参数错误", "Parameter error");
				}
			}

			/*********************************************************************************************
			 * 当前人员报名表信号量如果存在，说明有保存、提交请求正在处理中，防止此时频发刷新 张浪添加，20170925【修改开始】
			 *********************************************************************************************/
			if (isStudent) {
				if ("".equals(strMessageError)) {
					// 判断是否有保存、提交报名表请求正在执行
					Map.Entry<String, Semaphore> tmpSemaphoreObject = tzGdObject.getSemaphore(
							"com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl.tzOnlineAppServiceImpl-20170925",
							strClassId + "-" + oprid + "-" + numAppInsId);

					if (tmpSemaphoreObject == null || tmpSemaphoreObject.getKey() == null
							|| tmpSemaphoreObject.getValue() == null) {
						// 没有正在保存或提交的数据
					} else {
						Semaphore tmpSemaphore = tmpSemaphoreObject.getValue();
						// 信号量中当前可用的许可数<1，表示有正在处理中的保存、提交请求
						if (tmpSemaphore.availablePermits() < 1) {
							strMessageError = strRefreshFast;
						}
					}
				}

				if ("".equals(strMessageError)) {
					// 判断是否有正在刷新的报名表
					Map.Entry<String, Semaphore> tmpSemaphoreObject = tzGdObject
							.getSemaphore(strClassId + "-" + oprid + "-" + numAppInsId);

					if (tmpSemaphoreObject == null || tmpSemaphoreObject.getKey() == null
							|| tmpSemaphoreObject.getValue() == null) {
						// 没有正刷新的数据
					} else {
						Semaphore tmpSemaphore = tmpSemaphoreObject.getValue();
						// 信号量中当前可用的许可数<1，表示有正在刷新的报名表
						if (tmpSemaphore.availablePermits() < 1) {
							strMessageError = strRefreshFast;
						}
					}
				}
			}
			/*********************************************************************************************
			 * 当前人员报名表信号量如果存在，说明有请求正在处理中，防止此时频发刷新 张浪添加，20170925【修改結束】
			 *********************************************************************************************/

			System.out.println("报名表展现数据预处理End,Time=" + (System.currentTimeMillis() - time));

			if ("".equals(strMessageError)) {
				time = System.currentTimeMillis();
				System.out.println("报名表展现数据处理Begin");

				/*********************************************************************************************
				 * 当前人员报名表信号量如果存在，说明有请求正在处理中，防止此时频发刷新 张浪添加，20170925【修改开始】
				 *********************************************************************************************/
				Semaphore refreshSemaphore = null;
				boolean hasGetSemaphore = false;
				try {
					if (isStudent) {
						// 每次打开报名表时获取报名表信号灯，如果信号灯未释放，提示刷新过快
						Map.Entry<String, Semaphore> refreshSemaphoreObject = tzGdObject
								.getSemaphore(strClassId + "-" + oprid + "-" + numAppInsId);
						if (refreshSemaphoreObject == null || refreshSemaphoreObject.getKey() == null
								|| refreshSemaphoreObject.getValue() == null) {
							// 如果返回的信号灯为空，继续执行
						} else {
							refreshSemaphore = refreshSemaphoreObject.getValue();

							// 先判断当前报名表对应信号量大于等于1，说明当前报名表有请求尚在执行，否则获取信号灯
							if (refreshSemaphore.getQueueLength() >= 1 || refreshSemaphore.tryAcquire() == false) {
								strMessageError = strRefreshFast;
								strErrorPage = "Y";
								throw new TzException(strMessageError);
							}

							// 已获取信号灯，执行请求完成后需要释放
							hasGetSemaphore = true;
						}
					}
					/*********************************************************************************************
					 * 当前人员报名表信号量如果存在，说明有请求正在处理中，防止此时频发刷新 张浪添加，20170925【修改结束】
					 *********************************************************************************************/

					strAppOrgId = psTzApptplDyTWithBLOBs.getTzJgId();
					strTplType = psTzApptplDyTWithBLOBs.getTzUseType();
					strTplData = psTzApptplDyTWithBLOBs.getTzApptplJsonStr();
					strDisplayType = psTzApptplDyTWithBLOBs.getTzDisplayType();
					strLanguage = psTzApptplDyTWithBLOBs.getTzAppTplLan();
					strAfterSubmitUrl = psTzApptplDyTWithBLOBs.getTzAppTzurl();
					String showSubmitBtnOnly = psTzApptplDyTWithBLOBs.getTzOnlySubmitBtn();

					if (StringUtils.isBlank(strDisplayType) || StringUtils.equals("V", strDisplayType)) {
						strDisplayType = "";
						isMultilayerMenu = true;
					}

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
					strMenuId = sqlQuery.queryForObject(sql, new Object[] { "TZ_ACCOUNT_MANAGEMENT_" + strAppOrgId },
							"String");
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

					// PC版本处理
					String strTabs = "";
					String strTabsAll = "";
					String strLeftStyle = "";
					String strRightStyle = "";
					if (isMobile) {
						// 手机版本的菜单,手机只显示二级菜单
						int numIndex = 0;
						String strXxxBh = "";
						// String strXxxMc = "";
						String strXxxTitle = "";
						String strDivClass = "";
						// 页签自定义样式
						String strtabType = "";

						// 父分隔符号的id
						String strTZ_FPAGE_BH = "";

						int numChild = 0;

						sql = "SELECT A.TZ_XXX_BH,A.TZ_XXX_MC,A.TZ_TITLE,A.TZ_TAPSTYLE,A.TZ_FPAGE_BH,B.TZ_HAS_COMPLETE ";
						sql = sql
								+ "FROM PS_TZ_APP_XXXPZ_T A LEFT JOIN PS_TZ_APP_COMP_TBL B ON B.TZ_APP_INS_ID=? AND A.TZ_XXX_BH=B.TZ_XXX_BH ";
						sql = sql + "WHERE TZ_COM_LMC = 'Page' AND TZ_APP_TPL_ID = ? ORDER BY TZ_ORDER ASC";
						listData = sqlQuery.queryForList(sql, new Object[] { numAppInsId, strTplId });
						mapData = null;

						int index = -1;
						int size = listData.size();

						System.out.println("是否多层菜单:" + isMultilayerMenu);
						String strComplete = "";
						// String lastMenu = "";
						// int index = 0;
						List<String> name = new ArrayList<String>();
						for (Object objDataTap : listData) {
							mapData = (Map<String, Object>) objDataTap;
							strXxxBh = mapData.get("TZ_XXX_BH") == null ? "" : String.valueOf(mapData.get("TZ_XXX_BH"));
							strXxxTitle = mapData.get("TZ_TITLE") == null ? ""
									: String.valueOf(mapData.get("TZ_TITLE"));
							strtabType = mapData.get("TZ_TAPSTYLE") == null ? ""
									: String.valueOf(mapData.get("TZ_TAPSTYLE"));
							strTZ_FPAGE_BH = mapData.get("TZ_FPAGE_BH") == null ? ""
									: String.valueOf(mapData.get("TZ_FPAGE_BH"));

							// 对号

							numIndex = numIndex + 1;

							if (isMultilayerMenu) {
								// 多层菜单 不显示顶级的菜单
								if (strTZ_FPAGE_BH == null || strTZ_FPAGE_BH.trim().equals("")) {
								} else {
									index = index + 1;
									name.add(strXxxTitle);
									if (strPageID == null || strPageID.equals("")) {
										numChild = numChild + 1;
										// 默认第一页高亮
										if (numChild == 1) {
											strDivClass = "active";
										} else {
											strDivClass = "";
										}
									} else {
										if (strXxxBh.equals(strPageID)) {
											strDivClass = "active";
										} else {
											strDivClass = "";
										}
									}
								}
								strtabType = ""; // 多层菜单页签自定义样式无效
							} else {
								index = index + 1;
								name.add(strXxxTitle);
								if (strPageID == null || strPageID.equals("")) {
									numChild = numChild + 1;
									// 默认第一页高亮
									if (numChild == 1) {
										strDivClass = "active";
									} else {
										strDivClass = "";
									}
								} else {
									if (strXxxBh.equals(strPageID)) {
										strDivClass = "active";
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
										strComplete = "Y";
									} else {
										// 未完成时,不显示对号
										strComplete = "";
									}
								} else {
									// 实例不存在时,不显示对号
									strComplete = "";
								}
							}

							if ((isMultilayerMenu && strTZ_FPAGE_BH != null && !strTZ_FPAGE_BH.trim().equals(""))
									|| !isMultilayerMenu) {
								try {
									if (StringUtils.isNotBlank(strComplete)) {
										strComplete = "<i class=\"complete\"></i>";
									}
									// System.out.println("strComplete:" +
									// strComplete);

									strTabs = strTabs + tzGdObject.getHTMLText(
											"HTML.TZApplicationTemplateBundle.TZ_TABS_DIV_PHONE2", strDivClass,
											String.valueOf(index), strXxxBh, strXxxTitle, strComplete);
								} catch (TzSystemException e) {
									e.printStackTrace();
									strTabs = "";
								}
							}
						}

						try {
							strTabs = tzGdObject.getHTMLText("HTML.TZApplicationTemplateBundle.TZ_TABS_DIV_PHONE",
									strTabs);
						} catch (TzSystemException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						index = 0;
						size = name.size();
						StringBuffer sb = new StringBuffer();
						for (Object objDataTap : name) {
							strXxxTitle = (String) objDataTap;
							sb.append(" <li index=" + index + ">");
							sb.append(strXxxTitle);
							sb.append("</li>");
							index = index + 1;
						}
						try {
							strTabsAll = tzGdObject.getHTMLText("HTML.TZApplicationTemplateBundle.TZ_TABS_DIV_PHONE3",
									sb.toString());
						} catch (TzSystemException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						/*-----报名表菜单生成End--------------*/

						/* 如果页码大于1 则显示左侧，否则不显示左侧 */

						if (numIndex <= 1) {
							strLeftStyle = "display:none";
							strRightStyle = "margin: 0 auto;float:none";
						}

					} else {
						int numIndex = 0;
						String strXxxBh = "";
						// String strXxxMc = "";
						String strXxxTitle = "";
						String strDivClass = "";
						// 页签自定义样式
						String strtabType = "";

						// 父分隔符号的id
						String strTZ_FPAGE_BH = "";

						int numChild = 0;

						sql = "SELECT A.TZ_XXX_BH,A.TZ_XXX_MC,A.TZ_TITLE,A.TZ_TAPSTYLE,A.TZ_FPAGE_BH,B.TZ_HAS_COMPLETE ";
						sql = sql
								+ "FROM PS_TZ_APP_XXXPZ_T A LEFT JOIN PS_TZ_APP_COMP_TBL B ON B.TZ_APP_INS_ID=? AND A.TZ_XXX_BH=B.TZ_XXX_BH ";
						sql = sql + "WHERE TZ_COM_LMC = 'Page' AND TZ_APP_TPL_ID = ? ORDER BY TZ_ORDER ASC";
						listData = sqlQuery.queryForList(sql, new Object[] { numAppInsId, strTplId });
						mapData = null;

						String strComplete = ""; // 对号
						System.out.println("是否多层菜单:" + isMultilayerMenu);
						for (Object objDataTap : listData) {
							mapData = (Map<String, Object>) objDataTap;
							strXxxBh = mapData.get("TZ_XXX_BH") == null ? "" : String.valueOf(mapData.get("TZ_XXX_BH"));
							strXxxTitle = mapData.get("TZ_TITLE") == null ? ""
									: String.valueOf(mapData.get("TZ_TITLE"));
							strtabType = mapData.get("TZ_TAPSTYLE") == null ? ""
									: String.valueOf(mapData.get("TZ_TAPSTYLE"));
							strTZ_FPAGE_BH = mapData.get("TZ_FPAGE_BH") == null ? ""
									: String.valueOf(mapData.get("TZ_FPAGE_BH"));

							strComplete = contextUrl + "/statics/images/appeditor/new/check.png"; // 对号

							numIndex = numIndex + 1;

							if (isMultilayerMenu) {
								strtabType = ""; // 多层菜单页签自定义样式无效
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
								if (StringUtils.equals("V", strDisplayType) || strDisplayType.equals("")) {
									strComplete = tzGdObject.getHTMLText("HTML.TZApplicationTemplateBundle.TZ_TABS_IMG",
											strComplete);
								}
								// System.out.println("strComplete:" +
								// strComplete);
								strTabs = strTabs
										+ tzGdObject.getHTMLText("HTML.TZApplicationTemplateBundle.TZ_TABS_DIV",
												strDivClass, strXxxTitle, strComplete, strXxxBh, strtabType);
							} catch (TzSystemException e) {
								e.printStackTrace();
								strTabs = "";
							}
						}
						/*-----报名表菜单生成End--------------*/

						/* 如果页码大于1 则显示左侧，否则不显示左侧 */

						if (numIndex <= 1) {
							strLeftStyle = "display:none";
							strRightStyle = "margin: 0 auto;float:none";
						}
					}

					System.out.println("报名表展现左侧菜单处理End,Time=" + (System.currentTimeMillis() - time2));

					System.out.println("报名表展现获取控件信息处理Begin");
					time2 = System.currentTimeMillis();
					// 控件信息
					String strComRegInfo = "";
					ArrayList<Map<String, Object>> comDfn = templateEngine.getComDfn(strTplId);
					strComRegInfo = jacksonUtil.List2json(comDfn);
					// strComRegInfo = strComRegInfo.replace("\\", "\\\\");
					System.out.println("报名表展现获取控件信息处理End,Time=" + (System.currentTimeMillis() - time2));

					System.out.println("报名表展现历史报名表处理Begin");
					time2 = System.currentTimeMillis();
					/*-----------最新历史报名表Begin------------- */
					Map<String, String> m = tzOnlineAppEngineImpl.getHistoryOnlineApp(strAppInsId, strCopyFrom,
							strAppOprId, strAppOrgId, strTplId, strAppOprId, strClassId, strRefLetterId, strInsData,
							isWeChart);
					;
					strAppInsId = m.get("strAppInsId");
					strInsData = m.get("strInsData");
					strRefLetterId = m.get("strRefLetterId");

					if (strRefLetterId == null || strRefLetterId.equals("null")) {
						strRefLetterId = "";
					}
					System.out.println("strAppInsId:" + strAppInsId);
					System.out.println("strRefLetterId:" + strRefLetterId);
					System.out.println("报名表展现历史报名表处理End,Time=" + (System.currentTimeMillis() - time2));
					// System.out.println(strAppInsId);
					// System.out.println(strInsData);
					// System.out.println(strRefLetterId);
					/*-----------最新历史报名表End------------- */

					if (strTplData == null || "".equals(strTplData)) {
						strTplData = "''";
					}

					if (strInsData == null || "".equals(strInsData)) {
						strInsData = "''";
					}

					// 获取个人基本信息
					System.out.println("报名表展现获取个人基本信息Begin");
					time2 = System.currentTimeMillis();
					String strUserInfoSet = "";
					strUserInfoSet = tzOnlineAppEngineImpl.getUserInfo(strAppInsId, strTplType, strSiteId);

					System.out.println("报名表展现获取个人基本信息End,Time=" + (System.currentTimeMillis() - time2));

					// 双语化消息集合Json字符串
					// msgSet 用于双语
					System.out.println("报名表展现双语化处理Begin");
					time2 = System.currentTimeMillis();
					String strMsgSet = "{}";
					strMsgSet = gdObjectServiceImpl.getMessageSetByLanguageCd(request, response,
							"TZGD_APPONLINE_MSGSET", strLanguage);
					jacksonUtil.json2Map(strMsgSet);
					if (jacksonUtil.containsKey(strLanguage)) {
						Map<String, Object> msgLang = jacksonUtil.getMap(strLanguage);
						strMsgSet = jacksonUtil.Map2json(msgLang);
					}

					String strSave = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
							"SAVE", strLanguage, "保存", "Save");

					// 莫名其妙错误，做特殊处理
					if (strLanguage.equals("ENG")) {
						strSave = "Save";
					}

					String strSubmit = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
							"SUBMIT", strLanguage, "提交", "Submit");
					String strNext = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
							"NEXT", strLanguage, "下一步", "Next");
					String strPrev = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
							"PREV", strLanguage, "上一步", "Previous");
					String strLoading = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
							"TZGD_APPONLINE_MSGSET", "LOADING", strLanguage, "上传中", "Loading");
					String strProcessing = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
							"TZGD_APPONLINE_MSGSET", "PROCESS", strLanguage, "正在处理", "Processing");
					String strSubmitConfirmMsg = "";
//					if (!"MEM".equals(strAppOrgId)) {
//						strSubmitConfirmMsg = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
//								"TZGD_APPONLINE_MSGSET", "SUBMITCONFIRMMSG", strLanguage,
//								"请您确认所有的信息都已经填写完整，一经提交本人将无法修改。若有任何疑问，可联络MBA（EMBA）项目部021-64252634。",
//								"I have read the statement to confirm the submission of the registration form.");
//					} else {
//						strSubmitConfirmMsg = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
//								"TZGD_APPONLINE_MSGSET", "SUBMITCONFIRMMSG", strLanguage, "我已阅读声明，确认提交报名表",
//								"I have read the statement to confirm the submission of the registration form.");
//					}
					
					if("SEM".equals(strAppOrgId)){
						strSubmitConfirmMsg = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
								"TZGD_APPONLINE_MSGSET", "SUBMITCONFIRMMSGMBA", strLanguage,
								"请您确认所有的信息都已经填写完整，一经提交本人将无法修改。若有任何疑问，可联络MBA（EMBA）项目部021-64252634。",
								"I have read the statement to confirm the submission of the registration form.");
					}else if("MPACC".equals(strAppOrgId)){
						strSubmitConfirmMsg = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
								"TZGD_APPONLINE_MSGSET", "SUBMITCONFIRMMSGMPACC", strLanguage,
								"请您确认所有的信息都已经填写完整，一经提交本人将无法修改。若有任何疑问，可联络MPAcc项目部021-64252787。",
								"I have read the statement to confirm the submission of the registration form.");
					}else{
						strSubmitConfirmMsg = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
								"TZGD_APPONLINE_MSGSET", "SUBMITCONFIRMMSG", strLanguage, "我已阅读声明，确认提交报名表",
								"I have read the statement to confirm the submission of the registration form.");
					}
					String strDownLoadPDFMsg = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
							"TZGD_APPONLINE_MSGSET", "DOWN", strLanguage, "下载报名表", "Download");

					String strDownErrorMsg = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
							"TZGD_APPONLINE_MSGSET", "DOWNERR", strLanguage, "请先保存报名表",
							"Please save the application form。");

					String BMBTJMsg = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
							"BMBTJMSG", strLanguage, "报名表已提交", "The application has been submitted");

					String TJXTJMsg = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
							"TJXTJMsg", strLanguage, "推荐信已提交", "Reference Letter has been submitted");

					System.out.println("报名表展现双语化处理End,Time=" + (System.currentTimeMillis() - time2));

					if ("N".equals(strIsGuest)) {
						sql = "SELECT TZ_IS_GUEST FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID = ? AND OPRID = ?";
						try {
							strIsGuest = sqlQuery.queryForObject(sql, new Object[] { strClassId, strAppOprId },
									"String");
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

					System.out.println("strAppInsId:" + strAppInsId);

					// 非匿名报名时，如果当前登录人为管理员、并且可编辑，同时报名表只读参数为Y时，将只读参数改为N
					if (!StringUtils.equals("Y", strIsGuest) && StringUtils.equals("Y", strIsAdmin)
							&& StringUtils.equals("Y", strIsEdit) && StringUtils.equals("Y", strAppFormReadOnly)) {
						strAppFormReadOnly = "N";
					}

					try {

						System.out.println("报名表展现密码处理Begin");
						time2 = System.currentTimeMillis();
						String passWordHtml = "";

						String setPwdId = "setPwd";
						String setPwd2Id = "setPwd2";
						String pwdTitleDivId = "PwdTitleDiv";
						String pwdDivId = "setPwdDiv";
						String pwdDivId2 = "setPwdDiv2";
						// 推荐信 密码设置控制 add by caoy 2017-1-22 strIsAdmin
						if ("TJX".equals(strTplType) && !StringUtils.equals("Y", strIsAdmin)) {

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
											"TZGD_APPONLINE_MSGSET", "TJXSETPWD2", strLanguage, "重新输入密码",
											"Re Password");

									passWordHtml = tzGdObject.getHTMLText(
											"HTML.TZWebsiteApplicationBundle.TZ_ONLINE_PAGE_PWD_HTML", false, pwdTitle,
											setPwd, setPwd2, setPwdId, setPwd2Id, pwdTitleDivId, pwdDivId, pwdDivId2);
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

						// strTplData = strTplData.replace("\\", "\\\\");
						// strTplData = strTplData.replace("$", "\\$");

						Pattern CRLF = Pattern.compile("(\r\n|\r|\n|\n\r)");
						Matcher mc = CRLF.matcher(strInsData);
						if (mc.find()) {
							strInsData = mc.replaceAll("\\\\n");
						}
						// strInsData = strInsData.replace("\\", "\\\\");
						// strInsData = strInsData.replace("$", "\\$");

						// 处理HTML换行符号，是替换的\u2028;
						strInsData = strInsData.replace(" ", "");
						String pwdError = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
								"TZGD_APPONLINE_MSGSET", "TJXSETPWDError", strLanguage, "请填写密码",
								"Please fill in the password");

						String pwdError2 = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
								"TZGD_APPONLINE_MSGSET", "TJXSETPWDError", strLanguage, "密码和确认密码不一致",
								"Password and confirm password inconsistent");

						String Pwdname = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
								"TZGD_APPONLINE_MSGSET", "TJXSETPWD", strLanguage, "访问密码", "Access password");
						String strSubmit2 = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
								"TZGD_APPONLINE_MSGSET", "CONFIRM", strLanguage, "确认", "Confirm");

						String forgetPass = gdKjComServiceImpl.getMessageTextWithLanguageCd(request,
								"TZGD_APPONLINE_MSGSET", "FOGETPASS", strLanguage, "忘记密码", "forget your password");

						// 构建密码输入框
						String PWDHTML = tzGdObject.getHTMLText("HTML.TZWebsiteApplicationBundle.TZ_ONLINE_PWD_HTML",
								false, Pwdname, strSubmit2, contextUrl, forgetPass);

						System.out.println("报名表展现密码处理End,Time=" + (System.currentTimeMillis() - time2));

						System.out.println("报名表展现构造HTML页面Begin");
						time2 = System.currentTimeMillis();

						if (isMobile) {

							str_appform_main_html = tzGdObject.getHTMLTextForDollar(
									"HTML.TZWebsiteApplicationBundle.TZ_ONLINE_PAGE_HTML_PHONE", true, contextUrl,
									strTzGeneralURL, strClassId, strLanguage, strIsAdmin, strTplId, strAppInsId,
									strAppFormReadOnly, showSubmitBtnOnly, strTplType, strAppInsVersion, strIsEdit,
									strProcessing, strAppInsState, strSubmitConfirmMsg, strAfterSubmitUrl, strSiteId,
									strAppOrgId, strLoading, strProcessing, strPrev, strSave, strSubmit, strNext,
									BMBTJMsg, strMenuId, strBatchId, classProjectID, strTabs, strTabsAll,
									strAppInsVersion, strUserInfoSet, strMsgSet, strComRegInfo, strTplData, strInsData);
						} else {

							if (strDisplayType != null && !strDisplayType.equals("H")) {
								// sem机构用默认的，其他用机构的css
								if (!strAppOrgId.toLowerCase().equals("sem")) {
									strDisplayType = strAppOrgId.toLowerCase();
								}
							}

							str_appform_main_html = tzGdObject.getHTMLTextForDollar(
									"HTML.TZWebsiteApplicationBundle.TZ_ONLINE_PAGE_HTML", false, strTzGeneralURL,
									strComRegInfo, strTplId, strAppInsId, strClassId, strRefLetterId, strTplData,
									strInsData, strTabs, strSiteId, strAppOrgId, strMenuId, strAppFormReadOnly,
									strMsgSet, strLanguage, strSave, strNext, strSubmit, strTplType, strLoading,
									strProcessing, strAfterSubmitUrl, strOnlineHead, strOnlineFoot, strOnlineLeft,
									strIsAdmin, strMainInnerStyle, strUserInfoSet, strMainStyle, strPrev,
									strAppInsVersion, contextUrl, leftWidthStyle, rightWidthStyle, strLeftStyle,
									strRightStyle, showSubmitBtnOnly, strSubmitConfirmMsg, strIsEdit, strBatchId,
									strTJXIsPwd, passWordHtml, setPwdId, setPwd2Id, pwdTitleDivId, pwdDivId, pwdDivId2,
									pwdError, pwdError2, PWDHTML, strDownLoadPDFMsg, strDownErrorMsg, classProjectID,
									strAppInsState, strDisplayType, strIsReview, BMBTJMsg, TJXTJMsg, strTplData,
									strInsData);
						}
						System.out.println("报名表展现构造HTML页面End,Time=" + (System.currentTimeMillis() - time2));
						time2 = System.currentTimeMillis();
						System.out.println("报名表展现替换HTML页面Begin");
						str_appform_main_html = siteRepCssServiceImpl.repTitle(str_appform_main_html, strSiteId);
						str_appform_main_html = siteRepCssServiceImpl.repCss(str_appform_main_html, strSiteId);
						System.out.println("报名表展现替换HTML页面End,Time=" + (System.currentTimeMillis() - time2));
					} catch (TzSystemException e) {
						e.printStackTrace();
					}

					System.out.println("报名表展现数据处理End,Time=" + (System.currentTimeMillis() - time));

					/*********************************************************************************************
					 * 当前人员报名表信号量如果存在，说明有请求正在处理中，防止此时频发刷新 张浪添加，20170925【修改开始】
					 *********************************************************************************************/
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					// 释放信号量
					if (hasGetSemaphore) {
						refreshSemaphore.release();
					}
				}

			} else {
				strErrorPage = "Y";
				// str_appform_main_html = strMessageError;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (hasGetSessionSemaphore) {
				sessionSemaphore.release();
			}

			if (hasGetViewOnlineAppSemaphore) {
				onlineAppViewLockCounter.release();
			}
		}

		// 显示错误信息页面
		if ("Y".equals(strErrorPage)) {
			try {
				String hiddenCls = "hidden";
				if ("Y".equals(backIndexPage)) {
					str_appform_main_html = tzGdObject.getHTMLTextForDollar(
							"HTML.TZWebsiteApplicationBundle.TZ_ONLINE_ERROR_PAGE_HTML", true, contextUrl,
							strMessageError, hiddenCls, "");
				} else {
					str_appform_main_html = tzGdObject.getHTMLTextForDollar(
							"HTML.TZWebsiteApplicationBundle.TZ_ONLINE_ERROR_PAGE_HTML", true, contextUrl,
							strMessageError, "", hiddenCls);
				}
			} catch (TzSystemException e) {
				e.printStackTrace();
			}
		}
		/*********************************************************************************************
		 * 当前人员报名表信号量如果存在，说明有请求正在处理中，防止此时频发刷新 张浪添加，20170925【修改结束】
		 *********************************************************************************************/

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

		// 客户端是否移动设备访问
		boolean isMobile = CommonUtils.isMobile(request);

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
		// String strAppFormReadOnly = "";
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
		// String strTplData = "";
		// 报名表实例数据
		// String strInsData = "";
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

		// 系统繁忙
		String strSystemBusy = "";
		// 重复请求
		String strRequestRept = "";

		// 批次
		String strBatchId = "";
		// 密码
		String strPwd = "";
		// 是否保存密码
		String isPwd = "";

		// 微信浏览器下访问根据openid自动登录
		Boolean isWeChart = CommonUtils.isWeChartBrowser(request);

		JacksonUtil jacksonUtil = new JacksonUtil();
		// 表单内容
		int dataLength = actData.length;
		for (int num = 0; num < dataLength; num++) {

			long time = System.currentTimeMillis();

			System.out.println("报名表保存数据预处理Begin");
			String strForm = actData[num];

			// System.out.println("strForm:" + strForm);

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
			if (jacksonUtil.containsKey("ISPWD")) {
				isPwd = String.valueOf(jacksonUtil.getString("ISPWD"));
			}

			// System.out.println("strPwd:" + strPwd);

			// System.out.println("isPwd:" + isPwd);

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

			strSystemBusy = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
					"SYSTEM_BUSY", strLanguage, "系统忙，请稍候再试。", "The system is busy, please try again later.");
			strRequestRept = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
					"REQUEST_REPEAT", strLanguage, "您上次请求尚未完成，请稍后再试。",
					"Your last request has not been completed, please try again later.");

			Map<String, Object> mapData = null;
			String sql = "";
			if (!"".equals(strClassId) && strClassId != null) {
				// 如果跟换 class那么检查 新的classId 是否已经存在并且已经提交 不允许保存，报错
				// 整体逻辑变更，如果跟换班级，那么 修改 TZ_FORM_WRK_T 里面的内容 by caoy 20170118
				//
				if (!"".equals(strNewClassId) && strNewClassId != null) {
					if (!strClassId.equals(strNewClassId)) {
						if (!"TZ_GUEST".equals(oprid) && !"".equals(oprid)) {
							sql = " SELECT COUNT(OPRID) from PS_TZ_FORM_WRK_T A,PS_TZ_APP_INS_T B where  A.TZ_CLASS_ID=? AND A.OPRID=? AND A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND B.TZ_APP_FORM_STA='U'";
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

				System.out.println("strClassId:" + strClassId);
				System.out.println("tempClassId:" + tempClassId);
				System.out.println("numAppInsId:" + numAppInsId);

				if (!errMsg[0].equals("1")) {
					mapData = null;

					sql = "SELECT TZ_GUEST_APPLY,TZ_JG_ID,TZ_APP_MODAL_ID,TZ_IS_APP_OPEN FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID = ?";
					mapData = sqlQuery.queryForMap(sql, new Object[] { strClassId });
					strGuestApply = mapData.get("TZ_GUEST_APPLY") == null ? ""
							: String.valueOf(mapData.get("TZ_GUEST_APPLY"));

					strAppOrgId = mapData.get("TZ_JG_ID") == null ? "" : String.valueOf(mapData.get("TZ_JG_ID"));

					String TZ_APP_MODAL_ID = mapData.get("TZ_APP_MODAL_ID") == null ? ""
							: String.valueOf(mapData.get("TZ_APP_MODAL_ID"));
					String TZ_IS_APP_OPEN = mapData.get("TZ_IS_APP_OPEN") == null ? ""
							: String.valueOf(mapData.get("TZ_IS_APP_OPEN"));

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
								// ////System.out.println("1111111");
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
								strAppInsVersionDb = psTzAppInsT.getTzAppInsVersion();
							} else {
								if (TZ_IS_APP_OPEN != null && TZ_IS_APP_OPEN.equals("Y")) {
									strTplId = TZ_APP_MODAL_ID;
									strAppInsId = String.valueOf(getSeqNum.getSeqNum("TZ_APP_INS_T", "TZ_APP_INS_ID"));
									numAppInsId = Long.parseLong(strAppInsId);
								}

								// sql = "SELECT TZ_APP_MODAL_ID FROM
								// PS_TZ_CLASS_INF_T WHERE TZ_IS_APP_OPEN = 'Y'
								// AND TZ_CLASS_ID = ?";
								// strTplId = sqlQuery.queryForObject(sql, new
								// Object[] { strClassId }, "String");
								// strAppInsId =
								// String.valueOf(getSeqNum.getSeqNum("TZ_APP_INS_T",
								// "TZ_APP_INS_ID"));
								// numAppInsId = Long.parseLong(strAppInsId);
							}
						}
					}
				}
			} else {
				if (!"".equals(strRefLetterId) && strRefLetterId != null) {
					if (numAppInsId > 0) {
						// 查询推荐信报名表编号
						mapData = null;
						String sqlGetKsTjxInfo = "SELECT TZ_APP_INS_ID,OPRID FROM PS_TZ_KS_TJX_TBL WHERE TZ_REF_LETTER_ID = ? AND TZ_TJX_APP_INS_ID = ?";
						mapData = sqlQuery.queryForMap(sqlGetKsTjxInfo, new Object[] { strRefLetterId, strAppInsId });
						strAppInsIdRefer = mapData.get("TZ_APP_INS_ID") == null ? ""
								: String.valueOf(mapData.get("TZ_APP_INS_ID"));
						strAppOprId = mapData.get("OPRID") == null ? "" : String.valueOf(mapData.get("OPRID"));
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
						}
					} else {
						errMsg[0] = "1";
						errMsg[1] = strParaError;
						strMsg = strParaError;
					}
				} else {
					// 非法操作
					errMsg[0] = "1";
					errMsg[1] = strParaError;
					strMsg = strParaError;
				}
			}
			// 当前报名表实例版本是否和数据库一致
			if (strAppInsVersionDb == null) {
				strAppInsVersionDb = "";
			}

			System.out.println("strAppInsVersion:" + strAppInsVersion);
			System.out.println("strAppInsVersionDb:" + strAppInsVersionDb);

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

			System.out.println("报名表保存数据预处理End,Time=" + (System.currentTimeMillis() - time));

			time = System.currentTimeMillis();
			System.out.println("报名表保存数据处理Begin");

			if ("0".equals(errMsg[0]) && "".equals(strMsg)) {

				/*********************************************************************************************
				 * 用信号量来控制同一台服务器上只能允许5个考生进行排队执行保存、提交请求，并控制同一个报名表不能同时提交多次保存、
				 * 提交操作请求 张浪添加，20170925 【修改开始】
				 *********************************************************************************************/
				System.out.println("可用信号量总数：" + onlineAppSaveLockCounter.availablePermits());
				Semaphore tmpSemaphore = null;
				boolean hasGetOnlineAppLock = false;
				boolean hasGetSemaphore = false;
				try {
					// 同一个应用服务内只允许5个考生同时进入保存、提交报名表操作排队，否则报系统忙，请稍候再试。
					try {
						if (onlineAppSaveLockCounter.getQueueLength() >= tzOnlineAppServiceImpl.semaphoreCount
								|| onlineAppSaveLockCounter.tryAcquire(5000, TimeUnit.MILLISECONDS) == false) {
							throw new TzException(strSystemBusy);
						}
						hasGetOnlineAppLock = true;

						// 获取当前报名表对应的信号灯，每个报名表只能允许一次保存、提交操作，防止同一个报名表保存提交多次请求
						Map.Entry<String, Semaphore> tmpSemaphoreObject = tzGdObject.getSemaphore(
								"com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl.tzOnlineAppServiceImpl-20170925",
								tempClassId + "-" + oprid + "-" + numAppInsId);
						if (tmpSemaphoreObject == null || tmpSemaphoreObject.getKey() == null
								|| tmpSemaphoreObject.getValue() == null) {
							// 如果返回的信号灯为空，报系统忙，请稍后再试
							throw new TzException(strSystemBusy);
						} else {
							tmpSemaphore = tmpSemaphoreObject.getValue();

							// 先判断当前报名表对应信号量大于等于1，说明当前报名表有请求尚在执行，否则获取信号灯
							if (tmpSemaphore.getQueueLength() >= 1 || tmpSemaphore.tryAcquire() == false) {
								throw new TzException(strRequestRept);
							}

							// 已获取信号灯，执行请求完成后需要释放
							hasGetSemaphore = true;
						}
					} catch (TzException e) {
						throw e;
					} catch (InterruptedException e2) {
						throw new TzException(strSystemBusy);
					}
					/*********************************************************************************************
					 * 用信号量来控制同一台服务器上只能允许5个考生进行排队执行保存、提交请求，并控制同一个报名表不能同时提交多次保存、
					 * 提交操作请求 张浪添加，20170925 【修改结束】
					 *********************************************************************************************/

					System.out.println("报名表保存保存用户数据Begin");
					long time2 = System.currentTimeMillis();

					sql = "SELECT TZ_USE_TYPE FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ?";
					strTplType = sqlQuery.queryForObject(sql, new Object[] { strTplId }, "String");
					String strOtype = "";
					strOtype = String.valueOf(jacksonUtil.getString("TZ_APP_C_TYPE"));

					mapData = jacksonUtil.getMap("data");

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
						Map<String, Object> mapJsonItems = null;
						String strIsDoubleLine = null;
						String strIsSingleLine = null;
						String strIsFixedContainer = null;
						List<?> mapChildrens1 = null;
						Map<String, Object> mapChildren1 = null;
						Map<String, Object> mapJsonChildrenItems = null;
						for (Entry<String, Object> entry : mapData.entrySet()) {
							mapJsonItems = (Map<String, Object>) entry.getValue();
							strIsDoubleLine = "";
							if (mapJsonItems.containsKey("isDoubleLine")) {
								strIsDoubleLine = String.valueOf(mapJsonItems.get("isDoubleLine"));
							}
							strIsSingleLine = "";
							if (mapJsonItems.containsKey("isSingleLine")) {
								strIsSingleLine = String.valueOf(mapJsonItems.get("isSingleLine"));
							}
							strIsFixedContainer = "";
							if (mapJsonItems.containsKey("fixedContainer")) {
								strIsSingleLine = String.valueOf(mapJsonItems.get("fixedContainer"));
							}
							if ("Y".equals(strIsDoubleLine)) {
								// 多行容器
								if ("Y".equals(strIsFixedContainer)) {
									// 固定多行容器
								} else {
									mapChildrens1 = (ArrayList<?>) mapJsonItems.get("children");
									for (Object children1 : mapChildrens1) {
										mapChildren1 = null;
										mapChildren1 = (Map<String, Object>) children1;
										for (Entry<String, Object> entryChildren : mapChildren1.entrySet()) {
											mapJsonChildrenItems = null;
											mapJsonChildrenItems = (Map<String, Object>) entryChildren.getValue();
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
														if ("".equals(strLastName)
																&& strDefVal.contains("TZ_LAST_NAME")) {
															strLastName = String
																	.valueOf(mapJsonChildrenItems.get("value"));
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
							String sqlGetOrgId = "SELECT TZ_JG_ID FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ? AND TZ_EFFEXP_ZT = 'Y' LIMIT 1";
							strAppOrgId = sqlQuery.queryForObject(sqlGetOrgId, new Object[] { strTplId }, "String");
						}
						// 创建用户
						strGuestOprId = createGuestUser(strAppOrgId, strNAME);
						strAppOprId = strGuestOprId;
					}

					System.out.println("报名表保存保存用户数据End,Time=" + (System.currentTimeMillis() - time2));

					if ("SAVE".equals(strOtype)) {
						System.out.println("报名表保存SAVE数据Begin");
						time2 = System.currentTimeMillis();
						strMsg = tzOnlineAppEngineImpl.saveAppForm(strTplId, numAppInsId, tempClassId, strAppOprId,
								strData, strTplType, strIsGuest, strAppInsVersionDb, strAppInsState, strBatchId,
								strClassId, strPwd, strOtype, isPwd, strRefLetterId, isMobile, strAppOrgId, isWeChart);

						// 如果是推荐信就不保存扩展表
						if (!"".equals(strRefLetterId) && strRefLetterId != null) {
							System.out.println("TJXID===>"+strRefLetterId);
						} else {
							// 报名表高级设置同步
							tzOnlineAppSync.appSyncMain(numAppInsId, strTplId, strAppOprId);
						}

						if ("".equals(strMsg)) {
							strMsg = tzOnlineAppEngineImpl.checkFiledValid(numAppInsId, strTplId, strPageId, "submit",
									strTplType, strClassId, strBatchId, strLanguage, strIsAdmin);
							/* 当前页面是否完成 */
							String sqlCurrentPageCompleteState = "SELECT TZ_HAS_COMPLETE FROM PS_TZ_APP_COMP_TBL WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
							String strPageCompleteState1 = sqlQuery.queryForObject(sqlCurrentPageCompleteState,
									new Object[] { numAppInsId, strPageId }, "String");
							if (!"Y".equals(strPageCompleteState1)) {
								strMsg = "当前页面未完成";
							} else {
								strMsg = "";
							}

							/*
							 * strMsg = tzOnlineAppEngineImpl.checkFiledValid(
							 * numAppInsId, strTplId, strPageId, "save",
							 * strTplType); ////
							 * //System.out.println("checkFiledValid：" +
							 * strMsg);
							 * 
							 * if ("".equals(strMsg)) {
							 * tzOnlineAppEngineImpl.savePageCompleteState(
							 * numAppInsId, strPageId, "Y"); } else {
							 * tzOnlineAppEngineImpl.savePageCompleteState(
							 * numAppInsId, strPageId, "N"); }
							 */
						}
						// 模版级事件 JAVA 版本目前没有 注销掉
						// String sqlGetModalEvents = "SELECT
						// CMBC_APPCLS_PATH,CMBC_APPCLS_NAME,CMBC_APPCLS_METHOD
						// FROM
						// PS_TZ_APP_EVENTS_T WHERE TZ_APP_TPL_ID = ? AND
						// TZ_EVENT_TYPE = 'SA_A'";
						// List<?> listGetModalEvents =
						// sqlQuery.queryForList(sqlGetModalEvents, new Object[]
						// {
						// strTplId });
						// for (Object objDataGetModalEvents :
						// listGetModalEvents) {
						// Map<String, Object> MapGetModalEvents = (Map<String,
						// Object>) objDataGetModalEvents;
						// String strAppClassPath = "";
						// String strAppClassName = "";
						// String strAppClassMethod = "";
						// String strEventReturn = "";
						// strAppClassPath =
						// MapGetModalEvents.get("CMBC_APPCLS_PATH") == null ?
						// ""
						// :
						// String.valueOf(MapGetModalEvents.get("CMBC_APPCLS_PATH"));
						// strAppClassName =
						// MapGetModalEvents.get("CMBC_APPCLS_NAME") == null ?
						// ""
						// :
						// String.valueOf(MapGetModalEvents.get("CMBC_APPCLS_NAME"));
						// strAppClassMethod =
						// MapGetModalEvents.get("CMBC_APPCLS_METHOD") == null ?
						// ""
						// :
						// String.valueOf(MapGetModalEvents.get("CMBC_APPCLS_METHOD"));
						// if (!"".equals(strAppClassPath) &&
						// !"".equals(strAppClassName)
						// && !"".equals(strAppClassMethod)) {
						// // 根据配置需要去调用对应的程序
						// tzOnlineAppEventServiceImpl
						// tzOnlineAppEventServiceImpl =
						// (tzOnlineAppEventServiceImpl) ctx
						// .getBean(strAppClassPath + "." + strAppClassName);
						// switch (strAppClassMethod) {
						// // 根据报名表配置的方法名称去调用不同的方法
						// }
						//
						// }
						// }
						System.out.println("报名表保存SAVE数据End,Time=" + (System.currentTimeMillis() - time2));
					} else if ("PRE".equals(strOtype)) {
						System.out.println("报名表保存PRE数据Begin");
						time2 = System.currentTimeMillis();
						// 先保存数据
						strMsg = tzOnlineAppEngineImpl.saveAppForm(strTplId, numAppInsId, tempClassId, strAppOprId,
								strData, strTplType, strIsGuest, strAppInsVersionDb, strAppInsState, strBatchId,
								strClassId, strPwd, "SAVE", isPwd, strRefLetterId, isMobile, strAppOrgId, isWeChart);
						// 模版级事件 JAVA 版本目前没有 注销掉
						// String sqlGetModalEvents = "SELECT
						// CMBC_APPCLS_PATH,CMBC_APPCLS_NAME,CMBC_APPCLS_METHOD
						// FROM
						// PS_TZ_APP_EVENTS_T WHERE TZ_APP_TPL_ID = ? AND
						// TZ_EVENT_TYPE = 'SU_A'";
						// List<?> listGetModalEvents =
						// sqlQuery.queryForList(sqlGetModalEvents, new Object[]
						// {
						// strTplId });
						// for (Object objDataGetModalEvents :
						// listGetModalEvents) {
						// Map<String, Object> MapGetModalEvents = (Map<String,
						// Object>) objDataGetModalEvents;
						// String strAppClassPath = "";
						// String strAppClassName = "";
						// String strAppClassMethod = "";
						// String strEventReturn = "";
						// strAppClassPath =
						// MapGetModalEvents.get("CMBC_APPCLS_PATH") == null ?
						// ""
						// :
						// String.valueOf(MapGetModalEvents.get("CMBC_APPCLS_PATH"));
						// strAppClassName =
						// MapGetModalEvents.get("CMBC_APPCLS_NAME") == null ?
						// ""
						// :
						// String.valueOf(MapGetModalEvents.get("CMBC_APPCLS_NAME"));
						// strAppClassMethod =
						// MapGetModalEvents.get("CMBC_APPCLS_METHOD") == null ?
						// ""
						// :
						// String.valueOf(MapGetModalEvents.get("CMBC_APPCLS_METHOD"));
						// if (!"".equals(strAppClassPath) &&
						// !"".equals(strAppClassName)
						// && !"".equals(strAppClassMethod)) {
						// tzOnlineAppEventServiceImpl
						// tzOnlineAppEventServiceImpl =
						// (tzOnlineAppEventServiceImpl) ctx
						// .getBean(strAppClassPath + "." + strAppClassName);
						// switch (strAppClassMethod) {
						// // 根据报名表配置的方法名称去调用不同的方法
						// }
						// }
						// }

						// 提交数据
						// String strMsgAlter = "";

						if ("".equals(strMsg)) {
							strMsg = tzOnlineAppEngineImpl.checkFiledValid(numAppInsId, strTplId, strPageId, "pre",
									strTplType, strClassId, strBatchId, strLanguage, strIsAdmin);

						}

						// if ("".equals(strMsg)) {
						// strMsg =
						// tzOnlineAppEngineImpl.preAppForm(numAppInsId);

						if ("".equals(strMsg)) {
							strMsg = tzOnlineAppEngineImpl.preAppForm(numAppInsId);
							tzOnlineAppEngineImpl.savePageCompleteState(numAppInsId, strPageId, "Y");
							// 预备提交发送站内信件
							tzOnlineAppEngineImpl.sendSiteEmail(numAppInsId, "TZ_BMB_PRESUB", strAppOprId, strAppOrgId,
									"报名表预提交发送站内信", "BMBP", null);
						} else {
							tzOnlineAppEngineImpl.savePageCompleteState(numAppInsId, strPageId, "N");
						}

						if (StringUtils.equals("Y", strIsAdmin) && StringUtils.equals("Y", strIsEdit)) {
							// 如果是管理员并且可编辑的话继续 By WRL@20161027
						}
						// }
						System.out.println("报名表保存PRE数据End,Time=" + (System.currentTimeMillis() - time2));
					} else if ("SUBMIT".equals(strOtype)) {
						System.out.println("报名表保存SUBMIT数据Begin");
						time2 = System.currentTimeMillis();
						// 先保存数据
						strMsg = tzOnlineAppEngineImpl.saveAppForm(strTplId, numAppInsId, tempClassId, strAppOprId,
								strData, strTplType, strIsGuest, strAppInsVersionDb, strAppInsState, strBatchId,
								strClassId, strPwd, strOtype, isPwd, strRefLetterId, isMobile, strAppOrgId, isWeChart);
						// 模版级事件 JAVA 版本目前没有 注销掉
						// String sqlGetModalEvents = "SELECT
						// CMBC_APPCLS_PATH,CMBC_APPCLS_NAME,CMBC_APPCLS_METHOD
						// FROM
						// PS_TZ_APP_EVENTS_T WHERE TZ_APP_TPL_ID = ? AND
						// TZ_EVENT_TYPE = 'SU_A'";
						// List<?> listGetModalEvents =
						// sqlQuery.queryForList(sqlGetModalEvents, new Object[]
						// {
						// strTplId });
						// for (Object objDataGetModalEvents :
						// listGetModalEvents) {
						// Map<String, Object> MapGetModalEvents = (Map<String,
						// Object>) objDataGetModalEvents;
						// String strAppClassPath = "";
						// String strAppClassName = "";
						// String strAppClassMethod = "";
						// String strEventReturn = "";
						// strAppClassPath =
						// MapGetModalEvents.get("CMBC_APPCLS_PATH") == null ?
						// ""
						// :
						// String.valueOf(MapGetModalEvents.get("CMBC_APPCLS_PATH"));
						// strAppClassName =
						// MapGetModalEvents.get("CMBC_APPCLS_NAME") == null ?
						// ""
						// :
						// String.valueOf(MapGetModalEvents.get("CMBC_APPCLS_NAME"));
						// strAppClassMethod =
						// MapGetModalEvents.get("CMBC_APPCLS_METHOD") == null ?
						// ""
						// :
						// String.valueOf(MapGetModalEvents.get("CMBC_APPCLS_METHOD"));
						// if (!"".equals(strAppClassPath) &&
						// !"".equals(strAppClassName)
						// && !"".equals(strAppClassMethod)) {
						// tzOnlineAppEventServiceImpl
						// tzOnlineAppEventServiceImpl =
						// (tzOnlineAppEventServiceImpl) ctx
						// .getBean(strAppClassPath + "." + strAppClassName);
						// switch (strAppClassMethod) {
						// // 根据报名表配置的方法名称去调用不同的方法
						// }
						// }
						// }

						// 如果是推荐信就不保存扩展表
						if (!"".equals(strRefLetterId) && strRefLetterId != null) {
							System.out.println("THXID===>"+strRefLetterId);
						} else {
							// 报名表高级设置同步
							tzOnlineAppSync.appSyncMain(numAppInsId, strTplId, strAppOprId);
						}

						// 提交数据
						// String strMsgAlter = "";
						if ("".equals(strMsg)) {
							strMsg = tzOnlineAppEngineImpl.checkFiledValid(numAppInsId, strTplId, "", "submit",
									strTplType, strClassId, strBatchId, strLanguage, strIsAdmin);
						}
						if ("".equals(strMsg)) {
							if (StringUtils.equals("Y", strIsAdmin) && StringUtils.equals("Y", strIsEdit)) {
								// 如果是管理员并且可编辑的话继续 By WRL@20161027
							} else {
								// 如果是推荐信，则提交后发送邮件
								if ("TJX".equals(strTplType)) {
									strMsg = tzOnlineAppEngineImpl.submitAppForm(numAppInsId, strClassId, strAppOprId,
											strTplType, strBatchId, strPwd, isPwd);

									String strSubmitTjxSendEmail = tzTjxThanksServiceImpl.sendTJX_Thanks(numAppInsId);
									// TJX提交 发送站内信
									tzOnlineAppEngineImpl.sendSiteEmail(numAppInsId, "TZ_TJX_SUBSUC", strAppOprId,
											strAppOrgId, "推荐信提交发送站内信", "TJXZ", strRefLetterId);
								}
								if ("BMB".equals(strTplType)) {
								}
							}
						}
						if ("BMB".equals(strTplType)) {

						}
						System.out.println("报名表保存SUBMIT数据End,Time=" + (System.currentTimeMillis() - time2));
					} else if ("CONFIRMSUBMIT".equals(strOtype)) {
						System.out.println("报名表保存CONFIRMSUBMIT数据Begin");
						time2 = System.currentTimeMillis();
						/* 确认提交报名表 */
						if (StringUtils.equals("Y", strIsAdmin) && StringUtils.equals("Y", strIsEdit)) {
							// 如果是管理员并且可编辑的话继续 By WRL@20161027
							tzOnlineAppEngineImpl.savaContactInfo(numAppInsId, strTplId, strAppOprId);
							// 报名表高级设置同步
							tzOnlineAppSync.appSyncMain(numAppInsId, strTplId, strAppOprId);
						} else {
							strMsg = tzOnlineAppEngineImpl.submitAppForm(numAppInsId, strClassId, strAppOprId,
									strTplType, strBatchId, strPwd, isPwd);
							if ("BMB".equals(strTplType)) {
								/* 20170417 by hjl */
								if ("".equals(strMsg)) {
									// 同步报名人联系方式 提交成功后保存数据
									tzOnlineAppEngineImpl.savaAppKsInfoExt(numAppInsId, strAppOprId);
								}

								// 报名表高级设置同步
								tzOnlineAppSync.appSyncMain(numAppInsId, strTplId, strAppOprId);

								tzOnlineAppEngineImpl.savaContactInfo(numAppInsId, strTplId, strAppOprId);
								// 发送邮件
								String strSubmitSendEmail = tzOnlineAppEngineImpl.sendSubmitEmail(numAppInsId, strTplId,
										strAppOprId, strAppOrgId, strTplType);

								// 报名表提交 发送站内信
								tzOnlineAppEngineImpl.sendSiteEmail(numAppInsId, "TZ_BMB_FORSUB", strAppOprId,
										strAppOrgId, "报名表提交发送站内信", "BMBZ", null);

							}
						}
						System.out.println("报名表保存CONFIRMSUBMIT数据End,Time=" + (System.currentTimeMillis() - time2));
					}

					/*********************************************************************************************
					 * 请求执行完成后,释放信号量 张浪添加，20170925 【修改开始】
					 *********************************************************************************************/
				} catch (TzException e) {
					errMsg[0] = "1";
					errMsg[1] = e.getMessage();
				} finally {
					// 释放信号量
					if (hasGetSemaphore) {
						tmpSemaphore.release();
					}
					// 释放信号量
					if (hasGetOnlineAppLock) {
						onlineAppSaveLockCounter.release();
					}
				}
				/*********************************************************************************************
				 * 请求执行完成后,释放信号量 张浪添加，20170925 【修改结束】
				 *********************************************************************************************/
			}

			if (!"".equals(strMsg)) {
				successFlag = "1";
			}

			System.out.println("报名表保存数据处理End,Time=" + (System.currentTimeMillis() - time));
			time = System.currentTimeMillis();

			System.out.println("报名表保存页面完成状态Begin");
			// 页面完成状态Json
			String strPageXxxBh = "";
			String strPageCompleteState = "";

			ArrayList<Map<String, Object>> listJsonCompleteStateJson = new ArrayList<Map<String, Object>>();
			String sqlGetPageCompleteState = "SELECT TZ_XXX_BH FROM PS_TZ_APP_XXXPZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_COM_LMC = 'Page'";
			List<?> listGetPageCompleteState = sqlQuery.queryForList(sqlGetPageCompleteState,
					new Object[] { strTplId });
			Map<String, Object> MapGetPageCompleteState = null;
			Map<String, Object> strPageCompleteStateJson = null;
			String sqlPageHasCompleteFlag = "SELECT TZ_HAS_COMPLETE FROM PS_TZ_APP_COMP_TBL WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
			for (Object objDataGetPageCompleteState : listGetPageCompleteState) {
				MapGetPageCompleteState = (Map<String, Object>) objDataGetPageCompleteState;
				strPageXxxBh = MapGetPageCompleteState.get("TZ_XXX_BH") == null ? ""
						: String.valueOf(MapGetPageCompleteState.get("TZ_XXX_BH"));

				strPageCompleteState = sqlQuery.queryForObject(sqlPageHasCompleteFlag,
						new Object[] { numAppInsId, strPageXxxBh }, "String");
				if (!"Y".equals(strPageCompleteState)) {
					strPageCompleteState = "N";
				}
				strPageCompleteStateJson = new HashMap<String, Object>();
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

			System.out.println("报名表保存页面完成状态End,Time=" + (System.currentTimeMillis() - time));
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
				//// //System.out.println("boolRtn:" + boolRtn);
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
					//// //System.out.println("ADD cookie");
				}
				return "{\"code\": \"" + successFlag + "\",\"msg\": \"" + strMsg + "\"}";
			} else if (strEType.equals("PWDHTML")) {
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

}
