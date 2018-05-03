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

/**
 * 专业资格打分
 * 
 * @author caoy
 *
 */
@Service("com.tranzvision.gd.TZUnifiedBaseBundle.service.impl.TzZddfZYZGServiceImpl")
public class TzZddfZYZGServiceImpl extends TzZddfServiceImpl {

	@Autowired
	private PsTzCjxTblMapper psTzCjxTblMapper;

	@Autowired
	private SqlQuery SqlQuery;

	// 获取参数：成绩单ID、国际化背景成绩项ID、报名表ID
	@Override
	public float AutoCalculate(String TZ_APP_ID, String TZ_SCORE_ID, String TZ_SCORE_ITEM) {
		try {
			float Score = 0;
			float tempScore = 0;
			Map<String, String> ksMap = new HashMap<String, String>();
			String ksMapkey = "";
			String ksMapvalue = "";

			// 查询报名表信息
			String ks_valuesql = "SELECT TZ_XXX_BH,TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE  TZ_APP_INS_ID=?  AND TZ_XXX_BH like ?";
			List<Map<String, Object>> listMap = SqlQuery.queryForList(ks_valuesql,
					new Object[] { TZ_APP_ID, "TZ_6TZ_TZ_6_4%" });

			for (Map<String, Object> map : listMap) {
				ksMapkey = map.get("TZ_XXX_BH").toString();
				ksMapvalue = map.get("TZ_APP_S_TEXT").toString();
				ksMap.put(ksMapkey, ksMapvalue);
			}
			String value = "";
			String desc = "";
			String MarkRecord = "";
			String strScore = "";
			Iterator<Entry<String, String>> it = ksMap.entrySet().iterator();

			String SearchSql2 = "select TZ_CSMB_SCOR from PS_TZ_CSMB_ZY_T where TZ_CSMB_CK1=? and TZ_CSMB_CK2=? ";
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				value = entry.getValue();
				// 1国家级 2省部级 3地市级 4专业高级证书
				switch (Integer.parseInt(value)) {
				case 1:
					desc = "国家级";
					break;
				case 2:
					desc = "省部级";
					break;
				case 3:
					desc = "地市级";
					break;
				case 4:
					desc = "专业高级证书";
					break;
				}
				strScore = SqlQuery.queryForObject(SearchSql2, new Object[] { "C", value }, "String");
				if (strScore == null || strScore.equals("")) {
					strScore = "0";
				}
				tempScore = Float.parseFloat(strScore);
				if (Score + tempScore <= 3) {
					Score = Score + tempScore;
					MarkRecord = MarkRecord + "专业资格：".concat(desc) + "|";
				} else {
					break;
				}
			}
			MarkRecord = MarkRecord + String.valueOf(Score).concat("分");

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
