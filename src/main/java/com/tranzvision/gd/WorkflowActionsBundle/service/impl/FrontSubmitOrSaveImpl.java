package com.tranzvision.gd.WorkflowActionsBundle.service.impl;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.workflow.base.ErrorMessage;
import com.tranzvision.gd.workflow.base.TzStpActInfo;
import com.tranzvision.gd.workflow.engine.TzWorkflow;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: ZY
 * @Date: 2019/1/25 09:23
 * @Description:   前台 提交，同意，转发，退回重填，退回某步，拒绝，撤回。撤销 中转调用张浪接口实现
 */
@Service("com.tranzvision.gd.WorkflowActionsBundle.service.impl.FrontSubmitOrSaveImpl")
public class FrontSubmitOrSaveImpl extends FrameworkImpl {
    

    @Override
    public String tzOther(String oprType, String strParams, String[] errorMsg) {
        String strRet = "";
        try{
            //当前登录的机构;
            //String orgID = tzLoginServiceImpl.getLoginedManagerOrgid(request);
            if("submitForward".equals(oprType)){
                //提交转发
                strRet = this.submitForward(strParams,errorMsg);
            }else if("submitReturnFill".equals(oprType)){
                //提交退回重填
                strRet = this.submitReturnFill(strParams,errorMsg);
            }else if("submitReturnStep".equals(oprType)){
                //提交退回某步
                strRet = this.submitReturnStep(strParams,errorMsg);
            }else if("submitRefuse".equals(oprType)){
                //提交退回某步
                strRet = this.submitRefuse(strParams,errorMsg);
            }else if("submitWithdraw".equals(oprType)){
                //撤回
                strRet = this.submitWithdraw(strParams,errorMsg);
            }else if("submitCancel".equals(oprType)){
                //撤销
                strRet = this.submitCancel(strParams,errorMsg);
            }else if("submitSubmitForm".equals(oprType)){
                //提交
                strRet = this.submitSubmitForm(strParams,errorMsg);
            }else if("submitAgreeForm".equals(oprType)){
                //同意
                strRet = this.submitAgreeForm(strParams,errorMsg);
            }else if("readOver".equals(oprType)){
                //已阅
                strRet = this.readOver(strParams,errorMsg);
            }
        }catch(Exception e){
            e.printStackTrace();
            errorMsg[0] = "1";
            errorMsg[1] = e.toString();
        }

        return strRet;
    }

    /**
     * 提交
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String submitSubmitForm(String strParams, String[] errorMsg){
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
            // 提交意见;
            String  content = jacksonUtil.getString("content");
            //获取下一步路径步骤id
            String tzms_actinsid = jacksonUtil.getString("tzms_actinsid");
            //获取下一步责任人字符串  逗号 ,隔开
            String userStr = jacksonUtil.getString("userStr");

            if(tzms_wflinsid!=null&&!"".equals(tzms_wflinsid)&&tzms_stpinsid!=null&&!"".equals(tzms_stpinsid)){
                String [] userStrs = userStr.split(",");
                List<String> m_NextUserList = new ArrayList<>();
                if(tzms_actinsid!=null&&!"".equals(tzms_actinsid)&&userStr!=null&&!"".equals(userStr)){
                    for (int i = 0; i < userStrs.length; i++) {
                        m_NextUserList.add(userStrs[i]);
                    }
                }
                TzStpActInfo tzStpActInfo = new TzStpActInfo();
                //调用张浪接口实现
                TzWorkflow workflowInstance = new TzWorkflow(userId);
                boolean createOK;
                try {
                    //流程初始化
                    System.out.println("tzms_wflinsid====================:"+tzms_wflinsid);
                    System.out.println("tzms_stpinsid====================:"+tzms_stpinsid);
                    createOK = workflowInstance.CreateWorkflow1(tzms_wflinsid, tzms_stpinsid);
                } catch (TzException e) {
                    createOK = false;
                    mapRet.put("ret",1);
                    mapRet.put("msg",e.toString());
                }

                if(createOK == true){
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
                    }else{
                        //路径不唯一，获取动作路径、责任人列表
                        /**
                         * TzStpActInfo 对象存储了动作编号、动作名称、动作责任人等信息
                         */
                        List<TzStpActInfo> actionPathList = workflowInstance.getNextActionAndUserList();
                        for (int i = 0; i < actionPathList.size(); i++) {
                            if(actionPathList.get(i).getM_ActClsId().equals(tzms_actinsid)){
                                tzStpActInfo = actionPathList.get(i);
                                tzStpActInfo.setM_NextUserList(m_NextUserList);
                                tzStpActInfo.setM_IsEndAction(false);
                                break;
                            }
                        }
                        
