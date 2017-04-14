package com.tranzvision.gd.TZApplicationExportTemplateBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZApplicationExportTemplateBundle.dao.PsTzExportTmpTMapper;
import com.tranzvision.gd.TZApplicationExportTemplateBundle.dao.PsTzExpFrmFldTMapper;
import com.tranzvision.gd.TZApplicationExportTemplateBundle.dao.PsTzFrmFldGlTMapper;
import com.tranzvision.gd.TZApplicationExportTemplateBundle.model.PsTzExportTmpT;
import com.tranzvision.gd.TZApplicationExportTemplateBundle.model.PsTzExpFrmFldT;
import com.tranzvision.gd.TZApplicationExportTemplateBundle.model.PsTzFrmFldGlT;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author 张彬彬; 
 * 功能说明：报名表导出模板详细信息相关类;
 * 原PS类：TZ_GD_BMGL_DCMB_PKG:TZ_GD_TPL_INFO_CLS
 */
@Service("com.tranzvision.gd.TZApplicationExportTemplateBundle.service.impl.appExportTplInfoServiceImpl")
public class appExportTplInfoServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzExportTmpTMapper PsTzExportTmpTMapper;
	@Autowired
	private PsTzExpFrmFldTMapper PsTzExpFrmFldTMapper;
	@Autowired
	private PsTzFrmFldGlTMapper PsTzFrmFldGlTMapper;
	
	/* 查询报名表导出模板字段 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		jacksonUtil.json2Map(comParams);
		
		String strAppExportTplId = String.valueOf(jacksonUtil.getString("tplID"));
		
		String strAppModalId = String.valueOf(jacksonUtil.getString("modalID"));

		ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
		try{
			int total = 0;
			// 查询总数;
			String totalSQL = "SELECT COUNT(1) FROM PS_TZ_EXP_FRMFLD_T WHERE TZ_EXPORT_TMP_ID=?";
			total = sqlQuery.queryForObject(totalSQL, new Object[] { strAppExportTplId },"Integer");
			String sql = "SELECT TZ_DC_FIELD_ID,TZ_DC_FIELD_NAME,TZ_APPCLS_ID,TZ_DC_FIELD_FGF ,TZ_SORT_NUM,TZ_DC_COL_WIDTH,TZ_DC_COL_FILTER FROM PS_TZ_EXP_FRMFLD_T  WHERE TZ_EXPORT_TMP_ID= ? ORDER BY TZ_SORT_NUM";
			List<?> listData = sqlQuery.queryForList(sql, 
					new Object[] { strAppExportTplId });
			for (Object objData : listData) {

				Map<String, Object> mapData = (Map<String, Object>) objData;
				String strFieldId = "";
				String strFieldName = "";
				String strAppClass = "";
				String strFieldSep = "";
				String strSortNum = "";
				String strColumnWidth = "";
				String strColumnFilter = "";
			
				strFieldId = String.valueOf(mapData.get("TZ_DC_FIELD_ID"));
				strFieldName = String.valueOf(mapData.get("TZ_DC_FIELD_NAME"));
				strAppClass = String.valueOf(mapData.get("TZ_APPCLS_ID"));
				strFieldSep = String.valueOf(mapData.get("TZ_DC_FIELD_FGF"));
				strSortNum = String.valueOf(mapData.get("TZ_SORT_NUM"));
				strColumnWidth = String.valueOf(mapData.get("TZ_DC_COL_WIDTH"));
				strColumnFilter = String.valueOf(mapData.get("TZ_DC_COL_FILTER"));
				
				//宽度默认100
				if("".equals(strColumnWidth) || strColumnWidth == null){
					strColumnWidth = "100";
				}
				//筛选类型默认字符串
				if("".equals(strColumnFilter) || strSortNum == null){
					strSortNum = "string";
				}
				
				//获取导出字段对应的报名表字段;
				
				ArrayList<Map<String, Object>> listJsonGlField = new ArrayList<Map<String, Object>>();
				String sqlGetGlField = "SELECT TZ_FORM_FLD_ID,(SELECT TZ_XXX_MC FROM PS_TZ_TEMP_FIELD_V WHERE TZ_APP_TPL_ID= ? AND TZ_XXX_BH=A.TZ_FORM_FLD_ID) AS TZ_XXX_MC, TZ_CODE_TABLE_ID,(SELECT TZ_ZHZJH_MS FROM PS_TZ_PT_ZHZJH_TBL WHERE TZ_ZHZJH_ID=A.TZ_CODE_TABLE_ID) AS TZ_CODE_TABLE_NAME,TZ_SORT_NUM FROM PS_TZ_FRMFLD_GL_T A WHERE TZ_EXPORT_TMP_ID= ? AND TZ_DC_FIELD_ID= ?";
				List<?> listDataGlField = sqlQuery.queryForList(sqlGetGlField, 
						new Object[] { strAppModalId,strAppExportTplId,strFieldId });
				for (Object objDataGlField : listDataGlField) {
					Map<?, Object> mapDataGlField = (Map<?, Object>) objDataGlField;
					String strFieldIdGl = String.valueOf(mapDataGlField.get("TZ_FORM_FLD_ID"));
					String strFieldNameGl = String.valueOf(mapDataGlField.get("TZ_XXX_MC"));
					String strFieldGlTable = String.valueOf(mapDataGlField.get("TZ_CODE_TABLE_ID"));
					Object objFieldGlTableName = mapDataGlField.get("TZ_CODE_TABLE_NAME");
					String strFieldGlTableName;
					if(objFieldGlTableName == null) {
						strFieldGlTableName = "";
					}else{
						strFieldGlTableName = String.valueOf(objFieldGlTableName);
					}
					String strSortNumGl = String.valueOf(mapDataGlField.get("TZ_SORT_NUM"));
					Map<String, Object> mapJsonGlField = new HashMap<String, Object>();
					mapJsonGlField.put("appFormField", strFieldIdGl);
					mapJsonGlField.put("appFormFieldName", strFieldNameGl);
					mapJsonGlField.put("codeTable", strFieldGlTable);
					mapJsonGlField.put("codeTableName", strFieldGlTableName);
					mapJsonGlField.put("appFormFieldSeq", strSortNumGl);
					listJsonGlField.add(mapJsonGlField);
				}
				//
				Map<String, Object> mapJson = new HashMap<String, Object>();
				mapJson.put("tplID", strAppExportTplId);
				mapJson.put("fieldID", strFieldId);
				mapJson.put("fieldName", strFieldName);
				mapJson.put("appClass", strAppClass);
				mapJson.put("fieldSeq", strSortNum);
				mapJson.put("separator", strFieldSep);
				mapJson.put("columnWidth", strColumnWidth);
				mapJson.put("filter", strColumnFilter);
				
				mapJson.put("appFormField",listJsonGlField);

				listJson.add(mapJson);
			}
			mapRet.replace("total",total);
			mapRet.replace("root", listJson);
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return jacksonUtil.Map2json(mapRet);
	}
	 
	/**
	 * 新增报名表导出模版
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> mapData = jacksonUtil.getMap("data");

				if ("TPL".equals(typeFlag)) {

					String strAppExportTplId = String.valueOf(mapData.get("tplID"));
	
					//查看模版编号是否已经存在*
					String sqlExistAppExportTplId = "SELECT count(1) FROM PS_TZ_EXPORT_TMP_T WHERE TZ_EXPORT_TMP_ID= ?";

					int isExistAppExportTplIdNum = sqlQuery.queryForObject(sqlExistAppExportTplId, new Object[] { strAppExportTplId }, "Integer");
					
					if (isExistAppExportTplIdNum == 0) {
						
						String strAppExportTplName = String.valueOf(mapData.get("tplName"));
						String strAppExportTplStatus = String.valueOf(mapData.get("tplStatus"));
						String strAppExportTplType = String.valueOf(mapData.get("tplType"));
						String strAppModalId = String.valueOf(mapData.get("modalID"));
						//查看模版名称石佛重复
						String sqlExistAppExportTplName = "SELECT count(1) FROM PS_TZ_EXPORT_TMP_T WHERE TZ_EXPORT_TMP_ID <> ? AND TZ_JG_ID= ? AND TZ_EXPORT_TMP_NAME=?";

						int isExistAppExportTplNameNum = sqlQuery.queryForObject(sqlExistAppExportTplName, 
								new Object[] { strAppExportTplId,orgid,strAppExportTplName}, "Integer");

						if(isExistAppExportTplNameNum == 0){
							int isExistAppExportTplTypeNum = 0;
							if(!"0".equals(strAppExportTplType) && "A".equals(strAppExportTplStatus)){
								String sqlExistAppExportTplType = "SELECT COUNT(1) FROM PS_TZ_EXPORT_TMP_T WHERE TZ_EXPORT_TMP_ID <> ? AND TZ_EXP_TMP_STATUS='A' AND TZ_APP_MODAL_ID=? AND TZ_EXPORT_TMP_TYPE=?";
								isExistAppExportTplTypeNum = sqlQuery.queryForObject(sqlExistAppExportTplType, 
										new Object[] { strAppExportTplId,strAppModalId,strAppExportTplType }, "Integer");
							}
							//当导出模版类型不为“Excel导出模版”时，且模版有效状态为有效时，检查导出模版类型是否唯一
							if(isExistAppExportTplTypeNum == 0){
								PsTzExportTmpT psTzExportTmpT = new PsTzExportTmpT();
								psTzExportTmpT.setTzExportTmpId(strAppExportTplId);
								psTzExportTmpT.setTzExportTmpName(strAppExportTplName);
								psTzExportTmpT.setTzExportTmpType(strAppExportTplType);
								psTzExportTmpT.setTzExpTmpStatus(strAppExportTplStatus);
								psTzExportTmpT.setTzAppModalId(strAppModalId);
								psTzExportTmpT.setTzJgId(orgid);
								psTzExportTmpT.setRowAddedDttm(new Date());
								psTzExportTmpT.setRowAddedOprid(oprid);
								psTzExportTmpT.setRowLastmantDttm(new Date());
								psTzExportTmpT.setRowLastmantOprid(oprid);
								
								PsTzExportTmpTMapper.insert(psTzExportTmpT);
							}else{
								errMsg[0] = "1";
								errMsg[1] = "报名表模版编号为：" + strAppModalId + "的报名表只允许有一个此类型的模板。";
							}
						}else{
							errMsg[0] = "1";
							errMsg[1] = "导出模板名称为：" + strAppExportTplName + "的信息已经存在，请修改导出模板名称。";
						}	
					}else{
						errMsg[0] = "1";
						errMsg[1] = "导出模板编号为：" + strAppExportTplId + "的信息已经存在，请修改导出模板编号。";
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	/**
	 * 更新报名表导出模版
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> mapData = jacksonUtil.getMap("data");

				if ("TPL".equals(typeFlag)) {

					String strAppExportTplId = String.valueOf(mapData.get("tplID"));
					String strAppExportTplName = String.valueOf(mapData.get("tplName"));
					String strAppExportTplStatus = String.valueOf(mapData.get("tplStatus"));
					String strAppExportTplType = String.valueOf(mapData.get("tplType"));
					String strAppModalId = String.valueOf(mapData.get("modalID"));
					//查看模版名称石佛重复
					String sqlExistAppExportTplName = "SELECT count(1) FROM PS_TZ_EXPORT_TMP_T WHERE TZ_EXPORT_TMP_ID <> ? AND TZ_JG_ID= ? AND TZ_EXPORT_TMP_NAME=?";

					int isExistAppExportTplNameNum = sqlQuery.queryForObject(sqlExistAppExportTplName, new Object[] { strAppExportTplId,orgid,strAppExportTplName}, "Integer");

					if(isExistAppExportTplNameNum == 0){
						int isExistAppExportTplTypeNum = 0;
						if(!"0".equals(strAppExportTplType) && "A".equals(strAppExportTplStatus)){
							String sqlExistAppExportTplType = "SELECT count(1) FROM PS_TZ_EXPORT_TMP_T WHERE TZ_EXPORT_TMP_ID <> ? AND TZ_EXP_TMP_STATUS='A' AND TZ_APP_MODAL_ID=? AND TZ_EXPORT_TMP_TYPE=?";
							isExistAppExportTplTypeNum = sqlQuery.queryForObject(sqlExistAppExportTplType, new Object[] { strAppExportTplId,strAppModalId,strAppExportTplType}, "Integer");
						}
						//当导出模版类型不为“Excel导出模版”时，且模版有效状态为有效时，检查导出模版类型是否唯一
						if(isExistAppExportTplTypeNum == 0){
							//查看模版编号是否已经存在*
							String sqlExistAppExportTplId = "SELECT count(1) FROM PS_TZ_EXPORT_TMP_T WHERE TZ_EXPORT_TMP_ID= ?";
							int isExistAppExportTplIdNum = sqlQuery.queryForObject(sqlExistAppExportTplId, new Object[] { strAppExportTplId }, "Integer");
							if(isExistAppExportTplIdNum > 0){
								PsTzExportTmpT psTzExportTmpT = new PsTzExportTmpT();
								psTzExportTmpT.setTzExportTmpId(strAppExportTplId);
								psTzExportTmpT.setTzExportTmpName(strAppExportTplName);
								psTzExportTmpT.setTzExportTmpType(strAppExportTplType);
								psTzExportTmpT.setTzExpTmpStatus(strAppExportTplStatus);
								psTzExportTmpT.setTzAppModalId(strAppModalId);
								psTzExportTmpT.setTzJgId(orgid);
								psTzExportTmpT.setRowAddedDttm(new Date());
								psTzExportTmpT.setRowAddedOprid(oprid);
								psTzExportTmpT.setRowLastmantDttm(new Date());
								psTzExportTmpT.setRowLastmantOprid(oprid);
								
								PsTzExportTmpTMapper.updateByPrimaryKeySelective(psTzExportTmpT);
							}else{
								errMsg[0] = "1";
								errMsg[1] = "导出模板名称为：" + strAppModalId + "的信息不存在。";
							}
						}else{
							errMsg[0] = "1";
							errMsg[1] = "报名表模版编号为：" + strAppModalId + "的报名表只允许有一个此类型的模板。";
						}
					}else{
						errMsg[0] = "1";
						errMsg[1] = "导出模板名称为：" + strAppExportTplName + "的信息已经存在，请修改导出模板名称。";
					}	
				}else if("FIELD".equals(typeFlag)){
					String strAppExportTplId = String.valueOf(mapData.get("tplID"));
					String strFieldID = String.valueOf(mapData.get("fieldID"));
					String strFieldName = String.valueOf(mapData.get("fieldName"));
					String strAppClass = String.valueOf(mapData.get("appClass"));
					int numFieldSeq = Integer.parseInt(String.valueOf(mapData.get("fieldSeq")));
					String strSeparator = String.valueOf(mapData.get("separator"));
					short numColumnWidth = Short.parseShort(String.valueOf(mapData.get("columnWidth")));
					String strFilter = String.valueOf(mapData.get("filter"));
					if (strAppExportTplId != null && !"".equals(strAppExportTplId) && strFieldID != null && !"".equals(strFieldID)) {
						//模版字段信息表
						String sqlExistFiledId = "SELECT count(1) FROM PS_TZ_EXP_FRMFLD_T WHERE TZ_EXPORT_TMP_ID= ? AND TZ_DC_FIELD_ID = ?";
						int isExistFieldIdNum = sqlQuery.queryForObject(sqlExistFiledId, new Object[] { strAppExportTplId,strFieldID }, "Integer");
						if(isExistFieldIdNum > 0){
							PsTzExpFrmFldT psTzExpFrmFldT = new PsTzExpFrmFldT();
							psTzExpFrmFldT.setTzExportTmpId(strAppExportTplId);
							psTzExpFrmFldT.setTzDcFieldId(strFieldID);
							psTzExpFrmFldT.setTzDcFieldName(strFieldName);
							psTzExpFrmFldT.setTzAppclsId(strAppClass);
							psTzExpFrmFldT.setTzSortNum(numFieldSeq);
							psTzExpFrmFldT.setTzDcFieldFgf(strSeparator);
							psTzExpFrmFldT.setTzDcColWidth(numColumnWidth);
							psTzExpFrmFldT.setTzDcColFilter(strFilter);
							PsTzExpFrmFldTMapper.updateByPrimaryKeySelective(psTzExpFrmFldT);
						}else{
							PsTzExpFrmFldT psTzExpFrmFldT = new PsTzExpFrmFldT();
							psTzExpFrmFldT.setTzExportTmpId(strAppExportTplId);
							psTzExpFrmFldT.setTzDcFieldId(strFieldID);
							psTzExpFrmFldT.setTzDcFieldName(strFieldName);
							psTzExpFrmFldT.setTzAppclsId(strAppClass);
							psTzExpFrmFldT.setTzSortNum(numFieldSeq);
							psTzExpFrmFldT.setTzDcFieldFgf(strSeparator);
							psTzExpFrmFldT.setTzDcColWidth(numColumnWidth);
							psTzExpFrmFldT.setTzDcColFilter(strFilter);
							PsTzExpFrmFldTMapper.insert(psTzExpFrmFldT);
						}
						
						String strFormFieldList = "-";
						//添加模板字段原来对应报名表信息表
						List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
						
						listData = (ArrayList<Map<String, Object>>) mapData.get("appFormField");
						for (Object objData : listData) {
							Map<String, Object> mapDataGlField = (Map<String, Object>) objData;
							String strFieldIdGl = String.valueOf(mapDataGlField.get("appFormField"));
						
							if (strFieldIdGl != null && !"".equals(strFieldIdGl)) {
								
								strFormFieldList = strFormFieldList + "','" + strFieldIdGl;
								
								String strFieldGlTable = String.valueOf(mapDataGlField.get("codeTable"));
								String strSortNumGl = String.valueOf(mapDataGlField.get("appFormFieldSeq"));
								String sqlExistGlFiledId = "SELECT count(1) FROM PS_TZ_FRMFLD_GL_T WHERE TZ_EXPORT_TMP_ID= ? AND TZ_DC_FIELD_ID = ? AND TZ_FORM_FLD_ID = ?";
								int isExistGlFieldIdNum = sqlQuery.queryForObject(sqlExistGlFiledId, new Object[] { strAppExportTplId,strFieldID,strFieldIdGl }, "Integer");
								if(isExistGlFieldIdNum > 0){
									PsTzFrmFldGlT psTzFrmFldGlT = new PsTzFrmFldGlT();
									psTzFrmFldGlT.setTzExportTmpId(strAppExportTplId);
									psTzFrmFldGlT.setTzDcFieldId(strFieldID);
									psTzFrmFldGlT.setTzFormFldId(strFieldIdGl);
									psTzFrmFldGlT.setTzCodeTableId(strFieldGlTable);
									psTzFrmFldGlT.setTzSortNum(Integer.parseInt(strSortNumGl));
									PsTzFrmFldGlTMapper.updateByPrimaryKeySelective(psTzFrmFldGlT);
								}else{
									PsTzFrmFldGlT psTzFrmFldGlT = new PsTzFrmFldGlT();
									psTzFrmFldGlT.setTzExportTmpId(strAppExportTplId);
									psTzFrmFldGlT.setTzDcFieldId(strFieldID);
									psTzFrmFldGlT.setTzFormFldId(strFieldIdGl);
									psTzFrmFldGlT.setTzCodeTableId(strFieldGlTable);
									psTzFrmFldGlT.setTzSortNum(Integer.parseInt(strSortNumGl));
									PsTzFrmFldGlTMapper.insert(psTzFrmFldGlT);
								}
							}
						}
						//删除模板字段原来对应报名表信息表
						Object[] args = new Object[] { strAppExportTplId,strFieldID };
						String sql = "DELETE FROM PS_TZ_FRMFLD_GL_T WHERE TZ_EXPORT_TMP_ID = ? AND TZ_DC_FIELD_ID = ? AND TZ_FORM_FLD_ID NOT IN('" + strFormFieldList + "')";
						sqlQuery.update(sql, args);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	/* 获取报名表导出模版信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "{}");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			
			// 报名表导出模版编号;
			String strAppExportTplId = jacksonUtil.getString("tplID");
			
			PsTzExportTmpT psTzExportTmpT = PsTzExportTmpTMapper.selectByPrimaryKey(strAppExportTplId);
			if(psTzExportTmpT != null ){
				Map<String, Object> retMap = new HashMap<String, Object>();
			
				String strAppExportTplName = String.valueOf(psTzExportTmpT.getTzExportTmpName());
				String strAppExportTplStatus = String.valueOf(psTzExportTmpT.getTzExpTmpStatus());
				String strAppExportTplType = String.valueOf(psTzExportTmpT.getTzExportTmpType());
				String strAppModalId = String.valueOf(psTzExportTmpT.getTzAppModalId());
				
				retMap.put("tplID", strAppExportTplId);
				retMap.put("tplName", strAppExportTplName);
				retMap.put("tplType", strAppExportTplType);
				retMap.put("tplStatus", strAppExportTplStatus);
				retMap.put("modalID", strAppModalId);
				
				//查看模版编号是否已经存在*
				String sqlGetAppTplName = "SELECT TZ_APP_TPL_MC FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID= ?";
				String strAppModalName = sqlQuery.queryForObject(sqlGetAppTplName, new Object[] { strAppModalId }, "String");
				retMap.put("modalName", strAppModalName); 
				returnJsonMap.replace("formData",retMap);
				
			}else{
				errMsg[0] = "1";
				errMsg[1] = "报名表导出模版信息不存在。";
			}	

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}
	
	/**
	 * 删除报名表导出模版字段信息
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzDelete(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				// 报名表导出模版编号;
				String strAppExportTplId = jacksonUtil.getString("tplID");
				String strFieldID = jacksonUtil.getString("fieldID");
				
				if (strAppExportTplId != null && !"".equals(strAppExportTplId) && strFieldID != null && !"".equals(strFieldID)) {
					
					Object[] args = new Object[] { strAppExportTplId,strFieldID};
					sqlQuery.update("DELETE FROM PS_TZ_EXP_FRMFLD_T WHERE TZ_EXPORT_TMP_ID = ? AND TZ_DC_FIELD_ID = ?", args);
					sqlQuery.update("DELETE FROM PS_TZ_FRMFLD_GL_T WHERE TZ_EXPORT_TMP_ID = ? AND TZ_DC_FIELD_ID = ?", args);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}
	
	/**
	 * 其他相关函数 
	 * loadAppFormFields:加载报名表模板字段
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public String tzOther(String oprType,String strParams, String[] errMsg) {
		// 返回值;
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			if("loadAppFormFields".equals(oprType)){
				jacksonUtil.json2Map(strParams);
				// 报名表导出模版编号;
				String strAppExportTplId = jacksonUtil.getString("tplID");
				String strAppModalID = jacksonUtil.getString("modalID");
				ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
				String strXxxBh = "";
				String strXxxMc = "";
				String sql = "SELECT TZ_XXX_BH,TZ_XXX_MC FROM PS_TZ_TEMP_FIELD_V WHERE TZ_APP_TPL_ID= ? AND TZ_XXX_CCLX IN ('D','S','L','R')";
				List<?> listData = sqlQuery.queryForList(sql, new Object[] { strAppModalID });
				for (Object objData : listData) {

					Map<String, Object> mapData = (Map<String, Object>) objData;
					

					strXxxBh = String.valueOf(mapData.get("TZ_XXX_BH"));
					strXxxMc = String.valueOf(mapData.get("TZ_XXX_MC"));
					//
					Map<String, Object> mapJson = new HashMap<String, Object>();
					mapJson.put("infoID", strXxxBh);
					mapJson.put("infoName", strXxxMc);
					listJson.add(mapJson);
				}
				
				String sqlAppIns = "SELECT TZ_XXX_BH,TZ_XXX_MC FROM PS_TZ_FORM_FIELD_V WHERE TZ_APP_TPL_ID= ? AND TZ_XXX_CCLX = 'R'";
				List<?> listDataAppIns = sqlQuery.queryForList(sqlAppIns, new Object[] { strAppModalID });
				for (Object objData : listDataAppIns) {

					Map<String, Object> mapData = (Map<String, Object>) objData;
					

					strXxxBh = String.valueOf(mapData.get("TZ_XXX_BH"));
					strXxxMc = String.valueOf(mapData.get("TZ_XXX_MC"));
					//
					Map<String, Object> mapJson = new HashMap<String, Object>();
					mapJson.put("infoID", strXxxBh);
					mapJson.put("infoName", strXxxMc);
					listJson.add(mapJson);
				}
				mapRet.replace("root", listJson);
				
				//删除原来的数据
				if (strAppExportTplId != null && !"".equals(strAppExportTplId)) {
					Object[] args = new Object[] { strAppExportTplId };
					sqlQuery.update("DELETE FROM PS_TZ_EXP_FRMFLD_T WHERE TZ_EXPORT_TMP_ID = ?", args);
					sqlQuery.update("DELETE FROM PS_TZ_FRMFLD_GL_T WHERE TZ_EXPORT_TMP_ID = ?", args);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return jacksonUtil.Map2json(mapRet);
	}
}
