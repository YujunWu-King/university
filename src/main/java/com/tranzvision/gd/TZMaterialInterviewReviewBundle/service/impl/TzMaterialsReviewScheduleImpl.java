package com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 材料评审进度，原TZ_GD_CLPS_PKG:TZ_GD_SCHE_CLS
 * 
 * @author yuds 时间：2017年2月28日
 */
@Service("com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl.TzMaterialsReviewScheduleImpl")
public class TzMaterialsReviewScheduleImpl extends FrameworkImpl {
    @Autowired
    private SqlQuery sqlQuery;
    @Autowired
    private GetSysHardCodeVal getSysHardCodeVal;
    @Autowired
    private TZGDObject tzGdObject;
    
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
    @Override
    public String tzQuery(String strParams, String[] errMsg) {
	String strResponse = "\"failure\"";
	JacksonUtil jacksonUtil = new JacksonUtil();
	try {
	    jacksonUtil.json2Map(strParams);
	    String strClassID = jacksonUtil.getString("classID");
	    String strBatchID = jacksonUtil.getString("batchID");
	    String strCurrentOrg = "SEM";

	    String strScoreModalSql = "SELECT TZ_ZLPS_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
	    String strScoreModalId = sqlQuery.queryForObject(strScoreModalSql, new Object[] { strClassID },
			"String");
	    // 当前班级报考人数
	    String strTotalStudentSql = "SELECT COUNT(*) FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID=?";
	    String strTotalStudentCount = sqlQuery.queryForObject(strTotalStudentSql, new Object[] { strClassID },
			"String");
	    // 当前批次人数
	    String strCurBatchStuSql = "SELECT COUNT(*) FROM PS_TZ_CLPS_KSH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
	    String strCurBatchStuCount = sqlQuery.queryForObject(strCurBatchStuSql,
		    new Object[] { strClassID, strBatchID }, "String");

	    // 状态以及日期相关信息
	    String strStatusSql = "SELECT TZ_PYKS_RQ,TZ_PYKS_SJ,TZ_PYJS_RQ,TZ_PYJS_SJ,(CASE TZ_DQPY_ZT WHEN 'A' THEN '进行中' WHEN 'B' THEN '已关闭' WHEN 'N' THEN '未开始' ELSE '未开始' END) STATUS,TZ_DQPY_LUNC,TZ_PWKJ_TJB,TZ_PWKJ_FBT FROM PS_TZ_CLPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";

	    String strStatus = "未开始", strDelibCount = "0";
	    List<Map<String, Object>> list = sqlQuery.queryForList(strStatusSql,
			new Object[] { strClassID, strBatchID });
	    if (list != null && list.size() > 0) {
		for (int i = 0; i < list.size(); i++) {
		    strStatus = (String) list.get(i).get("STATUS");
		    strDelibCount = String.valueOf(list.get(i).get("TZ_DQPY_LUNC"));
		}
	    }
	    /* 获取当前批次下考生的数量 */
	    String sql10 = "SELECT COUNT(*) FROM PS_TZ_CLPS_KSH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
	    Integer materialStudents = sqlQuery.queryForObject(sql10, new Object[] { strClassID, strBatchID },
		"Integer");

	    String strTotalSql = "SELECT ifnull(TZ_MSPY_NUM,0) FROM PS_TZ_CLPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
	    Integer judgeCount = sqlQuery.queryForObject(strTotalSql, new Object[] { strClassID, strBatchID },
		"Integer");
	    if (judgeCount == null) {
		judgeCount = 0;
	    }
	    Integer numtotal = materialStudents * judgeCount;
	    // 得出当前班级，当前批次，当前轮次的已评审人次数
	    String strClpsCountSql = "SELECT COUNT(*) FROM ps_TZ_KSCLPSLS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_SUBMIT_YN='Y' AND TZ_CLPS_LUNC=?";
	    String strClpsCount = sqlQuery.queryForObject(strClpsCountSql,
		    new Object[] { strClassID, strBatchID, strDelibCount }, "String");
	    String strProgress = strClpsCount + "/" + numtotal;

	    Map<String, Object> mapData = new HashMap<String, Object>();
	    mapData.put("classID", strClassID);
	    mapData.put("batchID", strBatchID);
	    mapData.put("totalStudents", strTotalStudentCount);
	    mapData.put("materialStudents", strCurBatchStuCount);
	    mapData.put("status", strStatus);
	    mapData.put("progress", strProgress);
	    mapData.put("delibCount", strDelibCount);
	    mapData.put("reviewCount", judgeCount);
	    //要求评审人次
	    mapData.put("requiredCount", numtotal);
	    strResponse = jacksonUtil.Map2json(mapData);	   
	} catch (Exception e) {
	    e.printStackTrace();
	    errMsg[0] = "100";
	    errMsg[1] = e.toString();
	}
	return strResponse;
    }

