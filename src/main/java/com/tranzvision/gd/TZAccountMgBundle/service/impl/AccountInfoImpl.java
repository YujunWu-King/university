package com.tranzvision.gd.TZAccountMgBundle.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.PaseJsonUtil;
import com.tranzvision.gd.util.base.TZUtility;
import com.tranzvision.gd.util.encrypt.DESUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 安全管理-用户账号管理列表类 原PS类：TZ_GD_YHZHGL_PKG:TZ_GD_YHZHXX_CLS
 * 
 * @author tang
 * @version 1.0, 2015/10/19
 */
@Service("com.tranzvision.gd.TZAccountMgBundle.service.impl.AccountInfoImpl")
public class AccountInfoImpl extends FrameworkImpl {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/* 新增用户账号信息 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		try {
			String oprID = "";

			int num = 0;
			for (num = 0; num < actData.length; num++) {
				String strForm = actData[num];
				JSONObject CLASSJson = PaseJsonUtil.getJson(strForm);
				// 类型标志;
				String strFlag = CLASSJson.getString("typeFlag");

				// 用户账号信息;
				if ("USER".equals(strFlag)) {
					// 信息内容;
					String infoData = CLASSJson.getString("data");
					// 将字符串转换成json;
					CLASSJson = PaseJsonUtil.getJson(infoData);
					// 登录账号;
					String strActNum = CLASSJson.getString("usAccNum");
					// 机构编号;
					String orgID = CLASSJson.getString("orgId");
					// 查看是否已经存在;
					int isExistNum = 0;
					String isExistSQL = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=?";
					try {
						isExistNum = jdbcTemplate.queryForObject(isExistSQL, new Object[] { strActNum, orgID },
								Integer.class);
					} catch (DataAccessException e) {

					}
					if (isExistNum > 0) {
						errMsg[0] = "1";
						errMsg[1] = "机构编号为：" + orgID + "，登录账号为：" + strActNum + "的信息已经存在。";
						return strRet;
					}

					// 用户名称;
					String usName = CLASSJson.getString("usName");
					// 手机号码;
					String mobile = CLASSJson.getString("mobile");
					// 电子邮箱;
					String email = CLASSJson.getString("email");
					// 手机号码;
					String bdMobile = CLASSJson.getString("bdMobile");
					// 电子邮箱;
					String bdEmail = CLASSJson.getString("bdEmail");
					// 邮箱绑定标志;
					String eBindFlag = CLASSJson.getString("eBindFlag");
					// 手机绑定标志;
					String mBindFlag = CLASSJson.getString("mBindFlag");
					// 激活状态;
					String jhState = CLASSJson.getString("jhState");
					// 激活方式;
					String jhMethod = CLASSJson.getString("jhMethod");
					// 账号类型;
					String rylx = CLASSJson.getString("rylx");
					// 账号密码;
					String password = CLASSJson.getString("password");
					password = DESUtil.encrypt(password, "TZGD_Tranzvision");
					// 锁定账号;
					String acctLock = "";

					if (CLASSJson.containsKey("acctLock")) {
						acctLock = CLASSJson.getString("acctLock");
					}
					// 原机构ID，判断是否修改了用户的所属机构;
					String originOrgId = CLASSJson.getString("originOrgId");

					// 检查绑定手机是否被占用;
					String isBd = "";
					if ("Y".equals(mBindFlag)) {
						String isBdPhoneExist = "SELECT 'Y' FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_MOBILE=? AND TZ_SJBD_BZ='Y' AND TZ_JG_ID=? and TZ_DLZH_ID <> ?";
						try {
							isBd = jdbcTemplate.queryForObject(isBdPhoneExist,
									new Object[] { bdMobile, orgID, strActNum }, String.class);
						} catch (DataAccessException e) {

						}

						if ("Y".equals(isBd)) {
							errMsg[0] = "1";
							errMsg[1] = "当前机构下，该手机已经被占用";
							return strRet;
						}
					}

					// 检查绑定邮箱是否被占用;

					if ("Y".equals(eBindFlag)) {
						String isBdEmlExist = "SELECT 'Y' FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_EMAIL=? AND TZ_YXBD_BZ='Y' AND TZ_JG_ID=? and TZ_DLZH_ID <> ?";
						try {
							isBd = jdbcTemplate.queryForObject(isBdEmlExist, new Object[] { bdEmail, orgID, strActNum },
									String.class);
						} catch (DataAccessException e) {

						}

						if ("Y".equals(isBd)) {
							errMsg[0] = "1";
							errMsg[1] = "当前机构下，该邮箱已经被占用";
							return strRet;
						}
					}

					if (originOrgId != null && !"".equals(originOrgId) && !originOrgId.equals(orgID)) {
						String originOprId = "";
						// 删除原账号的权限;
						String originOpridSql = "SELECT OPRID from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID=? and TZ_DLZH_ID=?";
						try {
							originOprId = jdbcTemplate.queryForObject(originOpridSql,
									new Object[] { originOrgId, strActNum }, String.class);
						} catch (DataAccessException e) {

						}
						if (originOprId != null && !"".equals(originOprId)) {
							String deleteRoleSql = "DELETE from PSROLEUSER WHERE ROLEUSER=?";
							try {
								jdbcTemplate.update(deleteRoleSql, originOprId);
							} catch (DataAccessException e) {

							}
						}

					}

					TZUtility utility = new TZUtility();
					oprID = "TZ_" + utility.GetSeqNum("PSOPRDEFN", "OPRID");
					String insertOprdSql = "insert into PS_TZ_AQ_YHXX_TBL ( TZ_DLZH_ID, TZ_JG_ID, OPRID, TZ_REALNAME, TZ_EMAIL,TZ_MOBILE,TZ_RYLX,TZ_YXBD_BZ,TZ_SJBD_BZ,TZ_JIHUO_ZT,TZ_JIHUO_FS,TZ_ZHCE_DT,TZ_BJS_EML,TZ_BJS_SMS,ROW_ADDED_DTTM,ROW_ADDED_OPRID,ROW_LASTMANT_DTTM,ROW_LASTMANT_OPRID,SYNCID,SYNCDTTM ) values(?,?,?,?,?,?,?,?,?,?,?,curdate(),'','',curdate(),?,curdate(),?,0,curdate())";
					try {
						/**** TODO %OPRID *****/
						jdbcTemplate.update(insertOprdSql, strActNum, orgID, oprID, usName, bdEmail, bdMobile, rylx,
								eBindFlag, mBindFlag, jhState, jhMethod, "TZ_7", "TZ_7");
					} catch (DataAccessException e) {

					}

					String insertOprSQL = "INSERT INTO PSOPRDEFN(OPRID,OPERPSWD,ACCTLOCK,LASTUPDDTTM,LASTUPDOPRID) values(?,?,?,curdate(),?)";
					try {
						int acctLockNum;
						if ("on".equals(acctLock)) {
							acctLockNum = 1;
						} else {
							acctLockNum = 0;
						}
						/**** TODO %OPRID *****/
						jdbcTemplate.update(insertOprSQL, oprID, password, acctLockNum, "TZ_7");
					} catch (DataAccessException e) {

					}

					// 联系方式;
					if ((mobile != null && !"".equals(mobile)) || (email != null && !"".equals(email))) {
						String lsfsSQL = "INSERT INTO PS_TZ_LXFSINFO_TBL(TZ_LXFS_LY,TZ_LYDX_ID,TZ_ZY_SJ,TZ_ZY_EMAIL) VALUES(?,?,?,?)";
						try {
							jdbcTemplate.update(lsfsSQL, rylx, oprID, mobile, email);
						} catch (DataAccessException e) {

						}
					}

					// 如果是从机构信息里新建的用户，需要添加到机构管理员表中 start;
					// 机构编号 机构管理员使用;

					if (CLASSJson.containsKey("orgNo")) {
						String orgNo = CLASSJson.getString("orgNo");
						if (orgNo != null && !"".equals(orgNo)) {
							String insertJgGLYSQL = "INSERT INTO PS_TZ_JS_MGR_T(TZ_JG_ID,TZ_DLZH_ID) VALUES (?,?)";
							try {
								jdbcTemplate.update(insertJgGLYSQL, orgNo, strActNum);
							} catch (DataAccessException e) {

							}
						}

					}

				}

				if ("ROLE".equals(strFlag) && oprID != null && !"".equals(oprID)) {
					JSONArray jsonArray = CLASSJson.getJSONArray("data");
					int j = 0;
					for (j = 0; j < jsonArray.size(); j++) {
						String roleStr = jsonArray.getString(j);
						// 将字符串转换成json;
						JSONObject roleCLASSJson = PaseJsonUtil.getJson(roleStr);
						String roleID = roleCLASSJson.getString("roleID");
						String isRole = roleCLASSJson.getString("isRole");
						if ("true".equals(isRole)) {
							String addRoleSQL = "INSERT INTO PSROLEUSER(ROLEUSER,ROLENAME,DYNAMIC_SW) VALUES(?,?,'N')";
							try {
								jdbcTemplate.update(addRoleSQL, oprID, roleID);
							} catch (DataAccessException e) {

							}
						} else {
							String deleteRoleSQL = "DELETE FROM PSROLEUSER WHERE ROLEUSER = ? AND ROLENAME = ?";
							try {
								jdbcTemplate.update(deleteRoleSQL, oprID, roleID);
							} catch (DataAccessException e) {

							}

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
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		try {
	
			String oprID = "";

			int num = 0;
			for (num = 0; num < actData.length; num++) {
				String strForm = actData[num];
				JSONObject CLASSJson = PaseJsonUtil.getJson(strForm);
				// 类型标志;
				String strFlag = CLASSJson.getString("typeFlag");

				// 用户账号信息;
				if ("USER".equals(strFlag)) {
					// 信息内容;
					String infoData = CLASSJson.getString("data");
					// 将字符串转换成json;
					CLASSJson = PaseJsonUtil.getJson(infoData);
					// 登录账号;
					String strActNum = CLASSJson.getString("usAccNum");
					// 机构编号;
					String orgID = CLASSJson.getString("orgId");
					// 查看是否已经存在;
					String isExistSQL = "SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=?";
					try {
						oprID = jdbcTemplate.queryForObject(isExistSQL, new Object[] { strActNum, orgID },
								String.class);
					} catch (DataAccessException e) {

					}
					if (oprID == null || "".equals(oprID)) {
						errMsg[0] = "1";
						errMsg[1] = "机构编号为：" + orgID + "，登录账号为：" + strActNum + "的信息不存在，更新失败。";
						return strRet;
					}

					// 用户名称;
					String usName = CLASSJson.getString("usName");
					// 手机号码;
					String mobile = CLASSJson.getString("mobile");
					// 电子邮箱;
					String email = CLASSJson.getString("email");
					// 手机号码;
					String bdMobile = CLASSJson.getString("bdMobile");
					// 电子邮箱;
					String bdEmail = CLASSJson.getString("bdEmail");
					// 邮箱绑定标志;
					String eBindFlag = CLASSJson.getString("eBindFlag");
					// 手机绑定标志;
					String mBindFlag = CLASSJson.getString("mBindFlag");
					// 激活状态;
					String jhState = CLASSJson.getString("jhState");
					// 激活方式;
					String jhMethod = CLASSJson.getString("jhMethod");
					// 账号类型;
					String rylx = CLASSJson.getString("rylx");
					// 账号密码;
					String password = CLASSJson.getString("password");
					password = DESUtil.encrypt(password, "TZGD_Tranzvision");
					// 锁定账号;
					String acctLock = "";

					if (CLASSJson.containsKey("acctLock")) {
						acctLock = CLASSJson.getString("acctLock");
					}
					// 原机构ID，判断是否修改了用户的所属机构;
					String originOrgId = CLASSJson.getString("originOrgId");

					// 检查绑定手机是否被占用;
					String isBd = "";
					if ("Y".equals(mBindFlag)) {
						String isBdPhoneExist = "SELECT 'Y' FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_MOBILE=? AND TZ_SJBD_BZ='Y' AND TZ_JG_ID=? and TZ_DLZH_ID <> ?";
						try {
							isBd = jdbcTemplate.queryForObject(isBdPhoneExist,
									new Object[] { bdMobile, orgID, strActNum }, String.class);
						} catch (DataAccessException e) {

						}

						if ("Y".equals(isBd)) {
							errMsg[0] = "1";
							errMsg[1] = "当前机构下，该手机已经被占用";
							return strRet;
						}
					}

					// 检查绑定邮箱是否被占用;

					if ("Y".equals(eBindFlag)) {
						String isBdEmlExist = "SELECT 'Y' FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_EMAIL=? AND TZ_YXBD_BZ='Y' AND TZ_JG_ID=? and TZ_DLZH_ID <> ?";
						try {
							isBd = jdbcTemplate.queryForObject(isBdEmlExist, new Object[] { bdEmail, orgID, strActNum },
									String.class);
						} catch (DataAccessException e) {

						}

						if ("Y".equals(isBd)) {
							errMsg[0] = "1";
							errMsg[1] = "当前机构下，该邮箱已经被占用";
							return strRet;
						}
					}

					if (originOrgId != null && !"".equals(originOrgId) && !originOrgId.equals(orgID)) {
						String originOprId = "";
						// 删除原账号的权限;
						String originOpridSql = "SELECT OPRID from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID=? and TZ_DLZH_ID=?";
						try {
							originOprId = jdbcTemplate.queryForObject(originOpridSql,
									new Object[] { originOrgId, strActNum }, String.class);
						} catch (DataAccessException e) {

						}

						if (originOprId != null && !"".equals(originOprId)) {
							String deleteRoleSql = "DELETE from PSROLEUSER WHERE ROLEUSER=?";
							try {
								jdbcTemplate.update(deleteRoleSql, originOprId);
							} catch (DataAccessException e) {

							}
						}

					}

					String updateOprdSql = "update PS_TZ_AQ_YHXX_TBL set TZ_REALNAME = ?, TZ_EMAIL  = ?,TZ_MOBILE = ?,TZ_RYLX = ?,TZ_YXBD_BZ = ?,TZ_SJBD_BZ = ?,TZ_JIHUO_ZT = ?,TZ_JIHUO_FS = ?,ROW_LASTMANT_DTTM = curdate(),ROW_LASTMANT_OPRID = ? where TZ_DLZH_ID=? and TZ_JG_ID=?";
					try {
						/**** TODO %OPRID *****/
						jdbcTemplate.update(updateOprdSql, usName, bdEmail, bdMobile, rylx, eBindFlag, mBindFlag,
								jhState, jhMethod, "TZ_7", strActNum, orgID);
					} catch (DataAccessException e) {

					}

					String updatePSOPRDEFNSQL = "update PSOPRDEFN set OPERPSWD = ?,ACCTLOCK = ?,LASTUPDDTTM =curdate(),LASTUPDOPRID = ? where OPRID=?";
					try {
						int acctLockNum;
						if ("on".equals(acctLock)) {
							acctLockNum = 1;
						} else {
							acctLockNum = 0;
						}
						/**** TODO %OPRID *****/
						jdbcTemplate.update(updatePSOPRDEFNSQL, password, acctLockNum, "TZ_7", oprID);
					} catch (DataAccessException e) {

					}

					// 联系方式;
					int isExistNum = 0;
					String isExistLXFS = "SELECT COUNT(1) FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY=? and TZ_LYDX_ID=?";
					try {
						isExistNum = jdbcTemplate.queryForObject(isExistLXFS, new Object[] { rylx, oprID },
								Integer.class);
					} catch (DataAccessException e) {

					}

					if (isExistNum <= 0) {
						if ((mobile != null && !"".equals(mobile)) || (email != null && !"".equals(email))) {
							String lsfsSQL = "INSERT INTO PS_TZ_LXFSINFO_TBL(TZ_LXFS_LY,TZ_LYDX_ID,TZ_ZY_SJ,TZ_ZY_EMAIL) VALUES(?,?,?,?)";
							try {
								jdbcTemplate.update(lsfsSQL, rylx, oprID, mobile, email);
							} catch (DataAccessException e) {

							}
						}
					} else {
						String updatelSFSSql = "UPDATE PS_TZ_LXFSINFO_TBL SET TZ_ZY_SJ=?,TZ_ZY_EMAIL=? WHERE TZ_LXFS_LY=? AND TZ_LYDX_ID=?";
						try {
							jdbcTemplate.update(updatelSFSSql, mobile, email, rylx, oprID);
						} catch (DataAccessException e) {

						}
					}
				}

				if ("ROLE".equals(strFlag) && oprID != null && !"".equals(oprID)) {
					JSONArray jsonArray = CLASSJson.getJSONArray("data");
					int j = 0;
					for (j = 0; j < jsonArray.size(); j++) {
						String roleStr = jsonArray.getString(j);
						// 将字符串转换成json;
						JSONObject roleCLASSJson = PaseJsonUtil.getJson(roleStr);
						String roleID = roleCLASSJson.getString("roleID");
						String isRole = roleCLASSJson.getString("isRole");
						if ("true".equals(isRole)) {
							String isRoleExist = "";
							String isRoleExistSQL = "SELECT 'Y' FROM PSROLEUSER WHERE ROLEUSER=? AND ROLENAME=?";
							try {
								isRoleExist = jdbcTemplate.queryForObject(isRoleExistSQL,
										new Object[] { oprID, roleID }, String.class);
							} catch (DataAccessException e) {

							}
							if (!"Y".equals(isRoleExist)) {
								String addRoleSQL = "INSERT INTO PSROLEUSER(ROLEUSER,ROLENAME,DYNAMIC_SW) VALUES(?,?,'N')";
								try {
									jdbcTemplate.update(addRoleSQL, oprID, roleID);
								} catch (DataAccessException e) {

								}
							}

						} else {
							String deleteRoleSQL = "DELETE FROM PSROLEUSER WHERE ROLEUSER = ? AND ROLENAME = ?";
							try {
								jdbcTemplate.update(deleteRoleSQL, oprID, roleID);
							} catch (DataAccessException e) {

							}
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
		String strRet = "{}";
		try {
			
			JSONObject CLASSJson = PaseJsonUtil.getJson(strParams);
			if (CLASSJson.containsKey("usAccNum") && CLASSJson.containsKey("orgId")) {
				String usAccNum = CLASSJson.getString("usAccNum");
				String userOrg = CLASSJson.getString("orgId");

				// 用户ID,姓名，电子邮件，手机号码，人员类型，邮箱绑定标志，手机绑定标志，激活状态，激活方式;
				String oprID = "", name = "", bdEmail = "", bdMobile = "", perType = "", eBindFlg = "", mBindFlg = "",
						jhState = "", jhMethod = "";
				String email = "", mobile = "";
				String originOrgId = "";
				// 密码;
				String password = "";
				String rylx = "";
				String acctLock = "false";

				if (usAccNum != null && !"".equals(usAccNum)) {

					String sql1 = "SELECT OPRID,TZ_REALNAME,TZ_EMAIL,TZ_MOBILE,TZ_RYLX,TZ_YXBD_BZ,TZ_SJBD_BZ,TZ_JIHUO_ZT,TZ_JIHUO_FS,TZ_RYLX FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=?";
					Map<String, Object> map = null;
					try {
						map = jdbcTemplate.queryForMap(sql1, new Object[] { usAccNum, userOrg });
					} catch (DataAccessException e) {
						e.printStackTrace();
					}
					if (map != null) {
						oprID = TZUtility.transFormchar((String) map.get("OPRID")).trim();
						name = TZUtility.transFormchar((String) map.get("TZ_REALNAME")).trim();
						bdEmail = TZUtility.transFormchar((String) map.get("TZ_EMAIL")).trim();
						bdMobile = TZUtility.transFormchar((String) map.get("TZ_MOBILE")).trim();
						perType = TZUtility.transFormchar((String) map.get("TZ_RYLX")).trim();
						eBindFlg = TZUtility.transFormchar((String) map.get("TZ_YXBD_BZ")).trim();
						mBindFlg = TZUtility.transFormchar((String) map.get("TZ_SJBD_BZ")).trim();
						jhState = TZUtility.transFormchar((String) map.get("TZ_JIHUO_ZT")).trim();
						jhMethod = TZUtility.transFormchar((String) map.get("TZ_JIHUO_FS")).trim();
						rylx = TZUtility.transFormchar((String) map.get("TZ_RYLX")).trim();
					}

					map = null;
					String sql2 = "SELECT TZ_ZY_SJ,TZ_ZY_EMAIL FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY=? AND TZ_LYDX_ID=?";
					try {
						map = jdbcTemplate.queryForMap(sql2, new Object[] { rylx, oprID });
					} catch (DataAccessException e) {
						e.printStackTrace();
					}

					if (map != null) {
						mobile = TZUtility.transFormchar((String) map.get("TZ_ZY_SJ")).trim();
						email = TZUtility.transFormchar((String) map.get("TZ_ZY_EMAIL")).trim();
					}

					int localNum = 0;
					String sql3 = "select OPERPSWD,ACCTLOCK from PSOPRDEFN where OPRID=? ";
					map = null;
					
					try {
						map = jdbcTemplate.queryForMap(sql3, new Object[] { oprID });
					} catch (DataAccessException e) {
					}
					if (map != null) {
						password = TZUtility.transFormchar((String) map.get("OPERPSWD")).trim();
						if (!"".equals(password)) {
							password = DESUtil.decrypt(password, "TZGD_Tranzvision");
						}
						localNum = (int) map.get("ACCTLOCK");
					}
					if (localNum == 1) {
						acctLock = "true";
					}
					originOrgId = userOrg;

				} else {
					jhState = "Y";
					jhMethod = "R";
					rylx = "NBYH";
					originOrgId = "";
				}
				strRet = "{\"usAccNum\":\"" + usAccNum + "\",\"orgId\":\"" + userOrg + "\",\"oprid\":\"" + oprID
						+ "\",\"usName\":\"" + name + "\",\"email\":\"" + email + "\",\"mobile\":\"" + mobile
						+ "\",\"eBindFlag\":\"" + eBindFlg + "\",\"mBindFlag\":\"" + mBindFlg + "\",\"jhState\":\""
						+ jhState + "\",\"jhMethod\":\"" + jhMethod + "\",\"password\":\"" + password
						+ "\",\"reptPassword\":\"" + password + "\",\"perType\":\"" + perType + "\",\"originOrgId\": \""
						+ originOrgId + "\",\"rylx\": \"" + rylx + "\",\"acctLock\":" + acctLock + ",\"bdEmail\":\""
						+ bdEmail + "\",\"bdMobile\":\"" + bdMobile + "\"}";
			} else {
				errMsg[0] = "1";
				errMsg[1] = "无法获取用户信息";
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			e.printStackTrace();
		}
		return strRet;
	}

	/* 获取角色信息列表 */
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errMsg) {
		// 返回值;
		String strRet = "";
		String strContent = "";

		// 页面注册信息总数;
		int numTotal = 0;

		String oprID = "";

		try {
			
			JSONObject CLASSJson = PaseJsonUtil.getJson(strParams);
			// 登录账号;
			String usAccNum = CLASSJson.getString("usAccNum");
			// 机构编号;
			String orgID = CLASSJson.getString("orgId");

			// 从机构新建用户时 需根据角色类型选中对应的角色（普通用户选择角色类型为普通用户的角色，管理员用户选择角色类型为管理员的角色）;
			String roleType = "";
			if (CLASSJson.containsKey("roleType")) {
				roleType = CLASSJson.getString("roleType");
			}

			// 获取用户ID;
			String opridSql = "SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=?";
			try {
				oprID = jdbcTemplate.queryForObject(opridSql, new Object[] { usAccNum, orgID }, String.class);
			} catch (DataAccessException e) {
				e.printStackTrace();
			}

			// 获取角色信息列表sql;
			String sqlRoleList = "";
			// 角色编号，角色描述，是否该用户角色;
			String roleID = "", roleDesc = "", isRole = "";

			/******* 获取该户下的角色信息及所有角色信息; ****/
			List<Map<String, Object>> list = null;
			if (numLimit == 0) {
				if (!"".equals(oprID) && oprID != null) {
					sqlRoleList = "SELECT B.ROLENAME,B.DESCR,'Y' ISROLE ISROLE FROM PSROLEUSER A,PSROLEDEFN B WHERE A.ROLENAME=B.ROLENAME AND A.ROLEUSER = ? AND A.DYNAMIC_SW='N' UNION SELECT C.ROLENAME,A.DESCR,'N' ISROLE FROM PSROLEDEFN A, PS_TZ_JG_ROLE_T C WHERE C.TZ_JG_ID = ? AND A.ROLENAME = C.ROLENAME AND NOT EXISTS (SELECT 'Y' FROM PSROLEUSER B WHERE A.ROLENAME=B.ROLENAME AND B.ROLEUSER = ? AND B.DYNAMIC_SW='N') ORDER BY ISROLE DESC";
					try {
						list = jdbcTemplate.queryForList(sqlRoleList, new Object[] { oprID, orgID, oprID });
					} catch (DataAccessException e) {
						e.printStackTrace();
					}
				} else {
					if (!"".equals(roleType) && roleType != null) {
						sqlRoleList = "SELECT C.ROLENAME,A.DESCR,if(C.TZ_ROLE_TYPE=?,'Y','N') ISROLE FROM PSROLEDEFN A, PS_TZ_JG_ROLE_T C  WHERE C.TZ_JG_ID = ? AND A.ROLENAME = C.ROLENAME";
						try {
							list = jdbcTemplate.queryForList(sqlRoleList, new Object[] { roleType, orgID });
						} catch (DataAccessException e) {
							e.printStackTrace();
						}
					} else {
						sqlRoleList = "SELECT C.ROLENAME,A.DESCR,'N' ISROLE FROM PSROLEDEFN A, PS_TZ_JG_ROLE_T C  WHERE C.TZ_JG_ID = ? AND A.ROLENAME = C.ROLENAME";
						try {
							list = jdbcTemplate.queryForList(sqlRoleList, new Object[] { orgID });
						} catch (DataAccessException e) {
							e.printStackTrace();
						}
					}
				}

			} else {
				if (!"".equals(oprID) && oprID != null) {
					sqlRoleList = "SELECT B.ROLENAME,B.DESCR,'Y' ISROLE FROM PSROLEUSER A,PSROLEDEFN B WHERE A.ROLENAME=B.ROLENAME AND A.ROLEUSER = ? AND A.DYNAMIC_SW='N' UNION SELECT C.ROLENAME,A.DESCR,'N' ISROLE FROM PSROLEDEFN A, PS_TZ_JG_ROLE_T C WHERE C.TZ_JG_ID = ? AND A.ROLENAME = C.ROLENAME AND NOT EXISTS (SELECT 'Y' FROM PSROLEUSER B WHERE A.ROLENAME=B.ROLENAME AND B.ROLEUSER = ? AND B.DYNAMIC_SW='N') ORDER BY ISROLE DESC limit ?,?";
					try {
						list = jdbcTemplate.queryForList(sqlRoleList,
								new Object[] { oprID, orgID, oprID, numStart, numLimit });
					} catch (DataAccessException e) {
						e.printStackTrace();
					}
				} else {
					if (!"".equals(roleType) && roleType != null) {
						sqlRoleList = "SELECT C.ROLENAME,A.DESCR,if(C.TZ_ROLE_TYPE=?,'Y','N') ISROLE FROM PSROLEDEFN A, PS_TZ_JG_ROLE_T C  WHERE C.TZ_JG_ID = ? AND A.ROLENAME = C.ROLENAME limit ?,?";
						try {
							list = jdbcTemplate.queryForList(sqlRoleList,
									new Object[] { roleType, orgID, numStart, numLimit });
						} catch (DataAccessException e) {
							e.printStackTrace();
						}
					} else {
						sqlRoleList = "SELECT C.ROLENAME,A.DESCR,'N' ISROLE FROM PSROLEDEFN A, PS_TZ_JG_ROLE_T C  WHERE C.TZ_JG_ID = ? AND A.ROLENAME = C.ROLENAME limit ?,?";
						try {
							list = jdbcTemplate.queryForList(sqlRoleList, new Object[] { orgID, numStart, numLimit });
						} catch (DataAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}

			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					roleID = (String) list.get(i).get("ROLENAME");
					roleDesc = (String) list.get(i).get("DESCR");
					isRole = (String) list.get(i).get("ISROLE");
					if ("Y".equals(isRole)) {
						isRole = "true";
					} else {
						isRole = "false";
					}

					strContent = strContent + "," + "{\"roleID\":\"" + roleID + "\",\"roleName\":\"" + roleDesc
							+ "\",\"isRole\":" + isRole + "}";
				}
			}

			if (!"".equals(strContent)) {
				strContent = strContent.substring(1);
			}

			String totalSQL = "";
			if (oprID != null && !"".equals(oprID)) {
				totalSQL = "SELECT COUNT(1) FROM (SELECT B.ROLENAME  FROM PSROLEUSER A,PSROLEDEFN B WHERE A.ROLENAME=B.ROLENAME AND A.ROLEUSER = ? AND A.DYNAMIC_SW='N' UNION SELECT C.ROLENAME FROM PSROLEDEFN A, PS_TZ_JG_ROLE_T C WHERE C.TZ_JG_ID = ? AND A.ROLENAME = C.ROLENAME AND NOT EXISTS (SELECT 'Y' FROM PSROLEUSER B WHERE A.ROLENAME=B.ROLENAME AND B.ROLEUSER = ? AND B.DYNAMIC_SW='N')) AS AB";
				try {
					numTotal = jdbcTemplate.queryForObject(totalSQL, new Object[] { oprID, orgID, oprID },Integer.class);
				} catch (DataAccessException e) {

				}

			} else {
				totalSQL = "SELECT COUNT(1) FROM PSROLEDEFN A, PS_TZ_JG_ROLE_T C  WHERE C.TZ_JG_ID = ? AND A.ROLENAME = C.ROLENAME";
				try {
					numTotal = jdbcTemplate.queryForObject(totalSQL, new Object[] { orgID},Integer.class);
				} catch (DataAccessException e) {

				}
			}
			strRet = "{\"total\":" + numTotal + ",\"root\":[" + strContent + "]}";

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

}
