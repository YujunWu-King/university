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
public class TzByte implements TzSQLData
{
	private Byte value;
	
	public TzByte()
	{
		value = 0;
	}
	
	public TzByte(byte v)
	{
		value = v;
	}
	
	public void setValue(SqlRowSet rs,int colIndex)
	{
		value = rs.getByte(colIndex);
	}
	
	public void setValue(byte v)
	{
		value = v;
	}
	
	public void clearValue()
	{
		value = 0;
	}
	
	public byte getValue()
	{
		return value;
	}
}
