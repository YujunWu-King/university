package com.tranzvision.gd.WorkflowActionsBundle.service.impl;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.dynamicsBase.AnalysisDynaRole;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.workflow.base.TzStpActInfo;
import com.tranzvision.gd.workflow.engine.TzWorkflow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: ZY
 * @Date: 2019/1/24 10:51
 * @Description: 前台工作流业务提交时动态分支及责任人角色
 */
@Service("com.tranzvision.gd.WorkflowActionsBundle.service.impl.SubmitStepsImpl")
public class SubmitStepsImpl extends FrameworkImpl {

    @Autowired
    private SqlQuery jdbcTemplate;
    @Autowired
    private TzLoginServiceImpl tzLoginServiceImpl;
    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 查询可选动作分支，以及动作之后事件责任人角色查询责任人;(废弃，改为下面querySubmitSteps方法)
     * @param strParams
     * @param errorMsg
     * @return
     */
    @Override
    public String tzQuery(String strParams, String[] errorMsg) {
        //当前登录人
        String oprid = tzLoginServiceImpl.getLoginedManagerOprid(httpServletRequest);
        String sql = "Select tzms_user_uniqueid from tzms_tea_defn_t where tzms_oprid = ?";
        String tzms_user_uniqueid = jdbcTemplate.queryForObject(sql,new Object[]{oprid},"String");

        // 返回值;
        Map<String, Object> mapRet = new HashMap<String, Object>();
        mapRet.put("total", 0);
        List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
        mapRet.put("root", listData);
        JacksonUtil jacksonUtil = new JacksonUtil();
        try {
            jacksonUtil.json2Map(strParams);
            if(jacksonUtil.containsKey("tzms_wflinsid")&&jacksonUtil.containsKey("tzms_stpinsid")){
                //获取步骤实例Id
                // 实例ID;
                String tzms_wflinsid = jacksonUtil.getString("tzms_wflinsid");
                // 步骤实例id;
                String tzms_stpinsid = jacksonUtil.getString("tzms_stpinsid");
                //查可用动作:动作实例id，动作id，下一步步骤id
                String  sql1 =  "SELECT tzms_actinsid,tzms_actclsid,tzms_nextstpid FROM tzms_actins_tbl WHERE tzms_wflinsid = ? and tzms_stpinsid = ?";
                listData = jdbcTemplate.queryForList(sql1,new Object[]{tzms_wflinsid,tzms_stpinsid});
                for (int i = 0; i < listData.size(); i++) {
                    Map<String,Object> map = listData.get(i);
                    //动作id,获取动作名称
                    String tzms_actclsid = map.get("tzms_actclsid")==null?"": String.valueOf(map.get("tzms_actclsid"));
                    String sql2 = "SELECT tzms_actclsname FROM tzms_wf_actcls_t WHERE tzms_wf_actcls_tid = ?";
                    String tzms_actclsname = jdbcTemplate.queryForObject(sql2,new Object[]{tzms_actclsid},"String");
                    map.put("tzms_actclsname",tzms_actclsname);
                    //下一步步骤id ,获取责任人角色
                    String tzms_nextstpid = map.get("tzms_nextstpid")==null?"": String.valueOf(map.get("tzms_nextstpid"));
                    String sql3 = "SELECT tzms_wfrole_uniqueid FROM tzms_wflstp_t WHERE tzms_wflstp_tid = ?";
                    String tzms_wfrole_uniqueid = jdbcTemplate.queryForObject(sql3,new Object[]{tzms_nextstpid},"String");
                    AnalysisDynaRole analysisDynaRole = new AnalysisDynaRole ();
                    List<String> userIds= analysisDynaRole.getUserIds(tzms_wflinsid,tzms_stpinsid,tzms_user_uniqueid,tzms_wfrole_uniqueid);
                    //String[] userIdArr = userIds.split(",");
                    //获取责任人信息列表
                    List<Map<String,Object>> listInfo = new ArrayList<>();
                    String sql4  = "Select TOP 1  A.tzms_domain_zhid,A.tzms_staff_id, A.tzms_name,C.tzms_org_name from tzms_tea_defn_tBase A,tzms_org_user_tBase B,tzms_org_structure_tree_tbase C where A.tzms_tea_defn_tid = B.tzms_org_user_tid AND B.tzms_org_uniqueid = C.tzms_org_structure_tree_tid AND A.tzms_user_uniqueid = ?";
                    for (int j = 0; j < userIds.size(); j++) {
                        Map<String,Object> mapInfo = jdbcTemplate.queryForMap(sql4,new Object[]{userIds.get(j)});
                        //添加责任人id
                        mapInfo.put("userId",userIds.get(j));
                        listInfo.add(mapInfo);
                    }
                    map.put("userList",listInfo);
                }
                mapRet.put("root", listData);
                mapRet.put("total", listData.size());
                return jacksonUtil.Map2json(mapRet);
            }
        } catch (Exception e) {
            errorMsg[0] = "1";
            errorMsg[1] = e.toString();
        }

        return jacksonUtil.Map2json(mapRet);
    }


