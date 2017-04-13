package com.tranzvision.gd.TZJudgesTypeBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsTzAqYhxxTblMapper;
import com.tranzvision.gd.TZAccountMgBundle.dao.PsoprdefnMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTbl;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTblKey;
import com.tranzvision.gd.TZAccountMgBundle.model.Psoprdefn;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author xzx
 * 功能说明：面试评委账号管理列表页面;
 * 原PS类：TZ_GD_JUDEG_PKG:TZ_JUGINFO_CLS
 */
@Service("com.tranzvision.gd.TZJudgesTypeBundle.service.impl.JudgesAccMsServiceImpl")
public class JudgesAccMsServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsTzAqYhxxTblMapper psTzAqYhxxTblMapper;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsoprdefnMapper psoprdefnMapper;
	
	/* 查询面试评委账号列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] { { "TZ_DLZH_ID", "ASC" } };

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_JG_ID", "TZ_DLZH_ID", "TZ_REALNAME" };

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr,comParams, numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("jgID", rowList[0]);
				mapList.put("accountNo", rowList[1]);
				mapList.put("judgeName", rowList[2]);
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}
	/* 新增 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		try {
			String oprID = "";
			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");
				if ("JUDINFO".equals(strFlag)) {
//				if(jacksonUtil.containsKey("data")){
					// 信息内容;
					Map<String, Object> infoData = jacksonUtil.getMap("data");
					// 用户账号;
					String accountNo = (String) infoData.get("accountNo");
					// 所属机构编号;
					String orgID = (String) infoData.get("orgId");
					
					String sql = "select COUNT(1) from PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=?";
					int count = jdbcTemplate.queryForObject(sql, new Object[] { accountNo, orgID }, "Integer");
					if (count > 0) {
						errMsg[0] = "1";
						errMsg[1] = "当前机构下登陆账号为：" + accountNo + "的信息已经存在。";
						return strRet;
					} 
					// 密码;
					String password = (String) infoData.get("password");
					password = DESUtil.encrypt(password, "TZGD_Tranzvision");
					// 用户描述
					String judgeName = (String) infoData.get("judgeName");
					// 手机、邮箱
					String judgePhoneNumber = (String) infoData.get("judgePhoneNumber");
					String judgeEmail = (String) infoData.get("judgeEmail");
					// 用户角色
					String roleName = (String) infoData.get("roleName");
					// 用户类型
					String userType = (String) infoData.get("userType");
					// 人员类型，激活状态
					String tzRylx = "NBYH";
					String tzJihuoZt = "Y";
					// 锁定用户;
					String clockFlag = "";

					if (infoData.containsKey("clockFlag")) {
						clockFlag = (String) infoData.get("clockFlag");
					}

					oprID = "TZ_" + getSeqNum.getSeqNum("PSOPRDEFN", "OPRID");
					PsTzAqYhxxTbl psTzAqYhxxTbl = new PsTzAqYhxxTbl();
					psTzAqYhxxTbl.setTzDlzhId(accountNo);
					psTzAqYhxxTbl.setTzJgId(orgID);
					psTzAqYhxxTbl.setOprid(oprID);
					psTzAqYhxxTbl.setTzRealname(judgeName);
					psTzAqYhxxTbl.setTzEmail(judgeEmail);
					psTzAqYhxxTbl.setTzMobile(judgePhoneNumber);
					psTzAqYhxxTbl.setTzRylx(tzRylx);;
					psTzAqYhxxTbl.setTzJihuoZt(tzJihuoZt);
					psTzAqYhxxTbl.setTzZhceDt(new Date());
					String updateOperid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				    psTzAqYhxxTbl.setRowAddedDttm(new Date());
					psTzAqYhxxTbl.setRowAddedOprid(updateOperid);
					psTzAqYhxxTbl.setRowLastmantDttm(new Date());
					psTzAqYhxxTbl.setRowLastmantOprid(updateOperid);
					psTzAqYhxxTblMapper.insert(psTzAqYhxxTbl);
					
					short clockFlagNum;
					if ("Y".equals(clockFlag) || "on".equals(clockFlag) || "1".equals(clockFlag)) {
						clockFlagNum = 1;
					} else {
						clockFlagNum = 0;
					}
					if ("Y".equals(userType)) {
						userType = "1";
					} else {
						userType = "0";
					}
					Psoprdefn psoprdefn = new Psoprdefn();
					psoprdefn.setOprid(oprID);
					psoprdefn.setOperpswd(password);
					psoprdefn.setAcctlock(clockFlagNum);
					psoprdefn.setLastupddttm(new Date());
					psoprdefn.setLastupdoprid(updateOperid);
					psoprdefnMapper.insert(psoprdefn);
					
					// 联系方式;
					if ((judgePhoneNumber != null && !"".equals(judgePhoneNumber)) || (judgeEmail != null && !"".equals(judgeEmail))) {
						String lsfsSQL = "INSERT INTO PS_TZ_LXFSINFO_TBL(TZ_LXFS_LY,TZ_LYDX_ID,TZ_ZY_SJ,TZ_ZY_EMAIL) VALUES(?,?,?,?)";
						jdbcTemplate.update(lsfsSQL, new Object[]{tzRylx, oprID, judgePhoneNumber, judgeEmail});
					}
					// 评委 账号-类型-关系;
					if ((userType != null && !"".equals(userType))) {
						String yhlxSQL = "INSERT INTO PS_TZ_JUSR_REL_TBL(TZ_JG_ID,OPRID,TZ_JUGTYP_ID) VALUES(?,?,?)";
						jdbcTemplate.update(yhlxSQL, new Object[]{orgID, oprID, userType});
//						//添加用户角色;
//						String roleName="";
//						String sqlType = "SELECT ROLENAME FROM PS_TZ_JUGTYP_TBL WHERE TZ_JUGTYP_ID=?";
//						Map<String, Object> map = jdbcTemplate.queryForMap(sqlType, new Object[] { userType });
						if (roleName != null) {
//							roleName = (String) map.get("ROLENAME");
						
							
						String roleSQL = "INSERT INTO PSROLEUSER(ROLEUSER,ROLENAME,DYNAMIC_SW) VALUES(?,?,?)";
						jdbcTemplate.update(roleSQL, new Object[]{oprID,roleName,"N"});
						}
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
		return strRet;
	}
	
	// 查询评委信息表单
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
		
			if (jacksonUtil.containsKey("accountNo") && jacksonUtil.containsKey("orgId")) {
				String accountNo = jacksonUtil.getString("accountNo");
				String userOrg = jacksonUtil.getString("orgId");

				// 用户ID,姓名，电子邮件，手机号码，人员类型，激活状态，激活方式;
				String oprID = "", name = "", perType = "",
						jhState = "", jhMethod = "";
				String email = "", mobile = "";
				String originOrgId = "";
				String userType = "";
				String roleNameDesc="",roleName="";
				// 密码;
				String password = "";
				String rylx = "";
				String acctLock = "0";

				if (accountNo != null && !"".equals(accountNo)) {
					PsTzAqYhxxTblKey psTzAqYhxxTblKey = new PsTzAqYhxxTblKey();
					psTzAqYhxxTblKey.setTzDlzhId(accountNo);
					psTzAqYhxxTblKey.setTzJgId(userOrg);
					PsTzAqYhxxTbl psTzAqYhxxTbl = psTzAqYhxxTblMapper.selectByPrimaryKey(psTzAqYhxxTblKey);
					
					if (psTzAqYhxxTbl != null) {
						oprID = psTzAqYhxxTbl.getOprid();
						name = psTzAqYhxxTbl.getTzRealname();
//						perType = psTzAqYhxxTbl.getTzRylx();
						jhState = psTzAqYhxxTbl.getTzJihuoZt();
						jhMethod = psTzAqYhxxTbl.getTzJihuoFs();
						rylx = psTzAqYhxxTbl.getTzRylx();
					}

					// 用户类型
					String userTypeSql = "SELECT TZ_JUGTYP_ID FROM PS_TZ_JUSR_REL_TBL WHERE OPRID=?";
					Map<String, Object> map1 = jdbcTemplate.queryForMap(userTypeSql, new Object[] { oprID });
					String sql2 = "SELECT TZ_ZY_SJ,TZ_ZY_EMAIL FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY=? AND TZ_LYDX_ID=?";
					Map<String, Object> map = jdbcTemplate.queryForMap(sql2, new Object[] { rylx, oprID });
					// 角色名称
					String sqlRole = "SELECT A.ROLENAME,DESCR FROM PSROLEUSER A, PSROLEDEFN_VW B WHERE A.ROLENAME=B.ROLENAME AND A.ROLEUSER=? ";
					Map<String, Object> mapRole = jdbcTemplate.queryForMap(sqlRole, new Object[] { oprID });
					

					if (map1 != null) {
						userType = (String) map1.get("TZ_JUGTYP_ID");
					}
					if (map != null) {
						mobile = (String) map.get("TZ_ZY_SJ");
						email = (String) map.get("TZ_ZY_EMAIL");
					}
					if (mapRole != null) {
						roleName = (String) mapRole.get("ROLENAME");
						roleNameDesc = (String) mapRole.get("DESCR");
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
				
				returnJsonMap.put("accountNo", accountNo);
				returnJsonMap.put("orgId", userOrg);
				returnJsonMap.put("oprid", oprID);
				
				returnJsonMap.put("judgeName", name);
				returnJsonMap.put("judgeEmail", email);
				returnJsonMap.put("judgePhoneNumber", mobile);
				
				returnJsonMap.put("jhState", jhState);
				returnJsonMap.put("jhMethod", jhMethod);
				returnJsonMap.put("password", password);
				returnJsonMap.put("rePassword", password);
//				returnJsonMap.put("perType", perType);
				
				returnJsonMap.put("originOrgId", originOrgId);
				returnJsonMap.put("rylx", rylx);
				returnJsonMap.put("clockFlag", acctLock);
//				returnJsonMap.put("bdEmail", bdEmail);
				returnJsonMap.put("userType", userType);
				returnJsonMap.put("roleName", roleName);
				returnJsonMap.put("roleNameDesc", roleNameDesc);

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

	/*删除*/
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
 
		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				String oprID = "";
				// 内容页信息;
				String strUserInfo = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strUserInfo);
				// 登录账号;
				String accountNo = jacksonUtil.getString("accountNo");
				// 机构编号;
				String orgId = jacksonUtil.getString("jgID");
				
			    if(accountNo != null && !"".equals(accountNo)&& orgId != null && !"".endsWith(orgId)){
			    	/*获取用户ID*/
					PsTzAqYhxxTblKey psTzAqYhxxTblKey = new PsTzAqYhxxTblKey();
					psTzAqYhxxTblKey.setTzDlzhId(accountNo);
					psTzAqYhxxTblKey.setTzJgId(orgId);
					PsTzAqYhxxTbl psTzAqYhxxTbl= psTzAqYhxxTblMapper.selectByPrimaryKey(psTzAqYhxxTblKey);
					oprID = psTzAqYhxxTbl.getOprid();
					if(oprID != null && !"".equals(oprID)){
						// 删除评委 账号-类型-关系;
						String yhlxSQL = "DELETE FROM PS_TZ_JUSR_REL_TBL WHERE OPRID=?";
						jdbcTemplate.update(yhlxSQL, new Object[]{oprID});
						// 删除联系方式;
						String lsfsSQL = "DELETE FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LYDX_ID=?";
						jdbcTemplate.update(lsfsSQL, new Object[]{oprID});
						//删除用户信息记录信息;
				    	String deleteYHXXSQL = "DELETE FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=?";
				    	jdbcTemplate.update(deleteYHXXSQL, new Object[]{accountNo,orgId} );
				    	//删除用户;
				    	String deleteOPDSQL = "DELETE FROM PSOPRDEFN WHERE OPRID=?";
				    	jdbcTemplate.update(deleteOPDSQL, new Object[]{oprID} );
//				    	//删除用户角色;
				    	String deleteROLESQL = "DELETE FROM PSROLEUSER WHERE ROLEUSER=?";
				    	jdbcTemplate.update(deleteROLESQL, new Object[]{oprID});
				    	
					}
			    }
					
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
	/*  */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		try {
			String oprID = "";
			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");
				if ("JUDINFO".equals(strFlag)) {
//				if(jacksonUtil.containsKey("data")){
					// 信息内容;
					Map<String, Object> infoData = jacksonUtil.getMap("data");
					// 用户账号;
					String accountNo = (String) infoData.get("accountNo");
					// 所属机构编号;
					String orgID = (String) infoData.get("orgId");
					// 密码;
					String password = (String) infoData.get("password");
					password = DESUtil.encrypt(password, "TZGD_Tranzvision");
					// 用户描述
					String judgeName = (String) infoData.get("judgeName");
					// 手机、邮箱
					String judgePhoneNumber = (String) infoData.get("judgePhoneNumber");
					String judgeEmail = (String) infoData.get("judgeEmail");
					// 用户角色
					String roleName = (String) infoData.get("roleName");
//					String roleNameDesc = (String) infoData.get("roleNameDesc");
					// 用户类型
//					String userType = (String) infoData.get("userType");
					// 人员类型，激活状态
//					String tzRylx = (String) infoData.get("rylx");
//					String tzJihuoZt = "Y";
					// 锁定用户;
					String clockFlag = "";
					
					String tzRylxSql = "SELECT TZ_RYLX from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID=? and TZ_DLZH_ID=?";
					String tzRylx = jdbcTemplate.queryForObject(tzRylxSql,
								new Object[] { orgID, accountNo }, "String");

					if (infoData.containsKey("clockFlag")) {
						clockFlag = (String) infoData.get("clockFlag");
					}
					String originOpridSql = "SELECT OPRID from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID=? and TZ_DLZH_ID=?";
					oprID = jdbcTemplate.queryForObject(originOpridSql,
								new Object[] { orgID, accountNo }, "String");
					
					String updateOprdSql = "update PS_TZ_AQ_YHXX_TBL set TZ_DLZH_ID=?, TZ_REALNAME = ?,TZ_RYLX = ?, TZ_EMAIL  = ?,TZ_MOBILE = ?,ROW_LASTMANT_DTTM = curdate(),ROW_LASTMANT_OPRID = ? where OPRID=?";
					String updateOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
					jdbcTemplate.update(updateOprdSql, new Object[]{accountNo,judgeName, tzRylx,judgeEmail, judgePhoneNumber,updateOprid, oprID});

					/*short userTypeNum;
					if ("Y".equals(userType) || "on".equals(userType) || "1".equals(userType)) {
						userTypeNum = 1;
					} else {
						userTypeNum = 0;
					}*/
					short clockFlagNum;
					if ("Y".equals(clockFlag) || "on".equals(clockFlag) || "1".equals(clockFlag)) {
						clockFlagNum = 1;
					} else {
						clockFlagNum = 0;
					}
					
					Psoprdefn psoprdefn = new Psoprdefn();
					psoprdefn.setOprid(oprID);
					psoprdefn.setOperpswd(password);
					psoprdefn.setAcctlock(clockFlagNum);
					psoprdefn.setLastupddttm(new Date());
					psoprdefn.setLastupdoprid(updateOprid);
					psoprdefnMapper.updateByPrimaryKeySelective(psoprdefn);
					
					// 联系方式;
					int isExistNum = 0;
					String isExistLXFS = "SELECT COUNT(1) FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY=? and TZ_LYDX_ID=?";
					isExistNum = jdbcTemplate.queryForObject(isExistLXFS, new Object[] { tzRylx, oprID },
								"Integer");
					
					if (isExistNum <= 0) {
						if ((judgePhoneNumber != null && !"".equals(judgePhoneNumber)) || (judgeEmail != null && !"".equals(judgeEmail))) {
							String lsfsSQL = "INSERT INTO PS_TZ_LXFSINFO_TBL(TZ_LXFS_LY,TZ_LYDX_ID,TZ_ZY_SJ,TZ_ZY_EMAIL) VALUES(?,?,?,?)";
							jdbcTemplate.update(lsfsSQL, new Object[]{tzRylx, oprID, judgePhoneNumber, judgeEmail});
							
						}
					} else {
						String updatelSFSSql = "UPDATE PS_TZ_LXFSINFO_TBL SET TZ_ZY_SJ=?,TZ_ZY_EMAIL=? WHERE TZ_LXFS_LY=? AND TZ_LYDX_ID=?";
						jdbcTemplate.update(updatelSFSSql, new Object[]{judgePhoneNumber, judgeEmail, tzRylx, oprID});

					}
					// 用户角色;
					int roleNum = 0;
					String isExistRole = "SELECT COUNT(1) FROM PSROLEUSER WHERE ROLEUSER=? ";
					roleNum = jdbcTemplate.queryForObject(isExistRole, new Object[] { oprID },
								"Integer");
					
					if (roleNum <= 0) {
						if ((judgePhoneNumber != null && !"".equals(judgePhoneNumber)) || (judgeEmail != null && !"".equals(judgeEmail))) {
							String roleSQL = "INSERT INTO PSROLEUSER(ROLEUSER,ROLENAME,DYNAMIC_SW) VALUES(?,?,?)";
							jdbcTemplate.update(roleSQL, new Object[]{oprID,roleName,"N"});
						}
					} else {
						String roleSQL = "UPDATE PSROLEUSER SET ROLENAME=?,DYNAMIC_SW=? WHERE ROLEUSER=? ";
						jdbcTemplate.update(roleSQL, new Object[]{roleName, "N", oprID});

					}
					// 评委 账号-类型-关系;
//					String updateZhlxSql = "UPDATE PS_TZ_JUSR_REL_TBL SET TZ_JUGTYP_ID=? WHERE TZ_JG_ID=? AND OPRID=?";
//					jdbcTemplate.update(updateZhlxSql, new Object[]{userTypeNum, orgID, oprID});

				}else{
					errMsg[0] = "1";
					errMsg[1] = "参数错误";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
}


