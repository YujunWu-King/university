/**
 * 
 */
package com.tranzvision.gd.TZEventsBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzLmNrGlTMapper;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzLmNrGlTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

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
					"TZ_APPF_DT", "TZ_APPE_DT", "TZ_APPLY_NUM", "TZ_SITE_ID", "TZ_COLU_ID", "TZ_ART_PUB_STATE",
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
					mapList.put("siteId", rowList[8]);
					mapList.put("columnId", rowList[9]);
					mapList.put("releaseOrUndo", rowList[10]);
					mapList.put("topOrUndo", rowList[11]);

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

				if (errorMsg.length > 0) {
					break;
				} else {
					// 表单内容
					String strForm = actData[num];
					// 解析json
					jacksonUtil.json2Map(strForm);

					String strClickTyp = jacksonUtil.getString("ClickTyp");
					Map<String, Object> mapParams = jacksonUtil.getMap("data");

					String tzSiteId = String.valueOf(mapParams.getOrDefault("siteId", ""));
					String tzColuId = String.valueOf(mapParams.getOrDefault("columnId", ""));
					String tzArtId = String.valueOf(mapParams.getOrDefault("activityId", ""));

					if (!"".equals(tzSiteId) && !"".equals(tzColuId) && !"".equals(tzArtId)) {

						if ("T".equals(strClickTyp)) {
							// 置顶处理
							String topFlag = String.valueOf(mapParams.getOrDefault("topOrUndo", ""));

							if ("TOP".equals(topFlag.toUpperCase())) {
								sql = "select max(TZ_MAX_ZD_SEQ) from PS_TZ_LM_NR_GL_T where TZ_SITE_ID=? and TZ_COLU_ID=? and TZ_ART_ID<>?";
								int tzMaxZdSeq = sqlQuery.queryForObject(sql,
										new Object[] { tzSiteId, tzColuId, tzArtId }, "int");

								PsTzLmNrGlTWithBLOBs psTzLmNrGlTWithBLOBs = new PsTzLmNrGlTWithBLOBs();
								psTzLmNrGlTWithBLOBs.setTzSiteId(tzSiteId);
								psTzLmNrGlTWithBLOBs.setTzColuId(tzColuId);
								psTzLmNrGlTWithBLOBs.setTzArtId(tzArtId);
								psTzLmNrGlTWithBLOBs.setTzMaxZdSeq(tzMaxZdSeq + 1);
								psTzLmNrGlTWithBLOBs.setTzLastmantDttm(dateNow);
								psTzLmNrGlTWithBLOBs.setTzLastmantOprid(oprid);
								psTzLmNrGlTMapper.updateByPrimaryKeySelective(psTzLmNrGlTWithBLOBs);

							} else if ("0".equals(topFlag)) {

								sql = "select TZ_MAX_ZD_SEQ from PS_TZ_LM_NR_GL_T where TZ_SITE_ID=? and TZ_COLU_ID=? and TZ_ART_ID=?";
								int tzMaxZdSeq = sqlQuery.queryForObject(sql,
										new Object[] { tzSiteId, tzColuId, tzArtId }, "int");

								if (tzMaxZdSeq > 0) {

									sql = "update PS_TZ_LM_NR_GL_T set TZ_MAX_ZD_SEQ = TZ_MAX_ZD_SEQ - 1 where TZ_SITE_ID=? and TZ_COLU_ID=? and TZ_MAX_ZD_SEQ>?";
									sqlQuery.update(sql, new Object[] { tzSiteId, tzColuId, tzMaxZdSeq });

									PsTzLmNrGlTWithBLOBs psTzLmNrGlTWithBLOBs = new PsTzLmNrGlTWithBLOBs();
									psTzLmNrGlTWithBLOBs.setTzSiteId(tzSiteId);
									psTzLmNrGlTWithBLOBs.setTzColuId(tzColuId);
									psTzLmNrGlTWithBLOBs.setTzArtId(tzArtId);
									psTzLmNrGlTWithBLOBs.setTzMaxZdSeq(0);
									psTzLmNrGlTWithBLOBs.setTzLastmantDttm(dateNow);
									psTzLmNrGlTWithBLOBs.setTzLastmantOprid(oprid);
									psTzLmNrGlTMapper.updateByPrimaryKeySelective(psTzLmNrGlTWithBLOBs);

								}

							}

						} else if ("P".equals(strClickTyp)) {
							// 发布处理
							String tzArtPubState = String.valueOf(mapParams.getOrDefault("releaseOrUndo", ""));

							PsTzLmNrGlTWithBLOBs psTzLmNrGlTWithBLOBs = new PsTzLmNrGlTWithBLOBs();
							psTzLmNrGlTWithBLOBs.setTzSiteId(tzSiteId);
							psTzLmNrGlTWithBLOBs.setTzColuId(tzColuId);
							psTzLmNrGlTWithBLOBs.setTzArtId(tzArtId);
							psTzLmNrGlTWithBLOBs.setTzArtPubState(tzArtPubState);
							psTzLmNrGlTWithBLOBs.setTzLastmantDttm(dateNow);
							psTzLmNrGlTWithBLOBs.setTzLastmantOprid(oprid);

							String todoGenArtHtml;
							String tzArtHtml = "";
							psTzLmNrGlTWithBLOBs.setTzArtHtml(tzArtHtml);

							if ("Y".equals(tzArtPubState)) {
								psTzLmNrGlTWithBLOBs.setTzArtConentScr(tzArtHtml);
							} else {
								psTzLmNrGlTWithBLOBs.setTzArtConentScr("");
							}

							sql = "select TZ_ART_NEWS_DT from PS_TZ_LM_NR_GL_T where TZ_SITE_ID=? and TZ_COLU_ID=? and TZ_ART_ID=?";
							Date tzArtNewsDt = sqlQuery.queryForObject(sql,
									new Object[] { tzSiteId, tzColuId, tzArtId }, "Date");
							if ("Y".equals(tzArtPubState) && tzArtNewsDt == null) {
								tzArtNewsDt = new Date();
								psTzLmNrGlTWithBLOBs.setTzArtNewsDt(tzArtNewsDt);
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

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作失败。" + e.getMessage();
		}

		return strRet;
	}

}
