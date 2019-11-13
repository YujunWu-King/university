package com.tranzvision.gd.TZCreateClueBundle.service.impl;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl;
import com.tranzvision.gd.util.adfs.TZAdfsLoginObject;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.httpclient.CommonUtils;
import com.tranzvision.gd.util.session.TzSession;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 线索相关处理类
 * @author zhongcg
 * 2019年7月12日
 *
 */
@Service("com.tranzvision.gd.TZCreateClueBundle.service.impl.TzCreateClueServiceImpl")
public class TzCreateClueServiceImpl  extends FrameworkImpl {
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private TZGDObject tzGDObject;
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		if("CREATECLUE".equals(oprType)) {
			return createClue(strParams, errorMsg);
		}
		if("CREATECLUE2".equals(oprType)) {
			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strParams);
			//姓名
			String name = jacksonUtil.getString("name");
			//手机
			String phone = jacksonUtil.getString("phone");
			//公司
			String company = jacksonUtil.getString("company");
			//职位
			String position = jacksonUtil.getString("position");
			//咨询内容
			String desc = jacksonUtil.getString("desc");
			//推荐人姓名
			String recName = jacksonUtil.getString("recName");
			//推荐人ID
			String recSaifid = jacksonUtil.getString("recSaifid");
			//渠道：招生网站
			String sql = "select tzms_qddy_xsgl_tid from tzms_qddy_xsgl_t where tzms_label_name like ?";
			//String channel = sqlQuery.queryForObject(sql, new Object[] {"%招生网站%"}, "String");
			//省份
			String area = jacksonUtil.getString("area");
			//如果没有省份，则根据手机号查询所属省份
			if(StringUtils.isEmpty(area)) {
				area = getProvinceByPhone(phone);
			}
			//邮箱
			String email = jacksonUtil.getString("email");
			//线索创建方式：用户注册
			int tzms_rsfcreate_way = 919120000;
			//是否移动设备访问
			boolean isMobile = CommonUtils.isMobile(request);
			//创建媒介：默认电脑端
			int create_media = 919120001;
			sql = "select tzms_xsgl_xsly_tid from tzms_xsgl_xsly_t where tzms_xsgl_xsly_id = ?";
			//线索来源定义
			String tzms_source_id = sqlQuery.queryForObject(sql, new Object[] {"sou004"}, "String");
			
			//线索渠道定义
			sql = "select tzms_channel_id from tzms_xsgl_xsly_tBase where tzms_xsgl_xsly_tid=?";
			String tzms_channel_id = sqlQuery.queryForObject(sql, new Object[] {tzms_source_id}, "String");
			
			String theme = name + "-用户注册";
			if(isMobile) {
				//tzms_source_id = sqlQuery.queryForObject(sql, new Object[] {"003"}, "String");
				create_media = 919120000;
			}
			
