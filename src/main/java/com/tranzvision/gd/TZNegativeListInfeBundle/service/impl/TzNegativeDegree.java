package com.tranzvision.gd.TZNegativeListInfeBundle.service.impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZNegativeListInfeBundle.dao.PsTzCsKsFmTMapper;
import com.tranzvision.gd.TZNegativeListInfeBundle.dao.PsTzCsKsTBLMapper;
import com.tranzvision.gd.TZNegativeListInfeBundle.model.PsTzCsKsFmT;
import com.tranzvision.gd.TZNegativeListInfeBundle.model.PsTzCsKsTBL;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.TZNegativeListInfeBundle.service.impl.TzNegativeDegree")
public class TzNegativeDegree extends TzNegativeListBundle {
	@Autowired
	private TZGDObject TzSQLObject;
	@Autowired
	private SqlQuery SqlQuery;
	@Autowired
	private PsTzCsKsFmTMapper PsTzCsKsFmTMapper;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzCsKsTBLMapper PsTzCsKsTBLMapper;

	@Override
	public boolean makeNegativeList(String classId, String batchId, String labelId) {
		String oprid = "";
		String OrgID = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		Date nowdate_time = new Date();
		String sql = "";
		String sql1 = "";
		Integer appinsId = 0;
		String degree = "";
		try {
			String hodecode = "SELECT TZ_HARDCODE_VAL FROM  PS_TZ_HARDCD_PNT WHERE TZ_HARDCD_PNT='TZ_KSFMQDID'";
			String fmqdId = SqlQuery.queryForObject(hodecode, "String");
			List<?> opridlist = SqlQuery.queryForList(
					TzSQLObject.getSQLText("SQL.TZNegativeListInfeBundle.TzNegativeApplyNumber"),
					new Object[] { classId, batchId });
			if (opridlist != null && opridlist.size() > 0) {
				for (int i = 0; i < opridlist.size(); i++) {
					oprid = (String) opridlist.get(i);
					String sqlhave = "SELECT 'Y' FROM PS_TZ_BLACK_LIST_V WHERE OPRID=?";
					if (SqlQuery.queryForObject(sqlhave, "String") != null
							&& SqlQuery.queryForObject(sqlhave, "String").equals("Y")) {
						sql = "SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID=? AND TZ_CLASS_ID=? ";
						appinsId = SqlQuery.queryForObject(sql, new Object[] { classId, oprid }, "Integer");
						sql1 = "SELECT TZ_APP_S_TEXT from PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID=? AND TZ_XXX_BH=?";
						degree = SqlQuery.queryForObject(sql1, new Object[] { appinsId, "XXXXX" }, "String");

						if (degree == null || degree.equals("") || degree.equals("XXX") || degree.equals("XXX")) {
							PsTzCsKsFmT PsTzCsKsFmT = new PsTzCsKsFmT();
							// String fmqdId = "TZ_FMQ" +
							// String.valueOf(getSeqNum.getSeqNum("PS_TZ_CS_KSFM_T",
							// "TZ_FMQD_ID"));
							PsTzCsKsFmT.setTzAppInsId(Long.valueOf(appinsId));
							PsTzCsKsFmT.setTzClassId(classId);
							PsTzCsKsFmT.setTzApplyPcId(batchId);
							PsTzCsKsFmT.setTzJgId(OrgID);
							PsTzCsKsFmT.setTzFmqdId(fmqdId);
							PsTzCsKsFmT.setTzFmqdName("学位学历");
							PsTzCsKsFmTMapper.insert(PsTzCsKsFmT);
							PsTzCsKsTBL PsTzCsKsTBL = new PsTzCsKsTBL();

							PsTzCsKsTBL.setTzAppInsId(appinsId);
							PsTzCsKsTBL.setTzClassId(classId);
							PsTzCsKsTBL.setTzApplyPcId(batchId);
							PsTzCsKsTBL.setTzJgId(OrgID);
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
