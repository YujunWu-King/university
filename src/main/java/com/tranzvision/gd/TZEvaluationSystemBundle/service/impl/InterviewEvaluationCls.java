package com.tranzvision.gd.TZEvaluationSystemBundle.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.sql.SqlQuery;

import com.tranzvision.gd.TZMbaPwMspsBundle.dao.PsTzMspskspwTblMapper;
import com.tranzvision.gd.TZMbaPwMspsBundle.dao.PsTzMpPwKsTblMapper;
import com.tranzvision.gd.TZMbaPwMspsBundle.model.PsTzMspskspwTbl;
import com.tranzvision.gd.TZMbaPwMspsBundle.model.PsTzMpPwKsTbl;

/**
 * 材料评审相关：数据查询和处理类
 * 
 * @author ShaweYet
 * @since 2017/03/06
 */
@Service
public class InterviewEvaluationCls{
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private PsTzMspskspwTblMapper psTzMspskspwTblMapper;
	@Autowired
	private PsTzMpPwKsTblMapper psTzMpPwKsTblMapper;
	
	//获取考生搜索结果
	protected Map<String,Object> getSearchResult(String classId, String batchId, String srch_msid, String srch_name){
		
		Map<String,Object> rtn = new HashMap<String,Object>();
		
		String sqlString = "SELECT A.TZ_APP_INS_ID,B.OPRID,C.TZ_REALNAME,C.TZ_MSH_ID FROM PS_TZ_MSPS_KSH_TBL A,PS_TZ_FORM_WRK_T B,PS_TZ_AQ_YHXX_TBL C where A.TZ_CLASS_ID = B.TZ_CLASS_ID and A.TZ_APP_INS_ID = B.TZ_APP_INS_ID and B.OPRID=C.OPRID";
		String sqlWhere = "";
		Object[] searchObj = null;

		if((srch_msid==null&&"".equals(srch_msid))&&(srch_name ==null||"".equals(srch_name))){
			rtn.put("result", false);
			rtn.put("msg", "请输入查询关键字！");
			
			return rtn;
		}

		if(srch_msid!=null&&!"".equals(srch_msid)&&srch_name!=null&&!"".equals(srch_name)){
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
	
	//移除考生和评委关系
	protected Map<String,Object> removeJudgeApplicant(String classId, String batchId, String appInsId, String oprId){
		
		Map<String,Object> rtn = new HashMap<String,Object>();
		
		Boolean result = true;
		String msg = "";
		
		//判断是否已经整体提交，整体提交后不能再删除
		String if_TZ_SUBMIT_YN = sqlQuery.queryForObject("select TZ_SUBMIT_YN from PS_TZ_MSPWPSJL_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID= ? and TZ_PWEI_OPRID= ?", 
				new Object[]{classId,batchId,oprId}, "String");
		if("Y".equals(if_TZ_SUBMIT_YN)){
			result = false;
			msg = "评议数据已经提交，不允许对考生数据进行修改。";
			rtn.put("result", result);
			rtn.put("msg", msg);
			return rtn;
		}

		//判断该评委的账号状态，若为“暂停”，则不能移除
		String if_TZ_PWZH_YN = sqlQuery.queryForObject("select TZ_PWEI_ZHZT from PS_TZ_MSPS_PW_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=? and TZ_PWEI_OPRID=?", 
				new Object[]{classId,batchId,oprId}, "String");
		if("B".equals(if_TZ_PWZH_YN)){
			result = false;
			msg = "您的账号暂时无法评议该批次，请与管理员联系。";
			rtn.put("result", result);
			rtn.put("msg", msg);
			return rtn;
		}

		//判断当前批次总体评审状态，若为“关闭”，则不能移除
		String if_TZ_DQPY_ZT = sqlQuery.queryForObject("select TZ_DQPY_ZT from PS_TZ_MSPS_GZ_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=?", 
				new Object[]{classId,batchId}, "String");
		if("B".equals(if_TZ_DQPY_ZT)){
			result = false;
			msg = "该批次的评审已关闭";
			rtn.put("result", result);
			rtn.put("msg", msg);
			return rtn;
		}


		//查询是否存在相关记录
		int numDelnum = sqlQuery.queryForObject("select count(TZ_APP_INS_ID) as delnum from PS_TZ_MP_PW_KS_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=? and TZ_PWEI_OPRID=?", 
				new Object[]{classId,batchId,appInsId,oprId}, "Integer");

		if(numDelnum ==0){
			result = false;
			msg = "移除失败，没有相关记录，请联系管理员！";
		}else{
			sqlQuery.update("update PS_TZ_MP_PW_KS_TBL set TZ_DELETE_ZT='Y' where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=? and TZ_PWEI_OPRID=?", 
					new Object[]{classId,batchId,appInsId,oprId});

			//将该评委从 TZ_MSPSKSPW_TBL 表中的评委列表字段中删除
			String strJudgeList = sqlQuery.queryForObject("select TZ_MSPW_LIST from PS_TZ_MSPSKSPW_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID = ? and TZ_APP_INS_ID = ?", 
					new Object[]{classId,batchId,appInsId}, "String");
			
			if(strJudgeList!=null&&!"".equals(strJudgeList)){
				int judgeIndex = strJudgeList.indexOf(oprId);
				if(judgeIndex>-1){
					//若列表中存在该评委，则将其清除;
					ArrayList<String> judgeList = new ArrayList<String>(Arrays.asList(strJudgeList.split(",")));
					Iterator<String> iter = judgeList.iterator(); 
					while (iter.hasNext()) { 						 
						if (iter.next().equals(oprId)) {						 
						iter.remove();
						}
					}
					String newJudgeList = String.join(",",(String[]) judgeList.toArray(new String[judgeList.size()]));
					
					sqlQuery.update("update PS_TZ_MSPSKSPW_TBL set TZ_MSPW_LIST = ?,ROW_LASTMANT_DTTM = ?,ROW_LASTMANT_OPRID =? where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?", 
							new Object[]{newJudgeList,new Date(),oprId,classId,batchId,appInsId});
				
				}
			
				//重新计算排名;
				updateRanking(classId,batchId,oprId);
			}
		}
		
		rtn.put("result", result);
		rtn.put("msg", msg);
		return rtn;
	}
	
	//添加考生和评委关系
	protected Map<String,Object> addJudgeApplicant(String classId, String batchId, String appInsId, String oprId){
		
		Map<String,Object> rtn = new HashMap<String,Object>();
		
		Boolean result = true;
		String msg = "";
		
		//判断是否已经整体提交，整体提交后不能添加
		String if_TZ_SUBMIT_YN = sqlQuery.queryForObject("select TZ_SUBMIT_YN from PS_TZ_MSPWPSJL_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID= ? and TZ_PWEI_OPRID= ?", 
				new Object[]{classId,batchId,oprId}, "String");
		if("Y".equals(if_TZ_SUBMIT_YN)){
			result = false;
			msg = "评议数据已经提交，不允许新增考生评议数据。";
			rtn.put("result", result);
			rtn.put("msg", msg);
			return rtn;
		}
		
		//判断该评委的账号状态，若为“暂停”，则不能添加
		String if_TZ_PWZH_YN = sqlQuery.queryForObject("select TZ_PWEI_ZHZT from PS_TZ_MSPS_PW_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=? and TZ_PWEI_OPRID=?", 
				new Object[]{classId,batchId,oprId}, "String");
		if("B".equals(if_TZ_PWZH_YN)){
			result = false;
			msg = "您的账号暂时无法评议该批次，请与管理员联系。";
			rtn.put("result", result);
			rtn.put("msg", msg);
			return rtn;
		}
		
		//判断当前批次总体评审状态，若为“关闭”，则不能添加
		String if_TZ_DQPY_ZT = sqlQuery.queryForObject("select TZ_DQPY_ZT from PS_TZ_MSPS_GZ_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=?", 
				new Object[]{classId,batchId}, "String");
		if("B".equals(if_TZ_DQPY_ZT)){
			result = false;
			msg = "该批次的评审已关闭";
			rtn.put("result", result);
			rtn.put("msg", msg);
			return rtn;
		}
		
		//若考生评委关系已存在，且“移除”状态为“未移除”，则不再建立
		int unum = sqlQuery.queryForObject("select count(TZ_APP_INS_ID) from PS_TZ_MP_PW_KS_TBL where TZ_DELETE_ZT<>'Y' and TZ_CLASS_ID = ? and TZ_APPLY_PC_ID = ? and TZ_APP_INS_ID = ? and TZ_PWEI_OPRID = ?", 
				new Object[]{classId,batchId,appInsId,oprId}, "Integer");
		if(unum>0){
			rtn.put("result", result);
			return rtn;
		}
		
		//判断当前考生是否达到评委上限，若已达到，则不能再被新评委评审;
		Integer numKSPW = 0, numKSPWSX = 0;
		numKSPW = sqlQuery.queryForObject("select count(TZ_APP_INS_ID) from PS_TZ_MP_PW_KS_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID = ? and TZ_APP_INS_ID = ? and TZ_DELETE_ZT<>'Y'", 
				new Object[]{classId,batchId,appInsId}, "Integer");

		if(numKSPW>0){
			numKSPWSX = sqlQuery.queryForObject("select TZ_MSPY_NUM from PS_TZ_MSPS_GZ_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID = ?", 
					new Object[]{classId,batchId}, "Integer");
			numKSPWSX = numKSPWSX==null?0:numKSPWSX;
			
			if(numKSPWSX>numKSPW){
				//判断当前评委是否与评议该考生的其他评委在同一组;
				String strIsOneGroup = sqlQuery.queryForObject("select 'Y' from PS_TZ_MSPS_PW_TBL where TZ_PWEI_GRPID in (select TZ_PWEI_GRPID from PS_TZ_MSPS_PW_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=? and TZ_PWEI_OPRID in (select TZ_PWEI_OPRID from PS_TZ_MP_PW_KS_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=? and TZ_DELETE_ZT<>'Y')) and TZ_PWEI_OPRID=? and TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=?", 
						new Object[]{classId,batchId,classId,batchId,appInsId,oprId,classId,batchId}, "String");
				if(!"Y".equals(strIsOneGroup)){
					result = false;
					msg = "您与其他评委不在同一组，无法评议该考生！";
					rtn.put("result", result);
					rtn.put("msg", msg);
					return rtn;
				}
			}else{
				result = false;
				msg = "该考生已完成评审，无需再评审！";
				rtn.put("result", result);
				rtn.put("msg", msg);
				return rtn;
			}
		}
		
		//开始添加评委考生数据
		PsTzMpPwKsTbl psTzMpPwKsTbl  = new PsTzMpPwKsTbl();
		psTzMpPwKsTbl.setTzClassId(classId);
		psTzMpPwKsTbl.setTzApplyPcId(batchId);
		psTzMpPwKsTbl.setTzAppInsId(Long.parseLong(appInsId));
		psTzMpPwKsTbl.setTzPweiOprid(oprId);
		psTzMpPwKsTbl.setTzDeleteZt("N");
		psTzMpPwKsTbl.setTzMshiKssj(new Date());
		psTzMpPwKsTbl.setRowLastmantDttm(new Date());
		psTzMpPwKsTbl.setRowLastmantOprid(oprId);
		
		String mppwksExist = sqlQuery.queryForObject("SELECT 'Y' FROM PS_TZ_MP_PW_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? AND TZ_PWEI_OPRID=?", 
				new Object[]{classId,batchId,Long.parseLong(appInsId),oprId},"String");
		if("Y".equals(mppwksExist)){			
			psTzMpPwKsTblMapper.updateByPrimaryKeySelective(psTzMpPwKsTbl);
		}else{
			psTzMpPwKsTbl.setTzPshenZt("N");
			psTzMpPwKsTbl.setRowAddedDttm(new Date());
			psTzMpPwKsTbl.setRowAddedOprid(oprId);
			psTzMpPwKsTblMapper.insertSelective(psTzMpPwKsTbl);
		}

		//更新 TZ_MSPSKSPW_TBL 的 TZ_MSPW_LIST 字段;
		String strJudgeList = "",strNewJudgeList;
		Map<String, Object> map = sqlQuery.queryForMap("select TZ_MSPW_LIST from PS_TZ_MSPSKSPW_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?", 
				new Object[]{classId,batchId,appInsId});
		if(map!=null){
			strJudgeList = (String)map.get("TZ_MSPW_LIST");
			if(strJudgeList!=null&&!"".equals(strJudgeList)){
				if(strJudgeList.indexOf(oprId)>-1){
					strNewJudgeList = strJudgeList;
				}else{
					strNewJudgeList = strJudgeList+","+oprId;
				}
			}else{
				strNewJudgeList = oprId;
			}
		}else{
			strNewJudgeList = oprId;
		}
		
		PsTzMspskspwTbl psTzMspskspwTbl = new PsTzMspskspwTbl();
		psTzMspskspwTbl.setTzClassId(classId);
		psTzMspskspwTbl.setTzApplyPcId(batchId);
		psTzMspskspwTbl.setTzAppInsId(Long.parseLong(appInsId));
		psTzMspskspwTbl.setTzMspwList(strNewJudgeList);
		psTzMspskspwTbl.setRowLastmantDttm(new Date());
		psTzMspskspwTbl.setRowLastmantOprid(oprId);
		if(map!=null){
			psTzMspskspwTblMapper.updateByPrimaryKeySelective(psTzMspskspwTbl);
		}else{
			psTzMspskspwTbl.setRowAddedDttm(new Date());
			psTzMspskspwTbl.setRowAddedOprid(oprId);
			psTzMspskspwTblMapper.insertSelective(psTzMspskspwTbl);
		}

		//重新计算排名;
		updateRanking(classId,batchId,oprId);
		
		rtn.put("result", result);
		return rtn;
	}
	
	//重新计算排名
	private void updateRanking(String classId,String batchId,String oprId){
		//根据传入的评委与报考方向信息，从 “MBA材料评审评委考生关系表（TZ_MP_PW_KS_TBL）”中获取评委的所有考生（未删除） Person id、总成绩 ;
		Map<BigInteger,Double> applicants = new HashMap<BigInteger,Double>();

		BigInteger TZ_APP_INS_ID;
		BigInteger TZ_SCORE_INS_ID;

		String sql = "select TZ_APP_INS_ID,TZ_SCORE_INS_ID from PS_TZ_MP_PW_KS_TBL WHERE TZ_CLASS_ID = ? and TZ_APPLY_PC_ID= ? AND TZ_PWEI_OPRID= ? and TZ_DELETE_ZT<>'Y'";
		List<Map<String,Object>> scoreInsList = sqlQuery.queryForList(sql, new Object[]{classId,batchId,oprId});
		
		if(scoreInsList!=null){
			for(int i=0;i<scoreInsList.size();i++){
				TZ_APP_INS_ID = scoreInsList.get(i).get("TZ_APP_INS_ID")!=null?(BigInteger)scoreInsList.get(i).get("TZ_APP_INS_ID"):null;
				TZ_SCORE_INS_ID = scoreInsList.get(i).get("TZ_SCORE_INS_ID")!=null?(BigInteger)scoreInsList.get(i).get("TZ_SCORE_INS_ID"):null;
			
				String TREE_NODE_ZF = sqlQuery.queryForObject("SELECT A.TREE_NODE FROM PSTREENODE A,PS_TZ_RS_MODAL_TBL B,PS_TZ_SRMBAINS_TBL C WHERE A.TREE_NAME = B.TREE_NAME and B.TZ_SCORE_MODAL_ID = C.TZ_SCORE_MODAL_ID AND A.PARENT_NODE_NUM=0 AND C.TZ_SCORE_INS_ID=? limit 0,1", 
						new Object[]{TZ_SCORE_INS_ID}, "String");

				Double ZF = sqlQuery.queryForObject("SELECT TZ_SCORE_NUM FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID=?", 
							new Object[]{TZ_SCORE_INS_ID,TREE_NODE_ZF}, "Double");
				
				if(TZ_APP_INS_ID!=null){
					applicants.put(TZ_APP_INS_ID, ZF!=null?ZF:(double)0);
				}
			}
		}
		
		if(applicants.size()>0){
			
			for (Entry<BigInteger, Double> entry : applicants.entrySet()) {
				//计算排名
				int rank = 1;
				for (Entry<BigInteger, Double> entry2 : applicants.entrySet()) {
					if(!entry.getKey().equals(entry2.getKey())){
						if(entry2.getValue()>entry.getValue()){
							rank++;
						}
					}
				}
				
				//更新排名
				sqlQuery.update("update PS_TZ_MP_PW_KS_TBL set TZ_KSH_PSPM=? where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=? and TZ_PWEI_OPRID=?", 
						new Object[]{String.valueOf(rank),classId,batchId,entry.getKey(),oprId});				
			}
		}
	}
	
}
