package com.tranzvision.gd.TZMyEnrollmentClueBundle.service.impl;

import java.util.ArrayList;
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
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
@Service("com.tranzvision.gd.TZMyEnrollmentClueBundle.service.impl.TZMyEnrollmentClueServiceImpl")
public class TZMyEnrollmentClueServiceImpl extends FrameworkImpl {
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	@Autowired
	private SqlQuery sqlQuery;
	
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");
        String oprid=tzLoginServiceImpl.getLoginedManagerOprid(request);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {{ "ROW_ADDED_DTTM", "DESC" }};
			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_LEAD_ID","TZ_JG_ID","TZ_REALNAME", "TZ_LEAD_STATUS","TZ_LEAD_STATUS_DESC", "TZ_COMP_CNAME", 
					"TZ_MOBILE","TZ_POSITION","TZ_BMR_STATUS_DESC","TZ_BZ","ROW_ADDED_DTTM","TZ_RSFCREATE_WAY_DESC","TZ_ZR_OPRID",
					"TZ_ZRR_NAME","TZ_COLOUR_SORT_ID","TZ_REASON", "TZ_EMAIL","TZ_KH_OPRID","TZ_BATCH_NAME"};
			// 可配置搜索通用函数;
			//后台增加可配置搜索条件,责任人是"我"的销售线索
			String subStr=strParams.substring(1, strParams.length()-2);
			String condition=subStr+ ",\"TZ_ZR_OPRID2-operator\":\"01\",\"TZ_ZR_OPRID2-value\":\"" + oprid + "\"}";
			strParams="{"+condition+"}";
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				@SuppressWarnings("unchecked")
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					
					//邮箱取值：线索对应的报名表对应的OPRID的邮箱
