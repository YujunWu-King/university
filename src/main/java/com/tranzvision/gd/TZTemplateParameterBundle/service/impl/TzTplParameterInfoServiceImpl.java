/**
 * 
 */
package com.tranzvision.gd.TZTemplateParameterBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZTemplateParameterBundle.dao.PsTzExParaTblMapper;
import com.tranzvision.gd.TZTemplateParameterBundle.model.PsTzExParaTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 函件参数信息，原PS：TZ_GD_EMLSMSSET_PKG:TZ_GD_PARAINFO_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-01
 */
@Service("com.tranzvision.gd.TZTemplateParameterBundle.service.impl.TzTplParameterInfoServiceImpl")
public class TzTplParameterInfoServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private PsTzExParaTblMapper psTzExParaTblMapper;

	/**
	 * 获取参数配置信息
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
			if (jacksonUtil.containsKey("paraid")) {

				String paraid = jacksonUtil.getString("paraid");

				PsTzExParaTbl psTzExParaTbl = null;

				psTzExParaTbl = psTzExParaTblMapper.selectByPrimaryKey(paraid);

				if (psTzExParaTbl != null) {

					Map<String, Object> mapData = new HashMap<String, Object>();
					mapData.put("paraid", psTzExParaTbl.getTzParaId());
					mapData.put("chaname", psTzExParaTbl.getTzParaCname());
					mapData.put("datatype", psTzExParaTbl.getTzParaType());
					mapData.put("desc", psTzExParaTbl.getTzDescr254());

					strRet = jacksonUtil.Map2json(mapData);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该参数信息不存在";
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
		String comma = "";
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

				String paraid = jacksonUtil.getString("paraid");
				String chaname = jacksonUtil.getString("chaname");
				String datatype = jacksonUtil.getString("datatype");
				String desc = jacksonUtil.getString("desc");

				String sql = "select 'Y' from PS_TZ_EX_PARA_TBL where TZ_PARA_ID=?";
				String recExists = sqlQuery.queryForObject(sql, new Object[] { paraid }, "String");

				if (null != recExists) {
					if (!"".equals(conflictKeys)) {
						comma = ",";
					}
					conflictKeys += comma + paraid;
				} else {

					PsTzExParaTbl psTzExParaTbl = new PsTzExParaTbl();
					psTzExParaTbl.setTzParaId(paraid);
					psTzExParaTbl.setTzParaCname(chaname);
					psTzExParaTbl.setTzParaType(datatype);
					psTzExParaTbl.setTzDescr254(desc);
					psTzExParaTbl.setRowAddedDttm(datenow);
					psTzExParaTbl.setRowAddedOprid(oprid);
					psTzExParaTbl.setRowLastmantDttm(datenow);
					psTzExParaTbl.setRowLastmantOprid(oprid);

					psTzExParaTblMapper.insert(psTzExParaTbl);

				}

			}
			if (!"".equals(conflictKeys)) {
				errMsg[0] = "1";
				errMsg[1] = "参数编号为 " + conflictKeys + " 的参数已经存在，请勿重复定义";
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	/**
	 * 更新转换值信息
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

				String paraid = jacksonUtil.getString("paraid");
				String chaname = jacksonUtil.getString("chaname");
				String datatype = jacksonUtil.getString("datatype");
				String desc = jacksonUtil.getString("desc");

				String sql = "select 'Y' from PS_TZ_EX_PARA_TBL where TZ_PARA_ID=?";
				String recExists = sqlQuery.queryForObject(sql, new Object[] { paraid }, "String");

				if (null != recExists) {

					PsTzExParaTbl psTzExParaTbl = new PsTzExParaTbl();
					psTzExParaTbl.setTzParaId(paraid);
					psTzExParaTbl.setTzParaCname(chaname);
					psTzExParaTbl.setTzParaType(datatype);
					psTzExParaTbl.setTzDescr254(desc);
					psTzExParaTbl.setRowLastmantDttm(lastupddttm);
					psTzExParaTbl.setRowLastmantOprid(lastupdoprid);

					psTzExParaTblMapper.updateByPrimaryKeySelective(psTzExParaTbl);

				} else {
					if (!"".equals(errorMsg)) {
						comma = ",";
					}
					errorMsg += comma + paraid;

				}

			}

			if (!"".equals(errorMsg)) {
				errMsg[0] = "1";
				errMsg[1] = "参数：" + errorMsg + " 不存在。";
			}

		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

}
