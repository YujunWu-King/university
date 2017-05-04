package com.tranzvision.gd.TZUnifiedBaseBundle.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCjxTblMapper;
import com.tranzvision.gd.TZUnifiedBaseBundle.model.PsTzCjxTblKey;
import com.tranzvision.gd.TZUnifiedBaseBundle.model.PsTzCjxTblWithBLOBs;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZUnifiedBaseBundle.service.impl.TzZddfWYServiceImpl")
public class TzZddfWYServiceImpl extends TzZddfServiceImpl {

	@Autowired
	private SqlQuery SqlQuery;
	@Autowired
	private PsTzCjxTblMapper psTzCjxTblMapper;

	// 获取参数：成绩单ID、外语水平成绩项ID、报名表ID
	@Override
	public float AutoCalculate(String TZ_APP_ID, String TZ_SCORE_ID, String TZ_SCORE_ITEM) {
		try {

			// 报名表信息表定义
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

			// 声明float型字段“得分”，string型字段“打分记录”,float最高分
			float Score;
			float Highest = 0;

			String MarkRecord = null;

			String valuesql = "SELECT TZ_XXX_BH, TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE( TZ_APP_INS_ID = ? AND TZ_XXX_BH LIKE '%TZ_44exam_type%')";
			List<Map<String, Object>> SqlCon2 = SqlQuery.queryForList(valuesql, new Object[] { TZ_APP_ID });

			// 定义成绩list
			// List<Float> WYScore = new ArrayList<Float>();

			// 根据报名表ID查询考生循环考生英语成绩，查询考生英语成绩类型
			for (Map<String, Object> map2 : SqlCon2) {
				String WYLXXXX = map2.get("TZ_XXX_BH").toString(); // 外语类型信息项
				String WYLX = map2.get("TZ_APP_S_TEXT").toString(); // 外语类型
				String LX = null;

				String WYCJXXX = WYLXXXX.replace("type", "score"); // 外语成绩信息项
				String EngScoreSql = "SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE( TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?)";
				String WYCJori = SqlQuery.queryForObject(EngScoreSql, new Object[] { TZ_APP_ID, WYCJXXX }, "String"); // 外语成绩
				String WYCJ = WYCJori;
				float FSCJF = 0;

				switch (WYLX) {
				case "专业英语":
					WYLX = "ZYYY";
					LX = "专业英语";
					break;
				case "高级口译":
					WYLX = "GJKY";
					LX = "高级口译";
					break;
				case "中级口译":
					WYLX = "ZJKY";
					LX = "中级口译";
					break;
				case "BEC":
					WYLX = "BEC";
					LX = "BEC";
					break;
				case "GRE":
					WYLX = "GRE";
					LX = "GRE";
					break;
				case "GMAT":
					WYLX = "GMAT";
					LX = "GMAT";
					break;
				case "TOEFL":
					WYLX = "TOFEL";
					LX = "TOEFL";
					break;
				case "IELTS":
					WYLX = "IELTS";
					LX = "IELTS";
					break;
				case "英语六级（710分制）":
					WYLX = "710E6";
					LX = "英语六级（710分制）";
					break;
				case "英语四级（710分制）":
					WYLX = "710E4";
					LX = "英语四级（710分制）";
					break;
				case "英语六级（100分制）":
					WYLX = "100E6";
					LX = "英语六级（100分制）";
					break;
				case "英语四级（100分制）":
					WYLX = "100E4";
					LX = "英语四级（100分制）";
					break;
				case "TOEIC（990）":
					WYLX = "TOEIC";
					LX = "TOEIC（990）";
					break;
				}

				if (WYCJ != null && !WYCJ.equals("")) {
					switch (WYCJ) {
					case "专业八级":
						WYCJ = "A";
						break;
					case "专业四级":
						WYCJ = "B";
						break;
					case "拿到资格证书":
						WYCJ = "A";
						break;
					case "拿到笔试证书":
						WYCJ = "B";
						break;
					case "高级":
						WYCJ = "A";
						break;
					case "中级":
						WYCJ = "B";
						break;
					case "初级":
						WYCJ = "C";
						break;
					case "通过":
						WYCJ = "A";
						break;
					case "未通过":
						WYCJ = "B";
						break;

					}
				}

				if (WYLX.equals("GRE") || WYLX.equals("GMAT") || WYLX.equals("TOFEL") || WYLX.equals("IELTS")
						|| WYLX.equals("710E6") || WYLX.equals("710E4") || WYLX.equals("TOEIC") || WYLX.equals("100E4")
						|| WYLX.equals("100E6")) {
					float CJ = Float.parseFloat(WYCJ);
					String sql = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_WY_T where TZ_CSMB_DESC =? and TZ_CSMB_CK2>=? AND TZ_CSMB_CK3<=?";
					String FSCJ = SqlQuery.queryForObject(sql, new Object[] { WYLX, CJ, CJ }, "String");
					if (FSCJ != null && !FSCJ.equals("")) {
						FSCJF = Float.parseFloat(FSCJ);
						if (FSCJF > Highest) {
							Highest = FSCJF;
						}
					}

					// 证书类型，查询报名表中的证书等级字段
				} else if (WYLX.equals("ZYYY") || WYLX.equals("GJKY") || WYLX.equals("ZJKY") || WYLX.equals("BEC")) {
					String sql2 = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_WY_T where TZ_CSMB_DESC =? and TZ_CSMB_CK1=?";
					String FSCJ = SqlQuery.queryForObject(sql2, new Object[] { WYLX, WYCJ }, "String");
					FSCJF = Float.parseFloat(FSCJ);
					if (FSCJF > Highest) {
						Highest = FSCJF;
					}

				}

				// 记录打分记录：英语成绩类型：GMAT>=750|其他语种：日语二级|100分
				if (MarkRecord != null) {

					MarkRecord = MarkRecord + "英语成绩类型：".concat(LX).concat("=").concat(WYCJori) + "|";// .concat("|学位：").concat(XW);
				} else {

					MarkRecord = "英语成绩类型：".concat(LX).concat("=").concat(WYCJori) + "|";// .concat("|学位：").concat(XW);
				}
			}

			String QTYZsql = "SELECT TZ_XXX_BH, TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE( TZ_APP_INS_ID = ? AND TZ_XXX_BH LIKE '%TZ_18othlang%')";
			List<Map<String, Object>> QTYZMap = SqlQuery.queryForList(QTYZsql, new Object[] { TZ_APP_ID });
			for (Map<String, Object> QTmap2 : QTYZMap) {
				String QTYZ = QTmap2.get("TZ_XXX_BH").toString(); // 其他语种信息项
				String QTYZLX = QTmap2.get("TZ_APP_S_TEXT").toString();

				// 其他语种成绩
				if (QTYZ != null && !QTYZ.equals("")) {
					if (QTYZLX != null && !QTYZLX.equals("")) {

						if (80 > Highest) {
							Highest = 80;
						}

						switch (QTYZLX) {
						case "1":
							QTYZLX = "日语";
							break;
						case "2":
							QTYZLX = "法语";
							break;
						case "3":
							QTYZLX = "韩语";
							break;
						case "4":
							QTYZLX = "俄语";
							break;
						case "5":
							QTYZLX = "西班牙语";
							break;
						case "6":
							QTYZLX = "其他语言";
							break;
						}

						MarkRecord = MarkRecord + "其他语种：".concat(QTYZLX) + "|";

					}
				}

			}

			MarkRecord = MarkRecord.substring(0, MarkRecord.length() - 1);

			String uniScholContry = ksMap.get("TZ_11luniversitycountry");
			String Contry1 = ksMap.get("TZ_10hdegreeunicountry");
			String Xw = ksMap.get("TZ_11TZ_TZ_11_4");
			String ZGXW = ksMap.get("TZ_10hxuewei");

			// 判断 是否有海外学历
			if ("中国大陆".equals(uniScholContry) || "中国".equals(uniScholContry)) { // 中国本科
				if ("中国大陆".equals(Contry1) || "中国".equals(Contry1)) {
				} else { // 中国本科外国硕士 90分
					if ("1".equals(ZGXW) || "2".equals(ZGXW)) {
						if (90 > Highest) {
							Highest = 90;
							MarkRecord = MarkRecord + "|海外硕士";
						}
					}
				}
			} else { // 外国本科 100分
				if ("1".equals(Xw)) {
					Highest = 100;
					MarkRecord = MarkRecord + "|海外本科";
				}
			}

			// 对比考生的英语成绩得分、外语成绩得分，海外院校得分，取得分最大的外语成绩
			// Score=Collections.max(WYScore);
			Score = Highest;
			MarkRecord = MarkRecord + "|" + String.valueOf(Score).concat("分");
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
