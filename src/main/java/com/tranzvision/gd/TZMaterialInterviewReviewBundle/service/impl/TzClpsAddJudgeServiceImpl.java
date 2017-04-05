package com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.MapDataSourceLookup;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationProcessBundle.service.impl.proDefnServiceImpl;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 材料评审设置评审规则-批量添加评委
 * @author LuYan
 * 2017-3-31
 *
 */
@Service("com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl.TzClpsAddJudgeServiceImpl")
public class TzClpsAddJudgeServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TZGDObject tzSQLObject;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	
	/*评委列表*/
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
			
			//当前机构
			String currentOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			//材料评审类型编号
			String clpsPwLxId = getHardCodePoint.getHardCodePointVal("TZ_CLPS_PWLX_ID");
			
			String sql = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialAllJudge");
			
			jacksonUtil.json2Map(strParams);
			
			if(jacksonUtil.containsKey("judgeIdSrh") && jacksonUtil.containsKey("judgeNameSrh")) {
				String judgeIdSrh = (String) jacksonUtil.getString("judgeIdSrh");
				String judgeNameSrh = (String) jacksonUtil.getString("judgeNameSrh");
				
				sql+= "AND B.TZ_DLZH_ID LIKE '%" + judgeIdSrh + "%' AND B.TZ_REALNAME LIKE '%" + judgeNameSrh + "%'";
			} 

			List<Map<String, Object>> listClpw = sqlQuery.queryForList(sql,new Object[] {currentOrgId,clpsPwLxId});
			
			Integer count = 0;
			
			for(Map<String, Object> mapClpw : listClpw) {
				
				count++;
				
				String judgeOprid = (String) mapClpw.get("OPRID");
				String judgeId = (String) mapClpw.get("TZ_DLZH_ID");
				String judgeName = (String) mapClpw.get("TZ_REALNAME");
				String judgeMobile = (String) mapClpw.get("TZ_MOBILE");
				String judgeEmail = (String) mapClpw.get("TZ_EMAIL");
				
				Map<String, Object> mapList = new HashMap<String,Object>();
				mapList.put("judgeOprid", judgeOprid);
				mapList.put("judgeId", judgeId);
				mapList.put("judgeName", judgeName);
				mapList.put("judgeMobile", judgeMobile);
				mapList.put("judgeEmail", judgeEmail);
				mapList.put("judgeGroup", "");
				
				listData.add(mapList);
			}
			
			mapRet.replace("total", count);
			mapRet.replace("root", listData);
				
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		strRet = jacksonUtil.Map2json(mapRet);
		
		return strRet;
	}
}
