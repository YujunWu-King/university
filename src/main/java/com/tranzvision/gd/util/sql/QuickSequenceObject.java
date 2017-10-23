package com.tranzvision.gd.util.sql;

public class QuickSequenceObject
{
	private Integer m_CurrentSequenceNumber = 0;
	private Integer m_CurrentQueneLength = 0;
	private int m_SleepMilliseconds = 0;
	
	public QuickSequenceObject()
	{
		m_CurrentSequenceNumber = 0;
		m_CurrentQueneLength = 0;
	}
	
	public void set(int seqNumber,int queneLen)
	{
		m_CurrentSequenceNumber = seqNumber;
		m_CurrentQueneLength = queneLen;
		
		if(queneLen >= 2)
		{
			m_SleepMilliseconds = 500 / queneLen;
		}
		else
		{
			m_SleepMilliseconds = 0;
		}
	}
	
	public Integer getNextSequenceNumber()
	{
		Integer result = 0;
		
		
		if(m_SleepMilliseconds >= 1)
		{
			try
			{
				Thread.sleep(m_SleepMilliseconds);
			}
			catch(Exception e)
			{
				//do nothing
			}
		}
		
		
		if(m_CurrentQueneLength >= 1)
		{
			m_CurrentSequenceNumber ++;
			m_CurrentQueneLength --;
			
			result = m_CurrentSequenceNumber;
		}
		
		
		return result;
	}
}