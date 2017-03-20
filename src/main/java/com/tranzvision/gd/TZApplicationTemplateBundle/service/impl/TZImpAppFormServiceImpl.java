package com.tranzvision.gd.TZApplicationTemplateBundle.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdKjComServiceImpl;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzLxfsInfoTblMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzRegUserTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzLxfsInfoTbl;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzRegUserT;
import com.tranzvision.gd.TZRecommendationBundle.service.impl.TzTjxThanksServiceImpl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppCcTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppDhccTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppDhhsTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppHiddenTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppInsTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzFormAttTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzFormWrkTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzKsTjxTblMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppCcT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppDhccT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppDhhsT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppHiddenT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppInsT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormAttT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormWrkT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzKsTjxTbl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl.tzOnlineAppRulesImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.encrypt.Sha3DesMD5;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.TZApplicationTemplateBundle.service.impl.TZImpAppFormServiceImpl")
public class TZImpAppFormServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private HttpServletResponse response;

	@Autowired
	private PsTzKsTjxTblMapper psTzKsTjxTblMapper;
	@Autowired
	private PsTzLxfsInfoTblMapper psTzLxfsInfoTblMapper;
	
	@Autowired
	private PsTzRegUserTMapper psTzRegUserTMapper;
	
	@Autowired
	private GdKjComServiceImpl gdKjComServiceImpl;
	
	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private TzTjxThanksServiceImpl tzTjxThanksServiceImpl;

	@Autowired
	private tzOnlineAppRulesImpl tzOnlineAppRulesImpl;

	@Autowired
	private PsTzAppCcTMapper psTzAppCcTMapper;

	@Autowired
	private PsTzAppInsTMapper psTzAppInsTMapper;

	@Autowired
	private PsTzAppDhccTMapper psTzAppDhccTMapper;

	@Autowired
	private PsTzFormAttTMapper psTzFormAttTMapper;

	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private PsTzFormWrkTMapper psTzFormWrkTMapper;

	@Autowired
	private PsTzAppDhhsTMapper psTzAppDhhsTMapper;

	@Autowired
	private PsTzAppHiddenTMapper psTzAppHiddenTMapper;

	/**
	 * 批量导入报名表
	 * @param min	报名表编号最小值
	 * @param max	报名表编号最大值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String impAppForm(int min, int max) {
		String[] errMsg = { "0", "" };
		String retMsg = "";
		String tplJson = "";
		JacksonUtil jacksonUtil1 = new JacksonUtil();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		
		String sql = "SELECT * FROM PS_TZ_APPINS_TBL WHERE cast(TZ_APP_INS_ID as unsigned int) >= ? AND cast(TZ_APP_INS_ID as unsigned int) < ? order by cast(TZ_APP_INS_ID as unsigned int)";
		List<?> resultlist = sqlQuery.queryForList(sql, new Object[] { min,max });
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Object obj : resultlist) {
			errMsg[0] = "0";
			errMsg[1] = "";
			
			Map<String, Object> result = (Map<String, Object>) obj;
			String attrInsid = result.get("TZ_APP_INS_ID") == null ? "0" : String.valueOf(result.get("TZ_APP_INS_ID"));
//			String attrClsid = result.get("TZ_CLASS_ID") == null ? "122" : String.valueOf(result.get("TZ_CLASS_ID"));
			/*班级编号*/
			String attrClsid = "123";
			/*批次编号，这里不确定对应的批次是多少*/
			String attrBatchid = result.get("TZ_BATCH_ID") == null ? "45" : String.valueOf(result.get("TZ_BATCH_ID"));
			/*新版系统模板编号*/
//			String attrTplid = result.get("TZ_APP_TPL_ID") == null ? "129" : String.valueOf(result.get("TZ_APP_TPL_ID"));
//			String attrTplid = "129";
			String attrOprid = result.get("OPRID") == null ? "" : String.valueOf(result.get("OPRID"));

			String attrText1 = result.get("TEXTAREA_1") == null ? "" : String.valueOf(result.get("TEXTAREA_1"));
			String attrText2 = result.get("TEXTAREA_2") == null ? "" : String.valueOf(result.get("TEXTAREA_2"));
			String attrText3 = result.get("TEXTAREA_3") == null ? "" : String.valueOf(result.get("TEXTAREA_3"));
