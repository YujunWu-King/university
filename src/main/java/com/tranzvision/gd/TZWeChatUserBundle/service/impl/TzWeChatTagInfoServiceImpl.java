package com.tranzvision.gd.TZWeChatUserBundle.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzExcelDattT;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpsKshTblKey;
import com.tranzvision.gd.TZWeChatUserBundle.dao.PsTzWxTagTblMapper;
import com.tranzvision.gd.TZWeChatUserBundle.model.PsTzWxTagTbl;
import com.tranzvision.gd.TZWeChatUserBundle.model.PsTzWxTagTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 微信标签信息
 * @author LuYan 2017-8-29
 *
 */
@Service("com.tranzvision.gd.TZWeChatUserBundle.service.impl.TzWeChatTagInfoServiceImpl")
public class TzWeChatTagInfoServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsTzWxTagTblMapper psTzWxTagTblMapper;
	@Autowired
	private GetSeqNum getSeqNum;
	
	
	/* 新增 */
	@SuppressWarnings("unused")
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			//当前登录人
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			int num = 0;
			for (num = 0; num < actData.length; num++) {
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				String jgId = jacksonUtil.getString("jgId");
				String wxAppId = jacksonUtil.getString("wxAppId");
				String tagName = jacksonUtil.getString("tagName");
				
				//调用接口从微信的获取新增标签的ID，暂时先自增
				String tagId = String.valueOf(getSeqNum.getSeqNum("TZ_WX_TAG_TBL", "TZ_WX_TAG_ID"));
				/*
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 */
								
				PsTzWxTagTbl psTzWxTagTbl = new PsTzWxTagTbl();
				psTzWxTagTbl.setTzJgId(jgId);
				psTzWxTagTbl.setTzWxAppid(wxAppId);
				psTzWxTagTbl.setTzWxTagId(tagId);
				psTzWxTagTbl.setTzWxTagName(tagName);
				psTzWxTagTbl.setRowAddedDttm(new Date());
				psTzWxTagTbl.setRowAddedOprid(oprid);
				psTzWxTagTbl.setRowLastmantDttm(new Date());
				psTzWxTagTbl.setRowLastmantOprid(oprid);
				psTzWxTagTblMapper.insertSelective(psTzWxTagTbl);
				
				//调用接口同步微信端
				/*
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 */
				
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}
	
	
	/* 保存 */
	@SuppressWarnings("unused")
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			//当前登录人
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			int num = 0;
			for (num = 0; num < actData.length; num++) {
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				
				String jgId = jacksonUtil.getString("jgId");
				String wxAppId = jacksonUtil.getString("wxAppId");
				String tagId = jacksonUtil.getString("tagId");
				String tagName = jacksonUtil.getString("tagName");
				
				PsTzWxTagTblKey psTzWxTagTblKey = new PsTzWxTagTblKey();
				psTzWxTagTblKey.setTzJgId(jgId);
				psTzWxTagTblKey.setTzWxAppid(wxAppId);
				psTzWxTagTblKey.setTzWxTagId(tagId);
				
				PsTzWxTagTbl psTzWxTagTbl = psTzWxTagTblMapper.selectByPrimaryKey(psTzWxTagTblKey);
				if(psTzWxTagTbl!=null) {
					psTzWxTagTbl.setTzWxTagName(tagName);
					psTzWxTagTbl.setRowLastmantDttm(new Date());
					psTzWxTagTbl.setRowLastmantOprid(oprid);
					psTzWxTagTblMapper.updateByPrimaryKeySelective(psTzWxTagTbl);
					
					//调用接口同步微信端
					/*
					 * 
					 * 
					 * 
					 * 
					 * 
					 * 
					 * 
					 * 
					 * 
					 * 
					 * 
					 * 
					 */
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}
	
	
	/* 删除 */
	@SuppressWarnings("unchecked")
	public String tzDelete(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {

			if (actData.length == 0 || actData == null) {
				return strRet;
			}

			int num = 0;
			for (num = 0; num < actData.length; num++) {
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				String jgId = jacksonUtil.getString("jgId");
				String wxAppId = jacksonUtil.getString("wxAppId");
				String tagId = jacksonUtil.getString("tagId");
				
				//删除标签定义表
				PsTzWxTagTblKey psTzWxTagTblKey = new PsTzWxTagTblKey();
				psTzWxTagTblKey.setTzJgId(jgId);
				psTzWxTagTblKey.setTzWxAppid(wxAppId);
				psTzWxTagTblKey.setTzWxTagId(tagId);
				
				psTzWxTagTblMapper.deleteByPrimaryKey(psTzWxTagTblKey);

				//删除用户标签表
				String sql = "DELETE FROM PS_TZ_WXUSER_TAG_T WHERE TZ_JG_ID=? AND TZ_WX_APPID=? AND TZ_TAG_ID=?";
				sqlQuery.update(sql, new Object[] { jgId, wxAppId, tagId });
				
				//调用接口同步微信端
				/*
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 */
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}
}