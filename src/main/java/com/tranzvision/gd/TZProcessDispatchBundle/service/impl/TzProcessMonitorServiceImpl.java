package com.tranzvision.gd.TZProcessDispatchBundle.service.impl;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZProcessDispatchBundle.dao.TzProcessInstanceMapper;
import com.tranzvision.gd.TZProcessDispatchBundle.model.TzProcessInstance;
import com.tranzvision.gd.TZProcessDispatchBundle.model.TzProcessInstanceKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by WangDi on 2017/4/7.
 */
@Service("com.tranzvision.gd.TZProcessDispatchBundle.service.impl.TzProcessMonitorServiceImpl")
public class TzProcessMonitorServiceImpl extends FrameworkImpl{

    @Autowired
    private FliterForm fliterForm;

    @Autowired
    private TzProcessInstanceMapper tzProcessInstanceMapper;

    @Autowired
    private SqlQuery jdbcTemplate;
    

    // 进程实例信息
    public String tzQuery(String strParams, String[] errMsg) {
        // 返回值;
        Map<String, Object> returnJsonMap = new HashMap<String, Object>();
        returnJsonMap.put("formData", "");
        JacksonUtil jacksonUtil = new JacksonUtil();
        try {
            jacksonUtil.json2Map(strParams);

            if (jacksonUtil.containsKey("processInstance")) {

                // 进程实例ID;
                String processInstance = jacksonUtil.getString("processInstance");
                String orgId = jacksonUtil.getString("orgId");

                String processName = "",processDesc = "", platFormType = "",runConId = "",loop = "",runServer = "",status = "",requestTime = "",runStartTime = "",processStartTime = "",processEndTime = "";

                String sql = "SELECT A.TZ_JC_MC,A.TZ_YUNX_KZID,A.TZ_JCFWQ_MC,A.TZ_XH_QZBDS,A.TZ_JOB_YXZT,A.TZ_QQCJ_DTTM,A.TZ_JHZX_DTTM,A.TZ_JCKS_DTTM, " +
                        "A.TZ_JCJS_DTTM,(SELECT B.TZ_JC_MS FROM TZ_JINC_DY_T B WHERE B.TZ_JC_MC = A.TZ_JC_MC AND B.TZ_JG_ID=?) AS TZ_JC_MS, " +
                		"(SELECT B.TZ_YXPT_LX FROM TZ_JINC_DY_T B WHERE B.TZ_JC_MC = A.TZ_JC_MC AND B.TZ_JG_ID=?) AS TZ_YXPT_LX "+
                        "FROM TZ_JC_SHLI_T A WHERE A.TZ_JCSL_ID =?";
                Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { orgId,orgId,processInstance});
                if (map != null) {
                    platFormType = map.get("TZ_YXPT_LX") == null?"":map.get("TZ_YXPT_LX").toString();
                    processName = map.get("TZ_JC_MC") == null?"":map.get("TZ_JC_MC").toString();
                    processDesc = map.get("TZ_JC_MS") == null?"":map.get("TZ_JC_MS").toString();
                    runConId = map.get("TZ_YUNX_KZID") == null?"":map.get("TZ_YUNX_KZID").toString();
                    runServer = map.get("TZ_JCFWQ_MC") == null?"":map.get("TZ_JCFWQ_MC").toString();
                    loop = map.get("TZ_XH_QZBDS") == null?"":map.get("TZ_XH_QZBDS").toString();
                    status = map.get("TZ_JOB_YXZT") == null?"":map.get("TZ_JOB_YXZT").toString();
                    requestTime = map.get("TZ_QQCJ_DTTM") == null?"":map.get("TZ_QQCJ_DTTM").toString().substring(0, map.get("TZ_QQCJ_DTTM").toString().length()-2);
                    runStartTime = map.get("TZ_JHZX_DTTM") == null?"":map.get("TZ_JHZX_DTTM").toString().substring(0, map.get("TZ_JHZX_DTTM").toString().length()-2);
                    processStartTime = map.get("TZ_JCKS_DTTM") == null?"":map.get("TZ_JCKS_DTTM").toString().substring(0, map.get("TZ_JCKS_DTTM").toString().length()-2);
                    processEndTime = map.get("TZ_JCJS_DTTM") == null?"":map.get("TZ_JCJS_DTTM").toString().substring(0, map.get("TZ_JCJS_DTTM").toString().length()-2);
                    Map<String, Object> hMap = new HashMap<String,Object>();
                    
                    hMap.put("processInstanceId", processInstance);
                    hMap.put("processInstance", processInstance);
                    hMap.put("runPlatType", getRunType(platFormType));
                    hMap.put("processName", processName);
                    hMap.put("processDesc", processDesc);
                    hMap.put("runConId", runConId);
                    hMap.put("runServer", runServer);
                    hMap.put("loop", loop);
                    hMap.put("status", status);
                    hMap.put("requestTime", requestTime);
                    hMap.put("runStartTime", runStartTime);
                    hMap.put("processStartTime", processStartTime);
                    hMap.put("processEndTime", processEndTime);
                    returnJsonMap.replace("formData", hMap);
                }

            } else {
                errMsg[0] = "1";
                errMsg[1] = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            errMsg[0] = "1";
            errMsg[1] = e.toString();
        }
        return jacksonUtil.Map2json(returnJsonMap);
    }
    
