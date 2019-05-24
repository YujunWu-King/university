package com.tranzvision.gd.TZUnifiedBaseBundle.service.impl;

/**
 * 华东理工MPACC 非全日制教育背景计算
 */
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

@Service("com.tranzvision.gd.TZUnifiedBaseBundle.service.impl.TzZddMPACCJYBJServiceImpl")
public class TzZddMPACCJYBJServiceImpl  extends TzZddfServiceImpl{

	@Autowired
	private PsTzCjxTblMapper psTzCjxTblMapper;
	@Autowired
	private SqlQuery SqlQuery;

	// 获取参数：成绩单ID、学历分成绩项ID、报名表ID
	@Override
	public float AutoCalculate(String TZ_APP_ID, String TZ_SCORE_ID, String TZ_SCORE_ITEM) {
		try {

			// 报名表信息初始化
			Map<String, String> ksMap = new HashMap<String, String>();
			String ksMapkey = "";
			String ksMapvalue = "";

			String TZ_XXX_BH = "'zgjyTZ_1sch','zgjyTZ_zgjy_4','zgjyTZ_zgjy_3','zgjyTZ_zgjy_11'";

			// 查询报名表信息
			String ks_valuesql = "SELECT TZ_XXX_BH,TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE  TZ_APP_INS_ID=? AND TZ_XXX_BH in ("
					+ TZ_XXX_BH + ")";
			List<Map<String, Object>> listMap = SqlQuery.queryForList(ks_valuesql, new Object[] { TZ_APP_ID });
			for (Map<String, Object> map : listMap) {
				ksMapkey = map.get("TZ_XXX_BH").toString();
				ksMapvalue = map.get("TZ_APP_S_TEXT") == null ? "" : map.get("TZ_APP_S_TEXT").toString();
				ksMap.put(ksMapkey, ksMapvalue);
			}

			String school = ksMap.get("zgjyTZ_1sch"); // 最高院校
			if (school == null) {
				school = "";
			}

			String sw = ksMap.get("zgjyTZ_zgjy_4"); // 最高学位 1 博士 2 硕士 3 学士 4 无
			if (sw == null) {
				sw = "";
			}

			String sl = ksMap.get("zgjyTZ_zgjy_3"); // 最高学历 1 博士研究生 2 硕士研究生 3 本科
													// 4专科
			if (sl == null) {
				sl = "";
			}

			String other = ksMap.get("zgjyTZ_zgjy_11"); // 本科类型 1 普通本科 2 自考本科 3
														// 网络本科 4成教本科 5专升本
			if (other == null) {
				other = "";
			}

			StringBuffer sb = new StringBuffer();
			sb.append("报名表数据----报名表ID:");
			sb.append(TZ_APP_ID);
			sb.append(",");
			sb.append("最高院校:");
			sb.append(school);
			sb.append(",");
			sb.append("最高学位:");
			sb.append(sw);
			sb.append(",");
			sb.append("最高学历:");
			sb.append(sl);
			sb.append(",");
			sb.append("本科学历:");
			sb.append(other);

			System.out.println(sb.toString());

			String sql = "SELECT TZ_SCHOOL_TYPE FROM PS_TZ_SCH_LIB_TBL where TZ_SCHOOL_NAME=?";

			String TZ_SCHOOL_TYPE = SqlQuery.queryForObject(sql, new Object[] { school }, "String");
			if (TZ_SCHOOL_TYPE == null) {
				TZ_SCHOOL_TYPE = "";
			}

			// 华东理工/985:1 211:2 普通:3 二本:4 海外院校一类:5 海外院校二类:6
			String XXType2 = "";
			if (TZ_SCHOOL_TYPE != null && !TZ_SCHOOL_TYPE.equals("")) {
				switch (Integer.parseInt(TZ_SCHOOL_TYPE)) {
				case 1:
					// 清华大学;
					TZ_SCHOOL_TYPE = "1";
					XXType2 = "985";
					break;
				case 2:
					// 北京大学;
					TZ_SCHOOL_TYPE = "1";
					XXType2 = "985";
					break;
				case 3:
					// 985;
					TZ_SCHOOL_TYPE = "1";
					XXType2 = "985";
					break;
				case 4:
					// 211;
					TZ_SCHOOL_TYPE = "2";
					XXType2 = "211";
					break;
				case 5:
					// 成人;
					TZ_SCHOOL_TYPE = "4";
					XXType2 = "二级学院";
					break;
				case 6:
					// *985;
					TZ_SCHOOL_TYPE = "1";
					XXType2 = "985";
					break;
				case 7:
					// 海外1;
					TZ_SCHOOL_TYPE = "5";
					XXType2 = "海外院校一类";
					break;
				case 8:
					// 海外2;
					TZ_SCHOOL_TYPE = "6";
					XXType2 = "海外院校二类";
					break;
				case 9:
					// 普通本科;
					TZ_SCHOOL_TYPE = "3";
					XXType2 = "普通本科院校";
					break;
				default:
					// 其他; 都算二本
					TZ_SCHOOL_TYPE = "4";
					XXType2 = "普通本科院校";
					break;
				}
			} else {
				// 没有学校 默认二本
				TZ_SCHOOL_TYPE = "4";
				XXType2 = "普通本科院校";
			}

			if (school.equals("华东理工大学")) {
				TZ_SCHOOL_TYPE = "1";
				XXType2 = "华东理工大学";
			}

			// 最高学位 1 博士 2 硕士 3 学士 4 无
			// 博士/硕士 1 本科2 无3
			String XLMS = null;
			String XWMS = null;
			if (sw != null && !sw.equals("")) {
				switch (Integer.parseInt(sw)) {
				case 1:
					sw = "1";
					XWMS = "博士";
					break;
				case 2:
					sw = "1";
					XWMS = "硕士";
					break;
				case 3:
					sw = "2";
					XWMS = "学士";
					break;
				case 4:
					sw = "3";
					XWMS = "无";
					break;
				default:
					sw = "3";
					XWMS = "无";
					break;
				}
			} else {
				sw = "3";
				XWMS = "无";
			}

			// 最高学历 1 博士研究生 2 硕士研究生 3 本科 4专科
			// 博士研究生1/硕士研究生1 本科2 自考本科3 成教本科/网络本科4 专升本5 专科6
			if (sl != null && !sl.equals("")) {
				switch (Integer.parseInt(sl)) {
				case 1:
					sl = "1";
					XLMS = "博士研究生";
					break;
				case 2:
					sl = "1";
					XLMS = "硕士研究生";
					break;
				case 3:
					sl = "2";
					XLMS = "本科";
					break;
				case 4:
					sl = "6";
					XLMS = "专科";
					break;
				default:
					sl = "6";
					XLMS = "专科";
					break;
				}
			} else {
				sl = "6";
				XLMS = "专科";
			}

			if (sl.equals("2") && !other.equals("")) {

				// 博士研究生1/硕士研究生1 本科2 自考本科3 成教本科/网络本科4 专升本5 专科6
				// 本科类型 1 普通本科 2 自考本科 3 网络本科 4成教本科 5专升本
				switch (Integer.parseInt(other)) {
				case 1:
					sl = "2";
					XLMS = "普通本科";
					break;
				case 2:
					sl = "3";
					XLMS = "自考本科";
					break;
				case 3:
					sl = "4";
					XLMS = "网络本科";
					break;
				case 4:
					sl = "4";
					XLMS = "成教本科";
					break;
				case 5:
					sl = "5";
					XLMS = "专升本";
					break;
				default:
					sl = "6";
					XLMS = "专科";
					break;
				}
			}

			// 只要有硕士学位的，都给最高分8分。
			if (sw.equals("1")) {
				sl = "*";
				TZ_SCHOOL_TYPE = "*";
			}

			sb = new StringBuffer();
			sb.append("查询数据----报名表ID:");
			sb.append(TZ_APP_ID);
			sb.append("最高院校:");
			sb.append(TZ_SCHOOL_TYPE);
			sb.append(",");
			sb.append(",");
			sb.append("最高学位:");
			sb.append(sw);
			sb.append(",");
			sb.append("最高学历:");
			sb.append(sl);
			System.out.println(sb.toString());

			// 获取分数
			String SearchSql = "select TZ_CSMB_SCOR from PS_TZ_CSMB_XLF_T where TZ_CSMB_CK3=? and  TZ_CSMB_CK2=? and TZ_CSMB_CK1=? and TZ_CSMB_DESC not like 'MPACC%'";
			String StrScore = SqlQuery.queryForObject(SearchSql, new Object[] { TZ_SCHOOL_TYPE, sl, sw }, "String");
			if (StrScore == null || StrScore.equals("")) {
				StrScore = "0";
			}

			float Score = Float.parseFloat(StrScore);

			String MarkRecord = "";
			// 记录打分记录：示例：985|学历：本科|学位：学士|研究生|95分

			MarkRecord = "本科学校类型：".concat(XXType2).concat("|学历：").concat(XLMS).concat("|学位：").concat(XWMS);

			MarkRecord = MarkRecord + "|" + String.valueOf(StrScore).concat("分");

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
