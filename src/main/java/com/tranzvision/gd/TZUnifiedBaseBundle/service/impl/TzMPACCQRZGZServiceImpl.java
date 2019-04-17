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
 * MPACC全日制工作与实习信息计算
 * @author Administrator
 *
 */
@Service("com.tranzvision.gd.TZUnifiedBaseBundle.service.impl.TzMPACCQRZGZServiceImpl")
public class TzMPACCQRZGZServiceImpl extends TzZddfServiceImpl{

	@Autowired
	private SqlQuery SqlQuery;

	@Autowired
	private PsTzCjxTblMapper psTzCjxTblMapper;

	@Override
	public float AutoCalculate(String TZ_APP_ID, String TZ_SCORE_ID, String TZ_SCORE_ITEM) {
		try {
			float score = 0;
			// 报名表信息初始化
			Map<String, String> ksMap = new HashMap<String, String>();
			String ksMapkey = "";

			// 查询报名表信息
			String ks_valuesql = "SELECT TZ_XXX_BH,TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE  TZ_APP_INS_ID=? AND TZ_XXX_BH =? ";
			Map<String, Object> map = SqlQuery.queryForMap(ks_valuesql,
					new Object[] { TZ_APP_ID, "TZ_TZ_25_1"});

			//是否全职
			String isFullTime = "";
			String workYear = "";
			String is500 = "";
			String position_type = "";

			ksMapkey = map.get("TZ_XXX_BH").toString();
			if (ksMapkey.equals("TZ_TZ_25_1")) {
				isFullTime = map.get("TZ_APP_S_TEXT").toString();
			}
			float workScore = 0;
			//全职
			if("1".equals(isFullTime)) {
				isFullTime = "是";
				//工作年限，是否500强，岗位类型
				ks_valuesql = "SELECT TZ_XXX_BH,TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE  TZ_APP_INS_ID=? AND TZ_XXX_BH =? ";
				map = SqlQuery.queryForMap(ks_valuesql,
						new Object[] { TZ_APP_ID, "TZ_TZ_9_2","TZ_TZ_9_7","TZ_work_6"});
				ksMapkey = map.get("TZ_XXX_BH").toString();
				if (ksMapkey.equals("TZ_TZ_9_2")) {
					workYear = map.get("TZ_APP_S_TEXT").toString();
				}
				if (ksMapkey.equals("TZ_TZ_9_7")) {
					is500 = map.get("TZ_APP_S_TEXT").toString();
				}
				if (ksMapkey.equals("TZ_work_6")) {
					position_type = map.get("TZ_APP_S_TEXT").toString();
				}
				score = 3;
				//工作年限：2分/年，总分不超过4分
				workScore = Float.parseFloat(workYear)*2;
				if(workScore > 4) {
					workScore = 4;
				}
				score = score + workScore;
				//是否500强：是=1分
				if("Y".equals(is500)) {
					score = score + 1;
					is500 = "是";
				}else {
					is500 = "否";
				}
				//岗位类型：高层2分，中层1分，基层1分
				if("1".equals(position_type)) {
					score = score + 2;
				}else {
					score = score + 1;
				}
				
			}else {
				isFullTime = "否";
				float workMonthsScore = 0;
				//实习月数 实际经历：3分/月，总分不超过9分
				ks_valuesql = "SELECT TZ_XXX_BH,TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE  TZ_APP_INS_ID=? AND TZ_XXX_BH like ? ";
				List<Map<String, Object>> listMap = SqlQuery.queryForList(ks_valuesql,
						new Object[] { TZ_APP_ID, "%TZ_TZ_10_3%"});
				int months = 0;
				for (Map<String, Object> map2 : listMap) {
					String workMonths = map2.get("TZ_XXX_BH").toString();
					months = months + Integer.parseInt(workMonths);
				}
				workMonthsScore = months*3;
				if(workMonthsScore > 9) {
					workMonthsScore = 9;
				}
				score = workMonthsScore;
				ks_valuesql = "SELECT COUNT(1) FROM PS_TZ_APP_CC_T WHERE  TZ_APP_INS_ID=? AND TZ_XXX_BH = ? AND TZ_APP_S_TEXT = ?";
				int count = SqlQuery.queryForObject(ks_valuesql,
						new Object[] { TZ_APP_ID, "%TZ_10_7%", "Y"},"int");
				if(count > 0) {
					score = score + 1;
					is500 = "是";
				}else {
					is500 = "否";
				}
			}

			String MarkRecord = "工作经历：是否全职工作经历".concat(isFullTime).concat("|工作年限：").concat(String.valueOf(workScore)).concat("|是否500强：").concat(is500);

			MarkRecord = MarkRecord + "|" + score + "分";

			System.out.println("MPACC全日制工作与实习信息计算:" + MarkRecord);

			// 插入表TZ_CJX_TBL
			PsTzCjxTblWithBLOBs psTzCjxTblWithBLOBs = new PsTzCjxTblWithBLOBs();
			// 成绩单ID
			Long tzScoreInsId = Long.parseLong(TZ_SCORE_ID);
			psTzCjxTblWithBLOBs.setTzScoreInsId(tzScoreInsId);
			// 成绩项ID
			psTzCjxTblWithBLOBs.setTzScoreItemId(TZ_SCORE_ITEM);
			// 分值
			BigDecimal BigDeScore = new BigDecimal(score);
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

			return score;

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
