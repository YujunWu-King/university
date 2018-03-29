package com.tranzvision.gd.TZLeaguerAccountBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzKshLqlcTblMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzRegUserTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzKshLqlcTbl;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzRegUserT;
import com.tranzvision.gd.TZAccountMgBundle.dao.PsTzAqYhxxTblMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTbl;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzLxfsInfoTblMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzLxfsInfoTbl;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

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
@Service("com.tranzvision.gd.TZLeaguerAccountBundle.service.impl.LeaguerAccountInfoServiceImpl")
public class LeaguerAccountInfoServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private SqlQuery SqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsTzRegUserTMapper psTzRegUserTMapper;
	@Autowired
	private PsoprdefnMapper psoprdefnMapper;
	@Autowired
	private PsTzKshLqlcTblMapper psTzKshLqlcTblMapper;
	@Autowired
	private PsTzAqYhxxTblMapper psTzAqYhxxTblMapper;
	@Autowired
	private PsTzLxfsInfoTblMapper psTzLxfsInfoTblMapper;
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
				String strBindEmailFlg="";
				String strBindPhoneFlg="";
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
					/*if(str_sex != null && !"".equals(str_sex)){
						String sexSQL = "select TZ_ZHZ_DMS from PS_TZ_GENDER_V where TZ_ZHZ_ID=?";
						str_sex = jdbcTemplate.queryForObject(sexSQL, new Object[]{str_sex},"String");
					}*/
					
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
					
					//绑定手机和邮箱
					String bindPhoneAndEmailSQL = "SELECT TZ_EMAIL,TZ_MOBILE,TZ_YXBD_BZ,TZ_SJBD_BZ FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=?";
					Map<String, Object> phoneAndEmailMap = jdbcTemplate.queryForMap(bindPhoneAndEmailSQL,new Object[]{str_oprid});
					
					if(phoneAndEmailMap!=null){
						str_email = phoneAndEmailMap.get("TZ_EMAIL")==null?"":String.valueOf(phoneAndEmailMap.get("TZ_EMAIL"));
						str_phone = phoneAndEmailMap.get("TZ_MOBILE")==null?"":String.valueOf(phoneAndEmailMap.get("TZ_MOBILE"));
						strBindEmailFlg = phoneAndEmailMap.get("TZ_YXBD_BZ")==null?"":String.valueOf(phoneAndEmailMap.get("TZ_YXBD_BZ"));
						strBindPhoneFlg = phoneAndEmailMap.get("TZ_SJBD_BZ")==null?"":String.valueOf(phoneAndEmailMap.get("TZ_SJBD_BZ"));
					}
					
					String accoutztSQL = "select TZ_JIHUO_ZT, date_format(TZ_ZHCE_DT, '%Y-%m-%d %H:%i:%s') TZ_ZHCE_DT,TZ_MSH_ID from PS_TZ_AQ_YHXX_TBL where OPRID=?";
					Map<String, Object> accoutztMap = jdbcTemplate.queryForMap(accoutztSQL,new Object[]{str_oprid});
					if(accoutztMap != null){
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
				Long appIns = jdbcTemplate.queryForObject(" select TZ_APP_INS_ID from PS_TZ_FORM_WRK_T WHERE OPRID=? ORDER BY ROW_LASTMANT_DTTM LIMIT 0,1", new Object[]{str_oprid},"Long");
				Map<String, Object> ksdrMap = new HashMap<>();
				
//				if(appIns != null && appIns > 0){
//					//材料评审
//					Map< String, Object> clmsMap = jdbcTemplate.queryForMap("SELECT TZ_RESULT,TZ_RESULT_CODE FROM TZ_IMP_CLPS_TBL WHERE TZ_APP_INS_ID=?",new Object[]{appIns});
//					if(clmsMap != null){
//						ksdrMap.put("clpsJg", clmsMap.get("TZ_RESULT"));
//						ksdrMap.put("sfmsZg", clmsMap.get("TZ_RESULT_CODE"));
//					}
//					
//					//面试
//					Map< String, Object> msMap = jdbcTemplate.queryForMap("select TZ_TIME,TZ_ADDRESS,TZ_RESULT,TZ_RESULT_CODE from TZ_IMP_MSPS_TBL WHERE TZ_APP_INS_ID=?",new Object[]{appIns});
//					if(msMap != null){
//						ksdrMap.put("msbdSj", msMap.get("TZ_TIME"));
//						ksdrMap.put("msbdDd", msMap.get("TZ_ADDRESS"));
//						ksdrMap.put("msJg", msMap.get("TZ_RESULT"));
//						ksdrMap.put("msJgBz", msMap.get("TZ_RESULT_CODE"));
//					}
//					
//					//联考报名
//					Map< String, Object> lkMap = jdbcTemplate.queryForMap("select TZ_YEAR,TZ_SCORE,TZ_ENGLISH,TZ_COMPREHENSIVE,TZ_OVERLINE,TZ_POLITICS,TZ_ENG_LISTENING,TZ_POLLSN_OVERLINE,TZ_PRE_ADMISSION,TZ_STU_NUM,TZ_DEGREE_CHECK,TZ_STU_NUM_LAST4,TZ_POL_ENG_TIME,TZ_POL_ENG_ADDR,TZ_SCHOLARSHIP_STA,TZ_POLLSN_DESC from TZ_IMP_LKBM_TBL WHERE TZ_APP_INS_ID=?",new Object[]{appIns});
//					if(lkMap != null){
//						ksdrMap.put("lkNf", lkMap.get("TZ_YEAR"));
//						ksdrMap.put("lkZf", lkMap.get("TZ_SCORE"));
//						ksdrMap.put("lkYy", lkMap.get("TZ_ENGLISH"));
//						ksdrMap.put("lkZh", lkMap.get("TZ_COMPREHENSIVE"));
//						ksdrMap.put("lkSfgx", lkMap.get("TZ_OVERLINE"));
//						ksdrMap.put("Zz", lkMap.get("TZ_POLITICS"));
//						ksdrMap.put("yyTl", lkMap.get("TZ_ENG_LISTENING"));
//						ksdrMap.put("zzTlSfGx", lkMap.get("TZ_POLLSN_OVERLINE"));
//						ksdrMap.put("sfYlq", lkMap.get("TZ_PRE_ADMISSION"));
//						ksdrMap.put("lkksBh", lkMap.get("TZ_STU_NUM"));
//						ksdrMap.put("lkbmsXlJyZt", lkMap.get("TZ_DEGREE_CHECK"));
//						ksdrMap.put("ksbhHsw", lkMap.get("TZ_STU_NUM_LAST4"));
//						ksdrMap.put("zzYyTlKsSj", lkMap.get("TZ_POL_ENG_TIME"));
//						ksdrMap.put("zzYyTlKsDd", lkMap.get("TZ_POL_ENG_ADDR"));
//						ksdrMap.put("ssJxjSqZt", lkMap.get("TZ_SCHOLARSHIP_STA"));
//						ksdrMap.put("zzTlKsBz", lkMap.get("TZ_POLLSN_DESC"));
//					}
//					
//					//预录取
//					Map< String, Object> ylqMap = jdbcTemplate.queryForMap("select TZ_SCHOLARSHIP_RST,TZ_TUITION_REFERENCE,TZ_STU_ID,TZ_PYXY_ACCEPT,TZ_GZZM_ACCEPT,TZ_CLASS_RST,TZ_EMAIL,TZ_INITIAL_PSWD from TZ_IMP_YLQ_TBL WHERE TZ_APP_INS_ID=?",new Object[]{appIns});
//					if(ylqMap != null){
//						ksdrMap.put("ssJxjZzJg", ylqMap.get("TZ_SCHOLARSHIP_RST"));
//						ksdrMap.put("xfZeCk", ylqMap.get("TZ_TUITION_REFERENCE"));
//						ksdrMap.put("xh", ylqMap.get("TZ_STU_ID"));
//						ksdrMap.put("pyXyJs", ylqMap.get("TZ_PYXY_ACCEPT"));
//						ksdrMap.put("zzZmJs", ylqMap.get("TZ_GZZM_ACCEPT"));
//						ksdrMap.put("fbJg", ylqMap.get("TZ_CLASS_RST"));
//						ksdrMap.put("jgYx", ylqMap.get("TZ_EMAIL"));
//						ksdrMap.put("yxCsMm", ylqMap.get("TZ_INITIAL_PSWD"));
//					}
//					
//				}
				//tmt 修改 2017-4-7录取流程信息
				Map<String, Object> lqlcInfoMap = new HashMap<>();
				//lqlcInfoMap.put("lqlc", str_lenProvince);
				Map< String, Object> getlqlcInfoMap = jdbcTemplate.queryForMap("SELECT A.OPRID,A.TZ_MSPS_MC,A.TZ_MSPS_PC,A.TZ_TJLQZG,A.TZ_TJLQZG_XM,A.TZ_MSJG_PC,A.TZ_LKQ_TZQK,A.TZ_TZYY_NOTES,A.TZ_LKQ_TJLQZG,A.TZ_LKQ_TJLQZG_XM,A.TZ_LKBM,A.TZ_LKSK,A.TZ_LKGX,A.TZ_LKZZGX,A.TZ_LKYYTLGX,A.TZ_YLQ_TZQK,A.TZ_TZYY_NOTES2,A.TZ_YLQ_ZG,A.TZ_YLQ_ZG_XM,A.TZ_ZSLQ_TZQK,A.TZ_TZYY_NOTES3,A.TZ_ZSLQ_ZG,A.TZ_ZSLQ_ZG_XM,A.TZ_RXQ_TZQK,A.TZ_TZYY_NOTES4,A.TZ_RX_QK,A.TZ_RX_XM FROM PS_TZ_KSH_LQLC_TBL A ,PS_TZ_LXFSINFO_TBL B where A.OPRID = B.TZ_LYDX_ID AND A.OPRID=?",new Object[]{str_oprid});
				if(getlqlcInfoMap!=null){
					lqlcInfoMap.put("mspsmc", getlqlcInfoMap.get("TZ_MSPS_MC"));
					lqlcInfoMap.put("mspspc", getlqlcInfoMap.get("TZ_MSPS_PC"));
					lqlcInfoMap.put("tjlqzg", getlqlcInfoMap.get("TZ_TJLQZG"));
					lqlcInfoMap.put("tjlqzgxm", getlqlcInfoMap.get("TZ_TJLQZG_XM"));
					lqlcInfoMap.put("msjgpc", getlqlcInfoMap.get("TZ_MSJG_PC"));
					
					lqlcInfoMap.put("lkbm", getlqlcInfoMap.get("TZ_LKBM"));
					lqlcInfoMap.put("lqlzqk", getlqlcInfoMap.get("TZ_LKQ_TZQK"));
					lqlcInfoMap.put("tzyyNotes", getlqlcInfoMap.get("TZ_TZYY_NOTES"));
					lqlcInfoMap.put("lkqtjluzg", getlqlcInfoMap.get("TZ_LKQ_TJLQZG"));
					lqlcInfoMap.put("lkqtjluzgxm", getlqlcInfoMap.get("TZ_LKQ_TJLQZG_XM"));
					
					lqlcInfoMap.put("lksk", getlqlcInfoMap.get("TZ_LKSK"));
					lqlcInfoMap.put("yytlgx", getlqlcInfoMap.get("TZ_LKYYTLGX"));
					lqlcInfoMap.put("zzgx", getlqlcInfoMap.get("TZ_LKZZGX"));
					lqlcInfoMap.put("lkgx", getlqlcInfoMap.get("TZ_LKGX"));
					lqlcInfoMap.put("ylqzzqk", getlqlcInfoMap.get("TZ_YLQ_TZQK"));
					
					lqlcInfoMap.put("tzyyNotes2", getlqlcInfoMap.get("TZ_TZYY_NOTES2"));
					lqlcInfoMap.put("ylqzg", getlqlcInfoMap.get("TZ_YLQ_ZG"));
					lqlcInfoMap.put("ylqzgxm", getlqlcInfoMap.get("TZ_YLQ_ZG_XM"));
					lqlcInfoMap.put("zslqzzqk", getlqlcInfoMap.get("TZ_ZSLQ_TZQK"));
					lqlcInfoMap.put("tzyyNotes3", getlqlcInfoMap.get("TZ_TZYY_NOTES3"));
					
					lqlcInfoMap.put("zslqzg", getlqlcInfoMap.get("TZ_ZSLQ_ZG"));
					lqlcInfoMap.put("zslqzgxm", getlqlcInfoMap.get("TZ_ZSLQ_ZG_XM"));
					lqlcInfoMap.put("rxqzzqk", getlqlcInfoMap.get("TZ_RXQ_TZQK"));
					lqlcInfoMap.put("tzyyNotes4", getlqlcInfoMap.get("TZ_TZYY_NOTES4"));
					lqlcInfoMap.put("rxqk", getlqlcInfoMap.get("TZ_RX_QK"));
					lqlcInfoMap.put("rxxm", getlqlcInfoMap.get("TZ_RX_XM"));

				}
				//查询考生个人信息
				//出生日期、联系电话、证件类型、证件号码、工作所在省市、考生编号、准考证号、是否有海外学历、申请的专业硕士和专业、紧急联系人、紧急联系人性别、紧急联系人手机号码
				//str_lenProvince	
				Map<String, Object> perInfoMap = new HashMap<>();
				perInfoMap.put("lenProvince", str_lenProvince);
				Map< String, Object> grxxMap = jdbcTemplate.queryForMap("SELECT A.OPRID,B.TZ_ZY_SJ,A.TZ_REALNAME,A.BIRTHDATE,A.TZ_SCH_CNAME,A.TZ_LEN_PROID,A.TZ_COMPANY_NAME,A.TZ_COMP_INDUSTRY FROM PS_TZ_REG_USER_T A LEFT JOIN PS_TZ_LXFSINFO_TBL B ON A.OPRID=B.TZ_LYDX_ID WHERE A.OPRID=?",new Object[]{str_oprid});
				if(grxxMap!=null){
					perInfoMap.put("oprid", grxxMap.get("OPRID"));
				    perInfoMap.put("birthdate", grxxMap.get("BIRTHDATE"));
				    perInfoMap.put("zyPhone", grxxMap.get("TZ_ZY_SJ"));				    
				    perInfoMap.put("realname", grxxMap.get("TZ_REALNAME"));
				    perInfoMap.put("shcoolcname", grxxMap.get("TZ_SCH_CNAME"));
				    perInfoMap.put("lenProvince", grxxMap.get("TZ_LEN_PROID"));
				    perInfoMap.put("companyname", grxxMap.get("TZ_COMPANY_NAME"));
				    perInfoMap.put("copmindustry", grxxMap.get("TZ_COMP_INDUSTRY"));;
				    
				}
				String appMajor = jdbcTemplate.queryForObject("SELECT TZ_APP_MAJOR_NAME FROM PS_TZ_APP_KS_INFO_EXT_T WHERE TZ_OPRID=?", new Object[]{str_oprid}, "String");
				if(appMajor==null){
				    appMajor = "";
				}
				perInfoMap.put("appMajor", appMajor);
				
				Map<String, Object> jsonMap2 = new HashMap<String, Object>();
				jsonMap2.put("OPRID", str_oprid);
				jsonMap2.put("msSqh", str_msSqh);
				jsonMap2.put("userName", str_name);
				jsonMap2.put("userSex",str_sex );
				jsonMap2.put("userEmail",str_email );
				jsonMap2.put("userPhone",str_phone);
				boolean tmpBindPhoneFlg = false;
				if("Y".equals(strBindPhoneFlg)){
					tmpBindPhoneFlg = true;
				}
				jsonMap2.put("bindPhoneFlg",tmpBindPhoneFlg);
				boolean tmpBindEmailFlg = false;
				if("Y".equals(strBindEmailFlg)){
					tmpBindEmailFlg = true;
				}
				jsonMap2.put("bindEmailFlg",tmpBindEmailFlg);
				jsonMap2.put("jihuoZt",str_jihuozt );
				jsonMap2.put("acctlock",str_acctlook );
				jsonMap2.put("zcTime",str_zc_time);
				jsonMap2.put("titleImageUrl",titleImageUrl );
				jsonMap2.put("column",arraylist );
				jsonMap2.put("ksdrInfo",ksdrMap);
				jsonMap2.put("perInfo", perInfoMap);
				jsonMap2.put("lqlcInfo", lqlcInfoMap);
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
			
			String strCurrentOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				if(jacksonUtil.containsKey("data")){
					Map<String, Object> map = jacksonUtil.getMap("data");
					// 用户账号;
				    String strOprId = (String) map.get("OPRID");
				    //用户姓名
				    String strUserName = (String) map.get("userName");
				    //性别
				    String strUserSex = (String) map.get("userSex");
				    //绑定手机
				    String strUserEmail = (String) map.get("userEmail");
				    //绑定手机
				    String strUserPhone = (String) map.get("userPhone");
				    // 激活状态;
				    String strJihuoZt = (String) map.get("jihuoZt");
				    // 锁定状态;
				    String strAcctLock = (String) map.get("acctlock");
				    // 黑名单状态
				    String strBlackName = (String) map.get("blackName");
				    //允许继续申请
				    String strAllowApply = (String) map.get("allowApply");
				    //备注
				    String strBeiZhu = (String) map.get("beizhu");
				    //联系电话
				    String zyPhone = (String) map.get("zyPhone");
				    //真是姓名
				    String realname = (String) map.get("realname");
				    //出生日期
				    String strBirthdate = (String) map.get("birthdate");
				    //本科院校
				    String shcoolcname = (String) map.get("shcoolcname");
				    //常驻州省
				    String strLenProvince = (String) map.get("lenProvince");
				    //工作单位
				    String companyname = (String) map.get("companyname");
				    //行业类别
				    String copmindustry = (String) map.get("copmindustry");

				    //20170425,yuds,申请用户信息修改
				    String bindEmailFlg = (String) map.get("bindEmailFlg");
				    String bindPhoneFlg = (String) map.get("bindPhoneFlg");
				    //用户信息表
				    String strUserOrgSQL = "SELECT TZ_JG_ID FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
				    String strUserOrg = jdbcTemplate.queryForObject(strUserOrgSQL, new Object[]{strOprId}, "String");
				    PsTzAqYhxxTbl psTzAqYhxxTbl = new PsTzAqYhxxTbl();
				    psTzAqYhxxTbl.setTzDlzhId(strOprId);
				    psTzAqYhxxTbl.setTzJgId(strUserOrg);
				    if(strUserName!=null&&!"".equals(strUserName)){
				    	psTzAqYhxxTbl.setTzRealname(strUserName);
				    }
				    if(strUserEmail!=null){
				    	//查重邮箱
				    	if(!"".equals(strUserEmail)){
				    		String strEmailCheckSQL="SELECT 'Y' FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID<>? AND TZ_EMAIL=?";
				    		String strCheckFlg = jdbcTemplate.queryForObject(strEmailCheckSQL, new Object[]{strOprId,strUserEmail}, "String");
				    		if("Y".equals(strCheckFlg)){
				    			returnJsonMap.replace("OPRID", strOprId);
				    			strRet = jacksonUtil.Map2json(returnJsonMap);
				    			errMsg[0] = "1";
				    			errMsg[1] = "该邮箱已被绑定，请更换绑定邮箱.";
				    			
				    			return strRet;
				    		}else{
				    			psTzAqYhxxTbl.setTzYxbdBz(bindEmailFlg);
				    		}
				    	}else{
				    		psTzAqYhxxTbl.setTzYxbdBz(bindEmailFlg);
				    	}
				    	psTzAqYhxxTbl.setTzEmail(strUserEmail);
				    }
				    if(strUserPhone!=null){
				    	//查重手机
				    	if(!"".equals(strUserPhone)){
				    		String strPhoneCheckSQL="SELECT 'Y' FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID<>? AND TZ_MOBILE=?";
				    		String strCheckFlg = jdbcTemplate.queryForObject(strPhoneCheckSQL, new Object[]{strOprId,strUserPhone}, "String");
				    		if("Y".equals(strCheckFlg)){
				    			returnJsonMap.replace("OPRID", strOprId);
				    			strRet = jacksonUtil.Map2json(returnJsonMap);
				    			errMsg[0] = "1";
				    			errMsg[1] = "该手机已被绑定，请更换绑定手机.";
				    			
				    			return strRet;
				    		}else{
				    			psTzAqYhxxTbl.setTzSjbdBz(bindPhoneFlg);
				    		}
				    	}else{
				    		psTzAqYhxxTbl.setTzSjbdBz(bindPhoneFlg);
				    	}
				    	psTzAqYhxxTbl.setTzMobile(strUserPhone);
				    }
				    
				    if((!"".equals(strUserEmail)&&"Y".equals(bindEmailFlg))||(!"".equals(strUserPhone)&&"Y".equals(bindPhoneFlg))){
				    	
				    }else{
				    	returnJsonMap.replace("OPRID", strOprId);
		    			strRet = jacksonUtil.Map2json(returnJsonMap);
		    			errMsg[0] = "1";
		    			errMsg[1] = "绑定手机或绑定邮箱至少要有一个";
		    			
		    			return strRet;
				    }
				    
				    if(strJihuoZt!=null){
				    	psTzAqYhxxTbl.setTzJihuoZt(strJihuoZt);
				    }
				    psTzAqYhxxTbl.setRowLastmantDttm(new Date());
				    psTzAqYhxxTbl.setRowLastmantOprid(strCurrentOprid);
				    psTzAqYhxxTblMapper.updateByPrimaryKeySelective(psTzAqYhxxTbl);
				    
				    //注册信息表
				    PsTzRegUserT psTzRegUserT = new PsTzRegUserT();
				    psTzRegUserT.setOprid(strOprId);
				    if(strUserName!=null&&!"".equals(strUserName)){
				    	psTzRegUserT.setTzRealname(strUserName);
				    }
				    if(strUserSex!=null&&!"".equals(strUserSex)){
				    	psTzRegUserT.setTzGender(strUserSex);
				    }
//				    if(strBirthdate!=null){
//				    	SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
//				    	if("".equals(strBirthdate)){
//				    		psTzRegUserT.setBirthdate(null);
//				    	}else{
//				    		Date birthdate = dateFormate.parse(strBirthdate);
//					    	psTzRegUserT.setBirthdate(birthdate);
//				    	}				    	
//				    }
				    //联系电话
				    if(zyPhone!=null){
				    	psTzRegUserT.setNationalId(zyPhone);
				    }
				    if(realname!=null){
				    	psTzRegUserT.setTzRealname(realname);	    	
				    }
				    if(shcoolcname!=null){
				    	psTzRegUserT.setTzSchCname(shcoolcname);			    	
				    }
				    if(strLenProvince!=null){
				    	psTzRegUserT.setTzLenProid(strLenProvince);
				    }
				    if(companyname!=null){
				    	psTzRegUserT.setTzCompanyName(companyname);			    	
				    }
				    if(copmindustry!=null){
				    	psTzRegUserT.setTzCompIndustry(copmindustry);				    	
				    }
				    
				    psTzRegUserT.setRowLastmantDttm(new Date());
				    psTzRegUserT.setRowLastmantOprid(strCurrentOprid);
				    psTzRegUserTMapper.updateByPrimaryKeySelective(psTzRegUserT);
				    
				    //联系方式表
				    String strLxfsSQL = "SELECT TZ_LXFS_LY FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LYDX_ID=?";
				    String strLxfsLy = jdbcTemplate.queryForObject(strLxfsSQL, new Object[]{strOprId}, "String");
				    
				    PsTzLxfsInfoTbl psTzLxfsInfoTbl = new PsTzLxfsInfoTbl();
				    psTzLxfsInfoTbl.setTzLxfsLy(strLxfsLy);
				    psTzLxfsInfoTbl.setTzLydxId(strOprId);
//				    if(strZyPhone!=null){
//				    	psTzLxfsInfoTbl.setTzZySj(strZyPhone);
//				    }
//				    if(strZyEmail!=null){
//				    	psTzLxfsInfoTbl.setTzZyEmail(strZyEmail);
//				    }
//				    psTzLxfsInfoTblMapper.updateByPrimaryKeySelective(psTzLxfsInfoTbl);
				    
				    
				    //扩展表
				    String strExistsSql = "SELECT 'Y' FROM PS_TZ_APP_KS_INFO_EXT_T WHERE TZ_OPRID=?";
				    String strExistsFlg = jdbcTemplate.queryForObject(strExistsSql, new Object[]{strOprId}, "String");
				    String strUpdateSql = "UPDATE PS_TZ_APP_KS_INFO_EXT_T SET TZ_APP_MAJOR_NAME=? WHERE TZ_OPRID=?";
//				    if("Y".equals(strExistsFlg)){
//				    	jdbcTemplate.update(strUpdateSql, new Object[]{strAppMajor,strOprId});
//				    }else{
//				    	strUpdateSql = "INSERT INTO PS_TZ_APP_KS_INFO_EXT_T(TZ_OPRID,TZ_APP_MAJOR_NAME) VALUES(?,?)";
//				    	jdbcTemplate.update(strUpdateSql, new Object[]{strOprId,strAppMajor});
//				    }
				    //tmt 修改
//				    //录取流程
//				    String mspsmc=(String) map.get("mspsmc");
//				    String mspspc=(String) map.get("mspspc");
//				    String tjlqzg=(String) map.get("tjlqzg");
//				    String tjlqzgxm=(String) map.get("tjlqzgxm");
//				    String msjgpc=(String) map.get("msjgpc");
//				    
//				    String lkbm=(String) map.get("lkbm");
//				    String lqlzqk=(String) map.get("lqlzqk");
//				    String tzyyNotes=(String) map.get("tzyyNotes");
//				    String lkqtjluzg=(String) map.get("lkqtjluzg");
//				    String lkqtjluzgxm=(String) map.get("lkqtjluzgxm");
//				    
//				    String lksk=(String) map.get("lksk");
//				    String yytlgx=(String) map.get("yytlgx");
//				    String zzgx=(String) map.get("zzgx");
//				    String lkgx=(String) map.get("lkgx");
//				    String ylqzzqk=(String) map.get("ylqzzqk");
//				    
//				    String tzyyNotes2=(String) map.get("tzyyNotes2");
//				    String ylqzg=(String) map.get("ylqzg");
//				    String ylqzgxm=(String) map.get("ylqzgxm");
//				    String zslqzzqk=(String) map.get("zslqzzqk");
//				    String tzyyNotes3=(String) map.get("tzyyNotes3");
//				    
//				    String zslqzg=(String) map.get("zslqzg");
//				    String zslqzgxm=(String) map.get("zslqzgxm");
//				    String rxqzzqk=(String) map.get("rxqzzqk");
//				    String tzyyNotes4=(String) map.get("tzyyNotes4");
//				    String rxqk=(String) map.get("rxqk");
//				    String rxxm=(String) map.get("rxxm");
//				    String updatelSFSSql = "UPDATE PS_TZ_REG_USER_T SET TZ_BLACK_NAME=?,TZ_ALLOW_APPLY=?,TZ_BEIZHU=? WHERE OPRID=?";
//					jdbcTemplate.update(updatelSFSSql, new Object[]{strBlackName,strAllowApply,strBeiZhu, strOprId});
//					//tmt 修改 录取流程信息 
//					PsTzKshLqlcTbl psTzKshLqlcTbl=new PsTzKshLqlcTbl();
//					psTzKshLqlcTbl.setOprid(strOprId);
//					psTzKshLqlcTbl.setTzMspsMc(mspsmc);
//					psTzKshLqlcTbl.setTzMspsPc(mspspc);
//					psTzKshLqlcTbl.setTzTjlqzg(tjlqzg);
//					psTzKshLqlcTbl.setTzTjlqzgXm(tjlqzgxm);
//					psTzKshLqlcTbl.setTzMsjgPc(msjgpc);	
//					
//					psTzKshLqlcTbl.setTzLkbm(lkbm);
//					psTzKshLqlcTbl.setTzLkqTzqk(lqlzqk);
//					psTzKshLqlcTbl.setTzTzyyNotes(tzyyNotes);
//					psTzKshLqlcTbl.setTzLkqTjlqzg(lkqtjluzg);
//					psTzKshLqlcTbl.setTzLkqTjlqzgXm(lkqtjluzgxm);
//					
//					
//					psTzKshLqlcTbl.setTzLksk(lksk);
//					psTzKshLqlcTbl.setTzLkyytlgx(yytlgx);
//					psTzKshLqlcTbl.setTzLkzzgx(zzgx);
//					psTzKshLqlcTbl.setTzLkgx(lkgx);
//					psTzKshLqlcTbl.setTzYlqTzqk(ylqzzqk);
//					
//					psTzKshLqlcTbl.setTzTzyyNotes2(tzyyNotes2);
//					psTzKshLqlcTbl.setTzYlqZg(ylqzg);
//					psTzKshLqlcTbl.setTzYlqZgXm(ylqzgxm);
//					psTzKshLqlcTbl.setTzZslqTzqk(zslqzzqk);
//					psTzKshLqlcTbl.setTzTzyyNotes3(tzyyNotes3);
//					
//					psTzKshLqlcTbl.setTzZslqZg(zslqzg);
//					psTzKshLqlcTbl.setTzZslqZgXm(zslqzgxm);
//					psTzKshLqlcTbl.setTzRxqTzqk(rxqzzqk);
//					psTzKshLqlcTbl.setTzTzyyNotes4(tzyyNotes4);
//					psTzKshLqlcTbl.setTzRxQk(rxqk);
//					psTzKshLqlcTbl.setTzRxXm(rxxm);
//					PsTzKshLqlcTbl info=psTzKshLqlcTblMapper.selectByPrimaryKey(strOprId);
//					if(info==null){
//						psTzKshLqlcTblMapper.insert(psTzKshLqlcTbl);
//					}else{
//						psTzKshLqlcTblMapper.updateByPrimaryKeySelective(psTzKshLqlcTbl);
//					}
					//jdbcTemplate.update(updatelqlcSql, new Object[]{strOprId});
					
				    //20180328新流程
				    System.out.print("===开始====");
					String appInsId="";
					String classid="";
					String usersql = " SELECT TZ_CLASS_ID,TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID=? order by ROW_ADDED_DTTM desc LIMIT 1";

					Map<String, Object> userMap = null;
					userMap = jdbcTemplate.queryForMap(usersql, new Object[] { strOprId });
					System.out.print("userMap====>" + userMap);
					if (userMap != null) {
						classid = String.valueOf(userMap.get("TZ_CLASS_ID"));
						appInsId = String.valueOf(userMap.get("TZ_APP_INS_ID"));
						System.out.println("appinsid" + appInsId);
					}

					if (appInsId!=""&&!appInsId.equals("")){	
						Long tzAppInsId= Long.parseLong(appInsId);
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
				    
				    
				    
				    
				    
				    
					/*String updateJhuoZt = "UPDATE PS_TZ_AQ_YHXX_TBL SET TZ_JIHUO_ZT=? WHERE OPRID=?";
					jdbcTemplate.update(updateJhuoZt, new Object[]{strJihuoZt,strOprId});*/
					Psoprdefn psoprdefn = new Psoprdefn();
				    psoprdefn.setOprid(strOprId);
				    if("1".equals(strAcctLock)){
				    	psoprdefn.setAcctlock(Short.valueOf("1"));
				    }else{
				    	psoprdefn.setAcctlock(Short.valueOf("0"));
				    }
				    int i = psoprdefnMapper.updateByPrimaryKeySelective(psoprdefn);
				    
				    //保存报名表提交状态
				    ArrayList<String> appStatusUtil=new ArrayList<String>();
				    appStatusUtil=(ArrayList<String>) map.get("updateStatus");
				    if(appStatusUtil!=null&&appStatusUtil.size()>0){
						for (Object obj : appStatusUtil) {
						    Map<String, Object> mapFormData = (Map<String, Object>) obj;
						    String strAppInsId = String.valueOf(mapFormData.get("appInsId"));					    
						    String strStatus = String.valueOf(mapFormData.get("appSubStatus"));
						    String StrUpdateSql = "UPDATE PS_TZ_APP_INS_T SET TZ_APP_FORM_STA='" + strStatus + "' WHERE TZ_APP_INS_ID='" + strAppInsId + "'";
						    jdbcTemplate.update(StrUpdateSql, new Object[]{});
						}
				    }
				    
				    if (i > 0) {
				    	returnJsonMap.replace("OPRID", strOprId);
				    } else {
				    	errMsg[0] = "1";
				    	errMsg[1] = "信息更新保存失败";
				    }
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
