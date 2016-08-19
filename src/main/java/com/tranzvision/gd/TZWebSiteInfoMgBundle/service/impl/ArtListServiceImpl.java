package com.tranzvision.gd.TZWebSiteInfoMgBundle.service.impl;

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
import com.tranzvision.gd.TZOrganizationOutSiteMgBundle.dao.PsTzArtTypeTMapper;
import com.tranzvision.gd.TZOrganizationOutSiteMgBundle.dao.PsTzContFldefTMapper;
import com.tranzvision.gd.TZOrganizationOutSiteMgBundle.model.PsTzArtTypeT;
import com.tranzvision.gd.TZOrganizationOutSiteMgBundle.model.PsTzContFldefT;
import com.tranzvision.gd.TZOrganizationOutSiteMgBundle.model.PsTzContFldefTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * @author zhangbb
 * @version 创建时间：2016年8月17日 下午16:28:30 类说明 内容发布管理
 */
@Service("com.tranzvision.gd.TZWebSiteInfoMgBundle.service.impl.ArtListServiceImpl")
public class ArtListServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private PsTzArtTypeTMapper psTzArtTypeTMapper;
	
	@Autowired
	private PsTzContFldefTMapper PsTzContFldefTMapper;
	
	/* 查询类型类型列表 */
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
		String artTypeId = jacksonUtil.getString("artTypeId");
		try {
			String totalSQL = "SELECT COUNT(1) FROM PS_TZ_CONT_FLDEF_T where TZ_ART_TYPE_ID = ?";
			int total = jdbcTemplate.queryForObject(totalSQL, new Object[] { artTypeId }, "Integer");
			String sql = "";
			List<Map<String, Object>> list = null;

			sql = "SELECT TZ_FIELD_VALUE,TZ_FIELD_DESC,TZ_SEQ,IS_ENABLED_FLG FROM PS_TZ_CONT_FLDEF_T where TZ_ART_TYPE_ID = ? ORDER BY TZ_SEQ ASC";
			list = jdbcTemplate.queryForList(sql, new Object[] { artTypeId });
			Map<String, Object> jsonMap;
			if (list != null && total > 0 ) {

				for (int i = 0; i < list.size(); i++) {
					boolean used = false;
					if ("Y".equals(list.get(i).get("IS_ENABLED_FLG"))) {
						used = true;
					}
					jsonMap = new HashMap<String, Object>();
					jsonMap.put("fieldValue", list.get(i).get("TZ_FIELD_VALUE"));
					jsonMap.put("fieldDescr", list.get(i).get("TZ_FIELD_DESC"));
					jsonMap.put("seq", list.get(i).get("TZ_SEQ"));
					jsonMap.put("isused", used);
					arraylist.add(jsonMap);
				}
				returnJsonMap.replace("total", total);
				returnJsonMap.replace("root", arraylist);
				strRet = jacksonUtil.Map2json(returnJsonMap);
			}else{
				total = 9;
				for (int i = 0; i < 9; i++) {
					if(i<4){
						jsonMap = new HashMap<String, Object>();
						jsonMap.put("fieldValue", "TZ_TXT" + String.valueOf(i + 1));
						jsonMap.put("fieldDescr", "描述字段" + String.valueOf(i + 1 ));
						jsonMap.put("seq", String.valueOf(i + 1));
						jsonMap.put("isused", false);
						arraylist.add(jsonMap);
					}else if(i<7){
						jsonMap = new HashMap<String, Object>();
						jsonMap.put("fieldValue", "TZ_LONG" + String.valueOf(i + 1));
						jsonMap.put("fieldDescr", "描述字段" + String.valueOf(i + 1));
						jsonMap.put("seq", String.valueOf(i + 1));
						jsonMap.put("isused", false);
						arraylist.add(jsonMap);
					}else{
						jsonMap = new HashMap<String, Object>();
						jsonMap.put("fieldValue", "TZ_DATE" + String.valueOf(i + 1));
						jsonMap.put("fieldDescr", "描述字段" + String.valueOf(i + 1));
						jsonMap.put("seq", String.valueOf(i + 1));
						jsonMap.put("isused", false);
						arraylist.add(jsonMap);
					}
				}
			}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	/* 获取内容栏目树类型定义*/
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("siteid")) {

				// 机构编号（菜单树节点编号）
				String siteId = jacksonUtil.getString("siteId");
				// 查询类型
				String typeFlag = jacksonUtil.getString("typeFlag");

				String sql;
				Map<String, Object> mapRet = new HashMap<String, Object>();
				if ("TREE".equals(typeFlag)) {

					List<?> listChildren = this.getChannelList("MBA");

					Map<String, Object> mapRootJson = new HashMap<String, Object>();

					mapRootJson.put("id", "MBA");
					mapRootJson.put("nodeId", "MBA");
					mapRootJson.put("expanded", "true");
					mapRootJson.put("text", "MBA");
					mapRootJson.put("leaf", "false");
					mapRootJson.put("children", listChildren);
				
					mapRet.put("root", mapRootJson);
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
	 * 获取菜单的下级菜单列表
	 * 
	 * @param strMenuID
	 * @param baseLanguage
	 * @param targetLanguage
	 * @return List<Map<String, Object>>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getChannelList(String strChannelID) {

		List<Map<String, Object>> listRet = new ArrayList<Map<String, Object>>();

		try {

				// 判断该菜单是否是叶子菜单
				//sql = "select if(count(1)=0,'Y','N') from PSTREENODE where TREE_NAME = ? and PARENT_NODE_NAME=?";

				Map<String, Object> mapNodeJson = new HashMap<String, Object>();
				mapNodeJson.put("id", "MBA_CHNL_0001");
				mapNodeJson.put("nodeId", "MBA_CHNL_0001");
				mapNodeJson.put("expanded", "true");
				mapNodeJson.put("text", "学前教育");
				mapNodeJson.put("leaf", "true");

				/*	
				if ("Y".equals(isLeaf)) {

					mapNodeJson.put("leaf", true);

				} else {

					List<Map<String, Object>> listChildren = this.getChannelList(strChannelID);

					mapNodeJson.put("leaf", false);
					mapNodeJson.put("expanded", true);
					mapNodeJson.put("children", listChildren);

				}
				*/
				listRet.add(mapNodeJson);
				
				mapNodeJson = new HashMap<String, Object>();
				mapNodeJson.put("id", "MBA_CHNL_0002");
				mapNodeJson.put("nodeId", "MBA_CHNL_0002");
				mapNodeJson.put("expanded", "true");
				mapNodeJson.put("text", "学习手册");
				mapNodeJson.put("leaf", "true");
				listRet.add(mapNodeJson);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return listRet;
	}
}
