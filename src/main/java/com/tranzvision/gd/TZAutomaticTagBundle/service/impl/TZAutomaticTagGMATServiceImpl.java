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
			//GMAT720+ 标签id hardcode点;
			String zdbqGMATIdSql = "SELECT TZ_HARDCODE_VAL FROM  PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_ZDBQ_INISCRN_ID'";
			String zdbqGMATId = SqlQuery.queryForObject(zdbqGMATIdSql, "String");
			String zdbqGMATNameSql = "SELECT TZ_BIAOQZ_NAME FROM  PS_TZ_BIAOQZ_BQ_T WHERE TZ_BIAOQ_ID=?";
			String zdbqGMATName = SqlQuery.queryForObject(zdbqGMATNameSql, new Object[] { zdbqGMATId }, "String");
			
			//自动标签-报名表英语水平的编号
			String engLevelSql = "SELECT TZ_HARDCODE_VAL FROM  PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_BMB_ENG_LEVEL'";
			String engLevel = SqlQuery.queryForObject(engLevelSql,"String");
			if(engLevel == null){
				engLevel = "";
			}
			
			//自动标签-考试名称为GMAT的下拉值；
			String GMATTypeSql = "SELECT TZ_HARDCODE_VAL FROM  PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_BMB_GMAT'";
			String GMATType = SqlQuery.queryForObject(GMATTypeSql,"String");
			if(GMATType == null){
				GMATType = "GMAT";
			}
			
			//考试名称的编号;
			String engKsTypeBh = engLevel + "exam_type";
			//考试时间编号：
			String engKsDateBh =  engLevel + "exam_date";
			//考试成绩编号;
			String engKsCjBh = engLevel + "exam_score";
			System.out.println("GMAT打分开始======>");
			List<?> tzappins = SqlQuery.queryForList(
					TzSQLObject.getSQLText("SQL.TZAutomaticTagBundle.TZAutomaticTagGMATServiceSql"),
					new Object[] { batchId,classId,engKsTypeBh+'%',GMATType, engKsCjBh+"%", engKsDateBh+"%"});
			System.out.println("SQL======>"+TzSQLObject.getSQLText("SQL.TZAutomaticTagBundle.TZAutomaticTagGMATServiceSql"));
			
			System.out.println(classId+"===>"+ batchId+"===>"+engKsTypeBh+'%'+"===>"+GMATType+"===>"+ engKsCjBh+"%"+"===>"+engKsDateBh+"%");
			// 循环符合初筛条件的报名表id--start
			if (tzappins != null && tzappins.size() > 0) {
				for (int i = 0; i < tzappins.size(); i++) {
					long appind = Long.valueOf(String.valueOf(tzappins.get(i)));
					System.out.println("=======appind========>"+ appind);
					// 循环考试类型为GMAT的字段id对应的序号--start
					List<?> Seq = SqlQuery.queryForList(
							"SELECT substring(TZ_XXX_BH, length('"+engKsTypeBh+"')+1) FROM PS_TZ_APP_CC_T WHERE TZ_APP_S_TEXT=? AND TZ_APP_INS_ID=? and TZ_XXX_BH like ?",
							new Object[] {GMATType,appind,engKsTypeBh+'%' });
					if (Seq != null && Seq.size() > 0) {
						for (int j = 0; j < Seq.size(); j++) {

							/*
							 * 是否符合自动标签条件： 考试类型为GMAT and GMAT对应分数>=720 and
							 * GMAT对应考试日期为5年内（考试日期大于等于班级入学日期的年份减去5年的日期）;
							 */

							String FlgSql = "SELECT 'Y' FROM dual WHERE (SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE TZ_XXX_BH = '"
									+ engKsCjBh + String.valueOf(Seq.get(j))
									+ "'  AND TZ_APP_INS_ID=? )>=720 AND (SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE TZ_XXX_BH = '"
									+ engKsDateBh + String.valueOf(Seq.get(j))
									+ "' and TZ_APP_INS_ID=? )>= (select date_add(TZ_RX_DT, interval -5 year) FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?)";
							String Flg = SqlQuery.queryForObject(FlgSql,
									new Object[] { appind, appind, classId },
									"String");

							if (Flg == "Y") {
								PsTzCsKsbqT PsTzCsKsbqT = new PsTzCsKsbqT();
								PsTzCsKsbqT.setTzAppInsId(appind);
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
			}else{
				System.out.println("===================>gmat没有符合条件的报名表");
			}
			// 循环符合初筛条件的报名表id--end

		} catch (TzSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}
}