    public String tzOther(String strParams, String[] errMsg) {
	String strResponse = "\"failure\"";
	JacksonUtil jacksonUtil = new JacksonUtil();
	try {
	    jacksonUtil.json2Map(strParams);
	    String strType = jacksonUtil.getString("type");

	    if ("isJiSuanFenZhi".equals(strType)) {
		strResponse = this.isJiSuanFenZhi(strParams, errMsg);
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	    errMsg[0] = "100";
	    errMsg[1] = e.toString();
	}
	return strResponse;
    }

    public String isJiSuanFenZhi(String strParams, String[] errMsg) {
	String strResponse = "\"failure\"";
	JacksonUtil jacksonUtil = new JacksonUtil();
	try {
	    jacksonUtil.json2Map(strParams);
	    String strClassID = jacksonUtil.getString("classID");
	    String strBatchID = jacksonUtil.getString("batchID");
	    String strCurrentOrg = "SEM";

	    String strScoreModalSql = "SELECT TZ_ZLPS_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
	    String strScoreModalId = sqlQuery.queryForObject(strScoreModalSql, new Object[] { strClassID }, "String");

	    String strTreeSql = "SELECT TREE_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=?";
	    String strTreeName = sqlQuery.queryForObject(strTreeSql, new Object[] { strScoreModalId }, "String");

	    // String strTreeNodeSql = "SELECT TREE_NODE FROM PSTREENODE WHERE
	    // TREE_NAME=? and PARENT_NODE_NUM=0";
	    // String strScoreItemId = sqlQuery.queryForObject(strTreeNodeSql,
	    // new Object[] { strTreeName }, "String");

	    String strZfzSql = "select ifnull(A.TZ_JSFS,0) from ps_TZ_RS_MODAL_TBL A,ps_TZ_BPH_VW1 b where A.TREE_NAME=? and A.TZ_SCORE_MODAL_ID=B.TZ_SCORE_MODAL_ID AND B.TZ_SCORE_MODAL_ID=? AND ROWNUM=1";
	    String strZfz = sqlQuery.queryForObject(strZfzSql, new Object[] { strTreeName, strScoreModalId }, "String");

	    Map<String, Object> mapData = new HashMap<String, Object>();
	    mapData.put("ZFZ", strZfz);
	    strResponse = jacksonUtil.Map2json(mapData);

	} catch (Exception e) {
	    e.printStackTrace();
	    errMsg[0] = "100";
	    errMsg[1] = e.toString();
	}
	return strResponse;
    }

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
	    String strCurrentOrgID = "SEM";
	    
	    String strScoreModalSql = "SELECT TZ_ZLPS_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
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
		String strCjModalSql = "SELECT TZ_ZLPS_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
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
			String strWcNumSql = "SELECT COUNT(*) FROM PS_TZ_KSCLPSLS_TBL WHERE TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ? AND TZ_PWEI_OPRID = ? AND TZ_CLPS_LUNC=? AND TZ_SUBMIT_YN <> 'C'";
			strWc = sqlQuery.queryForObject(strWcNumSql, new Object[] { strClassID,strBatchID,strPwOprid,intDqpyLunc }, "String");
			intFzNum = intFzNum + 1;
			colName = "0" + intFzNum;
			strFzValue = "col" + this.right(colName,2);
			strGridDataHTML = strGridDataHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzValue,strWc);
			if(intSize==pwList.size()){
			    strLastGridDataHTML = strLastGridDataHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzValue,"0");
			}
			//提交状态
			String strSubmitZt="",strSubmitZtDesc="未提交";
			String strSubmitSql = "SELECT TZ_SUBMIT_YN FROM PS_TZ_CLPWPSLS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=? AND TZ_CLPS_LUNC=?";
			strSubmitZt = sqlQuery.queryForObject(strSubmitSql, new Object[] { strClassID,strBatchID,strPwOprid,intDqpyLunc }, "String");
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
				String numDangeSql = "SELECT COUNT(A.TZ_APP_INS_ID) FROM PS_TZ_CP_PW_KS_TBL A ,PS_TZ_CJX_TBL B ,PS_TZ_KSCLPSLS_TBL C  WHERE A.TZ_SCORE_INS_ID = B.TZ_SCORE_INS_ID  AND A.TZ_CLASS_ID = C.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID = C.TZ_APPLY_PC_ID AND A.TZ_PWEI_OPRID = C.TZ_PWEI_OPRID AND A.TZ_APP_INS_ID = C.TZ_APP_INS_ID AND C.TZ_SUBMIT_YN <> 'C'  AND A.TZ_CLASS_ID = ? AND A.TZ_APPLY_PC_ID = ? AND A.TZ_PWEI_OPRID =? AND B.TZ_SCORE_ITEM_ID = ?  AND C.TZ_CLPS_LUNC = ? AND B.TZ_SCORE_NUM " 
					+ strMFbdzMxXxJx + strMFbdzMxXX + "AND B.TZ_SCORE_NUM " + strMFbdzMxSxJx + strMFbdzMxSx;
				strDange = sqlQuery.queryForObject(numDangeSql, new Object[] { strClassID,strBatchID,strPwOprid,strScoreItemId,intDqpyLunc }, "String");
				
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
		}
		strGridGoalHtml = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_TJ_JSON_HTML","bl","比率",strBlHtml) + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_TJ_JSON_HTML","wc","误差",strWcHtml);;
		strResponse = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_JSON_HTML", strClassID,strBatchID,strGridColHTML,strGridHtml,strChartFieldsHTML,strGridGoalColHTML,strGridGoalHtml);
	    }
	} catch (Exception e) {
	    System.out.println(e.toString());
	}
	return strResponse;
    }

    public String queryJudgeInfo(String strParams, String[] errMsg) {
	String strResponse = "";
	JacksonUtil jacksonUtil = new JacksonUtil();
	try {
	    jacksonUtil.json2Map(strParams);
	    String strClassID = jacksonUtil.getString("classID");
	    String strBatchID = jacksonUtil.getString("batchID");
	    String strCurrentOrg = "ADMIN";

	    String strScoreModalSql = "SELECT TZ_ZLPS_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
	    String strScoreModalId = sqlQuery.queryForObject(strScoreModalSql, new Object[] { strClassID }, "String");
	    
	    String strTreeName = "";
	    String strTreeNameSql = "SELECT TREE_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=?";
	    strTreeName = sqlQuery.queryForObject(strScoreModalSql, new Object[] { strScoreModalId }, "String");
	    
	    String strScoreItemId = "";
	    String strScoreItemSql = "SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? and PARENT_NODE_NUM=0";
	    strScoreItemId = sqlQuery.queryForObject(strScoreItemSql, new Object[] { strTreeName }, "String");

	    String strJudgeAccount="",strJudgeAccountID="",strJudgeStatus="",strJudgeGroup="",strJudgeStuSX="",strJugeStuXX="",strJudgeDescript="",strSubmitYN="",strTotalNum="0";
	    String strPwSql = "SELECT TZ_PWEI_OPRID,TZ_PWEI_ZHZT,TZ_PWZBH,TZ_PYKS_XX FROM PS_TZ_CLPS_PW_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
	    List<Map<String, Object>> pwList = sqlQuery.queryForList(strPwSql, new Object[] { strClassID,strBatchID });
	    if (pwList != null && pwList.size() > 0) {
    	    	for(Object obj:pwList){
    	    	    Map<String,Object> result=(Map<String,Object>) obj;
    	    	    strJudgeAccount = result.get("TZ_PWEI_OPRID")==null ? "" : String.valueOf(result.get("TZ_PWEI_OPRID"));
    	    	    strJudgeStatus = result.get("TZ_PWEI_ZHZT")==null ? "" : String.valueOf(result.get("TZ_PWEI_ZHZT"));
    	    	    strJudgeGroup = result.get("TZ_PWZBH")==null ? "" : String.valueOf(result.get("TZ_PWZBH"));
    	    	    strJugeStuXX = result.get("TZ_PYKS_XX")==null ? "" : String.valueOf(result.get("TZ_PYKS_XX"));    	
		    
		    // 根据报考班级和批次取得当前轮次;
		    Integer intDqpyLunc = 0;
		    String strDqpyLuncSql = "SELECT ifnull(TZ_DQPY_LUNC,0) FROM PS_TZ_CLPS_GZ_TBL WHERE TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=?";
		    intDqpyLunc = sqlQuery.queryForObject(strDqpyLuncSql, new Object[] { strClassID, strBatchID }, "Integer");
		    
		    String strSubSql = "SELECT TZ_SUBMIT_YN FROM PS_TZ_CLPWPSLS_TBL WHERE TZ_APPLY_PC_ID=? AND TZ_CLASS_ID=? AND TZ_PWEI_OPRID=? AND TZ_CLPS_LUNC=?";
		    strSubmitYN = sqlQuery.queryForObject(strSubSql, new Object[] { strBatchID,strClassID,strJudgeAccount,intDqpyLunc }, "String");
		    
		    String strPwNameSql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_JG_ID=? AND OPRID=?";
		    String strJudgeName = sqlQuery.queryForObject(strPwNameSql, new Object[] { strCurrentOrg,strJudgeAccount }, "String");
		    //考生材料评审历史表（里面记录了各个轮次评委的打分情况）
		    String strSql1 = "SELECT count(*) FROM PS_TZ_KSCLPSLS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_SUBMIT_YN='Y' AND TZ_CLPS_LUNC=? AND TZ_PWEI_OPRID=?";
		    String strHasSubmited = sqlQuery.queryForObject(strSql1, new Object[] { strClassID,strBatchID,intDqpyLunc,strJudgeAccount }, "String");
		    
		    String strPwIdSql = "SELECT TZ_DLZH_ID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_JG_ID=? AND OPRID=?";
		    strJudgeAccountID = sqlQuery.queryForObject(strPwIdSql, new Object[] { strCurrentOrg,strJudgeAccount }, "String");
		    
		    String strSql2 = "SELECT COUNT(*) FROM PS_TZ_CP_PW_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=? ";
		    String strNeedSubmit = sqlQuery.queryForObject(strSql2, new Object[] { strClassID,strBatchID,strJudgeAccount }, "String");
		    
		    String strSubmited = strNeedSubmit + "/" + strHasSubmited;
		    
		    if(strResponse!=null&&!"".equals(strResponse)){
			strResponse = strResponse + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_CLPS_PWINFO_HTML", strClassID,strBatchID,strJudgeAccountID,strJudgeName,strJudgeGroup,strSubmitYN,strJudgeStatus,strSubmited,strJudgeStuSX,strJugeStuXX);
		    }else{
			strResponse = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_CLPS_PWINFO_HTML", strClassID,strBatchID,strJudgeAccountID,strJudgeName,strJudgeGroup,strSubmitYN,strJudgeStatus,strSubmited,strJudgeStuSX,strJugeStuXX);
		    }
		}
	    }
	    
	    String strSql3 = "SELECT COUNT(1) FROM PS_TZ_CLPS_PW_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
	    strTotalNum = sqlQuery.queryForObject(strSql3, new Object[] { strClassID,strBatchID }, "String");
	    
	    strResponse = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_BASE_JSON_HTML", strTotalNum,strResponse);
	    
	} catch (Exception e) {
	    e.printStackTrace();
	    errMsg[0] = "100";
	    errMsg[1] = e.toString();
	}
	return strResponse;
    }
    
    public String queryStuList(String strParams,int numLimit, int numStart, String[] errMsg) {
	String strResponse = "";
	JacksonUtil jacksonUtil = new JacksonUtil();
	try {
	    jacksonUtil.json2Map(strParams);
	    String strClassID = jacksonUtil.getString("classID");
	    String strBatchID = jacksonUtil.getString("batchID");
	    String strCurrentOrg = "SEM";

	    String strScoreModalSql = "SELECT TZ_ZLPS_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
	    String strScoreModalId = sqlQuery.queryForObject(strScoreModalSql, new Object[] { strClassID }, "String");
	    
	    String strTreeName = "";
	    String strTreeNameSql = "SELECT TREE_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=?";
	    strTreeName = sqlQuery.queryForObject(strScoreModalSql, new Object[] { strScoreModalId }, "String");
	    
	    String strScoreItemId = "";
	    String strScoreItemSql = "SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? and PARENT_NODE_NUM=0";
	    strScoreItemId = sqlQuery.queryForObject(strScoreItemSql, new Object[] { strTreeName }, "String");

	    Integer intDqpyLunc = 0;
	    String strDqpyLuncSql = "SELECT ifnull(TZ_DQPY_LUNC,0) FROM PS_TZ_CLPS_GZ_TBL WHERE TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=?";
	    intDqpyLunc = sqlQuery.queryForObject(strDqpyLuncSql, new Object[] { strClassID, strBatchID }, "Integer");
	    
	    Integer numZfz = 0;
	    String strZfzSql = "SELECT COUNT(*) FROM PS_TZ_MODAL_DT_TBL A,ps_TZ_CJ_BPH_TBL B WHERE A.TREE_NAME=? AND A.TZ_SCORE_ITEM_ID=B.TZ_SCORE_ITEM_ID AND B.TZ_SCORE_MODAL_ID=? AND A.TZ_SCR_TO_SCORE='Y' AND B.TZ_ITEM_S_TYPE='Y'";
	    numZfz = sqlQuery.queryForObject(strZfzSql, new Object[] { strTreeName, strScoreModalId }, "Integer");
	    
	    //报名表编号 姓名     性别    面试资格   评委间偏差    评委信息      评审状态   操作人   平均分;
	    String strAppInsID="",strName="",strGender="",strViewQua="",strPweiPc="",strJudgeInfo="",strJudgeStatus="",strOprID="";
	    String strSql1 = "SELECT TZ_APP_INS_ID,TZ_MSHI_ZGFLG,TZ_CLPS_PWJ_PC FROM PS_TZ_CLPS_KSH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? limit " + numStart + "," + numLimit;
	    System.out.println(strSql1);
	    List<Map<String, Object>> mapList1 = sqlQuery.queryForList(strSql1, new Object[] { strClassID,strBatchID });
	    if(mapList1!=null&&mapList1.size()>0){
		for(Object obj1:mapList1){
    	    	    Map<String,Object> result=(Map<String,Object>) obj1;
    	    	    strAppInsID = result.get("TZ_APP_INS_ID")==null ? "" : String.valueOf(result.get("TZ_APP_INS_ID"));
    	    	    strViewQua = result.get("TZ_MSHI_ZGFLG")==null ? "" : String.valueOf(result.get("TZ_MSHI_ZGFLG"));
    	    	    strPweiPc = result.get("TZ_CLPS_PWJ_PC")==null ? "" : String.valueOf(result.get("TZ_CLPS_PWJ_PC"));

		    String sql2 = "SELECT TZ_APP_INS_ID ,(SELECT OPRID FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID=A.TZ_APP_INS_ID limit 0,1) OPRID, (SELECT TZ_REALNAME FROM PS_TZ_FORM_WRK_T B ,PS_TZ_REG_USER_T C WHERE B.TZ_APP_INS_ID=A.TZ_APP_INS_ID AND B.OPRID = C.OPRID limit 0,1) TZ_REALNAME, (SELECT TZ_GENDER FROM PS_TZ_FORM_WRK_T B ,PS_TZ_REG_USER_T C WHERE B.TZ_APP_INS_ID=A.TZ_APP_INS_ID AND B.OPRID = C.OPRID limit 0,1)TZ_GENDER FROM PS_TZ_CLPS_KSH_TBL A WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=?";
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
		    String sql3 = "SELECT TZ_PWEI_OPRID FROM PS_TZ_CP_PW_KS_TBL  WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=?";
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
		    if("Y".equals(strViewQua)){
			strViewQua = "有面试资格";
		    }else if("N".equals(strViewQua)){
			strViewQua = "无面试资格";
		    }else{
			strViewQua = "待定";
		    }		    
		    
		    Integer intTotalSub = 0;
		    String strSql4 = "SELECT COUNT(*) FROM PS_TZ_KSCLPSLS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_SUBMIT_YN='Y' AND TZ_CLPS_LUNC=? AND TZ_APP_INS_ID=?";
		    intTotalSub = sqlQuery.queryForObject(strSql4, new Object[] { strClassID,strBatchID,intDqpyLunc,strAppInsID  }, "Integer");
		    
		    Integer intClpyNum = 0;
		    String strSql5 = "SELECT ifnull(TZ_MSPY_NUM,0) FROM PS_TZ_CLPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
		    intClpyNum = sqlQuery.queryForObject(strSql5, new Object[] { strClassID,strBatchID }, "Integer");
		    
		    Integer intFlg = intClpyNum - intTotalSub;
		    
		    String strJudgeProgress="",strStuProgress="";
		    if(intFlg!=0){
			strJudgeStatus = "N";
			strJudgeProgress = intTotalSub + "/" + intClpyNum;
			strStuProgress = "未完成" + "(" + strJudgeProgress + ")" ;
		    }else{
			strJudgeStatus = "N";
			strJudgeProgress = "";
			strStuProgress = "已完成";		
		    }
		    //待完成
		    Integer strTotalScore = 100;
		    double strAveScore = 0.00;
		    Integer intNumPwei = 0;
		    String strSql6 = "SELECT COUNT(TZ_PWEI_OPRID) FROM ps_TZ_CP_PW_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND  TZ_APP_INS_ID=?";
		    intNumPwei = sqlQuery.queryForObject(strSql6, new Object[] { strClassID,strBatchID,strAppInsID }, "Integer");
		    if(intNumPwei!=0){
			DecimalFormat df  = new DecimalFormat("######0.00");   
			double tmpDouble = (double)(strTotalScore/intNumPwei);
			String tmpAveScore = df.format(tmpDouble);
			strAveScore = Double.valueOf(tmpAveScore);
		    }
		    if(!"".equals(strResponse)&&strResponse!=null){
			strResponse = strResponse + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_CLPS_KSINFO_HTML",strAppInsID,strName,strGender,strPweiPc,strPwList,strStuProgress,strViewQua,String.valueOf(strAveScore),strStuProgress);
		    }else{
			strResponse = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_CLPS_KSINFO_HTML",strAppInsID,strName,strGender,strPweiPc,strPwList,strStuProgress,strViewQua,String.valueOf(strAveScore),strStuProgress);
		    }
		}
	    }
	    String strSql3 = "SELECT COUNT(1) FROM PS_TZ_CLPS_KSH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
	    String strTotalNum = sqlQuery.queryForObject(strSql3, new Object[] { strClassID,strBatchID }, "String");
	    
	    strResponse = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_BASE_JSON_HTML", strTotalNum,strResponse);
	} catch (Exception e) {
	    e.printStackTrace();
	    errMsg[0] = "100";
	    errMsg[1] = e.toString();
	}
	return strResponse;
    }
    
    public String queryPwDfChartData(String strParams, String[] errMsg) {
	String strResponse = "\"failure\"";
	JacksonUtil jacksonUtil = new JacksonUtil();
	try {
	    jacksonUtil.json2Map(strParams);
	    String strClassID = jacksonUtil.getString("classID");
	    String strBatchID = jacksonUtil.getString("batchID");
	    String strCurrentOrg = "SEM";
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
