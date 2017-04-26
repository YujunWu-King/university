package com.tranzvision.gd.TZAutomaticTagBundle.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZAutomaticTagBundle.service.impl.TZAutomaticTagServiceImpl;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsKsbqT;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCsKsbqTMapper;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author YTT
 * @since 2017-03-01
 * @description 标签初筛淘汰：评审后成绩排名后10%（排名后10%）
 * 
 */

@Service("com.tranzvision.gd.TZAutomaticTagBundle.service.impl.TZAutomaticTagIniScrnServiceImpl")
public class TZAutomaticTagIniScrnServiceImpl extends TZAutomaticTagServiceImpl {
	@Autowired
	private SqlQuery SqlQuery;
	@Autowired
	private PsTzCsKsbqTMapper PsTzCsKsbqTMapper;

	@Override
	public boolean automaticTagList(String classId, String batchId, String labelId) {
		try {
			//int fst_pm;
			//int lst_pm;
			String zdbqIniScrnIdSql = "SELECT TZ_HARDCODE_VAL FROM  PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_ZDBQ_INISCRN_ID'";
			String zdbqIniScrnId = SqlQuery.queryForObject(zdbqIniScrnIdSql, "String");
			if(zdbqIniScrnId != null && !"".equals(zdbqIniScrnId)){
				//删除该班级批次下人员的评审后成绩排名后10%,重新计算;
				SqlQuery.update("delete from PS_TZ_CS_KSBQ_T where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_ZDBQ_ID=?",new Object[]{classId,batchId,zdbqIniScrnId});
			}
			
			String zdbqIniScrnNameSql = "SELECT TZ_BIAOQZ_NAME FROM  PS_TZ_BIAOQZ_BQ_T WHERE TZ_BIAOQ_ID=?";
			String zdbqIniScrnName = SqlQuery.queryForObject(zdbqIniScrnNameSql, new Object[] { zdbqIniScrnId },
					"String");
			
			//需要标签的人数;
			String sqlcount = "SELECT COUNT(1) FROM PS_TZ_CS_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? ";
			int count = SqlQuery.queryForObject(sqlcount, new Object[] { classId, batchId }, "Integer");
			count = (int) (count * 0.1);
			if(count > 0){
				//需要标签人数的后一位排名;
				int hywPm = SqlQuery.queryForObject("SELECT TZ_KSH_PSPM FROM PS_TZ_CS_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? ORDER BY TZ_KSH_PSPM DESC LIMIT ?,1", new Object[]{classId, batchId ,count},"Integer");
				List<Map<String, Object>> list = SqlQuery.queryForList("SELECT TZ_APP_INS_ID,TZ_KSH_PSPM FROM PS_TZ_CS_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? ORDER BY TZ_KSH_PSPM DESC LIMIT ?",new Object[]{classId, batchId ,count});		
				if(list != null && list.size()>0){
					for(int i = 0; i < list.size(); i++){
						//和淘汰人数前一位排名不相同的全部淘汰;
						int pm = (int)list.get(i).get("TZ_KSH_PSPM");
						if(pm > hywPm){
							//淘汰;
							long appinsId = Long.valueOf(String.valueOf(list.get(i).get("TZ_APP_INS_ID")));
							
							PsTzCsKsbqT PsTzCsKsbqT = new PsTzCsKsbqT();
							PsTzCsKsbqT.setTzAppInsId(appinsId);
							PsTzCsKsbqT.setTzClassId(classId);
							PsTzCsKsbqT.setTzApplyPcId(batchId);
							PsTzCsKsbqT.setTzZdbqId(zdbqIniScrnId);
							PsTzCsKsbqT.setTzBiaoqzName(zdbqIniScrnName);
							PsTzCsKsbqTMapper.insert(PsTzCsKsbqT);
						}
					}
				}
			}
			
			
			/*
			int a = 0;

			String sqlcount = "SELECT COUNT(1) FROM PS_TZ_CS_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? ";
			int count = SqlQuery.queryForObject(sqlcount, new Object[] { classId, batchId }, "Integer");
			count = (int) (count * 0.1);

			if (count > 0) {
				String sql = "SELECT TZ_KSH_PSPM FROM PS_TZ_CS_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? ORDER BY TZ_KSH_PSPM DESC LIMIT ?,1";

				// 1.a=0; 2.循环比较最后百分之10的第a个和前百分之90的最后一个， 3.如果相同 4.a++
				// 5.就在比较最后百分之10的第二个前百分之90的最后一个 循环
				fst_pm = SqlQuery.queryForObject(sql, new Object[] { classId, batchId, count - 1 - a }, "Integer");
				lst_pm = SqlQuery.queryForObject(sql, new Object[] { classId, batchId, count }, "Integer");

				while (fst_pm >= lst_pm) {
					a++;
					fst_pm = SqlQuery.queryForObject(sql, new Object[] { classId, batchId, count - 1 - a }, "Integer");
				}
				;

				String sqltzappins = "SELECT TZ_APP_INS_ID FROM PS_TZ_CS_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? ORDER BY TZ_KSH_PSPM DESC LIMIT ?  ";
				List<?> tzappins = SqlQuery.queryForList(sqltzappins, new Object[] { classId, batchId, count - a });

				if (tzappins != null && tzappins.size() > 0) {
					for (int i = 0; i < tzappins.size(); i++) {
						PsTzCsKsbqT PsTzCsKsbqT = new PsTzCsKsbqT();
						PsTzCsKsbqT.setTzAppInsId(Long.valueOf(tzappins.get(i).toString()));
						PsTzCsKsbqT.setTzClassId(classId);
						PsTzCsKsbqT.setTzApplyPcId(batchId);
						PsTzCsKsbqT.setTzZdbqId(zdbqIniScrnId);
						PsTzCsKsbqT.setTzBiaoqzName(zdbqIniScrnName);
						PsTzCsKsbqTMapper.insert(PsTzCsKsbqT);

					}
				}
			}
			*/
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return true;
	}
}
