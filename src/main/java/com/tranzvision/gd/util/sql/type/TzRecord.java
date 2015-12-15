/**
 * 
 */
package com.tranzvision.gd.util.sql.type;

import com.tranzvision.gd.util.sql.type.*;
import com.tranzvision.gd.util.sql.SqlParams;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.base.TzException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.math.BigDecimal;

/**
 * @author LiGang
 * 2015/11/02
 */
public class TzRecord
{
	private String tableName = "";
	
	//封装过的用于执行SQL语句的对象
	private TZGDObject sqlExecuteObject = null;
	//用于执行SQL语句的对象
	private JdbcTemplate jdbcTemplate;
	
	//查询结果键值对映射表
	private Map<String,Object> colListMap;
	//查询结果数组
	private String[] colNameListArray;
	
	//更新字段键值对映射表
	private Map<String,Object> updateColListMap;
	
	//记录字段类型键值对映射表
	private Map<String,String> colTypeListMap;
	//主键键值对映射表
	private Map<String,Object> primaryColListMap;
	
	public TzRecord()
	{
		colTypeListMap = new HashMap<String,String>();
		
		initialize();
	}
	
	private void initialize()
	{
		colNameListArray = null; 
		colListMap = new HashMap<String,Object>();
		updateColListMap = new HashMap<String,Object>();
		primaryColListMap = new HashMap<String,Object>();
	}
	
	private void getColumnTypes() throws TzException
	{
		//获取结果集
		SqlRowSet rs = null;
		
		String sqlText = "SELECT * FROM " + tableName + " WHERE 1=2";
		rs = jdbcTemplate.queryForRowSet(sqlText);
		
		colTypeListMap.clear();
		int count = rs.getMetaData().getColumnCount();
		for(int i=1;i<=count;i++)
		{
			colTypeListMap.put(rs.getMetaData().getColumnName(i).toUpperCase(),rs.getMetaData().getColumnClassName(i));
		}
	}
	
	private void readyForCreateRecord(String tblName,TZGDObject sqlExecuteObject,JdbcTemplate jdgcObject) throws TzException
	{
		if(sqlExecuteObject == null)
		{
			throw new TzException("fatal error occurred because the specified object of TZGDObject is null.");
		}
		
		this.sqlExecuteObject = sqlExecuteObject;
		this.jdbcTemplate = jdgcObject;
		
		if(tblName.equals(tableName) == false)
		{
			tableName = tblName;
			colTypeListMap.clear();
			primaryColListMap.clear();
			
			checkForUpdateOrInsertOrSelect();
			
			getColumnTypes();
		}
		
		updateColListMap.clear();
	}
	
	public String getTableName()
	{
		return tableName;
	}
	
	private void checkForUpdateOrInsertOrSelect() throws TzException
	{
		if(tableName == null || "".equals(tableName) == true)
		{
			throw new TzException("the record instance is not invalid and please use the method createRecord of the class TZGDObject to create an record instance.");
		}
		
		if(sqlExecuteObject == null || TZGDObject.class.isInstance(sqlExecuteObject) == false)
		{
			throw new TzException("the record instance is not invalid and please use the method createRecord of the class TZGDObject to create an record instance.");
		}
	}
	
