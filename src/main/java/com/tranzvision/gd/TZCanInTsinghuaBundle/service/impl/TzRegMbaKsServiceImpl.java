package com.tranzvision.gd.TZCanInTsinghuaBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.RegisteMalServiceImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.ValidateUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
@Service("com.tranzvision.gd.TZCanInTsinghuaBundle.service.impl.TzRegMbaKsServiceImpl")
public class TzRegMbaKsServiceImpl extends FrameworkImpl {
	Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private ValidateUtil validateUtil;
	
	@Autowired
	private GetSeqNum getSeqNum;
	
	@Autowired
	private PsoprdefnMapper psoprdefnMapper;
	
	@Autowired
	private PsTzAqYhxxTblMapper psTzAqYhxxTblMapper;
	
	@Autowired
	private PsTzLxfsInfoTblMapper psTzLxfsInfoTblMapper;
	
	@Autowired
	private  RegisteMalServiceImpl registeMalServiceImpl;
	
	@Autowired
	private PsTzRegUserTMapper psTzRegUserTMapper;
	
	@Autowired
	private PsroleuserMapper psroleuserMapper;
	public String saveEnrollInfo(String strParams, String[] errMsg) {
		String strResult = "failure";
		String strOrgId = "";
		String strSiteId = "";
		String strLang = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			
			strOrgId = jacksonUtil.getString("orgid");
			strSiteId = jacksonUtil.getString("siteId");
			strLang = jacksonUtil.getString("lang");
			if (strOrgId == null || "".equals(strOrgId)) {
				errMsg[0] = "100";
				errMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgId, strLang, "TZ_SITE_MESSAGE", "55",
						"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
			}

			if (jacksonUtil.containsKey("data")) {
				Map<String, Object> dataMap = jacksonUtil.getMap("data");
				// Oprid;
				String oprId = "";
				if (dataMap.containsKey("OPRID")) {
					oprId = "MBA_" + ((String) dataMap.get("OPRID")).trim();
				}
				// 姓名;
				String strTZ_REALNAME = "";
				if (dataMap.containsKey("TZ_REALNAME")) {
					strTZ_REALNAME = ((String) dataMap.get("TZ_REALNAME")).trim();
				}

				// FIRST_NAME;
				String strTZ_FIRST_NAME = "";
				if (dataMap.containsKey("TZ_FIRST_NAME")) {
					strTZ_FIRST_NAME = ((String) dataMap.get("TZ_FIRST_NAME")).trim();
				}

				// LAST_NAME;
				String strTZ_LAST_NAME = "";
				if (dataMap.containsKey("TZ_LAST_NAME")) {
					strTZ_LAST_NAME = ((String) dataMap.get("TZ_LAST_NAME")).trim();
				}

				// 性别;
				String strTZ_GENDER = "";
				if (dataMap.containsKey("TZ_GENDER")) {
					strTZ_GENDER = ((String) dataMap.get("TZ_GENDER")).trim();
				}

				// 出生日期;
				String strBIRTHDATE = "";
				if (dataMap.containsKey("BIRTHDATE")) {
					strBIRTHDATE = ((String) dataMap.get("BIRTHDATE")).trim();
				}

				// 确认密码;
				String strTZ_REPASSWORD = "";
				if (dataMap.containsKey("TZ_REPASSWORD")) {
					strTZ_REPASSWORD = ((String) dataMap.get("TZ_REPASSWORD")).trim();
				}

				// 邮箱;
				String strTZ_EMAIL = "";
				if (dataMap.containsKey("TZ_EMAIL")) {
					strTZ_EMAIL = ((String) dataMap.get("TZ_EMAIL")).trim();
				}

				// 手机;
				String strTZ_MOBILE = "";
				if (dataMap.containsKey("TZ_MOBILE")) {
					strTZ_MOBILE = ((String) dataMap.get("TZ_MOBILE")).trim();
				}

				// SKYPE账号;
				String strTZ_SKYPE = "";
				if (dataMap.containsKey("TZ_SKYPE")) {
					strTZ_SKYPE = ((String) dataMap.get("TZ_SKYPE")).trim();
				}

				// 行业类型;
				String strTZ_COMP_INDUSTRY = "";
				if (dataMap.containsKey("TZ_COMP_INDUSTRY")) {
					strTZ_COMP_INDUSTRY = ((String) dataMap.get("TZ_COMP_INDUSTRY")).trim();
				}

				// 公司名称;
				String strTZ_COMPANY_NAME = "";
				if (dataMap.containsKey("TZ_COMPANY_NAME")) {
					strTZ_COMPANY_NAME = ((String) dataMap.get("TZ_COMPANY_NAME")).trim();
				}

				// 部门;
				String strTZ_DEPTMENT = "";
				if (dataMap.containsKey("TZ_DEPTMENT")) {
					strTZ_DEPTMENT = ((String) dataMap.get("TZ_DEPTMENT")).trim();
				}
				//面试申请号
				String tzMshId = "";
				if (dataMap.containsKey("TZ_MSH_ID")) {
					tzMshId = ((String) dataMap.get("TZ_MSH_ID")).trim();
				}
				
				// 职务;
				String strTZ_ZHIWU = "";
				if (dataMap.containsKey("TZ_ZHIWU")) {
					strTZ_ZHIWU = ((String) dataMap.get("TZ_ZHIWU")).trim();
				}

				// 时区;
				String strTZ_TIME_ZONE = "";
				if (dataMap.containsKey("TZ_TIMEZONE")) {
					strTZ_TIME_ZONE = ((String) dataMap.get("TZ_TIMEZONE")).trim();
				}

				// 国籍;
				String strTZ_COUNTRY = "";
				if (dataMap.containsKey("TZ_COUNTRY")) {
					strTZ_COUNTRY = ((String) dataMap.get("TZ_COUNTRY")).trim();
				}

				// 常住省;
				String strTZ_LEN_PROID = "";
				if (dataMap.containsKey("TZ_LEN_PROID")) {
					strTZ_LEN_PROID = ((String) dataMap.get("TZ_LEN_PROID")).trim();
				}

				// 常驻城市;
				String strTZ_LEN_CITY = "";
				if (dataMap.containsKey("TZ_LEN_CITY")) {
					strTZ_LEN_CITY = ((String) dataMap.get("TZ_LEN_CITY")).trim();
				}

				// 证件类型;
				String strNATIONAL_ID_TYPE = "";
				if (dataMap.containsKey("NATIONAL_ID_TYPE")) {
					strNATIONAL_ID_TYPE = ((String) dataMap.get("NATIONAL_ID_TYPE")).trim();
				}

				// 证件编号;
				String strNATIONAL_ID = "";
				if (dataMap.containsKey("NATIONAL_ID")) {
					strNATIONAL_ID = ((String) dataMap.get("NATIONAL_ID")).trim();
				}

				// 毕业院校所属国家;
				String strTZ_SCH_CNAME_COUNTRY = "";
				if (dataMap.containsKey("TZ_SCH_COUNTRY")) {
					strTZ_SCH_CNAME_COUNTRY = ((String) dataMap.get("TZ_SCH_COUNTRY")).trim();
				}
				
				// 毕业院校;
				String strTZ_SCH_CNAME = "";
				if (dataMap.containsKey("TZ_SCH_CNAME")) {
					strTZ_SCH_CNAME = ((String) dataMap.get("TZ_SCH_CNAME")).trim();
				}
				

				// 专业;
				String strTZ_SPECIALTY = "";
				if (dataMap.containsKey("TZ_SPECIALTY")) {
					strTZ_SPECIALTY = ((String) dataMap.get("TZ_SPECIALTY")).trim();
				}

				// 最高学历;
				String strTZ_HIGHEST_EDU = "";
				if (dataMap.containsKey("TZ_HIGHEST_EDU")) {
					strTZ_HIGHEST_EDU = ((String) dataMap.get("TZ_HIGHEST_EDU")).trim();
				}
				
				//项目;
				String strTZ_PRJ_ID = "";
				if (dataMap.containsKey("TZ_PROJECT")) {
					strTZ_PRJ_ID = ((String) dataMap.get("TZ_PROJECT")).trim();
				}
				
				String strTZ_MSSQH = "";
				if (dataMap.containsKey("TZ_MSSQH")) {
					strTZ_MSSQH = ((String) dataMap.get("TZ_MSSQH")).trim();
				}
				

				// 预留字段1;
				String strTZ_COMMENT1 = "";
				if (dataMap.containsKey("TZ_COMMENT1")) {
					strTZ_COMMENT1 = ((String) dataMap.get("TZ_COMMENT1")).trim();
				}

				// 预留字段2;
				String strTZ_COMMENT2 = "";
				if (dataMap.containsKey("TZ_COMMENT2")) {
					strTZ_COMMENT2 = ((String) dataMap.get("TZ_COMMENT2")).trim();
				}

				// 预留字段3;
				String strTZ_COMMENT3 = "";
				if (dataMap.containsKey("TZ_COMMENT3")) {
					strTZ_COMMENT3 = ((String) dataMap.get("TZ_COMMENT3")).trim();
				}

				// 预留字段4;
				String strTZ_COMMENT4 = "";
				if (dataMap.containsKey("TZ_COMMENT4")) {
					strTZ_COMMENT4 = ((String) dataMap.get("TZ_COMMENT4")).trim();
				}

				// 预留字段5;
				String strTZ_COMMENT5 = "";
				if (dataMap.containsKey("TZ_COMMENT5")) {
					strTZ_COMMENT5 = ((String) dataMap.get("TZ_COMMENT5")).trim();
				}

				// 预留字段6;
				String strTZ_COMMENT6 = "";
				if (dataMap.containsKey("TZ_COMMENT6")) {
					strTZ_COMMENT6 = ((String) dataMap.get("TZ_COMMENT6")).trim();
				}

				// 预留字段7;
				String strTZ_COMMENT7 = "";
				if (dataMap.containsKey("TZ_COMMENT7")) {
					strTZ_COMMENT7 = ((String) dataMap.get("TZ_COMMENT7")).trim();
				}

				// 预留字段8;
				String strTZ_COMMENT8 = "";
				if (dataMap.containsKey("TZ_COMMENT8")) {
					strTZ_COMMENT8 = ((String) dataMap.get("TZ_COMMENT8")).trim();
				}

				// 预留字段9;
				String strTZ_COMMENT9 = "";
				if (dataMap.containsKey("TZ_COMMENT9")) {
					strTZ_COMMENT9 = ((String) dataMap.get("TZ_COMMENT9")).trim();
				}

				// 预留字段10;
				String strTZ_COMMENT10 = "";
				if (dataMap.containsKey("TZ_COMMENT10")) {
					strTZ_COMMENT10 = ((String) dataMap.get("TZ_COMMENT10")).trim();
				}

				// 验证方式，默认邮箱;
				String strActivateType = "E";
				/*
				// 校验机构会员数据项--不能为空;
				String strTemV = "";
				String sqlMemberDatas = "SELECT TZ_REG_FIELD_ID,TZ_REG_FIELD_NAME,TZ_IS_REQUIRED,TZ_ENABLE FROM PS_TZ_REG_FIELD_T WHERE TZ_ENABLE='Y' AND TZ_SITEI_ID=? ORDER BY TZ_ORDER ASC";
				List<Map<String, Object>> list = sqlQuery.queryForList(sqlMemberDatas, new Object[] { strSiteId });
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String regFieldId = (String) list.get(i).get("TZ_REG_FIELD_ID");
						String regFieldName = (String) list.get(i).get("TZ_REG_FIELD_NAME");
						String required = (String) list.get(i).get("TZ_IS_REQUIRED");
						String enable = (String) list.get(i).get("TZ_ENABLE");
						if (dataMap.containsKey(regFieldId)) {
							strTemV = (String) dataMap.get(regFieldId) == null ? ""
									: ((String) dataMap.get(regFieldId)).trim();
							if ("".equals(strTemV) && "Y".equals(required) && "Y".equals(enable)) {
								errMsg[0] = "2";
								errMsg[1] = regFieldName + validateUtil.getMessageTextWithLanguageCd(strOrgId, strLang,
										"TZ_SITE_MESSAGE", "26", "不能为空", "cannot be blank");
								return strResult;
							}
						}

						// 如果字段是邮箱，增加校验格式;
						if ("TZ_EMAIL".equals(regFieldId)) {
							String strEmailParas = "{\"email\":\"" + strTemV + "\",\"orgid\":\"" + strOrgId
									+ "\",\"lang\":\"" + strLang + "\",\"sen\":\"1\"}";
							String strEmailResult = registeMalServiceImpl.emailVerifyByEnroll(strEmailParas, errMsg);
							if (!"0".equals(errMsg[0])) {
								strResult = strEmailResult;
								return strResult;
							}
						}

						// 如果字段是手机,增加校验格式;如果手机为非必填，则不需要校验手机格式;
						if ("TZ_MOBILE".equals(regFieldId) && "Y".equals(required)) {
							String strPhoneParas = "{\"phone\":\"" + strTemV + "\",\"orgid\":\"" + strOrgId
									+ "\",\"lang\":\"" + strLang + "\",\"sen\":\"1\"}";
							String strPhoneResult = registeSmsServiceImpl.smsVerifyByActive(strPhoneParas, errMsg);
							if (!"0".equals(errMsg[0])) {
								strResult = strPhoneResult;
								return strResult;
							}
						}
					}
				}
				*/
				String strEmailParas = "{\"email\":\"" + strTZ_EMAIL + "\",\"orgid\":\"" + strOrgId
						+ "\",\"lang\":\"" + strLang + "\",\"sen\":\"1\"}";
				String strEmailResult = registeMalServiceImpl.emailVerifyByEnroll(strEmailParas, errMsg);
				if (!"0".equals(errMsg[0])) {
					strResult = "failure";
					return strResult;
				}
				// 定义激活状态，默认激活;
				String strActiveStatus = "Y";

				// TZ_REALNAME或（FIRST_NAME/LAST_NAME) 必填一项;
				if (strTZ_REALNAME == null || "".equals(strTZ_REALNAME)) {
					strTZ_REALNAME =  oprId;
				}

				// 通过所有校验，开始创建账号;
				// 生成登录账号;
				String dlzh = this.tzGenerateAcount(strOrgId, oprId);

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
				psTzAqYhxxTbl.setTzRealname(strTZ_REALNAME);
				psTzAqYhxxTbl.setTzEmail(strTZ_EMAIL);
				psTzAqYhxxTbl.setTzMobile(strTZ_MOBILE);
				psTzAqYhxxTbl.setTzRylx("ZCYH");
				if ("M".equals(strActivateType)) {
					psTzAqYhxxTbl.setTzSjbdBz("Y");
				}
				if ("E".equals(strActivateType)) {
					psTzAqYhxxTbl.setTzYxbdBz("Y");
				}
				psTzAqYhxxTbl.setTzJihuoZt(strActiveStatus);
				psTzAqYhxxTbl.setTzJihuoFs(strActivateType);
				psTzAqYhxxTbl.setTzZhceDt(new Date());
				psTzAqYhxxTbl.setTzBjsEml("N");
				psTzAqYhxxTbl.setTzBjsSms("N");
				//注册产生的账号默认为完善
				psTzAqYhxxTbl.setTzIsCmpl("Y");
				//产生面试申请号，流水号格式：yyyy+00001
//				Calendar date=Calendar.getInstance();
//				String currentYear = String.valueOf(date.get(Calendar.YEAR));
//				String xuhao = "0000" + getSeqNum.getSeqNum(currentYear, "TZ_MSH_ID");
//				xuhao = xuhao.substring(xuhao.length()-5);
//				String tzMshId = currentYear + xuhao;
				psTzAqYhxxTbl.setTzMshId(tzMshId);
				psTzAqYhxxTbl.setRowAddedDttm(new Date());
				psTzAqYhxxTbl.setRowAddedOprid(oprId);
				psTzAqYhxxTbl.setRowLastmantDttm(new Date());
				psTzAqYhxxTbl.setRowLastmantOprid(oprId);				
				psTzAqYhxxTblMapper.insert(psTzAqYhxxTbl);

				// 通过所有校验，保存联系方式;
				PsTzLxfsInfoTbl psTzLxfsInfoTbl = new PsTzLxfsInfoTbl();
				psTzLxfsInfoTbl.setTzLxfsLy("ZCYH");
				psTzLxfsInfoTbl.setTzLydxId(oprId);
				psTzLxfsInfoTbl.setTzZyEmail(strTZ_EMAIL);
				psTzLxfsInfoTbl.setTzZySj(strTZ_MOBILE);
				psTzLxfsInfoTbl.setTzSkype(strTZ_SKYPE);
				psTzLxfsInfoTblMapper.insert(psTzLxfsInfoTbl);

				// 通过所有校验，保存用户注册信息;
				PsTzRegUserT psTzRegUserT = new PsTzRegUserT();
				psTzRegUserT.setOprid(oprId);
				psTzRegUserT.setTzSiteiId(strSiteId);
				psTzRegUserT.setTzRealname(strTZ_REALNAME);
				psTzRegUserT.setTzGender(strTZ_GENDER);
				psTzRegUserT.setTzSkype(strTZ_SKYPE);
				psTzRegUserT.setTzFirstName(strTZ_FIRST_NAME);
				psTzRegUserT.setTzLastName(strTZ_LAST_NAME);
				psTzRegUserT.setTzCompanyName(strTZ_COMPANY_NAME);
				psTzRegUserT.setTzCompIndustry(strTZ_COMP_INDUSTRY);
				psTzRegUserT.setTzDeptment(strTZ_DEPTMENT);
				psTzRegUserT.setTzZhiwu(strTZ_ZHIWU);
				psTzRegUserT.setTzTimezone(strTZ_TIME_ZONE);
				psTzRegUserT.setTzCountry(strTZ_COUNTRY);
				psTzRegUserT.setTzLenProid(strTZ_LEN_PROID);
				psTzRegUserT.setTzLenCity(strTZ_LEN_CITY);
				psTzRegUserT.setNationalIdType(strNATIONAL_ID_TYPE);
				psTzRegUserT.setNationalId(strNATIONAL_ID);

				SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
				if (strBIRTHDATE != null && !"".equals(strBIRTHDATE)) {
					try {
						Date bthDate = dateFormate.parse(strBIRTHDATE);
						psTzRegUserT.setBirthdate(bthDate);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}				
				psTzRegUserT.setTzSchCountry(strTZ_SCH_CNAME_COUNTRY);
				psTzRegUserT.setTzSchCname(strTZ_SCH_CNAME);
				psTzRegUserT.setTzSpecialty(strTZ_SPECIALTY);
				psTzRegUserT.setTzHighestEdu(strTZ_HIGHEST_EDU);
				psTzRegUserT.setTzPrjId(strTZ_PRJ_ID);
				psTzRegUserT.setTzComment1(strTZ_COMMENT1);
				psTzRegUserT.setTzComment2(strTZ_COMMENT2);
				psTzRegUserT.setTzComment3(strTZ_COMMENT3);
				psTzRegUserT.setTzComment4(strTZ_COMMENT4);
				psTzRegUserT.setTzComment5(strTZ_COMMENT5);
				psTzRegUserT.setTzComment6(strTZ_COMMENT6);
				psTzRegUserT.setTzComment7(strTZ_COMMENT7);
				psTzRegUserT.setTzComment8(strTZ_COMMENT8);
				psTzRegUserT.setTzComment9(strTZ_COMMENT9);
				psTzRegUserT.setTzComment10(strTZ_COMMENT10);
				psTzRegUserT.setRowAddedDttm(new Date());
				psTzRegUserT.setRowAddedOprid(oprId);
				psTzRegUserT.setRowLastmantDttm(new Date());
				psTzRegUserT.setRowLastmantOprid(oprId);
				
				//是否生产面试申请号;
				if(strTZ_MSSQH != null && "CREATE".equals(strTZ_MSSQH)){
					Calendar a = Calendar.getInstance();
					String year = String.valueOf(a.get(Calendar.YEAR));//得到年
					String sjNum = "0000"+ String.valueOf(getSeqNum.getSeqNumOracle("TZ_REG_USER_T", "TZ_MSSQH"+year));
					String mssqh = year + sjNum.substring(sjNum.length()-4,sjNum.length());
					psTzRegUserT.setTzMssqh(mssqh);
				}
				
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

				Map<String, Object> returnMap = new HashMap<>();
				returnMap.put("result", "success");
				strResult = jacksonUtil.Map2json(returnMap);
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
	public String tzGenerateAcount(String jgId, String oprId) {
		String accountId = oprId;
		// 查看账号是否已经被占用;
		boolean bl = true;
		while (bl) {
			String sql = "select COUNT(1) from PS_TZ_AQ_YHXX_TBL where TZ_DLZH_ID = ? AND TZ_JG_ID = ?";
			int count = sqlQuery.queryForObject(sql, new Object[] { accountId, jgId }, "Integer");
			if (count == 0 && accountId != null && !"".equals(accountId)) {
				bl = false;
			} else {
				accountId = "MBA_" + getSeqNum.getSeqNum("PSOPRDEFN", "OPRID");
			}
		}

		return accountId;
	}
	
	/**
	 * 批量注册账号
	 * 
	 * @param max 最大Oprid编号
	 * @param min 最新Oprid编号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String reg(int max, int min) {
		try {
			//列表SQL
			String sql = "SELECT * FROM PS_TZ_MBASLFRG_TBL WHERE cast(OPRID as unsigned int) >= ? AND cast(OPRID as unsigned int) < ?  AND CONCAT('MBA_',OPRID) NOT IN (SELECT OPRID FROM PSOPRDEFN WHERE OPRID REGEXP BINARY 'MBA_*') order by cast(OPRID as unsigned int)";
			List<?> resultlist = sqlQuery.queryForList(sql, new Object[] { min,max });
			String[] errMsg = { "0", "" };
			Map<String, Object> mapRetJson = new HashMap<String, Object>();
			JacksonUtil jacksonUtil = new JacksonUtil();
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			for (Object obj : resultlist) {
				errMsg[0] = "0";
				errMsg[1] = "";
				Map<String, Object> result = (Map<String, Object>) obj;
				String attrOprid = result.get("OPRID") == null ? "" : String.valueOf(result.get("OPRID"));
				String attrEmail = result.get("TZ_EMAIL") == null ? "" : String.valueOf(result.get("TZ_EMAIL"));
				String attrMobile = result.get("TZ_MOBILE") == null ? "" : String.valueOf(result.get("TZ_MOBILE"));
				String attrRealName = result.get("TZ_REALNAME") == null ? "" : String.valueOf(result.get("TZ_REALNAME"));
				String attrSex = result.get("TZ_GENDER") == null ? "" : String.valueOf(result.get("TZ_GENDER"));
				String attrSchName = result.get("TZ_SCH_CNAME") == null ? "" : String.valueOf(result.get("TZ_SCH_CNAME"));
				String attrCity = result.get("TZ_LEN_CITY") == null ? "" : String.valueOf(result.get("TZ_LEN_CITY"));
				String attrCompName = result.get("TZ_COMPANY_NAME") == null ? "" : String.valueOf(result.get("TZ_COMPANY_NAME"));
				String attrCompInd = result.get("TZ_COMP_INDUSTRY") == null ? "" : String.valueOf(result.get("TZ_COMP_INDUSTRY"));
				String attrMshId = result.get("TZ_MSH_ID") == null ? "" : String.valueOf(result.get("TZ_MSH_ID"));
				
				String pross = (Math.random() * 10000 + "").replace(".", "").substring(0, 6);  

				paramsMap.put("orgid", "SEM");
				paramsMap.put("siteId", "72");
				paramsMap.put("lang", "ZHS");
				
				Map<String, Object> mapData = new HashMap<String, Object>();
				mapData.put("OPRID", attrOprid);
				mapData.put("TZ_REALNAME", attrRealName);
				mapData.put("TZ_PASSWORD", pross);
				mapData.put("TZ_REPASSWORD", pross);
				mapData.put("TZ_EMAIL", attrEmail);
				mapData.put("TZ_MOBILE", attrMobile);
				mapData.put("TZ_COMPANY_NAME", attrCompName);
				mapData.put("TZ_COMP_INDUSTRY", attrCompInd);
				mapData.put("TZ_LEN_CITY", attrCity);
				mapData.put("TZ_SCH_CNAME",attrSchName);
				mapData.put("TZ_GENDER",attrSex);
				mapData.put("TZ_MSH_ID", attrMshId);
				paramsMap.put("data", mapData);
				String strParams = jacksonUtil.Map2json(paramsMap);
				
//				logger.info(attrOprid + " Params-----> " + strParams);
				String strResult = this.saveEnrollInfo(strParams , errMsg);
//				logger.info(attrOprid + " Result-----> " + strResult);
				if(StringUtils.equals(strResult, "failure")){
					mapRetJson.put(attrOprid, errMsg[1]);
				}
			}
			
			String error = jacksonUtil.Map2json(mapRetJson);
			return error;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 删除指定的账号信息
	 * 
	 * @param paramOprid
	 * @return
	 */
	public String delete(String paramOprid) {
		paramOprid = "MBA_" + paramOprid;
		String sql1 = "DELETE FROM PSOPRDEFN WHERE OPRID = ?";
		String sql2 = "DELETE FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID = ?";
		String sql3 = "DELETE FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LYDX_ID = ?";
		String sql4 = "DELETE FROM PS_TZ_REG_USER_T WHERE OPRID = ?";
		String sql5 = "DELETE FROM PSROLEUSER WHERE ROLEUSER = ?";
		
		int del1 = sqlQuery.update(sql1, new Object[]{paramOprid});
		int del2 = sqlQuery.update(sql2, new Object[]{paramOprid});
		int del3 = sqlQuery.update(sql3, new Object[]{paramOprid});
		int del4 = sqlQuery.update(sql4, new Object[]{paramOprid});
		int del5 = sqlQuery.update(sql5, new Object[]{paramOprid});
		
		String ret = paramOprid + "-->" + del1 + "    -->" + del2 + "    -->" + del3 + "    -->" + del4 + "    -->" + del5;
		return ret;
	}
	
	/**
	 * 删除重复数据
	 * @return
	 */
	public String delRepeat() {
		String sql = "SELECT M.OPRID,M.TZ_EMAIL FROM PS_TZ_MBASLFRG_TBL M,(SELECT LOWER(TZ_EMAIL) AS TZ_EMAIL,COUNT(1) FROM PS_TZ_MBASLFRG_TBL GROUP BY LOWER(TZ_EMAIL) HAVING COUNT(1) > 1 ) C WHERE LOWER(M.TZ_EMAIL) = LOWER(C.TZ_EMAIL) ORDER BY LOWER(M.TZ_EMAIL) DESC,OPRID DESC";
		List<?> resultlist = sqlQuery.queryForList(sql);
		String tmpEmail = "";
		String msg = "";
		
		for (Object obj : resultlist) {
			Map<String, Object> result = (Map<String, Object>) obj;
			String attrOprid = result.get("OPRID") == null ? "" : String.valueOf(result.get("OPRID"));
			String attrEmail = result.get("TZ_EMAIL") == null ? "" : String.valueOf(result.get("TZ_EMAIL"));

			attrEmail = StringUtils.lowerCase(attrEmail);
			
			if(StringUtils.equals(tmpEmail, attrEmail)){
				int del = sqlQuery.update("DELETE FROM PS_TZ_MBASLFRG_TBL WHERE OPRID = ?", new Object[]{attrOprid});
				msg = msg + "<br>OPRID : " + attrOprid + "    -->  EMAIL : " + attrEmail + "   --> DELE :  = " + del;
			}else{
				tmpEmail = attrEmail;
			}
		}
		return msg;
	}
	public String delAll() {

		String sql1 = "DELETE FROM PSOPRDEFN WHERE OPRID REGEXP BINARY 'MBA_*'";
		String sql2 = "DELETE FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID REGEXP BINARY 'MBA_*'";
		String sql3 = "DELETE FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LYDX_ID REGEXP BINARY 'MBA_*'";
		String sql4 = "DELETE FROM PS_TZ_REG_USER_T WHERE OPRID REGEXP BINARY 'MBA_*'";
		String sql5 = "DELETE FROM PSROLEUSER WHERE ROLEUSER REGEXP BINARY 'MBA_*'";
		
		int del1 = sqlQuery.update(sql1);
		int del2 = sqlQuery.update(sql2);
		int del3 = sqlQuery.update(sql3);
		int del4 = sqlQuery.update(sql4);
		int del5 = sqlQuery.update(sql5);
		
		String ret = "-->" + del1 + "    -->" + del2 + "    -->" + del3 + "    -->" + del4 + "    -->" + del5;
		return ret;
	}
}
