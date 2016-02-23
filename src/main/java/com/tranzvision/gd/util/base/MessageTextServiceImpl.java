package com.tranzvision.gd.util.base;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.util.base.MessageTextServiceImpl")
public class MessageTextServiceImpl {
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	
	/**
	 * 根据指定消息集合号、消息ID、语言代码获取消息文本的方法
	 * 
	 * @author SHIHUA
	 * @param msgSetId
	 * @param msgId
	 * @param langCd
	 * @param defaultCNMsg
	 * @param defaultENMsg
	 * @return String
	 */
	public String getMessageTextWithLanguageCd( String msgSetId, String msgId, String langCd,
			String defaultCNMsg, String defaultENMsg) {
		String retMsgText = "";
		String defaultLang = getSysHardCodeVal.getSysDefaultLanguage();
		if (null == langCd || "".equals(langCd)) {
			langCd = getSysHardCodeVal.getSysDefaultLanguage();
		}

		try {
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String ptOrid = getSysHardCodeVal.getPlatformOrgID();

			if (null != msgSetId && !"".equals(msgSetId) && null != msgId && !"".equals(msgId)) {
				String sql = tzGDObject.getSQLText("SQL.TZBaseBundle.TzGetMsgText");
				retMsgText = jdbcTemplate.queryForObject(sql, new Object[] { langCd, msgSetId, orgid, msgId },
						"String");
				if (null == retMsgText || "".equals(retMsgText)) {
					retMsgText = jdbcTemplate.queryForObject(sql, new Object[] { langCd, msgSetId, ptOrid, msgId },
							"String");
				}
			}

			if (null == retMsgText || "".equals(retMsgText)) {
				if (defaultLang.equals(langCd)) {
					retMsgText = defaultCNMsg;
				} else {
					retMsgText = defaultENMsg;
				}
			} else {

			}

		} catch (Exception e) {
			e.printStackTrace();
			retMsgText = "取数失败！" + e.getMessage();
		}
		return retMsgText;
	}
}
