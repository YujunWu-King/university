package com.tranzvision.gd.TZProcessDispatchBundle.service.impl;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBatchProcessBundle.model.TzProcessServer;
import com.tranzvision.gd.TZBatchProcessBundle.model.TzProcessServerKey;
import com.tranzvision.gd.TZProcessDispatchBundle.dao.TzProcessInstanceMapper;
import com.tranzvision.gd.TZProcessDispatchBundle.model.TzProcessInstance;
import com.tranzvision.gd.TZProcessDispatchBundle.model.TzProcessInstanceKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

                String processName = "",processDesc = "", platFormType = "",runConId = "",loop = "",runServer = "",status = "",requestTime = "",runStartTime = "",processStartTime = "",processEndTime = "";

                String sql = "SELECT DISTINCT B.TZ_YXPT_LX,B.TZ_JC_MC,B.TZ_JC_MS,A.TZ_YUNX_KZID,A.TZ_JCFWQ_MC,A.TZ_XH_QZBDS," +
                        "A.TZ_JOB_YXZT,A.TZ_QQCJ_DTTM,A.TZ_JHZX_DTTM,A.TZ_JCKS_DTTM,A.TZ_JCJS_DTTM FROM TZ_JC_SHLI_T A JOIN TZ_JINC_DY_T B " +
                        "ON A.TZ_JC_MC=B.TZ_JC_MC WHERE A.TZ_JCSL_ID=?";
                Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { processInstance});
                if (map != null) {
                    platFormType = map.get("TZ_YXPT_LX") == null?"":map.get("TZ_YXPT_LX").toString();
                    processName = map.get("TZ_JC_MC") == null?"":map.get("TZ_JC_MC").toString();
                    processDesc = map.get("TZ_JC_MS") == null?"":map.get("TZ_JC_MS").toString();
                    runConId = map.get("TZ_YUNX_KZID") == null?"":map.get("TZ_YUNX_KZID").toString();
                    runServer = map.get("TZ_JCFWQ_MC") == null?"":map.get("TZ_JCFWQ_MC").toString();
                    loop = map.get("TZ_XH_QZBDS") == null?"":map.get("TZ_XH_QZBDS").toString();
                    status = map.get("TZ_JOB_YXZT") == null?"":map.get("TZ_JOB_YXZT").toString();
                    requestTime = map.get("TZ_QQCJ_DTTM") == null?"":map.get("TZ_QQCJ_DTTM").toString();
                    runStartTime = map.get("TZ_JHZX_DTTM") == null?"":map.get("TZ_JHZX_DTTM").toString();
                    processStartTime = map.get("TZ_JCKS_DTTM") == null?"":map.get("TZ_JCKS_DTTM").toString();
                    processEndTime = map.get("TZ_JCJS_DTTM") == null?"":map.get("TZ_JCJS_DTTM").toString();
                    Map<String, Object> hMap = new HashMap<String,Object>();

                    hMap.put("processInstanceId", processInstance);
                    hMap.put("processInstance", processInstance);
                    hMap.put("runPlatType", platFormType);
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
            String[] resultFldArray = { "TZ_JCSL_ID","TZ_JC_MC","TZ_DLZH_ID","TZ_JCFWQ_MC","TZ_JOB_YXZT","TZ_JG_ID"};

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
        try {
            int num = 0;
            for (num = 0; num < actData.length; num++) {
                // 表单内容;
                String strForm = actData[num];
                // 将字符串转换成json;
                jacksonUtil.json2Map(strForm);

                // 信息内容;
                String orgId = jacksonUtil.getString("orgId");
                Integer processInstance = Integer.parseInt(jacksonUtil.getString("processInstance"));
                TzProcessInstanceKey tzProcessInstanceKey  = new TzProcessInstanceKey ();
                tzProcessInstanceKey.setTzJgId(orgId);
                tzProcessInstanceKey.setTzJcslId(processInstance);
                tzProcessInstanceMapper.deleteByPrimaryKey(tzProcessInstanceKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errMsg[0] = "1";
            errMsg[1] = e.toString();
        }
        return strRet;
    }
    @Override
    public String tzOther(String OperateType, String comParams, String[] errorMsg) {

        // 返回值;
        String strRet = "{}";
        JacksonUtil jacksonUtil = new JacksonUtil();

        if ("startProcess".equals(OperateType)) {

            jacksonUtil.json2Map(comParams);
            String orgId = jacksonUtil.getString("orgId");
            Integer processInstance = Integer.parseInt(jacksonUtil.getString("processInstance"));
            TzProcessInstance tzProcessInstance = new TzProcessInstance();
            tzProcessInstance.setTzJgId(orgId);
            tzProcessInstance.setTzJcslId(processInstance);
            tzProcessInstance.setTzJobYxzt("SUCCEEDED");
            tzProcessInstanceMapper.updateByPrimaryKeySelective(tzProcessInstance);
            return strRet;
        } else{
            jacksonUtil.json2Map(comParams);
            String orgId = jacksonUtil.getString("orgId");
            Integer processInstance = Integer.parseInt(jacksonUtil.getString("processInstance"));
            TzProcessInstance tzProcessInstance = new TzProcessInstance();
            tzProcessInstance.setTzJgId(orgId);
            tzProcessInstance.setTzJcslId(processInstance);
            tzProcessInstance.setTzJobYxzt("DEAD");
            tzProcessInstanceMapper.updateByPrimaryKeySelective(tzProcessInstance);
            return strRet;
        }

    }
}
