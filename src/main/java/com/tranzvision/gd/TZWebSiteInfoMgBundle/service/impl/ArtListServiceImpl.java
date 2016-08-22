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
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
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
	private FliterForm fliterForm;
	
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
				mapNodeJson.put("id", "146");
				mapNodeJson.put("nodeId", "146");
				mapNodeJson.put("expanded", "true");
				mapNodeJson.put("text", "新闻");
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
				mapNodeJson.put("id", "147");
				mapNodeJson.put("nodeId", "147");
				mapNodeJson.put("expanded", "true");
				mapNodeJson.put("text", "活动");
				mapNodeJson.put("leaf", "true");
				listRet.add(mapNodeJson);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return listRet;
	}
}
