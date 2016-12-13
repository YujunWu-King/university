package com.tranzvision.gd.TZApplicationSurveyBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcDyTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcDyTWithBLOBs;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * @author caoy
 * @version 创建时间：2016年7月28日 下午3:41:18 类说明 设置问卷模板
 */
@Service("com.tranzvision.gd.TZApplicationSurveyBundle.service.impl.SurveyMBSZClsServiceImpl")
public class SurveyMBSZClsServiceImpl extends FrameworkImpl {

	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private PsTzDcDyTMapper psTzDcDyTMapper;

	/* 获取报名表模板信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		String strComContent = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String tplId = jacksonUtil.getString("tplId");
		Map<String, Object> mapRet = null;
		if (!StringUtils.isBlank(tplId)) {
			String TZ_APP_TPL_ID = "";
			String TZ_APP_TPL_MC = "";
			String TZ_APP_TPL_LX = "";
			String TZ_APP_TPL_LAN = "";
			String TZ_DC_JTNR = "";
			String TZ_DC_JWNR = "";
			String TZ_EFFEXP_ZT = "";
			String str_jg_id = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String SQL = "select TZ_APP_TPL_ID,TZ_APP_TPL_MC,TZ_APP_TPL_LX,TZ_APP_TPL_LAN,TZ_DC_JTNR,TZ_DC_JWNR,TZ_EFFEXP_ZT from PS_TZ_DC_DY_T  where TZ_APP_TPL_ID=? and TZ_JG_ID=?";
			Map<String, Object> map = jdbcTemplate.queryForMap(SQL, new Object[] { tplId, str_jg_id });

			if (map != null) {
				TZ_APP_TPL_ID = map.get("TZ_APP_TPL_ID") == null ? "" : String.valueOf(map.get("TZ_APP_TPL_ID"));
				TZ_APP_TPL_MC = map.get("TZ_APP_TPL_MC") == null ? "" : String.valueOf(map.get("TZ_APP_TPL_MC"));
				TZ_APP_TPL_LX = map.get("TZ_APP_TPL_LX") == null ? "" : String.valueOf(map.get("TZ_APP_TPL_LX"));
				TZ_APP_TPL_LAN = map.get("TZ_APP_TPL_LAN") == null ? "ZHS" : String.valueOf(map.get("TZ_APP_TPL_LAN"));
				TZ_DC_JTNR = map.get("TZ_DC_JTNR") == null ? "" : String.valueOf(map.get("TZ_DC_JTNR"));
				TZ_DC_JWNR = map.get("TZ_DC_JWNR") == null ? "" : String.valueOf(map.get("TZ_DC_JWNR"));
				TZ_EFFEXP_ZT = map.get("TZ_EFFEXP_ZT") == null ? "" : String.valueOf(map.get("TZ_EFFEXP_ZT"));
			}
			mapRet = new HashMap<String, Object>();
			mapRet.put("TZ_APP_TPL_ID", TZ_APP_TPL_ID);
			mapRet.put("TZ_APP_TPL_MC", TZ_APP_TPL_MC);
			mapRet.put("TZ_APP_TPL_LX", TZ_APP_TPL_LX);
			mapRet.put("TZ_APP_TPL_LAN", TZ_APP_TPL_LAN);
			mapRet.put("TZ_DC_JTNR", TZ_DC_JTNR);
			mapRet.put("TZ_DC_JWNR", TZ_DC_JWNR);
			mapRet.put("TZ_EFFEXP_ZT", TZ_EFFEXP_ZT);
			mapRet.put("TZ_APPTPL_JSON_STR", "");
			strComContent = jacksonUtil.Map2json(mapRet);
		} else {
			errMsg[0] = "1";
			errMsg[1] = "参数不正确！";
		}

		mapRet = new HashMap<String, Object>();
		mapRet.put("formData",strComContent);
		strComContent = jacksonUtil.Map2json(mapRet);
		return strComContent;
	}

	/**
	 * 设置 问卷模板 模板设置
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		System.out.println("==模板设置tzUpdate()方法执行==");
		for(int index=0;index<actData.length;index++)
			System.out.print("==actData:=="+actData[index]+"_");
		System.out.println();
		String strRet = "{}";

		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String userID = tzLoginServiceImpl.getLoginedManagerOprid(request);
		if (StringUtils.isBlank(orgId)) {
			errMsg[0] = "1";
			errMsg[1] = "您当前没有机构，不能保存调查表模板！";
			return strRet;
		}

		if (userID.equals("TZ_GUEST")) {
			errMsg[0] = "1";
			errMsg[1] = "请先登录再操作";
			return strRet;
		}

		try {
			int dataLength = actData.length;

			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				
				strRet = this.tzUpdateFattrInfo(strForm, orgId, userID, errMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/**
	 * 
	 * @param strForm
	 * @return 设置 问卷模板 模板设置 修改问卷模板的时，语言不可更改
	 */
	private String tzUpdateFattrInfo(String strForm, String orgId, String userID, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		// 模板ID;
		String TZ_APP_TPL_ID = null;
		// 模板名称
		String TZ_APP_TPL_MC = null;
		// 模板类型
		String TZ_APP_TPL_LX = null;
		// 卷头内容
		String TZ_DC_JTNR = null;
		// 卷尾内容
		String TZ_DC_JWNR = null;
		// 模板语言
		String TZ_APP_TPL_LAN = null;
		// 状态
		String TZ_EFFEXP_ZT = null;
		// JSON报文
		String items = null;
		// JSON报文
		String TZ_APPTPL_JSON_STR = null;

		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strForm);
			TZ_APP_TPL_ID = jacksonUtil.getString("TZ_APP_TPL_ID");
			TZ_APP_TPL_MC = jacksonUtil.getString("TZ_APP_TPL_MC");
			TZ_APP_TPL_LX = jacksonUtil.getString("TZ_APP_TPL_LX");
			TZ_DC_JTNR = jacksonUtil.getString("TZ_DC_JTNR");
			TZ_DC_JWNR = jacksonUtil.getString("TZ_DC_JWNR");
			TZ_APP_TPL_LAN = jacksonUtil.getString("TZ_APP_TPL_LAN");
			if(TZ_APP_TPL_LAN==null||TZ_APP_TPL_LAN.equals("")){
				TZ_APP_TPL_LAN="ZHS";
			}
			TZ_EFFEXP_ZT = jacksonUtil.getString("TZ_EFFEXP_ZT");

