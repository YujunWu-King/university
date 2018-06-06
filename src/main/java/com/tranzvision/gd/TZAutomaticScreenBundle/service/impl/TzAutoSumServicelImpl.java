package com.tranzvision.gd.TZAutomaticScreenBundle.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCjxTblMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCjxTblKey;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCjxTblWithBLOBs;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsKsTbl;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsKsTblKey;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClassInfTMapper;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClassInfT;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzMsPskshTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzMsPskshTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 面试评审计算总分 自动打分+管理员打分+面试打分
 * 
 * @author caoy
 *
 */
@Service("com.tranzvision.gd.TZAutomaticScreenBundle.service.impl.TzAutoSumServicelImpl")
public class TzAutoSumServicelImpl extends FrameworkImpl {

	@Autowired
	private PsTzClassInfTMapper psTzClassInfTMapper;

	@Autowired
	private TZGDObject tzSQLObject;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TzAutoScreenEngineServiceImpl tzAutoScreenEngineServiceImpl;

	@Autowired
	private PsTzCjxTblMapper psTzCjxTblMapper;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private PsTzMsPskshTblMapper psTzMsPskshTblMapper;

	@Override
	public String tzOther(String strType, String strParams, String[] errorMsg) {
		String strRet = "{}";
		try {
			switch (strType) {
			case "RunSum": // 计算总分
				System.out.println("计算总分");
				strRet = this.tzRunSumProcess(strParams, errorMsg);
				break;
			case "RunMSSum": // 计算面试总分
				System.out.println("计算面试总分");
				strRet = this.tzRunMSSumProcess(strParams, errorMsg);
				break;
			case "runRleaseEngine": // 批量发布面试结果
				System.out.print("批量发布面试结果");
				strRet = this.tzRunReleaseProcess(strParams, errorMsg);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
		}
		return strRet;
	}

	/**
	 * 计算面试分数
	 * 
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String tzRunMSSumProcess(String strParams, String[] errorMsg) {
		String strRet = "";
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		rtnMap.put("status", "");
		rtnMap.put("msg", "");

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			// 班级ID
			String classId = jacksonUtil.getString("classId");
			String batchId = jacksonUtil.getString("batchId");

			if (!"".equals(classId) && classId != null && !"".equals(batchId) && batchId != null) {
				// 由于是非AE程序，为了效率 ，大部分数据库操作 全部放在循环外面执行
				PsTzClassInfT psTzClassInfT = psTzClassInfTMapper.selectByPrimaryKey(classId);
				String orgId = psTzClassInfT.getTzJgId();

				// 面试自动模型ID
				String socreModelId = psTzClassInfT.getTzMscjScorMdId();
				String oprId = tzLoginServiceImpl.getLoginedManagerOprid(request);

				// 成绩模型树根节点
				String rootSql = tzSQLObject.getSQLText("SQL.TZAutomaticScreenBundle.TzModelTreeRootNode");
				Map<String, Object> rootMap = sqlQuery.queryForMap(rootSql, new Object[] { socreModelId, orgId });

				long appInsId = 0;
				Long LscoreInsId = null;
				long scoreInsId = 0;
				String TZ_SCORE_ITEM_ID = "";
				String TZ_SCORE_NUM = "";

				// 自动打分总分

				PsTzMsPskshTbl psTzMsPskshTbl = null;
				PsTzCjxTblWithBLOBs psTzCjxTblWithBLOBs = null;
				PsTzCjxTblKey psTzCjxTblKey = null;

				// 班级批次下参与自动初筛的考生 为该班级下 报名表提交并且审核通过的考生
				String sql = "select DISTINCT TZ_APP_INS_ID from PS_TZ_MP_PW_KS_TBL A where TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ?";

				String avgsql = tzSQLObject.getSQLText("SQL.TZAutomaticScreenBundle.GetMsAvg");
				List<Map<String, Object>> scoreAvgList = null;

				List<Map<String, Object>> appInsList = sqlQuery.queryForList(sql, new Object[] { classId, batchId });
				boolean exists;
				float rootScoreAmount = 0;
				String treeName = "";
				String rootNode = "";

				for (Map<String, Object> appInsMap : appInsList) {
					appInsId = Long.valueOf(appInsMap.get("TZ_APP_INS_ID").toString());

					scoreAvgList = sqlQuery.queryForList(avgsql,
							new Object[] { classId, batchId, String.valueOf(appInsId) });

					if (scoreAvgList.size() > 0) {
						sql = "select TZ_SCORE_INS_ID from PS_TZ_MSPS_KSH_TBL where TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ? and TZ_APP_INS_ID=?";

						LscoreInsId = sqlQuery.queryForObject(sql,
								new Object[] { classId, batchId, String.valueOf(appInsId) }, "Long");
						if (LscoreInsId == null || LscoreInsId.longValue() <= 0) {
							exists = false;
							scoreInsId = tzAutoScreenEngineServiceImpl.creatNewScoreInstanceId(socreModelId, oprId);
							psTzMsPskshTbl = new PsTzMsPskshTbl();
							psTzMsPskshTbl.setTzAppInsId(appInsId);
							psTzMsPskshTbl.setTzApplyPcId(batchId);
							psTzMsPskshTbl.setTzClassId(classId);
							psTzMsPskshTbl.setTzScoreInsId(scoreInsId);
							psTzMsPskshTbl.setRowLastmantDttm(new Date());
							psTzMsPskshTbl.setRowLastmantOprid(oprId);
							psTzMsPskshTblMapper.updateByPrimaryKeySelective(psTzMsPskshTbl);

						} else {
							exists = true;
							scoreInsId = LscoreInsId.longValue();
						}

						System.out.println("classId:" + classId);
						System.out.println("batchId:" + batchId);
						System.out.println("appInsId:" + appInsId);
						System.out.println("scoreInsId:" + scoreInsId);
						for (Map<String, Object> avgMap : scoreAvgList) {
							if (avgMap.get("TZ_SCORE_ITEM_ID") != null) {
								// 总分不是累加

								TZ_SCORE_ITEM_ID = avgMap.get("TZ_SCORE_ITEM_ID").toString();
								if (!TZ_SCORE_ITEM_ID.equals("Total")) {
									TZ_SCORE_NUM = avgMap.get("TZ_SCORE_NUM").toString();
									System.out.println("TZ_SCORE_ITEM_ID:" + TZ_SCORE_ITEM_ID);
									System.out.println("TZ_SCORE_NUM:" + TZ_SCORE_NUM);
									if (!exists) {
										// 插入表TZ_CJX_TBL
										psTzCjxTblWithBLOBs = new PsTzCjxTblWithBLOBs();
										// 成绩单ID
										psTzCjxTblWithBLOBs.setTzScoreInsId(new Long(scoreInsId));
										// 成绩项ID
										psTzCjxTblWithBLOBs.setTzScoreItemId(TZ_SCORE_ITEM_ID);
										// 分值
										psTzCjxTblWithBLOBs.setTzScoreNum(new BigDecimal(TZ_SCORE_NUM));
										// 插入
										psTzCjxTblMapper.insert(psTzCjxTblWithBLOBs);
									} else {
										psTzCjxTblKey = new PsTzCjxTblKey();
										psTzCjxTblKey.setTzScoreInsId(new Long(scoreInsId));
										psTzCjxTblKey.setTzScoreItemId(TZ_SCORE_ITEM_ID);
										psTzCjxTblWithBLOBs = psTzCjxTblMapper.selectByPrimaryKey(psTzCjxTblKey);
										if (psTzCjxTblWithBLOBs != null) {
											// 修改表
											psTzCjxTblWithBLOBs = new PsTzCjxTblWithBLOBs();
											psTzCjxTblWithBLOBs.setTzScoreInsId(new Long(scoreInsId));
											// 成绩项ID
											psTzCjxTblWithBLOBs.setTzScoreItemId(TZ_SCORE_ITEM_ID);
											psTzCjxTblWithBLOBs.setTzScoreNum(new BigDecimal(TZ_SCORE_NUM));
											psTzCjxTblMapper.updateByPrimaryKeySelective(psTzCjxTblWithBLOBs);
										} else {
											// 插入表TZ_CJX_TBL
											psTzCjxTblWithBLOBs = new PsTzCjxTblWithBLOBs();
											// 成绩单ID
											psTzCjxTblWithBLOBs.setTzScoreInsId(new Long(scoreInsId));
											// 成绩项ID
											psTzCjxTblWithBLOBs.setTzScoreItemId(TZ_SCORE_ITEM_ID);
											// 分值
											psTzCjxTblWithBLOBs.setTzScoreNum(new BigDecimal(TZ_SCORE_NUM));
											// 插入
											psTzCjxTblMapper.insert(psTzCjxTblWithBLOBs);
										}
									}
								}
							}
						}
						// 计算总额，不是总分的平均分，而是平均分的总分
						if (rootMap != null) {
							treeName = rootMap.get("TREE_NAME").toString();
							int rootNodeNum = Integer.valueOf(rootMap.get("TREE_NODE_NUM").toString());
							rootNode = rootMap.get("TREE_NODE").toString();

							rootScoreAmount = tzAutoScreenEngineServiceImpl.calculateTreeNodeScore(scoreInsId, treeName,
									orgId, rootNodeNum, rootNode);
							BigDecimal rootScore = BigDecimal.valueOf(Double.valueOf(Float.toString(rootScoreAmount)));
							psTzCjxTblKey = new PsTzCjxTblKey();
							psTzCjxTblKey.setTzScoreInsId(new Long(scoreInsId));
							psTzCjxTblKey.setTzScoreItemId("Total");
							psTzCjxTblWithBLOBs = psTzCjxTblMapper.selectByPrimaryKey(psTzCjxTblKey);
							if (psTzCjxTblWithBLOBs != null) {
								// 修改表
								psTzCjxTblWithBLOBs = new PsTzCjxTblWithBLOBs();
								psTzCjxTblWithBLOBs.setTzScoreInsId(new Long(scoreInsId));
								// 成绩项ID
								psTzCjxTblWithBLOBs.setTzScoreItemId("Total");
								psTzCjxTblWithBLOBs.setTzScoreNum(rootScore);
								psTzCjxTblMapper.updateByPrimaryKeySelective(psTzCjxTblWithBLOBs);
							} else {
								// 插入表TZ_CJX_TBL
								psTzCjxTblWithBLOBs = new PsTzCjxTblWithBLOBs();
								// 成绩单ID
								psTzCjxTblWithBLOBs.setTzScoreInsId(new Long(scoreInsId));
								// 成绩项ID
								psTzCjxTblWithBLOBs.setTzScoreItemId("Total");
								// 分值
								psTzCjxTblWithBLOBs.setTzScoreNum(rootScore);
								// 插入
								psTzCjxTblMapper.insert(psTzCjxTblWithBLOBs);
							}
						}

					}
				}
				rtnMap.put("status", "0");
				rtnMap.put("msg", "打分成功");
			} else {
				rtnMap.put("status", "-1");
				rtnMap.put("msg", "参数错误");
			}
		} catch (Exception e) {
			e.printStackTrace();
			rtnMap.put("status", "-1");
			rtnMap.put("msg", "操作异常");
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
		}

		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}

	/**
	 * 计算总分
	 * 
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String tzRunSumProcess(String strParams, String[] errorMsg) {
		String strRet = "";
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		rtnMap.put("status", "");
		rtnMap.put("msg", "");

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			// 班级ID
			String classId = jacksonUtil.getString("classId");
			String batchId = jacksonUtil.getString("batchId");

			if (!"".equals(classId) && classId != null && !"".equals(batchId) && batchId != null) {
				// 由于是非AE程序，为了效率 ，大部分数据库操作 全部放在循环外面执行
				PsTzClassInfT psTzClassInfT = psTzClassInfTMapper.selectByPrimaryKey(classId);
				// 自动打分成绩模型ID
				String socreModelId = psTzClassInfT.getTzCsScorMdId();

				// 面试自动模型ID
				String msModelId = psTzClassInfT.getTzMscjScorMdId();

				// 管理员打分模型ID
				String adminModelId = psTzClassInfT.getTzZlpsScorMdId();

				String oprId = tzLoginServiceImpl.getLoginedManagerOprid(request);

				String orgId = psTzClassInfT.getTzJgId();

				// 自动初筛成绩模型里面配置的，没有自动初筛执行类的成绩项目(也就是留给管理员打分的项目)
				String sql = tzSQLObject.getSQLText("SQL.TZAutomaticScreenBundle.TzModelTreeRootNode");
				Map<String, Object> autoRootMap = sqlQuery.queryForMap(sql, new Object[] { socreModelId, orgId });
				Map<String, Object> msRootMap = sqlQuery.queryForMap(sql, new Object[] { msModelId, orgId });

				// Map<String, Object> adminRootMap = sqlQuery.queryForMap(sql,
				// new Object[] { adminModelId, orgId });

				// 自动初筛 报名表实例ID和总成绩
				Map<String, String> autoCSKS = new HashMap<String, String>();
				sql = "SELECT A.TZ_APP_INS_ID,B.TZ_SCORE_NUM FROM PS_TZ_CS_KS_TBL A,PS_TZ_CJX_TBL B WHERE A.TZ_CLASS_ID =? AND A.TZ_APPLY_PC_ID =? AND A.TZ_SCORE_INS_ID = B.TZ_SCORE_INS_ID AND B.TZ_SCORE_ITEM_ID =?";
				List<Map<String, Object>> list = sqlQuery.queryForList(sql,
						new Object[] { classId, batchId, autoRootMap.get("TREE_NODE").toString() });
				for (Map<String, Object> map : list) {
					autoCSKS.put(map.get("TZ_APP_INS_ID").toString(),
							map.get("TZ_SCORE_NUM") == null ? "" : map.get("TZ_SCORE_NUM").toString());
				}

				// 面试 报名表实例ID和成绩单ID对应表和总成绩
				Map<String, String> msCSKS = new HashMap<String, String>();
				sql = "SELECT A.TZ_APP_INS_ID,B.TZ_SCORE_NUM FROM PS_TZ_MSPS_KSH_TBL A,PS_TZ_CJX_TBL B WHERE A.TZ_CLASS_ID =? AND A.TZ_APPLY_PC_ID =? AND A.TZ_SCORE_INS_ID = B.TZ_SCORE_INS_ID AND B.TZ_SCORE_ITEM_ID =?";
				list = sqlQuery.queryForList(sql,
						new Object[] { classId, batchId, msRootMap.get("TREE_NODE").toString() });
				for (Map<String, Object> map : list) {
					msCSKS.put(map.get("TZ_APP_INS_ID").toString(),
							map.get("TZ_SCORE_NUM") == null ? "" : map.get("TZ_SCORE_NUM").toString());
				}

				// 专家打分自动去根节点下面第一个
				sql = "SELECT A.TZ_SCORE_ITEM_ID FROM PS_TZ_MODAL_DT_TBL A,PS_TZ_RS_MODAL_TBL B WHERE  A.TZ_SCORE_ITEM_TYPE='B' AND A.TREE_NAME=B.TREE_NAME AND B.TZ_SCORE_MODAL_ID=?  LIMIT 1";
				String itemName = sqlQuery.queryForObject(sql, new Object[] { adminModelId }, "String");

				// 专家打分 报名表实例ID和成绩单ID对应表和总成绩
				Map<String, String> adminCSKS = new HashMap<String, String>();
				sql = "SELECT A.TZ_APP_INS_ID,B.TZ_SCORE_NUM FROM PS_TZ_MSPS_KSH_TBL A,PS_TZ_CJX_TBL B WHERE A.TZ_CLASS_ID =? AND A.TZ_APPLY_PC_ID =? AND A.TZ_SCORE_INS_ID = B.TZ_SCORE_INS_ID AND B.TZ_SCORE_ITEM_ID =?";
				list = sqlQuery.queryForList(sql, new Object[] { classId, batchId, itemName });
				for (Map<String, Object> map : list) {
					adminCSKS.put(map.get("TZ_APP_INS_ID").toString(),
							map.get("TZ_SCORE_NUM") == null ? "" : map.get("TZ_SCORE_NUM").toString());
				}

				long appInsId = 0;
				long scoreInsId = 0;
				
				// 评分标准
				String bzId = "";
				if ("MEM".equals(orgId)){
					bzId=sqlQuery.queryForObject(
							"select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?",
							new Object[] { "TZ_MEM_MS_RESULT" }, "String");
				}else{
					bzId=sqlQuery.queryForObject(
							"select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?",
							new Object[] { "TZ_MBA_MS_RESULT" }, "String");
				}

				String bzsql = "select TZ_M_FBDZ_MX_SM from PS_TZ_FBDZ_MX_TBL where TZ_M_FBDZ_ID=? and TZ_M_FBDZ_MX_SX>=? and TZ_M_FBDZ_MX_XX<=? ";

				String bzDsc = "";

				PsTzCjxTblKey psTzCjxTblKey = null;
				PsTzCjxTblWithBLOBs psTzCjxTblWithBLOBs = null;
				Long LscoreInsId = null;
				PsTzMsPskshTbl psTzMsPskshTbl = null;

				// 自动打分总分
				float autoScore = 0;
				String strAutoScore = "";

				// 面试总分
				float msScore = 0;
				String strMsScore = "";

				// 管理员打分
				float adminScore = 0;
				String strAdminScore = "";

				float sumScore = 0;

				// 班级批次下参与自动初筛的考生 为该班级下 报名表提交并且审核通过的考生
				sql = "SELECT A.TZ_APP_INS_ID FROM PS_TZ_MSPS_KSH_TBL A where A.TZ_CLASS_ID = ? AND A.TZ_APPLY_PC_ID = ?";
				List<Map<String, Object>> appInsList = sqlQuery.queryForList(sql, new Object[] { classId, batchId });
				for (Map<String, Object> appInsMap : appInsList) {
					appInsId = Long.valueOf(appInsMap.get("TZ_APP_INS_ID").toString());

					sql = "select TZ_SCORE_INS_ID from PS_TZ_MSPS_KSH_TBL where TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ? and TZ_APP_INS_ID=?";

					LscoreInsId = sqlQuery.queryForObject(sql,
							new Object[] { classId, batchId, String.valueOf(appInsId) }, "Long");
					if (LscoreInsId == null || LscoreInsId.longValue() <= 0) {
						scoreInsId = tzAutoScreenEngineServiceImpl.creatNewScoreInstanceId(socreModelId, oprId);
						psTzMsPskshTbl = new PsTzMsPskshTbl();
						psTzMsPskshTbl.setTzAppInsId(appInsId);
						psTzMsPskshTbl.setTzApplyPcId(batchId);
						psTzMsPskshTbl.setTzClassId(classId);
						psTzMsPskshTbl.setTzScoreInsId(scoreInsId);
						psTzMsPskshTbl.setRowLastmantDttm(new Date());
						psTzMsPskshTbl.setRowLastmantOprid(oprId);
						psTzMsPskshTblMapper.updateByPrimaryKeySelective(psTzMsPskshTbl);

					} else {
						scoreInsId = LscoreInsId.longValue();
					}

					if (autoCSKS.get(String.valueOf(appInsId)) == null) {
						strAutoScore = "0";
					} else {
						strAutoScore = autoCSKS.get(String.valueOf(appInsId));
					}
					if (strAutoScore.equals("")) {
						autoScore = 0;
					} else {
						autoScore = Float.parseFloat(strAutoScore);
					}

					if (msCSKS.get(String.valueOf(appInsId)) == null) {
						strMsScore = "0";
					} else {
						strMsScore = msCSKS.get(String.valueOf(appInsId));
					}
					if (strMsScore.equals("")) {
						msScore = 0;
					} else {
						msScore = Float.parseFloat(strMsScore);
					}

					if (adminCSKS.get(String.valueOf(appInsId)) == null) {
						strAdminScore = "0";
					} else {
						strAdminScore = adminCSKS.get(String.valueOf(appInsId));
					}
					if (strAdminScore.equals("")) {
						adminScore = 0;
					} else {
						adminScore = Float.parseFloat(strAdminScore);
					}

					sumScore = autoScore + msScore + adminScore;
					// 计算档次
					bzDsc = sqlQuery.queryForObject(bzsql,
							new Object[] { bzId, String.valueOf(sumScore), String.valueOf(sumScore) }, "String");

					psTzCjxTblKey = new PsTzCjxTblKey();
					psTzCjxTblKey.setTzScoreInsId(new Long(scoreInsId));
					psTzCjxTblKey.setTzScoreItemId("SumTotal");
					psTzCjxTblWithBLOBs = psTzCjxTblMapper.selectByPrimaryKey(psTzCjxTblKey);
					if (psTzCjxTblWithBLOBs != null) {
						// 修改表
						psTzCjxTblWithBLOBs = new PsTzCjxTblWithBLOBs();
						psTzCjxTblWithBLOBs.setTzScoreInsId(new Long(scoreInsId));
						// 成绩项ID
						psTzCjxTblWithBLOBs.setTzScoreItemId("SumTotal");
						psTzCjxTblWithBLOBs.setTzScoreNum(new BigDecimal(sumScore));
						psTzCjxTblWithBLOBs.setTzScoreBz(bzDsc);
						psTzCjxTblWithBLOBs.setTzScoreDfgc("自动打分" + String.valueOf(autoScore) + "分|面试打分"
								+ String.valueOf(msScore) + "分|专家打分" + String.valueOf(adminScore) + "分");
						psTzCjxTblMapper.updateByPrimaryKeySelective(psTzCjxTblWithBLOBs);
					} else {
						// 插入表TZ_CJX_TBL
						psTzCjxTblWithBLOBs = new PsTzCjxTblWithBLOBs();
						// 成绩单ID
						psTzCjxTblWithBLOBs.setTzScoreInsId(new Long(scoreInsId));
						// 成绩项ID
						psTzCjxTblWithBLOBs.setTzScoreItemId("SumTotal");
						// 分值
						psTzCjxTblWithBLOBs.setTzScoreNum(new BigDecimal(sumScore));
						psTzCjxTblWithBLOBs.setTzScoreBz(bzDsc);
						psTzCjxTblWithBLOBs.setTzScoreDfgc("自动打分" + String.valueOf(autoScore) + "分|面试打分"
								+ String.valueOf(msScore) + "分|专家打分" + String.valueOf(adminScore) + "分");
						// 插入
						psTzCjxTblMapper.insert(psTzCjxTblWithBLOBs);
					}

				}
				rtnMap.put("status", "0");
				rtnMap.put("msg", "打分成功");

			}
		} catch (Exception e) {
			e.printStackTrace();
			rtnMap.put("status", "-1");
			rtnMap.put("msg", "操作异常");
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
		}

		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}

	/**
	 * 批量发布面试结果
	 * 
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String tzRunReleaseProcess(String strParams, String[] errorMsg) {
		String strRet = "";
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		rtnMap.put("status", "");
		rtnMap.put("msg", "");

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("attaList")) {
				List<Map<String, Object>> list = (List<Map<String, Object>>) jacksonUtil.getList("attaList");
				String jgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				if (list != null && list.size() > 0) {
					for (Map<String, Object> map : list) {
						Long appId = Long.valueOf(map.get("appId") == null ? "" : map.get("appId").toString());
						String msResult = map.get("msResult") == null ? "" : map.get("msResult").toString();
						String YYNL = map.get("YYNL") == null ? "0.00" : map.get("YYNL").toString();
						String strMsYy="";
						if("MEM".equals(jgId)){
							strMsYy=msResult;
						}else{
							strMsYy=msResult+"（英语："+YYNL+"分）";
						}
						
						String sqlCount = "SELECT COUNT(1) FROM TZ_IMP_MSJG_TBL WHERE TZ_APP_INS_ID=?";
						int total = sqlQuery.queryForObject(sqlCount, new Object[] { appId }, "Integer");
						if (total > 0) {
							String strUpdateSql = "UPDATE TZ_IMP_MSJG_TBL SET TZ_RESULT_CODE='" + strMsYy
									+ "' WHERE TZ_APP_INS_ID='" + appId + "'";
							sqlQuery.update(strUpdateSql, new Object[] {});
						} else {
							String strInsertSql = "INSERT INTO TZ_IMP_MSJG_TBL(TZ_APP_INS_ID,TZ_RESULT_CODE) VALUES(?,?)";
							sqlQuery.update(strInsertSql, new Object[] { appId, strMsYy });
						}

					}
					rtnMap.put("status", "0");
					rtnMap.put("msg", "发布成功");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			rtnMap.put("status", "-1");
			rtnMap.put("msg", "操作异常");
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
		}

		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}

}
