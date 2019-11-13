package com.tranzvision.gd.WorkflowActionsBundle.service.impl;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.workflow.engine.TzWorkflow;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName: DynamicsApplicationJSImpl
 * @author caoy
 * @version 1.0
 * @Create Time: 2019年1月16日 下午4:40:32
 * @Description: 用于流程实例页面展现按钮组
 */
@Service("com.tranzvision.gd.WorkflowActionsBundle.service.impl.DynamicsApplicationJSImpl")
public class DynamicsApplicationJSImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private WorkFlowPublicImpl workFlowPublic;

	/**
	 * 
	 * @Description: 返回Dynamics页面的一些特效，包括按钮情况，资源情况，是否只读
	 * @Create Time: 2019年1月16日 下午4:46:09
	 * @author caoy
	 * @param oprType
	 * @param strParams
	 *            需要传入 流程实例ID 流程步骤实例ID
	 * @param errorMsg
	 * @return JSON字符串 里面 stepFlag 1001:不存在实例 stepFlag 1002:知会实例 stepFlag
	 *         1003:步骤实例终止/结束/撤销 1004:其他 1005:dynamics查看
	 */
	public String tzOther(String oprType, String strParams, String[] errorMsg) {

		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		String etc = ""; // 流程关联的实体ID
		String tzms_wflinsid = ""; // 流程实例ID
		String tzms_stpinsid = ""; // 流程步骤实例ID
		String isCreate = "";
		// 按钮 保存 提交 同意 加签 擦看历史处理记录 查看流程记录 退回重填 退回某部重填 转发 知会 拒绝 撤回 撤销 页面有的按钮
		// 设置的 抄送(知会) 转发 退回重填 退回某步 加签 撤回 撤销 提交 审批同意 审批拒绝 是否只读整个页面 显示处理历史记录
		// 显示任务处理区
		Map<String, Object> mapJson = null;
		Map<String, Object> map = null;
		// 需要显示 是否 页面只读，需要显示 按钮，需要显示资源情况
		switch (oprType) {
		case "DynamicsButtom": // 显示按钮
			System.out.println("DynamicsApplicationJSImpl=========================DynamicsButtom");
			jacksonUtil.json2Map(strParams);
			System.out.println(strParams);
			if (jacksonUtil.containsKey("etc")) {
				etc = jacksonUtil.getString("etc");
			}
			if (StringUtils.isBlank(etc)) {
				etc = request.getParameter("etc");
			}
			if (jacksonUtil.containsKey("isCreate")) {
				isCreate = jacksonUtil.getString("isCreate");
			}
			if (StringUtils.isBlank(isCreate)) {
				isCreate = request.getParameter("isCreate");
			}

			if (jacksonUtil.containsKey("tzms_wflinsid")) {
				tzms_wflinsid = jacksonUtil.getString("tzms_wflinsid");
			}
			if (StringUtils.isBlank(tzms_wflinsid)) {
				tzms_wflinsid = request.getParameter("tzms_wflinsid");
			}
			if (jacksonUtil.containsKey("tzms_stpinsid")) {
				tzms_stpinsid = jacksonUtil.getString("tzms_stpinsid");
			}
			if (StringUtils.isBlank(tzms_stpinsid)) {
				tzms_stpinsid = request.getParameter("tzms_stpinsid");
			}
			// System.out.println("DynamicsApplicationJSImpl========================="
			// + tzms_wflstp_tid);
			if (tzms_wflinsid == null) {
				tzms_wflinsid = "";
			}
			if (tzms_stpinsid == null) {
				tzms_stpinsid = "";
			}
			if (isCreate == null) {
				isCreate = "";
			}

			System.out.println("DynamicsApplicationJSImpl=========================etc:" + etc);
			System.out.println("DynamicsApplicationJSImpl=========================tzms_wflinsid:" + tzms_wflinsid);
			System.out.println("DynamicsApplicationJSImpl=========================tzms_stpinsid:" + tzms_stpinsid);
			System.out.println("DynamicsApplicationJSImpl=========================isCreate:" + isCreate);

			// 如果没有isCreate 参数说明是 dynamcis系统查看不需要显示按钮，只要只读整个页面
			if ("".equals(isCreate)) {
				mapJson = new HashMap<String, Object>();
				mapJson.put("stepFlag", "1005");
				strRet = jacksonUtil.Map2json(mapJson);
				return strRet;
			}
			// isCreate 是True 说明是 新建立
			if ("True".equalsIgnoreCase(isCreate)) {
				String sql = "SELECT TOP 1 A.tzms_wfcldn_tid FROM tzms_wfcldn_tBase A,Entity B WHERE A.tzms_entity_name=B.Name AND B.ObjectTypeCode=?";
				String tzms_wfcldn_tid = sqlQuery.queryForObject(sql, new Object[] { etc }, "String");

				StringBuffer sb = new StringBuffer();
				sb.append("SELECT tzms_close_flg,tzms_manu_close_flg,TZms_DPYPRCREC1,TZms_DPYPRCFLG1,");
				sb.append("tzms_show_process_flg,tzms_show_gowfl_flg,tzms_dpycpyflg,");
				sb.append("tzms_dpytsfflg,tzms_dpybak_reflg,tzms_dpybakflg,tzms_dpyhqrflg,");
				sb.append("tzms_dpycalflg,tzms_dpyrevokeflg,tzms_dpysubmitflg,tzms_dpyapproveflg,");
				sb.append("tzms_dpybatrefuseflg,tzms_ifdisdisplaypage,tzms_wfcldn_uniqueid,tzms_buttonname");
				sb.append(" FROM tzms_wflstp_tBase WHERE tzms_stptype=1 AND tzms_wfcldn_uniqueid=?");

				// 增加对于是否打印的处理 tzms_wfcldn_ts表 tzms_is_print字段
				// 1 可用 0停用
				map = sqlQuery.queryForMap(sb.toString(), new Object[] { tzms_wfcldn_tid });
				if (map != null) {
					// 允许直接关闭
					String tzms_close_flg = map.get("tzms_close_flg") != null ? map.get("tzms_close_flg").toString() : "";

					String tzms_is_print = sqlQuery.queryForObject(
							"select tzms_is_print from tzms_wfcldn_tBase where tzms_wfcldn_tid=?",
							new Object[] { tzms_wfcldn_tid }, "String");

					// 需要手动关闭
					String tzms_manu_close_flg = map.get("tzms_manu_close_flg") != null ? map.get("tzms_manu_close_flg").toString() : "";
					// 显示处理历史记录
					String TZms_DPYPRCREC1 = map.get("TZms_DPYPRCREC1") != null ? map.get("TZms_DPYPRCREC1").toString() : "";
					// 显示任务处理区
					String TZms_DPYPRCFLG1 = map.get("TZms_DPYPRCFLG1") != null ? map.get("TZms_DPYPRCFLG1").toString() : "";
					// 显示处理过程列表
					String tzms_show_process_flg = map.get("tzms_show_process_flg") != null ? map.get("tzms_show_process_flg").toString() : "";
					// 启用流程图查看列表
					String tzms_show_gowfl_flg = map.get("tzms_show_gowfl_flg") != null ? map.get("tzms_show_gowfl_flg").toString() : "";
					// 启用抄送功能
					String tzms_dpycpyflg = map.get("tzms_dpycpyflg") != null ? map.get("tzms_dpycpyflg").toString() : "";
					// 启用转发功能
					String tzms_dpytsfflg = map.get("tzms_dpytsfflg") != null ? map.get("tzms_dpytsfflg").toString() : "";
					// 启用退回重填功能
					String tzms_dpybak_reflg = map.get("tzms_dpybak_reflg") != null ? map.get("tzms_dpybak_reflg").toString() : "";
					// 启用退回某步功能
					String tzms_dpybakflg = map.get("tzms_dpybakflg") != null ? map.get("tzms_dpybakflg").toString() : "";
					// 启用加签功能
					String tzms_dpyhqrflg = map.get("tzms_dpyhqrflg") != null ? map.get("tzms_dpyhqrflg").toString() : "";
					// 启用撤回功能
					String tzms_dpycalflg = map.get("tzms_dpycalflg") != null ? map.get("tzms_dpycalflg").toString() : "";
					// 启用撤销功能
					String tzms_dpyrevokeflg = map.get("tzms_dpyrevokeflg") != null ? map.get("tzms_dpyrevokeflg").toString() : "";
					// 启用提交功能
					String tzms_dpysubmitflg = map.get("tzms_dpysubmitflg") != null ? map.get("tzms_dpysubmitflg").toString() : "";
					// 启用审批同意功能
					String tzms_dpyapproveflg = map.get("tzms_dpyapproveflg") != null ? map.get("tzms_dpyapproveflg").toString() : "";
					// 启用审批拒绝功能
					String tzms_dpybatrefuseflg = map.get("tzms_dpybatrefuseflg") != null ? map.get("tzms_dpybatrefuseflg").toString() : "";
					// 是否只读整个页面
					String tzms_ifdisdisplaypage = map.get("tzms_ifdisdisplaypage") != null ? map.get("tzms_ifdisdisplaypage").toString() : "";
					//自定义按钮名称
					String tzms_buttonname = map.get("tzms_buttonname") != null ? map.get("tzms_buttonname").toString() : "";
					if("".equals(tzms_buttonname)) tzms_buttonname = "提交";

					mapJson = new HashMap<String, Object>();
					mapJson.put("stepFlag", "1004");
					mapJson.put("tzms_save_flg", "1");
					mapJson.put("tzms_close_flg", tzms_close_flg);
					mapJson.put("tzms_manu_close_flg", tzms_manu_close_flg);
					mapJson.put("TZms_DPYPRCREC1", TZms_DPYPRCREC1);
					mapJson.put("TZms_DPYPRCFLG1", TZms_DPYPRCFLG1);
					mapJson.put("tzms_show_process_flg", tzms_show_process_flg);
					mapJson.put("tzms_show_gowfl_flg", tzms_show_gowfl_flg);
					mapJson.put("tzms_dpycpyflg", tzms_dpycpyflg);
					mapJson.put("tzms_dpytsfflg", tzms_dpytsfflg);
					mapJson.put("tzms_dpybak_reflg", tzms_dpybak_reflg);
					mapJson.put("tzms_dpybakflg", tzms_dpybakflg);
					mapJson.put("tzms_dpyhqrflg", tzms_dpyhqrflg);
					mapJson.put("tzms_dpycalflg", tzms_dpycalflg);
					mapJson.put("tzms_dpyrevokeflg", tzms_dpyrevokeflg);
					mapJson.put("tzms_dpysubmitflg", tzms_dpysubmitflg);
					mapJson.put("tzms_dpyapproveflg", tzms_dpyapproveflg);
					mapJson.put("tzms_dpybatrefuseflg", tzms_dpybatrefuseflg);
					mapJson.put("tzms_ifdisdisplaypage", tzms_ifdisdisplaypage);
					mapJson.put("tzms_is_print", tzms_is_print);
					mapJson.put("tzms_buttonname", tzms_buttonname);
					mapJson.put("tzms_stpinsstat", "3"); // 新建

					strRet = jacksonUtil.Map2json(mapJson);
					return strRet;
				} else {
					mapJson = new HashMap<String, Object>();
					mapJson.put("stepFlag", "1001");
					strRet = jacksonUtil.Map2json(mapJson);
					return strRet;
				}
			} else {
				// 这里是更新
				// 显示逻辑
				// step1 查询用户的该流程步驟实例是否存在，如果不存在不需要显示按钮,页面只读
				// step2 检查是否只会，如果只会，只需要显示 已阅 按钮，页面只读
				// 1-激活、2-撤回、3-处理中、4-结束、5-提前终止、6-未签收、7-退回、8-已签收、9-转发
				// step3 下面是步骤实例里面tzms_stpinsstat的校验
				// step4 2 5 7 除了打印 按钮，其他都不显示，页面只读
				// step5 4和 9 一样 的時候校验撤回按钮是否显示，除了打印 按钮，其他都不显示，页面只读
				// step6 6 不需要显示按钮,页面只读
				// step7 1 3 8 一樣，正常显示
				//其中 step4 ,step5 step7  显示处理历史记录 显示处理过程列表 正常显示

				map = sqlQuery.queryForMap(
						"SELECT tzms_asgmethod,tzms_stpinsstat,tzms_wflstpid FROM tzms_stpins_tbl WHERE tzms_wflinsid=? AND tzms_stpinsid=?",
						new Object[] { tzms_wflinsid, tzms_stpinsid });

				if (map == null || map.size() == 0) {
					mapJson = new HashMap<String, Object>();
					mapJson.put("stepFlag", "1001");
					strRet = jacksonUtil.Map2json(mapJson);
					return strRet;
				}
				if (map != null && map.size() > 0) {

					String tzms_asgmethod = map.get("tzms_asgmethod") != null ? map.get("tzms_asgmethod").toString() : "";
					String tzms_stpinsstat = map.get("tzms_stpinsstat") != null ? map.get("tzms_stpinsstat").toString() : "";
					
					// step 2 检查是否是只会，如果是知会，只需要显示已阅 按钮，其他按钮不用显示
					if (tzms_asgmethod != null && tzms_asgmethod.equals("1")) {
						mapJson = new HashMap<String, Object>();
						mapJson.put("stepFlag", "1002");
						
						if("4".equals(tzms_stpinsstat)) {
							mapJson.put("readed", "Y");
						}else {
							mapJson.put("readed", "N");
						}
						
						strRet = jacksonUtil.Map2json(mapJson);
						return strRet;
					}

					// 是否打印
					String tzms_is_print = sqlQuery.queryForObject(
							"select A.tzms_is_print from tzms_wfcldn_tBase A,tzms_wflins_tbl B where A.tzms_wfcldn_tid=B.tzms_wfcldn_uniqueid and B.tzms_wflinsid=?",
							new Object[] { tzms_wflinsid }, "String");
					
					// 1-激活、2-撤回、3-处理中、4-结束、5-提前终止、6-未签收、7-退回、8-已签收、9-转发
					// step6 6 不需要显示按钮,页面只读
					//管理员未签收也可以编辑
//					if (tzms_stpinsstat != null && tzms_stpinsstat.equals("6")) {
//						mapJson = new HashMap<String, Object>();
//						mapJson.put("stepFlag", "1001");
//						strRet = jacksonUtil.Map2json(mapJson);
//						return strRet;
//					}

					String tzms_wflstp_tid = map.get("tzms_wflstpid") != null ? map.get("tzms_wflstpid").toString() : "";

					StringBuffer sb = new StringBuffer();
					sb.append("SELECT tzms_stptype,tzms_close_flg,tzms_manu_close_flg,TZms_DPYPRCREC1,TZms_DPYPRCFLG1,");
					sb.append("tzms_show_process_flg,tzms_show_gowfl_flg,tzms_dpycpyflg,");
					sb.append("tzms_dpytsfflg,tzms_dpybak_reflg,tzms_dpybakflg,tzms_dpyhqrflg,");
					sb.append("tzms_dpycalflg,tzms_dpyrevokeflg,tzms_dpysubmitflg,tzms_dpyapproveflg,");
					sb.append("tzms_dpybatrefuseflg,tzms_ifdisdisplaypage,tzms_wfcldn_uniqueid,tzms_buttonname");
					sb.append(" FROM tzms_wflstp_tBase WHERE tzms_wflstp_tid=?");
					// 1 可用 0停用
					map = sqlQuery.queryForMap(sb.toString(), new Object[] { tzms_wflstp_tid });
					if (map != null) {
						// 检查是否是首步骤
						String isFast = map.get("tzms_stptype") != null ? map.get("tzms_stptype").toString() : "";
						// 允许直接关闭
						String tzms_close_flg = map.get("tzms_close_flg") != null ? map.get("tzms_close_flg").toString() : "";
						// 需要手动关闭
						String tzms_manu_close_flg = map.get("tzms_manu_close_flg") != null ? map.get("tzms_manu_close_flg").toString() : "";
						// 显示处理历史记录
						String TZms_DPYPRCREC1 = map.get("TZms_DPYPRCREC1") != null ? map.get("TZms_DPYPRCREC1").toString() : "";
						// 显示任务处理区
						String TZms_DPYPRCFLG1 = map.get("TZms_DPYPRCFLG1") != null ? map.get("TZms_DPYPRCFLG1").toString() : "";
						// 显示处理过程列表
						String tzms_show_process_flg = map.get("tzms_show_process_flg") != null ? map.get("tzms_show_process_flg").toString() : "";
						// 启用流程图查看列表
						String tzms_show_gowfl_flg = map.get("tzms_show_gowfl_flg") != null ? map.get("tzms_show_gowfl_flg").toString() : "";
						// 启用抄送功能
						String tzms_dpycpyflg = map.get("tzms_dpycpyflg") != null ? map.get("tzms_dpycpyflg").toString() : "";
						// 启用转发功能
						String tzms_dpytsfflg = map.get("tzms_dpytsfflg") != null ? map.get("tzms_dpytsfflg").toString() : "";
						// 启用退回重填功能
						String tzms_dpybak_reflg = map.get("tzms_dpybak_reflg") != null ? map.get("tzms_dpybak_reflg").toString() : "";
						// 启用退回某步功能
						String tzms_dpybakflg = map.get("tzms_dpybakflg") != null ? map.get("tzms_dpybakflg").toString() : "";
						// 启用加签功能
						String tzms_dpyhqrflg = map.get("tzms_dpyhqrflg") != null ? map.get("tzms_dpyhqrflg").toString() : "";
						// 启用撤回功能
						String tzms_dpycalflg = map.get("tzms_dpycalflg") != null ? map.get("tzms_dpycalflg").toString() : "";
						// 启用撤销功能
						String tzms_dpyrevokeflg = map.get("tzms_dpyrevokeflg") != null ? map.get("tzms_dpyrevokeflg").toString() : "";
						// 启用提交功能
						String tzms_dpysubmitflg = map.get("tzms_dpysubmitflg") != null ? map.get("tzms_dpysubmitflg").toString() : "";
						// 启用审批同意功能
						String tzms_dpyapproveflg = map.get("tzms_dpyapproveflg") != null ? map.get("tzms_dpyapproveflg").toString() : "";
						// 启用审批拒绝功能
						String tzms_dpybatrefuseflg = map.get("tzms_dpybatrefuseflg") != null ? map.get("tzms_dpybatrefuseflg").toString() : "";
						// 是否只读整个页面
						String tzms_ifdisdisplaypage = map.get("tzms_ifdisdisplaypage") != null ? map.get("tzms_ifdisdisplaypage").toString() : "";
						//自定义按钮名称
						String tzms_buttonname = map.get("tzms_buttonname") != null ? map.get("tzms_buttonname").toString() : "";
						if("".equals(tzms_buttonname)) {
							tzms_buttonname = "1".equals(isFast) ? "提交" : "同意";
						}

						mapJson = new HashMap<String, Object>();
						String no = "0";
						
						// step3 检查步骤是否已经结束，如果已经结束,提前终止，已撤销，不显示按钮

						// 1-激活、2-撤回、3-处理中、4-结束、5-提前终止、7-退回、8-已签收、9-转发
						//step4 2 5 7 除了打印 按钮，其他都不显示，页面只读
						if (tzms_stpinsstat != null && (tzms_stpinsstat.equals("2") || tzms_stpinsstat.equals("5")
								|| tzms_stpinsstat.equals("7"))) {
							mapJson = new HashMap<String, Object>();
							mapJson.put("stepFlag", "1003");
							mapJson.put("tzms_is_print", tzms_is_print);
							mapJson.put("TZms_DPYPRCREC1", TZms_DPYPRCREC1);
							mapJson.put("tzms_show_process_flg", tzms_show_process_flg);
							strRet = jacksonUtil.Map2json(mapJson);
							return strRet;
						}
						
						// 1-激活、3-处理中、4-结束、8-已签收、9-转发
						// step5 4和 9 一样 的時候校验撤回按钮是否显示，除了打印 按钮，其他都不显示，页面只读
						if (tzms_stpinsstat != null && (tzms_stpinsstat.equals("4") || tzms_stpinsstat.equals("9"))) {
							
							mapJson.put("stepFlag", "1005");
							mapJson.put("tzms_is_print", tzms_is_print);
							mapJson.put("TZms_DPYPRCREC1", TZms_DPYPRCREC1);
							mapJson.put("tzms_show_process_flg", tzms_show_process_flg);
							// 已经提交的，没人签收，这是申请人可以撤回
							if (tzms_dpycalflg.equals("true")) {
								//注意：抄送任务签收不影响撤回
								String sql = "SELECT COUNT(*) FROM tzms_stpins_tbl WHERE tzms_stpinsid IN (SELECT tzms_stpinsid FROM tzms_stpins_pre_tbl WHERE tzms_prestpinsid =?) and ltrim(rtrim(tzms_stpproid)) != '' and tzms_asgmethod<>'1'";
								int r = sqlQuery.queryForObject(sql, new Object[] { tzms_stpinsid }, "Integer");
								if (r < 1) {
									mapJson.put("tzms_dpycalflg", tzms_dpycalflg);
								} else {
									mapJson.put("tzms_dpycalflg", no);
								}
							} else {
								mapJson.put("tzms_dpycalflg", no);
							}
							
						} else {
							// 1-激活、3-处理中、8-已签收
							// step7 1 3 8 一樣，正常显示
							mapJson.put("stepFlag", "1004");
							mapJson.put("tzms_is_print", tzms_is_print);
							// 非首步骤不显示保存按钮
							if("1".equals(isFast)) {
								mapJson.put("tzms_save_flg", "1");
							}else {
								mapJson.put("tzms_save_flg", no);
							}
							
							mapJson.put("tzms_close_flg", tzms_close_flg);
							mapJson.put("tzms_manu_close_flg", tzms_manu_close_flg);
							mapJson.put("TZms_DPYPRCREC1", TZms_DPYPRCREC1);
							mapJson.put("TZms_DPYPRCFLG1", TZms_DPYPRCFLG1);
							mapJson.put("tzms_show_process_flg", tzms_show_process_flg);
							mapJson.put("tzms_show_gowfl_flg", tzms_show_gowfl_flg);
							mapJson.put("tzms_dpycpyflg", tzms_dpycpyflg);
							mapJson.put("tzms_dpytsfflg", tzms_dpytsfflg);
							mapJson.put("tzms_dpybak_reflg", tzms_dpybak_reflg);
							mapJson.put("tzms_dpybakflg", tzms_dpybakflg);
							mapJson.put("tzms_buttonname", tzms_buttonname);

							// 加签按钮需要特殊处理下，如果该步骤实例 的任务分配方式 是加签，那么加签按钮不能显示
							if (tzms_asgmethod != null && tzms_asgmethod.equals("2")) {
								mapJson.put("tzms_dpyhqrflg", no);
							} else {
								mapJson.put("tzms_dpyhqrflg", tzms_dpyhqrflg);
							}

							mapJson.put("tzms_dpycalflg", no);
							// 【撤销】功能只有首步骤才有这个功能
							if ("1".equals(isFast)) {
								mapJson.put("tzms_dpyrevokeflg", tzms_dpyrevokeflg);
							} else {
								mapJson.put("tzms_dpyrevokeflg", no);
							}
							mapJson.put("tzms_dpysubmitflg", tzms_dpysubmitflg);
							mapJson.put("tzms_dpyapproveflg", tzms_dpyapproveflg);
							mapJson.put("tzms_dpybatrefuseflg", tzms_dpybatrefuseflg);
							mapJson.put("tzms_ifdisdisplaypage", tzms_ifdisdisplaypage);
							mapJson.put("tzms_stpinsstat", tzms_stpinsstat);
						}
						strRet = jacksonUtil.Map2json(mapJson);
						return strRet;
					}
				}
			}

			break;
		case "DynamicsYZ": // 显示资源
			System.out.println("DynamicsApplicationJSImpl=========================DynamicsYZ");
			jacksonUtil.json2Map(strParams);
			System.out.println(strParams);
			if (jacksonUtil.containsKey("tzms_wflinsid")) {
				tzms_wflinsid = jacksonUtil.getString("tzms_wflinsid");
			}
			if (StringUtils.isBlank(tzms_wflinsid)) {
				tzms_wflinsid = request.getParameter("tzms_wflinsid");
			}
			if (jacksonUtil.containsKey("tzms_stpinsid")) {
				tzms_stpinsid = jacksonUtil.getString("tzms_stpinsid");
			}
			if (StringUtils.isBlank(tzms_stpinsid)) {
				tzms_stpinsid = request.getParameter("tzms_stpinsid");
			}
			if (jacksonUtil.containsKey("etc")) {
				etc = jacksonUtil.getString("etc");
			}
			if (StringUtils.isBlank(etc)) {
				etc = request.getParameter("etc");
			}

			if (tzms_wflinsid == null) {
				tzms_wflinsid = "";
			}
			if (tzms_stpinsid == null) {
				tzms_stpinsid = "";
			}
			System.out.println("DynamicsApplicationJSImpl=========================etc:" + etc);
			System.out.println("DynamicsApplicationJSImpl=========================tzms_wflinsid:" + tzms_wflinsid);
			System.out.println("DynamicsApplicationJSImpl=========================tzms_stpinsid:" + tzms_stpinsid);
			StringBuffer sb = new StringBuffer();
			List<Map<String, Object>> zyList = null;
			List<String> webResList = null;
			boolean isAlive = true;		//流程任务是否存活
			
			// 如果两个实例ID为空，说明是新建立的
			if (tzms_wflinsid.equals("") && tzms_stpinsid.equals("")) {
				String sql = "SELECT TOP 1 A.tzms_wfcldn_tid FROM tzms_wfcldn_tBase A,Entity B WHERE A.tzms_entity_name=B.Name AND B.ObjectTypeCode=?";
				String tzms_wfcldn_tid = sqlQuery.queryForObject(sql, new Object[] { etc }, "String");
				sb.append("SELECT");
				sb.append("	A.tzms_field_name,");
				sb.append("	A.tzms_field_desc,");
				sb.append("	A.tzms_field_operate ");
				sb.append("FROM");
				sb.append("	tzms_wf_stpztmx_tBase A,");
				sb.append("	tzms_wflstp_tBase B ");
				sb.append("WHERE ");
				sb.append("	A.tzms_wfstp_uniqueid = B.tzms_wflstp_tid ");
				sb.append(" AND B.tzms_stptype =1");
				sb.append(" AND B.tzms_wfcldn_uniqueid =?");
				zyList = sqlQuery.queryForList(sb.toString(), new Object[] { tzms_wfcldn_tid });

				//Web资源控制
				webResList = sqlQuery.queryForList("select tzms_websrc_name from tzms_wfstp_webrsc_tBase A where exists(select 'Y' from tzms_wflstp_tBase where tzms_wflstp_tid=A.tzms_wfstp_uniqueid AND tzms_stptype=1 and tzms_wfcldn_uniqueid=?)", 
						new Object[] { tzms_wfcldn_tid }, "String");
			} else {

				sb.append("SELECT");
				sb.append("	A.tzms_field_name,");
				sb.append("	A.tzms_field_desc,");
				sb.append("	A.tzms_field_operate ");
				sb.append("FROM");
				sb.append("	tzms_wf_stpztmx_tBase A,");
				sb.append("	tzms_stpins_tbl B ");
				sb.append("WHERE ");
				sb.append("	A.tzms_wfstp_uniqueid = B.tzms_wflstpid ");
				sb.append(" AND B.tzms_wflinsid =?");
				sb.append(" AND B.tzms_stpinsid =?");
				zyList = sqlQuery.queryForList(sb.toString(), new Object[] { tzms_wflinsid, tzms_stpinsid });
				
				//Web资源控制
				webResList = sqlQuery.queryForList("select tzms_websrc_name from tzms_wfstp_webrsc_tBase A where exists(select 'Y' from tzms_stpins_tbl where tzms_wflstpid=A.tzms_wfstp_uniqueid AND tzms_wflinsid=? and tzms_stpinsid=?)", 
						new Object[] { tzms_wflinsid, tzms_stpinsid }, "String");
				
				//查询工作流是否存活
				String alive = sqlQuery.queryForObject("select 'Y' from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=? and tzms_stpinsstat in('1','3','6','8') and tzms_asgmethod<>'1'", 
						new Object[] { tzms_wflinsid, tzms_stpinsid }, "String");
				if(!"Y".equals(alive)) {
					isAlive = false;
				}
			}
			if(webResList == null) {
				webResList = new ArrayList<String>();
			}

			String tzms_field_name = "";
			String tzms_field_desc = "";
			String tzms_field_operate = "";
			ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < zyList.size(); i++) {
				map = zyList.get(i);
				tzms_field_name = map.get("tzms_field_name") != null ? map.get("tzms_field_name").toString() : "";
				tzms_field_desc = map.get("tzms_field_desc") != null ? map.get("tzms_field_desc").toString() : "";
				tzms_field_operate = map.get("tzms_field_operate") != null ? map.get("tzms_field_operate").toString()
						: "";
				mapJson = new HashMap<String, Object>();
				mapJson.put("Name", tzms_field_name);
				mapJson.put("Flag", tzms_field_operate);
				mapJson.put("Desc", tzms_field_desc);
				list.add(jacksonUtil.Map2json(mapJson));
			}
			
			mapJson = new HashMap<String, Object>();
			mapJson.put("isAlive", isAlive);
			mapJson.put("crlFields", list);
			mapJson.put("hideWebRes", webResList);
			
			return jacksonUtil.Map2json(mapJson);
		case "SaveWfl": // 工作流的保存
			System.out.println("DynamicsApplicationJSImpl=========================SaveWfl");
			jacksonUtil.json2Map(strParams);
			String tzms_wflrecordid = "";
			String userid = "";
			// 这个参数是从页面传过来的dynamics ID，也可以从JAVA系统当前登陆ID获取
			if (jacksonUtil.containsKey("userid")) {
				userid = jacksonUtil.getString("userid");
			}
			if (StringUtils.isBlank(userid)) {
				userid = request.getParameter("userid");
			}

			if (jacksonUtil.containsKey("tzms_wflrecordid")) {
				tzms_wflrecordid = jacksonUtil.getString("tzms_wflrecordid");
			}
			if (StringUtils.isBlank(tzms_wflrecordid)) {
				tzms_wflrecordid = request.getParameter("tzms_wflrecordid");
			}

			if (jacksonUtil.containsKey("etc")) {
				etc = jacksonUtil.getString("etc");
			}
			if (StringUtils.isBlank(etc)) {
				etc = request.getParameter("etc");
			}

			if (jacksonUtil.containsKey("tzms_wflinsid")) {
				tzms_wflinsid = jacksonUtil.getString("tzms_wflinsid");
			}
			if (StringUtils.isBlank(tzms_wflinsid)) {
				tzms_wflinsid = request.getParameter("tzms_wflinsid");
			}
			if (jacksonUtil.containsKey("tzms_stpinsid")) {
				tzms_stpinsid = jacksonUtil.getString("tzms_stpinsid");
			}
			if (StringUtils.isBlank(tzms_stpinsid)) {
				tzms_stpinsid = request.getParameter("tzms_stpinsid");
			}
			if (userid != null && !userid.equals("") && userid.length() > 2) {
				userid = this.clearString(userid);
			}
			if (tzms_wflrecordid != null && !tzms_wflrecordid.equals("") && tzms_wflrecordid.length() > 2) {
				tzms_wflrecordid = this.clearString(tzms_wflrecordid);
			}
			if (tzms_wflinsid == null) {
				tzms_wflinsid = "";
			}
			if (tzms_stpinsid == null) {
				tzms_stpinsid = "";
			}
			System.out.println("DynamicsApplicationJSImpl=========================userid:" + userid);
			System.out.println("DynamicsApplicationJSImpl=========================etc:" + etc);
			System.out.println("DynamicsApplicationJSImpl=========================tzms_wflinsid:" + tzms_wflinsid);
			System.out.println("DynamicsApplicationJSImpl=========================tzms_stpinsid:" + tzms_stpinsid);
			System.out
					.println("DynamicsApplicationJSImpl=========================tzms_wflrecordid:" + tzms_wflrecordid);

			// 调用张浪接口 保存工作流
			// 如果两个实例ID为空，说明是新建立的
			if (tzms_wflinsid.equals("") && tzms_stpinsid.equals("")) {
				String sql = "SELECT TOP 1 A.tzms_wfcldn_tid FROM tzms_wfcldn_tBase A,Entity B WHERE A.tzms_entity_name=B.Name AND B.ObjectTypeCode=?";
				String tzms_wfcldn_tid = sqlQuery.queryForObject(sql, new Object[] { etc }, "String");

				TzWorkflow workflowInstance = new TzWorkflow(userid);

				boolean createOK;
				String ErrorMsg = "";
				try {
					// 流程创建
					createOK = workflowInstance.CreateWorkflow0(tzms_wfcldn_tid, tzms_wflrecordid);
				} catch (Exception e) {
					createOK = false;
					ErrorMsg = e.getMessage(); // 错误信息
				}

				if (createOK == true) {
					// 工作流实例编号
					String wflInsId = workflowInstance.getM_WflInstanceID();
					// 步骤实例编号
					String wflStpInsId = workflowInstance.getM_WflStpInsID();
					//步骤编号
					String wflstdId = workflowInstance.getM_WorkflowStepInstance().getM_WflStepID();
					//关联业务实体
					String entityName = sqlQuery.queryForObject("SELECT tzms_entity_name FROM tzms_wfcldn_t A,tzms_wflins_tbl B WHERE B.tzms_wfcldn_uniqueid = A.tzms_wfcldn_tid  AND B.tzms_wflinsid = ?",new Object[]{wflInsId},"String");
					String url = workFlowPublic.getWflinsWindowsURL(entityName, wflstdId, tzms_wflrecordid,wflInsId,wflStpInsId);
					mapJson = new HashMap<String, Object>();
					mapJson.put("stepFlag", "0");
					mapJson.put("wflInsId", wflInsId);
					mapJson.put("wflStpInsId", wflStpInsId);
					mapJson.put("url", url);
					strRet = jacksonUtil.Map2json(mapJson);
					return strRet;
				} else {
					mapJson = new HashMap<String, Object>();
					mapJson.put("stepFlag", "1");
					mapJson.put("msg", ErrorMsg);
					strRet = jacksonUtil.Map2json(mapJson);
					return strRet;
				}
			} else {// 如果已经创建，直接返回
				mapJson = new HashMap<String, Object>();
				mapJson.put("stepFlag", "0");
				mapJson.put("wflInsId", tzms_wflinsid);
				mapJson.put("wflStpInsId", tzms_stpinsid);
				mapJson.put("url", "");
				strRet = jacksonUtil.Map2json(mapJson);
				return strRet;

			}

			// break;
		default:
			break;
		}
		return strRet;
	}

	private String clearString(String str) {
		str = str.substring(1, str.length() - 1);
		return str;
	}

}
