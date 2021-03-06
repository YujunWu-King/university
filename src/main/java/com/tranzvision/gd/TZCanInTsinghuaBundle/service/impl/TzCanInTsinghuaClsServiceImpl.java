package com.tranzvision.gd.TZCanInTsinghuaBundle.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsTzAqYhxxTblMapper;
import com.tranzvision.gd.TZAccountMgBundle.dao.PsoprdefnMapper;
import com.tranzvision.gd.TZAccountMgBundle.dao.PsroleuserMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTbl;
import com.tranzvision.gd.TZAccountMgBundle.model.Psoprdefn;
import com.tranzvision.gd.TZAccountMgBundle.model.Psroleuser;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcInsTMapper;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzLxfsInfoTblMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzRegUserTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzLxfsInfoTbl;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzRegUserT;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteEnrollClsServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.captcha.Patchca;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.httpclient.CommonUtils;
import com.tranzvision.gd.util.session.TzSession;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
/**
 * @author WRL  测测是否能上清华
 */
@Service("com.tranzvision.gd.TZCanInTsinghuaBundle.service.impl.TzCanInTsinghuaClsServiceImpl")
public class TzCanInTsinghuaClsServiceImpl extends FrameworkImpl {

	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private TZGDObject tzGdObject;

	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private GetSeqNum getSeqNum;
	
	@Autowired
	private PsTzDcInsTMapper psTzDcInsTMapper;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	
	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;
	
	@Autowired
	private SiteEnrollClsServiceImpl siteEnrollClsServiceImpl;
	
	@Autowired
	private PsoprdefnMapper psoprdefnMapper;
	
	@Autowired
	private PsTzAqYhxxTblMapper psTzAqYhxxTblMapper;
	
	@Autowired
	private PsTzLxfsInfoTblMapper psTzLxfsInfoTblMapper;
	
	@Autowired
	private PsTzRegUserTMapper psTzRegUserTMapper;
	
	@Autowired
	private PsroleuserMapper psroleuserMapper;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	/**
	 * Session存储的测试考生的Oprid
	 */
	public final String userSessionName = "TUser";
	
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		String strSen = "";
		String strResponse = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();   
		
		try{
			jacksonUtil.json2Map(strParams);
			if(jacksonUtil.containsKey("sen")){
				strSen = jacksonUtil.getString("sen");
				if("1".equals(strSen)){
					//邮箱校验
					return this.emailVerifyByEnroll(strParams, errMsg);
				}
				if("2".equals(strSen)){
					//注册用户
					return this.saveEnrollInfo(strParams, errMsg);
				}
				if("3".equals(strSen)){
					//验证码校验
					return this.checkCodeVerifyByActive(strParams, errMsg);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return strResponse;
	}
	
	/**
	 * 保存注册信息
	 * @param strParams
	 * @param errMsg
	 * @return
	 */
	public String saveEnrollInfo(String strParams, String[] errMsg) {
		String strResult = "\"failure\"";

		String oprid = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("data")) {
				Map<String, Object> dataMap = jacksonUtil.getMap("data");

				// 机构编号
				String strOrgId = "";
				if (dataMap.containsKey("_OrgId")) {
					strOrgId = ((String) dataMap.get("_OrgId")).trim();
				}
				
				// 站点编号
				String strSiteId = "";
				if (dataMap.containsKey("_SiteId")) {
					strSiteId = ((String) dataMap.get("_SiteId")).trim();
				}
				
				// 语音
				String strLang = "";
				if (dataMap.containsKey("_Lang")) {
					strLang = ((String) dataMap.get("_Lang")).trim();
				}
				
				// 密码;
				String userPwd = "";
				if (dataMap.containsKey("password")) {
					userPwd = ((String) dataMap.get("password")).trim();
				}

				// 确认密码;
				String strTZ_REPASSWORD = "";
				if (dataMap.containsKey("repassword")) {
					strTZ_REPASSWORD = ((String) dataMap.get("repassword")).trim();
				}

				// 邮箱;
				String strTZ_EMAIL = "";
				if (dataMap.containsKey("email")) {
					strTZ_EMAIL = ((String) dataMap.get("email")).trim();
				}

				// 验证码;
				String strYZM = "";
				if (dataMap.containsKey("yzm")) {
					strYZM = ((String) dataMap.get("yzm")).trim();
				}
				
				// 如果字段是邮箱，增加校验格式;
				String strEmailParas = "{\"email\":\"" + strTZ_EMAIL + "\",\"orgid\":\"" + strOrgId
						+ "\",\"lang\":\"" + strLang + "\",\"sen\":\"1\"}";
				String strEmailResult = this.emailVerifyByEnroll(strEmailParas, errMsg);
				if (StringUtils.equals(strEmailResult, "\"success\"")) {
					//注册用户
					
					// 校验验证码
					Patchca patchca = new Patchca();
					if (!patchca.verifyToken(request, strYZM)) {
						errMsg[0] = "3";
						errMsg[1] = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", "", "输入的验证码不正确",
								"Security code is incorrect.");
						
						return strResult;
					}

					String sql = "SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE LOWER(TZ_EMAIL) = LOWER(?) AND LOWER(TZ_JG_ID)=LOWER(?) limit 0,1";
					oprid = sqlQuery.queryForObject(sql, new Object[] { strTZ_EMAIL,strOrgId},"String");
					
					// 校验用户名、密码
					Object[] args = new Object[] { oprid, DESUtil.encrypt(userPwd, "TZGD_Tranzvision") };
					String strFlag = sqlQuery.queryForObject(tzGdObject.getSQLText("SQL.TZAuthBundle.TzCheckManagerPwd"), args,
							"String");

					if (!"Y".equals(strFlag)) {
						errMsg[0] = "3";
						errMsg[1] = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", "", "登录失败，请确认用户名和密码是否正确。",
								"Email address or password is incorrect !");
						return strResult;
					}
				}else{
					//未注册用户
					// 验证密码和确认密码是否一致;
					if (userPwd == null || !userPwd.equals(strTZ_REPASSWORD)) {
						errMsg[0] = "3";
						errMsg[1] = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", "", "密码和确认密码不一致!",
								"New Password and Confirm Password is not consistent!");
						return strResult;
					}
					/*激活方式*/
					String strActivateType = "E";
					// 定义激活状态，默认不激活;
					String strActiveStatus = "N";
					//注册项是否完整
					String strIsCmpl = "N";

					// 通过所有校验，开始创建账号;
					oprid = "TZ_" + getSeqNum.getSeqNum("PSOPRDEFN", "OPRID");
					// 生成登录账号;
					String dlzh = siteEnrollClsServiceImpl.tzGenerateAcount(strOrgId, oprid);

					// 保存用户信息;
					Psoprdefn psoprdefn = new Psoprdefn();
					psoprdefn.setOprid(oprid);
					String password = DESUtil.encrypt(strTZ_REPASSWORD, "TZGD_Tranzvision");
					psoprdefn.setOperpswd(password);
					psoprdefn.setAcctlock((short) 0);
					psoprdefn.setLastupdoprid(oprid);
					psoprdefn.setLastupddttm(new Date());
					psoprdefnMapper.insert(psoprdefn);

					PsTzAqYhxxTbl psTzAqYhxxTbl = new PsTzAqYhxxTbl();
					psTzAqYhxxTbl.setTzDlzhId(dlzh);
					psTzAqYhxxTbl.setTzJgId(strOrgId);
					psTzAqYhxxTbl.setOprid(oprid);
					psTzAqYhxxTbl.setTzEmail(strTZ_EMAIL);
					psTzAqYhxxTbl.setTzRylx("ZCYH");
					psTzAqYhxxTbl.setTzSjbdBz("N");
					psTzAqYhxxTbl.setTzYxbdBz("Y");
					psTzAqYhxxTbl.setTzJihuoZt(strActiveStatus);
					psTzAqYhxxTbl.setTzJihuoFs(strActivateType);
					psTzAqYhxxTbl.setTzIsCmpl(strIsCmpl);
					psTzAqYhxxTbl.setTzZhceDt(new Date());
					psTzAqYhxxTbl.setTzBjsEml("N");
					psTzAqYhxxTbl.setTzBjsSms("N");
					psTzAqYhxxTbl.setRowAddedDttm(new Date());
					psTzAqYhxxTbl.setRowAddedOprid(oprid);
					psTzAqYhxxTbl.setRowLastmantDttm(new Date());
					psTzAqYhxxTbl.setRowLastmantOprid(oprid);
					psTzAqYhxxTblMapper.insert(psTzAqYhxxTbl);

					// 通过所有校验，保存联系方式;
					PsTzLxfsInfoTbl psTzLxfsInfoTbl = new PsTzLxfsInfoTbl();
					psTzLxfsInfoTbl.setTzLxfsLy("ZCYH");
					psTzLxfsInfoTbl.setTzLydxId(oprid);
					psTzLxfsInfoTbl.setTzZyEmail(strTZ_EMAIL);
					psTzLxfsInfoTblMapper.insert(psTzLxfsInfoTbl);

					// 通过所有校验，保存用户注册信息;
					PsTzRegUserT psTzRegUserT = new PsTzRegUserT();
					psTzRegUserT.setOprid(oprid);
					psTzRegUserT.setTzSiteiId(strSiteId);
					psTzRegUserT.setRowAddedDttm(new Date());
					psTzRegUserT.setRowAddedOprid(oprid);
					psTzRegUserT.setRowLastmantDttm(new Date());
					psTzRegUserT.setRowLastmantOprid(oprid);
					
					psTzRegUserTMapper.insert(psTzRegUserT);
					
					// 添加角色;
					String roleSQL = " SELECT ROLENAME FROM PS_TZ_JG_ROLE_T WHERE TZ_JG_ID=? AND TZ_ROLE_TYPE='C'";
					List<Map<String, Object>> roleList = sqlQuery.queryForList(roleSQL, new Object[] { strOrgId });
					if (roleList != null && roleList.size() > 0) {
						for (int j = 0; j < roleList.size(); j++) {
							String rolename = (String) roleList.get(j).get("ROLENAME");
							Psroleuser psroleuser = new Psroleuser();
							psroleuser.setRoleuser(oprid);
							psroleuser.setRolename(rolename);
							psroleuser.setDynamicSw("N");
							psroleuserMapper.insert(psroleuser);
						}
					}
				}
				
