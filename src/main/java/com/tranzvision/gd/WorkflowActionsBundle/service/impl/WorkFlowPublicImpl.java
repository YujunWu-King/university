package com.tranzvision.gd.WorkflowActionsBundle.service.impl;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.util.sql.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 
 * @ClassName: WorkFlowPublicImpl
 * @author caoy
 * @version 1.0
 * @Create Time: 2019年1月14日 下午7:14:18
 * @Description: 工作流常用的方法
 */
@Service
public class WorkFlowPublicImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private HttpServletRequest request;

	

	/**
	 * 
	 * @Description: 由JAVA当前登陆人 得到Dynamics登陆人ID
	 * @Create Time: 2019年1月14日 下午7:15:13
	 * @author caoy
	 * @return
	 */
	public String getDynamicsLoginer() {
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		if (oprid == null || oprid.equals("")) {
			return null;
		} else {
			String sql = "SELECT tzms_user_uniqueid FROM tzms_tea_defn_tBase WHERE tzms_oprid=?";
			String dynamicID = sqlQuery.queryForObject(sql, new Object[] { oprid }, "String");

			return dynamicID;
		}
	}

	/**
	 * 
	 * @Description: 获取新建dynamics实例页面的URL
	 * @Create Time: 2019年1月15日 下午3:12:04
	 * @author caoy
	 * @param tzms_wfcldn_tid
	 *            业务流程ID
	 * @return
	 */
	public String getNewWflinsWindowsURL(String tzms_wfcldn_tid) {

		String url = "";
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT A.tzms_entity_name,B.tzms_wfstp_form_id,C.ObjectTypeCode ");
		sb.append("FROM tzms_wfcldn_tBase A,tzms_wflstp_tBase B,entity C");
		sb.append(" WHERE cast(A.tzms_wfcldn_tid  as varchar(36))=cast(B.tzms_wfcldn_uniqueid as varchar(36))");
		sb.append(" AND B.tzms_stptype='1' ");
		sb.append(" AND C.Name=A.tzms_entity_name ");
		sb.append(" AND cast(A.tzms_wfcldn_tid  as varchar(36))=?");

		Map<String, Object> map = sqlQuery.queryForMap(sb.toString(), new Object[] { tzms_wfcldn_tid});
		if (map != null) {
			String entityName = map.get("tzms_entity_name") != null ? map.get("tzms_entity_name").toString() : "";
			String formId = map.get("tzms_wfstp_form_id") != null ? map.get("tzms_wfstp_form_id").toString() : "";
			String code = map.get("ObjectTypeCode") != null ? map.get("ObjectTypeCode").toString() : "";
			if (!entityName.equals("")) {
				String dynamicUrl = sqlQuery.queryForObject(
						"select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",
						new Object[] { "DynamicUrl" }, "String");
				// https://crmdev.saif.sjtu.edu.cn/main.aspx?etc=10016&pagetype=entityrecord&ID=%7b0C613ACF-5315-E911-A826-D4428765BA51%7d&navbar=off&cmdbar=false
				if (!dynamicUrl.endsWith("/")) {
					dynamicUrl = dynamicUrl + "/";
				}
				// extraqs=primarycontactid={43b58571-eefa-e311-80c1-00155d2a68c4}&primarycontactidname=Yvonne
				// McKay (sample)
				//String param = "etc=" + code + "&pagetype=entityrecord&navbar=off&cmdbar=false&extraqs=formid="
				//		+ formId;
				try {

					sb = new StringBuffer();
					sb.append(dynamicUrl);
					sb.append("main.aspx?etc=");
					sb.append(code);
					sb.append("&pagetype=entityrecord&navbar=off&cmdbar=false&extraqs=");
					String extraqs = "formid=" + formId;
					sb.append(URLEncoder.encode(extraqs, "UTF-8"));

					url = sb.toString();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return url;
	}

	/**
	 * @Description: 获取新建dynamics实例页面的URL 携带表单参数
	 * @Create Time: 2019年7月16日 上午10:28:09
	 * @param tzms_wfcldn_tid
	 * @param parameterMap  需带入表单的参数
	 * @return
	 */
	public String getNewWflinsWindowsURLForParameter(String tzms_wfcldn_tid,Map<String,Object> parameterMap){
		String url = "";
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT A.tzms_entity_name,B.tzms_wfstp_form_id,C.ObjectTypeCode ");
		sb.append("FROM tzms_wfcldn_tBase A,tzms_wflstp_tBase B,entity C");
		sb.append(" WHERE cast(A.tzms_wfcldn_tid  as varchar(36))=cast(B.tzms_wfcldn_uniqueid as varchar(36))");
		sb.append(" AND B.tzms_stptype='1' ");
		sb.append(" AND C.Name=A.tzms_entity_name ");
		sb.append(" AND cast(A.tzms_wfcldn_tid  as varchar(36))=?");

		Map<String, Object> map = sqlQuery.queryForMap(sb.toString(), new Object[] { tzms_wfcldn_tid});
		if (map != null) {
			String entityName = map.get("tzms_entity_name") != null ? map.get("tzms_entity_name").toString() : "";
			String formId = map.get("tzms_wfstp_form_id") != null ? map.get("tzms_wfstp_form_id").toString() : "";
			String code = map.get("ObjectTypeCode") != null ? map.get("ObjectTypeCode").toString() : "";
			if (!entityName.equals("")) {
				String dynamicUrl = sqlQuery.queryForObject(
						"select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",
						new Object[] { "DynamicUrl" }, "String");
				// https://crmdev.saif.sjtu.edu.cn/main.aspx?etc=10016&pagetype=entityrecord&ID=%7b0C613ACF-5315-E911-A826-D4428765BA51%7d&navbar=off&cmdbar=false
				if (!dynamicUrl.endsWith("/")) {
					dynamicUrl = dynamicUrl + "/";
				}
				// extraqs=primarycontactid={43b58571-eefa-e311-80c1-00155d2a68c4}&primarycontactidname=Yvonne
				// McKay (sample)
				//String param = "etc=" + code + "&pagetype=entityrecord&navbar=off&cmdbar=false&extraqs=formid="
				//		+ formId;
				try {

					sb = new StringBuffer();
					sb.append(dynamicUrl);
					sb.append("main.aspx?etc=");
					sb.append(code);
					sb.append("&pagetype=entityrecord&navbar=off&cmdbar=false&extraqs=");
					String extraqs = "formid=" + formId;
					if(parameterMap!=null){
						for (Map.Entry<String, Object> parameter : parameterMap.entrySet()) {
							String key = parameter.getKey();
							Object value = parameter.getValue();
							if(key.length() > 0 && value != null && String.valueOf(value).length() > 0){
								extraqs += "&" + key + "=" +String.valueOf(value);
							}
						}
					}
					sb.append(URLEncoder.encode(extraqs, "UTF-8"));

					url = sb.toString();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return url;
	}



	/**
	 * 
	 * @Description:获取编辑dynamics实例新页面的URL
	 * @Create Time: 2019年1月15日 下午3:50:00
	 * @author caoy
	 * @param entityName
	 *            流程关联的实体名
	 * @param tzms_wflstp_tid
	 *            业务流程步骤 ID
	 * @param entityId
	 *            流程实例关联的实体实例ID
	 * @param tzms_wflinsid
	 *            流程实例 ID
	 * @param entityId
	 *            流程步骤实例ID
	 * @return
	 */
	public String getWflinsWindowsURL(String entityName, String tzms_wflstp_tid, String entityId, String tzms_wflinsid,
			String tzms_stpinsid) {
		String url = "";
		String dynamicUrl = sqlQuery.queryForObject(
				"select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?", new Object[] { "DynamicUrl" },
				"String");

		if (!dynamicUrl.endsWith("/")) {
			dynamicUrl = dynamicUrl + "/";
		}

		String code = sqlQuery.queryForObject("select ObjectTypeCode from entity where Name=?",
				new Object[] { entityName }, "String");

		String formId = sqlQuery.queryForObject(
				"select tzms_wfstp_form_id from tzms_wflstp_tBase where tzms_wflstp_tid=?",
				new Object[] { tzms_wflstp_tid }, "String");
		// https://crmdev.saif.sjtu.edu.cn/main.aspx?etc=10016&pagetype=entityrecord&ID=%7b0C613ACF-5315-E911-A826-D4428765BA51%7d&navbar=off&cmdbar=false

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(dynamicUrl);
			sb.append("main.aspx?etc=");
			sb.append(code);
			sb.append("&pagetype=entityrecord&ID=");
			sb.append(URLEncoder.encode("{" + entityId + "}", "UTF-8"));
			sb.append("&navbar=off&cmdbar=false&extraqs=");
			String extraqs = "formid=" + formId + "&tzms_wflinsid=" + tzms_wflinsid + "&tzms_stpinsid=" + tzms_stpinsid;
			sb.append(URLEncoder.encode(extraqs, "UTF-8"));
			url = sb.toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}
	
	
	
	
	/**
	 * 获取编辑dynamics实例新页面的URL
	 * @param entityName		流程关联的实体名
	 * @param tzms_wflstp_tid	业务流程步骤 ID
	 * @param entityId			流程实例关联的实体实例ID
	 * @param tzms_wflinsid		流程实例 ID
	 * @param tzms_stpinsid		流程步骤实例ID
	 * @param paramVal			其他参数值
	 * @return
	 */
	public String getWflinsWindowsURLWithParam(String entityName, String tzms_wflstp_tid, String entityId, String tzms_wflinsid, 
			String tzms_stpinsid, String paramVal) {
		String url = "";
		String dynamicUrl = sqlQuery.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?", 
				new Object[] { "DynamicUrl" }, "String");

		if (!dynamicUrl.endsWith("/")) {
			dynamicUrl = dynamicUrl + "/";
		}

		String code = sqlQuery.queryForObject("select ObjectTypeCode from entity where Name=?",
				new Object[] { entityName }, "String");

		String formId = sqlQuery.queryForObject("select tzms_wfstp_form_id from tzms_wflstp_tBase where tzms_wflstp_tid=?",
				new Object[] { tzms_wflstp_tid }, "String");

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(dynamicUrl);
			sb.append("main.aspx?etc=");
			sb.append(code);
			sb.append("&pagetype=entityrecord&ID=");
			sb.append(URLEncoder.encode("{" + entityId + "}", "UTF-8"));
			sb.append("&navbar=off&cmdbar=false&extraqs=");
			
			String extraqs = "formid=" + formId + "&tzms_wflinsid=" + tzms_wflinsid + "&tzms_stpinsid=" + tzms_stpinsid + "&tzms_param=" + paramVal;
			sb.append(URLEncoder.encode(extraqs, "UTF-8"));
			url = sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}
}
