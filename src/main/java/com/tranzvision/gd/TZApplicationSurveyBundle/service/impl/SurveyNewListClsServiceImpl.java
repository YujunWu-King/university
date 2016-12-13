package com.tranzvision.gd.TZApplicationSurveyBundle.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * @author caoy
 * @version 创建时间：2016年8月3日 下午2:45:45 类说明  新增问卷模板（复制现有模板）
 */
@Service("com.tranzvision.gd.TZApplicationSurveyBundle.service.impl.SurveyNewListClsServiceImpl")
public class SurveyNewListClsServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private SqlQuery jdbcTemplate;

	/* 获取报名表模板信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		System.out.println("==SurveyNewListClsServiceImpl类==tzQuery执行");
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		String strPreDefineTpl = "[";
		try {
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String sql = "select TZ_APP_TPL_ID,TZ_APP_TPL_MC from PS_TZ_DC_DY_T where TZ_JG_ID=?";
			List<Map<String, Object>> resultlist = jdbcTemplate.queryForList(sql, new Object[] { orgId });
			Map<String, Object> result = null;
			String TZ_APP_TPL_MC = "";
			String TZ_APP_TPL_ID = "";
			Map<String, Object> mapRet = null;
			for (Object obj : resultlist) {
				result = (Map<String, Object>) obj;
				TZ_APP_TPL_ID = result.get("TZ_APP_TPL_ID") == null ? "" : String.valueOf(result.get("TZ_APP_TPL_ID"));
				TZ_APP_TPL_MC = result.get("TZ_APP_TPL_MC") == null ? "" : String.valueOf(result.get("TZ_APP_TPL_MC"));
				mapRet = new HashMap<String, Object>();
				mapRet.put("tplid", TZ_APP_TPL_ID);
				mapRet.put("tplname", TZ_APP_TPL_MC);
				strPreDefineTpl = strPreDefineTpl + jacksonUtil.Map2json(mapRet) + ",";
			}
			strPreDefineTpl = strPreDefineTpl.substring(0, strPreDefineTpl.length() - 1) + "]";
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strPreDefineTpl;
	}

}
