/**
 * 
 */
package com.tranzvision.gd.TZTemplateBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailParameterBundle.dao.PsTzEmlsDefTblMapper;
import com.tranzvision.gd.TZEmailParameterBundle.model.PsTzEmlsDefTbl;
import com.tranzvision.gd.TZTemplateBundle.dao.PsTzSmsServTblMapper;
import com.tranzvision.gd.TZTemplateBundle.dao.PsTzTmpDefnTblMapper;
import com.tranzvision.gd.TZTemplateBundle.model.PsTzSmsServTbl;
import com.tranzvision.gd.TZTemplateBundle.model.PsTzTmpDefnTbl;
import com.tranzvision.gd.TZTemplateBundle.model.PsTzTmpDefnTblKey;
import com.tranzvision.gd.TZTemplateParameterBundle.model.PsTzExParaTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
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
	private JacksonUtil jacksonUtil;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private PsTzTmpDefnTblMapper psTzTmpDefnTblMapper; 
	
	@Autowired
	private PsTzEmlsDefTblMapper  psTzEmlsDefTblMapper;
	
	@Autowired
	private PsTzSmsServTblMapper psTzSmsServTblMapper;
	
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
					
					if(!"".equals(tzEmlservId) && tzEmlservId!=null){
						PsTzEmlsDefTbl psTzEmlsDefTbl =  psTzEmlsDefTblMapper.selectByPrimaryKey(tzEmlservId);
						if(psTzEmlsDefTbl!=null){
							mapData.replace("emailaddr", psTzEmlsDefTbl.getTzEmlAddr100());
						}
					}
					
					if(!"".equals(tzSmsServId) && tzSmsServId!=null){
						PsTzSmsServTbl psTzSmsServTbl = psTzSmsServTblMapper.selectByPrimaryKey(tzSmsServId);
						if(psTzSmsServTbl!=null){
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
				
				if("RESTMPLINFO".equals(typeFlag)){
					
					String restempid = String.valueOf(mapData.get("restempid"));
					String restempname = String.valueOf(mapData.get("restempname"));
					String restemporg = String.valueOf(mapData.get("restemporg"));
					
					String sql = "select 'Y' from PS_TZ_TMP_DEFN_TBL where TZ_JG_ID=? and TZ_YMB_ID=?";
					String recExists = sqlQuery.queryForObject(sql, new Object[] { restemporg, restempid }, "String");

					if (null != recExists) {

						PsTzTmpDefnTbl psTzTmpDefnTbl = new PsTzTmpDefnTbl();
						psTzTmpDefnTbl.setTzJgId(restemporg);
						psTzTmpDefnTbl.setTzYmbId(restempid);
						psTzTmpDefnTbl.setTzYmbName(restempname);
						

						psTzTmpDefnTblMapper.updateByPrimaryKeySelective(psTzTmpDefnTbl);

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
				errMsg[1] = "参数：" + errorMsg + " 不存在。";
			}

		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	
}
