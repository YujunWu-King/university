package com.tranzvision.gd.TZSmsTemplateBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZSmsTemplateBundle.dao.PsTzSmsServTblMapper;
import com.tranzvision.gd.TZSmsTemplateBundle.model.PsTzSmsServTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * PS: TZ_GD_EMLSMSSET_PKG:TZ_GD_SMSINFO_CLS
 * @author tang
 * 功能描述：短信服务器配置查询  
 */
@Service("com.tranzvision.gd.TZSmsTemplateBundle.service.impl.TzGdSmsInfClsServiceImpl")
public class TzGdSmsInfClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzSmsServTblMapper psTzSmsServTblMapper;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	
	/* 新增邮件模板设置信息 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 服务器编号;
		        String strSmsid = jacksonUtil.getString("smssevid");
		        // 短信服务器名称;
		        String strSmsname = jacksonUtil.getString("smssevname");
		        // 账户;
		        String strUsrname = jacksonUtil.getString("usrname");
		        // 密码;
		        String strUsrpwd = jacksonUtil.getString("usrpwd");
		        // 识别来源子号码;
		        String strSourceno = jacksonUtil.getString("sourceno");
		        // 描述;
		        String strDesc = jacksonUtil.getString("desc");
		        
		        String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		        PsTzSmsServTbl psTzSmsServTbl = psTzSmsServTblMapper.selectByPrimaryKey(strSmsid);
		        if(psTzSmsServTbl != null){
		        	psTzSmsServTbl.setTzSmsServName(strSmsname);;
		        	psTzSmsServTbl.setTzSmsUsrName(strUsrname);
		        	psTzSmsServTbl.setTzSmsUsrPwd(strUsrpwd);
		        	psTzSmsServTbl.setTzSourceNo(strSourceno);
		        	psTzSmsServTbl.setTzDescr254(strDesc);
		        	psTzSmsServTbl.setRowLastmantOprid(oprid);
		        	psTzSmsServTbl.setRowLastmantDttm(new Date());
		        	psTzSmsServTblMapper.updateByPrimaryKeySelective(psTzSmsServTbl);
		        }else{
		        	psTzSmsServTbl = new PsTzSmsServTbl();
		        	psTzSmsServTbl.setTzSmsServId(strSmsid);
		        	psTzSmsServTbl.setTzSmsServName(strSmsname);;
		        	psTzSmsServTbl.setTzSmsUsrName(strUsrname);
		        	psTzSmsServTbl.setTzSmsUsrPwd(strUsrpwd);
		        	psTzSmsServTbl.setTzSourceNo(strSourceno);
		        	psTzSmsServTbl.setTzDescr254(strDesc);
		        	psTzSmsServTbl.setRowAddedOprid(oprid);
		        	psTzSmsServTbl.setRowAddedDttm(new Date());
		        	psTzSmsServTbl.setRowLastmantOprid(oprid);
		        	psTzSmsServTbl.setRowLastmantDttm(new Date());
		        	psTzSmsServTblMapper.insert(psTzSmsServTbl);
		        }
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	
	/* 查询表单信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("smssevid", "");
		returnJsonMap.put("smssevname", "");
		returnJsonMap.put("usrname", "");
		returnJsonMap.put("usrpwd", "");
		returnJsonMap.put("sourceno", "");
		returnJsonMap.put("desc", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			Map<String, Object> map = jdbcTemplate.queryForMap("SELECT TZ_SMS_SERV_ID,TZ_SMS_SERV_NAME,TZ_SMS_USR_NAME,TZ_SMS_USR_PWD,TZ_SOURCE_NO,TZ_DESCR254 FROM PS_TZ_SMSSERV_TBL limit 0,1");
			if(map != null){
				returnJsonMap.replace("smssevid", map.get("TZ_SMS_SERV_ID"));
				returnJsonMap.replace("smssevname", map.get("TZ_SMS_SERV_NAME"));
				returnJsonMap.replace("usrname", map.get("TZ_SMS_USR_NAME"));
				returnJsonMap.replace("usrpwd", map.get("TZ_SMS_USR_PWD"));
				returnJsonMap.replace("sourceno", map.get("TZ_SOURCE_NO"));
				returnJsonMap.replace("desc", map.get("TZ_DESCR254"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return jacksonUtil.Map2json(returnJsonMap);
	}
}
