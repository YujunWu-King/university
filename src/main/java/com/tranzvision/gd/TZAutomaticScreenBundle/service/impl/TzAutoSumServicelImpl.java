package com.tranzvision.gd.TZAutomaticScreenBundle.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCjxTblMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCsKsTblMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzSrmbaInsTblMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCjxTblKey;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCjxTblWithBLOBs;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsKsTbl;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsKsTblKey;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClassInfTMapper;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClassInfT;
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
	private PsTzCsKsTblMapper psTzCsKsTblMapper;

	@Override
	public String tzOther(String strType, String strParams, String[] errorMsg) {
		String strRet = "{}";
		try {
			switch (strType) {
			case "RunSum": // 计算总分
				strRet = this.tzRunSumProcess(strParams, errorMsg);
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
		rtnMap.put("processIns", "");

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

				String orgId = psTzClassInfT.getTzJgId();

				// 自动初筛 报名表实例ID和成绩单ID对应表
				Map<String, String> autoCSKS = new HashMap<String, String>();

				String sql = "select TZ_APP_INS_ID,TZ_SCORE_INS_ID from PS_TZ_CS_KS_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=?";
				List<Map<String, Object>> list = sqlQuery.queryForList(sql, new Object[] { classId, batchId });
				for (Map<String, Object> map : list) {
					autoCSKS.put(map.get("TZ_APP_INS_ID").toString(),
							map.get("TZ_SCORE_INS_ID") == null ? "" : map.get("TZ_SCORE_INS_ID").toString());
				}

				// 自动初筛成绩模型里面配置的，没有自动初筛执行类的成绩项目(也就是留给管理员打分的项目)
				sql = tzSQLObject.getSQLText("SQL.TZAutomaticScreenBundle.TzModelTreeRootNode");
				Map<String, Object> autoRootMap = sqlQuery.queryForMap(sql, new Object[] { socreModelId, orgId });
				Map<String, Object> msRootMap = sqlQuery.queryForMap(sql, new Object[] { msModelId, orgId });
				
				
				//面试   报名表实例ID和成绩单ID对应表
				Map<String, String> msCSKS = new HashMap<String, String>();

				sql = "select TZ_APP_INS_ID,TZ_SCORE_INS_ID from PS_TZ_CS_KS_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=?";
				list = sqlQuery.queryForList(sql, new Object[] { classId, batchId });
				for (Map<String, Object> map : list) {
					autoCSKS.put(map.get("TZ_APP_INS_ID").toString(),
							map.get("TZ_SCORE_INS_ID") == null ? "" : map.get("TZ_SCORE_INS_ID").toString());
				}
				

				long appInsId = 0;
				long scoreInsId = 0;
				String treeName = "";
				String rootNode = "";
				PsTzCjxTblKey psTzCjxTblKey = null;
				PsTzCjxTblWithBLOBs psTzCjxTbl = null;
				PsTzCsKsTblKey psTzCsKsTblKey = null;
				PsTzCsKsTbl psTzCsKsTbl = null;

				// 自动打分总分
				float autoScore = 0;

				// 班级批次下参与自动初筛的考生 为该班级下 报名表提交并且审核通过的考生
				sql = "SELECT A.TZ_APP_INS_ID FROM PS_TZ_MSPS_KSH_TBL A where A.TZ_CLASS_ID = ? AND A.TZ_APPLY_PC_ID = ?";
				List<Map<String, Object>> appInsList = sqlQuery.queryForList(sql, new Object[] { classId, batchId });
				for (Map<String, Object> appInsMap : appInsList) {
					appInsId = Long.valueOf(appInsMap.get("TZ_APP_INS_ID").toString());

					// 由于管理员打分的成绩模型 放在 自动打分模型里面，所以需要重新计算下自动打分模型的 总分
					psTzCsKsTblKey = new PsTzCsKsTblKey();
					psTzCsKsTblKey.setTzClassId(classId);
					psTzCsKsTblKey.setTzApplyPcId(batchId);
					psTzCsKsTblKey.setTzAppInsId(appInsId);
					psTzCsKsTbl = psTzCsKsTblMapper.selectByPrimaryKey(psTzCsKsTblKey);

					scoreInsId = psTzCsKsTbl.getTzScoreInsId() == null ? 0 : psTzCsKsTbl.getTzScoreInsId();

					autoScore = 0;
					if (rootMap != null && scoreInsId > 0) {
						treeName = rootMap.get("TREE_NAME").toString();
						int rootNodeNum = Integer.valueOf(rootMap.get("TREE_NODE_NUM").toString());
						rootNode = rootMap.get("TREE_NODE").toString();

						autoScore = tzAutoScreenEngineServiceImpl.calculateTreeNodeScore(scoreInsId, treeName, orgId,
								rootNodeNum, rootNode);
						BigDecimal rootScore = BigDecimal.valueOf(Double.valueOf(Float.toString(autoScore)));

						psTzCjxTblKey = new PsTzCjxTblKey();
						psTzCjxTblKey.setTzScoreInsId(scoreInsId);
						psTzCjxTblKey.setTzScoreItemId(rootNode);
						psTzCjxTbl = psTzCjxTblMapper.selectByPrimaryKey(psTzCjxTblKey);
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

					} else {
						// 没有自动初筛
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
		}

		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}

}
