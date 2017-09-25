package com.tranzvision.gd.TZDispatchLoopBundle.service.impl;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
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
                    
                    //主表form
                    map.put("orgId", orgId);
                    map.put("loopName", loopName);
                    map.put("loopDesc", tzDispatchLoop.getTzXhMs());
                    map.put("status", tzDispatchLoop.getTzEeBz());
                    
                    
                    
                    //-----------------------------tabpanel分割线------------------
                    //年form                    
                    map.put("beginYear", tzDispatchLoop.getTzYQsnf());
                    map.put("endYear", tzDispatchLoop.getTzYJznf());
                    map.put("yearList", tzDispatchLoop.getTzYLbqz());
                    map.put("yearLoopInterval", tzDispatchLoop.getTzYXhqz());
                    
                    //月form
                    map.put("beginMonth", tzDispatchLoop.getTzM1Qsyf());
                    map.put("endMonth", tzDispatchLoop.getTzM1Jzyf());
                    map.put("monthList", tzDispatchLoop.getTzM1Lbqz());
                    map.put("monthLoopInterval", tzDispatchLoop.getTzM1Xhqz());
                    
                    //周、日form
                    map.put("beginDay1", tzDispatchLoop.getTzDQsrq());
                    map.put("endDay1", tzDispatchLoop.getTzDJzrq());
                    map.put("day1List", tzDispatchLoop.getTzDLbqz());
                    map.put("day1LoopInterval", tzDispatchLoop.getTzDXhqz());
                    map.put("beginDate1", tzDispatchLoop.getTzDZdrq());
                    
                    map.put("beginDay2", tzDispatchLoop.getTzWQsrq());
                    map.put("endDay2", tzDispatchLoop.getTzWJzrq());
                    map.put("day2List", tzDispatchLoop.getTzWLbqz());
                    map.put("day2LoopInterval", tzDispatchLoop.getTzWXhqz());
                    map.put("appointedDate1", tzDispatchLoop.getTzWZdrq1());
                    map.put("appointedWeek", tzDispatchLoop.getTzWZdzc());
                    map.put("appointedDate2", tzDispatchLoop.getTzWZdrq2());
                    
                    //时
                    map.put("beginHour", tzDispatchLoop.getTzHQsxs());
                    map.put("endHour", tzDispatchLoop.getTzHJzxs());
                    map.put("hourList", tzDispatchLoop.getTzHLbqz());
                    map.put("hourLoopInterval", tzDispatchLoop.getTzHXhqz());
                    
                    //分
                    map.put("beginMinute", tzDispatchLoop.getTzM2Qsfz());
                    map.put("endMinute", tzDispatchLoop.getTzM2Jzfz());
                    map.put("minuteList", tzDispatchLoop.getTzM2Lbqz());
                    map.put("minuteLoopInterval", tzDispatchLoop.getTzM2Xhqz());
                    
                    //秒
                    map.put("beginSecond", tzDispatchLoop.getTzSQsm());
                    map.put("endSecond", tzDispatchLoop.getTzSJzm());
                    map.put("secondList", tzDispatchLoop.getTzSLbqz());
                    map.put("secondLoopInterval", tzDispatchLoop.getTzSXhqz());
                    
                    //自定义
                    map.put("customYear", tzDispatchLoop.getTzXhzdY());
                    map.put("customMonth", tzDispatchLoop.getTzXhzdM1());
                    map.put("customWeek", tzDispatchLoop.getTzXhzdW());
                    map.put("customDay", tzDispatchLoop.getTzXhzdD());
                    map.put("customHour", tzDispatchLoop.getTzXhzdH());
                    map.put("customMinute", tzDispatchLoop.getTzXhzdM2());
                    map.put("customSecond", tzDispatchLoop.getTzXhzdS());
                    
                    //check和radio
                    map.put("yearCheck", tzDispatchLoop.getTzYearCheck());
                    map.put("monthCheck", tzDispatchLoop.getTzMonthCheck());
                    map.put("day1Check", tzDispatchLoop.getTzDay1Check());
                    map.put("day2Check", tzDispatchLoop.getTzDay2Check());
                    map.put("hourCheck", tzDispatchLoop.getTzHourCheck());
                    map.put("minuteCheck", tzDispatchLoop.getTzMinuteCheck());
                    map.put("secondCheck", tzDispatchLoop.getTzSecondCheck());
                    
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
        StringBuilder  stringBuilder = new StringBuilder();
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
                
                //年份tabpanel
                Integer tzYQsnf = infoData.get("beginYear") == ""?0:Integer.valueOf(infoData.get("beginYear").toString());
                Integer tzYJznf = infoData.get("endYear") == ""?0:Integer.valueOf(infoData.get("endYear").toString());
                String tzYLbqz = infoData.get("yearList") == null?"":String.valueOf(infoData.get("yearList"));
                String tzYXhqz = infoData.get("yearLoopInterval") == null?"":String.valueOf(infoData.get("yearLoopInterval"));
                
                //月份tabpanel

                Integer tzM1Qsyf = infoData.get("beginMonth") == ""?0:Integer.valueOf(infoData.get("beginMonth").toString());
                Integer tzM1Jzyf = infoData.get("endMonth") == ""?0:Integer.valueOf(infoData.get("endMonth").toString());
                String tzM1Lbqz = infoData.get("monthList") == null?"":String.valueOf(infoData.get("monthList"));
                String tzM1Xhqz = infoData.get("monthLoopInterval") == null?"":String.valueOf(infoData.get("monthLoopInterval"));
                
                //周、日tabpanel
                Integer tzDQsrq = infoData.get("beginDay1") == ""?0:Integer.valueOf(infoData.get("beginDay1").toString());
                Integer tzDJzrq = infoData.get("endDay1") == ""?0:Integer.valueOf(infoData.get("endDay1").toString());
                String tzDLbqz = infoData.get("day1List") == null?"":String.valueOf(infoData.get("day1List"));
                String tzDXhqz = infoData.get("day1LoopInterval") == null?"":String.valueOf(infoData.get("day1LoopInterval"));
                Integer tzDZdrq = infoData.get("beginDate1") == ""?0:Integer.valueOf(infoData.get("beginDate1").toString());
                Integer tzWQsrq = infoData.get("beginDay2") == ""?0:Integer.valueOf(infoData.get("beginDay2").toString());
                Integer tzWJzrq = infoData.get("endDay2") == ""?0:Integer.valueOf(infoData.get("endDay2").toString());
                String tzWLbqz = infoData.get("day2List") == null?"":String.valueOf(infoData.get("day2List"));
                String tzWXhqz = infoData.get("day2LoopInterval") == null?"":String.valueOf(infoData.get("day2LoopInterval")); 
                Integer tzWZdrq1 = infoData.get("appointedDate1") == ""?0:Integer.valueOf(infoData.get("appointedDate1").toString());
                Integer tzWZdzc = infoData.get("appointedWeek") == ""?0:Integer.valueOf(infoData.get("appointedWeek").toString());
                Integer tzWZdrq2 = infoData.get("appointedDate2") == ""?0:Integer.valueOf(infoData.get("appointedDate2").toString());
                
                //时tabpanel
                Integer tzHQsxs = infoData.get("beginHour") == ""?0:Integer.valueOf(infoData.get("beginHour").toString());
                Integer tzHJzxs = infoData.get("endHour") == ""?0:Integer.valueOf(infoData.get("endHour").toString());
                String tzHLbqz = infoData.get("hourList") == null?"":String.valueOf(infoData.get("hourList"));
                String tzHXhqz = infoData.get("hourLoopInterval") == null?"":String.valueOf(infoData.get("hourLoopInterval"));
                
                //分tabpanel
                Integer tzM2Qsfz = infoData.get("beginMinute") == ""?0:Integer.valueOf(infoData.get("beginMinute").toString());
                Integer tzM2Jzfz = infoData.get("endMinute") == ""?0:Integer.valueOf(infoData.get("endMinute").toString());
                String tzM2Lbqz = infoData.get("minuteList") == null?"":String.valueOf(infoData.get("minuteList"));
                String tzM2Xhqz = infoData.get("minuteLoopInterval") == null?"":String.valueOf(infoData.get("minuteLoopInterval"));
                
                //秒tabpanel
                Integer tzSQsm = infoData.get("beginSecond") == ""?0:Integer.valueOf(infoData.get("beginSecond").toString());
                Integer tzSJzm = infoData.get("endSecond") == ""?0:Integer.valueOf(infoData.get("endSecond").toString());
                String tzSLbqz = infoData.get("secondList") == null?"":String.valueOf(infoData.get("secondList"));
                String tzSXhqz = infoData.get("secondLoopInterval") == null?"":String.valueOf(infoData.get("secondLoopInterval"));
                
                //自定义
                String tzXhzdY = infoData.get("customYear") == null?"":String.valueOf(infoData.get("customYear"));
                String tzXhzdM1 = infoData.get("customMonth") == null?"":String.valueOf(infoData.get("customMonth"));
                String tzXhzdW = infoData.get("customWeek") == null?"":String.valueOf(infoData.get("customWeek"));
                String tzXhzdD = infoData.get("customDay") == null?"":String.valueOf(infoData.get("customDay"));
                String tzXhzdH = infoData.get("customHour") == null?"":String.valueOf(infoData.get("customHour"));
                String tzXhzdM2 = infoData.get("customMinute") == null?"":String.valueOf(infoData.get("customMinute"));
                String tzXhzdS = infoData.get("customSecond") == null?"":String.valueOf(infoData.get("customSecond"));

                //判断数据库中同一机构下循环名称是否存在
                String sql = "select COUNT(1) from TZ_XUNH_DEFN_T WHERE TZ_JG_ID = ? and TZ_XH_MC = ?";
                int count = jdbcTemplate.queryForObject(sql, new Object[]{orgId,loopName},"Integer");
                if(count > 0){
                    errMsg[0] = "1";
                    errMsg[1] = "该机构下循环名称的信息已经存在，请修改循环名称。";
                }else{
                    TzDispatchLoop tzDispatchLoop = new TzDispatchLoop();
                    
                    //插入必填信息
                    tzDispatchLoop.setTzJgId(orgId);
                    tzDispatchLoop.setTzXhMc(loopName);
                    tzDispatchLoop.setTzXhMs(loopDesc);
                    tzDispatchLoop.setTzEeBz(status);
             
                    //每个tabpanel的flag
                    String secondFlag = infoData.get("secondFlag").toString();
                    String minuteFlag = infoData.get("minuteFlag").toString();
                    String hourFlag = infoData.get("hourFlag").toString();
                    String dayFlag = infoData.get("dayFlag").toString();
                    String monthFlag = infoData.get("monthFlag").toString();
                    String weekFlag = infoData.get("weekFlag").toString();
                    String yearFlag = infoData.get("yearFlag").toString();
                    
                    stringBuilder.append(secondFlag).append(minuteFlag).append(hourFlag).
                    append(dayFlag).append(monthFlag).append(weekFlag).append(yearFlag);
                    
                    //插入循环表达式
                    tzDispatchLoop.setTzXhQzbds(stringBuilder.toString().trim());
                    
                    //秒
                    tzDispatchLoop.setTzSQsm(tzSQsm);
                    tzDispatchLoop.setTzSJzm(tzSJzm);
                    tzDispatchLoop.setTzSLbqz(tzSLbqz);
                    tzDispatchLoop.setTzSXhqz(tzSXhqz);
                    
                    //分
                    tzDispatchLoop.setTzM2Qsfz(tzM2Qsfz);
                    tzDispatchLoop.setTzM2Jzfz(tzM2Jzfz);
                    tzDispatchLoop.setTzM2Lbqz(tzM2Lbqz);
                    tzDispatchLoop.setTzM2Xhqz(tzM2Xhqz);
                    
                    //时
                    tzDispatchLoop.setTzHQsxs(tzHQsxs);
                    tzDispatchLoop.setTzHJzxs(tzHJzxs);
                    tzDispatchLoop.setTzHLbqz(tzHLbqz);
                    tzDispatchLoop.setTzHXhqz(tzHXhqz);
                    
                    //插入周、日
                    tzDispatchLoop.setTzDQsrq(tzDQsrq);
                    tzDispatchLoop.setTzDJzrq(tzDJzrq);
                    tzDispatchLoop.setTzDLbqz(tzDLbqz);
                    tzDispatchLoop.setTzDXhqz(tzDXhqz);
                    tzDispatchLoop.setTzDZdrq(tzDZdrq);
                    tzDispatchLoop.setTzWQsrq(tzWQsrq);
                    tzDispatchLoop.setTzWJzrq(tzWJzrq);
                    tzDispatchLoop.setTzWLbqz(tzWLbqz);
                    tzDispatchLoop.setTzWXhqz(tzWXhqz);
                    tzDispatchLoop.setTzWZdrq1(tzWZdrq1);
                    tzDispatchLoop.setTzWZdzc(tzWZdzc);
                    tzDispatchLoop.setTzWZdrq2(tzWZdrq2);
                    
                    //插入月
                    tzDispatchLoop.setTzM1Qsyf(tzM1Qsyf);
                    tzDispatchLoop.setTzM1Jzyf(tzM1Jzyf);
                    tzDispatchLoop.setTzM1Lbqz(tzM1Lbqz);
                    tzDispatchLoop.setTzM1Xhqz(tzM1Xhqz);
                    
                    //插入年
                    tzDispatchLoop.setTzYQsnf(tzYQsnf);
                    tzDispatchLoop.setTzYJznf(tzYJznf);
                    tzDispatchLoop.setTzYLbqz(tzYLbqz);
                    tzDispatchLoop.setTzYXhqz(tzYXhqz);

                    //自定义
                    tzDispatchLoop.setTzXhzdY(tzXhzdY);
                    tzDispatchLoop.setTzXhzdM1(tzXhzdM1);
                    tzDispatchLoop.setTzXhzdW(tzXhzdW);
                    tzDispatchLoop.setTzXhzdD(tzXhzdD);
                    tzDispatchLoop.setTzXhzdH(tzXhzdH);
                    tzDispatchLoop.setTzXhzdM2(tzXhzdM2);
                    tzDispatchLoop.setTzXhzdS(tzXhzdS);
                    
                    //radio和check
                    tzDispatchLoop.setTzYearCheck(infoData.get("yearCheck").toString());
                    tzDispatchLoop.setTzMonthCheck(infoData.get("monthCheck").toString());
                    tzDispatchLoop.setTzDay1Check(infoData.get("day1Check").toString());
                    tzDispatchLoop.setTzDay2Check(infoData.get("day2Check").toString());
                    tzDispatchLoop.setTzHourCheck(infoData.get("hourCheck").toString());
                    tzDispatchLoop.setTzMinuteCheck(infoData.get("minuteCheck").toString());
                    tzDispatchLoop.setTzSecondCheck(infoData.get("secondCheck").toString());
                    
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
        StringBuilder  stringBuilder = new StringBuilder();
        JacksonUtil jacksonUtil = new JacksonUtil();
        int dataLength = actData.length;
        
        try{
            for(int num = 0; num < dataLength; num++){

                // 表单内容;
                String strForm = actData[num];
                // 将字符串转换成json;
                jacksonUtil.json2Map(strForm);

                // 信息内容
                Map<String, Object> infoData = jacksonUtil.getMap("data");
                String orgId = infoData.get("orgId").toString();
                String loopName = infoData.get("loopName").toString();
                String loopDesc = infoData.get("loopDesc").toString();
                String status = infoData.get("status").toString();
                
                //年份tabpanel
                Integer tzYQsnf = infoData.get("beginYear") == ""?0:Integer.valueOf(infoData.get("beginYear").toString());
                Integer tzYJznf = infoData.get("endYear") == ""?0:Integer.valueOf(infoData.get("endYear").toString());
                String tzYLbqz = infoData.get("yearList") == null?"":String.valueOf(infoData.get("yearList"));
                String tzYXhqz = infoData.get("yearLoopInterval") == null?"":String.valueOf(infoData.get("yearLoopInterval"));
                
                //月份tabpanel
                Integer tzM1Qsyf = infoData.get("beginMonth") == ""?0:Integer.valueOf(infoData.get("beginMonth").toString());
                Integer tzM1Jzyf = infoData.get("endMonth") == ""?0:Integer.valueOf(infoData.get("endMonth").toString());
                String tzM1Lbqz = infoData.get("monthList") == null?"":String.valueOf(infoData.get("monthList"));
                String tzM1Xhqz = infoData.get("monthLoopInterval") == null?"":String.valueOf(infoData.get("monthLoopInterval"));
                
                //周、日tabpanel
                Integer tzDQsrq = infoData.get("beginDay1") == ""?0:Integer.valueOf(infoData.get("beginDay1").toString());
                Integer tzDJzrq = infoData.get("endDay1") == ""?0:Integer.valueOf(infoData.get("endDay1").toString());
                String tzDLbqz = infoData.get("day1List") == null?"":String.valueOf(infoData.get("day1List"));
                String tzDXhqz = infoData.get("day1LoopInterval") == null?"":String.valueOf(infoData.get("day1LoopInterval"));
                Integer tzDZdrq = infoData.get("beginDate1") == ""?0:Integer.valueOf(infoData.get("beginDate1").toString());
                Integer tzWQsrq = infoData.get("beginDay2") == ""?0:Integer.valueOf(infoData.get("beginDay2").toString());
                Integer tzWJzrq = infoData.get("endDay2") == ""?0:Integer.valueOf(infoData.get("endDay2").toString());
                String tzWLbqz = infoData.get("day2List") == null?"":String.valueOf(infoData.get("day2List"));
                String tzWXhqz = infoData.get("day2LoopInterval") == null?"":String.valueOf(infoData.get("day2LoopInterval")); 
                Integer tzWZdrq1 = infoData.get("appointedDate1") == ""?0:Integer.valueOf(infoData.get("appointedDate1").toString());
                Integer tzWZdzc = infoData.get("appointedWeek") == ""?0:Integer.valueOf(infoData.get("appointedWeek").toString());
                Integer tzWZdrq2 = infoData.get("appointedDate2") == ""?0:Integer.valueOf(infoData.get("appointedDate2").toString());
                
                //时tabpanel
                Integer tzHQsxs = infoData.get("beginHour") == ""?0:Integer.valueOf(infoData.get("beginHour").toString());
                Integer tzHJzxs = infoData.get("endHour") == ""?0:Integer.valueOf(infoData.get("endHour").toString());
                String tzHLbqz = infoData.get("hourList") == null?"":String.valueOf(infoData.get("hourList"));
                String tzHXhqz = infoData.get("hourLoopInterval") == null?"":String.valueOf(infoData.get("hourLoopInterval"));
                
                //分tabpanel
                Integer tzM2Qsfz = infoData.get("beginMinute") == ""?0:Integer.valueOf(infoData.get("beginMinute").toString());
                Integer tzM2Jzfz = infoData.get("endMinute") == ""?0:Integer.valueOf(infoData.get("endMinute").toString());
                String tzM2Lbqz = infoData.get("minuteList") == null?"":String.valueOf(infoData.get("minuteList"));
                String tzM2Xhqz = infoData.get("minuteLoopInterval") == null?"":String.valueOf(infoData.get("minuteLoopInterval"));
                
                //秒tabpanel
                Integer tzSQsm = infoData.get("beginSecond") == ""?0:Integer.valueOf(infoData.get("beginSecond").toString());
                Integer tzSJzm = infoData.get("endSecond") == ""?0:Integer.valueOf(infoData.get("endSecond").toString());
                String tzSLbqz = infoData.get("secondList") == null?"":String.valueOf(infoData.get("secondList"));
                String tzSXhqz = infoData.get("secondLoopInterval") == null?"":String.valueOf(infoData.get("secondLoopInterval"));
                
                //自定义
                String tzXhzdY = infoData.get("customYear") == null?"":String.valueOf(infoData.get("customYear"));
                String tzXhzdM1 = infoData.get("customMonth") == null?"":String.valueOf(infoData.get("customMonth"));
                String tzXhzdW = infoData.get("customWeek") == null?"":String.valueOf(infoData.get("customWeek"));
                String tzXhzdD = infoData.get("customDay") == null?"":String.valueOf(infoData.get("customDay"));
                String tzXhzdH = infoData.get("customHour") == null?"":String.valueOf(infoData.get("customHour"));
                String tzXhzdM2 = infoData.get("customMinute") == null?"":String.valueOf(infoData.get("customMinute"));
                String tzXhzdS = infoData.get("customSecond") == null?"":String.valueOf(infoData.get("customSecond"));
                
                //每个tabpanel的flag
                String secondFlag = infoData.get("secondFlag").toString();
                String minuteFlag = infoData.get("minuteFlag").toString();
                String hourFlag = infoData.get("hourFlag").toString();
                String dayFlag = infoData.get("dayFlag").toString();
                String monthFlag = infoData.get("monthFlag").toString();
                String weekFlag = infoData.get("weekFlag").toString();
                String yearFlag = infoData.get("yearFlag").toString();

                TzDispatchLoop tzDispatchLoop = new TzDispatchLoop();
                
                stringBuilder.append(secondFlag).append(minuteFlag).append(hourFlag).
                append(dayFlag).append(monthFlag).append(weekFlag).append(yearFlag);
                
                //插入循环表达式
                tzDispatchLoop.setTzXhQzbds(stringBuilder.toString().trim());
                
                //主动form
                tzDispatchLoop.setTzJgId(orgId);
                tzDispatchLoop.setTzXhMc(loopName);
                tzDispatchLoop.setTzXhMs(loopDesc);
                tzDispatchLoop.setTzEeBz(status);
                
                //秒
                tzDispatchLoop.setTzSQsm(tzSQsm);
                tzDispatchLoop.setTzSJzm(tzSJzm);
                tzDispatchLoop.setTzSLbqz(tzSLbqz);
                tzDispatchLoop.setTzSXhqz(tzSXhqz);
                
                //分
                tzDispatchLoop.setTzM2Qsfz(tzM2Qsfz);
                tzDispatchLoop.setTzM2Jzfz(tzM2Jzfz);
                tzDispatchLoop.setTzM2Lbqz(tzM2Lbqz);
                tzDispatchLoop.setTzM2Xhqz(tzM2Xhqz);
                
                //时
                tzDispatchLoop.setTzHQsxs(tzHQsxs);
                tzDispatchLoop.setTzHJzxs(tzHJzxs);
                tzDispatchLoop.setTzHLbqz(tzHLbqz);
                tzDispatchLoop.setTzHXhqz(tzHXhqz);
                
                //插入周、日
                tzDispatchLoop.setTzDQsrq(tzDQsrq);
                tzDispatchLoop.setTzDJzrq(tzDJzrq);
                tzDispatchLoop.setTzDLbqz(tzDLbqz);
                tzDispatchLoop.setTzDXhqz(tzDXhqz);
                tzDispatchLoop.setTzDZdrq(tzDZdrq);
                tzDispatchLoop.setTzWQsrq(tzWQsrq);
                tzDispatchLoop.setTzWJzrq(tzWJzrq);
                tzDispatchLoop.setTzWLbqz(tzWLbqz);
                tzDispatchLoop.setTzWXhqz(tzWXhqz);
                tzDispatchLoop.setTzWZdrq1(tzWZdrq1);
                tzDispatchLoop.setTzWZdzc(tzWZdzc);
                tzDispatchLoop.setTzWZdrq2(tzWZdrq2);
                
                //插入月
                tzDispatchLoop.setTzM1Qsyf(tzM1Qsyf);
                tzDispatchLoop.setTzM1Jzyf(tzM1Jzyf);
                tzDispatchLoop.setTzM1Lbqz(tzM1Lbqz);
                tzDispatchLoop.setTzM1Xhqz(tzM1Xhqz);
                
                //插入年
                tzDispatchLoop.setTzYQsnf(tzYQsnf);
                tzDispatchLoop.setTzYJznf(tzYJznf);
                tzDispatchLoop.setTzYLbqz(tzYLbqz);
                tzDispatchLoop.setTzYXhqz(tzYXhqz);

                //自定义
                tzDispatchLoop.setTzXhzdY(tzXhzdY);
                tzDispatchLoop.setTzXhzdM1(tzXhzdM1);
                tzDispatchLoop.setTzXhzdW(tzXhzdW);
                tzDispatchLoop.setTzXhzdD(tzXhzdD);
                tzDispatchLoop.setTzXhzdH(tzXhzdH);
                tzDispatchLoop.setTzXhzdM2(tzXhzdM2);
                tzDispatchLoop.setTzXhzdS(tzXhzdS);
                
                //radio和check
                tzDispatchLoop.setTzYearCheck(infoData.get("yearCheck").toString());
                tzDispatchLoop.setTzMonthCheck(infoData.get("monthCheck").toString());
                tzDispatchLoop.setTzDay1Check(infoData.get("day1Check").toString());
                tzDispatchLoop.setTzDay2Check(infoData.get("day2Check").toString());
                tzDispatchLoop.setTzHourCheck(infoData.get("hourCheck").toString());
                tzDispatchLoop.setTzMinuteCheck(infoData.get("minuteCheck").toString());
                tzDispatchLoop.setTzSecondCheck(infoData.get("secondCheck").toString());
                
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
