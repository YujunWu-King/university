package com.tranzvision.gd.TZApplicationSurveyBundle.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcCcTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcDhccTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcInsTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcWjDyTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcWjattTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcWjattchTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcdjBgtTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcCcT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcDhccT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcInsT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjDyTWithBLOBs;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjattT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjattchT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcdjBgtT;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl.tzOnlineAppUtility;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.httpclient.CommonUtils;
import com.tranzvision.gd.util.session.TzSession;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 前台问卷展示以及保存
 * 
 * @author CAOY
 *
 */
@Service("com.tranzvision.gd.TZApplicationSurveyBundle.service.impl.QuestionnaireFillImpl")
public class QuestionnaireFillImpl extends FrameworkImpl {
	private static final Logger logger = LoggerFactory.getLogger(QuestionnaireFillImpl.class);

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SurveryRulesImpl surveryRulesImpl;

	@Autowired
	private PsTzDcInsTMapper psTzDcInsTMapper;

	@Autowired
	private PsTzDcWjDyTMapper psTzDcWjDyTMapper;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private TZGDObject tzGdObject;

	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;

	@Autowired
	private QuestionnaireEditorEngineImpl questionnaireEditorEngineImpl;

	@Autowired
	private LogicControlImpl logicControlImpl;

	@Autowired
	private HttpServletResponse response;

	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;

	@Autowired
	private PsTzDcCcTMapper psTzDcCcTMapper;

	@Autowired
	private PsTzDcDhccTMapper psTzDcDhccTMapper;

	@Autowired
	private PsTzDcdjBgtTMapper psTzDcdjBgtTMapper;

	@Autowired
	private PsTzDcWjattTMapper psTzDcWjattTMapper;

	@Autowired
	private PsTzDcWjattchTMapper psTzDcWjattchTMapper;

	@Autowired
	private TZGDObject tzSQLObject;

	@Autowired
	private ApplicationContext ctx;

	/**
	 * Session存储的测试考生的Oprid
	 */
	public final String userSessionName = "TUser";

	/* 问卷保存 */

	/*******************************************************************************************************************************
	 * 说明：问卷提交 功能逻辑说明: 1、如果是记名问卷，那么检查是否登陆，如果没有登陆，则提示错误
	 * 2、看是否传入了报名表实例编号，如果传入了编号，则继续操作4，如果没有传入报名表编号，则操作6
	 * 3、根据报名表编号和班级编号查询报名人，如果查询到报名人，继续操作5，否则，提示错误
	 * 4、看报名人和当前登陆人是否一致，如果一致，在检查当前登陆人当前班级的的管理人员，如果是，则继续操作，否则，提示错误 5、创建报名表编号，继续操作
	 * 6、根据传入的事件类型进行保存或者提交操作
	 *******************************************************************************************************************************/
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		System.out.println("running tzUpdate");
		JacksonUtil jsonUtil = new JacksonUtil();
		String successFlag = "0";
		String strMsg = "";
		String isJump = "N";
		String cType = "SAVE";

		String strSubState = "";

		/* 参与调查人员填写数据、问卷编号、实例唯一编码 */
		String strData = null, surveyID = null, unique = null, openid = null;
		int preNextPageNo = 0,curPageNo = 0;
		/* 实例编号 */
		String surveyInsId = "";

		/* 当前登陆人 */
		String strPersonId = "";
		String userId = tzLoginServiceImpl.getLoginedManagerOprid(request);

		if (userId != null && !userId.equals("TZ_GUEST") && !userId.equals("")) {
			strPersonId = userId;
		} else {
			TzSession tzSession = new TzSession(request);
			Object objOprid = tzSession.getSession(userSessionName);

			if (null != objOprid) {
				strPersonId = String.valueOf(objOprid);
			} else {
				// 如果不存在登录人设置为访客
				strPersonId = "TZ_GUEST";
			}
		}

		String strForm = null;
		// 取JSON数据的MAP
		Map<String, Object> formDataMap = null;

		// 去JSON里面的data
		Map<String, Object> dataMap = null;

		String storageType = "";
		PsTzDcInsT psTzDcInsT = null;
		String tempContextUrl = "";
		StringBuffer url = null;
		String strRet = "";

