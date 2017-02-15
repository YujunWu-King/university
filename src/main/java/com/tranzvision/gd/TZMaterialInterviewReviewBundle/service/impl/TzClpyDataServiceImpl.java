package com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;

/**
 * 实现材料评议数据word格式下载;
 * 
 * @author 宋子成
 *
 */
@Service("com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl.TzClpyDataServiceImpl")
public class TzClpyDataServiceImpl extends FrameworkImpl {

	@Autowired
	private XmlToWord xmlToWord;

	@Autowired
	private GetHardCodePoint getHardCodePoint;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	/*
	 * 返回生成的word文档的url
	 */
	public String tzOther(String oprType, String comParams, String[] errorMsg) {
		
		// 返回值;
		String strRet = "{}";

		String url = "";// 定义返回的文档的url

		JacksonUtil jacksonUtil = new JacksonUtil();

		jacksonUtil.json2Map(comParams);
		String TZ_CLASS_ID = jacksonUtil.getString("TZ_CLASS_ID");
		String TZ_APPLY_PC_ID = jacksonUtil.getString("TZ_APPLY_PC_ID");
		String TZ_PWEI_OPRIDS = jacksonUtil.getString("TZ_PWEI_OPRIDS");

		// 检查参数的合法性
		if (StringUtils.isBlank(TZ_CLASS_ID) || StringUtils.isBlank(TZ_APPLY_PC_ID)
				|| StringUtils.isBlank(TZ_PWEI_OPRIDS)) {

			errorMsg[0] = "1";
			errorMsg[1] = "参数不全";

		} else {

			try {
				url = xmlToWord.createWord(TZ_CLASS_ID, TZ_APPLY_PC_ID, TZ_PWEI_OPRIDS);
			} catch (TzSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if ("1".equals(url)) {
				errorMsg[0] = "1";
				errorMsg[1] = "参数不全";
			} else {

				Map<String, Object> mapData = new HashMap<String, Object>();
				mapData.put("url", url);
				strRet = jacksonUtil.Map2json(mapData);
			}
		}

		return strRet;

	}
}
