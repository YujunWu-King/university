package com.tranzvision.gd.TZChargeManage.service.impl;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import net.sf.json.JSONArray;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 缴费计划 
 * @author xzp
 * 2019年7月30日
 *
 */
@Service("com.tranzvision.gd.TZChargeManage.service.impl.ViewDataDetailsServcieImpl")
public class ViewDataDetailsServcieImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private AdmissionAndPayImpl admissionAndPayImpl;

	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();

		String jgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		try{
			jacksonUtil.json2Map(strParams);
			String appIns = "";//报名表编号
			if (jacksonUtil.containsKey("appInsId")) {
				appIns = jacksonUtil.getString("appInsId");
			}
			//根据报名表编号去查信息
			String ssql = "SELECT TZ_REALNAME FROM ps_tz_aq_yhxx_tbl A JOIN ps_tz_form_wrk_t B ON A.OPRID=B.OPRID WHERE TZ_APP_INS_ID = ? AND TZ_JG_ID=?";
			String name = sqlQuery.queryForObject(ssql, new Object[]{appIns,jgId}, "String");
			//报考班级  学费标准
			String className = "",standard = "" ,mainFZR = "";
			ssql = "SELECT A.TZ_CLASS_NAME,A.TZ_TUITION_STANDARD,C.TZ_REALNAME FROM ps_tz_class_inf_t A JOIN ps_tz_form_wrk_t B ON A.TZ_CLASS_ID=B.TZ_CLASS_ID LEFT JOIN PS_TZ_AQ_YHXX_TBL C ON B.MAIN_OPRID = C.OPRID WHERE B.TZ_APP_INS_ID=?";
			Map<String, Object> classInfoMap= sqlQuery.queryForMap(ssql, new Object[]{appIns});
			if(classInfoMap!=null){
				className = classInfoMap.get("TZ_CLASS_NAME")==null?"":classInfoMap.get("TZ_CLASS_NAME").toString();
				standard = classInfoMap.get("TZ_TUITION_STANDARD")==null?"":classInfoMap.get("TZ_TUITION_STANDARD").toString();
				mainFZR = classInfoMap.get("TZ_REALNAME")==null?"":classInfoMap.get("TZ_REALNAME").toString();
			}
			//录取状态 
			String lqStatus = "";
			String sqlText8="select TZ_MSH_ID from PS_TZ_FORM_WRK_T where TZ_APP_INS_ID=?";
			String TZ_MSH_ID=sqlQuery.queryForObject(sqlText8, new Object[] {appIns}, "String");
			String sqlText1="select TZ_LQ_RESULT from TZ_MS_LQJG_T where TZ_BMB_ID=?";
			lqStatus=sqlQuery.queryForObject(sqlText1, new Object[] {TZ_MSH_ID}, "String")==null?"":sqlQuery.queryForObject(sqlText1, new Object[] {TZ_MSH_ID}, "String");


			//所属班级
			String sSClassName = "",stuXjzt="";
			ssql = "SELECT C.tzms_class_name,B.tzms_stu_xjzt FROM TZ_KSHSTU_GX_T A JOIN tzms_stu_defn_tBase B ON A.TZ_STU_BH = B.tzms_stu_defn_tid LEFT JOIN tzms_cls_defn_tBase C ON B.tzms_stu_xjbj=C.tzms_cls_defn_tId WHERE A.TZ_APP_INS_ID=?";
			Map<String, Object> stuInfoMap= sqlQuery.queryForMap(ssql, new Object[]{appIns});
			if(stuInfoMap!=null){
				sSClassName = stuInfoMap.get("tzms_class_name")==null?"":stuInfoMap.get("tzms_class_name").toString();
				stuXjzt = stuInfoMap.get("tzms_stu_xjzt")==null?"":stuInfoMap.get("tzms_stu_xjzt").toString();
			}
			List<Map<String, Object>> jxjList = sqlQuery.queryForList("SELECT A.TZ_JXJ_ID,TZ_JXJ_NAME FROM TZ_KS_JXJ_T A JOIN TZ_JXJ_DEFN_T B ON A.TZ_JXJ_ID = B.TZ_JXJ_ID WHERE TZ_APP_INS_ID=?",new Object[]{appIns});
			List<String> jxjIdList = new ArrayList<>();
			List<String> jxjNameList = new ArrayList<>();
			if(jxjList!=null && jxjList.size()>0){
				for(Map<String, Object> jxjMap:jxjList){
					jxjIdList.add(String.valueOf(jxjMap.get("TZ_JXJ_ID")));
					jxjNameList.add(String.valueOf(jxjMap.get("TZ_JXJ_NAME")));
				}
			}
			double yjJine = 0.00,yiJiaoJIne=0.00;
			ssql = "SELECT SUM(TZ_JF_BQYS) AS TZ_JF_YJ,SUM(TZ_JF_BQSS) AS TZ_JF_YIJ FROM TZ_JF_PLAN_T WHERE TZ_OBJ_ID=? AND TZ_OBJ_TYPE='1' ";
			Map<String, Object> jfplMap = sqlQuery.queryForMap(ssql,new Object[]{appIns});
			if(jfplMap!=null){
				yjJine = jfplMap.get("TZ_JF_YJ")==null?0.00:Double.parseDouble(jfplMap.get("TZ_JF_YJ").toString());
				yiJiaoJIne =  jfplMap.get("TZ_JF_YIJ")==null?0.00:Double.parseDouble(jfplMap.get("TZ_JF_YIJ").toString());
			}
			//奖学金说明
			ssql = "SELECT TOP 1 TZ_EXPLANATION FROM TZ_KS_JXJ_T WHERE TZ_APP_INS_ID=?";
			String explanation = sqlQuery.queryForObject(ssql, new Object[]{Long.parseLong(appIns)},"String");
			if(explanation==null){
				explanation = "";
			}
			
			Map<String, Object> returnMap = new HashMap<>();
			returnMap.put("appIns", appIns);
			returnMap.put("stuName", name);
			returnMap.put("bkClass", className);
			returnMap.put("lqStatus", lqStatus);
			returnMap.put("mainFZR", mainFZR);
			returnMap.put("sSClass", sSClassName);
			returnMap.put("xjStatus", stuXjzt);
			returnMap.put("xfStatus", standard);
			returnMap.put("jxjNames", jxjNameList);
			returnMap.put("jxjIds", jxjIdList);
			returnMap.put("yingJJine", yjJine);
			//本期实收总和
			returnMap.put("yiJJine", yiJiaoJIne);
			returnMap.put("explanation", explanation);
			returnJsonMap.replace("formData", returnMap);
		}catch(Exception e){
			e.printStackTrace();
			errMsg[0]="1";
			errMsg[1]=e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}

	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		int total=0;
		try{
			String ssql="";
			//报名表ID
			String appIns = jacksonUtil.getString("appIns");
			ssql = "SELECT A.TZ_TUITION_STANDARD FROM ps_tz_class_inf_t A JOIN ps_tz_form_wrk_t B ON A.TZ_CLASS_ID=B.TZ_CLASS_ID WHERE B.TZ_APP_INS_ID = ? ";
			String xfStandard = sqlQuery.queryForObject(ssql, new Object[]{appIns}, "String");
			if(xfStandard==null || "".equals(xfStandard) ){
				xfStandard = "0";
			}
			total = sqlQuery.queryForObject("SELECT COUNT(*) FROM TZ_JF_PLAN_T WHERE TZ_OBJ_ID=? AND TZ_OBJ_TYPE='1'",new Object[]{appIns},"int");
			ssql = "SELECT * FROM TZ_JF_PLAN_T WHERE TZ_OBJ_ID=? AND TZ_OBJ_TYPE='1' ORDER BY TZ_JFPL_ID  offset ? rows fetch next ? rows only";
			if(total>0){
				List<Map<String, Object>> fkPlanList = sqlQuery.queryForList(ssql, new Object[]{appIns,numStart,numLimit});
				if(fkPlanList!=null&&fkPlanList.size()>0){
					for(Map<String, Object> fkPlanRec :fkPlanList){
						//缴费日期
						String jfDate = fkPlanRec.get("TZ_JF_DATE")==null?"":fkPlanRec.get("TZ_JF_DATE").toString();
						double jfbz = fkPlanRec.get("TZ_JF_BZ_JE")==null?0.00:Double.parseDouble(fkPlanRec.get("TZ_JF_BZ_JE").toString());
						double xftz = fkPlanRec.get("TZ_JF_TZ_JE")==null?0.00:Double.parseDouble(fkPlanRec.get("TZ_JF_TZ_JE").toString());
						double jmJine = fkPlanRec.get("TZ_JF_JM_JE")==null?0.00:Double.parseDouble(fkPlanRec.get("TZ_JF_JM_JE").toString());
						double bqys = fkPlanRec.get("TZ_JF_BQYS")==null?0.00:Double.parseDouble(fkPlanRec.get("TZ_JF_BQYS").toString());
						double bqss = fkPlanRec.get("TZ_JF_BQSS")==null?0.00:Double.parseDouble(fkPlanRec.get("TZ_JF_BQSS").toString());
						double bqyt = fkPlanRec.get("TZ_JF_BQYT")==null?0.00:Double.parseDouble(fkPlanRec.get("TZ_JF_BQYT").toString());
						String zb = "0";
						if(!xfStandard.equals("0")){
							zb = String.format("%.2f",(jfbz/Double.parseDouble(xfStandard))*100)+"%";
						}
						String jfStatus = fkPlanRec.get("TZ_JF_STAT")==null?"":fkPlanRec.get("TZ_JF_STAT").toString();
						String jfPLID = fkPlanRec.get("TZ_JFPL_ID")==null?"":fkPlanRec.get("TZ_JFPL_ID").toString();
						String TZ_JF_TYPE = fkPlanRec.get("TZ_JF_TYPE")==null?"":fkPlanRec.get("TZ_JF_TYPE").toString();
						String TZ_REMARKS = fkPlanRec.get("TZ_REMARKS")==null?"":fkPlanRec.get("TZ_REMARKS").toString();
						Map<String, Object> returnJsonMap = new HashMap<String, Object>();
						returnJsonMap.put("TZ_JFPL_ID", jfPLID);
						returnJsonMap.put("TZ_JF_DATE", jfDate);
						returnJsonMap.put("TZ_JF_BZ_JE", jfbz);
						returnJsonMap.put("TZ_JF_TZ_JE", xftz);
						returnJsonMap.put("zb", zb);
						returnJsonMap.put("TZ_JF_JM_JE", jmJine);
						returnJsonMap.put("TZ_JF_BQYT", bqyt);
						returnJsonMap.put("TZ_JF_TYPE", TZ_JF_TYPE);
						returnJsonMap.put("TZ_JF_BQSS", bqss);
						returnJsonMap.put("TZ_JF_BQYS", bqys);
						returnJsonMap.put("TZ_JF_STAT", jfStatus);
						returnJsonMap.put("TZ_REMARKS", TZ_REMARKS);
						listData.add(returnJsonMap);
					}
				}
				mapRet.replace("total", total);
				mapRet.replace("root", listData);
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0]="1";
			errorMsg[1]=e.toString();
		}
		return jacksonUtil.Map2json(mapRet);

	}

	private String tzFormSave(String strParams, String[] errorMsg){
		String msg = ""; //返回消息
		String tzOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		Map<String ,Object> resultMap = new HashMap<>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			jacksonUtil.json2Map(strParams);
			String payPlan = "",appInsIds = "";
			if(jacksonUtil.containsKey("payPlan")) {
				payPlan = jacksonUtil.getString("payPlan");
			}
			if(jacksonUtil.containsKey("appInsIds")) {
				appInsIds = jacksonUtil.getString("appInsIds");
			}
			System.out.println("appInsIds======================>"+appInsIds);
			//学籍状态
			String xjstatus = jacksonUtil.getString("xjstatus");
			//奖学金ID
			String jxjDataList =jacksonUtil.getString("jxjList");
			//奖学金说明
			String explanation =jacksonUtil.getString("explanation");

			System.out.println("jxjDataList======================>"+jxjDataList);

			//msg+= admissionAndPayImpl.setSinglePayPlan(appInsIds,payPlan,tzOprid);

			long  appInsId = Long.parseLong(appInsIds);
			String stuId = sqlQuery.queryForObject("SELECT TZ_STU_BH FROM TZ_KSHSTU_GX_T WHERE TZ_APP_INS_ID=?", new Object[]{appInsId}, "String");
			String ssql ="";
			if(stuId!=null &&!"".equals(stuId)){
				ssql = "UPDATE tzms_stu_defn_t SET tzms_stu_xjzt=? WHERE tzms_stu_defn_tid=?";
				//更新学籍状态 
				sqlQuery.update(ssql,new Object[]{xjstatus,stuId});
			}
			//更新奖学金
			jxjDataList = jxjDataList.substring(1,jxjDataList.length()-1);
			//获取数据库中该学生已有的奖学金
			//ArrayList<String> stuHasJxjSet = this.getJxjList(appInsId);
			//前台传过来的奖学金
			Set<String> transjxjSet = new HashSet<>();
			//需要删除的奖学金编号
			//ArrayList<String> transDelJxjSet = new ArrayList<>();
			if(jxjDataList!=null && !"".equals(jxjDataList)){
				String[] jxjArray = jxjDataList.split(",");
				for (int i = 0; i < jxjArray.length; i++) {
					String jxjId = jxjArray[i];
					jxjId = jxjId.replaceAll("'", "").trim(); //11 19 25 12     '11','19','25','10'
					transjxjSet.add(jxjId);  
				}
			} 
			List<String> scholarshipList = new ArrayList<>();
			if(!transjxjSet.isEmpty()){
				for(String jxjId:transjxjSet){
					scholarshipList.add(jxjId);
				}
			}
			JSONArray payPlanJSONArray = JSONArray.fromObject(payPlan);
			msg += admissionAndPayImpl.savePayPlanAPI(appInsIds, payPlanJSONArray,"single",errorMsg);
			msg +=admissionAndPayImpl.saveScholarshipAPI(appInsIds,scholarshipList,"single",errorMsg,"",explanation);
			msg += admissionAndPayImpl.refreshPayPlanAPI(appInsIds,errorMsg);
		}catch(Exception e){
			e.printStackTrace();
		}
		resultMap.put("msg",msg);
		return jacksonUtil.Map2json(resultMap);
	}

	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		String strRet = "";
		try{
			switch (oprType) {
			case "tzFormSave":
				strRet = this.tzFormSave(strParams,errorMsg);
				break;
			default:
				break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return strRet;
	}
	public ArrayList<String> getJxjList(long appInsId){
		ArrayList<String> returnJxjSet = new ArrayList<>();
		String sql = "SELECT TZ_JXJ_ID FROM TZ_KS_JXJ_T WHERE TZ_APP_INS_ID =?";
		List<Map<String, Object>> jxjList = sqlQuery.queryForList(sql, new Object[]{appInsId});
		if(CollectionUtils.isNotEmpty(jxjList)){
			for(Map<String, Object> jxjMap:jxjList){
				returnJxjSet.add(String.valueOf(jxjMap.get("TZ_JXJ_ID")));
			}
		}
		return returnJxjSet;
	}
	
	public static void main(String[] args) {
		
		System.out.println(String.format("%.2f",(0.0/100.0)*100.0)+"%");
	}


}

