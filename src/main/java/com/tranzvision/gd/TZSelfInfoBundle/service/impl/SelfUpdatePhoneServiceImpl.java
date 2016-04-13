/**
 * 
 */
package com.tranzvision.gd.TZSelfInfoBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl;
import com.tranzvision.gd.TZSelfInfoBundle.dao.PsTzShjiYzmTblMapper;
import com.tranzvision.gd.TZSelfInfoBundle.model.PsTzShjiYzmTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.security.RegExpValidatorUtils;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 修改手机，原PS：TZ_GD_SELF_PKG:TZ_GD_PHONE_CLS
 * 
 * @author SHIHUA
 * @since 2016-02-04
 */
@Service("com.tranzvision.gd.TZSelfInfoBundle.service.impl.SelfUpdatePhoneServiceImpl")
public class SelfUpdatePhoneServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;

	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;
	
	@Autowired
	private PsTzShjiYzmTblMapper psTzShjiYzmTblMapper;

	/**
	 * 发送手机验证码
	 */
	@Override
	public String tzGetHtmlContent(String strParams) {

		String strRet = "";

		try {

			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strParams);

			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			if (null == oprid || "".equals(oprid)) {
				return "请先登录再修改手机";
			}

			String strPhone = jacksonUtil.getString("newPhone");

			// 判断手机格式
			boolean boolPhone = RegExpValidatorUtils.isMobile(strPhone);
			
			if (boolPhone) {
				String sql = "select 'Y' from PS_TZ_AQ_YHXX_TBL where TZ_JIHUO_ZT = 'Y' and TZ_MOBILE=? and TZ_JG_ID=? and TZ_SJBD_BZ='Y' and OPRID<>? limit 0,1";
				String phoneUsed = sqlQuery.queryForObject(sql, new Object[] { strPhone, orgid, oprid }, "String");

				if ("Y".equals(phoneUsed)) {
					return "该手机已被占用，请重新输入！";
				}

				sql = "select TZ_CNTLOG_ADDTIME,TZ_YZM_YXQ,TZ_SJYZM from PS_TZ_SHJI_YZM_TBL where TZ_EFF_FLAG='Y' and TZ_JG_ID=? and TZ_MOBILE_PHONE=? order by TZ_CNTLOG_ADDTIME desc limit 0,1";
				Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { orgid, strPhone });

				if (mapData != null) {

					String dtFormat = getSysHardCodeVal.getDateTimeFormat();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dtFormat);

					String strTzSjyzm = mapData.get("TZ_SJYZM") == null ? "" : String.valueOf(mapData.get("TZ_SJYZM"));
					Date dateAddTime = simpleDateFormat.parse(mapData.get("TZ_CNTLOG_ADDTIME") == null ? ""
							: String.valueOf(mapData.get("TZ_CNTLOG_ADDTIME")));
					Date dateYXQ = simpleDateFormat
							.parse(mapData.get("TZ_YZM_YXQ") == null ? "" : String.valueOf(mapData.get("TZ_YZM_YXQ")));

					Date dateNow = new Date();

					if (!"".equals(strTzSjyzm)) {
						if (dateYXQ.getTime() > dateNow.getTime()) {
							return "发送时间间隔太短,请等待一段时间后再试。";
						} else {
							sql = "update PS_TZ_SHJI_YZM_TBL set TZ_EFF_FLAG='N' where TZ_JG_ID=? and TZ_MOBILE_PHONE=? and TZ_CNTLOG_ADDTIME=?";
							sqlQuery.update(sql, new Object[] { strPhone, orgid, dateAddTime });
						}
					}

				}
				
				strRet = this.toSaveSendPhone(strPhone, orgid);

			} else {
				return "请输入正确的手机号码！";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return strRet;

	}

	/**
	 * 确认修改手机
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {

				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String strPhone = jacksonUtil.getString("newPhone");
				String strAuthCode = jacksonUtil.getString("authCode").trim();

				String sql = "select TZ_CNTLOG_ADDTIME,TZ_SJYZM from PS_TZ_SHJI_YZM_TBL where TZ_EFF_FLAG='Y' and TZ_JG_ID=? and TZ_MOBILE_PHONE=?";
				Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { orgid, strPhone });

				if (mapData != null) {

					String dtFormat = getSysHardCodeVal.getDateTimeFormat();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dtFormat);

					String strTzSjyzm = mapData.get("TZ_SJYZM") == null ? "" : String.valueOf(mapData.get("TZ_SJYZM"));
					Date dateAddTime = simpleDateFormat.parse(mapData.get("TZ_CNTLOG_ADDTIME") == null ? ""
							: String.valueOf(mapData.get("TZ_CNTLOG_ADDTIME")));

					dateAddTime = simpleDateFormat.parse(String.valueOf(dateAddTime.getTime() + 12 * 60 * 60 * 1000));

					Date dateCmp = new Date();

					if (!"".equals(strTzSjyzm)) {

						if (dateAddTime.getTime() <= dateCmp.getTime()) {
							errMsg[0] = "1";
							errMsg[1] = "验证码已失效，请重新发送验证码。";
							return strRet;
						}

						// 校验验证码是否正确
						if (strAuthCode.toUpperCase().equals(strTzSjyzm.toUpperCase())) {
							// 如果绑定了手机，则修改用户的主要手机时，则要同时修改绑定手机，同时要判断新的绑定手机是否在该机构下重复，如果重复，则修改失败，同时要提示用户;
							/*
							 * sql =
							 * "select TZ_MOBILE from PS_TZ_AQ_YHXX_TBL where TZ_JIHUO_ZT = 'Y'and OPRID=? and TZ_JG_ID=? and TZ_RYLX=? and TZ_SJBD_BZ='Y'"
							 * ; String strBindPhoneFlg =
							 * sqlQuery.queryForObject(sql, new Object[] {
							 * oprid, orgid, "NBYH" }, "String");
							 */
							sql = "select 'Y' from PS_TZ_AQ_YHXX_TBL where TZ_JIHUO_ZT = 'Y' and TZ_MOBILE = ? and TZ_JG_ID=? and TZ_SJBD_BZ='Y' and OPRID<>? limit 0,1";
							String phoneUsed = sqlQuery.queryForObject(sql, new Object[] { strPhone, orgid, oprid },
									"String");

							if ("Y".equals(phoneUsed)) {
								errMsg[0] = "1";
								errMsg[1] = "该手机号已被占用，请使用其他手机号。";
								return strRet;
							}

							sql = "update PS_TZ_AQ_YHXX_TBL set TZ_MOBILE=?,TZ_SJBD_BZ='Y' where TZ_JIHUO_ZT='Y' and OPRID=? AND TZ_JG_ID=? and TZ_RYLX=?";
							sqlQuery.update(sql, new Object[] { strPhone, oprid, orgid, "NBYH" });

							sql = "update PS_TZ_SHJI_YZM_TBL set TZ_EFF_FLAG='N' where TZ_EFF_FLAG='Y' and TZ_JG_ID=? and TZ_MOBILE_PHONE=?";
							sqlQuery.update(sql, new Object[] { orgid, strPhone });

						} else {
							errMsg[0] = "1";
							errMsg[1] = "验证码错误。";
							return strRet;
						}

					}

				} else {
					errMsg[0] = "1";
					errMsg[1] = "数据错误。";
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "更新失败。" + e.getMessage();
		}

		return strRet;
	}

	/**
	 * 发送修改手机验证码到新手机，并保存数据
	 * 
	 * @param strPhone
	 * @param orgid
	 * @return String
	 */
	private String toSaveSendPhone(String strPhone, String orgid) {
		String strRet = "";
		try {

			Random random = new Random();
			int authCode = random.nextInt(9999) % (9999 - 1000 + 1) + 1000;

			String dtFormat = getSysHardCodeVal.getDateTimeFormat();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dtFormat);

			Date dateNow = new Date();

			Date tzYzmYxq = simpleDateFormat.parse(String.valueOf(dateNow.getTime() + 60 * 1000));

			PsTzShjiYzmTbl psTzShjiYzmTbl = new PsTzShjiYzmTbl();
			psTzShjiYzmTbl.setTzJgId(orgid);
			psTzShjiYzmTbl.setTzMobilePhone(strPhone);
			psTzShjiYzmTbl.setTzCntlogAddtime(dateNow);
			psTzShjiYzmTbl.setTzSjyzm(String.valueOf(authCode));
			psTzShjiYzmTbl.setTzYzmYxq(tzYzmYxq);
			psTzShjiYzmTbl.setTzEffFlag("Y");
			psTzShjiYzmTblMapper.insert(psTzShjiYzmTbl);

			// 生成发送内容
			String content = authCode + "(修改手机验证码）";

			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String sql = "select TZ_REALNAME from PS_TZ_REG_USER_T where OPRID=?";
			String realname = sqlQuery.queryForObject(sql, new Object[] { oprid }, "String");

			String taskId = createTaskServiceImpl.createTaskIns(orgid, "TZ_SMS_N_001", "SMS", "A");
			if (taskId == null || "".equals(taskId)) {
				return "创建短信发送任务失败！";
			}

			String createAudience = createTaskServiceImpl.createAudience(taskId,orgid,"高端产品用户手机修改", "JSRW");
			if (null == createAudience || "".equals(createAudience)) {
				return "创建短信发送的听众失败！";
			}

			boolean addAudCy = createTaskServiceImpl.addAudCy(createAudience,realname, realname, strPhone, "", "", "", "", oprid, "",
					"", "");
			if (!addAudCy) {
				return "为听众添加听众成员失败！";
			}

			boolean updateSmsSendContent = createTaskServiceImpl.updateSmsSendContent(taskId,content);
			if (!updateSmsSendContent) {
				return "修改发送内容失败！";
			}

			if (null == taskId || "".equals(taskId)) {
				return "创建任务失败！";
			}

			// 发送短信
			sendSmsOrMalServiceImpl.send(taskId, "");

			strRet = "SUCCESS";

		} catch (Exception e) {
			e.printStackTrace();
			strRet = "操作失败。" + e.getMessage();
		}
		return strRet;

	}

}
