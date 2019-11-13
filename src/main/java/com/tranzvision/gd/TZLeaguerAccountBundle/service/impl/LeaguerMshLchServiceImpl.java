package com.tranzvision.gd.TZLeaguerAccountBundle.service.impl;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsoprdefnMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.Psoprdefn;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzRegUserTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.*;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 申请用户信息；原：TZ_GD_USERGL_PKG:TZ_GD_USER_CLS
 * 
 * @author tang
 * @since 2015-11-20
 */
@Service("com.tranzvision.gd.TZLeaguerAccountBundle.service.impl.LeaguerMshLchServiceImpl")
public class LeaguerMshLchServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsTzRegUserTMapper psTzRegUserTMapper;
	@Autowired
	private com.tranzvision.gd.TZLeaguerAccountBundle.dao.TzImpClpsTblMapper TzImpClpsTblMapper;
	@Autowired
	private com.tranzvision.gd.TZLeaguerAccountBundle.dao.TzImpMspsTblMapper TzImpMspsTblMapper;
	@Autowired
	private com.tranzvision.gd.TZLeaguerAccountBundle.dao.TzImpLkbmTblMapper TzImpLkbmTblMapper;
	@Autowired
	private com.tranzvision.gd.TZLeaguerAccountBundle.dao.TzImpYlqTblMapper TzImpYlqTblMapper;
	@Autowired
	private PsoprdefnMapper psoprdefnMapper;
	@Autowired
	private TZGDObject tzSQLObject;
	
	
	
	/*
	 * hjl添加 2017-4-18
	 * 
	 */
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		
		switch (oprType) {
		case "DYITEMS":
			String oprid="";
			String classid="";
			String appinsid="";
			String lcName="";
			String lcNamedesc="";
			String mshId="";
			String tableName ="";
			Map<String, Object> fristmap=null;
			ArrayList<Map<String, Object>> listData=null;
			//返回的list
			System.out.println("进入DYITEMS");
			ArrayList<Map<String, Object>> returnlistData  = new ArrayList<Map<String, Object>>();
			
			try {
				JacksonUtil jacksonUtil=new JacksonUtil();
				jacksonUtil.json2Map(strParams);
				oprid= jacksonUtil.getString("OPRID");
				
				classid=jacksonUtil.getString("CLASSID");
				
				appinsid=jacksonUtil.getString("appInsID");
			
			 
			    //layout的map
			    Map<String, Object> layoutmap=new HashMap<>();
			    //layout的map的list
			    ArrayList<Map<String, Object>> layoutlist  = new ArrayList<Map<String, Object>>();
			    layoutmap.put("type", "vbox");
			    layoutmap.put("align", "stretch");
			    layoutlist.add(layoutmap);
			    

				 
				
				
				String mshidsql="SELECT TZ_MSH_ID FROM  PS_TZ_FORM_WRK_T  WHERE TZ_CLASS_ID=? AND OPRID=? AND TZ_APP_INS_ID=? ";
				mshId=jdbcTemplate.queryForObject(mshidsql, new Object[]{classid , oprid ,appinsid }, "String");
				
				System.out.println("mshId"+mshId);
				//mshId="20170012";
				String tabsql="SELECT TZ_TPL_ID,TZ_TPL_NAME,TZ_TARGET_TBL FROM TZ_IMP_TPL_DFN_T";
				List<Map<String, Object>> tab_namelist=jdbcTemplate.queryForList(tabsql);
				
				if (tab_namelist.size()>0) {
					for (Map<String, Object> map : tab_namelist) {
						
						lcName=map.get("TZ_TPL_ID")==null?"":map.get("TZ_TPL_ID").toString();
						lcNamedesc=map.get("TZ_TPL_NAME")==null?"":map.get("TZ_TPL_NAME").toString();
						tableName=map.get("TZ_TARGET_TBL")==null?"":map.get("TZ_TARGET_TBL").toString();
			
						
						
						if (tableName == null || "".equals(tableName)) {
							
						}else{
							// 字段id;
							ArrayList<String> fieldIdList = new ArrayList<>();
							// 名称;
							ArrayList<String> fieldNameList = new ArrayList<>();
							// 查询的表字段;
							String fieldSelectSQL = "";
							
							String sql = "select TZ_FIELD,TZ_FIELD_NAME from TZ_IMP_TPL_FLD_T where TZ_TPL_ID=? and TZ_BACK_DISPLAY='Y' order by TZ_SEQ ASC";
							List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { lcName });

							for (int i = 0; i < list.size(); i++) {
								String fieldId = (String) list.get(i).get("TZ_FIELD");
								String fieldName = (String) list.get(i).get("TZ_FIELD_NAME");
								if (fieldName == null) {
									fieldName = "";
								}
								if ("".equals(fieldSelectSQL)) {
									fieldSelectSQL = fieldId;
								} else {
									fieldSelectSQL = fieldSelectSQL + " , " + fieldId;
								}
								fieldIdList.add(fieldId);
								fieldNameList.add(fieldName);
							}
							if(fieldSelectSQL!=""||fieldSelectSQL==null) {
								Map<String, Object> valueMap = null;
								try {
								
									fieldSelectSQL = " select " + fieldSelectSQL + " from " + tableName
											+ " where TZ_BMB_ID = ? ";
								
									valueMap = jdbcTemplate.queryForMap(fieldSelectSQL, new Object[] { appinsid });
								} catch (Exception e1) {
									valueMap = null;
								}
								fristmap=new HashMap<>();
								listData = new ArrayList<Map<String, Object>>();
								for (int i = 0; i < fieldNameList.size(); i++) {
									
									String xxxId = fieldIdList.get(i);
									String xxxmc = fieldNameList.get(i);
									String xxxValue = "";
									if (valueMap != null) {
										xxxValue = valueMap.get(xxxId) == null ? "" : String.valueOf(valueMap.get(xxxId));
									}
									if (xxxValue == null) {
										xxxValue = "";
									}
									Map<String, Object> dynamicitemsMap = new HashMap<>();
								
									dynamicitemsMap.put("xtype", "textfield");
									dynamicitemsMap.put("fieldLabel",xxxmc);
									dynamicitemsMap.put("name", xxxId);
									dynamicitemsMap.put("readOnly", true);
									dynamicitemsMap.put("value", xxxValue);
									
									listData.add(dynamicitemsMap);
									
								}
								fristmap.put("xtype", "fieldset");
								fristmap.put("title", lcNamedesc);
								//fristmap.put("disabled",true);
								fristmap.put("layout", layoutlist);
								fristmap.put("items",listData);
							}else {
								errorMsg[0] = "1";
								errorMsg[1] = "不存在符合条件的考生信息！";
							}
							
							
						}
						returnlistData.add(fristmap);
					}					
				}
				
				return jacksonUtil.List2json(returnlistData);
	
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			break;

		default:
			break;
		}
		
		
		return null;
	}
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
					
					String tzsql1 = tzSQLObject.getSQLText("SQL.TZLeaguerAccountBundle.LeaguerMKSI01");
