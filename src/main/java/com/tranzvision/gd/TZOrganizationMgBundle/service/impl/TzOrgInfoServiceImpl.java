/**
 * 
 */
package com.tranzvision.gd.TZOrganizationMgBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsTzAqYhxxTblMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTbl;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTblKey;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMessageSetMgBundle.model.PsTzPtXxdyTblKey;
import com.tranzvision.gd.TZMessageSetMgBundle.model.PsTzPtXxjhTbl;
import com.tranzvision.gd.TZOrganizationMgBundle.dao.PsTzJgBaseTMapper;
import com.tranzvision.gd.TZOrganizationMgBundle.dao.PsTzJgLoginbjTMapper;
import com.tranzvision.gd.TZOrganizationMgBundle.dao.PsTzJgMgrTMapper;
import com.tranzvision.gd.TZOrganizationMgBundle.dao.PsTzJgRoleTMapper;
import com.tranzvision.gd.TZOrganizationMgBundle.model.PsTzJgBaseT;
import com.tranzvision.gd.TZOrganizationMgBundle.model.PsTzJgLoginbjT;
import com.tranzvision.gd.TZOrganizationMgBundle.model.PsTzJgMgrTKey;
import com.tranzvision.gd.TZOrganizationMgBundle.model.PsTzJgRoleT;
import com.tranzvision.gd.TZOrganizationMgBundle.model.PsTzJgRoleTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TZUtility;
import com.tranzvision.gd.util.session.TzSession;
import com.tranzvision.gd.util.sql.SqlQuery;

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
	private JacksonUtil jacksonUtil;

	@Autowired
	private SqlQuery sqlQuery;

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

	/**
	 * 新增机构账号信息
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		String conflictKeys = "";
		String errorMsg = "";
		String comma = "";
		try {
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
					String tzJgId = infoData.get("orgId").toString();

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
						// 联系邮箱;
						String tzOrganContactem = infoData.get("orgLxrEmail").toString();
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

						Date dateNow = new Date();
						String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

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
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	/**
	 * 修改消息集合信息
	 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		String errorMsg = "";
		String comma = "";
		try {
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
					String tzJgId = infoData.get("orgId").toString();

					String sql = "select 'Y' from PS_TZ_JG_BASE_T WHERE TZ_JG_ID=?";
					String recExists = sqlQuery.queryForObject(sql, new Object[] { tzJgId }, "String");
					if (null != recExists) {
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

						Date dateNow = new Date();
						String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

						psTzJgBaseT.setRowLastmantDttm(dateNow);
						psTzJgBaseT.setRowLastmantOprid(oprid);

						psTzJgBaseTMapper.updateByPrimaryKey(psTzJgBaseT);

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
	 * 删除机构管理员信息
	 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		try {
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 提交信息
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 消息集合id
				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> infoData = jacksonUtil.getMap("data");

				String tzJgId = infoData.get("orgId").toString();

				if (null == tzJgId || "".equals(tzJgId)) {
					continue;
				}

				if ("USER".equals(typeFlag)) {
					String tzDlzhId = infoData.get("usAccNum").toString();
					if (tzDlzhId != null && !"".equals(tzDlzhId)) {

						// 删除机构管理员
						PsTzAqYhxxTblKey psTzAqYhxxTblKey = new PsTzAqYhxxTblKey();
						psTzAqYhxxTblKey.setTzJgId(tzJgId);
						psTzAqYhxxTblKey.setTzDlzhId(tzDlzhId);

						psTzAqYhxxTblMapper.deleteByPrimaryKey(psTzAqYhxxTblKey);

					}

				} else if ("ROLE".equals(typeFlag)) {
					String rolename = infoData.get("roleName").toString();
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
			fliterForm.orderByArr = orderByArr;

			if ("USER".equals(queryType)) {

				// json数据要的结果字段;
				String[] resultFldArray = { "TZ_JG_ID", "TZ_DLZH_ID", "TZ_REALNAME", };

				// 可配置搜索通用函数;
				Object[] obj = fliterForm.searchFilter(resultFldArray, strParams, numLimit, numStart, errorMsg);

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
	 * 更新机构管理员信息
	 * 
	 * @param mapData
	 * @param errorMsg
	 */
	private void tzEditOrgMemAccountInfo(Map<String, Object> mapData, String[] errorMsg) {

		// 机构管理员信息
		// 机构编号;
		String tzJgId = mapData.get("orgId").toString();
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
	private void tzEditOrgRoleInfo(Map<String, Object> mapData, String[] errorMsg) {
		try {
			// 机构编号;
			String tzJgId = mapData.get("orgId").toString();
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
	private String tzQueryRoleList(String strOrgID, int numLimit, int numStart, String[] errorMsg) {

		return "";
	}

}
