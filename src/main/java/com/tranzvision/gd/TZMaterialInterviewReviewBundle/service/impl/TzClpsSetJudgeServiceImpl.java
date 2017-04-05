package com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzClpskspwTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzCpPwKsTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzKsclpslsTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpskspwTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpskspwTblKey;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzCpPwKsTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzCpPwKsTblKey;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzKsclpslsTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzKsclpslsTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 材料评审考生名单-指定评委
 * @author LuYan
 * 2017-3-30
 *
 */
@Service("com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl.TzClpsSetJudgeServiceImpl")
public class TzClpsSetJudgeServiceImpl extends FrameworkImpl {
	
	@Autowired
	private TZGDObject tzSQLObject;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private PsTzCpPwKsTblMapper psTzCpPwKsTblMapper;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzKsclpslsTblMapper psTzKsclpslsTblMapper;
	@Autowired
	private PsTzClpskspwTblMapper psTzClpskspwTblMapper;
	
	

	/* 评委列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String strParams,int numLimit,int numStart,String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		Map<String, Object> mapRet = new HashMap<String,Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String,Object>>();
		mapRet.put("root", listData);

		try {
			
			jacksonUtil.json2Map(strParams);
			
			String classId = jacksonUtil.getString("classId");
			String batchId = jacksonUtil.getString("batchId");
			String appinsId = jacksonUtil.getString("appinsId");
			
			int pwNum = 0;
			
			String sql = "";
			sql = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialJudgeInfo");
			List<Map<String, Object>> listPw = sqlQuery.queryForList(sql, new Object[] {classId,batchId});
			
			for(Map<String, Object> mapPw : listPw) {
				
				pwNum++;
				
				String pwOprid = (String) mapPw.get("TZ_PWEI_OPRID");
				String dlzhId = (String) mapPw.get("TZ_DLZH_ID");
				String pwName = (String) mapPw.get("TZ_REALNAME");
				String pwzId = (String) mapPw.get("TZ_PWZBH");
				String pwzName = (String) mapPw.get("TZ_CLPS_GR_NAME");
				
				String selectFlag="";
				if(!"".equals(appinsId)) {
					String sqlPw = "SELECT COUNT(1) FROM PS_TZ_CP_PW_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? AND TZ_PWEI_OPRID=?";
					Integer num = sqlQuery.queryForObject(sqlPw, new Object[]{classId,batchId,appinsId,pwOprid},"Integer");
					if(num>0) {
						selectFlag="true";
					} else {
						selectFlag="false";
					}
				} else {
					selectFlag="false";
				}
					
				Map<String, Object> mapList = new HashMap<String,Object>();
				mapList.put("classId", classId);
				mapList.put("batchId", batchId);
				mapList.put("judgeOprid", pwOprid);
				mapList.put("judgeId", dlzhId);
				mapList.put("judgeName", pwName);
				mapList.put("judgeGroup", pwzName);
				mapList.put("selectFlag", selectFlag);
				listData.add(mapList);
			}
			
			mapRet.replace("total", pwNum);
			mapRet.replace("root", listData);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	public String tzOther(String operateType,String strParams,String[] errMsg) {
		String strRet="";

		try {
			//指定评委
			if("tzSetJudge".equals(operateType)) {
				strRet = setJudge(strParams,errMsg);
			}		
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	/* 指定评委 */
	public String setJudge(String strParams,String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			jacksonUtil.json2Map(strParams);
			
			String classId = jacksonUtil.getString("classId");
			String batchId = jacksonUtil.getString("batchId");
			List<?> appinsIdList = jacksonUtil.getList("appinsId");
			List<?> selectJudgeList = jacksonUtil.getList("selectJudge");
			List<?> NOselectJudgeIDList = jacksonUtil.getList("NOselectJudgeID");
			Integer clpsksNum = Integer.valueOf(jacksonUtil.getString("clpsksNum"));
			Short dqpsLunc = Short.valueOf(String.valueOf(jacksonUtil.getInt("dqpsLunc")));
			
			//当前登录人
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			String sql = "";
			
			for(Object appinsId : appinsIdList) {
				for(Object NOselectJudgeID : NOselectJudgeIDList) {
					sql = "DELETE FROM PS_TZ_CP_PW_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? AND TZ_PWEI_OPRID=?";
					sqlQuery.update(sql,new Object[]{classId,batchId,appinsId,NOselectJudgeID});
					
					sql = "DELETE FROM PS_TZ_KSCLPSLS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? AND TZ_PWEI_OPRID=?";
					sqlQuery.update(sql,new Object[]{classId,batchId,appinsId,NOselectJudgeID});
				}
				
				for(Object selectJudge : selectJudgeList) {
					//评委已达上限，则无法指定给考生
					sql = "SELECT COUNT(1) FROM PS_TZ_CP_PW_KS_TBL A,PS_TZ_CLPS_KSH_TBL B ";
					sql += " WHERE A.TZ_CLASS_ID=B.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID AND A.TZ_APP_INS_ID=B.TZ_APP_INS_ID";
					sql += " AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_PWEI_OPRID=?";
					
					Integer pwksnum = sqlQuery.queryForObject(sql, new Object[]{classId,batchId,selectJudge},"Integer");
					
					if(pwksnum>=clpsksNum) {
						
					} else {
						
						PsTzCpPwKsTblKey psTzCpPwKsTblKey = new PsTzCpPwKsTblKey();
						psTzCpPwKsTblKey.setTzClassId(classId);
						psTzCpPwKsTblKey.setTzApplyPcId(batchId);
						psTzCpPwKsTblKey.setTzAppInsId(Long.valueOf(String.valueOf(appinsId)));
						psTzCpPwKsTblKey.setTzPweiOprid(String.valueOf(selectJudge));
						
						PsTzCpPwKsTbl psTzCpPwKsTbl = psTzCpPwKsTblMapper.selectByPrimaryKey(psTzCpPwKsTblKey);
						
						if(psTzCpPwKsTbl==null) {
							psTzCpPwKsTbl = new PsTzCpPwKsTbl();
							psTzCpPwKsTbl.setTzClassId(classId);
							psTzCpPwKsTbl.setTzApplyPcId(batchId);
							psTzCpPwKsTbl.setTzAppInsId(Long.valueOf(String.valueOf(appinsId)));
							psTzCpPwKsTbl.setTzPweiOprid(String.valueOf(selectJudge));
							psTzCpPwKsTbl.setRowAddedDttm(new Date());
							psTzCpPwKsTbl.setRowAddedOprid(oprid);
							psTzCpPwKsTbl.setRowLastmantDttm(new Date());
							psTzCpPwKsTbl.setRowLastmantOprid(oprid);
							psTzCpPwKsTblMapper.insertSelective(psTzCpPwKsTbl);		
						} else {
							psTzCpPwKsTbl.setRowLastmantDttm(new Date());
							psTzCpPwKsTbl.setRowLastmantOprid(oprid);
							psTzCpPwKsTblMapper.updateByPrimaryKey(psTzCpPwKsTbl);
						}
						
						
						PsTzKsclpslsTblKey psTzKsclpslsTblKey = new PsTzKsclpslsTblKey();
						psTzKsclpslsTblKey.setTzClassId(classId);
						psTzKsclpslsTblKey.setTzApplyPcId(batchId);
						psTzKsclpslsTblKey.setTzAppInsId(Long.valueOf(String.valueOf(appinsId)));
						psTzKsclpslsTblKey.setTzPweiOprid(String.valueOf(selectJudge));
						psTzKsclpslsTblKey.setTzClpsLunc(dqpsLunc);
						
						PsTzKsclpslsTbl psTzKsclpslsTbl = psTzKsclpslsTblMapper.selectByPrimaryKey(psTzKsclpslsTblKey);
						
						if(psTzKsclpslsTbl==null) {
							psTzKsclpslsTbl = new PsTzKsclpslsTbl();
							psTzKsclpslsTbl.setTzClassId(classId);
							psTzKsclpslsTbl.setTzApplyPcId(batchId);
							psTzKsclpslsTbl.setTzAppInsId(Long.valueOf(String.valueOf(appinsId)));
							psTzKsclpslsTbl.setTzPweiOprid(String.valueOf(selectJudge));
							psTzKsclpslsTbl.setTzClpsLunc(dqpsLunc);
							psTzKsclpslsTbl.setTzSubmitYn("U");
							psTzKsclpslsTbl.setRowAddedDttm(new Date());
							psTzKsclpslsTbl.setRowAddedOprid(oprid);
							psTzKsclpslsTbl.setRowLastmantDttm(new Date());
							psTzKsclpslsTbl.setRowLastmantOprid(oprid);
							psTzKsclpslsTblMapper.insertSelective(psTzKsclpslsTbl);		
						} else {
							psTzKsclpslsTbl.setTzSubmitYn("U");
							psTzKsclpslsTbl.setRowLastmantDttm(new Date());
							psTzKsclpslsTbl.setRowLastmantOprid(oprid);
							psTzKsclpslsTblMapper.updateByPrimaryKey(psTzKsclpslsTbl);	
						}
						
						String pweiOpridDesc = "";
						sql = "SELECT TZ_PWEI_OPRID FROM PS_TZ_CP_PW_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_DIRC_ID=? AND TZ_APP_INS_ID=?";
						List<Map<String, Object>> ksPwList = sqlQuery.queryForList(sql,new Object[]{classId,batchId,appinsId});
						for(Map<String,Object> ksPwMap : ksPwList) {
							String pweiOprid = (String) ksPwMap.get("TZ_PWEI_OPRID");
							if(!"".equals(pweiOpridDesc)) {
								pweiOpridDesc += "," + pweiOprid;
							} else {
								pweiOpridDesc = pweiOprid;
							}
						}
						
						PsTzClpskspwTblKey psTzClpskspwTblKey = new PsTzClpskspwTblKey();
						psTzClpskspwTblKey.setTzClassId(classId);
						psTzClpskspwTblKey.setTzApplyPcId(batchId);
						psTzClpskspwTblKey.setTzAppInsId(Long.valueOf(String.valueOf(appinsId)));
						psTzClpskspwTblKey.setTzClpsLunc(dqpsLunc);
						
						PsTzClpskspwTbl psTzClpskspwTbl = psTzClpskspwTblMapper.selectByPrimaryKey(psTzClpskspwTblKey);
						
						if(psTzClpskspwTbl==null) {
							psTzClpskspwTbl = new PsTzClpskspwTbl();
							psTzClpskspwTbl.setTzClassId(classId);
							psTzClpskspwTbl.setTzApplyPcId(batchId);
							psTzClpskspwTbl.setTzAppInsId(Long.valueOf(String.valueOf(appinsId)));
							psTzClpskspwTblKey.setTzClpsLunc(dqpsLunc);
							psTzClpskspwTbl.setTzClpwList(pweiOpridDesc);
							psTzClpskspwTbl.setRowAddedDttm(new Date());
							psTzClpskspwTbl.setRowAddedOprid(oprid);
							psTzClpskspwTbl.setRowLastmantDttm(new Date());
							psTzClpskspwTbl.setRowLastmantOprid(oprid);
							psTzClpskspwTblMapper.insert(psTzClpskspwTbl);
						} else {
							psTzClpskspwTbl.setTzClpwList(pweiOpridDesc);
							psTzClpskspwTbl.setRowLastmantDttm(new Date());
							psTzClpskspwTbl.setRowLastmantOprid(oprid);
							psTzClpskspwTblMapper.updateByPrimaryKey(psTzClpskspwTbl);
						}
						
					}
					
				}
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
}
