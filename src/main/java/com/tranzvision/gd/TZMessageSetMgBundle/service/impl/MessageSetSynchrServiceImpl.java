/**
 * 
 */
package com.tranzvision.gd.TZMessageSetMgBundle.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMessageSetMgBundle.dao.PsTzPtXxdyTblMapper;
import com.tranzvision.gd.TZMessageSetMgBundle.model.PsTzPtXxdyTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.Memoryparameter;
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

	final String LJ = "@";

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
				List<Map<String, Object>> msgList = sqlQuery.queryForList(sql,
						new Object[] { msgSetID, sourceLanage, orgId });

				if (msgList.size() > 0) {
					for (Map<String, Object> mapObject : msgList) {

						String tzXxjhId = mapObject.get("TZ_XXJH_ID") == null ? ""
								: String.valueOf(mapObject.get("TZ_XXJH_ID"));
						String tzMsgId = mapObject.get("TZ_MSG_ID") == null ? ""
								: String.valueOf(mapObject.get("TZ_MSG_ID"));
						String tzJgId = mapObject.get("TZ_JG_ID") == null ? ""
								: String.valueOf(mapObject.get("TZ_JG_ID"));

						sql = "SELECT 'Y' FROM PS_TZ_PT_XXDY_TBL WHERE TZ_XXJH_ID=? AND TZ_LANGUAGE_ID=? AND TZ_JG_ID=? AND TZ_MSG_ID=?";
						String recExists = sqlQuery.queryForObject(sql,
								new Object[] { tzXxjhId, targetLanage, tzJgId, tzMsgId }, "String");

						if (!"Y".equals(recExists)) {

							PsTzPtXxdyTbl psTzPtXxdyTbl = new PsTzPtXxdyTbl();
							psTzPtXxdyTbl.setTzXxjhId(tzXxjhId);
							psTzPtXxdyTbl.setTzMsgId(tzMsgId);
							psTzPtXxdyTbl.setTzJgId(tzJgId);
							psTzPtXxdyTbl.setTzLanguageId(targetLanage);
							psTzPtXxdyTbl.setTzMsgText(mapObject.get("TZ_MSG_TEXT") == null ? ""
									: String.valueOf(mapObject.get("TZ_MSG_TEXT")));
							psTzPtXxdyTbl.setTzMsgBqid(mapObject.get("TZ_MSG_BQID") == null ? ""
									: String.valueOf(mapObject.get("TZ_MSG_BQID")));
							psTzPtXxdyTbl.setTzMsgKey(mapObject.get("TZ_MSG_KEY") == null ? ""
									: String.valueOf(mapObject.get("TZ_MSG_KEY")));
							psTzPtXxdyTbl.setTzMsgDesc(mapObject.get("TZ_MSG_DESC") == null ? ""
									: String.valueOf(mapObject.get("TZ_MSG_DESC")));
							psTzPtXxdyTblMapper.insert(psTzPtXxdyTbl);

							String firstKey = tzXxjhId + LJ + tzJgId;
							String secondKey = tzMsgId + LJ + targetLanage;

							// Key:TZ_XXJH_ID@TZ_JG_ID
							// value:map(key:TZ_MSG_ID@TZ_LANGUAGE_ID,value:TZ_MSG_TEXT)
							if (Memoryparameter.messageText.get(firstKey) == null) {
								Memoryparameter.messageText.put(firstKey, new HashMap<String, String>());
							}
							Memoryparameter.messageText.get(firstKey).put(secondKey, psTzPtXxdyTbl.getTzMsgText());

						}
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
			errMsg[1] = "同步失败：" + e.toString();
		}
		return strRet;
	}

}
