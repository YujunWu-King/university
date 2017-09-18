package com.tranzvision.gd.TZReferenceMaterialBundle.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZReferenceMaterialBundle.service.TzRefMaterialBase;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 英语水平-参考资料展现
 * @author zhanglang
 * 2017-04-20
 */
@Service("com.tranzvision.gd.TZReferenceMaterialBundle.service.impl.TzEnglishLevelImpl")
public class TzEnglishLevelImpl implements TzRefMaterialBase {

	@Autowired
	private TzGenRefMaterialPageServiceImpl tzGenRefMaterialPage;
	
	@Autowired
	private TZGDObject tzGdObject;
	
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	
	@Override
	public String genRefDataPage(Map<String, String> dataMap) {
		String refDataHtml = "";
		String errorMsg = "";
		try{
			String classId = dataMap.get("classId");
			String batchId = dataMap.get("batchId");
			String appInsId = dataMap.get("appInsId");
			String cjxId = dataMap.get("cjxId");
			
			//用于展现英语水平的报名表子模板编号
			String appTmpId = "";
			try{
				appTmpId = getHardCodePoint.getHardCodePointVal("TZ_CKZL_WYSP_ZMB");
			}catch(Exception e1){
				e1.printStackTrace();
				errorMsg = "报名表附属模板未定义";
			}
			
			/*打分过程*/
			String scoreProcessHtml = tzGenRefMaterialPage.getScoreProcessHtml(classId, batchId, appInsId, cjxId);
			
			String stuInfoUrl  = tzGenRefMaterialPage.getStuInfoUrl(appInsId, appTmpId);
			
			refDataHtml = tzGdObject.getHTMLText("HTML.TZReferenceMaterialBundle.TZ_REF_MATERAL_MAIN_HTML",scoreProcessHtml,stuInfoUrl);
		}catch(Exception e){
			e.printStackTrace();
			errorMsg = "系统错误，"+e.getMessage();
		}
		
		if(!"".equals(errorMsg)){
			refDataHtml = errorMsg;
		}
		return refDataHtml;
	}

}
