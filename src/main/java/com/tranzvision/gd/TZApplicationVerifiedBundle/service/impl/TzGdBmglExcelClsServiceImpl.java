package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzExcelDattTMapper;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 原PS类：TZ_GD_BMGL_BMBSH_PKG:TZ_GD_BMGL_EXCEL_CLS
 * @author tang
 * 报名管理-导出到Excel
 */
@Service("com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl.TzGdBmglExcelClsServiceImpl")
public class TzGdBmglExcelClsServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsTzExcelDattTMapper psTzExcelDattTMapper;
	
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
			String[] resultFldArray = {"TZ_DR_TASK_DESC", "TZ_REALNAME", "TZ_STARTTIME_CHAR", "PROCESSINSTANCE", "DESCR", "TZ_FWQ_FWLJ" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("fileName", rowList[0]);
					mapList.put("oprName", rowList[1]);
					mapList.put("oprTime", rowList[2]);
					mapList.put("processInstance", rowList[3]);
					mapList.put("applicationEngineStatus", rowList[4]);
					mapList.put("fileUrl", rowList[5]);
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
	
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (actData.length == 0) {
			return strRet;
		}

		try {

			for (int num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 报名表模板ID;
				String appFormModalID = jacksonUtil.getString("appFormModalID");
				// Excel导出模板ID;
				String excelTpl = jacksonUtil.getString("excelTpl");
				// Excel名称;
				String excelName = jacksonUtil.getString("excelName");
				
				//报名表编号;
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> appInsIdArray = (List<Map<String, Object>>)jacksonUtil.getList("applicantsList");
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	/***TZ_GD_DCE_AE***/
	public void tzGdDceAe(String runcntlId){
		String sql = "SELECT TZ_AUD_LIST ,RUN_CNTL_ID ,TZ_APP_TPL_ID ,TZ_EXPORT_TMP_ID ,TZ_EXCEL_NAME FROM PS_TZ_BMB_DCE_T WHERE RUN_CNTL_ID= ?";
		Map<String, Object> map = jdbcTemplate.queryForMap(sql,new Object[]{runcntlId});
		if(map != null){
			String excelName = (String)map.get("TZ_EXCEL_NAME");
			String excelTpl = (String)map.get("TZ_EXPORT_TMP_ID");
			String appFormModalID = (String)map.get("TZ_APP_TPL_ID");
			String appInsIdList = (String)map.get("TZ_AUD_LIST");
			
			
			/*将文件上传之前，先重命名该文件*/
			Date dt = new Date();
			SimpleDateFormat dateFormate = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
			String sDtUrl = dateFormate.format(dt);
			String sDttm = datetimeFormate.format(dt);
			
			String strUseFileName = sDttm + "_" + excelName + "." + "xls";
			
			/*判断给定的URL地址是否以“/”结尾，若不是以"/"结尾，在URL地址后添加字符串“/”*/
			//String sDirUrl = GetURL(URL.TZ_GD_EXPORT_EXCEL_URL);
			String sDirUrlSQL = "select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?";
			String sDirUrl = jdbcTemplate.queryForObject(sDirUrlSQL, new Object[] { "TZ_GD_EXPORT_EXCEL_URL" },"String");
			if(sDirUrl == null || "".equals(sDirUrl)){
				return;
			}
			String fileSeparator = File.separator;
			if((sDirUrl.lastIndexOf(fileSeparator)+1) == sDirUrl.length()){
				/*donothing*/
			}else{
				sDirUrl = sDirUrl + fileSeparator;
			}
			
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			/*创建一个新的目录*/
			sDirUrl = sDirUrl + oprid + fileSeparator + sDtUrl + fileSeparator;
		
			File dir =new File(sDirUrl);    
			//如果文件夹不存在则创建    
			if (!dir.exists())      
			{        
				dir.mkdir();    
			}
			
			ArrayList<String> arr_title = new ArrayList<>();
			arr_title.add("序号");
			
			String sqlExcelTpl = "SELECT TZ_DC_FIELD_ID,TZ_DC_FIELD_NAME,TZ_DC_FIELD_FGF FROM PS_TZ_EXP_FRMFLD_T WHERE TZ_EXPORT_TMP_ID=? ORDER BY TZ_SORT_NUM ASC";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlExcelTpl,new Object[]{excelTpl});
			String strFieldID = "", strFieldName = "", strFieldSep = "";
			// 存储模板字段的数组;
			ArrayList<String[]> arrExcelTplField = new ArrayList<>();
			if(list != null && list.size() > 0){
				for(int i = 0; i<list.size(); i++){
					strFieldID = (String)list.get(i).get("TZ_DC_FIELD_ID");
					strFieldName = (String)list.get(i).get("TZ_DC_FIELD_NAME");
					strFieldSep = (String)list.get(i).get("TZ_DC_FIELD_FGF");
					String[] str = new String[]{strFieldID, strFieldName, strFieldSep};
					arrExcelTplField.add(str);
				}
			}
			
			/*将需要导出报名表的编号存入数组*/
			String[] arrAppInsID = appInsIdList.split(",");
			int row_count = arrAppInsID.length; /*导出的数据的行数*/
			
			/* 参数分别代表的意思
			参数1：包含数据的数组对象
			参数2：当前行行数
			参数3：当前行行数
			参数4：需要导出的数据的总行数
			参数5：打开的Excel文件对象
			*/
			//TODO
			//dealWithExcel.crExcel(arr_title, 1, 1, row_count + 1, wwb);
			/*生成Excel标题*/
			ArrayList<String> arr_data = new ArrayList<>();

			// Hardcode定义取值视图;
			String appFormInfoViewSQL = "select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?";
			String appFormInfoView = jdbcTemplate.queryForObject(appFormInfoViewSQL, new Object[] {"TZ_FORM_VAL_REC"},"String");
		
			for(int i = 0; i < row_count; i++){
				arr_data.add(String.valueOf(i));
				/*根据模板配置取出每个报名表的数据*/
				for(int j = 0 ; j < arrExcelTplField.size(); j ++){
					String sqlAppFormField = "SELECT A.TZ_FORM_FLD_ID,A.TZ_CODE_TABLE_ID,B.TZ_XXX_CCLX,B.TZ_COM_LMC,B.TZ_XXX_NO FROM PS_TZ_FRMFLD_GL_T A ,PS_TZ_FORM_FIELD_V B WHERE B.TZ_APP_TPL_ID=? AND B.TZ_XXX_BH =A.TZ_FORM_FLD_ID AND A.TZ_EXPORT_TMP_ID=? AND A.TZ_DC_FIELD_ID=? ORDER BY A.TZ_SORT_NUM ASC";
					List<Map<String, Object>> appFormFieldList = jdbcTemplate.queryForList(sqlAppFormField,new Object[]{appFormModalID, excelTpl, arrExcelTplField.get(j)[0]});
					String strAppFormField = "", strCodeTable = "", strSaveType = "", strComClassName = "", strInfoSelectID = ""; /*报名表字段，码表，存储类型，控件类名称，下拉存储描述信息项编号*/
				    String strAppFormFieldValues = "";
				    if(appFormFieldList != null && list.size() > 0){
				    	for(int k = 0; k < appFormFieldList.size(); k++){
				    		strAppFormField = (String)appFormFieldList.get(k).get("TZ_FORM_FLD_ID");
				    		strCodeTable = (String)appFormFieldList.get(k).get("TZ_CODE_TABLE_ID");
				    		strSaveType = (String)appFormFieldList.get(k).get("TZ_XXX_CCLX");
				    		strComClassName = (String)appFormFieldList.get(k).get("TZ_COM_LMC");
				    		strInfoSelectID = (String)appFormFieldList.get(k).get("TZ_XXX_NO");
				    		
				    		String strAppFormFieldValue = "";
				    		String strSelectField = "";
				    		//strSelectField执行sql是否要传参数;
				    		boolean strSelectFieldBoolean = false;
				             
				            boolean isSingleValue = true; /*一个信息项是否对应单个值*/
				            boolean isRecordValue = false; /*是否从Hardcode定义的表中取值*/
				            
				            //是否其他类型
				            boolean strSaveTypeBoolean = true;
				            /*短文本*/
				            if("S".equals(strSaveType)){
				            	strSaveTypeBoolean = false;
				            	
				            	if("bmr".equals(strComClassName.substring(0, 3))){
				            		strSelectField = "TZ_APP_L_TEXT"; /*报名人相关控件取值从长字符串取描述*/
				            	}else{
				            		if(strCodeTable == null || "".equals(strCodeTable)){
				            			strSelectField = "TZ_APP_S_TEXT";
				            		}else{
				            			strSelectField = "(SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=? AND TZ_ZHZ_ID=A.TZ_APP_S_TEXT AND TZ_EFF_STATUS<>'I' AND TZ_EFF_DATE<=now())";
				            			strSelectFieldBoolean = true;
				            		}
				            	} 
				            }
				            /*长文本*/
				            if("L".equals(strSaveType)){
				            	strSaveTypeBoolean = false;
				            	if(strCodeTable == null || "".equals(strCodeTable)){
				            		strSelectField = "TZ_APP_L_TEXT";
			            		}else{
			            			strSelectField = "(SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=? AND TZ_ZHZ_ID=A.TZ_APP_L_TEXT AND TZ_EFF_STATUS<>'I' AND TZ_EFF_DATE<=now())";
			            			strSelectFieldBoolean = true;
			            		}
				            }
				            /*可选框*/
				            if("D".equals(strSaveType)){
				            	strSaveTypeBoolean = false;
				            	
				            	isSingleValue = false;
				            }
				            /*表存储*/
				            if("R".equals(strSaveType)){
				            	strSaveTypeBoolean = false;
				            	
				            	isSingleValue = false;
				                isRecordValue = true;
				            }
				            /*其他值*/
				            if(strSaveTypeBoolean){
				            	if(strCodeTable == null || "".equals(strCodeTable)){
				            		strSelectField = "TZ_APP_S_TEXT";
			            		}else{
			            			strSelectField = "(SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=? AND TZ_ZHZ_ID=A.TZ_APP_S_TEXT AND TZ_EFF_STATUS<>'I' AND TZ_EFF_DATE <= now())";
			            			strSelectFieldBoolean = true;
			            		}
				            }
				            
				            String sql1 = "";
				            if(isSingleValue){
				            	sql1 = "SELECT " + strSelectField + " FROM PS_TZ_APP_CC_T A WHERE TZ_APP_INS_ID=? AND TZ_XXX_BH=?";
				            	if(strSelectFieldBoolean == true){
				            		strAppFormFieldValue = jdbcTemplate.queryForObject(sql1, new Object[]{strCodeTable,Long.parseLong(arrAppInsID [i]), strAppFormField},"String");
				            	}else{
				            		strAppFormFieldValue = jdbcTemplate.queryForObject(sql1, new Object[]{Long.parseLong(arrAppInsID [i]), strAppFormField},"String");
				            	}
				            	if("Select".equals(strComClassName)
				            			|| "CompanyNature".equals(strComClassName)
				            			|| "Degree".equals(strComClassName)
				            			|| "Diploma".equals(strComClassName)){
				            		strAppFormFieldValue = jdbcTemplate.queryForObject("SELECT TZ_XXXKXZ_MS FROM PS_TZ_APPXXX_KXZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_BH = ? AND TZ_XXXKXZ_MC = ?", new Object[]{appFormModalID, strInfoSelectID, strAppFormFieldValue},"String");
				            	
				            	}
				            	
				            	if("bmr".equals(strComClassName.substring(0, 3))){
				            		String sql2 = "SELECT " + strSelectField.replaceAll("TZ_APP_S_TEXT", "TZ_APP_L_TEXT") + " FROM PS_TZ_APP_CC_T A WHERE TZ_APP_INS_ID=? AND TZ_XXX_BH=?";
				            		if(strSelectFieldBoolean == true){
				            			strAppFormFieldValue = jdbcTemplate.queryForObject(sql2, new Object[]{strCodeTable,Long.parseLong(arrAppInsID [i]), strAppFormField},"String");
				            		}else{
				            			strAppFormFieldValue = jdbcTemplate.queryForObject(sql2, new Object[]{Long.parseLong(arrAppInsID [i]), strAppFormField},"String");
				            		}
				            	}
				            }else{
				            	if(isRecordValue){
				            		if(appFormInfoView != null && !"".equals(appFormInfoView)){
				            			 String sql3 = "SELECT " + strAppFormField + " FROM " + appFormInfoView + " WHERE TZ_APP_INS_ID=?";
				            			 strAppFormFieldValue = jdbcTemplate.queryForObject(sql3,new Object[]{Long.parseLong(arrAppInsID [i])},"String");
				            			 if(strCodeTable != null && !"".equals(strCodeTable)){
				            				 strAppFormFieldValue = jdbcTemplate.queryForObject("SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=? AND TZ_ZHZ_ID=? AND TZ_EFF_STATUS<>'I' AND TZ_EFF_DATE<=now()", new Object[]{strCodeTable, strAppFormFieldValue},"String");
				            			 }
				            		}
				            	}else{
				            		// 多选框和单选框;
				            		String sqlMutilValue = "SELECT TZ_APP_S_TEXT,TZ_KXX_QTZ FROM PS_TZ_APP_DHCC_T WHERE TZ_APP_INS_ID=? AND TZ_XXX_BH=? AND TZ_IS_CHECKED='Y'";
				            		String strCheckBoxRadioValue = "", strCheckBoxRadioOtherValue="";
				            		List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sqlMutilValue,new Object[]{Long.parseLong(arrAppInsID [i]), strAppFormField});
				            		if(list2 != null && list2.size() > 0){
				            			for(int h = 0; h < list2.size(); h++){
				            				/*如果有可选项其他值则取该值*/
				            				if(strCheckBoxRadioOtherValue != null && !"".equals(strCheckBoxRadioOtherValue)){
				            					strCheckBoxRadioValue = strCheckBoxRadioOtherValue;
				            				}
				            				
				            				if(strAppFormFieldValue == null || "".equals(strAppFormFieldValue)){
				            					strAppFormFieldValue = strCheckBoxRadioValue;
				            				}else{
				            					strAppFormFieldValue = strAppFormFieldValue + "," + strCheckBoxRadioValue;
				            				}
				            			}
				            		}
				            	}
				            }
				            
				            /*拼装报名表字段值*/
				            if(strAppFormFieldValues == null || "".equals(strAppFormFieldValues)){
				            	strAppFormFieldValues = strAppFormFieldValue;
				            }
				            if(arrExcelTplField.get(j)[2] != null && !"".equals(arrExcelTplField.get(j)[2])){
				            	strAppFormFieldValues = strAppFormFieldValues +  arrExcelTplField.get(j)[2] + strAppFormFieldValue;
				            }else{
				            	strAppFormFieldValues = strAppFormFieldValues + "," + strAppFormFieldValue;
				            }
				            
				    	}
				    	 
				    }
				    
				    arr_data.add(strAppFormFieldValues);
				}
				//TODO
				//dealWithExcel.crExcel(arr_data, i + 1, i + 1, row_count + 1, wwb);
			}
			
			/*关闭打开的文件 TODO*/
			//dealWithExcel.closeFile(&wwb);
			
			String urlExcel = "/linkfiles/exportExcel/" + oprid + "/" + sDtUrl + "/" + strUseFileName;
			/* TODO
			psTzExcelDattTMapper.selectByPrimaryKey(processinstance)
			Local Record &TZ_EXCEL_DATT_T = CreateRecord(Record.TZ_EXCEL_DATT_T);
			&TZ_EXCEL_DATT_T.PROCESSINSTANCE.Value = TZ_BMB_DCE_AET.PROCESS_INSTANCE;
			If &TZ_EXCEL_DATT_T.SelectByKey() Then
			   &TZ_EXCEL_DATT_T.PROCESSINSTANCE.Value = TZ_BMB_DCE_AET.PROCESS_INSTANCE;
			   &TZ_EXCEL_DATT_T.TZ_SYSFILE_NAME.Value = &strExcelID;
			   &TZ_EXCEL_DATT_T.TZ_FILE_NAME.Value = &excelName;
			   &TZ_EXCEL_DATT_T.TZ_CF_LJ.Value = "A";
			   &TZ_EXCEL_DATT_T.TZ_FJ_REC_NAME.Value = "TZ_APP_CC_T";
			   &TZ_EXCEL_DATT_T.TZ_FWQ_FWLJ.Value = &urlExcel;
			   &TZ_EXCEL_DATT_T.Update();
			End-If;
			*/
			
			
		}
	}

}