    @Override
    public String tzOther(String oprType, String strParams, String[] errorMsg) {
        String strRet = "";
        try{
            //当前登录的机构;
            //String orgID = tzLoginServiceImpl.getLoginedManagerOrgid(request);
            if("querySubmitSteps".equals(oprType)){
                strRet = this.querySubmitSteps(strParams,errorMsg);
            }else if("queryProcessInput".equals(oprType)){
                strRet = this.queryProcessInput(strParams,errorMsg);
            }
        }catch(Exception e){
            e.printStackTrace();
            errorMsg[0] = "1";
            errorMsg[1] = e.toString();
        }

        return strRet;
    }


    /**
     * 查询是否显示处理意见
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String queryProcessInput(String strParams,String[] errorMsg){
        JacksonUtil jacksonUtil = new JacksonUtil();
        Map<String, Object> mapRet = new HashMap<String, Object>();
        String flag = "";
        try {
            jacksonUtil.json2Map(strParams);
            // 实例ID;
            String tzms_wflinsid = jacksonUtil.getString("tzms_wflinsid");
            // 步骤实例id;
            String tzms_stpinsid = jacksonUtil.getString("tzms_stpinsid");
            String sql = "SELECT A.TZms_DPYPRCFLG1 FROM tzms_wflstp_t A,tzms_stpins_tbl B WHERE B.tzms_wflinsid = ? AND B.tzms_stpinsid = ? AND B.tzms_wflstpid = A.tzms_wflstp_tid";
            flag = jdbcTemplate.queryForObject(sql,new Object[]{tzms_wflinsid,tzms_stpinsid},"String");
            if(flag == null || "0".equals(flag)){
                mapRet.put("processInput",false);
            }else if("1".equals(flag)){
                mapRet.put("processInput",true);
            }else {
                mapRet.put("processInput",false);
            }
        }catch (Exception e){
            mapRet.put("processInput",false);
            e.printStackTrace();
            errorMsg[0] = "1";
            errorMsg[1] = e.toString();
        }
        return jacksonUtil.Map2json(mapRet);
    }

    /**
     * 查询提交下部流转路径 调用张浪接口
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String querySubmitSteps(String strParams, String[] errorMsg){
        // 返回值;
        Map<String, Object> mapRet = new HashMap<String, Object>();
        JacksonUtil jacksonUtil = new JacksonUtil();
        try {
            jacksonUtil.json2Map(strParams);
            // 实例ID;
            String tzms_wflinsid = jacksonUtil.getString("tzms_wflinsid");
            // 步骤实例id;
            String tzms_stpinsid = jacksonUtil.getString("tzms_stpinsid");
            // dynamics用户id;
            String  userId = jacksonUtil.getString("userId");
            
            
            //是否启用处理意见
            String sql = "SELECT A.TZms_DPYPRCFLG1 FROM tzms_wflstp_t A,tzms_stpins_tbl B WHERE B.tzms_wflinsid=? AND B.tzms_stpinsid=? AND B.tzms_wflstpid=A.tzms_wflstp_tid";
            Map<String, Object> stpMap = jdbcTemplate.queryForMap(sql, new Object[]{ tzms_wflinsid, tzms_stpinsid });
            if(stpMap != null) {
            	boolean useClyj = stpMap.get("TZms_DPYPRCFLG1") == null ? false : (boolean) stpMap.get("TZms_DPYPRCFLG1");
            	mapRet.put("viewClyj", useClyj);
            }
            
            TzWorkflow workflowInstance = new TzWorkflow(userId);
            try {
                //流程初始化
               boolean createOK = workflowInstance.CreateWorkflow1(tzms_wflinsid, tzms_stpinsid);
               if(createOK) {
	               //错误信息
	                String ErrorMsg = "";
	                boolean isOnlyPath = false;
	                try {
	                    //判断路径是否唯一
	                    isOnlyPath = workflowInstance.NextActionIsOnlyPath();
	                } catch (TzException e) {
	                    ErrorMsg = e.getMessage();
	                    mapRet.put("ret",1);
	                    mapRet.put("msg",ErrorMsg);
	                    return jacksonUtil.Map2json(mapRet);
	                }
	
	                if(isOnlyPath == true){
	                    //路径唯一
	                    mapRet.put("ret",0);
	                }else{
	                    //路径不唯一，获取动作路径、责任人列表
	                    /**
	                     * TzStpActInfo 对象存储了动作编号、动作名称、动作责任人等信息
	                     */
	                    List<TzStpActInfo> actionPathList = workflowInstance.getNextActionAndUserList();
	
