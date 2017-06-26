package com.tranzvision.gd.ztest.service.impl;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import java.sql.Connection;
import java.sql.SQLException;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;

public class TestThreadServiceImpl extends Thread{
	public static final Object obj = new Object();
	
	@Override
	public void run() {
		String tblName = "TEST_TABLE";
		String fldName = "LOCK_ROWNUM";
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		JdbcTemplate sqlQuery = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
		
	//	try {
			//Connection con=sqlQuery.getDataSource().getConnection();
//			DataSource dataSource = sqlQuery.getDataSource();
//			Connection con = DataSourceUtils.getConnection(dataSource);
//			System.out.println("1============>"+con.getAutoCommit());
//			con.setAutoCommit(false);
//			System.out.println("2============>"+con.getAutoCommit());
			// 事务开始
			//sqlQuery.execute("begin");
//			synchronized (obj) {
				// 锁定指定行记录
				String sql = "select 'Y' from PS_TZ_SEQNUM_T where TZ_TABLE_NAME = ? and TZ_COL_NAME = ? for update";
				String recExists = "";
				try{
					recExists = sqlQuery.queryForObject(sql, new Object[] { tblName, fldName }, String.class);
				}catch(DataAccessException e){
					
				}
				

				if (!"Y".equals(recExists)) {
					sql = "insert into PS_TZ_SEQNUM_T (TZ_TABLE_NAME, TZ_COL_NAME, TZ_SEQNUM) values (?,?,0)";
					sqlQuery.update(sql, new Object[] { tblName, fldName });
				}

				sql = "update PS_TZ_SEQNUM_T set TZ_SEQNUM=TZ_SEQNUM+1 where TZ_TABLE_NAME = ? and TZ_COL_NAME = ?";
				sqlQuery.update(sql, new Object[] { tblName, fldName });
				
//				try {
					//Thread.sleep(2000);
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
					System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
			
			
			//con.commit();//手动提交
		    //con.setAutoCommit(true);//还原
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		
	}
}
