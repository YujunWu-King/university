package com.tranzvision.gd.TZDispatchLoopBundle.service.impl;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBatchProcessBundle.model.TzProcessServer;
import com.tranzvision.gd.TZDispatchLoopBundle.dao.TzDispatchLoopMapper;
import com.tranzvision.gd.TZDispatchLoopBundle.model.TzDispatchLoop;
import com.tranzvision.gd.TZDispatchLoopBundle.model.TzDispatchLoopKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WangDi on 2017/4/10.
 */
@Service
("com.tranzvision.gd.TZDispatchLoopBundle.service.impl.TzDispatchLoopServiceImpl")
public class TzDispatchLoopServiceImpl extends FrameworkImpl{

    @Autowired
    private FliterForm fliterForm;

    @Autowired
    private TzDispatchLoopMapper tzDispatchLoopMapper;

    @Autowired
    private SqlQuery jdbcTemplate;
    /* 获取主题定义信息 */
    @Override
    public String tzQuery(String strParams, String[] errMsg) {
        // 返回值;
        Map<String, Object> returnJsonMap = new HashMap<String, Object>();
        returnJsonMap.put("formData", "");

        JacksonUtil jacksonUtil = new JacksonUtil();
        try {
            jacksonUtil.json2Map(strParams);

            if (jacksonUtil.containsKey("orgId")&&jacksonUtil.containsKey("loopName")) {

                // 机构ID;
                String orgId = jacksonUtil.getString("orgId");
                //循环名称
                String loopName = jacksonUtil.getString("loopName");

                TzDispatchLoopKey tzDispatchLoopKey = new TzDispatchLoopKey();
                tzDispatchLoopKey.setTzJgId(orgId);
                tzDispatchLoopKey.setTzXhMc(loopName);
                TzDispatchLoop tzDispatchLoop = tzDispatchLoopMapper.selectByPrimaryKey(tzDispatchLoopKey);
                if (tzDispatchLoop != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("orgId", orgId);
                    map.put("loopName", loopName);
                    map.put("loopDesc", tzDispatchLoop.getTzXhMs());
                    map.put("status", tzDispatchLoop.getTzEeBz());
                    returnJsonMap.replace("formData", map);
                } else {
                    errMsg[0] = "1";
                    errMsg[1] = "该循环不存在";
                }

            } else {
                errMsg[0] = "1";
                errMsg[1] = "该循环不存在";
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
            String[][] orderByArr = new String[][] { new String[] { "TZ_XH_MC", "DESC" }};

            // json数据要的结果字段;
            String[] resultFldArray = { "TZ_JG_ID","TZ_XH_MC","TZ_XH_MS","TZ_EE_BZ"};

            // 可配置搜索通用函数;
            Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

            if (obj != null && obj.length > 0) {

                ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
                list.forEach(resultArray ->{
                    Map<String, Object> mapList = new HashMap<String, Object>();
                    mapList.put("orgId", resultArray[0]);
                    mapList.put("loopName", resultArray[1]);
                    mapList.put("loopDesc", resultArray[2]);
                    mapList.put("isUse", resultArray[3]);
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
     * 新增调度循环
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
                Map<String, Object> infoData = jacksonUtil.getMap("data");
                // 信息内容;
                String orgId = infoData.get("orgId").toString();
                String loopName = infoData.get("loopName").toString();
                String loopDesc = infoData.get("loopDesc").toString();
                String status = infoData.get("status").toString();

                //判断数据库中同一机构下循环名称是否存在
                String sql = "select COUNT(1) from tz_xunh_defn_t WHERE TZ_JG_ID = ? and TZ_XH_MC = ?";
                int count = jdbcTemplate.queryForObject(sql, new Object[]{orgId,loopName},"Integer");
                if(count > 0){
                    errMsg[0] = "1";
                    errMsg[1] = "该机构下循环名称的信息已经存在，请修改循环名称。";
                }else{
                    TzDispatchLoop tzDispatchLoop = new TzDispatchLoop();
                    tzDispatchLoop.setTzJgId(orgId);
                    tzDispatchLoop.setTzXhMc(loopName);
                    tzDispatchLoop.setTzXhMs(loopDesc);
                    tzDispatchLoop.setTzEeBz(status);
                    tzDispatchLoopMapper.insertSelective(tzDispatchLoop);
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
     * 编辑进程调度
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
                Map<String, Object> infoData = jacksonUtil.getMap("data");
                String orgId = infoData.get("orgId").toString();
                String loopName = infoData.get("loopName").toString();
                String loopDesc = infoData.get("loopDesc").toString();
                String status = infoData.get("status").toString();

                TzDispatchLoop tzDispatchLoop = new TzDispatchLoop();
                tzDispatchLoop.setTzJgId(orgId);
                tzDispatchLoop.setTzXhMc(loopName);
                tzDispatchLoop.setTzXhMs(loopDesc);
                tzDispatchLoop.setTzEeBz(status);
                tzDispatchLoopMapper.updateByPrimaryKeySelective(tzDispatchLoop);
            }
        }catch(Exception e){
            e.printStackTrace();
            errMsg[0] = "1";
            errMsg[1] = e.toString();
        }
        return strRet;
    }

    @Override
	/* 删除调度循环 */
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
                String loopName = jacksonUtil.getString("loopName");
                TzDispatchLoopKey tzDispatchLoopKey = new TzDispatchLoopKey();
                tzDispatchLoopKey.setTzJgId(orgId);
                tzDispatchLoopKey.setTzXhMc(loopName);
                tzDispatchLoopMapper.deleteByPrimaryKey(tzDispatchLoopKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errMsg[0] = "1";
            errMsg[1] = e.toString();
        }
        return strRet;
    }
}
