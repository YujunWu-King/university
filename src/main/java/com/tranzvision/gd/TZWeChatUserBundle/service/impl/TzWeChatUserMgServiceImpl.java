package com.tranzvision.gd.TZWeChatUserBundle.service.impl;

import java.lang.reflect.Array;
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
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWeChatBundle.service.impl.TzWxApiObject;
import com.tranzvision.gd.TZWeChatUserBundle.dao.PsTzWxTagTblMapper;
import com.tranzvision.gd.TZWeChatUserBundle.dao.PsTzWxUserAetMapper;
import com.tranzvision.gd.TZWeChatUserBundle.dao.PsTzWxUserTblMapper;
import com.tranzvision.gd.TZWeChatUserBundle.dao.PsTzWxuserTagTMapper;
import com.tranzvision.gd.TZWeChatUserBundle.model.PsTzWxTagTbl;
import com.tranzvision.gd.TZWeChatUserBundle.model.PsTzWxTagTblKey;
import com.tranzvision.gd.TZWeChatUserBundle.model.PsTzWxUserAet;
import com.tranzvision.gd.TZWeChatUserBundle.model.PsTzWxUserTbl;
import com.tranzvision.gd.TZWeChatUserBundle.model.PsTzWxUserTblKey;
import com.tranzvision.gd.TZWeChatUserBundle.model.PsTzWxuserTagT;
import com.tranzvision.gd.TZWeChatUserBundle.model.PsTzWxuserTagTKey;
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
	@Autowired
	private TzWxApiObject tzWxApiObject;
	
	
	
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
			
			//当前登录人OPRID
			String currentOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
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
			psTzWxUserAet.setOprid(currentOprid);
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
	public String getUserAllAeInfo(String jgId,String wxAppId,String oprid,String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String, Object> mapRet = new HashMap<String,Object>();
		
		try {
			
			String errcode = "",errmsg = "";
			
			/*调用微信接口*/
			
			/*获取标签*/
			Map<String, Object> mapTagList = new HashMap<String,Object>();
			List<Map<String, Object>> listTags = new ArrayList<Map<String,Object>>();
			String tagId = "", tagName = "";
			Integer tagCount = 0;
			mapTagList = tzWxApiObject.getAllTags(jgId,wxAppId);
			
			if(mapTagList.containsKey("errcode")) {
				//发生错误
				errcode = mapTagList.get("errcode") == null ? "" : mapTagList.get("errcode").toString();
				errmsg = mapTagList.get("errmsg") == null ? "" : mapTagList.get("errmsg").toString();
			} else {
				listTags = (ArrayList<Map<String,Object>>) mapTagList.get("tags");
				for(Map<String, Object> mapTags : listTags) {
					tagId = mapTags.get("id") == null ? "" : mapTags.get("id").toString();
					tagName = mapTags.get("name") == null ? "" : mapTags.get("name").toString();
					tagCount = mapRet.get("count") == null ? 0 :Integer.valueOf(mapRet.get("count").toString());
					
					PsTzWxTagTblKey psTzWxTagTblKey = new PsTzWxTagTblKey();
					psTzWxTagTblKey.setTzJgId(jgId);
					psTzWxTagTblKey.setTzWxAppid(wxAppId);
					psTzWxTagTblKey.setTzWxTagId(tagId);
					
					PsTzWxTagTbl psTzWxTagTbl = psTzWxTagTblMapper.selectByPrimaryKey(psTzWxTagTblKey);
					if(psTzWxTagTbl==null) {
						psTzWxTagTbl = new PsTzWxTagTbl();
						psTzWxTagTbl.setTzJgId(jgId);
						psTzWxTagTbl.setTzWxAppid(wxAppId);
						psTzWxTagTbl.setTzWxTagId(tagId);
						psTzWxTagTbl.setTzWxTagName(tagName);
						psTzWxTagTbl.setRowAddedDttm(new Date());
						psTzWxTagTbl.setRowAddedOprid(oprid);
						psTzWxTagTbl.setRowLastmantDttm(new Date());
						psTzWxTagTbl.setRowLastmantOprid(oprid);
						psTzWxTagTblMapper.insert(psTzWxTagTbl);
					} else {
						psTzWxTagTbl.setTzWxTagName(tagName);
						psTzWxTagTbl.setRowLastmantDttm(new Date());
						psTzWxTagTbl.setRowLastmantOprid(oprid);
						psTzWxTagTblMapper.updateByPrimaryKeySelective(psTzWxTagTbl);
					}
				}
			}
			
			
			/*获取用户列表*/
			Map<String, Object> mapUserList = new HashMap<String,Object>();
			Integer total = 0, count = 0;
			Map<String, Object> mapData = new HashMap<String,Object>();
			List<String> listOpenid = new ArrayList<String>();
			String next_openid = "";
			
			mapUserList = tzWxApiObject.getUserList(jgId,wxAppId,"");
			
			if(mapUserList.containsKey("errcode")) {
				//发生错误
				errcode = mapUserList.get("errcode") == null ? "" : mapUserList.get("errcode").toString();
				errmsg = mapUserList.get("errmsg") == null ? "" : mapUserList.get("errmsg").toString();
			} else {
				total = mapUserList.get("total") ==null ? 0 : Integer.valueOf(mapUserList.get("total").toString());
				count = mapUserList.get("count") ==null ? 0 : Integer.valueOf(mapUserList.get("count").toString());
				mapData = (Map<String, Object>) mapUserList.get("data");
				listOpenid = (ArrayList<String>) mapData.get("openid");
				next_openid = mapUserList.get("next_openid") == null ? "" : mapUserList.get("next_openid").toString();
				
				
				while("10000".equals(count) && !"".equals(next_openid)) {
					mapUserList = tzWxApiObject.getUserList(jgId,wxAppId,"");
					
					if(mapUserList.containsKey("errcode")) {
						//发生错误
						errcode = mapUserList.get("errcode") == null ? "" : mapUserList.get("errcode").toString();
						errmsg = mapUserList.get("errmsg") == null ? "" : mapUserList.get("errmsg").toString();
					} else {
						total = mapUserList.get("total") ==null ? 0 : Integer.valueOf(mapUserList.get("total").toString());
						count = mapUserList.get("count") ==null ? 0 : Integer.valueOf(mapUserList.get("count").toString());
						mapData = (Map<String, Object>) mapUserList.get("data");
						listOpenid = (ArrayList<String>) mapData.get("openid");
						next_openid = mapUserList.get("next_openid") == null ? "" : mapUserList.get("next_openid").toString();	
					}
				}	
			}
			
			if(listOpenid.size()>0) {
				/*获取用户信息*/
				Map<String, Object> mapUserInfo = new HashMap<String,Object>();
				String subscribe = "", openid = "", nickname = "", sex = "", language = "", city = "", province = "", country = "",
						headimgurl = "", subscribe_time_number = "", unionid = "", remark = "", groupid = "";
				Date subscribe_time;
				List<Integer> tagid_list = new ArrayList<Integer>();
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				for(String openidTmp : listOpenid) {
					mapUserInfo =  tzWxApiObject.getUserInfo(jgId,wxAppId,openidTmp,"zh_CN");
					
					if(mapUserInfo.containsKey("errcode")) {
						//发生错误
						errcode = mapUserInfo.get("errcode") == null ? "" : mapUserInfo.get("errcode").toString();
						errmsg = mapUserInfo.get("errmsg") == null ? "" : mapUserInfo.get("errmsg").toString();
					} else {
						//订阅标识，0为未订阅，拉取不到其他信息
						subscribe = mapUserInfo.get("subscribe") == null ? "" : mapUserInfo.get("subscribe").toString(); 
						openid = mapUserInfo.get("openid") == null ? "" : mapUserInfo.get("openid").toString(); 
						nickname = mapUserInfo.get("nickname") == null ? "" : mapUserInfo.get("nickname").toString(); 
						//1男性2女性0未知
						sex = mapUserInfo.get("sex") == null ? "" : mapUserInfo.get("sex").toString();  
						language = mapUserInfo.get("language") == null ? "" : mapUserInfo.get("language").toString(); 
						city = mapUserInfo.get("city") == null ? "" : mapUserInfo.get("city").toString(); 
						province = mapUserInfo.get("province") == null ? "" : mapUserInfo.get("province").toString(); 
						country = mapUserInfo.get("country") == null ? "" : mapUserInfo.get("country").toString(); 
						headimgurl = mapUserInfo.get("headimgurl") == null ? "" : mapUserInfo.get("headimgurl").toString(); 
						//时间戳
						subscribe_time_number = mapUserInfo.get("subscribe_time") == null ? "" : mapUserInfo.get("subscribe_time").toString(); 
						unionid = mapUserInfo.get("unionid") == null ? "" : mapUserInfo.get("unionid").toString(); 
						remark = mapUserInfo.get("remark") == null ? "" : mapUserInfo.get("remark").toString(); 
						groupid = mapUserInfo.get("groupid") == null ? "" : mapUserInfo.get("groupid").toString(); 
						tagid_list = (ArrayList<Integer>) mapUserInfo.get("tagid_list");
						
						//时间戳转为时间
						Long subscribe_time_long = new Long(subscribe_time_number+"000");
						String subscribe_time_string = simpleDateFormat.format(new Date(subscribe_time_long));
						subscribe_time = simpleDateFormat.parse(subscribe_time_string);
						
						PsTzWxUserTblKey psTzWxUserTblKey = new PsTzWxUserTblKey();
						psTzWxUserTblKey.setTzJgId(jgId);
						psTzWxUserTblKey.setTzWxAppid(wxAppId);
						psTzWxUserTblKey.setTzOpenId(openid);
						
						PsTzWxUserTbl psTzWxUserTbl = psTzWxUserTblMapper.selectByPrimaryKey(psTzWxUserTblKey);
						
						if(psTzWxUserTbl==null) {
							psTzWxUserTbl = new PsTzWxUserTbl();
							psTzWxUserTbl.setTzJgId(jgId);
							psTzWxUserTbl.setTzWxAppid(wxAppId);
							psTzWxUserTbl.setTzOpenId(openid);
							psTzWxUserTbl.setTzSubscribe(subscribe);
							psTzWxUserTbl.setTzNickname(nickname);
							psTzWxUserTbl.setTzSex(sex);
							psTzWxUserTbl.setTzLanguage(language);
							psTzWxUserTbl.setTzCity(city);
							psTzWxUserTbl.setTzProvince(province);
							psTzWxUserTbl.setTzCountry(country);
							psTzWxUserTbl.setTzImageUrl(headimgurl);
							psTzWxUserTbl.setTzSubsribeDt(subscribe_time);
							psTzWxUserTbl.setTzRemark(remark);
							psTzWxUserTblMapper.insert(psTzWxUserTbl);
						} else {
							psTzWxUserTbl.setTzSubscribe(subscribe);
							psTzWxUserTbl.setTzNickname(nickname);
							psTzWxUserTbl.setTzSex(sex);
							psTzWxUserTbl.setTzLanguage(language);
							psTzWxUserTbl.setTzCity(city);
							psTzWxUserTbl.setTzProvince(province);
							psTzWxUserTbl.setTzCountry(country);
							psTzWxUserTbl.setTzImageUrl(headimgurl);
							psTzWxUserTbl.setTzSubsribeDt(subscribe_time);
							psTzWxUserTbl.setTzRemark(remark);
							psTzWxUserTblMapper.updateByPrimaryKeySelective(psTzWxUserTbl);
						}
						
						//增加用户和标签关系表
						for(Integer tagidTmp : tagid_list) {
							PsTzWxuserTagTKey psTzWxuserTagTKey = new PsTzWxuserTagTKey();
							psTzWxuserTagTKey.setTzJgId(jgId);
							psTzWxuserTagTKey.setTzWxAppid(wxAppId);
							psTzWxuserTagTKey.setTzOpenId(openid);
							psTzWxuserTagTKey.setTzTagId(String.valueOf(tagidTmp));
							
							PsTzWxuserTagT psTzWxuserTagT = psTzWxuserTagTMapper.selectByPrimaryKey(psTzWxuserTagTKey);
							
							if(psTzWxuserTagT==null) {
								psTzWxuserTagT = new PsTzWxuserTagT();
								psTzWxuserTagT.setTzJgId(jgId);
								psTzWxuserTagT.setTzWxAppid(wxAppId);
								psTzWxuserTagT.setTzOpenId(openid);
								psTzWxuserTagT.setTzTagId(String.valueOf(tagidTmp));
								psTzWxuserTagT.setRowAddedDttm(new Date());
								psTzWxuserTagT.setRowAddedOprid(oprid);
								psTzWxuserTagT.setRowLastmantDttm(new Date());
								psTzWxuserTagT.setRowLastmantOprid(oprid);
								psTzWxuserTagTMapper.insert(psTzWxuserTagT);
							}
						}
					}
				}
			} 
						
		} catch (Exception e) {
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
