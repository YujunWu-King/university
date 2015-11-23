package com.tranzvision.gd.TZSiteTemplateBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZSiteTemplateBundle.dao.PsTzSitemSkinTMapper;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemSkinT;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemSkinTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;

/**
 * 站点皮肤设置；原：TZ_GD_DZMB_PKG:TZ_PFSZ_COM_SLS
 * 
 * @author tang
 * 
 */
@Service("com.tranzvision.gd.TZSiteTemplateBundle.service.impl.TemplateModelSkinServiceImpl")
public class TemplateModelSkinServiceImpl extends FrameworkImpl {
	@Autowired
	private JacksonUtil jacksonUtil;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzSitemSkinTMapper psTzSitemSkinTMapper;
	
	/*添加皮肤设置*/
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("skinId", "");
		
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 类型标志;
			    String strFlag = jacksonUtil.getString("typeFlag");
			    String siteId = "";
			    if("SKININFO".equals(strFlag)){
			    	Map<String, Object> skinMap = jacksonUtil.getMap("data");
			    	siteId = (String) skinMap.get("siteId");
			    	String skinStatus = (String) skinMap.get("skinStatus");
			    	String skinName = (String) skinMap.get("skinName");
			    	String skinCode = (String) skinMap.get("skinCode");
			    	String skinId = String.valueOf(getSeqNum.getSeqNum("TZ_SITEM_SKIN_T", "TZ_SKIN_ID"));
			    	PsTzSitemSkinT psTzSitemSkinT = new PsTzSitemSkinT();
			    	psTzSitemSkinT.setTzSitemId(siteId);
			    	psTzSitemSkinT.setTzSkinId(skinId);
			    	psTzSitemSkinT.setTzSkinState(skinStatus);
			    	psTzSitemSkinT.setTzSkinName(skinName);
			    	psTzSitemSkinT.setTzSkinCode(skinCode);
			    	int i = psTzSitemSkinTMapper.insert(psTzSitemSkinT);
			    	if(i > 0){
			    		returnJsonMap.replace("skinId", skinId);
			    	}else{
			    		errMsg[0] = "1";
						errMsg[1] = "站点皮肤信息保存失败";
			    	}
			    }
			}
		}catch(Exception e){
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	/*添加皮肤设置*/
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("skinId", "");
		
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 类型标志;
			    String strFlag = jacksonUtil.getString("typeFlag");
			    String siteId = "";
			    if("SKININFO".equals(strFlag)){
			    	Map<String, Object> skinMap = jacksonUtil.getMap("data");
			    	siteId = (String) skinMap.get("siteId");
			    	String skinId = (String)skinMap.get("skinId");
			    	String skinStatus = (String) skinMap.get("skinStatus");
			    	String skinName = (String) skinMap.get("skinName");
			    	String skinCode = (String) skinMap.get("skinCode");
			    	
			    	PsTzSitemSkinT psTzSitemSkinT = new PsTzSitemSkinT();
			    	psTzSitemSkinT.setTzSitemId(siteId);
			    	psTzSitemSkinT.setTzSkinId(skinId);
			    	psTzSitemSkinT.setTzSkinState(skinStatus);
			    	psTzSitemSkinT.setTzSkinName(skinName);
			    	psTzSitemSkinT.setTzSkinCode(skinCode);
			    	int i = psTzSitemSkinTMapper.updateByPrimaryKeySelective(psTzSitemSkinT);
			    	if(i > 0){
			    		returnJsonMap.replace("skinId", skinId);
			    	}else{
			    		errMsg[0] = "1";
						errMsg[1] = "站点皮肤信息保存失败";
			    	}
			    }
			}
		}catch(Exception e){
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	/* 查询表单信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "{}");
		
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("siteId") && jacksonUtil.containsKey("skinId")) {
				// 站点id 皮肤id;
				String siteId = jacksonUtil.getString("siteId");
				String skinId = jacksonUtil.getString("skinId");
				
				PsTzSitemSkinTKey psTzSitemSkinTKey = new PsTzSitemSkinTKey();
				psTzSitemSkinTKey.setTzSitemId(siteId);
				psTzSitemSkinTKey.setTzSkinId(skinId);
				PsTzSitemSkinT psTzSitemSkinT =psTzSitemSkinTMapper.selectByPrimaryKey(psTzSitemSkinTKey);
				if(psTzSitemSkinT != null){
					Map<String, Object> jsonMap = new HashMap<String, Object>();
					jsonMap.put("skinId", psTzSitemSkinT.getTzSkinId());
					jsonMap.put("skinName", psTzSitemSkinT.getTzSkinName());
					jsonMap.put("skinStatus", psTzSitemSkinT.getTzSkinState());
					jsonMap.put("skinCode", psTzSitemSkinT.getTzSkinCode());
					returnJsonMap.replace("formData", jsonMap);
				}else{
					errMsg[0] = "1";
					errMsg[1] = "未找到对应皮肤设置信息";
				}
				
			} else {
				errMsg[0] = "1";
				errMsg[1] = "参数不正确！";
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
}
