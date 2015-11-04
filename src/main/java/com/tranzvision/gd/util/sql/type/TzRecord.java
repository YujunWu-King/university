/**
 * 
 */
package com.tranzvision.gd.util.sql.type;

import com.tranzvision.gd.util.sql.type.*;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import com.tranzvision.gd.util.base.TzSystemException;

/**
 * @author LiGang
 * 2015/11/02
 */
public class TzRecord
{
	private Map<String,Object> colListMap;
	private Object[] colListArray;
	
	public TzRecord()
	{
		colListMap = new HashMap<String,Object>();
	}
	
	private Object getObject(Object obj)
	{
		Object retObj = null;
		
		String type = obj.getClass().getName();
		
		switch(type)
		{
		case "java.lang.Integer":
			retObj = new TzInt((Integer)obj);
			break;
		case "java.lang.String":
			retObj = new TzString((String)obj);
			break;
		case "java.lang.NString":
			retObj = new TzNString((String)obj);
			break;
		case "java.math.BigDecimal":
			retObj = new TzBigDecimal((BigDecimal)obj);
			break;
		case "java.lang.Boolean":
			retObj = new TzBoolean((Boolean)obj);
			break;
		case "java.lang.Byte":
			retObj = new TzByte((Byte)obj);
			break;
		case "java.sql.Date":
			retObj = new TzDate((Date)obj);
			break;
		case "java.lang.Double":
			retObj = new TzDouble((Double)obj);
			break;
		case "java.lang.Float":
			retObj = new TzFloat((Float)obj);
			break;
		case "java.lang.Long":
			retObj = new TzLong((Long)obj);
			break;
		case "java.lang.Short":
			retObj = new TzShort((Short)obj);
			break;
		case "java.sql.Time":
			retObj = new TzTime((Time)obj);
			break;
		case "java.sql.Timestamp":
			retObj = new TzTimestamp((Timestamp)obj);
			break;
		default:
			retObj = null;
		}
		
		return retObj;
	}
	
	public void setColList(SqlRowSet rs)
	{
		String[] colNames = rs.getMetaData().getColumnNames();
		
		colListMap.clear();
		colListArray = new Object[colNames.length];
		
		for(int i=0;i<colNames.length;i++)
		{
			Object tmpObj = getObject(rs.getObject(i+1));
			
			colListMap.put(colNames[i].toUpperCase(), tmpObj);
			colListArray[i] = tmpObj;
		}
	}
	
	public int getColumnCount()
	{
		return colListArray.length;
	}
	
	public Object getColumn(String colName) throws TzSystemException
	{
		Object ret = colListMap.get(colName.toUpperCase());
		
		if(ret == null)
		{
			throw new TzSystemException("error: can't find the column \"" + colName + "\"the resultset.");
		}
		
		return ret;
	}
	
	public Object getColumn(int index)
	{
		return colListArray[index];
	}
	
	public TzInt getTzint(String colName) throws TzSystemException
	{
		TzInt ret = (TzInt)colListMap.get(colName.toUpperCase());
		
		if(ret == null)
		{
			throw new TzSystemException("error: can't find the column \"" + colName + "\"the resultset.");
		}
		
		return ret;
	}
	
	public TzInt getTzint(int index)
	{
		return (TzInt)colListArray[index];
	}
	
	public TzString getTzString(String colName) throws TzSystemException
	{
		TzString ret = (TzString)colListMap.get(colName.toUpperCase());
		
		if(ret == null)
		{
			throw new TzSystemException("error: can't find the column \"" + colName + "\"the resultset.");
		}
		
		return ret;
	}
	
	public TzString getTzString(int index)
	{
		return (TzString)colListArray[index];
	}
	
	public TzNString getTzNString(String colName) throws TzSystemException
	{
		TzNString ret = (TzNString)colListMap.get(colName.toUpperCase());
		
		if(ret == null)
		{
			throw new TzSystemException("error: can't find the column \"" + colName + "\"the resultset.");
		}
		
		return ret;
	}
	
	public TzNString getTzNString(int index)
	{
		return (TzNString)colListArray[index];
	}
	
