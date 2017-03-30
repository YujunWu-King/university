package com.tranzvision.gd.TZNegativeListInfeBundle.service.impl;

/**
 * 
 * @author tzhjl
 * @since 2017-02-09
 */
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCsKsTblMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsKsTbl;
import com.tranzvision.gd.TZNegativeListInfeBundle.dao.PsTzCsKsFmTMapper;
import com.tranzvision.gd.TZNegativeListInfeBundle.model.PsTzCsKsFmT;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.TZNegativeListInfeBundle.service.impl.TzNegativeApplyNumber")
public class TzNegativeApplyNumberServiceImpl extends TzNegativeListBundleServiceImpl {
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
	private PsTzCsKsTblMapper PsTzCsKsTBLMapper;

	@Override
	public boolean makeNegativeList(String classId, String batchId, String labelId) {
		Integer havenumber = 0;
		String oprid = "";
		String OrgID = tzLoginServiceImpl.getLoginedManagerOrgid(request);

		Date nowdate_time = new Date();

		try {
			String hodecode = "SELECT TZ_HARDCODE_VAL FROM  PS_TZ_HARDCD_PNT WHERE TZ_HARDCD_PNT='TZ_KSFMQDID_OVER3'";
			String fmqdId = SqlQuery.queryForObject(hodecode, "String");

			List<?> opridlist = SqlQuery.queryForList(
					TzSQLObject.getSQLText("SQL.TZNegativeListInfeBundle.TzNegativeApplyNumber"),
					new Object[] { classId, batchId });

			if (opridlist != null && opridlist.size() > 0) {
				for (int i = 0; i < opridlist.size(); i++) {
					oprid = (String) opridlist.get(i);
					havenumber = SqlQuery.queryForObject(
							TzSQLObject.getSQLText("SQL.TZNegativeListInfeBundle.TzNegativeOverthree"),
							new Object[] { oprid }, "Integer");
					if (havenumber != null && havenumber > 3) {
						String sql = "SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID=? AND TZ_CLASS_ID=? ";
						Integer appinsId = SqlQuery.queryForObject(sql, new Object[] { classId, oprid }, "Integer");
						PsTzCsKsFmT PsTzCsKsFmT = new PsTzCsKsFmT();
						// String fmqdId = "TZ_FMQ" +
						// String.valueOf(getSeqNum.getSeqNum("PS_TZ_CS_KSFM_T",
						// "TZ_FMQD_ID"));
						PsTzCsKsFmT.setTzAppInsId(Long.valueOf(appinsId));
						PsTzCsKsFmT.setTzClassId(classId);
						PsTzCsKsFmT.setTzApplyPcId(batchId);
						PsTzCsKsFmT.setTzJgId(OrgID);
						PsTzCsKsFmT.setTzFmqdId(fmqdId);
						PsTzCsKsFmT.setTzFmqdName("申请次数大于3次");
						PsTzCsKsFmTMapper.insert(PsTzCsKsFmT);

						PsTzCsKsTbl PsTzCsKsTBL = new PsTzCsKsTbl();

						PsTzCsKsTBL.setTzAppInsId(Long.valueOf(appinsId));
						PsTzCsKsTBL.setTzClassId(classId);
						PsTzCsKsTBL.setTzApplyPcId(batchId);
						// PsTzCsKsTBL.setTzJgId(OrgID);
						PsTzCsKsTBL.setRowLastmantDttm(nowdate_time);
						PsTzCsKsTBL.setTzKshCsjg("N");
						PsTzCsKsTBLMapper.updateByPrimaryKeySelective(PsTzCsKsTBL);

						// TZ_REG_USER_T
					}
				}

			}
		} catch (TzSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO Auto-generated method stub
		return true;
	}

}
