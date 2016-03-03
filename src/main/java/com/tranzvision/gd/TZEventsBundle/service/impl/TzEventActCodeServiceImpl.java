/**
 * 
 */
package com.tranzvision.gd.TZEventsBundle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.sql.GetSeqNum;

/**
 * 生成活动签到码
 * 
 * @author SHIHUA
 * @since 2016-03-02
 */
@Service("com.tranzvision.gd.TZEventsBundle.service.impl.TzEventActCodeServiceImpl")
public class TzEventActCodeServiceImpl {

	@Autowired
	private GetSeqNum getSeqNum;

	public String generateActCode(String eventId, String tel) {

		// 活动签到码生成规则：序号 + 手机号后三位
		String strRet = "";

		String xh = String.valueOf(getSeqNum.getSeqNum("TZ_NAUDLIST_T", "TZ_HD_QDM"));

		int telLength = tel.length();
		if (telLength > 3) {
			tel = tel.substring(telLength - 3, telLength - 1);
		}

		strRet = xh + tel;

		strRet = strRet.replace("_", "");

		return strRet;

	}

}
