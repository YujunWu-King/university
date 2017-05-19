package com.tranzvision.gd.TZUnifiedBaseBundle.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.log.SysoCounter;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.*;
import com.tranzvision.gd.TZUnifiedBaseBundle.model.*;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZUnifiedBaseBundle.service.impl.TzZddfXLFServiceImpl")
public class TzZddfXLFServiceImpl extends TzZddfServiceImpl {

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

			// 查询报名表信息
			String ks_valuesql = "SELECT TZ_XXX_BH,TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE  TZ_APP_INS_ID=? ";
			List<Map<String, Object>> listMap = SqlQuery.queryForList(ks_valuesql, new Object[] { TZ_APP_ID });
			for (Map<String, Object> map : listMap) {
				ksMapkey = map.get("TZ_XXX_BH").toString();
				ksMapvalue = map.get("TZ_APP_S_TEXT").toString();
				ksMap.put(ksMapkey, ksMapvalue);
			}

			// 根据报名表ID查询考生本科学历的学历、学位、学校ID
			String XL = ksMap.get("TZ_10highdegree"); // 最高学历
			String XW = ksMap.get("TZ_10hxuewei"); // 最高学位

			// 学校1
			String XX = ksMap.get("TZ_11luniversitysch"); // 最后的本科院校
			String XW1 = ksMap.get("TZ_11TZ_TZ_11_4"); // 学位
			String XLFL = ksMap.get("TZ_11fl"); // 学历分类

			// 学校2
			String XX2 = ksMap.get("TZ_12ouniversitysch"); // 学校2
			String XL2 = ksMap.get("TZ_12xl2"); // 学历2
			String XW2 = ksMap.get("TZ_12xw2"); // 学位2
			String XLFL2 = ksMap.get("TZ_12fl2"); // 学历分类2

			// 学校3
			String XX3 = ksMap.get("TZ_13ouniver3sch"); // 学校3
			String XL3 = ksMap.get("TZ_13xueli3"); // 学历3
			String XW3 = ksMap.get("TZ_13xuewei3"); // 学位3
			String XLFL3 = ksMap.get("TZ_13TZ_TZ_13_6"); // 学历分类3

			// 声明float型字段“得分”，string型字段“打分记录”；
			float Score;
			float Score2 = 0;
			float Score3 = 0;
			String MarkRecord;
			String XXLX = "";
			String XXType = "";
			String XXLX2 = "";
			String XXType2 = "";
			String XXLX3 = "";
			String XXType3 = "";

			// 判断学校1是否为国外学校
			String uniScholContry = ksMap.get("TZ_11luniversitycountry");
			if ("中国大陆".equals(uniScholContry) || "中国".equals(uniScholContry)) {
			} else {

				// 如果是外国学校，截取（前的内容
				if (XX.indexOf("(") > 0) {
					XX = XX.substring(0, XX.indexOf("("));
				} else {
					XX = "邓莱里文艺理工学院";
				}
			}

			// 判断学校2是否为国外学校
			String ScholContry2 = ksMap.get("TZ_12ouniversitycountry");
			if (ScholContry2 != null && !ScholContry2.equals("")) {
				if ("中国大陆".equals(ScholContry2) || "中国".equals(ScholContry2)) {
				} else {

					// 如果是外国学校，截取（前的内容
					if (XX2.indexOf("(") > 0) {
						XX2 = XX2.substring(0, XX2.indexOf("("));
					} else {
						XX2 = "邓莱里文艺理工学院";
					}
				}
			}
			// 判断学校3是否为国外学校
			String ScholContry3 = ksMap.get("TZ_13ouniver3country");
			if (ScholContry3 != null && !ScholContry3.equals("")) {
				if ("中国大陆".equals(ScholContry3) || "中国".equals(ScholContry3)) {
				} else {

					// 如果是外国学校，截取（前的内容
					if (XX3.indexOf("(") > 0) {
						XX3 = XX3.substring(0, XX3.indexOf("("));
					} else {
						XX3 = "邓莱里文艺理工学院";
					}
				}
			}

			// 根据考生学校ID查询所属学校类型
			String sql = "SELECT TZ_SCHOOL_TYPE FROM PS_TZ_SCH_LIB_TBL where TZ_SCHOOL_NAME=?";
			XXLX = SqlQuery.queryForObject(sql, new Object[] { XX }, "String");

			String sqlSch2 = "SELECT TZ_SCHOOL_TYPE FROM PS_TZ_SCH_LIB_TBL where TZ_SCHOOL_NAME=?";
			XXLX2 = SqlQuery.queryForObject(sqlSch2, new Object[] { XX2 }, "String");

			String sqlSch3 = "SELECT TZ_SCHOOL_TYPE FROM PS_TZ_SCH_LIB_TBL where TZ_SCHOOL_NAME=?";
			XXLX3 = SqlQuery.queryForObject(sqlSch3, new Object[] { XX3 }, "String");

