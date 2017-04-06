package com.tranzvision.gd.TZDistributionTableBundle.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZDistributionTableBundle.dao.PsTzFbdzMxTblMapper;
import com.tranzvision.gd.TZDistributionTableBundle.dao.PsTzFbdzTblMapper;
import com.tranzvision.gd.TZDistributionTableBundle.model.PsTzFbdzMxTbl;
import com.tranzvision.gd.TZDistributionTableBundle.model.PsTzFbdzMxTblKey;
import com.tranzvision.gd.TZDistributionTableBundle.model.PsTzFbdzTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZDistributionTableBundle.service.impl.DistributionTableInfoServiceImpl")
public class DistributionTableInfoServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private PsTzFbdzTblMapper psTzFbdzTblMapper;
	
	@Autowired
	private PsTzFbdzMxTblMapper psTzFbdzMxTblMapper;

	@Autowired
	private GetSeqNum getSeqNum;
	/**
	 * 分布对照表明细设置
	 */
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", new ArrayList<Map<String, Object>>());

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String distrId = jacksonUtil.getString("distrId");

			if (null != distrId && !"".equals(distrId)) {
				// 查询总数
				String sql = "SELECT COUNT(*) FROM PS_TZ_FBDZ_MX_TBL WHERE TZ_M_FBDZ_ID=?";
				int total = jdbcTemplate.queryForObject(sql, new Object[] { distrId }, "int");

				ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
				if (total > 0) {
					sql = "SELECT TZ_M_FBDZ_MX_ID,TZ_M_FBDZ_MX_XH,TZ_M_FBDZ_MX_NAME,TZ_M_FBDZ_MX_SM,TZ_M_FBDZ_MX_XX_JX,TZ_M_FBDZ_MX_XX,TZ_M_FBDZ_MX_SX_JX,TZ_M_FBDZ_MX_SX FROM PS_TZ_FBDZ_MX_TBL WHERE TZ_M_FBDZ_ID=? ORDER BY TZ_M_FBDZ_MX_ID";
					List<Map<String, Object>> listData = jdbcTemplate.queryForList(sql,
							new Object[] { distrId });
					
					for (Map<String, Object> mapData : listData) {
						Map<String, Object> mapJson = new HashMap<String, Object>();

						String distrMXId = mapData.get("TZ_M_FBDZ_MX_ID").toString();
						String sequence = mapData.get("TZ_M_FBDZ_MX_XH").toString();
						String name = mapData.get("TZ_M_FBDZ_MX_NAME").toString();
						String explain = mapData.get("TZ_M_FBDZ_MX_SM").toString();
						String lowOpt = mapData.get("TZ_M_FBDZ_MX_XX_JX").toString();
						String lowScore = mapData.get("TZ_M_FBDZ_MX_XX").toString();
						String upOpt = mapData.get("TZ_M_FBDZ_MX_SX_JX").toString();
						String upScore = mapData.get("TZ_M_FBDZ_MX_SX").toString();
						if(lowOpt.equals(">=")){
							lowOpt = "大于等于";
						}else if(lowOpt.equals(">")){
							lowOpt = "大于";
						}
						if(upOpt.equals("<=")){
							upOpt = "小于等于";
						}else if(upOpt.equals("<")){
							upOpt = "小于";
						}
						mapJson.put("distrId", distrId);
						mapJson.put("distrMXId", distrMXId);
						mapJson.put("sequence", sequence);
						mapJson.put("name", name);
						mapJson.put("explain", explain);
						mapJson.put("lowOpt", lowOpt);
						mapJson.put("lowScore", lowScore);
						mapJson.put("upOpt", upOpt);
						mapJson.put("upScore", upScore);

						listJson.add(mapJson);
					}
					mapRet.replace("total", total);
					mapRet.replace("root", listJson);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "参数不正确！";
		}

		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}

	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("result", 0);
		mapRet.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 获取当前登录机构
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			// 当前登录人
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

