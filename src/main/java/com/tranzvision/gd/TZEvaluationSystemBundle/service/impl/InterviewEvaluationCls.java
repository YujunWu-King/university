package com.tranzvision.gd.TZEvaluationSystemBundle.service.impl;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 材料评审相关：数据查询和处理类
 * 
 * @author ShaweYet
 * @since 2017/03/06
 */
public class InterviewEvaluationCls{
	
	//获取考生搜索结果
	static Map<String,Object> getSearchResult(SqlQuery sqlQuery, String classId, String batchId, String srch_msid, String srch_name){
		
		Map<String,Object> rtn = new HashMap<String,Object>();
		
		String sqlString = "SELECT A.TZ_APP_INS_ID,B.OPRID,C.TZ_REALNAME,C.TZ_MSH_ID FROM PS_TZ_MSPS_KSH_TBL A,PS_TZ_FORM_WRK_T B,PS_TZ_AQ_YHXX_TBL C where A.TZ_CLASS_ID = B.TZ_CLASS_ID and A.TZ_APP_INS_ID = B.TZ_APP_INS_ID and B.OPRID=C.OPRID";
		String sqlWhere = "";
		Object[] searchObj = null;

		if((srch_msid==null&&"".equals(srch_msid))&&(srch_name ==null||"".equals(srch_name))){
			rtn.put("result", false);
			rtn.put("msg", "请输入查询关键字！");
			
			return rtn;
		}

		if(srch_msid!=null&&!"".equals(srch_msid)&&srch_name!=null&&!"".equals(srch_msid)){
			sqlWhere = " AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND C.TZ_MSH_ID=? AND C.TZ_REALNAME=?";
			sqlString = sqlString + sqlWhere;
			searchObj = new Object[]{classId,batchId,srch_msid,srch_name};
		}else{
			  if(srch_msid!=null&&!"".equals(srch_msid)){
				  sqlWhere = " AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND C.TZ_MSH_ID=?";
					sqlString = sqlString + sqlWhere;
					searchObj = new Object[]{classId,batchId,srch_msid};
			  }
			  
			  if(srch_name!=null&&!"".equals(srch_name)){
				  sqlWhere = " AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND C.TZ_REALNAME=?";
					sqlString = sqlString + sqlWhere;
					searchObj = new Object[]{classId,batchId,srch_name};
			  }
		}
		
		List<Map<String, Object>> list = sqlQuery.queryForList(sqlString,
				searchObj);
		
		int unum = 0;
		if(list!=null){
			
			for(int i=0;i<list.size();i++){
				unum = i+1;
				if(i>1){
					break;
				}
				BigInteger appInsId = (BigInteger)list.get(i).get("TZ_APP_INS_ID");
				String appOprId = (String)list.get(i).get("OPRID");
				String realName = (String)list.get(i).get("TZ_REALNAME");
				String msId = (String)list.get(i).get("TZ_MSH_ID");
				
				rtn.put("result", true);
				rtn.put("appInsId",appInsId);
				rtn.put("oprId",appOprId);
				rtn.put("realName",realName);
				rtn.put("msId",msId);
			}
		}

		if(unum == 0){
			rtn.put("result", false);
			rtn.put("msg", "未查找到相关考生信息。");
		}

		if(unum > 1){
			rtn.put("result", false);
			rtn.put("msg", "查找到多个相关考生信息，请输入准确的检索条件。");
		}
		
		return rtn;
	}
	
	//平均分计算（包括：单个评委和多个评委的情况）;
	static double calculateAverage(SqlQuery sqlQuery,String classId,String batchId,String oprid,String scoreItemId,int error_code,String error_decription){
		   
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
				//没有传值，取面试评审总分成绩项;
				String TZ_SCORE_MODAL_ID = sqlQuery.queryForObject("select TZ_MSCJ_SCOR_MD_ID from PS_TZ_CLASS_INF_T where TZ_CLASS_ID=?", 
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

		   String str_dgpw_all_num = sqlQuery.queryForObject(
			   		"select sum( b.TZ_SCORE_NUM) from PS_TZ_MP_PW_KS_TBL a,PS_TZ_CJX_TBL b where a.TZ_SCORE_INS_ID = b.TZ_SCORE_INS_ID  and a.TZ_CLASS_ID = ? and a.TZ_APPLY_PC_ID=? and a.TZ_PWEI_OPRID = ? and b.TZ_SCORE_ITEM_ID = ? AND a.TZ_DELETE_ZT <> 'Y' AND a.TZ_PSHEN_ZT = 'Y'",
			   		new Object[] { classId, batchId ,oprid,scoreItemId }, "String");		   
		   if(str_dgpw_all_num!=null&&!"".equals(str_dgpw_all_num)){
			   dgpw_all_num = Double.parseDouble(str_dgpw_all_num);
		   }
		   
		   //完成数量
		   String str_dgpw_wc_num = sqlQuery.queryForObject(
			   		"select count(distinct TZ_APP_INS_ID) from PS_TZ_MP_PW_KS_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID= ? and TZ_PWEI_OPRID = ?  AND TZ_DELETE_ZT <> 'Y' and  TZ_PSHEN_ZT = 'Y'",
			   		new Object[] { classId, batchId ,oprid }, "String");		   
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
	static List<Map<String,Object>> getScoreItemEvaluationData(SqlQuery sqlQuery,String classId,String batchId,String oprid,String scoreItemId,int error_code,String error_decription){

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
		String TZ_SCORE_MODAL_ID = sqlQuery.queryForObject("select TZ_MSCJ_SCOR_MD_ID from PS_TZ_CLASS_INF_T where TZ_CLASS_ID=?", 
				new Object[]{classId}, "String");
		   
		//分布对照id
		String TZ_M_FBDZ_ID = sqlQuery.queryForObject(
		   		"SELECT TZ_M_FBDZ_ID FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID = ? AND TZ_M_FBDZ_ID<>'' AND TZ_M_FBDZ_ID is not null limit 0,1 ",
		   		new Object[] { TZ_SCORE_MODAL_ID}, "String");


		//完成数量;
		int wc_num = 0;
		String str_wc_num = sqlQuery.queryForObject(
		   		"select count(distinct TZ_APP_INS_ID) from PS_TZ_MP_PW_KS_TBL where TZ_CLASS_ID= ? and TZ_APPLY_PC_ID = ? and TZ_PWEI_OPRID = ? and  TZ_DELETE_ZT <> 'Y' AND TZ_PSHEN_ZT = 'Y'",
		   		new Object[] { classId, batchId ,oprid}, "String");		   
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
				   		new StringBuffer("select count(distinct a.TZ_APP_INS_ID) from PS_TZ_MP_PW_KS_TBL a ,PS_TZ_CJX_TBL b where a.TZ_SCORE_INS_ID = b.TZ_SCORE_INS_ID  AND ")
				   		.append("a.TZ_DELETE_ZT <> 'Y' AND a.TZ_PSHEN_ZT = 'Y'  and  a.TZ_CLASS_ID = ? ")
				   		.append("and a.TZ_APPLY_PC_ID= ? and a.TZ_PWEI_OPRID = ? and b.TZ_SCORE_ITEM_ID = ? ")
				   		.append("and b.TZ_SCORE_NUM ").append(TZ_M_FBDZ_MX_XX_JX).append(TZ_M_FBDZ_MX_XX)
				   		.append(" and b.TZ_SCORE_NUM ").append(TZ_M_FBDZ_MX_SX_JX).append(TZ_M_FBDZ_MX_SX).toString(),
				   		new Object[] { classId, batchId ,oprid ,scoreItemId}, "String");		
				
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
}
