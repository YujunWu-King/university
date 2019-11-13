package com.tranzvision.gd.TZThirdPartyManage.service;

/**
 * @Auther: ZY
 * @Date: 2018/11/27 09:47
 * @update: 2019-03-26 张浪修改
 * @Description: 系统集成第三方系统的统一操作接口
 *                  jsonObject统一传入jsonObject字符串传入自定义参数
 *                  erroeMsg：errMsg[0]="1",errMsg[1]="错误描述"
 */
public interface ThirdPartyBase {

	
	/**
	 * 搜索第三方账号
	 * @param osysId		系统编号
	 * @param oprid			用户ID
	 * @param searchText	搜索关键词
	 * @param errorMsg
	 * @return
	 */
	public String searchAccount(String osysId, String oprid, String searchText, String[] errorMsg);
	
	
	
	
	/**
	 * 绑定账号
	 * @param osysId	系统编号
	 * @param oprid		用户ID
	 * @param jsonParams	绑定参数
	 * @param errorMsg
	 * @return
	 */
	public boolean bindAccount(String osysId, String oprid, String jsonParams, String[] errorMsg);

	
	
    /**
     * 创建第三方用户
     * @param osysId		系统编号
	 * @param oprid			用户ID
     * @param account	账号
     * @param password	密码
     * @param name		姓名
     * @param errorMsg
     * @return
     */
    public boolean createAccount(String osysId, String oprid, String account, String password, String name, String[] errorMsg);

    
    
    /**
	 *  解除账号绑定
	 * @param osysId
	 * @param oprid
	 * @param jsonParams
	 * @param errorMsg
	 * @return
	 */
	public boolean unBindAccount(String osysId, String oprid, String account, String[] errorMsg);
	
	
	
	
    /**
     * 锁定账号
     * @param osysId		系统编号
	 * @param oprid			用户ID
     * @param osysId
     * @param account
     * @param errorMsg
     * @return
     */
    public boolean lockAccount(String osysId, String oprid, String account, String[] errorMsg);



    /**
     * 解锁账号
     * @param osysId		系统编号
	 * @param oprid			用户ID
     * @param account
     * @param errorMsg
     * @return
     */
    public boolean unLockAccount(String osysId, String oprid, String account, String[] errorMsg);


    /**
     * 注销账号
     * @param osysId		系统编号
	 * @param oprid			用户ID
     * @param account
     * @param errorMsg
     * @return
     */
    public boolean logoutAccount(String osysId, String oprid, String account, String[] errorMsg);

}
