package com.tranzvision.gd.TZWeChatLogBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWeChatMsgBundle.dao.PsTzWxmsgLogTMapper;
import com.tranzvision.gd.TZWeChatMsgBundle.model.PsTzWxmsgLogT;
import com.tranzvision.gd.TZWeChatMsgBundle.model.PsTzWxmsgLogTKey;
import com.tranzvision.gd.util.base.JacksonUtil;

@Service("com.tranzvision.gd.TZWeChatLogBundle.service.impl.TzWeChatLogInfoServiceImpl")
public class TzWeChatLogInfoServiceImpl extends FrameworkImpl{
	@Autowired
	private PsTzWxmsgLogTMapper psTzWxmsgLogTMapper;
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
				PsTzWxmsgLogTKey psTzWxmsgLogTKey = new PsTzWxmsgLogTKey();
				psTzWxmsgLogTKey.setTzJgId(strJgID);
				psTzWxmsgLogTKey.setTzWxAppid(strAppID);
				psTzWxmsgLogTKey.setTzXh(strXH);
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				PsTzWxmsgLogT psTzWxmsgLogT =psTzWxmsgLogTMapper.selectByPrimaryKey(psTzWxmsgLogTKey);
				if (psTzWxmsgLogT != null) {
					Map<String, Object> jsonMap = new HashMap<>();
					jsonMap.put("jgId",strJgID);
					jsonMap.put("appId", strAppID);
					jsonMap.put("XH", strXH);
					jsonMap.put("sendTpye", psTzWxmsgLogT.getTzSendType());
					jsonMap.put("sendPsn", psTzWxmsgLogT.getTzSendPsn());
					jsonMap.put("sendDTime", format.format(psTzWxmsgLogT.getTzSendDtime()));
					jsonMap.put("sendState", psTzWxmsgLogT.getTzSendState());
					jsonMap.put("s_DT", format.format(psTzWxmsgLogT.getTzSendsDtime()));					
					jsonMap.put("s_total", psTzWxmsgLogT.getTzSTotal());
					jsonMap.put("s_fiter", psTzWxmsgLogT.getTzSFilter());
					jsonMap.put("s_suceuss", psTzWxmsgLogT.getTzSSucess());
					jsonMap.put("s_fail", psTzWxmsgLogT.getTzSFail());
					jsonMap.put("content", psTzWxmsgLogT.getTzContent());
					jsonMap.put("MediaId", psTzWxmsgLogT.getTzMediaId());
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
	
	
	/*获取授权组件列表*/
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			System.out.println("result:"+comParams);
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { };

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_XH_ID","TZ_XH_NAME","TZ_CONTENT","TZ_SEND_STATE"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);
			if (obj != null && obj.length > 0) {
				
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("openId", rowList[0]);
					mapList.put("nickName", rowList[1]);
					mapList.put("content", rowList[2]);
					mapList.put("sendState", rowList[3]);

					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);
	}
	
}
