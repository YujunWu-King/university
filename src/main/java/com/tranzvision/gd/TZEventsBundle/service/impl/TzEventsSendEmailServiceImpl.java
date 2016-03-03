/**
 * 
 */
package com.tranzvision.gd.TZEventsBundle.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 给报名人发送邮件页面操作类，原PS：TZ_GD_BMRGL_PKG:TZ_GD_EMAIL_CLS
 * 
 * @author SHIHUA
 * @since 2016-02-25
 */
@Service("com.tranzvision.gd.TZEventsBundle.service.impl.TzEventsSendEmailServiceImpl")
public class TzEventsSendEmailServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Override
	public String tzGetJsonData(String strParams) {
		String strRet = "";

		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strParams);

			// 活动编号
			String strActivityId = jacksonUtil.getString("activityId");
			// 报名人信息
			List<?> listBmrIds = jacksonUtil.getList("bmrIds");
			
			String jgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			// 创建邮件发送听众
			String crtAudi = createTaskServiceImpl.createAudience("",jgid,"报名人邮件发送", "HDBM");

			if (!"".equals(crtAudi)) {

				// 添加听众成员
				for (Object obj : listBmrIds) {
					String bmrId = String.valueOf(obj);
					String sql = "select TZ_CYR_NAME,OPRID from PS_TZ_NAUDLIST_T where TZ_ART_ID=? and TZ_HD_BMR_ID=?";
					Map<String, Object> mapBmr = sqlQuery.queryForMap(sql, new Object[] { strActivityId, bmrId });

					sql = "SELECT TZ_ZY_SJ,TZ_CY_SJ,TZ_ZY_EMAIL,TZ_CY_EMAIL,TZ_WEIXIN FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY='HDBM' AND TZ_LYDX_ID=?";
					Map<String, Object> mapBmrInfo = sqlQuery.queryForMap(sql, new Object[] { bmrId });

					String strName = "";
					String oprid = "";
					if (null != mapBmr) {
						strName = mapBmr.get("TZ_CYR_NAME") == null ? "" : String.valueOf(mapBmr.get("TZ_CYR_NAME"));
						oprid = mapBmr.get("OPRID") == null ? "" : String.valueOf(mapBmr.get("OPRID"));
					}

					String mainPhone = "";
					String cyPhone = "";
					String mainEmail = "";
					String cyEmail = "";
					String wxh = "";
					if (null != mapBmrInfo) {
						mainPhone = mapBmrInfo.get("TZ_ZY_SJ") == null ? ""
								: String.valueOf(mapBmrInfo.get("TZ_ZY_SJ"));
						cyPhone = mapBmrInfo.get("TZ_CY_SJ") == null ? "" : String.valueOf(mapBmrInfo.get("TZ_CY_SJ"));
						mainEmail = mapBmrInfo.get("TZ_ZY_EMAIL") == null ? ""
								: String.valueOf(mapBmrInfo.get("TZ_ZY_EMAIL"));
						cyEmail = mapBmrInfo.get("TZ_CY_EMAIL") == null ? ""
								: String.valueOf(mapBmrInfo.get("TZ_CY_EMAIL"));
						wxh = mapBmrInfo.get("TZ_WEIXIN") == null ? "" : String.valueOf(mapBmrInfo.get("TZ_WEIXIN"));
					}

					createTaskServiceImpl.addAudCy(crtAudi,strName, "", mainPhone, cyPhone, mainEmail, cyEmail, wxh, oprid, "",
							strActivityId, "");
				}

				Map<String, Object> mapRet = new HashMap<String, Object>();
				mapRet.put("audienceId", crtAudi);
				strRet = jacksonUtil.Map2json(mapRet);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return strRet;
	}

}