			if (XXLX != null && !XXLX.equals("")) {
				switch (Integer.parseInt(XXLX)) {
				case 1:
					// 清华大学;
					XXLX = "1";
					XXType = "清华大学";
					break;
				case 2:
					// 北京大学;
					XXLX = "1";
					XXType = "北京大学";
					break;
				case 3:
					// 985;
					XXLX = "3";
					XXType = "985";
					break;
				case 4:
					// 211;
					XXLX = "4";
					XXType = "211";
					break;
				case 5:
					// 成人;
					XXLX = "6";
					XXType = "其他";
					break;
				case 6:
					// *985;
					XXLX = "2";
					XXType = "*985";
					break;
				case 7:
					// 海外1;
					XXLX = "7";
					XXType = "海外1";
					break;
				case 8:
					// 海外2;
					XXLX = "8";
					XXType = "海外2";
					break;
				case 9:
					// 普通本科;
					XXLX = "5";
					XXType = "普通本科";
					break;

				default:
					// 其他;
					XXLX = "6";
					XXType = "其他";
					break;
				}
			} else {
				XXLX = "6";
				XXType = "其他";
			}

			if (XXLX2 != null && !XXLX2.equals("")) {
				switch (Integer.parseInt(XXLX2)) {
				case 1:
					// 清华大学;
					XXLX2 = "1";
					XXType2 = "清华大学";
					break;
				case 2:
					// 北京大学;
					XXLX2 = "1";
					XXType2 = "北京大学";
					break;
				case 3:
					// 985;
					XXLX2 = "3";
					XXType2 = "985";
					break;
				case 4:
					// 211;
					XXLX2 = "4";
					XXType2 = "211";
					break;
				case 5:
					// 成人;
					XXLX2 = "6";
					XXType2 = "其他";
					break;
				case 6:
					// *985;
					XXLX2 = "2";
					XXType2 = "*985";
					break;
				case 7:
					// 海外1;
					XXLX2 = "7";
					XXType2 = "海外1";
					break;
				case 8:
					// 海外2;
					XXLX2 = "8";
					XXType2 = "海外2";
					break;
				case 9:
					// 普通本科;
					XXLX2 = "5";
					XXType2 = "普通本科";
					break;

				default:
					// 其他;
					XXLX2 = "6";
					XXType2 = "其他";
					break;
				}
			} else {
				XXLX2 = "6";
				XXType2 = "其他";
			}
			if (XXLX3 != null && !XXLX3.equals("")) {
				switch (Integer.parseInt(XXLX3)) {
				case 1:
					// 清华大学;
					XXLX3 = "1";
					XXType3 = "清华大学";
					break;
				case 2:
					// 北京大学;
					XXLX3 = "1";
					XXType3 = "北京大学";
					break;
				case 3:
					// 985;
					XXLX3 = "3";
					XXType3 = "985";
					break;
				case 4:
					// 211;
					XXLX3 = "4";
					XXType3 = "211";
					break;
				case 5:
					// 成人;
					XXLX3 = "6";
					XXType3 = "其他";
					break;
				case 6:
					// *985;
					XXLX3 = "2";
					XXType3 = "*985";
					break;
				case 7:
					// 海外1;
					XXLX3 = "7";
					XXType3 = "海外1";
					break;
				case 8:
					// 海外2;
					XXLX3 = "8";
					XXType3 = "海外2";
					break;
				case 9:
					// 普通本科;
					XXLX3 = "5";
					XXType3 = "普通本科";
					break;

				default:
					// 其他;
					XXLX3 = "6";
					XXType3 = "其他";
					break;
				}
			} else {
				XXLX3 = "6";
				XXType3 = "其他学校";
			}



			// 学历
			// 数据库：1是本科，2自考本科 3成教本科 4专升本
			// 传入参数：1是本科 2自考本科 3成教本科 4专升本

			String XLF = "0";
			String XLF2 = "0";
			String XLF3 = "0";

			switch (XLFL) {
			case "1":
				XLF = "1";
				break;
			case "2":
				XLF = "2";
				break;
			case "3":
				XLF = "3";
				break;
			case "4":
				XLF = "4";
				break;
			}

			if (XLFL2 != null && !XLFL2.equals("")) {
				switch (XLFL2) {
				case "1":
					XLF2 = "1";
					break;
				case "2":
					XLF2 = "2";
					break;
				case "3":
					XLF2 = "3";
					break;
				case "4":
					XLF2 = "4";
					break;
				}
			}
			if (XLFL3 != null && !XLFL3.equals("")) {

				switch (XLFL3) {
				case "1":
					XLF3 = "1";
					break;
				case "2":
					XLF3 = "2";
					break;
				case "3":
					XLF3 = "3";
					break;
				case "4":
					XLF3 = "4";
					break;
				}
			}
			// 学位
			// 数据库：1是学士，2是无
			// 传入参数：1学士， 2未获得，3无
			String XWF = "2";
			String XWF2 = "2";
			String XWF3 = "2";

			if ("1".equals(XW1)) {
				XWF = "1";
			} else {
				XWF = "2";
			}
			if ("1".equals(XW2)) {
				XWF2 = "1";
			} else {
				XWF2 = "2";
			}
			if ("1".equals(XW3)) {
				XWF3 = "1";
			} else {
				XWF3 = "2";
			}

