package com.tranzvision.gd.TZComRegMgBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZComRegMgBundle.dao.PsTzAqComzcTblMapper;
import com.tranzvision.gd.TZComRegMgBundle.dao.PsTzAqPagzcTblMapper;
import com.tranzvision.gd.TZComRegMgBundle.model.PsTzAqComzcTbl;
import com.tranzvision.gd.TZComRegMgBundle.model.PsTzAqPagzcTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author tang
 * 2015-10-9
 * 功能组件注册管理相关类
 * PS:TZ_GD_COMREGMG_PKG:TZ_GD_COMREG_CLS
 *
 */
@Service("com.tranzvision.gd.TZComRegMgBundle.service.impl.ComRegImpl")
public class ComRegImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzAqComzcTblMapper psTzAqComzcTblMapper;
	@Autowired
	private PsTzAqPagzcTblMapper psTzAqPagzcTblMapper;

	/* 新增组件注册信息 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				if ("COM".equals(strFlag)) {
					// 组件编号;
					String strComID = (String) infoData.get("comID");
					// 组件名称;
					String comName = (String) infoData.get("comName");

					// 是否已经存在;
					String comExistSql = "SELECT 'Y' FROM PS_TZ_AQ_COMZC_TBL WHERE TZ_COM_ID=?";
					String isExist = "";
					isExist = jdbcTemplate.queryForObject(comExistSql, new Object[] { strComID }, "String");

					if ("Y".equals(isExist)) {
						errMsg[0] = "1";
						errMsg[1] = "组件ID为：" + strComID + "的信息已经注册，请修改组件ID。";
						return strRet;
					}

					PsTzAqComzcTbl psTzAqComzcTbl = new PsTzAqComzcTbl();
					psTzAqComzcTbl.setTzComId(strComID);
					psTzAqComzcTbl.setTzComMc(comName);
					String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
					psTzAqComzcTbl.setRowAddedOprid(oprid);
					psTzAqComzcTbl.setRowAddedDttm(new Date());
					psTzAqComzcTbl.setRowLastmantOprid(oprid);
					psTzAqComzcTbl.setRowLastmantDttm(new Date());
					int i = psTzAqComzcTblMapper.insert(psTzAqComzcTbl);
					if(i <= 0){
						errMsg[0] = "1";
						errMsg[1] = "保存失败";
					}
				}

				if ("PAGE".equals(strFlag)) {
					strRet = this.tzUpdatePageInfo(infoData, errMsg);
				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}

	/* 修改组件注册信息 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				if ("COM".equals(strFlag)) {
					// 组件编号;
					String strComID = (String) infoData.get("comID");
					// 组件名称;
					String comName = (String) infoData.get("comName");

					// 是否已经存在;
					String comExistSql = "SELECT 'Y' FROM PS_TZ_AQ_COMZC_TBL WHERE TZ_COM_ID=?";
					String isExist = "";
					isExist = jdbcTemplate.queryForObject(comExistSql, new Object[] { strComID }, "String");

					if (!"Y".equals(isExist)) {
						errMsg[0] = "1";
						errMsg[1] = "组件ID为：" + strComID + "的信息不存在。";
						return strRet;
					}

					PsTzAqComzcTbl psTzAqComzcTbl = new PsTzAqComzcTbl();
					psTzAqComzcTbl.setTzComId(strComID);
					psTzAqComzcTbl.setTzComMc(comName);
					String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
					psTzAqComzcTbl.setRowLastmantOprid(oprid);
					psTzAqComzcTbl.setRowLastmantDttm(new Date());
					int i = psTzAqComzcTblMapper.updateByPrimaryKeySelective(psTzAqComzcTbl);
					if(i <= 0){
						errMsg[0] = "1";
						errMsg[1] = "更新失败";
					}
				}

				if ("PAGE".equals(strFlag)) {
					strRet = this.tzUpdatePageInfo(infoData, errMsg);
				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}

	/* 获取组件注册信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("comID")) {
				String strComID = jacksonUtil.getString("comID");
				if (strComID != null && !"".equals(strComID)) {

					PsTzAqComzcTbl psTzAqComzcTbl = psTzAqComzcTblMapper.selectByPrimaryKey(strComID);
					if (psTzAqComzcTbl != null) {
						// 组件注册信息;
						Map<String, Object> jsonMap = new HashMap<>();
						jsonMap.put("comID", strComID);
						jsonMap.put("comName", psTzAqComzcTbl.getTzComMc());
						returnJsonMap.replace("formData", jsonMap);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "无法获取组件信息";
					}

				} else {
					errMsg[0] = "1";
					errMsg[1] = "无法获取组件信息";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "无法获取组件信息";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	/* 获取页面注册信息列表 */
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			String strComID = jacksonUtil.getString("comID");
			if (strComID != null && !"".equals(strComID)) {

				// 页面ID，页面名称，是否默认首页;
				String strPageID = "", strPageName = "", isDefault = "";
				// 序号;
				int numOrder = 0;
				// 页面注册信息列表sql;
				String sqlPageList = "";
				String totalSQL = "";
				//查询组件下的页面列表
				Object[] obj = null;
				if (numLimit == 0) {
					sqlPageList = "SELECT TZ_PAGE_ID,TZ_PAGE_MC,TZ_PAGE_XH,TZ_PAGE_MRSY FROM PS_TZ_AQ_PAGZC_TBL WHERE TZ_COM_ID=? ORDER BY TZ_PAGE_XH";
					obj = new Object[] { strComID };
				} else {
					sqlPageList = "SELECT TZ_PAGE_ID,TZ_PAGE_MC,TZ_PAGE_XH,TZ_PAGE_MRSY FROM PS_TZ_AQ_PAGZC_TBL WHERE TZ_COM_ID=? ORDER BY TZ_PAGE_XH limit ?,?";
					obj = new Object[] { strComID, numStart, numLimit };
				}
				
				int total = 0;
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlPageList, obj);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						isDefault = (String) list.get(i).get("TZ_PAGE_MRSY");
						if (!"Y".equals(isDefault)) {
							isDefault = "N";
						} 
						strPageID = (String) list.get(i).get("TZ_PAGE_ID");
						strPageName = (String) list.get(i).get("TZ_PAGE_MC");
						try{
							numOrder = (int) list.get(i).get("TZ_PAGE_XH");
						}catch(Exception e){
							numOrder = 0;
						}
						
						Map<String, Object> mapList = new HashMap<String, Object>();
						mapList.put("comID", strComID);
						mapList.put("pageID", strPageID);
						mapList.put("pageName", strPageName);
						mapList.put("orderNum", numOrder);
						mapList.put("isDefault", isDefault);
						listData.add(mapList);
					}
					
					totalSQL = "SELECT COUNT(1) FROM PS_TZ_AQ_PAGZC_TBL WHERE TZ_COM_ID=?";
					total = jdbcTemplate.queryForObject(totalSQL,new Object[] { strComID },"Integer");
					mapRet.replace("total", total);
					mapRet.replace("root", listData);
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "无法获取组件页面信息";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return jacksonUtil.Map2json(mapRet);
	}

	/* 删除页面注册信息列表 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 组件ID;
				String sComID = jacksonUtil.getString("comID");
				// 页面ID;
				String sPageID = jacksonUtil.getString("pageID");
				
				PsTzAqPagzcTbl psTzAqPagzcTbl = new PsTzAqPagzcTbl();
				psTzAqPagzcTbl.setTzComId(sComID);
				psTzAqPagzcTbl.setTzPageId(sPageID);
				
				psTzAqPagzcTblMapper.deleteByPrimaryKey(psTzAqPagzcTbl);
				
				//如果页面删除了，则删除改组件和页面对应的权限;
				String deleteSQL = "DELETE from PS_TZ_AQ_COMSQ_TBL WHERE TZ_COM_ID=? AND TZ_PAGE_ID=?";
				jdbcTemplate.update(deleteSQL,new Object[]{sComID,sPageID});
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}

	private String tzUpdatePageInfo(Map<String, Object> infoData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		try {
			// 组件编号;
			String strComID = (String) infoData.get("comID");
			// 组件名称;
			String strPageID = (String) infoData.get("pageID");
			// 序号;
			String numOrderStr = String.valueOf(infoData.get("orderNum"));
			short numOrder = Short.parseShort(numOrderStr);
			
			// 默认首页;
			boolean isDefaultBoolean = (boolean) infoData.get("isDefault");
			String isDefault = "N";
			if (isDefaultBoolean) {
				isDefault = "Y";
			} else {
				isDefault = "N";
			}

			if ("Y".equals(isDefault)) {
				// 默认首页只能有一个;
				String updateDefalutNoSQL = "UPDATE PS_TZ_AQ_PAGZC_TBL SET TZ_PAGE_MRSY='N' WHERE TZ_COM_ID=?";

				jdbcTemplate.update(updateDefalutNoSQL, new Object[] { strComID });
			}

			// 页面注册信息表;
			PsTzAqPagzcTbl psTzAqPagzcTbl = new PsTzAqPagzcTbl();
			psTzAqPagzcTbl.setTzComId(strComID);
			psTzAqPagzcTbl.setTzPageId(strPageID);
			psTzAqPagzcTbl.setTzPageXh(numOrder);
			psTzAqPagzcTbl.setTzPageMrsy(isDefault);
			psTzAqPagzcTblMapper.updateByPrimaryKeySelective(psTzAqPagzcTbl);
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

}
