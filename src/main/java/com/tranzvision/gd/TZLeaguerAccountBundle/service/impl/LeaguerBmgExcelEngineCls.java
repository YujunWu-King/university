package com.tranzvision.gd.TZLeaguerAccountBundle.service.impl;

import com.tranzvision.gd.batch.engine.base.BaseEngine;

public class LeaguerBmgExcelEngineCls extends BaseEngine {
	
	public void OnExecute() throws Exception
	{
		String runControlId = this.getRunControlID();
		LeaguerBmgDcExcelClass leaguerBmgDcExcelClass = new LeaguerBmgDcExcelClass();
		leaguerBmgDcExcelClass.tzGdDcBmbExcel(runControlId);
	}	
}
