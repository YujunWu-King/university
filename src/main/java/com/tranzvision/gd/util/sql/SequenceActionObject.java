package com.tranzvision.gd.util.sql;

import java.util.concurrent.Semaphore;

public class SequenceActionObject
{
	private String m_TableName = "";
	private String m_ColumnName = "";
	private Semaphore m_QueneObject = null;
	private Integer m_QueneLength = -1;
	private Integer m_StepLength = 1;
	private Integer m_BeginValue = 0;
	private Integer m_SequenceValue = 0;
	
	public SequenceActionObject(String tableName,String columnName,Semaphore queneObject,int stepLength,int beginValue)
	{
		m_TableName = tableName;
		m_ColumnName = columnName;
		m_QueneObject = queneObject;
		m_StepLength = stepLength;
		m_BeginValue = beginValue;
	}
	
	public String getTableName()
	{
		return m_TableName;
	}
	
	public String getColumnName()
	{
		return m_ColumnName;
	}
	
	public Integer getQueneLength()
	{
		if(m_QueneLength <= -1)
		{
			m_QueneLength = m_QueneObject.getQueueLength();
		}
		
		return m_QueneLength;
	}
	
	public Integer getStepLength()
	{
		return m_StepLength;
	}
	
	public Integer getBeginValue()
	{
		return m_BeginValue;
	}
	
	public void setSequenceValue(int value)
	{
		m_SequenceValue = value;
	}
	public Integer getSequenceValue()
	{
		return m_SequenceValue;
	}
}