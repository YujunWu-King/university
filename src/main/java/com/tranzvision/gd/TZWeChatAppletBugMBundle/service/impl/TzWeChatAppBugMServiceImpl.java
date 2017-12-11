package com.tranzvision.gd.TZWeChatAppletBugMBundle.service.impl;

import java.text.SimpleDateFormat;
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
import com.tranzvision.gd.TZWeChatAppletBugMBundle.dao.PsTzBugmgDfnTMapper;
import com.tranzvision.gd.TZWeChatAppletBugMBundle.model.PsTzBugmgDfnTKey;
import com.tranzvision.gd.TZWeChatAppletBugMBundle.model.PsTzBugmgDfnTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 微信小程序任务系统管理
 * 
 * @author xzx 
 * @since 原ps类：TZ_GD_BUG_MG_PKG:TZ_BUG_INFO_CLS
 */
@Service("com.tranzvision.gd.TZWeChatAppletBugMBundle.service.impl.TzWeChatAppBugMServiceImpl")
public class TzWeChatAppBugMServiceImpl extends FrameworkImpl {
	@Autowired
	private PsTzBugmgDfnTMapper psTzBugmgDfnTMapper;

	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;

	/* 加载任务列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("list", "[]");
		// 返回值;
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		// String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		// String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String orgid = "Tranzvision";
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			jacksonUtil.json2Map(comParams);
			String openId = jacksonUtil.getString("openId");
			String empId = jacksonUtil.getString("empId");
			String type = jacksonUtil.getString("type");
			type = type.toUpperCase();
			// 通过OPRID得到登录账号ID
			// String sqlOprid = "select TZ_DLZH_ID from PS_TZ_AQ_YHXX_TBL WHERE
			// TZ_JG_ID=? AND OPRID=?";
			// String dlzhId = jdbcTemplate.queryForObject(sqlOprid, new
			// Object[] { orgid, oprid, }, "String");

			String sqlnum = "select COUNT(1) from PS_TZ_BUGMG_APP_T WHERE TZ_JG_ID=? AND OPENID=? AND TZ_DLZH_ID=?";
			int count = jdbcTemplate.queryForObject(sqlnum, new Object[] { orgid, openId, empId }, "Integer");
			if (count < 1) {
				errMsg[0] = "1";
				errMsg[1] = "此微信服号没有绑定员工账号！";
			} else {

				String sqlSelect = "";
				if (type != null && type.equals("NO")) {
					// NO未完成任务
					sqlSelect = "select TZ_BUG_ID,TZ_BUG_PERCENT,TZ_BUG_PRIORITY,TZ_BUG_STA,TZ_BUG_JDGX_RQ from PS_TZ_BUGMG_DFN_T where TZ_JG_ID = ?  and TZ_DLZH_ID2=? AND AND TZ_BUG_STA IN('1','4')";
				} else {
					// ALL所有任务
					sqlSelect = "select TZ_BUG_ID,TZ_BUG_PERCENT,TZ_BUG_PRIORITY,TZ_BUG_STA,TZ_BUG_JDGX_RQ from PS_TZ_BUGMG_DFN_T where TZ_JG_ID = ?  and TZ_DLZH_ID2=? ";
				}
				List<?> list = jdbcTemplate.queryForList(sqlSelect, new Object[] { orgid, empId });

				Map<String, Object> mapData = jdbcTemplate.queryForMap(sqlSelect, new Object[] { orgid, empId });
				if (mapData != null) {
					String id = mapData.get("TZ_BUG_ID") == null ? "" : mapData.get("TZ_BUG_ID").toString();
					String per = mapData.get("TZ_BUG_PERCENT") == null ? "" : mapData.get("TZ_BUG_PERCENT").toString();
					String level = mapData.get("TZ_BUG_PRIORITY") == null ? ""
							: mapData.get("TZ_BUG_PRIORITY").toString();
					String state = mapData.get("TZ_BUG_STA") == null ? "" : mapData.get("TZ_BUG_STA").toString();
					String uDtime = mapData.get("TZ_BUG_JDGX_RQ") == null ? ""
							: mapData.get("TZ_BUG_JDGX_RQ").toString();

					Map<String, Object> mapRetJson = new HashMap<String, Object>();
					mapRetJson.put("id", id);
					mapRetJson.put("per", per);
					mapRetJson.put("level", level);
					mapRetJson.put("state", state);
					mapRetJson.put("uDtime", uDtime);

					listData.add(mapRetJson);
				}
				int size = list.size();
				mapRet.replace("total", size);
				mapRet.replace("list", listData);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRet.put("success", false);
			errMsg[0] = "1";
			errMsg[1] = "数据异常，请重试！";
		}

		return jacksonUtil.Map2json(mapRet);
	}

	/* 检索任务详情 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("info", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String bugId = jacksonUtil.getString("bugId");
			// String orgId = jacksonUtil.getString("jgId");
			String orgId = "Tranzvision";
			PsTzBugmgDfnTKey psTzBugmgDfnTKey = new PsTzBugmgDfnTKey();
			psTzBugmgDfnTKey.setTzJgId(orgId);
			psTzBugmgDfnTKey.setTzBugId(bugId);

			PsTzBugmgDfnTWithBLOBs psTzBugmgDfnTWithBLOBs = psTzBugmgDfnTMapper.selectByPrimaryKey(psTzBugmgDfnTKey);
			if (psTzBugmgDfnTWithBLOBs != null) {
				Map<String, Object> map = new HashMap<>();
				// map.put("jgId", psTzBugmgDfnTWithBLOBs.getTzJgId());
				map.put("id", psTzBugmgDfnTWithBLOBs.getTzBugId());// bug编号
				map.put("module", psTzBugmgDfnTWithBLOBs.getTzBugGnmk());// 功能模块
				map.put("explanation", psTzBugmgDfnTWithBLOBs.getTzBugSm());// 说明
				map.put("type", psTzBugmgDfnTWithBLOBs.getTzBugType());// 类型
				map.put("level", psTzBugmgDfnTWithBLOBs.getTzBugPriority());// 优先级
				map.put("inUser", psTzBugmgDfnTWithBLOBs.getTzDlzhId1());// 录入人姓名
				map.put("wDate", psTzBugmgDfnTWithBLOBs.getTzExpDate());// 期望解决日期
				map.put("rPsn", psTzBugmgDfnTWithBLOBs.getTzDlzhId2());// 责任人
				map.put("state", psTzBugmgDfnTWithBLOBs.getTzBugSta());// 处理状态
				map.put("uDTime", psTzBugmgDfnTWithBLOBs.getTzBugJdgxRq());// 进度更新日期
				map.put("eDate", psTzBugmgDfnTWithBLOBs.getTzEstfinDate());// 预计结束日期
				map.put("per", psTzBugmgDfnTWithBLOBs.getTzBugPercent());// 进度百分比
				map.put("remark", psTzBugmgDfnTWithBLOBs.getTzBugBz());// 备注
				returnJsonMap.replace("info", map);
			} else {
				errMsg[0] = "1";
				errMsg[1] = "该任务编号数据不存在";
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}

	/* 更新任务 /完成任务 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		// String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String orgid = "Tranzvision";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			int length = actData.length;
			for (num = 0; num < length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 信息内容;
				String bugId = jacksonUtil.getString("bugId");
				// 预计结束日期、进度百分比、备注
				String per = jacksonUtil.getString("per");
				String remrak = jacksonUtil.getString("remrak");
				String sDate = jacksonUtil.getString("eDate");
				Date eDate = new SimpleDateFormat("yyyy/MM/dd").parse(sDate);

				// 进度更新日期
				Date dt = new Date();
				SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyy-MM-dd");
				String strDateTime = datetimeFormate.format(dt);
				Date jdgxrq = new SimpleDateFormat("yyyy/MM/dd").parse(strDateTime);

				String sql = "select COUNT(1) from PS_TZ_BUGMG_DFN_T WHERE TZ_JG_ID=? AND TZ_BUG_ID=?";
				int count = jdbcTemplate.queryForObject(sql, new Object[] { orgid, bugId }, "Integer");
				if (count > 0) {
					PsTzBugmgDfnTWithBLOBs psTzBugmgDfnTWithBLOBs = new PsTzBugmgDfnTWithBLOBs();
					psTzBugmgDfnTWithBLOBs.setTzJgId(orgid);
					psTzBugmgDfnTWithBLOBs.setTzBugId(bugId);
					if (per == null || "".equals(per)) {
						// 完成任务
						psTzBugmgDfnTWithBLOBs.setTzBugPercent("100");// 进度百分比
						psTzBugmgDfnTWithBLOBs.setTzBugSta("2");// 任务状态，已完成
					} else {
						// 更新任务 预计结束日期、进度百分比、备注
						psTzBugmgDfnTWithBLOBs.setTzEstfinDate(eDate);// 预计结束日期
						psTzBugmgDfnTWithBLOBs.setTzBugPercent(per);// 进度百分比
						psTzBugmgDfnTWithBLOBs.setTzBugBz(remrak);// 备注
					}

					psTzBugmgDfnTWithBLOBs.setTzBugJdgxRq(jdgxrq);// 进度更新日期
					psTzBugmgDfnTWithBLOBs.setTzBugAddDttm(new Date());
					psTzBugmgDfnTWithBLOBs.setRowLastmantDttm(new Date());
					psTzBugmgDfnTWithBLOBs.setRowLastmantOprid(oprid);
					psTzBugmgDfnTMapper.updateByPrimaryKeySelective(psTzBugmgDfnTWithBLOBs);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "任务编号：" + bugId + ",不存在";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/**
	 * 撤销任务
	 * 
	 * @param actData
	 * @param errMsg
	 * @return
	 */
	public String tzRevoke(String[] actData, String[] errMsg) {
		String strRet = "";
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		// String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String orgid = "Tranzvision";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			int length = actData.length;
			for (num = 0; num < length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 信息内容;
				String bugId = jacksonUtil.getString("bugId");

				// 进度更新日期
				Date dt = new Date();
				SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyy-MM-dd");
				String strDateTime = datetimeFormate.format(dt);
				Date jdgxrq = new SimpleDateFormat("yyyy/MM/dd").parse(strDateTime);

				String sql = "select COUNT(1) from PS_TZ_BUGMG_DFN_T WHERE TZ_JG_ID=? AND TZ_BUG_ID=?";
				int count = jdbcTemplate.queryForObject(sql, new Object[] { orgid, bugId }, "Integer");
				if (count > 0) {
					PsTzBugmgDfnTWithBLOBs psTzBugmgDfnTWithBLOBs = new PsTzBugmgDfnTWithBLOBs();
					psTzBugmgDfnTWithBLOBs.setTzJgId(orgid);
					psTzBugmgDfnTWithBLOBs.setTzBugId(bugId);
					// 撤销任务
					psTzBugmgDfnTWithBLOBs.setTzBugPercent("0");// 进度百分比
					psTzBugmgDfnTWithBLOBs.setTzBugSta("1");// 任务状态，已分配

					psTzBugmgDfnTWithBLOBs.setTzBugJdgxRq(jdgxrq);// 进度更新日期
					psTzBugmgDfnTWithBLOBs.setTzBugAddDttm(new Date());
					psTzBugmgDfnTWithBLOBs.setRowLastmantDttm(new Date());
					psTzBugmgDfnTWithBLOBs.setRowLastmantOprid(oprid);
					psTzBugmgDfnTMapper.updateByPrimaryKeySelective(psTzBugmgDfnTWithBLOBs);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "任务编号：" + bugId + ",不存在";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;

	}
}
