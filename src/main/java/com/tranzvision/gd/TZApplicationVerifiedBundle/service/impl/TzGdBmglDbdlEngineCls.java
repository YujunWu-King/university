package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.util.sql.GetSeqNum;

/*打包批处理方法*/
@Service
public class TzGdBmglDbdlEngineCls extends BaseEngine{
	@Autowired
	private GetSeqNum getSeqNum;
	
	public void OnExecute() throws Exception
	{
		
		System.out.println("====================================>testesttsete");
		try{
			int num = getSeqNum.getSeqNum("TEST_TEST1", "TEST_TEST1");
			System.out.println("====================================>num:"+num);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("====================================>exception:"+e.toString());
		}
		
		for(int i=0;i<10;i++)
		{
			System.out.println("====================================>Is the task ternimated by force? " + (isForceExit() == true ? "Yes." : "No."));
			logInfo("====================================>Is the task ternimated by force? " + (isForceExit() == true ? "Yes." : "No."));
			
			if(isForceExit() == true)
			{
				return;
			}
			
			System.out.println("====================================>Hello, this is EngineSample running. It's " + (i + 1) + "times at :" + new Date());
			logInfo("====================================>Hello, this is EngineSample running. It's " + (i + 1) + "times at :" + new Date());
		
			sleep(10000);
		}
	}

}
