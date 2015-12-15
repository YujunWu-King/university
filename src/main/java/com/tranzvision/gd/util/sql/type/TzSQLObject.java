/**
 * 
 */
package com.tranzvision.gd.util.sql.type;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.tranzvision.gd.util.sql.type.TzRecord;
import com.tranzvision.gd.util.sql.type.TzSQLData;
import com.tranzvision.gd.util.base.TzSystemException;

/**
 * @author LiGang
 * 2015/11/03
 */
public class TzSQLObject
{
	private SqlRowSet value;
	private boolean isFirstRow;
	private String sqlText;
	private int colCount;
	
	public TzSQLObject()
	{
		value = null;
		isFirstRow = false;
		sqlText = "";
		colCount = 0;
	}
	
	public void setValue(SqlRowSet rs,String sqlText)
	{
		value = rs;
		isFirstRow = false;
		this.sqlText = sqlText;
		colCount = rs.getMetaData().getColumnCount();
	}
	
	/**
	 * 参数说明：
	 * args			TzSQLData类型的数组，用户指定接收SQL执行结果的变量数组，注意，这个变量数组可以采用个数不确定的参数列表的形式，例如p1,p2,p3...pn
	 * 返回结果：
	 * 如果成功获取查询结果，则返回true，否则返回false
	 */
	public boolean fetch(TzSQLData...args) throws TzSystemException
	{
		boolean ret = false;
		boolean moveCusorOk = false;
		SqlRowSet rs = value;
		
		if(isFirstRow == false)
		{
			//如果接收结果的参数为空，则直接抛出异常
			if(colCount > 0 && args == null)
			{
				throw new TzSystemException("error: there is no argument to receive the resultset.\nSQL:" + sqlText);
			}
			
			//如果结果集的列数与接收结果集的参数个数不一致，则抛出异常
			if(colCount > 0 && colCount > args.length)
			{
				throw new TzSystemException("error: there are not enough arguments to receive the resultset.\nSQL:" + sqlText);
			}
			else if(colCount > 0 && colCount < args.length)
			{
				throw new TzSystemException("error: there are too many arguments to receive the resultset.\nSQL:" + sqlText);
			}
			
			isFirstRow = rs.first();
			moveCusorOk = true;
		}
		else
		{
			moveCusorOk = rs.next();
		}
		
		if(moveCusorOk == true)
		{
			//获取结果集
			for(int i=0;i<colCount;i++)
			{
				args[i].setValue(rs, i + 1);
			}
			
			ret = true;
		}
		
		return ret;
	}
	
	/**
	 * 参数说明：
	 * rec			TzRecord类型的数组，用于以记录的形式返回查询结果的参数
	 * 返回结果：
	 * 如果成功获取行记录，则返回true，否则返回false
	 */
	public boolean fetch(TzRecord rec) throws TzSystemException
	{
		boolean ret = false;
		boolean moveCusorOk = false;
		SqlRowSet rs = value;
		
		if(isFirstRow == false)
		{
			//如果接收结果的参数为空，则直接抛出异常
			if(rec == null)
			{
				throw new TzSystemException("error: there is no record object to receive the resultset.\nSQL:" + sqlText);
			}
			
			isFirstRow = rs.first();
			moveCusorOk = isFirstRow;
		}
		else
		{
			moveCusorOk = rs.next();
		}
		
		if(moveCusorOk == true)
		{
			//获取结果集
			rec.setColList(rs);
			
			ret = true;
		}
		
		return ret;
	}
}
