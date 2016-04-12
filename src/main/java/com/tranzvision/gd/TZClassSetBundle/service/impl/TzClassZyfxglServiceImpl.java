/**
 * 
 */
package com.tranzvision.gd.TZClassSetBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClsMajorTMapper;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsMajorT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 专业方向，原PS：TZ_GD_BJGL_CLS:TZ_BJ_ZYFX
 * 
 * @author SHIHUA
 * @since 2016-02-03
 */
@Service("com.tranzvision.gd.TZClassSetBundle.service.impl.TzClassZyfxglServiceImpl")
public class TzClassZyfxglServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private PsTzClsMajorTMapper psTzClsMajorTMapper;

	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);

			String bj_id = jacksonUtil.getString("bj_id");
			String fx_id = jacksonUtil.getString("fx_id");

			Map<String, Object> mapJson = new HashMap<String, Object>();

			if (null != bj_id && !"".equals(bj_id) && null != fx_id && !"".equals(fx_id)) {
				String sql = "select TZ_MAJOR_NAME from PS_TZ_CLS_MAJOR_T where TZ_CLASS_ID=? and TZ_MAJOR_ID=?";
				String str_fx_name = sqlQuery.queryForObject(sql, new Object[] { bj_id, fx_id }, "String");

				mapJson.put("bj_id", bj_id);
				mapJson.put("fx_id", fx_id);
				mapJson.put("fx_name", str_fx_name);
			}

			mapRet.replace("formData", mapJson);

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "查询失败！" + e.getMessage();
		}

		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}

	@Override
	@Transactional
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		try {
			JacksonUtil jacksonUtil = new JacksonUtil();

			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {

				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String bj_id = jacksonUtil.getString("bj_id");
				String fx_id = jacksonUtil.getString("fx_id");

				if (!"".equals(bj_id) && !"".equals(fx_id)) {

					String sql = "select 'Y' from PS_TZ_CLS_MAJOR_T where TZ_CLASS_ID=? and TZ_MAJOR_ID=?";
					String recExists = sqlQuery.queryForObject(sql, new Object[] { bj_id, fx_id }, "String");

					if (!"Y".equals(recExists)) {

						String fx_name = jacksonUtil.getString("fx_name");

						sql = "select count(*) from PS_TZ_CLS_MAJOR_T where TZ_CLASS_ID=?";
						int tzSortNum = sqlQuery.queryForObject(sql, new Object[] { bj_id }, "int");
						tzSortNum = tzSortNum + 1;

						PsTzClsMajorT psTzClsMajorT = new PsTzClsMajorT();
						psTzClsMajorT.setTzClassId(bj_id);
						psTzClsMajorT.setTzMajorId(fx_id);
						psTzClsMajorT.setTzMajorName(fx_name);
						psTzClsMajorT.setTzSortNum(tzSortNum);
						psTzClsMajorT.setRowAddedDttm(dateNow);
						psTzClsMajorT.setRowAddedOprid(oprid);
						psTzClsMajorT.setRowLastmantDttm(dateNow);
						psTzClsMajorT.setRowLastmantOprid(oprid);

						int rst = psTzClsMajorTMapper.insertSelective(psTzClsMajorT);
						if (rst == 0) {
							errMsg[0] = "1";
							errMsg[1] = "添加失败！";
						}

					} else {
						errMsg[0] = "1";
						errMsg[1] = "编号【" + fx_id + "】重复！请重新输入。";
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

	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		try {

			JacksonUtil jacksonUtil = new JacksonUtil();

			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

//				Map<String, Object> mapParam = jacksonUtil.getMap("data");
				Map<String, Object> mapParam = jacksonUtil.getMap();

				String bj_id = String.valueOf(mapParam.getOrDefault("bj_id", ""));
				String fx_id = String.valueOf(mapParam.getOrDefault("fx_id", ""));
				String fx_name = String.valueOf(mapParam.getOrDefault("fx_name", ""));

				if (!"".equals(bj_id) && !"".equals(fx_id)) {

					PsTzClsMajorT psTzClsMajorT = new PsTzClsMajorT();
					psTzClsMajorT.setTzClassId(bj_id);
					psTzClsMajorT.setTzMajorId(fx_id);
					psTzClsMajorT.setTzMajorName(fx_name);
					psTzClsMajorT.setRowLastmantDttm(dateNow);
					psTzClsMajorT.setRowLastmantOprid(oprid);

					int rst = psTzClsMajorTMapper.updateByPrimaryKeySelective(psTzClsMajorT);
					if (rst == 0) {
						errMsg[0] = "1";
						errMsg[1] = "专业方向【" + fx_name + "】更新失败！";
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
