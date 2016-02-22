package com.tranzvision.gd.TZFileUploadBundle.service.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzFormAttTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormAttT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormAttTKey;
import com.tranzvision.gd.util.base.JacksonUtil;

/**
 * @author WRL TZ_FILEUPD_PKG:fileDownLoad 附件下载,图片查看
 */
@Service("com.tranzvision.gd.TZFileUploadBundle.service.impl.TzFileDownloadClsServiceImpl")
public class TzFileDownloadClsServiceImpl extends FrameworkImpl {
	@Autowired
	private PsTzFormAttTMapper psTzFormAttTMapper;

	/**
	 * 附件下载
	 */
	@Override
	public String tzGetJsonData(String strParams) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);

		// 报名表实例ID
		String appInsId = jacksonUtil.getString("tz_app_ins_id");

		// 信息项编号
		String itemId = jacksonUtil.getString("itemId");

		// 序号
		String index = jacksonUtil.getString("orderby");

		// 文件数据
		String fileDate = "";
		String sysfileName = "";
		if (jacksonUtil.containsKey("fileDate")) {
			fileDate = jacksonUtil.getString("fileDate");
			Map<String, Object> fileMap = jacksonUtil.parseJson2Map(fileDate);
			// 系统文件名
			String sysfileName2 = fileMap.get("sysFileName") == null ? "" : String.valueOf(fileMap.get("sysFileName"));

			PsTzFormAttTKey psTzFormAttTKey = new PsTzFormAttTKey();
			psTzFormAttTKey.setTzAppInsId(Long.parseLong(appInsId));
			psTzFormAttTKey.setTzXxxBh(itemId);
			psTzFormAttTKey.setTzIndex(Integer.parseInt(index));
			PsTzFormAttT psTzFormAttT = psTzFormAttTMapper.selectByPrimaryKey(psTzFormAttTKey);
			if (psTzFormAttT != null) {
				sysfileName = psTzFormAttT.getAttachsysfilename();
			}
			if (StringUtils.isBlank(sysfileName)) {
				sysfileName = sysfileName2;
			}
		}

		// 图片数据
		String imgData = "";
		if (jacksonUtil.containsKey("imgDate")) {
			imgData = jacksonUtil.getString("imgDate");
			// TODO
			Map<String, Object> imgMap = jacksonUtil.parseJson2Map(imgData);
		}

		return "";

	}

	/**
	 * 图片查看
	 */
	@Override
	public String tzGetHtmlContent(String strParams) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);

		return "";
	}

}
