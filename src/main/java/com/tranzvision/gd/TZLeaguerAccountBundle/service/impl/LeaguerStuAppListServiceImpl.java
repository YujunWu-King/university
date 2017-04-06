package com.tranzvision.gd.TZLeaguerAccountBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsoprdefnMapper;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzRegUserTMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 申请用户管理中，申请材料列表
 * 
 * @author yuds
 * @since 2015-11-20
 */
@Service("com.tranzvision.gd.TZLeaguerAccountBundle.service.impl.LeaguerStuAppListServiceImpl")
@SuppressWarnings("unchecked")
public class LeaguerStuAppListServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private SqlQuery SqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsoprdefnMapper psoprdefnMapper;
	@Autowired
	private PsTzRegUserTMapper PsTzRegUserTMapper;
	@Autowired
	private TZGDObject tzGdObject;
		
	
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", new ArrayList<Map<String,Object>>());

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		
		try {
		    jacksonUtil.json2Map(strParams);
		    
		    String oprid = jacksonUtil.getString("oprid");		   
		    
		    String strSql = "SELECT A.TZ_APP_INS_ID,A.TZ_APP_FORM_STA,T.TZ_CLASS_ID,(SELECT C.TZ_CLASS_NAME FROM PS_TZ_CLASS_INF_T C WHERE T.TZ_CLASS_ID=C.TZ_CLASS_ID) TZ_CLASS_NAME,T.TZ_BATCH_ID,(SELECT BT.TZ_BATCH_NAME FROM PS_TZ_CLS_BATCH_T BT WHERE BT.TZ_CLASS_ID=T.TZ_CLASS_ID AND T.TZ_BATCH_ID=BT.TZ_BATCH_ID limit 0,1) TZ_BATCH_NAME FROM PS_TZ_APP_INS_T A,PS_TZ_FORM_WRK_T T WHERE A.TZ_APP_INS_ID=T.TZ_APP_INS_ID AND T.OPRID=?";
		    List<Map<String, Object>> list = SqlQuery.queryForList(strSql,new Object[]{oprid});
		    if(list!=null&&list.size()>0){			
			
			for(Object useObj:list){
			    Map<String, Object> mapList = new HashMap<String, Object>();
			    
			    Map<String,Object> result1=(Map<String,Object>) useObj;
        		    String appInsId = result1.get("TZ_APP_INS_ID")==null ? "" : String.valueOf(result1.get("TZ_APP_INS_ID"));
        		    String appSubStatus = result1.get("TZ_APP_FORM_STA")==null ? "" : String.valueOf(result1.get("TZ_APP_FORM_STA"));
        		    String strClassId = result1.get("TZ_CLASS_ID")==null ? "" : String.valueOf(result1.get("TZ_CLASS_ID"));
        		    String strClassName = result1.get("TZ_CLASS_NAME")==null ? "" : String.valueOf(result1.get("TZ_CLASS_NAME"));
        		    String strBatchId = result1.get("TZ_BATCH_ID")==null ? "" : String.valueOf(result1.get("TZ_BATCH_ID"));
        		    String strBatchName = result1.get("TZ_BATCH_NAME")==null ? "" : String.valueOf(result1.get("TZ_BATCH_NAME"));
        		    mapList.put("appInsId", appInsId);
        		    mapList.put("appClassId", strClassId);
        		    mapList.put("appBatchId", strBatchId);
        		    mapList.put("appInfo", strClassName);
        		    mapList.put("appSubStatus", appSubStatus);
        		    mapList.put("batchName", strBatchName);
        		    mapList.put("oprID", oprid);        		    
        		    
        		    listData.add(mapList);
			}
			mapRet.replace("total", list.size());
			mapRet.replace("root", listData);
		    }			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jacksonUtil.Map2json(mapRet);
	}
		
}
