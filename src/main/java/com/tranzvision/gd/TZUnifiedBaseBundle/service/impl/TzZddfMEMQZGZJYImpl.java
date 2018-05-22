package com.tranzvision.gd.TZUnifiedBaseBundle.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCjxTblMapper;
import com.tranzvision.gd.TZUnifiedBaseBundle.model.PsTzCjxTblKey;
import com.tranzvision.gd.TZUnifiedBaseBundle.model.PsTzCjxTblWithBLOBs;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * MEM全职工作经验打分
 * 
 * @author caoy
 *
 */
@Service("com.tranzvision.gd.TZUnifiedBaseBundle.service.impl.TzZddfMEMQZGZJYImpl")
public class TzZddfMEMQZGZJYImpl extends TzZddfServiceImpl {

	@Autowired
	private PsTzCjxTblMapper psTzCjxTblMapper;

	@Autowired
	private SqlQuery SqlQuery;

	// 获取参数：成绩单ID、国际化背景成绩项ID、报名表ID 
	@Override
	public float AutoCalculate(String TZ_APP_ID, String TZ_SCORE_ID, String TZ_SCORE_ITEM) {
		try {
			float Score = 0;

			String ks_valuesql = "SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE  TZ_APP_INS_ID=?  AND TZ_XXX_BH = ?";
			String year = SqlQuery.queryForObject(ks_valuesql, new Object[] { TZ_APP_ID, "TZ_20TZ_TZ_20_2" }, "String");

			StringBuffer sb = new StringBuffer();
			sb.append("报名表数据----报名表ID:");
			sb.append(TZ_APP_ID);
			sb.append(",");
			sb.append("全职工作经验:");
			sb.append(year);

			System.out.println(sb.toString());
			if (year == null || year.equals("")) {
				year = "0";
			}

			String sql = "select TZ_CSMB_SCOR from PS_TZ_CSMB_ZY_T where TZ_CSMB_CK1=? and TZ_CSMB_CK2<=? and TZ_CSMB_CK3>=?";

			String strScore = SqlQuery.queryForObject(sql,
					new Object[] { "D", Float.parseFloat(year), Float.parseFloat(year) }, "String");

			if (strScore == null || strScore.equals("")) {
				strScore = "0";
			}

			Score = Float.parseFloat(strScore);
			if (Score == -1) {
				if (year == null || year.equals("")) {
					Score = 0;
				} else {
					Score = Float.parseFloat(year);
				}
			}
			String MarkRecord = "全职工作经验：".concat(year) + "年|" + String.valueOf(Score).concat("分");
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
