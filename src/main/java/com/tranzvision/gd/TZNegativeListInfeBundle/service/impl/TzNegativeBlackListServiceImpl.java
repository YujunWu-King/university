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

/**
 * 人员是否在黑名单中
 * 
 * @author tzhjl
 * @since 2017-2-14
 */
@Service("com.tranzvision.gd.TZNegativeListInfeBundle.service.impl.TzNegativeBlackListServiceImpl")
public class TzNegativeBlackListServiceImpl extends TzNegativeListBundleServiceImpl {
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
		Date nowdate_time = new Date();
		int have_one = 0;
		int count = 0;
		System.out.println("进入黑名单");
		try {
			String hodecode = "SELECT TZ_HARDCODE_VAL FROM  PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_KSFMQDID_BLACK'";
			String fmqdId = SqlQuery.queryForObject(hodecode, "String");
			List<Map<String, Object>> opridlist = SqlQuery.queryForList(
					TzSQLObject.getSQLText("SQL.TZNegativeListInfeBundle.TzNegativeApplyNumber"),
					new Object[] { batchId, classId });
			if (opridlist != null && opridlist.size() > 0) {
				for (int i = 0; i < opridlist.size(); i++) {

					System.out.println("黑名单Id" + opridlist.get(i).get("OPRID").toString());
					oprid = opridlist.get(i).get("OPRID").toString();
					String sqlhave = "SELECT COUNT(1) FROM PS_TZ_BLACK_LIST_V WHERE OPRID=?";
					count = SqlQuery.queryForObject(sqlhave, new Object[] { oprid }, "Integer");

					if (count > 0) {
						String sql = "SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID=? AND TZ_CLASS_ID=? ";
						Integer appinsId = SqlQuery.queryForObject(sql, new Object[] { oprid, classId }, "Integer");
						PsTzCsKsFmT PsTzCsKsFmT = new PsTzCsKsFmT();

						PsTzCsKsFmT.setTzAppInsId(Long.valueOf(appinsId));
						PsTzCsKsFmT.setTzClassId(classId);
						PsTzCsKsFmT.setTzApplyPcId(batchId);
						PsTzCsKsFmT.setTzFmqdId(fmqdId);
						PsTzCsKsFmT.setTzFmqdName("黑名单");
						have_one = SqlQuery.queryForObject(
								"SELECT COUNT(1) FROM PS_TZ_CS_KSFM_T WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? AND TZ_FMQD_ID=?",
								new Object[] { classId, batchId, Long.valueOf(appinsId), fmqdId }, "Integer");
						if (have_one > 0) {
							PsTzCsKsFmTMapper.updateByPrimaryKeySelective(PsTzCsKsFmT);

						} else {
							PsTzCsKsFmTMapper.insert(PsTzCsKsFmT);
						}

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

		} catch (TzSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
