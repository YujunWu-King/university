package com.tranzvision.gd.TZLeaguerAccountBundle.service.impl;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsoprdefnMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.Psoprdefn;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzRegUserTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzRegUserT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzKshLqlcTblMapper;
//import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzKshLqlcTbl;

/**
 * 申请用户信息；原：TZ_GD_USERGL_PKG:TZ_GD_USER_CLS
 * 
 * @author tang
 * @since 2015-11-20
 */
@Service("com.tranzvision.gd.TZLeaguerAccountBundle.service.impl.LeaguerAccountInfoServiceImpl")
public class LeaguerAccountInfoServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsTzRegUserTMapper psTzRegUserTMapper;
	@Autowired
	private PsoprdefnMapper psoprdefnMapper;
	@Autowired
	private TZGDObject tzSQLObject;
	//@Autowired
	//private PsTzKshLqlcTblMapper psTzKshLqlcTblMapper;

	
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
				//邮箱、手机;
				String str_email = "", str_phone= "";
				//邮箱绑定标识
				String bindEmailFlg="";
				//手机绑定标识
				String bindPhoneFlg="";
				//账号激活状态、创建日期时间;
				String str_jihuozt = "" , str_zc_time = "";
				//身份证
				String str_ksNationId="";
				//公司、职位、省份
				String comment1="";
				//真实姓名
				String realname="";
				Map<String, Object> arraylist = new HashMap<>();
				
				
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
					//添加身份证号
					str_ksNationId=psTzRegUserT.getNationalId();
					//-----------------
					comment1 = psTzRegUserT.getTzComment1();

					String phoneAndEmailSQL="SELECT TZ_REALNAME,TZ_EMAIL,TZ_MOBILE,TZ_YXBD_BZ,TZ_SJBD_BZ FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_RYLX='ZCYH' AND OPRID=?";
					Map<String, Object> phoneAndEmailMap = jdbcTemplate.queryForMap(phoneAndEmailSQL,new Object[]{str_oprid});
					//个人信息中的邮箱，手机，手机标识，邮箱标识
					if(phoneAndEmailMap != null){
						str_name=phoneAndEmailMap.get("TZ_REALNAME")==null?"":String.valueOf(phoneAndEmailMap.get
								("TZ_REALNAME"));
						str_email = (String) phoneAndEmailMap.get("TZ_EMAIL");
						str_email = (str_email != null) ? str_email : "";
						
						str_phone = (String) phoneAndEmailMap.get("TZ_MOBILE");
						str_phone = (str_phone != null) ? str_phone : "";

						bindEmailFlg=phoneAndEmailMap.get("TZ_YXBD_BZ")==null?"":String.valueOf(phoneAndEmailMap.get
								("TZ_YXBD_BZ"));

						bindPhoneFlg=phoneAndEmailMap.get("TZ_SJBD_BZ")==null?"":String.valueOf(phoneAndEmailMap.get
								("TZ_SJBD_BZ"));
					} 
					
					String tzsql1 = tzSQLObject.getSQLText("SQL.TZLeaguerAccountBundle.LeaguerAISI01");
//					String accoutztSQL = "select TZ_JIHUO_ZT, date_format(TZ_ZHCE_DT, '%Y-%m-%d %H:%i:%s') TZ_ZHCE_DT,TZ_MSH_ID from PS_TZ_AQ_YHXX_TBL where OPRID=?";
					Map<String, Object> accoutztMap = jdbcTemplate.queryForMap(tzsql1,new Object[]{str_oprid});
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
					String tzsql2 = tzSQLObject.getSQLText("SQL.TZLeaguerAccountBundle.LeaguerAISI02");
//					String sqlList = "select TZ_REG_FIELD_ID,TZ_RED_FLD_YSMC,TZ_REG_FIELD_NAME,TZ_FIELD_TYPE from PS_TZ_REG_FIELD_T where TZ_JG_ID=? and TZ_ENABLE='Y' order by TZ_ORDER asc limit 7,1000";
					List<Map<String, Object>> list = jdbcTemplate.queryForList(tzsql2, new Object[]{strJgid});
					
					
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
					
					realname= psTzRegUserT.getTzRealname();//真实姓名
					arraylist.put("realname",realname);
					arraylist.put("lenProvince", str_lenProvince);
					arraylist.put("companyname", str_comName);
					arraylist.put("copmindustry", str_comIndus);
					arraylist.put("shcoolcname", str_schName);
				}
				
				//考生导入信息;
				//查询当前人员的报名表id;
