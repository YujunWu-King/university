package com.tranzvision.gd.TZUnifiedBaseBundle.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCjxTblMapper;
import com.tranzvision.gd.TZUnifiedBaseBundle.model.PsTzCjxTblKey;
import com.tranzvision.gd.TZUnifiedBaseBundle.model.PsTzCjxTblWithBLOBs;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * MPACC全日制荣誉与奖励计算
 * @author Administrator
 *
 */
@Service("com.tranzvision.gd.TZUnifiedBaseBundle.service.impl.TzMPACCQRZRYServiceImpl")
public class TzMPACCQRZRYServiceImpl extends TzZddfServiceImpl{

	@Autowired
	private SqlQuery SqlQuery;

	@Autowired
	private PsTzCjxTblMapper psTzCjxTblMapper;

	@Override
	public float AutoCalculate(String TZ_APP_ID, String TZ_SCORE_ID, String TZ_SCORE_ITEM) {
		try {
			float Score = 0;
			String ksMapkey = "";
			String ksMapvalue = "";
			String TZ_XXX_BH = "TZ_11_1";
			// 查询报名表信息
			String ks_valuesql = "SELECT TZ_XXX_BH,TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE  TZ_APP_INS_ID=? AND TZ_XXX_BH LIKE '%"
					+ TZ_XXX_BH + "%'";
			List<Map<String, Object>> listMap = SqlQuery.queryForList(ks_valuesql,
					new Object[] { TZ_APP_ID});

			String MarkRecord = "荣誉与奖励：|";
			
			int count = 0;
			if(listMap != null && listMap.size() >= 0) {
				for (Map<String, Object> map : listMap) {
					ksMapkey = map.get("TZ_XXX_BH").toString();
					ksMapvalue = map.get("TZ_APP_S_TEXT").toString();
					
					if(!StringUtils.isBlank(ksMapvalue)) {
						MarkRecord = MarkRecord.concat(ksMapvalue) + "|";
						count ++;
					}
				}
				//有一项荣誉算5分，总分不超过10分
				Score = count * 5;
				if(Score >= 10) {
					Score = 10;
				}
			}
			MarkRecord = MarkRecord + Score + "分";
			System.out.println("MPACC全日制荣誉与奖励计算MarkRecord:" + MarkRecord);	

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
