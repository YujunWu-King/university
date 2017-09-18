package com.tranzvision.gd.TZBaseBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;


/**
 * 进程运行详细信息
 * @author zhanglang
 * @version 2017/02/16
 */
@Service("com.tranzvision.gd.TZBaseBundle.service.impl.BatchProcessDetailsImpl")
public class BatchProcessDetailsImpl extends FrameworkImpl{

	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal; 
	
	@Override
	public String tzQuery(String strParams, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("formData", "{}");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String processIns = jacksonUtil.getString("processIns");
			
			Map<String,Object> formMap = new HashMap<String,Object>();
			if(!"".equals(processIns) && processIns != null){
				//查询进程实例信息
				String sql = "select TZ_JG_ID,TZ_YUNX_KZID,TZ_JCFWQ_MC,TZ_JOB_YXZT,TZ_QQCJ_DTTM,TZ_JCJS_DTTM from TZ_JC_SHLI_T where TZ_JCSL_ID=?";
				Map<String,Object> procMap = jdbcTemplate.queryForMap(sql, new Object[]{ processIns });
				if(procMap != null){
					String orgId = procMap.get("TZ_JG_ID").toString();
					String runControlID = procMap.get("TZ_YUNX_KZID").toString();
					String runServer = procMap.get("TZ_JCFWQ_MC").toString();
					String runStatus = procMap.get("TZ_JOB_YXZT").toString();
					String runStatusDesc = "";
					
					switch(runStatus){
						//排队中
						case "QUENED":
							runStatusDesc = "排队中";
							break;
						case "SCHEDULED":
							runStatusDesc = "调度中";
							break;
						//已启动
						case "STARTED":
							runStatusDesc = "已启动";
							break;
						//正在运行中
						case "RUNNING":
							runStatusDesc = "正在运行中";
							break;
						//成功完成
						case "SUCCEEDED":
							runStatusDesc = "<font color='blue'>成功完成</font>";
							break;
						//发生错误
						case "ERROR":
							runStatusDesc = "<font color='red'>发生错误</font>";
							break;
						//发生严重错误
						case "FATAL":
							runStatusDesc = "<font color='red'>发生严重错误</font>";
							break;
						//正在停止
						case "STOPPING":
							runStatusDesc = "正在停止";
							break;
						//已强行终止
						case "TERMINATED":
							runStatusDesc = "已强行终止";
							break;
						//退出
						case "DEAD":
							runStatusDesc = "退出";
							break;
						//默认情况
						default:
							runStatusDesc = runStatus;
					}

					formMap.put("orgId", orgId);
					formMap.put("runControlID", runControlID);
					formMap.put("runServer", runServer);
					formMap.put("runStatus", runStatusDesc);
					formMap.put("statusCode", runStatus);
					
					String dttmFormat = getSysHardCodeVal.getDateTimeFormat();
					SimpleDateFormat dttmSimpleDateFormat = new SimpleDateFormat(dttmFormat);
					
					if(procMap.get("TZ_QQCJ_DTTM") != null){
						Date createDatetime = (Date) procMap.get("TZ_QQCJ_DTTM");
						formMap.put("createDatetime", dttmSimpleDateFormat.format(createDatetime));
					}
					
					if(procMap.get("TZ_JCJS_DTTM") != null){
						Date endDatetime = (Date) procMap.get("TZ_JCJS_DTTM");
						formMap.put("endDatetime", dttmSimpleDateFormat.format(endDatetime));
					}

					rtnMap.replace("formData", formMap);
				}else{
					errorMsg[0] = "1";
					errorMsg[1] = "进程实例不存在";
				}
			}else{
				errorMsg[0] = "1";
				errorMsg[1] = "参数错误";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}

		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
	
	
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", new ArrayList<Map<String, Object>>());

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String processIns = jacksonUtil.getString("processIns");
			String orgId = jacksonUtil.getString("orgId");

			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));

			if(null != processIns && !"".equals(processIns)){
				//查询面试安排总数
				String sql = "SELECT COUNT(*) FROM TZ_JCYX_LOG_T WHERE TZ_JG_ID=? AND TZ_JCSL_ID=?";
				int total = jdbcTemplate.queryForObject(sql, new Object[]{orgId, processIns}, "int");

				ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
				if (total > 0) {
					sql = "SELECT TZ_RZ_LSH,TZ_RZ_JB,date_format(TZ_RZ_DTTM,'%Y-%m-%d %H:%i:%s') as TZ_RZ_DTTM,TZ_RZ_NR FROM TZ_JCYX_LOG_T WHERE TZ_JG_ID=? AND TZ_JCSL_ID=? ORDER BY TZ_RZ_LSH ASC limit ?,?";
					List<Map<String, Object>> listData = jdbcTemplate.queryForList(sql, new Object[]{orgId, processIns, start, limit});

					for(Map<String,Object> mapData : listData){
						Map<String,Object> mapJson = new HashMap<String,Object>();
						
						mapJson.put("orderNum", mapData.get("TZ_RZ_LSH"));
						mapJson.put("level", mapData.get("TZ_RZ_JB"));
						mapJson.put("datetime", mapData.get("TZ_RZ_DTTM"));
						mapJson.put("logContent", mapData.get("TZ_RZ_NR"));
						
						listJson.add(mapJson);
					}
					mapRet.replace("total", total);
					mapRet.replace("root", listJson);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "参数不正确！";
		}
		
		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}
	
	
	/**
	 * 批处理状态描述
	 * @param status
	 * @return
	 */
	public String getBatchProcessStatusDescsr(String status){
		String runStatusDesc = "";
		switch(status){
			//排队中
			case "QUENED":
				runStatusDesc = "排队中";
				break;
			case "SCHEDULED":
				runStatusDesc = "调度中";
				break;
			//已启动
			case "STARTED":
				runStatusDesc = "已启动";
				break;
			//正在运行中
			case "RUNNING":
				runStatusDesc = "正在运行中";
				break;
			//成功完成
			case "SUCCEEDED":
				runStatusDesc = "成功完成";
				break;
			//发生错误
			case "ERROR":
				runStatusDesc = "发生错误";
				break;
			//发生严重错误
			case "FATAL":
				runStatusDesc = "发生严重错误";
				break;
			//正在停止
			case "STOPPING":
				runStatusDesc = "正在停止";
				break;
			//已强行终止
			case "TERMINATED":
				runStatusDesc = "已强行终止";
				break;
			//退出
			case "DEAD":
				runStatusDesc = "退出";
				break;
			//默认情况
			default:
				runStatusDesc = status;
		}
		
		return runStatusDesc;
	}
}
