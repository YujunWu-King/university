/**
 * 
 */
package com.tranzvision.gd.util.sql.type;

import java.sql.Timestamp;
import com.tranzvision.gd.util.sql.type.TzSQLData;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * @author LiGang
 * 2015/10/30
 */
public class TzTimestamp implements TzSQLData
{
	private Timestamp value;
	
	public TzTimestamp()
	{
		value = null;
	}
	
	public TzTimestamp(Timestamp v)
	{
		value = v;
	}
	
	public void setValue(SqlRowSet rs,int colIndex)
	{
		value = rs.getTimestamp(colIndex);
	}
	
	public void setValue(Timestamp v)
	{
		value = v;
	}
	
	public void clearValue()
	{
		value = null;
	}
	
	public Timestamp getValue()
	{
		return value;
	}
}