	public boolean update() throws TzException
	{
		checkForUpdateOrInsertOrSelect();
		
		//如果没有为更新记录指定对应的字段取值键值对，则直接返回false
		if(updateColListMap.size() <= 0)
		{
			return false;
		}
		
		//如果没有指定用于更新操作条件的键值对列表，则直接返回false
		if(primaryColListMap.size() <= 0)
		{
			return false;
		}
		
		//准备更新记录字段值列表和值列表
		String colList = "";
		Object[] values = new Object[updateColListMap.size() + primaryColListMap.size()];
		int count = 0;
		Iterator<Map.Entry<String,Object>> iter = updateColListMap.entrySet().iterator();
		while(iter.hasNext() == true)
		{
			Map.Entry<String,Object> entry = (Map.Entry<String,Object>) iter.next();
			
			if("".equals(colList) == true)
			{
				colList = entry.getKey() + "=?";
			}
			else
			{
				colList += "," + entry.getKey() + "=?";
			}
			
			values[count] = getDefaultObject(entry.getKey(),updateColListMap.get(entry.getKey()));
			count ++;
		}
		
		//准备更新操作条件语句
		String whereText = "";
		iter = primaryColListMap.entrySet().iterator();
		while(iter.hasNext() == true)
		{
			Map.Entry<String,Object> entry = (Map.Entry<String,Object>) iter.next();
			
			if("".equals(whereText) == true)
			{
				whereText = " " + entry.getKey() + "=?";
			}
			else
			{
				whereText += " AND " + entry.getKey() + "=?";
			}
			
			values[count] = entry.getValue();
			count ++;
		}
		
		//准备更新记录
		String updateSQLText = "UPDATE " + tableName + " SET " + colList + " WHERE " + whereText;
		try
		{
			sqlExecuteObject.sqlExec(updateSQLText,new SqlParams(values));
			updateColListMap.clear();
		}
		catch(Exception e)
		{
			throw new TzException(e.toString());
		}
		
		return true;
	}
	
	public boolean insert() throws TzException
	{
		checkForUpdateOrInsertOrSelect();
		
		//如果没有为插入记录指定对应的字段取值键值对，则直接返回false
		if(updateColListMap.size() <= 0)
		{
			return false;
		}
		
		//准备插入记录字段值列表和值列表
		String colList = "";
		Object[] values = new Object[colTypeListMap.size()];
		String valList = "";
		int count = 0;
		Iterator<Map.Entry<String,String>> iter = colTypeListMap.entrySet().iterator();
		while(iter.hasNext() == true)
		{
			Map.Entry<String,String> entry = (Map.Entry<String,String>) iter.next();
			
			if("".equals(colList) == true)
			{
				colList = entry.getKey();
				valList = "?";
			}
			else
			{
				colList += "," + entry.getKey();
				valList += ",?";
			}
			
			if(updateColListMap.containsKey(entry.getKey()) == true)
			{
				values[count++] = getDefaultObject(entry.getKey(),updateColListMap.get(entry.getKey()));
			}
			else
			{
				values[count++] = getDefaultObject(entry.getKey(),null);
			}
		}
		
		//准备插入新纪录
		String insertSQLText = "INSERT INTO " + tableName + "(" + colList + ") VALUES(" + valList + ")";
		try
		{
			sqlExecuteObject.sqlExec(insertSQLText,new SqlParams(values));
			updateColListMap.clear();
		}
		catch(Exception e)
		{
			throw new TzException(e.toString());
		}
		
		return true;
	}
	
	public boolean selectByKey(TzField...keyFields) throws TzException
	{
		checkForUpdateOrInsertOrSelect();
		
		if(keyFields != null && keyFields.length >= 1)
		{
			initialize();
			
			for(int i=0;i<keyFields.length;i++)
			{
				setColumnValue(keyFields[i].name,keyFields[i].value,true);
			}
		}
		
		if(primaryColListMap.size() <= 0)
		{
			throw new TzException("not specify the key fields when try to use the method selectByKey to get the record value.");
		}
		
		String sqlText = "SELECT * FROM " + tableName + " WHERE";
		String whereText = "";
		Object[] tmpSqlParams = new Object[primaryColListMap.size()];
		int count = 0;
		Iterator<Map.Entry<String,Object>> iter = primaryColListMap.entrySet().iterator();
		while(iter.hasNext() == true)
		{
			Map.Entry<String,Object> entry = (Map.Entry<String,Object>) iter.next();
			
			if("".equals(whereText) == true)
			{
				whereText = " " + entry.getKey() + "=?";
			}
			else
			{
				whereText += " AND " + entry.getKey() + "=?";
			}
			
			tmpSqlParams[count++] = entry.getValue();
		}
		
		//清空待更新字段列表
		updateColListMap.clear();
		
		try
		{
			sqlExecuteObject.sqlExec(sqlText + whereText,new SqlParams(tmpSqlParams),this);
		}
		catch(Exception e)
		{
			throw new TzException(e.toString());
		}
		
		//如果查询结果集的成员数为0 ，则返回false，否则返回true
		return colListMap.size() >= 1;
	}
	