//			String attrAddOprid = result.get("ROW_ADDED_OPRID") == null ? attrOprid : String.valueOf(result.get("ROW_ADDED_OPRID"));
//			String attrAddDttm = result.get("ROW_ADDED_DTTM") == null ? attrOprid : String.valueOf(result.get("ROW_ADDED_DTTM"));
//			String attrLastDttm = result.get("ROW_LASTMANT_DTTM") == null ? attrOprid : String.valueOf(result.get("ROW_LASTMANT_DTTM"));
//			String attrLastOprid = result.get("ROW_LASTMANT_OPRID") == null ? attrOprid : String.valueOf(result.get("ROW_LASTMANT_OPRID"));
			String isHasOpr = "SELECT 'Y' FROM PS_TZ_REG_USER_T WHERE OPRID = ? LIMIT 0,1";
			String isHas = sqlQuery.queryForObject(isHasOpr, new Object[] { attrOprid }, "String");
			if(!StringUtils.equals("Y", isHas)){
				retMsg  = retMsg + "<br>" + attrOprid +  "(" + attrInsid + ")   ----->    考生不存在";
				continue;
			}
			if(StringUtils.isBlank(tplJson)){
				try {
					/*初始化模板文件内容，是通过模板报文将其中的children改成[]后生成的，可通过模板报文、实例报文对比修改*/
					tplJson = tzGDObject.getHTMLText("HTML.TZApplicationTemplateBundle.TZ_APPTPL_JSON_STR_INIT_HTML");
				} catch (TzSystemException e) {
					e.printStackTrace();
				}
			}
			Map<String, Object> zsMap = new HashMap<String, Object>();
			zsMap.put("A1486638671285", attrText1);
			zsMap.put("A1486638683997", attrText2);
			zsMap.put("A1486638680350", attrText3);
			Map<String, Object> insJson = this.createInsJson(tplJson,zsMap);
			paramsMap.put("TZ_APP_C_TYPE", "SAVE");
			paramsMap.put("isEdit", "");
			paramsMap.put("TZ_REF_LETTER_ID", "");
			paramsMap.put("PASSWORD", "");
			paramsMap.put("ISPWD", "N");
			paramsMap.put("TZ_APP_INS_ID", "0");
			paramsMap.put("TZ_APP_INS_VERSION", "");
			paramsMap.put("TZ_PAGE_ID", "");
			paramsMap.put("TZ_LANGUAGE", "ZHS");
			paramsMap.put("TZ_CLASS_ID", attrClsid);
			paramsMap.put("TZ_BATCH_ID", attrBatchid);
			paramsMap.put("TZ_NEW_CLASS_ID", "");
			paramsMap.put("data", insJson);
			String[] aryParam = new String[] { jacksonUtil1.Map2json(paramsMap) };

			this.tzUpdate(aryParam, errMsg,attrOprid);
			if(!StringUtils.equals("0", errMsg[0])){
				retMsg  = retMsg + "<br>" + attrOprid +  "   ----->    " + errMsg[1];
			}
		}
		return retMsg;
	}
	
	
	/**
	 * 批量导入推荐信
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String impAppLetter(String clsid, int min, int max) {

		String[] errMsg = { "0", "" };
		String retMsg = "";
		String engTplJson = "";
		String zhsTplJson = "";
		String tplJson = "";
		JacksonUtil jacksonUtil1 = new JacksonUtil();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		
		String sql = "SELECT * FROM PS_TZ_LETTER_INS_TBL WHERE cast(TZ_APP_INS_ID as unsigned int) >= ? AND cast(TZ_APP_INS_ID as unsigned int) < ? order by cast(TZ_APP_INS_ID as unsigned int)";
		List<?> resultlist = sqlQuery.queryForList(sql, new Object[] { min,max });
		for (Object obj : resultlist) {
			errMsg[0] = "0";
			errMsg[1] = "";
			
			Map<String, Object> result = (Map<String, Object>) obj;

			String attrOprid = result.get("OPRID") == null ? "" : String.valueOf(result.get("OPRID"));
			String attrText1 = result.get("TEXTAREA_1") == null ? "" : String.valueOf(result.get("TEXTAREA_1"));
			String attrText2 = result.get("TEXTAREA_2") == null ? "" : String.valueOf(result.get("TEXTAREA_2"));
			String attrText3 = result.get("TEXTAREA_3") == null ? "" : String.valueOf(result.get("TEXTAREA_3"));
			String attrEmail = result.get("TZ_EMAIL") == null ? "" : String.valueOf(result.get("TZ_EMAIL"));
			String lang = result.get("LANGUAGE") == null ? "ZHS" : String.valueOf(result.get("LANGUAGE"));
			
			String isHasOpr = "SELECT 'Y' FROM PS_TZ_REG_USER_T WHERE OPRID = ? LIMIT 0,1";
			String isHas = sqlQuery.queryForObject(isHasOpr, new Object[] { attrOprid }, "String");
			
			String attrInsid = "";
			if(!StringUtils.equals("Y", isHas)){
				retMsg  = retMsg + "<br>" + attrOprid + "----->    考生不存在";
				continue;
			}
			
			String strTjxId = this.createLetter(lang, clsid, attrOprid, attrEmail, attrInsid);
			if(StringUtils.isBlank(attrInsid)){
				retMsg  = retMsg + "<br>班级" + clsid +  "(" + attrOprid + ")   ----->    报名表不存在";
				continue;
			}
			if(StringUtils.isBlank(engTplJson)){
				//初始化英文推荐信内容
				try {
					/*初始化模板文件内容，是通过模板报文将其中的children改成[]后生成的，可通过模板报文、实例报文对比修改*/
					engTplJson = tzGDObject.getHTMLText("HTML.TZApplicationTemplateBundle.TZ_APPTPLE_LETTER_ENG_INITHTML");
				} catch (TzSystemException e) {
					e.printStackTrace();
				}
			}
			
			if(StringUtils.isBlank(zhsTplJson)){
				//初始化中文推荐信内容
				try {
					/*初始化模板文件内容，是通过模板报文将其中的children改成[]后生成的，可通过模板报文、实例报文对比修改*/
					zhsTplJson = tzGDObject.getHTMLText("HTML.TZApplicationTemplateBundle.TZ_APPTPLE_LETTER_ZHS_INITHTML");
				} catch (TzSystemException e) {
					e.printStackTrace();
				}
			}
			
			Map<String, Object> zsMap = new HashMap<String, Object>();

			if(StringUtils.equals("ENG", lang)){
				//推荐信模板编号	131
				zsMap.put("A1486707994123", attrText1);
				zsMap.put("A1486708045185", attrText2);
				zsMap.put("A1486708049933", attrText3);
				tplJson = engTplJson;
			}else{
				//推荐信模板编号	130
				zsMap.put("A1487041145815", attrText1);
				zsMap.put("A1487041140318", attrText2);
				zsMap.put("A1487041105658", attrText3);
				tplJson = zhsTplJson;
			}

			Map<String, Object> insJson = this.createLetterInsJson(tplJson,zsMap);
			
			paramsMap.put("TZ_APP_C_TYPE", "SUBMIT");
			paramsMap.put("isEdit", "");
			paramsMap.put("TZ_REF_LETTER_ID", strTjxId);
			paramsMap.put("PASSWORD", "");
			paramsMap.put("ISPWD", "N");
			paramsMap.put("TZ_APP_INS_ID", attrInsid);
			paramsMap.put("TZ_APP_INS_VERSION", "");
			paramsMap.put("TZ_PAGE_ID", "");
			paramsMap.put("TZ_LANGUAGE", lang);
			paramsMap.put("TZ_CLASS_ID", "");
			paramsMap.put("TZ_BATCH_ID", "");
			paramsMap.put("TZ_NEW_CLASS_ID", "");
			
			paramsMap.put("data", insJson);
			String[] aryParam = new String[] { jacksonUtil1.Map2json(paramsMap) };

			this.tzUpdate(aryParam, errMsg,attrOprid);
			if(!StringUtils.equals("0", errMsg[0])){
				retMsg  = retMsg + "<br>" + attrOprid +  "   ----->    " + errMsg[1];
			}
		}
		return retMsg;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@SuppressWarnings("unchecked")
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg, String attrOprid) {
		String strRet = "{}";

		String successFlag = "0";

		String strMsg = "";

		// 当前登陆人
		String oprid = attrOprid;
//		oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

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
		
		// 报名人Oprid
		String strAppOprId = attrOprid;
		
		// 报名表状态
		String strAppInsState = "";
		
		// 报名表使用模版编号
		String strTplId = "";
		
		// 报名表模板类型
		String strTplType = "";
		
		// 报名表模版语言
		String strLanguage = "";
		
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
		
		// 是否保存密码
		String isPwd = "";

		JacksonUtil jacksonUtil = new JacksonUtil();
		// 表单内容
		int dataLength = actData.length;
		for (int num = 0; num < dataLength; num++) {

			long time = System.currentTimeMillis();

			System.out.println("报名表保存数据预处理Begin");
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
			if (jacksonUtil.containsKey("ISPWD")) {
				isPwd = String.valueOf(jacksonUtil.getString("ISPWD"));
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

			strSessionInvalidTips = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET", "SESSION_INVAILD", strLanguage, "当前会话已失效，请重新登陆。", "The current session is timeout or the current access is invalid,Please relogin.");
			strIllegalOperation = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET", "ILLEGAL_OPERATION", strLanguage, "非法操作", "Illegal operation");
			strParaError = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET", "PARAERROR", strLanguage, "参数错误", "Parameter error.");
			strVersionError = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET", "PAGE_INVALID", strLanguage, "当前页面已失效，请重新进入页面或刷新页面再试。","The current page has expired, please re-enter the page and try again.");
			strClassError = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET", "CLASSERROR", strLanguage, "该班级已经填写报名表，不允许重复。", "The class has filled in the application form, not allowed to repeat.");
			
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
					
					strGuestApply = mapData.get("TZ_GUEST_APPLY") == null ? "" : String.valueOf(mapData.get("TZ_GUEST_APPLY"));
					strAppOrgId = mapData.get("TZ_JG_ID") == null ? "" : String.valueOf(mapData.get("TZ_JG_ID"));
					String TZ_APP_MODAL_ID = mapData.get("TZ_APP_MODAL_ID") == null ? "" : String.valueOf(mapData.get("TZ_APP_MODAL_ID"));
					String TZ_IS_APP_OPEN = mapData.get("TZ_IS_APP_OPEN") == null ? "" : String.valueOf(mapData.get("TZ_IS_APP_OPEN"));

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
							strAppOprId = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, tempClassId }, "String");

							if (!"".equals(strTplId) && strTplId != null && !"".equals(strAppOprId) && strAppOprId != null) {
								if (strAppOprId.equals(oprid) || "Y".equals(strGuestApply)) {
									// 自己操作自己的报名表或者允许匿名报名
								} else {
									/*
									 * 判断当前登录人是不是班级管理员的逻辑与PS版保持一致 By
									 * WRL@20161130 BEGIN
									 */
									sql = "SELECT 'Y' FROM PS_TZ_CLS_ADMIN_T WHERE TZ_CLASS_ID = ? AND OPRID = ?";
									strIsAdmin = sqlQuery.queryForObject(sql, new Object[] { strClassId, oprid }, "String");
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
							}
						} else {
							// 没报名表实例编号
							strAppOprId = oprid;
							if (!"TZ_GUEST".equals(oprid) && !"".equals(oprid)) {
								sql = "SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID = ? AND TZ_CLASS_ID = ?";
								strAppInsId = sqlQuery.queryForObject(sql, new Object[] { oprid, tempClassId }, "String");
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
						strAppInsIdRefer = mapData.get("TZ_APP_INS_ID") == null ? "" : String.valueOf(mapData.get("TZ_APP_INS_ID"));
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
			}else{
				strAppInsVersion = strAppInsVersionDb;
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
				System.out.println("报名表保存保存用户数据End,Time=" + (System.currentTimeMillis() - time2));

				if ("SAVE".equals(strOtype)) {
					System.out.println("报名表保存SAVE数据Begin");
					time2 = System.currentTimeMillis();
					strMsg = this.saveAppForm(strTplId, numAppInsId, tempClassId, strAppOprId, strData,
							strTplType, strIsGuest, strAppInsVersionDb, strAppInsState, strBatchId, strClassId, strPwd,
							strOtype, isPwd);

					System.out.println("报名表保存SAVE数据End,Time=" + (System.currentTimeMillis() - time2));
				} else if ("PRE".equals(strOtype)) {
					System.out.println("报名表保存PRE数据Begin");
					time2 = System.currentTimeMillis();
					// 先保存数据
					strMsg = this.saveAppForm(strTplId, numAppInsId, tempClassId, strAppOprId, strData,
							strTplType, strIsGuest, strAppInsVersionDb, strAppInsState, strBatchId, strClassId, strPwd,
							"SAVE", isPwd);

					if ("".equals(strMsg)) {
						strMsg = this.saveAppForm(strTplId, numAppInsId, tempClassId, strAppOprId,
								strData, strTplType, strIsGuest, strAppInsVersionDb, strAppInsState, strBatchId,
								strClassId, strPwd, strOtype, isPwd);

						if (StringUtils.equals("Y", strIsAdmin) && StringUtils.equals("Y", strIsEdit)) {
							// 如果是管理员并且可编辑的话继续 By WRL@20161027
						} else {
							// 如果是推荐信，则提交后发送邮件
							if ("TJX".equals(strTplType)) {
								strMsg = this.submitAppForm(numAppInsId, strClassId, strAppOprId,
										strTplType, strBatchId, strPwd, isPwd,oprid);
								String strSubmitTjxSendEmail = tzTjxThanksServiceImpl.sendTJX_Thanks(numAppInsId);
							}
							if ("BMB".equals(strTplType)) {
							}
						}
					}
					System.out.println("报名表保存PRE数据End,Time=" + (System.currentTimeMillis() - time2));
				} else if ("SUBMIT".equals(strOtype)) {
					System.out.println("报名表保存SUBMIT数据Begin");
					time2 = System.currentTimeMillis();
					// 先保存数据
					strMsg = this.saveAppForm(strTplId, numAppInsId, tempClassId, strAppOprId, strData,
							strTplType, strIsGuest, strAppInsVersionDb, strAppInsState, strBatchId, strClassId, strPwd,
							strOtype, isPwd);

					if ("".equals(strMsg)) {
						if (StringUtils.equals("Y", strIsAdmin) && StringUtils.equals("Y", strIsEdit)) {
							// 如果是管理员并且可编辑的话继续 By WRL@20161027
						} else {
							// 如果是推荐信，则提交后发送邮件
							if ("TJX".equals(strTplType)) {
								strMsg = this.submitAppForm(numAppInsId, strClassId, strAppOprId,
										strTplType, strBatchId, strPwd, isPwd,oprid);
								String strSubmitTjxSendEmail = tzTjxThanksServiceImpl.sendTJX_Thanks(numAppInsId);
							}
							if ("BMB".equals(strTplType)) {
							}
						}
					}
					System.out.println("报名表保存SUBMIT数据End,Time=" + (System.currentTimeMillis() - time2));
				} else if ("CONFIRMSUBMIT".equals(strOtype)) {
					System.out.println("报名表保存CONFIRMSUBMIT数据Begin");
					time2 = System.currentTimeMillis();
					/* 确认提交报名表 */
					if (StringUtils.equals("Y", strIsAdmin) && StringUtils.equals("Y", strIsEdit)) {
						// 如果是管理员并且可编辑的话继续 By WRL@20161027
						this.savaContactInfo(numAppInsId, strTplId, strAppOprId);
					} else {
						strMsg = this.submitAppForm(numAppInsId, strClassId, strAppOprId, strTplType,
								strBatchId, strPwd, isPwd,oprid);
						if ("BMB".equals(strTplType)) {
							// 同步报名人联系方式
							this.savaContactInfo(numAppInsId, strTplId, strAppOprId);
						}
					}
					System.out.println("报名表保存CONFIRMSUBMIT数据End,Time=" + (System.currentTimeMillis() - time2));
				}
				
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


	private String createGuestUser(String strAppOrgId, String strNAME) {
		// TODO Auto-generated method stub
		return null;
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
			String appInsId = null, appTplId = null;

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

					String PWDHTML = tzGDObject.getHTMLText("HTML.TZWebsiteApplicationBundle.TZ_ONLINE_PWD_HTML", false,
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
	

	// 报名表保存
		@SuppressWarnings("unchecked")
		public String saveAppForm(String strTplId, Long numAppInsId, String strClassId, String strAppOprId,
				String strJsonData, String strTplType, String strIsGuest, String strAppInsVersion, String strAppInsState,
				String strBathId, String newClassId, String pwd, String strOtype, String isPwd) {

			String returnMsg = "";

//			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String oprid = strAppOprId;
			// Long numAppInsId = Long.parseLong(strAppInsId);
			try {
				String sql = "";
				// int count = 0;
				String TZ_APP_FORM_STA = null;
				String INS_ID = null;
				boolean chageClass = false;

				if (newClassId != null && !newClassId.equals(strClassId)) {
					chageClass = true;
				}

				// modity by caoy 保存的时候，如果是 预提交状态 那么状态不改变
				// 更换班级 需要变更 报名表和WOrk表 只需要保存一份实例
				sql = "SELECT TZ_APP_INS_ID,TZ_APP_FORM_STA FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID = ?";
				// count = sqlQuery.queryForObject(sql, new Object[] { numAppInsId
				// }, "Integer");
				Map<String, Object> mapData = null;

				mapData = sqlQuery.queryForMap(sql, new Object[] { numAppInsId });

				if (mapData != null) {
					TZ_APP_FORM_STA = mapData.get("TZ_APP_FORM_STA") == null ? ""
							: String.valueOf(mapData.get("TZ_APP_FORM_STA"));

					INS_ID = mapData.get("TZ_APP_INS_ID") == null ? "" : String.valueOf(mapData.get("TZ_APP_INS_ID"));
				}

				// System.out.println("TZ_APPINS_JSON_STR:" + TZ_APP_FORM_STA);
				// System.out.println("strOtype:" + strOtype);
				// if (count > 0) {
				if (INS_ID != null && !INS_ID.equals("") && Long.parseLong(INS_ID) > 0) {
					PsTzAppInsT psTzAppInsT = new PsTzAppInsT();
					psTzAppInsT.setTzAppInsId(numAppInsId);
					psTzAppInsT.setTzAppTplId(strTplId);
					psTzAppInsT.setTzAppInsVersion(strAppInsVersion);

					if (strOtype.equals("PRE")) {
						psTzAppInsT.setTzAppFormSta("P");
					} else {
						if (TZ_APP_FORM_STA.equals("P") && strAppInsState.equals("S")) {

						} else {
							psTzAppInsT.setTzAppFormSta(strAppInsState);
						}
					}
					psTzAppInsT.setTzAppinsJsonStr(strJsonData);
					psTzAppInsT.setRowLastmantOprid(oprid);
					psTzAppInsT.setRowLastmantDttm(new Date());

					if (isPwd != null && isPwd.equals("Y")) {
						psTzAppInsT.setTzPwd(pwd);
					}
					psTzAppInsTMapper.updateByPrimaryKeySelective(psTzAppInsT);
				} else {
					PsTzAppInsT psTzAppInsT = new PsTzAppInsT();
					psTzAppInsT.setTzAppInsId(numAppInsId);
					psTzAppInsT.setTzAppTplId(strTplId);
					psTzAppInsT.setTzAppInsVersion(strAppInsVersion);
					if (strOtype.equals("PRE")) {
						psTzAppInsT.setTzAppFormSta("P");
					} else {
						psTzAppInsT.setTzAppFormSta(strAppInsState);
					}
					psTzAppInsT.setTzAppinsJsonStr(strJsonData);
					psTzAppInsT.setRowAddedOprid(oprid);
					psTzAppInsT.setRowAddedDttm(new Date());
					psTzAppInsT.setRowLastmantOprid(oprid);
					psTzAppInsT.setRowLastmantDttm(new Date());
					if (isPwd != null && isPwd.equals("Y")) {
						psTzAppInsT.setTzPwd(pwd);
					}
					psTzAppInsTMapper.insert(psTzAppInsT);
				}

				if ("BMB".equals(strTplType)) {
					int count = 0;
					// 如果是 变更班级，那么
					if (chageClass) {

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
				// System.out.println("保存传入数据:"+strJsonData);
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
								// modity by caoy
								if (strClassName.equals("LayoutControls")) {
									this.saveDhLineNum(strItemIdLevel0, numAppInsId,
											(short) ((Map<String, Object>) mapChildrens1.get(0)).size());
								} else {
									this.saveDhLineNum(strItemIdLevel0, numAppInsId, (short) mapChildrens1.size());
								}
								// this.saveDhLineNum(strItemIdLevel0, numAppInsId,
								// (short) mapChildrens1.size());
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
											// 多行容器下的子容器 modity by caoy
											// 解决分组框的某些组合控件的问题
											//// //System.out.println("111:" +
											// mapJsonChildrenItems.get("children"));
											List<Map<String, Object>> mapChildrens2 = null;
											try {
												mapChildrens2 = (ArrayList<Map<String, Object>>) mapJsonChildrenItems
														.get("children");
											} catch (Exception e) {
												// e.printStackTrace();
												mapChildrens2 = new ArrayList<Map<String, Object>>();
												Map<String, Object> cmap = (Map<String, Object>) mapJsonChildrenItems
														.get("children");
												Map<String, Object> ccmap = null;
												for (String key : cmap.keySet()) {
													ccmap = (Map<String, Object>) cmap.get(key);
													mapChildrens2.add(ccmap);
												}
											}

											// ////System.out.println("Size:" +
											// mapChildrens2.size());

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
																	mapChildren2, numAppInsId,oprid);
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
															mapJsonChildrenItems, numAppInsId,oprid);
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
										this.savePerAttrInfo(strItemIdLevel0, mapChildren1, numAppInsId,oprid);
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
		
		// 删除报名表存储表信息
		public void delAppIns(Long numAppInsId) {

			Object[] args = new Object[] { numAppInsId };
			sqlQuery.update("DELETE FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID = ?", args);
			sqlQuery.update("DELETE FROM PS_TZ_APP_DHCC_T WHERE TZ_APP_INS_ID = ?", args);
			sqlQuery.update("DELETE FROM PS_TZ_FORM_ATT_T WHERE TZ_APP_INS_ID = ?", args);
			sqlQuery.update("DELETE FROM PS_TZ_APP_DHCC_T WHERE TZ_APP_INS_ID = ?", args);
			sqlQuery.update("DELETE FROM PS_TZ_APP_DHHS_T WHERE TZ_APP_INS_ID = ?", args);
			sqlQuery.update("DELETE FROM PS_TZ_APP_HIDDEN_T WHERE TZ_APP_INS_ID = ?", args);
		}
		
		/* 保存多行容器的行数信息 */
		public void saveDhLineNum(String strItemId, Long numAppInsId, short numLineDh) {
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

		/* 设置字段是否隐藏 */
		public void saveXxxHidden(Long numAppInsId, String strItemId, String strIsHidden) {
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
		
		// 将json数据解析保存到报名表存储表
		public void savePerXxxIns(String strParentItemId, Map<String, Object> xxxObject, Long numAppInsId) {

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
		// 将json数据解析保存到报名表附件存储表
		public void savePerAttrInfo(String strParentItemId, Map<String, Object> xxxObject, Long numAppInsId,String oprid) {

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
		
		// 同步报名人联系方式
		public void savaContactInfo(Long numAppInsId, String strTplId, String strAppOprId) {
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
		

		// 报名表提交
		public String submitAppForm(Long numAppInsId, String strClassId, String strAppOprId, String strTplType,
				String strBathId, String pwd, String isPwd, String oprid) {

			String returnMsg = "";
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
					if (isPwd != null && isPwd.equals("Y")) {
						psTzAppInsT.setTzPwd(pwd);
					}
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
		/**
		 * 初始化报名表数据
		 * 
		 * @param tplJson
		 * @param zsMap
		 * @return
		 */
		@SuppressWarnings("unchecked")
		private Map<String, Object> createInsJson(String tplJson, Map<String, Object> zsMap) {
			JacksonUtil jacksonUtil2 = new JacksonUtil();
			jacksonUtil2.json2Map(tplJson);
			Map<String, Object> itemsData = jacksonUtil2.getMap("items");
			Map<String, Object> layoutData = (Map<String, Object>) itemsData.get("A1486623607238");
			ArrayList<Map<String, Object>> childData = (ArrayList<Map<String, Object>>) layoutData.get("children");
			for (String key : zsMap.keySet()) {
				String value = zsMap.get(key) == null ? "" : String.valueOf(zsMap.get(key));
				Map<String, Object> xxxData = (Map<String, Object>) childData.get(0).get(key);
				xxxData.put("value", value);
			}
			layoutData.put("children", childData);
			itemsData.put("A1486623607238", layoutData);
			return itemsData;
		}
		

		
		/**
		 * 初始化推荐信
		 * 
		 * @param tplJson
		 * @param zsMap
		 * @return
		 */
		@SuppressWarnings("unchecked")
		private Map<String, Object> createLetterInsJson(String tplJson, Map<String, Object> zsMap) {
			JacksonUtil jacksonUtil2 = new JacksonUtil();
			jacksonUtil2.json2Map(tplJson);
			Map<String, Object> itemsData = jacksonUtil2.getMap("items");

			for (String key : zsMap.keySet()) {
				String value = zsMap.get(key) == null ? "" : String.valueOf(zsMap.get(key));
				Map<String, Object> xxxData = (Map<String, Object>) itemsData.get(key);
				xxxData.put("value", value);
			}
			return itemsData;
		}


		
		private String createLetter(String lang,String classId,String strOprid,String strEmail, String strInsId){
			//推荐信类型
			String strTjxType = "E";
			if(StringUtils.equals("ZHS", lang)){
				strTjxType = "C";
			}
			//报名表实例编号
			Long numAppinsId = sqlQuery.queryForObject("SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID = ? AND OPRID = ? limit 0,1",
					new Object[] { classId, strOprid}, "long");
			strInsId = String.valueOf(numAppinsId);
			//推荐人编号
			String strTjrId = sqlQuery.queryForObject("SELECT COUNT(*) + 1 FROM PS_TZ_KS_TJX_TBL WHERE TZ_APP_INS_ID = ? AND OPRID = ?",new Object[] { classId, strOprid}, "String");
			
			//推荐信编号
			String strTjxId = "";
			if (strTjxId == null || "".equals(strTjxId)) {
				String str_seq1 = String.valueOf((int) (Math.random() * 10000000));
				String str_seq2 = "00000000000000"
						+ String.valueOf(getSeqNum.getSeqNum("TZ_KS_TJX_TBL", "TZ_REF_LETTER_ID"));
				str_seq2 = str_seq2.substring(str_seq2.length() - 15, str_seq2.length());
				strTjxId = str_seq1 + str_seq2;
			}

			PsTzKsTjxTbl psTzKsTjxTbl = psTzKsTjxTblMapper.selectByPrimaryKey(strTjxId);
			if (psTzKsTjxTbl == null) {
				psTzKsTjxTbl = new PsTzKsTjxTbl();
				psTzKsTjxTbl.setTzRefLetterId(strTjxId);
				psTzKsTjxTbl.setTzAppInsId(numAppinsId);
				psTzKsTjxTbl.setOprid(strOprid);
				psTzKsTjxTbl.setTzTjxType(strTjxType);

				psTzKsTjxTbl.setTzTjrId(strTjrId);
				psTzKsTjxTbl.setTzMbaTjxYx("Y");
				psTzKsTjxTbl.setTzTjxTitle("");
				psTzKsTjxTbl.setTzReferrerGname("");
				psTzKsTjxTbl.setTzReferrerName("");
				psTzKsTjxTbl.setTzCompCname("");
				psTzKsTjxTbl.setTzPosition("");
				psTzKsTjxTbl.setTzEmail(strEmail);
				psTzKsTjxTbl.setTzPhoneArea("");
				psTzKsTjxTbl.setTzPhone("");
				psTzKsTjxTbl.setTzGender("M");
				String str_refLetterType = "S";
				psTzKsTjxTbl.setTzReflettertype(str_refLetterType );

				psTzKsTjxTbl.setTzTjxYl1("");
				psTzKsTjxTbl.setTzTjxYl2("");
				psTzKsTjxTbl.setTzTjxYl3("");
				psTzKsTjxTbl.setTzTjxYl4("");
				psTzKsTjxTbl.setTzTjxYl5("");
				psTzKsTjxTbl.setTzTjxYl6("");
				psTzKsTjxTbl.setTzTjxYl7("");
				psTzKsTjxTbl.setTzTjxYl8("");
				psTzKsTjxTbl.setTzTjxYl9("");
				psTzKsTjxTbl.setTzTjxYl10("");
				psTzKsTjxTbl.setTzTjrGx("");

				psTzKsTjxTbl.setAttachsysfilename("");
				psTzKsTjxTbl.setAttachuserfile("");
				psTzKsTjxTbl.setTzAccessPath("");
				psTzKsTjxTbl.setTzAttAUrl("");
				Date dttm = new Date();
				psTzKsTjxTbl.setRowAddedDttm(dttm);
				psTzKsTjxTbl.setRowAddedOprid(strOprid);
				psTzKsTjxTbl.setRowLastmantDttm(dttm);
				psTzKsTjxTbl.setRowLastmantOprid(strOprid);
				psTzKsTjxTblMapper.insert(psTzKsTjxTbl);
			}
			return strTjxId;
		}
		/**
		 * 删除MBA历史账户设计到的报名表
		 * @return
		 */
		public String delAppAll() {
			String sql1 = "DELETE FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID IN (SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID REGEXP BINARY 'MBA_*')";
			String sql2 = "DELETE FROM PS_TZ_APP_DHCC_T WHERE TZ_APP_INS_ID IN (SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID REGEXP BINARY 'MBA_*')";
			String sql3 = "DELETE FROM PS_TZ_FORM_ATT_T WHERE TZ_APP_INS_ID IN (SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID REGEXP BINARY 'MBA_*')";
			String sql4 = "DELETE FROM PS_TZ_APP_DHCC_T WHERE TZ_APP_INS_ID IN (SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID REGEXP BINARY 'MBA_*')";
			String sql5 = "DELETE FROM PS_TZ_APP_DHHS_T WHERE TZ_APP_INS_ID IN (SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID REGEXP BINARY 'MBA_*')";
			String sql6 = "DELETE FROM PS_TZ_APP_HIDDEN_T WHERE TZ_APP_INS_ID IN (SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID REGEXP BINARY 'MBA_*')";
			String sql7 = "DELETE FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID IN (SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID REGEXP BINARY 'MBA_*')";
			String sql8 = "DELETE FROM PS_TZ_FORM_WRK_T WHERE OPRID REGEXP BINARY 'MBA_*'";


			
			int del1 = sqlQuery.update(sql1);
			int del2 = sqlQuery.update(sql2);
			int del3 = sqlQuery.update(sql3);
			int del4 = sqlQuery.update(sql4);
			int del5 = sqlQuery.update(sql5);
			int del6 = sqlQuery.update(sql6);
			int del7 = sqlQuery.update(sql7);
			int del8 = sqlQuery.update(sql8);
			
			String ret = del1 + "    -->" + del2 + "    -->" + del3 + "    -->" + del4 + "    -->" + del5 + "    -->" + del6 + "    -->" + del7 + "    -->" + del8;
			return ret;
		}
}