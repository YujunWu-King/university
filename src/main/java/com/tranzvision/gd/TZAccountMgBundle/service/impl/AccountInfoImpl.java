package com.tranzvision.gd.TZAccountMgBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsTzAqYhxxTblMapper;
import com.tranzvision.gd.TZAccountMgBundle.dao.PsoprdefnMapper;
import com.tranzvision.gd.TZAccountMgBundle.dao.PsroleuserMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTbl;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTblKey;
import com.tranzvision.gd.TZAccountMgBundle.model.Psoprdefn;
import com.tranzvision.gd.TZAccountMgBundle.model.Psroleuser;
import com.tranzvision.gd.TZAccountMgBundle.model.PsroleuserKey;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationMgBundle.dao.PsTzJgMgrTMapper;
import com.tranzvision.gd.TZOrganizationMgBundle.model.PsTzJgMgrTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;


/**
 * 安全管理-用户账号管理列表类 原PS类：TZ_GD_YHZHGL_PKG:TZ_GD_YHZHXX_CLS
 * 
 * @author tang
 * @version 1.0, 2015/10/19
 */
@Service("com.tranzvision.gd.TZAccountMgBundle.service.impl.AccountInfoImpl")
public class AccountInfoImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsroleuserMapper psroleuserMapper;
	@Autowired
	private PsoprdefnMapper psoprdefnMapper;
	@Autowired
	private PsTzAqYhxxTblMapper psTzAqYhxxTblMapper;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzJgMgrTMapper psTzJgMgrTMapper;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	
	
	/* 新增用户账号信息 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		try {
			String oprID = "";
			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");

				// 用户账号信息;
				if ("USER".equals(strFlag)) {
					// 信息内容;
					Map<String, Object> infoData = jacksonUtil.getMap("data");
					
					// 登录账号;
					String strActNum = (String) infoData.get("usAccNum");
					// 机构编号;
					String orgID = (String) infoData.get("orgId");
					// 查看是否已经存在;
					int isExistNum = 0;
					String isExistSQL = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=?";
					isExistNum = jdbcTemplate.queryForObject(isExistSQL, new Object[] { strActNum, orgID },"Integer");
					
					if (isExistNum > 0) {
						errMsg[0] = "1";
						errMsg[1] = "机构编号为：" + orgID + "，登录账号为：" + strActNum + "的信息已经存在。";
						return strRet;
					}

					// 用户名称;
					String usName = (String) infoData.get("usName");
					// 手机号码;
					String mobile = (String) infoData.get("mobile");
					// 电子邮箱;
					String email = (String) infoData.get("email");
					// 手机号码;
					String bdMobile = (String) infoData.get("bdMobile");
					// 电子邮箱;
					String bdEmail = (String) infoData.get("bdEmail");
					// 邮箱绑定标志;
					String eBindFlag = (String) infoData.get("eBindFlag");
					// 手机绑定标志;
					String mBindFlag = (String) infoData.get("mBindFlag");
					// 激活状态;
					String jhState = (String) infoData.get("jhState");
					// 激活方式;
					String jhMethod = (String) infoData.get("jhMethod");
					// 账号类型;
					String rylx = (String) infoData.get("rylx");
					// 账号密码;
					String password = (String) infoData.get("password");
					password = DESUtil.encrypt(password, "TZGD_Tranzvision");
					// 锁定账号;
					String acctLock = "";

					if (infoData.containsKey("acctLock")) {
						acctLock = (String) infoData.get("acctLock");
					}
					// 原机构ID，判断是否修改了用户的所属机构;
					String originOrgId = (String) infoData.get("originOrgId");

					// 检查绑定手机是否被占用;
					int isBd = 0;
					if ("Y".equals(mBindFlag)) {
						String isBdPhoneExist = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_MOBILE=? AND TZ_SJBD_BZ='Y' AND TZ_JG_ID=? and TZ_DLZH_ID <> ?";
						isBd = jdbcTemplate.queryForObject(isBdPhoneExist,
									new Object[] { bdMobile, orgID, strActNum }, "Integer" );
						
						if (isBd > 0) {
							errMsg[0] = "1";
							errMsg[1] = "当前机构下，该手机已经被占用";
							return strRet;
						}
					}

					// 检查绑定邮箱是否被占用;

					if ("Y".equals(eBindFlag)) {
						String isBdEmlExist = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_EMAIL=? AND TZ_YXBD_BZ='Y' AND TZ_JG_ID=? and TZ_DLZH_ID <> ?";
						isBd = jdbcTemplate.queryForObject(isBdEmlExist, new Object[] { bdEmail, orgID, strActNum },
									"Integer");

						if (isBd > 0) {
							errMsg[0] = "1";
							errMsg[1] = "当前机构下，该邮箱已经被占用";
							return strRet;
						}
					}

					if (originOrgId != null && !"".equals(originOrgId) && !originOrgId.equals(orgID)) {
						String originOprId = "";
						// 删除原账号的权限;
						String originOpridSql = "SELECT OPRID from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID=? and TZ_DLZH_ID=?";
						originOprId = jdbcTemplate.queryForObject(originOpridSql,
									new Object[] { originOrgId, strActNum }, "String");
						
						if (originOprId != null && !"".equals(originOprId)) {
							String deleteRoleSql = "DELETE from PSROLEUSER WHERE ROLEUSER=?";
							jdbcTemplate.update(deleteRoleSql, new Object[]{originOprId});
							
						}

					}

					
					oprID = "TZ_" + getSeqNum.getSeqNum("PSOPRDEFN", "OPRID");
					PsTzAqYhxxTbl psTzAqYhxxTbl = new PsTzAqYhxxTbl();
					psTzAqYhxxTbl.setTzDlzhId(strActNum);
					psTzAqYhxxTbl.setTzJgId(orgID);
					psTzAqYhxxTbl.setOprid(oprID);
					psTzAqYhxxTbl.setTzRealname(usName);
					psTzAqYhxxTbl.setTzEmail(bdEmail);
					psTzAqYhxxTbl.setTzMobile(bdMobile);
					psTzAqYhxxTbl.setTzRylx(rylx);
					psTzAqYhxxTbl.setTzYxbdBz(eBindFlag);
					psTzAqYhxxTbl.setTzSjbdBz(mBindFlag);
					psTzAqYhxxTbl.setTzJihuoZt(jhState);
					psTzAqYhxxTbl.setTzJihuoFs(jhMethod);
					psTzAqYhxxTbl.setTzZhceDt(new Date());
					psTzAqYhxxTbl.setTzBjsEml("");
					psTzAqYhxxTbl.setTzBjsSms("");
					
					String updateOperid = tzLoginServiceImpl.getLoginedManagerOprid(request);
					psTzAqYhxxTbl.setRowAddedDttm(new Date());
					psTzAqYhxxTbl.setRowAddedOprid(updateOperid);
					psTzAqYhxxTbl.setRowLastmantDttm(new Date());
					psTzAqYhxxTbl.setRowLastmantOprid(updateOperid);
					psTzAqYhxxTblMapper.insert(psTzAqYhxxTbl);
					
					
					short acctLockNum;
					if ("on".equals(acctLock) || "1".equals(acctLock)) {
						acctLockNum = 1;
					} else {
						acctLockNum = 0;
					}
					Psoprdefn psoprdefn = new Psoprdefn();
					psoprdefn.setOprid(oprID);
					psoprdefn.setOperpswd(password);
					psoprdefn.setAcctlock(acctLockNum);
					psoprdefn.setLastupddttm(new Date());
					psoprdefn.setLastupdoprid(updateOperid);
					psoprdefnMapper.insert(psoprdefn);

					// 联系方式;
					if ((mobile != null && !"".equals(mobile)) || (email != null && !"".equals(email))) {
						String lsfsSQL = "INSERT INTO PS_TZ_LXFSINFO_TBL(TZ_LXFS_LY,TZ_LYDX_ID,TZ_ZY_SJ,TZ_ZY_EMAIL) VALUES(?,?,?,?)";
						jdbcTemplate.update(lsfsSQL, new Object[]{rylx, oprID, mobile, email});
						
					}

					// 如果是从机构信息里新建的用户，需要添加到机构管理员表中 start;
					// 机构编号 机构管理员使用;

					if (infoData.containsKey("orgNo")) {
						String orgNo = (String) infoData.get("orgNo");
						if (orgNo != null && !"".equals(orgNo)) {
							//String insertJgGLYSQL = "INSERT INTO PS_TZ_JG_MGR_T(TZ_JG_ID,TZ_DLZH_ID) VALUES (?,?)";
							//jdbcTemplate.update(insertJgGLYSQL, new Object[]{orgNo, strActNum});
							PsTzJgMgrTKey psTzJgMgrTkey = new PsTzJgMgrTKey(); 
							psTzJgMgrTkey.setTzDlzhId(strActNum);
							psTzJgMgrTkey.setTzJgId(orgNo);
							psTzJgMgrTMapper.deleteByPrimaryKey(psTzJgMgrTkey);
						}

					}

				}

				if ("ROLE".equals(strFlag) && oprID != null && !"".equals(oprID)) {
					
					List<Map<String, Object>> jsonArray = (List<Map<String, Object>>) jacksonUtil.getList("data");
					int j = 0;
					for (j = 0; j < jsonArray.size(); j++) {
						Map<String, Object> roleCLASSJson = jsonArray.get(j);
						String roleID = (String) roleCLASSJson.get("roleID");
						boolean isRole = Boolean.parseBoolean(String.valueOf(roleCLASSJson.get("isRole")));
						if (isRole) {
							Psroleuser psroleuser = new Psroleuser();
							psroleuser.setRoleuser(oprID);
							psroleuser.setRolename(roleID);
							psroleuser.setDynamicSw("N");
							psroleuserMapper.insert(psroleuser);
							
						} else {
							PsroleuserKey psroleuserKey = new PsroleuserKey();
							psroleuserKey.setRoleuser(oprID);
							psroleuserKey.setRolename(roleID);
							psroleuserMapper.deleteByPrimaryKey(psroleuserKey);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/* 修改组件注册信息 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		try {
			
			String oprID = "";
			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");

				// 用户账号信息;
				if ("USER".equals(strFlag)) {
					
					// 信息内容;
					Map<String, Object> infoData = jacksonUtil.getMap("data");
					
					// 登录账号;
					String strActNum = (String) infoData.get("usAccNum");
					// 机构编号;
					String orgID = (String) infoData.get("orgId");

					// 用户名称;
					String usName = (String) infoData.get("usName");
					// 手机号码;
					String mobile = (String) infoData.get("mobile");
					// 电子邮箱;
					String email = (String) infoData.get("email");
					// 手机号码;
					String bdMobile = (String) infoData.get("bdMobile");
					// 电子邮箱;
					String bdEmail = (String) infoData.get("bdEmail");
					// 邮箱绑定标志;
					String eBindFlag = (String) infoData.get("eBindFlag");
					// 手机绑定标志;
					String mBindFlag = (String) infoData.get("mBindFlag");
					// 激活状态;
					String jhState = (String) infoData.get("jhState");
					// 激活方式;
					String jhMethod = (String) infoData.get("jhMethod");
					// 账号类型;
					String rylx = (String) infoData.get("rylx");
					// 账号密码;
					String password = (String) infoData.get("password");
					password = DESUtil.encrypt(password, "TZGD_Tranzvision");
					// 锁定账号;
					String acctLock = "";

					if (infoData.containsKey("acctLock")) {
						acctLock = (String) infoData.get("acctLock");
					}
					

					// 检查绑定手机是否被占用;
					int isBd = 0;
					if ("Y".equals(mBindFlag)) {
						String isBdPhoneExist = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_MOBILE=? AND TZ_SJBD_BZ='Y' AND TZ_JG_ID=? and TZ_DLZH_ID <> ?";
						isBd = jdbcTemplate.queryForObject(isBdPhoneExist,
									new Object[] { bdMobile, orgID, strActNum }, "Integer" );
						
						if (isBd > 0) {
							errMsg[0] = "1";
							errMsg[1] = "当前机构下，该手机已经被占用";
							return strRet;
						}
					}

					// 检查绑定邮箱是否被占用;

					if ("Y".equals(eBindFlag)) {
						String isBdEmlExist = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_EMAIL=? AND TZ_YXBD_BZ='Y' AND TZ_JG_ID=? and TZ_DLZH_ID <> ?";
						isBd = jdbcTemplate.queryForObject(isBdEmlExist, new Object[] { bdEmail, orgID, strActNum },
									"Integer");

						if (isBd > 0) {
							errMsg[0] = "1";
							errMsg[1] = "当前机构下，该邮箱已经被占用";
							return strRet;
						}
					}
					
					// 原机构ID，判断是否修改了用户的所属机构;
					String originOrgId = (String) infoData.get("originOrgId");
					if (originOrgId != null && !"".equals(originOrgId.trim()) && !originOrgId.equals(orgID)) {
						
						// 如果改变了机构，判断该用户是否已经存在;
						String isExistSQL = "SELECT 'Y' FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=?";
						String orgIsExist = jdbcTemplate.queryForObject(isExistSQL, new Object[] { strActNum, orgID },
									"String");
						
						if ("Y".equals(orgIsExist)) {
							errMsg[0] = "1";
							errMsg[1] = "机构编号为：" + orgID + "，登录账号为：" + strActNum + "的信息已经存在。";
							return strRet;
						}else{
							
							// 删除原账号的权限;
							String originOpridSql = "SELECT OPRID from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID=? and TZ_DLZH_ID=?";
							oprID = jdbcTemplate.queryForObject(originOpridSql,
										new Object[] { originOrgId, strActNum }, "String");
							
							if (oprID != null && !"".equals(oprID)) {
								
								String deleteRoleSql = "DELETE from PSROLEUSER WHERE ROLEUSER=?";
								jdbcTemplate.update(deleteRoleSql, new Object[]{oprID});
								
							}
						}
					}else{
						String opridSql = "SELECT OPRID from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID=? and TZ_DLZH_ID=?";
						oprID = jdbcTemplate.queryForObject(opridSql,
									new Object[] { orgID, strActNum }, "String");
					}

					
					String updateOprdSql = "update PS_TZ_AQ_YHXX_TBL set TZ_DLZH_ID=?,TZ_JG_ID=?, TZ_REALNAME = ?, TZ_EMAIL  = ?,TZ_MOBILE = ?,TZ_RYLX = ?,TZ_YXBD_BZ = ?,TZ_SJBD_BZ = ?,TZ_JIHUO_ZT = ?,TZ_JIHUO_FS = ?,ROW_LASTMANT_DTTM = curdate(),ROW_LASTMANT_OPRID = ? where OPRID=?";
					String updateOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
					jdbcTemplate.update(updateOprdSql, new Object[]{strActNum,orgID,usName, bdEmail, bdMobile, rylx, eBindFlag, mBindFlag,
								jhState, jhMethod, updateOprid, oprID});

					short acctLockNum;
					if ("1".equals(acctLock)|| "on".equals(acctLock)) {
						acctLockNum = 1;
					} else {
						acctLockNum = 0;
					}
					Psoprdefn psoprdefn = new Psoprdefn();
					psoprdefn.setOprid(oprID);
					psoprdefn.setOperpswd(password);
					psoprdefn.setAcctlock(acctLockNum);
					psoprdefn.setLastupddttm(new Date());
					psoprdefn.setLastupdoprid(updateOprid);
					psoprdefnMapper.updateByPrimaryKeySelective(psoprdefn);


					// 联系方式;
					int isExistNum = 0;
					String isExistLXFS = "SELECT COUNT(1) FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY=? and TZ_LYDX_ID=?";
					isExistNum = jdbcTemplate.queryForObject(isExistLXFS, new Object[] { rylx, oprID },
								"Integer");
					
					if (isExistNum <= 0) {
						if ((mobile != null && !"".equals(mobile)) || (email != null && !"".equals(email))) {
							String lsfsSQL = "INSERT INTO PS_TZ_LXFSINFO_TBL(TZ_LXFS_LY,TZ_LYDX_ID,TZ_ZY_SJ,TZ_ZY_EMAIL) VALUES(?,?,?,?)";
							jdbcTemplate.update(lsfsSQL, new Object[]{rylx, oprID, mobile, email});
							
						}
					} else {
						String updatelSFSSql = "UPDATE PS_TZ_LXFSINFO_TBL SET TZ_ZY_SJ=?,TZ_ZY_EMAIL=? WHERE TZ_LXFS_LY=? AND TZ_LYDX_ID=?";
						jdbcTemplate.update(updatelSFSSql, new Object[]{mobile, email, rylx, oprID});

					}
				}

				if ("ROLE".equals(strFlag) && oprID != null && !"".equals(oprID)) {

					List<Map<String, Object>> jsonArray = (List<Map<String, Object>>) jacksonUtil.getList("data");
					int j = 0;
					for (j = 0; j < jsonArray.size(); j++) {
						Map<String, Object> roleCLASSJson = jsonArray.get(j);
						String roleID = (String) roleCLASSJson.get("roleID");
						boolean isRole = Boolean.parseBoolean(String.valueOf(roleCLASSJson.get("isRole")));
						if (isRole) {
							String isRoleExist = "";
							String isRoleExistSQL = "SELECT 'Y' FROM PSROLEUSER WHERE ROLEUSER=? AND ROLENAME=?";
							isRoleExist = jdbcTemplate.queryForObject(isRoleExistSQL,
										new Object[] { oprID, roleID }, "String");
							
							if (!"Y".equals(isRoleExist)) {
								Psroleuser psroleuser = new Psroleuser();
								psroleuser.setRoleuser(oprID);
								psroleuser.setRolename(roleID);
								psroleuser.setDynamicSw("N");
								psroleuserMapper.insert(psroleuser);
							}
							
						} else {
							PsroleuserKey psroleuserKey = new PsroleuserKey();
							psroleuserKey.setRoleuser(oprID);
							psroleuserKey.setRolename(roleID);
							psroleuserMapper.deleteByPrimaryKey(psroleuserKey);
						}
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/* 获取用户账号信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
		
			if (jacksonUtil.containsKey("usAccNum") && jacksonUtil.containsKey("orgId")) {
				String usAccNum = jacksonUtil.getString("usAccNum");
				String userOrg = jacksonUtil.getString("orgId");

				// 用户ID,姓名，电子邮件，手机号码，人员类型，邮箱绑定标志，手机绑定标志，激活状态，激活方式;
				String oprID = "", name = "", bdEmail = "", bdMobile = "", perType = "", eBindFlg = "", mBindFlg = "",
						jhState = "", jhMethod = "";
				String email = "", mobile = "";
				String originOrgId = "";
				// 密码;
				String password = "";
				String rylx = "";
				String acctLock = "0";

				if (usAccNum != null && !"".equals(usAccNum)) {
					PsTzAqYhxxTblKey psTzAqYhxxTblKey = new PsTzAqYhxxTblKey();
					psTzAqYhxxTblKey.setTzDlzhId(usAccNum);
					psTzAqYhxxTblKey.setTzJgId(userOrg);
					PsTzAqYhxxTbl psTzAqYhxxTbl = psTzAqYhxxTblMapper.selectByPrimaryKey(psTzAqYhxxTblKey);
					
					if (psTzAqYhxxTbl != null) {
						oprID = psTzAqYhxxTbl.getOprid();
						name = psTzAqYhxxTbl.getTzRealname();
						bdEmail = psTzAqYhxxTbl.getTzEmail();
						bdMobile = psTzAqYhxxTbl.getTzMobile();
						perType = psTzAqYhxxTbl.getTzRylx();
						eBindFlg = psTzAqYhxxTbl.getTzYxbdBz();
						mBindFlg = psTzAqYhxxTbl.getTzSjbdBz();
						jhState = psTzAqYhxxTbl.getTzJihuoZt();
						jhMethod = psTzAqYhxxTbl.getTzJihuoFs();
						rylx = psTzAqYhxxTbl.getTzRylx();
					}

					
					String sql2 = "SELECT TZ_ZY_SJ,TZ_ZY_EMAIL FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY=? AND TZ_LYDX_ID=?";
					Map<String, Object> map = jdbcTemplate.queryForMap(sql2, new Object[] { rylx, oprID });
					

					if (map != null) {
						mobile = (String) map.get("TZ_ZY_SJ");
						email = (String) map.get("TZ_ZY_EMAIL");
					}
					
					Psoprdefn psoprdefn = new Psoprdefn();
					psoprdefn = psoprdefnMapper.selectByPrimaryKey(oprID);
					short localNum = 0;
				
					if (psoprdefn != null) {
						password = psoprdefn.getOperpswd();
						if (!"".equals(password)) {
							password = DESUtil.decrypt(password, "TZGD_Tranzvision");
						}
						localNum = (short) psoprdefn.getAcctlock();
					}
					if (localNum == 1) {
						acctLock = "1";
					}else{
						acctLock = "0";
					}
					originOrgId = userOrg;

				} else {
					jhState = "Y";
					jhMethod = "R";
					rylx = "NBYH";
					originOrgId = "";
				}
				
				returnJsonMap.put("usAccNum", usAccNum);
				returnJsonMap.put("orgId", userOrg);
				returnJsonMap.put("oprid", oprID);
				
				returnJsonMap.put("usName", name);
				returnJsonMap.put("email", email);
				returnJsonMap.put("mobile", mobile);
				returnJsonMap.put("eBindFlag", eBindFlg);
				returnJsonMap.put("mBindFlag", mBindFlg);
				
				returnJsonMap.put("jhState", jhState);
				returnJsonMap.put("jhMethod", jhMethod);
				returnJsonMap.put("password", password);
				returnJsonMap.put("reptPassword", password);
				returnJsonMap.put("perType", perType);
				
				returnJsonMap.put("originOrgId", originOrgId);
				returnJsonMap.put("rylx", rylx);
				returnJsonMap.put("acctLock", acctLock);
				returnJsonMap.put("bdEmail", bdEmail);
				returnJsonMap.put("bdMobile", bdMobile);

			} else {
				errMsg[0] = "1";
				errMsg[1] = "无法获取用户信息";
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}

	/* 获取角色信息列表 */
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		// 页面注册信息总数;
		int numTotal = 0;

		String oprID = "";

		try {
			jacksonUtil.json2Map(strParams);
			// 登录账号;
			String usAccNum = jacksonUtil.getString("usAccNum");
			// 机构编号;
			String orgID = jacksonUtil.getString("orgId");

			// 从机构新建用户时 需根据角色类型选中对应的角色（普通用户选择角色类型为普通用户的角色，管理员用户选择角色类型为管理员的角色）;
			String roleType = "";
			if (jacksonUtil.containsKey("roleType")) {
				roleType = jacksonUtil.getString("roleType");
			}

			// 获取用户ID;
			String opridSql = "SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=?";
			oprID = jdbcTemplate.queryForObject(opridSql, new Object[] { usAccNum, orgID }, "String");

			// 获取角色信息列表sql;
			String sqlRoleList = "";
			// 角色编号，角色描述，是否该用户角色;
			String roleID = "", roleDesc = "", isRole = "";

			//获取该户下的角色信息及所有角色信息;
			List<Map<String, Object>> list = null;
			if (numLimit == 0) {
				if (!"".equals(oprID) && oprID != null) {
					sqlRoleList = "SELECT B.ROLENAME,B.DESCR,'Y' ISROLE ISROLE FROM PSROLEUSER A,PSROLEDEFN B WHERE A.ROLENAME=B.ROLENAME AND A.ROLEUSER = ? AND A.DYNAMIC_SW='N' UNION SELECT C.ROLENAME,A.DESCR,'N' ISROLE FROM PSROLEDEFN A, PS_TZ_JG_ROLE_T C WHERE C.TZ_JG_ID = ? AND A.ROLENAME = C.ROLENAME AND NOT EXISTS (SELECT 'Y' FROM PSROLEUSER B WHERE A.ROLENAME=B.ROLENAME AND B.ROLEUSER = ? AND B.DYNAMIC_SW='N') ORDER BY ISROLE DESC";
					list = jdbcTemplate.queryForList(sqlRoleList, new Object[] { oprID, orgID, oprID });
					
				} else {
					if (!"".equals(roleType) && roleType != null) {
						sqlRoleList = "SELECT C.ROLENAME,A.DESCR,if(C.TZ_ROLE_TYPE=?,'Y','N') ISROLE FROM PSROLEDEFN A, PS_TZ_JG_ROLE_T C  WHERE C.TZ_JG_ID = ? AND A.ROLENAME = C.ROLENAME";
						list = jdbcTemplate.queryForList(sqlRoleList, new Object[] { roleType, orgID });
						
					} else {
						sqlRoleList = "SELECT C.ROLENAME,A.DESCR,'N' ISROLE FROM PSROLEDEFN A, PS_TZ_JG_ROLE_T C  WHERE C.TZ_JG_ID = ? AND A.ROLENAME = C.ROLENAME";
						list = jdbcTemplate.queryForList(sqlRoleList, new Object[] { orgID });
					}
				}

			} else {
				if (!"".equals(oprID) && oprID != null) {
					sqlRoleList = "SELECT B.ROLENAME,B.DESCR,'Y' ISROLE FROM PSROLEUSER A,PSROLEDEFN B WHERE A.ROLENAME=B.ROLENAME AND A.ROLEUSER = ? AND A.DYNAMIC_SW='N' UNION SELECT C.ROLENAME,A.DESCR,'N' ISROLE FROM PSROLEDEFN A, PS_TZ_JG_ROLE_T C WHERE C.TZ_JG_ID = ? AND A.ROLENAME = C.ROLENAME AND NOT EXISTS (SELECT 'Y' FROM PSROLEUSER B WHERE A.ROLENAME=B.ROLENAME AND B.ROLEUSER = ? AND B.DYNAMIC_SW='N') ORDER BY ISROLE DESC limit ?,?";
					list = jdbcTemplate.queryForList(sqlRoleList,
								new Object[] { oprID, orgID, oprID, numStart, numLimit });
					
				} else {
					if (!"".equals(roleType) && roleType != null) {
						sqlRoleList = "SELECT C.ROLENAME,A.DESCR,if(C.TZ_ROLE_TYPE=?,'Y','N') ISROLE FROM PSROLEDEFN A, PS_TZ_JG_ROLE_T C  WHERE C.TZ_JG_ID = ? AND A.ROLENAME = C.ROLENAME limit ?,?";
						list = jdbcTemplate.queryForList(sqlRoleList,
									new Object[] { roleType, orgID, numStart, numLimit });
						
					} else {
						sqlRoleList = "SELECT C.ROLENAME,A.DESCR,'N' ISROLE FROM PSROLEDEFN A, PS_TZ_JG_ROLE_T C  WHERE C.TZ_JG_ID = ? AND A.ROLENAME = C.ROLENAME limit ?,?";
						list = jdbcTemplate.queryForList(sqlRoleList, new Object[] { orgID, numStart, numLimit });
					}
				}
			}

			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					roleID = (String) list.get(i).get("ROLENAME");
					roleDesc = (String) list.get(i).get("DESCR");
					isRole = (String) list.get(i).get("ISROLE");
					
					
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("roleID", roleID);
					mapList.put("roleName", roleDesc);
					if ("Y".equals(isRole)) {
						mapList.put("isRole", true);
					} else {
						mapList.put("isRole", false);
					}
					
					
					listData.add(mapList);
				}
			}

			String totalSQL = "";
			if (oprID != null && !"".equals(oprID)) {
				totalSQL = "SELECT COUNT(1) FROM (SELECT B.ROLENAME  FROM PSROLEUSER A,PSROLEDEFN B WHERE A.ROLENAME=B.ROLENAME AND A.ROLEUSER = ? AND A.DYNAMIC_SW='N' UNION SELECT C.ROLENAME FROM PSROLEDEFN A, PS_TZ_JG_ROLE_T C WHERE C.TZ_JG_ID = ? AND A.ROLENAME = C.ROLENAME AND NOT EXISTS (SELECT 'Y' FROM PSROLEUSER B WHERE A.ROLENAME=B.ROLENAME AND B.ROLEUSER = ? AND B.DYNAMIC_SW='N')) AS AB";
					numTotal = jdbcTemplate.queryForObject(totalSQL, new Object[] { oprID, orgID, oprID },"Integer");

			} else {
				totalSQL = "SELECT COUNT(1) FROM PSROLEDEFN A, PS_TZ_JG_ROLE_T C  WHERE C.TZ_JG_ID = ? AND A.ROLENAME = C.ROLENAME";
				numTotal = jdbcTemplate.queryForObject(totalSQL, new Object[] { orgID},"Integer");
				
			}
			mapRet.replace("total",numTotal);
			mapRet.replace("root", listData);

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return jacksonUtil.Map2json(mapRet);
	}

}
