package com.tranzvision.gd.TZUniPrintBundle.service.impl;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.elasticsearch.discovery.zen.ElectMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZUniPrintBundle.dao.PsTzDymbTMapper;
import com.tranzvision.gd.TZUniPrintBundle.dao.PsTzDymbYsTMapper;
import com.tranzvision.gd.TZUniPrintBundle.dao.PsTzPdfMbxxTMapper;
import com.tranzvision.gd.TZUniPrintBundle.model.PsTzDymbT;
import com.tranzvision.gd.TZUniPrintBundle.model.PsTzDymbTKey;
import com.tranzvision.gd.TZUniPrintBundle.model.PsTzDymbYsT;
import com.tranzvision.gd.TZUniPrintBundle.model.PsTzDymbYsTKey;
import com.tranzvision.gd.TZUniPrintBundle.model.PsTzPdfMbxxTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
/*
 * 打印模板信息
 * 清华mba招生
 */
@Service("com.tranzvision.gd.TZUniPrintBundle.service.impl.UniPrintTplInfoImpl")
public class UniPrintTplInfoImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzDymbTMapper psTzDymbTMapper;
	@Autowired
	private PsTzDymbYsTMapper psTzDymbYsTMapper;
	@Autowired
	private PsTzPdfMbxxTMapper psTzPdfMbxxTMapper;
	
	
	/* 获取打印模板定义信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("dymbId")&&jacksonUtil.containsKey("jgId")) {
				String dymbId = jacksonUtil.getString("dymbId");
				String jgId = jacksonUtil.getString("jgId");
				
				PsTzDymbTKey psTzDymbTKey = new PsTzDymbTKey();
				psTzDymbTKey.setTzJgId(jgId);
				psTzDymbTKey.setTzDymbId(dymbId);
				
				PsTzDymbT psTzDymbT=  psTzDymbTMapper.selectByPrimaryKey(psTzDymbTKey);
				
				if (psTzDymbT != null) {
					returnJsonMap.put("TZ_JG_ID", psTzDymbT.getTzJgId());
					returnJsonMap.put("TZ_DYMB_ID", psTzDymbT.getTzDymbId());
					returnJsonMap.put("TZ_DYMB_NAME", psTzDymbT.getTzDymbName());
					returnJsonMap.put("TZ_DYMB_ZT", psTzDymbT.getTzDymbZt());
					returnJsonMap.put("TZ_DYMB_DRMB_ID", psTzDymbT.getTzDymbDrmbId());
					returnJsonMap.put("TZ_DYMB_MENO", psTzDymbT.getTzDymbMeno());
					returnJsonMap.put("TZ_DYMB_PDF_NAME", psTzDymbT.getTzDymbPdfName());
					returnJsonMap.put("TZ_DYMB_PDF_URL", psTzDymbT.getTzDymbPdfUrl());
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该打印模板不存在";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "该打印模板不存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}
	
	
	/*加载打印模板字段列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart,String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] { { "TZ_DYMB_FIELD_ID", "ASC" } };

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_JG_ID", "TZ_DYMB_ID", "TZ_DYMB_FIELD_ID", "TZ_DYMB_FIELD_SM", "TZ_DYMB_FIELD_QY", "TZ_DYMB_FIELD_PDF"};
		
		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);
		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("TZ_JG_ID", rowList[0]);
				mapList.put("TZ_DYMB_ID", rowList[1]);
				mapList.put("TZ_DYMB_FIELD_ID", rowList[2]);
				mapList.put("TZ_DYMB_FIELD_SM", rowList[3]);
				mapList.put("TZ_DYMB_FIELD_QY", rowList[4]);
				mapList.put("TZ_DYMB_FIELD_PDF", rowList[5]);
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}
	

	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				String type = (String) jacksonUtil.getString("type");
				
				if("TPL".equals(type)) {
					strRet = this.saveTplInfo(infoData,errMsg);
				}
				
				if("FIELD".equals(type)) {
					strRet = this.saveFieldInfo(infoData,errMsg);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				String type = (String) jacksonUtil.getString("type");
				
				if("TPL".equals(type)) {
					strRet = this.saveTplInfo(infoData,errMsg);
				}
				
				if("FIELD".equals(type)) {
					String strRetField = this.saveFieldInfo(infoData,errMsg);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	
	/*删除数据映射关系信息*/
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				
				String jgId = jacksonUtil.getString("TZ_JG_ID");
				String tplId = jacksonUtil.getString("TZ_DYMB_ID");
				String fieldId = jacksonUtil.getString("TZ_DYMB_FIELD_ID");
				
				String sql = "DELETE FROM PS_TZ_DYMB_YS_T WHERE TZ_JG_ID=? AND TZ_DYMB_ID=? AND TZ_DYMB_FIELD_ID=?";
				sqlQuery.update(sql, new Object[]{jgId,tplId,fieldId});
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	
	@Override
	public String tzOther(String operateType, String strParams, String[] errMsg) {
		String strRet = "";

		try {
			//解析PDF模板字段并自动匹配
			if("tzAnalysisMatch".equals(operateType)) {
				strRet = analysisMatch(strParams, errMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}
	
	/*保存打印模板基本信息*/
	public String saveTplInfo(Map<String, Object> mapParams,String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			String jgId = (String) mapParams.get("TZ_JG_ID");
			String dymbId = (String) mapParams.get("TZ_DYMB_ID");
			String dymbName = (String) mapParams.get("TZ_DYMB_NAME");
			String dymbStatus = (String) mapParams.get("TZ_DYMB_ZT");
			String impTplId = (String) mapParams.get("TZ_DYMB_DRMB_ID");
			String memo = (String) mapParams.get("TZ_DYMB_MENO");
			String pdfName = (String) mapParams.get("TZ_DYMB_PDF_NAME");
			String pdfUrl = (String) mapParams.get("TZ_DYMB_PDF_URL");
			
			if("".equals(dymbId) || "NEXT".equals(dymbId)) {
				dymbId = String.valueOf(getSeqNum.getSeqNum("TZ_DYMB_T", "TZ_DYMB_ID"));
			}
				
			PsTzDymbTKey psTzDymbTKey = new PsTzDymbTKey();
			psTzDymbTKey.setTzJgId(jgId);
			psTzDymbTKey.setTzDymbId(dymbId);
			
			PsTzDymbT psTzDymbT = psTzDymbTMapper.selectByPrimaryKey(psTzDymbTKey);
			if(psTzDymbT==null) {
				psTzDymbT = new PsTzDymbT();
				psTzDymbT.setTzJgId(jgId);
				psTzDymbT.setTzDymbId(dymbId);
				psTzDymbT.setTzDymbName(dymbName);
				psTzDymbT.setTzDymbZt(dymbStatus);
				psTzDymbT.setTzDymbDrmbId(impTplId);
				psTzDymbT.setTzDymbMeno(memo);
				psTzDymbT.setTzDymbPdfName(pdfName);
				psTzDymbT.setTzDymbPdfUrl(pdfUrl);
				psTzDymbTMapper.insertSelective(psTzDymbT);
			} else {
				psTzDymbT.setTzDymbName(dymbName);
				psTzDymbT.setTzDymbZt(dymbStatus);
				psTzDymbT.setTzDymbDrmbId(impTplId);
				psTzDymbT.setTzDymbMeno(memo);
				psTzDymbT.setTzDymbPdfName(pdfName);
				psTzDymbT.setTzDymbPdfUrl(pdfUrl);
				psTzDymbTMapper.updateByPrimaryKeySelective(psTzDymbT);
			}
			
			mapRet.put("dymbId", dymbId);
			strRet = jacksonUtil.Map2json(mapRet);
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	/*保存数据映射关系信息*/
	public String saveFieldInfo(Map<String, Object> mapParams,String[] errMsg) {
		String strRet = "";
		
		try {
			
			String jgId = mapParams.get("TZ_JG_ID") == null ? "" : mapParams.get("TZ_JG_ID").toString();
			String dymbId = mapParams.get("TZ_DYMB_ID") == null ? "" : mapParams.get("TZ_DYMB_ID").toString();
			String fieldId = mapParams.get("TZ_DYMB_FIELD_ID") == null ? "" : mapParams.get("TZ_DYMB_FIELD_ID").toString();
			String fieldName = mapParams.get("TZ_DYMB_FIELD_SM") == null ? "" : mapParams.get("TZ_DYMB_FIELD_SM").toString();
			String fieldStatus = "0";
			boolean bool_fieldStatus = (boolean) mapParams.get("TZ_DYMB_FIELD_QY");
			if(bool_fieldStatus==true) {
				fieldStatus="1";
			} 
			String fieldPdf = mapParams.get("TZ_DYMB_FIELD_PDF") == null ? "" : mapParams.get("TZ_DYMB_FIELD_PDF").toString();
			
			PsTzDymbYsTKey psTzDymbYsTKey = new PsTzDymbYsTKey();
			psTzDymbYsTKey.setTzJgId(jgId);
			psTzDymbYsTKey.setTzDymbId(dymbId);
			psTzDymbYsTKey.setTzDymbFieldId(fieldId);
			
			PsTzDymbYsT psTzDymbYsT = psTzDymbYsTMapper.selectByPrimaryKey(psTzDymbYsTKey);
			if(psTzDymbYsT==null) {
				 psTzDymbYsT = new PsTzDymbYsT();
				 psTzDymbYsT.setTzJgId(jgId);
				 psTzDymbYsT.setTzDymbId(dymbId);
				 psTzDymbYsT.setTzDymbFieldId(fieldId);
				 psTzDymbYsT.setTzDymbFieldSm(fieldName);
				 psTzDymbYsT.setTzDymbFieldQy(fieldStatus);
				 psTzDymbYsT.setTzDymbFieldPdf(fieldPdf);
				 psTzDymbYsTMapper.insertSelective(psTzDymbYsT);
			} else {
				 psTzDymbYsT.setTzDymbFieldSm(fieldName);
				 psTzDymbYsT.setTzDymbFieldQy(fieldStatus);
				 psTzDymbYsT.setTzDymbFieldPdf(fieldPdf);
				 psTzDymbYsTMapper.updateByPrimaryKeySelective(psTzDymbYsT);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
		
	}
	
		
	/*解析PDF模板字段并自动匹配*/
	public String analysisMatch(String strParams,String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");
		
		try {
			jacksonUtil.json2Map(strParams);
			
			String jgIdParam = jacksonUtil.getString("jgId");
			String dymbIdParam = jacksonUtil.getString("dymbId");
			
			String filePath = jacksonUtil.getString("filePath");
			Resource resource = new ClassPathResource("conf/cookieSession.properties");
			Properties cookieSessioinProps = PropertiesLoaderUtils.loadProperties(resource);
			String webAppRootKey = cookieSessioinProps.getProperty("webAppRootKey");
			filePath = System.getProperty(webAppRootKey) + filePath;
			
			List<Map<String, Object>> listData = (List<Map<String, Object>>) jacksonUtil.getList("storDates");
			
			Map<String, Object> mapDataTmp = null;
			Map<String, Object> mapData = null;
			String temp = null;
			
			ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
			
			String jgId,dymbId,fieldID,fieldName,fieldStatus;
			
			
			if (listData != null && listData.size() > 0) {
				
				// 解析加载PDF模板信息项
				String[] fields = this.split(this.getPdfFileFields(jgIdParam,dymbIdParam,filePath), ";");
				
				Map<String, Object> mapJson = null;
				
				for (int i = 0; i < listData.size(); i++) {
					mapDataTmp = listData.get(i);
					mapData = (Map<String, Object>)mapDataTmp.get("data");
					jgId = String.valueOf(mapData.get("TZ_JG_ID"));
					dymbId = String.valueOf(mapData.get("TZ_DYMB_ID"));
					fieldID = String.valueOf(mapData.get("TZ_DYMB_FIELD_ID"));
					fieldName = String.valueOf(mapData.get("TZ_DYMB_FIELD_SM"));
					boolean bool_fieldStatus = (boolean)mapData.get("TZ_DYMB_FIELD_QY");
					if(bool_fieldStatus==true) {
						fieldStatus = "1";
					} else {
						fieldStatus = "0";
					}

					mapJson = new HashMap<String, Object>();
					mapJson.put("jgId", jgId);
					mapJson.put("dymbId", dymbId);
					mapJson.put("fieldID", fieldID);
					mapJson.put("fieldName", fieldName);
					mapJson.put("fieldStatus", fieldStatus);
					
					if (fields != null && fields.length > 0) {
						for (int j = 0; j < fields.length; j++) {
							temp = this.getTZ_XXX_BH(fields[j]);
							if (temp.equals(fieldID)) {
								mapJson.put("fieldPdf", fields[j]);
							}
						}
					} 
						
					if (mapJson.get("fieldPdf") != null) {
						listJson.add(mapJson);
					}
				}
				
				mapRet.replace("root", listJson);
			}
			
			strRet = jacksonUtil.Map2json(mapRet);
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	private String getTZ_XXX_BH(String str) {
		// 表单1[0].#subform[4].TZ_1124[0]

		str = str.substring(str.lastIndexOf(".") + 1, str.length());
		if (str.indexOf("[") != -1) {
			str = str.substring(0, str.indexOf("["));
		}
		return str;
	}
	
	private String[] split(String str, String sep) {
		int size = 0;
		Vector<String> v = new Vector<String>();
		int pos = -1;
		while (!str.equals("")) {
			pos = str.indexOf(sep);
			if (pos != -1) {
				v.add(str.substring(0, pos));
				str = str.substring(pos + sep.length());
				if (str.equalsIgnoreCase("")) {
					v.add("");
				}
			} else {
				v.add(str);
				str = "";
			}
		}

		size = v.size();
		String[] array = new String[size];
		for (int i = 0; i < size; i++) {
			array[i] = v.elementAt(i).toString();
		}
		return array;
	}
	
	
	private String getPdfFileFields(String jgId,String dymbId,String file) {
		String fields = "";
		PdfReader reader = null;
		ByteArrayOutputStream bos = null;
		PdfStamper ps = null;
		AcroFields s = null;
		try {
			
			/*删除PDF模板信息表数据*/
			sqlQuery.update("DELETE FROM PS_TZ_PDF_MBXX_T WHERE TZ_JG_ID=? AND TZ_MB_ID=?",new Object[]{jgId,dymbId});
			sqlQuery.update("COMMIT");
			
			reader = new PdfReader(file);
			bos = new ByteArrayOutputStream();
			ps = new PdfStamper(reader, bos);
			s = ps.getAcroFields();
			int i = 1;
			for (Iterator it = s.getFields().keySet().iterator(); it.hasNext(); ++i) {
				String name = (String) it.next();
				fields = fields + name + ";";
				
				PsTzPdfMbxxTKey psTzPdfMbxxTKey = new PsTzPdfMbxxTKey();
				psTzPdfMbxxTKey.setTzJgId(jgId);
				psTzPdfMbxxTKey.setTzMbId(dymbId);
				psTzPdfMbxxTKey.setTzPdfField(name);
				psTzPdfMbxxTMapper.insertSelective(psTzPdfMbxxTKey);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				bos.close();
				reader.close();
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fields;
	}
		
}
