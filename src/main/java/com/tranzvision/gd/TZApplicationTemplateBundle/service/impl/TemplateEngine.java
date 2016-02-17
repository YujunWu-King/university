package com.tranzvision.gd.TZApplicationTemplateBundle.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationTemplateBundle.dao.PsTzAppEventsTMapper;
import com.tranzvision.gd.TZApplicationTemplateBundle.dao.PsTzAppXxJygzTMapper;
import com.tranzvision.gd.TZApplicationTemplateBundle.dao.PsTzAppXxSyncTMapper;
import com.tranzvision.gd.TZApplicationTemplateBundle.dao.PsTzAppXxxKxzTMapper;
import com.tranzvision.gd.TZApplicationTemplateBundle.dao.PsTzAppXxxPzTMapper;
import com.tranzvision.gd.TZApplicationTemplateBundle.dao.PsTzApptplDyTMapper;
import com.tranzvision.gd.TZApplicationTemplateBundle.dao.PsTzRqXxxPzTMapper;
import com.tranzvision.gd.TZApplicationTemplateBundle.dao.PsTzTempFieldTMapper;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzAppEventsT;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzAppXxJygzT;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzAppXxSyncT;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzAppXxxKxzT;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzAppXxxPzTWithBLOBs;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzApptplDyTWithBLOBs;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzRqXxxPzT;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzTempFieldT;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.TZControlSetBundle.dao.PsTzComDyTMapper;
import com.tranzvision.gd.TZControlSetBundle.model.PsTzComDyT;
import com.tranzvision.gd.TZRuleSetBundle.dao.PsTzJygzDyEngMapper;
import com.tranzvision.gd.TZRuleSetBundle.dao.PsTzJygzDyTMapper;
import com.tranzvision.gd.TZRuleSetBundle.model.PsTzJygzDyEng;
import com.tranzvision.gd.TZRuleSetBundle.model.PsTzJygzDyEngKey;
import com.tranzvision.gd.TZRuleSetBundle.model.PsTzJygzDyT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service
public class TemplateEngine {
	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private HttpServletResponse response;

	@Autowired
	private TZGDObject tzSQLObject;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;

	@Autowired
	private PsTzJygzDyTMapper psTzJygzDyTMapper;

	@Autowired
	private PsTzJygzDyEngMapper psTzJygzDyEngMapper;

	@Autowired
	private PsTzApptplDyTMapper psTzApptplDyTMapper;

	@Autowired
	private PsTzComDyTMapper psTzComDyTMapper;

	@Autowired
	private PsTzAppEventsTMapper psTzAppEventsTMapper;

	@Autowired
	private PsTzAppXxJygzTMapper psTzAppXxJygzTMapper;

	@Autowired
	private PsTzRqXxxPzTMapper psTzRqXxxPzTMapper;

	@Autowired
	private PsTzAppXxxPzTMapper psTzAppXxxPzTMapper;

	@Autowired
	private PsTzAppXxxKxzTMapper psTzAppXxxKxzTMapper;

	@Autowired
	private PsTzAppXxSyncTMapper psTzAppXxSyncTMapper;
	
	@Autowired
	private PsTzTempFieldTMapper psTzTempFieldTMapper;

	@Autowired
	private TZGDObject tzGdObject;

	private String tid;