                        workflowInstance.SetWorkflowActionPathInfo(tzStpActInfo);
                        /******设置选择的动作路径信息***结束***/
                    }
                    //调用张浪接口待实现
                    //String ErrorMsg_OUT = "";
                    ErrorMessage ErrorMsg_OUT = new ErrorMessage();
                    //工作流提交
                    System.out.println("content====================:"+content);
                    boolean submitOK = workflowInstance.WorkflowSubmit(content, ErrorMsg_OUT);
                    System.out.println("submitOK====================:"+submitOK);
                    if(submitOK == true){
                        //提交成功
                        mapRet.put("ret",0);
                        mapRet.put("msg","提交成功");
                    }else{
                        //提交失败
                        mapRet.put("ret",1);
                        mapRet.put("msg",ErrorMsg_OUT.getErrorMsg());
                    }

                }else {
                    mapRet.put("ret",1);
                    mapRet.put("msg","流程初始化失败");
                }
            }else {
            	mapRet.put("ret",1);
                mapRet.put("msg","参数错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg[0] = "1";
            errorMsg[1] = e.toString();
            mapRet.put("ret",1);
            mapRet.put("msg",e.toString());
            return jacksonUtil.Map2json(mapRet);
        }

        return jacksonUtil.Map2json(mapRet);
    }


    /**
     * 同意
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String submitAgreeForm(String strParams, String[] errorMsg){
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
            // 提交意见;
            String  content = jacksonUtil.getString("content");
            //获取下一步路径步骤id
            String tzms_actinsid = jacksonUtil.getString("tzms_actinsid");
            //获取下一步责任人字符串  逗号 ,隔开
            String userStr = jacksonUtil.getString("userStr");

            if(tzms_wflinsid!=null&&!"".equals(tzms_wflinsid)&&tzms_stpinsid!=null&&!"".equals(tzms_stpinsid)){
                String [] userStrs = userStr.split(",");
                List<String> m_NextUserList = new ArrayList<>();
                if(tzms_actinsid!=null&&!"".equals(tzms_actinsid)&&userStr!=null&&!"".equals(userStr)){
                    for (int i = 0; i < userStrs.length; i++) {
                        m_NextUserList.add(userStrs[i]);
                    }
                }
                TzStpActInfo tzStpActInfo = new TzStpActInfo();
                
                TzWorkflow workflowInstance = new TzWorkflow(userId);
                boolean createOK;
                try {
                    //流程初始化
                    createOK = workflowInstance.CreateWorkflow1(tzms_wflinsid, tzms_stpinsid);
                } catch (TzException e) {
                    createOK = false;
                    mapRet.put("ret",1);
                    mapRet.put("msg",e.toString());
                }

                if(createOK == true){
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
                    }else{
                        //路径不唯一，获取动作路径、责任人列表
                        /**
                         * TzStpActInfo 对象存储了动作编号、动作名称、动作责任人等信息
                         */
                        List<TzStpActInfo> actionPathList = workflowInstance.getNextActionAndUserList();
                        for (int i = 0; i < actionPathList.size(); i++) {
                            if(actionPathList.get(i).getM_ActClsId().equals(tzms_actinsid)){
                                tzStpActInfo = actionPathList.get(i);
                                tzStpActInfo.setM_NextUserList(m_NextUserList);
                                tzStpActInfo.setM_IsEndAction(false);
                                break;
                            }
                        }

                        workflowInstance.SetWorkflowActionPathInfo(tzStpActInfo);
                        /******设置选择的动作路径信息***结束***/
                    }
                    //调用张浪同意接口待实现
                    //String ErrorMsg_OUT = "";
                    ErrorMessage ErrorMsg_OUT = new ErrorMessage();
                    


                    //工作流同意
                    boolean agreeOK  = workflowInstance.WorkflowAgree(content, ErrorMsg_OUT);

                    if(agreeOK  == true){
                        //同意成功
                        mapRet.put("ret",0);
                        mapRet.put("msg","同意成功");
                    }else{
                        //同意失败
                        mapRet.put("ret",1);
                        mapRet.put("msg",ErrorMsg_OUT.getErrorMsg());
                    }
                }else {
                    mapRet.put("ret",1);
                    mapRet.put("msg","流程初始化失败");
                }
            }else {
            	mapRet.put("ret",1);
                mapRet.put("msg","参数错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg[0] = "1";
            errorMsg[1] = e.toString();
            mapRet.put("ret",1);
            mapRet.put("msg",e.toString());
            return jacksonUtil.Map2json(mapRet);
        }

        return jacksonUtil.Map2json(mapRet);
    }


    /**
     * 转发
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String submitForward(String strParams, String[] errorMsg){
        // 返回值;
        Map<String, Object> mapRet = new HashMap<String, Object>();
        JacksonUtil jacksonUtil = new JacksonUtil();
        try {
            jacksonUtil.json2Map(strParams);
            // 实例ID;
            String tzms_wflinsid = jacksonUtil.getString("tzms_wflinsid");
            // 步骤实例id;
            String tzms_stpinsid = jacksonUtil.getString("tzms_stpinsid");
            //获取用户id
            String tzms_stpproid = jacksonUtil.getString("tzms_stpproid");
            // dynamics用户id;
            String  userId = jacksonUtil.getString("userId");

            if(tzms_wflinsid!=null&&!"".equals(tzms_wflinsid)&&tzms_stpinsid!=null&&!"".equals(tzms_stpinsid)&&userId!=null&&!"".equals(userId)) {
                //调用张浪接口实现
                TzWorkflow workflowInstance = new TzWorkflow(userId);
                boolean createOK;
                try {
                    //流程初始化
                    createOK = workflowInstance.CreateWorkflow1(tzms_wflinsid, tzms_stpinsid);
                } catch (TzException e) {
                    createOK = false;
                    mapRet.put("ret",1);
                    mapRet.put("msg",e.toString());
                }

                if(createOK == true){
                    //错误信息
                    String ErrorMsg = "";
                    boolean transferOK;
                    try {
                        //工作流转发
                        transferOK = workflowInstance.WorkflowTransfer(tzms_stpproid);
                    } catch (TzException e) {
                        transferOK = false;
                        ErrorMsg = e.getMessage();
                    }

                    if(transferOK == true){
                        //转发成功
                        mapRet.put("ret",0);
                        mapRet.put("msg","转发成功");
                    }else{
                        //转发失败
                        mapRet.put("ret",1);
                        mapRet.put("msg",ErrorMsg);
                    }
                }else {
                    mapRet.put("ret",1);
                    mapRet.put("msg","流程初始化失败");
                }
            }else {
            	mapRet.put("ret",1);
                mapRet.put("msg","参数错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg[0] = "1";
            errorMsg[1] = e.toString();
            mapRet.put("ret",1);
            mapRet.put("msg",e.toString());
            return jacksonUtil.Map2json(mapRet);
        }

        return jacksonUtil.Map2json(mapRet);
    }


    /**
     * 退回重填
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String submitReturnFill(String strParams, String[] errorMsg){
        // 返回值;
        Map<String, Object> mapRet = new HashMap<String, Object>();
        JacksonUtil jacksonUtil = new JacksonUtil();
        try {
            jacksonUtil.json2Map(strParams);
            // 实例ID;
            String tzms_wflinsid = jacksonUtil.getString("tzms_wflinsid");
            // 步骤实例id;
            String tzms_stpinsid = jacksonUtil.getString("tzms_stpinsid");
            //获取处理意见
            String handlingOpinions = jacksonUtil.getString("handlingOpinions");
            // dynamics用户id;
            String  userId = jacksonUtil.getString("userId");

            if(tzms_wflinsid!=null&&!"".equals(tzms_wflinsid)&&tzms_stpinsid!=null&&!"".equals(tzms_stpinsid)) {
                //调用张浪接口实现
                TzWorkflow workflowInstance = new TzWorkflow(userId);
                boolean createOK;
                try {
                    //流程初始化
                    createOK = workflowInstance.CreateWorkflow1(tzms_wflinsid, tzms_stpinsid);
                } catch (TzException e) {
                    createOK = false;
                    mapRet.put("ret",1);
                    mapRet.put("msg",e.toString());
                }

                if(createOK == true){
                    //错误信息
                    String ErrorMsg = "";
                    boolean returnOK;
                    try {
                        //退回重填
                        returnOK = workflowInstance.BackAndReFill(handlingOpinions);
                    } catch (TzException e) {
                        returnOK = false;
                        ErrorMsg = e.getMessage();
                    }

                    if(returnOK == true){
                        //转发成功
                        mapRet.put("ret",0);
                        mapRet.put("msg","退回重填成功");
                    }else{
                        //失败
                        mapRet.put("ret",1);
                        mapRet.put("msg",ErrorMsg);
                    }
                }else {
                	mapRet.put("ret",1);
                    mapRet.put("msg","退回重填失败");
                }
            }else {
            	mapRet.put("ret",1);
                mapRet.put("msg","参数错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg[0] = "1";
            errorMsg[1] = e.toString();
            mapRet.put("ret",1);
            mapRet.put("msg",e.toString());
            return jacksonUtil.Map2json(mapRet);
        }

        return jacksonUtil.Map2json(mapRet);
    }

    /**
     * 	退回某步
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String submitReturnStep(String strParams, String[] errorMsg){
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
            //获取处理意见
            String handlingOpinions = jacksonUtil.getString("handlingOpinions");
            //退回步骤id
            String tzms_bakstp_uniqueid = jacksonUtil.getString("tzms_bakstp_uniqueid");

            if(tzms_wflinsid!=null&&!"".equals(tzms_wflinsid)
            		&&tzms_stpinsid!=null&&!"".equals(tzms_stpinsid)
            		&&tzms_bakstp_uniqueid!=null&&!"".equals(tzms_bakstp_uniqueid)) {
                //调用张浪接口实现
                TzWorkflow workflowInstance = new TzWorkflow(userId);
                boolean createOK;
                try {
                    //流程初始化
                    createOK = workflowInstance.CreateWorkflow1(tzms_wflinsid, tzms_stpinsid);
                } catch (TzException e) {
                    createOK = false;
                    mapRet.put("ret",1);
                    mapRet.put("msg",e.toString());
                }

                if(createOK == true){
                    //错误信息
                    String ErrorMsg = "";
                    //工作流退回
                    boolean returnOK;
                    try {
                        //退回指定步骤
                        returnOK = workflowInstance.BackToChonseStep(handlingOpinions, tzms_bakstp_uniqueid);
                    } catch (TzException e) {
                        returnOK = false;
                        ErrorMsg = e.getMessage();
                    }

                    if(returnOK == true){
                        //成功
                        mapRet.put("ret",0);
                        mapRet.put("msg","退回成功");
                    }else{
                        //失败
                        mapRet.put("ret",1);
                        mapRet.put("msg",ErrorMsg);
                    }
                }else {
                	mapRet.put("ret",1);
                    mapRet.put("msg","退回失败");
                }
            }else {
            	mapRet.put("ret",1);
                mapRet.put("msg","参数错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg[0] = "1";
            errorMsg[1] = e.toString();
            mapRet.put("ret",1);
            mapRet.put("msg",e.toString());
            return jacksonUtil.Map2json(mapRet);
        }

        return jacksonUtil.Map2json(mapRet);
    }


    /**
     * 流程拒绝
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String submitRefuse(String strParams, String[] errorMsg){
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
            //获取拒绝处理意见
            String handlingOpinions = jacksonUtil.getString("handlingOpinions");

            if(tzms_wflinsid!=null&&!"".equals(tzms_wflinsid)&&tzms_stpinsid!=null&&!"".equals(tzms_stpinsid)) {
                //调用张浪接口实现
                TzWorkflow workflowInstance = new TzWorkflow(userId);
                boolean createOK;
                try {
                    //流程初始化
                    createOK = workflowInstance.CreateWorkflow1(tzms_wflinsid, tzms_stpinsid);
                } catch (TzException e) {
                    createOK = false;
                    mapRet.put("ret",1);
                    mapRet.put("msg",e.toString());
                }

                if(createOK == true){
                    //错误信息
                    ErrorMessage ErrorMsg = new ErrorMessage();
                    //工作流拒绝
                    boolean rejectOK = workflowInstance.WorkflowReject(handlingOpinions, ErrorMsg);

                    if(rejectOK == true){
                        //成功
                        mapRet.put("ret",0);
                        mapRet.put("msg","拒绝成功");
                    }else{
                        //失败
                        mapRet.put("ret",1);
                        mapRet.put("msg",ErrorMsg.getErrorMsg());
                    }
                }else {
                	mapRet.put("ret",1);
                    mapRet.put("msg", "拒绝失败");
                }
            }else {
            	mapRet.put("ret",1);
                mapRet.put("msg", "参数错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg[0] = "1";
            errorMsg[1] = e.toString();
            mapRet.put("ret",1);
            mapRet.put("msg",e.toString());
            return jacksonUtil.Map2json(mapRet);
        }

        return jacksonUtil.Map2json(mapRet);
    }


    /**
     * 流程撤回
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String submitWithdraw(String strParams, String[] errorMsg){
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

            if(tzms_wflinsid!=null&&!"".equals(tzms_wflinsid)&&tzms_stpinsid!=null&&!"".equals(tzms_stpinsid)) {
                //调用张浪接口实现
                //创建工作流实例
                TzWorkflow workflowInstance = new TzWorkflow(userId);
                boolean createOK;
                try {
                    //流程初始化
                    createOK = workflowInstance.CreateWorkflow1(tzms_wflinsid, tzms_stpinsid);
                    
                    if(createOK == true){
                        boolean withdrawOK;
                        try {
                            //工作流撤回
                            withdrawOK = workflowInstance.ExecuteWithdraw();
                            if(withdrawOK == true){
                                //转发成功
                                mapRet.put("ret",0);
                                mapRet.put("msg","撤回成功");
                            }else {
                            	mapRet.put("ret",1);
                                mapRet.put("msg","撤回失败");
                            }
                        } catch (TzException e) {
                            mapRet.put("ret",1);
                            mapRet.put("msg",e.getMessage());
                        }
                    }else {
                    	mapRet.put("ret",1);
                        mapRet.put("msg","撤回失败");
                    }
                } catch (TzException e) {
                    mapRet.put("ret",1);
                    mapRet.put("msg",e.getMessage());
                }
            }else {
            	mapRet.put("ret",1);
                mapRet.put("msg", "参数错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg[0] = "1";
            errorMsg[1] = "撤销失败" + e.getMessage();
        }

        return jacksonUtil.Map2json(mapRet);
    }

    /**
     * 流程撤销
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String submitCancel(String strParams, String[] errorMsg){
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

            if(tzms_wflinsid!=null&&!"".equals(tzms_wflinsid)&&tzms_stpinsid!=null&&!"".equals(tzms_stpinsid)) {
                //调用张浪接口实现
                //创建工作流实例
                TzWorkflow workflowInstance = new TzWorkflow(userId);
                boolean createOK;
                try {
                    //流程初始化
                    createOK = workflowInstance.CreateWorkflow1(tzms_wflinsid, tzms_stpinsid);
                } catch (TzException e) {
                    createOK = false;
                    mapRet.put("ret",1);
                    mapRet.put("msg",e.toString());
                }

                if(createOK == true){
                    //错误信息
                    String ErrorMsg = "";
                    boolean cancelOK;
                    try {
                        //工作流撤销
                        cancelOK = workflowInstance.WorkflowFirstStepCancel();
                    } catch (TzException e) {
                        cancelOK = false;
                        ErrorMsg = e.getMessage();
                    }

                    if(cancelOK == true){
                        //转发成功
                        mapRet.put("ret",0);
                        mapRet.put("msg","撤销成功");
                    }else{
                        //失败
                        mapRet.put("ret",1);
                        mapRet.put("msg",ErrorMsg);
                    }
                }else {
                	mapRet.put("ret",1);
                    mapRet.put("msg","撤销失败");
                }
            }else {
            	mapRet.put("ret",1);
                mapRet.put("msg","参数错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg[0] = "1";
            errorMsg[1] = e.toString();
            mapRet.put("ret",1);
            mapRet.put("msg",e.toString());
            return jacksonUtil.Map2json(mapRet);
        }

        return jacksonUtil.Map2json(mapRet);
    }


    /**
     * 流程已阅
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String readOver(String strParams, String[] errorMsg){
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

            if(tzms_wflinsid!=null&&!"".equals(tzms_wflinsid)
            		&&tzms_stpinsid!=null&&!"".equals(tzms_stpinsid)
            		&&userId!=null&&!"".equals(userId)) {
                //调用张浪接口实现
                //创建工作流实例，当前用户ID指的是Dynamics系统用户ID
                TzWorkflow workflowInstance = new TzWorkflow(userId);

                boolean createOK;
                try {
                    //流程初始化
                    createOK = workflowInstance.CreateWorkflow1(tzms_wflinsid, tzms_stpinsid);
                } catch (TzException e) {
                    createOK = false;
                    mapRet.put("ret",1);
                    mapRet.put("msg",e.toString());
                }

                if(createOK == true){
                    //工作流已阅
                    boolean readOver = workflowInstance.TaskReadOver();

                    if(readOver == true){
                        //已阅成功
                        mapRet.put("ret",0);
                        mapRet.put("msg","已阅成功");
                    }else{
                        //已阅失败
                        mapRet.put("ret",1);
                        mapRet.put("msg","已阅失败");
                    }
                }else {
                	mapRet.put("ret",1);
                    mapRet.put("msg","已阅失败");
                }
            }else {
            	mapRet.put("ret",1);
                mapRet.put("msg","参数错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg[0] = "1";
            errorMsg[1] = e.toString();
            mapRet.put("ret",1);
            mapRet.put("msg",e.toString());
            return jacksonUtil.Map2json(mapRet);
        }

        return jacksonUtil.Map2json(mapRet);
    }
}
