/**
 * 
 */
package com.tranzvision.gd.TZTemplateBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailParameterBundle.dao.PsTzEmlsDefTblMapper;
import com.tranzvision.gd.TZEmailParameterBundle.model.PsTzEmlsDefTbl;
import com.tranzvision.gd.TZTemplateBundle.dao.PsTzSmsservTblMapper;
import com.tranzvision.gd.TZTemplateBundle.dao.PsTzTmpDefnTblMapper;
import com.tranzvision.gd.TZTemplateBundle.dao.PsTzTmpParaTblMapper;
import com.tranzvision.gd.TZTemplateBundle.dao.PsTzTmpRrkfTblMapper;
import com.tranzvision.gd.TZTemplateBundle.model.PsTzSmsservTbl;
import com.tranzvision.gd.TZTemplateBundle.model.PsTzTmpDefnTbl;
import com.tranzvision.gd.TZTemplateBundle.model.PsTzTmpDefnTblKey;
import com.tranzvision.gd.TZTemplateBundle.model.PsTzTmpParaTblKey;
import com.tranzvision.gd.TZTemplateBundle.model.PsTzTmpRrkfTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 元模板定义查询类，原PS：TZ_GD_EMLSMSSET_PKG:TZ_GD_RESTMPINFO_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-01
 */
