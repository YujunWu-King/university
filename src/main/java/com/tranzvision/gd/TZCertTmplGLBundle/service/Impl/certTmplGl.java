package com.tranzvision.gd.TZCertTmplGLBundle.service.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Console;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZCertTmplGLBundle.dao.PsTzCerttmplTblMapper;
import com.tranzvision.gd.TZCertTmplGLBundle.dao.PsTzCertimageTblMapper;
import com.tranzvision.gd.TZCertTmplGLBundle.model.PsTzCerttmplTbl;
import com.tranzvision.gd.TZCertTmplGLBundle.model.PsTzCerttmplTblKey;
import com.tranzvision.gd.TZCertTmplGLBundle.model.PsTzCerttmplTblWithBLOBs;
import com.tranzvision.gd.TZCertTmplGLBundle.model.PsTzCertimageTbl;
import com.tranzvision.gd.TZClassDefnBundle.model.PsTzAppclsTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;


/*
 * 证书模版管理
 * @author tang
 */
@Service("com.tranzvision.gd.TZCertTmplGLBundle.service.Impl.certTmplGl")
public class certTmplGl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzCerttmplTblMapper psTzCerttmplTblMapper;
	@Autowired
	private PsTzCertimageTblMapper psTzCertimageTblMapper;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TZGDObject tzSQLObject;

	/* 查询列表 */
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
			String[][] orderByArr = new String[][] { { "TZ_CERT_TMPL_ID", "ASC" } };

			// 数据要的结果字段;
			String[] resultFldArray = {"TZ_CERT_TMPL_ID", "TZ_CERT_TYPE_NAME", "TZ_CERT_JG_NAME","TZ_USE_FLAG","TZ_JG_ID"  };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					
					mapList.put("certTmpl", rowList[0]);
					mapList.put("certType", rowList[1]);
					mapList.put("certJGID", rowList[2]);
					mapList.put("useFlag", rowList[3]);
					mapList.put("JgId", rowList[4]);
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

	/* 获取证书模板定义信息 */
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("certTmpl")&&jacksonUtil.containsKey("JgId")) {
				String certTmpl = jacksonUtil.getString("certTmpl");
				String JgId = jacksonUtil.getString("JgId");
				PsTzCerttmplTblKey psTzCerttmplTblKey = new PsTzCerttmplTblKey();
				psTzCerttmplTblKey.setTzCertTmplId(certTmpl);
				psTzCerttmplTblKey.setTzJgId(JgId);
				//PsTzCerttmplTblWithBLOBs psTzCerttmplTblWithBLOBs = new PsTzCerttmplTblWithBLOBs();
				PsTzCerttmplTblWithBLOBs psTzCerttmplTblWithBLOBs =  psTzCerttmplTblMapper.selectByPrimaryKey(psTzCerttmplTblKey);
				if (psTzCerttmplTblWithBLOBs != null) {
					Map<String, Object> map = new HashMap<>();
					map.put("certTmpl", psTzCerttmplTblWithBLOBs.getTzCertTmplId());
					map.put("JgId", psTzCerttmplTblWithBLOBs.getTzJgId());
					map.put("tmplName", psTzCerttmplTblWithBLOBs.getTzTmplName());
					map.put("certJGID", psTzCerttmplTblWithBLOBs.getTzCertJgId());
					map.put("certTypeID", psTzCerttmplTblWithBLOBs.getTzCertTypeId());
					map.put("useFlag", psTzCerttmplTblWithBLOBs.getTzUseFlag());
					map.put("certMergHtml1", psTzCerttmplTblWithBLOBs.getTzCertMergHtml1());
					map.put("certMergHtml2", psTzCerttmplTblWithBLOBs.getTzCertMergHtml2());
					map.put("certMergHtml3", psTzCerttmplTblWithBLOBs.getTzCertMergHtml3());
					map.put("titleImageName", psTzCerttmplTblWithBLOBs.getTzAttachsysfilena());
					
					String titleImageName= psTzCerttmplTblWithBLOBs.getTzAttachsysfilena();
					String sql = tzSQLObject.getSQLText("SQL.TZCertTmplGLBundle.TzGetZhengShuBtt");
					String imageAUrl = jdbcTemplate.queryForObject(sql, new Object[] {titleImageName}, "String");
					map.put("imageAUrl", imageAUrl);
					
					returnJsonMap.replace("formData", map);
				} else{
					errMsg[0] = "1";
					errMsg[1] = "请选择证书模板";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "请选择证书模板";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}

	/* 新增类方法 */
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];

				jacksonUtil.json2Map(strForm);
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");

				String certTmpl = (String) infoData.get("certTmpl");
				String tmplName = (String) infoData.get("tmplName");
				String JgId = (String) infoData.get("JgId");
				String certJGID = (String) infoData.get("certJGID");
				String certTypeID = (String) infoData.get("certTypeID");
				String useFlag = (String) infoData.get("useFlag");
				String certMergHtml1 = (String) infoData.get("certMergHtml1");
				String certMergHtml2 = (String) infoData.get("certMergHtml2");
				String certMergHtml3 = (String) infoData.get("certMergHtml3");
				String titleImageName = (String) infoData.get("titleImageName");
				String imageAUrl = (String) infoData.get("imageAUrl");
				if("Y".equals(useFlag)){
					String sqlUpdate = tzSQLObject.getSQLText("SQL.TZCertTmplGLBundle.TZUpdateUseFlag");
					sqlQuery.update(sqlUpdate, new Object[] { JgId,certTypeID });
				}else{
				}
				String sql = tzSQLObject.getSQLText("SQL.TZCertTmplGLBundle.TzGetCountTmplByCertId");
				int count = jdbcTemplate.queryForObject(sql, new Object[] { JgId,certTmpl }, "Integer");
				if (count > 0) {
					errMsg[0] = "1";
					errMsg[1] = "模板编号:" + certTmpl + ",已经存在";
				} else {
					//PsTzCerttmplTbl psTzCerttmplTbl = new PsTzCerttmplTbl();
					PsTzCerttmplTblWithBLOBs psTzCerttmplTblWithBLOBs = new PsTzCerttmplTblWithBLOBs();
					psTzCerttmplTblWithBLOBs.setTzJgId(JgId);
					psTzCerttmplTblWithBLOBs.setTzCertTmplId(certTmpl);
					psTzCerttmplTblWithBLOBs.setTzTmplName(tmplName);
					psTzCerttmplTblWithBLOBs.setTzCertJgId(certJGID);
					psTzCerttmplTblWithBLOBs.setTzCertTypeId(certTypeID);
					psTzCerttmplTblWithBLOBs.setTzUseFlag(useFlag);
					psTzCerttmplTblWithBLOBs.setTzCertMergHtml1(certMergHtml1);
					psTzCerttmplTblWithBLOBs.setTzCertMergHtml2(certMergHtml2);
					psTzCerttmplTblWithBLOBs.setTzCertMergHtml3(certMergHtml3);
					psTzCerttmplTblWithBLOBs.setTzAttachsysfilena(titleImageName);
					String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
					psTzCerttmplTblWithBLOBs.setRowAddedDttm(new Date());
					psTzCerttmplTblWithBLOBs.setRowAddedOprid(oprid);
					psTzCerttmplTblWithBLOBs.setRowLastmantDttm(new Date());
					psTzCerttmplTblWithBLOBs.setRowLastmantOprid(oprid);
					psTzCerttmplTblMapper.insert(psTzCerttmplTblWithBLOBs);
				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/*修改类定义信息 */
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");

				String certTmpl = (String) infoData.get("certTmpl");
				String tmplName = (String) infoData.get("tmplName");
				String JgId = (String) infoData.get("JgId");
				String certJGID = (String) infoData.get("certJGID");
				String certTypeID = (String) infoData.get("certTypeID");
				String useFlag = (String) infoData.get("useFlag");
				String certMergHtml1 = (String) infoData.get("certMergHtml1");
				String certMergHtml2 = (String) infoData.get("certMergHtml2");
				String certMergHtml3 = (String) infoData.get("certMergHtml3");
				String titleImageName = (String) infoData.get("titleImageName");
				System.out.println(useFlag);
				System.out.println(JgId);
				System.out.println(certTypeID);
				if("Y".equals(useFlag)){
					String sqlUpdate = tzSQLObject.getSQLText("SQL.TZCertTmplGLBundle.TZUpdateUseFlag");
					sqlQuery.update(sqlUpdate, new Object[] { JgId,certTypeID });
				}else{
				}
				String sql = tzSQLObject.getSQLText("SQL.TZCertTmplGLBundle.TzGetCountTmplByCertId");;
				int count = jdbcTemplate.queryForObject(sql, new Object[] { JgId,certTmpl }, "Integer");
				if (count > 0) {
					PsTzCerttmplTblWithBLOBs psTzCerttmplTblWithBLOBs = new PsTzCerttmplTblWithBLOBs();
					psTzCerttmplTblWithBLOBs.setTzCertTmplId(certTmpl);
					psTzCerttmplTblWithBLOBs.setTzJgId(JgId);
					psTzCerttmplTblWithBLOBs.setTzTmplName(tmplName);
					psTzCerttmplTblWithBLOBs.setTzCertJgId(certJGID);
					psTzCerttmplTblWithBLOBs.setTzCertTypeId(certTypeID);
					psTzCerttmplTblWithBLOBs.setTzUseFlag(useFlag);
					psTzCerttmplTblWithBLOBs.setTzCertMergHtml1(certMergHtml1);
					psTzCerttmplTblWithBLOBs.setTzCertMergHtml2(certMergHtml2);
					psTzCerttmplTblWithBLOBs.setTzCertMergHtml3(certMergHtml3);
					psTzCerttmplTblWithBLOBs.setTzAttachsysfilena(titleImageName);
					String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
					psTzCerttmplTblWithBLOBs.setRowLastmantDttm(new Date());
					psTzCerttmplTblWithBLOBs.setRowLastmantOprid(oprid);
					
					psTzCerttmplTblMapper.updateByPrimaryKeySelective(psTzCerttmplTblWithBLOBs);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "模板编号:" + certTmpl + "不存在";
				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	// 功能说明：删除类定义;
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				//提交信息
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 类定义ID;
				String certTmpl = jacksonUtil.getString("certTmpl");
				String JgId = jacksonUtil.getString("JgId");
				PsTzCerttmplTblKey psTzCerttmplTblKey = new PsTzCerttmplTblKey();
				psTzCerttmplTblKey.setTzCertTmplId(certTmpl);
				psTzCerttmplTblKey.setTzJgId(JgId);
				if (certTmpl != null && !"".equals(certTmpl)) {

					psTzCerttmplTblMapper.deleteByPrimaryKey(psTzCerttmplTblKey);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
	@Override
	public String tzGetHtmlContent(String strParams) {
			String strRet = "";
			String errorDesc = "";
			String sysFileName ="";
			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strParams);
			try {

				Map<String, Object> mapParams = jacksonUtil.getMap("data");
				String attachmentType = jacksonUtil.getString("attachmentType");
				
				if (null != attachmentType && !"".equals(attachmentType) && null != mapParams) {

					attachmentType = attachmentType.toUpperCase();

					String path = mapParams.get("path") == null ? "" : String.valueOf(mapParams.get("path"));
					sysFileName = mapParams.get("sysFileName") == null ? ""
							: String.valueOf(mapParams.get("sysFileName"));
					String filename = mapParams.get("filename") == null ? "" : String.valueOf(mapParams.get("filename"));
					String accessPath = mapParams.get("accessPath") == null ? ""
							: String.valueOf(mapParams.get("accessPath"));

					String fileDir = request.getSession().getServletContext().getResource(accessPath).getPath();
					path = fileDir;
					PsTzCertimageTbl psTzCertimageTbl = new PsTzCertimageTbl();
					psTzCertimageTbl.setTzAttachsysfilena(sysFileName);
					psTzCertimageTbl.setTzAttachfileName(filename);
					psTzCertimageTbl.setTzAttPUrl(path);
					psTzCertimageTbl.setTzAttAUrl(accessPath);

						int rstImg = psTzCertimageTblMapper.insert(psTzCertimageTbl);
						if (rstImg == 0) {
							errorDesc = "保存数据时发生错误";
						}

				} else {
					errorDesc = "参数错误。";
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				errorDesc = "系统异常。" + e.getMessage();
			}

			int errorCode = 0;
			if (!"".equals(errorDesc)) {
				errorCode = 1;
			}
		
			Map<String, Object> mapRet = new HashMap<String, Object>();
			mapRet.put("success", errorCode);
			mapRet.put("message", errorDesc);
			mapRet.put("minPicSysFileName", sysFileName);
			strRet = jacksonUtil.Map2json(mapRet);
			return strRet;
		}

}


