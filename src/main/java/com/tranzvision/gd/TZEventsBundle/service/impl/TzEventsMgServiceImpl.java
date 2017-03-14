/**
 * 
 */
package com.tranzvision.gd.TZEventsBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FileManageServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteInfoBundle.service.impl.ArtContentHtml;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzLmNrGlTMapper;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzLmNrGlTKey;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzLmNrGlTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 活动管理列表页，原PS：TZ_GD_HDGL:SearchActivities
 * 
 * @author SHIHUA
 * @since 2016-02-04
 */
@Service("com.tranzvision.gd.TZEventsBundle.service.impl.TzEventsMgServiceImpl")
public class TzEventsMgServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private FliterForm fliterForm;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private PsTzLmNrGlTMapper psTzLmNrGlTMapper;
	
	@Autowired
	private ArtContentHtml artContentHtml;
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	
	@Autowired
	private GetSeqNum getSeqNum;
	
	@Autowired
	private FileManageServiceImpl fileManageServiceImpl;
	
	@Autowired
	private TZGDObject tzGDObject;

	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

		try {
			// 排序字段
			String[][] orderByArr = new String[][] { new String[] { "TZ_MAX_ZD_SEQ", "DESC" },
					new String[] { "TZ_START_DT", "DESC" }, new String[] { "TZ_START_TM", "DESC" } };

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_ART_ID", "TZ_NACT_NAME", "TZ_NACT_ADDR", "TZ_START_DT", "TZ_END_DT",
					"TZ_APPF_DT", "TZ_APPE_DT", "TZ_APPLY_NUM", "TZ_ART_PUB_STATE",
					"TZ_MAX_ZD_SEQ" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("activityId", rowList[0]);
					mapList.put("activityName", rowList[1]);
					mapList.put("activityPlace", rowList[2]);
					mapList.put("activityStartDate", rowList[3]);
					mapList.put("activityEndDate", rowList[4]);
					mapList.put("applyStartDate", rowList[5]);
					mapList.put("applyEndDate", rowList[6]);
					mapList.put("applyNum", rowList[7]);
					mapList.put("releaseOrUndo", rowList[8]);
					mapList.put("topOrUndo", rowList[9]);
					mapList.put("artZdSeq", rowList[9]);

					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(mapRet);

	}

	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errorMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			String sql = "";

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {

				if (errorMsg.length > 0 && !"0".equals(errorMsg[0])) {
					break;
				} else {
					// 表单内容
					String strForm = actData[num];
					// 解析json
					jacksonUtil.json2Map(strForm);

					String strClickTyp = jacksonUtil.getString("ClickTyp");
					Map<String, Object> mapParams = jacksonUtil.getMap("data");

					//String tzSiteId = String.valueOf(mapParams.getOrDefault("siteId", ""));
					//String tzColuId = String.valueOf(mapParams.getOrDefault("columnId", ""));
					String tzArtId = String.valueOf(mapParams.getOrDefault("activityId", ""));
					int maxZdSeq = 0;
					maxZdSeq = Integer.parseInt(String.valueOf(mapParams.get("artZdSeq")==null?"0":mapParams.get("artZdSeq")));
					
					sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventViewClassId");
					String classId = sqlQuery.queryForObject(sql, "String");
					
					sql = "SELECT TZ_SITE_ID,TZ_COLU_ID FROM PS_TZ_LM_NR_GL_T where TZ_ART_ID=?";
					
					List<Map<String, Object>> list = sqlQuery.queryForList(sql,new Object[]{tzArtId});
					if (list != null && list.size() > 0){ 
						if(maxZdSeq==0){
							String getZdSeqSql = "select max(TZ_MAX_ZD_SEQ) from PS_TZ_LM_NR_GL_T where TZ_ART_ID<>?";
							try {
								maxZdSeq = sqlQuery.queryForObject(getZdSeqSql,
									new Object[] { tzArtId }, "Integer");
							} catch (Exception e) {
								maxZdSeq = 0;
							}
							maxZdSeq = maxZdSeq + 1;
						}
						
						int i = 0;
						for(i =0; i < list.size();i++){
							String tzSiteId = (String)list.get(i).get("TZ_SITE_ID");
							String tzColuId = (String)list.get(i).get("TZ_COLU_ID");
							if (!"".equals(tzSiteId) && !"".equals(tzColuId) && !"".equals(tzArtId)) {
								if ("D".equals(strClickTyp)) {
									PsTzLmNrGlTWithBLOBs psTzLmNrGlTWithBLOBs = new PsTzLmNrGlTWithBLOBs();
									psTzLmNrGlTWithBLOBs.setTzSiteId(tzSiteId);
									psTzLmNrGlTWithBLOBs.setTzColuId(tzColuId);
									psTzLmNrGlTWithBLOBs.setTzArtId(tzArtId);
									psTzLmNrGlTWithBLOBs.setTzMaxZdSeq(maxZdSeq);
									psTzLmNrGlTWithBLOBs.setTzLastmantDttm(dateNow);
									psTzLmNrGlTWithBLOBs.setTzLastmantOprid(oprid);
									psTzLmNrGlTMapper.updateByPrimaryKeySelective(psTzLmNrGlTWithBLOBs);
								}
								if ("T".equals(strClickTyp)) {
									// 置顶处理
									String topFlag = String.valueOf(mapParams.getOrDefault("topOrUndo", ""));
		
									if ("TOP".equals(topFlag.toUpperCase())) {
										
										PsTzLmNrGlTWithBLOBs psTzLmNrGlTWithBLOBs = new PsTzLmNrGlTWithBLOBs();
										psTzLmNrGlTWithBLOBs.setTzSiteId(tzSiteId);
										psTzLmNrGlTWithBLOBs.setTzColuId(tzColuId);
										psTzLmNrGlTWithBLOBs.setTzArtId(tzArtId);
										psTzLmNrGlTWithBLOBs.setTzMaxZdSeq(maxZdSeq);
										psTzLmNrGlTWithBLOBs.setTzLastmantDttm(dateNow);
										psTzLmNrGlTWithBLOBs.setTzLastmantOprid(oprid);
										psTzLmNrGlTMapper.updateByPrimaryKeySelective(psTzLmNrGlTWithBLOBs);
		
									} else if ("0".equals(topFlag)) {
										PsTzLmNrGlTWithBLOBs psTzLmNrGlTWithBLOBs = new PsTzLmNrGlTWithBLOBs();
										psTzLmNrGlTWithBLOBs.setTzSiteId(tzSiteId);
										psTzLmNrGlTWithBLOBs.setTzColuId(tzColuId);
										psTzLmNrGlTWithBLOBs.setTzArtId(tzArtId);
										psTzLmNrGlTWithBLOBs.setTzMaxZdSeq(0);
										psTzLmNrGlTWithBLOBs.setTzLastmantDttm(dateNow);
										psTzLmNrGlTWithBLOBs.setTzLastmantOprid(oprid);
										psTzLmNrGlTMapper.updateByPrimaryKeySelective(psTzLmNrGlTWithBLOBs);
										/*
										sql = "select TZ_MAX_ZD_SEQ from PS_TZ_LM_NR_GL_T where TZ_SITE_ID=? and TZ_COLU_ID=? and TZ_ART_ID=?";
										int tzMaxZdSeq = sqlQuery.queryForObject(sql,
												new Object[] { tzSiteId, tzColuId, tzArtId }, "int");
		
										if (tzMaxZdSeq > 0) {
		
											sql = "update PS_TZ_LM_NR_GL_T set TZ_MAX_ZD_SEQ = TZ_MAX_ZD_SEQ - 1 where TZ_SITE_ID=? and TZ_COLU_ID=? and TZ_MAX_ZD_SEQ>?";
											sqlQuery.update(sql, new Object[] { tzSiteId, tzColuId, tzMaxZdSeq });
		
											
		
										}
										*/
		
									}
		
								} else if ("P".equals(strClickTyp)) {
									// 发布处理
									String tzArtPubState = String.valueOf(mapParams.getOrDefault("releaseOrUndo", ""));
									
									PsTzLmNrGlTKey psTzLmNrGlTKey = new PsTzLmNrGlTKey();
									psTzLmNrGlTKey.setTzSiteId(tzSiteId);
									psTzLmNrGlTKey.setTzColuId(tzColuId);
									psTzLmNrGlTKey.setTzArtId(tzArtId);
									
									PsTzLmNrGlTWithBLOBs psTzLmNrGlTWithBLOBs = psTzLmNrGlTMapper.selectByPrimaryKey(psTzLmNrGlTKey);
									
									psTzLmNrGlTWithBLOBs.setTzArtPubState(tzArtPubState);
									psTzLmNrGlTWithBLOBs.setTzLastmantDttm(dateNow);
									psTzLmNrGlTWithBLOBs.setTzLastmantOprid(oprid);
		
									String tzArtHtml = artContentHtml.getContentHtml(tzSiteId, tzColuId, tzArtId,"PC",request.getContextPath());
									// 解析的模板内容;
									String tzArtHtmlSj = artContentHtml.getContentHtml(tzSiteId, tzColuId, tzArtId,"MS",request.getContextPath());
									
									//如果手机版内容为空，则不存储手机版URL地址
									if("".equals(tzArtHtmlSj) || tzArtHtmlSj == null){
										//publishUrlSj = "";
										tzArtHtmlSj = "";
									}
									
									psTzLmNrGlTWithBLOBs.setTzArtHtml(tzArtHtml);
		
									if ("Y".equals(tzArtPubState)) {
										psTzLmNrGlTWithBLOBs.setTzArtConentScr(tzArtHtml);
										psTzLmNrGlTWithBLOBs.setTzArtSjContScr(tzArtHtmlSj);
									} else {
										psTzLmNrGlTWithBLOBs.setTzArtConentScr("");
										psTzLmNrGlTWithBLOBs.setTzArtSjContScr("");
									}
		
									sql = "select TZ_ART_NEWS_DT from PS_TZ_LM_NR_GL_T where TZ_SITE_ID=? and TZ_COLU_ID=? and TZ_ART_ID=?";
									Date tzArtNewsDt = sqlQuery.queryForObject(sql,
											new Object[] { tzSiteId, tzColuId, tzArtId }, "Date");
									if ("Y".equals(tzArtPubState) && tzArtNewsDt == null) {
										tzArtNewsDt = new Date();
										psTzLmNrGlTWithBLOBs.setTzArtNewsDt(tzArtNewsDt);
									}
									
									
									String strAutoStaticName = psTzLmNrGlTWithBLOBs.getTzStaticAotoName();

									String rootparth = "";
									if(request.getServerPort()==80){
										rootparth = "http://" + request.getServerName() + request.getContextPath();
									}else{
										rootparth = "http://" + request.getServerName() + ":" + request.getServerPort()
										+ request.getContextPath();
									}
									
									String publishUrl = rootparth + "/dispatcher?classid=" + classId + "&operatetype=HTML&siteId="
											+ tzSiteId + "&columnId=" + tzColuId + "&artId=" + tzArtId;
									String publishUrlSj = rootparth + "/dispatcher?classid=" + classId + "&operatetype=HTML&siteId="
											+ tzSiteId + "&columnId=" + tzColuId + "&artId=" + tzArtId + "&from=m";
									
									String strFileName = "";
									String strFilePath = "";
									String strFilePathAccess = "";
									String strFilePathSj = "";
									String strFilePathAccessSj = "";

									/* 获取站点类型 */
									String sqlGetSiteType = "select TZ_SITEI_TYPE from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
									String strSiteType = sqlQuery.queryForObject(sqlGetSiteType,
											new Object[] { tzSiteId }, "String");
									
									if ("C".equals(strSiteType)) {
										// 招生站点;

										if ("Y".equals(tzArtPubState)) {

											psTzLmNrGlTWithBLOBs.setTzStaticArtUrl(publishUrl);
											if("".equals(tzArtHtmlSj) || tzArtHtmlSj==null){
												psTzLmNrGlTWithBLOBs.setTzArtUrl("");
											}else{
												psTzLmNrGlTWithBLOBs.setTzArtUrl(publishUrlSj);
											}
											psTzLmNrGlTMapper.updateByPrimaryKeyWithBLOBs(psTzLmNrGlTWithBLOBs);

										} else {
											psTzLmNrGlTWithBLOBs.setTzArtConentScr("");
											psTzLmNrGlTWithBLOBs.setTzArtSjContScr("");
											psTzLmNrGlTWithBLOBs.setTzStaticArtUrl("");
											psTzLmNrGlTWithBLOBs.setTzArtUrl("");
											psTzLmNrGlTWithBLOBs.setTzStaticAotoName("");
											psTzLmNrGlTMapper.updateByPrimaryKeyWithBLOBs(psTzLmNrGlTWithBLOBs);
										}
									} else {
										// 获取静态路径地址
										String strBasePath = "";
										String getBasePathSql = "SELECT TZ_COLU_PATH FROM PS_TZ_SITEI_COLU_T WHERE TZ_SITEI_ID = ? AND TZ_COLU_LEVEL = '0' LIMIT 1";
										strBasePath = sqlQuery.queryForObject(getBasePathSql,
												new Object[] { tzSiteId }, "String");
										String strColuPath = "";
										String getColuPathSql = "SELECT TZ_COLU_PATH FROM PS_TZ_SITEI_COLU_T WHERE TZ_SITEI_ID = ? AND TZ_COLU_ID = ? LIMIT 1";
										strColuPath = sqlQuery.queryForObject(getColuPathSql,
												new Object[] { tzSiteId, tzColuId }, "String");

										if (strBasePath != null && !"".equals(strBasePath)) {
											if (!strBasePath.startsWith("/")) {
												strBasePath = "/" + strBasePath;
											}
											if (strBasePath.endsWith("/")) {
												strBasePath = strBasePath.substring(0, strBasePath.length() - 1);
											}
										}

										if (strColuPath != null && !"".equals(strColuPath)) {
											if (!strColuPath.startsWith("/")) {
												strColuPath = "/" + strColuPath;
											}
											if (strColuPath.endsWith("/")) {
												strColuPath = strColuPath.substring(0, strColuPath.length() - 1);
											}
										}

										strFilePath = getSysHardCodeVal.getWebsiteEnrollPath() + strBasePath
												+ strColuPath;
										strFilePathSj = strFilePath + "/m";
										strFilePathAccess = strBasePath + strColuPath;
										strFilePathAccessSj = strFilePathAccess + "/m";

										if ("Y".equals(tzArtPubState)) {
											// 如果是外网站点，则静态话
											if ("A".equals(strSiteType) || "B".equals(strSiteType)) {
												// 静态化
												if (strAutoStaticName == null || "".equals(strAutoStaticName)) {
													strAutoStaticName = String.valueOf(getSeqNum
															.getSeqNum("TZ_LM_NR_GL_T", "TZ_STATIC_A_NAME"));
												}
												
												strFileName = strAutoStaticName + ".html";
												strFilePath = request.getServletContext().getRealPath(strFilePath);
												artContentHtml.staticFile(tzArtHtml, strFilePath, strFileName);
												if("".equals(tzArtHtmlSj) || tzArtHtmlSj==null){
													psTzLmNrGlTWithBLOBs.setTzArtUrl("");
												}else{
													artContentHtml.staticFile(tzArtHtmlSj, strFilePathSj, strFileName);
													publishUrlSj = strFilePathAccessSj + "/"
															+ strFileName;
													psTzLmNrGlTWithBLOBs.setTzArtUrl(publishUrlSj);
												}
												artContentHtml.staticSiteInfoByChannel(tzSiteId, tzColuId);
												publishUrl = strFilePathAccess + "/"
														+ strFileName;
												psTzLmNrGlTWithBLOBs.setTzStaticArtUrl(publishUrl);
												psTzLmNrGlTWithBLOBs.setTzStaticAotoName(strAutoStaticName);
												psTzLmNrGlTMapper.updateByPrimaryKeyWithBLOBs(psTzLmNrGlTWithBLOBs);

											}
										} else {
											artContentHtml.staticSiteInfoByChannel(tzSiteId, tzColuId);
											if (strAutoStaticName != null && !"".equals(strAutoStaticName)) {
												fileManageServiceImpl.DeleteFile(strFilePath,
														strAutoStaticName + ".html");
												//同时删除手机版本的地址
												fileManageServiceImpl.DeleteFile(strFilePathSj,
														strAutoStaticName + ".html");
											}

											psTzLmNrGlTWithBLOBs.setTzStaticArtUrl("");
											psTzLmNrGlTWithBLOBs.setTzArtUrl("");
											psTzLmNrGlTWithBLOBs.setTzStaticAotoName("");
											psTzLmNrGlTMapper.updateByPrimaryKeyWithBLOBs(psTzLmNrGlTWithBLOBs);
										}

									}
		
									int rst = psTzLmNrGlTMapper.updateByPrimaryKeySelective(psTzLmNrGlTWithBLOBs);
		
									if (rst > 0) {
		
									} else {
										errorMsg[0] = "1";
										errorMsg[1] = "更新数据时发生错误！";
									}
		
								}
		
							} else {
								errorMsg[0] = "1";
								errorMsg[1] = "更新数据时发现参数有误！";
							}
						}
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作失败。" + e.getMessage();
		}

		return strRet;
	}

}
