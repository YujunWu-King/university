package com.tranzvision.gd.TZBatchProcessBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 20170926，为测试进程添加
 * @author yuds
 *
 */
public class TzGdTestLoopEngineCls extends BaseEngine {
	@Override
	public void OnExecute() throws Exception {
		System.out.println("------------------------测试进程开始---------------------------");
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		SqlQuery jdbcTemplate = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");

		String runControlId = this.getRunControlID();
		try {
			int max = 9999;
			int min = 1000;
			Random random = new Random();

			int s = random.nextInt(max) % (max - min + 1) + min;

			// 向表中插入记录
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmssSSS");
			String xh = sdf.format(date) + "" + s;
			String strInsertSQL = "INSERT INTO PS_TZ_TEST_ENG_TBL VALUES('" + xh + "',now(),'Y','" + runControlId
					+ "')";
			jdbcTemplate.update(strInsertSQL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("------------------------测试进程结束---------------------------");
	}
}
