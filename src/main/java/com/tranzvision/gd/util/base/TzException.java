/**
 * 
 */
package com.tranzvision.gd.util.base;

/**
 * @author LiGang
 * 2015/11/3
 */
public class TzException extends Exception
{
	public static final long serialVersionUID=1L;
	
	public TzException()
	{
		super();
	}
	
	public TzException(String message)
	{
		super(message);
	}
	
	public TzException(String message, Throwable cause)
	{
		super(message,cause);
	}
	
	public TzException(Throwable cause)
	{
		super(cause);
	}
}