//					String accoutztSQL = "select (SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_JIHUO_ZT' AND TZ_ZHZ_ID=TZ_JIHUO_ZT AND TZ_EFF_STATUS='A') TZ_JIHUO_ZT, date_format(TZ_ZHCE_DT, '%Y-%m-%d %H:%i:%s') TZ_ZHCE_DT,TZ_MSH_ID from PS_TZ_AQ_YHXX_TBL where OPRID=?";
					Map<String, Object> accoutztMap = jdbcTemplate.queryForMap(tzsql1,new Object[]{str_oprid});
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
					String tzsql2 = tzSQLObject.getSQLText("SQL.TZLeaguerAccountBundle.LeaguerMKSI02");
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
					String tzsql3 = tzSQLObject.getSQLText("SQL.TZLeaguerAccountBundle.LeaguerMLSI01");
//					String str_sqlIndus = "SELECT TZ_OPT_VALUE FROM PS_TZ_YHZC_XXZ_TBL WHERE TZ_REG_FIELD_ID='TZ_COMP_INDUSTRY' AND TZ_SITEI_ID = '' AND TZ_OPT_ID=?";
					str_comIndusDesc = jdbcTemplate.queryForObject(tzsql3, new Object[]{str_comIndus},"String");
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
					//材料评审
					Map< String, Object> clmsMap = jdbcTemplate.queryForMap("SELECT TZ_RESULT,TZ_RESULT_CODE FROM TZ_IMP_CLPS_TBL WHERE TZ_APP_INS_ID=?",new Object[]{appIns});
					if(clmsMap != null){
						ksdrMap.put("clpsJg", clmsMap.get("TZ_RESULT"));
						ksdrMap.put("sfmsZg", clmsMap.get("TZ_RESULT_CODE"));
					}
					
					//面试
					Map< String, Object> msMap = jdbcTemplate.queryForMap("select TZ_TIME,TZ_ADDRESS,TZ_RESULT,TZ_RESULT_CODE from TZ_IMP_MSPS_TBL WHERE TZ_APP_INS_ID=?",new Object[]{appIns});
					if(msMap != null){
						ksdrMap.put("msbdSj", msMap.get("TZ_TIME"));
						ksdrMap.put("msbdDd", msMap.get("TZ_ADDRESS"));
						ksdrMap.put("msJg", msMap.get("TZ_RESULT"));
						ksdrMap.put("msJgBz", msMap.get("TZ_RESULT_CODE"));
					}
					
					//联考报名
					Map< String, Object> lkMap = jdbcTemplate.queryForMap("select TZ_YEAR,TZ_SCORE,TZ_ENGLISH,TZ_COMPREHENSIVE,TZ_OVERLINE,TZ_POLITICS,TZ_ENG_LISTENING,TZ_POLLSN_OVERLINE,TZ_PRE_ADMISSION,TZ_STU_NUM,TZ_DEGREE_CHECK,TZ_STU_NUM_LAST4,TZ_POL_ENG_TIME,TZ_POL_ENG_ADDR,TZ_SCHOLARSHIP_STA,TZ_POLLSN_DESC from TZ_IMP_LKBM_TBL WHERE TZ_APP_INS_ID=?",new Object[]{appIns});
					if(lkMap != null){
						ksdrMap.put("lkNf", lkMap.get("TZ_YEAR"));
						ksdrMap.put("lkZf", lkMap.get("TZ_SCORE"));
						ksdrMap.put("lkYy", lkMap.get("TZ_ENGLISH"));
						ksdrMap.put("lkZh", lkMap.get("TZ_COMPREHENSIVE"));
						ksdrMap.put("lkSfgx", lkMap.get("TZ_OVERLINE"));
						ksdrMap.put("Zz", lkMap.get("TZ_POLITICS"));
						ksdrMap.put("yyTl", lkMap.get("TZ_ENG_LISTENING"));
						ksdrMap.put("zzTlSfGx", lkMap.get("TZ_POLLSN_OVERLINE"));
						ksdrMap.put("sfYlq", lkMap.get("TZ_PRE_ADMISSION"));
						ksdrMap.put("lkksBh", lkMap.get("TZ_STU_NUM"));
						ksdrMap.put("lkbmsXlJyZt", lkMap.get("TZ_DEGREE_CHECK"));
						ksdrMap.put("ksbhHsw", lkMap.get("TZ_STU_NUM_LAST4"));
						ksdrMap.put("zzYyTlKsSj", lkMap.get("TZ_POL_ENG_TIME"));
						ksdrMap.put("zzYyTlKsDd", lkMap.get("TZ_POL_ENG_ADDR"));
						ksdrMap.put("ssJxjSqZt", lkMap.get("TZ_SCHOLARSHIP_STA"));
						ksdrMap.put("zzTlKsBz", lkMap.get("TZ_POLLSN_DESC"));
					}
					
					//预录取
					Map< String, Object> ylqMap = jdbcTemplate.queryForMap("select TZ_SCHOLARSHIP_RST,TZ_TUITION_REFERENCE,TZ_STU_ID,TZ_PYXY_ACCEPT,TZ_GZZM_ACCEPT,TZ_CLASS_RST,TZ_EMAIL,TZ_INITIAL_PSWD from TZ_IMP_YLQ_TBL WHERE TZ_APP_INS_ID=?",new Object[]{appIns});
					if(ylqMap != null){
						ksdrMap.put("ssJxjZzJg", ylqMap.get("TZ_SCHOLARSHIP_RST"));
						ksdrMap.put("xfZeCk", ylqMap.get("TZ_TUITION_REFERENCE"));
						ksdrMap.put("xh", ylqMap.get("TZ_STU_ID"));
						ksdrMap.put("pyXyJs", ylqMap.get("TZ_PYXY_ACCEPT"));
						ksdrMap.put("zzZmJs", ylqMap.get("TZ_GZZM_ACCEPT"));
						ksdrMap.put("fbJg", ylqMap.get("TZ_CLASS_RST"));
						ksdrMap.put("jgYx", ylqMap.get("TZ_EMAIL"));
						ksdrMap.put("yxCsMm", ylqMap.get("TZ_INITIAL_PSWD"));
					}
					
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
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				if(jacksonUtil.containsKey("data")){
				    Map<String, Object> map = jacksonUtil.getMap("data");
				    // 用户账号;
				    String strOprId = (String) map.get("OPRID");
				    String strAppId = (String) map.get("APPID");
				    Long appInsId = Long.valueOf(strAppId);
				    //材料评审
				    String strExistsSql = "SELECT 'Y' FROM TZ_IMP_CLPS_TBL WHERE TZ_APP_INS_ID=?";
				    String strExistsFlg = jdbcTemplate.queryForObject(strExistsSql, new Object[]{strAppId}, "String");
				    				    
				    String str_clpsJg = (String) map.get("clpsJg");
				    String str_sfmsZg = (String) map.get("sfmsZg");
				    TzImpClpsTbl tzImpClpsTbl = new TzImpClpsTbl();
				    tzImpClpsTbl.setTzAppInsId(appInsId);
				    tzImpClpsTbl.setTzResult(str_clpsJg);
				    tzImpClpsTbl.setTzResultCode(str_sfmsZg);
					
				    if("Y".equals(strExistsFlg)){
					TzImpClpsTblMapper.updateByPrimaryKeySelective(tzImpClpsTbl);
				    }else{
					TzImpClpsTblMapper.insert(tzImpClpsTbl);
				    }
				    
				    //面试
				    strExistsSql = "SELECT 'Y' FROM TZ_IMP_MSPS_TBL WHERE TZ_APP_INS_ID=?";
				    strExistsFlg = jdbcTemplate.queryForObject(strExistsSql, new Object[]{strAppId}, "String");
				    String str_msbdSj = (String) map.get("msbdSj");
				    String str_msbdDd = (String) map.get("msbdDd");
				    String str_msJg = (String) map.get("msJg");
				    String str_msJgBz = (String) map.get("msJgBz");
				    TzImpMspsTbl tzImpMspsTbl = new TzImpMspsTbl();
				    tzImpMspsTbl.setTzAppInsId(appInsId);
				    tzImpMspsTbl.setTzAddress(str_msbdDd);
				    tzImpMspsTbl.setTzTime(str_msbdSj);
				    tzImpMspsTbl.setTzResult(str_msJg);
				    tzImpMspsTbl.setTzResultCode(str_msJgBz);

				    if("Y".equals(strExistsFlg)){
					TzImpMspsTblMapper.updateByPrimaryKeySelective(tzImpMspsTbl);
				    }else{
					TzImpMspsTblMapper.insert(tzImpMspsTbl);
				    }
				    
				    //联考报名
				    strExistsSql = "SELECT 'Y' FROM TZ_IMP_LKBM_TBL WHERE TZ_APP_INS_ID=?";
				    strExistsFlg = jdbcTemplate.queryForObject(strExistsSql, new Object[]{strAppId}, "String");
				    String str_lkNf = (String) map.get("lkNf");
				    String str_lkZf = (String) map.get("lkZf");
				    String str_lkYy = (String) map.get("lkYy");
				    String str_lkZh = (String) map.get("lkZh");
				    String str_lkSfgx = (String) map.get("lkSfgx");
				    String str_Zz = (String) map.get("Zz");
				    String str_yyTl = (String) map.get("yyTl");
				    String str_zzTlSfGx = (String) map.get("zzTlSfGx");
				    String str_sfYlq = (String) map.get("sfYlq");
				    String str_lkksBh = (String) map.get("lkksBh");
				    String str_lkbmsXlJyZt = (String) map.get("lkbmsXlJyZt");
				    String str_ksbhHsw = (String) map.get("ksbhHsw");
				    String str_zzYyTlKsSj = (String) map.get("zzYyTlKsSj");
				    String str_zzYyTlKsDd = (String) map.get("zzYyTlKsDd");
				    String str_ssJxjSqZt = (String) map.get("ssJxjSqZt");
				    String str_zzTlKsBz = (String) map.get("zzTlKsBz");
				    
				    
				    TzImpLkbmTbl tzImpLkbmTbl = new TzImpLkbmTbl();
				    tzImpLkbmTbl.setTzAppInsId(appInsId);
				    tzImpLkbmTbl.setTzYear(str_lkNf);
				    tzImpLkbmTbl.setTzScore(str_lkZf);
				    tzImpLkbmTbl.setTzEnglish(str_lkYy);
				    tzImpLkbmTbl.setTzComprehensive(str_lkZh);
				    tzImpLkbmTbl.setTzOverline(str_lkSfgx);
				    tzImpLkbmTbl.setTzPolitics(str_Zz);
				    tzImpLkbmTbl.setTzEngListening(str_yyTl);				    
				    tzImpLkbmTbl.setTzPollsnOverline(str_zzTlSfGx);
				    tzImpLkbmTbl.setTzPreAdmission(str_sfYlq);
				    tzImpLkbmTbl.setTzStuNum(str_lkksBh);
				    tzImpLkbmTbl.setTzDegreeCheck(str_lkbmsXlJyZt);
				    tzImpLkbmTbl.setTzStuNumLast4(str_ksbhHsw);
				    tzImpLkbmTbl.setTzPolEngTime(str_zzYyTlKsSj);
				    tzImpLkbmTbl.setTzPolEngAddr(str_zzYyTlKsDd);
				    tzImpLkbmTbl.setTzScholarshipSta(str_ssJxjSqZt);
				    tzImpLkbmTbl.setTzPollsnDesc(str_zzTlKsBz);				    

				    if("Y".equals(strExistsFlg)){
					TzImpLkbmTblMapper.updateByPrimaryKeySelective(tzImpLkbmTbl);
				    }else{
					TzImpLkbmTblMapper.insert(tzImpLkbmTbl);
				    }
				    
				    //材料评审
				    strExistsSql = "SELECT 'Y' FROM TZ_IMP_YLQ_TBL WHERE TZ_APP_INS_ID=?";
				    strExistsFlg = jdbcTemplate.queryForObject(strExistsSql, new Object[]{strAppId}, "String");
				    				    
				    String str_ssJxjZzJg = (String) map.get("ssJxjZzJg");
				    String str_xfZeCk = (String) map.get("xfZeCk");
				    String str_xh = (String) map.get("xh");
				    String str_pyXyJs = (String) map.get("pyXyJs");
				    String str_zzZmJs = (String) map.get("zzZmJs");
				    String str_fbJg = (String) map.get("fbJg");
				    String str_jgYx = (String) map.get("jgYx");
				    String str_yxCsMm = (String) map.get("yxCsMm");
				    
				    TzImpYlqTbl tzImpYlqTbl = new TzImpYlqTbl();
				    tzImpYlqTbl.setTzAppInsId(appInsId);
				    tzImpYlqTbl.setTzScholarshipRst(str_ssJxjZzJg);
				    tzImpYlqTbl.setTzTuitionReference(str_xfZeCk);
				    tzImpYlqTbl.setTzStuId(str_xh);
				    tzImpYlqTbl.setTzPyxyAccept(str_pyXyJs);
				    tzImpYlqTbl.setTzGzzmAccept(str_zzZmJs);
				    tzImpYlqTbl.setTzClassRst(str_fbJg);
				    tzImpYlqTbl.setTzEmail(str_jgYx);
				    tzImpYlqTbl.setTzInitialPswd(str_yxCsMm);
					
				    if("Y".equals(strExistsFlg)){
					TzImpYlqTblMapper.updateByPrimaryKeySelective(tzImpYlqTbl);
				    }else{
					TzImpYlqTblMapper.insert(tzImpYlqTbl);
				    }
				    
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
