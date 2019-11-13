package com.tranzvision.gd.workflow.controller;

import com.tranzvision.gd.util.sql.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/workflow")
public class TzWorkflowController {

	@Autowired
	private SqlQuery sqlQuery;
	
	
	
	/**
	 * 获取业务流程实例状态
	 * @param request
	 * @param response
	 * @param wflInsId
	 * @return
	 */
	@RequestMapping(value = { "wfl/{wflInsId}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getWorkflowStatus(HttpServletRequest request,HttpServletResponse response, 
			@PathVariable(value = "wflInsId") String wflInsId) {
		String status = "";
		status = sqlQuery.queryForObject("select tzms_wflstatus from tzms_wflins_tbl where tzms_wflinsid=?", 
				new Object[] { wflInsId }, "String");

		return status;
	}
	
	
	/**
	 * 获取业务流程步骤实例状态
	 * @param request
	 * @param response
	 * @param wflInsId
	 * @param stpInsId
	 * @return
	 */
	@RequestMapping(value = { "stp/{wflInsId}/{stpInsId}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getWorkflowStepStatus(HttpServletRequest request,HttpServletResponse response, 
			@PathVariable(value = "wflInsId") String wflInsId, @PathVariable(value = "stpInsId") String stpInsId) {
		String status = "";
		status = sqlQuery.queryForObject("select tzms_stpinsstat from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", 
				new Object[] { wflInsId, stpInsId }, "String");

		return status;
	}
}
