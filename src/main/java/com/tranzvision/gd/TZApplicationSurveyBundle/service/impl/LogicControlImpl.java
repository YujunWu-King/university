package com.tranzvision.gd.TZApplicationSurveyBundle.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 问卷填写的时候逻辑控制
 * 
 * @author CAOY
 *
 */
@Service
public class LogicControlImpl {

	@Autowired
	private SqlQuery jdbcTemplate;

	/*********************************************************************************************************************************************************
	 * 功能描述：获取问卷指定页面问题的显示逻辑 参数说明：1 - &surveyIns：在线调查实例ID； 2 - &pageNum：页号； 3 -
	 * &allItemsFlag：查询所有信息项是否显示标准，“Y”标识查询所有信息项显示结果，此时页号不起作用，“N”
	 * 标识至查询指定页内信息项显示结果； 返回值：报文格式： { itemID:{ //问题编号 display:"Y"//是否显示 }, ... }
	 *********************************************************************************************************************************************************/
	public String getCurrentPageDisplay(String surveyIns, int pageNum, String allItemsFlag) {

		/* 调查问卷编号 */
		String surveyId = jdbcTemplate.queryForObject("SELECT TZ_DC_WJ_ID FROM PS_TZ_DC_INS_T WHERE TZ_APP_INS_ID= ?",
				new Object[] { surveyIns }, "String");
		String itemId = "";
		String storeType = "";
		String logicId = "";
		String strItemsJson = "";

		// 存放查询结果的list
		List<Map<String, Object>> qList = null;
		if (allItemsFlag.equals("Y")) {
			qList = jdbcTemplate.queryForList(
					"SELECT TZ_XXX_BH FROM PS_TZ_DCWJ_XXXPZ_T WHERE TZ_DC_WJ_ID=? ORDER BY TZ_ORDER",
					new Object[] { surveyId });
		} else {
			qList = jdbcTemplate.queryForList(
					"SELECT TZ_XXX_BH FROM PS_TZ_DCWJ_XXXPZ_T WHERE TZ_DC_WJ_ID=? AND TZ_PAGE_NO=? ORDER BY TZ_ORDER",
					new Object[] { surveyId, pageNum });
		}
		// 取数据用户的MAP
		Map<String, Object> itemsMap = null;

		Map<String, Object> dateMap = null;

		if (qList != null) {
			List<Map<String, Object>> subqList = null;

			List<Map<String, Object>> subqList2 = null;
			boolean isDisplay = true;
			String strLogicJson = "";
			String display = "";

			String className = "";
			int LogicCtrlCount;
			String mainItemId = "", logicType = "";
			int intlogicType;
			int toPageNum, orderBy;

			String zwt_name = "";
			String zwt_option = "";
			String zwt_opt_selected = "";
			String sItemAnswer = "";

			String wt_option = "", wt_opt_selected = "";

			String opt_qtz = "";
			String opt_checked = "";

			String yb_s_val = "";
			String yb_o_val = "";
			String yb_l_val = "";
			for (int i = 0; i < qList.size(); i++) {
				itemsMap = qList.get(i);
				itemId = itemsMap.get("TZ_XXX_BH") == null ? "" : itemsMap.get("TZ_XXX_BH").toString();
				LogicCtrlCount = jdbcTemplate.queryForObject(
						"SELECT COUNT(*) FROM PS_TZ_DC_WJ_LJXS_T WHERE TZ_DC_WJ_ID=? AND TZ_XXX_BH=?",
						new Object[] { surveyId, itemId }, "int");
				if (LogicCtrlCount > 0) {
					subqList = jdbcTemplate.queryForList(
							"SELECT TZ_DC_LJTJ_ID FROM PS_TZ_DC_WJ_LJXS_T WHERE TZ_DC_WJ_ID=? AND TZ_XXX_BH=?",
							new Object[] { surveyId, itemId });
					if (subqList != null) {
						for (int j = 0; j < subqList.size(); j++) {
							itemsMap = qList.get(j);
							logicId = itemsMap.get("TZ_DC_LJTJ_ID") == null ? ""
									: itemsMap.get("TZ_DC_LJTJ_ID").toString();

							/* 逻辑定义属性 */
							dateMap = null;
							dateMap = jdbcTemplate.queryForMap(
									"SELECT TZ_XXX_BH,TZ_LJ_LX,TZ_PAGE_NO,TZ_LJTJ_XH FROM PS_TZ_DC_WJ_LJGZ_T WHERE TZ_DC_LJTJ_ID=?",
									new Object[] { logicId });
							if (dateMap != null) {
								if (dateMap.containsKey("TZ_XXX_BH") && dateMap.get("TZ_XXX_BH") != null) {
									mainItemId = dateMap.get("TZ_XXX_BH").toString();
								}
								if (dateMap.containsKey("TZ_LJ_LX") && dateMap.get("TZ_LJ_LX") != null) {
									logicType = dateMap.get("TZ_LJ_LX").toString();
								}
								if (dateMap.containsKey("TZ_PAGE_NO") && dateMap.get("TZ_PAGE_NO") != null) {
									toPageNum = Integer.parseInt(dateMap.get("TZ_PAGE_NO").toString());
								}
								if (dateMap.containsKey("TZ_LJTJ_XH") && dateMap.get("TZ_LJTJ_XH") != null) {
									orderBy = Integer.parseInt(dateMap.get("TZ_LJTJ_XH").toString());
								}
							}
							/* 存储类型 */
							dateMap = null;
							dateMap = jdbcTemplate.queryForMap(
									"SELECT TZ_XXX_CCLX,TZ_COM_LMC FROM PS_TZ_DCWJ_XXXPZ_T WHERE TZ_DC_WJ_ID=? AND TZ_XXX_BH=?",
									new Object[] { surveyId, mainItemId });
							if (dateMap != null) {
								if (dateMap.containsKey("TZ_XXX_CCLX") && dateMap.get("TZ_XXX_CCLX") != null) {
									storeType = dateMap.get("TZ_XXX_CCLX").toString();
								}
								if (dateMap.containsKey("TZ_COM_LMC") && dateMap.get("TZ_COM_LMC") != null) {
									className = dateMap.get("TZ_COM_LMC").toString();
								}
							}

							/* T-表格存储类型 开始 */
							if (storeType.equals("T")) {
								/* 逻辑规则条件 */
								subqList2 = jdbcTemplate.queryForList(
										"SELECT TZ_XXXZWT_MC,TZ_XXXKXZ_MC,TZ_IS_SELECTED FROM PS_TZ_DC_WJ_GZGX_T WHERE TZ_DC_LJTJ_ID=? AND TZ_DC_WJ_ID=? AND TZ_XXX_BH=?",
										new Object[] { logicId, surveyId, mainItemId });
								/* 该逻辑规则下所有条件满足才显示 */
								if (subqList2 != null) {
									for (int x = 0; x < subqList2.size(); x++) {
										itemsMap = subqList2.get(x);
										zwt_name = itemsMap.get("TZ_XXXZWT_MC") == null ? ""
												: itemsMap.get("TZ_XXXZWT_MC").toString();
										zwt_option = itemsMap.get("TZ_XXXKXZ_MC") == null ? ""
												: itemsMap.get("TZ_XXXKXZ_MC").toString();
										zwt_opt_selected = itemsMap.get("TZ_IS_SELECTED") == null ? ""
												: itemsMap.get("TZ_IS_SELECTED").toString();

										sItemAnswer = jdbcTemplate.queryForObject(
												"SELECT TZ_APP_S_TEXT FROM PS_TZ_DCDJ_BGT_T WHERE TZ_APP_INS_ID=? AND TZ_XXX_BH=? AND TZ_XXXKXZ_WTMC=? AND TZ_XXXKXZ_XXMC=?",
												new Object[] { surveyIns, mainItemId, zwt_name, zwt_option }, "String");

										if ((zwt_opt_selected.equals("1") && StringUtils.isEmpty(sItemAnswer))
												|| (zwt_opt_selected.equals("2")
														&& !StringUtils.isEmpty(sItemAnswer))) {
											/* 不满足 */
											if (isDisplay) {
												isDisplay = false;
											}
										}
									}
								}
							}
							/* T-表格存储类型 结束 */
							else {
								subqList2 = jdbcTemplate.queryForList(
										"SELECT TZ_XXXKXZ_MC,TZ_IS_SELECTED FROM PS_TZ_DC_WJ_YBGZ_T WHERE TZ_DC_LJTJ_ID=? AND TZ_DC_WJ_ID=? AND TZ_XXX_BH=?",
										new Object[] { logicId, surveyId, mainItemId });
								if (subqList2 != null) {
									for (int x = 0; x < subqList2.size(); x++) {
										itemsMap = subqList2.get(x);
										wt_option = itemsMap.get("TZ_XXXKXZ_MC") == null ? ""
												: itemsMap.get("TZ_XXXKXZ_MC").toString();
										wt_opt_selected = itemsMap.get("TZ_IS_SELECTED") == null ? ""
												: itemsMap.get("TZ_IS_SELECTED").toString();

										/* 单选题和多选题 */
										if (storeType.equals("D")) {
											dateMap = null;
											dateMap = jdbcTemplate.queryForMap(
													"SELECT TZ_KXX_QTZ,TZ_IS_CHECKED FROM PS_TZ_DC_DHCC_T WHERE TZ_APP_INS_ID=? AND TZ_XXX_BH=? AND TZ_XXXKXZ_MC=?",
													new Object[] { surveyIns, mainItemId, wt_option });
											if (dateMap != null) {
												if (dateMap.containsKey("TZ_KXX_QTZ")
														&& dateMap.get("TZ_KXX_QTZ") != null) {
													opt_qtz = dateMap.get("TZ_KXX_QTZ").toString();
												}
												if (dateMap.containsKey("TZ_IS_CHECKED")
														&& dateMap.get("TZ_IS_CHECKED") != null) {
													opt_checked = dateMap.get("TZ_IS_CHECKED").toString();
												}

												if ((wt_opt_selected.equals("1") && opt_checked.equals("N"))
														|| (wt_opt_selected.equals("2") && opt_checked.equals("Y"))
														|| (wt_opt_selected.equals("1")
																&& StringUtils.isEmpty(opt_checked))) {
													/* 不满足 */
													if (isDisplay) {
														isDisplay = false;
													}
												}
											}
										}
										/* 单选题和多选题 结束 */
										/* 一般题型（填空题、下拉框等） */
										else {
											dateMap = null;
											dateMap = jdbcTemplate.queryForMap(
													"SELECT TZ_APP_S_TEXT,TZ_KXX_QTZ,TZ_APP_L_TEXT FROM PS_TZ_DC_CC_T WHERE TZ_APP_INS_ID=? AND TZ_XXX_BH=?",
													new Object[] { surveyIns, mainItemId });
											if (dateMap != null) {
												if (dateMap.containsKey("TZ_APP_S_TEXT")
														&& dateMap.get("TZ_APP_S_TEXT") != null) {
													yb_s_val = dateMap.get("TZ_APP_S_TEXT").toString();
												}
												if (dateMap.containsKey("TZ_KXX_QTZ")
														&& dateMap.get("TZ_KXX_QTZ") != null) {
													yb_o_val = dateMap.get("TZ_KXX_QTZ").toString();
												}
												if (dateMap.containsKey("TZ_APP_L_TEXT")
														&& dateMap.get("TZ_APP_L_TEXT") != null) {
													yb_l_val = dateMap.get("TZ_APP_L_TEXT").toString();
												}

												if (storeType.equals("T")) {
													if ((wt_opt_selected.equals("3") && StringUtils.isEmpty(yb_l_val))
															|| (wt_opt_selected.equals("4")
																	&& !StringUtils.isEmpty(yb_l_val))) {
														/* 不满足 */
														if (isDisplay) {
															isDisplay = false;
														}
													}
												} else {
													/* 下拉框 */
													if (className.equals("ComboBox")) {
														if (!wt_option.equals(yb_s_val)) {
															/* 不满足 */
															if (isDisplay) {
																isDisplay = false;
															}
														}
													} else {
														if ((wt_opt_selected.equals("3")
																&& StringUtils.isEmpty(yb_s_val))
																|| (wt_opt_selected.equals("4")
																		&& !StringUtils.isEmpty(yb_s_val))) {
															/* 不满足 */
															if (isDisplay) {
																isDisplay = false;
															}
														}
													}
												}
											}
										}
									}
								}
							}
							intlogicType = Integer.parseInt(logicType);
							switch (intlogicType) {
							/* 显示 */
							case 1:
								if (!isDisplay) {
									display = "N";
									break;
								}
								/* 隐藏 */
							case 2:
								if (isDisplay) {
									display = "N";
									break;
								}
							}
						}
					}
					if (!display.equals("N")) {
						display = "Y";
					}
				} else {
					display = "Y";
				}
				if (strItemsJson.equals("")) {
					strItemsJson = "\"" + itemId + "\":{\"display\":\"" + display + "\"}";
				} else {
					strItemsJson = strItemsJson + "," + "\"" + itemId + "\":{\"display\":\"" + display + "\"}";
				}
			}
		}

		return "{" + strItemsJson + "}";
	}

