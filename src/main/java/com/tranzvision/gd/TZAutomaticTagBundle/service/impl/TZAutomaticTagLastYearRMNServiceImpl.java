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
 * @description 标签前一年递补保留：前一年递补保留名单是否存在于“当前批次下所有报名表状态等于提交且初审状态不等于审批拒绝的考生中”
 * 
 */

@Service("com.tranzvision.gd.TZAutomaticTagBundle.service.impl.TZAutomaticTagLastYearRMNServiceImpl")
public class TZAutomaticTagLastYearRMNServiceImpl extends TZAutomaticTagServiceImpl{
	@Autowired
	private SqlQuery SqlQuery;
	@Autowired
	private TZGDObject TzSQLObject;
	@Autowired
	private PsTzCsKsbqTMapper PsTzCsKsbqTMapper;

	
	@Override
	public boolean automaticTagList(String classId, String batchId, String labelId) {
		try {
			String zdbqLyrmnIdSql = "SELECT TZ_HARDCODE_VAL FROM  TZGDQHDEV.PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_ZDBQ_LYRMN_ID'";
			String zdbqLyrmnId = SqlQuery.queryForObject(zdbqLyrmnIdSql, "String");
			String zdbqLyrmnNameSql = "SELECT TZ_BIAOQZ_NAME FROM  PS_TZ_BIAOQZ_BQ_T WHERE TZ_BIAOQ_ID=?";
			String zdbqLyrmnName = SqlQuery.queryForObject(zdbqLyrmnNameSql,new Object[] { zdbqLyrmnId },  "String");

			List<?> tzappins = SqlQuery.queryForList(
					TzSQLObject.getSQLText("SQL.TZAutomaticTagBundle.TZAutomaticTagLastYearRMNServiceSql"),
					new Object[] { classId, batchId });
			if (tzappins != null && tzappins.size() > 0) {
				for (int i = 0; i < tzappins.size(); i++) {
					PsTzCsKsbqT PsTzCsKsbqT = new PsTzCsKsbqT();
					PsTzCsKsbqT.setTzAppInsId(Long.valueOf(tzappins.get(i).toString()));
					PsTzCsKsbqT.setTzClassId(classId);
					PsTzCsKsbqT.setTzApplyPcId(batchId);
					PsTzCsKsbqT.setTzZdbqId(zdbqLyrmnId);
					PsTzCsKsbqTMapper.insert(PsTzCsKsbqT);				

				}

			}
		} catch (TzSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	return true;
	}
}
