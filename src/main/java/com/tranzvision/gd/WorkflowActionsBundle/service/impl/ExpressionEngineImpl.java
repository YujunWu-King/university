package com.tranzvision.gd.WorkflowActionsBundle.service.impl;

import com.tranzvision.gd.util.sql.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName: ExpressionEngineImpl
 * 
 * @author caoy
 * @version 1.0
 * @Create Time: 2019年1月14日 上午11:08:00
 * @Description: 工作流模板 路由设置 条件表达式引擎
 */
@Service("com.tranzvision.gd.WorkflowActionsBundle.service.impl.ExpressionEngineImpl")
public class ExpressionEngineImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private ExpressionRulesImpl expressionRulesImpl;

	/**
	 * 
	 * @Description: 工作流路由 路由条件 条件表达式配置正确性检查功能
	 * @Create Time: 2019年1月14日 上午11:34:52
	 * @author caoy
	 * @param expressList
	 *            工作流路由 路由条件 条件表达式 数据列表
	 * @return
	 */
	public ResultReturn checkSetting(List<Map<String, Object>> expressList) {
		ResultReturn rr = new ResultReturn();
		rr.setCode(ResultReturn.TRUE_CODE);

		StringBuffer brackets = new StringBuffer();
		String left_paren = "";
		String right_paren = "";
		Map<String, Object> map = null;
		for (int i = 0; i < expressList.size(); i++) {
			map = expressList.get(i);
			rr = expressionRulesImpl.check(map, i);
			// 如果某条有错误，就跳出校验
			if (!rr.getCode().equals(ResultReturn.TRUE_CODE)) {
				break;
			}
			// 左符号
			left_paren = map.get("tzms_left_paren") != null ? map.get("tzms_left_paren").toString() : "";
			// 右符号
			right_paren = map.get("tzms_right_paren") != null ? map.get("tzms_right_paren").toString() : "";

			if (!left_paren.equals("")) {
				// 1-( 2-(( 3-(((
				switch (left_paren) {
				case "1":
					brackets.append("(");
					break;
				case "2":
					brackets.append("((");
					break;
				case "3":
					brackets.append("(((");
					break;
				}
			}

			if (!right_paren.equals("")) {
				// 1-) 2-)) 3-)))
				switch (right_paren) {
				case "1":
					brackets.append(")");
					break;
				case "2":
					brackets.append("))");
					break;
				case "3":
					brackets.append(")))");
					break;
				}
			}

		}

		// 校验左右括号是否匹配
		boolean checkBrackets = expressionRulesImpl.checkBrackets(brackets.toString());
		if (!checkBrackets) {
			rr.setCode("-1001");
			rr.setMsg("左右括号必须匹配");
			return rr;
		}
		return rr;
	}

	/**
	 * 
	 * @Description:条件表达式解析引擎（Java）
	 * @Create Time: 2019年1月14日 下午3:26:03
	 * @author caoy
	 * @param atcID
	 *            业务流程动作ID
	 * @param tzms_wflinsid
	 *            流程实体ID
	 * @param tzms_stpinsid
	 *            步骤实体ID
	 * @return
	 */
	public boolean expressionEngine(String atcID, String tzms_wflinsid,String tzms_stpinsid) {
		// step 1 按条件取出所有条件表达式

		List<Map<String, Object>> expressList = expressionRulesImpl.getExpressList(atcID);
		// step 2 取出所属业务流模板 关联业务实体
		String entityName = expressionRulesImpl.getEntityName(atcID);

		String wflrecordId = sqlQuery.queryForObject(
				"SELECT tzms_wflrecord_uniqueid FROM tzms_wflins_tbl WHERE tzms_wflinsid=?",
				new Object[] { tzms_wflinsid }, "String");

		StringBuffer sb = new StringBuffer();

		// step 生成条件表达式 字符串
		Map<String, Object> map = null;
		for (int i = 0; i < expressList.size(); i++) {
			map = expressList.get(i);
			sb.append(expressionRulesImpl.getCheckSql(atcID, map, entityName, wflrecordId, tzms_wflinsid,tzms_stpinsid));
		}

		System.out.println("条件表达式解析引擎:" + sb.toString());

		// step 拼装SQL 去数据库验证表达式（比逆波兰简单多了）
		String sql = "SELECT COUNT(*) FROM test WHERE " + sb.toString();

		int r = sqlQuery.queryForObject(sql, "Integer");

		if (r < 1) {
			return false;
		} else {
			return true;
		}
	}

}
