package com.tranzvision.gd.TZNegativeListInfeBundle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author tzhjl
 * @since 2017-2-09 标签统一接口
 */
public class TzNegativeListCominServiceImpl extends FrameworkImpl {
	@Autowired
	private ApplicationContext ctx;
	@Autowired
	private SqlQuery SqlQuery;

	public String tzGetHtmlContent(String comParams) {
		try {
			JacksonUtil jsonUtil = new JacksonUtil();
			String labelId = "";
			String classId = "";
			String batchId = "";

			jsonUtil.json2Map(comParams);
			if (jsonUtil.containsKey("labelId")) {
				labelId = jsonUtil.getString("labelId");

			}
			String sql = "SELECT TZ_BIAOQZ_JAVA FROM PS_TZ_BIAOQZ_BQ_T WHERE TZ_BIAOQ_ID=?";
			String strAppClass = SqlQuery.queryForObject(sql, new Object[] { labelId }, "String");

			TzNegativeListBundleServiceImpl obj = (TzNegativeListBundleServiceImpl) ctx.getBean(strAppClass);

			Boolean strReturn = obj.makeNegativeList(classId, batchId, labelId);

			return comParams;

		} catch (Exception e) {
			// TODO: handle exception
		}
		return comParams;

	}

}
