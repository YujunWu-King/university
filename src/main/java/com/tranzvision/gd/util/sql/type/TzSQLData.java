/**
 * 
 */
package com.tranzvision.gd.util.sql.type;

import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * @author LiGang
 * 2015/10/30
 */
public interface TzSQLData
{
	public abstract void setValue(SqlRowSet rs,int colIndex);
	
	public abstract void clearValue();
}
