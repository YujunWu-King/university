package com.tranzvision.gd.TZUnifiedBaseBundle.service.impl;

/**
 * 华东理工MPACC全日制学历计算
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

@Service("com.tranzvision.gd.TZUnifiedBaseBundle.service.impl.TzMPACCQRZXLServiceImpl")
public class TzMPACCQRZXLServiceImpl  extends TzZddfServiceImpl{

	@Autowired
	private PsTzCjxTblMapper psTzCjxTblMapper;
	@Autowired
	private SqlQuery SqlQuery;

	// 获取参数：成绩单ID、学历分成绩项ID、报名表ID
	@Override
	public float AutoCalculate(String TZ_APP_ID, String TZ_SCORE_ID, String TZ_SCORE_ITEM) {
		try {

			float score = 0;
			// 报名表信息初始化
			Map<String, String> ksMap = new HashMap<String, String>();
			String ksMapkey = "";
			String ksMapvalue = "";
			//学历，学位，院校名称，分类
			String TZ_XXX_BH = "'TZ_zgjy_3','TZ_zgjy_4','TZ_1sch','TZ_zgjy_11'";

			// 查询报名表信息
			String ks_valuesql = "SELECT TZ_XXX_BH,TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE  TZ_APP_INS_ID=? AND TZ_XXX_BH in ("
					+ TZ_XXX_BH + ")";
			List<Map<String, Object>> listMap = SqlQuery.queryForList(ks_valuesql, new Object[] { TZ_APP_ID });
			for (Map<String, Object> map : listMap) {
				ksMapkey = map.get("TZ_XXX_BH").toString();
				ksMapvalue = map.get("TZ_APP_S_TEXT") == null ? "" : map.get("TZ_APP_S_TEXT").toString();
				ksMap.put(ksMapkey, ksMapvalue);
			}

			String xl = ksMap.get("TZ_zgjy_3"); // 最高学历
			if (xl == null) {
				xl = "";
			}

			String xw = ksMap.get("TZ_zgjy_4"); // 最高学位 1 博士 2 硕士 3 学士 4 无
			if (xw == null) {
				xw = "";
			}

			String school = ksMap.get("TZ_1sch"); // 院校
			if (school == null) {
				school = "";
			}

			String other = ksMap.get("TZ_zgjy_11"); // 本科类型 1 普通本科 2 自考本科 3
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
			sb.append(xw);
			sb.append(",");
			sb.append("最高学历:");
			sb.append(xl);
			sb.append(",");
			sb.append("本科学历:");
			sb.append(other);

			System.out.println(sb.toString());
			
			// 记录打分记录：示例：985|学历：本科|学位：学士|研究生|95分
			String MarkRecord = "";
			String type = "";
			String TZ_CSMB_CK2 = "";
			String TZ_CSMB_CK3 = "";
			String TZ_CSMB_CK1 = "";
			
			//学历：博士/硕士：1
			if("1".equals(xl) || "2".equals(xl)) {
				TZ_CSMB_CK2 = "1";
				
			}else if("3".equals(xl)) { //本科：2
				TZ_CSMB_CK2 = "2";
			}else {//专科
				//专升本、成教、网络本科：3
				if("3".equals(other) || "4".equals(other) || "5".equals(other)) {
					TZ_CSMB_CK2 = "3";
				}else {//专科：4
					TZ_CSMB_CK2 = "4";
				}
			}
			
			//学位 博士/硕士：1
			if("1".equals(xw) || "2".equals(xw)) {//硕士学位：25分
				TZ_CSMB_CK1 = "1";
			}else if("3".equals(xw)){ //学士：2
				TZ_CSMB_CK1 = "2";
			}else{//无  ：3
				TZ_CSMB_CK1 = "3";
			}
			
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
			//毕业院校985/211 ：1
			if("1".equals(TZ_SCHOOL_TYPE) || "2".equals(TZ_SCHOOL_TYPE)) {
				TZ_CSMB_CK3 = "1";
			}else { //其他：2
				TZ_CSMB_CK3 = "2";
			}
			
			// 只要有硕士以上学位的，都给最高分25分。
			if ("1".equals(xw) || "2".equals(xw)) {
				TZ_CSMB_CK2 = "*";
				TZ_CSMB_CK3 = "*";
			}
			// 只要有硕士以上学历的，都给最高分25分。
			if ("1".equals(xw) || "2".equals(xw)) {
				TZ_CSMB_CK1 = "*";
				TZ_CSMB_CK3 = "*";
			}
			// 获取分数
			String SearchSql = "select TZ_CSMB_DESC,TZ_CSMB_SCOR from PS_TZ_CSMB_XLF_T where TZ_CSMB_CK3=? AND TZ_CSMB_CK2= ? AND TZ_CSMB_CK1=? AND TZ_CSMB_DESC LIKE 'MPACC&%'";
			Map<String, Object> scoreMap= SqlQuery.queryForMap(SearchSql, new Object[] { TZ_CSMB_CK3, TZ_CSMB_CK2, TZ_CSMB_CK1});
			String StrScore = "0";
			String TZ_CSMB_DESC = "";
			if (scoreMap == null) {
				StrScore = "0";
			}
			StrScore = scoreMap.get("TZ_CSMB_SCOR").toString();
			TZ_CSMB_DESC = scoreMap.get("TZ_CSMB_DESC").toString();
			score = Float.parseFloat(StrScore);
			MarkRecord = "毕业院校：".concat(TZ_CSMB_DESC.substring(6));
			
			MarkRecord = MarkRecord + "|" + score +"分" ;

			System.out.println(MarkRecord);

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