			return createClue2(name, phone, company, position, desc, recName, recSaifid, tzms_channel_id, area, tzms_rsfcreate_way, email, create_media, tzms_source_id, theme, 919120001, "", false);
		}
		if("sendMessage".equals(oprType)) {
			return sendEmailOrSmsHistory(strParams, errorMsg);
		}
		if("sendEmail".equals(oprType)) {
			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strParams);
			String ownerId = jacksonUtil.getString("ownerId");
			String leadid = jacksonUtil.getString("leadid");
			String sql = "select tzms_oprid,tzms_email from tzms_tea_defn_t where tzms_user_uniqueid = ?";
			Map<String, Object> map = sqlQuery.queryForMap(sql, new Object[] {ownerId});
			String oprid = "", email2 = "";
			if(map != null) {
				oprid = map.get("tzms_oprid") == null ? "" : map.get("tzms_oprid").toString();
				email2 = map.get("tzms_email") == null ? "" : map.get("tzms_email").toString();
			}
			send(oprid, leadid, email2);
			
			setMainOprid(leadid, ownerId);
		}
		if("backClue".equals(oprType)) {
			return backClue(strParams, errorMsg);
		}
		if("acceptClue".equals(oprType)) {
			return acceptClue(strParams, errorMsg);
		}
		if("autoAssign".equals(oprType)) {
			return autoAssign(strParams, errorMsg);
		}
		if("noticeSale".equals(oprType)) {
			return noticeSale(strParams, errorMsg);
		}
		if("loadConflict".equals(oprType)) {
			return loadConflict(strParams, errorMsg);
		}
		if("setConflictLead".equals(oprType)) {
			return setConflictLead(strParams, errorMsg);
		}
		
		return super.tzOther(oprType, strParams, errorMsg);
	}

	/**
	 * 分派线索结束，判断改手机号还是否属于有冲突线索，如果不是，将是否有冲突设置为否
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String setConflictLead(String strParams, String[] errorMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			jacksonUtil.json2Map(strParams);
			String leadId = jacksonUtil.getString("leadid");
			String sql = "select MobilePhone from lead where leadid = ?";
			String mobilePhone = sqlQuery.queryForObject(sql, new Object[] {leadId}, "String");
			sql = "select DISTINCT A.mobilephone FROM LEAD A JOIN LEAD B ON A.ownerid != B.ownerid WHERE A.mobilephone = B.mobilephone AND A.mobilephone=?";
			String isHasConflict = sqlQuery.queryForObject(sql, new Object[] {mobilePhone}, "String");
			if(StringUtils.isEmpty(isHasConflict)) {
				sql = "UPDATE LEAD SET tzms_owner_conf = 0 WHERE mobilephone = ?";
				sqlQuery.update(sql, new Object[] {mobilePhone});
			}else {
				sql = "UPDATE LEAD SET tzms_owner_conf = 1 WHERE leadid = ?";
				sqlQuery.update(sql, new Object[] {leadId});
			}
			
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return null;
	}

	/**
	 * 自动查找当前线索所有电话号码相同，责任人不同的线索，标记为是否冲存在分配人冲突为‘是’
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String loadConflict(String strParams, String[] errorMsg) {
		try {
			String sql = "UPDATE LEAD SET tzms_owner_conf = 1 WHERE mobilephone IN (select DISTINCT A.mobilephone FROM LEAD A JOIN LEAD B ON A.ownerid != B.ownerid WHERE A.mobilephone = B.mobilephone)";
			sqlQuery.update(sql);
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return null;
	}

	/**
	 * 通知销售
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String noticeSale(String strParams, String[] errorMsg) {
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			jacksonUtil.json2Map(strParams);
			String leadIds = jacksonUtil.getString("leadIds");
			String[] leadArr = leadIds.split(",");
			String param = "";
			if(leadArr != null && leadArr.length > 0) {
				for (String lead : leadArr) {
					param += "'" + lead + "',";
				}
			}
			param = param.substring(0, param.length() - 1);
			
			sendSomeEmail(param);

		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return null;
	}

	//发送整合后的邮件
	public void sendSomeEmail(String leads) throws ParseException, TzSystemException {
		System.out.println("leads:" + leads);
		//首先根据ownerid进行排序
		String sql = "select leadid,fullname,mobilephone,CreatedOn,companyname,jobtitle,tzms_industry,tzms_province,tzms_program_type,tzms_course,description,tzms_remarks,tzms_source_id,ownerId,tzms_xsly_xq from leadbase where leadid in (" + leads + ") order by ownerid" ;
		//线索id，姓名，手机，咨询日期，公司，职务，行业，省份，项目类型，咨询课程，咨询内容，备注，来源名称，负责人
		String leadid = "", fullname = "", mobilephone = "", CreatedOn = "", companyname = "", jobtitle = "",
				tzms_industry = "", tzms_province = "", tzms_program_type = "",  tzms_course = "", description = "",
						tzms_remarks = "", tzms_source_id = "", ownerId = "", tzms_xsly_xq = "";
		List<Map<String, Object>> leadList = sqlQuery.queryForList(sql);
		System.out.println("leadList:" + leadList.size());
		if(leadList != null && leadList.size() > 0) {
			String tmpOwnerId = "";
			int count = 0;
			String trHtml = "";
			for (Map<String, Object> map : leadList) {
				if(map != null) {
					leadid = map.get("leadid") == null ? "" : map.get("leadid").toString();
					fullname = map.get("fullname") == null ? "" : map.get("fullname").toString();
					mobilephone = map.get("mobilephone") == null ? "" : map.get("mobilephone").toString();
					CreatedOn = map.get("CreatedOn") == null ? "" : map.get("CreatedOn").toString();
					//+8小时
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					if(StringUtils.isNotEmpty(CreatedOn)) {
						Date date = sdf.parse(CreatedOn);
						Calendar cal = Calendar.getInstance();
						cal.setTime(date);
						cal.add(Calendar.HOUR_OF_DAY, 8);
						CreatedOn =  cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DAY_OF_MONTH)
						 + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + (cal.get(Calendar.MINUTE) < 9 ? "0"+cal.get(Calendar.MINUTE) : cal.get(Calendar.MINUTE)) + ":" + cal.get(Calendar.SECOND);
					}
					companyname = map.get("companyname") == null ? "" : map.get("companyname").toString();
					jobtitle = map.get("jobtitle") == null ? "" : map.get("jobtitle").toString();
					tzms_industry = map.get("tzms_industry") == null ? "" : map.get("tzms_industry").toString();
					tzms_province = map.get("tzms_province") == null ? "" : map.get("tzms_province").toString();
					tzms_program_type = map.get("tzms_program_type") == null ? "" : map.get("tzms_program_type").toString();
					sql = "SELECT field_label FROM tzms_combo_option_v WHERE Field_Name = 'tzms_program_type' AND Field_Value=?";
					tzms_program_type = sqlQuery.queryForObject(sql, new Object[] {tzms_program_type}, "String");
					
					tzms_course = map.get("tzms_course") == null ? "" : map.get("tzms_course").toString();
					description = map.get("description") == null ? "" : map.get("description").toString();
					tzms_remarks = map.get("tzms_remarks") == null ? "" : map.get("tzms_remarks").toString();
					tzms_source_id = map.get("tzms_source_id") == null ? "" : map.get("tzms_source_id").toString();
					sql = "select tzms_label_name from TZMS_XSGL_XSLY_T where TZms_XSGL_XSLY_TId=?";
					tzms_source_id = sqlQuery.queryForObject(sql, new Object[] {tzms_source_id}, "String");
					
					ownerId = map.get("ownerId") == null ? "" : map.get("ownerId").toString();
					tzms_xsly_xq = map.get("tzms_xsly_xq") == null ? "" : map.get("tzms_xsly_xq").toString();
					System.out.println("tmpOwnerId:" + tmpOwnerId);
					System.out.println("ownerId:" + ownerId);
					//属于同一个负责人
					if(tmpOwnerId.equals(ownerId) || count == 0) {
						tmpOwnerId = ownerId;
						count++;
						trHtml += tzGDObject.getHTMLText("HTML.TZCreateClueBundle.TZ_EMAIL_TR_HTML", true, fullname, mobilephone, CreatedOn,
								companyname, jobtitle, tzms_industry, tzms_province, tzms_program_type, tzms_course, description, tzms_remarks,
								tzms_source_id, tzms_xsly_xq);
					}else {//负责人变化了，就给之前的负责人发送邮件
						String html = tzGDObject.getHTMLText("HTML.TZCreateClueBundle.TZ_EMAIL_TABLE_HTML", true, trHtml);
						//发送邮件
						sql = "select tzms_oprid,tzms_email from tzms_tea_defn_t where tzms_user_uniqueid = ?";
						Map<String, Object> map2 = sqlQuery.queryForMap(sql, new Object[] {tmpOwnerId});
						String oprid = "", email2 = "";
						if(map2 != null) {
							oprid = map2.get("tzms_oprid") == null ? "" : map2.get("tzms_oprid").toString();
							email2 = map2.get("tzms_email") == null ? "" : map2.get("tzms_email").toString();
						}
						send(oprid, "", email2, html);	
						
						count++;
						trHtml = tzGDObject.getHTMLText("HTML.TZCreateClueBundle.TZ_EMAIL_TR_HTML", true, fullname, mobilephone, CreatedOn,
								companyname, jobtitle, tzms_industry, tzms_province, tzms_program_type, tzms_course, description, tzms_remarks,
								tzms_source_id, tzms_xsly_xq);
						tmpOwnerId = ownerId;
					}
					//遍历结束也直接发送邮件
					if(count == leadList.size()) {
						String html = tzGDObject.getHTMLText("HTML.TZCreateClueBundle.TZ_EMAIL_TABLE_HTML", true, trHtml);
						//发送邮件
						sql = "select tzms_oprid,tzms_email from tzms_tea_defn_t where tzms_user_uniqueid = ?";
						Map<String, Object> map2 = sqlQuery.queryForMap(sql, new Object[] {tmpOwnerId});
						String oprid = "", email2 = "";
						if(map2 != null) {
							oprid = map2.get("tzms_oprid") == null ? "" : map2.get("tzms_oprid").toString();
							email2 = map2.get("tzms_email") == null ? "" : map2.get("tzms_email").toString();
						}
						send(oprid, "", email2, html);	
					}
				}
			}
		}
	}

	/**
	 * 根据手机号获得省份
	 * @param phone
	 * @param area
	 * @return
	 */
	public String getProvinceByPhone(String phone) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		String area = "";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("http://mobsec-dianhua.baidu.com/dianhua_api/open/location?tel=" + phone);
	    CloseableHttpResponse response = null;
	    try {
	        response = httpclient.execute(httpget);
	    } catch (IOException e1) {
	        e1.printStackTrace();
	    }
	    String result = null;
	    try {
	        HttpEntity entity = response.getEntity();
	        if (entity != null) {
	            result = EntityUtils.toString(entity);
	            jacksonUtil.json2Map(result);
	            Map<String, Object> map = (Map<String, Object>) jacksonUtil.getMap("response").get(phone);
	            Map<String, Object> map2 = (Map<String, Object>) map.get("detail");
	            area = map2.get("province").toString();
	        }
	    } catch (Exception e) {
	        //e.printStackTrace();
	    } finally {
	        try {
	            response.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
		return area;
	}

	private String autoAssign(String strParams, String[] errorMsg) {
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			jacksonUtil.json2Map(strParams);

			String leadIds = jacksonUtil.getString("leadIds");
			String[] leadArr = leadIds.split(",");
			String querySql = "select MobilePhone,tzms_province,tzms_referee_name,tzms_saifid,tzms_saifid_name from lead where leadid = ?";
			String updateSql = "update leadbase set ownerId = ? where leadid=?";
			String sql = "select tzms_oprid,tzms_email from tzms_tea_defn_t where tzms_user_uniqueid = ?";
			if(leadArr != null && leadArr.length > 0) {
				for (String lead : leadArr) {
					Map<String, Object> map = sqlQuery.queryForMap(querySql, new Object[] {lead});
					String MobilePhone = "", tzms_province = "", tzms_referee_name = "", tzms_saifid = "";
					if(map != null) {
						MobilePhone = map.get("MobilePhone") == null ? "" : map.get("MobilePhone").toString();
						tzms_province = map.get("tzms_province") == null ? "" : map.get("tzms_province").toString();
						//如果没有省份，则根据手机号查询所属省份
						if(StringUtils.isEmpty(tzms_province)) {
							tzms_province = getProvinceByPhone(MobilePhone);
						}
						tzms_referee_name = map.get("tzms_referee_name") == null ? "" : map.get("tzms_referee_name").toString();
						tzms_saifid = map.get("tzms_saifid") == null ? "" : map.get("tzms_saifid").toString();
						String ownerid = getOwnerId2(lead, tzms_saifid, tzms_referee_name, MobilePhone, tzms_province);
						sqlQuery.update(updateSql, new Object[] {ownerid, lead});
						
						Map<String, Object> map2 = sqlQuery.queryForMap(sql, new Object[] {ownerid});
						String oprid = "", email2 = "";
						if(map2 != null) {
							oprid = map2.get("tzms_oprid") == null ? "" : map2.get("tzms_oprid").toString();
							email2 = map2.get("tzms_email") == null ? "" : map2.get("tzms_email").toString();
						}
						send(oprid, lead, email2);
						
						setMainOprid(lead, ownerid);
					}
				}
			}

		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return null;
	}

	/**
	 * 线索分配的时候，如果这个线索有报名表，需要同步修改这个报名表的责任人为线索的责任人
	 * @param lead 线索id
	 * @param ownerid 负责人
	 */
	private void setMainOprid(String lead, String ownerid) {
		String sql;
		sql = "select B.tzms_app_ins_id,B.tzms_edp_wrk_tId from lead A, tzms_edp_wrk_t B where A.tzms_ksh_name = B.tzms_edp_wrk_tid AND A.LEADID=?";
		Map<String, Object> map = sqlQuery.queryForMap(sql, new Object[] {lead});
		String appinsid = "", tzms_edp_wrk_tId = "";
		if(map != null) {
			appinsid = map.get("tzms_app_ins_id") == null ? "" : map.get("tzms_app_ins_id").toString();
			tzms_edp_wrk_tId = map.get("tzms_edp_wrk_tId") == null ? "" : map.get("tzms_edp_wrk_tId").toString();
			if(StringUtils.isNotEmpty(appinsid)) {
				sql = "select tzms_oprid from tzms_tea_defn_t where tzms_user_uniqueid=?";
				String oprid = sqlQuery.queryForObject(sql, new Object[] {ownerid}, "String");
				String updateSql = "update ps_tz_form_wrk_t set MAIN_OPRID = ? where TZ_APP_INS_ID=?";
				sqlQuery.update(updateSql, new Object[] {oprid, appinsid});
				updateSql = "update tzms_edp_wrk_tbase set OwnerId = ? where tzms_edp_wrk_tId=?";
				sqlQuery.update(updateSql, new Object[] {ownerid, tzms_edp_wrk_tId});
			}
		}
	}

	private String acceptClue(String strParams, String[] errorMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			jacksonUtil.json2Map(strParams);

			String leadIds = jacksonUtil.getString("leadIds");
			String selectReason = jacksonUtil.getString("selectReason");
			String[] leadArr = leadIds.split(",");
			String sql = "update lead set tzms_lead_status = 919120002,tzms_gjzt = ? where leadid=?";
			String sql2 = "select ownerId from lead where leadid=?";
			if(leadArr != null && leadArr.length > 0) {
				TZAdfsLoginObject tzADFSObject = new TZAdfsLoginObject();
		        String dynURL = "";
				String dynUserName = "";
				String dynUserPswd = "";
				String domain = "";
				try {
					dynURL = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_LOGIN_URL");
					dynUserName = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_USER_NAME");
					dynUserPswd = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_USER_PSWD");
					domain = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_DOMAIN");
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
				boolean flag = false;
				String result = "";
				
				//模拟登陆
				tzADFSObject.setDynamicsLoginPrarameters(dynURL, dynUserName, dynUserPswd, -1);
				
				Map<String, Object> paramMap = new HashMap<>();
				for (String lead : leadArr) {
					lead = lead.replace("{", "").replace("}", "");
					String ownerid = sqlQuery.queryForObject(sql2, new Object[] {lead}, "String");
					sqlQuery.update(sql, new Object[] {selectReason, lead});
					paramMap.put("tzms_lead_id@odata.bind", "/leads(" + lead + ")");
					paramMap.put("tzms_lead_owner@odata.bind", "/systemusers(" + ownerid + ")");
					paramMap.put("tzms_leadlog_type", 919120001);
					paramMap.put("tzms_lead_opp", "接受");
					String url = domain + "/api/data/v8.2/tzms_lead_log_ts";
					String postData = jacksonUtil.Map2json(paramMap);
					System.out.println("postData:" + postData);
					try {
						flag= tzADFSObject.callDynamicsCRMWebAPIForJSONDataWithJSONData(url, postData);
						result = tzADFSObject.getWebAPIResult();
						System.out.println("result:" + result);
					} catch (Exception e1) {
						e1.printStackTrace();
						result = tzADFSObject.getErrorMessage();
					}
				}
			}

		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return null;
	}

	//批量退回线索
	private String backClue(String strParams, String[] errorMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			jacksonUtil.json2Map(strParams);

			String leadIds = jacksonUtil.getString("leadIds");
			int reason = jacksonUtil.getInt("reason");
			//String selectBackOwner = jacksonUtil.getString("selectBackOwner");
			String tzmsBackDesc = jacksonUtil.getString("tzmsBackDesc");
			String[] leadArr = leadIds.split(",");
			String sql = "update lead set tzms_lead_status = 919120005, tzms_thyy_id=?, tzms_back_descr=?  where leadid=?";
			if(leadArr != null && leadArr.length > 0) {
				TZAdfsLoginObject tzADFSObject = new TZAdfsLoginObject();
		        String dynURL = "";
				String dynUserName = "";
				String dynUserPswd = "";
				String domain = "";
				try {
					dynURL = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_LOGIN_URL");
					dynUserName = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_USER_NAME");
					dynUserPswd = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_USER_PSWD");
					domain = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_DOMAIN");
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
				boolean flag = false;
				String result = "";
				
				//模拟登陆
				tzADFSObject.setDynamicsLoginPrarameters(dynURL, dynUserName, dynUserPswd, -1);
				
				Map<String, Object> paramMap = new HashMap<>();
				for (String lead : leadArr) {
					lead = lead.replace("{", "").replace("}", "");
					sqlQuery.update(sql, new Object[] { reason, tzmsBackDesc, lead});
					paramMap.put("tzms_lead_id@odata.bind", "/leads(" + lead + ")");
					//paramMap.put("tzms_lead_owner@odata.bind", "/systemusers(" + selectBackOwner + ")");
					paramMap.put("tzms_log_description", tzmsBackDesc);
					paramMap.put("tzms_leadlog_type", reason);
					paramMap.put("tzms_lead_opp", "退回");
					String url = domain + "/api/data/v8.2/tzms_lead_log_ts";
					String postData = jacksonUtil.Map2json(paramMap);
					System.out.println("postData:" + postData);
					try {
						flag= tzADFSObject.callDynamicsCRMWebAPIForJSONDataWithJSONData(url, postData);
						result = tzADFSObject.getWebAPIResult();
						System.out.println("result:" + result);
					} catch (Exception e1) {
						e1.printStackTrace();
						result = tzADFSObject.getErrorMessage();
					}
				}
			}

		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return null;
	}

	private String sendEmailOrSmsHistory(String strParams, String[] errorMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			jacksonUtil.json2Map(strParams);

			String EmailOrSmsTmpID = "", bmrGuid = "", psnType = "", tmpMenuId = "";

			// 要跳转的菜单ID
			tmpMenuId = jacksonUtil.getString("tmpMenuId");
			// 人员的Guid
			bmrGuid = jacksonUtil.getString("bmrGuid");
			String[] bmrGuidArray = bmrGuid.split(",");
			// 要使用的邮件或短信模板
			EmailOrSmsTmpID = jacksonUtil.getString("EmailOrSmsTmpID");
			// 人员类型：A-学生，B-教师
			psnType = jacksonUtil.getString("psnType");

			String TZ_MENUID = sqlQuery.queryForObject(
					"select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?", new Object[] { tmpMenuId },
					"String");

			TzSession tzSession = new TzSession(request);

			tzSession.addSession("bmrGuid", bmrGuidArray);
			tzSession.addSession("EmailOrSmsTmpID", EmailOrSmsTmpID);
			tzSession.addSession("psnType", psnType);

			String mode = "no-inquire", model = "content";

			String headIndex = request.getContextPath() + "/index";

			headIndex = headIndex + "?mode=" + mode;
			headIndex = headIndex + "&model=" + model;

			String tmpUrl = headIndex + TZ_MENUID;

			response.sendRedirect(tmpUrl);

		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return null;
	}

	/**
	 * 创建线索
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String createClue(String strParams, String[] errorMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		try {
			jacksonUtil.json2Map(strParams);
			//姓名
			String ims_name = request.getParameter("ims_name");
			//手机
			String ims_phone = request.getParameter("ims_phone");
			//公司
			String ims_company = request.getParameter("ims_company");
			//职位
			String ims_position = request.getParameter("ims_position");
			//项目类型  EMBA/GES/EE
			String ims_program_type = request.getParameter("ims_program_type");
			//意向课程名称
			String ims_course = request.getParameter("ims_course");
			//咨询内容
			String ims_desc = request.getParameter("ims_desc");
			//推荐人姓名
			String ims_recname = request.getParameter("ims_recname");
			//推荐人SAIFID
			String ims_resphone = request.getParameter("ims_resphone");
			//渠道   10:SAIF官网PC版   11:SAIF官网手机版   12:EED订阅号（上海高级金融学院E通讯）
			String ims_channel_code = request.getParameter("ims_channel_code");
			String sql = "select tzms_qddy_xsgl_tid from TZMS_QDDY_XSGL_T where tzms_xsgl_xsqd_id=?";
			String tz_msqddy_xsgl_tid = sqlQuery.queryForObject(sql, new Object[] {ims_channel_code}, "String");
			
			//如果没有姓名和手机不产生线索
			if(StringUtils.isEmpty(ims_name) && StringUtils.isEmpty(ims_phone))return null;
			
			TZAdfsLoginObject tzADFSObject = new TZAdfsLoginObject();
	        String dynURL = "";
			String dynUserName = "";
			String dynUserPswd = "";
			String domain = "";
			try {
				dynURL = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_LOGIN_URL");
				dynUserName = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_USER_NAME");
				dynUserPswd = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_USER_PSWD");
				domain = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_DOMAIN");
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			boolean flag = false;
			String result = "";
			
			//模拟登陆
			tzADFSObject.setDynamicsLoginPrarameters(dynURL, dynUserName, dynUserPswd, -1);
			
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("fullname", ims_name);
			paramMap.put("lastname", ims_name);
			paramMap.put("mobilephone", ims_phone);
			paramMap.put("companyname", ims_company);
			paramMap.put("jobtitle", ims_position);
			paramMap.put("tzms_referee_name", ims_recname);
			paramMap.put("tzms_program_type", ims_program_type);
			paramMap.put("tzms_course", ims_course);
			if(StringUtils.isNotEmpty(ims_resphone)) {
				paramMap.put("tzms_edp_tjr@odata.bind", "/tzms_edp_tjr_ts(" + ims_resphone + ")");
			}
			paramMap.put("description", ims_desc);
			if(StringUtils.isNotEmpty(tz_msqddy_xsgl_tid)) {
				paramMap.put("tzms_channel_id@odata.bind", "/tzms_qddy_xsgl_ts(" + tz_msqddy_xsgl_tid + ")");
			}
			
			
			//分配负责人  分配规则，按推荐人SAIFID先匹配，在按姓名匹配，手机匹配，在按区域匹配，最后按固定角色匹配
			String ownerId = getOwnerId(ims_resphone, ims_recname, ims_phone, "");
			if(StringUtils.isNotEmpty(ownerId)) {
				paramMap.put("ownerid@odata.bind", "/systemusers(" + ownerId + ")");
			}
			Date now = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			String tzms_requiredate = sdf.format(now);
			paramMap.put("tzms_requiredate", tzms_requiredate + "Z");
			
			String url = domain + "/api/data/v8.2/leads";
			String postData = jacksonUtil.Map2json(paramMap);
			System.out.println("postData:" + postData);
			try {
				flag= tzADFSObject.callDynamicsCRMWebAPIForJSONDataWithJSONData(url, postData);
				result = tzADFSObject.getWebAPIResult();
			} catch (Exception e1) {
				e1.printStackTrace();
				result = tzADFSObject.getErrorMessage();
				mapRet.put("result_code", "99");
				mapRet.put("result", e1.getMessage());
				return jacksonUtil.Map2json(mapRet);
			}
			System.out.println("result:" + result);
			sql = "select tzms_oprid,tzms_email from tzms_tea_defn_t where tzms_user_uniqueid = ?";
			Map<String, Object> map = sqlQuery.queryForMap(sql, new Object[] {ownerId});
			String oprid = "", email = "";
			if(map != null) {
				oprid = map.get("tzms_oprid") == null ? "" : map.get("tzms_oprid").toString();
				email = map.get("tzms_email") == null ? "" : map.get("tzms_email").toString();
			}
			jacksonUtil.json2Map(result);
			String leadid = jacksonUtil.getString("leadid");
			send(oprid, leadid, email);
			mapRet.put("result_code", "0");
			return jacksonUtil.Map2json(mapRet);
		} catch (Exception e) {
			e.printStackTrace();
			mapRet.put("result_code", "99");
			mapRet.put("result", e.getMessage());
			return jacksonUtil.Map2json(mapRet);
		}
		
	}
	
	/**
	 * 
	 * @param name 姓名
	 * @param phone 手机
	 * @param company 公司
	 * @param position 职位
	 * @param desc 咨询内容
	 * @param recName 推荐人姓名
	 * @param recSaifid 推荐人Saifid
	 * @param channel 渠道
	 * @param area 区域
	 * @param tzms_rsfcreate_way 创建方式
	 * 	在线报名  919120005
	 *	用户注册 919120000
	 *	批量导入 919120001
	 *	手工创建 919120002
	 * 	资料索取 919120003
	 *	定制课程咨询 919120004
	 * @param tzms_source_id 线索来源
	 * @param create_media 创建媒介
	 * @param email 邮箱
	 * @param theme 主题
	 * @param colour_sort_id 咨询类别
	 * @param act_name 活动名称（线索来源详情）
	 * @param isAct 是否是活动创建
	 * @return
	 */
	public String createClue2(String name, String phone, String company, String position, String desc, String recName, String recSaifid, String channel, String area, int tzms_rsfcreate_way, String email, int create_media, String tzms_source_id, String theme, int colour_sort_id, String act_name, boolean isAct) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		try {
			String sql = "select tzms_qddy_xsgl_tid from TZMS_QDDY_XSGL_T where tzms_xsgl_xsqd_id=?";
			String tz_msqddy_xsgl_tid = sqlQuery.queryForObject(sql, new Object[] {channel}, "String");
			
			//如果没有姓名和手机不产生线索
			if(StringUtils.isEmpty(name) && StringUtils.isEmpty(phone))return null;
			
			TZAdfsLoginObject tzADFSObject = new TZAdfsLoginObject();
	        String dynURL = "";
			String dynUserName = "";
			String dynUserPswd = "";
			String domain = "";
			try {
				dynURL = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_LOGIN_URL");
				dynUserName = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_USER_NAME");
				dynUserPswd = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_USER_PSWD");
				domain = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_DOMAIN");
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			boolean flag = false;
			String result = "";
			
			//模拟登陆
			tzADFSObject.setDynamicsLoginPrarameters(dynURL, dynUserName, dynUserPswd, -1);
			
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("subject", theme);
			paramMap.put("lastname", name);
			paramMap.put("mobilephone", phone);
			paramMap.put("companyname", company);
			paramMap.put("jobtitle", position);
			paramMap.put("tzms_referee_name", recName);
			paramMap.put("tzms_province", area);
			paramMap.put("emailaddress1", email);
			paramMap.put("tzms_xsly_xq", act_name);
			//创建媒介
			paramMap.put("tzms_create_media", create_media);
			//咨询类别
			paramMap.put("tzms_colour_sort_id", colour_sort_id);
			//创建方式
			if(tzms_rsfcreate_way >= 919120000 && tzms_rsfcreate_way <= 919120006) {
				paramMap.put("tzms_rsfcreate_way", tzms_rsfcreate_way);
			}
			//线索来源
			if(StringUtils.isNotEmpty(tzms_source_id)) {
				paramMap.put("tzms_source_id@odata.bind", "/tzms_xsgl_xsly_ts(" + tzms_source_id + ")");
			}
			/*if(StringUtils.isNotEmpty(recSaifid)) {
				paramMap.put("tzms_edp_tjr@odata.bind", "/tzms_edp_tjr_ts(" + recSaifid + ")");
			}*/
			paramMap.put("description", desc);
			//线索渠道
			if(StringUtils.isNotEmpty(channel)) {
				paramMap.put("tzms_channel_id@odata.bind", "/tzms_qddy_xsgl_ts(" + channel + ")");
			}
			
			//推荐人SAIFID,并且是内部营销人员(systemuserid),接口中如果有saifid并且是销售，需要把这个saifid以及对应的人员姓名也记录下来
			sql = "select A.tzms_user_uniqueid, A.tzms_teaname_list from tzms_tea_defn_t A,tzms_edp_tjr_t B where A.tzms_domain_zhid=? AND A.tzms_user_uniqueid=B.tzms_user AND B.tzms_is_fzr=1";
			if(StringUtils.isNotEmpty(recSaifid)) {
				Map<String, Object> saifMap = sqlQuery.queryForMap(sql, new Object[] {recSaifid});
				if(saifMap != null) {
					String tzms_teaname_list = saifMap.get("tzms_teaname_list") == null ? "" : saifMap.get("tzms_teaname_list").toString();
					paramMap.put("tzms_saifid", recSaifid);
					paramMap.put("tzms_saifid_name", tzms_teaname_list);
				}
			}
			
			//分配负责人  分配规则，按推荐人SAIFID先匹配，在按姓名匹配，手机匹配，在按区域匹配，最后按固定角色匹配
			String ownerId = getOwnerId(recSaifid, name, phone, area);
			if(StringUtils.isNotEmpty(ownerId)) {
				paramMap.put("ownerid@odata.bind", "/systemusers(" + ownerId + ")");
			}
			
			Date now = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			String tzms_requiredate = sdf.format(now);
			paramMap.put("tzms_requiredate", tzms_requiredate + "Z");
			
			String url = domain + "/api/data/v8.2/leads";
			String postData = jacksonUtil.Map2json(paramMap);
			System.out.println("postData:" + postData);
			try {
				flag= tzADFSObject.callDynamicsCRMWebAPIForJSONDataWithJSONData(url, postData);
				result = tzADFSObject.getWebAPIResult();
			} catch (Exception e1) {
				e1.printStackTrace();
				result = tzADFSObject.getErrorMessage();
				mapRet.put("result_code", "99");
				mapRet.put("result", e1.getMessage());
				return jacksonUtil.Map2json(mapRet);
			}
			System.out.println("result:" + result);
			sql = "select tzms_oprid,tzms_email from tzms_tea_defn_t where tzms_user_uniqueid = ?";
			Map<String, Object> map = sqlQuery.queryForMap(sql, new Object[] {ownerId});
			String oprid = "", email2 = "";
			if(map != null) {
				oprid = map.get("tzms_oprid") == null ? "" : map.get("tzms_oprid").toString();
				email2 = map.get("tzms_email") == null ? "" : map.get("tzms_email").toString();
			}
			jacksonUtil.json2Map(result);
			String leadid = jacksonUtil.getString("leadid");
			//如果是活动创建，不需要单封发送邮件，活动那边发送合并邮件
			if(!isAct && StringUtils.isNotEmpty(leadid)) {
				send(oprid, leadid, email2);
			}
			
			mapRet.put("result_code", "0");
			mapRet.put("lead", leadid);
			return jacksonUtil.Map2json(mapRet);
		} catch (Exception e) {
			e.printStackTrace();
			mapRet.put("result_code", "99");
			mapRet.put("result", e.getMessage());
			return jacksonUtil.Map2json(mapRet);
		}
	}
	
	/**
	 * 获取区域负责人 分配规则，按推荐人SAIFID先匹配，在按姓名匹配，手机匹配，在按区域匹配，最后按固定角色匹配
	 * @param ims_resphone SAIFID
	 * @param ims_name 姓名
	 * @param ims_phone 手机
	 * @param area 区域
	 * @return ownerId
	 */
	public String getOwnerId(String ims_resphone, String recName, String ims_phone, String area) {
		String ownerId = "";
		try {
			//推荐人SAIFID先匹配,并且是内部营销人员(systemuserid)
			//String sql = "select tzms_user from tzms_edp_tjr_t where tzms_edp_tjr_tId = ? and tzms_is_fzr = 1";
			String sql = "select A.tzms_user_uniqueid from tzms_tea_defn_t A,tzms_edp_tjr_t B where A.tzms_domain_zhid=? AND A.tzms_user_uniqueid=B.tzms_user AND B.tzms_is_fzr=1";
			if(StringUtils.isNotEmpty(ims_resphone)) {
				ownerId = sqlQuery.queryForObject(sql, new Object[] {ims_resphone}, "String");
			}
			if(StringUtils.isNotEmpty(ownerId)) {
				return ownerId;
			}
			//根据姓名匹配,并且是内部营销人员(systemuserid)
			sql = "select top 1 tzms_user from tzms_edp_tjr_t where tzms_name = ? and tzms_is_fzr = 1";
			ownerId = sqlQuery.queryForObject(sql, new Object[] {recName}, "String");
			if(StringUtils.isNotEmpty(ownerId)) {
				return ownerId;
			}
			//根据手机号匹配
			sql = "select top 1 A.ownerid from lead A,owner B where A.mobilephone = ? AND A.ownerid = B.ownerid AND B.NAME NOT IN ('CRMAdmin','EEDAdmin','张露','王力鹏') order by A.CreatedOn desc";
			ownerId = sqlQuery.queryForObject(sql, new Object[] {ims_phone}, "String");
			if(StringUtils.isNotEmpty(ownerId)) {
				//查询查询该负责人是否有超过72小时以上未跟进的线索
				/*boolean flag = isHas72Lead(ownerId);
				if(flag) {
					ownerId = "";
				}else {
				}*/
				return ownerId;
			}
			int num = 0;
			//根据区域匹配,如果有多个负责人，随机取一条,做到线索平均分配给销售人员
			sql = "select top 1 B.tzms_user_id from tzms_salesarea_t A, tzms_salesareapsn_t B,tzms_salesareapsn_t_tzms_salesarea_tBase C WHERE A.tzms_name =? AND C.tzms_salesarea_tid= A.tzms_salesarea_tId AND B.tzms_salesareapsn_tId=C.tzms_salesareapsn_tid ORDER BY NEWID()";
			//sql = "select top 1 B.tzms_user_id,(SELECT count(*) from lead where OwnerId = B.tzms_user_id) num from tzms_salesarea_t A, tzms_salesareapsn_t B,tzms_salesareapsn_t_tzms_salesarea_tBase C WHERE A.tzms_name = ? AND C.tzms_salesarea_tid= A.tzms_salesarea_tId AND B.tzms_salesareapsn_tId=C.tzms_salesareapsn_tid ORDER BY num";
			ownerId = sqlQuery.queryForObject(sql, new Object[] {area}, "String");
			while(StringUtils.isNotEmpty(ownerId)) {
				num++;
				//查询该负责人是否有超过72小时以上未跟进的线索
				boolean flag = isHas72Lead(ownerId);
				if(flag) {
					ownerId = sqlQuery.queryForObject(sql, new Object[] {area}, "String");
				}else {
					return ownerId;
				}
				//循环超过20退出循环，避免死循环
				if(num >= 20)break;
			}
			//根据固定角色匹配
			sql = "select tzms_user_uniqueid from tzms_tea_defn_t where tzms_oprid= (select top 1 ROLEUSER from PSROLEUSER where ROLENAME=?)";
			ownerId = sqlQuery.queryForObject(sql, new Object[] {"SAIF_TZ_LEAD_ADMIN"}, "String");
			if(StringUtils.isNotEmpty(ownerId)) {
				return ownerId;
			}
		} catch (Exception e) {
			
		}
		return null;
	}
	/**
	 * 获取区域负责人 分配规则，按推荐人SAIFID先匹配，在按姓名匹配，手机匹配，在按区域匹配，最后按固定角色匹配
	 * @param ims_resphone SAIFID
	 * @param ims_name 姓名
	 * @param ims_phone 手机
	 * @param area 区域
	 * @return ownerId
	 */
	public String getOwnerId2(String leadid, String ims_resphone, String recName, String ims_phone, String area) {
		String ownerId = "";
		try {
			//推荐人SAIFID先匹配,并且是内部营销人员(systemuserid)
			//String sql = "select tzms_user from tzms_edp_tjr_t where tzms_edp_tjr_tId = ? and tzms_is_fzr = 1";
			String sql = "select A.tzms_user_uniqueid from tzms_tea_defn_t A,tzms_edp_tjr_t B where A.tzms_domain_zhid=? AND A.tzms_user_uniqueid=B.tzms_user AND B.tzms_is_fzr=1";
			if(StringUtils.isNotEmpty(ims_resphone)) {
				ownerId = sqlQuery.queryForObject(sql, new Object[] {ims_resphone}, "String");
			}
			if(StringUtils.isNotEmpty(ownerId)) {
				return ownerId;
			}
			//根据姓名匹配,并且是内部营销人员(systemuserid)
			sql = "select top 1 tzms_user from tzms_edp_tjr_t where tzms_name = ? and tzms_is_fzr = 1";
			ownerId = sqlQuery.queryForObject(sql, new Object[] {recName}, "String");
			if(StringUtils.isNotEmpty(ownerId)) {
				return ownerId;
			}
			//根据手机号匹配
			sql = "select top 1 A.ownerid from lead A,owner B where A.mobilephone = ? and A.leadid <> ? AND A.ownerid = B.ownerid AND B.NAME NOT IN ('CRMAdmin','EEDAdmin','张露','王力鹏') order by A.CreatedOn desc";
			ownerId = sqlQuery.queryForObject(sql, new Object[] {ims_phone, leadid}, "String");
			if(StringUtils.isNotEmpty(ownerId)) {
				//查询查询该负责人是否有超过72小时以上未跟进的线索
				boolean flag = isHas72Lead(ownerId);
				/*if(flag) {
					ownerId = "";
				}else {
				}*/
				return ownerId;
			}
			
			int num = 0;
			//根据区域匹配,如果有多个负责人，随机取一条
			sql = "select top 1 B.tzms_user_id from tzms_salesarea_t A, tzms_salesareapsn_t B,tzms_salesareapsn_t_tzms_salesarea_tBase C WHERE A.tzms_name =? AND C.tzms_salesarea_tid= A.tzms_salesarea_tId AND B.tzms_salesareapsn_tId=C.tzms_salesareapsn_tid ORDER BY NEWID()";
			//sql = "select top 1 B.tzms_user_id,(SELECT count(*) from lead where OwnerId = B.tzms_user_id) num from tzms_salesarea_t A, tzms_salesareapsn_t B,tzms_salesareapsn_t_tzms_salesarea_tBase C WHERE A.tzms_name = ? AND C.tzms_salesarea_tid= A.tzms_salesarea_tId AND B.tzms_salesareapsn_tId=C.tzms_salesareapsn_tid ORDER BY num";
			ownerId = sqlQuery.queryForObject(sql, new Object[] {area}, "String");
			while(StringUtils.isNotEmpty(ownerId)) {
				num++;
				//查询查询该负责人是否有超过72小时以上未跟进的线索
				boolean flag = isHas72Lead(ownerId);
				if(flag) {
					ownerId = sqlQuery.queryForObject(sql, new Object[] {area}, "String");
				}else {
					return ownerId;
				}
				//循环超过20退出循环，避免死循环
				if(num >= 20)break;
			}
			//根据固定角色匹配
			sql = "select tzms_user_uniqueid from tzms_tea_defn_t where tzms_oprid= (select top 1 ROLEUSER from PSROLEUSER where ROLENAME=?)";
			ownerId = sqlQuery.queryForObject(sql, new Object[] {"SAIF_TZ_LEAD_ADMIN"}, "String");
			if(StringUtils.isNotEmpty(ownerId)) {
				return ownerId;
			}
		} catch (Exception e) {
			
		}
		return null;
	}
	/**
	 * 如果销售堆积的线索存量（从09-01后产生的）超过50条没跟进，之后的自动分配的线索就不分配给这个销售了
	 * @param ownerId
	 */
	private boolean isHas50Lead(String ownerId) {
		String sql = "select count(*) from lead where ownerid = ? and tzms_lead_status = 919120001 and DATEDIFF(day, '2019-09-01 00:00:00', CreatedOn) > 0";
		int count = sqlQuery.queryForObject(sql, new Object[] {ownerId}, "int");
		if(count > 50)return true;
		return false;
	}
	
	/**
	 * 如果该销售有在2019-10-15日往后超过72小时并且状态是已分配的，之后的自动分配的线索就不分配给这个销售了
	 * @param ownerId
	 */
	private boolean isHas72Lead(String ownerId) {
		String overTime = getHardCodePoint.getHardCodePointVal("TZ_LEAD_OVERTIME");
		int time = 0;
		if(StringUtils.isNotEmpty(overTime)) {
			time = Integer.parseInt(overTime);
		}
		//东八区，减八小时
		String sql = "select count(*) from lead where ownerid = ? and tzms_lead_status = 919120001 and DATEDIFF(hour, CreatedOn, GETDATE()) > ? and DATEDIFF(day, '2019-10-15 08:00:00', CreatedOn) > 0";
		int count = sqlQuery.queryForObject(sql, new Object[] {ownerId, (time-8)}, "int");
		if(count > 0)return true;
		return false;
	}

	public String send(String oprid, String LeadId, String email2){
		System.out.println("LeadId:" + LeadId);
		System.out.println("email2:" + email2);
        // 创建邮件短信发送任务
        String strTaskId = createTaskServiceImpl.createTaskInsTiming("SAIF", "TZ_LEAD_ASSIGN", "MAL", "A");
        if (strTaskId == null || "".equals(strTaskId)) {
            return "false1";
        }
        // 创建短信、邮件发送的听众;
        String createAudience = createTaskServiceImpl.createAudienceTiming(strTaskId, "SAIF", "线索分配提醒",
                "");
        if ("".equals(createAudience) || createAudience == null) {
            return "false2";
        }
        if(StringUtils.isEmpty(email2)) {
        	return null;
        }
        boolean addAudCy = createTaskServiceImpl.addAudCy(createAudience, "", "", "", "", email2,
                "", "", oprid, "", "",LeadId);
        if (!addAudCy) {
            return "false3";
        }
        String emails = sqlQuery.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",  new Object[]{"TZ_LEAD_EMAIL"}, "String");
        if(StringUtils.isNotEmpty(emails)) {
        	createTaskServiceImpl.addCCAddr(strTaskId, emails);
        }
        // 得到创建的任务ID
        if ("".equals(strTaskId) || strTaskId == null) {
            return "false4";
        } else {
            // 发送邮件
            System.out.println("strTaskId==============="+strTaskId);
            sendSmsOrMalServiceImpl.send(strTaskId, "");
            return "true";
        }

    }
	public String send(String oprid, String LeadId, String email2, String content){
		System.out.println("LeadId:" + LeadId);
		System.out.println("email2:" + email2);
		// 创建邮件短信发送任务
		String strTaskId = createTaskServiceImpl.createTaskIns("SAIF", "TZ_LEAD_ASSIGN", "MAL", "A");
		if (strTaskId == null || "".equals(strTaskId)) {
			return "false1";
		}
		// 创建短信、邮件发送的听众;
		String createAudience = createTaskServiceImpl.createAudience(strTaskId, "SAIF", "线索分配提醒",
				"");
		if ("".equals(createAudience) || createAudience == null) {
			return "false2";
		}
		if(StringUtils.isEmpty(email2)) {
			return null;
		}
		boolean addAudCy = createTaskServiceImpl.addAudCy(createAudience, "", "", "", "", email2,
				"", "", oprid, "", "",LeadId);
		if (!addAudCy) {
			return "false3";
		}
		String emails = sqlQuery.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",  new Object[]{"TZ_LEAD_EMAIL"}, "String");
		if(StringUtils.isNotEmpty(emails)) {
			createTaskServiceImpl.addCCAddr(strTaskId, emails);
		}
		createTaskServiceImpl.updateEmailSendContent(strTaskId, content);
		// 得到创建的任务ID
		if ("".equals(strTaskId) || strTaskId == null) {
			return "false4";
		} else {
			// 发送邮件
			System.out.println("strTaskId==============="+strTaskId);
			sendSmsOrMalServiceImpl.send(strTaskId, "");
			return "true";
		}
		
	}
	
	/**
	 * 线索创建失败，发送错误信息给开发人员
	 * @param emails 邮箱
	 * @param content 邮件内容
	 * @return 
	 */
	public String sendErrorEmail(String emails, String content){
        // 创建邮件短信发送任务
        String strTaskId = createTaskServiceImpl.createTaskIns("SAIF", "TZ_LEAD_ASSIGN", "MAL", "A");
        if (strTaskId == null || "".equals(strTaskId)) {
            return "false1";
        }
        // 创建短信、邮件发送的听众;
        String createAudience = createTaskServiceImpl.createAudience(strTaskId, "SAIF", "线索创建失败提醒", "");
        if ("".equals(createAudience) || createAudience == null) {
            return "false2";
        }
        if(StringUtils.isNotEmpty(emails)) {
        	String[] emailArr = emails.split(";");
        	for (String email : emailArr) {
        		createTaskServiceImpl.addAudCy(createAudience, "", "", "", "", email, "", "", "", "", "", "");
			}
        }
        createTaskServiceImpl.updateEmailSendTitle(strTaskId, "线索创建失败提醒");
        createTaskServiceImpl.updateEmailSendContent(strTaskId, content);
        // 得到创建的任务ID
        if ("".equals(strTaskId) || strTaskId == null) {
            return "false4";
        } else {
            // 发送邮件
            System.out.println("strTaskId==============="+strTaskId);
            sendSmsOrMalServiceImpl.send(strTaskId, "");
            return "true";
        }

    }
	/**
	 * 通用邮件发送
	 * @param emails 邮箱
	 * @param content 邮件内容
	 * @return 
	 */
	public String sendEmail(String sendHardcode, String title, String content){
		// 创建邮件短信发送任务
		String strTaskId = createTaskServiceImpl.createTaskIns("SAIF", "TZ_LEAD_ASSIGN", "MAL", "A");
		if (strTaskId == null || "".equals(strTaskId)) {
			return "false1";
		}
		// 创建短信、邮件发送的听众;
		String createAudience = createTaskServiceImpl.createAudience(strTaskId, "SAIF", "线索创建失败提醒", "");
		if ("".equals(createAudience) || createAudience == null) {
			return "false2";
		}
		String emails = sqlQuery.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",  new Object[]{sendHardcode}, "String");
		if(StringUtils.isNotEmpty(emails)) {
			String[] emailArr = emails.split(";");
			for (String email : emailArr) {
				createTaskServiceImpl.addAudCy(createAudience, "", "", "", "", email, "", "", "", "", "", "");
			}
		}
		
		createTaskServiceImpl.updateEmailSendTitle(strTaskId, title);
		createTaskServiceImpl.updateEmailSendContent(strTaskId, content);
		// 得到创建的任务ID
		if ("".equals(strTaskId) || strTaskId == null) {
			return "false4";
		} else {
			// 发送邮件
			System.out.println("strTaskId==============="+strTaskId);
			sendSmsOrMalServiceImpl.send(strTaskId, "");
			return "true";
		}
		
	}
}
