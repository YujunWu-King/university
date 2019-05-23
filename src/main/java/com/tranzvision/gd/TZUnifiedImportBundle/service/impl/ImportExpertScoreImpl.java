package com.tranzvision.gd.TZUnifiedImportBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZUnifiedImportBundle.service.UnifiedImportBase;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZUnifiedImportBundle.service.impl.ImportExpertScoreImpl")
public class ImportExpertScoreImpl implements UnifiedImportBase {

	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;

	/**
	 * 保存导入的数据（专家打分使用面试成绩项ID）
	 */
	public String tzSave(List<Map<String, Object>> data, List<String> fields, String targetTbl, int[] result,
			String[] errMsg) {
		String strRet = "";
		try {
			// modity by caoy 2019-5-23 优化效率
			// 加载所有的班级和批次，放入2个HashMap
			// 存放班级信息
			Map<String, String> cls = new HashMap<String, String>();
			// 存放班级信息
			Map<String, String> batch = new HashMap<String, String>();

			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String classSQL = "SELECT TZ_CLASS_ID,TZ_CLASS_NAME FROM PS_TZ_CLASS_INF_T WHERE TZ_JG_ID=?";
			List<Map<String, Object>> classlist = sqlQuery.queryForList(classSQL, new Object[] { orgId });

			String batchSQL = "SELECT TZ_CLASS_ID,TZ_BATCH_ID,TZ_BATCH_NAME FROM PS_TZ_CLS_BATCH_T ";
			List<Map<String, Object>> batchlist = sqlQuery.queryForList(batchSQL);
			Map<String, Object> listmap = null;
			for (int i = 0; i < classlist.size(); i++) {
				listmap = classlist.get(i);
				cls.put(String.valueOf(listmap.get("TZ_CLASS_NAME")), String.valueOf(listmap.get("TZ_CLASS_ID")));
			}
			listmap = null;
			for (int i = 0; i < batchlist.size(); i++) {
				listmap = batchlist.get(i);
				batch.put(
						String.valueOf(listmap.get("TZ_CLASS_ID")) + "@" + String.valueOf(listmap.get("TZ_BATCH_NAME")),
						String.valueOf(listmap.get("TZ_BATCH_ID")));
			}

			String TZ_MSH_ID = "";
			String TZ_CLASS_NAME = "";
			String TZ_BATCH_NAME = "";
			String TZ_SCORE_NUM = "";

			String stuSql = "SELECT A.TZ_SCORE_INS_ID FROM PS_TZ_MSPS_KSH_TBL A,PS_TZ_AQ_YHXX_TBL B,PS_TZ_FORM_WRK_T C WHERE A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_APP_INS_ID=C.TZ_APP_INS_ID AND C.OPRID=B.OPRID AND B.TZ_MSH_ID =?";

			String isExist = "SELECT COUNT(1) FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID='ZYXX'";
			// 更新SQL
			String updateSql = "UPDATE PS_TZ_CJX_TBL SET TZ_SCORE_NUM=?,TZ_SCORE_DFGC=? WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID=?";
			String strInsertSql = "INSERT INTO PS_TZ_CJX_TBL(TZ_SCORE_INS_ID,TZ_SCORE_ITEM_ID,TZ_SCORE_NUM,TZ_SCORE_DFGC) VALUES(?,?,?,?)";
			String classID = "";
			String batchID = "";
			String scoreId = "";
			String scoreDfgc = "";
			int count = 0;
			if (data != null && data.size() > 0) {
				for (int i = 0; i < data.size(); i++) {
					TZ_MSH_ID = ((String) data.get(i).get("TZ_MSH_ID"));
					TZ_CLASS_NAME = ((String) data.get(i).get("TZ_CLASS_NAME"));
					TZ_BATCH_NAME = ((String) data.get(i).get("TZ_BATCH_NAME"));
					TZ_SCORE_NUM = ((String) data.get(i).get("TZ_SCORE_NUM"));

					classID = cls.get(TZ_CLASS_NAME);
					if (classID != null && classID.equals("")) {
						batchID = batch.get(classID + "@" + TZ_BATCH_NAME);
						if (batchID != null && batchID.equals("")) {
							scoreId = sqlQuery.queryForObject(stuSql, new Object[] { classID, batchID, TZ_MSH_ID },
									"String");
							if (scoreId != null && !"".equals(scoreId)) {

								count = sqlQuery.queryForObject(isExist, new Object[] { scoreId }, "Integer");
								scoreDfgc = "职业形象及配合度|" + TZ_SCORE_NUM + "分";
								if (count > 0) {
									sqlQuery.update(updateSql,
											new Object[] { TZ_SCORE_NUM, scoreDfgc, scoreId, "ZYXX" });
								} else {

									sqlQuery.update(strInsertSql,
											new Object[] { scoreId, "ZYXX", TZ_SCORE_NUM, scoreDfgc });
								}
							}
						} else {
							continue;
						}
					} else {
						continue;
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	@Override
	public void tzValidate(List<Map<String, Object>> data, List<String> fields, String targetTbl, Object[] result,
			String[] errMsg) {
		try {

			ArrayList<String> resultMsg = new ArrayList<String>();
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			// 开始校验数据
			if (data != null && data.size() > 0) {
				for (int i = 0; i < data.size(); i++) {
					String TZ_MSH_ID = ((String) data.get(i).get("TZ_MSH_ID"));
					String TZ_CLASS_NAME = ((String) data.get(i).get("TZ_CLASS_NAME"));
					String TZ_BATCH_NAME = ((String) data.get(i).get("TZ_BATCH_NAME"));
					if (TZ_MSH_ID == null || "".equals(TZ_MSH_ID) || TZ_CLASS_NAME == null || "".equals(TZ_CLASS_NAME)
							|| TZ_BATCH_NAME == null || "".equals(TZ_BATCH_NAME)) {
						result[0] = false;
						resultMsg.add("第[" + (i + 1) + "]行面试申请号、班级名称、批次名称都不能为空");
					} else {
						// 查询班级ID
						String TZ_CLASS_ID = sqlQuery.queryForObject(
								"SELECT TZ_CLASS_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_JG_ID=? AND TZ_CLASS_NAME=? LIMIT 0,1",
								new Object[] { orgId, TZ_CLASS_NAME }, "String");

						if (TZ_CLASS_ID == null || "".equals(TZ_CLASS_ID)) {
							result[0] = false;
							resultMsg.add("第[" + (i + 1) + "]行找不到该班级");
						} else {
							// 查询批次ID
							String TZ_BATCH_ID = sqlQuery.queryForObject(
									"SELECT TZ_BATCH_ID FROM PS_TZ_CLS_BATCH_T WHERE TZ_CLASS_ID=? AND TZ_BATCH_NAME=? LIMIT 0,1",
									new Object[] { TZ_CLASS_ID, TZ_BATCH_NAME }, "String");

							if (TZ_BATCH_ID == null || "".equals(TZ_BATCH_ID)) {
								result[0] = false;
								resultMsg.add("第[" + (i + 1) + "]行找不到该批次");
							} else {
								// 检查面试申请号是否存在用户信息表中
								String TZ_MSH_ID_EXIST = sqlQuery.queryForObject(
										"SELECT 'Y' FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_MSH_ID=?",
										new Object[] { TZ_MSH_ID }, "String");
								if (!"Y".equals(TZ_MSH_ID_EXIST)) {
									result[0] = false;
									resultMsg.add("第[" + (i + 1) + "]行面试申请号不存在");
								}
							}
						}

					}
				}
			} else {
				resultMsg.add("您没有导入任何数据！");
			}

			result[1] = String.join("，", (String[]) resultMsg.toArray(new String[resultMsg.size()]));

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
	}

}
