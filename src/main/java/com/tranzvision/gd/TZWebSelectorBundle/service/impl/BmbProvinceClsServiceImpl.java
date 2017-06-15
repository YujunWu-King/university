

package com.tranzvision.gd.TZWebSelectorBundle.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 州省选择器
 * 
 * @author guXda
 * 
 *         PS: TZ_GD_COMMON_PKG:TZ_PROVINCE_CLS
 */
@Service("com.tranzvision.gd.TZWebSelectorBundle.service.impl.BmbProvinceClsServiceImpl")
public class BmbProvinceClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Override
	// 选择省;
	public String tzGetHtmlContent(String strParams) {
		try {
			String pageID = "";
			// String language = "ZHS";
			String skinId = "";
			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strParams);

			// 是否是报名表;
			if (jacksonUtil.containsKey("siteId")) {
				String siteId = jacksonUtil.getString("siteId");
				// 根据站点id查询;
				String sql = "select TZ_SITE_LANG,TZ_SKIN_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
				Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { siteId });
				// language = (String)map.get("TZ_SITE_LANG");
				skinId = (String) map.get("TZ_SKIN_ID");

			} else if (jacksonUtil.containsKey("TPLID")) {
				// 根据报名表模板查询language;
				String TPLID = jacksonUtil.getString("TPLID");
				String sql = "select a.TZ_APP_TPL_LAN,b.TZ_SKIN_ID from PS_TZ_APPTPL_DY_T a, PS_TZ_SITEI_DEFN_T b where a.TZ_JG_ID=b.TZ_JG_ID AND b.TZ_SITEI_ENABLE='Y' and a.TZ_APP_TPL_ID=? limit 0,1";
				Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { TPLID });
				// language = (String)map.get("TZ_APP_TPL_LAN");
				skinId = (String) map.get("TZ_SKIN_ID");
			}

			if (jacksonUtil.containsKey("TZ_PROV_ID")) {
				pageID = jacksonUtil.getString("TZ_PROV_ID");
			}
			String sql = "SELECT DESCR,STATE FROM PS_STATE_TBL WHERE COUNTRY='CHN' order by convert(DESCR using gbk) asc";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			String proInfo = "";
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					String strDesc = (String) list.get(i).get("DESCR");
					// String strState = (String)list.get(i).get("STATE");
					String states = strDesc.substring(0, strDesc.indexOf(" "));					
					proInfo = proInfo + tzGdObject.getHTMLText("HTML.TZWebSelectorBundle.TZ_MPROVINCE_A_HTML", states);
				}
			}

			String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
			imgPath = request.getContextPath() + imgPath + "/" + skinId;
			return tzGdObject.getHTMLText("HTML.TZWebSelectorBundle.TZ_MPROVINCE_SELECTOR_HTML2", proInfo, pageID,
					request.getContextPath(), imgPath);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
