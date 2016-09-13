package com.tranzvision.gd.util.cms.manager.main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.cms.entity.main.ArticleImage;
import com.tranzvision.gd.util.cms.entity.main.Attachment;
import com.tranzvision.gd.util.cms.entity.main.ChannelTpl;
import com.tranzvision.gd.util.cms.entity.main.CmsContent;
import com.tranzvision.gd.util.cms.page.Finder;
import com.tranzvision.gd.util.cms.page.Pagination;

public class ArticleMngImpl extends Manager implements ArticleMng {

	Logger logger = Logger.getLogger(this.getClass());

	@Override
	public CmsContent findArticleById(String id, String chnlid) {
		CmsContent art = null;
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");

			String sql = "SELECT A.TZ_ART_ID,A.TZ_ART_TITLE,A.TZ_ART_TITLE_STYLE,"
					+ " A.TZ_ART_NAME,A.TZ_ART_TYPE1,A.TZ_ART_CONENT," + " A.TZ_OUT_ART_URL,A.TZ_ATTACHSYSFILENA,"
					+ " A.TZ_IMAGE_TITLE,A.TZ_IMAGE_DESC, A.TZ_ATTACHSYSFILENA,B.TZ_SITE_ID, B.TZ_COLU_ID,"
					+ " E.TZ_COLU_NAME,B.TZ_ART_NEWS_DT,B.TZ_ART_PUB_STATE,"
					+ " B.TZ_ART_URL,B.TZ_STATIC_ART_URL,B.TZ_ART_SEQ,"
					+ " B.TZ_MAX_ZD_SEQ,B.TZ_FBZ,B.TZ_BLT_DEPT,B.TZ_LASTMANT_OPRID," + " B.TZ_LASTMANT_DTTM "
					+ " FROM PS_TZ_ART_REC_TBL A,PS_TZ_LM_NR_GL_T B," + " PS_TZ_SITEI_COLU_T E"
					+ " WHERE A.TZ_ART_ID = B.TZ_ART_ID" + " AND B.TZ_SITE_ID = E.TZ_SITEI_ID "
					+ " AND B.TZ_COLU_ID = E.TZ_COLU_ID " + " AND A.TZ_ART_ID = ? AND B.TZ_COLU_ID = ?";

			Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { id, chnlid });
			if (map != null) {
				art = new CmsContent();
				art.setSiteId((String) map.get("TZ_SITE_ID"));
				art.setChnlId((String) map.get("TZ_COLU_ID"));
				art.setChnlName((String) map.get("TZ_COLU_NAME"));
				art.setId((String) map.get("TZ_ART_ID"));
				art.setTitle((String) map.get("TZ_ART_TITLE"));
				art.setStyleTile((String) map.get("TZ_ART_TITLE_STYLE"));
				art.setName((String) map.get("TZ_ART_NAME"));
				art.setContent((String) map.get("TZ_ART_CONENT"));
				art.setType((String) map.get("TZ_ART_TYPE1"));
				art.setOutUrl((String) map.get("TZ_OUT_ART_URL"));

				art.setTitleImageSysfileName((String) map.get("TZ_ATTACHSYSFILENA"));
				art.setImageTitle((String) map.get("TZ_IMAGE_TITLE"));
				art.setImageDesc((String) map.get("TZ_IMAGE_DESC"));

				art.setPublished((Date) map.get("TZ_ART_NEWS_DT"));
				art.setPubstate((String) map.get("TZ_ART_PUB_STATE"));
				art.setPublisher((String) map.get("TZ_FBZ"));
				art.setArtDept((String) map.get("TZ_BLT_DEPT"));
				art.setModifier((String) map.get("TZ_LASTMANT_OPRID"));
				art.setUpdated((Date) map.get("TZ_LASTMANT_DTTM"));
				if (map.get("TZ_ART_SEQ") != null) {
					art.setOrder((int) map.get("TZ_ART_SEQ"));
				}
				if (map.get("TZ_MAX_ZD_SEQ") != null) {
					art.setMaxOrder((long) (map.get("TZ_MAX_ZD_SEQ")));
				}

				String titleSysFileId = (String) map.get("TZ_ATTACHSYSFILENA");
				// 标题图;
				if (titleSysFileId != null && !"".equals(titleSysFileId)) {
					String titleSQL = "select C.TZ_ATTACHFILE_NAME," + " C.TZ_ATT_P_URL,C.TZ_ATT_A_URL,"
							+ " C.TZ_YS_ATTACHSYSNAM,C.TZ_SL_ATTACHSYSNAM" + " from PS_TZ_ART_TITIMG_T C "
							+ " where TZ_ATTACHSYSFILENA=?";
					try {
						Map<String, Object> titleMap = jdbcTemplate.queryForMap(titleSQL,
								new Object[] { titleSysFileId });
						if (titleMap != null) {
							art.setImageName((String) titleMap.get("TZ_ATTACHFILE_NAME"));
							art.setImagePurl((String) titleMap.get("TZ_ATT_P_URL"));
							art.setImageAurl((String) titleMap.get("TZ_ATT_A_URL"));
							art.setYsName((String) titleMap.get("TZ_YS_ATTACHSYSNAM"));
							art.setSlName((String) titleMap.get("TZ_SL_ATTACHSYSNAM"));
						}
					} catch (Exception e) {
					}

				}

				// 活动信息;
				art.setOpenActApp("N");
				String hdSQL = "SELECT D.TZ_START_DT,D.TZ_START_TM," + " D.TZ_END_DT,D.TZ_END_TM,D.TZ_QY_ZXBM "
						+ " from PS_TZ_ART_HD_TBL D where TZ_ART_ID=? LIMIT 0,1";

				try {
					Map<String, Object> hdMap = jdbcTemplate.queryForMap(hdSQL, new Object[] { id });
					if (hdMap != null) {
						art.setStartDate((Date) hdMap.get("TZ_START_DT"));
						art.setStartTime((Date) hdMap.get("TZ_START_TM"));
						art.setEndDate((Date) hdMap.get("TZ_END_DT"));
						art.setEndTime((Date) hdMap.get("TZ_END_TM"));
						String isOpenHdBm = (String) hdMap.get("TZ_QY_ZXBM");
						if (isOpenHdBm == null || "".equals(isOpenHdBm)) {
							isOpenHdBm = "N";
						}
						art.setOpenActApp(isOpenHdBm);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return art;

	}

	@Override
	public List<ArticleImage> findArticleImagesById(String id) {

		List<ArticleImage> rsList = new ArrayList<ArticleImage>();

		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String sql = "SELECT A.TZ_ART_ID,A.TZ_ATTACHSYSFILENA,A.TZ_PRIORITY,A.TZ_IMG_DESCR,A.TZ_IMG_TRS_URL,B.TZ_ATTACHFILE_NAME,B.TZ_ATT_P_URL,B.TZ_ATT_A_URL,B.TZ_YS_ATTACHSYSNAM,B.TZ_SL_ATTACHSYSNAM FROM PS_TZ_ART_PIC_T A,PS_TZ_ART_TPJ_T B WHERE A.TZ_ATTACHSYSFILENA = B.TZ_ATTACHSYSFILENA AND A.TZ_ART_ID = ? ORDER BY A.TZ_PRIORITY";
			try {
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { id });
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						ArticleImage aimg = new ArticleImage();
						Map<String, Object> map = list.get(i);
						aimg.setId((String) map.get("TZ_ATTACHSYSFILENA"));
						aimg.setArtId((String) map.get("TZ_ART_ID"));
						aimg.setPriority((long) map.get("TZ_PRIORITY"));
						aimg.setDesc((String) map.get("TZ_IMG_DESCR"));
						aimg.setPurl((String) map.get("TZ_ATT_P_URL"));
						aimg.setUrl((String) map.get("TZ_ATT_A_URL"));
						aimg.setLink((String) map.get("TZ_IMG_TRS_URL"));
						aimg.setTitle((String) map.get("TZ_ATTACHFILE_NAME"));

						aimg.setYsName((String) map.get("TZ_YS_ATTACHSYSNAM"));
						aimg.setSlName((String) map.get("TZ_SL_ATTACHSYSNAM"));
						String aurl = (String) map.get("TZ_ATT_A_URL");
						String ysName = (String) map.get("TZ_YS_ATTACHSYSNAM");
						String slName = (String) map.get("TZ_SL_ATTACHSYSNAM");
						String bigImageUrl = "";
						String smallImageUrl = "";
						if (aurl.endsWith("/")) {
							bigImageUrl = aurl + ysName;
							smallImageUrl = aurl + slName;
						} else {
							bigImageUrl = aurl + "/" + ysName;
							smallImageUrl = aurl + "/" + slName;
						}
						aimg.setBigImageUrl(bigImageUrl);
						aimg.setSmallImageUrl(smallImageUrl);

						rsList.add(aimg);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rsList;
	}

	@Override
	public List<Attachment> findArticleAttachmentsById(String id) {

		List<Attachment> rsList = new ArrayList<Attachment>();

		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String sql = "SELECT A.TZ_ART_ID,A.TZ_ATTACHSYSFILENA,"
					+ " B.TZ_ATTACHFILE_NAME,B.TZ_ATT_P_URL,B.TZ_ATT_A_URL "
					+ " FROM PS_TZ_ART_FILE_T A,PS_TZ_ART_FJJ_T B "
					+ " WHERE A.TZ_ATTACHSYSFILENA = B.TZ_ATTACHSYSFILENA " + " AND A.TZ_ART_ID = ?";
			try {
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { id });
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {

						Map<String, Object> map = list.get(i);

						Attachment atts = new Attachment();
						atts.setId((String) map.get("TZ_ATTACHSYSFILENA"));
						atts.setArtid((String) map.get("TZ_ART_ID"));
						atts.setTitle((String) map.get("TZ_ATTACHFILE_NAME"));
						atts.setPurl((String) map.get("TZ_ATT_P_URL"));
						String urlPath = (String) map.get("TZ_ATT_A_URL");
						String filename = (String) map.get("TZ_ATTACHSYSFILENA");
						String url = "";
						if (urlPath.endsWith("/")) {
							url = urlPath + filename;
						} else {
							url = urlPath + "/" + filename;
						}
						atts.setUrl(url);

						rsList.add(atts);
					}
				}
			} catch (Exception e) {

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return rsList;
	}

	@Override
	public List<CmsContent> getListByChannelIdsForTag(String channelIds, int orderBy, int first, int count) {
		CmsContent art = null;
		List<CmsContent> rsList = new ArrayList<CmsContent>();
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");

			String sql = "SELECT A.TZ_ART_ID,A.TZ_ART_TITLE,A.TZ_ART_TITLE_STYLE,"
					+ " A.TZ_ART_NAME,A.TZ_ART_TYPE1,A.TZ_ART_CONENT, A.TZ_OUT_ART_URL,A.TZ_ATTACHSYSFILENA,"
					+ " A.TZ_IMAGE_TITLE,A.TZ_IMAGE_DESC, A.TZ_ATTACHSYSFILENA,B.TZ_SITE_ID, B.TZ_COLU_ID,"
					+ " E.TZ_COLU_NAME,B.TZ_ART_NEWS_DT,B.TZ_ART_PUB_STATE,"
					+ " B.TZ_ART_URL,B.TZ_STATIC_ART_URL,B.TZ_ART_SEQ,"
					+ " B.TZ_MAX_ZD_SEQ,B.TZ_FBZ,B.TZ_BLT_DEPT,B.TZ_LASTMANT_OPRID, B.TZ_LASTMANT_DTTM "
					+ " FROM PS_TZ_ART_REC_TBL A,PS_TZ_LM_NR_GL_T B, PS_TZ_SITEI_COLU_T E"
					+ " WHERE A.TZ_ART_ID = B.TZ_ART_ID  AND B.TZ_SITE_ID = E.TZ_SITEI_ID AND B.TZ_ART_PUB_STATE='Y'"
					+ " AND B.TZ_COLU_ID = E.TZ_COLU_ID  AND B.TZ_COLU_ID in (?) " + appendOrder(orderBy)
					+ " LIMIT ?,?";

			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { channelIds, first, count });
			Map<String, Object> map = null;

			for (Object objData : list) {
				map = (Map<String, Object>) objData;
				art = new CmsContent();
				art.setSiteId((String) map.get("TZ_SITE_ID"));
				art.setChnlId((String) map.get("TZ_COLU_ID"));
				art.setChnlName((String) map.get("TZ_COLU_NAME"));
				art.setId((String) map.get("TZ_ART_ID"));
				art.setTitle((String) map.get("TZ_ART_TITLE"));
				art.setStyleTile((String) map.get("TZ_ART_TITLE_STYLE"));
				art.setName((String) map.get("TZ_ART_NAME"));
				art.setContent((String) map.get("TZ_ART_CONENT"));
				art.setType((String) map.get("TZ_ART_TYPE1"));
				art.setOutUrl((String) map.get("TZ_OUT_ART_URL"));

				art.setTitleImageSysfileName((String) map.get("TZ_ATTACHSYSFILENA"));
				art.setImageTitle((String) map.get("TZ_IMAGE_TITLE"));
				art.setImageDesc((String) map.get("TZ_IMAGE_DESC"));

				art.setPublished((Date) map.get("TZ_ART_NEWS_DT"));
				art.setPubstate((String) map.get("TZ_ART_PUB_STATE"));
				art.setPublisher((String) map.get("TZ_FBZ"));
				art.setArtDept((String) map.get("TZ_BLT_DEPT"));
				art.setModifier((String) map.get("TZ_LASTMANT_OPRID"));
				art.setUpdated((Date) map.get("TZ_LASTMANT_DTTM"));
				if (map.get("TZ_ART_SEQ") != null) {
					art.setOrder((int) map.get("TZ_ART_SEQ"));
				}
				if (map.get("TZ_MAX_ZD_SEQ") != null) {
					art.setMaxOrder((long) (map.get("TZ_MAX_ZD_SEQ")));
				}

				String titleSysFileId = (String) map.get("TZ_ATTACHSYSFILENA");
				// 标题图;
				if (titleSysFileId != null && !"".equals(titleSysFileId)) {
					String titleSQL = "select C.TZ_ATTACHFILE_NAME," + " C.TZ_ATT_P_URL,C.TZ_ATT_A_URL,"
							+ " C.TZ_YS_ATTACHSYSNAM,C.TZ_SL_ATTACHSYSNAM" + " from PS_TZ_ART_TITIMG_T C "
							+ " where TZ_ATTACHSYSFILENA=?";
					try {
						Map<String, Object> titleMap = jdbcTemplate.queryForMap(titleSQL,
								new Object[] { titleSysFileId });
						if (titleMap != null) {
							art.setImageName((String) titleMap.get("TZ_ATTACHFILE_NAME"));
							art.setImagePurl((String) titleMap.get("TZ_ATT_P_URL"));
							art.setImageAurl((String) titleMap.get("TZ_ATT_A_URL"));
							art.setYsName((String) titleMap.get("TZ_YS_ATTACHSYSNAM"));
							art.setSlName((String) titleMap.get("TZ_SL_ATTACHSYSNAM"));
						}
					} catch (Exception e) {
					}

				}

				// 活动信息;
				// art.setOpenActApp("N");
				// String hdSQL = "SELECT D.TZ_START_DT,D.TZ_START_TM,
				// D.TZ_END_DT,D.TZ_END_TM,D.TZ_QY_ZXBM "
				// + " from PS_TZ_ART_HD_TBL D where TZ_ART_ID in (?) LIMIT
				// 0,1";
				//
				// try {
				// Map<String, Object> hdMap = jdbcTemplate.queryForMap(hdSQL,
				// new Object[] { ids });
				// if (hdMap != null) {
				// art.setStartDate((Date) hdMap.get("TZ_START_DT"));
				// art.setStartTime((Date) hdMap.get("TZ_START_TM"));
				// art.setEndDate((Date) hdMap.get("TZ_END_DT"));
				// art.setEndTime((Date) hdMap.get("TZ_END_TM"));
				// String isOpenHdBm = (String) hdMap.get("TZ_QY_ZXBM");
				// if (isOpenHdBm == null || "".equals(isOpenHdBm)) {
				// isOpenHdBm = "N";
				// }
				// art.setOpenActApp(isOpenHdBm);
				// }
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				rsList.add(art);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rsList;
	}

	@Override
	public Pagination getPageByChannelIdsForTag(String channelIds, int orderBy, int pageNo, int count, String filters) {
		Finder f = Finder.create();
		String sql = "SELECT A.TZ_ART_ID,A.TZ_ART_TITLE,A.TZ_ART_TITLE_STYLE,"
				+ " A.TZ_ART_NAME,A.TZ_ART_TYPE1,A.TZ_ART_CONENT, A.TZ_OUT_ART_URL,A.TZ_ATTACHSYSFILENA,"
				+ " A.TZ_IMAGE_TITLE,A.TZ_IMAGE_DESC, A.TZ_ATTACHSYSFILENA,B.TZ_SITE_ID, B.TZ_COLU_ID,"
				+ " E.TZ_COLU_NAME,B.TZ_ART_NEWS_DT,B.TZ_ART_PUB_STATE,"
				+ " B.TZ_ART_URL,B.TZ_STATIC_ART_URL,B.TZ_ART_SEQ,"
				+ " B.TZ_MAX_ZD_SEQ,B.TZ_FBZ,B.TZ_BLT_DEPT,B.TZ_LASTMANT_OPRID, B.TZ_LASTMANT_DTTM "
				+ " FROM PS_TZ_ART_REC_TBL A,PS_TZ_LM_NR_GL_T B, PS_TZ_SITEI_COLU_T E"
				+ " WHERE A.TZ_ART_ID = B.TZ_ART_ID  AND B.TZ_SITE_ID = E.TZ_SITEI_ID AND B.TZ_ART_PUB_STATE='Y'"
				+ " AND B.TZ_COLU_ID = E.TZ_COLU_ID  AND B.TZ_COLU_ID in (?) " + appendOrder(orderBy) + " LIMIT ?,?";

		f.append(sql);

		return find(f, pageNo, count, channelIds);
	}

	private Pagination find(Finder f, int pageNo, int count, String channelIds) {
		int totalCount = countQueryResult(f, channelIds);

		Pagination p = new Pagination(pageNo, count, totalCount);

		if (totalCount < 1) {
			p.setList(new ArrayList<CmsContent>());
			return p;
		}
		List<CmsContent> rsList = new ArrayList<CmsContent>();
		int first = p.getFirstResult() + 1;
		int last = p.getFirstResult() + p.getPageSize();
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(f.getOrigHql(), new Object[] { channelIds, first, last });
		Map<String, Object> map = null;
		CmsContent art = null;
		for (Object objData : list) {
			map = (Map<String, Object>) objData;
			art = new CmsContent();
			art.setSiteId((String) map.get("TZ_SITE_ID"));
			art.setChnlId((String) map.get("TZ_COLU_ID"));
			art.setChnlName((String) map.get("TZ_COLU_NAME"));
			art.setId((String) map.get("TZ_ART_ID"));
			art.setTitle((String) map.get("TZ_ART_TITLE"));
			art.setStyleTile((String) map.get("TZ_ART_TITLE_STYLE"));
			art.setName((String) map.get("TZ_ART_NAME"));
			art.setContent((String) map.get("TZ_ART_CONENT"));
			art.setType((String) map.get("TZ_ART_TYPE1"));
			art.setOutUrl((String) map.get("TZ_OUT_ART_URL"));

			art.setTitleImageSysfileName((String) map.get("TZ_ATTACHSYSFILENA"));
			art.setImageTitle((String) map.get("TZ_IMAGE_TITLE"));
			art.setImageDesc((String) map.get("TZ_IMAGE_DESC"));

			art.setPublished((Date) map.get("TZ_ART_NEWS_DT"));
			art.setPubstate((String) map.get("TZ_ART_PUB_STATE"));
			art.setPublisher((String) map.get("TZ_FBZ"));
			art.setArtDept((String) map.get("TZ_BLT_DEPT"));
			art.setModifier((String) map.get("TZ_LASTMANT_OPRID"));
			art.setUpdated((Date) map.get("TZ_LASTMANT_DTTM"));
			if (map.get("TZ_ART_SEQ") != null) {
				art.setOrder((int) map.get("TZ_ART_SEQ"));
			}
			if (map.get("TZ_MAX_ZD_SEQ") != null) {
				art.setMaxOrder((long) (map.get("TZ_MAX_ZD_SEQ")));
			}

			String titleSysFileId = (String) map.get("TZ_ATTACHSYSFILENA");
			// 标题图;
			if (titleSysFileId != null && !"".equals(titleSysFileId)) {
				String titleSQL = "select C.TZ_ATTACHFILE_NAME," + " C.TZ_ATT_P_URL,C.TZ_ATT_A_URL,"
						+ " C.TZ_YS_ATTACHSYSNAM,C.TZ_SL_ATTACHSYSNAM" + " from PS_TZ_ART_TITIMG_T C "
						+ " where TZ_ATTACHSYSFILENA=?";
				try {
					Map<String, Object> titleMap = jdbcTemplate.queryForMap(titleSQL,
							new Object[] { titleSysFileId });
					if (titleMap != null) {
						art.setImageName((String) titleMap.get("TZ_ATTACHFILE_NAME"));
						art.setImagePurl((String) titleMap.get("TZ_ATT_P_URL"));
						art.setImageAurl((String) titleMap.get("TZ_ATT_A_URL"));
						art.setYsName((String) titleMap.get("TZ_YS_ATTACHSYSNAM"));
						art.setSlName((String) titleMap.get("TZ_SL_ATTACHSYSNAM"));
					}
				} catch (Exception e) {
				}

			}
			rsList.add(art);
		}
		p.setList(rsList);
		return p;
	}

	private int countQueryResult(Finder f, String channelIds) {
		int totalCount = 0;
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			totalCount = jdbcTemplate.queryForObject(f.getRowCountHql(), new Object[] { channelIds }, Integer.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalCount;
	}

	private String appendFilter(String filters) {
		if (StringUtils.isBlank(filters)) {
			return "";
		}
		String where = "";
		String[] arrFilter = StringUtils.split(filters, "|");
		for (int i = 0; i < arrFilter.length; i++) {
			String[] filter = StringUtils.split(arrFilter[i], ":");
			if (filter.length < 2) {
				continue;
			} else {
				where = where + " AND BEAN." + filter[0] + " LIKE '%" + filter[1] + "%'";
			}
		}
		return where;
	}

	/**
	 * 获取排序SQL
	 * 
	 * @param orderBy
	 * @return
	 */
	private String appendOrder(int orderBy) {
		StringBuffer orderStr = new StringBuffer("");
		switch (orderBy) {

		case 0:
			// 发布时间降序
			orderStr.append(" order by B.TZ_ART_NEWS_DT desc ");
			return orderStr.toString();
		case 1:
			// 发布时间升序
			orderStr.append(" order by B.TZ_ART_NEWS_DT  ");
			return orderStr.toString();
		case 2:
			// orderStr.append(" order by bean.tz_art_news_dt asc ");
			// return orderStr.toString();
			return "";
		case 3:
			// orderStr.append(" order by bean.tz_weight_seq
			// desc,bean.tz_art_news_dt desc ");
			// return orderStr.toString();
			return "";
		case 4:
			// 发生时间升序
			orderStr.append(" order by A.ROW_LASTMANT_DTTM  ");
			return orderStr.toString();
		case 5:
			// 发生时间降序
			orderStr.append(" order by A.ROW_LASTMANT_DTTM desc ");
			return orderStr.toString();
		default:
			// 发布时间降序
			orderStr.append(" order by B.TZ_ART_NEWS_DT desc ");
			return orderStr.toString();
		}
	}

	@Override
	public List<CmsContent> getListByIdsForTag(String ids, int orderBy) {
		CmsContent art = null;
		List<CmsContent> rsList = new ArrayList<CmsContent>();
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");

			String sql = "SELECT A.TZ_ART_ID,A.TZ_ART_TITLE,A.TZ_ART_TITLE_STYLE,"
					+ " A.TZ_ART_NAME,A.TZ_ART_TYPE1,A.TZ_ART_CONENT, A.TZ_OUT_ART_URL,A.TZ_ATTACHSYSFILENA,"
					+ " A.TZ_IMAGE_TITLE,A.TZ_IMAGE_DESC, A.TZ_ATTACHSYSFILENA,B.TZ_SITE_ID, B.TZ_COLU_ID,"
					+ " E.TZ_COLU_NAME,B.TZ_ART_NEWS_DT,B.TZ_ART_PUB_STATE,"
					+ " B.TZ_ART_URL,B.TZ_STATIC_ART_URL,B.TZ_ART_SEQ,"
					+ " B.TZ_MAX_ZD_SEQ,B.TZ_FBZ,B.TZ_BLT_DEPT,B.TZ_LASTMANT_OPRID, B.TZ_LASTMANT_DTTM "
					+ " FROM PS_TZ_ART_REC_TBL A,PS_TZ_LM_NR_GL_T B, PS_TZ_SITEI_COLU_T E"
					+ " WHERE A.TZ_ART_ID = B.TZ_ART_ID  AND B.TZ_SITE_ID = E.TZ_SITEI_ID AND B.TZ_ART_PUB_STATE='Y'"
					+ " AND B.TZ_COLU_ID = E.TZ_COLU_ID  AND A.TZ_ART_ID in (?) " + appendOrder(orderBy);

			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { ids });
			Map<String, Object> map = null;

			for (Object objData : list) {
				map = (Map<String, Object>) objData;
				art = new CmsContent();
				art.setSiteId((String) map.get("TZ_SITE_ID"));
				art.setChnlId((String) map.get("TZ_COLU_ID"));
				art.setChnlName((String) map.get("TZ_COLU_NAME"));
				art.setId((String) map.get("TZ_ART_ID"));
				art.setTitle((String) map.get("TZ_ART_TITLE"));
				art.setStyleTile((String) map.get("TZ_ART_TITLE_STYLE"));
				art.setName((String) map.get("TZ_ART_NAME"));
				art.setContent((String) map.get("TZ_ART_CONENT"));
				art.setType((String) map.get("TZ_ART_TYPE1"));
				art.setOutUrl((String) map.get("TZ_OUT_ART_URL"));

				art.setTitleImageSysfileName((String) map.get("TZ_ATTACHSYSFILENA"));
				art.setImageTitle((String) map.get("TZ_IMAGE_TITLE"));
				art.setImageDesc((String) map.get("TZ_IMAGE_DESC"));

				art.setPublished((Date) map.get("TZ_ART_NEWS_DT"));
				art.setPubstate((String) map.get("TZ_ART_PUB_STATE"));
				art.setPublisher((String) map.get("TZ_FBZ"));
				art.setArtDept((String) map.get("TZ_BLT_DEPT"));
				art.setModifier((String) map.get("TZ_LASTMANT_OPRID"));
				art.setUpdated((Date) map.get("TZ_LASTMANT_DTTM"));
				if (map.get("TZ_ART_SEQ") != null) {
					art.setOrder((int) map.get("TZ_ART_SEQ"));
				}
				if (map.get("TZ_MAX_ZD_SEQ") != null) {
					art.setMaxOrder((long) (map.get("TZ_MAX_ZD_SEQ")));
				}

				String titleSysFileId = (String) map.get("TZ_ATTACHSYSFILENA");
				// 标题图;
				if (titleSysFileId != null && !"".equals(titleSysFileId)) {
					String titleSQL = "select C.TZ_ATTACHFILE_NAME," + " C.TZ_ATT_P_URL,C.TZ_ATT_A_URL,"
							+ " C.TZ_YS_ATTACHSYSNAM,C.TZ_SL_ATTACHSYSNAM" + " from PS_TZ_ART_TITIMG_T C "
							+ " where TZ_ATTACHSYSFILENA=?";
					try {
						Map<String, Object> titleMap = jdbcTemplate.queryForMap(titleSQL,
								new Object[] { titleSysFileId });
						if (titleMap != null) {
							art.setImageName((String) titleMap.get("TZ_ATTACHFILE_NAME"));
							art.setImagePurl((String) titleMap.get("TZ_ATT_P_URL"));
							art.setImageAurl((String) titleMap.get("TZ_ATT_A_URL"));
							art.setYsName((String) titleMap.get("TZ_YS_ATTACHSYSNAM"));
							art.setSlName((String) titleMap.get("TZ_SL_ATTACHSYSNAM"));
						}
					} catch (Exception e) {
					}

				}

				// 活动信息;
				art.setOpenActApp("N");
				String hdSQL = "SELECT D.TZ_START_DT,D.TZ_START_TM, D.TZ_END_DT,D.TZ_END_TM,D.TZ_QY_ZXBM "
						+ " from PS_TZ_ART_HD_TBL D where TZ_ART_ID in (?) LIMIT 0,1";

				try {
					Map<String, Object> hdMap = jdbcTemplate.queryForMap(hdSQL, new Object[] { ids });
					if (hdMap != null) {
						art.setStartDate((Date) hdMap.get("TZ_START_DT"));
						art.setStartTime((Date) hdMap.get("TZ_START_TM"));
						art.setEndDate((Date) hdMap.get("TZ_END_DT"));
						art.setEndTime((Date) hdMap.get("TZ_END_TM"));
						String isOpenHdBm = (String) hdMap.get("TZ_QY_ZXBM");
						if (isOpenHdBm == null || "".equals(isOpenHdBm)) {
							isOpenHdBm = "N";
						}
						art.setOpenActApp(isOpenHdBm);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				rsList.add(art);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rsList;
	}

	@Override
	public List<ChannelTpl> findChanTpl(String chnlId) {
		List<ChannelTpl> channelList = new ArrayList<ChannelTpl>();
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");

			String sql = "SELECT * FROM PS_TZ_ASSCHNL_T BEAN WHERE BEAN.TZ_COLU_ID = ?";

			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { chnlId });
			Map<String, Object> map = null;
			ChannelTpl ctpl = null;
			for (Object objData : list) {
				map = (Map<String, Object>) objData;
				ctpl = new ChannelTpl();
				ctpl.setChnlid((String) map.get("TZ_COLU_ID"));
				ctpl.setTplid((String) map.get("TZ_TEMP_ID"));
				ctpl.setSiteId((String) map.get("TZ_SITEI_ID"));
				channelList.add(ctpl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return channelList;
	}

	@Override
	public void addChnlTpl(List<ChannelTpl> insertTpl) {
		try {
			Map<String, Object> map = null;
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String sql = null;
			String TZ_SITEI_ID = null;
			for (ChannelTpl ctpl : insertTpl) {
				sql = "SELECT TZ_SITEI_ID FROM PS_TZ_SITEI_COLU_T  WHERE TZ_COLU_ID = ?";
				TZ_SITEI_ID = jdbcTemplate.queryForObject(sql, new Object[] { ctpl.getChnlid() }, String.class);
				sql = "INSERT INTO PS_TZ_ASSCHNL_T (TZ_SITEI_ID,TZ_TEMP_ID,TZ_COLU_ID) VALUES (?,?,?)";
				jdbcTemplate.update(sql, new Object[] { TZ_SITEI_ID, ctpl.getChnlid(), ctpl.getTplid() });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
