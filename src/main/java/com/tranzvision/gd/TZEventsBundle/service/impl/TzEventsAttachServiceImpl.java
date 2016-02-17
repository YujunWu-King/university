/**
 * 
 */
package com.tranzvision.gd.TZEventsBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 获取活动附件信息，原PS：TZ_GD_HDGL:ActivityAttach
 * 
 * @author SHIHUA
 * @since 2016-02-04
 */
@Service("com.tranzvision.gd.TZEventsBundle.service.impl.TzEventsAttachServiceImpl")
public class TzEventsAttachServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery sqlQuery;

	@Override
	@Transactional
	public String tzQuery(String strParams, String[] errMsg) {
		String strRet = "";

		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strParams);

			String tzArtId = jacksonUtil.getString("activityId");
			String sql = "select count(1) from PS_TZ_ART_FILE_T where TZ_ART_ID=?";

			int total = sqlQuery.queryForObject(sql, new Object[] { tzArtId }, "int");

			sql = "select TZ_FILE_ID,TZ_ATTACH_NAME,TZ_ATTACH_URL,TZ_ATTSYSFILENAME from PS_TZ_ART_FILE_T where TZ_ART_ID=? order by TZ_PRIORITY";
			List<Map<String, Object>> listData = sqlQuery.queryForList(sql, new Object[] { tzArtId });

			ArrayList<Map<String,Object>> listJson = new ArrayList<Map<String,Object>>();
			
			for (Map<String, Object> mapData : listData) {

				String attachmentID = mapData.get("TZ_FILE_ID") == null ? ""
						: String.valueOf(mapData.get("TZ_FILE_ID"));
				String attachmentName = mapData.get("TZ_ATTACH_NAME") == null ? ""
						: String.valueOf(mapData.get("TZ_ATTACH_NAME"));
				String attachmentUrl = mapData.get("TZ_ATTACH_URL") == null ? ""
						: String.valueOf(mapData.get("TZ_ATTACH_URL"));
				String sysFileName = mapData.get("TZ_ATTSYSFILENAME") == null ? ""
						: String.valueOf(mapData.get("TZ_ATTSYSFILENAME"));

				String ctxPath = request.getContextPath();
				if(attachmentUrl.contains("/")){
					attachmentUrl = ctxPath + attachmentUrl + sysFileName;
				}else{
					attachmentUrl = ctxPath + attachmentUrl + "/" + sysFileName;
				}
				
				Map<String,Object> mapJson = new HashMap<String,Object>();
				
				mapJson.put("attachmentID", attachmentID);
				mapJson.put("attachmentName", attachmentName);
				mapJson.put("attachmentUrl", attachmentUrl);
				
				listJson.add(mapJson);
			}
			
			Map<String,Object> mapRet = new HashMap<String,Object>();
			mapRet.put("total", total);
			mapRet.put("root", listJson);
			
			strRet = jacksonUtil.Map2json(mapRet);

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "查询失败！" + e.getMessage();
		}

		return strRet;
	}

}
