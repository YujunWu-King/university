package com.tranzvision.gd.TZControlSetBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZControlSetBundle.dao.PsTzComDyEngMapper;
import com.tranzvision.gd.TZControlSetBundle.dao.PsTzComDyTMapper;
import com.tranzvision.gd.TZControlSetBundle.dao.PsTzComJygzpzTMapper;
import com.tranzvision.gd.TZControlSetBundle.dao.PsTzComYtTMapper;
import com.tranzvision.gd.TZControlSetBundle.model.PsTzComDyEng;
import com.tranzvision.gd.TZControlSetBundle.model.PsTzComDyEngKey;
import com.tranzvision.gd.TZControlSetBundle.model.PsTzComDyTWithBLOBs;
import com.tranzvision.gd.TZControlSetBundle.model.PsTzComJygzpzT;
import com.tranzvision.gd.TZControlSetBundle.model.PsTzComYtT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * @author WRL TZ_KJ_GL_PKG:TZ_KJ_INFO_CLS
 * 
 * 控件详情
 */
@Service("com.tranzvision.gd.TZControlSetBundle.service.impl.ComsInfoClsServiceImpl")
public class ComsInfoClsServiceImpl extends FrameworkImpl {
	
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private PsTzComDyTMapper psTzComDyTMapper;
	
	@Autowired
	private PsTzComDyEngMapper psTzComDyEngMapper;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private PsTzComYtTMapper psTzComYtTMapper;
	
	@Autowired
	private PsTzComJygzpzTMapper psTzComJygzpzTMapper;

