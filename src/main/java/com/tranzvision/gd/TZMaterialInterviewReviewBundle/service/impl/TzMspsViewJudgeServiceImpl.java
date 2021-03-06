package com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzMsPsGzTblMapper;
import com.tranzvision.gd.TZMbaPwMspsBundle.dao.psTzMspwpsjlTblMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原 TZ_GD_MSPS_PKG:TZ_GD_APPJUG_CLS
 * 
 * @author yuds 面试评审查看评分信息
 *
 */

@Service("com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl.TzMspsViewJudgeServiceImpl")
public class TzMspsViewJudgeServiceImpl  extends FrameworkImpl  {
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
	@Autowired
	private TzFilterIllegalCharacter tzFilterIllegalCharacter;
	
	@SuppressWarnings("unchecked")
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		
		String strResponse = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try
		{

			String strCurrentOrg = tzLoginServiceImpl.getLoginedManagerOrgid(request);;
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			jacksonUtil.json2Map(strParams);
			String strClassID = jacksonUtil.getString("classID");
			String strBatchID = jacksonUtil.getString("batchID");
			String strAppInsID = jacksonUtil.getString("appINSID");
			String strOprID = jacksonUtil.getString("oprID");
			String strScoreModalSql = "SELECT TZ_MSCJ_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
			String strScoreModalId = (String)sqlQuery.queryForObject(strScoreModalSql, new Object[] {strClassID}, "String");
			String strTreeName = "";
			String strJsfs = "";
			String strTreeNameSql = "SELECT TREE_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=? AND TZ_JG_ID=?";
			strTreeName = (String)sqlQuery.queryForObject(strTreeNameSql, new Object[] {strScoreModalId, strCurrentOrg}, "String");
			String strStuSql = "SELECT DISTINCT TZ_REALNAME FROM PS_TZ_REG_USER_T WHERE OPRID=(SELECT OPRID FROM PS_TZ_FORM_WRK_T WHERE  TZ_APP_INS_ID=?) ";
			String strStuName = (String)sqlQuery.queryForObject(strStuSql, new Object[] {strAppInsID}, "String");
			String strScoreItemId = "";
			String strScoreItemSql = "SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? and PARENT_NODE_NUM=0";
			strScoreItemId = (String)sqlQuery.queryForObject(strScoreItemSql, new Object[] {strTreeName	}, "String");
			String strJudgeListSql = "SELECT A.TZ_PWEI_OPRID,(SELECT B.TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL B WHERE A.TZ_PWEI_OPRID=B.OPRID limit 0,1) TZ_REALNAME,A.TZ_SCORE_INS_ID FROM PS_TZ_MP_PW_KS_TBL A WHERE A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_APP_INS_ID=? AND A.TZ_DELETE_ZT<>'Y'";
			List<Map<String,Object>> judgeInfoUtil = sqlQuery.queryForList(strJudgeListSql, new Object[] {strClassID, strBatchID, strAppInsID});
			int intTotal = 0;
			if (judgeInfoUtil != null && judgeInfoUtil.size() > 0)
			{
				for(Object sUitl:judgeInfoUtil){
					String strDyColValue = "";
					
					Map<String,Object> sMap = (Map<String,Object>) sUitl;
					String judgeOprid = sMap.get("TZ_PWEI_OPRID")==null?"":String.valueOf(sMap.get("TZ_PWEI_OPRID"));
					String judgeRealName = sMap.get("TZ_REALNAME")==null?"":String.valueOf(sMap.get("TZ_REALNAME"));
					String judgeInsId = sMap.get("TZ_SCORE_INS_ID")==null?"":String.valueOf(sMap.get("TZ_SCORE_INS_ID"));
					String bphSql = "SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? ORDER BY TREE_NODE_NUM ASC";
					
					List<Map<String,Object>> bphMapList = sqlQuery.queryForList(bphSql, new Object[] {strTreeName});
					if(bphMapList!=null&&bphMapList.size()>0){
						for(Object bphObj:bphMapList){
							Map<String,Object> bphMap = (Map<String,Object>) bphObj;
							String strItemId = bphMap.get("TREE_NODE") == null ? "" : String.valueOf(bphMap.get("TREE_NODE"));
							
							String strTTmpSQL = "SELECT DESCR FROM PS_TZ_MODAL_DT_TBL WHERE TZ_JG_ID=? AND TREE_NAME=? AND TZ_SCORE_ITEM_ID=?";
							String strScoreItemMc = sqlQuery.queryForObject(strTTmpSQL,new Object[]{strCurrentOrg,strTreeName,strItemId},"String");
							
							String strScoreItemType = "";
							String strScoreToScore = "";
							String strScoreSql = "SELECT TZ_SCORE_ITEM_TYPE,TZ_SCR_TO_SCORE FROM PS_TZ_MODAL_DT_TBL WHERE TREE_NAME=? AND TZ_SCORE_ITEM_ID=?";
							Map<String,Object> saMap = sqlQuery.queryForMap(strScoreSql, new Object[] {strTreeName, strItemId});
							if (saMap != null)
							{
								strScoreItemType = saMap.get("TZ_SCORE_ITEM_TYPE") != null ? String.valueOf(saMap.get("TZ_SCORE_ITEM_TYPE")) : "";
								strScoreToScore = saMap.get("TZ_SCR_TO_SCORE") != null ? String.valueOf(saMap.get("TZ_SCR_TO_SCORE")) : "";
							}
							
							String strScoreNum = "";
							String strScorePyValue = "";
							String strSql3 = "SELECT TZ_SCORE_NUM,TZ_SCORE_PY_VALUE FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID=?";
							Map<String,Object> aMap = sqlQuery.queryForMap(strSql3, new Object[] {judgeInsId, strItemId});
							if (aMap != null)
							{
								strScoreNum = aMap.get("TZ_SCORE_NUM") != null ? String.valueOf(aMap.get("TZ_SCORE_NUM")) : "";
								strScorePyValue = aMap.get("TZ_SCORE_PY_VALUE") != null ? String.valueOf(aMap.get("TZ_SCORE_PY_VALUE")) : "";
							}

							switch(strScoreItemType){
								case "A"://数字汇总项
									strScorePyValue = strScoreNum;
									break;
								case "B"://数字打分项
									strScorePyValue = strScoreNum;
									break;
								case "C"://评语
									//strScorePyValue默认为评语值，无须转换
									strScorePyValue = this.transChar(strScorePyValue);
									break;
								case "D"://下拉框
									String xlk_xxbh = strScorePyValue;
									String strSql4 = "SELECT TZ_CJX_XLK_XXMC FROM PS_TZ_ZJCJXXZX_T WHERE TREE_NAME=? AND TZ_SCORE_ITEM_ID=? AND TZ_CJX_XLK_XXBH=?";
									strScorePyValue = (String)sqlQuery.queryForObject(strSql4, new Object[] {strTreeName, strScoreItemId, xlk_xxbh}, "String");
									break;									
							}
							if ("".equals(strDyColValue)){
								strDyColValue = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_MSPSSCO_HTML", new String[] {strScorePyValue, strScoreItemMc});
							}else{
								strDyColValue = strDyColValue + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_MSPSSCO_HTML", new String[] {strScorePyValue, strScoreItemMc});
							}						
						}
					}
					
					if("".equals(strResponse)){
						strResponse = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_MSPS_JUGSCO_HTML", new String[] {
								strAppInsID, judgeRealName, strStuName, strDyColValue, strClassID, strBatchID
							});
					}else{
						strResponse = strResponse + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_MSPS_JUGSCO_HTML", new String[] {
								strAppInsID, judgeRealName, strStuName, strDyColValue, strClassID, strBatchID
							});
					}
					intTotal ++ ;
				}
				strResponse = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_BASE_JSON_HTML", new String[] {String.valueOf(intTotal), strResponse});
			}
		}
		catch (Exception e)
		{
			errMsg[0] = "100";
			errMsg[1] = e.toString();
			e.printStackTrace();
		}
		return strResponse;
	}
	
	@Override
	public String tzQueryList(String strParams,int numLimit, int numStart, String[] errMsg) {
		
		String strResponse = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try
		{

			String strCurrentOrg = tzLoginServiceImpl.getLoginedManagerOrgid(request);;
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			jacksonUtil.json2Map(strParams);
			String strClassID = jacksonUtil.getString("classID");
			String strBatchID = jacksonUtil.getString("batchID");
			String strAppInsID = jacksonUtil.getString("appINSID");
			String strOprID = jacksonUtil.getString("oprID");
			String strScoreModalSql = "SELECT TZ_MSCJ_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
			String strScoreModalId = (String)sqlQuery.queryForObject(strScoreModalSql, new Object[] {strClassID}, "String");
			String strTreeName = "";
			String strJsfs = "";
			String strTreeNameSql = "SELECT TREE_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=? AND TZ_JG_ID=?";
			strTreeName = (String)sqlQuery.queryForObject(strTreeNameSql, new Object[] {strScoreModalId, strCurrentOrg}, "String");
			String strStuSql = "SELECT DISTINCT TZ_REALNAME FROM PS_TZ_REG_USER_T WHERE OPRID=(SELECT OPRID FROM PS_TZ_FORM_WRK_T WHERE  TZ_APP_INS_ID=?) ";
			String strStuName = (String)sqlQuery.queryForObject(strStuSql, new Object[] {strAppInsID}, "String");
			String strScoreItemId = "";
			String strScoreItemSql = "SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? and PARENT_NODE_NUM=0";
			strScoreItemId = (String)sqlQuery.queryForObject(strScoreItemSql, new Object[] {strTreeName	}, "String");
			String strJudgeListSql = "SELECT A.TZ_PWEI_OPRID,(SELECT B.TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL B WHERE A.TZ_PWEI_OPRID=B.OPRID limit 0,1) TZ_REALNAME,A.TZ_SCORE_INS_ID FROM PS_TZ_MP_PW_KS_TBL A WHERE A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_APP_INS_ID=? AND A.TZ_DELETE_ZT<>'Y'";
			List<Map<String,Object>> judgeInfoUtil = sqlQuery.queryForList(strJudgeListSql, new Object[] {strClassID, strBatchID, strAppInsID});
			int intTotal = 0;
			
			if (judgeInfoUtil != null && judgeInfoUtil.size() > 0)
			{
				for(Object sUitl:judgeInfoUtil){
					int n = 0;
					
					String strDyColValue = "";
					
					Map<String,Object> sMap = (Map<String,Object>) sUitl;
					String judgeOprid = sMap.get("TZ_PWEI_OPRID")==null?"":String.valueOf(sMap.get("TZ_PWEI_OPRID"));
					String judgeRealName = sMap.get("TZ_REALNAME")==null?"":String.valueOf(sMap.get("TZ_REALNAME"));
					String judgeInsId = sMap.get("TZ_SCORE_INS_ID")==null?"":String.valueOf(sMap.get("TZ_SCORE_INS_ID"));
					String bphSql = "SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? ORDER BY TREE_NODE_NUM ASC";
					
					List<Map<String,Object>> bphMapList = sqlQuery.queryForList(bphSql, new Object[] {strTreeName});
					if(bphMapList!=null&&bphMapList.size()>0){
						for(Object bphObj:bphMapList){						
							
							Map<String,Object> bphMap = (Map<String,Object>) bphObj;
							String strItemId = bphMap.get("TREE_NODE") == null ? "" : String.valueOf(bphMap.get("TREE_NODE"));
							
							String strTTmpSQL = "SELECT DESCR FROM PS_TZ_MODAL_DT_TBL WHERE TZ_JG_ID=? AND TREE_NAME=? AND TZ_SCORE_ITEM_ID=?";
							String strScoreItemMc = sqlQuery.queryForObject(strTTmpSQL,new Object[]{strCurrentOrg,strTreeName,strItemId},"String");
							
							String strScoreItemType = "";
							String strScoreToScore = "";
							String strScoreSql = "SELECT TZ_SCORE_ITEM_TYPE,TZ_SCR_TO_SCORE FROM PS_TZ_MODAL_DT_TBL WHERE TREE_NAME=? AND TZ_SCORE_ITEM_ID=?";
							Map<String,Object> saMap = sqlQuery.queryForMap(strScoreSql, new Object[] {strTreeName, strItemId});
							if (saMap != null)
							{
								strScoreItemType = saMap.get("TZ_SCORE_ITEM_TYPE") != null ? String.valueOf(saMap.get("TZ_SCORE_ITEM_TYPE")) : "";
								strScoreToScore = saMap.get("TZ_SCR_TO_SCORE") != null ? String.valueOf(saMap.get("TZ_SCR_TO_SCORE")) : "";
							}
							
							String strScoreNum = "";
							String strScorePyValue = "";
							String strSql3 = "SELECT TZ_SCORE_NUM,TZ_SCORE_PY_VALUE FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID=?";
							Map<String,Object> aMap = sqlQuery.queryForMap(strSql3, new Object[] {judgeInsId, strItemId});
							if (aMap != null)
							{
								strScoreNum = aMap.get("TZ_SCORE_NUM") != null ? String.valueOf(aMap.get("TZ_SCORE_NUM")) : "";
								strScorePyValue = aMap.get("TZ_SCORE_PY_VALUE") != null ? String.valueOf(aMap.get("TZ_SCORE_PY_VALUE")) : "";								
							}

							switch(strScoreItemType){
								case "A"://数字汇总项
									strScorePyValue = strScoreNum;
									break;
								case "B"://数字打分项
									strScorePyValue = strScoreNum;
									break;
								case "C"://评语
									//strScorePyValue默认为评语值，无须转换
									strScorePyValue = this.transChar(strScorePyValue);
									break;
								case "D"://下拉框
									String xlk_xxbh = strScorePyValue;
									String strSql4 = "SELECT TZ_CJX_XLK_XXMC FROM PS_TZ_ZJCJXXZX_T WHERE TREE_NAME=? AND TZ_SCORE_ITEM_ID=? AND TZ_CJX_XLK_XXBH=?";
									strScorePyValue = (String)sqlQuery.queryForObject(strSql4, new Object[] {strTreeName, strScoreItemId, xlk_xxbh}, "String");
									break;									
							}
							if ("".equals(strDyColValue)){
								strDyColValue = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_MSPS_JUSCO_HTML", new String[] {String.valueOf(n), strScorePyValue});
							}else{
								strDyColValue = strDyColValue + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_MSPS_JUSCO_HTML", new String[] {String.valueOf(n), strScorePyValue});
							}							
							n = n + 1;
						}
					}
					
					if("".equals(strResponse)){
						strResponse = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_MSPS_JUGSTO_HTML", new String[] {
								strAppInsID, judgeRealName, strStuName, strDyColValue, strClassID, strBatchID
							});
					}else{
						strResponse = strResponse + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_MSPS_JUGSTO_HTML", new String[] {
								strAppInsID, judgeRealName, strStuName, strDyColValue, strClassID, strBatchID
							});
					}
					intTotal ++ ;
				}
				strResponse = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_BASE_JSON_HTML", new String[] {String.valueOf(intTotal), strResponse});
			}
		}
		catch (Exception e)
		{
			errMsg[0] = "100";
			errMsg[1] = e.toString();
			e.printStackTrace();
		}
		return strResponse;
	}
	
	public String commonCal(String strParams, String[] errMsg) {
		String strResponse = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try
		{

			String strCurrentOrg = tzLoginServiceImpl.getLoginedManagerOrgid(request);;
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			jacksonUtil.json2Map(strParams);
			String strClassID = jacksonUtil.getString("classID");
			String strBatchID = jacksonUtil.getString("batchID");
			String strAppInsID = jacksonUtil.getString("appINSID");
			String strOprID = jacksonUtil.getString("oprID");
			String strScoreModalSql = "SELECT TZ_MSCJ_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
			String strScoreModalId = (String)sqlQuery.queryForObject(strScoreModalSql, new Object[] {strClassID}, "String");
			String strTreeName = "";
			String strJsfs = "";
			String strTreeNameSql = "SELECT TREE_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=? AND TZ_JG_ID=?";
			strTreeName = (String)sqlQuery.queryForObject(strTreeNameSql, new Object[] {strScoreModalId, strCurrentOrg}, "String");
			String strStuSql = "SELECT DISTINCT TZ_REALNAME FROM PS_TZ_REG_USER_T WHERE OPRID=(SELECT OPRID FROM PS_TZ_FORM_WRK_T WHERE  TZ_APP_INS_ID=?) ";
			String strStuName = (String)sqlQuery.queryForObject(strStuSql, new Object[] {strAppInsID}, "String");
			String strScoreItemId = "";
			String strScoreItemSql = "SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? and PARENT_NODE_NUM=0";
			strScoreItemId = (String)sqlQuery.queryForObject(strScoreItemSql, new Object[] {strTreeName	}, "String");
			String strJudgeListSql = "SELECT A.TZ_PWEI_OPRID,(SELECT B.TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL B WHERE A.TZ_PWEI_OPRID=B.OPRID limit 0,1) TZ_REALNAME,A.TZ_SCORE_INS_ID FROM PS_TZ_MP_PW_KS_TBL A WHERE A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_APP_INS_ID=? AND A.TZ_DELETE_ZT<>'Y'";
			List<Map<String,Object>> judgeInfoUtil = sqlQuery.queryForList(strJudgeListSql, new Object[] {strClassID, strBatchID, strAppInsID});
			int intTotal = 0;
			if (judgeInfoUtil != null && judgeInfoUtil.size() > 0)
			{
				for(Object sUitl:judgeInfoUtil){
					String strDyColValue = "";
					
					Map<String,Object> sMap = (Map<String,Object>) sUitl;
					String judgeOprid = sMap.get("TZ_PWEI_OPRID")==null?"":String.valueOf(sMap.get("TZ_PWEI_OPRID"));
					String judgeRealName = sMap.get("TZ_REALNAME")==null?"":String.valueOf(sMap.get("TZ_REALNAME"));
					String judgeInsId = sMap.get("TZ_SCORE_INS_ID")==null?"":String.valueOf(sMap.get("TZ_SCORE_INS_ID"));
					String bphSql = "SELECT TZ_SCORE_ITEM_ID,TZ_XS_MC FROM PS_TZ_CJ_BPH_TBL WHERE TZ_ITEM_S_TYPE = 'A' AND TZ_JG_ID=? AND TZ_SCORE_MODAL_ID=? ORDER BY TZ_PX";
					List<Map<String,Object>> bphMapList = sqlQuery.queryForList(bphSql, new Object[] {strCurrentOrg,strScoreModalId});
					if(bphMapList!=null&&bphMapList.size()>0){
						for(Object bphObj:bphMapList){
							Map<String,Object> bphMap = (Map<String,Object>) bphObj;
							String strItemId = bphMap.get("TZ_SCORE_ITEM_ID")==null?"":String.valueOf(bphMap.get("TZ_SCORE_ITEM_ID"));
							String strScoreItemMc = bphMap.get("TZ_XS_MC")==null?"":String.valueOf(bphMap.get("TZ_XS_MC"));
							
							String strScoreItemType = "";
							String strScoreToScore = "";
							String strScoreSql = "SELECT TZ_SCORE_ITEM_TYPE,TZ_SCR_TO_SCORE FROM PS_TZ_MODAL_DT_TBL WHERE TREE_NAME=? AND TZ_SCORE_ITEM_ID=?";
							Map<String,Object> saMap = sqlQuery.queryForMap(strScoreSql, new Object[] {strTreeName, strItemId});
							if (saMap != null)
							{
								strScoreItemType = saMap.get("TZ_SCORE_ITEM_TYPE") != null ? String.valueOf(saMap.get("TZ_SCORE_ITEM_TYPE")) : "";
								strScoreToScore = saMap.get("TZ_SCR_TO_SCORE") != null ? String.valueOf(saMap.get("TZ_SCR_TO_SCORE")) : "";
							}
							
							String strScoreNum = "";
							String strScorePyValue = "";
							String strSql3 = "SELECT TZ_SCORE_NUM,TZ_SCORE_PY_VALUE FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID=?";
							Map<String,Object> aMap = sqlQuery.queryForMap(strSql3, new Object[] {judgeInsId, strItemId});
							if (aMap != null)
							{
								strScoreNum = aMap.get("TZ_SCORE_NUM") != null ? String.valueOf(aMap.get("TZ_SCORE_NUM")) : "";
								strScorePyValue = aMap.get("TZ_SCORE_PY_VALUE") != null ? String.valueOf(aMap.get("TZ_SCORE_PY_VALUE")) : "";
							}

							if ("W".equals(strScoreItemType) || "R".equals(strScoreItemType) || ("Y".equals(strScoreItemType)&&"A".equals(strScoreToScore)))
							{
								if ("".equals(strDyColValue)){
									strDyColValue = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_MSPSSCO_HTML", new String[] {strScoreNum, strScoreItemMc});
								}else{
									strDyColValue = strDyColValue + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_MSPSSCO_HTML", new String[] {strScoreNum, strScoreItemMc});
								}		
							} 
							else
							{
								if ("X".equals(strScoreItemType))
								{
									String xlk_xxbh = strScorePyValue;
									String strSql4 = "SELECT TZ_CJX_XLK_XXMC FROM PS_TZ_ZJCJXXZX_T WHERE TREE_NAME=? AND TZ_SCORE_ITEM_ID=? AND TZ_CJX_XLK_XXBH=?";
									strScorePyValue = (String)sqlQuery.queryForObject(strSql4, new Object[] {strTreeName, strScoreItemId, xlk_xxbh}, "String");
								}
								if ("".equals(strDyColValue)){
									strDyColValue = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_MSPSSCO_HTML", new String[] {strScoreNum, strScoreItemMc});
								}else{
									strDyColValue = strDyColValue + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_MSPSSCO_HTML", new String[] {strScoreNum, strScoreItemMc});
								}
								
							}
						}
					}
					
					if("".equals(strResponse)){
						strResponse = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_MSPS_JUGSCO_HTML", new String[] {
								strAppInsID, judgeRealName, strStuName, strDyColValue, strClassID, strBatchID
							});
					}else{
						strResponse = strResponse + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_MSPS_JUGSCO_HTML", new String[] {
								strAppInsID, judgeRealName, strStuName, strDyColValue, strClassID, strBatchID
							});
					}
					intTotal ++ ;
				}
				strResponse = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_BASE_JSON_HTML", new String[] {String.valueOf(intTotal), strResponse});
			}
		}
		catch (Exception e)
		{
			errMsg[0] = "100";
			errMsg[1] = e.toString();
		}
		return strResponse;
	}
	
	private String transChar(String strValue){
		String result = strValue;
		
		result = tzFilterIllegalCharacter.filterAllIllegalCharacter(result);
		
		result = result.replace("\n", "");
		result = result.replace("\r", "");
		result = result.replace("\t", "    ");
		result = result.replace(" ", " ");
		result = result.replace("\"", "\\" + "\"");
		return result;
	}
}
