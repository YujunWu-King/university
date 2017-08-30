package com.tranzvision.gd.TZWeChatUserBundle.service.impl;

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
import com.tranzvision.gd.TZWeChatUserBundle.dao.PsTzWxuserTagTMapper;
import com.tranzvision.gd.TZWeChatUserBundle.model.PsTzWxTagTbl;
import com.tranzvision.gd.TZWeChatUserBundle.model.PsTzWxTagTblKey;
import com.tranzvision.gd.TZWeChatUserBundle.model.PsTzWxuserTagT;
import com.tranzvision.gd.TZWeChatUserBundle.model.PsTzWxuserTagTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 微信标签列表
 * @author LuYan 2017-8-29
 *
 */
@Service("com.tranzvision.gd.TZWeChatUserBundle.service.impl.TzWeChatTagServiceImpl")
public class TzWeChatTagServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsTzWxuserTagTMapper psTzWxuserTagTMapper;
	
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
			
			jacksonUtil.json2Map(strParams);
			String jgId = jacksonUtil.getString("jgId");
			String wxAppId = jacksonUtil.getString("wxAppId");
			String openId = jacksonUtil.getString("openId");
			
			String sql = "SELECT TZ_WX_TAG_ID,TZ_WX_TAG_NAME FROM PS_TZ_WX_TAG_TBL WHERE TZ_JG_ID=? AND TZ_WX_APPID=?";
			List<Map<String, Object>> listTag = sqlQuery.queryForList(sql,new Object[]{jgId,wxAppId});
			
			for(Map<String, Object> mapTag : listTag) {
				String tagId = (String) mapTag.get("TZ_WX_TAG_ID");
				String tagName = (String) mapTag.get("TZ_WX_TAG_NAME");
							
				Boolean selectFlag = false;
				if(!"".equals(openId)) {
					String sqlFlag = "SELECT COUNT(1) FROM PS_TZ_WXUSER_TAG_T WHERE TZ_JG_ID=? AND TZ_WX_APPID=? AND TZ_OPEN_ID=? AND TZ_TAG_ID=?";
					Integer num = sqlQuery.queryForObject(sqlFlag, new Object[]{jgId,wxAppId,openId,tagId},"Integer");
					if(num>0) {
						selectFlag = true;
					} 
				}
				
				Map<String,Object> mapList = new HashMap<String,Object>();
				mapList.put("jgId", jgId);
				mapList.put("wxAppId", wxAppId);
				mapList.put("tagId", tagId);
				mapList.put("tagName", tagName);
				mapList.put("selectFlag", selectFlag);
				
				listData.add(mapList);
			}
								
			mapRet.replace("total", listTag.size());
			mapRet.replace("root", listData);
			
			strRet = jacksonUtil.Map2json(mapRet);
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	/* 保存 */
	@SuppressWarnings("unused")
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			//当前登录人
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			int num = 0;
			for (num = 0; num < actData.length; num++) {
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				
				String jgId = jacksonUtil.getString("jgId");
				String wxAppId = jacksonUtil.getString("wxAppId");
				String openIdListTmp = jacksonUtil.getString("openIdList");
				String selectTagIdTmp = jacksonUtil.getString("selectTagId");
				String NoSelectTagIdTmp = jacksonUtil.getString("NoSelectTagId");
				
				String[] openIdArr = openIdListTmp.split(",");
				String[] selectTagIdArr = selectTagIdTmp.split(",");
				String[] NoSelectTagIdArr = NoSelectTagIdTmp.split(",");
				
				for(String openId : openIdArr) {
					
					for(String NoSelectTagId :  NoSelectTagIdArr) {
						//删除用户标签关系表
						String sql = "DELETE FROM PS_TZ_WXUSER_TAG_T WHERE TZ_JG_ID=? AND TZ_WX_APPID=? AND TZ_OPEN_ID=? AND TZ_TAG_ID=?";
						sqlQuery.update(sql,new Object[]{jgId,wxAppId,openId,NoSelectTagId});
						
						//调用接口同步微信端
						/*
						 * 
						 * 
						 * 
						 * 
						 * 
						 * 
						 * 
						 * 
						 */
					}
					
					for(String selectTagId : selectTagIdArr) {
						PsTzWxuserTagTKey psTzWxuserTagTKey= new PsTzWxuserTagTKey();
						psTzWxuserTagTKey.setTzJgId(jgId);
						psTzWxuserTagTKey.setTzWxAppid(wxAppId);
						psTzWxuserTagTKey.setTzOpenId(openId);
						psTzWxuserTagTKey.setTzTagId(selectTagId); 
						
						PsTzWxuserTagT psTzWxuserTagT = psTzWxuserTagTMapper.selectByPrimaryKey(psTzWxuserTagTKey);
						if(psTzWxuserTagT==null) {
							psTzWxuserTagT = new PsTzWxuserTagT();
							psTzWxuserTagT.setTzJgId(jgId);
							psTzWxuserTagT.setTzWxAppid(wxAppId);
							psTzWxuserTagT.setTzOpenId(openId);
							psTzWxuserTagT.setTzTagId(selectTagId);
							psTzWxuserTagT.setRowAddedDttm(new Date());
							psTzWxuserTagT.setRowAddedOprid(oprid);
							psTzWxuserTagT.setRowLastmantDttm(new Date());
							psTzWxuserTagT.setRowLastmantOprid(oprid);
							psTzWxuserTagTMapper.insertSelective(psTzWxuserTagT);
							
							//调用接口同步微信端
							/*
							 * 
							 * 
							 * 
							 * 
							 * 
							 * 
							 * 
							 * 
							 */
						}
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
}
