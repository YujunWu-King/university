package com.tranzvision.gd.TZUnifiedBaseBundle.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCjxTblMapper;
import com.tranzvision.gd.TZUnifiedBaseBundle.model.PsTzCjxTblKey;
import com.tranzvision.gd.TZUnifiedBaseBundle.model.PsTzCjxTblWithBLOBs;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZUnifiedBaseBundle.service.impl.TzZddMPACCWYServiceImpl")
public class TzZddMPACCWYServiceImpl extends TzZddfServiceImpl{
	@Autowired
	private SqlQuery SqlQuery;
	@Autowired
	private PsTzCjxTblMapper psTzCjxTblMapper;

	public static void main(String[] args) throws Exception {
		TzZddfWYServiceImpl a = new TzZddfWYServiceImpl();
		System.out.println(Integer.parseInt("01"));

	}

	private String chage(String s) {
		// TZ_7TZ_TZ_7_2 --> TZ_7TZ_TZ_7_1 或 TZ_7TZ_TZ_7_2_1-->TZ_7TZ_TZ_7_1_1
		if (s.length() == 13) {
			return s.substring(0, s.length() - 1) + "1";
		} else {
			return s.substring(0, 12) + "1" + s.substring(13, s.length());
		}
	}

	// 取字符串中的连续数字(包含小数点)
	private String getScore(String s) {
		StringBuffer sb = new StringBuffer();
		boolean flag = false;
		boolean numflag = false;
		String result = "";
		char[] b = s.toCharArray();
		for (int i = 0; i < b.length; i++) {
			if (("0123456789.").indexOf(b[i] + "") != -1) {
				if (".".equals(b[i] + "")) {
					if (flag) {
						break;
					} else {
						flag = true;
					}
				}
				if (!numflag) {
					sb = new StringBuffer();
					numflag = true;
				}
				sb.append(b[i]);
			} else {
				if (sb.toString().equals(".")) {
					sb = new StringBuffer();
					flag = false;
					numflag = false;
				}
				if (numflag) {
					break;
				}
			}
		}
		//
		result = sb.toString();
		// System.out.println(result);
		if (result.equals("") || result.equals(".")) {
			result = "0";
		}
		if (result.startsWith(".")) {
			result = "0" + result;
		}
		return result;

	}

	// 获取参数：成绩单ID、外语水平成绩项ID、报名表ID
	@Override
	public float AutoCalculate(String TZ_APP_ID, String TZ_SCORE_ID, String TZ_SCORE_ITEM) {
		try {

			// 声明float型字段“得分”，string型字段“打分记录”,float最高分
			float Score = 0;

			String MarkRecord = null;

			String valuesql = "SELECT TZ_XXX_BH, TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID = ? AND (TZ_XXX_BH like 'TZ_7TZ_TZ_7_2%' or TZ_XXX_BH like 'TZ_7TZ_TZ_7_1%')";
			List<Map<String, Object>> SqlCon2 = SqlQuery.queryForList(valuesql, new Object[] { TZ_APP_ID });
			
			
			// 定义成绩list
			Map<String, String> engMap = new HashMap<String, String>();

			Map<String, String> souseMap = new HashMap<String, String>();

			String TZ_XXX_BH = "";
			String TZ_APP_S_TEXT = "";

			String engType = "";

			// 根据报名表ID查询考生循环考生英语成绩，查询考生英语成绩类型
			for (Map<String, Object> map2 : SqlCon2) {
				TZ_XXX_BH = map2.get("TZ_XXX_BH").toString(); // 外语类型
				TZ_APP_S_TEXT = map2.get("TZ_APP_S_TEXT") == null ? "" : map2.get("TZ_APP_S_TEXT").toString(); // 分数
				engMap.put(TZ_XXX_BH, TZ_APP_S_TEXT);
			}
			
			StringBuffer sb = new StringBuffer();
			sb.append("报名表数据----报名表ID:");
			sb.append(TZ_APP_ID);
			String value="";
			String key = "";
			String keyType = "";
			Iterator<Entry<String, String>> it = engMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				key = entry.getKey();

				// 如果是值，那么找到他的类型
				if (key.startsWith("TZ_7TZ_TZ_7_1")) {
					// 得到对应的考试类型
					keyType = chage(key);
					// 报名表中的字段 1:GRE 2:GMAT 3:托福TOFEL 4:TOEFL 机考 5:TOFEL IBT(网考）
					// 6:雅思IELTS 7:专业八级（TEM8） 8:专业四级（TEM4） 9:剑桥商务英语（BEC高级）
					// 10:剑桥商务英语（BEC中级） 11:CET-4 12:CET-6
					// GRE GMAT TOFEL IELTS CET6 CET4 BEC高级 BEC中级 TEM8 TEM4
					if (!engMap.get(keyType).equals("")) {
						switch (Integer.parseInt(engMap.get(keyType))) {
						case 1:
							engType = "GRE";
							break;
						case 2:
							engType = "GMAT";
							break;
						case 3:
							engType = "TOFEL";
							break;
						case 4:
							engType = "TOFEL";
							break;
						case 5:
							engType = "TOFEL";
							break;
						case 6:
							engType = "IELTS";
							break;
						case 7:
							engType = "TEM8";
							break;
						case 8:
							engType = "TEM4";
							break;
						case 9:
							engType = "BEC高级";
							break;
						case 10:
							engType = "BEC中级";
							break;
						case 11:
							engType = "CET4";
							break;
						case 12:
							engType = "CET6";
							break;
						default:
							engType = "无";
							break;
						}
					}
					

				}
				if (key.startsWith("TZ_7TZ_TZ_7_2")){
					keyType = chage(key);
					value=engMap.get(keyType);
				}
			}
			souseMap.put(engType, value);
			sb.append(",");
			sb.append("英语考试类型:");
			sb.append(engType);
			sb.append(",");
			sb.append("成绩:");
			sb.append(value);
			System.out.println(sb.toString());

			// 分数校验
			Score = 0;
			float tempScore = 0;

			String strTempScore = "";

			// 报名表里面分数
			String strScore = "";
			String SearchSql = "select TZ_CSMB_SCOR from PS_TZ_CSMB_WY_T where TZ_CSMB_DESC=? and  TZ_CSMB_CK3<=? and TZ_CSMB_CK2>=?";
			String SearchSql2 = "select TZ_CSMB_SCOR from PS_TZ_CSMB_WY_T where TZ_CSMB_DESC=?";
			it = souseMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				key = entry.getKey();
				strScore = getScore(entry.getValue());

				if (strScore == null || strScore.equals("")) {
					strScore = "0";
				}
				if (key.equals("GRE") || key.equals("GMAT") || key.equals("TOFEL") || key.equals("IELTS")
						|| key.equals("IELTS")) {
					strTempScore = SqlQuery.queryForObject(SearchSql,
							new Object[] { key, Float.parseFloat(strScore), Float.parseFloat(strScore) }, "String");
				} else {
					strTempScore = SqlQuery.queryForObject(SearchSql2, new Object[] { key }, "String");
				}
				if (strTempScore == null || strTempScore.equals("")) {
					strTempScore = "0";
				}
				tempScore = Float.parseFloat(strTempScore);
				if (tempScore > Score) {
					Score = tempScore;
					MarkRecord = "英语成绩类型：".concat(key).concat("=").concat(strScore) + "|" + strTempScore.concat("分");
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

		} catch (

		Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
