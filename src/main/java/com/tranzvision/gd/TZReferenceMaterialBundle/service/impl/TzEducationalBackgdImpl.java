package com.tranzvision.gd.TZReferenceMaterialBundle.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZReferenceMaterialBundle.service.TzRefMaterialBase;

/**
 * 教育背景参考资料展示逻辑
 * @author zhanglang
 * 2017-04-01
 */
@Service("com.tranzvision.gd.TZReferenceMaterialBundle.service.impl.TzEducationalBackgdImpl")
public class TzEducationalBackgdImpl implements TzRefMaterialBase {
	
	@Autowired
	private TzGenRefMaterialPageServiceImpl tzGenRefMaterialPage;

	@Override
	public String genRefDataPage(Map<String, String> dataMap) {
		String refDataHtml = "";

		try{
			String classId = dataMap.get("classId");
			String batchId = dataMap.get("batchId");
			String appInsId = dataMap.get("appInsId");
			String cjxId = dataMap.get("cjxId");
			
			//用于展现教育背景的报名表子模板编号
			String appTmpId = "";
			
			String scoreProcessHtml = tzGenRefMaterialPage.getScoreProcessHtml(classId, batchId, appInsId, cjxId);
			
			String stuInfoHtml  = tzGenRefMaterialPage.getStuInfoUrl(appInsId, appTmpId);
			
			refDataHtml = scoreProcessHtml + stuInfoHtml;
					
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return refDataHtml;
	}

}