	public void setColumnValue(String colName,Object value) throws TzException
	{
		setColumnValue(colName.trim().toUpperCase(),value,false);
	}
	
	private Object getDefaultObject(String colName,Object value) throws TzException
	{
		Object tmpObject = null;
		
		checkForUpdateOrInsertOrSelect();
		
		switch(colTypeListMap.get(colName.toUpperCase()))
		{
		case "java.lang.Integer":
			tmpObject = value == null ? new Integer(0) : value;
			break;
		case "java.lang.String":
			tmpObject = value == null ? new String("") : value;;
			break;
		case "java.lang.NString":
			tmpObject = value == null ? new String("") : value;
			break;
		case "java.math.BigDecimal":
			tmpObject = value == null ? new BigDecimal(0) : value;
			break;
		case "java.lang.Boolean":
			tmpObject = value;
			break;
		case "java.lang.Byte":
			tmpObject = value;
			break;
		case "java.sql.Date":
			tmpObject = value;
			break;
		case "java.lang.Double":
			tmpObject = value == null ? new Double(0) : value;
			break;
		case "java.lang.Float":
			tmpObject = value == null ? new Float(0) : value;
			break;
		case "java.lang.Long":
			tmpObject = value == null ? new Long(0) : value;
			break;
		case "java.lang.Short":
			tmpObject = value;
			break;
		case "java.sql.Time":
			tmpObject = value;
			break;
		case "java.sql.Timestamp":
			tmpObject = value;
			break;
		default:
			tmpObject = null;
		}
		
		return tmpObject;
	}
	
	public void setColumnValue(String colName,Object value,boolean primaryKey) throws TzException
	{
		if(colName == null || "".equals(colName.trim()) == true)
		{
			throw new TzException("not specify the column name when try to set the value for the specified column use the method setColumnValue.");
		}
		
		String tmpColName = colName.trim().toUpperCase();
		Object tmpObject = getDefaultObject(tmpColName,value);
		
		if(primaryKey == true)
		{
			primaryColListMap.put(tmpColName,tmpObject);
		}
		else
		{
			updateColListMap.put(tmpColName,tmpObject);
		}
	}
	
	private Object getObject(Object obj,String colName)
	{
		Object retObj = null;
		
		String type = obj == null ? colTypeListMap.get(colName) : obj.getClass().getName();
		
		switch(type)
		{
		case "java.lang.Integer":
			retObj = obj == null ? new TzInt() : new TzInt((Integer)obj);
			break;
		case "java.lang.String":
			retObj = obj == null ? new TzString() : new TzString((String)obj);
			break;
		case "java.lang.NString":
			retObj = obj == null ? new TzNString() : new TzNString((String)obj);
			break;
		case "java.math.BigDecimal":
			retObj = obj == null ? new TzBigDecimal() : new TzBigDecimal((BigDecimal)obj);
			break;
		case "java.lang.Boolean":
			retObj = obj == null ? new TzBoolean() : new TzBoolean((Boolean)obj);
			break;
		case "java.lang.Byte":
			retObj = obj == null ? new TzByte() : new TzByte((Byte)obj);
			break;
		case "java.sql.Date":
			retObj = obj == null ? new TzDate() : new TzDate((Date)obj);
			break;
		case "java.lang.Double":
			retObj = obj == null ? new TzDouble() : new TzDouble((Double)obj);
			break;
		case "java.lang.Float":
			retObj = obj == null ? new TzFloat() : new TzFloat((Float)obj);
			break;
		case "java.lang.Long":
			retObj = obj == null ? new TzLong() : new TzLong((Long)obj);
			break;
		case "java.lang.Short":
			retObj = obj == null ? new TzShort() : new TzShort((Short)obj);
			break;
		case "java.sql.Time":
			retObj = obj == null ? new TzTime() : new TzTime((Time)obj);
			break;
		case "java.sql.Timestamp":
			retObj = obj == null ? new TzTimestamp() : new TzTimestamp((Timestamp)obj);
			break;
		default:
			retObj = null;
		}
		
		return retObj;
	}
	
