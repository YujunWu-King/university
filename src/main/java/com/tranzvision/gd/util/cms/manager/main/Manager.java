package com.tranzvision.gd.util.cms.manager.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.cms.entity.main.CmsTemplate;

public class Manager {

	protected String clob2String(Clob clob) throws SQLException, IOException {
		if (clob == null)
			return null;
		Reader is = clob.getCharacterStream();
		BufferedReader br = new BufferedReader(is);
		String s = br.readLine();
		StringBuffer sb = new StringBuffer();
		while (s != null) {
			sb.append(s);
			s = br.readLine();
		}
		return sb.toString();
	}

	public CmsTemplate findTplById(String tmpid) {

		String sql = "select A.TZ_SITEI_ID,A.TZ_TEMP_ID,A.TZ_TEMP_STATE,"
				+ " A.TZ_TEMP_NAME,A.TZ_TEMP_TYPE,A.TZ_TEMP_PCCODE,A.TZ_TEMP_MSCODE " + " from PS_TZ_SITEI_TEMP_T A"
				+ " where A.TZ_TEMP_ID = ?";
		Object[] obj = new Object[] { tmpid };
		return this.findTpl(sql, obj);
	}

	protected CmsTemplate findTpl(String sql, Object[] obj) {
		CmsTemplate tpl = null;
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			Map<String, Object> map = jdbcTemplate.queryForMap(sql, obj);
			if (map != null) {
				tpl = new CmsTemplate();
				tpl.setSiteId((String) map.get("TZ_SITEI_ID"));
				tpl.setId((String) map.get("TZ_TEMP_ID"));
				tpl.setName((String) map.get("TZ_TEMP_NAME"));
				tpl.setState((String) map.get("TZ_TEMP_STATE"));
				tpl.setType((String) map.get("TZ_TEMP_TYPE"));
				tpl.setPcContent((String) map.get("TZ_TEMP_PCCODE"));
				tpl.setMsContent((String) map.get("TZ_TEMP_MSCODE"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tpl;
	}

}
