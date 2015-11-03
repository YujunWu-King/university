package com.tranzvision.gd.TZBaseBundle.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author tang
 * 2015-10-23
 * 功能说明：高端产品-招生系统框架通用类
 */
public interface GdKjComService {
	/**
	 * 获取框架级错误、警告、提示等描述信息的方法
	 * 该方法使得框架级错误、警告、提示等描述信息可以通过可配置的方法实现，而不是在mainapp.js程序中写死
	 *****/
	public String getFrameworkDescriptionResources(HttpServletRequest request,
			HttpServletResponse response);
	
	/******************************************************************
	 *** 开发时间：2015-09-30 开发人：tang 控件标签 功能说明：根据标签ID获取对应的标签描述，若未找到对应值，则添加
	 * 一条数据到数据库，标签描述为传入的默认标签描述。 参数说明：sCID 标签ID sDefaultText 默认标签内容
	 *******************************************************************/
	public String getFieldTag(HttpServletRequest request,
			HttpServletResponse response, String sCID, String sDefaultText,
			String[] errMsgArr);
	
	/* 根据字段名称获取TransValue值 */
	public String getTransValue(HttpServletRequest request, HttpServletResponse response,String sFieldName) ;
	
	/* 功能说明：根据组件ID获取该组件下所有页面使用到的资源 */
	public String getComResourses(HttpServletRequest request, HttpServletResponse response, String sComID) throws Exception;

	/* 用户请求操作分发器逻辑 */
	public String userRequestDispatcher(HttpServletRequest request,
			HttpServletResponse response, String sComID, String sPageID,
			String strOprType, String comParams, String[] errMsgArr);
	
	/* 功能说明：根据搜索条件获取满足条件中的数据，供放大镜使用
	 * 参数说明：&recname 搜索表或视图名称 &condition 搜索条件 &result 搜索结果字段 &maxRow 搜索最大行*/
	public String getPromptSearchList(String recname, String condition,
			String result, String maxRow, int numLimit, int numStart,
			String[] errMsgArr);
	
	/*下拉框数据*/
	public String getComboxValue(String recname , String condition,  String result,String[] errMsgArr );
	
	/*获取指定组件页面的访问授权信息*/
	public String getComAuthorizedInfo(String sUserId,String sComID);
}
