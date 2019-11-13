package com.tranzvision.gd.workflow.method;


import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.SqlQuery;

public class TzEmailModelApp{

	private SqlQuery sqlQuery;
	
	private GetHardCodePoint getHardCodePoint;
	public TzEmailModelApp() {
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		sqlQuery  = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
		getHardCodePoint =(GetHardCodePoint)getSpringBeanUtil.getSpringBeanByID("getHardCodePoint");
	}

	/**
	 * 申请人姓名
	 * @param para
	 * @return
	 */
	public String getApplyStuName(String[] paramters){
		System.out.println("==========START ASYALY EMAIL PARA===========");
		try {
			if(paramters.length!=2){
				return "参数错误";
			}
			String sql = "SELECT TZ_BMB_ID AS tzms_preinvoice_tid FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND  TZ_AUDCY_ID=?";
			String audId = paramters[0];
			String audCyId = paramters[1];
			String preinvoiceId = sqlQuery.queryForObject(sql,new Object[] { audId, audCyId },"String");
			String applyUserID = sqlQuery.queryForObject("SELECT tzms_apply_user FROM tzms_preinvoice_tBase WHERE  CONVERT(VARCHAR(36),tzms_preinvoice_tid)=?", 
					new Object[]{preinvoiceId}, "String");
			String name = sqlQuery.queryForObject("SELECT top 1 tzms_name FROM tzms_tea_defn_tBase WHERE CONVERT(VARCHAR(36),tzms_user_uniqueid) = ?",
					new Object[]{applyUserID},"String");
			System.out.println("==========START ASYALY EMAIL END==========="+name);
			return name;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}

	public String getTaskId(String[] paramters){
		try {
			if(paramters.length!=2){
				return "参数错误";
			}
			String sql = "SELECT TZ_BMB_ID FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND  TZ_AUDCY_ID=?";
			String audId = paramters[0];
			String audCyId = paramters[1];
			String preinvoiceId = sqlQuery.queryForObject(sql,new Object[] { audId, audCyId },"String");
			String taskId = sqlQuery.queryForObject("SELECT tzms_task_number FROM tzms_preinvoice_tBase WHERE  CONVERT(VARCHAR(36),tzms_preinvoice_tid)=?", 
					new Object[]{preinvoiceId}, "String");
			return taskId;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}
	
	public String getDzDate(String[] paramters){
		try {
			if(paramters.length!=2){
				return "参数错误";
			}
			String sql = "SELECT TZ_BMB_ID FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND  TZ_AUDCY_ID=?";
			String audId = paramters[0];
			String audCyId = paramters[1];
			String preinvoiceId = sqlQuery.queryForObject(sql,new Object[] { audId, audCyId },"String");
			String tzms_dkdate = sqlQuery.queryForObject("SELECT CONVERT(VARCHAR(100),tzms_dkdate,23) AS tzms_dkdate FROM tzms_preinvoice_tBase WHERE  CONVERT(VARCHAR(36),tzms_preinvoice_tid)=?", 
					new Object[]{preinvoiceId}, "String");
			return tzms_dkdate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}
	
	public String getTimeInterval(String[] paramters){
		try {
			if(paramters.length!=2){
				return "参数错误";
			}
			String sql = "SELECT TZ_BMB_ID FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND  TZ_AUDCY_ID=?";
			String audId = paramters[0];
			String audCyId = paramters[1];
			String preinvoiceId = sqlQuery.queryForObject(sql,new Object[] { audId, audCyId },"String");
			String tzms_interval = sqlQuery.queryForObject("SELECT DATEDIFF(DAY, CONVERT(VARCHAR(100),GETDATE(),23), CONVERT(VARCHAR(100),tzms_dkdate,23)) AS tzms_interval FROM tzms_preinvoice_tBase  WHERE CONVERT(VARCHAR(36),tzms_preinvoice_tid)=?", 
					new Object[]{preinvoiceId}, "String");
			return tzms_interval;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}

}
