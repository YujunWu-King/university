/**
 * 
 */
package com.tranzvision.gd.util.sql.type;

import java.math.BigDecimal;
import com.tranzvision.gd.util.sql.type.TzSQLData;
import org.springframework.jdbc.support.rowset.SqlRowSet;


/**
 * @author LiGang
 * 2015/10/30
 */
public class TzBigDecimal implements TzSQLData
{
	private BigDecimal value;
	
	public TzBigDecimal()
	{
		value = null;
	}
	
	public TzBigDecimal(BigDecimal v)
	{
		value = v;
	}
	
	public void setValue(SqlRowSet rs,int colIndex)
	{
		value = rs.getBigDecimal(colIndex);
	}
	
	public void setValue(BigDecimal v)
	{
		value = v;
	}
	
	public void clearValue()
	{
		value = null;
	}
	
	public BigDecimal getValue()
	{
		return value;
	}
}
