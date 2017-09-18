package com.tranzvision.gd.TZDistrubutionAppBunld.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZDistrubutionAppBunld.dao.PsTzTappUserTblMapper;
import com.tranzvision.gd.TZDistrubutionAppBunld.model.PsTzTappUserTbl;
import com.tranzvision.gd.TZDistrubutionAppBunld.model.PsTzTappUserTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZDistrubutionAppBunld.service.impl.TzDistributionUserServiceImpl")
public class TzDistributionUserServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzTappUserTblMapper psTzTappUserTblMapper;
	
	/* 新增组件页面注册信息 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 表单内容;
			String strForm = actData[0];
			// 将字符串转换成json;
			jacksonUtil.json2Map(strForm);
			//机构id;
			String jgId = jacksonUtil.getString("jgId");
			//jgId;
			String appId = jacksonUtil.getString("appId");
			//用户名;
			String otherUserName = jacksonUtil.getString("otherUserName");
			//姓名;
			String userName = jacksonUtil.getString("userName");
			// 是否启用;
			String isEnable = jacksonUtil.getString("isEnable");
			//指定登录账号;
			String accountId = jacksonUtil.getString("accountId");
			
			// 查找当前组件下是否已经存在该页面;
			String isExistSql = "SELECT count(1) FROM PS_TZ_TAPP_USER_TBL where TZ_JG_ID=? and TZ_TRANZ_APPID=? and TZ_OTH_USER=?";

			int count = jdbcTemplate.queryForObject(isExistSql, new Object[] { jgId, appId,otherUserName }, "Integer");

			if (count == 0) {
				PsTzTappUserTbl psTzTappUserTbl = new PsTzTappUserTbl();
				psTzTappUserTbl.setTzJgId(jgId);
				psTzTappUserTbl.setTzTranzAppid(appId);
				psTzTappUserTbl.setTzOthUser(otherUserName);
				psTzTappUserTbl.setTzOthName(userName);
				psTzTappUserTbl.setTzDlzhId(accountId);
				psTzTappUserTbl.setTzEnable(isEnable);
				int i = psTzTappUserTblMapper.insert(psTzTappUserTbl);
				if(i <= 0){
					errMsg[0] = "1";
					errMsg[1] = "保存失败";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "用户名：" + otherUserName + "已经指定过登录账号。";
				return strRet;
			}

		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}

	/* 更新页面注册信息 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 表单内容;
			String strForm = actData[0];
			// 将字符串转换成json;
			jacksonUtil.json2Map(strForm);
			//机构id;
			String jgId = jacksonUtil.getString("jgId");
			//jgId;
			String appId = jacksonUtil.getString("appId");
			//用户名;
			String otherUserName = jacksonUtil.getString("otherUserName");
			//姓名;
			String userName = jacksonUtil.getString("userName");
			// 是否启用;
			String isEnable = jacksonUtil.getString("isEnable");
			//指定登录账号;
			String accountId = jacksonUtil.getString("accountId");
			
			// 查找当前组件下是否已经存在该页面;
			String isExistSql = "SELECT count(1) FROM PS_TZ_TAPP_USER_TBL where TZ_JG_ID=? and TZ_TRANZ_APPID=? and TZ_OTH_USER=?";

			int count = jdbcTemplate.queryForObject(isExistSql, new Object[] { jgId, appId,otherUserName }, "Integer");

			if (count > 0) {
				PsTzTappUserTblKey psTzTappUserTblKey = new PsTzTappUserTblKey();
				psTzTappUserTblKey.setTzJgId(jgId);
				psTzTappUserTblKey.setTzTranzAppid(appId);
				psTzTappUserTblKey.setTzOthUser(otherUserName);
				PsTzTappUserTbl psTzTappUserTbl = psTzTappUserTblMapper.selectByPrimaryKey(psTzTappUserTblKey);
				
				psTzTappUserTbl.setTzOthName(userName);
				psTzTappUserTbl.setTzDlzhId(accountId);
				psTzTappUserTbl.setTzEnable(isEnable);
				int i = psTzTappUserTblMapper.updateByPrimaryKey(psTzTappUserTbl);
				if(i <= 0){
					errMsg[0] = "1";
					errMsg[1] = "保存失败";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "更新的用户名：" + otherUserName + "不存在。";
				return strRet;
			}

		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
	
	
	/* 获取应用分配用户信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("jgId") && jacksonUtil.containsKey("appId")) {
				String jgId = jacksonUtil.getString("jgId");
				String appId = jacksonUtil.getString("appId");
				String otherUserName = jacksonUtil.getString("otherUserName");
				
				if (jgId != null && !"".equals(jgId) && appId != null && !"".equals(appId) && otherUserName != null && !"".equals(otherUserName)) {
					PsTzTappUserTblKey psTzTappUserTblKey = new PsTzTappUserTblKey();
					psTzTappUserTblKey.setTzJgId(jgId);
					psTzTappUserTblKey.setTzTranzAppid(appId);
					psTzTappUserTblKey.setTzOthUser(otherUserName);
					
					PsTzTappUserTbl psTzTappUserTbl = psTzTappUserTblMapper.selectByPrimaryKey(psTzTappUserTblKey);
					if (psTzTappUserTbl != null) {
						// 应用分配信息;
						Map<String, Object> jsonMap = new HashMap<>();
						jsonMap.put("jgId", jgId);
						jsonMap.put("appId", appId);
						jsonMap.put("otherUserName", otherUserName);
						jsonMap.put("userName", psTzTappUserTbl.getTzOthName());
						jsonMap.put("isEnable", psTzTappUserTbl.getTzEnable());
						String accountId = psTzTappUserTbl.getTzDlzhId();
						String accountName = "";
						if(accountId == null){
							accountId = "";
						}else{
							accountName = jdbcTemplate.queryForObject("select TZ_REALNAME from PS_TZ_AQ_YHXX_TBL where TZ_REALNAME=? and TZ_DLZH_ID=?", new Object[]{jgId,accountId},"String");
							if(accountName == null){
								accountName = "";
							}
						}
						jsonMap.put("accountId", accountId);
						jsonMap.put("accountName", accountName);
						returnJsonMap.replace("formData", jsonMap);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "无法获取应用分配信息";
					}

				} else {
					errMsg[0] = "1";
					errMsg[1] = "无法获取应用分配信息";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "无法获取应用分配信息";
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
