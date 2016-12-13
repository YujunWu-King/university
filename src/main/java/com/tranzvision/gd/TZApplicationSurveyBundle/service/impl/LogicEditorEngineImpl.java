package com.tranzvision.gd.TZApplicationSurveyBundle.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcMbGzgxTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcMbLjgzTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcMbLjxsTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcMbYbgzTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcWjGzgxTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcWjLjgzTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcWjLjxsTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcWjYbgzTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcMbGzgxT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcMbLjgzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcMbLjxsTKey;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcMbYbgzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjGzgxT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjLjgzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjLjxsTKey;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjYbgzT;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.TZApplicationSurveyBundle.service.impl.LogicEditorEngineImpl")
public class LogicEditorEngineImpl extends FrameworkImpl {
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzDcMbLjgzTMapper psTzDcMbLjgzTMapper;
	@Autowired
	private PsTzDcWjLjgzTMapper psTzDcWjLjgzTMapper;
	@Autowired
	private PsTzDcMbGzgxTMapper psTzDcMbGzgxTMapper;
	@Autowired
	private PsTzDcWjGzgxTMapper psTzDcWjGzgxTMapper;
	@Autowired
	private PsTzDcMbLjxsTMapper psTzDcMbLjxsTMapper;
	@Autowired
	private PsTzDcWjLjxsTMapper psTzDcWjLjxsTMapper;
	@Autowired
	private PsTzDcMbYbgzTMapper psTzDcMbYbgzTMapper;
	@Autowired
	private PsTzDcWjYbgzTMapper psTzDcWjYbgzTMapper;
	@Autowired
	private SqlQuery jdbcTemplate;

