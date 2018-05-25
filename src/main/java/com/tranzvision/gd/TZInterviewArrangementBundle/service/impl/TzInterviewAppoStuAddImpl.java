package com.tranzvision.gd.TZInterviewArrangementBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZInterviewArrangementBundle.dao.PsTzMsyyKsTblMapper;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMsyyKsTbl;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMsyyKsTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZInterviewArrangementBundle.service.impl.TzInterviewAppoStuAddImpl")
public class TzInterviewAppoStuAddImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery jdbcTemplate;
	/*
	@Autowired
	private HttpServletRequest request;
	*/
	@Autowired
	private FliterForm fliterForm;
	
	@Autowired
	private PsTzMsyyKsTblMapper psTzMsyyKsTblMapper;
	
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", new ArrayList<Map<String, Object>>());

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};

			// json数据要的结果字段;
			String[] resultFldArray = {"TZ_CLASS_ID", "TZ_APPLY_PC_ID", "TZ_APP_INS_ID", "OPRID", "TZ_REALNAME", "TZ_EMAIL","TZ_MOBILE","TZ_MSH_ID"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				int num = list.size();
				for (int i = 0; i < num; i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("classID", rowList[0]);
					mapList.put("batchID", rowList[1]);
					mapList.put("appId", rowList[2]);
					mapList.put("oprid", rowList[3]);
					mapList.put("stuName", rowList[4]);
					mapList.put("email", rowList[5]);
					mapList.put("mobile", rowList[6]);
					mapList.put("msApplyID", rowList[7]);
					
					//预约状态
					String sql = "select 'Y' from PS_TZ_MSYY_KS_TBL where TZ_CLASS_ID=? and TZ_BATCH_ID=? and OPRID=? limit 1";
					String yySta = jdbcTemplate.queryForObject(sql, new Object[]{ rowList[0], rowList[1], rowList[3] }, "String");
					String yyStatus = "未预约";
					if("Y".equals(yySta)){
						yyStatus = "已预约";
					}
					mapList.put("yyStatus", yyStatus);

					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}
	
	
	@Override
	public String tzOther(String strType, String strParams, String[] errorMsg) {
		String strRet = "";
		try {
			switch (strType) {
			case "addInterviewAppoStu":
				//添加预约考生
				strRet = this.addInterviewAppoStu(strParams,errorMsg);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
		}
		return strRet;
	}
	
	/**
	 * 添加面试预约学生
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String addInterviewAppoStu(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("result", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			
			String classID = jacksonUtil.getString("classID");
			String batchID = jacksonUtil.getString("batchID");
			String timePlan = jacksonUtil.getString("timePlan");

			List<Map<String,Object>> selStuList = (List<Map<String, Object>>) jacksonUtil.getList("selStuList");
			//选择的学生数
			int stuCount = selStuList.size();
			
			String sql = "SELECT TZ_MSYY_COUNT FROM PS_TZ_MSSJ_ARR_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=? AND TZ_MS_PLAN_SEQ=?";
			int maxPerson = jdbcTemplate.queryForObject(sql, new Object[]{classID, batchID, timePlan}, "int");
			
			if(stuCount > maxPerson){
				errorMsg[0] = "1";
				errorMsg[1] = "指定面试时间段最多可预约"+maxPerson+"人，您已超过最大值。";
			}else{
				String whereIn = "";
				for(Map<String,Object> stuMap: selStuList){
					String selOprid = stuMap.get("oprid") == null? "" : String.valueOf(stuMap.get("oprid"));
					if(!"".equals(selOprid)){
						if("".equals(whereIn)){
							whereIn = "'"+selOprid+"'";
						}else{
							whereIn = whereIn+",'"+selOprid+"'";
						}
					}
				}
				
				//当前预约时间已预约人数
				sql = "SELECT COUNT(*) FROM PS_TZ_MSYY_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=? AND TZ_MS_PLAN_SEQ=?";
				int planTotalNum = jdbcTemplate.queryForObject(sql, new Object[]{classID, batchID, timePlan}, "int");
				
				//选择的选手已在指定预约时间内人数
				sql = "SELECT COUNT(*) FROM PS_TZ_MSYY_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=? AND TZ_MS_PLAN_SEQ=? AND OPRID IN("+ whereIn +")";
				int inPlanCount = jdbcTemplate.queryForObject(sql, new Object[]{classID, batchID, timePlan}, "int");
				//添加的选择加上原来的学生，去重后的学生数
				int lastNum = (planTotalNum+stuCount)-2*inPlanCount;
				if(lastNum > maxPerson){
					errorMsg[0] = "1";
					errorMsg[1] = "指定面试时间段最多可预约"+maxPerson+"人，预约人数已满。";
				}else{
					//删除其他时间段的预约学生
					sql = "DELETE FROM PS_TZ_MSYY_KS_TBL WHERE TZ_CLASS_ID='"+ classID +"' AND TZ_BATCH_ID='"+ batchID +"' AND TZ_MS_PLAN_SEQ<>'"+ timePlan +"' AND OPRID IN("+ whereIn +")";
					jdbcTemplate.execute(sql);
					
					for(Map<String,Object> stuMap: selStuList){
						String oprid = stuMap.get("oprid") == null? "" : String.valueOf(stuMap.get("oprid"));
						PsTzMsyyKsTblKey psTzMsyyKsTblKey = new PsTzMsyyKsTblKey(); 
						psTzMsyyKsTblKey.setTzClassId(classID);
						psTzMsyyKsTblKey.setTzBatchId(batchID);
						psTzMsyyKsTblKey.setTzMsPlanSeq(timePlan);
						psTzMsyyKsTblKey.setOprid(oprid);
						
						PsTzMsyyKsTbl psTzMsyyKsTbl = psTzMsyyKsTblMapper.selectByPrimaryKey(psTzMsyyKsTblKey);
						if(psTzMsyyKsTbl == null){
							psTzMsyyKsTbl = new PsTzMsyyKsTbl();
							psTzMsyyKsTbl.setTzClassId(classID);
							psTzMsyyKsTbl.setTzBatchId(batchID);
							psTzMsyyKsTbl.setTzMsPlanSeq(timePlan);
							psTzMsyyKsTbl.setOprid(oprid);
							psTzMsyyKsTblMapper.insert(psTzMsyyKsTbl);
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
			rtnMap.replace("result", "fail");
		}
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
}