	public void setColList(SqlRowSet rs)
	{
		boolean recExistsFlag = false;
		boolean recFirstFlag = false;
		
		if(rs.isBeforeFirst() == true || rs.isAfterLast() == true)
		{
			recExistsFlag = rs.first();
			recFirstFlag = recExistsFlag;
		}
		else
		{
			try
			{
				//取当前行的第一列，如果能成功，则表示当前结果集肯定不为空
				rs.getObject(1);
				
				recExistsFlag = true;
			}
			catch(Exception e)
			{
				recExistsFlag = false;
			}
		}
		
		String[] colNames = rs.getMetaData().getColumnNames();
		
		//重新设置列类型列表
		if(recFirstFlag == true || colTypeListMap.isEmpty() == true)
		{
			colTypeListMap.clear();
			int count = rs.getMetaData().getColumnCount();
			for(int i=1;i<=count;i++)
			{
				colTypeListMap.put(rs.getMetaData().getColumnName(i).toUpperCase(),rs.getMetaData().getColumnClassName(i));
			}
		}
		
		//读取行记录信息
		colListMap.clear();
		colNameListArray = new String[colNames.length];
		for(int i=0;i<colNames.length;i++)
		{
			Object tmpObj = null;
			
			if(recExistsFlag == true)
			{
				//存在结果集，则读取第一行数据记录
				tmpObj = getObject(rs.getObject(i+1),colNames[i].toUpperCase());
			}
			else
			{
				//不存在结果集，则生成默认空值记录
				tmpObj = getObject(null,colNames[i].toUpperCase());
			}
			
			colListMap.put(colNames[i].toUpperCase(), tmpObj);
			colNameListArray[i] = colNames[i].toUpperCase();
		}
	}
	
	public int getColumnCount()
	{
		return colListMap.size();
	}
	
	public Object getColumn(String colName)
	{
		if(colName == null || colName.trim().equals("") == true)
		{
			return null;
		}
		
		String tmpColName = colName.trim().toUpperCase();
		Object ret = null;
		if(updateColListMap.containsKey(colName.toUpperCase()) == true)
		{
			ret = getObject(updateColListMap.get(tmpColName),tmpColName);
		}
		else
		{
			ret = colListMap.get(tmpColName);
		}
		
		return ret;
	}
	
	public Object getColumn(int index)
	{
		if(index < 0 || index >= colListMap.size())
		{
			return null;
		}
		
		return getColumn(colNameListArray[index]);
	}
	
	public TzInt getTzInt(String colName)
	{
		if(colName == null || colName.trim().equals("") == true)
		{
			return null;
		}
		
		String tmpColName = colName.trim().toUpperCase();
		TzInt ret = null;
		if(updateColListMap.containsKey(colName.toUpperCase()) == true)
		{
			ret = (TzInt)getObject(updateColListMap.get(tmpColName),tmpColName);
		}
		else
		{
			String objType = colListMap.get(tmpColName).getClass().getName();
			if(objType.equals("com.tranzvision.gd.util.sql.type.TzLong") == true)
			{
				ret = new TzInt((int)getTzLong(tmpColName).getValue());
			}
			else
			{
				ret = (TzInt)colListMap.get(tmpColName);
			}
		}
		
		return ret;
	}
	
	public TzInt getTzInt(int index)
	{
		if(index < 0 || index >= colListMap.size())
		{
			return null;
		}
		
		String colName = colNameListArray[index];
		
		return getTzInt(colName);
	}
	
	public TzString getTzString(String colName)
	{
		if(colName == null || colName.trim().equals("") == true)
		{
			return null;
		}
		
		String tmpColName = colName.trim().toUpperCase();
		TzString ret = null;
		if(updateColListMap.containsKey(colName.toUpperCase()) == true)
		{
			ret = (TzString)getObject(updateColListMap.get(tmpColName),tmpColName);
		}
		else
		{
			ret = (TzString)colListMap.get(tmpColName);
		}
		
		return ret;
	}
	
	public TzString getTzString(int index)
	{
		if(index < 0 || index >= colListMap.size())
		{
			return null;
		}
		
		String colName = colNameListArray[index];
		
		return getTzString(colName);
	}
	
