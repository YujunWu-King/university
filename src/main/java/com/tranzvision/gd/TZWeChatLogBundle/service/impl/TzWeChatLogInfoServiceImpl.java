package com.tranzvision.gd.TZWeChatLogBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZPermissionDefnBundle.model.PsClassDefn;
import com.tranzvision.gd.TZWeChatBundle.model.PsTzWxAppseTblKey;
import com.tranzvision.gd.TZWeChatLogBundle.dao.PsTzWeChatLogMapper;
import com.tranzvision.gd.TZWeChatLogBundle.model.PsTzWeChatLogTbl;
import com.tranzvision.gd.TZWeChatLogBundle.model.PsTzWeChatLogTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;

@Service("com.tranzvision.gd.TZWeChatLogBundle.service.impl.TzWeChatLogInfoServiceImpl")
public class TzWeChatLogInfoServiceImpl extends FrameworkImpl{
	@Autowired
	private PsTzWeChatLogMapper psTzWeChatLogMapper;
	@Autowired
	private FliterForm fliterForm;
	/* 获取微信服务号定义信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			System.out.println("strParams:"+strParams);
			if (jacksonUtil.containsKey("orgId")&& jacksonUtil.containsKey("wxAppId")&& jacksonUtil.containsKey("XH")) {
				// 类方法ID;
				String strJgID = jacksonUtil.getString("orgId");
				String strAppID=jacksonUtil.getString("wxAppId");
				String strXH=jacksonUtil.getString("XH");
				PsTzWeChatLogTblKey psTzWeChatLogTblKey = new PsTzWeChatLogTblKey();
				psTzWeChatLogTblKey.setTzJgId(strJgID);
				psTzWeChatLogTblKey.setTzAppId(strAppID);
				psTzWeChatLogTblKey.setTzXH(strXH);
				
				
				PsTzWeChatLogTbl psTzWeChatLogTbl =psTzWeChatLogMapper.selectByPrimaryKey(psTzWeChatLogTblKey);
				if (psTzWeChatLogTbl != null) {
					Map<String, Object> jsonMap = new HashMap<>();
					jsonMap.put("jgId",strJgID);
					jsonMap.put("appId", strAppID);
					jsonMap.put("XH", strXH);
					jsonMap.put("sendTpye", psTzWeChatLogTbl.getTzSendType());
					jsonMap.put("sendPsn", psTzWeChatLogTbl.getTzSendPSN());
					jsonMap.put("sendDTime", psTzWeChatLogTbl.getTzSendDTime());
					jsonMap.put("sendState", psTzWeChatLogTbl.getTzSendState());
					jsonMap.put("s_DT", psTzWeChatLogTbl.getTzSendSDTime());
					jsonMap.put("s_total", psTzWeChatLogTbl.getTzSTotal());
					jsonMap.put("s_fiter", psTzWeChatLogTbl.getTzSFilter());
					jsonMap.put("s_suceuss", psTzWeChatLogTbl.getTzSSucess());
					jsonMap.put("s_fail", psTzWeChatLogTbl.getTzSFail());
//					jsonMap.put("context", strXH);
					returnJsonMap.replace("formData", jsonMap);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该微信服务号日志数据不存在";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "请选择微信服务号";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}
	
	
//	/*获取授权组件列表*/
//	@Override
//	@SuppressWarnings("unchecked")
//	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
//		// 返回值;
//		Map<String, Object> mapRet = new HashMap<String, Object>();
//		mapRet.put("total", 0);
//		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
//		mapRet.put("root", listData);
//		JacksonUtil jacksonUtil = new JacksonUtil();
//		try {
//			System.out.println("result:"+comParams);
//			// 排序字段如果没有不要赋值
//			String[][] orderByArr = new String[][] { };
//
//			// json数据要的结果字段;
//			String[] resultFldArray = { "TZ_XH","TZ_SEND_TYPE", "TZ_SEND_DTIME", "TZ_SEND_PSN","TZ_SEND_STATE"};
//
//			// 可配置搜索通用函数;
//			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);
//			if (obj != null && obj.length > 0) {
//				
//				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
//				for (int i = 0; i < list.size(); i++) {
//					String[] rowList = list.get(i);
//					Map<String, Object> mapList = new HashMap<String, Object>();
//					mapList.put("XH", rowList[0]);
//					mapList.put("sendType", rowList[1]);
//					mapList.put("sendDTime", rowList[2]);
//					mapList.put("sendPSN", rowList[3]);
//					mapList.put("sendState", rowList[4]);
//
//					listData.add(mapList);
//				}
//
//				mapRet.replace("total", obj[0]);
//				mapRet.replace("root", listData);
//
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return jacksonUtil.Map2json(mapRet);
//	}
	
}