	@SuppressWarnings("unchecked")
	public String saveTpl(String tid, Map<String, Object> infoData) {
		this.tid = tid;
		String successFlag = "0";
		String strMsg = "";
		String diffMsg = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (StringUtils.isNotBlank(tid) && infoData != null) {

			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			Date dateNow = new Date();
			String mainTplId = "";
			try {
				/* -------模板基本信息更新------- Begin */
				PsTzApptplDyTWithBLOBs psTzApptplDyT = new PsTzApptplDyTWithBLOBs();
				psTzApptplDyT.setTzAppTplId(tid);

				// 模板名称
				if (infoData.containsKey("tplName")) {
					psTzApptplDyT.setTzAppTplMc((String) infoData.get("tplName"));
				}

				// 模板描述
				if (infoData.containsKey("tplDesc")) {
					psTzApptplDyT.setTzAppTplMs((String) infoData.get("tplDesc"));
				}

				// 模板用途（在线报名、在线调查）
				if (infoData.containsKey("tplUse")) {
					psTzApptplDyT.setTzAppTplYt((String) infoData.get("tplUse"));
				}

				// 模板类型（报名表、推荐信）
				if (infoData.containsKey("tplUseType")) {
					psTzApptplDyT.setTzUseType((String) infoData.get("tplUseType"));
				}

				// 标签位置
				if (infoData.containsKey("labelPostion")) {
					psTzApptplDyT.setTzAppLabelWz((String) infoData.get("labelPostion"));
				}

				// 提示信息方式
				if (infoData.containsKey("showType")) {
					psTzApptplDyT.setTzAppTsxxFs((String) infoData.get("showType"));
				}

				// 语言
				if (infoData.containsKey("lang")) {
					psTzApptplDyT.setTzAppTplLan((String) infoData.get("lang"));
				}

				// 文件名
				if (infoData.containsKey("filename")) {
					psTzApptplDyT.setTzAttachfileName((String) infoData.get("filename"));
				}

				// 系统文件名
				if (infoData.containsKey("sysFileName")) {
					psTzApptplDyT.setTzAttsysfilename((String) infoData.get("sysFileName"));
				}

				// 绝对路径
				if (infoData.containsKey("path")) {
					psTzApptplDyT.setTzAttPUrl((String) infoData.get("path"));
				}

				// 访问路径
				if (infoData.containsKey("accessPath")) {
					psTzApptplDyT.setTzAttAUrl((String) infoData.get("accessPath"));
				}

				// 提交跳转方式
				if (infoData.containsKey("targetType")) {
					psTzApptplDyT.setTzAppTzfs((String) infoData.get("targetType"));
				}

				// Redirect Url
				if (infoData.containsKey("redirectUrl")) {
					psTzApptplDyT.setTzAppTzurl((String) infoData.get("redirectUrl"));
				}

				// 报名表状态
				if (infoData.containsKey("state")) {
					psTzApptplDyT.setTzEffexpZt((String) infoData.get("state"));
				}

				// 报名表主模板ID
				if (infoData.containsKey("mainTemplate")) {
					mainTplId = (String) infoData.get("mainTemplate");
					psTzApptplDyT.setTzAppMTplId(mainTplId);
				}

				// 提交后是否发送邮件
				if (infoData.containsKey("isSendMail")) {
					psTzApptplDyT.setTzIssendmail((String) infoData.get("isSendMail"));
				}

				// 邮件模板
				if (infoData.containsKey("mailTemplate")) {
					psTzApptplDyT.setTzEmlModalId((String) infoData.get("mailTemplate"));
				}

				// 报名表报文
				psTzApptplDyT.setTzApptplJsonStr(jacksonUtil.Map2json(infoData));
				psTzApptplDyT.setRowLastmantDttm(dateNow);
				psTzApptplDyT.setRowLastmantOprid(oprid);

				psTzApptplDyTMapper.updateByPrimaryKeySelective(psTzApptplDyT);
				/*------- 模板基本信息更新 End -------*/

				/*------- 模板事件设置更新 Begin -------*/
				if (infoData.containsKey("events")) {

					Map<String, Object> eventsData = (Map<String, Object>) infoData.get("events");

					String eventsSql = "DELETE FROM PS_TZ_APP_EVENTS_T WHERE TZ_APP_TPL_ID = ?";
					sqlQuery.update(eventsSql, new Object[] { tid });

					for (String key : eventsData.keySet()) {
						Map<String, Object> event = (Map<String, Object>) eventsData.get(key);
						PsTzAppEventsT psTzAppEventsT = new PsTzAppEventsT();
						// 模板编号
						psTzAppEventsT.setTzAppTplId(tid);

						// 事件编号
						psTzAppEventsT.setTzEventId(key);

						// 是否启用
						if (event.containsKey("isEff")) {
							psTzAppEventsT.setTzQyBz((String) infoData.get("isEff"));
						}

						// 应用程序类路径
						if (event.containsKey("classPath")) {
							psTzAppEventsT.setCmbcAppclsPath((String) infoData.get("classPath"));
						}

						// 应用程序类名称
						if (event.containsKey("className")) {
							psTzAppEventsT.setCmbcAppclsName((String) infoData.get("className"));
						}

						// 应用程序类方法
						if (event.containsKey("classFun")) {
							psTzAppEventsT.setCmbcAppclsMethod((String) infoData.get("classFun"));
						}

						// 事件类型
						if (event.containsKey("eventType")) {
							psTzAppEventsT.setTzEventType((String) infoData.get("eventType"));
						}

						psTzAppEventsTMapper.insert(psTzAppEventsT);
					}
				}
				/*------- 模板事件设置更新 End -------*/

				/*------- 报名表模板信息项维护 Begin -------*/
				if (infoData.containsKey("items")) {
					this.delTpl(tid);
					Map<String, Object> itemsData = (Map<String, Object>) infoData.get("items");

					for (String key : itemsData.keySet()) {
						Map<String, Object> item = (Map<String, Object>) itemsData.get(key);
						String itemId = "";
						String isDoubleLine = "";
						String isSingleLine = "";
						int pageno = 0;
						// 信息项编号
						if (item.containsKey("itemId")) {
							itemId = (String) item.get("itemId");
						}

						// 是否多行容器
						if (item.containsKey("isDoubleLine")) {
							isDoubleLine = (String) item.get("isDoubleLine");
						}

						// 是否单行
						if (item.containsKey("isSingleLine")) {
							isSingleLine = (String) item.get("isSingleLine");
						}

						// 页码
						if (item.containsKey("pageno")) {
							pageno = Integer.parseInt("" + item.get("pageno"));
						}

						this.savePerXXX(item);
						this.saveTemplateField(item);

						/*------- 模板校验规则更新 Begin -------*/
//						if (item.containsKey("rules")) {
//							Map<String, Object> itemRules = (Map<String, Object>) item.get("rules");
//							for (String keyi : itemRules.keySet()) {
//								Map<String, Object> rule = (Map<String, Object>) itemRules.get(keyi);
//								PsTzAppXxJygzT psTzAppXxJygzT = new PsTzAppXxJygzT();
//								// 模板编号
//								psTzAppXxJygzT.setTzAppTplId(tid);
//
//								// 信息项编号
//								psTzAppXxJygzT.setTzXxxBh(itemId);
//
//								// 校验规则ID
//								if (rule.containsKey("ruleId")) {
//									psTzAppXxJygzT.setTzJygzId((String) rule.get("ruleId"));
//								}
//
//								// 是否启用
//								if (rule.containsKey("isEnable")) {
//									psTzAppXxJygzT.setTzQyBz((String) rule.get("isEnable"));
//								}
//
//								// 提示信息
//								if (rule.containsKey("messages")) {
//									psTzAppXxJygzT.setTzJygzTsxx((String) rule.get("messages"));
//								}
//								psTzAppXxJygzTMapper.insert(psTzAppXxJygzT);
//							}
//						}
						/*------- 模板校验规则更新 End -------*/

						/*------ 多行容器（通用多行容器、固定多行容器） Begin ------*/
						if (StringUtils.equals("Y", isDoubleLine)) {
							if (item.containsKey("children")) {
								Map<String, Object> childrens = (Map<String, Object>) item.get("children");
								for (String keyi : childrens.keySet()) {
									Map<String, Object> children = (Map<String, Object>) childrens.get(keyi);
									PsTzRqXxxPzT psTzRqXxxPzT = new PsTzRqXxxPzT();
									psTzRqXxxPzT.setTzAppTplId(tid);
									psTzRqXxxPzT.setTzDXxxBh(itemId);
									// 信息项编号
									String childItemid = "";
									if (children.containsKey("itemId")) {
										childItemid = (String) children.get("itemId");
										psTzRqXxxPzT.setTzXxxBh(childItemid);
									}

									// 排序
									if (children.containsKey("orderby")) {
										int order = Integer.parseInt("" + children.get("orderby"));
										psTzRqXxxPzT.setTzOrder(order);
									}
									int count = psTzRqXxxPzTMapper.insert(psTzRqXxxPzT);
									if (count > 0) {
										children.put("pageno", pageno);
										this.savePerXXX(children);
									}

									int childPageno = 0;
									if (children.containsKey("pageno")) {
										childPageno = Integer.parseInt("" + children.get("pageno"));
									}

									String isSingle = (String) children.get("isSingleLine");
									if (StringUtils.equals("Y", isSingle)) {
										ArrayList<Map<String, Object>> childs = (ArrayList<Map<String, Object>>) children
												.get("children");

										for (Object obj : childs) {
											Map<String, Object> child = (Map<String, Object>) obj;
											PsTzRqXxxPzT psTzRqXxxPzT_ = new PsTzRqXxxPzT();
											// 模板编号
											psTzRqXxxPzT_.setTzAppTplId(tid);

											// 组合框信息项编号
											psTzRqXxxPzT_.setTzDXxxBh(childItemid);
											String tzXxxBh = child.get("itemId") == null ? ""
													: String.valueOf(child.get("itemId"));

											// 信息项编号
											psTzRqXxxPzT_.setTzXxxBh(tzXxxBh);

											// 排序
											String order_ = child.get("orderby") == null ? "0"
													: String.valueOf(child.get("orderby"));
											psTzRqXxxPzT_.setTzOrder(Integer.parseInt(order_));
											int count_ = psTzRqXxxPzTMapper.insert(psTzRqXxxPzT_);
											if (count_ > 0) {
												child.put("pageno", childPageno);
												this.savePerXXX(child);
											}
										}
									}
								}
							}
						}
						/*------ 多行容器（通用多行容器、固定多行容器） End ------*/

						/*------ 单行组合控件 Begin ------*/
						if (StringUtils.equals("Y", isSingleLine)) {
							ArrayList<Map<String, Object>> childrens = (ArrayList<Map<String, Object>>) item.get("children");
							int i = 0;
							for (Object obj : childrens) {
								Map<String, Object> children = (Map<String, Object>) obj;
								PsTzRqXxxPzT psTzRqXxxPzT = new PsTzRqXxxPzT();
								// 模板编号
								psTzRqXxxPzT.setTzAppTplId(tid);

								// 组合框信息项编号
								psTzRqXxxPzT.setTzDXxxBh(itemId);

								// 信息项编号
								if (children.containsKey("itemId")) {
									psTzRqXxxPzT.setTzXxxBh((String) children.get("itemId"));
								}

								// 排序
								psTzRqXxxPzT.setTzOrder(i);
								int count = psTzRqXxxPzTMapper.insert(psTzRqXxxPzT);
								if (count > 0) {
									children.put("pageno", pageno);
									this.savePerXXX(children);
								}
								i++;
							}
						}
						/*------ 单行组合控件 End ------*/
					}
				}

				/*-- 报名表模板信息项维护 End --*/

				/* 检查主副报名表的差异 */
				if (StringUtils.isNotBlank(mainTplId)) {
					diffMsg = "";
					List<?> resultlist = sqlQuery.queryForList(
							tzSQLObject.getSQLText("SQL.TZApplicationTemplateBundle.TZ_CHECK_ZF_TPL_SQL"),
							new Object[] { mainTplId, tid });
					for (Object obj : resultlist) {
						Map<String, Object> result = (Map<String, Object>) obj;

						String xxxBh = result.get("TZ_XXX_BH") == null ? "" : String.valueOf(result.get("TZ_XXX_BH"));
						String xxxMc = result.get("TZ_XXX_MC") == null ? "" : String.valueOf(result.get("TZ_XXX_MC"));
						diffMsg = diffMsg + "信息项编号:" + xxxBh + ",信息项名称:" + xxxMc + "\n";
					}

				}
			} catch (Exception e) {
				successFlag = "1";
				strMsg = e.toString();
				e.printStackTrace();
			}
		}

		String alterMsg = "";

		if (StringUtils.isBlank(diffMsg)) {
			alterMsg = "当前报名表模版中的如下信息项在主模版中不存在或控件类名称不一致，请检查当前模版信息项配置。";
			alterMsg = alterMsg + "\n" + diffMsg;
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("code", successFlag);
		mapRet.put("msg", strMsg);
		mapRet.put("insid", "");
		mapRet.put("pageCompleteState", "");
		mapRet.put("alterMsg", alterMsg);
		mapRet.put("appInsVersionId", "");

		return jacksonUtil.Map2json(mapRet);
	}

	@SuppressWarnings("unchecked")
	private void saveTemplateField(Map<String, Object> item) {
		PsTzTempFieldT psTzTempFieldT = new PsTzTempFieldT();

		String parItemId = item.get("itemId") == null ? "" : String.valueOf(item.get("itemId"));
		String parOrderby = item.get("orderby") == null ? "0" : String.valueOf(item.get("orderby"));
		
		//模板编号
		psTzTempFieldT.setTzAppTplId(this.tid);
		//信息编号
		psTzTempFieldT.setTzXxxBh(parItemId);
		//容器信息项编号
		psTzTempFieldT.setTzDXxxBh("");
		//信息项编号
		psTzTempFieldT.setTzXxxNo(parItemId);
		//行号
		psTzTempFieldT.setTzLineNum(0);
		//行内序号
		psTzTempFieldT.setTzLineOrder(0);
		//排序序号
		psTzTempFieldT.setTzOrder(Integer.parseInt(parOrderby));
		psTzTempFieldTMapper.insert(psTzTempFieldT);
		
		String isDoubleLine = item.get("isDoubleLine") == null ? "" : String.valueOf(item.get("isDoubleLine"));
		String isSingleLine = item.get("isSingleLine") == null ? "" : String.valueOf(item.get("isSingleLine"));
		String fixedContainer = item.get("fixedContainer") == null ? "" : String.valueOf(item.get("fixedContainer"));
		String maxLines = item.get("maxLines") == null ? "0" : String.valueOf(item.get("maxLines"));

		//是否多行容器
		if(StringUtils.equals("Y", isDoubleLine)){
			Map<String, Object> childrens = (Map<String, Object>) item.get("children");
			for (int i = 0; i < Integer.parseInt(maxLines); i++) {  
				if(StringUtils.equals("Y", fixedContainer)){
					//固定多行容器
					for (String key : childrens.keySet()) {
						Map<String, Object> children = (Map<String, Object>) childrens.get(key);
						String itemId = children.get("itemId") == null ? "" : String.valueOf(children.get("itemId"));
						
						PsTzTempFieldT psTzTempFieldTFixed = new PsTzTempFieldT();
						//模板编号
						psTzTempFieldTFixed.setTzAppTplId(this.tid);
						//信息项编号
						if(i == 0){
							psTzTempFieldTFixed.setTzXxxBh(parItemId + itemId); 
						}else{
							psTzTempFieldTFixed.setTzXxxBh(parItemId + itemId + "_" + i); 
						}
						//信息项编号
						psTzTempFieldTFixed.setTzXxxNo(itemId);
						//多行容器编号
						psTzTempFieldTFixed.setTzDXxxBh(parItemId);
						//行号
						psTzTempFieldTFixed.setTzLineNum(i + 1);
						//行内编号
						if(children.containsKey("orderby")){
							String orderby = children.get("orderby") == null ? "0" : String.valueOf(children.get("orderby"));
							psTzTempFieldTFixed.setTzLineOrder(Integer.parseInt(orderby));
						}
						//排序序号
						psTzTempFieldTFixed.setTzOrder(Integer.parseInt(parOrderby));
						psTzTempFieldTMapper.insert(psTzTempFieldTFixed);
					}
				}else{
					//通用多行容器
					int order = 1;
					for (String key : childrens.keySet()) {
						Map<String, Object> children = (Map<String, Object>) childrens.get(key);
						String itemId = children.get("itemId") == null ? "" : String.valueOf(children.get("itemId"));
						String isSingle = children.get("isSingleLine") == null ? "" : String.valueOf(children.get("isSingleLine"));
						
						PsTzTempFieldT psTzTempFieldTTY = new PsTzTempFieldT();
						//模板编号
						psTzTempFieldTTY.setTzAppTplId(this.tid);
						//信息项编号
						if(i == 0){
							psTzTempFieldTTY.setTzXxxBh(parItemId + itemId);
						}else{
							psTzTempFieldTTY.setTzXxxBh(parItemId + itemId + "_" + i);
						}
						//信息项编号
						psTzTempFieldTTY.setTzXxxNo(itemId);
						//多行容器编号
						psTzTempFieldTTY.setTzDXxxBh(parItemId);
						//行号
						psTzTempFieldTTY.setTzLineNum(i + 1);
						//行内编号
						psTzTempFieldTTY.setTzLineOrder(order);
						//排序序号
						psTzTempFieldTTY.setTzOrder(Integer.parseInt(parOrderby));
						psTzTempFieldTMapper.insert(psTzTempFieldTTY);
						
						order ++;
						if(StringUtils.equals("Y", isSingle)){
							ArrayList<Map<String, Object>> childs = (ArrayList<Map<String, Object>>) children.get("children");

							for (Object obj : childs) {
								Map<String, Object> child = (Map<String, Object>) obj;
								String childItemId = child.get("itemId") == null ? "" : String.valueOf(child.get("itemId"));
								PsTzTempFieldT psTzTempFieldTSingle = new PsTzTempFieldT();
								//模板编号
								psTzTempFieldTSingle.setTzAppTplId(this.tid);
								//信息项编号
								if(i == 0){
									psTzTempFieldTSingle.setTzXxxBh(parItemId + itemId + childItemId);
								}else{
									psTzTempFieldTSingle.setTzXxxBh(parItemId + itemId + childItemId + "_" + i);
								}
								//信息项编号
								psTzTempFieldTSingle.setTzXxxNo(childItemId);
								//多行容器编号
								psTzTempFieldTSingle.setTzDXxxBh(parItemId);
								//行号
								psTzTempFieldTSingle.setTzLineNum(i + 1);
								//行内编号
								psTzTempFieldTSingle.setTzLineOrder(order);
								//排序序号
								psTzTempFieldTSingle.setTzOrder(Integer.parseInt(parOrderby));
								
								psTzTempFieldTMapper.insert(psTzTempFieldTSingle);
								order ++;
							}
						}
					}
				}
			} 
		}
		
		//是否组合控件
		if(StringUtils.equals("Y", isSingleLine)){
			ArrayList<Map<String, Object>> childrens = (ArrayList<Map<String, Object>>) item.get("children");
			int i = 0;
			for (Object obj : childrens) {
				Map<String, Object> children = (Map<String, Object>) obj;
				PsTzTempFieldT psTzTempFieldTSingle = new PsTzTempFieldT();
				String itemId = children.get("itemId") == null ? "" : String.valueOf(children.get("itemId"));
				//模板编号
				psTzTempFieldTSingle.setTzAppTplId(this.tid);
				//信息项编号
				psTzTempFieldTSingle.setTzXxxBh(parItemId + itemId);
				//信息项编号
				psTzTempFieldTSingle.setTzXxxNo(itemId);
				//多行容器编号
				psTzTempFieldTSingle.setTzDXxxBh(parItemId);
				//行号
				psTzTempFieldTSingle.setTzLineNum(0);
				//行内编号
				psTzTempFieldTSingle.setTzLineOrder(i + 1);
				//排序序号
				psTzTempFieldTSingle.setTzOrder(Integer.parseInt(parOrderby));
				
				psTzTempFieldTMapper.insert(psTzTempFieldTSingle);
				i++;
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void savePerXXX(Map<String, Object> item) {
		PsTzAppXxxPzTWithBLOBs psTzAppXxxPz = new PsTzAppXxxPzTWithBLOBs();
		String itemId = "";
		psTzAppXxxPz.setTzAppTplId(this.tid);

		// 信息项编号
		if (item.containsKey("itemId")) {
			itemId = (String) item.get("itemId");
			psTzAppXxxPz.setTzXxxBh(itemId);
		}

		// 信息项实例ID
		if (item.containsKey("instanceId")) {
			psTzAppXxxPz.setTzXxxSlid((String) item.get("instanceId"));
		}

		// 信息项文字说明
		if (item.containsKey("itemName")) {
			psTzAppXxxPz.setTzXxxMc((String) item.get("itemName"));
		}

		// 信息项名称
		if (item.containsKey("title")) {
			psTzAppXxxPz.setTzTitle((String) item.get("title"));
		}

		// 排序序号
		if (item.containsKey("orderby")) {
			String order = "" + item.get("orderby");
			psTzAppXxxPz.setTzOrder(Integer.parseInt(order));
		}

		// 分页号
		if (item.containsKey("pageno")) {
			String pageno = "" + item.get("pageno");
			psTzAppXxxPz.setTzPageNo(Integer.parseInt(pageno));
		}

		// 控件类名称
		if (item.containsKey("classname")) {
			psTzAppXxxPz.setTzComLmc((String) item.get("classname"));
		}

		// 空值提示信息
		if (item.containsKey("emptyText")) {
			psTzAppXxxPz.setTzXxxKztsxx((String) item.get("emptyText"));
		}

		// 焦点提示信息
		if (item.containsKey("onFoucsMessage")) {
			psTzAppXxxPz.setTzXxxJdtsxx((String) item.get("onFoucsMessage"));
		}

		// 默认值
		if (item.containsKey("defaultval")) {
			psTzAppXxxPz.setTzXxxMrz((String) item.get("defaultval"));
		}

		// 后缀
		if (item.containsKey("suffix")) {
			psTzAppXxxPz.setTzXxxHz((String) item.get("suffix"));
		}

		// 后缀链接
		if (item.containsKey("suffixUrl")) {
			psTzAppXxxPz.setTzXxxHzlj((String) item.get("suffixUrl"));
		}

		// 存储类型
		if (item.containsKey("StorageType")) {
			psTzAppXxxPz.setTzXxxCclx((String) item.get("StorageType"));
		}

		// 文本框大小
		if (item.containsKey("boxSize")) {
			psTzAppXxxPz.setTzXxxWbkdx((String) item.get("boxSize"));
		}

		// 日期格式
		if (item.containsKey("dateformate")) {
			psTzAppXxxPz.setTzXxxRqgs((String) item.get("dateformate"));
		}

		// 年份最小值
		if (item.containsKey("minYear")) {
			psTzAppXxxPz.setTzXxxNfmin((String) item.get("minYear"));
		}

		// 年份最大值
		if (item.containsKey("maxYear")) {
			psTzAppXxxPz.setTzXxxNfmax((String) item.get("maxYear"));
		}

		// 单选、多选排列方式
		if (item.containsKey("plfs")) {
			psTzAppXxxPz.setTzXxxPlfs((String) item.get("plfs"));
		}

		// 多选最少选择个数
		if (item.containsKey("minSelect")) {
			String minSelect = "" + item.get("minSelect");
			psTzAppXxxPz.setTzXxxZsxzgs(Integer.parseInt(minSelect));
		}

		// 多选最多选择个数
		if (item.containsKey("maxSelect")) {
			String maxSelect = "" + item.get("maxSelect");
			psTzAppXxxPz.setTzXxxZdxzgs(Integer.parseInt(maxSelect));
		}

		// 允许上传类型
		if (item.containsKey("yxsclx")) {
			psTzAppXxxPz.setTzXxxYxsclx((String) item.get("yxsclx"));
		}

		// 允许上传大小
		if (item.containsKey("yxscdx")) {
			String yxscdx = "" + item.get("yxscdx");
			psTzAppXxxPz.setTzXxxYxscdx(Integer.parseInt(yxscdx));
		}

		// 允许多附件上传
		if (item.containsKey("allowMultiAtta")) {
			psTzAppXxxPz.setTzXxxMulti((String) item.get("allowMultiAtta"));
		}

		// 图片是否裁剪
		if (item.containsKey("tpsfcj")) {
			psTzAppXxxPz.setTzXxxSfcj((String) item.get("tpsfcj"));
		}

		// 图片裁剪类型
		if (item.containsKey("tpcjlx")) {
			psTzAppXxxPz.setTzXxxTpcjlx((String) item.get("tpcjlx"));
		}

		// 文字说明
		if (item.containsKey("wzsm")) {
			psTzAppXxxPz.setTzXxxWzsm((String) item.get("wzsm"));
		}

		// 是否必填
		if (item.containsKey("isRequire")) {
			psTzAppXxxPz.setTzXxxBtBz((String) item.get("isRequire"));
		}

		// 是否字数限制
		if (item.containsKey("isCheckStrLen")) {
			psTzAppXxxPz.setTzXxxCharBz((String) item.get("isCheckStrLen"));
		}

		// 最小长度
		if (item.containsKey("minLen")) {
			String minLen = "" + item.get("minLen");
			psTzAppXxxPz.setTzXxxMinlen(Integer.parseInt(minLen));
		}

		// 最大长度
		if (item.containsKey("maxLen")) {
			String maxLen = "" + item.get("maxLen");
			psTzAppXxxPz.setTzXxxMaxlen(Integer.parseInt(maxLen));
		}

		// 是否限制数字范围
		if (item.containsKey("isNumSize")) {
			psTzAppXxxPz.setTzXxxNumBz((String) item.get("isNumSize"));
		}

		// 最小值
		if (item.containsKey("min")) {
			String min = "" + item.get("min");
			psTzAppXxxPz.setTzXxxMin(Long.parseLong(min));
		}

		// 最大值
		if (item.containsKey("max")) {
			String max = "" + item.get("max");
			psTzAppXxxPz.setTzXxxMax(Long.parseLong(max));
		}

		// 小数位数
		if (item.containsKey("digits")) {
			String digits = "" + item.get("digits");
			psTzAppXxxPz.setTzXxxXsws(Integer.parseInt(digits));
		}

		// 固定规则校验
		if (item.containsKey("preg")) {
			psTzAppXxxPz.setTzXxxGdgsjy((String) item.get("preg"));
		}

		// 是否多行容器
		if (item.containsKey("isDoubleLine")) {
			psTzAppXxxPz.setTzXxxDrqBz((String) item.get("isDoubleLine"));
		}

		// 最小行记录数
		if (item.containsKey("minLines")) {
			String minLines = "" + item.get("minLines");
			psTzAppXxxPz.setTzXxxMinLine(Integer.parseInt(minLines));
		}

		// 最大行记录数
		if (item.containsKey("maxLines")) {
			String maxLines = "" + item.get("maxLines");
			psTzAppXxxPz.setTzXxxMaxLine(Integer.parseInt(maxLines));
		}

		// 是否为单行组合
		if (item.containsKey("isSingleLine")) {
			psTzAppXxxPz.setTzXxxSrqBz((String) item.get("isSingleLine"));
		}

		// 外网是否可下载
		if (item.containsKey("isDownLoad")) {
			psTzAppXxxPz.setTzIsDownload((String) item.get("isDownLoad"));
		}

		if (item.containsKey("tapStyle")) {
			psTzAppXxxPz.setTzTapstyle((String) item.get("tapStyle"));
		}

		if (item.containsKey("toCheck")) {
			psTzAppXxxPz.setTzTjxSub((String) item.get("toCheck"));
		}

		// if(item.containsKey("toCheckRefApp")){
		// psTzAppXxxPz.setTzRefCheck((String)item.get("toCheckRefApp"));
		// }

		// if(item.containsKey("checkRefApp")){
		// psTzAppXxxPz.setTzRefCheckApp((String)item.get("checkRefApp"));
		// }

		// 是否显示在报名表审核
		if (item.containsKey("isShow")) {
			psTzAppXxxPz.setTzIsShow((String) item.get("isShow"));
		}

		// 是否启用PDF在线阅读
		if (item.containsKey("isOnlineShow")) {
			psTzAppXxxPz.setTzIsOnlineshow((String) item.get("isOnlineShow"));
		}

		// //是否只读
		// if(item.containsKey("isReadOnly")){
		// psTzAppXxxPz.setReadonly((String)item.get("isReadOnly"));
		// }

		/*------- 是否Option Begin -------*/
		if (item.containsKey("option")) {

			Map<String, Object> options = (Map<String, Object>) item.get("option");
			for (String keyi : options.keySet()) {
				Map<String, Object> option = (Map<String, Object>) options.get(keyi);
				
				String code = option.get("code") == null ? "" : String.valueOf(option.get("code"));
				
				String sqlOpt = "SELECT 'Y' FROM PS_TZ_APPXXX_KXZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_BH = ? AND TZ_XXXKXZ_MC = ?";
				String isSameOpt = sqlQuery.queryForObject(sqlOpt, new Object[] { this.tid, itemId,code }, "String");
				if (StringUtils.equals("Y", isSameOpt)) {
					continue;
				}
				
				PsTzAppXxxKxzT psTzAppXxxKxzT = new PsTzAppXxxKxzT();
				// 报名表模板编号
				psTzAppXxxKxzT.setTzAppTplId(this.tid);
				psTzAppXxxKxzT.setTzXxxBh(itemId);

				// 可选值编码
				psTzAppXxxKxzT.setTzXxxkxzMc(code);

				// 排序序号
				if (option.containsKey("orderby")) {
					String order = "" + option.get("orderby");
					psTzAppXxxKxzT.setTzOrder(Integer.parseInt(order));
				}

				// 可选值描述
				if (option.containsKey("txt")) {
					psTzAppXxxKxzT.setTzXxxkxzMs((String) option.get("txt"));
				}

				// 是否为默认
				if (option.containsKey("defaultval")) {
					psTzAppXxxKxzT.setTzKxzMrzBz((String) option.get("defaultval"));
				}

				// 是否为其他
				if (option.containsKey("other")) {
					psTzAppXxxKxzT.setTzKxzQtBz((String) option.get("other"));
				}

				// 权重
				if (option.containsKey("weight")) {
					String weight = "" + option.get("weight");
					psTzAppXxxKxzT.setTzXxxkxzQz(new BigDecimal(weight));
				}
				psTzAppXxxKxzTMapper.insert(psTzAppXxxKxzT);
			}
		}
		/*------- 是否Option End -------*/

		/*------- 是否Events Begin -------*/
		if (item.containsKey("rules")) {
			Map<String, Object> rules = (Map<String, Object>) item.get("rules");
			for (String key : rules.keySet()) {
				Map<String, Object> rule = (Map<String, Object>) rules.get(key);
				PsTzAppXxJygzT psTzAppXxJygzT = new PsTzAppXxJygzT();
				// 模板编号
				psTzAppXxJygzT.setTzAppTplId(this.tid);

				// 信息项编号
				psTzAppXxJygzT.setTzXxxBh(itemId);
				// if (rule.containsKey("itemId")) {
				// psTzAppXxJygzT.setTzXxxBh((String) rule.get("itemId"));
				// }

				// 校验规则ID
				if (rule.containsKey("ruleId")) {
					psTzAppXxJygzT.setTzJygzId((String) rule.get("ruleId"));
				}

				// 是否启用
				if (rule.containsKey("isEnable")) {
					psTzAppXxJygzT.setTzQyBz((String) rule.get("isEnable"));
				}

				// 提示信息
				if (rule.containsKey("messages")) {
					psTzAppXxJygzT.setTzJygzTsxx((String) rule.get("messages"));
				}
				psTzAppXxJygzTMapper.insert(psTzAppXxJygzT);
			}
		}

		/*------- 是否Events End -------*/

		/*------- 信息项同步配置 Begin -------*/
		if (item.containsKey("syncRule")) {
			Map<String, Object> syncRules = (Map<String, Object>) item.get("syncRule");
			for (String key : syncRules.keySet()) {
				Map<String, Object> syncRule = (Map<String, Object>) syncRules.get(key);
				PsTzAppXxSyncT psTzAppXxSyncT = new PsTzAppXxSyncT();
				psTzAppXxSyncT.setTzAppTplId(this.tid);
				psTzAppXxSyncT.setTzXxxBh(itemId);
				// 是否启用
				if (syncRule.containsKey("isEff")) {
					psTzAppXxSyncT.setTzQyBz((String) syncRule.get("isEff"));
				}

				// 同步类型
				if (syncRule.containsKey("syncType")) {
					psTzAppXxSyncT.setTzSyncType((String) syncRule.get("syncType"));
				}

				// 同步顺序
				if (syncRule.containsKey("syncOrder")) {
					String order = "" + syncRule.get("syncOrder");
					if (StringUtils.isBlank(order)) {
						order = "0";
					}
					psTzAppXxSyncT.setTzSyncOrder(Integer.parseInt(order));
				}

				// 分隔符
				if (syncRule.containsKey("syncSep")) {
					psTzAppXxSyncT.setTzSyncSep((String) syncRule.get("syncSep"));
				}

				psTzAppXxSyncTMapper.insert(psTzAppXxSyncT);
			}
		}
		/*------- 信息项同步配置 End -------*/
		String sql = "SELECT 'Y' FROM PS_TZ_APP_XXXPZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_BH = ?";
		String isSame = sqlQuery.queryForObject(sql, new Object[] { this.tid, itemId }, "String");
		if (StringUtils.equals("Y", isSame)) {
			return;
		}
		psTzAppXxxPzTMapper.insert(psTzAppXxxPz);

	}

	private void delTpl(String tid) {
		try {
			// 报名表信息表
			String xxxpzSql = "DELETE FROM PS_TZ_APP_XXXPZ_T WHERE TZ_APP_TPL_ID = ?";
			sqlQuery.update(xxxpzSql, new Object[] { tid });

			// 报名表选项值定义表
			String kxzSql = "DELETE FROM PS_TZ_APPXXX_KXZ_T WHERE TZ_APP_TPL_ID = ?";
			sqlQuery.update(kxzSql, new Object[] { tid });

			// 报名表校验规则定义表
			String jygzSql = "DELETE FROM PS_TZ_APPXX_JYGZ_T WHERE TZ_APP_TPL_ID = ?";
			sqlQuery.update(jygzSql, new Object[] { tid });

			// 报名表容器信息表
			String rqxxxpzSql = "DELETE FROM PS_TZ_RQ_XXXPZ_T WHERE TZ_APP_TPL_ID = ?";
			sqlQuery.update(rqxxxpzSql, new Object[] { tid });

			// 报名表模板扁平化表
			String tempSql = "DELETE FROM PS_TZ_TEMP_FIELD_T WHERE TZ_APP_TPL_ID = ?";
			sqlQuery.update(tempSql, new Object[] { tid });

			// 信息项同步规则表
			String syncSql = "DELETE FROM PS_TZ_APPXX_SYNC_T WHERE TZ_APP_TPL_ID = ?";
			sqlQuery.update(syncSql, new Object[] { tid });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* 获取模板设计页面HTML */
	public String init(String tplId, String insId) {
		JacksonUtil jacksonUtil = new JacksonUtil();

		String sql = "SELECT TZ_APP_TPL_MC FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ?";
		String tplName = sqlQuery.queryForObject(sql, new Object[] { tplId }, "String");
		// 1、读取控件注册信息，显示报名表模板编辑页面左侧工具条;
		String componentData = this.getComponentData(tplId);

		/*
		 * 2、读取报名表模板配置信息，若有配置信息，则根据控件的JS类路径加载JS文件（存在则不再重复加载），把控件类存储到类管理器中，
		 * 并调用类的初始化方法_init， 控件类调用中_gethtml生成控件实例,传递参数
		 * id、data（控件的属性数据集,包含配置数据），将生成的控件实例存入类实例管理器中（_data）
		 * 
		 * 3、点击、拖拽报名表左侧工具条中的控件按钮，加载JS类、生成实例
		 * 
		 * 4、点击右侧区域的控件实例，调用其类中的_edit方法，传递参数 id、data参数,显示编辑页面；
		 * 
		 * 5、控件编辑页面修改字段的值后，需要将值立即同步到类实例对象中 若属性字段为level0级别的，则属性编辑框需要添加键盘事件
		 * onkeyup=“SurveyBuild.saveAttr('id','attrName',val)”；
		 * 若属性字段为level1级别的字段，则属性编辑框需要添加键盘事件
		 * onkeyup=“SurveyBuild.saveLevel1Attr('id','attrName1'，'attrName2',val)
		 * ”；
		 * 若上述两个字段不能满足赋值要求，则可调用SurveyBuild.getComponentInstance(ID)获取控件实例，在进行赋值
		 * 若属性字段值修改后需要同步修改右侧控件预览信息，则需要控件自行实现
		 * 
		 * 6、离开当前编辑页面（添加控件实例、修改其他控件实例）、保存当前模板时，需调用_validator方法校验当前编辑的控件是否校验通过，
		 * 若不通过则不能进行当前操作；
		 * 
		 * 7、校验规则的列表、设置页面、设置页面参数设置到_data中、校验列表页面参数赋值到_data中;
		 * 
		 * 8、保存当前控件时会把类实例对象中的_data数据集传递到后台进行保存;
		 */

		String contextUrl = request.getContextPath();
		String tzGeneralURL = contextUrl + "/dispatcher";

		String sqlLang = "SELECT TZ_APP_TPL_LAN FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ?";
		String language = sqlQuery.queryForObject(sqlLang, new Object[] { tplId }, "String");

		String msgSet = gdObjectServiceImpl.getMessageSetByLanguageCd(request, response, "TZGD_APPONLINE_MSGSET",
				language);
		jacksonUtil.json2Map(msgSet);
		if (jacksonUtil.containsKey(language)) {
			Map<String, Object> msgLang = jacksonUtil.getMap(language);
			msgSet = jacksonUtil.Map2json(msgLang);
		}

		String tplHtml = "";

		componentData = componentData.replace("\"", "\\\\\"");
		try {
			tplHtml = tzGdObject.getHTMLText("HTML.TZApplicationTemplateBundle.TZ_TEMPLATE_HTML", true,
					request.getContextPath(), tplName, tplId, componentData, tzGeneralURL, msgSet, contextUrl);
		} catch (TzSystemException e) {
			e.printStackTrace();
			tplHtml = "";
		}

		return tplHtml;
	}

	/*
	 * 获取当前模板对应的控件列表以及模板的基本信息以及信息项
	 */
	private String getComponentData(String tplId) {
		JacksonUtil jacksonUtil = new JacksonUtil();

		ArrayList<Map<String, Object>> comDfn = this.getComDfn(tplId);

		String sqlTplData = "SELECT TZ_APPTPL_JSON_STR FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ?";
		String tplData = sqlQuery.queryForObject(sqlTplData, new Object[] { tplId }, "String");
		if (StringUtils.isBlank(tplData)) {
			tplData = "{}";
		}
		jacksonUtil.json2Map(tplData);
		Map<String, Object> comData = new HashMap<String, Object>();
		comData.put("componentDfn", comDfn);
		comData.put("componentInstance", jacksonUtil.getMap());

		return jacksonUtil.Map2json(comData);
	}

	/*
	 * 获取模板相关控件
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Map<String, Object>> getComDfn(String tplId) {
		String contextUrl = request.getContextPath();

		String sqlLang = "SELECT TZ_APP_TPL_LAN FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ?";
		String language = sqlQuery.queryForObject(sqlLang, new Object[] { tplId }, "String");

		String sql = "SELECT TZ_APP_TPL_YT FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ?";
		String tplYt = sqlQuery.queryForObject(sql, new Object[] { tplId }, "String");

		String sqlYt = "SELECT * FROM PS_TZ_COM_YT_T WHERE TZ_COM_YT_ID = ? AND TZ_QY_BZ = 'Y' ORDER BY TZ_ORDER ASC";
		List<?> resultlist = sqlQuery.queryForList(sqlYt, new Object[] { tplYt });

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		for (Object obj : resultlist) {
			Map<String, Object> result = (Map<String, Object>) obj;
			String componentId = result.get("TZ_COM_ID") == null ? "" : String.valueOf(result.get("TZ_COM_ID"));
			PsTzComDyT psTzComDyT = psTzComDyTMapper.selectByPrimaryKey(componentId);

			String componentName = psTzComDyT.getTzComMc();
			String displayArea = result.get("TZ_COM_LX_ID") == null ? "" : String.valueOf(result.get("TZ_COM_LX_ID"));
			String jsfileUrl = contextUrl + psTzComDyT.getTzComJslj();
			String[] arrLj = jsfileUrl.split("/");
			String className = StringUtils.substringBeforeLast(arrLj[arrLj.length - 1], ".js");
			String iconPath = contextUrl + psTzComDyT.getTzComIconlj();

			Map<String, Object> mapOptJson = new HashMap<String, Object>();
			mapOptJson.put("componentId", componentId);
			mapOptJson.put("componentName", componentName);
			mapOptJson.put("displayArea", displayArea);
			mapOptJson.put("jsfileUrl", jsfileUrl);
			mapOptJson.put("className", className);
			mapOptJson.put("iconPath", iconPath);

			String sqlRules = "SELECT * FROM PS_TZ_COM_JYGZPZ_T WHERE TZ_COM_ID = ? ORDER BY TZ_ORDER ASC";
			List<?> rulelist = sqlQuery.queryForList(sqlRules, new Object[] { componentId });
			Map<String, Object> mapRuleRet = new HashMap<String, Object>();

			for (Object rule : rulelist) {
				Map<String, Object> rul = (Map<String, Object>) rule;
				String ruleId = rul.get("TZ_JYGZ_ID") == null ? "" : String.valueOf(rul.get("TZ_JYGZ_ID"));
				PsTzJygzDyT psTzJygzDyT = psTzJygzDyTMapper.selectByPrimaryKey(ruleId);

				String ruleName = psTzJygzDyT.getTzJygzMc();
				String className1 = psTzJygzDyT.getTzJygzJslmc();
				String messages = psTzJygzDyT.getTzJygzTsxx();
				if (!StringUtils.equals(language, "ZHS")) {
					PsTzJygzDyEngKey psTzJygzDyEngKey = new PsTzJygzDyEngKey();
					psTzJygzDyEngKey.setLanguageCd(language);
					psTzJygzDyEngKey.setTzJygzId(ruleId);
					PsTzJygzDyEng psTzJygzDyEng = psTzJygzDyEngMapper.selectByPrimaryKey(psTzJygzDyEngKey);
					messages = psTzJygzDyEng.getTzJygzTsxx();
				}
				String isEnable = rul.get("TZ_QY_BZ") == null ? "" : String.valueOf(rul.get("TZ_QY_BZ"));

				Map<String, Object> ruleJson = new HashMap<String, Object>();
				ruleJson.put("ruleId", ruleId);
				ruleJson.put("ruleName", ruleName);
				ruleJson.put("className", className1);
				ruleJson.put("isEnable", isEnable);
				ruleJson.put("messages", messages);
				mapRuleRet.put(className1, ruleJson);
			}
			mapOptJson.put("rules", mapRuleRet);
			listData.add(mapOptJson);
		}
		return listData;
	}
}
