/**
 * 
 */
package com.tranzvision.gd.util.base;

/**
 * @author LiGang
 * 2015/11/3
 */
public class TzSystemException extends Exception
{
	public static final long serialVersionUID=1L;
	
	public TzSystemException()
	{
		super();
	}
	
	public TzSystemException(String message)
	{
		super(message);
	}
	
	public TzSystemException(String message, Throwable cause)
	{
		super(message,cause);
	}
	
	public TzSystemException(Throwable cause)
	{
		super(cause);
	}
}
