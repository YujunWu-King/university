package com.tranzvision.gd.TZUnifiedBaseBundle.service.impl;

/**
 * MPACC全职工作经验计算
 */
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCjxTblMapper;
import com.tranzvision.gd.TZUnifiedBaseBundle.model.PsTzCjxTblKey;
import com.tranzvision.gd.TZUnifiedBaseBundle.model.PsTzCjxTblWithBLOBs;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZUnifiedBaseBundle.service.impl.TzZddMPACCQZGZJYImpl")
public class TzZddMPACCQZGZJYServiceImpl extends TzZddfServiceImpl{
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
			String year = SqlQuery.queryForObject(ks_valuesql, new Object[] { TZ_APP_ID, "workTZ_TZ_9_2" }, "String");

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
			if(Float.parseFloat(year)<=0){
				year="0";
				Score=0;
			}else if(Float.parseFloat(year)>0 && Float.parseFloat(year)<4){
				Score=Float.parseFloat(year)*2;
			}else{
				Score=8;
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
