package com.tranzvision.gd.TZUnifiedImportBundle.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.poi.excel.ExcelHandle;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 解析Excel功能；原：TZ_GD_IMPORT_EXCEL_PKG:TZ_IMPORT_EXCEL_CLS
 * 
 * @author 叶少威
 * @since 2017-01-22
 */
@Service("com.tranzvision.gd.TZUnifiedImportBundle.service.impl.ExcelAnalysisImpl")
public class ExcelAnalysisImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private HttpServletRequest request;


	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		String reString = "";
		if("tzGetExcelTplUrl".equals(oprType)){
			reString = this.tzGetExcelTplUrl(strParams, errorMsg);
		}
		if("tzAnalyzeExcel".equals(oprType)){
			reString = this.tzAnalyzeExcel(strParams, errorMsg);
		}
		
		return reString;
	}

	public String tzGetExcelTplUrl(String strParams, String[] errorMsg) {

		JacksonUtil jacksonUtil = new JacksonUtil();
		String strUrl = "";
		
		try {
			jacksonUtil.json2Map(strParams);
			
			String tplResId =  jacksonUtil.getString("tplResId");
		    String tplResSQL = "SELECT TZ_RES_FILE_PATH ,TZ_RES_FILE_NAME FROM PS_TZ_PT_ZYXX_TBL WHERE TZ_ZYJH_ID='TZ_TMPLRES_MGR' AND TZ_RES_ID=?";
		    Map<String, Object> map = jdbcTemplate.queryForMap(tplResSQL, new Object[] { tplResId });

		    if (map != null) {
				String strFilePath = (String) map.get("TZ_RES_FILE_PATH");
				String strFileName = (String) map.get("TZ_RES_FILE_NAME");
				
				if(strFilePath.lastIndexOf("/")!=(strFilePath.length()-1)){
					strFilePath = strFilePath +"/";
				}

				strUrl = request.getContextPath()+strFilePath+strFileName;
			}

		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		Map<String, Object> hMap = new HashMap<>();
		hMap.put("url", strUrl);
		
		return jacksonUtil.Map2json(hMap);
	}
	
	public String tzAnalyzeExcel(String strParams, String[] errorMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		String strRet = "{}";
		
		try {
			jacksonUtil.json2Map(strParams);
			
			// Excel路径;
			String strPath = jacksonUtil.getString("path");
			// Excel文件名;
			String strFileName = jacksonUtil.getString("sysFileName");

			String dataFilePath = strPath + strFileName;

			List<String> dataListCellKeys = new ArrayList<String>();

			ExcelHandle excelHandle = new ExcelHandle(request);
			excelHandle.readExcel(dataFilePath, dataListCellKeys, false);

			jacksonUtil = new JacksonUtil();
			ArrayList<Map<String, Object>> listData = excelHandle.getExcelListData();

			strRet = jacksonUtil.List2json(listData);

			//删除临时文件
			File file = new File(dataFilePath);  
		    if (file.isFile() && file.exists()) {  
		        file.delete();
		    } 
		    
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return strRet;
	}
}
