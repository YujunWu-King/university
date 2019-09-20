package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl.TzGdMsErrorDataInfoServiceImpl")
public class TzGdMsErrorDataInfoServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private GetSeqNum getSeqNum;

	/**
	 * 获取异常面试信息列表
	 */
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", new ArrayList<Map<String, Object>>());

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String classID = jacksonUtil.getString("classID");
			String batchID = jacksonUtil.getString("batchID");

			if (null != classID && !"".equals(classID) && null != batchID && !"".equals(batchID)) {
				// 查询异常数据
				String sql = "select TZ_APP_INS_ID,TZ_PWEI_OPRID from PS_TZ_MS_ERROR_DATA_V where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=?";
				List<Map<String, Object>> listData = jdbcTemplate.queryForList(sql, new Object[] { classID, batchID});
				int total = listData.size();
				ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
				Map<String, Object> mapJson = null;
				for (Map<String, Object> mapData : listData) {
					String appInsID=mapData.get("TZ_APP_INS_ID").toString();
					String pwOprID=mapData.get("TZ_PWEI_OPRID").toString();
					if (""!=appInsID&&null!=appInsID&&""!=pwOprID&&null!=pwOprID){
						mapJson = new HashMap<String, Object>();
						String sql1="select TZ_PWEI_GRPID,TZ_REALNAME from PS_TZ_PWFZXX_V where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_PWEI_OPRID=?";
						Map<String,Object> mapPw=jdbcTemplate.queryForMap(sql1,new Object[]{classID,batchID,pwOprID});
						String pwGroupID=mapPw.get("TZ_PWEI_GRPID").toString();
						String pwName=mapPw.get("TZ_REALNAME").toString();
						String sql2="select TZ_REALNAME,TZ_MSH_ID,TZ_GROUP_NAME,TZ_ORDER,TZ_GROUP_DATE from PS_TZ_XSFZXX_V where TZ_APP_INS_ID=?";
						Map<String,Object> mapStu=jdbcTemplate.queryForMap(sql2,new Object[]{appInsID});
						String stuName=mapStu.get("TZ_REALNAME").toString();
						String mshID=mapStu.get("TZ_MSH_ID").toString();
						String groupName=mapStu.get("TZ_GROUP_NAME").toString();
						String msOrder=mapStu.get("TZ_ORDER").toString();
						String groupDate=mapStu.get("TZ_GROUP_DATE").toString();
						mapJson.put("appInsID",appInsID);
						mapJson.put("pwOprID",pwOprID);
						mapJson.put("classID",classID);
						mapJson.put("batchID",batchID);
						mapJson.put("stuName",stuName);
						mapJson.put("mshID",mshID);
						mapJson.put("groupName",groupName);
						mapJson.put("msOrder",msOrder);
						mapJson.put("groupDate",groupDate);
						String sql3="select TZ_CLPS_GR_NAME from PS_TZ_MSPS_GR_TBL where TZ_CLPS_GR_ID=?";
						String pwGroupName=jdbcTemplate.queryForObject(sql3,new Object[]{pwGroupID},"String");
						mapJson.put("pwGroupName",pwGroupName);
						mapJson.put("pwName",pwName);
						listJson.add(mapJson);
					}
				}
				mapRet.replace("total", total);
				mapRet.replace("root", listJson);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "参数不正确！";
		}
		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}


	public String tzOther(String strType, String strParams, String[] errorMsg) {
		String strRet = "";
		try {
			switch (strType) {
				case "deleteErrorData":
					//删除异常数据
					strRet = this.deleteErrorData(strParams,errorMsg);
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
		}
		return strRet;
	}


	public String deleteErrorData(String strParams, String[] errMsg) {
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("result", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			List<Map<String,Object>> recs=(List<Map<String, Object>>)jacksonUtil.getList("removerecs");
			if(null!=recs&&recs.size()>0){
				for(Map<String, Object> deleteMap: recs){
					String classID=deleteMap.get("classID")==null?null:String.valueOf(deleteMap.get("classID"));
					String batchID=deleteMap.get("batchID")==null?null:String.valueOf(deleteMap.get("batchID"));
					String appInsID=deleteMap.get("appInsID")==null?null:String.valueOf(deleteMap.get("appInsID"));
					String pwOprID=deleteMap.get("pwOprID")==null?null:String.valueOf(deleteMap.get("pwOprID"));
					System.out.println("班级ID："+classID+"---批次ID"+batchID+"---报名表ID"+appInsID+"---评委ID"+pwOprID);
					String sql="delete from PS_TZ_MP_PW_KS_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=? and TZ_PWEI_OPRID=?";
					jdbcTemplate.update(sql,new Object[]{classID,batchID,appInsID,pwOprID});
				}
				mapRet.replace("result","success");
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(mapRet);
	}
}
