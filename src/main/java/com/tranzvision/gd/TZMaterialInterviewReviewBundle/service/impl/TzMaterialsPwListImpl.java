package com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原 TZ_GD_CLPS_PKG:TZ_GD_VIEWJDG_CLS
 * @author yuds
 * 时间：2017年4月1日
 */
@Service("com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl.TzMaterialsPwListImpl")
public class TzMaterialsPwListImpl  extends FrameworkImpl {
    @Autowired
    private SqlQuery sqlQuery;
    @Autowired
    private TZGDObject tzGdObject;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private TzLoginServiceImpl tzLoginServiceImpl;
        
    @Override
    public String tzQueryList(String strParams,int numLimit, int numStart, String[] errMsg) {
	String strResponse = "";
	JacksonUtil jacksonUtil = new JacksonUtil();
	    
	try {
	    String strCurrentOrg = tzLoginServiceImpl.getLoginedManagerOrgid(request);
	    
	    jacksonUtil.json2Map(strParams);
	    String strClassID = jacksonUtil.getString("classID");
	    String strBatchID = jacksonUtil.getString("batchID");
	    String strAppInsID = jacksonUtil.getString("appInsID");
	    String strOprID = jacksonUtil.getString("oprID");
	    
	    String strScoreModalSql = "SELECT TZ_ZLPS_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
	    String strScoreModalId = sqlQuery.queryForObject(strScoreModalSql, new Object[] { strClassID }, "String");
	    
	    String strTreeName = "", strJsfs = "";
	
	    String strTreeNameSql = "SELECT TREE_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=? AND TZ_JG_ID=?";
	    strTreeName = sqlQuery.queryForObject(strTreeNameSql, new Object[] { strScoreModalId,strCurrentOrg }, "String");
	    
	    String strStuSql = "SELECT DISTINCT TZ_REALNAME FROM PS_TZ_REG_USER_T WHERE OPRID=(SELECT OPRID FROM PS_TZ_FORM_WRK_T WHERE  TZ_APP_INS_ID=?) ";
	    String strStuName = sqlQuery.queryForObject(strStuSql, new Object[]{strAppInsID},"String");
	    
	    String strScoreItemId = "";
	    String strScoreItemSql = "SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? and PARENT_NODE_NUM=0";
	    strScoreItemId = sqlQuery.queryForObject(strScoreItemSql, new Object[] { strTreeName }, "String");
	    
	    String strJudgeListSql = "SELECT A.TZ_PWEI_OPRID,(SELECT B.TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL B WHERE A.TZ_PWEI_OPRID=B.OPRID limit 0,1) TZ_REALNAME,A.TZ_SCORE_INS_ID FROM PS_TZ_CP_PW_KS_TBL A WHERE A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_APP_INS_ID=?";
	    List<Map<String,Object>> judgeInfoUtil = sqlQuery.queryForList(strJudgeListSql, new Object[]{strClassID,strBatchID,strAppInsID});
	    int intTotal = 0;	
	    if(judgeInfoUtil!=null&&judgeInfoUtil.size()>0){
		for(Object sJudge:judgeInfoUtil){
		    Map<String,Object> resultMap = (Map<String,Object>) sJudge;
		    
		    String judgeOprid = String.valueOf(resultMap.get("TZ_PWEI_OPRID"));
		    String judgeRealName = String.valueOf(resultMap.get("TZ_REALNAME"));
		    String judgeInsId = String.valueOf(resultMap.get("TZ_SCORE_INS_ID"));
		    
		    String strDyColValue = "";
		    String bphSql = "SELECT TZ_SCORE_ITEM_ID,TZ_XS_MC FROM PS_TZ_BPH_VW1 WHERE TZ_SCORE_MODAL_ID=? ORDER BY TZ_PX";
		    List<Map<String,Object>> bphMap = sqlQuery.queryForList(bphSql, new Object[]{strScoreModalId}); 
		    if(bphMap!=null&&bphMap.size()>0){
			for(Object bphObj:bphMap){
			    Map<String,Object> resultBph = (Map<String,Object>) bphObj;
			    
			    String strItemId = String.valueOf(resultBph.get("TZ_SCORE_ITEM_ID"));
			    String strScoreItemMc = String.valueOf(resultBph.get("TZ_XS_MC"));
			    
			    String strScoreItemType="",strScoreToScore="";
			    String strScoreSql = "SELECT TZ_SCORE_ITEM_TYPE,TZ_SCR_TO_SCORE FROM PS_TZ_MODAL_DT_TBL WHERE TREE_NAME=? AND TZ_SCORE_ITEM_ID=?";
			    Map<String,Object> sMap = sqlQuery.queryForMap(strScoreSql, new Object[]{strTreeName,strItemId});
			    if(sMap!=null){
				strScoreItemType = sMap.get("TZ_SCORE_ITEM_TYPE")==null?"":String.valueOf(sMap.get("TZ_SCORE_ITEM_TYPE"));
				strScoreToScore = sMap.get("TZ_SCR_TO_SCORE")==null?"":String.valueOf(sMap.get("TZ_SCR_TO_SCORE"));
			    }
			    //得到分值
			    String strScoreNum="",strScorePyValue="";
			    String strSql3 = "SELECT TZ_SCORE_NUM,TZ_SCORE_PY_VALUE FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID=?";
			    Map<String,Object> aMap = sqlQuery.queryForMap(strSql3, new Object[]{judgeInsId,strItemId});
			    if(aMap!=null){
				strScoreNum = sMap.get("TZ_SCORE_NUM")==null?"":String.valueOf(sMap.get("TZ_SCORE_NUM"));
				strScorePyValue = sMap.get("TZ_SCORE_PY_VALUE")==null?"":String.valueOf(sMap.get("TZ_SCORE_PY_VALUE"));
			    }
			    
			    if("W".equals(strScoreItemType)||"R".equals(strScoreItemType)){
				//对评语做特殊字符处理-待完成
				
				if("".equals(strDyColValue)){
				    strDyColValue = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_CLPSSCO_HTML",strScorePyValue,strScoreItemMc);
				}else{
				    strDyColValue = strDyColValue + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_CLPSSCO_HTML",strScorePyValue,strScoreItemMc);
				}
			    }else {
				if("X".equals(strScoreItemType)){
			    
				    //如果是下拉框，需要取得下拉框名称，此处存的是下拉框编号
				    String xlk_xxbh = strScorePyValue;
				    String strSql4 = "SELECT TZ_CJX_XLK_XXMC FROM PS_TZ_ZJCJXXZX_T WHERE TREE_NAME=? AND TZ_SCORE_ITEM_ID=? AND TZ_CJX_XLK_XXBH=?";
				
				    strScorePyValue = sqlQuery.queryForObject(strSql4,new Object[]{strTreeName,strScoreItemId,xlk_xxbh}, "String");
				}
				//strScorePyValue需要特殊字符处理
				
				if("".equals(strDyColValue)){
				    strDyColValue = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_CLPSSCO_HTML",strScorePyValue,strScoreItemMc);
				}else{
				    strDyColValue = strDyColValue + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_CLPSSCO_HTML",strScorePyValue,strScoreItemMc);
				}
			    }
			}
		    }
		    strDyColValue = "[" + strDyColValue + "]";
		    if("".equals(strResponse)){
			strResponse = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_CLPS_APPJUDGECO_HTML",strAppInsID,judgeRealName,strStuName,strDyColValue,strClassID,strBatchID);
		    }else{
			strResponse = strResponse + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_CLPS_APPJUDGECO_HTML",strAppInsID,judgeRealName,strStuName,strDyColValue,strClassID,strBatchID);
		    }
		}
		intTotal = intTotal + 1;
	    }
	    strResponse = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_BASE_JSON_HTML", String.valueOf(intTotal),strResponse);
	}catch(Exception e){
	    errMsg[0] = "100";
	    errMsg[1] = e.toString();
	}
	return strResponse;
    }
}
