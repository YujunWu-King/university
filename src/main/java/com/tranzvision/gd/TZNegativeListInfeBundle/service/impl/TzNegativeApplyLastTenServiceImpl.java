
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
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 后百分之10
 * 
 * @author tzhjl
 * @since 2017-02-09
 */
@Service("com.tranzvision.gd.TZNegativeListInfeBundle.service.impl.TzNegativeApplyLastTenServiceImpl")
public class TzNegativeApplyLastTenServiceImpl extends TzNegativeListBundleServiceImpl {
	@Autowired
	private SqlQuery SqlQuery;
	@Autowired
	private PsTzCsKsFmTMapper PsTzCsKsFmTMapper;

	@Autowired
	private PsTzCsKsTblMapper PsTzCsKsTBLMapper;

	@Override
	public boolean makeNegativeList(String classId, String batchId, String labelId) {
		try {

			Date nowdate_time = new Date();
			int frisone_pm;
			int lastone_pm;
			int have_one = 0;
			String hodecode = "SELECT TZ_HARDCODE_VAL FROM  PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_KSFMQDID_LASTTEN'";
			String fmqdId = SqlQuery.queryForObject(hodecode, "String");
			int a = 0;

			String sqlcount = "SELECT COUNT(1) FROM PS_TZ_CS_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? ";
			int count = SqlQuery.queryForObject(sqlcount, new Object[] { classId, batchId }, "Integer");
			count = (int) (count * 0.1);
			if (count > 0) {
				System.out.println("count:" + count);
				String sqlfristone = "SELECT TZ_KSH_PSPM FROM PS_TZ_CS_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? ORDER BY TZ_KSH_PSPM DESC LIMIT ?,1";

				String sqllastone = "SELECT TZ_KSH_PSPM FROM PS_TZ_CS_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? ORDER BY TZ_KSH_PSPM DESC LIMIT ?,1";
				/**
				 * 1.a=0; 2.循环比较最后百分之10的第a个和前百分之90的最后一个， 3.如果相同 4.a++
				 * 5.就在比较最后百分之10的第二个前百分之90的最后一个 循环
				 ***/

				frisone_pm = SqlQuery.queryForObject(sqlfristone, new Object[] { classId, batchId, count - 1 - a },
						"Integer");
				lastone_pm = SqlQuery.queryForObject(sqllastone, new Object[] { classId, batchId, count }, "Integer");

				while (frisone_pm >= lastone_pm) {
					a++;
					frisone_pm = SqlQuery.queryForObject(sqlfristone, new Object[] { classId, batchId, count - 1 - a },
							"Integer");

				}
				String sqltzappint = "SELECT TZ_APP_INS_ID FROM PS_TZ_CS_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? ORDER BY TZ_KSH_PSPM DESC LIMIT ?  ";
				List<Map<String, Object>> tzappintlist = SqlQuery.queryForList(sqltzappint,
						new Object[] { classId, batchId, count - a });

				if (tzappintlist != null && tzappintlist.size() > 0) {
					for (int i = 0; i < tzappintlist.size(); i++) {
						PsTzCsKsFmT PsTzCsKsFmT = new PsTzCsKsFmT();
						// String fmqdId = "TZ_FMQ" +
						// String.valueOf(getSeqNum.getSeqNum("PS_TZ_CS_KSFM_T",
						// "TZ_FMQD_ID"));
						PsTzCsKsFmT.setTzAppInsId(Long.valueOf(tzappintlist.get(i).get("TZ_APP_INS_ID").toString()));
						PsTzCsKsFmT.setTzClassId(classId);
						PsTzCsKsFmT.setTzApplyPcId(batchId);
						PsTzCsKsFmT.setTzFmqdId(fmqdId);
						PsTzCsKsFmT.setTzFmqdName("排名后百分之十");
						have_one = SqlQuery.queryForObject(
								"SELECT COUNT(1) FROM PS_TZ_CS_KSFM_T WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? AND TZ_FMQD_ID=?",
								new Object[] { classId, batchId,
										Long.valueOf(tzappintlist.get(i).get("TZ_APP_INS_ID").toString()), fmqdId },
								"Integer");
						if (have_one > 0) {

						} else {
							PsTzCsKsFmTMapper.insert(PsTzCsKsFmT);
						}

						PsTzCsKsTbl PsTzCsKsTBL = new PsTzCsKsTbl();

						PsTzCsKsTBL.setTzAppInsId(Long.valueOf(tzappintlist.get(i).get("TZ_APP_INS_ID").toString()));
						PsTzCsKsTBL.setTzClassId(classId);
						PsTzCsKsTBL.setTzApplyPcId(batchId);
						// PsTzCsKsTBL.setTzJgId(OrgID);
						PsTzCsKsTBL.setRowLastmantDttm(nowdate_time);
						PsTzCsKsTBL.setTzKshCsjg("N");
						PsTzCsKsTBLMapper.updateByPrimaryKeySelective(PsTzCsKsTBL);
					}
				}
			} else {

			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// TODO: handle exception
		}

		// TODO Auto-generated method stub
		return true;
	}

}
