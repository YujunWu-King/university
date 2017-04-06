package com.tranzvision.gd.TZAutomaticTagBundle.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZAutomaticTagBundle.service.impl.TZAutomaticTagServiceImpl;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsKsbqT;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCsKsbqTMapper;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 
 * @author YTT
 * @since 2017-03-01
 * @description 标签GMAT720+：考试时间<5年 and 英语成绩>=720分
 * 
 */

@Service("com.tranzvision.gd.TZAutomaticTagBundle.service.impl.TZAutomaticTagGMATServiceImpl")
public class TZAutomaticTagGMATServiceImpl extends TZAutomaticTagServiceImpl {
	@Autowired
	private SqlQuery SqlQuery;
	@Autowired
	private TZGDObject TzSQLObject;
	@Autowired
	private PsTzCsKsbqTMapper PsTzCsKsbqTMapper;

	@Override
	public boolean automaticTagList(String classId, String batchId, String labelId) {
		try {
			String zdbqGMATIdSql = "SELECT TZ_HARDCODE_VAL FROM  PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_ZDBQ_INISCRN_ID'";
			String zdbqGMATId = SqlQuery.queryForObject(zdbqGMATIdSql, "String");
			String zdbqGMATNameSql = "SELECT TZ_BIAOQZ_NAME FROM  PS_TZ_BIAOQZ_BQ_T WHERE TZ_BIAOQ_ID=?";
			String zdbqGMATName = SqlQuery.queryForObject(zdbqGMATNameSql, new Object[] { zdbqGMATId }, "String");

			List<?> tzappins = SqlQuery.queryForList(
					TzSQLObject.getSQLText("SQL.TZAutomaticTagBundle.TZAutomaticTagGMATServiceSql"),
					new Object[] { classId, batchId });

			// 循环符合初筛条件的报名表id--start
			if (tzappins != null && tzappins.size() > 0) {
				for (int i = 0; i < tzappins.size(); i++) {

					// 循环考试类型为GMAT的字段id对应的序号--start
					List<?> Seq = SqlQuery.queryForList(
							"SELECT substring(TZ_XXX_BH, length('TZ_44exam_type')+1) FROM PS_TZ_APP_CC_T WHERE TZ_APP_S_TEXT='ENG_LEV_T2' AND TZ_APP_INS_ID=?",
							new Object[] { tzappins.get(i).toString() });
					if (Seq != null && Seq.size() > 0) {
						for (int j = 0; j < Seq.size(); j++) {

							/*
							 * 是否符合自动标签条件： 考试类型为GMAT and GMAT对应分数>=720 and
							 * GMAT对应考试日期为5年内（考试日期大于等于班级入学日期的年份减去5年的日期）;
							 */

							String FlgSql = "SELECT 'Y' FROM dual WHERE (SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE TZ_XXX_BH = 'TZ_44exam_score"
									+ Seq.get(j).toString()
									+ "'  AND TZ_APP_INS_ID=? )>=720 AND (SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE TZ_XXX_BH = 'TZ_44exam_date"
									+ Seq.get(j).toString()
									+ "' and TZ_APP_INS_ID=? )>= (select date_add(TZ_RX_DT, interval -5 year) FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?)";
							String Flg = SqlQuery.queryForObject(FlgSql,
									new Object[] { tzappins.get(i).toString(), tzappins.get(i).toString(), classId },
									"String");

							if (Flg == "Y") {
								PsTzCsKsbqT PsTzCsKsbqT = new PsTzCsKsbqT();
								PsTzCsKsbqT.setTzAppInsId(Long.valueOf(tzappins.get(i).toString()));
								PsTzCsKsbqT.setTzClassId(classId);
								PsTzCsKsbqT.setTzApplyPcId(batchId);
								PsTzCsKsbqT.setTzZdbqId(zdbqGMATId);
								PsTzCsKsbqT.setTzBiaoqzName(zdbqGMATName);
								PsTzCsKsbqTMapper.insert(PsTzCsKsbqT);
								// 如果符合条件，则跳出循环，直接检查下一报名表
								break;
							}
						}
					}
					// 循环考试类型为GMAT的字段id对应的序号--end
				}
			}
			// 循环符合初筛条件的报名表id--end

		} catch (TzSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}
}