				if(StringUtils.isNotBlank(oprid)){
					HttpSession session = request.getSession(false);
					if (session != null) {
						session.invalidate();
					}
					
					// 设置Session
					TzSession tzSession = new TzSession(request);
					tzSession.addSession(userSessionName, oprid);

		      		strResult = "\"success\"";
		            return strResult;
				}

				return strResult;
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "100";
			errMsg[1] = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", "", "获取数据失败，请联系管理员",
					"Get the data failed, please contact the administrator");
		}

		return strResult;
	}

	/**
	 * 校验注册邮箱格式以及是否被占用
	 * 
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	public String emailVerifyByEnroll(String strParams,String[] errorMsg){
		String strResult = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();   
		try{
			jacksonUtil.json2Map(strParams);
			if(jacksonUtil.containsKey("email")){
				String strEmail = jacksonUtil.getString("email").trim();
				String strOrgid = jacksonUtil.getString("orgid").trim();
				
		      	//邮箱是否被占用
		      	String sql = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL WHERE LOWER(TZ_EMAIL) = LOWER(?) AND LOWER(TZ_JG_ID)=LOWER(?)";
		      	int count = sqlQuery.queryForObject(sql, new Object[]{strEmail,strOrgid},"Integer");

		      	if(count > 0){
		      		strResult = "\"success\"";
		            return strResult;
		      	}
		        return strResult;
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "100";
			errorMsg[1] = "获取数据失败，请联系管理员!";
		}
		
		return strResult;
	}
	
	/**
	 * 验证码是否正确
	 * @param strParams
	 * @param errMsg
	 * @return
	 */
	public String checkCodeVerifyByActive(String strParams, String[] errMsg) {
		String strCheckCode = "";

		String strResult = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("checkCode")){
				strCheckCode = jacksonUtil.getString("checkCode").trim();
			}
				
			// 校验验证码
			Patchca patchca = new Patchca();
			if (!patchca.verifyToken(request, strCheckCode)) {
				errMsg[0] = "1";
				errMsg[1] = "验证码不正确";
				return strResult;
			}

			strResult = "\"success\"";
			return strResult;
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "100";
			errMsg[1] = "获取数据失败，请联系管理员!";
		}

		return strResult;
	}
	
	/*
	 * 1.选择测试方向
	 * 2.
	 * (non-Javadoc)
	 * @see com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl#tzGetHtmlContent(java.lang.String)
	 */
	@Override
	public String tzGetHtmlContent(String strParams) {
		String isMobile = request.getParameter("isMobile");
		//是否是从网站首页进入
		String isWebsite=request.getParameter("isWebsite");
		String strResponse = "获取数据失败，请联系管理员";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("isMobile")){
				isMobile = jacksonUtil.getString("isMobile").trim();
			}
            
			// 考生可参与的有效的测试方向
			return this.createTestDirection(isMobile,isWebsite);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResponse;
	}

	/**
	 * 当前考生能参与的测试方向
	 * 
	 * @param isMobile
	 * @param isWebsite 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String createTestDirection(String isMobile, String isWebsite) {
		String strTypes = "获取数据失败，请联系管理员";
		String strHtml = "";
		try {
			/*当前考生oprid*/
			String strOprid = "";
			TzSession tzSession = new TzSession(request);
			Object objOprid = tzSession.getSession(userSessionName);
			logger.info("------test type -----" + objOprid);
			if (null != objOprid) {
				strOprid = String.valueOf(objOprid);
			}else{
				//是否有当前登录人 ldd 170401
				String strCurUser=tzLoginServiceImpl.getLoginedManagerOprid(request);
				if (null!=strCurUser){
					strOprid=strCurUser;
				}
			}
			
			/*获取机构编号*/
			String jsSql = "SELECT TZ_JG_ID FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID = ? limit 0,1";
			String jgid = sqlQuery.queryForObject(jsSql, new Object[] { strOprid},"String");
			if(StringUtils.isBlank(jgid) || StringUtils.isBlank(strOprid)){
				return strTypes;
			}

			//列表SQL
