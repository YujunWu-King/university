/**
 * 
 */
package com.tranzvision.gd.util.sql.type;

import java.sql.Time;
import com.tranzvision.gd.util.sql.type.TzSQLData;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * @author LiGang
 * 2015/10/30
 */
public class TzTime implements TzSQLData
{
	private Time value;
	
	public TzTime()
	{
		value = null;
	}
	
	public TzTime(Time v)
	{
		value = v;
	}
	
	public void setValue(SqlRowSet rs,int colIndex)
	{
		value = rs.getTime(colIndex);
	}
	
	public void setValue(Time v)
	{
		value = v;
	}
	
	public void clearValue()
	{
		value = null;
	}
	
	public Time getValue()
	{
		return value;
	}
}
