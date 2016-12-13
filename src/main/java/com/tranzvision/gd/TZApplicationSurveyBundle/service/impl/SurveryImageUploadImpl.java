package com.tranzvision.gd.TZApplicationSurveyBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcWjattchTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjattchT;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 文件上传
 * 
 * @author Caoy
 *
 */
@Service("com.tranzvision.gd.TZApplicationSurveyBundle.service.impl.SurveryImageUploadImpl")
public class SurveryImageUploadImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private PsTzDcWjattchTMapper psTzDcWjattchTMapper;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	/**
	 * JAVA不需要把文件存进数据库，不过需要把路径存进数据库
	 */
	@Override
	public String tzGetJsonData(String strParams) {

		JacksonUtil jsonUtil = new JacksonUtil();

		jsonUtil.json2Map(strParams);
		System.out.println(strParams);

		String strAppInsId = "";
		String strItemId = "";
		String itemName2 = "";
		String sysFileName = "";
		String filename = "";
		String maxOrder = "";
		// String path = "";

		// 取JSON数据的MAP
		Map<String, Object> optionValueMap = jsonUtil.getMap();

		if (optionValueMap.containsKey("tz_app_ins_id") && optionValueMap.get("tz_app_ins_id") != null) {
			strAppInsId = optionValueMap.get("tz_app_ins_id").toString();
		}

		if (optionValueMap.containsKey("itemId") && optionValueMap.get("itemId") != null) {
			strItemId = optionValueMap.get("itemId").toString();
		}
		if (optionValueMap.containsKey("itemName") && optionValueMap.get("itemName") != null) {
			itemName2 = optionValueMap.get("itemName").toString();
		}

		if (optionValueMap.containsKey("sysFileName") && optionValueMap.get("sysFileName") != null) {
			sysFileName = optionValueMap.get("sysFileName").toString();
		}

		if (optionValueMap.containsKey("filename") && optionValueMap.get("filename") != null) {
			filename = optionValueMap.get("filename").toString();
		}

		if (optionValueMap.containsKey("maxOrderBy") && optionValueMap.get("maxOrderBy") != null) {
			maxOrder = optionValueMap.get("maxOrderBy").toString();
		}

		//System.out.println("maxOrder:" + maxOrder);

		String tz_wj_id; /* 问卷ID */

		String strItemName = "", strItemNameCut = "";
		String re_fileName = "";
		String fileSuffix = "";
		String numMaxIndex = "";
		String strMaxIndex = "";
		String viewFileName = "";

		if (!StringUtils.isEmpty(strAppInsId)) {
			tz_wj_id = jdbcTemplate.queryForObject("SELECT TZ_DC_WJ_ID FROM PS_TZ_DC_INS_T WHERE TZ_APP_INS_ID = ?",
					new Object[] { strAppInsId }, "String");

			strItemName = jdbcTemplate.queryForObject(
					"SELECT TZ_XXX_MC FROM PS_TZ_DCWJ_XXXPZ_T WHERE TZ_DC_WJ_ID=? AND TZ_XXX_BH=?",
					new Object[] { tz_wj_id, strItemId }, "String");
		}

		if (StringUtils.isEmpty(strItemName)) {
			strItemName = itemName2;
		}

		// 当前登录人机构ID，用户ID;
		String m_curOrgID = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		/* 文件后缀名 */
		// 文件后缀
		int index = StringUtils.lastIndexOf(sysFileName, ".");
		fileSuffix = StringUtils.substring(sysFileName, index + 1);
		fileSuffix = StringUtils.lowerCase(fileSuffix);

		/* 只有存在报名表实例ID时才写数据库 */
		// if (!StringUtils.isEmpty(strAppInsId) && !strAppInsId.equals("0")) {
		// String is_Exists = jdbcTemplate.queryForObject(
		// "SELECT 'Y' FROM PS_TZ_DC_WJATTCH_T WHERE TZ_ATTACHSYSFILENA = ?",
		// new Object[] { sysFileName },
		// "String");
		// if (StringUtils.isEmpty(is_Exists)) {
		// /* 插入数据库 */
		// PsTzDcWjattchT psTzDcWjattchT = new PsTzDcWjattchT();
		//
		// psTzDcWjattchT.setTzAttachsysfilena(sysFileName);
		// psTzDcWjattchT.setTzAttachfileName(filename);
		// // 服务器存储路径
		// psTzDcWjattchT.setTzAttPUrl(request.getServletContext().getRealPath(path));
		// // 访问路径
		// psTzDcWjattchT.setTzAttAUrl(path);
		// psTzDcWjattchTMapper.insert(psTzDcWjattchT);
		// }
		// strResult = "success";
		// strRsltDesc = "上传成功！";
		// }
		try {
			if (StringUtils.isBlank(maxOrder)) {
				numMaxIndex = "1";
			} else {
				numMaxIndex = String.valueOf(Integer.parseInt(maxOrder) + 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (strItemName.length() > 15) {
			strItemNameCut = strItemName.substring(0, 15) + "...";
		} else {
			strItemNameCut = strItemName;
		}

		strMaxIndex = "_" + numMaxIndex;

		re_fileName = strItemName + strMaxIndex + "." + fileSuffix;

		// rem 文件名不能超过64位;
		if (re_fileName.length() >= 64) {
			int len = re_fileName.length() - 64;
			strItemName = strItemName.substring(0, strItemName.length() - len - 3) + "...";
			re_fileName = strItemName + strMaxIndex + "." + fileSuffix;
		}

		viewFileName = strItemNameCut + strMaxIndex + "." + fileSuffix;

		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("result", "success");
		mapRet.put("resultDesc", "上传成功！");
		mapRet.put("fileName", re_fileName);
		mapRet.put("sysFileName", sysFileName);
		mapRet.put("index", numMaxIndex);
		mapRet.put("viewFileName", viewFileName);

		return jsonUtil.Map2json(mapRet);

	}

}
