/**
 * 
 */
package com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl;
import com.tranzvision.gd.TZSelfInfoBundle.dao.PsTzShjiYzmTblMapper;
import com.tranzvision.gd.TZSelfInfoBundle.model.PsTzShjiYzmTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 修改手机，原PS：TZ_GD_USERMG_PKG:TZ_CHANGE_MOBILE
 * 
 * @author SHIHUA
 * @since 2016-02-04
 */
@Service("com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl.ChangeMobileServiceImpl")
public class ChangeMobileServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private TZGDObject tzGDObject;
	
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	
	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;
	
	@Autowired
	private PsTzShjiYzmTblMapper psTzShjiYzmTblMapper;

	@Override
	public String tzGetHtmlContent(String strParams) {

		String strRet = "";

		try {

			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strParams);

			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			String ctxPath = request.getContextPath();
			String commonUrl = ctxPath + "/dispatcher";

			String siteId = jacksonUtil.getString("siteId");
			String sql = "select TZ_JG_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			String orgid = sqlQuery.queryForObject(sql, new Object[] { siteId }, "String");

			String RegisterInit_url = commonUrl;
			String SureTel_url = commonUrl;

			sql = "select TZ_ZY_SJ from PS_TZ_LXFSINFO_TBL where TZ_LXFS_LY='ZCYH' and TZ_LYDX_ID=?";
			String mobile = sqlQuery.queryForObject(sql, new Object[] { oprid }, "String");

			strRet = tzGDObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_WDZH_MOBILE", ctxPath, RegisterInit_url,
					SureTel_url, mobile, orgid);

		} catch (Exception e) {
			e.printStackTrace();
			strRet = "发生异常。" + e.getMessage();
		}

		return strRet;

	}

	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {

		String strRet = "";

		switch (oprType) {
		case "INIT":
			strRet = this.RegisterInit(strParams);
			break;

		case "SURE":
			strRet = this.SureTel(strParams);
			break;
		}

		return strRet;

	}

	private String RegisterInit(String strParams) {
		String strRet = "";
		String msg = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			String strMoble = jacksonUtil.getString("");
			String orgid = jacksonUtil.getString("strJgid");

			// 校验手机格式
			String todo;
			boolean boolPhone = true;

			if (boolPhone) {
				String sql = "select 'Y' from PS_TZ_AQ_YHXX_TBL where TZ_JIHUO_ZT = 'Y' and TZ_MOBILE = ? and TZ_JG_ID=? limit 0,1";
				String phoneExists = sqlQuery.queryForObject(sql, new Object[] { strMoble, orgid }, "String");

				if ("Y".equals(phoneExists)) {
					msg = "该手机已被占用，请重新输入！";
				} else {

					sql = "select TZ_CNTLOG_ADDTIME,TZ_YZM_YXQ,TZ_SJYZM from PS_TZ_SHJI_YZM_TBL where TZ_EFF_FLAG='Y' and TZ_JG_ID=? and TZ_MOBILE_PHONE=? order by TZ_CNTLOG_ADDTIME desc limit 0,1";
					Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { orgid, strMoble });

					if (mapData != null) {
						String dtFormat = getSysHardCodeVal.getDateTimeFormat();
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dtFormat);

						String strTzSjyzm = mapData.get("TZ_SJYZM") == null ? ""
								: String.valueOf(mapData.get("TZ_SJYZM"));
						Date dateAddTime = simpleDateFormat.parse(mapData.get("TZ_CNTLOG_ADDTIME") == null ? ""
								: String.valueOf(mapData.get("TZ_CNTLOG_ADDTIME")));
						Date dateYXQ = simpleDateFormat.parse(
								mapData.get("TZ_YZM_YXQ") == null ? "" : String.valueOf(mapData.get("TZ_YZM_YXQ")));

						Date dateNow = new Date();

						if (!"".equals(strTzSjyzm)) {

							if (dateYXQ.getTime() > dateNow.getTime()) {
								msg = "shtime";
							} else {
								sql = "update PS_TZ_SHJI_YZM_TBL set TZ_EFF_FLAG='N' where TZ_JG_ID=? and TZ_MOBILE_PHONE=? and TZ_CNTLOG_ADDTIME=?";
								sqlQuery.update(sql, new Object[] { strMoble, orgid, dateAddTime });

								msg = this.registerSave(strMoble, orgid);

							}

						} else {
							msg = this.registerSave(strMoble, orgid);
						}

					} else {
						msg = this.registerSave(strMoble, orgid);
					}

				}

			} else {
				msg = "请输入正确的手机号码！";
			}

		} catch (Exception e) {
			e.printStackTrace();
			msg = "修改失败。" + e.getMessage();
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("success", msg);
		strRet = jacksonUtil.Map2json(mapRet);

		return strRet;
	}

	/**
	 * 确认修改手机号
	 * 
	 * @param strParams
	 * @return String
	 */
	private String SureTel(String strParams) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String, Object> mapRet = new HashMap<String, Object>();
		try {

			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			jacksonUtil.json2Map(strParams);

			String strPhone = jacksonUtil.getString("Tel");
			String strAuthCode = jacksonUtil.getString("Yzm").trim();
			String orgid = jacksonUtil.getString("strJgid");

			String sql = "select TZ_CNTLOG_ADDTIME,TZ_SJYZM from PS_TZ_SHJI_YZM_TBL where TZ_EFF_FLAG='Y' and TZ_JG_ID=? and TZ_MOBILE_PHONE=?";
			Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { orgid, strPhone });

			if (mapData != null) {

				String dtFormat = getSysHardCodeVal.getDateTimeFormat();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dtFormat);

				String strTzSjyzm = mapData.get("TZ_SJYZM") == null ? "" : String.valueOf(mapData.get("TZ_SJYZM"));
				Date dateAddTime = simpleDateFormat.parse(mapData.get("TZ_CNTLOG_ADDTIME") == null ? ""
						: String.valueOf(mapData.get("TZ_CNTLOG_ADDTIME")));

				dateAddTime = simpleDateFormat.parse(String.valueOf(dateAddTime.getTime() + 10 * 60 * 1000));

				Date dateCmp = new Date();

				if (!"".equals(strTzSjyzm)) {

					if (dateAddTime.getTime() <= dateCmp.getTime()) {
						mapRet.put("success", "*验证已超时，请重新获取验证码。");
						return jacksonUtil.Map2json(mapRet);
					}

					// 校验验证码是否正确
					if (strAuthCode.toUpperCase().equals(strTzSjyzm.toUpperCase())) {
						// 如果绑定了手机，则修改用户的主要手机时，则要同时修改绑定手机，同时要判断新的绑定手机是否在该机构下重复，如果重复，则修改失败，同时要提示用户;
						sql = "select 'Y' from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID=? and TZ_RYLX=? and TZ_MOBILE=? and OPRID<>?";
						String phoneUsed = sqlQuery.queryForObject(sql, new Object[] { orgid, "ZCYH", strPhone, oprid },
								"String");

						if ("Y".equals(phoneUsed)) {
							mapRet.put("success", "该手机号已被占用，请选择其他手机号。");
							return jacksonUtil.Map2json(mapRet);
						}

						sql = "update PS_TZ_AQ_YHXX_TBL set TZ_MOBILE=?, TZ_SJBD_BZ='Y' where OPRID=? and TZ_JG_ID=? and TZ_RYLX=?";
						sqlQuery.update(sql, new Object[] { strPhone, oprid, orgid, "ZCYH" });

						sql = "update PS_TZ_SHJI_YZM_TBL set TZ_EFF_FLAG='N' where TZ_EFF_FLAG='Y' and TZ_JG_ID=? and TZ_MOBILE_PHONE=?";
						sqlQuery.update(sql, new Object[] { orgid, strPhone });

					} else {
						mapRet.put("success", "验证码错误，请重新输入。");
						return jacksonUtil.Map2json(mapRet);
					}

				}

			} else {
				mapRet.put("success", "数据错误。");
				return jacksonUtil.Map2json(mapRet);
			}

		} catch (Exception e) {
			e.printStackTrace();
			mapRet.put("success", "修改修改。" + e.getMessage());
			return jacksonUtil.Map2json(mapRet);
		}

		return strRet;
	}

	/**
	 * 保存并发送手机验证码
	 * 
	 * @param strMoble
	 * @param orgid
	 * @return String
	 */
	private String registerSave(String strMoble, String orgid) {
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
			psTzShjiYzmTbl.setTzMobilePhone(strMoble);
			psTzShjiYzmTbl.setTzCntlogAddtime(dateNow);
			psTzShjiYzmTbl.setTzSjyzm(String.valueOf(authCode));
			psTzShjiYzmTbl.setTzYzmYxq(tzYzmYxq);
			psTzShjiYzmTbl.setTzEffFlag("Y");
			psTzShjiYzmTblMapper.insert(psTzShjiYzmTbl);

			// 生成发送内容
			String content = "本次验证码为：" + authCode + "";

			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String sql = "select TZ_REALNAME from PS_TZ_REG_USER_T where OPRID=?";
			String realname = sqlQuery.queryForObject(sql, new Object[] { oprid }, "String");

			boolean createTaskIns = createTaskServiceImpl.createTaskIns(orgid, "TZ_SMS_N_001", "SMS", "A");
			if (!createTaskIns) {
				return "创建短信发送任务失败！";
			}

			String createAudience = createTaskServiceImpl.createAudience("高端产品用户手机修改", "JSRW");
			if (null == createAudience || "".equals(createAudience)) {
				return "创建短信发送的听众失败！";
			}

			boolean addAudCy = createTaskServiceImpl.addAudCy(realname, realname, strMoble, "", "", "", "", oprid, "",
					"", "");
			if (!addAudCy) {
				return "为听众添加听众成员失败！";
			}

			boolean updateSmsSendContent = createTaskServiceImpl.updateSmsSendContent(content);
			if (!updateSmsSendContent) {
				return "修改发送内容失败！";
			}

			String taskId = createTaskServiceImpl.getTaskId();
			if (null == taskId || "".equals(taskId)) {
				return "创建任务失败！";
			}

			// 发送短信
			sendSmsOrMalServiceImpl.send(taskId, "");

			strRet = "验证码已发送到您手机，请注意查收！";

		} catch (Exception e) {
			e.printStackTrace();
			strRet = "操作失败。" + e.getMessage();
		}
		return strRet;

	}

}