//			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String strFlag = jacksonUtil.getString("type");
				if ("tableInfo".equals(strFlag)) {
					// 信息内容;
					Map<String, Object> infoData = jacksonUtil.getMap("data");
					String distrId = (String) infoData.get("distrId");
					String distrName = (String) infoData.get("distrName");
					String status = (String) infoData.get("status");

					String sql = "select COUNT(1) from PS_TZ_FBDZ_TBL WHERE TZ_M_FBDZ_ID=? AND TZ_JG_ID=?";
					int count = jdbcTemplate.queryForObject(sql, new Object[] { distrId, orgId }, "Integer");
					if (count > 0) {
						errMsg[0] = "1";
						errMsg[1] = "当前机构下分布对照表编号为：" + distrId + "的信息已经存在。";
						return strRet;
					}
					PsTzFbdzTbl psTzFbdzTbl = new PsTzFbdzTbl();
					psTzFbdzTbl.setTzJgId(orgId);
					psTzFbdzTbl.setTzMFbdzId(distrId);
					psTzFbdzTbl.setTzMFbdzName(distrName);
					psTzFbdzTbl.setTzMFbdzZt(status);

					psTzFbdzTbl.setRowAddedDttm(new Date());
					psTzFbdzTbl.setRowAddedOprid(oprid);
					psTzFbdzTbl.setRowLastmantDttm(new Date());
					psTzFbdzTbl.setRowLastmantOprid(oprid);
					int rtn = psTzFbdzTblMapper.insert(psTzFbdzTbl);
					if(rtn != 0){
						Map<String, Object> mapForm = new HashMap<String, Object>();
						mapForm.put("distrId", distrId);
						mapForm.put("distrName", distrName);
						mapRet.replace("result", "success");
						mapRet.replace("formData", mapForm);
					}else{
						errMsg[0] = "1";
						errMsg[1] = "保存失败";
						mapRet.replace("result", "fail");
					}
				} else {
					errMsg[0] = "1";
					errMsg[1] = "分布对照表编号已存在";
					mapRet.replace("result", "fail");
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("result", 0);
		mapRet.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 获取当前登录机构
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			// 当前登录人
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);

				String type = jacksonUtil.getString("type");

				if ("tableInfo".equals(type)) {
					Map<String, Object> formDate = jacksonUtil.getMap("data");

					String distrId = formDate.get("distrId").toString();
					String distrName = formDate.get("distrName").toString();
					String status = formDate.get("status").toString();
					PsTzFbdzTbl psTzFbdzTbl = new PsTzFbdzTbl();
					psTzFbdzTbl.setTzJgId(orgId);
					psTzFbdzTbl.setTzMFbdzId(distrId);
					psTzFbdzTbl.setTzMFbdzName(distrName);
					psTzFbdzTbl.setTzMFbdzZt(status);
					
					psTzFbdzTbl.setRowLastmantOprid(oprid);
					psTzFbdzTbl.setRowLastmantDttm(new Date());
					psTzFbdzTblMapper.updateByPrimaryKeySelective(psTzFbdzTbl);
				} else if ("ItemsInfo".equals(type)) {
					List<Map<String, Object>> itemsList = (List<Map<String, Object>>) jacksonUtil.getList("data");

					for (Map<String, Object> itemMap : itemsList) {
//						
						String distrMXId = itemMap.get("distrMXId").toString();
						if(distrMXId == null || "".equals(distrMXId)){
							Integer distrMXIdint = getSeqNum.getSeqNum("PS_TZ_FBDZ_MX_TBL", "TZ_M_FBDZ_MX_ID");
							distrMXId = distrMXIdint.toString();
						}
						String distrId = itemMap.get("distrId").toString();
						String sequence = itemMap.get("sequence").toString();
						String name = itemMap.get("name").toString();
						String explain = itemMap.get("explain").toString();
						String lowOpt = itemMap.get("lowOpt").toString();
						String lowScore = itemMap.get("lowScore").toString();
						String upOpt = itemMap.get("upOpt").toString();
						String upScore = itemMap.get("upScore").toString();
						if(lowOpt.equals("大于等于")){
							lowOpt = ">=";
						}else if(lowOpt.equals("大于")){
							lowOpt = ">";
						}
						if(upOpt.equals("小于等于")){
							upOpt = "<=";
						}else if(upOpt.equals("小于")){
							upOpt = "<";
						}
						PsTzFbdzMxTblKey psTzFbdzMxTblKey = new PsTzFbdzMxTblKey();
						psTzFbdzMxTblKey.setTzMFbdzId(distrId);
						psTzFbdzMxTblKey.setTzMFbdzMxId(distrMXId);
						
						PsTzFbdzMxTbl psTzFbdzMxTbl = psTzFbdzMxTblMapper.selectByPrimaryKey(psTzFbdzMxTblKey);

						if (psTzFbdzMxTbl == null) {
							psTzFbdzMxTbl = new PsTzFbdzMxTbl();
							psTzFbdzMxTbl.setTzMFbdzId(distrId);
							psTzFbdzMxTbl.setTzMFbdzMxId(distrMXId);
							psTzFbdzMxTbl.setTzMFbdzMxXh(sequence);
							psTzFbdzMxTbl.setTzMFbdzMxName(name);
							psTzFbdzMxTbl.setTzMFbdzMxSm(explain);
							psTzFbdzMxTbl.setTzMFbdzMxSx(new BigDecimal(upScore));
							psTzFbdzMxTbl.setTzMFbdzMxXx(new BigDecimal(lowScore));
							psTzFbdzMxTbl.setTzMFbdzMxSxJx(upOpt);
							psTzFbdzMxTbl.setTzMFbdzMxXxJx(lowOpt);
							psTzFbdzMxTblMapper.insert(psTzFbdzMxTbl);
						} else {
							psTzFbdzMxTbl.setTzMFbdzId(distrId);
							psTzFbdzMxTbl.setTzMFbdzMxId(distrMXId);
							psTzFbdzMxTbl.setTzMFbdzMxXh(sequence);
							psTzFbdzMxTbl.setTzMFbdzMxName(name);
							psTzFbdzMxTbl.setTzMFbdzMxSm(explain);
							psTzFbdzMxTbl.setTzMFbdzMxSx(new BigDecimal(upScore));
							psTzFbdzMxTbl.setTzMFbdzMxXx(new BigDecimal(lowScore));
							psTzFbdzMxTbl.setTzMFbdzMxSxJx(upOpt);
							psTzFbdzMxTbl.setTzMFbdzMxXxJx(lowOpt);
							psTzFbdzMxTblMapper.updateByPrimaryKeySelective(psTzFbdzMxTbl);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("result", 0);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);

				String type = jacksonUtil.getString("type");

				if ("ItemsInfo".equals(type)) {
					List<Map<String, Object>> itemsList = (List<Map<String, Object>>) jacksonUtil.getList("data");

					for (Map<String, Object> itemMap : itemsList) {
						String distrId = itemMap.get("distrId").toString();
						String distrMXId = itemMap.get("distrMXId").toString();
						if(distrId != null && !"".equals(distrId) && distrMXId != null && !"".equals(distrMXId)){
					    	
					    	String deleteSQL = "DELETE FROM PS_TZ_FBDZ_MX_TBL WHERE TZ_M_FBDZ_ID=? AND TZ_M_FBDZ_MX_ID=?";
					    	jdbcTemplate.update(deleteSQL, new Object[]{distrId,distrMXId});
					    	
					    }
					}
					mapRet.replace("result", "success");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}
}
