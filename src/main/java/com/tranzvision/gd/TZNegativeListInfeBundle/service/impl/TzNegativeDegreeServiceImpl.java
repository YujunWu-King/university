package com.tranzvision.gd.TZNegativeListInfeBundle.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCsKsTblMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsKsTbl;
import com.tranzvision.gd.TZNegativeListInfeBundle.dao.PsTzCsKsFmTMapper;
import com.tranzvision.gd.TZNegativeListInfeBundle.model.PsTzCsKsFmT;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.TZNegativeListInfeBundle.service.impl.TzNegativeDegreeServiceImpl")
public class TzNegativeDegreeServiceImpl extends TzNegativeListBundleServiceImpl {
	@Autowired
	private TZGDObject TzSQLObject;
	@Autowired
	private SqlQuery SqlQuery;
	@Autowired
	private PsTzCsKsFmTMapper PsTzCsKsFmTMapper;

	@Autowired
	private PsTzCsKsTblMapper PsTzCsKsTBLMapper;

	@Override
	public boolean makeNegativeList(String classId, String batchId, String labelId) {
		String oprid = "";
		String OrgID = "SEM";
		Date nowdate_time = new Date();
		String sql = "";
		String sql1 = "";
		Integer appinsId = 0;
		String degree = "";
		String degree1 = "";
		String degree2 = "";
		try {
			String hodecode = "SELECT TZ_HARDCODE_VAL FROM  PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_KSFMQDID_GEGREE'";
			String fmqdId = SqlQuery.queryForObject(hodecode, "String");
			List<Map<String, Object>> opridlist = SqlQuery.queryForList(
					TzSQLObject.getSQLText("SQL.TZNegativeListInfeBundle.TzNegativeApplyNumber"),
					new Object[] { classId, batchId });
			if (opridlist != null && opridlist.size() > 0) {
				for (int i = 0; i < opridlist.size(); i++) {
					oprid = opridlist.get(i).get("OPRID").toString();
					String sqlhave = "SELECT 'Y' FROM PS_TZ_BLACK_LIST_V WHERE OPRID=?";
					if (SqlQuery.queryForObject(sqlhave, "String") != null
							&& SqlQuery.queryForObject(sqlhave, "String").equals("Y")) {
						sql = "SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID=? AND TZ_CLASS_ID=? ";
						appinsId = SqlQuery.queryForObject(sql, new Object[] { classId, oprid }, "Integer");
						sql1 = "SELECT TZ_APP_S_TEXT from PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID=? AND TZ_XXX_BH=?";

						degree = SqlQuery.queryForObject(sql1, new Object[] { appinsId, "TZ_13TZ_TZ_13_6" }, "String");
						degree1 = SqlQuery.queryForObject(sql1, new Object[] { appinsId, "TZ_12fl2" }, "String");
						degree2 = SqlQuery.queryForObject(sql1, new Object[] { appinsId, "TZ_11fl" }, "String");

						if (degree == null || !degree.equals("1") || degree1 == null || !degree1.equals("1")
								|| degree2 == null || degree2.equals("1")) {
							PsTzCsKsFmT PsTzCsKsFmT = new PsTzCsKsFmT();
							// String fmqdId = "TZ_FMQ" +
							// String.valueOf(getSeqNum.getSeqNum("PS_TZ_CS_KSFM_T",
							// "TZ_FMQD_ID"));
							PsTzCsKsFmT.setTzAppInsId(Long.valueOf(appinsId));
							PsTzCsKsFmT.setTzClassId(classId);
							PsTzCsKsFmT.setTzApplyPcId(batchId);
							PsTzCsKsFmT.setTzFmqdId(fmqdId);
							PsTzCsKsFmT.setTzFmqdName("学位学历");
							PsTzCsKsFmTMapper.insert(PsTzCsKsFmT);
							PsTzCsKsTbl PsTzCsKsTBL = new PsTzCsKsTbl();

							PsTzCsKsTBL.setTzAppInsId(Long.valueOf(appinsId));
							PsTzCsKsTBL.setTzClassId(classId);
							PsTzCsKsTBL.setTzApplyPcId(batchId);
							// PsTzCsKsTBL.setTzJgId(OrgID);
							PsTzCsKsTBL.setRowLastmantDttm(nowdate_time);
							PsTzCsKsTBL.setTzKshCsjg("N");
							PsTzCsKsTBLMapper.updateByPrimaryKeySelective(PsTzCsKsTBL);

						}

					}
				}
			}

		} catch (TzSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO Auto-generated method stub
		return false;
	}

}
