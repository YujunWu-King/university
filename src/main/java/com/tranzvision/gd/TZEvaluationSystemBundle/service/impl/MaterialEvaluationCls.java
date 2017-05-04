package com.tranzvision.gd.TZEvaluationSystemBundle.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 材料评审相关：数据查询和处理类
 * 
 * @author ShaweYet
 * @since 2017/03/06
 */
@Service
public class MaterialEvaluationCls{
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	//平均分计算（包括：单个评委和多个评委的情况）;
	protected double calculateAverage(String classId,String batchId,String oprid,String scoreItemId,int error_code,String error_decription){
		   
		   //参数验证
		   if(oprid==null||"".equals(oprid)){
			   error_code = 1;
			   error_decription = "评委不能为空！";
		   }		   
		   if(classId==null||"".equals(classId)){
			   error_code = 1;
			   error_decription = "报考班级不能为空！";
		   }
		   if(batchId==null||"".equals(batchId)){
			   error_code = 1;
			   error_decription = "报考批次不能为空！";
		   }
		   
		   
		   if(scoreItemId==null||"".equals(scoreItemId)){
				//没有传值，取材料评审总分成绩项;
				String TZ_SCORE_MODAL_ID = sqlQuery.queryForObject("select TZ_ZLPS_SCOR_MD_ID from PS_TZ_CLASS_INF_T where TZ_CLASS_ID=?", 
						new Object[]{classId}, "String");

				scoreItemId = sqlQuery.queryForObject("SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=(SELECT TREE_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=?) and PARENT_NODE_NUM=0", 
						new Object[]{TZ_SCORE_MODAL_ID}, "String");
		   }
		   
		   //计算平均分
		   double pjf = 0;

		   //某个报考班级批次下：单个评委在某个成绩项的总分;
		   double dgpw_all_num = 0;
		   //某个报考方向下：单个评委完成的数量;
		   int dgpw_wc_num = 0;

		   //当前轮次
		   int TZ_DQPY_LUNC = 1;
		   String STR_DQPY_LUNC = sqlQuery.queryForObject(
		   		"select TZ_DQPY_LUNC from PS_TZ_CLPS_GZ_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=?",
		   		new Object[] { classId, batchId }, "String");
		   if(STR_DQPY_LUNC!=null&&!"".equals(STR_DQPY_LUNC)){
		   		TZ_DQPY_LUNC = Integer.parseInt(STR_DQPY_LUNC);
		   }

		   String str_dgpw_all_num = sqlQuery.queryForObject(
			   		"select sum( b.TZ_SCORE_NUM) from PS_TZ_CP_PW_KS_TBL a,PS_TZ_CJX_TBL b ,PS_TZ_KSCLPSLS_TBL c where a.TZ_SCORE_INS_ID = b.TZ_SCORE_INS_ID and a.TZ_CLASS_ID = c.TZ_CLASS_ID and a.TZ_APPLY_PC_ID = c.TZ_APPLY_PC_ID and a.TZ_PWEI_OPRID = c.TZ_PWEI_OPRID and a.TZ_APP_INS_ID = c.TZ_APP_INS_ID and c.TZ_SUBMIT_YN <> 'C' and a.TZ_CLASS_ID = ? and a.TZ_APPLY_PC_ID = ? and a.TZ_PWEI_OPRID = ? and b.TZ_SCORE_ITEM_ID = ? and c.TZ_CLPS_LUNC = ?",
			   		new Object[] { classId, batchId ,oprid,scoreItemId ,TZ_DQPY_LUNC}, "String");		   
		   if(str_dgpw_all_num!=null&&!"".equals(str_dgpw_all_num)){
			   dgpw_all_num = Double.parseDouble(str_dgpw_all_num);
		   }
		   
		   //完成数量
		   String str_dgpw_wc_num = sqlQuery.queryForObject(
			   		"select count(distinct TZ_APP_INS_ID) from PS_TZ_KSCLPSLS_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID = ? and TZ_PWEI_OPRID = ? and TZ_CLPS_LUNC = ? and  TZ_SUBMIT_YN <> 'C'",
			   		new Object[] { classId, batchId ,oprid ,TZ_DQPY_LUNC}, "String");		   
		   if(str_dgpw_wc_num!=null&&!"".equals(str_dgpw_wc_num)){
			   dgpw_wc_num = Integer.parseInt(str_dgpw_wc_num);
		   }

		   if(dgpw_all_num==0||dgpw_wc_num==0){
			   pjf = 0;
		   }else{
			   pjf = dgpw_all_num / dgpw_wc_num;
		   }

		   //取2位小数;
		   pjf = (double)Math.round(pjf*100)/100;

		   return pjf;
	}
	
