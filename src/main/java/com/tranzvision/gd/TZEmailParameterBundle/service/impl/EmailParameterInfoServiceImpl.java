package com.tranzvision.gd.TZEmailParameterBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailParameterBundle.dao.PsTzEmlsDefTblMapper;
import com.tranzvision.gd.TZEmailParameterBundle.model.PsTzEmlsDefTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 邮件服务器参数设置；原：TZ_GD_EMLSMSSET_PKG:TZ_GD_EMLINFO_CLS
 * 
 * @author tang
 * @since 2015-11-19
 */
@Service("com.tranzvision.gd.TZEmailParameterBundle.service.impl.EmailParameterInfoServiceImpl")
public class EmailParameterInfoServiceImpl extends FrameworkImpl {
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzEmlsDefTblMapper psTzEmlsDefTblMapper;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;

	/* 新增邮件服务器参数设置信息 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("emailservid", "");
		returnJsonMap.put("emailaddr", "");
		returnJsonMap.put("isdefault", "");
		returnJsonMap.put("emailorg", "");
		returnJsonMap.put("chnsname", "");
		returnJsonMap.put("smtpaddr", "");
		returnJsonMap.put("emlalias", "");
		returnJsonMap.put("usrname", "");
		returnJsonMap.put("usrpwd", "");
		returnJsonMap.put("desc", "");
		
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				// 邮箱地址;
				String emailaddr = jacksonUtil.getString("emailaddr");
				// 机构默认邮箱;
				String isdefault = jacksonUtil.getString("isdefault");
				// 机构;
				String emailorg = jacksonUtil.getString("emailorg");
				String strEmailorgName = "";
				// 邮箱中文名称;
				String chnsname = jacksonUtil.getString("chnsname");
				// STMP邮箱;
				String smtpaddr = jacksonUtil.getString("smtpaddr");
				// 邮箱别名;
				String emlalias = jacksonUtil.getString("emlalias");
				// 账户;
				String usrname = jacksonUtil.getString("usrname");
				// 密码;
				String usrpwd = jacksonUtil.getString("usrpwd");
				// 描述;
				String desc = jacksonUtil.getString("desc");

				// 检查邮箱定义是否重复;
				int rptNum = 0;
				String rptNumSQL = "SELECT COUNT(1) FROM PS_TZ_EMLS_DEF_TBL WHERE TZ_JG_ID = ? AND TZ_EML_ADDR100 = ?";
				rptNum = jdbcTemplate.queryForObject(rptNumSQL, new Object[] { emailorg, emailaddr }, "Integer");
				if (rptNum > 0) {
					String jgNameSQL = "SELECT TZ_JG_NAME FROM PS_TZ_JG_BASE_T WHERE TZ_JG_ID = ?";
					strEmailorgName = jdbcTemplate.queryForObject(jgNameSQL, new Object[] { emailorg }, "String");
					errMsg[0] = "1";
					errMsg[1] = "机构" + strEmailorgName + "已经定义了一个电子邮箱为" + strEmailorgName + "的邮箱服务器，请务重复定义。";
				} else {
					int defaultNum = 0;
					if ("Y".equals(isdefault)) {
						String defaultNumSQL = "SELECT COUNT(1) FROM PS_TZ_EMLS_DEF_TBL WHERE TZ_JG_ID = ? AND TZ_IS_DEFAULT = 'Y'";
						defaultNum = jdbcTemplate.queryForObject(defaultNumSQL, new Object[] { emailorg }, "Integer");
					}

					if (defaultNum > 0) {
						errMsg[0] = "1";
						errMsg[1] = "每个机构仅允许一个默认邮箱。";
					} else {
						String emailservid = String.valueOf(getSeqNum.getSeqNum("TZ_EMLS_DEF_TBL", "TZ_EMLSERV_ID"));
						PsTzEmlsDefTbl psTzEmlsDefTbl = new PsTzEmlsDefTbl();
						psTzEmlsDefTbl.setTzEmlservId(emailservid);
						psTzEmlsDefTbl.setTzJgId(emailorg);
						psTzEmlsDefTbl.setTzEmlAddr100(emailaddr);
						psTzEmlsDefTbl.setTzIsDefault(isdefault);
						psTzEmlsDefTbl.setTzChsSname(chnsname);
						psTzEmlsDefTbl.setTzSmtpAddr(smtpaddr);
						psTzEmlsDefTbl.setTzEmlAlias(emlalias);
						psTzEmlsDefTbl.setTzUsrName(usrname);
						psTzEmlsDefTbl.setTzUsrPwd(usrpwd);
						psTzEmlsDefTbl.setTzDescr254(desc);

						psTzEmlsDefTbl.setRowAddedDttm(new Date());
						psTzEmlsDefTbl.setRowAddedOprid(oprid);
						psTzEmlsDefTbl.setRowLastmantDttm(new Date());
						psTzEmlsDefTbl.setRowLastmantOprid(oprid);
						int i = psTzEmlsDefTblMapper.insert(psTzEmlsDefTbl);
						if (i > 0) {
							returnJsonMap.replace("emailservid", emailservid);
							returnJsonMap.replace("emailaddr", emailaddr);
							returnJsonMap.replace("isdefault", isdefault);
							returnJsonMap.replace("emailorg", emailorg);
							returnJsonMap.replace("chnsname", chnsname);
							returnJsonMap.replace("smtpaddr", smtpaddr);
							returnJsonMap.replace("emlalias", emlalias);
							returnJsonMap.replace("usrname", usrname);
							returnJsonMap.replace("usrpwd", usrpwd);
							returnJsonMap.replace("desc", desc);
						} else {
							errMsg[0] = "1";
							errMsg[1] = "站点信息保存失败";
						}
					}
				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	/* 新增邮件服务器参数设置信息 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("emailservid", "");
		returnJsonMap.put("emailaddr", "");
		returnJsonMap.put("isdefault", "");
		returnJsonMap.put("emailorg", "");
		returnJsonMap.put("chnsname", "");
		returnJsonMap.put("smtpaddr", "");
		returnJsonMap.put("emlalias", "");
		returnJsonMap.put("usrname", "");
		returnJsonMap.put("usrpwd", "");
		returnJsonMap.put("desc", "");

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				String emailservid = jacksonUtil.getString("emailservid");

				// 邮箱地址;
				String emailaddr = jacksonUtil.getString("emailaddr");
				// 机构默认邮箱;
				String isdefault = jacksonUtil.getString("isdefault");
				// 机构;
				String emailorg = jacksonUtil.getString("emailorg");
				// 邮箱中文名称;
				String chnsname = jacksonUtil.getString("chnsname");
				// STMP邮箱;
				String smtpaddr = jacksonUtil.getString("smtpaddr");
				// 邮箱别名;
				String emlalias = jacksonUtil.getString("emlalias");
				// 账户;
				String usrname = jacksonUtil.getString("usrname");
				// 密码;
				String usrpwd = jacksonUtil.getString("usrpwd");
				// 描述;
				String desc = jacksonUtil.getString("desc");

				int defaultNum = 0;
				if ("Y".equals(isdefault)) {
					String defaultNumSQL = "SELECT COUNT(1) FROM PS_TZ_EMLS_DEF_TBL WHERE TZ_JG_ID = ? AND TZ_EMLSERV_ID <> ? AND TZ_IS_DEFAULT = 'Y'";
					defaultNum = jdbcTemplate.queryForObject(defaultNumSQL, new Object[] { emailorg,emailservid }, "Integer");
				}

				if (defaultNum > 0) {
					errMsg[0] = "1";
					errMsg[1] = "每个机构仅允许一个默认邮箱。";
				} else {

					PsTzEmlsDefTbl psTzEmlsDefTbl = new PsTzEmlsDefTbl();
					psTzEmlsDefTbl.setTzEmlservId(emailservid);
					psTzEmlsDefTbl.setTzJgId(emailorg);
					psTzEmlsDefTbl.setTzEmlAddr100(emailaddr);
					psTzEmlsDefTbl.setTzIsDefault(isdefault);
					psTzEmlsDefTbl.setTzChsSname(chnsname);
					psTzEmlsDefTbl.setTzSmtpAddr(smtpaddr);
					psTzEmlsDefTbl.setTzEmlAlias(emlalias);
					psTzEmlsDefTbl.setTzUsrName(usrname);
					psTzEmlsDefTbl.setTzUsrPwd(usrpwd);
					psTzEmlsDefTbl.setTzDescr254(desc);
					psTzEmlsDefTbl.setRowLastmantDttm(new Date());
					psTzEmlsDefTbl.setRowLastmantOprid(oprid);
					int i = psTzEmlsDefTblMapper.updateByPrimaryKeySelective(psTzEmlsDefTbl);
					if (i > 0) {
						returnJsonMap.replace("emailservid", emailservid);
						returnJsonMap.replace("emailaddr", emailaddr);
						returnJsonMap.replace("isdefault", isdefault);
						returnJsonMap.replace("emailorg", emailorg);
						returnJsonMap.replace("chnsname", chnsname);
						returnJsonMap.replace("smtpaddr", smtpaddr);
						returnJsonMap.replace("emlalias", emlalias);
						returnJsonMap.replace("usrname", usrname);
						returnJsonMap.replace("usrpwd", usrpwd);
						returnJsonMap.replace("desc", desc);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "站点信息保存失败";
					}
				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	/* 查询表单信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("emailservid")) {
				// 邮件服务器参数id;
				String emailservid = jacksonUtil.getString("emailservid");
				
				PsTzEmlsDefTbl psTzEmlsDefTbl = psTzEmlsDefTblMapper.selectByPrimaryKey(emailservid);

				returnJsonMap.put("emailservid", emailservid);
				returnJsonMap.put("emailaddr", psTzEmlsDefTbl.getTzEmlAddr100());
				returnJsonMap.put("isdefault", psTzEmlsDefTbl.getTzIsDefault());
				returnJsonMap.put("emailorg", psTzEmlsDefTbl.getTzJgId());
				returnJsonMap.put("chnsname", psTzEmlsDefTbl.getTzChsSname());
				returnJsonMap.put("smtpaddr", psTzEmlsDefTbl.getTzSmtpAddr());
				returnJsonMap.put("emlalias", psTzEmlsDefTbl.getTzEmlAlias());
				returnJsonMap.put("usrname", psTzEmlsDefTbl.getTzUsrName());
				returnJsonMap.put("usrpwd", psTzEmlsDefTbl.getTzUsrPwd());
				returnJsonMap.put("desc", psTzEmlsDefTbl.getTzDescr254());

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
}
