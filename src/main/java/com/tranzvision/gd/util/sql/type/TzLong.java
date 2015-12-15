/**
 * 
 */
package com.tranzvision.gd.util.sql.type;

import com.tranzvision.gd.util.sql.type.TzSQLData;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * @author LiGang
 * 2015/10/30
 */
public class TzLong implements TzSQLData
{
	private Long value;
	
	public TzLong()
	{
		value = Long.valueOf(0);
	}
	
	public TzLong(long v)
	{
		value = v;
	}
	
	public void setValue(SqlRowSet rs,int colIndex)
	{
		value = rs.getLong(colIndex);
	}
	
	public void setValue(long v)
	{
		value = v;
	}
	
	public void clearValue()
	{
		value = Long.valueOf(0);
	}
	
	public long getValue()
	{
		return value;
	}
}