//					String tzEmail = "";
//					String bmrOprid=sqlQuery.queryForObject("select B.OPRID from PS_TZ_XSXS_BMB_T A LEFT JOIN PS_TZ_FORM_WRK_T B on A.TZ_APP_INS_ID=B.TZ_APP_INS_ID where A.TZ_LEAD_ID=?", new Object[]{rowList[0]}, "String");
//					if(bmrOprid!=null && !"".equals(bmrOprid)) {
//						tzEmail=sqlQuery.queryForObject("SELECT A.TZ_ZY_EMAIL FROM PS_TZ_LXFSINFO_TBL A,PS_TZ_AQ_YHXX_TBL B WHERE A.TZ_LXFS_LY=B.TZ_RYLX AND A.TZ_LYDX_ID=B.OPRID AND B.OPRID=?", new Object[]{bmrOprid}, "String");
//					}
					
					mapList.put("clueId", rowList[0]);
					mapList.put("orgID", rowList[1]);
					mapList.put("cusName", rowList[2]);
					mapList.put("clueState", rowList[3]);
					mapList.put("clueStateDesc", rowList[4]);
					mapList.put("comName", rowList[5]);
					mapList.put("cusMobile", rowList[6]);
					mapList.put("cusPos", rowList[7]);
					mapList.put("bmStateDesc", rowList[8]);
					mapList.put("cusBz", rowList[9]);
					mapList.put("createDate", rowList[10]);
					mapList.put("createWayDesc", rowList[11]);
					mapList.put("chargeOprid", rowList[12]);
					mapList.put("zrPer", rowList[13]);
					mapList.put("colorType", rowList[14]);
					mapList.put("gbyy", rowList[15]);
					mapList.put("email", rowList[16]);
					//mapList.put("batchId", rowList[17]);
					mapList.put("ksoprid", rowList[17]);
					mapList.put("batchName", rowList[18]);
							
					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jacksonUtil.Map2json(mapRet);
	}
	
	/* 添加听众 */
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
				String sql = jacksonUtil.getString("sql");
				boolean bMultiType = false;
				boolean dxType = false;
				boolean selyjType = false;
				boolean seldxType = false;
			      
				if("MULTI".equals(strType)){
					audID = createTaskServiceImpl.createAudience("",orgId,"我的招生线索管理批量发送邮件", "XSXS");
					bMultiType = true;
				}
				
				if("DX".equals(strType)){
					audID = createTaskServiceImpl.createAudience("",orgId,"我的招生线索管理批量发送短信", "XSXS");
					dxType = true;
				}
				
				if("SELYJ".equals(strType)){
					audID = createTaskServiceImpl.createAudience("",orgId,"给搜索结果发送邮件", "XSXS");
					selyjType = true;
				}
				
				if("SELDX".equals(strType)){
					audID = createTaskServiceImpl.createAudience("",orgId,"给搜索结果发送短信", "XSXS");
					seldxType = true;
				}
				
				
				if(selyjType||seldxType){
					//搜索结果发送
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					if("default".equals(sql)){
						sql = "SELECT ifnull(TZ_LEAD_ID,'') TZ_LEAD_ID FROM PS_TZ_XSXS_INFO_VW WHERE TZ_JG_ID=? AND TZ_LEAD_STATUS<>'G'";
						String sqlStr = "SELECT TZ_REALNAME,TZ_EMAIL,TZ_MOBILE,";
						sql = sql.replace("SELECT ", sqlStr)+" AND TZ_ZR_OPRID2 =?";
						list = sqlQuery.queryForList(sql,new Object[]{orgId,oprid});
					}else{
						String sqlStr = "SELECT TZ_REALNAME,TZ_EMAIL,TZ_MOBILE,";
						sql = sql.replace("SELECT ", sqlStr)+" AND TZ_ZR_OPRID2 =?";
						list = sqlQuery.queryForList(sql,new Object[]{oprid});
					}
					for(int num_1=0;num_1<list.size();num_1++){
						Map<String, Object> map = list.get(num_1);
						String clueId=(String)map.get("TZ_LEAD_ID");
						String name = (String)map.get("TZ_REALNAME");
			            String email = (String)map.get("TZ_EMAIL");
			            String mobile=(String)map.get("TZ_MOBILE");
			            if(oprid != null && !"".equals(oprid)){
			                createTaskServiceImpl.addAudCy(audID,name, "", mobile, mobile, email, email, "", oprid, "", "", clueId);
			            }
					}
				}else{
					//添加听众;
					//选择发送
					@SuppressWarnings("unchecked")
					List<Map<String, Object>> list = (List<Map<String, Object>>) jacksonUtil.getList("personList");
					if(list != null && list.size() > 0){
						for(int num_1 = 0; num_1 < list.size(); num_1 ++){
							Map<String, Object> map = list.get(num_1);
							String name = (String)map.get("name");
				            String email = (String)map.get("email");
				            String clueId=(String)map.get("clueId");
				            String mobile=(String)map.get("mobile");
				            if(oprid != null && !"".equals(oprid)){
				                createTaskServiceImpl.addAudCy(audID,name, "", mobile, mobile, email, email, "", oprid, "", "", clueId);
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
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String, Object> map = new HashMap<String, Object>();
		//加载更多数据
		if("tzLoadExpandData".equals(oprType)){
			map=this.tzLoadExpandData(strParams);
		}
		return jacksonUtil.Map2json(map);
	}

	private Map<String, Object> tzLoadExpandData(String strParams) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String, Object> mapRet = new HashMap<String, Object>();
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("moreInfo", "[]");
		
		jacksonUtil.json2Map(strParams);
		String clueId=jacksonUtil.getString("clueId");
	    if(clueId!=null&&!"".equals(clueId)){
			String phone=sqlQuery.queryForObject("select TZ_PHONE from PS_TZ_XSXS_INFO_T where TZ_LEAD_ID=?", new Object[]{clueId},"String");
			Map<String, Object> mapList = new HashMap<String, Object>();
		    mapList.put("itemName","电话");
		    mapList.put("itemValue",phone);
		    listData.add(mapList);
		}
	    mapRet.replace("moreInfo", listData);
		return mapRet;
	}
}
