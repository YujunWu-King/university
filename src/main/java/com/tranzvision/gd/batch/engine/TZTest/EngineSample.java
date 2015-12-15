/**
 * 
 */
package com.tranzvision.gd.batch.engine.TZTest;

import com.tranzvision.gd.batch.engine.base.BaseEngine;
import java.util.Date;

/**
 * @author LiGang
 * 2015/12/07
 */
public class EngineSample extends BaseEngine
{
	public void OnExecute() throws Exception
	{
		for(int i=0;i<10;i++)
		{
			System.out.println("Is the task ternimated by force? " + (isForceExit() == true ? "Yes." : "No."));
			logInfo("Is the task ternimated by force? " + (isForceExit() == true ? "Yes." : "No."));
			
			if(isForceExit() == true)
			{
				return;
			}
			
			System.out.println("Hello, this is EngineSample running. It's " + (i + 1) + "times at :" + new Date());
			logInfo("Hello, this is EngineSample running. It's " + (i + 1) + "times at :" + new Date());
			
			sleep(10000);;
		}
	}
}
