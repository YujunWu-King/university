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
public class TzDouble implements TzSQLData
{
	private Double value;
	
	public TzDouble()
	{
		value = Double.valueOf(0);
	}
	
	public TzDouble(double v)
	{
		value = v;
	}
	
	public void setValue(SqlRowSet rs,int colIndex)
	{
		value = rs.getDouble(colIndex);
	}
	
	public void setValue(double v)
	{
		value = v;
	}
	
	public void clearValue()
	{
		value = Double.valueOf(0);
	}
	
	public double getValue()
	{
		return value;
	}
}
