package com.tranzvision.gd.TZUnifiedBaseBundle.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCjxTblMapper;
import com.tranzvision.gd.TZUnifiedBaseBundle.model.*;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * MEM公司背景打分
 * 
 * @author feifei
 *
 */
@Service("com.tranzvision.gd.TZUnifiedBaseBundle.service.impl.TzZddfMEMGJBJServiceImpl")
public class TzZddfMEMGJBJServiceImpl extends TzZddfServiceImpl {
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
			String ks_valuesql = "SELECT TZ_XXX_BH,TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE  TZ_APP_INS_ID=?  AND (TZ_XXX_BH =? or TZ_XXX_BH=? or TZ_XXX_BH=?  or TZ_XXX_BH=? or TZ_XXX_BH=?)";
			List<Map<String, Object>> listMap = SqlQuery.queryForList(ks_valuesql, new Object[] { TZ_APP_ID,
					"TZ_20TZ_TZ_20_14", "TZ_20TZ_TZ_20_15", "TZ_20TZ_TZ_20_18", "TZ_20TZ_TZ_20_7", "TZ_20TZ_TZ_20_12" });

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
				ksMapkey = map.get("TZ_XXX_BH") == null ? "" : map.get("TZ_XXX_BH").toString();
				ksMapvalue = map.get("TZ_APP_S_TEXT") == null ? "" : map.get("TZ_APP_S_TEXT").toString();
				ksMap.put(ksMapkey, ksMapvalue);
			}

			StringBuffer sb = new StringBuffer();
			sb.append("报名表数据----报名表ID:");
			sb.append(TZ_APP_ID);
			sb.append(",");
			sb.append("是否500强企业:");
			sb.append(ksMap.get("TZ_20TZ_TZ_20_7"));
			sb.append(",");
			sb.append("是否上司公司:");
			sb.append(ksMap.get("TZ_20TZ_TZ_20_14"));
			sb.append(",");
			sb.append("个人年收入:");
			sb.append(ksMap.get("TZ_20TZ_TZ_20_18"));
			sb.append(",");
			sb.append("总资产:");
			sb.append(ksMap.get("TZ_20TZ_TZ_20_15"));
			sb.append(",");
			sb.append("集团人数:");
			sb.append(ksMap.get("TZ_20TZ_TZ_20_12"));

			System.out.println(sb.toString());

			// 声明float型字段“得分”，string型字段“打分记录”
			float Score = 0;
			float tempScore = 0;
			String strScore = "";
			String MarkRecord = "";

			String SearchSql2 = "select TZ_CSMB_SCOR from PS_TZ_CSMB_ZY_T where TZ_CSMB_CK1=? and TZ_CSMB_CK2=? ";

			// TZ_20TZ_TZ_20_14是否是上市公司： TZ_20TZ_TZ_20_15资产 TZ_20TZ_TZ_20_18年薪
			// TZ_20TZ_TZ_20_7是否是500强企业 
			// TZ_20TZ_TZ_20_12集团员工数

			// 企业性质
			if (ksMap.get("TZ_20TZ_TZ_20_7") != null && ksMap.get("TZ_20TZ_TZ_20_7").equals("Y")) {
				type = "1";
				desc = "500强企业";
			} else if (ksMap.get("TZ_20TZ_TZ_20_14") != null && ksMap.get("TZ_20TZ_TZ_20_14").equals("Y")) {
				type = "2";
				desc = "上市公司";
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
			map.put("5", "TZ_20TZ_TZ_20_18");
			map.put("6", "TZ_20TZ_TZ_20_12");
			map.put("7", "TZ_20TZ_TZ_20_15");

			String SearchSql = "select TZ_CSMB_SCOR from PS_TZ_CSMB_ZY_T where TZ_CSMB_CK1=? and TZ_CSMB_CK2=? and TZ_CSMB_TJ2>=? and TZ_CSMB_TJ1<=? limit 1";
			String strI = "";

			if (!isGo) {
				// TZ_20TZ_TZ_20_14是否是上市公司： TZ_20TZ_TZ_20_15资产 TZ_20TZ_TZ_20_18年薪
				// TZ_20TZ_TZ_20_7是否是500强企业 
				// TZ_20TZ_TZ_20_12集团员工数
				// 个人年收入
				// 个人年收入:5 单位，集团人数:6 总资产:7
				String temp = "";
				for (int i = 5; i <= 7; i++) {
					strI = String.valueOf(i);
					if (ksMap.get(map.get(strI)) == null || ksMap.get(map.get(strI)).equals("")) {
						temp = "0";
					} else {
						temp = ksMap.get(map.get(strI));
					}
					strScore = SqlQuery.queryForObject(SearchSql,
							new Object[] { "A", strI, Float.parseFloat(temp), Float.parseFloat(temp) }, "String");
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
