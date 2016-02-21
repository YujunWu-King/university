package com.tranzvision.gd.TZFileUploadBundle.service.impl;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTbl;
import com.tranzvision.gd.TZApplicationTemplateBundle.dao.PsTzAppXxxPzTMapper;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzAppXxxPzTKey;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzAppXxxPzTWithBLOBs;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppInsTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzKsTjxTblMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppInsT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzKsTjxTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * @author WRL TZ_FILEUPD_PKG:fileUploadToDB 附件上传、图片删除等相关上传功能
 */
@Service("com.tranzvision.gd.TZFileUploadBundle.service.impl.TzFileUploadClsServiceImpl")
public class TzFileUploadClsServiceImpl extends FrameworkImpl {
	@Autowired
	private PsTzAppInsTMapper psTzAppInsTMapper;

	@Autowired
	private PsTzAppXxxPzTMapper psTzAppXxxPzTMapper;

	@Autowired
	private PsTzKsTjxTblMapper psTzKsTjxTblMapper;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Override
	public String tzGetJsonData(String strParams) {

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);

		// 报名表实例ID
		String parAppInsId = jacksonUtil.getString("tz_app_ins_id");

		// 信息项编号
		String parItemId = jacksonUtil.getString("itemId");

		// 信息项名称
		String parItemName = jacksonUtil.getString("itemName");

		// 系统文件名
		String parSysFileName = jacksonUtil.getString("sysFileName");

		// 序号
		String parMaxOrder = jacksonUtil.getString("maxOrderBy");

		// 多行容器行号
		String parDhIndex = jacksonUtil.getString("dhIndex");

		// 推荐信编号
		String parRefLetterId = jacksonUtil.getString("refLetterId");

		// 报名表模板
		String appTplId = "";
		// 信息项名称
		String itemName = "";
		if (StringUtils.isNotBlank(parAppInsId) && !StringUtils.equals(parAppInsId, "0")
				&& StringUtils.isNotBlank(parItemId)) {
			PsTzAppInsT psTzAppInsT = psTzAppInsTMapper.selectByPrimaryKey(Long.parseLong(parAppInsId));
			appTplId = psTzAppInsT.getTzAppTplId();

			PsTzAppXxxPzTKey psTzAppXxxPzTKey = new PsTzAppXxxPzTKey();
			psTzAppXxxPzTKey.setTzAppTplId(appTplId);
			psTzAppXxxPzTKey.setTzXxxBh(parItemId);
			PsTzAppXxxPzTWithBLOBs psTzAppXxxPzT = psTzAppXxxPzTMapper.selectByPrimaryKey(psTzAppXxxPzTKey);
			if(psTzAppXxxPzT != null){
				itemName = psTzAppXxxPzT.getTzXxxMc();
			}
		}

		if (StringUtils.isBlank(itemName)) {
			itemName = parItemName;
		}

		PsTzAqYhxxTbl psTzAqYhxxTbl = tzLoginServiceImpl.getLoginedManagerInfo(request);
		// 当前登录人姓名
		String userName = psTzAqYhxxTbl.getTzRealname();

		if (StringUtils.isBlank(userName)) {
			userName = "GUEST";
			if (StringUtils.isNotBlank(parRefLetterId)) {
				// 如果为推荐信上传附件，显示推荐人姓名
				PsTzKsTjxTbl psTzKsTjxTbl = psTzKsTjxTblMapper.selectByPrimaryKey(parRefLetterId);
				String appIns = psTzKsTjxTbl.getTzTjxAppInsId() == null ? ""
						: String.valueOf(psTzKsTjxTbl.getTzTjxAppInsId());
				String tjrName = psTzKsTjxTbl.getTzReferrerName();
				String tjrGname = psTzKsTjxTbl.getTzReferrerGname();

				String sql = "SELECT TZ_USE_TYPE FROM PS_TZ_APPTPL_DY_T A,PS_TZ_APP_INS_T B WHERE A.TZ_APP_TPL_ID = B.TZ_APP_TPL_ID AND B.TZ_APP_INS_ID = ?";
				String userType = sqlQuery.queryForObject(sql, new Object[] { appIns }, "String");
				if (StringUtils.equals("TJX", userType)) {
					userName = tjrGname + " " + tjrName;
				}
			}
		}

		// 文件后缀
		int index = StringUtils.lastIndexOf(parSysFileName, ".");
		String fileSuffix = StringUtils.substring(parSysFileName, index + 1);
		fileSuffix = StringUtils.lowerCase(fileSuffix);

		int numMaxIndex;
		if (StringUtils.isBlank(parMaxOrder)) {
			numMaxIndex = 1;
		} else {
			numMaxIndex = Integer.parseInt(parMaxOrder) + 1;
		}

		String itemNameCut;
		if (StringUtils.length(itemName) > 15) {
			itemNameCut = StringUtils.substring(itemName, 0, 15) + "...";
		} else {
			itemNameCut = itemName;
		}

		String maxIndex;
		if (StringUtils.isNotBlank(parDhIndex)) {
			maxIndex = "_" + (Integer.parseInt(parDhIndex) + 1) + "_" + numMaxIndex;
		} else {
			maxIndex = "_" + numMaxIndex;
		}

		String reFileName = userName + "_" + itemName + maxIndex + "." + fileSuffix;
		// 文件名不能超过64位
		if (StringUtils.length(reFileName) >= 64) {
			int len = reFileName.length() - 64;
			itemName = StringUtils.substring(itemName, 0, itemName.length() - len - 3) + "...";
			reFileName = userName + "_" + itemName + maxIndex + "." + fileSuffix;
		}

		String viewFileName = userName + "_" + itemNameCut + maxIndex + "." + fileSuffix;

		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("result", "success");
		mapRet.put("resultDesc", "上传成功！");
		mapRet.put("fileName", reFileName);
		mapRet.put("sysFileName", parSysFileName);
		mapRet.put("index", numMaxIndex);
		mapRet.put("viewFileName", viewFileName);

		return jacksonUtil.Map2json(mapRet);
	}
}
