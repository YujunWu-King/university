package com.tranzvision.gd.util.sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

@Service
public class GetSeqNum
{
	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private TZGDObject tzGDObject;
	
	//记录当前服务获取序列状态对象的映射集合变量
	private static HashMap<String,QuickSequenceObject> currentQueneLengthMap = new HashMap<String,QuickSequenceObject>();
	
	//public int getSeqNum(String tableName, String colName) throws Exception
	public int getSeqNum(String tableName, String colName)
	{
		int index = 0;
		
		
		//获取序列号
		index = this.pGetSeqNum1(tableName,colName);
		if(index <= 0)
		{
			//throw new Exception("failed to acquire the next sequence number for [" + tableName + "." + colName + "].");
		}
		
		
		return index;
	}
	
	
	private int pGetSeqNum1(String tableName, String colName)
	{
		int index = 0;
		
		
		//获取当前序列对应的信号灯
		Map.Entry<String,Semaphore> tmpSemaphoreObject = tzGDObject.getSemaphore(tableName + "-" + colName);
		if(tmpSemaphoreObject == null || tmpSemaphoreObject.getKey() == null || tmpSemaphoreObject.getValue() == null)
		{
			//如果返回的信号灯为空，则直接返回0，表示获取下一个序列号失败
			return 0;
		}
		String tmpSemaphoreName = tmpSemaphoreObject.getKey();
		Semaphore tmpSemaphore = tmpSemaphoreObject.getValue();
		
		//尝试获取信号灯通行权
		try
		{
			//通过获取的信号灯将获取下一个序列值的并行访问串行化执行
			tmpSemaphore.acquireUninterruptibly();
			
			
			//生成记录上次序列状态的对象
			if(currentQueneLengthMap.containsKey(tmpSemaphoreName) == false)
			{
				currentQueneLengthMap.put(tmpSemaphoreName,new QuickSequenceObject());
			}
			
			QuickSequenceObject tmpQuickSequenceObject = currentQueneLengthMap.get(tmpSemaphoreName);
			
			try
			{
				//如果上次记录了当前序列的排队参数，则直接通过上次记录的排队参数来获取下一个序列号，以减少对数据库中序列对象的争用，减少数据库发送死锁的概率
				index = tmpQuickSequenceObject.getNextSequenceNumber();
				
				if(index <= 0)
				{//否则通过数据库中的序列对象来获取指定序列的下一个值
					//如果尝试1000次还没有获得下一个序列号，则直接返回0，返回0表示获取下一个序列号失败
					int counter = 0;
					while(index <= 0)
					{
						//获取下一个序列号值，并计数
						index = this.pGetSeqNum2(tableName, colName, tmpQuickSequenceObject, tmpSemaphore.getQueueLength());
						counter ++;
						
						//每成功抢到一次就睡眠10毫秒
						if(index >= 1)
						{
							try
							{
								Thread.sleep(10);
							}
							catch(Exception e)
							{
								//do nothing
							}
						}
						
						//如果超过100次都没有成功获得下一个序列号，则宣告失败并返回0
						if(counter >= 1000)
						{
							System.err.println((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()) + "ERROR, failed to get the next sequence value for " + tableName + "." + colName + " in the 1000 times cycle.");
							break;
						}
					}
				}
			}
			catch (Exception e)
			{
				index = 0;
			}
			finally
			{
				tmpSemaphore.release();
			}
		}
		catch (Exception e)
		{
			index = 0;
		}
		
		
		return index;
	}


	private int pGetSeqNum2(String tableName, String colName, QuickSequenceObject quickSequenceObject, int queneLength)
	{
		int index = 0;


		String sql = "";
		String tmpStr = "";
		
		//先判断对应的序列是否存在，如果不存在，则新增相关序列
		try
		{
			sql = "SELECT 'X' FROM PS_TZ_SEQNUM_T WHERE TZ_TABLE_NAME = ? AND TZ_COL_NAME = ?";
			tmpStr = jdbcTemplate.queryForObject(sql,new Object[] { tableName, colName },"String");
			if("X".equals(tmpStr) == false)
			{
				sql = "insert into PS_TZ_SEQNUM_T (TZ_TABLE_NAME, TZ_COL_NAME, TZ_SEQNUM) values (?,?,0)";
				jdbcTemplate.update(sql, new Object[] { tableName, colName });
			}
		}
		catch (Exception e)
		{
			//如果新增相关序列失败，则直接返回0，表示获取下一个序列号失败
			return 0;
		}
		
		
		try
		{
			//获取当前序列的值
			int tmpIndex = 0;
			sql = "SELECT TZ_SEQNUM FROM PS_TZ_SEQNUM_T WHERE TZ_TABLE_NAME = ? AND TZ_COL_NAME = ?";
			tmpIndex = jdbcTemplate.queryForObject(sql, new Object[] { tableName, colName }, "int");
			
			
			//更新当前序列的值，queneLength记录了当前正在排队请求获取当前指定序列序列值的队列长度，如果队列长度大于等于1，则直接将数据库中对应的序列对象的当前序列值更新为TZ_SEQNUM+1+queneLength
			//这样后续队列中的queneLength个请求直接通过后续记录的序列值和队列长度计算获得，以减少对数据库序列对象的争用，减少数据库死锁发生的概率
			int updateFlag = 0;
			sql = "UPDATE PS_TZ_SEQNUM_T SET TZ_SEQNUM=TZ_SEQNUM+1+? WHERE TZ_TABLE_NAME = ? AND TZ_COL_NAME = ? AND TZ_SEQNUM <= ?";
			updateFlag = jdbcTemplate.update(sql, new Object[] { queneLength, tableName, colName, tmpIndex});
			
			
			//此处利用jdbcTemplate.update方法返回更新记录行数来判断是否更新成功
			if(updateFlag >= 1)
			{
				//如果更新成功，则返回下一个序列值
				index = tmpIndex + 1;
				
				//记录当前序列的排队参数，如果排队队列长度大于等于1，则记录当前的已获取的序列值和序列长度，以便后续请求中可以使用这两个排队参数直接获取下一个序列值
				if(queneLength >= 1)
				{
					quickSequenceObject.set(index, queneLength);
				}
			}
		}
		catch (Exception e)
		{
			System.err.println("failed to get the next sequence number for the sequence[" + tableName + "." + colName + "] because of the exception: " + e.toString());
			//如果发生异常直接返回0，表示获取下一个序列号失败
			return 0;
		}
		
		
		return index;
	}


//	/**** 自动编号 *****/
//	public int getSeqNumOracle(String tableName, String colName)
//	{
//		int index = 0;
//		try {
//			String lockSQL = "update PS_TZ_SEQNUM_T set tz_seqnum = tz_seqnum + 1 where tz_table_name = ? and tz_col_name = ?";
//			jdbcTemplate.update(lockSQL, new Object[] { tableName, colName });
//
//			String selectSql = "select tz_seqnum from PS_TZ_SEQNUM_T where tz_table_name = ? and tz_col_name = ?";
//			try {
//				index = jdbcTemplate.queryForObject(selectSql, new Object[] { tableName, colName }, "Integer");
//			} catch (Exception e) {
//				index = 0;
//			}
//
//			if (index == 0) {
//				String insertSQL = "insert into PS_TZ_SEQNUM_T (tz_table_name, tz_col_name, tz_seqnum) values (?, ?, 1)";
//				int i = jdbcTemplate.update(insertSQL, new Object[] { tableName, colName });
//				if (i > 0) {
//					index = 1;
//				}
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return index;
//	}
}
