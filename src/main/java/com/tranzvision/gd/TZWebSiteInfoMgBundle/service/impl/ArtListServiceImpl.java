package com.tranzvision.gd.TZWebSiteInfoMgBundle.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FileManageServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationOutSiteMgBundle.dao.PsTzArtTypeTMapper;
import com.tranzvision.gd.TZOrganizationOutSiteMgBundle.dao.PsTzContFldefTMapper;
import com.tranzvision.gd.TZOrganizationOutSiteMgBundle.model.PsTzArtTypeT;
import com.tranzvision.gd.TZOrganizationOutSiteMgBundle.model.PsTzContFldefT;
import com.tranzvision.gd.TZOrganizationOutSiteMgBundle.model.PsTzContFldefTKey;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiDefnTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiDefnTWithBLOBs;
import com.tranzvision.gd.TZWebSiteInfoBundle.service.impl.ArtContentHtml;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzArtFileTMapper;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzArtFjjTMapper;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzArtPicTMapper;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzArtPrjTMapper;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzArtRecTblMapper;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzArtTitimgTMapper;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzArtTpjTMapper;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzLmNrGlTMapper;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtRecTblWithBLOBs;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzLmNrGlTKey;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzLmNrGlTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * @author zhangbb
 * @version 创建时间：2016年8月17日 下午16:28:30 类说明 内容发布管理
 */
