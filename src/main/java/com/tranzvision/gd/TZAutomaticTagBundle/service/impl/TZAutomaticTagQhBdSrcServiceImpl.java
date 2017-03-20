package com.tranzvision.gd.TZAutomaticTagBundle.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZAutomaticTagBundle.service.impl.TZAutomaticTagServiceImpl;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsKsbqTKey;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCsKsbqTMapper;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 
 * @author YTT
 * @since 2017-03-01
 * @description 标签清华（北大）生源：本科教育经历是清华或北大，且有学士学位
 * 
 */

@Service("com.tranzvision.gd.TZAutomaticTagBundle.service.impl.TZAutomaticTagQhBdSrcServiceImpl")
public class TZAutomaticTagQhBdSrcServiceImpl extends TZAutomaticTagServiceImpl {
	@Autowired
	private SqlQuery SqlQuery;
	@Autowired
	private TZGDObject TzSQLObject;
	@Autowired
	private PsTzCsKsbqTMapper PsTzCsKsbqTMapper;

	@Override
	public boolean automaticTagList(String classId, String batchId, String labelId) {
		try {
			// 标签id：清华（北大）生源-本科教育经历是清华或北大，且有学士学位
			String zdbqQBSrcIdSql = "SELECT TZ_HARDCODE_VAL FROM  TZGDQHDEV.PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_ZDBQ_QBSRC_ID'";
			String zdbqQBSrcId = SqlQuery.queryForObject(zdbqQBSrcIdSql, "String");

			List<?> tzappins = SqlQuery.queryForList(
					TzSQLObject.getSQLText("SQL.TZAutomaticTagBundle.TZAutomaticTagLastYearRMNServiceSql"),
					new Object[] { classId, batchId });
			if (tzappins != null && tzappins.size() > 0) {
				for (int i = 0; i < tzappins.size(); i++) {
					PsTzCsKsbqTKey PsTzCsKsbqTKey = new PsTzCsKsbqTKey();
					PsTzCsKsbqTKey.setTzAppInsId(Long.valueOf(tzappins.get(i).toString()));
					PsTzCsKsbqTKey.setTzClassId(classId);
					PsTzCsKsbqTKey.setTzApplyPcId(batchId);
					PsTzCsKsbqTKey.setTzZdbqId(zdbqQBSrcId);
					PsTzCsKsbqTMapper.insert(PsTzCsKsbqTKey);				

				}

			}
		} catch (TzSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}
}
