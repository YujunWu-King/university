package com.tranzvision.gd.TZEnrollmentClueBundle.service.impl;

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
	private CreateTaskServiceImpl createTaskServiceImpl;
	
	
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
			String[] resultFldArray = { "TZ_LEAD_ID","TZ_JG_ID","TZ_REALNAME", "TZ_MOBILE", "TZ_COMP_CNAME", "TZ_POSITION","TZ_BMR_STATUS_DESC",
					"TZ_BZ","TZ_LEAD_STATUS","TZ_LEAD_STATUS_DESC","TZ_ZR_OPRID","TZ_ZRR_NAME","ROW_ADDED_DTTM",
					"TZ_RSFCREATE_WAY_DESC","TZ_THYY_DESC","TZ_GBYY_DESC","TZ_REASON","TZ_EMAIL"};
			// 默认显示当前机构下，责任人是当前登录人或没有责任人的线索	
			jacksonUtil.json2Map(strParams);
			if(jacksonUtil.containsKey("defaultFlag")) {
				String subStr=strParams.substring(1, strParams.length()-2);
				String condition=subStr+"\"TZ_LEAD_STATUS-operator\":\"02\",\"TZ_LEAD_STATUS-value\":\"G\",\"TZ_ZR_OPRID2-operator\":\"10\",\"TZ_ZR_OPRID2-value\":\"" + oprid + ",NEXT\",\"TZ_JG_ID-operator\":\"01\",\"TZ_JG_ID-value\":\"" + orgId + "\"}";
				strParams="{"+condition+"}";
			}
			
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, strParams, numLimit, numStart, errorMsg);

			if(obj!=null && obj.length>0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				
				for(int i=0;i<list.size();i++) {
					String[] rowList = list.get(i);
					
					//邮箱取值：线索对应的报名表对应的OPRID的邮箱
//					String email = "";
//					String bmrOprid=sqlQuery.queryForObject("select B.OPRID from PS_TZ_XSXS_BMB_T A LEFT JOIN PS_TZ_FORM_WRK_T B on A.TZ_APP_INS_ID=B.TZ_APP_INS_ID where A.TZ_LEAD_ID=?", new Object[]{rowList[0]}, "String");
//					if(bmrOprid!=null && !"".equals(bmrOprid)) {
//						email=sqlQuery.queryForObject("SELECT A.TZ_ZY_EMAIL FROM PS_TZ_LXFSINFO_TBL A,PS_TZ_AQ_YHXX_TBL B WHERE A.TZ_LXFS_LY=B.TZ_RYLX AND A.TZ_LYDX_ID=B.OPRID AND B.OPRID=?", new Object[]{bmrOprid}, "String");
//					}
					
					Map<String, Object> mapList = new HashMap<String,Object>();
					mapList.put("clueId", rowList[0]);
					mapList.put("name", rowList[2]);
					mapList.put("mobile", rowList[3]);
					mapList.put("companyName", rowList[4]);
					mapList.put("position", rowList[5]);
					mapList.put("applyState", rowList[6]);
					mapList.put("memo", rowList[7]);
					mapList.put("clueState", rowList[8]);
					mapList.put("clueStateDesc", rowList[9]);
					mapList.put("chargeOprid", rowList[10]);
					mapList.put("chargeName", rowList[11]);
					mapList.put("createDttm", rowList[12]);
					mapList.put("createWayDesc", rowList[13]);
					mapList.put("reason", rowList[16]);
					mapList.put("email", rowList[17]);
					
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
			
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return strRet;		
	}
}
