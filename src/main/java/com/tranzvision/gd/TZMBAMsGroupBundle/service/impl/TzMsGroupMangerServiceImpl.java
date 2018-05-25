package com.tranzvision.gd.TZMBAMsGroupBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzMsPskshTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZMBAMsGroupBundle.service.impl.TzMsGroupMangerServiceImpl")
public class TzMsGroupMangerServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private GetSeqNum getSeqNum;

	// 添加考生
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String, Object> mapRet = new HashMap<String, Object>();
		Date nowdate = new Date();
		String appinsId = "";
		String classId = "";
		String batchId = "";
		String groupid = "";
		Map<String, Object> data = null;

		String strReturn = "";
		String name = "";
		String check = "";
		String gropid = "";
		int sum = 0;
		String strnum;
		int intis = 0;
		String sql = "update PS_TZ_MSPS_KSH_TBL set TZ_GROUP_ID=?,TZ_GROUP_DATE=now(),TZ_ORDER=? where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?";
		String totole = "select max(TZ_ORDER) from PS_TZ_MSPS_KSH_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=?  and TZ_GROUP_ID=?";
		String is = "select count(1) from PS_TZ_MSPS_KSH_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=? and TZ_GROUP_ID=?";
		String updatesql = "update PS_TZ_INTEGROUP_T set TZ_GROUP_NAME=? where TZ_GROUP_ID=?";
		try {
			String[] appinsIds = null;
			for (int i = 0; i < actData.length; i++) {
				// 表单内容
				String strForm = actData[i];
				// System.out.println(strForm);
				jacksonUtil.json2Map(strForm);
				// 解析 json
				classId = jacksonUtil.getString("classId");
				batchId = jacksonUtil.getString("batchId");
				appinsId = jacksonUtil.getString("appinsId");

				System.out.println("appinsId:" + appinsId);

				data = jacksonUtil.getMap("data");

				check = data.get("check") == null ? "" : data.get("check").toString();
				name = data.get("groupName") == null ? "" : data.get("groupName").toString();
				gropid = data.get("groupID").toString();
				System.out.println(check);

				sqlQuery.update(updatesql, new Object[] { name, gropid });

				if (check.equals("true") || check.equals("Y")) {

					if (appinsId.indexOf(",") != -1) {
						appinsIds = appinsId.split(",");
						strnum = sqlQuery.queryForObject(totole, new Object[] { classId, batchId, gropid }, "String");
						if (strnum == null || strnum.equals("")) {
							sum = 0;
						} else {
							try {
								sum = Integer.parseInt(strnum);
							} catch (Exception e) {
								sum = 0;
							}
						}
						for (int x = 0; x < appinsIds.length; x++) {
							intis = sqlQuery.queryForObject(is, new Object[] { classId, batchId, appinsIds[x], gropid },
									"Integer");
							// System.out.println(intis);
							// 存在相同的数据 不修改
							if (intis <= 0) {
								// System.out.println(sum);
								sum = sum + 1;
								sqlQuery.update(sql, new Object[] { gropid, sum, classId, batchId, appinsIds[x] });
							}
						}
					} else {
						// System.out.println(gropid);
						intis = sqlQuery.queryForObject(is, new Object[] { classId, batchId, appinsId, gropid },
								"Integer");
						// System.out.println(intis);
						// 存在相同的数据 不修改
						if (intis <= 0) {
							strnum = sqlQuery.queryForObject(totole, new Object[] { classId, batchId, gropid },
									"String");
							if (strnum == null || strnum.equals("")) {
								sum = 0;
							} else {
								try {
									sum = Integer.parseInt(strnum);
								} catch (Exception e) {
									sum = 0;
								}
							}
							// System.out.println(sum);
							sum = sum + 1;
							sqlQuery.update(sql, new Object[] { gropid, sum, classId, batchId, appinsId });
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();

			// TODO: handle exception
		}

		mapRet.put("strReturn", strReturn);

		return jacksonUtil.Map2json(mapRet);
	}

	// 面试规则 其他操作
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		// Map<String, Object> returMap = new HashMap<String, Object>();
		// JacksonUtil jacksonUtil = new JacksonUtil();
		String strRet = "{}";
		try {

			switch (oprType) {
			case "GETGROUP":
				strRet = this.getGroup(strParams);
				break;
			default:

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
			// TODO: handle exception
		}
		return strRet;
	}

	/**
	 * 得到用户分组所属的面试组
	 * 
	 * @param strParams
	 * @return
	 */
	private String getGroup(String strParams) {
		String strRet = "";
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		rtnMap.put("pwGroupId", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			// 班级ID
			String classId = jacksonUtil.getString("classId");
			String batchId = jacksonUtil.getString("batchId");
			String appInsID = jacksonUtil.getString("appInsId");
			// 批量的情况
			if (appInsID.indexOf(",") != -1) {
				rtnMap.put("pwGroupId", "");
			} else {
				String sql = "select B.TZ_CLPS_GR_ID from PS_TZ_INTEGROUP_T B,PS_TZ_MSPS_KSH_TBL A where A.TZ_GROUP_ID=B.TZ_GROUP_ID and A.TZ_CLASS_ID=? and A.TZ_APPLY_PC_ID=? and A.TZ_APP_INS_ID=?";
				strRet = sqlQuery.queryForObject(sql, new Object[] { classId, batchId, appInsID }, "String");
				if (strRet == null) {
					strRet = "";
				}
				rtnMap.put("pwGroupId", strRet);
			}

		} catch (Exception e) {

		}
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}

	/***
	 * 获取评委--面试分组列表，如果没有 默认添加20组分组
	 * 
	 * @param comParams
	 * @param numLimit
	 * @param numStart
	 * @param errorMsg
	 * @return
	 */

	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		String strRet = "";

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(comParams);
			// 班级ID
			String classId = jacksonUtil.getString("classId");
			String batchId = jacksonUtil.getString("batchId");
			String appInsID = jacksonUtil.getString("appInsId");
			String pwGroupId = jacksonUtil.getString("pwGroupId");
			System.out.println("appInsID:" + appInsID);
			if (pwGroupId != null && !pwGroupId.equals("")) {

				Map<String, Object> mapList = null;
				int count = sqlQuery.queryForObject(
						"select count(1) from PS_TZ_INTEGROUP_T where TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? and TZ_CLPS_GR_ID=?",
						new Object[] { classId, batchId, pwGroupId }, "Integer");
				String sql = "";
				// 一条都没有默认添加20个分组
				// start: 0
				// limit: 5
				if (count <= 0) {
					int id = 0;
					sql = "INSERT INTO PS_TZ_INTEGROUP_T(TZ_GROUP_ID,TZ_GROUP_NAME,TZ_CLPS_GR_ID,TZ_CLASS_ID,TZ_APPLY_PC_ID) VALUES(?,?,?,?,?)";
					for (int i = 1; i <= 20; i++) {
						id = getSeqNum.getSeqNum("TZ_INTEGROUP_T", "TZ_GROUP_ID");
						sqlQuery.update(sql, new Object[] { id, i + "组", pwGroupId, classId, batchId });
						mapList = new HashMap<String, Object>();
						mapList.put("check", "");
						mapList.put("groupID", id);
						mapList.put("groupName", i + "组");
						mapList.put("suNum", "0");
						// 分页
						if (i <= 5) {
							listData.add(mapList);
						}
					}
					mapRet.replace("total", 20);
					mapRet.replace("root", listData);
				} else {
					// 有就取出来
					sql = "select TZ_GROUP_ID,TZ_GROUP_NAME from PS_TZ_INTEGROUP_T where TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? and TZ_CLPS_GR_ID=? order by TZ_GROUP_ID limit ?,?";
					String getCheck = "select 'Y' from PS_TZ_MSPS_KSH_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=? AND TZ_GROUP_ID=?";
					String getNum = "select count(1) from PS_TZ_MSPS_KSH_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=?  AND TZ_GROUP_ID=?";
					List<Map<String, Object>> appInsList = sqlQuery.queryForList(sql,
							new Object[] { classId, batchId, pwGroupId, numStart, numLimit });
					String TZ_GROUP_ID = "";
					String TZ_GROUP_NAME = "";
					String isCheck = "";

					for (Map<String, Object> appInsMap : appInsList) {
						TZ_GROUP_ID = appInsMap.get("TZ_GROUP_ID").toString();
						TZ_GROUP_NAME = appInsMap.get("TZ_GROUP_NAME").toString();
						mapList = new HashMap<String, Object>();
						mapList.put("groupID", TZ_GROUP_ID);
						mapList.put("groupName", TZ_GROUP_NAME);
						if (appInsID.indexOf(",") != -1) {
							isCheck = "";
						} else {
							isCheck = sqlQuery.queryForObject(getCheck,
									new Object[] { classId, batchId, appInsID, TZ_GROUP_ID }, "String");
						}
						if (isCheck != null && isCheck.equals("Y")) {
							mapList.put("check", "Y");
						} else {
							mapList.put("check", "");
						}
						mapList.put("suNum", sqlQuery.queryForObject(getNum,
								new Object[] { classId, batchId, TZ_GROUP_ID }, "Integer"));
						listData.add(mapList);
					}
					mapRet.replace("total", 20);
					mapRet.replace("root", listData);
				}
				strRet = jacksonUtil.Map2json(mapRet);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return strRet;
	}
}
