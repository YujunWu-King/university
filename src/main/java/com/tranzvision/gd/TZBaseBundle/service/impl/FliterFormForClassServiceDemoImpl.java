package com.tranzvision.gd.TZBaseBundle.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * ClassName: FliterFormForClassServiceDemoImpl
 * 
 * @author caoy
 * @version 1.0 Create Time: 2019年11月22日 上午10:51:04 Description: 采用类配置的可配置查询的
 *          DEMO
 */
@Service("com.tranzvision.gd.TZBaseBundle.service.impl.FliterFormForClassServiceDemoImpl")
public class FliterFormForClassServiceDemoImpl extends FliterFormForClassServiceImpl {

	@Autowired
	private SqlQuery jdbcTemplate;

	/**
	 * 
	 * Description:实现类配置可配置查询的 具体的方法 Create Time: 2019年11月22日 上午11:14:27
	 * 
	 * @author caoy
	 * @param resultFldArray
	 * @param orderByArr
	 * @param conditionList
	 * @param numLimit
	 * @param numStart
	 * @param maxNum
	 * @param errorMsg
	 * @return
	 */
	public String[] searchFilter(String resultFld, String orderBy, List<ConditionBean> conditionList, int numLimit,
			int numStart, int maxNum, String[] errorMsg) {

		// 返回值;
		String[] strRet = null;

		// 结果值;
		ArrayList<String[]> list = new ArrayList<String[]>();

		int numTotal = 0;


		String TotalSQL = "SELECT count(A.TZ_APP_INS_ID) FROM PS_TZ_FORM_WRK_T A, PS_TZ_APP_INS_T B, PS_TZ_AQ_YHXX_TBL C, PS_TZ_CLASS_INF_T D, PS_TZ_CLS_BATCH_T E WHERE A.TZ_APP_INS_ID = B.TZ_APP_INS_ID AND A.OPRID = C.OPRID AND A.TZ_CLASS_ID = D.TZ_CLASS_ID AND A.TZ_BATCH_ID = E.TZ_BATCH_ID AND D.TZ_CLASS_ID = E.TZ_CLASS_ID  ";
		
		
		//写SQL的的时候要注意，我们取出来的都是String 类型，所以需要对时间/数字类型进行转换
		//如何转换 看 FliterForm 561行开始
		String SQL = "SELECT D.TZ_CLASS_NAME AS TZ_CLASS_NAME,E.TZ_BATCH_NAME AS TZ_BATCH_NAME, CONCAT(ifnull(A.TZ_APP_INS_ID,0),'') AS TZ_APP_INS_ID, B.TZ_APP_FORM_STA AS TZ_APP_FORM_STA,ifnull(date_format(B.TZ_APP_SUB_DTTM,'%Y-%m-%d %H:%i'),'')  AS TZ_APP_SUB_DTTM, CONCAT(ifnull(B.TZ_FILL_PROPORTION,0),'') AS TZ_FILL_PROPORTION, C.OPRID AS OPRID, C.TZ_REALNAME AS TZ_REALNAME, C.TZ_EMAIL AS TZ_EMAIL, C.TZ_MOBILE AS TZ_MOBILE FROM PS_TZ_FORM_WRK_T A, PS_TZ_APP_INS_T B, PS_TZ_AQ_YHXX_TBL C, PS_TZ_CLASS_INF_T D, PS_TZ_CLS_BATCH_T E WHERE A.TZ_APP_INS_ID = B.TZ_APP_INS_ID AND A.OPRID = C.OPRID AND A.TZ_CLASS_ID = D.TZ_CLASS_ID AND A.TZ_BATCH_ID = E.TZ_BATCH_ID AND D.TZ_CLASS_ID = E.TZ_CLASS_ID ";

		String operator = "";
		String value = "";

		String fild = "";
		String sql = "";

		// conditionList 里面的查询SQL 并不一定能实际使用，用的时候需要做某些处理，比如加上别名
		// operator 是 7,8,9,10,11,12,13 不是实际的运算符好，而是包含啊开始于，结束语，在什么之内，这些
		if (conditionList != null && conditionList.size() > 0) {
			for (ConditionBean cb : conditionList) {
				sql = cb.getSql();
				fild = cb.getFild();
				if ("TZ_CLASS_ID".equals(fild) || "TZ_BATCH_ID".equals(fild)) {
					TotalSQL = TotalSQL + " AND A." + sql;
					SQL = SQL + " AND A." + sql;
				}
				if ("TZ_JG_ID".equals(fild)) {
					TotalSQL = TotalSQL + " AND C." + sql;
					SQL = SQL + " AND C." + sql;
				}
			}
		}

		

		// 总数;
		if (maxNum > 0 && numTotal > maxNum) {
			numTotal = maxNum;
		}

		if (numTotal == 0 || numStart >= numTotal) {
			numStart = 0;
		}

		// 查看开始行数+限制的行数是否大于最大显示行数;
		if (maxNum > 0 && (numStart + numLimit) > maxNum) {
			numLimit = maxNum - numStart;
		}

		// 查询结果;
		if (numLimit == 0 && numStart == 0) {
			SQL = resultFld +" ( " +SQL +" ) T " + orderBy;
		} else {
			SQL = resultFld +" ( " +SQL +" ) T " + orderBy + " LIMIT ?,?";
		}
		
		System.out.println("TotalSQL:" + TotalSQL);
		System.out.println("SQL:" + SQL);
		strRet = new String[] { TotalSQL, SQL };

		return strRet;
	}

}