	/**
	 * 获取控件定义信息
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
			if (jacksonUtil.containsKey("kjID")) {

				String kjID = jacksonUtil.getString("kjID");
				
				PsTzComDyTWithBLOBs psTzComDyT = psTzComDyTMapper.selectByPrimaryKey(kjID);
				
				PsTzComDyEngKey psTzComDyEngKey = new PsTzComDyEngKey();
				psTzComDyEngKey.setTzComId(kjID);
				psTzComDyEngKey.setLanguageCd("ENG");

				PsTzComDyEng psTzComDyEng = psTzComDyEngMapper.selectByPrimaryKey(psTzComDyEngKey);
				
				if (psTzComDyT != null) {
					Map<String, Object> mapData = new HashMap<String, Object>();
					
					mapData.put("kjID", psTzComDyT.getTzComId());
					mapData.put("kjName", psTzComDyT.getTzComMc());
					if(psTzComDyEng != null){
						mapData.put("kjEnName", psTzComDyEng.getTzComMc());
					}else{
						mapData.put("kjEnName", "");
					}
					mapData.put("kjJsUrl", psTzComDyT.getTzComJslj());
					mapData.put("kjPtUrl", psTzComDyT.getTzComIconlj());
					mapData.put("kjExpHtml", psTzComDyT.getTzComExphtml());
					mapData.put("kjState", psTzComDyT.getTzEffexpZt());

					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("formData", mapData);

					strRet = jacksonUtil.Map2json(mapRet);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该控件数据不存在";
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
		try {
			int dataLength = actData.length;
			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			for (int num = 0; num < dataLength; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				
				//控件信息
				if(StringUtils.equals(typeFlag, "KJ")){
					String kjID = (String) infoData.get("kjID");
					String kjName = (String) infoData.get("kjName");
					String kjEnName = (String) infoData.get("kjEnName");
					String kjJsUrl = (String) infoData.get("kjJsUrl");
					String kjPtUrl = (String) infoData.get("kjPtUrl");
					String kjExpHtml = (String) infoData.get("kjExpHtml");
					String kjState = (String) infoData.get("kjState");

					String sql = "SELECT COUNT(1) FROM PS_TZ_COM_DY_T WHERE TZ_COM_ID=?";
					int count = sqlQuery.queryForObject(sql, new Object[] { kjID }, "Integer");

					if (count > 0) {
						PsTzComDyTWithBLOBs psTzComDyT = new PsTzComDyTWithBLOBs();
						psTzComDyT.setTzComId(kjID);
						psTzComDyT.setTzComMc(kjName);
						psTzComDyT.setTzComJslj(kjJsUrl);
						psTzComDyT.setTzComIconlj(kjPtUrl);
						psTzComDyT.setTzComExphtml(kjExpHtml);
						psTzComDyT.setTzEffexpZt(kjState);
						psTzComDyT.setRowLastmantDttm(dateNow);
						psTzComDyT.setRowLastmantOprid(oprid);
						psTzComDyTMapper.updateByPrimaryKeySelective(psTzComDyT);

						PsTzComDyEng psTzComDyEng = new PsTzComDyEng();
						psTzComDyEng.setTzComId(kjID);
						psTzComDyEng.setTzComMc(kjEnName);
						psTzComDyEng.setLanguageCd("ENG");
						psTzComDyEngMapper.updateByPrimaryKeySelective(psTzComDyEng);
					} else {
						PsTzComDyTWithBLOBs psTzComDyT = new PsTzComDyTWithBLOBs();
						psTzComDyT.setTzComId(kjID);
						psTzComDyT.setTzComMc(kjName);
						psTzComDyT.setTzComJslj(kjJsUrl);
						psTzComDyT.setTzComIconlj(kjPtUrl);
						psTzComDyT.setTzComExphtml(kjExpHtml);
						psTzComDyT.setTzEffexpZt(kjState);
						psTzComDyT.setRowAddedDttm(dateNow);
						psTzComDyT.setRowAddedOprid(oprid);
						psTzComDyT.setRowLastmantDttm(dateNow);
						psTzComDyT.setRowLastmantOprid(oprid);
						int i = psTzComDyTMapper.insert(psTzComDyT);
						if (i > 0) {
							PsTzComDyEng psTzComDyEng = new PsTzComDyEng();
							psTzComDyEng.setTzComId(kjID);
							psTzComDyEng.setTzComMc(kjEnName);
							psTzComDyEng.setLanguageCd("ENG");
							psTzComDyEngMapper.insert(psTzComDyEng);
						} else {
							errMsg[0] = "1";
							errMsg[1] = "控件：" + kjID + "，不存在";
						}
					}
				}
				//用途信息
				if(StringUtils.equals(typeFlag, "YT")){

					String kjID = jacksonUtil.getString("kjID");
					String isQy = (String) infoData.get("isQy");
					String tz_order = "" + infoData.get("tz_order");
					if(StringUtils.isBlank(tz_order)){
						tz_order = sqlQuery.queryForObject("SELECT MAX(TZ_ORDER) + 1 FROM PS_TZ_COM_YT_T WHERE TZ_COM_ID = ?", new Object[] { kjID }, "String");
						if(StringUtils.isBlank(tz_order)){
							tz_order = "1";
						}
					}
					String kjYtID = (String) infoData.get("kjYtID");
					String kjLx = (String) infoData.get("kjLx");
					
					String sql = "SELECT COUNT(1) FROM PS_TZ_COM_YT_T WHERE TZ_COM_ID = ? AND TZ_COM_YT_ID = ?";
					int count = sqlQuery.queryForObject(sql, new Object[] { kjID,kjYtID }, "Integer");

					if (count > 0) {
						PsTzComYtT psTzComYtT = new PsTzComYtT();
						psTzComYtT.setTzComId(kjID);
						psTzComYtT.setTzComYtId(kjYtID);
						psTzComYtT.setTzComLxId(kjLx);
						psTzComYtT.setTzQyBz(isQy);
						psTzComYtT.setTzOrder(Integer.parseInt(tz_order));
						psTzComYtTMapper.updateByPrimaryKeySelective(psTzComYtT);

					} else {
						PsTzComYtT psTzComYtT = new PsTzComYtT();
						psTzComYtT.setTzComId(kjID);
						psTzComYtT.setTzComYtId(kjYtID);
						psTzComYtT.setTzComLxId(kjLx);
						psTzComYtT.setTzQyBz(isQy);
						psTzComYtT.setTzOrder(Integer.parseInt(tz_order));
						psTzComYtTMapper.insert(psTzComYtT);
					}
				}
				//控件校验规则信息
				if(StringUtils.equals(typeFlag, "KJJYGZ")){
					String kjID = jacksonUtil.getString("kjID");
					String isQy = (String) infoData.get("isQy");
					String tz_order = "" + infoData.get("tz_order");
					if(StringUtils.isBlank(tz_order)){
						tz_order = sqlQuery.queryForObject("SELECT MAX(TZ_ORDER) + 1 FROM PS_TZ_COM_JYGZPZ_T WHERE TZ_COM_ID = ?", new Object[] { kjID }, "String");
						if(StringUtils.isBlank(tz_order)){
							tz_order = "1";
						}
					}
					String kjJygzID = (String) infoData.get("kjJygzID");
					
					String sql = "SELECT COUNT(1) FROM PS_TZ_COM_JYGZPZ_T WHERE TZ_COM_ID = ? AND TZ_JYGZ_ID = ?";
					int count = sqlQuery.queryForObject(sql, new Object[] { kjID,kjJygzID }, "Integer");

					if (count > 0) {
						PsTzComJygzpzT psTzComJygzpzT = new PsTzComJygzpzT();
						psTzComJygzpzT.setTzComId(kjID);
						psTzComJygzpzT.setTzJygzId(kjJygzID);
						psTzComJygzpzT.setTzQyBz(isQy);
						psTzComJygzpzT.setTzOrder(Integer.parseInt(tz_order));
						psTzComJygzpzTMapper.updateByPrimaryKeySelective(psTzComJygzpzT);

					} else {
						PsTzComJygzpzT psTzComJygzpzT = new PsTzComJygzpzT();
						psTzComJygzpzT.setTzComId(kjID);
						psTzComJygzpzT.setTzJygzId(kjJygzID);
						psTzComJygzpzT.setTzQyBz(isQy);
						psTzComJygzpzT.setTzOrder(Integer.parseInt(tz_order));
						psTzComJygzpzTMapper.insert(psTzComJygzpzT);
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
	/*
	 * 删除控件校验规则信息
	 * @see com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl#tzDelete(java.lang.String[], java.lang.String[])
	 */
	@Override
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
                //控件ID
                String kjID = jacksonUtil.getString("kjID");
				// 校验规则id
				String kjJygzID = jacksonUtil.getString("kjJygzID");
				if(StringUtils.isNotEmpty(kjID) && StringUtils.isNotEmpty(kjJygzID)){
					// 删除控件校验规则
					sqlQuery.update("DELETE FROM PS_TZ_COM_JYGZPZ_T WHERE TZ_COM_ID=? AND TZ_JYGZ_ID = ?", new Object[] { kjID,kjJygzID });
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
	
	/* 查询控件列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);

		String kjID = jacksonUtil.getString("kjID");
		String queryType = jacksonUtil.getString("queryType");
		
		if(StringUtils.equals(queryType, "YT")){
			//用途
			String sql = "SELECT TZ_ZHZ_ID, TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID = 'TZ_COM_YT_ID' AND TZ_EFF_STATUS = 'A'";

			List<?> resultlist = sqlQuery.queryForList(sql);

			ArrayList<Map<String, Object>> listRetJson = new ArrayList<Map<String, Object>>();

			for (Object obj : resultlist) {
				Map<String, Object> result = (Map<String, Object>) obj;

				String isQy = "";
				String isQyMx = "";
				String tz_order = "";
				String kjLx = "";
				String kjLxMx = "";
				String kjYtID = result.get("TZ_ZHZ_ID") == null ? "" : String.valueOf(result.get("TZ_ZHZ_ID"));
				String kjYtIDMx = result.get("TZ_ZHZ_DMS") == null ? "" : String.valueOf(result.get("TZ_ZHZ_DMS"));
				
				if(StringUtils.isBlank(kjID) || StringUtils.equals(kjID, "NEXT")){
					kjID = "";
				}else{
					isQy = sqlQuery.queryForObject("SELECT TZ_QY_BZ FROM PS_TZ_COM_YT_T WHERE TZ_COM_ID =? AND TZ_COM_YT_ID = ?", new Object[] { kjID,kjYtID }, "String");
					isQyMx = sqlQuery.queryForObject("SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID = 'TZ_QY_BZ' AND TZ_EFF_STATUS = 'A' AND TZ_ZHZ_ID =?", new Object[] { isQy }, "String");
					tz_order = sqlQuery.queryForObject("SELECT TZ_ORDER FROM PS_TZ_COM_YT_T WHERE TZ_COM_ID =? AND TZ_COM_YT_ID = ?", new Object[] { kjID,kjYtID }, "String");
					kjLx = sqlQuery.queryForObject("SELECT TZ_COM_LX_ID FROM PS_TZ_COM_YT_T WHERE TZ_COM_ID =? AND TZ_COM_YT_ID = ?", new Object[] { kjID,kjYtID }, "String");
					kjLxMx = sqlQuery.queryForObject("SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID = 'TZ_COM_BMB_LX' AND TZ_EFF_STATUS = 'A' AND TZ_ZHZ_ID =?", new Object[] { kjLx }, "String");
				}

				Map<String, Object> mapRetJson = new HashMap<String, Object>();
				mapRetJson.put("kjID", kjID);
				mapRetJson.put("isQy", isQy);
				mapRetJson.put("isQyMx", isQyMx);
				mapRetJson.put("tz_order", tz_order);
				mapRetJson.put("kjYtID", kjYtID);
				mapRetJson.put("kjYtIDMx", kjYtIDMx);
				mapRetJson.put("kjLx", kjLx);
				mapRetJson.put("kjLxMx", kjLxMx);
				
				listRetJson.add(mapRetJson);
			}
		
			mapRet.replace("total", resultlist.size());
			mapRet.replace("root", listRetJson);
		}
		
		if(StringUtils.equals(queryType, "JYGZ")){
			//校验规则
			String sql = "SELECT TZ_JYGZ_ID,TZ_QY_BZ,TZ_ORDER FROM PS_TZ_COM_JYGZPZ_T WHERE TZ_COM_ID =? ORDER BY TZ_ORDER";
			if(StringUtils.isBlank(kjID)){
				
			}else{
				List<?> resultlist = sqlQuery.queryForList(sql, new Object[] { kjID });
	
				ArrayList<Map<String, Object>> listRetJson = new ArrayList<Map<String, Object>>();
	
				for (Object obj : resultlist) {
					Map<String, Object> result = (Map<String, Object>) obj;
					String isQy = result.get("TZ_QY_BZ") == null ? "" : String.valueOf(result.get("TZ_QY_BZ"));
					String isQyMx = sqlQuery.queryForObject("SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID = 'TZ_QY_BZ' AND TZ_EFF_STATUS = 'A' AND TZ_ZHZ_ID =?", new Object[] { isQy }, "String");
					String tz_order = result.get("TZ_ORDER") == null ? "" : String.valueOf(result.get("TZ_ORDER"));
					String kjJygzID = result.get("TZ_JYGZ_ID") == null ? "" : String.valueOf(result.get("TZ_JYGZ_ID"));
					String kjJygzMx = sqlQuery.queryForObject("SELECT TZ_JYGZ_MC FROM PS_TZ_JYGZ_DY_T WHERE TZ_JYGZ_ID = ?", new Object[] { kjJygzID }, "String");
					
					Map<String, Object> mapRetJson = new HashMap<String, Object>();
					mapRetJson.put("kjID", kjID);
					mapRetJson.put("isQy", isQy);
					mapRetJson.put("isQyMx", isQyMx);
					mapRetJson.put("tz_order", tz_order);
					mapRetJson.put("kjJygzID", kjJygzID);
					mapRetJson.put("kjJygzMx", kjJygzMx);
					listRetJson.add(mapRetJson);
				}
	
				mapRet.replace("total", resultlist.size());
				mapRet.replace("root", listRetJson);
			}
		}
		return jacksonUtil.Map2json(mapRet);
	}
}