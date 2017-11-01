package com.tranzvision.gd.util.sql;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GetSeqNum
{
	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private TZGDObject tzGDObject;
	
	//记录当前服务获取序列状态对象的映射集合变量
	private static HashMap<String,QuickSequenceObject> currentQueneLengthMap = new HashMap<String,QuickSequenceObject>();
	
	//记录序列请求的映射集合变量
	private static Queue<SequenceActionObject> nextSequenceRequestMap = new LinkedList<SequenceActionObject>();
	
	
	public GetSeqNum()
	{
		//启动处理获取序列值的线程
		SequenceHandlerThreadPool t1 = new SequenceHandlerThreadPool();
		Thread t2 = new Thread(t1);
		t2.start();
	}
	
	/**
	 * 获取指定表名和字段名的增量为1的下一个自增序号
	 * @param tableName 表名
	 * @param colName	字段名
	 * @return
	 */
	public int getSeqNum(String tableName, String colName)
	{
		int index = 0;
		
		
		//获取序列号
		index = this.pGetSeqNum1(tableName,colName, 1, 0);
		if(index <= 0)
		{
			//throw new Exception("failed to acquire the next sequence number for [" + tableName + "." + colName + "].");
		}
		
		
		return index;
	}
	
	
	private int pGetSeqNum1(String tableName, String colName, int Step, int begin)
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
				currentQueneLengthMap.put(tmpSemaphoreName,new QuickSequenceObject(Step));
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
						index = this.pGetSeqNum2(tableName, colName, tmpQuickSequenceObject, tmpSemaphore, Step, begin);
						counter ++;
						
						//每成功抢到一次就睡眠10毫秒
						/*if(index >= 1)
						{
							try
							{
								Thread.sleep(10);
							}
							catch(Exception e)
							{
								//do nothing
							}
						}*/
						
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


	private int pGetSeqNum2(String tableName, String colName, QuickSequenceObject quickSequenceObject, Semaphore semaphoreObject, int Step, int begin)
	{
		int index = 0;
		
		
		boolean tmpOfferFlag = false;
		SequenceActionObject tmpSequenceActionObject = new SequenceActionObject(tableName,colName,semaphoreObject,Step,begin);
		synchronized(nextSequenceRequestMap)
		{
			tmpOfferFlag = nextSequenceRequestMap.offer(tmpSequenceActionObject);
		}
		
		if(tmpOfferFlag == true)
		{
			synchronized(tmpSequenceActionObject)
			{
				try
				{
					tmpSequenceActionObject.wait();
					
					index = tmpSequenceActionObject.getSequenceValue();
					
					if(index >= 1)
					{
						//记录当前序列的排队参数，如果排队队列长度大于等于1，则记录当前的已获取的序列值和序列长度，以便后续请求中可以使用这两个排队参数直接获取下一个序列值
						if(tmpSequenceActionObject.getQueneLength() >= 1)
						{
							quickSequenceObject.set(index, tmpSequenceActionObject.getQueneLength());
						}
					}
				}
				catch(Exception e)
				{
					// do nothing
				}
			}
		}
		
		
		return index;
	}
	
	
	
	
	/**
	 * 获取指定表名和字段名起始值为begin，增量为Step的下一个自增序号
	 * 注意：调用该方法时，同一个序列号需要设置相同的Step
	 * @param tableName	表名
	 * @param colName	字段名
	 * @param Step	自增步长，正整数
	 * @param begin	起始值，0和正整数
	 * @return
	 */
	public int getSeqNum(String tableName, String colName, int Step, int begin){
		int index = 0;
		
		//获取序列号
		index = this.pGetSeqNum1(tableName,colName, Step, begin);
		if(index <= 0)
		{
			//throw new Exception("failed to acquire the next sequence number for [" + tableName + "." + colName + "].");
		}
		
		
		return index;
	}
	
	//序列处理线程池管理类
	private class SequenceHandlerThreadPool implements Runnable
	{
		private final ExecutorService m_SequenceHandlerThreadPool;
		
		public SequenceHandlerThreadPool()
		{
			m_SequenceHandlerThreadPool = Executors.newFixedThreadPool(50);
		}
		
		final public void run()
		{
			while(true)
			{
				SequenceActionObject tmpSequenceActionObject = getNextSequenceActionObject();
				
				if(tmpSequenceActionObject != null)
				{
					m_SequenceHandlerThreadPool.execute(new SequenceActionProtector(tmpSequenceActionObject));
				}
				else
				{
					try
					{
						Thread.sleep(100);
					}
					catch(Exception e)
					{
						// do nothing
					}
				}
			}
		}
		
		private SequenceActionObject getNextSequenceActionObject()
		{
			SequenceActionObject result = null;
			
			
			synchronized(nextSequenceRequestMap)
			{
				if(nextSequenceRequestMap.size() >= 1)
				{
					result = nextSequenceRequestMap.poll();
				}
			}
			
			
			return result;
		}
	}
	
	//用于处理获取序列值的线程，使用线程隔离事务，避免不当的调用顺序导致数据库死锁
	private class SequenceActionProtector implements Runnable
	{
		private final SequenceActionObject m_SequenceActionObject;
		
		public SequenceActionProtector(SequenceActionObject sequenceActionObject)
		{
			m_SequenceActionObject = sequenceActionObject;
		}
		
		final public void run()
		{
			if(m_SequenceActionObject != null)
			{
				getNextSequenceValue(m_SequenceActionObject);
				
				synchronized(m_SequenceActionObject)
				{
					m_SequenceActionObject.notify();
				}
			}
		}
		
		private void getNextSequenceValue(SequenceActionObject sequenceActionObject)
		{
			String tableName = sequenceActionObject.getTableName();
			String colName = sequenceActionObject.getColumnName();
			int queneLength = sequenceActionObject.getQueneLength();
			int Step = sequenceActionObject.getStepLength();
			int begin = sequenceActionObject.getBeginValue();
			
			
			String sql = "";
			String tmpStr = "";
			
			//先判断对应的序列是否存在，如果不存在，则新增相关序列
			try
			{
				sql = "SELECT 'X' FROM PS_TZ_SEQNUM_T WHERE TZ_TABLE_NAME = ? AND TZ_COL_NAME = ?";
				tmpStr = jdbcTemplate.queryForObject(sql,new Object[] { tableName, colName },"String");
				if("X".equals(tmpStr) == false)
				{
					sql = "INSERT IGNORE INTO PS_TZ_SEQNUM_T (TZ_TABLE_NAME, TZ_COL_NAME, TZ_SEQNUM) values (?,?,?)";
					jdbcTemplate.update(sql, new Object[] { tableName, colName, begin });
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				//如果新增相关序列失败，则直接返回，表示获取下一个序列号失败
				return;
			}
			
			
			try
			{
				//获取当前序列的值
				int tmpIndex = 0;
				sql = "SELECT TZ_SEQNUM FROM PS_TZ_SEQNUM_T WHERE TZ_TABLE_NAME = ? AND TZ_COL_NAME = ?";
				tmpIndex = jdbcTemplate.queryForObject(sql, new Object[] { tableName, colName }, "int");
				
				if(tmpIndex >= begin)
				{
					//更新当前序列的值，queneLength记录了当前正在排队请求获取当前指定序列序列值的队列长度，如果队列长度大于等于1，则直接将数据库中对应的序列对象的当前序列值更新为TZ_SEQNUM+1+queneLength
					//这样后续队列中的queneLength个请求直接通过后续记录的序列值和队列长度计算获得，以减少对数据库序列对象的争用，减少数据库死锁发生的概率
					int updateFlag = 0;
					sql = "UPDATE PS_TZ_SEQNUM_T SET TZ_SEQNUM=TZ_SEQNUM+? WHERE TZ_TABLE_NAME = ? AND TZ_COL_NAME = ? AND TZ_SEQNUM <= ?";
					updateFlag = jdbcTemplate.update(sql, new Object[] { (1+queneLength)*Step, tableName, colName, tmpIndex});
					
					
					//此处利用jdbcTemplate.update方法返回更新记录行数来判断是否更新成功
					if(updateFlag >= 1)
					{
						//如果更新成功，则返回下一个序列值
						sequenceActionObject.setSequenceValue(tmpIndex + Step);
					}
				}
				else
				{
					//小于开始值，将序列号设置为开始值
					int updateFlag = 0;
					sql = "UPDATE PS_TZ_SEQNUM_T SET TZ_SEQNUM=? WHERE TZ_TABLE_NAME = ? AND TZ_COL_NAME = ? AND TZ_SEQNUM < ?";
					updateFlag = jdbcTemplate.update(sql, new Object[] { (begin+queneLength*Step), tableName, colName, begin});
					
					//此处利用jdbcTemplate.update方法返回更新记录行数来判断是否更新成功
					if(updateFlag >= 1)
					{
						//如果更新成功，则返回开始值
						sequenceActionObject.setSequenceValue(begin);
					}
				}
			}
			catch (Exception e)
			{
				System.err.println("failed to get the next sequence number for the sequence[" + tableName + "." + colName + "] because of the exception: " + e.toString());
				//如果发生异常直接返回0，表示获取下一个序列号失败
				return;
			}
		}
	}
}