	// 传地址 和传值问题
	/* 初始化问卷调查模板逻辑规则 */
	public String init(String tplId) {
		System.out.println("==模板init()方法执行==");
		String strHtml = "";
		String strItemJson = "";
		String strGlItemJson = "";
		String strLogicJson = "";

		String com = "TZ_ZXDC_MBGL_COM";
		String page = "TZ_ZXDC_LJGZ_STD";

		String tzGeneralURL = request.getContextPath() + "/dispatcher";
		String URL=request.getContextPath();
		Map<String, Object> resultMap = this.getItemsJson(tplId, strItemJson, strGlItemJson);
		if (resultMap.get("tplId") != null)
			tplId = resultMap.get("tplId").toString();
		if (resultMap.get("mItemsJson") != null)
			strItemJson = resultMap.get("mItemsJson").toString();
		if (resultMap.get("glItemsJson") != null)
			strGlItemJson = resultMap.get("glItemsJson").toString();
		strLogicJson = this.getLogicJson(tplId);
		
		System.out.println("===传到前台数据strLogicJson：==" + strLogicJson);
		System.out.println("===传到前台数据strItemJson：==" + strItemJson);
		System.out.println("===传到前台数据strGlItemJson：==" + strGlItemJson);
		try {
			strHtml = tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_DC_LOGIC_DEFN_HTML", tzGeneralURL,
					tplId, strItemJson, strGlItemJson, strLogicJson, com, page,URL);
		} catch (TzSystemException e) {
			strHtml = "";
			e.printStackTrace();
		}
		return strHtml;
	}

	/* 保存逻辑规则定义 */
	@Transactional
	@SuppressWarnings("unchecked")
	public String saveLogicDefn(String tplId, List<Map<String, Object>> jsonDataList, String type) {

		int i, j;
		String isSubQue = null;
		String successFlag = "0";
		String strMsg = null;

		String itemId = null;
		String sItemId = null;
		String option = null;
		String checked = null;
		String orderBy = null;
		String logicType = null;

		Map<String, Object> logic;
		List<Map<String, Object>> subQuestion;
		Map<String, Object> subQuetObj;
		List<Map<String, Object>> relatedQ;
		Map<String, Object> relQuestObj;

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			/* 存放逻辑规则定义查重字符串 */
			List<Object> reptArr = new ArrayList<Object>();

			if (jsonDataList != null && tplId != null) {
				String strRept = "";
				// 前台数据放入的LIST
				List<String> subArray = null;

				// 关联问题数组;
				List<String> glItemArr = null;
				// 第一部分校验规则是否重复
				for (i = 0; i < jsonDataList.size(); i++) {
					logic = jsonDataList.get(i);
					subArray = new ArrayList<String>();
					// 关联问题数组;
					glItemArr = new ArrayList<String>();
					/* 信息项编号 */
					if (logic.containsKey("itemId")) {
						itemId = logic.get("itemId").toString();
					}
					/* 序号 */
					if (logic.containsKey("orderBy") && logic.get("orderBy") != null) {
						orderBy = logic.get("orderBy").toString();
					}
					/* 逻辑类型 */
					if (logic.containsKey("type") && logic.get("type") != null) {
						logicType = logic.get("type").toString();
					}
					// 放入数据1
					if (itemId != null && !itemId.equals("")) {
						subArray.add(itemId + "-" + logicType);
					}
					// -----------------------------------------------------------------
					/* 子问题及可选项 */
					if (logic.containsKey("subQuestion") && logic.get("subQuestion") != null) {
						subQuestion = (List<Map<String, Object>>) logic.get("subQuestion");
						for (j = 0; j < subQuestion.size(); j++) {
							subQuetObj = subQuestion.get(j);

							if (subQuetObj.containsKey("sItemId")) {
								sItemId = subQuetObj.get("sItemId").toString();
							}
							if (subQuetObj.containsKey("option") && subQuetObj.get("option") != null) {
								option = subQuetObj.get("option").toString();
							}
							if (subQuetObj.containsKey("checked") && subQuetObj.get("checked") != null) {
								checked = subQuetObj.get("checked").toString();
							}
							// 放入数据2
							if (sItemId != null && !sItemId.equals("")) {
								subArray.add(sItemId + "--" + option + "--" + checked);
							}
						}
						// 排序
						Collections.sort(subArray);
						strRept = "";
						for (int index = 0; index < subArray.size(); index++) {
							strRept = strRept + subArray.get(index) + ">";
						}

						System.out.println("strRept=" + strRept);
						int rtnNum = 0;
						if (reptArr.contains(strRept)) {
							for (int index = 0; index < reptArr.size(); index++) {
								if (reptArr.get(index).equals(strRept)) {
									rtnNum++;
								}
							}
						}
						if (rtnNum == 0) {
							reptArr.add(strRept);
						} else {
							successFlag = "1";
							strMsg = "逻辑" + orderBy + "规则定义重复！";
							System.out.println("逻辑" + orderBy + "规则重复定义");
							break;
						}
					}
					// -------------------------------------------------
					/* 关联问题 */
					if (logic.containsKey("relatedQ") && logic.get("relatedQ") != null) {
						relatedQ = (List<Map<String, Object>>) logic.get("relatedQ");
						boolean boolGl = false;
						for (j = 0; j < relatedQ.size(); j++) {
							relQuestObj = relatedQ.get(j);

							String glItemId = null;/* 关联问题编号 */
							if (relQuestObj.containsKey("itemId")) {
								glItemId = relQuestObj.get("itemId").toString();
							}

							System.out.println("glItemId=" + glItemId);
							int count = 0;
							if (glItemArr.contains(glItemId)) {
								for (int index = 0; index < glItemArr.size(); index++) {
									if (glItemId.equals(glItemArr.get(index))) {
										count++;
									}
								}
							}
							if (count == 0) {
								glItemArr.add(glItemId);
							} else {
								successFlag = "1";
								strMsg = "逻辑" + orderBy + "关联问题定义重复！";
								boolGl = true;
								break;
							}
						}
						if (boolGl) {
							break;
						}
					}
				}
			} else {
				successFlag = "1";
				strMsg = "参数不正确！";
				// System.out.println("==223==参数不正确");
			}
			// ======================================

			// 第二部分 做数据库操作
			if (successFlag.equals("0")) {
				// 先删除多有逻辑
				// this.delLogic(tplId, type);
				System.out.println("先删除多有逻辑：type=" + type);
				if (type.equals("MB")) { // 删除模版的逻辑
					jdbcTemplate.update("DELETE FROM PS_TZ_DC_MB_LJXS_T WHERE TZ_APP_TPL_ID=?", new Object[] { tplId });
					jdbcTemplate.update("DELETE FROM PS_TZ_DC_MB_GZGX_T WHERE TZ_APP_TPL_ID=?", new Object[] { tplId });
					jdbcTemplate.update("DELETE FROM PS_TZ_DC_MB_YBGZ_T WHERE TZ_APP_TPL_ID=?", new Object[] { tplId });
					jdbcTemplate.update("DELETE FROM PS_TZ_DC_MB_LJGZ_T WHERE TZ_APP_TPL_ID=?", new Object[] { tplId });
				} else { // 删除问卷的逻辑
					jdbcTemplate.update("DELETE FROM PS_TZ_DC_WJ_LJXS_T WHERE TZ_DC_WJ_ID=?", new Object[] { tplId });
					jdbcTemplate.update("DELETE FROM PS_TZ_DC_WJ_GZGX_T WHERE TZ_DC_WJ_ID=?", new Object[] { tplId });
					jdbcTemplate.update("DELETE FROM PS_TZ_DC_WJ_YBGZ_T WHERE TZ_DC_WJ_ID=?", new Object[] { tplId });
					jdbcTemplate.update("DELETE FROM PS_TZ_DC_WJ_LJGZ_T WHERE TZ_DC_WJ_ID=?", new Object[] { tplId });
				}
				System.out.println("删除多有逻辑结束");
				// boolean isNewLogic = false;
				// 操作类型
				String optype = "";
				itemId = "";
				orderBy = "";
				for (i = 0; i < jsonDataList.size(); i++) {
					String logicalId = null;

					/* 逻辑规则定义 */
					logic = jsonDataList.get(i);
					/* 在线调查模板逻辑规则定义表 */
					PsTzDcMbLjgzT psTzDcMbLjgzT = null;
					PsTzDcWjLjgzT psTzDcWjLjgzT = null;
					if (type.equals("MB")) {// 模版表
						psTzDcMbLjgzT = new PsTzDcMbLjgzT();
					} else { // 问卷表
						psTzDcWjLjgzT = new PsTzDcWjLjgzT();
					}
					/* 逻辑规则编号 */
					if (logic.containsKey("logicalId") && logic.get("logicalId") != null) {
						logicalId = logic.get("logicalId").toString();
					}
					// 信息项编号
					if (logic.containsKey("itemId") && logic.get("itemId") != null) {
						itemId = logic.get("itemId").toString();
					}
					/* 逻辑类型 */
					if (logic.containsKey("type") && logic.get("type") != null) {
						optype = logic.get("type").toString();
					}
					/* 序号 */
					if (logic.containsKey("orderBy") && logic.get("orderBy") != null) {
						orderBy = logic.get("orderBy").toString();
					}

					if (logicalId == null || logicalId.equals("")) {
						// isNewLogic = true;
						if (type.equals("MB")) {
							logicalId = "L" + getSeqNum.getSeqNum("TZ_DC_MB_LJGZ_T", "TZ_DC_LJTJ_ID");
						} else {
							logicalId = "L" + getSeqNum.getSeqNum("TZ_DC_WJ_LJGZ_T", "TZ_DC_LJTJ_ID");
						}
					}
					System.out.println("logicalId:" + logicalId);
					// 入库 逻辑规则表
					if (type.equals("MB")) {
						psTzDcMbLjgzT.setTzAppTplId(tplId);
						// 信息项编号
						psTzDcMbLjgzT.setTzXxxBh(itemId);
						/* 逻辑类型 */
						psTzDcMbLjgzT.setTzLjLx(optype);
						/* 序号 */
						psTzDcMbLjgzT.setTzLjtjXh(Integer.valueOf(orderBy));
						// 主键
						psTzDcMbLjgzT.setTzDcLjtjId(logicalId);

						psTzDcMbLjgzT.setTzPageNo(new Integer(0));
						// 将上述数据插入数据库
						psTzDcMbLjgzTMapper.insertSelective(psTzDcMbLjgzT);
					} else {
						psTzDcWjLjgzT.setTzDcWjId(tplId);
						// 信息项编号
						psTzDcWjLjgzT.setTzXxxBh(itemId);
						/* 逻辑类型 */
						psTzDcWjLjgzT.setTzLjLx(optype);
						/* 序号 */
						psTzDcWjLjgzT.setTzLjtjXh(Integer.valueOf(orderBy));

						psTzDcWjLjgzT.setTzPageNo(new Integer(0));
						// 主键
						psTzDcWjLjgzT.setTzDcLjtjId(logicalId);
						// 将上述数据插入数据库
						psTzDcWjLjgzTMapper.insertSelective(psTzDcWjLjgzT);
					}

					/* 规则逻辑问题类型 */
					if (logic.containsKey("isSubQue") && logic.get("isSubQue") != null) {
						isSubQue = logic.get("isSubQue").toString();
					}
					/* 子问题及可选项 */
					if (logic.containsKey("subQuestion") && logic.get("subQuestion") != null) {
						subQuestion = (List<Map<String, Object>>) logic.get("subQuestion");
						if (subQuestion != null) {
							for (j = 0; j < subQuestion.size(); j++) {
								subQuetObj = subQuestion.get(j);
								if (subQuetObj.containsKey("sItemId")) {
									sItemId = subQuetObj.get("sItemId").toString();
								}
								if (subQuetObj.containsKey("option") && subQuetObj.get("option") != null) {
									option = subQuetObj.get("option").toString();
								}
								if (subQuetObj.containsKey("checked") && subQuetObj.get("checked") != null) {
									checked = subQuetObj.get("checked").toString();
								}

								if (isSubQue.equals("Y")) { // 表格多选题,表格单选题,课程评估里面的逻辑关系
									PsTzDcMbGzgxT psTzDcMbGzgxT = null;
									PsTzDcWjGzgxT psTzDcWjGzgxT = null;
									if (type.equals("MB")) {
										psTzDcMbGzgxT = new PsTzDcMbGzgxT();
										psTzDcMbGzgxT.setTzAppTplId(tplId);
										psTzDcMbGzgxT.setTzDcLjtjId(logicalId);
										psTzDcMbGzgxT.setTzXxxBh(itemId);
										psTzDcMbGzgxT.setTzXxxzwtMc(sItemId);
										psTzDcMbGzgxT.setTzXxxkxzMc(option);
										psTzDcMbGzgxT.setTzIsSelected(checked);
										psTzDcMbGzgxTMapper.insertSelective(psTzDcMbGzgxT);
									} else {
										psTzDcWjGzgxT = new PsTzDcWjGzgxT();
										psTzDcWjGzgxT.setTzDcWjId(tplId);
										psTzDcWjGzgxT.setTzDcLjtjId(logicalId);
										psTzDcWjGzgxT.setTzXxxBh(itemId);
										psTzDcWjGzgxT.setTzXxxzwtMc(sItemId);
										psTzDcWjGzgxT.setTzXxxkxzMc(option);
										psTzDcWjGzgxT.setTzIsSelected(checked);
										psTzDcWjGzgxTMapper.insertSelective(psTzDcWjGzgxT);

									}
								} else { // 其他逻辑关系
									if (type.equals("MB")) {
										PsTzDcMbYbgzT psTzDcMbYbgzT = new PsTzDcMbYbgzT();
										psTzDcMbYbgzT.setTzAppTplId(tplId);
										psTzDcMbYbgzT.setTzDcLjtjId(logicalId);
										psTzDcMbYbgzT.setTzXxxBh(itemId);
										psTzDcMbYbgzT.setTzXxxkxzMc(option);
										psTzDcMbYbgzT.setTzIsSelected(checked);
										psTzDcMbYbgzTMapper.insertSelective(psTzDcMbYbgzT);
									} else {
										PsTzDcWjYbgzT psTzDcWjYbgzT = new PsTzDcWjYbgzT();
										psTzDcWjYbgzT.setTzDcWjId(tplId);
										psTzDcWjYbgzT.setTzDcLjtjId(logicalId);
										psTzDcWjYbgzT.setTzXxxBh(itemId);
										psTzDcWjYbgzT.setTzXxxkxzMc(option);
										psTzDcWjYbgzT.setTzIsSelected(checked);
										psTzDcWjYbgzTMapper.insertSelective(psTzDcWjYbgzT);
									}
								}
							}
						}
					}
					/* 子问题及可选项 */
					if (logic.containsKey("relatedQ") && logic.get("relatedQ") != null) {
						relatedQ = (List<Map<String, Object>>) logic.get("relatedQ");
						if (relatedQ != null) {
							for (j = 0; j < relatedQ.size(); j++) {
								relQuestObj = relatedQ.get(j);

								String relItemId = null;/* 关联问题编号 */
								if (relQuestObj.containsKey("itemId")) {
									relItemId = relQuestObj.get("itemId").toString();
								}

								/* 在线调查模板关联显示逻辑关系表 */
								if (type.equals("MB")) {
									PsTzDcMbLjxsTKey psTzDcMbLjxsTKey = new PsTzDcMbLjxsTKey();
									psTzDcMbLjxsTKey.setTzAppTplId(tplId);
									psTzDcMbLjxsTKey.setTzDcLjtjId(logicalId);
									psTzDcMbLjxsTKey.setTzXxxBh(relItemId);
									psTzDcMbLjxsTMapper.insertSelective(psTzDcMbLjxsTKey);
								} else {
									PsTzDcWjLjxsTKey psTzDcWjLjxsTkey = new PsTzDcWjLjxsTKey();
									psTzDcWjLjxsTkey.setTzDcWjId(tplId);
									psTzDcWjLjxsTkey.setTzDcLjtjId(logicalId);
									psTzDcWjLjxsTkey.setTzXxxBh(relItemId);
									psTzDcWjLjxsTMapper.insertSelective(psTzDcWjLjxsTkey);
								}
							}
						}
					}
					// -----------------------------------
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			successFlag = "1";
			strMsg = e.toString();
		}
		Map<String, Object> returnMap = new HashMap<String, Object>();
		System.out.println("successFlag:" + successFlag);
		System.out.println("strMsg:" + strMsg);
		returnMap.put("errorCode", successFlag);
		returnMap.put("errorMsg", strMsg);
		return jacksonUtil.Map2json(returnMap);
	}

	/* 获取问卷模板信息项JSON数据 */
	public Map<String, Object> getItemsJson(String tplId, String mItemsJson, String glItemsJson) {
		System.out.println("==模板getItemsJson()方法执行==");
		
		String itemID=null;
		String itemName=null;
		String itemTitle=null;
		String className=null;

		String sqlGlItems=null;
		int count = 0;
		int order=0;
		String strQid=null;
		String itemDesc=null;

		JacksonUtil jacksonUtil = new JacksonUtil();
		String lan = jdbcTemplate.queryForObject("SELECT TZ_APP_TPL_LAN FROM PS_TZ_DC_DY_T WHERE TZ_APP_TPL_ID=?",
				new Object[] { tplId }, "String");
		final String sqlItems = "SELECT TZ_XXX_BH,TZ_XXX_MC,TZ_TITLE,TZ_COM_LMC,TZ_ORDER,TZ_XXX_QID FROM PS_TZ_DC_XXXPZ_T WHERE TZ_APP_TPL_ID=? AND TZ_XXX_CCLX IN('S','D','T') AND TZ_COM_LMC NOT IN('MultipleTextBox') ORDER BY TZ_ORDER";
		List<Map<String, Object>> itemsList = new ArrayList<Map<String, Object>>();
		itemsList = jdbcTemplate.queryForList(sqlItems, new Object[] { tplId });
		if (itemsList != null) {
			for (int i = 0; i < itemsList.size(); i++) {
				Map<String, Object> itemMap = itemsList.get(i);
				itemID = itemMap.get("TZ_XXX_BH") == null ? "" : itemMap.get("TZ_XXX_BH").toString();
				itemName = itemMap.get("TZ_XXX_MC") == null ? "" : itemMap.get("TZ_XXX_MC").toString();
				itemTitle = itemMap.get("TZ_TITLE") == null ? "" : itemMap.get("TZ_TITLE").toString();
				className = itemMap.get("TZ_COM_LMC") == null ? "" : itemMap.get("TZ_COM_LMC").toString();
				order = itemMap.get("TZ_ORDER") == null ? null : Integer.valueOf(itemMap.get("TZ_ORDER").toString());
				strQid = itemMap.get("TZ_XXX_QID") == null ? "" : itemMap.get("TZ_XXX_QID").toString();

				int isSub=0;/* 是子问题控制题型 */
				int isOpt=0;

				itemDesc = strQid + "." + itemName;
				if (lan.equals("ZHS"))// 中文
				{
					if (itemDesc.length() > 50) {
						itemDesc = itemDesc.substring(1, 50) + "...";
					}
				} else {
					if (itemDesc.length() > 100) {
						itemDesc = itemDesc.substring(1, 100) + "...";
					}
				}
				// &itemDesc = &UtilObj.Transformchar(&itemDesc);
				final String SQL1 = "SELECT COUNT('Y') FROM PS_TZ_DC_BGT_ZWT_T WHERE TZ_APP_TPL_ID=? AND TZ_XXX_BH=?";
				isSub = jdbcTemplate.queryForObject(SQL1, new Object[] { tplId, itemID }, "int");
				final String SQL2 = "SELECT COUNT('Y') FROM PS_TZ_DC_XXX_KXZ_T WHERE  TZ_APP_TPL_ID=? AND TZ_XXX_BH=?";
				isOpt = jdbcTemplate.queryForObject(SQL2, new Object[] { tplId, itemID }, "int");
				System.out.println("====isSub:"+isSub+"===isOpt:"+isOpt);
				count++;
				if (isSub>0) {
					String sub_name, sub_desc;
					// String subQuestion = "";
					List<Map<String, Object>> subQuestionDataList = new ArrayList<Map<String, Object>>();
					Map<String,Object>tempMap=null;
					final String sqlSubq = "SELECT TZ_QU_CODE,TZ_QU_NAME FROM PS_TZ_DC_BGT_ZWT_T WHERE TZ_APP_TPL_ID=? AND TZ_XXX_BH=? ORDER BY TZ_ORDER";
					List<Map<String, Object>> subqList = new ArrayList<Map<String, Object>>();
					subqList = jdbcTemplate.queryForList(sqlSubq, new Object[] { tplId, itemID });
					if (subqList != null) {
						for (int j = 0; j < subqList.size(); j++) {
							Map<String, Object> subqMap = new HashMap<String, Object>();
							subqMap = subqList.get(j);

							sub_name = subqMap.get("TZ_QU_CODE") == null ? "" : subqMap.get("TZ_QU_CODE").toString();
							sub_desc = subqMap.get("TZ_QU_NAME") == null ? "" : subqMap.get("TZ_QU_NAME").toString();
							
							tempMap=new HashMap<String,Object>();
							tempMap.put("sItemId", sub_name);
							tempMap.put("sItemName", sub_desc);
							subQuestionDataList.add(tempMap);
						}
					}
					//
					String sOpt_name, sOpt_desc, sOpt_order;
					// String subOption = "";
					List<Map<String, Object>> optionDataList = new ArrayList<Map<String, Object>>();
					final String sqlSubOption = "SELECT TZ_OPT_CODE,TZ_OPT_NAME,TZ_ORDER FROM PS_TZ_DC_BGT_KXZ_T WHERE TZ_APP_TPL_ID=? AND TZ_XXX_BH=? ORDER BY TZ_ORDER";

					List<Map<String, Object>> subOptionList = new ArrayList<Map<String, Object>>();
					subOptionList = jdbcTemplate.queryForList(sqlSubOption, new Object[] { tplId, itemID });
					if (subOptionList != null) {
						for (int j = 0; j < subOptionList.size(); j++) {
							Map<String, Object> subOptionMap = new HashMap<String, Object>();
							subOptionMap = subOptionList.get(j);

							sOpt_name = subOptionMap.get("TZ_OPT_CODE") == null ? ""
									: subOptionMap.get("TZ_OPT_CODE").toString();
							sOpt_desc = subOptionMap.get("TZ_OPT_NAME") == null ? ""
									: subOptionMap.get("TZ_OPT_NAME").toString();
							sOpt_order = subOptionMap.get("TZ_ORDER") == null ? ""
									: subOptionMap.get("TZ_ORDER").toString();
							Map<String, Object> optionMap = new HashMap<String, Object>();
							optionMap.put("sOptId", sOpt_order);
							optionMap.put("sOptName", sOpt_name);
							optionMap.put("sOptDesc", sOpt_desc);
							// if(subOption.equals("")){
							// subOption=jacksonUtil.Map2json(optionMap);
							// }
							// else{
							// subOption=subOption+","+jacksonUtil.Map2json(optionMap);
							// }
							optionDataList.add(optionMap);
						}
					}
					Map<String, Object> mItemsMap = new HashMap<String, Object>();
					mItemsMap.put("itemId", itemID);
					mItemsMap.put("itemName", itemDesc);
					mItemsMap.put("className", className);
					mItemsMap.put("isSubQue", "Y");
					// ??中括号 什么鬼？
					// mItemsMap.put("subQuestion", "["+subQuestion+"]");
					// mItemsMap.put("subOption", "["+subOption+"]");
					mItemsMap.put("subOption", optionDataList);
					mItemsMap.put("subQuestion", subQuestionDataList);
					//拼接 需要字符串
					String tempStr="\""+itemID+"\":"+jacksonUtil.Map2json(mItemsMap);
					if(mItemsJson==null||mItemsJson.equals("")){
						mItemsJson=tempStr;
					}
					else{
						mItemsJson=mItemsJson+","+tempStr;
					}
				} else {
					/* 可选项问题（单选、多选、下拉框） */
					if (isOpt>0) {
						String optName, optDesc, optOrder;
						//String str_opt = "";
						List<Map<String,Object>>str_optDataList=new ArrayList<Map<String,Object>>();
						final String sqlOpt = "SELECT TZ_XXXKXZ_MC,TZ_XXXKXZ_MS,TZ_ORDER FROM PS_TZ_DC_XXX_KXZ_T WHERE TZ_APP_TPL_ID=? AND TZ_XXX_BH=? ORDER BY TZ_ORDER";
						List<Map<String, Object>> optList = new ArrayList<Map<String, Object>>();
						optList = jdbcTemplate.queryForList(sqlOpt, new Object[] { tplId, itemID });
						if (optList != null) {
							for (int j = 0; j < optList.size(); j++) {
								Map<String, Object> optMap = new HashMap<String, Object>();
								optMap=optList.get(j);
								optName = optMap.get("TZ_XXXKXZ_MC") == null ? ""
										: optMap.get("TZ_XXXKXZ_MC").toString();
								optDesc = optMap.get("TZ_XXXKXZ_MS") == null ? ""
										: optMap.get("TZ_XXXKXZ_MS").toString();
								optOrder = optMap.get("TZ_ORDER") == null ? "" : optMap.get("TZ_ORDER").toString();

								Map<String, Object> optionMap = new HashMap<String, Object>();
								optionMap.put("sOptId", optOrder);
								optionMap.put("sOptName", optName);
								optionMap.put("sOptDesc", optDesc);
//								if (str_opt.equals("")) {
//									str_opt = jacksonUtil.Map2json(optionMap);
//								} else {
//									str_opt = str_opt + "," + jacksonUtil.Map2json(optionMap);
//								}
								str_optDataList.add(optionMap);
							}
						}
						
						//&itemID, &itemDesc, &className, &str_opt, ""
						Map<String,Object>tempMap=new HashMap<String,Object>();
						tempMap.put("itemId", itemID);
						tempMap.put("itemName", itemDesc);
						tempMap.put("className", className);
						tempMap.put("isSubQue", "N");
						tempMap.put("subOption", str_optDataList);
						tempMap.put("subQuestion", new ArrayList<Map<String,Object>>());
						//拼接 需要字符串
						String tempStr="\""+itemID+"\":"+jacksonUtil.Map2json(tempMap);
						if(mItemsJson==null||mItemsJson.equals("")){
							mItemsJson=tempStr;
						}
						else{
							mItemsJson=mItemsJson+","+tempStr;
						}
						
					} else {
						Map<String, Object> mItemsMap = new HashMap<String, Object>();
						mItemsMap.put("itemId", itemID);
						mItemsMap.put("itemName", itemDesc);
						mItemsMap.put("className", className);
						mItemsMap.put("isSubQue", "N");
						// ??中括号 什么鬼？
						mItemsMap.put("subOption", new ArrayList<Map<String,Object>>());
						mItemsMap.put("subQuestion", new ArrayList<Map<String,Object>>());
						//拼接 需要字符串
						String tempStr="\""+itemID+"\":"+jacksonUtil.Map2json(mItemsMap);
						if(mItemsJson==null||mItemsJson.equals("")){
							mItemsJson=tempStr;
						}
						else{
							mItemsJson=mItemsJson+","+tempStr;
						}
					}
				}
			}
		}
		mItemsJson = "{" + mItemsJson + "}";
		sqlGlItems = "SELECT TZ_XXX_BH,TZ_XXX_MC,TZ_TITLE,TZ_COM_LMC,TZ_ORDER,TZ_XXX_QID FROM PS_TZ_DC_XXXPZ_T WHERE TZ_APP_TPL_ID=? AND TZ_COM_LMC NOT IN('PageNav') ORDER BY TZ_ORDER";
		List<Map<String, Object>> glItemsList = new ArrayList<Map<String, Object>>();
		glItemsList = jdbcTemplate.queryForList(sqlGlItems, new Object[] { tplId });
		if (glItemsList != null) {
			for (int i = 0; i < glItemsList.size(); i++) {
				Map<String, Object> glItemsMap = new HashMap<String, Object>();
				glItemsMap = glItemsList.get(i);

				itemID = glItemsMap.get("TZ_XXX_BH") == null ? "" : glItemsMap.get("TZ_XXX_BH").toString();
				itemName = glItemsMap.get("TZ_XXX_MC") == null ? "" : glItemsMap.get("TZ_XXX_MC").toString();
				itemTitle = glItemsMap.get("TZ_TITLE") == null ? "" : glItemsMap.get("TZ_TITLE").toString();
				className = glItemsMap.get("TZ_COM_LMC") == null ? "" : glItemsMap.get("TZ_COM_LMC").toString();
				order = glItemsMap.get("TZ_ORDER") == null ? null
						: Integer.valueOf(glItemsMap.get("TZ_ORDER").toString());
				strQid = glItemsMap.get("TZ_XXX_QID") == null ? "" : glItemsMap.get("TZ_XXX_QID").toString();

				itemDesc = strQid + "." + itemName;
				if (lan.equals("ZHS"))// 中文
				{
					if (itemDesc.length() > 50) {
						itemDesc = itemDesc.substring(1, 50) + "...";
					}
				} else {
					if (itemDesc.length() > 100) {
						itemDesc = itemDesc.substring(1, 100) + "...";
					}
				}
				// &itemDesc = &UtilObj.Transformchar(&itemDesc);
				Map<String, Object> glItemsMap2 = new HashMap<String, Object>();
				glItemsMap2.put("itemId", itemID);
				glItemsMap2.put("itemName", itemDesc);
				String tempStr="\""+itemID+"\":"+ jacksonUtil.Map2json(glItemsMap2);
				if (glItemsJson==null||glItemsJson.equals("")) {
					glItemsJson =tempStr;
				} else {
					glItemsJson = glItemsJson + "," + tempStr;
				}
				//拼接需要字符串
				
			}
		}
		// String tplId,String mItemsJson,String glItemsJson
		glItemsJson = "{" + glItemsJson + "}";
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("tplId", tplId);
		returnMap.put("mItemsJson", mItemsJson);
		returnMap.put("glItemsJson", glItemsJson);
		return returnMap;

	}

	/* 获取逻辑规则JSON数据 */
	public String getLogicJson(String tplId) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		/* 表格题逻辑规则 */
		System.out.println("=模板==getLogicJson()执行===");

		String strLogicId=null;
		String strItemId=null;
		String strType=null;
		int pageNo=0;
		int orderNo=0;

		String strLogicJson = "";
		int isSquFlag=0;
		int isOptFlag=0;

		/* 在线调查模板表逻辑规则 */
		final String sqlLogic = "SELECT TZ_DC_LJTJ_ID,TZ_XXX_BH,TZ_LJ_LX,TZ_PAGE_NO,TZ_LJTJ_XH FROM PS_TZ_DC_MB_LJGZ_T A WHERE TZ_APP_TPL_ID=? AND EXISTS(SELECT 'Y' FROM PS_TZ_DC_XXXPZ_T B WHERE A.TZ_APP_TPL_ID=B.TZ_APP_TPL_ID AND A.TZ_XXX_BH=B.TZ_XXX_BH AND TZ_XXX_CCLX IN('S','D','T') AND TZ_COM_LMC NOT IN('MultipleTextBox')) ORDER BY TZ_LJTJ_XH";
		List<Map<String, Object>> logicArrayList = new ArrayList<Map<String, Object>>();
		logicArrayList = jdbcTemplate.queryForList(sqlLogic, new Object[] { tplId });
		if (logicArrayList != null) {
			for (int i = 0; i < logicArrayList.size(); i++) {
				Map<String, Object> logicMap = new HashMap<String, Object>();
				logicMap = logicArrayList.get(i);

				// &strLogicId, &strItemId, &strType, &pageNo, &orderNo
				strLogicId = logicMap.get("TZ_DC_LJTJ_ID") == null ? "" : logicMap.get("TZ_DC_LJTJ_ID").toString();
				strItemId = logicMap.get("TZ_XXX_BH") == null ? "" : logicMap.get("TZ_XXX_BH").toString();
				strType = logicMap.get("TZ_LJ_LX") == null ? "" : logicMap.get("TZ_LJ_LX").toString();
				pageNo = logicMap.get("TZ_PAGE_NO") == null ? null
						: Integer.valueOf(logicMap.get("TZ_PAGE_NO").toString());
				orderNo = logicMap.get("TZ_LJTJ_XH") == null ? null
						: Integer.valueOf(logicMap.get("TZ_LJTJ_XH").toString());

				// String str_gxwt = ""; /*逻辑规则对应的关系问题*/
				List<Map<String, Object>> str_gxwtList = new ArrayList<Map<String, Object>>();
				String cItemId;
				/* 控制问题 */
				final String sqlBgCtrl = "SELECT TZ_XXX_BH FROM PS_TZ_DC_MB_LJXS_T A WHERE TZ_DC_LJTJ_ID=? AND TZ_APP_TPL_ID=? AND EXISTS(SELECT 'Y' FROM PS_TZ_DC_XXXPZ_T B WHERE A.TZ_APP_TPL_ID=B.TZ_APP_TPL_ID AND A.TZ_XXX_BH=B.TZ_XXX_BH)";
				List<Map<String, Object>> bgCtrlList = new ArrayList<Map<String, Object>>();
				bgCtrlList = jdbcTemplate.queryForList(sqlBgCtrl, new Object[] { strLogicId, tplId });
				// begin
				if (bgCtrlList != null) {
					for (int j = 0; j < bgCtrlList.size(); j++) {
						Map<String, Object> bgCtrlMap = bgCtrlList.get(j);
						cItemId = bgCtrlMap.get("TZ_XXX_BH").toString();
						System.out.print("==cItemId==" + cItemId);
						Map<String, Object> tempMap = new HashMap<String, Object>();
						if (!cItemId.equals(""))
							tempMap.put("itemId", cItemId);
						// if(str_gxwt.equals("")){
						// str_gxwt= jacksonUtil.Map2json(tempMap);
						// }
						// else{
						// str_gxwt=str_gxwt+","+jacksonUtil.Map2json(tempMap);
						// }
						str_gxwtList.add(tempMap);
					}
				}
				// --end
				// if(str_gxwt.equals("")){

				// str_gxwt= jacksonUtil.Map2json(tempMap);
				// }
				/* 一般题型逻辑规则 */
				int qType = jdbcTemplate.queryForObject(
						"SELECT COUNT('Y') FROM PS_TZ_DC_MB_YBGZ_T WHERE TZ_DC_LJTJ_ID=? AND TZ_APP_TPL_ID=?",
						new Object[] { strLogicId, tplId }, "int");
				System.out.println("qType:" + qType);
				if (qType>0) {
					String strOpt;
					String checkSta;
					// String str_yb_opt="";
					List<Map<String, Object>> yb_opt_DataList = new ArrayList<Map<String, Object>>();
					final String sqlYbLogic = "SELECT TZ_XXXKXZ_MC,TZ_IS_SELECTED FROM PS_TZ_DC_MB_YBGZ_T WHERE TZ_DC_LJTJ_ID=? AND TZ_APP_TPL_ID=? AND TZ_XXX_BH=?";
					List<Map<String, Object>> ybLogicList = new ArrayList<Map<String, Object>>();
					ybLogicList = jdbcTemplate.queryForList(sqlYbLogic, new Object[] { strLogicId, tplId, strItemId });
					// begin
					if (ybLogicList != null) {
						for (int j = 0; j < ybLogicList.size(); j++) {
							Map<String, Object> ybLogicMap = new HashMap<String, Object>();
							ybLogicMap = ybLogicList.get(j);

							strOpt = ybLogicMap.get("TZ_XXXKXZ_MC") == null ? ""
									: ybLogicMap.get("TZ_XXXKXZ_MC").toString();
							checkSta = ybLogicMap.get("TZ_IS_SELECTED") == null ? ""
									: ybLogicMap.get("TZ_IS_SELECTED").toString();
							isOptFlag = jdbcTemplate.queryForObject(
									"SELECT COUNT('Y') FROM PS_TZ_DC_XXX_KXZ_T WHERE TZ_APP_TPL_ID=? AND TZ_XXX_BH=? AND TZ_XXXKXZ_MC=?",
									new Object[] { tplId, strItemId, strOpt }, "int");
							if (isOptFlag<=0) {
								strOpt = "";
							}
							Map<String, Object> tempMap = new HashMap<String, Object>();
							tempMap.put("sItemId", "");
							tempMap.put("option", strOpt);
							tempMap.put("checked", checkSta);
							// if(str_yb_opt.equals("")){
							// str_yb_opt=jacksonUtil.Map2json(tempMap);
							// }
							// else{
							// str_yb_opt=str_yb_opt+","+jacksonUtil.Map2json(tempMap);
							// }
							yb_opt_DataList.add(tempMap);
						}
					}
					// end
					// &tplId, &strLogicId, &strItemId, "N", &strType, &pageNo,
					// &orderNo, &str_yb_opt, &str_gxwt
					Map<String, Object> tempMap = new HashMap<String, Object>();
					tempMap.put("SurveyID", tplId);
					tempMap.put("logicalId", strLogicId);
					tempMap.put("itemId", strItemId);
					tempMap.put("isSubQue", "N");
					tempMap.put("type", strType);
					tempMap.put("pageNo", pageNo);
					tempMap.put("orderBy", orderNo);
					/// tempMap.put("subQuestion", str_yb_opt);
					tempMap.put("subQuestion", yb_opt_DataList);
					tempMap.put("relatedQ", str_gxwtList);
					if (strLogicJson==null||strLogicJson.equals("")) {
						strLogicJson = jacksonUtil.Map2json(tempMap);
					} else {
						strLogicJson = strLogicJson + "," + jacksonUtil.Map2json(tempMap);
					}
				}
				// ---表格题型逻辑规则
				qType = jdbcTemplate.queryForObject(
						"SELECT COUNT('Y') FROM PS_TZ_DC_MB_GZGX_T WHERE TZ_DC_LJTJ_ID=? AND TZ_APP_TPL_ID=?",
						new Object[] { strLogicId, tplId }, "int");
				System.out.println("表格题型qType:" + qType);
				if (qType>0) {
					// String str_sOpt = "";
					List<Map<String, Object>> str_sOptList = new ArrayList<Map<String, Object>>();
					String TZ_XXXZWT_MC, TZ_XXXKXZ_MC, TZ_IS_SELECTED;
					final String sqlBgLogic = "SELECT TZ_XXXZWT_MC,TZ_XXXKXZ_MC,TZ_IS_SELECTED FROM PS_TZ_DC_MB_GZGX_T WHERE TZ_DC_LJTJ_ID=? AND TZ_APP_TPL_ID=? AND TZ_XXX_BH=?";
					List<Map<String, Object>> bgLogicList = new ArrayList<Map<String, Object>>();
					bgLogicList = jdbcTemplate.queryForList(sqlBgLogic, new Object[] { strLogicId, tplId, strItemId });
					// begin
					if (bgLogicList != null) {
						for (int j = 0; j < bgLogicList.size(); j++) {
							Map<String, Object> bgLogicMap = new HashMap<String, Object>();
							bgLogicMap = bgLogicList.get(j);
							TZ_XXXZWT_MC = bgLogicMap.get("TZ_XXXZWT_MC") == null ? ""
									: bgLogicMap.get("TZ_XXXZWT_MC").toString();
							TZ_XXXKXZ_MC = bgLogicMap.get("TZ_XXXKXZ_MC") == null ? ""
									: bgLogicMap.get("TZ_XXXKXZ_MC").toString();
							TZ_IS_SELECTED = bgLogicMap.get("TZ_IS_SELECTED") == null ? ""
									: bgLogicMap.get("TZ_IS_SELECTED").toString();

							isSquFlag = jdbcTemplate.queryForObject(
									"SELECT COUNT('Y') FROM PS_TZ_DC_BGT_ZWT_T WHERE TZ_APP_TPL_ID=? AND TZ_XXX_BH=? AND TZ_QU_CODE=?",
									new Object[] { tplId, strItemId, TZ_XXXZWT_MC }, "int");
							isOptFlag = jdbcTemplate.queryForObject(
									"SELECT COUNT('Y') FROM PS_TZ_DC_BGT_KXZ_T WHERE TZ_APP_TPL_ID=? AND TZ_XXX_BH=? AND TZ_OPT_CODE=?",
									new Object[] { tplId, strItemId, TZ_XXXKXZ_MC }, "int");

							if (isSquFlag<=0) {
								TZ_XXXZWT_MC = "";
							}
							if (isOptFlag<=0) {
								TZ_XXXKXZ_MC = "";
							}
							Map<String, Object> tempMap = new HashMap<String, Object>();
							tempMap.put("sItemId", TZ_XXXZWT_MC);
							tempMap.put("option", TZ_XXXKXZ_MC);
							tempMap.put("checked", TZ_IS_SELECTED);
							// if(str_sOpt.equals("")){
							// str_sOpt=jacksonUtil.Map2json(tempMap);
							// }
							// else{
							// str_sOpt=str_sOpt+","+jacksonUtil.Map2json(tempMap);
							// }
							str_sOptList.add(tempMap);
						}
					}
					// end
					// &tplId, &strLogicId, &strItemId, "Y", &strType, &pageNo,
					// &orderNo, &str_sOpt, &str_gxwt
					Map<String, Object> tempMap = new HashMap<String, Object>();
					tempMap.put("SurveyID", tplId);
					tempMap.put("logicalId", strLogicId);
					tempMap.put("itemId", strItemId);
					tempMap.put("isSubQue", "Y");
					tempMap.put("type", strType);
					tempMap.put("pageNo", pageNo);
					tempMap.put("orderBy", orderNo);
					tempMap.put("subQuestion", str_sOptList);
					tempMap.put("relatedQ", str_gxwtList);
					if (strLogicJson==null||strLogicJson.equals("")) {
						strLogicJson = jacksonUtil.Map2json(tempMap);
					} else {
						strLogicJson = strLogicJson + "," + jacksonUtil.Map2json(tempMap);
						;
					}
				}
			}
		}
		// 中括号什么鬼？
		strLogicJson = "[" + strLogicJson + "]";
		return strLogicJson;
	}

	private boolean delLogic(String tplId, String type) {
		try {
			if (tplId != null) {
				if (type.equals("MB")) { // 删除模版的逻辑
					jdbcTemplate.update("DELETE FROM PS_TZ_DC_MB_LJXS_T WHERE TZ_APP_TPL_ID=?", new Object[] { tplId });
					jdbcTemplate.update("DELETE FROM PS_TZ_DC_MB_GZGX_T WHERE TZ_APP_TPL_ID=?", new Object[] { tplId });
					jdbcTemplate.update("DELETE FROM PS_TZ_DC_MB_YBGZ_T WHERE TZ_APP_TPL_ID=?", new Object[] { tplId });
					jdbcTemplate.update("DELETE FROM PS_TZ_DC_MB_LJGZ_T WHERE TZ_APP_TPL_ID=?", new Object[] { tplId });
					return true;
				} else { // 删除问卷的逻辑
					jdbcTemplate.update("DELETE FROM PS_TZ_DC_WJ_LJXS_T WHERE TZ_DC_WJ_ID=?", new Object[] { tplId });
					jdbcTemplate.update("DELETE FROM PS_TZ_DC_WJ_GZGX_T WHERE TZ_DC_WJ_ID=?", new Object[] { tplId });
					jdbcTemplate.update("DELETE FROM PS_TZ_DC_WJ_YBGZ_T WHERE TZ_DC_WJ_ID=?", new Object[] { tplId });
					jdbcTemplate.update("DELETE FROM PS_TZ_DC_WJ_LJGZ_T WHERE TZ_DC_WJ_ID=?", new Object[] { tplId });
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 初始化问卷调查逻辑规则
	 * 
	 * @param tplId
	 * @return
	 */
	public String wjInit(String tplId) {
		String strHtml = "";
		String strItemJson = "";
		String strGlItemJson = "";
		String strLogicJson = "";

		final String com = "TZ_ZXDC_WJGL_COM";
		final String page = "TZ_DCWJ_LJGZ_STD";
		final String tzGeneralURL = request.getContextPath() + "/dispatcher";
		// 获取问卷所有除了多行文本外的所有控件
		Map<String, Object> resultMap = this.getWjItemsJson(tplId, strItemJson, strGlItemJson);

		if (resultMap.get("mItemsJson") != null)
			strItemJson = resultMap.get("mItemsJson").toString();
		if (resultMap.get("glItemsJson") != null)
			strGlItemJson = resultMap.get("glItemsJson").toString();
		// 获取已经保存的逻辑关系
		strLogicJson = this.getWjLogicJson(tplId);
		//url JS资源路径根目录
		String URL=request.getContextPath();
		
		System.out.println("===传到前台数据strLogicJson：==" + strLogicJson);
		System.out.println("===传到前台数据strItemJson：==" + strItemJson);
		System.out.println("===传到前台数据strGlItemJson：==" + strGlItemJson);
		try {
			strHtml = tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_DC_LOGIC_DEFN_HTML", tzGeneralURL,
					tplId, strItemJson, strGlItemJson, strLogicJson, com, page,URL);
		} catch (TzSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strHtml;
	}

	/**
	 * 获取问卷调查信息项JSON数据
	 * 
	 * @param tplId
	 * @param mItemsJson
	 *            调查表所有信息项
	 * @param glItemsJson
	 * @return
	 */
	public Map<String, Object> getWjItemsJson(String tplId, String mItemsJson, String glItemsJson) {
		System.out.println("===getWjItemsJson执行===");
		System.out.println("tplId:" + tplId + "==mItemsJson:" + mItemsJson + "==glItemsJson:" + glItemsJson);
		String itemID;
		String itemName;
		String className;
		String strQid;
		String itemDesc;

		JacksonUtil jacksonUtil = new JacksonUtil();
		// 获取问卷调查文字版本（中文还是英文）
		String lan = jdbcTemplate.queryForObject("SELECT TZ_APP_TPL_LAN FROM PS_TZ_DC_WJ_DY_T WHERE TZ_DC_WJ_ID=?",
				new Object[] { tplId }, "String");
		// 查询的SQL
		String sqlSubq = null;
		// 存放查询结果的list
		List<Map<String, Object>> qList = null;
		// 取数据用户的MAP
		Map<String, Object> itemsMap = null;
		// 结果用的MAP
		Map<String, Object> tempMap = null;
		// 临时字符串
		String tempStr = "";

		// 加载调查表所有的控件 开始
		// 判断逻辑的时候把多行文本排除，多行文本不参与逻辑设置，不过可以逻辑的结果 TZ_XXX_CCLX 存储类型 D可选框 F附件
		// L长文本存储类型 S短文本存储类型 T表格题
		// TZ_XXX_BH信息项编号 TZ_XXX_MC信息项名称 TZ_TITLE主题 TZ_COM_LMC控件类名称 TZ_ORDER排序序号
		// TZ_XXX_QID 题号
		sqlSubq = "SELECT TZ_XXX_BH,TZ_XXX_MC,TZ_TITLE,TZ_COM_LMC,TZ_ORDER,TZ_XXX_QID FROM PS_TZ_DCWJ_XXXPZ_T WHERE TZ_DC_WJ_ID=? AND TZ_XXX_CCLX IN('S','D','T') AND TZ_COM_LMC NOT IN('MultipleTextBox') ORDER BY TZ_ORDER";
		qList = jdbcTemplate.queryForList(sqlSubq, new Object[] { tplId });
		if (qList != null) {
			List<Map<String, Object>> subqList = null;
			// 多选表格的 行
			String sub_name, sub_desc;
			// 多选表格的列
			String sOpt_name, sOpt_desc, sOpt_order;

			// 单选框和下拉框，量表题，多选题，多行文本框里面的项
			String optName, optDesc, optOrder;

			// 多选表格 行保存的对象
			List<Map<String, Object>> subQuestionDataList = null;
			// 多选表格 列保存的对象
			List<Map<String, Object>> subOptionDataList = null;

			// 单选框和下拉框，量表题，多选题，多行文本框里面的项
			List<Map<String, Object>> optDataList = null;

			for (int i = 0; i < qList.size(); i++) {
				itemsMap = qList.get(i);
				// &itemID, &itemName, &itemTitle, &className, &order, &strQid
				itemID = itemsMap.get("TZ_XXX_BH") == null ? "" : itemsMap.get("TZ_XXX_BH").toString();
				itemName = itemsMap.get("TZ_XXX_MC") == null ? "" : itemsMap.get("TZ_XXX_MC").toString();
				className = itemsMap.get("TZ_COM_LMC") == null ? "" : itemsMap.get("TZ_COM_LMC").toString();
				strQid = itemsMap.get("TZ_XXX_QID") == null ? "" : itemsMap.get("TZ_XXX_QID").toString();

				int isSub = 0;
				int isOpt = 0;

				// 传到前台的itemName
				itemDesc = strQid + "." + itemName;

				if (lan.equals("ZHS")) {
					if (itemDesc.length() > 50) {
						itemDesc = itemDesc.substring(1, 50) + "...";
					}
				} else {
					if (itemDesc.length() > 100) {
						itemDesc = itemDesc.substring(1, 100) + "...";
					}
				}

				// isSub 是否是多选表格 0 不是 >0是
				isSub = jdbcTemplate.queryForObject(
						"SELECT count('Y') FROM PS_TZ_DCWJ_BGZWT_T WHERE TZ_DC_WJ_ID=? AND TZ_XXX_BH=?",
						new Object[] { tplId, itemID }, "int");
				// isOpt 是否是下拉菜单 0 不是 >0是
				isOpt = jdbcTemplate.queryForObject(
						"SELECT count('Y') FROM PS_TZ_DCWJ_XXKXZ_T WHERE TZ_DC_WJ_ID=? AND TZ_XXX_BH=?",
						new Object[] { tplId, itemID }, "int");

				// 表格多选题和表格单选题以及课程评估处理
				if (isSub != 0) {
					// 查询多选表格或单选表格 里面的内容 也就是行 填充进 subQuestionDataList
					sqlSubq = "SELECT TZ_QU_CODE,TZ_QU_NAME FROM PS_TZ_DCWJ_BGZWT_T WHERE TZ_DC_WJ_ID=? AND TZ_XXX_BH=? ORDER BY TZ_ORDER";
					subqList = jdbcTemplate.queryForList(sqlSubq, new Object[] { tplId, itemID });
					if (subqList != null) {
						subQuestionDataList = new ArrayList<Map<String, Object>>();
						for (int j = 0; j < subqList.size(); j++) {
							itemsMap = subqList.get(j);
							sub_name = itemsMap.get("TZ_QU_CODE") == null ? "" : itemsMap.get("TZ_QU_CODE").toString();
							sub_desc = itemsMap.get("TZ_QU_NAME") == null ? "" : itemsMap.get("TZ_QU_NAME").toString();

							tempMap = new HashMap<String, Object>();
							tempMap.put("sItemId", sub_name);
							tempMap.put("sItemName", sub_desc);
							subQuestionDataList.add(tempMap);
						}
					}

					sqlSubq = "SELECT TZ_OPT_CODE,TZ_OPT_NAME,TZ_ORDER FROM PS_TZ_DCWJ_BGKXZ_T WHERE TZ_DC_WJ_ID=? AND TZ_XXX_BH=? ORDER BY TZ_ORDER";
					subqList = jdbcTemplate.queryForList(sqlSubq, new Object[] { tplId, itemID });
					// begin
					if (subqList != null) {
						subOptionDataList = new ArrayList<Map<String, Object>>();
						for (int j = 0; j < subqList.size(); j++) {
							itemsMap = subqList.get(j);
							sOpt_name = itemsMap.get("TZ_OPT_CODE") == null ? ""
									: itemsMap.get("TZ_OPT_CODE").toString();
							sOpt_desc = itemsMap.get("TZ_OPT_NAME") == null ? ""
									: itemsMap.get("TZ_OPT_NAME").toString();
							sOpt_order = itemsMap.get("TZ_ORDER") == null ? "" : itemsMap.get("TZ_ORDER").toString();
							tempMap = new HashMap<String, Object>();
							tempMap.put("sOptId", sOpt_order);
							tempMap.put("sOptName", sOpt_name);
							tempMap.put("sOptDesc", sOpt_desc);
							subOptionDataList.add(tempMap);
						}
					}

					tempMap = new HashMap<String, Object>();
					tempMap.put("itemId", itemID);
					tempMap.put("itemName", itemDesc);
					tempMap.put("className", className);
					tempMap.put("isSubQue", "Y");
					tempMap.put("subQuestion", subQuestionDataList);
					tempMap.put("subOption", subOptionDataList);

					tempStr = "\"" + itemID + "\"" + ":" + jacksonUtil.Map2json(tempMap);

					// System.out.println("tempStr:" + tempStr);

					if (mItemsJson.equals("")) {
						mItemsJson = tempStr;
					} else {
						mItemsJson = mItemsJson + "," + tempStr;
					}
				} else {
					/* 可选项问题（单选、多选、下拉框） */
					if (isOpt != 0) {
						System.out.println("===可选项问题（单选、多选、下拉框）====");

						sqlSubq = "SELECT TZ_XXXKXZ_MC,TZ_XXXKXZ_MS,TZ_ORDER FROM PS_TZ_DCWJ_XXKXZ_T WHERE TZ_DC_WJ_ID=? AND TZ_XXX_BH=? ORDER BY TZ_ORDER";
						subqList = jdbcTemplate.queryForList(sqlSubq, new Object[] { tplId, itemID });
						// begin
						if (subqList != null) {
							optDataList = new ArrayList<Map<String, Object>>();
							for (int j = 0; j < subqList.size(); j++) {
								itemsMap = subqList.get(j);
								optName = itemsMap.get("TZ_XXXKXZ_MC") == null ? ""
										: itemsMap.get("TZ_XXXKXZ_MC").toString();
								optDesc = itemsMap.get("TZ_XXXKXZ_MS") == null ? ""
										: itemsMap.get("TZ_XXXKXZ_MS").toString();
								optOrder = itemsMap.get("TZ_ORDER") == null ? "" : itemsMap.get("TZ_ORDER").toString();

								// &optOrder, &optName, &optDesc
								tempMap = new HashMap<String, Object>();
								tempMap.put("sOptId", optOrder);
								tempMap.put("sOptName", optName);
								tempMap.put("sOptDesc", optDesc);
								optDataList.add(tempMap);
							}
						}

						tempMap = new HashMap<String, Object>();
						tempMap.put("itemId", itemID);
						tempMap.put("itemName", itemDesc);
						tempMap.put("className", className);
						tempMap.put("isSubQue", "N");
						tempMap.put("subOption", optDataList);
						tempMap.put("subQuestion", new ArrayList<Map<String, Object>>());

						tempStr = "\"" + itemID + "\"" + ":" + jacksonUtil.Map2json(tempMap);
						// System.out.println("tempStr:" + tempStr);
						if (mItemsJson.equals("")) {
							mItemsJson = tempStr;
						} else {
							mItemsJson = mItemsJson + "," + tempStr;
						}
						// System.out.println("==right==mItemsJson:" +
						// mItemsJson);

					}
					/* 普通问题（填空、问答） */
					else {
						System.out.println("====普通问题（填空、问答）===");

						tempMap = new HashMap<String, Object>();
						tempMap.put("itemId", itemID);
						tempMap.put("itemName", itemDesc);
						tempMap.put("className", className);
						tempMap.put("isSubQue", "N");
						tempMap.put("subOption", new ArrayList<Map<String, Object>>());
						tempMap.put("subQuestion", new ArrayList<Map<String, Object>>());
						tempStr = "\"" + itemID + "\"" + ":" + jacksonUtil.Map2json(tempMap);
						// System.out.println("tempStr:" + tempStr);
						if (mItemsJson.equals("")) {
							mItemsJson = tempStr;
						} else {
							mItemsJson = mItemsJson + "," + tempStr;
						}
					}
				}
			}
		}
		mItemsJson = "{" + mItemsJson + "}";
		// 加载调查表所有的控件结束

		/* 关联问题信息项Begin */
		sqlSubq = "SELECT TZ_XXX_BH,TZ_XXX_MC,TZ_TITLE,TZ_COM_LMC,TZ_ORDER,TZ_XXX_QID FROM PS_TZ_DCWJ_XXXPZ_T WHERE TZ_DC_WJ_ID=? AND TZ_COM_LMC NOT IN('PageNav') ORDER BY TZ_ORDER";
		qList = jdbcTemplate.queryForList(sqlSubq, new Object[] { tplId });
		// begin
		if (qList != null) {
			for (int i = 0; i < qList.size(); i++) {
				itemsMap = qList.get(i);

				// &itemID, &itemName, &itemTitle, &className, &order, &strQid
				itemID = itemsMap.get("TZ_XXX_BH") == null ? "" : itemsMap.get("TZ_XXX_BH").toString();
				itemName = itemsMap.get("TZ_XXX_MC") == null ? "" : itemsMap.get("TZ_XXX_MC").toString();
				className = itemsMap.get("TZ_COM_LMC") == null ? "" : itemsMap.get("TZ_COM_LMC").toString();
				strQid = itemsMap.get("TZ_XXX_QID") == null ? "" : itemsMap.get("TZ_XXX_QID").toString();

				itemDesc = strQid + "." + itemName;
				if (lan.equals("ZHS")) {
					if (itemDesc.length() > 50) {
						itemDesc = itemDesc.substring(1, 50) + "...";
					}
				} else {
					if (itemDesc.length() > 100) {
						itemDesc = itemDesc.substring(1, 100) + "...";
					}
				}
				// &itemDesc = &UtilObj.Transformchar(&itemDesc);
				tempMap = new HashMap<String, Object>();
				tempMap.put("itemId", itemID);
				tempMap.put("itemName", itemDesc);
				// ---拼接需要格式字符串
				tempStr = "\"" + itemID + "\"" + ":" + jacksonUtil.Map2json(tempMap);
				if (glItemsJson.equals("")) {
					glItemsJson = tempStr;
				} else {
					glItemsJson = glItemsJson + "," + tempStr;
				}
			}
		}

		glItemsJson = "{" + glItemsJson + "}";
		/* 关联问题信息项End */
		Map<String, Object> returnMap = new HashMap<String, Object>();

		returnMap.put("tplId", tplId);
		returnMap.put("mItemsJson", mItemsJson);
		returnMap.put("glItemsJson", glItemsJson);
		return returnMap;
	}

	/**
	 * 获取问卷调查逻辑规则JSON数据
	 * 
	 * @param tplId
	 * @return
	 */
	public String getWjLogicJson(String tplId) {
		JacksonUtil jacksonUtil = new JacksonUtil();

		String strLogicId;
		String strItemId;
		String strType;
		// 正式编号建议从1以上
		int pageNo = 0;
		int orderNo = 0;
		int qType=0;

		String strLogicJson = "";
		String isSquFlag;
		String isOptFlag;

		String strOpt;
		String checkSta;

		String cItemId;

		String TZ_XXXZWT_MC, TZ_XXXKXZ_MC, TZ_IS_SELECTED;

		/* 在线调查模板表逻辑规则 */
		String sqlLogic = "SELECT TZ_DC_LJTJ_ID,TZ_XXX_BH,TZ_LJ_LX,TZ_PAGE_NO,TZ_LJTJ_XH FROM PS_TZ_DC_WJ_LJGZ_T A WHERE TZ_DC_WJ_ID=? AND EXISTS(SELECT 'Y' FROM PS_TZ_DCWJ_XXXPZ_T B WHERE A.TZ_DC_WJ_ID=B.TZ_DC_WJ_ID AND A.TZ_XXX_BH=B.TZ_XXX_BH AND TZ_XXX_CCLX IN('S','D','T') AND TZ_COM_LMC NOT IN('MultipleTextBox')) ORDER BY TZ_LJTJ_XH";
		List<Map<String, Object>> logicList = jdbcTemplate.queryForList(sqlLogic, new Object[] { tplId });

		// 获取数据库参数MAP
		Map<String, Object> logicMap = null;
		// 存放结果的MAP
		Map<String, Object> tempMap = null;
		// 获取数据库LIST
		List<Map<String, Object>> dbList = null;

		// 存放逻辑逻辑以后的动作关联项目
		List<Map<String, Object>> str_gxwtList = null;

		// 存放除了表格多选题和表格单选题的逻辑关系
		List<Map<String, Object>> str_yb_optList = null;

		if (logicList != null) {
			for (int i = 0; i < logicList.size(); i++) {
				logicMap = logicList.get(i);
				strLogicId = logicMap.get("TZ_DC_LJTJ_ID") == null ? "" : logicMap.get("TZ_DC_LJTJ_ID").toString();
				strItemId = logicMap.get("TZ_XXX_BH") == null ? "" : logicMap.get("TZ_XXX_BH").toString();
				strType = logicMap.get("TZ_LJ_LX") == null ? "" : logicMap.get("TZ_LJ_LX").toString();
				if (logicMap.get("TZ_PAGE_NO") != null) {
					pageNo = Integer.valueOf(logicMap.get("TZ_PAGE_NO").toString());
				}
				if (logicMap.get("TZ_LJTJ_XH") != null) {
					orderNo = Integer.valueOf(logicMap.get("TZ_LJTJ_XH").toString());
				}

				/* 逻辑规则对应的关系问题 */
				sqlLogic = "SELECT TZ_XXX_BH FROM PS_TZ_DC_WJ_LJXS_T A WHERE TZ_DC_LJTJ_ID=? AND TZ_DC_WJ_ID=? AND EXISTS(SELECT 'Y' FROM PS_TZ_DCWJ_XXXPZ_T B WHERE A.TZ_DC_WJ_ID=B.TZ_DC_WJ_ID AND A.TZ_XXX_BH=B.TZ_XXX_BH)";
				dbList = jdbcTemplate.queryForList(sqlLogic, new Object[] { strLogicId, tplId });
				// begin
				if (dbList != null) {
					str_gxwtList = new ArrayList<Map<String, Object>>();
					for (int j = 0; j < dbList.size(); j++) {
						logicMap = dbList.get(j);
						cItemId = logicMap.get("TZ_XXX_BH") == null ? "" : logicMap.get("TZ_XXX_BH").toString();
						tempMap = new HashMap<String, Object>();
						if (!cItemId.equals("")) {
							tempMap.put("itemId", cItemId);
						}
						str_gxwtList.add(tempMap);
					}
				}

				//
				/* 一般题型逻辑规则 除了表格多选题和表格单选题的逻辑关系 */
				qType = jdbcTemplate.queryForObject(
						"SELECT COUNT('Y') FROM PS_TZ_DC_WJ_YBGZ_T WHERE TZ_DC_LJTJ_ID=? AND TZ_DC_WJ_ID=?",
						new Object[] { strLogicId, tplId }, "int");
				if (qType>0) {
					sqlLogic = "SELECT TZ_XXXKXZ_MC,TZ_IS_SELECTED FROM PS_TZ_DC_WJ_YBGZ_T WHERE TZ_DC_LJTJ_ID=? AND TZ_DC_WJ_ID=? AND TZ_XXX_BH=?";
					dbList = jdbcTemplate.queryForList(sqlLogic, new Object[] { strLogicId, tplId, strItemId });
					// begin
					if (dbList != null) {
						str_yb_optList = new ArrayList<Map<String, Object>>();
						for (int j = 0; j < dbList.size(); j++) {
							logicMap = dbList.get(j);

							strOpt = logicMap.get("TZ_XXXKXZ_MC") == null ? ""
									: logicMap.get("TZ_XXXKXZ_MC").toString();
							checkSta = logicMap.get("TZ_IS_SELECTED") == null ? ""
									: logicMap.get("TZ_IS_SELECTED").toString();

							isOptFlag = jdbcTemplate.queryForObject(
									"SELECT 'Y' FROM PS_TZ_DCWJ_XXKXZ_T WHERE TZ_DC_WJ_ID=? AND TZ_XXX_BH=? AND TZ_XXXKXZ_MC=?",
									new Object[] { tplId, strItemId, strOpt }, "String");
							if (isOptFlag != null && isOptFlag.equals("Y")) {
							} else {
								strOpt = "";
							}
							tempMap = new HashMap<String, Object>();
							tempMap.put("sItemId", "");
							tempMap.put("option", strOpt);
							tempMap.put("checked", checkSta);
							str_yb_optList.add(tempMap);
						}
					}
					// end
					tempMap = new HashMap<String, Object>();
					tempMap.put("SurveyID", tplId);
					tempMap.put("logicalId", strLogicId);
					tempMap.put("itemId", strItemId);
					tempMap.put("isSubQue", "N");
					tempMap.put("type", strType);
					tempMap.put("pageNo", pageNo);
					tempMap.put("orderBy", orderNo);
					tempMap.put("subQuestion", str_yb_optList);
					tempMap.put("relatedQ", str_gxwtList);
					if (strLogicJson.equals("")) {
						strLogicJson = jacksonUtil.Map2json(tempMap);
					} else {
						strLogicJson = strLogicJson + "," + jacksonUtil.Map2json(tempMap);
					}
					System.out.println("====strLogicJson=A===:" + strLogicJson);
				}
				/* 表格题型逻辑规则 */
				qType = jdbcTemplate.queryForObject(
						"SELECT COUNT('Y') FROM PS_TZ_DC_WJ_GZGX_T WHERE TZ_DC_LJTJ_ID=? AND  TZ_DC_WJ_ID=?",
						new Object[] { strLogicId, tplId }, "int");
				if (qType>0) {

					sqlLogic = "SELECT TZ_XXXZWT_MC,TZ_XXXKXZ_MC,TZ_IS_SELECTED FROM PS_TZ_DC_WJ_GZGX_T WHERE TZ_DC_LJTJ_ID=? AND TZ_DC_WJ_ID=? AND TZ_XXX_BH=?";
					dbList = jdbcTemplate.queryForList(sqlLogic, new Object[] { strLogicId, tplId, strItemId });

					// begin
					if (dbList != null) {
						str_yb_optList = new ArrayList<Map<String, Object>>();
						for (int j = 0; j < dbList.size(); j++) {
							logicMap = dbList.get(j);
							TZ_XXXZWT_MC = logicMap.get("TZ_XXXZWT_MC") == null ? ""
									: logicMap.get("TZ_XXXZWT_MC").toString();
							TZ_XXXKXZ_MC = logicMap.get("TZ_XXXKXZ_MC") == null ? ""
									: logicMap.get("TZ_XXXKXZ_MC").toString();
							TZ_IS_SELECTED = logicMap.get("TZ_IS_SELECTED") == null ? ""
									: logicMap.get("TZ_IS_SELECTED").toString();

							isSquFlag = jdbcTemplate.queryForObject(
									"SELECT 'Y' FROM PS_TZ_DCWJ_BGZWT_T WHERE TZ_DC_WJ_ID=? AND TZ_XXX_BH=? AND TZ_QU_CODE=?",
									new Object[] { tplId, strItemId, TZ_XXXZWT_MC }, "String");
							isOptFlag = jdbcTemplate.queryForObject(
									"SELECT 'Y' FROM PS_TZ_DCWJ_BGKXZ_T WHERE TZ_DC_WJ_ID=? AND TZ_XXX_BH=? AND TZ_OPT_CODE=?",
									new Object[] { tplId, strItemId, TZ_XXXKXZ_MC }, "String");

							if (!isSquFlag.equals("Y")) {
								TZ_XXXZWT_MC = "";
							}
							if (!isOptFlag.equals("Y")) {
								TZ_XXXKXZ_MC = "";
							}
							tempMap = new HashMap<String, Object>();
							tempMap.put("sItemId", TZ_XXXZWT_MC);
							tempMap.put("option", TZ_XXXKXZ_MC);
							tempMap.put("checked", TZ_IS_SELECTED);
							str_yb_optList.add(tempMap);
						}
					}
					// end
					tempMap = new HashMap<String, Object>();

					tempMap.put("SurveyID", tplId);
					tempMap.put("logicalId", strLogicId);
					tempMap.put("itemId", strItemId);
					tempMap.put("isSubQue", "Y");
					tempMap.put("type", strType);
					tempMap.put("pageNo", pageNo);
					tempMap.put("orderBy", orderNo);
					tempMap.put("subQuestion", str_yb_optList);
					tempMap.put("relatedQ", str_gxwtList);
					if (strLogicJson.equals("")) {
						strLogicJson = jacksonUtil.Map2json(tempMap);
					} else {
						strLogicJson = strLogicJson + "," + jacksonUtil.Map2json(tempMap);
					}
					// System.out.println("====strLogicJson=B===:" +
					// strLogicJson);
				}
			}
		}
		// System.out.println("==here==");
		strLogicJson = "[" + strLogicJson + "]";
		return strLogicJson;
	}

}
