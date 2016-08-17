package com.tranzvision.gd.TZOrganizationOutSiteMgBundle.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiColuTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiDefnTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiMenuTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiColuT;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiDefnTWithBLOBs;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiMenuT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * @author caoy
 * @version 创建时间：2016年8月15日 下午3:37:30 类说明 机构外部站点设置
 */
@Service("com.tranzvision.gd.TZOrganizationOutSiteMgBundle.service.impl.OrgOutSiteMgServiceImpl")
public class OrgOutSiteMgServiceImpl extends FrameworkImpl {

	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private PsTzSiteiDefnTMapper psTzSiteiDefnTMapper;
	@Autowired
	private PsTzSiteiColuTMapper psTzSiteiColuTMapper;
	@Autowired
	private PsTzSiteiMenuTMapper psTzSiteiMenuTMapper;

	/* 查询列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("total", 0);
		ArrayList<Map<String, Object>> arraylist = new ArrayList<Map<String, Object>>();
		returnJsonMap.put("root", arraylist);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			System.out.println("orgId:" + orgId);
			String totalSQL = "SELECT COUNT(1) FROM PS_TZ_SITEI_DEFN_T where TZ_SITEI_TYPE in ('A','B') and TZ_JG_ID = ?";
			int total = jdbcTemplate.queryForObject(totalSQL, new Object[] { orgId }, "Integer");
			String sql = "";
			List<Map<String, Object>> list = null;
			System.out.println("total:" + total);
			System.out.println("numLimit:" + numLimit);
			if (numLimit > 0) {
				sql = "SELECT TZ_SITEI_ID,TZ_SITEI_NAME,TZ_SITEI_DESCR FROM PS_TZ_SITEI_DEFN_T where TZ_SITEI_TYPE in ('A','B') and TZ_JG_ID = ? ORDER BY TZ_SITEI_ID ASC LIMIT ?,?";
				list = jdbcTemplate.queryForList(sql, new Object[] { orgId, numStart, numLimit });
			} else {
				sql = "SELECT TZ_SITEI_ID,TZ_SITEI_NAME,TZ_SITEI_DESCR FROM PS_TZ_SITEI_DEFN_T where TZ_SITEI_TYPE in ('A','B') and TZ_JG_ID = ? ORDER BY TZ_SITEI_ID ASC";
				list = jdbcTemplate.queryForList(sql, new Object[] { orgId });
			}
			System.out.println("sql:" + sql);
			if (list != null) {
				System.out.println("list:" + list.size());
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> jsonMap = new HashMap<String, Object>();
					jsonMap.put("siteId", list.get(i).get("TZ_SITEI_ID"));
					jsonMap.put("sitetemplateName", list.get(i).get("TZ_SITEI_NAME"));
					jsonMap.put("explanation", list.get(i).get("TZ_SITEI_DESCR"));
					arraylist.add(jsonMap);
				}
				returnJsonMap.replace("total", total);
				returnJsonMap.replace("root", arraylist);
				strRet = jacksonUtil.Map2json(returnJsonMap);
			}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	/* 删除站点模板 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 提交信息
				String strComInfo = actData[num];
				jacksonUtil.json2Map(strComInfo);
				// 站点ID;
				String siteId = jacksonUtil.getString("siteId");
				if (siteId != null && !"".equals(siteId)) {
					// 删除机构站点基本信息;
					psTzSiteiDefnTMapper.deleteByPrimaryKey(siteId);

					// 删除机构站点模板信息;
					String deleteSQL = "DELETE from PS_TZ_SITEI_TEMP_T where TZ_SITEI_ID=?";
					jdbcTemplate.update(deleteSQL, new Object[] { siteId });

					// 删除机构站点栏目信息;
					deleteSQL = "DELETE from PS_TZ_SITEI_COLU_T where TZ_SITEI_ID=?";
					jdbcTemplate.update(deleteSQL, new Object[] { siteId });

					// 删除机构站点菜单信息;
					deleteSQL = "DELETE from PS_TZ_SITEI_MENU_T where TZ_SITEI_ID=?";
					jdbcTemplate.update(deleteSQL, new Object[] { siteId });
					// 删除站点内容
					// deleteSQL = "DELETE from PS_TZ_ART_REC_TBL where
					// TZ_SITEI_ID=?";
					// jdbcTemplate.update(deleteSQL, new Object[] { siteId });
					// 删除站点菜单（PAGE）模板栏目关联表
					deleteSQL = "DELETE from PS_TZ_ASSCHNL_T where TZ_SITEI_ID=?";
					jdbcTemplate.update(deleteSQL, new Object[] { siteId });
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/* 新增机构站点信息 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("siteId", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			String strForm = "";
			String siteId = "";
			String siteLanguage = "";
			String siteName = "";
			String enabled = "";
			String siteType = "";
			String siteIntroduce = "";
			String sitePath = "";
			String picPrefix = "";
			String attPrefix = "";
			String viewPrefix = "";
			String parentRealPath = "";
			File dir = null;
			String sql = "";
			String isHas = "";
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			PsTzSiteiDefnTWithBLOBs psTzSiteiDefnT = null;
			PsTzSiteiColuT psTzSiteiColuT = null;
			PsTzSiteiMenuT psTzSiteiMenuT = null;
			String coluId = "";
			String tzColuPath = "";
			String tzMenuId = "";
			// System.out.println("orgId:"+orgId);
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				siteLanguage = jacksonUtil.getString("siteLanguage");
				siteName = jacksonUtil.getString("siteName");
				enabled = jacksonUtil.getString("enabled");
				siteType = jacksonUtil.getString("siteType");
				sitePath = jacksonUtil.getString("sitePath");
				siteIntroduce = jacksonUtil.getString("siteIntroduce");
				picPrefix = jacksonUtil.getString("picPrefix");
				attPrefix = jacksonUtil.getString("attPrefix");
				viewPrefix = jacksonUtil.getString("viewPrefix");

				tzColuPath = sitePath;

				sql = "SELECT 'Y' FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_PATH = ?";

				isHas = jdbcTemplate.queryForObject(sql, new Object[] { sitePath }, "String");
				if (StringUtils.equals(isHas, "Y")) {
					errMsg[0] = "1";
					errMsg[1] = "站点路径已经存在";
				} else {
					siteId = String.valueOf(getSeqNum.getSeqNum("TZ_SITEI_DEFN_T", "TZ_SITEI_ID"));
					psTzSiteiDefnT = new PsTzSiteiDefnTWithBLOBs();
					psTzSiteiDefnT.setTzSiteiId(siteId);
					psTzSiteiDefnT.setTzSiteLang(siteLanguage);
					psTzSiteiDefnT.setTzSiteiName(siteName);
					psTzSiteiDefnT.setTzSiteiEnable(enabled);
					psTzSiteiDefnT.setTzSiteiDescr(siteIntroduce);
					psTzSiteiDefnT.setTzImgStor(picPrefix);
					psTzSiteiDefnT.setTzAttsStor(attPrefix);
					psTzSiteiDefnT.setTzVideoStor(viewPrefix);
					psTzSiteiDefnT.setTzSiteiType(siteType);
					psTzSiteiDefnT.setTzSiteiPath(sitePath);
					psTzSiteiDefnT.setTzJgId(orgId);
					psTzSiteiDefnT.setTzAddedOprid(oprid);
					psTzSiteiDefnT.setTzAddedDttm(new Date());
					psTzSiteiDefnT.setTzLastmantOprid(oprid);
					psTzSiteiDefnT.setTzLastmantDttm(new Date());
					int i = psTzSiteiDefnTMapper.insert(psTzSiteiDefnT);
					if (i > 0) {
						returnJsonMap.replace("siteId", siteId);
						// 生成路径
						try {
							if (!sitePath.startsWith("/")) {
								sitePath = "/" + sitePath;
							}
							parentRealPath = request.getServletContext().getRealPath(sitePath);
							dir = new File(parentRealPath);
							if (!dir.exists()) {
								dir.mkdirs();
							}
						} catch (Exception e) {
							errMsg[0] = "1";
							errMsg[1] = "站点目录生成失败";
						}
					} else {
						errMsg[0] = "1";
						errMsg[1] = "机构站点信息保存失败";
					}
					// 默认增加根栏目和根菜单
					psTzSiteiColuT = new PsTzSiteiColuT();
					coluId = String.valueOf(getSeqNum.getSeqNum("TZ_SITEI_COLU_T", "TZ_COLU_ID"));
					psTzSiteiColuT.setTzSiteiId(siteId);
					psTzSiteiColuT.setTzColuId(coluId);
					psTzSiteiColuT.setTzColuName(siteName);
					psTzSiteiColuT.setTzColuPath(tzColuPath);
					psTzSiteiColuT.setTzColuState("Y");
					psTzSiteiColuT.setTzColuLevel(new Integer(0));
					// psTzSiteiColuT.setTzColuType(tzColuType);
					psTzSiteiColuTMapper.insertSelective(psTzSiteiColuT);

					psTzSiteiMenuT = new PsTzSiteiMenuT();
					tzMenuId = String.valueOf(getSeqNum.getSeqNum("TZ_SITEI_MENU_T", "TZ_MENU_ID"));
					psTzSiteiMenuT.setTzSiteiId(siteId);
					psTzSiteiMenuT.setTzMenuId(tzMenuId);
					psTzSiteiMenuT.setTzMenuName(siteName);
					psTzSiteiMenuT.setTzMenuPath(tzColuPath);
					psTzSiteiMenuT.setTzMenuState("Y");
					psTzSiteiMenuT.setTzIsDel("N"); // 不允许删除
					psTzSiteiMenuT.setTzIsEditor("N"); // 不允许编辑
					psTzSiteiMenuT.setTzMenuType("B"); // BOOK
					psTzSiteiMenuT.setTzMenuLevel(new Integer(0));
					psTzSiteiMenuT.setTzAddedOprid(oprid);
					psTzSiteiMenuT.setTzAddedDttm(new Date());
					psTzSiteiMenuT.setTzLastmantOprid(oprid);
					psTzSiteiMenuT.setTzLastmantDttm(new Date());
					psTzSiteiMenuTMapper.insertSelective(psTzSiteiMenuT);
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	/* 修改机构站点信息 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("siteId", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String strForm = "";
			String siteId = "";
			String siteLanguage = "";
			String siteName = "";
			String enabled = "";
			String siteType = "";
			String siteIntroduce = "";
			String sitePath = "";
			String picPrefix = "";
			String attPrefix = "";
			String viewPrefix = "";
			String sql = "";
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				siteId = jacksonUtil.getString("siteId");
				siteLanguage = jacksonUtil.getString("siteLanguage");
				siteName = jacksonUtil.getString("siteName");
				enabled = jacksonUtil.getString("enabled");
				siteIntroduce = jacksonUtil.getString("siteIntroduce");
				picPrefix = jacksonUtil.getString("picPrefix");
				attPrefix = jacksonUtil.getString("attPrefix");
				viewPrefix = jacksonUtil.getString("viewPrefix");
				siteType = jacksonUtil.getString("siteType");
				sitePath = jacksonUtil.getString("sitePath");

				// 站点类型以及 站点PATH 图片 下载 view 路径 站点语言不允许修改
				PsTzSiteiDefnTWithBLOBs psTzSiteiDefnT = new PsTzSiteiDefnTWithBLOBs();
				psTzSiteiDefnT.setTzSiteiId(siteId);
				// psTzSiteiDefnT.setTzSiteLang(siteLanguage);
				psTzSiteiDefnT.setTzSiteiName(siteName);
				psTzSiteiDefnT.setTzSiteiEnable(enabled);
				psTzSiteiDefnT.setTzSiteiDescr(siteIntroduce);
				// psTzSiteiDefnT.setTzImgStor(picPrefix);
				// psTzSiteiDefnT.setTzAttsStor(attPrefix);
				// psTzSiteiDefnT.setTzVideoStor(viewPrefix);
				// psTzSiteiDefnT.setTzSiteiType(siteType);
				// psTzSiteiDefnT.setTzSiteiPath(sitePath);

				psTzSiteiDefnT.setTzLastmantOprid(oprid);
				psTzSiteiDefnT.setTzLastmantDttm(new Date());
				int i = psTzSiteiDefnTMapper.updateByPrimaryKeySelective(psTzSiteiDefnT);
				if (i > 0) {
					returnJsonMap.replace("siteId", siteId);
					// 修改 栏目 和菜单的根节点
					sql = "update PS_TZ_SITEI_COLU_T set TZ_COLU_NAME=? where TZ_SITEI_ID=? and (ISNULL(TZ_F_COLU_ID) || LENGTH(trim(TZ_F_COLU_ID))<1)";
					jdbcTemplate.update(sql, new Object[] { siteName, siteId });

					sql = "update PS_TZ_SITEI_MENU_T set TZ_MENU_NAME=? where TZ_SITEI_ID=? and TZ_MENU_LEVEL=0";
					jdbcTemplate.update(sql, new Object[] { siteName, siteId });
				} else {
					errMsg[0] = "1";
					errMsg[1] = "机构站点信息保存失败";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	/* 查询表单信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("siteId")) {
				// 站点id;
				String siteId = jacksonUtil.getString("siteId");

				PsTzSiteiDefnTWithBLOBs psTzSiteiDefnT = psTzSiteiDefnTMapper.selectByPrimaryKey(siteId);
				Map<String, Object> jsonMap = new HashMap<String, Object>();
				jsonMap.put("siteId", siteId);
				jsonMap.put("siteLanguage", psTzSiteiDefnT.getTzSiteLang());
				jsonMap.put("siteName", psTzSiteiDefnT.getTzSiteiName());
				jsonMap.put("enabled", psTzSiteiDefnT.getTzSiteiEnable());
				jsonMap.put("siteType", psTzSiteiDefnT.getTzSiteiType());
				jsonMap.put("siteIntroduce", psTzSiteiDefnT.getTzSiteiDescr());
				jsonMap.put("sitePath", psTzSiteiDefnT.getTzSiteiPath());
				jsonMap.put("picPrefix", psTzSiteiDefnT.getTzImgStor());
				jsonMap.put("attPrefix", psTzSiteiDefnT.getTzAttsStor());
				jsonMap.put("viewPrefix", psTzSiteiDefnT.getTzVideoStor());

				returnJsonMap.replace("formData", jsonMap);

			} else {
				errMsg[0] = "1";
				errMsg[1] = "参数不正确！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

}
