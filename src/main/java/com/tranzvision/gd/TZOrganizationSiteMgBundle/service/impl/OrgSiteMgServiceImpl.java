package com.tranzvision.gd.TZOrganizationSiteMgBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiDefnTMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/*
 * 站点模板设置， 原PS类：TZ_GD_ZD_PKG:TZ_GD_ZDGL_CLS
 * 描述：高端产品-机构站点list
 * @author tang
 */

@Service("com.tranzvision.gd.TZOrganizationSiteMgBundle.service.impl.OrgSiteMgServiceImpl")
public class OrgSiteMgServiceImpl extends FrameworkImpl {
	
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzSiteiDefnTMapper psTzSiteiDefnTMapper;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	/* 查询许可权列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("total", 0);
		ArrayList<Map<String, Object>> arraylist = new ArrayList<Map<String, Object>>();
		returnJsonMap.put("root", arraylist);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String totalSQL="";
			int total=0;
			if ("ADMIN".equals(orgId.toUpperCase())){
				 totalSQL = "SELECT COUNT(1) FROM PS_TZ_SITEI_DEFN_T where TZ_SITEI_TYPE='C'";
				 total = jdbcTemplate.queryForObject(totalSQL,"Integer");
			}else{
				 totalSQL = "SELECT COUNT(1) FROM PS_TZ_SITEI_DEFN_T where TZ_SITEI_TYPE='C'  and TZ_JG_ID = ?";
				 total = jdbcTemplate.queryForObject(totalSQL,new Object[] { orgId }, "Integer");
			}
			String sql = "";
			List<Map<String, Object>> list = null;
			if(numLimit > 0){
				if("ADMIN".equals(orgId.toUpperCase())){
					sql = "SELECT TZ_SITEI_ID,TZ_SITEI_NAME,TZ_SITEI_DESCR FROM PS_TZ_SITEI_DEFN_T where TZ_SITEI_TYPE='C' ORDER BY TZ_SITEI_ID ASC LIMIT ?,?";
					list = jdbcTemplate.queryForList(sql, new Object[]{numStart,numLimit});
				}else{
					sql = "SELECT TZ_SITEI_ID,TZ_SITEI_NAME,TZ_SITEI_DESCR FROM PS_TZ_SITEI_DEFN_T where TZ_SITEI_TYPE='C' and TZ_JG_ID = ? ORDER BY TZ_SITEI_ID ASC LIMIT ?,?";
					list = jdbcTemplate.queryForList(sql, new Object[]{orgId,numStart,numLimit});
				}
			}else{
				if("ADMIN".equals(orgId.toUpperCase())){
					sql = "SELECT TZ_SITEI_ID,TZ_SITEI_NAME,TZ_SITEI_DESCR FROM PS_TZ_SITEI_DEFN_T where TZ_SITEI_TYPE='C' ORDER BY TZ_SITEI_ID ASC";
					list = jdbcTemplate.queryForList(sql);
				}else{
				sql = "SELECT TZ_SITEI_ID,TZ_SITEI_NAME,TZ_SITEI_DESCR FROM PS_TZ_SITEI_DEFN_T where TZ_SITEI_TYPE='C' and TZ_JG_ID = ? ORDER BY TZ_SITEI_ID ASC";
				list = jdbcTemplate.queryForList(sql,new Object[] { orgId } );
				}
			}
			if(list != null){
				
				for(int i = 0; i<list.size();i++){
					Map<String, Object> jsonMap = new HashMap<String, Object>();
					jsonMap.put("siteId", list.get(i).get("TZ_SITEI_ID"));
					jsonMap.put("sitetemplateName", list.get(i).get("TZ_SITEI_NAME"));
					jsonMap.put("explanation", list.get(i).get("TZ_SITEI_DESCR"));
					arraylist.add(jsonMap);
				}
				returnJsonMap.replace("total", total);
				returnJsonMap.replace("root", arraylist);
				strRet = jacksonUtil.Map2json(returnJsonMap);
			}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	/* 删除站点模板 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 提交信息
				String strComInfo = actData[num];
				jacksonUtil.json2Map(strComInfo);
				// 站点ID;
				String siteId = jacksonUtil.getString("siteId");
				if (siteId != null && !"".equals(siteId)) {
					//删除机构站点基本信息;
					psTzSiteiDefnTMapper.deleteByPrimaryKey(siteId);
					
					//删除机构站点模板信息;
					String deleteSQL = "DELETE from PS_TZ_SITEI_TEMP_T where TZ_SITEI_ID=?";
					jdbcTemplate.update(deleteSQL,new Object[]{siteId});
					
					//删除机构站点栏目信息;
					deleteSQL = "DELETE from PS_TZ_SITEI_COLU_T where TZ_SITEI_ID=?";
					jdbcTemplate.update(deleteSQL,new Object[]{siteId});
					
					//删除机构站点区域信息;
					deleteSQL = "DELETE from PS_TZ_SITEI_ATYP_T where TZ_SITEI_ID=?";
					jdbcTemplate.update(deleteSQL,new Object[]{siteId});
					deleteSQL = "DELETE from PS_TZ_SITEI_AREA_T where TZ_SITEI_ID=?";
					jdbcTemplate.update(deleteSQL,new Object[]{siteId});
					
					//删除菜单皮肤图片;
					deleteSQL = "DELETE from PS_TZ_SITEI_MTYP_T where TZ_SITEI_ID=?";
					jdbcTemplate.update(deleteSQL,new Object[]{siteId});
					deleteSQL = "DELETE from PS_TZ_SITEI_CDPF_T where TZ_SITEI_ID=?";
					jdbcTemplate.update(deleteSQL,new Object[]{siteId});
					deleteSQL = "DELETE from PS_TZ_SITEI_MENU_T where TZ_SITEI_ID=?";
					jdbcTemplate.update(deleteSQL,new Object[]{siteId});
					deleteSQL = "DELETE from PS_TZ_SITEI_MNPF_T where TZ_SITEI_ID=?";
					jdbcTemplate.update(deleteSQL,new Object[]{siteId});
					
					
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
}
