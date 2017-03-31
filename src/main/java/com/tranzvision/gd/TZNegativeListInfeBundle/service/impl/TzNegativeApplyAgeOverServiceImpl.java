package com.tranzvision.gd.TZNegativeListInfeBundle.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZNegativeListInfeBundle.dao.PsTzCsKsFmTMapper;
import com.tranzvision.gd.TZNegativeListInfeBundle.model.PsTzCsKsFmT;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 年级超过45数的
 * 
 * @author tzhjl
 *
 */
@Service("com.tranzvision.gd.TZNegativeListInfeBundle.service.impl.TzNegativeApplyAgeOver")
public class TzNegativeApplyAgeOverServiceImpl extends TzNegativeListBundleServiceImpl {
	@Autowired
	private SqlQuery SqlQuery;
	@Autowired
	private TZGDObject TzSQLObject;
	@Autowired
	private PsTzCsKsFmTMapper PsTzCsKsFmTMapper;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;

	@Override
	public boolean makeNegativeList(String classId, String batchId, String labelId) {

		try {
			String OrgID = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String hodecode = "SELECT TZ_HARDCODE_VAL FROM  PS_TZ_HARDCD_PNT WHERE TZ_HARDCD_PNT='TZ_KSFMQDID_AGE'";
			String fmqdId = SqlQuery.queryForObject(hodecode, "String");

			List<?> opridlist = SqlQuery.queryForList(
					TzSQLObject.getSQLText("SQL.TZNegativeListInfeBundle.TzNegativeApplyNumber"),
					new Object[] { classId, batchId });
			if (opridlist != null && opridlist.size() > 0) {
				for (int i = 0; i < opridlist.size(); i++) {
					String sqlage = "SELECT BIRTHDATE FROM PS_TZ_REG_USER_T WHERE OPRID=?";
					if (getAge(
							SqlQuery.queryForObject(sqlage, new Object[] { (String) opridlist.get(i) }, "Date")) > 45) {
						String sql = "SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID=? AND TZ_CLASS_ID=? ";
						Long appinsId = SqlQuery.queryForObject(sql,
								new Object[] { classId, (String) opridlist.get(i) }, "Long");
						PsTzCsKsFmT PsTzCsKsFmT = new PsTzCsKsFmT();
						// String fmqdId = "TZ_FMQ" +
						// String.valueOf(getSeqNum.getSeqNum("PS_TZ_CS_KSFM_T",
						// "TZ_FMQD_ID"));
						PsTzCsKsFmT.setTzAppInsId(appinsId);
						PsTzCsKsFmT.setTzClassId(classId);
						PsTzCsKsFmT.setTzApplyPcId(batchId);
						PsTzCsKsFmT.setTzJgId(OrgID);
						PsTzCsKsFmT.setTzFmqdId(fmqdId);
						PsTzCsKsFmT.setTzFmqdName("年龄大于45岁");
						PsTzCsKsFmTMapper.insert(PsTzCsKsFmT);

					}

				}

			}
		} catch (TzSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	// 根据生日计算年纪
	public static int getAge(Date dateOfBirth) {
		int age = 0;
		Calendar born = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		if (dateOfBirth != null) {
			now.setTime(new Date());
			born.setTime(dateOfBirth);
			if (born.after(now)) {
				throw new IllegalArgumentException("年龄不能超过当前日期");
			}
			age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
			int nowDayOfYear = now.get(Calendar.DAY_OF_YEAR);
			int bornDayOfYear = born.get(Calendar.DAY_OF_YEAR);
			System.out.println("nowDayOfYear:" + nowDayOfYear + " bornDayOfYear:" + bornDayOfYear);
			if (nowDayOfYear < bornDayOfYear) {
				age -= 1;
			}
		}
		return age;
	}

}
