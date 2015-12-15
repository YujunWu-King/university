/**
 * 
 */
package com.tranzvision.gd.util.sql.type;

import java.util.Date;
import com.tranzvision.gd.util.sql.type.TzSQLData;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * @author LiGang
 * 2015/10/30
 */
public class TzDate implements TzSQLData
{
	private Date value;
	
	public TzDate()
	{
		value = null;
	}
	
	public TzDate(Date v)
	{
		value = v;
	}
	
	public void setValue(SqlRowSet rs,int colIndex)
	{
		value = rs.getDate(colIndex);
	}
	
	public void setValue(Date v)
	{
		value = v;
	}
	
	public void clearValue()
	{
		value = null;
	}
	
	public Date getValue()
	{
		return value;
	}
}
