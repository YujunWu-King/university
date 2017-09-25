package com.tranzvision.gd.util.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl.tzOnlineAppServiceImpl;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 内存数据初始化加载
 * 
 * @author caoy
 *
 */
public class MemoryStart implements ApplicationListener<ContextRefreshedEvent> {

	// @Autowired
	// private ApplicationContext acx;

	@Autowired
	private SqlQuery sqlQuery;

	final String LJ = "@";

	/**
	 * 当spring容器初始化完成后，自动启动MemoryStart管理进程
	 */
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// 当spring容器初始化完成后,启动MemoryStart管理进程
		if (event.getApplicationContext().getParent() == null) {
			startMemoryLoad();
			//初始化在线报名信号量
			initAppOnlineSemaphoreCount();
		}
	}

	public MemoryStart() {
		;
	}

	/**
	 * 启动BatchServer管理进程的方法
	 */
	private void startMemoryLoad() {
		System.out.println("Start Load Memory");

		System.out.println("Load MessageText Start");
		long time = System.currentTimeMillis();

		String sql = "select TZ_XXJH_ID,TZ_MSG_ID,TZ_JG_ID,TZ_LANGUAGE_ID,TZ_MSG_TEXT from PS_TZ_PT_XXDY_TBL";
		
		
		
		List<Map<String, Object>> listData = sqlQuery.queryForList(sql);
		
		
		System.out.println("Load MessageText by DB：time=" + (System.currentTimeMillis() - time));
		
		 time = System.currentTimeMillis();
		String TZ_XXJH_ID = null;
		String TZ_MSG_ID = null;
		String TZ_JG_ID = null;
		String TZ_LANGUAGE_ID = null;
		String TZ_MSG_TEXT = null;
		Map<String, Object> mapData = null;

		// Key:TZ_XXJH_ID@TZ_JG_ID
		// value:map(key:TZ_MSG_ID@TZ_LANGUAGE_ID,value:TZ_MSG_TEXT)

		// Map<String, String> messageMap = new HashMap<String, String>();
		String firstKey = null;
		String secondKey = null;

		for (Object objDataTap : listData) {
			mapData = (Map<String, Object>) objDataTap;
			TZ_XXJH_ID = mapData.get("TZ_XXJH_ID") == null ? "" : String.valueOf(mapData.get("TZ_XXJH_ID"));
			TZ_MSG_ID = mapData.get("TZ_MSG_ID") == null ? "" : String.valueOf(mapData.get("TZ_MSG_ID"));
			TZ_JG_ID = mapData.get("TZ_JG_ID") == null ? "" : String.valueOf(mapData.get("TZ_JG_ID"));
			TZ_LANGUAGE_ID = mapData.get("TZ_LANGUAGE_ID") == null ? "" : String.valueOf(mapData.get("TZ_LANGUAGE_ID"));
			TZ_MSG_TEXT = mapData.get("TZ_MSG_TEXT") == null ? "" : String.valueOf(mapData.get("TZ_MSG_TEXT"));

			firstKey = TZ_XXJH_ID + LJ + TZ_JG_ID;
			secondKey = TZ_MSG_ID + LJ + TZ_LANGUAGE_ID;

			if (Memoryparameter.messageText.get(firstKey) == null) {
				Memoryparameter.messageText.put(firstKey, new HashMap<String, String>());
			}

			Memoryparameter.messageText.get(firstKey).put(secondKey, TZ_MSG_TEXT);

		}
		System.out.println("Load MessageText put HashMap：time=" + (System.currentTimeMillis() - time));

	}

	
	/**
	 * 初始化在线报名信号量
	 */
	private void initAppOnlineSemaphoreCount() {
		Integer semaphoreCount;
		String sql = "select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT='TZ_APPONL_XHL_COUNT'";
		try {
			semaphoreCount = sqlQuery.queryForObject(sql, "Integer");
		} catch (Exception e) {
			semaphoreCount = null;
		}

		if(semaphoreCount != null){
			tzOnlineAppServiceImpl.setOnlineAppLockCounter(semaphoreCount);
		}
		
		System.out.println("Load onlineAppSemaphoreCount: " + semaphoreCount);
	}
}
