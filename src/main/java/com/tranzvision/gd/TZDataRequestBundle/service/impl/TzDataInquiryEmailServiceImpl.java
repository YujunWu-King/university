package com.tranzvision.gd.TZDataRequestBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZDataRequestBundle.dao.PsTzDataRequestTMapper;
import com.tranzvision.gd.TZDataRequestBundle.model.PsTzDataRequestT;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.captcha.Patchca;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * @author caoy
 * @version 创建时间：2016年10月10日 下午10:49:33 类说明
 */
@Service("com.tranzvision.gd.TZDataRequestBundle.service.impl.TzDataInquiryEmailServiceImpl")
public class TzDataInquiryEmailServiceImpl extends FrameworkImpl {
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;

	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;

	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;

	@Autowired
	private PsTzDataRequestTMapper psTzDataRequestTMapper;

	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private SqlQuery sqlQuery;

	final String filterPattern = "[<>{}\\[\\];\\&]";

	@Override
	public String tzGetHtmlContent(String strParams) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String, Object> map = new HashMap<String, Object>();
		String msg = "";
		map.put("success", "0");
		map.put("msg", "");

		String strJgid = "ADMIN";

		String language = "ENG";

		String name = request.getParameter("name");
		if (name == null || "".equals(name)) {
			map.replace("success", "1");
			msg = "姓名不能为空";
			map.replace("msg", msg);
			// System.out.println("result:" + jacksonUtil.Map2json(map));
			return jacksonUtil.Map2json(map);
		}

		name = name.replaceAll(filterPattern, "");

		String type = request.getParameter("type");
		if (type == null || "".equals(type)) {
			map.replace("success", "1");
			msg = "type不能为空";
			map.replace("msg", msg);
			// System.out.println("result:" + jacksonUtil.Map2json(map));
			return jacksonUtil.Map2json(map);
		}

		type = type.replaceAll(filterPattern, "");

		String email = request.getParameter("email");
		if (email == null || "".equals(email)) {
			map.replace("success", "1");
			msg = "Email不能为空";
			map.replace("msg", msg);
			// System.out.println("result:" + jacksonUtil.Map2json(map));
			return jacksonUtil.Map2json(map);
		}

		email = email.replaceAll(filterPattern, "");

		String Company = request.getParameter("company");
		if (Company == null || "".equals(Company)) {
			map.replace("success", "1");
			msg = "公司不能为空";
			map.replace("msg", msg);
			// System.out.println("result:" + jacksonUtil.Map2json(map));
			return jacksonUtil.Map2json(map);
		}

		Company = Company.replaceAll(filterPattern, "");

		String phone = request.getParameter("phone");
		if (phone == null || "".equals(phone)) {
			map.replace("success", "1");
			msg = "电话不能为空";
			map.replace("msg", msg);
			// System.out.println("result:" + jacksonUtil.Map2json(map));
			return jacksonUtil.Map2json(map);
		}

		phone = phone.replaceAll(filterPattern, "");

		String message = request.getParameter("message");
		if (message == null || "".equals(message)) {
			map.replace("success", "1");
			msg = "message不能为空";
			map.replace("msg", msg);
			// System.out.println("result:" + jacksonUtil.Map2json(map));
			return jacksonUtil.Map2json(map);
		}

		message = message.replaceAll(filterPattern, "");

		// 校验验证码
		String code = request.getParameter("code");

		if (code == null || "".equals(code)) {
			map.replace("success", "1");
			msg = "code不能为空";
			map.replace("msg", msg);
			// System.out.println("result:" + jacksonUtil.Map2json(map));
			return jacksonUtil.Map2json(map);
		}

		code = code.replaceAll(filterPattern, "");
		
