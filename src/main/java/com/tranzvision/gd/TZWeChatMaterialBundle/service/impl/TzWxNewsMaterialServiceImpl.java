package com.tranzvision.gd.TZWeChatMaterialBundle.service.impl;

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
import com.tranzvision.gd.TZWeChatBundle.service.impl.TzWxApiObject;
import com.tranzvision.gd.TZWeChatMaterialBundle.dao.PsTzWxMediaTblMapper;
import com.tranzvision.gd.TZWeChatMaterialBundle.dao.PsTzWxTwlTblMapper;
import com.tranzvision.gd.TZWeChatMaterialBundle.model.PsTzWxMediaTbl;
import com.tranzvision.gd.TZWeChatMaterialBundle.model.PsTzWxMediaTblKey;
import com.tranzvision.gd.TZWeChatMaterialBundle.model.PsTzWxTwlTbl;
import com.tranzvision.gd.TZWeChatMaterialBundle.model.PsTzWxTwlTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZWeChatMaterialBundle.service.impl.TzWxNewsMaterialServiceImpl")
public class TzWxNewsMaterialServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private TzWxApiObject tzWxApiObject;
	
	@Autowired
	private GetSeqNum getSeqNum;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private PsTzWxMediaTblMapper psTzWxMediaTblMapper;
	
	@Autowired
	private PsTzWxTwlTblMapper psTzWxTwlTblMapper;
	
	
	
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg)  {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", new ArrayList<Map<String, Object>>());
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(comParams); 
			
			String jgId = jacksonUtil.getString("jgId");
			String wxAppId = jacksonUtil.getString("wxAppId");
			String materialID = jacksonUtil.getString("materialID");
			
			String sql = "select count(1) from PS_TZ_WX_TWL_TBL where TZ_JG_ID=? and TZ_WX_APPID=? and TZ_XH=?";
			int total = sqlQuery.queryForObject(sql, new Object[]{ jgId, wxAppId, materialID }, "int");
			if(total > 0){
				sql = "select TZ_PUB_STATE,TZ_TW_TITLE,TZ_TW_DESCR,TZ_AUTHOR,TZ_HEAD_IMAGE,TZ_CONTENT,TZ_SHCPIC_FLG,TZ_ART_URL,TZ_SEQNUM from PS_TZ_WX_MEDIA_TBL A join PS_TZ_WX_TWL_TBL B on(A.TZ_JG_ID=B.TZ_JG_ID and A.TZ_WX_APPID=B.TZ_WX_APPID and A.TZ_XH=B.TZ_XH) where A.TZ_JG_ID=? and A.TZ_WX_APPID=? and A.TZ_XH=? order by TZ_SEQNUM";
				List<Map<String, Object>> articesList = sqlQuery.queryForList(sql, new Object[]{ jgId, wxAppId, materialID });
				
				List<Map<String,Object>> dataList = new ArrayList<Map<String, Object>>();
				for(Map<String, Object> articesMap : articesList){
					Map<String, Object> dataMap = new HashMap<String,Object>();
					
					String thumb_img_url = "";
					String thumb_media_id = articesMap.get("TZ_HEAD_IMAGE").toString();
					sql = "select TZ_IMAGE_PATH from PS_TZ_WX_MEDIA_TBL where TZ_JG_ID=? and TZ_WX_APPID=? and TZ_MEDIA_ID=? limit 1";
					thumb_img_url = sqlQuery.queryForObject(sql, new Object[]{ jgId, wxAppId, thumb_media_id }, "String");
					
					dataMap.put("title", articesMap.get("TZ_TW_TITLE"));
					dataMap.put("thumb_media_id", thumb_media_id);
					dataMap.put("thumb_img_url", thumb_img_url);
					dataMap.put("author", articesMap.get("TZ_AUTHOR"));
					dataMap.put("digest", articesMap.get("TZ_TW_DESCR"));
					dataMap.put("show_cover_pic", articesMap.get("TZ_SHCPIC_FLG"));
					dataMap.put("content", articesMap.get("TZ_CONTENT"));
					dataMap.put("content_source_url", articesMap.get("TZ_ART_URL"));
					dataMap.put("orderNum", articesMap.get("TZ_SEQNUM"));
					dataMap.put("isCurrArt", "");
					dataMap.put("publishSta", articesMap.get("TZ_PUB_STATE"));
					
					dataList.add(dataMap);
				}
				
				mapRet.replace("total", total);
				mapRet.replace("root", dataList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		return jacksonUtil.Map2json(mapRet);
	}
	
	
	
	
	
	
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "{}");

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String jgId = jacksonUtil.getString("jgId");
			String wxAppId = jacksonUtil.getString("wxAppId");
			String materialID = jacksonUtil.getString("materialID");
			
			String sql = "select TZ_SC_NAME,TZ_SC_REMARK,TZ_PUB_STATE,TZ_MEDIA_ID from PS_TZ_WX_MEDIA_TBL where TZ_JG_ID=? and TZ_WX_APPID=? and TZ_XH=?";
			Map<String,Object> map=sqlQuery.queryForMap(sql, new Object[]{jgId,wxAppId,materialID});
			
			Map<String, Object> jsonMap2 = new HashMap<String, Object>();

			jsonMap2.put("jgId",jgId);
			jsonMap2.put("wxAppId", wxAppId);
			jsonMap2.put("materialID", materialID);
			
			jsonMap2.put("name", map.get("TZ_SC_NAME"));
			jsonMap2.put("bzInfo", map.get("TZ_SC_REMARK"));
			jsonMap2.put("mediaId", map.get("TZ_MEDIA_ID"));
			
			jsonMap2.put("publishSta", map.get("TZ_PUB_STATE"));
			
			returnJsonMap.replace("formData", jsonMap2);	
		}catch(Exception e){
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "操作异常。"+e.getMessage();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}
	
	
	
	
	
	@Override
	public String tzOther(String strType, String strParams, String[] errorMsg) {
		String strRet = "";
		try {
			switch (strType) {
			case "tzSaveNewsMaterial":
				strRet = this.tzSaveNewsMaterial(strParams,errorMsg);
				break;
			case "tzPublishNewsMaterial":
				strRet = this.tzPublishNewsMaterial(strParams,errorMsg);
				break;
			case "tzRevokeNewsMaterial":
				strRet = this.tzRevokeNewsMaterial(strParams,errorMsg);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
		}
		return strRet;
	}
		
		
		
	@SuppressWarnings("unchecked")
	private String tzSaveNewsMaterial(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("materialID", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		boolean isPublish = false;
		//新增素材JSON
		List<Map<String,Object>> newArticlesList = new ArrayList<Map<String,Object>>();
		try {
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			jacksonUtil.json2Map(strParams);
			String jgId = jacksonUtil.getString("jgId");
			String wxAppId = jacksonUtil.getString("wxAppId");
			String materialID = jacksonUtil.getString("materialID");
			String name = jacksonUtil.getString("name");
			String bzInfo = jacksonUtil.getString("bzInfo");
			
			//发布
			if(jacksonUtil.containsKey("isPublish") 
					&& jacksonUtil.getString("isPublish").equals("Y")){
				isPublish = true;
				rtnMap.put("publishStr", "");
			}
			
			List<Map<String,Object>> articesDataList = (List<Map<String, Object>>) jacksonUtil.getList("articesData");
			if(articesDataList != null && articesDataList.size() > 0){
				if("".equals(materialID)){
					materialID = ""+getSeqNum.getSeqNum("TZ_WX_MEDIA_TBL", "TZ_XH");
					
					PsTzWxMediaTbl PsTzWxMediaTbl = new PsTzWxMediaTbl();
					PsTzWxMediaTbl.setTzJgId(jgId);
					PsTzWxMediaTbl.setTzWxAppid(wxAppId);
					PsTzWxMediaTbl.setTzXh(materialID);
					
					PsTzWxMediaTbl.setTzScName(name);
					PsTzWxMediaTbl.setTzScRemark(bzInfo);
					PsTzWxMediaTbl.setTzPubState("N");
					PsTzWxMediaTbl.setTzMediaType("B");
					
					PsTzWxMediaTbl.setRowAddedDttm(new Date());
					PsTzWxMediaTbl.setRowAddedOprid(oprid);
					PsTzWxMediaTbl.setRowLastmantDttm(new Date());
					PsTzWxMediaTbl.setRowLastmantOprid(oprid);
					int rtn = psTzWxMediaTblMapper.insert(PsTzWxMediaTbl);
					
					if(rtn > 0){
						for(Map<String,Object> articesMap : articesDataList){
							//String xh = articesMap.get("xh").toString();
							String title = articesMap.get("title").toString();
							String thumb_media_id = articesMap.get("thumb_media_id").toString();
							String author = articesMap.get("author") == null ? "" : articesMap.get("author").toString();
							String digest = articesMap.get("digest") == null ? "" : articesMap.get("digest").toString();
							String show_cover_pic = articesMap.get("show_cover_pic").toString();
							String content = articesMap.get("content").toString();
							String content_source_url = articesMap.get("content_source_url") == null ? "" : articesMap.get("content_source_url").toString();
							String orderNum = articesMap.get("orderNum").toString();
							//String thumb_img_url = articesMap.get("thumb_img_url").toString();
							
							PsTzWxTwlTbl psTzWxTwlTbl = new PsTzWxTwlTbl();
							psTzWxTwlTbl.setTzJgId(jgId);
							psTzWxTwlTbl.setTzWxAppid(wxAppId);
							psTzWxTwlTbl.setTzXh(materialID);
							
							psTzWxTwlTbl.setTzSeqnum(orderNum);
							psTzWxTwlTbl.setTzTwTitle(title);
							psTzWxTwlTbl.setTzTwDescr(digest);
							psTzWxTwlTbl.setTzAuthor(author);
							psTzWxTwlTbl.setTzHeadImage(thumb_media_id);
							psTzWxTwlTbl.setTzContent(content);
							if("0".equals(orderNum)){
								psTzWxTwlTbl.setTzShcpicFlg("Y");
							}else{
								psTzWxTwlTbl.setTzShcpicFlg("N");
							}
							
							psTzWxTwlTbl.setTzArtUrl(content_source_url);
							
							psTzWxTwlTbl.setRowAddedDttm(new Date());
							psTzWxTwlTbl.setRowAddedOprid(oprid);
							psTzWxTwlTbl.setRowLastmantDttm(new Date());
							psTzWxTwlTbl.setRowLastmantOprid(oprid);
							
							psTzWxTwlTblMapper.insert(psTzWxTwlTbl);
							
							if(isPublish){
								Map<String,Object> newArticesMap = new HashMap<String,Object>();
								newArticesMap.put("title", title);
								newArticesMap.put("thumb_media_id", thumb_media_id);
								newArticesMap.put("author", author);
								newArticesMap.put("digest", digest);
								newArticesMap.put("show_cover_pic", show_cover_pic);
								newArticesMap.put("content", content);
								newArticesMap.put("content_source_url", "");
								
								newArticlesList.add(newArticesMap);
							}
						}
						
						rtnMap.replace("materialID", materialID);
					}
				}else{
					PsTzWxMediaTblKey psTzWxMediaTblKey = new PsTzWxMediaTblKey();
					psTzWxMediaTblKey.setTzJgId(jgId);
					psTzWxMediaTblKey.setTzWxAppid(wxAppId);
					psTzWxMediaTblKey.setTzXh(materialID);
					PsTzWxMediaTbl PsTzWxMediaTbl = psTzWxMediaTblMapper.selectByPrimaryKey(psTzWxMediaTblKey);
					if(PsTzWxMediaTbl != null){
						PsTzWxMediaTbl.setTzScName(name);
						PsTzWxMediaTbl.setTzScRemark(bzInfo);
						
						PsTzWxMediaTbl.setRowLastmantDttm(new Date());
						PsTzWxMediaTbl.setRowLastmantOprid(oprid);
						int rtn = psTzWxMediaTblMapper.updateByPrimaryKey(PsTzWxMediaTbl);
						if(rtn > 0){
							//是否有删除文章
							if(jacksonUtil.containsKey("removeData")){
								List<Map<String,Object>> removeDataList = (List<Map<String, Object>>) jacksonUtil.getList("removeData");
								if(removeDataList != null && removeDataList.size() > 0){
									//全部删除重新插入
									String delSql = "select TZ_SEQNUM from PS_TZ_WX_TWL_TBL where TZ_JG_ID=? and TZ_WX_APPID=? and TZ_XH=?";
									List<Map<String,Object>> removeAllList = sqlQuery.queryForList(delSql, new Object[]{ jgId, wxAppId, materialID });
									if(removeAllList != null && removeAllList.size() > 0){
										for(Map<String,Object> removeAllMap : removeAllList){
											String orderNum = removeAllMap.get("TZ_SEQNUM").toString();
											
											PsTzWxTwlTblKey psTzWxTwlTblKey = new PsTzWxTwlTblKey();
											psTzWxTwlTblKey.setTzJgId(jgId);
											psTzWxTwlTblKey.setTzWxAppid(wxAppId);
											psTzWxTwlTblKey.setTzXh(materialID);
											psTzWxTwlTblKey.setTzSeqnum(orderNum);
											
											psTzWxTwlTblMapper.deleteByPrimaryKey(psTzWxTwlTblKey);
										}
									}
								}
							}

							for(Map<String,Object> articesMap : articesDataList){
								String title = articesMap.get("title").toString();
								String thumb_media_id = articesMap.get("thumb_media_id").toString();
								String author = articesMap.get("author") == null ? "" : articesMap.get("author").toString();
								String digest = articesMap.get("digest") == null ? "" : articesMap.get("digest").toString();
								String show_cover_pic = articesMap.get("show_cover_pic").toString();
								String content = articesMap.get("content").toString();
								String content_source_url = articesMap.get("content_source_url") == null ? "" : articesMap.get("content_source_url").toString();
								String orderNum = articesMap.get("orderNum").toString();
								//String thumb_img_url = articesMap.get("thumb_img_url").toString();
								
								PsTzWxTwlTblKey psTzWxTwlTblKey = new PsTzWxTwlTblKey();
								psTzWxTwlTblKey.setTzJgId(jgId);
								psTzWxTwlTblKey.setTzWxAppid(wxAppId);
								psTzWxTwlTblKey.setTzXh(materialID);
								psTzWxTwlTblKey.setTzSeqnum(orderNum);
								
								PsTzWxTwlTbl psTzWxTwlTbl = psTzWxTwlTblMapper.selectByPrimaryKey(psTzWxTwlTblKey);
								if(psTzWxTwlTbl != null){
									psTzWxTwlTbl.setTzTwTitle(title);
									psTzWxTwlTbl.setTzTwDescr(digest);
									psTzWxTwlTbl.setTzAuthor(author);
									psTzWxTwlTbl.setTzHeadImage(thumb_media_id);
									psTzWxTwlTbl.setTzContent(content);
									if("0".equals(orderNum)){
										psTzWxTwlTbl.setTzShcpicFlg("Y");
									}else{
										psTzWxTwlTbl.setTzShcpicFlg("N");
									}
									psTzWxTwlTbl.setTzArtUrl(content_source_url);
									
									psTzWxTwlTbl.setRowLastmantDttm(new Date());
									psTzWxTwlTbl.setRowLastmantOprid(oprid);
									
									psTzWxTwlTblMapper.updateByPrimaryKeyWithBLOBs(psTzWxTwlTbl);
								}else{
									psTzWxTwlTbl = new PsTzWxTwlTbl();
									psTzWxTwlTbl.setTzJgId(jgId);
									psTzWxTwlTbl.setTzWxAppid(wxAppId);
									psTzWxTwlTbl.setTzXh(materialID);
									
									psTzWxTwlTbl.setTzSeqnum(orderNum);
									psTzWxTwlTbl.setTzTwTitle(title);
									psTzWxTwlTbl.setTzTwDescr(digest);
									psTzWxTwlTbl.setTzAuthor(author);
									psTzWxTwlTbl.setTzHeadImage(thumb_media_id);
									psTzWxTwlTbl.setTzContent(content);
									if("0".equals(orderNum)){
										psTzWxTwlTbl.setTzShcpicFlg("Y");
									}else{
										psTzWxTwlTbl.setTzShcpicFlg("N");
									}
									psTzWxTwlTbl.setTzArtUrl(content_source_url);
									
									psTzWxTwlTbl.setRowAddedDttm(new Date());
									psTzWxTwlTbl.setRowAddedOprid(oprid);
									psTzWxTwlTbl.setRowLastmantDttm(new Date());
									psTzWxTwlTbl.setRowLastmantOprid(oprid);
									
									psTzWxTwlTblMapper.insert(psTzWxTwlTbl);
								}
								
								
								if(isPublish){
									Map<String,Object> newArticesMap = new HashMap<String,Object>();
									newArticesMap.put("title", title);
									newArticesMap.put("thumb_media_id", thumb_media_id);
									newArticesMap.put("author", author);
									newArticesMap.put("digest", digest);
									newArticesMap.put("show_cover_pic", show_cover_pic);
									newArticesMap.put("content", content);
									newArticesMap.put("content_source_url", "");
									
									newArticlesList.add(newArticesMap);
								}
							}
						}
						
						rtnMap.replace("materialID", materialID);
					}else{
						errorMsg[0] = "1";
						errorMsg[1] = "图文素材不存在";
					}
				}
				
				if(isPublish){
					rtnMap.replace("publishStr", newArticlesList);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}

	
	
	@SuppressWarnings("unchecked")
	private String tzPublishNewsMaterial(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("materialID", "");
		rtnMap.put("mediaId", "");
		rtnMap.put("publishSta", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			jacksonUtil.json2Map(strParams);
			String jgId = jacksonUtil.getString("jgId");
			String wxAppId = jacksonUtil.getString("wxAppId");
			String mediaId = jacksonUtil.getString("mediaId"); //素材mediaId
			
			Map<String,Object> ParamsMap = jacksonUtil.getMap();
			ParamsMap.put("isPublish", "Y");
			
			//先保存
			String result = this.tzSaveNewsMaterial(jacksonUtil.Map2json(ParamsMap), errorMsg);
			jacksonUtil.json2Map(result);
			String materialID = jacksonUtil.getString("materialID");
			List<Map<String,Object>> articesDataList = (List<Map<String, Object>>) jacksonUtil.getList("publishStr");
			
			if("".equals(mediaId)){
				//新增图文
				Map<String,Object> addMap = tzWxApiObject.addMaterialNews(jgId, wxAppId, articesDataList);
				if(addMap.containsKey("errcode")){
					errorMsg[0] = "1";
					errorMsg[1] = addMap.get("errmsg").toString();
				}else{
					mediaId = addMap.get("media_id").toString();
					
					//发布成功，更新发布状态
					PsTzWxMediaTblKey psTzWxMediaTblKey = new PsTzWxMediaTblKey();
					psTzWxMediaTblKey.setTzJgId(jgId);
					psTzWxMediaTblKey.setTzWxAppid(wxAppId);
					psTzWxMediaTblKey.setTzXh(materialID);
					PsTzWxMediaTbl PsTzWxMediaTbl = psTzWxMediaTblMapper.selectByPrimaryKey(psTzWxMediaTblKey);
					if(PsTzWxMediaTbl != null){
						PsTzWxMediaTbl.setTzPubState("Y");
						PsTzWxMediaTbl.setTzMediaId(mediaId);
						
						PsTzWxMediaTbl.setRowLastmantDttm(new Date());
						PsTzWxMediaTbl.setRowLastmantOprid(oprid);
						psTzWxMediaTblMapper.updateByPrimaryKey(PsTzWxMediaTbl);
						
						rtnMap.replace("publishSta", "Y");
					}
				}
			}else{
				//修改图文
				if(articesDataList != null && articesDataList.size() > 0){
					int index = 0;
					for(Map<String,Object> updateMap : articesDataList){
						Map<String,Object> errMap = tzWxApiObject.updateMaterialNews(jgId, wxAppId, mediaId, index, updateMap);
						if(!errMap.get("errcode").toString().equals("0")){
							errorMsg[0] = "1";
							errorMsg[1] = errMap.get("errmsg").toString();
							break;
						}
						index++;
					}
				}
			}
			
			rtnMap.replace("materialID", materialID);
			rtnMap.replace("mediaId", mediaId);
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
	
	
	
	private String tzRevokeNewsMaterial(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("publishSta", "");
		rtnMap.put("mediaId", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			jacksonUtil.json2Map(strParams);
			String jgId = jacksonUtil.getString("jgId");
			String wxAppId = jacksonUtil.getString("wxAppId");
			String materialID = jacksonUtil.getString("materialID");
			String mediaId = jacksonUtil.getString("mediaId"); //素材mediaId
			
			if(mediaId != null && !"".equals(mediaId)){
				Map<String,Object> errMap = tzWxApiObject.deleteMaterial(jgId, wxAppId, mediaId);
				if(errMap.get("errcode").toString().equals("0")){
					//撤销成功，更新发布状态
					PsTzWxMediaTblKey psTzWxMediaTblKey = new PsTzWxMediaTblKey();
					psTzWxMediaTblKey.setTzJgId(jgId);
					psTzWxMediaTblKey.setTzWxAppid(wxAppId);
					psTzWxMediaTblKey.setTzXh(materialID);
					PsTzWxMediaTbl PsTzWxMediaTbl = psTzWxMediaTblMapper.selectByPrimaryKey(psTzWxMediaTblKey);
					if(PsTzWxMediaTbl != null){
						PsTzWxMediaTbl.setTzPubState("N");
						PsTzWxMediaTbl.setTzMediaId("");
						
						PsTzWxMediaTbl.setRowLastmantDttm(new Date());
						PsTzWxMediaTbl.setRowLastmantOprid(oprid);
						psTzWxMediaTblMapper.updateByPrimaryKey(PsTzWxMediaTbl);
						
						rtnMap.replace("publishSta", "N");
					}
				}
			}else{
				errorMsg[0] = "1";
				errorMsg[1] = "素材media_id不存在";
			}
			
			rtnMap.replace("mediaId", "");
			
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
}
