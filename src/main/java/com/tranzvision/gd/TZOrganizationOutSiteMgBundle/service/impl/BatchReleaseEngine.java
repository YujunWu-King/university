package com.tranzvision.gd.TZOrganizationOutSiteMgBundle.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import com.tranzvision.gd.TZWebSiteInfoBundle.service.impl.ArtContentHtml;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.cms.CmsBean;
import com.tranzvision.gd.util.cms.CmsUtils;

/**
 * @author caoy
 * @version 创建时间：2016年9月19日 下午4:34:45 类说明 批量发布引擎
 */
public class BatchReleaseEngine extends Thread {
	private String batchType;
	private String objectId;
	private String opr;
	private String id;
	private String rootparth;
	private String contentPath;

	private String dir;

	public BatchReleaseEngine(String batchType, String objectId, String opr, String id, String rootparth,
			String contentPath, String dir) {
		this.batchType = batchType;
		this.objectId = objectId;
		this.opr = opr;
		this.id = id;
		this.rootparth = rootparth;
		this.contentPath = contentPath;
		this.dir = dir;
	}

	public void run() {
		// A:按站点发布 B:按栏目发布
		// 按站点发布的循序 1.按栏目
		// 菜单 对应 模板
		// 模板里面 写栏目
		// 栏目 分级

		try {

			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			// 插入 批量发布表
			StringBuffer sb = new StringBuffer();
			sb.append("INSERT INTO PS_TZ_BATCH_RELEASE_T ");
			sb.append("(TZ_BATCH_RELEASE_ID,TZ_BATCH_OBJECT_ID,TZ_BATCH_RELEASE_TYPE,");
			sb.append("BATCH_DTTM,BATCH_OPRID,TZ_BATCH_RELEASE_STATE) VALUES ('");
			sb.append(this.id);
			sb.append("','");
			sb.append(this.objectId);
			sb.append("','");
			sb.append(this.batchType);
			sb.append("',now(),'");
			sb.append(this.opr);
			// Y:发布成功 N:发布失败 C:发布中
			sb.append("','C') ");
			System.out.println(sb.toString());
			jdbcTemplate.update(sb.toString());
			boolean flag = false;
			// A:按站点发布 B:按栏目发布
			if (batchType.equals("A")) {
				flag = this.releaseSite(objectId);
			} else if (batchType.equals("B")) {
				flag = this.releaseColu(objectId);
			}

			if (flag) {
				sb = new StringBuffer();
				sb.append("UPDATE PS_TZ_BATCH_RELEASE_T ");
				sb.append("SET END_DTTM = now(),TZ_BATCH_RELEASE_STATE='Y' ");
				sb.append("WHERE TZ_BATCH_RELEASE_ID='");
				sb.append(this.id);
				sb.append("'");
				System.out.println(sb.toString());
				jdbcTemplate.update(sb.toString());
			} else {
				sb = new StringBuffer();
				sb.append("UPDATE PS_TZ_BATCH_RELEASE_T ");
				sb.append("SET END_DTTM = now(),TZ_BATCH_RELEASE_STATE='N' ");
				sb.append("WHERE TZ_BATCH_RELEASE_ID='");
				sb.append(this.id);
				sb.append("'");
				System.out.println(sb.toString());
				jdbcTemplate.update(sb.toString());
			}
			System.out.println("End Thread");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 批量发布 站点
	 * 
	 * @param siteId
	 * @return
	 */
	public boolean releaseSite(String siteId) {
		boolean rs = false;
		StringBuffer sql = new StringBuffer(
				"select TZ_MENU_ID,TZ_SITEI_ID,TZ_MENU_NAME,TZ_MENU_TYPE,TZ_MENU_LEVEL,TZ_MENU_XH,");
		sql.append("ifnull(TZ_F_MENU_ID,\"\") TZ_F_MENU_ID,");
		sql.append("ifnull(TZ_D_MENU_ID,\"\") TZ_D_MENU_ID,");
		sql.append("ifnull(TZ_MENU_PATH,\"\") TZ_MENU_PATH,");
		sql.append("ifnull(TZ_TEMP_ID,\"\") TZ_TEMP_ID,");
		sql.append("ifnull(TZ_PAGE_NAME,\"\") TZ_PAGE_NAME,");
		sql.append("ifnull(TZ_MENU_STYLE,\"\") TZ_MENU_STYLE");
		sql.append(" from PS_TZ_SITEI_MENU_T");
		sql.append(" where TZ_SITEI_ID=? ");
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			List<Map<String, Object>> menu = jdbcTemplate.queryForList(sql.toString(), new Object[] { siteId });

			// 首先发布 所有栏目下面的文章
			sql = new StringBuffer("select TZ_COLU_ID,TZ_ART_ID");
			sql.append(" from PS_TZ_LM_NR_GL_T");
			sql.append(" where TZ_SITE_ID=? AND TZ_ART_PUB_STATE='Y'");
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), new Object[] { siteId });

			if (list != null) {
				// 获取静态路径地址
				String SQL2 = "SELECT TZ_COLU_PATH FROM PS_TZ_SITEI_COLU_T WHERE  TZ_SITEI_ID = ? AND TZ_COLU_LEVEL = '0' LIMIT 1";
				String strBasePath = jdbcTemplate.queryForObject(SQL2, new Object[] { siteId }, String.class);
				Map<String, Object> mapNode = null;
				for (Object objNode : list) {
					mapNode = (Map<String, Object>) objNode;
					rs = this.content(siteId, strBasePath, mapNode.get("TZ_COLU_ID").toString(),
							mapNode.get("TZ_ART_ID").toString());
					System.out.println("CREATE Content:" + mapNode.get("TZ_ART_ID").toString() + ",result:" + rs);
					if (!rs) {
						return rs;
					}
				}
			}

			if (menu != null) {
				Map<String, Object> mapNode = null;
				for (Object objNode : menu) {
					mapNode = (Map<String, Object>) objNode;
					rs = this.menu(mapNode.get("TZ_MENU_ID").toString(), mapNode.get("TZ_MENU_TYPE").toString(), siteId,
							menu);
					System.out.println("CREATE Menu:" + mapNode.get("TZ_MENU_ID").toString() + ",result:" + rs);
					if (!rs) {
						return rs;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			rs = false;
		}
		return rs;
	}

	/**
	 * 按栏目发布 只负责本级栏目的发布，不管下级栏目
	 * 
	 * @param Colu
	 * @return
	 */
	public boolean releaseColu(String ColuId) {
		boolean rs = false;
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			// System.out.println("ColuId:" + ColuId);
			String sql = "SELECT TZ_SITEI_ID,TZ_COLU_PATH FROM PS_TZ_SITEI_COLU_T WHERE TZ_COLU_ID = ?  LIMIT 1";
			Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { ColuId });
			String siteId = map.get("TZ_SITEI_ID").toString();
			String strColuPath = map.get("TZ_COLU_PATH").toString();

			StringBuffer sb = new StringBuffer("select TZ_ART_ID");
			sb.append(" from PS_TZ_LM_NR_GL_T");
			sb.append(" where TZ_COLU_ID=? AND TZ_SITE_ID=? AND TZ_ART_PUB_STATE='Y'");
			List<Map<String, Object>> colu = jdbcTemplate.queryForList(sb.toString(), new Object[] { ColuId, siteId });

			if (colu != null) {
				// 获取静态路径地址
				sql = "SELECT TZ_COLU_PATH FROM PS_TZ_SITEI_COLU_T WHERE  TZ_SITEI_ID = ? AND TZ_COLU_LEVEL = '0' LIMIT 1";
				String strBasePath = jdbcTemplate.queryForObject(sql, new Object[] { siteId }, String.class);
				Map<String, Object> mapNode = null;
				for (Object objNode : colu) {
					mapNode = (Map<String, Object>) objNode;
					rs = this.content(siteId, strBasePath, strColuPath, ColuId, mapNode.get("TZ_ART_ID").toString());
					System.out.println("CREATE Content:" + mapNode.get("TZ_ART_ID").toString() + ",result:" + rs);
					if (!rs) {
						return rs;
					}
				}
			}

			// 栏目中的文章发布完毕，下一步跟新栏目相关的菜单
			sb = new StringBuffer();
			sb.append("SELECT A.TZ_MENU_ID,A.TZ_MENU_TYPE FROM PS_TZ_SITEI_MENU_T A,PS_TZ_ASSCHNL_T B ");
			sb.append("WHERE A.TZ_TEMP_ID = B.TZ_TEMP_ID AND B.TZ_COLU_ID=? ");
			List<Map<String, Object>> thisMenu = jdbcTemplate.queryForList(sb.toString(), new Object[] { ColuId });

			if (thisMenu != null && thisMenu.size() > 0) {

				sb = new StringBuffer(
						"select TZ_MENU_ID,TZ_SITEI_ID,TZ_MENU_NAME,TZ_MENU_TYPE,TZ_MENU_LEVEL,TZ_MENU_XH,");
				sb.append("ifnull(TZ_F_MENU_ID,\"\") TZ_F_MENU_ID,");
				sb.append("ifnull(TZ_D_MENU_ID,\"\") TZ_D_MENU_ID,");
				sb.append("ifnull(TZ_MENU_PATH,\"\") TZ_MENU_PATH,");
				sb.append("ifnull(TZ_TEMP_ID,\"\") TZ_TEMP_ID,");
				sb.append("ifnull(TZ_PAGE_NAME,\"\") TZ_PAGE_NAME,");
				sb.append("ifnull(TZ_MENU_STYLE,\"\") TZ_MENU_STYLE");
				sb.append(" from PS_TZ_SITEI_MENU_T");
				sb.append(" where TZ_SITEI_ID=? ");
				List<Map<String, Object>> menu = jdbcTemplate.queryForList(sb.toString(), new Object[] { siteId });

				Map<String, Object> mapNode = null;
				for (Object objNode : thisMenu) {
					mapNode = (Map<String, Object>) objNode;
					rs = this.menu(mapNode.get("TZ_MENU_ID").toString(), mapNode.get("TZ_MENU_TYPE").toString(), siteId,
							menu);
					System.out.println("CREATE Menu:" + mapNode.get("TZ_MENU_ID").toString() + ",result:" + rs);
					if (!rs) {
						return rs;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return rs;
	}

	/**
	 * 静态化菜单
	 * 
	 * @param menuId
	 * @param menuType
	 * @param siteId
	 * @param menuList
	 * @return
	 */
	private boolean menu(String menuId, String menuType, String siteId, List<Map<String, Object>> menuList) {
		boolean br = false;
		try {

			// A:PAGE B:BOOK
			CmsUtils cu = new CmsUtils();
			CmsBean bean = null;
			if (menuType.equals("A")) {
				bean = cu.menuPage(siteId, menuId, contentPath, menuList, "1");
			} else {
				bean = cu.menuBook(siteId, menuId, menuList);
			}

			if (bean != null) {
				String strFileName = bean.getHtmlName();
				String menuHtml = bean.getHtml();
				String strFilePath = bean.getPath();
				if (menuHtml != null && !menuHtml.equals("") && strFileName != null && !strFileName.equals("")
						&& strFilePath != null && !strFilePath.equals("")) {
					// System.out.println("strFileName:" + strFileName);
					// System.out.println("strFilePath:" + strFilePath);
					// GetSpringBeanUtil getSpringBeanUtil = new
					// GetSpringBeanUtil();
					// FileManageServiceImpl fileManageServiceImpl =
					// (FileManageServiceImpl) getSpringBeanUtil
					// .getAutowiredSpringBean("FileManage");
					// 更新文件
					// br = fileManageServiceImpl.UpdateFile(strFilePath,
					// strFileName, menuHtml.getBytes());

					String ndir = this.dir + File.separator + strFilePath;
					br = this.staticFile(menuHtml, ndir, strFileName);
				} else {
					return true;
				}
			} else {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return br;
	}

	/**
	 * 静态化文章
	 * 
	 * @param siteId
	 * @param strBasePath
	 * @param coluId
	 * @param artId
	 */
	private boolean content(String siteId, String strBasePath, String coluId, String artId) {
		boolean br = false;
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");

			// 本级栏目路径
			String strColuPath = "";
			String sql = "SELECT TZ_COLU_PATH FROM PS_TZ_SITEI_COLU_T WHERE TZ_SITEI_ID = ? AND TZ_COLU_ID = ? LIMIT 1";
			strColuPath = jdbcTemplate.queryForObject(sql, new Object[] { siteId, coluId }, String.class);

			sql = "SELECT ifnull(TZ_STATIC_AOTO_NAME,\"\") TZ_STATIC_AOTO_NAME,ifnull(TZ_STATIC_NAME,\"\") TZ_STATIC_NAME FROM PS_TZ_LM_NR_GL_T WHERE TZ_SITE_ID = ? AND TZ_COLU_ID = ? AND TZ_ART_ID = ? LIMIT 1";
			Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { siteId, coluId, artId });

			// 自动命名文章名
			String strAutoStaticName = map.get("TZ_STATIC_AOTO_NAME").toString();
			// 手工命名的文章名
			String strOriginStaticName = map.get("TZ_STATIC_NAME").toString();
			// 静态化文件名
			String strFileName = "";
			// 静态化路径
			String strFilePath = "";
			if (strBasePath != null && !"".equals(strBasePath)) {
				if (!strBasePath.startsWith("/")) {
					strBasePath = "/" + strBasePath;
				}
				if (strBasePath.endsWith("/")) {
					strBasePath = strBasePath.substring(0, strBasePath.length() - 1);
				}
			}
			if (strColuPath != null && !"".equals(strColuPath)) {
				if (!strColuPath.startsWith("/")) {
					strColuPath = "/" + strColuPath;
				}
				if (strColuPath.endsWith("/")) {
					strColuPath = strColuPath.substring(0, strColuPath.length() - 1);
				}
			}
			strFilePath = strBasePath + strColuPath;

			// 静态化
			// System.out.println("strFilePath:" + strFilePath);
			// System.out.println("strOriginStaticName:" + strOriginStaticName);
			// System.out.println("strAutoStaticName:" + strAutoStaticName);

			if (strOriginStaticName != null && !strOriginStaticName.equals("")) {
				strFileName = strOriginStaticName;
			} else {
				strFileName = strAutoStaticName;
			}

			if (strFileName != null && !strFileName.equals("")) {
				if (!strFileName.toLowerCase().endsWith(".html")) {
					strFileName = strFileName + ".html";
				}

				// System.out.println("strFileName:" + strFileName);

				ArtContentHtml artContentHtml = (ArtContentHtml) getSpringBeanUtil.getAutowiredSpringBean("ArtContent");
				// 解析的模板内容;
				String contentHtml = artContentHtml.getContentHtml(siteId, coluId, artId, contentPath);
				if (contentHtml != null && !contentHtml.equals("")) {
					// 更新文件

					// 更新文件

					String ndir = this.dir + File.separator + strFilePath;
					br = this.staticFile(contentHtml, ndir, strFileName);

					// br = fileManageServiceImpl.UpdateFile(strFilePath,
					// strFileName, contentHtml.getBytes());
					String staticPath = strFilePath + "/" + strFileName;
					// 更新PS_TZ_LM_NR_GL_T 栏目文章 对应表
					if (br) {
						sql = "UPDATE PS_TZ_LM_NR_GL_T SET TZ_ART_CONENT_SCR=?,TZ_ART_HTML=?,TZ_STATIC_ART_URL=? WHERE TZ_SITE_ID=? AND TZ_COLU_ID=? AND TZ_ART_ID=?";
						jdbcTemplate.update(sql,
								new Object[] { contentHtml, contentHtml, staticPath, siteId, coluId, artId });
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return br;
	}

	/**
	 * 静态化文章
	 * 
	 * @param siteId
	 * @param strBasePath
	 * @param strColuPath
	 * @param coluId
	 * @param artId
	 * @return
	 */
	private boolean content(String siteId, String strBasePath, String strColuPath, String coluId, String artId) {
		boolean br = false;
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");

			String sql = "SELECT ifnull(TZ_STATIC_AOTO_NAME,\"\") TZ_STATIC_AOTO_NAME,ifnull(TZ_STATIC_NAME,\"\") TZ_STATIC_NAME FROM PS_TZ_LM_NR_GL_T WHERE TZ_SITE_ID = ? AND TZ_COLU_ID = ? AND TZ_ART_ID = ? LIMIT 1";
			Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { siteId, coluId, artId });

			// 自动命名文章名
			String strAutoStaticName = map.get("TZ_STATIC_AOTO_NAME").toString();
			// 手工命名的文章名
			String strOriginStaticName = map.get("TZ_STATIC_NAME").toString();
			// 静态化文件名
			String strFileName = "";
			// 静态化路径
			String strFilePath = "";
			if (strBasePath != null && !"".equals(strBasePath)) {
				if (!strBasePath.startsWith("/")) {
					strBasePath = "/" + strBasePath;
				}
				if (strBasePath.endsWith("/")) {
					strBasePath = strBasePath.substring(0, strBasePath.length() - 1);
				}
			}
			if (strColuPath != null && !"".equals(strColuPath)) {
				if (!strColuPath.startsWith("/")) {
					strColuPath = "/" + strColuPath;
				}
				if (strColuPath.endsWith("/")) {
					strColuPath = strColuPath.substring(0, strColuPath.length() - 1);
				}
			}
			strFilePath = strBasePath + strColuPath;

			// 静态化
			/// System.out.println("strFilePath:" + strFilePath);
			System.out.println("strOriginStaticName:" + strOriginStaticName);
			System.out.println("strAutoStaticName:" + strAutoStaticName);

			if (strOriginStaticName != null && !strOriginStaticName.equals("")) {
				strFileName = strOriginStaticName;
			} else {
				strFileName = strAutoStaticName;
			}

			if (strFileName != null && !strFileName.equals("")) {
				System.out.println("strFileName:" + strFileName);
				if (!strFileName.toLowerCase().endsWith(".html")) {
					strFileName = strFileName + ".html";
				}

				System.out.println("strFileName:" + strFileName);
				// System.out.println("strFileName:" + strFileName);

				ArtContentHtml artContentHtml = (ArtContentHtml) getSpringBeanUtil.getAutowiredSpringBean("ArtContent");
				// 解析的模板内容;
				String contentHtml = artContentHtml.getContentHtml(siteId, coluId, artId, contentPath);
				if (contentHtml != null && !contentHtml.equals("")) {

					// 更新文件
					String ndir = this.dir + File.separator + strFilePath;
					br = this.staticFile(contentHtml, ndir, strFileName);

					// br = fileManageServiceImpl.UpdateFile(strFilePath,
					// strFileName, contentHtml.getBytes());
					String staticPath = strFilePath + "/" + strFileName;
					// 更新PS_TZ_LM_NR_GL_T 栏目文章对应表
					if (br) {
						sql = "UPDATE PS_TZ_LM_NR_GL_T SET TZ_ART_CONENT_SCR=?,TZ_ART_HTML=?,TZ_STATIC_ART_URL=? WHERE TZ_SITE_ID=? AND TZ_COLU_ID=? AND TZ_ART_ID=?";
						jdbcTemplate.update(sql,
								new Object[] { contentHtml, contentHtml, staticPath, siteId, coluId, artId });
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return br;
	}

	private boolean staticFile(String strReleasContent, String dir, String fileName) {
		try {
			// System.out.println(dir);
			File fileDir = new File(dir);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}

			String filePath = "";
			if ((dir.lastIndexOf(File.separator) + 1) != dir.length()) {
				filePath = dir + File.separator + fileName;
			} else {
				filePath = dir + fileName;
			}

			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(strReleasContent);
			bw.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
