package com.tranzvision.gd.TZEvaluationSystemBundle.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.MySqlLockService;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.sql.type.TzSQLObject;
import com.tranzvision.gd.TZMaterialInterviewReviewBundle.dao.psTzClpwpslsTblMapper;
import com.tranzvision.gd.TZMaterialInterviewReviewBundle.model.psTzClpwpslsTbl;
import com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl.XmlToWord;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzClpskspwTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzCpPwKsTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzKsclpslsTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpskspwTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpskspwTblKey;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzCpPwKsTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzKsclpslsTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzKsclpslsTblKey;
import com.tranzvision.gd.TZEvaluationSystemBundle.service.impl.MaterialEvaluationCls;

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
	@Autowired
	private XmlToWord xmlToWord;
	@Autowired
	private MySqlLockService mySqlLockService;
	@Autowired
	private psTzClpwpslsTblMapper psTzClpspwlsTblMapper;
	@Autowired
	private PsTzKsclpslsTblMapper psTzKsclpslsTblMapper;
	@Autowired
	private PsTzClpskspwTblMapper psTzClpskspwTblMapper;
	@Autowired
	private PsTzCpPwKsTblMapper psTzCpPwKsTblMapper;
	@Autowired
	private MaterialEvaluationCls materialEvaluationCls;
	@Autowired
	private TZGDObject tzSQLObject;
	
	@Override
	public String tzGetJsonData(String strParams) {

		String strReturn = "";
		try {
			String type = request.getParameter("type");

			// 获取批次列表
			if ("list".equals(type)) {
				strReturn = getBatchList(strParams);
			}
			// 获取批次信息：评审说明，统计信息区，考生列表
			if ("data".equals(type)) {
				strReturn = getBatchData(strParams);
			}
			// 打印评审总表
			if ("print".equals(type)) {
				strReturn = printEvaluationList(strParams);
			}
			// 提交全部考生数据
			if ("submit".equals(type)) {
				strReturn = submitAllData(strParams);
			}
			// 获取下一个考生
			if ("next".equals(type)) {
				strReturn = getNextApplicant(strParams);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strReturn;
	}
	
	private String getBatchList(String strParams) {

		String maxRowCount = request.getParameter("MaxRowCount"); /*数字，返回最大行数。如果不指定，默认返回10条新闻或者通知记录 */
		String startRowNumber = request.getParameter("StartRowNumber"); /*数字，返回起始行数，即从第几行开始返回。如果不指定，默认从第一行开始返回 */
		String moreRowsFlag = request.getParameter("MoreRowsFlag"); /*字符串，更多行标志，即是否还有更多的数据。Y，是；N，否 */

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

			String sql = "SELECT A.TZ_CLASS_ID  ,B.TZ_CLASS_NAME  ,A.TZ_APPLY_PC_ID  ,TZ_BATCH_NAME ,TZ_PWEI_OPRID  ,D.TZ_DQPY_ZT ,A.TZ_PYKS_XX ,date_format(D.TZ_PYJS_RQ, '%Y-%m-%d') TZ_PYJS_RQ,(to_days(D.TZ_PYJS_RQ)-to_days(now())) TZ_DAYS FROM PS_TZ_CLPS_PW_TBL A   ,PS_TZ_CLASS_INF_T B   ,PS_TZ_CLPS_GZ_TBL D   ,PS_TZ_CLS_BATCH_T E WHERE A.TZ_CLASS_ID = B.TZ_CLASS_ID    AND A.TZ_CLASS_ID = D.TZ_CLASS_ID    AND A.TZ_APPLY_PC_ID = D.TZ_APPLY_PC_ID    AND D.TZ_DQPY_ZT IN('A','B')    AND A.TZ_PWEI_OPRID=?   AND D.TZ_CLASS_ID = E.TZ_CLASS_ID    AND D.TZ_APPLY_PC_ID = E.TZ_BATCH_ID ORDER BY D.TZ_DQPY_ZT ASC, D.TZ_PYKS_RQ DESC,cast(A.TZ_APPLY_PC_ID as SIGNED INTEGER) DESC LIMIT ?,?";
			List<Map<String, Object>> list = sqlQuery.queryForList(sql,
					new Object[] { oprid, Integer.parseInt(startRowNumber), Integer.parseInt(maxRowCount) });

			String strClassId = "", strClassName = "", strBatchId = "", strBatchName = "", strEvalationStatus = "",
					strEvalationStatusDesc = "";
			
			//列表数据和评委未完成评审提醒数据
			ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
			ArrayList<String> remindData = new ArrayList<String>();
			
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
					
					//评委未完成评审提醒
					if("A".equals(strEvalationStatus)){
						long tz_need_eva_num =  list.get(i).get("TZ_PYKS_XX")!=null?(Long)list.get(i).get("TZ_PYKS_XX"):0;
						
						/*完成的数量 */
						int tz_done_num = sqlQuery.queryForObject(
								"select count(TZ_APP_INS_ID) from PS_TZ_KSCLPSLS_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_PWEI_OPRID = ? and TZ_CLPS_LUNC in ( select TZ_DQPY_LUNC from PS_TZ_CLPS_GZ_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=?) and TZ_SUBMIT_YN = 'Y'",
								new Object[] { strClassId, strBatchId, oprid, strClassId, strBatchId }, "Integer");
						
						if(tz_need_eva_num>tz_done_num){
							String tz_end_date = (String) list.get(i).get("TZ_PYJS_RQ");
							int tz_days = (Integer) list.get(i).get("TZ_DAYS");
							
							/*评委评审概要信息 */
							String ps_gaiy_info = new StringBuffer(strClassName).append(" ").append(strBatchName)
									.append("，距离评审截止日期 <span style='color:red'>").append(tz_end_date).append("</span>")
									.append(tz_days>0?"还有":"已过去").append("<span style='color:red'>").append(Math.abs(tz_days))
									.append("</span>天，您有<span style='color:red'>").append(tz_need_eva_num-tz_done_num).append("</span>位考生未评审。").toString();
							
							remindData.add(ps_gaiy_info);
						}
						
					}
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
			rtnMap.put("remindData", remindData);
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
		String classId = request.getParameter("BaokaoClassID"); /*字符串，请求班级编号 */
		String batchId = request.getParameter("BaokaoPCID"); /*字符串，请求报考批次编号 */
		String maxRowCount = request.getParameter("MaxRowCount"); /*数字，返回最大行数。如果不指定，默认返回10条 */
		String startRowNumber = request.getParameter("StartRowNumber"); /*数字，返回起始行数，即从第几行开始返回。如果不指定，默认从第一行开始返回 */
		String moreRowsFlag = request.getParameter("MoreRowsFlag"); /*字符串，更多行标志，即是否还有更多的数据。Y，是；N，否 */

		String ps_description = "", ps_gaiy_info = "", error_decription = "";
		String TZ_APPLY_PCH = "";
		String TZ_ZLPS_SCOR_MD_ID = "",TREE_NAME = "";
		String TZ_PYJS_RQ = "",TZ_PYJS_SJ = "";
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

			Map<String, Object> scoreModalMap = sqlQuery.queryForMap(
					"SELECT TREE_NAME,TZ_M_FBDZ_ID FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=? AND TZ_JG_ID=?",
					new Object[] { TZ_ZLPS_SCOR_MD_ID ,orgid});
			if (scoreModalMap != null) {
				/*树名称、分布对照id（总分） */
				TREE_NAME = (String) scoreModalMap.get("TREE_NAME");
				TZ_M_FBDZ_ID = (String) scoreModalMap.get("TZ_M_FBDZ_ID");
			}

			// 评委评审可见偏差、可见评议标准数据、详细说明与通知信息
			String TZ_PWKJ_PCH = "",TZ_PWKJ_BZH = "";
			Map<String, Object> evaluationDescMap = sqlQuery.queryForMap(
					"select TZ_CLPS_SM,date_format(TZ_PYJS_RQ, '%Y-%m-%d') TZ_PYJS_RQ,date_format(TZ_PYJS_SJ, '%H:%i') TZ_PYJS_SJ ,TZ_PWKJ_PCH,TZ_PWKJ_FBT from PS_TZ_CLPS_GZ_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=?",
					new Object[] { classId, batchId });
			if (evaluationDescMap != null) {
				ps_description = (String) evaluationDescMap.get("TZ_CLPS_SM");
				ps_description = ps_description==null?"":ps_description;
				
				TZ_PYJS_RQ = (String) evaluationDescMap.get("TZ_PYJS_RQ");
				TZ_PYJS_RQ = TZ_PYJS_RQ==null?"":TZ_PYJS_RQ;
				TZ_PYJS_SJ = (String) evaluationDescMap.get("TZ_PYJS_SJ");
				TZ_PYJS_SJ = TZ_PYJS_SJ==null?"":TZ_PYJS_SJ;
				
				//评委可见评议标准数据对应数据库字段为TZ_PWKJ_FBT（评委可见分布图）
				TZ_PWKJ_PCH = (String) evaluationDescMap.get("TZ_PWKJ_PCH");
				TZ_PWKJ_BZH = (String) evaluationDescMap.get("TZ_PWKJ_FBT");
			}

			// 当评审说明区域存在：由word复制粘贴过来的表格时，进行处理，添加div;
			String table_css = "<style type=\"text/css\">	#pwxxsm_id  td{  border:1px solid Grey;  }</style>";
			ps_description = new StringBuffer(table_css).append("<div id=\"pwxxsm_id\">").append(ps_description)
					.append("</div>").toString();

			// 评议考生下限
			Integer TZ_PYKS_XX = sqlQuery.queryForObject(
					"select TZ_PYKS_XX from PS_TZ_CLPS_PW_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID = ? and TZ_PWEI_OPRID= ?",
					new Object[] { classId, batchId, oprid }, "Integer");
			TZ_PYKS_XX = TZ_PYKS_XX==null?0:TZ_PYKS_XX;
			
			//评议的考生数量
			int totalEvaluationNum = sqlQuery.queryForObject(
					"SELECT COUNT(*) FROM PS_TZ_CP_PW_KS_TBL WHERE TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=?",
					new Object[] { classId, batchId, oprid }, "Integer");
			
			/*完成的数量 */
			tz_done_num = sqlQuery.queryForObject(
					"select count(TZ_APP_INS_ID) from PS_TZ_KSCLPSLS_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_PWEI_OPRID = ? and TZ_CLPS_LUNC in ( select TZ_DQPY_LUNC from PS_TZ_CLPS_GZ_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=?) and TZ_SUBMIT_YN = 'Y'",
					new Object[] { classId, batchId, oprid, classId, batchId }, "Integer");
			
			/*评委评审概要信息 */
			ps_gaiy_info = "您在" +TZ_PYJS_RQ+ " " +TZ_PYJS_SJ+ "前需要完成" +TZ_PYKS_XX+ "份考生资料评审工作，您目前已完成了" +tz_done_num+ "份。";

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


			/*第2部分 当前评委打分平均分统计信息 */
			List<Map<String,Object>> secondPwdfThList= new ArrayList<Map<String,Object>>();
			Map<String, Object> secondPwdfTh1 = new HashMap<String, Object>();
			secondPwdfTh1.put("col01", "指标名称");
			secondPwdfTh1.put("ps_cht_flg", "N");
			secondPwdfTh1.put("ps_grp_flg", "N");
			Map<String, Object> secondPwdfTh2 = new HashMap<String, Object>();
			
			//评委可见评议标准数据
			if("Y".equals(TZ_PWKJ_BZH)){
				secondPwdfTh2.put("col02", "平均分指标");
				secondPwdfTh2.put("ps_cht_flg", "N");
				secondPwdfTh2.put("ps_grp_flg", "Y");
				
				List<Map<String,Object>> subColList= new ArrayList<Map<String,Object>>();
				Map<String, Object> subColMap = new HashMap<String, Object>();
				subColMap.put("sub_col01", "标准平均分");
				subColMap.put("ps_cht_flg", "Y");
				subColMap.put("ps_grp_flg", "N");
				subColList.add(subColMap);
				subColMap = new HashMap<String, Object>();
				subColMap.put("sub_col02", "允许误差");
				subColMap.put("ps_cht_flg", "N");
				subColMap.put("ps_grp_flg", "N");
				subColList.add(subColMap);
				subColMap = new HashMap<String, Object>();
				subColMap.put("sub_col03", "实际平均分");
				subColMap.put("ps_cht_flg", "Y");
				subColMap.put("ps_grp_flg", "N");
				subColList.add(subColMap);
				subColMap = new HashMap<String, Object>();
				subColMap.put("sub_col04", "实际误差");
				subColMap.put("ps_cht_flg", "N");
				subColMap.put("ps_grp_flg", "N");
				subColList.add(subColMap);
				subColMap = new HashMap<String, Object>();
				subColMap.put("sub_col05", "是否符合要求");
				subColMap.put("ps_cht_flg", "N");
				subColMap.put("ps_grp_flg", "N");
				subColList.add(subColMap);
				secondPwdfTh2.put("ps_sub_col", subColList);
			}else{
				secondPwdfTh2.put("col02", "评分平均分");
				secondPwdfTh2.put("ps_cht_flg", "Y");
				secondPwdfTh2.put("ps_grp_flg", "N");
			}
			
			
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
			double pjf = 0;

			// 总分成绩项
			String TZ_SCORE_ITEM_ID = sqlQuery.queryForObject(
					"SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? and PARENT_NODE_NUM=0",
					new Object[] { TREE_NAME }, "String");
			
			pjf = materialEvaluationCls.calculateAverage(classId, batchId, oprid, TZ_SCORE_ITEM_ID, error_code, error_decription);
			 
			List<Map<String,Object>> sjfzRowList= new ArrayList<Map<String,Object>>();
			Map<String, Object> sjfzRow = new HashMap<String, Object>();
			sjfzRow.put("col01", "总分");
			
			//评委可见评议标准数据
			if("Y".equals(TZ_PWKJ_BZH)){
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
				String avgMeetRequirement = actualDeviation>avgAllowDeviation?"不符合":"符合";

				sjfzRow.put("col02_sub_col01", standardAvg);
				sjfzRow.put("col02_sub_col02", avgAllowDeviation);
				sjfzRow.put("col02_sub_col03", pjf);
				sjfzRow.put("col02_sub_col04", actualDeviation);
				sjfzRow.put("col02_sub_col05", avgMeetRequirement);
				
			}else{
				sjfzRow.put("col02", pjf);
			}
			
			sjfzRowList.add(sjfzRow);
			
			
			Map<String,Object> second_total_map = new HashMap<String, Object>();			
			second_total_map.put("ps_tjzb_btmc", secondPwdfThList);
			second_total_map.put("ps_tjzb_mxsj", sjfzRowList);
			
			/*第3部分 当前评委打分分布统计信息 */
			List<Map<String,Object>> evaluationDataList = materialEvaluationCls.getScoreItemEvaluationData(classId, batchId, oprid, TZ_SCORE_ITEM_ID, error_code, error_decription);

			List<Map<String,Object>> fbsjrow = new ArrayList<Map<String,Object>>();
			
			if(evaluationDataList!=null){
				for(int i=0;i<evaluationDataList.size();i++){
					
					String TZ_M_FBDZ_MX_ID = (String)evaluationDataList.get(i).get("mx_id");
					String FBMS = sqlQuery.queryForObject(
							"SELECT TZ_M_FBDZ_MX_SM FROM PS_TZ_FBDZ_MX_TBL WHERE TZ_M_FBDZ_ID=? AND TZ_M_FBDZ_MX_ID=?",
							new Object[] { TZ_M_FBDZ_ID ,TZ_M_FBDZ_MX_ID}, "String");
					
					Map<String,Object> fbsjrowItem = new HashMap<String,Object>();
					fbsjrowItem.put("ps_fb_mc", FBMS);
					fbsjrowItem.put("ps_sjfb_bilv", (Double)evaluationDataList.get(i).get("num_rate")+"%");
					fbsjrowItem.put("ps_sjfb_rshu", (Integer)evaluationDataList.get(i).get("num_dange"));
					
					//如果显示评议标准数据
					if("Y".equals(TZ_PWKJ_BZH)){
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
						String meetRequirement = "符合";
						if(deviation>Math.abs(TZ_YXWC_NUM)){
							meetRequirement = "不符合";
						}
						
						fbsjrowItem.put("ps_bzfb_bilv", TZ_BZFB_BL+"%");
						fbsjrowItem.put("ps_bzfb_rshu", TZ_BZFB_NUM);
						fbsjrowItem.put("ps_bzfb_wcrs", TZ_YXWC_NUM);
						fbsjrowItem.put("ps_sjfb_wcrs", deviation);
						fbsjrowItem.put("ps_sjfb_fhyq", meetRequirement);
					}
					
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

			//如果显示评议标准数据，则图表区域显示评议标准曲线图
			if("Y".equals(TZ_PWKJ_BZH)){
				ps_cht_flds.add("ps_bzfb_bilv");
			}
			ps_cht_flds.add("ps_sjfb_bilv");
			
			thdFbMap.put("ps_cht_flds", ps_cht_flds);
			thdFbMap.put("ps_fszb_fbsj", fbsjrow);
			thdFbList.add(thdFbMap);
			
			/*第4部分 当前评委已评审考生统计信息 */
			Map<String,Object> dyColThMap= new HashMap<String,Object>();			
			String TZ_XS_MC2 = "";
			int dyColNum2 = 0;

			String sql1 = "select TZ_SCORE_ITEM_ID,TZ_XS_MC from PS_TZ_CJ_BPH_TBL where TZ_SCORE_MODAL_ID=? AND TZ_JG_ID = ? AND TZ_ITEM_S_TYPE = 'A' order by TZ_PX";
			List<Map<String, Object>> scoreModalList = sqlQuery.queryForList(sql1, new Object[] { TZ_ZLPS_SCOR_MD_ID,orgid });

			if (scoreModalList != null) {
				for (int i = 0; i < scoreModalList.size(); i++) {
					TZ_XS_MC2 = (String) scoreModalList.get(i).get("TZ_XS_MC");

					dyColNum2 = dyColNum2 + 1;
					String strDyColNum2 = "0" + dyColNum2;
					
					dyColThMap.put("col"+strDyColNum2.substring(strDyColNum2.length() - 2), TZ_XS_MC2);
				}
			}

			Long TZ_APP_INS_ID;
			String forthSql = "select TZ_APP_INS_ID ,TZ_SCORE_INS_ID from PS_TZ_CP_PW_KS_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_PWEI_OPRID=?";
			
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

						// 考生姓名、面试申请号,本科院校，工作单位;
						String first_name = "",msh_id = "",ksh_school = "",ksh_company = "";
						Map<String, Object> map1 = sqlQuery.queryForMap(
								"select B.TZ_MSH_ID,B.TZ_REALNAME,TZ_SCH_CNAME,TZ_COMPANY_NAME from PS_TZ_FORM_WRK_T A,PS_TZ_AQ_YHXX_TBL B,PS_TZ_REG_USER_T C where A.OPRID = B.OPRID and A.OPRID=C.OPRID and A.TZ_APP_INS_ID=? AND B.TZ_RYLX='ZCYH' LIMIT 0,1",
								new Object[] { TZ_APP_INS_ID });
						if (map1 != null) {
							first_name = (String) map1.get("TZ_REALNAME");
							msh_id = (String) map1.get("TZ_MSH_ID");
							ksh_school = (String) map1.get("TZ_SCH_CNAME");
							ksh_company = (String) map1.get("TZ_COMPANY_NAME");
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
						// 偏差
						String pc = sqlQuery.queryForObject(
								"select TZ_CLPS_PWJ_PC from PS_TZ_CLPS_KSH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=?",
								new Object[] { classId, batchId, TZ_APP_INS_ID}, "String");
						//其他评委是否已经复评
						String re_evaluation = sqlQuery.queryForObject(
								"SELECT 'Y' FROM PS_TZ_KSCLPSLS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? AND TZ_PWEI_OPRID<>? AND TZ_CLPS_LUNC=? AND TZ_SUBMIT_YN='Y' AND TZ_IS_PW_FP='Y' limit 0,1",
								new Object[] { classId, batchId, TZ_APP_INS_ID,oprid,TZ_DQPY_LUNC}, "String");
						
						if("Y".equals(re_evaluation)){
							re_evaluation = "是";
						}else{
							re_evaluation = "否";
						}
						         
						// 评审时间;
						String pssj = "";
						Map<String, Object> map2 = sqlQuery.queryForMap(
								"select A.TZ_KSH_PSPM,date_format(A.ROW_LASTMANT_DTTM, '%Y-%m-%d %H:%i') ROW_LASTMANT_DTTM ,(SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_SUBMIT_YN' AND TZ_ZHZ_ID=A.TZ_SUBMIT_YN AND TZ_EFF_STATUS='A') AS TZ_SUBMIT_YN from PS_TZ_KSCLPSLS_TBL A,PS_TZ_CLPS_GZ_TBL B,PS_TZ_CP_PW_KS_TBL C WHERE A.TZ_CLASS_ID=B.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID AND A.TZ_CLASS_ID=C.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=C.TZ_APPLY_PC_ID AND A.TZ_PWEI_OPRID=C.TZ_PWEI_OPRID AND A.TZ_APP_INS_ID=C.TZ_APP_INS_ID AND A.TZ_CLASS_ID=? and A.TZ_APPLY_PC_ID=? AND A.TZ_APP_INS_ID=? AND A.TZ_PWEI_OPRID=? AND A.TZ_CLPS_LUNC = B.TZ_DQPY_LUNC",
								new Object[] { classId, batchId, TZ_APP_INS_ID, oprid });
						if (map2 != null) {
							TZ_KSH_PSPM2 = (String) map2.get("TZ_KSH_PSPM");
							pssj = (String) map2.get("ROW_LASTMANT_DTTM");
							pyZt = (String) map2.get("TZ_SUBMIT_YN");
						}

						/*动态列的值 */
						Map<String, Object> dyRowValueItem = new HashMap<String, Object>();
						
						String TZ_SCORE_ITEM_ID3 = "";
						int dyColNum = 0;

						/*考生成绩单ID */
						long cjdId = 0;
						BigInteger biCjdId = (BigInteger) applicantsList.get(i).get("TZ_SCORE_INS_ID");
						
						if(biCjdId!=null){
							cjdId = biCjdId.longValue();
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
								double TZ_SCORE_NUM = 0;
								String TZ_SCORE_PY_VALUE = "";

								Map<String, Object> map3 = sqlQuery.queryForMap(
										"select TZ_SCORE_NUM,TZ_SCORE_PY_VALUE from PS_TZ_CJX_TBL where TZ_SCORE_INS_ID=? and TZ_SCORE_ITEM_ID=?",
										new Object[] { cjdId, TZ_SCORE_ITEM_ID3});
								if (map3 != null) {
									Object OBJ_TZ_SCORE_NUM = map3.get("TZ_SCORE_NUM");
									TZ_SCORE_NUM = OBJ_TZ_SCORE_NUM!=null?((BigDecimal)OBJ_TZ_SCORE_NUM).doubleValue():0;
									TZ_SCORE_PY_VALUE = (String) map3.get("TZ_SCORE_PY_VALUE");
								}

								String strDyColNum = "0" + dyColNum;
								strDyColNum = strDyColNum.substring(strDyColNum.length() - 2);
								
								/*动态列
								 * 成绩项类型：A-数字成绩汇总项,B-数字成绩录入项,C-评语,D-下拉框
								 * From KitchenSink.view.scoreModelManagement.scoreModelTreeNodeWin
								 */
								if ("A".equals(TZ_SCORE_ITEM_TYPE) || "B".equals(TZ_SCORE_ITEM_TYPE)) {
							        dyRowValueItem.put("col"+strDyColNum,String.valueOf(TZ_SCORE_NUM));
								} else {
									if ("D".equals(TZ_SCORE_ITEM_TYPE)) {
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
				        
						/*添加考生类别				         
				        String TZ_KSH_TYPE = sqlQuery.queryForObject(
								"select TZ_COLOR_NAME FROM PS_TZ_ORG_COLOR_V WHERE TZ_COLOR_SORT_ID=(select TZ_COLOR_SORT_ID from PS_TZ_FORM_WRK_T where TZ_APP_INS_ID=? limit 0,1) AND TZ_JG_ID=?",
								new Object[] { TZ_APP_INS_ID, orgid}, "String");
				        */
						
				        dyRowValueItem.put("ps_ksh_xh",String.valueOf(xh));
				        dyRowValueItem.put("ps_ksh_id",ksbh);
				        dyRowValueItem.put("ps_msh_id",msh_id);
				        dyRowValueItem.put("ps_ksh_bmbid",String.valueOf(TZ_APP_INS_ID));
				        dyRowValueItem.put("ps_ksh_xm",first_name);
				        dyRowValueItem.put("ps_ksh_ppm",TZ_KSH_PSPM);
				        dyRowValueItem.put("ps_ksh_cpm",TZ_KSH_PSPM2);
				        dyRowValueItem.put("ps_ksh_zt",pyZt);
				        dyRowValueItem.put("ps_ksh_dt",pssj);
				        dyRowValueItem.put("ps_ksh_school",ksh_school);
				        dyRowValueItem.put("ps_ksh_company",ksh_company);
				        //评委间偏差
				        if("Y".equals(TZ_PWKJ_PCH)){
				        	dyRowValueItem.put("ps_ksh_pc",pc==null?0:Double.parseDouble(pc));
				        }
				        //其他评委已复评
				        dyRowValueItem.put("ps_re_evaluation",re_evaluation);
				        //dyRowValueItem.put("ps_ksh_type",TZ_KSH_TYPE);
				        
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
			allDataMap.put("ps_data_fb",thdFbList);
			allDataMap.put("ps_show_standard","Y".equals(TZ_PWKJ_BZH));
			allDataMap.put("ps_show_deviation","Y".equals(TZ_PWKJ_PCH));
			
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
	
	private String printEvaluationList(String strParams) {
		String classId = request.getParameter("TZ_CLASS_ID");
		String batchId = request.getParameter("TZ_PC_ID");
		String oprId = tzLoginServiceImpl.getLoginedManagerOprid(request);
		
		String url = "";
		
		Map<String, Object> mapData = new HashMap<String, Object>();
		// 检查参数的合法性
		if (StringUtils.isBlank(classId) || StringUtils.isBlank(batchId)
				|| StringUtils.isBlank(oprId)) {

			mapData.put("errorCode", "1");
			mapData.put("errorMsg", "参数不全");
		} else {

			try {
				url = xmlToWord.createWord(classId, batchId, oprId);
			} catch (TzSystemException e) {
				e.printStackTrace();
			}

			if ("1".equals(url)) {
				mapData.put("errorCode", "1");
				mapData.put("errorMsg", "参数不全");
			} else {
				mapData.put("url", url);
			}
		}
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(mapData);
	}
	
	private String getNextApplicant(String strParams) {
		String classId =request.getParameter("BaokaoClassID"); /*字符串，请求班级编号*/
		String batchId = request.getParameter("BaokaoPCID"); /*请求报考批次编号*/
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		
		String strRtn = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String, Object> mapRet = new HashMap<String,Object>();
		
		String bmbIdNext = "";
		String error_code = "0";
		String error_decription = "";
		
		try {
			String sql;
			
			//是否存在没有评委组的评委
			sql = "SELECT 'Y' FROM PS_TZ_CLPS_PW_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_ZHZT='A' AND TZ_PWZBH=' '";
			String noPwzFlag = sqlQuery.queryForObject(sql, new Object[]{classId,batchId},"String");

			if("Y".equals(noPwzFlag)) {
				bmbIdNext = "";
				error_code = "1";
				error_decription = "存在没有被分组的评委";
			} else {
				sql = "SELECT A.TZ_PYKS_XX,A.TZ_PWEI_ZHZT,A.TZ_PWZBH,B.TZ_DQPY_ZT,B.TZ_DQPY_LUNC,B.TZ_MSPY_NUM,C.TZ_SUBMIT_YN";
				sql = sql + ",(SELECT COUNT(1) FROM PS_TZ_CP_PW_KS_TBL E,PS_TZ_CLPS_KSH_TBL F";
				sql = sql + " WHERE E.TZ_CLASS_ID=F.TZ_CLASS_ID AND E.TZ_APPLY_PC_ID=F.TZ_APPLY_PC_ID AND E.TZ_APP_INS_ID=F.TZ_APP_INS_ID AND E.TZ_CLASS_ID=A.TZ_CLASS_ID AND E.TZ_APPLY_PC_ID=A.TZ_APPLY_PC_ID AND E.TZ_PWEI_OPRID=A.TZ_PWEI_OPRID) TZ_PWKS_NUM";
				sql = sql + ",(SELECT COUNT(distinct G.TZ_APP_INS_ID ) FROM PS_TZ_CP_PW_KS_TBL E,PS_TZ_KSCLPSLS_TBL G";
				sql = sql + " WHERE E.TZ_CLASS_ID=G.TZ_CLASS_ID AND E.TZ_APPLY_PC_ID=G.TZ_APPLY_PC_ID AND E.TZ_APP_INS_ID=G.TZ_APP_INS_ID AND E.TZ_CLASS_ID=A.TZ_CLASS_ID AND E.TZ_APPLY_PC_ID=A.TZ_APPLY_PC_ID AND G.TZ_PWEI_OPRID=A.TZ_PWEI_OPRID AND G.TZ_CLPS_LUNC=B.TZ_DQPY_LUNC AND G.TZ_SUBMIT_YN='Y') TZ_PWKS_SUB_NUM";
				sql = sql + " FROM PS_TZ_CLPS_PW_TBL A INNER JOIN PS_TZ_CLPS_GZ_TBL B ON(A.TZ_CLASS_ID=B.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID)";
				sql = sql + " LEFT JOIN PS_TZ_CLPWPSLS_TBL C ON(C.TZ_CLASS_ID=A.TZ_CLASS_ID AND C.TZ_APPLY_PC_ID=A.TZ_APPLY_PC_ID AND C.TZ_PWEI_OPRID=A.TZ_PWEI_OPRID AND C.TZ_CLPS_LUNC=B.TZ_DQPY_LUNC)";
				sql = sql + " WHERE A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_PWEI_OPRID=?";

				Map<String, Object> mapData = sqlQuery.queryForMap(sql,new Object[]{classId,batchId,oprid});

				if(mapData!=null){
					Integer pyksNum = mapData.get("TZ_PYKS_XX") == null ? 0 : Integer.valueOf(mapData.get("TZ_PYKS_XX").toString());
					String pweiZhzt = mapData.get("TZ_PWEI_ZHZT") == null ? "" : mapData.get("TZ_PWEI_ZHZT").toString();
					String pwzbh = mapData.get("TZ_PWZBH") == null ? "" : mapData.get("TZ_PWZBH").toString();
					String dqpyZt = mapData.get("TZ_DQPY_ZT") == null ? "" : mapData.get("TZ_DQPY_ZT").toString();
					Short dqpyLunc = mapData.get("TZ_DQPY_LUNC") == null ? 1 : Short.valueOf(mapData.get("TZ_DQPY_LUNC").toString());
					Integer mspyNum = mapData.get("TZ_MSPY_NUM") == null ? 0 : Integer.valueOf(mapData.get("TZ_MSPY_NUM").toString());
					String submitAllFlag = mapData.get("TZ_SUBMIT_YN") == null ? "" : mapData.get("TZ_SUBMIT_YN").toString();
					Integer pwksNum = mapData.get("TZ_PWKS_NUM") == null ? 0 : Integer.valueOf(mapData.get("TZ_PWKS_NUM").toString());
					Integer pwksSubNum= mapData.get("TZ_PWKS_SUB_NUM") == null ? 0 : Integer.valueOf(mapData.get("TZ_PWKS_SUB_NUM").toString());
					
					if("Y".equals(submitAllFlag)) {
						bmbIdNext = "";
						error_code = "1";
						error_decription = "该轮次已经全部提交了,无法得到下一个考生";
					} else {
						if("A".equals(dqpyZt)) {
							if("A".equals(pweiZhzt)) {
								if(pwksNum>pwksSubNum) {
									bmbIdNext = "";
									error_code = "1";
									error_decription = "评委有“打分后未提交”的考生";
								} else {
									if(pwksNum>=pyksNum) {
										bmbIdNext = "";
										error_code = "1";
										error_decription = "该评委已经达到了评审的上限";
									} else {
										
										sql = tzSQLObject.getSQLText("SQL.TZEvaluationSystemBundle.TzMaterialGetNext");	
										Map<String, Object> mapNext = sqlQuery.queryForMap(sql,new Object[]{mspyNum,pwzbh,classId,batchId});
										
										if(mapNext!=null){
											Long tzAppInsId = Long.valueOf(mapNext.get("TZ_APP_INS_ID") == null ? "" : mapNext.get("TZ_APP_INS_ID").toString());
											
											if(tzAppInsId>0) {
												//锁表PS_TZ_CLPS_KSH_TBL
												mySqlLockService.lockRow(sqlQuery, "TZ_CLPS_KSH_TBL");
												
												//再次查看有没有超过上限
												sql = "SELECT COUNT(1) FROM PS_TZ_CP_PW_KS_TBL A,PS_TZ_CLPS_KSH_TBL B WHERE A.TZ_CLASS_ID=B.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID AND A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_APP_INS_ID=?";
												Integer kspwNum = sqlQuery.queryForObject(sql, new Object[]{classId,batchId,tzAppInsId},"Integer");
												
												//再次查看有没有被自己抽取过
												sql = "SELECT 'Y' FROM PS_TZ_CP_PW_KS_TBL A,PS_TZ_CLPS_KSH_TBL B WHERE A.TZ_CLASS_ID=B.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID AND A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_APP_INS_ID=? AND A.TZ_PWEI_OPRID=?";
												String myKsFlag = sqlQuery.queryForObject(sql, new Object[]{classId,batchId,tzAppInsId,oprid}, "String");
												
												//再次查看有没有被同组人抽取过
												sql = "SELECT 'Y' FROM PS_TZ_CP_PW_KS_TBL A,PS_TZ_CLPS_KSH_TBL B,PS_TZ_CLPS_PW_TBL C WHERE A.TZ_CLASS_ID=B.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID AND A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND A.TZ_CLASS_ID=C.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=C.TZ_APPLY_PC_ID AND A.TZ_APP_INS_ID=C.TZ_PWEI_OPRID";
												sql = sql + " AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_APP_INS_ID=? AND C.TZ_PWZBH=?";
												String tzpwFlag = sqlQuery.queryForObject(sql, new Object[]{classId,batchId,tzAppInsId,pwzbh}, "String");
												
												if(kspwNum>=mspyNum || "Y".equals(myKsFlag) || "Y".equals(tzpwFlag)) {
													
												} else {
													//材料评审评委考生关系表
													int insertRow;
													
													PsTzCpPwKsTbl psTzCpPwKsTbl = new PsTzCpPwKsTbl();
													psTzCpPwKsTbl.setTzClassId(classId);
													psTzCpPwKsTbl.setTzApplyPcId(batchId);
													psTzCpPwKsTbl.setTzAppInsId(tzAppInsId);
													psTzCpPwKsTbl.setTzPweiOprid(oprid);
													psTzCpPwKsTbl.setTzGuanxLeix("A");
													psTzCpPwKsTbl.setRowAddedDttm(new Date());
													psTzCpPwKsTbl.setRowAddedOprid(oprid);
													psTzCpPwKsTbl.setRowLastmantDttm(new Date());
													psTzCpPwKsTbl.setRowLastmantOprid(oprid);
													insertRow = psTzCpPwKsTblMapper.insert(psTzCpPwKsTbl);

													if(insertRow == 1) {
														//材料评审考生评审得分历史
														PsTzKsclpslsTblKey psTzKsclpslsTblKey = new PsTzKsclpslsTblKey();
														psTzKsclpslsTblKey.setTzClassId(classId);
														psTzKsclpslsTblKey.setTzApplyPcId(batchId);
														psTzKsclpslsTblKey.setTzAppInsId(tzAppInsId);
														psTzKsclpslsTblKey.setTzPweiOprid(oprid);
														psTzKsclpslsTblKey.setTzClpsLunc(dqpyLunc);
														
														PsTzKsclpslsTbl psTzKsclpslsTbl = psTzKsclpslsTblMapper.selectByPrimaryKey(psTzKsclpslsTblKey);
														
														if(psTzKsclpslsTbl==null) {
															psTzKsclpslsTbl = new PsTzKsclpslsTbl();
															psTzKsclpslsTbl.setTzClassId(classId);
															psTzKsclpslsTbl.setTzApplyPcId(batchId);
															psTzKsclpslsTbl.setTzAppInsId(tzAppInsId);
															psTzKsclpslsTbl.setTzPweiOprid(oprid);
															psTzKsclpslsTbl.setTzClpsLunc(dqpyLunc);
															psTzKsclpslsTbl.setTzGuanxLeix("A");
															psTzKsclpslsTbl.setTzSubmitYn("U");
															psTzKsclpslsTbl.setRowAddedDttm(new Date());
															psTzKsclpslsTbl.setRowAddedOprid(oprid);
															psTzKsclpslsTbl.setRowLastmantDttm(new Date());
															psTzKsclpslsTbl.setRowLastmantOprid(oprid);
															psTzKsclpslsTblMapper.insert(psTzKsclpslsTbl);
														} else {
															psTzKsclpslsTbl.setTzKshPspm("");
															psTzKsclpslsTbl.setTzSubmitYn("U");
															psTzKsclpslsTbl.setRowLastmantDttm(new Date());
															psTzKsclpslsTbl.setRowLastmantOprid(oprid);
															psTzKsclpslsTblMapper.updateByPrimaryKey(psTzKsclpslsTbl);
														}
														
														//材料评审考生评委信息
														
														sql = "SELECT TZ_CLASS_ID,TZ_APPLY_PC_ID,TZ_APP_INS_ID,group_concat(TZ_PWEI_OPRID SEPARATOR',') AS TZ_PW_LIST FROM PS_TZ_CP_PW_KS_TBL";
														sql = sql + " WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? GROUP BY TZ_CLASS_ID,TZ_APPLY_PC_ID,TZ_APP_INS_ID";

														Map<String, Object> mapKsPwList = sqlQuery.queryForMap(sql,new Object[]{classId,batchId,tzAppInsId});
														String ksPwList = mapKsPwList.get("TZ_PW_LIST") == null ? "" : mapKsPwList.get("TZ_PW_LIST").toString();
																
														
														PsTzClpskspwTblKey psTzClpskspwTblKey = new PsTzClpskspwTblKey();
														psTzClpskspwTblKey.setTzClassId(classId);
														psTzClpskspwTblKey.setTzApplyPcId(batchId);
														psTzClpskspwTblKey.setTzAppInsId(tzAppInsId);
														psTzClpskspwTblKey.setTzClpsLunc(dqpyLunc);
														
														PsTzClpskspwTbl psTzClpskspwTbl = psTzClpskspwTblMapper.selectByPrimaryKey(psTzClpskspwTblKey);
														
														if(psTzClpskspwTbl==null) {
															psTzClpskspwTbl = new PsTzClpskspwTbl();
															psTzClpskspwTbl.setTzClassId(classId);
															psTzClpskspwTbl.setTzApplyPcId(batchId);
															psTzClpskspwTbl.setTzAppInsId(tzAppInsId);
															psTzClpskspwTbl.setTzClpsLunc(dqpyLunc);
															psTzClpskspwTbl.setTzClpwList(ksPwList);
															psTzClpskspwTbl.setRowAddedDttm(new Date());
															psTzClpskspwTbl.setRowAddedOprid(oprid);
															psTzClpskspwTbl.setRowLastmantDttm(new Date());
															psTzClpskspwTbl.setRowLastmantOprid(oprid);
															psTzClpskspwTblMapper.insert(psTzClpskspwTbl);
														} else {
															psTzClpskspwTbl.setTzClpwList(ksPwList);
															psTzClpskspwTbl.setRowLastmantDttm(new Date());
															psTzClpskspwTbl.setRowLastmantOprid(oprid);
															psTzClpskspwTblMapper.updateByPrimaryKey(psTzClpskspwTbl);
														}
														
														bmbIdNext = String.valueOf(tzAppInsId);
														error_code = "0";
														error_decription = "";
								
													} else {
														bmbIdNext = "";
														error_code = "1";
														error_decription = "得到下一个考生失败或者已经没有可评审的考生";
													}	
												}
												
												//解锁
												mySqlLockService.unlockRow(sqlQuery);
											} else {
												bmbIdNext = "";
												error_code = "1";
												error_decription = "同步MBA材料评审评委考生关系表失败";	
											}
										}else{
											bmbIdNext = "";
											error_code = "1";
											error_decription = "没有可获取的考生";	
										}
										
									}
								}
							} else {
								bmbIdNext = "";
								error_code = "1";
								error_decription = "评委账号没有被启用";
							}
						} else {
							bmbIdNext = "";
							error_code = "1";
							error_decription = "当前批次的评审状态不在进行中";
						}
					}
				}else{
					bmbIdNext = "";
					error_code = "1";
					error_decription = "没有开启评审";
				}
				

			}
			
			// 考生姓名;
			String first_name = "";
			if(bmbIdNext!=null&&!"".equals(bmbIdNext)){
				first_name = sqlQuery.queryForObject("select B.TZ_REALNAME from PS_TZ_FORM_WRK_T A,PS_TZ_REG_USER_T B where A.OPRID = B.OPRID and A.TZ_APP_INS_ID=?",
						new Object[] { bmbIdNext },"String");
			}
			
			
			mapRet.put("ps_class_id", classId);
			mapRet.put("ps_bkpc_id", batchId);
			mapRet.put("ps_ksh_bmbid", bmbIdNext);
			mapRet.put("ps_ksh_xm", first_name);
			mapRet.put("error_code", error_code);
			mapRet.put("error_decription", error_decription);
			
		} catch(Exception e) {
			e.printStackTrace();
			mapRet.put("error_code", "1");
			mapRet.put("error_decription", e.toString());
		}
		
		strRtn = jacksonUtil.Map2json(mapRet);
		return strRtn;
	}
	
	private String submitAllData(String strParams) {

		String batchId = request.getParameter("BaokaoPCID"); /*请求报考批次编号*/
		String classId =request.getParameter("BaokaoClassID"); /*字符串，请求班级编号*/
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		
		String error_code = "", error_decription = "";

		short TZ_DQPY_LUNC = 1; /*当前评审轮次*/
		
	    //班级名称
		String className = "";		
		
		if(!StringUtils.isBlank(classId)&&!StringUtils.isBlank(batchId)){  
			//当前报考轮次
			String STR_DQPY_LUNC = sqlQuery.queryForObject("select TZ_DQPY_LUNC from PS_TZ_CLPS_GZ_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=?", 
					new Object[]{classId,batchId}, "String");
			if(STR_DQPY_LUNC!=null&&!"".equals(STR_DQPY_LUNC)){
				TZ_DQPY_LUNC = Short.parseShort(STR_DQPY_LUNC);
			}
		}

		//判断当前评委是否是暂停状态
		 String TZ_PWEI_ZHZT = sqlQuery.queryForObject("select TZ_PWEI_ZHZT from PS_TZ_CLPS_PW_TBL WHERE TZ_CLASS_ID = ? and TZ_APPLY_PC_ID= ? AND TZ_PWEI_OPRID=?", 
				new Object[]{classId,batchId,oprid}, "String");
		 if(TZ_PWEI_ZHZT==null||"B".equals(TZ_PWEI_ZHZT)){
			error_code = "PAUSE";
			error_decription = "当前评委账号为暂停状态，不能提交数据。";
		 }else{
			//检查是否有排名重复考生
				String have_equal = sqlQuery.queryForObject("select 'Y' from PS_TZ_CP_PW_KS_TBL a, PS_TZ_CP_PW_KS_TBL b where a.TZ_CLASS_ID=b.TZ_CLASS_ID AND a.TZ_APPLY_PC_ID = b.TZ_APPLY_PC_ID and a.TZ_PWEI_OPRID=b.TZ_PWEI_OPRID and a.TZ_APP_INS_ID<>b.TZ_APP_INS_ID and a.TZ_KSH_PSPM=b.TZ_KSH_PSPM and a.TZ_PWEI_OPRID=? and a.TZ_CLASS_ID=? AND a.TZ_APPLY_PC_ID=?",
						 new Object[]{oprid,classId,batchId}, "String");
				if("Y".equals(have_equal)){
					error_code = "SORTREPEAT";
					error_decription = "发现排名重复考生，不能提交数据。";
				}else{
					//判断当前评委是否已经提交;
					 String TZ_SUBMIT_YN = sqlQuery.queryForObject("select TZ_SUBMIT_YN from PS_TZ_CLPWPSLS_TBL WHERE TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=? and TZ_CLPS_LUNC=?",
							 new Object[]{classId,batchId,oprid,TZ_DQPY_LUNC}, "String");
					 
					 if("Y".equals(TZ_SUBMIT_YN)){
						 error_code = "SUBMITTED";
						 error_decription = "当前评委账号已经提交，不能再提交";
					 }else{
						   //允许评议数量
						   int TZ_PYKS_XX = 0;
						   
						   String STR_PYKS_XX = sqlQuery.queryForObject("select TZ_PYKS_XX from PS_TZ_CLPS_PW_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=? and TZ_PWEI_OPRID=? and TZ_PWEI_ZHZT = 'A'  ", 
									new Object[]{classId,batchId,oprid},"String");
						   if(STR_PYKS_XX!=null){
							   TZ_PYKS_XX = Integer.parseInt(STR_PYKS_XX);
							}
						   
						   int submitCount = sqlQuery.queryForObject("select count(1) from PS_TZ_KSCLPSLS_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=? and TZ_PWEI_OPRID=? and TZ_SUBMIT_YN='Y' AND TZ_CLPS_LUNC=(select TZ_DQPY_LUNC from PS_TZ_CLPS_GZ_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=?) ", 
									new Object[]{classId,batchId,oprid,classId,batchId},"Integer");
						   
						   /*所有考生的提交状态都是“已提交”*/
						   String submit_zt = sqlQuery.queryForObject("select 'Y' from PS_TZ_KSCLPSLS_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=? and TZ_PWEI_OPRID=? and TZ_CLPS_LUNC=? and TZ_SUBMIT_YN<>'Y'and TZ_SUBMIT_YN<>'C'", 
									new Object[]{classId,batchId,oprid,TZ_DQPY_LUNC},"String");
						   
						   if("Y".equals(submit_zt)){
							   error_decription = "存在未评审的考生";
							   error_code = "SUBMTALL03";
						   }else{
							   if(submitCount<TZ_PYKS_XX){
								   error_decription = "您当前评审的学生数量未达到要求";
								   error_code = "SUBMTALL02";
							   }else{
								   //校验评审是否符合要求
								    if(materialEvaluationCls.validateEvaluation(classId,batchId,oprid,error_code,error_decription)){
								    	String clpwpslsExist = sqlQuery.queryForObject("select 'Y' from PS_TZ_CLPWPSLS_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=? and TZ_PWEI_OPRID=? and TZ_CLPS_LUNC=?", 
									   			new Object[]{classId,batchId,oprid,TZ_DQPY_LUNC},"String");
									   
										psTzClpwpslsTbl psTzClpwpslsTbl = new psTzClpwpslsTbl();
										psTzClpwpslsTbl.setTzClassId(classId);
										psTzClpwpslsTbl.setTzApplyPcId(batchId);
										psTzClpwpslsTbl.setTzPweiOprid(oprid);
										psTzClpwpslsTbl.setTzClpsLunc(TZ_DQPY_LUNC);
										psTzClpwpslsTbl.setTzSubmitYn("Y");
										psTzClpwpslsTbl.setRowLastmantOprid(oprid);
										psTzClpwpslsTbl.setRowLastmantDttm(new Date());
									    
										if("Y".equals(clpwpslsExist)){
											psTzClpspwlsTblMapper.updateByPrimaryKeySelective(psTzClpwpslsTbl);
										}else{
											psTzClpwpslsTbl.setRowAddedOprid(oprid);
											psTzClpwpslsTbl.setRowAddedDttm(new Date());
											psTzClpspwlsTblMapper.insertSelective(psTzClpwpslsTbl);
										}
										
										error_decription = "";;
										error_code = "0";
								    }else{
								    	error_decription = "您当前评审不符合要求。";
										error_code = "FAILED";
								    }
								   	
							   }
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
