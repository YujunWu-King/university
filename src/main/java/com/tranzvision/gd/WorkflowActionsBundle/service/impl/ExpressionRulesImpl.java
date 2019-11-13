package com.tranzvision.gd.WorkflowActionsBundle.service.impl;

import com.tranzvision.gd.util.dynamicsBase.AnalysisDynaSysVar;
import com.tranzvision.gd.util.sql.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName: ExpressionRulesImpl
 * @author caoy
 * @version 1.0
 * @Create Time: 2019年1月14日 下午2:08:56
 * @Description: 工作流模板 路由设置 条件表达式规则校验
 */
@Service
public class ExpressionRulesImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private WorkFlowPublicImpl workFlowPublicImpl;

	/**
	 * 
	 * @Description:
	 * @Create Time: 2019年1月14日 下午2:12:01
	 * @author caoy
	 * @param map
	 *            工作流路由 路由条件 条件表达式 数据
	 * @param i
	 *            第几条数据
	 * @return
	 */
	public ResultReturn check(Map<String, Object> map, int i) {
		i = i + 1;
		ResultReturn rr = new ResultReturn();
		rr.setCode(ResultReturn.TRUE_CODE);
		ConditionBean cb = ConditionBean.chageMap(map);
		String first = "第" + i + "条规则:";
		// 规则1：第一条规则不需要有 and或or
		if (i == 1 && !cb.getAnd_or().equals("")) {
			rr.setCode("-1000");
			rr.setMsg(first + "不需要有 and或or");
			return rr;
		}

		if (i > 1) {
			// 1-and 2-or
			switch (cb.getAnd_or()) {
			case "1":
				break;
			case "2":
				break;
			default:
				rr.setCode("-1000");
				rr.setMsg(first + " and或or 为空");
				return rr;
			}
		}

		// 1-( 2-(( 3-(((
		switch (cb.getLeft_paren()) {
		case "1":
			break;
		case "2":
			break;
		case "3":
			break;
		// default:
		// rr.setCode("-1001");
		// rr.setMsg(first + " 左括号不正确");
		// return rr;
		}

		switch (cb.getRight_paren()) {
		case "1":
			break;
		case "2":
			break;
		case "3":
			break;
		// default:
		// rr.setCode("-1001");
		// rr.setMsg(first + " 右括号不正确");
		// return rr;
		}

		// 规则3：操作符一定要有
		if (cb.getOperator_flg().equals("")) {
			rr.setCode("-1002");
			rr.setMsg(first + "操作符必填");
			return rr;
		}

		// 1-= 2-<> 3-> 4->= 5-< 6-<=
		switch (cb.getOperator_flg()) {
		case "1":
			break;
		case "2":
			break;
		case "3":
			break;
		case "4":
			break;
		case "5":
			break;
		case "6":
			break;
		default:
			rr.setCode("-1002");
			rr.setMsg(first + " 操作符不正确");
			return rr;
		}

		// 规则4：取值类型一定要有
		if (cb.getValue_type().equals("")) {
			rr.setCode("-1003");
			rr.setMsg(first + "取值类型必填");
			return rr;
		}

		// 1-常量 2-系统变量 3-环境变量
		// 规则5：取值类型要和后面的字段匹配
		switch (cb.getValue_type()) {
		case "1":
			if (cb.getCond_value().equals("")) {
				rr.setCode("-1004");
				rr.setMsg(first + "取值类型为常量，常量不能为空");
				return rr;
			}
			break;
		case "2":
			if (cb.getSvar_uniqueid().equals("")) {
				rr.setCode("-1004");
				rr.setMsg(first + "取值类型为系统变量，系统变量不能为空");
				return rr;
			}
			break;
		case "3":
			if (cb.getEnvironment_type().equals("")) {
				rr.setCode("-1004");
				rr.setMsg(first + "取值类型为环境变量，环境变量不能为空");
				return rr;
			}
			break;
		default:
			rr.setCode("-1003");
			rr.setMsg(first + "取值类型不正确");
			return rr;
		}

		// 规则6：页面字段名称一定要有
		if (cb.getField_name().equals("")) {
			rr.setCode("-1005");
			rr.setMsg(first + "页面字段名称必填");
			return rr;
		}
		return rr;
	}

	/**
	 * 
	 * @Description:校验字符串括号是否闭合
	 * @Create Time: 2019年1月14日 下午3:36:03
	 * @author caoy
	 * @param str
	 * @return true 校验通过 false校验不通过
	 */
	public boolean checkBrackets(String str) {
		int xi = 0;
		for (int i = 0; i < str.length(); i++) {
			char s = str.charAt(i);
			if (s == '(') {
				xi += 1;
			}
			if (s == ')') {
				xi -= 1;
			}
			if (xi < 0) {
				break;
			}
		}
		if (xi != 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 
	 * @Description:根据业务流程动作ID 取出所有表达式
	 * @Create Time: 2019年1月14日 下午6:09:07
	 * @author caoy
	 * @param atcID
	 *            业务流程动作ID
	 * @return
	 */
	public List<Map<String, Object>> getExpressList(String atcID) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT tzms_and_or,tzms_left_paren,tzms_field_name,tzms_operator_flg,");
		sb.append("tzms_value_type,tzms_cond_value,tzms_svar_uniqueid,");
		sb.append("tzms_environment_type,tzms_right_paren FROM tzms_condition_tBase ");
		sb.append("WHERE cast(tzms_actcls_uniqueid as varchar(36))=? ORDER BY tzms_condition_xh");

		List<Map<String, Object>> expressList = sqlQuery.queryForList(sb.toString(), new Object[] { atcID });
		return expressList;
	}

	/**
	 * 
	 * @Description: 根据业务流程动作ID 获得业务流程关的联业务实体
	 * @Create Time: 2019年1月14日 下午6:10:47
	 * @author caoy
	 * @param atcID
	 *            业务流程动作ID
	 * @return
	 */
	public String getEntityName(String atcID) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT C.tzms_entity_name FROM tzms_wf_actcls_tBase A,tzms_wfcldn_tBase C ");
		sb.append("WHERE A.tzms_wfl_uniqueid=C.tzms_wfcldn_tid AND ");
		sb.append("cast(A.tzms_wf_actcls_tid as varchar(36))=? ");

		String entityName = sqlQuery.queryForObject(sb.toString(), new Object[] { atcID }, "String");
		return entityName;
	}

	/**
	 * 
	 * @Description:读取实体的某一个字段
	 * @Create Time: 2019年1月14日 下午6:48:49
	 * @author feifei
	 * @param entityName
	 *            实体名
	 * @param wflrecordId
	 *            流程实体业务数据ID
	 * @param columName
	 *            需要取出的实体的字段
	 * @return
	 */
	public String getEntityValue(String entityName, String wflrecordId, String columName) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		sb.append(columName);
		sb.append(" FROM ");
		sb.append(entityName);
		sb.append("base WHERE ");
		sb.append("cast(" + entityName + "id as varchar(36))=?");
		// sb.append("id=?");
		String entityValue = sqlQuery.queryForObject(sb.toString(), new Object[] { wflrecordId }, "String");

		return entityValue;
	}

	/**
	 * 
	 * @Description:读取实体的某一个字段类型
	 * @Create Time: 2019年4月17日 下午6:49:49
	 * @author feifei
	 * @param entityName
	 * @param columName
	 * @return
	 */
	public String getEntityValueType(String entityName, String columName) {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.name as typename ");
		sb.append("from sys.columns a inner join sys.tables b on b.object_id=a.object_id  ");
		sb.append("inner join sys.types c on c.system_type_id=a.system_type_id  ");
		sb.append("where b.name='" + entityName + "base' ");
		sb.append("and a.name=? ");
		// sb.append("id=?");
		String entityValue = sqlQuery.queryForObject(sb.toString(), new Object[] { columName }, "String");

		return entityValue;
	}

	/**
	 * 
	 * @Description:把某一表达式拼装成SQL语句
	 * @Create Time: 2019年1月14日 下午7:26:07
	 * @author caoy
	 * @param atcID
	 *            业务流程动作ID
	 * @param map
	 *            工作流路由 路由条件 条件表达式 数据
	 * @param entityName
	 *            业务流关联实体名
	 * @param wflrecordId
	 *            业务流实例数据ID
	 * @param tzms_wflinsid
	 *            业务流实例ID
	 * @param tzms_stpinsid
	 *            步骤实体ID
	 * @return
	 */
	public String getCheckSql(String atcID, Map<String, Object> map, String entityName, String wflrecordId,
			String tzms_wflinsid, String tzms_stpinsid) {
		StringBuffer sb = new StringBuffer();
		ConditionBean cb = ConditionBean.chageMap(map);

		// 1-and 2-or
		switch (cb.getAnd_or()) {
		case "1":
			sb.append(" AND ");
			break;
		case "2":
			sb.append(" OR ");
			break;
		default:
			break;
		}

		// 1-( 2-(( 3-(((
		switch (cb.getLeft_paren()) {
		case "1":
			sb.append(" ( ");
			break;
		case "2":
			sb.append(" (( ");
			break;
		case "3":
			sb.append(" ((( ");
			break;
		default:
			break;
		}

		String entityValue = getEntityValue(entityName, wflrecordId, cb.getField_name());

		if (entityValue == null) {
			entityValue = "";
		}

		// modity by caoy 需要根据字段类型来判断是否加'
		String entityValueType = getEntityValueType(entityName, cb.getField_name());
		entityValueType = entityValueType.toUpperCase();
		if (entityValueType.equals("INT") || entityValueType.equals("SMALLINT") || entityValueType.equals("TINYINT")
				|| entityValueType.equals("BIGINT") || entityValueType.equals("FLOAT") || entityValueType.equals("REAL")
				|| entityValueType.equals("DECIMAL") || entityValueType.equals("NUMERIC")
				|| entityValueType.equals("INTEGER")) {
			
			if("".equals(entityValue)) {
				sb.append("null");
			}else {
				sb.append(entityValue);
			}
		} else {
			sb.append("'");
			sb.append(entityValue);
			sb.append("'");
		}

		// 1-= 2-<> 3-> 4->= 5-< 6-<=
		switch (cb.getOperator_flg()) {
		case "1":
			sb.append(" = ");
			break;
		case "2":
			sb.append(" <> ");
			break;
		case "3":
			sb.append(" > ");
			break;
		case "4":
			sb.append(" >= ");
			break;
		case "5":
			sb.append(" < ");
			break;
		case "6":
			sb.append(" <= ");
			break;
		default:
			break;
		}

		// String sysValue = "";

		// 1-常量 2-系统变量 3-环境变量
		// 规则5：取值类型要和后面的字段匹配
		switch (cb.getValue_type()) {
		case "1":
			if (entityValueType.equals("INT") || entityValueType.equals("SMALLINT") || entityValueType.equals("TINYINT")
					|| entityValueType.equals("BIGINT") || entityValueType.equals("FLOAT")
					|| entityValueType.equals("REAL") || entityValueType.equals("DECIMAL")
					|| entityValueType.equals("NUMERIC") || entityValueType.equals("INTEGER")) {
				sb.append(cb.getCond_value());
			} else {
				sb.append("'");
				sb.append(cb.getCond_value());
				sb.append("'");
			}
			break;
		case "2":
			if (entityValueType.equals("INT") || entityValueType.equals("SMALLINT") || entityValueType.equals("TINYINT")
					|| entityValueType.equals("BIGINT") || entityValueType.equals("FLOAT")
					|| entityValueType.equals("REAL") || entityValueType.equals("DECIMAL")
					|| entityValueType.equals("NUMERIC") || entityValueType.equals("INTEGER")) {
				sb.append(getSysValue(cb.getSvar_uniqueid(), tzms_wflinsid, atcID, tzms_stpinsid));
			} else {
				sb.append("'");
				sb.append(getSysValue(cb.getSvar_uniqueid(), tzms_wflinsid, atcID, tzms_stpinsid));
				sb.append("'");
			}
			break;
		case "3":
			if (entityValueType.equals("INT") || entityValueType.equals("SMALLINT") || entityValueType.equals("TINYINT")
					|| entityValueType.equals("BIGINT") || entityValueType.equals("FLOAT")
					|| entityValueType.equals("REAL") || entityValueType.equals("DECIMAL")
					|| entityValueType.equals("NUMERIC") || entityValueType.equals("INTEGER")) {
				sb.append(getFinalValue(cb.getEnvironment_type(), tzms_wflinsid));
			} else {
				sb.append("'");
				sb.append(getFinalValue(cb.getEnvironment_type(), tzms_wflinsid));
				sb.append("'");
			}
			break;
		default:
			break;
		}

		switch (cb.getRight_paren()) {
		case "1":
			sb.append(" ) ");
			break;
		case "2":
			sb.append(" )) ");
			break;
		case "3":
			sb.append(" ))) ");
			break;
		default:
			break;
		}

		return sb.toString();
	}

	/**
	 * 根据系统变量名获取系统变量
	 * 
	 * @Description:
	 * @Create Time: 2019年1月14日 下午6:58:48
	 * @author caoy
	 * @param Svar_uniqueid
	 *            系统变量ID
	 * @param tzms_wflinsid
	 *            流程实体ID
	 * @param atcID
	 *            业务流程动作ID
	 * @param tzms_stpinsid
	 *            步骤实体ID
	 * @return
	 */
	public String getSysValue(String Svar_uniqueid, String tzms_wflinsid, String atcID, String tzms_stpinsid) {
		String userid = workFlowPublicImpl.getDynamicsLoginer();
		String[] sysVarParam = { tzms_wflinsid, atcID, userid, tzms_stpinsid };
		AnalysisDynaSysVar analysisSysVar = new AnalysisDynaSysVar();
		analysisSysVar.setM_SysVarID(Svar_uniqueid);
		analysisSysVar.setM_SysVarParam(sysVarParam);
		Object obj = analysisSysVar.GetVarValue();
		if (obj == null) {
			return "";
		} else {
			return obj.toString();
		}
	}

	/**
	 * 
	 * @Description: 得到环境变量
	 * @Create Time: 2019年1月14日 下午7:00:51
	 * @author caoy
	 * @param Environment_type
	 *            1-当前登录人 2-流程发起人
	 * @return
	 */
	public String getFinalValue(String Environment_type, String tzms_wflinsid) {
		String finalValu = "";
		switch (Environment_type) {
		case "1":
			finalValu = workFlowPublicImpl.getDynamicsLoginer();
			break;
		case "2":
			finalValu = sqlQuery.queryForObject("SELECT tzms_startproid FROM tzms_wflins_tbl WHERE tzms_wflinsid=?",
					new Object[] { tzms_wflinsid }, "String");
		default:
			break;
		}
		return finalValu;
	}

}
