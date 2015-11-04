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
public class TzString implements TzSQLData
{
	private String value;
	
	public TzString()
	{
		value = "";
	}
	
	public TzString(String v)
	{
		value = v;
	}
	
	public void setValue(SqlRowSet rs,int colIndex)
	{
		value = rs.getString(colIndex);
	}
	
	public void setValue(String v)
	{
		value = v;
	}
	
	public void clearValue()
	{
		value = "";
	}
	
	public String getValue()
	{
		return value;
	}
}