	public TzNString getTzNString(String colName)
	{
		if(colName == null || colName.trim().equals("") == true)
		{
			return null;
		}
		
		String tmpColName = colName.trim().toUpperCase();
		TzNString ret = null;
		if(updateColListMap.containsKey(colName.toUpperCase()) == true)
		{
			ret = (TzNString)getObject(updateColListMap.get(tmpColName),tmpColName);
		}
		else
		{
			ret = (TzNString)colListMap.get(tmpColName);
		}
		
		return ret;
	}
	
	public TzNString getTzNString(int index)
	{
		if(index < 0 || index >= colListMap.size())
		{
			return null;
		}
		
		String colName = colNameListArray[index];
		
		return getTzNString(colName);
	}
	
	public TzBigDecimal getTzBigDecimal(String colName)
	{
		if(colName == null || colName.trim().equals("") == true)
		{
			return null;
		}
		
		String tmpColName = colName.trim().toUpperCase();
		TzBigDecimal ret = null;
		if(updateColListMap.containsKey(colName.toUpperCase()) == true)
		{
			ret = (TzBigDecimal)getObject(updateColListMap.get(tmpColName),tmpColName);
		}
		else
		{
			ret = (TzBigDecimal)colListMap.get(tmpColName);
		}
		
		return ret;
	}
	
	public TzBigDecimal getTzBigDecimal(int index)
	{
		if(index < 0 || index >= colListMap.size())
		{
			return null;
		}
		
		String colName = colNameListArray[index];
		
		return getTzBigDecimal(colName);
	}
	
	public TzBoolean getTzBoolean(String colName)
	{
		if(colName == null || colName.trim().equals("") == true)
		{
			return null;
		}
		
		String tmpColName = colName.trim().toUpperCase();
		TzBoolean ret = null;
		if(updateColListMap.containsKey(colName.toUpperCase()) == true)
		{
			ret = (TzBoolean)getObject(updateColListMap.get(tmpColName),tmpColName);
		}
		else
		{
			ret = (TzBoolean)colListMap.get(tmpColName);
		}
		
		return ret;
	}
	
	public TzBoolean getTzBoolean(int index)
	{
		if(index < 0 || index >= colListMap.size())
		{
			return null;
		}
		
		String colName = colNameListArray[index];
		
		return getTzBoolean(colName);
	}
	
	public TzByte getTzByte(String colName)
	{
		if(colName == null || colName.trim().equals("") == true)
		{
			return null;
		}
		
		String tmpColName = colName.trim().toUpperCase();
		TzByte ret = null;
		if(updateColListMap.containsKey(colName.toUpperCase()) == true)
		{
			ret = (TzByte)getObject(updateColListMap.get(tmpColName),tmpColName);
		}
		else
		{
			ret = (TzByte)colListMap.get(tmpColName);
		}
		
		return ret;
	}
	
	public TzByte getTzByte(int index)
	{
		if(index < 0 || index >= colListMap.size())
		{
			return null;
		}
		
		String colName = colNameListArray[index];
		
		return getTzByte(colName);
	}
	
	public TzDate getTzDate(String colName)
	{
		if(colName == null || colName.trim().equals("") == true)
		{
			return null;
		}
		
		String tmpColName = colName.trim().toUpperCase();
		TzDate ret = null;
		if(updateColListMap.containsKey(colName.toUpperCase()) == true)
		{
			ret = (TzDate)getObject(updateColListMap.get(tmpColName),tmpColName);
		}
		else
		{
			String objType = colListMap.get(tmpColName).getClass().getName();
			if(objType.equals("com.tranzvision.gd.util.sql.type.TzTimestamp") == true)
			{
				ret = new TzDate(getTzTimestamp(tmpColName).getValue());
			}
			else
			{
				ret = (TzDate)colListMap.get(tmpColName);
			}
		}
		
		return ret;
	}
	
	public TzDate getTzDate(int index)
	{
		if(index < 0 || index >= colListMap.size())
		{
			return null;
		}
		
		String colName = colNameListArray[index];
		
		return getTzDate(colName);
	}
	
	public TzDouble getTzDouble(String colName)
	{
		if(colName == null || colName.trim().equals("") == true)
		{
			return null;
		}
		
		String tmpColName = colName.trim().toUpperCase();
		TzDouble ret = null;
		if(updateColListMap.containsKey(colName.toUpperCase()) == true)
		{
			ret = (TzDouble)getObject(updateColListMap.get(tmpColName),tmpColName);
		}
		else
		{
			ret = (TzDouble)colListMap.get(tmpColName);
		}
		
		return ret;
	}
	