	/*
	 * =========================================================================
	 * ====================================== 功能描述：获取在线调查逻辑控制JSON报文 返回报文格式： {
	 * "itemId":[ { Conditions: [ {//规则条件 SubQuestion:'',//子问题 option:'',//选中值
	 * Filter:'', //过滤类型，【选中、未选择、填写、未填写】 }, ... ], Type:'1',//类型，【目前只有显示】
	 * logicId:'',//对应逻辑编号 Settings:[ { QuestName:'', //设置问题 oConditions:{
	 * //其他规则 "信息项ID"："逻辑规则ID", ... } }, .... ] } .... ], ... }
	 * =========================================================================
	 * ========================================
	 */
	public String getSurveyLogicJson(String surveyIns) {

		String str_wj_id = jdbcTemplate.queryForObject("SELECT TZ_DC_WJ_ID FROM PS_TZ_DC_INS_T WHERE TZ_APP_INS_ID= ?",
				new Object[] { surveyIns }, "String");
		String itemId = "";
		String strLogic = "";

		// 查询的SQL
		String sqlSubq = null;
		// 存放查询结果的list
		List<Map<String, Object>> qList = null;

		// 取数据用户的MAP
		Map<String, Object> itemsMap = null;
		// 结果用的MAP
		// Map<String, Object> tempMap = null;

		sqlSubq = "SELECT TZ_XXX_BH FROM PS_TZ_DCWJ_XXXPZ_T WHERE TZ_DC_WJ_ID=? ORDER BY TZ_ORDER";
		qList = jdbcTemplate.queryForList(sqlSubq, new Object[] { str_wj_id });

		/* 最外面的循环，遍历整个问卷的所有控件 Begin */
		if (qList != null) {
			List<Map<String, Object>> subqList = null;

			List<Map<String, Object>> subqList2 = null;

			List<Map<String, Object>> subqList3 = null;
			// String pageNum = null;
			String logicID = null;
			String logicType = null;
			String strItemLogic = "";
			String strConditions = ""; /* 逻辑规则条件 */
			String strSetings = "";
			String subQuestion;
			String option = null;
			String filter = null;

			String setQuest = null;

			String oLogicID = null, oItemId = null;
			String oConditions = "";
			for (int i = 0; i < qList.size(); i++) {
				// 每次循环初始化strItemLogic
				strItemLogic = "";
				itemsMap = qList.get(i);
				itemId = itemsMap.get("TZ_XXX_BH") == null ? "" : itemsMap.get("TZ_XXX_BH").toString();

				/* 第二重循环 遍历所有逻辑 Begin */
				sqlSubq = "SELECT TZ_DC_LJTJ_ID,TZ_LJ_LX,TZ_PAGE_NO FROM PS_TZ_DC_WJ_LJGZ_T WHERE TZ_DC_WJ_ID=? AND TZ_XXX_BH=?";
				subqList = jdbcTemplate.queryForList(sqlSubq, new Object[] { str_wj_id, itemId });
				if (subqList != null) {
					for (int j = 0; j < subqList.size(); j++) {
						itemsMap = subqList.get(j);
						logicID = itemsMap.get("TZ_DC_LJTJ_ID") == null ? "" : itemsMap.get("TZ_DC_LJTJ_ID").toString();
						logicType = itemsMap.get("TZ_LJ_LX") == null ? "" : itemsMap.get("TZ_LJ_LX").toString();
						// pageNum = itemsMap.get("TZ_PAGE_NO") == null ? "" :
						// itemsMap.get("TZ_PAGE_NO").toString();

						/* 一般题型逻辑规则 Begin */
						sqlSubq = "SELECT TZ_XXXKXZ_MC,TZ_IS_SELECTED FROM PS_TZ_DC_WJ_YBGZ_T WHERE TZ_DC_LJTJ_ID=? AND TZ_DC_WJ_ID=? AND TZ_XXX_BH=?";
						subqList2 = jdbcTemplate.queryForList(sqlSubq, new Object[] { logicID, str_wj_id, itemId });
						if (subqList2 != null) {
							for (int x = 0; x < subqList2.size(); x++) {
								itemsMap = subqList2.get(x);
								option = itemsMap.get("TZ_XXXKXZ_MC") == null ? ""
										: itemsMap.get("TZ_XXXKXZ_MC").toString();
								filter = itemsMap.get("TZ_IS_SELECTED") == null ? ""
										: itemsMap.get("TZ_IS_SELECTED").toString();

								if (strConditions.equals("")) {
									strConditions = strConditions + "{\"SubQuestion\":\"\",\"option\":\"" + option
											+ "\",\"Filter\":\"" + filter + "\"}";
								} else {
									strConditions = strConditions + "," + "{\"SubQuestion\":\"\",\"option\":\"" + option
											+ "\",\"Filter\":\"" + filter + "\"}";
								}
							}
						}
						/* 一般题型逻辑规则 End */

						/* 表格题型逻辑规则 Begin */
						sqlSubq = "SELECT TZ_XXXZWT_MC,TZ_XXXKXZ_MC,TZ_IS_SELECTED FROM PS_TZ_DC_WJ_GZGX_T WHERE TZ_DC_LJTJ_ID=? AND TZ_DC_WJ_ID=? AND TZ_XXX_BH=?";
						subqList2 = jdbcTemplate.queryForList(sqlSubq, new Object[] { logicID, str_wj_id, itemId });
						if (subqList2 != null) {
							for (int x = 0; x < subqList2.size(); x++) {
								itemsMap = subqList2.get(x);
								subQuestion = itemsMap.get("TZ_XXXZWT_MC") == null ? ""
										: itemsMap.get("TZ_XXXZWT_MC").toString();
								option = itemsMap.get("TZ_XXXKXZ_MC") == null ? ""
										: itemsMap.get("TZ_XXXKXZ_MC").toString();
								filter = itemsMap.get("TZ_IS_SELECTED") == null ? ""
										: itemsMap.get("TZ_IS_SELECTED").toString();

								if (strConditions.equals("")) {
									strConditions = strConditions + "{\"SubQuestion\":\"" + subQuestion
											+ "\",\"option\":\"" + option + "\",\"Filter\":\"" + filter + "\"}";
								} else {
									strConditions = strConditions + "," + "{\"SubQuestion\":\"" + subQuestion
											+ "\",\"option\":\"" + option + "\",\"Filter\":\"" + filter + "\"}";
								}
							}
						}
						/* 表格题型逻辑规则 End */

						/* 逻辑规则下的设置问题 Begin */
						sqlSubq = "SELECT TZ_XXX_BH FROM PS_TZ_DC_WJ_LJXS_T WHERE TZ_DC_LJTJ_ID=? AND TZ_DC_WJ_ID=?";
						subqList2 = jdbcTemplate.queryForList(sqlSubq, new Object[] { logicID, str_wj_id });
						if (subqList2 != null) {
							for (int x = 0; x < subqList2.size(); x++) {
								itemsMap = subqList2.get(x);
								setQuest = itemsMap.get("TZ_XXX_BH") == null ? ""
										: itemsMap.get("TZ_XXX_BH").toString();

								/* 其他关联逻辑 Begin */
								sqlSubq = "SELECT A.TZ_DC_LJTJ_ID,B.TZ_XXX_BH FROM PS_TZ_DC_WJ_LJXS_T A left JOIN PS_TZ_DC_WJ_LJGZ_T B on (A.TZ_DC_LJTJ_ID=B.TZ_DC_LJTJ_ID) WHERE A.TZ_XXX_BH=? AND A.TZ_DC_WJ_ID=? AND A.TZ_DC_LJTJ_ID<>?";
								subqList3 = jdbcTemplate.queryForList(sqlSubq,
										new Object[] { setQuest, str_wj_id, logicID });
								if (subqList3 != null) {
									for (int y = 0; y < subqList3.size(); y++) {
										itemsMap = subqList3.get(y);
										oLogicID = itemsMap.get("TZ_DC_LJTJ_ID") == null ? ""
												: itemsMap.get("TZ_DC_LJTJ_ID").toString();
										oItemId = itemsMap.get("TZ_XXX_BH") == null ? ""
												: itemsMap.get("TZ_XXX_BH").toString();
										if (oConditions.equals("")) {
											oConditions = oConditions + "\"\"" + oItemId + "\"\":\"\"" + oLogicID
													+ "\"\"";
										} else {
											oConditions = oConditions + ",\"\"" + oItemId + "\"\":\"\"" + oLogicID
													+ "\"\"";
										}
									}
								}
								/* 其他关联逻辑 End */

								if (strSetings.equals("")) {
									strSetings = strSetings + "{\"QuestName\":\"" + setQuest + "\",\"oConditions\":{"
											+ oConditions + "}}";
								} else {
									strSetings = strSetings + "," + "{\"QuestName\":\"" + setQuest
											+ "\",\"oConditions\":{" + oConditions + "}}";
								}
							}
						}
						/* 逻辑规则下的设置问题 End */

						if (strItemLogic.equals("")) {
							strItemLogic = strItemLogic + "{\"Conditions\":[" + strConditions + "],\"Type\":\""
									+ logicType + "\",\"logicId\":\"" + logicID + "\",\"Settings\":[" + strSetings
									+ "]}";
						} else {
							strItemLogic = strItemLogic + "," + "{\"Conditions\":[" + strConditions + "],\"Type\":\""
									+ logicType + "\",\"logicId\":\"" + logicID + "\",\"Settings\":[" + strSetings
									+ "]}";
						}
					}
				}
				/* 第二重循环 遍历所有逻辑 End */
				if (!strItemLogic.equals("")) {
					if (strLogic.equals("")) {
						strLogic = strLogic + "\"" + itemId + "\":[" + strItemLogic + "]";
					} else {
						strLogic = strLogic + "," + "\"" + itemId + "\":[" + strItemLogic + "]";
					}
				}
			}
		}
		/* 最外面的循环，遍历整个问卷的所有控件 End */
		strLogic = "{" + strLogic + "}";
		return strLogic;
	}

