package com.tranzvision.gd.TZEvaluationSystemBundle.service.impl.module;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 面试评审签到
 * 
 * @author “张超”
 * @since 2019/08/09
 */
@Service
public class InterviewCheckin extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	/*
	 * 业务处理器
	 * 
	 * @param strParams 请求报文json字符串
	 * 
	 * @param errorMsg 错误信息 errorMsg[0] String 错误编码，"0"表示正常，"1"表示错误 errorMsg[1]
	 * String 错误消息
	 * 
	 * @return String 务必返回json格式的字符串
	 * 
	 * @description 通过解析strParams中的如type参数 或者通过request中的type参数判断用户的具体操作执行不同的方法
	 *
	 */
	public String processor(String strParams, String[] errorMsg) {
		JSONObject jsonObject = JSONObject.fromObject(strParams);
		String strReturn = "";
		JacksonUtil jacksonUtil = new JacksonUtil();

		String CLASSID = jsonObject.get("classId").toString();
		String BATCHID = jsonObject.get("batchId").toString();

		String OPRID = tzLoginServiceImpl.getLoginedManagerOprid(request);

		if (OPRID == null || "".equals(OPRID)) {
			return "";
		}

		Map<String, Object> flag = getFlag(OPRID, CLASSID, BATCHID);
		if (1 == ((Integer) flag.get("errorCode"))) {
			return jacksonUtil.Map2json(flag);
		}

		if (jsonObject.containsKey("appInsId")) {
			String appInsId = jsonObject.get("appInsId").toString();
			String isScore = sqlQuery.queryForObject(
					"SELECT TOP 1 'Y' FROM PS_TZ_MP_PW_KS_TBL where TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ? AND TZ_APP_INS_ID = ? and TZ_SCORE_INS_ID > 0",
					new Object[] { CLASSID, BATCHID, appInsId }, "String");

			// 判断学生是否已经被打分
			if (null != isScore && "Y".equals(isScore)) {
				errorMsg[0] = "1";
				errorMsg[1] = "学生已存在打分成绩，无法进行操作！";
				return jacksonUtil.Map2json(flag);
			}
		}

		try {
			String type = jsonObject.get("type").toString();
			// 搜索账号信息
			if ("list".equals(type)) {
				strReturn = queryInfo(jsonObject, errorMsg);
			}
			// 签到和撤销签到操作
			if ("checkIn".equals(type)) {
				strReturn = checkInButton(jsonObject, errorMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strReturn;

	}

	/**
	 * @author “张超”
	 * @param jsonObject
	 * @param errorMsg
	 * @return
	 */
	private String queryInfo(JSONObject jsonObject, String[] errorMsg) {
		// 返回数据map集合
		Map<String, Object> rtnMap = new HashMap<String, Object>();

		try {
			String classId = jsonObject.get("classId").toString();
			String batchId = jsonObject.get("batchId").toString();
			
			String times = jsonObject.get("times").toString();
			String key = jsonObject.get("key").toString();
			Integer nationalId = null;
			String sql = null;

			sql = "SELECT TZ_APP_INS_ID,TZ_REALNAME,NATIONAL_ID,TZ_COMPANY_NAME,TZ_CHECKIN_DTTM  "
					+ "FROM PS_TZ_MSPS_KS_VW WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? ";

			//判断是否选择了时段
			if (null != times && !"".equals(times)) {
				//上午
				if("am".equals(times)){
					sql += " AND EXISTS(SELECT 'Y' FROM TZ_INTERVIEW_GROUP WHERE "
							+ "TZ_CLASS_ID=PS_TZ_MSPS_KS_VW.TZ_CLASS_ID "
							+ "AND TZ_APPLY_PC_ID=PS_TZ_MSPS_KS_VW.TZ_APPLY_PC_ID "
							+ "AND TZ_GROUP_ID=PS_TZ_MSPS_KS_VW.TZ_GROUP_ID AND TZ_GROUP_SPACE='A')";
				}
				//下午
				if("pm".equals(times)){
					sql += " AND EXISTS(SELECT 'Y' FROM TZ_INTERVIEW_GROUP WHERE "
							+ "TZ_CLASS_ID=PS_TZ_MSPS_KS_VW.TZ_CLASS_ID "
							+ "AND TZ_APPLY_PC_ID=PS_TZ_MSPS_KS_VW.TZ_APPLY_PC_ID "
							+ "AND TZ_GROUP_ID=PS_TZ_MSPS_KS_VW.TZ_GROUP_ID AND TZ_GROUP_SPACE='B')";
				}
			}
			
			// 判断key输入的是按姓名检索还是按身份证号检索
			if (null == key || "".equals(key)) {
				//TODO sql += " TZ_CHECKIN_DTTM IS NULL" 取消搜索未签到的考生;
			} else if (isNumeric(key)) {
				nationalId = Integer.parseInt(key);
				sql += " AND RIGHT(NATIONAL_ID,4) LIKE '%" + nationalId + "%'";
			} else {
				key = key.replace("'", "''").replace("_", "[_]").replace("%", "[%]");
				sql += " AND TZ_REALNAME LIKE '%" + key + "%'";
			}

			sql += " ORDER BY TZ_CHECKIN_DTTM ASC,TZ_REALNAME";

			List<Map<String, Object>> list = sqlQuery.queryForList(sql, new Object[] { classId, batchId });
			ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

			// 搜索的sql语句
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> mapList = new HashMap<String, Object>();

				Long TZ_APP_INS_ID = Long.valueOf(String.valueOf(list.get(i).get("TZ_APP_INS_ID")));
				String TZ_REALNAME = (String) list.get(i).get("TZ_REALNAME");
				String NATIONAL_ID = (String) list.get(i).get("NATIONAL_ID");
				String TZ_COMPANY_NAME = (String) list.get(i).get("TZ_COMPANY_NAME");
				String TZ_CHECKIN_DTTM = (String) list.get(i).get("TZ_CHECKIN_DTTM");
				String checkInStatus = "N";

				if (TZ_CHECKIN_DTTM != null) {
					checkInStatus = "Y";
				}

				String photo = sqlQuery.queryForObject(
						"SELECT B.TZ_ATT_A_URL + B.TZ_ATTACHSYSFILENA FROM PS_TZ_OPR_PHT_GL_T A,PS_TZ_OPR_PHOTO_T B WHERE A.TZ_ATTACHSYSFILENA = B.TZ_ATTACHSYSFILENA AND EXISTS(SELECT 1 FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID=? AND OPRID=A.OPRID)",
						new Object[] { TZ_APP_INS_ID }, "String");

				mapList.put("company", TZ_COMPANY_NAME);
				mapList.put("appInsId", TZ_APP_INS_ID);
				mapList.put("name", TZ_REALNAME);
				mapList.put("nationalId", NATIONAL_ID);
				mapList.put("checkInTime", TZ_CHECKIN_DTTM);
				mapList.put("checkInStatus", checkInStatus);
				mapList.put("photo", photo);
				listData.add(mapList);
			}

			rtnMap.put("item", listData);
			rtnMap.put("errorCode", 0);
			rtnMap.put("error_decription", "处理成功");

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}

		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(rtnMap);
	}

	/**
	 * @author “张超”
	 * @param jsonObject
	 * @param errorMsg
	 * @return
	 */
	private String checkInButton(JSONObject jsonObject, String[] errorMsg) {
		Map<String, Object> rtnMap = new HashMap<String, Object>();

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 获取传过来的参数值
			String operation = jsonObject.get("operation").toString();
			String classId = jsonObject.get("classId").toString();
			String batchId = jsonObject.get("batchId").toString();
			String appInsId = jsonObject.get("appInsId").toString();

			Timestamp timestamp = null;
			String lastmant_oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			// 签到操作的sql语句
			String sql = null;

			// 签到操作
			if ("checkIn".equals(operation)) {

				// List<Map<String,Object>> queryForList =
				// sqlQuery.queryForList("SELECT a.TZ_PWEI_OPRID FROM
				// ps_TZ_MSPS_PW_TBL A INNER JOIN PS_TZ_AQ_YHXX_TBL D ON
				// A.TZ_PWEI_OPRID = D.OPRID INNER JOIN PS_TZ_INTEGROUP_T B ON
				// a.TZ_PWEI_GRPID = b.TZ_CLPS_GR_ID INNER JOIN
				// PS_TZ_MSPS_KSH_TBL C ON (B.TZ_GROUP_ID = C.TZ_GROUP_ID AND
				// A.TZ_CLASS_ID = C.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID =
				// C.TZ_APPLY_PC_ID) WHERE a.TZ_PWEI_ZHZT = 'A' AND
				// A.TZ_CLASS_ID = ? AND A.TZ_APPLY_PC_ID = ? AND
				// C.TZ_APP_INS_ID = ?", new
				// Object[]{classId,batchId,appInsId});
				// if(null==queryForList||0>=queryForList.size()){
				// rtnMap.put("errorCode", 1);
				// rtnMap.put("error_decription","没有面试评委！");
				// return jacksonUtil.Map2json(rtnMap);
				// }else{
				String signature = jsonObject.get("signature").toString();
				sql = "update PS_TZ_MSPS_KSH_TBL set TZ_CHECKIN_DTTM=?,TZ_SIGNATURE=?,ROW_LASTMANT_DTTM=?,ROW_LASTMANT_OPRID=? where";
				sql += " TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and  TZ_APP_INS_ID=?";

				// 获取当前的时间
				Date date = new Date();
				timestamp = new Timestamp(date.getTime());
				sqlQuery.update(sql,
						new Object[] { timestamp, signature, timestamp, lastmant_oprid, classId, batchId, appInsId });
				// sqlQuery.update("delete from ps_TZ_MP_PW_KS_TBL where
				// TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ? AND TZ_APP_INS_ID = ?
				// and TZ_SCORE_INS_ID = '0'", new
				// Object[]{classId,batchId,appInsId});
				// //建立评委考生关系
				// queryForList.forEach((item)->{
				// String TZ_PWEI_OPRID = item.get("TZ_PWEI_OPRID").toString();
				// sqlQuery.update("insert into ps_TZ_MP_PW_KS_TBL ("
				// + "TZ_APPLY_PC_ID,"
				// + "TZ_CLASS_ID,"
				// + "TZ_APP_INS_ID,"
				// + "TZ_PWEI_OPRID,"
				// + "ROW_ADDED_DTTM,"
				// + "ROW_ADDED_OPRID,"
				// + "ROW_LASTMANT_DTTM,"
				// + "ROW_LASTMANT_OPRID,"
				// + "TZ_SCORE_INS_ID,"
				// + "TZ_DELETE_ZT,"
				// + "TZ_PSHEN_ZT)"
				// + " values ("
				// + "?,"
				// + "?,"
				// + "?,"
				// + "?,"
				// + "getdate(),"
				// + "?,"
				// + "getdate(),"
				// + "?,"
				// + "'0',"
				// + "'N',"
				// + "'N')", new
				// Object[]{batchId,classId,appInsId,TZ_PWEI_OPRID,lastmant_oprid,lastmant_oprid});
				// });
				rtnMap.put("success", true);
				rtnMap.put("checkInTime", timestamp.toString());
				rtnMap.put("errorCode", 0);
				rtnMap.put("error_decription", "签到成功");
				// }

			}
			// 撤销签到操作
			if ("revoke".equals(operation)) {

				if (null == lastmant_oprid) {
					errorMsg[0] = "1";
					errorMsg[1] = "未知用户操作";
				}
				;

				// 撤销签到的sql语句
				sql = "update PS_TZ_MSPS_KSH_TBL set TZ_CHECKIN_DTTM=?,TZ_SIGNATURE=?,ROW_LASTMANT_DTTM=?,ROW_LASTMANT_OPRID=? where";
				sql += "  TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and  TZ_APP_INS_ID=?";
				sqlQuery.update(sql, new Object[] { timestamp, null, new Timestamp(new Date().getTime()),
						lastmant_oprid, classId, batchId, appInsId });
				// sqlQuery.update("delete from ps_TZ_MP_PW_KS_TBL where
				// TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ? AND TZ_APP_INS_ID = ?
				// and TZ_SCORE_INS_ID = '0'", new
				// Object[]{classId,batchId,appInsId});

				rtnMap.put("success", true);
				rtnMap.put("errorCode", 0);
				rtnMap.put("error_decription", "撤销成功");
			}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
			e.printStackTrace();
		}

		return jacksonUtil.Map2json(rtnMap);
	}

	/**
	 * 判断字符串是否全为数字方法
	 * 
	 * @param str
	 * @return
	 */
	private boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 验证项目开通状态、批次开通状态、面试评委状态
	 * 
	 * @param OPRID
	 *            人员ID
	 * @param CLASSID
	 *            班级ID
	 * @param BATCHID
	 *            批次ID
	 * @return Map.errorCode = 0 验证通过，Map.error_decription = 错误信息
	 */
	public Map<String, Object> getFlag(String OPRID, String CLASSID, String BATCHID) {
		Map<String, Object> flag = new HashMap<String, Object>();
		flag.put("errorCode", 0);
		flag.put("error_decription", "");
		String queryForObject0 = "";
		String queryForObject1 = "";
		String queryForObject2 = "";
		try {
			queryForObject0 = sqlQuery.queryForObject(
					"select 'Y' from PS_TZ_PRJ_INF_T c INNER JOIN PS_TZ_CLASS_INF_T d ON (c.tz_prj_id = d.tz_prj_id) WHERE c.TZ_IS_OPEN <> 'Y' and d.TZ_CLASS_ID = ?",
					new Object[] { CLASSID }, "String");
			queryForObject1 = sqlQuery.queryForObject(
					"select 'Y' from PS_TZ_MSPS_GZ_TBL WHERE TZ_DQPY_ZT <> 'A' and TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ?",
					new Object[] { CLASSID, BATCHID }, "String");
			queryForObject2 = sqlQuery.queryForObject(
					"select 'Y' from PS_TZ_MSPS_PW_TBL WHERE tz_pwei_zhzt NOT IN ('A', 'N') and TZ_pwei_oprid = ? AND TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ?",
					new Object[] { OPRID, CLASSID, BATCHID }, "String");
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ("Y".equals(queryForObject0)) {
			flag.replace("errorCode", 1);
			flag.replace("error_decription", "项目已关闭");
			return flag;
		}
		if ("Y".equals(queryForObject1)) {
			flag.replace("errorCode", 1);
			flag.replace("error_decription", "面试批次已关闭");
			return flag;
		}
		if ("Y".equals(queryForObject2)) {
			flag.replace("errorCode", 1);
			flag.replace("error_decription", "当前账号已停用");
			return flag;
		}
		return flag;
	}
}