	//某个报考班级批次下：某个评委：某个成绩项的指标项的计算;
	protected List<Map<String,Object>> getScoreItemEvaluationData(String classId,String batchId,String oprid,String scoreItemId,int error_code,String error_decription){

	   //参数验证
	   if(oprid==null||"".equals(oprid)){
		   error_code = 1;
		   error_decription = "评委不能为空！";
	   }		   
	   if(classId==null||"".equals(classId)){
		   error_code = 1;
		   error_decription = "报考班级不能为空！";
	   }
	   if(batchId==null||"".equals(batchId)){
		   error_code = 1;
		   error_decription = "报考批次不能为空！";
	   }
	   if(scoreItemId==null||"".equals(scoreItemId)){
		   error_code = 1;
		   error_decription = "成绩项不能为空！";
	   }

		//存放数据的List
		List<Map<String,Object>> retList = new ArrayList<Map<String,Object>>();

		//由报考报考找到“成绩模型id”
		String TZ_SCORE_MODAL_ID = sqlQuery.queryForObject("select TZ_ZLPS_SCOR_MD_ID from PS_TZ_CLASS_INF_T where TZ_CLASS_ID=?", 
				new Object[]{classId}, "String");
		
		//根据报考班级和批次取得当前轮次;
	    int TZ_DQPY_LUNC = 1;
	    String STR_DQPY_LUNC = sqlQuery.queryForObject(
	   		"select TZ_DQPY_LUNC from PS_TZ_CLPS_GZ_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=?",
	   		new Object[] { classId, batchId }, "String");
	    if(STR_DQPY_LUNC!=null&&!"".equals(STR_DQPY_LUNC)){
	   		TZ_DQPY_LUNC = Integer.parseInt(STR_DQPY_LUNC);
	    }
		   
		//分布对照id
		String TZ_M_FBDZ_ID = sqlQuery.queryForObject(
		   		"SELECT TZ_M_FBDZ_ID FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID = ? AND TZ_M_FBDZ_ID<>'' AND TZ_M_FBDZ_ID is not null limit 0,1 ",
		   		new Object[] { TZ_SCORE_MODAL_ID}, "String");


		//完成数量;
		int wc_num = 0;
		String str_wc_num = sqlQuery.queryForObject(
		   		"select count(distinct TZ_APP_INS_ID) from PS_TZ_KSCLPSLS_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID = ? and TZ_PWEI_OPRID = ? and TZ_CLPS_LUNC = ? and  TZ_SUBMIT_YN <> 'C'",
		   		new Object[] { classId, batchId ,oprid ,TZ_DQPY_LUNC}, "String");		   
		if(str_wc_num!=null&&!"".equals(str_wc_num)){
		   wc_num = Integer.parseInt(str_wc_num);
		}
		
		//分布对照明细id;
		String TZ_M_FBDZ_MX_ID = "";
		//分布对照明细name;
		String TZ_M_FBDZ_MX_NAME = "";
		//分布对照明细下限;
		String TZ_M_FBDZ_MX_XX = "";
		//分布对照明细下限界限;
		String TZ_M_FBDZ_MX_XX_JX = "";
		//分布对照明细上限界限;
		String TZ_M_FBDZ_MX_SX_JX = "";
		//分布对照明细上限;
		String TZ_M_FBDZ_MX_SX = "";

		List<Map<String,Object>> fbdzmxList = sqlQuery.queryForList("select TZ_M_FBDZ_MX_ID,TZ_M_FBDZ_MX_NAME,TZ_M_FBDZ_MX_XX,TZ_M_FBDZ_MX_XX_JX,TZ_M_FBDZ_MX_SX_JX,TZ_M_FBDZ_MX_SX  from PS_TZ_FBDZ_MX_TBL WHERE TZ_M_FBDZ_ID = ? ORDER BY CAST( TZ_M_FBDZ_MX_XH as SIGNED INTEGER) ASC", 
				new Object[]{TZ_M_FBDZ_ID});
		
		if(fbdzmxList!=null){
			for(int i=0;i<fbdzmxList.size();i++){
				 
				TZ_M_FBDZ_MX_ID = (String)fbdzmxList.get(i).get("TZ_M_FBDZ_MX_ID"); 
				TZ_M_FBDZ_MX_NAME = (String)fbdzmxList.get(i).get("TZ_M_FBDZ_MX_NAME");
				TZ_M_FBDZ_MX_XX = String.valueOf((BigDecimal)fbdzmxList.get(i).get("TZ_M_FBDZ_MX_XX")); 
				TZ_M_FBDZ_MX_XX_JX = (String)fbdzmxList.get(i).get("TZ_M_FBDZ_MX_XX_JX");
				TZ_M_FBDZ_MX_SX_JX = (String)fbdzmxList.get(i).get("TZ_M_FBDZ_MX_SX_JX");
				TZ_M_FBDZ_MX_SX = String.valueOf((BigDecimal)fbdzmxList.get(i).get("TZ_M_FBDZ_MX_SX"));

				Map<String,Object> fbdzmxMap = new HashMap<String,Object>();
				
				//分项个数
				int num_dange = 0;
				String str_num_dange = sqlQuery.queryForObject(
				   		new StringBuffer("select count(distinct a.TZ_APP_INS_ID) from PS_TZ_CP_PW_KS_TBL a ,PS_TZ_CJX_TBL b ,PS_TZ_KSCLPSLS_TBL c ")
				   		.append("where a.TZ_SCORE_INS_ID = b.TZ_SCORE_INS_ID  and a.TZ_CLASS_ID = c.TZ_CLASS_ID ")
				   		.append("and a.TZ_APPLY_PC_ID = c.TZ_APPLY_PC_ID and a.TZ_PWEI_OPRID = c.TZ_PWEI_OPRID ")
				   		.append("and a.TZ_APP_INS_ID = c.TZ_APP_INS_ID and c.TZ_SUBMIT_YN <> 'C' ")
				   		.append("and a.TZ_CLASS_ID = ? and a.TZ_APPLY_PC_ID = ? and a.TZ_PWEI_OPRID =? ")
				   		.append("and b.TZ_SCORE_ITEM_ID = ?  and c.TZ_CLPS_LUNC = ? ")
				   		.append("and b.TZ_SCORE_NUM ").append(TZ_M_FBDZ_MX_XX_JX).append(TZ_M_FBDZ_MX_XX)
				   		.append(" and b.TZ_SCORE_NUM ").append(TZ_M_FBDZ_MX_SX_JX).append(TZ_M_FBDZ_MX_SX).toString(),
				   		new Object[] { classId, batchId ,oprid ,scoreItemId,TZ_DQPY_LUNC}, "String");		
				
			   if(str_num_dange!=null&&!"".equals(str_num_dange)){
				   num_dange = Integer.parseInt(str_num_dange);
			   }
				  
			   //分布对照明细id;
			   fbdzmxMap.put("mx_id", TZ_M_FBDZ_MX_ID);
				  
			   //布对照明细name;
			   fbdzmxMap.put("mx_name", TZ_M_FBDZ_MX_NAME);
			   
			   //分布区间的个数;
			   fbdzmxMap.put("num_dange", num_dange);
			   
			   //分布区间的比率;
			   if(num_dange==0||wc_num==0){
				   fbdzmxMap.put("num_rate", (double)0);
			   }else{
				   fbdzmxMap.put("num_rate", (double)Math.round((double)num_dange/wc_num*100*100)/100);
			   }

			   retList.add(fbdzmxMap);
			}
		}

		return retList;
	}
	
