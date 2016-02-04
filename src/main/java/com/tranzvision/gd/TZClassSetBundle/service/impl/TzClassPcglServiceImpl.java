/**
 * 
 */
package com.tranzvision.gd.TZClassSetBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClsBatchTMapper;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsBatchT;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsBatchTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 批次管理功能，原PS：TZ_GD_BJGL_CLS:TZ_BJ_PCGL
 * 
 * @author SHIHUA
 * @since 2016-02-03
 */
@Service("com.tranzvision.gd.TZClassSetBundle.service.impl.TzClassPcglServiceImpl")
public class TzClassPcglServiceImpl extends FrameworkImpl {

	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private PsTzClsBatchTMapper psTzClsBatchTMapper;

	/**
	 * 根据班级id和 批次id查当前班级批次信息
	 */
	@Override
	@Transactional
	public String tzQuery(String strParams, String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);

			String bj_id = jacksonUtil.getString("bj_id");
			String pc_id = jacksonUtil.getString("pc_id");

			if (null != bj_id && !"".equals(bj_id) && null != pc_id && !"".equals(pc_id)) {

				PsTzClsBatchTKey psTzClsBatchTKey = new PsTzClsBatchTKey();
				psTzClsBatchTKey.setTzClassId(bj_id);
				psTzClsBatchTKey.setTzBatchId(pc_id);

				PsTzClsBatchT psTzClsBatchT = psTzClsBatchTMapper.selectByPrimaryKey(psTzClsBatchTKey);

				if (psTzClsBatchT != null) {

					String pcName = psTzClsBatchT.getTzBatchName();
					Date pc_st_time = psTzClsBatchT.getTzStartDt();
					Date pc_sp_time = psTzClsBatchT.getTzEndDt();
					Date pc_stbm_time = psTzClsBatchT.getTzAppEndDt();
					String pc_fb = psTzClsBatchT.getTzAppPubStatus();
					Date interviewConfirmDate = psTzClsBatchT.getTzMsqrDate();

					Map<String, Object> mapJson = new HashMap<String, Object>();
					mapJson.put("bj_id", bj_id);
					mapJson.put("pc_id", pc_id);
					mapJson.put("pc_name", pcName == null ? "" : pcName);
					mapJson.put("pc_st_time", pc_st_time);
					mapJson.put("pc_sp_time", pc_sp_time);
					mapJson.put("pc_stbm_time", pc_stbm_time);
					mapJson.put("pc_fb", pc_fb == null ? "" : pc_fb);
					mapJson.put("interviewConfirmDate", interviewConfirmDate);

					mapRet.replace("formData", mapJson);

				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "参数错误！";
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "查询失败！" + e.getMessage();
		}

		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}