@Service("com.tranzvision.gd.TZWebSiteInfoMgBundle.service.impl.ArtListServiceImpl")
public class ArtListServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private FliterForm fliterForm;
	
	@Autowired
	private TZGDObject tzSQLObject;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private PsTzArtRecTblMapper psTzArtRecTblMapper;
	@Autowired
	private PsTzLmNrGlTMapper psTzLmNrGlTMapper;
	@Autowired
	private PsTzSiteiDefnTMapper psTzSiteiDefnTMapper;
	@Autowired
	private PsTzArtTitimgTMapper psTzArtTitimgTMapper;
	@Autowired
	private PsTzArtFjjTMapper psTzArtFjjTMapper;
	@Autowired
	private PsTzArtTpjTMapper psTzArtTpjTMapper;
	@Autowired
	private PsTzArtPicTMapper psTzArtPicTMapper;
	@Autowired
	private PsTzArtFileTMapper psTzArtFileTMapper;
	@Autowired
	private PsTzArtPrjTMapper psTzArtPrjTMapper;
	@Autowired
	private ArtContentHtml artContentHtml;
	@Autowired
	private FileManageServiceImpl fileManageServiceImpl;	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	
	/* 查询类型类型列表 */
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { { "TZ_MAX_ZD_SEQ", "DESC" }, { "TZ_ART_NEWS_DT", "DESC" } };

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_SITE_ID", "TZ_COLU_ID", "TZ_ART_ID", "TZ_ART_TITLE", "TZ_ART_NEWS_DT",
					"TZ_REALNAME", "TZ_ART_PUB_STATE", "TZ_MAX_ZD_SEQ", "TZ_PAGE_REFCODE" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr,strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("siteId", rowList[0]);
					mapList.put("columnId", rowList[1]);
					mapList.put("articleId", rowList[2]);
					mapList.put("articleTitle", rowList[3]);
					mapList.put("releaseTime", rowList[4]);
					mapList.put("lastUpdate", rowList[5]);
					mapList.put("releaseOrUndo", rowList[6]);
					mapList.put("topOrUndo", rowList[7]);
					mapList.put("classId", rowList[8]);

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
	
	/* 获取内容栏目树类型定义*/
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 获取登录的机构;
			String strJgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			jacksonUtil.json2Map(strParams);

			// 查询类型
			String typeFlag = jacksonUtil.getString("typeFlag");

			String sql;
			Map<String, Object> mapRet = new HashMap<String, Object>();
			if ("SITE".equals(typeFlag)) {
				//站点;

				ArrayList<Map<String, Object>> arraylist = new ArrayList<>();
				mapRet.put("TransList", arraylist);
				String getSiteSQL = "SELECT TZ_SITEI_ID,TZ_SITEI_NAME,TZ_SITEI_DESCR FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ENABLE='Y' and TZ_JG_ID=?";

				List<Map<String, Object>> list = jdbcTemplate.queryForList(getSiteSQL, new Object[] { strJgid });
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> jsonMap = new HashMap<>();
						jsonMap.put("TValue", list.get(i).get("TZ_SITEI_ID"));
						jsonMap.put("TSDesc", list.get(i).get("TZ_SITEI_NAME"));
						jsonMap.put("TLDesc", list.get(i).get("TZ_SITEI_DESCR"));

						arraylist.add(jsonMap);
					}
					mapRet.replace("TransList", arraylist);
				}
				strRet = jacksonUtil.Map2json(mapRet);
			}else if ("TREE".equals(typeFlag)) {
				// 站点编号
				String siteId = jacksonUtil.getString("siteid");
				// 一次性获取一整颗树的所有数据放入List
				sql = tzSQLObject.getSQLText("SQL.TZOutSiteMgBundle.TzSelectOutSiteColuList");

				List<Map<String, Object>> listData = jdbcTemplate.queryForList(sql, new Object[] { siteId });

				if (null == listData || listData.size() <= 0) {
					/*
					errMsg[0] = "1";
					errMsg[1] = "树不存在";
					*/
					Map<String, Object> mapRootJson = new HashMap<String, Object>();
					mapRootJson.put("id", "NAN");
					mapRootJson.put("nodeId", "NAN");
					mapRootJson.put("siteId", "");
					mapRootJson.put("text", "");
					mapRootJson.put("NodeType", "");
					mapRootJson.put("operateNode", "");
					mapRootJson.put("rootNode", "");
					mapRootJson.put("expanded", "true");
					mapRootJson.put("children", "{}");
					mapRet.put("root", mapRootJson);
				}else{

					// 遍历List 得到树形结构
					int flag = -1;
					String TZ_COLU_LEVEL = "";
					Map<String, Object> mapData = null;
	
					// 循环得到 根节点
					for (Object objData : listData) {
						mapData = (Map<String, Object>) objData;
						TZ_COLU_LEVEL = String.valueOf(mapData.get("TZ_COLU_LEVEL"));
						if (TZ_COLU_LEVEL.equals("0")) {
							flag = 0;
							break;
						}
					}
	
					if (flag == 0) {
						String coluId = String.valueOf(mapData.get("TZ_COLU_ID"));
	
						List<Map<String, Object>> listChildren = this.getMenuList(coluId, listData);
	
						Map<String, Object> mapRootJson = new HashMap<String, Object>();
	
						// text : root.text,
						// //nodeId : root.nodeId,
						// id : root.coluId,
						// coluState : root.coluState,
						// coluPath : root.coluPath,
						// coluTempletId : root.coluTempletId,
						// contentTypeId : root.contentTypeId,
						// coluTempletName : root.coluTempletName,
						// contentTypeName : root.contentTypeName,
						// NodeType : root.NodeType,
						// operateNode : root.operateNode,
						// rootNode : config.coluId,
						// expanded : root.expanded,
						// children : me.getChartNavItems(items)
						//System.out.println("id=" + coluId);
	
						mapRootJson.put("id", coluId);
						mapRootJson.put("nodeId", coluId);
						mapRootJson.put("siteId", siteId);
						mapRootJson.put("coluUrl", mapData.get("TZ_OUT_URL").toString());
						mapRootJson.put("text", mapData.get("TZ_COLU_NAME").toString());
						mapRootJson.put("coluState", mapData.get("TZ_COLU_STATE").toString());
						mapRootJson.put("coluType", mapData.get("TZ_COLU_TYPE").toString());
						mapRootJson.put("coluPath", mapData.get("TZ_COLU_PATH").toString());
						mapRootJson.put("coluTempletId", mapData.get("TZ_CONT_TEMP").toString());
						mapRootJson.put("contentTypeId", mapData.get("TZ_ART_TYPE_ID").toString());
						mapRootJson.put("coluTempletName", mapData.get("TZ_TEMP_NAME").toString());
						mapRootJson.put("contentTypeName", mapData.get("TZ_ART_TYPE_NAME").toString());
	
						if (listData.size() > 1) {
							mapRootJson.put("leaf", false); // 有子节点
						} else {
							mapRootJson.put("leaf", true); // 没有子节点
						}
						mapRootJson.put("NodeType", "");
						mapRootJson.put("operateNode", "");
						mapRootJson.put("rootNode", "");
						mapRootJson.put("expanded", "true");
						mapRootJson.put("children", listChildren);
						mapRet.put("root", mapRootJson);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "根节点不存在";
						return strRet;
					}
				}

				strRet = jacksonUtil.Map2json(mapRet);

				errMsg[0] = "0";

			} else {
				errMsg[0] = "1";
				errMsg[1] = "参数错误";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	

	/**
	 * 判断该节点是否存在子节点
	 * 
	 * @param fcoluId
	 * @param listData
	 * @return false 不存在 true 存在
	 */
	private boolean isLeaf(String fcoluId, List<?> listData) {
		// boolean isLeaf = false;
		try {
			Map<String, Object> mapNode = null;
			String TZ_F_COLU_ID = "";
			for (Object objNode : listData) {
				mapNode = (Map<String, Object>) objNode;
				TZ_F_COLU_ID = mapNode.get("TZ_F_COLU_ID").toString();
				if (TZ_F_COLU_ID.equals(fcoluId)) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}
	
	/**
	 * 获取菜单的下级菜单列表
	 * 
	 * @param strMenuID
	 * @param baseLanguage
	 * @param targetLanguage
	 * @return List<Map<String, Object>>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMenuList(String FcoluId, List<Map<String, Object>> listData) {

		List<Map<String, Object>> listRet = new ArrayList<Map<String, Object>>();

		try {
			Map<String, Object> mapNode = null;
			String TZ_F_COLU_ID = "";
			boolean isLeaf = false;
			Map<String, Object> mapNodeJson = null;
			String coluId = "";
			for (Object objNode : listData) {
				mapNode = (Map<String, Object>) objNode;
				TZ_F_COLU_ID = mapNode.get("TZ_F_COLU_ID").toString();

				if (TZ_F_COLU_ID.equals(FcoluId)) {

					coluId = String.valueOf(mapNode.get("TZ_COLU_ID"));
					mapNodeJson = new HashMap<String, Object>();
					mapNodeJson.put("id", coluId);
					mapNodeJson.put("nodeId", coluId);
					mapNodeJson.put("text", mapNode.get("TZ_COLU_NAME").toString());
					mapNodeJson.put("coluState", mapNode.get("TZ_COLU_STATE").toString());
					mapNodeJson.put("coluPath", mapNode.get("TZ_COLU_PATH").toString());
					mapNodeJson.put("coluTempletId", mapNode.get("TZ_CONT_TEMP").toString());
					mapNodeJson.put("contentTypeId", mapNode.get("TZ_ART_TYPE_ID").toString());
					mapNodeJson.put("coluTempletName", mapNode.get("TZ_TEMP_NAME").toString());
					mapNodeJson.put("contentTypeName", mapNode.get("TZ_ART_TYPE_NAME").toString());
					mapNodeJson.put("coluUrl", mapNode.get("TZ_OUT_URL").toString());
					mapNodeJson.put("coluType", mapNode.get("TZ_COLU_TYPE").toString());

					mapNodeJson.put("NodeType", "");
					mapNodeJson.put("operateNode", "");
					mapNodeJson.put("rootNode", "");

					isLeaf = this.isLeaf(coluId, listData);
					if (isLeaf) {
						mapNodeJson.put("leaf", false);
						mapNodeJson.put("expanded", true);
						mapNodeJson.put("children", this.getMenuList(coluId, listData));
					} else {
						mapNodeJson.put("leaf", true);
					}

					listRet.add(mapNodeJson);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listRet;
	}
	
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 提交信息
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				String siteId = jacksonUtil.getString("siteId");
				String columnId = jacksonUtil.getString("columnId");
				String articleId = jacksonUtil.getString("articleId");
				String maxZdSeqSQL = "SELECT TZ_MAX_ZD_SEQ FROM PS_TZ_LM_NR_GL_T WHERE TZ_SITE_ID=? AND TZ_COLU_ID=? AND TZ_ART_ID=?";
				int maxZdSEQ = 0;
				try {
					maxZdSEQ = jdbcTemplate.queryForObject(maxZdSeqSQL, new Object[] { siteId, columnId, articleId },
							"Integer");
				} catch (Exception e) {
					maxZdSEQ = 0;
				}

				String deleteSQL = "DELETE from PS_TZ_LM_NR_GL_T WHERE TZ_SITE_ID=? AND TZ_COLU_ID=? AND TZ_ART_ID=?";
				int success = jdbcTemplate.update(deleteSQL, new Object[] { siteId, columnId, articleId });
				if (success > 0 && maxZdSEQ > 0) {
					String updateMaxZdSeqSQL = "UPDATE PS_TZ_LM_NR_GL_T SET TZ_MAX_ZD_SEQ = TZ_MAX_ZD_SEQ - 1 WHERE TZ_SITE_ID=? AND TZ_COLU_ID=? AND TZ_MAX_ZD_SEQ>?";
					jdbcTemplate.update(updateMaxZdSeqSQL, new Object[] { siteId, columnId, maxZdSEQ });
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	/* 新增站点内容文章信息 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				// 点击事件类型：T置顶； P发布；
				String clickTyp = jacksonUtil.getString("ClickTyp");
				// 提交的数据;
				Map<String, Object> dataMap = jacksonUtil.getMap("data");
				String siteId = (String) dataMap.get("siteId");
				String columnId = (String) dataMap.get("columnId");
				String articleId = (String) dataMap.get("articleId");

				if (siteId != null && !"".equals(siteId) && columnId != null && !"".equals(columnId)
						&& articleId != null && !"".equals(articleId)) {
					if ("T".equals(clickTyp)) {
						String topOrUndo = (String) dataMap.get("topOrUndo");
						String maxSQL = "" ,updateSQL = "";
						int maxZdSeq = 0;
						
						if("TOP".equals(topOrUndo)){
							maxSQL = "SELECT MAX(TZ_MAX_ZD_SEQ) FROM PS_TZ_LM_NR_GL_T WHERE TZ_SITE_ID=? AND TZ_COLU_ID=? AND TZ_ART_ID<>?";
							try{
								maxZdSeq = jdbcTemplate.queryForObject(maxSQL, new Object[]{siteId,columnId,articleId},"Integer");
							}catch(Exception e){
								maxZdSeq = 0;
							}
							PsTzLmNrGlTWithBLOBs psTzLmNrGlTWithBLOBs = new PsTzLmNrGlTWithBLOBs();
							psTzLmNrGlTWithBLOBs.setTzSiteId(siteId);
							psTzLmNrGlTWithBLOBs.setTzColuId(columnId);
							psTzLmNrGlTWithBLOBs.setTzArtId(articleId);
							psTzLmNrGlTWithBLOBs.setTzMaxZdSeq(maxZdSeq + 1);
							psTzLmNrGlTWithBLOBs.setTzLastmantDttm(new Date());
							psTzLmNrGlTWithBLOBs.setTzLastmantOprid(oprid);
							psTzLmNrGlTMapper.updateByPrimaryKeySelective(psTzLmNrGlTWithBLOBs);
						}else{
							if("0".equals(topOrUndo)){
								maxSQL = "SELECT TZ_MAX_ZD_SEQ FROM PS_TZ_LM_NR_GL_T WHERE TZ_SITE_ID=? AND TZ_COLU_ID=? AND TZ_ART_ID=?"; 
								try{
									maxZdSeq = jdbcTemplate.queryForObject(maxSQL, new Object[]{siteId,columnId,articleId},"Integer");
								}catch(Exception e){
									maxZdSeq = 0;
								}
								
								if(maxZdSeq > 0){
									updateSQL = "UPDATE PS_TZ_LM_NR_GL_T SET TZ_MAX_ZD_SEQ = TZ_MAX_ZD_SEQ - 1 WHERE TZ_SITE_ID=? AND TZ_COLU_ID=? AND TZ_MAX_ZD_SEQ>?";
									jdbcTemplate.update(updateSQL,new Object[]{siteId,columnId,maxZdSeq});
									
									PsTzLmNrGlTWithBLOBs psTzLmNrGlTWithBLOBs = new PsTzLmNrGlTWithBLOBs();
									psTzLmNrGlTWithBLOBs.setTzSiteId(siteId);
									psTzLmNrGlTWithBLOBs.setTzColuId(columnId);
									psTzLmNrGlTWithBLOBs.setTzArtId(articleId);
									psTzLmNrGlTWithBLOBs.setTzMaxZdSeq(0);
									psTzLmNrGlTWithBLOBs.setTzLastmantDttm(new Date());
									psTzLmNrGlTWithBLOBs.setTzLastmantOprid(oprid);
									psTzLmNrGlTMapper.updateByPrimaryKeySelective(psTzLmNrGlTWithBLOBs);
								}
							}
						}
					}

					if ("P".equals(clickTyp)) {
						String releaseOrUndo = (String) dataMap.get("releaseOrUndo");
						
						/*获取站点类型*/
						String sqlGetSiteType = "select TZ_SITEI_TYPE from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
						String strSiteType = jdbcTemplate.queryForObject(sqlGetSiteType,  new Object[] { siteId }, "String");
						//获取静态路径地址
						String strBasePath = "";
						String getBasePathSql = "SELECT TZ_COLU_PATH FROM PS_TZ_SITEI_COLU_T WHERE TZ_SITEI_ID = ? AND TZ_COLU_LEVEL = '0' LIMIT 1";
						strBasePath = jdbcTemplate.queryForObject(getBasePathSql, new Object[]{siteId},"String");
						String strColuPath = "";
						String getColuPathSql = "SELECT TZ_COLU_PATH FROM PS_TZ_SITEI_COLU_T WHERE TZ_SITEI_ID = ? AND TZ_COLU_ID = ? LIMIT 1";
						strColuPath = jdbcTemplate.queryForObject(getColuPathSql, new Object[]{siteId,columnId},"String");
						
						String strFileName = "";
						String strFilePath = "";
						String strFilePathAccess = "";
						
						if(strBasePath != null && !"".equals(strBasePath)){
							if (!strBasePath.startsWith("/")) {
								strBasePath = "/" + strBasePath;
							}
							if (strBasePath.endsWith("/")) {
								strBasePath = strBasePath.substring(0, strBasePath.length() - 1);
							}
						}
						
						if(strColuPath != null && !"".equals(strColuPath)){
							if (!strColuPath.startsWith("/")) {
								strColuPath = "/" + strColuPath;
							}
							if (strColuPath.endsWith("/")) {
								strColuPath = strColuPath.substring(0, strColuPath.length() - 1);
							}
						}
						
						String dir = getSysHardCodeVal.getWebsiteEnrollPath();
						dir = request.getServletContext().getRealPath(dir);
						strFilePath = dir + strBasePath + strColuPath;
						strFilePathAccess = strBasePath + strColuPath;
								
						String rootparth = "http://"+ request.getServerName()+":"+request.getServerPort()+ request.getContextPath();
						
						PsTzLmNrGlTKey psTzLmNrGlTKey = new PsTzLmNrGlTKey();
						psTzLmNrGlTKey.setTzSiteId(siteId);
						psTzLmNrGlTKey.setTzColuId(columnId);
						psTzLmNrGlTKey.setTzArtId(articleId);
						PsTzLmNrGlTWithBLOBs psTzLmNrGlT = psTzLmNrGlTMapper.selectByPrimaryKey(psTzLmNrGlTKey);
						if (psTzLmNrGlT != null) {
							Date artNewsDt = psTzLmNrGlT.getTzArtNewsDt();
							String strStaticName = psTzLmNrGlT.getTzStaticName();
							String strAutoStaticName = psTzLmNrGlT.getTzStaticAotoName();
							PsTzLmNrGlTWithBLOBs psTzLmNrGlTWithBLOBs = new PsTzLmNrGlTWithBLOBs();
							psTzLmNrGlTWithBLOBs.setTzSiteId(siteId);
							psTzLmNrGlTWithBLOBs.setTzColuId(columnId);
							psTzLmNrGlTWithBLOBs.setTzArtId(articleId);
							psTzLmNrGlTWithBLOBs.setTzArtPubState(releaseOrUndo);
							//解析的模板内容;
							String contentHtml = artContentHtml.getContentHtml(siteId, columnId, articleId);
							int success = 0;
							psTzLmNrGlTWithBLOBs.setTzArtHtml(contentHtml);
							if ("Y".equals(releaseOrUndo)) {
								// 如果发布但没有发布时间，则赋值当前时间为发布时间;
								if (artNewsDt == null) {
									psTzLmNrGlTWithBLOBs.setTzArtNewsDt(new Date());
								}
								psTzLmNrGlTWithBLOBs.setTzArtConentScr(contentHtml);
								//发布时，判断是否是外网网站，如果是，则静态化
								//如果是外网站点和手机站点，则静态话
								if("A".equals(strSiteType) || "B".equals(strSiteType)){
									//静态化
									if(strStaticName!=null && !"".equals(strStaticName)){
										strFileName = strStaticName + ".html";
									}else{
										if(strAutoStaticName!=null && !"".equals(strAutoStaticName)){
											strFileName = strAutoStaticName + ".html";
										}else{
											strAutoStaticName = String.valueOf(getSeqNum.getSeqNum("TZ_LM_NR_GL_T", "TZ_STATIC_A_NAME"));
											psTzLmNrGlTWithBLOBs.setTzStaticAotoName(strAutoStaticName);
										}
										strFileName = strAutoStaticName + ".html";
									}
									//System.out.println("文件路径：" + strFilePath);
									//fileManageServiceImpl.UpdateFile(strFilePath, strFileName,contentHtml.getBytes());
									artContentHtml.staticFile(contentHtml,strFilePath, strFileName);
									
									String publishUrl = rootparth + strFilePathAccess + "/" + strFileName;
									psTzLmNrGlTWithBLOBs.setTzStaticArtUrl(publishUrl);
									psTzLmNrGlTWithBLOBs.setTzLastmantDttm(new Date());
									psTzLmNrGlTWithBLOBs.setTzLastmantOprid(oprid);
									artContentHtml.staticSiteInfoByChannel(siteId, columnId);
								}else{
									psTzLmNrGlTWithBLOBs.setTzLastmantDttm(new Date());
									psTzLmNrGlTWithBLOBs.setTzLastmantOprid(oprid);
									success = psTzLmNrGlTMapper.updateByPrimaryKeySelective(psTzLmNrGlTWithBLOBs);
								}
							} else {
								psTzLmNrGlTWithBLOBs.setTzArtConentScr("");
								//删除静态化文件
								if(strStaticName!=null && !"".equals(strStaticName)){
									
									fileManageServiceImpl.DeleteFile(strFilePath, strStaticName + ".html");
								}
								if(strAutoStaticName!=null && !"".equals(strAutoStaticName)){
									
									fileManageServiceImpl.DeleteFile(strFilePath, strAutoStaticName + ".html");
								}
								psTzLmNrGlTWithBLOBs.setTzLastmantDttm(new Date());
								psTzLmNrGlTWithBLOBs.setTzLastmantOprid(oprid);
								success = psTzLmNrGlTMapper.updateByPrimaryKeySelective(psTzLmNrGlTWithBLOBs);
								artContentHtml.staticSiteInfoByChannel(siteId, columnId);
							}
							if (success <= 0){
								errMsg[0] = "1";
								errMsg[1] = "保存数据出错，未找到对应的数据";
							}
						}
					}
				}else{
		        	errMsg[0] = "1";
					errMsg[1] = "保存数据出错，未找到对应的数据";
		        }
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	/* 新增站点内容文章信息 */
	@Override
	public String tzOther(String oprType,String strParams, String[] errMsg) {
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		JacksonUtil jacksonUtil = new JacksonUtil();
		System.out.println("操作类型"+oprType);
		System.out.println(strParams);
		
		try {
			int num = 0;
			// 发布的站点;
			String siteId = "";
			// 发布的栏目
			String coluId = "";
			// 发布的内容id;
			String artId = "";
			//站点类型
			String strSiteType = "";
			
			jacksonUtil.json2Map(strParams);

			// 点击事件类型：T置顶； P发布；
			ArrayList<Map<String, Object>> copyData = (ArrayList<Map<String, Object>>) jacksonUtil.getList("copy");
			for(int i = 0;i<copyData.size();i++){
				   
				   Map<String, Object> mapCopyData = copyData.get(i);
				   String strChannelIdCopyTo = (String) mapCopyData.get("channelId");
				   String strChannelIdCopyFrom = "";
				   String strArtIdCopyFrom = "";
				   String strSiteIdCopyFrom = "";
				   System.out.println(strChannelIdCopyTo);
				   Map<String, Object> artInfoCopyFrom = (Map<String, Object>) mapCopyData.get("data");
				   if(artInfoCopyFrom.containsKey("articleId") && artInfoCopyFrom.containsKey("columnId")
						   && artInfoCopyFrom.containsKey("siteId")){
					   strArtIdCopyFrom = (String) artInfoCopyFrom.get("articleId");
					   strChannelIdCopyFrom = (String) artInfoCopyFrom.get("columnId");
					   strSiteIdCopyFrom = (String) artInfoCopyFrom.get("siteId");
					   if(strArtIdCopyFrom!=null && !"".equals(strArtIdCopyFrom)
							 && strChannelIdCopyFrom!=null && !"".equals(strChannelIdCopyFrom)
							 && strSiteIdCopyFrom!=null && !"".equals(strSiteIdCopyFrom)){
						   /*复制内容*/
					   }else{
						   
					   }
				   }else{
					   
				   }
				   System.out.println(artInfoCopyFrom.get("articleId"));
				   System.out.println(artInfoCopyFrom.get("siteId"));
				   System.out.println(artInfoCopyFrom.get("columnId"));
			}
			// 提交的数据;
			
		}catch (Exception e){
			e.printStackTrace();
		}
			
		return strRet;
	}
	/**/
	private boolean copyArtToOtherChannel(String strArtIdCopyFrom,String strChannelIdCopyFrom,String strSiteIdCopyFrom,String strChannelIdCopyTo){
		boolean b_retrun = true;
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		
		/*网站上传文件的路径*/
		String rootPath = getSysHardCodeVal.getWebsiteFileUploadPath();
		
		// 站点表;
		PsTzSiteiDefnTWithBLOBs psTzSiteiDefnT = psTzSiteiDefnTMapper.selectByPrimaryKey(strSiteIdCopyFrom);
		String imagePath = psTzSiteiDefnT.getTzImgStor();
		String attrPath = psTzSiteiDefnT.getTzAttsStor();
		
		/*得到复制到的栏目对应的站点编号*/
		// 获取机构对应的站点；
		String siteSQL = " SELECT TZ_SITEI_ID FROM PS_TZ_SITEI_COLU_T WHERE TZ_COLU_ID=?";
		String strSiteIdCopyTo = jdbcTemplate.queryForObject(siteSQL, new Object[] { strChannelIdCopyTo }, "String");
		
		// 文章表;
		PsTzArtRecTblWithBLOBs psTzArtRecTblFrom = psTzArtRecTblMapper.selectByPrimaryKey(strArtIdCopyFrom);
		// 内容站点关联表;
		PsTzLmNrGlTKey psTzLmNrGlTKey = new PsTzLmNrGlTKey();
		psTzLmNrGlTKey.setTzSiteId(strSiteIdCopyFrom);
		psTzLmNrGlTKey.setTzColuId(strChannelIdCopyFrom);
		psTzLmNrGlTKey.setTzArtId(strArtIdCopyFrom);
		PsTzLmNrGlTWithBLOBs psTzLmNrGlTFrom = psTzLmNrGlTMapper.selectByPrimaryKey(psTzLmNrGlTKey);
		if(psTzArtRecTblFrom!=null && psTzLmNrGlTFrom !=null 
				&& !"".equals(strChannelIdCopyTo) && !"".equals(strSiteIdCopyTo)){
			/*复制文章信息表*/
			// 内容表;
			String titleImageSysfilenaFrom = psTzArtRecTblFrom.getTzAttachsysfilena();
			if(titleImageSysfilenaFrom!=null && !"".equals(titleImageSysfilenaFrom)){
				String titleImageSysfileSuffix = titleImageSysfilenaFrom.substring(titleImageSysfilenaFrom.lastIndexOf(".") + 1);
				String titleImageSysfilenaNew = (new StringBuilder(String.valueOf(getNowTime())))
						.append(".").append(titleImageSysfileSuffix).toString();
				
			}
			
			String strArtIdCopyTo = String.valueOf(getSeqNum.getSeqNum("TZ_ART_REC_TBL", "TZ_ART_ID"));
			PsTzArtRecTblWithBLOBs PsTzArtRecTbl = new PsTzArtRecTblWithBLOBs();
			PsTzArtRecTbl.setTzArtId(strArtIdCopyTo);
			PsTzArtRecTbl.setTzArtTitle(psTzArtRecTblFrom.getTzArtTitle());
			PsTzArtRecTbl.setTzArtShorttitle(psTzArtRecTblFrom.getTzArtShorttitle());
			PsTzArtRecTbl.setTzSubhead(psTzArtRecTblFrom.getTzSubhead());
			PsTzArtRecTbl.setTzAbout(psTzArtRecTblFrom.getTzAbout());
			PsTzArtRecTbl.setTzMetadesc(psTzArtRecTblFrom.getTzMetadesc());
			PsTzArtRecTbl.setTzMetakeys(psTzArtRecTblFrom.getTzMetakeys());
			PsTzArtRecTbl.setTzTxt1(psTzArtRecTblFrom.getTzTxt1());
			PsTzArtRecTbl.setTzTxt2(psTzArtRecTblFrom.getTzTxt2());
			PsTzArtRecTbl.setTzTxt3(psTzArtRecTblFrom.getTzTxt3());
			PsTzArtRecTbl.setTzTxt4(psTzArtRecTblFrom.getTzTxt4());
			PsTzArtRecTbl.setTzLong1(psTzArtRecTblFrom.getTzLong1());
			PsTzArtRecTbl.setTzLong2(psTzArtRecTblFrom.getTzLong2());
			PsTzArtRecTbl.setTzLong3(psTzArtRecTblFrom.getTzLong3());
			PsTzArtRecTbl.setTzDate1(psTzArtRecTblFrom.getTzDate1());
			PsTzArtRecTbl.setTzDate2(psTzArtRecTblFrom.getTzDate2());
			PsTzArtRecTbl.setTzArtTitleStyle(psTzArtRecTblFrom.getTzArtTitleStyle());
			PsTzArtRecTbl.setTzArtConent(psTzArtRecTblFrom.getTzArtConent());
			PsTzArtRecTbl.setTzArtName(psTzArtRecTblFrom.getTzArtName());
			PsTzArtRecTbl.setTzProjectLimit(psTzArtRecTblFrom.getTzProjectLimit());
			PsTzArtRecTbl.setTzArtType1(psTzArtRecTblFrom.getTzArtType1());
			PsTzArtRecTbl.setTzOutArtUrl(psTzArtRecTblFrom.getTzOutArtUrl());
			PsTzArtRecTbl.setTzImageTitle(psTzArtRecTblFrom.getTzImageTitle());
			PsTzArtRecTbl.setTzImageDesc(psTzArtRecTblFrom.getTzImageDesc());
			PsTzArtRecTbl.setTzAttachsysfilena(psTzArtRecTblFrom.getTzAttachsysfilena());
			PsTzArtRecTbl.setRowAddedDttm(new Date());
			PsTzArtRecTbl.setRowAddedOprid(oprid);
			PsTzArtRecTbl.setRowLastmantDttm(new Date());
			PsTzArtRecTbl.setRowLastmantOprid(oprid);
			psTzArtRecTblMapper.insertSelective(PsTzArtRecTbl);
			
			// 内容站点关联表;
			PsTzLmNrGlTWithBLOBs psTzLmNrGlT = new PsTzLmNrGlTWithBLOBs();
			psTzLmNrGlT.setTzSiteId(strSiteIdCopyTo);
			psTzLmNrGlT.setTzColuId(strChannelIdCopyTo);
			psTzLmNrGlT.setTzArtId(strArtIdCopyTo);
			psTzLmNrGlT.setTzFbz(psTzLmNrGlTFrom.getTzFbz());
			psTzLmNrGlT.setTzBltDept(psTzLmNrGlTFrom.getTzBltDept());
			psTzLmNrGlT.setTzArtPubState("N");
			psTzLmNrGlT.setTzStaticArtUrl("");
			psTzLmNrGlT.setTzArtSeq(Integer.parseInt(strArtIdCopyTo));
			psTzLmNrGlT.setTzLastmantDttm(new Date());
			psTzLmNrGlT.setTzLastmantOprid(oprid);
			psTzLmNrGlT.setTzOrginArtChnl(strChannelIdCopyFrom);
			psTzLmNrGlT.setTzOrginArtId(strArtIdCopyFrom);
			//psTzLmNrGlT.setTzArtNewsDt(psTzLmNrGlTFrom.getTzArtNewsDt());
			psTzLmNrGlT.setTzStaticName("");
			psTzLmNrGlTMapper.insertSelective(psTzLmNrGlT);
			
			//图片集
			jdbcTemplate.update("delete from PS_TZ_ART_PIC_T where TZ_ART_ID=?",new Object[]{strArtIdCopyTo});
			
			//图片集
			jdbcTemplate.update("delete from PS_TZ_ART_PRJ_T where TZ_ART_ID=?",new Object[]{strArtIdCopyTo});
			
			//图片集
			jdbcTemplate.update("delete from PS_TZ_ART_PRJ_T where TZ_ART_ID=?",new Object[]{strArtIdCopyTo});
		}
		return b_retrun;
	}
	
	protected String getDateNow() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(1);
		int month = cal.get(2) + 1;
		int day = cal.get(5);
		return (new StringBuilder()).append(year).append(month).append(day).toString();
	}

	protected String getNowTime() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(1);
		int month = cal.get(2) + 1;
		int day = cal.get(5);
		int hour = cal.get(10);
		int minute = cal.get(12);
		int second = cal.get(13);
		int mi = cal.get(14);
		long num = cal.getTimeInMillis();
		int rand = (int) (Math.random() * 899999 + 100000);
		return (new StringBuilder()).append(year).append(month).append(day).append(hour).append(minute).append(second)
				.append(mi).append(num).append("_").append(rand).toString();
	}
}
