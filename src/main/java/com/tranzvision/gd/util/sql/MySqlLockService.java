/**
 * 
 */
package com.tranzvision.gd.util.sql;

import org.springframework.stereotype.Service;

/**
 * MySQL锁表操作类
 * 
 * @author SHIHUA
 * @since 2016-02-16
 */
@Service
public class MySqlLockService {

	public void lockRow(SqlQuery sqlQuery, String tblName) {

		String fldName = "LOCK_ROWNUM";

		// 事务开始
		sqlQuery.execute("begin");

		// 锁定指定行记录
		String sql = "select 'Y' from PS_TZ_SEQNUM_T where TZ_TABLE_NAME = ? and TZ_COL_NAME = ? for update";
		String recExists = sqlQuery.queryForObject(sql, new Object[] { tblName, fldName }, "String");

		if (!"Y".equals(recExists)) {
			sql = "insert into PS_TZ_SEQNUM_T (TZ_TABLE_NAME, TZ_COL_NAME, TZ_SEQNUM) values (?,?,0)";
			sqlQuery.update(sql, new Object[] { tblName, fldName });
		}

		sql = "update PS_TZ_SEQNUM_T set TZ_SEQNUM=TZ_SEQNUM+1 where TZ_TABLE_NAME = ? and TZ_COL_NAME = ?";
		sqlQuery.update(sql, new Object[] { tblName, fldName });

	}

	public void unlockRow(SqlQuery sqlQuery) {

		sqlQuery.execute("commit");

	}

}
