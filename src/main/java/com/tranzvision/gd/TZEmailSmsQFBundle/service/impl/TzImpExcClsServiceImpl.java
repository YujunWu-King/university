package com.tranzvision.gd.TZEmailSmsQFBundle.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzExcSetTblMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzMlsmDrnrTMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzExcSetTbl;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzMlsmDrnrT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.poi.excel.ExcelHandle;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author tang TZ_SMSEML_IMPEXC_PKG:TZ_IMPEXC_CLS
 */
@Service("com.tranzvision.gd.TZEmailSmsQFBundle.service.impl.TzImpExcClsServiceImpl")
public class TzImpExcClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private GetSeqNum getSeqNum;
	
	@Autowired
	private PsTzMlsmDrnrTMapper psTzMlsmDrnrTMapper;

	@Autowired
	private PsTzExcSetTblMapper psTzExcSetTblMapper;

	@Override
	public String tzOther(String OperateType, String comParams, String[] errorMsg) {
		String returnString = "";
		if ("tzAnalysisExcel".equals(OperateType)) {
			returnString = this.tzAnalysisExcel(comParams, errorMsg);
		}
		if("tzSetExcelHeader".equals(OperateType)){
			returnString = this.tzSetExcelHeader(comParams, errorMsg);
		}
		if("tzSaveExcelData".equals(OperateType)){
			returnString = this.tzSaveExcelData(comParams, errorMsg);
		}
		return returnString;
	}

	/* 解析导入的Excel */
	private String tzAnalysisExcel(String comParams, String[] errorMsg) {
		// 返回值;
		ArrayList<ArrayList<String>> list1 = new ArrayList<>();
		ArrayList<String> list2 = new ArrayList<>();

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		if (jacksonUtil.containsKey("path") && jacksonUtil.containsKey("sysFileName")) {
			String strPath = jacksonUtil.getString("path");
			String strFileName = jacksonUtil.getString("sysFileName");
			if (strPath != null && !"".equals(strPath) && strFileName != null && !"".equals(strFileName)) {
				String uploadPath = strPath;
				String dataFilePath = "";
				if (uploadPath.lastIndexOf("/") + 1 == uploadPath.length()) {
					dataFilePath = uploadPath + strFileName;
				} else {
					dataFilePath = uploadPath + "/" + strFileName;
				}

				List<String> dataListCellKeys = new ArrayList<String>();

				ExcelHandle excelHandle = new ExcelHandle(request);
				excelHandle.readExcel(dataFilePath, dataListCellKeys, false);

				ArrayList<Map<String, Object>> listData = excelHandle.getExcelListData();

				if (listData != null && listData.size() > 0) {
					for (int i = 0; i < listData.size(); i++) {
						list2 = new ArrayList<>();
						Map<String, Object> map = listData.get(i);
						for (Map.Entry entry : map.entrySet()) {
							String value = entry.getValue().toString();
							list2.add(value);
						}
						list1.add(list2);
					}
				}

				String deleteFilePath = strPath;
				if (deleteFilePath.lastIndexOf("/") + 1 == deleteFilePath.length()) {
					deleteFilePath = deleteFilePath + strFileName;
				} else {
					deleteFilePath = deleteFilePath + "/" + strFileName;
				}

				String relPath = request.getServletContext().getRealPath(deleteFilePath);
				File file = new File(relPath);
				if (file.exists() && file.isFile()) {
					file.delete();
				}
			}

		}

		return jacksonUtil.List2json(list1);
	}

	/* 解析导入的Excel */
	private String tzSetExcelHeader(String comParams, String[] errorMsg) {
		
		//返回值;
		ArrayList<Map<String, Object>> returnList = new ArrayList<>();
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		if (jacksonUtil.containsKey("sendPcId") && jacksonUtil.containsKey("headerData")) {
			// 群发任务批次;
			String sendPcId = jacksonUtil.getString("sendPcId");
			// Excel标题行数据;
			List<?> list = jacksonUtil.getList("headerData");
			int count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM PS_TZ_EXC_SET_TBL WHERE TZ_MLSM_QFPC_ID=?",
					new Object[] { sendPcId }, "Integer");
			if (count > 0) {
				jdbcTemplate.update("DELETE FROM PS_TZ_EXC_SET_TBL WHERE TZ_MLSM_QFPC_ID=?", new Object[] { sendPcId });
			}

			boolean afterNrField = false;
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					@SuppressWarnings("unchecked")
					Map<String, Object> map = (Map<String, Object>) list.get(i);
					String title = (String) map.get("title");
					String set = (String) map.get("set");
					if (set == null || "null".equals(set.toLowerCase())) {
						set = "";
					}

					PsTzExcSetTbl psTzExcSetTbl = new PsTzExcSetTbl();
					psTzExcSetTbl.setTzMlsmQfpcId(sendPcId);
					psTzExcSetTbl.setTzIndex(i + 1);
					psTzExcSetTbl.setTzXxxName(title);
					psTzExcSetTbl.setTzXxxType(set);

					String filedName = "";
					if ("D".equals(set)) {
						afterNrField = true;
						filedName = "TZ_MLSM_CONTENT";
					} else {
						if (afterNrField == false) {
							filedName = "TZ_XXX_NR" + (i + 1);
						} else {
							filedName = "TZ_XXX_NR" + i;
						}
					}
					psTzExcSetTbl.setTzFieldName(filedName);
					
					psTzExcSetTblMapper.insert(psTzExcSetTbl);
					
					Map<String, Object> returnMap = new HashMap<>();
					returnMap.put("header", title);
					returnMap.put("sendType",  set);
					returnMap.put("storeField", filedName);
					returnList.add(returnMap);
				}
			}
		}
		return jacksonUtil.List2json(returnList);
	}
	
	/* 解析导入的Excel */
	private String tzSaveExcelData(String comParams, String[] errorMsg) {
		//返回值
		Map<String, Object> returnMap = new HashMap<>();
		ArrayList<String> strMailOrPhone = new ArrayList<>();
		returnMap.put("recever", strMailOrPhone);
		returnMap.put("subject", "");
		returnMap.put("content", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		if(jacksonUtil.containsKey("sendPcId") && jacksonUtil.containsKey("sendType") 
			&& jacksonUtil.containsKey("data")){
			// 首封短信邮件内容;
		    String firstContent = "";
		    String firstSubject = "";
		    
		    String sendPcId = jacksonUtil.getString("sendPcId");
		    //发送类型，SMS-短信群发，MAL-邮件群发
		    //String sendType = jacksonUtil.getString("sendType");
		    // Excel数据;
		    List<?> list = jacksonUtil.getList("data");
		    
		    //手机或者邮箱存储字段
		    String storeFileName = jdbcTemplate.queryForObject("SELECT TZ_FIELD_NAME FROM PS_TZ_EXC_SET_TBL WHERE TZ_MLSM_QFPC_ID=? AND TZ_XXX_TYPE='B'", new Object[]{sendPcId},"String");
		    //邮件主题或短信签名存储字段
		    String subjectFileName = jdbcTemplate.queryForObject("SELECT TZ_FIELD_NAME FROM PS_TZ_EXC_SET_TBL WHERE TZ_MLSM_QFPC_ID=? AND TZ_XXX_TYPE='C'", new Object[]{sendPcId},"String");
		    
		    jdbcTemplate.update("DELETE FROM PS_TZ_MLSM_DRNR_T WHERE TZ_MLSM_QFPC_ID=?",new Object[]{sendPcId});

		    if(list != null && list.size()>0){
		    	for(int i=0; i<list.size(); i++){
		    		@SuppressWarnings("unchecked")
					Map<String, Object> map = (Map<String, Object>)list.get(i);
		    		
		    		String emailOrPhone = "";
		    		String subject = "";
		             
		    		if(storeFileName != null && !"".equals(storeFileName)){
		    			emailOrPhone = (String)map.get(storeFileName); 
		            }
		            
		            if(subjectFileName != null && !"".equals(subjectFileName)){
		            	 subject = (String)map.get(subjectFileName); 
		            }
		            
		            if(emailOrPhone != null && !"".equals(emailOrPhone)){
		            	PsTzMlsmDrnrT psTzMlsmDrnrT = new PsTzMlsmDrnrT();
		            	psTzMlsmDrnrT.setTzMlsmQfpcId(sendPcId);
		            	String audCyId = String.valueOf(getSeqNum.getSeqNum("TZ_AUDCYUAN_T", "TZ_AUDCY_ID"));
		            	psTzMlsmDrnrT.setTzAudcyId(audCyId);
		            	
		            	if(map.containsKey("TZ_MLSM_CONTENT")){
		            		psTzMlsmDrnrT.setTzMlsmContent((String)map.get("TZ_MLSM_CONTENT"));
		            	}
		            	
		            	if(map.containsKey("TZ_XXX_NR1")){
		            		psTzMlsmDrnrT.setTzXxxNr1((String)map.get("TZ_XXX_NR1"));
		            	}
		            	
		            	if(map.containsKey("TZ_XXX_NR2")){
		            		psTzMlsmDrnrT.setTzXxxNr2((String)map.get("TZ_XXX_NR2"));
		            	}
		            	
		            	if(map.containsKey("TZ_XXX_NR3")){
		            		psTzMlsmDrnrT.setTzXxxNr3((String)map.get("TZ_XXX_NR3"));
		            	}
		            	
		            	if(map.containsKey("TZ_XXX_NR4")){
		            		psTzMlsmDrnrT.setTzXxxNr4((String)map.get("TZ_XXX_NR4"));
		            	}
		            	
		            	if(map.containsKey("TZ_XXX_NR5")){
		            		psTzMlsmDrnrT.setTzXxxNr5((String)map.get("TZ_XXX_NR5"));
		            	}
		            	
		            	if(map.containsKey("TZ_XXX_NR6")){
		            		psTzMlsmDrnrT.setTzXxxNr6((String)map.get("TZ_XXX_NR6"));
		            	}
		            	
		            	if(map.containsKey("TZ_XXX_NR7")){
		            		psTzMlsmDrnrT.setTzXxxNr7((String)map.get("TZ_XXX_NR7"));
		            	}
		            	
		            	if(map.containsKey("TZ_XXX_NR8")){
		            		psTzMlsmDrnrT.setTzXxxNr8((String)map.get("TZ_XXX_NR8"));
		            	}
		            	
		            	if(map.containsKey("TZ_XXX_NR9")){
		            		psTzMlsmDrnrT.setTzXxxNr9((String)map.get("TZ_XXX_NR9"));
		            	}
		            	
		            	if(map.containsKey("TZ_XXX_NR10")){
		            		psTzMlsmDrnrT.setTzXxxNr10((String)map.get("TZ_XXX_NR10"));
		            	}
		            	
		            	if(map.containsKey("TZ_XXX_NR11")){
		            		psTzMlsmDrnrT.setTzXxxNr11((String)map.get("TZ_XXX_NR11"));
		            	}
		            	
		            	if(map.containsKey("TZ_XXX_NR12")){
		            		psTzMlsmDrnrT.setTzXxxNr12((String)map.get("TZ_XXX_NR12"));
		            	}
		            	
		            	if(map.containsKey("TZ_XXX_NR13")){
		            		psTzMlsmDrnrT.setTzXxxNr13((String)map.get("TZ_XXX_NR13"));
		            	}
		            	
		            	if(map.containsKey("TZ_XXX_NR14")){
		            		psTzMlsmDrnrT.setTzXxxNr14((String)map.get("TZ_XXX_NR14"));
		            	}
		            	
		            	if(map.containsKey("TZ_XXX_NR15")){
		            		psTzMlsmDrnrT.setTzXxxNr15((String)map.get("TZ_XXX_NR15"));
		            	}
		            	
		            	if(map.containsKey("TZ_XXX_NR16")){
		            		psTzMlsmDrnrT.setTzXxxNr16((String)map.get("TZ_XXX_NR16"));
		            	}
		            	
		            	if(map.containsKey("TZ_XXX_NR17")){
		            		psTzMlsmDrnrT.setTzXxxNr17((String)map.get("TZ_XXX_NR17"));
		            	}
		            	
		            	if(map.containsKey("TZ_XXX_NR18")){
		            		psTzMlsmDrnrT.setTzXxxNr18((String)map.get("TZ_XXX_NR18"));
		            	}
		            	
		            	if(map.containsKey("TZ_XXX_NR19")){
		            		psTzMlsmDrnrT.setTzXxxNr19((String)map.get("TZ_XXX_NR19"));
		            	}
		            	
		            	int bl = psTzMlsmDrnrTMapper.insert(psTzMlsmDrnrT);
		            	if(bl > 0){
		            		if("".equals(firstContent) && map.containsKey("TZ_MLSM_CONTENT")){
		            			firstContent = (String)map.get("TZ_MLSM_CONTENT");
		            		}
		            		
		            		if("".equals(firstSubject)){
		            			firstSubject = subject;
		            		}
		            	}
		            	
		            	strMailOrPhone.add(emailOrPhone);
		            	
		            	returnMap.replace("recever", strMailOrPhone);
		            	returnMap.replace("subject", firstSubject);
		            	returnMap.replace("content", firstContent);
		            }

		    	}
		    }
		}
		
		return jacksonUtil.Map2json(returnMap);
	}
}
