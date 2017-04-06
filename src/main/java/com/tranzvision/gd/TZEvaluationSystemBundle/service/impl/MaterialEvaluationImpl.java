package com.tranzvision.gd.TZEvaluationSystemBundle.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
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

/**
 * 材料面试评审
 * 
 * @author ShaweYet
 * @since 2017/03/06
 */
@Service("com.tranzvision.gd.TZEvaluationSystemBundle.service.impl.MaterialEvaluationImpl")
public class MaterialEvaluationImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Override
	public String tzGetJsonData(String strParams) {

		String strReturn = "";
		try {
			String type = request.getParameter("type");

			// 获取批次列表
			if ("list".equals(type)) {
				strReturn = this.getBatchList(strParams);
			}
			// 获取批次信息：评审说明，统计信息区，考生列表
			if ("data".equals(type)) {
				strReturn = this.getBatchData(strParams);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return strReturn;
	}

	public String getBatchList(String strParams) {

		String maxRowCount = request.getParameter(
				"MaxRowCount"); /* 数字，返回最大行数。如果不指定，默认返回10条新闻或者通知记录 */
		String startRowNumber = request.getParameter(
				"StartRowNumber"); /* 数字，返回起始行数，即从第几行开始返回。如果不指定，默认从第一行开始返回 */
		String moreRowsFlag = request.getParameter(
				"MoreRowsFlag"); /* 字符串，更多行标志，即是否还有更多的数据。Y，是；N，否 */

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

			String sql = "SELECT A.TZ_CLASS_ID  ,B.TZ_CLASS_NAME  ,A.TZ_APPLY_PC_ID  ,TZ_BATCH_NAME ,TZ_PWEI_OPRID  ,D.TZ_DQPY_ZT   FROM PS_TZ_CLPS_PW_TBL A   ,PS_TZ_CLASS_INF_T B   ,PS_TZ_CLPS_GZ_TBL D   ,PS_TZ_CLS_BATCH_T E WHERE A.TZ_CLASS_ID = B.TZ_CLASS_ID    AND A.TZ_CLASS_ID = D.TZ_CLASS_ID    AND A.TZ_APPLY_PC_ID = D.TZ_APPLY_PC_ID    AND D.TZ_DQPY_ZT IN('A','B')    AND A.TZ_PWEI_OPRID=?   AND D.TZ_CLASS_ID = E.TZ_CLASS_ID    AND D.TZ_APPLY_PC_ID = E.TZ_BATCH_ID   ORDER BY D.TZ_PYKS_RQ DESC,cast(A.TZ_APPLY_PC_ID as SIGNED INTEGER) DESC LIMIT ?,?";
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
			String totalSql = "select count(*) from PS_TZ_CLPS_PW_TBL a, PS_TZ_CLPS_GZ_TBL b where a.TZ_PWEI_OPRID=? and  a.TZ_CLASS_ID=b.TZ_CLASS_ID and a.TZ_APPLY_PC_ID=b.TZ_APPLY_PC_ID and b.TZ_DQPY_ZT IN('A','B') and a.TZ_CLASS_ID in (select TZ_CLASS_ID from PS_TZ_CLASS_INF_T )";
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

	public String getBatchData(String strParams) {

		String requestDataType = request.getParameter("RequestDataType"); /*字符串，请求数据类型。A，返回全部数据，S，返回局部动态刷新数据。此接口该参数取值A*/
		String classId = request.getParameter("BaokaoClassID"); /* 字符串，请求班级编号 */
		String batchId = request.getParameter("BaokaoPCID"); /* 字符串，请求报考批次编号 */
		String maxRowCount = request.getParameter("MaxRowCount"); /* 数字，返回最大行数。如果不指定，默认返回10条 */
		String startRowNumber = request.getParameter("StartRowNumber"); /* 数字，返回起始行数，即从第几行开始返回。如果不指定，默认从第一行开始返回 */
		String moreRowsFlag = request.getParameter("MoreRowsFlag"); /* 字符串，更多行标志，即是否还有更多的数据。Y，是；N，否 */

		String ps_description = "", ps_gaiy_info = "", error_decription = "";
		String TZ_APPLY_PCH = "";
		String TZ_ZLPS_SCOR_MD_ID = "",TREE_NAME = "";
		//String TZ_PYJS_RQ = "",TZ_PYJS_SJ = "";
		String TZ_M_FBDZ_ID = "";
		int error_code = 0, totalRowCount = 0,  tz_done_num = 0;

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		
		try {
			// 班级名称、报名表编号;
			String str_class_name = "";
			Map<String, Object> classInfoMap = sqlQuery.queryForMap(
					"select TZ_CLASS_NAME,TZ_ZLPS_SCOR_MD_ID,TZ_APP_MODAL_ID from PS_TZ_CLASS_INF_T where TZ_CLASS_ID=?",
					new Object[] { classId });
			if (classInfoMap != null) {
				str_class_name = (String) classInfoMap.get("TZ_CLASS_NAME");
				TZ_ZLPS_SCOR_MD_ID = (String) classInfoMap.get("TZ_ZLPS_SCOR_MD_ID");
			}

			// 评委是否可见评议标准数据;
			//String TZ_PWKJ_PYBZSJ = "Y";
			
			Map<String, Object> scoreModalMap = sqlQuery.queryForMap(
					"SELECT TREE_NAME,TZ_M_FBDZ_ID FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=? AND TZ_JG_ID=?",
					new Object[] { TZ_ZLPS_SCOR_MD_ID ,orgid});
			if (scoreModalMap != null) {
				/* 树名称、分布对照id（总分） */
				TREE_NAME = (String) scoreModalMap.get("TREE_NAME");
				TZ_M_FBDZ_ID = (String) scoreModalMap.get("TZ_M_FBDZ_ID");
			}

			// 评委评审详细说明与通知信息
			Map<String, Object> evaluationDescMap = sqlQuery.queryForMap(
					"select TZ_CLPS_SM,date_format(TZ_PYJS_RQ, '%Y-%m-%d') TZ_PYJS_RQ,date_format(TZ_PYJS_SJ, '%H:%i') TZ_PYJS_SJ from PS_TZ_CLPS_GZ_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=?",
					new Object[] { classId, batchId });
			if (evaluationDescMap != null) {
				ps_description = (String) evaluationDescMap.get("TZ_CLPS_SM");
				//TZ_PYJS_RQ = (String) evaluationDescMap.get("TZ_PYJS_RQ");
				//TZ_PYJS_SJ = (String) evaluationDescMap.get("TZ_PYJS_SJ");
			}

			// 当评审说明区域存在：由word复制粘贴过来的表格时，进行处理，添加div;
			String table_css = "<style type=\"text/css\">	#pwxxsm_id  td{  border:1px solid Grey;  }</style>";
			ps_description = new StringBuffer(table_css).append("<div id=\"pwxxsm_id\">").append(ps_description)
					.append("</div>").toString();

			// 评议考生上限
			/*
			TZ_PYKS_SX = sqlQuery.queryForObject(
					"select TZ_PYKS_SX from PS_TZ_CLPS_PW_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID = ? and TZ_PWEI_OPRID= ?",
					new Object[] { classId, batchId, oprid }, "String");
			*/
			
			/* 完成的数量 */
			tz_done_num = sqlQuery.queryForObject(
					"select count(TZ_APP_INS_ID) from PS_TZ_KSCLPSLS_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_PWEI_OPRID = ? and TZ_CLPS_LUNC in ( select TZ_DQPY_LUNC from PS_TZ_CLPS_GZ_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=?) and TZ_SUBMIT_YN = 'Y'",
					new Object[] { classId, batchId, oprid, classId, batchId }, "Integer");
			ps_gaiy_info = "您目前已完成了" + tz_done_num + "份。"; /* 评委评审概要信息 */

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
				if (TZ_ZLPS_SCOR_MD_ID == null || "".equals(TZ_ZLPS_SCOR_MD_ID)) {
					error_code = 2;
					error_decription = "当前报考班级没有配置成绩模型";
				} else {
					if (TREE_NAME == null || "".equals(TREE_NAME)) {
						error_code = 3;
						error_decription = "报考班级对应的成绩模型没有配置对应的成绩树";
					}
				}
			}


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

			// 当前轮次
			int TZ_DQPY_LUNC = 1;
			String STR_DQPY_LUNC = sqlQuery.queryForObject(
					"select TZ_DQPY_LUNC from PS_TZ_CLPS_GZ_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=?",
					new Object[] { classId, batchId }, "String");
			if(STR_DQPY_LUNC!=null&&!"".equals(STR_DQPY_LUNC)){
				TZ_DQPY_LUNC = Integer.parseInt(STR_DQPY_LUNC);
			}
			
			// 计算平均分
			int pjf = 0;

			// 总分成绩项
			String TZ_SCORE_ITEM_ID = sqlQuery.queryForObject(
					"SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? and PARENT_NODE_NUM=0",
					new Object[] { TREE_NAME }, "String");
			/*
			 * TODO Local TZ_CLPS:TZ_CLPS_PW_PY &obj_clps = create
			 * TZ_CLPS:TZ_CLPS_PW_PY(); &pjf =
			 * &obj_clps.TZ_CLPS_PJF(&TZ_CLASS_ID, &TZ_APPLY_PC_ID, %UserId,
			 * &TZ_SCORE_ITEM_ID);
			 **/
			 
			List<Map<String,Object>> sjfzRowList= new ArrayList<Map<String,Object>>();
			Map<String, Object> sjfzRow1 = new HashMap<String, Object>();
			sjfzRow1.put("col01", "评审结果");			
			Map<String, Object> sjfzRow2 = new HashMap<String, Object>();
			sjfzRow1.put("col02", pjf);			
			sjfzRowList.add(sjfzRow1);
			sjfzRowList.add(sjfzRow2);
			
			Map<String,Object> second_total_map = new HashMap<String, Object>();			
			second_total_map.put("ps_tjzb_btmc", secondPwdfThList);
			second_total_map.put("ps_tjzb_mxsj", sjfzRowList);
			

			/*int TOTALPWRS = sqlQuery.queryForObject(
					"SELECT COUNT(*) FROM PS_TZ_CP_PW_KS_TBL WHERE TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=?",
					new Object[] { classId, batchId, oprid }, "Integer");
			*/
			
			/*
			 * 第3部分 当前评委打分分布统计信息 TODO Local TZ_CLPS:TZ_CLPS_PW_PY &tz_zlps_cjx =
			 * create TZ_CLPS:TZ_CLPS_PW_PY(); Local array of array of any
			 * &anyArr = &tz_zlps_cjx.TZ_CLPS_PW_CJX_PY_DATA(%UserId,
			 * &TZ_CLASS_ID, &TZ_APPLY_PC_ID, &TZ_SCORE_ITEM_ID); Local number
			 * &i;
			 */
			String fbsjrow = "";
			/*
			 * For &i = 1 To &anyArr.Len
			 * 
			 * Local string &FBMS; SQLExec(
			 * "SELECT TZ_M_FBDZ_MX_SM FROM PS_TZ_FBDZ_MX_TBL WHERE TZ_M_FBDZ_ID=:1 AND TZ_M_FBDZ_MX_ID=:2"
			 * , &TZ_M_FBDZ_ID, &anyArr [&i][1], &FBMS); If All(&fbsjrow) Then
			 * &fbsjrow = &fbsjrow | "," | GetHTMLText(HTML.TZ_THIRD_PWDF_1,
			 * &FBMS, &anyArr [&i][4], &anyArr [&i][3]); Else &fbsjrow =
			 * GetHTMLText(HTML.TZ_THIRD_PWDF_1, &FBMS, &anyArr [&i][4], &anyArr
			 * [&i][3]); End-If;
			 * 
			 * End-For;
			 */

			// 成绩项名称;
			String DESCR2 = sqlQuery.queryForObject(
					"SELECT DESCR  FROM PS_TZ_MODAL_DT_TBL WHERE TREE_NAME=? and TZ_SCORE_ITEM_ID=?",
					new Object[] { TREE_NAME, TZ_SCORE_ITEM_ID }, "String");

			Map<String, Object> thdFbMap = new HashMap<String, Object>();
			thdFbMap.put("ps_fszb_mc", DESCR2);
			List<String> ps_cht_flds= new ArrayList<String>();
			ps_cht_flds.add("ps_sjfb_bilv");
			thdFbMap.put("ps_cht_flds", ps_cht_flds);
			thdFbMap.put("ps_fszb_fbsj", fbsjrow);
			
			/* 第4部分 当前评委已评审考生统计信息 */

			Map<String,Object> dyColThMap= new HashMap<String,Object>();			
			String TZ_XS_MC2 = "";
			int dyColNum2 = 0;

			String sql1 = "select TZ_SCORE_ITEM_ID,TZ_XS_MC from PS_TZ_CJ_BPH_TBL where TZ_SCORE_MODAL_ID=? AND TZ_ITEM_S_TYPE = 'A' order by TZ_PX";
			List<Map<String, Object>> scoreModalList = sqlQuery.queryForList(sql1, new Object[] { TZ_ZLPS_SCOR_MD_ID });

			if (scoreModalList != null) {
				for (int i = 0; i < scoreModalList.size(); i++) {
					TZ_XS_MC2 = (String) scoreModalList.get(i).get("TZ_XS_MC");

					dyColNum2 = dyColNum2 + 1;
					String strDyColNum2 = "0" + dyColNum2;
					
					dyColThMap.put("col"+strDyColNum2.substring(strDyColNum2.length() - 2), TZ_XS_MC2);
				}
			}

			Long TZ_APP_INS_ID;
			String forthSql = "select TZ_APP_INS_ID from PS_TZ_CP_PW_KS_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_PWEI_OPRID=?";

			/* 评委可见偏差 
			String TZ_PWKJ_PCH = sqlQuery.queryForObject(
					"select TZ_PWKJ_PCH from PS_TZ_CLPS_GZ_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=?",
					new Object[] { classId, batchId }, "String");
			 */
			
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

						// 考生人员ID;
						//String ksPerson_id = "";

						// 考生编号;
						String ksbh = "";
						ksbh = String.valueOf(TZ_APP_INS_ID);

						// 考生姓名;
						String first_name = "";
						Map<String, Object> map1 = sqlQuery.queryForMap(
								"select A.OPRID,B.TZ_REALNAME from PS_TZ_FORM_WRK_T A,PS_TZ_REG_USER_T B where A.OPRID = B.OPRID and A.TZ_APP_INS_ID=?",
								new Object[] { TZ_APP_INS_ID });
						if (map1 != null) {
							//ksPerson_id = (String) map1.get("OPRID");
							first_name = (String) map1.get("TZ_REALNAME");
						}

						// 上次排名;
						String TZ_KSH_PSPM = "";
						TZ_KSH_PSPM = sqlQuery.queryForObject(
								"select TZ_KSH_PSPM from PS_TZ_KSCLPSLS_TBL A,PS_TZ_CLPS_GZ_TBL B WHERE A.TZ_CLASS_ID=B.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID AND A.TZ_CLASS_ID=? and A.TZ_APPLY_PC_ID=? AND A.TZ_APP_INS_ID=? AND A.TZ_PWEI_OPRID=? AND A.TZ_CLPS_LUNC = B.TZ_SCPY_LUNC",
								new Object[] { classId, batchId, TZ_APP_INS_ID, oprid }, "String");
						
						// 本次排名;
						String TZ_KSH_PSPM2 = "";
						// 评议状态;
						String pyZt = "";
						// 偏差;
						//int pc = 0;

						// 评审时间;
						String pssj = "";
						Map<String, Object> map2 = sqlQuery.queryForMap(
								"select A.TZ_KSH_PSPM,date_format(A.ROW_LASTMANT_DTTM, '%Y-%m-%d %H:%i') ROW_LASTMANT_DTTM ,A.TZ_SUBMIT_YN from PS_TZ_KSCLPSLS_TBL A,PS_TZ_CLPS_GZ_TBL B,PS_TZ_CP_PW_KS_TBL C WHERE A.TZ_CLASS_ID=B.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID AND A.TZ_CLASS_ID=C.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=C.TZ_APPLY_PC_ID AND A.TZ_PWEI_OPRID=C.TZ_PWEI_OPRID AND A.TZ_APP_INS_ID=C.TZ_APP_INS_ID AND A.TZ_CLASS_ID=? and A.TZ_APPLY_PC_ID=? AND A.TZ_APP_INS_ID=? AND A.TZ_PWEI_OPRID=? AND A.TZ_CLPS_LUNC = B.TZ_DQPY_LUNC",
								new Object[] { classId, batchId, TZ_APP_INS_ID, oprid });
						if (map2 != null) {
							TZ_KSH_PSPM2 = (String) map2.get("OPRID");
							pssj = (String) map2.get("ROW_LASTMANT_DTTM");
							pyZt = (String) map2.get("TZ_SUBMIT_YN");
						}

						/* 动态列的值 */
						Map<String, Object> dyRowValueItem = new HashMap<String, Object>();
						
						String TZ_SCORE_ITEM_ID3 = "";
						int dyColNum = 0;

						/* 考生成绩单ID */
						int cjdId = 0;
						String strCjdId = sqlQuery.queryForObject(
								"select TZ_SCORE_INS_ID from PS_TZ_CP_PW_KS_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID= ? and TZ_APP_INS_ID=? and TZ_PWEI_OPRID=?",
								new Object[] { classId, batchId, TZ_APP_INS_ID, oprid }, "String");
						if(strCjdId!=null&&!"".equals(strCjdId)){
							cjdId = Integer.parseInt(strCjdId);
						}
						
						if (scoreModalList != null) {
							for (int j = 0; j < scoreModalList.size(); j++) {
								TZ_SCORE_ITEM_ID3 = (String) scoreModalList.get(j).get("TZ_SCORE_ITEM_ID");

								// 判断成绩项的类型;
								String TZ_SCORE_ITEM_TYPE = sqlQuery.queryForObject(
										"select TZ_SCORE_ITEM_TYPE from PS_TZ_MODAL_DT_TBL where TREE_NAME=? and TZ_SCORE_ITEM_ID=?",
										new Object[] { TREE_NAME, TZ_SCORE_ITEM_ID3 }, "String");

								dyColNum = dyColNum + 1;

								// 得到分值;
								float TZ_SCORE_NUM = 0;
								String TZ_SCORE_PY_VALUE = "";

								Map<String, Object> map3 = sqlQuery.queryForMap(
										"select TZ_SCORE_NUM,TZ_SCORE_PY_VALUE from PS_TZ_CJX_TBL where TZ_SCORE_INS_ID=? and TZ_SCORE_ITEM_ID=?",
										new Object[] { cjdId, TZ_SCORE_ITEM_ID3});
								if (map3 != null) {
									TZ_SCORE_NUM = ((BigDecimal) map3.get("TZ_SCORE_NUM")).floatValue();
									TZ_SCORE_PY_VALUE = (String) map3.get("TZ_SCORE_PY_VALUE");
								}

								String strDyColNum = "0" + dyColNum;
								strDyColNum = strDyColNum.substring(strDyColNum.length() - 2);

								//动态列
								if ("R".equals(TZ_SCORE_ITEM_TYPE) || "W".equals(TZ_SCORE_ITEM_TYPE)) {
							        dyRowValueItem.put("col"+strDyColNum,String.valueOf(TZ_SCORE_NUM));
								} else {
									if ("X".equals(TZ_SCORE_ITEM_TYPE)) {
										// 下拉框取描述信息;
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

						if (TZ_KSH_PSPM == null || "".equals(TZ_KSH_PSPM)) {
							TZ_KSH_PSPM = "0";
						}

						if (TZ_KSH_PSPM2 == null || "".equals(TZ_KSH_PSPM2)) {
							TZ_KSH_PSPM2 = "0";
						}
				        
						/*添加考生类别*/				         
				        String TZ_KSH_TYPE = sqlQuery.queryForObject(
								"select TZ_COLOR_NAME FROM PS_TZ_ORG_COLOR_V WHERE TZ_COLOR_SORT_ID=(select TZ_COLOR_SORT_ID from PS_TZ_FORM_WRK_T where TZ_APP_INS_ID=? limit 0,1) AND TZ_JG_ID=?",
								new Object[] { TZ_APP_INS_ID, orgid}, "String");
				        
				        dyRowValueItem.put("ps_ksh_xh",String.valueOf(xh));
				        dyRowValueItem.put("ps_ksh_id",ksbh);
				        dyRowValueItem.put("ps_ksh_bmbid",String.valueOf(TZ_APP_INS_ID));
				        dyRowValueItem.put("ps_ksh_xm",first_name);
				        dyRowValueItem.put("ps_ksh_ppm",TZ_KSH_PSPM);
				        dyRowValueItem.put("ps_ksh_cpm",TZ_KSH_PSPM2);
				        dyRowValueItem.put("ps_ksh_zt",pyZt);
				        dyRowValueItem.put("ps_ksh_dt",pssj);
				        dyRowValueItem.put("ps_ksh_type",TZ_KSH_TYPE);
				      
				        dyRowValueItem.put("ps_row_id", ksbh);
				        dyRowValue.add(dyRowValueItem);

					}

					// 如果行数大于最大一条的行数则;
					if ((Integer.parseInt(maxRowCount) + Integer.parseInt(startRowNumber)) >= xh) {
						moreRowsFlag = "N";
					} else {
						moreRowsFlag = "Y";
					}

					// 共多少条;
					totalRowCount = xh;
				}
			}
			
			Map<String,Object> forthMap = new HashMap<String,Object>();
			forthMap.put("ps_ksh_list_headers", dyColThMap);
			forthMap.put("ps_ksh_list_contents", dyRowValue);

			//是否已经全部提交了;
			String ps_kslb_submtall = sqlQuery.queryForObject(
					"select TZ_SUBMIT_YN from PS_TZ_CLPWPSLS_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID = ? and TZ_PWEI_OPRID = ? and TZ_CLPS_LUNC = ?",
					new Object[] { classId, batchId,oprid, TZ_DQPY_LUNC}, "String");

			//取得批次名称;
			TZ_APPLY_PCH = sqlQuery.queryForObject(
					"select TZ_BATCH_NAME from PS_TZ_CLS_BATCH_T where TZ_CLASS_ID=? and TZ_BATCH_ID=?",
					new Object[] { classId, batchId}, "String");
			
			Map<String, Object> allDataMap = new HashMap<String, Object>();
			allDataMap.put("ps_class_id",classId);
			allDataMap.put("ps_class_mc",str_class_name);
			allDataMap.put("ps_bkpc_id",batchId);
			allDataMap.put("ps_baok_pc",TZ_APPLY_PCH);
			allDataMap.put("ps_description",ps_description);
			allDataMap.put("ps_gaiy_info",ps_gaiy_info);
			allDataMap.put("MaxRowCount",maxRowCount);
			allDataMap.put("StartRowNumber",startRowNumber);
			allDataMap.put("MoreRowsFlag",moreRowsFlag);
			allDataMap.put("TotalRowCount",totalRowCount);
			allDataMap.put("ps_data_kslb",forthMap);
			allDataMap.put("error_code",error_code);
			allDataMap.put("error_decription",error_decription);
			allDataMap.put("ps_kslb_submtall",ps_kslb_submtall);
			allDataMap.put("ps_data_cy",second_total_map);
			allDataMap.put("ps_data_fb",thdFbMap);

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
}
