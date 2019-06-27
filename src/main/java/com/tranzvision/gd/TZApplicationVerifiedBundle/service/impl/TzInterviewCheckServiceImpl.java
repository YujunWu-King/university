package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;


/**
 * @author a
 * 查看面试情况
 */
@Service("com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl.TzInterviewCheckServiceImpl")
public class TzInterviewCheckServiceImpl extends FrameworkImpl {
	
	@Autowired
	private SqlQuery sqlQuery;
	
	// 查询框数据
	public String tzQueryList(String comParams,int numLimit,int numStart,String[] errorMsg) {
		String strRet = "";
		Map<String,Object> mapRet = new HashMap<String,Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String,Object>>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(comParams);
			String classID = jacksonUtil.getString("classID");
			String batchID = jacksonUtil.getString("batchID");
			String selOne = "";
			if(classID!=null&&batchID!=null){
				String sql = "Select B.TZ_GROUP_NAME" +
						" From PS_TZ_MSPS_KSH_TBL A,PS_TZ_INTEGROUP_T B"+
						" Where A.TZ_GROUP_ID=B.TZ_GROUP_ID"+
						" AND B.TZ_CLASS_ID=?"+
						" AND B.TZ_APPLY_PC_ID=?"+
						" AND A.TZ_GROUP_ID IS NOT NULL" +
						" AND A.TZ_GROUP_ID != ''"+
						" GROUP BY B.TZ_GROUP_NAME";
				List<Map<String, Object>> dataList = sqlQuery.queryForList(sql, new Object[] { classID,batchID });
				if(dataList!=null&&!"".equals(dataList)){
					String sqlRq = "SELECT A.TZ_MS_DATE"+
							" From PS_TZ_MSSJ_ARR_TBL A,PS_TZ_MSYY_KS_TBL B,PS_TZ_FORM_WRK_T C,PS_TZ_MSPS_KSH_TBL D,PS_TZ_INTEGROUP_T E"+
							" Where A.TZ_CLASS_ID=B.TZ_CLASS_ID"+
							" AND A.TZ_BATCH_ID=B.TZ_BATCH_ID"+
							" AND A.TZ_MS_PLAN_SEQ=B.TZ_MS_PLAN_SEQ"+
							" AND A.TZ_CLASS_ID=D.TZ_CLASS_ID"+
							" AND A.TZ_BATCH_ID=D.TZ_APPLY_PC_ID"+
							" AND B.OPRID=C.OPRID"+
							" AND C.TZ_APP_INS_ID=D.TZ_APP_INS_ID"+
							" AND D.TZ_CLASS_ID=E.TZ_CLASS_ID"+
							" AND D.TZ_APPLY_PC_ID=E.TZ_APPLY_PC_ID"+
							" AND D.TZ_GROUP_ID=E.TZ_GROUP_ID"+
							" AND E.TZ_GROUP_NAME=?"+
							" AND E.TZ_CLASS_ID=?"+
							" AND E.TZ_APPLY_PC_ID=?"+
							" LIMIT 1";
					if(dataList.size()>0){
						for(int i =0;i<dataList.size();i++){
							Map<String, Object> mapList = new HashMap<String, Object>();
							String rq = sqlQuery.queryForObject(sqlRq, new Object[] { dataList.get(i).get("TZ_GROUP_NAME"),classID,batchID },"String");
							selOne = rq + " 第"+dataList.get(i).get("TZ_GROUP_NAME")+"面试情况一览表";
							mapList.put("selOne", selOne);
							mapList.put("classID", classID);
							mapList.put("batchID", batchID);
							listData.add(mapList);
						}
					}else{
						Map<String, Object> mapList = new HashMap<String, Object>();
						mapList.put("selOne", selOne);
						mapList.put("classID", classID);
						mapList.put("batchID", batchID);
						listData.add(mapList);
					}
				}else{
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("selOne", selOne);
					mapList.put("classID", classID);
					mapList.put("batchID", batchID);
					listData.add(mapList);
				}
				
			}else{
				errorMsg[0] = "1";
				errorMsg[1] = "参数有问题";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		mapRet.replace("root", listData);
		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}
	
	@Override
	public String tzOther(String strType, String strParams, String[] errorMsg) {
		String strRet = "{}";
		try {
			switch (strType) {
			case "queryColumns":
				strRet = this.queryColumns(strParams, errorMsg);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
		}
		return strRet;
	}
	
	//获取列
	private String queryColumns(String strParams, String[] errorMsg) {
		String strRet = "";
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		ArrayList<Map<String, Object>> dataListAll = new ArrayList<Map<String, Object>>();
		ArrayList<Map<String, Object>> dataListX = new ArrayList<Map<String, Object>>();
		rtnMap.put("all", dataListAll);
		rtnMap.put("x", dataListX);

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String classID = jacksonUtil.getString("classID");
			String batchID = jacksonUtil.getString("batchID");
			String selOne = jacksonUtil.getString("selOne");
			String groupName = selOne.substring(12, 14);
			if(classID!=null&&batchID!=null){
				//横轴
				String sqlX = "SELECT distinct A.TZ_ORDER"+
						" From PS_TZ_MSPS_KSH_TBL A,PS_TZ_INTEGROUP_T B,PS_TZ_PWZ_VW C,PS_TZ_AQ_YHXX_TBL D,PS_TZ_FORM_WRK_T E"+
						" Where A.TZ_GROUP_ID=B.TZ_GROUP_ID"+
						" AND A.TZ_CLASS_ID=B.TZ_CLASS_ID"+
						" AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID"+
						" AND A.TZ_APP_INS_ID=E.TZ_APP_INS_ID"+
						" AND E.OPRID=D.OPRID"+
						" AND B.TZ_CLPS_GR_ID=C.TZ_CLPS_GR_ID"+
						" AND B.TZ_CLASS_ID=C.TZ_CLASS_ID"+
						" AND B.TZ_APPLY_PC_ID=C.TZ_APPLY_PC_ID"+
						" AND B.TZ_CLASS_ID=?"+ 
						" AND B.TZ_APPLY_PC_ID=?"+
						" AND B.TZ_GROUP_NAME=?"+
						" ORDER BY A.TZ_ORDER ASC";
				List<Map<String, Object>> dataList1 = sqlQuery.queryForList(sqlX, new Object[] { classID,batchID,groupName });
				rtnMap.put("size", dataList1.size());
				if(dataList1!=null&&!"".equals(dataList1)){
					for(int i =0;i<dataList1.size();i++){
						Map<String, Object> mapList = new HashMap<String, Object>();
						String order = String.valueOf(dataList1.get(i).get("TZ_ORDER"));
						mapList.put("order", order);
						dataListX.add(mapList);
					}
				}
				
				//纵轴
				String sqlY = "SELECT distinct C.TZ_CLPS_GR_NAME"+
						" From PS_TZ_MSPS_KSH_TBL A,PS_TZ_INTEGROUP_T B,PS_TZ_PWZ_VW C,PS_TZ_AQ_YHXX_TBL D,PS_TZ_FORM_WRK_T E"+
						" Where A.TZ_GROUP_ID=B.TZ_GROUP_ID"+
						" AND A.TZ_CLASS_ID=B.TZ_CLASS_ID"+
						" AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID"+
						" AND A.TZ_APP_INS_ID=E.TZ_APP_INS_ID"+
						" AND E.OPRID=D.OPRID"+
						" AND B.TZ_CLPS_GR_ID=C.TZ_CLPS_GR_ID"+
						" AND B.TZ_CLASS_ID=C.TZ_CLASS_ID"+
						" AND B.TZ_APPLY_PC_ID=C.TZ_APPLY_PC_ID"+
						" AND B.TZ_CLASS_ID=?"+ 
						" AND B.TZ_APPLY_PC_ID=?"+
						" AND B.TZ_GROUP_NAME=?"+
						" ORDER BY C.TZ_CLPS_GR_NAME ASC";
				List<Map<String, Object>> dataList2 = sqlQuery.queryForList(sqlY, new Object[] { classID,batchID,groupName });
				rtnMap.put("psSize", dataList2.size());
				
				//所有
				String sql = "SELECT A.TZ_MSZT,A.TZ_MSSJ,C.TZ_CLPS_GR_NAME,D.TZ_REALNAME,A.TZ_ORDER"+
						" From PS_TZ_MSPS_KSH_TBL A,PS_TZ_INTEGROUP_T B,PS_TZ_PWZ_VW C,PS_TZ_AQ_YHXX_TBL D,PS_TZ_FORM_WRK_T E"+
						" Where A.TZ_GROUP_ID=B.TZ_GROUP_ID"+
						" AND A.TZ_CLASS_ID=B.TZ_CLASS_ID"+
						" AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID"+
						" AND A.TZ_APP_INS_ID=E.TZ_APP_INS_ID"+
						" AND E.OPRID=D.OPRID"+
						" AND B.TZ_CLPS_GR_ID=C.TZ_CLPS_GR_ID"+
						" AND B.TZ_CLASS_ID=C.TZ_CLASS_ID"+
						" AND B.TZ_APPLY_PC_ID=C.TZ_APPLY_PC_ID"+
						" AND B.TZ_CLASS_ID=?"+ 
						" AND B.TZ_APPLY_PC_ID=?"+
						" AND B.TZ_GROUP_NAME=?"+
						" AND C.TZ_CLPS_GR_NAME=?"+
						" ORDER BY A.TZ_ORDER,C.TZ_CLPS_GR_NAME ASC";
				if(dataList2!=null&&!"".equals(dataList2)){
					for(int i =0;i<dataList2.size();i++){
						List<Map<String, Object>> dataList = sqlQuery.queryForList(sql, new Object[] { classID,batchID,groupName,dataList2.get(i).get("TZ_CLPS_GR_NAME")});
						Map<String, Object> mapList = new HashMap<String, Object>();
						String psName = (String) dataList2.get(i).get("TZ_CLPS_GR_NAME");
						mapList.put("psName", psName);
						
						if(dataList!=null&&!"".equals(dataList)){
							for(int j = 0;j<dataList.size();j++){
								String order = String.valueOf(dataList.get(j).get("TZ_ORDER"));
								String mszt = String.valueOf(dataList.get(j).get("TZ_MSZT"))==null?"":String.valueOf(dataList.get(j).get("TZ_MSZT"));
								Date mssj = (Date) dataList.get(j).get("TZ_MSSJ");
								String mssjStr = "";
								if(mssj!=null&&!"".equals(mssj)){
									 SimpleDateFormat format = new SimpleDateFormat("HH:mm");
									 mssjStr = format.format(mssj);
								}
								String realName = (String) dataList.get(j).get("TZ_REALNAME")==null?"":(String) dataList.get(j).get("TZ_REALNAME");
								mapList.put("order"+j, order);
								mapList.put("mszt"+j, mszt);
								mapList.put("mssj"+j, mssjStr);
								mapList.put("realName"+j, realName+" "+mssjStr);
							}
						}
						dataListAll.add(mapList);
					}
				}
				
			}else{
				errorMsg[0] = "1";
				errorMsg[1] = "参数有问题";
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
		}
		rtnMap.replace("all", dataListAll);
		rtnMap.replace("x", dataListX);
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
}
