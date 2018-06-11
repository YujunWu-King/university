package com.tranzvision.gd.TZUnifiedBaseBundle.service.impl;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
/**
 * MPACC专业资格打分
 */
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCjxTblMapper;
import com.tranzvision.gd.TZUnifiedBaseBundle.model.PsTzCjxTblKey;
import com.tranzvision.gd.TZUnifiedBaseBundle.model.PsTzCjxTblWithBLOBs;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZUnifiedBaseBundle.service.impl.TzZddMPACCZYZGServiceImpl")
public class TzZddMPACCZYZGServiceImpl extends TzZddfServiceImpl{
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
			String TZ_XXX_BH = "'TZ_6TZ_TZ_6_1','TZ_6TZ_TZ_6_2'";
			// 查询报名表信息
			String ks_valuesql = "SELECT TZ_XXX_BH,TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE  TZ_APP_INS_ID=? AND TZ_XXX_BH in ("
					+ TZ_XXX_BH + ")";
			List<Map<String, Object>> listMap = SqlQuery.queryForList(ks_valuesql,
					new Object[] { TZ_APP_ID});

			for (Map<String, Object> map : listMap) {
				ksMapkey = map.get("TZ_XXX_BH").toString();
				ksMapvalue = map.get("TZ_APP_S_TEXT").toString();
				ksMap.put(ksMapkey, ksMapvalue);
			}
			String value = "";
			String desc = "";
			String MarkRecord = "";
			String strScore = "";
	//		Iterator<Entry<String, String>> it = ksMap.entrySet().iterator();

			
			if("".equals(ksMap.get("TZ_6TZ_TZ_6_1")) && "".equals(ksMap.get("TZ_6TZ_TZ_6_2"))){
				value="0";
				desc="没有证书";
			}
			else if(("注册会计师".equals(ksMap.get("TZ_6TZ_TZ_6_1")) || "注册税务师".equals(ksMap.get("TZ_6TZ_TZ_6_1")) ||
					"注册评估师".equals(ksMap.get("TZ_6TZ_TZ_6_1"))) && "中国".equals(ksMap.get("TZ_6TZ_TZ_6_2"))){
				value="1";
				desc="中国注册证书";
			}
			else if((ksMap.get("TZ_6TZ_TZ_6_1").contains("注册会计") || ksMap.get("TZ_6TZ_TZ_6_1").contains("财务"))){
				value="2";
				desc="其他国家注册证书";
			}
			else{
				value="3";
				desc="其他资格证书";
			}
			String SearchSql2 = "select TZ_CSMB_SCOR from PS_TZ_CSMB_ZY_T where TZ_CSMB_CK1=? and TZ_CSMB_CK2=? ";
			strScore = SqlQuery.queryForObject(SearchSql2, new Object[] { "E", value }, "String");
			Score=Float.parseFloat(strScore);
			MarkRecord = MarkRecord + "专业资格：".concat(desc) + "|";
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
