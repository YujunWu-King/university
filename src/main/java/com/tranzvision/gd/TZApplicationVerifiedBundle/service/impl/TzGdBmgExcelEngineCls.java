package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import com.tranzvision.gd.batch.engine.base.BaseEngine;

public class TzGdBmgExcelEngineCls extends BaseEngine {
	
	public void OnExecute() throws Exception
	{
		String runControlId = this.getRunControlID();
		TzGdBmgDcExcelClass tzGdBmgDcExcelClass = new TzGdBmgDcExcelClass();
		tzGdBmgDcExcelClass.tzGdDcBmbExcel(runControlId);
	}
	
}
