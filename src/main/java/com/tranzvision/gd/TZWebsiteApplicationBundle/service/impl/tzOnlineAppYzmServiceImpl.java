package com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Date;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.TZAccountMgBundle.dao.PsTzAqYhxxTblMapper;
import com.tranzvision.gd.TZAccountMgBundle.dao.PsoprdefnMapper;
import com.tranzvision.gd.TZAccountMgBundle.dao.PsroleuserMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTbl;
import com.tranzvision.gd.TZAccountMgBundle.model.Psoprdefn;
import com.tranzvision.gd.TZAccountMgBundle.model.Psroleuser;
import com.tranzvision.gd.TZApplicationTemplateBundle.service.impl.TemplateEngine;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdKjComServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppInsTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppInsT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzFormWrkTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormWrkT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppDhhsTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppDhhsT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppCcTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppCcT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppDhccTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppDhccT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppHiddenTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppHiddenT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzFormAttTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormAttT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzFormPhotoTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormPhotoT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppCompTblMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppCompTbl;

import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl.tzOnlineAppViewServiceImpl;
import com.tranzvision.gd.TZRecommendationBundle.service.impl.TzTjxThanksServiceImpl;

import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzLxfsInfoTblMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzOprPhotoTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzRegUserTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzLxfsInfoTbl;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzOprPhotoT;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzRegUserT;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteEnrollClsServiceImpl;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.ObjectDoMethod;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.captcha.Patchca;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 申请人在线报名；原：TZ_ONLINE_REG_PKG:TZ_ONLINE_APP_CLS
 * 
 * @author 张彬彬
 * @since 2016-1-29
 */
@Service("com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl.tzOnlineAppYzmServiceImpl")
public class tzOnlineAppYzmServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private TZGDObject tzGdObject;
	
	/* 报名表展示 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		
		String strSen = "";
		String strResponse = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			strResponse = this.checkCodeVerify(strParams, errMsg);

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "100";
			errMsg[1] = "获取数据失败，请联系管理员";
		}

		return strResponse;
	}
	
	public String checkCodeVerify(String strParams, String[] errMsg) {
		String strCheckCode = "";

		String strResult = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("checkCode"))
				strCheckCode = jacksonUtil.getString("checkCode").trim();
			// 校验验证码
			Patchca patchca = new Patchca();
			if (!patchca.verifyToken(request, strCheckCode)) {
				errMsg[0] = "2";
				errMsg[1] = "验证码不正确";
				return strResult;
			}

			strResult = "\"success\"";
			return strResult;
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "100";
			errMsg[1] = "获取数据失败，请联系管理员";
		}

		return strResult;
	}

}
