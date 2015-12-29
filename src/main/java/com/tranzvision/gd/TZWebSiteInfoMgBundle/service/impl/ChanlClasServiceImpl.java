package com.tranzvision.gd.TZWebSiteInfoMgBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 内容管理栏目设置；原：TZ_GD_CONTENT_PKG:TZ_GD_CHANL_CLS
 * 
 * @author tang
 * @since 2015-11-23
 */

@Service("com.tranzvision.gd.TZWebSiteInfoMgBundle.service.impl.ChanlClasServiceImpl")
public class ChanlClasServiceImpl extends FrameworkImpl {
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private SqlQuery jdbcTemplate;
	
	/* 查询表单信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData","");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			//获取登录的机构;
			String strJgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			//获取机构对应的站点；
			String siteSQL = " SELECT TZ_SITEI_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ENABLE='Y' and TZ_JG_ID=?";
			String siteId = jdbcTemplate.queryForObject(siteSQL, new Object[]{strJgid},"String");
			
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("templateId")) {
				// 邮件服务器参数id;
				String templateId = jacksonUtil.getString("templateId");
				if(templateId != null && !"".equals(templateId) && siteId != null && !"".equals(siteId)){
					String sql = "SELECT TZ_COLU_NAME,TZ_COLU_TYPE,TZ_TEMP_ID,TZ_CONT_TYPE,TZ_CONT_TEMP FROM PS_TZ_SITEI_COLU_T WHERE TZ_SITEI_ID=? AND TZ_COLU_ID=? and TZ_COLU_STATE='Y'";
					Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[]{siteId,templateId});
					Map<String, Object> jsonMap = new HashMap<>();
					jsonMap.put("lm_id", templateId);
					jsonMap.put("lm_name", map.get("TZ_COLU_NAME"));
					jsonMap.put("siteId", siteId);
					
					returnJsonMap.replace("formData", jsonMap);
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "参数不正确！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	
	/* 修改栏目设置 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				
				if(jacksonUtil.containsKey("siteId") && jacksonUtil.containsKey("lm_id") && jacksonUtil.containsKey("lm_name")){
					String siteId = jacksonUtil.getString("siteId");
					String lm_id = jacksonUtil.getString("lm_id");
					String lm_name = jacksonUtil.getString("lm_name");
					String updateSQL = "UPDATE PS_TZ_SITEI_COLU_T SET TZ_COLU_NAME=? WHERE TZ_SITEI_ID=? AND TZ_COLU_ID=?";
					int excuteNum = jdbcTemplate.update(updateSQL,new Object[]{lm_name,siteId,lm_id});
					if(excuteNum <=0){
						errMsg[0] = "1";
						errMsg[1] = "数据更新失败";
					}
				}else{
					errMsg[0] = "1";
					errMsg[1] = "参数错误";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}
	
	/* 删除栏目*/
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
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				
				if(jacksonUtil.containsKey("siteId") && jacksonUtil.containsKey("lm_id")){
					String siteId = jacksonUtil.getString("siteId");
					String lm_id = jacksonUtil.getString("lm_id");
					
					// 如果该栏目已经关联了一个有效菜单，则不允许删除;
					String isGlSQL = "SELECT COUNT(1) from PS_TZ_SITEI_MENU_T where TZ_MENU_COLUMN=? and TZ_MENU_STATE='Y'";
					int count = jdbcTemplate.queryForObject(isGlSQL,new Object[]{lm_id}, "Integer");
					if(count > 0){
						errMsg[0] = "1";
						errMsg[1] = "该栏目已被菜单关联，不能删除！";
					}else{
						String updateSQL = "UPDATE PS_TZ_SITEI_COLU_T SET TZ_COLU_STATE='N' WHERE TZ_SITEI_ID=? AND TZ_COLU_ID=?";
						int excuteNum = jdbcTemplate.update(updateSQL,new Object[]{siteId,lm_id});
						if(excuteNum <=0){
							errMsg[0] = "1";
							errMsg[1] = "数据更新失败";
						}
					}
				}else{
					errMsg[0] = "1";
					errMsg[1] = "参数错误";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
}
