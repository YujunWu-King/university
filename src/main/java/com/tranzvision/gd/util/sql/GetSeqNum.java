package com.tranzvision.gd.util.sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	//记录当前服务连续获取下一个序列号次数的变量
	private static HashMap<String,Integer> continuousTimesMap = new HashMap<String,Integer>();
	//记录当前服务上次获取的下一个序列号的变量
	private static HashMap<String,Integer> previousIndexMap = new HashMap<String,Integer>();
	
	public int getSeqNum(String tableName, String colName)
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
			tmpSemaphore.acquire();
			
			
			if(continuousTimesMap.containsKey(tmpSemaphoreName) == false)
			{
				continuousTimesMap.put(tmpSemaphoreName, new Integer(0));
			}
			if(previousIndexMap.containsKey(tmpSemaphoreName) == false)
			{
				previousIndexMap.put(tmpSemaphoreName, new Integer(0));
			}
			
			
			try
			{
				int continuoustimes = continuousTimesMap.get(tmpSemaphoreName);
				int previousindex = previousIndexMap.get(tmpSemaphoreName);
				
				//如果尝试20次还没有获得下一个序列号，则直接返回0，返回0表示获取下一个序列号失败
				int counter = 0;
				while(index <= 0)
				{
					//获取下一个序列号值，并计数
					index = this.pGetSeqNum(tableName, colName);
					counter ++;
					
					//如果成功获得下一个序列号（index的值大于0），则记录该值，并将continuoustimes的值增加1
					if(index >= 1)
					{
						if(index - previousindex == 1)
						{
							continuoustimes ++;
						}
						else
						{
							continuoustimes = 0;
						}
						previousindex = index;
						
						//如果针对同一个序列，当前服务已连续获得了10个连续的序列值，则让它睡眠20毫秒，以便其他的服务有机会获得针对同一序列的序列值的机会
						if(continuoustimes >= 10)
						{
							continuoustimes = 0;
							System.out.println("the current thread[" + Thread.currentThread().getId() + "] will sleep 20 milliseconds.");
							Thread.sleep(20);
						}
						
						continuousTimesMap.put(tmpSemaphoreName, continuoustimes);
						previousIndexMap.put(tmpSemaphoreName, previousindex);
					}
					
					//如果超过20次都没有成功获得下一个序列号，则宣告失败并返回0
					if(counter >= 20)
					{
						break;
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


	private int pGetSeqNum(String tableName, String colName)
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
			
			
			//更新当前序列的值
			int updateFlag = 0;
			sql = "UPDATE PS_TZ_SEQNUM_T SET TZ_SEQNUM=TZ_SEQNUM+1 WHERE TZ_TABLE_NAME = ? AND TZ_COL_NAME = ? AND TZ_SEQNUM <= ?";
			updateFlag = jdbcTemplate.update(sql, new Object[] { tableName, colName, tmpIndex});
			
			
			//此处利用jdbcTemplate.update方法返回更新记录行数来判断是否更新成功
			if(updateFlag >= 1)
			{
				//如果更新成功，则返回下一个序列值
				index = tmpIndex + 1;
			}
		}
		catch (Exception e)
		{
			//如果发生异常直接返回0，表示获取下一个序列号失败
			return 0;
		}
		
		
		return index;
	}


	/**** 自动编号 *****/
	public int getSeqNumOracle(String tableName, String colName)
	{
		int index = 0;
		try {
			String lockSQL = "update PS_TZ_SEQNUM_T set tz_seqnum = tz_seqnum + 1 where tz_table_name = ? and tz_col_name = ?";
			jdbcTemplate.update(lockSQL, new Object[] { tableName, colName });

			String selectSql = "select tz_seqnum from PS_TZ_SEQNUM_T where tz_table_name = ? and tz_col_name = ?";
			try {
				index = jdbcTemplate.queryForObject(selectSql, new Object[] { tableName, colName }, "Integer");
			} catch (Exception e) {
				index = 0;
			}

			if (index == 0) {
				String insertSQL = "insert into PS_TZ_SEQNUM_T (tz_table_name, tz_col_name, tz_seqnum) values (?, ?, 1)";
				int i = jdbcTemplate.update(insertSQL, new Object[] { tableName, colName });
				if (i > 0) {
					index = 1;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return index;
	}
}
