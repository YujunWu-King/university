package com.tranzvision.gd.TZUnifiedBaseBundle.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

//import com.tranzvision.gd.TZUnifiedBaseBundle.dao.PsTzCjxTblMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.*;
import com.tranzvision.gd.TZUnifiedBaseBundle.model.*;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 职业背景（职务打分）打分
 * 
 * @author caoy
 *
 */
@Service("com.tranzvision.gd.TZUnifiedBaseBundle.service.impl.TzZddfZYBJServiceImpl")
public class TzZddfZYBJServiceImpl extends TzZddfServiceImpl {

	@Autowired
	private SqlQuery SqlQuery;

	@Autowired
	private PsTzCjxTblMapper psTzCjxTblMapper;

	// 获取参数：成绩单ID、职业背景成绩项ID、报名表ID
	@Override
	public float AutoCalculate(String TZ_APP_ID, String TZ_SCORE_ID, String TZ_SCORE_ITEM) {
		try {

			// 报名表信息初始化
			Map<String, String> ksMap = new HashMap<String, String>();
			String ksMapkey = "";

			// 查询报名表信息
			String ks_valuesql = "SELECT TZ_XXX_BH,TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE  TZ_APP_INS_ID=? AND (TZ_XXX_BH =? or TZ_XXX_BH=?)";
			List<Map<String, Object>> listMap = SqlQuery.queryForList(ks_valuesql,
					new Object[] { TZ_APP_ID, "workTZ_work_185firm_type", "workTZ_work_185position_type" });

			String firm_type = "";
			String firm_desc = "";
			String position_type = "";
			String position_desc = "";

			for (Map<String, Object> map : listMap) {
				ksMapkey = map.get("TZ_XXX_BH").toString();
				if (ksMapkey.equals("workTZ_work_185firm_type")) {
					firm_type = map.get("TZ_APP_S_TEXT").toString();
				}
				if (ksMapkey.equals("workTZ_work_185position_type")) {
					position_type = map.get("TZ_APP_S_TEXT").toString();
				}
			}

			StringBuffer sb = new StringBuffer();
			sb.append("报名表数据----报名表ID:");
			sb.append(TZ_APP_ID);
			sb.append(",");
			sb.append("公司性质:");
			sb.append(firm_type);
			sb.append(",");
			sb.append("岗位性质:");
			sb.append(position_type);

			System.out.println(sb.toString());

			// 格式化
			// 报名表字段 "外资/合资企业",//01 "自主创业",//02 "国有企业",//03 "民营企业",//04
			// "政府机构",//05 "事业单位",//06 "其他"//07
			// TZ_CSMB_TJ1 单位性质 政府机构/事业单位1 企业2

			switch (Integer.parseInt(firm_type)) {
			case 1:
				firm_type = "2";
				firm_desc = "外资/合资企业";
				break;
			case 2:
				firm_type = "2";
				firm_desc = "自主创业";
				break;
			case 3:
				firm_type = "2";
				firm_desc = "国有企业";
				break;
			case 4:
				firm_type = "2";
				firm_desc = "民营企业";
				break;
			case 5:
				firm_type = "1";
				firm_desc = "政府机构";
				break;
			case 6:
				firm_type = "1";
				firm_desc = "事业单位";
				break;
			case 7:
				firm_type = "2";
				firm_desc = "其他";
				break;
			}

			// "高层管理（总经理/副总经理以上级）",//01
			// "高级管理（总助/执行主任/执行总监级）",//02
			// "中级管理（总监/部门经理级）",//03
			// "初级管理（主管级/一般经理级）",//04
			// "高级专业人士",//05
			// "初级专业人士",//06
			// "管理培训生",//07
			// "其他"//08

			// "处级及以上",//01
			// "副处级",//02
			// "正科级",//03
			// "副科级",//04
			// "一般科员",//05
			// "其他"//06
			// TZ_CSMB_TJ2 职务
			// 处级及以上/高层管理1
			// 副处级/高级管理2
			// 正科级/中级管理、高级专业人士3
			// 副科级/初级管理4
			// 一般科员/初级专业人士5
			// 其他/其他、管理培训生6
			if (position_type.equals("A1") || position_type.equals("B1")) {
				if (position_type.startsWith("A")) {
					position_desc = "高层管理";
				} else {
					position_desc = "处级及以上";
				}
				position_type = "1";
			} else if (position_type.equals("A2") || position_type.equals("B2")) {
				if (position_type.startsWith("A")) {
					position_desc = "高级管理";
				} else {
					position_desc = "副处级";
				}
				position_type = "2";
			} else if (position_type.equals("A3") || position_type.equals("B3") || position_type.equals("A5")) {
				if (position_type.equals("A3")) {
					position_desc = "中级管理";
				} else if (position_type.equals("A5")) {
					position_desc = "高级专业人士";
				} else {
					position_desc = "正科级";
				}
				position_type = "3";
			} else if (position_type.equals("A4") || position_type.equals("B4")) {
				if (position_type.startsWith("A")) {
					position_desc = "初级管理";
				} else {
					position_desc = "副科级";
				}
				position_type = "4";
			} else if (position_type.equals("A6") || position_type.equals("B5")) {
				if (position_type.startsWith("A")) {
					position_desc = "初级专业人士";
				} else {
					position_desc = "一般科员";
				}
				position_type = "5";
			} else if (position_type.equals("A7") || position_type.equals("A8") || position_type.equals("B6")) {
				if (position_type.equals("A7")) {
					position_desc = "管理培训生";
				} else if (position_type.equals("A8")) {
					position_desc = "其他";
				} else {
					position_desc = "其他";
				}
				position_type = "6";
			}

			String SearchSql = "select TZ_CSMB_SCOR from PS_TZ_CSMB_ZY_T where TZ_CSMB_CK1=? and TZ_CSMB_TJ1=? and TZ_CSMB_TJ2=?";
			String strScore = SqlQuery.queryForObject(SearchSql, new Object[] { "B", firm_type, position_type },
					"String");

			if (strScore == null || strScore.equals("")) {
				strScore = "0";
			}

			// 声明float型字段“得分”，string型字段“打分记录”
			float Score = Float.parseFloat(strScore);

			String MarkRecord = "公司性质：".concat(firm_desc).concat("|职务性质：").concat(position_desc);

			MarkRecord = MarkRecord + "|" + strScore.concat("分");
			
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
