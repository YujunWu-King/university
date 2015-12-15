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
public class TzFloat implements TzSQLData
{
	private Float value;
	
	public TzFloat()
	{
		value = Float.valueOf(0);
	}
	
	public TzFloat(float v)
	{
		value = v;
	}
	
	public void setValue(SqlRowSet rs,int colIndex)
	{
		value = rs.getFloat(colIndex);
	}
	
	public void setValue(float v)
	{
		value = v;
	}
	
	public void clearValue()
	{
		value = Float.valueOf(0);
	}
	
	public float getValue()
	{
		return value;
	}
}