			// 根据考生查询到的学历、学位、学校类型在TZ_CSMB_XLF_T查询对应的得分，如果没有查询到对应得分，得分=0
			String ExistSql = "select 'Y' from PS_TZ_CSMB_XLF_T where TZ_CSMB_CK3=? and  TZ_CSMB_CK2=? and TZ_CSMB_CK1=?";
			String isExist = "";
			isExist = SqlQuery.queryForObject(ExistSql, new Object[] { XXLX, XWF, XLF }, "String");

			if ("Y".equals(isExist)) {
				String SearchSql = "select TZ_CSMB_SCOR from PS_TZ_CSMB_XLF_T where TZ_CSMB_CK3=? and  TZ_CSMB_CK2=? and TZ_CSMB_CK1=?";
				String StrScore = SqlQuery.queryForObject(SearchSql, new Object[] { XXLX, XWF, XLF }, "String");
				float Score1 = Float.parseFloat(StrScore);


				if (XXLX2 != null && !XXLX2.equals("") && XL2.equals("1") && XLF2 != "0" && XWF2 != null
						&& !XWF2.equals("")) {

					String SearchSql2 = "select TZ_CSMB_SCOR from PS_TZ_CSMB_XLF_T where TZ_CSMB_CK3=? and  TZ_CSMB_CK2=? and TZ_CSMB_CK1=?";
					String StrScore2 = SqlQuery.queryForObject(SearchSql2, new Object[] { XXLX2, XWF2, XLF2 },
							"String");
					Score2 = Float.parseFloat(StrScore2);

				}

				if (XXLX3 != null && !XXLX3.equals("") && XL3.equals("1") && XLF3 != "0" && XWF3 != null
						&& !XWF3.equals("")) {

					String SearchSql3 = "select TZ_CSMB_SCOR from PS_TZ_CSMB_XLF_T where TZ_CSMB_CK3=? and  TZ_CSMB_CK2=? and TZ_CSMB_CK1=?";
					String StrScore3 = SqlQuery.queryForObject(SearchSql3, new Object[] { XXLX3, XWF3, XLF3 },
							"String");
					Score3 = Float.parseFloat(StrScore3);

				}

				if (Score1 > Score2) {
					Score = Score1;
				} else {
					Score = Score2;
				}

				if (Score3 > Score) {
					Score = Score3;
				}

				// PS: 学历=研究生TZ_10highdegree（1、3），学位TZ_10hxuewei=硕士/博士（1，2）

				// 查询考生其他教育经历中，是否有学历=研究生，学位等于硕士/博士的教育经历，如果有，得分=得分+5

				// 学历
				// 数据库：2是本科，1为研究生
				// 传入参数：1博士 2本科 3硕士

				// 学位
				// 数据库：1是学士，2是无
				// 传入参数：1博士 2硕士 3学士 4无

				if (XW.equals("2") || XW.equals("1")) {
					if (XL.equals("3") || XL.equals("1")) {
						Score += 5;
					}
				}

				// 得分如果>100，得分=100；
				if (Score > 100) {
					Score = 100;
				}

				String XLMS = null;
				String XWMS = null;

				switch (XLFL) {
				case "1":
					XLMS = "普通本科";
					break;
				case "2":
					XLMS = "自考本科";
					break;
				case "3":
					XLMS = "成教本科";
					break;
				case "4":
					XLMS = "专升本";
					break;
				}
				if (XLFL2 != null && !XLFL2.equals("")) {
					switch (XLFL2) {
					case "1":
						XLMS = "普通本科";
						break;
					case "2":
						XLMS = "自考本科";
						break;
					case "3":
						XLMS = "成教本科";
						break;
					case "4":
						XLMS = "专升本";
						break;
					}
				}
				if (XLFL3 != null && !XLFL3.equals("")) {
					switch (XLFL3) {
					case "1":
						XLMS = "普通本科";
						break;
					case "2":
						XLMS = "自考本科";
						break;
					case "3":
						XLMS = "成教本科";
						break;
					case "4":
						XLMS = "专升本";
						break;
					}
				}
				switch (XW) {
				case "1":
					XWMS = "博士";
					break;
				case "2":
					XWMS = "硕士";
					break;
				case "3":
					XWMS = "学士";
					break;
				default:
					XWMS = "无";
				}

				// 记录打分记录：示例：985|学历：本科|学位：学士|研究生|95分
				if (Score1 > Score2) {
					MarkRecord = "本科学校类型：".concat(XXType).concat("|学历：").concat(XLMS).concat("|学位：").concat(XWMS);

				} else {
					MarkRecord = "本科学校类型：".concat(XXType2).concat("|学历：").concat(XLMS).concat("|学位：").concat(XWMS);

				}

				if (Score3 == Score) {

					MarkRecord = "本科学校类型：".concat(XXType3).concat("|学历：").concat(XLMS).concat("|学位：").concat(XWMS);

				}

				// 是否拼接研究生；
				if (XW.equals("2") || XW.equals("1")) {
					if (XL.equals("3") || XL.equals("1")) {
						MarkRecord.concat("|研究生");
					}
				}

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
			} else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}
