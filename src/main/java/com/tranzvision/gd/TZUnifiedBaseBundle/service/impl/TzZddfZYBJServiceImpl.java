package com.tranzvision.gd.TZUnifiedBaseBundle.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

//import com.tranzvision.gd.TZUnifiedBaseBundle.dao.PsTzCjxTblMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.*;
import com.tranzvision.gd.TZUnifiedBaseBundle.model.*;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZUnifiedBaseBundle.service.impl.TzZddfZYBJServiceImpl")
public class TzZddfZYBJServiceImpl extends TzZddfServiceImpl {

	@Autowired
	private SqlQuery SqlQuery;

	@Autowired
	private PsTzCjxTblMapper psTzCjxTblMapper;

	// 获取参数：成绩单ID、职业背景成绩项ID、报名表ID
	@Override
	public float AutoCalculate(String TZ_APP_ID, String TZ_SCORE_ID, String TZ_SCORE_ITEM) {
		try {

			// 报名表信息初始化
			Map<String, String> ksMap = new HashMap<String, String>();
			String ksMapkey = "";
			String ksMapvalue = "";

			// 查询报名表信息
			String ks_valuesql = "SELECT TZ_XXX_BH,TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE  TZ_APP_INS_ID=? ";
			List<Map<String, Object>> listMap = SqlQuery.queryForList(ks_valuesql, new Object[] { TZ_APP_ID });
			for (Map<String, Object> map : listMap) {
				ksMapkey = map.get("TZ_XXX_BH").toString();
				ksMapvalue = map.get("TZ_APP_S_TEXT").toString();
				ksMap.put(ksMapkey, ksMapvalue);
			}

			// 声明float型字段“得分”，string型字段“打分记录”
			float Score = 0;
			String MarkRecord = "报名表填写不完整";
			float Score1 = 0;
			float Score2 = 0;
			float Score3 = 0;

			// 根据报名表ID查询考生职业背景，获取公司性质
			String GSXZ = ksMap.get("TZ_20TZ_TZ_20_14firm_type");
			// 根据报名表ID查询考生创业背景
			// String CYBJ = ksMap.get("TZ_17TZ_TZ_17_4firm_type");

			// 政府机构或事业单位，获取岗位类型
			String ZWLXori = ksMap.get("TZ_20TZ_TZ_20_14position_type");
			String ZWLX = ZWLXori;
			// 职位类型转换
			if (ZWLX != null && !ZWLX.equals("")) {
				switch (ZWLX) {
				case "B1":
					ZWLX = "1";
					break;
				case "B2":
					ZWLX = "2";
					break;
				case "B3":
					ZWLX = "3";
					break;
				case "B4":
					ZWLX = "4";
					break;
				case "B5":
					ZWLX = "5";
					break;
				case "B6":
					ZWLX = "6";
					break;

				default:
					ZWLX = "6";
					break;
				}
			}
			// 年收入字段
			String NSR = ksMap.get("TZ_20TZ_TZ_20_21");

			// 创业类型
			String CY = ksMap.get("TZ_17TZ_TZ_17_20business_type");

			String RZE1 = "0";
			String RZE2 = "0";
			String RZE3 = "0";

			// 融资情况
			String RZ = ksMap.get("TZ_17TZ_TZ_17_20financing_type");
			if (RZ != null && !RZ.equals("")) {
				// 融资数额
				RZE1 = ksMap.get("TZ_17TZ_TZ_17_20financing_binput"); // B轮融资额
				RZE2 = ksMap.get("TZ_17TZ_TZ_17_20financing_ainput"); // A轮融资额
				RZE3 = ksMap.get("TZ_17TZ_TZ_17_20financing_anginput"); // 天使轮融资额
			}

			// 用户数
			String YH = ksMap.get("TZ_17TZ_TZ_17_20user_num");
			int YHS = 0;
			if (YH != null && !YH.equals("")) {
				YHS = Integer.parseInt(YH);
			}
			// 营收情况（近12个月收入）
			String YSQK = ksMap.get("TZ_17TZ_TZ_17_20income_y");
			int YS = 0;
			if (YSQK != null && !YSQK.equals("")) {
				// YS = Integer.parseInt(YSQK);

				String cc2 = "";
				for (int i = 0; i < YSQK.length(); i++) {
					if (YSQK.charAt(i) >= 48 && YSQK.charAt(i) <= 57) {
						cc2 += YSQK.charAt(i);
					}
				}
				YS = Integer.parseInt(cc2);

			}
			// 自有资金
			String ZYZJ = ksMap.get("TZ_17TZ_TZ_17_20own_money");
			// 家族企业资产
			String JZZC = ksMap.get("TZ_17TZ_TZ_17_20family_money");

			// 其他企业营收（近12个月收入）
			String QTYS = ksMap.get("TZ_17TZ_TZ_17_20income_o");
			// 其他企业利润（年纯利润）
			String QTLR = ksMap.get("TZ_17TZ_TZ_17_20year_profit");
			// 其他企业规模
			String QTGM = ksMap.get("TZ_17TZ_TZ_17_20firm_scale");

			// 如果单位性质是政府机构或事业单位，查询报名表职称字段，到表TZ_CSMB_ZY_T查询对应的得分
			if ("05".equals(GSXZ) || "06".equals(GSXZ)) {
				String sql = "SELECT DISTINCT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T where  (TZ_CSMB_CK1='ZFJG' or TZ_CSMB_CK1='SYDW') and TZ_CSMB_CK2=?";
				String FSCJ = SqlQuery.queryForObject(sql, new Object[] { ZWLX }, "String");
				Score = Float.parseFloat(FSCJ);

				switch (ZWLXori) {
				case "B1":
					ZWLXori = "处级及以上";
					break;
				case "B2":
					ZWLXori = "副处级";
					break;
				case "B3":
					ZWLXori = "正科级";
					break;
				case "B4":
					ZWLXori = "副科级";
					break;
				case "B5":
					ZWLXori = "一般科员";
					break;
				case "B6":
					ZWLXori = "其他";
					break;
				default:
					ZWLXori = "其他";

				}

				if ("05".equals(GSXZ)) {
					MarkRecord = "公司类型：".concat("政府机关").concat("|职位类型：").concat(ZWLXori);
				} else {
					MarkRecord = "公司类型：".concat("事业单位").concat("|职位类型：").concat(ZWLXori);
				}

				// 如果单位性质是企业类，查询报名表中的个人年收入字段，到表TZ_CSMB_ZY_T查询对应的得分
			} else if ("01".equals(GSXZ) || "03".equals(GSXZ) || "04".equals(GSXZ)) {

				String sql2 = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T WHERE TZ_CSMB_CK3 <=? AND TZ_CSMB_CK2 >=? AND TZ_CSMB_CK1 = 'QY'";

				float c = Float.parseFloat(NSR);
				int c1 = (int) (c);
				String FSCJ2 = SqlQuery.queryForObject(sql2, new Object[] { c1, c1 }, "String");
				Score = Float.parseFloat(FSCJ2);
				MarkRecord = "公司类型：".concat("企业类").concat("|年收入：").concat(NSR).concat("万元");

				// 如果单位性质是创业类，查询报名表中的创业经历
			}

			float CYScore = 0;
			String CYMarkRecord = "";
			// if("".equals(GSXZ)||"02".equals(CYBJ)){
			if (CY != null && !CY.equals("")) {

				// 互联网类
				if ("01".equals(CY)) {

					// 融资分
					if (RZ == null || RZ.equals("")) {
						CYMarkRecord = "没有填写融资情况";
						Score1 = 0;
					}
					// 初创
					if ("NEW_CREATE".equals(RZ)) {
						Score1 = 50;
						// 尚未获得投资
					} else if ("NO_FINANCING".equals(RZ)) {
						Score1 = 60;
						// A轮B轮天使轮
					} else {
						if ("ANGEL_INVEST".equals(RZ)) {
							String RZ1 = "C";
							// 天使轮
							String sql = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T WHERE TZ_CSMB_TJ2= ? AND TZ_CSMB_TJ1 = 'IT' and TZ_CSMB_TJ3<=? and TZ_CSMB_TJ4>?";
							float TSL = Float.parseFloat(RZE3);
							int TSL1 = (int) (TSL);
							String FSCJ = SqlQuery.queryForObject(sql, new Object[] { RZ1, TSL1, TSL1 }, "String");
							if (FSCJ != null) {
								Score1 = Float.parseFloat(FSCJ);
							}
						} else if ("A_FINANCING".equals(RZ)) {
							String RZ2 = "B";
							// A轮融资
							String sql = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T WHERE TZ_CSMB_TJ2= ? AND TZ_CSMB_TJ1 = 'IT' and TZ_CSMB_TJ3<=? and TZ_CSMB_TJ4>?";
							float ARZ = Float.parseFloat(RZE2);
							int ARZ1 = (int) (ARZ);
							String FSCJ = SqlQuery.queryForObject(sql, new Object[] { RZ2, ARZ1, ARZ1 }, "String");
							if (FSCJ != null) {
								Score1 = Float.parseFloat(FSCJ);
							}
						} else if ("B_FINANCING".equals(RZ)) {
							String RZ3 = "A";
							// B轮融资
							String sql = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T WHERE TZ_CSMB_TJ2= ? AND TZ_CSMB_TJ1 = 'IT' and TZ_CSMB_TJ3<=? and TZ_CSMB_TJ4>?";
							float BRZ = Float.parseFloat(RZE1);
							int BRZ1 = (int) (BRZ);
							String FSCJ = SqlQuery.queryForObject(sql, new Object[] { RZ3, BRZ1, BRZ1 }, "String");
							if (FSCJ != null) {
								Score1 = Float.parseFloat(FSCJ);
							}
						}
					}

					// 营收
					String sql2 = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T WHERE TZ_CSMB_TJ2= 'YS' AND TZ_CSMB_TJ1 = 'IT' and TZ_CSMB_TJ3<=? and TZ_CSMB_TJ4>?";
					String FSCJ2 = SqlQuery.queryForObject(sql2, new Object[] { YS, YS }, "String");
					if (FSCJ2 != null && !FSCJ2.equals("")) {
						Score2 = Float.parseFloat(FSCJ2);
					}

					if (Score1 > Score2) {
						CYScore = Score1;
					} else {
						CYScore = Score2;
					}
					// 用户数
					if (YHS > 1000000) {
						if (90 > CYScore) {
							CYScore = 90;
						}
					}

					String RZQK = "";
					switch (RZ) {
					case "NEW_CREATE":
						RZQK = "初创";
						break;
					case "NO_FINANCING":
						RZQK = "尚未获得投资";
						break;
					case "ANGEL_INVEST":
						RZQK = "天使投资";
						break;
					case "A_FINANCING":
						RZQK = "A轮融资";
						break;
					case "B_FINANCING":
						RZQK = "B轮融资";
						break;
					}

					CYMarkRecord = "自主创业：".concat("互联网类").concat("|融资情况：").concat(RZQK).concat("|营收情况：") + YS
							+ "万元|用户数：" + YHS + "人";

					// 家族创业类
				} else if ("02".equals(CY)) {
					if (ZYZJ == null || ZYZJ.equals("")) {
						ZYZJ = "0";
					}
					if (JZZC == null || JZZC.equals("")) {
						JZZC = "0";
					}
					String sql = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T WHERE TZ_CSMB_TJ2= 'ZYZJ' AND TZ_CSMB_TJ1 = 'JZ' and TZ_CSMB_TJ3<=? and TZ_CSMB_TJ4>?";
					float cc = Float.parseFloat(ZYZJ);
					int c = (int) (cc);
					String FSCJ = SqlQuery.queryForObject(sql, new Object[] { c, c }, "String");
					if (FSCJ != null) {
						Score1 = Float.parseFloat(FSCJ);
					}
					String sql2 = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T WHERE TZ_CSMB_TJ2= 'JZZC' AND TZ_CSMB_TJ1 = 'JZ' and TZ_CSMB_TJ3<=? and TZ_CSMB_TJ4>?";
					int dd = Integer.parseInt(JZZC);
					int d = (int) (dd);
					String FSCJ2 = SqlQuery.queryForObject(sql2, new Object[] { d, d }, "String");
					if (FSCJ2 != null) {
						Score2 = Float.parseFloat(FSCJ2);
					}
					if (Score1 > Score2) {
						CYScore = Score1;
					} else {
						CYScore = Score2;
					}
					CYMarkRecord = "自主创业：".concat("家族企业").concat("|自有资金：").concat(ZYZJ).concat("万元|家族企业资产：")
							.concat(JZZC) + "万元";

					// 其他企业
				} else if ("03".equals(CY)) {
					if (QTYS == null || QTYS.equals("")) {
						QTYS = "0";
					}
					if (QTLR == null || QTLR.equals("")) {
						QTLR = "0";
					}
					if (QTGM == null || QTGM.equals("")) {
						QTGM = "0";
					}

					// 近12个月收入
					String sql = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T WHERE TZ_CSMB_TJ2= 'YS' AND TZ_CSMB_TJ1 = 'QT' and TZ_CSMB_TJ3<=	? and TZ_CSMB_TJ4>?";

					String cc4 = "";
					for (int i = 0; i < QTYS.length(); i++) {
						if (QTYS.charAt(i) >= 48 && QTYS.charAt(i) <= 57) {
							cc4 += QTYS.charAt(i);
						}
					}
					float aa = Float.parseFloat(cc4);
					int a = (int) (aa);

					String FSCJ = SqlQuery.queryForObject(sql, new Object[] { a, a }, "String");
					if (FSCJ != null) {
						Score1 = Float.parseFloat(FSCJ);
					}
					// 年纯利润
					String sql2 = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T WHERE TZ_CSMB_TJ2= 'LR' AND TZ_CSMB_TJ1 = 'QT' and TZ_CSMB_TJ3<=? and TZ_CSMB_TJ4>?";

					String cc3 = "";
					for (int i = 0; i < QTLR.length(); i++) {
						if (QTLR.charAt(i) >= 48 && QTLR.charAt(i) <= 57) {
							cc3 += QTLR.charAt(i);
						}
					}

					float bb = Float.parseFloat(cc3);
					int b = (int) (bb);
					String FSCJ2 = SqlQuery.queryForObject(sql2, new Object[] { b, b }, "String");
					if (FSCJ2 != null) {
						Score2 = Float.parseFloat(FSCJ2);
					}

					// 企业规模
					String sql3 = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T WHERE TZ_CSMB_TJ2= 'GM' AND TZ_CSMB_TJ1 = 'QT' and TZ_CSMB_TJ3<=? and TZ_CSMB_TJ4>?";
					// int cc = Integer.parseInt(QTGM);
					String cc2 = "";
					for (int i = 0; i < QTGM.length(); i++) {
						if (QTGM.charAt(i) >= 48 && QTGM.charAt(i) <= 57) {
							cc2 += QTGM.charAt(i);
						}
					}

					float cc = Float.parseFloat(cc2);
					int c = (int) (cc);
					String FSCJ3 = SqlQuery.queryForObject(sql3, new Object[] { c, c }, "String");
					if (FSCJ3 != null) {
						Score3 = Float.parseFloat(FSCJ3);
					}
					if (Score1 > Score2) {
						CYScore = Score1;
					} else {
						CYScore = Score2;
					}
					if (Score3 > Score) {
						CYScore = Score3;
					}

					CYMarkRecord = "自主创业：".concat("其他类型").concat("|近12个月收入：").concat(QTYS).concat("万元|年纯利润：")
							.concat(QTLR).concat("万元|企业规模：").concat(QTGM) + "人";

				} else {
					CYScore = 0;
					CYMarkRecord = "创业经历填写错误";
				}
			} else {
				CYMarkRecord = "没有填写创业经历";
				CYScore = 0;
			}

			if (CYScore > Score) {
				MarkRecord = CYMarkRecord + ("|") + String.valueOf(CYScore).concat("分");
				Score = CYScore;
			} else {
				MarkRecord = MarkRecord + ("|") + String.valueOf(Score).concat("分");
			}

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
