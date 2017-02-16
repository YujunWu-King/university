package com.tranzvision.gd.TZClmsCsBiaoqzBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZClmsCsBiaoqzBundle.dao.PsTzBiaoqzBqTMapper;
import com.tranzvision.gd.TZClmsCsBiaoqzBundle.dao.PsTzBiaoqzTMapper;
import com.tranzvision.gd.TZClmsCsBiaoqzBundle.model.PsTzBiaoqzBqT;
import com.tranzvision.gd.TZClmsCsBiaoqzBundle.model.PsTzBiaoqzBqTKey;
import com.tranzvision.gd.TZClmsCsBiaoqzBundle.model.PsTzBiaoqzT;
import com.tranzvision.gd.TZClmsCsBiaoqzBundle.model.PsTzBiaoqzTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author ZXW
 * 2017-2-9
 * 标签组定义页面相关类
 *
 */
@Service("com.tranzvision.gd.TZClmsCsBiaoqzBundle.service.impl.BqzDefnImpl")
public class BqzDefnImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzBiaoqzTMapper PsTzBiaoqzTMapper;
	@Autowired
	private PsTzBiaoqzBqTMapper PsTzBiaoqzBqTMapper;

	/* 新增标签组 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");
				//机构编号
				String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				if ("COM".equals(strFlag)) {
					// 标签编号;
					String strbqzID = (String) infoData.get("bqzID");
					// 标签名称;
					String bqzName = (String) infoData.get("bqzName");

					// 是否已经存在;
					String bqzExistSql = "SELECT 'Y' FROM PS_TZ_BIAOQZ_T WHERE TZ_BIAOQZ_ID=? AND TZ_JG_ID=?";
					String isExist = "";
					isExist = jdbcTemplate.queryForObject(bqzExistSql, new Object[] { strbqzID,orgid }, "String");

					if ("Y".equals(isExist)) {
						errMsg[0] = "1";
						errMsg[1] = "编号为：" + strbqzID + "的标签组已经存在，请修改标签组ID。";
						return strRet;
					}
					
					PsTzBiaoqzT PsTzBiaoqzT = new PsTzBiaoqzT();
					PsTzBiaoqzT.setTzBiaoqzId(strbqzID);
					PsTzBiaoqzT.setTzJgId(orgid);
					PsTzBiaoqzT.setTzBiaoqzName(bqzName);
					int i = PsTzBiaoqzTMapper.insert(PsTzBiaoqzT);
					if(i <= 0){
						errMsg[0] = "1";
						errMsg[1] = "保存失败";
					}
				}

				if ("PAGE".equals(strFlag)) {
					strRet = this.tzUpdateBqInfo(infoData, errMsg);
				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}

	/* 修改标签组信息 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
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
				if ("COM".equals(strFlag)) {
					// 标签组编号;
					String strbqzID = (String) infoData.get("bqzID");
					// 标签组名称;
					String bqzName = (String) infoData.get("bqzName");
					//机构编号
					String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
					// 是否已经存在;
					String bqzExistSql = "SELECT 'Y' FROM PS_TZ_BIAOQZ_T WHERE TZ_BIAOQZ_ID=? AND TZ_JG_ID=?";
					String isExist = "";
					isExist = jdbcTemplate.queryForObject(bqzExistSql, new Object[] { strbqzID,orgid }, "String");

					if (!"Y".equals(isExist)) {
						errMsg[0] = "1";
						errMsg[1] = "更新时编号为：" + strbqzID + "的信息不存在。";
						return strRet;
					}
					PsTzBiaoqzT PsTzBiaoqzT = new PsTzBiaoqzT();
					PsTzBiaoqzT.setTzBiaoqzId(strbqzID);
					PsTzBiaoqzT.setTzJgId(orgid);
					PsTzBiaoqzT.setTzBiaoqzName(bqzName);
					//int i = PsTzBiaoqzBqTMapper.insert(PsTzBiaoqzT);
					int i = PsTzBiaoqzTMapper.updateByPrimaryKeySelective(PsTzBiaoqzT);
					if(i <= 0){
						errMsg[0] = "1";
						errMsg[1] = "更新失败";
					}
				}

				if ("PAGE".equals(strFlag)) {
					strRet = this.tzUpdateBqInfo(infoData, errMsg);
				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}

	/* 获取标签组信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			if (jacksonUtil.containsKey("bqzID")) {
				String strbqzID = jacksonUtil.getString("bqzID");
				PsTzBiaoqzTKey key = new PsTzBiaoqzTKey();
				key.setTzBiaoqzId(strbqzID);
				key.setTzJgId(orgid);
				if (strbqzID != null && !"".equals(strbqzID)) {					
					PsTzBiaoqzT PsTzBiaoqzT = PsTzBiaoqzTMapper.selectByPrimaryKey(key);
					if (PsTzBiaoqzT != null) {
						// 标签组信息;
						Map<String, Object> jsonMap = new HashMap<>();
						jsonMap.put("bqzID", strbqzID);
						jsonMap.put("bqzName", PsTzBiaoqzT.getTzBiaoqzName());
						returnJsonMap.replace("formData", jsonMap);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "无法获取组件信息";
					}

				} else {
					errMsg[0] = "1";
					errMsg[1] = "无法获取组件信息";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "无法获取组件信息";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	/* 获取标签列表 */
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			String strbqzID = jacksonUtil.getString("bqzID");
			if (strbqzID != null && !"".equals(strbqzID)) {

				// 标签ID，标签名称;
				String strbqID = "", strbqName = "",strbqDesc="",strbqJava="";
				// 标签列表sql;
				String sqlbqList = "";
				String totalSQL = "";
				//查询组件下的页面列表
				Object[] obj = null;
				if (numLimit == 0) {
					sqlbqList = "SELECT TZ_BIAOQZ_ID,TZ_BIAOQ_ID,TZ_BIAOQZ_NAME,TZ_DESC,TZ_BIAOQZ_JAVA FROM PS_TZ_BIAOQZ_BQ_T WHERE TZ_BIAOQZ_ID=? AND TZ_JG_ID=?";
					obj = new Object[] { strbqzID, orgid };
				} else {
					sqlbqList = "SELECT TZ_BIAOQZ_ID,TZ_BIAOQ_ID,TZ_BIAOQZ_NAME,TZ_DESC,TZ_BIAOQZ_JAVA FROM PS_TZ_BIAOQZ_BQ_T WHERE TZ_BIAOQZ_ID=? AND TZ_JG_ID=? limit ?,?";
					obj = new Object[] { strbqzID, orgid, numStart, numLimit };
				}
				
				int total = 0;
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlbqList, obj);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						strbqzID = (String) list.get(i).get("TZ_BIAOQZ_ID");
						strbqID = (String) list.get(i).get("TZ_BIAOQ_ID");
						strbqName = (String) list.get(i).get("TZ_BIAOQZ_NAME");
						strbqDesc = (String) list.get(i).get("TZ_DESC");
						strbqJava = (String) list.get(i).get("TZ_BIAOQZ_JAVA");						
						Map<String, Object> mapList = new HashMap<String, Object>();
						mapList.put("bqzID", strbqzID);
						mapList.put("bqID", strbqID);
						mapList.put("bqName", strbqName);
						mapList.put("bqDesc", strbqDesc);
						mapList.put("java", strbqJava);
						listData.add(mapList);
					}
					
					totalSQL = "SELECT COUNT(1) FROM PS_TZ_BIAOQZ_BQ_T WHERE TZ_BIAOQZ_ID=? ";
					total = jdbcTemplate.queryForObject(totalSQL,new Object[] { strbqzID },"Integer");
					mapRet.replace("total", total);
					mapRet.replace("root", listData);
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "无法获取组件页面信息";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return jacksonUtil.Map2json(mapRet);
	}

	/* 删除标签列表 */
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
				// 标签组ID;
				String sbqzID = jacksonUtil.getString("bqzID");
				// 标签ID;
				String sbqID = jacksonUtil.getString("bqID");
				String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				
				PsTzBiaoqzBqT PsTzBiaoqzBqT = new PsTzBiaoqzBqT();
				PsTzBiaoqzBqT.setTzBiaoqzId(sbqzID);
				PsTzBiaoqzBqT.setTzBiaoqId(sbqID);
				PsTzBiaoqzBqT.setTzJgId(orgid);
				PsTzBiaoqzBqTMapper.deleteByPrimaryKey(PsTzBiaoqzBqT);
				
				//如果页面删除了，则删除改组件和页面对应的权限;
				//String deleteSQL = "DELETE from PS_TZ_BIAOQZ_BQ_T WHERE TZ_JG_ID=? AND TZ_BIAOQZ_ID=? AND TZ_BIAOQ_ID=?";
				//jdbcTemplate.update(deleteSQL,new Object[]{orgid,sbqzID,sbqID});
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}

	private String tzUpdateBqInfo(Map<String, Object> infoData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		try {
			// 标签组编号;
			String strbqzID = (String) infoData.get("bqzID");
			// 标签编号;
			String strbqID = (String) infoData.get("bqID");
			String strJava = (String) infoData.get("java");
			String strDesc = (String) infoData.get("bqDesc");
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			// 标签列表TZ_BIAOQZ_BQ_T;
			PsTzBiaoqzBqT PsTzBiaoqzBqT = new PsTzBiaoqzBqT();
			PsTzBiaoqzBqT.setTzBiaoqzId(strbqzID);
			PsTzBiaoqzBqT.setTzJgId(orgid);
			PsTzBiaoqzBqT.setTzBiaoqId(strbqID);
			PsTzBiaoqzBqT.setTzBiaoqzJava(strJava);
			PsTzBiaoqzBqT.setTzDesc(strDesc);
			PsTzBiaoqzBqTMapper.updateByPrimaryKeySelective(PsTzBiaoqzBqT);
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

}
