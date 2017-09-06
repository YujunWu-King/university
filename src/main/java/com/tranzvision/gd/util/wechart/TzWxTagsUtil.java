package com.tranzvision.gd.util.wechart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.httpclient.HttpClientService;

/**
 * 微信标签相关操作API
 * @author zhanglang
 *
 */
public class TzWxTagsUtil {
	//创建标签http请求URL
	private static final String tz_createTag_url = "https://api.weixin.qq.com/cgi-bin/tags/create";
	
	//获取已创建标签http请求URL
	private static final String tz_getTags_url = "https://api.weixin.qq.com/cgi-bin/tags/get";
	
	//修改标签http请求URL
	private static final String tz_updateTag_url = "https://api.weixin.qq.com/cgi-bin/tags/update";
	
	//删除标签http请求URL
	private static final String tz_deleteTag_url = "https://api.weixin.qq.com/cgi-bin/tags/delete";
	
	//获取标签下粉丝列表http请求URL
	private static final String tz_getTagUsers_url = "https://api.weixin.qq.com/cgi-bin/user/tag/get";
	
	//批量为用户打标签http请求URL
	private static final String tz_batchtagging_url = "https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging";
	
	//批量为用户取消标签http请求URL
	private static final String tz_batchUntagging_url = "https://api.weixin.qq.com/cgi-bin/tags/members/batchuntagging";
	
	//获取用户身上的标签列表http请求URL
	private static final String tz_getUserTags_url = "https://api.weixin.qq.com/cgi-bin/tags/getidlist";
	

	
	/**
	 * 创建标签
	 * @param access_token	调用接口凭据
	 * @param tagName	标签名（30个字符以内）
	 * @return
	 * @throws TzException
	 */
	public static String createTag(String access_token, String tagName) throws TzException {
		String returnStr = "";
		try{
			Map<String, String> tagMap = new HashMap<String, String>();
			tagMap.put("name", tagName);
			
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("tag", tagMap);
			
			String createTagUrl = tz_createTag_url + "?access_token=" + access_token;
			//http请求
			HttpClientService HttpClientService = new HttpClientService(createTagUrl,"POST",paramsMap,"UTF-8");
			returnStr = HttpClientService.sendRequest();
		}catch(Exception e){
			throw new TzException("https请求失败："+e.getMessage());
		}
		return returnStr;
	}
	
	
	
	/**
	 * 获取公众号已创建的标签
	 * @param access_token 	调用接口凭据
	 * @return
	 * @throws TzException
	 */
	public static String getAllTags(String access_token) throws TzException {
		String returnStr = "";
		try{
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("access_token", access_token);
			
			//http请求
			HttpClientService HttpClientService = new HttpClientService(tz_getTags_url,"GET",paramsMap,"UTF-8");
			returnStr = HttpClientService.sendRequest();
		}catch(Exception e){
			throw new TzException("https请求失败："+e.getMessage());
		}
		return returnStr;
	}
	
	
	
