package com.tranzvision.gd.ztest.service.impl;

import com.tranzvision.gd.util.base.AnalysisSysVar;

public class Test {

	public static void main(String[] args) {
		String type = "A", TZ_APP_INS_ID = "1", rootPath = "root", TZ_APPPRO_RST = "TZ_ZLTJ", isMobile = "N", siteId = "21";
		System.out.println(TZ_APPPRO_RST);
		String[] result = { "", "" };
		String isFb = "";
		/*String TZ_APPPRO_RST = "";
		String[] sysVarParam = { type, TZ_APP_INS_ID, rootPath, isMobile, siteId };
		AnalysisSysVar analysisSysVar = new AnalysisSysVar();
		analysisSysVar.setM_SysVarID(sysvarId);
		analysisSysVar.setM_SysVarParam(sysVarParam);
		Object obj = analysisSysVar.GetVarValue();
		if (obj != null && !"".equals(obj)) {
			String[] sysVarList = (String[]) obj;
			if (!"Y".equals(isFb)) {
				isFb = sysVarList[0];
			}
			TZ_APPPRO_RST = sysVarList[1];
		} else {
			TZ_APPPRO_RST = "";
		} */
		
		result[0] = isFb;
		result[1] = TZ_APPPRO_RST;

		System.out.println(TZ_APPPRO_RST);
	}

}
