package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzExcelDattTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzExcelDrxxTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzExcelDattT;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzExcelDrxxT;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 原PS类：TZ_GD_BMGL_BMBSH_PKG:TZ_GD_BMGL_DBDL_CLS
 * @author tang
 * 报名管理-材料批量打包下载 
 */
@Service("com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl.TzGdBmglDbdlClsServiceImpl")
public class TzGdBmglDbdlClsServiceImpl extends FrameworkImpl{
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private PsTzExcelDrxxTMapper psTzExcelDrxxTMapper;
	@Autowired
	private PsTzExcelDattTMapper psTzExcelDattTMapper;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private HttpServletRequest request;
	@Autowired 
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private TzDealWithXMLServiceImpl tzDealWithXMLServiceImpl;
	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};

			// json数据要的结果字段;
			String[] resultFldArray = {"TZ_DR_TASK_DESC", "OPRID", "TZ_STARTTIME_CHAR", "PROCESSINSTANCE", "DESCR" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("fileName", rowList[0]);
					mapList.put("czPerName", rowList[1]);
					mapList.put("bgTime", rowList[2]);
					mapList.put("AEId", rowList[3]);
					mapList.put("AEState", rowList[4]);
					listData.add(mapList);
				}
				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);
	}
	
	// 修改;
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		// 返回值;
		String strComContent = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (actData.length == 0) {
			return strComContent;
		}
		String currentOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			for (int num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				// 保存数据;
				String comParams = jacksonUtil.getString("strAppId");
				
				// 打包文件名称;
				String fileName = jacksonUtil.getString("zldbName");
				strComContent = fileName;
				if(comParams != null && !"".equals(comParams)){ 
					if(comParams.lastIndexOf(";") + 1 == comParams.length()){
						comParams = comParams.substring(0,comParams.length()-1);
					}
					String[] arrAppId = comParams.split(";");
					
					int processInstance = getSeqNum.getSeqNum("TZ_EXCEL_DRXX_T", "PROCESSINSTANCE");
					PsTzExcelDrxxT psTzExcelDrxxT = new PsTzExcelDrxxT();
					psTzExcelDrxxT.setProcessinstance(processInstance);
					psTzExcelDrxxT.setTzComId("TZ_BMGL_BMBSH_COM");
					psTzExcelDrxxT.setTzPageId("TZ_BMGL_DBDL_STD");
					psTzExcelDrxxT.setTzDrLxbh("1");
					psTzExcelDrxxT.setTzDrTaskDesc(fileName);
				      
					psTzExcelDrxxT.setTzStartDtt(new Date());
					psTzExcelDrxxT.setTzDrTotalNum(arrAppId.length);
					psTzExcelDrxxT.setOprid(currentOprid);
					psTzExcelDrxxT.setTzIsViewAtt("Y");
					psTzExcelDrxxTMapper.insert(psTzExcelDrxxT);
					
					//生产打包文件附件存放的路径;
					SimpleDateFormat dateFormate = new SimpleDateFormat("yyyyMMdd");
				    String s_dt = dateFormate.format(new Date());
					int XUHAO = getSeqNum.getSeqNum("TZ_ZLDB_AE", "TZ_PACKAGE_ID");
					String ID = "000000000" + String.valueOf(XUHAO);
					ID = ID.substring(ID.length()-9,ID.length());
					
					String fjlj = "";
					String websiteDir = getSysHardCodeVal.getWebsiteFileUploadPath();
					if(websiteDir.lastIndexOf("/")+1 == websiteDir.length()){
						fjlj = websiteDir + currentOprid + "/" + s_dt + "/" + "pack" + "/" + ID;
					}else{
						fjlj = websiteDir + "/" + currentOprid + "/" + s_dt + "/" + "pack" + "/" + ID;
					}
				     
					PsTzExcelDattT psTzExcelDattT = new PsTzExcelDattT();
					psTzExcelDattT.setProcessinstance(processInstance);
					psTzExcelDattT.setTzSysfileName(fileName);
					psTzExcelDattT.setTzFileName(fileName);
					psTzExcelDattT.setTzCfLj("A");
					psTzExcelDattT.setTzFjRecName("TZ_APPFATTACH_T");
					psTzExcelDattT.setTzFwqFwlj(fjlj);
					psTzExcelDattTMapper.insert(psTzExcelDattT);
					

					String[] appids = comParams.split(";");
					for(int i = 0; i < appids.length; i++){
						String appInsID = appids[i];
						String OPRID = jdbcTemplate.queryForObject("SELECT OPRID FROM PS_TZ_FORM_WRK_T A WHERE A.TZ_APP_INS_ID = ?", new Object[]{Long.parseLong(appInsID)},"String");
						String TZ_APP_TPL_ID = jdbcTemplate.queryForObject("SELECT TZ_APP_TPL_ID FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID = ?", new Object[]{Long.parseLong(appInsID)},"String");
						tzDealWithXMLServiceImpl.replaceXMLPulish(appInsID, OPRID, TZ_APP_TPL_ID, true,fjlj + "/" + appInsID, errMsg);
					}
				}

			}
		} catch (Exception e) {
				errMsg[0] = "1";
				errMsg[1] = e.toString();
		}

		return strComContent;
	}
	
	
}
