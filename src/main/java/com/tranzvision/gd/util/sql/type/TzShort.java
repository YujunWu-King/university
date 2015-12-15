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
public class TzShort implements TzSQLData
{
	private Short value;
	
	public TzShort()
	{
		value = 0;
	}
	
	public TzShort(short v)
	{
		value = v;
	}
	
	public void setValue(SqlRowSet rs,int colIndex)
	{
		value = rs.getShort(colIndex);
	}
	
	public void setValue(short v)
	{
		value = v;
	}
	
	public void clearValue()
	{
		value = 0;
	}
	
	public short getValue()
	{
		return value;
	}
}