	/**
	 * 新增批次
	 */
	@Override
	@Transactional
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		try {
			JacksonUtil jacksonUtil = new JacksonUtil();

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {

				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				Map<String, Object> mapParam = jacksonUtil.getMap("data");

				String str_bj_id = String.valueOf(mapParam.getOrDefault("bj_id", ""));
				String str_pc_name = String.valueOf(mapParam.getOrDefault("pc_name", ""));

				if (!"".equals(str_bj_id) && !"".equals(str_pc_name)) {

					String templateId = String.valueOf(getSeqNum.getSeqNum("TZ_CLS_BATCH_T", "TZ_BATCH_ID"));

					String sql = "select 'Y' from PS_TZ_CLS_BATCH_T where TZ_CLASS_ID=? and TZ_BATCH_NAME=?";
					String recExists = sqlQuery.queryForObject(sql, new Object[] { str_bj_id, str_pc_name }, "String");

					if (!"Y".equals(recExists)) {

						String dtFormat = getSysHardCodeVal.getDateFormat();
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dtFormat);

						Date tzStartDt = simpleDateFormat
								.parse(String.valueOf(mapParam.getOrDefault("pc_st_time", "")));
						Date tzEndDt = simpleDateFormat.parse(String.valueOf(mapParam.getOrDefault("pc_sp_time", "")));
						Date tzAppEndDt = simpleDateFormat
								.parse(String.valueOf(mapParam.getOrDefault("pc_stbm_time", "")));
						Date tzMsqrDate = simpleDateFormat
								.parse(String.valueOf(mapParam.getOrDefault("interviewConfirmDate", "")));
						String tzAppPubStatus = String.valueOf(mapParam.getOrDefault("pc_fb", ""));

						PsTzClsBatchT psTzClsBatchT = new PsTzClsBatchT();
						psTzClsBatchT.setTzClassId(str_bj_id);
						psTzClsBatchT.setTzBatchId(templateId);
						psTzClsBatchT.setTzBatchName(str_pc_name);
						psTzClsBatchT.setTzStartDt(tzStartDt);
						psTzClsBatchT.setTzEndDt(tzEndDt);
						psTzClsBatchT.setTzAppEndDt(tzAppEndDt);
						psTzClsBatchT.setTzAppPubStatus(tzAppPubStatus);
						psTzClsBatchT.setTzMsqrDate(tzMsqrDate);

						int rst = psTzClsBatchTMapper.insert(psTzClsBatchT);
						if (rst == 0) {
							errMsg[0] = "1";
							errMsg[1] = "添加批次【" + str_pc_name + "】失败！";
						} else {
							Map<String, Object> mapRet = new HashMap<String, Object>();
							mapRet.put("pc_id", templateId);
							strRet = jacksonUtil.Map2json(mapRet);
						}

					} else {
						errMsg[0] = "1";
						errMsg[1] = "批次名称【" + str_pc_name + "】在当前班级已经存在！";
						break;
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "添加失败！" + e.getMessage();
		}

		return strRet;
	}

	/**
	 * 修改批次
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		try {

			JacksonUtil jacksonUtil = new JacksonUtil();

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				Map<String, Object> mapParam = jacksonUtil.getMap("data");

				String str_bj_id = String.valueOf(mapParam.getOrDefault("bj_id", ""));
				String str_pc_id = String.valueOf(mapParam.getOrDefault("pc_id", ""));
				String str_pc_name = String.valueOf(mapParam.getOrDefault("pc_name", ""));

				if (null != str_bj_id && !"".equals(str_bj_id) && null != str_pc_id && !"".equals(str_pc_id)) {

					String sql = "select 'Y' from PS_TZ_CLS_BATCH_T where TZ_CLASS_ID=? and TZ_BATCH_NAME=? and TZ_BATCH_ID<>?";
					String recExists = sqlQuery.queryForObject(sql, new Object[] { str_bj_id, str_pc_name, str_pc_id },
							"String");

					if (!"Y".equals(recExists)) {

						String dtFormat = getSysHardCodeVal.getDateFormat();
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dtFormat);

						Date tzStartDt = simpleDateFormat
								.parse(String.valueOf(mapParam.getOrDefault("pc_st_time", "")));
						Date tzEndDt = simpleDateFormat.parse(String.valueOf(mapParam.getOrDefault("pc_sp_time", "")));
						Date tzAppEndDt = simpleDateFormat
								.parse(String.valueOf(mapParam.getOrDefault("pc_stbm_time", "")));
						Date tzMsqrDate = simpleDateFormat
								.parse(String.valueOf(mapParam.getOrDefault("interviewConfirmDate", "")));
						String tzAppPubStatus = String.valueOf(mapParam.getOrDefault("pc_fb", ""));

						PsTzClsBatchT psTzClsBatchT = new PsTzClsBatchT();
						psTzClsBatchT.setTzClassId(str_bj_id);
						psTzClsBatchT.setTzBatchId(str_pc_id);
						psTzClsBatchT.setTzBatchName(str_pc_name);
						psTzClsBatchT.setTzStartDt(tzStartDt);
						psTzClsBatchT.setTzEndDt(tzEndDt);
						psTzClsBatchT.setTzAppEndDt(tzAppEndDt);
						psTzClsBatchT.setTzAppPubStatus(tzAppPubStatus);
						psTzClsBatchT.setTzMsqrDate(tzMsqrDate);

						int rst = psTzClsBatchTMapper.updateByPrimaryKey(psTzClsBatchT);
						if (rst == 0) {
							errMsg[0] = "1";
							errMsg[1] = "更新批次【" + str_pc_name + "】失败！";
							break;
						}

					} else {
						errMsg[0] = "1";
						errMsg[1] = "批次名称【" + str_pc_name + "】在当前班级已经存在！";
						break;
					}

				} else {
					errMsg[0] = "1";
					errMsg[1] = "参数错误！";
					break;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "更新失败！" + e.getMessage();
		}

		return strRet;
	}

}
