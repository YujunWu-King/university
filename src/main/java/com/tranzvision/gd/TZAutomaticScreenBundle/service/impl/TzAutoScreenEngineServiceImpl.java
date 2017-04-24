package com.tranzvision.gd.TZAutomaticScreenBundle.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCjxTblMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCsKsTblMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzSrmbaInsTblMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCjxTblKey;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCjxTblWithBLOBs;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsKsTbl;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsKsTblKey;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzSrmbaInsTbl;
import com.tranzvision.gd.TZAutomaticTagBundle.service.impl.TZAutomaticTagServiceImpl;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClassInfTMapper;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClassInfT;
import com.tranzvision.gd.TZNegativeListInfeBundle.service.impl.TzNegativeListBundleServiceImpl;
import com.tranzvision.gd.TZUnifiedBaseBundle.service.TzClmsZddfInterface;
import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.TZAutomaticScreenBundle.service.impl.TzAutoScreenEngineServiceImpl")
public class TzAutoScreenEngineServiceImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private TZGDObject tzSQLObject;

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private PsTzClassInfTMapper psTzClassInfTMapper;

	@Autowired
	private PsTzCsKsTblMapper psTzCsKsTblMapper;

	@Autowired
	private PsTzSrmbaInsTblMapper psTzSrmbaInsTblMapper;

	@Autowired
	private PsTzCjxTblMapper psTzCjxTblMapper;

	/**
	 * 自动初筛
	 * 
	 * @param classId
	 *            班级ID
	 * @param batchId
	 *            批次ID
	 * @param oprId
	 *            操作人员OPRID
	 * @throws TzException
	 * @throws TzSystemException
	 */
	@SuppressWarnings("unchecked")
	public void autoScreen(String classId, String batchId, String oprId, BaseEngine logger)
			throws TzException, TzSystemException {

		PsTzClassInfT psTzClassInfT = psTzClassInfTMapper.selectByPrimaryKey(classId);
		// 成绩模型ID
		String socreModelId = psTzClassInfT.getTzCsScorMdId();
		String orgId = psTzClassInfT.getTzJgId();
		// 考生标签组
		String ksbqGroup = psTzClassInfT.getTzCsKsbqzId();
		// 负面清单标签组
		String fmqdGroup = psTzClassInfT.getTzCsFmbqzId();

		// 存放考生总成绩，用于计算排名
		List<Map<String, Object>> totalScoreList = new ArrayList<Map<String, Object>>();

		// 成绩项类型为“数字成绩录入项”的节点，查询节点对应的成绩项打分规则ID
		String itemsSql = tzSQLObject.getSQLText("SQL.TZAutomaticScreenBundle.TzModelTreeAutoScreenItems");
		List<Map<String, Object>> itemsList = sqlQuery.queryForList(itemsSql, new Object[] { socreModelId, orgId });

		// 成绩模型树根节点
		String rootSql = tzSQLObject.getSQLText("SQL.TZAutomaticScreenBundle.TzModelTreeRootNode");
		Map<String, Object> rootMap = sqlQuery.queryForMap(rootSql, new Object[] { socreModelId, orgId });

		// 班级批次下参与自动初筛的考生
		String sql = "select TZ_APP_INS_ID from PS_TZ_CS_STU_VW where TZ_CLASS_ID=? and TZ_BATCH_ID=?";
		List<Map<String, Object>> appInsList = sqlQuery.queryForList(sql, new Object[] { classId, batchId });
		for (Map<String, Object> appInsMap : appInsList) {
			long appInsId = Long.valueOf(appInsMap.get("TZ_APP_INS_ID").toString());

			PsTzCsKsTblKey psTzCsKsTblKey = new PsTzCsKsTblKey();
			psTzCsKsTblKey.setTzClassId(classId);
			psTzCsKsTblKey.setTzApplyPcId(batchId);
			psTzCsKsTblKey.setTzAppInsId(appInsId);
			PsTzCsKsTbl psTzCsKsTbl = psTzCsKsTblMapper.selectByPrimaryKey(psTzCsKsTblKey);
			// System.out.println("---------->"+classId+"---"+batchId+"---"+appInsId);
			long scoreInsId = 0;
			boolean exists;
			if (psTzCsKsTbl == null) {
				exists = false;
				scoreInsId = this.creatNewScoreInstanceId(socreModelId, oprId);

				psTzCsKsTbl = new PsTzCsKsTbl();
				psTzCsKsTbl.setTzClassId(classId);
				psTzCsKsTbl.setTzApplyPcId(batchId);
				psTzCsKsTbl.setTzAppInsId(appInsId);
				psTzCsKsTbl.setRowAddedDttm(new Date());
				psTzCsKsTbl.setRowAddedOprid(oprId);
			} else {
				exists = true;
				scoreInsId = psTzCsKsTbl.getTzScoreInsId() == null ? 0 : psTzCsKsTbl.getTzScoreInsId();
				if (scoreInsId > 0) {
				} else {
					scoreInsId = this.creatNewScoreInstanceId(socreModelId, oprId);
				}
			}

			psTzCsKsTbl.setTzScoreInsId(scoreInsId);
			psTzCsKsTbl.setRowLastmantDttm(new Date());
			psTzCsKsTbl.setRowLastmantOprid(oprId);

			// 更新自动初筛考生表
			if (exists) {
				psTzCsKsTblMapper.updateByPrimaryKeySelective(psTzCsKsTbl);
			} else {
				psTzCsKsTblMapper.insert(psTzCsKsTbl);
			}

			// 循环自动初筛成绩模型，获取所有成绩项类型为“数字成绩录入项”的节点，查询节点对应的成绩项打分规则ID
			for (Map<String, Object> itemMap : itemsList) {
				String itemId = itemMap.get("TZ_SCORE_ITEM_ID").toString();
				String csDfgzId = itemMap.get("TZ_ZDCSGZ_ID") == null ? "" : itemMap.get("TZ_ZDCSGZ_ID").toString();
				String csDfgzCls = itemMap.get("TZ_ZDCSGZ") == null ? "" : itemMap.get("TZ_ZDCSGZ").toString();

				if (!"".equals(csDfgzCls)) {
					// 执行自动打分规则
					try {
						TzClmsZddfInterface zzdfObj = (TzClmsZddfInterface) ctx.getBean(csDfgzCls);
						zzdfObj.AutoCalculate(String.valueOf(appInsId), String.valueOf(scoreInsId), itemId);
					} catch (Exception e1) {
						e1.printStackTrace();
						// throw new
						// TzException("自动打分规则定义不正确,规则编号："+csDfgzId+",错误原因："+e1.getMessage());
						logger.logError("自动打分规则定义不正确,规则编号：" + csDfgzId + ",错误原因：" + e1.getMessage());
					}
				}
			}

			// 计算总分,即根节点得分
			float rootScoreAmount = 0;
			if (rootMap != null) {
				String treeName = rootMap.get("TREE_NAME").toString();
				int rootNodeNum = Integer.valueOf(rootMap.get("TREE_NODE_NUM").toString());
				String rootNode = rootMap.get("TREE_NODE").toString();

				rootScoreAmount = calculateTreeNodeScore(scoreInsId, treeName, orgId, rootNodeNum, rootNode);
				BigDecimal rootScore = BigDecimal.valueOf(Double.valueOf(Float.toString(rootScoreAmount)));

				PsTzCjxTblKey psTzCjxTblKey = new PsTzCjxTblKey();
				psTzCjxTblKey.setTzScoreInsId(scoreInsId);
				psTzCjxTblKey.setTzScoreItemId(rootNode);
				PsTzCjxTblWithBLOBs psTzCjxTbl = psTzCjxTblMapper.selectByPrimaryKey(psTzCjxTblKey);
				if (psTzCjxTbl != null) {
					psTzCjxTbl.setTzScoreNum(rootScore);
					psTzCjxTblMapper.updateByPrimaryKey(psTzCjxTbl);
				} else {
					psTzCjxTbl = new PsTzCjxTblWithBLOBs();
					psTzCjxTbl.setTzScoreInsId(scoreInsId);
					psTzCjxTbl.setTzScoreItemId(rootNode);
					psTzCjxTbl.setTzScoreNum(rootScore);
					psTzCjxTblMapper.insert(psTzCjxTbl);
				}

				Map<String, Object> ksScoreMap = new HashMap<String, Object>();
				ksScoreMap.put("appInsId", appInsId);
				ksScoreMap.put("scoreNum", rootScoreAmount);
				totalScoreList.add(ksScoreMap);
			}
		}

		/*********************************** 计算排名开始 *************************************/
		// 计算排名,根据总分倒序排序
		Collections.sort(totalScoreList, new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				Map<String, Object> map1 = (Map<String, Object>) o1;
				Map<String, Object> map2 = (Map<String, Object>) o2;

				float score1 = (float) map1.get("scoreNum");
				float score2 = (float) map2.get("scoreNum");

				return -(new Float(score1).compareTo(new Float(score2)));
			}
		});

		int count = 0;
		short sortNum = 0;
		float tmp_score = 0;
		for (Map<String, Object> scoreMap : totalScoreList) {
			long appInsId = Long.valueOf(scoreMap.get("appInsId").toString());
			float scoreNum = Float.valueOf(scoreMap.get("scoreNum").toString());

			if (count == 0) {
				sortNum = 1;
				tmp_score = scoreNum;
			} else {
				if (scoreNum != tmp_score) {
					tmp_score = scoreNum;
					sortNum = (short) (count + 1);
				}
			}
			count++;

			PsTzCsKsTblKey psTzCsKsTblKey = new PsTzCsKsTblKey();
			psTzCsKsTblKey.setTzClassId(classId);
			psTzCsKsTblKey.setTzApplyPcId(batchId);
			psTzCsKsTblKey.setTzAppInsId(appInsId);
			PsTzCsKsTbl psTzCsKsTbl = psTzCsKsTblMapper.selectByPrimaryKey(psTzCsKsTblKey);
			if (psTzCsKsTbl != null) {
				psTzCsKsTbl.setTzKshPspm(sortNum);
				psTzCsKsTblMapper.updateByPrimaryKey(psTzCsKsTbl);
			}
		}
		/*********************************** 计算排名结束 *******************************************/

		String autoLabelSql = tzSQLObject.getSQLText("SQL.TZAutomaticScreenBundle.TzKshAutoLabel");
		/***************** 循环执行班级下考生标签组标签定义java类---开始 *****************/
		if (!"".equals(ksbqGroup) && ksbqGroup != null) {
			List<Map<String, Object>> labelList = sqlQuery.queryForList(autoLabelSql,
					new Object[] { orgId, ksbqGroup });
			for (Map<String, Object> labelMap : labelList) {
				String labelId = labelMap.get("TZ_BIAOQ_ID").toString();
				String javaClass = labelMap.get("TZ_BIAOQZ_JAVA") == null ? ""
						: labelMap.get("TZ_BIAOQZ_JAVA").toString();

				if (!"".equals(javaClass) && javaClass != null) {
					try {
						System.out.println("javaClass:" + javaClass);
						TZAutomaticTagServiceImpl autoTagObj = (TZAutomaticTagServiceImpl) ctx.getBean(javaClass);
						autoTagObj.automaticTagList(classId, batchId, labelId);
					} catch (Exception e2) {
						e2.printStackTrace();
						// throw new
						// TzException("考生自动标签组："+ksbqGroup+",标签ID："+labelId+"定义有误。"+e2.getMessage());
						logger.logError("考生自动标签组：" + ksbqGroup + ",标签ID：" + labelId + "定义有误。" + e2.getMessage());
					}
				}
			}
		}
		/***************** 循环执行班级下考生标签组标签定义java类---结束 *****************/

		/***************** 循环执行班级下负面清单标签组标签定义java类---开始 *****************/
		if (!"".equals(fmqdGroup) && fmqdGroup != null) {
			List<Map<String, Object>> fmqdList = sqlQuery.queryForList(autoLabelSql, new Object[] { orgId, fmqdGroup });
			for (Map<String, Object> fmqdMap : fmqdList) {
				String labelId = fmqdMap.get("TZ_BIAOQ_ID").toString();
				String javaClass = fmqdMap.get("TZ_BIAOQZ_JAVA") == null ? ""
						: fmqdMap.get("TZ_BIAOQZ_JAVA").toString();

				if (!"".equals(javaClass) && javaClass != null) {
					try {
						System.out.println("javaClass:" + javaClass);

						TzNegativeListBundleServiceImpl neGListObj = (TzNegativeListBundleServiceImpl) ctx
								.getBean(javaClass);

						/* TzNegativeListBundleServiceImpl neGListObj = new */
						neGListObj.makeNegativeList(classId, batchId, labelId);
					} catch (Exception e3) {
						System.out.print("e3.toString:" + e3.getMessage());
						e3.printStackTrace();
						// throw new
						// TzException("考生负面清单标签组："+ksbqGroup+",标签ID："+labelId+"定义有误。"+e3.getMessage());
						logger.logError("考生负面清单标签组：" + fmqdGroup + ",标签ID：" + labelId + "定义有误。" + e3.getMessage());
					}
				}
			}
		}
		/***************** 循环执行班级下负面清单标签组标签定义java类---结束 *****************/
	}

	/**
	 * 计算分值
	 * 
	 * @param scoreInsId
	 * @param treeName
	 * @param orgId
	 * @param treeNodeNum
	 * @param treeNode
	 * @return
	 */
	public float calculateTreeNodeScore(long scoreInsId, String treeName, String orgId, int treeNodeNum,
			String treeNode) {
		// 当前节点的总分值（若是叶子节点，则为 分值*权重的和；若非叶子，则是其下级叶子的分值和）;
		float nodeScoreAmount = 0;
		try {
			// 获取子节点数据
			String sql = tzSQLObject.getSQLText("SQL.TZAutomaticScreenBundle.TzModelTreeChildNode");
			List<Map<String, Object>> childNodeList = sqlQuery.queryForList(sql,
					new Object[] { treeNodeNum, treeName, orgId });

			for (Map<String, Object> childNodeMap : childNodeList) {
				int childNodeNum = Integer.valueOf(childNodeMap.get("TREE_NODE_NUM").toString());
				String childNode = childNodeMap.get("TREE_NODE").toString();
				String nodeType = childNodeMap.get("TZ_SCORE_ITEM_TYPE").toString();

				// 是否存在子节点
				String hasChild = childNodeMap.get("TZ_HAS_CHILD") == null ? "N"
						: childNodeMap.get("TZ_HAS_CHILD").toString();

				if ("Y".equals(hasChild)) {
					// 存在子节点，递归
					nodeScoreAmount = nodeScoreAmount
							+ this.calculateTreeNodeScore(scoreInsId, treeName, orgId, childNodeNum, childNode);
				} else {

					sql = "select TZ_SCORE_NUM from PS_TZ_CJX_TBL where TZ_SCORE_INS_ID=? and TZ_SCORE_ITEM_ID=?";
					Map<String, Object> scoreMap = sqlQuery.queryForMap(sql, new Object[] { scoreInsId, childNode });

					if (scoreMap != null && "B".equals(nodeType)) {
						float val = Float.valueOf(scoreMap.get("TZ_SCORE_NUM").toString());
						// 权重
						Float weight = Float.valueOf(childNodeMap.get("TZ_SCORE_QZ").toString());
						// 计算该成绩项的权重值
						nodeScoreAmount = nodeScoreAmount + (val * weight) / 100;
					}
				}
			}

			// 当前节点向上汇总系数
			String hzSql = "SELECT TZ_SCORE_HZ FROM PS_TZ_MODAL_DT_TBL WHERE TZ_JG_ID=? AND TREE_NAME=? AND TZ_SCORE_ITEM_ID=?";
			float HzNum = sqlQuery.queryForObject(hzSql, new Object[] { orgId, treeName, treeNode }, "Float");

			nodeScoreAmount = nodeScoreAmount * HzNum / 100;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return nodeScoreAmount;
	}

	/**
	 * 创建成绩单ID
	 * 
	 * @param modelId
	 * @param oprId
	 * @return
	 */
	public Long creatNewScoreInstanceId(String modelId, String oprId) {
		long scoreInsId = 0;
		try {
			String dtFormat = getSysHardCodeVal.getDateFormat();
			SimpleDateFormat format = new SimpleDateFormat(dtFormat);

			scoreInsId = getSeqNum.getSeqNum("TZ_SRMBAINS_TBL", "TZ_SCORE_INS_ID");

			PsTzSrmbaInsTbl psTzSrmbaInsTbl = new PsTzSrmbaInsTbl();
			psTzSrmbaInsTbl.setTzScoreInsId(scoreInsId);
			psTzSrmbaInsTbl.setTzScoreModalId(modelId);

			Date scoreInsDate = format.parse("2099-12-31");
			psTzSrmbaInsTbl.setTzScoreInsDate(scoreInsDate);
			psTzSrmbaInsTbl.setRowAddedDttm(new Date());
			psTzSrmbaInsTbl.setRowAddedOprid(oprId);
			psTzSrmbaInsTbl.setRowLastmantDttm(new Date());
			psTzSrmbaInsTbl.setRowLastmantOprid(oprId);

			psTzSrmbaInsTblMapper.insert(psTzSrmbaInsTbl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return scoreInsId;
	}

}