    private String getRunType(String str) {
		
    	String platType;
    	switch (str) {
    	
		case "1":
			platType = "Windows";
			break;
		case "2":
			platType = "Unix";
			break;
		default:
			platType = "其他";
			break;
		}
    	return platType;
	}
    @SuppressWarnings("unchecked")
    @Override
    public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

        // 返回值;
        Map<String, Object> mapRet = new HashMap<String, Object>();
        mapRet.put("total", 0);
        mapRet.put("root", "[]");

        ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

        try {
            // 排序字段
            String[][] orderByArr = new String[][] { new String[] { "TZ_JCSL_ID", "DESC"  }};

            // json数据要的结果字段;
            String[] resultFldArray = { "TZ_JCSL_ID","TZ_JC_MC","TZ_DLZH_ID","TZ_JCFWQ_MC","TZ_JOB_YXZT","TZ_JG_ID","TZ_JHZX_DTTM"};

            // 可配置搜索通用函数;
            Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

            if (obj != null && obj.length > 0) {

                ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
                list.forEach(resultArray ->{
                    Map<String, Object> mapList = new HashMap<String, Object>();
                    mapList.put("processInstance", resultArray[0]);
                    mapList.put("processName", resultArray[1]);
                    mapList.put("user", resultArray[2]);
                    mapList.put("processServerName", resultArray[3]);
                    mapList.put("status", resultArray[4]);
                    mapList.put("orgId", resultArray[5]);
                    mapList.put("planExecuteTime", resultArray[6]);
                    listData.add(mapList);
                });
                mapRet.replace("total", obj[0]);
                mapRet.replace("root", listData);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JacksonUtil jacksonUtil = new JacksonUtil();
        return jacksonUtil.Map2json(mapRet);

    }
    @Override
	/* 删除进程实例 */
    public String tzDelete(String[] actData, String[] errMsg) {
    	
        String strRet = "";
        JacksonUtil jacksonUtil = new JacksonUtil();
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
        Set<Boolean> set = new HashSet<Boolean>();
        boolean flag = true;
        set.add(flag);

        for(int i = 0; i < actData.length; i++) {
            	
        	Map<String,Object> map = new HashMap<String,Object>();
        	String strForm = actData[i];
        	jacksonUtil.json2Map(strForm);
        	
        	String orgId = jacksonUtil.getString("orgId");
        	String sqlStatus = "";
        	String processInstance = jacksonUtil.getString("processInstance");
        	String processInstanceSql = "SELECT TZ_JOB_YXZT FROM TZ_JC_SHLI_T WHERE TZ_JG_ID = ? AND TZ_JCSL_ID = ?";
            Map<String,Object> processInstanceMap = jdbcTemplate.queryForMap(processInstanceSql, new String[] {orgId,processInstance});
            
            if(processInstanceMap != null) {
            		
            	sqlStatus = String.valueOf(processInstanceMap.get("TZ_JOB_YXZT"));
            		
                if("STARTED".equals(sqlStatus)||"RUNNING".equals(sqlStatus)||"STOPPING".equals(sqlStatus)) {
                	flag = false;
                	set.add(flag);
                }else {
                		
                	map.put("orgId", orgId);
                	map.put("processInstance", processInstance);
                	resultList.add(map);
                }
            }else {
            	
            	map.put("orgId", orgId);
            	map.put("processInstance", processInstance);
            	resultList.add(map);
            }
            	

        }
            
            for (int j = 0; j < resultList.size(); j++) {

                //删除可以删除的实例
            	String orgId = resultList.get(j).get("orgId").toString();
            	String processInstance = resultList.get(j).get("processInstance").toString();
                TzProcessInstanceKey tzProcessInstanceKey  = new TzProcessInstanceKey ();
                tzProcessInstanceKey.setTzJgId(orgId);
                tzProcessInstanceKey.setTzJcslId(Integer.parseInt(processInstance));
                tzProcessInstanceMapper.deleteByPrimaryKey(tzProcessInstanceKey);
            }
            

            /*全部删除和部分删除返回状态*/

            if(set.contains(false)) {

            	return "{\"status\":\"failed\"}";
            }else {
            	
            	
            	return strRet;
            }

        
    }
    @Override
    public String tzOther(String OperateType, String comParams, String[] errorMsg) {

        // 返回值;
        String strRet = "{}";
        JacksonUtil jacksonUtil = new JacksonUtil();
        jacksonUtil.json2Map(comParams);
        String orgId = jacksonUtil.getString("orgId");
        String processInstance = jacksonUtil.getString("processInstance");
        String processServerName = jacksonUtil.getString("processServerName");
        String sqlStatus = "";
        
        String processSql = "SELECT TZ_YXZT FROM TZ_JC_FWQDX_T WHERE TZ_JG_ID = ? AND TZ_JCFWQ_MC = ?";
        String processInstanceSql = "SELECT TZ_JOB_YXZT FROM TZ_JC_SHLI_T WHERE TZ_JG_ID = ? AND TZ_JCSL_ID = ?";
        Map<String,Object> processMap = jdbcTemplate.queryForMap(processSql, new String[] {orgId,processServerName});
        Map<String,Object> processInstanceMap = jdbcTemplate.queryForMap(processInstanceSql, new String[] {orgId,processInstance});
        if(processInstanceMap != null) {
        	
        	sqlStatus = String.valueOf(processInstanceMap.get("TZ_JOB_YXZT"));
        }

        if ("startProcess".equals(OperateType)) {

            if(processMap != null && ("STARTING".equals(String.valueOf(processMap.get("TZ_YXZT")))||"RUNNING".equals(String.valueOf(processMap.get("TZ_YXZT"))))) {
            	
            	if("SUCCEEDED".equals(sqlStatus)||"ERROR".equals(sqlStatus)||"FATAL".equals(sqlStatus)||"TERMINATED".equals(sqlStatus)) {
            		
                    TzProcessInstance tzProcessInstance = new TzProcessInstance();
                    tzProcessInstance.setTzJgId(orgId);
                    tzProcessInstance.setTzJcslId(Integer.parseInt(processInstance));
                    tzProcessInstance.setTzJobYxzt("QUENED");
                    tzProcessInstanceMapper.updateByPrimaryKeySelective(tzProcessInstance);
                	
            	}else if("QUENED".equals(sqlStatus)){
            		
            		return "{\"status\":\"open\"}";
            	}else {
            		
            		return "{\"status\":\"startfailed\"}";
            	}
		    	
                return strRet;
            }else {
            	
            	return "{\"status\":\"failed\"}";
            }
            
            
            
        } else{
        	
        		if("STARTED".equals(sqlStatus)||"RUNNING".equals(sqlStatus)||"TERMINATED".equals(sqlStatus)) {
        			
                    jacksonUtil.json2Map(comParams);
                    TzProcessInstance tzProcessInstance = new TzProcessInstance();
                    tzProcessInstance.setTzJgId(orgId);
                    tzProcessInstance.setTzJcslId(Integer.parseInt(processInstance));
                    tzProcessInstance.setTzJobYxzt("TERMINATED");
                    tzProcessInstanceMapper.updateByPrimaryKeySelective(tzProcessInstance);
        		}else if("TERMINATED".equals(sqlStatus)){
        			
        			return "{\"status\":\"close\"}";
        		}else{
        			return "{\"status\":\"failed\"}";
        		}

            return strRet;
        }

    }
}
