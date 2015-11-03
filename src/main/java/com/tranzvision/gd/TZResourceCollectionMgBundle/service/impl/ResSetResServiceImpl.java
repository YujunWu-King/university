package com.tranzvision.gd.TZResourceCollectionMgBundle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZResourceCollectionMgBundle.dao.PsTzPtZyxxTblMapper;
import com.tranzvision.gd.TZResourceCollectionMgBundle.model.PsTzPtZyxxTbl;
import com.tranzvision.gd.TZResourceCollectionMgBundle.model.PsTzPtZyxxTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TZUtility;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author tang; 功能说明：资源集合管理管理相关类; 原PS类：TZ_GD_RESSET_PKG:TZ_GD_RESSET_RES_CLS
 */
@Service("com.tranzvision.gd.TZResourceCollectionMgBundle.service.impl.ResSetResServiceImpl")
public class ResSetResServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private JacksonUtil jacksonUtil;
	@Autowired
	private PsTzPtZyxxTblMapper psTzPtZyxxTblMapper;
	
	/* 获取资源信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("resSetID") && jacksonUtil.containsKey("resourceID")) {
				// 资源集合编号;
				String strResSetID = jacksonUtil.getString("resSetID");
				// 资源编号;
				String strResourceID = jacksonUtil.getString("resourceID");
				PsTzPtZyxxTblKey psTzPtZyxxTblKey = new PsTzPtZyxxTblKey();
				psTzPtZyxxTblKey.setTzZyjhId(strResSetID);
				psTzPtZyxxTblKey.setTzResId(strResourceID);
				PsTzPtZyxxTbl psTzPtZyxxTbl = psTzPtZyxxTblMapper.selectByPrimaryKey(psTzPtZyxxTblKey);
				if (psTzPtZyxxTbl != null) {
					strRet = "{\"resSetID\":\"" + TZUtility.transFormchar(psTzPtZyxxTbl.getTzZyjhId())
							+ "\",\"resourceID\":\"" + TZUtility.transFormchar(psTzPtZyxxTbl.getTzResId())
							+ "\",\"resourceName\":\"" + TZUtility.transFormchar(psTzPtZyxxTbl.getTzResMc()) 
							+ "\",\"fileType\":\"" + TZUtility.transFormchar(psTzPtZyxxTbl.getTzResFileType())
							+ "\",\"filePath\":\"" + TZUtility.transFormchar(psTzPtZyxxTbl.getTzResFilePath())
							+ "\",\"fileName\":\"" + TZUtility.transFormchar(psTzPtZyxxTbl.getTzResFileName())+ "\"}";
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该资源信息数据不存在";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "该资源信息数据不存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/* 新增资源信息 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 资源集合编号;
				String strResSetID = jacksonUtil.getString("resSetID");
				// 资源编号;
				String strResourceID = jacksonUtil.getString("resourceID");
				// 资源名称;
				String strResourceName = jacksonUtil.getString("resourceName");
			    // 文件类型;
				String strFileType = jacksonUtil.getString("fileType");
			    // 文件路径;
			    String strFilePath = jacksonUtil.getString("filePath");
			    // 文件名称;
			    String strFileName = jacksonUtil.getString("fileName");

				String sql = "select COUNT(1) from PS_TZ_PT_ZYXX_TBL WHERE TZ_ZYJH_ID=? AND TZ_RES_ID=?";
				int count = jdbcTemplate.queryForObject(sql, new Object[] { strResSetID ,strResourceID}, "Integer");
				if (count > 0) {
					errMsg[0] = "1";
					errMsg[1] = "资源编号为：" + strResourceID + "的信息已经存在，请修改资源编号。";
				} else {
					PsTzPtZyxxTbl psTzPtZyxxTbl = new PsTzPtZyxxTbl();
					psTzPtZyxxTbl.setTzZyjhId(strResSetID);
					psTzPtZyxxTbl.setTzResId(strResourceID);
					psTzPtZyxxTbl.setTzResMc(strResourceName);
					psTzPtZyxxTbl.setTzResFileType(strFileType);
					psTzPtZyxxTbl.setTzResFilePath(strFilePath);
					psTzPtZyxxTbl.setTzResFileName(strFileName);
					psTzPtZyxxTblMapper.insert(psTzPtZyxxTbl);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	
	/* 修改资源信息 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 资源集合编号;
				String strResSetID = jacksonUtil.getString("resSetID");
				// 资源编号;
				String strResourceID = jacksonUtil.getString("resourceID");
				// 资源名称;
				String strResourceName = jacksonUtil.getString("resourceName");
			    // 文件类型;
				String strFileType = jacksonUtil.getString("fileType");
			    // 文件路径;
			    String strFilePath = jacksonUtil.getString("filePath");
			    // 文件名称;
			    String strFileName = jacksonUtil.getString("fileName");

				String sql = "select COUNT(1) from PS_TZ_PT_ZYXX_TBL WHERE TZ_ZYJH_ID=? AND TZ_RES_ID=?";
				int count = jdbcTemplate.queryForObject(sql, new Object[] { strResSetID ,strResourceID}, "Integer");
				if (count > 0) {
					PsTzPtZyxxTbl psTzPtZyxxTbl = new PsTzPtZyxxTbl();
					psTzPtZyxxTbl.setTzZyjhId(strResSetID);
					psTzPtZyxxTbl.setTzResId(strResourceID);
					psTzPtZyxxTbl.setTzResMc(strResourceName);
					psTzPtZyxxTbl.setTzResFileType(strFileType);
					psTzPtZyxxTbl.setTzResFilePath(strFilePath);
					psTzPtZyxxTbl.setTzResFileName(strFileName);
					psTzPtZyxxTblMapper.updateByPrimaryKey(psTzPtZyxxTbl);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "资源编号为：" + strResourceID + "的信息不存在。";
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

}
