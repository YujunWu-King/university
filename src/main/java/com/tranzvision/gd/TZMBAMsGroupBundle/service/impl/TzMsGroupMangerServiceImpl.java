package com.tranzvision.gd.TZMBAMsGroupBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.TZMBAMsGroupBundle.service.impl.TzMsGroupMangerServiceImpl")
public class TzMsGroupMangerServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private GetSeqNum getSeqNum;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private TZGDObject tzSQLObject;

	// 添加考生
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String, Object> mapRet = new HashMap<String, Object>();
		Date nowdate = new Date();
		//String appinsId = "";
		//String classId = "";
		//String batchId = "";
		
		Map<String, Object> data = null;

		//String strReturn = "";
		//String name = "";
		String check = "";
		String gropid = "";
		int sum = 0;
		String strnum;
		//int intis = 0;
		StringBuilder errorMsg = new StringBuilder("学生:");
		List<Map<String,Object>> queryForList = new ArrayList<Map<String,Object>>();
		//String sql = "update PS_TZ_MSPS_KSH_TBL set TZ_GROUP_ID=?,TZ_GROUP_DATE=now(),TZ_ORDER=? where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?";
		String sql = "UPDATE PS_TZ_MSPS_KSH_TBL SET TZ_PS_GR_ID = ?, TZ_GROUP_DATE = NOW(), TZ_ORDER = ?, TZ_GROUP_ID = ? WHERE TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ? AND TZ_APP_INS_ID = ?";
		//String totole = "select max(TZ_ORDER) from PS_TZ_MSPS_KSH_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=?  and TZ_GROUP_ID=?";
		String totole = "select max(TZ_ORDER) from PS_TZ_MSPS_KSH_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=?  and TZ_PS_GR_ID=? and TZ_GROUP_ID = ?";
		//String is = "select count(1) from PS_TZ_MSPS_KSH_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=? and TZ_GROUP_ID=?";
		String is = "select count(1) from PS_TZ_MSPS_KSH_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=? and TZ_PS_GR_ID=? and TZ_GROUP_ID = ?";
		//String updatesql = "update PS_TZ_INTEGROUP_T set TZ_GROUP_NAME=? where TZ_GROUP_ID=?";
		try {
			String[] appinsIds = null;
			for (int i = 0; i < actData.length; i++) {
				// 表单内容
				String strForm = actData[i];
				// System.out.println(strForm);
				jacksonUtil.json2Map(strForm);
				// 解析 json
				String classId = jacksonUtil.getString("classId");
				String batchId = jacksonUtil.getString("batchId");
				String appinsId = jacksonUtil.getString("appinsId");
				String jugGroupId = jacksonUtil.getString("jugGroupId");
				System.out.println("appinsId:" + appinsId);

				data = jacksonUtil.getMap("data");
				check = data.get("check") == null ? "" : data.get("check").toString();
				//name = data.get("groupName") == null ? "" : data.get("groupName").toString();
				gropid = data.get("groupID").toString();
				//System.out.println(check);
				
				//查询是否有评委
				queryForList = sqlQuery.queryForList("SELECT A.TZ_PWEI_OPRID, A.TZ_PWEI_GRPID FROM PS_TZ_AQ_YHXX_TBL D INNER JOIN PS_TZ_MSPS_PW_TBL A ON A.TZ_PWEI_OPRID = D.OPRID WHERE A.TZ_PWEI_ZHZT = 'A' AND A.TZ_CLASS_ID = ? AND A.TZ_APPLY_PC_ID = ?", new Object[]{classId,batchId});

				//没有评委终跳出
				if(null==queryForList||queryForList.size()<=0){
					break;
				} else{
					queryForList = queryForList
							.stream()
							.filter(item->{
								if(item.get("TZ_PWEI_GRPID")==null)return false;
								String[] split = item.get("TZ_PWEI_GRPID").toString().split(",");
								for (String s : split) {
									if(s.equals(jugGroupId))return true;
								}
								return false;
							})
							.collect(Collectors.toList());
				}
				//sqlQuery.update(updatesql, new Object[] { name, gropid });
				appinsIds = appinsId.split(",");
				if (check.equals("true") || check.equals("Y")) {
				String Oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				for(String ins : appinsIds) {


					String flag = sqlQuery.queryForObject("SELECT DISTINCT (SELECT c.TZ_REALNAME AS sName FROM PS_TZ_FORM_WRK_T d,PS_TZ_REG_USER_T c WHERE c.OPRID = d.OPRID AND d.TZ_APP_INS_ID = a.TZ_APP_INS_ID) as SNAME FROM PS_TZ_MP_PW_KS_TBL a where a.TZ_CLASS_ID = ? AND a.TZ_APPLY_PC_ID = ? AND a.TZ_APP_INS_ID = ? and a.TZ_SCORE_INS_ID <> 0", new Object[]{classId,batchId,ins}, "String");

					//String flag = sqlQuery.queryForObject("SELECT 'Y' FROM PS_TZ_MSPS_KSH_TBL WHERE TZ_CHECKIN_DTTM IS NOT NULL AND TZ_SIGNATURE IS NOT NULL AND TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ? AND TZ_APP_INS_ID = ?", new Object[]{classId,batchId,ins}, "String");
					//判断学生是否已经被打分
					if(null!=flag&&!"".equals(flag)){
						//已经打分
						errorMsg.append(flag+",");
					}else{
						strnum = sqlQuery.queryForObject(totole, new Object[] { classId, batchId,jugGroupId ,gropid}, "String");
						if (null==strnum|| strnum.equals("")) {
							sum = 0;
						} else {
							try {
								sum = Integer.parseInt(strnum);
							} catch (Exception e) {
								sum = 0;
							}
						}
						Integer intis = sqlQuery.queryForObject(is, new Object[] { classId, batchId, ins,jugGroupId, gropid},
								"Integer");
						// 存在相同的数据 不修改
						if (intis <= 0) {
							sum = sum + 1;
							sqlQuery.update(sql, new Object[] { jugGroupId, sum,gropid, classId, batchId, ins});
							//考生与评委建立关系-----改在签到时建立
							sqlQuery.update("delete from PS_TZ_MP_PW_KS_TBL where TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ? AND TZ_APP_INS_ID = ? and TZ_SCORE_INS_ID = '0'", new Object[]{classId,batchId,ins});
							queryForList.forEach((item)->{
								String TZ_PWEI_OPRID = item.get("TZ_PWEI_OPRID").toString();
								sqlQuery.update("insert into PS_TZ_MP_PW_KS_TBL ("
										+ "TZ_APPLY_PC_ID,"
										+ "TZ_CLASS_ID,"
										+ "TZ_APP_INS_ID,"
										+ "TZ_PWEI_OPRID,"
										+ "ROW_ADDED_DTTM,"
										+ "ROW_ADDED_OPRID,"
										+ "ROW_LASTMANT_DTTM,"
										+ "ROW_LASTMANT_OPRID,"
										+ "TZ_SCORE_INS_ID,"
										+ "TZ_DELETE_ZT,"
										+ "TZ_PSHEN_ZT)"
										+ " values ("
										+ "?,"
										+ "?,"
										+ "?,"
										+ "?,"
										+ "NOW(),"
										+ "?,"
										+ "NOW(),"
										+ "?,"
										+ "'0',"
										+ "'N',"
										+ "'N')", new Object[]{batchId,classId,ins,TZ_PWEI_OPRID,Oprid,Oprid});
							});
						}
					}
				
				}
//					if (appinsId.indexOf(",") != -1) {
//						appinsIds = appinsId.split(",");
//						strnum = sqlQuery.queryForObject(totole, new Object[] { classId, batchId, gropid }, "String");
//						if (strnum == null || strnum.equals("")) {
//							sum = 0;
//						} else {
//							try {
//								sum = Integer.parseInt(strnum);
//							} catch (Exception e) {
//								sum = 0;
//							}
//						}
//						for (int x = 0; x < appinsIds.length; x++) {
//							intis = sqlQuery.queryForObject(is, new Object[] { classId, batchId, appinsIds[x], gropid },
//									"Integer");
//							// System.out.println(intis);
//							// 存在相同的数据 不修改
//							if (intis <= 0) {
//								// System.out.println(sum);
//								sum = sum + 1;
//								sqlQuery.update(sql, new Object[] { gropid, sum, classId, batchId, appinsIds[x] });
//							}
//						}
//					} else {
//						// System.out.println(gropid);
//						intis = sqlQuery.queryForObject(is, new Object[] { classId, batchId, appinsId, gropid },
//								"Integer");
//						// System.out.println(intis);
//						// 存在相同的数据 不修改
//						if (intis <= 0) {
//							strnum = sqlQuery.queryForObject(totole, new Object[] { classId, batchId, gropid },
//									"String");
//							if (strnum == null || strnum.equals("")) {
//								sum = 0;
//							} else {
//								try {
//									sum = Integer.parseInt(strnum);
//								} catch (Exception e) {
//									sum = 0;
//								}
//							}
//							// System.out.println(sum);
//							sum = sum + 1;
//							sqlQuery.update(sql, new Object[] { gropid, sum, classId, batchId, appinsId });
//						}
//					}
				}
			}
			//System.out.println(errorMsg.length()+"***********7************"+errorMsg.toString());
			if(errorMsg.length()>3){
				errorMsg.deleteCharAt(errorMsg.length()-1);
				errorMsg.append("已存在打分成绩，无法进行分组！");
			}else{
				//没有评委终跳出
				//System.out.println("++++++"+queryForList+"+++++++");
				if(null!=queryForList&&queryForList.size()<=0){
					errorMsg.replace(0, errorMsg.length(), "该评委组没有面试评委！");
				}else{
					errorMsg.replace(0, errorMsg.length(), "分组成功！");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();

			// TODO: handle exception
		}

		mapRet.put("MSG", errorMsg);

		return jacksonUtil.Map2json(mapRet);
	}

	// 面试规则 其他操作
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		// Map<String, Object> returMap = new HashMap<String, Object>();
		// JacksonUtil jacksonUtil = new JacksonUtil();
		String strRet = "{}";
		try {

			switch (oprType) {
			case "GETGROUP":
				strRet = this.getGroup(strParams);
				break;
			case "clearGroupInfo":
				strRet = this.clearGroupInfo(strParams);
				break;
			case "GETMSDATA":
				strRet = this.getGroupMs(strParams);
				break;
			case "GETSTUDATA":
				strRet = this.getMsStudent(strParams);
				break;
			case "saveOrder":
				strRet = this.saveOrder(strParams);
				break;
			default:

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
			// TODO: handle exception
		}
		return strRet;
	}
	/**
	 * 组内排序学生数据显示
	 * author:丁鹏
	 * time：2019年11月22日17:13:14
	 * */
	private String getMsStudent(String strParams) {
		JacksonUtil jacksonUtil = new JacksonUtil(strParams);
		String classId = jacksonUtil.getString("classId");
		String batchId = jacksonUtil.getString("batchId");
		String jugGroupId = jacksonUtil.getString("jugGroupId");
		String msGroupId = jacksonUtil.getString("msGroupId");
		HashMap<String, Object> map = new HashMap<>();
		String sql = "SELECT a.TZ_APP_INS_ID AS ins,a.TZ_ORDER AS sXh,c.TZ_REALNAME AS sName,b.TZ_MSH_ID as msh FROM PS_TZ_MSPS_KSH_TBL a,PS_TZ_FORM_WRK_T b,PS_TZ_REG_USER_T c WHERE a.TZ_CLASS_ID = ? AND a.TZ_APPLY_PC_ID = ? AND a.TZ_PS_GR_ID = ? AND a.TZ_GROUP_ID = ? AND a.TZ_APP_INS_ID = b.TZ_APP_INS_ID AND c.OPRID = b.OPRID ORDER BY a.TZ_ORDER";
		try {
			List<Map<String, Object>> queryForList = sqlQuery.queryForList(sql, new Object[]{classId,batchId,jugGroupId,msGroupId});
			map.put("SDATA", queryForList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(map).toString();
	}
	/**
	 * 组内排序面试组数据显示
	 * author:丁鹏
	 * time：2019年11月22日17:13:14
	 * */
	private String getGroupMs(String strParams) {
		JacksonUtil jacksonUtil = new JacksonUtil(strParams);
		String classId = jacksonUtil.getString("classId");
		String batchId = jacksonUtil.getString("batchId");
		//String jugGroupId = jacksonUtil.getString("jugGroupId");
		HashMap<String, Object> map = new HashMap<>();
		String sql = "SELECT TZ_GROUP_ID,TZ_GROUP_DESC FROM TZ_INTERVIEW_GROUP WHERE TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ?";
		try {
			List<Map<String, Object>> queryForList = sqlQuery.queryForList(sql, new Object[]{classId,batchId});
			map.put("MSDATA", queryForList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(map).toString();
	}
	/**
	 * 组内排序保存-迁移
	 * author：丁鹏
	 * time：2019年11月22日15:51:08
	 * */
	private String saveOrder(String strParams) {
		JacksonUtil jacksonUtil = new JacksonUtil(strParams);
		String classId = jacksonUtil.getString("classId");
		String batchId = jacksonUtil.getString("batchId");
		String jugGroupId = jacksonUtil.getString("jugGroupId");
		String msGroupId = jacksonUtil.getString("msGroupId");
		int msGroupId2 = Integer.parseInt(msGroupId);
		List<Map<String,Object>> list = (List<Map<String, Object>>) jacksonUtil.getList("data");
		list.forEach(item->{
			String ins = item.get("ins").toString();
			String xh = item.get("sXh").toString();
			int xh2 = Integer.parseInt(xh);
			sqlQuery.update("update PS_TZ_MSPS_KSH_TBL set TZ_ORDER = ? "
					+ "where TZ_CLASS_ID = ? "
					+ "AND TZ_APPLY_PC_ID = ? "
					+ "AND TZ_APP_INS_ID = ? "
					+ "AND TZ_PS_GR_ID = ? "
					+ "AND TZ_GROUP_ID = ?", new Object[]{xh2,classId,batchId,ins,jugGroupId,msGroupId2});
		});
		return null;
	}
	/**
	 * 清除分组信息-迁移
	 * @author 丁鹏
	 * @time 2019年11月22日14:26:24
	 */
	
	private String clearGroupInfo(String strParams) {
		JacksonUtil jacksonUtil = new JacksonUtil(strParams);
		String classId = jacksonUtil.getString("classId");
		String batchId = jacksonUtil.getString("batchId");
		String insId = jacksonUtil.getString("insId");
		List<Map<String,Object>> queryForList = sqlQuery.queryForList("SELECT a.TZ_APP_INS_ID FROM PS_TZ_MSPS_KSH_TBL a WHERE a.TZ_CLASS_ID = ? AND a.TZ_APPLY_PC_ID = ? AND a.TZ_APP_INS_ID IN ("+insId+") AND NOT EXISTS (SELECT 'X' FROM PS_TZ_MP_PW_KS_TBL WHERE a.TZ_CLASS_ID = TZ_CLASS_ID AND a.TZ_APPLY_PC_ID = TZ_APPLY_PC_ID AND a.TZ_APP_INS_ID = TZ_APP_INS_ID AND TZ_SCORE_INS_ID > 0);",new Object[]{classId,batchId});

		if(null!=queryForList&&queryForList.size()>0){
			queryForList.forEach(item->{
				System.out.println(item.toString());
				String TZ_SCORE_INS_ID = item.get("TZ_SCORE_INS_ID")!=null?item.get("TZ_SCORE_INS_ID").toString():"0";
				String TZ_APP_INS_ID = item.get("TZ_APP_INS_ID")!=null?item.get("TZ_APP_INS_ID").toString():"0";
				try {
					sqlQuery.update("delete from PS_TZ_MP_PW_KS_TBL where TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ? AND TZ_APP_INS_ID = ? and TZ_SCORE_INS_ID = '0'", new Object[]{classId,batchId,TZ_APP_INS_ID});
					String sql = "UPDATE PS_TZ_MSPS_KSH_TBL SET TZ_PS_GR_ID = ?, TZ_GROUP_DATE = NOW(), TZ_ORDER = ?, TZ_GROUP_ID = ? WHERE TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ? AND TZ_APP_INS_ID = ?";
					sqlQuery.update(sql,new Object[]{"",null,null, classId, batchId, TZ_APP_INS_ID});
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}else{

		}
		return null;
	}
	/**
	 * 得到用户分组所属的面试组
	 * 
	 * @param strParams
	 * @return
	 */
	/***
	 * 得到用户分组所属的面试组，修改
	 * 
	 * @author ding peng
	 * @time 2019年11月21日14:03:24
	 * @return
	 */
	private String getGroup(String strParams) {
		String strRet = "";
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		rtnMap.put("pwGroupId", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			// 班级ID
			String classId = jacksonUtil.getString("classId");
			String batchId = jacksonUtil.getString("batchId");
			String appInsID = jacksonUtil.getString("appInsId");
			// 批量的情况
			if (appInsID.indexOf(",") != -1) {
				rtnMap.put("pwGroupId", "");
			} else {
				String sql = "SELECT A.TZ_PS_GR_ID FROM PS_TZ_MSPS_KSH_TBL A WHERE A.TZ_CLASS_ID = ? and A.TZ_APPLY_PC_ID = ? and A.TZ_APP_INS_ID = ?";
				strRet = sqlQuery.queryForObject(sql, new Object[] { classId, batchId, appInsID }, "String");
				if (strRet == null) {
					strRet = "";
				}
				rtnMap.put("pwGroupId", strRet);
			}
			String Orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			List<Map<String, Object>> queryForList2 = new ArrayList<Map<String,Object>>(0);
			List<Map<String,Object>> queryForList = sqlQuery.queryForList("SELECT DISTINCT B.TZ_CLPS_GR_ID AS TZ_CLPS_GR_ID,B.TZ_CLPS_GR_NAME AS TZ_CLPS_GR_NAME "
					+ "FROM PS_TZ_MSPS_PW_TBL A,PS_TZ_MSPS_GR_TBL B "
					+ "WHERE A.TZ_CLASS_ID = ? and A.TZ_APPLY_PC_ID = ? AND B.TZ_JG_ID = ? AND A.TZ_PWEI_GRPID <> ''", new Object[]{classId,batchId,Orgid});
			if(queryForList!=null){
				queryForList2 = queryForList;
			}
			rtnMap.put("pwGroup",queryForList2);
		} catch (Exception e) {

		}
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}

	/***
	 * 获取评委--面试分组列表，如果没有 默认添加20组分组
	 * 
	 * @param comParams
	 * @param numLimit
	 * @param numStart
	 * @param errorMsg
	 * @return
	 */
	/***
	 * 获取评委--面试分组列表，修改
	 * 
	 * @author ding peng
	 * @time 2019年11月21日10:03:08
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		//String strRet = "";

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(comParams);
			// 班级ID
			String classId = jacksonUtil.getString("classId");
			String batchId = jacksonUtil.getString("batchId");
			String appInsID = jacksonUtil.getString("appInsId");
			String pwGroupId = jacksonUtil.getString("pwGroupId");
			System.out.println("appInsID:" + appInsID);
			if (pwGroupId != null && !pwGroupId.equals("")) {
				List<Map<String,Object>> queryForList = sqlQuery.queryForList("SELECT a.TZ_GROUP_SPACE as space,a.TZ_GROUP_ID as groupID,a.TZ_GROUP_DESC as groupName,"
						+ "(SELECT COUNT(1) FROM PS_TZ_MSPS_KSH_TBL b where a.TZ_CLASS_ID = b.TZ_CLASS_ID and a.TZ_APPLY_PC_ID = b.TZ_APPLY_PC_ID and a.TZ_GROUP_ID = b.TZ_GROUP_ID and b.TZ_PS_GR_ID = ?) as suNum "
						+ "FROM TZ_INTERVIEW_GROUP a "
						+ "WHERE a.TZ_CLASS_ID = ? AND a.TZ_APPLY_PC_ID = ?",new Object[]{pwGroupId,classId,batchId});
				if(queryForList!=null&&queryForList.size()>0){

					mapRet.replace("total", queryForList.size());
					mapRet.replace("root", queryForList);
				}
			}

		
			
//			jacksonUtil.json2Map(comParams);
//			// 班级ID
//			String classId = jacksonUtil.getString("classId");
//			String batchId = jacksonUtil.getString("batchId");
//			String appInsID = jacksonUtil.getString("appInsId");
//			String pwGroupId = jacksonUtil.getString("pwGroupId");
//			System.out.println("appInsID:" + appInsID);
//			if (pwGroupId != null && !pwGroupId.equals("")) {
//
//				Map<String, Object> mapList = null;
//				int count = sqlQuery.queryForObject(
//						"select count(1) from PS_TZ_INTEGROUP_T where TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? and TZ_CLPS_GR_ID=?",
//						new Object[] { classId, batchId, pwGroupId }, "Integer");
//				String sql = "";
//				// 一条都没有默认添加20个分组
//				// start: 0
//				// limit: 5
//				if (count <= 0) {
//					int id = 0;
//					sql = "INSERT INTO PS_TZ_INTEGROUP_T(TZ_GROUP_ID,TZ_GROUP_NAME,TZ_CLPS_GR_ID,TZ_CLASS_ID,TZ_APPLY_PC_ID) VALUES(?,?,?,?,?)";
//					for (int i = 1; i <= 20; i++) {
//						id = getSeqNum.getSeqNum("TZ_INTEGROUP_T", "TZ_GROUP_ID");
//						sqlQuery.update(sql, new Object[] { id, i + "组", pwGroupId, classId, batchId });
//						mapList = new HashMap<String, Object>();
//						mapList.put("check", "");
//						mapList.put("groupID", id);
//						mapList.put("groupName", i + "组");
//						mapList.put("suNum", "0");
//						// 分页
//						if (i <= 5) {
//							listData.add(mapList);
//						}
//					}
//					mapRet.replace("total", 20);
//					mapRet.replace("root", listData);
//				} else {
//					// 有就取出来
//					sql = "select TZ_GROUP_ID,TZ_GROUP_NAME from PS_TZ_INTEGROUP_T where TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? and TZ_CLPS_GR_ID=? order by TZ_GROUP_ID limit ?,?";
//					String getCheck = "select 'Y' from PS_TZ_MSPS_KSH_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=? AND TZ_GROUP_ID=?";
//					String getNum = "select count(1) from PS_TZ_MSPS_KSH_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=?  AND TZ_GROUP_ID=?";
//					List<Map<String, Object>> appInsList = sqlQuery.queryForList(sql,
//							new Object[] { classId, batchId, pwGroupId, numStart, numLimit });
//					String TZ_GROUP_ID = "";
//					String TZ_GROUP_NAME = "";
//					String isCheck = "";
//
//					for (Map<String, Object> appInsMap : appInsList) {
//						TZ_GROUP_ID = appInsMap.get("TZ_GROUP_ID").toString();
//						TZ_GROUP_NAME = appInsMap.get("TZ_GROUP_NAME").toString();
//						mapList = new HashMap<String, Object>();
//						mapList.put("groupID", TZ_GROUP_ID);
//						mapList.put("groupName", TZ_GROUP_NAME);
//						if (appInsID.indexOf(",") != -1) {
//							isCheck = "";
//						} else {
//							isCheck = sqlQuery.queryForObject(getCheck,
//									new Object[] { classId, batchId, appInsID, TZ_GROUP_ID }, "String");
//						}
//						if (isCheck != null && isCheck.equals("Y")) {
//							mapList.put("check", "Y");
//						} else {
//							mapList.put("check", "");
//						}
//						mapList.put("suNum", sqlQuery.queryForObject(getNum,
//								new Object[] { classId, batchId, TZ_GROUP_ID }, "Integer"));
//						listData.add(mapList);
//					}
//					mapRet.replace("total", 20);
//					mapRet.replace("root", listData);
//				}
//				strRet = jacksonUtil.Map2json(mapRet);
//			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet).toString();
		//return strRet;
	}
}
