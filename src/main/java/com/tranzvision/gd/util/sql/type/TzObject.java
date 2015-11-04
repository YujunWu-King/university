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
public class TzObject implements TzSQLData
{
	private Object value;
	
	public TzObject()
	{
		value = null;
	}
	
	public TzObject(Object v)
	{
		value = v;
	}
	
	public void setValue(SqlRowSet rs,int colIndex)
	{
		value = rs.getObject(colIndex);
	}
	
	public void setValue(Object object)
	{
		value = object;
	}
	
	public void clearValue()
	{
		value = null;
	}
	
	public Object getValue()
	{
		return value;
	}
}
