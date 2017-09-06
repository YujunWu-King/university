package com.tranzvision.gd.TZBatchProcessBundle.service.impl;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBatchProcessBundle.dao.TzProcessServerMapper;
import com.tranzvision.gd.TZBatchProcessBundle.model.TzBatchProcess;
import com.tranzvision.gd.TZBatchProcessBundle.model.TzBatchProcessKey;
import com.tranzvision.gd.TZBatchProcessBundle.model.TzProcessServer;
import com.tranzvision.gd.TZBatchProcessBundle.model.TzProcessServerKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WangDi on 2017/4/7.
 */
@Service("com.tranzvision.gd.TZBatchProcessBundle.service.impl.TzProcessServerListServiceImpl")
public class TzProcessServerListServiceImpl extends FrameworkImpl {

    @Autowired
    private FliterForm fliterForm;

    @Autowired
    private SqlQuery jdbcTemplate;

    @Autowired
    TzProcessServerMapper tzProcessServerMapper;

    // 进程服务器信息
    public String tzQuery(String strParams, String[] errMsg) {
        // 返回值;
        Map<String, Object> returnJsonMap = new HashMap<String, Object>();
        returnJsonMap.put("formData", "");
        JacksonUtil jacksonUtil = new JacksonUtil();
        try {
            jacksonUtil.json2Map(strParams);

            if (jacksonUtil.containsKey("orgId")&&jacksonUtil.containsKey("processName")) {
                // 机构ID;
                String orgId = jacksonUtil.getString("orgId");
                // 进程名称;
                String processName = jacksonUtil.getString("processName");

                String processDesc = "", platFormType = "",serverIP = "",status = "",remark = "";
                Integer intervalTime = 0,parallelNum = 0;

                String sql = "SELECT A.TZ_JG_ID,A.TZ_JCFWQ_MC,A.TZ_JCFWQ_MS,A.TZ_FWQ_IP,A.TZ_CZXT_LX,A.TZ_RWXH_JG,A.TZ_ZDBX_RWS,A.TZ_BEIZHU,A.TZ_YXZT " +
                        "FROM TZ_JC_FWQDX_T A WHERE A.TZ_JG_ID = ? AND A.TZ_JCFWQ_MC=?";
                Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { orgId,processName });
                if (map != null) {
                    processDesc = (String) map.get("TZ_JCFWQ_MS");
                    platFormType = (String) map.get("TZ_CZXT_LX");
                    serverIP = (String) map.get("TZ_FWQ_IP");
                    status = map.get("TZ_YXZT") == null?"":map.get("TZ_YXZT").toString();
                    intervalTime = Integer.parseInt(map.get("TZ_RWXH_JG").toString());
                    parallelNum = Integer.parseInt(map.get("TZ_ZDBX_RWS").toString());
                    remark = map.get("TZ_BEIZHU") == null?"":map.get("TZ_BEIZHU").toString();
                    Map<String, Object> hMap = new HashMap<>();
                    hMap.put("orgId", orgId);
                    hMap.put("processName", processName);
                    hMap.put("processDesc", processDesc);
                    hMap.put("runPlatType", platFormType);
                    hMap.put("serverIP", serverIP);
                    hMap.put("intervalTime", intervalTime);
                    hMap.put("parallelNum", parallelNum);
                    if("STARTING".equals(status) ){
                        status = "运行中";
                    }else if ("STOPPING".equals(status)){
                        status = "已停止";
                    }
                    hMap.put("status", status);
                    hMap.put("remark", remark);
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
            String[][] orderByArr = new String[][] { new String[] { "TZ_JCFWQ_MC", "DESC" }};

            // json数据要的结果字段;
            String[] resultFldArray = {"TZ_JG_ID", "TZ_JCFWQ_MC","TZ_JCFWQ_MS","TZ_YXZT","TZ_CZXT_LX"};

            // 可配置搜索通用函数;
            Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

            if (obj != null && obj.length > 0) {

                ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
                list.forEach(resultArray ->{
                    Map<String, Object> mapList = new HashMap<String, Object>();
                    mapList.put("orgId", resultArray[0]);
                    mapList.put("processName", resultArray[1]);
                    mapList.put("processDesc", resultArray[2]);
                    mapList.put("runningStatus", resultArray[3]);
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

    /**
     * 新增进程服务器
     */
    @Override
    @Transactional
    public String tzAdd(String[] actData, String[] errMsg) {

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
                String platType = jacksonUtil.getString("runPlatType");
                String serverIP = jacksonUtil.getString("serverIP");
                String processName = jacksonUtil.getString("processName");
                String processDec = jacksonUtil.getString("processDesc");
                Integer intervalTime = jacksonUtil.getString("intervalTime")==null?0:Integer.parseInt(jacksonUtil.getString("intervalTime"));
                Integer parallelNum = Integer.parseInt(jacksonUtil.getString("parallelNum"));
                String remark = jacksonUtil.getString("remark") == null?"":jacksonUtil.getString("remark");
                String sql = "SELECT 'Y' FROM TZ_JC_FWQDX_T A WHERE A.TZ_JG_ID=? AND A.TZ_JCFWQ_MC=?";
                String isExist =  jdbcTemplate.queryForObject(sql, new Object[]{orgId,processName},"String");
                if("Y".equals(isExist)){
                    errMsg[0] = "1";
                    errMsg[1] = "当前机构已存在相同的进程名称!";
                    return strRet;
                }else {
                    TzProcessServer tzProcessServer = new TzProcessServer();
                    tzProcessServer.setTzJgId(orgId);
                    tzProcessServer.setTzJcfwqMc(processName);
                    tzProcessServer.setTzFwqIp(serverIP);
                    tzProcessServer.setTzCzxtLx(platType);
                    tzProcessServer.setTzJcfwqMs(processDec);
                    tzProcessServer.setTzRwxhJg(intervalTime);
                    tzProcessServer.setTzZdbxRws(parallelNum);
                    tzProcessServer.setTzYxzt("STOPPING");
                    tzProcessServer.setTzBeizhu(remark);

                    tzProcessServerMapper.insertSelective(tzProcessServer);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            errMsg[0] = "1";
            errMsg[1] = e.toString();
        }
        return strRet;
    }

    /**
     * 编辑进程服务器
     */
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
                String processName = jacksonUtil.getString("processName");
                String platType = jacksonUtil.getString("runPlatType");
                String serverIP = jacksonUtil.getString("serverIP");
                String processDec = jacksonUtil.getString("processDesc");
                Integer intervalTime = Integer.parseInt(jacksonUtil.getString("intervalTime"));
                Integer parallelNum = Integer.parseInt(jacksonUtil.getString("parallelNum"));
                String remark = jacksonUtil.getString("remark") == null?"":jacksonUtil.getString("remark");
                TzProcessServer tzProcessServer = new TzProcessServer();
                tzProcessServer.setTzJgId(orgId);
                tzProcessServer.setTzJcfwqMc(processName);
                tzProcessServer.setTzJcfwqMs(processDec);
                tzProcessServer.setTzCzxtLx(platType);
                tzProcessServer.setTzFwqIp(serverIP);
                tzProcessServer.setTzZdbxRws(parallelNum);
                tzProcessServer.setTzRwxhJg(intervalTime);
                tzProcessServer.setTzBeizhu(remark);
                tzProcessServerMapper.updateByPrimaryKeySelective(tzProcessServer);
            }
        }catch(Exception e){
            e.printStackTrace();
            errMsg[0] = "1";
            errMsg[1] = e.toString();
        }
        return strRet;
    }

    @Override
	/* 删除服务器进程 */
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
                String processName = jacksonUtil.getString("processName");
                TzProcessServerKey tzProcessServerKey  = new TzProcessServerKey ();
                tzProcessServerKey.setTzJgId(orgId);
                tzProcessServerKey.setTzJcfwqMc(processName);
                tzProcessServerMapper.deleteByPrimaryKey(tzProcessServerKey);
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
            String processName = jacksonUtil.getString("processName");
            TzProcessServer tzProcessServer = new TzProcessServer();
            tzProcessServer.setTzJgId(orgId);
            tzProcessServer.setTzJcfwqMc(processName);
            tzProcessServer.setTzYxzt("STARTING");
            tzProcessServerMapper.updateByPrimaryKeySelective(tzProcessServer);
            return strRet;
        } else{
            jacksonUtil.json2Map(comParams);
            String orgId = jacksonUtil.getString("orgId");
            String processName = jacksonUtil.getString("processName");
            TzProcessServer tzProcessServer = new TzProcessServer();
            tzProcessServer.setTzJgId(orgId);
            tzProcessServer.setTzJcfwqMc(processName);
            tzProcessServer.setTzYxzt("STOPPING");
            tzProcessServerMapper.updateByPrimaryKeySelective(tzProcessServer);
            return strRet;
        }


    }
}
