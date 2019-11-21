package com.tranzvision.gd.TZEvaluationSystemBundle.service.impl.module;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZMbaPwMspsBundle.dao.psTzMspwpsjlTblMapper;
import com.tranzvision.gd.TZMbaPwMspsBundle.model.psTzMspwpsjlTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * @author 张枫
 * 2019年7月29日
 * 面试汇总
 */
@Component
public class InterviewStatistics{

	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	TZGDObject tzSqlObject;
	@Autowired
	HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	psTzMspwpsjlTblMapper psTzMspwpsjlTblMapper;
	@Autowired
	private SqlQuery sqlQuery;

	public String getContent (String strParams) throws Exception {
		JacksonUtil params = new JacksonUtil(strParams);
		params.json2Map(strParams);
		String method = params.getString("mothed");
		System.out.println(method);
		Method method2 = this.getClass().getMethod(method,String.class);
		return (String) method2.invoke(this,strParams);
	}

	public String submitCollect(String strParams){
		JacksonUtil params = new JacksonUtil(strParams);
		String CLASSID = params.getString("CLASSID");
		String BATCHID = params.getString("BATCHID");
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		//评委信息
		String OPRID = tzLoginServiceImpl.getLoginedManagerOprid(request);
		
		try {
			//判断当前评委是否已经提交;
			String TZ_SUBMIT_YN = queryData("select TZ_SUBMIT_YN from PS_TZ_MSPWPSJL_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID = ? and TZ_PWEI_OPRID = ?",
					new Object[]{CLASSID,BATCHID,OPRID}, String.class);

			if("Y".equals(TZ_SUBMIT_YN)){
				result.put("flag", "0");
				result.put("msg", "当前评委账号已经提交，不能再提交。");
			}else{

				/*所有考生的提交状态都是“已提交”*/

				String sql_str = null ;
				try {
					sql_str = tzSqlObject.getSQLText("SQL.TZEvaluationSystemBundle.InterviewEI03");
				} catch (TzSystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String submit_zt = queryData(sql_str,new Object[]{CLASSID,BATCHID,OPRID}, String.class);

				if("Y".equals(submit_zt)){
					result.put("flag", "0");
					result.put("msg", "存在未评审的考生，无法提交数据。");
				}else{


					/*检查是否给所有分组下的考生打分完成*/

					//目前评委与考生关系表的个数
					Integer alreadyNum = queryData("SELECT COUNT(1) FROM PS_TZ_MP_PW_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=? AND TZ_DELETE_ZT<>'Y'",
							new Object[]{CLASSID,BATCHID,OPRID}, Integer.class);

					//评委需要评审面试组下所有考生的个数
					StringBuilder needNumSql = new StringBuilder("SELECT COUNT(1) FROM PS_TZ_MP_PW_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=? and tz_pshen_zt = 'Y' AND TZ_DELETE_ZT<>'Y'");

					Integer needNum = queryData(needNumSql.toString(), new Object[]{CLASSID,BATCHID,OPRID}, Integer.class);

					if(!alreadyNum.equals(needNum)) {
						result.put("flag", "0");
						result.put("msg", "发现存在未评审的面试组考生，无法提交数据。");
					} else {
						result.put("flag", "1");
						result.put("msg", "提交成功。");
						//校验成功
						String mspwpsjlExist = queryData("select 'Y' from PS_TZ_MSPWPSJL_TBL where TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=? and TZ_PWEI_OPRID=? and TZ_SUBMIT_YN<>'Y'", 
								new Object[]{CLASSID,BATCHID,OPRID},String.class);

						psTzMspwpsjlTbl psTzMspwpsjlTbl = new psTzMspwpsjlTbl();
						psTzMspwpsjlTbl.setTzClassId(CLASSID);
						psTzMspwpsjlTbl.setTzApplyPcId(BATCHID);
						psTzMspwpsjlTbl.setTzPweiOprid(OPRID);
						psTzMspwpsjlTbl.setTzSubmitYn("Y");
						psTzMspwpsjlTbl.setTzSignature("");
						psTzMspwpsjlTbl.setRowLastmantOprid(OPRID);
						psTzMspwpsjlTbl.setRowLastmantDttm(new Date());

						if("Y".equals(mspwpsjlExist)){
							psTzMspwpsjlTblMapper.updateByPrimaryKeySelective(psTzMspwpsjlTbl);
						}else{
							psTzMspwpsjlTbl.setRowAddedOprid(OPRID);
							psTzMspwpsjlTbl.setRowAddedDttm(new Date());
							psTzMspwpsjlTblMapper.insertSelective(psTzMspwpsjlTbl);
						}
						
						//计算考生平均分
						updateTally(CLASSID,BATCHID);
					}
				
					/*检查考生的成绩单里有成绩为0的成绩项*/ 
//					String name_zero_all = "";

//					String zeroSql = null;
//					try {
//						zeroSql = tzSqlObject.getSQLText("SQL.TZEvaluationSystemBundle.InterviewEI04");
//					} catch (TzSystemException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//
//					List<Map<String, Object>> listZero = queryData(zeroSql,new Object[]{CLASSID,BATCHID,OPRID}, List.class);  
//
//					for(Map<String, Object> mapZero : listZero) {
//						String name_zero = mapZero.get("TZ_REALNAME") == null ? "" : mapZero.get("TZ_REALNAME").toString();
//						String flag_zero = mapZero.get("TZ_FLAG") == null ? "" : mapZero.get("TZ_FLAG").toString();
//
//						if("Y".equals(flag_zero)) {
//							if(!"".equals(name_zero_all)) {
//								name_zero_all += "、" + name_zero;
//							} else {
//								name_zero_all = name_zero;
//							}
//						}
//					}
//
//					if(!"".equals(name_zero_all)) {
//						result.put("flag", "0");
//						result.put("msg", "发现存在成绩项分数为0的考生["+ name_zero_all +"]，无法提交数据。");
//					} else  {}	

				}

			}

		} catch (Exception e) {
			result.put("flag", "0");
			result.put("msg", e.toString());
		}
		return params.Map2json(result);
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public String initPageData(String strParams){
		JacksonUtil params = new JacksonUtil(strParams);
		String USERNAME = params.getString("USERNAME");
		String JGID = params.getString("JGID");
		String CLASSID = params.getString("CLASSID");
		String BATCHID = params.getString("BATCHID");
		String MSZSPACE = params.getString("MSZSPACE");
		HashMap<String, Object> result = new HashMap<String, Object>();
		//评委信息
		String OPRID = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String ORGID = tzLoginServiceImpl.getLoginedManagerOrgid(request);

		//表头数据
		List<String> headerS = new ArrayList<String>();
		headerS.add("评分标准\\学生");
		//insid
		List<String> headerSins = new ArrayList<String>();
		headerSins.add("");
		//成绩数据
		List<List<String>> scoreS = new ArrayList<List<String>>();
		//公司
		List<String> personCom = new ArrayList<String>();
		personCom.add("公司");
		//职位
		List<String> personPos = new ArrayList<String>();
		personPos.add("职位");


		try {

			String queryForObject = sqlQuery.queryForObject("SELECT TOP 1 TZ_GROUP_SPACE FROM ps_TZ_MP_PW_KS_TBL a INNER JOIN PS_TZ_MSPS_KSH_TBL b ON (A.TZ_CLASS_ID = b.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID = b.TZ_APPLY_PC_ID AND A.TZ_APP_INS_ID = b.TZ_APP_INS_ID) INNER JOIN TZ_INTERVIEW_GROUP c ON (c.TZ_CLASS_ID = b.TZ_CLASS_ID AND c.TZ_APPLY_PC_ID = b.TZ_APPLY_PC_ID AND c.TZ_GROUP_ID = b.TZ_GROUP_ID) WHERE A.TZ_CLASS_ID = ? AND A.TZ_APPLY_PC_ID = ? AND A.TZ_PWEI_OPRID = ? AND A.TZ_DELETE_ZT = 'N' AND TZ_GROUP_SPACE = (CASE WHEN DATEDIFF(HOUR,CONVERT(VARCHAR(100),GETDATE(), 8),'12:00:00')>0 THEN 'A' ELSE 'B' END)", new Object[]{CLASSID,BATCHID,OPRID}, "String");
			MSZSPACE = (null==MSZSPACE||"".equals(MSZSPACE.trim()))?queryForObject==null?"":queryForObject:MSZSPACE;
			result.put("MSZSPACE", MSZSPACE);
			
			List<Map<String,Object>> MSZSPACES = sqlQuery.queryForList("SELECT DISTINCT c.TZ_GROUP_SPACE as MSZSPACE,(CASE c.TZ_GROUP_SPACE WHEN 'A' THEN '上午' WHEN 'B' THEN '下午' END) AS DESCR FROM ps_TZ_MP_PW_KS_TBL a INNER JOIN PS_TZ_MSPS_KSH_TBL b ON (A.TZ_CLASS_ID = b.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID = b.TZ_APPLY_PC_ID AND A.TZ_APP_INS_ID = b.TZ_APP_INS_ID) INNER JOIN TZ_INTERVIEW_GROUP c ON (c.TZ_CLASS_ID = b.TZ_CLASS_ID AND c.TZ_APPLY_PC_ID = b.TZ_APPLY_PC_ID AND c.TZ_GROUP_ID = b.TZ_GROUP_ID) WHERE A.TZ_CLASS_ID = ? AND A.TZ_APPLY_PC_ID = ? AND A.TZ_PWEI_OPRID = ? AND A.TZ_DELETE_ZT = 'N'", new Object[]{CLASSID,BATCHID,OPRID});
			result.put("MSZSPACES", MSZSPACES);
			
			//表头，学生公司、职位处理
			String studentSql = tzGDObject.getSQLText("SQL.TZEvaluationSystemBundle.TzInterviewStudentList");
			List<Map<String,Object>> studentList = queryData(studentSql, new Object[]{OPRID,CLASSID,BATCHID},ArrayList.class);
			if(!MSZSPACE.isEmpty()){
				String MSZSPACE0 = MSZSPACE;
				//studentList = studentList.stream().filter(item-> MSZSPACE0.equals(item.get("SPACE").toString())).collect(Collectors.toList());
				studentList = studentList.stream().filter(item-> MSZSPACE0.equals(item.get("SPACE")==null?"null":item.get("SPACE").toString())).collect(Collectors.toList());

			}
			Optional.ofNullable(studentList).get().stream().forEach(map->{
				String oprid = map.get("oprid").toString();
				//String SPACE1 = map.get("SPACE").toString();
				//表头，学生公司、职位处理
				Map<String, Object> baseInfo = null;
				try {
					baseInfo = queryData("select " + "e.TZ_REALNAME as name,f.TZ_COMPANY_NAME as COMPANY,f.TZ_COMMENT1 as postion "
							+ "from PS_TZ_AQ_YHXX_TBL e " + "LEFT JOIN PS_TZ_REG_USER_T f ON f.OPRID = e.OPRID "
							+ "where e.OPRID = ?",
							new Object[] { oprid }, Map.class);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("baseInfo:"+baseInfo.toString()+"-------------------------------------------------------");
				Optional.ofNullable(baseInfo).ifPresent(item->{
					headerSins.add(map.get("insid")==null?"":map.get("insid").toString());
					headerS.add(item.get("name")==null?"":item.get("name").toString());
					personCom.add(item.get("COMPANY")==null?"":item.get("COMPANY").toString());
					personPos.add(item.get("postion")==null?"":item.get("postion").toString());
				});
			});			
//			for (Map<String, Object> map : studentList) {
//				//学生oprid
//				
//			}

			//获得成绩模型
			String scoreModalId = queryData("select d.TZ_MSCJ_SCOR_MD_ID  from PS_TZ_PRJ_INF_T c INNER JOIN PS_TZ_CLASS_INF_T d ON (c.tz_prj_id = d.tz_prj_id) WHERE c.TZ_IS_OPEN = 'Y' and d.TZ_CLASS_ID = ?", new Object[]{CLASSID},String.class);
			if (scoreModalId!=null&&!"null".equals(scoreModalId.trim())) {
				//获得成绩模型对应的树
				Map<String, Object> fbAndTree = queryData(
						"SELECT TREE_NAME as treeId,TZ_M_FBDZ_ID as fbdzId FROM PS_TZ_RS_MODAL_TBL WHERE TZ_JG_ID = ? AND TZ_SCORE_MODAL_ID = ?",
						new Object[] { JGID, scoreModalId }, Map.class);
				
				if(Optional.of(fbAndTree).isPresent()){

					//树Id
					String treeId = fbAndTree.get("treeId")!=null?fbAndTree.get("treeId").toString():"";
					//分布对照表Id
					String fbdzId = fbAndTree.get("fbdzId")!=null?fbAndTree.get("fbdzId").toString():"";
					/*--------------------------------------成绩处理开始--------------------------------------*/
					//成绩项 
					String cjxSql0 = "(SELECT " 
							+ "c.tz_score_item_id," 
							+ "b.tz_score_item_type," 
							+ "b.DESCR," 
							+ "c.TZ_PX,"
							+ "a.TZ_SCORE_MODAL_ID "
							+ "FROM PS_TZ_RS_MODAL_TBL a "
							+ "INNER JOIN PS_TZ_MODAL_DT_TBL b ON (a.tree_name = b.tree_name AND a.TZ_JG_ID = b.TZ_JG_ID) "
							+ "INNER JOIN PS_TZ_CJ_BPH_TBL c ON (a.TZ_SCORE_MODAL_ID = c.TZ_SCORE_MODAL_ID AND b.tz_score_item_id = c.tz_score_item_id) "
							+ "WHERE c.TZ_ITEM_S_TYPE = 'A' AND b.tz_score_item_type <> 'A')";
					//成绩汇总
					String cjxSql1 = "(SELECT " 
							+ "b.tz_score_item_id," 
							+ "b.tz_score_item_type," 
							+ "b.DESCR," 
							+ "99,"
							+ "a.TZ_SCORE_MODAL_ID "
							+ "FROM PS_TZ_RS_MODAL_TBL a "
							+ "INNER JOIN PS_TZ_MODAL_DT_TBL b ON (a.tree_name = b.tree_name AND a.TZ_JG_ID = b.TZ_JG_ID) "
							+ "WHERE b.tz_score_item_type = 'A')";
					//并集
					String cjxSql = "SELECT z.tz_score_item_id,z.tz_score_item_type,z.DESCR,z.TZ_PX FROM (" + cjxSql0
							+ " UNION ALL " + cjxSql1 + ") z WHERE z.TZ_SCORE_MODAL_ID = ? ORDER BY z.TZ_PX ASC";
					List<Map<String, Object>> cjxList = queryData(cjxSql, new Object[] {scoreModalId},ArrayList.class);
					for (Map<String, Object> cjxmap : cjxList) {

						List<String> scoreList = new ArrayList<String>();

						//成绩项Id
						String cjxId = cjxmap.get("tz_score_item_id").toString();

						//成绩项打分类型
						String cjxType = cjxmap.get("tz_score_item_type").toString();

						//成绩项描述
						String cjxDescr = cjxmap.get("DESCR").toString();

						//成绩打分类型	A：汇总	B:数字	C：评语	D：下拉框
						String limitNum = "";//分数上限
						switch (cjxType) {
						case "A":
							//用于前端标红
							limitNum = "-2";
							break;
						case "B":
							//设置的分数上限
							limitNum = queryData(
									"SELECT TZ_SCORE_LIMITED FROM PS_TZ_MODAL_DT_TBL WHERE TZ_JG_ID = ? AND TREE_NAME = ? AND TZ_SCORE_ITEM_ID = ?",
									new Object[] { JGID, treeId, cjxId }, String.class);
							break;
						case "D":
							//选择项分数最大值
							limitNum = queryData(
									"SELECT MAX(TZ_CJX_XLK_XXFZ) from PS_TZ_ZJCJXXZX_T WHERE TZ_JG_ID = ? AND TREE_NAME = ? AND TZ_SCORE_ITEM_ID = ?",
									new Object[] { JGID, treeId, cjxId }, String.class);
							break;
						default:
							//不显示分数上限
							limitNum = "-1";
							break;
						}

						scoreList.add(cjxDescr==null?"":cjxDescr);
						scoreList.add(limitNum==null?"":limitNum);
						/*--------------------------------------学生处理开始--------------------------------------*/
						//学生列表
						for (Map<String, Object> map : studentList) {
							//实例Id
							String insid = map.get("insid").toString();
							//成绩Id
							String scoreid = map.get("scoreid").toString();
							//学生oprid
							String oprid = map.get("oprid").toString();

							//学生的得分状况
							Map<String, Object> scoreData = queryData(
									"SELECT TZ_SCORE_NUM,TZ_CJX_XLK_XXBH,TZ_SCORE_PY_VALUE "
											+ "FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID = ? AND TZ_SCORE_ITEM_ID = ?",
											new Object[] { scoreid, cjxId }, Map.class);

							//页面显示
							String viewScore = "";

							if (scoreData!=null) {
								switch (cjxType) {
								case "A":
								case "B":
									viewScore = scoreData.get("TZ_SCORE_NUM")==null?"0":scoreData.get("TZ_SCORE_NUM").toString();
									break;
								case "C":
									viewScore = scoreData.get("TZ_SCORE_PY_VALUE") == null ? ""
											: scoreData.get("TZ_SCORE_PY_VALUE").toString();
									break;
								case "D":
									viewScore = queryData(
											"SELECT TZ_CJX_XLK_XXFZ from PS_TZ_ZJCJXXZX_T "
													+ "WHERE TZ_JG_ID = ? AND TREE_NAME = ? AND TZ_SCORE_ITEM_ID = ? AND TZ_CJX_XLK_XXBH = ?",
													new Object[] { JGID, treeId, cjxId,
															scoreData.get("TZ_CJX_XLK_XXBH").toString() },
													String.class);
									break;
								default:
									viewScore = "";
									break;
								}
							}
							scoreList.add(viewScore);

						}
						scoreS.add(scoreList);
						/*--------------------------------------学生处理结束--------------------------------------*/
					}
					/*--------------------------------------成绩处理结束--------------------------------------*/

				}
			}
		} catch (TzSystemException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}


		result.put("headerS", headerS);
		result.put("scoreS", scoreS);
		result.put("personCom", personCom);
		result.put("headerSins", headerSins);
		result.put("personPos", personPos);
		return params.Map2json(result);
	}
	
	@SuppressWarnings({"unchecked" })
	public <T> T queryData(String sql,Object[] params,Class<T> cType) throws Exception{
		T result = null;
		if(sql==null||sql.length()==0){
			throw new Exception("查询语句为空"); 
		}else if(cType==null){
			throw new Exception("返回类型为空"); 
		}else if(sql.indexOf("?")!=-1&&(params==null||params.length==0)){
			throw new Exception("参数为空"); 
		}else{
			String typeT = cType.getSimpleName();
			switch (typeT) {
			case "List": 
			case "ArrayList": 
				result = (T) sqlQuery.queryForList(sql, params);
				break;
			case "Map": 
				result = (T) sqlQuery.queryForMap(sql, params);
				break;
			case "String": 
				result = sqlQuery.queryForObject(sql, params, typeT);
			case "Integer": 
				result = sqlQuery.queryForObject(sql, params, typeT);
				break;
			default:
				result = (T) "";
				break;
			}
		}
		
		return result;
	}

	/**
	 * 计算考生面试总分
	 * 
	 * @return
	 */
	private void updateTally(String classId, String batchId) {

		try {
			/* 当前机构 */
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			String sql = tzSqlObject.getSQLText("SQL.TZEvaluationSystemBundle.TzInterviewExamineeTally");

			List<Map<String, Object>> listData = sqlQuery.queryForList(sql,
					new Object[] { orgId, classId, batchId });

			for (Map<String, Object> mapData : listData) {
				Long appInsId = (Long) mapData.get("TZ_APP_INS_ID");
				BigDecimal tally = ((BigDecimal) mapData.get("TZ_SCORE")).setScale(1,BigDecimal.ROUND_HALF_UP);
				sqlQuery.update("UPDATE PS_TZ_MSPS_KSH_TBL SET TZ_SCORE=? WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=?",
						new Object[]{tally,classId,batchId,appInsId});
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
