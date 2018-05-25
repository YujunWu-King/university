package com.tranzvision.gd.TZEnrollmentClueBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzWtxsTmpTMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzZdfpFlagTMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzWtxsTmpT;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzWtxsTmpTKey;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzZdfpFlagT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 招生线索管理
 * @author LuYan 2017-10-09
 *
 */
@Service("com.tranzvision.gd.TZEnrollmentClueBundle.service.impl.TzEnrollmentClueServiceImpl")
public class TzEnrollmentClueServiceImpl extends FrameworkImpl {

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	@Autowired
	private PsTzWtxsTmpTMapper psTzWtxsTmpTMapper;
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	@Autowired
	private PsTzZdfpFlagTMapper psTzZdfpFlagTMapper;
	@Autowired
	private TzClueAutoAssign tzClueAutoAssign;
	
	
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String strParams,int numLimit,int numStart,String[] errorMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			//当前登录人
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			//当前机构
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {{ "TZ_DATE_RANK", "DESC" }};
			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_LEAD_ID","TZ_JG_ID","TZ_REALNAME", "TZ_MOBILE", "TZ_COMP_CNAME", "TZ_POSITION","TZ_XSQU_ID","TZ_XSQU_DESC",
					"TZ_BMR_STATUS_DESC","TZ_BZ","TZ_LEAD_STATUS","TZ_LEAD_STATUS_DESC","TZ_ZR_OPRID","TZ_ZRR_NAME","TZ_REFEREE_NAME","ROW_ADDED_DTTM",
					"TZ_RSFCREATE_WAY_DESC","TZ_THYY_DESC","TZ_GBYY_DESC","TZ_REASON"};
			// 默认显示当前机构下，责任人是当前登录人或没有责任人的线索	
			jacksonUtil.json2Map(strParams);
			if(jacksonUtil.containsKey("defaultFlag")) {
				String subStr=strParams.substring(1, strParams.length()-2);
				String condition=subStr+"\"TZ_LEAD_STATUS-operator\":\"02\",\"TZ_LEAD_STATUS-value\":\"G\",\"TZ_ZR_OPRID-operator\":\"10\",\"TZ_ZR_OPRID-value\":\"" + oprid + ",NEXT\",\"TZ_JG_ID-operator\":\"01\",\"TZ_JG_ID-value\":\"" + orgId + "\"}";
				strParams="{"+condition+"}";
			}
			
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, strParams, numLimit, numStart, errorMsg);

			if(obj!=null && obj.length>0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				
				for(int i=0;i<list.size();i++) {
					String[] rowList = list.get(i);
					
					//邮箱取值：线索对应的报名表对应的OPRID的邮箱
					String email = "";
					String bmrOprid=sqlQuery.queryForObject("select B.OPRID from PS_TZ_XSXS_BMB_T A LEFT JOIN PS_TZ_FORM_WRK_T B on A.TZ_APP_INS_ID=B.TZ_APP_INS_ID where A.TZ_LEAD_ID=?", new Object[]{rowList[0]}, "String");
					if(bmrOprid!=null && !"".equals(bmrOprid)) {
						email=sqlQuery.queryForObject("SELECT A.TZ_ZY_EMAIL FROM PS_TZ_LXFSINFO_TBL A,PS_TZ_AQ_YHXX_TBL B WHERE A.TZ_LXFS_LY=B.TZ_RYLX AND A.TZ_LYDX_ID=B.OPRID AND B.OPRID=?", new Object[]{bmrOprid}, "String");
					}
					
					Map<String, Object> mapList = new HashMap<String,Object>();
					mapList.put("clueId", rowList[0]);
					mapList.put("name", rowList[2]);
					mapList.put("mobile", rowList[3]);
					mapList.put("companyName", rowList[4]);
					mapList.put("position", rowList[5]);
					mapList.put("localId", rowList[6]);
					mapList.put("localAddress", rowList[7]);
					mapList.put("applyState", rowList[8]);
					mapList.put("memo", rowList[9]);
					mapList.put("clueState", rowList[10]);
					mapList.put("clueStateDesc", rowList[11]);
					mapList.put("chargeOprid", rowList[12]);
					mapList.put("chargeName", rowList[13]);
					mapList.put("recommendPer", rowList[14]);
					mapList.put("createDttm", rowList[15]);
					mapList.put("createWayDesc", rowList[16]);
					mapList.put("reason", rowList[19]);
					mapList.put("email", email);
					
					listData.add(mapList);
				}
				
				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
				
				strRet = jacksonUtil.Map2json(mapRet);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return strRet;	
	}
	
	
	/* 批量发送邮件添加听众 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		// 返回值;
		String audID= "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (actData.length == 0) {
			return audID;
		}
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			for (int num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String strType = jacksonUtil.getString("type");
				boolean bMultiType = false;
			      
				if("MULTI".equals(strType)){
					audID = createTaskServiceImpl.createAudience("",orgId,"招生线索管理批量发送邮件", "XSXS");
					bMultiType = true;
				}
								
				if(bMultiType){
					//群发邮件添加听众;
					@SuppressWarnings("unchecked")
					List<Map<String, Object>> list = (List<Map<String, Object>>) jacksonUtil.getList("personList");
					if(list != null && list.size() > 0){
						for(int num_1 = 0; num_1 < list.size(); num_1 ++){
							Map<String, Object> map = list.get(num_1);
							String name = (String)map.get("name");
				            String email = (String)map.get("email");
				            String clueId=(String)map.get("clueId");
				            if(oprid != null && !"".equals(oprid)
				            		&& email!=null&&!"".equals(email)){
				                createTaskServiceImpl.addAudCy(audID,name, "", "", "", email, email, "", oprid, "", "", clueId);
				            }
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return audID;
	}
	
	
	@Override
	public String tzOther(String operateType,String strParams,String[] errorMsg) {
		String strRet = "";
		
		try {
			//查询是否存在有问题线索
			if("tzHaveProblemClue".equals(operateType)) {
				strRet = haveProblemClue(strParams,errorMsg);
			}
			//开启自动分配
			if("tzOpenAutoAssign".equals(operateType)) {
				strRet = openAutoAssign(strParams,errorMsg);
			}
			//关闭自动分配
			if("tzCloseAutoAssign".equals(operateType)) {
				strRet = closeAutoAssign(strParams,errorMsg);
			}
			//选中线索自动分配
			if("tzAutoAssignBatch".equals(operateType)) {
				strRet = autoAssignBatch(strParams,errorMsg);
			}
			//查找自动分配是否开启
			if("tzAutoAssignFlag".equals(operateType)) {
				strRet = autoAssignFlag(strParams,errorMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return strRet;		
	}
	
	
	
	/*查询是否存在有问题线索*/
	public String haveProblemClue(String strParams,String[] errorMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
				
		try {
			
			//当前机构
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			//当前登录人
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			//删除当前登录人的临时表数据
			sqlQuery.update("DELETE FROM PS_TZ_WTXS_TMP_T WHERE OPRID=?",new Object[]{oprid});
			sqlQuery.update("COMMIT");
			
			//从几个月之前的数据开始算
			String setMonth = getHardCodePoint.getHardCodePointVal("TZ_WTXS_SET_MONTH");
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			Calendar nowTime = Calendar.getInstance();
			nowTime.add(Calendar.MONTH, Integer.valueOf(setMonth));
			String queryDate = sdf.format(nowTime.getTime());
			
			
			/*
			 * 从“未关闭”线索查找，过滤掉从848导入的历史线索，查询两年内线索
			 * 一定是责任人不一样或为空
			 * 先根据“姓名+手机”查找
			 * 再根据“手机”查找
			 * 再根据“姓名”查找
			 * 写入临时表中
			 */
			String clueId = "",realname = "",mobile = "",leadDesc = "";
			String sql_1 = "SELECT DISTINCT A.TZ_LEAD_ID,A.TZ_REALNAME, A.TZ_MOBILE FROM PS_TZ_XSXS_INFO_T A,PS_TZ_XSXS_INFO_T B";
			sql_1 += " WHERE A.TZ_REALNAME=B.TZ_REALNAME AND A.TZ_MOBILE=B.TZ_MOBILE";
			sql_1 += " AND A.TZ_MOBILE<>' ' AND A.TZ_MOBILE IS NOT NULL";
			sql_1 += " AND B.TZ_MOBILE<>' ' AND B.TZ_MOBILE IS NOT NULL";
			sql_1 += " AND (A.TZ_ZR_OPRID<>B.TZ_ZR_OPRID OR (A.TZ_ZR_OPRID='' OR A.TZ_ZR_OPRID IS NULL) OR (B.TZ_ZR_OPRID='' OR B.TZ_ZR_OPRID IS NULL))";
			sql_1 += " AND A.TZ_LEAD_ID<>B.TZ_LEAD_ID";
			sql_1 += " AND A.TZ_LEAD_STATUS<>'G'";
			//sql_1 += " AND A.TZ_LEAD_ID NOT LIKE 'LS_%'";
			sql_1 += " AND DATE_FORMAT(A.ROW_ADDED_DTTM,'%Y-%m-%d') > DATE_FORMAT('"+ queryDate +"' ,'%Y-%m-%d')";
			sql_1 += " AND B.TZ_LEAD_STATUS<>'G'";
			//sql_1 += " AND B.TZ_LEAD_ID NOT LIKE 'LS_%'";
			sql_1 += " AND DATE_FORMAT(B.ROW_ADDED_DTTM,'%Y-%m-%d') > DATE_FORMAT('"+ queryDate +"' ,'%Y-%m-%d')";
			
			List<Map<String, Object>> listData_1 = sqlQuery.queryForList(sql_1);
			for(Map<String, Object> mapData_1 : listData_1) {
				clueId = mapData_1.get("TZ_LEAD_ID") == null ? "" : mapData_1.get("TZ_LEAD_ID").toString();
				realname = mapData_1.get("TZ_REALNAME") == null ? "" : mapData_1.get("TZ_REALNAME").toString();
				mobile = mapData_1.get("TZ_MOBILE") == null ? "" : mapData_1.get("TZ_MOBILE").toString();
				leadDesc = realname + "—" + mobile;
				
				
				PsTzWtxsTmpTKey psTzWtxsTmpTKey = new PsTzWtxsTmpTKey();
				psTzWtxsTmpTKey.setTzLeadId(clueId);
				psTzWtxsTmpTKey.setOprid(oprid);
				psTzWtxsTmpTKey.setTzWtType("A");
				
				PsTzWtxsTmpT psTzWtxsTmpT = psTzWtxsTmpTMapper.selectByPrimaryKey(psTzWtxsTmpTKey);
				if(psTzWtxsTmpT==null) {
					psTzWtxsTmpT = new PsTzWtxsTmpT();
					psTzWtxsTmpT.setTzLeadId(clueId);
					psTzWtxsTmpT.setOprid(oprid);
					psTzWtxsTmpT.setTzJgId(orgId);
					psTzWtxsTmpT.setTzWtType("A"); //姓名+手机一致
					psTzWtxsTmpT.setTzLeadDescr(leadDesc);
					psTzWtxsTmpT.setRowAddedDttm(new Date());
					psTzWtxsTmpT.setRowAddedOprid(oprid);
					psTzWtxsTmpT.setRowLastmantDttm(new Date());
					psTzWtxsTmpT.setRowLastmantOprid(oprid);
					psTzWtxsTmpTMapper.insertSelective(psTzWtxsTmpT);
				}
				
			}
			
			
			String sql_2 = "SELECT DISTINCT A.TZ_LEAD_ID, A.TZ_MOBILE FROM PS_TZ_XSXS_INFO_T A,PS_TZ_XSXS_INFO_T B";
			sql_2 += " WHERE A.TZ_REALNAME<>B.TZ_REALNAME AND A.TZ_MOBILE=B.TZ_MOBILE";
			sql_2 += " AND A.TZ_MOBILE<>' ' AND A.TZ_MOBILE IS NOT NULL";
			sql_2 += " AND B.TZ_MOBILE<>' ' AND B.TZ_MOBILE IS NOT NULL";
			sql_2 += " AND (A.TZ_ZR_OPRID<>B.TZ_ZR_OPRID OR (A.TZ_ZR_OPRID='' OR A.TZ_ZR_OPRID IS NULL) OR (B.TZ_ZR_OPRID='' OR B.TZ_ZR_OPRID IS NULL))";
			sql_2 += " AND A.TZ_LEAD_ID<>B.TZ_LEAD_ID";
			//sql_2 += " AND NOT EXISTS (SELECT 'Y' FROM PS_TZ_WTXS_TMP_T X WHERE X.TZ_LEAD_ID=A.TZ_LEAD_ID AND X.OPRID=?)";
			//sql_2 += " AND NOT EXISTS (SELECT 'Y' FROM PS_TZ_WTXS_TMP_T X WHERE X.TZ_LEAD_ID=B.TZ_LEAD_ID AND X.OPRID=?)";
			sql_2 += " AND A.TZ_LEAD_STATUS<>'G'";
			//sql_2 += " AND A.TZ_LEAD_ID NOT LIKE 'LS_%'";
			sql_2 += " AND DATE_FORMAT(A.ROW_ADDED_DTTM,'%Y-%m-%d') > DATE_FORMAT('"+ queryDate +"' ,'%Y-%m-%d')";
			sql_2 += " AND B.TZ_LEAD_STATUS<>'G'";
			//sql_2 += " AND B.TZ_LEAD_ID NOT LIKE 'LS_%'";
			sql_2 += " AND DATE_FORMAT(B.ROW_ADDED_DTTM,'%Y-%m-%d') > DATE_FORMAT('"+ queryDate +"' ,'%Y-%m-%d')";
			//List<Map<String, Object>> listData_2 = sqlQuery.queryForList(sql_2,new Object[]{oprid,oprid});
			List<Map<String, Object>> listData_2 = sqlQuery.queryForList(sql_2);
			for(Map<String, Object> mapData_2 : listData_2) {
				clueId = mapData_2.get("TZ_LEAD_ID") == null ? "" : mapData_2.get("TZ_LEAD_ID").toString();
				mobile = mapData_2.get("TZ_MOBILE") == null ? "" : mapData_2.get("TZ_MOBILE").toString();
				leadDesc =  mobile;
				
				
				PsTzWtxsTmpTKey psTzWtxsTmpTKey = new PsTzWtxsTmpTKey();
				psTzWtxsTmpTKey.setTzLeadId(clueId);
				psTzWtxsTmpTKey.setOprid(oprid);
				psTzWtxsTmpTKey.setTzWtType("B");
				
				PsTzWtxsTmpT psTzWtxsTmpT = psTzWtxsTmpTMapper.selectByPrimaryKey(psTzWtxsTmpTKey);
				if(psTzWtxsTmpT==null) {
					psTzWtxsTmpT = new PsTzWtxsTmpT();
					psTzWtxsTmpT.setTzLeadId(clueId);
					psTzWtxsTmpT.setOprid(oprid);
					psTzWtxsTmpT.setTzJgId(orgId);
					psTzWtxsTmpT.setTzWtType("B"); //手机一致
					psTzWtxsTmpT.setTzLeadDescr(leadDesc);
					psTzWtxsTmpT.setRowAddedDttm(new Date());
					psTzWtxsTmpT.setRowAddedOprid(oprid);
					psTzWtxsTmpT.setRowLastmantDttm(new Date());
					psTzWtxsTmpT.setRowLastmantOprid(oprid);
					psTzWtxsTmpTMapper.insertSelective(psTzWtxsTmpT);
				}	
			}
			
			
			String sql_3 = "SELECT DISTINCT A.TZ_LEAD_ID, A.TZ_REALNAME FROM PS_TZ_XSXS_INFO_T A,PS_TZ_XSXS_INFO_T B";
			sql_3 += " WHERE A.TZ_REALNAME=B.TZ_REALNAME";
			sql_3 += " AND (A.TZ_MOBILE<>B.TZ_MOBILE OR (A.TZ_MOBILE='' OR A.TZ_MOBILE IS NULL) OR (B.TZ_MOBILE='' OR B.TZ_MOBILE IS NULL))";
			sql_3 += " AND (A.TZ_ZR_OPRID<>B.TZ_ZR_OPRID OR (A.TZ_ZR_OPRID='' OR A.TZ_ZR_OPRID IS NULL) OR (B.TZ_ZR_OPRID='' OR B.TZ_ZR_OPRID IS NULL))";
			sql_3 += " AND A.TZ_LEAD_ID<>B.TZ_LEAD_ID";
			//sql_3 += " AND NOT EXISTS (SELECT 'Y' FROM PS_TZ_WTXS_TMP_T X WHERE X.TZ_LEAD_ID=A.TZ_LEAD_ID AND X.OPRID=?)";
			//sql_3 += " AND NOT EXISTS (SELECT 'Y' FROM PS_TZ_WTXS_TMP_T X WHERE X.TZ_LEAD_ID=B.TZ_LEAD_ID AND X.OPRID=?)";
			sql_3 += " AND A.TZ_LEAD_STATUS<>'G'";
			//sql_3 += " AND A.TZ_LEAD_ID NOT LIKE 'LS_%'";
			sql_3 += " AND DATE_FORMAT(A.ROW_ADDED_DTTM,'%Y-%m-%d') > DATE_FORMAT('"+ queryDate +"' ,'%Y-%m-%d')";
			sql_3 += " AND B.TZ_LEAD_STATUS<>'G'";
			//sql_3 += " AND B.TZ_LEAD_ID NOT LIKE 'LS_%'";
			sql_3 += " AND DATE_FORMAT(B.ROW_ADDED_DTTM,'%Y-%m-%d') > DATE_FORMAT('"+ queryDate +"' ,'%Y-%m-%d')";
			//List<Map<String, Object>> listData_3 = sqlQuery.queryForList(sql_3,new Object[]{oprid,oprid});
			List<Map<String, Object>> listData_3 = sqlQuery.queryForList(sql_3);
			for(Map<String, Object> mapData_3 : listData_3) {
				clueId = mapData_3.get("TZ_LEAD_ID") == null ? "" : mapData_3.get("TZ_LEAD_ID").toString();
				realname = mapData_3.get("TZ_REALNAME") == null ? "" : mapData_3.get("TZ_REALNAME").toString();
				leadDesc =  realname;
				
				
				PsTzWtxsTmpTKey psTzWtxsTmpTKey = new PsTzWtxsTmpTKey();
				psTzWtxsTmpTKey.setTzLeadId(clueId);
				psTzWtxsTmpTKey.setOprid(oprid);
				psTzWtxsTmpTKey.setTzWtType("C");
				
				PsTzWtxsTmpT psTzWtxsTmpT = psTzWtxsTmpTMapper.selectByPrimaryKey(psTzWtxsTmpTKey);
				if(psTzWtxsTmpT==null) {
					psTzWtxsTmpT = new PsTzWtxsTmpT();
					psTzWtxsTmpT.setTzLeadId(clueId);
					psTzWtxsTmpT.setOprid(oprid);
					psTzWtxsTmpT.setTzJgId(orgId);
					psTzWtxsTmpT.setTzWtType("C"); //姓名一致
					psTzWtxsTmpT.setTzLeadDescr(leadDesc);
					psTzWtxsTmpT.setRowAddedDttm(new Date());
					psTzWtxsTmpT.setRowAddedOprid(oprid);
					psTzWtxsTmpT.setRowLastmantDttm(new Date());
					psTzWtxsTmpT.setRowLastmantOprid(oprid);
					psTzWtxsTmpTMapper.insertSelective(psTzWtxsTmpT);
				}	
			}
			
			
			//删除掉责任人都为空的线索，这样的不是问题线索+根据同组线索最新的创建时间排序并定义排序数字
			String sql_wtxs = "SELECT TMP.TZ_WT_TYPE,TMP.TZ_LEAD_DESCR FROM ( ";
			sql_wtxs += "SELECT A.TZ_WT_TYPE,A.TZ_LEAD_DESCR,MAX(B.ROW_ADDED_DTTM) TZ_ORDER_DTTM FROM PS_TZ_WTXS_TMP_T A,PS_TZ_XSXS_INFO_T B";
		    sql_wtxs +=" WHERE A.TZ_LEAD_ID=B.TZ_LEAD_ID AND A.OPRID=? AND A.TZ_JG_ID=? GROUP BY A.TZ_WT_TYPE,A.TZ_LEAD_DESCR";
		    sql_wtxs +=" ) TMP";
		    sql_wtxs +=" ORDER BY TMP.TZ_WT_TYPE,TMP.TZ_ORDER_DTTM DESC";
		    
		    //查询目前有几组
		    String countTmp = sqlQuery.queryForObject("SELECT COUNT(1) FROM PS_TZ_WTXS_TMP_T WHERE TZ_JG_ID=? AND OPRID=?", new Object[]{orgId,oprid},"String");
			
		    Integer orderBasicNum = countTmp.length()*10000;
		    Integer orderIncreaseNum = 0;
		    Integer orderNum=0;

			List<Map<String, Object>> list_wtxs = sqlQuery.queryForList(sql_wtxs, new Object[]{oprid,orgId});
			for(Map<String, Object> map_wtxs : list_wtxs) {
				String wtType = map_wtxs.get("TZ_WT_TYPE") == null ? "" : map_wtxs.get("TZ_WT_TYPE").toString();
				String leadDescr = map_wtxs.get("TZ_LEAD_DESCR") == null ? "" : map_wtxs.get("TZ_LEAD_DESCR").toString();
				
				String sql_not_delete = "SELECT COUNT(1) FROM PS_TZ_WTXS_TMP_T A ,PS_TZ_XSXS_INFO_T B";
				sql_not_delete += " WHERE A.TZ_LEAD_ID=B.TZ_LEAD_ID AND B.TZ_ZR_OPRID<>'' AND B.TZ_ZR_OPRID IS NOT NULL";
				sql_not_delete += " AND A.OPRID=? AND A.TZ_JG_ID=? AND A.TZ_WT_TYPE=? AND A.TZ_LEAD_DESCR=?";
				Integer unDeleteCount = sqlQuery.queryForObject(sql_not_delete, new Object[]{oprid,orgId,wtType,leadDescr},"Integer");
				
				if(unDeleteCount>0) {
					orderIncreaseNum++;
					orderNum=orderBasicNum+orderIncreaseNum;
					sqlQuery.update("UPDATE PS_TZ_WTXS_TMP_T SET TZ_ORDER_NUM=? WHERE OPRID=? AND TZ_JG_ID=? AND TZ_WT_TYPE=? AND TZ_LEAD_DESCR=?",new Object[]{orderNum,oprid,orgId,wtType,leadDescr});
					sqlQuery.update("COMMIT");
					
				} else {
					sqlQuery.update("DELETE FROM PS_TZ_WTXS_TMP_T WHERE OPRID=? AND TZ_JG_ID=? AND TZ_WT_TYPE=? AND TZ_LEAD_DESCR=?", new Object[]{oprid,orgId,wtType,leadDescr});
					sqlQuery.update("COMMIT");
				}
			}
			
			Integer count = sqlQuery.queryForObject("SELECT COUNT(1) FROM PS_TZ_WTXS_TMP_T WHERE TZ_JG_ID=? AND OPRID=?", new Object[]{orgId,oprid},"Integer");
			
			mapRet.put("count", count);
			strRet = jacksonUtil.Map2json(mapRet);
			
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	
	/*开启自动分配*/
	public String openAutoAssign(String strParams,String[] errorMsg) {
		String strRet="";
		
		try {
			//当前机构
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			//当前登录人
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			PsTzZdfpFlagT psTzZdfpFlagT = psTzZdfpFlagTMapper.selectByPrimaryKey(orgId);
			if(psTzZdfpFlagT==null) {
				psTzZdfpFlagT = new PsTzZdfpFlagT();
				psTzZdfpFlagT.setTzJgId(orgId);
				psTzZdfpFlagT.setTzZdfpFlg("Y");
				psTzZdfpFlagT.setRowAddedDttm(new Date());
				psTzZdfpFlagT.setRowAddedOprid(oprid);
				psTzZdfpFlagT.setRowLastmantDttm(new Date());
				psTzZdfpFlagT.setRowLastmantOprid(oprid);
				psTzZdfpFlagTMapper.insertSelective(psTzZdfpFlagT);
			} else {
				psTzZdfpFlagT.setTzJgId(orgId);
				psTzZdfpFlagT.setTzZdfpFlg("Y");
				psTzZdfpFlagT.setRowLastmantDttm(new Date());
				psTzZdfpFlagT.setRowLastmantOprid(oprid);
				psTzZdfpFlagTMapper.updateByPrimaryKeySelective(psTzZdfpFlagT);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	
	/*关闭自动分配*/
	public String closeAutoAssign(String strParams,String[] errorMsg) {
		String strRet="";
		
		try {
			//当前机构
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			//当前登录人
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			PsTzZdfpFlagT psTzZdfpFlagT = psTzZdfpFlagTMapper.selectByPrimaryKey(orgId);
			if(psTzZdfpFlagT==null) {
				psTzZdfpFlagT = new PsTzZdfpFlagT();
				psTzZdfpFlagT.setTzJgId(orgId);
				psTzZdfpFlagT.setTzZdfpFlg("N");
				psTzZdfpFlagT.setRowAddedDttm(new Date());
				psTzZdfpFlagT.setRowAddedOprid(oprid);
				psTzZdfpFlagT.setRowLastmantDttm(new Date());
				psTzZdfpFlagT.setRowLastmantOprid(oprid);
				psTzZdfpFlagTMapper.insertSelective(psTzZdfpFlagT);
			} else {
				psTzZdfpFlagT.setTzJgId(orgId);
				psTzZdfpFlagT.setTzZdfpFlg("N");
				psTzZdfpFlagT.setRowLastmantDttm(new Date());
				psTzZdfpFlagT.setRowLastmantOprid(oprid);
				psTzZdfpFlagTMapper.updateByPrimaryKeySelective(psTzZdfpFlagT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	
	/*选中线索自动分配*/
	public String autoAssignBatch(String strParams,String[] errorMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			//当前机构
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			//当前登录人
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			jacksonUtil.json2Map(strParams);
			String clueIdSelect = jacksonUtil.getString("clueIdSelect");
			String[] arrClueId = clueIdSelect.split(",");
			for(String clueId : arrClueId) {
				String countryId = "",localId = "";
				
				//如果线索关联考生，且考生国籍为非中国，根据考生国籍分配，否则根据线索常住地分配
				String appinsId="",ksCountryId="";
				String sqlCountry = "SELECT A.TZ_APP_INS_ID,B.OPRID,C.TZ_COMMENT1";
				sqlCountry+= " FROM PS_TZ_XSXS_BMB_T A,PS_TZ_FORM_WRK_T B,PS_TZ_REG_USER_T C";
				sqlCountry+= " WHERE A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND B.OPRID=C.OPRID AND A.TZ_LEAD_ID=?";
				Map<String, Object> mapCountry = sqlQuery.queryForMap(sqlCountry, new Object[]{clueId});
				if(mapCountry!=null) {
					appinsId=mapCountry.get("TZ_APP_INS_ID") == null ? "0" : mapCountry.get("TZ_APP_INS_ID").toString();
					ksCountryId=mapCountry.get("TZ_COMMENT1") == null ? "" : mapCountry.get("TZ_COMMENT1").toString();
				}
				
				String clueLocalId = "";
				clueLocalId=sqlQuery.queryForObject("SELECT TZ_XSQU_ID FROM PS_TZ_XSXS_INFO_T WHERE TZ_LEAD_ID=?", new Object[]{clueId},"String");
				
				if(!"".equals(appinsId) && !"0".equals(appinsId)) {
					countryId = ksCountryId;
					if("CHN".equals(ksCountryId)) {
						localId = clueLocalId;
					}
				} else {
					countryId="";
					localId=clueLocalId;
				}
				
				String strRetAuto = tzClueAutoAssign.autoAssign(orgId, oprid, clueId, countryId, localId, errorMsg);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	
	/*查找自动分配是否开启*/
	public String autoAssignFlag(String strParams,String[] errorMsg) {
		String strRet="";
		Map<String,Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			//当前机构
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String strFlag = sqlQuery.queryForObject("SELECT TZ_ZDFP_FLG FROM PS_TZ_ZDFP_FLAG_T WHERE TZ_JG_ID=?", new Object[]{orgId},"String");
			if(strFlag!=null && !"".equals(strFlag)) {
				
			} else {
				strFlag="";
			}
			
			mapRet.put("autoFlag", strFlag);
			
			strRet = jacksonUtil.Map2json(mapRet);
			
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return strRet;
	}
}
