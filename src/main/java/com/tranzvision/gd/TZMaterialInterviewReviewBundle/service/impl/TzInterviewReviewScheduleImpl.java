package com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.TZMbaPwMspsBundle.dao.psTzMspwpsjlTblMapper;
import com.tranzvision.gd.TZMbaPwMspsBundle.model.psTzMspwpsjlTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpsGzTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzKsclpslsTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzMsPsGzTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzMsPsGzTblMapper;


/**PS_TZ_MSPWPSJL_TBL psTzMspwpsjlTbl
 * 评审进度管理
 * 原 TZ_GD_MSPS_PKG:TZ_GD_PLAN_CLS
 * @author yuds
 * 时间：2017年3月20日
 */

@Service("com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl.TzInterviewReviewScheduleImpl")
public class TzInterviewReviewScheduleImpl extends FrameworkImpl  {
    @Autowired
    private SqlQuery sqlQuery;
    @Autowired
    private GetSysHardCodeVal getSysHardCodeVal;
    @Autowired
    private TZGDObject tzGdObject;
    @Autowired
    private TzLoginServiceImpl tzLoginServiceImpl;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private psTzMspwpsjlTblMapper psTzMspwpsjlTblMapper;
    @Autowired
    private PsTzMsPsGzTblMapper psTzMsPsGzTblMapper;
    
