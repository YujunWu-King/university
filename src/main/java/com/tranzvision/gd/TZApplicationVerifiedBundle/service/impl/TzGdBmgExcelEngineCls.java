package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzExcelDattT;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.Psprcsrqst;

//import org.springframework.beans.factory.annotation.Autowired;

import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.poi.excel.ExcelHandle;
//import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlParams;
import com.tranzvision.gd.util.sql.type.TzInt;

public class TzGdBmgExcelEngineCls extends BaseEngine {
	//@Autowired
	//private GetSeqNum getSeqNum;
	
	public void OnExecute() throws Exception
	{
		String runControlId = this.getRunControlID();
		TzGdBmgDcExcelClass tzGdBmgDcExcelClass = new TzGdBmgDcExcelClass();
		tzGdBmgDcExcelClass.tzGdDcBmbExcel(runControlId);
	}
	
}
