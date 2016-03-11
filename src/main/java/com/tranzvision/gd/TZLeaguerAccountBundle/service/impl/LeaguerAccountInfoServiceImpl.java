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
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzRegUserT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

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
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsTzRegUserTMapper psTzRegUserTMapper;
	@Autowired
	private PsoprdefnMapper psoprdefnMapper;
	
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
					
					
					String accoutztSQL = "select (SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_JIHUO_ZT' AND TZ_ZHZ_ID=TZ_JIHUO_ZT AND TZ_EFF_STATUS='A') TZ_JIHUO_ZT, date_format(TZ_ZHCE_DT, '%Y-%m-%d %H:%i:%s') TZ_ZHCE_DT from PS_TZ_AQ_YHXX_TBL where OPRID=?";
					Map<String, Object> accoutztMap = jdbcTemplate.queryForMap(accoutztSQL,new Object[]{str_oprid});
					if(phoneAndEmailMap != null){
						str_jihuozt = (String) accoutztMap.get("TZ_JIHUO_ZT");
						str_jihuozt = (str_jihuozt != null) ? str_jihuozt : "";
						
						str_zc_time = (String) accoutztMap.get("TZ_ZHCE_DT");
						str_zc_time = (str_zc_time != null) ? str_zc_time : "";
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
								arraylist.add(jsonMap);
							}
						}
					}
					
					jsonMap = new HashMap<>();
					jsonMap.put("TZ_SKYPE", str_skype);
					jsonMap.put("desc", "Sky账号");
					arraylist.add(jsonMap);
					
				}
				
				Map<String, Object> jsonMap2 = new HashMap<String, Object>();
				jsonMap2.put("OPRID", str_oprid);
				jsonMap2.put("userName", str_name);
				jsonMap2.put("userSex",str_sex );
				jsonMap2.put("userEmail",str_email );
				jsonMap2.put("userPhone",str_phone);
				jsonMap2.put("jihuoZt",str_jihuozt );
				jsonMap2.put("acctlock",str_acctlook );
				jsonMap2.put("zcTime",str_zc_time);
				jsonMap2.put("titleImageUrl",titleImageUrl );
				jsonMap2.put("column",arraylist );
				
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

				    Psoprdefn psoprdefn = new Psoprdefn();
				    psoprdefn.setOprid(strOprId);
				    if("1".equals(strAcctLock)){
				    	psoprdefn.setAcctlock(Short.valueOf("1"));
				    }else{
				    	psoprdefn.setAcctlock(Short.valueOf("0"));
				    }
				    
				    int i = psoprdefnMapper.updateByPrimaryKeySelective(psoprdefn);
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
