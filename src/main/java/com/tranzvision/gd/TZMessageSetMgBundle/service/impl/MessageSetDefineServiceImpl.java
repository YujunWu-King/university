/**
 * 
 */
package com.tranzvision.gd.TZMessageSetMgBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMessageSetMgBundle.dao.PsTzPtXxdyTblMapper;
import com.tranzvision.gd.TZMessageSetMgBundle.model.PsTzPtXxdyTbl;
import com.tranzvision.gd.TZMessageSetMgBundle.model.PsTzPtXxdyTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.Memoryparameter;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 消息定义功能；原：TZ_GD_MESSAGE_PKG:TZ_MESSAGEDEFINE_CLS
 * 
 * @author SHIHUA
 * @since 2015-11-09
 */
@Service("com.tranzvision.gd.TZMessageSetMgBundle.service.impl.MessageSetDefineServiceImpl")
public class MessageSetDefineServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private PsTzPtXxdyTblMapper psTzPtXxdyTblMapper;

	final String LJ = "@";

	/**
	 * 获取消息定义信息
	 * 
	 * @param strParams
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("msgSetID") && jacksonUtil.containsKey("messageId")
					&& jacksonUtil.containsKey("messageLanguage") && jacksonUtil.containsKey("orgId")) {

				String msgSetID = jacksonUtil.getString("msgSetID");
				String messageId = jacksonUtil.getString("messageId");
				String messageLanguage = jacksonUtil.getString("messageLanguage");
				String orgId = jacksonUtil.getString("orgId");

				PsTzPtXxdyTblKey psTzPtXxdyTblKey = new PsTzPtXxdyTblKey();
				psTzPtXxdyTblKey.setTzXxjhId(msgSetID);
				psTzPtXxdyTblKey.setTzMsgId(messageId);
				psTzPtXxdyTblKey.setTzJgId(orgId);
				psTzPtXxdyTblKey.setTzLanguageId(messageLanguage);
				PsTzPtXxdyTbl psTzPtXxdyTbl = psTzPtXxdyTblMapper.selectByPrimaryKey(psTzPtXxdyTblKey);

				if (psTzPtXxdyTbl != null) {
					Map<String, Object> mapData = new HashMap<String, Object>();
					mapData.put("msgSetID", psTzPtXxdyTbl.getTzXxjhId());
					mapData.put("msgId", psTzPtXxdyTbl.getTzMsgId());
					mapData.put("msgLanage", psTzPtXxdyTbl.getTzLanguageId());
					mapData.put("msgTest", psTzPtXxdyTbl.getTzMsgText());
					mapData.put("markId", psTzPtXxdyTbl.getTzMsgBqid());
					mapData.put("keyWord", psTzPtXxdyTbl.getTzMsgKey());
					mapData.put("msgDesc", psTzPtXxdyTbl.getTzMsgDesc());
					mapData.put("orgId", psTzPtXxdyTbl.getTzJgId());

					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("formData", mapData);

					strRet = jacksonUtil.Map2json(mapRet);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该消息定义数据不存在";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "参数错误";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/**
	 * 新增消息定义
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		String conflictKeys = "";
		String comma = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String msgSetID = jacksonUtil.getString("msgSetID");
				String msgId = jacksonUtil.getString("msgId");
				String orgId = jacksonUtil.getString("orgId");
				String msgLanage = jacksonUtil.getString("msgLanage");
				String msgTest = jacksonUtil.getString("msgTest");
				String markId = jacksonUtil.getString("markId");
				String keyWord = jacksonUtil.getString("keyWord");
				String msgDesc = jacksonUtil.getString("msgDesc");

				String sql = "select 'Y' from PS_TZ_PT_XXDY_TBL WHERE TZ_XXJH_ID=? and TZ_MSG_ID=? and TZ_JG_ID=? and TZ_LANGUAGE_ID=?";
				String recExists = sqlQuery.queryForObject(sql, new Object[] { msgSetID, msgId, orgId, msgLanage },
						"String");
				if (null != recExists) {
					conflictKeys += comma + "[" + msgSetID + "," + msgId + "," + orgId + "," + msgLanage + "]";
					comma = ",";
				} else {
					PsTzPtXxdyTbl psTzPtXxdyTbl = new PsTzPtXxdyTbl();
					psTzPtXxdyTbl.setTzXxjhId(msgSetID);
					psTzPtXxdyTbl.setTzMsgId(msgId);
					psTzPtXxdyTbl.setTzJgId(orgId);
					psTzPtXxdyTbl.setTzLanguageId(msgLanage);
					psTzPtXxdyTbl.setTzMsgText(msgTest);
					psTzPtXxdyTbl.setTzMsgBqid(markId);
					psTzPtXxdyTbl.setTzMsgKey(keyWord);
					psTzPtXxdyTbl.setTzMsgDesc(msgDesc);
					psTzPtXxdyTblMapper.insert(psTzPtXxdyTbl);

					String firstKey = msgSetID + LJ + orgId;
					String secondKey = msgId + LJ + msgLanage;

					// Key:TZ_XXJH_ID@TZ_JG_ID
					// value:map(key:TZ_MSG_ID@TZ_LANGUAGE_ID,value:TZ_MSG_TEXT)
					if (Memoryparameter.messageText.get(firstKey) == null) {
						Memoryparameter.messageText.put(firstKey, new HashMap<String, String>());
					}
					Memoryparameter.messageText.get(firstKey).put(secondKey, msgTest);
				}
			}
			if (!"".equals(conflictKeys)) {
				errMsg[0] = "1";
				errMsg[1] = "消息定义：" + conflictKeys + "，已经存在";
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	/**
	 * 修改消息定义信息
	 */
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
				String msgId = jacksonUtil.getString("msgId");
				String orgId = jacksonUtil.getString("orgId");
				String msgLanage = jacksonUtil.getString("msgLanage");
				String msgTest = jacksonUtil.getString("msgTest");
				String markId = jacksonUtil.getString("markId");
				String keyWord = jacksonUtil.getString("keyWord");
				String msgDesc = jacksonUtil.getString("msgDesc");

				String sql = "select 'Y' from PS_TZ_PT_XXDY_TBL WHERE TZ_XXJH_ID=? and TZ_MSG_ID=? and TZ_JG_ID=? and TZ_LANGUAGE_ID=?";
				String recExists = sqlQuery.queryForObject(sql, new Object[] { msgSetID, msgId, orgId, msgLanage },
						"String");
				if (null != recExists) {
					PsTzPtXxdyTbl psTzPtXxdyTbl = new PsTzPtXxdyTbl();
					psTzPtXxdyTbl.setTzXxjhId(msgSetID);
					psTzPtXxdyTbl.setTzMsgId(msgId);
					psTzPtXxdyTbl.setTzJgId(orgId);
					psTzPtXxdyTbl.setTzLanguageId(msgLanage);
					psTzPtXxdyTbl.setTzMsgText(msgTest);
					psTzPtXxdyTbl.setTzMsgBqid(markId);
					psTzPtXxdyTbl.setTzMsgKey(keyWord);
					psTzPtXxdyTbl.setTzMsgDesc(msgDesc);
					psTzPtXxdyTblMapper.updateByPrimaryKeyWithBLOBs(psTzPtXxdyTbl);
					
					String firstKey = msgSetID + LJ + orgId;
					String secondKey = msgId + LJ + msgLanage;

					// Key:TZ_XXJH_ID@TZ_JG_ID
					// value:map(key:TZ_MSG_ID@TZ_LANGUAGE_ID,value:TZ_MSG_TEXT)
					if (Memoryparameter.messageText.get(firstKey) == null) {
						Memoryparameter.messageText.put(firstKey, new HashMap<String, String>());
					}
					Memoryparameter.messageText.get(firstKey).put(secondKey, msgTest);
				} else {
					errorMsg += comma + "[" + msgSetID + "," + msgId + "," + orgId + "," + msgLanage + "]";
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
