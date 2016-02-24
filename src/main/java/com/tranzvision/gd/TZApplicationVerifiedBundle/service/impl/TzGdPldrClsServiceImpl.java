package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;

/**
 * 高端产品-报名审核-报名流程结果公布
 * PS:TZ_GD_LCFB_PKG:TZ_GD_PLDR_CLS
 * @author tang
 *
 */
@Service("com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl.TzGdPldrClsServiceImpl")
public class TzGdPldrClsServiceImpl extends FrameworkImpl {
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnMap = new HashMap<>();
		String str_cw_yhk = "";
		returnMap.put("retrun_desc", str_cw_yhk);
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (actData.length == 0) {
			return jacksonUtil.Map2json(returnMap);
		}

		try {

			for (int num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				//TODO
				
			}
			
		}catch(Exception e){
			
		}
		str_cw_yhk = "该excel导入未开发完成，请完成开发";
		returnMap.replace("retrun_desc", str_cw_yhk);
		return jacksonUtil.Map2json(returnMap);
	}
}
