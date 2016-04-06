/**
 * 
 */
package com.tranzvision.gd.TZOrganizationMgBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsTzAqYhxxTblMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTblKey;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationMgBundle.dao.PsTzJgBaseTMapper;
import com.tranzvision.gd.TZOrganizationMgBundle.dao.PsTzJgLoginbjTMapper;
import com.tranzvision.gd.TZOrganizationMgBundle.dao.PsTzJgMgrTMapper;
import com.tranzvision.gd.TZOrganizationMgBundle.dao.PsTzJgRoleTMapper;
import com.tranzvision.gd.TZOrganizationMgBundle.model.PsTzJgBaseT;
import com.tranzvision.gd.TZOrganizationMgBundle.model.PsTzJgLoginbjT;
import com.tranzvision.gd.TZOrganizationMgBundle.model.PsTzJgMgrTKey;
import com.tranzvision.gd.TZOrganizationMgBundle.model.PsTzJgRoleT;
import com.tranzvision.gd.TZOrganizationMgBundle.model.PsTzJgRoleTKey;
import com.tranzvision.gd.TZPermissionDefnBundle.dao.PsClassDefnMapper;
import com.tranzvision.gd.TZPermissionDefnBundle.dao.PsTzAqComsqTblMapper;
import com.tranzvision.gd.TZPermissionDefnBundle.model.PsClassDefn;
import com.tranzvision.gd.TZPermissionDefnBundle.model.PsTzAqComsqTbl;
import com.tranzvision.gd.TZRoleMgBundle.dao.PsRoleclassMapper;
import com.tranzvision.gd.TZRoleMgBundle.dao.PsRoledefnMapper;
import com.tranzvision.gd.TZRoleMgBundle.model.PsRoleclassKey;
import com.tranzvision.gd.TZRoleMgBundle.model.PsRoledefn;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.security.RegExpValidatorUtils;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 机构管理-机构账号信息类，原：TZ_GD_JGGL_PKG:TZ_GD_JGXX_CLS
 * 
 * @author SHIHUA
 * @since 2015-11-10
 */
