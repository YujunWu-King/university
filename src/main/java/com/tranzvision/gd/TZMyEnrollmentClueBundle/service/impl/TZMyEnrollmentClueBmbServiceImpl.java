package com.tranzvision.gd.TZMyEnrollmentClueBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzXsxsBmbTMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsBmbT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
@Service("com.tranzvision.gd.TZMyEnrollmentClueBundle.service.impl.TZMyEnrollmentClueBmbServiceImpl")
public class TZMyEnrollmentClueBmbServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private PsTzXsxsBmbTMapper psTzXsxsBmbTMapper;

	
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");
        
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {{ "ROW_ADDED_DTTM", "DESC" }};
			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_APP_INS_ID","TZ_APP_FORM_STA","TZ_REALNAME", "TZ_MOBILE", "TZ_EMAIL", "TZ_COMPANY_NAME","TZ_CLASS_NAME","ROW_ADDED_DTTM","OPRID","TZ_CLASS_ID","TZ_LEAD_ID"};
			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {
				@SuppressWarnings("unchecked")
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					
					mapList.put("bmbId", rowList[0]);
					mapList.put("bmbSubStatus", rowList[1]);
					mapList.put("bmbName", rowList[2]);
					mapList.put("bmbPhone", rowList[3]);
					mapList.put("bmbEmail", rowList[4]);
					mapList.put("bmbCompany", rowList[5]);
					mapList.put("bmbClass", rowList[6]);
					mapList.put("bmbTime", rowList[7]);
					mapList.put("oprid", rowList[8]);
					mapList.put("classId", rowList[9]);
					mapList.put("bmbClueId", rowList[10]);
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
	
	
	@Override
	@Transactional
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			//当前登录人
			String oprid=tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);
				String bmbId=jacksonUtil.getString("bmbId");
				String clueId=jacksonUtil.getString("clueId");
	
				if(bmbId!=null&&!"".equals(bmbId)&&clueId!=null&&!"".equals(clueId)){
					//线索关联报名表
					PsTzXsxsBmbT PsTzXsxsBmbT=new PsTzXsxsBmbT();
					PsTzXsxsBmbT.setTzLeadId(clueId);
					PsTzXsxsBmbT.setTzAppInsId(Long.parseLong(bmbId));
					PsTzXsxsBmbT.setRowAddedOprid(oprid);
					PsTzXsxsBmbT.setRowAddedDttm(new java.util.Date());
					PsTzXsxsBmbT.setRowLastmantOprid(oprid);
					PsTzXsxsBmbT.setRowLastmantDttm(new java.util.Date());
					psTzXsxsBmbTMapper.insert(PsTzXsxsBmbT);
					
					//根据报名表编号动态获取报考状态
					String bkStatus= this.getClueBkStatus(bmbId);
					mapRet.put("bkStatus", bkStatus);
					
					strRet = jacksonUtil.Map2json(mapRet);
					
				}else{
					errMsg[0] = "1";
					errMsg[1] = "报名表编号或线索编号为空";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
    //根据报名表编号,获取报考状态
	private String getClueBkStatus(String bmbId) {
		String bkStatus = "";
		//报考状态
		String sql = "select 'Y' from PS_TZ_XSXS_BMB_T where TZ_APP_INS_ID=? limit 0,1";
		String bmbExists = sqlQuery.queryForObject(sql, new Object[]{ bmbId }, "String");
		
		if("Y".equals(bmbExists)){
			//已报名
			bkStatus="B";
		}else{
			//未报名
			bkStatus="A";
		}
        
		return bkStatus;
	}
}
