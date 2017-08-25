package com.tranzvision.gd.TZWeChatBundle.service.impl;

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
import com.tranzvision.gd.TZWeChatBundle.dao.PsTzWxAppseTblMapper;
import com.tranzvision.gd.TZWeChatBundle.model.PsTzWxAppseTbl;
import com.tranzvision.gd.TZWeChatBundle.model.PsTzWxAppseTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 微信服务号管理
 * @author xzx
 *
 */
@Service("com.tranzvision.gd.TZWeChatBundle.service.impl.TzWeChatServiceImpl")
public class TzWeChatServiceImpl extends FrameworkImpl {
	@Autowired
	private PsTzWxAppseTblMapper psTzWxAppseTblMapper;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	
	/* 加载微信服务号列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart,
			String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] { { "ROW_ADDED_DTTM", "DESC" } };

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_JG_ID", "TZ_WX_NAME", "TZ_WX_APPID", "TZ_WX_SECRET","TZ_FROM_PVALUE", "ROW_ADDED_OPRID", "ROW_ADDED_DTTM" };

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams,
				numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("orgId", rowList[0]);
				mapList.put("wxName", rowList[1]);
				mapList.put("wxId", rowList[2]);
				mapList.put("wxSecret", rowList[3]);
				mapList.put("wxParam", rowList[4]);
				mapList.put("wxAddOprid", rowList[5]);
				mapList.put("wxAddTime", rowList[6]);
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}
	
	
	/* 获取微信服务号定义信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("weChatId")) {
				String orgId = jacksonUtil.getString("orgId");
				String weChatId = jacksonUtil.getString("weChatId");
				PsTzWxAppseTblKey psTzWxAppseTblKey = new PsTzWxAppseTblKey();
				psTzWxAppseTblKey.setTzJgId(orgId);
				psTzWxAppseTblKey.setTzWxAppid(weChatId);
				
				PsTzWxAppseTbl psTzWxAppseTbl = psTzWxAppseTblMapper.selectByPrimaryKey(psTzWxAppseTblKey);
				if (psTzWxAppseTbl != null) {
					Map<String, Object> map = new HashMap<>();
					map.put("jgId", psTzWxAppseTbl.getTzJgId());
					map.put("wxName", psTzWxAppseTbl.getTzWxName());
					map.put("wxId", psTzWxAppseTbl.getTzWxAppid());
					map.put("wxSecret", psTzWxAppseTbl.getTzWxSecret());
					map.put("wxParam", psTzWxAppseTbl.getTzFromPvalue());
					returnJsonMap.replace("formData", map);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该微信服务号数据不存在";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "该微信服务号数据不存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}
	
	
	/* 新增微信服务号 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				// 微信服务号名称;
				String weChatName = (String) infoData.get("wxName");
				String weChatId = (String) infoData.get("wxId");
				String weChatSecret = (String) infoData.get("wxSecret");
				String weChatParam = (String) infoData.get("wxParam");

				String sql = "select COUNT(1) from PS_TZ_WX_APPSE_TBL WHERE TZ_JG_ID=? AND TZ_WX_APPID=?";
				int count = jdbcTemplate.queryForObject(sql, new Object[] { orgid, weChatId }, "Integer");
				if (count > 0) {
					errMsg[0] = "1";
					errMsg[1] = "微信服务号ID：" + weChatId + ",已经存在";
				} else {
					PsTzWxAppseTbl psTzWxAppseTbl = new PsTzWxAppseTbl();
					
					psTzWxAppseTbl.setTzJgId(orgid);
					psTzWxAppseTbl.setTzWxAppid(weChatId);
					psTzWxAppseTbl.setTzWxSecret(weChatSecret);
					psTzWxAppseTbl.setTzWxName(weChatName);
					psTzWxAppseTbl.setTzFromPvalue(weChatParam);
					
					psTzWxAppseTbl.setRowAddedDttm(new Date());
					psTzWxAppseTbl.setRowAddedOprid(oprid);
					psTzWxAppseTbl.setRowLastmantDttm(new Date());
					psTzWxAppseTbl.setRowLastmantOprid(oprid);
					
					psTzWxAppseTblMapper.insert(psTzWxAppseTbl);
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	/* 修改微信服务号 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");

				// 微信服务号名称;
				// String orgId = (String) infoData.get("jgId");
				String weChatName = (String) infoData.get("wxName");
				String weChatId = (String) infoData.get("wxId");
				String weChatSecret = (String) infoData.get("wxSecret");
				String weChatParam = (String) infoData.get("wxParam");

				String sql = "select COUNT(1) from PS_TZ_WX_APPSE_TBL WHERE TZ_JG_ID=? AND TZ_WX_APPID=?";
				int count = jdbcTemplate.queryForObject(sql, new Object[] { orgid, weChatId }, "Integer");
				if (count > 0) {
					PsTzWxAppseTbl psTzWxAppseTbl = new PsTzWxAppseTbl();
					psTzWxAppseTbl.setTzJgId(orgid);
					psTzWxAppseTbl.setTzWxAppid(weChatId);
					psTzWxAppseTbl.setTzWxSecret(weChatSecret);
					psTzWxAppseTbl.setTzWxName(weChatName);
					psTzWxAppseTbl.setTzFromPvalue(weChatParam);
					
					psTzWxAppseTbl.setRowLastmantDttm(new Date());
					psTzWxAppseTbl.setRowLastmantOprid(oprid);
					
					psTzWxAppseTblMapper.updateByPrimaryKeySelective(psTzWxAppseTbl);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "更新的微信服务号：" + weChatId + ",不存在";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	
	/* 删除微信服务号 */
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
					// weChatId;
					String orgId = jacksonUtil.getString("orgId");
					String weChatId = jacksonUtil.getString("wxId");
					if (weChatId != null && !"".equals(weChatId)) {
						PsTzWxAppseTblKey psTzWxAppseTblKey = new PsTzWxAppseTblKey();
						psTzWxAppseTblKey.setTzJgId(orgId);
						psTzWxAppseTblKey.setTzWxAppid(weChatId);
						
						psTzWxAppseTblMapper.deleteByPrimaryKey(psTzWxAppseTblKey);
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