		String sql = "";
		Map<String, Object> optionValueMap = null;
		for (int i = 0; i < actData.length; i++) {

			/* 表单内容 */
			strForm = actData[i];
			/* 将字符串转换成JSON */
			jsonUtil.json2Map(strForm);
			formDataMap = jsonUtil.getMap();
			surveyID = formDataMap.get("SURVEY_WJ_ID") == null ? null : formDataMap.get("SURVEY_WJ_ID").toString();
			surveyInsId = formDataMap.get("SURVEY_INS_ID") == null ? null : formDataMap.get("SURVEY_INS_ID").toString();
			preNextPageNo = formDataMap.get("pageNo") == null ? 0 : Integer.parseInt(String.valueOf(formDataMap.get("pageNo")));

			dataMap = (Map<String, Object>) formDataMap.get("data");
			strData = jsonUtil.Map2json(dataMap);
			// strData = formDataMap.get("data") == null ? null :
			// formDataMap.get("data").toString();
			cType = formDataMap.get("TZ_APP_C_TYPE") == null ? null : formDataMap.get("TZ_APP_C_TYPE").toString();
			openid = formDataMap.get("openid") == null ? null : formDataMap.get("openid").toString();
			unique = formDataMap.get("unique") == null ? null : formDataMap.get("unique").toString();
			if(StringUtils.equals("PRE", cType)){
				curPageNo = preNextPageNo + 1;
			}else if(StringUtils.equals("NEXT", cType)){
				curPageNo = preNextPageNo - 1;
			}else{
				curPageNo = preNextPageNo;
			}
			System.out.println("********************OPENID"+openid);
			try {
				if (successFlag.equals("0")) {
					/*保存前事件*/
					Class[] paramTypes = new Class[]{String.class,String.class,Integer.class,Integer.class,String.class};
					Object[] params = new Object[]{surveyInsId,openid,curPageNo,preNextPageNo,strData};
					Map<String, Object> eventRet = this.getMethod(surveyID, "E",paramTypes,params);
					successFlag = eventRet.get("code") == null ? "0" : eventRet.get("code").toString();
					strMsg = eventRet.get("msg") == null ? "" : eventRet.get("msg").toString();
				}
				if (successFlag.equals("0")) {
					if (StringUtils.equals("PRE", cType)) {
						/*上一页事件*/
						Class[] paramTypes = new Class[]{String.class,String.class,Integer.class,Integer.class,String.class};
						Object[] params = new Object[]{surveyInsId,openid,curPageNo,preNextPageNo,strData};
						Map<String, Object> eventRet = this.getMethod(surveyID, "C",paramTypes,params);
						successFlag = eventRet.get("code") == null ? "0" : eventRet.get("code").toString();
						strMsg = eventRet.get("msg") == null ? "" : eventRet.get("msg").toString();
					}
				}
				// 定义为0
				if (successFlag.equals("0")) {

					// 先保存实例表
					psTzDcInsT = new PsTzDcInsT();
					if (surveyInsId != null && !surveyInsId.equals("")
							&& psTzDcInsTMapper.selectByPrimaryKey(Long.valueOf(surveyInsId)) != null) {
					} else {
						if (surveyInsId == null || surveyInsId.equals("")) {
							surveyInsId = String.valueOf(getSeqNum.getSeqNum("TZ_DC_INS_T", "TZ_APP_INS_ID"));
						}
						psTzDcInsT.setTzAppInsId(Long.valueOf(surveyInsId));
						psTzDcInsT.setTzDcWjId(surveyID);
						psTzDcInsT.setTzAppinsJsonStr(strData);
						psTzDcInsT.setTzDcInsFrom("1");
						// 从request中获取用户IP
						psTzDcInsT.setTzDcInsIp(request.getRemoteAddr());
						psTzDcInsT.setTzDcInsMac("1");
						psTzDcInsT.setPersonId(strPersonId);
						if (unique != null && !unique.equals("")) {
							psTzDcInsT.setTzUniqueNum(unique);
						}
						psTzDcInsT.setRowAddedOprid(strPersonId);
						psTzDcInsT.setRowAddedDttm(new Date());
						psTzDcInsT.setRowLastmantDttm(new Date());
						psTzDcInsT.setRowLastmantOprid(strPersonId);
						psTzDcInsTMapper.insert(psTzDcInsT);
						logger.info("Insert INTO PS_TZ_DC_INS_T");
						/* 将实例编号写入Cookie */
						if (surveyInsId == null || surveyInsId.equals("")) {
							Cookie cookie = new Cookie("SURVEY_INS_ID", "");

							url = request.getRequestURL();
							tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length())
									.append("/").toString();
							logger.info("Domain=" + tempContextUrl);
							cookie.setDomain(tempContextUrl);
							cookie.setPath("/");
							cookie.setMaxAge(-1);
							cookie.setValue(surveyInsId + "_" + surveyID);
							response.addCookie(cookie);
						}
					}

					// 写入在线调查答卷存储表，在线调查答卷多选信息项存储表，在线调查答卷表格选择题存储表，在线调查问卷附件存储表
					if (formDataMap != null) {
						this.delSurveyIns(surveyInsId);

						for (Object optionValue : dataMap.values()) {

							optionValueMap = (Map<String, Object>) optionValue;
							if (optionValueMap.containsKey("StorageType")
									&& optionValueMap.get("StorageType") != null) {
								storageType = optionValueMap.get("StorageType").toString();

								if (storageType.equals("D")) {
									this.saveDTypeIns(optionValueMap, String.valueOf(surveyInsId));
								} else if (storageType.equals("S") || storageType.equals("L")) {
									this.saveSLTypeIns(optionValueMap, String.valueOf(surveyInsId));
								} else if (storageType.equals("T")) {
									this.saveTableTypeIns(optionValueMap, String.valueOf(surveyInsId));
								} else if (storageType.equals("F")) {
									this.saveAttrTypeIns(optionValueMap, String.valueOf(surveyInsId), strPersonId,
											request);
								}
							}
						}
					}
				}
				if (successFlag.equals("0")) {
					/*保存后事件*/
					Class[] paramTypes = new Class[]{String.class,String.class,Integer.class,Integer.class,String.class};
					Object[] params = new Object[]{surveyInsId,openid,curPageNo,preNextPageNo,strData};
					Map<String, Object> eventRet = this.getMethod(surveyID, "F",paramTypes,params);
					successFlag = eventRet.get("code") == null ? "0" : eventRet.get("code").toString();
					strMsg = eventRet.get("msg") == null ? "" : eventRet.get("msg").toString();
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
				errMsg[0] = "1";
				errMsg[1] = e.toString();
				strMsg = e.toString();
				successFlag = "1";
			}
			
			if(successFlag.equals("0")){
				/*提交前事件*/
				if(StringUtils.equals("SUBMIT", cType)){
					Class[] paramTypes = new Class[]{String.class,String.class,Integer.class,Integer.class,String.class};
					Object[] params = new Object[]{surveyInsId,openid,curPageNo,preNextPageNo,strData};
					Map<String, Object> eventRet = this.getMethod(surveyID, "G",paramTypes,params);
					successFlag = eventRet.get("code") == null ? "0" : eventRet.get("code").toString();
					strMsg = eventRet.get("msg") == null ? "" : eventRet.get("msg").toString();
				}
			}
			
			if (successFlag.equals("0")) {
				try {
					/*--检查完成规则-- BEGIN*/
					if (cType != null && cType.equals("SUBMIT")) {
						strMsg = this.checkFiledValid(surveyID, surveyInsId);
						if (strMsg != null && !strMsg.equals("")) {
							successFlag = "1";
						} else {
							surveryRulesImpl.setComputerCookie(response, surveyID);
						}
					}
					/*--检查完成规则-- END*/

					// psTzDcInsT =
					// psTzDcInsTMapper.selectByPrimaryKey(Long.valueOf(surveyInsId));

					// if (psTzDcInsT != null) {
					if (cType != null && cType.equals("SUBMIT") && (strMsg == null || strMsg.equals(""))) {
						// psTzDcInsT.setTzDcWcSta("0");
						// psTzDcInsT.setTzAppSubSta("S");
						isJump = sqlQuery.queryForObject(
								"SELECT TZ_DC_WJ_SFTZ FROM PS_TZ_DC_WJ_DY_T WHERE TZ_DC_WJ_ID =?",
								new Object[] { surveyID }, "String");
						sql = "update PS_TZ_DC_INS_T set TZ_APPINS_JSON_STR=?,TZ_DC_INS_IP=?,";
						sql = sql
								+ "ROW_LASTMANT_DTTM=?,ROW_LASTMANT_OPRID=?,TZ_DC_WC_STA='0',TZ_APP_SUB_STA='S' where TZ_APP_INS_ID=?";

					} else {
						sql = "update PS_TZ_DC_INS_T set TZ_APPINS_JSON_STR=?,TZ_DC_INS_IP=?,";
						sql = sql + "ROW_LASTMANT_DTTM=?,ROW_LASTMANT_OPRID=? where TZ_APP_INS_ID=?";
					}
					logger.info("strData:" + strData);
					logger.info("sql:" + sql);
					// psTzDcInsT.setTzAppinsJsonStr(strData);
					// psTzDcInsT.setTzDcInsFrom("1");
					// psTzDcInsT.setTzDcInsIp(request.getRemoteAddr());
					// psTzDcInsT.setTzDcInsMac("1");
					// psTzDcInsT.setRowLastmantDttm(new Date());
					// psTzDcInsT.setRowLastmantOprid(strPersonId);
					// psTzDcInsTMapper.updateByPrimaryKey(psTzDcInsT);

					int XX = sqlQuery.update(sql,
							new Object[] { strData, request.getRemoteAddr(), new Date(), strPersonId, surveyInsId });
					logger.info("Update  PS_TZ_DC_INS_T:" + XX);
					// }

				} catch (Exception e) {
					e.printStackTrace();
					errMsg[0] = "1";
					errMsg[1] = e.toString();
					strMsg = e.toString();
					successFlag = "1";
				}
			}
			if(successFlag.equals("0")){
				/*提交后事件*/
				if(StringUtils.equals("SUBMIT", cType)){
					Class[] paramTypes = new Class[]{String.class,String.class,Integer.class,Integer.class,String.class};
					Object[] params = new Object[]{surveyInsId,openid,curPageNo,preNextPageNo,strData};
					Map<String, Object> eventRet = this.getMethod(surveyID, "H",paramTypes,params);
					successFlag = eventRet.get("code") == null ? "0" : eventRet.get("code").toString();
					strMsg = eventRet.get("msg") == null ? "" : eventRet.get("msg").toString();
				}
			}
			if (successFlag.equals("0")) {
				if (StringUtils.equals("NEXT", cType)) {
					/*下一页事件*/
					Class[] paramTypes = new Class[]{String.class,String.class,Integer.class,Integer.class,String.class};
					Object[] params = new Object[]{surveyInsId,openid,curPageNo,preNextPageNo,strData};
					Map<String, Object> eventRet = this.getMethod(surveyID, "D",paramTypes,params);
					successFlag = eventRet.get("code") == null ? "0" : eventRet.get("code").toString();
					strMsg = eventRet.get("msg") == null ? "" : eventRet.get("msg").toString();
				}
			}
			/* 当前问卷的提交状态 */
			strSubState = "A";
			String readonly = "N";
			strSubState = sqlQuery.queryForObject(
					"SELECT TZ_APP_SUB_STA FROM PS_TZ_DC_INS_T WHERE TZ_APP_INS_ID = ?", new Object[] { surveyInsId },
					"String");
			PsTzDcWjDyTWithBLOBs psTzDcWjDyTWithBLOBs = psTzDcWjDyTMapper.selectByPrimaryKey(surveyID);
			if (psTzDcWjDyTWithBLOBs == null) {
				String strDtgz = psTzDcWjDyTWithBLOBs.getTzDcWjDtgz();

				if (StringUtils.equals("S", strSubState)) {
					if (StringUtils.equals("2", strDtgz)) {
						readonly = "N";
					} else {
						readonly = "Y";
					}
				}
			}

			// {"code":"%BIND(:1)","msg":"%bind(:2)","insid":"%bind(:3)","subState":"%bind(:4)","jump":"%bind(:5)"}
			strRet = "{\"code\":\"" + successFlag + "\",\"msg\":\"" + strMsg + "\",\"insid\":\"" + surveyInsId
					+ "\",\"subState\":\"" + strSubState + "\",\"readonly\":\"" + readonly + "\",\"jump\":\"" + isJump
					+ "\"}";
		}
		return strRet;
	}

	/**
	 * 问卷填写页面
	 * 
	 * @param strParams
	 * @return
	 */
	@Override
	public String tzGetHtmlContent(String strParams) {
		logger.info("问卷填写页面");

		// 客户端是否移动设备访问
		boolean isMobile = CommonUtils.isMobile(request);

		String successFlag = "0";
		String strHtml = null;
		String strMsg = "";

		// REM 实例唯一随机数;
		String uniqueNum = "";

		String fromIntro = "";

		String strTitle = null, strModeDesc = null;
		String strReturn = null;
		JacksonUtil jsonUtil = new JacksonUtil();

		String strPersonId = tzLoginServiceImpl.getLoginedManagerOprid(request);

		// 如果不存在登录人设置为访客
		if (strPersonId == null || strPersonId.equals("")) {
			/* 是不是测测上清华相关的功能 */
			TzSession tzSession = new TzSession(request);
			Object objOprid = tzSession.getSession(userSessionName);

			if (null != objOprid) {
				strPersonId = String.valueOf(objOprid);
			} else {
				strPersonId = "TZ_GUEST";
			}
		}

		jsonUtil.json2Map(strParams);

		/* 问卷编号 、实例编号、控制逻辑 */
		String surveyID = "", surveyInsId = "", surveyLogic = "";

		/* 调查问卷应用编号 */
		String classId = request.getParameter("classid");
		/* 是否存在合法实例编号 */
		boolean isHasIns = false;
		/* 从参数中获取问卷编号、实例编号 */
		if (classId != null && !classId.equals("")) {
			surveyID = request.getParameter("SURVEY_WJ_ID");
			surveyInsId = request.getParameter("SURVEY_INS_ID");
			fromIntro = request.getParameter("F");
			uniqueNum = request.getParameter("unique");

		} else {
			// System.out.println("going here?");
			if (jsonUtil.containsKey("SURVEY_WJ_ID")) {
				surveyID = jsonUtil.getString("SURVEY_WJ_ID");
			}
			if (jsonUtil.containsKey("SURVEY_INS_ID")) {
				surveyInsId = jsonUtil.getString("SURVEY_INS_ID");
			}
			if (jsonUtil.containsKey("F")) {
				fromIntro = jsonUtil.getString("F");
			}
			if (jsonUtil.containsKey("unique")) {
				uniqueNum = jsonUtil.getString("unique");
			}
		}

		logger.info("surveyID=" + surveyID);
		logger.info("surveyInsId=" + surveyInsId);
		logger.info("fromIntro=" + fromIntro);
		logger.info("uniqueNum=" + uniqueNum);
		if (StringUtils.isNotBlank(surveyInsId) && Integer.parseInt(surveyInsId) > 0) {
			isHasIns = true;
		}

		/* 1.验证实例编号是否为null */
		logger.info("--- 1.验证问卷编号是否为null ---");
		if (StringUtils.isBlank(surveyID)) {
			successFlag = "1";
			strMsg = "The Survey Id is empty!";
		}

		/* 2.验证问卷编号是否合法 */
		logger.info("--- 2.验证问卷是否存在 ---");
		PsTzDcWjDyTWithBLOBs psTzDcWjDyTWithBLOBs = new PsTzDcWjDyTWithBLOBs();
		if (successFlag.equals("0")) {
			psTzDcWjDyTWithBLOBs = psTzDcWjDyTMapper.selectByPrimaryKey(surveyID);
			if (psTzDcWjDyTWithBLOBs == null) {
				successFlag = "1";
				strMsg = "The Survey Id is not valid!";
			}
		}

		/* 卷头、卷尾、语言 */
		String header = "", footer = "", language = "";
		if (successFlag.equals("0")) {
			header = psTzDcWjDyTWithBLOBs.getTzDcJtnr();
			footer = psTzDcWjDyTWithBLOBs.getTzDcJwnr();

			language = psTzDcWjDyTWithBLOBs.getTzAppTplLan();
			if (StringUtils.isBlank(language)) {
				language = "ZHS";
			}
		}

		/* 3.根据登录状态判断是否可以参与调查 */
		logger.info("---2.根据登录状态判断是否可以参与调查 ---");
		boolean boolRtn = false;
		if (successFlag.equals("0")) {
			boolRtn = surveryRulesImpl.checkCanAnswer(psTzDcWjDyTWithBLOBs, language, strPersonId);
			if (!boolRtn) {
				successFlag = "1";
				strMsg = surveryRulesImpl.msg;
			}
		}
		
		/* 3.2 根据听众列表判断是否可以参与调查  卢艳添加，2017-12-1*/
		logger.info("---3.根据听众列表判断是否可以参与调查 ---");
		boolean boolAud = false;
		if (successFlag.equals("0")) {
			//查询是否可匿名访问
			String dcwjDlzt = psTzDcWjDyTWithBLOBs.getTzDcWjDlzt();
			if(!"Y".equals(dcwjDlzt)) {
				//不可匿名访问
			
				//查询是否设置了听众
				String haveAudFlag = sqlQuery.queryForObject("SELECT 'Y' FROM PS_TZ_SURVEY_AUD_T WHERE TZ_DC_WJ_ID=? LIMIT 0,1", new Object[]{surveyID},"String");
				if("Y".equals(haveAudFlag)) {
					//查询当前登录人是否在听众列表中
					String audCyFlag = sqlQuery.queryForObject("SELECT 'Y' FROM PS_TZ_SURVEY_AUD_T A,PS_TZ_AUD_LIST_T B WHERE A.TZ_AUD_ID=B.TZ_AUD_ID AND B.TZ_DXZT='A' AND B.OPRID=? AND A.TZ_DC_WJ_ID=? LIMIT 0,1", new Object[]{strPersonId,surveyID},"String");
					boolAud = surveryRulesImpl.checkSurveryAudience(psTzDcWjDyTWithBLOBs, language, audCyFlag);
					if (!boolAud) {
						successFlag = "1";
						strMsg = surveryRulesImpl.msg;
					}   
				}
			}
		}

		/* 4.实例编号、实例唯一随机数是否为null */
		logger.info("---4.实例编号、实例唯一随机数是否为null ---");

		if (successFlag.equals("0")) {
			if (StringUtils.isBlank(surveyInsId) && StringUtils.isBlank(uniqueNum)) {
				String isTrue = "N";
				isTrue = sqlQuery.queryForObject(
						"SELECT 'Y' FROM PS_TZ_DC_WJ_DY_T WHERE TZ_DC_WJ_ID = ? AND TZ_DC_WJ_DLZT = 'N' AND TZ_DC_WJ_IPGZ = '3'",
						new Object[] { surveyID }, "String");
				if (StringUtils.equals("Y", isTrue)) {
					Map<String, Object> map = sqlQuery.queryForMap(
							"SELECT TZ_APP_INS_ID,TZ_UNIQUE_NUM FROM PS_TZ_DC_INS_T WHERE TZ_DC_WJ_ID = ? AND ROW_ADDED_OPRID = ? ORDER BY ROW_LASTMANT_DTTM DESC limit 0,1",
							new Object[] { surveyID, strPersonId });
					if (map != null) {
						surveyInsId = map.get("TZ_APP_INS_ID") == null ? "" : String.valueOf(map.get("TZ_APP_INS_ID"));
						uniqueNum = map.get("TZ_UNIQUE_NUM") == null ? "" : String.valueOf(map.get("TZ_UNIQUE_NUM"));
					}
				}
				if (StringUtils.isBlank(surveyInsId) || Integer.parseInt(surveyInsId) < 1) {
					uniqueNum = String.valueOf(((int) (Math.random() * 100)) * 951)
							+ String.valueOf(((int) (Math.random() * 100)) * 233)
							+ String.valueOf(((int) (Math.random() * 100)) * 5713)
							+ String.valueOf(((int) (Math.random() * 100)) * 35771) + "000000000000000";
					uniqueNum = uniqueNum.substring(0, 15);
				}

				String url = request.getRequestURL() + "?" + request.getQueryString();
				if (StringUtils.isNotBlank(surveyInsId) && Integer.parseInt(surveyInsId) > 0) {
					url = url + "&SURVEY_INS_ID=" + surveyInsId;
				}
				url = url + "&unique=" + uniqueNum;
				logger.info("   --- 请求URL     " + url);

			/*	try {
					response.sendRedirect(url);
					return null;
				} catch (IOException e) {
					e.printStackTrace();
				}*/
			}
		}

		/* 统一接口URL */
		String tzGeneralURL = request.getContextPath() + "/dispatcher";
		String path = request.getContextPath();

		/* 5.问卷状态检查 */
		logger.info("5.问卷状态检查");
		if (successFlag.equals("0")) {
			boolRtn = surveryRulesImpl.checkSurveryStatus(psTzDcWjDyTWithBLOBs, language);
			if (!boolRtn && !isHasIns) {
				successFlag = "1";
				strMsg = surveryRulesImpl.msg;
			}
		}

		/* 6.开始结束时间、开始结束日期检查 */
		logger.info("6.开始结束时间、开始结束日期检查");
		if (successFlag.equals("0")) {
			boolRtn = surveryRulesImpl.checkSurveryDate(psTzDcWjDyTWithBLOBs, language);
			if (!boolRtn && !isHasIns) {
				successFlag = "1";
				strMsg = surveryRulesImpl.msg;
			}
		}

		// TODO 不允许匿名调查,检查听众 JAVA版本没有听众这个
		/* 7.根据唯一序列号获取实例编号 */
		if (successFlag.equals("0")) {
			if (StringUtils.isBlank(surveyInsId)) {
				surveyInsId = sqlQuery.queryForObject(
						"SELECT TZ_APP_INS_ID FROM PS_TZ_DC_INS_T WHERE TZ_UNIQUE_NUM = ?", new Object[] { uniqueNum },
						"String");
			}
		}

		/* 8.调查数据采集规则 */
		logger.info("7.调查数据采集规则");
		if (successFlag.equals("0")) {
			if (StringUtils.isBlank(surveyInsId) || Integer.parseInt(surveyInsId) < 1) {

				boolRtn = surveryRulesImpl.checkSingleAnswerRules(psTzDcWjDyTWithBLOBs, language, request, strPersonId);
				if (!boolRtn) {
					successFlag = "1";
					strMsg = surveryRulesImpl.msg;
				}
			}
		}

		/* 是否可以填写调查问卷 */
		if (successFlag.equals("0")) {
			/* 模式信息 */
			String survey_mode = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET",
					"SURVEY_MODE", language, "调查模式", "Survey");
			String survey_mode_desc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET",
					"SURVEY_MODE_DESC", language, "您提交的数据我们将会保存", "Save");
			String begin = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "BEGIN", language,
					"开始", "GO");
			String submit = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "SUBMIT",
					language, "提交问卷", "Submit");
			String strPre = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "SURVEY_PRE",
					language, "上一页", "Pre");
			String strNext = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "SURVEY_NEXT",
					language, "下一页", "Next");
			String strSurveySubmit = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET",
					"SURVEY_SUBMIT", language, "问卷已提交！", "The questionnaire has been submitted! ");

			try {
				if (isMobile) {
					strModeDesc = tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_SURVEY_MODE_M_HTML",
							survey_mode, survey_mode_desc);
				} else {
					strModeDesc = tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_SURVEY_MODE_HTML",
							survey_mode, survey_mode_desc, path);
				}
				logger.info("GO TZ_SURVEY_MODE_M_HTML OR TZ_SURVEY_MODE_HTML");
			} catch (TzSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/* 是否需要密码 */
			logger.info("是否需要密码 ");
			boolRtn = surveryRulesImpl.checkSurveryNeedsPwd(psTzDcWjDyTWithBLOBs, language);
			String isPassAuth = "N";
			if (boolRtn) {
				/* 需要密码 */
				isPassAuth = "Y";
			}

			// 是否启用前导页
			String isEnable = psTzDcWjDyTWithBLOBs.getTzDcWjQyqd();

			// 前导页内容
			String strIntro = psTzDcWjDyTWithBLOBs.getTzDcWjQdnr();

			// 问卷标题
			strTitle = psTzDcWjDyTWithBLOBs.getTzDcWjbt();
			try {
				if (isEnable != null && isEnable.equals("Y") && (fromIntro == null || fromIntro.equals(""))) {
					strHtml = "";
					if (isMobile) {
						strHtml = tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_SURVEY_INTRO_M_HTML",
								header, strIntro, footer, tzGeneralURL, surveyID, surveyInsId, strTitle, strModeDesc,
								begin, isPassAuth, surveyID, path);
					} else {
						strHtml = tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_SURVEY_INTRO_HTML", header,
								strIntro, footer, tzGeneralURL, surveyID, surveyInsId, strTitle, strModeDesc, begin,
								isPassAuth, surveyID, path);
					}

					logger.info("RETURN TZ_SURVEY_INTRO_HTML OR TZ_SURVEY_INTRO_M_HTML");
					logger.info("surveyID:" + surveyID);
					logger.info("surveyInsId:" + surveyInsId);
					logger.info("uniqueNum:" + uniqueNum);
					return strHtml;
				}
			} catch (TzSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/* 文件报文数据、问卷实例报文数据 */
			String surveyData = psTzDcWjDyTWithBLOBs.getTzApptplJsonStr();
			// logger.info("surveyData:" + surveyData);
			surveyData = surveyData.replace("\\", "\\\\");
			surveyData = surveyData.replaceAll("\\$", "~");
			Pattern CRLF = Pattern.compile("(\r\n|\r|\n|\n\r)");
			Matcher mc = CRLF.matcher(surveyData);
			if (mc.find()) {
				surveyData = mc.replaceAll("\\\\n");
			}
			surveyData = surveyData.replace(" ", "");
			
			String surveyInsData = null;
			try {
				if (StringUtils.isNotBlank(surveyInsId) && Integer.parseInt(surveyInsId) > 0) {
					surveyInsData = sqlQuery.queryForObject(
							"SELECT TZ_APPINS_JSON_STR FROM PS_TZ_DC_INS_T WHERE TZ_DC_WJ_ID = ? AND TZ_APP_INS_ID = ?",
							new Object[] { surveyID, surveyInsId }, "String");
					if (surveyInsData == null || surveyInsData.equals("")) {
						surveyInsData = "''";
					}
				} else {
					surveyInsData = "''";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// logger.info("surveyInsData:" + surveyInsData);
			surveyInsData = surveyInsData.replace("\\", "\\\\");
			surveyInsData = surveyInsData.replaceAll("\\$", "~");
			mc = CRLF.matcher(surveyInsData);
			if (mc.find()) {
				surveyInsData = mc.replaceAll("\\\\n");
			}
			surveyInsData = surveyInsData.replace(" ", "");

			int numMaxPage = sqlQuery.queryForObject(
					"SELECT MAX(TZ_PAGE_NO) + 1 FROM PS_TZ_DCWJ_XXXPZ_T WHERE TZ_DC_WJ_ID = ?",
					new Object[] { surveyID }, "int");

			logger.info("numMaxPage:" + numMaxPage);
			/* 控件信息 */
			ArrayList<Map<String, Object>> comDfn = questionnaireEditorEngineImpl.getComDfn(surveyID);
			String strComRegInfo = jsonUtil.List2json(comDfn);

			// logger.info("strComRegInfo:" + strComRegInfo);
			/* 控制逻辑 */
			try {
				if (surveyInsId != null && !surveyInsId.equals("") && Integer.parseInt(surveyInsId) > 0) {
					surveyLogic = logicControlImpl.getSurveyLogicJson(surveyInsId);
				} else {
					surveyLogic = logicControlImpl.getSurveyLogicJson2(surveyID);
				}
				if (surveyLogic == null || surveyLogic.equals("")) {
					surveyLogic = "''";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// logger.info("surveyLogic:" + surveyLogic);
			/* 调查问卷消息集合 */
			String str_MsgSet = gdObjectServiceImpl.getMessageSetByLanguageCd(request, response, "TZGD_SURVEY_MSGSET",
					language);

			/* 解析JSON字符串 */
			jsonUtil.json2Map(str_MsgSet);
			if (jsonUtil.containsKey(language)) {
				Map<String, Object> msgLang = jsonUtil.getMap(language);
				str_MsgSet = jsonUtil.Map2json(msgLang);
			}
			// logger.info("str_MsgSet:" + str_MsgSet);

			/* 当前问卷的提交状态 */
			String strSubState = "A";
			String readonly = "N";
			strSubState = sqlQuery.queryForObject(
					"SELECT TZ_APP_SUB_STA FROM PS_TZ_DC_INS_T WHERE TZ_APP_INS_ID = ?", new Object[] { surveyInsId },
					"String");
			String strDtgz = psTzDcWjDyTWithBLOBs.getTzDcWjDtgz();
			if (StringUtils.equals("S", strSubState)) {
				if (StringUtils.equals("2", strDtgz)) {
					boolean boolStatus = surveryRulesImpl.checkSurveryStatus(psTzDcWjDyTWithBLOBs, language);
					boolean boolDate = surveryRulesImpl.checkSurveryDate(psTzDcWjDyTWithBLOBs, language);
					if (boolStatus && boolDate) {
						readonly = "N";
					} else {
						readonly = "Y";
					}
				} else {
					readonly = "Y";
				}
			}
			try {
				/* 是否为测试问卷 */
				String isTestSurvey = sqlQuery.queryForObject(
						"SELECT 'Y' FROM PS_TZ_CSWJ_TBL WHERE TZ_DC_WJ_ID = ? limit 0,1", new Object[] { surveyID },
						"String");
				if (StringUtils.equals("Y", isTestSurvey)) {
					submit = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "QUIT", language,
							"退出/测试其他项目", "Quit");
				}
				/*初始化页面事件 begin*/
				Class[] paramTypes = new Class[]{HttpServletRequest.class,HttpServletResponse.class,String.class};
				Object[] params = new Object[]{request,response,surveyID};
				Map<String, Object> eventRet = this.getMethod(surveyID, "A",paramTypes,params);
				String code =  eventRet.get("code") == null ? "0" : eventRet.get("code").toString();
				String initInput = "";
				if(StringUtils.equals("0", code)){
					initInput = eventRet.get("msg") == null ? "" : eventRet.get("msg").toString();
				}

				/*初始化页面事件 begin*/
				
				if (isMobile) {
					strReturn = tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_SURVEY_PAGE_M_HTML", header,
							footer, tzGeneralURL, strComRegInfo, surveyID, surveyInsId, surveyData, surveyInsData,
							String.valueOf(numMaxPage), isPassAuth, surveyLogic, str_MsgSet, strTitle, strModeDesc,
							submit, language, strPre, strNext, strSubState, uniqueNum, path, readonly, strSurveySubmit,initInput);
				} else {
					strReturn = tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_SURVEY_PAGE_HTML", header,
							footer, tzGeneralURL, strComRegInfo, surveyID, surveyInsId, surveyData, surveyInsData,
							String.valueOf(numMaxPage), isPassAuth, surveyLogic, str_MsgSet, strTitle, strModeDesc,
							submit, language, strPre, strNext, strSubState, uniqueNum, path, readonly, strSurveySubmit,initInput);
				}

				strReturn = strReturn.replaceAll("\\~", "\\$");
				logger.info("RETURN TZ_SURVEY_PAGE_HTML OR TZ_SURVEY_PAGE_M_HTML");
				logger.info("surveyID:" + surveyID);
				logger.info("surveyInsId:" + surveyInsId);
				logger.info("uniqueNum:" + uniqueNum);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			/* 不能填写问卷 */
			try {
				if (isMobile) {
					strReturn = tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_SURVEY_ERROR_M_HTML", header,
							strMsg, footer, path);
				} else {
					strReturn = tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_SURVEY_ERROR_HTML", header,
							strMsg, footer, path);
				}
				logger.info("RETURN TZ_SURVEY_ERROR_M_HTML OR TZ_SURVEY_ERROR_HTML");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return strReturn;
	}

	@Override
	public String tzGetJsonData(String strParams) {
		System.out.println("running tzGetJsonData");

		String result = "{}";
		String successFlag = "0";
		String strMsg = "";
		JacksonUtil jsonUtil = new JacksonUtil();

		jsonUtil.json2Map(strParams);
		// 取JSON数据的MAP
		Map<String, Object> formDataMap = jsonUtil.getMap();

		if (formDataMap.containsKey("EType") && formDataMap.get("EType") != null) {
			String strEType = formDataMap.get("EType").toString();

			String surveyID = null, surveyInsId = null, rtnMsg = null;

			if (formDataMap.containsKey("SURVEY_WJ_ID") && formDataMap.get("SURVEY_WJ_ID") != null) {
				surveyID = formDataMap.get("SURVEY_WJ_ID").toString();
			}
			if (formDataMap.containsKey("SURVEY_INS_ID") && formDataMap.get("SURVEY_INS_ID") != null) {
				surveyInsId = formDataMap.get("SURVEY_INS_ID").toString();
			}

			// 获取问卷信息
			PsTzDcWjDyTWithBLOBs psTzDcWjDyTWithBLOBs = null;
			if (!StringUtils.isEmpty(surveyID)) {
				psTzDcWjDyTWithBLOBs = psTzDcWjDyTMapper.selectByPrimaryKey(surveyID);
			}

			/* 语言 */
			String language = null;
			if (psTzDcWjDyTWithBLOBs != null) {
				language = psTzDcWjDyTWithBLOBs.getTzAppTplLan();
				if (language == null || language.equals("")) {
					language = "ZHS";
				}
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
				boolRtn = surveryRulesImpl.checkSurveryPwd(psTzDcWjDyTWithBLOBs, language, password);
				if (!boolRtn) {
					successFlag = "1";
					strMsg = surveryRulesImpl.msg;
				} else {
					Cookie cookie = new Cookie("SURVEY_WJ_IS_PASSWORD", "");

					logger.info("Domain=" + tempContextUrl);
					cookie.setDomain(tempContextUrl);
					cookie.setPath("/");
					cookie.setMaxAge(-1);
					cookie.setValue(surveyInsId + "_" + surveyID + "_Y");
					response.addCookie(cookie);
				}
				return "{\"code\": \"" + successFlag + "\",\"msg\": \"" + strMsg + "\"}";
			}
			/* 问卷密码是否正确 END */

			/* 问题是否可编辑 BEGIN */
			if (strEType.equals("ISMODIFY")) {
				/* 答题规则、问卷实例状态 */
				String strDtRule = null, strSubState = null;
				strDtRule = sqlQuery.queryForObject(
						"SELECT TZ_DC_WJ_DTGZ FROM PS_TZ_DC_WJ_DY_T WHERE TZ_DC_WJ_ID = ?", new Object[] { surveyID },
						"String");

				strSubState = sqlQuery.queryForObject(
						"SELECT TZ_APP_SUB_STA FROM PS_TZ_DC_INS_T WHERE TZ_APP_INS_ID = ?",
						new Object[] { surveyInsId }, "String");
				if (strSubState.equals("S")) {
					// 已提交
					if (!strDtRule.equals("2")) {
						successFlag = "N";
					} else {
						successFlag = "Y";
					}
				} else {
					// 新建
					if (strDtRule.equals("1")) {
						successFlag = "N";
					} else {
						successFlag = "Y";
					}
				}
				return "{\"code\": \"" + successFlag + "\",\"msg\": \"" + strMsg + "\"}";
			}
			/* 问题是否可编辑 END */

			/* 问卷控制逻辑 BEGIN */
			if (strEType.equals("DISPLAY")) {
				String pageno = null;
				if (formDataMap.containsKey("PAGE_NO") && formDataMap.get("PAGE_NO") != null) {
					pageno = formDataMap.get("PAGE_NO").toString();
				}
				if (!StringUtils.isEmpty(surveyInsId) && StringUtils.isEmpty(pageno)) {
					result = logicControlImpl.getCurrentPageDisplay(surveyInsId, Integer.parseInt(pageno) - 1, "N");
				} else {
					successFlag = "1";
					strMsg = rtnMsg;
				}
				return "{\"code\": \"" + successFlag + "\",\"msg\": \"" + strMsg + ")\",\"value\":" + result + "}";
			}
			/* 问卷控制逻辑 END */
			return null;

		} else {
			return result;
		}
	}

	/**
	 * 删除 问卷实例的存储数据
	 * 
	 * @param surveyInsId
	 * @return
	 */
	private boolean delSurveyIns(String surveyInsId) {
		System.out.println("running delSurveyIns");

		try {
			if (surveyInsId != null && !surveyInsId.equals("")) {
				/* 在线调查答卷存储表 */
				sqlQuery.update("DELETE FROM PS_TZ_DC_CC_T WHERE TZ_APP_INS_ID=?", new Object[] { surveyInsId });
				/* 在线调查答卷多选信息项存储表 */
				sqlQuery.update("DELETE FROM PS_TZ_DC_DHCC_T WHERE TZ_APP_INS_ID=?", new Object[] { surveyInsId });
				/* 在线调查答卷表格选择题存储表 */
				sqlQuery.update("DELETE FROM PS_TZ_DCDJ_BGT_T WHERE TZ_APP_INS_ID=?", new Object[] { surveyInsId });
				/* 在线调查答卷附件存储表 */
				sqlQuery.update("DELETE FROM PS_TZ_DC_WJATT_T WHERE TZ_APP_INS_ID=?", new Object[] { surveyInsId });
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 答卷存储表(短文本、长文本)
	 * 
	 * @param jsonXxxMap
	 * @param surveyInsId
	 */
	private void saveSLTypeIns(Map<String, Object> jsonXxxMap, String surveyInsId) {
		System.out.println("running saveSLTypeIns");

		PsTzDcCcT psTzDcCcT = new PsTzDcCcT();
		psTzDcCcT.setTzAppInsId(Long.valueOf(surveyInsId));
		psTzDcCcT.setTzXxxBh(jsonXxxMap.get("itemId") == null ? null : jsonXxxMap.get("itemId").toString());
		String StorageType = null;
		String value = null;
		if (jsonXxxMap.containsKey("StorageType") && jsonXxxMap.get("StorageType") != null) {
			StorageType = jsonXxxMap.get("StorageType") == null ? null : jsonXxxMap.get("StorageType").toString();
			value = jsonXxxMap.get("value") == null ? null : jsonXxxMap.get("value").toString();
			if (StorageType != null && StorageType.equals("L")) {
				psTzDcCcT.setTzAppLText(value);
			} else {
				psTzDcCcT.setTzAppSText(value);
				/* 是否有文字说明 */
				if (jsonXxxMap.containsKey("wzsm")) {
					psTzDcCcT.setTzAppLText(jsonXxxMap.get("wzsm") == null ? null : jsonXxxMap.get("wzsm").toString());
				}
			}
		}
		psTzDcCcTMapper.insert(psTzDcCcT);
	}

	/**
	 * 单选框、复选框 保存 JSON格式如下所示 "option": {"A14806755501221": {"code": 1,"txt":
	 * "选项1","orderby": 1,"defaultval": "N","other": "N","weight":
	 * 0,"othervalue": "","checked": "N"}}
	 * 
	 * @param jsonXxxMap
	 * @param surveyInsId
	 */
	private void saveDTypeIns(Map<String, Object> jsonXxxMap, String surveyInsId) {
		System.out.println("running saveDTypeIns");

		PsTzDcDhccT psTzDcDhccT = null;

		if (jsonXxxMap.containsKey("option") && jsonXxxMap.get("option") != null) {
			Map<String, Object> dataMap = (Map<String, Object>) jsonXxxMap.get("option");
			Map<String, Object> optionValueMap = null;
			for (Object optionValue : dataMap.values()) {
				optionValueMap = (Map<String, Object>) optionValue;

				if (optionValueMap.containsKey("checked") && optionValueMap.get("checked") != null
						&& optionValueMap.get("checked").toString().equals("Y")) {
					psTzDcDhccT = new PsTzDcDhccT();
					psTzDcDhccT.setTzAppInsId(Long.valueOf(surveyInsId));
					psTzDcDhccT.setTzXxxBh(jsonXxxMap.get("itemId").toString());
					if (optionValueMap.containsKey("code") && optionValueMap.get("code") != null) {
						psTzDcDhccT.setTzXxxkxzMc(optionValueMap.get("code").toString());
					}

					if (optionValueMap.containsKey("txt") && optionValueMap.get("txt") != null) {
						psTzDcDhccT.setTzAppSText(optionValueMap.get("txt").toString());
					}

					if (optionValueMap.containsKey("othervalue") && optionValueMap.get("othervalue") != null) {
						psTzDcDhccT.setTzKxxQtz(optionValueMap.get("othervalue").toString());
					}
					psTzDcDhccT.setTzIsChecked(optionValueMap.get("checked").toString());
					psTzDcDhccTMapper.insert(psTzDcDhccT);
				}
			}
		}
	}

	/**
	 * 类型为表格类型的问题保存 JSON格式如下所示 "child": {"S14806755501221": {"sqCode":
	 * 3,"question": "子问题3","shortDesc": "简称3","orderby": 3, "weight":
	 * 0,"value": [2,1]]}}
	 * 
	 * @param jsonXxxMap
	 * @param surveyInsId
	 */
	private void saveTableTypeIns(Map<String, Object> jsonXxxMap, String surveyInsId) {
		System.out.println("running saveTableTypeIns");

		PsTzDcdjBgtT psTzDcdjBgtT = null;

		if (jsonXxxMap.containsKey("child") && jsonXxxMap.get("child") != null) {
			Map<String, Object> dataMap = (Map<String, Object>) jsonXxxMap.get("child");
			Map<String, Object> optionValueMap = null;
			List<Integer> jsonArray = null;
			for (Object optionValue : dataMap.values()) {
				optionValueMap = (Map<String, Object>) optionValue;
				if (optionValueMap.containsKey("value") && optionValueMap.get("value") != null) {
					jsonArray = (List<Integer>) optionValueMap.get("value");
					for (int i = 0; i < jsonArray.size(); i++) {
						psTzDcdjBgtT = new PsTzDcdjBgtT();
						psTzDcdjBgtT.setTzAppInsId(Long.valueOf(surveyInsId));
						psTzDcdjBgtT.setTzXxxBh(jsonXxxMap.get("itemId").toString());

						if (optionValueMap.containsKey("sqCode") && optionValueMap.get("sqCode") != null) {
							psTzDcdjBgtT.setTzXxxkxzWtmc(optionValueMap.get("sqCode").toString());
						}
						psTzDcdjBgtT.setTzXxxkxzXxmc(String.valueOf(jsonArray.get(i)));
						psTzDcdjBgtT.setTzAppSText("Y");

						psTzDcdjBgtTMapper.insert(psTzDcdjBgtT);
					}
				}
			}
		}
	}

	/**
	 * 类型为附件类型的问题保存
	 * 
	 * @param jsonXxxMap
	 * @param surveyInsId
	 */
	private void saveAttrTypeIns(Map<String, Object> jsonXxxMap, String surveyInsId, String Oprid,
			HttpServletRequest request) {

		System.out.println("running saveAttrTypeIns");

		PsTzDcWjattT psTzDcWjattT = null;
		PsTzDcWjattchT psTzDcWjattchT = null;

		if (jsonXxxMap.containsKey("children") && jsonXxxMap.get("children") != null) {
			List<Map<String, Object>> jsonArray = (List<Map<String, Object>>) jsonXxxMap.get("children");

			Map<String, Object> optionValueMap = null;
			java.util.Date now = new java.util.Date();
			String strSysFileName = null, strUserFileName = null, strOrderBy = null, strPath = null, strItemId = null;

			String is_Exists = null;
			for (int i = 0; i < jsonArray.size(); i++) {
				optionValueMap = (Map<String, Object>) jsonArray.get(i);

				if (optionValueMap.containsKey("itemId") && optionValueMap.get("itemId") != null) {
					strItemId = optionValueMap.get("itemId").toString();
				}

				if (optionValueMap.containsKey("sysFileName") && optionValueMap.get("sysFileName") != null) {
					strSysFileName = optionValueMap.get("sysFileName").toString();
				}

				if (optionValueMap.containsKey("fileName") && optionValueMap.get("fileName") != null) {
					strUserFileName = optionValueMap.get("fileName").toString();
				}

				if (optionValueMap.containsKey("orderby") && optionValueMap.get("orderby") != null) {
					strOrderBy = optionValueMap.get("orderby").toString();
				}

				if (optionValueMap.containsKey("accessPath") && optionValueMap.get("accessPath") != null) {
					strPath = optionValueMap.get("accessPath").toString();
				}
				logger.info("path:" + strPath);
				logger.info("orderby:" + strOrderBy);
				logger.info("fileName:" + strUserFileName);
				logger.info("sysFileName:" + strSysFileName);
				logger.info("itemId:" + strItemId);

				try {
					if (!StringUtils.isEmpty(strSysFileName) && !StringUtils.isEmpty(strUserFileName)
							&& !StringUtils.isEmpty(strOrderBy) && !StringUtils.isEmpty(strPath)) {
						psTzDcWjattT = new PsTzDcWjattT();
						psTzDcWjattT.setTzAppInsId(surveyInsId);
						psTzDcWjattT.setTzXxxBh(jsonXxxMap.get("itemId").toString());
						psTzDcWjattT.setTzIndex(Integer.parseInt(strOrderBy));
						psTzDcWjattT.setAttachsysfilename(strSysFileName);
						psTzDcWjattT.setAttachuserfile(strUserFileName);
						psTzDcWjattT.setRowAddedDttm(now);
						psTzDcWjattT.setRowAddedOprid(Oprid);
						psTzDcWjattT.setRowLastmantDttm(now);
						psTzDcWjattT.setRowLastmantOprid(Oprid);

						psTzDcWjattTMapper.insert(psTzDcWjattT);

						/* 先查看数据库是否存在 */
						is_Exists = sqlQuery.queryForObject(
								"SELECT 'Y' FROM PS_TZ_DC_WJATTCH_T WHERE TZ_ATTACHSYSFILENA = ?",
								new Object[] { strSysFileName }, "String");
						if (StringUtils.isEmpty(is_Exists)) {
							/* 插入数据库 */
							psTzDcWjattchT = new PsTzDcWjattchT();

							psTzDcWjattchT.setTzAttachsysfilena(strSysFileName);
							psTzDcWjattchT.setTzAttachfileName(strUserFileName);
							// 服务器存储路径
							psTzDcWjattchT.setTzAttPUrl(request.getServletContext().getRealPath(strPath));
							// 访问路径
							psTzDcWjattchT.setTzAttAUrl(strPath);
							psTzDcWjattchTMapper.insert(psTzDcWjattchT);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	}

	private String checkFiledValid(String surveyID, String surveyInsId) {

		System.out.println("running checkFiledValid");

		String str_msg = "";

		/* 信息项编号 */
		String str_XXX_BH = "";

		/* 信息项名称 */
		String str_XXX_MC = "";

		/* 控件类名称 */
		String str_COM_MC = "";

		/* 分页号 */
		String num_PAGE_NO = "";

		/* 信息项日期格式 */
		String str_XXX_RQGS = "";

		/* 信息项日期年份最小值 */
		String str_XXX_NFMIN = "";

		/* 信息项日期年份最大值 */
		String str_XXX_NFMAX = "";

		/* 信息项多选最少选择数量 */
		String str_XXX_ZSXZGS = "";

		/* 信息项多选最多选择数量 */
		String str_XXX_ZDXZGS = "";

		/* 信息项文件允许上传类型 */
		String str_XXX_YXSCLX = "";

		/* 信息项文件允许上传大小 */
		String str_XXX_YXSCDX = "";

		/* 信息项是否必填 */
		String str_XXX_BT_BZ = "";

		/* 信息项是否启用字数范围 */
		String str_XXX_CHAR_BZ = "";

		/* 信息项字数最小长度 */
		String num_XXX_MINLEN = "";

		/* 信息项字数最大长度 */
		String num_XXX_MAXLEN = "";

		/* 信息项是否启用数字范围 */
		String str_XXX_NUM_BZ = "";

		/* 信息项字数最小长度 */
		String num_XXX_MIN = "";

		/* 信息项字数最大长度 */
		String num_XXX_MAX = "";

		/* 信息项字段小数位数 */
		String str_XXX_XSWS = "";

		/* 信息项字段固定格式校验 */
		String str_XXX_GDGSJY = "";

		/* 信息项字段是否多容器 */
		String str_Xxx_DrqBz = "";

		/* 信息项最小行记录数 */
		String num_XXX_MIN_LINE = "";

		/* 信息项最大行记录数 */
		String num_XXX_MAX_LINE = "";

		/* 存储类型 */
		String str_TZ_XXX_CCLX = "";

		/* 信息项校验规则 */
		String str_JYGZ_ID = "";

		/* 信息项校验提示信息 */
		String str_JYGZ_TSXX = "";

		/* 信息项校验程序 */
		String str_PATH = "", str_NAME = "", str_METHOD = "";

		String sql = null;
		try {
			sql = tzSQLObject.getSQLText("SQL.TZApplicationSurveyBundle.TZ_SURVEY_CHECK_SQL");
		} catch (TzSystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			sql = null;
		}
		if (!StringUtils.isEmpty(sql)) {
			List<Map<String, Object>> listData = sqlQuery.queryForList(sql, new Object[] { surveyID });

			Map<String, Object> mapData = null;
			for (Object objData : listData) {
				mapData = (Map<String, Object>) objData;
				str_XXX_BH = mapData.get("TZ_XXX_BH") == null ? "" : mapData.get("TZ_XXX_BH").toString();
				str_XXX_MC = mapData.get("TZ_XXX_MC") == null ? "" : mapData.get("TZ_XXX_MC").toString();
				str_COM_MC = mapData.get("TZ_COM_LMC") == null ? "" : mapData.get("TZ_COM_LMC").toString();
				num_PAGE_NO = mapData.get("TZ_PAGE_NO") == null ? "" : mapData.get("TZ_PAGE_NO").toString();
				str_XXX_RQGS = mapData.get("TZ_XXX_RQGS") == null ? "" : mapData.get("TZ_XXX_RQGS").toString();
				str_XXX_NFMIN = mapData.get("TZ_XXX_NFMIN") == null ? "" : mapData.get("TZ_XXX_NFMIN").toString();
				str_XXX_NFMAX = mapData.get("TZ_XXX_NFMAX") == null ? "" : mapData.get("TZ_XXX_NFMAX").toString();
				str_XXX_ZSXZGS = mapData.get("TZ_XXX_ZSXZGS") == null ? "" : mapData.get("TZ_XXX_ZSXZGS").toString();
				str_XXX_ZDXZGS = mapData.get("TZ_XXX_ZDXZGS") == null ? "" : mapData.get("TZ_XXX_ZDXZGS").toString();
				str_XXX_YXSCLX = mapData.get("TZ_XXX_YXSCLX") == null ? "" : mapData.get("TZ_XXX_YXSCLX").toString();
				str_XXX_YXSCDX = mapData.get("TZ_XXX_YXSCDX") == null ? "" : mapData.get("TZ_XXX_YXSCDX").toString();
				str_XXX_BT_BZ = mapData.get("TZ_XXX_BT_BZ") == null ? "" : mapData.get("TZ_XXX_BT_BZ").toString();
				str_XXX_CHAR_BZ = mapData.get("TZ_XXX_CHAR_BZ") == null ? "" : mapData.get("TZ_XXX_CHAR_BZ").toString();
				num_XXX_MINLEN = mapData.get("TZ_XXX_MINLEN") == null ? "" : mapData.get("TZ_XXX_MINLEN").toString();
				num_XXX_MAXLEN = mapData.get("TZ_XXX_MAXLEN") == null ? "" : mapData.get("TZ_XXX_MAXLEN").toString();
				str_XXX_NUM_BZ = mapData.get("TZ_XXX_NUM_BZ") == null ? "" : mapData.get("TZ_XXX_NUM_BZ").toString();
				num_XXX_MIN = mapData.get("TZ_XXX_MIN") == null ? "" : mapData.get("TZ_XXX_MIN").toString();
				num_XXX_MAX = mapData.get("TZ_XXX_MAX") == null ? "" : mapData.get("TZ_XXX_MAX").toString();
				str_XXX_XSWS = mapData.get("TZ_XXX_XSWS") == null ? "" : mapData.get("TZ_XXX_XSWS").toString();
				str_XXX_GDGSJY = mapData.get("TZ_XXX_GDGSJY") == null ? "" : mapData.get("TZ_XXX_GDGSJY").toString();
				str_Xxx_DrqBz = mapData.get("TZ_XXX_DRQ_BZ") == null ? "" : mapData.get("TZ_XXX_DRQ_BZ").toString();
				num_XXX_MIN_LINE = mapData.get("TZ_XXX_MIN_LINE") == null ? ""
						: mapData.get("TZ_XXX_MIN_LINE").toString();
				num_XXX_MAX_LINE = mapData.get("TZ_XXX_MAX_LINE") == null ? ""
						: mapData.get("TZ_XXX_MAX_LINE").toString();
				str_TZ_XXX_CCLX = mapData.get("TZ_XXX_CCLX") == null ? "" : mapData.get("TZ_XXX_CCLX").toString();
				str_PATH = mapData.get("TZ_APPCLS_PATH") == null ? "" : mapData.get("TZ_APPCLS_PATH").toString();
				str_NAME = mapData.get("TZ_APPCLS_NAME") == null ? "" : mapData.get("TZ_APPCLS_NAME").toString();
				str_METHOD = mapData.get("TZ_APPCLS_METHOD") == null ? "" : mapData.get("TZ_APPCLS_METHOD").toString();
				str_JYGZ_TSXX = mapData.get("TZ_JYGZ_TSXX") == null ? "" : mapData.get("TZ_JYGZ_TSXX").toString();

				// 反射机制 调用 校验方法
				// 参数类型
				Class[] parameterTypes = new Class[26];
				parameterTypes[0] = Long.class;
				parameterTypes[1] = String.class;
				parameterTypes[2] = String.class;
				parameterTypes[3] = String.class;
				parameterTypes[4] = String.class;
				parameterTypes[5] = int.class;
				parameterTypes[6] = String.class;
				parameterTypes[7] = String.class;
				parameterTypes[8] = String.class;
				parameterTypes[9] = String.class;
				parameterTypes[10] = String.class;
				parameterTypes[11] = String.class;
				parameterTypes[12] = String.class;
				parameterTypes[13] = String.class;
				parameterTypes[14] = String.class;
				parameterTypes[15] = int.class;
				parameterTypes[16] = int.class;
				parameterTypes[17] = String.class;
				parameterTypes[18] = int.class;
				parameterTypes[19] = int.class;
				parameterTypes[20] = String.class;
				parameterTypes[21] = String.class;
				parameterTypes[22] = String.class;
				parameterTypes[23] = int.class;
				parameterTypes[24] = String.class;
				parameterTypes[25] = String.class;

				// Long numAppInsId,String strTplId,String strXxxBh,String
				// strXxxMc,String strComMc,
				// int numPageNo,String strXxxRqgs,String strXxxXfmin,String
				// strXxxXfmax,String strXxxZsxzgs,String strXxxZdxzgs,
				// String strXxxYxsclx,String strXxxYxscdx,String
				// strXxxBtBz,String
				// strXxxCharBz,int numXxxMinlen,int numXxxMaxlen,
				// String strXxxNumBz,int numXxxMin,int numXxxMax,String
				// strXxxXsws,String strXxxGdgsjy,String strXxxDrqBz,
				// int numXxxMinLine,String strTjxSub,String strJygzTsxx
				// 参数
				Object[] parameter = new Object[26];

				parameter[0] = Long.valueOf(surveyInsId);
				parameter[1] = surveyID;
				parameter[2] = str_XXX_BH;
				parameter[3] = str_XXX_MC;
				parameter[4] = str_COM_MC;
				parameter[5] = Integer.parseInt(num_PAGE_NO);
				parameter[6] = str_XXX_RQGS;
				parameter[7] = str_XXX_NFMIN;
				parameter[8] = str_XXX_NFMAX;
				parameter[9] = str_XXX_ZSXZGS;
				parameter[10] = str_XXX_ZDXZGS;
				parameter[11] = str_XXX_YXSCLX;
				parameter[12] = str_XXX_YXSCDX;
				parameter[13] = str_XXX_BT_BZ;
				parameter[14] = str_XXX_CHAR_BZ;
				parameter[15] = Integer.parseInt(num_XXX_MINLEN);
				parameter[16] = Integer.parseInt(num_XXX_MAXLEN);
				parameter[17] = str_XXX_NUM_BZ;
				parameter[18] = Integer.parseInt(num_XXX_MIN);
				parameter[19] = Integer.parseInt(num_XXX_MAX);
				parameter[20] = str_XXX_XSWS;
				parameter[21] = str_XXX_GDGSJY;
				parameter[22] = str_Xxx_DrqBz;
				parameter[23] = Integer.parseInt(num_XXX_MIN_LINE);
				parameter[24] = "";
				parameter[25] = str_JYGZ_TSXX;

				if (!StringUtils.isEmpty(str_PATH) && !StringUtils.isEmpty(str_NAME)
						&& !StringUtils.isEmpty(str_METHOD)) {
					tzOnlineAppUtility tzOnlineAppUtility = (tzOnlineAppUtility) ctx.getBean(str_PATH + "." + str_NAME);

					// a.得到对象所属类
					Class<?> ownerClass = tzOnlineAppUtility.getClass();
					Method method = null;
					try {
						method = ownerClass.getMethod(str_METHOD, parameterTypes);
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
					Object result = null;
					if (method != null) {
						try {
							result = method.invoke(tzOnlineAppUtility, parameter);
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							break;
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							break;
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							break;
						} // 必须要有类对象才可以调用
					}
					logger.info("结果返回值：" + result);
					if (result != null) {
						str_msg = str_msg + result + "\n";
					}

				}
			}
		}

		return str_msg;
	}
	
	private Map<String, Object> getMethod(String wjid,String eventType,Class[] parameterTypes,Object[] parameter){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", "0");
		result.put("msg", "");
		
		if(StringUtils.isBlank(wjid) || StringUtils.isBlank(eventType)){
			return result;
		}
		
		String strClsInfo = "SELECT TZ_APPCLS_PATH,TZ_APPCLS_NAME,TZ_APPCLS_METHOD FROM PS_TZ_DC_WJ_APPCLS_T appcls,PS_TZ_APPCLS_TBL cls WHERE appcls.TZ_APPCLS_ID = cls.TZ_APPCLS_ID AND appcls.TZ_DC_WJ_ID = ? AND appcls.TZ_APPCLS_TYPE = ? AND appcls.TZ_QY_STATUS = 'Y' LIMIT 0,1";
		Map<String, Object> mapClsInfo = sqlQuery.queryForMap(strClsInfo,new Object[] {  wjid,eventType });
		
		if(mapClsInfo == null){
			return result;
		}

		try{
			String clsName = "",clsPath = "",clsMethod = "";
			clsPath = (String)mapClsInfo.get("TZ_APPCLS_PATH");
			clsName = (String)mapClsInfo.get("TZ_APPCLS_NAME");
			clsMethod = (String)mapClsInfo.get("TZ_APPCLS_METHOD");
			
			Object myClass = Class.forName(clsPath + "." + clsName).newInstance();
			
			Method method = myClass.getClass().getMethod(clsMethod, parameterTypes);  
			result = (Map<String, Object>) method.invoke(myClass,parameter);
			System.out.println(clsMethod + "----> " + result);

		}catch (Exception e) {
			e.printStackTrace();
			result.replace("code", "1");
			result.replace("msg", e.getMessage());
		} 
		return result;
	}
}
