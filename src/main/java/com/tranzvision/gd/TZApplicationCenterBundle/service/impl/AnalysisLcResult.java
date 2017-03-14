package com.tranzvision.gd.TZApplicationCenterBundle.service.impl;

import com.tranzvision.gd.util.base.AnalysisSysVar;

public class AnalysisLcResult {
	/**
	 * 
	 * @param type "A":表示报名中心传入解析，"B"表示面试申请页面，"C"表示历史报名页面 
	 * @param TZ_APP_INS_ID  报名表id
	 * @param rootPath  项目根目录
	 * @param TZ_APPPRO_RST 解析的内容
	 * @param isMobile 是否是手机
	 * @param siteId 站点id
	 * @return
	 */
	public String[] analysisLc(String type,String TZ_APP_INS_ID,String rootPath, String TZ_APPPRO_RST,String isMobile,String siteId){
		String[] result = {"",""};
		String isFb = "";
		//查看有没有占位符系统变量;
		int startIndex = TZ_APPPRO_RST.indexOf("[占位符");
		int endIndex = 0;
		if(startIndex >= 0 ){
			endIndex = TZ_APPPRO_RST.indexOf("]",startIndex);
		}

		while (startIndex >=0 && endIndex >0){
			//需要解析的系统变量;
			String sysvarId = TZ_APPPRO_RST.substring(startIndex + 5,endIndex);
			
			//系统变量前的字符;
			String startString = TZ_APPPRO_RST.substring(0,startIndex);
			//系统变量后的字符;
			String endString = TZ_APPPRO_RST.substring(endIndex+1,TZ_APPPRO_RST.length());
		
			//解析的系统变量值；
			String sysvalue = "";
			String[] sysVarParam = {type,TZ_APP_INS_ID,rootPath,isMobile,siteId};
			AnalysisSysVar analysisSysVar = new AnalysisSysVar();
			analysisSysVar.setM_SysVarID(sysvarId);
			analysisSysVar.setM_SysVarParam(sysVarParam);
			Object obj = analysisSysVar.GetVarValue();
			if(obj != null && !"".equals(obj)){
				String[] sysVarList = (String[])obj;
				if(!"Y".equals(isFb)){
					isFb = sysVarList[0];
				}
				sysvalue = sysVarList[1];
			}else{
				sysvalue = "";
			}
			
			TZ_APPPRO_RST = startString+sysvalue+endString;
			
			startIndex = TZ_APPPRO_RST.indexOf("[占位符");
			if(startIndex >=0 ){
				endIndex = TZ_APPPRO_RST.indexOf("]",startIndex);
			}
		}
		result[0] = isFb;
		result[1] = TZ_APPPRO_RST;

		return result;
	}
}
