package com.tranzvision.gd.TZCertTypeGlBundle.service.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZCertTypeGlBundle.dao.PsTzCertTypeTblMapper;
import com.tranzvision.gd.TZCertTypeGlBundle.model.PsTzCertTypeTbl;
import com.tranzvision.gd.TZCertTypeGlBundle.model.PsTzCertTypeTblKey;
import com.tranzvision.gd.TZZsCreOrorganizationBundle.model.PsTzZsJGTBL;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.sql.GetSeqNum;


/*
 * 证书模版管理
 * @author tang
 */
@Service("com.tranzvision.gd.TZCertTypeGlBundle.service.Impl.certTypeGl")
public class certTypeGl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzCertTypeTblMapper psTzCertTypeTblMapper;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private TZGDObject tzSQLObject;
	@Autowired
	private GetSeqNum getSeqNum;

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
			String[][] orderByArr = new String[][] { { "TZ_CERT_TYPE_ID", "ASC" } };

			// 数据要的结果字段;
			String[] resultFldArray = {"TZ_JG_ID", "TZ_CERT_TYPE_ID", "TZ_CERT_TYPE_NAME" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("JgId", rowList[0]);
					mapList.put("certTypeId", rowList[1]);
					mapList.put("certName", rowList[2]);
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
			if (jacksonUtil.containsKey("certTypeId")&&jacksonUtil.containsKey("JgId")) {
				String certTypeId = jacksonUtil.getString("certTypeId");
				String JgId = jacksonUtil.getString("JgId");
				PsTzCertTypeTblKey PsTzCertTypeTblKey = new PsTzCertTypeTblKey();
				PsTzCertTypeTblKey.setTzJgId(JgId);
				PsTzCertTypeTblKey.setTzCertTypeId(certTypeId);
				PsTzCertTypeTbl psTzCertTypeTbl =  psTzCertTypeTblMapper.selectByPrimaryKey(PsTzCertTypeTblKey);
				if (psTzCertTypeTbl != null) {
					Map<String, Object> map = new HashMap<>();
					map.put("certTypeId", psTzCertTypeTbl.getTzCertTypeId());
					map.put("JgId", psTzCertTypeTbl.getTzJgId());
					map.put("certName", psTzCertTypeTbl.getTzCertTypeName());	
					returnJsonMap.replace("formData", map);
				} else{
					errMsg[0] = "1";
					errMsg[1] = "请选择证书";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "请选择证书";
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
				Map<String, Object> returnJsonMap = new HashMap<String, Object>();
				String JgId = (String) infoData.get("JgId");
				//String certTypeId = (String) infoData.get("certTypeId");
				int certType= getSeqNum.getSeqNum("TZ_CERT_TYPE_TBL", "TZ_CERT_TYPE_ID");
				String certTypeId= String.valueOf(certType);
				String certName = (String) infoData.get("certName");
				
				String sql = tzSQLObject.getSQLText("SQL.TZCertTypeGlBundle.TZGetCertID");
				int count = jdbcTemplate.queryForObject(sql, new Object[] { JgId,certTypeId }, "Integer");
				if (count > 0) {
					errMsg[0] = "1";
					errMsg[1] = "证书类型编号:" + certTypeId + ",已经存在";
				} else {					
					PsTzCertTypeTbl psTzCertTypeTbl  = new PsTzCertTypeTbl();
					psTzCertTypeTbl.setTzJgId(JgId);
					psTzCertTypeTbl.setTzCertTypeId(certTypeId);
					psTzCertTypeTbl.setTzCertTypeName(certName);
					psTzCertTypeTbl.setTzUseFlag("Y");
					String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
					psTzCertTypeTbl.setRowAddedDttm(new Date());
					psTzCertTypeTbl.setRowAddedOprid(oprid);
					psTzCertTypeTbl.setRowLastmantDttm(new Date());
					psTzCertTypeTbl.setRowLastmantOprid(oprid);
					psTzCertTypeTblMapper.insert(psTzCertTypeTbl);
					
					returnJsonMap.put("certTypeId", String.valueOf(certTypeId));
					
					strRet=String.valueOf(certTypeId);
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			e.printStackTrace();
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
				Map<String, Object> returnJsonMap = new HashMap<String, Object>();
				String JgId = (String) infoData.get("JgId");
				String certTypeId = (String) infoData.get("certTypeId");
				String certName = (String) infoData.get("certName");				
				
				String sql = tzSQLObject.getSQLText("SQL.TZCertTypeGlBundle.TZGetCertID");
				
				int count = jdbcTemplate.queryForObject(sql, new Object[] { JgId,certTypeId }, "Integer");
				if (count > 0) {
					PsTzCertTypeTbl psTzCertTypeTbl  = new PsTzCertTypeTbl();
					psTzCertTypeTbl.setTzJgId(JgId);
					psTzCertTypeTbl.setTzCertTypeId(certTypeId);
					psTzCertTypeTbl.setTzCertTypeName(certName);
					psTzCertTypeTblMapper.updateByPrimaryKeySelective(psTzCertTypeTbl);
					returnJsonMap.put("certTypeId", String.valueOf(certTypeId));
					strRet=String.valueOf(certTypeId);
				} else {
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
				String certTypeId = jacksonUtil.getString("certTypeId");
				String JgId = jacksonUtil.getString("JgId");
				
				if (certTypeId != null && !"".equals(certTypeId)) {
					PsTzCertTypeTbl psTzCertTypeTbl=new PsTzCertTypeTbl();
					psTzCertTypeTbl.setTzCertTypeId(certTypeId);
					psTzCertTypeTbl.setTzUseFlag("N");
					psTzCertTypeTbl.setTzJgId(JgId);
					psTzCertTypeTblMapper.updateByPrimaryKeySelective(psTzCertTypeTbl);
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
}



