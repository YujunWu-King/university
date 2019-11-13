package com.tranzvision.gd.workflow.method;

import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预开票审批流程
 * @author xzp
 * 2019年7月10日
 *
 */
public class TzAdvanceInvoiceApproval {

	private SqlQuery sqlQuery;

	private TZGDObject tzGDObject;

	private GetSeqNum getSeqNum;

	private CreateTaskServiceImpl createTaskServiceImpl;

	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;


	public TzAdvanceInvoiceApproval() {
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
		tzGDObject = (TZGDObject) getSpringBeanUtil.getAutowiredSpringBean("TZGDObject");
		getSeqNum = (GetSeqNum) getSpringBeanUtil.getAutowiredSpringBean("GetSeqNum");
		this.createTaskServiceImpl = (CreateTaskServiceImpl) getSpringBeanUtil.getSpringBeanByID("com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl");
		this.sendSmsOrMalServiceImpl = (SendSmsOrMalServiceImpl) getSpringBeanUtil.getSpringBeanByID("com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl");
	}
	/**
	 * 
	 * @param preinvoiceId  预审批流程ID
	 * @return
	 */
	public String judgeIsOver(String wflInsId, String stpInsId,String preinvoiceId){
		//1 申请人在流程申请单填写的“开票金额”，在提交申请时，校验填写的开票金额+已开票的金额不能大于合同总金额；
		//返回的信息
		String msg = "";
		String ssql = "";
		//开票金额
		String kpJine = "";
		//立项编号
		String lxbh = "";
		String isAgree = "";
		try{
			ssql = "SELECT tzms_kp_jine,tzms_pro_lxbh,tzms_is_agree FROM tzms_preinvoice_tBase WHERE tzms_preinvoice_tid = ?";
			Map<String,Object>  preinvoiceMap = sqlQuery.queryForMap(ssql, new Object[]{preinvoiceId});
			if(preinvoiceMap!=null){
				lxbh = preinvoiceMap.get("tzms_pro_lxbh") == null?"":preinvoiceMap.get("tzms_pro_lxbh").toString();
				kpJine = preinvoiceMap.get("tzms_kp_jine") == null?"":preinvoiceMap.get("tzms_kp_jine").toString();
				isAgree = preinvoiceMap.get("tzms_is_agree") == null?"":preinvoiceMap.get("tzms_is_agree").toString();
			}
			double kpJineNum = 0;
			if(!"".equals(kpJine)){
				kpJineNum = Double.parseDouble(kpJine);
			}
			//根据立项编号获取到账记录，在获取开票信息
			ssql = "SELECT SUM(TZ_KP_JINE) FROM TZ_DZJL_TBL A JOIN TZ_KPJL_TBL B ON A.TZ_DZ_ID = B.TZ_DZ_ID WHERE A.TZ_OBJ_ID=? AND A.TZ_OBJ_TYPE='2'";
			String  totalKpJIne =  sqlQuery.queryForObject(ssql, new Object[]{lxbh}, "String");
			double  totalKpJIneNum = 0;
			if(totalKpJIne != null && !"".equals(totalKpJIne)){
				totalKpJIneNum = Double.parseDouble(totalKpJIne);
			}
			//获得合同总金额
			ssql = "SELECT tzms_ht_jine FROM tzms_lxxmb_def_tBase A JOIN tzms_nxht_def_tBase B ON A.tzms_agreement_unique = B.tzms_nxht_def_tid WHERE tzms_item_num =?";
			String htJine =  sqlQuery.queryForObject(ssql, new Object[]{lxbh}, "String");
			double totalHtJine = 0;
			if(htJine != null && !"".equals(htJine)){
				totalHtJine = Double.parseDouble(htJine);
			}
			if(totalKpJIneNum+kpJineNum>totalHtJine){
				return "{\"checkResult\":false,\"errorMsg\":\"当前开票金额+已开票金额大于合同总金额！\"}";
			}
			if("false".equals(isAgree)){
				return "{\"checkResult\":false,\"errorMsg\":\"是否同意承诺为否，请先同意承诺！\"}";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "{\"checkResult\":true,\"errorMsg\":\"\"}";

	}
	/**
	 * 
	 * @param wflInsId  
	 * @param stpInsId
	 * @param preinvoiceId
	 * @param eventId
	 */
	public void createKpRecAndSendEmail(String wflInsId, String stpInsId,String preinvoiceId,String eventId){
		System.out.println("------ 预审批开票流程同步开票信息表开始执行--------->> "+ wflInsId + " --- " + stpInsId + " --- " + preinvoiceId + " --- " + eventId);

		//流程责任人oprid
		/*String oprid = sqlQuery.queryForObject("select top 1 tzms_oprid from tzms_tea_defn_tBase where tzms_user_uniqueid=(select tzms_stpproid from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?)",
				new Object[] { wflInsId, stpInsId }, "String");*/
		//通过预审批ID得到相关信息将其写入到开票信息表中
		String operator = "";
		List<String> userIdList = sqlQuery.queryForList("select distinct tzms_stpproid from tzms_stpins_tbl A where tzms_wflinsid=? and tzms_stpinsid=?", 
				new Object[] { wflInsId, stpInsId }, "String");
		
		//获取当前步骤人
		if(userIdList != null && userIdList.size() > 0) {
			for(String userId : userIdList) {
				String oprid = sqlQuery.queryForObject("select top 1 tzms_oprid from tzms_tea_defn_tBase where tzms_user_uniqueid=?", 
						new Object[] { userId }, "String");
				if(StringUtils.isNotBlank(oprid)) {
					operator += "".equals(operator) ? oprid : ("，" + oprid);
				}
			}
		}
		String ssql = "";
		try{
			//开票内部编号                
			String kpnbid = ""; 
			//发票号
			String fpno = "";
			//开票金额                                //申请人ID
			String kpJine = ""; String applyUserID = "";
			//开票日期                       //纳税人内部编号  //立项编号
			String kpDate = "",nsrsbh = "",lxbh = "",fyxbh = "";
			String email = "",mobile = "",name = "",tzOprid = "";
			String fpTitle = "";
			ssql = "SELECT tzms_fpno,tzms_kp_jine,tzms_apply_user,tzms_nsrsbh,tzms_pro_lxbh,tzms_fkunit FROM tzms_preinvoice_tBase WHERE tzms_preinvoice_tid = ?";
			Map<String, Object> infoMap = sqlQuery.queryForMap(ssql, new Object[]{preinvoiceId});
			if(infoMap != null){
				fpno = infoMap.get("tzms_fpno") == null?"":String.valueOf(infoMap.get("tzms_fpno"));
				kpJine = infoMap.get("tzms_kp_jine") == null?"":String.valueOf(infoMap.get("tzms_kp_jine"));
				applyUserID = infoMap.get("tzms_apply_user") == null?"":String.valueOf(infoMap.get("tzms_apply_user"));
				nsrsbh = infoMap.get("tzms_nsrsbh") == null?"":String.valueOf(infoMap.get("tzms_nsrsbh"));
				lxbh =  infoMap.get("tzms_pro_lxbh") == null?"":String.valueOf(infoMap.get("tzms_pro_lxbh"));
				fpTitle = infoMap.get("tzms_fkunit") == null?"":String.valueOf(infoMap.get("tzms_fkunit"));
			}
			double kpJineNum = 0;
			if(!"".equals(kpJine)){
				kpJineNum = Double.parseDouble(kpJine);
			}
			fyxbh = sqlQuery.queryForObject("SELECT tzms_fyxbh FROM tzms_lxxmb_def_tBase WHERE tzms_item_num =?", new Object[]{lxbh}, "String");
			//	通过申请人ID得到申请人相关信息，此ID是dynamicID
			Map<String,Object> applyUserMap = sqlQuery.queryForMap("SELECT top 1 tzms_name,tzms_phone,tzms_email,tzms_oprid FROM tzms_tea_defn_tBase WHERE CONVERT(VARCHAR(36),tzms_user_uniqueid) = ?",
					new Object[]{applyUserID});
			if(applyUserMap != null){
				email = applyUserMap.get("tzms_email") == null?"":String.valueOf(applyUserMap.get("tzms_email"));
				name = applyUserMap.get("tzms_name") == null?"":String.valueOf(applyUserMap.get("tzms_name"));
				mobile = applyUserMap.get("tzms_phone") == null?"":String.valueOf(applyUserMap.get("tzms_phone"));
				tzOprid = applyUserMap.get("tzms_oprid") == null?"":String.valueOf(applyUserMap.get("tzms_oprid"));
			}
			//增加开票状态为已开票
			//新增发票抬头
			String insertSql = "INSERT INTO TZ_KPJL_TBL(TZ_KP_NBID,TZ_KP_JINE,TZ_KP_DATE,TZ_KPFY_NO,TZ_FP_NO,TZ_NAME,TZ_MOBILE,TZ_EMAIL,TZ_NSRSB_NO,TZ_FP_TYPE,TZ_KP_OPRID,TZ_KP_TIME,TZ_KP_STATUS,TZ_FP_TITLE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			kpnbid = "KP" + getNum(String.valueOf(getSeqNum.getSeqNum("TZ_KPJL_TBL", "TZ_KP_NBID")),10);
			int count = sqlQuery.update(insertSql,new Object[]{kpnbid,kpJineNum,new Date(),fyxbh,fpno,name,mobile,email,nsrsbh,"2",operator,new Date(),"2",fpTitle});
			if(count==1){
				//更新审批流程的开票人，开票时间，开票内部编号
				ssql = "UPDATE tzms_preinvoice_tBase SET tzms_kpr=?,tzms_kptime=?,tzms_kpnbid=? WHERE tzms_preinvoice_tid =?";
				sqlQuery.update(ssql,new Object[]{operator,new Date(),kpnbid,preinvoiceId});
				//发送邮件
				sendEmailToApplyUser(email,tzOprid,name,wflInsId,stpInsId,preinvoiceId);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 发送邮件给申请人
	 * @param email
	 * @param oprid
	 * @param name
	 * @return 
	 */
	public boolean sendEmailToApplyUser(String email,String oprid,String name,String wflInsId, String stpInsId,String preinvoiceId){
		String strTaskId = createTaskServiceImpl.createTaskInsTiming("SAIF", "TZ_APPLY_EMAIL", "MAL", "A");
		if (strTaskId == null || "".equals(strTaskId)) {
			return false;
		}
		String createAudience = createTaskServiceImpl.createAudienceTiming(strTaskId, "SAIF", "通知申请人开票",
				"");
		if ("".equals(createAudience) || createAudience == null) {
			return false;
		}
		boolean addAudCy = createTaskServiceImpl.addAudCy(createAudience, name, "", "", "", email,
				"", "", oprid, wflInsId, stpInsId,preinvoiceId);
		System.out.println("============="+preinvoiceId);
		if (!addAudCy) {
			return false;
		}
		// 得到创建的任务ID
		if ("".equals(strTaskId) || strTaskId == null) {
			return false;
		} else {
			// 发送邮件
			sendSmsOrMalServiceImpl.send(strTaskId, "");
		}
		return true;

	}
	public static String getNum(String  str,int length){
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < length-str.length(); i++) {
			s.append("0");
		}
		return s.toString()+str;
	}

	public void ensure(String wflInsId, String stpInsId,String preinvoiceId,String eventId){
		try{
			//根据preinvoiceId取内部编号和JR编号
			String ssql = "SELECT tzms_jr_no,tzms_kpnbid FROM tzms_preinvoice_tBase WHERE tzms_preinvoice_tid=?  ";
			Map<String, Object> ssMap = sqlQuery.queryForMap(ssql, new Object[]{preinvoiceId});
			String jrNo = "",kpnbid = "";
			if(ssMap!=null){
				//JR编号
				jrNo= ssMap.get("tzms_jr_no")==null?"":ssMap.get("tzms_jr_no").toString();
				//内部编号
				kpnbid = ssMap.get("tzms_kpnbid")==null?"":ssMap.get("tzms_kpnbid").toString();
			}
			//根据JRbianhao 得到Daozhang编号
			String dzID = sqlQuery.queryForObject("SELECT TZ_DZ_ID FROM TZ_DZJL_TBL WHERE TZ_JR_ID = ?", new Object[]{jrNo},"String");
			//更新开票记录表
			ssql = "UPDATE TZ_KPJL_TBL SET TZ_DZ_ID=? WHERE TZ_KP_NBID=?";
			sqlQuery.update(ssql, new Object[]{dzID,kpnbid});
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
}