	public TzDouble getTzDouble(int index)
	{
		if(index < 0 || index >= colListMap.size())
		{
			return null;
		}
		
		String colName = colNameListArray[index];
		
		return getTzDouble(colName);
	}
	
	public TzFloat getTzFloat(String colName)
	{
		if(colName == null || colName.trim().equals("") == true)
		{
			return null;
		}
		
		String tmpColName = colName.trim().toUpperCase();
		TzFloat ret = null;
		if(updateColListMap.containsKey(colName.toUpperCase()) == true)
		{
			ret = (TzFloat)getObject(updateColListMap.get(tmpColName),tmpColName);
		}
		else
		{
			ret = (TzFloat)colListMap.get(tmpColName);
		}
		
		return ret;
	}
	
	public TzFloat getTzFloat(int index)
	{
		if(index < 0 || index >= colListMap.size())
		{
			return null;
		}
		
		String colName = colNameListArray[index];
		
		return getTzFloat(colName);
	}
	
	public TzLong getTzLong(String colName)
	{
		if(colName == null || colName.trim().equals("") == true)
		{
			return null;
		}
		
		String tmpColName = colName.trim().toUpperCase();
		TzLong ret = null;
		if(updateColListMap.containsKey(colName.toUpperCase()) == true)
		{
			ret = (TzLong)getObject(updateColListMap.get(tmpColName),tmpColName);
		}
		else
		{
			ret = (TzLong)colListMap.get(tmpColName);
		}
		
		return ret;
	}
	
	public TzLong getTzLong(int index)
	{
		if(index < 0 || index >= colListMap.size())
		{
			return null;
		}
		
		String colName = colNameListArray[index];
		
		return getTzLong(colName);
	}
	
	public TzShort getTzShort(String colName)
	{
		if(colName == null || colName.trim().equals("") == true)
		{
			return null;
		}
		
		String tmpColName = colName.trim().toUpperCase();
		TzShort ret = null;
		if(updateColListMap.containsKey(colName.toUpperCase()) == true)
		{
			ret = (TzShort)getObject(updateColListMap.get(tmpColName),tmpColName);
		}
		else
		{
			ret = (TzShort)colListMap.get(tmpColName);
		}
		
		return ret;
	}
	
	public TzShort getTzShort(int index)
	{
		if(index < 0 || index >= colListMap.size())
		{
			return null;
		}
		
		String colName = colNameListArray[index];
		
		return getTzShort(colName);
	}
	
	public TzTime getTzTime(String colName)
	{
		if(colName == null || colName.trim().equals("") == true)
		{
			return null;
		}
		
		String tmpColName = colName.trim().toUpperCase();
		TzTime ret = null;
		if(updateColListMap.containsKey(colName.toUpperCase()) == true)
		{
			ret = (TzTime)getObject(updateColListMap.get(tmpColName),tmpColName);
		}
		else
		{
			ret = (TzTime)colListMap.get(tmpColName);
		}
		
		return ret;
	}
	
	public TzTime getTzTime(int index)
	{
		if(index < 0 || index >= colListMap.size())
		{
			return null;
		}
		
		String colName = colNameListArray[index];
		
		return getTzTime(colName);
	}
	
	public TzTimestamp getTzTimestamp(String colName)
	{
		if(colName == null || colName.trim().equals("") == true)
		{
			return null;
		}
		
		String tmpColName = colName.trim().toUpperCase();
		TzTimestamp ret = null;
		if(updateColListMap.containsKey(colName.toUpperCase()) == true)
		{
			ret = (TzTimestamp)getObject(updateColListMap.get(tmpColName),tmpColName);
		}
		else
		{
			ret = (TzTimestamp)colListMap.get(tmpColName);
		}
		
		return ret;
	}
	
	public TzTimestamp getTzTimestamp(int index)
	{
		if(index < 0 || index >= colListMap.size())
		{
			return null;
		}
		
		String colName = colNameListArray[index];
		
		return getTzTimestamp(colName);
	}
}
