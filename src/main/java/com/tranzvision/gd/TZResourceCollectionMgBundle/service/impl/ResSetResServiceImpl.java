package com.tranzvision.gd.TZResourceCollectionMgBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZResourceCollectionMgBundle.dao.PsTzPtZyxxTblMapper;
import com.tranzvision.gd.TZResourceCollectionMgBundle.model.PsTzPtZyxxTbl;
import com.tranzvision.gd.TZResourceCollectionMgBundle.model.PsTzPtZyxxTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
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
	private PsTzPtZyxxTblMapper psTzPtZyxxTblMapper;
	
	/* 获取资源信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
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
					returnJsonMap.put("resSetID", psTzPtZyxxTbl.getTzZyjhId());
					returnJsonMap.put("resourceID", psTzPtZyxxTbl.getTzResId());
					returnJsonMap.put("resourceName", psTzPtZyxxTbl.getTzResMc());
					returnJsonMap.put("fileType", psTzPtZyxxTbl.getTzResFileType());
					returnJsonMap.put("filePath", psTzPtZyxxTbl.getTzResFilePath());
					returnJsonMap.put("fileName", psTzPtZyxxTbl.getTzResFileName());
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
		return jacksonUtil.Map2json(returnJsonMap);
	}

	/* 新增资源信息 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
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
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
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
