package com.tranzvision.gd.TZWeChatUserBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.protocol.HTTP;
import org.dom4j.tree.BaseElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWeChatUserBundle.dao.PsTzWxTagTblMapper;
import com.tranzvision.gd.TZWeChatUserBundle.dao.PsTzWxUserAetMapper;
import com.tranzvision.gd.TZWeChatUserBundle.dao.PsTzWxUserTblMapper;
import com.tranzvision.gd.TZWeChatUserBundle.dao.PsTzWxuserTagTMapper;
import com.tranzvision.gd.TZWeChatUserBundle.model.PsTzWxUserAet;
import com.tranzvision.gd.TZWeChatUserBundle.model.PsTzWxUserTbl;
import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.batch.engine.base.EngineParameters;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 微信用户管理
 * @author LuYan 2017-8-28
 *
 */
@Service("com.tranzvision.gd.TZWeChatUserBundle.service.impl.TzWeChatUserMgServiceImpl")
public class TzWeChatUserMgServiceImpl extends FrameworkImpl {

	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzWxUserAetMapper psTzWxUserAetMapper;
	@Autowired
	private PsTzWxUserTblMapper psTzWxUserTblMapper;
	@Autowired
	private PsTzWxTagTblMapper psTzWxTagTblMapper;
	@Autowired
	private PsTzWxuserTagTMapper psTzWxuserTagTMapper;
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	
	
	
	/*获取用户信息*/
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
			
			if(jacksonUtil.containsKey("condition")) {
				
				//默认头像url
				String userPictureDefault = getHardCodePoint.getHardCodePointVal("TZ_WX_USER_PIC_DEFAULT");
			
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
							
							String strUserPicture = userPictureDefault;
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
							mapList.put("associateClueName", rowList[9]);
							mapList.put("userTagDesc", rowList[10]);
							
							listData.add(mapList);
						}	
					