@Service("com.tranzvision.gd.TZOrganizationMgBundle.service.impl.TzOrgInfoServiceImpl")
public class TzOrgInfoServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzSQLObject;

	@Autowired
	private GetHardCodePoint getHardCodePoint;
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private PsTzJgBaseTMapper psTzJgBaseTMapper;

	@Autowired
	private PsTzJgMgrTMapper psTzJgMgrTMapper;

	@Autowired
	private PsTzJgRoleTMapper psTzJgRoleTMapper;

	@Autowired
	private PsTzAqYhxxTblMapper psTzAqYhxxTblMapper;

	@Autowired
	private PsTzJgLoginbjTMapper psTzJgLoginbjTMapper;

	@Autowired
	private PsRoledefnMapper psRoledefnMapper;

	@Autowired
	private PsRoleclassMapper psRoleclassMapper;

	@Autowired
	private PsClassDefnMapper psClassDefnMapper;

	@Autowired
	private PsTzAqComsqTblMapper psTzAqComsqTblMapper;

	/**
	 * 新增机构账号信息
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
			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> infoData = jacksonUtil.getMap("data");

				if ("ORG".equals(typeFlag)) {
					// 机构账号信息;
					// 机构编号;
					String tzJgId = infoData.get("orgId").toString().toUpperCase();

					String sql = "select 'Y' from PS_TZ_JG_BASE_T WHERE TZ_JG_ID=?";
					String recExists = sqlQuery.queryForObject(sql, new Object[] { tzJgId }, "String");
					if (null != recExists) {
						conflictKeys += comma + tzJgId;
						comma = ",";
					} else {

						// 机构名称;
						String tzJgName = infoData.get("orgName").toString();
						// 机构有效性状态;
						String tzJgEffSta = infoData.get("orgYxState").toString();
						// 备注;
						String tzJgDescr = infoData.get("orgBeizhu").toString();
						// 联系人姓名;
						String tzOrganContact = infoData.get("orgLxrName").toString();
						// 联系电话;
						String tzOrganContactph = infoData.get("orgLxrPhone").toString();
						if(!RegExpValidatorUtils.isMobile(tzOrganContactph)){
							errMsg[0] = "1";
							errMsg[1] = "请输入正确的手机号。";
							return strRet;
						}
						// 联系邮箱;
						String tzOrganContactem = infoData.get("orgLxrEmail").toString();
						if(!RegExpValidatorUtils.isEmail(tzOrganContactem)){
							errMsg[0] = "1";
							errMsg[1] = "请输入正确的邮箱地址。";
							return strRet;
						}
						// 静态文件存放路径
						String tzJgJtfjPath = infoData.get("staticPath").toString();
						// 登录系统文字;
						String tzJgLoginInfo = infoData.get("orgLoginInf").toString();
						// 登录页面版权信息;
						String orgLoginCopr = infoData.get("orgLoginCopr").toString();
						// 系统文件名;
						String orgLoginBjImgUrl = infoData.get("orgLoginBjImgUrl").toString();
						// 获取图片文件名
						String sysFileName = "";
						if (null != orgLoginBjImgUrl && !"".equals(orgLoginBjImgUrl)) {
							String[] arrUrl = orgLoginBjImgUrl.split("/");
							sysFileName = arrUrl[arrUrl.length - 1];
						}

						PsTzJgBaseT psTzJgBaseT = new PsTzJgBaseT();
						psTzJgBaseT.setTzJgId(tzJgId);
						psTzJgBaseT.setTzJgName(tzJgName);
						psTzJgBaseT.setTzJgEffSta(tzJgEffSta);
						psTzJgBaseT.setTzJgDescr(tzJgDescr);
						psTzJgBaseT.setTzOrganContact(tzOrganContact);
						psTzJgBaseT.setTzOrganContactph(tzOrganContactph);
						psTzJgBaseT.setTzOrganContactem(tzOrganContactem);
						psTzJgBaseT.setTzJgLoginInfo(tzJgLoginInfo);
						psTzJgBaseT.setTzJgLoginCopr(orgLoginCopr);
						psTzJgBaseT.setTzAttachsysfilena(sysFileName);
						psTzJgBaseT.setTzJgJtfjPath(tzJgJtfjPath);

						psTzJgBaseT.setRowAddedDttm(dateNow);
						psTzJgBaseT.setRowAddedOprid(oprid);
						psTzJgBaseT.setRowLastmantDttm(dateNow);
						psTzJgBaseT.setRowLastmantOprid(oprid);

						psTzJgBaseTMapper.insert(psTzJgBaseT);
					}

				} else if ("MEM".equals(typeFlag)) {
					this.tzEditOrgMemAccountInfo(infoData, errMsg);
				}
			}
			if (!"".equals(conflictKeys)) {
				errMsg[0] = "1";
				errMsg[1] = "机构编号为：" + conflictKeys + "的信息已经存在，请修改机构编号。";
			}
		} catch (Exception e) {
			e.printStackTrace();
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
			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> infoData = jacksonUtil.getMap("data");

				if ("ORG".equals(typeFlag)) {
					// 机构编号;
					String tzJgId = infoData.get("orgId").toString().toUpperCase();

					String sql = "select 'Y' from PS_TZ_JG_BASE_T WHERE TZ_JG_ID=?";
					String recExists = sqlQuery.queryForObject(sql, new Object[] { tzJgId }, "String");
					if ("Y".equals(recExists)) {
						// 机构名称;
						String tzJgName = infoData.get("orgName").toString();
						// 机构有效性状态;
						String tzJgEffSta = infoData.get("orgYxState").toString();
						// 备注;
						String tzJgDescr = infoData.get("orgBeizhu").toString();
						// 联系人姓名;
						String tzOrganContact = infoData.get("orgLxrName").toString();
						// 联系电话;
						String tzOrganContactph = infoData.get("orgLxrPhone").toString();
						// 联系邮箱;
						String tzOrganContactem = infoData.get("orgLxrEmail").toString();
						// 静态文件存放路径
						String tzJgJtfjPath = infoData.get("staticPath")==null?"":String.valueOf(infoData.get("staticPath"));
						// 登录系统文字;
						String tzJgLoginInfo = infoData.get("orgLoginInf")==null?"":String.valueOf(infoData.get("orgLoginInf"));
						// 登录页面版权信息;
						String orgLoginCopr = infoData.get("orgLoginCopr").toString();
						// 系统文件名;
						String orgLoginBjImgUrl = infoData.get("orgLoginBjImgUrl")==null?"":String.valueOf(infoData.get("orgLoginBjImgUrl"));
						// 获取图片文件名
						String sysFileName = "";
						if (null != orgLoginBjImgUrl && !"".equals(orgLoginBjImgUrl)) {
							String[] arrUrl = orgLoginBjImgUrl.split("/");
							sysFileName = arrUrl[arrUrl.length - 1];
						}

						PsTzJgBaseT psTzJgBaseT = new PsTzJgBaseT();
						psTzJgBaseT.setTzJgId(tzJgId);
						psTzJgBaseT.setTzJgName(tzJgName);
						psTzJgBaseT.setTzJgEffSta(tzJgEffSta);
						psTzJgBaseT.setTzJgDescr(tzJgDescr);
						psTzJgBaseT.setTzOrganContact(tzOrganContact);
						psTzJgBaseT.setTzOrganContactph(tzOrganContactph);
						psTzJgBaseT.setTzOrganContactem(tzOrganContactem);
						psTzJgBaseT.setTzJgLoginInfo(tzJgLoginInfo);
						psTzJgBaseT.setTzJgLoginCopr(orgLoginCopr);
						psTzJgBaseT.setTzAttachsysfilena(sysFileName);
						psTzJgBaseT.setTzJgJtfjPath(tzJgJtfjPath);

						psTzJgBaseT.setRowLastmantDttm(dateNow);
						psTzJgBaseT.setRowLastmantOprid(oprid);

						psTzJgBaseTMapper.updateByPrimaryKeyWithBLOBs(psTzJgBaseT);

					} else {
						errorMsg += comma + tzJgId;
						comma = ",";
					}

				} else if ("USER".equals(typeFlag)) {
					// 修改机构管理员
					this.tzEditOrgMemAccountInfo(infoData, errMsg);
				} else if ("ROLE".equals(typeFlag)) {
					// 修改机构角色
					this.tzEditOrgRoleInfo(infoData, errMsg);
				} else if ("COPYROLE".equals(typeFlag)) {
					// 复制机构角色
					this.tzCopyOrgRole(infoData, errMsg);
				}
			}
			if (!"".equals(errorMsg)) {
				errMsg[0] = "1";
				errMsg[1] = "机构：" + errorMsg + "，不存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/**
	 * 删除机构管理员信息
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
				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> infoData = jacksonUtil.getMap("data");

				String tzJgId = infoData.get("orgId").toString().toUpperCase();

				if (null == tzJgId || "".equals(tzJgId)) {
					continue;
				}

				if ("USER".equals(typeFlag)) {
					String tzDlzhId = null == infoData.get("usAccNum") ? "" : String.valueOf(infoData.get("usAccNum"));
					if (tzDlzhId != null && !"".equals(tzDlzhId)) {

						// 删除机构管理员
						PsTzAqYhxxTblKey psTzAqYhxxTblKey = new PsTzAqYhxxTblKey();
						psTzAqYhxxTblKey.setTzJgId(tzJgId);
						psTzAqYhxxTblKey.setTzDlzhId(tzDlzhId);

						psTzAqYhxxTblMapper.deleteByPrimaryKey(psTzAqYhxxTblKey);

					}

				} else if ("ROLE".equals(typeFlag)) {
					String rolename = null == infoData.get("roleName") ? "" : String.valueOf(infoData.get("roleName"));
					if (rolename != null && !"".equals(rolename)) {

						// 删除机构角色
						PsTzJgRoleTKey psTzJgRoleTKey = new PsTzJgRoleTKey();
						psTzJgRoleTKey.setTzJgId(tzJgId);
						psTzJgRoleTKey.setRolename(rolename);

						psTzJgRoleTMapper.deleteByPrimaryKey(psTzJgRoleTKey);
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

	/**
	 * 获取机构账号信息
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
		try {

			String tzJgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			if ("".equals(tzJgId)) {
				errMsg[0] = "1";
				errMsg[1] = "参数错误";
				return strRet;
			}

			PsTzJgBaseT psTzJgBaseT = psTzJgBaseTMapper.selectByPrimaryKey(tzJgId);

			if (psTzJgBaseT != null) {

				String orgLoginBjImgUrl = "";
				if (null != psTzJgBaseT.getTzAttachsysfilena()
						&& !"".equals(psTzJgBaseT.getTzAttachsysfilena().trim())) {
					PsTzJgLoginbjT psTzJgLoginbjT = psTzJgLoginbjTMapper
							.selectByPrimaryKey(psTzJgBaseT.getTzAttachsysfilena());
					if (psTzJgLoginbjT != null) {
						String tzAttAUrl = psTzJgLoginbjT.getTzAttAUrl();
						String lastChar = tzAttAUrl.substring(tzAttAUrl.length() - 2);
						if ("/".equals(lastChar)) {
							orgLoginBjImgUrl = tzAttAUrl + psTzJgBaseT.getTzAttachsysfilena();
						} else {
							orgLoginBjImgUrl = tzAttAUrl + "/" + psTzJgBaseT.getTzAttachsysfilena();
						}
					}
				}

				Map<String, Object> mapData = new HashMap<String, Object>();
				mapData.put("orgId", psTzJgBaseT.getTzJgId());
				mapData.put("orgName", psTzJgBaseT.getTzJgName());
				mapData.put("orgYxState", psTzJgBaseT.getTzJgEffSta());
				mapData.put("orgBeizhu", psTzJgBaseT.getTzJgDescr());
				mapData.put("orgLxrName", psTzJgBaseT.getTzOrganContact());
				mapData.put("orgLxrPhone", psTzJgBaseT.getTzOrganContactph());
				mapData.put("orgLxrEmail", psTzJgBaseT.getTzOrganContactem());
				mapData.put("orgLoginInf", psTzJgBaseT.getTzJgLoginInfo());
				mapData.put("orgLoginBjImgUrl", orgLoginBjImgUrl);
				mapData.put("orgLoginCopr", psTzJgBaseT.getTzJgLoginCopr());

				/*
				 * Map<String, Object> mapMemList = new HashMap<String,
				 * Object>(); mapMemList.put("total", 0); mapMemList.put("root",
				 * "");
				 */

				Map<String, Object> mapRet = new HashMap<String, Object>();
				mapRet.put("formData", mapData);

				JacksonUtil jacksonUtil = new JacksonUtil();
				strRet = jacksonUtil.Map2json(mapRet);
			} else {
				errMsg[0] = "1";
				errMsg[1] = "[" + tzJgId + "]" + "该机构数据不存在";
			}

			errMsg[0] = "0";
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/**
	 * 获取机构管理员信息和角色信息列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		JacksonUtil jacksonUtil = new JacksonUtil();
		
		jacksonUtil.json2Map(strParams);
		String queryType = jacksonUtil.getString("queryType");

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");
		String strRet = jacksonUtil.Map2json(mapRet);

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};

			if ("USER".equals(queryType)) {

				// json数据要的结果字段;
				String[] resultFldArray = { "TZ_JG_ID", "TZ_DLZH_ID", "TZ_REALNAME" };

				// 可配置搜索通用函数;
				Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

				if (obj != null && obj.length > 0) {

					ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

					for (int i = 0; i < list.size(); i++) {
						String[] rowList = list.get(i);

						Map<String, Object> mapList = new HashMap<String, Object>();
						mapList.put("orgId", rowList[0]);
						mapList.put("usAccNum", rowList[1]);
						mapList.put("usName", rowList[2]);

						listData.add(mapList);
					}

					mapRet.replace("total", obj[0]);
					mapRet.replace("root", listData);

					strRet = jacksonUtil.Map2json(mapRet);
				}

			} else if ("ROLE".equals(queryType)) {
				String tzJgId = jacksonUtil.getString("orgId");
				if (null != tzJgId && !"".equals(tzJgId.trim())) {
					strRet = this.tzQueryRoleList(tzJgId, numLimit, numStart, errorMsg);
				}
			} else {
				errorMsg[0] = "1";
				errorMsg[1] = "请求错误";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strRet;

	}

	/**
	 * 存储机构图片文件信息
	 * 
	 * @param strParams
	 * @return String
	 */
	@Override
	@Transactional
	public String tzGetHtmlContent(String strParams) {

		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("success", 0);
		mapRet.put("message", "");

		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			jacksonUtil.json2Map(strParams);

			String tzAttachfileName = jacksonUtil.getString("filename");
			String tzAttachsysfilena = jacksonUtil.getString("sysFileName");
			String tzAttPUrl = jacksonUtil.getString("path");
			String tzAttAUrl = jacksonUtil.getString("accessPath");

			PsTzJgLoginbjT psTzJgLoginbjT = new PsTzJgLoginbjT();
			psTzJgLoginbjT.setTzAttachfileName(tzAttachfileName);
			psTzJgLoginbjT.setTzAttachsysfilena(tzAttachsysfilena);
			psTzJgLoginbjT.setTzAttAUrl(tzAttAUrl);
			psTzJgLoginbjT.setTzAttPUrl(tzAttPUrl);

			psTzJgLoginbjTMapper.insert(psTzJgLoginbjT);

			mapRet.put("success", 0);
			mapRet.put("message", "");
		} catch (Exception e) {
			e.printStackTrace();
			mapRet.replace("message", e.getMessage());
		}

		return jacksonUtil.Map2json(mapRet);
	}

	/**
	 * 更新机构管理员信息
	 * 
	 * @param mapData
	 * @param errorMsg
	 */
	@Transactional
	private void tzEditOrgMemAccountInfo(Map<String, Object> mapData, String[] errorMsg) {

		// 机构管理员信息
		// 机构编号;
		String tzJgId = mapData.get("orgId").toString().toUpperCase();
		// 管理员账号ID;
		String tzDlzhId = mapData.get("usAccNum").toString();
		String sql = "select 'Y' from PS_TZ_JS_MGR_T WHERE TZ_JG_ID=? and TZ_DLZH_ID=?";
		String recExists = sqlQuery.queryForObject(sql, new Object[] { tzJgId, tzDlzhId }, "String");
		if (null == recExists) {

			PsTzJgMgrTKey psTzJgMgrTKey = new PsTzJgMgrTKey();
			psTzJgMgrTKey.setTzJgId(tzJgId);
			psTzJgMgrTKey.setTzDlzhId(tzDlzhId);

			psTzJgMgrTMapper.insert(psTzJgMgrTKey);

		}

	}

	/**
	 * 新增或修改机构角色信息
	 * 
	 * @param mapData
	 * @param errorMsg
	 */
	@Transactional
	private void tzEditOrgRoleInfo(Map<String, Object> mapData, String[] errorMsg) {
		try {
			// 机构编号;
			String tzJgId = mapData.get("orgId").toString().toUpperCase();
			// 角色名称;
			String rolename = mapData.get("roleName").toString();
			// 角色类型
			String tzRoleType = mapData.get("roleType").toString();

			String sql = "select 'Y' from PS_TZ_JG_ROLE_T WHERE TZ_JG_ID=? and ROLENAME=?";
			String recExists = sqlQuery.queryForObject(sql, new Object[] { tzJgId, rolename }, "String");

			PsTzJgRoleT psTzJgRoleT = new PsTzJgRoleT();
			psTzJgRoleT.setTzJgId(tzJgId);
			psTzJgRoleT.setRolename(rolename);
			psTzJgRoleT.setTzRoleType(tzRoleType);

			if (null == recExists) {

				psTzJgRoleTMapper.insert(psTzJgRoleT);

			} else {

				psTzJgRoleTMapper.updateByPrimaryKey(psTzJgRoleT);

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.getMessage();
		}
	}

	/**
	 * 获取机构下角色信息列表
	 * 
	 * @param strOrgID
	 * @param numLimit
	 * @param numStart
	 * @param errorMsg
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	private String tzQueryRoleList(String strOrgID, int numLimit, int numStart, String[] errorMsg) {

		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		try {
			String sql = "";
			Object[] args;
			if (numLimit == 0 && numStart == 0) {
				sql = tzSQLObject.getSQLText("SQL.TZOrganizationMgBundle.TzSelectOrgRolesNolimit");
				args = new Object[] { strOrgID };
			} else {
				sql = tzSQLObject.getSQLText("SQL.TZOrganizationMgBundle.TzSelectOrgRoleslimit");
				args = new Object[] { strOrgID, numStart, numLimit };
			}

			List<?> listData = sqlQuery.queryForList(sql, args);

			ArrayList<Map<String, Object>> listDataJson = new ArrayList<Map<String, Object>>();

			for (Object jgRoleData : listData) {

				Map<String, Object> mapData = (Map<String, Object>) jgRoleData;

				Map<String, Object> mapDataJson = new HashMap<String, Object>();
				mapDataJson.put("orgId", strOrgID);
				mapDataJson.put("roleName",
						mapData.get("ROLENAME") == null ? "" : String.valueOf(mapData.get("ROLENAME")));
				mapDataJson.put("roleDesc",
						mapData.get("ROLE_DESC") == null ? "" : String.valueOf(mapData.get("ROLE_DESC")));
				mapDataJson.put("roleType",
						mapData.get("TZ_ROLE_TYPE") == null ? "" : String.valueOf(mapData.get("TZ_ROLE_TYPE")));
				mapDataJson.put("roleTypeDesc", mapData.get("TZ_ROLE_TYPE_DESC") == null ? ""
						: String.valueOf(mapData.get("TZ_ROLE_TYPE_DESC")));

				listDataJson.add(mapDataJson);

			}

			sql = tzSQLObject.getSQLText("SQL.TZOrganizationMgBundle.TzCountOrgRoles");
			int total = sqlQuery.queryForObject(sql, new Object[] { strOrgID }, "int");

			mapRet.replace("total", total);
			mapRet.replace("root", listDataJson);

			JacksonUtil jacksonUtil = new JacksonUtil();
			strRet = jacksonUtil.Map2json(mapRet);

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.getMessage();
		}

		return strRet;
	}

	/**
	 * 从平台机构复制机构角色
	 * 
	 * @param strParams
	 * @param errorMsg
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	private String tzCopyOrgRole(Map<String, Object> mapParams, String[] errorMsg) {

		String tzJgId = mapParams.get("orgId").toString().toUpperCase();

		String strAdminJg = getSysHardCodeVal.getPlatformOrgID();
		String strPlstBasic = getHardCodePoint.getHardCodePointVal("TZGD_BASIC");

		Date dateNow = new Date();
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		try {
			// 复制角色
			String sql = tzSQLObject.getSQLText("SQL.TZOrganizationMgBundle.TzCopyOrgRoles");

			List<?> listRoles = sqlQuery.queryForList(sql, new Object[] { strAdminJg });

			for (Object srcRole : listRoles) {

				Map<String, Object> mapRole = (Map<String, Object>) srcRole;

				String strSrcRoleName = mapRole.get("ROLENAME") == null ? "" : String.valueOf(mapRole.get("ROLENAME"));
				String roleId = tzJgId + "_" + strSrcRoleName;
				roleId = roleId.toUpperCase();

				// 检查角色定义表中是否存在该角色
				sql = "SELECT 'Y' FROM PSROLEDEFN WHERE ROLENAME=?";
				String recExsit = sqlQuery.queryForObject(sql, new Object[] { roleId }, "String");
				if (null == recExsit) {

					PsRoledefn psRoledefn = new PsRoledefn();

					psRoledefn.setRolename(roleId);
					psRoledefn.setDescr(mapRole.get("DESCR") == null ? "" : String.valueOf(mapRole.get("DESCR")));
					psRoledefn.setDescrlong(
							mapRole.get("DESCRLONG") == null ? "" : String.valueOf(mapRole.get("DESCRLONG")));
					psRoledefn.setVersion(Integer.parseInt(mapRole.get("VERSION").toString()));
					psRoledefn.setRolestatus(
							mapRole.get("ROLESTATUS") == null ? "" : String.valueOf(mapRole.get("ROLESTATUS")));
					psRoledefn.setRolePcodeRuleOn(mapRole.get("ROLE_PCODE_RULE_ON") == null ? ""
							: String.valueOf(mapRole.get("ROLE_PCODE_RULE_ON")));
					psRoledefn.setRoleQueryRuleOn(mapRole.get("ROLE_QUERY_RULE_ON") == null ? ""
							: String.valueOf(mapRole.get("ROLE_QUERY_RULE_ON")));
					psRoledefn.setLdapRuleOn(
							mapRole.get("LDAP_RULE_ON") == null ? "" : String.valueOf(mapRole.get("LDAP_RULE_ON")));
					psRoledefn.setAllownotify(
							mapRole.get("ALLOWNOTIFY") == null ? "" : String.valueOf(mapRole.get("ALLOWNOTIFY")));
					psRoledefn.setAllowlookup(
							mapRole.get("ALLOWLOOKUP") == null ? "" : String.valueOf(mapRole.get("ALLOWLOOKUP")));
					psRoledefn.setLastupddttm(dateNow);
					psRoledefn.setLastupdoprid(oprid);

					psRoledefnMapper.insert(psRoledefn);
				}

				// 复制角色的许可权
				sql = tzSQLObject.getSQLText("SQL.TZOrganizationMgBundle.TzCopyRolePermit");

				List<?> listPermits = sqlQuery.queryForList(sql, new Object[] {strSrcRoleName});

				for (Object srcPermit : listPermits) {

					Map<String, Object> mapPermit = (Map<String, Object>) srcPermit;

					String srcClassId = mapPermit.get("CLASSID").toString();

					if (!strPlstBasic.equals(srcClassId)) {

						String srcPermID = srcClassId;
						String permId = tzJgId + "_" + srcPermID;
						permId = permId.toUpperCase();

						sql = "SELECT 'Y' FROM PSCLASSDEFN WHERE CLASSID=?";
						recExsit = sqlQuery.queryForObject(sql, new Object[] { permId }, "String");
						// 复制许可权定义表
						if (null == recExsit) {

							PsClassDefn psClassDefn = new PsClassDefn();

							psClassDefn.setClassid(permId);
							psClassDefn.setClassdefndesc(mapPermit.get("CLASSDEFNDESC").toString());
							psClassDefn.setVersion(Integer.parseInt(mapPermit.get("VERSION").toString()));
							psClassDefn.setLastupddttm(dateNow);
							psClassDefn.setLastupdoprid(oprid);

							psClassDefnMapper.insert(psClassDefn);

							// 复制许可权的登陆时间表 PSAUTHSIGNON
							/*
							 * 待定，可能不需要此表 sql = tzSQLObject.getSQLText(
							 * "SQL.TZOrganizationMgBundle.TzCopyAuthSignOn");
							 * 
							 * List<?> listAuthSignon =
							 * sqlQuery.queryForList(sql, new Object[] {
							 * srcPermID });
							 * 
							 * for (Object srcAuthSignon : listAuthSignon) {
							 * 
							 * Map<String, Object> mapAuthSignon = (Map<String,
							 * Object>) srcAuthSignon;
							 * 
							 * sql =
							 * "SELECT 'Y' FROM PSAUTHSIGNON WHERE CLASSID=? and DAYOFWEEK=?"
							 * ; recExsit = sqlQuery.queryForObject(sql, new
							 * Object[] { permId,mapAuthSignon.get("DAYOFWEEK")
							 * }, "String");
							 * 
							 * if (null == recExsit) {
							 * 
							 * 
							 * 
							 * }
							 * 
							 * }
							 */

						}

						// 复制许可权列表的组件
						sql = tzSQLObject.getSQLText("SQL.TZOrganizationMgBundle.TzCopyAQComsq");

						List<?> listAQComsq = sqlQuery.queryForList(sql, new Object[] { srcPermID });

						for (Object srcAQComsq : listAQComsq) {

							Map<String, Object> mapAQComsq = (Map<String, Object>) srcAQComsq;

							sql = "SELECT 'Y' FROM PS_TZ_AQ_COMSQ_TBL WHERE CLASSID=? and TZ_COM_ID=? and TZ_PAGE_ID=?";
							String strComId = mapAQComsq.get("TZ_COM_ID") == null ? null
									: mapAQComsq.get("TZ_COM_ID").toString();
							String strPageId = mapAQComsq.get("TZ_PAGE_ID") == null ? null
									: mapAQComsq.get("TZ_PAGE_ID").toString();
							recExsit = sqlQuery.queryForObject(sql, new Object[] { permId, strComId, strPageId },
									"String");

							if (null == recExsit) {

								short displayonly = Short.parseShort(mapAQComsq.get("DISPLAYONLY").toString());
								short editFlag = Short.parseShort(mapAQComsq.get("TZ_EDIT_FLG").toString());

								PsTzAqComsqTbl psTzAqComsqTbl = new PsTzAqComsqTbl();

								psTzAqComsqTbl.setClassid(permId);
								psTzAqComsqTbl.setTzComId(strComId);
								psTzAqComsqTbl.setTzPageId(strPageId);
								psTzAqComsqTbl.setDisplayonly(displayonly);
								psTzAqComsqTbl.setTzEditFlg(editFlag);
								psTzAqComsqTbl.setAuthorizedactions(mapAQComsq.get("AUTHORIZEDACTIONS") == null ? null
										: Integer.parseInt(mapAQComsq.get("AUTHORIZEDACTIONS").toString()));
								psTzAqComsqTbl.setRowAddedDttm(dateNow);
								psTzAqComsqTbl.setRowAddedOprid(oprid);
								psTzAqComsqTbl.setRowLastmantDttm(dateNow);
								psTzAqComsqTbl.setRowLastmantOprid(oprid);

								psTzAqComsqTblMapper.insert(psTzAqComsqTbl);

							}

						}

					}

					// 复制到新的角色关系表中
					String classId = tzJgId + "_" + srcClassId;
					classId = classId.toUpperCase();
					if (strPlstBasic.equals(srcClassId)) {
						classId = srcClassId;
					}

					sql = "SELECT 'Y' FROM PSROLECLASS WHERE ROLENAME=? and CLASSID=?";
					recExsit = sqlQuery.queryForObject(sql, new Object[] { roleId, classId }, "String");

					if (null == recExsit) {

						PsRoleclassKey psRoleclassKey = new PsRoleclassKey();

						psRoleclassKey.setRolename(roleId);
						psRoleclassKey.setClassid(classId);

						psRoleclassMapper.insert(psRoleclassKey);
					}

				} // 许可权循环结束

				// 将角色添加到机构里
				sql = "SELECT 'Y' FROM PS_TZ_JG_ROLE_T WHERE TZ_JG_ID=? and ROLENAME=?";
				recExsit = sqlQuery.queryForObject(sql, new Object[] { tzJgId, roleId }, "String");

				if (null == recExsit) {

					PsTzJgRoleT psTzJgRoleT = new PsTzJgRoleT();

					psTzJgRoleT.setTzJgId(tzJgId);
					psTzJgRoleT.setRolename(roleId);
					psTzJgRoleT.setTzRoleType(mapRole.get("TZ_ROLE_TYPE").toString());

					psTzJgRoleTMapper.insert(psTzJgRoleT);

				}

			} // 角色循环结束

		} catch (TzSystemException tse) {
			tse.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = tse.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.getMessage();
		}

		return "{}";
	}

}