		Patchca patchca = new Patchca();
		if (!patchca.verifyToken(request, code)) {
			map.replace("success", "1");
			msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "6", language, "验证码不正确",
					"验证码不正确空");
			map.replace("msg", msg);
			return jacksonUtil.Map2json(map);
		}

		// 收件人email
		String sjr = sqlQuery.queryForObject(
				"select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_ZLSQ_SJR_EMAIL'", "String");
		if (sjr == null || "".equals(sjr)) {
			map.replace("success", "1");
			msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "8", language, "收件人未配置",
					"收件人未配置");
			map.replace("msg", msg);
			// System.out.println("result:" + jacksonUtil.Map2json(map));
			return jacksonUtil.Map2json(map);
		}

		// 给当前填写的邮箱发送资料索取邮件---开始;
		// 发送内容;
		String source = sqlQuery.queryForObject(
				"select TZ_ZHZ_DMS from PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_REQUEST_TYPE' and TZ_ZHZ_ID=?",
				new Object[] { type }, "String");
		String content = "亲爱的管理员，你收到了一个调查，信息如下：<br>" + "Name:" + name + "<br>" + "Email:" + email + "<br>" + "Company :"
				+ Company + "<br>" + "Telephone:" + phone + "<br>" + "Message :" + message + "<br>" + "来源 :" + source
				+ "<br>" + " 请及时处理。";

		// 发送邮件;
		String taskId = createTaskServiceImpl.createTaskIns(strJgid, "TZ_EML_N_001", "MAL", "A");

		if (taskId == null || "".equals(taskId)) {
			map.replace("success", "1");
			msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "7", language, "发送失败",
					"发送失败");
			map.replace("msg", msg);
			// System.out.println("result:" + jacksonUtil.Map2json(map));
			return jacksonUtil.Map2json(map);
		}

		// 创建短信、邮件发送的听众;
		String createAudience = createTaskServiceImpl.createAudience(taskId, strJgid, "高端产品用户邮箱修改", "JSRW");
		if (createAudience == null || "".equals(createAudience)) {
			map.replace("success", "1");
			msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "7", language, "发送失败",
					"发送失败");
			map.replace("msg", msg);
			// System.out.println("result:" + jacksonUtil.Map2json(map));
			return jacksonUtil.Map2json(map);
		}

		// 为听众添加听众成员;
		int sendNum = 0;
		String[] arr = sjr.split(";");
		for (int i = 0; i < arr.length; i++) {
			String sjrEmail = arr[i];
			if (sjrEmail != null && !"".equals(sjrEmail)) {
				boolean addAudCy = createTaskServiceImpl.addAudCy(createAudience, "管理员", "管理员", "", "", sjrEmail, "",
						"", "", "", "", "");
				if (addAudCy == false) {
					map.replace("success", "1");
					msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "7", language,
							"发送失败", "发送失败");
					map.replace("msg", msg);
					// System.out.println("result:" +
					// jacksonUtil.Map2json(map));
					return jacksonUtil.Map2json(map);
				} else {
					sendNum++;
				}
			}
		}

		if (sendNum == 0) {
			map.replace("success", "1");
			msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "9", language, "收件人为空",
					"收件人为空");
			map.replace("msg", msg);
			// System.out.println("result:" + jacksonUtil.Map2json(map));
			return jacksonUtil.Map2json(map);
		}

		// 修改主题;
		boolean bl = createTaskServiceImpl.updateEmailSendTitle(taskId, "Inquiry");
		if (bl == false) {
			map.replace("success", "1");
			msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "7", language, "发送失败",
					"发送失败");
			map.replace("msg", msg);
			// System.out.println("result:" + jacksonUtil.Map2json(map));
			return jacksonUtil.Map2json(map);
		}

		// 修改内容;
		bl = createTaskServiceImpl.updateEmailSendContent(taskId, content);
		if (bl == false) {
			map.replace("success", "1");
			msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "7", language, "发送失败",
					"发送失败");
			map.replace("msg", msg);
			// System.out.println("result:" + jacksonUtil.Map2json(map));
			return jacksonUtil.Map2json(map);
		}

		// 得到创建的任务ID;
		if (taskId == null || "".equals(taskId)) {
			map.replace("success", "1");
			msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "7", language, "发送失败",
					"发送失败");
			map.replace("msg", msg);
			// System.out.println("result:" + jacksonUtil.Map2json(map));
			return jacksonUtil.Map2json(map);
		}

		sendSmsOrMalServiceImpl.send(taskId, "");

		PsTzDataRequestT psTzDataRequestT = new PsTzDataRequestT();
		psTzDataRequestT.setId(getSeqNum.getSeqNum("TZ_DATA_REQUEST_T", "ID"));
		psTzDataRequestT.setTzName(name);
		psTzDataRequestT.setTzEmail(email);
		psTzDataRequestT.setTzCompany(Company);
		psTzDataRequestT.setTzMessage(message);
		psTzDataRequestT.setTzPhone(phone);
		psTzDataRequestT.setTzRequestType(type);
		psTzDataRequestTMapper.insert(psTzDataRequestT);
		// System.out.println("result:" + jacksonUtil.Map2json(map));
		return jacksonUtil.Map2json(map);
	}
}
