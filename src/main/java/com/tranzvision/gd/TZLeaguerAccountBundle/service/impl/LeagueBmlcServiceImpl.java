package com.tranzvision.gd.TZLeaguerAccountBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsoprdefnMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.Psoprdefn;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzRegUserTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzRegUserT;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.TzImpClpsTblMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.TzImpMspsTblMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.TzImpLkbmTblMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.TzImpYlqTblMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.TzImpClpsTbl;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.TzImpMspsTbl;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.TzImpLkbmTbl;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.TzImpYlqTbl;

@Service("com.tranzvision.gd.TZLeaguerAccountBundle.service.impl.LeagueBmlcServiceImpl")
public class LeagueBmlcServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;

	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {

		switch (oprType) {
		case "DYITEMS":
			String oprid = "";
			String classid = "";
			String appinsid = "";
			String lcName = "";
			String lcNamedesc = "";
			String mshId = "";
			String tableName = "";
			Map<String, Object> fristmap = null;
			Map<String, Object> formDatamap = new HashMap<>();
			Map<String, Object> fieldsetmap = new HashMap<>();
			ArrayList<Map<String, Object>> listData = null;
			// 返回的list
			System.out.println("进入DYITEMS");
			ArrayList<Map<String, Object>> returnlistData = new ArrayList<Map<String, Object>>();
			ArrayList<Map<String, Object>> fieldsetlistData = new ArrayList<Map<String, Object>>();
			try {
				JacksonUtil jacksonUtil = new JacksonUtil();
				jacksonUtil.json2Map(strParams);
				oprid = jacksonUtil.getString("oprid");

				String usersql = " SELECT TZ_CLASS_ID,TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID=?";
				Map<String, Object> userMap = jdbcTemplate.queryForMap(usersql, new Object[] { oprid });
				classid = String.valueOf(userMap.get("TZ_CLASS_ID"));
				appinsid = String.valueOf(userMap.get("TZ_APP_INS_ID"));
				System.out.println("appinsid" + appinsid);
				// layout的map
				Map<String, Object> layoutmap = new HashMap<>();
				// layout的map的list
				ArrayList<Map<String, Object>> layoutlist = new ArrayList<Map<String, Object>>();
				layoutmap.put("type", "vbox");
				layoutmap.put("align", "stretch");
				layoutlist.add(layoutmap);

//				String mshidsql = "SELECT TZ_MSH_ID FROM  PS_TZ_FORM_WRK_T  WHERE TZ_CLASS_ID=? AND OPRID=? AND TZ_APP_INS_ID=? ";
//				mshId = jdbcTemplate.queryForObject(mshidsql, new Object[] { classid, oprid, appinsid }, "String");
//
//				System.out.println("mshId" + mshId);

				// mshId="20170012";
				String tabsql = "SELECT TZ_TPL_ID,TZ_TPL_NAME,TZ_TARGET_TBL FROM TZ_IMP_TPL_DFN_T ORDER BY TZ_TPL_NAME ASC";
				List<Map<String, Object>> tab_namelist = jdbcTemplate.queryForList(tabsql);
				System.out.println("tab_namelist====>" + tab_namelist);
				if (tab_namelist.size() > 0) {
					for (Map<String, Object> map : tab_namelist) {
						lcName = map.get("TZ_TPL_ID") == null ? "" : map.get("TZ_TPL_ID").toString();
						lcNamedesc = map.get("TZ_TPL_NAME") == null ? "" : map.get("TZ_TPL_NAME").toString();
						tableName = map.get("TZ_TARGET_TBL") == null ? "" : map.get("TZ_TARGET_TBL").toString();

						if (tableName == null || "".equals(tableName)) {

						} else {
							// 字段id;
							ArrayList<String> fieldIdList = new ArrayList<>();
							// 名称;
							ArrayList<String> fieldNameList = new ArrayList<>();
							// 字段类型;
							ArrayList<String> srkLxList = new ArrayList<>();
							// 查询的表字段;
							String fieldSelectSQL = "";

							String sql = "select TZ_FIELD,TZ_FIELD_NAME,TZ_REQUIRED,TZ_DISPLAY from TZ_IMP_TPL_FLD_T where TZ_TPL_ID=?  order by TZ_SEQ ASC";
							List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { lcName });

							for (int i = 0; i < list.size(); i++) {
								String fieldId = (String) list.get(i).get("TZ_FIELD");
								String fieldName = (String) list.get(i).get("TZ_FIELD_NAME");
								// String srkLx = (String)
								// list.get(i).get("TZ_SRK_LX");
								String required = (String) list.get(i).get("TZ_REQUIRED");
								String display = (String) list.get(i).get("TZ_DISPLAY");
								// String backdisplay = (String)
								// list.get(i).get("TZ_BACK_DISPLAY");
								if (fieldName == null) {
									fieldName = "";
								}
								// if ("Y".equals(backdisplay)) {
								if ("".equals(fieldSelectSQL)) {
									fieldSelectSQL = fieldId;
								} else {
									fieldSelectSQL = fieldSelectSQL + " , " + fieldId;
								}
								fieldIdList.add(fieldId);
								fieldNameList.add(fieldName);
								// srkLxList.add(srkLx);
								// }

							}
							Map<String, Object> valueMap = null;
							try {
								fieldSelectSQL = " select " + fieldSelectSQL + " from " + tableName
										+ " where TZ_APP_INS_ID = ? ";

								valueMap = jdbcTemplate.queryForMap(fieldSelectSQL, new Object[] { appinsid });
							} catch (Exception e1) {
								valueMap = null;
							}
							fristmap = new HashMap<>();

							listData = new ArrayList<Map<String, Object>>();
							for (int i = 0; i < fieldNameList.size(); i++) {

								String xxxId = fieldIdList.get(i);
								String xxxmc = fieldNameList.get(i);
								// String srkLxing = srkLxList.get(i);
								String xxxValue = "";
								if (valueMap != null) {
									xxxValue = valueMap.get(xxxId) == null ? "" : String.valueOf(valueMap.get(xxxId));
								}
								if (xxxValue == null) {
									xxxValue = "";
								}
								Map<String, Object> dynamicitemsMap = new HashMap<>();
								dynamicitemsMap.put("srkLxing", "TEXT");
								dynamicitemsMap.put("fieldname", xxxmc);
								dynamicitemsMap.put("field", xxxId);
								dynamicitemsMap.put("value", xxxValue);
								listData.add(dynamicitemsMap);
							}
							fristmap.put("table", tableName);
							fristmap.put("lcNamedesc", lcNamedesc);
							fristmap.put("lcname", lcName);
							fristmap.put("items", listData);
							returnlistData.add(fristmap);
						}
					}
					formDatamap.put("formData", returnlistData);
					return jacksonUtil.Map2json(formDatamap);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
		return null;
	}

}