//				Long appIns = jdbcTemplate.queryForObject(" select TZ_APP_INS_ID from PS_TZ_FORM_WRK_T WHERE OPRID=? ORDER BY ROW_LASTMANT_DTTM LIMIT 0,1", new Object[]{str_oprid},"Long");
//				Map<String, Object> ksdrMap = new HashMap<>();
//
//				if(appIns != null && appIns > 0){
					//材料评审
//					Map< String, Object> clmsMap = jdbcTemplate.queryForMap("SELECT TZ_RESULT,TZ_RESULT_CODE FROM TZ_IMP_CLPS_TBL WHERE TZ_APP_INS_ID=?",new Object[]{appIns});
//					if(clmsMap != null){
//						ksdrMap.put("clpsJg", clmsMap.get("TZ_RESULT"));
//						ksdrMap.put("sfmsZg", clmsMap.get("TZ_RESULT_CODE"));
//					}

					//面试
//					Map< String, Object> msMap = jdbcTemplate.queryForMap("select TZ_TIME,TZ_ADDRESS,TZ_RESULT,TZ_RESULT_CODE from TZ_IMP_MSPS_TBL WHERE TZ_APP_INS_ID=?",new Object[]{appIns});
//					if(msMap != null){
//						ksdrMap.put("msbdSj", msMap.get("TZ_TIME"));
//						ksdrMap.put("msbdDd", msMap.get("TZ_ADDRESS"));
//						ksdrMap.put("msJg", msMap.get("TZ_RESULT"));
//						ksdrMap.put("msJgBz", msMap.get("TZ_RESULT_CODE"));
//					}

					//联考报名
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

