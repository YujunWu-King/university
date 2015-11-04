/**
 * 
 */
package com.tranzvision.gd.util.sql;

/**
 * @author LiGang
 * 2015/10/30
 */
public class SqlParams
{
	private Object[] value;
	
	public SqlParams(Object...args)
	{
		value = args;
	}
	
	public Object[] getValue()
	{
		return value;
	}
}
