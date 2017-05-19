/**
 * 
 */
package com.tranzvision.gd.TZMessageSetMgBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMessageSetMgBundle.dao.PsTzPtXxdyTblMapper;
import com.tranzvision.gd.TZMessageSetMgBundle.dao.PsTzPtXxjhTblMapper;
import com.tranzvision.gd.TZMessageSetMgBundle.model.PsTzPtXxdyTblKey;
import com.tranzvision.gd.TZMessageSetMgBundle.model.PsTzPtXxjhTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.Memoryparameter;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 消息集合定义功能；原：TZ_GD_MESSAGE_PKG:TZ_MESSAGEINFO_CLS
 * 
 * @author SHIHUA
 * @since 2015-11-09
 */
@Service("com.tranzvision.gd.TZMessageSetMgBundle.service.impl.MessageSetInfoServiceImpl")
public class MessageSetInfoServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private PsTzPtXxjhTblMapper psTzPtXxjhTblMapper;

	@Autowired
	private PsTzPtXxdyTblMapper psTzPtXxdyTblMapper;

	final String LJ = "@";

	/**
	 * 获取消息集合定义信息
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
			if (jacksonUtil.containsKey("msgSetID")) {

				String msgSetID = jacksonUtil.getString("msgSetID");

				PsTzPtXxjhTbl psTzPtXxjhTbl = psTzPtXxjhTblMapper.selectByPrimaryKey(msgSetID);

				if (psTzPtXxjhTbl != null) {
					Map<String, Object> mapData = new HashMap<String, Object>();
					mapData.put("msgSetID", psTzPtXxjhTbl.getTzXxjhId());
					mapData.put("msgSetDesc", psTzPtXxjhTbl.getTzXxjhMc());

					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("formData", mapData);

					strRet = jacksonUtil.Map2json(mapRet);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该消息集合数据不存在";
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
	 * 新增消息集合定义
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
				String msgSetDesc = jacksonUtil.getString("msgSetDesc");

				String sql = "select 'Y' from PS_TZ_PT_XXJH_TBL WHERE TZ_XXJH_ID=?";
				String recExists = sqlQuery.queryForObject(sql, new Object[] { msgSetID }, "String");
				if (null != recExists) {
					conflictKeys += comma + msgSetID;
					comma = ",";
				} else {
					PsTzPtXxjhTbl psTzPtXxjhTbl = new PsTzPtXxjhTbl();
					psTzPtXxjhTbl.setTzXxjhId(msgSetID);
					psTzPtXxjhTbl.setTzXxjhMc(msgSetDesc);
					psTzPtXxjhTblMapper.insert(psTzPtXxjhTbl);
				}
			}
			if (!"".equals(conflictKeys)) {
				errMsg[0] = "1";
				errMsg[1] = "消息集合：" + conflictKeys + "，已经存在";
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	/**
	 * 修改消息集合信息
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
				String msgSetDesc = jacksonUtil.getString("msgSetDesc");

				String sql = "select 'Y' from PS_TZ_PT_XXJH_TBL WHERE TZ_XXJH_ID=?";
				String recExists = sqlQuery.queryForObject(sql, new Object[] { msgSetID }, "String");
				if (null != recExists) {
					PsTzPtXxjhTbl psTzPtXxjhTbl = new PsTzPtXxjhTbl();
					psTzPtXxjhTbl.setTzXxjhId(msgSetID);
					psTzPtXxjhTbl.setTzXxjhMc(msgSetDesc);
					psTzPtXxjhTblMapper.updateByPrimaryKey(psTzPtXxjhTbl);
				} else {
					errorMsg += comma + msgSetID;
					comma = ",";
				}
			}
			if (!"".equals(errorMsg)) {
				errMsg[0] = "1";
				errMsg[1] = "消息集合：" + errorMsg + "，不存在";
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/**
	 * 查询消息集合信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_XXJH_ID", "TZ_MSG_ID", "TZ_MSG_TEXT", "TZ_LANGUAGE_NAME", "TZ_JG_ID" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("msgSetID", rowList[0]);
					mapList.put("messageId", rowList[1]);
					mapList.put("messageTest", rowList[2]);
					mapList.put("messageLanguage", rowList[3]);
					mapList.put("orgId", rowList[4]);

					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(mapRet);

	}

	/**
	 * 删除消息集合定义
	 */
	@Override
	@Transactional
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 提交信息
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 消息集合id
				String msgSetID = jacksonUtil.getString("msgSetID");
				String messageId = jacksonUtil.getString("messageId");
				String messageLanguage = jacksonUtil.getString("messageLanguage");
				String orgId = jacksonUtil.getString("orgId");

				if (msgSetID != null && !"".equals(msgSetID)) {

					if (messageId != null && !"".equals(messageId)) {

						if (messageLanguage != null && !"".equals(messageLanguage)) {

							if (orgId != null && !"".equals(orgId)) {

								String languageID = sqlQuery.queryForObject(
										"SELECT TZ_ZHZ_ID FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=? AND TZ_ZHZ_CMS=?",
										new Object[] { "TZ_LANGUAGE_ID", messageLanguage }, "String");

								// 删除消息信息定义表
								PsTzPtXxdyTblKey psTzPtXxdyTblKey = new PsTzPtXxdyTblKey();
								psTzPtXxdyTblKey.setTzXxjhId(msgSetID);
								psTzPtXxdyTblKey.setTzMsgId(messageId);
								psTzPtXxdyTblKey.setTzJgId(orgId);
								psTzPtXxdyTblKey.setTzLanguageId(languageID);
								psTzPtXxdyTblMapper.deleteByPrimaryKey(psTzPtXxdyTblKey);

								String firstKey = msgSetID + LJ + orgId;
								String secondKey = messageId + LJ + languageID;

								// Key:TZ_XXJH_ID@TZ_JG_ID
								// value:map(key:TZ_MSG_ID@TZ_LANGUAGE_ID,value:TZ_MSG_TEXT)
								if (Memoryparameter.messageText.get(firstKey) != null) {
									Memoryparameter.messageText.get(firstKey).remove(secondKey);
								}

							}

						}

					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}

}
