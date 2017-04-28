package com.tranzvision.gd.TZNegativeListInfeBundle.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service("com.tranzvision.gd.TZNegativeListInfeBundle.service.impl.TzNegativeApplyAgeOverServiceImpl")
public class TzNegativeApplyAgeOverServiceImpl extends TzNegativeListBundleServiceImpl {
	@Autowired
	private SqlQuery SqlQuery;
	@Autowired
	private TZGDObject TzSQLObject;
	@Autowired
	private PsTzCsKsFmTMapper PsTzCsKsFmTMapper;

	@Override
	public boolean makeNegativeList(String classId, String batchId, String labelId) {

		try {
			System.out.println("classid：" + classId + "batchId:" + batchId);
			// String OrgID =
			// tzLoginServiceImpl.getLoginedManagerOrgid(request);
			int have_one = 0;
			String hodecode = "SELECT TZ_HARDCODE_VAL FROM  PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_KSFMQDID_AGE'";
			String fmqdId = SqlQuery.queryForObject(hodecode, "String");

			List<Map<String, Object>> opridlist = SqlQuery.queryForList(
					TzSQLObject.getSQLText("SQL.TZNegativeListInfeBundle.TzNegativeApplyNumber"),
					new Object[] { batchId, classId });
			// System.out.println("opridlist.size:" + opridlist.size());
			if (opridlist != null && opridlist.size() > 0) {

				for (int i = 0; i < opridlist.size(); i++) {
					// System.out.println(opridlist.get(i).get("OPRID").toString());
					String sql = "SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID=? AND TZ_CLASS_ID=? ";
					Long appinsId = SqlQuery.queryForObject(sql,
							new Object[] { opridlist.get(i).get("OPRID").toString(), classId }, "Long");
					String sqlage = "SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE  TZ_APP_INS_ID=? AND TZ_XXX_BH='TZ_6birthday'";
					// 修改生日直接去报名表中的数据
					// String sqlage = "SELECT BIRTHDATE FROM PS_TZ_REG_USER_T
					// WHERE OPRID=?";

					/*
					 * int agename = this.getAge(SqlQuery.queryForObject(sqlage,
					 * new Object[] { opridlist.get(i).get("OPRID").toString()
					 * }, "Date"));
					 */

					int agename = this.getAge(SqlQuery.queryForObject(sqlage, new Object[] { appinsId }, "Date"));
					// System.out.println("agename:" + agename);
					if (agename > 45) {
						/*
						 * String sql =
						 * "SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID=? AND TZ_CLASS_ID=? "
						 * ; Long appinsId = SqlQuery.queryForObject(sql, new
						 * Object[] { opridlist.get(i).get("OPRID").toString(),
						 * classId }, "Long");
						 */
						PsTzCsKsFmT PsTzCsKsFmT = new PsTzCsKsFmT();
						// String fmqdId = "TZ_FMQ" +
						// String.valueOf(getSeqNum.getSeqNum("PS_TZ_CS_KSFM_T",
						// "TZ_FMQD_ID"));

						PsTzCsKsFmT.setTzAppInsId(appinsId);
						PsTzCsKsFmT.setTzClassId(classId);
						PsTzCsKsFmT.setTzApplyPcId(batchId);
						PsTzCsKsFmT.setTzFmqdId(fmqdId);
						PsTzCsKsFmT.setTzFmqdName("年龄大于45岁");
						have_one = SqlQuery.queryForObject(
								"SELECT COUNT(1) FROM PS_TZ_CS_KSFM_T WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? AND TZ_FMQD_ID=?",
								new Object[] { classId, batchId, appinsId, fmqdId }, "Integer");
						if (have_one > 0) {
							PsTzCsKsFmTMapper.updateByPrimaryKeySelective(PsTzCsKsFmT);

						} else {
							PsTzCsKsFmTMapper.insert(PsTzCsKsFmT);
						}

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
	public int getAge(Date dateOfBirth) {
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
			// System.out.println("nowDayOfYear:" + nowDayOfYear + "
			// bornDayOfYear:" + bornDayOfYear);
			if (nowDayOfYear < bornDayOfYear) {
				age -= 1;
			}
		}
		return age;
	}

}
