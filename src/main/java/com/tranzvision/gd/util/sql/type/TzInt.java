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
public class TzInt implements TzSQLData
{
	private Integer value;
	
	public TzInt()
	{
		value = 0;
	}
	
	public TzInt(int v)
	{
		value = v;
	}
	
	public void setValue(SqlRowSet rs,int colIndex)
	{
		value = rs.getInt(colIndex);
	}
	
	public void setValue(int v)
	{
		value = v;
	}
	
	public void clearValue()
	{
		value = 0;
	}
	
	public int getValue()
	{
		return value;
	}
}