    @SuppressWarnings("unchecked")
    @Override
    public String tzQuery(String strParams, String[] errMsg) {
	String strResponse = "\"failure\"";
	JacksonUtil jacksonUtil = new JacksonUtil();
	try {
	    jacksonUtil.json2Map(strParams);
	    String strClassID = jacksonUtil.getString("classID");
	    String strBatchID = jacksonUtil.getString("batchID");
	    
	    String strScoreModalSql = "SELECT TZ_ZLPS_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
	    String strScoreModalId = sqlQuery.queryForObject(strScoreModalSql, new Object[] { strClassID },
			"String");
	    // 当前班级报考人数
	    String strTotalStudentSql = "SELECT COUNT(1) FROM PS_TZ_FORM_WRK_T A,PS_TZ_APP_INS_T B WHERE A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND B.TZ_APP_FORM_STA='U' AND A.TZ_CLASS_ID=? AND A.TZ_BATCH_ID=?";
	    String strTotalStudentCount = sqlQuery.queryForObject(strTotalStudentSql, new Object[] { strClassID,strBatchID },"String");
	    // 当前批次人数
	    /*String strCurBatchStuSql = "SELECT COUNT(*) FROM PS_TZ_MSPS_KSH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
	    String strCurBatchStuCount = sqlQuery.queryForObject(strCurBatchStuSql,
		    new Object[] { strClassID, strBatchID }, "String");*/

	    // 状态以及日期相关信息
	    String str_startDtime="",str_endDtime="",str_dqpyZt="",str_pwkjTjb="",str_pwkjFbt="";
	    String strStatusSql = "SELECT CONCAT(date_format(TZ_PYKS_RQ,'%Y-%m-%d'),' ',date_format(TZ_PYKS_SJ,'%H:%i')) AS STARTDTIME,CONCAT(date_format(TZ_PYJS_RQ,'%Y-%m-%d'),' ',date_format(TZ_PYJS_SJ,'%H:%i')) AS ENDDTIME,TZ_DQPY_ZT,TZ_PWKJ_TJB,TZ_PWKJ_FBT FROM PS_TZ_MSPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";

	    String strStatus = "未开始", strDelibCount = "0";
	    List<Map<String, Object>> list = sqlQuery.queryForList(strStatusSql,
			new Object[] { strClassID, strBatchID });
	    if (list != null && list.size() > 0) {
		for(Object obj:list){
    	    	    Map<String,Object> result=(Map<String,Object>) obj;
    	    	    str_startDtime = result.get("STARTDTIME")==null ? "" : String.valueOf(result.get("STARTDTIME"));
    	    	    str_endDtime = result.get("ENDDTIME")==null ? "" : String.valueOf(result.get("ENDDTIME"));
    	    	    str_dqpyZt = result.get("TZ_DQPY_ZT")==null ? "" : String.valueOf(result.get("TZ_DQPY_ZT"));
    	    	    str_pwkjTjb = result.get("TZ_PWKJ_TJB")==null ? "" : String.valueOf(result.get("TZ_PWKJ_TJB"));
    	    	    str_pwkjFbt = result.get("TZ_PWKJ_FBT")==null ? "" : String.valueOf(result.get("TZ_PWKJ_FBT"));    	    	
		}
	    }
	    String sql10Material = "SELECT COUNT(*) FROM PS_TZ_CLPS_KSH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
	    Integer materialStudents = sqlQuery.queryForObject(sql10Material, new Object[] { strClassID, strBatchID },
		"Integer");
	    
	    /* 获取当前批次下考生的数量 */
	    String sql10 = "SELECT COUNT(*) FROM PS_TZ_MSPS_KSH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
	    Integer interviewStudents = sqlQuery.queryForObject(sql10, new Object[] { strClassID, strBatchID },"Integer");

	    String strTotalSql = "SELECT ifnull(TZ_MSPY_NUM,0) FROM PS_TZ_MSPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
	    Integer judgeCount = sqlQuery.queryForObject(strTotalSql, new Object[] { strClassID, strBatchID },
		"Integer");
	    if (judgeCount == null) {
		judgeCount = 0;
	    }
	    Integer numtotal = interviewStudents * judgeCount;
	    // 得出当前班级，当前批次，当前轮次的已评审人次数
	    String strClpsCountSql = "SELECT COUNT(*) FROM PS_TZ_MP_PW_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PSHEN_ZT='Y' AND TZ_DELETE_ZT<>'Y'";
	    String strClpsCount = sqlQuery.queryForObject(strClpsCountSql, new Object[] { strClassID, strBatchID }, "String");
	    
	    String strProgress = strClpsCount + "/" + numtotal;

	    //每位考生需要被多少位评委评审
	    String strStuSqlCount = "SELECT ifnull(TZ_MSPY_NUM,0) FROM PS_TZ_MSPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
	    Integer intStuC = sqlQuery.queryForObject(strClpsCountSql, new Object[] { strClassID, strBatchID}, "Integer");
	    
	    Map<String, Object> mapData = new HashMap<String, Object>();
	    mapData.put("classID", strClassID);
	    mapData.put("batchID", strBatchID);
	    mapData.put("totalStudents", strTotalStudentCount);
	    mapData.put("interviewStudents", String.valueOf(interviewStudents));
	    mapData.put("materialStudents", String.valueOf(materialStudents));
	    mapData.put("status", str_dqpyZt);
	    mapData.put("progress", strProgress);
	    mapData.put("startDateTime", str_startDtime);
	    mapData.put("endDateTime", str_endDtime);
	    mapData.put("judgeTJB", str_pwkjTjb);
	    mapData.put("judgeFBT", str_pwkjFbt);
	    mapData.put("judgeCount", intStuC);
	    //要求评审人次
	    //mapData.put("requiredCount", numtotal);
	    strResponse = jacksonUtil.Map2json(mapData);
	}catch(Exception e){
	    
	}
	return strResponse;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public String tzQueryList(String strParams,int numLimit, int numStart, String[] errMsg) {
	String strResponse = "\"failure\"";
	JacksonUtil jacksonUtil = new JacksonUtil();
	try {
	    jacksonUtil.json2Map(strParams);
	    String strType = jacksonUtil.getString("type");
	    switch(strType){
	    	case "judgeInfo":
	    	    strResponse = this.queryJudgeInfo(strParams,errMsg);
	    	    break;
	    	case "stuList":
	    	    strResponse = this.queryStuList(strParams,numLimit,numStart,errMsg);	    	
	    	    break;
	    	case "chart":	    	
	    	    strResponse = this.queryPwDfChartData(strParams,errMsg);	 
	    	    break;
	    	case "getPwDfData":
	    	    strResponse = this.tzGetPwDfTotalPjfGridData(strParams,errMsg);
	    	    break;
	    	default:
	    	    /*传入错误的类型*/
	    }
	}catch (Exception e) {
	    /* doNothing */
	}
	return strResponse;
    }
    
    @SuppressWarnings("unchecked")
    public String queryJudgeInfo(String strParams, String[] errMsg) {
	String strResponse = "";
	JacksonUtil jacksonUtil = new JacksonUtil();
	try {
	    jacksonUtil.json2Map(strParams);
	    String strClassID = jacksonUtil.getString("classID");
	    String strBatchID = jacksonUtil.getString("batchID");
	    String strCurrentOrg = tzLoginServiceImpl.getLoginedManagerOrgid(request);

	    String strScoreModalSql = "SELECT TZ_ZLPS_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
	    String strScoreModalId = sqlQuery.queryForObject(strScoreModalSql, new Object[] { strClassID }, "String");
	    
	    String strTreeName = "";
	    String strTreeNameSql = "SELECT TREE_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=?";
	    strTreeName = sqlQuery.queryForObject(strScoreModalSql, new Object[] { strScoreModalId }, "String");
	    
	    String strScoreItemId = "";
	    String strScoreItemSql = "SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? and PARENT_NODE_NUM=0";
	    strScoreItemId = sqlQuery.queryForObject(strScoreItemSql, new Object[] { strTreeName }, "String");

	    String strJudgeAccount="",strJudgeAccountID="",strJudgeStatus="",strJudgeGroup="",strJudgeStuSX="",strJugeStuXX="",strJudgeDescript="",strSubmitYN="",strTotalNum="0";
	    String strPwSql = "SELECT TZ_PWEI_OPRID,TZ_PWEI_ZHZT,TZ_PWEI_GRPID FROM PS_TZ_MSPS_PW_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
	    List<Map<String, Object>> pwList = sqlQuery.queryForList(strPwSql, new Object[] { strClassID,strBatchID });
	    if (pwList != null && pwList.size() > 0) {
    	    	for(Object obj:pwList){
    	    	    Map<String,Object> result=(Map<String,Object>) obj;
    	    	    strJudgeAccount = result.get("TZ_PWEI_OPRID")==null ? "" : String.valueOf(result.get("TZ_PWEI_OPRID"));
    	    	    strJudgeStatus = result.get("TZ_PWEI_ZHZT")==null ? "" : String.valueOf(result.get("TZ_PWEI_ZHZT"));
    	    	    strJudgeGroup = result.get("TZ_PWEI_GRPID")==null ? "" : String.valueOf(result.get("TZ_PWEI_GRPID")); 			   
		    
		    String strSubSql = "SELECT TZ_SUBMIT_YN FROM PS_TZ_MSPWPSJL_TBL WHERE TZ_APPLY_PC_ID=? AND TZ_CLASS_ID=? AND TZ_PWEI_OPRID=?";
		    strSubmitYN = sqlQuery.queryForObject(strSubSql, new Object[] { strBatchID,strClassID,strJudgeAccount }, "String");
		    
		    String strPwNameSql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_JG_ID=? AND OPRID=?";
		    String strJudgeName = sqlQuery.queryForObject(strPwNameSql, new Object[] { strCurrentOrg,strJudgeAccount }, "String");
		    
		    //考生材料评审历史表（里面记录了各个轮次评委的打分情况）
		    String strSql1 = "SELECT COUNT(*) FROM PS_TZ_MP_PW_KS_TBL WHERE TZ_APPLY_PC_ID=? AND TZ_CLASS_ID=? AND TZ_PWEI_OPRID=? AND TZ_PSHEN_ZT='Y' AND TZ_DELETE_ZT<>'Y'";
		    String strHasSubmited = sqlQuery.queryForObject(strSql1, new Object[] { strBatchID,strClassID,strJudgeAccount }, "String");
		    		  		    
		    String strSql2 = "SELECT COUNT(*) FROM PS_TZ_MP_PW_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=? AND TZ_DELETE_ZT<>'Y'";
		    String strNeedSubmit = sqlQuery.queryForObject(strSql2, new Object[] { strClassID,strBatchID,strJudgeAccount }, "String");
		    		    
		    String strSubmited = strNeedSubmit + "/" + strHasSubmited;
		    
		    String strPwIdSql = "SELECT TZ_DLZH_ID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_JG_ID=? AND OPRID=?";
		    strJudgeAccountID = sqlQuery.queryForObject(strPwIdSql, new Object[] { strCurrentOrg,strJudgeAccount }, "String");
		    
		    /*String strSql3 = "SELECT TZ_PWZMS FROM PS_TZ_PWZDY_T WHERE TZ_PWZBH=?";
		    String strJudgeGroupDesc = sqlQuery.queryForObject(strSql3, new Object[] { strJudgeGroup }, "String");*/
		    
		    if(strResponse!=null&&!"".equals(strResponse)){
			strResponse = strResponse + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_MSPS_PWINFO_HTML", strClassID,strBatchID,strJudgeAccountID,strJudgeName,strJudgeGroup,strSubmitYN,strJudgeStatus,strSubmited);
		    }else{
			strResponse = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_MSPS_PWINFO_HTML", strClassID,strBatchID,strJudgeAccountID,strJudgeName,strJudgeGroup,strSubmitYN,strJudgeStatus,strSubmited);
		    }
		}
	    }
	    
	   /* String strSql4 = "SELECT TZ_MSPY_NUM FROM PS_TZ_MSPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
	    String strJudgesCount = sqlQuery.queryForObject(strSql4, new Object[] { strClassID,strBatchID }, "String");*/
	    
	    String strSql5 = "SELECT COUNT(1) FROM PS_TZ_MSPS_PW_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
	    strTotalNum = sqlQuery.queryForObject(strSql5, new Object[] { strClassID,strBatchID }, "String");
	    
	    strResponse = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_BASE_JSON_HTML", strTotalNum,strResponse);
	    
	} catch (Exception e) {
	    e.printStackTrace();
	    errMsg[0] = "100";
	    errMsg[1] = e.toString();
	}
	return strResponse;
    }
    
    @SuppressWarnings("unchecked")
    public String queryStuList(String strParams,int numLimit, int numStart, String[] errMsg) {
	String strResponse = "";
	JacksonUtil jacksonUtil = new JacksonUtil();
	try {
	    jacksonUtil.json2Map(strParams);
	    String strClassID = jacksonUtil.getString("classID");
	    String strBatchID = jacksonUtil.getString("batchID");
	    String strCurrentOrg = tzLoginServiceImpl.getLoginedManagerOrgid(request);

	    String strScoreModalSql = "SELECT TZ_ZLPS_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
	    String strScoreModalId = sqlQuery.queryForObject(strScoreModalSql, new Object[] { strClassID }, "String");
	    
	    String strTreeName = "";
	    String strTreeNameSql = "SELECT TREE_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=?";
	    strTreeName = sqlQuery.queryForObject(strScoreModalSql, new Object[] { strScoreModalId }, "String");
	    
	    String strScoreItemId = "";
	    String strScoreItemSql = "SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? and PARENT_NODE_NUM=0";
	    strScoreItemId = sqlQuery.queryForObject(strScoreItemSql, new Object[] { strTreeName }, "String");
	    
	    Integer numZfz = 0;
	    String strZfzSql = "SELECT COUNT(*) FROM PS_TZ_MODAL_DT_TBL A,PS_TZ_CJ_BPH_TBL B WHERE A.TREE_NAME=? AND A.TZ_SCORE_ITEM_ID=B.TZ_SCORE_ITEM_ID AND B.TZ_SCORE_MODAL_ID=? AND A.TZ_SCR_TO_SCORE='Y' AND B.TZ_ITEM_S_TYPE='Y'";
	    numZfz = sqlQuery.queryForObject(strZfzSql, new Object[] { strTreeName, strScoreModalId }, "Integer");
	    
	    //报名表编号 姓名     性别    面试资格   评委间偏差    评委信息      评审状态   操作人   平均分;
	    String strAppInsID="",strName="",strGender="",strViewQua="",strPweiPc="",strJudgeInfo="",strJudgeStatus="",strOprID="",strLqZt="";
	    String strSql1 = "SELECT TZ_APP_INS_ID,TZ_MSPS_PWJ_PC,TZ_LUQU_ZT FROM PS_TZ_MSPS_KSH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? limit " + numStart + "," + numLimit;
	    
	    List<Map<String, Object>> mapList1 = sqlQuery.queryForList(strSql1, new Object[] { strClassID,strBatchID });
	    if(mapList1!=null&&mapList1.size()>0){
		for(Object obj1:mapList1){
    	    	    Map<String,Object> result=(Map<String,Object>) obj1;
    	    	    strAppInsID = result.get("TZ_APP_INS_ID")==null ? "" : String.valueOf(result.get("TZ_APP_INS_ID"));
    	    	    strLqZt = result.get("TZ_LUQU_ZT")==null ? "" : String.valueOf(result.get("TZ_LUQU_ZT"));
    	    	    strPweiPc = result.get("TZ_CLPS_PWJ_PC")==null ? "" : String.valueOf(result.get("TZ_CLPS_PWJ_PC"));

		    String sql2 = "SELECT TZ_APP_INS_ID ,(SELECT OPRID FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID=A.TZ_APP_INS_ID limit 0,1) OPRID, (SELECT TZ_REALNAME FROM PS_TZ_FORM_WRK_T B ,PS_TZ_REG_USER_T C WHERE B.TZ_APP_INS_ID=A.TZ_APP_INS_ID AND B.OPRID = C.OPRID limit 0,1) TZ_REALNAME, (SELECT TZ_GENDER FROM PS_TZ_FORM_WRK_T B ,PS_TZ_REG_USER_T C WHERE B.TZ_APP_INS_ID=A.TZ_APP_INS_ID AND B.OPRID = C.OPRID limit 0,1)TZ_GENDER FROM PS_TZ_MSPS_KSH_TBL A WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=?";
		    List<Map<String, Object>> mapList2 = sqlQuery.queryForList(sql2, new Object[] { strClassID,strBatchID,strAppInsID });
		    if(mapList2!=null&&mapList2.size()>0){
			for(Object obj2:mapList2){
			    Map<String,Object> result2=(Map<String,Object>) obj2;
			    strOprID = result2.get("OPRID")==null ? "" : String.valueOf(result2.get("OPRID"));
			    strName = result2.get("TZ_REALNAME")==null ? "" : String.valueOf(result2.get("TZ_REALNAME"));
			    strGender = result2.get("TZ_GENDER")==null ? "" : String.valueOf(result2.get("TZ_GENDER"));
			}
		    }
		    String strPwList = "";
		    String sql3 = "SELECT TZ_PWEI_OPRID FROM PS_TZ_MP_PW_KS_TBL  WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=?";
		    List<Map<String, Object>> mapList3 = sqlQuery.queryForList(sql3, new Object[] { strClassID,strBatchID,strAppInsID });
		    if(mapList3!=null&&mapList3.size()>0){
			for(Object obj2:mapList3){
			    Map<String,Object> result3=(Map<String,Object>) obj2;
			    String strPWeiOprid = result3.get("TZ_PWEI_OPRID")==null ? "" : String.valueOf(result3.get("TZ_PWEI_OPRID"));
			    
			    String strPwNameSql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_JG_ID=? AND OPRID=?";
			    String strJudgeName = sqlQuery.queryForObject(strPwNameSql, new Object[] { strCurrentOrg,strPWeiOprid }, "String");
			    if(strPwList!=null&&!"".equals(strPwList)){
				strPwList = strPwList + "," + strJudgeName;
			    }else{
				strPwList = strJudgeName;
			    }
			}
		    }
		    //面试资格从材料评审中获取
		    String strTmpSql1 = "SELECT TZ_MSHI_ZGFLG FROM PS_TZ_CLPS_KSH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=?";
		    strViewQua = sqlQuery.queryForObject(strTmpSql1, new Object[]{strClassID,strBatchID,strAppInsID}, "String");
		    if("Y".equals(strViewQua)){
			strViewQua = "有面试资格";
		    }else if("N".equals(strViewQua)){
			strViewQua = "无面试资格";
		    }else{
			strViewQua = "待定";
		    }		    
		    
		    Integer intTotalSub = 0;
		    String strSql4 = "SELECT ifnull(COUNT(*),0) FROM PS_TZ_MP_PW_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? AND TZ_PSHEN_ZT='Y' AND TZ_DELETE_ZT<>'Y'";
		    intTotalSub = sqlQuery.queryForObject(strSql4, new Object[] { strClassID,strBatchID,strAppInsID  }, "Integer");
		    
		    Integer intMspyNum = 0;
		    String strSql5 = "SELECT ifnull(TZ_MSPY_NUM,0) FROM PS_TZ_MSPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
		    intMspyNum = sqlQuery.queryForObject(strSql5, new Object[] { strClassID,strBatchID }, "Integer");
		    
		    Integer intFlg = intTotalSub - intMspyNum;
		    
		    String strJudgeProgress="",strStuProgress="";
		    if(intFlg!=0){
			strJudgeStatus = "N";
			strJudgeProgress = intMspyNum + "/" + intTotalSub;
			strStuProgress = "未完成" + "(" + strJudgeProgress + ")" ;
		    }else{
			strJudgeStatus = "Y";
			strJudgeProgress = "";
			strStuProgress = "已完成";		
		    }
		    
		    if(!"".equals(strResponse)&&strResponse!=null){
			strResponse = strResponse + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_MSPS_STULIST_HTML",strAppInsID,strName,strGender,strPweiPc,strPwList,strStuProgress,strLqZt,strPwList,strJudgeProgress,strViewQua);
		    }else{
			strResponse = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_MSPS_STULIST_HTML",strAppInsID,strName,strGender,strPweiPc,strPwList,strStuProgress,strLqZt,strPwList,strJudgeProgress,strViewQua);
		    }
		}
	    }
	    String strSql3 = "SELECT ifnull(COUNT(1),0) FROM PS_TZ_MSPS_PW_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
	    String strTotalNum = sqlQuery.queryForObject(strSql3, new Object[] { strClassID,strBatchID }, "String");
	    
	    strResponse = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_BASE_JSON_HTML", strTotalNum,strResponse);
	} catch (Exception e) {
	    e.printStackTrace();
	    errMsg[0] = "100";
	    errMsg[1] = e.toString();
	}
	return strResponse;
    }
    
    @SuppressWarnings("unchecked")
    public String queryPwDfChartData(String strParams, String[] errMsg) {
	String strResponse = "\"failure\"";
	JacksonUtil jacksonUtil = new JacksonUtil();
	try {
	    jacksonUtil.json2Map(strParams);
	    String strClassID = jacksonUtil.getString("classID");
	    String strBatchID = jacksonUtil.getString("batchID");
	    String strCurrentOrg = tzLoginServiceImpl.getLoginedManagerOrgid(request);
	    String strPwDlzhIDs = jacksonUtil.getString("pw_ids");
	    
	    String[] strPwZhArray = strPwDlzhIDs.split(",");
	    ArrayList<String> strPwOprIDList = new ArrayList<String>();
	    
	    String strScoreModalID = "";
	    String strSql1 = "SELECT TZ_ZLPS_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
	    strScoreModalID = sqlQuery.queryForObject(strSql1, new Object[] { strClassID }, "String");
	    
	    String strFbdzID="",strJsfs="",strTreeName="";
	    String strSql2 = "SELECT TZ_M_FBDZ_ID,TREE_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=?";
	    List<Map<String, Object>> mapList1 = sqlQuery.queryForList(strSql2, new Object[] { strClassID });
	    if(mapList1!=null&&mapList1.size()>0){
		for(Object obj1:mapList1){
		    Map<String,Object> result1=(Map<String,Object>) obj1;
		    strFbdzID = result1.get("TZ_M_FBDZ_ID")==null ? "" : String.valueOf(result1.get("TZ_M_FBDZ_ID"));
		    strTreeName = result1.get("TREE_NAME")==null ? "" : String.valueOf(result1.get("TREE_NAME"));
		}
	    }
	    
	    String strScoreItemID = "";
	    String strScoreItemSql = "SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? and PARENT_NODE_NUM=0";
	    strScoreItemID = sqlQuery.queryForObject(strScoreItemSql, new Object[] { strTreeName }, "String");
	    
	    String strTreeNode="";
	    String strSql3 = "SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? AND PARENT_NODE_NUM=0";
	    strTreeNode = sqlQuery.queryForObject(strSql3, new Object[] { strTreeName }, "String");
	    
	    //柱状图数据
	    String strColumnChartHTML="";
	    //图表数据
	    String strChartFieldsHTML="";
	    
	    int intTb = 0;
	    String strPwOprid="",strPwDlzhID="",strPwName="";
	    String strSql4 = "SELECT TZ_PWEI_OPRID,(SELECT TZ_DLZH_ID FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=A.TZ_PWEI_OPRID limit 0,1) TZ_DLZH_ID,(SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL R WHERE R.OPRID=A.TZ_PWEI_OPRID) TZ_REALNAME FROM PS_TZ_CLPS_PW_TBL A WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
	    List<Map<String, Object>> mapList2 = sqlQuery.queryForList(strSql4, new Object[] { strClassID,strBatchID });
	    if(mapList2!=null&&mapList2.size()>0){		
		for(Object obj2:mapList2){
		    Map<String,Object> result2=(Map<String,Object>) obj2;
		    strPwOprid = result2.get("TZ_PWEI_OPRID")==null ? "" : String.valueOf(result2.get("TZ_PWEI_OPRID"));
		    strPwDlzhID = result2.get("TZ_DLZH_ID")==null ? "" : String.valueOf(result2.get("TZ_DLZH_ID"));
		    strPwName = result2.get("TZ_REALNAME")==null ? "" : String.valueOf(result2.get("TZ_REALNAME"));
		    
		    if(this.find(strPwZhArray,strPwDlzhID)>=0){
			strPwOprIDList.add(strPwOprid);
			//总分平均分-待完成
			double douPjf = 12.5;
			
			if(strColumnChartHTML!=null&&!"".equals(strColumnChartHTML)){
			    strColumnChartHTML = strColumnChartHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "name",strPwName);
			}else{
			    strColumnChartHTML = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "name",strPwName);
			}
			strColumnChartHTML = strColumnChartHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_NUM_HTML", "data1",String.valueOf(douPjf));
			strColumnChartHTML = strColumnChartHTML + "}";
			
			//图表字段
			intTb = intTb + 1;
			if(strChartFieldsHTML!=null&&!"".equals(strColumnChartHTML)){
			    strChartFieldsHTML = strChartFieldsHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "pw_field","data" + intTb);
			}else{
			    strChartFieldsHTML = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "pw_field","data" + intTb);
			}
			strChartFieldsHTML = strChartFieldsHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "pw_name",strPwName);
			strChartFieldsHTML = strChartFieldsHTML + "}";
		    }
		}
	    }
	    //添加所有评委总分平均分字段名称
	    intTb = intTb + 1;
	    if(strChartFieldsHTML!=null&&!"".equals(strColumnChartHTML)){
		strChartFieldsHTML = strChartFieldsHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "pw_field","data" + intTb);
	    }else{
		strChartFieldsHTML = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "pw_field","data" + intTb);
	    }
	    strChartFieldsHTML = strChartFieldsHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "pw_name","标准曲线");
	    strChartFieldsHTML = strChartFieldsHTML + "}";
	    
	    //根据报考班级和批次取得当前轮次
	    Integer intDqpyLunc = 0;
	    String strDqpyLuncSql = "SELECT ifnull(TZ_DQPY_LUNC,0) FROM PS_TZ_CLPS_GZ_TBL WHERE TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=?";
	    intDqpyLunc = sqlQuery.queryForObject(strDqpyLuncSql, new Object[] { strClassID, strBatchID }, "Integer");
	    
	    //总分平均分-待完成
	    double douZfPjf = 12.5;
	    String strTotalName = "标准平均分";
	    
	    if(strColumnChartHTML!=null&&!"".equals(strColumnChartHTML)){
		strColumnChartHTML = strColumnChartHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "name",strTotalName);
	    }else{
		strColumnChartHTML = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "name",strTotalName);
	    }
	    strColumnChartHTML = strColumnChartHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_NUM_HTML", "data1",String.valueOf(douZfPjf));
	    strColumnChartHTML = strColumnChartHTML + "}";

	    //评委完成评审考生数量
	    int intWc = 0;
	    String strLineChartHTML="";
	   
	    String strMFbdzMxId="",strMFbdzMxName="",strMFbdzMxXX="",strMFbdzMxXxJx="",strMFbdzMxSx="",strMFbdzMxSxJx="";
	    String strFbdzSql = "SELECT  TZ_M_FBDZ_MX_ID,TZ_M_FBDZ_MX_NAME,TZ_M_FBDZ_MX_XX,TZ_M_FBDZ_MX_XX_JX,TZ_M_FBDZ_MX_SX_JX,TZ_M_FBDZ_MX_SX  FROM PS_TZ_FBDZ_MX_TBL WHERE TZ_M_FBDZ_ID = ? ORDER BY TZ_M_FBDZ_MX_XH ASC";
	    List<Map<String, Object>> mapList3 = sqlQuery.queryForList(strFbdzSql, new Object[] { strFbdzID });
	    if (mapList3 != null && mapList3.size() > 0) {
		for(Object obj3:mapList3){
		    Map<String,Object> result3=(Map<String,Object>) obj3;
		    strPwOprid = result3.get("TZ_M_FBDZ_MX_ID")==null ? "" : String.valueOf(result3.get("TZ_M_FBDZ_MX_ID"));
		    strMFbdzMxName = result3.get("TZ_M_FBDZ_MX_NAME")==null ? "" : String.valueOf(result3.get("TZ_M_FBDZ_MX_NAME"));
		    strMFbdzMxXX = result3.get("TZ_M_FBDZ_MX_XX")==null ? "" : String.valueOf(result3.get("TZ_M_FBDZ_MX_XX"));
		    strMFbdzMxXxJx = result3.get("TZ_M_FBDZ_MX_XX_JX")==null ? "" : String.valueOf(result3.get("TZ_M_FBDZ_MX_XX_JX"));
		    strMFbdzMxSx = result3.get("TZ_M_FBDZ_MX_SX")==null ? "" : String.valueOf(result3.get("TZ_M_FBDZ_MX_SX"));
		    strMFbdzMxSxJx = result3.get("TZ_M_FBDZ_MX_SX_JX")==null ? "" : String.valueOf(result3.get("TZ_M_FBDZ_MX_SX_JX"));
		    
		    if(strLineChartHTML!=null&&!"".equals(strLineChartHTML)){
			strLineChartHTML = strLineChartHTML + ",{" + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "fbName",strMFbdzMxName);
		    }else{
			strLineChartHTML = "{" + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "fbName",strMFbdzMxName);
		    }
		    		    	   
		    int ii=0;
		    String strSearchWhere = "";
		    if(strPwOprIDList.size()>0){
			strSearchWhere = " AND (";
			for( ii=0;ii<strPwOprIDList.size();ii++){
			    String tmpOprid = strPwOprIDList.get(ii);
			    if(ii==0){
				strSearchWhere = strSearchWhere + " A.TZ_PWEI_OPRID = '" + tmpOprid + "'";				
			    }else{
				strSearchWhere = strSearchWhere + " OR A.TZ_PWEI_OPRID = '" + tmpOprid + "'";	
			    }
			    
			    
			    String strSql5 = "SELECT COUNT(TZ_APP_INS_ID) FROM PS_TZ_KSCLPSLS_TBL WHERE TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ? AND TZ_PWEI_OPRID = ? AND TZ_CLPS_LUNC = ? AND  TZ_SUBMIT_YN <> 'C'";
			    intWc = sqlQuery.queryForObject(strSql5, new Object[] { strClassID, strBatchID,tmpOprid,intDqpyLunc }, "int");
			    
			    int intDange = 0;
			    String strSql6 = "SELECT COUNT(A.TZ_APP_INS_ID) FROM PS_TZ_CP_PW_KS_TBL A ,PS_TZ_CJX_TBL B ,PS_TZ_KSCLPSLS_TBL C  WHERE A.TZ_SCORE_INS_ID = B.TZ_SCORE_INS_ID  AND A.TZ_CLASS_ID = C.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID = C.TZ_APPLY_PC_ID AND A.TZ_PWEI_OPRID = C.TZ_PWEI_OPRID AND A.TZ_APP_INS_ID = C.TZ_APP_INS_ID AND C.TZ_SUBMIT_YN <> 'C'  AND A.TZ_CLASS_ID = ? AND A.TZ_APPLY_PC_ID = ? AND A.TZ_PWEI_OPRID =? AND B.TZ_SCORE_ITEM_ID = ?  AND C.TZ_CLPS_LUNC = ? AND B.TZ_SCORE_NUM " 
					+ strMFbdzMxXxJx + strMFbdzMxXX + "AND B.TZ_SCORE_NUM " + strMFbdzMxSxJx + strMFbdzMxSx;
			    intDange = sqlQuery.queryForObject(strSql6, new Object[] { strClassID,strBatchID,strPwOprid,strScoreItemID,intDqpyLunc }, "int");
			    if(intDange==0||intWc==0){
				strLineChartHTML = strLineChartHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_NUM_HTML", "data"+ii,"0");
			    }else{
				DecimalFormat df  = new DecimalFormat("######0.00");   
				double tmpDouble = (double)(intDange/intWc)*100;
				String tmpAveScore = df.format(tmpDouble);				
				strLineChartHTML = strLineChartHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_NUM_HTML", "data"+ii,tmpAveScore);
			    }
			}
			strSearchWhere = strSearchWhere + " )";
		    }
		    
		    int intWc2 = 0;
		    String strSql7 = "SELECT COUNT(TZ_APP_INS_ID) FROM PS_TZ_KSCLPSLS_TBL A WHERE TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ? AND TZ_CLPS_LUNC = ? AND  TZ_SUBMIT_YN <> 'C'" + strSearchWhere;
		    intWc2 = sqlQuery.queryForObject(strSql7, new Object[] { strClassID, strBatchID, intDqpyLunc }, "int");
		    int intDange2 = 0;
		    String strSql8 = "SELECT COUNT(A.TZ_APP_INS_ID) FROM PS_TZ_CP_PW_KS_TBL A ,PS_TZ_CJX_TBL B ,PS_TZ_KSCLPSLS_TBL C  WHERE A.TZ_SCORE_INS_ID = B.TZ_SCORE_INS_ID  AND A.TZ_CLASS_ID = C.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID = C.TZ_APPLY_PC_ID AND A.TZ_PWEI_OPRID = C.TZ_PWEI_OPRID AND A.TZ_APP_INS_ID = C.TZ_APP_INS_ID AND C.TZ_SUBMIT_YN <> 'C'  AND A.TZ_CLASS_ID = ? AND A.TZ_APPLY_PC_ID = ?  AND B.TZ_SCORE_ITEM_ID = ?  AND C.TZ_CLPS_LUNC = ? AND B.TZ_SCORE_NUM  " 
				+ strMFbdzMxXxJx + strMFbdzMxXX + "AND B.TZ_SCORE_NUM " + strMFbdzMxSxJx + strMFbdzMxSx  + strSearchWhere;
		    intDange2 = sqlQuery.queryForObject(strSql8, new Object[] { strClassID,strBatchID,strScoreItemID,intDqpyLunc }, "int");
		    if(intDange2==0||intWc2==0){
			strLineChartHTML = strLineChartHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_NUM_HTML", "data"+ii,"0");
		    }else{
			DecimalFormat df  = new DecimalFormat("######0.00");   
			double tmpDouble = (double)(intDange2/intWc2)*100;
			String tmpAveScore = df.format(tmpDouble);				
			strLineChartHTML = strLineChartHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_NUM_HTML", "data"+ii,tmpAveScore);
		    }
		    strLineChartHTML = strLineChartHTML + "}";
		}	
	    }
	    strResponse = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_CHART_JSON_HTML", strColumnChartHTML,strLineChartHTML,strChartFieldsHTML,strJsfs);
	} catch (Exception e) {
	    e.printStackTrace();
	    errMsg[0] = "100";
	    errMsg[1] = e.toString();
	}
	return strResponse;
    }
    
    @SuppressWarnings("unchecked")
    public String tzGetPwDfTotalPjfGridData(String strParams, String[] errMsg) {
	String strResponse = "\"failure\"";

	JacksonUtil jacksonUtil = new JacksonUtil();
	    
	try {
	    	
	    jacksonUtil.json2Map(strParams);
	    String strClassID = jacksonUtil.getString("classID");
	    String strBatchID = jacksonUtil.getString("batchID");
	    String strPwLists = "";
	    try{
		/*计算选中评委的分布*/
		strPwLists = jacksonUtil.getString("pwIds");
	    }catch(Exception e){
		/*首次进入页面*/
	    }
	    String strCurrentOrgID = tzLoginServiceImpl.cookieOrgId;
	    
	    String strScoreModalSql = "SELECT TZ_MSCJ_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
	    String strScoreModalId = sqlQuery.queryForObject(strScoreModalSql, new Object[] { strClassID }, "String");
	    
	    String strTreeName = "", strJsfs = "";
	    /*String strTreeNameSql = "SELECT TREE_NAME,TZ_JSFS FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=?";
	    List<Map<String, Object>> treeList = sqlQuery.queryForList(strTreeNameSql,
		    new Object[] { strScoreModalId });
	    if (treeList != null && treeList.size() > 0) {
		for (int i = 0; i < treeList.size(); i++) {
		    strTreeName = (String) treeList.get(i).get("TREE_NAME");
		    strJsfs = (String) treeList.get(i).get("TZ_JSFS");
		}
	    }*/
	    String strTreeNameSql = "SELECT TREE_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=?";
	    strTreeName = sqlQuery.queryForObject(strScoreModalSql, new Object[] { strScoreModalId }, "String");
	    
	    String strScoreItemId = "";
	    String strScoreItemSql = "SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? and PARENT_NODE_NUM=0";
	    strScoreItemId = sqlQuery.queryForObject(strScoreItemSql, new Object[] { strTreeName }, "String");
	    
	    // 根据报考班级和批次取得当前轮次;
	    Integer intDqpyLunc = 0;
	    String strDqpyLuncSql = "SELECT ifnull(TZ_DQPY_LUNC,0) FROM PS_TZ_CLPS_GZ_TBL WHERE TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=?";
	    intDqpyLunc = sqlQuery.queryForObject(strDqpyLuncSql, new Object[] { strClassID, strBatchID }, "Integer");

	    String strPwPjfHtml = "";
	    int intFzNum=4;
	    /*if ("Y".equals(strJsfs)) {
		strPwPjfHtml = "{\"col05\":\"平均分\"}";
		intFzNum = intFzNum + 1;
	    }*/
	    strPwPjfHtml = ",{\"col05\":\"平均分\"}";
	    intFzNum = intFzNum + 1;
		
	    String strGridColHTML = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_BASE_HTML", strPwPjfHtml);
	    int intFbzbNum = 1;
	    String strGridGoalColHTML = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBZB_BASE_HTML");
	    
	    String strBlHtml="",strWcHtml="";
	    String strCjModalId="",strFbdzId="";
	    if(strClassID!=null&&!"".equals(strClassID)){
		String strCjModalSql = "SELECT TZ_MSCJ_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
		strCjModalId = sqlQuery.queryForObject(strCjModalSql, new Object[] { strClassID }, "String");
		if(strCjModalId!=null&&!"".equals(strCjModalId)){
		    String strFbdzSql = "SELECT TZ_M_FBDZ_ID FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=? AND TZ_JG_ID=?";
		    strFbdzId = sqlQuery.queryForObject(strFbdzSql, new Object[] { strCjModalId,strCurrentOrgID }, "String");    
		    if(strFbdzId!=null&&!"".equals(strFbdzId)){
			String strFbdzMxId="",strFbdzMxSm="";
			String strFzdzSql = "SELECT TZ_M_FBDZ_MX_ID,TZ_M_FBDZ_MX_SM FROM PS_TZ_FBDZ_MX_TBL WHERE TZ_M_FBDZ_ID=? ORDER BY TZ_M_FBDZ_MX_XH ASC";
			List<Map<String, Object>> fzdzList = sqlQuery.queryForList(strFzdzSql,  new Object[] { strFbdzId });
			if (fzdzList != null && fzdzList.size() > 0) {
			    for(Object obj1:fzdzList){
				Map<String,Object> result1=(Map<String,Object>) obj1;
				strFbdzMxId = result1.get("TZ_M_FBDZ_MX_ID")==null ? "" : String.valueOf(result1.get("TZ_M_FBDZ_MX_ID"));
				strFbdzMxSm = result1.get("TZ_M_FBDZ_MX_SM")==null ? "" : String.valueOf(result1.get("TZ_M_FBDZ_MX_SM"));
				intFzNum = intFzNum + 1;
				
				String colXh = "0" + intFzNum;
				String strFzStr = "col" + this.right(colXh,2);
				strGridColHTML = strGridColHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzStr,strFbdzMxSm);
				
				intFbzbNum = intFbzbNum + 1;
				colXh = "0" + intFbzbNum;
				strFzStr = "col" + this.right(colXh,2);
				strGridGoalColHTML = strGridGoalColHTML + ","  + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzStr,strFbdzMxSm);
				
				String strSql1 = "SELECT TZ_BZFB_BL,TZ_YXWC_NUM FROM PS_TZ_CPFB_BZH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_SCORE_MODAL_ID=? AND TZ_SCORE_ITEM_ID=? AND TZ_M_FBDZ_ID=? AND TZ_M_FBDZ_MX_ID=?";
				List<Map<String, Object>> fbDataList = sqlQuery.queryForList(strSql1, new Object[] { strClassID,strBatchID,strCjModalId,strScoreItemId,strFbdzId,strFbdzMxId });
				String strBl="",strWc="";
				if(fbDataList!=null&&fbDataList.size()>0){
				    for(Object objFb:fbDataList){
					Map<String,Object> resultFb=(Map<String,Object>) objFb;
					strBl = resultFb.get("TZ_BZFB_BL")==null ? "" : String.valueOf(resultFb.get("TZ_BZFB_BL"));
					strWc = resultFb.get("TZ_YXWC_NUM")==null ? "" : String.valueOf(resultFb.get("TZ_YXWC_NUM"));
				    }
				}
				strBlHtml = strBlHtml + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzStr,strBl);
				strWcHtml = strWcHtml + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzStr,strBl);							
			    }
			}			
		    }
		}
		
		String strChartFieldsHTML="";
		String strGridHtml = "";
		String strGridGoalHtml = "";
		String strGridDataHTML="";
		
		String strPwOprid="",strPwDlId="",strFirstName="";
		String strPwSql = "SELECT TZ_PWEI_OPRID,(SELECT TZ_DLZH_ID FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=A.TZ_PWEI_OPRID limit 0,1) TZ_DLZH_ID,(SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL R WHERE R.OPRID=A.TZ_PWEI_OPRID) TZ_REALNAME FROM PS_TZ_CLPS_PW_TBL A WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
		List<Map<String, Object>> pwList = sqlQuery.queryForList(strPwSql,
			    new Object[] { strClassID,strBatchID });
		if (pwList != null && pwList.size() > 0) {
		    int intTb = 0;
		    //为最后一行做准备		
		    int intSize = 1;
		    String strLastGridDataHTML = "";
		    for(Object obj2:pwList){
			Map<String,Object> result2=(Map<String,Object>) obj2;
			strPwOprid = result2.get("TZ_PWEI_OPRID")==null ? "" : String.valueOf(result2.get("TZ_PWEI_OPRID"));
			strPwDlId = result2.get("TZ_DLZH_ID")==null ? "" : String.valueOf(result2.get("TZ_DLZH_ID"));
			strFirstName = result2.get("TZ_PWEI_OPRID")==null ? "" : String.valueOf(result2.get("TZ_REALNAME"));
			
			intFzNum = 1;
			String colName = "0" + intFzNum;
			String strFzValue = "col" + this.right(colName,2);
			
			//登录账号
			strGridDataHTML = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzValue,strPwDlId);
			if(intSize==pwList.size()){
			    strLastGridDataHTML = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzValue,"");
			}			
			//评委姓名
			intFzNum = intFzNum + 1;
			colName = "0" + intFzNum;
			strFzValue = "col" + this.right(colName,2);
			strGridDataHTML = strGridDataHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzValue,strFirstName);
			if(intSize==pwList.size()){
			    strLastGridDataHTML = strLastGridDataHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzValue,"评委（" + strPwLists + "）");
			}
			//完成数量
			String strWc = "0";
			String strWcNumSql = "SELECT COUNT(*) FROM PS_TZ_MP_PW_KS_TBL WHERE TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ? AND TZ_PWEI_OPRID = ?";
			strWc = sqlQuery.queryForObject(strWcNumSql, new Object[] { strClassID,strBatchID,strPwOprid }, "String");
			intFzNum = intFzNum + 1;
			colName = "0" + intFzNum;
			strFzValue = "col" + this.right(colName,2);
			strGridDataHTML = strGridDataHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzValue,strWc);
			if(intSize==pwList.size()){
			    strLastGridDataHTML = strLastGridDataHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzValue,"0");
			}
			//提交状态
			String strSubmitZt="",strSubmitZtDesc="未提交";
			String strSubmitSql = "select TZ_SUBMIT_YN from PS_TZ_MSPWPSJL_TBL A where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_PWEI_OPRID=?";
			strSubmitZt = sqlQuery.queryForObject(strSubmitSql, new Object[] { strClassID,strBatchID,strPwOprid }, "String");
			if("Y".equals(strSubmitZt)){
			    strSubmitZtDesc = "已提交";
			}
			intFzNum = intFzNum + 1;
			colName = "0" + intFzNum;
			strFzValue = "col" + this.right(colName,2);
			strGridDataHTML = strGridDataHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzValue,strSubmitZtDesc);
			if(intSize==pwList.size()){
			    strLastGridDataHTML = strLastGridDataHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzValue,"-");
			}
			//平均分
			intFzNum = intFzNum + 1;
			colName = "0" + intFzNum;
			strFzValue = "col" + this.right(colName,2);
			strGridDataHTML = strGridDataHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzValue,"1.20");
			if(intSize==pwList.size()){
			    strLastGridDataHTML = strLastGridDataHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzValue,"0");
			}
			/*if ("Y".equals(strJsfs)) {
			    intFzNum = intFzNum + 1;
			    colName = "0" + intFzNum;
			    strFzValue = "col" + this.right(colName,2);
			    strGridDataHTML = strGridDataHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzValue,"1.20");
			}*/
			//取得分布统计人数
			String strMFbdzMxId="",strMFbdzMxName="",strMFbdzMxXX="",strMFbdzMxXxJx="",strMFbdzMxSx="",strMFbdzMxSxJx="";
			String strFbdzSql = "SELECT  TZ_M_FBDZ_MX_ID,TZ_M_FBDZ_MX_NAME,TZ_M_FBDZ_MX_XX,TZ_M_FBDZ_MX_XX_JX,TZ_M_FBDZ_MX_SX_JX,TZ_M_FBDZ_MX_SX  FROM PS_TZ_FBDZ_MX_TBL WHERE TZ_M_FBDZ_ID = ? ORDER BY TZ_M_FBDZ_MX_XH ASC";
			List<Map<String, Object>> fbdzList = sqlQuery.queryForList(strFbdzSql,new Object[] { strFbdzId });
			if (fbdzList != null && fbdzList.size() > 0) {
			    for(Object obj3:fbdzList){
				Map<String,Object> result3=(Map<String,Object>) obj3;
				strMFbdzMxId = result3.get("TZ_M_FBDZ_MX_ID")==null ? "" : String.valueOf(result3.get("TZ_M_FBDZ_MX_ID"));
				strMFbdzMxName = result3.get("TZ_M_FBDZ_MX_NAME")==null ? "" : String.valueOf(result3.get("TZ_M_FBDZ_MX_NAME"));
				strMFbdzMxXX = result3.get("TZ_M_FBDZ_MX_XX")==null ? "" : String.valueOf(result3.get("TZ_M_FBDZ_MX_XX"));
				strMFbdzMxXxJx = result3.get("TZ_M_FBDZ_MX_XX_JX")==null ? "" : String.valueOf(result3.get("TZ_M_FBDZ_MX_XX_JX"));
				strMFbdzMxSx = result3.get("TZ_M_FBDZ_MX_SX")==null ? "" : String.valueOf(result3.get("TZ_M_FBDZ_MX_SX"));
				strMFbdzMxSxJx = result3.get("TZ_M_FBDZ_MX_ID")==null ? "" : String.valueOf(result3.get("TZ_M_FBDZ_MX_SX_JX"));			
				
				String strDange = "0";
				/*String numDangeSql = "SELECT COUNT(A.TZ_APP_INS_ID) FROM PS_TZ_CP_PW_KS_TBL A ,PS_TZ_CJX_TBL B ,PS_TZ_KSCLPSLS_TBL C  WHERE A.TZ_SCORE_INS_ID = B.TZ_SCORE_INS_ID  AND A.TZ_CLASS_ID = C.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID = C.TZ_APPLY_PC_ID AND A.TZ_PWEI_OPRID = C.TZ_PWEI_OPRID AND A.TZ_APP_INS_ID = C.TZ_APP_INS_ID AND C.TZ_SUBMIT_YN <> 'C'  AND A.TZ_CLASS_ID = ? AND A.TZ_APPLY_PC_ID = ? AND A.TZ_PWEI_OPRID =? AND B.TZ_SCORE_ITEM_ID = ?  AND C.TZ_CLPS_LUNC = ? AND B.TZ_SCORE_NUM " 
					+ strMFbdzMxXxJx + strMFbdzMxXX + "AND B.TZ_SCORE_NUM " + strMFbdzMxSxJx + strMFbdzMxSx;
				strDange = sqlQuery.queryForObject(numDangeSql, new Object[] { strClassID,strBatchID,strPwOprid,strScoreItemId,intDqpyLunc }, "String");
				*/
				intFzNum = intFzNum + 1;
				colName = "0" + intFzNum;
				strFzValue = "col" + this.right(colName,2);
				strGridDataHTML = strGridDataHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzValue,strDange);
				if(intSize==pwList.size()){
				    strLastGridDataHTML = strLastGridDataHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzValue,"0（0）");
				}
			    }
			}			
			if(strGridHtml!=null&&!"".equals(strGridHtml)){
			    strGridHtml = strGridHtml + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DATA_IT_HTML", strPwOprid,strGridDataHTML);			    
			}else{
			    strGridHtml = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DATA_IT_HTML", strPwOprid,strGridDataHTML);
			}
			if(intSize==pwList.size()){
			    strGridHtml = strGridHtml + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DATA_IT_HTML", "-",strLastGridDataHTML);
			}
			//图表
			intTb = intTb + 1;
			intSize = intSize + 1;
			if(strChartFieldsHTML!=null&&!"".equals(strChartFieldsHTML)){
			    strChartFieldsHTML = strChartFieldsHTML + ",{" + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "pw_field","data" + intTb);
			}else{
			    strChartFieldsHTML = "{" + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "pw_field","data" + intTb);
			}
			strChartFieldsHTML = strChartFieldsHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "pw_name",strFirstName);
			strChartFieldsHTML = strChartFieldsHTML + "}";
		    }
		    int intAllTb = intTb + 1;
		    if(strChartFieldsHTML!=null&&!"".equals(strChartFieldsHTML)){
			strChartFieldsHTML = strChartFieldsHTML + ",{" + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "pw_field","data" + intAllTb);
		    }else{
			strChartFieldsHTML = "{" + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "pw_field","data" + intAllTb);
		    }
		    strChartFieldsHTML = strChartFieldsHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "pw_name","标准曲线");
		    strChartFieldsHTML = strChartFieldsHTML + "}";
		}
		strGridGoalHtml = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_TJ_JSON_HTML","bl","比率",strBlHtml) + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_TJ_JSON_HTML","wc","误差",strWcHtml);;
		strResponse = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_JSON_HTML", strClassID,strBatchID,strGridColHTML,strGridHtml,strChartFieldsHTML,strGridGoalColHTML,strGridGoalHtml,"","");
	    }
	} catch (Exception e) {
	    System.out.println(e.toString());
	}
	return strResponse;
    }
    
    @Override
    public String tzUpdate(String[] actData, String[] errMsg) {
	String strRet = "{}";
	try{
	    String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
	    
	    JacksonUtil jacksonUtil = new JacksonUtil();
	    int num = 0;
	    for (num = 0; num < actData.length; num++) {
		String strForm = actData[num];
		jacksonUtil.json2Map(strForm);
		String strClassID = jacksonUtil.getString("classID");
		String strBatchID = jacksonUtil.getString("batchID");
		//评委可见分布
		String strJudgeTJB = jacksonUtil.getString("judgeTJB");
		String strJudgeFBT = jacksonUtil.getString("judgeFBT");
		//启动或关闭
		String buttonStartClicked = jacksonUtil.getString("buttonStartClicked");
		String buttonEndClicked = jacksonUtil.getString("buttonEndClicked");
		
		String strUpdateSql = "UPDATE PS_TZ_MSPS_GZ_TBL SET TZ_DQPY_ZT=? WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
		if("1".equals(buttonStartClicked)){
		    sqlQuery.update(strUpdateSql, new Object[]{"A",strClassID,strBatchID});
		}
		if("2".equals(buttonEndClicked)){
		    sqlQuery.update(strUpdateSql, new Object[]{"B",strClassID,strBatchID});
		}
		
		
		// 评委信息内容;
		List<?> judgeInfoUtil = jacksonUtil.getList("judgeInfoUpdate");		
		if(judgeInfoUtil!=null&&judgeInfoUtil.size()>0){
        		for (Object obj : judgeInfoUtil) {
        		    Map<String, Object> mapFormData = (Map<String, Object>) obj;
        		    String strJudgeID = String.valueOf(mapFormData.get("judgeID"));
        		    
        		    String strSubmitYN = String.valueOf(mapFormData.get("submitYN"));
        		    String accountStatus = String.valueOf(mapFormData.get("accountStatus"));
        		    
        		    String strPwOprid = sqlQuery.queryForObject("SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=?", new Object[]{strJudgeID}, "String");
        		    //更改账户状态
        		    String strUpdateSql2 = "UPDATE PS_TZ_MSPS_PW_TBL SET TZ_PWEI_ZHZT=? WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=?";
        		    sqlQuery.update(strUpdateSql2,new Object[]{accountStatus,strClassID,strBatchID,strPwOprid});
        		    
        		    //更改评审记录提交状态
        		    String strExist="";
        		    String strExistSql = "SELECT 'Y' FROM PS_TZ_MSPWPSJL_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=?";
        		    strExist = sqlQuery.queryForObject(strExistSql, new Object[] { strClassID,strBatchID,strPwOprid },"String");
        		    psTzMspwpsjlTbl psTzMspwpsjlTbl = new psTzMspwpsjlTbl();
        		    psTzMspwpsjlTbl.setTzClassId(strClassID);
        		    psTzMspwpsjlTbl.setTzApplyPcId(strBatchID);        		    
        		    psTzMspwpsjlTbl.setTzPweiOprid(strPwOprid);
        		    psTzMspwpsjlTbl.setRowLastmantDttm(new Date());
        		    psTzMspwpsjlTbl.setRowLastmantOprid(oprid);
        		    if(strSubmitYN==null||"".equals(strSubmitYN)){
        			strSubmitYN = "N";
        		    }
        		    psTzMspwpsjlTbl.setTzSubmitYn(strSubmitYN);		    
        		    if("Y".equals(strExist)){
        			psTzMspwpsjlTblMapper.updateByPrimaryKey(psTzMspwpsjlTbl);
        		    }else{
        			psTzMspwpsjlTbl.setRowAddedDttm(new Date());
        			psTzMspwpsjlTbl.setRowAddedOprid(oprid);
        			psTzMspwpsjlTblMapper.insert(psTzMspwpsjlTbl);
        		    }        		    
        		}
		}
		// 评委偏差内容;
		List<?> studentUtil = jacksonUtil.getList("studentInfo");		
		if(studentUtil!=null&&studentUtil.size()>0){
		    for(Object stuObj:studentUtil){
			Map<String,Object> stuMap = (Map<String,Object>) stuObj;
			String strAppInsId = String.valueOf(stuMap.get("appInsId"));
			String strPwePc = String.valueOf(stuMap.get("pweiPC"));
			Double douPwiPc = Double.valueOf(strPwePc);
			String strUpdateSQl3 = "UPDATE PS_TZ_MSPS_KSH_TBL SET TZ_MSPS_PWJ_PC=? WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=?";
			sqlQuery.update(strUpdateSQl3, new Object[]{douPwiPc,strClassID,strBatchID,strAppInsId});			    
		    }
		}
		//评委可见指标信息
		if(!"Y".equals(strJudgeFBT)){
		    strJudgeFBT = "N";
		}
		if(!"Y".equals(strJudgeTJB)){
		    strJudgeTJB = "N";
		}
		PsTzMsPsGzTbl psTzMsPsGzTbl = new PsTzMsPsGzTbl();
		psTzMsPsGzTbl.setTzClassId(strClassID);
		psTzMsPsGzTbl.setTzApplyPcId(strBatchID);        		    
		psTzMsPsGzTbl.setTzPwkjFbt(strJudgeFBT);
		psTzMsPsGzTbl.setTzPwkjTjb(strJudgeTJB);
		psTzMsPsGzTblMapper.updateByPrimaryKeySelective(psTzMsPsGzTbl);
	    }
	}catch(Exception e){
	    e.printStackTrace();
	    errMsg[0] = "1";
	    errMsg[1] = e.toString();
	}
	return strRet;
    }
    
    @Override
    public String tzOther(String strOprType,String strParams, String[] errMsg) {
	String strResponse = "\"failure\"";
	JacksonUtil jacksonUtil = new JacksonUtil();
	try {
	    jacksonUtil.json2Map(strParams);
	    String strType = jacksonUtil.getString("type");

	    switch(strType){
	    	case "calculate":
	    	    //计算偏差
	    	    strResponse = this.calculate(strParams, errMsg);
	    	    break;
	    	case "startClick":
	    	    //开启新一轮评审
	    	    strResponse = this.btnClick(strParams, errMsg);	    	    
	    	    break;
	    	case "finishClick":
	    	    //关闭评审
	    	    strResponse = this.btnClick(strParams, errMsg);	    	    
	    	    break;
	    	case "rjd":
	    	    //撤销评议数据
	    	    strResponse = this.removeJudgeData(strParams, errMsg);
	    	    break;
	    	case "IFP":
	    	    //保存评议数据
	    	    //strResponse = this.savePyData(strParams, errMsg);
	    	    break;
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    errMsg[0] = "100";
	    errMsg[1] = e.toString();
	}
	return strResponse;
    }
    
    public String calculate(String strParams, String[] errMsg) {
	String strResponse = "\"failure\"";
	JacksonUtil jacksonUtil = new JacksonUtil();
	String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
	
	try {
	    String strCurrentOrg = tzLoginServiceImpl.getLoginedManagerOrgid(request);
	    
	    jacksonUtil.json2Map(strParams);
	    String strClassID = jacksonUtil.getString("classID");
	    String strBatchID = jacksonUtil.getString("batchID");
	    
	    String strContent = "";
	    List<?> listAppID = jacksonUtil.getList("appID");
	    
	    if(listAppID!=null&&listAppID.size()>0){
		String strScoreModalID = "";
		String strSql1 = "SELECT TZ_ZLPS_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
		strScoreModalID = sqlQuery.queryForObject(strSql1, new Object[] { strClassID }, "String");
		
		String strTreeName="";
		String strSql2 = "SELECT TREE_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=?";
		strTreeName = sqlQuery.queryForObject(strSql2,  new Object[] { strScoreModalID }, "String");		
		
		String strScoreItemID = "";
		String strScoreItemSql = "SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? and PARENT_NODE_NUM=0";
		strScoreItemID = sqlQuery.queryForObject(strScoreItemSql, new Object[] { strTreeName }, "String");
		
		String strTreeNode="";
		String strSql3 = "SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? AND PARENT_NODE_NUM=0";
		strTreeNode = sqlQuery.queryForObject(strSql3, new Object[] { strTreeName }, "String");
		
		String strDeleteSql = "DELETE FROM PS_TZ_PW_KS_PC_TBL";
		sqlQuery.update(strDeleteSql);
		
		for(Object pwObj:listAppID){
		    ArrayList<String> mapScore = new ArrayList();
		    String strAppInsID = String.valueOf(pwObj);
		    		    
		    int numTotal = 0;
		    String strListSql1 = "SELECT DISTINCT TZ_PWEI_OPRID FROM PS_TZ_CP_PW_KS_TBL WHERE TZ_APPLY_PC_ID = ? AND TZ_APP_INS_ID = ?";
		    List<Map<String, Object>> mapList1 = sqlQuery.queryForList(strListSql1, new Object[] { strBatchID,strAppInsID });
		    
		    if(mapList1!=null&&mapList1.size()>0){
			int pw_num = 1;
			for(Object mapObj:mapList1){
			    Map<String,Object> result=(Map<String,Object>) mapObj;
			    String str_PwOprid = result.get("TZ_PWEI_OPRID")==null?"":String.valueOf(result.get("TZ_PWEI_OPRID"));
			    
			    //获取当前班级批次下的轮次
			    String strSql12 = "SELECT TZ_DQPY_LUNC FROM PS_TZ_CLPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
			    Integer numLunc = sqlQuery.queryForObject(strSql12, new Object[] { strClassID, strBatchID }, "Integer");
			    if(numLunc==null){
				numLunc = 0;
			    }
			    
			    String strMapSql = "SELECT TZ_SUBMIT_YN FROM PS_TZ_KSCLPSLS_TBL WHERE TZ_APPLY_DIRC_ID = ? AND TZ_APP_INS_ID = ? AND TZ_PWEI_OPRID = ? AND TZ_CLPS_LUNC = ?";
			    String strSubmitStatus = sqlQuery.queryForObject(strMapSql, new Object[]{strBatchID,strAppInsID,str_PwOprid,numLunc}, "String");
			    if(!"C".equals(strSubmitStatus)){
				String strListSql2 = "SELECT B.TZ_SCORE_MODAL_ID,A.TZ_SCORE_INS_ID,A.TZ_SCORE_ITEM_ID,A.TZ_SCORE_NUM,TZ_SCORE_PY_VALUE FROM PS_TZ_CP_PW_KS_TBL C JOIN PS_TZ_CJX_TBL A ON A.TZ_SCORE_INS_ID=C.TZ_SCORE_INS_ID JOIN PS_TZ_SRMBAINS_TBL B ON A.TZ_SCORE_INS_ID=B.TZ_SCORE_INS_ID WHERE C.TZ_CLASS_ID=? AND C.TZ_APPLY_PC_ID=? AND C.TZ_APP_INS_ID=? AND C.TZ_PWEI_OPRID=?";
				List<Map<String, Object>> mapList2 = sqlQuery.queryForList(strListSql2, new Object[] { strClassID,strBatchID,strAppInsID, str_PwOprid});
				if(mapList2!=null&&mapList2.size()>0){
				    for(Object sObj:mapList2){
					Map<String,Object> resultMap = (Map<String,Object>) sObj;
					String strScoreItemId = String.valueOf(resultMap.get("TZ_SCORE_ITEM_ID"));
					String strScoreNum = resultMap.get("TZ_SCORE_NUM")==null?"0":String.valueOf(resultMap.get("TZ_SCORE_NUM"));
					String strScorePyValue = String.valueOf(resultMap.get("TZ_SCORE_PY_VALUE"));
					
					String strMap2Sql = "SELECT TZ_ITEM_S_TYPE FROM PS_TZ_CJ_BPH_TBL WHERE TZ_JG_ID=? AND TZ_SCORE_MODAL_ID=? AND TZ_SCORE_ITEM_ID=?";
					String strItemType = sqlQuery.queryForObject(strMap2Sql, new Object[]{strCurrentOrg,strScoreModalID,strScoreItemId}, "String");
					if("C".equals(strItemType)){
					    
					}else{
					    
					}
					pw_num = pw_num + 1;
					String strInsertSql = "INSERT INTO PS_TZ_PW_KS_PC_TBL VALUES(" + pw_num + ",'" + strScoreNum + "')";
					sqlQuery.update(strInsertSql);
				    }
				    
				}
			    }
			}
		    }
		    
		    //偏差
		    String strPianChaSql = "SELECT stddev(TZ_SCORE_NUM) FROM PS_TZ_PW_KS_PC_TBL";
		    Double doublePianCha = sqlQuery.queryForObject(strPianChaSql, "Double");
		    if(doublePianCha==null){
			doublePianCha = 0.0;
		    }
		    /*String strExistsSql = "SELECT 'Y' FROM PS_TZ_CLPS_KSH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=?";
		    String strExFlg = sqlQuery.queryForObject(strExistsSql, new Object[]{strClassID,strBatchID,strAppInsID}, "String");
		    PsTzClpsKshTbl psTzClpsKshTbl = new PsTzClpsKshTbl();
		    psTzClpsKshTbl.setTzClassId(strClassID);
		    psTzClpsKshTbl.setTzApplyPcId(strBatchID);
		    psTzClpsKshTbl.setTzAppInsId(Long.parseLong(strAppInsID));
		    psTzClpsKshTbl.setTzClpsPwjPc(BigDecimal.valueOf(doublePianCha));
		    psTzClpsKshTbl.setRowLastmantDttm(new Date());
		    psTzClpsKshTbl.setRowLastmantOprid(oprid);
		    if("Y".equals(strExFlg)){
			PsTzClpsKshTblMapper.updateByPrimaryKeySelective(psTzClpsKshTbl);
		    }else{
			psTzClpsKshTbl.setRowAddedDttm(new Date());
			psTzClpsKshTbl.setRowAddedOprid(oprid);
			PsTzClpsKshTblMapper.insert(psTzClpsKshTbl);
		    }
		    */
		    if("".equals(strContent)){
			strContent = "{\"appInsID\":\"" + strAppInsID + "\",\"standardDeviation\":\"" + doublePianCha + "\"}";
		    }else{
			strContent = strContent + ",{\"appInsID\":\"" + strAppInsID + "\",\"standardDeviation\":\"" + doublePianCha + "\"}";
		    }		    		   
		}
	    }
	    
	    strResponse = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_BASE_JSON_HTML", String.valueOf(listAppID.size()),strContent);
	    
	}catch(Exception e){
	    errMsg[0] = "100";
	    errMsg[1] = e.toString();
	}
	return strResponse;
    }
    public String btnClick(String strParams, String[] errMsg) {
	String strResponse = "\"failure\"";
	JacksonUtil jacksonUtil = new JacksonUtil();
	String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
	
	try {
	    jacksonUtil.json2Map(strParams);
	    String strClassID = jacksonUtil.getString("classID");
	    String strBatchID = jacksonUtil.getString("batchID");	    
	    
	    String strBtnType = jacksonUtil.getString("type");
	    
	    String strExistsSql = "SELECT 'Y' FROM PS_TZ_MSPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
    	    String strExistsFlg = sqlQuery.queryForObject(strExistsSql, new Object[]{strClassID,strBatchID},"String");
    	    PsTzMsPsGzTbl psTzMspsGzTbl = new PsTzMsPsGzTbl();
    	    
	    switch(strBtnType){
	    	case "startClick":
	    	    //启动评审
	    	    
	    	    //是否合法，评委数是否为0，评委是否有分组,评委数量是否达到人次要求(未做)
	    	    String isLegal="",isZero="",isGrp="";
	    	    String strSql1 = "SELECT (CASE WHEN A.PW_NUM-A.NUM>0 THEN 1 ELSE 0 END) IS_lEGAL,(CASE WHEN A.NUM>0 THEN 1 ELSE 0 END) IS_ZERO,(CASE WHEN A.PW_GRP>0 THEN 1 ELSE 0 END) IS_GRP FROM (SELECT TZ_MSPY_NUM NUM,(SELECT COUNT(*) FROM PS_TZ_MSPS_PW_TBL) PW_NUM,(SELECT COUNT(*) FROM PS_TZ_MSPS_PW_TBL WHERE TZ_PWEI_GRPID <> ' ') PW_GRP FROM PS_TZ_MSPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?) A";
	    	    Map<String,Object> sMap = sqlQuery.queryForMap(strSql1, new Object[]{strClassID,strBatchID});
	    	    if(sMap!=null){
	    		isLegal = sMap.get("IS_lEGAL")==null?"":String.valueOf(sMap.get("IS_lEGAL"));
	    		isZero =  sMap.get("IS_ZERO")==null?"":String.valueOf(sMap.get("IS_ZERO"));
	    		isGrp = sMap.get("IS_GRP")==null?"":String.valueOf(sMap.get("IS_GRP"));
	    	    }
	    	    
	    	    if("1".equals(isLegal)&&"1".equals(isZero)&&"1".equals(isGrp)){
	    		//修改评审状态为开启
	    		psTzMspsGzTbl.setTzClassId(strClassID);
	    		psTzMspsGzTbl.setTzApplyPcId(strBatchID);
	    		psTzMspsGzTbl.setTzDqpyZt("A");
	    		if("Y".equals(strExistsFlg)){
	    		    psTzMsPsGzTblMapper.updateByPrimaryKeySelective(psTzMspsGzTbl);
	    		}else{
	    		    psTzMsPsGzTblMapper.insertSelective(psTzMspsGzTbl);
	    		}
	    		strResponse = "{\"isPass\":\"Y\",\"status\":\"A\"}";
	    	    }else{
	    		strResponse = "{\"isPass\":\"N\"}";
	    	    }	   
	    	    break;
	    	case "finishClick":
	    	    //关闭本轮评审
    	    	    psTzMspsGzTbl.setTzClassId(strClassID);
    	    	    psTzMspsGzTbl.setTzApplyPcId(strBatchID);
    	    	    psTzMspsGzTbl.setTzDqpyZt("B");
    	    	    if("Y".equals(strExistsFlg)){
    	    		psTzMsPsGzTblMapper.updateByPrimaryKeySelective(psTzMspsGzTbl);
    	    	    }else{
    	    		psTzMsPsGzTblMapper.insertSelective(psTzMspsGzTbl);
    	    	    }
	    	    strResponse = "{\"status\":\"B\"}";
	    	    break;
	    }
	    errMsg[0] = "0";	    
	}catch(Exception e){
	    System.out.println(e.toString());
	    errMsg[0] = "1";
	    errMsg[1] = e.toString();
	}
	return strResponse;
    }
    
    public String removeJudgeData(String strParams, String[] errMsg) {
	String strResponse = "";
	JacksonUtil jacksonUtil = new JacksonUtil();
	String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
	
	try {
	    String strCurrentOrg = tzLoginServiceImpl.getLoginedManagerOrgid(request);
	    
	    jacksonUtil.json2Map(strParams);
	    
	    List<?> removeList = jacksonUtil.getList("remove");

	    if(removeList!=null&&removeList.size()>0){
		for (Object obj : removeList) {
		    Map<String, Object> mapFormData = (Map<String, Object>) obj;
		    
		    String strClassID = mapFormData.get("classID")==null?"":String.valueOf(mapFormData.get("classID"));
		    String strBatchID = mapFormData.get("batchID")==null?"":String.valueOf(mapFormData.get("batchID"));
		    String strJudgeId = mapFormData.get("judgeID")==null?"":String.valueOf(mapFormData.get("judgeID"));
		    //获取当前评委的OPRID
		    String strSql1 = "SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=?";
		    String strJudgeID = sqlQuery.queryForObject(strSql1, new Object[] { strJudgeId, strCurrentOrg }, "String");
		    //将评委考生记录表中的数据设置为撤销
		    String strUpdateSql1 = "UPDATE PS_TZ_MSPWPSJL_TBL SET TZ_SUBMIT_YN='C' WHERE TZ_APPLY_PC_ID=? AND TZ_CLASS_ID=? AND TZ_PWEI_OPRID=?";
		    sqlQuery.update(strUpdateSql1, new Object[]{strBatchID,strClassID,strJudgeID});
		    
		    //评委列表
		    String strSql2 = "SELECT TZ_MSPW_LIST,TZ_APP_INS_ID FROM PS_TZ_MSPSKSPW_TBL WHERE TZ_APPLY_PC_ID = ? AND TZ_APP_INS_ID = ?";
		    List<Map<String, Object>> mapList = sqlQuery.queryForList(strSql2, new Object[] { strClassID,strBatchID });
		    if(mapList!=null&&mapList.size()>0){
			for(Object pwObj:mapList){
			    Map<String,Object> result=(Map<String,Object>) pwObj;
			    String strJudgeList = result.get("TZ_MSPW_LIST")==null ? "" : String.valueOf(result.get("TZ_MSPW_LIST"));
			    String strAppInsId = result.get("TZ_APP_INS_ID")==null ? "" : String.valueOf(result.get("TZ_APP_INS_ID"));
			    //从所有当前班级和批次的考生的评委列表中查找出当前评委并删除
			    if(strJudgeList!=null&&!"".equals(strJudgeList)){
				if(strJudgeList.contains(strJudgeId + ",")){
				    strJudgeList.replace(strJudgeId + ",", "");
				}else if(strJudgeList.contains("," + strJudgeId)){
				    strJudgeList.replace("," + strJudgeId, "");
				}else{
				    strJudgeList.replace(strJudgeId, "");
				}
				String strUpdateSql = "UPDATE PS_TZ_MSPSKSPW_TBL SET TZ_MSPW_LIST='" + strJudgeList + "' WHERE TZ_APPLY_PC_ID='" + strBatchID + "' AND TZ_CLASS_ID='" + strClassID + "' AND TZ_APP_INS_ID='" + strAppInsId + "'";
				sqlQuery.update(strUpdateSql);
			    }
			}
		    }
		}
	    }
	    errMsg[0] = "0";
	}catch(Exception e){
	    errMsg[0] = "100";
	    errMsg[1] = e.toString();
	}
	return strResponse;
    }
    
    public String right(String strValue,int num){
	String returnValue = "";
	try{
	    if(strValue!=null&&!"".equals(strValue)){
		if(num>0){
		    int strLen = strValue.length();
		    int beginIndex = strLen - num;
		    if(beginIndex < 0){
			beginIndex = 0;
		    }
		    returnValue = strValue.substring(beginIndex);		    
		}
	    }
	}catch(Exception e){
	    /**/
	}
	return returnValue;
    }
    
    public int find(String[] arr,String strValue){
	int index = -1;
	if(arr!=null&&arr.length>0){
	    for(int i=0;i<arr.length;i++){
		String tmpValue = arr[i];
		if(strValue.equals(tmpValue)){
		    return i;
		}
	    }
	}
	return index;
    }
}
