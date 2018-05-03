package com.tranzvision.gd.TZUnifiedBaseBundle.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCjxTblMapper;
import com.tranzvision.gd.TZUnifiedBaseBundle.model.*;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 公司背景打分
 * 
 * @author feifei
 *
 */
@Service("com.tranzvision.gd.TZUnifiedBaseBundle.service.impl.TzZddfGJBJServiceImpl")
public class TzZddfGJBJServiceImpl extends TzZddfServiceImpl {
	@Autowired
	private PsTzCjxTblMapper psTzCjxTblMapper;

	@Autowired
	private SqlQuery SqlQuery;

	// 获取参数：成绩单ID、国际化背景成绩项ID、报名表ID
	@Override
	public float AutoCalculate(String TZ_APP_ID, String TZ_SCORE_ID, String TZ_SCORE_ITEM) {
		try {

			// 报名表信息初始化
			Map<String, String> ksMap = new HashMap<String, String>();
			String ksMapkey = "";
			String ksMapvalue = "";

			// 查询报名表信息
			String ks_valuesql = "SELECT TZ_XXX_BH,TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE  TZ_APP_INS_ID=?  AND (TZ_XXX_BH =? or TZ_XXX_BH=? or TZ_XXX_BH=? or TZ_XXX_BH=? or TZ_XXX_BH=? or TZ_XXX_BH=?)";
			List<Map<String, Object>> listMap = SqlQuery.queryForList(ks_valuesql,
					new Object[] { TZ_APP_ID, "workTZ_TZ_9_14", "workTZ_TZ_9_15", "workTZ_TZ_9_17", "workTZ_TZ_9_7",
							"workTZ_work_185firm_type", "workTZ_TZ_9_12" });

			// 企业类型
			String type = "";
			// 个人年收入
			String grValue = "";
			// 总资产
			String dwValue = "";
			// 集团人数
			String qynum = "";

			String desc = "";

			for (Map<String, Object> map : listMap) {
				ksMapkey = map.get("TZ_XXX_BH").toString();
				ksMapvalue = map.get("TZ_APP_S_TEXT").toString();
				ksMap.put(ksMapkey, ksMapvalue);
			}

			StringBuffer sb = new StringBuffer();
			sb.append("报名表数据----报名表ID:");
			sb.append(TZ_APP_ID);
			sb.append(",");
			sb.append("是否500强企业:");
			sb.append(ksMap.get("workTZ_TZ_9_7"));
			sb.append(",");
			sb.append("是否上司公司:");
			sb.append(ksMap.get("workTZ_TZ_9_14"));
			sb.append(",");
			sb.append("企业性质:");
			sb.append(ksMap.get("workTZ_work_185firm_type"));
			sb.append(",");
			sb.append("个人年收入:");
			sb.append(ksMap.get("workTZ_TZ_9_17"));
			sb.append(",");
			sb.append("总资产:");
			sb.append(ksMap.get("workTZ_TZ_9_15"));
			sb.append(",");
			sb.append("集团人数:");
			sb.append(ksMap.get("workTZ_TZ_9_12"));

			System.out.println(sb.toString());

			// 声明float型字段“得分”，string型字段“打分记录”
			float Score = 0;
			float tempScore = 0;
			String strScore = "";
			String MarkRecord = "";

			String SearchSql2 = "select TZ_CSMB_SCOR from PS_TZ_CSMB_ZY_T where TZ_CSMB_CK1=? and TZ_CSMB_CK2=? ";

			// workTZ_TZ_9_14是否是上市公司： workTZ_TZ_9_15资产 workTZ_TZ_9_17年薪
			// workTZ_TZ_9_7是否是500强企业 workTZ_work_185firm_type企业性质
			// workTZ_TZ_9_12集团员工数

			// 企业性质
			if (ksMap.get("workTZ_TZ_9_7") != null && ksMap.get("workTZ_TZ_9_7").equals("Y")) {
				type = "1";
				desc = "500强企业";
			} else if (ksMap.get("workTZ_TZ_9_14") != null && ksMap.get("workTZ_TZ_9_14").equals("Y")) {
				type = "2";
				desc = "上市公司";
			} else if (ksMap.get("workTZ_work_185firm_type") != null
					&& ksMap.get("workTZ_work_185firm_type").equals("05")) {
				desc = "政府机构";
				type = "3";
			} else if (ksMap.get("workTZ_work_185firm_type") != null
					&& ksMap.get("workTZ_work_185firm_type").equals("06")) {
				desc = "事业单位";
				type = "4";
			} else {
				type = "0";
			}

			if (!type.equals("0")) {
				strScore = SqlQuery.queryForObject(SearchSql2, new Object[] { "A", type }, "String");
			}
			if (strScore == null || strScore.equals("")) {
				strScore = "0";
			}

			boolean isGo = false;
			tempScore = Float.parseFloat(strScore);
			Score = tempScore;

			if (tempScore == 4) {
				isGo = true;
			}

			MarkRecord = "企业类型：".concat(desc);
			MarkRecord = MarkRecord + "|" + strScore.concat("分");

			Map<String, String> map = new HashMap<String, String>();
			map.put("5", "workTZ_TZ_9_17");
			map.put("6", "workTZ_TZ_9_12");
			map.put("7", "workTZ_TZ_9_15");

			String SearchSql = "select TZ_CSMB_SCOR from PS_TZ_CSMB_ZY_T where TZ_CSMB_CK1=? and TZ_CSMB_CK2=? and TZ_CSMB_TJ2<=? and TZ_CSMB_TJ1>=?";
			String strI = "";

			if (!isGo) {
				// workTZ_TZ_9_14是否是上市公司： workTZ_TZ_9_15资产 workTZ_TZ_9_17年薪
				// workTZ_TZ_9_7是否是500强企业 workTZ_work_185firm_type企业性质
				// workTZ_TZ_9_12集团员工数
				// 个人年收入
				// 个人年收入:5 单位，集团人数:6 总资产:7

				for (int i = 5; i <= 7; i++) {
					strI = String.valueOf(i);
					strScore = SqlQuery.queryForObject(SearchSql,
							new Object[] { "A", strI, ksMap.get(map.get(strI)), ksMap.get(map.get(strI)) }, "String");
					switch (i) {
					case 5:
						desc = "个人年收入";
						break;
					case 6:
						desc = "单位，集团人数";
						break;
					case 7:
						desc = "总资产";
						break;
					}
					if (strScore == null || strScore.equals("")) {
						strScore = "0";
					}
					tempScore = Float.parseFloat(strScore);
					if (tempScore == 4) {
						Score = tempScore;
						MarkRecord = "公司背景：".concat(desc).concat("=").concat(ksMap.get(map.get(strI))) + "|"
								+ String.valueOf(Score).concat("分");
						break;
					} else {
						if (tempScore > Score) {
							Score = tempScore;
							MarkRecord = "公司背景：".concat(desc).concat("=").concat(ksMap.get(map.get(strI))) + "|"
									+ String.valueOf(Score).concat("分");
						}
					}

				}
			}

			System.out.println(MarkRecord);

			// 插入表TZ_CJX_TBL
			PsTzCjxTblWithBLOBs psTzCjxTblWithBLOBs = new PsTzCjxTblWithBLOBs();
			// 成绩单ID
			Long tzScoreInsId = Long.parseLong(TZ_SCORE_ID);
			psTzCjxTblWithBLOBs.setTzScoreInsId(tzScoreInsId);
			// 成绩项ID
			psTzCjxTblWithBLOBs.setTzScoreItemId(TZ_SCORE_ITEM);
			// 分值
			BigDecimal BigDeScore = new BigDecimal(Float.toString(Score));
			psTzCjxTblWithBLOBs.setTzScoreNum(BigDeScore);
			// 打分记录
			psTzCjxTblWithBLOBs.setTzScoreDfgc(MarkRecord);

			// 删除已有数据
			PsTzCjxTblKey psTzCjxTblKey = new PsTzCjxTblKey();

			psTzCjxTblKey.setTzScoreInsId(tzScoreInsId);
			psTzCjxTblKey.setTzScoreItemId(TZ_SCORE_ITEM);

			psTzCjxTblMapper.deleteByPrimaryKey(psTzCjxTblKey);

			// 插入
			psTzCjxTblMapper.insert(psTzCjxTblWithBLOBs);

			return Score;

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
