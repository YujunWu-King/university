package com.tranzvision.gd.util.cms.page;


/**
 * SQL分页查询
 * 
 * @author WRL
 *
 */
public class Finder {

	protected Finder() {
		sqlBuilder = new StringBuilder();
	}

	protected Finder(String hql) {
		sqlBuilder = new StringBuilder(hql);
	}

	public static Finder create() {
		return new Finder();
	}

	public static Finder create(String hql) {
		return new Finder(hql);
	}

	public Finder append(String hql) {
		sqlBuilder.append(hql);
		return this;
	}

	/**
	 * 获得原始hql语句
	 * 
	 * @return
	 */
	public String getOrigHql() {
		return sqlBuilder.toString();
	}

	/**
	 * 获得查询数据库记录数的hql语句。
	 * 
	 * @return
	 */
	public String getRowCountHql() {
		String hql = sqlBuilder.toString();

		int fromIndex = hql.toLowerCase().indexOf(FROM);
		String projectionHql = hql.substring(0, fromIndex);

		hql = hql.substring(fromIndex);
		String rowCountHql = hql.replace(HQL_FETCH, "");

		int index = rowCountHql.indexOf(ORDER_BY);
		if (index > 0) {
			rowCountHql = rowCountHql.substring(0, index);
		}
		return wrapProjection(projectionHql) + rowCountHql;
	}

	public int getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}


	private String wrapProjection(String projection) {
		if (projection.indexOf("select") == -1) {
			return ROW_COUNT;
		} else {
			return "select count(* ) ";
		}
	}

	private StringBuilder sqlBuilder;

	private int firstResult = 0;

	private int maxResults = 0;

	public static final String ROW_COUNT = "select count(*) ";
	public static final String FROM = " from ";
	public static final String DISTINCT = "distinct";
	public static final String HQL_FETCH = "fetch";
	public static final String ORDER_BY = "order";

	public static void main(String[] args) {
		 String sql = "SELECT A.TZ_ART_ID,A.TZ_ART_TITLE,A.TZ_ART_TITLE_STYLE,"
				+ " A.TZ_ART_NAME,A.TZ_ART_TYPE1,A.TZ_ART_CONENT, A.TZ_OUT_ART_URL,A.TZ_ATTACHSYSFILENA,"
				+ " A.TZ_IMAGE_TITLE,A.TZ_IMAGE_DESC, A.TZ_ATTACHSYSFILENA,B.TZ_SITE_ID, B.TZ_COLU_ID,"
				+ " E.TZ_COLU_NAME,B.TZ_ART_NEWS_DT,B.TZ_ART_PUB_STATE,"
				+ " B.TZ_ART_URL,B.TZ_STATIC_ART_URL,B.TZ_ART_SEQ,"
				+ " B.TZ_MAX_ZD_SEQ,B.TZ_FBZ,B.TZ_BLT_DEPT,B.TZ_LASTMANT_OPRID, B.TZ_LASTMANT_DTTM "
				+ " FROM PS_TZ_ART_REC_TBL A,PS_TZ_LM_NR_GL_T B, PS_TZ_SITEI_COLU_T E"
				+ " WHERE A.TZ_ART_ID = B.TZ_ART_ID  AND B.TZ_SITE_ID = E.TZ_SITEI_ID AND B.TZ_ART_PUB_STATE='Y'"
				+ " AND B.TZ_COLU_ID = E.TZ_COLU_ID  AND B.TZ_COLU_ID in (?)  order by B.TZ_ART_NEWS_DT desc "
				+ " LIMIT ?,?";
		Finder find = Finder
				.create(sql);
		System.out.println(find.getRowCountHql());
		System.out.println(find.getOrigHql());
	}

}