			String strNameRepeated = "";
			// 如果ID有值，那么判重的时候，是与除去该ID之外的其他值进行判重;
			// 如果保存过一次就不对它进行判重;
			String isZcSQL = "SELECT 'Y' FROM PS_TZ_DC_DY_T WHERE TZ_JG_ID=? AND TZ_APP_TPL_MC=? and  TZ_APP_TPL_ID!=?";
			strNameRepeated = jdbcTemplate.queryForObject(isZcSQL, new Object[] { orgId, TZ_APP_TPL_MC, TZ_APP_TPL_ID },
					"String");

			if ("Y".equals(strNameRepeated)) {
				errMsg[0] = "1";
				errMsg[1] = "问卷模板名称重复！";
				return strRet;
			}
			// 获取新增模板的ID
			String strNewTplid = "";
			if (TZ_APP_TPL_ID.equals("NEXT") || StringUtils.isBlank(TZ_APP_TPL_ID)) {
				strNewTplid = "" + getSeqNum.getSeqNum("TZ_DC_DY_T", "TZ_APP_TPL_ID");
			}

			// 根据模板ID，查找JSON报文;
			isZcSQL = "select TZ_APPTPL_JSON_STR from PS_TZ_DC_DY_T where TZ_APP_TPL_ID=? ";
			TZ_APPTPL_JSON_STR = jdbcTemplate.queryForObject(isZcSQL, new Object[] { TZ_APP_TPL_ID }, "String");

			if (!StringUtils.isBlank(TZ_APPTPL_JSON_STR)) {
				jacksonUtil = new JacksonUtil();
				jacksonUtil.json2Map(TZ_APPTPL_JSON_STR);
				items = jacksonUtil.getString("items");
				if(items.equals("{}"))
					items = null;
			} else {
				items = null;
			}
			System.out.println("==items:=="+items);
			String tplJson = "";
			// 新建模板
			Map<String, Object> mapRet = new HashMap<String, Object>();
			if (TZ_APP_TPL_ID.equals("NEXT") || StringUtils.isBlank(TZ_APP_TPL_ID)) {
//				mapRet.put("tplId", strNewTplid);
//				mapRet.put("tplName", TZ_APP_TPL_MC);
//				mapRet.put("tplLx", TZ_APP_TPL_LX);
//				mapRet.put("header", TZ_DC_JTNR);
//				mapRet.put("footer", TZ_DC_JWNR);
//				mapRet.put("state", TZ_EFFEXP_ZT);
//				mapRet.put("depId", orgId);
//				mapRet.put("depName", orgId);
				if(items==null)
					mapRet.put("items", new HashMap<String,Object>());
				else
					mapRet.put("items", items);
//				mapRet.put("tplType", TZ_APP_TPL_LX);
				tplJson = jacksonUtil.Map2json(mapRet);
			} else {
//				mapRet.put("tplId", TZ_APP_TPL_ID);
//				mapRet.put("tplName", TZ_APP_TPL_MC);
//				mapRet.put("tplLx", TZ_APP_TPL_LX);
//				mapRet.put("header", TZ_DC_JTNR);
//				mapRet.put("footer", TZ_DC_JWNR);
//				mapRet.put("state", TZ_EFFEXP_ZT);
//				mapRet.put("depId", orgId);
//				mapRet.put("depName", orgId);
				if(items==null)
					mapRet.put("items", new HashMap<String,Object>());
				else
					mapRet.put("items", items);
//				mapRet.put("tplType", TZ_APP_TPL_LX);
				tplJson = jacksonUtil.Map2json(mapRet);
			}

