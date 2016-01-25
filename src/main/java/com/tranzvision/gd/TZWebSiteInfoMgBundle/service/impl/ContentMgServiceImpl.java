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
import com.tranzvision.gd.TZWebSiteInfoBundle.service.impl.ArtContentHtml;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzLmNrGlTMapper;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzLmNrGlTKey;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzLmNrGlTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 内容管理列表；原：TZ_GD_CONTENT_PKG:TZ_CONTENT_CLS
 * 
 * @author tang
 * @since 2015-11-23
 */

@Service("com.tranzvision.gd.TZWebSiteInfoMgBundle.service.impl.ContentMgServiceImpl")
@SuppressWarnings("unchecked")
public class ContentMgServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzLmNrGlTMapper psTzLmNrGlTMapper;
	@Autowired
	private ArtContentHtml artContentHtml;


	/* 查询内容列表 */
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

	/* 查询栏目 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		ArrayList<Map<String, Object>> arraylist = new ArrayList<>();
		returnJsonMap.put("TransList", arraylist);
		JacksonUtil jacksonUtil = new JacksonUtil();
		// 获取登录的机构;
		String strJgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		// 获取机构对应的站点；
		String siteSQL = " SELECT TZ_SITEI_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ENABLE='Y' and TZ_JG_ID=?";
		String siteId = jdbcTemplate.queryForObject(siteSQL, new Object[] { strJgid }, "String");

		
		// 栏目;

		String columnSQL = "SELECT TZ_COLU_ID,TZ_COLU_NAME FROM PS_TZ_SITEI_COLU_T WHERE TZ_CONT_TYPE<>'A' and  TZ_SITEI_ID=? and TZ_COLU_STATE='Y' ORDER BY TZ_COLU_ID ASC";

		List<Map<String, Object>> list = jdbcTemplate.queryForList(columnSQL, new Object[] { siteId });
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> jsonMap = new HashMap<>();
				jsonMap.put("TValue", list.get(i).get("TZ_COLU_ID"));
				jsonMap.put("TSDesc", list.get(i).get("TZ_COLU_NAME"));
				jsonMap.put("TLDesc", list.get(i).get("TZ_COLU_NAME"));

				arraylist.add(jsonMap);
			}
			returnJsonMap.replace("TransList", arraylist);
		}

		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
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

						PsTzLmNrGlTKey psTzLmNrGlTKey = new PsTzLmNrGlTKey();
						psTzLmNrGlTKey.setTzSiteId(siteId);
						psTzLmNrGlTKey.setTzColuId(columnId);
						psTzLmNrGlTKey.setTzArtId(articleId);
						PsTzLmNrGlTWithBLOBs psTzLmNrGlT = psTzLmNrGlTMapper.selectByPrimaryKey(psTzLmNrGlTKey);
						if (psTzLmNrGlT != null) {
							Date artNewsDt = psTzLmNrGlT.getTzArtNewsDt();
							PsTzLmNrGlTWithBLOBs psTzLmNrGlTWithBLOBs = new PsTzLmNrGlTWithBLOBs();
							psTzLmNrGlTWithBLOBs.setTzSiteId(siteId);
							psTzLmNrGlTWithBLOBs.setTzColuId(columnId);
							psTzLmNrGlTWithBLOBs.setTzArtId(articleId);
							psTzLmNrGlTWithBLOBs.setTzArtPubState(releaseOrUndo);
							//解析的模板内容;
							String contentHtml = artContentHtml.getContentHtml(siteId, columnId, articleId);
							
							psTzLmNrGlTWithBLOBs.setTzArtHtml(contentHtml);
							if ("Y".equals(releaseOrUndo)) {
								// 如果发布但没有发布时间，则赋值当前时间为发布时间;
								if (artNewsDt == null) {
									psTzLmNrGlTWithBLOBs.setTzArtNewsDt(new Date());
								}
								psTzLmNrGlTWithBLOBs.setTzArtConentScr(contentHtml);
							} else {
								psTzLmNrGlTWithBLOBs.setTzArtConentScr("");
							}
							psTzLmNrGlTWithBLOBs.setTzLastmantDttm(new Date());
							psTzLmNrGlTWithBLOBs.setTzLastmantOprid(oprid);
							int success = psTzLmNrGlTMapper.updateByPrimaryKeySelective(psTzLmNrGlTWithBLOBs);
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

}
