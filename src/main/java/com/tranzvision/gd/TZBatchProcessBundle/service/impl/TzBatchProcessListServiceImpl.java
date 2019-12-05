package com.tranzvision.gd.TZBatchProcessBundle.service.impl;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBatchBundle.dao.TzAqJcddTMapper;
import com.tranzvision.gd.TZBatchBundle.model.TzAqJcddT;
import com.tranzvision.gd.TZBatchProcessBundle.dao.TzBatchProcessMapper;
import com.tranzvision.gd.TZBatchProcessBundle.model.TzBatchProcess;
import com.tranzvision.gd.TZBatchProcessBundle.model.TzBatchProcessKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by WangDi on 2017/4/7.
 */
@Service("com.tranzvision.gd.TZBatchProcessBundle.service.impl.TzBatchProcessListServiceImpl")
public class TzBatchProcessListServiceImpl extends FrameworkImpl {

    @Autowired
    private FliterForm fliterForm;

    @Autowired
    private SqlQuery jdbcTemplate;

    @Autowired
    private TzBatchProcessMapper tzBatchProcessMapper;
    
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private TzAqJcddTMapper tzAqJcddTMapper;

    // 进程定义信息
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

                String processDesc = "", platFormType = "",comId = "",className = "",remark = "";

                String sql = "SELECT A.TZ_JG_ID,A.TZ_JC_MC,A.TZ_JC_MS,A.TZ_YXPT_LX,A.TZ_ZCZJ_ID,A.TZ_JAVA_CLASS,A.TZ_BEIZHU FROM TZ_JINC_DY_T A WHERE A.TZ_JG_ID=? \n" +
                        "AND A.TZ_JC_MC=?";
                Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { orgId,processName });
                if (map != null) {
                    processDesc = (String) map.get("TZ_JC_MS");
                    platFormType = (String) map.get("TZ_YXPT_LX");
                    comId = (String) map.get("TZ_ZCZJ_ID");
                    className = map.get("TZ_JAVA_CLASS") == null?"":map.get("TZ_JAVA_CLASS").toString();
                    remark = map.get("TZ_BEIZHU") == null?"":map.get("TZ_BEIZHU").toString();
                    Map<String, Object> hMap = new HashMap<>();
                    hMap.put("orgId", orgId);
                    hMap.put("processName", processName);
                    hMap.put("processDesc", processDesc);
                    //开始   suzl 平台类型取值判断修改
                	if("WINDOWS".equals(platFormType)){
                		platFormType = "1";
                	}
                	if("UNIX".equals(platFormType)){
                		platFormType = "2";
                	}
                	if("其他".equals(platFormType)){
                		platFormType = "3";
                	}
                	//结束
                    hMap.put("runPlatType", platFormType);
                    hMap.put("ComID", comId);
                    hMap.put("className", className);
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
            String[][] orderByArr = new String[][] { new String[] { "TZ_JC_MC", "DESC" }};

            // json数据要的结果字段;
            String[] resultFldArray = { "TZ_JG_ID","TZ_JC_MC","TZ_JC_MS","TZ_YXPT_LX"};

            // 可配置搜索通用函数;
            Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

            if (obj != null && obj.length > 0) {

                ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
                list.forEach(resultArray ->{
                    Map<String, Object> mapList = new HashMap<String, Object>();
                    mapList.put("orgId", resultArray[0]);
                    mapList.put("processName", resultArray[1]);
                    mapList.put("processDesc", resultArray[2]);
                    mapList.put("platformType", resultArray[3]);
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
     * 新增进程定义
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
                //开始 suzl 平台类型取值判断修改
                if("1".equals(platType)){
                	platType = "WINDOWS";
                }
                if("2".equals(platType)){
                	platType = "UNIX";
                }
                if("3".equals(platType)){
                	platType = "其他";
                }
                //结束
                String comID = jacksonUtil.getString("ComID");
                String processName = jacksonUtil.getString("processName");
                String processDec = jacksonUtil.getString("processDesc");
                String className = jacksonUtil.getString("className");
                String remark = jacksonUtil.getString("remark") == null?"":jacksonUtil.getString("remark");
                String sql = "SELECT 'Y' FROM TZ_JINC_DY_T A WHERE A.TZ_JG_ID=? AND A.TZ_JC_MC=?";
                String isExist =  jdbcTemplate.queryForObject(sql, new Object[]{orgId,processName},"String");
                if("Y".equals(isExist)){
                    errMsg[0] = "1";
                    errMsg[1] = "当前机构已存在相同的进程名称!";
                    return strRet;
                }else {
                    TzBatchProcess tzBatchProcess = new TzBatchProcess();
                    tzBatchProcess.setTzBeizhu(remark);
                    tzBatchProcess.setTzJgId(orgId);
                    tzBatchProcess.setTzJcMc(processName);
                    tzBatchProcess.setTzJcMs(processDec);
                    tzBatchProcess.setTzJavaClass(className);
                    tzBatchProcess.setTzZczjId(comID);
                    tzBatchProcess.setTzYxptLx(platType);
                    tzBatchProcessMapper.insertSelective(tzBatchProcess);
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
     * 编辑进程定义
     */
    @Override
    @Transactional
    public String tzUpdate(String[] actData, String[] errMsg) {

        String strRet = "{}";
        String CLASSID = "";
        JacksonUtil jacksonUtil = new JacksonUtil();
        int dataLength = actData.length;
        try{
            for(int num = 0; num < dataLength; num++){
            	
                // 表单内容;
                String strForm = actData[num];
                // 将字符串转换成json;
                jacksonUtil.json2Map(strForm);
                String temp = "";
                // 信息内容;

                String currentAccountId = tzLoginServiceImpl.getLoginedManagerDlzhid(request);
                String orgId = jacksonUtil.getString("orgId");
                String platType = jacksonUtil.getString("runPlatType");
                //开始 suzl 平台类型取值判断修改
                if("1".equals(platType)){
                	platType = "WINDOWS";
                }
                if("2".equals(platType)){
                	platType = "UNIX";
                }
                if("3".equals(platType)){
                	platType = "其他";
                }
                //结束
                String comID = jacksonUtil.getString("ComID");
                String processName = jacksonUtil.getString("processName");
                String processDec = jacksonUtil.getString("processDesc");
                String className = jacksonUtil.getString("className");
                String remark = jacksonUtil.getString("remark") == null?"":jacksonUtil.getString("remark");
                String isDispatch = jacksonUtil.getString("isDispatch") == null?"":jacksonUtil.getString("isDispatch");
                
            	
//            	获取许可权编号
            	if(num == 0) {
            		
            		CLASSID = jacksonUtil.getString("classId");
            	}
                //过滤非许可权下的操作
                if(!"".equals(isDispatch)) {
                	
                	if("true".equals(isDispatch)) {
                		
                		temp = "1";
                	}else {
                		temp = "0";
                	}
                	insertData2Permission(CLASSID,comID,new String[] {orgId,processName,temp,currentAccountId});
                }
                
                
                TzBatchProcess tzBatchProcess = new TzBatchProcess();
                tzBatchProcess.setTzBeizhu(remark);
                tzBatchProcess.setTzJgId(orgId);
                tzBatchProcess.setTzJcMc(processName);
                tzBatchProcess.setTzJcMs(processDec);
                tzBatchProcess.setTzJavaClass(className);
                tzBatchProcess.setTzZczjId(comID);
                tzBatchProcess.setTzYxptLx(platType);
                tzBatchProcess.setTzIsDispatch(temp);
                tzBatchProcessMapper.updateByPrimaryKeySelective(tzBatchProcess);
            }
        }catch(Exception e){
            e.printStackTrace();
            errMsg[0] = "1";
            errMsg[1] = e.toString();
        }
        return strRet;
    }
    
    //获取开启进程实例权限
    private void insertData2Permission(String CLASSID,String comId,String[] args) {
    	
    	String isExist = "";
    	String existSql = "SELECT 'Y' FROM TZ_AQ_JCDD_T WHERE CLASSID = ? AND TZ_COM_ID = ? AND TZ_JG_ID = ? AND TZ_JC_MC = ?";
    	
    	TzAqJcddT tzAqJcddT = new TzAqJcddT();
    	tzAqJcddT.setClassid(CLASSID);
    	tzAqJcddT.setTzComId(comId);
    	tzAqJcddT.setTzJgId(args[0]);
    	tzAqJcddT.setTzJcMc(args[1]);
    	tzAqJcddT.setTzDdFlg(args[2]);
    	tzAqJcddT.setTzTjr(args[3]);
    	tzAqJcddT.setTzZhxgr(args[3]);
    	tzAqJcddT.setTzTjsj(Calendar.getInstance().getTime());
    	tzAqJcddT.setTzZhxgsj(Calendar.getInstance().getTime());
    	isExist = jdbcTemplate.queryForObject(existSql, new String[] {CLASSID,comId,args[0],args[1]}, "String");

    	if("Y".equals(isExist)) {
    		tzAqJcddTMapper.updateByPrimaryKeySelective(tzAqJcddT);
    	}else {
    		tzAqJcddTMapper.insertSelective(tzAqJcddT);
    	}
    		
    }

    @Override
	/* 删除进程 */
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
                TzBatchProcessKey tzBatchProcessKey = new TzBatchProcessKey();
                tzBatchProcessKey.setTzJgId(orgId);
                tzBatchProcessKey.setTzJcMc(processName);
                tzBatchProcessMapper.deleteByPrimaryKey(tzBatchProcessKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errMsg[0] = "1";
            errMsg[1] = e.toString();
        }
        return strRet;
    }
}