						mapRet.replace("total", numTotal);
						mapRet.replace("root",listData);
					}
				}
				
				strRet = jacksonUtil.Map2json(mapRet);
				
			} else {
				//根据标签查询用户信息
				strRet = queryByTag(strParams, errMsg);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	public String tzOther(String operateType,String strParams,String[] errMsg) {
		String strRet="";

		try {
			//获取标签信息
			if("tzGetTag".equals(operateType)) {
				strRet = getTag(strParams,errMsg);
			}
			//执行进程从微信获取全量用户
			if("tzGetUserAllByAe".equals(operateType)) {
				strRet = getUserAllByAe(strParams,errMsg);
			}
			//获取微信服务号下的所有用户
			if("tzGetAllUser".equals(operateType)){
				strRet = getAllUser(strParams,errMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	/*获取标签信息*/
	public String getTag(String strParams,String[] errMsg) {
		String strRet = ""; 
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		Map<String, Object> mapRet = new HashMap<String,Object>();
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String,Object>>();
		
		try {
			
			String tagIdList = "";
			
			jacksonUtil.json2Map(strParams);
			
			String jgId = jacksonUtil.getString("jgId");
			String wxAppId = jacksonUtil.getString("wxAppId");
			
			String sql = "SELECT TZ_WX_TAG_ID,TZ_WX_TAG_NAME FROM PS_TZ_WX_TAG_TBL WHERE TZ_JG_ID=? AND TZ_WX_APPID=? ORDER BY ROW_ADDED_DTTM";
			List<Map<String,Object>> listTag = sqlQuery.queryForList(sql,new Object[]{jgId,wxAppId});
			
			for(Map<String, Object> mapTag : listTag) {
				String tagId = (String) mapTag.get("TZ_WX_TAG_ID");
				String tagName = (String) mapTag.get("TZ_WX_TAG_NAME");
				
				if(!"".equals(tagIdList)) {
					tagIdList = tagIdList + "," + tagId;
				} else {
					tagIdList = tagId;
				}
				
				Map<String, Object> mapData = new HashMap<String,Object>();
				mapData.put("tagId", tagId);
				mapData.put("tagName", tagName);
				
				listData.add(mapData);	
			}
			
			mapRet.put("tagList", listData);
			mapRet.put("tagIdList", tagIdList);
			strRet = jacksonUtil.Map2json(mapRet);
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	/*根据标签搜索用户*/
	public String queryByTag(String strParams,String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		Map<String, Object> mapRet = new HashMap<String,Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String,Object>>();
		mapRet.put("root", listData);
		
		try {
			
			//默认头像url
			String userPictureDefault = getHardCodePoint.getHardCodePointVal("TZ_WX_USER_PIC_DEFAULT");
			
			Integer limit = Integer.valueOf(request.getParameter("limit"));
			Integer start = Integer.valueOf(request.getParameter("start"));
			
			jacksonUtil.json2Map(strParams);
			
			String jgId = jacksonUtil.getString("jgId");
			String wxAppId = jacksonUtil.getString("wxAppId");
			String tagId = jacksonUtil.getString("tagId");
			
			String sql = "SELECT B.TZ_JG_ID,B.TZ_WX_APPID,B.TZ_OPEN_ID,B.TZ_IMAGE_URL,B.TZ_NICKNAME,B.TZ_SUBSRIBE_DT,B.TZ_GL_CONTID,B.TZ_GL_CONTNAME,B.TZ_SALESLEAD_ID,B.TZ_SALESLEAD_NAME,B.TZ_USER_TAG_DESC";
			sql += " FROM PS_TZ_WXUSER_TAG_T A,PS_TZ_WX_USER_VW B";
			sql += " WHERE A.TZ_OPEN_ID=B.TZ_OPEN_ID AND A.TZ_JG_ID=? AND A.TZ_WX_APPID=? AND A.TZ_TAG_ID=?";
			
			if("0".equals(limit) && "0".equals(start)) {
				
			} else {
				sql += " LIMIT " + start + "," + (start+limit);
			}

			List<Map<String, Object>> listUser = sqlQuery.queryForList(sql,new Object[]{jgId,wxAppId,tagId});
			
			for(Map<String, Object> mapUser : listUser) {
				
				String openId = mapUser.get("TZ_OPEN_ID") == null ? "" : mapUser.get("TZ_OPEN_ID").toString();
				String userPicture = mapUser.get("TZ_IMAGE_URL") == null ? "" : mapUser.get("TZ_IMAGE_URL").toString();
				if(userPicture!=null && !"".equals(userPicture)) {
				} else {
					userPicture = userPictureDefault;
				}
				String nickName = mapUser.get("TZ_NICKNAME") == null ? "" : mapUser.get("TZ_NICKNAME").toString();
				String followDttm = mapUser.get("TZ_SUBSRIBE_DT") == null ? "" : mapUser.get("TZ_SUBSRIBE_DT").toString();
				String associateUserId = mapUser.get("TZ_GL_CONTID") == null ? "" : mapUser.get("TZ_GL_CONTID").toString();
				String associateUserName = mapUser.get("TZ_GL_CONTNAME") == null ? "" : mapUser.get("TZ_GL_CONTNAME").toString();
				String associateClueId = mapUser.get("TZ_SALESLEAD_ID") == null ? "" : mapUser.get("TZ_SALESLEAD_ID").toString();
				String associateClueName = mapUser.get("TZ_SALESLEAD_NAME") == null ? "" : mapUser.get("TZ_SALESLEAD_NAME").toString();
				String userTagDesc = mapUser.get("TZ_USER_TAG_DESC") == null ? "" : mapUser.get("TZ_USER_TAG_DESC").toString();
				
				
				Map<String, Object> mapList = new HashMap<String,Object>();
				mapList.put("jgId", jgId);
				mapList.put("wxAppId", wxAppId);
				mapList.put("openId", openId);
				mapList.put("userPicture", userPicture);
				mapList.put("nickName", nickName);
				mapList.put("followDttm", followDttm);
				mapList.put("associateUserId", associateUserId);
				mapList.put("associateUserName", associateUserName);
				mapList.put("associateClueId", associateClueId);
				mapList.put("associateClueName", associateClueName);
				mapList.put("userTagDesc", userTagDesc);
				
				listData.add(mapList);
			}	
		
			//总数
			sql = "SELECT COUNT(1) FROM PS_TZ_WXUSER_TAG_T WHERE TZ_JG_ID=? AND TZ_WX_APPID=? AND TZ_TAG_ID=?";
			Integer numTotal = sqlQuery.queryForObject(sql, new Object[]{jgId,wxAppId,tagId},"Integer");
			
			mapRet.replace("total", numTotal);
			mapRet.replace("root",listData);
			

			strRet = jacksonUtil.Map2json(mapRet);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	/*执行进程从微信获取全量用户*/
	public String getUserAllByAe(String strParams,String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			jacksonUtil.json2Map(strParams);
			String jgId = jacksonUtil.getString("jgId");
			String wxAppId = jacksonUtil.getString("wxAppId");
			
			/*生成运行控制ID*/
			SimpleDateFormat dateFormate = new SimpleDateFormat("yyyyMMddHHmmss");
			String s_dt = dateFormate.format(new Date());
			String runCntlId = "WXUSER" + s_dt + "_" + getSeqNum.getSeqNum("TZ_GD_WXSERVICE_COM", "WXUSER_GET");
			
			/*进程使用参数表*/
			PsTzWxUserAet psTzWxUserAet = new PsTzWxUserAet();
			psTzWxUserAet.setRunCntlId(runCntlId);
			psTzWxUserAet.setTzJgId(jgId);
			psTzWxUserAet.setTzWxAppid(wxAppId);
			psTzWxUserAetMapper.insert(psTzWxUserAet);
			
			
			//当前机构
			String currentOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			//当前登录账号
			String currentDlzhId = tzLoginServiceImpl.getLoginedManagerDlzhid(request);
			
			BaseEngine tmpEngine = tzGdObject.createEngineProcess(currentOrgId, "TZ_WX_USER_PROC");
			
			/*指定调度作业的相关参数*/
			EngineParameters schdProcessParameters = new EngineParameters();
			schdProcessParameters.setBatchServer("");
			schdProcessParameters.setCycleExpression("");
			schdProcessParameters.setLoginUserAccount(currentDlzhId);
			schdProcessParameters.setPlanExcuteDateTime(new Date());
			schdProcessParameters.setRunControlId(runCntlId);
			
			/*调度作业*/
			tmpEngine.schedule(schdProcessParameters);
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	/*AE执行内容*/
	public String getUserAllAeInfo(String jgId,String wxAppId,String[] errMsg) {
		String strRet = "";
		
		try {
			
			/**调用微信接口获取全量用户
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 */
			
			PsTzWxUserTbl psTzWxUserTbl = new PsTzWxUserTbl();
			psTzWxUserTbl.setTzJgId(jgId);
			psTzWxUserTbl.setTzWxAppid(wxAppId);
			psTzWxUserTbl.setTzOpenId("3");
			psTzWxUserTbl.setTzNickname("33");
			psTzWxUserTblMapper.insert(psTzWxUserTbl);
			
		} catch (Exception e) {
			e.toString();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	/*获取微信服务号下的所有用户*/
	public String getAllUser(String strParams,String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			String openIdList = "";
			
			jacksonUtil.json2Map(strParams);
			
			String jgId = jacksonUtil.getString("jgId");
			String wxAppId = jacksonUtil.getString("wxAppId");
			
			String sql = "SELECT TZ_OPEN_ID FROM PS_TZ_WX_USER_TBL WHERE TZ_JG_ID=? AND TZ_WX_APPID=?";
			List<Map<String, Object>> listUser = sqlQuery.queryForList(sql,new Object[]{jgId,wxAppId});
			
			for(Map<String, Object> mapUser : listUser) {
				String openId = mapUser.get("TZ_OPEN_ID") == null ? "" : mapUser.get("TZ_OPEN_ID").toString();
				if(!"".equals(openIdList)) {
					openIdList = openIdList + "," + openId;
				} else {
					openIdList = openId;
				}
			}
			
			mapRet.put("openIdList", openIdList);
			strRet = jacksonUtil.Map2json(mapRet);
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
		
	}
}
