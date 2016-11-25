package com.tranzvision.gd.util.cms.manager.main;

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
		// System.out.println("id:" + id);
		// System.out.println("chnlid:" + chnlid);
		CmsContent art = null;
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");

			String sql = "";
			Map<String, Object> map = null;
			if (id != null && !id.equals("")) {
				sql = "SELECT A.TZ_ART_ID,A.TZ_ART_TITLE,A.TZ_ART_TITLE_STYLE,"
						+ " A.TZ_ABOUT,A.TZ_METAKEYS,A.TZ_METADESC, A.TZ_ART_SHORTTITLE,A.TZ_SUBHEAD,"
						+ " A.TZ_TXT1,A.TZ_TXT2,A.TZ_TXT3, A.TZ_TXT4,A.TZ_LONG1,"
						+ " A.TZ_LONG2,A.TZ_LONG3,A.TZ_DATE1, A.TZ_DATE2,"
						+ " A.TZ_ART_NAME,A.TZ_ART_TYPE1,A.TZ_ART_CONENT," + " A.TZ_OUT_ART_URL,A.TZ_ATTACHSYSFILENA,"
						+ " A.TZ_IMAGE_TITLE,A.TZ_IMAGE_DESC, A.TZ_ATTACHSYSFILENA,B.TZ_SITE_ID, B.TZ_COLU_ID,"
						+ " E.TZ_COLU_NAME,B.TZ_ART_NEWS_DT,B.TZ_ART_PUB_STATE,"
						+ " B.TZ_ART_URL,B.TZ_STATIC_ART_URL,B.TZ_ART_SEQ,"
						+ " B.TZ_MAX_ZD_SEQ,B.TZ_FBZ,B.TZ_BLT_DEPT,B.TZ_LASTMANT_OPRID," + " B.TZ_LASTMANT_DTTM "
						+ " FROM PS_TZ_ART_REC_TBL A,PS_TZ_LM_NR_GL_T B," + " PS_TZ_SITEI_COLU_T E"
						+ " WHERE A.TZ_ART_ID = B.TZ_ART_ID" + " AND B.TZ_SITE_ID = E.TZ_SITEI_ID "
						+ " AND B.TZ_COLU_ID = E.TZ_COLU_ID " + " AND A.TZ_ART_ID = ? AND B.TZ_COLU_ID = ?";
				map = jdbcTemplate.queryForMap(sql, new Object[] { id, chnlid });
			} else {
				sql = "SELECT A.TZ_ART_ID,A.TZ_ART_TITLE,A.TZ_ART_TITLE_STYLE,"
						+ " A.TZ_ABOUT,A.TZ_METAKEYS,A.TZ_METADESC, A.TZ_ART_SHORTTITLE,A.TZ_SUBHEAD,"
						+ " A.TZ_TXT1,A.TZ_TXT2,A.TZ_TXT3, A.TZ_TXT4,A.TZ_LONG1,"
						+ " A.TZ_LONG2,A.TZ_LONG3,A.TZ_DATE1, A.TZ_DATE2,"
						+ " A.TZ_ART_NAME,A.TZ_ART_TYPE1,A.TZ_ART_CONENT," + " A.TZ_OUT_ART_URL,A.TZ_ATTACHSYSFILENA,"
						+ " A.TZ_IMAGE_TITLE,A.TZ_IMAGE_DESC, A.TZ_ATTACHSYSFILENA,B.TZ_SITE_ID, B.TZ_COLU_ID,"
						+ " E.TZ_COLU_NAME,B.TZ_ART_NEWS_DT,B.TZ_ART_PUB_STATE,"
						+ " B.TZ_ART_URL,B.TZ_STATIC_ART_URL,B.TZ_ART_SEQ,"
						+ " B.TZ_MAX_ZD_SEQ,B.TZ_FBZ,B.TZ_BLT_DEPT,B.TZ_LASTMANT_OPRID," + " B.TZ_LASTMANT_DTTM "
						+ " FROM PS_TZ_ART_REC_TBL A,PS_TZ_LM_NR_GL_T B," + " PS_TZ_SITEI_COLU_T E"
						+ " WHERE A.TZ_ART_ID = B.TZ_ART_ID"
						+ " AND B.TZ_SITE_ID = E.TZ_SITEI_ID AND B.TZ_ART_PUB_STATE='Y' "
						+ " AND B.TZ_COLU_ID = E.TZ_COLU_ID "
						+ " AND B.TZ_COLU_ID = ? order by B.TZ_ART_NEWS_DT desc limit 0,1";
				map = jdbcTemplate.queryForMap(sql, new Object[] { chnlid });
			}

			// Map<String, Object> map = jdbcTemplate.queryForMap(sql, new
			// Object[] { id, chnlid });
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
					// art.setOrder((int) map.get("TZ_ART_SEQ"));
					art.setOrder(((Long) map.get("TZ_ART_SEQ")).intValue());
				}

				if (map.get("TZ_MAX_ZD_SEQ") != null) {
					art.setMaxOrder((long) (map.get("TZ_MAX_ZD_SEQ")));
				}

				if (map.get("TZ_STATIC_ART_URL") != null) {
					art.setUrl((String) (map.get("TZ_STATIC_ART_URL")));
				}

				if (map.get("TZ_ABOUT") != null) {
					art.setAbout((String) (map.get("TZ_ABOUT")));
				}

				if (map.get("TZ_METAKEYS") != null) {
					art.setMetakeys((String) (map.get("TZ_METAKEYS")));
				}

				if (map.get("TZ_METADESC") != null) {
					art.setMetadesc((String) (map.get("TZ_METADESC")));
				}

				if (map.get("TZ_ART_SHORTTITLE") != null) {
					art.setArt_shorttitle((String) (map.get("TZ_ART_SHORTTITLE")));
				}

				if (map.get("TZ_SUBHEAD") != null) {
					art.setSubhead((String) (map.get("TZ_SUBHEAD")));
				}

				if (map.get("TZ_TXT1") != null) {
					art.setTxt1((String) (map.get("TZ_TXT1")));
				}

				if (map.get("TZ_TXT2") != null) {
					art.setTxt2((String) (map.get("TZ_TXT2")));
				}

				if (map.get("TZ_TXT3") != null) {
					art.setTxt3((String) (map.get("TZ_TXT3")));
				}

				if (map.get("TZ_TXT4") != null) {
					art.setTxt4((String) (map.get("TZ_TXT4")));
				}

				if (map.get("TZ_LONG1") != null) {
					art.setLong1((String) (map.get("TZ_LONG1")));
				}

				if (map.get("TZ_LONG2") != null) {
					art.setLong2((String) (map.get("TZ_LONG2")));
				}

				if (map.get("TZ_LONG3") != null) {
					art.setLong3((String) (map.get("TZ_LONG3")));
				}

				if (map.get("TZ_DATE1") != null) {
					art.setDate1((Date) (map.get("TZ_DATE1")));
				}

				if (map.get("TZ_DATE2") != null) {
					art.setDate2((Date) (map.get("TZ_DATE2")));
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
							String imagePathP = (String) titleMap.get("TZ_ATT_P_URL");
							String imagePathA = (String) titleMap.get("TZ_ATT_A_URL");

							if (!imagePathP.trim().endsWith("/")) {
								imagePathP = imagePathP + "/";
							}

							// 修改 By caoy
							if (!imagePathA.trim().endsWith("/")) {
								imagePathA = imagePathA + "/";
							}

							art.setImagePurl(imagePathP);
							art.setImageAurl(imagePathA);
							art.setYsName((String) titleMap.get("TZ_YS_ATTACHSYSNAM"));
							art.setSlName((String) titleMap.get("TZ_SL_ATTACHSYSNAM"));
						}
					} catch (Exception e) {
					}

				}

				// 活动信息;
				if (id != null && !id.equals("")) {
					art.setOpenActApp("N");
					// Date dateNow = new Date();
					String hdSQL = "SELECT D.TZ_START_DT,D.TZ_START_TM,"
							+ " D.TZ_END_DT,D.TZ_END_TM,D.TZ_QY_ZXBM,D.TZ_NACT_ADDR,D.TZ_HD_CS,D.TZ_XWS  "
							+ " from PS_TZ_ART_HD_TBL D where D.TZ_ART_ID=? ";

					try {
						List<Map<String, Object>> list = jdbcTemplate.queryForList(hdSQL, new Object[] { id });
						Map<String, Object> hdMap = null;
						if (list != null && list.size() == 1) {
							hdMap = list.get(0);
							art.setStartDate((Date) hdMap.get("TZ_START_DT"));
							art.setStartTime((Date) hdMap.get("TZ_START_TM"));
							art.setEndDate((Date) hdMap.get("TZ_END_DT"));
							art.setEndTime((Date) hdMap.get("TZ_END_TM"));
							art.setHd_city((String) hdMap.get("TZ_HD_CS"));
							art.setHd_address((String) hdMap.get("TZ_NACT_ADDR"));
							art.setHd_totalNumber(((Long) hdMap.get("TZ_XWS")).intValue());
							
							
							if (hdMap.get("TZ_QY_ZXBM") != null) {
								String isOpenHdBm = (String) hdMap.get("TZ_QY_ZXBM");
								if (isOpenHdBm == null || "".equals(isOpenHdBm)) {
									isOpenHdBm = "N";
								}
								art.setOpenActApp(isOpenHdBm);
							} else {
								art.setOpenActApp("N");
							}
							// hdSQL = "select count(*) from PS_TZ_NAUDLIST_T
							// where TZ_ART_ID=? ";
							// int active = jdbcTemplate.queryForObject(hdSQL,
							// new Object[] { id }, Integer.class);
							// art.setHd_activeNumber(active);
							// art.setHd_watingNumber(art.getHd_totalNumber() -
							// active);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
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
		// System.out.println("id:" + id);
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String sql = "SELECT A.TZ_ART_ID,A.TZ_ATTACHSYSFILENA,A.TZ_PRIORITY,A.TZ_IMG_DESCR,A.TZ_IMG_TRS_URL,B.TZ_ATTACHFILE_NAME,B.TZ_ATT_P_URL,B.TZ_ATT_A_URL,B.TZ_YS_ATTACHSYSNAM,B.TZ_SL_ATTACHSYSNAM FROM PS_TZ_ART_PIC_T A,PS_TZ_ART_TPJ_T B WHERE A.TZ_ATTACHSYSFILENA = B.TZ_ATTACHSYSFILENA AND A.TZ_ART_ID = ? ORDER BY A.TZ_PRIORITY";
			try {
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { id });
				// System.out.println("size" + list.size());
				ArticleImage aimg = null;
				Map<String, Object> map = null;
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						aimg = new ArticleImage();
						map = list.get(i);
						aimg.setId((String) map.get("TZ_ATTACHSYSFILENA"));
						aimg.setArtId((String) map.get("TZ_ART_ID"));
						aimg.setPriority((long) map.get("TZ_PRIORITY"));
						aimg.setDesc((String) map.get("TZ_IMG_DESCR"));

						String imagePathP = (String) map.get("TZ_ATT_P_URL");
						String imagePathA = (String) map.get("TZ_ATT_A_URL");

						if (!imagePathP.trim().endsWith("/")) {
							imagePathP = imagePathP + "/";
						}

						// 修改 By caoy
						if (!imagePathA.trim().endsWith("/")) {
							imagePathA = imagePathA + "/";
						}

						aimg.setPurl(imagePathP);
						aimg.setUrl(imagePathA);
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
					+ " A.TZ_ABOUT,A.TZ_METAKEYS,A.TZ_METADESC, A.TZ_ART_SHORTTITLE,A.TZ_SUBHEAD,"
					+ " A.TZ_TXT1,A.TZ_TXT2,A.TZ_TXT3, A.TZ_TXT4,A.TZ_LONG1,"
					+ " A.TZ_LONG2,A.TZ_LONG3,A.TZ_DATE1, A.TZ_DATE2,"
					+ " A.TZ_ART_NAME,A.TZ_ART_TYPE1,A.TZ_ART_CONENT, A.TZ_OUT_ART_URL,A.TZ_ATTACHSYSFILENA,"
					+ " A.TZ_IMAGE_TITLE,A.TZ_IMAGE_DESC, A.TZ_ATTACHSYSFILENA,B.TZ_SITE_ID, B.TZ_COLU_ID,"
					+ " E.TZ_COLU_NAME,B.TZ_ART_NEWS_DT,B.TZ_ART_PUB_STATE,"
					+ " B.TZ_ART_URL,B.TZ_STATIC_ART_URL,B.TZ_ART_SEQ,"
					+ " B.TZ_MAX_ZD_SEQ,B.TZ_FBZ,B.TZ_BLT_DEPT,B.TZ_LASTMANT_OPRID, B.TZ_LASTMANT_DTTM "
					+ " FROM PS_TZ_ART_REC_TBL A,PS_TZ_LM_NR_GL_T B, PS_TZ_SITEI_COLU_T E"
					+ " WHERE A.TZ_ART_ID = B.TZ_ART_ID  AND B.TZ_SITE_ID = E.TZ_SITEI_ID AND B.TZ_ART_PUB_STATE='Y'"
					+ " AND B.TZ_COLU_ID = E.TZ_COLU_ID  AND B.TZ_COLU_ID in (" + channelIds + ") "
					+ appendOrder(orderBy) + " LIMIT ?,?";

			// System.out.println("sql:" + sql);
			// System.out.println("first:" + first);
			// System.out.println("count:" + count);

			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { first, count });
			// System.out.println("size:" + list.size());
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
					art.setOrder(((Long) map.get("TZ_ART_SEQ")).intValue());
				}
				if (map.get("TZ_MAX_ZD_SEQ") != null) {
					art.setMaxOrder((long) (map.get("TZ_MAX_ZD_SEQ")));
				}

				if (map.get("TZ_STATIC_ART_URL") != null) {
					art.setUrl((String) (map.get("TZ_STATIC_ART_URL")));
				}

				if (map.get("TZ_ABOUT") != null) {
					art.setAbout((String) (map.get("TZ_ABOUT")));
				}

				if (map.get("TZ_METAKEYS") != null) {
					art.setMetakeys((String) (map.get("TZ_METAKEYS")));
				}

				if (map.get("TZ_METADESC") != null) {
					art.setMetadesc((String) (map.get("TZ_METADESC")));
				}

				if (map.get("TZ_ART_SHORTTITLE") != null) {
					art.setArt_shorttitle((String) (map.get("TZ_ART_SHORTTITLE")));
				}

				if (map.get("TZ_SUBHEAD") != null) {
					art.setSubhead((String) (map.get("TZ_SUBHEAD")));
				}

				if (map.get("TZ_TXT1") != null) {
					art.setTxt1((String) (map.get("TZ_TXT1")));
				}

				if (map.get("TZ_TXT2") != null) {
					art.setTxt2((String) (map.get("TZ_TXT2")));
				}

				if (map.get("TZ_TXT3") != null) {
					art.setTxt3((String) (map.get("TZ_TXT3")));
				}

				if (map.get("TZ_TXT4") != null) {
					art.setTxt4((String) (map.get("TZ_TXT4")));
				}

				if (map.get("TZ_LONG1") != null) {
					art.setLong1((String) (map.get("TZ_LONG1")));
				}

				if (map.get("TZ_LONG2") != null) {
					art.setLong2((String) (map.get("TZ_LONG2")));
				}

				if (map.get("TZ_LONG3") != null) {
					art.setLong3((String) (map.get("TZ_LONG3")));
				}

				if (map.get("TZ_DATE1") != null) {
					art.setDate1((Date) (map.get("TZ_DATE1")));
				}

				if (map.get("TZ_DATE2") != null) {
					art.setDate2((Date) (map.get("TZ_DATE2")));
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
							String imagePathP = (String) titleMap.get("TZ_ATT_P_URL");
							String imagePathA = (String) titleMap.get("TZ_ATT_A_URL");

							if ((imagePathP.lastIndexOf("\\") + 1) != imagePathP.length()
									|| (imagePathP.lastIndexOf("/") + 1) != imagePathP.length()) {
								imagePathP = imagePathP + "/";
							}

							// 修改 By caoy
							if ((imagePathA.lastIndexOf("\\") + 1) != imagePathA.length()
									|| (imagePathA.lastIndexOf("/") + 1) != imagePathA.length()) {
								imagePathA = imagePathA + "/";
							}

							art.setImagePurl(imagePathP);
							art.setImageAurl(imagePathA);
							art.setYsName((String) titleMap.get("TZ_YS_ATTACHSYSNAM"));
							art.setSlName((String) titleMap.get("TZ_SL_ATTACHSYSNAM"));
						}
					} catch (Exception e) {
					}

				}

				// 活动信息;
				art.setOpenActApp("N");
				String hdSQL = "SELECT D.TZ_START_DT,D.TZ_START_TM,"
						+ " D.TZ_END_DT,D.TZ_END_TM,D.TZ_QY_ZXBM,D.TZ_NACT_ADDR,D.TZ_HD_CS "
						+ " from PS_TZ_ART_HD_TBL D where TZ_ART_ID=? ";

				try {
					List<Map<String, Object>> listxx = jdbcTemplate.queryForList(hdSQL, new Object[] { art.getId() });
					Map<String, Object> hdMap = null;
					if (listxx != null && listxx.size() > 0) {
						for (int i = 0; i < listxx.size(); i++) {
							hdMap = listxx.get(i);
							art.setStartDate((Date) hdMap.get("TZ_START_DT"));
							art.setStartTime((Date) hdMap.get("TZ_START_TM"));
							art.setEndDate((Date) hdMap.get("TZ_END_DT"));
							art.setEndTime((Date) hdMap.get("TZ_END_TM"));
							art.setHd_city((String) hdMap.get("TZ_HD_CS"));
							art.setHd_address((String) hdMap.get("TZ_NACT_ADDR"));
							String isOpenHdBm = (String) hdMap.get("TZ_QY_ZXBM");
							if (isOpenHdBm == null || "".equals(isOpenHdBm)) {
								isOpenHdBm = "N";
							}
							art.setOpenActApp(isOpenHdBm);
						}
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
	public Pagination getPageByChannelIdsForTag(String channelIds, int orderBy, int pageNo, int count, String filters) {
		Finder f = Finder.create();
		String sql = "SELECT A.TZ_ART_ID,A.TZ_ART_TITLE,A.TZ_ART_TITLE_STYLE,"
				+ " A.TZ_ABOUT,A.TZ_METAKEYS,A.TZ_METADESC, A.TZ_ART_SHORTTITLE,A.TZ_SUBHEAD,"
				+ " A.TZ_TXT1,A.TZ_TXT2,A.TZ_TXT3, A.TZ_TXT4,A.TZ_LONG1,"
				+ " A.TZ_LONG2,A.TZ_LONG3,A.TZ_DATE1, A.TZ_DATE2,"
				+ " A.TZ_ART_NAME,A.TZ_ART_TYPE1,A.TZ_ART_CONENT, A.TZ_OUT_ART_URL,A.TZ_ATTACHSYSFILENA,"
				+ " A.TZ_IMAGE_TITLE,A.TZ_IMAGE_DESC, A.TZ_ATTACHSYSFILENA,B.TZ_SITE_ID, B.TZ_COLU_ID,"
				+ " E.TZ_COLU_NAME,B.TZ_ART_NEWS_DT,B.TZ_ART_PUB_STATE,"
				+ " B.TZ_ART_URL,B.TZ_STATIC_ART_URL,B.TZ_ART_SEQ,"
				+ " B.TZ_MAX_ZD_SEQ,B.TZ_FBZ,B.TZ_BLT_DEPT,B.TZ_LASTMANT_OPRID, B.TZ_LASTMANT_DTTM "
				+ " FROM PS_TZ_ART_REC_TBL A,PS_TZ_LM_NR_GL_T B, PS_TZ_SITEI_COLU_T E"
				+ " WHERE A.TZ_ART_ID = B.TZ_ART_ID  AND B.TZ_SITE_ID = E.TZ_SITEI_ID AND B.TZ_ART_PUB_STATE='Y'"
				+ " AND B.TZ_COLU_ID = E.TZ_COLU_ID  AND B.TZ_COLU_ID in (" + channelIds + ") " + appendOrder(orderBy)
				+ " LIMIT ?,?";
		System.out.println("sql:" + sql);

		f.append(sql);
		System.out.println("sql:" + f.getRowCountHql());
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
		int first = p.getFirstResult();
		int last = p.getFirstResult() + p.getPageSize();
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
		System.out.println("first:" + first);
		System.out.println("last:" + last);

		System.out.println("size:" + p.getPageSize());
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(f.getOrigHql(), new Object[] { first, p.getPageSize() });
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
				// art.setOrder((int) map.get("TZ_ART_SEQ"));
				art.setOrder(((Long) map.get("TZ_ART_SEQ")).intValue());
			}
			if (map.get("TZ_MAX_ZD_SEQ") != null) {
				art.setMaxOrder((long) (map.get("TZ_MAX_ZD_SEQ")));
			}

			if (map.get("TZ_STATIC_ART_URL") != null) {
				art.setUrl((String) (map.get("TZ_STATIC_ART_URL")));
			}

			if (map.get("TZ_ABOUT") != null) {
				art.setAbout((String) (map.get("TZ_ABOUT")));
			}

			if (map.get("TZ_METAKEYS") != null) {
				art.setMetakeys((String) (map.get("TZ_METAKEYS")));
			}

			if (map.get("TZ_METADESC") != null) {
				art.setMetadesc((String) (map.get("TZ_METADESC")));
			}

			if (map.get("TZ_ART_SHORTTITLE") != null) {
				art.setArt_shorttitle((String) (map.get("TZ_ART_SHORTTITLE")));
			}

			if (map.get("TZ_SUBHEAD") != null) {
				art.setSubhead((String) (map.get("TZ_SUBHEAD")));
			}

			if (map.get("TZ_TXT1") != null) {
				art.setTxt1((String) (map.get("TZ_TXT1")));
			}

			if (map.get("TZ_TXT2") != null) {
				art.setTxt2((String) (map.get("TZ_TXT2")));
			}

			if (map.get("TZ_TXT3") != null) {
				art.setTxt3((String) (map.get("TZ_TXT3")));
			}

			if (map.get("TZ_TXT4") != null) {
				art.setTxt4((String) (map.get("TZ_TXT4")));
			}

			if (map.get("TZ_LONG1") != null) {
				art.setLong1((String) (map.get("TZ_LONG1")));
			}

			if (map.get("TZ_LONG2") != null) {
				art.setLong2((String) (map.get("TZ_LONG2")));
			}

			if (map.get("TZ_LONG3") != null) {
				art.setLong3((String) (map.get("TZ_LONG3")));
			}

			if (map.get("TZ_DATE1") != null) {
				art.setDate1((Date) (map.get("TZ_DATE1")));
			}

			if (map.get("TZ_DATE2") != null) {
				art.setDate2((Date) (map.get("TZ_DATE2")));
			}

			String titleSysFileId = (String) map.get("TZ_ATTACHSYSFILENA");
			// 标题图;
			if (titleSysFileId != null && !"".equals(titleSysFileId)) {
				String titleSQL = "select C.TZ_ATTACHFILE_NAME," + " C.TZ_ATT_P_URL,C.TZ_ATT_A_URL,"
						+ " C.TZ_YS_ATTACHSYSNAM,C.TZ_SL_ATTACHSYSNAM" + " from PS_TZ_ART_TITIMG_T C "
						+ " where TZ_ATTACHSYSFILENA=?";
				try {
					Map<String, Object> titleMap = jdbcTemplate.queryForMap(titleSQL, new Object[] { titleSysFileId });
					if (titleMap != null) {
						art.setImageName((String) titleMap.get("TZ_ATTACHFILE_NAME"));
						String imagePathP = (String) titleMap.get("TZ_ATT_P_URL");
						String imagePathA = (String) titleMap.get("TZ_ATT_A_URL");

						if ((imagePathP.lastIndexOf("\\") + 1) != imagePathP.length()
								|| (imagePathP.lastIndexOf("/") + 1) != imagePathP.length()) {
							imagePathP = imagePathP + "/";
						}

						// 修改 By caoy
						if ((imagePathA.lastIndexOf("\\") + 1) != imagePathA.length()
								|| (imagePathA.lastIndexOf("/") + 1) != imagePathA.length()) {
							imagePathA = imagePathA + "/";
						}

						art.setImagePurl(imagePathP);
						art.setImageAurl(imagePathA);
						art.setYsName((String) titleMap.get("TZ_YS_ATTACHSYSNAM"));
						art.setSlName((String) titleMap.get("TZ_SL_ATTACHSYSNAM"));
					}
				} catch (Exception e) {
				}

			}

			// 活动信息;
			art.setOpenActApp("N");
			String hdSQL = "SELECT D.TZ_START_DT,D.TZ_START_TM,"
					+ " D.TZ_END_DT,D.TZ_END_TM,D.TZ_QY_ZXBM,D.TZ_NACT_ADDR,D.TZ_HD_CS  "
					+ " from PS_TZ_ART_HD_TBL D where TZ_ART_ID=? ";

			try {
				List<Map<String, Object>> listxx = jdbcTemplate.queryForList(hdSQL, new Object[] { art.getId() });
				Map<String, Object> hdMap = null;
				if (listxx != null && listxx.size() > 0) {
					for (int i = 0; i < listxx.size(); i++) {
						hdMap = listxx.get(i);
						art.setStartDate((Date) hdMap.get("TZ_START_DT"));
						art.setStartTime((Date) hdMap.get("TZ_START_TM"));
						art.setEndDate((Date) hdMap.get("TZ_END_DT"));
						art.setEndTime((Date) hdMap.get("TZ_END_TM"));
						art.setHd_city((String) hdMap.get("TZ_HD_CS"));
						art.setHd_address((String) hdMap.get("TZ_NACT_ADDR"));
						String isOpenHdBm = (String) hdMap.get("TZ_QY_ZXBM");
						if (isOpenHdBm == null || "".equals(isOpenHdBm)) {
							isOpenHdBm = "N";
						}
						art.setOpenActApp(isOpenHdBm);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
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
			totalCount = jdbcTemplate.queryForObject(f.getRowCountHql(), Integer.class);
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
			// 权重（置顶）降序和发布时间降序
			orderStr.append(" order by B.TZ_MAX_ZD_SEQ desc,B.TZ_ART_NEWS_DT desc ");
			return orderStr.toString();
		case 1:
			// 权重（置顶）降序和发布时间升序
			orderStr.append(" order by B.TZ_MAX_ZD_SEQ desc,B.TZ_ART_NEWS_DT ");
			return orderStr.toString();
		case 2:
			// 权重（置顶）降序和序列的降序
			orderStr.append(" order by B.TZ_MAX_ZD_SEQ desc,B.TZ_ART_SEQ desc ");
			return orderStr.toString();
		case 3:
			// 权重（置顶）降序和序列的升序
			orderStr.append(" order by B.TZ_MAX_ZD_SEQ desc,B.TZ_ART_SEQ ");
			return orderStr.toString();
		case 4:
			// 权重（置顶）降序和发生时间升序
			orderStr.append(" order by B.TZ_MAX_ZD_SEQ desc,A.ROW_LASTMANT_DTTM ");
			return orderStr.toString();
		case 5:
			// 权重（置顶）降序和发生时间降序
			orderStr.append(" order by B.TZ_MAX_ZD_SEQ desc,A.ROW_LASTMANT_DTTM desc ");
			return orderStr.toString();
		default:
			// 权重（置顶）降序和发布时间降序
			orderStr.append(" order by B.TZ_MAX_ZD_SEQ desc,B.TZ_ART_NEWS_DT desc ");
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
					+ " A.TZ_ABOUT,A.TZ_METAKEYS,A.TZ_METADESC, A.TZ_ART_SHORTTITLE,A.TZ_SUBHEAD,"
					+ " A.TZ_TXT1,A.TZ_TXT2,A.TZ_TXT3, A.TZ_TXT4,A.TZ_LONG1,"
					+ " A.TZ_LONG2,A.TZ_LONG3,A.TZ_DATE1, A.TZ_DATE2,"
					+ " A.TZ_ART_NAME,A.TZ_ART_TYPE1,A.TZ_ART_CONENT, A.TZ_OUT_ART_URL,A.TZ_ATTACHSYSFILENA,"
					+ " A.TZ_IMAGE_TITLE,A.TZ_IMAGE_DESC, A.TZ_ATTACHSYSFILENA,B.TZ_SITE_ID, B.TZ_COLU_ID,"
					+ " E.TZ_COLU_NAME,B.TZ_ART_NEWS_DT,B.TZ_ART_PUB_STATE,"
					+ " B.TZ_ART_URL,B.TZ_STATIC_ART_URL,B.TZ_ART_SEQ,"
					+ " B.TZ_MAX_ZD_SEQ,B.TZ_FBZ,B.TZ_BLT_DEPT,B.TZ_LASTMANT_OPRID, B.TZ_LASTMANT_DTTM "
					+ " FROM PS_TZ_ART_REC_TBL A,PS_TZ_LM_NR_GL_T B, PS_TZ_SITEI_COLU_T E"
					+ " WHERE A.TZ_ART_ID = B.TZ_ART_ID  AND B.TZ_SITE_ID = E.TZ_SITEI_ID AND B.TZ_ART_PUB_STATE='Y'"
					+ " AND B.TZ_COLU_ID = E.TZ_COLU_ID  AND A.TZ_ART_ID in (" + ids + ") " + appendOrder(orderBy);

			// System.out.println("sql:" + sql);
			// System.out.println("ids:" + ids);

			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

			// System.out.println("size:" + list.size());
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
					// art.setOrder((int) map.get("TZ_ART_SEQ"));
					art.setOrder(((Long) map.get("TZ_ART_SEQ")).intValue());
				}
				if (map.get("TZ_MAX_ZD_SEQ") != null) {
					art.setMaxOrder((long) (map.get("TZ_MAX_ZD_SEQ")));
				}

				if (map.get("TZ_STATIC_ART_URL") != null) {
					art.setUrl((String) (map.get("TZ_STATIC_ART_URL")));
				}

				if (map.get("TZ_ABOUT") != null) {
					art.setAbout((String) (map.get("TZ_ABOUT")));
				}

				if (map.get("TZ_METAKEYS") != null) {
					art.setMetakeys((String) (map.get("TZ_METAKEYS")));
				}

				if (map.get("TZ_METADESC") != null) {
					art.setMetadesc((String) (map.get("TZ_METADESC")));
				}

				if (map.get("TZ_ART_SHORTTITLE") != null) {
					art.setArt_shorttitle((String) (map.get("TZ_ART_SHORTTITLE")));
				}

				if (map.get("TZ_SUBHEAD") != null) {
					art.setSubhead((String) (map.get("TZ_SUBHEAD")));
				}

				if (map.get("TZ_TXT1") != null) {
					art.setTxt1((String) (map.get("TZ_TXT1")));
				}

				if (map.get("TZ_TXT2") != null) {
					art.setTxt2((String) (map.get("TZ_TXT2")));
				}

				if (map.get("TZ_TXT3") != null) {
					art.setTxt3((String) (map.get("TZ_TXT3")));
				}

				if (map.get("TZ_TXT4") != null) {
					art.setTxt4((String) (map.get("TZ_TXT4")));
				}

				if (map.get("TZ_LONG1") != null) {
					art.setLong1((String) (map.get("TZ_LONG1")));
				}

				if (map.get("TZ_LONG2") != null) {
					art.setLong2((String) (map.get("TZ_LONG2")));
				}

				if (map.get("TZ_LONG3") != null) {
					art.setLong3((String) (map.get("TZ_LONG3")));
				}

				if (map.get("TZ_DATE1") != null) {
					art.setDate1((Date) (map.get("TZ_DATE1")));
				}

				if (map.get("TZ_DATE2") != null) {
					art.setDate2((Date) (map.get("TZ_DATE2")));
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
							String imagePathP = (String) titleMap.get("TZ_ATT_P_URL");
							String imagePathA = (String) titleMap.get("TZ_ATT_A_URL");

							if ((imagePathP.lastIndexOf("\\") + 1) != imagePathP.length()
									|| (imagePathP.lastIndexOf("/") + 1) != imagePathP.length()) {
								imagePathP = imagePathP + "/";
							}

							// 修改 By caoy
							if ((imagePathA.lastIndexOf("\\") + 1) != imagePathA.length()
									|| (imagePathA.lastIndexOf("/") + 1) != imagePathA.length()) {
								imagePathA = imagePathA + "/";
							}

							art.setImagePurl(imagePathP);
							art.setImageAurl(imagePathA);
							art.setYsName((String) titleMap.get("TZ_YS_ATTACHSYSNAM"));
							art.setSlName((String) titleMap.get("TZ_SL_ATTACHSYSNAM"));
						}
					} catch (Exception e) {
					}

				}

				// 活动信息;
				art.setOpenActApp("N");
				String hdSQL = "SELECT D.TZ_START_DT,D.TZ_START_TM,"
						+ " D.TZ_END_DT,D.TZ_END_TM,D.TZ_QY_ZXBM,D.TZ_NACT_ADDR,D.TZ_HD_CS  "
						+ " from PS_TZ_ART_HD_TBL D where TZ_ART_ID=? ";

				try {
					List<Map<String, Object>> listxx = jdbcTemplate.queryForList(hdSQL, new Object[] { art.getId() });
					Map<String, Object> hdMap = null;
					if (listxx != null && listxx.size() > 0) {
						for (int i = 0; i < listxx.size(); i++) {
							hdMap = listxx.get(i);
							art.setStartDate((Date) hdMap.get("TZ_START_DT"));
							art.setStartTime((Date) hdMap.get("TZ_START_TM"));
							art.setEndDate((Date) hdMap.get("TZ_END_DT"));
							art.setEndTime((Date) hdMap.get("TZ_END_TM"));
							art.setHd_city((String) hdMap.get("TZ_HD_CS"));
							art.setHd_address((String) hdMap.get("TZ_NACT_ADDR"));
							String isOpenHdBm = (String) hdMap.get("TZ_QY_ZXBM");
							if (isOpenHdBm == null || "".equals(isOpenHdBm)) {
								isOpenHdBm = "N";
							}
							art.setOpenActApp(isOpenHdBm);
						}
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
