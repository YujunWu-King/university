package com.tranzvision.gd.TZDataRequestBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.captcha.Patchca;

@Service("com.tranzvision.gd.TZDataRequestBundle.service.impl.TzDataRequestEmailServiceImpl")
public class TzDataRequestEmailServiceImpl extends FrameworkImpl {
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	
	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;
	
	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;
	
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
		if(name==null || "".equals(name)){
			map.replace("success", "1");
			msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "1",
					language, "姓名不能为空", "姓名不能为空");
			map.replace("msg", msg);
			return jacksonUtil.Map2json(map);
		}
		
		String email = request.getParameter("email");
		if(email==null || "".equals(email)){
			map.replace("success", "1");
			msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "2",
					language, "Email不能为空", "Email不能为空");
			map.replace("msg", msg);
			return jacksonUtil.Map2json(map);
		}
		
		String location = request.getParameter("location");
		if(location==null || "".equals(location)){
			map.replace("success", "1");
			msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "3",
					language, "地址不能为空", "地址不能为空");
			map.replace("msg", msg);
			return jacksonUtil.Map2json(map);
		}
		
		String phone = request.getParameter("phone");
		if(phone==null || "".equals(phone)){
			map.replace("success", "1");
			msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "4",
					language, "电话不能为空", "电话不能为空");
			map.replace("msg", msg);
			return jacksonUtil.Map2json(map);
		}
		
		String code = request.getParameter("code");
		if(code==null || "".equals(code)){
			map.replace("success", "1");
			msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "5",
					language, "验证码不能为空", "验证码不能为空");
			map.replace("msg", msg);
			return jacksonUtil.Map2json(map);
		}
		
		// 校验验证码
		/*
		Patchca patchca = new Patchca();
		if (!patchca.verifyToken(request, code)) {
			map.replace("success", "1");
			msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "6",
					language, "验证码不正确", "验证码不正确空");
			map.replace("msg", msg);
			return jacksonUtil.Map2json(map);
		}
		*/
		
		// 给当前填写的邮箱发送资料索取邮件---开始;
		// 发送内容;
		String content = "资料索取--内容（待定）";

		// 发送邮件;
		String taskId = createTaskServiceImpl.createTaskIns(strJgid, "TZ_EML_N_001", "MAL", "A");
		

		if (taskId == null || "".equals(taskId)) {
			map.replace("success", "1");
			msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "7",
					language, "发送失败", "发送失败");
			map.replace("msg", msg);
			return jacksonUtil.Map2json(map);
		}

		// 创建短信、邮件发送的听众;
		String createAudience = createTaskServiceImpl.createAudience(taskId,strJgid,"高端产品用户邮箱修改", "JSRW");
		if (createAudience == null || "".equals(createAudience)) {
			map.replace("success", "1");
			msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "7",
					language, "发送失败", "发送失败");
			map.replace("msg", msg);
			return jacksonUtil.Map2json(map);
		}

		// 为听众添加听众成员;
		boolean addAudCy = createTaskServiceImpl.addAudCy(createAudience,name, name, "", "", email, "", "", "",
							"", "", "");
		if (addAudCy == false) {
			map.replace("success", "1");
			msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "7",
					language, "发送失败", "发送失败");
			map.replace("msg", msg);
			return jacksonUtil.Map2json(map);
		}

		// 修改主题;
		boolean bl = createTaskServiceImpl.updateEmailSendTitle(taskId,"资料索取-主题（待定）");
		if (bl == false) {
			map.replace("success", "1");
			msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "7",
					language, "发送失败", "发送失败");
			map.replace("msg", msg);
			return jacksonUtil.Map2json(map);
		}
		
		
		// 修改内容;
		bl = createTaskServiceImpl.updateEmailSendContent(taskId,content);
		if (bl == false) {
			map.replace("success", "1");
			msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "7",
					language, "发送失败", "发送失败");
			map.replace("msg", msg);
			return jacksonUtil.Map2json(map);
		}

		// 得到创建的任务ID;
		if (taskId == null || "".equals(taskId)) {
			map.replace("success", "1");
			msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "7",
					language, "发送失败", "发送失败");
			map.replace("msg", msg);
			return jacksonUtil.Map2json(map);
		}

		sendSmsOrMalServiceImpl.send(taskId, "");

		return jacksonUtil.Map2json(map);
	}
}
