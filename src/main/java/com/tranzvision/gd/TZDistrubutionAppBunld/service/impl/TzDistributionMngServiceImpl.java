package com.tranzvision.gd.TZDistrubutionAppBunld.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZDistrubutionAppBunld.dao.PsTzTappUserTblMapper;
import com.tranzvision.gd.TZDistrubutionAppBunld.dao.PsTzTranzAppTblMapper;
import com.tranzvision.gd.TZDistrubutionAppBunld.model.PsTzTappUserTblKey;
import com.tranzvision.gd.TZDistrubutionAppBunld.model.PsTzTranzAppTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZDistrubutionAppBunld.service.impl.TzDistributionMngServiceImpl")
public class TzDistributionMngServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private FliterForm fliterForm;	
	@Autowired
	private PsTzTranzAppTblMapper psTzTranzAppTblMapper;
	@Autowired
	private PsTzTappUserTblMapper psTzTappUserTblMapper;
	
	/* 加载HardCode列表 */
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
		String[][] orderByArr = new String[][] {};

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_JG_ID","TZ_TRANZ_APPID", "TZ_TRANZ_APPNAME", "TZ_TRANZ_APPSECRET","TZ_DESCR" };

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams,
				numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("jgId", rowList[0]);
				mapList.put("appId", rowList[1]);
				mapList.put("appName", rowList[2]);
				mapList.put("appSecret", rowList[3]);
				mapList.put("appDesc", rowList[4]);
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}
	
	/* 删除应用分配信息 */
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

			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// appid;
				String jgId = jacksonUtil.getString("jgId");
				String appId = jacksonUtil.getString("appId");
				PsTzTranzAppTblKey psTzTranzAppTblKey = new PsTzTranzAppTblKey();
				psTzTranzAppTblKey.setTzJgId(jgId);
				psTzTranzAppTblKey.setTzTranzAppid(appId);
				int i = psTzTranzAppTblMapper.deleteByPrimaryKey(psTzTranzAppTblKey);
				if(i > 0){
					String sql = "select TZ_OTH_USER from PS_TZ_TAPP_USER_TBL WHERE TZ_JG_ID=? AND TZ_TRANZ_APPID=?";
					List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,new Object[]{jgId,appId});
					if(list != null && list.size() > 0){
						for(int j = 0; j < list.size(); j++){
							String otherUser = String.valueOf(list.get(j).get("TZ_OTH_USER"));
							PsTzTappUserTblKey psTzTppUserTblKey = new PsTzTappUserTblKey();
							psTzTppUserTblKey.setTzJgId(jgId);
							psTzTppUserTblKey.setTzTranzAppid(appId);
							psTzTppUserTblKey.setTzOthUser(otherUser);
							psTzTappUserTblMapper.deleteByPrimaryKey(psTzTppUserTblKey);
						}
					}
				}
				
					
				
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
}
