package com.tranzvision.gd.TZDistrubutionAppBunld.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZDistrubutionAppBunld.dao.PsTzTappUserTblMapper;
import com.tranzvision.gd.TZDistrubutionAppBunld.dao.PsTzTranzAppTblMapper;
import com.tranzvision.gd.TZDistrubutionAppBunld.model.PsTzTappUserTblKey;
import com.tranzvision.gd.TZDistrubutionAppBunld.model.PsTzTranzAppTbl;
import com.tranzvision.gd.TZDistrubutionAppBunld.model.PsTzTranzAppTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZDistrubutionAppBunld.service.impl.TzDistributionDefnServiceImpl")
public class TzDistributionDefnServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzTranzAppTblMapper psTzTranzAppTblMapper;
	@Autowired
	private PsTzTappUserTblMapper psTzTappUserTblMapper;
	
	/* 新增应用分配*/
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("success", "false");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return jacksonUtil.Map2json(returnMap);
		}
		
		try {
			
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				if ("TRANZAPP".equals(strFlag)) {
					//jgId
					String jgId = (String) infoData.get("jgId");
					//appId;
					String appId = (String) infoData.get("appId");
					// appName;
					String appName = (String) infoData.get("appName");
					//appDesc;
					String appSecret = (String) infoData.get("appSecret");
					//appDesc
					String appDesc = (String) infoData.get("appDesc");

					// 是否已经存在;
					String comExistSql = "SELECT 'Y' from PS_TZ_TRANZ_APP_TBL WHERE TZ_JG_ID=? AND TZ_TRANZ_APPID=?";
					String isExist = "";
					isExist = jdbcTemplate.queryForObject(comExistSql, new Object[] {jgId,appId }, "String");

					if ("Y".equals(isExist)) {
						errMsg[0] = "1";
						errMsg[1] = "appId为：" + appId + "的信息已被其他应用分配使用，请重新生成。";
						return jacksonUtil.Map2json(returnMap);
					}

					PsTzTranzAppTbl psTzTranzAppTbl = new PsTzTranzAppTbl();
					psTzTranzAppTbl.setTzJgId(jgId);
					psTzTranzAppTbl.setTzTranzAppid(appId);
					psTzTranzAppTbl.setTzTranzAppname(appName);
					psTzTranzAppTbl.setTzTranzAppsecret(appSecret);
					psTzTranzAppTbl.setTzDescr(appDesc);
					
					int i = psTzTranzAppTblMapper.insert(psTzTranzAppTbl);
					if(i <= 0){
						errMsg[0] = "1";
						errMsg[1] = "保存失败";
						
					}else{
						returnMap.replace("success", "true");
					}
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return jacksonUtil.Map2json(returnMap);
	}

	/* 修改组件注册信息 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("success", "false");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return jacksonUtil.Map2json(returnMap);
		}
		
		try {
			
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				if ("TRANZAPP".equals(strFlag)) {
					//jgId
					String jgId = (String) infoData.get("jgId");
					//appId;
					String appId = (String) infoData.get("appId");
					// appName;
					String appName = (String) infoData.get("appName");
					//appDesc;
					String appSecret = (String) infoData.get("appSecret");
					//appDesc
					String appDesc = (String) infoData.get("appDesc");

					PsTzTranzAppTblKey psTzTranzAppTblKey = new PsTzTranzAppTblKey();
					psTzTranzAppTblKey.setTzJgId(jgId);
					psTzTranzAppTblKey.setTzTranzAppid(appId);
					PsTzTranzAppTbl psTzTranzAppTbl = psTzTranzAppTblMapper.selectByPrimaryKey(psTzTranzAppTblKey);
					if(psTzTranzAppTbl == null){
						errMsg[0] = "1";
						errMsg[1] = "保存失败。";
					}else{
						psTzTranzAppTbl.setTzTranzAppname(appName);
						psTzTranzAppTbl.setTzTranzAppsecret(appSecret);
						psTzTranzAppTbl.setTzDescr(appDesc);
						int i = psTzTranzAppTblMapper.updateByPrimaryKeyWithBLOBs(psTzTranzAppTbl);
						if(i <= 0){
							errMsg[0] = "1";
							errMsg[1] = "保存失败";
						}else{
							returnMap.replace("success", "true");
						}
					}
					
					
				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return jacksonUtil.Map2json(returnMap);
	}

	/* 获取应用分配信息 */
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
				
				if (jgId != null && !"".equals(jgId) && appId != null && !"".equals(appId)) {
					PsTzTranzAppTblKey psTzTranzAppTblKey = new PsTzTranzAppTblKey();
					psTzTranzAppTblKey.setTzJgId(jgId);
					psTzTranzAppTblKey.setTzTranzAppid(appId);
					
					PsTzTranzAppTbl psTzTranzAppTbl = psTzTranzAppTblMapper.selectByPrimaryKey(psTzTranzAppTblKey);
					if (psTzTranzAppTbl != null) {
						String appDesc = psTzTranzAppTbl.getTzDescr() == null ? "" :  psTzTranzAppTbl.getTzDescr();
						// 应用分配信息;
						Map<String, Object> jsonMap = new HashMap<>();
						jsonMap.put("jgId", jgId);
						jsonMap.put("appId", appId);
						jsonMap.put("appName", psTzTranzAppTbl.getTzTranzAppname());
						jsonMap.put("appSecret", psTzTranzAppTbl.getTzTranzAppsecret());
						jsonMap.put("appDesc", appDesc);
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

	
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			String jgId = jacksonUtil.getString("jgId");
			String appId = jacksonUtil.getString("appId");
			if (jgId != null && !"".equals(jgId) && appId != null && !"".equals(appId)) {
				//otherUserName,userName,isEnable,accountId,accountName
				
				String sql = "";
				Object[] obj = null;
				if (numLimit == 0) {
					sql = "select TZ_OTH_USER,TZ_OTH_NAME,TZ_DLZH_ID,TZ_ENABLE from PS_TZ_TAPP_USER_TBL where TZ_JG_ID=? and TZ_TRANZ_APPID=?";
					obj = new Object[] { jgId, appId };
				} else {
					sql = "select TZ_OTH_USER,TZ_OTH_NAME,TZ_DLZH_ID,TZ_ENABLE from PS_TZ_TAPP_USER_TBL where TZ_JG_ID=? and TZ_TRANZ_APPID=? limit ?,?";
					obj = new Object[] { jgId, appId, numStart, numLimit };
				}
				
				int total = 0;
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, obj);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String isEnable = (String) list.get(i).get("TZ_ENABLE");
						if (!"Y".equals(isEnable)) {
							isEnable = "N";
						} 
						String otherUserName = list.get(i).get("TZ_OTH_USER") == null ? "" : String.valueOf(list.get(i).get("TZ_OTH_USER"));
						String userName = list.get(i).get("TZ_OTH_NAME") == null ? "" : String.valueOf(list.get(i).get("TZ_OTH_NAME"));
						String accountId = list.get(i).get("TZ_DLZH_ID") == null ? "" : String.valueOf(list.get(i).get("TZ_DLZH_ID"));
						
						Map<String, Object> mapList = new HashMap<String, Object>();
						mapList.put("jgId", jgId);
						mapList.put("appId", appId);
						mapList.put("otherUserName", otherUserName);
						mapList.put("userName", userName);
						mapList.put("accountId", accountId);
						mapList.put("isEnable", isEnable);
						listData.add(mapList);
					}
					
					String totalSQL = "SELECT COUNT(1) FROM PS_TZ_TAPP_USER_TBL where TZ_JG_ID=? and TZ_TRANZ_APPID=?";
					total = jdbcTemplate.queryForObject(totalSQL,new Object[] { jgId,appId },"Integer");
					mapRet.replace("total", total);
					mapRet.replace("root", listData);
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "无法登录用户关系列表信息";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return jacksonUtil.Map2json(mapRet);
	}
	
	
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
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);

				String jgId = jacksonUtil.getString("jgId");
				String appId = jacksonUtil.getString("appId");
				String otherUserName = jacksonUtil.getString("otherUserName");
				
				PsTzTappUserTblKey psTzTappUserTblKey = new PsTzTappUserTblKey();
				psTzTappUserTblKey.setTzJgId(jgId);
				psTzTappUserTblKey.setTzTranzAppid(appId);
				psTzTappUserTblKey.setTzOthUser(otherUserName);
				
				psTzTappUserTblMapper.deleteByPrimaryKey(psTzTappUserTblKey);
				
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}

	
	
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("RANDOM", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		String randomStr = "";
		//生产30位appId;
		if("APPID".equals(oprType)){
			randomStr = this.createRandomString(30);
			returnMap.replace("RANDOM", randomStr);
		}
		
		//生成60位appSecret
		if("APPSECRET".equals(oprType)){
			randomStr = this.createRandomString(60);
			returnMap.replace("RANDOM", randomStr);
		}

		return jacksonUtil.Map2json(returnMap);
	}
	
	public String createRandomString(int len){
		StringBuffer str = new StringBuffer();
		String[] arr = {"0","1","2","3","4","5","6","7","8","9"
				,"a","b","c","d","e","f","g"
				,"h","i","j","k","l","m","n"
				,"o","p","q","r","s","t"
				,"u","v","w","x","y","z"
				,"A","B","C","D","E","F","G"
				,"H","I","J","K","L","M","N"
				,"O","P","Q","R","S","T"
				,"U","V","W","X","Y","Z"};
		for(int i = 0; i < len; i++){
			int randNum = new Random().nextInt(62);
			str.append(arr[randNum]);
		}
		return str.toString();
	}
}
