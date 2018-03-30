package com.tranzvision.gd.TZLeaguerAccountBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsoprdefnMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.Psoprdefn;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzRegUserTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzFsT;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzLkbmT;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzMsjgT;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzMszgT;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzQtT;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzRegUserT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.TzImpClpsTblMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.TzImpMspsTblMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.TzImpLkbmTblMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.TzImpYlqTblMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.TzImpClpsTbl;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.TzImpMspsTbl;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.TzImpLkbmTbl;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.TzImpYlqTbl;

import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzFsT;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzLkbmT;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzMsjgT;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzMszgT;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzQtT;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzFsTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzLkbmTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzMsjgTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzMszgTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzQtTMapper;

/**
 * 申请用户信息；原：TZ_GD_USERGL_PKG:TZ_GD_USER_CLS
 * 
 * @author tang
 * @since 2015-11-20
 */
@Service("com.tranzvision.gd.TZLeaguerAccountBundle.service.impl.LeaguerMshLchServiceImpl")
public class LeaguerMshLchServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsTzRegUserTMapper psTzRegUserTMapper;
	@Autowired
	private TzImpClpsTblMapper TzImpClpsTblMapper;
	@Autowired
	private TzImpMspsTblMapper TzImpMspsTblMapper;
	@Autowired
	private TzImpLkbmTblMapper TzImpLkbmTblMapper;
	@Autowired
	private TzImpYlqTblMapper TzImpYlqTblMapper;
	@Autowired
	private PsoprdefnMapper psoprdefnMapper;
	@Autowired
	private PsTzFsTMapper psTzFsTMapper;
	@Autowired
	private PsTzLkbmTMapper psTzLkbmTMapper;
	@Autowired
	private PsTzMsjgTMapper psTzMsjgTMapper;
	@Autowired
	private PsTzMszgTMapper psTzMszgTMapper;
	@Autowired
	private PsTzQtTMapper psTzQtTMapper;
	
	/* 查询表单信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "{}");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("OPRID")) {
				
				// oprid;
				String str_oprid = jacksonUtil.getString("OPRID");
				String appIns = jacksonUtil.getString("appInsID");
				
				// 头像地址;
				String titleImageUrlSQL = "SELECT B.TZ_ATT_A_URL,A.TZ_ATTACHSYSFILENA FROM PS_TZ_OPR_PHT_GL_T A , PS_TZ_OPR_PHOTO_T B WHERE A.OPRID=? AND A.TZ_ATTACHSYSFILENA = B.TZ_ATTACHSYSFILENA";
				Map<String , Object> imgMap = jdbcTemplate.queryForMap(titleImageUrlSQL,new Object[]{str_oprid});
				String titleImageUrl = "";
				if(imgMap != null){
					String tzAttAUrl = (String)imgMap.get("TZ_ATT_A_URL");
					String sysImgName = (String)imgMap.get("TZ_ATTACHSYSFILENA");
					if(tzAttAUrl != null &&!"".equals(tzAttAUrl)
						&& sysImgName != null &&!"".equals(sysImgName)){
						if(tzAttAUrl.lastIndexOf("/") + 1 == tzAttAUrl.length()){
							titleImageUrl = tzAttAUrl + sysImgName;
						}else{
							titleImageUrl = tzAttAUrl + "/" + sysImgName;
						}
					}
				}
				//------------
				//工作地址，单位，行业类别，本科院校;
				String str_lenProvince="",str_lenCity = "",str_comName="",str_comIndus = "",str_schName="";
				//是否是黑名单，是否允许申请;备注
				String str_blackName = "",str_allowApply="",str_beizhu="";
				//面试申请号
				String str_msSqh="";
				//------------
				
				//性别;
				String str_sex = "",str_name="",str_acctlook="";
				//邮箱、手机,skype;
				String str_email = "", str_phone= "", str_skype="";
				//账号激活状态、创建日期时间;
				String str_jihuozt = "" , str_zc_time = "";
				ArrayList<Map<String, Object>> arraylist = new ArrayList<>();
				
				
				PsTzRegUserT psTzRegUserT = psTzRegUserTMapper.selectByPrimaryKey(str_oprid);
				if(psTzRegUserT == null){
					errMsg[0] = "1";
					errMsg[1] = "不存在该用户！";
				}else{
					//性别;
					str_sex = psTzRegUserT.getTzGender();
					if(str_sex != null && !"".equals(str_sex)){
						String sexSQL = "select TZ_ZHZ_DMS from PS_TZ_GENDER_V where TZ_ZHZ_ID=?";
						str_sex = jdbcTemplate.queryForObject(sexSQL, new Object[]{str_sex},"String");
					}
					
					str_name= psTzRegUserT.getTzRealname();
					//-----------------
					str_msSqh= psTzRegUserT.getTzMssqh();
					str_blackName= psTzRegUserT.getTzBlackName();
					str_allowApply= psTzRegUserT.getTzAllowApply();
					str_beizhu= psTzRegUserT.getTzBeizhu();
					
					str_lenProvince = psTzRegUserT.getTzLenProid();
					str_lenCity= psTzRegUserT.getTzLenCity();
					str_comName= psTzRegUserT.getTzCompanyName();
					str_comIndus= psTzRegUserT.getTzCompIndustry();
					str_schName= psTzRegUserT.getTzSchCname();
					//-----------------
					
					
					String phoneAndEmailSQL = "select TZ_ZY_EMAIL,TZ_ZY_SJ,TZ_SKYPE from PS_TZ_LXFSINFO_TBL where TZ_LXFS_LY='ZCYH' and TZ_LYDX_ID=?";
					Map<String, Object> phoneAndEmailMap = jdbcTemplate.queryForMap(phoneAndEmailSQL,new Object[]{str_oprid});
					if(phoneAndEmailMap != null){
						str_email = (String) phoneAndEmailMap.get("TZ_ZY_EMAIL");
						str_email = (str_email != null) ? str_email : "";
						
						str_phone = (String) phoneAndEmailMap.get("TZ_ZY_SJ");
						str_phone = (str_phone != null) ? str_phone : "";
						
						str_skype = (String) phoneAndEmailMap.get("TZ_SKYPE");
						str_skype = (str_skype != null) ? str_skype : "";
					} 
					
					
					String accoutztSQL = "select (SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_JIHUO_ZT' AND TZ_ZHZ_ID=TZ_JIHUO_ZT AND TZ_EFF_STATUS='A') TZ_JIHUO_ZT, date_format(TZ_ZHCE_DT, '%Y-%m-%d %H:%i:%s') TZ_ZHCE_DT,TZ_MSH_ID from PS_TZ_AQ_YHXX_TBL where OPRID=?";
					Map<String, Object> accoutztMap = jdbcTemplate.queryForMap(accoutztSQL,new Object[]{str_oprid});
					if(phoneAndEmailMap != null){
						str_jihuozt = (String) accoutztMap.get("TZ_JIHUO_ZT");
						str_jihuozt = (str_jihuozt != null) ? str_jihuozt : "";
						
						str_zc_time = (String) accoutztMap.get("TZ_ZHCE_DT");
						str_zc_time = (str_zc_time != null) ? str_zc_time : "";
						
						str_msSqh= (String) accoutztMap.get("TZ_MSH_ID");
						str_msSqh = (str_msSqh != null) ? str_msSqh : "";
					} 
					
					//账号锁定状态;
					int acctLock = 0;
					Psoprdefn psoprdefn = psoprdefnMapper.selectByPrimaryKey(str_oprid);
					if(psoprdefn != null){
						acctLock = psoprdefn.getAcctlock();
					}
					if(acctLock == 1){
						str_acctlook = "1";
					}else{
						str_acctlook = "0";
					}
					
					//获取机构id;
					String strJgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
					String sqlList = "select TZ_REG_FIELD_ID,TZ_RED_FLD_YSMC,TZ_REG_FIELD_NAME,TZ_FIELD_TYPE from PS_TZ_REG_FIELD_T where TZ_JG_ID=? and TZ_ENABLE='Y' order by TZ_ORDER asc limit 7,1000";
					List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlList, new Object[]{strJgid});
					
					
					Map<String, Object> jsonMap = null;
					if(list != null){
						for(int i=0; i<list.size();i++){
							String regfieldId = (String) list.get(i).get("TZ_REG_FIELD_ID");
							String str_fld = "";
							String str_fld_name = "";
							if(regfieldId != null && !"".equals(regfieldId) 
									&& !"TZ_EMAIL".equals(regfieldId)
									&& !"TZ_PASSWORD".equals(regfieldId)
									&& !"TZ_REPASSWORD".equals(regfieldId)
									&& !"TZ_MOBILE".equals(regfieldId)
									&& !"TZ_TIME_ZONE".equals(regfieldId)){
								String str_fld_q = (String) list.get(i).get("TZ_RED_FLD_YSMC");
								String str_fld_h = (String) list.get(i).get("TZ_REG_FIELD_NAME");
								if(str_fld_h != null && !"".equals(str_fld_h)){
									str_fld_name = str_fld_h;
								}else{
									str_fld_name = str_fld_q;
								}
								
								String valueSQL = "SELECT " + regfieldId + " FROM PS_TZ_REG_USER_T where OPRID=?";
							    str_fld = jdbcTemplate.queryForObject(valueSQL, new Object[]{str_oprid},"String");
								
								String str_fld_type = (String) list.get(i).get("TZ_FIELD_TYPE");
								
								if("DROP".equals(str_fld_type)){
									String optValuSQL = "SELECT TZ_OPT_VALUE FROM PS_TZ_YHZC_XXZ_TBL WHERE TZ_JG_ID=? AND TZ_REG_FIELD_ID=? AND TZ_OPT_ID=?";
									str_fld = jdbcTemplate.queryForObject(optValuSQL, new Object[]{strJgid,regfieldId,str_fld},"String");
								}
							}
							
							if(!"TZ_REPASSWORD".equals(regfieldId)){
								jsonMap = new HashMap<>();
								jsonMap.put(regfieldId, str_fld);
								jsonMap.put("desc", str_fld_name);
//								arraylist.add(jsonMap);
							}
						}
					}
					
//					jsonMap = new HashMap<>();
//					jsonMap.put("TZ_SKYPE", str_skype);
//					jsonMap.put("desc", "Sky账号");
//					arraylist.add(jsonMap);
					jsonMap = new HashMap<>();
					//20170225,yuds,更改为省市
					jsonMap.put("TZ_LEN_CITY", str_lenProvince);
					jsonMap.put("desc", "工作所在省市");
					arraylist.add(jsonMap);
					jsonMap = new HashMap<>();
					jsonMap.put("TZ_COMPANY_NAME", str_comName);
					jsonMap.put("desc", "工作单位");
					arraylist.add(jsonMap);
					jsonMap = new HashMap<>();
					//20170225,yuds,行业类别下拉框
					String str_comIndusDesc="";
					String str_sqlIndus = "SELECT TZ_OPT_VALUE FROM PS_TZ_YHZC_XXZ_TBL WHERE TZ_REG_FIELD_ID='TZ_COMP_INDUSTRY' AND TZ_SITEI_ID = '' AND TZ_OPT_ID=?";
					str_comIndusDesc = jdbcTemplate.queryForObject(str_sqlIndus, new Object[]{str_comIndus},"String");
					jsonMap.put("TZ_COMP_INDUSTRY", str_comIndusDesc);
					jsonMap.put("desc", "行业类别");
					arraylist.add(jsonMap);
					jsonMap = new HashMap<>();
					jsonMap.put("TZ_SCH_CNAME", str_schName);
					jsonMap.put("desc", "本科院校");
					arraylist.add(jsonMap);
				}
				
				//考生导入信息;
				//查询当前人员的报名表id;
				/*Long appIns = jdbcTemplate.queryForObject(" select TZ_APP_INS_ID from PS_TZ_FORM_WRK_T WHERE OPRID=? ORDER BY ROW_LASTMANT_DTTM LIMIT 0,1", new Object[]{str_oprid},"Long");*/
				Map<String, Object> ksdrMap = new HashMap<>();
				
				if(appIns != null && !"".equals(appIns)){
//					//材料评审
//					Map< String, Object> clmsMap = jdbcTemplate.queryForMap("SELECT TZ_ENTER_CLPS,TZ_RESULT,TZ_RESULT_CODE FROM TZ_IMP_CLPS_TBL WHERE TZ_APP_INS_ID=?",new Object[]{appIns});
//					if(clmsMap != null){
//						String enterClps = clmsMap.get("TZ_ENTER_CLPS")==null?"":String.valueOf(clmsMap.get("TZ_ENTER_CLPS"));
//						String str1 = clmsMap.get("TZ_RESULT")==null?"":String.valueOf(clmsMap.get("TZ_RESULT"));
//						String str2 = clmsMap.get("TZ_RESULT_CODE")==null?"":String.valueOf(clmsMap.get("TZ_RESULT_CODE"));
//						
//						ksdrMap.put("enterClps", enterClps);
//						ksdrMap.put("clpsJg", str1);
//						ksdrMap.put("sfmsZg", str2);
//					}
					
//					//面试
//					Map< String, Object> msMap = jdbcTemplate.queryForMap("select TZ_TIME,TZ_ADDRESS,TZ_RESULT,TZ_RESULT_CODE from TZ_IMP_MSPS_TBL WHERE TZ_APP_INS_ID=?",new Object[]{appIns});
//					if(msMap != null){
//						String str1 = msMap.get("TZ_TIME")==null?"":String.valueOf(msMap.get("TZ_TIME"));
//						String str2 = msMap.get("TZ_ADDRESS")==null?"":String.valueOf(msMap.get("TZ_ADDRESS"));
//						String str3 = msMap.get("TZ_RESULT")==null?"":String.valueOf(msMap.get("TZ_RESULT"));
//						String str4 = msMap.get("TZ_RESULT_CODE")==null?"":String.valueOf(msMap.get("TZ_RESULT_CODE"));
//						
//						ksdrMap.put("msbdSj", str1);
//						ksdrMap.put("msbdDd", str2);
//						ksdrMap.put("msJg",str3);
//						ksdrMap.put("msJgBz",str4);
//					}
					
					//联考报名// 2017-12-11 xzx 添加（是否需要打印准考证、考试名称、考场号、座位号）
//					Map< String, Object> lkMap = jdbcTemplate.queryForMap("select TZ_YEAR,TZ_SCORE,TZ_ENGLISH,TZ_COMPREHENSIVE,TZ_OVERLINE,TZ_POLITICS,TZ_ENG_LISTENING,TZ_POLLSN_OVERLINE,TZ_PRE_ADMISSION,TZ_STU_NUM,TZ_DEGREE_CHECK,TZ_STU_NUM_LAST4,TZ_POL_ENG_TIME,TZ_POL_ENG_ADDR,TZ_SCHOLARSHIP_STA,TZ_POLLSN_DESC,TZ_IS_PRINT,TZ_KSMC,TZ_KCH,TZ_ZWH from TZ_IMP_LKBM_TBL WHERE TZ_APP_INS_ID=?",new Object[]{appIns});
//					if(lkMap != null){
//						String str2 = lkMap.get("TZ_YEAR")==null?"":String.valueOf(lkMap.get("TZ_YEAR"));
//						String str3 = lkMap.get("TZ_SCORE")==null?"":String.valueOf(lkMap.get("TZ_SCORE"));
//						String str4 = lkMap.get("TZ_ENGLISH")==null?"":String.valueOf(lkMap.get("TZ_ENGLISH"));
//						String str5 = lkMap.get("TZ_COMPREHENSIVE")==null?"":String.valueOf(lkMap.get("TZ_COMPREHENSIVE"));
//						String str6 = lkMap.get("TZ_OVERLINE")==null?"":String.valueOf(lkMap.get("TZ_OVERLINE"));
//						String str7 = lkMap.get("TZ_POLITICS")==null?"":String.valueOf(lkMap.get("TZ_POLITICS"));
//						String str8 = lkMap.get("TZ_ENG_LISTENING")==null?"":String.valueOf(lkMap.get("TZ_ENG_LISTENING"));
//						String str9 = lkMap.get("TZ_POLLSN_OVERLINE")==null?"":String.valueOf(lkMap.get("TZ_POLLSN_OVERLINE"));
//						String str10 = lkMap.get("TZ_PRE_ADMISSION")==null?"":String.valueOf(lkMap.get("TZ_PRE_ADMISSION"));
//						String str11 = lkMap.get("TZ_STU_NUM")==null?"":String.valueOf(lkMap.get("TZ_STU_NUM"));
//						String str12 = lkMap.get("TZ_DEGREE_CHECK")==null?"":String.valueOf(lkMap.get("TZ_DEGREE_CHECK"));
//						String str13 = lkMap.get("TZ_STU_NUM_LAST4")==null?"":String.valueOf(lkMap.get("TZ_STU_NUM_LAST4"));
//						String str14 = lkMap.get("TZ_POL_ENG_TIME")==null?"":String.valueOf(lkMap.get("TZ_POL_ENG_TIME"));
//						String str15 = lkMap.get("TZ_POL_ENG_ADDR")==null?"":String.valueOf(lkMap.get("TZ_POL_ENG_ADDR"));
//						String str16 = lkMap.get("TZ_SCHOLARSHIP_STA")==null?"":String.valueOf(lkMap.get("TZ_SCHOLARSHIP_STA"));
//						String str17 = lkMap.get("TZ_POLLSN_DESC")==null?"":String.valueOf(lkMap.get("TZ_POLLSN_DESC"));
//						
//						String str18 = lkMap.get("TZ_IS_PRINT")==null?"":String.valueOf(lkMap.get("TZ_IS_PRINT"));
//						String str19 = lkMap.get("TZ_KSMC")==null?"":String.valueOf(lkMap.get("TZ_KSMC"));
//						String str20 = lkMap.get("TZ_KCH")==null?"":String.valueOf(lkMap.get("TZ_KCH"));
//						String str21 = lkMap.get("TZ_ZWH")==null?"":String.valueOf(lkMap.get("TZ_ZWH"));
//						
//						ksdrMap.put("lkNf", str2);
//						ksdrMap.put("lkZf", str3);
//						ksdrMap.put("lkYy", str4);
//						ksdrMap.put("lkZh", str5);
//						ksdrMap.put("lkSfgx", str6);
//						ksdrMap.put("Zz", str7);
//						ksdrMap.put("yyTl", str8);
//						ksdrMap.put("zzTlSfGx", str9);
//						ksdrMap.put("sfYlq", str10);
//						ksdrMap.put("lkksBh", str11);
//						ksdrMap.put("lkbmsXlJyZt", str12);
//						ksdrMap.put("ksbhHsw", str13);
//						ksdrMap.put("zzYyTlKsSj", str14);
//						ksdrMap.put("zzYyTlKsDd", str15);
//						ksdrMap.put("ssJxjSqZt", str16);
//						ksdrMap.put("zzTlKsBz", str17);
//						ksdrMap.put("printZkz", str18);
//						ksdrMap.put("ksmc", str19);
//						ksdrMap.put("kskch", str20);
//						ksdrMap.put("kszwh", str21);
//					}
//					
//					//预录取
//					Map< String, Object> ylqMap = jdbcTemplate.queryForMap("SELECT TZ_SCHOLARSHIP_RST,TZ_TUITION_REFERENCE,TZ_STU_ID,TZ_PYXY_ACCEPT,TZ_GZZM_ACCEPT,TZ_CLASS_RST,TZ_EMAIL,TZ_INITIAL_PSWD,TZ_REMARK FROM TZ_IMP_YLQ_TBL WHERE TZ_APP_INS_ID=?",new Object[]{appIns});
//					if(ylqMap != null){
//						String str1 = ylqMap.get("TZ_SCHOLARSHIP_RST")==null?"":String.valueOf(ylqMap.get("TZ_SCHOLARSHIP_RST"));
//						ksdrMap.put("ssJxjZzJg", str1);
//						String str2 = ylqMap.get("TZ_TUITION_REFERENCE")==null?"":String.valueOf(ylqMap.get("TZ_TUITION_REFERENCE"));
//						ksdrMap.put("xfZeCk", str2);
//						String str3 = ylqMap.get("TZ_STU_ID")==null?"":String.valueOf(ylqMap.get("TZ_STU_ID"));
//						ksdrMap.put("xh", str3);
//						String str4 = ylqMap.get("TZ_PYXY_ACCEPT")==null?"":String.valueOf(ylqMap.get("TZ_PYXY_ACCEPT"));
//						ksdrMap.put("pyXyJs", str4);
//						String str5 = ylqMap.get("TZ_GZZM_ACCEPT")==null?"":String.valueOf(ylqMap.get("TZ_GZZM_ACCEPT"));
//						ksdrMap.put("zzZmJs", str5);
//						String str6 = ylqMap.get("TZ_CLASS_RST")==null?"":String.valueOf(ylqMap.get("TZ_CLASS_RST"));
//						ksdrMap.put("fbJg", str6);
//						String str7 = ylqMap.get("TZ_EMAIL")==null?"":String.valueOf(ylqMap.get("TZ_EMAIL"));
//						ksdrMap.put("jgYx", str7);
//						String str8 = ylqMap.get("TZ_INITIAL_PSWD")==null?"":String.valueOf(ylqMap.get("TZ_INITIAL_PSWD"));
//						ksdrMap.put("yxCsMm", str8);
//						String str9 = ylqMap.get("TZ_REMARK")==null?"":String.valueOf(ylqMap.get("TZ_REMARK"));
//						ksdrMap.put("yxRemark", str9);
//						
//					}
					
				}
				//查询考生个人信息
				//出生日期、联系电话、证件类型、证件号码、工作所在省市、考生编号、准考证号、是否有海外学历、申请的专业硕士和专业、紧急联系人、紧急联系人性别、紧急联系人手机号码
				//str_lenProvince					
				Map<String, Object> jsonMap2 = new HashMap<String, Object>();
				jsonMap2.put("OPRID", str_oprid);
				jsonMap2.put("msSqh", str_msSqh);
				jsonMap2.put("APPID", appIns);
				jsonMap2.put("userName", str_name);
				jsonMap2.put("userSex",str_sex );
				jsonMap2.put("userEmail",str_email );
				jsonMap2.put("userPhone",str_phone);
				jsonMap2.put("jihuoZt",str_jihuozt );
				jsonMap2.put("acctlock",str_acctlook );
				jsonMap2.put("zcTime",str_zc_time);
				jsonMap2.put("titleImageUrl",titleImageUrl );
				jsonMap2.put("column",arraylist );
				jsonMap2.put("ksdrInfo",ksdrMap);				
				if(!"Y".equals(str_blackName)){
				    str_blackName = "N";
				}
				jsonMap2.put("blackName",str_blackName );
				if(!"Y".equals(str_allowApply)){
				    str_allowApply = "N";
				}
				jsonMap2.put("allowApply",str_allowApply );
				jsonMap2.put("beizhu",str_beizhu );
				
				returnJsonMap.replace("formData", jsonMap2);	
	
			} else {
				errMsg[0] = "1";
				errMsg[1] = "参数不正确！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	/* 修改会员用户锁定状态 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("OPRID", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				System.out.print("===开始保存===");
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				if(jacksonUtil.containsKey("data")){
				    Map<String, Object> map = jacksonUtil.getMap("data");
				    System.out.print(map);
				    // 用户账号;
				    String strOprId = map.get("OPRID")==null?"":String.valueOf(map.get("OPRID"));
				    String strAppId = map.get("APPID")==null?"":String.valueOf(map.get("APPID"));
				    Long appInsId = Long.valueOf(strAppId);
				    System.out.print(strAppId);
				    
				    //20180328新流程

					if (strAppId!=""&&!strAppId.equals("")){	
						Long tzAppInsId= appInsId;
					    String tzFsmsResult=(String) map.get("TZ_IMP_FS_TBL_tf_TZ_FSMS_RESULT");
					    String tzIsFs=(String) map.get("TZ_IMP_FS_TBL_tf_TZ_IS_FS");
					    String tzIsFsms=(String) map.get("TZ_IMP_FS_TBL_tf_TZ_IS_FSMS");
					    String tzIsTj=(String) map.get("TZ_IMP_FS_TBL_tf_TZ_IS_TJ");
					    String tzIsZs=(String) map.get("TZ_IMP_FS_TBL_tf_TZ_IS_ZS");
					    String tzMsSjadd=(String) map.get("TZ_IMP_FS_TBL_tf_TZ_MS_SJADD");
					    String tzPolitics=(String) map.get("TZ_IMP_FS_TBL_tf_TZ_POLITICS");
					    String tzPoliticsSjadd=(String) map.get("TZ_IMP_FS_TBL_tf_TZ_POLITICS_SJADD");
					    String tzRemark=(String) map.get("TZ_IMP_FS_TBL_tf_TZ_REMARK");
					    String tzResultCode=(String) map.get("TZ_IMP_FS_TBL_tf_TZ_RESULT_CODE");
					    
					    PsTzFsT psTzFsT = new PsTzFsT();
					    psTzFsT.setTzAppInsId(tzAppInsId);
					    psTzFsT.setTzFsmsResult(tzFsmsResult);
					    psTzFsT.setTzIsFs(tzIsFs);
					    psTzFsT.setTzIsFsms(tzIsFsms);
					    psTzFsT.setTzIsTj(tzIsTj);
					    psTzFsT.setTzIsZs(tzIsZs);
					    psTzFsT.setTzMsSjadd(tzMsSjadd);
					    psTzFsT.setTzPolitics(tzPolitics);
					    psTzFsT.setTzPoliticsSjadd(tzPoliticsSjadd);
					    psTzFsT.setTzRemark(tzRemark);
					    psTzFsT.setTzResultCode(tzResultCode);
					    PsTzFsT fsInfo=psTzFsTMapper.selectByPrimaryKey(tzAppInsId);
					    if(fsInfo==null){
					    	psTzFsTMapper.insert(psTzFsT);
						}else{
							psTzFsTMapper.updateByPrimaryKeySelective(psTzFsT);
						}
				    
			    	
					    String tzComprehensive=(String) map.get("TZ_IMP_LKBM_TBL_tf_TZ_COMPREHENSIVE");
					    String tzEnglish=(String) map.get("TZ_IMP_LKBM_TBL_tf_TZ_ENGLISH");
					    String tzIsCheck=(String) map.get("TZ_IMP_LKBM_TBL_tf_TZ_IS_CHECK");
					    String tzIsConfirm=(String) map.get("TZ_IMP_LKBM_TBL_tf_TZ_IS_CONFIRM");
					    String tzIsPay=(String) map.get("TZ_IMP_LKBM_TBL_tf_TZ_IS_PAY");
					    String tzIsPrint=(String) map.get("TZ_IMP_LKBM_TBL_tf_TZ_IS_PRINT");
					    String tzIsWangbao=(String) map.get("TZ_IMP_LKBM_TBL_tf_TZ_IS_WANGBAO");
					    String tzOverline=(String) map.get("TZ_IMP_LKBM_TBL_tf_TZ_OVERLINE");
					    String tzScore=(String) map.get("TZ_IMP_LKBM_TBL_tf_TZ_SCORE");
					    String tzStuNum=(String) map.get("TZ_IMP_LKBM_TBL_tf_TZ_STU_NUM");
					    
					    PsTzLkbmT psTzLkbmT = new PsTzLkbmT();
					    psTzLkbmT.setTzAppInsId(tzAppInsId);
					    psTzLkbmT.setTzComprehensive(tzComprehensive);
					    psTzLkbmT.setTzEnglish(tzEnglish);
					    psTzLkbmT.setTzIsCheck(tzIsCheck);
					    psTzLkbmT.setTzIsConfirm(tzIsConfirm);
					    psTzLkbmT.setTzIsPay(tzIsPay);
					    psTzLkbmT.setTzIsPrint(tzIsPrint);
					    psTzLkbmT.setTzIsWangbao(tzIsWangbao);
					    psTzLkbmT.setTzOverline(tzOverline);
					    psTzLkbmT.setTzScore(tzScore);
					    psTzLkbmT.setTzStuNum(tzStuNum);
					    PsTzLkbmT lkbmInfo=psTzLkbmTMapper.selectByPrimaryKey(tzAppInsId);
					    if(lkbmInfo==null){
					    	psTzLkbmTMapper.insert(psTzLkbmT);
						}else{
							psTzLkbmTMapper.updateByPrimaryKeySelective(psTzLkbmT);
						}
				    
					    String tzResultCode_jg=(String) map.get("TZ_IMP_MSJG_TBL_tf_TZ_RESULT_CODE");
	
					    PsTzMsjgT psTzMsjgT =new PsTzMsjgT();
					    psTzMsjgT.setTzAppInsId(tzAppInsId);
					    psTzMsjgT.setTzResultCode(tzResultCode_jg);
					    PsTzMsjgT msjgInfo=psTzMsjgTMapper.selectByPrimaryKey(tzAppInsId);
					    if(msjgInfo==null){
					    	psTzMsjgTMapper.insert(psTzMsjgT);
						}else{
							psTzMsjgTMapper.updateByPrimaryKeySelective(psTzMsjgT);
						}
					    
				    
				    

					    String tzAddress=(String) map.get("TZ_IMP_MSZG_TBL_tf_TZ_ADDRESS");
					    String tzDate=(String) map.get("TZ_IMP_MSZG_TBL_tf_TZ_DATE");
					    String tzMaterial=(String) map.get("TZ_IMP_MSZG_TBL_tf_TZ_MATERIAL");
					    String tzMsBatch=(String) map.get("TZ_IMP_MSZG_TBL_tf_TZ_MS_BATCH");
					    System.out.print("更新MSZG"+tzMsBatch+tzAppInsId);
					    String tzRemark_zg=(String) map.get("TZ_IMP_MSZG_TBL_tf_TZ_REMARK");
					    String tzResultCode_zg=(String) map.get("TZ_IMP_MSZG_TBL_tf_TZ_RESULT_CODE");
					    String tzTime=(String) map.get("TZ_IMP_MSZG_TBL_tf_TZ_TIME");
					    
					    PsTzMszgT psTzMszgT = new PsTzMszgT();
					    psTzMszgT.setTzAppInsId(tzAppInsId);
					    psTzMszgT.setTzAddress(tzAddress);
					    psTzMszgT.setTzDate(tzDate);
					    psTzMszgT.setTzMaterial(tzMaterial);
					    psTzMszgT.setTzMsBatch(tzMsBatch);
					    psTzMszgT.setTzRemark(tzRemark_zg);
					    psTzMszgT.setTzResultCode(tzResultCode_zg);
					    psTzMszgT.setTzTime(tzTime);
					    PsTzMszgT mszgInfo=psTzMszgTMapper.selectByPrimaryKey(tzAppInsId);
					    if(mszgInfo==null){
					    	psTzMszgTMapper.insert(psTzMszgT);
						}else{
							psTzMszgTMapper.updateByPrimaryKeySelective(psTzMszgT);
						}

				    	 String tzIsSub=(String) map.get("TZ_IMP_QT_TBL_tf_TZ_IS_SUB");
						    String tzPayDateF=(String) map.get("TZ_IMP_QT_TBL_tf_TZ_PAY_DATE_F");
						    String tzPayDateS=(String) map.get("TZ_IMP_QT_TBL_tf_TZ_PAY_DATE_S");
						    String tzPayDateT=(String) map.get("TZ_IMP_QT_TBL_tf_TZ_PAY_DATE_T");
						    String tzPayF=(String) map.get("TZ_IMP_QT_TBL_tf_TZ_PAY_F");
						    String tzPayS=(String) map.get("TZ_IMP_QT_TBL_tf_TZ_PAY_S");
						    String tzPayT=(String) map.get("TZ_IMP_QT_TBL_tf_TZ_PAY_T");
						    String tzReferenceEmba=(String) map.get("TZ_IMP_QT_TBL_tf_TZ_REFERENCE_EMBA");
						    String tzReferenceLk=(String) map.get("TZ_IMP_QT_TBL_tf_TZ_REFERENCE_LK");
						    String tzReferenceLz=(String) map.get("TZ_IMP_QT_TBL_tf_TZ_REFERENCE_LZ");
						    String tzReferenceYx=(String) map.get("TZ_IMP_QT_TBL_tf_TZ_REFERENCE_YX");
						    String tzTuitionReference=(String) map.get("TZ_IMP_QT_TBL_tf_TZ_TUITION_REFERENCE");
						    String tzYdMs=(String) map.get("TZ_IMP_QT_TBL_tf_TZ_YD_MS");	    
						    
						    PsTzQtT psTzQtT =new PsTzQtT();
						    psTzQtT.setTzAppInsId(tzAppInsId);
						    psTzQtT.setTzIsSub(tzIsSub);
//						    if(tzPayDateF!=null){
//					    	SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
//					    	if("".equals(tzPayDateF)){
//					    		psTzRegUserT.setBirthdate(null);
//					    	}else{
//					    		Date date = dateFormate.parse(tzPayDateF);
//						    	psTzQtT.setTzPayDateF(date);
//					    	}				    	
//					    	}
						    psTzQtT.setTzPayDateF(tzPayDateF);
						    psTzQtT.setTzPayDateS(tzPayDateS);
						    psTzQtT.setTzPayDateT(tzPayDateT);
						    psTzQtT.setTzPayF(tzPayF);
						    psTzQtT.setTzPayS(tzPayS);
						    psTzQtT.setTzPayT(tzPayT);
						    psTzQtT.setTzReferenceEmba(tzReferenceEmba);
						    psTzQtT.setTzReferenceLk(tzReferenceLk);
						    psTzQtT.setTzReferenceLz(tzReferenceLz);
						    psTzQtT.setTzReferenceYx(tzReferenceYx);
						    psTzQtT.setTzTuitionReference(tzTuitionReference);
						    psTzQtT.setTzYdMs(tzYdMs);
						    PsTzQtT qtInfo=psTzQtTMapper.selectByPrimaryKey(tzAppInsId);
						    if(qtInfo==null){
						    	psTzQtTMapper.insert(psTzQtT);
							}else{
								psTzQtTMapper.updateByPrimaryKeySelective(psTzQtT);
							}
				    }
				    		
				    //20180328新流程结束
				    
				    returnJsonMap.replace("OPRID", strOprId);
				    
				}else{
					errMsg[0] = "1";
					errMsg[1] = "参数错误";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
}
