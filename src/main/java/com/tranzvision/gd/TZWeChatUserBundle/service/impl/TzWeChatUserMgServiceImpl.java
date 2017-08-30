package com.tranzvision.gd.TZWeChatUserBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;

/**
 * 微信用户管理
 * @author LuYan 2017-8-28
 *
 */
@Service("com.tranzvision.gd.TZWeChatUserBundle.service.impl.TzWeChatUserMgServiceImpl")
public class TzWeChatUserMgServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;
	
	@Override
	@SuppressWarnings({"unchecked","unused"})
	public String tzQueryList(String strParams,int numLimit,int numStart,String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		Map<String, Object> mapRet = new HashMap<String,Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String,Object>>();
		mapRet.put("root", listData);
		
		try {
			
			//排序字段
			String[][] orderByArr = new String[][] {};
			
			//json数据要的结果字段
			String[] resultFldArray = {"TZ_JG_ID","TZ_WX_APPID","TZ_OPEN_ID","TZ_IMAGE_URL","TZ_NICKNAME","TZ_SUBSRIBE_DT","TZ_GL_CONTID","TZ_GL_CONTNAME","TZ_SALESLEAD_ID","TZ_SALESLEAD_NAME","TZ_USER_TAG_DESC"};
			
			//可配置搜索通用函数
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errMsg);
			
			if(obj!=null && obj.length>0) {
				int numTotal = (int) obj[0];
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				
				if(list!=null && list.size()>0) {
					for(int i=0;i<list.size();i++) {
						String[] rowList = list.get(i);
						
						String strUserPicture = "/university/statics/images/appeditor/bjphoto.jpg";
						if(rowList[3]!=null && !"".equals(rowList[3])) {
							strUserPicture = rowList[3];
						}
						
						Map<String, Object> mapList = new HashMap<String,Object>();
						mapList.put("jgId", rowList[0]);
						mapList.put("wxAppId", rowList[1]);
						mapList.put("openId", rowList[2]);
						mapList.put("userPicture", strUserPicture);
						mapList.put("nickName", rowList[4]);
						mapList.put("followDttm", rowList[5]);
						mapList.put("associateUserId", rowList[6]);
						mapList.put("associateUserName", rowList[7]);
						mapList.put("associateClueId", rowList[8]);
						mapList.put("associateClueId", rowList[9]);
						mapList.put("userTagDesc", rowList[10]);
						
						listData.add(mapList);
					}	
					
					mapRet.replace("total", numTotal);
					mapRet.replace("root",listData);
				}
			}
			
			strRet = jacksonUtil.Map2json(mapRet);
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
}