			// 增加或修改数据库
			String isTPL = "";
			// 如果ID有值，那么判重的时候，是与除去该ID之外的其他值进行判重;
			// 如果保存过一次就不对它进行判重;
			isZcSQL = "SELECT 'Y' FROM PS_TZ_DC_DY_T WHERE TZ_JG_ID=?  and  TZ_APP_TPL_ID=?";
			isTPL = jdbcTemplate.queryForObject(isZcSQL, new Object[] { orgId, TZ_APP_TPL_ID }, "String");

			// 存在就修改
			if ("Y".equals(isTPL)) {
				PsTzDcDyTWithBLOBs psTzDcDyTWithBLOBs = new PsTzDcDyTWithBLOBs();
				psTzDcDyTWithBLOBs.setTzAppTplId(TZ_APP_TPL_ID);
				psTzDcDyTWithBLOBs.setTzJgId(orgId);
				psTzDcDyTWithBLOBs.setTzAppTplMc(TZ_APP_TPL_MC);
				psTzDcDyTWithBLOBs.setTzAppTplLx(TZ_APP_TPL_LX);
				psTzDcDyTWithBLOBs.setTzDcJtnr(TZ_DC_JTNR);
				psTzDcDyTWithBLOBs.setTzDcJwnr(TZ_DC_JWNR);
				psTzDcDyTWithBLOBs.setTzEffexpZt(TZ_EFFEXP_ZT);
				psTzDcDyTWithBLOBs.setTzApptplJsonStr(tplJson);
				psTzDcDyTWithBLOBs.setRowLastmantDttm(new java.util.Date());
				psTzDcDyTWithBLOBs.setRowLastmantOprid(userID);
				int i = psTzDcDyTMapper.updateByPrimaryKeySelective(psTzDcDyTWithBLOBs);
				if (i > 0) {
					mapRet = new HashMap<String, Object>();
					mapRet.put("id", TZ_APP_TPL_ID);
					strRet = jacksonUtil.Map2json(mapRet);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "更新问卷模板失败！";
					return strRet;
				}
			} else { // 不存在就新增加
				PsTzDcDyTWithBLOBs psTzDcDyTWithBLOBs = new PsTzDcDyTWithBLOBs();
				psTzDcDyTWithBLOBs.setTzAppTplId(strNewTplid);
				psTzDcDyTWithBLOBs.setTzJgId(orgId);
				psTzDcDyTWithBLOBs.setTzAppTplMc(TZ_APP_TPL_MC);
				psTzDcDyTWithBLOBs.setTzAppTplLx(TZ_APP_TPL_LX);
				psTzDcDyTWithBLOBs.setTzDcJtnr(TZ_DC_JTNR);
				psTzDcDyTWithBLOBs.setTzAppTplLan(TZ_APP_TPL_LAN);
				psTzDcDyTWithBLOBs.setTzDcJwnr(TZ_DC_JWNR);
				psTzDcDyTWithBLOBs.setTzEffexpZt(TZ_EFFEXP_ZT);
				psTzDcDyTWithBLOBs.setTzApptplJsonStr(tplJson);
				psTzDcDyTWithBLOBs.setRowLastmantDttm(new java.util.Date());
				psTzDcDyTWithBLOBs.setRowLastmantOprid(userID);
				psTzDcDyTWithBLOBs.setRowAddedDttm(new java.util.Date());
				psTzDcDyTWithBLOBs.setRowAddedOprid(userID);
				int i = psTzDcDyTMapper.insertSelective(psTzDcDyTWithBLOBs);
				if (i > 0) {
					mapRet = new HashMap<String, Object>();
					mapRet.put("id", strNewTplid);
					strRet = jacksonUtil.Map2json(mapRet);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "更新问卷模板失败！";
					return strRet;
				}
			}
			errMsg[0] = "0";
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

}