	public TzBigDecimal getTzBigDecimal(String colName) throws TzSystemException
	{
		TzBigDecimal ret = (TzBigDecimal)colListMap.get(colName.toUpperCase());
		
		if(ret == null)
		{
			throw new TzSystemException("error: can't find the column \"" + colName + "\"the resultset.");
		}
		
		return ret;
	}
	
	public TzBigDecimal getTzBigDecimal(int index)
	{
		return (TzBigDecimal)colListArray[index];
	}
	
	public TzBoolean getTzBoolean(String colName) throws TzSystemException
	{
		TzBoolean ret = (TzBoolean)colListMap.get(colName.toUpperCase());
		
		if(ret == null)
		{
			throw new TzSystemException("error: can't find the column \"" + colName + "\"the resultset.");
		}
		
		return ret;
	}
	
	public TzBoolean getTzBoolean(int index)
	{
		return (TzBoolean)colListArray[index];
	}
	
	public TzByte getTzByte(String colName) throws TzSystemException
	{
		TzByte ret = (TzByte)colListMap.get(colName.toUpperCase());
		
		if(ret == null)
		{
			throw new TzSystemException("error: can't find the column \"" + colName + "\"the resultset.");
		}
		
		return ret;
	}
	
	public TzByte getTzByte(int index)
	{
		return (TzByte)colListArray[index];
	}
	
	public TzDate getTzDate(String colName) throws TzSystemException
	{
		TzDate ret = (TzDate)colListMap.get(colName.toUpperCase());
		
		if(ret == null)
		{
			throw new TzSystemException("error: can't find the column \"" + colName + "\"the resultset.");
		}
		
		return ret;
	}
	
	public TzDate getTzDate(int index)
	{
		return (TzDate)colListArray[index];
	}
	
	public TzDouble getTzDouble(String colName) throws TzSystemException
	{
		TzDouble ret = (TzDouble)colListMap.get(colName.toUpperCase());
		
		if(ret == null)
		{
			throw new TzSystemException("error: can't find the column \"" + colName + "\"the resultset.");
		}
		
		return ret;
	}
	
	public TzDouble getTzDouble(int index)
	{
		return (TzDouble)colListArray[index];
	}
	
	public TzFloat getTzFloat(String colName) throws TzSystemException
	{
		TzFloat ret = (TzFloat)colListMap.get(colName.toUpperCase());
		
		if(ret == null)
		{
			throw new TzSystemException("error: can't find the column \"" + colName + "\"the resultset.");
		}
		
		return ret;
	}
	
	public TzFloat getTzFloat(int index)
	{
		return (TzFloat)colListArray[index];
	}
	
	public TzLong getTzLong(String colName) throws TzSystemException
	{
		TzLong ret = (TzLong)colListMap.get(colName.toUpperCase());
		
		if(ret == null)
		{
			throw new TzSystemException("error: can't find the column \"" + colName + "\"the resultset.");
		}
		
		return ret;
	}
	
	public TzLong getTzLong(int index)
	{
		return (TzLong)colListArray[index];
	}
	
	public TzShort getTzShort(String colName) throws TzSystemException
	{
		TzShort ret = (TzShort)colListMap.get(colName.toUpperCase());
		
		if(ret == null)
		{
			throw new TzSystemException("error: can't find the column \"" + colName + "\"the resultset.");
		}
		
		return ret;
	}
	
	public TzShort getTzShort(int index)
	{
		return (TzShort)colListArray[index];
	}
	
	public TzTime getTzTime(String colName) throws TzSystemException
	{
		TzTime ret = (TzTime)colListMap.get(colName.toUpperCase());
		
		if(ret == null)
		{
			throw new TzSystemException("error: can't find the column \"" + colName + "\"the resultset.");
		}
		
		return ret;
	}
	
	public TzTime getTzTime(int index)
	{
		return (TzTime)colListArray[index];
	}
	
	public TzTimestamp getTzTimestamp(String colName) throws TzSystemException
	{
		TzTimestamp ret = (TzTimestamp)colListMap.get(colName.toUpperCase());
		
		if(ret == null)
		{
			throw new TzSystemException("error: can't find the column \"" + colName + "\"the resultset.");
		}
		
		return ret;
	}
	
	public TzTimestamp getTzTimestamp(int index)
	{
		return (TzTimestamp)colListArray[index];
	}
}
