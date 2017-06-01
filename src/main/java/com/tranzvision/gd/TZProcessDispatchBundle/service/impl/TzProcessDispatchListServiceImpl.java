package com.tranzvision.gd.TZProcessDispatchBundle.service.impl;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBatchProcessBundle.model.TzProcessServer;
import com.tranzvision.gd.TZProcessDispatchBundle.dao.TzProcessInstanceMapper;
import com.tranzvision.gd.TZProcessDispatchBundle.model.TzProcessInstance;
import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by WangDi on 2017/4/7.
 */
@Service("com.tranzvision.gd.TZProcessDispatchBundle.service.impl.TzProcessDispatchListServiceImpl")
public class TzProcessDispatchListServiceImpl extends FrameworkImpl{

    @Autowired
    private FliterForm fliterForm;
    @Autowired
    private SqlQuery jdbcTemplate;
    @Autowired
    private GetSeqNum getSeqNum;
    @Autowired
    private TzProcessInstanceMapper tzProcessInstanceMapper;

    // 进程实例信息查询
    public String tzQuery(String strParams, String[] errMsg) {

        Map<String, Object> returnJsonMap = new HashMap<String, Object>();
        returnJsonMap.put("formData", "");
        JacksonUtil jacksonUtil = new JacksonUtil();
        try {
            jacksonUtil.json2Map(strParams);

            if (jacksonUtil.containsKey("user")&&jacksonUtil.containsKey("processName")) {
                String user = jacksonUtil.getString("user");
                String jcName = jacksonUtil.getString("processName");
                Map<String,Object> hMap = new HashMap<String,Object>();
                SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
                String s_dtm = datetimeFormate.format(new Date());
                String runCntlId = "TX_" + s_dtm + "_" + getSeqNum.getSeqNum("PSPRCSRQST", "RUN_ID");
                hMap.put("user", user);
                hMap.put("jcName", jcName);
                hMap.put("runCntlId", runCntlId);
                returnJsonMap.replace("formData", hMap);
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
            String[][] orderByArr = new String[][] { new String[] { "TZ_JC_MC", "DESC"  }};

            // json数据要的结果字段;
            String[] resultFldArray = { "TZ_JC_MC","TZ_JC_MS","TZ_YXPT_LX"};

            // 可配置搜索通用函数;
            Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

            if (obj != null && obj.length > 0) {

                ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
                list.forEach(resultArray ->{
                    Map<String, Object> mapList = new HashMap<String, Object>();
                    mapList.put("processName", resultArray[0]);
                    mapList.put("processDesc", resultArray[1]);
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
    @Transactional
    public String tzUpdate(String[] actData, String[] errMsg) {

        String strRet = "{}";
        JacksonUtil jacksonUtil = new JacksonUtil();
        int dataLength = actData.length;
        try{
            for(int num = 0; num < dataLength; num++){
                // 表单内容;
                String strForm = actData[num];
                // 将字符串转换成json;
                jacksonUtil.json2Map(strForm);
                // 信息内容;
                String orgId = jacksonUtil.getString("orgId");
                String user = jacksonUtil.getString("user");
                String jcName = jacksonUtil.getString("jcName");
                String processName = jacksonUtil.getString("processName");
                String runCntlId = jacksonUtil.getString("runCntlId") == null?"":jacksonUtil.getString("runCntlId");
                String cycleExpression = jacksonUtil.getString("cycleExpression") == null?"":jacksonUtil.getString("cycleExpression");

                //日期类型存储
                Date runStartDate = new Date();
                if(jacksonUtil.getString("runDate") != null && jacksonUtil.getString("runTime") != null){
                    String dateTime = jacksonUtil.getString("runDate") + jacksonUtil.getString("runTime");
                    SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
                    runStartDate = datetimeFormate.parse(dateTime);
                }

                TzProcessInstance tzProcessInstance = new TzProcessInstance();
                tzProcessInstance.setTzJgId(orgId);
                tzProcessInstance.setTzDlzhId(user);
                tzProcessInstance.setTzJcslId(new Random().nextInt(899999999));
                tzProcessInstance.setTzYunxKzid(runCntlId);
                tzProcessInstance.setTzJcMc(jcName);
                tzProcessInstance.setTzJcfwqMc(processName);
                tzProcessInstance.setTzQqcjDttm(runStartDate);
                tzProcessInstance.setTzXhQzbds(cycleExpression);
                tzProcessInstanceMapper.insertSelective(tzProcessInstance);
            }
        }catch(Exception e){
            e.printStackTrace();
            errMsg[0] = "1";
            errMsg[1] = e.toString();
        }
        return strRet;
    }
}
