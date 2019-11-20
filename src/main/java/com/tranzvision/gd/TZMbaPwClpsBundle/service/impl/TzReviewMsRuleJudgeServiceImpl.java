package com.tranzvision.gd.TZMbaPwClpsBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzMsPsPwTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzMsPsPwTbl;
import com.tranzvision.gd.TZMbaPwMspsBundle.dao.psTzMspwpsjlTblMapper;
import com.tranzvision.gd.TZMbaPwMspsBundle.model.psTzMspwpsjlTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * MBA材料面试评审-面试规则-添加评委页
 * 
 * @author tzhjl
 * @since 2017-03-13
 */

@Service("com.tranzvision.gd.TZMbaPwClpsBundle.service.impl.TzReviewMsRuleJudgeServiceImpl")
public class TzReviewMsRuleJudgeServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private PsTzMsPsPwTblMapper psTzMsPsPwTblMapper;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private psTzMspwpsjlTblMapper psTzMspwpsjlTblMapper;

	/**
	 * 面试评审修改
	 * 
	 * @author dingpeng
	 * @since 2019年11月19日14:27:54
	 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", "");
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};

			// json数据要的结果字段;
						String[] resultFldArray;

						//面试管理员
						if(comParams.indexOf("TZ_MSGLY_REL_VW")>0){
							resultFldArray = "TZ_JG_ID,OPRID,TZ_REALNAME,TZ_DLZH_ID".split(",");
						}else{
							resultFldArray = "TZ_JG_ID,OPRID,TZ_REALNAME,TZ_DLZH_ID".split(",");
						}

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {

					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					if (comParams.indexOf("TZ_MSGLY_REL_VW")>0) {
						mapList.put("glyId", rowList[1]);
						mapList.put("glyName", rowList[2]);
						mapList.put("glyZhxx", rowList[3]);
					} else {
						mapList.put("judgId", rowList[1]);
						mapList.put("judzhxx", rowList[3]);
						mapList.put("judgName", rowList[2]);
						mapList.put("judgGroupId", "");
					}
//					if (rowList[0].equals("SEM")) {
//						mapList.put("judgId", rowList[1]);
//						mapList.put("judzhxx", rowList[3]);
//						mapList.put("judgName", rowList[2]);
//						mapList.put("judgGroupId", "");

//					} else {
//
//					}

					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);

	}
	
	/**
	 * 面试评审添加管理员和面试组迁移
	 * 
	 * @author dingpeng
	 * @since 2019年11月19日14:28:44
	 */
	
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		JSONObject params = JSONObject.fromObject(strParams);
		JSONObject result = new JSONObject();

		switch (oprType) {
		/*获取面试管理员*/
		case "GETGLY":
			String CLASSID = params.getString("CLASSID");
			String PCID = params.getString("PCID");
			List<Map<String,Object>> queryForList = new ArrayList<Map<String,Object>>();
			JSONArray jsonA = new JSONArray();
			result.put("total", "0");
			result.put("root", jsonA);
			try {
				queryForList = sqlQuery.queryForList("SELECT "
						+ "a.oprid,TZ_DLZH_id,b.TZ_realname,a.type,a.status "
						+ "FROM tz_interview_admin_t a "
						+ "INNER JOIN PS_TZ_AQ_YHXX_TBL b ON a.oprid = b.oprid "
						+ "WHERE TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ?", new Object[]{CLASSID,PCID});
				if(queryForList!=null&&queryForList.size()>0){
					queryForList.forEach(item->{
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("glyId", item.get("oprid")!=null?item.get("oprid").toString():"");
						map.put("glyZh", item.get("TZ_DLZH_id")!=null?item.get("TZ_DLZH_id").toString():"");
						map.put("glyName", item.get("TZ_realname")!=null?item.get("TZ_realname").toString():"");
						map.put("glyLb", item.get("type")!=null?item.get("type").toString():"");
						map.put("glyState", item.get("status")!=null?item.get("status").toString():"");
						jsonA.add(map);
					});
				}
				result.replace("total", jsonA.size());
				result.replace("root", jsonA);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			/*删除面试管理员*/
		case "deleteGly":
			String judgId = params.getString("judgId");
			String pcid = params.getString("PCID");
			String classId = params.getString("CLASSID");
			try {
				sqlQuery.update("delete from tz_interview_admin_t where tz_apply_pc_id = ? and oprid=? and " +
						"tz_class_id=?", new Object[]{pcid,judgId,classId});
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			/*获取面试组数据*/
		case "MSZLIST":
			try {

				String CLASSID1 = params.getString("CLASSID");
				String PCID1 = params.getString("PCID");
				List<Map<String,Object>> queryForList2 = sqlQuery.queryForList("SELECT "
						+ "TZ_GROUP_ID as mszid,"
						+ "TZ_GROUP_SPACE as mszsd,"
						+ "DATE_FORMAT(TZ_START_DTTM, '%T') AS mszkssj,"
						+ "DATE_FORMAT(TZ_END_DTTM, '%T') AS mszjssj,"
						+ "TZ_GROUP_DESC as mszmj "
						+ "FROM TZ_INTERVIEW_GROUP "
						+ "WHERE TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ?", new Object[]{CLASSID1,PCID1});
				List<Map<String, Object>> orElseGet = null;
				if(null!=queryForList2&&queryForList2.size()>0){
					orElseGet = queryForList2;
					System.out.println("true");
				}else{
					/*如果没有面试组自动生成面试组 上下午*/
					List<Map<String,Object>> returnList = new ArrayList<>();
					Map<String, Object> mapA = new HashMap<>();
					mapA.put("mszsd", "A");//上午
					mapA.put("mszkssj", "");//上午开始时间
					mapA.put("mszjssj", "");//上午
					mapA.put("mszid", "null");//上午
					Map<String, Object> mapB = new HashMap<>();
					mapB.put("mszsd", "B");//下午
					mapB.put("mszkssj", "");//下午开始时间
					mapB.put("mszjssj", "");//下午结束时间
					mapB.put("mszid", "null");//下午结束时间
					returnList.add(mapA);
					returnList.add(mapB);
					orElseGet = returnList;
					System.out.println("false");
				}
				System.out.println(orElseGet.size());
				result.put("total", orElseGet.size());
				result.put("root", orElseGet);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case "deleteMsZ":
			String CLASSID1 = params.getString("classId");
			String PCID1 = params.getString("batchId");
			String MSZID = params.getString("mszId");
			sqlQuery.update("delete from TZ_INTERVIEW_GROUP WHERE TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ? and TZ_GROUP_ID = ?", new Object[]{CLASSID1,PCID1,MSZID});
			break;
		default:
			break;
		}
		System.out.println(result.toString());
		return result.toString();
	}
	/**
	 * 添加评委修改迁移
	 * 
	 * @author dingpeng
	 * @since 2019年11月19日14:28:44
	 */
	// 添加评委
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		Date nowdate = new Date();

		String judgId = "";
		String judgGroupId = "";
		String judgName = "";

		String ksName = "";
		String jugeNameList = "";

		String judgType = "";
		String groupleader = "";

		String sql1 = "";
		int count = 0;
		int count1 = 0;
		try {
			String Oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			jacksonUtil.json2Map(actData[0]);
			String classId = jacksonUtil.getString("classId");
			jacksonUtil.json2Map(actData[1]);
			String batchId = jacksonUtil.getString("batchId");
			jacksonUtil.json2Map(actData[2]);
			String type = jacksonUtil.getString("type");
			for (int i = 3; i < actData.length; i++) {
				// 表单内容
				String strForm = actData[i];
				
				
				// 解析 json
				jacksonUtil.json2Map(strForm);
				if ("PW".equals(type)) {
				judgId = jacksonUtil.getString("judgId");
				
				judgGroupId = jacksonUtil.getString("judgGroupId");
				judgName = jacksonUtil.getString("judgName");
				judgType = jacksonUtil.getString("judgType");
				
				// 评委类型A是英语评委，B是其他评委
				if (judgType != null && judgType.equals("Y")) {
					judgType = "A";
				} else {
					judgType = "B";
				}
				groupleader = jacksonUtil.getString("groupleader");
				
				if (groupleader!=null && groupleader.equals("true")) {
					groupleader = "Y";
				} else {
					groupleader = "N";
				}

				String sql = "SELECT COUNT(1) from PS_TZ_MSPS_PW_TBL where TZ_CLASS_ID =? and TZ_APPLY_PC_ID =? and TZ_PWEI_OPRID=?";
				count = sqlQuery.queryForObject(sql, new Object[] { classId, batchId, judgId }, "Integer");
				if (count > 0) {
					jugeNameList = jugeNameList + judgName + ",";

				} else {
					PsTzMsPsPwTbl psTzMsPsPwTbl = new PsTzMsPsPwTbl();
					psTzMsPsPwTbl.setTzClassId(classId);
					psTzMsPsPwTbl.setTzApplyPcId(batchId);
					psTzMsPsPwTbl.setTzPweiOprid(judgId);
					psTzMsPsPwTbl.setTzPweiGrpid(judgGroupId);
					psTzMsPsPwTbl.setTzPweiZhzt("A");
					psTzMsPsPwTbl.setRowAddedDttm(nowdate);
					psTzMsPsPwTbl.setRowAddedOprid(Oprid);
					psTzMsPsPwTbl.setRowLastmantDttm(nowdate);
					psTzMsPsPwTbl.setRowLastmantOprid(Oprid);
					psTzMsPsPwTbl.setTzPweiType(judgType);
					psTzMsPsPwTbl.setTzGroupLeader(groupleader);
					psTzMsPsPwTblMapper.insertSelective(psTzMsPsPwTbl);

				}
				sql1 = "SELECT COUNT(1) FROM PS_TZ_MSPWPSJL_TBL WHERE TZ_CLASS_ID =? and TZ_APPLY_PC_ID =? and TZ_PWEI_OPRID=?";
				count1 = sqlQuery.queryForObject(sql1, new Object[] { classId, batchId, judgId }, "Integer");

				if (count1 > 0) {

				} else {

					psTzMspwpsjlTbl psTzMspwpsjlTbl = new psTzMspwpsjlTbl();
					psTzMspwpsjlTbl.setTzClassId(classId);
					psTzMspwpsjlTbl.setTzApplyPcId(batchId);
					psTzMspwpsjlTbl.setTzPweiOprid(judgId);
					psTzMspwpsjlTbl.setTzSubmitYn("N");
					psTzMspwpsjlTbl.setRowAddedDttm(nowdate);
					psTzMspwpsjlTbl.setRowAddedOprid(Oprid);
					psTzMspwpsjlTbl.setRowLastmantDttm(nowdate);
					psTzMspwpsjlTbl.setRowLastmantOprid(Oprid);
					psTzMspwpsjlTblMapper.insertSelective(psTzMspwpsjlTbl);

				}

			
			if (!"".equals(jugeNameList)) {
				errMsg[0] = "1";

				errMsg[1] = "评委:" + jugeNameList + "已经存在于评委列表";

			}
				}else{
					judgId = jacksonUtil.getString("glyId");
					judgGroupId = jacksonUtil.getString("glyZhxx");
					judgName = jacksonUtil.getString("glyName");

					String sql = "SELECT COUNT(1) from tz_interview_admin_t where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID = ? and OPRID = ?";
					count = sqlQuery.queryForObject(sql, new Object[] { classId, batchId, judgId }, "Integer");
					if (count > 0) {
						jugeNameList = jugeNameList + judgName + ",";

					} else {
						sqlQuery.update("insert into tz_interview_admin_t (tz_class_id,tz_apply_pc_id,oprid,type,status,added_dttm,added_oprid,lastmant_dttm,lastmant_oprid)"
								+ " values (?,?,?,'','A',NOW(),?,NOW(),?)", new Object[]{classId,batchId,judgId,Oprid,Oprid});
					}

					if (!"".equals(jugeNameList)) {
						errMsg[0] = "1";

						errMsg[1] = "管理员:" + jugeNameList + "已经存在于管理员列表";

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();

			// TODO: handle exception
		}

		return null;
	}

	// 修改评委信息
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		Date nowdate = new Date();

		String judgId = "";
		String judgGroupId = "";
		String judgName = "";
		String judgState = "";
		String ksName = "";
		String judgType = "";
		String groupleader = "";
		
		int count = 0;
		try {
			String Oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			jacksonUtil.json2Map(actData[0]);
			String classId = jacksonUtil.getString("classId");
			jacksonUtil.json2Map(actData[1]);
			String batchId = jacksonUtil.getString("batchId");
			for (int i = 2; i < actData.length; i++) {
				// 表单内容
				String strForm = actData[i];
				// 解析 json
				jacksonUtil.json2Map(strForm);
				judgId = jacksonUtil.getString("judgId");
				judgGroupId = jacksonUtil.getString("judgGroupId");
				judgName = jacksonUtil.getString("judgName");
				judgState = jacksonUtil.getString("judgState");
				
				judgType = jacksonUtil.getString("judgType");
				
				// 评委类型A是英语评委，B是其他评委
				if (judgType != null && judgType.equals("true")) {
					judgType = "A";
				} else {
					judgType = "B";
				}
				groupleader = jacksonUtil.getString("groupleader");
				
				if (groupleader!=null && groupleader.equals("true")) {
					groupleader = "Y";
				} else {
					groupleader = "N";
				}

				String sql = "SELECT COUNT(1) from PS_TZ_MSPS_PW_TBL where TZ_CLASS_ID =? and TZ_APPLY_PC_ID =? and TZ_PWEI_OPRID=?";
				count = sqlQuery.queryForObject(sql, new Object[] { classId, batchId, judgId }, "Integer");
				if (count > 0) {
					PsTzMsPsPwTbl psTzMsPsPwTbl = new PsTzMsPsPwTbl();
					psTzMsPsPwTbl.setTzClassId(classId);
					psTzMsPsPwTbl.setTzApplyPcId(batchId);
					psTzMsPsPwTbl.setTzPweiOprid(judgId);
					psTzMsPsPwTbl.setTzPweiGrpid(judgGroupId);
					psTzMsPsPwTbl.setTzPweiZhzt(judgState);
					psTzMsPsPwTbl.setRowLastmantDttm(nowdate);
					psTzMsPsPwTbl.setRowLastmantOprid(Oprid);
					psTzMsPsPwTbl.setTzPweiType(judgType);
					psTzMsPsPwTbl.setTzGroupLeader(groupleader);
					psTzMsPsPwTblMapper.updateByPrimaryKeySelective(psTzMsPsPwTbl);

					sqlQuery.update(
							"UPDATE  PS_TZ_MP_PW_KS_TBL SET  TZ_PSHEN_ZT=? WHERE TZ_APPLY_PC_ID=? AND TZ_CLASS_ID=? AND TZ_PWEI_OPRID=?",
							new Object[] { judgState, batchId, classId, judgId });

				} else {
					errMsg[0] = "1";
					errMsg[1] = "评委:" + judgName + "不已存在，无法修改！";

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();

			// TODO: handle exception
		}

		return null;
	}

	// 修改评委信息
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		Date nowdate = new Date();

		String judgId = "";
		String judgGroupId = "";
		String judgName = "";
		String judgState = "";
		String ksName = "";
		int count = 0;
		String sql1 = "";
		int count1 = 0;
		try {
			String Oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			jacksonUtil.json2Map(actData[0]);
			String classId = jacksonUtil.getString("classId");
			jacksonUtil.json2Map(actData[1]);
			String batchId = jacksonUtil.getString("batchId");
			for (int i = 2; i < actData.length; i++) {
				// 表单内容
				String strForm = actData[i];
				// 解析 json
				jacksonUtil.json2Map(strForm);
				judgId = jacksonUtil.getString("judgId");
				judgGroupId = jacksonUtil.getString("judgGroupId");
				judgName = jacksonUtil.getString("judgName");
				judgState = jacksonUtil.getString("judgState");

				// System.out.println("classId:" + classId + "batchId:" +
				// batchId + "judgId:" + judgId);

				String sql = "SELECT COUNT(1) from PS_TZ_MSPS_PW_TBL where TZ_CLASS_ID =? and TZ_APPLY_PC_ID =? and TZ_PWEI_OPRID=?";
				count = sqlQuery.queryForObject(sql, new Object[] { classId, batchId, judgId }, "Integer");
				if (count > 0) {
					PsTzMsPsPwTbl psTzMsPsPwTbl = new PsTzMsPsPwTbl();
					psTzMsPsPwTbl.setTzClassId(classId);
					psTzMsPsPwTbl.setTzApplyPcId(batchId);
					psTzMsPsPwTbl.setTzPweiOprid(judgId);
					psTzMsPsPwTblMapper.deleteByPrimaryKey(psTzMsPsPwTbl);
					sqlQuery.update(
							"UPDATE  PS_TZ_MP_PW_KS_TBL SET  TZ_DELETE_ZT='Y' WHERE TZ_APPLY_PC_ID=? AND TZ_CLASS_ID=? AND TZ_PWEI_OPRID=?",
							new Object[] { batchId, classId, judgId });

				} else {
					errMsg[0] = "1";
					errMsg[1] = "评委:" + judgName + "不已存在，无法删除！";

				}

				sql1 = "SELECT COUNT(1) FROM PS_TZ_MSPWPSJL_TBL WHERE TZ_CLASS_ID =? and TZ_APPLY_PC_ID =? and TZ_PWEI_OPRID=?";
				count1 = sqlQuery.queryForObject(sql1, new Object[] { classId, batchId, judgId }, "Integer");
				if (count1 > 0) {
					psTzMspwpsjlTbl psTzMspwpsjlTbl = new psTzMspwpsjlTbl();
					psTzMspwpsjlTbl.setTzClassId(classId);
					psTzMspwpsjlTbl.setTzApplyPcId(batchId);
					psTzMspwpsjlTbl.setTzPweiOprid(judgId);
					psTzMspwpsjlTblMapper.deleteByPrimaryKey(psTzMspwpsjlTbl);

				} else {

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();

			// TODO: handle exception
		}

		return null;
	}

}