@Service("com.tranzvision.gd.TZTemplateBundle.service.impl.TzTemplateInfoServiceImpl")
public class TzTemplateInfoServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private PsTzTmpDefnTblMapper psTzTmpDefnTblMapper;

	@Autowired
	private PsTzEmlsDefTblMapper psTzEmlsDefTblMapper;

	@Autowired
	private PsTzSmsservTblMapper psTzSmsservTblMapper;

	@Autowired
	private PsTzTmpParaTblMapper psTzTmpParaTblMapper;

	@Autowired
	private PsTzTmpRrkfTblMapper psTzTmpRrkfTblMapper;

	/**
	 * 元模板定义查询
	 * 
	 * @param strParams
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("restempid") && jacksonUtil.containsKey("restemporg")) {

				String restempid = jacksonUtil.getString("restempid");
				String restemporg = jacksonUtil.getString("restemporg");

				PsTzTmpDefnTblKey psTzTmpDefnTblKey = new PsTzTmpDefnTblKey();
				psTzTmpDefnTblKey.setTzJgId(restemporg);
				psTzTmpDefnTblKey.setTzYmbId(restempid);
				PsTzTmpDefnTbl psTzTmpDefnTbl = psTzTmpDefnTblMapper.selectByPrimaryKey(psTzTmpDefnTblKey);

				if (psTzTmpDefnTbl != null) {

					String tzEmlservId = psTzTmpDefnTbl.getTzEmlservId();
					String tzSmsServId = psTzTmpDefnTbl.getTzSmsServId();

					Map<String, Object> mapData = new HashMap<String, Object>();
					mapData.put("restempid", psTzTmpDefnTbl.getTzYmbId());
					mapData.put("restempname", psTzTmpDefnTbl.getTzYmbName());
					mapData.put("restemporg", psTzTmpDefnTbl.getTzJgId());
					mapData.put("isneed", psTzTmpDefnTbl.getTzUseFlag());
					mapData.put("isDefaultOpen", psTzTmpDefnTbl.getTzDefaultOpen());
					mapData.put("isExtendChildTmpl", psTzTmpDefnTbl.getTzExtendCTmpl());
					mapData.put("ispersonalize", psTzTmpDefnTbl.getTzDtgxhhbFlg());
					mapData.put("baseinfodesc", psTzTmpDefnTbl.getTzYmbDesc());
					mapData.put("tempemailserv", tzEmlservId);
					mapData.put("emailaddr", "");
					mapData.put("tempsmsserv", tzSmsServId);
					mapData.put("smssevname", "");
					mapData.put("tempclassalias", psTzTmpDefnTbl.getTzYmbCslbm());
					mapData.put("restempopra", psTzTmpDefnTbl.getTzYmbNrgm());
					mapData.put("contclassid", psTzTmpDefnTbl.getTzAppcls());
					mapData.put("recname", psTzTmpDefnTbl.getTzDsrecName());
					mapData.put("recalias", psTzTmpDefnTbl.getTzDsrecAlias());

					if (!"".equals(tzEmlservId) && tzEmlservId != null) {
						PsTzEmlsDefTbl psTzEmlsDefTbl = psTzEmlsDefTblMapper.selectByPrimaryKey(tzEmlservId);
						if (psTzEmlsDefTbl != null) {
							mapData.replace("emailaddr", psTzEmlsDefTbl.getTzEmlAddr100());
						}
					}

					if (!"".equals(tzSmsServId) && tzSmsServId != null) {
						PsTzSmsservTbl psTzSmsServTbl = psTzSmsservTblMapper.selectByPrimaryKey(tzSmsServId);
						if (psTzSmsServTbl != null) {
							mapData.replace("smssevname", psTzSmsServTbl.getTzSmsServName());
						}
					}

					strRet = jacksonUtil.Map2json(mapData);
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "参数错误";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/**
	 * 修改元模板定义信息
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		String errorMsg = "";
		String comma = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			Date lastupddttm = new Date();
			String lastupdoprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> mapData = jacksonUtil.getMap("data");

				if ("RESTMPLINFO".equals(typeFlag)) {

					String restempid = String.valueOf(mapData.get("restempid"));
					String restempname = String.valueOf(mapData.get("restempname"));
					String restemporg = String.valueOf(mapData.get("restemporg"));

					String sql = "select 'Y' from PS_TZ_TMP_DEFN_TBL where TZ_JG_ID=? and TZ_YMB_ID=?";
					String recExists = sqlQuery.queryForObject(sql, new Object[] { restemporg, restempid }, "String");

					if (null != recExists) {

						String tzUseFlag = String.valueOf(mapData.get("isneed"));
						String tzDefaultOpen = String.valueOf(mapData.get("isDefaultOpen"));
						String tzExtendCTmpl = String.valueOf(mapData.get("isExtendChildTmpl"));
						String tzDtgxhhbFlg = String.valueOf(mapData.get("ispersonalize"));
						String tzYmbDesc = String.valueOf(mapData.get("baseinfodesc"));
						String tzEmlservId = String.valueOf(mapData.get("tempemailserv"));
						String tzSmsServId = String.valueOf(mapData.get("tempsmsserv"));
						String tzYmbCslbm = String.valueOf(mapData.get("tempclassalias"));
						String tzYmbNrgm = String.valueOf(mapData.get("restempopra"));
						String tzDsrecName = String.valueOf(mapData.get("recname"));
						String tzDsrecAlias = String.valueOf(mapData.get("recalias"));
						String tzAppcls = String.valueOf(mapData.get("contclassid"));

						PsTzTmpDefnTbl psTzTmpDefnTbl = new PsTzTmpDefnTbl();
						psTzTmpDefnTbl.setTzJgId(restemporg);
						psTzTmpDefnTbl.setTzYmbId(restempid);
						psTzTmpDefnTbl.setTzYmbName(restempname);
						psTzTmpDefnTbl.setTzUseFlag(tzUseFlag);
						psTzTmpDefnTbl.setTzDefaultOpen(tzDefaultOpen);
						psTzTmpDefnTbl.setTzExtendCTmpl(tzExtendCTmpl);
						psTzTmpDefnTbl.setTzDtgxhhbFlg(tzDtgxhhbFlg);
						psTzTmpDefnTbl.setTzYmbDesc(tzYmbDesc);
						psTzTmpDefnTbl.setTzEmlservId(tzEmlservId);
						psTzTmpDefnTbl.setTzSmsServId(tzSmsServId);
						psTzTmpDefnTbl.setTzYmbCslbm(tzYmbCslbm);
						psTzTmpDefnTbl.setTzYmbNrgm(tzYmbNrgm);
						psTzTmpDefnTbl.setTzDsrecName(tzDsrecName);
						psTzTmpDefnTbl.setTzDsrecAlias(tzDsrecAlias);
						psTzTmpDefnTbl.setTzAppcls(tzAppcls);
						psTzTmpDefnTbl.setRowLastmantDttm(lastupddttm);
						psTzTmpDefnTbl.setRowLastmantOprid(lastupdoprid);

						psTzTmpDefnTblMapper.updateByPrimaryKeySelective(psTzTmpDefnTbl);

						Map<String, Object> mapJson = new HashMap<String, Object>();
						mapJson.put("restempid", restempid);
						mapJson.put("restemporg", restemporg);
						strRet = jacksonUtil.Map2json(mapJson);

					} else {
						if (!"".equals(errorMsg)) {
							comma = ",";
						}
						errorMsg += comma + restempid;

					}

				}

			}

			if (!"".equals(errorMsg)) {
				errMsg[0] = "1";
				errMsg[1] = "模板：" + errorMsg + " 不存在。";
			}

		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/**
	 * 新增参数
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		String conflictKeys = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			Date datenow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> mapData = jacksonUtil.getMap("data");

				if ("RESTMPLINFO".equals(typeFlag)) {

					String restempname = String.valueOf(mapData.get("restempname"));
					String restemporg = String.valueOf(mapData.get("restemporg"));
					String restempid = "";
					boolean bolRst = false;
					int loopTimes = 5;
					while (!bolRst && loopTimes > 0) {
						
						restempid = String.valueOf(getSeqNum.getSeqNum("PS_TZ_TMP_DEFN_TBL", "TZ_YMB_ID"));

						String sql = "select 'Y' from PS_TZ_TMP_DEFN_TBL where TZ_JG_ID=? and TZ_YMB_ID=?";
						String recExists = sqlQuery.queryForObject(sql, new Object[] { restemporg, restempid },
								"String");

						if (null == recExists) {
							bolRst = true;
							conflictKeys = "";
						} else {
							conflictKeys = "0";
						}

						loopTimes--;
					}

					if (bolRst) {
						String tzUseFlag = String.valueOf(mapData.get("isneed"));
						String tzDefaultOpen = String.valueOf(mapData.get("isDefaultOpen"));
						String tzExtendCTmpl = String.valueOf(mapData.get("isExtendChildTmpl"));
						String tzDtgxhhbFlg = String.valueOf(mapData.get("ispersonalize"));
						String tzYmbDesc = String.valueOf(mapData.get("baseinfodesc"));
						String tzEmlservId = String.valueOf(mapData.get("tempemailserv"));
						String tzSmsServId = String.valueOf(mapData.get("tempsmsserv"));
						String tzYmbCslbm = String.valueOf(mapData.get("tempclassalias"));
						String tzYmbNrgm = String.valueOf(mapData.get("restempopra"));
						String tzDsrecName = String.valueOf(mapData.get("recname"));
						String tzDsrecAlias = String.valueOf(mapData.get("recalias"));
						String tzAppcls = String.valueOf(mapData.get("contclassid"));

						PsTzTmpDefnTbl psTzTmpDefnTbl = new PsTzTmpDefnTbl();
						psTzTmpDefnTbl.setTzJgId(restemporg);
						psTzTmpDefnTbl.setTzYmbId(restempid);
						psTzTmpDefnTbl.setTzYmbName(restempname);
						psTzTmpDefnTbl.setTzUseFlag(tzUseFlag);
						psTzTmpDefnTbl.setTzDefaultOpen(tzDefaultOpen);
						psTzTmpDefnTbl.setTzExtendCTmpl(tzExtendCTmpl);
						psTzTmpDefnTbl.setTzDtgxhhbFlg(tzDtgxhhbFlg);
						psTzTmpDefnTbl.setTzYmbDesc(tzYmbDesc);
						psTzTmpDefnTbl.setTzEmlservId(tzEmlservId);
						psTzTmpDefnTbl.setTzSmsServId(tzSmsServId);
						psTzTmpDefnTbl.setTzYmbCslbm(tzYmbCslbm);
						psTzTmpDefnTbl.setTzYmbNrgm(tzYmbNrgm);
						psTzTmpDefnTbl.setTzDsrecName(tzDsrecName);
						psTzTmpDefnTbl.setTzDsrecAlias(tzDsrecAlias);
						psTzTmpDefnTbl.setTzAppcls(tzAppcls);
						psTzTmpDefnTbl.setRowAddedDttm(datenow);
						psTzTmpDefnTbl.setRowAddedOprid(oprid);
						psTzTmpDefnTbl.setRowLastmantDttm(datenow);
						psTzTmpDefnTbl.setRowLastmantOprid(oprid);

						psTzTmpDefnTblMapper.insert(psTzTmpDefnTbl);

						Map<String, Object> mapJson = new HashMap<String, Object>();
						mapJson.put("restempid", restempid);
						mapJson.put("restemporg", restemporg);
						strRet = jacksonUtil.Map2json(mapJson);
					}
				}

			}
			if (!"".equals(conflictKeys)) {
				errMsg[0] = "1";
				errMsg[1] = "生成模板编号失败，请重试";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	/**
	 * 参数列表和内容模板查询
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		jacksonUtil.json2Map(strParams);
		String listtype = jacksonUtil.getString("listtype");
		String restempid = jacksonUtil.getString("restempid");
		String restemporg = jacksonUtil.getString("restemporg");

		ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
		try {
			if ("PARA".equals(listtype)) {

				String sql = "select TZ_PARA_ID,TZ_PARA_ALIAS,TZ_SYSVARID from PS_TZ_TMP_PARA_TBL where TZ_JG_ID=? AND TZ_YMB_ID=?";
				List<?> listData = sqlQuery.queryForList(sql, new Object[] { restemporg, restempid });
				for (Object objData : listData) {

					Map<String, Object> mapData = (Map<String, Object>) objData;
					String strParaId = "";
					String strParaCname = "";
					String strSysvarid = "";
					String strSysvarname = "";
					String paraalias = null == mapData.get("TZ_PARA_ID") ? ""
							: String.valueOf(mapData.get("TZ_PARA_ID"));

					if (null != mapData.get("TZ_PARA_ID")) {
						sql = "select TZ_PARA_CNAME from PS_TZ_EX_PARA_TBL where TZ_PARA_ID = ?";
						strParaId = String.valueOf(mapData.get("TZ_PARA_ID"));
						strParaCname = sqlQuery.queryForObject(sql, new Object[] { strParaId }, "String");

					}
					if (null != mapData.get("TZ_SYSVARID")) {
						sql = "select TZ_SYSVARNAME from PS_TZ_SYSVAR_T where TZ_SYSVARID = ?";
						strSysvarid = String.valueOf(mapData.get("TZ_SYSVARID"));
						strSysvarname = sqlQuery.queryForObject(sql, new Object[] { strSysvarid }, "String");
					}

					Map<String, Object> mapJson = new HashMap<String, Object>();
					mapJson.put("restempid", restempid);
					mapJson.put("restemporg", restemporg);
					mapJson.put("paraid", strParaId);
					mapJson.put("paraname", strParaCname);
					mapJson.put("paraalias", paraalias);
					mapJson.put("systvar", strSysvarid);
					mapJson.put("systvarname", strSysvarname);

					listJson.add(mapJson);
				}

			} else if ("CONTENT".equals(listtype)) {

				String sql = "select TZ_KEY_NAME,TZ_PARA_ID from PS_TZ_TMP_RRKF_TBL where TZ_JG_ID=? AND TZ_YMB_ID=?";
				List<?> listData = sqlQuery.queryForList(sql, new Object[] { restemporg, restempid });
				for (Object objData : listData) {

					Map<String, Object> mapData = (Map<String, Object>) objData;

					String paraid = "";
					String keyname = null == mapData.get("TZ_KEY_NAME") ? ""
							: String.valueOf(mapData.get("TZ_KEY_NAME"));
					String paraname = "";
					if (null != mapData.get("TZ_PARA_ID")) {
						sql = "select TZ_PARA_CNAME from PS_TZ_EX_PARA_TBL where TZ_PARA_ID = ?";
						paraid = String.valueOf(mapData.get("TZ_PARA_ID"));
						paraname = sqlQuery.queryForObject(sql, new Object[] { paraid }, "String");
					}

					Map<String, Object> mapJson = new HashMap<String, Object>();
					mapJson.put("restempid", restempid);
					mapJson.put("restemporg", restemporg);
					mapJson.put("keyname", keyname);
					mapJson.put("paraid", paraid);
					mapJson.put("paraname", paraname);

					listJson.add(mapJson);

				}

			} else {
				errorMsg[0] = "1";
				errorMsg[1] = "请求错误";
			}

			mapRet.replace("root", listJson);

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}

		return jacksonUtil.Map2json(mapRet);

	}

	/**
	 * 删除新增元模板参数信息
	 */
	@Override
	@Transactional
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 提交信息
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 消息集合id
				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> infoData = jacksonUtil.getMap("data");

				String restempid = jacksonUtil.getString("restempid");
				String restemporg = jacksonUtil.getString("restemporg");

				if (null == restempid || "".equals(restempid)) {
					continue;
				}

				if (null == restemporg || "".equals(restemporg)) {
					continue;
				}

				if ("RESTMPLPARA".equals(typeFlag)) {
					String paraid = null == infoData.get("paraid") ? "" : String.valueOf(infoData.get("paraid"));
					if (!"".equals(paraid)) {

						// 删除参数项
						PsTzTmpParaTblKey psTzTmpParaTblKey = new PsTzTmpParaTblKey();
						psTzTmpParaTblKey.setTzJgId(restemporg);
						psTzTmpParaTblKey.setTzParaId(paraid);
						psTzTmpParaTblKey.setTzYmbId(restempid);

						psTzTmpParaTblMapper.deleteByPrimaryKey(psTzTmpParaTblKey);

					}

				} else if ("RESTMPLCONTENT".equals(typeFlag)) {
					String keyname = null == infoData.get("keyname") ? "" : String.valueOf(infoData.get("keyname"));
					if (!"".equals(keyname)) {

						// 删除机构角色
						PsTzTmpRrkfTblKey psTzTmpRrkfTblKey = new PsTzTmpRrkfTblKey();
						psTzTmpRrkfTblKey.setTzJgId(restemporg);
						psTzTmpRrkfTblKey.setTzKeyName(keyname);
						psTzTmpRrkfTblKey.setTzYmbId(restempid);

						psTzTmpRrkfTblMapper.deleteByPrimaryKey(psTzTmpRrkfTblKey);
					}
				}

				Map<String, Object> mapJson = new HashMap<String, Object>();
				mapJson.put("restempid", restempid);
				mapJson.put("restemporg", restemporg);
				strRet = jacksonUtil.Map2json(mapJson);
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
