package com.tranzvision.gd.TZAutomaticTagBundle.service.impl;

import java.util.List;
import java.util.Map;

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
//			String zdbqQBSrcIdSql = "SELECT TZ_HARDCODE_VAL FROM  PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_ZDBQ_QBSRC_ID'";
//			String zdbqQBSrcId = SqlQuery.queryForObject(zdbqQBSrcIdSql, "String");
			String zdbqQBSrcId = labelId;
			if(zdbqQBSrcId != null && !"".equals(zdbqQBSrcId)){
				//删除该班级批次下人员的清华（北大）生源,重新计算;
				SqlQuery.update("delete from PS_TZ_CS_KSBQ_T where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_ZDBQ_ID=?",new Object[]{classId,batchId,zdbqQBSrcId});
			}
			
			String zdbqQBSrcNameSql = "SELECT TZ_BIAOQZ_NAME FROM  PS_TZ_BIAOQZ_BQ_T WHERE TZ_BIAOQ_ID=?";
			String zdbqQBSrcName = SqlQuery.queryForObject(zdbqQBSrcNameSql,new Object[] { zdbqQBSrcId },  "String");

			List<Map<String, Object>> tzappins = SqlQuery.queryForList(
					TzSQLObject.getSQLText("SQL.TZAutomaticTagBundle.TZAutomaticTagQhBdSrcServiceSql"),
					new Object[] { batchId,classId });
			if (tzappins != null && tzappins.size() > 0) {
				for (int i = 0; i < tzappins.size(); i++) {
					long appinsId = Long.valueOf(String.valueOf(tzappins.get(i).get("TZ_APP_INS_ID")));
					PsTzCsKsbqT PsTzCsKsbqT = new PsTzCsKsbqT();
					PsTzCsKsbqT.setTzAppInsId(appinsId);
					PsTzCsKsbqT.setTzClassId(classId);
					PsTzCsKsbqT.setTzApplyPcId(batchId);
					PsTzCsKsbqT.setTzZdbqId(zdbqQBSrcId);
					PsTzCsKsbqT.setTzBiaoqzName(zdbqQBSrcName);
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
