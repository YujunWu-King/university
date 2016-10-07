package com.tranzvision.gd.util.cms.manager.main;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.cms.entity.main.CmsChannel;
import com.tranzvision.gd.util.cms.entity.main.CmsTemplate;

public class ChannelMngImpl extends Manager implements ChannelMng {
	
	//站点栏目信息;
	@Override
	public CmsChannel findById(String siteId,String chnlId) {
		CmsChannel channel = null;
		try{
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil(); 
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String sql = "select TZ_SITEI_ID,TZ_COLU_ID,TZ_COLU_NAME,TZ_COLU_TYPE,"
					+ " TZ_TEMP_ID,TZ_CONT_TYPE,TZ_CONT_TEMP,TZ_MENU_TYPE_ID "
					+ " from PS_TZ_SITEI_COLU_T where TZ_SITEI_ID = ? "
					+ " and TZ_COLU_ID=?";
			Map<String, Object> map = jdbcTemplate.queryForMap(sql,new Object[]{siteId,chnlId});
			if(map != null){
				channel = new CmsChannel();
				channel.setSiteId((String)map.get("TZ_SITEI_ID"));
				channel.setId((String)map.get("TZ_COLU_ID"));
				channel.setColuName((String)map.get("TZ_COLU_NAME"));
				channel.setColuType((String)map.get("TZ_COLU_TYPE"));
				channel.setColuTplid((String)map.get("TZ_TEMP_ID"));
				channel.setArtType((String)map.get("TZ_CONT_TYPE"));
				channel.setArtTplId((String)map.get("TZ_CONT_TEMP"));
				channel.setMenuTypeId((String)map.get("TZ_MENU_TYPE_ID"));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return channel;
	}
	
	public String getSitePath(String siteId) {
		String sql = "select TZ_SITEI_PATH from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=? ";
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			List<Map<String, Object>> pathList = jdbcTemplate.queryForList(sql.toString(), new Object[] { siteId });
			if (pathList != null && pathList.size() == 1) {
				Map<String, Object> mapNode = pathList.get(0);
				if (mapNode.get("TZ_SITEI_PATH") != null) {
					String path = mapNode.get("TZ_SITEI_PATH").toString();
					if (!path.equals("/")) {
						if (!path.endsWith("/")) {
							path = path + "/";
						}
					}
					return path;
				} else {
					return "";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public CmsTemplate findChnlContentTpl(String siteId, String chnlId) {
		//查询栏目使用的模板;
		String sql = "select A.TZ_SITEI_ID,A.TZ_TEMP_ID,A.TZ_TEMP_STATE,"
					+ " A.TZ_TEMP_NAME,A.TZ_TEMP_TYPE,A.TZ_TEMP_PCCODE,A.TZ_TEMP_MSCODE "
					+ " from PS_TZ_SITEI_TEMP_T A,PS_TZ_SITEI_COLU_T B"
					+ " where A.TZ_SITEI_ID = B.TZ_SITEI_ID AND A.TZ_TEMP_ID = B.TZ_CONT_TEMP"
					+ " AND B.TZ_SITEI_ID=? and B.TZ_COLU_ID =?";
		Object[] obj = new Object[]{siteId,chnlId};
		CmsTemplate tpl = this.findTpl(sql, obj);

		return tpl;
	}
}
