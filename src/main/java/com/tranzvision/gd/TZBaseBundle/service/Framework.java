package com.tranzvision.gd.TZBaseBundle.service;

/**
 * @author ： tang
 * 框架通用接口
 * 统一描述：以下各方法中的String[] errMsg是执行方法时的错误提示信息；
 * 			 同原&errorCode As number out, &strErrorDesc As string out
 * 			 原&errorCode = 1，&strErrorDesc="错误描述"; 改为errMsg[0]="1",errMsg[1]="错误描述";
 */
public interface Framework {
	/*actData:提交过来的json字符串数组，和原PS tzAdd中的actData参数一致;*/
	public String tzAdd(String[] actData, String[] errMsg);
	
	/*actData:提交过来的json字符串数组，和原PS tzUpdate中的actData参数一致;*/
	public String tzUpdate(String[] actData, String[] errMsg);
	
	/*actData:提交过来的json字符串数组，和原PS tzDelete方法中的actData参数一致;*/
	public String tzDelete(String[] actData, String[] errMsg);

	/* strParams:查询提交的json字符串，和原PS tzQuery方法中的comParams参数一致;*/
	public String tzQuery(String strParams, String[] errMsg);
	
	/* strParams:查询提交的json字符串，和原PS tzQueryList方法中的comParams参数一致;
	 * numLimit： 查询限制的条数，0表示不限制
	 * numStart： 查询从第几条开始
	 * */
	public String tzQueryList(String strParams,int numLimit, int numStart, String[] errorMsg);
	
	/* 
	 * oprType：操作提交的类型，和原PS tzOther方法中的oprType参数一致
	 * strParams:操作提交的json字符串，和原PS tzOther方法中的comParams参数一致;
	 * */
	public String tzOther(String oprType, String strParams, String[] errorMsg);
	
	/*strParams:提交过来的json字符串数组，和原PS tzGetHtmlContent的strParams参数一致;*/
	public String tzGetHtmlContent(String strParams);
	
	/*strParams:提交过来的json字符串数组，和原PS tzGetHtmlData的strParams参数一致;*/
	public String tzGetHtmlData(String strParams);
	
	public String tzGetJsonData(String strParams);
}
