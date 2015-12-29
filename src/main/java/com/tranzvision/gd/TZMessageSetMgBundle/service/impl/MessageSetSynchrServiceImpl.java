/**
 * 
 */
package com.tranzvision.gd.TZMessageSetMgBundle.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMessageSetMgBundle.dao.PsTzPtXxdyTblMapper;
import com.tranzvision.gd.TZMessageSetMgBundle.model.PsTzPtXxdyTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 消息集合同步资源；原：TZ_GD_MESSAGE_PKG:TZ_MESSAGESYNCHR_CLS
 * 
 * @author SHIHUA
 * @since 2015-11-09
 */
@Service("com.tranzvision.gd.TZMessageSetMgBundle.service.impl.MessageSetSynchrServiceImpl")
public class MessageSetSynchrServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private PsTzPtXxdyTblMapper psTzPtXxdyTblMapper;

	/**
	 * 同步资源
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		String errorMsg = "";
		String comma = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String msgSetID = jacksonUtil.getString("msgSetID");
				String sourceLanage = jacksonUtil.getString("sourceLanage");
				String targetLanage = jacksonUtil.getString("targetLanage");
				String orgId = jacksonUtil.getString("orgId");

				String sql = "SELECT * FROM PS_TZ_PT_XXDY_TBL WHERE TZ_XXJH_ID=? AND TZ_LANGUAGE_ID=? AND TZ_JG_ID=?";
				List<?> msgList = sqlQuery.queryForList(sql, new Object[] { msgSetID, sourceLanage, orgId });

				if (msgList.size() > 0) {
					for (Object obj : msgList) {
						Map<String, Object> mapObject = (Map<String, Object>) obj;
						PsTzPtXxdyTbl psTzPtXxdyTbl = new PsTzPtXxdyTbl();
						psTzPtXxdyTbl.setTzXxjhId(mapObject.get("TZ_XXJH_ID").toString());
						psTzPtXxdyTbl.setTzMsgId(mapObject.get("TZ_MSG_ID").toString());
						psTzPtXxdyTbl.setTzJgId(mapObject.get("TZ_JG_ID").toString());
						psTzPtXxdyTbl.setTzLanguageId(targetLanage);
						psTzPtXxdyTbl.setTzMsgText(mapObject.get("TZ_MSG_TEXT").toString());
						psTzPtXxdyTbl.setTzMsgBqid(mapObject.get("TZ_MSG_BQID").toString());
						psTzPtXxdyTbl.setTzMsgKey(mapObject.get("TZ_MSG_KEY").toString());
						psTzPtXxdyTbl.setTzMsgDesc(mapObject.get("TZ_MSG_DESC").toString());
						psTzPtXxdyTblMapper.insert(psTzPtXxdyTbl);
					}

				} else {
					errorMsg += comma + "[" + msgSetID + "," + sourceLanage + "," + orgId + "]";
					comma = ",";
				}

			}
			if (!"".equals(errorMsg)) {
				errMsg[0] = "1";
				errMsg[1] = "消息定义：" + errorMsg + "，不存在";
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

}