	                    List<Map<String,Object>> listdata = new ArrayList<>();
	                    for (int i = 0; i < actionPathList.size(); i++) {
	                        String tzms_actclsid = actionPathList.get(i).getM_ActClsId();
	                        String tzms_actclsname = actionPathList.get(i).getM_ActClsName();
	                        List<String> userList = actionPathList.get(i).getM_NextUserList();
	                        
	                        Map<String,Object> map = new HashMap<>();
	                        map.put("tzms_actclsid",tzms_actclsid);
	                        map.put("tzms_actclsname",tzms_actclsname);
	                        
	                        //获取责任人信息列表
	                        List<Map<String,Object>> listInfo = new ArrayList<>();
	                        String sql1  = "SELECT TOP 1 tzms_domain_zhid,tzms_staff_id,tzms_name FROM tzms_tea_defn_tBase WHERE tzms_user_uniqueid = ?";
	                        String sql2  = "Select TOP 1  C.tzms_org_name from tzms_tea_defn_tBase A,tzms_org_user_tBase B,tzms_org_structure_tree_tbase C where A.tzms_tea_defn_tid = B.tzms_tea_defnid AND B.tzms_org_uniqueid = C.tzms_org_structure_tree_tid AND A.tzms_user_uniqueid = ?";
	                        
	                        for (int j = 0; j < userList.size(); j++) {
	                            String tzms_user_uniqueid = userList.get(j);
	                            tzms_user_uniqueid = tzms_user_uniqueid == null ?"":tzms_user_uniqueid;
	                            
	                            Map<String,Object> mapInfo = jdbcTemplate.queryForMap(sql1,new Object[]{tzms_user_uniqueid});
	                            if(mapInfo!=null){
	                                String tzms_org_name = jdbcTemplate.queryForObject(sql2,new Object[]{tzms_user_uniqueid},"String");
	                                mapInfo.put("tzms_org_name",tzms_org_name == null ? "":tzms_org_name);
	                                mapInfo.put("tzms_user_uniqueid",tzms_user_uniqueid);
	                                listInfo.add(mapInfo);
	                            }
	                        }
	                        map.put("userList",listInfo);
	                        listdata.add(map);
	                    }
	                    mapRet.put("root",listdata);
	                    mapRet.put("total",listdata.size());
	                    mapRet.put("ret",0);
	                }
               }
            } catch (TzException e) {
                mapRet.put("ret",1);
                mapRet.put("msg",e.toString());
            }

            return jacksonUtil.Map2json(mapRet);
        } catch (Exception e) {
            e.printStackTrace();
            mapRet.put("ret",1);
            mapRet.put("msg",e.toString());
            return jacksonUtil.Map2json(mapRet);
        }
    }
}
