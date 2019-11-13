package com.tranzvision.gd.TZCreateClueBundle.controller;


import com.tranzvision.gd.TZCreateClueBundle.service.impl.TzCreateClueServiceImpl;
import com.tranzvision.gd.util.adfs.TZAdfsLoginObject;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.SqlQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/clue")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TZCreateClueController {
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TzCreateClueServiceImpl tzCreateClueServiceImpl;
	
	/**
	 * 创建线索
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/createClue", method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String createClue(HttpServletRequest request, HttpServletResponse response) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		String param = "技术字段信息：</br>";
		try {
			//姓名
			String ims_name = request.getParameter("ims_name");
			param += "ims_name:" + ims_name + "</br>";
			//手机
			String ims_phone = request.getParameter("ims_phone");
			param += "ims_phone:" + ims_phone + "</br>";
			//根据手机查询最新一条线索的毫秒数，十秒之内无法重复创建线索
			String queryTimesql = "SELECT top 1 DATEDIFF(S,CreatedOn, GETDATE()) from lead WHERE mobilephone = ? order by CreatedOn desc";
			
			String date = sqlQuery.queryForObject(queryTimesql, new Object[] {ims_phone}, "String");
			System.out.println("date:" + date);
			//时间不为空，并且秒数 <=(8*60*60+10),不允许创建线索
			if(StringUtils.isNotEmpty(date) && Integer.parseInt(date) <= 28810) {
				mapRet.put("result_code", "99");
				mapRet.put("result", "同一手机号创建时间相差不到十秒，不允许创建线索！");
				return jacksonUtil.Map2json(mapRet);
			}
			
			//公司
			String ims_company = request.getParameter("ims_company");
			param += "ims_company:" + ims_company + "</br>";
			//职位
			String ims_position = request.getParameter("ims_position");
			param += "ims_position:" + ims_position + "</br>";
			//项目类型  EMBA/GES/EE
			String ims_program_type = request.getParameter("ims_program_type");
			param += "ims_program_type:" + ims_program_type + "</br>";
			if(StringUtils.isNotEmpty(ims_program_type)) {
				ims_program_type = ims_program_type.toUpperCase();
			}
			int program_type = 0;
			if("EMBA".equals(ims_program_type)) {
				program_type = 1;
			}else if("GES".equals(ims_program_type)) {
				program_type = 2;
			}else if("EE".equals(ims_program_type)) {
				program_type = 3;
			}else if("DBA".equals(ims_program_type)) {
				program_type = 2;
			}
			//意向课程名称
			String ims_course = request.getParameter("ims_course");
			param += "ims_course:" + ims_course + "</br>";
			//咨询内容
			String ims_desc = request.getParameter("ims_desc");
			param += "ims_desc:" + ims_desc + "</br>";
			//招生老师姓名
			String ims_recname = request.getParameter("ims_sales_name");
			param += "ims_sales_name:" + ims_recname + "</br>";
			//推荐人SAIFID
			String ims_resphone = request.getParameter("saifid");
			param += "saifid:" + ims_resphone + "</br>";
			//推荐人姓名
			String ims_rec_name = request.getParameter("ims_rec_name");
			param += "ims_rec_name:" + ims_rec_name + "</br>";
			//渠道   10:SAIF官网PC版   11:SAIF官网手机版   12:EED订阅号（上海高级金融学院E通讯）
			String ims_channel_code = request.getParameter("ims_channel_code");
			param += "ims_channel_code:" + ims_channel_code + "</br>";
			String sql = "select tzms_xsgl_xsly_tid from tzms_xsgl_xsly_t where tzms_xsgl_xsly_id=?";
			String tz_msqddy_xsgl_tid = sqlQuery.queryForObject(sql, new Object[] {ims_channel_code}, "String");
			
			//根据来源查询渠道ID
			sql = "select tzms_channel_id from tzms_xsgl_xsly_tBase where tzms_xsgl_xsly_tid=?";
			String tzms_channel_id = sqlQuery.queryForObject(sql, new Object[] {tz_msqddy_xsgl_tid}, "String");
			//来源详情
			String ims_detail_code = URLDecoder.decode(request.getParameter("ims_detail_code")==null ? "":request.getParameter("ims_detail_code"), "utf-8");
			System.out.println("ims_detail_code:" + ims_detail_code);
			param += "ims_detail_code:" + ims_detail_code + "</br>";
			//如果没有姓名和手机不产生线索
			if(StringUtils.isEmpty(ims_name) && StringUtils.isEmpty(ims_phone)) {
				mapRet.put("result_code", "99");
				mapRet.put("result", "姓名和手机为空！");
				return jacksonUtil.Map2json(mapRet);
			}
			
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
			paramMap.put("subject", ims_name + "-外部咨询");
			paramMap.put("fullname", ims_name);
			paramMap.put("lastname", ims_name);
			paramMap.put("mobilephone", ims_phone);
			paramMap.put("companyname", ims_company);
			paramMap.put("jobtitle", ims_position);
			paramMap.put("tzms_referee_name", ims_recname);
			if(program_type != 0) {
				paramMap.put("tzms_program_type", program_type);
			}
			paramMap.put("tzms_course", ims_course);
			paramMap.put("tzms_ims_rec_name", ims_rec_name);
			paramMap.put("tzms_rsfcreate_way", 919120003);
			
			/*if(StringUtils.isNotEmpty(ims_resphone)) {
				paramMap.put("tzms_edp_tjr@odata.bind", "/tzms_edp_tjr_ts(" + ims_resphone + ")");
			}*/
			
			//推荐人SAIFID,并且是内部营销人员(systemuserid),接口中如果有saifid并且是销售，需要把这个saifid以及对应的人员姓名也记录下来
			sql = "select A.tzms_user_uniqueid, A.tzms_teaname_list from tzms_tea_defn_t A,tzms_edp_tjr_t B where A.tzms_domain_zhid=? AND A.tzms_user_uniqueid=B.tzms_user AND B.tzms_is_fzr=1";
			if(StringUtils.isNotEmpty(ims_resphone)) {
				Map<String, Object> saifMap = sqlQuery.queryForMap(sql, new Object[] {ims_resphone});
				if(saifMap != null) {
					String tzms_teaname_list = saifMap.get("tzms_teaname_list") == null ? "" : saifMap.get("tzms_teaname_list").toString();
					paramMap.put("tzms_saifid", ims_resphone);
					paramMap.put("tzms_saifid_name", tzms_teaname_list);
				}
			}
			String province = tzCreateClueServiceImpl.getProvinceByPhone(ims_phone);
			//分配负责人  分配规则，按推荐人SAIFID先匹配，在按姓名匹配，手机匹配，在按区域匹配，最后按固定角色匹配
			String ownerId = tzCreateClueServiceImpl.getOwnerId(ims_resphone, ims_recname, ims_phone, province);
			if(StringUtils.isNotEmpty(ownerId)) {
				paramMap.put("ownerid@odata.bind", "/systemusers(" + ownerId + ")");
			}
			paramMap.put("tzms_province", province);
			paramMap.put("description", ims_desc);
			paramMap.put("tzms_xsly_xq", ims_detail_code);
			if(StringUtils.isNotEmpty(tz_msqddy_xsgl_tid)) {
				paramMap.put("tzms_source_id@odata.bind", "/tzms_xsgl_xsly_ts(" + tz_msqddy_xsgl_tid + ")");
			}
			if(StringUtils.isNotEmpty(tzms_channel_id)) {
				paramMap.put("tzms_channel_id@odata.bind", "/tzms_qddy_xsgl_ts(" + tzms_channel_id + ")");
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
				param += "错误信息：";
				//线索创建失败发送邮件
				String emails = sqlQuery.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",  new Object[]{"TZ_ERROR_EMAIL"}, "String");
				tzCreateClueServiceImpl.sendErrorEmail(emails, param + e1.getMessage() + "</br>" +result);
				mapRet.put("result_code", "99");
				mapRet.put("result", e1.getMessage());
				return jacksonUtil.Map2json(mapRet);
			}
			if(flag) {
				if(tzADFSObject.isRequestSuccess() == true) {
					sql = "select tzms_oprid,tzms_email from tzms_tea_defn_t where tzms_user_uniqueid = ?";
					Map<String, Object> map = sqlQuery.queryForMap(sql, new Object[] {ownerId});
					String oprid = "", email = "";
					if(map != null) {
						oprid = map.get("tzms_oprid") == null ? "" : map.get("tzms_oprid").toString();
						email = map.get("tzms_email") == null ? "" : map.get("tzms_email").toString();
					}
					jacksonUtil.json2Map(result);
					String leadid = jacksonUtil.getString("leadid");
					tzCreateClueServiceImpl.send(oprid, leadid, email);
					mapRet.put("result_code", "0");
					return jacksonUtil.Map2json(mapRet);
				}else {
					flag = false;
					//接口请求发生错误
					String badRequestResult = tzADFSObject.getWebAPIResult();
					//此处错误信息需要根据具体接口返回进行解析，下面只是一个解析示例
					try {
						jacksonUtil.json2Map(badRequestResult);
						if(jacksonUtil.containsKey("error")) {
							String message = jacksonUtil.getMap("error").get("message").toString();
							if(StringUtils.isNotBlank(message)) {
								badRequestResult = message;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					//输出错误消息
					result=badRequestResult;
					param += "错误信息：";
					//线索创建失败发送邮件
					String emails = sqlQuery.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",  new Object[]{"TZ_ERROR_EMAIL"}, "String");
					tzCreateClueServiceImpl.sendErrorEmail(emails, param+result);
				}
			}
			mapRet.put("result_code", "99");
			mapRet.put("result", result);
			return jacksonUtil.Map2json(mapRet);
		} catch (Exception e) {
			e.printStackTrace();
			mapRet.put("result_code", "99");
			mapRet.put("result", e.getMessage());
			//线索创建失败发送邮件
			String emails = sqlQuery.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",  new Object[]{"TZ_ERROR_EMAIL"}, "String");
			tzCreateClueServiceImpl.sendErrorEmail(emails, param + e.getMessage());
			return jacksonUtil.Map2json(mapRet);
		}
	}

	
}
