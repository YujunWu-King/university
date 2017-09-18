package com.tranzvision.gd.TZClmsCsBiaoqzBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZClmsCsBiaoqzBundle.dao.PsTzBiaoqzBqTMapper;
//import com.tranzvision.gd.TZClmsCsBiaoqzBundle.dao.PsTzBiaoqzTMapper;
import com.tranzvision.gd.TZClmsCsBiaoqzBundle.model.PsTzBiaoqzBqT;
//import com.tranzvision.gd.TZClmsCsBiaoqzBundle.model.PsTzBiaoqzT;
import com.tranzvision.gd.TZClmsCsBiaoqzBundle.model.PsTzBiaoqzBqTKey;
//import com.tranzvision.gd.TZClmsCsBiaoqzBundle.model.PsTzBiaoqzTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * @author ZXW
 * @version 1.0, 2017-2-9
 * @功能：标签定义
 */
@Service("com.tranzvision.gd.TZClmsCsBiaoqzBundle.service.impl.BqDefnImpl")
public class BqDefnImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzBiaoqzBqTMapper PsTzBiaoqzBqTMapper;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;

	/* 新增标签组标签注册信息 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 表单内容;
			String strForm = actData[0];
			// 将字符串转换成json;
			jacksonUtil.json2Map(strForm);
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			// 标签组编号;
			String strbqzID = jacksonUtil.getString("bqzID");
			// 标签编号;
			String strbqID = jacksonUtil.getString("bqID");
			if ("".equals(strbqID) || "NEXT".equals(strbqID.toUpperCase())) {
				strbqID = String.valueOf(getSeqNum.getSeqNum("PS_TZ_BIAOQZ_BQ_T", "TZ_BIAOQ_ID"));
			}
			// 标签名称;
			String strbqName = jacksonUtil.getString("bqName");
			String strDesc = jacksonUtil.getString("bqDesc");
			String strJava = jacksonUtil.getString("java");
			String csOut = jacksonUtil.getString("csOut");

			PsTzBiaoqzBqT PsTzBiaoqzBqT = new PsTzBiaoqzBqT();
			PsTzBiaoqzBqT.setTzBiaoqzId(strbqzID);
			PsTzBiaoqzBqT.setTzBiaoqId(strbqID);
			PsTzBiaoqzBqT.setTzJgId(orgid);
			PsTzBiaoqzBqT.setTzBiaoqzName(strbqName);
			PsTzBiaoqzBqT.setTzBiaoqzJava(strJava);
			PsTzBiaoqzBqT.setTzDesc(strDesc);
			PsTzBiaoqzBqT.setTzOutFlg(csOut);
			int i = PsTzBiaoqzBqTMapper.insert(PsTzBiaoqzBqT);
			if (i <= 0) {
				errMsg[0] = "1";
				errMsg[1] = "保存失败";
			}

		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}

	/* 更新标签注册信息 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 表单内容;
			String strForm = actData[0];
			// 将字符串转换成json;
			jacksonUtil.json2Map(strForm);
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			// 标签组编号;
			String strbqzID = jacksonUtil.getString("bqzID");
			// 标签编号;
			String strbqID = jacksonUtil.getString("bqID");
			// 标签名称;
			String strbqName = jacksonUtil.getString("bqName");
			String strDesc = jacksonUtil.getString("bqDesc");
			String strJava = jacksonUtil.getString("java");
			String csOut = jacksonUtil.getString("csOut");

			PsTzBiaoqzBqT PsTzBiaoqzBqT = new PsTzBiaoqzBqT();
			PsTzBiaoqzBqT.setTzBiaoqzId(strbqzID);
			PsTzBiaoqzBqT.setTzBiaoqId(strbqID);
			PsTzBiaoqzBqT.setTzJgId(orgid);
			PsTzBiaoqzBqT.setTzBiaoqzName(strbqName);
			PsTzBiaoqzBqT.setTzBiaoqzJava(strJava);
			PsTzBiaoqzBqT.setTzDesc(strDesc);
			PsTzBiaoqzBqT.setTzOutFlg(csOut);
			int i = PsTzBiaoqzBqTMapper.updateByPrimaryKeySelective(PsTzBiaoqzBqT);
			if (i <= 0) {
				errMsg[0] = "1";
				errMsg[1] = "更新数据失败";
			}

			/*
			 * } else { errMsg[0] = "1"; errMsg[1] = "当前标签组下，标签ID为：" + strPageID
			 * + "的信息不存在"; return strRet; }
			 */

		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}

	/* 获取标签信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String strbqzID = jacksonUtil.getString("bqzID");
			String strbqID = jacksonUtil.getString("bqID");
			if (strbqzID != null && !"".equals(strbqzID) && strbqID != null && !"".equals(strbqID)) {
				PsTzBiaoqzBqTKey PsTzBiaoqzBqTKey = new PsTzBiaoqzBqTKey();
				PsTzBiaoqzBqTKey.setTzBiaoqzId(strbqzID);
				PsTzBiaoqzBqTKey.setTzBiaoqId(strbqID);
				PsTzBiaoqzBqTKey.setTzJgId(orgid);
				// 获取标签信息;
				PsTzBiaoqzBqT PsTzBiaoqzBqT = PsTzBiaoqzBqTMapper.selectByPrimaryKey(PsTzBiaoqzBqTKey);
				
				returnJsonMap.put("bqzID", strbqzID);
				returnJsonMap.put("bqID", strbqID);
				returnJsonMap.put("bqName", PsTzBiaoqzBqT.getTzBiaoqzName());
				returnJsonMap.put("bqDesc", PsTzBiaoqzBqT.getTzDesc());
				returnJsonMap.put("java", PsTzBiaoqzBqT.getTzBiaoqzJava());
				returnJsonMap.put("csOut", PsTzBiaoqzBqT.getTzOutFlg()==null?"":PsTzBiaoqzBqT.getTzOutFlg());
			} else {
				errMsg[0] = "1";
				errMsg[1] = "无法获取页面信息";
			}

		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
}
