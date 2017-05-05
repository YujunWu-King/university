package com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 材料评审考生名单-新增考生
 * @author LuYan
 * 2017-3-30
 *
 */
@Service("com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl.TzClpsAddExamineeServiceImpl")
public class TzClpsAddExamineeServiceImpl extends FrameworkImpl {
	
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private TZGDObject tzSQLObject;
	@Autowired
	private SqlQuery sqlQuery;
	

	/*所有考生，默认显示进入的班级批次下的考生*/
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String strParams,int numLimit,int numStart,String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		Map<String, Object> mapRet = new HashMap<String,Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String,Object>>();
		mapRet.put("root", listData);

		try {
			
			//排序字段
			String[][] orderByArr = new String[][] {};
			
			//json数据要的结果字段
			String[] resultFldArray = {
					"TZ_CLASS_ID","TZ_CLASS_NAME","TZ_APPLY_PC_ID","TZ_BATCH_NAME","TZ_REALNAME","TZ_MSSQH","TZ_APP_INS_ID","TZ_GENDER","TZ_GENDER_DESC"};
			
			//可配置搜索通用函数
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errMsg);
			
			if(obj!=null && obj.length>0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for(int i=0;i<list.size();i++) {
					String[] rowList = list.get(i);
					
					String classId = rowList[0];
					String batchId = rowList[2];
					String appinsId = rowList[6];
					
					String sql = "";
					
					//评委列表、评审状态
					String pwList = "",reviewStatusDesc = "";
					//评委数
					Integer pwNum = 0;
					//每生评审人数
					Integer mspsNum = 0;
					//当前评审轮次
					Integer dqpyLunc = 0;
					
					sql = "SELECT 'Y' TZ_IS_EXIST,TZ_MSPY_NUM,TZ_DQPY_LUNC FROM PS_TZ_CLPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
					Map<String, Object> mapRule = sqlQuery.queryForMap(sql, new Object[] {classId,batchId});
					String strIsExist = mapRule.get("TZ_IS_EXIST") == null ? "" : mapRule.get("TZ_IS_EXIST").toString();
						
					if(!"Y".equals(strIsExist)) {
						//没有定义评审规则
						mspsNum = 2;
					} else {
						//定义了评审规则
						String strMspsNum = mapRule.get("TZ_MSPY_NUM") == null ? "" : mapRule.get("TZ_MSPY_NUM").toString();
						if(!"".equals(strMspsNum)) {
							mspsNum = Integer.valueOf(strMspsNum);
						}
						
						String strDqpyLunc = mapRule.get("TZ_DQPY_LUNC") == null ? "" : mapRule.get("TZ_DQPY_LUNC").toString();
						if(!"".equals(strDqpyLunc)) {
							dqpyLunc = Integer.valueOf(strDqpyLunc);
						}
						
						sql = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialKsPwInfo");
						List<Map<String, Object>> listPw = sqlQuery.queryForList(sql, new Object[] {classId,batchId,appinsId,dqpyLunc});
						
						for(Map<String, Object> mapPw : listPw) {

							//String pwOprid = mapPw.get("TZ_PWEI_OPRID")  == null ? "" : mapPw.get("TZ_PWEI_OPRID").toString();
							String pwDlzhId = mapPw.get("TZ_DLZH_ID") == null ? "" : mapPw.get("TZ_DLZH_ID").toString();
							String submitFlag = mapPw.get("TZ_SUBMIT_YN") == null ? "" : mapPw.get("TZ_SUBMIT_YN").toString();
							
							if("Y".equals(submitFlag)) {
								//已评审
								pwNum++;
							}
							
							if(!"".equals(pwList)) {
								pwList += "," + pwDlzhId;
							} else {
								pwList = pwDlzhId;
							}
						}
					}
					
					
					if(mspsNum.equals(pwNum)) {
						reviewStatusDesc = "已完成";
					} else {
						reviewStatusDesc = "未完成（"+pwNum+"/"+mspsNum+"）";
					}
					
					Map<String, Object> mapList = new HashMap<String,Object>();
					mapList.put("classId", classId);
					mapList.put("className", rowList[1]);
					mapList.put("batchId", batchId);
					mapList.put("batchName", rowList[3]);
					mapList.put("name", rowList[4]);
					mapList.put("mssqh", rowList[5]);
					mapList.put("appinsId", appinsId);
					mapList.put("sex", rowList[7]);
					mapList.put("sexDesc", rowList[8]);
					mapList.put("judgeList", pwList);
					mapList.put("reviewStatusDesc", reviewStatusDesc);
					
					
					//负面清单
					String fmqdVal = "";
					String fmqdSql = "select TZ_FMQD_ID,TZ_FMQD_NAME from PS_TZ_CS_KSFM_T where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?";
					List<Map<String,Object>> fmqdList = sqlQuery.queryForList(fmqdSql, new Object[]{ classId, batchId, appinsId });
					for(Map<String,Object> fmqdMap : fmqdList){
						String LabelDesc = fmqdMap.get("TZ_FMQD_NAME") == null ? "" : fmqdMap.get("TZ_FMQD_NAME").toString();
						if(!"".equals(LabelDesc)){
							if("".equals(fmqdVal)){
								fmqdVal = LabelDesc;
							}else{
								fmqdVal = fmqdVal + "|" + LabelDesc;
							}
						}
					}
					mapList.put("negativeList", fmqdVal);
					
					//自动标签
					String zdbqVal = "";
					String zdbqSql = "select TZ_ZDBQ_ID,TZ_BIAOQZ_NAME from PS_TZ_CS_KSBQ_T where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?";
					List<Map<String,Object>> zdbqList = sqlQuery.queryForList(zdbqSql, new Object[]{ classId, batchId, appinsId });
					for(Map<String,Object> zdbqMap : zdbqList){
						String LabelDesc = zdbqMap.get("TZ_BIAOQZ_NAME") == null ? "" : zdbqMap.get("TZ_BIAOQZ_NAME").toString();
						if(!"".equals(LabelDesc)){
							if("".equals(zdbqVal)){
								zdbqVal = LabelDesc ;
							}else{
								zdbqVal = zdbqVal + "|" + LabelDesc ;
							}
						}
					}
					mapList.put("autoLabel", zdbqVal);
					
					//手动标签
					String sdbqVal = "";
					String sdbqSql = "select TZ_LABEL_NAME from PS_TZ_FORM_LABEL_T A,PS_TZ_LABEL_DFN_T B where A.TZ_LABEL_ID=B.TZ_LABEL_ID and TZ_APP_INS_ID=?";
					List<Map<String,Object>> sdbqList = sqlQuery.queryForList(sdbqSql, new Object[]{ appinsId });
					for(Map<String,Object> sdbqMap: sdbqList){
						String LabelDesc = sdbqMap.get("TZ_LABEL_NAME") == null ? "" : sdbqMap.get("TZ_LABEL_NAME").toString();
						if(!"".equals(LabelDesc)){
							if("".equals(sdbqVal)){
								sdbqVal = LabelDesc;
							}else{
								sdbqVal = sdbqVal + "|" + LabelDesc;
							}
						}
					}
					mapList.put("manualLabel", sdbqVal);
					
					
					
					listData.add(mapList);
				}
				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		strRet = jacksonUtil.Map2json(mapRet);
		
		return strRet;
	}
}
