package com.tranzvision.gd.TZSelfInfoBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzLxfsInfoTblMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzLxfsInfoTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author tang
 * TZ_GD_SELF_PKG:TZ_SELFINFO_CLS
 */
@Service("com.tranzvision.gd.TZSelfInfoBundle.service.impl.SelfInfoClsServiceImpl")
public class SelfInfoClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzLxfsInfoTblMapper psTzLxfsInfoTblMapper;

	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";

		if (actData == null || actData.length == 0) {
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
		if (jacksonUtil.containsKey("contactEmail") && jacksonUtil.containsKey("contactPhone")) {
			// 获得联系邮箱;
			String strEmail = jacksonUtil.getString("contactEmail");
			// 获得联系手机;
			String strPhone = jacksonUtil.getString("contactPhone");

			if (strEmail == null || "".equals(strEmail)) {
				errMsg[0] = "1";
				errMsg[1] = "联系邮箱必填";
				return strRet;
			}

			if (strPhone == null || "".equals(strPhone)) {
				errMsg[0] = "1";
				errMsg[1] = "联系手机必填";
				return strRet;
			}
			
			String existSQL = "SELECT COUNT(1) FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY=? AND TZ_LYDX_ID=?" ;
			int count = jdbcTemplate.queryForObject(existSQL,new Object[]{"NBYH", userid},"Integer");
			if(count > 0){
				String SQL = "UPDATE PS_TZ_LXFSINFO_TBL SET TZ_ZY_EMAIL=?,TZ_ZY_SJ=? WHERE TZ_LXFS_LY=? AND TZ_LYDX_ID=?";
				jdbcTemplate.update(SQL, new Object[] { strEmail, strPhone, "NBYH", userid });
			}else{
				PsTzLxfsInfoTbl psTzLxfsInfoTbl = new PsTzLxfsInfoTbl();
				psTzLxfsInfoTbl.setTzLxfsLy("NBYH");
				psTzLxfsInfoTbl.setTzLydxId(userid);
				psTzLxfsInfoTbl.setTzZyEmail(strEmail);
				psTzLxfsInfoTbl.setTzZySj(strPhone);
				psTzLxfsInfoTblMapper.insert(psTzLxfsInfoTbl);
			}
			return strRet;
		} else {
			errMsg[0] = "1";
			errMsg[1] = "修改失败";
			return strRet;
		}

	}

	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		Map<String, Object> returnMap = new HashMap<>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		// 当前机构;
		String strOrgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		// 当前登录人员;
		String userid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		if (userid == null || "".equals(userid) || "TZ_GUEST".equals(userid)) {
			errMsg[0] = "1";
			errMsg[1] = "请先登录再操作";
			return "";
		}

		String strAccountId = "", strName = "", strBindEmail = "", strBindPhone = "";

		String sql = "select TZ_DLZH_ID,TZ_REALNAME,TZ_EMAIL,TZ_MOBILE from PS_TZ_AQ_YHXX_TBL where OPRID=?";
		Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { userid });
		if (map != null) {
			strAccountId = (String) map.get("TZ_DLZH_ID");
			strName = (String) map.get("TZ_REALNAME");
			strBindEmail = (String) map.get("TZ_EMAIL");
			strBindPhone = (String) map.get("TZ_MOBILE");
		}
		String strPhone = "", strEmail = "";
		sql = "SELECT TZ_ZY_SJ,TZ_ZY_EMAIL FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY='NBYH' AND TZ_LYDX_ID=?";
		map = jdbcTemplate.queryForMap(sql, new Object[] { userid });
		if (map != null) {
			strPhone = (String) map.get("TZ_ZY_SJ");
			strEmail = (String) map.get("TZ_ZY_EMAIL");
		}

		String strBindEmailFlg = "", strBindPhoneFlg = "";
		sql = "select TZ_YXBD_BZ,TZ_SJBD_BZ from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID=? and OPRID=?";
		map = jdbcTemplate.queryForMap(sql, new Object[] { strOrgid, userid });
		if (map != null) {
			strBindEmailFlg = (String) map.get("TZ_YXBD_BZ");
			strBindPhoneFlg = (String) map.get("TZ_SJBD_BZ");
		}

		if (!"Y".equals(strBindPhoneFlg)) {
			strBindPhone = "未绑定";
		}

		if (!"Y".equals(strBindEmailFlg)) {
			strBindEmail = "未绑定";
		}
		returnMap.put("accountID", strAccountId);
		returnMap.put("accountName", strName);
		returnMap.put("Email", strBindEmail);
		returnMap.put("Phone", strBindPhone);
		returnMap.put("bindPhoneFlg", strBindPhoneFlg);
		returnMap.put("bindEmailFlg", strBindEmailFlg);
		returnMap.put("contactEmail", strEmail);
		returnMap.put("contactPhone", strPhone);
		return jacksonUtil.Map2json(returnMap);

	}

	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		String strUser = tzLoginServiceImpl.getLoginedManagerOprid(request);
		if (strUser == null || "".equals(strUser) || "TZ_GUEST".equals(strUser)) {
			return "\"请先登录再操作\"";
		}

		String sql = "select TZ_ZY_SJ,TZ_ZY_EMAIL from PS_TZ_LXFSINFO_TBL where TZ_LXFS_LY=? and TZ_LYDX_ID=?";
		Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { "NBYH", strUser });
		String strEmail = "";
		if (map != null) {
			strEmail = (String) map.get("TZ_ZY_EMAIL");
		}

		// 当前机构;
		String strOrgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

		// 是否绑定了手机;
		//sql = "select count(1) from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID=? and OPRID=? and TZ_RYLX='NBYH' and TZ_SJBD_BZ='Y'";
		//int intBindPhoneFlg = jdbcTemplate.queryForObject(sql, new Object[] { strOrgid, strUser }, "Integer");
		// 是否绑定了邮箱;
		sql = "select count(1) from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID=? and OPRID=? and TZ_RYLX='NBYH' and TZ_YXBD_BZ='Y'";
		int intBindEmailFlg = jdbcTemplate.queryForObject(sql, new Object[] { strOrgid, strUser }, "Integer");

		// 未绑定邮箱，进行绑定;
		if (intBindEmailFlg <= 0) {
			// 未录入邮箱;
			if (strEmail == null || "".equals(strEmail)) {
				return "\"绑定失败， 请先录入邮箱\"";
			} else {
				// 判重;
				String reptSQL = "select COUNT(1) from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID=? and OPRID<>? and TZ_RYLX='NBYH' and TZ_YXBD_BZ='Y' and TZ_EMAIL=?";
				int isRpt = jdbcTemplate.queryForObject(reptSQL, new Object[] { strOrgid, strUser, strEmail }, "Integer");
				if (isRpt > 0) {
					return "\"绑定失败， 该邮箱已被其他人绑定\"";
				} else {
					String updateSql = "update PS_TZ_AQ_YHXX_TBL set TZ_EMAIL=?,TZ_YXBD_BZ='Y',ROW_LASTMANT_DTTM=current_timestamp(),ROW_LASTMANT_OPRID=? where TZ_JG_ID=? and OPRID=? and TZ_RYLX='NBYH'";
					jdbcTemplate.update(updateSql, new Object[] { strEmail, strUser, strOrgid, strUser });
					return "\"BINDEMAIL\"";
				}
			}

		} else {
			// 已绑定邮箱，进行解绑;
			String unBindSQL = "update PS_TZ_AQ_YHXX_TBL set TZ_EMAIL='',TZ_YXBD_BZ='N',ROW_LASTMANT_DTTM=current_timestamp(),ROW_LASTMANT_OPRID=? where TZ_JG_ID=? and OPRID=? and TZ_RYLX='NBYH'";
			jdbcTemplate.update(unBindSQL, new Object[] { strUser, strOrgid, strUser });
			return "\"UNBINDEMAIL\"";
		}
	}

}
