package com.tranzvision.gd.TZCanInTsinghuaBundle.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsTzAqYhxxTblMapper;
import com.tranzvision.gd.TZAccountMgBundle.dao.PsoprdefnMapper;
import com.tranzvision.gd.TZAccountMgBundle.dao.PsroleuserMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTbl;
import com.tranzvision.gd.TZAccountMgBundle.model.Psoprdefn;
import com.tranzvision.gd.TZAccountMgBundle.model.Psroleuser;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzLxfsInfoTblMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzRegUserTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzLxfsInfoTbl;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzRegUserT;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteEnrollClsServiceImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.ValidateUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.captcha.Patchca;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.session.TzSession;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
/**
 * @author WRL  测测是否能上清华
 */
@Service("com.tranzvision.gd.TZCanInTsinghuaBundle.service.impl.TzCanInTsinghuaClsServiceImpl")
public class TzCanInTsinghuaClsServiceImpl extends FrameworkImpl {

	@Autowired
	private TZGDObject tzGdObject;

	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private GetSeqNum getSeqNum;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ValidateUtil validateUtil;
	
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
		String strOrgId = "";
		String strSiteId = "";
		String strLang = "";
		String oprid = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
//			strOrgId = jacksonUtil.getString("orgid");
//			strSiteId = jacksonUtil.getString("siteId");
//			strLang = jacksonUtil.getString("lang");
			strOrgId = "SEM";
			strSiteId = "27";
			strLang = "ZHS";
			if (strOrgId == null || "".equals(strOrgId)) {
				errMsg[0] = "100";
				errMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgId, strLang, "TZ_SITE_MESSAGE", "55",
						"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
			}

			if (jacksonUtil.containsKey("data")) {
				Map<String, Object> dataMap = jacksonUtil.getMap("data");

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
						errMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgId, strLang, "TZGD_FWINIT_MSGSET", "55",
								"输入的验证码不正确", "Security code is incorrect.");
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
						errMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgId, strLang, "TZGD_FWINIT_MSGSET", "55",
								"登录失败，请确认用户名和密码是否正确。", "Email address or password is incorrect !");
						return strResult;
					}
				}else{
					//未注册用户
					// 验证密码和确认密码是否一致;
					if (userPwd == null || !userPwd.equals(strTZ_REPASSWORD)) {
						errMsg[0] = "3";
						errMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgId, strLang, "TZ_SITE_MESSAGE", "55",
								"密码和确认密码不一致", "New Password and Confirm Password is not consistent");
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
			errMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgId, strLang, "TZ_SITE_MESSAGE", "55",
					"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
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
		String strEmail = "";
		String strOrgid = "sem";
		String strResult = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();   
		try{
			jacksonUtil.json2Map(strParams);
			if(jacksonUtil.containsKey("email")){
				strEmail = jacksonUtil.getString("email").trim();
	      	
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
		String isMobile = "";
		String strResponse = "获取数据失败，请联系管理员";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("isMobile")){
				isMobile = jacksonUtil.getString("isMobile").trim();
			}

			// 考生可参与的有效的测试方向
			return this.createTestDirection(isMobile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResponse;
	}

	/**
	 * 当前考生能参与的测试方向
	 * 
	 * @param isMobile
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String createTestDirection(String isMobile) {
		String strHtml = "获取数据失败，请联系管理员";

		try {
			/*当前考生oprid*/
			String strOprid = "";
			TzSession tzSession = new TzSession(request);
			Object objOprid = tzSession.getSession(userSessionName);

			if (null != objOprid) {
				strOprid = String.valueOf(objOprid);
			}
			
			/*获取机构编号*/
			String jsSql = "SELECT TZ_JG_ID FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID = ? limit 0,1";
			String jgid = sqlQuery.queryForObject(jsSql, new Object[] { strOprid},"String");
			if(StringUtils.isBlank(jgid) || StringUtils.isBlank(strOprid)){
				return strHtml;
			}

			//列表SQL
			String sql = "SELECT TZ_CS_WJ_ID,TZ_CS_WJ_NAME,TZ_DC_WJ_ID, date_format(TZ_DC_WJ_JSSJ,'%H:%i:%s') TZ_DC_WJ_KSSJ,date_format(TZ_DC_WJ_JSSJ,'%H:%i:%s') TZ_DC_WJ_JSSJ FROM PS_TZ_CSWJ_TBL WHERE TZ_JG_ID = ? AND TZ_STATE = 'Y' AND TZ_DC_WJ_ZT = '1' AND TZ_DC_WJ_KSRQ <= curdate() AND TZ_DC_WJ_JSRQ >= curdate() ORDER BY TZ_DC_WJ_KSRQ";
			List<?> resultlist = sqlQuery.queryForList(sql, new Object[] { jgid});
			
			DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss"); 
			Date nowTime = new Date();
			
			strHtml = "";
			
			for (Object obj : resultlist) {
				Map<String, Object> result = (Map<String, Object>) obj;
	
				String attrCsWjId = result.get("TZ_CS_WJ_ID") == null ? "" : String.valueOf(result.get("TZ_CS_WJ_ID"));
				String attrCsWjName = result.get("TZ_CS_WJ_NAME") == null ? "" : String.valueOf(result.get("TZ_CS_WJ_NAME"));
				String attrWjId = result.get("TZ_DC_WJ_ID") == null ? "" : String.valueOf(result.get("TZ_DC_WJ_ID"));
				String attrDcWjKssj = result.get("TZ_DC_WJ_KSSJ")==null?"09:00:00":String.valueOf(result.get("TZ_DC_WJ_KSSJ"));
				String attrDcWjJssj = result.get("TZ_DC_WJ_JSSJ")==null?"18:00:00":String.valueOf(result.get("TZ_DC_WJ_JSSJ"));
				if(timeFormat.parse(attrDcWjKssj).getTime() > nowTime.getTime() || timeFormat.parse(attrDcWjJssj).getTime() < nowTime.getTime()){
					continue;
				}
				String wjUrlSql = "SELECT TZ_DC_WJ_URL FROM PS_TZ_DC_WJ_DY_T WHERE TZ_DC_WJ_ID = ? limit 0,1";
				String wjUrl = sqlQuery.queryForObject(wjUrlSql, new Object[] { attrWjId },"String");
				String strTr = tzGdObject.getHTMLText("HTML.TZCanInTsinghuaBundle.TZ_CAN_TSINGHUA_TR",attrCsWjId,attrCsWjName,wjUrl);

				strHtml = strHtml + strTr;
			}
		}catch(Exception e){
	    	e.printStackTrace();
	    }
		
		if(StringUtils.isBlank(strHtml)){
			strHtml = "<tr><td><span'>没有可选择的问卷！</span></td></tr>";
		}
		
		return strHtml;
	}
}
