package com.tranzvision.gd.TZBusProNotice.controller;

import com.tranzvision.gd.WorkflowActionsBundle.service.impl.WorkFlowPublicImpl;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.workflow.base.ErrorMessage;
import com.tranzvision.gd.workflow.engine.TzWorkflow;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 工作流邮件审批处理类
 * @author 张浪
 * 2019年6月28日
 *
 */
@Controller
@RequestMapping(value = "/wflEmailHandler")
public class TzWorkflowForEmailController {
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private WorkFlowPublicImpl workFlowPublicImpl;

	
	
	/**
	 * 邮件工作流表单处理方法
	 * @author 张浪，2019-06-28
	 * @param request
	 * @param response
	 * @param wflInsId	工作流实例ID
	 * @param stpInsId	步骤实例ID
	 * @param userId	用户ID
	 * @return
	 */
	@RequestMapping(value = { "/wflFormUrl/{wflInsId}/{stpInsId}/{userId}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public ModelAndView wflFormUrlHandler(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable(value = "wflInsId") String wflInsId, 
			@PathVariable(value = "stpInsId") String stpInsId, 
			@PathVariable(value = "userId") String userId) {
		String errorMsg = "";
		if(StringUtils.isNotBlank(wflInsId) 
				&& StringUtils.isNotBlank(stpInsId) 
				&& StringUtils.isNotBlank(userId)) {
			
			String entityName = sqlQuery.queryForObject("select B.tzms_entity_name from tzms_wflins_tbl A LEFT JOIN tzms_wfcldn_tBase B ON A.tzms_wfcldn_uniqueid = B.tzms_wfcldn_tid where A.tzms_wflinsid=? ",  
					new Object[] { wflInsId }, "String");
			// 业务流程实例--业务数据ID
			String entityId = sqlQuery.queryForObject("select tzms_wflrecord_uniqueid from tzms_wflins_tbl where tzms_wflinsid=?", 
					new Object[] { wflInsId }, "String");
			// 业务流程步骤 ID
			String tzms_wflstp_tid = sqlQuery.queryForObject("select tzms_wflstpid from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", 
					new Object[] { wflInsId , stpInsId },"String");
			
			//构建工作流表单url
			String formUrl = workFlowPublicImpl.getWflinsWindowsURL(entityName, tzms_wflstp_tid, entityId, wflInsId, stpInsId);
			
			if(!"".equals(formUrl)) {
				//需要新对工作流任务进行签收
				TzWorkflow tzWorkflow = new TzWorkflow(userId);
				try {
					//流程初始化
					tzWorkflow.CreateWorkflow1(wflInsId, stpInsId);
					
					//失败信息
					ErrorMessage ErrorMsg = new ErrorMessage();
					//工作流签收
					tzWorkflow.WorkflowSign(ErrorMsg);
				} catch (TzException e) {
					e.printStackTrace();
				}
				
				try {
					response.sendRedirect(formUrl);
				} catch (IOException e) {
					e.printStackTrace();
					errorMsg = "打开业务流程表单页面失败";
				}
			}else {
				errorMsg = "业务流程表单链接错误，请您链接系统管理员解决";
			}
		} else {
			errorMsg = "参数不合法";
		}
		
		ModelAndView mv = new ModelAndView("errorPage");
	    mv.addObject("errorMessage", errorMsg);
	    return mv;
	}
	
	
	
	
	/**
	 * 工作流邮件审批
	 * @author 张浪，2019-06-28
	 * @param request
	 * @param response
	 * @param type		请求类型，SUB-提交， AGR-同意，REJ-拒绝
	 * @param wflInsId	工作流实例ID
	 * @param stpInsId	步骤实例ID
	 * @param userId	用户ID
	 * @return
	 */
	@RequestMapping(value = { "/wflEmailApprove/{type}/{wflInsId}/{stpInsId}/{userId}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public ModelAndView emailWflBtnHandler(HttpServletRequest request,HttpServletResponse response,
			@PathVariable(value = "type") String type, 
			@PathVariable(value = "wflInsId") String wflInsId, 
			@PathVariable(value = "stpInsId") String stpInsId, 
			@PathVariable(value = "userId") String userId) {
		
		String message = "";
		if("SUB".equals(type) 
				|| "AGR".equals(type) 
				|| "REJ".equals(type)) {
			if(StringUtils.isNotBlank(wflInsId) 
					&& StringUtils.isNotBlank(stpInsId) 
					&& StringUtils.isNotBlank(userId)) {
				try {
					//初始化工作流
					TzWorkflow workflowInstance = new TzWorkflow(userId);
					//流程初始化
					boolean createOK = workflowInstance.CreateWorkflow1(wflInsId, stpInsId);
					if(createOK) {
						
						//首先进行签收
						try {
							//失败信息
							ErrorMessage ErrorMsg = new ErrorMessage();
							//工作流签收
							boolean wflSign = workflowInstance.WorkflowSign(ErrorMsg);
							if(wflSign == true) {
								if("SUB".equals(type) || "AGR".equals(type)) {
									try {
										boolean isOnlyPath = workflowInstance.NextActionIsOnlyPath();
										if(isOnlyPath == true) {
											//错误信息
											ErrorMessage ErrorMsg_OUT = new ErrorMessage();
											
											boolean submitOK = false;
											if("SUB".equals(type)) {
												submitOK = workflowInstance.WorkflowSubmit("", ErrorMsg_OUT);
											}else {
												submitOK = workflowInstance.WorkflowAgree("", ErrorMsg_OUT);
											}
											
											if(submitOK == true){
							    			  	//提交成功
												message = "您已审批成功";
							    			}else{
							    				//提交失败
							    				message = ErrorMsg_OUT.getErrorMsg();
							    			}
										}else {
											message = "下一步审批流程路径不唯一，请从业务流程表单中进行审批";
										}
									} catch (TzException e) {
										e.printStackTrace();
										message = e.getMessage();
									}
								}else {
									//错误信息
									ErrorMessage ErrorMsg_OUT = new ErrorMessage();
									
									boolean rejectOk = workflowInstance.WorkflowReject("", ErrorMsg_OUT);
									if(rejectOk) {
										message = "拒绝成功";
									}else {
										message = ErrorMsg_OUT.getErrorMsg();
									}
								}
							}else {
								message = ErrorMsg.getErrorMsg();
							}
						}catch (Exception e) {
							e.printStackTrace();
							message = e.getMessage();
						}
					}
				}catch (Exception e) {
					e.printStackTrace();
					message = "业务流程审批失败";
				}
			}else {
				message = "参数错误";
			}
		}else {
			message = "参数错误";
		}
		
		ModelAndView mv = new ModelAndView("errorPage");
        mv.addObject("errorMessage", message);
		return mv;
	}
}