//				}
//				//tmt 修改 2017-4-7录取流程信息
//				Map<String, Object> lqlcInfoMap = new HashMap<>();
//				//lqlcInfoMap.put("lqlc", str_lenProvince);
//				Map< String, Object> getlqlcInfoMap = jdbcTemplate.queryForMap("SELECT A.OPRID,A.TZ_MSPS_MC,A.TZ_MSPS_PC,A.TZ_TJLQZG,A.TZ_TJLQZG_XM,A.TZ_MSJG_PC,A.TZ_LKQ_TZQK,A.TZ_TZYY_NOTES,A.TZ_LKQ_TJLQZG,A.TZ_LKQ_TJLQZG_XM,A.TZ_LKBM,A.TZ_LKSK,A.TZ_LKGX,A.TZ_LKZZGX,A.TZ_LKYYTLGX,A.TZ_YLQ_TZQK,A.TZ_TZYY_NOTES2,A.TZ_YLQ_ZG,A.TZ_YLQ_ZG_XM,A.TZ_ZSLQ_TZQK,A.TZ_TZYY_NOTES3,A.TZ_ZSLQ_ZG,A.TZ_ZSLQ_ZG_XM,A.TZ_RXQ_TZQK,A.TZ_TZYY_NOTES4,A.TZ_RX_QK,A.TZ_RX_XM FROM PS_TZ_KSH_LQLC_TBL A ,PS_TZ_LXFSINFO_TBL B where A.OPRID = B.TZ_LYDX_ID AND A.OPRID=?",new Object[]{str_oprid});
//				if(getlqlcInfoMap!=null){
//					lqlcInfoMap.put("mspsmc", getlqlcInfoMap.get("TZ_MSPS_MC"));
//					lqlcInfoMap.put("mspspc", getlqlcInfoMap.get("TZ_MSPS_PC"));
//					lqlcInfoMap.put("tjlqzg", getlqlcInfoMap.get("TZ_TJLQZG"));
//					lqlcInfoMap.put("tjlqzgxm", getlqlcInfoMap.get("TZ_TJLQZG_XM"));
//					lqlcInfoMap.put("msjgpc", getlqlcInfoMap.get("TZ_MSJG_PC"));
//
//					lqlcInfoMap.put("lkbm", getlqlcInfoMap.get("TZ_LKBM"));
//					lqlcInfoMap.put("lqlzqk", getlqlcInfoMap.get("TZ_LKQ_TZQK"));
//					lqlcInfoMap.put("tzyyNotes", getlqlcInfoMap.get("TZ_TZYY_NOTES"));
//					lqlcInfoMap.put("lkqtjluzg", getlqlcInfoMap.get("TZ_LKQ_TJLQZG"));
//					lqlcInfoMap.put("lkqtjluzgxm", getlqlcInfoMap.get("TZ_LKQ_TJLQZG_XM"));
//
//					lqlcInfoMap.put("lksk", getlqlcInfoMap.get("TZ_LKSK"));
//					lqlcInfoMap.put("yytlgx", getlqlcInfoMap.get("TZ_LKYYTLGX"));
//					lqlcInfoMap.put("zzgx", getlqlcInfoMap.get("TZ_LKZZGX"));
//					lqlcInfoMap.put("lkgx", getlqlcInfoMap.get("TZ_LKGX"));
//					lqlcInfoMap.put("ylqzzqk", getlqlcInfoMap.get("TZ_YLQ_TZQK"));
//
//					lqlcInfoMap.put("tzyyNotes2", getlqlcInfoMap.get("TZ_TZYY_NOTES2"));
//					lqlcInfoMap.put("ylqzg", getlqlcInfoMap.get("TZ_YLQ_ZG"));
//					lqlcInfoMap.put("ylqzgxm", getlqlcInfoMap.get("TZ_YLQ_ZG_XM"));
//					lqlcInfoMap.put("zslqzzqk", getlqlcInfoMap.get("TZ_ZSLQ_TZQK"));
//					lqlcInfoMap.put("tzyyNotes3", getlqlcInfoMap.get("TZ_TZYY_NOTES3"));
//
//					lqlcInfoMap.put("zslqzg", getlqlcInfoMap.get("TZ_ZSLQ_ZG"));
//					lqlcInfoMap.put("zslqzgxm", getlqlcInfoMap.get("TZ_ZSLQ_ZG_XM"));
//					lqlcInfoMap.put("rxqzzqk", getlqlcInfoMap.get("TZ_RXQ_TZQK"));
//					lqlcInfoMap.put("tzyyNotes4", getlqlcInfoMap.get("TZ_TZYY_NOTES4"));
//					lqlcInfoMap.put("rxqk", getlqlcInfoMap.get("TZ_RX_QK"));
//					lqlcInfoMap.put("rxxm", getlqlcInfoMap.get("TZ_RX_XM"));
//
//
//
//
//
//				}
				//查询考生个人信息
				//出生日期、联系电话、证件类型、证件号码、工作所在省市、考生编号、准考证号、是否有海外学历、申请的专业硕士和专业、紧急联系人、紧急联系人性别、紧急联系人手机号码
				//str_lenProvince	
				Map<String, Object> perInfoMap = new HashMap<>();
				perInfoMap.put("lenProvince", str_lenProvince);
				Map< String, Object> grxxMap = jdbcTemplate.queryForMap("SELECT A.OPRID,A.TZ_REALNAME,A.BIRTHDATE,A.NATIONAL_ID_TYPE,A.NATIONAL_ID,B.TZ_ZY_SJ,A.TZ_COMMENT4,A.TZ_COMMENT9,A.TZ_COMMENT10,A.TZ_COMMENT11 FROM PS_TZ_REG_USER_T A LEFT JOIN PS_TZ_LXFSINFO_TBL B ON A.OPRID=B.TZ_LYDX_ID WHERE A.OPRID=?",new Object[]{str_oprid});
				if(grxxMap!=null){
				    perInfoMap.put("birthdate", grxxMap.get("BIRTHDATE"));
				    perInfoMap.put("zyPhone", grxxMap.get("TZ_ZY_SJ"));
				    perInfoMap.put("nationType", grxxMap.get("NATIONAL_ID_TYPE"));
				    perInfoMap.put("nationId", grxxMap.get("NATIONAL_ID"));
				    perInfoMap.put("jjlxr", grxxMap.get("TZ_COMMENT9"));
				    perInfoMap.put("jjlxrSex", grxxMap.get("TZ_COMMENT10"));
				    perInfoMap.put("jjlxrPhone", grxxMap.get("TZ_COMMENT11"));
				    perInfoMap.put("kshNo","");
				    perInfoMap.put("isHaiwXuel", grxxMap.get("TZ_COMMENT4"));
				    
				}
				Map<String, Object> jsonMap2 = new HashMap<String, Object>();
				jsonMap2.put("OPRID", str_oprid);
				jsonMap2.put("msSqh", str_msSqh);
				jsonMap2.put("userName", str_name);
				jsonMap2.put("userSex",str_sex );
				jsonMap2.put("userEmail",str_email );
				jsonMap2.put("userPhone",str_phone);
				jsonMap2.put("bindEmailFlg",bindEmailFlg);
				jsonMap2.put("bindPhoneFlg",bindPhoneFlg);
				jsonMap2.put("jihuoZt",str_jihuozt );
				jsonMap2.put("acctlock",str_acctlook );
				jsonMap2.put("zcTime",str_zc_time);
				jsonMap2.put("titleImageUrl",titleImageUrl );
				jsonMap2.put("column",arraylist );
				jsonMap2.put("perInfo", perInfoMap);
				if(!"Y".equals(str_blackName)){
				    str_blackName = "N";
				}
				jsonMap2.put("blackName",str_blackName );
				if(!"Y".equals(str_allowApply)){
				    str_allowApply = "N";
				}
				jsonMap2.put("allowApply",str_allowApply );
				jsonMap2.put("beizhu",str_beizhu );
				jsonMap2.put("nationId", str_ksNationId);
				jsonMap2.put("comment1", comment1);
				jsonMap2.put("companyName", str_comName);
				jsonMap2.put("lenProid", str_lenProvince);
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
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				if(jacksonUtil.containsKey("data")){
					Map<String, Object> map = jacksonUtil.getMap("data");
					// 用户账号;
				    String strOprId = (String) map.get("OPRID");
				    // 锁定状态;
				    String strAcctLock = (String) map.get("acctlock");
				    // 激活状态;
				    String strJihuoZt = (String) map.get("jihuoZt");
				    // 黑名单状态
				    String strBlackName = (String) map.get("blackName");
				    String strAllowApply = (String) map.get("allowApply");
				    String strBeiZhu = (String) map.get("beizhu");
				    //身份证
				    String strnationId=(String) map.get("nationId");
				    //公司
				    String companyName =(String) map.get("companyName");
				    //职位
				    String comment1 =(String) map.get("comment1");
				    //省份
				    String lenProid =(String) map.get("lenProid"); 
				    //姓名
				    String userName =(String) map.get("userName");
				    //性别
					String userSex=String.valueOf(map.get("userSex"));
					//手机号
					String userPhone=String.valueOf(map.get("userPhone"));
					//邮箱
					String userEmail=String.valueOf(map.get("userEmail"));
					//邮箱绑定标识
					String bindEmailFlg=String.valueOf(map.get("bindEmailFlg"));
					//手机绑定标识
					String bindPhoneFlg=String.valueOf(map.get("bindPhoneFlg"));
					//个人信息
					//真实姓名
					String realname=String.valueOf(map.get("realname"));
					//本科院校
					String shcoolcname=String.valueOf(map.get("shcoolcname"));
					//常驻州省
					String lenProvince=String.valueOf(map.get("lenProvince"));
					//工作单位
					String companyname=String.valueOf(map.get("companyname"));
					//工作类别
					String copmindustry=String.valueOf(map.get("copmindustry"));
				    Psoprdefn psoprdefn = new Psoprdefn();
				    psoprdefn.setOprid(strOprId);
				    if("1".equals(strAcctLock)){
				    	psoprdefn.setAcctlock(Short.valueOf("1"));
				    }else{
				    	psoprdefn.setAcctlock(Short.valueOf("0"));
				    }
				    //更新TZ_LXFSINFO_TBL中的手机信息和邮箱
                    String phoneAndemailSql="UPDATE PS_TZ_LXFSINFO_TBL SET TZ_ZY_SJ=? ,TZ_ZY_EMAIL=? WHERE TZ_LXFS_LY='ZCYH' AND TZ_LYDX_ID=?";
				    jdbcTemplate.update(phoneAndemailSql, new Object[]{userPhone, userEmail, strOprId});
                    if("F".equals(userSex)||("M").equals(userSex)){
						//添加用户信息表中的身份证号
						String updatelSFSSql = "UPDATE PS_TZ_REG_USER_T SET TZ_BLACK_NAME=?,TZ_ALLOW_APPLY=?,TZ_BEIZHU=?,NATIONAL_ID=?,TZ_COMPANY_NAME=?,TZ_COMMENT1=?,TZ_LEN_PROID=?,TZ_GENDER=?,TZ_REALNAME=?,TZ_SCH_CNAME=?,TZ_LEN_PROID=?," +
								"TZ_COMPANY_NAME=?,TZ_COMP_INDUSTRY=? WHERE OPRID=?";
						jdbcTemplate.update(updatelSFSSql, new Object[]{strBlackName,strAllowApply,strBeiZhu,strnationId,companyName, comment1,lenProid,userSex,realname,shcoolcname,lenProvince,companyname,copmindustry,strOprId});
					}else{
						//添加用户信息表中的身份证号
						String updatelSFSSql = "UPDATE PS_TZ_REG_USER_T SET TZ_BLACK_NAME=?,TZ_ALLOW_APPLY=?,TZ_BEIZHU=?,NATIONAL_ID=?,TZ_COMPANY_NAME=?,TZ_COMMENT1=?,TZ_LEN_PROID=?,TZ_REALNAME=?,TZ_SCH_CNAME=?,TZ_LEN_PROID=?," +
								"TZ_COMPANY_NAME=?,TZ_COMP_INDUSTRY=? WHERE OPRID=?";
						jdbcTemplate.update(updatelSFSSql, new Object[]{strBlackName,strAllowApply,strBeiZhu,strnationId,companyName, comment1,lenProid,realname,shcoolcname,lenProvince,companyname,copmindustry,strOprId});
					}

				    //同步修改报名表身份证 
				    
					String oldstrnationId = "";
					String sql = "SELECT TZ_APP_S_TEXT FROM  PS_TZ_APP_CC_T WHERE ";
					sql += "TZ_APP_INS_ID=(SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID = ? AND ROW_ADDED_DTTM = (SELECT MAX(ROW_ADDED_DTTM) FROM PS_TZ_FORM_WRK_T WHERE OPRID = ?))";
					sql += "AND TZ_XXX_BH=(select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT='TZ_BMB_XXX_NATIONID')";
					oldstrnationId = jdbcTemplate.queryForObject(sql, new Object[] { strOprId, strOprId }, "String");
					// 同步修改报名表PS_TZ_APP_CC_T 表-身份证
					sql = "UPDATE PS_TZ_APP_CC_T SET TZ_APP_S_TEXT=? WHERE ";
					sql += "TZ_APP_INS_ID=(SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID = ? AND ROW_ADDED_DTTM = (SELECT MAX(ROW_ADDED_DTTM) FROM PS_TZ_FORM_WRK_T WHERE OPRID = ?))";
					sql += "AND TZ_XXX_BH=(select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT='TZ_BMB_XXX_NATIONID')";
					jdbcTemplate.update(sql, new Object[] { strnationId, strOprId, strOprId });

					// 同步修改报名表PS_TZ_APP_INS_T 表-身份证
					if (!"".equals(oldstrnationId) && oldstrnationId != null) {
						String sql1 = "UPDATE PS_TZ_APP_INS_T SET TZ_APPINS_JSON_STR=replace(TZ_APPINS_JSON_STR,?,?)";
						sql1 += "WHERE TZ_APP_INS_ID=(SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID = ? AND ROW_ADDED_DTTM = (SELECT MAX(ROW_ADDED_DTTM) FROM PS_TZ_FORM_WRK_T WHERE OPRID = ?))";
						jdbcTemplate.update(sql1, new Object[] { oldstrnationId, strnationId, strOprId, strOprId });

					}
					/**
					 * 张超 时间：2019年12月11日12:23:45 备注：在原有性别，邮箱，邮箱绑定标识，手机，手机绑定标识未保存到后台，修改sql完成保存
					 */
					String updateJhuoZt = "UPDATE PS_TZ_AQ_YHXX_TBL SET TZ_JIHUO_ZT=?,TZ_REALNAME=?,TZ_YXBD_BZ=?," +
							"TZ_SJBD_BZ=?,TZ_EMAIL=?,TZ_MOBILE=? WHERE OPRID=?";
					jdbcTemplate.update(updateJhuoZt, new Object[]{strJihuoZt,userName,bindEmailFlg,bindPhoneFlg,
							userEmail,userPhone,strOprId});
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
            e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
}
