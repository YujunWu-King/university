package com.tranzvision.gd.TZEvaluationSystemBundle.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.TZMbaPwMspsBundle.dao.psTzMspwpsjlTblMapper;
import com.tranzvision.gd.TZMbaPwMspsBundle.model.psTzMspwpsjlTbl;
import com.tranzvision.gd.TZEvaluationSystemBundle.service.impl.InterviewEvaluationCls;

/**
 * 材料面试评审
 * 
 * @author ShaweYet
 * @since 2017/03/06
 */
@Service("com.tranzvision.gd.TZEvaluationSystemBundle.service.impl.InterviewEvaluationImpl")
public class InterviewEvaluationImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private psTzMspwpsjlTblMapper psTzMspwpsjlTblMapper;
	
	@Override
	public String tzGetJsonData(String strParams) {

		String strReturn = "";
		try {
			String type = request.getParameter("type");

			// 获取批次列表
			if ("list".equals(type)) {
				strReturn = getBatchList(strParams);
			}
			// 获取下一个考生
			if ("check".equals(type)) {
				strReturn = checkJudgeAccount(strParams);
			}
			// 获取批次信息：评审说明，统计信息区，考生列表
			if ("data".equals(type)) {
				strReturn = getBatchData(strParams);
			}
			// 搜索考生数据
			if ("search".equals(type)){
				strReturn = searchApplicant(strParams);
			}
			// 提交全部考生数据
			if ("submit".equals(type)) {
				strReturn = submitAllData(strParams);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strReturn;
	}

	private String searchApplicant(String strParams){
		String classId = request.getParameter("BaokaoClassID"); /*字符串，请求班级编号*/
		String batchId = request.getParameter("BaokaoPCID"); /*字符串，请求报考批次编号*/
		String srch_msid = request.getParameter("KSH_SEARCH_MSID"); /*考生面试申请号（搜索条件）*/
		String srch_name = request.getParameter("KSH_SEARCH_NAME"); /*考生姓名（搜索条件）*/
		
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		
		Map<String,Object> userInfo;
		
		userInfo = InterviewEvaluationCls.getSearchResult(sqlQuery,classId, batchId, srch_msid, srch_name);
		
		//判断返回是否有效值
		if((boolean)userInfo.get("result")==true){
			 //取出用户数据;
			 String str_class_name = "", str_pc_name = "";
			 
			 str_class_name = sqlQuery.queryForObject("select TZ_CLASS_NAME from PS_TZ_CLASS_INF_T where TZ_CLASS_ID=?", 
					 new Object[]{classId}, "String");
			 str_pc_name = sqlQuery.queryForObject("select TZ_BATCH_NAME from PS_TZ_CLS_BATCH_T where TZ_CLASS_ID=? and TZ_BATCH_ID=?", 
					 new Object[]{classId,batchId}, "String");
			 
			 rtnMap.put("success", true);
			 rtnMap.put("ps_class_id", classId);
			 rtnMap.put("ps_class_name", str_class_name);
			 rtnMap.put("ps_pc_id", batchId);
			 rtnMap.put("ps_pc_name", str_pc_name);
			 rtnMap.put("ps_ksh_xm", userInfo.get("realName"));
			 rtnMap.put("ps_ksh_msid",  userInfo.get("msId"));
			 rtnMap.put("ps_ksh_bmbid", userInfo.get("appInsId"));
			 rtnMap.put("error_code", 0);
			 rtnMap.put("error_decription", "");
			 
		}else{
			rtnMap.put("error_code", -1);
			rtnMap.put("error_decription", userInfo.get("msg"));
		}

		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(rtnMap);
	}
	
	private String checkJudgeAccount(String strParams){

		String classId = request.getParameter("BaokaoClassID"); /* 字符串，请求班级编号 */
		String batchId = request.getParameter("BaokaoPCID"); /* 字符串，请求报考批次编号 */
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		
		//判断该评委的账号状态，若为“暂停”，则不能再新增
		String if_TZ_PWZH_YN = sqlQuery.queryForObject("select TZ_PWEI_ZHZT from PS_TZ_MSPS_PW_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=? and TZ_PWEI_OPRID=?", 
				new Object[]{classId,batchId,oprid}, "String");

		if(if_TZ_PWZH_YN==null){
			if_TZ_PWZH_YN = "";
		}

		Map<String, Object> rtnMap = new HashMap<String, Object>();
		rtnMap.put("status", if_TZ_PWZH_YN);

		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(rtnMap);
	}
	
	private String getBatchList(String strParams) {

		String maxRowCount = request.getParameter("MaxRowCount"); /* 数字，返回最大行数。如果不指定，默认返回10条新闻或者通知记录 */
		String startRowNumber = request.getParameter("StartRowNumber"); /* 数字，返回起始行数，即从第几行开始返回。如果不指定，默认从第一行开始返回 */
		String moreRowsFlag = request.getParameter("MoreRowsFlag"); /* 字符串，更多行标志，即是否还有更多的数据。Y，是；N，否 */

		try {
			int error_code = 0;
			String error_decription = "";

			if (moreRowsFlag == null || "".equals(moreRowsFlag)) {
				error_code = 1;
				error_decription = "参数不全";
			}

			if (maxRowCount == null || "".equals(maxRowCount)) {
				maxRowCount = "10";
			}

			if (startRowNumber == null || "".equals(startRowNumber)) {
				startRowNumber = "0";
			} else {
				startRowNumber = String.valueOf(Integer.parseInt(startRowNumber) - 1);
			}

			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String userName = sqlQuery.queryForObject("SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?",
					new Object[] { oprid }, "String");

			String sql = "select A.TZ_CLASS_ID,C.TZ_CLASS_NAME,A.TZ_APPLY_PC_ID,D.TZ_BATCH_NAME,B.TZ_DQPY_ZT from PS_TZ_MSPS_PW_TBL A ,PS_TZ_MSPS_GZ_TBL B ,PS_TZ_CLASS_INF_T C ,PS_TZ_CLS_BATCH_T D where A.TZ_CLASS_ID=B.TZ_CLASS_ID and A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID and B.TZ_DQPY_ZT='A' and  A.TZ_PWEI_OPRID=? and A.TZ_CLASS_ID = C.TZ_CLASS_ID and A.TZ_CLASS_ID = D.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID = D.TZ_BATCH_ID ORDER BY B.TZ_PYKS_RQ DESC,cast(A.TZ_APPLY_PC_ID as SIGNED INTEGER) DESC LIMIT ?,?";
			List<Map<String, Object>> list = sqlQuery.queryForList(sql,
					new Object[] { oprid, Integer.parseInt(startRowNumber), Integer.parseInt(maxRowCount) });

			String strClassId = "", strClassName = "", strBatchId = "", strBatchName = "", strEvalationStatus = "",
					strEvalationStatusDesc = "";
			ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> mapList = new HashMap<String, Object>();

					strClassId = (String) list.get(i).get("TZ_CLASS_ID");
					strClassName = (String) list.get(i).get("TZ_CLASS_NAME");
					strBatchId = (String) list.get(i).get("TZ_APPLY_PC_ID");
					strBatchName = (String) list.get(i).get("TZ_BATCH_NAME");
					strEvalationStatus = (String) list.get(i).get("TZ_DQPY_ZT");

					if ("A".equals(strEvalationStatus)) {
						strEvalationStatusDesc = "进行中";
					}
					if ("B".equals(strEvalationStatus)) {
						strEvalationStatusDesc = "已结束";
					}
					if ("N".equals(strEvalationStatus)) {
						strEvalationStatusDesc = "未开始";
					}
					mapList.put("class_id", strClassId);
					mapList.put("class_name", strClassName);
					mapList.put("pc_id", strBatchId);
					mapList.put("pc_name", strBatchName);
					mapList.put("pc_ztid", strEvalationStatus);
					mapList.put("pc_zt", strEvalationStatusDesc);

					listData.add(mapList);
				}
			}
			// 总行数;
			String totalSql = "select count(*) from PS_TZ_MSPS_PW_TBL a, PS_TZ_MSPS_GZ_TBL b where a.TZ_PWEI_OPRID=? and  a.TZ_CLASS_ID=b.TZ_CLASS_ID and a.TZ_APPLY_PC_ID=b.TZ_APPLY_PC_ID and b.TZ_DQPY_ZT ='A' and a.TZ_CLASS_ID in (select TZ_CLASS_ID from PS_TZ_CLASS_INF_T )";
			int totalRowCount = sqlQuery.queryForObject(totalSql, new Object[] { oprid }, "Integer");

			// 是否有更多数据
			if ((Integer.parseInt(maxRowCount) + Integer.parseInt(startRowNumber)) >= totalRowCount) {
				moreRowsFlag = "N";
			} else {
				moreRowsFlag = "Y";
			}

			Map<String, Object> rtnMap = new HashMap<String, Object>();
			rtnMap.put("pw_id", oprid);
			rtnMap.put("pw_name", userName);
			rtnMap.put("MaxRowCount", maxRowCount);
			rtnMap.put("StartRowNumber", String.valueOf(Integer.parseInt(startRowNumber) + 1));
			rtnMap.put("MoreRowsFlag", moreRowsFlag);
			rtnMap.put("TotalRowCount", totalRowCount);
			rtnMap.put("data", listData);
			rtnMap.put("error_code", error_code);
			rtnMap.put("error_decription", error_decription);

			JacksonUtil jacksonUtil = new JacksonUtil();
			return jacksonUtil.Map2json(rtnMap);
		} catch (Exception e) {
			e.printStackTrace();
			Map<String,Object> expMap = new HashMap<String,Object>();
			JacksonUtil jacksonUtil = new JacksonUtil();
			expMap.put("error_code", "1");
			expMap.put("error_decription",e.toString());
			return jacksonUtil.Map2json(expMap);
		}

	}

	private String getBatchData(String strParams) {

		String requestDataType = request.getParameter("RequestDataType"); /*字符串，请求数据类型。A，返回全部数据，S，返回局部动态刷新数据。此接口该参数取值A*/
		String classId = request.getParameter("BaokaoClassID"); /* 字符串，请求班级编号 */
		String batchId = request.getParameter("BaokaoPCID"); /* 字符串，请求报考批次编号 */
		String maxRowCount = request.getParameter("MaxRowCount"); /* 数字，返回最大行数。如果不指定，默认返回10条 */
		String startRowNumber = request.getParameter("StartRowNumber"); /* 数字，返回起始行数，即从第几行开始返回。如果不指定，默认从第一行开始返回 */
		String moreRowsFlag = request.getParameter("MoreRowsFlag"); /* 字符串，更多行标志，即是否还有更多的数据。Y，是；N，否 */

		String KS_MOREN_PX = request.getParameter("KS_MOREN_PX"); /*字符串，考生默认排序字段（按排名排序），为空则按默认排序，否则按面试顺序*/
		
		String ps_description = "", ps_gaiy_info = "", error_decription = "";
		String TZ_APPLY_PCH = "";
		String TZ_MSPS_SCOR_MD_ID = "", TZ_SCORE_ITEM_ID = "";
		String TZ_M_FBDZ_ID = "", TREE_NAME = "";
		int error_code = 0, TotalRowCount = 0,  tz_done_num = 0;

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		
		try {
			// 班级名称、报名模板表编号
			String str_class_name = "";
			Map<String, Object> classInfoMap = sqlQuery.queryForMap(
					"select TZ_CLASS_NAME,TZ_MSCJ_SCOR_MD_ID,TZ_APP_MODAL_ID from PS_TZ_CLASS_INF_T where TZ_CLASS_ID=?",
					new Object[] { classId });
			if (classInfoMap != null) {
				str_class_name = (String) classInfoMap.get("TZ_CLASS_NAME");
				TZ_MSPS_SCOR_MD_ID = (String) classInfoMap.get("TZ_MSCJ_SCOR_MD_ID");
			}

			//评委是否可见统计图表，评委是否可见分布图
			String strTZ_PWKJ_TJB = "Y";
			String strTZ_PWKJ_FBT = "Y";
			Map<String, Object> map1 = sqlQuery.queryForMap(
					"select TZ_PWKJ_TJB,TZ_PWKJ_FBT from PS_TZ_MSPS_GZ_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=?",
					new Object[] { classId ,batchId});
			if (map1 != null) {
				strTZ_PWKJ_TJB = (String) map1.get("TZ_PWKJ_TJB");
				strTZ_PWKJ_FBT = (String) map1.get("TZ_PWKJ_FBT");
			}
			
			//是否计算分数
			String str_jsfs = "Y";
			Map<String, Object> map2 = sqlQuery.queryForMap(
					"SELECT TREE_NAME,TZ_M_FBDZ_ID FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=? AND TZ_JG_ID=?",
					new Object[] { TZ_MSPS_SCOR_MD_ID ,orgid});
			if (map2 != null) {
				/* 树名称、分布对照id（总分） */
				TREE_NAME = (String) map2.get("TREE_NAME");
				TZ_M_FBDZ_ID = (String) map2.get("TZ_M_FBDZ_ID");
				//str_jsfs = (String) map2.get("TZ_JSFS");
			}

			if (moreRowsFlag == null || "".equals(moreRowsFlag)) {
				moreRowsFlag = "N";
			}
			if (maxRowCount == null || "".equals(maxRowCount)) {
				maxRowCount = "10";
			}
			if (startRowNumber == null || "".equals(startRowNumber)) {
				startRowNumber = "0";
			} else {
				startRowNumber = String.valueOf(Integer.parseInt(startRowNumber) - 1);
			}

			if (requestDataType == null || "".equals(requestDataType) || classId == null || "".equals(classId)
					|| batchId == null || "".equals(batchId)) {
				error_code = 1;
				error_decription = "参数不全";
			} else {
				if (TZ_MSPS_SCOR_MD_ID == null || "".equals(TZ_MSPS_SCOR_MD_ID)) {
					error_code = 2;
					error_decription = "当前报考班级没有配置成绩模型";
				} else {
					if (TREE_NAME == null || "".equals(TREE_NAME)) {
						error_code = 3;
						error_decription = "报考班级对应的成绩模型没有配置对应的成绩树";
					}
				}
			}

			// 评委评审详细说明与通知信息
			ps_description = sqlQuery.queryForObject(
					"select TZ_MSPS_SM from PS_TZ_MSPS_GZ_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=?",
					new Object[] { classId, batchId },"String");
			ps_description = ps_description ==null?"":ps_description;

			// 当评审说明区域存在：由word复制粘贴过来的表格时，进行处理，添加div;
			String table_css = "<style type='text/css'>	#pwxxsm_id  td{  border:1px solid Grey;  }</style>";
			ps_description = new StringBuffer(table_css).append("<div id=\"pwxxsm_id\">").append(ps_description)
					.append("</div>").toString();
			
			/* 完成的数量 */
			tz_done_num = sqlQuery.queryForObject(
					"select count(TZ_APP_INS_ID) from PS_TZ_MP_PW_KS_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=? and TZ_PWEI_OPRID= ? and TZ_DELETE_ZT<>'Y' and TZ_PSHEN_ZT='Y'",
					new Object[] { classId, batchId, oprid}, "Integer");
			ps_gaiy_info = "我已评审" + tz_done_num + "人。"; /* 评委评审概要信息 */


			/* 第2部分 当前评委打分平均分统计信息 */
			List<Map<String,Object>> secondPwdfThList= new ArrayList<Map<String,Object>>();
			Map<String, Object> secondPwdfTh1 = new HashMap<String, Object>();
			secondPwdfTh1.put("col01", "指标名称");
			secondPwdfTh1.put("ps_cht_flg", "N");
			secondPwdfTh1.put("ps_grp_flg", "N");
			Map<String, Object> secondPwdfTh2 = new HashMap<String, Object>();
			secondPwdfTh2.put("col02", "评分平均分");
			secondPwdfTh2.put("ps_cht_flg", "Y");
			secondPwdfTh2.put("ps_grp_flg", "N");
			
			secondPwdfThList.add(secondPwdfTh1);
			secondPwdfThList.add(secondPwdfTh2);
			
			// 总分成绩项
			TZ_SCORE_ITEM_ID = sqlQuery.queryForObject(
					"SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? and PARENT_NODE_NUM=0",
					new Object[] { TREE_NAME }, "String");
			
			// 计算平均分
			double pjf = 0;
			
			pjf = InterviewEvaluationCls.calculateAverage(sqlQuery, classId, batchId, oprid, TZ_SCORE_ITEM_ID, error_code, error_decription);
			 
			List<Map<String,Object>> sjfzRowList= new ArrayList<Map<String,Object>>();
			Map<String, Object> sjfzRow1 = new HashMap<String, Object>();
			sjfzRow1.put("col01", "总分");			
			Map<String, Object> sjfzRow2 = new HashMap<String, Object>();
			sjfzRow1.put("col02", pjf);			
			sjfzRowList.add(sjfzRow1);
			sjfzRowList.add(sjfzRow2);
			
			Map<String,Object> second_total_map = new HashMap<String, Object>();			
			second_total_map.put("ps_tjzb_btmc", secondPwdfThList);
			second_total_map.put("ps_tjzb_mxsj", sjfzRowList);
			
			if("Y".equals(str_jsfs)){
				 ps_gaiy_info = ps_gaiy_info + "平均分：" + pjf;	
			}
				   
			/* 第3部分 当前评委打分分布统计信息 */
			//int TOTALPWRS = tz_done_num;/*完成数量*/

			List<Map<String,Object>> evaluationDataList = InterviewEvaluationCls.getScoreItemEvaluationData(sqlQuery, classId, batchId, oprid, TZ_SCORE_ITEM_ID, error_code, error_decription);

			List<Map<String,Object>> fbsjrow = new ArrayList<Map<String,Object>>();
			
			if(evaluationDataList!=null){
				for(int i=0;i<evaluationDataList.size();i++){
					String FBMS = sqlQuery.queryForObject(
							"SELECT TZ_M_FBDZ_MX_SM FROM PS_TZ_FBDZ_MX_TBL WHERE TZ_M_FBDZ_ID=? AND TZ_M_FBDZ_MX_ID=?",
							new Object[] { TZ_M_FBDZ_ID ,(String)evaluationDataList.get(i).get("mx_id")}, "String");
					
					Map<String,Object> fbsjrowItem = new HashMap<String,Object>();
					fbsjrowItem.put("ps_fb_mc", FBMS);
					fbsjrowItem.put("ps_sjfb_bilv", (Double)evaluationDataList.get(i).get("num_rate")+"%");
					fbsjrowItem.put("ps_sjfb_rshu", (Integer)evaluationDataList.get(i).get("num_dange"));
					
					fbsjrow.add(fbsjrowItem);
				}
			}

			// 成绩项名称;
			String DESCR2 = sqlQuery.queryForObject(
					"SELECT DESCR  FROM PS_TZ_MODAL_DT_TBL WHERE TREE_NAME=? and TZ_SCORE_ITEM_ID=?",
					new Object[] { TREE_NAME, TZ_SCORE_ITEM_ID }, "String");

			List<Map<String, Object>> thdFbList = new ArrayList<Map<String, Object>>();
			Map<String, Object> thdFbMap = new HashMap<String, Object>();
			thdFbMap.put("ps_fszb_mc", DESCR2);
			List<String> ps_cht_flds= new ArrayList<String>();
			ps_cht_flds.add("ps_sjfb_bilv");
			thdFbMap.put("ps_cht_flds", ps_cht_flds);
			thdFbMap.put("ps_fszb_fbsj", fbsjrow);
			thdFbList.add(thdFbMap);
			
			/* 第4部分 当前评委已评审考生统计信息 */
			Map<String,Object> dyColThMap= new HashMap<String,Object>();			
			String TZ_XS_MC2 = "";
			int dyColNum2 = 0;

			String sql1 = "select TZ_SCORE_ITEM_ID,TZ_XS_MC from PS_TZ_CJ_BPH_TBL where TZ_SCORE_MODAL_ID=? AND TZ_JG_ID=? AND TZ_ITEM_S_TYPE = 'A' order by TZ_PX";
			List<Map<String, Object>> scoreModalList = sqlQuery.queryForList(sql1, new Object[] { TZ_MSPS_SCOR_MD_ID ,orgid});

			if (scoreModalList != null) {
				for (int i = 0; i < scoreModalList.size(); i++) {
					TZ_XS_MC2 = (String) scoreModalList.get(i).get("TZ_XS_MC");

					dyColNum2 = dyColNum2 + 1;
					String strDyColNum2 = "0" + dyColNum2;
					
					dyColThMap.put("col"+strDyColNum2.substring(strDyColNum2.length() - 2), TZ_XS_MC2);
				}
			}			
			if("Y".equals(str_jsfs)){
				dyColThMap.put("ps_ksh_ppm", "排名");
			}
			
			Long TZ_APP_INS_ID;
			String forthSql;
			if(KS_MOREN_PX==null||"".equals(KS_MOREN_PX)){
				forthSql = "select TZ_APP_INS_ID from PS_TZ_MP_PW_KS_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=? and TZ_PWEI_OPRID=? and TZ_DELETE_ZT <>'Y' order by cast(TZ_KSH_PSPM as SIGNED INTEGER) ASC";
			}else{
				forthSql = "select TZ_APP_INS_ID from PS_TZ_MP_PW_KS_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=? and TZ_PWEI_OPRID=? and TZ_DELETE_ZT <>'Y' order by ROW_ADDED_DTTM ASC";
			}
			
			List<Map<String, Object>> applicantsList = sqlQuery.queryForList(forthSql,
					new Object[] { classId, batchId, oprid });

			List<Map<String,Object>> dyRowValue= new ArrayList<Map<String,Object>>();
			if (applicantsList != null) {				
				int xh = 0;
				for (int i = 0; i < applicantsList.size(); i++) {
					TZ_APP_INS_ID = ((BigInteger) applicantsList.get(i).get("TZ_APP_INS_ID")).longValue();

					xh = xh + 1;

					if (xh >= Integer.parseInt(startRowNumber) + 1
							&& xh < (Integer.parseInt(startRowNumber) + Integer.parseInt(maxRowCount))) {

						// 考生编号;
						String ksbh = "";
						ksbh = String.valueOf(TZ_APP_INS_ID);

						// 考生姓名;
						String first_name = "";
						Map<String, Object> map3 = sqlQuery.queryForMap(
								"select A.OPRID,B.TZ_REALNAME from PS_TZ_FORM_WRK_T A,PS_TZ_REG_USER_T B where A.OPRID = B.OPRID and A.TZ_APP_INS_ID=?",
								new Object[] { TZ_APP_INS_ID });
						if (map3 != null) {
							first_name = (String) map3.get("TZ_REALNAME");
						}

						//组内排名;
						String TZ_KSH_PSPM2 = "";
						// 评议状态;
						String pyZt = "";
						// 评审时间;
						String pssj = "";
						//考生成绩单ID
						BigInteger cjdId = null;
						
						Map<String, Object> map4 = sqlQuery.queryForMap(
								"select A.TZ_KSH_PSPM,date_format(A.ROW_LASTMANT_DTTM, '%Y-%m-%d %H:%i') ROW_LASTMANT_DTTM ,(SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_PSHEN_ZT' AND TZ_ZHZ_ID=A.TZ_PSHEN_ZT AND TZ_EFF_STATUS='A') AS TZ_PSHEN_ZT,TZ_SCORE_INS_ID from PS_TZ_MP_PW_KS_TBL A WHERE A.TZ_CLASS_ID=? and A.TZ_APPLY_PC_ID=? AND A.TZ_APP_INS_ID=? AND A.TZ_PWEI_OPRID=?",
								new Object[] { classId, batchId, TZ_APP_INS_ID, oprid });
						if (map4 != null) {
							TZ_KSH_PSPM2 = (String) map4.get("OPRID");
							pssj = (String) map4.get("ROW_LASTMANT_DTTM");
							pyZt = (String) map4.get("TZ_PSHEN_ZT");
							cjdId = (BigInteger) map4.get("TZ_SCORE_INS_ID");
							
						}

						/* 动态列的值 */
						Map<String, Object> dyRowValueItem = new HashMap<String, Object>();
						
						String TZ_SCORE_ITEM_ID3 = "";
						int dyColNum = 0;
						
						if (scoreModalList != null) {
							for (int j = 0; j < scoreModalList.size(); j++) {
								TZ_SCORE_ITEM_ID3 = (String) scoreModalList.get(j).get("TZ_SCORE_ITEM_ID");

								// 判断成绩项的类型，下拉框转换为分值;
								String TZ_SCORE_ITEM_TYPE = "",TZ_SCR_TO_SCORE = "";
								Map<String, Object> map5= sqlQuery.queryForMap(
										"select TZ_SCORE_ITEM_TYPE,TZ_SCR_TO_SCORE from PS_TZ_MODAL_DT_TBL where TREE_NAME=? and TZ_SCORE_ITEM_ID=?",
										new Object[] { TREE_NAME, TZ_SCORE_ITEM_ID3 });
								if(map5!=null){
									TZ_SCORE_ITEM_TYPE = (String)map5.get("TZ_SCORE_ITEM_TYPE");
									TZ_SCR_TO_SCORE = (String)map5.get("TZ_SCR_TO_SCORE");
								}
								
								dyColNum = dyColNum + 1;

								// 得到分值;
								double TZ_SCORE_NUM = 0;
								String TZ_SCORE_PY_VALUE = "";

								Map<String, Object> map6 = sqlQuery.queryForMap(
										"select TZ_SCORE_NUM,TZ_SCORE_PY_VALUE from PS_TZ_CJX_TBL where TZ_SCORE_INS_ID=? and TZ_SCORE_ITEM_ID=?",
										new Object[] { cjdId, TZ_SCORE_ITEM_ID3});
								if (map6 != null) {
									Object OBJ_TZ_SCORE_NUM = map6.get("TZ_SCORE_NUM");
									TZ_SCORE_NUM = OBJ_TZ_SCORE_NUM!=null?((BigDecimal)OBJ_TZ_SCORE_NUM).doubleValue():0;
									TZ_SCORE_PY_VALUE = (String) map6.get("TZ_SCORE_PY_VALUE");
								}

								String strDyColNum = "0" + dyColNum;
								strDyColNum = strDyColNum.substring(strDyColNum.length() - 2);

								/*动态列
								 * 成绩项类型：A-数字成绩汇总项,B-数字成绩录入项,C-评语,D-下拉框
								 * From KitchenSink.view.scoreModelManagement.scoreModelTreeNodeWin
								 */
								if ("A".equals(TZ_SCORE_ITEM_TYPE) || "B".equals(TZ_SCORE_ITEM_TYPE)
										||("D".equals(TZ_SCORE_ITEM_TYPE)&&"Y".equals(TZ_SCR_TO_SCORE))) {
									//成绩录入项、成绩汇总项、下拉框且转换为分值   取分数，否则取评语;
							        dyRowValueItem.put("col"+strDyColNum,String.valueOf(TZ_SCORE_NUM));
								} else {
									if ("D".equals(TZ_SCORE_ITEM_TYPE)) {
										// 如果是下拉框，需要取得下拉框名称，此处存的是下拉框编号;
										String str_xlk_desc = sqlQuery.queryForObject(
												"select TZ_CJX_XLK_XXMC from PS_TZ_ZJCJXXZX_T where TREE_NAME=? and TZ_SCORE_ITEM_ID=? and TZ_CJX_XLK_XXBH=?",
												new Object[] { TREE_NAME, TZ_SCORE_ITEM_ID3, TZ_SCORE_PY_VALUE },
												"String");

										TZ_SCORE_PY_VALUE = str_xlk_desc;

									}
							        dyRowValueItem.put("col"+strDyColNum,TZ_SCORE_PY_VALUE);
								}
							}
						}

						if (TZ_KSH_PSPM2 == null || "".equals(TZ_KSH_PSPM2)) {
							TZ_KSH_PSPM2 = "0";
						}
				        
						dyRowValueItem.put("ps_ksh_id",ksbh);
				        dyRowValueItem.put("ps_ksh_bmbid",String.valueOf(TZ_APP_INS_ID));
				        dyRowValueItem.put("ps_ksh_xm",first_name);
				        dyRowValueItem.put("ps_ksh_zt",pyZt);
				        dyRowValueItem.put("ps_ksh_dt",pssj);				      
				        dyRowValueItem.put("ps_row_id", ksbh);
				        
						//KS_MOREN_PX 为空时按排名排序，否则按面试顺序排序;
						if(KS_MOREN_PX==null||"".equals(KS_MOREN_PX)){

							int xh_an_pm = 0; /*面试顺序*/

							String str_xh_an_pm = sqlQuery.queryForObject("select C.num from (select TZ_APP_INS_ID,@rowno:=@rowno + 1 AS num from PS_TZ_MP_PW_KS_TBL A,(SELECT @rowno:=0) B where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=? and TZ_PWEI_OPRID=? and TZ_DELETE_ZT <>'Y' order by ROW_ADDED_DTTM ASC) C where C.TZ_APP_INS_ID = ?", 
									new Object[]{classId, batchId, oprid, TZ_APP_INS_ID}, "String");							
							if(str_xh_an_pm!=null){
								xh_an_pm = Integer.parseInt(str_xh_an_pm);
							}

							dyRowValueItem.put("ps_ksh_xh",String.valueOf(xh_an_pm));
					        
							if("Y".equals(str_jsfs)){
						        dyRowValueItem.put("ps_ksh_ppm",TZ_KSH_PSPM2);
							}
							
						}else{
							
							dyRowValueItem.put("ps_ksh_xh",String.valueOf(xh));
					        
					        if("Y".equals(str_jsfs)){
						        dyRowValueItem.put("ps_ksh_ppm",TZ_KSH_PSPM2);
							}
						}
				        
				        dyRowValue.add(dyRowValueItem);

					}

					// 如果行数大于最大一条的行数则;
					if ((Integer.parseInt(maxRowCount) + Integer.parseInt(startRowNumber)) >= xh) {
						moreRowsFlag = "N";
					} else {
						moreRowsFlag = "Y";
					}

					// 共多少条;
					TotalRowCount = xh;
				}
			}
			
			Map<String,Object> forthMap = new HashMap<String,Object>();
			forthMap.put("ps_ksh_list_headers", dyColThMap);
			forthMap.put("ps_ksh_list_contents", dyRowValue);

			//是否已经全部提交了;
			String ps_kslb_submtall = sqlQuery.queryForObject(
					"select TZ_SUBMIT_YN from PS_TZ_MSPWPSJL_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=? and TZ_PWEI_OPRID=?",
					new Object[] { classId, batchId,oprid}, "String");

			//取得批次名称;
			TZ_APPLY_PCH = sqlQuery.queryForObject(
					"select TZ_BATCH_NAME from PS_TZ_CLS_BATCH_T where TZ_CLASS_ID=? and TZ_BATCH_ID=?",
					new Object[] { classId, batchId}, "String");
			
			Map<String, Object> allDataMap = new HashMap<String, Object>();
			if("A".equals(requestDataType)){
				allDataMap.put("ps_class_id",classId);
				allDataMap.put("ps_class_mc",str_class_name);
				allDataMap.put("ps_pc_id",batchId);
				allDataMap.put("ps_pc_name",TZ_APPLY_PCH);
				allDataMap.put("ps_description",ps_description);
				allDataMap.put("ps_gaiy_info",ps_gaiy_info);
				allDataMap.put("ps_data_cy",second_total_map);
				allDataMap.put("ps_data_fb",thdFbList);
				allDataMap.put("MaxRowCount",maxRowCount);
				allDataMap.put("StartRowNumber",startRowNumber);
				allDataMap.put("MoreRowsFlag",moreRowsFlag);
				allDataMap.put("TotalRowCount",TotalRowCount);
				allDataMap.put("ps_data_kslb",forthMap);
				allDataMap.put("ps_kslb_submtall",ps_kslb_submtall);
				allDataMap.put("ps_pwkj_tjb",strTZ_PWKJ_TJB);
				allDataMap.put("ps_pwkj_fbt",strTZ_PWKJ_FBT);
				allDataMap.put("error_code",error_code);
				allDataMap.put("error_decription",error_decription);
				allDataMap.put("ps_display_fs",str_jsfs);
			}else{
				/*返回局部动态刷新数据*/
				allDataMap.put("ps_class_id",classId);
				allDataMap.put("ps_gaiy_info",ps_gaiy_info);
				allDataMap.put("ps_data_cy",second_total_map);
				allDataMap.put("ps_data_fb",thdFbList);
				allDataMap.put("ps_data_kslb",forthMap);
				allDataMap.put("error_code",error_code);
				allDataMap.put("error_decription",error_decription);
				allDataMap.put("ps_kslb_submtall",ps_kslb_submtall);
				allDataMap.put("ps_pwkj_tjb",strTZ_PWKJ_TJB);
				allDataMap.put("ps_pwkj_fbt",strTZ_PWKJ_FBT);
				allDataMap.put("ps_pc_id",batchId);
				allDataMap.put("ps_display_fs",str_jsfs);
			}

			JacksonUtil jacksonUtil = new JacksonUtil();
			return jacksonUtil.Map2json(allDataMap);
		} catch (Exception e) {
			e.printStackTrace();
			Map<String,Object> expMap = new HashMap<String,Object>();
			JacksonUtil jacksonUtil = new JacksonUtil();
			expMap.put("error_code", "1");
			expMap.put("error_decription",e.toString());
			return jacksonUtil.Map2json(expMap);
		}

	}

	
	private String submitAllData(String strParams) {

		String batchId = request.getParameter("BaokaoPCID"); /*请求报考批次编号*/
		String classId =request.getParameter("BaokaoClassID"); /*字符串，请求班级编号*/
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		
		String error_code = "", error_decription = "";
		
	    //班级名称
		String className = "";

		//判断当前评委是否是暂停状态
		String TZ_PWEI_ZHZT = sqlQuery.queryForObject("select TZ_PWEI_ZHZT from PS_TZ_MSPS_PW_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=? and TZ_PWEI_OPRID=?", 
				new Object[]{classId,batchId,oprid}, "String");
		
		if(TZ_PWEI_ZHZT==null||"B".equals(TZ_PWEI_ZHZT)){
			error_code = "JUDGE_PAUSE";
			error_decription = "当前评委账号为暂停状态";
		}else{
			 //判断当前评委是否已经提交;
			 String TZ_SUBMIT_YN = sqlQuery.queryForObject("select TZ_SUBMIT_YN from PS_TZ_MSPWPSJL_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID = ? and TZ_PWEI_OPRID = ?",
					 new Object[]{classId,batchId,oprid}, "String");
			 
			 if("Y".equals(TZ_SUBMIT_YN)){
				 error_code = "SUBMITTED";
				 error_decription = "当前评委账号已经提交，不能再提交";
			 }else{
				 //判断当前批次总体评审状态，若为“关闭”，则不能再打分;
				 String if_TZ_DQPY_ZT = sqlQuery.queryForObject("select TZ_DQPY_ZT from PS_TZ_MSPS_GZ_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=?",
						 new Object[]{classId,batchId,oprid}, "String");
				 
				 if("B".equals(if_TZ_DQPY_ZT)){
					 error_code = "EVALUATION_CLOSED";
					 error_decription = "该批次的评审已关闭";
				 }else{
					   
					   /*所有考生的提交状态都是“已提交”*/
					   String submit_zt = sqlQuery.queryForObject("select 'Y' from PS_TZ_MP_PW_KS_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID = ? and TZ_PWEI_OPRID = ? and TZ_PSHEN_ZT<>'Y'and TZ_PSHEN_ZT<>'C' and TZ_DELETE_ZT<>'Y'", 
								new Object[]{classId,batchId,oprid},"String");
					   
					   if("Y".equals(submit_zt)){
						   error_decription = "存在未评审的考生";
						   error_code = "SUBMTALL03";
					   }else{
						   String mspwpsjlExist = sqlQuery.queryForObject("select 'Y' from PS_TZ_MSPWPSJL_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=? and TZ_PWEI_OPRID=? and TZ_SUBMIT_YN<>'Y'", 
						   			new Object[]{classId,batchId,oprid},"String");
						   
						   psTzMspwpsjlTbl psTzMspwpsjlTbl = new psTzMspwpsjlTbl();
						   psTzMspwpsjlTbl.setTzClassId(classId);
						   psTzMspwpsjlTbl.setTzApplyPcId(batchId);
						   psTzMspwpsjlTbl.setTzPweiOprid(oprid);
						   psTzMspwpsjlTbl.setTzSubmitYn("Y");
						   psTzMspwpsjlTbl.setRowLastmantOprid(oprid);
						   psTzMspwpsjlTbl.setRowLastmantDttm(new Date());
						    
							if("Y".equals(mspwpsjlExist)){
								psTzMspwpsjlTblMapper.updateByPrimaryKeySelective(psTzMspwpsjlTbl);
							}else{
								psTzMspwpsjlTbl.setRowAddedOprid(oprid);
								psTzMspwpsjlTbl.setRowAddedDttm(new Date());
								psTzMspwpsjlTblMapper.insertSelective(psTzMspwpsjlTbl);
							}
							
							error_decription = "";;
							error_code = "0";
					   } 
				 }
				 
			 }
		}

		Map<String,Object> retMap = new HashMap<String,Object>();
		retMap.put("success", true);
		retMap.put("ps_class_id", classId);
		retMap.put("ps_class_name", className);
		retMap.put("ps_bkpc_id", batchId);
		retMap.put("operation_type", "SUBMTALL");
		retMap.put("error_code", error_code);
		retMap.put("error_decription", error_decription);

		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(retMap);
	}
}
