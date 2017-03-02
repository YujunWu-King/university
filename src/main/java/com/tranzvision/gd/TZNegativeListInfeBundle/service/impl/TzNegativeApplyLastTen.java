
package com.tranzvision.gd.TZNegativeListInfeBundle.service.impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZNegativeListInfeBundle.dao.PsTzCsKsFmTMapper;
import com.tranzvision.gd.TZNegativeListInfeBundle.dao.PsTzCsKsTBLMapper;
import com.tranzvision.gd.TZNegativeListInfeBundle.model.PsTzCsKsFmTKey;
import com.tranzvision.gd.TZNegativeListInfeBundle.model.PsTzCsKsTBL;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 后百分之10
 * 
 * @author tzhjl
 * @since 2017-02-09
 */
@Service("com.tranzvision.gd.TZNegativeListInfeBundle.service.impl.TzNegativeApplyLastTen")
public class TzNegativeApplyLastTen extends TzNegativeListBundle {
	@Autowired
	private SqlQuery SqlQuery;
	@Autowired
	private PsTzCsKsFmTMapper PsTzCsKsFmTMapper;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzCsKsTBLMapper PsTzCsKsTBLMapper;

	@Override
	public boolean makeNegativeList(String classId, String batchId, String labelId) {

		String OrgID = tzLoginServiceImpl.getLoginedManagerOrgid(request);

		Date nowdate_time = new Date();

		String sqlcount = "SELECT COUNT(1) FROM PS_TZ_CS_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? ";
		int count = SqlQuery.queryForObject(sqlcount, new Object[] { classId, batchId }, "Integer");
		count = (int) (count * 0.1);
		String sqltzappint = "SELECT TZ_APP_INS_ID FROM PS_TZ_CS_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? ORDER BY TZ_KSH_PSPM DESC LIMIT ?  ";
		List<?> tzappintlist = SqlQuery.queryForList(sqltzappint, new Object[] { classId, batchId, count });

		if (tzappintlist != null && tzappintlist.size() > 0) {
			for (int i = 0; i < tzappintlist.size(); i++) {
				PsTzCsKsFmTKey PsTzCsKsFmTKey = new PsTzCsKsFmTKey();
				String fmqdId = "TZ_FMQ" + String.valueOf(getSeqNum.getSeqNum("PS_TZ_CS_KSFM_T", "TZ_FMQD_ID"));
				PsTzCsKsFmTKey.setTzAppInsId((Integer) tzappintlist.get(i));
				PsTzCsKsFmTKey.setTzClassId(classId);
				PsTzCsKsFmTKey.setTzApplyPcId(batchId);
				PsTzCsKsFmTKey.setTzJgId(OrgID);
				PsTzCsKsFmTKey.setTzFmqdId(fmqdId);
				PsTzCsKsFmTMapper.insert(PsTzCsKsFmTKey);
				PsTzCsKsTBL PsTzCsKsTBL = new PsTzCsKsTBL();

				PsTzCsKsTBL.setTzAppInsId((Integer) tzappintlist.get(i));
				PsTzCsKsTBL.setTzClassId(classId);
				PsTzCsKsTBL.setTzApplyPcId(batchId);
				PsTzCsKsTBL.setTzJgId(OrgID);
				PsTzCsKsTBL.setRowLastmantDttm(nowdate_time);
				PsTzCsKsTBL.setTzKshCsjg("N");
				PsTzCsKsTBLMapper.updateByPrimaryKeySelective(PsTzCsKsTBL);
			}
		}

		// TODO Auto-generated method stub
		return true;
	}

}
