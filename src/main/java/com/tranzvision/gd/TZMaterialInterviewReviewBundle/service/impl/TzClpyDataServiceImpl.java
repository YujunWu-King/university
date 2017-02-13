package com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;

/**
 * 实现材料评议数据word格式下载;
 * @author 宋子成
 *
 */
@Service("com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl.TzClpyDataServiceImpl")
public class TzClpyDataServiceImpl extends FrameworkImpl  {
	
	@Autowired
	private XmlToWord xmlToWord;
	
	@Autowired
	private  GetHardCodePoint getHardCodePoint;

	/*
	 * 返回生产的word文档的url
	 */
	public String tzOther(String strParams, String[] errMsg) {
		
		String filepath = "C:/tstcreateword2";//定义路径 ， 要用hardcode 配置
		filepath = getHardCodePoint.getHardCodePointVal("TZ_GD_CL_DCPYSJ_PATH");
		filepath = "/pydata/clpydata/";
		
		String url = null ;//定义返回的文档的url
		
		
		// 返回值;
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		jacksonUtil.json2Map(strParams);
		String TZ_CLASS_ID = jacksonUtil.getString("TZ_CLASS_ID");
		String TZ_APPLY_PC_ID = jacksonUtil.getString("TZ_APPLY_PC_ID");
		String TZ_DQPY_LUNC = jacksonUtil.getString("TZ_DQPY_LUNC");
		String TZ_PWEI_OPRIDS = jacksonUtil.getString("TZ_PWEI_OPRIDS");
		
		
		// 检查参数的合法性
		if (StringUtils.isBlank(filepath) || StringUtils.isBlank(TZ_CLASS_ID) || StringUtils.isBlank(TZ_APPLY_PC_ID) || StringUtils.isBlank(TZ_DQPY_LUNC)
				|| StringUtils.isBlank(TZ_PWEI_OPRIDS)) {

			 strRet = "{'error':'参数不全'}";

		}else{
			
			//要判断每个评委是否已经提交了（TZ_CLPWPSLS_TBL）

			try {
				 url = xmlToWord.createWord(filepath, TZ_CLASS_ID, TZ_APPLY_PC_ID,TZ_DQPY_LUNC, TZ_PWEI_OPRIDS);
			} catch (TzSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			strRet = "{'url':'"+url+"'}";
			
		}
		return strRet;
		
	}
}
