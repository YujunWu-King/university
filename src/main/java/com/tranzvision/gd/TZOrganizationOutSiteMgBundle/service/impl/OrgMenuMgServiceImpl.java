package com.tranzvision.gd.TZOrganizationOutSiteMgBundle.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FileManageServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiDefnTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiMenuTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiDefnTWithBLOBs;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiMenuT;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiMenuTKey;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzArtFjjTMapper;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzArtTitimgTMapper;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzArtTpjTMapper;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtFjjT;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtRecTblWithBLOBs;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtTitimgT;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtTpjT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.ResizeImageUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.cms.CmsBean;
import com.tranzvision.gd.util.cms.CmsUtils;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * @author caoy
 * @version 创建时间：2016年8月26日 下午3:31:11 类说明 菜单管理
 */
@Service("com.tranzvision.gd.TZOrganizationOutSiteMgBundle.service.impl.OrgMenuMgServiceImpl")
public class OrgMenuMgServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;
	// 新增代码
	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private PsTzSiteiDefnTMapper psTzSiteiDefnTMapper;
	//
	@Autowired
	private TZGDObject tzSQLObject;

	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private PsTzSiteiMenuTMapper psTzSiteiMenuTMapper;

	@Autowired
	private HttpServletRequest request;

	/*
	 * @Autowired private PsTzSiteiMenuTKey psTzSiteiMenuTKey;
	 */

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private FileManageServiceImpl fileManageServiceImpl;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	// 新增代码
	@Autowired
	private ResizeImageUtil resizeImageUtil;

	@Autowired
	private PsTzArtTitimgTMapper psTzArtTitimgTMapper;

	@Autowired
	private PsTzArtFjjTMapper psTzArtFjjTMapper;

	@Autowired
	private PsTzArtTpjTMapper psTzArtTpjTMapper;

	//

	/* 查询列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("total", 0);
		ArrayList<Map<String, Object>> arraylist = new ArrayList<Map<String, Object>>();
		returnJsonMap.put("root", arraylist);
		JacksonUtil jacksonUtil = new JacksonUtil();

		jacksonUtil.json2Map(comParams);

		String menuId = String.valueOf(jacksonUtil.getString("menuId"));

		System.out.println("menuId:" + menuId);
		try {

			String totalSQL = "SELECT COUNT(1) FROM PS_TZ_SITEI_MENU_T where  TZ_F_MENU_ID = ?";
			int total = sqlQuery.queryForObject(totalSQL, new Object[] { menuId }, "Integer");
			String sql = "";
			List<Map<String, Object>> list = null;

			if (numLimit > 0) {

				sql = "SELECT A.TZ_MENU_ID,A.TZ_MENU_NAME,B.TZ_ZHZ_DMS TZ_MENU_TYPE,A.TZ_MENU_XH,A.TZ_MENU_SHOW TZ_MENU_SHOW FROM PS_TZ_SITEI_MENU_T A left join (SELECT TZ_ZHZ_ID, TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_ZDCD_LX' AND TZ_EFF_STATUS='A') B on A.TZ_MENU_TYPE = B.TZ_ZHZ_ID WHERE  A.TZ_F_MENU_ID = ? limit ?,?";
				list = sqlQuery.queryForList(sql, new Object[] { menuId, numStart, numLimit });
			} else {
				sql = "SELECT A.TZ_MENU_ID,A.TZ_MENU_NAME,B.TZ_ZHZ_DMS TZ_MENU_TYPE,A.TZ_MENU_XH,A.TZ_MENU_SHOW TZ_MENU_SHOW FROM PS_TZ_SITEI_MENU_T A left join (SELECT TZ_ZHZ_ID, TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_ZDCD_LX' AND TZ_EFF_STATUS='A') B on A.TZ_MENU_TYPE = B.TZ_ZHZ_ID WHERE  A.TZ_F_MENU_ID = ?";
				list = sqlQuery.queryForList(sql, new Object[] { menuId });
			}
			// System.out.println("sql:" + sql);
			if (list != null) {
				// System.out.println("list:" + list.size());
				Map<String, Object> jsonMap = null;
				for (int i = 0; i < list.size(); i++) {
					jsonMap = new HashMap<String, Object>();
					jsonMap.put("menuId", list.get(i).get("TZ_MENU_ID"));
					jsonMap.put("menuName", list.get(i).get("TZ_MENU_NAME"));
					jsonMap.put("menuType", list.get(i).get("TZ_MENU_TYPE"));
					jsonMap.put("menuXH", list.get(i).get("TZ_MENU_XH"));
					jsonMap.put("menuShow", list.get(i).get("TZ_MENU_SHOW"));
					if (list.get(i).get("TZ_MENU_SHOW").toString().equals("Y"))
						jsonMap.put("menuShow", true);
					else
						jsonMap.put("menuShow", false);
					arraylist.add(jsonMap);
				}
				returnJsonMap.replace("total", total);
				returnJsonMap.replace("root", arraylist);
			}

		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	@Override
	public String tzGetHtmlContent(String strParams) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		String desc = "";
		String minSysFile = "";
		String newSysFile = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("attachmentType") && jacksonUtil.containsKey("data")) {
				String attachmentType = jacksonUtil.getString("attachmentType");

				if ("IMG".equals(attachmentType)) {
					Map<String, Object> dataMap = jacksonUtil.getMap("data");
					if (dataMap != null) {
						String accessPath = (String) dataMap.get("accessPath");
						// 根据相对路径得到物理路径;
						String path = request.getServletContext().getRealPath(accessPath);
						String sysFileName1 = (String) dataMap.get("sysFileName");

						/* 压缩图片 */
						String separator = File.separator;

						String flg = "";
						if ((path.lastIndexOf(separator) + 1) == path.length()) {
							flg = resizeImageUtil.resize(path + sysFileName1, path, "MINI_" + sysFileName1, 100);
						} else {
							flg = resizeImageUtil.resize(path + separator + sysFileName1, path, "MINI_" + sysFileName1,
									100);
						}
						if ("Y".equals(flg)) {
							minSysFile = "MINI_" + sysFileName1;
						} else {
							minSysFile = sysFileName1;
						}
						if ((path.lastIndexOf(separator) + 1) == path.length()) {
							flg = resizeImageUtil.resize(path + sysFileName1, path, "NEW_" + sysFileName1, 1000);
						} else {
							flg = resizeImageUtil.resize(path + separator + sysFileName1, path, "NEW_" + sysFileName1,
									1000);
						}
						if ("Y".equals(flg)) {
							newSysFile = "NEW_" + sysFileName1;
						} else {
							newSysFile = sysFileName1;
						}

						PsTzArtTitimgT psTzArtTitimgT = new PsTzArtTitimgT();
						psTzArtTitimgT.setTzAttachsysfilena(sysFileName1);
						psTzArtTitimgT.setTzAttachfileName((String) dataMap.get("filename"));
						psTzArtTitimgT.setTzAttPUrl(path);
						psTzArtTitimgT.setTzAttAUrl(accessPath);
						psTzArtTitimgT.setTzYsAttachsysnam(newSysFile);
						psTzArtTitimgT.setTzSlAttachsysnam(minSysFile);

						psTzArtTitimgTMapper.insert(psTzArtTitimgT);
					}
				}

				if ("ATTACHMENT".equals(attachmentType)) {
					Map<String, Object> dataMap = jacksonUtil.getMap("data");
					if (dataMap != null) {
						String accessPath = (String) dataMap.get("accessPath");
						// 根据相对路径得到物理路径;
						String path = request.getServletContext().getRealPath(accessPath);
						PsTzArtFjjT psTzArtFjjT = new PsTzArtFjjT();
						psTzArtFjjT.setTzAttachsysfilena((String) dataMap.get("sysFileName"));
						psTzArtFjjT.setTzAttachfileName((String) dataMap.get("filename"));
						psTzArtFjjT.setTzAttPUrl(path);
						psTzArtFjjT.setTzAttAUrl(accessPath);
						psTzArtFjjTMapper.insert(psTzArtFjjT);
					}
				}

				if ("TPJ".equals(attachmentType)) {
					// int order = jacksonUtil.getInt("order");
					Map<String, Object> dataMap = jacksonUtil.getMap("data");
					if (dataMap != null) {
						String accessPath = (String) dataMap.get("accessPath");
						// 根据相对路径得到物理路径;
						String path = request.getServletContext().getRealPath(accessPath);
						String sysFileName = (String) dataMap.get("sysFileName");

						/* 压缩图片 */
						String separator = File.separator;
						String flg = "";
						if ((path.lastIndexOf(separator) + 1) == path.length()) {
							flg = resizeImageUtil.resize(path + sysFileName, path, "MINI_" + sysFileName, 100);
						} else {
							flg = resizeImageUtil.resize(path + separator + sysFileName, path, "MINI_" + sysFileName,
									100);
						}
						if ("Y".equals(flg)) {
							minSysFile = "MINI_" + sysFileName;
						} else {
							minSysFile = sysFileName;
						}

						if ((path.lastIndexOf(separator) + 1) == path.length()) {
							flg = resizeImageUtil.resize(path + sysFileName, path, "NEW_" + sysFileName, 1000);
						} else {
							flg = resizeImageUtil.resize(path + separator + sysFileName, path, "NEW_" + sysFileName,
									1000);
						}
						if ("Y".equals(flg)) {
							newSysFile = "NEW_" + sysFileName;
						} else {
							newSysFile = sysFileName;
						}

						PsTzArtTpjT psTzArtTpjT = new PsTzArtTpjT();
						psTzArtTpjT.setTzAttachsysfilena(sysFileName);
						psTzArtTpjT.setTzAttachfileName((String) dataMap.get("filename"));
						psTzArtTpjT.setTzAttPUrl(path);
						psTzArtTpjT.setTzAttAUrl(accessPath);
						psTzArtTpjT.setTzYsAttachsysnam(newSysFile);
						psTzArtTpjT.setTzSlAttachsysnam(minSysFile);

						psTzArtTpjTMapper.insert(psTzArtTpjT);
					}
				}
			} else {
				desc = "参数不正确";
			}
		} catch (Exception e) {
			e.printStackTrace();
			desc = e.toString();
		}

		if (!"".equals(desc)) {
			returnJsonMap.put("success", "1");
			returnJsonMap.put("message", desc);
			returnJsonMap.put("minPicSysFileName", minSysFile);
		} else {
			returnJsonMap.put("success", "0");
			returnJsonMap.put("message", "");
			returnJsonMap.put("minPicSysFileName", minSysFile);
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	/**
	 * 获取栏目节点信息
	 * 
	 * @param strParams
	 * @param errMsg
	 * @return String
	 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {

		// 返回值;
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();

		// 新增代码
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		// Map<String, Object> map = new HashMap<>();

		//
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("siteId")) {
				// 新增代码
				// String strArtId = jacksonUtil.getString("artId");

				//
				// 站点编号
				String siteId = jacksonUtil.getString("siteId");

				// 查询类型
				String typeFlag = jacksonUtil.getString("typeFlag");

				String sql;
				Map<String, Object> mapRet = new HashMap<String, Object>();
				if ("TREE".equals(typeFlag)) {

					// 一次性获取一整颗树的所有数据放入List
					sql = tzSQLObject.getSQLText("SQL.TZOutSiteMgBundle.TzSelectOutSiteMenuList");

					List<Map<String, Object>> listData = sqlQuery.queryForList(sql, new Object[] { siteId });

					if (null == listData || listData.size() <= 0) {
						errMsg[0] = "1";
						errMsg[1] = "树不存在";
						return strRet;
					}

					// 遍历List 得到树形结构
					int flag = -1;
					String TZ_MENU_LEVEL = "";
					Map<String, Object> mapData = null;

					// 循环得到 根节点
					for (Object objData : listData) {
						mapData = (Map<String, Object>) objData;
						TZ_MENU_LEVEL = String.valueOf(mapData.get("TZ_MENU_LEVEL"));
						if (TZ_MENU_LEVEL.equals("0")) {
							flag = 0;
							break;
						}
					}

					if (flag == 0) {

						//
						String acessUrl = "";
						// 标题图实际存放地址
						String titleImageUrl = "";
						String attachSysFile = mapData.get("TZ_ATTACHSYSFILENA").toString();
						// 站点表;
						PsTzSiteiDefnTWithBLOBs psTzSiteiDefnT = psTzSiteiDefnTMapper.selectByPrimaryKey(siteId);
						// 站点存放图片路径
						String saveImageAccessUrl = psTzSiteiDefnT.getTzImgStor();

						// System.out.println("-----------------------------");
						String menuId = String.valueOf(mapData.get("TZ_MENU_ID"));

						List<Map<String, Object>> listChildren = this.getMenuList(menuId, listData, saveImageAccessUrl,siteId);

						Map<String, Object> mapRootJson = new HashMap<String, Object>();

						System.out.println("id=" + menuId);

						mapRootJson.put("id", menuId);
						mapRootJson.put("nodeId", menuId);
						mapRootJson.put("siteId", siteId);
						mapRootJson.put("text", mapData.get("TZ_MENU_NAME").toString());
						mapRootJson.put("menuState", mapData.get("TZ_MENU_STATE").toString());
						mapRootJson.put("menuXH", mapData.get("TZ_MENU_XH").toString());
						mapRootJson.put("menuType", mapData.get("TZ_MENU_TYPE").toString());
						mapRootJson.put("menuPath", mapData.get("TZ_MENU_PATH").toString());
						mapRootJson.put("menuTempletId", mapData.get("TZ_TEMP_ID").toString());
						mapRootJson.put("menuTempletName", mapData.get("TZ_TEMP_NAME").toString());
						mapRootJson.put("menuPageName", mapData.get("TZ_PAGE_NAME").toString());
						mapRootJson.put("defaultPage", mapData.get("TZ_DEFAULT_PAGE").toString());
						mapRootJson.put("menuStyle", mapData.get("TZ_MENU_STYLE").toString());

						// 新增加对于标题图的处理
						mapRootJson.put("titleImageTitle", mapData.get("TZ_IMAGE_TITLE").toString());
						mapRootJson.put("titleImageDesc", mapData.get("TZ_IMAGE_DESC").toString());
						mapRootJson.put("titleImageUrl", attachSysFile);
						mapRootJson.put("saveImageAccessUrl", saveImageAccessUrl);
						mapRootJson.put("titleImageUrl", titleImageUrl);

						if (mapData.get("TZ_MENU_SHOW").toString().equals("Y"))
							mapRootJson.put("menuShow", true);
						else
							mapRootJson.put("menuShow", false);
						mapRootJson.put("isDefault", "");

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

	private String getTitleImageUrl(String TZ_ATTACHSYSFILENA) {
		String attachSysFile = TZ_ATTACHSYSFILENA;
		String acessUrl = null;
		String titleImageUrl = null;
		if (attachSysFile != null && !"".equals(attachSysFile)) {
			String arraurlSQL = "select TZ_ATT_A_URL from PS_TZ_ART_TITIMG_T where TZ_ATTACHSYSFILENA=?";
			acessUrl = jdbcTemplate.queryForObject(arraurlSQL, new Object[] { attachSysFile }, "String");
			/*
			 * System.out.println("---------------");
			 * System.out.println(acessUrl);
			 */

			if (acessUrl != null && !"".equals(acessUrl)) {
				if ((acessUrl.lastIndexOf("/") + 1) != acessUrl.length()) {
					titleImageUrl = acessUrl + "/" + attachSysFile;
				} else {
					titleImageUrl = acessUrl + attachSysFile;
				}
			}
		}
		return titleImageUrl;
	}

	/**
	 * 获取菜单的下级菜单列表
	 * 
	 * @param FmenuId
	 *            父菜单ID
	 * @param List<?>
	 *            listData
	 * @return List<Map<String, Object>>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMenuList(String FmenuId, List<Map<String, Object>> listData,
			String saveImageAccessUrl,String siteId) {

		List<Map<String, Object>> listRet = new ArrayList<Map<String, Object>>();

		try {
			Map<String, Object> mapNode = null;
			String TZ_F_MENU_ID = "";
			boolean isLeaf = false;
			boolean isDefault = false;
			Map<String, Object> mapNodeJson = null;
			String menuId = "";
			String menuType = "";
			for (Object objNode : listData) {
				mapNode = (Map<String, Object>) objNode;
				TZ_F_MENU_ID = mapNode.get("TZ_F_MENU_ID").toString();

				if (TZ_F_MENU_ID.equals(FmenuId)) {

					menuId = String.valueOf(mapNode.get("TZ_MENU_ID"));
					menuType = mapNode.get("TZ_MENU_TYPE").toString();
					mapNodeJson = new HashMap<String, Object>();
					mapNodeJson.put("id", menuId);
					mapNodeJson.put("siteId", siteId);
					mapNodeJson.put("nodeId", menuId);
					mapNodeJson.put("text", mapNode.get("TZ_MENU_NAME").toString());
					mapNodeJson.put("menuState", mapNode.get("TZ_MENU_STATE").toString());
					mapNodeJson.put("menuType", menuType);
					mapNodeJson.put("menuXH", mapNode.get("TZ_MENU_XH").toString());
					mapNodeJson.put("menuPath", mapNode.get("TZ_MENU_PATH").toString());
					mapNodeJson.put("menuTempletId", mapNode.get("TZ_TEMP_ID").toString());
					mapNodeJson.put("menuTempletName", mapNode.get("TZ_TEMP_NAME").toString());
					mapNodeJson.put("menuPageName", mapNode.get("TZ_PAGE_NAME").toString());
					mapNodeJson.put("defaultPage", mapNode.get("TZ_DEFAULT_PAGE").toString());
					mapNodeJson.put("menuStyle", mapNode.get("TZ_MENU_STYLE").toString());

					// 新增加对于标题图的处理
					mapNodeJson.put("titleImageTitle", mapNode.get("TZ_IMAGE_TITLE").toString());
					mapNodeJson.put("titleImageDesc", mapNode.get("TZ_IMAGE_DESC").toString());
					mapNodeJson.put("titleImageUrl", mapNode.get("TZ_ATTACHSYSFILENA").toString());
					mapNodeJson.put("saveImageAccessUrl", saveImageAccessUrl);
					/*
					 * System.out.println("[[[[[[[[[[[[[[[[[[");
					 * System.out.println(mapNode.get("TZ_ATTACHSYSFILENA").
					 * toString());
					 */
					mapNodeJson.put("titleImageUrl",
							this.getTitleImageUrl(mapNode.get("TZ_ATTACHSYSFILENA").toString()));

					if (mapNode.get("TZ_MENU_SHOW").equals("Y"))

						mapNodeJson.put("menuShow", true);
					else
						mapNodeJson.put("menuShow", false);

					// 查询默认主页 A:PAGE B:BOOK
					if (menuType.equals("A")) {
						isDefault = this.isDefault(menuId, listData);
						if (isDefault) {
							mapNodeJson.put("isDefault", "Y");
						} else {
							mapNodeJson.put("isDefault", "N");
						}
					}

					mapNodeJson.put("NodeType", "");
					mapNodeJson.put("operateNode", "");
					mapNodeJson.put("rootNode", "");

					isLeaf = this.isLeaf(menuId, listData);
					if (isLeaf) {
						mapNodeJson.put("leaf", false);
						mapNodeJson.put("expanded", true);
						mapNodeJson.put("children", this.getMenuList(menuId, listData, saveImageAccessUrl,siteId));
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

	/**
	 * 判断该节点是否存在子节点
	 * 
	 * @param fmenuId
	 * @param listData
	 * @return false 不存在 true 存在
	 */
	private boolean isLeaf(String fmenuId, List<?> listData) {
		// boolean isLeaf = false;
		try {
			Map<String, Object> mapNode = null;
			String TZ_F_MENU_ID = "";
			for (Object objNode : listData) {
				mapNode = (Map<String, Object>) objNode;
				TZ_F_MENU_ID = mapNode.get("TZ_F_MENU_ID").toString();
				if (TZ_F_MENU_ID.equals(fmenuId)) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 判断该节点是否是默认页面
	 * 
	 * @param fmenuId
	 * @param listData
	 * @return false 是默认页面 true 不是默认页面
	 */
	private boolean isDefault(String menuId, List<?> listData) {
		// boolean isLeaf = false;
		try {
			Map<String, Object> mapNode = null;
			String TZ_D_MENU_ID = "";
			for (Object objNode : listData) {
				mapNode = (Map<String, Object>) objNode;
				TZ_D_MENU_ID = mapNode.get("TZ_D_MENU_ID").toString();
				if (TZ_D_MENU_ID.equals(menuId)) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<Map<String, Object>> colseMenuShow(List<Map<String, Object>> sonList,
			List<Map<String, Object>> listData, String saveImageAccessUrl,String siteId) {
		for (int i = 0; i < sonList.size(); i++) {
			sonList.get(i).replace("TZ_MENU_SHOW", "N");
			System.out.println("menuId:" + sonList.get(i).get("id"));
			// sqlQuery.execute("update PS_TZ_SITEI_MENU_T set TZ_MENU_SHOW='N'
			// where TZ_MENU_ID='"+sonList.get(i).get("id")+"'");
			String sql = "update  PS_TZ_SITEI_MENU_T set TZ_MENU_SHOW='N' where TZ_MENU_ID =?";
			sqlQuery.update(sql, new Object[] { sonList.get(i).get("id") });
			List<Map<String, Object>> tempList = this.getMenuList(sonList.get(i).get("id").toString(), listData,
					saveImageAccessUrl,siteId);
			if (tempList != null)
				colseMenuShow(tempList, listData, saveImageAccessUrl,siteId);
		}
		return sonList;
	}

	/**
	 * 功能说明：插入节点信息
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzAdd(String[] actData, String[] errMsg) {

		String strRet = "{}";
		// 若参数为空，直接返回;
		if (actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// Date dateNow = new Date();
			int dataLength = actData.length;
			String sql = "";
			PsTzSiteiMenuT psTzSiteiMenuT = null;
			String parentRealPath = "";
			File dir = null;
			String strForm = "";
			String synchronous = "";
			Map<String, Object> infoData = null;
			String siteId = "";
			String menuId = "";
			String menuName = "";
			String menuPath = "";
			String menuState = "";
			String menuType = "";
			String menuTempletId = "";
			String menuPageName = "";
			String isDefault = "";
			String NodeType = "";
			String operateNode = "";
			String menuXH = "";
			String menuStyle = "";
			String menuShow = "";
			// 新增代码
			String titleImageUrl = "";
			String titleImageTitle = "";
			String titleImageDesc = "";
			String saveImageAccessUrl = "";
			//

			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			Map<String, Object> mapData = null;

			// 父节点 操作节点的父节点
			Map<String, Object> pNode = null;

			// 自己节点，操作节点
			Map<String, Object> thisNode = null;
			// 子节点
			List<Map<String, Object>> sonList = null;
			// 根节点
			Map<String, Object> rootNode = null;

			List<Map<String, Object>> listData = null;

			// 父节点的默认页面
			String defaultPage = "";

			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				synchronous = jacksonUtil.getString("synchronous");

				// 判断是否是同步多语言数据的操作
				if ("true".equals(synchronous)) {
				} else {

					// 信息内容
					infoData = jacksonUtil.getMap("data");

					// 节点编号;

					siteId = infoData.get("siteId").toString();
					// 名称;
					menuName = infoData.get("menuName").toString();
					// 路径
					menuPath = infoData.get("menuPath").toString();
					// 栏目状态
					menuState = infoData.get("menuState").toString();
					// 类型
					menuType = infoData.get("menuType").toString();
					// 模板ID
					menuTempletId = infoData.get("menuTempletId").toString();
					// 循序
					menuXH = infoData.get("menuXH").toString();
					// 页面内容;
					if (infoData.containsKey("menuPageName")) {
						menuPageName = infoData.get("menuPageName").toString();
					}

					if (infoData.containsKey("menuStyle")) {
						menuStyle = infoData.get("menuStyle").toString();
					}

					// 新增代码
					/*
					 * if (infoData.containsKey("attachsysfilena")) {
					 * attachsysfilena =
					 * infoData.get("attachsysfilena").toString(); }
					 */
					if (infoData.containsKey("titleImageTitle")) {
						titleImageTitle = infoData.get("titleImageTitle").toString();
					}
					if (infoData.containsKey("titleImageDesc")) {
						titleImageDesc = infoData.get("titleImageDesc").toString();
					}
					if (infoData.containsKey("titleImageUrl")) {
						titleImageUrl = infoData.get("titleImageUrl").toString();
					}
					if (infoData.containsKey("saveImageAccessUrl")) {
						saveImageAccessUrl = infoData.get("saveImageAccessUrl").toString();
					}
					//

					// 是否默认页面;
					if (infoData.containsKey("isDefault")) {
						isDefault = infoData.get("isDefault").toString();
					} else {
						isDefault = "N";
					}

					if (infoData.containsKey("menuShow")) {
						menuShow = infoData.get("menuShow").toString();
						// System.out.println(infoData.get("menuShow").toString());
					} else
						menuShow = "N";
					// System.out.println("--------add--------menuShow----"+menuShow);
					System.out.println("isDefault:" + isDefault);
					// 插入同级节点还是子节点,Y:表示同级节点，'N'表示子节点;
					NodeType = infoData.get("NodeType").toString();

					// 插入同级节点或子节点是在哪个节点上操作的;
					operateNode = infoData.get("operateNode").toString();

					if ((menuName == null || "".equals(menuName)) || (menuState == null || "".equals(menuState))) {
						return "";
					}
					System.out.println("operateNode=" + operateNode);

					// 修改当前节点
					if (operateNode == null || "".equals(operateNode)) {
						operateNode = infoData.get("menuId").toString();
					}

					// 一次性获取一整颗树的所有数据放入List，然后对List进行各种操作，减轻SQL负担
					sql = tzSQLObject.getSQLText("SQL.TZOutSiteMgBundle.TzSelectOutSiteMenuList");

					listData = sqlQuery.queryForList(sql, new Object[] { siteId });

					if (null == listData || listData.size() <= 0) {
						errMsg[0] = "1";
						errMsg[1] = "树不存在";
						return strRet;
					}

					// 遍历List 得到树形结构
					int flag = -1;

					// 得到当前结点,以及根节点
					for (Object objData : listData) {
						mapData = (Map<String, Object>) objData;
						if (String.valueOf(mapData.get("TZ_MENU_LEVEL")).equals("0")) {
							rootNode = mapData;
						}

						if (String.valueOf(mapData.get("TZ_MENU_ID")).equals(operateNode)) {
							thisNode = mapData;
						}
					}

					// System.out.println("rootNode=" + rootNode);
					System.out.println("operateNode=" + operateNode);
					System.out.println("rootNode=" + rootNode);
					System.out.println("thisNode=" + thisNode);
					if (rootNode != null && thisNode != null) {
						if (rootNode.get("TZ_MENU_ID").toString().equals(thisNode.get("TZ_MENU_ID").toString())) {
							// 当前结点就是跟节点,没有父节点
							pNode = null;
						} else {
							for (Object objData : listData) {
								mapData = (Map<String, Object>) objData;
								if (String.valueOf(mapData.get("TZ_MENU_ID"))
										.equals(thisNode.get("TZ_F_MENU_ID").toString())) {
									pNode = mapData;
								}
							}
						}
					}
					// System.out.println("menuStyle=" + menuStyle);
					// 通过上的操作得到 当前节点父亲节点为pNode,当前节点为thisNode

					// boolean boolRst = false;

					// 新增代码
					// Map<String, Object> dataMap = jacksonUtil.getMap("data");
					// 标题图标题;
					titleImageTitle = (String) infoData.get("titleImageTitle");
					// 标题图描述;
					titleImageDesc = (String) infoData.get("titleImageDesc");
					// 标题图URL;
					titleImageUrl = (String) infoData.get("titleImageUrl");
					String sysFileName = "";
					if (titleImageUrl != null && !"".equals(titleImageUrl)) {
						String arr[] = titleImageUrl.split("/");
						sysFileName = arr[arr.length - 1];
					}

					//
					switch (NodeType) {

					// 添加同级节点;
					case "Y":
						menuId = String.valueOf(getSeqNum.getSeqNum("TZ_SITEI_MENU_T", "TZ_MENU_ID"));
						psTzSiteiMenuT = new PsTzSiteiMenuT();
						psTzSiteiMenuT.setTzSiteiId(siteId);
						psTzSiteiMenuT.setTzMenuId(menuId);
						psTzSiteiMenuT.setTzMenuName(menuName);
						psTzSiteiMenuT.setTzMenuPath(menuPath);
						psTzSiteiMenuT.setTzMenuState(menuState);
						psTzSiteiMenuT.setTzMenuLevel(new Integer(thisNode.get("TZ_MENU_LEVEL").toString()));
						psTzSiteiMenuT.setTzMenuType(menuType);
						psTzSiteiMenuT.setTzFMenuId(thisNode.get("TZ_F_MENU_ID").toString());
						psTzSiteiMenuT.setTzTempId(menuTempletId);
						psTzSiteiMenuT.setTzPageName(menuPageName);
						psTzSiteiMenuT.setTzMenuXh(new Integer(menuXH));
						psTzSiteiMenuT.setTzMenuStyle(menuStyle);

						// 新增代码
						// psTzSiteiMenuT.setTzAttachsysfilena(attachsysfilena);
						psTzSiteiMenuT.setTzImageTitle(titleImageTitle);
						psTzSiteiMenuT.setTzImageDesc(titleImageDesc);
						psTzSiteiMenuT.setTzAttachsysfilena(sysFileName);
						//

						psTzSiteiMenuT.setTzMenuShow(menuShow);

						psTzSiteiMenuT.setTzIsDel("Y"); // 允许删除
						psTzSiteiMenuT.setTzIsEditor("Y"); // 允许编辑

						psTzSiteiMenuT.setTzAddedOprid(oprid);
						psTzSiteiMenuT.setTzAddedDttm(new Date());
						psTzSiteiMenuT.setTzLastmantOprid(oprid);
						psTzSiteiMenuT.setTzLastmantDttm(new Date());

						psTzSiteiMenuTMapper.insertSelective(psTzSiteiMenuT);
						// 增加菜单,同级的菜单
						if (menuPath != null && !menuPath.equals("") && menuType.equals("B")) {
							if (!menuPath.startsWith("/")) {
								menuPath = "/" + menuPath;
							}
							// System.out.println(TZ_MENU_PATH + menuPath);
							parentRealPath = request.getServletContext()
									.getRealPath(rootNode.get("TZ_MENU_PATH").toString() + menuPath);
							System.out.println(parentRealPath);
							dir = new File(parentRealPath);
							if (!dir.exists()) {
								dir.mkdirs();
							}
						}

						// 对默认页面进行处理 A:PAGE B:BOOK 是否是默认页面 Y:是 N:不是
						if (menuType.equals("A") && isDefault.equals("Y")) {
							// 上级节点添加默认页面
							sql = "update PS_TZ_SITEI_MENU_T set TZ_D_MENU_ID=? where TZ_MENU_ID=? and TZ_SITEI_ID =?";
							sqlQuery.update(sql,
									new Object[] { menuId, thisNode.get("TZ_F_MENU_ID").toString(), siteId });
							defaultPage = menuName;
						} else {
							if (pNode != null) {
								defaultPage = pNode.get("TZ_DEFAULT_PAGE").toString();
							}
						}

						break;
					// 添加子节点;
					case "N":

						menuId = String.valueOf(getSeqNum.getSeqNum("TZ_SITEI_MENU_T", "TZ_MENU_ID"));
						psTzSiteiMenuT = new PsTzSiteiMenuT();
						psTzSiteiMenuT.setTzSiteiId(siteId);
						psTzSiteiMenuT.setTzMenuId(menuId);
						psTzSiteiMenuT.setTzMenuName(menuName);
						psTzSiteiMenuT.setTzMenuPath(menuPath);
						psTzSiteiMenuT.setTzMenuState(menuState);
						psTzSiteiMenuT.setTzMenuLevel(new Integer(thisNode.get("TZ_MENU_LEVEL").toString()) + 1);
						psTzSiteiMenuT.setTzMenuType(menuType);
						psTzSiteiMenuT.setTzFMenuId(operateNode);
						psTzSiteiMenuT.setTzTempId(menuTempletId);
						psTzSiteiMenuT.setTzPageName(menuPageName);
						psTzSiteiMenuT.setTzMenuXh(new Integer(menuXH));
						psTzSiteiMenuT.setTzMenuStyle(menuStyle);

						// 新增代码
						// psTzSiteiMenuT.setTzAttachsysfilena(attachsysfilena);
						psTzSiteiMenuT.setTzImageTitle(titleImageTitle);
						psTzSiteiMenuT.setTzImageDesc(titleImageDesc);
						psTzSiteiMenuT.setTzAttachsysfilena(sysFileName);
						//

						psTzSiteiMenuT.setTzMenuShow(menuShow);

						psTzSiteiMenuT.setTzIsDel("Y"); // 允许删除
						psTzSiteiMenuT.setTzIsEditor("Y"); // 允许编辑

						psTzSiteiMenuT.setTzAddedOprid(oprid);
						psTzSiteiMenuT.setTzAddedDttm(new Date());
						psTzSiteiMenuT.setTzLastmantOprid(oprid);
						psTzSiteiMenuT.setTzLastmantDttm(new Date());

						psTzSiteiMenuTMapper.insertSelective(psTzSiteiMenuT);

						if (menuPath != null && !menuPath.equals("") && menuType.equals("B")) {
							if (!menuPath.startsWith("/")) {
								menuPath = "/" + menuPath;
							}
							parentRealPath = request.getServletContext()
									.getRealPath(rootNode.get("TZ_MENU_PATH").toString() + menuPath);
							dir = new File(parentRealPath);
							if (!dir.exists()) {
								dir.mkdirs();
							}
						}

						// 对默认页面进行处理 A:PAGE B:BOOK 是否是默认页面 Y:是 N:不是
						if (menuType.equals("A") && isDefault.equals("Y")) {
							// 上级节点添加默认页面
							sql = "update PS_TZ_SITEI_MENU_T set TZ_D_MENU_ID=? where TZ_MENU_ID=? and TZ_SITEI_ID =?";
							sqlQuery.update(sql, new Object[] { menuId, operateNode, siteId });
							defaultPage = menuName;
						} else {
							defaultPage = thisNode.get("TZ_DEFAULT_PAGE").toString();
						}
						break;

					// 修改当前结点
					default:

						psTzSiteiMenuT = new PsTzSiteiMenuT();
						psTzSiteiMenuT.setTzSiteiId(siteId);
						psTzSiteiMenuT.setTzMenuId(operateNode);
						psTzSiteiMenuT.setTzMenuName(menuName);
						// psTzSiteiMenuT.setTzMenuPath(menuPath); path不能修改
						psTzSiteiMenuT.setTzMenuState(menuState);
						psTzSiteiMenuT.setTzMenuXh(new Integer(menuXH));
						psTzSiteiMenuT.setTzMenuStyle(menuStyle);

						// 新增代码
						// psTzSiteiMenuT.setTzAttachsysfilena(attachsysfilena);
						psTzSiteiMenuT.setTzImageTitle(titleImageTitle);
						psTzSiteiMenuT.setTzImageDesc(titleImageDesc);
						psTzSiteiMenuT.setTzAttachsysfilena(sysFileName);
						//
						psTzSiteiMenuT.setTzMenuShow(menuShow);

						psTzSiteiMenuT.setTzTempId(menuTempletId);
						psTzSiteiMenuT.setTzPageName(menuPageName);
						psTzSiteiMenuT.setTzLastmantOprid(oprid);
						psTzSiteiMenuT.setTzLastmantDttm(new Date());
						psTzSiteiMenuTMapper.updateByPrimaryKeySelective(psTzSiteiMenuT);

						// 对默认页面进行处理 A:PAGE B:BOOK 是否是默认页面 Y:是 N:不是
						if (menuType.equals("A") && pNode != null) {

							String TZ_D_MENU_ID = pNode.get("TZ_D_MENU_ID").toString();

							System.out.println("TZ_D_MENU_ID:" + TZ_D_MENU_ID);

							System.out.println("operateNode:" + operateNode);

							// 情况1 ： 本来是默认页面 修改成非默认页面，那么上级节点默认页面取消
							if (TZ_D_MENU_ID.equals(operateNode) && !isDefault.equals("Y")) {
								sql = "update PS_TZ_SITEI_MENU_T set TZ_D_MENU_ID='' where TZ_MENU_ID=? and TZ_SITEI_ID =?";
								sqlQuery.update(sql, new Object[] { thisNode.get("TZ_F_MENU_ID").toString(), siteId });
								defaultPage = "";
							} else
							// 情况2 ： 本来是默认页面 现在还是默认 啥都不做
							if (TZ_D_MENU_ID.equals(operateNode) && isDefault.equals("Y")) {
								defaultPage = pNode.get("TZ_DEFAULT_PAGE").toString();
							} else
							// 情况3 ： 本来不是默认页面 现在还不是默认 啥都不做
							if (!TZ_D_MENU_ID.equals(operateNode) && !isDefault.equals("Y")) {
								defaultPage = pNode.get("TZ_DEFAULT_PAGE").toString();
							} else
							// 情况4 ： 本来不是默认页面 现在是默认 上级节点修改默认页面
							if (!TZ_D_MENU_ID.equals(operateNode) && isDefault.equals("Y")) {
								sql = "update PS_TZ_SITEI_MENU_T set TZ_D_MENU_ID=? where TZ_MENU_ID=? and TZ_SITEI_ID =?";
								sqlQuery.update(sql,
										new Object[] { operateNode, thisNode.get("TZ_F_MENU_ID").toString(), siteId });
								defaultPage = menuName;
							}
						}
						// 获取子节点集合
						sonList = getMenuList(thisNode.get("TZ_MENU_ID").toString(), listData, saveImageAccessUrl,siteId);
						// if(sonList!=null)
						// System.out.println("sonListSize:"+sonList.size());
						// System.out.println("TZ_MENU_ID:"+thisNode.get("TZ_MENU_ID").toString());
						// System.out.println("TZ_MENU_SHOW:"+thisNode.get("TZ_MENU_SHOW"));
						// System.out.println("====show==="+menuShow);
						if (sonList != null && menuShow.equals("N")) {
							this.colseMenuShow(sonList, listData, saveImageAccessUrl,siteId);
						}
						// this.getChildList(thisNode.get("TZ_MENU_ID").toString(),
						// listData, sonList);
						// System.out.println(sonList.size());
						break;
					}

					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("success", "true");
					mapRet.put("newMenuID", menuId);
					// 上级菜单的默认页面
					mapRet.put("defaultPage", defaultPage);
					strRet = jacksonUtil.Map2json(mapRet);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/**
	 * 删除节点
	 */
	@Override
	@Transactional
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		String deleteDefaultPage = "N";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int dataLength = actData.length;
			String strForm = "";
			Map<String, Object> infoData = null;
			String menuId = "";
			String siteId = "";
			String sql = "";
			List<Map<String, Object>> listData = null;

			for (int num = 0; num < dataLength; num++) {
				// 提交信息
				strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				infoData = jacksonUtil.getMap("data");

				menuId = infoData.get("menuId").toString();
				siteId = infoData.get("siteId").toString();

				// 全表查询
				sql = "select TZ_MENU_ID,ifnull(TZ_F_MENU_ID,\"\") TZ_F_MENU_ID from PS_TZ_SITEI_MENU_T where TZ_SITEI_ID = ?";

				listData = sqlQuery.queryForList(sql, new Object[] { siteId });

				sql = "delete from PS_TZ_SITEI_MENU_T where  TZ_SITEI_ID = ? and TZ_MENU_ID=?";
				sqlQuery.update(sql, new Object[] { siteId, menuId });

				// 默认页面处理
				sql = "select 'Y' from PS_TZ_SITEI_MENU_T where TZ_D_MENU_ID = ? and TZ_SITEI_ID = ? ";
				String isHas = sqlQuery.queryForObject(sql, new Object[] { menuId, siteId }, "String");
				System.out.println("isHas:" + isHas);
				if (StringUtils.equals(isHas, "Y")) {
					sql = "update PS_TZ_SITEI_MENU_T set TZ_D_MENU_ID='' where TZ_D_MENU_ID =? and TZ_SITEI_ID = ?";
					sqlQuery.update(sql, new Object[] { menuId, siteId });
					deleteDefaultPage = "Y";
				}

				this.deleteChild(menuId, listData, siteId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("defaultPage", deleteDefaultPage);
		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}

	private void deleteChild(String FmenuId, List<Map<String, Object>> listData, String siteId) {
		Map<String, Object> mapNode = null;
		String TZ_F_MENU_ID = "";
		boolean isLeaf = false;
		String sql = "";
		String menuId = "";
		for (Object objNode : listData) {
			mapNode = (Map<String, Object>) objNode;
			TZ_F_MENU_ID = mapNode.get("TZ_F_MENU_ID").toString();

			if (TZ_F_MENU_ID.equals(FmenuId)) {
				menuId = String.valueOf(mapNode.get("TZ_MENU_ID"));
				sql = "delete from PS_TZ_SITEI_MENU_T where  TZ_SITEI_ID = ? and TZ_MENU_ID=?";
				sqlQuery.update(sql, new Object[] { siteId, menuId });
				isLeaf = this.isLeaf(menuId, listData);
				if (isLeaf) {
					this.deleteChild(menuId, listData, siteId);
				}
			}
		}
	}

	/**
	 * 其他相关函数
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public String tzOther(String oprType, String strParams, String[] errMsg) {
		Map<String, Object> mapRet = new HashMap<String, Object>();

		JacksonUtil jacksonUtil = new JacksonUtil();
		boolean success = false;
		try {
			// 生成一级菜单
			CmsUtils cu = new CmsUtils();
			if ("mainMenu".equals(oprType)) {
				jacksonUtil.json2Map(strParams);

				String siteId = jacksonUtil.getString("siteId");
				CmsBean cm = cu.menuBook(siteId, "");
				// 写文件
				if (cm != null && cm.getHtmlName() != null && !cm.getHtmlName().equals("")) {
					try {
						// 生成文件
						// success =
						// fileManageServiceImpl.UpdateFile(cm.getPath(),
						// cm.getHtmlName(),
						// cm.getHtml().getBytes());

						String dir = getSysHardCodeVal.getWebsiteEnrollPath();
						dir = request.getServletContext().getRealPath(dir);
						dir = dir + File.separator + cm.getPath();

						System.out.println("path:" + cm.getPath());
						System.out.println("html:" + cm.getHtml());
						System.out.println("dir:" + dir);
						System.out.println("name:" + cm.getHtmlName());
						success = this.staticFile(cm.getHtml(), dir, cm.getHtmlName(), errMsg);

					} catch (Exception e) {
						success = false;
						errMsg[0] = "1";
						errMsg[1] = "站点目录生成失败";
					}
				} else {
					success = false;
					errMsg[0] = "1";
					errMsg[1] = "站点目录生成失败";
				}

			} else if ("otherMenu".equals(oprType)) { // 生成其他菜单或页面
				jacksonUtil.json2Map(strParams);
				String siteId = jacksonUtil.getString("siteId");
				String menuId = jacksonUtil.getString("menuId");
				String menuType = jacksonUtil.getString("menuType");

				CmsBean cm = null;
				// A:PAGE B:BOOK
				if (menuType.endsWith("B")) {
					cm = cu.menuBook(siteId, menuId);
				} else if (menuType.endsWith("A")) {
					System.out.println("PAGE Creeate,menuId=" + menuId + ",siteid=" + siteId);
					String contentPath = request.getContextPath();
					cm = cu.menuPage(siteId, menuId, contentPath, "1");
				}
				// 写文件 不是所有的BOOK 都有文件名，某些次级BOOK不需要生成 菜单文件
				if (cm != null && cm.getHtmlName() != null && !cm.getHtmlName().equals("")) {
					try {
						// 生成文件
						// success =
						// fileManageServiceImpl.UpdateFile(cm.getPath(),
						// cm.getHtmlName(),
						// cm.getHtml().getBytes());

						String dir = getSysHardCodeVal.getWebsiteEnrollPath();
						dir = request.getServletContext().getRealPath(dir);
						dir = dir + File.separator + cm.getPath();

						success = this.staticFile(cm.getHtml(), dir, cm.getHtmlName(), errMsg);
					} catch (Exception e) {
						success = false;
						errMsg[0] = "1";
						errMsg[1] = "站点目录生成失败";
					}
				} else {
					success = false;
					errMsg[0] = "1";
					errMsg[1] = "站点目录生成失败";
				}
			}
		} catch (Exception e) {
			success = false;
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		mapRet.put("success", success);
		return jacksonUtil.Map2json(mapRet);
	}

	public boolean staticFile(String strReleasContent, String dir, String fileName, String[] errMsg) {
		try {
			System.out.println("dir:" + dir);
			File fileDir = new File(dir);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}

			String filePath = "";
			if ((dir.lastIndexOf(File.separator) + 1) != dir.length()) {
				filePath = dir + File.separator + fileName;
			} else {
				filePath = dir + fileName;
			}
			System.out.println("filePath:" + filePath);
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			} else {
				file.delete();
			}
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file.getAbsoluteFile(), true),
					"UTF-8");
			osw.write(strReleasContent);
			osw.close();
			System.out.println("Creatt " + fileName + " OK!");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "3";
			errMsg[1] = "静态化文件时异常！";
			return false;
		}
	}

}