	public String getSurveyLogicJson2(String surveyId) {

		String itemId = "";
		String strLogic = "";
		String str_wj_id = surveyId;
		// 查询的SQL
		String sqlSubq = null;
		// 存放查询结果的list
		List<Map<String, Object>> qList = null;

		// 取数据用户的MAP
		Map<String, Object> itemsMap = null;
		// 结果用的MAP
		// Map<String, Object> tempMap = null;

		sqlSubq = "SELECT TZ_XXX_BH FROM PS_TZ_DCWJ_XXXPZ_T WHERE TZ_DC_WJ_ID=? ORDER BY TZ_ORDER";
		qList = jdbcTemplate.queryForList(sqlSubq, new Object[] { str_wj_id });

		/* 最外面的循环，遍历整个问卷的所有控件 Begin */
		if (qList != null) {
			List<Map<String, Object>> subqList = null;

			List<Map<String, Object>> subqList2 = null;

			List<Map<String, Object>> subqList3 = null;
			// String pageNum = null;
			String logicID = null;
			String logicType = null;
			String strItemLogic = "";
			String strConditions = ""; /* 逻辑规则条件 */
			String strSetings = "";
			String subQuestion;
			String option = null;
			String filter = null;

			String setQuest = null;

			String oLogicID = null, oItemId = null;
			String oConditions = "";
			for (int i = 0; i < qList.size(); i++) {
				// 每次循环初始化strItemLogic
				strItemLogic = "";
				itemsMap = qList.get(i);
				itemId = itemsMap.get("TZ_XXX_BH") == null ? "" : itemsMap.get("TZ_XXX_BH").toString();

				/* 第二重循环 遍历所有逻辑 Begin */
				sqlSubq = "SELECT TZ_DC_LJTJ_ID,TZ_LJ_LX,TZ_PAGE_NO FROM PS_TZ_DC_WJ_LJGZ_T WHERE TZ_DC_WJ_ID=? AND TZ_XXX_BH=?";
				subqList = jdbcTemplate.queryForList(sqlSubq, new Object[] { str_wj_id, itemId });
				if (subqList != null) {
					for (int j = 0; j < subqList.size(); j++) {
						itemsMap = subqList.get(j);
						logicID = itemsMap.get("TZ_DC_LJTJ_ID") == null ? "" : itemsMap.get("TZ_DC_LJTJ_ID").toString();
						logicType = itemsMap.get("TZ_LJ_LX") == null ? "" : itemsMap.get("TZ_LJ_LX").toString();
						// pageNum = itemsMap.get("TZ_PAGE_NO") == null ? "" :
						// itemsMap.get("TZ_PAGE_NO").toString();

						/* 一般题型逻辑规则 Begin */
						sqlSubq = "SELECT TZ_XXXKXZ_MC,TZ_IS_SELECTED FROM PS_TZ_DC_WJ_YBGZ_T WHERE TZ_DC_LJTJ_ID=? AND TZ_DC_WJ_ID=? AND TZ_XXX_BH=?";
						subqList2 = jdbcTemplate.queryForList(sqlSubq, new Object[] { logicID, str_wj_id, itemId });
						if (subqList2 != null) {
							for (int x = 0; x < subqList2.size(); x++) {
								itemsMap = subqList2.get(x);
								option = itemsMap.get("TZ_XXXKXZ_MC") == null ? ""
										: itemsMap.get("TZ_XXXKXZ_MC").toString();
								filter = itemsMap.get("TZ_IS_SELECTED") == null ? ""
										: itemsMap.get("TZ_IS_SELECTED").toString();

								if (strConditions.equals("")) {
									strConditions = strConditions + "{\"SubQuestion\":\"\",\"option\":\"" + option
											+ "\",\"Filter\":\"" + filter + "\"}";
								} else {
									strConditions = strConditions + "," + "{\"SubQuestion\":\"\",\"option\":\"" + option
											+ "\",\"Filter\":\"" + filter + "\"}";
								}
							}
						}
						/* 一般题型逻辑规则 End */

						/* 表格题型逻辑规则 Begin */
						sqlSubq = "SELECT TZ_XXXZWT_MC,TZ_XXXKXZ_MC,TZ_IS_SELECTED FROM PS_TZ_DC_WJ_GZGX_T WHERE TZ_DC_LJTJ_ID=? AND TZ_DC_WJ_ID=? AND TZ_XXX_BH=?";
						subqList2 = jdbcTemplate.queryForList(sqlSubq, new Object[] { logicID, str_wj_id, itemId });
						if (subqList2 != null) {
							for (int x = 0; x < subqList2.size(); x++) {
								itemsMap = subqList2.get(x);
								subQuestion = itemsMap.get("TZ_XXXZWT_MC") == null ? ""
										: itemsMap.get("TZ_XXXZWT_MC").toString();
								option = itemsMap.get("TZ_XXXKXZ_MC") == null ? ""
										: itemsMap.get("TZ_XXXKXZ_MC").toString();
								filter = itemsMap.get("TZ_IS_SELECTED") == null ? ""
										: itemsMap.get("TZ_IS_SELECTED").toString();

								if (strConditions.equals("")) {
									strConditions = strConditions + "{\"SubQuestion\":\"" + subQuestion
											+ "\",\"option\":\"" + option + "\",\"Filter\":\"" + filter + "\"}";
								} else {
									strConditions = strConditions + "," + "{\"SubQuestion\":\"" + subQuestion
											+ "\",\"option\":\"" + option + "\",\"Filter\":\"" + filter + "\"}";
								}
							}
						}
						/* 表格题型逻辑规则 End */

						/* 逻辑规则下的设置问题 Begin */
						sqlSubq = "SELECT TZ_XXX_BH FROM PS_TZ_DC_WJ_LJXS_T WHERE TZ_DC_LJTJ_ID=? AND TZ_DC_WJ_ID=?";
						subqList2 = jdbcTemplate.queryForList(sqlSubq, new Object[] { logicID, str_wj_id });
						if (subqList2 != null) {
							for (int x = 0; x < subqList2.size(); x++) {
								itemsMap = subqList2.get(x);
								setQuest = itemsMap.get("TZ_XXX_BH") == null ? ""
										: itemsMap.get("TZ_XXX_BH").toString();

								/* 其他关联逻辑 Begin */
								sqlSubq = "SELECT A.TZ_DC_LJTJ_ID,B.TZ_XXX_BH FROM PS_TZ_DC_WJ_LJXS_T A left JOIN PS_TZ_DC_WJ_LJGZ_T B on (A.TZ_DC_LJTJ_ID=B.TZ_DC_LJTJ_ID) WHERE A.TZ_XXX_BH=? AND A.TZ_DC_WJ_ID=? AND A.TZ_DC_LJTJ_ID<>?";
								subqList3 = jdbcTemplate.queryForList(sqlSubq,
										new Object[] { setQuest, str_wj_id, logicID });
								if (subqList3 != null) {
									for (int y = 0; y < subqList3.size(); y++) {
										itemsMap = subqList3.get(y);
										oLogicID = itemsMap.get("TZ_DC_LJTJ_ID") == null ? ""
												: itemsMap.get("TZ_DC_LJTJ_ID").toString();
										oItemId = itemsMap.get("TZ_XXX_BH") == null ? ""
												: itemsMap.get("TZ_XXX_BH").toString();
										if (oConditions.equals("")) {
											oConditions = oConditions + "\"\"" + oItemId + "\"\":\"\"" + oLogicID
													+ "\"\"";
										} else {
											oConditions = oConditions + ",\"\"" + oItemId + "\"\":\"\"" + oLogicID
													+ "\"\"";
										}
									}
								}
								/* 其他关联逻辑 End */

								if (strSetings.equals("")) {
									strSetings = strSetings + "{\"QuestName\":\"" + setQuest + "\",\"oConditions\":{"
											+ oConditions + "}}";
								} else {
									strSetings = strSetings + "," + "{\"QuestName\":\"" + setQuest
											+ "\",\"oConditions\":{" + oConditions + "}}";
								}
							}
						}
						/* 逻辑规则下的设置问题 End */

						if (strItemLogic.equals("")) {
							strItemLogic = strItemLogic + "{\"Conditions\":[" + strConditions + "],\"Type\":\""
									+ logicType + "\",\"logicId\":\"" + logicID + "\",\"Settings\":[" + strSetings
									+ "]}";
						} else {
							strItemLogic = strItemLogic + "," + "{\"Conditions\":[" + strConditions + "],\"Type\":\""
									+ logicType + "\",\"logicId\":\"" + logicID + "\",\"Settings\":[" + strSetings
									+ "]}";
						}
					}
				}
				/* 第二重循环 遍历所有逻辑 End */
				if (!strItemLogic.equals("")) {
					if (strLogic.equals("")) {
						strLogic = strLogic + "\"" + itemId + "\":[" + strItemLogic + "]";
					} else {
						strLogic = strLogic + "," + "\"" + itemId + "\":[" + strItemLogic + "]";
					}
				}
			}
		}
		/* 最外面的循环，遍历整个问卷的所有控件 End */
		strLogic = "{" + strLogic + "}";
		return strLogic;
	}

}