	/**
	 * 编辑标签
	 * @param access_token	调用接口凭据
	 * @param tagId		标签ID
	 * @param tagName	标签名称
	 * @return
	 * @throws TzException
	 */
	public static String editTagNameByTagID(String access_token, String tagId, String tagName) throws TzException {
		String returnStr = "";
		try{
			Map<String, String> tagMap = new HashMap<String, String>();
			tagMap.put("id", tagId);
			tagMap.put("name", tagName);
			
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("tag", tagMap);
			
			String updateTagUrl = tz_updateTag_url + "?access_token=" + access_token;
			//http请求
			HttpClientService HttpClientService = new HttpClientService(updateTagUrl,"POST",paramsMap,"UTF-8");
			returnStr = HttpClientService.sendRequest();
		}catch(Exception e){
			throw new TzException("https请求失败："+e.getMessage());
		}
		return returnStr;
	}
	
	
	
	
	/**
	 * 删除标签
	 * @param access_token	调用接口凭据
	 * @param tagId			标签ID
	 * @return
	 * @throws TzException
	 */
	public static String deleteTagByTagID(String access_token, String tagId) throws TzException {
		String returnStr = "";
		try{
			Map<String, String> tagMap = new HashMap<String, String>();
			tagMap.put("id", tagId);
			
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("tag", tagMap);
			
			String deleteTagUrl = tz_deleteTag_url + "?access_token=" + access_token;
			//http请求
			HttpClientService HttpClientService = new HttpClientService(deleteTagUrl,"POST",paramsMap,"UTF-8");
			returnStr = HttpClientService.sendRequest();
		}catch(Exception e){
			throw new TzException("https请求失败："+e.getMessage());
		}
		return returnStr;
	}
	
	
	
	
	/**
	 * 获取标签下粉丝列表
	 * @param access_token		调用接口凭据
	 * @param tagId				标签ID
	 * @param nextOpenid		第一个拉取的OPENID，不填默认从头开始拉取
	 * @return
	 * @throws TzException
	 */
	public static String getTagUserList(String access_token, String tagId, String nextOpenid) throws TzException {
		String returnStr = "";
		try{
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("access_token", access_token);
			paramsMap.put("tagid", tagId);
			
			if(nextOpenid != null && !"".equals(nextOpenid)){
				paramsMap.put("next_openid", nextOpenid);
			}
	
			//http请求
			HttpClientService HttpClientService = new HttpClientService(tz_getTagUsers_url,"GET",paramsMap,"UTF-8");
			returnStr = HttpClientService.sendRequest();
		}catch(Exception e){
			throw new TzException("https请求失败："+e.getMessage());
		}
		return returnStr;
	}
	
	
	
	
	/**
	 * 批量为用户打标签
	 * @param access_token		调用接口凭据
	 * @param tagId				标签ID
	 * @param openIds			需要被打标签的OPENID List
	 * @return
	 * @throws TzException
	 */
	public static String batchTagging(String access_token, String tagId, List<String> openIds) throws TzException {
		String returnStr = "";
		try{
			if(openIds == null){
				new TzException("需要打标签的用户OPENID为空");
			}
			
			if(openIds.size() > 50){
				new TzException("每次传入的openid列表个数不能超过50个");
			}
			
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("tagid", tagId);
			paramsMap.put("openid_list", openIds);
			
			String batchtaggingUrl = tz_batchtagging_url + "?access_token=" + access_token;
			//http请求
			HttpClientService HttpClientService = new HttpClientService(batchtaggingUrl,"POST",paramsMap,"UTF-8");
			returnStr = HttpClientService.sendRequest();
		}catch(Exception e){
			throw new TzException("https请求失败："+e.getMessage());
		}
		return returnStr;
	}
	
	
	
	/**
	 * 批量为用户取消标签
	 * @param access_token		调用接口凭据
	 * @param tagId				标签ID
	 * @param openIds			需要被打标签的OPENID List
	 * @return
	 * @throws TzException
	 */
	public static String batchUnTagging(String access_token, String tagId, List<String> openIds) throws TzException {
		String returnStr = "";
		try{
			if(openIds == null){
				new TzException("需要打标签的用户OPENID为空");
			}
			
			if(openIds.size() > 50){
				new TzException("每次传入的openid列表个数不能超过50个");
			}
			
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("tagid", tagId);
			paramsMap.put("openid_list", openIds);
			
			String batchUntaggingUrl = tz_batchUntagging_url + "?access_token=" + access_token;
			//http请求
			HttpClientService HttpClientService = new HttpClientService(batchUntaggingUrl,"POST",paramsMap,"UTF-8");
			returnStr = HttpClientService.sendRequest();
		}catch(Exception e){
			throw new TzException("https请求失败："+e.getMessage());
		}
		return returnStr;
	}
	
	
	
	
	
	/**
	 * 获取用户身上的标签列表
	 * @param access_token		调用接口凭据
	 * @param openId			微信用户OPENID
	 * @return
	 * @throws TzException
	 */
	public static String getUserTags(String access_token, String openId) throws TzException {
		String returnStr = "";
		try{
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("openid", openId);
			
			String getUserTagsUrl = tz_getUserTags_url + "?access_token=" + access_token;
			//http请求
			HttpClientService HttpClientService = new HttpClientService(getUserTagsUrl,"POST",paramsMap,"UTF-8");
			returnStr = HttpClientService.sendRequest();
		}catch(Exception e){
			throw new TzException("https请求失败："+e.getMessage());
		}
		return returnStr;
	}

}
