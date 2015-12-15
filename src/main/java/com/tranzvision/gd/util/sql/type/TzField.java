/**
 * 
 */
package com.tranzvision.gd.util.sql.type;

/**
 * @author LiGang
 * 2015/11/30
 */
public class TzField
{
	public String name;
	public Object value;
	
	public TzField(String fieldName,Object fieldValue)
	{
		name = fieldName;
		value = fieldValue;
	}
}
