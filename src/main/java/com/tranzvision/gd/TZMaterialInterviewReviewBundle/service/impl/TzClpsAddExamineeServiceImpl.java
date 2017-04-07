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
	

	/*所有考生，默认显示进入的班级批次下的考试*/
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
					sql = "SELECT TZ_MSPY_NUM FROM PS_TZ_CLPS_GZ_TBL  WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
					Integer mspsNum = sqlQuery.queryForObject(sql, new Object[] {classId,batchId},"Integer");
					if(mspsNum==null) {
						mspsNum=0;
					}
					

					sql = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialKsPwInfo");
					List<Map<String, Object>> listPw = sqlQuery.queryForList(sql, new Object[] {classId,batchId,appinsId});
					
					for(Map<String, Object> mapPw : listPw) {
						
						pwNum++;
						
						//String pwOprid = mapPw.get("TZ_PWEI_OPRID")  == null ? "" : mapPw.get("TZ_PWEI_OPRID").toString();
						String pwDlzhId = mapPw.get("TZ_DLZH_ID") == null ? "" : mapPw.get("TZ_DLZH_ID").toString();
						
						if(!"".equals(pwList)) {
							pwList += "," + pwDlzhId;
						} else {
							pwList = pwDlzhId;
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