//			String sql = "SELECT TZ_CS_WJ_ID,TZ_CS_WJ_NAME,TZ_DC_WJ_ID, date_format(TZ_DC_WJ_JSSJ,'%H:%i:%s') TZ_DC_WJ_KSSJ,date_format(TZ_DC_WJ_JSSJ,'%H:%i:%s') TZ_DC_WJ_JSSJ FROM PS_TZ_CSWJ_TBL WHERE TZ_JG_ID = ? AND TZ_STATE = '0' AND TZ_DC_WJ_ZT = '1' AND TZ_DC_WJ_KSRQ <= curdate() AND TZ_DC_WJ_JSRQ >= curdate() ORDER BY TZ_DC_WJ_KSRQ";
			//String sql = "SELECT TZ_CS_WJ_ID,TZ_CS_WJ_NAME,TZ_DC_WJ_ID FROM PS_TZ_CSWJ_TBL WHERE TZ_JG_ID = ? AND TZ_STATE = '0' AND TZ_DC_WJ_ZT = '1' AND str_to_date(concat(TZ_DC_WJ_KSRQ,' ',TZ_DC_WJ_KSSJ),'%Y-%m-%d %T') <= date_format(curdate(),'%Y-%m-%d %T') AND str_to_date(concat(TZ_DC_WJ_JSRQ,' ',TZ_DC_WJ_JSSJ),'%Y-%m-%d %T') >= date_format(curdate(),'%Y-%m-%d %T') ORDER BY TZ_DC_WJ_KSRQ";
			String sql="SELECT TZ_CS_WJ_ID,TZ_CS_WJ_NAME,A.TZ_DC_WJ_ID FROM PS_TZ_CSWJ_TBL A,PS_TZ_DC_WJ_DY_T B WHERE A.TZ_DC_WJ_ID=B.TZ_DC_WJ_ID AND B.TZ_JG_ID =? AND TZ_STATE = '0' AND B.TZ_DC_WJ_ZT = '1' AND str_to_date(concat(B.TZ_DC_WJ_KSRQ,' ',B.TZ_DC_WJ_KSSJ),'%Y-%m-%d %T') <= date_format(curdate(),'%Y-%m-%d %T') AND str_to_date(concat(B.TZ_DC_WJ_JSRQ,' ',B.TZ_DC_WJ_JSSJ),'%Y-%m-%d %T') >= date_format(curdate(),'%Y-%m-%d %T') ORDER BY B.TZ_DC_WJ_KSRQ;";
			List<?> resultlist = sqlQuery.queryForList(sql, new Object[] { jgid});
			
			strTypes = "";
			
			for (Object obj : resultlist) {
				Map<String, Object> result = (Map<String, Object>) obj;
	
				String attrCsWjId = result.get("TZ_CS_WJ_ID") == null ? "" : String.valueOf(result.get("TZ_CS_WJ_ID"));
				String attrCsWjName = result.get("TZ_CS_WJ_NAME") == null ? "" : String.valueOf(result.get("TZ_CS_WJ_NAME"));
				String attrWjId = result.get("TZ_DC_WJ_ID") == null ? "" : String.valueOf(result.get("TZ_DC_WJ_ID"));

				String wjUrlSql = "SELECT TZ_DC_WJ_URL FROM PS_TZ_DC_WJ_DY_T WHERE TZ_DC_WJ_ID = ? limit 0,1";
				String wjUrl = sqlQuery.queryForObject(wjUrlSql, new Object[] { attrWjId },"String");
				String strTr = tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_TR",attrCsWjId,wjUrl,attrCsWjName);

				strTypes = strTypes + strTr;
			}
			if(StringUtils.isBlank(strTypes)){
				strTypes = "<tr><td><span'>没有可选择的测试方向！</span></td></tr>";
				
			}
			
			if(StringUtils.equals("Y", isMobile)){
				strHtml = tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_MTESTTYPE",request.getContextPath(),strTypes);
			}else{
				if(StringUtils.equals("Y", isWebsite)){
					strHtml = tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_W_TESTTYPE",request.getContextPath(),strTypes);
				}else{
					strHtml = tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_TESTTYPE",request.getContextPath(),strTypes);
				}
			}
			
		}catch(Exception e){
	    	e.printStackTrace();
	    }
		
		return strHtml;
	}
	/**
	 * 获取指定问卷、指定页的统计信息
	 * 
	 * @param wjid
	 * @param pageno
	 * @param isMobile
	 * @return
	 */
	public String getCountPage(String wjid, String pageno, boolean isMobile) {
		String strCountHtml = "";
        int TZ_PAGE_NO=Integer.valueOf((pageno==null?"1":pageno));
        TZ_PAGE_NO=TZ_PAGE_NO-1;
        
        int maxPageNo=sqlQuery.queryForObject("select max(TZ_PAGE_NO) from PS_TZ_DCWJ_XXXPZ_T where TZ_DC_WJ_ID=?", new Object[]{wjid}, "Integer");
		int lastPageNo=0;
		int flagCount=0;
		if(Integer.valueOf(pageno).equals(maxPageNo)){
			lastPageNo=maxPageNo;
			flagCount=flagCount+1;
		}
		
		TzSession tzSession = new TzSession(request);
		Object objOprid = tzSession.getSession(userSessionName);
        String strOprid="";
		if (null != objOprid) {
			strOprid = String.valueOf(objOprid);
		}else{
			String strCurUser=tzLoginServiceImpl.getLoginedManagerOprid(request);
			if (null!=strCurUser){
				strOprid=strCurUser;
			}
		}
		
		this.logger.info(new Date() + " >---问卷编号:---- " + wjid + " >----PageNo----" + pageno);
		try {
			String strDivHtml = "";

			try {
				strCountHtml = tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_SURVEY_ANS_HTML",
						request.getContextPath());
			} catch (TzSystemException e) {
				strCountHtml = "";
				e.printStackTrace();
			}
			//测试问卷调查项目编号
			String strClassId=sqlQuery.queryForObject("select TZ_CLASS_ID from PS_TZ_CSWJ_TBL where TZ_DC_WJ_ID=?", new Object[]{wjid}, "String");
			String strXxxBh, strComLmc, strXxxKxzMs, strXxxKxzQz, strXxxKxzCode;
			String strRadioBoxHtml, strRadioBoxHtml2;
			String strCategories;
			// 是否计算分值字段TZ_IS_AVG
			//String TZ_IS_AVG = "N";
			@SuppressWarnings("unused")
			String TZ_XXX_QID = "";
			String TZ_TITLE = "";
			// 保存平均分
			double avgScore = 0;
			int countY, count;
			double tempCount;
			//表格显示颜色
			String[] arrColor = {
					"#FF4700","#F10EF1","#03FC26","#3030CF","#FCF503","#640A62","#245D2C","#1A5267",
	            	"#6A0A03","#C60210","#678B1A","#B8860B","#6F7CBC","#6F7CBC","#7b68ee","#cd5c5c"		
			};
			//本科院校信息项编号hardcode
			String strBkSchool=sqlQuery.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?", new Object[]{"TZ_BKYX_XXXBH"}, "String");
			// 用来保留小数2位位数
			DecimalFormat decimalFormat = new DecimalFormat("#.00");
			//该问卷下面的所有问卷实例
			String totalCount = sqlQuery.queryForObject("select count(*) from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?", new Object[] { wjid }, "String");
			// 所有循环的索引都用index,i,j
			final String dcwjXxxPzSQL = "select TZ_XXX_BH,TZ_TITLE,TZ_XXX_QID,TZ_COM_LMC,TZ_IS_AVG  from PS_TZ_DCWJ_XXXPZ_T where TZ_DC_WJ_ID=?   and TZ_PAGE_NO=? and TZ_COM_LMC not in ('PageNav') order by TZ_ORDER";
			List<Map<String, Object>> dcwjXxxPzDataList = new ArrayList<Map<String, Object>>();
			dcwjXxxPzDataList = sqlQuery.queryForList(dcwjXxxPzSQL, new Object[] { wjid,TZ_PAGE_NO });
			if (dcwjXxxPzDataList != null) {
				for (int index = 0; index < dcwjXxxPzDataList.size(); index++) {
					
					Map<String, Object> dcwjXxxPzMap = new HashMap<String, Object>();
					dcwjXxxPzMap = dcwjXxxPzDataList.get(index);
					strXxxBh = dcwjXxxPzMap.get("TZ_XXX_BH") == null ? null : dcwjXxxPzMap.get("TZ_XXX_BH").toString();
					TZ_TITLE = dcwjXxxPzMap.get("TZ_TITLE") == null ? "" : dcwjXxxPzMap.get("TZ_TITLE").toString();
					TZ_XXX_QID = dcwjXxxPzMap.get("TZ_XXX_QID") == null ? "" : dcwjXxxPzMap.get("TZ_XXX_QID").toString();
					strComLmc = dcwjXxxPzMap.get("TZ_COM_LMC") == null ? null : dcwjXxxPzMap.get("TZ_COM_LMC").toString();
					String isExistFlag=sqlQuery.queryForObject("SELECT 'Y' FROM PS_TZ_CSWJ_DCX_TBL WHERE TZ_DC_WJ_ID=? and TZ_XXX_BH=?", new Object[]{wjid,strXxxBh},"String");
				    if(null!=isExistFlag&&"Y".equals(isExistFlag)){
					//TZ_IS_AVG = dcwjXxxPzMap.get("TZ_IS_AVG") == null ? "N" : dcwjXxxPzMap.get("TZ_IS_AVG").toString();
					//String strPersonId = tzLoginServiceImpl.getLoginedManagerOprid(request);
					//根据当前登录人和问卷编号查找问卷实例编号
					String strInsId=sqlQuery.queryForObject("select TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=? and PERSON_ID=? order by TZ_APP_INS_ID DESC LIMIT 0,1", new Object[]{wjid,strOprid}, "String");
					logger.info("===当前登录人====personId:" + strOprid);
					logger.info("===问卷实例ID====strInsId:" + strInsId);
					String TZ_BY_DESC="";
					if (null!=strOprid&&!"".equals(strOprid)&&null!=strInsId&&!"".equals(strInsId)) {
						String	TZ_BYSJ_BH=sqlQuery.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?", new Object[]{"TZ_BYSJ_XXXBH"}, "String");
						String	TZ_XUELI_BH=sqlQuery.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?", new Object[]{"TZ_XUELE_XXXBH"}, "String");
						//学历
						int strXueli=sqlQuery.queryForObject("select TZ_XXXKXZ_MC from PS_TZ_DC_DHCC_T WHERE TZ_APP_INS_ID=? and TZ_XXX_BH=?", new Object[]{strInsId,TZ_XUELI_BH}, "int");
						//毕业时间
						Date graduateDate=sqlQuery.queryForObject("select TZ_APP_S_TEXT  from PS_TZ_DC_CC_T WHERE TZ_APP_INS_ID=? and TZ_XXX_BH=?", new Object[]{strInsId,TZ_BYSJ_BH}, "date");
						//入学时间
						String	TZ_CAN_RXSJ=sqlQuery.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?", new Object[]{"TZ_CAN_RXSJ"}, "String");
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
						//String nowTimeStr = simpleDateFormat.format(new Date());
						Date nowDate = simpleDateFormat.parse(TZ_CAN_RXSJ);
						//相隔天数
						logger.info("===毕业时间====graduateDate:" + graduateDate);
						int year=0;
						if (null==graduateDate||(nowDate.getTime()<=graduateDate.getTime())){
						}else{
							year=(int) ((nowDate.getTime()-graduateDate.getTime())/86400000/365);
						}
						String strSfFh="不符合";
						if (strXueli>=2&&year>=3){
							strSfFh="符合";
						}
						if (strXueli>=3&&year>=2){
							strSfFh="符合";
						}
						TZ_BY_DESC=sqlQuery.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?", new Object[]{"TZ_BY_DESC"}, "String");
						TZ_BY_DESC=TZ_BY_DESC.replaceAll("X",year+"");
						TZ_BY_DESC=TZ_BY_DESC.replaceAll("Y",strSfFh);
					
						//最后一页，需要显示工作经验文本描述
						if(lastPageNo>0&&flagCount==1){
							String TZ_JY_DESC1=sqlQuery.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?", new Object[]{"TZ_JY_DESC1"}, "String");
							String TZ_JY_DESC2=sqlQuery.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?", new Object[]{"TZ_JY_DESC2"}, "String");
							String TZ_JY_DESC3=sqlQuery.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?", new Object[]{"TZ_JY_DESC3"}, "String");
							String hisGzjy=sqlQuery.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?", new Object[]{"TZ_HISJY_"+wjid}, "String");
							logger.info("===历史工作经验====hisGzjy:" + hisGzjy);
							if(null==hisGzjy||"".equals(hisGzjy)){
								hisGzjy="0";
							}
							TZ_JY_DESC1=TZ_JY_DESC1.replaceAll("X", year+"");
							TZ_JY_DESC2=TZ_JY_DESC2.replaceAll("X", strClassId);
							TZ_JY_DESC2=TZ_JY_DESC2.replaceAll("Y", hisGzjy);
							//本次测试的平均工作经验
							List<Map<String,Object>> list=sqlQuery.queryForList("select TZ_APP_S_TEXT  from PS_TZ_DC_CC_T WHERE TZ_APP_INS_ID in(select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=?", new Object[]{wjid,TZ_BYSJ_BH});
							int total=sqlQuery.queryForObject("select count(*)  from PS_TZ_DC_CC_T WHERE TZ_APP_INS_ID in(select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=?", new Object[]{wjid,TZ_BYSJ_BH}, "int");
							float countByYear=0;
							for(int i=0;i<list.size();i++){
								Map<String,Object> map=list.get(i);
								Date dateTmp=simpleDateFormat.parse(map.get("TZ_APP_S_TEXT").toString());
								float tmpYear;
								if(nowDate.getTime()<=dateTmp.getTime()){
									tmpYear=0;
								}else{
									tmpYear=(float) ((nowDate.getTime()-dateTmp.getTime())/86400000/365);
								}
								countByYear=countByYear+tmpYear;
							}
							double avgYear=0;
							if(total>0){
								avgYear=Double.valueOf(countByYear/total);
							}
							DecimalFormat df=new DecimalFormat(".##");
							String avgYear2=df.format(avgYear);
							TZ_JY_DESC3=TZ_JY_DESC3.replaceAll("X", strClassId);
							TZ_JY_DESC3=TZ_JY_DESC3.replaceAll("Y", avgYear2);
							strDivHtml = strDivHtml + tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_TB_L_HTML",TZ_JY_DESC1,TZ_JY_DESC2,TZ_JY_DESC3);
							flagCount=flagCount+1;
						}
					}
                    //自动填空题
					if (strComLmc != null && (strComLmc.equals("autoCompletion"))&&(strXxxBh.equals(strBkSchool))) {
						//关于本科院校的描述
						if(isMobile){
							strDivHtml = strDivHtml + tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_TB_F_HTML",TZ_BY_DESC);
						}else{
							strDivHtml = strDivHtml + tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_TB_F_HTML", TZ_BY_DESC);
						}
						strRadioBoxHtml = "";
						strRadioBoxHtml2 = "";
						strCategories="";
						// type用于后面合成最终结果html中 显示控件类型
						
                        int total=sqlQuery.queryForObject("select  count(TZ_APP_S_TEXT) from PS_TZ_DC_CC_T WHERE TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=? and  TZ_APP_S_TEXT<>''", new Object[]{wjid, strXxxBh}, "int");
						final String cswjPzxSQL = "select TZ_SCHOOL_TYPEID,TZ_SCHOOL_TYPENAME from PS_TZ_SCHOOL_TYPE_VW where TZ_SCHOOL_FLAG='Y'";
						List<Map<String, Object>> cswjPzxDataList = new ArrayList<Map<String, Object>>();
						cswjPzxDataList = sqlQuery.queryForList(cswjPzxSQL);
						String autoCompAns=sqlQuery.queryForObject("select TZ_SCHOOL_TYPENAME  from PS_TZ_SCHOOL_TYPE_VW where TZ_SCHOOL_TYPEID in(select  (select TZ_SCHOOL_TYPE from PS_TZ_SCH_LIB_TBL where TZ_SCHOOL_NAME=TZ_APP_S_TEXT)TZ_SCHOOL_TYPE  from PS_TZ_DC_CC_T WHERE TZ_APP_INS_ID =? and TZ_XXX_BH=?)", new Object[]{strInsId,strXxxBh}, "String");
						if (cswjPzxDataList.size()>0&&cswjPzxDataList != null) {
							for (int i = 0; i < cswjPzxDataList.size(); i++) {
								Map<String, Object> completionMap = new HashMap<String, Object>();
								completionMap = cswjPzxDataList.get(i);
								String TZ_SCHOOL_TYPEID=completionMap.get("TZ_SCHOOL_TYPEID").toString();
								String TZ_SCHOOL_TYPENAME=completionMap.get("TZ_SCHOOL_TYPENAME").toString();
								int choseNum=sqlQuery.queryForObject("select count(*) FROM (select  TZ_APP_S_TEXT,(select TZ_SCHOOL_TYPE from PS_TZ_SCH_LIB_TBL where TZ_SCHOOL_NAME=TZ_APP_S_TEXT limit 0,1)TZ_SCHOOL_TYPE  from PS_TZ_DC_CC_T WHERE TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=?)TMP where TMP.TZ_SCHOOL_TYPE=?", new Object[]{wjid, strXxxBh,TZ_SCHOOL_TYPEID}, "int");
								if (choseNum > 0) {
									// 投票百分比
									tempCount = Double.valueOf(decimalFormat.format((double) choseNum / (double) total * 100));
								} else {
									tempCount = 0;
								}
								
								if (!strRadioBoxHtml.equals("")) {
									strRadioBoxHtml = strRadioBoxHtml + "," + tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_SUB_TB2_HTML",TZ_SCHOOL_TYPENAME, String.valueOf(tempCount),arrColor[i]);
								} else {
									strRadioBoxHtml = tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_SUB_TB2_HTML", TZ_SCHOOL_TYPENAME, String.valueOf(tempCount),arrColor[i]);
								}
							}
							
							//计算历史得分
							List<Map<String, Object>> BkHisDataList = new ArrayList<Map<String, Object>>();
							BkHisDataList=sqlQuery.queryForList("select TZ_XXXKXZ_MS,TZ_HISTORY_VAL from PS_TZ_CSWJ_PCT_TBL where TZ_DC_WJ_ID=? and TZ_XXX_BH=? order by TZ_XXXKXZ_MC", new Object[]{wjid, strXxxBh});
						    if(BkHisDataList!=null){
						    	for (int i = 0; i < BkHisDataList.size(); i++){
						    		Map<String, Object> map = new HashMap<String, Object>();
									map = BkHisDataList.get(i);
									if (!strRadioBoxHtml2.equals("")) {
								    	strRadioBoxHtml2 = strRadioBoxHtml2 + "," + tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_SUB_TB2_HTML", map.get("TZ_XXXKXZ_MS").toString(), map.get("TZ_HISTORY_VAL").toString(),arrColor[i]);
									} else {
										strRadioBoxHtml2 = tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_SUB_TB2_HTML", map.get("TZ_XXXKXZ_MS").toString(), map.get("TZ_HISTORY_VAL").toString(),arrColor[i]);
									}
								    
						    	}
						    }
							// 拼最终统计 填空题结果的Html
							if(isMobile){
								strDivHtml = strDivHtml + tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_TB_M_HTML",  TZ_TITLE,totalCount, strClassId, strXxxBh, strRadioBoxHtml, strCategories,strRadioBoxHtml2,autoCompAns);
							}else{
								strDivHtml = strDivHtml + tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_TB_HTML",  TZ_TITLE, totalCount, strClassId, strXxxBh, strRadioBoxHtml, strCategories,strRadioBoxHtml2,autoCompAns);
							}
							// strRadioBoxHtml,strRadioBoxHtml2变量通用于所有控件
							// 每次用完要进行初始化
							strRadioBoxHtml = "";
							strRadioBoxHtml2 = "";
							strCategories="";
						}
					}
					// 单选题
					if (strComLmc != null && strComLmc.equals("RadioBox")) {
						// 平均分清零
						avgScore = 0;
						strRadioBoxHtml = "";
						strRadioBoxHtml2 = "";
						strCategories="";
						final String radioBoxSQL = "select TZ_XXXKXZ_MS,TZ_XXXKXZ_MC,TZ_XXXKXZ_QZ from PS_TZ_DCWJ_XXKXZ_T where TZ_DC_WJ_ID=? and  TZ_XXX_BH=? order by TZ_ORDER";
						List<Map<String, Object>> radioBoxDataList = new ArrayList<Map<String, Object>>();
						radioBoxDataList = sqlQuery.queryForList(radioBoxSQL, new Object[] { wjid, strXxxBh });

						if (radioBoxDataList != null) {
							String autoRadioAns=sqlQuery.queryForObject("select TZ_APP_S_TEXT from PS_TZ_DC_DHCC_T WHERE TZ_APP_INS_ID=? and TZ_XXX_BH=?", new Object[]{strInsId,strXxxBh}, "String");
							for (int i = 0; i < radioBoxDataList.size(); i++) {
								Map<String, Object> radioBoxMap = new HashMap<String, Object>();
								radioBoxMap = radioBoxDataList.get(i);
								// 单选题 可选值描述(這里实际是选项名称)
								strXxxKxzMs = radioBoxMap.get("TZ_XXXKXZ_MS") == null ? null : radioBoxMap.get("TZ_XXXKXZ_MS").toString();
								// 单选题 可选值名称(这里实际是选项题号)
								strXxxKxzCode = radioBoxMap.get("TZ_XXXKXZ_MC") == null ? null : radioBoxMap.get("TZ_XXXKXZ_MC").toString();
								final String SQL1 = "select count(*) from PS_TZ_DC_DHCC_T WHERE TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=?";
								count = sqlQuery.queryForObject(SQL1, new Object[] { wjid, strXxxBh }, "int");
								final String SQL2 = "select count(*) from PS_TZ_DC_DHCC_T WHERE TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=? and TZ_XXXKXZ_MC=?";
								countY = sqlQuery.queryForObject(SQL2, new Object[] { wjid, strXxxBh, strXxxKxzCode }, "int");
								
								// 单选题分值:TZ_XXXKXZ_QZ 数据库中的数据类型为demical
								strXxxKxzQz = radioBoxMap.get("TZ_XXXKXZ_QZ") == null ? "0" : radioBoxMap.get("TZ_XXXKXZ_QZ").toString();
								// 单选题总分
								logger.info("单选题分值：" + strXxxKxzQz);
								// 如果投票的人数大于0,则计算投票人数占总参与人数的百分比，和投票平均得分
								if (count > 0) {
									// 投票百分比
									tempCount = Double.valueOf(decimalFormat.format((double) countY / (double) count * 100));
								} else {
									tempCount = 0;
									avgScore = avgScore + 0;
								}
								if(strXxxKxzMs.length()>=5){
									strXxxKxzMs=strXxxKxzMs.substring(0, 5)+"...";
								}
								//暂时不显示分值，只显示百分比和信息项描述
								if (!strRadioBoxHtml.equals("")) {
									strRadioBoxHtml = strRadioBoxHtml + "," + tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_SUB_TB2_HTML", strXxxKxzMs, String.valueOf(tempCount),arrColor[i]);
								} else {
									strRadioBoxHtml = tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_SUB_TB2_HTML", strXxxKxzMs, String.valueOf(tempCount),arrColor[i]);
								}
								
							    if(!strCategories.equals("")){
							    	strCategories=strCategories+",'"+strXxxKxzMs+"'";
							    }else{
							    	strCategories="'"+strXxxKxzMs+"'";
							    }
							    //计算历史得分
							    String strHisTempCount=sqlQuery.queryForObject("select TZ_HISTORY_VAL from PS_TZ_CSWJ_PCT_TBL where TZ_DC_WJ_ID=? and TZ_XXX_BH=? and TZ_XXXKXZ_MC=?", new Object[]{wjid, strXxxBh, strXxxKxzCode}, "String");
							    strHisTempCount=(strHisTempCount==null?"0":strHisTempCount.toString());
							    if (!strRadioBoxHtml2.equals("")) {
							    	strRadioBoxHtml2 = strRadioBoxHtml2 + "," + tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_SUB_TB2_HTML", strXxxKxzMs, strHisTempCount,arrColor[i]);
								} else {
									strRadioBoxHtml2 = tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_SUB_TB2_HTML", strXxxKxzMs, strHisTempCount,arrColor[i]);
								}
								//logger.info("===单选题====strRadioBoxHtml:" + strRadioBoxHtml);
							}
							// 拼最终统计 单选题结果的Html
							if(isMobile){
								strDivHtml = strDivHtml + tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_TB_M_HTML", TZ_TITLE,totalCount, strClassId, strXxxBh, strRadioBoxHtml,strCategories,strRadioBoxHtml2,autoRadioAns);
							}else{
							    strDivHtml = strDivHtml + tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_TB_HTML", TZ_TITLE,totalCount, strClassId, strXxxBh, strRadioBoxHtml,strCategories,strRadioBoxHtml2,autoRadioAns);
							}
							// strRadioBoxHtml,strRadioBoxHtml2变量通用于所有控件
							// 每次用完要进行初始化
							strRadioBoxHtml = "";
							strRadioBoxHtml2 = "";
							strCategories="";
						}
						logger.info("单选题终strDivHtml==" + strDivHtml);
					}
					// 单选量表题
					if (strComLmc != null && strComLmc.equals("RadioBoxQu")) {
						avgScore = 0;
						strRadioBoxHtml = "";
						strRadioBoxHtml2 = "";
						strCategories="";
						final String radioBoxQuSQL = "select TZ_XXXKXZ_MS,TZ_XXXKXZ_QZ,TZ_XXXKXZ_MC from PS_TZ_DCWJ_XXKXZ_T where TZ_DC_WJ_ID=? and  TZ_XXX_BH=? order by TZ_ORDER";
						List<Map<String, Object>> radioBoxDataList = new ArrayList<Map<String, Object>>();
						radioBoxDataList = sqlQuery.queryForList(radioBoxQuSQL, new Object[] { wjid, strXxxBh });
						if (radioBoxDataList != null) {
							String autoRadioBoxQuAns=sqlQuery.queryForObject("select TZ_APP_S_TEXT from PS_TZ_DC_DHCC_T WHERE TZ_APP_INS_ID=? and TZ_XXX_BH=?", new Object[]{strInsId,strXxxBh}, "String");
							for (int i = 0; i < radioBoxDataList.size(); i++) {
								Map<String, Object> radioBoxQuMap = new HashMap<String, Object>();
								radioBoxQuMap = radioBoxDataList.get(i);

								strXxxKxzMs = radioBoxQuMap.get("TZ_XXXKXZ_MS") == null ? null : radioBoxQuMap.get("TZ_XXXKXZ_MS").toString();
								strXxxKxzQz = radioBoxQuMap.get("TZ_XXXKXZ_QZ") == null ? null : radioBoxQuMap.get("TZ_XXXKXZ_QZ").toString();
								strXxxKxzCode = radioBoxQuMap.get("TZ_XXXKXZ_MC") == null ? null : radioBoxQuMap.get("TZ_XXXKXZ_MC").toString();

								final String SQL1 = "select count(*) from PS_TZ_DC_DHCC_T WHERE TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=?";
								count = sqlQuery.queryForObject(SQL1, new Object[] { wjid, strXxxBh }, "int");
								final String SQL2 = "select count(*) from PS_TZ_DC_DHCC_T WHERE TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=? and TZ_XXXKXZ_MC=?";
								countY = sqlQuery.queryForObject(SQL2, new Object[] { wjid, strXxxBh, strXxxKxzCode },
										"int");
								
								if (count > 0) {
									// 百分比
									tempCount = Double.valueOf(decimalFormat.format((double) countY / (double) count * 100));
									// 平均分
									avgScore = Double.valueOf(decimalFormat.format(avgScore+ (double) countY / (double) count * Double.valueOf(strXxxKxzQz)));
								} else {
									tempCount = 0;
									avgScore = avgScore + 0;
								}
								if(strXxxKxzMs.length()>=5){
									strXxxKxzMs=strXxxKxzMs.substring(0, 5)+"...";
								}
								if (!strRadioBoxHtml.equals("")) {
									strRadioBoxHtml = strRadioBoxHtml + "," + tzGdObject.getHTMLText( "HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_SUB_TB2_HTML", strXxxKxzMs, String.valueOf(tempCount),arrColor[i]);
								} else {
									strRadioBoxHtml = tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_SUB_TB2_HTML", strXxxKxzMs,String.valueOf(tempCount),arrColor[i]);
								}
								//计算历史得分
							    String strHisTempCount=sqlQuery.queryForObject("select TZ_HISTORY_VAL from PS_TZ_CSWJ_PCT_TBL where TZ_DC_WJ_ID=? and TZ_XXX_BH=? and TZ_XXXKXZ_MC=?", new Object[]{wjid, strXxxBh, strXxxKxzCode}, "String");
							    strHisTempCount=(strHisTempCount==null?"0":strHisTempCount.toString());
							    if (!strRadioBoxHtml2.equals("")) {
							    	strRadioBoxHtml2 = strRadioBoxHtml2 + "," + tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_SUB_TB2_HTML", strXxxKxzMs, strHisTempCount,arrColor[i]);
								} else {
									strRadioBoxHtml2 = tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_SUB_TB2_HTML", strXxxKxzMs, strHisTempCount,arrColor[i]);
								}
							   if(!strCategories.equals("")){
							    	strCategories=strCategories+",'"+strXxxKxzMs+"'";
							   }else{
							    	strCategories="'"+strXxxKxzMs+"'";
							   }
							}
							// 拼最终统计 单选题结果的Html
							if(isMobile){
								strDivHtml = strDivHtml + tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_TB_M_HTML",  TZ_TITLE, totalCount, strClassId, strXxxBh, strRadioBoxHtml, strCategories,strRadioBoxHtml2,autoRadioBoxQuAns);
							}else{
								strDivHtml = strDivHtml + tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_TB_HTML",  TZ_TITLE, totalCount, strClassId, strXxxBh, strRadioBoxHtml, strCategories,strRadioBoxHtml2,autoRadioBoxQuAns);
							}
 
							// strRadioBoxHtml,strRadioBoxHtml2变量通用于所有控件
							// 每次用完要进行初始化
							strRadioBoxHtml = "";
							strRadioBoxHtml2 = "";
							strCategories="";
						}
					}

					// 下拉框
					String province = sqlQuery.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?", new Object[]{"TZ_CAN_PROVINCE"}, "String");
					//省份不统计
					if (strComLmc != null && strComLmc.equals("ComboBox")&&!strXxxBh.equals(province)&&null!=province) {
						strRadioBoxHtml = "";
						strRadioBoxHtml2 = "";
						strCategories="";
						final String comboBoxSQL = "select TZ_XXXKXZ_MS,TZ_XXXKXZ_MC from PS_TZ_DCWJ_XXKXZ_T where TZ_DC_WJ_ID=? and  TZ_XXX_BH=? order by TZ_ORDER";
						List<Map<String, Object>> comboBoxDataList = new ArrayList<Map<String, Object>>();
						comboBoxDataList = sqlQuery.queryForList(comboBoxSQL, new Object[] { wjid, strXxxBh });

						if (comboBoxDataList != null) {
							String comboBoxAns=sqlQuery.queryForObject("select TZ_XXXKXZ_MS from PS_TZ_DCWJ_XXKXZ_T where TZ_DC_WJ_ID=? and  TZ_XXX_BH=? and TZ_XXXKXZ_MC=(select TZ_APP_S_TEXT from PS_TZ_DC_CC_T WHERE TZ_APP_INS_ID=? and TZ_XXX_BH=?)", new Object[]{wjid,strXxxBh,strInsId,strXxxBh}, "String");

							for (int i = 0; i < comboBoxDataList.size(); i++) {
								Map<String, Object> comboBoxMap = new HashMap<String, Object>();
								comboBoxMap = comboBoxDataList.get(i);

								strXxxKxzMs = comboBoxMap.get("TZ_XXXKXZ_MS") == null ? null : comboBoxMap.get("TZ_XXXKXZ_MS").toString();
								strXxxKxzCode = comboBoxMap.get("TZ_XXXKXZ_MC") == null ? null : comboBoxMap.get("TZ_XXXKXZ_MC").toString();

								final String SQL1 = "select count(*) from PS_TZ_DC_CC_T WHERE TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=? and TZ_APP_S_TEXT !=' '";
								count = sqlQuery.queryForObject(SQL1, new Object[] { wjid, strXxxBh }, "int");
								
								final String SQL2 = "select count(*) from PS_TZ_DC_CC_T WHERE TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=?  and TZ_APP_S_TEXT=?";
								countY = sqlQuery.queryForObject(SQL2, new Object[] { wjid, strXxxBh, strXxxKxzCode }, "int");
								logger.info("==count:" + count + "==countY:" + countY);
								if (count > 0) {
									// 百分比
									tempCount = Double.valueOf(decimalFormat.format((double) countY / (double) count * 100));
								} else {
									tempCount = 0;
								}
								if(strXxxKxzMs.length()>=5){
									strXxxKxzMs=strXxxKxzMs.substring(0, 5)+"...";
								}
								// 下拉框不显示分数
								if (!strRadioBoxHtml.equals("")) {
									strRadioBoxHtml = strRadioBoxHtml + "," + tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_SUB_TB2_HTML", strXxxKxzMs, String.valueOf(tempCount),arrColor[i]);
								} else {
									strRadioBoxHtml = tzGdObject.getHTMLText( "HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_SUB_TB2_HTML", strXxxKxzMs, String.valueOf(tempCount),arrColor[i]);
								}
								//计算历史得分
							    String strHisTempCount=sqlQuery.queryForObject("select TZ_HISTORY_VAL from PS_TZ_CSWJ_PCT_TBL where TZ_DC_WJ_ID=? and TZ_XXX_BH=? and TZ_XXXKXZ_MC=?", new Object[]{wjid, strXxxBh, strXxxKxzCode}, "String");
							    strHisTempCount=(strHisTempCount==null?"0":strHisTempCount.toString());
							    if (!strRadioBoxHtml2.equals("")) {
							    	strRadioBoxHtml2 = strRadioBoxHtml2 + "," + tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_SUB_TB2_HTML", strXxxKxzMs, strHisTempCount,arrColor[i]);
								} else {
									strRadioBoxHtml2 = tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_SUB_TB2_HTML", strXxxKxzMs, strHisTempCount,arrColor[i]);
								}
								if(!strCategories.equals("")){
							    	strCategories=strCategories+",'"+strXxxKxzMs+"'";
							    }else{
							    	strCategories="'"+strXxxKxzMs+"'";
							    }
								//strRadioBoxHtml2 = strRadioBoxHtml2 + tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB3_HTML", strXxxKxzMs, String.valueOf(countY), tempCount + "%");
							}
							// 拼最终统计 单选题结果的Html
							if(isMobile){
							    strDivHtml = strDivHtml + tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_TB_M_HTML",TZ_TITLE, totalCount, strClassId, strXxxBh, strRadioBoxHtml, strCategories,strRadioBoxHtml2,comboBoxAns);
							}else{
								strDivHtml = strDivHtml + tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_TB_HTML", TZ_TITLE, totalCount, strClassId, strXxxBh, strRadioBoxHtml, strCategories,strRadioBoxHtml2,comboBoxAns);	
							}
							// strRadioBoxHtml,strRadioBoxHtml2变量通用于所有控件
							// 每次用完要进行初始化
							strRadioBoxHtml = "";
							strRadioBoxHtml2 = "";
							strCategories="";
						}
					}
					
					// 量表题
					if (strComLmc != null && strComLmc.equals("QuantifyQu")) {
						avgScore = 0;
						strRadioBoxHtml = "";
						strRadioBoxHtml2 = "";
						strCategories="";
						final String quantifyQuSQL = "select TZ_XXXKXZ_MS,TZ_XXXKXZ_QZ,TZ_XXXKXZ_MC from PS_TZ_DCWJ_XXKXZ_T where TZ_DC_WJ_ID=? and  TZ_XXX_BH=? order by TZ_ORDER";

						List<Map<String, Object>> quantifyQuDataList = new ArrayList<Map<String, Object>>();
						quantifyQuDataList = sqlQuery.queryForList(quantifyQuSQL, new Object[] { wjid, strXxxBh });
						if (quantifyQuDataList != null) {
							String quantifyQuAns=sqlQuery.queryForObject("select TZ_XXXKXZ_MS from PS_TZ_DCWJ_XXKXZ_T where TZ_DC_WJ_ID=? and  TZ_XXX_BH=? and TZ_XXXKXZ_MC=(select TZ_APP_S_TEXT from PS_TZ_DC_CC_T WHERE TZ_APP_INS_ID=? and TZ_XXX_BH=?)", new Object[]{wjid,strXxxBh,strInsId,strXxxBh}, "String");

							for (int i = 0; i < quantifyQuDataList.size(); i++) {
								Map<String, Object> quantifyQuMap = new HashMap<String, Object>();
								quantifyQuMap = quantifyQuDataList.get(i);

								strXxxKxzMs = quantifyQuMap.get("TZ_XXXKXZ_MS") == null ? null : quantifyQuMap.get("TZ_XXXKXZ_MS").toString();
								strXxxKxzQz = quantifyQuMap.get("TZ_XXXKXZ_QZ") == null ? null : quantifyQuMap.get("TZ_XXXKXZ_QZ").toString();
								strXxxKxzCode = quantifyQuMap.get("TZ_XXXKXZ_MC") == null ? null : quantifyQuMap.get("TZ_XXXKXZ_MC").toString();

								final String SQL1 = "select count(*) from PS_TZ_DC_CC_T WHERE TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=? and TZ_APP_S_TEXT!=' '";
								count = sqlQuery.queryForObject(SQL1, new Object[] { wjid, strXxxBh }, "int");
								final String SQL2 = "select count(*) from PS_TZ_DC_CC_T WHERE TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=? and TZ_APP_S_TEXT=?";
								countY = sqlQuery.queryForObject(SQL2, new Object[] { wjid, strXxxBh, strXxxKxzCode }, "int");

								if (count > 0) {
									// 投票百分比
									tempCount = Double.valueOf(decimalFormat.format((double) countY / (double) count * 100));
									// 单选题平均得分
									avgScore = Double.valueOf(decimalFormat.format(avgScore+ (double) countY / (double) count * Double.valueOf(strXxxKxzQz)));// decimalFormat用于取2位小数，乘除法运算实用
								} else {
									tempCount = 0;
									avgScore = avgScore + 0;
								}
								if(strXxxKxzMs.length()>=5){
									strXxxKxzMs=strXxxKxzMs.substring(0, 5)+"...";
								}
								if (!strRadioBoxHtml.equals("")) {
									strRadioBoxHtml = strRadioBoxHtml + "," + tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_SUB_TB2_HTML",strXxxKxzMs, String.valueOf(tempCount),arrColor[i]);
								} else {
									strRadioBoxHtml = tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_SUB_TB2_HTML", strXxxKxzMs, String.valueOf(tempCount),arrColor[i]);
								}
								//计算历史得分
							    String strHisTempCount=sqlQuery.queryForObject("select TZ_HISTORY_VAL from PS_TZ_CSWJ_PCT_TBL where TZ_DC_WJ_ID=? and TZ_XXX_BH=? and TZ_XXXKXZ_MC=?", new Object[]{wjid, strXxxBh, strXxxKxzCode}, "String");
							    strHisTempCount=(strHisTempCount==null?"0":strHisTempCount.toString());
							    if (!strRadioBoxHtml2.equals("")) {
							    	strRadioBoxHtml2 = strRadioBoxHtml2 + "," + tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_SUB_TB2_HTML", strXxxKxzMs, strHisTempCount,arrColor[i]);
								} else {
									strRadioBoxHtml2 = tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_SUB_TB2_HTML", strXxxKxzMs, strHisTempCount,arrColor[i]);
								}
								if(!strCategories.equals("")){
							    	strCategories=strCategories+",'"+strXxxKxzMs+"'";
							    }else{
							    	strCategories="'"+strXxxKxzMs+"'";
							    }
						}
						// 拼最终统计 单选题结果的Html
						if(isMobile){
							strDivHtml = strDivHtml + tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_TB_M_HTML",TZ_TITLE, totalCount, strClassId, strXxxBh, strRadioBoxHtml, strCategories,strRadioBoxHtml2,quantifyQuAns);
						}else{
							strDivHtml = strDivHtml + tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_TB_HTML", TZ_TITLE, totalCount, strClassId, strXxxBh, strRadioBoxHtml, strCategories,strRadioBoxHtml2,quantifyQuAns);
						}
						
						// strRadioBoxHtml,strRadioBoxHtml2变量通用于所有控件
						// 每次用完要进行初始化
						strRadioBoxHtml = "";
						strRadioBoxHtml2 = "";
						strCategories="";
					  }
					}
					
					// 数字填空题
					if (strComLmc != null && (strComLmc.equals("DigitalCompletion"))) {
						strRadioBoxHtml = "";
						strRadioBoxHtml2 = "";
						strCategories="";
						// type用于后面合成最终结果html中 显示控件类型
		
                        int total=sqlQuery.queryForObject("select  count(TZ_APP_S_TEXT) from PS_TZ_DC_CC_T WHERE TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=? and  TZ_APP_S_TEXT<>''", new Object[]{wjid, strXxxBh}, "int");
						final String cswjPzxSQL = "select TZ_XXXKXZ_MS,TZ_L_LIMIT,TZ_U_LIMIT from PS_TZ_CSWJ_PCT_TBL where  TZ_DC_WJ_ID=? and TZ_XXX_BH=? order by TZ_ORDER";
						List<Map<String, Object>> cswjPzxDataList = new ArrayList<Map<String, Object>>();
						cswjPzxDataList = sqlQuery.queryForList(cswjPzxSQL, new Object[] { wjid, strXxxBh });
						String digitalComAns=sqlQuery.queryForObject("select  TZ_APP_S_TEXT from PS_TZ_DC_CC_T WHERE TZ_APP_INS_ID =? and  TZ_XXX_BH=?", new Object[]{strInsId,strXxxBh}, "String");
						if (cswjPzxDataList.size()>0&&cswjPzxDataList != null) {
						
							for (int i = 0; i < cswjPzxDataList.size(); i++) {
								
								Map<String, Object> completionMap = new HashMap<String, Object>();
								completionMap = cswjPzxDataList.get(i);
								
								//strAppStext = completionMap.get("TZ_APP_S_TEXT") == null ? null : completionMap.get("TZ_APP_S_TEXT").toString();
								strXxxKxzMs= completionMap.get("TZ_XXXKXZ_MS") == null ? "" : completionMap.get("TZ_XXXKXZ_MS").toString();
								float lowLimit= (float) (completionMap.get("TZ_L_LIMIT") == null ?0.0 : Float.parseFloat(completionMap.get("TZ_L_LIMIT").toString()));
								float upperLimit= (float) (completionMap.get("TZ_U_LIMIT") == null ?0.0 : Float.parseFloat(completionMap.get("TZ_U_LIMIT").toString()));
								int choseNum=0;
								if (upperLimit==0.0){
									choseNum=sqlQuery.queryForObject("select  count(TZ_APP_S_TEXT) from PS_TZ_DC_CC_T WHERE TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=? and TZ_APP_S_TEXT>=?  and TZ_APP_S_TEXT<>''", new Object[]{wjid, strXxxBh,lowLimit}, "int");
								}else{
									choseNum=sqlQuery.queryForObject("select  count(TZ_APP_S_TEXT) from PS_TZ_DC_CC_T WHERE TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=? and TZ_APP_S_TEXT>=? and TZ_APP_S_TEXT<=? and TZ_APP_S_TEXT<>''", new Object[]{wjid, strXxxBh,lowLimit,upperLimit}, "int");
								}
								 
								if (choseNum > 0) {
									// 投票百分比
									tempCount = Double.valueOf(decimalFormat.format((double) choseNum / (double) total * 100));
								} else {
									tempCount = 0;
								}
								if(strXxxKxzMs.length()>=5){
									strXxxKxzMs=strXxxKxzMs.substring(0, 5)+"...";
								}
								if(!strCategories.equals("")){
							    	strCategories=strCategories+",'"+strXxxKxzMs+"'";
							    }else{
							    	strCategories="'"+strXxxKxzMs+"'";
							    }
								
								if (!strRadioBoxHtml.equals("")) {
									strRadioBoxHtml = strRadioBoxHtml + "," + tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_SUB_TB2_HTML",strXxxKxzMs, String.valueOf(tempCount),arrColor[i]);
								} else {
									strRadioBoxHtml = tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_SUB_TB2_HTML", strXxxKxzMs, String.valueOf(tempCount),arrColor[i]);
								}
							}
							
							String histCont="";
							String TZ_CANIN_XSRS=sqlQuery.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?", new Object[]{"TZ_CANIN_XSRS"}, "String");
							String TZ_CANIN_GWJY=sqlQuery.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?", new Object[]{"TZ_CANIN_GWJY"}, "String");
							//下属人数
							if(null!=TZ_CANIN_XSRS&&strXxxBh.equals(TZ_CANIN_XSRS)){
								histCont=sqlQuery.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?", new Object[]{"TZ_CANIN_XSRH_"+wjid}, "String");
								if(null!=histCont){
								  histCont=histCont.replaceAll("X", strClassId);
								}
							}
							//岗位经验
							if(null!=TZ_CANIN_GWJY&&strXxxBh.equals(TZ_CANIN_GWJY)){
								histCont=sqlQuery.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?", new Object[]{"TZ_CANIN_GWJY_"+wjid}, "String");
								if(null!=histCont){
								 histCont=histCont.replaceAll("X", strClassId);
								}
							}
							// 拼最终统计 数字填空题结果的Html
							if(isMobile){
								strDivHtml = strDivHtml + tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_DIG_TB_M_HTML",  TZ_TITLE,totalCount, "", strXxxBh, strRadioBoxHtml, strCategories,strRadioBoxHtml2,digitalComAns,histCont);
							}else{
								strDivHtml = strDivHtml + tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_DIG_TB_HTML",  TZ_TITLE, totalCount, "", strXxxBh, strRadioBoxHtml, strCategories,strRadioBoxHtml2,digitalComAns,histCont);
							}
							// strRadioBoxHtml,strRadioBoxHtml2变量通用于所有控件
							// 每次用完要进行初始化
							strRadioBoxHtml = "";
							strRadioBoxHtml2 = "";
							strCategories="";
						}
					}

				}
			}
		}
			// 整合结果html
			logger.info("strDivHtml最终值：" + strDivHtml);
			
			//获取注册信息页面
			strCountHtml = tzGdObject.getHTMLTextForDollar("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_SUR_ANS_NEW_HTML", request.getContextPath(), String.valueOf(lastPageNo), strDivHtml,this.createPerfectUrl(wjid));
			return strCountHtml;
			
		} catch (Exception e) {
			//最外层 try catch捕捉所有异常
			e.printStackTrace();
			return null;
		}
			
	}
	
	/**
	 * 问卷编号是否合法
	 * 
	 * @param wjid 问卷编号
	 * @return
	 */
	public boolean isWjValid(String wjid) {
		if(StringUtils.isBlank(wjid)){
			return false;
		}
		String isValidwjSql = "SELECT 'Y' FROM PS_TZ_DC_WJ_DY_T WHERE TZ_DC_WJ_ID = ? limit 0,1";
		String isHas = sqlQuery.queryForObject(isValidwjSql, new Object[] { wjid },"String");
		if(StringUtils.isNotBlank(isHas) && StringUtils.equals("Y", isHas)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 根据当前登录人获取注册信息项页面URL
	 * 
	 * @return
	 */
	private String createPerfectUrl(String wjid){
		
		/*Oprid、机构编号、注册信息项是否完善、站点编号、URL*/
		String strOprid = "",strJgId = "",strIsCmpl = "N",strSiteId = "",url = "#";
		TzSession tzSession = new TzSession(request);
		boolean isMobile = CommonUtils.isMobile(request);
		Object objOprid = tzSession.getSession(userSessionName);

		if (null != objOprid) {
			strOprid = String.valueOf(objOprid);
		}else{
			String strCurUser=tzLoginServiceImpl.getLoginedManagerOprid(request);
			if (null!=strCurUser){
				strOprid=strCurUser;
			}
		}
		
		if (null != strOprid&&!"".equals(strOprid)) {
			
			String jgSql = "SELECT TZ_JG_ID,TZ_IS_CMPL FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID = ? limit 0,1";
			String siteSql = "SELECT TZ_SITEI_ID FROM PS_TZ_REG_USER_T WHERE OPRID = ? limit 0,1";

			Map<String, Object> map = sqlQuery.queryForMap(jgSql, new Object[] { strOprid });
			if(map != null){
				strJgId = map.get("TZ_JG_ID") == null?"":String.valueOf(map.get("TZ_JG_ID"));
				strIsCmpl = map.get("TZ_IS_CMPL") == null?"":String.valueOf(map.get("TZ_IS_CMPL"));
			}
			
			strSiteId = sqlQuery.queryForObject(siteSql, new Object[] { strOprid }, "String");
			
			if(StringUtils.isBlank(strJgId) || StringUtils.isBlank(strSiteId)){
				return url;
			}
			
			String encryUserName = DESUtil.encrypt(strOprid,"TZ_GD_TRANZVISION");
			
			if(StringUtils.equals("Y", strIsCmpl)){
				url = request.getContextPath() + "/user/login/" + strJgId.toLowerCase() + "/" + strSiteId;
			}else{
				if(isMobile){
					url = request.getContextPath() + "/" + strJgId.toLowerCase() + "/" + strSiteId + "/mperfect.html?userName=" + encryUserName;
				}else{
					url = request.getContextPath() + "/" + strJgId.toLowerCase() + "/" + strSiteId + "/perfect.html?userName=" + encryUserName;
				}
				
				String sql = "SELECT TZ_APP_INS_ID FROM PS_TZ_DC_INS_T WHERE TZ_DC_WJ_ID = ? AND PERSON_ID = ? ORDER BY TZ_APP_INS_ID DESC LIMIT 0,1";
				String insId = sqlQuery.queryForObject(sql, new Object[] { wjid,strOprid},"String");

				String sSql = "SELECT TZ_APP_S_TEXT FROM PS_TZ_DC_CC_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ? LIMIT 0,1";
				String dSql = "SELECT TZ_XXXKXZ_MC FROM PS_TZ_DC_DHCC_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ? AND TZ_IS_CHECKED = 'Y' LIMIT 0,1";
				if(StringUtils.isNotBlank(insId)){
					String ug = getHardCodePoint.getHardCodePointVal("TZ_CAN_UG");
					String ugVal = sqlQuery.queryForObject(sSql, new Object[] { insId,ug},"String");
					
					String province = getHardCodePoint.getHardCodePointVal("TZ_CAN_PROVINCE");
					String descSql = "SELECT TZ_XXXKXZ_MS FROM PS_TZ_DCWJ_XXKXZ_T K,PS_TZ_DC_CC_T C WHERE K.TZ_XXX_BH = C.TZ_XXX_BH AND K.TZ_XXX_BH = ? AND K.TZ_DC_WJ_ID = ? AND C.TZ_APP_INS_ID = ? AND K.TZ_XXXKXZ_MC = C.TZ_APP_S_TEXT LIMIT 0,1";
					String provinceDesc = sqlQuery.queryForObject(descSql, new Object[] { province,wjid,insId},"String");
					
					String dept = getHardCodePoint.getHardCodePointVal("TZ_CAN_DEPT");
					String deptVal = sqlQuery.queryForObject(sSql, new Object[] { insId,dept},"String");
					
					String industry = getHardCodePoint.getHardCodePointVal("TZ_CAN_INDUSTRY");
					String industryVal = sqlQuery.queryForObject(dSql, new Object[] { insId,industry},"String");
					
					PsTzRegUserT psTzRegUserT = new PsTzRegUserT();
					psTzRegUserT.setOprid(strOprid);
					psTzRegUserT.setTzSchCname(ugVal);
					psTzRegUserT.setTzLenProid(provinceDesc);
					psTzRegUserT.setTzCompanyName(deptVal);
					psTzRegUserT.setTzCompIndustry(industryVal);
					psTzRegUserTMapper.updateByPrimaryKeySelective(psTzRegUserT);
				}
			}
			return url;
		}else{
			return url;
		}
	}
}