	protected boolean validateEvaluation(String classId,String batchId,String oprid,String error_code, String error_decription){

		// 是否可见评议标准数据
		String TZ_PWKJ_BZH = sqlQuery.queryForObject(
				"select TZ_PWKJ_FBT from PS_TZ_CLPS_GZ_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=?",
				new Object[] { classId, batchId },"String");
		
		if("Y".equals(TZ_PWKJ_BZH)){
			
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			String TZ_ZLPS_SCOR_MD_ID=sqlQuery.queryForObject(
					"select TZ_ZLPS_SCOR_MD_ID from PS_TZ_CLASS_INF_T where TZ_CLASS_ID=?",
					new Object[] { classId },"String");
		
			Map<String, Object> scoreModalMap = sqlQuery.queryForMap(
					"SELECT TREE_NAME,TZ_M_FBDZ_ID FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=? AND TZ_JG_ID=?",
					new Object[] { TZ_ZLPS_SCOR_MD_ID ,orgid});
			/*树名称、分布对照id（总分） */
			String TREE_NAME = "",TZ_M_FBDZ_ID="";
			if (scoreModalMap != null) {
				TREE_NAME = (String) scoreModalMap.get("TREE_NAME");
				TZ_M_FBDZ_ID = (String) scoreModalMap.get("TZ_M_FBDZ_ID");
			}
			// 总分成绩项
			String TZ_SCORE_ITEM_ID = sqlQuery.queryForObject(
					"SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? and PARENT_NODE_NUM=0",
					new Object[] { TREE_NAME }, "String");
			
			int int_error_code = 0;
			
			/*1、校验平均分指标是否符合要求*/
			double pjf = this.calculateAverage(classId, batchId, oprid, TZ_SCORE_ITEM_ID, int_error_code, error_decription);
			error_code = String.valueOf(int_error_code);
			
			//查询标准平均分和允许误差
			Double standardAvg = (double)0,avgAllowDeviation = (double)0;
			Map<String, Object> avgStandardMap = sqlQuery.queryForMap(
					"select TZ_TJL_BZH,TZ_TJL_WCZ from PS_TZ_QTTJ_BZH_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_SCORE_MODAL_ID=? and TZ_SCORE_ITEM_ID=? and TZ_TJGN_ID=?",
					new Object[] { classId, batchId ,TZ_ZLPS_SCOR_MD_ID,"TOTAL","001"});
			if (avgStandardMap != null) {
				standardAvg = avgStandardMap.get("TZ_TJL_BZH")==null?standardAvg:((BigDecimal) avgStandardMap.get("TZ_TJL_BZH")).doubleValue();
				avgAllowDeviation = avgStandardMap.get("TZ_TJL_WCZ")==null?standardAvg:((BigDecimal) avgStandardMap.get("TZ_TJL_WCZ")).doubleValue();
			}
			
			//实际误差和是否符合要求
			BigDecimal bg = new BigDecimal(Math.abs(standardAvg-pjf));
			double actualDeviation = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			if(actualDeviation>avgAllowDeviation){
				//不符合要求
				return false;
			}
			
			/*2、校验分布统计数据是否符合要求*/
			//评议的考生数量
			int totalEvaluationNum = sqlQuery.queryForObject(
					"SELECT COUNT(*) FROM PS_TZ_CP_PW_KS_TBL WHERE TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=?",
					new Object[] { classId, batchId, oprid }, "Integer");
			
			/*当前评委打分分布统计信息 */
			List<Map<String,Object>> evaluationDataList = this.getScoreItemEvaluationData(classId, batchId, oprid, TZ_SCORE_ITEM_ID, int_error_code, error_decription);
			error_code = String.valueOf(int_error_code);
			
			if(evaluationDataList!=null){
				for(int i=0;i<evaluationDataList.size();i++){
					
					String TZ_M_FBDZ_MX_ID = (String)evaluationDataList.get(i).get("mx_id");
					
					//标准分布比率、标准分布人数、允许误差人数
					Double TZ_BZFB_BL = (double)0, TZ_BZFB_NUM = (double)0, TZ_YXWC_NUM = (double)0;
					Map<String, Object> evaluationStandardMap = sqlQuery.queryForMap(
							"select TZ_BZFB_BL,TZ_BZFB_NUM,TZ_YXWC_NUM from PS_TZ_CPFB_BZH_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? AND TZ_SCORE_MODAL_ID=? and TZ_SCORE_ITEM_ID=? and TZ_M_FBDZ_ID=? and TZ_M_FBDZ_MX_ID=?",
							new Object[] {classId,batchId,TZ_ZLPS_SCOR_MD_ID,TZ_SCORE_ITEM_ID,TZ_M_FBDZ_ID,TZ_M_FBDZ_MX_ID});
					if (evaluationStandardMap != null) {
						BigDecimal BD_TZ_BZFB_BL = (BigDecimal) evaluationStandardMap.get("TZ_BZFB_BL");
						BigDecimal BD_TZ_BZFB_NUM = (BigDecimal) evaluationStandardMap.get("TZ_BZFB_NUM");
						BigDecimal BD_TZ_YXWC_NUM = (BigDecimal) evaluationStandardMap.get("TZ_YXWC_NUM");
						
						if(BD_TZ_BZFB_BL!=null){
							TZ_BZFB_BL = BD_TZ_BZFB_BL.doubleValue();
						}
						if(BD_TZ_BZFB_NUM!=null){
							TZ_BZFB_NUM = BD_TZ_BZFB_NUM.doubleValue();
						}
						if(BD_TZ_YXWC_NUM!=null){
							TZ_YXWC_NUM = BD_TZ_YXWC_NUM.doubleValue();
						}
					}

					TZ_BZFB_NUM = (double)Math.round((double)totalEvaluationNum*TZ_BZFB_BL/100);

					//评分误差
					long deviation = Math.round(Math.abs((Integer)evaluationDataList.get(i).get("num_dange")-TZ_BZFB_NUM));
					
					//是否符合要求
					if(deviation>Math.abs(TZ_YXWC_NUM)){
						return false;
					}
				}
			}
		}
		
		return true;
	}
}
