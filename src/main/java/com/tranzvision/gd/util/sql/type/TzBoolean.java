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
public class TzBoolean implements TzSQLData
{
	private boolean value;
	
	public TzBoolean()
	{
		value = false;
	}
	
	public TzBoolean(boolean v)
	{
		value = v;
	}
	
	public void setValue(SqlRowSet rs,int colIndex)
	{
		value = rs.getBoolean(colIndex);
	}
	
	public void setValue(boolean v)
	{
		value = v;
	}
	
	public void clearValue()
	{
		value = false;
	}
	
	public boolean getValue()
	{
		return value;
	}
}
