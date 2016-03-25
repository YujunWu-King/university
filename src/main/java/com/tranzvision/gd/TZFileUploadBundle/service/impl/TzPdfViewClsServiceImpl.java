package com.tranzvision.gd.TZFileUploadBundle.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzFormAttTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormAttT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormAttTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * @author WRL TZ_FILEUPD_PKG:pdfReader PDF在线预览
 */
@Service("com.tranzvision.gd.TZFileUploadBundle.service.impl.TzPdfViewClsServiceImpl")
public class TzPdfViewClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private PsTzFormAttTMapper psTzFormAttTMapper;

	@Autowired
	private TZGDObject tzGdObject;

	/**
	 * PDF在线预览
	 */
	@Override
	public String tzGetJsonData(String strParams) {
		String retHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);

		// 报名表实例ID
		String appInsId = jacksonUtil.getString("tz_app_ins_id");

		// 信息项编号
		String itemId = jacksonUtil.getString("itemId");

		// 序号
		String index = jacksonUtil.getString("orderby");

		// 窗口高度
		String winHeight = jacksonUtil.getString("winHeight") == null ? "0" : jacksonUtil.getString("winHeight");

		String sql = "SELECT B.TZ_APP_TPL_LAN FROM PS_TZ_APP_INS_T A,PS_TZ_APPTPL_DY_T B WHERE A.TZ_APP_INS_ID = ? AND A.TZ_APP_TPL_ID = B.TZ_APP_TPL_ID(+)";
		String lang = sqlQuery.queryForObject(sql, new Object[] { appInsId }, "String");

		String readerTitle = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
				"PDF_VIEW", lang, "PDF预览", "PDF preview");

		// 文件数据
		String fileDate = "";
		String sysfileName = "";
		String contextUrl = request.getContextPath();

		if (jacksonUtil.containsKey("fileDate")) {
			fileDate = jacksonUtil.getString("fileDate");
			Map<String, Object> fileMap = jacksonUtil.parseJson2Map(fileDate);
			// 系统文件名
			String sysfileName2 = fileMap.get("sysFileName") == null ? "" : String.valueOf(fileMap.get("sysFileName"));
			String accessPath = fileMap.get("accessPath") == null ? "" : String.valueOf(fileMap.get("accessPath"));

			PsTzFormAttTKey psTzFormAttTKey = new PsTzFormAttTKey();
			psTzFormAttTKey.setTzAppInsId(Long.parseLong(appInsId));
			psTzFormAttTKey.setTzXxxBh(itemId);
			psTzFormAttTKey.setTzIndex(Integer.parseInt(index));
			PsTzFormAttT psTzFormAttT = psTzFormAttTMapper.selectByPrimaryKey(psTzFormAttTKey);
			if (psTzFormAttT != null) {
				sysfileName = psTzFormAttT.getAttachsysfilename();
			}
			if (StringUtils.isBlank(sysfileName)) {
				sysfileName = contextUrl + accessPath + sysfileName2;
			} else {
				sysfileName = contextUrl + accessPath + sysfileName;
			}
		}

		int height = Integer.parseInt(winHeight) - 40;

		try {
			retHtml = tzGdObject.getHTMLText("HTML.TZFileUploadBundle.TZ_PDF_READER_HTML",sysfileName,
					readerTitle, String.valueOf(height), contextUrl);
		} catch (TzSystemException e) {
			e.printStackTrace();
			retHtml = "";
		}

		return retHtml;

	}
}