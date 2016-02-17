package com.tranzvision.gd.util.sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetSeqNum {
	@Autowired
	private SqlQuery jdbcTemplate;

	public int getSeqNum(String tableName, String colName) {

		int index = 0;

		// 开启事务
		jdbcTemplate.execute("begin");

		// 锁定指定行记录
		String sql = "";
		try {
			sql = "select TZ_SEQNUM from PS_TZ_SEQNUM_T where TZ_TABLE_NAME = ? and TZ_COL_NAME = ? for update";
			index = jdbcTemplate.queryForObject(sql, new Object[] { tableName, colName }, "int");
		} catch (Exception e) {
			index = 0;
		}

		if (index > 0) {
			sql = "update PS_TZ_SEQNUM_T set TZ_SEQNUM=TZ_SEQNUM+1 where TZ_TABLE_NAME = ? and TZ_COL_NAME = ?";
			jdbcTemplate.update(sql, new Object[] { tableName, colName });
		} else {
			sql = "insert into PS_TZ_SEQNUM_T (TZ_TABLE_NAME, TZ_COL_NAME, TZ_SEQNUM) values (?,?,1)";
			jdbcTemplate.update(sql, new Object[] { tableName, colName });
		}

		index = index + 1;

		// 提交事务
		jdbcTemplate.execute("commit");

		return index;
	}

	/**** 自动编号 *****/
	public int getSeqNumOracle(String tableName, String colName) {
		int index = 0;
		try {
			String lockSQL = "update PS_TZ_SEQNUM_T set tz_seqnum = tz_seqnum + 1 where tz_table_name = ? and tz_col_name = ?";
			jdbcTemplate.update(lockSQL, new Object[] { tableName, colName });

			String selectSql = "select tz_seqnum from PS_TZ_SEQNUM_T where tz_table_name = ? and tz_col_name = ?";
			try {
				index = jdbcTemplate.queryForObject(selectSql, new Object[] { tableName, colName }, "Integer");
			} catch (Exception e) {
				index = 0;
			}

			if (index == 0) {
				String insertSQL = "insert into PS_TZ_SEQNUM_T (tz_table_name, tz_col_name, tz_seqnum) values (?, ?, 1)";
				int i = jdbcTemplate.update(insertSQL, new Object[] { tableName, colName });
				if (i > 0) {
					index = 1;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return index;
	}
}
