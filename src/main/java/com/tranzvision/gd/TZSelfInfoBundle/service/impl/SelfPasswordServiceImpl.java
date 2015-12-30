package com.tranzvision.gd.TZSelfInfoBundle.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsoprdefnMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.Psoprdefn;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.encrypt.DESUtil;

/**
 * 
 * @author tang
 * 修改密码
 * TZ_GD_MMXG_PKG:TZ_GD_MMXG_CLS
 */
@Service("com.tranzvision.gd.TZSelfInfoBundle.service.impl.SelfPasswordServiceImpl")
public class SelfPasswordServiceImpl extends FrameworkImpl {
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsoprdefnMapper psoprdefnMapper;
	
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";

		if (actData == null || actData.length == 0) {
			errMsg[0] = "1";
			errMsg[1] = "参数有误";
			return strRet;
		}
		
		String userid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		if (userid == null || "".equals(userid) || "TZ_GUEST".equals(userid)) {
			errMsg[0] = "1";
			errMsg[1] = "请先登录再操作";
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		// 表单内容;
		String strForm = actData[0];
		jacksonUtil.json2Map(strForm);
		if(jacksonUtil.containsKey("oldPwd") && jacksonUtil.containsKey("newPwd")){
			 // 获得旧密码;
			 String strOldPass = jacksonUtil.getString("oldPwd");
			 //查看旧密码是否正确;
			 Psoprdefn psoprdefn = psoprdefnMapper.selectByPrimaryKey(userid);
			 if(psoprdefn == null){
				errMsg[0] = "1";
				errMsg[1] = "请先登录再操作";
				return strRet;
			 }
			 String password = psoprdefn.getOperpswd();
			 strOldPass = DESUtil.encrypt(strOldPass, "TZGD_Tranzvision");
			 if(strOldPass == null || !strOldPass.equals(password)){
				errMsg[0] = "1";
				errMsg[1] = "旧密码不正确";
				return strRet;
			 }
			 
			 //新密码;
			 String strNewPass = jacksonUtil.getString("newPwd");
			 if(strNewPass == null || "".equals(strNewPass)){
				 errMsg[0] = "1";
				 errMsg[1] = "新密码不能为空";
				 return strRet;
			 }
			 
			 strNewPass = DESUtil.encrypt(strNewPass, "TZGD_Tranzvision");
			 psoprdefn.setOperpswd(strNewPass);
			 psoprdefnMapper.updateByPrimaryKeySelective(psoprdefn);
			 return strRet;
		}else{
			errMsg[0] = "1";
			errMsg[1] = "参数有误";
			return strRet;
		}
	}
}
