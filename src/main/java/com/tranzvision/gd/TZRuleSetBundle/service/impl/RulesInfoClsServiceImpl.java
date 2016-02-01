package com.tranzvision.gd.TZRuleSetBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZRuleSetBundle.dao.PsTzJygzDyEngMapper;
import com.tranzvision.gd.TZRuleSetBundle.dao.PsTzJygzDyTMapper;
import com.tranzvision.gd.TZRuleSetBundle.model.PsTzJygzDyEng;
import com.tranzvision.gd.TZRuleSetBundle.model.PsTzJygzDyEngKey;
import com.tranzvision.gd.TZRuleSetBundle.model.PsTzJygzDyT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
/**
 * @author WRL TZ_JYGZ_GL_PKG:TZ_JYGZ_INFO_CLS
 * 校验规则定义
 */
@Service("com.tranzvision.gd.TZRuleSetBundle.service.impl.RulesInfoClsServiceImpl")
public class RulesInfoClsServiceImpl extends FrameworkImpl {
	
	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private PsTzJygzDyTMapper psTzJygzDyTMapper;

	@Autowired
	private PsTzJygzDyEngMapper psTzJygzDyEngMapper;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	
	/**
	 * 校验规则定义
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
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			Date dateNow = new Date();
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> infoData = jacksonUtil.getMap("data");

				if(StringUtils.equals(typeFlag, "JYGZ")){
					String jygzId = (String) infoData.get("jygzID");
					String jygzName = (String) infoData.get("jygzName");
					String jygzClssName = (String) infoData.get("jygzClssName");
					String jygzFwdJy = (String) infoData.get("jygzFwdJy");
					String jygzState = (String) infoData.get("jygzState");
					String jygzTsxx = (String) infoData.get("jygzTsxx");
					String jygzEnTsxx = (String) infoData.get("jygzEnTsxx");
					
					String sql = "SELECT 'Y' FROM PS_TZ_JYGZ_DY_T WHERE TZ_JYGZ_ID=?";
					String recExists = sqlQuery.queryForObject(sql, new Object[] { jygzId }, "String");

					if (null != recExists) {
						conflictKeys += comma + jygzId;
						comma = ",";
					} else {
						PsTzJygzDyT psTzJygzDyT = new PsTzJygzDyT();
						psTzJygzDyT.setTzJygzId(jygzId);
						psTzJygzDyT.setTzJygzMc(jygzName);
						psTzJygzDyT.setTzJygzJslmc(jygzClssName);
						psTzJygzDyT.setTzJygzAppclsid(jygzFwdJy);
						psTzJygzDyT.setTzEffexpZt(jygzState);						
						psTzJygzDyT.setTzJygzTsxx(jygzTsxx);
						psTzJygzDyT.setRowAddedDttm(dateNow);
						psTzJygzDyT.setRowAddedOprid(oprid);
						psTzJygzDyT.setRowLastmantDttm(dateNow);
						psTzJygzDyT.setRowLastmantOprid(oprid);
						int i = psTzJygzDyTMapper.insert(psTzJygzDyT);
						if (i > 0) {
							PsTzJygzDyEng psTzJygzDyEng = new PsTzJygzDyEng();
							psTzJygzDyEng.setTzJygzId(jygzId);
							psTzJygzDyEng.setTzJygzTsxx(jygzEnTsxx);
							psTzJygzDyEng.setLanguageCd("ENG");
							psTzJygzDyEngMapper.insert(psTzJygzDyEng);
						} else {
							errMsg[0] = "1";
							errMsg[1] = "校验规则保存失败";
						}
					}
				}
			}
			if (!"".equals(conflictKeys)) {
				errMsg[0] = "1";
				errMsg[1] = "校验规则编号为：" + conflictKeys + "的信息已经存在。";
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}
	
	/**
	 * 获取规则定义信息
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
			if (jacksonUtil.containsKey("jygzID")) {

				String jygzID = jacksonUtil.getString("jygzID");
				PsTzJygzDyT psTzJygzDyT = psTzJygzDyTMapper.selectByPrimaryKey(jygzID);
				
				PsTzJygzDyEngKey psTzJygzDyEngKey = new PsTzJygzDyEngKey();
				psTzJygzDyEngKey.setTzJygzId(jygzID);
				psTzJygzDyEngKey.setLanguageCd("ENG");

				PsTzJygzDyEng psTzJygzDyEng = psTzJygzDyEngMapper.selectByPrimaryKey(psTzJygzDyEngKey);
				
				if (psTzJygzDyT != null) {
					Map<String, Object> mapData = new HashMap<String, Object>();
					
					mapData.put("jygzID", psTzJygzDyT.getTzJygzId());
					mapData.put("jygzName", psTzJygzDyT.getTzJygzMc());
					mapData.put("jygzClssName", psTzJygzDyT.getTzJygzJslmc());
					mapData.put("jygzFwdJy", psTzJygzDyT.getTzJygzAppclsid());
					mapData.put("jygzTsxx", psTzJygzDyT.getTzJygzTsxx());
					if(psTzJygzDyEng != null){
						mapData.put("jygzEnTsxx", psTzJygzDyEng.getTzJygzTsxx());
					}else{
						mapData.put("jygzEnTsxx", "");
					}

					mapData.put("jygzState", psTzJygzDyT.getTzEffexpZt());

					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("formData", mapData);

					strRet = jacksonUtil.Map2json(mapRet);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该消息集合数据不存在";
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
	 * 修改消息集合信息
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		try {
			Date dateNow = new Date();
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				if(StringUtils.equals(typeFlag, "JYGZ")){

					String jygzId = (String) infoData.get("jygzID");
					String jygzName = (String) infoData.get("jygzName");
					String jygzClssName = (String) infoData.get("jygzClssName");
					String jygzFwdJy = (String) infoData.get("jygzFwdJy");
					String jygzState = (String) infoData.get("jygzState");
					String jygzTsxx = (String) infoData.get("jygzTsxx");
					String jygzEnTsxx = (String) infoData.get("jygzEnTsxx");
					
					System.out.println("-----1.----- " +  jygzId);
					String sql = "SELECT COUNT(1) FROM PS_TZ_JYGZ_DY_T WHERE TZ_JYGZ_ID=?";
					int count = sqlQuery.queryForObject(sql, new Object[] { jygzId }, "Integer");

					if (count > 0) {
						PsTzJygzDyT psTzJygzDyT = new PsTzJygzDyT();
						psTzJygzDyT.setTzJygzId(jygzId);
						psTzJygzDyT.setTzJygzMc(jygzName);
						psTzJygzDyT.setTzJygzJslmc(jygzClssName);
						psTzJygzDyT.setTzJygzAppclsid(jygzFwdJy);
						psTzJygzDyT.setTzEffexpZt(jygzState);						
						psTzJygzDyT.setTzJygzTsxx(jygzTsxx);
						psTzJygzDyT.setRowLastmantDttm(dateNow);
						psTzJygzDyT.setRowLastmantOprid(oprid);
						psTzJygzDyTMapper.updateByPrimaryKeySelective(psTzJygzDyT);

						PsTzJygzDyEng psTzJygzDyEng = new PsTzJygzDyEng();
						psTzJygzDyEng.setTzJygzId(jygzId);
						psTzJygzDyEng.setTzJygzTsxx(jygzEnTsxx);
						psTzJygzDyEng.setLanguageCd("ENG");
						psTzJygzDyEngMapper.updateByPrimaryKeySelective(psTzJygzDyEng);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "校验规则：" + jygzId + "，不存在";
					}
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
}
